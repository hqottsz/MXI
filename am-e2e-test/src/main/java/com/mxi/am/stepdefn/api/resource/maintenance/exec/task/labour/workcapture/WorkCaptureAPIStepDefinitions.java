package com.mxi.am.stepdefn.api.resource.maintenance.exec.task.labour.workcapture;

import static com.mxi.am.helper.api.GenericAmApiCalls.create;
import static com.mxi.am.helper.api.GenericAmApiCalls.get;
import static com.mxi.am.helper.api.GenericAmApiCalls.search;
import static com.mxi.am.helper.api.GenericAmApiCalls.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.inject.Inject;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;
import com.mxi.am.api.resource.erp.location.Location;
import com.mxi.am.api.resource.erp.location.LocationSearchParameters;
import com.mxi.am.api.resource.maintenance.eng.config.assembly.flightmeasurement.FlightMeasurementRequirement;
import com.mxi.am.api.resource.maintenance.eng.config.assembly.flightmeasurement.Measurement;
import com.mxi.am.api.resource.maintenance.exec.task.Task;
import com.mxi.am.api.resource.maintenance.exec.task.TaskSearchParameters;
import com.mxi.am.api.resource.maintenance.exec.task.labour.Labour;
import com.mxi.am.api.resource.maintenance.exec.task.labour.LabourRole;
import com.mxi.am.api.resource.maintenance.exec.task.labour.workcapture.InstalledPart;
import com.mxi.am.api.resource.maintenance.exec.task.labour.workcapture.JobStop;
import com.mxi.am.api.resource.maintenance.exec.task.labour.workcapture.Measurements;
import com.mxi.am.api.resource.maintenance.exec.task.labour.workcapture.Notes;
import com.mxi.am.api.resource.maintenance.exec.task.labour.workcapture.Parts;
import com.mxi.am.api.resource.maintenance.exec.task.labour.workcapture.RemovedPart;
import com.mxi.am.api.resource.maintenance.exec.task.labour.workcapture.Signature;
import com.mxi.am.api.resource.maintenance.exec.task.labour.workcapture.Steps;
import com.mxi.am.api.resource.maintenance.exec.task.labour.workcapture.Tools;
import com.mxi.am.api.resource.maintenance.exec.task.labour.workcapture.WorkCapture;
import com.mxi.am.api.resource.materials.asset.inventory.Inventory;
import com.mxi.am.api.resource.materials.asset.inventory.InventorySearchParameters;
import com.mxi.am.api.resource.materials.proc.owner.Owner;
import com.mxi.am.api.resource.materials.proc.owner.OwnerSearchParameters;
import com.mxi.am.driver.query.BlobQueriesDriver;
import com.mxi.am.driver.query.BlobQueriesDriver.BlobInfo;
import com.mxi.am.driver.query.GlobalParametersQueriesDriver;
import com.mxi.am.driver.query.HumanResourceQueriesDriver;
import com.mxi.am.driver.query.SchedLabourQueriesDriver;
import com.mxi.am.driver.query.SchedLabourQueriesDriver.SchedLabourEsigInfo;
import com.mxi.am.driver.web.AuthenticationRequiredPageDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.esigner.GenerateCertificatePageDriver;
import com.mxi.am.driver.web.part.PartRequestDetailsPageDriver;
import com.mxi.am.driver.web.part.addpartrequirementpage.AddPartRequirementPageDriver;
import com.mxi.am.driver.web.part.addpartrequirementpage.AddPartRequirementPageDriver.PartsFound;
import com.mxi.am.driver.web.req.IssueInventoryPageDriver;
import com.mxi.am.driver.web.req.ReserveLocalInventoryPageDriver;
import com.mxi.am.driver.web.task.AddMeasurementPageDriver;
import com.mxi.am.driver.web.task.AddMeasurementPageDriver.MeasurementResult;
import com.mxi.am.driver.web.task.AddStepPageDriver;
import com.mxi.am.driver.web.task.AddToolRequirementPageDriver;
import com.mxi.am.driver.web.task.AddToolRequirementPageDriver.ToolFound;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPageDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPanes.TaskExecutionPaneDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPanes.TaskExecutionPaneDriver.Labor;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPanes.TaskExecutionPaneDriver.PartRequests;
import com.mxi.am.driver.web.task.labor.EditLabourRequirementsPageDriver;
import com.mxi.am.driver.web.task.labor.EditLabourRequirementsPageDriver.LabourRequirement;
import com.mxi.am.driver.web.user.userdetailspage.UserDetailsPageDriver;
import com.mxi.am.driver.web.user.userdetailspage.userdetailspanes.UserDetailsElectronicSignaturePaneDriver;
import com.mxi.am.helper.api.WorkCaptureAPITestHelper;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * Work Capture API Step Definitions
 *
 */
public class WorkCaptureAPIStepDefinitions {

   private static final String IN_WORK_STATUS = "IN WORK";
   private static final String ACTV_STATUS = "ACTV";
   private static final String COMPLETE_STATUS = "COMPLETE";
   private static final String PENDING_STATUS = "PENDING";
   private static final String PAUSE_STATUS = "PAUSE";
   private static final String MXCOMPLETE_STATUS = "MXCOMPLETE";

   private static final String USERNAME = "mxi";
   private static final String PASS = "password";
   private static final String ENCRYPTED_PASSWORD = "cGFzc3dvcmQ=";
   private static final String ESIG_PARM = "ENABLE_JC_ESIG";

   private static final String LBR_SKILL = "LBR";
   private static final String TECH_LABOUR_ROLE = "TECH";
   private static final String CERT_LABOUR_ROLE = "CERT";
   private static final String INSP_LABOUR_ROLE = "INSP";

   private static final String MEASUREMENT_DATE = "04-DEC-2018";
   private static final String WC_MEASUREMENT_DATE = "2019-10-17T04:56:00Z";
   private static final String QTS_MEASUREMENT_CD = "QTS";
   private static final String DAY_MEASUREMENT_CD = "DAY";

   private static final String SER_TOOL_INV_SERIAL_NO = "SERWCTOOL";
   private static final String TRK_TOOL_INV_SERIAL_NO = "TRKWCTOOL";

   private static final String PART_NO = "A0000001";
   private static final String PART_MANUFACT_CD = "00001";

   private static final String REQ_ACTION_CD = "REQ";
   private static final String LOAN_REASON_CD = "LOAN";

   private static final String INV_LOCATION_CD = "AIRPORT1/LINE";
   private static final String INV_OWNER_CD = "MXI";
   private static final String INV_TYPE = "TRK";
   private static final String INV_CONDITION_CD = "RFI";

   private static final String SHIFT_CHG_REASON_CD = "TPSHFT";

   private static final String SERIAL_NO_FORMAT = "%08d";

   // A blank Work Capture report PDF will have a length of about 907 bytes since it contains
   // information even if not shown.
   private static final long EMPTY_PDF_LENGTH = 1000;

   private static final String PDF_CONTENT_TYPE = "application/pdf";

   private static Response response;

   private static Task task;
   private static WorkCapture workCapture;
   private static File workCapturePdf;
   private static int expectedStatus;

   private static List<Measurements> measurements;
   private static List<Tools> tools;
   private static Notes notes;
   private static List<Steps> steps;
   private static List<Parts> parts;
   private static JobStop jobStop;

   private static WorkCaptureAPITestHelper workCaptureAPITestHelper =
         new WorkCaptureAPITestHelper();

   private static ObjectMapper objectMapper =
         ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();

   @Inject
   private NavigationDriver navigationDriver;

   @Inject
   private UserDetailsPageDriver userDetailsPageDriver;

   @Inject
   private AuthenticationRequiredPageDriver authenticationRequiredPageDriver;

   @Inject
   private GenerateCertificatePageDriver generateCertificatePageDriver;

   @Inject
   private TaskDetailsPageDriver taskDetailsPageDriver;

   private TaskExecutionPaneDriver taskExecutionPaneDriver;

   @Inject
   private AddMeasurementPageDriver addMeasurementPageDriver;

   @Inject
   private AddStepPageDriver addStepPageDriver;

   @Inject
   private AddToolRequirementPageDriver addToolRequirementPageDriver;

   @Inject
   private AddPartRequirementPageDriver addPartRequirementPageDriver;

   @Inject
   private EditLabourRequirementsPageDriver editLabourRequirementsPageDriver;

   @Inject
   private PartRequestDetailsPageDriver partRequestDetailsPageDriver;

   @Inject
   private ReserveLocalInventoryPageDriver reserveLocalInventoryPageDriver;

   @Inject
   private IssueInventoryPageDriver issueInventoryPageDriver;

   @Inject
   private HumanResourceQueriesDriver humanResourceQueriesDriver;

   @Inject
   private GlobalParametersQueriesDriver globalParametersQueriesDriver;

   @Inject
   private SchedLabourQueriesDriver schedLabourQueriesDriver;

   @Inject
   private BlobQueriesDriver blobQueriesDriver;

   private UserDetailsElectronicSignaturePaneDriver userDetailsElectronicSignaturePaneDriver;


   @Given( "^I have an electronic signature certificate" )
   public void iHaveAnESignatureCertificate() {

      globalParametersQueriesDriver.setGlobalParameterValue( ESIG_PARM, "true" );

      Map<String, String> certificates = humanResourceQueriesDriver.getCertificates( USERNAME );
      boolean hasESignature = false;

      for ( Map.Entry<String, String> certificate : certificates.entrySet() ) {
         if ( certificate.getKey().equals( "SERVER" ) && certificate.getValue().equals( "1" ) ) {
            hasESignature = true;
            break;
         }
      }

      if ( !hasESignature ) {
         navigationDriver.navigateOther( "References", "My User Details" );
         userDetailsElectronicSignaturePaneDriver = userDetailsPageDriver.clickTabESignature();
         userDetailsElectronicSignaturePaneDriver.clickGenerateCertificate();

         authenticationRequiredPageDriver.setPasswordAndClickOK( PASS );

         generateCertificatePageDriver.setPassword( PASS );
         generateCertificatePageDriver.setVerifyPassword( PASS );
         generateCertificatePageDriver.clickOk();
      }

      try {
         RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
         RestAssured.baseURI = "http://".concat( InetAddress.getLocalHost().getHostName() );

         RestAssured.port = 80;

      } catch ( UnknownHostException e ) {
         throw new RuntimeException( "Cannot find local host name." );
      }
   }


   @Given( "^there is a started work package with an adhoc task" )
   public void thereIsAWorkPackageInProgressWithAnAdhocTask()
         throws JsonParseException, JsonMappingException, IOException, ParseException {

      workCaptureAPITestHelper.setUpWorkPackageAndTask( true );
      task = workCaptureAPITestHelper.getTask();

      // Add the necessary information to the task
      navigationDriver.barcodeSearch( task.getBarcode() );

      taskExecutionPaneDriver = taskDetailsPageDriver.clickTabTaskExecution();

      // Start the work
      Labor labor = taskExecutionPaneDriver.getLaborRow( LBR_SKILL );
      labor.clickStart();

      // Build the object for the work capture
      // TODO uncomment in OPER-31298
      // measurements = buildMeasurements();
      tools = buildTools();
      notes = buildNotes();
      steps = buildSteps();
      parts = buildPart();
      jobStop = buildJobStop();

      expectedStatus = 200;

   }


   @Given( "^there is an active work package with an adhoc task" )
   public void thereIsAWorkPackageNotInProgressWithAnAdhocTask()
         throws JsonParseException, JsonMappingException, IOException, ParseException {

      workCaptureAPITestHelper.setUpWorkPackageAndTask( false );
      task = workCaptureAPITestHelper.getTask();
      expectedStatus = 400;

   }


   @When( "^I call the work capture API to generate the report" )
   public void createWorkCaptureWithoutCertificatePasswordAndEsigIsRequiredSuccess()
         throws ParseException, JsonParseException, JsonMappingException, MessagingException,
         IOException {
      workCapture = workCaptureAPITestHelper.buildWorkCapture();
      workCapture.setAction( "Test Action" );
      workCapture.setTimeTracking( "REG" );
      workCapture.setMeasurements( measurements );
      workCapture.setTools( tools );
      workCapture.setNotes( notes );
      workCapture.setSteps( steps );
      workCapture.setParts( parts );
      workCapture.setJobStop( jobStop );

      response = workCaptureAPITestHelper.generateReport( expectedStatus, Credentials.AUTHORIZED,
            workCapture );

      if ( expectedStatus == 200 ) {
         workCapture = getWorkCaptureFromResponse( response );
      }
   }


   @When( "^I call the work capture API with the certificate password" )
   public void createWorkCaptureWithCertificatePasswordSuccess() {
      Signature signature = workCapture.getSignature();
      signature.setPassword( ENCRYPTED_PASSWORD );
      workCapture.setSignature( signature );
      workCaptureAPITestHelper.createWorkCaptureWithPdf( expectedStatus, Credentials.AUTHORIZED,
            workCapture, workCapturePdf );

   }


   @When( "^I call the work capture API with a missing report" )
   public void createWorkCaptureWithMissingReportFailure() throws IOException {
      Signature signature = workCapture.getSignature();
      signature.setPassword( ENCRYPTED_PASSWORD );
      workCapture.setSignature( signature );
      workCapturePdf.delete();
      workCapturePdf.createNewFile();
      response = workCaptureAPITestHelper.createWorkCaptureWithPdf( 400, Credentials.AUTHORIZED,
            workCapture, workCapturePdf );
   }


   @Then( "^the labour row is in work and a report is generated" )
   public void validateLabourIsInWork()
         throws JsonParseException, JsonMappingException, IOException {
      assertWorkCaptureLabourRow( workCapture.getLabourId(), IN_WORK_STATUS, USERNAME, null,
            ACTV_STATUS, PENDING_STATUS );
      Assert.assertNotNull( "No work capture report was received.", workCapturePdf );
      Assert.assertTrue( "Work capture report is blank.",
            workCapturePdf.exists() && workCapturePdf.length() > EMPTY_PDF_LENGTH );
      Assert.assertNotNull( "No token was found in the payload.",
            workCapture.getSignature().getToken() );

   }


   @Then( "^the labour row is complete and a new labour row is added" )
   public void validateLabourIsComplete()
         throws JsonParseException, JsonMappingException, IOException {

      String labourId = workCapture.getLabourId();

      assertWorkCaptureLabourRow( labourId, COMPLETE_STATUS, USERNAME, USERNAME, COMPLETE_STATUS,
            COMPLETE_STATUS );
      assertWorkCapturePDF( labourId );

      Map<String, String> taskParams = new HashMap<String, String>();
      taskParams.put( TaskSearchParameters.LABOUR_ID_PARAM, labourId );

      Response taskResponse = search( Status.OK.getStatusCode(), Credentials.AUTHENTICATED,
            Task.PATH, taskParams, MediaType.APPLICATION_JSON );

      List<Task> taskList = objectMapper.readValue( taskResponse.getBody().asString(),
            TypeFactory.defaultInstance().constructCollectionType( List.class, Task.class ) );

      // Check that there is a unique task
      Assert.assertTrue( "There are multiple tasks for labour with ID [" + labourId + "].",
            taskList.size() == 1 );

      Task task = taskList.get( 0 );

      // Assert that a new labour row was created and the task is in PAUSE status
      Assert.assertEquals( "A new labour row was not added to the task.", 2,
            task.getLabour().size() );

      Labour addedLabour = null;
      for ( Labour tmpLabour : task.getLabour() ) {
         if ( !tmpLabour.getId().equals( labourId ) ) {
            addedLabour = tmpLabour;
         }
      }

      assertWorkCaptureLabourRow( addedLabour.getId(), ACTV_STATUS, USERNAME, null, ACTV_STATUS,
            PENDING_STATUS );

      Assert.assertEquals( "Task with ID [" + task.getId() + "] is not in PAUSE status.",
            PAUSE_STATUS, task.getStatus() );

      globalParametersQueriesDriver.setGlobalParameterValue( ESIG_PARM, "false" );
   }


   @Then( "^a bad request for Work Package not IN WORK response is returned" )
   public void assertBadRequestResponseWPNotInWork() {

      String expectedErrorMessage =
            "You cannot start work on task '" + task.getName() + " [" + task.getBarcode()
                  + "]' because it is assigned to a work package whose status is not IN WORK.";

      String actualErrorMessage = response.getBody().asString();

      Assert.assertTrue( "Error Message: Expected: \n[" + expectedErrorMessage + "] \n Actual: \n["
            + actualErrorMessage + "]", actualErrorMessage.contains( expectedErrorMessage ) );

   }


   @Then( "^a bad request for unexpected change in Work Capture response is returned" )
   public void assertBadRequestResponseChangedWC() {
      String expectedErrorMessage = "The work capture has been unexpectedly changed.";
      String actualErrorMessage = response.getBody().asString();

      Assert.assertTrue( "Error Message: Expected: \n[" + expectedErrorMessage + "] \n Actual: \n["
            + actualErrorMessage + "]", actualErrorMessage.contains( expectedErrorMessage ) );
   }


   private WorkCapture getWorkCaptureFromResponse( Response response )
         throws MessagingException, JsonParseException, JsonMappingException, IOException {

      WorkCapture workCapture = new WorkCapture();
      InputStream body = response.getBody().asInputStream();
      ByteArrayDataSource datasource = new ByteArrayDataSource( body, "multipart/form-data" );
      MimeMultipart multipart = new MimeMultipart( datasource );
      int count = multipart.getCount();

      for ( int i = 0; i < count; i++ ) {
         javax.mail.BodyPart bodyPart = multipart.getBodyPart( i );
         if ( bodyPart.getContentType().equals( MediaType.APPLICATION_JSON ) ) {
            workCapture = objectMapper.readValue( bodyPart.getInputStream(), WorkCapture.class );
         } else if ( bodyPart.isMimeType( "application/pdf" ) ) {
            workCapturePdf = createTempPdf( bodyPart.getInputStream() );
         }
      }
      return workCapture;

   }


   private void assertWorkCaptureLabourRow( String labourId, String stageCd, String techUsername,
         String certUsername, String techStatus, String certStatus )
         throws JsonParseException, JsonMappingException, IOException {

      Response labourResponse = get( 200, Credentials.AUTHENTICATED, Labour.PATH, labourId,
            MediaType.APPLICATION_JSON );
      Labour actualLabour =
            objectMapper.readValue( labourResponse.getBody().asString(), Labour.class );

      // Check that the labour is in the appropriate stage (IN WORK or COMPLETE)
      Assert.assertEquals(
            "Labour row with labour ID [" + labourId + "] does not have status " + stageCd + ".",
            stageCd, actualLabour.getLabourStageCode() );

      // Assert that the INSP role has been updated according to the work capture payload.
      if ( stageCd.equals( COMPLETE_STATUS ) ) {
         Assert.assertEquals(
               "Labour row with labour ID [" + labourId + "] did not update the INSPECTION role.",
               notes.isIndependentInspectionRequired(), actualLabour.isInspectionRequired() );
      }

      for ( LabourRole actualLabourRole : actualLabour.getLabourRoles() ) {
         String expectedStatusCd = "";
         String expectedUsername = "";

         switch ( actualLabourRole.getRole() ) {
            case TECH_LABOUR_ROLE:
               expectedStatusCd = techStatus;
               expectedUsername = techUsername;
               break;
            case CERT_LABOUR_ROLE:
               expectedStatusCd = certStatus;
               expectedUsername = certUsername;
               break;
            case INSP_LABOUR_ROLE:
               expectedStatusCd = PENDING_STATUS;
               expectedUsername = null;
               break;
         }

         // Check that the labour row has the appropriate status and username
         Assert.assertEquals(
               "Labour role [" + actualLabourRole.getRole() + "] with labour ID [" + labourId
                     + "] doesn't have correct status. ",
               expectedStatusCd, actualLabourRole.getStatusCode() );
         Assert.assertEquals(
               "Labour row with labour ID [" + labourId + "] doesn't have correct technician. ",
               expectedUsername, actualLabourRole.getUsername() );
      }

   }


   private void assertWorkCapturePDF( String labourId ) {

      List<SchedLabourEsigInfo> labourEsigs =
            schedLabourQueriesDriver.getSchedLabourEsigInfoByLabourId( labourId );

      Assert.assertTrue( "Work Capture PDF was not saved in the database.",
            !labourEsigs.isEmpty() );

      SchedLabourEsigInfo certEsig = null;
      SchedLabourEsigInfo techEsig = null;

      for ( SchedLabourEsigInfo labourEsig : labourEsigs ) {
         if ( labourEsig.getDocName().equals( "Work Capture Report" ) ) {

            String labourRoleCd = labourEsig.getLabourRoleCd();

            if ( labourRoleCd.equals( TECH_LABOUR_ROLE ) ) {
               techEsig = labourEsig;
            } else if ( labourRoleCd.equals( CERT_LABOUR_ROLE ) ) {
               certEsig = labourEsig;
            } else {
               Assert.fail(
                     "Work Capture Report is not associated with an appropriate role. Expected [CERT] or [TECH] but was ["
                           + labourRoleCd + "]." );
            }

            boolean workCapturePdfExists = labourEsig.getBlobKey().getDbId() != null
                  && labourEsig.getBlobKey().getId() != null;

            Assert.assertTrue( "Work Capture PDF was not saved in the database.",
                  workCapturePdfExists );

            BlobInfo blobInfo = blobQueriesDriver.getBlobInfoByKey( labourEsig.getBlobKey() );

            Assert.assertEquals(
                  "Expected Work Capture PDF content type is " + PDF_CONTENT_TYPE + ".",
                  PDF_CONTENT_TYPE, blobInfo.getContentType() );

            byte[] workCaptureBlob = blobQueriesDriver.getBlobDataByKey( labourEsig.getBlobKey(),
                  blobInfo.getDataLocation() );

            Assert.assertNotNull( "Work Capture report found in database is empty.",
                  workCaptureBlob );

            Assert.assertTrue( "Work capture report is blank.",
                  !( workCaptureBlob.length < EMPTY_PDF_LENGTH ) );

         }
      }

      int workCaptureReportNum = labourEsigs.size();
      Assert.assertTrue( "Expected an e-signature for the work performed.", techEsig != null );

      if ( notes.isCertifyWorkPerformed() ) {
         Assert.assertTrue( "Expected an e-signature for the certification.", certEsig != null );
         Assert.assertTrue(
               "Certification e-signature should be the same as the work performed e-signature.",
               techEsig.getBlobKey().getDbId().equals( certEsig.getBlobKey().getDbId() )
                     && techEsig.getBlobKey().getId().equals( certEsig.getBlobKey().getId() ) );
         // Since the e-signature attached to the certification is the same as the one for the work
         // performed, we do not count it.
         workCaptureReportNum--;
      } else {
         Assert.assertTrue(
               "No e-signature should have been appended to the work capture for certification.",
               certEsig == null );
      }

      Assert.assertTrue(
            "Expected 1 work capture report saved in the database, found " + labourEsigs.size(),
            workCaptureReportNum == 1 );
   }


   private List<Measurements> buildMeasurements() throws ParseException {

      // Create the measurements for the inventory
      Measurement measurement1 = new Measurement();
      measurement1.setDataTypeCode( QTS_MEASUREMENT_CD );

      Measurement measurement2 = new Measurement();
      measurement2.setDataTypeCode( DAY_MEASUREMENT_CD );
      measurement2.setEngineeringUnitCode( DAY_MEASUREMENT_CD );

      FlightMeasurementRequirement flightMeasurementReq = new FlightMeasurementRequirement();
      flightMeasurementReq
            .setAssemblyId( workCaptureAPITestHelper.getWPInventory().getAssemblyId() );
      flightMeasurementReq.setMeasurements( Arrays.asList( measurement1, measurement2 ) );

      create( 200, Credentials.AUTHORIZED, flightMeasurementReq, FlightMeasurementRequirement.PATH,
            MediaType.APPLICATION_JSON );

      // Add the measurements to the task
      taskExecutionPaneDriver.clickAddMeasurement();
      List<MeasurementResult> measurementResults = addMeasurementPageDriver.getResults();

      for ( MeasurementResult row : measurementResults ) {
         if ( row.getParameter().contains( QTS_MEASUREMENT_CD ) ) {
            row.clickMeasurement();
         } else if ( row.getParameter().contains( DAY_MEASUREMENT_CD ) ) {
            row.clickMeasurement();
            row.setValue( MEASUREMENT_DATE );
         }
      }

      addMeasurementPageDriver.clickOk();

      // Build the measurements for the work capture
      Measurements wcMeasurement1 = new Measurements();
      wcMeasurement1.setInventoryId( task.getInventoryId() );
      wcMeasurement1.setParameterCode( QTS_MEASUREMENT_CD );
      wcMeasurement1.setValue( "1" );

      Measurements wcMeasurement2 = new Measurements();
      wcMeasurement2.setInventoryId( task.getInventoryId() );
      wcMeasurement2.setParameterCode( DAY_MEASUREMENT_CD );
      wcMeasurement2.setValue( WC_MEASUREMENT_DATE );

      return Arrays.asList( wcMeasurement1, wcMeasurement2 );
   }


   private List<Tools> buildTools() throws JsonParseException, JsonMappingException, IOException {

      HashMap<String, String> invParams = new HashMap<>();

      invParams.put( InventorySearchParameters.SERIAL_NO_PARAM, SER_TOOL_INV_SERIAL_NO );
      Inventory serToolInventory = workCaptureAPITestHelper.searchByParameters( Inventory.PATH,
            invParams, Inventory.class );

      invParams.put( InventorySearchParameters.SERIAL_NO_PARAM, TRK_TOOL_INV_SERIAL_NO );
      Inventory trkToolInventory = workCaptureAPITestHelper.searchByParameters( Inventory.PATH,
            invParams, Inventory.class );

      // Add the tools to the task
      taskExecutionPaneDriver.clickAddToolRequirement();
      addToolRequirementPageDriver.setPartNoOEMField( serToolInventory.getPartNumber() );
      addToolRequirementPageDriver.clickSearch();
      ToolFound row = addToolRequirementPageDriver.getToolsFoundRows().get( 0 );
      row.clickCheckbox();
      addToolRequirementPageDriver.clickAssignSelectedPartsToTask();

      taskExecutionPaneDriver.clickAddToolRequirement();
      addToolRequirementPageDriver.setPartNoOEMField( trkToolInventory.getPartNumber() );
      addToolRequirementPageDriver.clickSearch();
      row = addToolRequirementPageDriver.getToolsFoundRows().get( 0 );
      row.clickCheckbox();
      addToolRequirementPageDriver.clickAssignSelectedPartsToTask();

      // Build the tools for the work capture
      Tools tool1 = new Tools();
      tool1.setInventoryId( serToolInventory.getId() );
      tool1.setHours( BigDecimal.ONE );

      Tools tool2 = new Tools();
      tool2.setInventoryId( trkToolInventory.getId() );
      tool2.setHours( BigDecimal.ONE );

      return Arrays.asList( tool1, tool2 );
   }


   private List<Steps> buildSteps() {

      // Add the steps to the task
      taskExecutionPaneDriver.clickAddJobCardStep();
      addStepPageDriver.setDescription( "Step 1: Use tool to uninstall part." );
      addStepPageDriver.clickOk();

      taskExecutionPaneDriver.clickAddJobCardStep();
      addStepPageDriver.setDescription( "Step 2: Use tool to install part." );
      addStepPageDriver.clickOk();

      // Build the steps for the work capture
      Steps step1 = new Steps();
      step1.setOrder( 1 );
      step1.setStatusCode( MXCOMPLETE_STATUS );
      step1.setNote( "Part uninstalled." );

      Steps step2 = new Steps();
      step2.setOrder( 2 );
      step2.setStatusCode( MXCOMPLETE_STATUS );
      step2.setNote( "Part installed." );

      return Arrays.asList( step1, step2 );
   }


   private Notes buildNotes() {

      // Add the certification and inspection required values
      Labor row = taskExecutionPaneDriver.getLaborRows().get( 0 );
      row.clickLabourRow();

      taskExecutionPaneDriver.clickEditLabor();

      LabourRequirement labourReq =
            editLabourRequirementsPageDriver.getLabourRequirementRows().get( 0 );
      labourReq.clickRequiredForCertification();
      labourReq.clickRequiredForInspection();

      editLabourRequirementsPageDriver.clickOkButton();

      // Build the notes for the work capture
      Notes notes = new Notes();
      notes.setCertifyWorkPerformed( true );
      notes.setIndependentInspectionRequired( false );
      notes.setDocumentReference( "Test" );

      return notes;

   }


   private List<Parts> buildPart() throws JsonParseException, JsonMappingException, IOException {

      Inventory installedPartInv = createTrkInventory( PART_NO, PART_MANUFACT_CD );

      RemovedPart removedPart = buildRemovedPart();
      InstalledPart installedPart = buildInstalledPart( installedPartInv.getId() );

      // Add part requirement with part request to task
      taskExecutionPaneDriver.clickAddPartRequirement();
      addPartRequirementPageDriver.setPartNoOEMField( PART_NO );
      addPartRequirementPageDriver.clickSearch();

      PartsFound row = addPartRequirementPageDriver.getPartsFoundRows().get( 0 );

      row.clickPartGroupCheckbox();
      row.setInstall( true );
      row.setRemoval( true );
      row.setAction( REQ_ACTION_CD );
      row.clickAlternatePartsListRadioButton();

      addPartRequirementPageDriver.clickAssignSelectedPartsToTask();

      PartRequests partReq = taskExecutionPaneDriver.getPartRequestsRows().get( 0 );
      partReq.clickRequestID();

      partRequestDetailsPageDriver.clickReserveLocalInventory();
      reserveLocalInventoryPageDriver
            .clickRadioButtonForInventory( installedPartInv.getSerialNumber() );
      reserveLocalInventoryPageDriver.clickOk();

      partRequestDetailsPageDriver.clickIssueInventory();
      issueInventoryPageDriver.setPartNoOem( installedPartInv.getPartNumber() );
      issueInventoryPageDriver.setSerialNo( installedPartInv.getSerialNumber() );
      issueInventoryPageDriver.clickAddPart();
      issueInventoryPageDriver.clickOk();
      partRequestDetailsPageDriver.clickClose();

      Response taskResponse =
            get( 200, Credentials.AUTHORIZED, Task.PATH, task.getId(), MediaType.APPLICATION_JSON );
      task = objectMapper.readValue( taskResponse.getBody().asString(), Task.class );

      // Build part requirement for the work capture
      Parts part = new Parts();
      part.setPositionCode( "1" );
      part.setRemovedPart( removedPart );
      part.setInstalledPart( installedPart );
      part.setPartRequirementId( task.getPartRequirements().get( 0 ).getId() );

      return Arrays.asList( part );
   }


   private RemovedPart buildRemovedPart()
         throws JsonParseException, JsonMappingException, IOException {

      HashMap<String, String> invParams = new HashMap<>();

      invParams.put( InventorySearchParameters.PART_NO_PARAM, PART_NO );
      invParams.put( InventorySearchParameters.MANUFACT_CD_PARAM, PART_MANUFACT_CD );
      invParams.put( InventorySearchParameters.HIGHEST_INV_ID, task.getInventoryId() );
      Inventory rmvInventory = workCaptureAPITestHelper.searchByParameters( Inventory.PATH,
            invParams, Inventory.class );

      rmvInventory.setSerialNumber(
            String.format( SERIAL_NO_FORMAT, new Random().nextInt( 90000000 ) + 10000000 ) );

      Response updatedRmvInventoryResponse = update( 200, Credentials.AUTHORIZED, rmvInventory,
            Inventory.PATH, rmvInventory.getId(), MediaType.APPLICATION_JSON );

      // Update the serial number of the inventory (otherwise it's 'XXX' and invalid)
      Inventory updatedRmvInventory = objectMapper
            .readValue( updatedRmvInventoryResponse.getBody().asString(), Inventory.class );

      updatedRmvInventory.setConditionCode( rmvInventory.getConditionCode() );
      updatedRmvInventory.setHighestInventoryId( rmvInventory.getHighestInventoryId() );
      updatedRmvInventory.setParentId( rmvInventory.getParentId() );

      // We need to re-attach the inventory to the aircraft
      update( 200, Credentials.AUTHORIZED, updatedRmvInventory, Inventory.PATH,
            rmvInventory.getId(), MediaType.APPLICATION_JSON );

      // Build the removed part for the work capture
      RemovedPart removedPart = new RemovedPart();
      removedPart.setInventoryId( rmvInventory.getId() );
      removedPart.setQuantity( BigDecimal.ONE );
      removedPart.setReasonCode( LOAN_REASON_CD );

      return removedPart;

   }


   private InstalledPart buildInstalledPart( String inventoryId )
         throws JsonParseException, JsonMappingException, IOException {

      // Build the installed part for the work capture
      InstalledPart installedPart = new InstalledPart();
      installedPart.setInventoryId( inventoryId );
      installedPart.setQuantity( BigDecimal.ONE );

      return installedPart;
   }


   private Inventory createTrkInventory( String partNumber, String manufactCd )
         throws JsonParseException, JsonMappingException, IOException {

      HashMap<String, String> locationParams = new HashMap<>();
      locationParams.put( LocationSearchParameters.PARAM_CODE, INV_LOCATION_CD );
      Location location = workCaptureAPITestHelper.searchByParameters( Location.PATH,
            locationParams, Location.class );

      HashMap<String, String> ownerParams = new HashMap<>();
      ownerParams.put( OwnerSearchParameters.OWNER_CODE_PARAM, INV_OWNER_CD );
      Owner owner =
            workCaptureAPITestHelper.searchByParameters( Owner.PATH, ownerParams, Owner.class );

      Inventory inventory = new Inventory();
      inventory.setType( INV_TYPE );
      inventory.setLocked( false );
      inventory.setSerialNumber(
            String.format( SERIAL_NO_FORMAT, new Random().nextInt( 90000000 ) + 10000000 ) );
      inventory.setConditionCode( INV_CONDITION_CD );
      inventory.setOwnerId( owner.getId() );
      inventory.setPartNumber( partNumber );
      inventory.setManufacturerCode( manufactCd );
      inventory.setLocationId( location.getId() );

      Response inventoryResponse = create( 200, Credentials.AUTHORIZED, inventory, Inventory.PATH,
            MediaType.APPLICATION_JSON );
      inventory = objectMapper.readValue( inventoryResponse.getBody().asString(), Inventory.class );

      return inventory;
   }


   private JobStop buildJobStop() {

      JobStop jobStop = new JobStop();
      jobStop.setRemainingHours( BigDecimal.TEN );
      jobStop.setReassignMe( true );
      jobStop.setReasonCode( SHIFT_CHG_REASON_CD );
      jobStop.setNotes( "Test Job Stop Notes" );

      return jobStop;
   }


   private File createTempPdf( InputStream in ) throws IOException {
      final File tempFile = File.createTempFile( "WorkCaptureReport", ".pdf" );
      tempFile.deleteOnExit();
      try ( FileOutputStream out = new FileOutputStream( tempFile ) ) {
         IOUtils.copy( in, out );
      }
      return tempFile;
   }

}
