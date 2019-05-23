
package com.mxi.mx.web.query.todolist;

import static org.junit.Assert.assertEquals;

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
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the query file with the same package and name.
 *
 * @author amar
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class TurnInTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), TurnInTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Tests the query for a specific user.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testQuery() throws Exception {
      execute( new HumanResourceKey( "4650:100" ) );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();

      testRow( "4650:1000", "TurnInTransfer", "4650:3000", "RmvdPartSDesc", "RMVDPARTOEM",
            "4650:2000", "TurnInInv", "4650:3001", "IssuedPartDesc", "ISSUEDPARTOEM", "4650:2001",
            "IssuedInvSerial", "4.0", "1", "BOX", "0", "4650:2002", "AcftInvDesc", "4650:1003",
            "PrTask", "PartRequest", "4650:1002", null, null, "4650:104", "ATL/USSTG", null,
            false );
   }


   /**
    * Execute the query.
    *
    * @param aUserHrKey
    *           the user for whom the turn-in data needs to be tested.
    */
   private void execute( HumanResourceKey aUserHrKey ) {

      // Build the arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aUserHrKey, "aHrDbId", "aHrId" );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Method for testing a specific row returned by dataset.
    *
    * @param aXferKey
    *           the transfer key.
    * @param aXferDesc
    *           the transfer description.
    * @param aRmvdPartKey
    *           the removed part key.
    * @param aRmvdPartDesc
    *           the removed part description.
    * @param aRmvdPartOem
    *           the removed part oem.
    * @param aXferInvKey
    *           the inventory to be turned-in.
    * @param aXferInvSerial
    *           to be turned-in inventory's serial no.
    * @param aIssuedPartKey
    *           the issued part key.
    * @param aIssuedPartDesc
    *           the issued part description.
    * @param aIssuedPartOem
    *           the issued part OEM no.
    * @param aInitInvKey
    *           the initiating/issued inventory.
    * @param aInitInvSerial
    *           the initiating/issued inventory serial no.
    * @param aXferQty
    *           the transfer qty.
    * @param aInvLocalBool
    *           the local inventory bool.
    * @param aQtyUnitCd
    *           the qty unit code.
    * @param aDecimalPlacesQt
    *           the decimal places value.
    * @param aAcftInvKey
    *           the highest inventory key.
    * @param aAcftDesc
    *           the highest inventory description.
    * @param aTaskKey
    *           the task (for part request) key.
    * @param aTaskDesc
    *           the task description.
    * @param aReqPartBarcode
    *           the part request description.
    * @param aReqPartKey
    *           the part request key.
    * @param aTurnInDate
    *           the turn-in date.
    * @param aExpTurnInDate
    *           the expected turn-in date.
    * @param aLocationKey
    *           the location key.
    * @param aLocationCode
    *           the location code.
    * @param aIssuedTo
    *           the inventory was issued to this hr.
    * @param aWarrantyBool
    *           the warranty bool.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private void testRow( String aXferKey, String aXferDesc, String aRmvdPartKey,
         String aRmvdPartDesc, String aRmvdPartOem, String aXferInvKey, String aXferInvSerial,
         String aIssuedPartKey, String aIssuedPartDesc, String aIssuedPartOem, String aInitInvKey,
         String aInitInvSerial, String aXferQty, String aInvLocalBool, String aQtyUnitCd,
         String aDecimalPlacesQt, String aAcftInvKey, String aAcftDesc, String aTaskKey,
         String aTaskDesc, String aReqPartBarcode, String aReqPartKey, String aTurnInDate,
         String aExpTurnInDate, String aLocationKey, String aLocationCode, String aIssuedTo,
         boolean aWarrantyBool ) throws Exception {

      MxAssert.assertEquals( aXferKey, iDataSet.getString( "transfer_key" ) );
      MxAssert.assertEquals( aXferDesc, iDataSet.getString( "transfer_event_sdesc" ) );
      MxAssert.assertEquals( aRmvdPartKey, iDataSet.getString( "removed_part_no_key" ) );
      MxAssert.assertEquals( aRmvdPartDesc, iDataSet.getString( "removed_part" ) );
      MxAssert.assertEquals( aRmvdPartOem, iDataSet.getString( "removed_part_oem" ) );
      MxAssert.assertEquals( aXferInvKey, iDataSet.getString( "xfer_inventory_key" ) );
      MxAssert.assertEquals( aXferInvSerial, iDataSet.getString( "xfer_inv_serialno_oem" ) );
      MxAssert.assertEquals( aIssuedPartKey, iDataSet.getString( "issued_part_no_key" ) );
      MxAssert.assertEquals( aIssuedPartDesc, iDataSet.getString( "issued_part" ) );
      MxAssert.assertEquals( aIssuedPartOem, iDataSet.getString( "issued_part_oem" ) );
      MxAssert.assertEquals( aInitInvKey, iDataSet.getString( "init_inventory_key" ) );
      MxAssert.assertEquals( aInitInvSerial, iDataSet.getString( "init_inv_serialno_oem" ) );
      MxAssert.assertEquals( aXferQty, iDataSet.getString( "xfer_qt" ) );
      MxAssert.assertEquals( aInvLocalBool, iDataSet.getString( "local_bool" ) );
      MxAssert.assertEquals( aQtyUnitCd, iDataSet.getString( "qty_unit_cd" ) );
      MxAssert.assertEquals( aDecimalPlacesQt, iDataSet.getString( "decimal_places_qt" ) );
      MxAssert.assertEquals( aAcftInvKey, iDataSet.getString( "aircraft_inventory_key" ) );
      MxAssert.assertEquals( aAcftDesc, iDataSet.getString( "aircraft_inv_no_sdesc" ) );
      MxAssert.assertEquals( aTaskKey, iDataSet.getString( "task_key" ) );
      MxAssert.assertEquals( aTaskDesc, iDataSet.getString( "task_event_sdesc" ) );
      MxAssert.assertEquals( aReqPartBarcode, iDataSet.getString( "req_part_barcode" ) );
      MxAssert.assertEquals( aReqPartKey, iDataSet.getString( "req_part_key" ) );
      MxAssert.assertEquals( new Date(), iDataSet.getDate( "turn_in_date" ) );
      MxAssert.assertEquals( aExpTurnInDate, iDataSet.getString( "expected_turn_in_date" ) );
      MxAssert.assertEquals( aLocationKey, iDataSet.getString( "location_key" ) );
      MxAssert.assertEquals( aLocationCode, iDataSet.getString( "loc_cd" ) );
      MxAssert.assertEquals( aIssuedTo, iDataSet.getString( "issued_to" ) );
      MxAssert.assertEquals( aWarrantyBool, iDataSet.getBoolean( "warranty_bool" ) );
   }
}
