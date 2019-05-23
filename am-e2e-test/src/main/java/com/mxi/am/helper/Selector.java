
package com.mxi.am.helper;

import java.util.ArrayList;
import java.util.List;


/**
 * Filters a list of objects.
 */
public final class Selector {

   private Selector() {
      // utility class
   }


   /**
    * Finds the first value that matches the predicate. If the value cannot be found, a
    * {@link IllegalArgumentException} will be thrown.
    *
    * @param aValues
    *           the values
    * @param aPredicate
    *           the predicate
    * @return The first value that matches the predicate; IllegalArgumentException is thrown if no
    *         values match.
    */
   public static <T> T selectFirst( List<T> aValues, FilterCriteria<T> aPredicate ) {
      for ( T lValue : aValues ) {
         if ( aPredicate.test( lValue ) ) {
            return lValue;
         }
      }
      throw new IllegalArgumentException( "Could not find item with: " + aPredicate );
   }


   /**
    * Finds all values that matches the predicate.
    *
    * @param aValues
    *           the values
    * @param aPredicate
    *           the predicate
    * @return the list of values that match the predicate
    */
   public static <T> List<T> selectAll( List<T> aValues, FilterCriteria<T> aPredicate ) {
      List<T> lList = new ArrayList<T>();
      for ( T lValue : aValues ) {
         if ( aPredicate.test( lValue ) ) {
            lList.add( lValue );
         }
      }
      return lList;
   }
}
