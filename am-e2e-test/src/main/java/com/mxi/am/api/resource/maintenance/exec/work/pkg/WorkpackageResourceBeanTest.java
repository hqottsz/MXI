package com.mxi.am.api.resource.maintenance.exec.work.pkg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxi.am.api.annotation.CSIContractTest;
import com.mxi.am.api.annotation.CSIContractTest.Project;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;
import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.driver.common.database.Result;
import com.mxi.am.guice.AssetManagementDatabaseDriverProvider;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * Workpackage API test
 *
 */
public class WorkpackageResourceBeanTest {

   private static final String NAME = "Test Work Package";
   private static final String STATUS = "ACTV";
   private static final String TASK_CLASS = "CHECK";
   private static final String DUE_DATA_TYPE = "CDY";
   private static final Boolean REQUEST_PARTS = true;
   private static final String DUE_DATE = "2018-10-03 03:59:59";
   private static final String SCHED_START_DATE = "2018-10-16 23:56:00";
   private static final Boolean HEAVY_MAINTENANCE_BOOL = false;
   private static final String SCHED_END_DATE = "2018-10-20 16:56:00";
   private static final String STATUS_DESCRIPTION = "Commit";

   private static final String LOC_CD = "AIRPORT1/LINE";
   private static final String SERIAL_NO = "LT-SSP-3";
   private static final String ISSUE_ACCOUNT_CD = "5";
   private static final String APPLICATION_JSON = "application/json";
   private static final String WORK_PACKAGE_PATH = "/amapi/" + Workpackage.PATH;
   private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

   private final ObjectMapper iObjectMapper =
         ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();

   private String workpackageId;
   private String locationId;
   private String invId;
   private String issueAccountId;
   private Workpackage workpackage;

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

      Result locationResult = driver.select( "select alt_id from inv_loc where loc_cd=?", LOC_CD );
      if ( locationResult.isEmpty() ) {
         fail( "Could not find the location: " + LOC_CD );
      }
      locationId = locationResult.get( 0 ).getUuidString( "alt_id" );

      Result invIdResult =
            driver.select( "select alt_id from inv_inv where serial_no_oem=?", SERIAL_NO );
      if ( invIdResult.isEmpty() ) {
         fail( "Could not find the inventory with serial number: " + SERIAL_NO );
      }
      invId = invIdResult.get( 0 ).getUuidString( "alt_id" );

      Result issueAccountResult =
            driver.select( "select alt_id from fnc_account where account_cd=?", ISSUE_ACCOUNT_CD );
      if ( issueAccountResult.isEmpty() ) {
         fail( "Could not find the issue to account: " + ISSUE_ACCOUNT_CD );
      }
      issueAccountId = issueAccountResult.get( 0 ).getUuidString( "alt_id" );

   }


   @Test
   @CSIContractTest( Project.UPS )
   public void testWorkpackageGetByIDSuccess() throws IOException, ParseException {
      prepareData();
      Response lResponse = getById( 200, Credentials.AUTHORIZED, workpackageId );

      assertWorkpackageRetrieved( workpackage, getWorkpackageFromResponse( lResponse ) );

   }


   @Test
   public void testWorkpackageGetByIDUnauthorizedReturns403() throws IOException, ParseException {
      prepareData();

      getById( 403, Credentials.UNAUTHORIZED, workpackageId );
   }


   /**
    * Assert two work package attributes
    *
    * @param wp
    *           Actual work package
    * @param workpackageFromResponse
    *           expected work package
    */
   private void assertWorkpackageRetrieved( Workpackage wp, Workpackage workpackageFromResponse ) {
      assertEquals( "Incorrect Aicraft ID: ", workpackageFromResponse.getAircraftId(),
            wp.getAircraftId() );
      assertEquals( "Incorrect Class Mode: ", workpackageFromResponse.getClassMode(),
            wp.getClassMode() );
      assertEquals( "Incorrect value for Heavy Maintenance: ",
            workpackageFromResponse.getHeavyMaintenanceBool(), wp.getHeavyMaintenanceBool() );
      assertEquals( "Incorrect Location ID: ", workpackageFromResponse.getLocationId(),
            wp.getLocationId() );
      assertEquals( "Incorrect Highest Inventory ID: ",
            workpackageFromResponse.getHighestInventoryId(), wp.getHighestInventoryId() );
      assertEquals( "Incorrect Issue Account ID: ", workpackageFromResponse.getIssueAccountId(),
            wp.getIssueAccountId() );
      assertEquals( "Incorrect Inventory ID: ", workpackageFromResponse.getInventoryId(),
            wp.getInventoryId() );
      assertEquals( "Incorrect Name: ", workpackageFromResponse.getName(), wp.getName() );
      assertEquals( "Incorrect Sched End Date: ", workpackageFromResponse.getSchedEndDate(),
            wp.getSchedEndDate() );
      assertEquals( "Incorrect Sched Start Date: ", workpackageFromResponse.getSchedStartDate(),
            wp.getSchedStartDate() );
      assertEquals( "Incorrect Status: ", workpackageFromResponse.getStatus(), wp.getStatus() );

   }


   private Response getById( int statusCode, Credentials security, String wpId ) {
      return getById( statusCode, security, wpId, APPLICATION_JSON, APPLICATION_JSON );
   }


   private Response getById( int statusCode, Credentials security, String wpId, String contentType,
         String acceptType ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      Response lResponse = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( acceptType ).contentType( contentType ).expect().statusCode( statusCode )
            .when().get( WORK_PACKAGE_PATH + "/" + wpId );

      return lResponse;
   }


   private void prepareData()
         throws JsonParseException, JsonMappingException, IOException, ParseException {

      workpackage = getWorkpackage();

      Response lResponse = create( 200, Credentials.AUTHENTICATED, workpackage, APPLICATION_JSON,
            APPLICATION_JSON );

      Workpackage workpackageCreated =
            iObjectMapper.readValue( lResponse.getBody().asString(), Workpackage.class );

      workpackageId = workpackageCreated.getId();
   }


   private Workpackage getWorkpackageFromResponse( Response aResponse ) throws IOException {
      return iObjectMapper.readValue( aResponse.getBody().asString(), Workpackage.class );
   }


   private Response create( int statusCode, Credentials security, Object workpackage,
         String contentType, String acceptType ) {
      String lUserName = security.getUserName();
      String lPassword = security.getPassword();

      Response lResponse = RestAssured.given().auth().preemptive().basic( lUserName, lPassword )
            .accept( acceptType ).contentType( contentType ).body( workpackage ).expect()
            .statusCode( statusCode ).when().post( WORK_PACKAGE_PATH );
      return lResponse;
   }


   /**
    * Create workpackage
    *
    * @return workpackage
    * @throws ParseException
    */
   private Workpackage getWorkpackage() throws ParseException {
      Workpackage workpackage = new Workpackage();

      workpackage.setAircraftId( invId );
      workpackage.setDueDataType( DUE_DATA_TYPE );
      workpackage.setDueDate( new SimpleDateFormat( DATE_FORMAT ).parse( DUE_DATE ) );
      workpackage.setHeavyMaintenanceBool( HEAVY_MAINTENANCE_BOOL );
      workpackage.setHighestInventoryId( invId );
      workpackage.setInventoryId( invId );
      workpackage.setIssueAccountId( issueAccountId );
      workpackage.setLocationId( locationId );
      workpackage.setName( NAME );
      workpackage.setRequestParts( REQUEST_PARTS );
      workpackage.setSchedEndDate( new SimpleDateFormat( DATE_FORMAT ).parse( SCHED_END_DATE ) );
      workpackage
            .setSchedStartDate( new SimpleDateFormat( DATE_FORMAT ).parse( SCHED_START_DATE ) );
      workpackage.setStatus( STATUS );
      workpackage.setStatusDescription( STATUS_DESCRIPTION );
      workpackage.setTaskClass( TASK_CLASS );

      return workpackage;
   }
}
