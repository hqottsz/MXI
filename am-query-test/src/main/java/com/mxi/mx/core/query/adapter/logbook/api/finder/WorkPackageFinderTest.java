
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
 * This class tests the query com.mxi.mx.core.query.adapter.logbook.api.finder.WorkPackageFinder.qrx
 *
 * @author hliu
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class WorkPackageFinderTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), WorkPackageFinderTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case where work package external key does not exist.
    *
    * <ol>
    * <li>Query for work package key by external key.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testExternalKeyNotExists() throws Exception {
      execute( "badExternalKey" );

      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * Test the case to retrieve work package key by external key
    *
    * <ol>
    * <li>Query for work package key by external key.</li>
    * <li>Verify that the results are as expected.</li>
    * <li>Check for the values of row.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testWorkPackageFinder() throws Exception {
      execute( "ExternalKey" );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "4650:60479" );
   }


   /**
    * This method executes the query in WorkPackageFinder.qrx
    *
    * @param aExternalKey
    *           the external key.
    */
   private void execute( String aExternalKey ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aExternalKey", aExternalKey );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Method for testing a specific row returned by dataset.
    *
    * @param aTaskKey
    *           the work package Key.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private void testRow( String aTaskKey ) throws Exception {
      MxAssert.assertEquals( aTaskKey, iDataSet.getString( "task_pk" ) );
   }
}
