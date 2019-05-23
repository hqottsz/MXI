package com.mxi.mx.core.query.inventory.reservation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.KitBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.OwnerDomainBuilder;
import com.mxi.am.domain.builder.PartGroupDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.PartRequestBuilder;
import com.mxi.am.domain.builder.ShipmentDomainBuilder;
import com.mxi.am.domain.builder.ShipmentLineBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.EqpPartBaselineKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefReqTypeKey;
import com.mxi.mx.core.key.RefShipmentTypeKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.table.eqp.EqpPartBaselineTable;
import com.mxi.mx.core.table.eqp.EqpPartNoTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.inv.InvKitTable;


/**
 * This class performs unit testing for listPossibleInventoriesFromBomPart.qrx query.
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class ListPossibleInventoriesFromBomPartTest {

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
   private OwnerKey iOwnerKey;
   private Date iReceivedDate = new Date( "05-MAY-2017 12:00:00" );

   private DataSet iDs;

   private PartNoKey iKitPart;

   private InventoryKey iKitInventory;

   private InventoryKey iKitContentInventory;

   private PartGroupKey iKitContentPartGroup;

   private PartGroupKey iKitPartGroup;


   /**
    * Tests that the query returns no data if inventory condition is not RFI
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testScenarioNonRFIConditionInv() throws Exception {

      // Setup the Test Data
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iInventory );
      lInvInv.setInvCond( RefInvCondKey.ARCHIVE );
      lInvInv.update();

      // execute query
      execute( iPartGroup );

      // Ensure that no rows are returned
      verifyNoResult();
   }


   /**
    * Tests that the query returns no data if inventory exists but the related part allows
    * controlled reservations only
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testScenarioControlReservationPart() throws Exception {

      // Setup the Test Data
      EqpPartNoTable lPart = EqpPartNoTable.findByPrimaryKey( iPartNo );
      lPart.setControlledReserveBool( true );
      lPart.update();

      // execute query
      execute( iPartGroup );

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
   public void testScenarioExpiredInv() throws Exception {

      // Setup the Test Data
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iInventory );
      lInvInv.setShelfExpiryDt( DateUtils.parseDefaultDateString( "11-FEB-2000 EST" ) );
      lInvInv.update();

      // execute query
      execute( iPartGroup );

      // Ensure that no rows are returned
      verifyNoResult();
   }


   /**
    * Tests that the query returns data sorted by shelf-life of the inventory, less shelf-life
    * inventory on top
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testScenarioInvWithLessShelfLifeFirst() throws Exception {

      // Setup the Test Data - create one more inventory with shelf-life expiring in 2 days
      Date lShelfExpiryDt1 = DateUtils.addDays( new Date(), 2 );

      InventoryKey lInventoryWithShelfLife = new InventoryBuilder().withClass( RefInvClassKey.SER )
            .withPartNo( iPartNo ).withCondition( RefInvCondKey.RFI ).atLocation( iSupplyLocation )
            .receivedOn( iReceivedDate ).withOwner( iOwnerKey ).withShelfExpiryDt( lShelfExpiryDt1 )
            .build();

      // update shelf-expiry for inventory created in setup to expire in 3 days
      Date lShelfExpiryDt2 = DateUtils.addDays( new Date(), 4 );
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iInventory );
      lInvInv.setShelfExpiryDt( lShelfExpiryDt2 );
      lInvInv.update();

      // execute query
      execute( iPartGroup );

      // verify result
      assertEquals( "Number of retrieved rows", 2, iDs.getRowCount() );

      // Test the first row
      iDs.next();
      assertEquals( lInventoryWithShelfLife, iDs.getKey( InventoryKey.class, "loose_inv_no_key" ) );
      assertTrue( "Shelf Expiry Dates are not close enough", Math
            .abs( lShelfExpiryDt1.getTime() - iDs.getDate( "shelf_expiry_dt" ).getTime() ) < 1000 );
      assertEquals( "received_dt", iReceivedDate, iDs.getDate( "received_dt" ) );
      assertEquals( "inv_cond_cd", RefInvCondKey.RFI.getCd(), iDs.getString( "inv_cond_cd" ) );
      assertEquals( "inv_no_id", lInventoryWithShelfLife.getId(), iDs.getInt( "inv_no_id" ) );

      // verify the second row
      iDs.next();
      assertEquals( iInventory, iDs.getKey( InventoryKey.class, "loose_inv_no_key" ) );
      assertTrue( "Shelf Expiry Dates are not close enough", Math
            .abs( lShelfExpiryDt2.getTime() - iDs.getDate( "shelf_expiry_dt" ).getTime() ) < 1000 );
      assertEquals( "received_dt", iReceivedDate, iDs.getDate( "received_dt" ) );
      assertEquals( "inv_cond_cd", RefInvCondKey.RFI.getCd(), iDs.getString( "inv_cond_cd" ) );
      assertEquals( "inv_no_id", iInventory.getId(), iDs.getInt( "inv_no_id" ) );
   }


   /**
    * Tests that the query returns data sorted by inventory owner, locally owned inventory first and
    * then vendor owned
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testScenarioLocallyOwnedInvFirst() throws Exception {

      // Setup the Test Data - create inventory with non-local owner
      OwnerKey lOwnerKey = new OwnerDomainBuilder().isNonLocal().build();

      InventoryKey lInventoryWithNonLocalOwner =
            new InventoryBuilder().withClass( RefInvClassKey.SER ).withPartNo( iPartNo )
                  .withCondition( RefInvCondKey.RFI ).atLocation( iSupplyLocation )
                  .receivedOn( iReceivedDate ).withOwner( lOwnerKey ).build();

      // execute query
      execute( iPartGroup );

      // verify result
      assertEquals( "Number of retrieved rows", 2, iDs.getRowCount() );

      // Test the first row
      iDs.next();
      assertEquals( iInventory, iDs.getKey( InventoryKey.class, "loose_inv_no_key" ) );
      assertEquals( "received_dt", iReceivedDate, iDs.getDate( "received_dt" ) );
      assertEquals( "inv_cond_cd", RefInvCondKey.RFI.getCd(), iDs.getString( "inv_cond_cd" ) );
      assertEquals( "inv_no_id", iInventory.getId(), iDs.getInt( "inv_no_id" ) );

      // verify the second row
      iDs.next();
      assertEquals( lInventoryWithNonLocalOwner,
            iDs.getKey( InventoryKey.class, "loose_inv_no_key" ) );
      assertEquals( "received_dt", iReceivedDate, iDs.getDate( "received_dt" ) );
      assertEquals( "inv_cond_cd", RefInvCondKey.RFI.getCd(), iDs.getString( "inv_cond_cd" ) );
      assertEquals( "inv_no_id", lInventoryWithNonLocalOwner.getId(), iDs.getInt( "inv_no_id" ) );
   }


   /**
    * Tests that the query returns no data if inventory class is aircraft type
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testScenarioACFTClassInv() throws Exception {

      // Setup the Test Data
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iInventory );
      lInvInv.setInvClass( RefInvClassKey.ACFT );
      lInvInv.update();

      // execute query
      execute( iPartGroup );

      // Ensure that no rows are returned
      verifyNoResult();
   }


   /**
    * Tests that the query returns no data if inventory is already issued
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testScenarioIssuedInv() throws Exception {

      // Setup the Test Data
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iInventory );
      lInvInv.setIssuedBool( true );
      lInvInv.update();

      // execute query
      execute( iPartGroup );

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
   public void testScenarioLockedInv() throws Exception {

      // Setup the Test Data
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iInventory );
      lInvInv.setLockedBool( true );
      lInvInv.update();

      // execute query
      execute( iPartGroup );

      // Ensure that no rows are returned
      verifyNoResult();
   }


   /**
    * Tests that the query returns no data if inventory exists but related part prevents
    * auto-reservation
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testScenarioNoAutoReservePart() throws Exception {

      // Setup the Test Data
      EqpPartNoTable lPart = EqpPartNoTable.findByPrimaryKey( iPartNo );
      lPart.setNoAutoReserveBool( true );
      lPart.update();

      // execute query
      execute( iPartGroup );

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
   public void testScenarioNoQuantityBatchInventory() throws Exception {

      // Setup the Test Data
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iInventory );
      lInvInv.setInvClass( RefInvClassKey.BATCH );
      lInvInv.setBinQt( 0.0 );
      lInvInv.update();

      // execute query
      execute( iPartGroup );

      // Ensure that no rows are returned
      verifyNoResult();
   }


   /**
    * Tests that the query returns no data if inventory exists but it is marked as not found.
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testScenarioNotFoundInv() throws Exception {

      // Setup the Test Data
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iInventory );
      lInvInv.setNotFoundBool( true );
      lInvInv.update();

      // execute query
      execute( iPartGroup );

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
   public void testScenarioNotLooseInv() throws Exception {

      // Setup the Test Data
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iInventory );
      lInvInv.setNhInvNo( iInventory );
      lInvInv.update();

      // execute query
      execute( iPartGroup );

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
   public void testScenarioOtherLocationInv() throws Exception {

      // Setup the Test Data - change location of inventory
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iInventory );
      lInvInv.setLocation( new LocationKey( 1, 568 ) );
      lInvInv.update();

      // execute query
      execute( iPartGroup );

      // Ensure that no rows are returned
      verifyNoResult();
   }


   /**
    * Tests that the query returns no data if inventory exists but the part is having a BUILD status
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testScenarioPartStatusBUILD() throws Exception {

      // Setup the Test Data
      EqpPartNoTable lEqpPartNo = EqpPartNoTable.findByPrimaryKey( iPartNo );
      lEqpPartNo.setPartStatus( RefPartStatusKey.BUILD );
      lEqpPartNo.update();

      // execute query
      execute( iPartGroup );

      // Ensure that no rows are returned
      verifyNoResult();
   }


   /**
    * Tests that the query returns no data if inventory exists but the part is having OBSLT status
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testScenarioPartStatusOBSLT() throws Exception {

      // Setup the Test Data
      EqpPartNoTable lEqpPartNo = EqpPartNoTable.findByPrimaryKey( iPartNo );
      lEqpPartNo.setPartStatus( RefPartStatusKey.OBSLT );
      lEqpPartNo.update();

      // execute query
      execute( iPartGroup );

      // Ensure that no rows are returned
      verifyNoResult();
   }


   /**
    * Tests that the query returns no data if inventory exists but the part is not approved.
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testScenarioUnApprovedPartInv() throws Exception {
      // make part unapproved for the part-group
      EqpPartBaselineKey lEqpPartBaselineKey = new EqpPartBaselineKey( iPartGroup, iPartNo );
      EqpPartBaselineTable lEqpPartBaseline =
            EqpPartBaselineTable.findByPrimaryKey( lEqpPartBaselineKey );
      lEqpPartBaseline.setApprovedBool( false );
      lEqpPartBaseline.update();

      // execute query
      execute( iPartGroup );

      // Ensure that no rows are returned
      verifyNoResult();
   }


   /**
    * Tests that the query returns no data if inventory exists but it is located at a Predraw Bin
    * location
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testScenarioPredrawBinInv() throws Exception {

      // Setup the Test Data
      InvInvTable lInventory = InvInvTable.findByPrimaryKey( iInventory );
      lInventory.setLocation( iPreDrawLocation );
      lInventory.update();

      // execute query
      execute( iPartGroup );

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
   public void testScenarioValidSERInventory() throws Exception {

      // execute query with standard data setup
      execute( iPartGroup );

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
   public void testScenarioValidBatchInventory() throws Exception {

      // setup data
      double lExpectedBinQty = 15.0;
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iInventory );
      lInvInv.setInvClass( RefInvClassKey.BATCH );
      lInvInv.setBinQt( lExpectedBinQty );
      lInvInv.update();

      // execute query
      execute( iPartGroup );

      // validate the returned row
      verifyResult( lExpectedBinQty );
   }


   /**
    * Tests that the query returns the proper data for the batch inventory with null reserved
    * quantity.
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testScenarioNullReserveQtyBatchInventory() throws Exception {
   
      // setup data
      double lExpectedBinQty = 15.0;
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iInventory );
      lInvInv.setInvClass( RefInvClassKey.BATCH );
      lInvInv.setBinQt( lExpectedBinQty );
      lInvInv.setReservedQt( null );
      lInvInv.update();
   
      // execute query
      execute( iPartGroup );
   
      // validate the returned row
      verifyResult( lExpectedBinQty );
   }


   /**
    * Tests that the query returns no data for the incomplete tracked inventory with RFI condition
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testScenarioTRKInventoryWithRFBCondition() throws Exception {
      // change part inventory class to be TRK
      EqpPartNoTable lEqpPartNo = EqpPartNoTable.findByPrimaryKey( iPartNo );
      lEqpPartNo.setInvClass( RefInvClassKey.TRK );
      lEqpPartNo.update();

      // change inventory class to be TRK and mark incomplete to mimic RFB(Ready for build) scenario
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iInventory );
      lInvInv.setComplete( false );
      lInvInv.setInvClass( RefInvClassKey.TRK );
      lInvInv.setInvCond( RefInvCondKey.RFI );
      lInvInv.update();

      // execute query
      execute( iPartGroup );

      // Ensure that no rows are returned
      verifyNoResult();
   }


   /**
    * Tests that the query returns no data if inventory exists but it is in a kit
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testScenarioKitContentInventory() throws Exception {
      // setup kit data
      setupKitData();

      // execute query
      execute( iKitContentPartGroup );

      // validate the returned row
      verifyNoResult();
   }


   /**
    * Tests that the query returns data if kit inventory exists and it is RFI condition and complete
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testScenarioRFICompleteKitInventory() throws Exception {
      // setup kit data
      setupKitData();

      // set the kit complete boolean to true
      InvKitTable lInvKitTable = InvKitTable.findByPrimaryKey( iKitInventory );
      lInvKitTable.setKitComplete( true );
      lInvKitTable.update();

      // execute query
      execute( iKitPartGroup );

      // validate the returned row for kit inventory
      assertEquals( "Number of retrieved rows", 1, iDs.getRowCount() );

      iDs.next();
      assertEquals( iKitInventory, iDs.getKey( InventoryKey.class, "loose_inv_no_key" ) );
      assertEquals( "shelf_expiry_dt", null, iDs.getDate( "shelf_expiry_dt" ) );
      assertEquals( "received_dt", iReceivedDate, iDs.getDate( "received_dt" ) );
      assertEquals( "available_qt", 0, iDs.getDouble( "available_qt" ), 0 );
      assertEquals( "inv_cond_cd", RefInvCondKey.RFI.getCd(), iDs.getString( "inv_cond_cd" ) );
      assertEquals( "inv_no_id", iKitInventory.getId(), iDs.getInt( "inv_no_id" ) );
   }


   /**
    * Tests that the query returns no data if kit inventory exists and has RFI condition but it is
    * not complete
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testScenarioRFIIncompleteKitInventory() throws Exception {
      // setup kit data
      setupKitData();

      // execute query
      execute( iKitPartGroup );

      // validate the returned row
      verifyNoResult();
   }


   /**
    * Tests that the query returns no data if inventory is already picked by a pending STKTRN
    * shipment
    *
    * @throws Exception
    */
   @Test
   public void testScenarioInvPickedByShipment() throws Exception {

      // create a dock at the supply location
      LocationKey lDockLocation = new LocationDomainBuilder().withType( RefLocTypeKey.DOCK )
            .withSupplyLocation( iSupplyLocation ).build();

      // create shipment
      ShipmentKey lShipmentKey = new ShipmentDomainBuilder().fromLocation( iSupplyLocation )
            .toLocation( lDockLocation ).withType( RefShipmentTypeKey.STKTRN )
            .withStatus( RefEventStatusKey.IXPEND ).build();

      // create shipment line for inventory
      new ShipmentLineBuilder( lShipmentKey ).forPart( iPartNo ).forInventory( iInventory ).build();

      // execute query
      execute( iPartGroup );

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
      assertEquals( "received_dt", iReceivedDate, iDs.getDate( "received_dt" ) );
      assertEquals( "available_qt", aExpectedBinQuantity, iDs.getDouble( "available_qt" ), 0 );
      assertEquals( "inv_cond_cd", RefInvCondKey.RFI.getCd(), iDs.getString( "inv_cond_cd" ) );
      assertEquals( "inv_no_id", iInventory.getId(), iDs.getInt( "inv_no_id" ) );
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
    * @param aPartGroup
    *
    *
    * @return dataSet result.
    */
   private void execute( PartGroupKey aPartGroup ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aPartGroup, new String[] { "aBomPartDbId", "aBomPartId" } );
      lArgs.add( iSupplyLocation, new String[] { "aLocationDbId", "aLocationId" } );

      // execute query
      iDs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.core.query.inventory.reservation.listPossibleInventoriesFromBomPart",
            lArgs );

   }


   @Before
   public void loadData() throws Exception {

      // Create a Location to be tested
      iSupplyLocation =
            new LocationDomainBuilder().withType( RefLocTypeKey.AIRPORT ).isSupplyLocation().build();

      iPreDrawLocation = new LocationDomainBuilder().withType( RefLocTypeKey.PREDRBIN )
            .withSupplyLocation( iSupplyLocation ).build();

      // Create the Part and Part Group to be tested
      iPartNo =
            new PartNoBuilder().withOemPartNo( "PART1" ).withInventoryClass( RefInvClassKey.SER )
                  .withStatus( RefPartStatusKey.ACTV ).withUnitType( RefQtyUnitKey.EA ).build();

      iPartGroup = new PartGroupDomainBuilder( "PGCODE" ).withPartNo( iPartNo ).build();

      // Create a local Inventory Owner
      iOwnerKey = new OwnerDomainBuilder().build();

      // Create inventory
      iInventory = new InventoryBuilder().withClass( RefInvClassKey.SER ).withPartNo( iPartNo )
            .withCondition( RefInvCondKey.RFI ).atLocation( iSupplyLocation )
            .receivedOn( iReceivedDate ).withOwner( iOwnerKey ).build();

      // create part-request at the location
      iPartRequest = new PartRequestBuilder().withType( RefReqTypeKey.TASK )
            .withStatus( RefEventStatusKey.PROPEN ).forSpecifiedPart( iPartNo )
            .withRequestedQuantity( 5.0 ).requiredBy( new Date() ).isNeededAt( iSupplyLocation )
            .withReservedInventory( iInventory ).build();
   }


   private void setupKitData() {

      // create kit content part first
      PartNoBuilder lPartNoBuilder = new PartNoBuilder();

      PartNoKey lKitContentPart = lPartNoBuilder.withDefaultPartGroup().build();
      iKitContentPartGroup = lPartNoBuilder.getDefaultPartGroup();

      // create kit part
      iKitPart =
            new PartNoBuilder().withOemPartNo( "KITPART" ).withInventoryClass( RefInvClassKey.KIT )
                  .withStatus( RefPartStatusKey.ACTV ).withUnitType( RefQtyUnitKey.EA ).build();

      iKitPartGroup = new PartGroupDomainBuilder( "KIT" ).withInventoryClass( RefInvClassKey.KIT )
            .withPartNo( iKitPart ).build();

      // assign kit content part to this kit
      iKitPart =
            new KitBuilder( iKitPart ).withContent( lKitContentPart, iKitContentPartGroup ).build();

      iKitInventory = new InventoryBuilder().withPartNo( iKitPart ).withClass( RefInvClassKey.KIT )
            .withCondition( RefInvCondKey.RFI ).atLocation( iSupplyLocation )
            .receivedOn( iReceivedDate ).withOwner( iOwnerKey ).build();

      iKitContentInventory =
            new InventoryBuilder().withPartNo( lKitContentPart ).withSerialNo( "KIT_CONTENT_INV" )
                  .isInKit( iKitInventory ).atLocation( iSupplyLocation ).build();

   }

}
