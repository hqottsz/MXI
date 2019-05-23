
package com.mxi.mx.core.query.adapter.logbook.api.service.fault;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

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
import com.mxi.mx.persistence.uuid.UuidUtils;


/**
 * This class tests the query
 * com.mxi.mx.core.query.adapter.logbook.api.service.fault.FindAircraftInFlight.qrx
 *
 * @author hliu
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class FindAircraftInFlightTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), FindAircraftInFlightTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case to looks up the aircraft that is attached to a particular flight.
    *
    * <ol>
    * <li>Query for the aircraft that is attached to a particular flight.</li>
    * <li>Verify that the results are as expected.</li>
    * <li>Check for the values of row.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testFindAircraftInFlight() throws Exception {
      execute( UuidUtils.fromHexString( "E8BB19D8191F421097D132FEE5841ADC" ) );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "4650:1234" );
   }


   /**
    * Test the case where aircraft does not exist by invalid flight.
    *
    * <ol>
    * <li>Query for the aircraft that is attached to a particular flight.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testFlightNotExists() throws Exception {
      execute( UuidUtils.fromHexString( "E8BB19D8191F421097D132FEE5841ADD" ) );
      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * This method executes the query in FindAircraftInFlight.qrx
    *
    * @param aFlightLegId
    *           the flight leg id
    */
   private void execute( UUID aFlightLegId ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aFlightLegId", aFlightLegId );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Method for testing a specific row returned by dataset.
    *
    * @param aAircraftKey
    *           the aircraft inventory key.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private void testRow( String aAircraftKey ) throws Exception {
      MxAssert.assertEquals( aAircraftKey, iDataSet.getString( "aircraft_key" ) );
   }
}
