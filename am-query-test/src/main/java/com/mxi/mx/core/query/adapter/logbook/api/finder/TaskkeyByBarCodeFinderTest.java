
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
 * com.mxi.mx.core.query.adapter.logbook.api.finder.TaskByBarCodeFinder.qrx
 *
 * @author hliu
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class TaskkeyByBarCodeFinderTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), TaskkeyByBarCodeFinderTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case where task barcode does not exist.
    *
    * <ol>
    * <li>Query for task key by task barcode.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testTaskBarcodeNotExists() throws Exception {
      execute( "badBarcode" );

      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * Test the case to retrieve task key by task barcode.
    *
    * <ol>
    * <li>Query for task key by task barcode.</li>
    * <li>Verify that the results are as expected.</li>
    * <li>Check for the values of row.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testTaskkeyByBarCodeFinder() throws Exception {
      execute( "T0000K34" );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "4650:60352" );
   }


   /**
    * This method executes the query in TaskByBarCodeFinder.qrx
    *
    * @param aBarcode
    *           the task barcode.
    */
   private void execute( String aBarcode ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aBarcode", aBarcode );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Method for testing a specific row returned by dataset.
    *
    * @param aTaskKey
    *           the task key.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private void testRow( String aTaskKey ) throws Exception {
      MxAssert.assertEquals( aTaskKey, iDataSet.getString( "task_pk" ) );
   }
}
