
package com.mxi.mx.web.query.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

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
import com.mxi.mx.core.key.TaskKey;


/**
 * This class tests the query com.mxi.mx.web.query.task.GetPackageAndCompleteValidationDetails.qrx
 *
 * @author mdesouza
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetPackageAndCompleteValidationDetailsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetPackageAndCompleteValidationDetailsTest.class );
   }


   /**
    * Tests a single task returns the correct row
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testTask() throws Exception {

      DataSet lResults = execute( new TaskKey( "4650:105360" ) );

      assertEquals( 1, lResults.getRowCount() );
      lResults.next();
      assertEquals( "4650:105360", lResults.getString( "task_key" ) );
      assertNull( lResults.getString( "fault_key" ) );
      assertNull( lResults.getInteger( "ext_raised_bool" ) );
      assertNull( lResults.getInteger( "ext_controlled_bool" ) );

   }


   /**
    * Tests that a single fault returns the correct row
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testFault() throws Exception {

      DataSet lResults = execute( new TaskKey( "4650:105362" ) );

      assertEquals( 1, lResults.getRowCount() );
      lResults.next();
      assertEquals( "4650:105362", lResults.getString( "task_key" ) );
      assertEquals( "4650:106362", lResults.getString( "fault_key" ) );
      assertEquals( new Integer( 0 ), lResults.getInteger( "ext_raised_bool" ) );
      assertEquals( new Integer( 0 ), lResults.getInteger( "ext_controlled_bool" ) );
   }


   /**
    * Tests that a single externally managed fault returns the correct row
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testExternalFault() throws Exception {

      DataSet lResults = execute( new TaskKey( "4650:105363" ) );

      assertEquals( 1, lResults.getRowCount() );
      lResults.next();
      assertEquals( "4650:105363", lResults.getString( "task_key" ) );
      assertEquals( "4650:106363", lResults.getString( "fault_key" ) );
      assertEquals( new Integer( 1 ), lResults.getInteger( "ext_raised_bool" ) );
      assertEquals( new Integer( 1 ), lResults.getInteger( "ext_controlled_bool" ) );
   }


   /**
    * This method executes a query with a single task
    *
    * @param aQueryName
    *           the class name of a query
    * @param aTaskKey
    *           a list of task events
    *
    * @return The dataset after execution.
    */
   private DataSet execute( TaskKey aTaskKey ) {
      return execute( new TaskKey[] { aTaskKey } );
   }


   /**
    * This method executes a query using a list of tasks
    *
    * @param aTaskKeyList
    *           a list of task events
    *
    * @return The dataset after execution.
    */
   private DataSet execute( TaskKey[] aTaskKeyList ) {
      List<Integer> lTaskDbIds = new ArrayList<Integer>();
      List<Integer> lTaskIds = new ArrayList<Integer>();
      for ( TaskKey lTask : aTaskKeyList ) {
         lTaskDbIds.add( lTask.getDbId() );
         lTaskIds.add( lTask.getId() );
      }
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.addIntegerArray( "aEventDbIdArray", lTaskDbIds );
      lArgs.addIntegerArray( "aEventIdArray", lTaskIds );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
