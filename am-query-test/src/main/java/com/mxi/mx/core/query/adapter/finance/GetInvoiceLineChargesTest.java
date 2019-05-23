
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
public final class GetInvoiceLineChargesTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetInvoiceLineChargesTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case where Invoices-InvoiceLines-Charges are not in date range.
    *
    * <ol>
    * <li>Query for all Charges in date range.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testInvoiceLineChargesBeforeDate() throws Exception {
      execute( CoreAdapterUtils.toDate( "2007-06-01 10:00:00" ) );
      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * Test the case where InvoiceLines-Charges for Invoices in the date range are retrieved.
    *
    * <ol>
    * <li>Query for all Charges in date range.</li>
    * <li>Verify that the results are as expected.</li>
    * <li>Check for the values of row.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testInvoiceLineChargesRetrieval() throws Exception {
      execute( CoreAdapterUtils.toDate( "2007-01-01 10:00:00" ) );
      assertEquals( 2, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "4650", "113486", "1", "CUSTOMS", "2" );

      iDataSet.next();
      testRow( "4650", "113486", "1", "SHIPPING", "3" );
   }


   /**
    * Execute the query.
    *
    * @param aSinceDate
    *           the date after which all invoices are required.
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
    * @param aChargeTypeCd
    *           the charge type code.
    * @param aChargeAmount
    *           the charge amount.
    */
   private void testRow( String aPoInvoiceDbId, String aPoInvoiceId, String aPoInvoiceLineId,
         String aChargeTypeCd, String aChargeAmount ) {
      MxAssert.assertEquals( aPoInvoiceDbId, iDataSet.getString( "po_invoice_db_id" ) );
      MxAssert.assertEquals( aPoInvoiceId, iDataSet.getString( "po_invoice_id" ) );
      MxAssert.assertEquals( aPoInvoiceLineId, iDataSet.getString( "po_invoice_line_id" ) );
      MxAssert.assertEquals( aChargeTypeCd, iDataSet.getString( "charge_code" ) );
      MxAssert.assertEquals( aChargeAmount, iDataSet.getString( "charge_amount" ) );
   }
}
