package com.mxi.mx.web.unittest.scenario.timezone;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.exception.MxRuntimeException;
import com.mxi.mx.common.key.TimeZoneKey;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.timezone.TimeZones;
import com.mxi.mx.common.timezone.TimeZonesFactory;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.services.location.LocationDetailsTO;
import com.mxi.mx.core.services.location.LocationService;
import com.mxi.mx.core.table.inv.InvLocTable;
import com.mxi.mx.testing.mock.servlet.MockHttpServletRequest;
import com.mxi.mx.testing.mock.servlet.MockHttpSession;
import com.mxi.mx.web.jsp.controller.location.EditLocationController;
import com.mxi.mx.web.jsp.controller.location.LocationInfo;


/**
 * Contains scenarios related to time zones. Can be run outside of the appserver, as long as a
 * unitest query db is accessible.
 *
 * @author gpearson
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class TimeZoneModelScenarioTest {

   @Rule
   public final DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public final FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule =
         new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static LocationService iLocationService;

   private static int iLocationId = 500;

   private static TimeZones iTimeZones;


   /**
    * Creates a location.
    *
    * @param aCode
    *           location's code and name.
    * @param aLocationType
    *           location type.
    *
    * @return key of the new location.
    */
   @SuppressWarnings( "static-access" )
   public static LocationKey createLocation( String aCode, RefLocTypeKey aLocationType ) {

      /*
       * This method uses InvLocTable because LocationService's getPrimaryKey method cannot be
       * overridden. Changing LocationService would have a very heavy impact on existing code.
       */
      LocationKey lKey = new LocationKey( 1234, iLocationId++ );
      InvLocTable lTable = InvLocTable.create( lKey );
      lTable.setLocType( aLocationType );
      lTable.insert();

      LocationDetailsTO lLocation = new LocationDetailsTO();
      try {
         lLocation.setCode( aCode );
         lLocation.setName( aCode );
         lLocation.setCountry( null );
         lLocation.setState( null );

         iLocationService.setDetails( lKey, lLocation, null );
      } catch ( Exception lEx ) {
         throw new MxRuntimeException( lEx );
      }

      return lKey;
   }


   /**
    * Creates a non-supply location.
    *
    * @param aCode
    *           the code and name of the new location.
    *
    * @return key of the new location.
    */
   public static LocationKey createNonSupplyLocation( String aCode ) {
      return createLocation( aCode, RefLocTypeKey.LINE );
   }


   /**
    * Creates an airport that is marked as a supply location.
    *
    * @param aCode
    *           the code and name of the airport.
    *
    * @return key of the new airport.
    */
   @SuppressWarnings( "static-access" )
   public static LocationKey createSupplyAirport( String aCode ) {
      LocationKey lAirportKey = createLocation( aCode, RefLocTypeKey.AIRPORT );
      try {
         iLocationService.markAsSupplyLocation( lAirportKey );
      } catch ( MxException lEx ) {
         throw new MxRuntimeException( lEx );
      }

      return lAirportKey;
   }


   /**
    * Tests the EditLocationController's behaviour when viewing a non-supply location. At the
    * moment, this amounts to ensuring {@linkplain EditLocationController#isSupplyLocation()}
    * returns false.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testIsNotSupplyLocation() throws Exception {
      LocationKey lLocation = createNonSupplyLocation( "STORE1" );
      LocationInfo lLocationInfo = new LocationInfo( lLocation );
      Assert.assertFalse( lLocationInfo.isSupplyLocation() );
   }


   /**
    * Ensures supply locations are correctly identified by
    * {@linkplain EditLocationController#isSupplyLocation()}.
    */
   @Test
   public void testIsSupplyLocation() {

      LocationKey lAirportKey = createSupplyAirport( "AIRPORT1" );
      LocationInfo lLocationInfo = new LocationInfo( lAirportKey );
      Assert.assertTrue( lLocationInfo.isSupplyLocation() );
   }


   /**
    * Ensures {@linkplain LocationService#setDetails(LocationKey, LocationDetailsTO)} can update a
    * location's time zone.
    *
    * @throws MxException
    *            if an error occurs.
    * @throws TriggerException
    *            if an error occurs.
    */
   @SuppressWarnings( "static-access" )
   @Test
   public void testLocationTimeZoneUpdate() throws MxException, TriggerException {

      LocationKey lAirportKey = createSupplyAirport( "AIRPORT1" );
      LocationDetailsTO lAirport = iLocationService.getDetails( lAirportKey );

      // Update the key to LA...
      lAirport.setTimeZoneKey( TimeZoneKey.LOS_ANGELES );
      iLocationService.setDetails( lAirportKey, lAirport, null );

      // And ensure it's really updated.
      lAirport = iLocationService.getDetails( lAirportKey );
      Assert.assertEquals( TimeZoneKey.LOS_ANGELES, lAirport.getTimeZoneKey() );

      // Update the key to Moscow...
      lAirport.setTimeZoneKey( TimeZoneKey.MOSCOW );
      iLocationService.setDetails( lAirportKey, lAirport, null );

      // ... and ensure it's really updated.
      lAirport = iLocationService.getDetails( lAirportKey );
      Assert.assertEquals( TimeZoneKey.MOSCOW, lAirport.getTimeZoneKey() );
   }


   /**
    * Tests the EditLocationController's behaviour when viewing a supply location. At the moment,
    * this amounts to ensuring {@linkplain EditLocationController#isSupplyLocation()} returns true.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @SuppressWarnings( "static-access" )
   @Test
   public void testMarkSupplyLocationDefaultTimezone() throws Exception {
      LocationKey lAirportKey = createSupplyAirport( "AIRPORT1" );
      LocationKey lSupplierKey = iLocationService.getSupplyLocation( lAirportKey );

      // Ensure the airport is a supply location.
      Assert.assertEquals( lAirportKey, lSupplierKey );

      // Ensure the airport has been assigned the default time zone.
      LocationDetailsTO lAirport = iLocationService.getDetails( lAirportKey );
      Assert.assertEquals( iTimeZones.getServerTimeZone().getKey(), lAirport.getTimeZoneKey() );
   }


   /**
    * Tests the format of time zones returned by the EditLocationController. Should be the standard
    * "code (sdesc)" format.
    */
   @Test
   public void testTimeZoneDropDownFormat() {
      DataSet lZones = TimeZonesFactory.getInstance().getTimeZonesDataSet();
      while ( lZones.next() ) {
         Assert.assertTrue(
               lZones.getString( "timezone_key" ) + ": " + lZones.getString( "timezone_name" )
                     + " is not in the valid drop-down format.",
               lZones.getString( "timezone_name" ).matches( "^.+ .+$" ) );
      }
   }


   /**
    * Verifies that Timezones dropdown list doesn't show timezones with visible_bool=false
    */
   @Test
   public void testTimeZonesDropDownAppearance() {
      DataSetArgument lArgs = new DataSetArgument();
      DataSetArgument lWhereArgs = new DataSetArgument();

      lArgs.add( "visible_bool", false );
      MxDataAccess.getInstance().executeUpdate( "utl_timezone", lArgs, null );

      lArgs.clear();
      lWhereArgs.add( "timezone_cd", "America/New_York" );
      lArgs.add( "visible_bool", true );
      MxDataAccess.getInstance().executeUpdate( "utl_timezone", lArgs, lWhereArgs );

      lWhereArgs.add( "timezone_cd", "America/Los_Angeles" );
      MxDataAccess.getInstance().executeUpdate( "utl_timezone", lArgs, lWhereArgs );

      LocationKey lAirport = createSupplyAirport( "AIRPORT1" );
      MockHttpServletRequest lRequest =
            new MockHttpServletRequest( new MockHttpSession( UserKey.ADMIN ) );
      lRequest.setParameter( "aLocation", lAirport.toString() );

      DataSet lDataSet = TimeZonesFactory.getInstance().getTimeZonesDataSet();
      Object[] lObject = lDataSet.getColumn( "timezone_key" );

      Assert.assertEquals( lObject.length, 2 );

      boolean lAssertAmerNY = false;
      boolean lAssertAmerLA = false;
      for ( int lI = 0; lI < lObject.length; lI++ ) {
         if ( lObject[lI].toString().equals( "America/New_York" ) ) {
            lAssertAmerNY = true;
         }

         if ( lObject[lI].toString().equals( "America/Los_Angeles" ) ) {
            lAssertAmerLA = true;
         }
      }

      Assert.assertTrue( lAssertAmerNY );
      Assert.assertTrue( lAssertAmerLA );
   }


   @Before
   public void setUp() throws Exception {
      iLocationService = new LocationService();
      iTimeZones = TimeZonesFactory.getInstance();

      // Set the default time zone.
      setServerTimeZone( "America/New_York" );
   }


   private void setServerTimeZone( String aTimeZoneId ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "default_bool", false );
      MxDataAccess.getInstance().executeUpdate( "utl_timezone", lArgs, null );

      lArgs.add( "visible_bool", true );
      MxDataAccess.getInstance().executeUpdate( "utl_timezone", lArgs,
            new DataSetArgument( "(STRING)timezone_cd=\'" + aTimeZoneId + "\'" ) );
   }
}
