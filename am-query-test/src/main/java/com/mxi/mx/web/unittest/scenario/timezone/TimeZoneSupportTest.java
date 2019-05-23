
package com.mxi.mx.web.unittest.scenario.timezone;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;


/**
 * <p>
 * Contains tests to ensure that all time zones we claim to be supported in Maintenix return
 * meaningful {@link TimeZone} classes, and perform accurate conversions.
 *
 * <p>
 *
 * @author gpearson
 * @aurhor mgharibjanian
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class TimeZoneSupportTest {

   private String[] iDeprecatedZones = new String[] { "Africa/Asmera", "Africa/Timbuktu",
         "America/Argentina/ComodRivadavia", "America/Atka", "America/Buenos_Aires",
         "America/Catamarca", "America/Coral_Harbour", "America/Cordoba", "America/Ensenada",
         "America/Fort_Wayne", "America/Indianapolis", "America/Jujuy", "America/Knox_IN",
         "America/Louisville", "America/Mendoza", "America/Porto_Acre", "America/Rosario",
         "America/Virgin", "Asia/Ashkhabad", "Asia/Chungking", "Asia/Dacca", "Asia/Calcutta",
         "Asia/Macao", "Asia/Tel_Aviv", "Asia/Saigon", "Asia/Thimbu", "Asia/Ujung_Pandang",
         "Asia/Ulan_Bator", "Atlantic/Faeroe", "Atlantic/Jan_Mayen", "Australia/ACT",
         "Australia/Canberra", "Australia/LHI", "Australia/NSW", "Australia/North",
         "Australia/Queensland", "Australia/South", "Australia/Tasmania", "Australia/Victoria",
         "Australia/West", "Australia/Yancowinna", "Brazil/Acre", "Brazil/DeNoronha", "Brazil/East",
         "Brazil/West", "Canada/Atlantic", "Canada/Central", "Canada/East-Saskatchewan",
         "Canada/Eastern", "Canada/Mountain", "Canada/Newfoundland", "Canada/Pacific",
         "Canada/Saskatchewan", "Canada/Yukon", "Chile/Continental", "Chile/EasterIsland", "Cuba",
         "Egypt", "Eire", "Europe/Belfast", "Europe/Tiraspol", "GB", "GB-Eire", "GMT+0", "GMT-0",
         "GMT0", "Greenwich", "Hongkong", "Iceland", "Iran", "Israel", "Jamaica", "Japan",
         "Kwajalein", "Libya", "Mexico/BajaNorte", "Mexico/BajaSur", "Mexico/General", "NZ",
         "NZ-CHAT", "Navajo", "PRC", "Pacific/Samoa", "Pacific/Yap", "Poland", "Portugal",

         /* "ROC" - Republic of China returns GMT in Java 5 - not supported, */
         "ROK", "Singapore", "Turkey", "UCT", "US/Alaska", "US/Aleutian", "US/Arizona",
         "US/Central", "US/East-Indiana", "US/Eastern", "US/Hawaii", "US/Indiana-Starke",
         "US/Michigan", "US/Mountain", "US/Pacific", "US/Samoa", "UTC", "Universal", "W-SU",
         "Zulu" };


   /**
    * Tests if any given date is converted correctly for a deprecated time zone and its
    * corresponding new time zone.
    */
   @Test
   public void testCurrentDateInDeprecatedTimeZones() {

      TimeZone lDeprecatedTimeZone = TimeZone.getTimeZone( "Chile/Continental" );
      TimeZone lNewTimeZone = TimeZone.getTimeZone( "America/Santiago" );

      SimpleDateFormat lDateFormat = new SimpleDateFormat( "yyyy/MM/dd HH:mm:ss" );

      Date lCurrentDate = new Date();

      // Current time in deprected time zone
      lDateFormat.setTimeZone( lDeprecatedTimeZone );

      String lCurrentTimeInDepTZ = lDateFormat.format( lCurrentDate );

      // Current time in new time zone
      lDateFormat.setTimeZone( lNewTimeZone );

      String lCurrentTimeInNewTZ = lDateFormat.format( lCurrentDate );

      // Assert both these date/time are equal
      Assert.assertTrue( lCurrentTimeInDepTZ.equals( lCurrentTimeInNewTZ ) );
   }


   /**
    * Tests if deprecated time zones offsets are equal to their corresponding new time zones.
    */
   @Test
   public void testDeprecatedTimeZonesOffset() {
      TimeZone lNewTimeZone = TimeZone.getTimeZone( "America/Santiago" );

      TimeZone lOldTimeZone = TimeZone.getTimeZone( "Chile/Continental" );

      Assert.assertTrue( lNewTimeZone.getRawOffset() == lOldTimeZone.getRawOffset() );
   }


   /**
    * <p>
    * Tests that all time zones in the "backward" list of the Olsen database are supported by Java.
    *
    * <p>
    * <p>
    * If a time zone makes this test fail, we should remove it from the 10-level utl_timezone data,
    * and update the user manual to reflect that we can no longer support it with Java version
    * <i>Y</i>.
    * </p>
    *
    * <p>
    * Sun Java returns GMT for invalid time zones, so check that any time zones highlighted by this
    * test have a non-zero offset from GMT before raising flags. e.g. In Java 5, ROC (People's
    * Republic of China) returns GMT as its time zone - it's safe to assume that this is an issue.
    * </p>
    */
   @Test
   public void testDeprecatedTimeZoneSupport() {
      TimeZone lBadZone = TimeZone
            .getTimeZone( "THIS WILL RETURN THE VALUE FOR AN INVALID TIME ZONE (typically GMT)" );

      StringBuilder lBuffer = new StringBuilder();
      for ( String lTimeZoneId : iDeprecatedZones ) {
         TimeZone lZone = TimeZone.getTimeZone( lTimeZoneId );
         if ( lZone.equals( lBadZone ) ) {
            lBuffer.append( lTimeZoneId + System.getProperty( "line.separator" ) );
         }
      }

      if ( lBuffer.length() > 0 ) {
         Assert.fail( "Some time zone IDs returned Java's 'fallback' time zone. "
               + "We may have to remove them from the list of supported time zones: "
               + System.getProperty( "line.separator" ) + lBuffer );
      }
   }
}
