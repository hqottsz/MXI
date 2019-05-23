
package com.mxi.am.domain.builder;

import com.mxi.mx.core.key.ShipSegmentKey;
import com.mxi.mx.core.key.ShipSegmentMapKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.table.ship.ShipSegmentMapTable;


/**
 * This is a data builder that builds a map of ship segment and shipment
 */
public class ShipSegmentMapBuilder implements DomainBuilder<ShipSegmentMapKey> {

   private int iOrder;
   private ShipmentKey iShipment;
   private ShipSegmentKey iShipSegment;


   /**
    * Builds the ship segment map
    *
    * @return The ship segment map key
    */
   @Override
   public ShipSegmentMapKey build() {
      ShipSegmentMapTable lSegmentMapTable = ShipSegmentMapTable.create( iShipment, iShipSegment );
      lSegmentMapTable.setSegmentOrd( iOrder );

      return lSegmentMapTable.insert();
   }


   /**
    * Sets the segment order
    *
    * @param aOrder
    *           segment order
    *
    * @return The builder
    */
   public ShipSegmentMapBuilder withOrder( int aOrder ) {
      iOrder = aOrder;

      return this;
   }


   /**
    * Sets the ship segment
    *
    * @param aShipSegment
    *           ship segment
    *
    * @return The builder
    */
   public ShipSegmentMapBuilder withSegment( ShipSegmentKey aShipSegment ) {
      iShipSegment = aShipSegment;

      return this;
   }


   /**
    * Sets the shipment
    *
    * @param aShipment
    *           shipment
    *
    * @return The builder
    */
   public ShipSegmentMapBuilder withShipment( ShipmentKey aShipment ) {
      iShipment = aShipment;

      return this;
   }
}
