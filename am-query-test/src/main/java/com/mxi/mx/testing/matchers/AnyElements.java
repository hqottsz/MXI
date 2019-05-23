
package com.mxi.mx.testing.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;


/**
 * This class iterates over a list of values to validate that at least one element matches.
 */
public class AnyElements<T> extends TypeSafeMatcher<Iterable<T>> {

   private final Matcher<T> iMatcher;


   /**
    * Creates a new {@linkplain AnyElements} object.
    *
    * @param aMatcher
    *           the matcher
    */
   public AnyElements(Matcher<T> aMatcher) {
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
      aDescription.appendText( "at least one element " );
      iMatcher.describeTo( aDescription );
   }


   /**
    * Returns true if any elements match the matcher
    *
    * @param aIterable
    *           the elements
    *
    * @return TRUE if there's at least one match
    */
   @Override
   public boolean matchesSafely( Iterable<T> aIterable ) {
      for ( T lItem : aIterable ) {
         if ( iMatcher.matches( lItem ) ) {
            return true;
         }
      }

      return false;
   }
}
