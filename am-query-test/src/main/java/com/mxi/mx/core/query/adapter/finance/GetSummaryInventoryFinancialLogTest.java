
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
 * Test the query com.mxi.mx.core.query.adapter.finance.GetSummaryInventoryFinancialLog.
 *
 * @author amar
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetSummaryInventoryFinancialLogTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetSummaryInventoryFinancialLogTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case where all the accounts in the date range are returned, except FIXASSETS
    * accounts.
    *
    * <ol>
    * <li>Query for all accounts in date range.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testAccounts() throws Exception {
      execute( CoreAdapterUtils.toDate( "2000-01-01 10:00:00" ), new Date() );

      // only 4 should be there. 5th is a fixasset account.
      assertEquals( 4, iDataSet.getRowCount() );
      iDataSet.next();
      testRow( "A100", null, "EXPENSE", "TCODE-01", "0", "0" );
   }


   /**
    * Test the case where an account with credit cost exits in the date range.
    *
    * <ol>
    * <li>Query for all accounts in date range.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testAccountWithCredit() throws Exception {
      execute( CoreAdapterUtils.toDate( "2002-01-01 10:00:00" ),
            CoreAdapterUtils.toDate( "2003-01-01 10:00:00" ) );
      iDataSet.next();
      testRow( "TEST_ADJQTY", null, "ADJQTY", null, "50", "0" );
   }


   /**
    * Test the case where an account with net debit cost exits in the date range.
    *
    * <ol>
    * <li>Query for all accounts in date range.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testAccountWithCreditDebit() throws Exception {
      execute( CoreAdapterUtils.toDate( "2004-01-01 10:00:00" ),
            CoreAdapterUtils.toDate( "2005-01-01 10:00:00" ) );
      iDataSet.next();
      testRow( "EXPENSE-02", null, "EXPENSE", null, "0", "2" );
   }


   /**
    * Test the case where an account with debit cost exits in the date range.
    *
    * <ol>
    * <li>Query for all accounts in date range.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testAccountWithDebit() throws Exception {
      execute( CoreAdapterUtils.toDate( "2003-01-01 10:00:00" ),
            CoreAdapterUtils.toDate( "2004-01-01 10:00:00" ) );
      iDataSet.next();
      testRow( "EXPENSE-01", null, "EXPENSE", null, "0", "54" );
   }


   /**
    * Test the case where an account with type FIXASSET, exits in the date range and it is not
    * returned.
    *
    * <ol>
    * <li>Query for all accounts in date range.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testAccountWithFixAssetType() throws Exception {
      execute( CoreAdapterUtils.toDate( "2005-01-01 10:00:00" ),
            CoreAdapterUtils.toDate( "2006-01-01 10:00:00" ) );
      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * Execute the query.
    *
    * @param aStartDate
    *           the start date.
    * @param aEndDate
    *           the end date.
    */
   private void execute( Date aStartDate, Date aEndDate ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aStartDate", aStartDate );
      lArgs.add( "aEndDate", aEndDate );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Method for testing a specific row returned by dataset.
    *
    * @param aCode
    *           the account code.
    * @param aExternal_reference
    *           the account external reference.
    * @param aType
    *           the account type.
    * @param aTCode
    *           the t-code value.
    * @param aCredit
    *           net credit cost for account.
    * @param aDebit
    *           net debit cost for account.
    */
   private void testRow( String aCode, String aExternal_reference, String aType, String aTCode,
         String aCredit, String aDebit ) {

      MxAssert.assertEquals( aCode, iDataSet.getString( "code" ) );
      MxAssert.assertEquals( aExternal_reference, iDataSet.getString( "external_reference" ) );
      MxAssert.assertEquals( aType, iDataSet.getString( "type" ) );
      MxAssert.assertEquals( aTCode, iDataSet.getString( "t_code" ) );
      MxAssert.assertEquals( aCredit, iDataSet.getString( "credit_cost" ) );
      MxAssert.assertEquals( aDebit, iDataSet.getString( "debit_cost" ) );
   }
}
