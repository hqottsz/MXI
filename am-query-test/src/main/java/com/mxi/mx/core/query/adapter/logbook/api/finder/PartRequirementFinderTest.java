
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
 * com.mxi.mx.core.query.adapter.logbook.api.finder.PartRequirementFinder.qrx
 *
 * @author hliu
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class PartRequirementFinderTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), PartRequirementFinderTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


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
      execute( 4650, 127642, "BadInstallParNo", "SN000053", "CFM56-3C", "123456" );

      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * Test the case to look up a part requirement given a task key and install/removed part
    * information (can match on partial part information (missing installed or removed information))
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
   public void testPartRequirementFinder() throws Exception {
      execute( 4650, 127642, "CFM56-3C", "SN000053", "CFM56-3C", "123456" );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "4650:127642:1" );
   }


   /**
    * Test the case to look up a part requirement given a task key and installed part information
    * (can match on partial part information (missing installed or removed information))
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
   public void testPartRequirementFinderByIntalledPart() throws Exception {
      execute( 4650, 127642, "CFM56-3C", "SN000053", null, null );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "4650:127642:1" );
   }


   /**
    * Test the case to look up a part requirement given a task key and removed part information (can
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
   public void testPartRequirementFinderByRemovedPart() throws Exception {
      execute( 4650, 127642, null, null, "CFM56-3C", "123456" );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "4650:127642:1" );
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
      execute( 4650, 127642, "CFM56-3C", "SN000053", "CFM56-3C", "BadRemovedPartSerialNo" );

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
      execute( 4650, 127640, "CFM56-3C", "SN000053", "CFM56-3C", "123456" );

      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * This method executes the query in PartRequirementFinder.qrx
    *
    * @param aTaskDbId
    *           the task db id.
    * @param aTaskId
    *           the task id.
    * @param aInstalledPartNo
    *           the installed part number
    * @param aInstalledPartSerialNo
    *           the installed part serial number
    * @param aRemovedPartNo
    *           the removed part number
    * @param aRemovedPartSerialNo
    *           the removed part serial number
    */
   private void execute( int aTaskDbId, int aTaskId, String aInstalledPartNo,
         String aInstalledPartSerialNo, String aRemovedPartNo, String aRemovedPartSerialNo ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aTaskDbId", aTaskDbId );
      lArgs.add( "aTaskId", aTaskId );

      if ( ( aInstalledPartNo != null ) && ( aInstalledPartSerialNo != null ) ) {
         lArgs.add( "aInstalledPartNo", aInstalledPartNo );
         lArgs.add( "aInstalledPartSerialNo", aInstalledPartSerialNo );
      }

      if ( ( aRemovedPartSerialNo != null ) && ( aRemovedPartNo != null ) ) {
         lArgs.add( "aRemovedPartNo", aRemovedPartNo );
         lArgs.add( "aRemovedPartSerialNo", aRemovedPartSerialNo );
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
