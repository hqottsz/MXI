package com.mxi.mx.common.services.event;

import java.util.HashMap;
import java.util.Map;

import com.mxi.am.api.events.MaintenixEvent;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.core.services.events.MxEventsService;


/**
 * Stub class allowing the query tests to not rely on the Event Service
 *
 */
public class MxEventServiceStub extends MxEventsService {

   private static Map<String, MaintenixEvent> publishedEventMap =
         new HashMap<String, MaintenixEvent>();


   /**
    * {@inheritDoc}
    */
   @Override
   public void publishEvent( MaintenixEvent maintenixEvent ) throws MxException {
      // Add the published event to hash map instead of sending it to pubsub
      publishedEventMap.put( maintenixEvent.getEventType().name(), maintenixEvent );
   }


   /**
    *
    * {@inheritDoc}
    */
   @Override
   public void publishEvent( MaintenixEvent maintenixMessageEvent, Map<String, String> aProperties )
         throws MxException {
      publishedEventMap.put( maintenixMessageEvent.getEventType().name(), maintenixMessageEvent );
   }


   public Map<String, MaintenixEvent> getPublishedEvents() {
      return publishedEventMap;
   }

}
