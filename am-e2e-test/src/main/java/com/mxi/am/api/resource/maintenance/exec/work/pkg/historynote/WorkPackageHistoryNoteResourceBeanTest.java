package com.mxi.am.api.resource.maintenance.exec.work.pkg.historynote;

import static org.junit.Assert.assertEquals;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mxi.am.api.annotation.CSIContractTest;
import com.mxi.am.api.annotation.CSIContractTest.Project;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.driver.common.database.Result;
import com.mxi.am.guice.AssetManagementDatabaseDriverProvider;

import io.restassured.RestAssured;
import io.restassured.response.Response;


public class WorkPackageHistoryNoteResourceBeanTest {

   private static final String APPLICATION_JSON = "application/json";
   private static final String HISTORY_NOTE_PATH = "/amapi/" + HistoryNote.PATH;

   private DatabaseDriver driver;

   private static final String WP_BARCODE = "T40S0002JPN";
   private static final String USERNAME = "ADMIN";
   private static final String INVALID_ALT_ID = "00000000000000000000000000000000";

   // Queries to retrieve generated alt_id
   private final String queryTask = "SELECT * FROM sched_stask WHERE barcode_sdesc = ?";
   private final String queryUtlUser = "SELECT * FROM utl_user WHERE username = ?";
   private final String queryHistoryNote = "SELECT evt_stage.alt_id FROM  evt_event"
         + " INNER JOIN sched_stask ON"
         + "    evt_event.event_db_id = sched_stask.sched_db_id AND evt_event.event_id = sched_stask.sched_id"
         + " LEFT JOIN evt_stage ON"
         + "    evt_stage.event_db_id = evt_event.event_db_id AND evt_stage.event_id = evt_event.event_id"
         + " WHERE sched_stask.alt_id = ? ORDER BY evt_stage.stage_id";

   private String wpId;
   private String userId;
   private String historyNoteId;
   private int wpIdCount;


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

      Result task = driver.select( queryTask, WP_BARCODE );
      wpId = task.get( 0 ).getUuidString( "alt_id" );

      Result user = driver.select( queryUtlUser, USERNAME );
      userId = user.get( 0 ).getUuidString( "alt_id" );

      Result historyNote = driver.select( queryHistoryNote, wpId );
      historyNoteId = historyNote.get( 0 ).getUuidString( "alt_id" );
      wpIdCount = historyNote.getNumberOfRows();
   }


   @Test
   @CSIContractTest( Project.SWA_WP_STATUS )
   public void get_200() {
      Response response = getById( 200, Credentials.AUTHORIZED, historyNoteId );

      assertHistoryNote( buildHistoryNoteToFind(), response );
   }


   @Test
   public void get_401() {
      getById( 401, Credentials.UNAUTHENTICATED, historyNoteId );
   }


   @Test
   public void get_403() {
      getById( 403, Credentials.UNAUTHORIZED, historyNoteId );
   }


   @Test
   public void get_404() {
      getById( 404, Credentials.AUTHORIZED, INVALID_ALT_ID );
   }


   @Test
   public void search_200() {
      Response response = searchByWorkPackageId( 200, Credentials.AUTHORIZED, wpId );

      List<HistoryNote> historyNoteList = response.jsonPath().getList( "", HistoryNote.class );

      assertEquals( "Unexpected number of results from search: ", wpIdCount,
            historyNoteList.size() );
   }


   @Test
   public void search_200_noResults() {
      Response response = searchByWorkPackageId( 200, Credentials.AUTHORIZED, INVALID_ALT_ID );

      List<HistoryNote> historyNoteList = response.jsonPath().getList( "", HistoryNote.class );

      assertEquals( "Unexpected number of results from search: ", 0, historyNoteList.size() );
   }


   @Test
   public void create_200() {
      HistoryNote historyNoteToCreate = buildHistoryNoteToCreate();
      Response response = create( 200, Credentials.AUTHORIZED, historyNoteToCreate );

      assertHistoryNote( historyNoteToCreate, response );
   }


   private Response getById( int expectedStatusCode, Credentials security, String noteId ) {

      return getById( expectedStatusCode, security, noteId, APPLICATION_JSON, APPLICATION_JSON );
   }


   private Response getById( int expectedStatusCode, Credentials security, String noteId,
         String contentType, String acceptType ) {

      String username = security.getUserName();
      String password = security.getPassword();

      Response lResponse = RestAssured.given().auth().preemptive().basic( username, password )
            .accept( acceptType ).contentType( contentType ).expect()
            .statusCode( expectedStatusCode ).when().get( HISTORY_NOTE_PATH + "/" + noteId );

      return lResponse;
   }


   private Response searchByWorkPackageId( int expectedStatusCode, Credentials security,
         String taskId ) {

      return searchByWorkPackageId( expectedStatusCode, security, taskId, APPLICATION_JSON,
            APPLICATION_JSON );
   }


   private Response searchByWorkPackageId( int expectedStatusCode, Credentials security,
         String workpackageId, String contentType, String acceptType ) {

      String username = security.getUserName();
      String password = security.getPassword();

      Response lResponse =
            RestAssured.given().auth().preemptive().basic( username, password ).accept( acceptType )
                  .contentType( contentType ).expect().statusCode( expectedStatusCode ).when()
                  .get( HISTORY_NOTE_PATH + "?workpackage_id=" + workpackageId );

      return lResponse;
   }


   private Response create( int statusCode, Credentials security, Object historyNoteToCreate ) {
      return create( statusCode, security, historyNoteToCreate, APPLICATION_JSON,
            APPLICATION_JSON );
   }


   private Response create( int statusCode, Credentials security, Object historyNoteToCreate,
         String contentType, String acceptType ) {

      String username = security.getUserName();
      String password = security.getPassword();

      Response lResponse = RestAssured.given().auth().preemptive().basic( username, password )
            .accept( acceptType ).contentType( contentType ).body( historyNoteToCreate ).expect()
            .statusCode( statusCode ).when().post( HISTORY_NOTE_PATH );

      return lResponse;
   }


   private void assertHistoryNote( HistoryNote expectedNote, Response response ) {
      HistoryNote actualNote = response.jsonPath().getObject( "", HistoryNote.class );
      assertHistoryNote( expectedNote, actualNote );
   }


   private void assertHistoryNote( HistoryNote expected, HistoryNote actual ) {
      assertEquals( "Task Id of history note did not match the expected value: ",
            expected.getWorkPackageId(), actual.getWorkPackageId() );
      assertEquals( "Stage Reason Code of history note did not match the expected value: ",
            expected.getStageReasonCode(), actual.getStageReasonCode() );
      assertEquals( "Status of history note did not match the expected value: ",
            expected.getStatusCode(), actual.getStatusCode() );
      assertEquals( "Note of history note did not match the expected value: ", expected.getNote(),
            actual.getNote() );
   }


   private HistoryNote buildHistoryNoteToFind() {
      HistoryNote historyNote = new HistoryNote();
      historyNote.setId( historyNoteId );
      historyNote.setUserId( userId );
      historyNote.setWorkPackageId( wpId );

      historyNote.setNote( "The Work package has been created." );
      historyNote.setStatusCode( "ACTV" );

      return historyNote;
   }


   private HistoryNote buildHistoryNoteToCreate() {
      HistoryNote historyNote = new HistoryNote();
      historyNote.setNote( "This is a test note" );
      historyNote.setWorkPackageId( wpId );
      historyNote.setStageReasonCode( "OBSOLETE" );
      historyNote.setStatusCode( "CANCEL" );
      historyNote.setDate( new Date() );

      return historyNote;
   }
}
