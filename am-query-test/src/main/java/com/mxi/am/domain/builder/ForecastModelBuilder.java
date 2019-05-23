
package com.mxi.am.domain.builder;

import com.mxi.am.domain.ForecastModel;
import com.mxi.am.domain.ForecastModel.RangeInfo;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.FcModelKey;
import com.mxi.mx.core.key.FcRateKey;
import com.mxi.mx.core.table.fc.FcModelTable;
import com.mxi.mx.core.table.fc.FcRangeTable;
import com.mxi.mx.core.table.fc.FcRateTable;


/**
 * Builds an <code>fc_model</code> object.
 */
public final class ForecastModelBuilder {

   private static final Integer RANGE_START_MONTH = 1;
   private static final Integer RANGE_START_DAY = 1;
   private static final DataTypeKey DEFAULT_DATA_TYPE = DataTypeKey.LANDING;
   private static final Double DEFAULT_FORECAST_RATE = 1.0;


   /**
    * {@inheritDoc}
    */
   public static FcModelKey build( ForecastModel aForecastModel ) {

      // add a record in fc_model
      FcModelKey lFcModelKey = FcModelTable.generatePrimaryKey();
      FcModelTable lFcModelTable = FcModelTable.create( lFcModelKey );
      lFcModelTable.setSDesc( aForecastModel.getName() );
      lFcModelTable.setAuthority( aForecastModel.getAuthority() );
      lFcModelTable.insert();

      // add the ranges
      if ( aForecastModel.getRanges().isEmpty() ) {
         aForecastModel.addRange( RANGE_START_MONTH, RANGE_START_DAY, DEFAULT_DATA_TYPE,
               DEFAULT_FORECAST_RATE );
      }

      for ( RangeInfo lRange : aForecastModel.getRanges() ) {
         FcRangeTable lFcRangeTable = FcRangeTable.create( lFcModelKey );
         lFcRangeTable.setStartMonth( lRange.getStartMonth() );
         lFcRangeTable.setStartDay( lRange.getStartDay() );
         lFcRangeTable.insert();

         FcRateTable lFcRateTable =
               FcRateTable.create( new FcRateKey( lFcRangeTable.getPk(), lRange.getDataType() ) );
         lFcRateTable.setForecastRateQt( lRange.getRate() );
         lFcRateTable.insert();
      }

      return lFcModelKey;
   }

}
