package com.mxi.mx.core.query.inventory.reservation;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.OwnerDomainBuilder;
import com.mxi.am.domain.builder.PartGroupDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.PartRequestBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefReqTypeKey;
import com.mxi.mx.core.table.eqp.EqpPartNoTable;
import com.mxi.mx.core.table.inv.InvInvTable;


/**
 * This class performs unit testing for listPossiblePreDrawnInventoriesFromBomPart.
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class ListPossiblePreDrawnInventoriesFromBomPartTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private InventoryKey iInventory;
   private LocationKey iSupplyLocation;
   private PartNoKey iPartNo;
   private PartGroupKey iPartGroup;
   private PartRequestKey iPartRequest;
   private LocationKey iPreDrawLocation;

   private DataSet iDs;


   /**
    * Tests that the query returns no data if inventory condition is not RFI
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQueryScenarioNonRFIConditionInv() throws Exception {

      // Setup the Test Data
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iInventory );
      lInvInv.setInvCond( RefInvCondKey.ARCHIVE );
      lInvInv.update();

      // execute query
      execute();

      // Ensure that no rows are returned
      verifyNoResult();
   }


   /**
    * Tests that the query returns no data if part has controlled-reservation flag on
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQueryScenarioControlReservePart() throws Exception {

      // Setup the Test Data
      EqpPartNoTable lPart = EqpPartNoTable.findByPrimaryKey( iPartNo );
      lPart.setControlledReserveBool( true );
      lPart.update();

      // execute query
      execute();

      // Ensure that no rows are returned
      verifyNoResult();
   }


   /**
    * Tests that the query returns no data if inventory is expired
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQueryScenarioExpiredInv() throws Exception {

      // Setup the Test Data
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iInventory );
      lInvInv.setShelfExpiryDt( DateUtils.parseDefaultDateString( "11-FEB-2000 EST" ) );
      lInvInv.update();

      // execute query
      execute();

      // Ensure that no rows are returned
      verifyNoResult();
   }


   /**
    * Tests that the query returns no data if inventory is aircraft type
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQueryScenarioACFTClassInv() throws Exception {

      // Setup the Test Data
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iInventory );
      lInvInv.setInvClass( RefInvClassKey.ACFT );
      lInvInv.update();

      // execute query
      execute();

      // Ensure that no rows are returned
      verifyNoResult();
   }


   /**
    * Tests that the query returns no data if inventory is issued
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQueryScenarioIssuedInv() throws Exception {

      // Setup the Test Data
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iInventory );
      lInvInv.setIssuedBool( true );
      lInvInv.update();

      // execute query
      execute();

      // Ensure that no rows are returned
      verifyNoResult();
   }


   /**
    * Tests that the query returns no data if inventory is locked
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQueryScenarioLockedInv() throws Exception {

      // Setup the Test Data
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iInventory );
      lInvInv.setLockedBool( true );
      lInvInv.update();

      // execute query
      execute();

      // Ensure that no rows are returned
      verifyNoResult();
   }


   /**
    * Tests that the query returns no data if part has prevent auto-reservation flag on
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQueryScenarioNoAutoReservePart() throws Exception {

      // Setup the Test Data
      EqpPartNoTable lPart = EqpPartNoTable.findByPrimaryKey( iPartNo );
      lPart.setNoAutoReserveBool( true );
      lPart.update();

      // execute query
      execute();

      // Ensure that no rows are returned
      verifyNoResult();
   }


   /**
    * Tests that the query returns no data if inventory class is batch but bin quantity is 0
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQueryScenarioNoQuantityBatchInventory() throws Exception {

      // Setup the Test Data
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iInventory );
      lInvInv.setInvClass( RefInvClassKey.BATCH );
      lInvInv.setBinQt( 0.0 );
      lInvInv.update();

      // execute query
      execute();

      // Ensure that no rows are returned
      verifyNoResult();
   }


   /**
    * Tests that the query returns no data if inventory is not loose
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQueryScenarioNotLooseInv() throws Exception {

      // Setup the Test Data
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iInventory );
      lInvInv.setNhInvNo( iInventory );
      lInvInv.update();

      // execute query
      execute();

      // Ensure that no rows are returned
      verifyNoResult();
   }


   /**
    * Tests that the query returns no data if inventory is not located under same supply location
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQueryScenarioOtherLocationInv() throws Exception {

      // Setup the Test Data
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iInventory );
      lInvInv.setLocation( new LocationKey( 1, 568 ) );
      lInvInv.update();

      // execute query
      execute();

      // Ensure that no rows are returned
      verifyNoResult();
   }


   /**
    * Tests that the query returns the proper data for serialized inventory with RFI condition
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQueryStandardScenarioSERInventory() throws Exception {

      // execute query with standard data setup
      execute();

      // validate the returned row
      verifyResult( 0.0 );
   }


   /**
    * Tests that the query returns the proper data for the batch inventory with RFI condition.
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQueryStandardScenarioBatchInventory() throws Exception {

      // setup data
      double lExpectedBinQty = 15.0;
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iInventory );
      lInvInv.setInvClass( RefInvClassKey.BATCH );
      lInvInv.setBinQt( lExpectedBinQty );
      lInvInv.update();

      // execute query
      execute();

      // validate the returned row
      verifyResult( lExpectedBinQty );
   }


   /**
    * Tests that the query returns no data for the incomplete Tracked inventory with RFI condition
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQueryScenarioTRKInventoryWithRFBCondition() throws Exception {
      // Setup test data
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iInventory );
      lInvInv.setInvClass( RefInvClassKey.TRK );
      lInvInv.setComplete( false );
      lInvInv.update();

      // execute query
      execute();

      // Ensure that no rows are returned
      verifyNoResult();
   }


   /**
    * validate data in the returned row
    *
    * @param aExpectedInvQuantity
    */
   private void verifyResult( double aExpectedBinQuantity ) {
      // validate row count
      assertEquals( "Number of retrieved rows", 1, iDs.getRowCount() );

      // Test the first row
      iDs.next();
      assertEquals( iInventory, iDs.getKey( InventoryKey.class, "loose_inv_no_key" ) );
      assertEquals( "shelf_expiry_dt", null, iDs.getDate( "shelf_expiry_dt" ) );
      assertEquals( "received_dt", null, iDs.getDate( "received_dt" ) );
      assertEquals( "available_qt", aExpectedBinQuantity, iDs.getDouble( "available_qt" ), 0 );
      assertEquals( "inv_cond_cd", RefInvCondKey.RFI.getCd(), iDs.getString( "inv_cond_cd" ) );
      assertEquals( iPartRequest, iDs.getKey( PartRequestKey.class, "part_request_key" ) );
      assertEquals( "req_priority_cd", "NORMAL", iDs.getString( "req_priority_cd" ) );
   }


   /**
    * verify the no data is returned
    */
   private void verifyNoResult() {
      assertEquals( "Number of retrieved rows", 0, iDs.getRowCount() );
   }


   /**
    * Execute the query.
    *
    *
    * @return dataSet result.
    */
   private void execute() {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( iPartGroup, new String[] { "aBomPartDbId", "aBomPartId" } );
      lArgs.add( iSupplyLocation, new String[] { "aLocationDbId", "aLocationId" } );

      // execute query
      iDs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.core.query.inventory.reservation.listPossiblePreDrawnInventoriesFromBomPart",
            lArgs );

   }


   @Before
   public void loadData() throws Exception {

      iSupplyLocation =
            new LocationDomainBuilder().withType( RefLocTypeKey.AIRPORT ).isSupplyLocation().build();

      iPreDrawLocation = new LocationDomainBuilder().withType( RefLocTypeKey.PREDRBIN )
            .withSupplyLocation( iSupplyLocation ).build();

      iPartNo =
            new PartNoBuilder().withOemPartNo( "PART1" ).withInventoryClass( RefInvClassKey.SER )
                  .withStatus( RefPartStatusKey.ACTV ).withUnitType( RefQtyUnitKey.EA ).build();

      iPartGroup = new PartGroupDomainBuilder( "PGCODE" ).withPartNo( iPartNo ).build();

      // Create a local Inventory Owner
      OwnerKey lOwnerKey = new OwnerDomainBuilder().build();
      iInventory = new InventoryBuilder().withClass( RefInvClassKey.SER ).withPartNo( iPartNo )
            .withCondition( RefInvCondKey.RFI ).atLocation( iPreDrawLocation ).withBinQt( 15.0 )
            .withOwner( lOwnerKey ).build();

      // create part-request at the pre-draw bin location
      iPartRequest = new PartRequestBuilder().withType( RefReqTypeKey.TASK )
            .withStatus( RefEventStatusKey.PRAVAIL ).withRequestedQuantity( 1 )
            .requiredBy( new Date() ).isNeededAt( iSupplyLocation )
            .withReservedInventory( iInventory ).build();
   }

}
