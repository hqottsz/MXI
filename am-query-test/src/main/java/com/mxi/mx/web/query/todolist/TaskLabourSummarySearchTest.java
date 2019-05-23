
package com.mxi.mx.web.query.todolist;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;


/**
 * Tests the query com.mxi.mx.web.query.todolist.TaskLabourSummarySearch
 */

public final class TaskLabourSummarySearchTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();


   @Before
   public void loadData() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
   }


   private final static String ASSMBL_CD = "B767-200";

   private final static String ENG = "ENG";


   /**
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testQuery() throws Exception {

      // Test case 1: Retrieve all tasks with deviation greater than 50
      DataSet lResult = execute( 50 );

      assertEquals( 1, lResult.getRowCount() );

      // Test case 2: Retrieve all tasks with deviation greater than 49
      lResult = execute( 49 );

      assertEquals( 2, lResult.getRowCount() );

      // Test case 3: Retrieve all tasks with deviation greater than 30
      lResult = execute( 30 );

      assertEquals( 3, lResult.getRowCount() );
   }


   /**
    * Execute the query.
    *
    * @param aDeviation
    *           the deviation
    *
    * @return the result
    */
   private DataSet execute( Integer aDeviation ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aAssemblyCode", ASSMBL_CD );
      lArgs.add( "aSkillCode", ENG );
      lArgs.add( "aDeviation", aDeviation );

      // Execute the query
      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }

}
