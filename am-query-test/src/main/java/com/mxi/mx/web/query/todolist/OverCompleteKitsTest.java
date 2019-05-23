
package com.mxi.mx.web.query.todolist;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.XmlLoader;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Tests the query com.mxi.mx.web.query.todolist.OverCompleteKitsTest. This Class Tests, Kit with
 * Excess Part, Kit with Non-Standard part, Over-Complete Kits with Alternate part, Non-Standard
 * part with Kit as Incomplete, Over-Complete and Non-Standard part with Kit as Incomplete.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class OverCompleteKitsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   /**
    * Create the test data.
    *
    * @throws Exception
    */
   @BeforeClass
   public static void loadData() throws Exception {
      XmlLoader.load( sDatabaseConnectionRule.getConnection(), OverCompleteKitsTest.class,
            OverCompleteKitsData.getDataFile() );
   }


   /**
    * Tests Over-Complete kit contents like, Kit with Excess Part, Kit with Non-Standard part, Over
    * Complete Kits with Alternate part, Non-Standard part with Kit as Incomplete, Over-Complete and
    * Non-Standard part with Kit as Incomplete.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testOverCompleteKits() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aHrDbId", 4650 );
      lArgs.add( "aHrId", 7000324 );

      // execute the query
      DataSet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // Assert for No of Rows
      Assert.assertEquals( "Number of retrieved rows", 6, lDs.getRowCount() );

      // Kit with Excess part
      lDs.next();
      testRow( lDs, "01 (kit01)", new InventoryKey( "4650:391158" ),
            new InventoryKey( "4650:391160" ), "03 (batch03)", "BN 100031", 4 );

      // Kit with Non-Standard part
      lDs.next();
      testRow( lDs, "01 (kit01)", new InventoryKey( "4650:391158" ),
            new InventoryKey( "4650:391159" ), "02 (batch02)", "BN 100030", 1 );

      // Over Complete Kits with alternate part
      lDs.next();
      testRow( lDs, "100 (kit100)", new InventoryKey( "4650:391153" ),
            new InventoryKey( "4650:391156" ), "102 (batch101)", "BN 100025", 2 );

      // Non-Standard part with Kit as Incomplete
      lDs.next();
      testRow( lDs, "1000 (kit1000)", new InventoryKey( "4650:391161" ),
            new InventoryKey( "4650:391162" ), "1001 (batch1001)", "BN 100032", 1 );

      // Over-Complete and Non-Standard part with Kit as Incomplete
      // Over-Complete
      lDs.next();
      testRow( lDs, "2000 (kit2000)", new InventoryKey( "4650:391163" ),
            new InventoryKey( "4650:391164" ), "2001 (batch2001)", "BN 100033", 4 );

      // Non-Standard
      lDs.next();
      testRow( lDs, "2000 (kit2000)", new InventoryKey( "4650:391163" ),
            new InventoryKey( "4650:391165" ), "2002 (batch2002)", "BN 100034", 1 );
   }


   /**
    * Tests a row in the dataset
    *
    * @param aDs
    *           the dataset.
    * @param aPartNo
    *           the kit part no.
    * @param aKitInventoryKey
    *           the kit Inventory Key.
    * @param aPartInventoryKey
    *           the part Inventory Key.
    * @param aOemPartNo
    *           the Oem Part No.
    * @param aOemSerialNo
    *           the Oem Serial No.
    * @param aActualQty
    *           the actual Qty.
    */
   private void testRow( DataSet aDs, String aPartNo, InventoryKey aKitInventoryKey,
         InventoryKey aPartInventoryKey, String aOemPartNo, String aOemSerialNo, int aActualQty ) {

      // Check for the Kit Inventory Key
      MxAssert.assertEquals( "kit_inventory_key", aKitInventoryKey,
            aDs.getString( "kit_inventory_key" ) );

      // Check for the Kit Part no
      MxAssert.assertEquals( "part_no", aPartNo, aDs.getString( "part_no" ) );

      // Check for the Part Inventory Key
      MxAssert.assertEquals( "overcomplete_inventory_key", aPartInventoryKey,
            aDs.getString( "overcomplete_inventory_key" ) );

      // Check for the OEM Part No
      MxAssert.assertEquals( "oem_part_no", aOemPartNo, aDs.getString( "oem_part_no" ) );

      // Check for the Serial No OEM
      MxAssert.assertEquals( "serial_no_oem", aOemSerialNo, aDs.getString( "serial_no_oem" ) );

      // Check for the Actual Quantity
      MxAssert.assertEquals( "actual_qty", aActualQty, aDs.getString( "actual_qty" ) );
   }

}
