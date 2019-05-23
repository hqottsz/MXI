package com.mxi.am.domain;

import com.mxi.mx.core.key.ShipSegmentKey;
import com.mxi.mx.core.key.ShipmentKey;


/**
 * Domain entity shipment segment map for query testing.
 *
 */
public class ShipmentSegmentMap {

   private int iOrder;
   private ShipmentKey iShipmentKey;
   private ShipSegmentKey iShipSegmentKey;


   public ShipmentSegmentMap() {
   }


   public ShipmentSegmentMap order( int aOrder ) {
      iOrder = aOrder;
      return this;
   }


   public int getOrder() {
      return iOrder;
   }


   public ShipmentSegmentMap shipmentKey( ShipmentKey aShipmentKey ) {
      iShipmentKey = aShipmentKey;
      return this;
   }


   public ShipmentKey getShipmentKey() {
      return iShipmentKey;
   }


   public ShipmentSegmentMap segmentKey( ShipSegmentKey aShipSegmentKey ) {
      iShipSegmentKey = aShipSegmentKey;
      return this;
   }


   public ShipSegmentKey getSegmentKey() {
      return iShipSegmentKey;
   }

}
