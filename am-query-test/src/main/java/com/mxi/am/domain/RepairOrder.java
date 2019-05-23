
package com.mxi.am.domain;

import java.util.Date;

import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefEventStatusKey;


public final class RepairOrder {

   private InventoryKey iMainInventory = null;
   private RefEventStatusKey iStatus;
   private LocationKey iLocationKey;
   private Date iActualStartDate;
   private Date iActualEndDate;


   public InventoryKey getMainInventory() {
      return iMainInventory;
   }


   public void setMainInventory( InventoryKey aMainInventory ) {
      iMainInventory = aMainInventory;
   }


   public RefEventStatusKey getStatus() {
      return iStatus;
   }


   public void setStatus( RefEventStatusKey aStatus ) {
      iStatus = aStatus;
   }


   public LocationKey getLocationKey() {
      return iLocationKey;
   }


   public void setLocationKey( LocationKey aLocationKey ) {
      iLocationKey = aLocationKey;
   }


   public Date getActualEndDate() {
      return iActualEndDate;
   }


   public void setActualEndDate( Date aActualEndDate ) {
      iActualEndDate = aActualEndDate;
   }


   public Date getActualStartDate() {
      return iActualStartDate;
   }


   public void setActualStartDate( Date aActualStartDate ) {
      iActualStartDate = aActualStartDate;
   }

}
