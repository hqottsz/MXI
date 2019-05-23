package com.mxi.am.domain.builder;

import com.mxi.am.domain.ShipmentSegmentMap;
import com.mxi.mx.core.key.ShipSegmentMapKey;
import com.mxi.mx.core.table.ship.ShipSegmentMapTable;


/**
 *
 * Domain builder for shipment segment map entities. Performs insert on SHIP_SEGMENT_MAP
 *
 */

public class ShipSegmentMapDomainBuilder {

   /**
    * @param aShipSegmentMap
    *           domain entity for insertion
    * @return The ship segment map key
    */
   public static ShipSegmentMapKey build( ShipmentSegmentMap aShipSegmentMap ) {
      ShipSegmentMapTable lSegmentMapTable = ShipSegmentMapTable
            .create( aShipSegmentMap.getShipmentKey(), aShipSegmentMap.getSegmentKey() );
      lSegmentMapTable.setSegmentOrd( aShipSegmentMap.getOrder() );

      return lSegmentMapTable.insert();
   }

}
