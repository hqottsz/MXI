
package com.mxi.am.domain.builder;

import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefShipSegmentStatusKey;
import com.mxi.mx.core.key.ShipSegmentKey;
import com.mxi.mx.core.table.ship.ShipSegmentTable;


/**
 * This is a data builder that builds ship segments
 */
public class ShipSegmentBuilder implements DomainBuilder<ShipSegmentKey> {

   private LocationKey iShipFrom;
   private LocationKey iShipTo;

   private RefShipSegmentStatusKey iStatus;


   /**
    * Builds the order line.
    *
    * @return The order line key
    */
   @Override
   public ShipSegmentKey build() {
      ShipSegmentTable lSegment = ShipSegmentTable.create();

      lSegment.setShipFrom( iShipFrom );
      lSegment.setShipTo( iShipTo );
      lSegment.setStatus( iStatus );

      return lSegment.insert();
   }


   /**
    * Sets the ship from location of the segment
    *
    * @param aShipFrom
    *           The ship from location
    *
    * @return The builder
    */
   public ShipSegmentBuilder fromLocation( LocationKey aShipFrom ) {
      iShipFrom = aShipFrom;

      return this;
   }


   /**
    * Sets the ship to location of the segment
    *
    * @param aShipTo
    *           The ship to location
    *
    * @return The builder
    */
   public ShipSegmentBuilder toLocation( LocationKey aShipTo ) {
      iShipTo = aShipTo;

      return this;
   }


   /**
    * Sets the status of the segment
    *
    * @param aStatus
    *           The status
    *
    * @return The builder
    */
   public ShipSegmentBuilder withStatus( RefShipSegmentStatusKey aStatus ) {
      iStatus = aStatus;

      return this;
   }
}
