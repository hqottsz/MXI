
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
 * com.mxi.mx.core.query.adapter.logbook.api.finder.HumanResourceFinder.qrx
 *
 * @author hliu
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class HumanResourceFinderTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), HumanResourceFinderTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case to retrieve human resource key by user name.
    *
    * <ol>
    * <li>Query for human resource key by user name.</li>
    * <li>Verify that the results are as expected.</li>
    * <li>Check for the values of row.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testHumanResourceFinder() throws Exception {
      execute( "mxi" );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "4650:100" );
   }


   /**
    * Test the case where user does not exist.
    *
    * <ol>
    * <li>Query for human resource key by user name.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testUserNotExists() throws Exception {
      execute( "badUser" );
      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * This method executes the query in HumanResourceFinder.qrx
    *
    * @param aUserName
    *           the user name.
    */
   private void execute( String aUserName ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aUserName", aUserName );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Method for testing a specific row returned by dataset.
    *
    * @param aHrKey
    *           the HR Key.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private void testRow( String aHrKey ) throws Exception {
      MxAssert.assertEquals( aHrKey, iDataSet.getString( "hr_key" ) );
   }
}
