
package com.mxi.am.domain.builder;

import java.math.BigDecimal;

import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefEventTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefRelationTypeKey;
import com.mxi.mx.core.key.RefXferTypeKey;
import com.mxi.mx.core.key.TransferKey;
import com.mxi.mx.core.table.evt.EvtLocTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.inv.InvXferTable;


/**
 * Builds an <code>inv_xfer</code> object
 */
public class InventoryTransferBuilder implements DomainBuilder<TransferKey> {

   private BigDecimal iTransferQty;
   private RefXferTypeKey iTransferType;
   private EventKey iInitEvent;
   private InventoryKey iInventory;
   private InventoryKey iKitInventory;
   private LocationKey iSourceLocation;
   private LocationKey iDestinationLocation;
   private RefEventStatusKey iStatus = RefEventStatusKey.LXPEND;
   private String iTransferId = "transferid";
   private RefEventTypeKey iEventType = RefEventTypeKey.PR;
   private RefQtyUnitKey iQtyUnitKey;
   private boolean iHistorical = false;


   public InventoryTransferBuilder withTransferId( String aTransferId ) {
      iTransferId = aTransferId;

      return this;
   }


   /**
    * Sets the Init event key.
    *
    * @param aInitEvent
    *           the Init event key
    *
    * @return The builder
    */
   public InventoryTransferBuilder withInitEvent( EventKey aInitEvent ) {
      iInitEvent = aInitEvent;

      return this;
   }


   /**
    * Sets the Inventory key.
    *
    * @param aInventoryEvent
    *           the Inventory key
    *
    * @return The builder
    */
   public InventoryTransferBuilder withInventory( InventoryKey aInventory ) {
      iInventory = aInventory;

      return this;
   }


   /**
    * Sets the Kit Inventory key.
    *
    * @param aKitInventory
    *           the Kit Inventory key
    *
    * @return The builder
    */
   public InventoryTransferBuilder withKitInventory( InventoryKey aKitInventory ) {
      iKitInventory = aKitInventory;

      return this;
   }


   public InventoryTransferBuilder withQtyUnit( RefQtyUnitKey aQtyUnitKey ) {
      iQtyUnitKey = aQtyUnitKey;
      return this;
   }


   /**
    * Sets the quantity.
    *
    * @param aTransferQty
    *           the transfer quantity
    *
    * @return The builder
    */
   public InventoryTransferBuilder withQuantity( BigDecimal aTransferQty ) {
      iTransferQty = aTransferQty;

      return this;
   }


   /**
    * Sets the type.
    *
    * @param aTransferType
    *           the transfer type
    *
    * @return The builder
    */
   public InventoryTransferBuilder withType( RefXferTypeKey aTransferType ) {
      iTransferType = aTransferType;

      return this;
   }


   /**
    * Sets the source location for the transfer.
    *
    * @param aSourceLocation
    *           the location key
    *
    * @return The builder
    */
   public InventoryTransferBuilder withSource( LocationKey aSourceLocation ) {
      iSourceLocation = aSourceLocation;

      return this;
   }


   /**
    * Sets the destination location for the transfer.
    *
    * @param aDestinationLocation
    *           the location key
    *
    * @return The builder
    */
   public InventoryTransferBuilder withDestination( LocationKey aDestinationLocation ) {
      iDestinationLocation = aDestinationLocation;

      return this;
   }


   /**
    *
    * Overrides the default status of PROPEN.
    *
    * @param aEventStatus
    *           the status
    * @return the builder
    */
   public InventoryTransferBuilder withStatus( RefEventStatusKey aEventStatus ) {
      iStatus = aEventStatus;

      return this;
   }


   /**
    *
    * Overrides the default event type of PR
    *
    * @param aEventType
    *           the type of event
    * @return the builder
    */
   public InventoryTransferBuilder withEventType( RefEventTypeKey aEventType ) {
      iEventType = aEventType;

      return this;
   }


   /**
    *
    * Overrides the default value of historic bool (false) of the transfer event
    *
    * @param aHistoric
    *           true for historic events, false by default
    * @return the builder
    */
   public InventoryTransferBuilder isHistoric( boolean aHistoric ) {
      iHistorical = aHistoric;

      return this;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public TransferKey build() {

      EventBuilder lEventBuilder = new EventBuilder().withStatus( iStatus ).withType( iEventType )
            .withDescription( iTransferId );

      if ( iHistorical ) {
         lEventBuilder.asHistoric();
      }

      EventKey lEventKey = lEventBuilder.build();

      InvXferTable lInvXferTable =
            InvXferTable.create( new TransferKey( lEventKey.getDbId(), lEventKey.getId() ) );

      // create the row in the inv_xfer
      lInvXferTable.setXferQt( iTransferQty );
      lInvXferTable.setTransferType( iTransferType );
      lInvXferTable.setInitEvent( iInitEvent );
      lInvXferTable.setInventory( iInventory );
      lInvXferTable.setKitInventory( iKitInventory );
      lInvXferTable.setQtyUnit( iQtyUnitKey );

      // create two rows in the evt_loc source and destination
      EvtLocTable lEvtLoc = EvtLocTable.create( lEventKey.getEventKey() );
      lEvtLoc.setLocation( iSourceLocation );
      lEvtLoc.insert();
      lEvtLoc = EvtLocTable.create( lEventKey.getEventKey() );
      lEvtLoc.setLocation( iDestinationLocation );
      lEvtLoc.insert();

      // update the location of the inventory to the destination if create a completed transfer
      if ( RefEventStatusKey.LXCMPLT.equals( iStatus ) && iInventory != null ) {

         InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iInventory );
         lInvInv.setLocation( iDestinationLocation );
         lInvInv.update();
      }

      if ( iInitEvent != null ) {

         // create relationship between the initial event and the transfer
         new EventRelationshipBuilder().fromEvent( iInitEvent ).toEvent( iInitEvent )
               .withType( RefRelationTypeKey.TRNSFER ).build();
      }

      return lInvXferTable.insert();
   }
}
