
package com.mxi.am.guice;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.openqa.selenium.WebDriver;

import com.google.inject.AbstractModule;
import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.driver.integrationtesting.Mule;
import com.mxi.am.driver.integrationtesting.MuleXmlBeansMessageDriver;
import com.mxi.am.driver.integrationtesting.Rest;
import com.mxi.am.driver.integrationtesting.RestDriver;
import com.mxi.am.driver.jms.AsbAdapterNotificationQueue;
import com.mxi.am.driver.jms.AsbIntegrationErrorQueue;
import com.mxi.am.driver.web.AssetManagement;
import com.mxi.driver.api.ApiDriver;
import com.mxi.driver.jms.JmsQueueDriver;


/**
 * The module
 */
public class AssetManagementModule extends AbstractModule {

   private static final String USERNAME = "mxintegration";
   private static final String PASSWORD = "password";

   private String iUrlHostName;


   public AssetManagementModule() {
      try {
         iUrlHostName = "http://".concat( InetAddress.getLocalHost().getHostName() );
      } catch ( UnknownHostException unknowHostException ) {
         throw new RuntimeException( "Could not find local host name" );
      }
   }


   /**
    * {@inheritDoc}
    */
   @Override
   protected void configure() {
      bind( URI.class ).annotatedWith( AssetManagement.class )
            .toInstance( URI.create( iUrlHostName.concat( "/maintenix" ) ) );
      bind( RestDriver.class ).annotatedWith( Rest.class )
            .toInstance( new RestDriver( iUrlHostName )
                  .register( HttpAuthenticationFeature.basic( USERNAME, PASSWORD ) ) );
      bind( MuleXmlBeansMessageDriver.class ).annotatedWith( Mule.class ).toInstance(
            new MuleXmlBeansMessageDriver( iUrlHostName.concat( ":63081" ), USERNAME, PASSWORD ) );

      bind( WebDriver.class ).annotatedWith( AssetManagement.class )
            .toProvider( AssetManagementWebDriverProvider.class );
      bind( ApiDriver.class ).annotatedWith( AssetManagement.class )
            .toProvider( AssetManagementApiDriverProvider.class );
      bind( JmsQueueDriver.class ).annotatedWith( AsbAdapterNotificationQueue.class )
            .toProvider( AsbNotificationJmsQueueDriverProvider.class );
      bind( JmsQueueDriver.class ).annotatedWith( AsbIntegrationErrorQueue.class )
            .toProvider( AsbIntegrationErrorJmsQueueDriverProvider.class );
      bind( DatabaseDriver.class ).annotatedWith( AssetManagement.class )
            .toProvider( AssetManagementDatabaseDriverProvider.class );
   }

}
