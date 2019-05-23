package com.mxi.am.helper;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * This class generates timestamps to assist with creating new data
 */
public final class TimestampGenerator {

   public static final String DEFAULT_PATTERN = "MM/dd/HH:mm:ss";


   public static String generate() {
      return format( DEFAULT_PATTERN, new Date() );
   }


   public static String generate( String aPattern ) {
      return format( aPattern, new Date() );
   }


   private static String format( String aPattern, Date aDate ) {
      return new SimpleDateFormat( aPattern ).format( aDate );
   }
}
