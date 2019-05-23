package com.mxi.mx.util;

/**
 * This class is String utility
 *
 * @author Alicia Qian
 */
public class StringUtils {

   /**
    * append to string
    *
    */

   public static void append( StringBuffer aSource, String aStringToAppend, String aDelimiter ) {
      if ( !isBlank( aStringToAppend ) ) {
         if ( aSource.length() > 0 && aDelimiter != null ) {
            aSource.append( aDelimiter );
         }

         aSource.append( aStringToAppend );
      }

   }


   /**
    * Delimit String
    *
    */

   public static String toDelimitedString( String aDelimiter, Object... aElements ) {
      StringBuilder lBuilder = new StringBuilder();
      boolean lFirst = true;
      Object[] lobject = aElements;
      int lint = aElements.length;

      for ( int i = 0; i < lint; ++i ) {
         Object lElement = lobject[i];
         if ( lFirst ) {
            lFirst = false;
         } else {
            lBuilder.append( aDelimiter );
         }

         lBuilder.append( lElement );
      }

      return lBuilder.toString();
   }


   /**
    * Check string is blank
    * 
    * @return boolean True/False
    *
    */
   public static boolean isBlank( String aValue ) {
      return aValue == null || aValue.trim().length() < 1 || aValue.trim().equals( "null" );
   }

}
