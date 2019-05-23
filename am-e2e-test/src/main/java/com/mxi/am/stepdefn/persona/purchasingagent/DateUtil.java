
package com.mxi.am.stepdefn.persona.purchasingagent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Utility to get dates.
 */
public final class DateUtil {

   private DateUtil() {
      // utility class
   }


   /**
    * Parses a date with a specified pattern
    *
    * @param aPattern
    *           the pattern
    * @param aValue
    *           the date (as a string)
    * @return the date
    */
   public static Date getDate( String aPattern, String aValue ) {
      try {
         return new SimpleDateFormat( aPattern ).parse( aValue );
      } catch ( ParseException e ) {
         throw new RuntimeException( "Could not parse " + aPattern + " with " + aValue, e );
      }
   }
}
