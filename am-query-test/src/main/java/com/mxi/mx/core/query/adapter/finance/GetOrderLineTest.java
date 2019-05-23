
package com.mxi.mx.core.query.adapter.finance;

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
import com.mxi.mx.core.adapter.CoreAdapterUtils;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the query file with the same package and name.
 *
 * @author amar
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetOrderLineTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetOrderLineTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case where POs/POLines are not in date range.
    *
    * <ol>
    * <li>Query for all POLines in date range.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testOrderLineBeforeDate() throws Exception {
      execute( CoreAdapterUtils.toDate( "2007-06-01 10:00:00" ) );
      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * Test the case where POLines for POs in the date range are retrieved.
    *
    * <ol>
    * <li>Query for all POLines in date range.</li>
    * <li>Verify that the resuts are as expected.</li>
    * <li>Check for the values of row.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testOrderLineRetrieval() throws Exception {
      execute( CoreAdapterUtils.toDate( "2007-01-01 10:00:00" ) );
      assertEquals( 2, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "4650", "107107", "1", "1", "POLINE-1", "321005656-01-M1", "1 EA", "0 EA", "10000",
            CoreAdapterUtils.toDate( "2007-02-06 13:58:00" ), true, "TEST_EXPENSE", "EXPENSE", null,
            "TCODE-01", "ext-ref_123", "This is the vendor note.", "A319/320",
            "This is the receiver note." );
   }


   /**
    * Execute the query.
    *
    * @param aSinceDate
    *           the date after which all purchase orders are required.
    */
   private void execute( Date aSinceDate ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aSinceDate", aSinceDate );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Method for testing a specific row returned by dataset.
    *
    * @param aPODbId
    *           the po db id.
    * @param aPOId
    *           the po id.
    * @param aPOLineId
    *           the po line id.
    * @param aLineNumber
    *           the line number.
    * @param aLineDesc
    *           the line description.
    * @param aPartNumber
    *           the part number.
    * @param aOrderQty
    *           the order quantity.
    * @param aReceivedQty
    *           the received quantity.
    * @param aUnitPrice
    *           the unit price.
    * @param aPromiseByDate
    *           the promised by date.
    * @param aConfirmedPromisedDate
    *           true is promised date is confirmed.
    * @param aAccountCode
    *           the account code.
    * @param aAccountType
    *           the account type.
    * @param aExtKeySDesc
    *           the external key description.
    * @param aTCode
    *           the TCode.
    * @param aPOLineExtSDesc
    *           the po line external description
    * @param aVendorNote
    *           the vendor note.
    * @param aOwner
    *           the owner code.
    * @param aReceiverNote
    *           the receiver note.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private void testRow( String aPODbId, String aPOId, String aPOLineId, String aLineNumber,
         String aLineDesc, String aPartNumber, String aOrderQty, String aReceivedQty,
         String aUnitPrice, Date aPromiseByDate, boolean aConfirmedPromisedDate,
         String aAccountCode, String aAccountType, String aExtKeySDesc, String aTCode,
         String aPOLineExtSDesc, String aVendorNote, String aOwner, String aReceiverNote )
         throws Exception {
      MxAssert.assertEquals( aPODbId, iDataSet.getString( "po_db_id" ) );
      MxAssert.assertEquals( aPOId, iDataSet.getString( "po_id" ) );

      MxAssert.assertEquals( aPOLineId, iDataSet.getString( "po_line_id" ) );
      MxAssert.assertEquals( aLineNumber, iDataSet.getString( "line_number" ) );
      MxAssert.assertEquals( aLineDesc, iDataSet.getString( "line_desc" ) );
      MxAssert.assertEquals( aPartNumber, iDataSet.getString( "part_number" ) );
      MxAssert.assertEquals( aOrderQty, iDataSet.getString( "order_qty" ) );
      MxAssert.assertEquals( aReceivedQty, iDataSet.getString( "received_qty" ) );
      MxAssert.assertEquals( aUnitPrice, iDataSet.getString( "unit_price" ) );

      MxAssert.assertEquals( aPromiseByDate, iDataSet.getDate( "promise_by_dt" ) );
      MxAssert.assertEquals( aConfirmedPromisedDate,
            iDataSet.getBoolean( "promised_dt_confirmed" ) );
      MxAssert.assertEquals( aAccountCode, iDataSet.getString( "account_cd" ) );
      MxAssert.assertEquals( aAccountType, iDataSet.getString( "account_type_cd" ) );
      MxAssert.assertEquals( aExtKeySDesc, iDataSet.getString( "ext_key_sdesc" ) );
      MxAssert.assertEquals( aTCode, iDataSet.getString( "tcode_cd" ) );
      MxAssert.assertEquals( aPOLineExtSDesc, iDataSet.getString( "po_line_ext_sdesc" ) );
      MxAssert.assertEquals( aVendorNote, iDataSet.getString( "vendor_note" ) );
      MxAssert.assertEquals( aOwner, iDataSet.getString( "owner_cd" ) );
      MxAssert.assertEquals( aReceiverNote, iDataSet.getString( "receiver_note" ) );
   }
}
