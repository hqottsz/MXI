
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
public final class GetDetailedInventoryFinancialLogByIdTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetDetailedInventoryFinancialLogByIdTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case where all the transaction records are returned by id.
    *
    * <ol>
    * <li>Query for all transactions records by id.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testDetailedFinancialLogRetrievalById() throws Exception {
      execute( 4650, 100000 );

      assertEquals( 2, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "100000", "CRTINV", "2007-05-26 10:00:00", "Created 15 EA of part number BN 100113.",
            "TEST_EXPENSE", null, "EXPENSE", "TCODE-01", "0.0", "27.0" );
      iDataSet.next();
      testRow( "100000", "CRTINV", "2007-05-26 10:00:00", "Created 15 EA of part number BN 100113.",
            "TEST_INVASSET", null, "INVASSET", "TCODE-01", "27.0", "0.0" );
   }


   /**
    * Test the case where the trasaction has a FIXASSET account but the transaction type is INSP so
    * all the transaction records are returned by id.
    *
    * <ol>
    * <li>Query for all transactions records by id.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testGetDetailedFinancialLogById_INSP() throws Exception {
      execute( 4650, 100002 );

      assertEquals( 2, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "100002", "INSP", "2007-05-28 10:00:00", "Inspected shipment", "TEST_FIXASSET", null,
            "FIXASSET", "TCODE-01", "325.0", "0.0" );
      iDataSet.next();
      testRow( "100002", "INSP", "2007-05-28 10:00:00", "Inspected shipment", "TEST_INVOICE", null,
            "INVOICE", "TCODE-01", "0.0", "325.0" );
   }


   /**
    * Test the case where the trasaction has a FIXASSET account but the transaction type is PAYINVC
    * so all the transaction records are returned by id.
    *
    * <ol>
    * <li>Query for all transactions records by id.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testGetDetailedFinancialLogById_PAYINVC() throws Exception {
      execute( 4650, 100003 );

      assertEquals( 2, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "100003", "PAYINVC", "2007-05-29 10:00:00", "Payed invoice", "TEST_AP", null, "AP",
            "TCODE-01", "0.0", "425.0" );
      iDataSet.next();
      testRow( "100003", "PAYINVC", "2007-05-29 10:00:00", "Payed invoice", "TEST_INVOICE", null,
            "INVOICE", "TCODE-01", "425.0", "0.0" );
   }


   /**
    * Test the case where the trasaction has a FIXASSET account but the transaction type is RTNVEN
    * so all the transaction records are returned by id.
    *
    * <ol>
    * <li>Query for all transactions records by id.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testGetDetailedFinancialLogById_RTNVEN() throws Exception {
      execute( 4650, 100004 );

      assertEquals( 2, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "100004", "RTNVEN", "2007-05-30 10:00:00", "Returned to vendor", "TEST_FIXASSET",
            null, "FIXASSET", "TCODE-01", "0.0", "525.0" );
      iDataSet.next();
      testRow( "100004", "RTNVEN", "2007-05-30 10:00:00", "Returned to vendor", "TEST_INVOICE",
            null, "INVOICE", "TCODE-01", "525.0", "0.0" );
   }


   /**
    * Execute the query.
    *
    * @param aXActionDbId
    *           the xaction db id.
    * @param aXActionId
    *           the xaction id.
    */
   private void execute( int aXActionDbId, int aXActionId ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aTransactionDbID", aXActionDbId );
      lArgs.add( "aTransactionID", aXActionId );

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
         MxAssert.assertEquals( lActual, lExpected );
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
