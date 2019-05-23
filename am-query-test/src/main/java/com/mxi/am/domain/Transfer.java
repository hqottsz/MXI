package com.mxi.am.domain;

import java.math.BigDecimal;

import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefXferTypeKey;


public class Transfer {

   private InventoryKey inventory;
   private LocationKey fromLocation;
   private LocationKey toLocation;
   private RefXferTypeKey type;
   private RefEventStatusKey status;
   private BigDecimal quantity;


   public InventoryKey getInventory() {
      return inventory;
   }


   public void setInventory( InventoryKey inventory ) {
      this.inventory = inventory;
   }


   public LocationKey getFromLocation() {
      return fromLocation;
   }


   public void setFromLocation( LocationKey fromLocation ) {
      this.fromLocation = fromLocation;
   }


   public LocationKey getToLocation() {
      return toLocation;
   }


   public void setToLocation( LocationKey toLocation ) {
      this.toLocation = toLocation;
   }


   public RefXferTypeKey getType() {
      return type;
   }


   public void setType( RefXferTypeKey type ) {
      this.type = type;
   }


   public RefEventStatusKey getStatus() {
      return status;
   }


   public void setStatus( RefEventStatusKey status ) {
      this.status = status;
   }


   public BigDecimal getQuantity() {
      return quantity;
   }


   public void setQuantity( BigDecimal quantity ) {
      this.quantity = quantity;
   }


   public void setQuantity( int quantity ) {
      this.quantity = BigDecimal.valueOf( quantity );
   }

}
