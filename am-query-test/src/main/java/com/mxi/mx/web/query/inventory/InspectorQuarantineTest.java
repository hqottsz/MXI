package com.mxi.mx.web.query.inventory;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AuthorityKey;
import com.mxi.mx.core.key.HrAuthorityKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InvCndChgEventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.table.acevent.InvCndChgEventDao;
import com.mxi.mx.core.table.quarantine.QuarQuarTable;


/**
 * Tests for <code>InspectorQuarantine</code> query
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class InspectorQuarantineTest {

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();

   private static final String NOTE = "Test - User Note";

   private HumanResourceKey humanResource;
   private LocationKey supplyLocation;

   private HumanResourceKey humanResource2;
   private LocationKey supplyLocation2;

   private HumanResourceKey humanResource3;


   @Before
   public void setUp() {

      supplyLocation = Domain.createLocation( supplyLocation -> {
         supplyLocation.setCode( "YYZ" );
         supplyLocation.setIsSupplyLocation( true );
      } );

      supplyLocation2 = Domain.createLocation( location -> {
         location.setCode( "YYC" );
         location.setIsSupplyLocation( true );
      } );

      humanResource = Domain.createHumanResource( hr -> {
         hr.setUser( Domain.createUser() );
         hr.setSupplyLocations( supplyLocation );
      } );

      humanResource2 = Domain.createHumanResource( hr -> {
         hr.setUser( Domain.createUser() );
         hr.setSupplyLocations( supplyLocation2 );
      } );

      humanResource3 = Domain.createHumanResource( hr -> {
         hr.setUser( Domain.createUser() );
         hr.setSupplyLocations( supplyLocation );
         hr.setSupplyLocations( supplyLocation2 );
      } );
   }


   /**
    * Given a quarantined inventory in supply location X; When <code>InspectorQuarantine.qrx</code>
    * is executed for an HR who has being assigned to location X, then the quarantined inventory is
    * returned with quarantined event's description.
    */
   @Test
   public void testReturnMyListOfToDoAsInspector() {

      // ARRANGE
      setupQuarantinedInventoryWithoutAuthority();

      // ACT
      QuerySet querySet = executeQuery( humanResource );

      // ASSERT
      assertEquals( 1, querySet.getRowCount() );
      querySet.next();
      assertEquals( NOTE, querySet.getString( "inv_cond_change_note" ) );
   }


   /**
    * Given a inventory that is quarantined in supply location X, when the
    * <code>InspectorQuarantine.qrx</code> is executed for an HR who is NOT assigned to location X,
    * then the quarantined inventory does NOT returned.
    */
   @Test
   public void testReturnEmptyWhenHrIsNotAssignedToLocationOfQuarantinedInventory() {

      // ARRANGE
      setupQuarantinedInventoryWithoutAuthority();

      // ACT
      QuerySet querySet = executeQuery( humanResource2 );

      // ASSERT
      assertEquals( 0, querySet.getRowCount() );
   }


   /**
    * Given a quarantined inventory located in supply location X with required authority Y; when the
    * <code>InspectorQuarantine.qrx</code> is executed for an HR who has authority and is assigned
    * to location X, then the quarantined inventory should returned.
    */
   @Test
   public void testReturnQuarantinedInventoryWhenHrHasAuthorityOnTheInventory() {

      // ARRANGE
      final InventoryKey inventoryKey = setQuarantinedInventoryWithAuthority();

      // ACT
      QuerySet querySet = executeQuery( humanResource3 );

      // ASSERT
      querySet.next();
      assertEquals( 1, querySet.getRowCount() );
      assertEquals( inventoryKey.toString(), querySet.getString( "inv_no_key" ) );
   }


   /**
    * Given a quarantined inventory located in supply location X with required authority Y, when the
    * <code>InspectorQuarantine.qrx</code> is executed for an HR who has NO authority but is
    * assigned to location X, then the quarantined inventory should NOT be returned.
    */
   @Test
   public void testReturnEmptyWhenHrHasNoAuthorityOnTheInventory() {

      // ARRANGE
      setQuarantinedInventoryWithAuthority();

      // ACT
      QuerySet querySet = executeQuery( humanResource );

      // ASSERT
      querySet.next();
      assertEquals( 0, querySet.getRowCount() );
   }


   /**
    * Given a quarantined inventory located in supply location X with required authority Y, when the
    * <code>InspectorQuarantine.qrx</code> is executed for an HR who has NO authority and is NOT
    * assigned to location X, then the quarantined inventory should NOT be returned.
    */
   @Test
   public void testReturnEmptyWhenHrHasNoAuthorityAndIsNotAssignedToLocationOnTheInventory() {

      // ARRANGE
      setQuarantinedInventoryWithAuthority();

      // ACT
      QuerySet querySet = executeQuery( humanResource2 );

      // ASSERT
      querySet.next();
      assertEquals( 0, querySet.getRowCount() );
   }


   /**
    * setup a quarantined inventory registered in location X with associated AC event.
    */
   private void setupQuarantinedInventoryWithoutAuthority() {
      final InventoryKey inventoryKey = Domain.createSerializedInventory( trackedInventory -> {
         trackedInventory.setLocation( supplyLocation );
         trackedInventory.setCondition( RefInvCondKey.QUAR );
         trackedInventory.setOwner( Domain.createOwner() );
         trackedInventory.setPartNumber( Domain.createPart() );
      } );
      final InvCndChgEventDao invCndChgEventDao =
            InjectorContainer.get().getInstance( InvCndChgEventDao.class );

      // AC event
      final InvCndChgEventKey acEventKey = invCndChgEventDao.generatePrimaryKey();
      createAcEvent( acEventKey );

      // Quarantine record
      QuarQuarTable quarQuarTable = QuarQuarTable.create();
      quarQuarTable.setAcEventKey( acEventKey );
      quarQuarTable.setInventoryKey( inventoryKey );
      quarQuarTable.insert();
   }


   /**
    * setup a quarantined inventory registered in location X which requires authority Y with its
    * associated AC event
    */
   private InventoryKey setQuarantinedInventoryWithAuthority() {
      AuthorityKey authorityKey = new AuthorityKey( 1234, 1 );
      DataSetArgument dataSetArgument = new DataSetArgument();
      dataSetArgument.add( authorityKey, "authority_db_id", "authority_id" );
      MxDataAccess.getInstance().executeInsert( "org_authority", dataSetArgument );

      HrAuthorityKey hrAuthorityKey = new HrAuthorityKey( humanResource3.getDbId(),
            humanResource3.getId(), authorityKey.getDbId(), authorityKey.getId() );
      dataSetArgument = new DataSetArgument();
      dataSetArgument.add( hrAuthorityKey, "hr_db_id", "hr_id", "authority_db_id", "authority_id" );
      MxDataAccess.getInstance().executeInsert( "org_hr_authority", dataSetArgument );

      final InventoryKey inventoryKey = Domain.createSerializedInventory( trackedInventory -> {
         trackedInventory.setLocation( supplyLocation );
         trackedInventory.setCondition( RefInvCondKey.QUAR );
         trackedInventory.setOwner( Domain.createOwner() );
         trackedInventory.setPartNumber( Domain.createPart() );
         trackedInventory.setAuthorityKey( authorityKey );
      } );
      final InvCndChgEventDao invCndChgEventDao =
            InjectorContainer.get().getInstance( InvCndChgEventDao.class );

      // AC event
      final InvCndChgEventKey acEventKey = invCndChgEventDao.generatePrimaryKey();
      createAcEvent( acEventKey );

      // Quarantine record
      QuarQuarTable quarQuarTable = QuarQuarTable.create();
      quarQuarTable.setAcEventKey( acEventKey );
      quarQuarTable.setInventoryKey( inventoryKey );
      quarQuarTable.insert();
      return inventoryKey;
   }


   private QuerySet executeQuery( HumanResourceKey hrKey ) {
      DataSetArgument args = new DataSetArgument();
      args.add( hrKey, new String[] { "aHrDbId", "aHrId" } );
      QuerySet querySet = QueryExecutor.executeQuery( databaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), args );

      return querySet;
   }


   private void createAcEvent( InvCndChgEventKey acEventKey ) {
      final DataSetArgument dataSetArgument = new DataSetArgument();
      dataSetArgument.add( acEventKey, "event_db_id", "event_id" );
      dataSetArgument.add( "event_ldesc", NOTE );

      MxDataAccess.getInstance().executeInsert( "inv_cnd_chg_event", dataSetArgument );
   }
}
