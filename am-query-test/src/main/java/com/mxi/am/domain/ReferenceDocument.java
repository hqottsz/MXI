package com.mxi.am.domain;

import java.util.Date;

import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskTaskKey;


public class ReferenceDocument {

   private RefTaskClassKey iTaskClass;
   private InventoryKey iInventory;
   private TaskTaskKey iDefinition;
   private RefEventStatusKey iStatus;
   private Date iActualStartDate;
   private Date iActualEndDate;


   ReferenceDocument() {
   }


   public void setTaskClass( RefTaskClassKey aTaskClass ) {
      iTaskClass = aTaskClass;
   }


   public RefTaskClassKey getTaskClass() {
      return iTaskClass;
   }


   public void setInventory( InventoryKey aMainInventory ) {
      iInventory = aMainInventory;
   }


   public InventoryKey getInventory() {
      return iInventory;
   }


   public void setDefinition( TaskTaskKey aDefinition ) {
      iDefinition = aDefinition;
   }


   public TaskTaskKey getDefinition() {
      return iDefinition;
   }


   public void setStatus( RefEventStatusKey aStatus ) {
      iStatus = aStatus;
   }


   public RefEventStatusKey getStatus() {
      return iStatus;
   }


   public void setActualStartDate( Date aDate ) {
      iActualStartDate = aDate;
   }


   public Date getActualStartDate() {
      return iActualStartDate;
   }


   public void setActualEndDate( Date aDate ) {
      iActualEndDate = aDate;
   }


   public Date getActualEndDate() {
      return iActualEndDate;
   }

}
