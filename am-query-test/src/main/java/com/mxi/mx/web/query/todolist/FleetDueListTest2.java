
package com.mxi.mx.web.query.todolist;

import java.util.ArrayList;
import java.util.List;

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


/**
 * Tests the query com.mxi.mx.web.query.todolist.FleetDueList. We have isolated this test from the
 * others in order to simplify the data file.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class FleetDueListTest2 {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), FleetDueListTest2.class );
   }


   private DataSet execute( DataSetArgument aArgs ) {

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.web.query.todolist.FleetDueList", aArgs );
   }


   /**
    * Tests that the query does not return historical Repair Order tasks
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testThatHistoricRepairOrdersAreNotReturned() throws Exception {
      List<String> aAssembliesToInclude = new ArrayList<String>();
      aAssembliesToInclude.add( "C17" );
      DataSetArgument lArgs =
            new DataSetArgumentBuilder().assembliesToInclude( aAssembliesToInclude ).build();

      DataSet lTasksDs = execute( lArgs );
      // There should be 1 rows
      Assert.assertEquals( "Number of retrieved rows", 1, lTasksDs.getRowCount() );

   }

}
