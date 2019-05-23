package com.mxi.am.domain;

import java.math.BigDecimal;

import com.mxi.mx.core.key.DataTypeKey;


/**
 * Non recurring scheduling rule domain object
 *
 */
public class OneTimeSchedulingRule extends SchedulingRule {

   private BigDecimal iThreshold;


   public OneTimeSchedulingRule() {
   }


   public OneTimeSchedulingRule(DataTypeKey aUsageParameter, BigDecimal aThreshold) {
      iUsageParameter = aUsageParameter;
      iThreshold = aThreshold;
   }


   public BigDecimal getThreshold() {
      return iThreshold;
   }


   public void setThreshold( BigDecimal aThreshold ) {
      iThreshold = aThreshold;
   }


   public void setThreshold( int aThreshold ) {
      iThreshold = new BigDecimal( aThreshold );
   }

}
