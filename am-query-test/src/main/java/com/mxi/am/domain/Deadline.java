package com.mxi.am.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.RefSchedFromKey;
import com.mxi.mx.core.key.TaskKey;


public class Deadline {

   private TaskKey iTaskKey;
   private DataTypeKey iUsageType;
   private RefSchedFromKey iScheduledFrom;
   private BigDecimal iStartTsn;
   private BigDecimal iInterval;
   private BigDecimal iDueValue;
   private Date iDueDate;
   private Date iStartDate;
   private BigDecimal iDeviation;
   private BigDecimal iUsageRemaining;
   private BigDecimal iScheduleToPlanLow;
   private BigDecimal iScheduleToPlanHigh;
   private Boolean iDriving;


   public TaskKey getTask() {
      return iTaskKey;
   }


   public void setTask( TaskKey aTask ) {
      iTaskKey = aTask;
   }


   public Optional<DataTypeKey> getUsageType() {
      return Optional.ofNullable( iUsageType );
   }


   public void setUsageType( DataTypeKey aUsageType ) {
      iUsageType = aUsageType;
   }


   public RefSchedFromKey getScheduledFrom() {
      return iScheduledFrom;
   }


   public void setScheduledFrom( RefSchedFromKey aScheduledFrom ) {
      iScheduledFrom = aScheduledFrom;
   }


   public BigDecimal getStartTsn() {
      return iStartTsn;
   }


   public void setStartTsn( BigDecimal aStartTsn ) {
      iStartTsn = aStartTsn;
   }


   public void setStartTsn( int aStartTsn ) {
      iStartTsn = new BigDecimal( aStartTsn );
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


   public BigDecimal getDueValue() {
      return iDueValue;
   }


   public void setDueValue( BigDecimal aDueValue ) {
      iDueValue = aDueValue;
   }


   public void setDueValue( int aDueValue ) {
      iDueValue = new BigDecimal( aDueValue );
   }


   public Date getDueDate() {
      return iDueDate;
   }


   public void setDueDate( Date aDueDate ) {
      iDueDate = aDueDate;
   }


   public Optional<BigDecimal> getDeviation() {
      return Optional.ofNullable( iDeviation );
   }


   public void setDeviation( BigDecimal aDeviation ) {
      iDeviation = aDeviation;
   }


   public void setDeviation( int aDeviation ) {
      iDeviation = new BigDecimal( aDeviation );
   }


   public BigDecimal getUsageRemaining() {
      return iUsageRemaining;
   }


   public void setUsageRemaining( BigDecimal aUsageRemaining ) {
      iUsageRemaining = aUsageRemaining;
   }


   public void setUsageRemaining( int aUsageRemaining ) {
      iUsageRemaining = new BigDecimal( aUsageRemaining );
   }


   public void setScheduleToPlanWindow( BigDecimal aScheduleToPlanLow,
         BigDecimal aScheduleToPlanHigh ) {
      iScheduleToPlanLow = aScheduleToPlanLow;
      iScheduleToPlanHigh = aScheduleToPlanHigh;
   }


   public BigDecimal getScheduleToPlanLow() {
      return iScheduleToPlanLow;
   }


   public BigDecimal getScheduleToPlanHigh() {
      return iScheduleToPlanHigh;
   }


   public Optional<Date> getStartDate() {
      return Optional.ofNullable( iStartDate );
   }


   public void setStartDate( Date aStartDate ) {
      iStartDate = aStartDate;
   }


   public Optional<Boolean> isDriving() {
      return Optional.ofNullable( iDriving );
   }


   public void setDriving( boolean aFlag ) {
      iDriving = aFlag;
   }

}
