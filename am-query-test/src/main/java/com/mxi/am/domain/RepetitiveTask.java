package com.mxi.am.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefRelationTypeKey;
import com.mxi.mx.core.key.TaskKey;


public class RepetitiveTask {

   private InventoryKey iInventory;
   private RefEventStatusKey iStatus;
   private TaskKey iPreviousTask;
   private FaultKey iRelatedFault;
   private BigDecimal iRepeatInterval;
   private Date iStartDate;
   private RefRelationTypeKey faultRelationType;


   public InventoryKey getInventory() {
      return iInventory;
   }


   public void setInventory( InventoryKey aInventory ) {
      iInventory = aInventory;
   }


   public void setStatus( RefEventStatusKey aStatus ) {
      iStatus = aStatus;
   }


   public RefEventStatusKey getStatus() {
      return iStatus;
   }


   public void setPreviousTask( TaskKey aPreviousTask ) {
      iPreviousTask = aPreviousTask;
   }


   public TaskKey getPreviousTask() {
      return iPreviousTask;
   }


   public void setRelatedFault( FaultKey aFault ) {
      iRelatedFault = aFault;
   }


   public FaultKey getRelatedFault() {
      return iRelatedFault;
   }


   public Optional<BigDecimal> getRepeatInterval() {
      return Optional.ofNullable( iRepeatInterval );
   }


   public void setRepeatInterval( BigDecimal aRepeatInterval ) {
      iRepeatInterval = aRepeatInterval;
   }


   public Optional<Date> getStartDate() {
      return Optional.ofNullable( iStartDate );
   }


   public void setStartDate( Date aStartDate ) {
      iStartDate = aStartDate;
   }


   public RefRelationTypeKey getFaultRelationType() {
      return faultRelationType;
   }


   public void setFaultRelationType( RefRelationTypeKey faultRelationType ) {
      this.faultRelationType = faultRelationType;
   }

}
