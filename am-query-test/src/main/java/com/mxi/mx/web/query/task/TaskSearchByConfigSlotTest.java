
package com.mxi.mx.web.query.task;

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
 * Tests the query com.mxi.mx.query.task.TaskSearchByConfigSlot
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class TaskSearchByConfigSlotTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   /** DOCUMENT ME! */
   TaskSearchAssertUtil iTaskSearchValidator = new TaskSearchAssertUtil();


   /**
    * Tests the search by config slot query
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testInvalidInputs() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();

      // build the WHERE_CLAUSE and WHERE_CLAUSE_CONFIGSLOT clauses for the query
      lArgs.addWhere( "1=1" );
      lArgs.addWhereLike( "WHERE_CLAUSE_CONFIGSLOT", "assmbl_bom.assmbl_bom_cd", "INVALID" );

      // execute the query
      DataSet lTasksDs = execute( lArgs, 100, 4650, 1 );

      // There should be 0 row
      MxAssert.assertEquals( "Number of retrieved rows", 0, lTasksDs.getRowCount() );
   }


   /**
    * Tests the search by config slot query
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testSearchByConfigSlotAndDateRange() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();

      // build the WHERE_CLAUSE and WHERE_CLAUSE_CONFIGSLOT clauses for the query
      lArgs.addWhere( "1=1" );
      lArgs.addWhereLike( "WHERE_CLAUSE_CONFIGSLOT", "assmbl_bom.assmbl_bom_cd",
            "ASSMBL-BOM-1-111" );

      // execute the query
      DataSet lTasksDs = execute( lArgs, 100, 4650, 1 );

      // There should be 1 row
      MxAssert.assertEquals( "Number of retrieved rows", 1, lTasksDs.getRowCount() );

      lTasksDs.next();

      iTaskSearchValidator.assertTask1( lTasksDs );
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
      MxAssert.assertEquals( "Number of retrieved rows", 1, lTasksDs.getRowCount() );

      lTasksDs.next();
      iTaskSearchValidator.assertTask1( lTasksDs );
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
      MxAssert.assertEquals( "Number of retrieved rows", 0, lTasksDs.getRowCount() );
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
      MxAssert.assertEquals( "Number of retrieved rows", 1, lTasksDs.getRowCount() );

      lTasksDs.next();
      iTaskSearchValidator.assertTask1( lTasksDs );
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
      MxAssert.assertEquals( "Number of retrieved rows", 0, lTasksDs.getRowCount() );
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
      XmlLoader.load( sDatabaseConnectionRule.getConnection(), TaskSearchByConfigSlotTest.class,
            new TaskSearchByTypeData().getDataFile() );
   }


   /**
    * Execute the query
    *
    * @param aArgs
    *           The argument set
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
