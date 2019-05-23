
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
import com.mxi.mx.common.utils.StringUtils;
import com.mxi.mx.core.adapter.CoreAdapterUtils;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the query file with the same package and name.
 *
 * @author amar
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetDetailedInventoryFinancialLogTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetDetailedInventoryFinancialLogTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case where all the transactions in the date range are returned, except transactions
    * with FIXASSETS accounts.
    *
    * <ol>
    * <li>Query for all transactions records in date range.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testDetailedFinancialLogRetrieval() throws Exception {
      execute( CoreAdapterUtils.toDate( "2000-01-01 10:00:00" ), new Date() );

      assertEquals( 10, iDataSet.getRowCount() );
      iDataSet.next();
      testRow( "100000", "CRTINV", "2006-05-26 10:00:00", "Created 15 EA of part number BN 100113.",
            "TEST_EXPENSE", null, "EXPENSE", "TCODE-01", "0.0", "25.0" );
      iDataSet.next();
      testRow( "100000", "CRTINV", "2006-05-26 10:00:00", "Created 15 EA of part number BN 100113.",
            "TEST_INVASSET", null, "INVASSET", "TCODE-02", "25.0", "0.0" );
      iDataSet.next();
      testRow( "100002", "CRTINV", "2006-05-26 10:00:00", "Created 15 EA of part number BN 100113.",
            "TEST_EXPENSE", null, "EXPENSE", "TCODE-01", "0.0", "25.0" );
      iDataSet.next();
      testRow( "100002", "CRTINV", "2006-05-26 10:00:00", "Created 15 EA of part number BN 100113.",
            "TEST_INVASSET", null, "INVASSET", "TCODE-02", "25.0", "0.0" );
      iDataSet.next();
      testRow( "100003", "INSP", "2007-05-28 10:00:00", "Inspected shipment", "TEST_FIXASSET", null,
            "FIXASSET", "TCODE-01", "325.0", "0.0" );
      iDataSet.next();
      testRow( "100003", "INSP", "2007-05-28 10:00:00", "Inspected shipment", "TEST_INVOICE", null,
            "INVOICE", "TCODE-01", "0.0", "325.0" );
      iDataSet.next();
      testRow( "100004", "PAYINVC", "2007-05-29 10:00:00", "Payed invoice", "TEST_AP", null, "AP",
            "TCODE-01", "0.0", "425.0" );
      iDataSet.next();
      testRow( "100004", "PAYINVC", "2007-05-29 10:00:00", "Payed invoice", "TEST_INVOICE", null,
            "INVOICE", "TCODE-01", "425.0", "0.0" );
      iDataSet.next();
      testRow( "100005", "RTNVEN", "2007-05-30 10:00:00", "Returned to vendor", "TEST_FIXASSET",
            null, "FIXASSET", "TCODE-01", "0.0", "525.0" );
      iDataSet.next();
      testRow( "100005", "RTNVEN", "2007-05-30 10:00:00", "Returned to vendor", "TEST_INVOICE",
            null, "INVOICE", "TCODE-01", "525.0", "0.0" );
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
    * @param aXActionId
    *           the transaction id.
    * @param aXActionType
    *           the transaction type.
    * @param aXActionDate
    *           the transaction date.
    * @param aXActionDesc
    *           the transaction description.
    * @param aAccountCode
    *           the account code.
    * @param aAccountExtRef
    *           the account external reference.
    * @param aAccountType
    *           the account type.
    * @param aTCode
    *           the t-code value.
    * @param aCredit
    *           the credit cost for account.
    * @param aDebit
    *           the debit cost for account.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private void testRow( String aXActionId, String aXActionType, String aXActionDate,
         String aXActionDesc, String aAccountCode, String aAccountExtRef, String aAccountType,
         String aTCode, String aCredit, String aDebit ) throws Exception {
      MxAssert.assertEquals( aXActionId, iDataSet.getString( "xaction_id" ) );
      MxAssert.assertEquals( aXActionType, iDataSet.getString( "xaction_type_cd" ) );

      if ( !StringUtils.isBlank( iDataSet.getString( "xaction_dt" ) )
            && !StringUtils.isBlank( aXActionDate ) ) {
         Date lActual = iDataSet.getDate( "xaction_dt" );
         Date lExpected = CoreAdapterUtils.toDate( aXActionDate );
         MxAssert.assertEquals( lExpected, lActual );
      }

      MxAssert.assertEquals( aXActionDesc, iDataSet.getString( "xaction_ldesc" ) );

      MxAssert.assertEquals( aAccountCode, iDataSet.getString( "ACCOUNT_CD" ) );
      MxAssert.assertEquals( aAccountExtRef, iDataSet.getString( "ext_key_sdesc" ) );
      MxAssert.assertEquals( aAccountType, iDataSet.getString( "account_type_cd" ) );
      MxAssert.assertEquals( aTCode, iDataSet.getString( "tcode_cd" ) );
      MxAssert.assertEquals( aCredit, iDataSet.getString( "credit_cost" ) );
      MxAssert.assertEquals( aDebit, iDataSet.getString( "debit_cost" ) );
   }
}
