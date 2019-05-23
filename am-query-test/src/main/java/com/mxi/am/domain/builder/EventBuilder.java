
package com.mxi.am.domain.builder;

import java.util.Date;

import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefEventTypeKey;
import com.mxi.mx.core.key.RefSchedPriorityKey;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.EvtInvTable;
import com.mxi.mx.core.table.evt.EvtLocTable;
import com.mxi.mx.core.table.inv.InvInvTable;


/**
 * Builds an <code>evt_event</code> object
 *
 *
 * @deprecated This is not a domain type; it's a technical implementation detail and shouldn't have
 *             a builder!
 */
@Deprecated
public class EventBuilder implements DomainBuilder<EventKey> {

   private String iDescription;

   private boolean iHistoric = false;
   private LocationKey iLocation;
   private InventoryKey iMainInventory;
   private EventKey iParentEvent;
   private Date iSchedEndDt;
   private Date iSchedStartDt;
   private Date iActualStartDt;
   private Date iActualEndDt;
   private RefEventStatusKey iStatus = RefEventStatusKey.ACTV;
   private RefEventTypeKey iType;
   private RefSchedPriorityKey iSchedPriority;
   private PartNoKey iPartNoKey;


   /**
    * Sets the event as historic
    *
    * @return the builder
    */
   public EventBuilder asHistoric() {
      iHistoric = true;

      return this;
   }


   /**
    * Sets the historic boolean
    *
    * @return the builder
    */
   public EventBuilder withHistoric( boolean aHistoric ) {
      iHistoric = aHistoric;

      return this;
   }


   /**
    * Sets the location of the event.
    *
    * @param aLocation
    *           The event location.
    *
    * @return The builder.
    */
   public EventBuilder atLocation( LocationKey aLocation ) {
      iLocation = aLocation;

      return this;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public EventKey build() {
      EventKey lPrimaryKey = EvtEventTable.generatePrimaryKey();
      EvtEventTable lTable = EvtEventTable.create( lPrimaryKey );
      lTable.setEventType( iType );
      lTable.setHistBool( iHistoric );
      lTable.setStatus( iStatus );
      lTable.setEventSdesc( iDescription );
      lTable.setNhEvent( iParentEvent );
      lTable.setSchedStartDate( iSchedStartDt );
      lTable.setSchedEndDt( iSchedEndDt );
      lTable.setActualStartDate( iActualStartDt );
      lTable.setEventDate( iActualEndDt );
      lTable.setEventStatus( iStatus );

      lTable.setSchedPriority( iSchedPriority );

      if ( iParentEvent != null ) {
         EvtEventTable lParentEvtEvent = EvtEventTable.findByPrimaryKey( iParentEvent );
         lTable.setHEvent( lParentEvtEvent.getHEvent() );
      } else {
         lTable.setHEvent( lPrimaryKey );
      }

      EventKey lEventKey = lTable.insert();

      if ( iMainInventory != null ) {
         InvInvTable lInv = InvInvTable.findByPrimaryKey( iMainInventory );

         EvtInvTable lEvtInv = EvtInvTable.create( lEventKey );
         lEvtInv.setInventoryKey( iMainInventory );
         lEvtInv.setMainInvBool( true );
         lEvtInv.setHInventoryKey( lInv.getHInvNo() );
         lEvtInv.setAssmblInventoryKey( lInv.getAssmblInvNo() );
         lEvtInv.setNHInventoryKey( lInv.getNhInvNo() );
         lEvtInv.setAssemblyKey( lInv.getOrigAssmbl() );
         lEvtInv.setPartNoKey( iPartNoKey );
         lEvtInv.insert();
      }

      if ( iLocation != null ) {
         EvtLocTable lEvtLoc = EvtLocTable.create( lEventKey );
         lEvtLoc.setLocation( iLocation );
         lEvtLoc.insert();
      }

      return lEventKey;
   }


   /**
    * Sets the main inventory of the event.
    *
    * @param aInventory
    *           The inventory
    *
    * @return The builder
    */
   public EventBuilder onInventory( InventoryKey aInventory ) {
      iMainInventory = aInventory;

      return this;
   }


   /**
    * Sets the event description
    *
    * @param aDescription
    *           The description
    *
    * @return The builder
    */
   public EventBuilder withDescription( String aDescription ) {
      iDescription = aDescription;

      return this;
   }


   /**
    * Sets the parent event
    *
    * @param aParentEvent
    *           The parent event
    *
    * @return The builder
    */
   public EventBuilder withParentEvent( EventKey aParentEvent ) {
      iParentEvent = aParentEvent;

      return this;
   }


   /**
    * Sets the scheduled end date.
    *
    * @param aSchedEndDt
    *           The scheduled end date
    *
    * @return The builder
    */
   public EventBuilder withScheduledEnd( Date aSchedEndDt ) {
      iSchedEndDt = aSchedEndDt;

      return this;
   }


   /**
    * Sets the scheduled start date.
    *
    * @param aSchedStartDt
    *           The scheduled start date
    *
    * @return The builder
    */
   public EventBuilder withScheduledStart( Date aSchedStartDt ) {
      iSchedStartDt = aSchedStartDt;

      return this;
   }


   /**
    * Sets the actual start date.
    *
    * @param aActualStartDate
    *           The actual start date
    *
    * @return The builder
    */
   public EventBuilder withActualStartDate( Date aActualStartDate ) {
      iActualStartDt = aActualStartDate;

      return this;
   }


   /**
    * Set the actual end date.
    *
    * @param anActualEndDate
    *           The actual end date in local format
    */
   public EventBuilder withActualEndDate( Date anActualEndDate ) {
      iActualEndDt = anActualEndDate;

      return this;
   }


   /**
    * Sets the event status
    *
    * @param aStatus
    *           the status
    *
    * @return The builder
    */
   public EventBuilder withStatus( RefEventStatusKey aStatus ) {
      iStatus = aStatus;

      return this;
   }


   /**
    * Sets the event type.
    *
    * @param aType
    *           The event type.
    *
    * @return The builder
    */
   public EventBuilder withType( RefEventTypeKey aType ) {
      iType = aType;

      return this;
   }


   /**
    * Sets the sched priority value
    *
    * @param aOd
    *
    * @return The builder
    */
   public EventBuilder withSchedPriority( RefSchedPriorityKey aSchedPriority ) {
      this.iSchedPriority = aSchedPriority;

      return this;

   }


   /**
    * Sets the evt_inv part number
    *
    * @param aPartNoKey
    *
    * @return The builder
    */
   public EventBuilder withPartNo( PartNoKey aPartNoKey ) {
      iPartNoKey = aPartNoKey;

      return this;
   }
}
