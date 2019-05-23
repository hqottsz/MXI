
package com.mxi.mx.testing.matchers;

import java.util.Collection;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;


/**
 * A matcher that determines if the collection contains exactly the same items as those that were
 * provided.
 */
public class ListContainingOnly<T> extends TypeSafeMatcher<List<T>> {

   private Collection<Matcher<? super T>> iMatchers;


   /**
    * Creates a new {@link ListContainingOnly} object.
    *
    * @param aMatchers
    *           The matchers to compare against the array.
    */
   public ListContainingOnly(Collection<Matcher<? super T>> aMatchers) {
      iMatchers = aMatchers;
   }


   /**
    * Returns a matcher that determines if the given matchers match all the items in a list.
    *
    * @param aMatchers
    *           The matchers
    *
    * @return The matcher
    */
   public static <T> Matcher<List<T>> listContainingOnly( List<Matcher<? super T>> aMatchers ) {
      return new ListContainingOnly<T>( aMatchers );
   }


   /**
    * (non-Javadoc)
    *
    * @param aDescription
    *           the description
    *
    * @see org.hamcrest.SelfDescribing#describeTo(org.hamcrest.Description)
    */
   @Override
   public void describeTo( Description aDescription ) {
      aDescription.appendList( "[", ", ", "]", iMatchers ).appendText( " in the list" );
   }


   /**
    * The size of both collections must match and all of the elements must match as well.
    *
    * @param aItem
    *           the collection
    *
    * @return true if empty
    *
    * @see org.hamcrest.TypeSafeMatcher#matchesSafely(java.lang.Object)
    */
   @Override
   public boolean matchesSafely( List<T> aItems ) {
      if ( aItems.size() != iMatchers.size() ) {
         return false;
      }

      for ( Matcher<? super T> lMatcher : iMatchers ) {
         if ( !Matchers.hasItem( lMatcher ).matches( aItems ) ) {
            return false;
         }
      }

      return true;
   }
}
