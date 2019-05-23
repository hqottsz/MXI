
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
public final class GetInvoiceLinesTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetInvoiceLinesTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case where InvoiceLines for Invoices are not in date range.
    *
    * <ol>
    * <li>Query for all InvoiceLines in date range.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testInvoiceLinesBeforeDate() throws Exception {
      execute( CoreAdapterUtils.toDate( "2007-06-01 10:00:00" ) );
      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * Test the case where InvoiceLines for Invoices in the date range are retrieved.
    *
    * <ol>
    * <li>Query for all InvoiceLines in date range.</li>
    * <li>Verify that the results are as expected.</li>
    * <li>Check for the values of row.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testInvoiceLinesRetrieval() throws Exception {
      execute( CoreAdapterUtils.toDate( "2007-01-01 10:00:00" ) );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "4650", "113486", "1", "1", "SL2808-3 (ABSORBER)", "SL2808-3", "3", "20",
            "TEST_EXPENSE", "EXPENSE", null, "TCODE-01", "Line Note." );
   }


   /**
    * Execute the query.
    *
    * @param aSinceDate
    *           the date after which all purchase order invoices are required.
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
    * @param aPoInvoiceDbId
    *           the invoice db id.
    * @param aPoInvoiceId
    *           the invoice id.
    * @param aPoInvoiceLineId
    *           the invoice line id.
    * @param aLineNumber
    *           the line number.
    * @param aLineDesc
    *           the lien desc.
    * @param aPartNumber
    *           the part number.
    * @param aInvoiceQty
    *           the invoice quantity.
    * @param aUnitPrice
    *           the unit price.
    * @param aAccountCode
    *           the account code.
    * @param aAccountType
    *           the account type.
    * @param aExtKeySDesc
    *           the external key description.
    * @param aTCode
    *           the TCode.
    * @param aLineNote
    *           the line note.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private void testRow( String aPoInvoiceDbId, String aPoInvoiceId, String aPoInvoiceLineId,
         String aLineNumber, String aLineDesc, String aPartNumber, String aInvoiceQty,
         String aUnitPrice, String aAccountCode, String aAccountType, String aExtKeySDesc,
         String aTCode, String aLineNote ) throws Exception {

      MxAssert.assertEquals( aPoInvoiceDbId, iDataSet.getString( "po_invoice_db_id" ) );
      MxAssert.assertEquals( aPoInvoiceId, iDataSet.getString( "po_invoice_id" ) );
      MxAssert.assertEquals( aPoInvoiceLineId, iDataSet.getString( "po_invoice_line_id" ) );
      MxAssert.assertEquals( aLineNumber, iDataSet.getString( "line_number" ) );
      MxAssert.assertEquals( aLineDesc, iDataSet.getString( "line_desc" ) );
      MxAssert.assertEquals( aPartNumber, iDataSet.getString( "part_number" ) );
      MxAssert.assertEquals( aInvoiceQty, iDataSet.getString( "invoice_qty" ) );
      MxAssert.assertEquals( aUnitPrice, iDataSet.getString( "unit_price" ) );
      MxAssert.assertEquals( aAccountCode, iDataSet.getString( "account_cd" ) );
      MxAssert.assertEquals( aAccountType, iDataSet.getString( "account_type_cd" ) );
      MxAssert.assertEquals( aExtKeySDesc, iDataSet.getString( "ext_key_sdesc" ) );
      MxAssert.assertEquals( aTCode, iDataSet.getString( "tcode_cd" ) );
      MxAssert.assertEquals( aLineNote, iDataSet.getString( "line_note" ) );
   }
}
