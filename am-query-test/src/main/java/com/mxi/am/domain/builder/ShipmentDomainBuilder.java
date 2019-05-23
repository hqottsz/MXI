
package com.mxi.am.domain.builder;

import java.util.Date;

import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.EventLocationKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefEventTypeKey;
import com.mxi.mx.core.key.RefShipSegmentStatusKey;
import com.mxi.mx.core.key.RefShipmentTypeKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.table.evt.EvtLocTable;
import com.mxi.mx.core.table.ship.ShipSegmentMapTable;
import com.mxi.mx.core.table.ship.ShipSegmentTable;
import com.mxi.mx.core.table.ship.ShipShipmentTable;


/**
 * This is a data builder for shipments.
 *
 * @author dsewell
 */
public class ShipmentDomainBuilder implements DomainBuilder<ShipmentKey> {

   private LocationKey iFromLocation;

   private PurchaseOrderKey iOrderKey;

   private boolean iHistoric = false;

   private RefEventStatusKey iShipmentStatus = RefEventStatusKey.IXPEND;

   private LocationKey iToLocation;

   private RefShipmentTypeKey iType;

   private Date iActualShipmentDate;


   /**
    * Builds the order.
    *
    * @return The key from the new order.
    */
   @Override
   public ShipmentKey build() {
      EventKey lEventKey =
            new EventBuilder().withType( RefEventTypeKey.IX ).withStatus( iShipmentStatus )
                  .withHistoric( iHistoric ).withActualStartDate( iActualShipmentDate ).build();

      ShipmentKey lShipment = new ShipmentKey( lEventKey );

      if ( iFromLocation != null ) {
         EvtLocTable lFromEvtLoc = EvtLocTable.create( new EventLocationKey( lEventKey, 1 ) );
         lFromEvtLoc.setLocation( iFromLocation );
         lFromEvtLoc.insert();
      }

      if ( iToLocation != null ) {
         EvtLocTable lToEvtLoc = EvtLocTable.create( new EventLocationKey( lEventKey, 2 ) );
         lToEvtLoc.setLocation( iToLocation );
         lToEvtLoc.insert();
      }

      ShipSegmentTable lShipSegment = ShipSegmentTable.create();
      lShipSegment.setShipTo( iToLocation );
      lShipSegment.setShipFrom( iFromLocation );
      // set the segment status based on the shipment status
      if ( RefEventStatusKey.IXCMPLT.equals( iShipmentStatus ) ) {
         lShipSegment.setStatus( RefShipSegmentStatusKey.COMPLETE );
      } else {
         lShipSegment.setStatus( RefShipSegmentStatusKey.PENDING );
      }
      lShipSegment.insert();

      // Create an entry in the ship_segment_map
      ShipSegmentMapTable lSegmentMapTable =
            ShipSegmentMapTable.create( lShipment, lShipSegment.getPk() );
      lSegmentMapTable.setSegmentOrd( 1 );
      lSegmentMapTable.insert();

      ShipShipmentTable lShipShipment = ShipShipmentTable.create( lShipment );

      lShipShipment.setShipmentType( iType );

      if ( iOrderKey != null ) {
         lShipShipment.setPO( iOrderKey );
      }

      return lShipShipment.insert();
   }


   /**
    * Sets the historic boolean
    *
    * @return the builder
    */
   public ShipmentDomainBuilder withHistoric( boolean aHistoric ) {
      iHistoric = aHistoric;

      return this;
   }


   /**
    * Sets the from location of the shipment.
    *
    * @param aLocation
    *           The from location
    *
    * @return The builder
    */
   public ShipmentDomainBuilder fromLocation( LocationKey aLocation ) {
      iFromLocation = aLocation;

      return this;
   }


   /**
    * Sets the destination location
    *
    * @param aLocation
    *           The to location
    *
    * @return Thge builder
    */
   public ShipmentDomainBuilder toLocation( LocationKey aLocation ) {
      iToLocation = aLocation;

      return this;
   }


   /**
    * Sets the order key
    *
    * @param aOrderKey
    *           The order key
    *
    * @return The builder
    */
   public ShipmentDomainBuilder withOrder( PurchaseOrderKey aOrderKey ) {
      iOrderKey = aOrderKey;

      return this;
   }


   /**
    * Sets the shipment status.
    *
    * @param aOrderStatus
    *           The shipment status
    *
    * @return The builder
    */
   public ShipmentDomainBuilder withStatus( RefEventStatusKey aOrderStatus ) {
      iShipmentStatus = aOrderStatus;

      return this;
   }


   /**
    * Sets the shipment type
    *
    * @param aType
    *           The type
    *
    * @return The builder
    */
   public ShipmentDomainBuilder withType( RefShipmentTypeKey aType ) {
      iType = aType;

      return this;
   }


   public ShipmentDomainBuilder withActualShipmentDate( Date aActualShipmentDate ) {

      iActualShipmentDate = aActualShipmentDate;
      return this;
   }
}
