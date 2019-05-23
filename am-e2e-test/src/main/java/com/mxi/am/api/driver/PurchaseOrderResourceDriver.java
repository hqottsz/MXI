package com.mxi.am.api.driver;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import com.mxi.am.api.resource.materials.proc.purchaseorder.PurchaseOrder;
import com.mxi.am.driver.integrationtesting.Rest;
import com.mxi.am.driver.integrationtesting.RestDriver;


/**
 * Resource driver for Purchase Order API
 *
 */
public class PurchaseOrderResourceDriver {

   private static final String USERNAME = "mxintegration";
   private static final String PASSWORD = "password";

   @Inject
   @Rest
   private RestDriver iRestDriver;


   private Client buildClient() {
      Client iClient =
            ClientBuilder.newBuilder().property( ClientProperties.FOLLOW_REDIRECTS, true )
                  .property( ClientProperties.JSON_PROCESSING_FEATURE_DISABLE, false )
                  .register( HttpAuthenticationFeature.basic( USERNAME, PASSWORD ) ).build();

      return iClient;
   }


   /**
    * This method creates a purchase order
    *
    * @param aPurchaseOrder
    */
   public void createPurchaseOrder( PurchaseOrder aPurchaseOrder ) {
      Client lClient = buildClient();
      try {
         iRestDriver = new RestDriver(
               "http://".concat( InetAddress.getLocalHost().getHostName() ).concat( "/amapi/" ) );
      } catch ( UnknownHostException aUnknownHostException ) {
         throw new RuntimeException( aUnknownHostException );
      }

      lClient.target( iRestDriver.target( PurchaseOrder.PATH ).getUri() )
            .request( MediaType.APPLICATION_JSON_TYPE )
            .post( Entity.entity( aPurchaseOrder, MediaType.APPLICATION_JSON_TYPE ) );

   }
}
