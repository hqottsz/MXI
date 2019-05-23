
package com.mxi.am.domain.builder;

import java.util.Date;

import com.mxi.am.domain.Aircraft;
import com.mxi.mx.common.exception.MxRuntimeException;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventTypeKey;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.EvtInvTable;


/**
 * Builds an <code>evt_event</code> object with an event type of 'BLK' (blackout).
 *
 * @deprecated Blackouts are property of the Aircraft; use {@link Aircraft#addBlackout(Date, Date)}
 *             instead.
 */
@Deprecated
public class BlackoutBuilder implements DomainBuilder<EventKey> {

   private InventoryKey iAircraft;
   private Date iEnd;
   private Date iStart;


   /**
    * {@inheritDoc}
    */
   @Override
   public EventKey build() {

      if ( iAircraft == null ) {
         throw new MxRuntimeException( "BlackoutBuilder: aircraft key cannot be null." );
      }

      if ( iStart == null ) {
         throw new MxRuntimeException( "BlackoutBuilder: aircraft key cannot be null." );
      }

      if ( iEnd == null ) {
         throw new MxRuntimeException( "BlackoutBuilder: aircraft key cannot be null." );
      }

      EventKey lEventKey = EvtEventTable.generatePrimaryKey();

      EvtEventTable lEventTable = EvtEventTable.create( lEventKey );
      lEventTable.setEventType( RefEventTypeKey.BLK );
      lEventTable.setActualStartDate( iStart );
      lEventTable.setEventDate( iEnd );
      lEventTable.insert();

      EvtInvTable lEvtInv = EvtInvTable.create( lEventKey );
      lEvtInv.setInventoryKey( iAircraft );
      lEvtInv.insert();

      return lEventKey;
   }


   /**
    * Set the end date of the blackout period.
    *
    * @param aEnd
    *           end date
    *
    * @return the builder
    */
   public BlackoutBuilder endingAt( Date aEnd ) {
      iEnd = aEnd;

      return this;
   }


   /**
    * Set the start date of the blackout period.
    *
    * @param aStart
    *           start date
    *
    * @return the builder
    */
   public BlackoutBuilder startingAt( Date aStart ) {
      iStart = aStart;

      return this;
   }


   /**
    * Set the aircraft to which the blackout period applies.
    *
    * @param aAircraft
    *           aircraft key
    *
    * @return the builder
    */
   public BlackoutBuilder withAircraft( AircraftKey aAircraft ) {
      return withAircraft( aAircraft.getInventoryKey() );
   }


   /**
    * Set the aircraft to which the blackout period applies.
    *
    * @param aAircraft
    *           inventory key
    *
    * @return the builder
    */
   public BlackoutBuilder withAircraft( InventoryKey aAircraft ) {
      iAircraft = aAircraft;

      return this;
   }
}
