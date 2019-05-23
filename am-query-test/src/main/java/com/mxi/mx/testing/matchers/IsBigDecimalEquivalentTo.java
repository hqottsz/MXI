
package com.mxi.mx.testing.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;


/**
 * Matches comparables based on the {@link Comparable} function. In the case of {@link BigDecimal},
 * equalTo will return false when the scale is different (1.0 != 1.00). Since the {@link BigDecimal}
 * scale is lost when committing to the database, we need to validate using the compareTo( aObject )
 * == 0.
 */
public class IsBigDecimalEquivalentTo<T extends Comparable<T>> extends BaseMatcher<T> {

   private final java.math.BigDecimal iValueToMatch;


   /**
    * Creates a new {@linkplain IsBigDecimalEquivalentTo} object.
    *
    * @param aValueToMatch
    *           the value to match against
    */
   public IsBigDecimalEquivalentTo(java.math.BigDecimal aValueToMatch) {
      iValueToMatch = aValueToMatch;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void describeTo( Description aDescription ) {
      aDescription.appendValue( iValueToMatch );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   @SuppressWarnings( "unchecked" )
   public boolean matches( Object aItem ) {
      if ( !iValueToMatch.getClass().isInstance( aItem ) ) {
         return false;
      }

      return ( iValueToMatch.subtract( ( java.math.BigDecimal ) aItem ).abs()
            .compareTo( new java.math.BigDecimal( "0.0003" ) ) <= 0 );
   }
}
