package com.mxi.am.api.resource.sys.refterm.partstatus;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxi.am.api.annotation.CSIContractTest;
import com.mxi.am.api.annotation.CSIContractTest.Project;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;
import com.mxi.am.helper.api.GenericAmApiCalls;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * e2e test for PartStatusResourceBean
 *
 */
public class PartStatusResourceBeanTest {

   private static final String APPLICATION_JSON = MediaType.APPLICATION_JSON;

   private static final String PART_STATUS_CODE1 = "BUILD";
   private static final String PART_STATUS_NAME1 = "Unapproved Part";
   private static final String PART_STATUS_DESCRIPTION1 =
         "The part has been created but not approved";

   private static final String PART_STATUS_CODE2 = "ACTV";
   private static final String PART_STATUS_NAME2 = "Approved Part";
   private static final String PART_STATUS_DESCRIPTION2 =
         "The part has been approved and can be used for installations";

   private static final String PART_STATUS_CODE3 = "OBSLT";
   private static final String PART_STATUS_NAME3 = "Reject Part";
   private static final String PART_STATUS_DESCRIPTION3 =
         "The part is rejected and cannot be used for installations";

   private static final int PART_STATUS_RECORD_COUNT = 3;
   private static final String INVALID_PART_STATUS_CODE = "ACTVX";

   PartStatusPathBuilder partStatusPathBuilder = new PartStatusPathBuilder();


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


   @Test
   @CSIContractTest( { Project.AFKLM_IMECH } )
   public void search_200() throws JsonParseException, JsonMappingException, IOException {

      Response response = GenericAmApiCalls.search( partStatusPathBuilder, null, 200,
            Credentials.AUTHENTICATED, APPLICATION_JSON );
      assertPartStatusForsearch( buildDefaultPartStatus(), response );
   }


   @Test
   public void search_403() throws JsonParseException, JsonMappingException, IOException {

      GenericAmApiCalls.search( partStatusPathBuilder, null, 403, Credentials.UNAUTHORIZED,
            APPLICATION_JSON );
   }


   @Test
   @CSIContractTest( { Project.AFKLM_IMECH } )
   public void get_200() throws JsonParseException, JsonMappingException, IOException {

      Response response = GenericAmApiCalls.get( partStatusPathBuilder, PART_STATUS_CODE1, 200,
            Credentials.AUTHENTICATED, APPLICATION_JSON );
      assertPartStatus( buildDefaultPartStatus().get( 0 ), response );
   }


   @Test
   public void get_404() throws JsonParseException, JsonMappingException, IOException {

      Response response = GenericAmApiCalls.get( partStatusPathBuilder, INVALID_PART_STATUS_CODE,
            404, Credentials.AUTHENTICATED, APPLICATION_JSON );
      Assert.assertEquals( 404, response.getStatusCode() );
   }


   private void assertPartStatusForsearch( List<PartStatus> PartStatusListExpected,
         Response partStatusActualResponse )
         throws JsonParseException, JsonMappingException, IOException {
      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      List<PartStatus> partStatusListActual = objectMapper.readValue(
            partStatusActualResponse.getBody().asString(),
            objectMapper.getTypeFactory().constructCollectionType( List.class, PartStatus.class ) );
      assertEquals( "Expected 3 Part Status found in the database.", PART_STATUS_RECORD_COUNT,
            partStatusListActual.size() );
      Assert.assertEquals( "Incorrect part status list returned.", partStatusListActual,
            PartStatusListExpected );
   }


   private void assertPartStatus( PartStatus PartStatusExpected, Response partStatusActualResponse )
         throws JsonParseException, JsonMappingException, IOException {
      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      PartStatus partStatusListActual =
            objectMapper.readValue( partStatusActualResponse.getBody().asString(),
                  objectMapper.getTypeFactory().constructType( PartStatus.class ) );
      Assert.assertEquals( "Incorrect part status  returned.", partStatusListActual,
            PartStatusExpected );
   }


   private List<PartStatus> buildDefaultPartStatus() {
      List<PartStatus> partStatusList = new ArrayList<PartStatus>();

      PartStatus partStatus1 = new PartStatus();
      partStatus1.setCode( PART_STATUS_CODE1 );
      partStatus1.setName( PART_STATUS_NAME1 );
      partStatus1.setDescription( PART_STATUS_DESCRIPTION1 );
      partStatusList.add( partStatus1 );

      PartStatus partStatus2 = new PartStatus();
      partStatus2.setCode( PART_STATUS_CODE2 );
      partStatus2.setName( PART_STATUS_NAME2 );
      partStatus2.setDescription( PART_STATUS_DESCRIPTION2 );
      partStatusList.add( partStatus2 );

      PartStatus partStatus3 = new PartStatus();
      partStatus3.setCode( PART_STATUS_CODE3 );
      partStatus3.setName( PART_STATUS_NAME3 );
      partStatus3.setDescription( PART_STATUS_DESCRIPTION3 );
      partStatusList.add( partStatus3 );

      return partStatusList;
   }

}
