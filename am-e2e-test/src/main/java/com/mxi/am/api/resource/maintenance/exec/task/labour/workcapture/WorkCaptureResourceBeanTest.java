package com.mxi.am.api.resource.maintenance.exec.task.labour.workcapture;

import static com.mxi.am.helper.api.GenericAmApiCalls.get;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;

import javax.ws.rs.core.MediaType;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;
import com.mxi.am.api.resource.maintenance.exec.task.labour.Labour;
import com.mxi.am.api.resource.maintenance.exec.task.labour.LabourRole;
import com.mxi.am.helper.api.WorkCaptureAPITestHelper;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * e2e test class for the Work Capture API.
 *
 */
public class WorkCaptureResourceBeanTest {

   private static WorkCaptureAPITestHelper workCaptureAPITestHelper =
         new WorkCaptureAPITestHelper();

   private static ObjectMapper objectMapper =
         ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();


   @BeforeClass
   public static void setUpClass() throws Exception {
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
      workCaptureAPITestHelper.setUpWorkPackageAndTask( true );
   }


   @Test
   public void createWorkCapture_200_mandatoryFields() throws Exception {
      WorkCapture workCapture = workCaptureAPITestHelper.buildWorkCapture();
      workCaptureAPITestHelper.createWorkCapture( 200, Credentials.AUTHORIZED, workCapture );
      assertWorkCapture( workCapture.getLabourId() );

   }


   @Test
   public void createWorkCapture_403_unauthorized() throws ParseException {
      WorkCapture workCapture = workCaptureAPITestHelper.buildWorkCapture();
      workCaptureAPITestHelper.createWorkCapture( 403, Credentials.UNAUTHORIZED, workCapture );
   }


   @Test
   public void generateReport_403_unauthorized() throws ParseException {
      WorkCapture workCapture = workCaptureAPITestHelper.buildWorkCapture();
      workCaptureAPITestHelper.generateReport( 403, Credentials.UNAUTHORIZED, workCapture );
   }


   private void assertWorkCapture( String labourId )
         throws JsonParseException, JsonMappingException, IOException {

      Response labourResponse = get( 200, Credentials.AUTHENTICATED, Labour.PATH, labourId,
            MediaType.APPLICATION_JSON );
      Labour labour = objectMapper.readValue( labourResponse.getBody().asString(), Labour.class );
      LabourRole labourRole = workCaptureAPITestHelper.getLabourRole( labour, "TECH" );

      // Check that the labour has been completed through the work capture API
      Assert.assertEquals(
            "Labour row with labour ID [" + labourId + "] does not have status COMPLETE.",
            "COMPLETE", labour.getLabourStageCode() );
      Assert.assertEquals(
            "Labour row with labour ID [" + labourId + "] doesn't have correct technician. ", "mxi",
            labourRole.getUsername() );
   }

}
