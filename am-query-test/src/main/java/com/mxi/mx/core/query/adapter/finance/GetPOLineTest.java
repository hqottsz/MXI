
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
 * Tests the query com.mxi.mx.core.query.adapter.finance.GetPOLineTest
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetPOLineTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetPOLineTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Execute the query.
    *
    * @param aPONumber
    *           PO Number
    * @param aPOLineNumber
    *           PO Line Number
    */
   public void execute( String aPONumber, int aPOLineNumber ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aPONumber", aPONumber );
      lArgs.add( "aPOLine", aPOLineNumber );

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
      execute( "P0100034", 1 );

      // There should be 1 row
      MxAssert.assertEquals( "Number of retrieved rows", 1, iDataSet.getRowCount() );

      // test the row retrieved
      iDataSet.next();
      MxAssert.assertEquals( 4650, iDataSet.getInt( "po_db_id" ) );
      MxAssert.assertEquals( 116872, iDataSet.getInt( "po_id" ) );
      MxAssert.assertEquals( 1, iDataSet.getInt( "po_line_id" ) );

      // Execute the query
      execute( "P0100034", 2 );

      // There should be 1 row
      MxAssert.assertEquals( "Number of retrieved rows", 1, iDataSet.getRowCount() );

      // test the row retrieved
      iDataSet.next();
      MxAssert.assertEquals( 4650, iDataSet.getInt( "po_db_id" ) );
      MxAssert.assertEquals( 116872, iDataSet.getInt( "po_id" ) );
      MxAssert.assertEquals( 2, iDataSet.getInt( "po_line_id" ) );
   }
}
