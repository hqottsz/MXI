
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
public final class GetInvoiceLineTaxesByIdTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetInvoiceLineTaxesByIdTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case where InvoiceLines-Taxes for Invoices are retrieved.
    *
    * <ol>
    * <li>Query for all Taxes by id.</li>
    * <li>Verify that the results are as expected.</li>
    * <li>Check for the values of row.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testInvoiceLineTaxesByIdRetrieval() throws Exception {
      execute( 4650, 113486 );
      assertEquals( 2, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "4650", "113486", "1", "0DFEFCB01FA642AF8517933CA89F4F58", "COUNTRY" );

      iDataSet.next();
      testRow( "4650", "113486", "1", "10BE4171DF0242E3B4647ADF2BFE9CA7", "STATE" );
   }


   /**
    * Test the case where Invoice id does not exists.
    *
    * <ol>
    * <li>Query for all Taxes by id.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testLineTaxesNotExists() throws Exception {
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
    *           the invoice line id.
    * @param aTaxId
    *           the tax type code.
    * @param aTaxCd
    *           the tax amount.
    */
   private void testRow( String aPoInvoiceDbId, String aPoInvoiceId, String aPoInvoiceLineId,
         String aTaxId, String aTaxCd ) {
      MxAssert.assertEquals( aPoInvoiceDbId, iDataSet.getString( "po_invoice_db_id" ) );
      MxAssert.assertEquals( aPoInvoiceId, iDataSet.getString( "po_invoice_id" ) );
      MxAssert.assertEquals( aPoInvoiceLineId, iDataSet.getString( "po_invoice_line_id" ) );
      MxAssert.assertEquals( aTaxId, iDataSet.getString( "tax_id" ) );
      MxAssert.assertEquals( aTaxCd, iDataSet.getString( "tax_code" ) );
   }
}
