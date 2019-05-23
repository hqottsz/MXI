
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
 * This class tests the query com.mxi.mx.core.query.adapter.logbook.api.finder.AircraftFinder.qrx
 *
 * @author hliu
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class AircraftFinderTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), AircraftFinderTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case where aircraft is returned by aircraft regiatration code and org carrier code.
    *
    * <ol>
    * <li>Query for aircraft by aircraft regiatration code and org carrier code.</li>
    * <li>Verify that the results are as expected.</li>
    * <li>Check for the values of row.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testAircraftFinder() throws Exception {
      execute( "CFYJD", "OTA" );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "5000000:98784" );
   }


   /**
    * Test the case where aircraft reg code or carrier code does not exist.
    *
    * <ol>
    * <li>Query for aircraft by aircraft regiatration code and org carrier code.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testAircraftRegCodeNotExists() throws Exception {
      execute( "BADCODE", "OTA" );
      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * This method executes the query in AircraftFinder.qrx
    *
    * @param aAircraftRegCode
    *           the Aircraft Reg Code.
    * @param aOrgCarrierCode
    *           the Org Carrier Code.
    */
   private void execute( String aAircraftRegCode, String aOrgCarrierCode ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aAircraftRegCode", aAircraftRegCode );
      lArgs.add( "aOrgCarrierCode", aOrgCarrierCode );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Method for testing a specific row returned by dataset.
    *
    * @param aAircraftPk
    *           the aircraft inventory key.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private void testRow( String aAircraftPk ) throws Exception {
      MxAssert.assertEquals( aAircraftPk, iDataSet.getString( "aircraft_pk" ) );
   }
}
