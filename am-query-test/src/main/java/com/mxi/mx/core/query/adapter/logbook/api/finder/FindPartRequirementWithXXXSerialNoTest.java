
package com.mxi.mx.core.query.adapter.logbook.api.finder;

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
 * This class tests the query
 * com.mxi.mx.core.query.adapter.logbook.api.finder.FindPartRequirementWithXXXSerialNo.qrx
 *
 * @author hliu
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class FindPartRequirementWithXXXSerialNoTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            FindPartRequirementWithXXXSerialNoTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case to look up a part requirement given a task key and install/removed part number
    * (can match on partial part information (missing installed or removed part))
    *
    * <ol>
    * <li>Query for part requirement by task key and install/removed part info.</li>
    * <li>Verify that the results are as expected.</li>
    * <li>Check for the values of row.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testFindPartRequirementWithXXXSerialNo() throws Exception {
      execute( 4650, 127642, "CFM56-3C", "CFM56-3C" );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "4650:127642:1" );
   }


   /**
    * Test the case to look up a part requirement given a task key and installed part number (can
    * match on partial part information (missing installed or removed information))
    *
    * <ol>
    * <li>Query for part requirement by task key and installed part info.</li>
    * <li>Verify that the results are as expected.</li>
    * <li>Check for the values of row.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testFindPartRequirementWithXXXSerialNoByIntalledPart() throws Exception {
      execute( 4650, 127642, "CFM56-3C", null );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "4650:127642:1" );
   }


   /**
    * Test the case to look up a part requirement given a task key and removed part number (can
    * match on partial part information (missing installed or removed information))
    *
    * <ol>
    * <li>Query for part requirement by task key and removed part info.</li>
    * <li>Verify that the results are as expected.</li>
    * <li>Check for the values of row.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testFindPartRequirementWithXXXSerialNoByRemovedPart() throws Exception {
      execute( 4650, 127642, null, "CFM56-3C" );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "4650:127642:1" );
   }


   /**
    * Test the case to look up a part requirement with xxx on install part given a task key and
    * install and removed part number.
    *
    * <ol>
    * <li>Query for part requirement by task key and install/removed part info.</li>
    * <li>Verify that the results are as expected.</li>
    * <li>Check for the values of row.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testFindPartRequirementWithXXXSerialNoOnInstallGivenInstallAndRemoval()
         throws Exception {
      execute( 4650, 127642, "A28640-20", "A28640-20" );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "4650:127642:4" );
   }


   /**
    * Test the case to look up a part requirement with xxx on install part given a task key and
    * install part number
    *
    * <ol>
    * <li>Query for part requirement by task key and install/removed part info.</li>
    * <li>Verify that the results are as expected.</li>
    * <li>Check for the values of row.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testFindPartRequirementWithXXXSerialNoOnInstallGivenInstallOnly() throws Exception {
      execute( 4650, 127642, "A28640-20", null );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "4650:127642:4" );
   }


   /**
    * Test the case to look up a part requirement with xxx on install part given a task key and
    * removed part number.
    *
    * <ol>
    * <li>Query for part requirement by task key and install/removed part info.</li>
    * <li>Verify that the results are as expected.</li>
    * <li>Check for the values of row.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testFindPartRequirementWithXXXSerialNoOnInstallGivenRemovalOnly() throws Exception {
      execute( 4650, 127642, null, "A28640-20" );
      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * Test the case to look up a part requirement given a task key and install/removed part number
    * (can match on partial part information (missing installed or removed part))
    *
    * <ol>
    * <li>Query for part requirement by task key and install/removed part info.</li>
    * <li>Verify that the results are as expected.</li>
    * <li>Check for the values of row.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testFindPartRequirementWithXXXSerialNoWithInstallOnly() throws Exception {
      execute( 4650, 127642, "A28640-10", null );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "4650:127642:3" );
   }


   /**
    * Test the case where the part requirement only has installed part in Maintenix, but passed in
    * parameters with both install and removal.
    *
    * <ol>
    * <li>Query for part requirement by task key and install/removed part info.</li>
    * <li>Verify that the results are as expected.</li>
    * <li>Check for the values of row.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testFindPartRequirementWithXXXSerialNoWithInstallOnlyFailure() throws Exception {
      execute( 4650, 127642, "A28640-10", "A28640-10" );

      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * Test the case to look up a part requirement given a task key and install/removed part number
    * (can match on partial part information (missing installed or removed part))
    *
    * <ol>
    * <li>Query for part requirement by task key and install/removed part info.</li>
    * <li>Verify that the results are as expected.</li>
    * <li>Check for the values of row.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testFindPartRequirementWithXXXSerialNoWithRemovalOnly() throws Exception {
      execute( 4650, 127642, null, "A28640-10" );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "4650:127642:2" );
   }


   /**
    * Test the case where the part requirement only has removal part in Maintenix, but passed in
    * parameters with both install and removal.
    *
    * <ol>
    * <li>Query for part requirement by task key and install/removed part info.</li>
    * <li>Verify that the results are as expected.</li>
    * <li>Check for the values of row.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testFindPartRequirementWithXXXSerialNoWithRemovalOnlyFailure() throws Exception {
      execute( 4650, 127642, "A28640-10", "A28640-10" );

      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * Test the case where install part does not exist.
    *
    * <ol>
    * <li>Query for wpart requirement by task key and install/removed part info.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testInstallPartNotExists() throws Exception {
      execute( 4650, 127642, "BadInstPartNo", "CFM56-3C" );

      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * Test the case where removed part does not exist.
    *
    * <ol>
    * <li>Query for wpart requirement by task key and install/removed part info.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testRemovedPartNotExists() throws Exception {
      execute( 4650, 127642, "CFM56-3C", "BadRmvdPartNo" );

      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * Test the case where task does not exist.
    *
    * <ol>
    * <li>Query for wpart requirement by task key and install/removed part info.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testTaskNotExists() throws Exception {
      execute( 4650, 127640, "CFM56-3C", "CFM56-3C" );

      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * This method executes the query in FindPartRequirementWithXXXSerialNo.qrx
    *
    * @param aTaskDbId
    *           the task db id.
    * @param aTaskId
    *           the task id.
    * @param aInstalledPartNo
    *           the installed part number
    * @param aRemovedPartNo
    *           the removed part number
    */
   private void execute( int aTaskDbId, int aTaskId, String aInstalledPartNo,
         String aRemovedPartNo ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aTaskDbId", aTaskDbId );
      lArgs.add( "aTaskId", aTaskId );

      if ( aInstalledPartNo != null ) {
         lArgs.add( "aInstalledPartNo", aInstalledPartNo );
      } else {
         lArgs.add( "aInstalledPartNo", "null" );
      }

      if ( aRemovedPartNo != null ) {
         lArgs.add( "aRemovedPartNo", aRemovedPartNo );
      } else {
         lArgs.add( "aRemovedPartNo", "null" );
      }

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Method for testing a specific row returned by dataset.
    *
    * @param aTaskPartKey
    *           the task part key Key.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private void testRow( String aTaskPartKey ) throws Exception {
      MxAssert.assertEquals( aTaskPartKey, iDataSet.getString( "sched_part_pk" ) );
   }
}
