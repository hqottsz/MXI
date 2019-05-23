package com.mxi.am.domain;

import java.util.UUID;

import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.InventoryDamageKey;
import com.mxi.mx.core.key.InventoryKey;


public class DamageRecord {

   private InventoryDamageKey inventoryDamageKey;
   private String location;
   private InventoryKey inventoryKey;
   private FaultKey faultKey;
   private UUID altId;


   public InventoryDamageKey getInventoryDamageKey() {
      return inventoryDamageKey;
   }


   public void setInventoryDamageKey( InventoryDamageKey inventoryDamageKey ) {
      this.inventoryDamageKey = inventoryDamageKey;
   }


   public String getLocation() {
      return location;
   }


   public void setLocation( String location ) {
      this.location = location;
   }


   public InventoryKey getInventoryKey() {
      return inventoryKey;
   }


   public void setInventoryKey( InventoryKey inventoryKey ) {
      this.inventoryKey = inventoryKey;
   }


   public FaultKey getFaultKey() {
      return faultKey;
   }


   public void setFaultKey( FaultKey faultKey ) {
      this.faultKey = faultKey;
   }


   public UUID getAltId() {
      return altId;
   }


   public void setAltId( UUID altId ) {
      this.altId = altId;
   }

}
