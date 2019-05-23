
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
 * com.mxi.mx.core.query.adapter.logbook.api.facade.GetOpenWorkPackageLocation.qrx
 *
 * @author hliu
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetOpenWorkPackageLocationTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetOpenWorkPackageLocationTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case to retrieve open work package location by aircraft inventory key.
    *
    * <ol>
    * <li>Query for open work package location by aircraft.</li>
    * <li>Verify that the results are as expected.</li>
    * <li>Check for the values of row.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testGetOpenWorkPackageLocation() throws Exception {
      execute( 4650, 56380 );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "4650:100029" );
   }


   /**
    * Test the case where work package does not exist by invalid aircraft.
    *
    * <ol>
    * <li>Query for open work package location by aircraft.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testOpenWorkPackageNotExists() throws Exception {
      execute( 4650, 56381 );
      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * This method executes the query in GetOpenWorkPackageLocation.qrx
    *
    * @param aInvNoDbId
    *           the aircraft inventory no db id.
    * @param aInvNoId
    *           the aircraft inventory no id.
    */
   private void execute( int aInvNoDbId, int aInvNoId ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aInvNoDbId", aInvNoDbId );
      lArgs.add( "aInvNoId", aInvNoId );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Method for testing a specific row returned by dataset.
    *
    * @param aLocationKey
    *           the location key.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private void testRow( String aLocationKey ) throws Exception {
      MxAssert.assertEquals( aLocationKey, iDataSet.getString( "location_key" ) );
   }
}
