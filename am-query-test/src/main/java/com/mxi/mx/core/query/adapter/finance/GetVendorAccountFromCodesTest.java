
package com.mxi.mx.core.query.adapter.finance;

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
 * Tests the query com.mxi.mx.core.query.adapter.finance.GetVendorAccountFromCodes
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetVendorAccountFromCodesTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetVendorAccountFromCodesTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Execute the query.
    *
    * @param aVendorCd
    *           Vendor code
    * @param aAccountCd
    *           Account code
    */
   public void execute( String aVendorCd, String aAccountCd ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aVendorCd", aVendorCd );
      lArgs.add( "aAccountCd", aAccountCd );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Tests that the query returns the proper data
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQuery() throws Exception {

      // Execute the query
      execute( "BOE", "TEST2" );

      // There should be 1 row
      MxAssert.assertEquals( "Number of retrieved rows", 1, iDataSet.getRowCount() );

      // test the row retrieved
      iDataSet.next();
      MxAssert.assertEquals( 4650, iDataSet.getInt( "vendor_db_id" ) );
      MxAssert.assertEquals( 10005, iDataSet.getInt( "vendor_id" ) );
      MxAssert.assertEquals( "TEST2", iDataSet.getString( "account_cd" ) );

      // Execute the query again
      execute( "MXI", "TEST" );

      // There should be 1 row
      MxAssert.assertEquals( "Number of retrieved rows", 1, iDataSet.getRowCount() );

      // test the row retrieved
      iDataSet.next();
      MxAssert.assertEquals( 4650, iDataSet.getInt( "vendor_db_id" ) );
      MxAssert.assertEquals( 10004, iDataSet.getInt( "vendor_id" ) );
      MxAssert.assertEquals( "TEST", iDataSet.getString( "account_cd" ) );
   }
}
