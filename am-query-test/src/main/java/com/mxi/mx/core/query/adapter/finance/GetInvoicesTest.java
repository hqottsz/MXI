
package com.mxi.mx.core.query.adapter.finance;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Assert;
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


/**
 * This class performs unit testing on the query file with the same package and name.
 *
 * @author amar
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetInvoicesTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetInvoicesTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case where Invoices are not in date range.
    *
    * <ol>
    * <li>Query for all Invoices in date range.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testInvoicesBeforeDate() throws Exception {
      execute( CoreAdapterUtils.toDate( "2007-06-01 10:00:00" ) );
      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * Test the case where Invoices in the date range are retrieved.
    *
    * <ol>
    * <li>Query for all Invoices in date range.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testInvoicesRetrieval() throws Exception {

      execute( CoreAdapterUtils.toDate( "2007-01-01 10:00:00" ) );
      assertEquals( 2, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "P0100014-INV", "mhunt", "193", "Hunt, Mark", "MXI", "Mxi Technologies", "TEST",
            CoreAdapterUtils.toDate( "2007-01-10 00:00:00" ), "USD", 1f, "NET90", 0.1f,
            CoreAdapterUtils.toDate( "2006-12-10 00:00:00" ), "This is Invoice Note." );

      iDataSet.next();
      testRow( "P0100015-INV", "mhunt", "193", "Hunt, Mark", "NO_ACCT", "No Account Vendor", null,
            CoreAdapterUtils.toDate( "2007-01-10 00:00:00" ), "USD", 1f, "NET90", 0.1f,
            CoreAdapterUtils.toDate( "2006-12-10 00:00:00" ), "This is Invoice Note." );
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
    * @param aInvoiceNumber
    *           the invoice number.
    * @param aUserName
    *           the user name.
    * @param aHrCode
    *           the hr code.
    * @param aContactName
    *           the contact name.
    * @param aVendorCode
    *           the vendor code.
    * @param aVendorName
    *           the vendor name.
    * @param aVendorAccount
    *           the vendor account.
    * @param aInvoiceDate
    *           the invoice date.
    * @param aCurrencyCode
    *           the currency.
    * @param aExchangeRate
    *           the exchange rate
    * @param aTermsConditions
    *           the terms & conditions.
    * @param aCashDiscount
    *           the cash discount percentage.
    * @param aCashDiscountDate
    *           the cash discount expiry date.
    * @param aInvoiceNote
    *           the invoice note.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private void testRow( String aInvoiceNumber, String aUserName, String aHrCode,
         String aContactName, String aVendorCode, String aVendorName, String aVendorAccount,
         Date aInvoiceDate, String aCurrencyCode, float aExchangeRate, String aTermsConditions,
         float aCashDiscount, Date aCashDiscountDate, String aInvoiceNote ) throws Exception {
      Assert.assertEquals( aInvoiceNumber, iDataSet.getString( "invoice_number" ) );
      Assert.assertEquals( aUserName, iDataSet.getString( "user_name" ) );
      Assert.assertEquals( aHrCode, iDataSet.getString( "hr_code" ) );
      Assert.assertEquals( aContactName, iDataSet.getString( "contact_name" ) );
      Assert.assertEquals( aVendorCode, iDataSet.getString( "vendor_code" ) );
      Assert.assertEquals( aVendorName, iDataSet.getString( "vendor_name" ) );
      Assert.assertEquals( aVendorAccount, iDataSet.getString( "vendor_account" ) );
      Assert.assertEquals( aInvoiceDate, iDataSet.getDate( "invoice_dt" ) );
      Assert.assertEquals( aCurrencyCode, iDataSet.getString( "currency" ) );
      Assert.assertEquals( aExchangeRate, iDataSet.getFloat( "exchange_rate" ), 0.01 );
      Assert.assertEquals( aTermsConditions, iDataSet.getString( "terms_conditions" ) );
      Assert.assertEquals( aCashDiscount, iDataSet.getFloat( "cash_discount_pct" ), 0.01 );
      Assert.assertEquals( aCashDiscountDate, iDataSet.getDate( "cash_discount_exp_dt" ) );
      Assert.assertEquals( aInvoiceNote, iDataSet.getString( "invoice_note" ) );
   }
}
