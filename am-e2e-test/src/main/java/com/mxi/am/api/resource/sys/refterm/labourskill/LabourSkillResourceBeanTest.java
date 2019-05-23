package com.mxi.am.api.resource.sys.refterm.labourskill;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxi.am.api.annotation.CSIContractTest;
import com.mxi.am.api.annotation.CSIContractTest.Project;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * e2e test for LabourSkill ResourceBean
 *
 */
public class LabourSkillResourceBeanTest {

   private static final String APPLICATION_JSON = "application/json";
   private static final String LABOUR_SKILL_PATH = "/amapi/" + LabourSkill.PATH;

   private static final String LABOUR_SKILL_CODE = "ENG";
   private static final String LABOUR_SKILL_NAME = "Engineering";
   private static final String LABOUR_SKILL_DESCRIPTION = "Engineering";
   private static final double ESTIMATED_HOURLY_COST = 75.0;
   private static final boolean ESIGNATURE_REQUIRED = true;

   private static final String LABOUR_SKILL_CODE2 = "PILOT";
   private static final String LABOUR_SKILL_NAME2 = "Pilot";
   private static final String LABOUR_SKILL_DESCRIPTION2 = "Pilot";
   private static final double ESTIMATED_HOURLY_COST2 = 100.0;
   private static final boolean ESIGNATURE_REQUIRED2 = true;

   private static final String LABOUR_SKILL_CODE3 = "LBR";
   private static final String LABOUR_SKILL_NAME3 = "Laborer";
   private static final String LABOUR_SKILL_DESCRIPTION3 = "Laborer";
   private static final double ESTIMATED_HOURLY_COST3 = 75.0;
   private static final boolean ESIGNATURE_REQUIRED3 = true;

   private static final int LABOUR_SKILL_RECORD_COUNT = 20;
   private static final String LABOUR_SKILL_CODE_DOUBLE = "PILOT";


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


   @Test
   @CSIContractTest( { Project.AFKLM_IMECH } )
   public void get_200() throws JsonProcessingException, IOException {
      Response response = getByLabourSkillCode( 200, Credentials.AUTHENTICATED, LABOUR_SKILL_CODE );
      assertLabourSkillForGet( defaultLabourSkillBuilder(), response );
   }


   @Test
   @CSIContractTest( { Project.AFKLM_IMECH } )
   public void search_200() throws JsonProcessingException, IOException {
      Response response = searchAllLabourSkills( 200, Credentials.AUTHENTICATED );
      assertLabourSkillForSearch( defaultLabourSkillListBuilder(), response );
   }


   @Test
   public void get_401() {
      getByLabourSkillCode( 401, Credentials.UNAUTHENTICATED, LABOUR_SKILL_CODE );
   }


   @Test
   public void search_401() {
      searchAllLabourSkills( 401, Credentials.UNAUTHENTICATED );
   }


   @Test
   public void get_403() {
      getByLabourSkillCode( 403, Credentials.UNAUTHORIZED, LABOUR_SKILL_CODE );
   }


   @Test
   public void search_403() {
      searchAllLabourSkills( 403, Credentials.UNAUTHORIZED );
   }


   @Test
   public void get_500() {
      getByLabourSkillCode( 500, Credentials.AUTHENTICATED, LABOUR_SKILL_CODE_DOUBLE );
   }


   /**
    * Gets a LabourSkill by passing the LabourSkill code to LabourSkill API
    *
    */
   private Response getByLabourSkillCode( int statusCode, Credentials security,
         String labourSkillCode ) {
      String username = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().pathParam( "labourSkillCode", labourSkillCode ).auth()
            .preemptive().basic( username, password ).accept( APPLICATION_JSON )
            .contentType( APPLICATION_JSON ).expect().statusCode( statusCode ).when()
            .get( LABOUR_SKILL_PATH + "/{labourSkillCode}" );
      return response;
   }


   /**
    * Gets all LabourSkills by LabourSkill API
    *
    */
   private Response searchAllLabourSkills( int statusCode, Credentials security ) {
      String username = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( username, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).expect()
            .statusCode( statusCode ).when().get( LABOUR_SKILL_PATH );
      return response;
   }


   private void assertLabourSkillForGet( LabourSkill expectedLabourSkill,
         Response actualLabourSkill ) throws JsonParseException, JsonMappingException, IOException {
      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      LabourSkill labourSkillActual =
            objectMapper.readValue( actualLabourSkill.getBody().asString(),
                  objectMapper.getTypeFactory().constructType( LabourSkill.class ) );
      Assert.assertEquals( expectedLabourSkill, labourSkillActual );
   }


   private void assertLabourSkillForSearch( List<LabourSkill> labourSkillList,
         Response actualLabourSkillList )
         throws JsonParseException, JsonMappingException, IOException {
      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      List<LabourSkill> labourSkillActual =
            objectMapper.readValue( actualLabourSkillList.getBody().asString(), objectMapper
                  .getTypeFactory().constructCollectionType( List.class, LabourSkill.class ) );
      assertEquals( LABOUR_SKILL_RECORD_COUNT, labourSkillActual.size() );
      Assert.assertTrue( labourSkillActual.containsAll( labourSkillList ) );
   }


   /**
    * Creates a default LabourSkill object
    *
    */
   private LabourSkill defaultLabourSkillBuilder() {
      LabourSkill labourSkill = new LabourSkill();
      labourSkill.setCode( LABOUR_SKILL_CODE );
      labourSkill.setName( LABOUR_SKILL_NAME );
      labourSkill.setDescription( LABOUR_SKILL_DESCRIPTION );
      labourSkill.setEstimatedHourlyCost( ESTIMATED_HOURLY_COST );
      labourSkill.setEsignatureRequired( ESIGNATURE_REQUIRED );
      return labourSkill;
   }


   /**
    * Creates a default LabourSkill object List
    *
    */
   public List<LabourSkill> defaultLabourSkillListBuilder() {

      List<LabourSkill> labourSkillObjList = new ArrayList<LabourSkill>();

      LabourSkill labourSkill1 = new LabourSkill();
      labourSkill1.setCode( LABOUR_SKILL_CODE );
      labourSkill1.setName( LABOUR_SKILL_NAME );
      labourSkill1.setDescription( LABOUR_SKILL_DESCRIPTION );
      labourSkill1.setEstimatedHourlyCost( ESTIMATED_HOURLY_COST );
      labourSkill1.setEsignatureRequired( ESIGNATURE_REQUIRED );

      LabourSkill labourSkill2 = new LabourSkill();
      labourSkill2.setCode( LABOUR_SKILL_CODE2 );
      labourSkill2.setName( LABOUR_SKILL_NAME2 );
      labourSkill2.setDescription( LABOUR_SKILL_DESCRIPTION2 );
      labourSkill2.setEstimatedHourlyCost( ESTIMATED_HOURLY_COST2 );
      labourSkill2.setEsignatureRequired( ESIGNATURE_REQUIRED2 );

      LabourSkill labourSkill3 = new LabourSkill();
      labourSkill3.setCode( LABOUR_SKILL_CODE3 );
      labourSkill3.setName( LABOUR_SKILL_NAME3 );
      labourSkill3.setDescription( LABOUR_SKILL_DESCRIPTION3 );
      labourSkill3.setEstimatedHourlyCost( ESTIMATED_HOURLY_COST3 );
      labourSkill3.setEsignatureRequired( ESIGNATURE_REQUIRED3 );

      labourSkillObjList.add( labourSkill1 );
      labourSkillObjList.add( labourSkill2 );
      labourSkillObjList.add( labourSkill3 );

      return labourSkillObjList;
   }
}
