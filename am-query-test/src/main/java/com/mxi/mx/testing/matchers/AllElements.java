
package com.mxi.mx.testing.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;


/**
 * This class iterates over a list of values to validate that all elements match.
 */
public class AllElements<T> extends TypeSafeMatcher<Iterable<T>> {

   private final Matcher<T> iMatcher;


   /**
    * Creates a new {@linkplain AnyElements} object.
    *
    * @param aMatcher
    *           the matcher
    */
   public AllElements(Matcher<T> aMatcher) {
      iMatcher = aMatcher;
   }


   /**
    * Adds the description
    *
    * @param aDescription
    *           the description
    */
   @Override
   public void describeTo( Description aDescription ) {
      aDescription.appendText( "all elements " );
      iMatcher.describeTo( aDescription );
   }


   /**
    * Returns true if all elements match the matcher
    *
    * @param aIterable
    *           the elements
    *
    * @return TRUE if all elements match
    */
   @Override
   public boolean matchesSafely( Iterable<T> aIterable ) {
      for ( T lItem : aIterable ) {
         if ( !iMatcher.matches( lItem ) ) {
            return false;
         }
      }

      return true;
   }
}
