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
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the query file with the same package and name.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class WorkCaptureInfoTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   /** The maintenance program key */
   public static final SchedLabourKey SCHED_LABOUR_KEY = new SchedLabourKey( 4650, 200 );


   /**
    * Tests that the query returns the work capture info
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQuery() throws Exception {
      DataSet lResult = execute();

      // asserts that 2 rows are returned
      assertEquals( 2, lResult.getRowCount() );

      lResult.first();

      // technician info
      MxAssert.assertEquals( "1", lResult.getString( "technician_key" ) );
      MxAssert.assertEquals( "TECH, technician", lResult.getString( "technician_name" ) );
      MxAssert.assertEquals(
            new SimpleDateFormat( "dd-MMM-yyyy hh:mm" ).parse( "21-SEP-2009 3:32" ),
            lResult.getDate( "work_completed_on" ) );

      // certifier info
      MxAssert.assertEquals( "2", lResult.getString( "certifier_key" ) );
      MxAssert.assertEquals( "CERT, certifier", lResult.getString( "certifier_name" ) );
      MxAssert.assertEquals(
            new SimpleDateFormat( "dd-MMM-yyyy hh:mm" ).parse( "22-SEP-2009 3:32" ),
            lResult.getDate( "certification_completed_on" ) );
      MxAssert.assertEquals( 5.0, lResult.getDouble( "cert_sched_hr" ) );
      MxAssert.assertEquals( true, lResult.getBoolean( "re_certify_bool" ) );

      // inspector info
      MxAssert.assertEquals( "3", lResult.getString( "inspector_key" ) );
      MxAssert.assertEquals( "INSP, inspector", lResult.getString( "inspector_name" ) );
      MxAssert.assertEquals(
            new SimpleDateFormat( "dd-MMM-yyyy hh:mm" ).parse( "23-SEP-2009 3:32" ),
            lResult.getDate( "inspection_completed_on" ) );
      MxAssert.assertEquals( 2.0, lResult.getDouble( "insp_sched_hr" ) );
      MxAssert.assertEquals( true, lResult.getBoolean( "re_inspect_bool" ) );

      // Task Info
      MxAssert.assertEquals( "4650:101", lResult.getString( "task_key" ) );
      MxAssert.assertEquals( "Actual Task Test", lResult.getString( "event_sdesc" ) );
      MxAssert.assertEquals( "Actual Task Test Document Reference",
            lResult.getString( "doc_ref_sdesc" ) );
      MxAssert.assertEquals( "TASK101", lResult.getString( "barcode_sdesc" ) );
      MxAssert.assertEquals( "4650:1", lResult.getString( "h_work_location_key" ) );

      // Sched Labour info
      MxAssert.assertEquals( true, lResult.getBoolean( "cert_bool" ) );
      MxAssert.assertEquals( true, lResult.getBoolean( "insp_bool" ) );

      // Resolution Config Slot
      MxAssert.assertEquals( "10-20-30", lResult.getString( "resolution_config_slot_ata" ) );
      MxAssert.assertEquals( "Resolution Slot 1",
            lResult.getString( "resolution_config_slot_name" ) );
   }


   /**
    * create the test data
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @BeforeClass
   public static void loadData() throws Exception {
      XmlLoader.load( sDatabaseConnectionRule.getConnection(), WorkCaptureInfoTest.class,
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
      lArgs.add( SCHED_LABOUR_KEY, "aSchedLabourDbId", "aSchedLabourId" );

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
