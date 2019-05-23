
package com.mxi.mx.core.query.adapter.finance;

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
 * This class performs unit testing on the query file with the same package and name.
 *
 * @author amar
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetInvoiceLinePurchaseOrdersByIdTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetInvoiceLinePurchaseOrdersByIdTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case where InvoiceLines-POs for Invoices are retrieved.
    *
    * <ol>
    * <li>Query for all POs by id.</li>
    * <li>Verify that the results are as expected.</li>
    * <li>Check for the values of row.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testInvoiceLinePurchaseOrdersByIdRetrieval() throws Exception {
      execute( 4650, 113486 );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "4650", "113486", "1", "P0100014", "1" );
   }


   /**
    * Test the case where Invoice id does not exists.
    *
    * <ol>
    * <li>Query for all PO by id.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testLinePartRequestsNotExists() throws Exception {
      execute( 4650, 107108 );
      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * Execute the query.
    *
    * @param aPoInDbId
    *           the PO Invoice db id.
    * @param aPoInId
    *           the PO Invoice id.
    */
   private void execute( int aPoInDbId, int aPoInId ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aPoInDbId", aPoInDbId );
      lArgs.add( "aPoInId", aPoInId );

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
    *           the invoice line id
    * @param aPoNumber
    *           the purchase order number.
    * @param aPoLineNumber
    *           the po line number.
    */
   private void testRow( String aPoInvoiceDbId, String aPoInvoiceId, String aPoInvoiceLineId,
         String aPoNumber, String aPoLineNumber ) {
      MxAssert.assertEquals( aPoInvoiceDbId, iDataSet.getString( "po_invoice_db_id" ) );
      MxAssert.assertEquals( aPoInvoiceId, iDataSet.getString( "po_invoice_id" ) );
      MxAssert.assertEquals( aPoInvoiceLineId, iDataSet.getString( "po_invoice_line_id" ) );
      MxAssert.assertEquals( aPoNumber, iDataSet.getString( "po_number" ) );
      MxAssert.assertEquals( aPoLineNumber, iDataSet.getString( "po_line_number" ) );
   }
}
