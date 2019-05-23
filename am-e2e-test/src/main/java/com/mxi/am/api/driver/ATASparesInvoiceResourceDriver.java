package com.mxi.am.api.driver;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import com.mxi.am.driver.integrationtesting.Rest;
import com.mxi.am.driver.integrationtesting.RestDriver;


/**
 * Resource driver for Invoice API
 *
 */
public class ATASparesInvoiceResourceDriver {

   private static final String USERNAME = "mxintegration";
   private static final String PASSWORD = "password";
   private static final String PATH_API = "/integrationweb/rest/";
   private static final String PATH_INVOICE = "ataspares/invoice";

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
    * This method creates an Invoice using REST API
    *
    * @param aATASparesInvoice
    *           XML message
    * @return status code of the response
    */
   public String createInvoice( String aATASparesInvoice ) {
      Client lClient = buildClient();
      try {
         iRestDriver = new RestDriver(
               "http://".concat( InetAddress.getLocalHost().getHostName() ).concat( PATH_API ) );
      } catch ( UnknownHostException e ) {
         throw new RuntimeException( e );
      }

      Response lResponse = lClient.target( iRestDriver.target( PATH_INVOICE ).getUri() )
            .request( MediaType.APPLICATION_XML )
            .post( Entity.entity( aATASparesInvoice, MediaType.APPLICATION_XML ) );
      return String.valueOf( lResponse.getStatus() );
   }
}
