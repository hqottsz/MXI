
package com.mxi.mx.testing.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import com.mxi.mx.common.dataset.DataSetArgument;


/**
 * This class allows us to match {@link DataSetArgument} argument values
 */
public class DataSetArgumentMatcher extends TypeSafeMatcher<DataSetArgument> {

   private final String iArgument;
   private final Matcher<?> iArgumentMatcher;


   /**
    * Creates a new {@linkplain DataSetArgumentMatcher} object.
    *
    * @param aArgument
    *           the argument name
    * @param aMatcher
    *           the value matcher
    */
   public DataSetArgumentMatcher(String aArgument, Matcher<?> aMatcher) {
      iArgument = aArgument;
      iArgumentMatcher = aMatcher;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void describeTo( Description aDescription ) {
      aDescription.appendText( "argument " );
      aDescription.appendValue( iArgument );
      aDescription.appendText( " " );
      aDescription.appendDescriptionOf( iArgumentMatcher );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public boolean matchesSafely( DataSetArgument aDataSetArgument ) {
      Object lArgmentValue = aDataSetArgument.get( iArgument );

      return iArgumentMatcher.matches( lArgmentValue );
   }
}
