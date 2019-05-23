package com.mxi.am.domain;

import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.table.po.POLineTable;
import com.mxi.mx.core.table.ship.ShipShipmentTable;


/**
 * Domain entity for shipment line used for query testing.
 *
 */
public class ShipmentLine {

   private double iExpectedQty;

   private Double iReceivedQty;

   private PurchaseOrderLineKey iOrderLine;

   private PartNoKey iPart;

   private InventoryKey iInventory;

   private String iSerialNo;

   private ShipmentKey iShipmentKey;


   public ShipmentLine orderLine( PurchaseOrderLineKey aOrderLine ) {
      iOrderLine = aOrderLine;

      PartNoKey lPart = POLineTable.findByPrimaryKey( aOrderLine ).getPartNo();
      if ( lPart != null ) {
         iPart = lPart;
      }

      return this;
   }


   public PurchaseOrderLineKey getOrderLine() {
      return iOrderLine;
   }


   public ShipmentLine part( PartNoKey aPart ) {
      iPart = aPart;
      return this;
   }


   public PartNoKey getPart() {
      return iPart;
   }


   public ShipmentLine inventory( InventoryKey aInventory ) {
      iInventory = aInventory;
      return this;
   }


   public InventoryKey getInventory() {
      return iInventory;
   }


   public ShipmentLine expectedQuantity( double aQuantity ) {
      iExpectedQty = aQuantity;
      return this;
   }


   public double getExpectedQuantity() {
      return iExpectedQty;
   }


   public ShipmentLine serialNo( String aSerialNo ) {
      iSerialNo = aSerialNo;
      return this;
   }


   public String getSerialNo() {
      return iSerialNo;
   }


   public ShipmentLine receivedQuantity( Double aReceiveQty ) {
      iReceivedQty = aReceiveQty;
      return this;
   }


   public Double getReceivedQuantity() {
      return iReceivedQty;
   }


   public ShipmentLine shipmentKey( ShipmentKey aShipmentKey ) {

      ShipmentKey lShipmentKey = ShipShipmentTable.findByPrimaryKey( aShipmentKey ).getPk();
      if ( lShipmentKey != null ) {
         iShipmentKey = lShipmentKey;
      }
      return this;
   }


   public ShipmentKey getShipmentKey() {
      return iShipmentKey;
   }

}
