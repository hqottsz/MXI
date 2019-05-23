
package com.mxi.am.guice;

import javax.inject.Inject;
import javax.inject.Provider;

import com.mxi.driver.jms.JmsQueueDriver;
import com.mxi.driver.jms.JmsQueueDriverFactory;

import cucumber.runtime.java.guice.ScenarioScoped;


/**
 * The provider for the jms driver which drives the asset management's asb notification queue.
 */
@ScenarioScoped
public class AsbIntegrationErrorJmsQueueDriverProvider implements Provider<JmsQueueDriver> {

   private final JmsQueueDriver iJmsQueueDriver;


   /**
    * Creates a new {@linkplain AsbIntegrationErrorJmsQueueDriverProvider} object.
    *
    */
   @Inject
   public AsbIntegrationErrorJmsQueueDriverProvider(InitialContextFactory aInitialContextFactory,
         JmsQueueDriverFactory aFactory) {

      // Using 30 second timeout as some message are generated by asynchronous events (ex: messages
      // generated from the work item framework).
      iJmsQueueDriver = aFactory.createInstance( aInitialContextFactory.getIntance(),
            "com.mxi.mx.jms.integration.ErrorQueue", 30000 );

   }


   /**
    * {@inheritDoc}
    */
   @Override
   public JmsQueueDriver get() {
      return iJmsQueueDriver;
   }

}