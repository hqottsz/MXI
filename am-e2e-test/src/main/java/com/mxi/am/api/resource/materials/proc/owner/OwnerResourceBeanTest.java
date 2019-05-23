package com.mxi.am.api.resource.materials.proc.owner;

import static com.mxi.am.helper.api.GenericAmApiCalls.get;
import static com.mxi.am.helper.api.GenericAmApiCalls.search;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.apache.poi.hssf.record.formula.functions.T;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.mxi.am.api.annotation.CSIContractTest;
import com.mxi.am.api.annotation.CSIContractTest.Project;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;
import com.mxi.am.api.resource.organization.Organization;
import com.mxi.am.api.resource.organization.OrganizationSearchParameters;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * Owner API tests
 *
 */
public class OwnerResourceBeanTest {

   private final ObjectMapper objectMapper =
         ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();

   private static final String MXI_OWNER_CD = "MXI";
   private static final String MXI_OWNER_NAME = "Mxi Technologies";

   private String ownerId;
   private Owner expectedOwner;


   @BeforeClass
   public static void setUpClass() {
      try {
         RestAssured.reset();
         RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
         RestAssured.baseURI = "http://".concat( InetAddress.getLocalHost().getHostName() );
         RestAssured.port = 80;

      } catch ( UnknownHostException e ) {
         throw new RuntimeException( "Cannot find local host name." );
      }

   }


   @Before
   public void setUpData() throws JsonParseException, JsonMappingException, IOException {
      ownerId = getOwnerId();
      expectedOwner = buildOwner( ownerId );
   }


   @Test
   @CSIContractTest( Project.EXT_MATERIAL_PLANNER )
   public void testGetOwnerByIdSuccess200()
         throws JsonParseException, JsonMappingException, IOException {
      Response ownerResponse = get( Status.OK.getStatusCode(), Credentials.AUTHORIZED, Owner.PATH,
            ownerId, MediaType.APPLICATION_JSON );
      Owner foundOwner = objectMapper.readValue( ownerResponse.getBody().asString(), Owner.class );

      assertEquals( "Incorrect returned owner: ", expectedOwner, foundOwner );
   }


   @Test
   public void testGetOwnerUnauthorized403() {
      get( Status.FORBIDDEN.getStatusCode(), Credentials.UNAUTHORIZED, Owner.PATH, ownerId,
            MediaType.APPLICATION_JSON );
   }


   @Test
   @CSIContractTest( Project.UPS )
   public void testSearchOwnerSuccess200()
         throws JsonParseException, JsonMappingException, IOException {
      Map<String, String> ownerSearchParams = new HashMap<String, String>();
      ownerSearchParams.put( OwnerSearchParameters.OWNER_CODE_PARAM, MXI_OWNER_CD );

      Response ownersResponse = search( Status.OK.getStatusCode(), Credentials.AUTHORIZED,
            Owner.PATH, ownerSearchParams, MediaType.APPLICATION_JSON );
      List<T> ownersList = objectMapper.readValue( ownersResponse.getBody().asString(),
            TypeFactory.defaultInstance().constructCollectionType( List.class, Owner.class ) );

      assertEquals( "Incorrect number of owners returned: ", 1, ownersList.size() );
      assertEquals( "Incorrect returned owner: ", expectedOwner, ownersList.get( 0 ) );
   }


   @Test
   public void testSearchOwnerUnauthorized403()
         throws JsonParseException, JsonMappingException, IOException {
      search( Status.FORBIDDEN.getStatusCode(), Credentials.UNAUTHORIZED, Owner.PATH,
            new HashMap<String, String>(), MediaType.APPLICATION_JSON );
   }


   private String getOwnerId() throws JsonParseException, JsonMappingException, IOException {
      // Get an owner ID for tests
      Map<String, String> ownerSearchParams = new HashMap<String, String>();
      ownerSearchParams.put( OwnerSearchParameters.OWNER_CODE_PARAM, MXI_OWNER_CD );

      Owner owner = searchByParameters( Owner.PATH, ownerSearchParams, Owner.class );

      return owner.getId();
   }


   private Owner buildOwner( String id )
         throws JsonParseException, JsonMappingException, IOException {

      Owner owner = new Owner();
      owner.setId( id );
      owner.setCode( MXI_OWNER_CD );
      owner.setName( MXI_OWNER_NAME );

      Map<String, String> orgSearchParams = new HashMap<String, String>();
      orgSearchParams.put( OrganizationSearchParameters.PARAM_ORG_CD, MXI_OWNER_CD );
      Organization org =
            searchByParameters( Organization.PATH, orgSearchParams, Organization.class );

      owner.setOrganizationId( org.getId() );

      return owner;
   }


   private <T extends Object> T searchByParameters( String path,
         Map<String, String> searchParameters, Class<T> type )
         throws JsonParseException, JsonMappingException, IOException {

      Response response = search( Status.OK.getStatusCode(), Credentials.AUTHORIZED, path,
            searchParameters, MediaType.APPLICATION_JSON );

      List<T> objectList = objectMapper.readValue( response.getBody().asString(),
            TypeFactory.defaultInstance().constructCollectionType( List.class, type ) );
      return objectList.get( 0 );
   }

}
