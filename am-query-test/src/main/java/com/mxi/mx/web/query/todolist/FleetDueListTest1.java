
package com.mxi.mx.web.query.todolist;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;


/**
 * Tests the query com.mxi.mx.web.query.todolist.FleetDueList
 */
public final class FleetDueListTest1 {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), FleetDueListTest1.class );
   }


   private DataSet execute( DataSetArgument aArgs ) {

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), aArgs );
   }


   /**
    * Tests the query returns active actual tasks and not historical tasks
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testActualTaskIsActive() throws Exception {
      // execute the query
      DataSetArgument lArgs = new DataSetArgumentBuilder().build();

      DataSet lTasksDs = execute( lArgs );
      // There should be 2 rows
      Assert.assertEquals( "Number of retrieved rows", 2, lTasksDs.getRowCount() );

   }


   /**
    * Tests the query return only non-forecasted tasks
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testForecastedTasks() throws Exception {
      // execute the query
      DataSetArgument lArgs = new DataSetArgumentBuilder().build();

      DataSet lTasksDs = execute( lArgs );
      // There should be 2 rows
      Assert.assertEquals( "Number of retrieved rows", 2, lTasksDs.getRowCount() );

      // Each actual tasks is active
      while ( lTasksDs.next() ) {
         Assert.assertNotEquals( "FORECAST", lTasksDs.getString( "event_status_cd" ) );
      } ;
   }


   /**
    * Tests the query does not return Check or Repair Order tasks
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testTaskIsNotCheckOrRepairOrder() throws Exception {
      // execute the query
      DataSetArgument lArgs = new DataSetArgumentBuilder().build();

      DataSet lTasksDs = execute( lArgs );
      // There should be 2 rows
      Assert.assertEquals( "Number of retrieved rows", 2, lTasksDs.getRowCount() );
   }


   /**
    * Tests the query returns a task against unlocked Aircraft
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testTaskIsOnUnlockedAircraft() throws Exception {
      // execute the query
      DataSetArgument lArgs = new DataSetArgumentBuilder().build();

      DataSet lTasksDs = execute( lArgs );
      // There should be 2 rows
      Assert.assertEquals( "Number of retrieved rows", 2, lTasksDs.getRowCount() );
   }


   /**
    * Tests the query does not return a task against locked Aircraft
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testTaskIsOnLockedAircraft() throws Exception {
      // execute the query
      List<String> aAssembliesToInclude = new ArrayList<String>();
      aAssembliesToInclude.add( "F2000" );
      DataSetArgument lArgs =
            new DataSetArgumentBuilder().assembliesToInclude( aAssembliesToInclude ).build();

      DataSet lTasksDs = execute( lArgs );
      // There should be no row
      Assert.assertEquals( "Number of retrieved rows", 0, lTasksDs.getRowCount() );
   }


   /**
    * Tests the query returns tasks against Aircraft inventory
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testTaskIsAgainstAircraft() throws Exception {
      // execute the query
      DataSetArgument lArgs = new DataSetArgumentBuilder().build();

      DataSet lTasksDs = execute( lArgs );
      // There should be 2 rows
      Assert.assertEquals( "Number of retrieved rows", 2, lTasksDs.getRowCount() );
   }


   /**
    * Tests the query does not return tasks against Engine Inventory (i.e. Non-Aircraft )
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testTaskIsAgainstNonAircraft() throws Exception {
      // execute the query
      DataSetArgument lArgs = new DataSetArgumentBuilder().build();

      DataSet lTasksDs = execute( lArgs );
      // There should be 2 rows
      Assert.assertEquals( "Number of retrieved rows", 2, lTasksDs.getRowCount() );
   }


   /**
    *
    * Ensure that when the Soft Deadline Filter is set to Off, we retrieve exactly 2 rows.
    *
    * @throws Exception
    */
   @Test
   public void testSoftDeadlinesFilterOff() throws Exception {

      DataSetArgument lArgs = new DataSetArgumentBuilder().softDeadline( false ).build();

      DataSet lTasksDs = execute( lArgs );

      // There should be no row
      Assert.assertEquals( "Number of retrieved rows", 2, lTasksDs.getRowCount() );

   }


   /**
    *
    * Ensure that when the Soft Deadline Filter is set to On, we retrieve 3 rows.
    *
    * @throws Exception
    */
   @Test
   public void testSoftDeadlinesFilterOn() throws Exception {

      DataSetArgument lArgs = new DataSetArgumentBuilder().softDeadline( true ).build();

      DataSet lTasksDs = execute( lArgs );

      // There should be no row
      Assert.assertEquals( "Number of retrieved rows", 3, lTasksDs.getRowCount() );

   }


   /**
    *
    * Ensure that when the 'A320' Assembly is specified, we retrieve exactly 2 rows.
    *
    * @throws Exception
    */
   @Test
   public void testAssemblyFilter() throws Exception {

      List<String> lAssembliesToInclude = new ArrayList<String>();
      lAssembliesToInclude.add( "A320" );

      DataSetArgument lArgs =
            new DataSetArgumentBuilder().assembliesToInclude( lAssembliesToInclude ).build();

      DataSet lTasksDs = execute( lArgs );

      // There should be 2 rows
      Assert.assertEquals( "Number of retrieved rows", 2, lTasksDs.getRowCount() );

   }

}
