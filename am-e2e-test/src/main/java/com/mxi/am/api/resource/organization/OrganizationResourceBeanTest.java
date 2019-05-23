package com.mxi.am.api.resource.organization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;
import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.driver.common.database.Result;
import com.mxi.am.guice.AssetManagementDatabaseDriverProvider;

import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.mapper.factory.Jackson2ObjectMapperFactory;
import io.restassured.response.Response;


/**
 * Organization API application-level tests
 *
 */
public class OrganizationResourceBeanTest {

   private final static String APPLICATION_JSON = "application/json";
   private final static String ORGANIZATION_PATH = "/amapi/" + Organization.PATH;
   private final static String MXI_ORG_CODE = "MXI";
   private final static String MXI_ORG_TYPE = "ADMIN";
   private final static String MXI_SDESC = "Mxi Technologies";
   private final static String MXI_LDESC = "The root organization";

   private DatabaseDriver iDriver;

   private String iOrganizationId;

   private final String iQueryOrgOrg = "SELECT * FROM org_org WHERE org_cd = ?";


   @BeforeClass
   public static void setUpClass() {
      try {
         JacksonJsonProvider lJsonProvider = new JacksonJsonProvider();

         final ObjectMapper sObjectMapper =
               ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
         lJsonProvider.setMapper( sObjectMapper );

         RestAssured.reset();
         RestAssured.config =
               RestAssuredConfig.config().objectMapperConfig( new ObjectMapperConfig()
                     .jackson2ObjectMapperFactory( new Jackson2ObjectMapperFactory() {

                        @Override
                        public ObjectMapper create( @SuppressWarnings( "rawtypes" ) Class aClass,
                              String s ) {
                           return sObjectMapper;
                        }
                     } ) );
         RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
         RestAssured.baseURI = "http://".concat( InetAddress.getLocalHost().getHostName() );

         RestAssured.port = 80;

      } catch ( UnknownHostException e ) {
         throw new RuntimeException( "Cannot find local host name." );
      }

   }


   @Before
   public void setUpData() throws Exception {

      iDriver = new AssetManagementDatabaseDriverProvider().get();
      Result lOrgOrg = iDriver.select( iQueryOrgOrg, MXI_ORG_CODE );

      if ( lOrgOrg.getNumberOfRows() > 1 ) {
         fail( "Expecting a single row but found " + lOrgOrg.getNumberOfRows() );
      }

      iOrganizationId = lOrgOrg.get( 0 ).getUuidString( "alt_id" );
   }


   @Test
   public void testSuccessSearchOrganizationByCode() throws JsonProcessingException, IOException {

      Response lResponse = getByCode( 200, Credentials.AUTHORIZED, MXI_ORG_CODE );

      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      List<Organization> lOrganizations =
            objectMapper.readValue( lResponse.getBody().asString(), objectMapper.getTypeFactory()
                  .constructCollectionType( List.class, Organization.class ) );

      assertEquals( "Expected only one organzation to be found: ", 1, lOrganizations.size() );

      Organization lOrganizationExpected = constructOrganization();

      Assert.assertEquals( "Expected organization object does not match with actual object",
            lOrganizationExpected, lOrganizations.get( 0 ) );
   }


   @Test
   public void testSearchOrganizationUnauthenticatedReturns401() throws ParseException {
      getByCode( 401, Credentials.UNAUTHENTICATED, MXI_ORG_CODE );
   }


   @Test
   public void testSearchOrganizationUnauthorizedAccessReturns403() {
      getByCode( 403, Credentials.UNAUTHORIZED, MXI_ORG_CODE );
   }


   @Test
   public void testSuccessGetOrganizationById() throws JsonProcessingException, IOException {

      Response lResponse = getById( 200, Credentials.AUTHORIZED, iOrganizationId );

      Organization lOrganization = ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY
            .createObjectMapper().readValue( lResponse.getBody().asString(), Organization.class );

      Organization lOrganizationExpected = constructOrganization();

      Assert.assertEquals( "Expected organization object does not match with actual object",
            lOrganizationExpected, lOrganization );
   }


   @Test
   public void testGetOrganizationUnauthenticatedReturns401()
         throws ParseException, JsonProcessingException, IOException {
      getByCode( 401, Credentials.UNAUTHENTICATED, iOrganizationId );
   }


   @Test
   public void testGetOrganizationUnauthorizedAccessReturns403()
         throws JsonProcessingException, IOException {
      getByCode( 403, Credentials.UNAUTHORIZED, iOrganizationId );
   }


   private Response getByCode( int aStatusCode, Credentials aSecurity, String aOrgCode ) {
      String lUserName = aSecurity.getUserName();
      String lPassword = aSecurity.getPassword();

      Response lResponse = RestAssured.given().auth().preemptive().basic( lUserName, lPassword )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).expect()
            .statusCode( aStatusCode ).when().get( ORGANIZATION_PATH + "?code=" + aOrgCode );
      return lResponse;
   }


   private Response getById( int aStatusCode, Credentials aSecurity, String aOrgId ) {
      return getById( aStatusCode, aSecurity, aOrgId, APPLICATION_JSON, APPLICATION_JSON );
   }


   private Response getById( int aStatusCode, Credentials aSecurity, String aOrgId,
         String aContentType, String aAcceptType ) {
      String lUserName = aSecurity.getUserName();
      String lPassword = aSecurity.getPassword();

      Response lResponse = RestAssured.given().auth().preemptive().basic( lUserName, lPassword )
            .accept( aAcceptType ).contentType( aContentType ).expect().statusCode( aStatusCode )
            .when().get( ORGANIZATION_PATH + "/" + aOrgId );
      return lResponse;
   }


   private Organization constructOrganization() {

      Organization lOrganization = new Organization();

      lOrganization.setId( iOrganizationId );
      lOrganization.setOrgCode( MXI_ORG_CODE );
      lOrganization.setShortDesc( MXI_SDESC );
      lOrganization.setOrgType( MXI_ORG_TYPE );
      lOrganization.setLongDesc( MXI_LDESC );

      return lOrganization;
   }
}
