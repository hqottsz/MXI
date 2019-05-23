
/**
 *
 */
package com.mxi.mx.testing.matchers;

import java.util.Collection;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;


/**
 * A matcher that determines if the collection is empty
 */
public class IsEmpty extends TypeSafeMatcher<Collection<?>> {

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
      aDescription.appendText( "empty" );
   }


   /**
    * (non-Javadoc)
    *
    * @param aItem
    *           the collection
    *
    * @return true if empty
    *
    * @see org.hamcrest.TypeSafeMatcher#matchesSafely(java.lang.Object)
    */
   @Override
   public boolean matchesSafely( Collection<?> aItem ) {
      return aItem.isEmpty();
   }
}
