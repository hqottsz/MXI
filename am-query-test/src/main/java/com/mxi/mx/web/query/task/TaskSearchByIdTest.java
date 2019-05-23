
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
 * Tests the query com.mxi.mx.query.task.TaskSearchById
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class TaskSearchByIdTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   /** DOCUMENT ME! */
   TaskSearchAssertUtil iTaskSearchValidator = new TaskSearchAssertUtil();


   /**
    * Tests the search query with invalid RO Number
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testInvalidRONumber() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();

      // invalid RO Number
      lArgs.addWhereLike( "h_sched_stask.ro_ref_sdesc", "INVALID RO" );

      // execute the query
      DataSet lTasksDs = execute( lArgs, 100, 4650, 1 );

      // There should be 0 row
      MxAssert.assertEquals( "Number of retrieved rows", 0, lTasksDs.getRowCount() );
   }


   /**
    * Tests the search query with invalid Vendor WO Number
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testInvalidVendorWONumber() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();

      // invalid Vendor WO Number
      lArgs.addWhereLike( "h_sched_stask.ro_ref_sdesc", "INVALID VENDOR WO" );

      // execute the query
      DataSet lTasksDs = execute( lArgs, 100, 4650, 1 );

      // There should be 0 row
      MxAssert.assertEquals( "Number of retrieved rows", 0, lTasksDs.getRowCount() );
   }


   /**
    * Tests the search query with invalid WO Number
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testInvalidWONumber() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();

      // invalid WO Number
      lArgs.addWhereLike( "h_sched_stask.wo_ref_sdesc", "INVALID WO NUMBER" );

      // execute the query
      DataSet lTasksDs = execute( lArgs, 100, 4650, 1 );

      // There should be 0 row
      MxAssert.assertEquals( "Number of retrieved rows", 0, lTasksDs.getRowCount() );
   }


   /**
    * Tests the search by Id - Repair Order Number
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testSearchByRONumber() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();

      // build the WHERE_CLAUSE for the query
      lArgs.addWhereLike( "h_sched_stask.ro_ref_sdesc", "RO - 200" );

      // execute the query
      DataSet lTasksDs = execute( lArgs, 100, 4650, 1 );

      // There should be 1 row
      MxAssert.assertEquals( "Number of retrieved rows", 1, lTasksDs.getRowCount() );

      lTasksDs.next();

      iTaskSearchValidator.assertTask2( lTasksDs );
   }


   /**
    * Tests the search by Id - Vendor WO Number
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testSearchByVendorWONumber() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();

      // build the WHERE_CLAUSE for the query
      lArgs.addWhereLike( "h_sched_stask.vendor_wo_ref_sdesc", "Vendor WO - 300" );

      // execute the query
      DataSet lTasksDs = execute( lArgs, 100, 4650, 1 );

      // There should be 1 row
      MxAssert.assertEquals( "Number of retrieved rows", 1, lTasksDs.getRowCount() );

      lTasksDs.next();

      iTaskSearchValidator.assertTask3( lTasksDs );
   }


   /**
    * Tests the search by Id - WO Number
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testSearchByWONumber() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();

      // build the WHERE_CLAUSE for the query
      lArgs.addWhereLike( "h_sched_stask.wo_ref_sdesc", "WO - 100" );

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

      // There should be 2 row
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
      XmlLoader.load( sDatabaseConnectionRule.getConnection(), TaskSearchByIdTest.class,
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
      DataSet lDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), aArgs );
      lDataSet.addSort( "dsString( event_key )", true );

      return lDataSet;
   }
}
