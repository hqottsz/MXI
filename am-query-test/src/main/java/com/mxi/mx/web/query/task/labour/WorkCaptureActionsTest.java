
package com.mxi.mx.web.query.task.labour;

import static org.junit.Assert.assertEquals;

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
import com.mxi.mx.core.key.SchedLabourKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the query file with the same package and name.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class WorkCaptureActionsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   /** The task key */
   public static final TaskKey TASK_KEY = new TaskKey( 4650, 101 );
   public static final SchedLabourKey LABOUR_KEY = new SchedLabourKey( 4650, 200 );


   /**
    * Tests that the query returns task labour actions
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQuery() throws Exception {
      DataSet lResult = execute();

      // asserts that 3 rows are returned
      assertEquals( 3, lResult.getRowCount() );

      lResult.first();

      // technician action note
      MxAssert.assertEquals( "4650:101:1", lResult.getString( "sched_action_key" ) );
      MxAssert.assertEquals( "Action entered by technician", lResult.getString( "action_ldesc" ) );
      MxAssert.assertEquals(
            new SimpleDateFormat( "dd-MMM-yyyy hh:mm" ).parse( "21-SEP-2009 3:32" ),
            lResult.getDate( "action_dt" ) );
      MxAssert.assertEquals( "LBR", lResult.getString( "labour_skill_cd" ) );
      MxAssert.assertEquals( "Technician", lResult.getString( "labour_role" ) );
      MxAssert.assertEquals( "TECH, technician", lResult.getString( "user_name" ) );

      lResult.next();

      // certifier action note
      MxAssert.assertEquals( "4650:101:2", lResult.getString( "sched_action_key" ) );
      MxAssert.assertEquals( "Action entered by certifier", lResult.getString( "action_ldesc" ) );
      MxAssert.assertEquals(
            new SimpleDateFormat( "dd-MMM-yyyy hh:mm" ).parse( "22-SEP-2009 3:32" ),
            lResult.getDate( "action_dt" ) );
      MxAssert.assertEquals( "LBR", lResult.getString( "labour_skill_cd" ) );
      MxAssert.assertEquals( "Certifier", lResult.getString( "labour_role" ) );
      MxAssert.assertEquals( "CERT, certifier", lResult.getString( "user_name" ) );

      lResult.next();

      // inspector action note
      MxAssert.assertEquals( "4650:101:3", lResult.getString( "sched_action_key" ) );
      MxAssert.assertEquals( "Action entered by inspector", lResult.getString( "action_ldesc" ) );
      MxAssert.assertEquals(
            new SimpleDateFormat( "dd-MMM-yyyy hh:mm" ).parse( "23-SEP-2009 3:32" ),
            lResult.getDate( "action_dt" ) );
      MxAssert.assertEquals( "LBR", lResult.getString( "labour_skill_cd" ) );
      MxAssert.assertEquals( "Inspector", lResult.getString( "labour_role" ) );
      MxAssert.assertEquals( "INSP, inspector", lResult.getString( "user_name" ) );
   }


   /**
    * create the test data
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @BeforeClass
   public static void loadData() throws Exception {
      XmlLoader.load( sDatabaseConnectionRule.getConnection(), WorkCaptureActionsTest.class,
            WorkCaptureData.getDataFile() );
   }


   /**
    * Execute the query.
    *
    * @return dataSet result.
    */
   private DataSet execute() {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( TASK_KEY, "aSchedDbId", "aSchedId" );
      lArgs.add( LABOUR_KEY, "alabourdbid", "aLabourId" );

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
