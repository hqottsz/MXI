
package com.mxi.mx.core.unittest;

import com.mxi.mx.common.dataset.DataSet;


/**
 * This class contains utility methods to be used by Mxi test cases.
 */

public class MxTestUtils {

   /**
    * Generates string made up of A's of a specified length.
    *
    * @param aLength
    *           length of the string.
    *
    * @return generated string.
    */
   public static String generateString( int aLength ) {

      StringBuilder lString = new StringBuilder();;
      for ( int lCount = 0; lCount < aLength; lCount++ ) {
         lString.append( "A" );
      }

      return lString.toString();
   }


   /**
    * This method is used to move a DataSet to an exact row based on the passed in Column / Value
    * search criteria
    *
    * @param aDataSet
    *           The Data Set
    * @param aColValPairs
    *           List of the Columns and their expected values
    */
   public static void getRow( DataSet aDataSet, Object... aColValPairs ) {
      if ( ( aColValPairs.length % 2 ) > 0 ) {
         MxAssert.fail( "Invalid number of Col/Row attributes" );
      }

      // Set the DataSet to the first row
      boolean lMatch = false;
      aDataSet.beforeFirst();

      // Search through all the rows
      while ( aDataSet.next() ) {
         lMatch = true;

         // Compare the attributes to this row
         int lIndex = 0;
         while ( aColValPairs.length > lIndex ) {
            String lColumn = aColValPairs[lIndex++].toString();
            Object lValue = aColValPairs[lIndex++];

            // Validate for Null case
            if ( aDataSet.isNull( lColumn ) ) {
               if ( lValue != null ) {
                  lMatch = false;

                  break;
               }
            }
            // Validate the non-null case
            else if ( !aDataSet.getString( lColumn ).equals( lValue.toString() ) ) {
               lMatch = false;

               break;
            }
         }

         // If the row was a match, stop looking
         if ( lMatch ) {
            break;
         }
      }

      // If no match was found, fail
      if ( !lMatch ) {
         MxAssert.fail( "Row match not found" );
      }
   }
}
