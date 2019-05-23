
package com.mxi.mx.common.alert;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.mxi.mx.common.key.AlertTypeKey;


/**
 * Stub class for testing with the alert engine cache.
 */
public class AlertEngineCacheStub implements AlertEngineCache {

   private Map<AlertTypeKey, AlertType> iMap = new HashMap<AlertTypeKey, AlertType>();
   private Map<String, AlertNotificationRule> iNotifRuleMap =
         new HashMap<String, AlertNotificationRule>();


   /**
    * Associates an alert notification rule to the provided classname.
    *
    * @param aClassName
    *           classname associated to the rule
    * @param aAlertNotificationRule
    *           alert notification rule
    */
   public void addAlertNotificationRule( String aClassName,
         AlertNotificationRule aAlertNotificationRule ) {
      iNotifRuleMap.put( aClassName, aAlertNotificationRule );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public AlertNotificationRule getAlertNotificationRule( String aClassName ) {
      return iNotifRuleMap.get( aClassName );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public AlertPriorityCalculator getAlertPriorityCalculator( String aClassName ) {
      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public AlertType getAlertType( AlertTypeKey aAlertTypeKey ) {
      return iMap.get( aAlertTypeKey );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public Set<AlertType> getAlertTypes() {
      return ( Set<AlertType> ) iMap.values();
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public Set<AlertType> getAlertTypes( AlertTypeKey... aAlertTypeKeys ) {
      Set<AlertType> lAlertTypes = new HashSet<AlertType>( iMap.size() );
      for ( AlertTypeKey lAlertTypeKey : aAlertTypeKeys ) {
         lAlertTypes.add( iMap.get( lAlertTypeKey ) );
      }

      return lAlertTypes;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void refresh() {
      iMap = new HashMap<AlertTypeKey, AlertType>();
      iNotifRuleMap = new HashMap<String, AlertNotificationRule>();
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void setAlertType( AlertType aAlertType ) {
      iMap.put( aAlertType.getPrimaryKey(), aAlertType );
   }
}
