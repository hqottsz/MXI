package com.mxi.am.domain.builder;

import com.mxi.am.domain.ShipmentSegment;
import com.mxi.mx.core.key.ShipSegmentKey;
import com.mxi.mx.core.table.ship.ShipSegmentTable;


public class ShipmentSegmentBuilder {

   public static ShipSegmentKey build( ShipmentSegment segment ) {
      ShipSegmentTable segmentRow = ShipSegmentTable.create();
      segmentRow.setShipFrom( segment.getFromLocation() );
      segmentRow.setShipTo( segment.getToLocation() );
      segmentRow.setStatus( segment.getStatus() );
      return segmentRow.insert();
   }

}
