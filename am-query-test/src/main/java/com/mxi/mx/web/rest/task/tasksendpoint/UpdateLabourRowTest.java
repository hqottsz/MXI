package com.mxi.mx.web.rest.task.tasksendpoint;

import static com.mxi.mx.core.key.RefLabourRoleTypeKey.TECH;
import static com.mxi.mx.core.key.RefLabourSkillKey.ENG;
import static com.mxi.mx.web.rest.task.TasksEndpoint.ACTION_EDIT_LABOUR_HISTORY;
import static com.mxi.mx.web.rest.task.TasksEndpoint.ACTION_EDIT_LABOUR_REQUIREMENT;
import static java.math.BigDecimal.TEN;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.UUID;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.SchedLabourKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.user.UserService;
import com.mxi.mx.core.table.sched.JdbcSchedStaskDao;
import com.mxi.mx.core.unittest.table.sched.SchedLabourRole;
import com.mxi.mx.domain.task.TaskLabourService;
import com.mxi.mx.web.rest.Authorizer;
import com.mxi.mx.web.rest.task.TasksEndpoint;


/**
 * Integration unit tests for the behaviours of
 * {@link TasksEndpoint#updateLabourRow(SecurityContext, String, String, String)}
 *
 * The boundaries for these tests are the TasksEndpoint methods and the DB. Thus exercising the
 * integration of all the code between those boundaries. Note that the authorization validation is
 * mocked and thus excluded from these tests. However, the resulting behaviours regarding the
 * success and failure of that validation are exercised.
 *
 */
@SuppressWarnings( "deprecation" )
@RunWith( MockitoJUnitRunner.class )
public class UpdateLabourRowTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Mock
   private Authorizer mockAuthorizer;

   @Mock
   private SecurityContext mockSecurityContext;

   @Mock
   private Principal mockPrincipal;

   private static final String VALID_TASK_ID = UUID.randomUUID().toString();
   private static final String VALID_LABOUR_ROW_ID = UUID.randomUUID().toString();

   private static final String VALID_JSON_FOR_UPDATE_SCHEDULED_HOURS = "{\"scheduledHours\": 100}";
   private static final String VALID_JSON_FOR_UPDATE_ACTUAL_HOURS = "{\"actualHours\": 100}";

   private static final String USERNAME = "USERNAME";

   private TasksEndpoint tasksEndpoint;


   @Before
   public void before() throws Exception {
      // In order to control the data in the DB, any rows that exist in the sched_stask will be
      // deleted. (e.g. delete any 0-level data)
      MxDataAccess.getInstance().executeDelete( "sched_stask", null );

      // Most tests will require the authorization to be valid.
      // Those that do not can simply overwrite this mocked behaviour.
      when( mockAuthorizer.validate( any( SecurityContext.class ), any( String.class ) ) )
            .thenReturn( true );

      // Create a user and have the security context return that user as the principle user.
      Domain.createHumanResource( hr -> {
         hr.setUsername( USERNAME );
      } );
      when( mockSecurityContext.getUserPrincipal() ).thenReturn( mockPrincipal );
      when( mockPrincipal.getName() ).thenReturn( USERNAME );

      // Unit under test.
      tasksEndpoint = new TasksEndpoint();
      tasksEndpoint.setAuthorizer( mockAuthorizer );
      tasksEndpoint.setLabourService( new TaskLabourService() );
      tasksEndpoint.setUserService( new UserService() );
   }


   /**
    * Verify that {@link TasksEndpoint#updateLabourRow} responds with status of BAD_REQUEST when
    * data is null.
    *
    * @throws Exception
    */
   @Test
   public void respondsWithBadRequestWhenDataIsNull() throws Exception {

      // When updateLabourRow is called and data is null.
      String data = null;
      Response response = tasksEndpoint.updateLabourRow( mockSecurityContext, VALID_TASK_ID,
            VALID_LABOUR_ROW_ID, data );

      // Then the response status is returned as FORBIDDEN.
      assertThat( "Unexpected response status.", response.getStatusInfo(),
            is( Status.BAD_REQUEST ) );
   }


   /**
    * Verify that {@link TasksEndpoint#updateLabourRow} responds with status of BAD_REQUEST when
    * data has invalid format.
    */
   @Test
   public void respondsWithBadRequestWhenDataHasInvalidFormat() throws Exception {

      // When updateLabourRow is called and data has an invalid format (not json).
      String data = "hello";
      Response response = tasksEndpoint.updateLabourRow( mockSecurityContext, VALID_TASK_ID,
            VALID_LABOUR_ROW_ID, data );

      // Then the response status is returned as FORBIDDEN.
      assertThat( "Unexpected response status.", response.getStatusInfo(),
            is( Status.BAD_REQUEST ) );
   }


   /**
    * Verify that {@link TasksEndpoint#updateLabourRow} responds with status of BAD_REQUEST when
    * labourRowId is null.
    */
   @Test
   public void respondsWithBadRequestWhenLabourRowIdIsNull() throws Exception {

      // When updateLabourRow is called with a null labourRowId.
      String invalidLabourRowId = null;
      Response response = tasksEndpoint.updateLabourRow( mockSecurityContext, VALID_TASK_ID,
            invalidLabourRowId, VALID_JSON_FOR_UPDATE_SCHEDULED_HOURS );

      // Then the response status is returned as OK.
      assertThat( "Unexpected response status.", response.getStatusInfo(),
            is( Status.BAD_REQUEST ) );
   }


   /**
    * Verify that {@link TasksEndpoint#updateLabourRow} responds with status of BAD_REQUEST when
    * labourRowId is blank.
    */
   @Test
   public void respondsWithBadRequestWhenLabourRowIdIsBlank() throws Exception {

      // When updateLabourRow is called with a blank labourRowId.
      String invalidLabourRowId = "";
      Response response = tasksEndpoint.updateLabourRow( mockSecurityContext, VALID_TASK_ID,
            invalidLabourRowId, VALID_JSON_FOR_UPDATE_ACTUAL_HOURS );

      // Then the response status is returned as OK.
      assertThat( "Unexpected response status.", response.getStatusInfo(),
            is( Status.BAD_REQUEST ) );
   }


   /**
    * Verify that {@link TasksEndpoint#updateLabourRow} responds with status of BAD_REQUEST when
    * labourRowId is not a valid uuid.
    */
   @Test
   public void respondsWithBadRequestWhenLabourRowIdIsNotValidUuid() throws Exception {

      // When updateLabourRow is called with an invalid labourRowId.
      String invalidLabourRowId = "hello";
      Response response = tasksEndpoint.updateLabourRow( mockSecurityContext, VALID_TASK_ID,
            invalidLabourRowId, VALID_JSON_FOR_UPDATE_ACTUAL_HOURS );

      // Then the response status is returned as OK.
      assertThat( "Unexpected response status.", response.getStatusInfo(),
            is( Status.BAD_REQUEST ) );
   }


   /**
    * Verify that {@link TasksEndpoint#updateLabourRow} responds with status of BAD_REQUEST when
    * labourRowId does not exist.
    */
   @Test
   public void respondsWithBadRequestWhenLabourRowIdDoesNotExist() throws Exception {

      // Given no task, thus no labour row.
      // When updateLabourRow is called with a labourRowId that does not exist.
      String nonExistingLabourRowId = UUID.randomUUID().toString();
      Response response = tasksEndpoint.updateLabourRow( mockSecurityContext, VALID_TASK_ID,
            nonExistingLabourRowId, VALID_JSON_FOR_UPDATE_ACTUAL_HOURS );

      // Then the response status is returned as OK.
      assertThat( "Unexpected response status.", response.getStatusInfo(),
            is( Status.BAD_REQUEST ) );
   }


   /**************************************************************************************************
    *
    * Tests for updating scheduled hours.
    *
    **************************************************************************************************/

   /**
    * Verify that {@link TasksEndpoint#updateLabourRow} responds with status of OK when valid
    * labourRowId provided and when data contains valid scheduledHours.
    */
   @Test
   public void respondsWithOkWhenValidLabourRowIdAndDataContainsScheduledHours() throws Exception {

      // Given the user is authorized to edit labour requirement.
      setupAuthorizedUserForAction( ACTION_EDIT_LABOUR_REQUIREMENT );

      // Given a task with a labour row.
      TaskKey task = Domain.createAdhocTask( tsk -> {
         tsk.addLabour( labour -> {
            labour.setSkill( ENG );
            labour.setTechnicianRole( tech -> tech.setScheduledHours( TEN ) );
         } );
      } );
      SchedLabourKey labour = Domain.readLabourRequirement( task, ENG, TEN );

      String taskId = readTaskId( task );
      String labourRowId = readLabourRowId( labour );

      // When updateLabourRow is called with a valid labourRowId and the data contains valid
      // scheduledHours.
      String data = "{\"scheduledHours\": \"123.456\"}";
      Response response =
            tasksEndpoint.updateLabourRow( mockSecurityContext, taskId, labourRowId, data );

      // Then the response status is returned as OK.
      assertThat( "Unexpected response status.", response.getStatusInfo(), is( Status.OK ) );
   }


   /**
    * Verify that {@link TasksEndpoint#updateLabourRow} updates the scheduled hours when valid
    * labourRowId provided and when data contains valid scheduledHours.
    */
   @Test
   public void updatesScheduledHoursWhenValidLabourRowIdAndDataContainsScheduledHours()
         throws Exception {

      // Given the user is authorized to edit labour requirement.
      setupAuthorizedUserForAction( ACTION_EDIT_LABOUR_REQUIREMENT );

      // Given a task with a labour row.
      TaskKey task = Domain.createAdhocTask( tsk -> {
         tsk.addLabour( labour -> {
            labour.setSkill( ENG );
            labour.setTechnicianRole( tech -> tech.setScheduledHours( TEN ) );
         } );
      } );
      SchedLabourKey labour = Domain.readLabourRequirement( task, ENG, TEN );

      String taskId = readTaskId( task );
      String labourRowId = readLabourRowId( labour );

      // When updateLabourRow is called with a valid labourRowId and the data contains valid
      // scheduledHours.
      double newScheduledHours = 123.456;
      String data = "{\"scheduledHours\": " + Double.toString( newScheduledHours ) + "}";
      tasksEndpoint.updateLabourRow( mockSecurityContext, taskId, labourRowId, data );

      // Then the the scheduled hours is updated.
      Double actualSchedHours = SchedLabourRole.findByForeignKey( labour, TECH ).getSchedHours();
      assertThat( "Unexpected scheduled hours.", actualSchedHours, is( newScheduledHours ) );
   }


   /**
    * Verify that {@link TasksEndpoint#updateLabourRow} responds with status of BAD_REQUEST when
    * data contains negative scheduledHours.
    */
   @Test
   public void respondsWithBadRequestWhenDataContainsNegativeScheduledHours() throws Exception {

      // Given the user is authorized to edit labour requirement.
      setupAuthorizedUserForAction( ACTION_EDIT_LABOUR_REQUIREMENT );

      // Given a task with a labour row.
      TaskKey task = Domain.createAdhocTask( tsk -> {
         tsk.addLabour( labour -> {
            labour.setSkill( ENG );
            labour.setTechnicianRole( tech -> tech.setScheduledHours( TEN ) );
         } );
      } );
      SchedLabourKey labour = Domain.readLabourRequirement( task, ENG, TEN );

      String taskId = readTaskId( task );
      String labourRowId = readLabourRowId( labour );

      // When updateLabourRow is called with a valid labourRowId and the data contains negative
      // scheduledHours.
      String data = "{\"scheduledHours\": \"-100\"}";
      Response response =
            tasksEndpoint.updateLabourRow( mockSecurityContext, taskId, labourRowId, data );

      // Then the response status is returned as BAD_REQUEST.
      assertThat( "Unexpected response status.", response.getStatusInfo(),
            is( Status.BAD_REQUEST ) );
   }


   /**
    * Verify that {@link TasksEndpoint#updateLabourRow} responds with status of BAD_REQUEST when
    * data contains invalid scheduledHours.
    */
   @Test
   public void respondsWithBadRequestWhenDataContainsInvalidScheduledHours() throws Exception {

      // When updateLabourRow is called and data has an invalid scheduledHours value.
      String data = "{\"scheduledHours\": \"hello\"}";
      Response response = tasksEndpoint.updateLabourRow( mockSecurityContext, VALID_TASK_ID,
            VALID_LABOUR_ROW_ID, data );

      // Then the response status is returned as BAD_REQUEST.
      assertThat( "Unexpected response status.", response.getStatusInfo(),
            is( Status.BAD_REQUEST ) );
   }


   /**
    * Verify that {@link TasksEndpoint#updateLabourRow} responds with status of FORBIDDEN when data
    * contains scheduledHours but user is not authorized to edit labour requirement.
    */
   @Test
   public void respondsWithForbiddenWhenDataContainsScheduledHoursButUserIsNotAuthorized()
         throws Exception {

      // Given the user is not authorized to edit labour requirement.
      when( mockAuthorizer.validate( any( SecurityContext.class ),
            eq( ACTION_EDIT_LABOUR_REQUIREMENT ) ) ).thenReturn( false );

      // Given a task with a labour row.
      TaskKey task = Domain.createAdhocTask( tsk -> {
         tsk.addLabour( labour -> {
            labour.setSkill( ENG );
            labour.setTechnicianRole( tech -> tech.setScheduledHours( TEN ) );
         } );
      } );
      SchedLabourKey labour = Domain.readLabourRequirement( task, ENG, TEN );

      String taskId = readTaskId( task );
      String labourRowId = readLabourRowId( labour );

      // When updateLabourRow is called and data has valid scheduledHours value.
      Response response = tasksEndpoint.updateLabourRow( mockSecurityContext, taskId, labourRowId,
            VALID_JSON_FOR_UPDATE_SCHEDULED_HOURS );

      // Then the response status is returned as FORBIDDEN.
      assertThat( "Unexpected response status.", response.getStatusInfo(), is( Status.FORBIDDEN ) );
   }


   /**************************************************************************************************
    *
    * Tests for updating actual hours.
    *
    **************************************************************************************************/

   /**
    * Verify that {@link TasksEndpoint#updateLabourRow} responds with status of OK when valid
    * labourRowId provided and when data contains valid actualHours.
    */
   @Test
   public void respondsWithOkWhenValidLabourRowIdAndDataContainsActualHours() throws Exception {

      // Given the user is authorized to edit labour requirement.
      setupAuthorizedUserForAction( ACTION_EDIT_LABOUR_HISTORY );

      // Given a task with a labour row.
      TaskKey task = Domain.createAdhocTask( tsk -> {
         tsk.addLabour( labour -> {
            labour.setSkill( ENG );
            labour.setTechnicianRole( tech -> tech.setScheduledHours( TEN ) );
         } );
      } );
      SchedLabourKey labour = Domain.readLabourRequirement( task, ENG, TEN );

      String taskId = readTaskId( task );
      String labourRowId = readLabourRowId( labour );

      // When updateLabourRow is called with a valid labourRowId and the data contains valid
      // actualHours.
      String data = "{\"actualHours\": \"123.456\"}";
      Response response =
            tasksEndpoint.updateLabourRow( mockSecurityContext, taskId, labourRowId, data );

      // Then the response status is returned as OK.
      assertThat( "Unexpected response status.", response.getStatusInfo(), is( Status.OK ) );
   }


   /**
    * Verify that {@link TasksEndpoint#updateLabourRow} updates the actual hours when valid
    * labourRowId provided and when data contains valid actualHours.
    */
   @Test
   public void updatesActualHoursWhenValidlabourRowIdAndDataContainsActualHours() throws Exception {

      // Given the user is authorized to edit labour requirement.
      setupAuthorizedUserForAction( ACTION_EDIT_LABOUR_HISTORY );

      // Given a task with a labour row.
      TaskKey task = Domain.createAdhocTask( tsk -> {
         tsk.addLabour( labour -> {
            labour.setSkill( ENG );
            labour.setTechnicianRole( tech -> tech.setScheduledHours( TEN ) );
         } );
      } );
      SchedLabourKey labour = Domain.readLabourRequirement( task, ENG, TEN );

      String taskId = readTaskId( task );
      String labourRowId = readLabourRowId( labour );

      // When updateLabourRow is called with a valid labourRowId and the data contains valid
      // actualHours.
      double newActualHours = 123.456;
      String data = "{\"actualHours\": " + Double.toString( newActualHours ) + "}";
      tasksEndpoint.updateLabourRow( mockSecurityContext, taskId, labourRowId, data );

      // Then the the actual hours is updated.
      Double actualHours = SchedLabourRole.findByForeignKey( labour, TECH ).getActualHours();
      assertThat( "Unexpected actual hours.", actualHours, is( newActualHours ) );
   }


   /**
    * Verify that {@link TasksEndpoint#updateLabourRow} responds with status of BAD_REQUEST when
    * valid labourRowId provided and when data contains negative actualHours.
    */
   @Test
   public void respondsWithBadRequestWhenValidLabourRowIdAndDataContainsNegativeActualHours()
         throws Exception {

      // Given the user is authorized to edit labour requirement.
      setupAuthorizedUserForAction( ACTION_EDIT_LABOUR_HISTORY );

      // Given a task with a labour row.
      TaskKey task = Domain.createAdhocTask( tsk -> {
         tsk.addLabour( labour -> {
            labour.setSkill( ENG );
            labour.setTechnicianRole( tech -> tech.setScheduledHours( TEN ) );
         } );
      } );
      SchedLabourKey labour = Domain.readLabourRequirement( task, ENG, TEN );

      String taskId = readTaskId( task );
      String labourRowId = readLabourRowId( labour );

      // When updateLabourRow is called with a valid labourRowId and the data contains valid
      // actualHours.
      String data = "{\"actualHours\": \"-987\"}";
      Response response =
            tasksEndpoint.updateLabourRow( mockSecurityContext, taskId, labourRowId, data );

      // Then the response status is returned as BAD_REQUEST.
      assertThat( "Unexpected response status.", response.getStatusInfo(),
            is( Status.BAD_REQUEST ) );
   }


   /**
    * Verify that {@link TasksEndpoint#updateLabourRow} responds with status of BAD_REQUEST when
    * data contains invalid actualHours.
    */
   @Test
   public void respondsWithBadRequestWhenDataContainsInvalidActualHours() throws Exception {

      // When updateLabourRow is called and data has an invalid actualHours value.
      String data = "{\"actualHours\": \"hello\"}";
      Response response = tasksEndpoint.updateLabourRow( mockSecurityContext, VALID_TASK_ID,
            VALID_LABOUR_ROW_ID, data );

      // Then the response status is returned as FORBIDDEN.
      assertThat( "Unexpected response status.", response.getStatusInfo(),
            is( Status.BAD_REQUEST ) );
   }


   /**
    * Verify that {@link TasksEndpoint#updateLabourRow} responds with status of FORBIDDEN when data
    * contains actualHours but user is not authorized to edit labour requirement.
    */
   @Test
   public void respondsWithForbiddenWhenDataContainsActualHoursButUserIsNotAuthorized()
         throws Exception {

      // Given the user is not authorized to edit labour requirement.
      when( mockAuthorizer.validate( any( SecurityContext.class ),
            eq( ACTION_EDIT_LABOUR_HISTORY ) ) ).thenReturn( false );

      // Given a task with a labour row.
      TaskKey task = Domain.createAdhocTask( tsk -> {
         tsk.addLabour( labour -> {
            labour.setSkill( ENG );
            labour.setTechnicianRole( tech -> tech.setScheduledHours( TEN ) );
         } );
      } );
      SchedLabourKey labour = Domain.readLabourRequirement( task, ENG, TEN );

      String taskId = readTaskId( task );
      String labourRowId = readLabourRowId( labour );

      // When updateLabourRow is called and data has valid actualHours value.
      Response response = tasksEndpoint.updateLabourRow( mockSecurityContext, taskId, labourRowId,
            VALID_JSON_FOR_UPDATE_ACTUAL_HOURS );

      // Then the response status is returned as FORBIDDEN.
      assertThat( "Unexpected response status.", response.getStatusInfo(), is( Status.FORBIDDEN ) );
   }


   private void setupAuthorizedUserForAction( String action ) {
      when( mockAuthorizer.validate( any( SecurityContext.class ), eq( action ) ) )
            .thenReturn( true );
   }


   private String readTaskId( TaskKey task ) {
      return new JdbcSchedStaskDao().findByPrimaryKey( task ).getAlternateKey().toString();
   }


   private String readLabourRowId( SchedLabourKey labour ) {
      return SchedLabourRole.findByForeignKey( labour, TECH ).getAlternateKey().toString();
   }
}
