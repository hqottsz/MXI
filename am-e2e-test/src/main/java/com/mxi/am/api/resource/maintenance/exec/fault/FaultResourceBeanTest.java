package com.mxi.am.api.resource.maintenance.exec.fault;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.MediaType;

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
import com.mxi.am.api.resource.AmApiMediaType;
import com.mxi.am.api.resource.ObjectMapperFactory;
import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.driver.common.database.Result;
import com.mxi.am.guice.AssetManagementDatabaseDriverProvider;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * Fault API Test
 *
 */
public class FaultResourceBeanTest {

   private static final String INVALID_ID = "9867054582A44C50A0A49B6C6412AA";
   private static final String ID = "1549D92523E04B9B92A9F6F59B1B53F1";
   private static final String SEVERITY_UNKNOWN = "MINOR";
   private static final String FOUNDDATE = "2018-07-09T04:00:00Z";
   private static final String STATUS_OPEN = "OPEN";
   private static final Float SCHEDULING_RULES_START_USAGE = new Float( "0.0" );
   private static final String ESTIMATED_DURATION = "0.0";
   private static final Double ESTIMATED_DURATION2 = new Double( "0.0" );
   private static final String FAILED_SYSTEM = "Aircraft Part 2 - G500";
   private static final boolean HISTORIC = false;
   private static final String SEVERITY_TYPE_UNKNOWN = "MINOR";
   private static final String FLIGHT_LEG_NO = "Planned Flight PUD-DS-1";
   private static final String DESCRIPTION = "Fault Description";
   private static final String HIGHEST_INV_SERIAL_NO_OEM = "20986-03";
   private static final String LOGBOOK_TYPE_CABIN = "CABIN";
   private static final String FLIGHT_PHASE_ICL = "ICL";
   private static final String FOUND_BY_PERSON_USERNAME = "user1";
   private static final String FAULT_SOURCE_PILOT = "PILOT";
   private static final String FAULT_CODE1 = "FC00100";
   private static final String NAME = "Fault name";
   private static final String NAME2 = "Edited fault name";
   private static final String SEVERITY_MINOR = "MINOR";
   private static final Object HIGHEST_INV_SERIAL_NO_OEM2 = "OPER-9088";
   private static final String SERIAL_NO_OEM1 = "GJA-911";
   private static final Object FLIGHT_LEG_NO2 = "Planned Flight PUD-AUTH-1";
   private static final Object FOUND_BY_PERSON_USERNAME2 = "user2";
   private static final String DESCRIPTION2 = "Updated fault description";
   private static final String LOGBOOK_TYPE_TECH = "TECH";
   private static final String FLIGHT_PHASE_PRF = "PRF";
   private static final String FAULT_SOURCE_CABIN = "CABIN";
   private static final String FAULT_CODE2 = "FC00101";
   private static final Object WORKPACKAGE_NAME = "Fault API Test Work Package";

   private static final String APPLICATION_JSON = MediaType.APPLICATION_JSON;
   private static final String APPLICATION_JSON_V2 = AmApiMediaType.AMAPI_V2_TYPE;
   private static final String FAULT_API_PATH = "/amapi/" + FaultPathBuilder.PATH_BASE;
   private final ObjectMapper objectMapper =
         ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
   private static DatabaseDriver iDriver;

   private static Fault iFault;
   private static FaultV2 iFaultV2;
   private static String workPackageId;
   private static String iInventoryId;


   @BeforeClass
   public static void setUpClass() throws JsonParseException, JsonMappingException, IOException,
         ParseException, ClassNotFoundException, SQLException {
      try {
         RestAssured.reset();
         RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
         RestAssured.baseURI = "http://".concat( InetAddress.getLocalHost().getHostName() );
         RestAssured.port = 80;

      } catch ( UnknownHostException e ) {
         throw new RuntimeException( "Cannot find local host name." );
      }
      iDriver = new AssetManagementDatabaseDriverProvider().get();
      prepareData();

   }


   private static void prepareData() throws JsonParseException, JsonMappingException, IOException,
         ParseException, ClassNotFoundException, SQLException {
      // create a fault to be used in tests
      Fault lFault = defaultFault();
      FaultV2 lFaultV2 = defaultFaultV2();
      Response lResponse = create( 200, Credentials.AUTHENTICATED, lFault );
      Response lResponseFaultV2 = createFaultV2( 200, Credentials.AUTHENTICATED, lFaultV2 );
      iFault = constructFaultFromResponse( lResponse.getBody().asString() );
      iFaultV2 = constructFaultV2FromResponse( lResponseFaultV2.getBody().asString() );
   }


   /**
    * Test get method for success
    *
    * @throws IOException
    * @throws ParseException
    * @throws ClassNotFoundException
    * @throws SQLException
    */
   @Test
   @CSIContractTest( Project.UPS )
   public void testFaultGetByIdSuccessReturns200()
         throws IOException, ParseException, ClassNotFoundException, SQLException {

      Response lResponse = getById( 200, Credentials.AUTHORIZED, iFault.getId() );

      Fault lActualFault = objectMapper.readValue( lResponse.getBody().asString(), Fault.class );

      assertEquals( "Incorrect content type returned", lResponse.getContentType(),
            APPLICATION_JSON );
      assertFault( iFault, lActualFault );

   }


   /**
    * Test get method for not found
    *
    * @throws IOException
    * @throws ParseException
    */
   @Test
   public void testFaultGetByIdNotFoundReturns404() throws IOException, ParseException {

      Response lResponse = getById( 404, Credentials.AUTHORIZED, INVALID_ID );

      Assert.assertEquals( 404, lResponse.getStatusCode() );

   }


   /**
    * Test get method for unauthenticated access
    *
    */
   @Test
   public void testFaultGetByIdUnauthenticatedReturns401() {

      Response lResponse = getById( 401, Credentials.UNAUTHENTICATED, iFault.getId() );

      Assert.assertEquals( 401, lResponse.getStatusCode() );
   }


   /**
    * Test get method for unauthorized access
    *
    */
   @Test
   public void testFaultGetByIdUnauthorizedReturns403() {

      iFault = new Fault();
      getById( 403, Credentials.UNAUTHORIZED, iFault.getId() );
   }


   @Test
   @CSIContractTest( Project.UPS )
   public void testFaultSearchSuccessReturns200()
         throws IOException, ParseException, ClassNotFoundException, SQLException {

      Response response = search( 200, Credentials.AUTHORIZED );

      assertEquals( "Incorrect content type returned", response.getContentType(),
            APPLICATION_JSON );
      assertFaultSearch( response );

   }


   @Test
   public void testCreateFaultSuccess200() throws Exception {

      Fault lNewFault = buildNewFaultObject();
      Response lResponse = create( 200, Credentials.AUTHENTICATED, lNewFault );

      assertEquals( "Incorrect content type returned", lResponse.getContentType(),
            APPLICATION_JSON );

      Fault lCreatedFault = constructFaultFromResponse( lResponse.getBody().asString() );
      assertCreatedFault( lNewFault, lCreatedFault );

   }


   @Test
   public void testCreateFaultWithInvalidAuthentication401()
         throws JsonParseException, JsonMappingException, IOException {
      Fault lNewFault = new Fault();
      create( 401, Credentials.UNAUTHENTICATED, lNewFault );
   }


   @Test
   public void testCreateFaultUnauthorizedReturns403()
         throws JsonParseException, JsonMappingException, IOException {
      Fault newFault = new Fault();
      create( 403, Credentials.UNAUTHORIZED, newFault );
   }


   @Test
   public void testUpdateFaultWithInvalidAuthentication401()
         throws JsonParseException, JsonMappingException, IOException {
      Fault lUpdateDetails = new Fault();
      updateFault( 401, "", lUpdateDetails, Credentials.UNAUTHENTICATED );

   }


   @Test
   @CSIContractTest( Project.SWA_MOPP )
   public void testUpdateFaultDetailsSuccess200()
         throws JsonParseException, JsonMappingException, IOException {
      // Create new fault
      Fault lNewFault = buildNewFaultObject();
      Response lCreateResponse = create( 200, Credentials.AUTHENTICATED, lNewFault );
      assertEquals( "Incorrect content type returned", lCreateResponse.getContentType(),
            APPLICATION_JSON );
      Fault lCreatedFault = constructFaultFromResponse( lCreateResponse.getBody().asString() );

      // Update the created fault
      Fault lUpdateDetails = buildFaultWithUpdateDetails();
      lUpdateDetails.setFaultId( lCreatedFault.getFaultId() );

      Response lUpdateResponse =
            updateFault( 200, lCreatedFault.getId(), lUpdateDetails, Credentials.AUTHENTICATED );
      assertEquals( "Incorrect content type returned", lUpdateResponse.getContentType(),
            APPLICATION_JSON );
      Assert.assertEquals( 200, lUpdateResponse.getStatusCode() );

      // Get the updated fault
      Response lGetUpdatedFaultResponse =
            getById( 200, Credentials.AUTHENTICATED, lCreatedFault.getId() );
      assertEquals( "Incorrect content type returned", lGetUpdatedFaultResponse.getContentType(),
            APPLICATION_JSON );
      Fault lUpdatedFault =
            constructFaultFromResponse( lGetUpdatedFaultResponse.getBody().asString() );

      assertUpdatedFault( lUpdateDetails, lUpdatedFault );

   }


   @Test
   public void testUpdateFaultUnauthorizedReturns403()
         throws JsonParseException, JsonMappingException, IOException {
      Fault updateDetails = new Fault();
      updateFault( 403, ID, updateDetails, Credentials.UNAUTHORIZED );

   }


   @Test
   public void testFaultSearchUnauthenticatedReturns401()
         throws IOException, ParseException, ClassNotFoundException, SQLException {
      search( 401, Credentials.UNAUTHENTICATED );
   }


   /**
    * Test get method for success
    *
    * @throws JsonMappingException
    * @throws JsonParseException
    * @throws IOException
    */
   @Test
   public void testFaultV2GetByIdSuccessReturns200()
         throws JsonParseException, JsonMappingException, IOException {
      Response lResponse = getFaultV2ById( 200, Credentials.AUTHORIZED, iFaultV2.getId() );

      assertEquals( "Incorrect content type returned", lResponse.getContentType(),
            APPLICATION_JSON_V2 );

      FaultV2 lActualFault =
            objectMapper.readValue( lResponse.getBody().asString(), FaultV2.class );
      assertFaultV2( iFaultV2, lActualFault );
   }


   /**
    * Test get method for not found
    *
    */
   @Test
   public void testFaultV2GetByIdNotFoundReturns404() {
      Response lResponse = getFaultV2ById( 404, Credentials.AUTHORIZED, INVALID_ID );
      Assert.assertEquals( 404, lResponse.getStatusCode() );
   }


   /**
    * Test get method for unauthenticated access
    *
    */
   @Test
   public void testFaultV2GetByIdUnauthenticatedReturns401() {
      Response lResponse = getFaultV2ById( 401, Credentials.UNAUTHENTICATED, iFaultV2.getId() );
      Assert.assertEquals( 401, lResponse.getStatusCode() );
   }


   /**
    * Test get method for unauthorized access
    *
    */
   @Test
   public void testFaultV2GetByIdUnauthorizedReturns403() {
      iFaultV2 = new FaultV2();
      getFaultV2ById( 403, Credentials.UNAUTHORIZED, iFaultV2.getId() );
   }


   @Test
   public void testCreateFaultV2Success200() throws Exception {

      FaultV2 lNewFault = buildNewFaulV2Object();
      Response lResponse = createFaultV2( 200, Credentials.AUTHENTICATED, lNewFault );

      assertEquals( "Incorrect content type returned", lResponse.getContentType(),
            APPLICATION_JSON_V2 );

      FaultV2 lCreatedFault = constructFaultV2FromResponse( lResponse.getBody().asString() );
      assertCreatedFaultV2( lNewFault, lCreatedFault );

   }


   @Test
   public void testCreateFaultV2WithInvalidAuthentication401()
         throws JsonParseException, JsonMappingException, IOException {
      FaultV2 lNewFault = new FaultV2();
      createFaultV2( 401, Credentials.UNAUTHENTICATED, lNewFault );
   }


   @Test
   public void testCreateFaultV2UnauthorizedReturns403()
         throws JsonParseException, JsonMappingException, IOException {
      FaultV2 newFault = new FaultV2();
      createFaultV2( 403, Credentials.UNAUTHORIZED, newFault );
   }


   public void testUpdateWorkpackageSuccess()
         throws JsonParseException, JsonMappingException, IOException {
      // Create new fault
      Fault lNewFault = buildNewFaultObject();
      Response lCreateResponse = create( 200, Credentials.AUTHENTICATED, lNewFault );
      Fault lCreatedFault = constructFaultFromResponse( lCreateResponse.getBody().asString() );

      // Update the work package of the created fault
      Fault lUpdateDetails = buildFaultWithUpdateWorkpackageDetails( lCreatedFault.getId() );
      updateFault( 200, lCreatedFault.getId(), lUpdateDetails, Credentials.AUTHENTICATED );

      // Get the updated fault
      Response lGetUpdatedFaultResponse =
            getById( 200, Credentials.AUTHENTICATED, lCreatedFault.getId() );
      Fault lUpdatedFault =
            constructFaultFromResponse( lGetUpdatedFaultResponse.getBody().asString() );

      // Validate updated work package id
      Assert.assertEquals( lUpdateDetails.getWorkpackageId(), lUpdatedFault.getWorkpackageId() );
   }


   /**
    * Create a fault by using the fault API
    *
    * @param aStatusCode
    * @param aCredentials
    * @param aFault
    * @return
    */
   private static Response create( int aStatusCode, Credentials aCredentials, Object aFault ) {
      String lUserName = aCredentials.getUserName();
      String lPassword = aCredentials.getPassword();

      Response lResponse = RestAssured.given().auth().preemptive().basic( lUserName, lPassword )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).body( aFault ).expect()
            .statusCode( aStatusCode ).when().post( FAULT_API_PATH );

      return lResponse;
   }


   /**
    * Create a FaultV2 by using the fault API
    *
    * @param aStatusCode
    * @param aCredentials
    * @param aFault
    * @return
    */
   private static Response createFaultV2( int aStatusCode, Credentials aCredentials,
         Object aFault ) {
      String lUserName = aCredentials.getUserName();
      String lPassword = aCredentials.getPassword();

      Response lResponse = RestAssured.given().auth().preemptive().basic( lUserName, lPassword )
            .accept( APPLICATION_JSON_V2 ).contentType( APPLICATION_JSON_V2 ).body( aFault )
            .expect().statusCode( aStatusCode ).when().post( FAULT_API_PATH );

      return lResponse;
   }


   /**
    *
    * Updates a fault with the details in the given object searched by the provided task id
    *
    * @param aStatusCode
    * @param aId
    *           task Id
    * @param aFault
    * @param aSecurity
    * @return
    */
   private Response updateFault( int aStatusCode, String aId, Object aFault,
         Credentials aSecurity ) {
      String lUserName = aSecurity.getUserName();
      String lPassword = aSecurity.getPassword();

      Response lResponse = RestAssured.given().auth().preemptive().basic( lUserName, lPassword )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).body( aFault ).expect()
            .statusCode( aStatusCode ).when().put( FAULT_API_PATH + "/" + aId );

      return lResponse;
   }


   /**
    * Get a fault by ID using the fault API
    *
    * @param aStatusCode
    * @param aCredentials
    * @param aId
    * @return
    */
   private Response getById( int aStatusCode, Credentials aCredentials, String aId ) {

      String userName = aCredentials.getUserName();
      String password = aCredentials.getPassword();

      Response lResponse = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).expect()
            .statusCode( aStatusCode ).when().get( FAULT_API_PATH + "/" + aId );

      return lResponse;

   }


   /**
    * Get a faultV2 by ID using the fault API
    *
    * @param aStatusCode
    * @param aCredentials
    * @param aId
    * @return
    */
   private Response getFaultV2ById( int aStatusCode, Credentials aCredentials, String aId ) {

      String userName = aCredentials.getUserName();
      String password = aCredentials.getPassword();

      Response lResponse = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( APPLICATION_JSON_V2 ).expect().statusCode( aStatusCode ).when()
            .get( FAULT_API_PATH + "/" + aId );

      return lResponse;

   }


   private Response search( int statusCode, Credentials security ) {
      String username = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given()
            .queryParam( FaultSearchParameters.LOGBOOK_REFERENCE, iFault.getLogbookReference() )
            .queryParam( FaultSearchParameters.SEVERITY_CODE_PARAM, SEVERITY_TYPE_UNKNOWN )
            .queryParam( FaultSearchParameters.HISTORIC_PARAM, HISTORIC )
            .queryParam( FaultSearchParameters.INV_ID_PARAM, iInventoryId )
            .queryParam( FaultSearchParameters.FAULT_ID_PARAM, iFault.getFaultId() )
            .queryParam( FaultSearchParameters.STATUS_CODE_PARAM, STATUS_OPEN ).auth().preemptive()
            .basic( username, password ).accept( APPLICATION_JSON ).contentType( APPLICATION_JSON )
            .expect().statusCode( statusCode ).when().get( FAULT_API_PATH );

      return response;

   }


   private void assertFaultSearch( Response actual ) throws JsonProcessingException, IOException {
      boolean isContains = false;
      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      List<Fault> faultList = objectMapper.readValue( actual.getBody().asString(),
            objectMapper.getTypeFactory().constructCollectionType( List.class, Fault.class ) );

      isContains = faultList.contains( iFault );
      Assert.assertTrue( "The retrieved fault list doesn't contain the expected fault: " + iFault,
            isContains );
      Assert.assertEquals( "The retrieved fault list should only contain one fault.", 1,
            faultList.size() );

   }


   private static Fault defaultFault() throws ParseException, ClassNotFoundException, SQLException {

      Fault lFault = new Fault();
      lFault.setLogbookReference( generateLogbookReference() );
      lFault.setDescription( DESCRIPTION );
      lFault.setName( NAME );
      lFault.setSeverity( SEVERITY_UNKNOWN );

      Date lFoundDate;
      try {
         lFoundDate = new SimpleDateFormat( "yyyy-MM-dd" ).parse( FOUNDDATE );
      } catch ( ParseException e ) {
         throw ( e );
      }

      lFault.setFoundDate( lFoundDate );

      Result lInventoryResult =
            iDriver.select( "SELECT * FROM inv_inv WHERE serial_no_oem=?", SERIAL_NO_OEM1 );
      iInventoryId = lInventoryResult.get( 0 ).getUuidString( "alt_id" );
      lFault.setInventoryId( iInventoryId );

      lFault.setFailedSystemId( iInventoryId );

      Result lFlightLegResult =
            iDriver.select( "SELECT leg_id FROM fl_leg WHERE leg_no = ?", FLIGHT_LEG_NO );
      lFault.setFlightLegId( lFlightLegResult.get( 0 ).getUuidString( "leg_id" ) );

      lFault.setFaultSource( FAULT_SOURCE_PILOT );

      return lFault;
   }


   private static FaultV2 defaultFaultV2()
         throws ParseException, ClassNotFoundException, SQLException {

      FaultV2 lFaultV2 = new FaultV2();
      lFaultV2.setLogbookReference( generateLogbookReference() );
      lFaultV2.setDescription( DESCRIPTION );
      lFaultV2.setName( NAME );
      lFaultV2.setSeverityCode( SEVERITY_UNKNOWN );

      Date lFoundDate;
      try {
         lFoundDate = new SimpleDateFormat( "yyyy-MM-dd" ).parse( FOUNDDATE );
      } catch ( ParseException e ) {
         throw ( e );
      }

      lFaultV2.setFoundDate( lFoundDate );

      Result lInventoryResult =
            iDriver.select( "SELECT * FROM inv_inv WHERE serial_no_oem=?", SERIAL_NO_OEM1 );
      iInventoryId = lInventoryResult.get( 0 ).getUuidString( "alt_id" );
      lFaultV2.setInventoryId( iInventoryId );

      lFaultV2.setFailedSystemId( iInventoryId );

      lFaultV2.setFaultSourceCode( FAULT_SOURCE_PILOT );

      return lFaultV2;
   }


   private void assertFault( Fault aExpectedFault, Fault aActualFault )
         throws JsonParseException, JsonMappingException, IOException {

      Assert.assertEquals( "Incorrect Name for retrieved fault: ", aExpectedFault.getName(),
            aActualFault.getName() );
      Assert.assertEquals( "Incorrect Inventory ID for retrieved fault: ",
            aExpectedFault.getInventoryId(), aActualFault.getInventoryId() );
      Assert.assertEquals( "Incorrect Status for retrieved fault: ", STATUS_OPEN,
            aActualFault.getStatus() );
      Assert.assertEquals( "Incorrect Logbook Reference for retrieved fault: ",
            aExpectedFault.getLogbookReference(), aActualFault.getLogbookReference() );
      Assert.assertEquals( "Incorrect Failed System ID for retrieved fault: ",
            aExpectedFault.getInventoryId(), aActualFault.getFailedSystemId() );
      Assert.assertEquals( "Incorrect Description for retrieved fault: ",
            aExpectedFault.getDescription(), aActualFault.getDescription() );
      Assert.assertEquals( "Incorrect Severity for retrieved fault: ", aExpectedFault.getSeverity(),
            aActualFault.getSeverity() );
      Assert.assertEquals( "Incorrect Found Date for retrieved fault: ",
            aExpectedFault.getFoundDate(), aActualFault.getFoundDate() );
      Assert.assertEquals( "Incorrect Fault Source for retrieved fault: ",
            aExpectedFault.getFaultSource(), aActualFault.getFaultSource() );
      Assert.assertEquals( "Incorrect Scheduling Rules Start Usage for retrieved fault: ",
            SCHEDULING_RULES_START_USAGE, aActualFault.getSchedulingRulesStartUsage() );
      Assert.assertEquals( "Incorrect Estimated Duration for retrieved fault: ", ESTIMATED_DURATION,
            aActualFault.getEstimatedDuration() );
      Assert.assertEquals( "Incorrect Failed System for retrieved fault: ", FAILED_SYSTEM,
            aActualFault.getFailedSystem() );
      Assert.assertEquals( "Incorrect Historic value for retrieved fault: ", HISTORIC,
            aActualFault.isHistoric() );
      Assert.assertEquals( "Incorrect Severity Type for retrieved fault: ", SEVERITY_TYPE_UNKNOWN,
            aActualFault.getSeverityType() );
   }


   private void assertFaultV2( FaultV2 aExpectedFault, FaultV2 aActualFault )
         throws JsonParseException, JsonMappingException, IOException {

      Assert.assertEquals( aExpectedFault.getDescription(), aActualFault.getDescription() );
      Assert.assertEquals( ESTIMATED_DURATION2, aActualFault.getEstimatedDuration() );
      Assert.assertEquals( aExpectedFault.getFailedSystemId(), aActualFault.getFailedSystemId() );
      Assert.assertEquals( aExpectedFault.getFaultCode(), aActualFault.getFaultCode() );
      Assert.assertEquals( aExpectedFault.getFaultSourceCode(), aActualFault.getFaultSourceCode() );
      Assert.assertEquals( aExpectedFault.getFoundByUserId(), aActualFault.getFoundByUserId() );
      Assert.assertEquals( aExpectedFault.getFoundInTaskId(), aActualFault.getFoundInTaskId() );
      Assert.assertEquals( aExpectedFault.getFoundDate(), aActualFault.getFoundDate() );
      Assert.assertEquals( aExpectedFault.getId(), aActualFault.getId() );
      Assert.assertEquals( aExpectedFault.getInventoryId(), aActualFault.getInventoryId() );
      Assert.assertEquals( aExpectedFault.getName(), aActualFault.getName() );
      Assert.assertEquals( aExpectedFault.getLogbookReference(),
            aActualFault.getLogbookReference() );
      Assert.assertEquals( aExpectedFault.getLogbookTypeCode(), aActualFault.getLogbookTypeCode() );
      Assert.assertEquals( aExpectedFault.getPriorityCode(), aActualFault.getPriorityCode() );
      Assert.assertEquals( SEVERITY_TYPE_UNKNOWN, aActualFault.getSeverityCode() );
   }


   private Fault buildFaultWithUpdateWorkpackageDetails( String aFaultId ) {
      Result lWorkpackageResult = iDriver.select(
            "SELECT s.alt_id FROM sched_stask s, evt_event e WHERE s.sched_db_id = e.event_db_id AND s.sched_id = e.event_id AND e.event_sdesc=?",
            WORKPACKAGE_NAME );
      if ( lWorkpackageResult.getNumberOfRows() > 0 ) {
         workPackageId = lWorkpackageResult.get( 0 ).getUuidString( "alt_id" );
      } else {
         Assert.fail( "No work package was found for name " + WORKPACKAGE_NAME );
      }
      Fault lFault = new Fault();
      lFault.setFaultId( aFaultId );
      lFault.setWorkpackageId( workPackageId );
      return lFault;
   }


   private Fault buildNewFaultObject() {

      // Get highest inventory id
      String lHighestInvId = null;
      Result lHighestInvResult = iDriver.select(
            "SELECT alt_id FROM inv_inv WHERE serial_no_oem = ?", HIGHEST_INV_SERIAL_NO_OEM );
      if ( lHighestInvResult.getNumberOfRows() > 0 ) {
         lHighestInvId = lHighestInvResult.get( 0 ).getUuidString( "alt_id" );
      } else {
         Assert.fail( "Inventory not found for seriel number " + HIGHEST_INV_SERIAL_NO_OEM );
      }

      // Get flight leg id
      String lFlightLegId = null;
      Result lFlightLegResult =
            iDriver.select( "SELECT leg_id FROM fl_leg WHERE leg_no = ?", FLIGHT_LEG_NO );
      if ( lFlightLegResult.getNumberOfRows() > 0 ) {
         lFlightLegId = lFlightLegResult.get( 0 ).getUuidString( "leg_id" );
      } else {
         Assert.fail( "Flight leg not found for leg number " + FLIGHT_LEG_NO );
      }

      // Get user alt_id with username
      String lUserId = null;
      Result lUserIdResult = iDriver.select( "SELECT alt_id FROM utl_user WHERE username = ?",
            FOUND_BY_PERSON_USERNAME );
      if ( lUserIdResult.getNumberOfRows() > 0 ) {
         lUserId = lUserIdResult.get( 0 ).getUuidString( "alt_id" );
      } else {
         Assert.fail( "User not found for username " + FOUND_BY_PERSON_USERNAME );
      }

      // Build the object
      Fault lNewFault = new Fault();
      lNewFault.setLogbookReference( generateLogbookReference() );
      lNewFault.setDescription( DESCRIPTION );
      lNewFault.setFailedSystemId( lHighestInvId );
      lNewFault.setLogbookType( LOGBOOK_TYPE_CABIN );
      lNewFault.setInventoryId( lHighestInvId );
      lNewFault.setFailedSystemId( lHighestInvId );
      lNewFault.setFlightLegId( lFlightLegId );
      lNewFault.setFoundDuringFlightPhase( FLIGHT_PHASE_ICL );
      Date lFoundDate = new Date();
      lNewFault.setFoundDate( lFoundDate );
      lNewFault.setFoundByPerson( lUserId );
      lNewFault.setFaultSource( FAULT_SOURCE_PILOT );
      lNewFault.setFaultCode( FAULT_CODE1 );
      lNewFault.setName( NAME );
      lNewFault.setSeverity( SEVERITY_MINOR );
      return lNewFault;
   }


   private FaultV2 buildNewFaulV2Object() {

      // Get highest inventory id
      String lHighestInvId = null;
      Result lHighestInvResult = iDriver.select(
            "SELECT alt_id FROM inv_inv WHERE serial_no_oem = ?", HIGHEST_INV_SERIAL_NO_OEM );
      if ( lHighestInvResult.getNumberOfRows() > 0 ) {
         lHighestInvId = lHighestInvResult.get( 0 ).getUuidString( "alt_id" );
      } else {
         Assert.fail( "Inventory not found for seriel number " + HIGHEST_INV_SERIAL_NO_OEM );
      }

      // Get user alt_id with username
      String lUserId = null;
      Result lUserIdResult = iDriver.select( "SELECT alt_id FROM utl_user WHERE username = ?",
            FOUND_BY_PERSON_USERNAME );
      if ( lUserIdResult.getNumberOfRows() > 0 ) {
         lUserId = lUserIdResult.get( 0 ).getUuidString( "alt_id" );
      } else {
         Assert.fail( "User not found for username " + FOUND_BY_PERSON_USERNAME );
      }

      // Build the object
      FaultV2 lNewFault = new FaultV2();
      lNewFault.setLogbookReference( generateLogbookReference() );
      lNewFault.setDescription( DESCRIPTION );
      lNewFault.setFailedSystemId( lHighestInvId );
      lNewFault.setLogbookTypeCode( LOGBOOK_TYPE_CABIN );
      lNewFault.setInventoryId( lHighestInvId );
      lNewFault.setFailedSystemId( lHighestInvId );
      Date lFoundDate = new Date();
      lNewFault.setFoundDate( lFoundDate );
      lNewFault.setFoundByUserId( lUserId );
      lNewFault.setFaultSourceCode( FAULT_SOURCE_PILOT );
      lNewFault.setFaultCode( FAULT_CODE1 );
      lNewFault.setName( NAME );
      lNewFault.setSeverityCode( SEVERITY_MINOR );
      return lNewFault;
   }


   private Fault buildFaultWithUpdateDetails() {

      // Get highest inventory id
      String lHighestInvId = null;
      Result lHighestInvResult = iDriver.select(
            "SELECT alt_id FROM inv_inv WHERE serial_no_oem = ?", HIGHEST_INV_SERIAL_NO_OEM2 );
      if ( lHighestInvResult.getNumberOfRows() > 0 ) {
         lHighestInvId = lHighestInvResult.get( 0 ).getUuidString( "alt_id" );
      } else {
         Assert.fail( "Inventory not found for seriel number " + HIGHEST_INV_SERIAL_NO_OEM2 );
      }

      // Get flight leg id
      String lFlightLegId = null;
      Result lFlightLegResult =
            iDriver.select( "SELECT leg_id FROM fl_leg WHERE leg_no = ?", FLIGHT_LEG_NO2 );
      if ( lFlightLegResult.getNumberOfRows() > 0 ) {
         lFlightLegId = lFlightLegResult.get( 0 ).getUuidString( "leg_id" );
      } else {
         Assert.fail( "Flight leg not found for leg number " + FLIGHT_LEG_NO2 );
      }

      // Get user alt_id with username
      String lUserId = null;
      Result lUserIdResult = iDriver.select( "SELECT alt_id FROM utl_user WHERE username = ?",
            FOUND_BY_PERSON_USERNAME2 );
      if ( lUserIdResult.getNumberOfRows() > 0 ) {
         lUserId = lUserIdResult.get( 0 ).getUuidString( "alt_id" );
      } else {
         Assert.fail( "User not found for username " + FOUND_BY_PERSON_USERNAME2 );
      }

      String lWorkpackageId = null;
      Result lWorkpackageResult = iDriver.select(
            "SELECT s.alt_id FROM sched_stask s, evt_event e WHERE s.sched_db_id = e.event_db_id AND s.sched_id = e.event_id AND e.event_sdesc=?",
            WORKPACKAGE_NAME );
      if ( lWorkpackageResult.getNumberOfRows() > 0 ) {
         lWorkpackageId = lWorkpackageResult.get( 0 ).getUuidString( "alt_id" );
      } else {
         Assert.fail( "No work package was found for name " + WORKPACKAGE_NAME );
      }

      // Build the object
      Fault lNewFault = new Fault();
      lNewFault.setLogbookReference( generateLogbookReference() );
      lNewFault.setDescription( DESCRIPTION2 );
      lNewFault.setFailedSystemId( lHighestInvId );
      lNewFault.setLogbookType( LOGBOOK_TYPE_TECH );
      lNewFault.setInventoryId( lHighestInvId );
      lNewFault.setFailedSystemId( lHighestInvId );
      lNewFault.setFlightLegId( lFlightLegId );
      lNewFault.setFoundDuringFlightPhase( FLIGHT_PHASE_PRF );
      Date lFoundDate = new Date();
      lNewFault.setFoundDate( lFoundDate );
      lNewFault.setFoundByPerson( lUserId );
      lNewFault.setFaultSource( FAULT_SOURCE_CABIN );
      lNewFault.setFaultCode( FAULT_CODE2 );
      lNewFault.setName( NAME2 );
      lNewFault.setWorkpackageId( lWorkpackageId );

      return lNewFault;
   }


   /**
    * Generates a unique ID for the logbook reference id using the current date
    *
    * @return
    */
   private static String generateLogbookReference() {
      Date lDateNow = new Date();
      SimpleDateFormat lFormatter = new SimpleDateFormat( "yyMMddhhmmssMs" );
      String lLogbookRefId = lFormatter.format( lDateNow ) + Math.random();
      return lLogbookRefId;
   }


   private static Fault constructFaultFromResponse( String aBody )
         throws JsonParseException, JsonMappingException, IOException {
      ObjectMapper lObjectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      Fault lFault = lObjectMapper.readValue( aBody,
            lObjectMapper.getTypeFactory().constructType( Fault.class ) );
      return lFault;
   }


   private static FaultV2 constructFaultV2FromResponse( String aBody )
         throws JsonParseException, JsonMappingException, IOException {
      ObjectMapper lObjectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      FaultV2 lFaultV2 = lObjectMapper.readValue( aBody,
            lObjectMapper.getTypeFactory().constructType( FaultV2.class ) );
      return lFaultV2;
   }


   private void assertCreatedFault( Fault aExpectedFault, Fault aCreatedFault ) {
      // Validate all the values inserted
      Assert.assertEquals( aExpectedFault.getLogbookReference(),
            aCreatedFault.getLogbookReference() );
      Assert.assertEquals( aExpectedFault.getDescription(), aCreatedFault.getDescription() );
      Assert.assertEquals( aExpectedFault.getFailedSystemId(), aCreatedFault.getFailedSystemId() );
      Assert.assertEquals( aExpectedFault.getLogbookType(), aCreatedFault.getLogbookType() );
      Assert.assertEquals( aExpectedFault.getInventoryId(), aCreatedFault.getInventoryId() );
      Assert.assertEquals( aExpectedFault.getFailedSystemId(), aCreatedFault.getFailedSystemId() );
      Assert.assertEquals( aExpectedFault.getFlightLegId(), aCreatedFault.getFlightLegId() );
      Assert.assertEquals( aExpectedFault.getFoundDuringFlightPhase(),
            aCreatedFault.getFoundDuringFlightPhase() );
      Assert.assertEquals( aExpectedFault.getFoundDate().toString(),
            aCreatedFault.getFoundDate().toString() );
      Assert.assertEquals( aExpectedFault.getFoundByPerson(), aCreatedFault.getFoundByPerson() );
      Assert.assertEquals( aExpectedFault.getFaultSource(), aCreatedFault.getFaultSource() );
      Assert.assertEquals( aExpectedFault.getFaultCode(), aCreatedFault.getFaultCode() );
      Assert.assertEquals( aExpectedFault.getName(), aCreatedFault.getName() );
      Assert.assertEquals( aExpectedFault.getSeverity(), aCreatedFault.getSeverity() );
   }


   private void assertCreatedFaultV2( FaultV2 aExpectedFault, FaultV2 aCreatedFault ) {
      // Validate all the values inserted
      Assert.assertEquals( aExpectedFault.getLogbookReference(),
            aCreatedFault.getLogbookReference() );
      Assert.assertEquals( aExpectedFault.getDescription(), aCreatedFault.getDescription() );
      Assert.assertEquals( aExpectedFault.getFailedSystemId(), aCreatedFault.getFailedSystemId() );
      Assert.assertEquals( aExpectedFault.getLogbookTypeCode(),
            aCreatedFault.getLogbookTypeCode() );
      Assert.assertEquals( aExpectedFault.getInventoryId(), aCreatedFault.getInventoryId() );
      Assert.assertEquals( aExpectedFault.getFailedSystemId(), aCreatedFault.getFailedSystemId() );
      Assert.assertEquals( aExpectedFault.getFoundDate().toString(),
            aCreatedFault.getFoundDate().toString() );
      Assert.assertEquals( aExpectedFault.getFoundByUserId(), aCreatedFault.getFoundByUserId() );
      Assert.assertEquals( aExpectedFault.getFaultSourceCode(),
            aCreatedFault.getFaultSourceCode() );
      Assert.assertEquals( aExpectedFault.getFaultCode(), aCreatedFault.getFaultCode() );
      Assert.assertEquals( aExpectedFault.getName(), aCreatedFault.getName() );
      Assert.assertEquals( aExpectedFault.getSeverityCode(), aCreatedFault.getSeverityCode() );
   }


   private void assertUpdatedFault( Fault lUpdateDetails, Fault lUpdatedFault ) {
      // Validate the updated details
      Assert.assertEquals( lUpdateDetails.getLogbookReference(),
            lUpdatedFault.getLogbookReference() );
      Assert.assertEquals( lUpdateDetails.getDescription(), lUpdatedFault.getDescription() );
      Assert.assertEquals( lUpdateDetails.getFailedSystemId(), lUpdatedFault.getFailedSystemId() );
      Assert.assertEquals( lUpdateDetails.getLogbookType(), lUpdatedFault.getLogbookType() );
      Assert.assertEquals( lUpdateDetails.getInventoryId(), lUpdatedFault.getInventoryId() );
      Assert.assertEquals( lUpdateDetails.getFailedSystemId(), lUpdatedFault.getFailedSystemId() );
      Assert.assertEquals( lUpdateDetails.getFlightLegId(), lUpdatedFault.getFlightLegId() );
      Assert.assertEquals( lUpdateDetails.getFoundDuringFlightPhase(),
            lUpdatedFault.getFoundDuringFlightPhase() );
      Assert.assertEquals( lUpdateDetails.getFoundDate().toString(),
            lUpdatedFault.getFoundDate().toString() );
      Assert.assertEquals( lUpdateDetails.getFoundByPerson(), lUpdatedFault.getFoundByPerson() );
      Assert.assertEquals( lUpdateDetails.getFaultSource(), lUpdatedFault.getFaultSource() );
      Assert.assertEquals( lUpdateDetails.getFaultCode(), lUpdatedFault.getFaultCode() );
      Assert.assertEquals( lUpdateDetails.getWorkpackageId(), lUpdatedFault.getWorkpackageId() );
   }

}
