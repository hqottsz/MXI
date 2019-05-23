package com.mxi.am.domain;

import java.math.BigDecimal;

import com.mxi.mx.core.key.DataTypeKey;


/**
 * Abstract class for scheduling rules
 *
 */
public abstract class SchedulingRule {

   protected DataTypeKey iUsageParameter;
   protected BigDecimal iDeviation;
   protected BigDecimal iNotification;
   protected BigDecimal iSchedToPlanHigh;
   protected BigDecimal iSchedToPlanLow;


   public DataTypeKey getUsageParameter() {
      return iUsageParameter;
   }


   public void setUsageParameter( DataTypeKey aUsageParameter ) {
      iUsageParameter = aUsageParameter;
   }


   public BigDecimal getDeviation() {
      return iDeviation;
   }


   public void setDeviation( BigDecimal aDeviation ) {
      iDeviation = aDeviation;
   }


   public BigDecimal getNotification() {
      return iNotification;
   }


   public void setNotification( BigDecimal aNotification ) {
      iNotification = aNotification;
   }


   public BigDecimal getSchedToPlanHigh() {
      return iSchedToPlanHigh;
   }


   public void setSchedToPlanHigh( BigDecimal aSchedToPlanHigh ) {
      iSchedToPlanHigh = aSchedToPlanHigh;
   }


   public BigDecimal getSchedToPlanLow() {
      return iSchedToPlanLow;
   }


   public void setSchedToPlanLow( BigDecimal aSchedToPlanLow ) {
      iSchedToPlanLow = aSchedToPlanLow;
   }
}
