package com.mxi.am.api.resource.maintenance.exec.maintenancereleasestatus;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;
import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.driver.common.database.Result;
import com.mxi.am.guice.AssetManagementDatabaseDriverProvider;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * MaintenanceReleaseStatus API test
 *
 */
public class MaintenanceReleaseStatusResourceBeanTest {

   private final String iApplicationJson = "application/json";
   private final String iMaintenanceReleaseStatusPath = "/amapi/" + MaintenanceReleaseStatus.PATH;

   private String iHrCd = "1000090";
   private String iAircraftId;
   private String iWPName = "Maintenance Release Status WKP";
   private boolean iReleaseToService = true;
   private boolean iEtops = false;
   private String iSignOffUserId;
   private String iSignOffDt = "2018-05-04T12:02:44Z";
   private CertificateReleaseService iCertificateReleaseService;

   private final String iGetInvAltIdQuery = "SELECT inv_inv.alt_id FROM sched_stask "
         + "INNER JOIN evt_event ON sched_stask.sched_db_id = evt_event.event_db_id AND sched_stask.sched_id = evt_event.event_id "
         + "INNER JOIN inv_inv ON inv_inv.inv_no_db_id = sched_stask.main_inv_no_db_id AND inv_inv.inv_no_id = sched_stask.main_inv_no_id "
         + "WHERE evt_event.event_sdesc = ?";

   private final String iGetUserAltIdQuery = " SELECT utl_user.alt_id FROM org_hr "
         + " INNER JOIN utl_user ON utl_user.user_id = org_hr.user_id WHERE hr_cd=?";


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
   public void setUpData() throws Exception {
      DatabaseDriver lDriver = new AssetManagementDatabaseDriverProvider().get();

      Result lInvAltIdResult = lDriver.select( iGetInvAltIdQuery, iWPName );
      Result lUserAltIdResult = lDriver.select( iGetUserAltIdQuery, iHrCd );

      if ( lInvAltIdResult.isEmpty() ) {
         Assert.fail( "Could not find the Inventory with Work Package Name: " + iWPName );
      } else {
         iAircraftId = lInvAltIdResult.get( 0 ).getUuidString( "alt_id" );
      }

      if ( lUserAltIdResult.isEmpty() ) {
         Assert.fail( "Could not find the User with Hr Code: " + iHrCd );
      } else {
         iSignOffUserId = lUserAltIdResult.get( 0 ).getUuidString( "alt_id" );
      }

   }


   @Test
   public void testGetByIdMaintenanceReleaseStatusSuccessReturns200()
         throws JsonProcessingException, IOException, ParseException {

      Response lResponse = createGetById( 200, Credentials.AUTHENTICATED, iAircraftId,
            iApplicationJson, iApplicationJson );
      assertMaintenanceReleaseStatusForGet( defaultMaintenanceReleaseStatusBuilder(), lResponse );
   }


   @Test
   public void testGetByIdMaintenanceReleaseStatusUnauthenticatedReturns401()
         throws JsonProcessingException, IOException {

      Response lResponse = createGetById( 401, Credentials.UNAUTHENTICATED, iAircraftId,
            iApplicationJson, iApplicationJson );
      Assert.assertEquals( 401, lResponse.getStatusCode() );
   }


   @Test
   public void testGetByIdMaintenanceReleaseStatusUnauthorizedReturns403() {

      createGetById( 403, Credentials.UNAUTHORIZED, iAircraftId, iApplicationJson,
            iApplicationJson );
   }


   private Response createGetById( int aStatusCode, Credentials aSecurity, String aAircraftId,
         String aContentType, String aAcceptType ) {
      String lUserName = aSecurity.getUserName();
      String lPassword = aSecurity.getPassword();

      Response lResponse = RestAssured.given().pathParam( "id", aAircraftId ).auth().preemptive()
            .basic( lUserName, lPassword ).accept( aAcceptType ).contentType( aContentType )
            .expect().statusCode( aStatusCode ).when()
            .get( iMaintenanceReleaseStatusPath + "/{id}" );
      return lResponse;
   }


   private void assertMaintenanceReleaseStatusForGet(
         MaintenanceReleaseStatus aMaintenanceReleaseStatusExpected, Response aActual )
         throws JsonProcessingException, IOException {
      ObjectMapper lObjectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      MaintenanceReleaseStatus lMaintenanceReleaseStatusActual =
            lObjectMapper.readValue( aActual.getBody().asString(),
                  lObjectMapper.getTypeFactory().constructType( MaintenanceReleaseStatus.class ) );
      lMaintenanceReleaseStatusActual.setLastModifiedDate( null );// we are not checking
                                                                  // LastModifiedDate in the e2e
                                                                  // testing
      Assert.assertEquals( aMaintenanceReleaseStatusExpected, lMaintenanceReleaseStatusActual );

   }


   private MaintenanceReleaseStatus defaultMaintenanceReleaseStatusBuilder() throws ParseException {
      ISO8601DateFormat lDateFromat = new ISO8601DateFormat();
      MaintenanceReleaseStatus lMaintenanceReleaseStatus = new MaintenanceReleaseStatus();
      lMaintenanceReleaseStatus.setAircraftId( iAircraftId );
      lMaintenanceReleaseStatus.setReleaseToService( iReleaseToService );
      iCertificateReleaseService = new CertificateReleaseService();
      iCertificateReleaseService.setEtops( iEtops );
      iCertificateReleaseService.setSignOffUserId( iSignOffUserId );
      iCertificateReleaseService.setSignOffDate( lDateFromat.parse( iSignOffDt ) );
      lMaintenanceReleaseStatus.setCertificateReleaseService( iCertificateReleaseService );
      return lMaintenanceReleaseStatus;
   }
}
