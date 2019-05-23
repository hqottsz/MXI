
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
 * com.mxi.mx.core.query.adapter.logbook.api.finder.LineLocationFinderByPartentLocationCode.qrx
 *
 * @author hliu
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class LineLocationFinderByPartentLocationCodeTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            LineLocationFinderByPartentLocationCodeTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case to retrieve line location code by airport location.
    *
    * <ol>
    * <li>Query for line location code by airport location.</li>
    * <li>Verify that the results are as expected.</li>
    * <li>Check for the values of row.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testLineLocationFinderByPartentLocationCode() throws Exception {
      execute( "CDG" );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "CDG/LINE" );
   }


   /**
    * Test the case where airport location does not exist.
    *
    * <ol>
    * <li>Query for line location code by airport location.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testLocationNotExists() throws Exception {
      execute( "BADLocationCode" );
      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * This method executes the query in LineLocationFinderByPartentLocationCode.qrx
    *
    * @param aParentLocationCode
    *           the parent (airport) location code.
    */
   private void execute( String aParentLocationCode ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aParentLocationCode", aParentLocationCode );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Method for testing a specific row returned by dataset.
    *
    * @param aLocationCd
    *           the location code.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private void testRow( String aLocationCd ) throws Exception {
      MxAssert.assertEquals( aLocationCd, iDataSet.getString( "location_cd" ) );
   }
}
