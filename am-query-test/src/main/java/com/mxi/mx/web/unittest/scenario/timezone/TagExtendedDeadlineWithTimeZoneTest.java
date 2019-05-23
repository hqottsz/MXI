
package com.mxi.mx.web.unittest.scenario.timezone;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.key.TimeZoneKey;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.timezone.TimeZonesFactory;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.web.expression.TagExtendedDeadline;
import com.mxi.mx.web.utils.SRT;


/**
 * Tests the {@link TagExtendedDeadlineWithTimeZone} tag.
 *
 * @author gpearson
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class TagExtendedDeadlineWithTimeZoneTest {

   @Rule
   public final DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public final FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule =
         new FakeJavaEeDependenciesRule();

   private static final LocationKey PARIS = new LocationKey( 1234, 1 );

   private static final LocationKey MOSCOW = new LocationKey( 1234, 2 );

   private static String iUnit = "ERG";
   private static Double iUsageRemaining = 2d;
   private static Double iDeviation = 5d;

   private SimpleDateFormat iDefaultFormat = new SimpleDateFormat( "yyyy-MMM-dd hh:mm:ss" );
   private SimpleDateFormat iMoscowFormat = new SimpleDateFormat( "yyyy-MMM-dd hh:mm:ss" );
   private SimpleDateFormat iParisFormat = new SimpleDateFormat( "yyyy-MMM-dd hh:mm:ss" );


   /**
    * Tests the result of the {@link TagExtendedDeadlineWithTimeZone} tag for calendar-based output.
    * Tests server/local time zone modes.
    *
    * @throws ParseException
    *            If a date cannot be parsed.
    */
   @Test
   public void testCalendarType() throws ParseException {

      Date lTestDate = iDefaultFormat.parse( "1999-Nov-22 20:00:00" );
      Date lExtTestDate = iDefaultFormat.parse( "1999-Nov-27 20:00:00" );

      // Get the tag result for calendar/server time zone mode.
      String lServerModeResult = getCalendarTagString( lTestDate, lExtTestDate, PARIS, false );

      // Ensure the result is relative to the server time zone.
      Assert.assertTrue( lServerModeResult.matches( ".*22-NOV-1999 20:00 EST.*" ) );

      // Get the tag result for calendar/local time zone mode.
      String lLocalModeResult = getCalendarTagString( lTestDate, lExtTestDate, PARIS, true );

      // ... and ensure it has rolled forward a day to match Paris' CET time.
      Assert.assertTrue( lLocalModeResult.matches( ".*23-NOV-1999 02:00 CET.*" ) );
   }


   /**
    * Tests the result of the {@link TagExtendedDeadlineWithTimeZone} tag for usage-based output.
    * Tests server/local time zone modes.
    *
    * @throws ParseException
    *            If a date cannot be parsed.
    */
   @Test
   public void testUsageType() throws ParseException {

      Date lTestDate = iDefaultFormat.parse( "2004-Feb-28 23:00:00" );

      // Get the tag result for calendar/server time zone mode.
      String lServerModeResult = getUsageTagString( lTestDate, MOSCOW, false );

      // Ensure the result is relative to the server time zone.
      Assert.assertTrue( lServerModeResult.matches( ".*28-FEB-2004 23:00 EST.*" ) );

      // Get the tag result for calendar/local time zone mode.
      String lLocalModeResult = getUsageTagString( lTestDate, MOSCOW, true );

      // ... and ensure it has rolled forward a day to match Moscow's CET time.
      Assert.assertTrue( lLocalModeResult.matches( ".*29-FEB-2004 07:00 MSK.*" ) );
   }


   @Before
   public void setUp() throws Exception {
      // Create a location in the Paris time zone.
      DataSetArgument lLocationData = new DataSetArgument();
      lLocationData.add( PARIS, "loc_db_id", "loc_id" );
      lLocationData.add( TimeZoneKey.PARIS, "timezone_cd" );
      MxDataAccess.getInstance().executeInsert( "INV_LOC", lLocationData );

      // Create a location in the Moscow time zone.
      lLocationData = new DataSetArgument();
      lLocationData.add( MOSCOW, "loc_db_id", "loc_id" );
      lLocationData.add( TimeZoneKey.MOSCOW, "timezone_cd" );
      MxDataAccess.getInstance().executeInsert( "INV_LOC", lLocationData );

      // Set the default format's time zone to the server's.
      iDefaultFormat.setTimeZone( TimeZone
            .getTimeZone( TimeZonesFactory.getInstance().getServerTimeZone().getKey().getCd() ) );

      iParisFormat.setTimeZone( TimeZone.getTimeZone( "Europs/Paris" ) );
      iMoscowFormat.setTimeZone( TimeZone.getTimeZone( "Europe/Moscow" ) );
   }


   private static String getCalendarTagString( Date aDate, Date aExtDate, LocationKey aLocationKey,
         boolean aLocalTimeMode ) {

      return getTagString( SRT.DomainType.CALENDAR, aDate, aExtDate, aLocationKey, aLocalTimeMode );
   }


   private static String getTagString( String aUsageType, Date aDate, Date aExtDate,
         LocationKey aLocationKey, boolean aLocalTimeMode ) {

      return TagExtendedDeadline.getContent( aUsageType, iUsageRemaining, iUnit, 0, // precision_qt
            aDate, iDeviation, aExtDate, aLocalTimeMode, aLocationKey.toString() );
   }


   private static String getUsageTagString( Date aDate, LocationKey aLocationKey,
         boolean aLocalTimeMode ) {

      return getTagString( SRT.DomainType.USAGE, aDate, aDate, aLocationKey, aLocalTimeMode );
   }
}
