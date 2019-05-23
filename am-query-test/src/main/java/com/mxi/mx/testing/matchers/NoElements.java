
/**
 *
 */
package com.mxi.mx.testing.matchers;

import static com.mxi.mx.testing.matchers.MxMatchers.anyElements;
import static org.hamcrest.Matchers.not;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;


/**
 * This class is a matcher that checks that no elements matches the element matcher
 */
public class NoElements<T> extends TypeSafeMatcher<Iterable<T>> {

   private final Matcher<T> iMatcher;


   /**
    * Creates a new {@linkplain NoElements} object.
    *
    * @param aMatcher
    *           the element matcher
    */
   public NoElements(Matcher<T> aMatcher) {
      iMatcher = aMatcher;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void describeTo( Description aDescription ) {

      aDescription.appendText( "no elements " );
      iMatcher.describeTo( aDescription );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public boolean matchesSafely( Iterable<T> aCollection ) {
      return not( anyElements( iMatcher ) ).matches( aCollection );
   }
}
