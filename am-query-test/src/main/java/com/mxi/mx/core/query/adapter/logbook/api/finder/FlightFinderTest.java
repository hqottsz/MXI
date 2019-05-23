
package com.mxi.mx.core.query.adapter.logbook.api.finder;

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
 * This class tests the query com.mxi.mx.core.query.adapter.logbook.api.finder.FlightFinder.qrx
 *
 * @author hliu
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class FlightFinderTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), FlightFinderTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case to retrieve flight by flight name, departure date and airport.
    *
    * <ol>
    * <li>Query for flight by flight name, departure date and airport.</li>
    * <li>Verify that the results are as expected.</li>
    * <li>Check for the values of row.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testFlightFinder() throws Exception {
      execute( "2581", "2002-01-26", "ORD" );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( UuidUtils.fromHexString( "E8BB19D8191F421097D132FEE5841ADC" ) );
   }


   /**
    * Test the case where flight does not exist.
    *
    * <ol>
    * <li>Query for flight by flight name, departure date and airport.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testFlightNotExists() throws Exception {
      execute( "BadFaultFlightId", "2002-01-26", "ORD" );
      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * This method executes the query in FlightFinder.qrx
    *
    * @param aFlightName
    *           the flight name.
    * @param aScheduleDepartureDate
    *           the scheduled departure date.
    * @param aDepartureAirport
    *           the departure airport
    */
   private void execute( String aFlightName, String aScheduleDepartureDate,
         String aDepartureAirport ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aFlightName", aFlightName );
      lArgs.add( "aScheduleDepartureDate", aScheduleDepartureDate );
      lArgs.add( "aDepartureAirport", aDepartureAirport );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Method for testing a specific row returned by dataset.
    *
    * @param aFlightLegId
    *           the flight leg id.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private void testRow( UUID aFlightLegId ) throws Exception {
      MxAssert.assertEquals( aFlightLegId, iDataSet.getUuid( "flight_leg_id" ) );
   }
}
