package com.mxi.am.domain.builder;

import static com.mxi.mx.core.key.RefEventStatusKey.IXCANCEL;
import static com.mxi.mx.core.key.RefEventStatusKey.IXCMPLT;
import static com.mxi.mx.core.key.RefEventStatusKey.IXPEND;
import static com.mxi.mx.core.key.RefEventTypeKey.IX;

import java.util.Optional;

import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Shipment;
import com.mxi.am.domain.ShipmentLine;
import com.mxi.am.domain.ShipmentSegment;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.EventLocationKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefShipmentTypeKey;
import com.mxi.mx.core.key.ShipSegmentKey;
import com.mxi.mx.core.key.ShipSegmentMapKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.EvtLocTable;
import com.mxi.mx.core.table.ship.ShipSegmentMapTable;
import com.mxi.mx.core.table.ship.ShipShipmentTable;


public class ShipmentBuilder {

   static EvtEventDao evtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );


   public static ShipmentKey build( Shipment shipment ) {

      RefEventStatusKey status = shipment.getStatus().orElse( IXPEND );

      EventKey eventKey = evtEventDao.generatePrimaryKey();
      EvtEventTable evtEventRow = evtEventDao.create( eventKey );
      evtEventRow.setEventStatus( status );
      evtEventRow.setEventType( IX );
      evtEventRow.setActualStartDate( shipment.getShipmentDate() );
      evtEventRow.setHistBool( isHistorical( shipment.getHistorical(), status ) );
      evtEventDao.insert( evtEventRow );

      ShipmentKey shipmentKey = new ShipmentKey( eventKey );
      ShipShipmentTable shipmentRow = ShipShipmentTable.create( shipmentKey );
      shipmentRow.setShipmentType( shipment.getType().orElse( RefShipmentTypeKey.STKTRN ) );
      shipmentRow.setShipByDate( shipment.getShipByDate() );
      shipmentRow.setPO( shipment.getPurchaseOrder() );
      shipmentRow.setWaybillSdesc( shipment.getWaybillNumber() );
      shipmentRow.insert();

      // Build shipment segments
      int segmentNumber = 1;
      for ( DomainConfiguration<ShipmentSegment> segmentConfig : shipment
            .getSegmentConfigurations() ) {
         ShipmentSegment segment = new ShipmentSegment();
         segmentConfig.configure( segment );
         ShipSegmentKey segmentKey = ShipmentSegmentBuilder.build( segment );

         ShipSegmentMapKey segmentMapKey = new ShipSegmentMapKey( shipmentKey, segmentKey );
         ShipSegmentMapTable segmentMapRow = ShipSegmentMapTable.create( segmentMapKey );
         segmentMapRow.setSegmentOrd( segmentNumber );
         segmentMapRow.insert();

         if ( segmentNumber == 1 ) {
            buildShipmentEventLocations( eventKey, segment );
         }
         segmentNumber++;
      }

      // Build shipment lines
      for ( DomainConfiguration<ShipmentLine> lineConfig : shipment.getLineConfigurations() ) {
         ShipmentLine line = new ShipmentLine();
         lineConfig.configure( line );
         line.shipmentKey( shipmentKey );
         ShipmentLineDomainBuilder.build( line );
      }

      return shipmentKey;
   }


   private static void buildShipmentEventLocations( EventKey eventKey, ShipmentSegment segment ) {
      EvtLocTable fromEvtLocRow = EvtLocTable.create( new EventLocationKey( eventKey, 1 ) );
      fromEvtLocRow.setLocation( segment.getFromLocation() );
      fromEvtLocRow.insert();

      EvtLocTable toEvtLocRow = EvtLocTable.create( new EventLocationKey( eventKey, 2 ) );
      toEvtLocRow.setLocation( segment.getToLocation() );
      toEvtLocRow.insert();
   }


   private static boolean isHistorical( Optional<Boolean> isHistorical, RefEventStatusKey status ) {
      // use historical flag if present otherwise use helper logic to determine if historical
      // (in java 9 there will be a Optional.ifPresentOrElse, but until then...)
      if ( isHistorical.isPresent() ) {
         return isHistorical.get();
      } else {
         return IXCANCEL.equals( status ) || IXCMPLT.equals( status );
      }
   }

}
