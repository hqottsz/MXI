package com.mxi.mx.core.services.taskdefn.taskdefnservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.RefOrgTypeKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.services.taskdefn.TaskDefnService;
import com.mxi.mx.core.services.taskdefn.exception.ActionAlreadyPerformedException;
import com.mxi.mx.core.services.taskdefn.exception.InvalidClassModeException;
import com.mxi.mx.core.services.taskdefn.exception.InvalidTaskDefinitionStatusException;
import com.mxi.mx.core.services.taskdefn.exception.OnConditionException;
import com.mxi.mx.core.services.taskdefn.exception.UserNotAssignedException;
import com.mxi.mx.core.table.task.TaskDefnTable;
import com.mxi.mx.core.table.task.TaskTaskLogTable;
import com.mxi.mx.core.table.task.TaskTaskTable;


/**
 * Tests for {@linkplain TaskDefnService#allowManualInitialization} that involve testing the
 * behavior of allowing manual initialization of task definitions
 */
public class AllowManualInitializationTest {

   private static final String JIC = "JIC";
   private static final String REQ = "REQ";
   private static final String USER_NOTE = "USER_NOTE";
   private static final String TASK_TASK_LOG_TABLE = "task_task_log";

   private TaskDefnService iTaskDefnService;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public ExpectedException iExpectedException = ExpectedException.none();


   /**
    * <pre>
   * Given a JIC task definition with class mode of 'JIC'
   * When the manual initialization is allowed
   * Then an error message for invalid class mode is displayed
    * </pre>
    */
   @Test
   public void itThrowsInvalidClassModeCodeExceptionWhenClassModeCodeNotReq() throws Exception {

      String lExpectedExceptionMessage =
            "[MXERR-30740] The action could not be completed because the 'JIC (null)' task definition must have a class mode of 'REQ' but its actual class mode is 'JIC'.";
      // Given
      TaskTaskKey lJicDefn = Domain.createJobCardDefinition( aJicDefn -> {
         aJicDefn.setCode( JIC );
         aJicDefn.setPreventManualInitialization( true );
      } );
      // Then
      iExpectedException.expect( InvalidClassModeException.class );
      iExpectedException.expectMessage( lExpectedExceptionMessage );
      // When
      iTaskDefnService.allowManualInitialization( lJicDefn, null, null );
      fail( "InvalidClassModeException expected" );
   }


   /**
    * <pre>
    * Given an 'OBSOLETE' task definition with class mode of 'REQ'
    * When the manual initialization is allowed
    * Then an error message for invalid status is displayed
    * </pre>
    */
   @Test
   public void itThrowsInvalidTaskDefinitionStatusExceptionWhenTaskDefinitionStatusObsolete()
         throws Exception {

      String lExpectedExceptionMessage =
            "[MXERR-30514] The 'REQ (null)' task definition has an invalid status of OBSOLETE.";

      // Given
      TaskTaskKey lReqDefn = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.setCode( REQ );
         aReqDefn.setStatus( RefTaskDefinitionStatusKey.OBSOLETE );
         aReqDefn.setPreventManualInitialization( true );
      } );
      // Then
      iExpectedException.expect( InvalidTaskDefinitionStatusException.class );
      iExpectedException.expectMessage( lExpectedExceptionMessage );
      // When
      iTaskDefnService.allowManualInitialization( lReqDefn, null, null );
      fail( "InvalidTaskDefinitionStatusException expected" );

   }


   /**
    * <pre>
    * Given a requirement definition that is prevent from manual initialization
    * When the manual initialization is allowed
    * Then an error message for action cannot be completed is displayed
    * </pre>
    */
   @Test
   public void
         itThrowsActionAlreadyPerformedExceptionWhenTaskDefinitionIsAlreadyAllowManualInitialization()
               throws Exception {

      String lExpectedExceptionMessage =
            "[MXERR-33707] The action could not be completed because 'Allow Manual Initialization' action is already performed for this task definition.";
      // Given
      TaskTaskKey lReqDefn = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.setCode( REQ );
      } );
      // Then
      iExpectedException.expect( ActionAlreadyPerformedException.class );
      iExpectedException.expectMessage( lExpectedExceptionMessage );
      // When
      iTaskDefnService.allowManualInitialization( lReqDefn, null, null );
      fail( "ActionAlreadyPerformed expected" );

   }


   /**
    * <pre>
    * Given a non on-condition based task definition
    * When the manual initialization is allowed
    * Then an error message stating that action can't be performed because the task definition is not on-condition
    * </pre>
    */
   @Test
   public void itThrowsOnConditionExceptionWhenTaskDefinitionIsNotOnCondition() throws Exception {

      String lExpectedExceptionMessage =
            "[MXERR-33706] The action cannot be performed for this task definition as it is not on-condition.";
      // Given
      TaskTaskKey lReqDefn = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.setCode( REQ );
         aReqDefn.setOnCondition( false );
         aReqDefn.setPreventManualInitialization( true );
      } );
      // Then
      iExpectedException.expect( OnConditionException.class );
      iExpectedException.expectMessage( lExpectedExceptionMessage );
      // When
      iTaskDefnService.allowManualInitialization( lReqDefn, null, null );
      fail( "OnConditionException expected" );
   }


   /**
    * <pre>
    * Given an organization and a user assigned to it
    * Given an on-condition task definition belonging to the same organization
    * When the manual initialization is allowed
    * Then an error message for user not assigned is displayed
    * </pre>
    */
   @Test
   public void itThrowsUserNotAssignedExceptionWhenUserNotAssignedToTaskDefinitionOrganization()
         throws Exception {

      // Given
      // UserNotAssignedException.validate requires this exercise the code for exception
      HumanResourceKey lHr =
            Domain.createHumanResource( aHr -> aHr.setUser( Domain.createUser() ) );
      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( lHr ) );

      String lExpectedExceptionMessage =
            "[MXERR-31407] You are not assigned to the organization (or a parent organization) that Task Definition 'REQ (null)' is assigned to.";
      TaskTaskKey lReqDefn = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.setCode( REQ );
         aReqDefn.setOnCondition( true );
         aReqDefn.setPreventManualInitialization( true );
      } );
      // Then
      iExpectedException.expect( UserNotAssignedException.class );
      iExpectedException.expectMessage( lExpectedExceptionMessage );
      // When
      iTaskDefnService.allowManualInitialization( lReqDefn, null, null );
      fail( "UserNotAssignedException expected" );

   }


   /**
    * <pre>
    * Given an organization and a user assigned to it
    * Given an on-condition task definition belonging to the same organization
    * When the manual initialization is allowed
    * Then the manual initialization of the requirement definition is set to prevented
    * </pre>
    */
   @Test
   public void itSetsAllowManualInitialization() throws Exception {

      // Given
      final OrgKey lOrgkey = Domain
            .createOrganization( aOrganization -> aOrganization.setType( RefOrgTypeKey.MRO ) );
      HumanResourceKey lHr = Domain.createHumanResource( aHr -> {
         aHr.setUser( Domain.createUser() );
         aHr.setOrganization( lOrgkey );
      } );
      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( lHr ) );
      TaskTaskKey lReqDefn = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.setCode( REQ );
         aReqDefn.setOnCondition( true );
         aReqDefn.setOrganization( lOrgkey );
         aReqDefn.setPreventManualInitialization( true );
      } );
      // When
      iTaskDefnService.allowManualInitialization( lReqDefn, lHr, null );
      TaskDefnKey lTaskDefnKey = TaskTaskTable.findByPrimaryKey( lReqDefn ).getTaskDefn();
      TaskDefnTable lTaskDefnTable = TaskDefnTable.findByPrimaryKey( lTaskDefnKey );

      // Then
      assertTrue( "Expected allow manual initialization to be true",
            !lTaskDefnTable.isPreventManualInitialization() );
   }


   /**
    * <pre>
    * Given an organization and a user assigned to it
    * Given an on-condition task definition belonging to the same organization
    * When the manual initialization is allowed
    * Then the system note and user note are recorded
    * </pre>
    */
   @Test
   public void itRecordsSystemAndUserNoteWhenAllowManualInitializationIsSet() throws Exception {

      // Given
      final OrgKey lOrgkey = Domain
            .createOrganization( aOrganization -> aOrganization.setType( RefOrgTypeKey.MRO ) );
      HumanResourceKey lHr = Domain.createHumanResource( aHr -> {
         aHr.setUser( Domain.createUser() );
         aHr.setOrganization( lOrgkey );
      } );
      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( lHr ) );
      TaskTaskKey lReqDefn = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.setCode( REQ );
         aReqDefn.setOnCondition( true );
         aReqDefn.setOrganization( lOrgkey );
         aReqDefn.setPreventManualInitialization( true );
      } );

      // When
      iTaskDefnService.allowManualInitialization( lReqDefn, lHr, USER_NOTE );
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lReqDefn, TaskTaskTable.ColumnName.TASK_DB_ID.name(),
            TaskTaskTable.ColumnName.TASK_ID.name() );
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( TASK_TASK_LOG_TABLE, lArgs );
      if ( !lQs.next() )
         fail( "Unexpectedly, Allow Manual Initialization action not logged" );

      String lExpectedSystemNote = i18n.get( "core.msg.ALLOW_MANUAL_INITIALIZATION" );
      // Then
      assertEquals( "Unexpected system note for allow manual initialziation", lExpectedSystemNote,
            lQs.getString( TaskTaskLogTable.ColumnName.SYSTEM_NOTE.name() ) );
      assertEquals( "Unexpected user note for allow manual initialziation", USER_NOTE,
            lQs.getString( TaskTaskLogTable.ColumnName.USER_NOTE.name() ) );
   }


   @Before
   public void setup() {
      iTaskDefnService = new TaskDefnService();
   }

}
