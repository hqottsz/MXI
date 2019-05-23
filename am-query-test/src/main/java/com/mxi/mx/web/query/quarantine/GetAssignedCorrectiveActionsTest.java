package com.mxi.mx.web.query.quarantine;

import static org.junit.Assert.assertEquals;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InvCndChgEventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.QuarActionAssignmentKey;
import com.mxi.mx.core.key.QuarActionKey;
import com.mxi.mx.core.key.QuarActionStatusKey;
import com.mxi.mx.core.key.QuarQuarKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefQuarActionCategoryKey;
import com.mxi.mx.core.key.RefQuarActionStatusKey;
import com.mxi.mx.core.key.RefStageReasonKey;
import com.mxi.mx.core.table.acevent.InvCndChgEventDao;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.quarantine.QuarActionAssignmentTable;
import com.mxi.mx.core.table.quarantine.QuarActionStatusTable;
import com.mxi.mx.core.table.quarantine.QuarActionTable;
import com.mxi.mx.core.table.quarantine.QuarQuarTable;


/**
 * This test class tests the GetAssignedCorrectiveActions.qrx It does not test the services that are
 * used in this test (services are used for setting up of data for the test).
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class GetAssignedCorrectiveActionsTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   public static final String NOTE = "User note";
   public static final String DISCREPANCY_DESCRIPTION = "Discrepancy Description";

   private HumanResourceKey iHumanResource;
   private LocationKey iSupplyLocation;
   private OwnerKey iLocalOwner;


   @Before
   public void setUp() {

      iSupplyLocation = Domain.createLocation( aSupplyLocation -> {
         aSupplyLocation.setCode( "YYZ" );
         aSupplyLocation.setIsSupplyLocation( true );
      } );

      iLocalOwner = Domain.createOwner();

      iHumanResource = Domain.createHumanResource( aHr -> {
         aHr.setUser( Domain.createUser() );
         aHr.setSupplyLocations( iSupplyLocation );

      } );
   }


   /**
    * Given an open quarantined corrective action, when the query GetAssignedCorrectiveActions.qrx
    * is executed for hr who is responsible for that action, then that open corrective action is
    * returned.
    */
   @Test
   public void testOpenQuarantinedCorrectiveActionsRetrieved() {

      // ARRANGE
      RefQuarActionCategoryKey lRefQuarActionCategoryKey =
            new RefQuarActionCategoryKey( "10:OTHER" );

      final InventoryKey lInventory = Domain.createSerializedInventory( trackedInventory -> {
         trackedInventory.setCondition( RefInvCondKey.QUAR );
         trackedInventory.setOwner( iLocalOwner );
         trackedInventory.setPartNumber( Domain.createPart() );
      } );

      InvCndChgEventDao lInvCndChgEventDao =
            InjectorContainer.get().getInstance( InvCndChgEventDao.class );

      // AC event
      InvCndChgEventKey lAcEventKey = lInvCndChgEventDao.generatePrimaryKey();
      createAcEvent( lAcEventKey, RefStageReasonKey.ACQUP );
      createInvCndChgInv( lAcEventKey, lInventory, 1 );
      QuarantineTestData lQuarantineTestData =
            createQuarantineData( lRefQuarActionCategoryKey, lInventory, lAcEventKey );

      // ACT
      DataSet lDs = executeQuery();
      assertExist( lDs );
      lDs.next();

      // ASSERT
      InventoryKey ldatasetInventoryKey = new InventoryKey( lDs.getString( "inv_no_key" ) );
      assertEquals( "Unfortunately, Wrong inventory is fetched.", lInventory,
            ldatasetInventoryKey );

      InvInvTable lInv = InvInvTable.findByPrimaryKey( ldatasetInventoryKey );
      assertEquals( "Unfortunately, Inventory is not quarantined.", RefInvCondKey.QUAR,
            lInv.getInvCondCd() );

      assertEquals( "Unfortunately, Wrong Inventory is quarantined.",
            lQuarantineTestData.getQuarQuarKey(), new QuarQuarKey( lDs.getString( "quar_key" ) ) );

      assertEquals( "Unfortunately, Corrective action is added to wrong quarantined inventory.",
            lQuarantineTestData.getQuarActionKey(),
            new QuarActionKey( lDs.getString( "quar_action_key" ) ) );

      String[] lRefStageReasonCols = { "user_reason_cd" };
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( RefStageReasonKey.ACQUP, "stage_reason_db_id", "stage_reason_cd" );
      QuerySet lDataSet = QuerySetFactory.getInstance().executeQuery( lRefStageReasonCols,
            "ref_stage_reason", lDataSetArgument );
      lDataSet.next();
      assertEquals( lDataSet.getString( "user_reason_cd" ), lDs.getString( "quar_reason_cd" ) );
   }


   /**
    * Given two open quarantined corrective actions for the same inventory, when the query
    * GetAssignedCorrectiveActions.qrx is executed for an hr who is responsible for those actions,
    * then only the last action is returned.
    * 
    * @throws InterruptedException
    */
   @Test
   public void testReturnLastQuarantineRecordForAGivenInventory() throws InterruptedException {

      // ARRANGE
      RefQuarActionCategoryKey lRefQuarActionCategoryKey =
            new RefQuarActionCategoryKey( "10:OTHER" );

      final InventoryKey lInventory = Domain.createSerializedInventory( trackedInventory -> {
         trackedInventory.setCondition( RefInvCondKey.QUAR );
         trackedInventory.setOwner( iLocalOwner );
         trackedInventory.setPartNumber( Domain.createPart() );
      } );

      InvCndChgEventDao lInvCndChgEventDao =
            InjectorContainer.get().getInstance( InvCndChgEventDao.class );

      // AC event
      InvCndChgEventKey lAcEventKey = lInvCndChgEventDao.generatePrimaryKey();
      createAcEvent( lAcEventKey, RefStageReasonKey.ACQUP );
      createInvCndChgInv( lAcEventKey, lInventory, 1 );
      createQuarantineData( lRefQuarActionCategoryKey, lInventory, lAcEventKey );

      // Sleep for a second so that there's a difference in time between first and second record.
      Thread.sleep( 1000 );

      // Second AC event
      lAcEventKey = lInvCndChgEventDao.generatePrimaryKey();
      createAcEvent( lAcEventKey, RefStageReasonKey.ACQISHLF );
      createInvCndChgInv( lAcEventKey, lInventory, 2 );
      createQuarantineData( lRefQuarActionCategoryKey, lInventory, lAcEventKey );

      // ACT
      DataSet lDs = executeQuery();
      assertExist( lDs );
      lDs.next();

      // ASSERT
      String[] lRefStageReasonCols = { "user_reason_cd" };
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( RefStageReasonKey.ACQISHLF, "stage_reason_db_id", "stage_reason_cd" );
      QuerySet lDataSet = QuerySetFactory.getInstance().executeQuery( lRefStageReasonCols,
            "ref_stage_reason", lDataSetArgument );
      lDataSet.next();
      assertEquals( lDataSet.getString( "user_reason_cd" ), lDs.getString( "quar_reason_cd" ) );
   }


   private QuarantineTestData createQuarantineData(
         RefQuarActionCategoryKey lRefQuarActionCategoryKey, InventoryKey lInventory,
         InvCndChgEventKey lAcEventKey ) {

      QuarQuarKey lQuarQuarKey;
      QuarActionKey lQuarActionKey;
      QuarActionAssignmentKey lQuarActionAssignmentKey;
      QuarActionStatusKey lQuarActionStatusKey;

      QuarQuarTable lQuarQuarTable = QuarQuarTable.create();
      lQuarQuarTable.setAcEventKey( lAcEventKey );
      lQuarQuarTable.setInventoryKey( lInventory );
      lQuarQuarKey = lQuarQuarTable.insert();

      QuarActionTable lQuarActionTable = QuarActionTable.create( lQuarQuarTable.getPk() );
      lQuarActionTable.setActionLdesc( DISCREPANCY_DESCRIPTION );
      lQuarActionTable.setCategoryKey( lRefQuarActionCategoryKey );
      lQuarActionKey = lQuarActionTable.insert();

      QuarActionStatusTable lQuarActionStatusTable =
            QuarActionStatusTable.create( lQuarActionTable.getPk() );
      lQuarActionStatusTable.setHumanResourceKey( iHumanResource );
      lQuarActionStatusTable.setStatusKey( RefQuarActionStatusKey.OPEN );
      lQuarActionStatusKey = lQuarActionStatusTable.insert();

      QuarActionAssignmentTable lQuarActionAssignmentTable =
            QuarActionAssignmentTable.create( lQuarActionTable.getPk() );
      lQuarActionAssignmentTable.setHumanResource( iHumanResource );
      lQuarActionAssignmentTable.setAssigned( true );
      lQuarActionAssignmentKey = lQuarActionAssignmentTable.insert();

      return new QuarantineTestData( lQuarQuarKey, lQuarActionKey, lQuarActionAssignmentKey,
            lQuarActionStatusKey );
   }


   private DataSet executeQuery() {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( iHumanResource, new String[] { "aHrDbId", "aHrId" } );
      DataSet lDs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      return lDs;
   }


   private void assertExist( DataSet aDs ) {
      if ( !aDs.hasNext() ) {
         Assert.fail( "There are no quarantined inventories." );
      }
      assertEquals( 1, aDs.getTotalRowCount() );
   }


   private void createAcEvent( InvCndChgEventKey aAcEventKey,
         RefStageReasonKey aRefStageReasonKey ) {
      final DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( aAcEventKey, "event_db_id", "event_id" );
      lDataSetArgument.add( "event_ldesc", NOTE );
      lDataSetArgument.add( RefEventStatusKey.ACQUAR, "event_status_db_id", "event_status_cd" );
      lDataSetArgument.add( aRefStageReasonKey, "stage_reason_db_id", "stage_reason_cd" );
      lDataSetArgument.add( "event_dt",
            Date.from( LocalDateTime.now().toInstant( ZoneOffset.UTC ) ) );
      MxDataAccess.getInstance().executeInsert( "inv_cnd_chg_event", lDataSetArgument );
   }


   private void createInvCndChgInv( InvCndChgEventKey aAcEventKey, InventoryKey aInventory,
         int aEventSequence ) {
      final DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( aAcEventKey, "event_db_id", "event_id" );
      lDataSetArgument.add( aInventory, "inv_no_db_id", "inv_no_id" );
      lDataSetArgument.add( "event_inv_id", aEventSequence );

      MxDataAccess.getInstance().executeInsert( "inv_cnd_chg_inv", lDataSetArgument );
   }


   private class QuarantineTestData {

      QuarQuarKey iQuarQuarKey;
      QuarActionKey iQuarActionKey;
      QuarActionAssignmentKey iQuarActionAssignmentKey;
      QuarActionStatusKey iQuarActionStatusKey;


      public QuarantineTestData(QuarQuarKey aQuarQuarKey, QuarActionKey aQuarActionKey,
            QuarActionAssignmentKey aQuarActionAssignmentKey,
            QuarActionStatusKey aQuarActionStatusKey) {
         this.iQuarActionAssignmentKey = aQuarActionAssignmentKey;
         this.iQuarActionKey = aQuarActionKey;
         this.iQuarActionStatusKey = aQuarActionStatusKey;
         this.iQuarQuarKey = aQuarQuarKey;
      }


      public QuarQuarKey getQuarQuarKey() {
         return iQuarQuarKey;
      }


      public QuarActionKey getQuarActionKey() {
         return iQuarActionKey;
      }


      public QuarActionAssignmentKey getQuarActionAssignmentKey() {
         return iQuarActionAssignmentKey;
      }


      public QuarActionStatusKey getQuarActionStatusKey() {
         return iQuarActionStatusKey;
      }
   }
}
