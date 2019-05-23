
package com.mxi.mx.web.query.todolist;

import static org.junit.Assert.assertEquals;

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
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the NonRepairableTab query file with the same package and
 * name.
 *
 * @author asachan
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class NonRepairableReturnsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), NonRepairableReturnsTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test Results of RepairableReturns query
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testNonRepairableTab() throws Exception {
      execute( 60 );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "3480715", "4650:2000", "INSRV", "4650:2", "ABQ", "4650:400", "ACE", "4650:1000",
            "WP-1", "4650:1001", "RmvlTask-1", "4650:1002", "Claim-1", "4650:1003", "Ship-1",
            "PEND", "4650:5000:1:1" );
   }


   /**
    * Execute the query.
    *
    * @param aNoOfDays
    *           no. of days
    */
   private void execute( int aNoOfDays ) {

      // Build the arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aNoOfDays", aNoOfDays );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Method for testing a specific row returned by dataset.
    *
    * @param aInvSerial
    *           the inventory serial no.
    * @param aInvKey
    *           the inventory key.
    * @param aInvCondition
    *           the inventory condition.
    * @param aLocKey
    *           the location key.
    * @param aLocName
    *           the location key.
    * @param aVendorKey
    *           the vendor key.
    * @param aVendorCode
    *           the vendor code.
    * @param aWPKey
    *           the work package key.
    * @param aWPDesc
    *           the work package description.
    * @param aRmvlTaskKey
    *           the removal task key.
    * @param aRmvlTaskDesc
    *           the removal task description.
    * @param aClaimKey
    *           the claim key.
    * @param aClaimDesc
    *           the claim description.
    * @param aShipmentKey
    *           the shipment key.
    * @param aShipmentDesc
    *           the shipment description.
    * @param aShipmentStatus
    *           the shipment status.
    * @param aWarrantyEvalPartKey
    *           the warranty evaluation part key.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private void testRow( String aInvSerial, String aInvKey, String aInvCondition, String aLocKey,
         String aLocName, String aVendorKey, String aVendorCode, String aWPKey, String aWPDesc,
         String aRmvlTaskKey, String aRmvlTaskDesc, String aClaimKey, String aClaimDesc,
         String aShipmentKey, String aShipmentDesc, String aShipmentStatus,
         String aWarrantyEvalPartKey ) throws Exception {

      MxAssert.assertEquals( aInvSerial, iDataSet.getString( "serial_no_oem" ) );
      MxAssert.assertEquals( aInvKey, iDataSet.getString( "inventory_key" ) );
      MxAssert.assertEquals( aInvCondition, iDataSet.getString( "inv_cond_cd" ) );
      MxAssert.assertEquals( aLocKey, iDataSet.getString( "loc_key" ) );
      MxAssert.assertEquals( aLocName, iDataSet.getString( "loc_name" ) );
      MxAssert.assertEquals( aVendorKey, iDataSet.getString( "vendor_key" ) );
      MxAssert.assertEquals( aVendorCode, iDataSet.getString( "vendor_cd" ) );
      MxAssert.assertEquals( aWPKey, iDataSet.getString( "workpackage_key" ) );
      MxAssert.assertEquals( aWPDesc, iDataSet.getString( "workpackage_sdesc" ) );
      MxAssert.assertEquals( aRmvlTaskKey, iDataSet.getString( "removal_task_key" ) );
      MxAssert.assertEquals( aRmvlTaskDesc, iDataSet.getString( "removal_task_sdesc" ) );
      MxAssert.assertEquals( aClaimKey, iDataSet.getString( "claim_key" ) );
      MxAssert.assertEquals( aClaimDesc, iDataSet.getString( "claim_sdesc" ) );
      MxAssert.assertEquals( aShipmentKey, iDataSet.getString( "shipment_key" ) );
      MxAssert.assertEquals( aShipmentDesc, iDataSet.getString( "shipment_sdesc" ) );
      MxAssert.assertEquals( aShipmentStatus, iDataSet.getString( "shipment_status" ) );
      MxAssert.assertEquals( aWarrantyEvalPartKey, iDataSet.getString( "eval_part_key" ) );
   }
}
