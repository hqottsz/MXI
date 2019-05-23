package com.mxi.am.domain;

import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefShipSegmentStatusKey;


/**
 * Domain entity shipment segment for query testing.
 *
 */
public class ShipmentSegment {

   private LocationKey fromLocation;
   private LocationKey toLocation;
   private RefShipSegmentStatusKey status;


   public ShipmentSegment() {
   }


   public ShipmentSegment setFromLocation( LocationKey fromLocation ) {
      this.fromLocation = fromLocation;
      return this;
   }


   public LocationKey getFromLocation() {
      return fromLocation;
   }


   public ShipmentSegment setToLocation( LocationKey toLocation ) {
      this.toLocation = toLocation;
      return this;
   }


   public LocationKey getToLocation() {
      return toLocation;
   }


   public ShipmentSegment setStatus( RefShipSegmentStatusKey status ) {
      this.status = status;
      return this;
   }


   public RefShipSegmentStatusKey getStatus() {
      return status;
   }
}
