
package com.mxi.mx.web.query.taskdefn;

import static org.junit.Assert.assertEquals;

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
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Query test for the RefDocSearchByPartNumber Query
 *
 * @author sdeshmukh
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class RefDocSearchByPartNumberTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            RefDocSearchByPartNumberTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case where TaskDefn(REF) are retrieved for an Part Number.
    *
    * <ol>
    * <li>Query for all TaskDefn(REF) related to Part Number.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testNoRefDocSearchByPartNumber() throws Exception {
      execute( "2-5319P2", 100 );
      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * Test the case where TaskDefn(REF) are not there for an Part Number.
    *
    * <ol>
    * <li>Query for all TaskDefn(REF) related to Part Number.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testRefDocSearchByPartNumber() throws Exception {
      execute( "2-5319P1", 1 );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "4650:15909", "80-001-REF3 (Query Test For Ref)", "REF", "ACTV", "1", "null",
            "MRB" );
   }


   /**
    * Execute the query.
    *
    * @param aPartNumber
    *           Part Number
    * @param aRow
    *           Max row to be shown.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private void execute( String aPartNumber, int aRow ) throws Exception {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aRowNum", aRow );
      lArgs.addWhereLike( "eqp_part_no.part_no_oem", aPartNumber );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Method for testing a specific row returned by dataset.
    *
    * @param aTaskKey
    *           the task task key.
    * @param aTaskName
    *           the task name no.
    * @param aTaskClassCd
    *           the task class code.
    * @param aTaskStatusCd
    *           the task status code.
    * @param aTaskRevisionOrder
    *           the task revision order.
    * @param aConfigSlot
    *           the assembly config slot.
    * @param aOriginator
    *           the task originator
    *
    * @throws Exception
    *            if an error occurs.
    */
   private void testRow( String aTaskKey, String aTaskName, String aTaskClassCd,
         String aTaskStatusCd, String aTaskRevisionOrder, String aConfigSlot, String aOriginator )
         throws Exception {

      MxAssert.assertEquals( aTaskKey, iDataSet.getString( "task_task_key" ) );
      MxAssert.assertEquals( aTaskName, iDataSet.getString( "task_cd_name" ) );
      MxAssert.assertEquals( aTaskClassCd, iDataSet.getString( "class_subclass" ) );
      MxAssert.assertEquals( aTaskStatusCd, iDataSet.getString( "task_def_status_cd" ) );
      MxAssert.assertEquals( aTaskRevisionOrder, iDataSet.getString( "revision_ord" ) );
      MxAssert.assertNull( aConfigSlot, iDataSet.getString( "config_slot_cd" ) );
      MxAssert.assertEquals( aOriginator, iDataSet.getString( "task_originator_cd" ) );
   }
}
