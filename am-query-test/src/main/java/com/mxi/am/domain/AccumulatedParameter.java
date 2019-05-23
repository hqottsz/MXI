package com.mxi.am.domain;

import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.UsageDefinitionKey;


/**
 * Domain class for accumulated parameter
 *
 */
public class AccumulatedParameter {

   private DataTypeKey iAggregatedDataType;
   private PartNoKey iPartNoKey;
   private UsageDefinitionKey iUsageDefinitionKey;
   private String iCode;


   public DataTypeKey getAggregatedDataType() {
      return iAggregatedDataType;
   }


   public void setAggregatedDataType( DataTypeKey aAggregatedDataType ) {
      iAggregatedDataType = aAggregatedDataType;
   }


   public UsageDefinitionKey getUsageDefinition() {
      return iUsageDefinitionKey;
   }


   public void setUsageDefinition( UsageDefinitionKey aUsageDefinitionKey ) {
      iUsageDefinitionKey = aUsageDefinitionKey;
   }


   public PartNoKey getPartNoKey() {
      return iPartNoKey;
   }


   public void setPartNoKey( PartNoKey aPartNoKey ) {
      iPartNoKey = aPartNoKey;
   }


   public String getCode() {
      return iCode;
   }


   public void setCode( String aCode ) {
      iCode = aCode;
   }

}
