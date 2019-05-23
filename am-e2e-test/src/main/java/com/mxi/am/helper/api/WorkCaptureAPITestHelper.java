package com.mxi.am.helper.api;

import static com.mxi.am.helper.api.GenericAmApiCalls.create;
import static com.mxi.am.helper.api.GenericAmApiCalls.search;
import static com.mxi.am.helper.api.GenericAmApiCalls.update;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;
import com.mxi.am.api.resource.erp.location.Location;
import com.mxi.am.api.resource.erp.location.LocationSearchParameters;
import com.mxi.am.api.resource.finance.FinanceAccount;
import com.mxi.am.api.resource.finance.FinanceAccountSearchParameters;
import com.mxi.am.api.resource.maintenance.exec.task.Task;
import com.mxi.am.api.resource.maintenance.exec.task.labour.Labour;
import com.mxi.am.api.resource.maintenance.exec.task.labour.LabourRole;
import com.mxi.am.api.resource.maintenance.exec.task.labour.LabourSearchParameters;
import com.mxi.am.api.resource.maintenance.exec.task.labour.workcapture.Signature;
import com.mxi.am.api.resource.maintenance.exec.task.labour.workcapture.WorkCapture;
import com.mxi.am.api.resource.maintenance.exec.task.labour.workcapture.WorkCapturePathBuilder;
import com.mxi.am.api.resource.maintenance.exec.work.pkg.Workpackage;
import com.mxi.am.api.resource.materials.asset.inventory.Inventory;
import com.mxi.am.api.resource.materials.asset.inventory.InventorySearchParameters;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * Work Capture API Test Helper
 *
 */
public class WorkCaptureAPITestHelper {

   private static final String AMAPI = "/amapi/";

   private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
   private static final String CURRENT_DATE = new Date().toString();

   private static final String WP_NAME = "Test WC Work Package " + CURRENT_DATE;
   private static final String WP_ACTV_STATUS = "ACTV";
   private static final String WP_TASK_CLASS = "CHECK";
   private static final String WP_DUE_DATA_TYPE = "CDY";
   private static final Boolean WP_REQUEST_PARTS = true;
   private static final String WP_DUE_DATE = "2019-10-03 03:59:59";
   private static final String WP_SCHED_START_DATE = "2019-10-16 23:56:00";
   private static final Boolean WP_HEAVY_MAINTENANCE_BOOL = false;
   private static final String WP_SCHED_END_DATE = "2019-10-20 16:56:00";
   private static final String WP_STATUS_DESCRIPTION = "Commit";

   private static final String WP_LOC_CD = "AIRPORT1/LINE";
   private static final String WP_INV_SERIAL_NO = "WC-111";
   private static final String WP_ISSUE_ACCOUNT_CD = "5";

   private static final String TASK_CLASS = "ADHOC";
   private static final String TASK_NAME = "Test Work Capture Task " + CURRENT_DATE;

   private static final String WC_START_DATE = "2019-10-17 00:56:00";
   private static final String WC_END_DATE = "2019-10-17 10:56:00";
   private static final String WC_USER = "mxi";

   private static final String IN_WORK_STATUS = "IN WORK";
   private static final String IN_WORK_STATUS_DESC = "Task In Work";

   private static ObjectMapper objectMapper =
         ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();

   private Task task;
   private String taskId;

   private Labour labour;
   private String labourId;

   private Workpackage workpackage;
   private String workpackageId;
   private String locationId;
   private Inventory wpInventory;
   private String workPackageInvId;
   private String issueAccountId;


   public void setUpWorkPackageAndTask( Boolean startWorkPackage )
         throws JsonParseException, JsonMappingException, IOException, ParseException {

      HashMap<String, String> locParams = new HashMap<>();
      locParams.put( LocationSearchParameters.PARAM_CODE, WP_LOC_CD );
      Location location = searchByParameters( Location.PATH, locParams, Location.class );
      locationId = location.getId();

      HashMap<String, String> invParams = new HashMap<>();
      invParams.put( InventorySearchParameters.SERIAL_NO_PARAM, WP_INV_SERIAL_NO );
      wpInventory = searchByParameters( Inventory.PATH, invParams, Inventory.class );
      workPackageInvId = wpInventory.getId();

      HashMap<String, String> fncAccParams = new HashMap<>();
      fncAccParams.put( FinanceAccountSearchParameters.PARAM_ACCOUNT_CODE, WP_ISSUE_ACCOUNT_CD );
      FinanceAccount financeAccount =
            searchByParameters( FinanceAccount.PATH, fncAccParams, FinanceAccount.class );
      issueAccountId = financeAccount.getAltId();

      buildWorkPackageAndTask( startWorkPackage );

      HashMap<String, String> labourParams = new HashMap<>();
      labourParams.put( LabourSearchParameters.TASK_ID_PARAM, taskId );
      labour = searchByParameters( Labour.PATH, labourParams, Labour.class );
      labourId = labour.getId();
   }


   public String getLabourId() {
      return labourId;
   }


   public void setLabourId( String labourId ) {
      this.labourId = labourId;
   }


   public Task getTask() {
      return task;
   }


   public Inventory getWPInventory() {
      return wpInventory;
   }


   public void buildWorkPackageAndTask( Boolean startWorkPackage )
         throws JsonParseException, JsonMappingException, IOException, ParseException {
      buildWorkpackage();
      Response wpResponse = create( 200, Credentials.AUTHENTICATED, workpackage, Workpackage.PATH,
            MediaType.APPLICATION_JSON );

      workpackage = objectMapper.readValue( wpResponse.getBody().asString(), Workpackage.class );
      workpackageId = workpackage.getId();

      // Start work package if startWorkPackage is true
      if ( startWorkPackage ) {
         workpackage.setStatus( IN_WORK_STATUS );
         workpackage.setStatusDescription( IN_WORK_STATUS_DESC );
         update( 200, Credentials.AUTHENTICATED, workpackage, Workpackage.PATH, workpackageId,
               MediaType.APPLICATION_JSON );
      }

      buildAdhocTask();
      Response taskResponse =
            create( 200, Credentials.AUTHENTICATED, task, Task.PATH, MediaType.APPLICATION_JSON );
      Task taskCreated = objectMapper.readValue( taskResponse.getBody().asString(), Task.class );

      // Assigning task to work package
      taskCreated.setWorkPackageId( workpackageId );
      taskResponse = update( 200, Credentials.AUTHENTICATED, taskCreated, Task.PATH,
            taskCreated.getId(), MediaType.APPLICATION_JSON );
      task = objectMapper.readValue( taskResponse.getBody().asString(), Task.class );
      taskId = task.getId();

   }


   private void buildWorkpackage() throws ParseException {
      workpackage = new Workpackage();

      workpackage.setAircraftId( workPackageInvId );
      workpackage.setDueDataType( WP_DUE_DATA_TYPE );
      workpackage.setDueDate( new SimpleDateFormat( DATE_FORMAT ).parse( WP_DUE_DATE ) );
      workpackage.setHeavyMaintenanceBool( WP_HEAVY_MAINTENANCE_BOOL );
      workpackage.setHighestInventoryId( workPackageInvId );
      workpackage.setInventoryId( workPackageInvId );
      workpackage.setIssueAccountId( issueAccountId );
      workpackage.setLocationId( locationId );
      workpackage.setName( WP_NAME );
      workpackage.setRequestParts( WP_REQUEST_PARTS );
      workpackage.setSchedEndDate( new SimpleDateFormat( DATE_FORMAT ).parse( WP_SCHED_END_DATE ) );
      workpackage
            .setSchedStartDate( new SimpleDateFormat( DATE_FORMAT ).parse( WP_SCHED_START_DATE ) );
      workpackage.setStatus( WP_ACTV_STATUS );
      workpackage.setStatusDescription( WP_STATUS_DESCRIPTION );
      workpackage.setTaskClass( WP_TASK_CLASS );

   }


   private void buildAdhocTask() {
      task = new Task();
      task.setName( TASK_NAME );
      task.setHistoric( false );
      task.setInventoryId( workPackageInvId );
      task.setTaskClass( TASK_CLASS );
   }


   public WorkCapture buildWorkCapture() throws ParseException {

      WorkCapture workCapture = new WorkCapture();
      workCapture = new WorkCapture();
      workCapture.setStartDate( new SimpleDateFormat( DATE_FORMAT ).parse( WC_START_DATE ) );
      workCapture.setEndDate( new SimpleDateFormat( DATE_FORMAT ).parse( WC_END_DATE ) );
      workCapture.setLabourId( labourId );
      workCapture.setHours( BigDecimal.TEN );

      Signature signature = new Signature();
      signature.setUsername( WC_USER );
      workCapture.setSignature( signature );
      return workCapture;
   }


   public <T extends Object> T searchByParameters( String path,
         Map<String, String> searchParameters, Class<T> type )
         throws JsonParseException, JsonMappingException, IOException {

      Response response = search( Status.OK.getStatusCode(), Credentials.AUTHENTICATED, path,
            searchParameters, MediaType.APPLICATION_JSON );

      List<T> objectList = objectMapper.readValue( response.getBody().asString(),
            TypeFactory.defaultInstance().constructCollectionType( List.class, type ) );

      if ( objectList.isEmpty() ) {
         return null;
      }
      return objectList.get( 0 );
   }


   public LabourRole getLabourRole( Labour labour, String roleCd ) {
      LabourRole labourRole = null;

      for ( LabourRole tmpLabourRole : labour.getLabourRoles() ) {
         if ( tmpLabourRole.getRole().equals( roleCd ) ) {
            labourRole = tmpLabourRole;
            break;
         }
      }

      return labourRole;
   }


   public Response generateReport( int statusCode, Credentials security, Object workCapture ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      String path = AMAPI + new WorkCapturePathBuilder().post( labourId ).build();

      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .contentType( MediaType.APPLICATION_JSON ).body( workCapture ).expect()
            .statusCode( statusCode ).when().post( path );
      return response;
   }


   public Response createWorkCapture( int statusCode, Credentials security, Object workCapture ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      String path = AMAPI + new WorkCapturePathBuilder().put( labourId ).build();

      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .multiPart( "WorkCapture", workCapture, MediaType.APPLICATION_JSON ).expect()
            .statusCode( statusCode ).when().put( path );
      return response;
   }


   public Response createWorkCaptureWithPdf( int statusCode, Credentials security,
         Object workCapture, File workCaptureReport ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      String path = AMAPI + new WorkCapturePathBuilder().put( labourId ).build();

      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .multiPart( "WorkCapture", workCapture, MediaType.APPLICATION_JSON )
            .multiPart( "WorkCaptureReport", workCaptureReport, "application/pdf" ).expect()
            .statusCode( statusCode ).when().put( path );
      return response;
   }

}
