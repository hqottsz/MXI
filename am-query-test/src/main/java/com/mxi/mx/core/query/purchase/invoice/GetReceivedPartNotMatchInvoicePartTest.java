
package com.mxi.mx.core.query.purchase.invoice;

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
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseInvoiceLineKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Ensures that <code>GetReceivedPartNotMatchInvoicePart</code> query works
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetReceivedPartNotMatchInvoicePartTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetReceivedPartNotMatchInvoicePartTest.class );
   }


   private static final PurchaseInvoiceLineKey PURCHASE_INVOICE_LINE_1 =
         new PurchaseInvoiceLineKey( 4650, 1, 1 );
   private static final PartNoKey INVOICE_PART_1 = new PartNoKey( 4650, 1 );
   private static final PartNoKey RECEIVE_PART_1 = new PartNoKey( 4650, 2 );

   private static final PurchaseInvoiceLineKey PURCHASE_INVOICE_LINE_2 =
         new PurchaseInvoiceLineKey( 4650, 2, 1 );


   /**
    * TEST CASE 2: Received part is alternate part of invoiced part. There is no warning in this
    * case.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testNoWarningWithReceivedPartIsAlternatePartOfInvoicedPart() throws Exception {
      DataSet lDataSet;

      // Passed in PO invoice line
      lDataSet = execute( PURCHASE_INVOICE_LINE_2 );

      MxAssert.assertEquals( 0, lDataSet.getRowCount() );
   }


   /**
    * TEST CASE 1: Received part is not alternate part and also not invoiced part. There is warning
    * in this case.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testWarningWithReceivedPartIsNotAlternatePartNotInvoicedPart() throws Exception {
      DataSet lDataSet;

      // Passed in PO invoice line
      lDataSet = execute( PURCHASE_INVOICE_LINE_1 );

      MxAssert.assertTrue( lDataSet.next() );

      assertRow( lDataSet, INVOICE_PART_1, RECEIVE_PART_1 );
   }


   /**
    * Tests a row in the dataset
    *
    * @param aDs
    *           the dataset
    * @param aInvoicedPartNoKey
    *           the invoiced part
    * @param aReceivedPartNoKey
    *           the received part
    */
   private void assertRow( DataSet aDs, PartNoKey aInvoicedPartNoKey,
         PartNoKey aReceivedPartNoKey ) {

      MxAssert.assertEquals( "po_invoice_part", aInvoicedPartNoKey,
            aDs.getKey( PartNoKey.class, "po_invoice_part" ) );
      MxAssert.assertEquals( "received_part", aReceivedPartNoKey,
            aDs.getKey( PartNoKey.class, "received_part" ) );
   }


   /**
    * Execute the query
    *
    * @param aPoInvoiceLineKey
    *           The PO invoice line
    *
    * @return the result
    */

   private DataSet execute( PurchaseInvoiceLineKey aPoInvoiceLineKey ) {

      // Build arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( aPoInvoiceLineKey,
            new String[] { "aPoInvoiceDbId", "aPoInvoiceId", "aPoInvoiceLineId" } );

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
