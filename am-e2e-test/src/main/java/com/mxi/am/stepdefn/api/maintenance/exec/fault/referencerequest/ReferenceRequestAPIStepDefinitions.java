package com.mxi.am.stepdefn.api.maintenance.exec.fault.referencerequest;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.UUID;

import javax.json.JsonObject;

import org.junit.Assert;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;
import com.mxi.am.api.resource.maintenance.exec.fault.Fault;
import com.mxi.am.api.resource.maintenance.exec.fault.FaultPathBuilder;
import com.mxi.am.api.resource.maintenance.exec.fault.FaultSearchParameters;
import com.mxi.am.api.resource.maintenance.exec.fault.referencerequest.ReferenceRequest;
import com.mxi.am.api.resource.maintenance.exec.fault.referencerequest.ReferenceRequestSearchParameters;
import com.mxi.am.driver.integrationtesting.Rest;
import com.mxi.am.driver.integrationtesting.RestDriver;
import com.mxi.am.driver.query.AircraftQueriesDriver;
import com.mxi.am.driver.query.AircraftQueriesDriver.AircraftInfo;
import com.mxi.am.driver.query.FaultQueriesDriver;
import com.mxi.am.driver.query.FaultQueriesDriver.FaultInfo;
import com.mxi.am.driver.query.InventoryQueriesDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.lmoc.pendingdeferralrequests.PendingReferenceApprovalsPageDriver;
import com.mxi.am.driver.web.lmoc.requestdeferral.SelectReferencePageDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPageDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * Step definitions for searching reference request API.
 *
 */
public class ReferenceRequestAPIStepDefinitions {

   private static final String REFERENCE_REQUEST_API_PATH = "/amapi/" + ReferenceRequest.PATH;

   public static final String AC_REG_CD = "Aircraft Part MOC 1 - RTD-1";
   public static final String AIRCRAFT_ASSEMBLY_CD = "ACFTMOC1";
   public static final String AIRCRAFT_PART_NO = "ACFT_ASSY_MOC1";
   public static final String AIRCRAFT_SERIAL_NO = "RTD-1";

   public static final String FAULT_NAME = UUID.randomUUID().toString();
   public static final String FAILED_SYSTEM_NAME = "SYS-1 - Aircraft System 1";
   public static final String FAULT_DESC = UUID.randomUUID().toString();
   public static final String FAULT_SOURCE = "MECH";
   public static final String FAULT_SOURCE_DESC = "Mechanic";
   public static final String FAULT_STATUS = "OPEN (Active)";
   public static final String FAULT_STATUS_CD = "CFACTV";
   public static final String USERNAME = "mxi";

   public static final String REFERENCE_FILTER_TEXT = "RTD";
   public static final String REFERENCE_NAME = "RTD-DEFER-1-DEF";

   public static final String APPROVAL_STATUS_CD = "APPROVED";

   private static final String APPLICATION_JSON = "application/json";
   private static final String FAULT_API_PATH = "/amapi/" + FaultPathBuilder.PATH_BASE;
   private static final String DEFERRAL_REFERENCE_TYPE = "DEFERRAL";

   public static JsonObject iJsonResponse;

   @Inject
   @Rest
   private RestDriver restDriver;
   @Inject
   private NavigationDriver navigationDriver;
   @Inject
   private SelectReferencePageDriver selectReferencePageDriver;
   @Inject
   private TaskDetailsPageDriver taskDetailsPageDriver;
   @Inject
   private FaultQueriesDriver faultQueriesDriver;
   @Inject
   private AircraftQueriesDriver aircraftQueriesDriver;
   @Inject
   private InventoryQueriesDriver inventoryQueriesDriver;
   @Inject
   private PendingReferenceApprovalsPageDriver pendingReferenceApprovalsPageDriver;

   private static FaultInfo faultInfo = null;
   private static String faultId = null;
   private static Response response;


   @Given( "^there is a reference request" )
   public void thereIsAReferenceRequest()
         throws JsonParseException, JsonMappingException, IOException {
      faultInfo = setUpFault();
      // navigate to the fault details page
      navigationDriver.barcodeSearch( faultInfo.getCorrectiveTaskBarcode() );
      // click the Select Reference Button
      taskDetailsPageDriver.clickSelectReference();
      // fill in the Select Reference page
      selectReferencePageDriver.selectReference( REFERENCE_FILTER_TEXT, REFERENCE_NAME );
      selectReferencePageDriver.clickOk();

      navigationDriver.navigateOther( "Maintenance Controller", "Pending Reference Approvals" );

      pendingReferenceApprovalsPageDriver.findRequest( faultInfo.getCorrectiveTaskBarcode() );
      pendingReferenceApprovalsPageDriver.clickAuthorizeButton();
      pendingReferenceApprovalsPageDriver.confirmDialogAction( "Approve Reference" );

      try {
         RestAssured.reset();
         RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
         RestAssured.baseURI = "http://".concat( InetAddress.getLocalHost().getHostName() );
         RestAssured.port = 80;

      } catch ( UnknownHostException e ) {
         throw new RuntimeException( "Cannot find local host name." );
      }

      faultId = getFaultId();
   }


   @When( "^search reference request by status code and fault id" )
   public void searchReferenceRequestByStatusCdSuccess() {
      response = search( 200, Credentials.AUTHORIZED, faultId, APPROVAL_STATUS_CD );
   }


   @When( "^search reference request without status code and fault id" )
   public void searchReferenceRequestWithoutStatusCdFailure() {
      response = search( 412, Credentials.AUTHORIZED, null, null );
   }


   @When( "^search reference request with unauthorized credentials" )
   public void searchReferenceRequestWithUnauthorizedFailure() {
      response = search( 403, Credentials.UNAUTHORIZED, faultId, APPROVAL_STATUS_CD );
   }


   @Then( "^the reference request list is returned" )
   public void assertReferenceRequests()
         throws JsonParseException, JsonMappingException, IOException {
      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      List<ReferenceRequest> referenceRequestList =
            objectMapper.readValue( response.getBody().asString(), objectMapper.getTypeFactory()
                  .constructCollectionType( List.class, ReferenceRequest.class ) );

      Assert.assertEquals( "Incorrect number of reference requests returned: ", 1,
            referenceRequestList.size() );
      assertReferenceRequest( referenceRequestList.get( 0 ) );
   }


   private void assertReferenceRequest( ReferenceRequest referenceRequest ) {
      Assert.assertEquals( "Incorrect Status Code: ", APPROVAL_STATUS_CD,
            referenceRequest.getStatusCode() );
      Assert.assertEquals( "Incorrect Fault ID: ", faultId, referenceRequest.getFaultId() );
      Assert.assertEquals( "Incorrect Reference Type: ", DEFERRAL_REFERENCE_TYPE,
            referenceRequest.getReferenceType() );
      Assert.assertEquals( "Incorrect Approved User: ", USERNAME,
            referenceRequest.getApproverUser() );
      Assert.assertEquals( "Incorrect Revision User: ", USERNAME,
            referenceRequest.getRevisionUser() );
   }


   private Response search( int statusCode, Credentials security, String faultId,
         String statusCd ) {
      String username = security.getUserName();
      String password = security.getPassword();

      io.restassured.response.Response response = RestAssured.given()
            .queryParam( ReferenceRequestSearchParameters.FAULT_ID_PARAM, faultId )
            .queryParam( ReferenceRequestSearchParameters.STATUS_CODE_PARAM, statusCd ).auth()
            .preemptive().basic( username, password ).accept( APPLICATION_JSON )
            .contentType( APPLICATION_JSON ).expect().statusCode( statusCode ).when()
            .get( REFERENCE_REQUEST_API_PATH );

      return response;
   }


   private FaultInfo setUpFault() {
      AircraftInfo acInvNoId = aircraftQueriesDriver
            .getAircraftInfoByPartAndSerialNo( AIRCRAFT_PART_NO, AIRCRAFT_SERIAL_NO );

      BigDecimal failedSysInvNoId = inventoryQueriesDriver
            .getInvInfoByAircraftInvNoAndInvName( acInvNoId.getInvNoId(), FAILED_SYSTEM_NAME )
            .getId();

      FaultInfo faultInfo = faultQueriesDriver.insertFault( FAULT_NAME, FAULT_DESC, FAULT_SOURCE,
            acInvNoId.getInvNoId(), USERNAME, FAULT_STATUS_CD, AIRCRAFT_ASSEMBLY_CD,
            failedSysInvNoId );

      return faultInfo;
   }


   private String getFaultId() throws JsonParseException, JsonMappingException, IOException {
      String faultId = null;

      io.restassured.response.Response response = RestAssured.given()
            .queryParam( FaultSearchParameters.BARCODE_PARAM, faultInfo.getCorrectiveTaskBarcode() )
            .auth().preemptive()
            .basic( Credentials.AUTHORIZED.getUserName(), Credentials.AUTHORIZED.getPassword() )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).expect().statusCode( 200 )
            .when().get( FAULT_API_PATH );

      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      List<Fault> faultList = objectMapper.readValue( response.getBody().asString(),
            objectMapper.getTypeFactory().constructCollectionType( List.class, Fault.class ) );

      if ( faultList != null && faultList.size() == 1 ) {
         faultId = faultList.get( 0 ).getFaultId();
      }

      return faultId;
   }
}
