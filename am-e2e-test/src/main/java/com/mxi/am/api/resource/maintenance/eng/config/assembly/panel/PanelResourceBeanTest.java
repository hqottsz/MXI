package com.mxi.am.api.resource.maintenance.eng.config.assembly.panel;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;
import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.driver.common.database.Result;
import com.mxi.am.guice.AssetManagementDatabaseDriverProvider;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * Panel API Test
 *
 */
public class PanelResourceBeanTest {

   private final String applicationJson = "application/json";
   private final String panelPath = "/amapi/" + Panel.PATH;

   private String panelId;
   private String assemblyId;
   private String assemblyCode = "ACFT_LT7";
   private String zoneCode = "ZONEA";
   private String panelCode = "PANEL1";
   private String panelDescription = "Panel 1";

   private DatabaseDriver driver;


   @BeforeClass
   public static void setUpClass() {
      try {
         RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
         RestAssured.baseURI = "http://".concat( InetAddress.getLocalHost().getHostName() );

         RestAssured.port = 80;

      } catch ( UnknownHostException e ) {
         throw new RuntimeException( "Cannot find local host name." );
      }
   }


   @Before
   public void setUpData() throws Exception {
      driver = new AssetManagementDatabaseDriverProvider().get();

      Result panelResult = driver.select(
            "select eqp_task_panel.alt_id from eqp_task_panel left outer join eqp_task_zone on eqp_task_panel.zone_db_id =  eqp_task_zone.zone_db_id and eqp_task_panel.zone_id = eqp_task_zone.zone_id where panel_cd=? and eqp_task_zone.zone_cd=?",
            panelCode, zoneCode );

      Result assemblyResult =
            driver.select( "select alt_id from eqp_assmbl where assmbl_cd=?", assemblyCode );

      if ( panelResult.isEmpty() ) {
         fail( "Could not find the Panel with code: " + panelCode );
      }

      if ( assemblyResult.isEmpty() ) {
         fail( "Could not find the Assembly with code: " + assemblyCode );
      }

      panelId = panelResult.get( 0 ).getUuidString( "alt_id" );
      assemblyId = assemblyResult.get( 0 ).getUuidString( "alt_id" );
   }


   @Test
   public void testSearchPanelSuccessReturns200() throws JsonProcessingException, IOException {
      Response response = createSearch( 200, Credentials.AUTHENTICATED, assemblyId, zoneCode,
            panelCode, applicationJson, applicationJson );
      assertPanelForSearch( defaultPanelBuilder(), response );
   }


   @Test
   public void testSearchPanelUnauthenticatedReturns401() {
      createSearch( 401, Credentials.UNAUTHENTICATED, assemblyId, zoneCode, panelCode,
            applicationJson, applicationJson );
   }


   @Test
   public void testGetPanelByIdSuccessReturns200() throws JsonProcessingException, IOException {
      Response response = createGetById( 200, Credentials.AUTHENTICATED, panelId, applicationJson,
            applicationJson );
      assertPanelForGet( defaultPanelBuilder(), response );
   }


   @Test
   public void testGetPanelByIdUnauthenticatedReturns401() {
      createGetById( 401, Credentials.UNAUTHENTICATED, panelId, applicationJson, applicationJson );
   }


   @Test
   public void testGetPanelByIdUnauthorizedReturns403()
         throws JsonProcessingException, IOException {
      createGetById( 403, Credentials.UNAUTHORIZED, panelId, applicationJson, applicationJson );
   }


   /**
    * Searches a Panel by using the Panel API
    *
    */
   private Response createSearch( int statusCode, Credentials security, String assemblyId,
         String zoneCode, String panelCode, String contentType, String acceptType ) {

      String userName = security.getUserName();
      String password = security.getPassword();

      Response response =
            RestAssured.given().queryParam( PanelResource.ASSEMBLY_ID_PARAM, assemblyId )
                  .queryParam( PanelResource.ZONE_CODE_PARAM, zoneCode )
                  .queryParam( PanelResource.PANEL_CODE_PARAM, panelCode ).auth().preemptive()
                  .basic( userName, password ).accept( acceptType ).contentType( contentType )
                  .expect().statusCode( statusCode ).when().get( panelPath );
      return response;
   }


   /**
    * Gets a Panel by passing Id to the Panel API
    *
    */
   private Response createGetById( int statusCode, Credentials security, String panelId,
         String contentType, String acceptType ) {
      String username = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().pathParam( "panelUUId", panelId ).auth().preemptive()
            .basic( username, password ).accept( acceptType ).contentType( contentType ).expect()
            .statusCode( statusCode ).when().get( panelPath + "/{panelUUId}" );
      return response;
   }


   private void assertPanelForSearch( Panel expectedPanel, Response actualPanel )
         throws JsonProcessingException, IOException {

      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      List<Panel> panelActual = objectMapper.readValue( actualPanel.getBody().asString(),
            objectMapper.getTypeFactory().constructCollectionType( List.class, Panel.class ) );
      Assert.assertEquals( 1, panelActual.size() );
      Assert.assertEquals( expectedPanel, panelActual.get( 0 ) );

   }


   private void assertPanelForGet( Panel expectedPanel, Response actualPanel )
         throws JsonProcessingException, IOException {
      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      Panel panelActual = objectMapper.readValue( actualPanel.getBody().asString(),
            objectMapper.getTypeFactory().constructType( Panel.class ) );
      Assert.assertEquals( expectedPanel, panelActual );
   }


   /**
    * Creates a default Panel object
    *
    */
   private Panel defaultPanelBuilder() {
      Panel panel = new Panel();
      panel.setAssemblyId( assemblyId );
      panel.setAssemblyCode( assemblyCode );
      panel.setId( panelId );
      panel.setPanelCode( panelCode );
      panel.setZoneCode( zoneCode );
      panel.setPanelDescription( panelDescription );
      return panel;
   }
}
