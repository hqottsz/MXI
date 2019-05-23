
package com.mxi.mx.web.query.todolist;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;


/**
 * Tests the query com.mxi.mx.web.query.todolist.FleetDueList
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class FleetDueListTest3 {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();


   @Before
   public void loadData() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), FleetDueListTest3.class );
   }


   private DataSet execute( DataSetArgument aArgs ) {

      // Execute the query
      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.web.query.todolist.FleetDueList", aArgs );
   }


   /**
    * Tests the query includes only tasks which have a plan by date before SYSDATE + COUNT
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testTasksWithPlanByDateBeforeSysdateAreReturned() throws Exception {
      Calendar lCalendar = Calendar.getInstance();
      lCalendar.set( Calendar.YEAR, lCalendar.get( Calendar.YEAR ) + 1 );
      updatePlanByDate( lCalendar.getTime(), 201 );

      lCalendar.set( Calendar.YEAR, lCalendar.get( Calendar.YEAR ) + 2 );
      updateDeadlineDate( lCalendar.getTime(), 201 );

      List<String> lAssembliesToInclude = new ArrayList<String>();
      lAssembliesToInclude.add( "C17" );
      // execute the query
      DataSetArgument lArgs =
            new DataSetArgumentBuilder().assembliesToInclude( lAssembliesToInclude ).build();

      DataSet lTasksDs = execute( lArgs );
      // There should be 1 rows
      Assert.assertEquals( "Number of retrieved rows", 1, lTasksDs.getRowCount() );
   }


   /**
    * Tests the query includes only tasks which have a deadline before SYSDATE + COUNT
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testTasksWithDeadlineDateBeforeSysdateAreReturned() throws Exception {

      Calendar lCalendar = Calendar.getInstance();
      lCalendar.set( Calendar.YEAR, lCalendar.get( Calendar.YEAR ) + 1 );
      updateDeadlineDate( lCalendar.getTime(), 203 );

      lCalendar.set( Calendar.YEAR, lCalendar.get( Calendar.YEAR ) + 2 );
      updatePlanByDate( lCalendar.getTime(), 203 );
      List<String> lAssembliesToInclude = new ArrayList<String>();
      lAssembliesToInclude.add( "B767" );
      // execute the query
      DataSetArgument lArgs =
            new DataSetArgumentBuilder().assembliesToInclude( lAssembliesToInclude ).build();

      DataSet lTasksDs = execute( lArgs );
      // There should be 1 rows
      Assert.assertEquals( "Number of retrieved rows", 1, lTasksDs.getRowCount() );

   }


   private void updatePlanByDate( Date aDate, int aId ) throws SQLException {
      String lSql = "Update sched_stask set plan_by_dt = ? where sched_id = ?";
      executeUpdate( lSql, aDate, aId );
   }


   private void updateDeadlineDate( Date aDate, int aId ) throws SQLException {
      String lSql = "Update evt_sched_dead set sched_dead_dt = ? where event_id = ?";
      executeUpdate( lSql, aDate, aId );
   }


   private void executeUpdate( String aSql, Date aDate, int aId ) throws SQLException {
      PreparedStatement lPrepareStatement =
            iDatabaseConnectionRule.getConnection().prepareStatement( aSql );

      lPrepareStatement.setDate( 1, new java.sql.Date( aDate.getTime() ) );
      lPrepareStatement.setInt( 2, aId );
      lPrepareStatement.execute();
   }

}
