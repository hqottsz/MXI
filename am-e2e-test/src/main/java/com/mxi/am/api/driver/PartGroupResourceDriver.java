package com.mxi.am.api.driver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.mxi.am.api.helper.Credentials;
import com.mxi.mx.api.configurationslots.ConfigurationSlotApiModel;
import com.mxi.mx.api.id.IdApiModel;
import com.mxi.mx.api.partgroups.PartGroupApiModel;
import com.mxi.mx.api.partgroups.PartGroupResource;
import com.mxi.mx.api.serialization.mapper.ObjectMapperFactory;


/**
 * Resource driver for Part Group Search API
 */
public class PartGroupResourceDriver {

   private static final WebTarget PART_GROUP_SEARCH_API;
   private static final WebTarget HOST;
   static {
      JacksonJsonProvider lJsonProvider = new JacksonJsonProvider();
      ObjectMapper lObjectMapper = ObjectMapperFactory.buildMapper();
      lJsonProvider.setMapper( lObjectMapper );

      String lHost;
      try {
         lHost = "http://".concat( InetAddress.getLocalHost().getHostName() );
         HOST = ClientBuilder.newClient().target( lHost ).register( lJsonProvider );

         PART_GROUP_SEARCH_API = HOST.path( "mxapi/" + PartGroupApiModel.PATH )
               .register( HttpAuthenticationFeature.basic( Credentials.AUTHENTICATED.getUserName(),
                     Credentials.AUTHENTICATED.getPassword() ) );

      } catch ( UnknownHostException e ) {
         throw new RuntimeException( e );
      }
   }


   public Collection<IdApiModel<PartGroupApiModel>> findPartGroups(
         IdApiModel<ConfigurationSlotApiModel> aConfigurationSlotId, String aPartGroupCode ) {
      Response lResponse = PART_GROUP_SEARCH_API
            .queryParam( PartGroupResource.CONFIGURATION_SLOT_ID, aConfigurationSlotId.toString() )
            .queryParam( PartGroupResource.CODE, aPartGroupCode )
            .request( MediaType.APPLICATION_JSON ).accept( MediaType.APPLICATION_JSON ).get();

      if ( Response.Status.NOT_FOUND.getStatusCode() == lResponse.getStatus() ) {
         return Collections.<IdApiModel<PartGroupApiModel>>emptyList();
      }

      ObjectMapper mapper = new ObjectMapper();
      JsonNode lPartGroups;
      try {
         lPartGroups = mapper.readTree( lResponse.readEntity( String.class ) );
      } catch ( IOException e ) {
         throw new RuntimeException( e );
      }

      if ( !lPartGroups.isArray() ) {
         throw new RuntimeException( "Expecting array as root JSON element" );
      }

      Collection<IdApiModel<PartGroupApiModel>> lPartGroupIds = new ArrayList<>();

      for ( JsonNode lPartGroup : lPartGroups ) {
         JsonNode lPartGroupId = lPartGroup.get( "id" );

         if ( lPartGroupId == null ) {
            throw new RuntimeException(
                  "Unable to get part group ID ('id' property) from " + lPartGroup.toString() );
         }

         lPartGroupIds.add( new IdApiModel<PartGroupApiModel>( lPartGroupId.asText() ) );
      }
      return lPartGroupIds;
   }
}
