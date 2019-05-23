
package com.mxi.am.domain.builder;

import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.EventKeyInterface;
import com.mxi.mx.core.key.EventRelationKey;
import com.mxi.mx.core.key.RefRelationTypeKey;
import com.mxi.mx.core.table.evt.EvtEventRel;


/**
 * This class helps build relationships between events
 *
 * @see http://eharmony.com/
 */
public class EventRelationshipBuilder implements DomainBuilder<EventRelationKey> {

   private EventKey iFromEvent;
   private EventKey iToEvent;
   private RefRelationTypeKey iType;


   /**
    * Builds the relationship
    *
    * @return The relationship key
    */
   @Override
   public EventRelationKey build() {
      EvtEventRel lEvtEventRel = EvtEventRel.create( iFromEvent, iToEvent, iType );

      return ( EventRelationKey ) lEvtEventRel.getPk();
   }


   /**
    * Sets the from event
    *
    * @param aIsEvent
    *           The from event
    *
    * @return The builder
    */
   public EventRelationshipBuilder fromEvent( EventKeyInterface aIsEvent ) {
      iFromEvent = aIsEvent.getEventKey();

      return this;
   }


   /**
    * Sets the to event.
    *
    * @param aIsEvent
    *           The to event
    *
    * @return The builder
    */
   public EventRelationshipBuilder toEvent( EventKeyInterface aIsEvent ) {
      iToEvent = aIsEvent.getEventKey();

      return this;
   }


   /**
    * Sets the type of the relationship.
    *
    * @param aType
    *           The relationship type
    *
    * @return The builder
    */
   public EventRelationshipBuilder withType( RefRelationTypeKey aType ) {
      iType = aType;

      return this;
   }
}
