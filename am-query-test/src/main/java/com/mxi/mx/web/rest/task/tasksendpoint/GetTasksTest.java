package com.mxi.mx.web.rest.task.tasksendpoint;

import static com.mxi.mx.common.utils.DateUtils.addDays;
import static com.mxi.mx.common.utils.DateUtils.addMinutes;
import static com.mxi.mx.common.utils.DateUtils.floorSecond;
import static com.mxi.mx.common.utils.DateUtils.toDefaultDateTimeString;
import static com.mxi.mx.core.key.LocationKey.OPS;
import static com.mxi.mx.core.key.RefEventStatusKey.ACARCHIVE;
import static com.mxi.mx.core.key.RefLabourSkillKey.ENG;
import static com.mxi.mx.core.key.RefLabourSkillKey.LBR;
import static com.mxi.mx.core.key.RefLabourSkillKey.PILOT;
import static com.mxi.mx.core.key.RefLabourStageKey.ACTV;
import static com.mxi.mx.core.key.RefStageReasonKey.DEADEXT;
import static com.mxi.mx.core.key.RefTaskClassKey.AD;
import static com.mxi.mx.core.key.RefTaskSubclassKey.MPCCLOSE;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.crypto.parameter.ParameterEncrypter;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.DepartmentKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefTaskPriorityKey;
import com.mxi.mx.core.key.SchedLabourKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.org.OrgWorkDept;
import com.mxi.mx.core.table.sched.SchedStaskDao;
import com.mxi.mx.web.rest.Authorizer;
import com.mxi.mx.web.rest.task.TasksEndpoint;
import com.mxi.mx.web.rest.task.TasksEndpointParameters;
import com.mxi.mx.web.rest.task.TasksResponse;


/**
 * Integration unit tests for the behaviours of
 * {@link TasksEndpoint#getTasks(SecurityContext, com.mxi.mx.web.rest.task.TasksEndpointParameters)}
 *
 * The boundaries for these tests are the TasksEndpoint methods and the DB. Thus exercising the
 * integration of all the code between those boundaries. Note that the authorization validation is
 * mocked and thus excluded from these tests. However, the resulting behaviours regarding the
 * success and failure of that validation are exercised.
 *
 */
@SuppressWarnings( "deprecation" )
@RunWith( MockitoJUnitRunner.class )
public class GetTasksTest {

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

   private static final String USERNAME = "USERNAME";
   private HumanResourceKey user;

   private static final String TASK_DESCRIPTION = "TASK_DESCRIPTION";
   private static final RefTaskPriorityKey TASK_PRIORITY = new RefTaskPriorityKey( 1, "PRIORITY" );
   private static final Date SCHEDULED_START_DATE = new Date();
   private static final Date ACTUAL_START_DATE = DateUtils.addHours( SCHEDULED_START_DATE, 2 );
   private static final BigDecimal SCHEDULED_WORK_HOURS = BigDecimal.valueOf( 1.2 );
   private static final BigDecimal ACTUAL_WORK_HOURS = BigDecimal.valueOf( 3.4 );
   private static final String AIRCRAFT_DESCRIPTION = "AIRCRAFT_DESCRIPTION";
   private static final String WORKPACKAGE_NAME = "WORKPACKAGE_NAME";
   private static final BigDecimal JOB_STOPPED_SCHEDULED_WORK_HOURS = BigDecimal.valueOf( 5.6 );
   private static final String FIRST_NAME = "FIRST_NAME";
   private static final String LAST_NAME = "LAST_NAME";
   private static final RefLabourSkillKey SKILL_1 = new RefLabourSkillKey( 1, "ONE" );

   private static final BigDecimal SCHEDULED_WORK_HOURS_ONE = BigDecimal.valueOf( 1.0 );
   private static final BigDecimal SCHEDULED_WORK_HOURS_TWO = BigDecimal.valueOf( 2.0 );
   private static final BigDecimal SCHEDULED_WORK_HOURS_THREE = BigDecimal.valueOf( 3.0 );
   private static final BigDecimal SCHEDULED_WORK_HOURS_FOUR = BigDecimal.valueOf( 4.0 );

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
      user = Domain.createHumanResource( hr -> {
         hr.setUsername( USERNAME );
      } );
      when( mockSecurityContext.getUserPrincipal() ).thenReturn( mockPrincipal );
      when( mockPrincipal.getName() ).thenReturn( USERNAME );

      // Unit under test.
      tasksEndpoint = new TasksEndpoint();
      tasksEndpoint.setAuthorizer( mockAuthorizer );
   }


   /**
    * Verify that {@link TasksEndpoint#getTasks} responds with status of BAD_REQUEST when params is
    * null.
    */
   @Test
   public void respondsWithBadRequestWhenParmsIsNull() throws Exception {

      // When getTasks is called and parms is null.
      TasksEndpointParameters parms = null;
      Response response = tasksEndpoint.getTasks( mockSecurityContext, parms );

      // Then a FORBIDDEN response status is returned.
      assertThat( "Unexpected response status.", response.getStatusInfo(),
            is( Status.BAD_REQUEST ) );
   }


   /**
    * Verify that {@link TasksEndpoint#getTasks} responds with status of FORBIDDEN when user is not
    * authorized.
    */
   @Test
   public void respondsWithForbiddenWhenUserIsNotAuthorized() throws Exception {

      // Given the user is not authorized.
      when( mockAuthorizer.validate( any( SecurityContext.class ), any( String.class ) ) )
            .thenReturn( false );

      // When getTasks is called.
      Response response =
            tasksEndpoint.getTasks( mockSecurityContext, buildDefaultTasksEndpointParameters() );

      // Then a FORBIDDEN response status is returned.
      assertThat( "Unexpected response status.", response.getStatusInfo(), is( Status.FORBIDDEN ) );
   }


   /**
    * Verify that {@link TasksEndpoint#getTasks} responds with status of OK when user is authorized.
    */
   @Test
   public void respondsWithOkWhenUserIsAuthorized() throws Exception {

      // Given the user is authorized.
      when( mockAuthorizer.validate( any( SecurityContext.class ), any( String.class ) ) )
            .thenReturn( true );

      // When getTasks is called.
      Response response =
            tasksEndpoint.getTasks( mockSecurityContext, buildDefaultTasksEndpointParameters() );

      // Then an OK response status is returned.
      assertThat( "Unexpected response status.", response.getStatusInfo(), is( Status.OK ) );
   }


   /**
    * Verify that {@link TasksEndpoint#getTasks} responds with an empty list when user is authorized
    * but there are not tasks.
    */
   @SuppressWarnings( "unchecked" )
   @Test
   public void returnsEmptyListWhenUserIsAuthorizedButThereAreNoTasks() throws Exception {

      // Given the user is authorized.
      when( mockAuthorizer.validate( any( SecurityContext.class ), any( String.class ) ) )
            .thenReturn( true );

      // When getTasks is called.
      Response response =
            tasksEndpoint.getTasks( mockSecurityContext, buildDefaultTasksEndpointParameters() );

      // Then an empty list is returned in the response.
      List<TasksResponse> list = ( List<TasksResponse> ) response.getEntity();
      assertThat( "Unexpected list in response.", list.size(), is( 0 ) );
   }


   /**
    * Verify that {@link TasksEndpoint#getTasks} responds with matching tasks when crewId provided
    * and there are tasks assigned to the crew and the user belongs to the crew.
    */
   @SuppressWarnings( "unchecked" )
   @Test
   public void returnsCrewTasksWhenCrewIdProvidedAndTasksAssignedToCrewAndUserBelongsToCrew()
         throws Exception {

      // Given a crew to which the user belongs (the user that is setup within the security
      // context).
      DepartmentKey usersCrew = Domain.createCrew( crew -> {
         crew.addHumanResource( user );
      } );
      String usersCrewId = readCrewId( usersCrew );

      // Given another crew to which the user does not belong.
      DepartmentKey anotherCrew = Domain.createCrew();

      // Given tasks assigned to the user's crew.
      TaskKey task1 = Domain.createAdhocTask( task -> {
         task.setCrew( usersCrew );
      } );
      TaskKey task2 = Domain.createAdhocTask( task -> {
         task.setCrew( usersCrew );
      } );

      // Given tasks assigned to the other crew.
      Domain.createAdhocTask( task -> {
         task.setCrew( anotherCrew );
      } );
      Domain.createAdhocTask( task -> {
         task.setCrew( anotherCrew );
      } );

      // When getTasks is called and provided the crew id.
      TasksEndpointParameters parms = buildDefaultTasksEndpointParameters();
      parms.setAssignedToCrewId( usersCrewId );

      Response response = tasksEndpoint.getTasks( mockSecurityContext, parms );

      // Then a list is returned with the tasks assigned to the user's crew.
      List<TasksResponse> list = ( List<TasksResponse> ) response.getEntity();
      assertThat( "Unexpected size of list in response.", list.size(), is( 2 ) );

      List<String> actualTaskIds = new ArrayList<>();
      list.forEach( taskResponse -> {
         actualTaskIds.add( taskResponse.getTaskId() );
      } );

      List<String> expectedTaskIds = new ArrayList<>();
      expectedTaskIds.add( readTaskId( task1 ) );
      expectedTaskIds.add( readTaskId( task2 ) );

      assertThat( "Unexpected tasks in the response.", actualTaskIds,
            Matchers.containsInAnyOrder( expectedTaskIds.toArray() ) );
   }


   /**
    * Verify that {@link TasksEndpoint#getTasks} responds with tasks assigned to the provided crewId
    * when there are tasks assigned to that crew and there are tasks assigned to other crews and the
    * user belongs to all of the crews.
    */
   @SuppressWarnings( "unchecked" )
   @Test
   public void
         returnsCrewTasksWhenCrewIdProvidedAndTasksAssignedToManyCrewsAndUserBelongsToManyCrews()
               throws Exception {

      // Given many crews to which the user belongs (the user that is setup within the security
      // context).
      DepartmentKey usersCrew1 = Domain.createCrew( crew -> {
         crew.addHumanResource( user );
      } );
      String usersCrewId1 = readCrewId( usersCrew1 );
      DepartmentKey usersCrew2 = Domain.createCrew( crew -> {
         crew.addHumanResource( user );
      } );

      // Given another crew to which the user does not belong.
      DepartmentKey anotherCrew = Domain.createCrew();

      // Given tasks assigned to each of the user's crews.
      TaskKey task1 = Domain.createAdhocTask( task -> {
         task.setCrew( usersCrew1 );
      } );
      TaskKey task2 = Domain.createAdhocTask( task -> {
         task.setCrew( usersCrew1 );
      } );
      Domain.createAdhocTask( task -> {
         task.setCrew( usersCrew2 );
      } );
      Domain.createAdhocTask( task -> {
         task.setCrew( usersCrew2 );
      } );

      // Given a tasks assigned to the other crew.
      Domain.createAdhocTask( task -> {
         task.setCrew( anotherCrew );
      } );
      Domain.createAdhocTask( task -> {
         task.setCrew( anotherCrew );
      } );

      // When getTasks is called and provided one of the crew ids.
      TasksEndpointParameters parms = buildDefaultTasksEndpointParameters();
      parms.setAssignedToCrewId( usersCrewId1 );

      Response response = tasksEndpoint.getTasks( mockSecurityContext, parms );

      // Then a list is returned with only the tasks assigned to the provided user's crew.
      List<TasksResponse> list = ( List<TasksResponse> ) response.getEntity();
      assertThat( "Unexpected size of list in response.", list.size(), is( 2 ) );

      List<String> actualTaskIds = new ArrayList<>();
      list.forEach( taskResponse -> {
         actualTaskIds.add( taskResponse.getTaskId() );
      } );

      List<String> expectedTaskIds = new ArrayList<>();
      expectedTaskIds.add( readTaskId( task1 ) );
      expectedTaskIds.add( readTaskId( task2 ) );

      assertThat( "Unexpected tasks in the response.", actualTaskIds,
            Matchers.containsInAnyOrder( expectedTaskIds.toArray() ) );
   }


   /**
    * Verify that {@link TasksEndpoint#getTasks} responds with an empty list when crewId provided
    * and there are tasks assigned to the crew but the user belongs to another the crew.
    */
   @SuppressWarnings( "unchecked" )
   @Test
   public void returnsNoTasksWhenCrewIdProvidedAndTasksAssignedCrewButUserDoesNotBelongToCrew()
         throws Exception {

      // Given a crews to which the user does not belong.
      DepartmentKey crew1 = Domain.createCrew();
      String crewId1 = readCrewId( crew1 );

      // Given a crews to which the user does belong.
      DepartmentKey usersCrew = Domain.createCrew( crew -> {
         crew.addHumanResource( user );
      } );

      // Given tasks assigned to each of the crews.
      Domain.createAdhocTask( task -> {
         task.setCrew( crew1 );
      } );
      Domain.createAdhocTask( task -> {
         task.setCrew( crew1 );
      } );
      Domain.createAdhocTask( task -> {
         task.setCrew( usersCrew );
      } );
      Domain.createAdhocTask( task -> {
         task.setCrew( usersCrew );
      } );

      // When getTasks is called and provided the crew id to which the user does not belong.
      TasksEndpointParameters parms = buildDefaultTasksEndpointParameters();
      parms.setAssignedToCrewId( crewId1 );

      Response response = tasksEndpoint.getTasks( mockSecurityContext, parms );

      // Then an empty list is returned.
      List<TasksResponse> list = ( List<TasksResponse> ) response.getEntity();
      assertThat( "Unexpected size of list in response.", list.size(), is( 0 ) );
   }


   /**
    * Verify that {@link TasksEndpoint#getTasks} responds with a list of tasks whose scheduled start
    * date is equal to or after the provided minimum scheduled start date.
    */
   @SuppressWarnings( "unchecked" )
   @Test
   public void returnsTasksWhoseSchedStartIsSameOrAfterMinSchedStart() throws Exception {

      // Given a minimum scheduled start date.
      Date minSchedStart = floorSecond( new Date() );

      // Given tasks whose scheduled start date is before the minimum scheduled start date.
      Domain.createRequirement( task -> {
         task.setScheduledStartDate( addMinutes( minSchedStart, -1 ) );
      } );
      Domain.createRequirement( task -> {
         task.setScheduledStartDate( addDays( minSchedStart, -1 ) );
      } );

      // Given tasks whose scheduled start date is the same as the minimum scheduled start date.
      TaskKey sameTask1 = Domain.createRequirement( task -> {
         task.setScheduledStartDate( minSchedStart );
      } );
      TaskKey sameTask2 = Domain.createRequirement( task -> {
         task.setScheduledStartDate( minSchedStart );
      } );

      // Given tasks whose scheduled start date is after the minimum scheduled start date.
      TaskKey afterTask1 = Domain.createRequirement( task -> {
         task.setScheduledStartDate( addMinutes( minSchedStart, +1 ) );
      } );
      TaskKey afterTask2 = Domain.createRequirement( task -> {
         task.setScheduledStartDate( addDays( minSchedStart, +1 ) );
      } );

      // When getTasks is called and provided a minimum scheduled start date.
      TasksEndpointParameters parms = buildDefaultTasksEndpointParameters();
      parms.setMinScheduledStartDateTime( toDefaultDateTimeString( minSchedStart ) );

      Response response = tasksEndpoint.getTasks( mockSecurityContext, parms );

      // Then a list is returned with only the tasks whose scheduled start date is the same or is
      // after the
      // provided minimum scheduled start date.
      List<TasksResponse> list = ( List<TasksResponse> ) response.getEntity();
      assertThat( "Unexpected size of list in response.", list.size(), is( 4 ) );

      List<String> actualTaskIds = new ArrayList<>();
      list.forEach( taskResponse -> {
         actualTaskIds.add( taskResponse.getTaskId() );
      } );

      List<String> expectedTaskIds = new ArrayList<>();
      expectedTaskIds.add( readTaskId( afterTask1 ) );
      expectedTaskIds.add( readTaskId( afterTask2 ) );
      expectedTaskIds.add( readTaskId( sameTask1 ) );
      expectedTaskIds.add( readTaskId( sameTask2 ) );

      assertThat( "Unexpected tasks in the response.", actualTaskIds,
            Matchers.containsInAnyOrder( expectedTaskIds.toArray() ) );
   }


   /**
    * Verify that {@link TasksEndpoint#getTasks} responds with a list of tasks whose scheduled start
    * date is before the provided maximum scheduled start date.
    */
   @SuppressWarnings( "unchecked" )
   @Test
   public void returnsTasksWhoseSchedStartIsBeforeMaxSchedStart() throws Exception {

      // Given a maximum scheduled start date.
      Date maxSchedStart = floorSecond( new Date() );

      // Given tasks whose scheduled start date is before the maximum scheduled start date.
      TaskKey task1 = Domain.createRequirement( task -> {
         task.setScheduledStartDate( addMinutes( maxSchedStart, -1 ) );
      } );
      TaskKey task2 = Domain.createRequirement( task -> {
         task.setScheduledStartDate( addDays( maxSchedStart, -1 ) );
      } );

      // Given tasks whose scheduled start date is after the maximum scheduled start date.
      Domain.createRequirement( task -> {
         task.setScheduledStartDate( addMinutes( maxSchedStart, +1 ) );
      } );
      Domain.createRequirement( task -> {
         task.setScheduledStartDate( addDays( maxSchedStart, +1 ) );
      } );

      // When getTasks is called and provided a maximum scheduled start date.
      TasksEndpointParameters parms = buildDefaultTasksEndpointParameters();
      parms.setMaxScheduledStartDateTime( toDefaultDateTimeString( maxSchedStart ) );

      Response response = tasksEndpoint.getTasks( mockSecurityContext, parms );

      // Then a list is returned with only the tasks whose scheduled start date is before the
      // provided maximum scheduled start date.
      List<TasksResponse> list = ( List<TasksResponse> ) response.getEntity();
      assertThat( "Unexpected size of list in response.", list.size(), is( 2 ) );

      List<String> actualTaskIds = new ArrayList<>();
      list.forEach( taskResponse -> {
         actualTaskIds.add( taskResponse.getTaskId() );
      } );

      List<String> expectedTaskIds = new ArrayList<>();
      expectedTaskIds.add( readTaskId( task1 ) );
      expectedTaskIds.add( readTaskId( task2 ) );

      assertThat( "Unexpected tasks in the response.", actualTaskIds,
            Matchers.containsInAnyOrder( expectedTaskIds.toArray() ) );
   }


   /**
    * Verify that {@link TasksEndpoint#getTasks} responds with a list of tasks whose scheduled start
    * date is the same as the provided maximum scheduled start date.
    */
   @SuppressWarnings( "unchecked" )
   @Test
   public void returnsTasksWhoseSchedStartIsSameAsMaxSchedStart() throws Exception {

      // Given a maximum scheduled start date.
      Date maxSchedStart = floorSecond( new Date() );

      // Given tasks whose scheduled start date is the same as the maximum scheduled start date.
      TaskKey task1 = Domain.createRequirement( task -> {
         task.setScheduledStartDate( maxSchedStart );
      } );
      TaskKey task2 = Domain.createRequirement( task -> {
         task.setScheduledStartDate( maxSchedStart );
      } );

      // Given tasks whose scheduled start date is after the maximum scheduled start date.
      Domain.createRequirement( task -> {
         task.setScheduledStartDate( addMinutes( maxSchedStart, +1 ) );
      } );
      Domain.createRequirement( task -> {
         task.setScheduledStartDate( addDays( maxSchedStart, +1 ) );
      } );

      // When getTasks is called and provided a maximum scheduled start date.
      TasksEndpointParameters parms = buildDefaultTasksEndpointParameters();
      parms.setMaxScheduledStartDateTime( toDefaultDateTimeString( maxSchedStart ) );

      Response response = tasksEndpoint.getTasks( mockSecurityContext, parms );

      // Then a list is returned with only the tasks whose scheduled start date is the same as the
      // provided maximum scheduled start date.
      List<TasksResponse> list = ( List<TasksResponse> ) response.getEntity();
      assertThat( "Unexpected size of list in response.", list.size(), is( 2 ) );

      List<String> actualTaskIds = new ArrayList<>();
      list.forEach( taskResponse -> {
         actualTaskIds.add( taskResponse.getTaskId() );
      } );

      List<String> expectedTaskIds = new ArrayList<>();
      expectedTaskIds.add( readTaskId( task1 ) );
      expectedTaskIds.add( readTaskId( task2 ) );

      assertThat( "Unexpected tasks in the response.", actualTaskIds,
            Matchers.containsInAnyOrder( expectedTaskIds.toArray() ) );
   }


   /**
    * Verify that {@link TasksEndpoint#getTasks} responds with a list of tasks whose scheduled start
    * date is after the provided minimum scheduled start date.
    */
   @SuppressWarnings( "unchecked" )
   @Test
   public void returnsTasksWhoseSchedStartIsAfterMinSchedStart() throws Exception {

      // Given a minimum scheduled start date.
      Date minSchedStart = new Date();

      // Given tasks whose scheduled start date is before the minimum scheduled start date.
      Domain.createRequirement( task -> {
         task.setScheduledStartDate( addMinutes( minSchedStart, -1 ) );
      } );
      Domain.createRequirement( task -> {
         task.setScheduledStartDate( addDays( minSchedStart, -1 ) );
      } );

      // Given tasks whose scheduled start date is after the minimum scheduled start date.
      TaskKey task1 = Domain.createRequirement( task -> {
         task.setScheduledStartDate( addMinutes( minSchedStart, +1 ) );
      } );
      TaskKey task2 = Domain.createRequirement( task -> {
         task.setScheduledStartDate( addDays( minSchedStart, +1 ) );
      } );

      // When getTasks is called and provided a minimum scheduled start date.
      TasksEndpointParameters parms = buildDefaultTasksEndpointParameters();
      parms.setMinScheduledStartDateTime( toDefaultDateTimeString( minSchedStart ) );

      Response response = tasksEndpoint.getTasks( mockSecurityContext, parms );

      // Then a list is returned with only the tasks whose scheduled start date is after the
      // provided minimum scheduled start date.
      List<TasksResponse> list = ( List<TasksResponse> ) response.getEntity();
      assertThat( "Unexpected size of list in response.", list.size(), is( 2 ) );

      List<String> actualTaskIds = new ArrayList<>();
      list.forEach( taskResponse -> {
         actualTaskIds.add( taskResponse.getTaskId() );
      } );

      List<String> expectedTaskIds = new ArrayList<>();
      expectedTaskIds.add( readTaskId( task1 ) );
      expectedTaskIds.add( readTaskId( task2 ) );

      assertThat( "Unexpected tasks in the response.", actualTaskIds,
            Matchers.containsInAnyOrder( expectedTaskIds.toArray() ) );
   }


   /**
    * Verify that {@link TasksEndpoint#getTasks} responds with a list of tasks whose scheduled start
    * date is the same as the provided minimum scheduled start date.
    */
   @SuppressWarnings( "unchecked" )
   @Test
   public void returnsTasksWhoseSchedStartIsSameAsMinSchedStart() throws Exception {

      // Given a minimum scheduled start date.
      Date minSchedStart = new Date();

      // Given tasks whose scheduled start date is before the minimum scheduled start date.
      Domain.createRequirement( task -> {
         task.setScheduledStartDate( addMinutes( minSchedStart, -1 ) );
      } );
      Domain.createRequirement( task -> {
         task.setScheduledStartDate( addDays( minSchedStart, -1 ) );
      } );

      // Given tasks whose scheduled start date is the same as the minimum scheduled start date.
      TaskKey task1 = Domain.createRequirement( task -> {
         task.setScheduledStartDate( minSchedStart );
      } );
      TaskKey task2 = Domain.createRequirement( task -> {
         task.setScheduledStartDate( minSchedStart );
      } );

      // When getTasks is called and provided a minimum scheduled start date.
      TasksEndpointParameters parms = buildDefaultTasksEndpointParameters();
      parms.setMinScheduledStartDateTime( toDefaultDateTimeString( minSchedStart ) );

      Response response = tasksEndpoint.getTasks( mockSecurityContext, parms );

      // Then a list is returned with only the tasks whose scheduled start date is the same as the
      // provided minimum scheduled start date.
      List<TasksResponse> list = ( List<TasksResponse> ) response.getEntity();
      assertThat( "Unexpected size of list in response.", list.size(), is( 2 ) );

      List<String> actualTaskIds = new ArrayList<>();
      list.forEach( taskResponse -> {
         actualTaskIds.add( taskResponse.getTaskId() );
      } );

      List<String> expectedTaskIds = new ArrayList<>();
      expectedTaskIds.add( readTaskId( task1 ) );
      expectedTaskIds.add( readTaskId( task2 ) );

      assertThat( "Unexpected tasks in the response.", actualTaskIds,
            Matchers.containsInAnyOrder( expectedTaskIds.toArray() ) );
   }


   /**
    * Verify that {@link TasksEndpoint#getTasks} responds with a list of both completed and
    * non-completed tasks when the provided exclude-completed flag is false.
    */
   @SuppressWarnings( "unchecked" )
   @Test
   public void returnsCompetedAndNonCompletedTasksWhenExcludeCompletedIsFalse() throws Exception {

      // Given completed tasks.
      TaskKey task1 = Domain.createAdhocTask( task -> {
         task.setIsCompleted( true );
      } );
      TaskKey task2 = Domain.createAdhocTask( task -> {
         task.setIsCompleted( true );
      } );

      // Given non-completed tasks.
      TaskKey task3 = Domain.createAdhocTask( task -> {
         task.setIsCompleted( false );
      } );
      TaskKey task4 = Domain.createAdhocTask( task -> {
         task.setIsCompleted( false );
      } );

      // When getTasks is called and the provided exclude-completed flag is false.
      TasksEndpointParameters parms = buildDefaultTasksEndpointParameters();
      parms.setExcludeCompleted( false );

      Response response = tasksEndpoint.getTasks( mockSecurityContext, parms );

      // Then a list of all tasks is returned.
      List<TasksResponse> list = ( List<TasksResponse> ) response.getEntity();
      assertThat( "Unexpected size of list in response.", list.size(), is( 4 ) );

      List<String> actualTaskIds = new ArrayList<>();
      list.forEach( taskResponse -> {
         actualTaskIds.add( taskResponse.getTaskId() );
      } );

      List<String> expectedTaskIds = new ArrayList<>();
      expectedTaskIds.add( readTaskId( task1 ) );
      expectedTaskIds.add( readTaskId( task2 ) );
      expectedTaskIds.add( readTaskId( task3 ) );
      expectedTaskIds.add( readTaskId( task4 ) );

      assertThat( "Unexpected tasks in the response.", actualTaskIds,
            Matchers.containsInAnyOrder( expectedTaskIds.toArray() ) );
   }


   /**
    * Verify that {@link TasksEndpoint#getTasks} responds with a list of only non-completed tasks
    * when the provided exclude-completed flag is true.
    */
   @SuppressWarnings( "unchecked" )
   @Test
   public void returnsNonCompetedTasksWhenExcludeCompletedIsTrue() throws Exception {

      // Given completed tasks.
      Domain.createAdhocTask( task -> {
         task.setIsCompleted( true );
      } );
      Domain.createAdhocTask( task -> {
         task.setIsCompleted( true );
      } );

      // Given non-completed tasks.
      TaskKey task1 = Domain.createAdhocTask( task -> {
         task.setIsCompleted( false );
      } );
      TaskKey task2 = Domain.createAdhocTask( task -> {
         task.setIsCompleted( false );
      } );

      // When getTasks is called and the provided exclude-completed flag is false.
      TasksEndpointParameters parms = buildDefaultTasksEndpointParameters();
      parms.setExcludeCompleted( true );

      Response response = tasksEndpoint.getTasks( mockSecurityContext, parms );

      // Then a list of all tasks is returned.
      List<TasksResponse> list = ( List<TasksResponse> ) response.getEntity();
      assertThat( "Unexpected size of list in response.", list.size(), is( 2 ) );

      List<String> actualTaskIds = new ArrayList<>();
      list.forEach( taskResponse -> {
         actualTaskIds.add( taskResponse.getTaskId() );
      } );

      List<String> expectedTaskIds = new ArrayList<>();
      expectedTaskIds.add( readTaskId( task1 ) );
      expectedTaskIds.add( readTaskId( task2 ) );

      assertThat( "Unexpected tasks in the response.", actualTaskIds,
            Matchers.containsInAnyOrder( expectedTaskIds.toArray() ) );
   }


   /**
    * Verify that {@link TasksEndpoint#getTasks} responds with a list of only workscoped tasks when
    * the provided only-workscoped flag is true.
    */
   @SuppressWarnings( "unchecked" )
   @Test
   public void returnsWorkscopedTasksWhenOnlyWorkscopedIsTrue() throws Exception {

      // Given many tasks.
      TaskKey task1 = Domain.createAdhocTask();
      TaskKey task2 = Domain.createAdhocTask();
      TaskKey task3 = Domain.createAdhocTask();
      TaskKey task4 = Domain.createAdhocTask();

      // Given a work package with only some of the tasks assigned to its workscope.
      Domain.createWorkPackage( wp -> {
         wp.addTask( task1 );
         wp.addTask( task2 );
         wp.addTask( task3 );
         wp.addTask( task4 );
         wp.addWorkScopeTask( task1 );
         wp.addWorkScopeTask( task2 );
      } );

      // When getTasks is called and the provided only-workscoped flag is true.
      TasksEndpointParameters parms = buildDefaultTasksEndpointParameters();
      parms.setOnlyWorkscoped( true );

      Response response = tasksEndpoint.getTasks( mockSecurityContext, parms );

      // Then a list of only the workscoped tasks is returned.
      List<TasksResponse> list = ( List<TasksResponse> ) response.getEntity();
      assertThat( "Unexpected size of list in response.", list.size(), is( 2 ) );

      List<String> actualTaskIds = new ArrayList<>();
      list.forEach( taskResponse -> {
         actualTaskIds.add( taskResponse.getTaskId() );
      } );

      List<String> expectedTaskIds = new ArrayList<>();
      expectedTaskIds.add( readTaskId( task1 ) );
      expectedTaskIds.add( readTaskId( task2 ) );

      assertThat( "Unexpected tasks in the response.", actualTaskIds,
            Matchers.containsInAnyOrder( expectedTaskIds.toArray() ) );
   }


   /**
    * Verify that {@link TasksEndpoint#getTasks} responds with a list of all tasks when the provided
    * only-workscoped flag is false.
    */
   @SuppressWarnings( "unchecked" )
   @Test
   public void returnsAllTasksWhenOnlyWorkscopedIsFalse() throws Exception {

      // Given many tasks.
      TaskKey task1 = Domain.createAdhocTask();
      TaskKey task2 = Domain.createAdhocTask();
      TaskKey task3 = Domain.createAdhocTask();
      TaskKey task4 = Domain.createAdhocTask();

      // Given a work package with only some of the tasks assigned to its workscope.
      Domain.createWorkPackage( wp -> {
         wp.addTask( task1 );
         wp.addTask( task2 );
         wp.addTask( task3 );
         wp.addTask( task4 );
         wp.addWorkScopeTask( task1 );
         wp.addWorkScopeTask( task2 );
      } );

      // When getTasks is called and the provided only-workscoped flag is false.
      TasksEndpointParameters parms = buildDefaultTasksEndpointParameters();
      parms.setOnlyWorkscoped( false );

      Response response = tasksEndpoint.getTasks( mockSecurityContext, parms );

      // Then a list of all tasks is returned.
      List<TasksResponse> list = ( List<TasksResponse> ) response.getEntity();
      assertThat( "Unexpected size of list in response.", list.size(), is( 4 ) );

      List<String> actualTaskIds = new ArrayList<>();
      list.forEach( taskResponse -> {
         actualTaskIds.add( taskResponse.getTaskId() );
      } );

      List<String> expectedTaskIds = new ArrayList<>();
      expectedTaskIds.add( readTaskId( task1 ) );
      expectedTaskIds.add( readTaskId( task2 ) );
      expectedTaskIds.add( readTaskId( task3 ) );
      expectedTaskIds.add( readTaskId( task4 ) );

      assertThat( "Unexpected tasks in the response.", actualTaskIds,
            Matchers.containsInAnyOrder( expectedTaskIds.toArray() ) );
   }


   /**
    * Verify that {@link TasksEndpoint#getTasks} responds with a list of only tasks with labour when
    * the provided only-contains-labour flag is true.
    */
   @SuppressWarnings( "unchecked" )
   @Test
   public void returnsTasksWithLabourWhenOnlyContainsLabourIsTrue() throws Exception {

      // Given tasks with labour.
      TaskKey task1 = Domain.createAdhocTask( task -> {
         task.addLabour( lab -> {
            lab.setSkill( ENG );
         } );
      } );
      TaskKey task2 = Domain.createAdhocTask( task -> {
         task.addLabour( lab -> {
            lab.setSkill( PILOT );
         } );
      } );

      // Given tasks without labour.
      Domain.createAdhocTask();
      Domain.createAdhocTask();

      // When getTasks is called and provided only-contains-labour flag is true
      // (and the task key encryption name).
      TasksEndpointParameters parms = buildDefaultTasksEndpointParameters();
      parms.setOnlyContainingLabour( true );
      parms.setTaskKeyEncriptionName( "aTask" );

      Response response = tasksEndpoint.getTasks( mockSecurityContext, parms );

      // Then a list of only the tasks with labour is returned.
      List<TasksResponse> list = ( List<TasksResponse> ) response.getEntity();
      assertThat( "Unexpected size of list in response.", list.size(), is( 2 ) );

      List<String> actualTaskIds = new ArrayList<>();
      list.forEach( taskResponse -> {
         actualTaskIds.add( taskResponse.getTaskId() );
      } );

      List<String> expectedTaskIds = new ArrayList<>();
      expectedTaskIds.add( readTaskId( task1 ) );
      expectedTaskIds.add( readTaskId( task2 ) );

      assertThat( "Unexpected tasks in the response.", actualTaskIds,
            Matchers.containsInAnyOrder( expectedTaskIds.toArray() ) );
   }


   /**
    * Verify that {@link TasksEndpoint#getTasks} responds with a list of only tasks without labour
    * when the provided only-contains-labour flag is false.
    */
   @SuppressWarnings( "unchecked" )
   @Test
   public void returnsTasksWithoutLabourWhenOnlyContainsLabourIsFalse() throws Exception {

      // Given tasks with labour.
      TaskKey task1 = Domain.createAdhocTask( task -> {
         task.addLabour( lab -> {
            lab.setSkill( ENG );
         } );
      } );
      TaskKey task2 = Domain.createAdhocTask( task -> {
         task.addLabour( lab -> {
            lab.setSkill( PILOT );
         } );
      } );

      // Given tasks without labour.
      TaskKey task3 = Domain.createAdhocTask();
      TaskKey task4 = Domain.createAdhocTask();

      // When getTasks is called and the provided only-contains-labour flag is false.
      TasksEndpointParameters parms = buildDefaultTasksEndpointParameters();
      parms.setOnlyContainingLabour( false );

      Response response = tasksEndpoint.getTasks( mockSecurityContext, parms );

      // Then a list of all the tasks is returned.
      List<TasksResponse> list = ( List<TasksResponse> ) response.getEntity();
      assertThat( "Unexpected size of list in response.", list.size(), is( 4 ) );

      List<String> actualTaskIds = new ArrayList<>();
      list.forEach( taskResponse -> {
         actualTaskIds.add( taskResponse.getTaskId() );
      } );

      List<String> expectedTaskIds = new ArrayList<>();
      expectedTaskIds.add( readTaskId( task1 ) );
      expectedTaskIds.add( readTaskId( task2 ) );
      expectedTaskIds.add( readTaskId( task3 ) );
      expectedTaskIds.add( readTaskId( task4 ) );

      assertThat( "Unexpected tasks in the response.", actualTaskIds,
            Matchers.containsInAnyOrder( expectedTaskIds.toArray() ) );
   }


   /**
    * Verify that {@link TasksEndpoint#getTasks} responds with task information when returning
    * tasks.
    */
   @SuppressWarnings( "unchecked" )
   @Test
   public void returnsTaskInformation() throws Exception {

      // Given a task with information that is expected to be returned.
      TaskKey task = Domain.createRequirement( req -> {
         req.setDescription( TASK_DESCRIPTION );
         req.setStatus( ACARCHIVE );
         req.setTaskClass( AD );
         req.setTaskSubclass( MPCCLOSE );
         req.setPriority( TASK_PRIORITY );
         req.setScheduledStartDate( SCHEDULED_START_DATE );
         req.setActualStartDate( ACTUAL_START_DATE );
      } );
      String expectedTaskId = readTaskId( task );
      String expectedEncryptedTaskKey = encryptKeyString( task.toString() );

      // When getTasks is called without parameters.
      Response response =
            tasksEndpoint.getTasks( mockSecurityContext, buildDefaultTasksEndpointParameters() );

      // Then the response contains the task information.
      TasksResponse returnedTask = ( ( List<TasksResponse> ) response.getEntity() ).get( 0 );

      assertThat( "Unexprected id", returnedTask.getTaskId(), is( expectedTaskId ) );
      assertThat( "Unexprected name", returnedTask.getTaskName(), is( TASK_DESCRIPTION ) );
      assertThat( "Unexprected status", returnedTask.getTaskStatus(), is( ACARCHIVE.getCd() ) );
      assertThat( "Unexprected class", returnedTask.getTaskClass(), is( AD.getCd() ) );
      assertThat( "Unexprected subclass", returnedTask.getTaskSubclass(), is( MPCCLOSE.getCd() ) );
      assertThat( "Unexprected priority", returnedTask.getTaskPriority(),
            is( TASK_PRIORITY.getCd() ) );
      assertThat( "Unexprected scheduled start date", returnedTask.getScheduledStartDate(),
            is( DateUtils.toString( SCHEDULED_START_DATE, DateUtils.JAVA_DATETIME_FORMAT ) ) );
      assertThat( "Unexprected actual start date", returnedTask.getActualStartDate(),
            is( DateUtils.toString( ACTUAL_START_DATE, DateUtils.JAVA_DATETIME_FORMAT ) ) );
      assertThat( "Unexprected key", returnedTask.getEncryptedTaskKey(),
            is( expectedEncryptedTaskKey ) );
   }


   /**
    * Verify that {@link TasksEndpoint#getTasks} responds with workpackage information when task is
    * assigned to a work package's work scope and the only-workscope is provided and set to true.
    */
   @SuppressWarnings( "unchecked" )
   @Test
   public void returnsWorkPackageInfoWhenTaskAssignedToWorkscopeAndOnlyWorkscopeIsTrue()
         throws Exception {

      // Given a task that is assigned to the workscope of an workpackage.
      InventoryKey aircraft = Domain.createAircraft( acft -> {
         acft.setDescription( AIRCRAFT_DESCRIPTION );
      } );
      TaskKey workpackage = Domain.createWorkPackage( wp -> {
         wp.addWorkScopeTask( Domain.createRequirement() );
         wp.setName( WORKPACKAGE_NAME );
         wp.setLocation( OPS );
         wp.setAircraft( aircraft );
      } );
      String expectedEncryptedWpKey = encryptKeyString( workpackage.toString() );
      String expectedEncryptedAircraftKey = encryptKeyString( aircraft.toString() );

      // When getTasks is called with only-workscope set to true.
      TasksEndpointParameters parms = buildDefaultTasksEndpointParameters();
      parms.setOnlyWorkscoped( true );
      Response response = tasksEndpoint.getTasks( mockSecurityContext, parms );

      // Then the response contains the workpackage information.
      TasksResponse returnedTask = ( ( List<TasksResponse> ) response.getEntity() ).get( 0 );
      assertThat( "Unexprected workpackage name", returnedTask.getWorkpackage(),
            is( WORKPACKAGE_NAME ) );
      assertThat( "Unexprected aircraft description", returnedTask.getAircraft(),
            is( AIRCRAFT_DESCRIPTION ) );
      assertThat( "Unexprected line number", returnedTask.getLineNo(), is( "1" ) );
      assertThat( "Unexprected encrypted work package key",
            returnedTask.getEncryptedWorkpackageKey(), is( expectedEncryptedWpKey ) );
      assertThat( "Unexprected encrypted aircraft key", returnedTask.getEncryptedAircraftKeyd(),
            is( expectedEncryptedAircraftKey ) );
   }


   /**
    * Verify that {@link TasksEndpoint#getTasks} responds with workpackage information when task is
    * assigned to a work package's work scope and the only-workscope is provided and set to false.
    */
   @SuppressWarnings( "unchecked" )
   @Test
   public void returnsWorkPackageInfoWhenTaskAssignedToWorkscopeAndOnlyWorkscopeIsFalse()
         throws Exception {

      // Given a task that is assigned to the workscope of an workpackage.
      InventoryKey aircraft = Domain.createAircraft( acft -> {
         acft.setDescription( AIRCRAFT_DESCRIPTION );
      } );
      TaskKey workpackage = Domain.createWorkPackage( wp -> {
         wp.addWorkScopeTask( Domain.createRequirement() );
         wp.setName( WORKPACKAGE_NAME );
         wp.setLocation( OPS );
         wp.setAircraft( aircraft );
      } );
      String expectedEncryptedWpKey = encryptKeyString( workpackage.toString() );
      String expectedEncryptedAircraftKey = encryptKeyString( aircraft.toString() );

      // When getTasks is called with only-workscope set to false.
      TasksEndpointParameters parms = buildDefaultTasksEndpointParameters();
      parms.setOnlyWorkscoped( false );
      Response response = tasksEndpoint.getTasks( mockSecurityContext, parms );

      // Then the response contains the workpackage information.
      TasksResponse returnedTask = ( ( List<TasksResponse> ) response.getEntity() ).get( 0 );
      assertThat( "Unexprected workpackage name", returnedTask.getWorkpackage(),
            is( WORKPACKAGE_NAME ) );
      assertThat( "Unexprected aircraft description", returnedTask.getAircraft(),
            is( AIRCRAFT_DESCRIPTION ) );
      assertThat( "Unexprected line number", returnedTask.getLineNo(), is( "1" ) );
      assertThat( "Unexprected encrypted work package key",
            returnedTask.getEncryptedWorkpackageKey(), is( expectedEncryptedWpKey ) );
      assertThat( "Unexprected encrypted aircraft key", returnedTask.getEncryptedAircraftKeyd(),
            is( expectedEncryptedAircraftKey ) );
   }


   /**
    * Verify that {@link TasksEndpoint#getTasks} responds with workpackage information when task is
    * assigned to a work package's work scope and the only-workscope is not provided.
    */
   @SuppressWarnings( "unchecked" )
   @Test
   public void returnsWorkPackageInfoWhenTaskAssignedToWorkscopeAndOnlyWorkscopeIsNotProvided()
         throws Exception {

      // Given a task that is assigned to the workscope of an workpackage.
      InventoryKey aircraft = Domain.createAircraft( acft -> {
         acft.setDescription( AIRCRAFT_DESCRIPTION );
      } );
      TaskKey workpackage = Domain.createWorkPackage( wp -> {
         wp.addWorkScopeTask( Domain.createRequirement() );
         wp.setName( WORKPACKAGE_NAME );
         wp.setLocation( OPS );
         wp.setAircraft( aircraft );
      } );
      String expectedEncryptedWpKey = encryptKeyString( workpackage.toString() );
      String expectedEncryptedAircraftKey = encryptKeyString( aircraft.toString() );

      // When getTasks is called with only-workscope not provided.
      Response response =
            tasksEndpoint.getTasks( mockSecurityContext, buildDefaultTasksEndpointParameters() );

      // Then the response contains the workpackage information.
      TasksResponse returnedTask = ( ( List<TasksResponse> ) response.getEntity() ).get( 0 );
      assertThat( "Unexprected workpackage name", returnedTask.getWorkpackage(),
            is( WORKPACKAGE_NAME ) );
      assertThat( "Unexprected aircraft description", returnedTask.getAircraft(),
            is( AIRCRAFT_DESCRIPTION ) );
      assertThat( "Unexprected line number", returnedTask.getLineNo(), is( "1" ) );
      assertThat( "Unexprected encrypted work package key",
            returnedTask.getEncryptedWorkpackageKey(), is( expectedEncryptedWpKey ) );
      assertThat( "Unexprected encrypted aircraft key", returnedTask.getEncryptedAircraftKeyd(),
            is( expectedEncryptedAircraftKey ) );
   }


   /**
    * Verify that {@link TasksEndpoint#getTasks} responds with labour information when task has
    * labour and the only-contains-labour is provided and set to true.
    */
   @SuppressWarnings( "unchecked" )
   @Test
   public void returnsLabourInfoWhenTaskHasLabourAndOnlyContainsLabourIsTrue() throws Exception {

      // Given a job stopped labour row (needed as part of the labour information below).
      TaskKey stoppedTask = Domain.createRequirement( stopped -> {
         stopped.addLabour( jobStopped -> {
            jobStopped.setSkill( ENG );
            jobStopped.setTechnicianRole(
                  tech -> tech.setScheduledHours( JOB_STOPPED_SCHEDULED_WORK_HOURS ) );
         } );
      } );
      SchedLabourKey jobStoppedLabour =
            Domain.readLabourRequirement( stoppedTask, ENG, JOB_STOPPED_SCHEDULED_WORK_HOURS );

      // Given a task that contains labour.
      TaskKey task = Domain.createRequirement( req -> {
         req.addLabour( labour -> {
            labour.setSkill( PILOT );
            labour.setStage( ACTV );
            labour.setStageReason( DEADEXT );
            labour.setSourceJobStopLabour( jobStoppedLabour );
            labour.setTechnicianRole( tech -> {
               tech.setScheduledHours( SCHEDULED_WORK_HOURS );
               tech.setActualHours( ACTUAL_WORK_HOURS );
               tech.setHumanResouce( Domain.createHumanResource( hr -> {
                  hr.setUser( Domain.createUser( user -> {
                     user.setFirstName( FIRST_NAME );
                     user.setLastName( LAST_NAME );
                  } ) );
               } ) );
            } );
         } );
      } );

      SchedLabourKey labour = Domain.readLabourRequirement( task, PILOT, SCHEDULED_WORK_HOURS );
      String expectedEncryptedLabourKey = encryptKeyString( labour.toString() );

      // When getTasks is called with only-contains-labour set to true.
      TasksEndpointParameters parms = buildDefaultTasksEndpointParameters();
      parms.setOnlyContainingLabour( true );
      Response response = tasksEndpoint.getTasks( mockSecurityContext, parms );

      // Then the response contains the labour information.
      for ( TasksResponse returnedTask : ( List<TasksResponse> ) response.getEntity() ) {
         if ( returnedTask.getTaskId().equals( readTaskId( task ) ) ) {
            assertThat( "Unexprected labour row id", returnedTask.getLabourRowId(),
                  is( readLabourRowId( task, PILOT, SCHEDULED_WORK_HOURS ) ) );
            assertThat( "Unexprected skill", returnedTask.getLabourSkill(), is( PILOT.getCd() ) );
            assertThat( "Unexprected status", returnedTask.getlabourRoleStatus(),
                  is( ACTV.getCd() ) );
            assertThat( "Unexprected job stop reason", returnedTask.getJobStopReason(),
                  is( DEADEXT.getCd() ) );
            assertThat( "Unexprected scheduled hours", returnedTask.getScheduledHours(),
                  is( SCHEDULED_WORK_HOURS.toString() ) );
            assertThat( "Unexprected actual hours", returnedTask.getActualHours(),
                  is( ACTUAL_WORK_HOURS.toString() ) );
            assertTrue( "Unexprected is from job stop", returnedTask.getIsFromJobStop() );
            assertThat( "Unexprected tech first name", returnedTask.getTechnicianFirstName(),
                  is( FIRST_NAME ) );
            assertThat( "Unexprected tech first name", returnedTask.getTechnicianLastName(),
                  is( LAST_NAME ) );
            assertThat( "Unexprected encrypted labour key", returnedTask.getEncryptedLabourKey(),
                  is( expectedEncryptedLabourKey ) );
            return;
         }
      }
      Assert.fail( "Expeceted task was not returned." );
   }


   /**
    * Verify that {@link TasksEndpoint#getTasks} responds with labour information when task has
    * labour and the only-contains-labour is provided and set to false.
    */
   @SuppressWarnings( "unchecked" )
   @Test
   public void returnsLabourInfoWhenTaskHasLabourAndOnlyContainsLabourIsFalse() throws Exception {

      // Given a job stopped labour row (needed as part of the labour information below).
      TaskKey stoppedTask = Domain.createRequirement( stopped -> {
         stopped.addLabour( jobStopped -> {
            jobStopped.setSkill( ENG );
            jobStopped.setTechnicianRole(
                  tech -> tech.setScheduledHours( JOB_STOPPED_SCHEDULED_WORK_HOURS ) );
         } );
      } );
      SchedLabourKey jobStoppedLabour =
            Domain.readLabourRequirement( stoppedTask, ENG, JOB_STOPPED_SCHEDULED_WORK_HOURS );

      // Given a task that contains labour.
      TaskKey task = Domain.createRequirement( req -> {
         req.addLabour( labour -> {
            labour.setSkill( PILOT );
            labour.setStage( ACTV );
            labour.setStageReason( DEADEXT );
            labour.setSourceJobStopLabour( jobStoppedLabour );
            labour.setTechnicianRole( tech -> {
               tech.setScheduledHours( SCHEDULED_WORK_HOURS );
               tech.setActualHours( ACTUAL_WORK_HOURS );
               tech.setHumanResouce( Domain.createHumanResource( hr -> {
                  hr.setUser( Domain.createUser( user -> {
                     user.setFirstName( FIRST_NAME );
                     user.setLastName( LAST_NAME );
                  } ) );
               } ) );
            } );
         } );
      } );

      // When getTasks is called with only-contains-labour set to false.
      TasksEndpointParameters parms = buildDefaultTasksEndpointParameters();
      parms.setOnlyContainingLabour( false );
      Response response = tasksEndpoint.getTasks( mockSecurityContext, parms );

      // Then the response contains the labour information.
      for ( TasksResponse returnedTask : ( List<TasksResponse> ) response.getEntity() ) {
         if ( returnedTask.getTaskId().equals( readTaskId( task ) ) ) {
            assertThat( "Unexprected labour row id", returnedTask.getLabourRowId(),
                  is( readLabourRowId( task, PILOT, SCHEDULED_WORK_HOURS ) ) );
            assertThat( "Unexprected skill", returnedTask.getLabourSkill(), is( PILOT.getCd() ) );
            assertThat( "Unexprected status", returnedTask.getlabourRoleStatus(),
                  is( ACTV.getCd() ) );
            assertThat( "Unexprected job stop reason", returnedTask.getJobStopReason(),
                  is( DEADEXT.getCd() ) );
            assertThat( "Unexprected scheduled hours", returnedTask.getScheduledHours(),
                  is( SCHEDULED_WORK_HOURS.toString() ) );
            assertThat( "Unexprected actual hours", returnedTask.getActualHours(),
                  is( ACTUAL_WORK_HOURS.toString() ) );
            assertTrue( "Unexprected is from job stop", returnedTask.getIsFromJobStop() );
            assertThat( "Unexprected tech first name", returnedTask.getTechnicianFirstName(),
                  is( FIRST_NAME ) );
            assertThat( "Unexprected tech first name", returnedTask.getTechnicianLastName(),
                  is( LAST_NAME ) );
            return;
         }
      }
      Assert.fail( "Expeceted task was not returned." );
   }


   /**
    * Verify that {@link TasksEndpoint#getTasks} responds with labour information when task has
    * labour and the only-contains-labour is not provided.
    */
   @SuppressWarnings( "unchecked" )
   @Test
   public void returnsLabourInfoWhenTaskHasLabourAndOnlyContainsLabourIsNotProvided()
         throws Exception {

      // Given a job stopped labour row (needed as part of the labour information below).
      TaskKey stoppedTask = Domain.createRequirement( stopped -> {
         stopped.addLabour( jobStopped -> {
            jobStopped.setSkill( ENG );
            jobStopped.setTechnicianRole(
                  tech -> tech.setScheduledHours( JOB_STOPPED_SCHEDULED_WORK_HOURS ) );
         } );
      } );
      SchedLabourKey jobStoppedLabour =
            Domain.readLabourRequirement( stoppedTask, ENG, JOB_STOPPED_SCHEDULED_WORK_HOURS );

      // Given a task that contains labour.
      TaskKey task = Domain.createRequirement( req -> {
         req.addLabour( labour -> {
            labour.setSkill( PILOT );
            labour.setStage( ACTV );
            labour.setStageReason( DEADEXT );
            labour.setSourceJobStopLabour( jobStoppedLabour );
            labour.setTechnicianRole( tech -> {
               tech.setScheduledHours( SCHEDULED_WORK_HOURS );
               tech.setActualHours( ACTUAL_WORK_HOURS );
               tech.setHumanResouce( Domain.createHumanResource( hr -> {
                  hr.setUser( Domain.createUser( user -> {
                     user.setFirstName( FIRST_NAME );
                     user.setLastName( LAST_NAME );
                  } ) );
               } ) );
            } );
         } );
      } );

      // When getTasks is called without only-contains-labour.
      Response response =
            tasksEndpoint.getTasks( mockSecurityContext, buildDefaultTasksEndpointParameters() );

      // Then the response contains the labour information.
      for ( TasksResponse returnedTask : ( List<TasksResponse> ) response.getEntity() ) {
         if ( returnedTask.getTaskId().equals( readTaskId( task ) ) ) {
            assertThat( "Unexprected labour row id", returnedTask.getLabourRowId(),
                  is( readLabourRowId( task, PILOT, SCHEDULED_WORK_HOURS ) ) );
            assertThat( "Unexprected skill", returnedTask.getLabourSkill(), is( PILOT.getCd() ) );
            assertThat( "Unexprected status", returnedTask.getlabourRoleStatus(),
                  is( ACTV.getCd() ) );
            assertThat( "Unexprected job stop reason", returnedTask.getJobStopReason(),
                  is( DEADEXT.getCd() ) );
            assertThat( "Unexprected scheduled hours", returnedTask.getScheduledHours(),
                  is( SCHEDULED_WORK_HOURS.toString() ) );
            assertThat( "Unexprected actual hours", returnedTask.getActualHours(),
                  is( ACTUAL_WORK_HOURS.toString() ) );
            assertTrue( "Unexprected is from job stop", returnedTask.getIsFromJobStop() );
            assertThat( "Unexprected tech first name", returnedTask.getTechnicianFirstName(),
                  is( FIRST_NAME ) );
            assertThat( "Unexprected tech first name", returnedTask.getTechnicianLastName(),
                  is( LAST_NAME ) );
            return;
         }
      }
      Assert.fail( "Expeceted task was not returned." );
   }


   /**
    * Verify that {@link TasksEndpoint#getTasks} responds with all labour rows when many tasks have
    * many labour rows.
    */
   @SuppressWarnings( "unchecked" )
   @Test
   public void returnsAllLabourRowsWhenManyTasksHaveManyLabourRows() throws Exception {

      // Given many task each with many labour rows.
      TaskKey task1 = Domain.createRequirement( req -> {
         req.addLabour( labour -> {
            labour.setSkill( PILOT );
            labour.setTechnicianRole( tech -> tech.setScheduledHours( SCHEDULED_WORK_HOURS_ONE ) );
         } );
         req.addLabour( labour -> {
            labour.setSkill( ENG );
            labour.setTechnicianRole( tech -> tech.setScheduledHours( SCHEDULED_WORK_HOURS_TWO ) );
         } );
      } );
      TaskKey task2 = Domain.createRequirement( req -> {
         req.addLabour( labour -> {
            labour.setSkill( LBR );
            labour.setTechnicianRole(
                  tech -> tech.setScheduledHours( SCHEDULED_WORK_HOURS_THREE ) );
         } );
         req.addLabour( labour -> {
            labour.setSkill( SKILL_1 );
            labour.setTechnicianRole( tech -> tech.setScheduledHours( SCHEDULED_WORK_HOURS_FOUR ) );
         } );
      } );

      List<String> expectedIds = new ArrayList<>();
      expectedIds
            .add( readTaskId( task1 ) + readLabourRowId( task1, PILOT, SCHEDULED_WORK_HOURS_ONE ) );
      expectedIds
            .add( readTaskId( task1 ) + readLabourRowId( task1, ENG, SCHEDULED_WORK_HOURS_TWO ) );
      expectedIds
            .add( readTaskId( task2 ) + readLabourRowId( task2, LBR, SCHEDULED_WORK_HOURS_THREE ) );
      expectedIds.add(
            readTaskId( task2 ) + readLabourRowId( task2, SKILL_1, SCHEDULED_WORK_HOURS_FOUR ) );

      // When getTasks is called without parameters.
      Response response =
            tasksEndpoint.getTasks( mockSecurityContext, buildDefaultTasksEndpointParameters() );

      // Then the response contains a row for each labour of each task.
      List<String> actualIds = new ArrayList<>();
      for ( TasksResponse returnedTask : ( List<TasksResponse> ) response.getEntity() ) {
         actualIds.add( returnedTask.getTaskId() + returnedTask.getLabourRowId() );
      }
      assertThat( "Unexpected labour rows", actualIds,
            containsInAnyOrder( expectedIds.toArray() ) );

   }


   /**
    * Verify that {@link TasksEndpoint#getTasks} responds with an accurate elapsed-hours-minutes
    * when the task's labour has a start date in the past.
    */
   @SuppressWarnings( "unchecked" )
   @Test
   public void returnsAccurateElapsedHoursMinutesOfLabourRowWithStartDateInThePast()
         throws Exception {

      // Determine now with a granularity of seconds (same as DB date-times).
      Date now = DateUtils.floorMillisecond( new Date() );
      Date startDateInPast = DateUtils.addMinutes( now, -315 ); // 5 hours 15 minutes

      // Given a task with a labour having an actual start date in the past.
      Domain.createRequirement( req -> {
         req.addLabour( labour -> {
            labour.setTechnicianRole( tech -> tech.setActualStartDate( startDateInPast ) );
         } );
      } );

      // When getTasks is called.
      Response response =
            tasksEndpoint.getTasks( mockSecurityContext, buildDefaultTasksEndpointParameters() );

      // Then the response contains a row for the labour row.
      List<TasksResponse> tasksList = ( List<TasksResponse> ) response.getEntity();
      assertThat( "Unexpected number of tasks returned in response.", tasksList.size(), is( 1 ) );

      // Then the response contains an accurate elapsed-hours-minutes.
      //
      // Note: depending on how long the production code takes to execute and when exactly its "now"
      // is determined, there could be a variation in the returned value. So we will give the
      // results a window of a minute.
      assertThat( "Unexpected elapsed hours:minutes.", tasksList.get( 0 ).getElapsedHoursMinutes(),
            anyOf( is( "5:15" ), is( "5:16" ) ) );
   }


   /**
    * Verify that {@link TasksEndpoint#getTasks} responds with an accurate elapsed-hours-minutes
    * when the task's labour has a start date of now.
    */
   @SuppressWarnings( "unchecked" )
   @Test
   public void returnsAccurateElapsedHoursMinutesOfLabourRowWithStartDateOfNow() throws Exception {

      // Determine now with a granularity of seconds (same as DB date-times).
      Date now = DateUtils.floorMillisecond( new Date() );
      Date startDateNow = now;

      // Given a task with a labour having an actual start date in the past.
      Domain.createRequirement( req -> {
         req.addLabour( labour -> {
            labour.setTechnicianRole( tech -> tech.setActualStartDate( startDateNow ) );
         } );
      } );

      // When getTasks is called.
      Response response =
            tasksEndpoint.getTasks( mockSecurityContext, buildDefaultTasksEndpointParameters() );

      // Then the response contains a row for the labour row.
      List<TasksResponse> tasksList = ( List<TasksResponse> ) response.getEntity();
      assertThat( "Unexpected number of tasks returned in response.", tasksList.size(), is( 1 ) );

      // Then the response contains an accurate elapsed-hours-minutes.
      //
      // Note: depending on how long the production code takes to execute and when exactly its "now"
      // is determined, there could be a variation in the returned value. So we will give the
      // results a window of a minute.
      assertThat( "Unexpected elapsed hours:minutes.", tasksList.get( 0 ).getElapsedHoursMinutes(),
            anyOf( is( "0:00" ), is( "0:01" ) ) );
   }


   /**
    * Verify that {@link TasksEndpoint#getTasks} responds with an accurate elapsed-hours-minutes
    * when the task's labour has a start date in the future.
    */
   @SuppressWarnings( "unchecked" )
   @Test
   public void returnsAccurateElapsedHoursMinutesOfLabourRowWithStartDateInTheFuture()
         throws Exception {

      // Determine now with a granularity of seconds (same as DB date-times).
      Date now = DateUtils.floorMillisecond( new Date() );
      Date startDateInFuture = DateUtils.addMinutes( now, 315 ); // 5 hours 15 minutes

      // Given a task with a labour having an actual start date in the past.
      Domain.createRequirement( req -> {
         req.addLabour( labour -> {
            labour.setTechnicianRole( tech -> tech.setActualStartDate( startDateInFuture ) );
         } );
      } );

      // When getTasks is called.
      Response response =
            tasksEndpoint.getTasks( mockSecurityContext, buildDefaultTasksEndpointParameters() );

      // Then the response contains a row for the labour row.
      List<TasksResponse> tasksList = ( List<TasksResponse> ) response.getEntity();
      assertThat( "Unexpected number of tasks returned in response.", tasksList.size(), is( 1 ) );

      // Then the response contains an accurate elapsed-hours-minutes.
      //
      // Note: depending on how long the production code takes to execute and when exactly its "now"
      // is determined, there could be a variation in the returned value. So we will give the
      // results a window of a minute.
      assertThat( "Unexpected elapsed hours:minutes.", tasksList.get( 0 ).getElapsedHoursMinutes(),
            anyOf( is( "-5:14" ), is( "-5:15" ) ) );
   }


   /**
    * Verify that {@link TasksEndpoint#getTasks} responds with no elapsed-hours-minutes when the
    * task's labour has no start date.
    */
   @SuppressWarnings( "unchecked" )
   @Test
   public void returnsNoElapsedHoursMinutesOfLabourRowWithNoStartDate() throws Exception {

      // Given a task with a labour having no actual start date (but has an actual end date).
      Domain.createRequirement( req -> {
         req.addLabour( labour -> {
            labour.setTechnicianRole( tech -> {
               tech.setActualStartDate( null );
               tech.setActualEndDate( new Date() );
            } );
         } );
      } );

      // When getTasks is called.
      Response response =
            tasksEndpoint.getTasks( mockSecurityContext, buildDefaultTasksEndpointParameters() );

      // Then the response contains a row for the labour row.
      List<TasksResponse> tasksList = ( List<TasksResponse> ) response.getEntity();
      assertThat( "Unexpected number of tasks returned in response.", tasksList.size(), is( 1 ) );

      // Then the response contains an accurate elapsed-hours-minutes.
      //
      // Note: depending on how long the production code takes to execute and when exactly its "now"
      // is determined, there could be a variation in the returned value. So we will give the
      // results a window of a minute.
      assertThat( "Unexpected elapsed hours:minutes.", tasksList.get( 0 ).getElapsedHoursMinutes(),
            is( "" ) );
   }


   /**
    * Verify that {@link TasksEndpoint#getTasks} responds with no elapsed-hours-minutes when the
    * task's labour has an start date but also has an end date.
    */
   @SuppressWarnings( "unchecked" )
   @Test
   public void returnsNoElapsedHoursMinutesOfLabourRowWithStartDateAndEndDate() throws Exception {

      // Given a task with a labour having both an actual start date and an actual end date.
      Domain.createRequirement( req -> {
         req.addLabour( labour -> {
            labour.setTechnicianRole( tech -> {
               tech.setActualStartDate( new Date() );
               tech.setActualEndDate( new Date() );
            } );
         } );
      } );

      // When getTasks is called.
      Response response =
            tasksEndpoint.getTasks( mockSecurityContext, buildDefaultTasksEndpointParameters() );

      // Then the response contains a row for the labour row.
      List<TasksResponse> tasksList = ( List<TasksResponse> ) response.getEntity();
      assertThat( "Unexpected number of tasks returned in response.", tasksList.size(), is( 1 ) );

      // Then the response contains an accurate elapsed-hours-minutes.
      //
      // Note: depending on how long the production code takes to execute and when exactly its "now"
      // is determined, there could be a variation in the returned value. So we will give the
      // results a window of a minute.
      assertThat( "Unexpected elapsed hours:minutes.", tasksList.get( 0 ).getElapsedHoursMinutes(),
            is( "" ) );
   }


   private String readCrewId( DepartmentKey crew ) {
      return OrgWorkDept.findByPrimaryKey( crew ).getAltId().toString();
   }


   private String readTaskId( TaskKey task ) {
      return InjectorContainer.get().getInstance( SchedStaskDao.class ).findByPrimaryKey( task )
            .getAlternateKey().toString();
   }


   private String readLabourRowId( TaskKey task, RefLabourSkillKey skill,
         BigDecimal scheduledHours ) {

      for ( SchedLabourKey labourRow : Domain.readLabourRequirement( task ) ) {
         DataSetArgument args = new DataSetArgument();
         args.add( labourRow, "labour_db_id", "labour_id" );
         QuerySet qs = QuerySetFactory.getInstance().executeQueryTable( "sched_labour_role", args );
         while ( qs.next() ) {
            if ( scheduledHours.equals( BigDecimal.valueOf( qs.getDouble( "sched_hr" ) ) ) ) {
               return qs.getUuid( "alt_id" ).toString();
            }
         }
      }
      return null;
   }


   private TasksEndpointParameters buildDefaultTasksEndpointParameters() {
      TasksEndpointParameters parms = new TasksEndpointParameters();
      parms.setTaskKeyEncriptionName( "" );
      parms.setWorkpackageKeyEncriptionName( "" );
      parms.setAircraftKeyEncriptionName( "" );
      parms.setLabourKeyEncriptionName( "" );
      return parms;
   }


   private String encryptKeyString( String key ) {
      return new ParameterEncrypter( USERNAME ).encrypt( "", key );
   }

}
