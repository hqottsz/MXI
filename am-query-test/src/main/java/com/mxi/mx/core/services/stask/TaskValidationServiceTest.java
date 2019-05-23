package com.mxi.mx.core.services.stask;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.stask.CannotAssignUnassignException.TaskAction;
import com.mxi.mx.core.table.org.OrgHr;


public class TaskValidationServiceTest {

   private static final String ACTION_ASSIGN_TASK_TO_CHECK = "ACTION_ASSIGN_TASK_TO_CHECK";
   private static final String ACTION_ASSIGN_FAULT_TO_CHECK = "ACTION_ASSIGN_FAULT_TO_CHECK";
   private static final String SECURED_RESOURCE = "SECURED_RESOURCE";
   private static final String TRUE = "TRUE";

   private TaskValidationService iTaskValidationService;
   private HumanResourceKey iHr;
   private TaskKey iTask;
   private TaskKey iFault;
   private UserParametersFake iUserParms;

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void setUp() {
      iHr = new HumanResourceDomainBuilder().build();
      int lUserId = OrgHr.findByPrimaryKey( iHr ).getUserId();

      iUserParms = new UserParametersFake( lUserId, SECURED_RESOURCE );
      UserParameters.setInstance( lUserId, SECURED_RESOURCE, iUserParms );

      iTaskValidationService = new TaskValidationService( iHr );

      iTask = Domain.createBlock();
      iFault = new TaskBuilder().withTaskClass( RefTaskClassKey.CORR )
            .withFault( Domain.createFault() ).build();
   }


   @Test( expected = NullPointerException.class )
   public void validate_withNullAsTaskActionForTask() throws CannotAssignUnassignException {
      iTaskValidationService.validate( null, TaskAction.ASSIGN_FAULT_TO_CHECK, iTask );
   }


   @Test( expected = NullPointerException.class )
   public void validate_withNullAsTaskActionForFault() throws CannotAssignUnassignException {
      iTaskValidationService.validate( TaskAction.ASSIGN_TASK_TO_CHECK, null, iFault );
   }


   @Test( expected = NullPointerException.class )
   public void validate_withNullAsTasks() throws CannotAssignUnassignException {
      TaskKey[] lTasks = null;
      iTaskValidationService.validate( TaskAction.ASSIGN_TASK_TO_CHECK,
            TaskAction.ASSIGN_FAULT_TO_CHECK, lTasks );
   }


   @Test( expected = CannotAssignUnassignException.class )
   public void validate_withoutPermissionForAssignTask() throws CannotAssignUnassignException {
      iTaskValidationService.validate( TaskAction.ASSIGN_TASK_TO_CHECK,
            TaskAction.ASSIGN_FAULT_TO_CHECK, iTask );
   }


   @Test( expected = CannotAssignUnassignException.class )
   public void validate_withoutPermissionForAssignFault() throws CannotAssignUnassignException {
      iTaskValidationService.validate( TaskAction.ASSIGN_TASK_TO_CHECK,
            TaskAction.ASSIGN_FAULT_TO_CHECK, iFault );
   }


   @Test( expected = CannotAssignUnassignException.class )
   public void validate_withoutPermissionForAssignTaskAndFault()
         throws CannotAssignUnassignException {
      iTaskValidationService.validate( TaskAction.ASSIGN_TASK_TO_CHECK,
            TaskAction.ASSIGN_FAULT_TO_CHECK, iTask, iFault );
   }


   @Test
   public void validate_withPermissionForAssignFault() throws CannotAssignUnassignException {
      iUserParms.setProperty( ACTION_ASSIGN_FAULT_TO_CHECK, TRUE );
      iTaskValidationService.validate( TaskAction.ASSIGN_TASK_TO_CHECK,
            TaskAction.ASSIGN_FAULT_TO_CHECK, iFault );
   }


   @Test
   public void validate_withPermissionForAssignTask() throws CannotAssignUnassignException {
      iUserParms.setProperty( ACTION_ASSIGN_TASK_TO_CHECK, TRUE );
      iTaskValidationService.validate( TaskAction.ASSIGN_TASK_TO_CHECK,
            TaskAction.ASSIGN_FAULT_TO_CHECK, iTask );
   }


   @Test
   public void validate_withPermissionForAssignTaskAndFault() throws CannotAssignUnassignException {
      iUserParms.setProperty( ACTION_ASSIGN_TASK_TO_CHECK, TRUE );
      iUserParms.setProperty( ACTION_ASSIGN_FAULT_TO_CHECK, TRUE );
      iTaskValidationService.validate( TaskAction.ASSIGN_TASK_TO_CHECK,
            TaskAction.ASSIGN_FAULT_TO_CHECK, iFault, iTask );
   }


   @Test( expected = NullPointerException.class )
   public void validateAndGetTasksInfo_withNullAsTaskActionForTask()
         throws CannotAssignUnassignException {
      iTaskValidationService.validateAndGetTasksInfo( null, TaskAction.ASSIGN_FAULT_TO_CHECK,
            iTask );
   }


   @Test( expected = NullPointerException.class )
   public void validateAndGetTasksInfo_withNullAsTaskActionForFault()
         throws CannotAssignUnassignException {
      iTaskValidationService.validateAndGetTasksInfo( TaskAction.ASSIGN_TASK_TO_CHECK, null,
            iFault );
   }


   @Test( expected = NullPointerException.class )
   public void validateAndGetTasksInfo_withNullAsTasks() throws CannotAssignUnassignException {
      TaskKey[] lTasks = null;
      iTaskValidationService.validateAndGetTasksInfo( TaskAction.ASSIGN_TASK_TO_CHECK,
            TaskAction.ASSIGN_FAULT_TO_CHECK, lTasks );
   }


   @Test( expected = CannotAssignUnassignException.class )
   public void validateAndGetTasksInfo_withoutPermissionForAssignTask()
         throws CannotAssignUnassignException {
      iTaskValidationService.validateAndGetTasksInfo( TaskAction.ASSIGN_TASK_TO_CHECK,
            TaskAction.ASSIGN_FAULT_TO_CHECK, iTask );
   }


   @Test( expected = CannotAssignUnassignException.class )
   public void validateAndGetTasksInfo_withoutPermissionForAssignFault()
         throws CannotAssignUnassignException {
      iTaskValidationService.validateAndGetTasksInfo( TaskAction.ASSIGN_TASK_TO_CHECK,
            TaskAction.ASSIGN_FAULT_TO_CHECK, iFault );
   }


   @Test( expected = CannotAssignUnassignException.class )
   public void validateAndGetTasksInfo_withoutPermissionForAssignTaskAndFault()
         throws CannotAssignUnassignException {
      iTaskValidationService.validateAndGetTasksInfo( TaskAction.ASSIGN_TASK_TO_CHECK,
            TaskAction.ASSIGN_FAULT_TO_CHECK, iTask, iFault );
   }


   @Test
   public void validateAndGetTasksInfo_withPermissionForAssignFault()
         throws CannotAssignUnassignException {
      iUserParms.setProperty( ACTION_ASSIGN_FAULT_TO_CHECK, TRUE );
      String lTasksInfo = iTaskValidationService.validateAndGetTasksInfo(
            TaskAction.ASSIGN_TASK_TO_CHECK, TaskAction.ASSIGN_FAULT_TO_CHECK, iFault );
      assertEquals( TaskValidationService.FAULTS_ONLY, lTasksInfo );
   }


   @Test
   public void validateAndGetTasksInfo_withPermissionForAssignTask()
         throws CannotAssignUnassignException {
      iUserParms.setProperty( ACTION_ASSIGN_TASK_TO_CHECK, TRUE );
      String lTasksInfo = iTaskValidationService.validateAndGetTasksInfo(
            TaskAction.ASSIGN_TASK_TO_CHECK, TaskAction.ASSIGN_FAULT_TO_CHECK, iTask );
      assertEquals( TaskValidationService.TASKS_ONLY, lTasksInfo );
   }


   @Test
   public void validateAndGetTasksInfo_withPermissionForAssignTaskAndFault()
         throws CannotAssignUnassignException {
      iUserParms.setProperty( ACTION_ASSIGN_TASK_TO_CHECK, TRUE );
      iUserParms.setProperty( ACTION_ASSIGN_FAULT_TO_CHECK, TRUE );
      String lTasksInfo = iTaskValidationService.validateAndGetTasksInfo(
            TaskAction.ASSIGN_TASK_TO_CHECK, TaskAction.ASSIGN_FAULT_TO_CHECK, iFault, iTask );
      assertEquals( TaskValidationService.TASKS_AND_FAULTS, lTasksInfo );
   }

}
