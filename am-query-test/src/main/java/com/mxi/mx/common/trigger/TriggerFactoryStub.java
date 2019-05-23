
package com.mxi.mx.common.trigger;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This is a stub for the {@link TriggerFactory} that allows testing triggers.
 */
public class TriggerFactoryStub extends TriggerFactory {

   private final Map<String, Object> iTriggerMap;


   /**
    * Creates a new {@linkplain TriggerFactoryStub} object.
    */
   public TriggerFactoryStub() {
      super();

      // returns null for all triggers
      iTriggerMap = new HashMap<String, Object>();
   }


   /**
    * Creates a new {@linkplain TriggerFactoryStub} object.
    *
    * @param aTriggerMap
    *           the trigger map
    */
   public TriggerFactoryStub(Map<String, Object> aTriggerMap) {
      iTriggerMap = aTriggerMap;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public Object createInstance( String aTriggerCode ) {
      return iTriggerMap.get( aTriggerCode );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public List<Object> createInstanceArray( String aTriggerCode ) {
      Object lObject = iTriggerMap.get( aTriggerCode );
      if ( lObject == null ) {
         return Collections.emptyList();
      }

      return Arrays.asList( lObject );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isTriggerActive( String aTriggerCode ) {
      return iTriggerMap.containsKey( aTriggerCode );
   }


   /**
    * Helper method to set a particular trigger.
    */
   public void setTrigger( String aTriggerCode, Object aTrigger ) {
      iTriggerMap.put( aTriggerCode, aTrigger );
   }
}
