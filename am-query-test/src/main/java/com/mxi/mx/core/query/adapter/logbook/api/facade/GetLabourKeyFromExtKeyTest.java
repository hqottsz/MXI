
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
 * com.mxi.mx.core.query.adapter.logbook.api.facade.GetLabourKeyFromExtKey.qrx
 *
 * @author hliu
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetLabourKeyFromExtKeyTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetLabourKeyFromExtKeyTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case where labour does not exists by invalid external key.
    *
    * <ol>
    * <li>Query for labour key by labour external key.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testExternalKeyNotExists() throws Exception {
      execute( "BadExternalKey" );
      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * Test the case where labour key is retrieved by labour external key.
    *
    * <ol>
    * <li>Query for labour key by labour external key.</li>
    * <li>Verify that the results are as expected.</li>
    * <li>Check for the values of row.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testGetLabourKeyFromExtKey() throws Exception {
      execute( "LbrExternalKey" );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "4650:1234" );
   }


   /**
    * This method executes the query in GetLabourKeyFromExtKey.qrx
    *
    * @param aExternalKey
    *           the labour external key.
    */
   private void execute( String aExternalKey ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aExtKey", aExternalKey );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Method for testing a specific row returned by dataset.
    *
    * @param aLabourKey
    *           the labour key.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private void testRow( String aLabourKey ) throws Exception {
      MxAssert.assertEquals( aLabourKey, iDataSet.getString( "labour_key" ) );
   }
}
