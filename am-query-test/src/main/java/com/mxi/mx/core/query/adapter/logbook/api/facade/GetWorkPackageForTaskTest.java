
package com.mxi.mx.core.query.adapter.logbook.api.facade;

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
 * com.mxi.mx.core.query.adapter.logbook.api.facade.GetWorkPackageForTask.qrx
 *
 * @author hliu
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetWorkPackageForTaskTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetWorkPackageForTaskTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case to retrieve work package key by task key.
    *
    * <ol>
    * <li>Query for work package key by task.</li>
    * <li>Verify that the results are as expected.</li>
    * <li>Check for the values of row.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testGetWorkPackageForTask() throws Exception {
      execute( 4650, 60485 );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "4650:60479" );
   }


   /**
    * Test the case where task key does not exist.
    *
    * <ol>
    * <li>Query for work package key by task.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testTaskNotExists() throws Exception {
      execute( 4650, 60480 );

      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * This method executes the query in GetWorkPackageForTask.qrx
    *
    * @param aTaskDbId
    *           the task db id.
    * @param aTaskId
    *           the task id.
    */
   private void execute( int aTaskDbId, int aTaskId ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aTaskDbId", aTaskDbId );
      lArgs.add( "aTaskId", aTaskId );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Method for testing a specific row returned by dataset.
    *
    * @param aWorkPackageKey
    *           the work package key.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private void testRow( String aWorkPackageKey ) throws Exception {
      MxAssert.assertEquals( aWorkPackageKey, iDataSet.getString( "workpackage_pk" ) );
   }
}
