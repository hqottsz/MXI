
package com.mxi.mx.web.query.todolist;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Tests the query com.mxi.mx.web.query.todolist.IncompleteKits.qrx
 *
 * @author brajasekaran
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class IncompleteKitsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), IncompleteKitsTest.class );
   }


   /**
    * Tests Over-Complete kit contents
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testIncompleteKits() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aHrDbId", 4650 );
      lArgs.add( "aHrId", 6000149 );

      // execute the query
      DataSet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // Test the no.of rows returned in the DataSet
      MxAssert.assertEquals( "Number of retrieved rows", 5, lDs.getRowCount() );

      // Test Row 1 and 2 - Incomplete Kit with Missing part and Part Request
      // Kit Information and Part Request Information will be common, whereas the
      // missing Part details will vary.

      lDs.next();

      // Test Kit Information
      testKitInformation( lDs, new InventoryKey( "4650:391156" ), new PartNoKey( "4650:30198" ),
            new LocationKey( "4650:100026" ), "K001(KitPart1)", "XXX", "YVR/LINE" );

      // Test Part Request Information
      testPartRequestInformation( lDs, new PartRequestKey( "4650:133899" ), "R00025KX",
            new SimpleDateFormat( "dd-MMM-yyyy hh:mm" ).parse( "30-JUL-2009 13:28" ) );

      // Test Missing Part Information for both the rows
      testMissingPartInformation( lDs, new PartGroupKey( "8000001:1347" ), "031A", -3 );
      lDs.next();
      testMissingPartInformation( lDs, new PartGroupKey( "8000001:1348" ), "031B", -4 );

      // Test Rows 3,4 and 5
      // Kit Information is common for all these 3 rows, whereas the
      // missing Part details will vary.
      lDs.next();
      testKitInformation( lDs, new InventoryKey( "4650:391153" ), new PartNoKey( "4650:30198" ),
            new LocationKey( "4650:100026" ), "K001(KitPart1)", "XXX", "YVR/LINE" );
      testMissingPartInformation( lDs, new PartGroupKey( "8000001:1348" ), "031B",
            new PartNoKey( "8000001:1090" ), "K285P03", new InventoryKey( "4650:243680" ), "XXX",
            "INSPREQ" );

      lDs.next();
      testMissingPartInformation( lDs, new PartGroupKey( "8000001:1347" ), "031A", -2 );
      lDs.next();
      testMissingPartInformation( lDs, new PartGroupKey( "8000001:1348" ), "031B", -3 );
   }


   /**
    * Asserts the Kit information
    *
    * @param aDs
    *           Incomplete Kits Data Set
    * @param aKitInventoryKey
    *           Kit Inventory Key
    * @param aPartNoKey
    *           Kit Part Number Key
    * @param aLocationKey
    *           Kit Location Key
    * @param aKitOEMPartNo
    *           Kit OEM Part Number
    * @param aKitSerialNo
    *           Kit Serial Number
    * @param aKitLocation
    *           Kit Location
    */
   private void testKitInformation( DataSet aDs, InventoryKey aKitInventoryKey,
         PartNoKey aPartNoKey, LocationKey aLocationKey, String aKitOEMPartNo, String aKitSerialNo,
         String aKitLocation ) {

      MxAssert.assertEquals( "kit_inventory_key", aKitInventoryKey,
            aDs.getString( "kit_inventory_key" ) );
      MxAssert.assertEquals( "kit_part_no_key", aPartNoKey, aDs.getString( "kit_part_no_key" ) );
      MxAssert.assertEquals( "kit_location_key", aLocationKey, aDs.getString( "kit_loc_key" ) );
      MxAssert.assertEquals( "kit_oem_part_no", aKitOEMPartNo, aDs.getString( "kit_oem_part_no" ) );
      MxAssert.assertEquals( "kit_location", aKitLocation, aDs.getString( "kit_location" ) );
      MxAssert.assertEquals( "kit_serial_no", aKitSerialNo, aDs.getString( "kit_serial_no" ) );
   }


   /**
    * Assert the missing part information.
    *
    * @param aDs
    *           Incomplete Kits Data Set
    * @param aMissingBomPartKey
    *           Missing Bom part Key
    * @param aMissingPartGroup
    *           Missing Part Group
    * @param aMissingQty
    *           Missing Quantity
    */
   private void testMissingPartInformation( DataSet aDs, PartGroupKey aMissingBomPartKey,
         String aMissingPartGroup, int aMissingQty ) {
      MxAssert.assertEquals( "missing_bom_part_key", aMissingBomPartKey,
            aDs.getString( "missing_bom_part_key" ) );
      MxAssert.assertEquals( "missing_part_group", aMissingPartGroup,
            aDs.getString( "missing_part_group" ) );
      MxAssert.assertEquals( "missing_qty", aMissingQty, aDs.getInt( "missing_qty" ) );
   }


   /**
    * Assert the missing part information for the part inventory have condition other than RFI.
    *
    * @param aDs
    *           Incomplete Kits Data Set
    * @param aMissingBomPartKey
    *           Missing Bom part Key
    * @param aMissingPartGroup
    *           Missing Part Group
    * @param aMissingPartKey
    *           Missing Part Key
    * @param aMissingPartOEMNo
    *           Missing Part OEM Number
    * @param aMissingPartInvKey
    *           Missing Part Inventory Key
    * @param aMissingPartInvSerialNo
    *           Missing Part Inventory Serial Number
    * @param aMissingPartInvCondition
    *           MIssing Part Inventory Condition
    */
   private void testMissingPartInformation( DataSet aDs, PartGroupKey aMissingBomPartKey,
         String aMissingPartGroup, PartNoKey aMissingPartKey, String aMissingPartOEMNo,
         InventoryKey aMissingPartInvKey, String aMissingPartInvSerialNo,
         String aMissingPartInvCondition ) {
      MxAssert.assertEquals( "missing_bom_part_key", aMissingBomPartKey,
            aDs.getString( "missing_bom_part_key" ) );
      MxAssert.assertEquals( "missing_part_group", aMissingPartGroup,
            aDs.getString( "missing_part_group" ) );
      MxAssert.assertEquals( "missing_part_key", aMissingPartKey,
            aDs.getString( "missing_part_key" ) );
      MxAssert.assertEquals( "missing_oem_part_no", aMissingPartOEMNo,
            aDs.getString( "missing_oem_part_no" ) );
      MxAssert.assertEquals( "missing_part_inv_key", aMissingPartInvKey,
            aDs.getString( "missing_part_inv_key" ) );
      MxAssert.assertEquals( "missing_part_inv_serial_no", aMissingPartInvSerialNo,
            aDs.getString( "missing_part_inv_serial_no" ) );
      MxAssert.assertEquals( "missing_part_inv_condition", aMissingPartInvCondition,
            aDs.getString( "missing_part_inv_condition" ) );
   }


   /**
    * Asserts the Kit Part Request Information
    *
    * @param aDs
    *           Incomplete Kits Data Set
    * @param aPartRequestKey
    *           Part Request Key
    * @param aRequestId
    *           Part Request ID
    * @param aNeededBy
    *           Part Needed By
    */
   private void testPartRequestInformation( DataSet aDs, PartRequestKey aPartRequestKey,
         String aRequestId, Date aNeededBy ) {

      MxAssert.assertEquals( "kit_req_part_key", aPartRequestKey,
            aDs.getString( "kit_req_part_key" ) );
      MxAssert.assertEquals( "request_id", aRequestId, aDs.getString( "request_id" ) );
      MxAssert.assertEquals( "needed_by", aNeededBy, aDs.getDate( "needed_by" ) );
   }
}
