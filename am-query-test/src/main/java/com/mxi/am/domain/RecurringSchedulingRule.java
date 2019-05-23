package com.mxi.am.domain;

import java.math.BigDecimal;

import com.mxi.mx.core.key.DataTypeKey;


/**
 * Recurring scheduling rule domain object
 *
 */
public class RecurringSchedulingRule extends SchedulingRule {

   private BigDecimal iInterval;
   private BigDecimal iInitialInterval;


   public RecurringSchedulingRule() {
   }


   public RecurringSchedulingRule(DataTypeKey aUsageParameter, BigDecimal aInterval) {
      iUsageParameter = aUsageParameter;
      iInterval = aInterval;
   }


   public BigDecimal getInterval() {
      return iInterval;
   }


   public void setInterval( BigDecimal aInterval ) {
      iInterval = aInterval;
   }


   public void setInterval( int aInterval ) {
      iInterval = new BigDecimal( aInterval );

   }


   public BigDecimal getInitialInterval() {
      return iInitialInterval;
   }


   public void setInitialInterval( BigDecimal aInitialInterval ) {
      iInitialInterval = aInitialInterval;
   }
}
