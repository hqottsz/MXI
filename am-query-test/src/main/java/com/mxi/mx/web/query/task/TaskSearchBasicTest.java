
package com.mxi.mx.web.query.task;

import java.text.SimpleDateFormat;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.XmlLoader;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Tests the query com.mxi.mx.query.task.TaskSearchBasic
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class TaskSearchBasicTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   /** DOCUMENT ME! */
   TaskSearchAssertUtil iTaskSearchValidator = new TaskSearchAssertUtil();


   /**
    * Tests the query with an invalid aircraft tail number
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testInvalidAircraft() throws Exception {
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.addWhere( "lower( inv_ac_reg.ac_reg_cd ) LIKE lower ('INVALID AIRCRAFT')" );
      lArgs.addWhere( "evt_event.hist_bool=1" );
      lArgs.addWhereAfter( "evt_event.event_gdt",
            new SimpleDateFormat( "dd-MMM-yyyy hh:mm" ).parse( "04-JAN-2007 03:36" ) );
      lArgs.addWhereBefore( "evt_event.event_gdt",
            new SimpleDateFormat( "dd-MMM-yyyy hh:mm" ).parse( "05-JAN-2007 01:00" ) );

      // execute the query
      DataSet lTasksDs = execute( lArgs, 100, 4650, 1 );

      // There should be 0 row
      MxAssert.assertEquals( "Number of retrieved rows", 0, lTasksDs.getRowCount() );
   }


   /**
    * Tests the query with an invalid Date range
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testInvalidDateRange() throws Exception {
      DataSetArgument lArgs = new DataSetArgument();

      /* Invalid Date Range */
      lArgs.addWhere( "lower( inv_ac_reg.ac_reg_cd ) LIKE lower ('AT001')" );
      lArgs.addWhere( "evt_event.hist_bool=1" );
      lArgs.addWhereAfter( "evt_event.event_gdt",
            new SimpleDateFormat( "dd-MMM-yyyy hh:mm" ).parse( "04-JAN-2007 03:36" ) );
      lArgs.addWhereBefore( "evt_event.event_gdt",
            new SimpleDateFormat( "dd-MMM-yyyy hh:mm" ).parse( "05-JAN-2007 01:00" ) );

      // execute the query
      DataSet lTasksDs = execute( lArgs, 100, 4650, 1 );

      // There should be 0 row
      MxAssert.assertEquals( "Number of retrieved rows", 0, lTasksDs.getRowCount() );
   }


   /**
    * Tests the query with an invalid task name
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testInvalidTaskName() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();

      /* Invalid Task Name */
      lArgs.addWhereLike( "WHERE_CLAUSE", "evt_event.event_sdesc", "INVALID TASK NAME" );

      // execute the query
      DataSet lTasksDs = execute( lArgs, 100, 4650, 1 );

      // There should be 0 row
      MxAssert.assertEquals( "Number of retrieved rows", 0, lTasksDs.getRowCount() );
   }


   /**
    * Tests the search by aircraft and date range query
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testSearchByAircraftAndDateRange() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();

      // build the WHERE_CLAUSE for the query
      lArgs.addWhere( "lower( inv_ac_reg.ac_reg_cd ) LIKE lower ('AT001')" );

      // add historic clause to the query
      lArgs.addWhere( "evt_event.hist_bool=1" );

      // add Completed After and Completed Before clauses to the query
      lArgs.addWhereAfter( "evt_event.event_gdt",
            new SimpleDateFormat( "dd-MMM-yyyy hh:mm" ).parse( "01-JAN-2007 03:36" ) );
      lArgs.addWhereBefore( "evt_event.event_gdt",
            new SimpleDateFormat( "dd-MMM-yyyy hh:mm" ).parse( "3-JAN-2007 01:00" ) );

      // execute the query
      DataSet lTasksDs = execute( lArgs, 100, 4650, 1 );

      // There should be 1 row
      MxAssert.assertEquals( "Number of retrieved rows", 1, lTasksDs.getRowCount() );

      lTasksDs.next();
      iTaskSearchValidator.assertTask3( lTasksDs );
   }


   /**
    * Tests the search by task name query
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testSearchByTaskName() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();

      // add Where clause to the query
      lArgs.addWhereLike( "WHERE_CLAUSE", "evt_event.event_sdesc", "aircraft test task 2" );

      // execute the query
      DataSet lTasksDs = execute( lArgs, 100, 4650, 1 );

      // There should be 1 row
      MxAssert.assertEquals( "Number of retrieved rows", 1, lTasksDs.getRowCount() );

      lTasksDs.next();
      iTaskSearchValidator.assertTask2( lTasksDs );
   }


   /**
    * tests that SearchForPartsReady returns the correct data.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testSearchForPartsNotReady() throws Exception {
      DataSetArgument lArgs = new DataSetArgument();

      // add parts ready clause to the query
      lArgs.addWhere( "sched_stask.parts_ready_bool=0" );

      // execute the query
      DataSet lTasksDs = execute( lArgs, 100, 4650, 1 );

      // There should be 1 row
      MxAssert.assertEquals( "Number of retrieved rows", 2, lTasksDs.getRowCount() );

      lTasksDs.next();
      iTaskSearchValidator.assertTask1( lTasksDs );

      lTasksDs.next();
      iTaskSearchValidator.assertTask2( lTasksDs );
   }


   /**
    * tests that SearchForPartsReady returns the correct data.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testSearchForPartsReady() throws Exception {
      DataSetArgument lArgs = new DataSetArgument();

      // add parts ready clause to the query
      lArgs.addWhere( "sched_stask.parts_ready_bool=1" );

      // execute the query
      DataSet lTasksDs = execute( lArgs, 100, 4650, 1 );

      // There should be 1 row
      MxAssert.assertEquals( "Number of retrieved rows", 2, lTasksDs.getRowCount() );

      lTasksDs.next();

      iTaskSearchValidator.assertTask3( lTasksDs );

      lTasksDs.next();

      iTaskSearchValidator.assertTask4( lTasksDs );
   }


   /**
    * tests that SearchForPartsReady returns the correct data.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testSearchForToolsNotReady() throws Exception {
      DataSetArgument lArgs = new DataSetArgument();

      // add parts ready clause to the query
      lArgs.addWhere( "sched_stask.tools_ready_bool=0" );

      // execute the query
      DataSet lTasksDs = execute( lArgs, 100, 4650, 1 );

      // There should be 1 row
      MxAssert.assertEquals( "Number of retrieved rows", 2, lTasksDs.getRowCount() );

      lTasksDs.next();
      iTaskSearchValidator.assertTask1( lTasksDs );

      lTasksDs.next();
      iTaskSearchValidator.assertTask4( lTasksDs );
   }


   /**
    * tests that SearchForPartsReady returns the correct data.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testSearchForToolsReady() throws Exception {
      DataSetArgument lArgs = new DataSetArgument();

      // add parts ready clause to the query
      lArgs.addWhere( "sched_stask.tools_ready_bool=1" );

      // execute the query
      DataSet lTasksDs = execute( lArgs, 100, 4650, 1 );

      // There should be 1 row
      MxAssert.assertEquals( "Number of retrieved rows", 2, lTasksDs.getRowCount() );

      lTasksDs.next();
      iTaskSearchValidator.assertTask2( lTasksDs );

      lTasksDs.next();
      iTaskSearchValidator.assertTask3( lTasksDs );
   }


   /**
    * Tests the query with an unauthorized user
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testUnauthorizedUser() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();

      // execute the query
      DataSet lTasksDs = execute( lArgs, 100, 4650, 1000000 );

      // There should be no row
      MxAssert.assertEquals( "Number of retrieved rows", 0, lTasksDs.getRowCount() );
   }


   /**
    * create the test data
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @BeforeClass
   public static void loadData() throws Exception {
      XmlLoader.load( sDatabaseConnectionRule.getConnection(), TaskSearchBasicTest.class,
            TaskSearchByTypeData.getDataFile() );
   }


   /**
    * Execute the query
    *
    * @param aArgs
    *           the argument set
    * @param aRowNum
    *           number of rows
    * @param aHrDbId
    *           hr_db_id
    * @param aHrId
    *           hr_id
    *
    * @return the result
    */
   private DataSet execute( DataSetArgument aArgs, Integer aRowNum, Integer aHrDbId,
         Integer aHrId ) {

      // Build query arguments
      aArgs.add( "aRowNum", aRowNum );
      aArgs.add( "aHrDbId", aHrDbId );
      aArgs.add( "aHrId", aHrId );

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), aArgs );
   }
}
