
package com.mxi.mx.testing.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.table.mim.MimDataType;
import com.mxi.mx.core.usage.service.UsageDelta;


/**
 * Matcher for the {@link UsageDelta}'s delta and data type.
 */
public class UsageDeltaOf extends TypeSafeMatcher<UsageDelta> {

   private final DataTypeKey iDataType;

   private final String iDataTypeCode;

   private final Matcher<Double> iDelta;


   /**
    * Creates a new {@linkplain UsageDeltaOf} object.
    *
    * @param aDelta
    *           the delta
    * @param aDataType
    *           the data type
    */
   public UsageDeltaOf(Matcher<Double> aDelta, DataTypeKey aDataType) {
      iDelta = aDelta;
      iDataType = aDataType;

      String lDataTypeCode = aDataType.toString();
      try {
         lDataTypeCode = getDataTypeCode( iDataType );
      } catch ( Exception e ) {
         // ignore
      }

      iDataTypeCode = lDataTypeCode;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void describeTo( Description aDescription ) {

      aDescription.appendText( "usage delta of " );
      aDescription.appendValue( iDelta );
      aDescription.appendText( " " );
      aDescription.appendValue( iDataTypeCode );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public boolean matchesSafely( UsageDelta aItem ) {
      if ( iDelta.matches( aItem.getDelta() )
            && iDataType.equals( aItem.getUsageParmKey().getDataType() ) ) {
         return true;
      }

      return false;
   }


   private String getDataTypeCode( DataTypeKey aDataType ) {
      return MimDataType.findByPrimaryKey( aDataType ).getDataTypeCd();
   }
}
