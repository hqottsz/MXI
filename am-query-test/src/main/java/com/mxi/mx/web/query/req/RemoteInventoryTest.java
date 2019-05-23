
package com.mxi.mx.web.query.req;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.MethodSorters;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.EqpPartBaselineKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefSupplyChainKey;
import com.mxi.mx.core.table.eqp.EqpPartBaselineTable;
import com.mxi.mx.core.table.eqp.EqpPartNoTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.inv.InvLocTable;
import com.mxi.mx.core.table.req.ReqPartTable;


/**
 * This class performs unit testing on the query file with the same package and name.
 */
@FixMethodOrder( MethodSorters.NAME_ASCENDING )
@RunWith( BlockJUnit4ClassRunner.class )
public final class RemoteInventoryTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   /** DOCUMENT ME! */
   private static final PartNoKey PART_WITH_SHIPPING_TIME = new PartNoKey( 11, 111 );

   /** DOCUMENT ME! */
   private static final LocationKey MULTI_PART_LOC = new LocationKey( 555, 555 );

   /** DOCUMENT ME! */
   private static final PartNoKey NON_RFI_PART = new PartNoKey( 6666, 6666 );

   /** DOCUMENT ME! */
   private static final PartRequestKey PART_REQUEST_KEY = new PartRequestKey( 747, 4698 );

   /** DOCUMENT ME! */
   private static final PartNoKey MULTI_PART_PART2 = new PartNoKey( 2222, 2222 );

   /** DOCUMENT ME! */
   private static final PartNoKey MULTI_PART_PART1 = new PartNoKey( 444, 444 );

   /** DOCUMENT ME! */
   private static final PartGroupKey MULTI_PART_BOM = new PartGroupKey( 682, 7982 );

   /** DOCUMENT ME! */
   private static final LocationKey MULTI_LOC_LOC2 = new LocationKey( 4, 4 );

   /** DOCUMENT ME! */
   private static final LocationKey MULTI_LOC_LOC1 = new LocationKey( 222, 222 );

   /** DOCUMENT ME! */
   private static final PartNoKey MULTI_LOC_PART = new PartNoKey( 111, 111 );

   /** DOCUMENT ME! */
   private static final PartNoKey ISSUED_PART = new PartNoKey( 11, 11 );

   /** DOCUMENT ME! */
   private static final PartNoKey NON_LOOSE_PART = new PartNoKey( 1, 1 );


   /**
    * Creates the database data
    */
   @Before
   public void setUp() {

      /** Setup a Part Request to be used by all the Tests **/
      InvLocTable lSupplierLocation = InvLocTable.create( new LocationKey( 747, 4698 ) );
      lSupplierLocation.setSupplyLoc( lSupplierLocation.getPk() );
      lSupplierLocation.setSupplyBool( true );
      lSupplierLocation.insert();

      InvLocTable lLocation = InvLocTable.create( new LocationKey( 1010, 320 ) );
      lLocation.setSupplyLoc( lLocation.getPk() );
      lLocation.setSupplyBool( true );
      lLocation.setHubLocation( lSupplierLocation.getPk() );
      lLocation.setShippingTime( 5 );
      lLocation.insert();

      ReqPartTable lReqPart = ReqPartTable.create( PART_REQUEST_KEY );
      lReqPart.setReqLocation( lLocation.getPk() );
      lReqPart.setSupplyChain( RefSupplyChainKey.DEFAULT );
      lReqPart.insert();

      /** Setup a non-loose inventory **/
      EqpPartNoTable lPart1 = EqpPartNoTable.create( NON_LOOSE_PART );
      lPart1.setQtyUnit( RefQtyUnitKey.EA );
      lPart1.insert();

      InvLocTable lLocation1 = InvLocTable.create( new LocationKey( 2, 2 ) );
      lLocation1.setSupplyLoc( lLocation1.getPk() );
      lLocation1.setSupplyBool( true );
      lLocation1.insert();

      InvInvTable lInventory1 = InvInvTable.create( new InventoryKey( 3, 3 ) );
      lInventory1.setIssuedBool( false );
      lInventory1.setNhInvNo( lInventory1.getPk() );
      lInventory1.setBinQt( 20.0 );
      lInventory1.setReservedQt( 0.0 );
      lInventory1.setReservedBool( false );
      lInventory1.setPartNo( lPart1.getPk() );
      lInventory1.setLocation( lLocation1.getPk() );
      lInventory1.setInvCond( RefInvCondKey.RFI );
      lInventory1.insert();

      /** Setup an issued inventory **/
      EqpPartNoTable lPart2 = EqpPartNoTable.create( ISSUED_PART );
      lPart2.setQtyUnit( RefQtyUnitKey.EA );
      lPart2.insert();

      InvLocTable lLocation2 = InvLocTable.create( new LocationKey( 22, 22 ) );
      lLocation2.setSupplyLoc( lLocation2.getPk() );
      lLocation2.setSupplyBool( true );
      lLocation2.insert();

      InvInvTable lInventory2 = InvInvTable.create( new InventoryKey( 33, 33 ) );
      lInventory2.setIssuedBool( true );
      lInventory2.setNhInvNo( null );
      lInventory2.setBinQt( 20.0 );
      lInventory2.setReservedQt( 0.0 );
      lInventory2.setReservedBool( false );
      lInventory2.setPartNo( lPart2.getPk() );
      lInventory2.setLocation( lLocation2.getPk() );
      lInventory2.setInvCond( RefInvCondKey.RFI );
      lInventory2.insert();

      /** Inventory at multiple Locations (Single Part) **/
      EqpPartNoTable lPart3 = EqpPartNoTable.create( MULTI_LOC_PART );
      lPart3.setQtyUnit( RefQtyUnitKey.EA );
      lPart3.insert();

      InvLocTable lLocation3_1 = InvLocTable.create( MULTI_LOC_LOC1 );
      lLocation3_1.setSupplyLoc( lLocation3_1.getPk() );
      lLocation3_1.setSupplyBool( true );
      lLocation3_1.insert();

      InvLocTable lLocation3_2 = InvLocTable.create( MULTI_LOC_LOC2 );
      lLocation3_2.setSupplyLoc( lLocation3_2.getPk() );
      lLocation3_2.setSupplyBool( true );
      lLocation3_2.insert();

      InvInvTable lInventory3_1 = InvInvTable.create( new InventoryKey( 333, 333 ) );
      lInventory3_1.setIssuedBool( false );
      lInventory3_1.setNhInvNo( null );
      lInventory3_1.setBinQt( 10.0 );
      lInventory3_1.setReservedQt( 5.0 );
      lInventory3_1.setReservedBool( false );
      lInventory3_1.setPartNo( lPart3.getPk() );
      lInventory3_1.setLocation( lLocation3_1.getPk() );
      lInventory3_1.setInvCond( RefInvCondKey.RFI );
      lInventory3_1.insert();

      InvInvTable lInventory3_2 = InvInvTable.create( new InventoryKey( 5555, 5555 ) );
      lInventory3_2.setIssuedBool( false );
      lInventory3_2.setNhInvNo( null );
      lInventory3_2.setBinQt( 20.0 );
      lInventory3_2.setReservedQt( 20.0 );
      lInventory3_2.setReservedBool( true );
      lInventory3_2.setPartNo( lPart3.getPk() );
      lInventory3_2.setLocation( lLocation3_2.getPk() );
      lInventory3_2.setInvCond( RefInvCondKey.RFI );
      lInventory3_2.insert();

      /** Inventory from Multiple Parts (Single Location) **/

      PartGroupKey lPartGroup = MULTI_PART_BOM;

      EqpPartNoTable lPart4_1 = EqpPartNoTable.create( MULTI_PART_PART1 );
      lPart4_1.setQtyUnit( RefQtyUnitKey.EA );
      lPart4_1.insert();

      EqpPartNoTable lPart4_2 = EqpPartNoTable.create( MULTI_PART_PART2 );
      lPart4_2.setQtyUnit( RefQtyUnitKey.EA );
      lPart4_2.insert();

      EqpPartBaselineTable lPartBaseline4_1 =
            EqpPartBaselineTable.create( new EqpPartBaselineKey( lPartGroup, lPart4_1.getPk() ) );
      lPartBaseline4_1.insert();

      EqpPartBaselineTable lPartBaseline4_2 =
            EqpPartBaselineTable.create( new EqpPartBaselineKey( lPartGroup, lPart4_2.getPk() ) );
      lPartBaseline4_2.insert();

      InvLocTable lLocation4 = InvLocTable.create( MULTI_PART_LOC );
      lLocation4.setSupplyLoc( lLocation4.getPk() );
      lLocation4.setSupplyBool( true );
      lLocation4.insert();

      InvInvTable lInventory4_1 = InvInvTable.create( new InventoryKey( 666, 666 ) );
      lInventory4_1.setIssuedBool( false );
      lInventory4_1.setNhInvNo( null );
      lInventory4_1.setBinQt( 20.0 );
      lInventory4_1.setReservedQt( 15.0 );
      lInventory4_1.setReservedBool( false );
      lInventory4_1.setPartNo( lPart4_1.getPk() );
      lInventory4_1.setLocation( lLocation4.getPk() );
      lInventory4_1.setInvCond( RefInvCondKey.RFI );
      lInventory4_1.insert();

      InvInvTable lInventory4_2 = InvInvTable.create( new InventoryKey( 1111, 1111 ) );
      lInventory4_2.setIssuedBool( false );
      lInventory4_2.setNhInvNo( null );
      lInventory4_2.setBinQt( 8.0 );
      lInventory4_2.setReservedQt( 4.0 );
      lInventory4_2.setReservedBool( false );
      lInventory4_2.setPartNo( lPart4_2.getPk() );
      lInventory4_2.setLocation( lLocation4.getPk() );
      lInventory4_2.setInvCond( RefInvCondKey.RFI );
      lInventory4_2.insert();

      /** Setup a non-loose inventory **/
      EqpPartNoTable lPart5 = EqpPartNoTable.create( NON_RFI_PART );
      lPart5.setQtyUnit( RefQtyUnitKey.EA );
      lPart5.insert();

      InvLocTable lLocation5 = InvLocTable.create( new LocationKey( 4444, 4444 ) );
      lLocation5.setSupplyLoc( lLocation1.getPk() );
      lLocation5.setSupplyBool( true );
      lLocation5.insert();

      InvInvTable lInventory5 = InvInvTable.create( new InventoryKey( 3333, 3333 ) );
      lInventory5.setIssuedBool( false );
      lInventory5.setNhInvNo( null );
      lInventory5.setBinQt( 20.0 );
      lInventory5.setReservedQt( 0.0 );
      lInventory5.setReservedBool( false );
      lInventory5.setPartNo( lPart5.getPk() );
      lInventory5.setLocation( lLocation5.getPk() );
      lInventory5.setInvCond( RefInvCondKey.QUAR );
      lInventory5.insert();

      /** Setup the Shipping Time **/
      EqpPartNoTable lPart6 = EqpPartNoTable.create( PART_WITH_SHIPPING_TIME );
      lPart6.setQtyUnit( RefQtyUnitKey.EA );
      lPart6.insert();

      InvInvTable lInventory6 = InvInvTable.create( new InventoryKey( 333, 3333 ) );
      lInventory6.setIssuedBool( false );
      lInventory6.setNhInvNo( null );
      lInventory6.setBinQt( 20.0 );
      lInventory6.setReservedQt( 0.0 );
      lInventory6.setReservedBool( false );
      lInventory6.setPartNo( lPart6.getPk() );
      lInventory6.setLocation( lSupplierLocation.getPk() );
      lInventory6.setInvCond( RefInvCondKey.RFI );
      lInventory6.insert();
   }


   /**
    * Tests that the query does not return issued inventory
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQueryIssuedInventory() throws Exception {

      // Setup the Test Data (tie the Request to the proper Part/Part Group)
      updatePartRequest( ISSUED_PART, null );

      // Execute the query to retrieve data
      DataSet lDs = execute();

      // Ensure that no rows are returned
      assertEquals( "Number of retrieved rows", 0, lDs.getRowCount() );
   }


   /**
    * Tests that the query does not return installed inventory
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQueryLooseInventory() throws Exception {

      // Setup the Test Data (tie the Request to the proper Part/Part Group)
      updatePartRequest( NON_LOOSE_PART, null );

      // Execute the query to retrieve data
      DataSet lDs = execute();

      // Ensure that no rows are returned
      assertEquals( "Number of retrieved rows", 0, lDs.getRowCount() );
   }


   /**
    * Tests that the query returns inventory in multiple locations
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQueryMultipleLocations() throws Exception {

      // Setup the Test Data (tie the Request to the proper Part/Part Group)
      updatePartRequest( MULTI_LOC_PART, null );

      // Execute the query to retrieve data
      DataSet lDs = execute();

      // Ensure that two rows are returned
      assertEquals( "Number of retrieved rows", 2, lDs.getRowCount() );

      while ( lDs.next() ) {
         LocationKey lLocationKey = lDs.getKey( LocationKey.class, "location_key" );
         if ( MULTI_LOC_LOC2.equals( lLocationKey ) ) {
            assertEquals( "avail_qt", 0, lDs.getDouble( "avail_qt" ), 0f );
            assertEquals( "total_qt", 20, lDs.getDouble( "total_qt" ), 0f );
            assertEquals( "location_key", MULTI_LOC_LOC2,
                  lDs.getKey( LocationKey.class, "location_key" ) );
            assertEquals( "part_key", MULTI_LOC_PART, lDs.getKey( PartNoKey.class, "part_key" ) );
            assertEquals( "shipping_time", null, lDs.getString( "shipping_time" ) );
            assertEquals( "decimal_places_qt", 0, lDs.getDouble( "decimal_places_qt" ), 0f );
            assertEquals( "qty_unit_cd", "EA", lDs.getString( "qty_unit_cd" ) );
         } else if ( MULTI_LOC_LOC1.equals( lLocationKey ) ) {
            assertEquals( "avail_qt", 5, lDs.getDouble( "avail_qt" ), 0f );
            assertEquals( "total_qt", 10, lDs.getDouble( "total_qt" ), 0f );
            assertEquals( "location_key", MULTI_LOC_LOC1,
                  lDs.getKey( LocationKey.class, "location_key" ) );
            assertEquals( "part_key", MULTI_LOC_PART, lDs.getKey( PartNoKey.class, "part_key" ) );
            assertEquals( "shipping_time", null, lDs.getString( "shipping_time" ) );
            assertEquals( "decimal_places_qt", 0, lDs.getDouble( "decimal_places_qt" ), 0f );
            assertEquals( "qty_unit_cd", "EA", lDs.getString( "qty_unit_cd" ) );
         } else {
            fail( "Unexpected row" );
         }
      }

   }


   /**
    * Tests that the query returns inventory of two different part numbers
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQueryMultipleParts() throws Exception {

      // Setup the Test Data (tie the Request to the proper Part/Part Group)
      updatePartRequest( null, MULTI_PART_BOM );

      // Execute the query to retrieve data
      DataSet lDs = execute();

      // Ensure that two rows are returned
      assertEquals( "Number of retrieved rows", 2, lDs.getRowCount() );

      // Test the first row
      while ( lDs.next() ) {
         LocationKey lLocationKey = lDs.getKey( LocationKey.class, "location_key" );
         PartNoKey lPartNoKey = lDs.getKey( PartNoKey.class, "part_key" );

         if ( MULTI_PART_LOC.equals( lLocationKey ) && MULTI_PART_PART2.equals( lPartNoKey ) ) {
            assertEquals( "avail_qt", 4, lDs.getDouble( "avail_qt" ), 0f );
            assertEquals( "total_qt", 8, lDs.getDouble( "total_qt" ), 0f );
            assertEquals( "location_key", MULTI_PART_LOC,
                  lDs.getKey( LocationKey.class, "location_key" ) );
            assertEquals( "part_key", MULTI_PART_PART2, lDs.getKey( PartNoKey.class, "part_key" ) );
            assertEquals( "shipping_time", null, lDs.getString( "shipping_time" ) );
            assertEquals( "decimal_places_qt", 0, lDs.getDouble( "decimal_places_qt" ), 0f );
            assertEquals( "qty_unit_cd", "EA", lDs.getString( "qty_unit_cd" ) );
         } else if ( MULTI_PART_LOC.equals( lLocationKey )
               && MULTI_PART_PART1.equals( lPartNoKey ) ) {
            assertEquals( "avail_qt", 5, lDs.getDouble( "avail_qt" ), 0f );
            assertEquals( "total_qt", 20, lDs.getDouble( "total_qt" ), 0f );
            assertEquals( "location_key", MULTI_PART_LOC,
                  lDs.getKey( LocationKey.class, "location_key" ) );
            assertEquals( "part_key", MULTI_PART_PART1, lDs.getKey( PartNoKey.class, "part_key" ) );
            assertEquals( "shipping_time", null, lDs.getString( "shipping_time" ) );
            assertEquals( "decimal_places_qt", 0, lDs.getDouble( "decimal_places_qt" ), 0f );
            assertEquals( "qty_unit_cd", "EA", lDs.getString( "qty_unit_cd" ) );
         } else {
            fail( "Unexpected row" );
         }
      }
   }


   /**
    * Tests that the query returns does not return unserviceable inventory
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQueryNonRFIInventory() throws Exception {

      // Setup the Test Data (tie the Request to the proper Part/Part Group)
      updatePartRequest( NON_RFI_PART, null );

      // Execute the query to retrieve data
      DataSet lDs = execute();

      // Ensure that no rows are returned
      assertEquals( "Number of retrieved rows", 0, lDs.getRowCount() );
   }


   /**
    * Tests that the query returns shipping time
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQueryShippingTime() throws Exception {

      // Setup the Test Data (tie the Request to the proper Part/Part Group)
      updatePartRequest( PART_WITH_SHIPPING_TIME, null );

      // Execute the query to retrieve data
      DataSet lDs = execute();

      // Ensure that two rows are returned
      assertEquals( "Number of retrieved rows", 1, lDs.getRowCount() );

      // Test the first row
      lDs.next();
      assertEquals( "shipping_time", "5", lDs.getString( "shipping_time" ) );
      assertEquals( "decimal_places_qt", 0, lDs.getDouble( "decimal_places_qt" ), 0f );
      assertEquals( "qty_unit_cd", "EA", lDs.getString( "qty_unit_cd" ) );
   }


   /**
    * Execute the query.
    *
    * @return dataSet result.
    */
   private DataSet execute() {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( PART_REQUEST_KEY, new String[] { "aReqPartDbId", "aReqPartId" } );

      // Sort the Dataset
      DataSet lDs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // Execute the query
      return lDs;
   }


   /**
    * Sets the Part and Part Group that the Test Request needs
    *
    * @param aPartNo
    *           New Part PK
    * @param aPartGroup
    *           New Part Group PK
    */
   private void updatePartRequest( PartNoKey aPartNo, PartGroupKey aPartGroup ) {
      ReqPartTable lReqPart = ReqPartTable.findByPrimaryKey( PART_REQUEST_KEY );
      lReqPart.setReqBomPart( aPartGroup );
      lReqPart.setReqSpecPartNo( aPartNo );
      lReqPart.setQuantityUnit( RefQtyUnitKey.EA );
      lReqPart.update();
   }
}
