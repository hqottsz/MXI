package com.mxi.mx.core.services.taskdefn;

import static com.mxi.mx.common.utils.DateUtils.addDays;
import static com.mxi.mx.common.utils.DateUtils.getEndOfDay;
import static com.mxi.mx.core.key.DataTypeKey.CDY;
import static com.mxi.mx.core.key.DataTypeKey.HOURS;
import static com.mxi.mx.core.key.RefTaskDefinitionStatusKey.ACTV;
import static com.mxi.mx.core.key.RefTaskDefinitionStatusKey.BUILD;
import static com.mxi.mx.core.key.RefTaskDefinitionStatusKey.REVISION;
import static com.mxi.mx.core.unittest.MxTestUtils.generateString;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.AircraftAssembly;
import com.mxi.am.domain.ConfigurationSlot;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Flight;
import com.mxi.am.domain.JobCard;
import com.mxi.am.domain.JobCardDefinition;
import com.mxi.am.domain.PartGroup;
import com.mxi.am.domain.RecurringSchedulingRule;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.domain.Step;
import com.mxi.am.domain.UsageAdjustment;
import com.mxi.am.domain.builder.LabourRequirementBuilder;
import com.mxi.am.domain.builder.TaskRevisionBuilder;
import com.mxi.am.domain.builder.TaskStepBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersStub;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.exception.StringTooLongException;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.adapter.flight.messages.flight.adapter.logic.ActualDatesCalculator;
import com.mxi.mx.core.flight.model.FlightLegId;
import com.mxi.mx.core.job.stask.deadline.UpdateTaskDeadlinesCoreService;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.FcModelKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefOrgTypeKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.RefTaskRevReasonKey;
import com.mxi.mx.core.key.TaskBlockReqMapKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskJicReqMapKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskPartMapKey;
import com.mxi.mx.core.key.TaskStepKey;
import com.mxi.mx.core.key.TaskStepSkillKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.key.TaskWeightAndBalanceKey;
import com.mxi.mx.core.services.bsync.synchronization.logic.InventorySynchronizer;
import com.mxi.mx.core.services.stask.creation.CreationService;
import com.mxi.mx.core.services.stask.deadline.MxSoftDeadlineService;
import com.mxi.mx.core.services.stask.deadline.SoftDeadlineService;
import com.mxi.mx.core.services.stask.deadline.updatedeadline.UpdateDeadlineService;
import com.mxi.mx.core.services.stask.deadline.updatedeadline.logic.MxUpdateDeadlineService;
import com.mxi.mx.core.services.stask.taskpart.TaskStepTO;
import com.mxi.mx.core.services.taskdefn.exception.InvalidTaskDefinitionStatusException;
import com.mxi.mx.core.services.taskdefn.transferobject.DuplicateTaskDefinitionTO;
import com.mxi.mx.core.services.taskdefn.transferobject.RequirementTO;
import com.mxi.mx.core.services.taskdefn.transferobject.TaskDefnRevTO;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.EvtSchedDeadTable;
import com.mxi.mx.core.table.org.OrgHr;
import com.mxi.mx.core.table.sched.SchedStaskDao;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.table.task.TaskBlockReqMapTable;
import com.mxi.mx.core.table.task.TaskDefnDao;
import com.mxi.mx.core.table.task.TaskDefnTable;
import com.mxi.mx.core.table.task.TaskJicReqMapTable;
import com.mxi.mx.core.table.task.TaskPartMapTable;
import com.mxi.mx.core.table.task.TaskRepRefDao;
import com.mxi.mx.core.table.task.TaskRepRefTableRow;
import com.mxi.mx.core.table.task.TaskStepSkillDao;
import com.mxi.mx.core.table.task.TaskStepSkillTable;
import com.mxi.mx.core.table.task.TaskStepTable;
import com.mxi.mx.core.table.task.TaskTaskLogTable;
import com.mxi.mx.core.table.task.TaskTaskTable;
import com.mxi.mx.core.usage.model.UsageAdjustmentId;
import com.mxi.mx.core.usage.service.MxUsageAccrualService;
import com.mxi.mx.core.usage.service.UsageAccrualService;


/**
 * Integration tests for {@link TaskDefnService}
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class TaskDefnServiceTest {

   private static final String REQ_DEFN_CODE = "REQ_DEFN_CODE";
   private static final String ACFT_APPLICABILITY_CODE_WITHIN_RANGE = "105";
   private static final String ACFT_APPLICABILITY_CODE_OUTSIDE_RANGE = "200";
   private static final String TASK_DEFN_APPLICABILITY_RANGE = "100-150";
   private static final HumanResourceKey HR = new HumanResourceKey( 1, 1 );
   private static final Integer REVISION_1 = 1;
   private static final Integer REVISION_2 = 2;
   private static final BigDecimal REPEAT_INTERVAL = new BigDecimal( 2 );
   private static final BigDecimal NEW_REPEAT_INTERVAL = new BigDecimal( 10 );
   private static final BigDecimal THRESHOLD_NUM_DAYS = new BigDecimal( 10 );
   private static final Date EFFECTIVE_DATE = addDays( new Date(), 0 );
   private static final Date DUE_DATE = addDays( new Date(), 30 );
   private static final SimpleDateFormat DATE_FORMAT_WITHOUT_MS =
         new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
   private static final String TASK_DEFN_APPLICABILITY_RULE = "ac_inv.serial_no_oem LIKE 'QUERY%'";
   private static final String AIRCRAFT_SERIAL_NUMBER_THAT_MATCHES_RULE = "QUERY_TEST";
   private static final String AIRCRAFT_SERIAL_NUMBER_THAT_DOES_NOT_MATCH_RULE = "E2E_TEST";
   private static final String STEP_DESCRIPTION = "STEP_DESCRIPTION";
   private static final RefTaskRevReasonKey TASK_REVISION_REASON_KEY =
         new RefTaskRevReasonKey( 4650, "Rev-1" );
   private static final String REVISION_NOTE = "Revision Note 1";
   private static final boolean FORCE_ACTIVATE = true;
   private static final String TASK_TASK_LOG_TABLE = "TASK_TASK_LOG";
   private static final boolean DO_NOT_DELETE_ENTIRE_BLOCK_CHAIN = false;
   private static final String ACTV_REQ_NAME = "ACTV_REQ_NAME";
   private static final String REV_REQ_NAME = "REV_REQ_NAME";
   private static final String ACTV_REQ_CODE = "ACTV_REQ_CODE";
   private static final String FIRST_AIRCRAFT_ASSEMBLY_WITH_PART = "ASSMBL_1";
   private static final String ASSEMBLY_WITH_CONFIG_SLOT_A = "CS_A";
   private static final String ASSEMBLY_WITH_CONFIG_SLOT_B = "CS_B";
   private static final String TEST_ERROR_LABEL = "TEST_ERROR_LABEL";
   private static final int MAX_STEP_DESCRIPTION_LENGTH = 10;

   // Required by the production code but not applicable to the test.
   private static final String NA_APPLICABILITY = null;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   @Rule
   public ExpectedException iExpectedException = ExpectedException.none();

   private UsageAccrualService iUsageAccrualService;

   private TaskStepSkillDao iTaskStepSkillDao;

   private EvtEventDao evtEventDao;
   private SchedStaskDao schedStaskDao;
   private TaskDefnDao taskDefnDao;


   @Before
   public void setup() {
      evtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );
      schedStaskDao = InjectorContainer.get().getInstance( SchedStaskDao.class );
      taskDefnDao = InjectorContainer.get().getInstance( TaskDefnDao.class );
   }


   @Test
   public void itPersistsStepWhenAddingStep() throws Exception {

      // Create a task revision with BUILD status.
      TaskTaskKey lTaskRev = new TaskRevisionBuilder().withStatus( BUILD ).build();

      // Add one step by TaskDefnService.
      TaskStepKey lTaskStepKey = TaskDefnService.addStep( lTaskRev, "step 1", "100-150" );

      // Ensure the task step is added properly.
      TaskStepTable lTaskStep = TaskStepTable.findByPrimaryKey( lTaskStepKey );
      assertTrue( lTaskStep.exists() );
      assertEquals( 1, lTaskStep.getStepOrd() );
      assertEquals( "step 1", lTaskStep.getStepLdesc() );
      assertEquals( "100-150", lTaskStep.getApplRangeLdesc() );

   }


   @Test( expected = StringTooLongException.class )
   public void itThrowsExceptionWhenAddingStepWithDescriptionLengthGreaterThanMax()
         throws Exception {

      // Given a requirement definition with a BUILD status.
      TaskTaskKey reqDefn = Domain.createRequirementDefinition( defn -> {
         defn.setStatus( BUILD );
      } );

      // Given Mx is configured to have a maximum step description length.
      setMaxStepDescriptionLength( MAX_STEP_DESCRIPTION_LENGTH );

      // When a step is added to the definition and that step description's length is greater than
      // the configured maximum.
      String stepDescription = generateString( MAX_STEP_DESCRIPTION_LENGTH + 1 );
      TaskDefnService.addStep( reqDefn, stepDescription, NA_APPLICABILITY );

      // Then an exception is thrown.
      // (see expected in Test annotation)
   }


   @Test
   public void itPersistsStepWhenAddingStepWithDescriptionLengthEqualToMax() throws Exception {

      // Given a requirement definition with a BUILD status.
      TaskTaskKey reqDefn = Domain.createRequirementDefinition( defn -> {
         defn.setStatus( BUILD );
      } );

      // Given Mx is configured to have a maximum step description length.
      setMaxStepDescriptionLength( MAX_STEP_DESCRIPTION_LENGTH );

      // When a step is added to the definition and that step description's length is equal to the
      // configured maximum.
      String stepDescription = generateString( MAX_STEP_DESCRIPTION_LENGTH );
      TaskStepKey taskStep = TaskDefnService.addStep( reqDefn, stepDescription, NA_APPLICABILITY );

      // Then the step is persisted.
      assertTrue( "Task step not persisted.", TaskStepTable.findByPrimaryKey( taskStep ).exists() );
   }


   @Test
   public void itPersistsStepWhenAddingStepWithDescriptionLengthLessThanMax() throws Exception {

      // Given a requirement definition with a BUILD status.
      TaskTaskKey reqDefn = Domain.createRequirementDefinition( defn -> {
         defn.setStatus( BUILD );
      } );

      // Given Mx is configured to have a maximum step description length.
      setMaxStepDescriptionLength( MAX_STEP_DESCRIPTION_LENGTH );

      // When a step is added to the definition and that step description's length is less than the
      // configured maximum.
      String stepDescription = generateString( MAX_STEP_DESCRIPTION_LENGTH - 1 );
      TaskStepKey taskStep = TaskDefnService.addStep( reqDefn, stepDescription, NA_APPLICABILITY );

      // Then the step is persisted.
      assertTrue( "Task step not persisted.", TaskStepTable.findByPrimaryKey( taskStep ).exists() );
   }


   @Test( expected = StringTooLongException.class )
   public void itThrowsExceptionWhenEditingStepWithDescriptionLengthGreaterThanMaxUsingTO()
         throws Exception {

      // Given a requirement definition with a BUILD status and a step.
      String originalStepDesc = "hello";
      TaskTaskKey reqDefn = createReqDefinitionWithStep( BUILD, originalStepDesc );

      // Given Mx is configured to have a maximum step description length.
      setMaxStepDescriptionLength( MAX_STEP_DESCRIPTION_LENGTH );

      // When the step is edited using a transfer object and a new step description is provided
      // whose length is greater than the configured maximum.
      String newStepDescription = generateString( MAX_STEP_DESCRIPTION_LENGTH + 1 );
      TaskStepTO stepTO = new TaskStepTO();
      stepTO.setDescription( newStepDescription );
      Collection<TaskStepTO> transferObjects = new ArrayList<>();
      transferObjects.add( stepTO );
      TaskDefnService.setSteps( reqDefn, transferObjects );

      // Then an exception is thrown.
      // (see expected in Test annotation)
   }


   @Test
   public void itUpdatesStepWhenEditingStepWithDescriptionLengthEqualToMaxUsingTO()
         throws Exception {

      // Given a requirement definition with a BUILD status and a step.
      String originalStepDesc = "hello";
      TaskTaskKey reqDefn = createReqDefinitionWithStep( BUILD, originalStepDesc );
      TaskStepKey step = Domain.readRequirementDefinitionStep( reqDefn, 1 );

      // Given Mx is configured to have a maximum step description length.
      setMaxStepDescriptionLength( MAX_STEP_DESCRIPTION_LENGTH );

      // When the step is edited using a transfer object and a new step description is provided
      // whose length is equal to the configured maximum.
      String newStepDescription = generateString( MAX_STEP_DESCRIPTION_LENGTH );
      TaskStepTO stepTO = new TaskStepTO();
      stepTO.setTaskStepKey( step );
      stepTO.setDescription( newStepDescription );
      Collection<TaskStepTO> transferObjects = new ArrayList<>();
      transferObjects.add( stepTO );
      TaskDefnService.setSteps( reqDefn, transferObjects );

      // Then the step description is updated.
      assertThat( "Unexpected task step description.",
            TaskStepTable.findByPrimaryKey( step ).getStepLdesc(), is( newStepDescription ) );
   }


   @Test
   public void itUpdatesStepWhenEditingStepWithDescriptionLengthLessThanMaxUsingTO()
         throws Exception {

      // Given a requirement definition with a BUILD status and a step.
      String originalStepDesc = "hello";
      TaskTaskKey reqDefn = createReqDefinitionWithStep( BUILD, originalStepDesc );
      TaskStepKey step = Domain.readRequirementDefinitionStep( reqDefn, 1 );

      // Given Mx is configured to have a maximum step description length.
      setMaxStepDescriptionLength( MAX_STEP_DESCRIPTION_LENGTH );

      // When the step is edited using a transfer object and a new step description is provided
      // whose length is less than the configured maximum.
      String newStepDescription = generateString( MAX_STEP_DESCRIPTION_LENGTH - 1 );
      TaskStepTO stepTO = new TaskStepTO();
      stepTO.setTaskStepKey( step );
      stepTO.setDescription( newStepDescription );
      Collection<TaskStepTO> transferObjects = new ArrayList<>();
      transferObjects.add( stepTO );
      TaskDefnService.setSteps( reqDefn, transferObjects );

      // Then the step description is updated.
      assertThat( "Unexpected task step description.",
            TaskStepTable.findByPrimaryKey( step ).getStepLdesc(), is( newStepDescription ) );
   }


   @Test( expected = StringTooLongException.class )
   public void itThrowsExceptionWhenEditingStepWithDescriptionLengthGreaterThanMaxUsingCollections()
         throws Exception {

      // Given a requirement definition with a BUILD status and a step.
      String originalStepDesc = "hello";
      TaskTaskKey reqDefn = createReqDefinitionWithStep( BUILD, originalStepDesc );
      TaskStepKey step = Domain.readRequirementDefinitionStep( reqDefn, 1 );

      // Given Mx is configured to have a maximum step description length.
      setMaxStepDescriptionLength( MAX_STEP_DESCRIPTION_LENGTH );

      // When the step is edited using a collections and a new step description is provided
      // whose length is greater than the configured maximum.
      String newStepDescription = generateString( MAX_STEP_DESCRIPTION_LENGTH + 1 );
      TaskStepKey[] steps = { step };
      String[] descriptions = { newStepDescription };
      String[] applicabilities = {};
      TaskDefnService.setSteps( reqDefn, steps, descriptions, applicabilities );

      // Then an exception is thrown.
      // (see expected in Test annotation)
   }


   @Test
   public void itUpdatesStepWhenEditingStepWithDescriptionLengthEqualToMaxUsingCollections()
         throws Exception {

      // Given a requirement definition with a BUILD status and a step.
      String originalStepDesc = "hello";
      TaskTaskKey reqDefn = createReqDefinitionWithStep( BUILD, originalStepDesc );
      TaskStepKey step = Domain.readRequirementDefinitionStep( reqDefn, 1 );

      // Given Mx is configured to have a maximum step description length.
      setMaxStepDescriptionLength( MAX_STEP_DESCRIPTION_LENGTH );

      // When the step is edited using a collections and a new step description is provided
      // whose length is equal to the configured maximum.
      String newStepDescription = generateString( MAX_STEP_DESCRIPTION_LENGTH );
      TaskStepKey[] steps = { step };
      String[] descriptions = { newStepDescription };
      String[] applicabilities = {};
      TaskDefnService.setSteps( reqDefn, steps, descriptions, applicabilities );

      // Then the step description is updated.
      assertThat( "Unexpected task step description.",
            TaskStepTable.findByPrimaryKey( step ).getStepLdesc(), is( newStepDescription ) );
   }


   @Test
   public void itUpdatesStepWhenEditingStepWithDescriptionLengthLessThanMaxUsingCollections()
         throws Exception {

      // Given a requirement definition with a BUILD status and a step.
      String originalStepDesc = "hello";
      TaskTaskKey reqDefn = createReqDefinitionWithStep( BUILD, originalStepDesc );
      TaskStepKey step = Domain.readRequirementDefinitionStep( reqDefn, 1 );

      // Given Mx is configured to have a maximum step description length.
      setMaxStepDescriptionLength( MAX_STEP_DESCRIPTION_LENGTH );

      // When the step is edited using a collections and a new step description is provided
      // whose length is less than the configured maximum.
      String newStepDescription = generateString( MAX_STEP_DESCRIPTION_LENGTH - 1 );
      TaskStepKey[] steps = { step };
      String[] descriptions = { newStepDescription };
      String[] applicabilities = {};
      TaskDefnService.setSteps( reqDefn, steps, descriptions, applicabilities );

      // Then the step description is updated.
      assertThat( "Unexpected task step description.",
            TaskStepTable.findByPrimaryKey( step ).getStepLdesc(), is( newStepDescription ) );
   }


   @Test
   public void testAddStepSkills() throws Exception {

      // DATA SETUP: Create an executable requirement
      TaskTaskKey lTaskTask = new TaskRevisionBuilder().withTaskCode( "test" )
            .withTaskClass( RefTaskClassKey.REQ ).isWorkscoped().build();

      // DATA SETUP: Add labor requirements to the task
      new LabourRequirementBuilder( lTaskTask, RefLabourSkillKey.PILOT ).withCertRequired( true )
            .withInspRequired( true ).build();

      new LabourRequirementBuilder( lTaskTask, RefLabourSkillKey.ENG ).withCertRequired( true )
            .withInspRequired( true ).build();

      // DATA SETUP: Add Step
      TaskStepKey lTaskStepKey = new TaskStepBuilder().withTaskTask( lTaskTask ).build();

      // Add Step with step skills
      TaskStepTO.StepSkill lStepSkill = new TaskStepTO.StepSkill();
      lStepSkill.setLabourSkill( RefLabourSkillKey.PILOT );
      lStepSkill.setInspReq( false );

      TaskStepTO.StepSkill lStepSkill2 = new TaskStepTO.StepSkill();
      lStepSkill2.setLabourSkill( RefLabourSkillKey.ENG );
      lStepSkill2.setInspReq( true );

      Collection<TaskStepTO.StepSkill> lStepSkills = new ArrayList<TaskStepTO.StepSkill>();
      lStepSkills.add( lStepSkill );
      lStepSkills.add( lStepSkill2 );

      TaskDefnService.addStepSkills( lTaskStepKey, lStepSkills );

      // Assert that the step skills are added correctly
      TaskStepSkillTable lTaskStepSkillTable = iTaskStepSkillDao
            .findByPrimaryKey( new TaskStepSkillKey( lTaskStepKey, RefLabourSkillKey.PILOT ) );

      assertTrue( lTaskStepSkillTable.exists() );
      assertFalse( lTaskStepSkillTable.getInsp() );

      lTaskStepSkillTable = iTaskStepSkillDao
            .findByPrimaryKey( new TaskStepSkillKey( lTaskStepKey, RefLabourSkillKey.ENG ) );

      assertTrue( lTaskStepSkillTable.exists() );
      assertTrue( lTaskStepSkillTable.getInsp() );
   }


   @Test
   public void testSetSteps() throws Exception {
      // Create a task revision with BUILD status and prepare JIC steps data.
      TaskTaskKey lTaskRev = new TaskRevisionBuilder().withStatus( BUILD ).build();

      TaskStepKey[] lTaskStepKeys = { new TaskStepKey( lTaskRev, 1 ),
            new TaskStepKey( lTaskRev, 2 ), new TaskStepKey( lTaskRev, 3 ) };
      String[] lDescriptions = { "step 1", "step 2", "step 3" };
      String[] lApplRanges = { "100-150", "200-250", "300-350" };

      // Add steps by addStep method of TaskDefnService.
      for ( int i = 0; i < lTaskStepKeys.length; i++ ) {
         lTaskStepKeys[i] = TaskDefnService.addStep( lTaskRev, "step X", "xxx-yyy" );
      }

      // Update steps by setSteps method of TaskDefnService.
      TaskDefnService.setSteps( lTaskRev, lTaskStepKeys, lDescriptions, lApplRanges );

      // Ensure that the task steps are updated properly.
      for ( int i = 0; i < lTaskStepKeys.length; i++ ) {
         TaskStepTable lTaskStep = TaskStepTable.findByPrimaryKey( lTaskStepKeys[i] );
         assertTrue( lTaskStep.exists() );
         assertEquals( i + 1, lTaskStep.getStepOrd() );
         assertEquals( lDescriptions[i], lTaskStep.getStepLdesc() );
         assertEquals( lApplRanges[i], lTaskStep.getApplRangeLdesc() );
      }
   }


   @Test
   public void testStepsAreDeletedWhenRequirementIsDeleted() throws Exception {

      final TaskTaskKey lTaskTask =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               private DomainConfiguration<Step> createStep() {
                  return new DomainConfiguration<Step>() {

                     @Override
                     public void configure( Step aStep ) {
                        aStep.setDescription( "STEP" );
                        aStep.addStepSkill( RefLabourSkillKey.ENG, true );
                     }
                  };
               }


               @Override
               public void configure( RequirementDefinition aReqDefinition ) {

                  aReqDefinition.setStatus( RefTaskDefinitionStatusKey.BUILD );

                  // Add Step
                  aReqDefinition.addStep( createStep() );
               }
            } );

      // Asserts task is created
      TaskTaskTable lTaskTaskTable = TaskTaskTable.findByPrimaryKey( lTaskTask );
      assertTrue( lTaskTaskTable.exists() );

      // Asserts step is created
      TaskStepKey lTaskStepKey = new TaskStepKey( lTaskTask, 1 );

      TaskStepTable lTaskStepTable = TaskStepTable.findByPrimaryKey( lTaskStepKey );
      assertTrue( lTaskStepTable.exists() );

      // Asserts step skill is created
      TaskStepSkillTable lTaskStepSkillTable = iTaskStepSkillDao
            .findByPrimaryKey( new TaskStepSkillKey( lTaskStepKey, RefLabourSkillKey.ENG ) );
      assertTrue( lTaskStepSkillTable.exists() );

      // ACTION: Call delete() method
      TaskDefnService.delete( lTaskTask, false );

      // Asserts task is deleted
      lTaskTaskTable = TaskTaskTable.findByPrimaryKey( lTaskTask );
      assertFalse( lTaskTaskTable.exists() );

      // Asserts step is deleted
      lTaskStepTable = TaskStepTable.findByPrimaryKey( lTaskStepKey );
      assertFalse( lTaskStepTable.exists() );

      // Asserts step skill is deleted
      lTaskStepSkillTable = iTaskStepSkillDao
            .findByPrimaryKey( new TaskStepSkillKey( lTaskStepKey, RefLabourSkillKey.ENG ) );
      assertFalse( lTaskStepSkillTable.exists() );
   }


   /**
    * Test that child records for weight and balance are deleted when the requirement definition is
    * deleted.
    */
   @Test
   public void testWeightAndBalanceIsDeletedWhenRequirementIsDeleted() throws Exception {

      // ARRANGE
      final TaskTaskKey taskTaskKey = Domain.createRequirementDefinition( req -> {
         req.setStatus( RefTaskDefinitionStatusKey.BUILD );
         req.addWeightAndBalanceImpact( wb -> {
            wb.setBalance( new BigDecimal( 6 ) );
            wb.setWeight( BigDecimal.ONE );
         } );
         req.addWeightAndBalanceImpact( wb -> {
            wb.setBalance( BigDecimal.TEN );
            wb.setWeight( new BigDecimal( 6 ) );
         } );
      } );

      // Asserts task is created
      TaskTaskTable taskTaskTable = TaskTaskTable.findByPrimaryKey( taskTaskKey );
      assertTrue( taskTaskTable.exists() );

      // Asserts weight and balance impacts were created
      List<TaskWeightAndBalanceKey> weightAndBalanceKeyList =
            Domain.readWeightAndBalanceImpacts( taskTaskKey );
      assertEquals( 2, weightAndBalanceKeyList.size() );

      // ACT
      TaskDefnService.delete( taskTaskKey, false );

      // ASSERT
      weightAndBalanceKeyList = Domain.readWeightAndBalanceImpacts( taskTaskKey );
      assertEquals( 0, weightAndBalanceKeyList.size() );
   }


   /**
    *
    * Verify we cannot delete an ACTV REQ definition
    *
    * <pre>
    * Given - an ACTV REQ definition
    * When  - we try deleting the definition
    * Then  - InvalidTaskDefinitionStatusException is thrown
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void itThrowsInvalidTaskDefinitionStatusExceptionWhenDeleteActvReqDefn()
         throws Exception {

      final TaskTaskKey lActiveReqDefn = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
      } );

      iExpectedException.expect( InvalidTaskDefinitionStatusException.class );

      TaskDefnService.delete( lActiveReqDefn, DO_NOT_DELETE_ENTIRE_BLOCK_CHAIN );

   }


   /**
    *
    * The JIC definition is no longer mapped to the REQ definition once the JIC definition is
    * deleted.
    *
    * <pre>
    * Given - a JIC definition in revision AND
    *         a REQ definition with the JIC definition assigned to it
    * When  - the JIC definition is deleted
    * Then  - the JIC is deleted and the definition is no longer mapped to the REQ definition
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void itRemovesMappingBetweenReqDefnAndJicDefnWhenJicDefnDeleted() throws Exception {

      final TaskTaskKey lRevisionJicDefn = Domain.createJobCardDefinition( aJicDefn -> {
         aJicDefn.setStatus( RefTaskDefinitionStatusKey.REVISION );
      } );

      final TaskTaskKey lReqDefn = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.addJobCardDefinition( lRevisionJicDefn );
      } );

      TaskDefnService.delete( lRevisionJicDefn, DO_NOT_DELETE_ENTIRE_BLOCK_CHAIN );

      TaskTaskTable lReqDefnTaskTaskTable = TaskTaskTable.findByPrimaryKey( lReqDefn );
      TaskTaskTable lRevisionJicDefnTaskTaskTable =
            TaskTaskTable.findByPrimaryKey( lRevisionJicDefn );

      TaskJicReqMapTable lTaskJicReqMapTable = TaskJicReqMapTable.findByPrimaryKey(
            new TaskJicReqMapKey( lRevisionJicDefn, lReqDefnTaskTaskTable.getTaskDefn() ) );

      assertThat( "The JIC is still mapped to the REQ.", lTaskJicReqMapTable.exists(),
            is( false ) );
      assertThat( "The JIC was not deleted.", lRevisionJicDefnTaskTaskTable.exists(), is( false ) );

   }


   /**
    *
    * If a REQ with multiple revisions is assigned to a block, and the latest revision of the REQ is
    * deleted, then the block and REQ definition are still mapped together.
    *
    * <pre>
    * Given - a REQ definition AND
    *         a revision of this REQ (revision 2) definition AND
    *         a block definition with the REQ revision 2 assigned to it
    * When  - the REQ revision 2 is deleted
    * Then  - the REQ definition is still mapped to the block (because revision 1 still exists)
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void itUpdatesReqDefnInBlockDefnToPreviousRevisionWhenLatestRevisionDeleted()
         throws Exception {

      final TaskTaskKey lReqRev1 = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.setRevisionNumber( 1 );
      } );

      final TaskTaskKey lReqRev2 = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.setStatus( RefTaskDefinitionStatusKey.REVISION );
         aReqDefn.setRevisionNumber( 2 );
         aReqDefn.setPreviousRevision( lReqRev1 );
      } );

      final TaskTaskKey lRevisionBlockDefn = Domain.createBlockDefinition( aBlockDefn -> {
         aBlockDefn.addRequirementDefinition( lReqRev2 );
      } );

      TaskDefnService.delete( lReqRev2, DO_NOT_DELETE_ENTIRE_BLOCK_CHAIN );

      TaskTaskTable lReqRev1TaskTaskTable = TaskTaskTable.findByPrimaryKey( lReqRev1 );

      TaskBlockReqMapTable lTaskBlockReqMapTable = TaskBlockReqMapTable.findByPrimaryKey(
            new TaskBlockReqMapKey( lRevisionBlockDefn, lReqRev1TaskTaskTable.getTaskDefn() ) );

      assertThat( "The previous REQ revision was not assigned to the block.",
            lTaskBlockReqMapTable.exists(), is( true ) );

   }


   @Test
   public void testSetStepWithSkills() throws Exception {

      // DATA SETUP: Create an executable requirement
      TaskTaskKey lTaskTask =
            new TaskRevisionBuilder().withTaskCode( "test" ).withTaskClass( RefTaskClassKey.REQ )
                  .withStatus( RefTaskDefinitionStatusKey.REVISION ).isWorkscoped().build();

      // DATA SETUP: Add labor requirements to the task
      new LabourRequirementBuilder( lTaskTask, RefLabourSkillKey.PILOT ).withCertRequired( true )
            .withInspRequired( true ).build();

      new LabourRequirementBuilder( lTaskTask, RefLabourSkillKey.LBR ).withCertRequired( true )
            .withInspRequired( true ).build();

      new LabourRequirementBuilder( lTaskTask, RefLabourSkillKey.ENG ).withCertRequired( true )
            .withInspRequired( true ).withInspRequired( true ).build();

      // DATA SETUP: Add Step with step skills
      TaskStepKey lTaskStepKey = new TaskStepBuilder().withTaskTask( lTaskTask )
            .withStepSkill( RefLabourSkillKey.ENG, true )
            .withStepSkill( RefLabourSkillKey.LBR, false )
            .withStepSkill( RefLabourSkillKey.PILOT, true ).build();

      // Assert that the step skills are added correctly
      TaskStepSkillTable lTaskStepSkillTable = iTaskStepSkillDao
            .findByPrimaryKey( new TaskStepSkillKey( lTaskStepKey, RefLabourSkillKey.ENG ) );

      assertTrue( lTaskStepSkillTable.exists() );
      assertTrue( lTaskStepSkillTable.getInsp() );

      lTaskStepSkillTable = iTaskStepSkillDao
            .findByPrimaryKey( new TaskStepSkillKey( lTaskStepKey, RefLabourSkillKey.LBR ) );

      assertTrue( lTaskStepSkillTable.exists() );
      assertFalse( lTaskStepSkillTable.getInsp() );

      lTaskStepSkillTable = iTaskStepSkillDao
            .findByPrimaryKey( new TaskStepSkillKey( lTaskStepKey, RefLabourSkillKey.PILOT ) );

      assertTrue( lTaskStepSkillTable.exists() );
      assertTrue( lTaskStepSkillTable.getInsp() );

      // Set task step skills
      TaskStepTO.StepSkill lStepSkill1 = new TaskStepTO.StepSkill();
      lStepSkill1.setLabourSkill( RefLabourSkillKey.ENG );
      lStepSkill1.setInspReq( false );

      TaskStepTO.StepSkill lStepSkill2 = new TaskStepTO.StepSkill();
      lStepSkill2.setLabourSkill( RefLabourSkillKey.LBR );
      lStepSkill2.setInspReq( true );

      Collection<TaskStepTO.StepSkill> lStepSkills = new ArrayList<TaskStepTO.StepSkill>();
      lStepSkills.add( lStepSkill1 );
      lStepSkills.add( lStepSkill2 );

      TaskStepTO lTaskStepTO = new TaskStepTO();
      lTaskStepTO.setTaskStepKey( lTaskStepKey );
      lTaskStepTO.addStepSkill( lStepSkill1 );
      lTaskStepTO.addStepSkill( lStepSkill2 );
      lTaskStepTO.setDescription( "description" );

      Collection<TaskStepTO> lTaskStepTOs = new ArrayList<TaskStepTO>();
      lTaskStepTOs.add( lTaskStepTO );

      TaskDefnService.setSteps( lTaskTask, lTaskStepTOs );

      // Assert that the step skills are updated correctly
      lTaskStepSkillTable = iTaskStepSkillDao
            .findByPrimaryKey( new TaskStepSkillKey( lTaskStepKey, RefLabourSkillKey.ENG ) );

      assertTrue( lTaskStepSkillTable.exists() );
      assertFalse( lTaskStepSkillTable.getInsp() );

      lTaskStepSkillTable = iTaskStepSkillDao
            .findByPrimaryKey( new TaskStepSkillKey( lTaskStepKey, RefLabourSkillKey.LBR ) );

      assertTrue( lTaskStepSkillTable.exists() );
      assertTrue( lTaskStepSkillTable.getInsp() );

      // Asserts that this step skill is removed
      lTaskStepSkillTable = iTaskStepSkillDao
            .findByPrimaryKey( new TaskStepSkillKey( lTaskStepKey, RefLabourSkillKey.PILOT ) );

      assertFalse( lTaskStepSkillTable.exists() );
   }


   @Test
   public void testRemoveStepWithStepSkills() throws Exception {

      // DATA SETUP: Create an executable requirement
      TaskTaskKey lTaskTask =
            new TaskRevisionBuilder().withTaskCode( "test" ).withTaskClass( RefTaskClassKey.REQ )
                  .withStatus( RefTaskDefinitionStatusKey.REVISION ).isWorkscoped().build();

      // DATA SETUP: Add labor requirements to the task
      new LabourRequirementBuilder( lTaskTask, RefLabourSkillKey.ENG ).withCertRequired( true )
            .withInspRequired( true ).build();

      new LabourRequirementBuilder( lTaskTask, RefLabourSkillKey.LBR ).withCertRequired( true )
            .withInspRequired( true ).build();

      // DATA SETUP: Add Step with step skills
      TaskStepKey lTaskStepKey = new TaskStepBuilder().withTaskTask( lTaskTask )
            .withStepSkill( RefLabourSkillKey.ENG, true )
            .withStepSkill( RefLabourSkillKey.LBR, false ).build();

      // Assert that the step skills are added correctly
      TaskStepSkillTable lTaskStepSkillTable = iTaskStepSkillDao
            .findByPrimaryKey( new TaskStepSkillKey( lTaskStepKey, RefLabourSkillKey.ENG ) );

      assertTrue( lTaskStepSkillTable.exists() );

      lTaskStepSkillTable = iTaskStepSkillDao
            .findByPrimaryKey( new TaskStepSkillKey( lTaskStepKey, RefLabourSkillKey.LBR ) );

      assertTrue( lTaskStepSkillTable.exists() );

      TaskStepTable lTaskStepTable =
            TaskStepTable.findByPrimaryKey( new TaskStepKey( lTaskTask, 1 ) );

      assertTrue( lTaskStepTable.exists() );

      // Remove Steps
      TaskDefnService.removeSteps( lTaskTask, new TaskStepKey[] { lTaskStepKey } );

      // Asserts that the step is removed
      lTaskStepTable = TaskStepTable.findByPrimaryKey( new TaskStepKey( lTaskTask, 1 ) );

      assertFalse( lTaskStepTable.exists() );

      // Asserts that the step skills are removed correctly
      lTaskStepSkillTable = iTaskStepSkillDao
            .findByPrimaryKey( new TaskStepSkillKey( lTaskStepKey, RefLabourSkillKey.ENG ) );

      assertFalse( lTaskStepSkillTable.exists() );

      lTaskStepSkillTable = iTaskStepSkillDao
            .findByPrimaryKey( new TaskStepSkillKey( lTaskStepKey, RefLabourSkillKey.LBR ) );

      assertFalse( lTaskStepSkillTable.exists() );
   }


   /**
    * Verify that all steps may be removed from a task.
    *
    * @throws Exception
    */
   @Test
   public void testRemoveStepsWhenRemovingAllSteps() throws Exception {

      // Create a task revision with BUILD status.
      TaskTaskKey lTaskRev = new TaskRevisionBuilder().withStatus( BUILD ).build();

      // Create a task step for the task defn.
      TaskStepKey lTaskStep1Key = new TaskStepKey( lTaskRev, 1 );
      TaskStepTable lTaskStep = TaskStepTable.create( lTaskStep1Key );
      lTaskStep.setStepOrd( 1 );
      lTaskStep.insert();

      // Create another task step for the task defn.
      TaskStepKey lTaskStep2Key = new TaskStepKey( lTaskRev, 2 );
      lTaskStep = TaskStepTable.create( lTaskStep2Key );
      lTaskStep.setStepOrd( 2 );
      lTaskStep.insert();

      // Ensure the task steps exist.
      assertTrue( TaskStepTable.findByPrimaryKey( lTaskStep1Key ).exists() );
      assertTrue( TaskStepTable.findByPrimaryKey( lTaskStep2Key ).exists() );

      // Remove all the task steps.
      TaskDefnService.removeSteps( lTaskRev, new TaskStepKey[] { lTaskStep1Key, lTaskStep2Key } );

      // Verify that both the task steps were removed.
      assertFalse( TaskStepTable.findByPrimaryKey( lTaskStep1Key ).exists() );
      assertFalse( TaskStepTable.findByPrimaryKey( lTaskStep2Key ).exists() );
   }


   /**
    * Verify that the step order is maintained when the first step of many steps is removed from a
    * task.
    *
    * @throws Exception
    */
   @Test
   public void testRemoveStepsWhenRemovingFirstStep() throws Exception {

      // Create a task revision with BUILD status.
      TaskTaskKey lTaskRev = new TaskRevisionBuilder().withStatus( BUILD ).build();

      // Create 3 steps for the task defn, but with their step order value differing from their
      // step id value :<br>
      // - step A(1) order 3 <br>
      // - step B(2) order 1 <br>
      // - step C(3) order 2 <br>
      // Thus, ordered as B,C,A.

      TaskStepKey lTaskStepA = new TaskStepKey( lTaskRev, 1 );
      TaskStepTable lTaskStep = TaskStepTable.create( lTaskStepA );
      lTaskStep.setStepOrd( 3 );
      lTaskStep.insert();

      TaskStepKey lTaskStepB = new TaskStepKey( lTaskRev, 2 );
      lTaskStep = TaskStepTable.create( lTaskStepB );
      lTaskStep.setStepOrd( 1 );
      lTaskStep.insert();

      TaskStepKey lTaskStepC = new TaskStepKey( lTaskRev, 3 );
      lTaskStep = TaskStepTable.create( lTaskStepC );
      lTaskStep.setStepOrd( 2 );
      lTaskStep.insert();

      // Ensure the task steps exist and are in proper order.
      lTaskStep = TaskStepTable.findByPrimaryKey( lTaskStepB );
      assertTrue( lTaskStep.exists() );
      assertEquals( 1, lTaskStep.getStepOrd() );

      lTaskStep = TaskStepTable.findByPrimaryKey( lTaskStepC );
      assertTrue( lTaskStep.exists() );
      assertEquals( 2, lTaskStep.getStepOrd() );

      lTaskStep = TaskStepTable.findByPrimaryKey( lTaskStepA );
      assertTrue( lTaskStep.exists() );
      assertEquals( 3, lTaskStep.getStepOrd() );

      // Remove the first task step.
      TaskDefnService.removeSteps( lTaskRev, new TaskStepKey[] { lTaskStepB } );

      // Verify that remaining task steps maintained their order and receive new step order values.
      lTaskStep = TaskStepTable.findByPrimaryKey( lTaskStepC );
      assertTrue( lTaskStep.exists() );
      assertEquals( 1, lTaskStep.getStepOrd() );

      lTaskStep = TaskStepTable.findByPrimaryKey( lTaskStepA );
      assertTrue( lTaskStep.exists() );
      assertEquals( 2, lTaskStep.getStepOrd() );
   }


   /**
    * Verify that the step order is maintained when an intermediary step of many steps is removed
    * from a task.
    *
    * @throws Exception
    */
   @Test
   public void testRemoveStepsWhenRemovingMiddleStep() throws Exception {

      // Create a task revision with BUILD status.
      TaskTaskKey lTaskRev = new TaskRevisionBuilder().withStatus( BUILD ).build();

      // Create 3 steps for the task defn, but with their step order value differing from their
      // step id value :<br>
      // - step A(1) order 3 <br>
      // - step B(2) order 1 <br>
      // - step C(3) order 2 <br>
      // Thus, ordered as B,C,A.

      TaskStepKey lTaskStepA = new TaskStepKey( lTaskRev, 1 );
      TaskStepTable lTaskStep = TaskStepTable.create( lTaskStepA );
      lTaskStep.setStepOrd( 3 );
      lTaskStep.insert();

      TaskStepKey lTaskStepB = new TaskStepKey( lTaskRev, 2 );
      lTaskStep = TaskStepTable.create( lTaskStepB );
      lTaskStep.setStepOrd( 1 );
      lTaskStep.insert();

      TaskStepKey lTaskStepC = new TaskStepKey( lTaskRev, 3 );
      lTaskStep = TaskStepTable.create( lTaskStepC );
      lTaskStep.setStepOrd( 2 );
      lTaskStep.insert();

      // Ensure the task steps exist and are in proper order.
      lTaskStep = TaskStepTable.findByPrimaryKey( lTaskStepB );
      assertTrue( lTaskStep.exists() );
      assertEquals( 1, lTaskStep.getStepOrd() );

      lTaskStep = TaskStepTable.findByPrimaryKey( lTaskStepC );
      assertTrue( lTaskStep.exists() );
      assertEquals( 2, lTaskStep.getStepOrd() );

      lTaskStep = TaskStepTable.findByPrimaryKey( lTaskStepA );
      assertTrue( lTaskStep.exists() );
      assertEquals( 3, lTaskStep.getStepOrd() );

      // Remove the middle task step.
      TaskDefnService.removeSteps( lTaskRev, new TaskStepKey[] { lTaskStepC } );

      // Verify that remaining task steps maintained their order and receive new step order values.
      lTaskStep = TaskStepTable.findByPrimaryKey( lTaskStepB );
      assertTrue( lTaskStep.exists() );
      assertEquals( 1, lTaskStep.getStepOrd() );

      lTaskStep = TaskStepTable.findByPrimaryKey( lTaskStepA );
      assertTrue( lTaskStep.exists() );
      assertEquals( 2, lTaskStep.getStepOrd() );
   }


   /**
    * Verify that the step order is maintained when multiple steps are removed from a task.
    *
    * @throws Exception
    */
   @Test
   public void testRemoveStepsWhenRemovingMultipleSteps() throws Exception {

      // Create a task revision with BUILD status.
      TaskTaskKey lTaskRev = new TaskRevisionBuilder().withStatus( BUILD ).build();

      // Create 5 steps for the task defn, but with their step order value differing from their
      // step id value :<br>
      // - step A(1) order 3 <br>
      // - step B(2) order 1 <br>
      // - step C(3) order 5 <br>
      // - step D(4) order 2 <br>
      // - step E(5) order 4 <br>
      // Thus, ordered as B,D,A,E,C

      TaskStepKey lTaskStepA = new TaskStepKey( lTaskRev, 1 );
      TaskStepTable lTaskStep = TaskStepTable.create( lTaskStepA );
      lTaskStep.setStepOrd( 3 );
      lTaskStep.insert();

      TaskStepKey lTaskStepB = new TaskStepKey( lTaskRev, 2 );
      lTaskStep = TaskStepTable.create( lTaskStepB );
      lTaskStep.setStepOrd( 1 );
      lTaskStep.insert();

      TaskStepKey lTaskStepC = new TaskStepKey( lTaskRev, 3 );
      lTaskStep = TaskStepTable.create( lTaskStepC );
      lTaskStep.setStepOrd( 5 );
      lTaskStep.insert();

      TaskStepKey lTaskStepD = new TaskStepKey( lTaskRev, 4 );
      lTaskStep = TaskStepTable.create( lTaskStepD );
      lTaskStep.setStepOrd( 2 );
      lTaskStep.insert();

      TaskStepKey lTaskStepE = new TaskStepKey( lTaskRev, 5 );
      lTaskStep = TaskStepTable.create( lTaskStepE );
      lTaskStep.setStepOrd( 4 );
      lTaskStep.insert();

      // Ensure the task steps exist and are in proper order.
      lTaskStep = TaskStepTable.findByPrimaryKey( lTaskStepB );
      assertTrue( lTaskStep.exists() );
      assertEquals( 1, lTaskStep.getStepOrd() );

      lTaskStep = TaskStepTable.findByPrimaryKey( lTaskStepD );
      assertTrue( lTaskStep.exists() );
      assertEquals( 2, lTaskStep.getStepOrd() );

      lTaskStep = TaskStepTable.findByPrimaryKey( lTaskStepA );
      assertTrue( lTaskStep.exists() );
      assertEquals( 3, lTaskStep.getStepOrd() );

      lTaskStep = TaskStepTable.findByPrimaryKey( lTaskStepE );
      assertTrue( lTaskStep.exists() );
      assertEquals( 4, lTaskStep.getStepOrd() );

      lTaskStep = TaskStepTable.findByPrimaryKey( lTaskStepC );
      assertTrue( lTaskStep.exists() );
      assertEquals( 5, lTaskStep.getStepOrd() );

      // Remove the multiple task step.
      TaskDefnService.removeSteps( lTaskRev,
            new TaskStepKey[] { lTaskStepB, lTaskStepA, lTaskStepE } );

      // Verify that remaining task steps maintained their order and receive new step order values.
      lTaskStep = TaskStepTable.findByPrimaryKey( lTaskStepD );
      assertTrue( lTaskStep.exists() );
      assertEquals( 1, lTaskStep.getStepOrd() );

      lTaskStep = TaskStepTable.findByPrimaryKey( lTaskStepC );
      assertTrue( lTaskStep.exists() );
      assertEquals( 2, lTaskStep.getStepOrd() );
   }


   /**
    * Verify that the last step may be removed from a task.
    *
    * @throws Exception
    */
   @Test
   public void testRemoveStepsWhenRemovingTheLastStep() throws Exception {

      // Create a task revision with BUILD status.
      TaskTaskKey lTaskRev = new TaskRevisionBuilder().withStatus( BUILD ).build();

      // Create a task step for the task defn.
      TaskStepKey lTaskStepKey = new TaskStepKey( lTaskRev, 1 );
      TaskStepTable lTaskStep = TaskStepTable.create( lTaskStepKey );
      lTaskStep.setStepOrd( 1 );
      lTaskStep.insert();

      // Ensure the task step exists.
      assertTrue( TaskStepTable.findByPrimaryKey( lTaskStepKey ).exists() );

      // Remove the only task step.
      TaskDefnService.removeSteps( lTaskRev, new TaskStepKey[] { lTaskStepKey } );

      // Verify the task step was removed.
      assertFalse( TaskStepTable.findByPrimaryKey( lTaskStepKey ).exists() );
   }


   /**
    * Verify that all steps will be removed from a task.
    *
    * @throws Exception
    */
   @Test
   public void testRemoveAllSteps() throws Exception {

      // Create a task revision with BUILD status.
      TaskTaskKey lTaskRev = new TaskRevisionBuilder().withStatus( BUILD ).build();

      // Create a task step for the task defn.
      TaskStepKey lTaskStep1Key = new TaskStepKey( lTaskRev, 1 );
      TaskStepTable lTaskStep = TaskStepTable.create( lTaskStep1Key );
      lTaskStep.setStepOrd( 1 );
      lTaskStep.insert();

      // Create another task step for the task defn.
      TaskStepKey lTaskStep2Key = new TaskStepKey( lTaskRev, 2 );
      lTaskStep = TaskStepTable.create( lTaskStep2Key );
      lTaskStep.setStepOrd( 2 );
      lTaskStep.insert();

      // Ensure the task steps exist.
      assertTrue( TaskStepTable.findByPrimaryKey( lTaskStep1Key ).exists() );
      assertTrue( TaskStepTable.findByPrimaryKey( lTaskStep2Key ).exists() );

      // Remove all the task steps by the task task key.
      TaskDefnService.removeAllSteps( lTaskRev );

      // Verify that both the task steps were removed.
      assertFalse( TaskStepTable.findByPrimaryKey( lTaskStep1Key ).exists() );
      assertFalse( TaskStepTable.findByPrimaryKey( lTaskStep2Key ).exists() );
   }


   /**
    * <pre>
    *    Given that I am a System Engineer and I have created a new requirement definition
    *        and the requirement definition is applicable to an inventory
    *        and the requirement definition is scheduled from a provided effective date
    *        and the requirement has a calendar based scheduling rule with a threshold
    *        when I activate the requirement
    *        then the requirement should be initialized against the inventory
    *        and the calendar deadline of the requirement should be set to effective date + threshold
    * </pre>
    */
   @Test
   public void testRequirementDeadlineSetToEffectiveDateAddThreshold() throws Exception {

      /*
       * Create Aircraft. We need to provide the part and the part group information in order for
       * the applicability logic to work properly. Without the part and part group , the
       * applicability logic throws errors when trying to initialize the requirement onto the
       * aircraft
       */
      final PartNoKey lAircraftPart = Domain.createPart();

      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAircraftAssembly ) {
                  aAircraftAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aConfigurationSlot ) {
                              aConfigurationSlot
                                    .addPartGroup( new DomainConfiguration<PartGroup>() {

                                       @Override
                                       public void configure( PartGroup aPartGroup ) {
                                          aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                                          aPartGroup.addPart( lAircraftPart );
                                       }
                                    } );
                           }
                        } );
               }
            } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.setPart( lAircraftPart );
            aAircraft.allowSynchronization();
         }
      } );

      final ConfigSlotKey lAircraftRootConfigSlot = new ConfigSlotKey( lAircraftAssembly, 0 );

      // Requirement Definition with a provided effective date and calendar based scheduling rule
      final TaskTaskKey lReqDefnKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setStatus( REVISION );
                  aReqDefn.setScheduledFromEffectiveDate( EFFECTIVE_DATE );
                  aReqDefn.addSchedulingRule( CDY, THRESHOLD_NUM_DAYS );
               }
            } );

      // activate the requirement
      new TaskDefnService().activate( lReqDefnKey, new TaskDefnRevTO(), HR, true );

      // Emulate baseline sync processing of the inv_sync_queue rows
      // ( added by TaskDefnBean.activate() ).
      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      // Check that the Task was initialized on the inventory
      List<TaskKey> lTasks =
            TaskDefnUtils.getActualTasks( lReqDefnKey, lAircraft, RefEventStatusKey.ACTV );

      Assert.assertEquals(
            "Requirement was not initialized on the inventory even though it was applicable", 1,
            lTasks.size() );

      // Actual task is initialized with the correct deadline.
      Date lTaskDeadline =
            EvtSchedDeadTable.findByPrimaryKey( lTasks.get( 0 ), CDY ).getDeadlineDate();

      Date lExpectedDeadlineDate =
            getEndOfDay( addDays( EFFECTIVE_DATE, THRESHOLD_NUM_DAYS.intValue() ) );

      Assert.assertEquals(
            "Task initialized from requirement definition scheduled from effective date with a calendar based scheduling rule has incorrect deadline",
            DATE_FORMAT_WITHOUT_MS.format( lExpectedDeadlineDate ),
            DATE_FORMAT_WITHOUT_MS.format( lTaskDeadline ) );
   }


   /**
    * <pre>
    *    Given that I am a System Engineer and I have created a new requirement definition
    *        and the requirement definition is applicable to an inventory
    *        and the requirement definition is scheduled from effective date
    *        and the requirement definition has both effective date and due date set
    *        when I activate the requirement definition
    *        then the requirement should be initialized on the applicable inventory
    *        and the calendar deadline of the requirement should be set to the due date
    * </pre>
    */
   @Test
   public void testRequirementDeadlineSetToDueDate() throws Exception {

      /*
       * Create Aircraft. We need to provide the part and the part group information in order for
       * the applicability logic to work properly. Without the part and part group , the
       * applicability logic throws errors when trying to initialize the requirement onto the
       * aircraft
       */
      final PartNoKey lAircraftPart = Domain.createPart();

      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAircraftAssembly ) {
                  aAircraftAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aConfigurationSlot ) {
                              aConfigurationSlot
                                    .addPartGroup( new DomainConfiguration<PartGroup>() {

                                       @Override
                                       public void configure( PartGroup aPartGroup ) {
                                          aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                                          aPartGroup.addPart( lAircraftPart );
                                       }
                                    } );
                           }
                        } );
               }
            } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.setPart( lAircraftPart );
            aAircraft.allowSynchronization();
         }
      } );

      final ConfigSlotKey lAircraftRootConfigSlot = new ConfigSlotKey( lAircraftAssembly, 0 );

      // Requirement Definition with Effective and Due Date
      final TaskTaskKey lReqDefnKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setStatus( REVISION );
                  aReqDefn.setScheduledFromEffectiveDate( EFFECTIVE_DATE );
                  aReqDefn.setDueDate( DUE_DATE );
               }
            } );

      // Activate the Requirement
      new TaskDefnService().activate( lReqDefnKey, new TaskDefnRevTO(), HR, true );

      // Emulate baseline sync processing of the inv_sync_queue rows
      // ( added by TaskDefnBean.activate() ).
      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      // Check that the Task was initialized correctly on the inventory
      List<TaskKey> lTasks =
            TaskDefnUtils.getActualTasks( lReqDefnKey, lAircraft, RefEventStatusKey.ACTV );

      Assert.assertEquals(
            "Requirement was not initialized on the inventory even though it was applicable", 1,
            lTasks.size() );

      // Check Deadline Date of the Actual Task
      Date lTaskDeadline =
            EvtSchedDeadTable.findByPrimaryKey( lTasks.get( 0 ), CDY ).getDeadlineDate();

      Date lExpectedDeadlineDate = getEndOfDay( DUE_DATE );

      Assert.assertEquals(
            "Task initialized from requirement definition scheduled from effective date with a due date has incorrect deadline",
            DATE_FORMAT_WITHOUT_MS.format( lExpectedDeadlineDate ),
            DATE_FORMAT_WITHOUT_MS.format( lTaskDeadline ) );
   }


   /**
    * <pre>
    *    Given a requirement exists with a calendar based scheduling rule (has an interval)
    *      And a task exists that is based on that requirement
    *      And the requirement is revised and the scheduling rule interval is modified
    *     When the revised requirement is activated
    *     Then the task is updated with the new interval
    * </pre>
    *
    */
   @Test
   public void activatingRevisedRequirementUpdatesTaskDeadlineInterval() throws Exception {

      // Set up support data.
      final PartNoKey lAircraftPart = Domain.createPart();
      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAircraftAssembly ) {
                  aAircraftAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aConfigurationSlot ) {
                              aConfigurationSlot
                                    .addPartGroup( new DomainConfiguration<PartGroup>() {

                                       @Override
                                       public void configure( PartGroup aPartGroup ) {
                                          aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                                          aPartGroup.addPart( lAircraftPart );
                                       }
                                    } );
                           }
                        } );
               }
            } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.setPart( lAircraftPart );
            aAircraft.allowSynchronization();
         }
      } );

      final ConfigSlotKey lAircraftRootConfigSlot = new ConfigSlotKey( lAircraftAssembly, 0 );

      // Given a requirement exists with a calendar based scheduling rule (has an interval).
      final TaskTaskKey lReqDefnKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setCode( REQ_DEFN_CODE );
                  aReqDefn.setRevisionNumber( REVISION_1 );
                  aReqDefn.setStatus( ACTV );
                  aReqDefn.addSchedulingRule( CDY, ONE );
               }
            } );

      // Given a task exists that is based on that requirement.
      TaskKey lReqTask =
            new CreationService().createTaskFromDefinition( lAircraft, lReqDefnKey, null, HR );

      // Given the requirement is revised and the scheduling rule interval is modified.
      final TaskTaskKey lRevisedReqDefnKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setPreviousRevision( lReqDefnKey );
                  aReqDefn.setRevisionNumber( REVISION_2 );
                  aReqDefn.setStatus( REVISION );
                  aReqDefn.addSchedulingRule( CDY, TEN );
               }
            } );

      // When the revised requirement is activated.
      new TaskDefnService().activate( lRevisedReqDefnKey, new TaskDefnRevTO(), HR, true );

      // Emulate baseline sync processing of the inv_sync_queue rows
      // ( added by TaskDefnService.activate() ).
      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      // Then the task is updated with the new interval.
      BigDecimal lTaskInterval =
            new BigDecimal( EvtSchedDeadTable.findByPrimaryKey( lReqTask, CDY ).getIntervalQt() );
      assertEquals( "Unexpected interval value.", TEN, lTaskInterval );

   }


   /**
    *
    * Verify that when the scheduling interval of a recurring requirement definition is revised that
    * both the active requirement and forecast requirement tasks' due dates are adjusted by the
    * interval difference.
    *
    * @throws Exception
    */
   @Test
   public void itUpdatesRecurringRequirementDueDateWhenSchedulingIntervalOfDefinitionIsRevised()
         throws Exception {

      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly();

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.allowSynchronization();
         }
      } );

      final ConfigSlotKey lAircraftRootConfigSlot = new ConfigSlotKey( lAircraftAssembly, 0 );

      //
      // Given an activated recurring requirement definition exists with a usage based scheduling
      // rule (has an interval).
      //
      final TaskTaskKey lActivatedReqDefnKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setCode( REQ_DEFN_CODE );
                  aReqDefn.setRevisionNumber( REVISION_1 );
                  aReqDefn.setRecurring( true );
                  aReqDefn.setStatus( ACTV );
                  aReqDefn.setScheduledFromEffectiveDate( EFFECTIVE_DATE );
                  aReqDefn.addRecurringSchedulingRule( CDY, REPEAT_INTERVAL );
               }
            } );

      //
      // Given a revision of the activated recurring requirement definition.
      //
      final RecurringSchedulingRule lNewRecurringSchedulingRule =
            new RecurringSchedulingRule( CDY, NEW_REPEAT_INTERVAL );
      final TaskTaskKey lRevReqDefnKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.setPreviousRevision( lActivatedReqDefnKey );
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setCode( REQ_DEFN_CODE );
                  aReqDefn.setRevisionNumber( REVISION_2 );
                  aReqDefn.setRecurring( true );
                  aReqDefn.setStatus( REVISION );
                  aReqDefn.setScheduledFromEffectiveDate( EFFECTIVE_DATE );
                  aReqDefn.addRecurringSchedulingRule( lNewRecurringSchedulingRule );
               }

            } );

      //
      // Given an active requirement task that is based on the activated requirement
      // definition with a deadline that corresponds to the scheduling rule.
      //
      final Date lActvReqTaskDeadline =
            getEndOfDay( addDays( EFFECTIVE_DATE, REPEAT_INTERVAL.intValue() ) );
      final TaskKey lActvReqTask =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aReq ) {
                  aReq.setDefinition( lActivatedReqDefnKey );
                  aReq.setInventory( lAircraft );
                  aReq.setStatus( RefEventStatusKey.ACTV );
                  aReq.addCalendarDeadline( CDY, REPEAT_INTERVAL, lActvReqTaskDeadline );
               }
            } );

      //
      // Given a forecast requirement task whose previous task is the active requirement task.
      //
      final Date lForecastReqTaskDeadline =
            getEndOfDay( addDays( lActvReqTaskDeadline, REPEAT_INTERVAL.intValue() ) );
      final TaskKey lForecastReqTask =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aReq ) {
                  aReq.setPreviousTask( lActvReqTask );
                  aReq.setDefinition( lActivatedReqDefnKey );
                  aReq.setInventory( lAircraft );
                  aReq.setStatus( RefEventStatusKey.FORECAST );
                  aReq.addCalendarDeadline( CDY, REPEAT_INTERVAL, lForecastReqTaskDeadline );
               }
            } );

      //
      // When the revised recurring requirement is activated.
      //
      new TaskDefnService().activate( lRevReqDefnKey, new TaskDefnRevTO(), HR, true );

      // Emulate baseline sync processing of the inv_sync_queue rows
      // ( added by TaskDefnService.activate() ).
      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      // Emulate the update task deadlines job.
      new UpdateTaskDeadlinesCoreService().updateTaskDeadlines();

      //
      // Then the deadline of the active requirement task is updated using the new interval of the
      // revised recurring requirement definition.
      //
      Date lExpectedActiveDeadlineDate =
            getEndOfDay( addDays( EFFECTIVE_DATE, NEW_REPEAT_INTERVAL.intValue() ) );
      Date lActualActiveDeadlineDate =
            EvtSchedDeadTable.findByPrimaryKey( lActvReqTask, CDY ).getDeadlineDate();

      assertEquals( "Unexpected deadline date for the active requirement",
            DATE_FORMAT_WITHOUT_MS.format( lExpectedActiveDeadlineDate ),
            DATE_FORMAT_WITHOUT_MS.format( lActualActiveDeadlineDate ) );

      // Then the deadline of the forecast requirement task is updated using the new interval of the
      // revised recurring requirement definition.
      Date lExpectedForecastDeadlineDate =
            getEndOfDay( addDays( lExpectedActiveDeadlineDate, NEW_REPEAT_INTERVAL.intValue() ) );
      Date lActualForecastDeadlineDate =
            EvtSchedDeadTable.findByPrimaryKey( lForecastReqTask, CDY ).getDeadlineDate();

      assertEquals( "Unexpected deadline date for the forecast requirement",
            DATE_FORMAT_WITHOUT_MS.format( lExpectedForecastDeadlineDate ),
            DATE_FORMAT_WITHOUT_MS.format( lActualForecastDeadlineDate ) );

   }


   @Test
   public void bulk_activate_RevisedRequirement_WithRevisionInformation() throws Exception {

      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly();

      Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
         }
      } );

      final ConfigSlotKey lAircraftRootConfigSlot = new ConfigSlotKey( lAircraftAssembly, 0 );

      //
      // Given an activated recurring requirement definition exists
      //
      final TaskTaskKey lActivatedReqDefnKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setCode( REQ_DEFN_CODE );
                  aReqDefn.setRevisionNumber( REVISION_1 );
                  aReqDefn.setRecurring( true );
                  aReqDefn.setStatus( ACTV );
                  aReqDefn.setScheduledFromEffectiveDate( EFFECTIVE_DATE );
                  aReqDefn.addRecurringSchedulingRule( CDY, REPEAT_INTERVAL );
               }
            } );

      //
      // Given a revision of the activated recurring requirement definition with revision
      // information
      //
      final TaskTaskKey lRevReqDefnKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.setPreviousRevision( lActivatedReqDefnKey );
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setCode( REQ_DEFN_CODE );
                  aReqDefn.setRevisionNumber( REVISION_2 );
                  aReqDefn.setRecurring( true );
                  aReqDefn.setStatus( REVISION );
                  aReqDefn.setScheduledFromEffectiveDate( EFFECTIVE_DATE );
                  aReqDefn.addRecurringSchedulingRule( CDY, NEW_REPEAT_INTERVAL );
                  aReqDefn.setRevisionNote( REVISION_NOTE );
                  aReqDefn.setRevisionReason( TASK_REVISION_REASON_KEY );
               }

            } );

      // set the transfer object simulating a bulk activation (no reason and notes)
      TaskDefnRevTO lTaskDefnRevTO = new TaskDefnRevTO();
      lTaskDefnRevTO.setRevisionNotes( null, null );
      lTaskDefnRevTO.setTaskRevisionReason( null, null );

      // activate the revision
      new TaskDefnService().activate( lRevReqDefnKey, lTaskDefnRevTO, HR, true, true );

      // assert the revision reason and notes are not altered after the bulk activation
      TaskTaskTable lTaskTaskTable = TaskTaskTable.findByPrimaryKey( lRevReqDefnKey );
      assertEquals( TASK_REVISION_REASON_KEY, lTaskTaskTable.getTaskRevReason() );
      assertEquals( REVISION_NOTE, lTaskTaskTable.getTaskRefSdesc() );
   }


   /**
    * This test case is when a non-recurring follow on task definition is activated, the unique bool
    * is set to false.
    *
    * <pre>
    *    Given a non-recurring follow-on task definition in revision status
    *    When activate it
    *    Then verify it is activated
    *    And the unique bool is set to false
    * </pre>
    *
    */
   @Test
   public void itSetsUniqueBoolToFalseForNonRecurringFollowTask() throws Exception {

      TaskTaskKey lFollowTaskDefn = Domain.createRequirementDefinition( aFollowTaskDefn -> {
         aFollowTaskDefn.setTaskClass( RefTaskClassKey.FOLLOW );
         aFollowTaskDefn.setStatus( REVISION );
         aFollowTaskDefn.setRecurring( false );
      } );

      TaskDefnService lTaskDefnService = new TaskDefnService();

      TaskDefnRevTO lTaskDefnRevTO = new TaskDefnRevTO();

      lTaskDefnService.activate( lFollowTaskDefn, lTaskDefnRevTO, HR, FORCE_ACTIVATE );

      TaskTaskTable lActivatedTaskDefn = TaskTaskTable.findByPrimaryKey( lFollowTaskDefn );

      assertEquals( "Follow-on task is not activated successfully.",
            RefTaskDefinitionStatusKey.ACTV, lActivatedTaskDefn.getTaskDefStatus() );

      assertEquals( "Follow-on task's unique is not set to false.", false,
            lActivatedTaskDefn.isUniqueBool() );

   }


   /**
    * This test case is when a recurring follow on task definition is activated, the unique bool is
    * set to false.
    *
    * <pre>
    *    Given a recurring follow-on task definition in revision status
    *    When activate it
    *    Then verify it is activated
    *    And the unique bool is set to false
    * </pre>
    *
    */
   @Test
   public void itSetsUniqueBoolToFalseForRecurringFollowTask() throws Exception {

      TaskTaskKey lFollowTaskDefn = Domain.createRequirementDefinition( aFollowTaskDefn -> {
         aFollowTaskDefn.setTaskClass( RefTaskClassKey.FOLLOW );
         aFollowTaskDefn.setStatus( REVISION );
         aFollowTaskDefn.setRecurring( true );
      } );

      TaskDefnService lTaskDefnService = new TaskDefnService();

      TaskDefnRevTO lTaskDefnRevTO = new TaskDefnRevTO();
      lTaskDefnRevTO.setRevisionNotes( null, null );
      lTaskDefnRevTO.setTaskRevisionReason( null, null );

      lTaskDefnService.activate( lFollowTaskDefn, lTaskDefnRevTO, HR, FORCE_ACTIVATE );

      TaskTaskTable lActivatedTaskDefn = TaskTaskTable.findByPrimaryKey( lFollowTaskDefn );

      assertEquals( "Follow-on task is not activated successfully.",
            RefTaskDefinitionStatusKey.ACTV, lActivatedTaskDefn.getTaskDefStatus() );

      assertEquals( "Follow-on task's unique is not set to false.", false,
            lActivatedTaskDefn.isUniqueBool() );

   }


   /**
    * <pre>
    *    Given that I am a System Engineer and I have added a new scheduling interval of an existing requirement
    *      And there exists an active and forecast tasks based on the recurring requirement
    *     When the revised requirement is activated
    *     Then the new scheduling deadline of the active task and the forecast tasks are updated using the new interval
    * </pre>
    *
    */
   @Test
   public void itUpdatesRecurringRequirementDueDateWhenNewSchedulingAddedToRevisedDefinition()
         throws Exception {
      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly();

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.allowSynchronization();
         }
      } );

      final ConfigSlotKey lAircraftRootConfigSlot = new ConfigSlotKey( lAircraftAssembly, 0 );

      // Given an activated recurring requirement definition.
      final TaskTaskKey lActivatedReqDefnKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setCode( REQ_DEFN_CODE );
                  aReqDefn.setRevisionNumber( REVISION_1 );
                  aReqDefn.setRecurring( true );
                  aReqDefn.setStatus( ACTV );
                  aReqDefn.setScheduledFromEffectiveDate( EFFECTIVE_DATE );
               }
            } );

      // Given a revision of the activated recurring requirement definition exists with added a new
      // scheduling.
      final RecurringSchedulingRule lNewRecurringSchedulingRule =
            new RecurringSchedulingRule( CDY, NEW_REPEAT_INTERVAL );
      final TaskTaskKey lRevReqDefnKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.setPreviousRevision( lActivatedReqDefnKey );
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setCode( REQ_DEFN_CODE );
                  aReqDefn.setRevisionNumber( REVISION_2 );
                  aReqDefn.setRecurring( true );
                  aReqDefn.setStatus( REVISION );
                  aReqDefn.setScheduledFromEffectiveDate( EFFECTIVE_DATE );
                  aReqDefn.addRecurringSchedulingRule( lNewRecurringSchedulingRule );
               }

            } );

      // Given an active requirement task that is based on the activated requirement
      // definition
      final TaskKey lActvReqTask =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aReq ) {
                  aReq.setDefinition( lActivatedReqDefnKey );
                  aReq.setInventory( lAircraft );
                  aReq.setStatus( RefEventStatusKey.ACTV );
               }
            } );

      // Given a forecast requirement task whose previous task is the active requirement task.
      final TaskKey lForecastReqTask =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aReq ) {
                  aReq.setPreviousTask( lActvReqTask );
                  aReq.setDefinition( lActivatedReqDefnKey );
                  aReq.setInventory( lAircraft );
                  aReq.setStatus( RefEventStatusKey.FORECAST );
               }
            } );

      // When the revised recurring requirement is activated.
      new TaskDefnService().activate( lRevReqDefnKey, new TaskDefnRevTO(), HR, true );

      // Emulate baseline sync processing of the inv_sync_queue rows
      // ( added by TaskDefnService.activate() ).
      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      // Emulate the update task deadlines job.
      new UpdateTaskDeadlinesCoreService().updateTaskDeadlines();

      // Then the deadline of the active requirement task is updated using the new interval of the
      // revised recurring requirement definition.
      Date lExpectedActiveDeadlineDate =
            getEndOfDay( addDays( EFFECTIVE_DATE, NEW_REPEAT_INTERVAL.intValue() ) );
      Date lActualActiveDeadlineDate =
            EvtSchedDeadTable.findByPrimaryKey( lActvReqTask, CDY ).getDeadlineDate();

      assertEquals( "Unexpected deadline date for the active requirement",
            DATE_FORMAT_WITHOUT_MS.format( lExpectedActiveDeadlineDate ),
            DATE_FORMAT_WITHOUT_MS.format( lActualActiveDeadlineDate ) );

      // Then the deadline of the forecast requirement task is updated using the new interval of the
      // revised recurring requirement definition.
      Date lExpectedForecastDeadlineDate =
            getEndOfDay( addDays( lExpectedActiveDeadlineDate, NEW_REPEAT_INTERVAL.intValue() ) );
      Date lActualForecastDeadlineDate =
            EvtSchedDeadTable.findByPrimaryKey( lForecastReqTask, CDY ).getDeadlineDate();

      assertEquals( "Unexpected deadline date for the forecast requirement",
            DATE_FORMAT_WITHOUT_MS.format( lExpectedForecastDeadlineDate ),
            DATE_FORMAT_WITHOUT_MS.format( lActualForecastDeadlineDate ) );

   }


   /**
    * <pre>
    *    Given that I am a System Engineer and I have created a new requirement with an Applicability Range
    *        and the requirement is not on-condition
    *        and the requirement is not part of a maintenance program
    *        when I activate the requirement
    *        then the requirement should be initialized against applicable inventory
    * </pre>
    */
   @Test
   public void itInitializesTasksOnInventoryMatchingApplicabilityRange() throws Exception {

      final PartNoKey lAircraftPart = Domain.createPart();

      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAircraftAssembly ) {
                  aAircraftAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aConfigurationSlot ) {
                              aConfigurationSlot
                                    .addPartGroup( new DomainConfiguration<PartGroup>() {

                                       @Override
                                       public void configure( PartGroup aPartGroup ) {
                                          aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                                          aPartGroup.addPart( lAircraftPart );
                                       }
                                    } );
                           }
                        } );
               }
            } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.setPart( lAircraftPart );
            aAircraft.allowSynchronization();
            aAircraft.setApplicabilityCode( ACFT_APPLICABILITY_CODE_WITHIN_RANGE );
         }
      } );

      final ConfigSlotKey lAircraftRootConfigSlot = new ConfigSlotKey( lAircraftAssembly, 0 );

      // Given an activated one-time requirement definition.
      final TaskTaskKey lReqDefnKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setStatus( REVISION );
                  aReqDefn.setApplicabilityRange( TASK_DEFN_APPLICABILITY_RANGE );
               }
            } );

      // When the requirement is activated.
      new TaskDefnService().activate( lReqDefnKey, new TaskDefnRevTO(), HR, true );

      // Emulate baseline sync processing of the inv_sync_queue rows
      // ( added by TaskDefnService.activate() ).
      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      List<TaskKey> lTasks =
            TaskDefnUtils.getActualTasks( lReqDefnKey, lAircraft, RefEventStatusKey.ACTV );

      Assert.assertEquals(
            "Requirement was not initialized on the inventory although the applicability code matched",
            1, lTasks.size(), 0 );
   }


   /**
    * <pre>
    *    Given that I am a System Engineer and I have created a new requirement with an Applicability Range
    *        and the requirement is not on-condition
    *        and the requirement is not part of a maintenance program
    *        when I activate the requirement
    *        then the requirement should be initialized against inventory that do not have any applicability codes
    * </pre>
    */
   @Test
   public void itInitializesTasksAgainstInventoryWithoutApplicabilityCodes() throws Exception {

      final PartNoKey lAircraftPart = Domain.createPart();

      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAircraftAssembly ) {
                  aAircraftAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aConfigurationSlot ) {
                              aConfigurationSlot
                                    .addPartGroup( new DomainConfiguration<PartGroup>() {

                                       @Override
                                       public void configure( PartGroup aPartGroup ) {
                                          aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                                          aPartGroup.addPart( lAircraftPart );
                                       }
                                    } );
                           }
                        } );
               }
            } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.setPart( lAircraftPart );
            aAircraft.allowSynchronization();
         }
      } );

      final ConfigSlotKey lAircraftRootConfigSlot = new ConfigSlotKey( lAircraftAssembly, 0 );

      // Given an activated one-time requirement definition.
      final TaskTaskKey lReqDefnKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setStatus( REVISION );
                  aReqDefn.setApplicabilityRange( TASK_DEFN_APPLICABILITY_RANGE );
               }
            } );

      // When the requirement is activated.
      new TaskDefnService().activate( lReqDefnKey, new TaskDefnRevTO(), HR, true );

      // Emulate baseline sync processing of the inv_sync_queue rows
      // ( added by TaskDefnService.activate() ).
      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      List<TaskKey> lTasks =
            TaskDefnUtils.getActualTasks( lReqDefnKey, lAircraft, RefEventStatusKey.ACTV );

      Assert.assertEquals(
            "Requirement was not initialized for inventory even though it did not have any applicability codes",
            1, lTasks.size(), 0 );
   }


   /**
    * <pre>
    *    Given that I am a System Engineer and I have created a new requirement with an Applicability Range
    *        and the requirement is not on-condition
    *        and the requirement is not part of a maintenance program
    *        when I activate the requirement
    *        then the requirement should not be initialized against non-applicable inventory
    * </pre>
    */
   @Test
   public void requirementWithApplicabilityRangeIsNotInitializedAgainstNonApplicableInventory()
         throws Exception {

      final PartNoKey lAircraftPart = Domain.createPart();

      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAircraftAssembly ) {
                  aAircraftAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aConfigurationSlot ) {
                              aConfigurationSlot
                                    .addPartGroup( new DomainConfiguration<PartGroup>() {

                                       @Override
                                       public void configure( PartGroup aPartGroup ) {
                                          aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                                          aPartGroup.addPart( lAircraftPart );
                                       }
                                    } );
                           }
                        } );
               }
            } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.allowSynchronization();
            aAircraft.setPart( lAircraftPart );
            aAircraft.setApplicabilityCode( ACFT_APPLICABILITY_CODE_OUTSIDE_RANGE );
         }
      } );

      final ConfigSlotKey lAircraftRootConfigSlot = new ConfigSlotKey( lAircraftAssembly, 0 );

      // Given an activated one-time requirement definition.
      final TaskTaskKey lReqDefnKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setStatus( REVISION );
                  aReqDefn.setApplicabilityRange( TASK_DEFN_APPLICABILITY_RANGE );
               }
            } );

      // When the requirement is activated.
      new TaskDefnService().activate( lReqDefnKey, new TaskDefnRevTO(), HR, true );

      // Emulate baseline sync processing of the inv_sync_queue rows
      // ( added by TaskDefnBean.activate() ).
      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      List<TaskKey> lTasks =
            TaskDefnUtils.getActualTasks( lReqDefnKey, lAircraft, RefEventStatusKey.ACTV );

      Assert.assertTrue(
            "Requirement was initialized on the inventory although the applicability code did not match",
            lTasks.isEmpty() );
   }


   /**
    * <pre>
    *    Given that I am a System Engineer and I have created a new requirement with an Applicability Rule
    *        and the requirement is not on-condition
    *        and the requirement is not part of a maintenance program
    *        when I activate the requirement
    *        then the requirement should be initialized against applicable inventory
    * </pre>
    */
   @Test
   public void itInitializesTasksOnInventoryMatchingApplicabilityRule() throws Exception {

      final PartNoKey lAircraftPart = Domain.createPart();

      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAircraftAssembly ) {
                  aAircraftAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aConfigurationSlot ) {
                              aConfigurationSlot
                                    .addPartGroup( new DomainConfiguration<PartGroup>() {

                                       @Override
                                       public void configure( PartGroup aPartGroup ) {
                                          aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                                          aPartGroup.addPart( lAircraftPart );
                                       }
                                    } );
                           }
                        } );
               }
            } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.setSerialNumber( AIRCRAFT_SERIAL_NUMBER_THAT_MATCHES_RULE );
            aAircraft.setPart( lAircraftPart );
            aAircraft.allowSynchronization();

         }
      } );

      final ConfigSlotKey lAircraftRootConfigSlot = new ConfigSlotKey( lAircraftAssembly, 0 );

      // Given an activated one-time requirement definition.
      final TaskTaskKey lReqDefnKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setStatus( REVISION );
                  aReqDefn.setApplicabilityRule( TASK_DEFN_APPLICABILITY_RULE );
               }
            } );

      // When the requirement is activated.
      new TaskDefnService().activate( lReqDefnKey, new TaskDefnRevTO(), HR, true );

      // Emulate baseline sync processing of the inv_sync_queue rows
      // ( added by TaskDefnService.activate() ).
      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      List<TaskKey> lTasks =
            TaskDefnUtils.getActualTasks( lReqDefnKey, lAircraft, RefEventStatusKey.ACTV );

      Assert.assertEquals(
            "Requirement with applicability rule was not initialized on inventory, even though it matched the rule",
            1, lTasks.size(), 0 );
   }


   /**
    * <pre>
    *    Given that I am a System Engineer and I have created a new requirement with an Applicability Rule
    *        and the requirement is not on-condition
    *        and the requirement is not part of a maintenance program
    *        when I activate the requirement
    *        then the requirement should not be initialized against non-applicable inventory
    * </pre>
    */
   @Test
   public void tasksAreNotInitializedOnInventoryNonApplicableToTheRule() throws Exception {

      final PartNoKey lAircraftPart = Domain.createPart();

      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAircraftAssembly ) {
                  aAircraftAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aConfigurationSlot ) {
                              aConfigurationSlot
                                    .addPartGroup( new DomainConfiguration<PartGroup>() {

                                       @Override
                                       public void configure( PartGroup aPartGroup ) {
                                          aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                                          aPartGroup.addPart( lAircraftPart );
                                       }
                                    } );
                           }
                        } );
               }
            } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.allowSynchronization();
            aAircraft.setPart( lAircraftPart );
            aAircraft.setSerialNumber( AIRCRAFT_SERIAL_NUMBER_THAT_DOES_NOT_MATCH_RULE );
         }
      } );

      final ConfigSlotKey lAircraftRootConfigSlot = new ConfigSlotKey( lAircraftAssembly, 0 );

      // Given an activated one-time requirement definition.
      final TaskTaskKey lReqDefnKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setStatus( REVISION );
                  aReqDefn.setApplicabilityRule( TASK_DEFN_APPLICABILITY_RULE );
               }
            } );

      // When the requirement is activated.
      new TaskDefnService().activate( lReqDefnKey, new TaskDefnRevTO(), HR, true );

      // Emulate baseline sync processing of the inv_sync_queue rows
      // ( added by TaskDefnBean.activate() ).
      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      List<TaskKey> lTasks =
            TaskDefnUtils.getActualTasks( lReqDefnKey, lAircraft, RefEventStatusKey.ACTV );

      Assert.assertTrue(
            "Requirement was initialized on the inventory although the applicability code did not match",
            lTasks.isEmpty() );
   }


   /**
    * <pre>
    *    Given that I am a System Engineer
    *        and there exists a requirement with an applicability range that matches some aircraft
    *        and the requirement is not on-condition
    *        and the requirement is not part of a maintenance program
    *        when I edit the applicability range of the requirement by reducing the number of aircraft that are applicable
    *        and I activate the revised requirement
    *        then all previously initialized tasks that are no longer applicable to their inventory should be cancelled
    * </pre>
    */
   @Test
   public void
         itCancelsPreviouslyInitializedTasksWhenTheRevisedRangeMakesTheInventoryNonApplicable()
               throws Exception {

      // test data
      final String TASK_DEFN_INITIAL_APPL_RANGE = "300-500";
      final String TASK_DEFN_REVISED_APPL_RANGE = "300-400";
      final String ACFT_APPLICABILITY_CODE = "405";

      // Baseline Sync needs to have this permission for being able
      // to cancel tasks that are no longer applicable
      int lUserId = OrgHr.findByPrimaryKey( HumanResourceKey.ADMIN ).getUserId();
      UserParametersStub lUserParametersStub = new UserParametersStub( lUserId, "LOGIC" );
      lUserParametersStub.setBoolean( "ALLOW_ASSIGN_LRP_ITEM_FOR_INWORK_WP", false );
      UserParameters.setInstance( lUserId, "LOGIC", lUserParametersStub );

      final PartNoKey lAircraftPart = Domain.createPart();

      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAircraftAssembly ) {
                  aAircraftAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aConfigurationSlot ) {
                              aConfigurationSlot
                                    .addPartGroup( new DomainConfiguration<PartGroup>() {

                                       @Override
                                       public void configure( PartGroup aPartGroup ) {
                                          aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                                          aPartGroup.addPart( lAircraftPart );
                                       }
                                    } );
                           }
                        } );
               }
            } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.allowSynchronization();
            aAircraft.setPart( lAircraftPart );
            aAircraft.setApplicabilityCode( ACFT_APPLICABILITY_CODE );
         }
      } );

      final ConfigSlotKey lAircraftRootConfigSlot = new ConfigSlotKey( lAircraftAssembly, 0 );

      // Given an activated one-time requirement definition.
      final TaskTaskKey lReqDefnKeyWithInitialApplRange =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setRevisionNumber( REVISION_1 );
                  aReqDefn.setStatus( REVISION );
                  aReqDefn.setApplicabilityRange( TASK_DEFN_INITIAL_APPL_RANGE );
               }
            } );

      // When the revised recurring requirement is activated.
      new TaskDefnService().activate( lReqDefnKeyWithInitialApplRange, new TaskDefnRevTO(), HR,
            true );

      // Given a task exists that is based on that requirement.
      new CreationService().createTaskFromDefinition( lAircraft, lReqDefnKeyWithInitialApplRange,
            null, HR );

      final TaskTaskKey lReqDefnKeyWithRevisedApplRange =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setPreviousRevision( lReqDefnKeyWithInitialApplRange );
                  aReqDefn.setRevisionNumber( REVISION_2 );
                  aReqDefn.setStatus( REVISION );
                  aReqDefn.setApplicabilityRange( TASK_DEFN_REVISED_APPL_RANGE );
               }
            } );

      // When the revised recurring requirement is activated.
      new TaskDefnService().activate( lReqDefnKeyWithRevisedApplRange, new TaskDefnRevTO(), HR,
            true );

      // Emulate baseline sync processing of the inv_sync_queue rows
      // ( added by TaskDefnService.activate() ).
      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      List<TaskKey> lTaskList = TaskDefnUtils.getActualTasks( lReqDefnKeyWithRevisedApplRange,
            lAircraft, RefEventStatusKey.ACTV );

      Assert.assertEquals(
            "Requirement was not cancelled even though the task definition is no longer applicable to the inventory",
            0, lTaskList.size(), 0 );
   }


   /**
    * <pre>
    *    Given that I am a System Engineer
    *        and there exists a requirement with an applicability range that matches some aircraft
    *        and the requirement is not on-condition
    *        and the requirement is not part of a maintenance program
    *        when I edit the applicability range of the requirement by increasing the range of aircraft that are applicable
    *        and I activate the revised requirement
    *        then all previously non-applicable inventory that are now applicable must get tasks initialized on them
    * </pre>
    */
   @Test
   public void itInitializesTasksMatchingTheReviseApplicabilityRange() throws Exception {

      // test data
      final String TASK_DEFN_INITIAL_APPL_RANGE = "300-500";
      final String TASK_DEFN_REVISED_APPL_RANGE = "300-600";
      final String ACFT_APPLICABILITY_CODE = "550";

      final PartNoKey lAircraftPart = Domain.createPart();

      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAircraftAssembly ) {
                  aAircraftAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aConfigurationSlot ) {
                              aConfigurationSlot
                                    .addPartGroup( new DomainConfiguration<PartGroup>() {

                                       @Override
                                       public void configure( PartGroup aPartGroup ) {
                                          aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                                          aPartGroup.addPart( lAircraftPart );
                                       }
                                    } );
                           }
                        } );
               }
            } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.allowSynchronization();
            aAircraft.setPart( lAircraftPart );
            aAircraft.setApplicabilityCode( ACFT_APPLICABILITY_CODE );
         }
      } );

      final ConfigSlotKey lAircraftRootConfigSlot = new ConfigSlotKey( lAircraftAssembly, 0 );

      // Given an activated one-time requirement definition.
      final TaskTaskKey lReqDefnKeyWithInitialApplRange =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setRevisionNumber( REVISION_1 );
                  aReqDefn.setStatus( REVISION );
                  aReqDefn.setApplicabilityRange( TASK_DEFN_INITIAL_APPL_RANGE );
               }
            } );

      // When the revised recurring requirement is activated.
      new TaskDefnService().activate( lReqDefnKeyWithInitialApplRange, new TaskDefnRevTO(), HR,
            true );

      // Emulate baseline sync processing of the inv_sync_queue rows
      // ( added by TaskDefnService.activate() ).
      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      List<TaskKey> lTaskList = TaskDefnUtils.getActualTasks( lReqDefnKeyWithInitialApplRange,
            lAircraft, RefEventStatusKey.ACTV );

      Assert.assertEquals(
            "Task is initialized even though task definition is not applicable to inventory", 0,
            lTaskList.size(), 0 );

      final TaskTaskKey lReqDefnKeyWithRevisedApplRange =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setPreviousRevision( lReqDefnKeyWithInitialApplRange );
                  aReqDefn.setRevisionNumber( REVISION_2 );
                  aReqDefn.setStatus( REVISION );
                  aReqDefn.setApplicabilityRange( TASK_DEFN_REVISED_APPL_RANGE );
               }
            } );

      // When the revised recurring requirement is activated.
      new TaskDefnService().activate( lReqDefnKeyWithRevisedApplRange, new TaskDefnRevTO(), HR,
            true );

      // Emulate BSYNC
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      lTaskList = TaskDefnUtils.getActualTasks( lReqDefnKeyWithRevisedApplRange, lAircraft,
            RefEventStatusKey.ACTV );

      Assert.assertEquals(
            "Task is not initialized on previously non-applicable inventory, even though it matches the revised applicability range",
            1, lTaskList.size(), 0 );
   }


   /**
    * <pre>
    *    Given that I am a System Engineer
    *        and there exists a requirement with an applicability rule that matches some aircraft
    *        and the requirement is not on-condition
    *        and the requirement is not part of a maintenance program
    *        when I edit the applicability rule of the requirement so that it matches fewer aircraft
    *        and I activate the revised requirement
    *        then all previously initialized tasks that belong to inventory which is no longer applicable should be cancelled
    * </pre>
    */
   @Test
   public void itCancelsPreviouslyInitializedTasksThatDoNotApplyToTheRevisedRule()
         throws Exception {

      // test data
      final String TASK_DEFN_INITIAL_APPL_RULE = "ac_inv.serial_no_oem Like 'QWERTY%'";
      final String TASK_DEFN_REVISED_APPL_RULE = "ac_inv.serial_no_oem Like 'QWERTY-ZXCV%'";
      final String ACFT_SERIAL_NUMBER = "QWERTY-ASDF-001";

      // Baseline Sync needs to have this permission for being able
      // to cancel tasks that are no longer applicable
      int lUserId = OrgHr.findByPrimaryKey( HumanResourceKey.ADMIN ).getUserId();
      UserParametersStub lUserParametersStub = new UserParametersStub( lUserId, "LOGIC" );
      lUserParametersStub.setBoolean( "ALLOW_ASSIGN_LRP_ITEM_FOR_INWORK_WP", false );
      UserParameters.setInstance( lUserId, "LOGIC", lUserParametersStub );

      final PartNoKey lAircraftPart = Domain.createPart();

      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAircraftAssembly ) {
                  aAircraftAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aConfigurationSlot ) {
                              aConfigurationSlot
                                    .addPartGroup( new DomainConfiguration<PartGroup>() {

                                       @Override
                                       public void configure( PartGroup aPartGroup ) {
                                          aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                                          aPartGroup.addPart( lAircraftPart );
                                       }
                                    } );
                           }
                        } );
               }
            } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.allowSynchronization();
            aAircraft.setPart( lAircraftPart );
            aAircraft.setSerialNumber( ACFT_SERIAL_NUMBER );
         }
      } );

      final ConfigSlotKey lAircraftRootConfigSlot = new ConfigSlotKey( lAircraftAssembly, 0 );

      // Given an activated one-time requirement definition.
      final TaskTaskKey lReqDefnKeyWithInitialApplRule =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setRevisionNumber( REVISION_1 );
                  aReqDefn.setStatus( REVISION );
                  aReqDefn.setApplicabilityRule( TASK_DEFN_INITIAL_APPL_RULE );
               }
            } );

      // When the revised recurring requirement is activated.
      new TaskDefnService().activate( lReqDefnKeyWithInitialApplRule, new TaskDefnRevTO(), HR,
            true );

      // Given a task exists that is based on that requirement.
      new CreationService().createTaskFromDefinition( lAircraft, lReqDefnKeyWithInitialApplRule,
            null, HR );

      final TaskTaskKey lReqDefnKeyWithRevisedApplRule =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setPreviousRevision( lReqDefnKeyWithInitialApplRule );
                  aReqDefn.setRevisionNumber( REVISION_2 );
                  aReqDefn.setStatus( REVISION );
                  aReqDefn.setApplicabilityRule( TASK_DEFN_REVISED_APPL_RULE );
               }
            } );

      // When the revised recurring requirement is activated.
      new TaskDefnService().activate( lReqDefnKeyWithRevisedApplRule, new TaskDefnRevTO(), HR,
            true );

      // Emulate baseline sync processing of the inv_sync_queue rows
      // ( added by TaskDefnService.activate() ).
      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      List<TaskKey> lTaskList = TaskDefnUtils.getActualTasks( lReqDefnKeyWithRevisedApplRule,
            lAircraft, RefEventStatusKey.ACTV );

      Assert.assertEquals(
            "Requirement was not cancelled even though the task definition is no longer applicable to the inventory",
            0, lTaskList.size(), 0 );
   }


   /**
    * <pre>
    *    Given that I am a System Engineer
    *        and there exists a requirement with an applicability rule that does not match a given aircraft
    *        and the requirement is not on-condition
    *        and the requirement is not part of a maintenance program
    *        when I edit the applicability rule of the requirement so that it matches the aircraft that it previously wasn't matching
    *        and I activate the revised requirement
    *        then a task is initialized on the newly applicable inventory
    * </pre>
    */
   @Test
   public void itInitializesTasksMatchingTheRevisedApplicabilityRule() throws Exception {

      // test data
      final String TASK_DEFN_INITIAL_APPL_RULE = "ac_inv.serial_no_oem Like 'Query%'";
      final String TASK_DEFN_REVISED_APPL_RULE = "ac_inv.serial_no_oem Like 'Q%'";
      final String ACFT_SERIAL_NUMBER = "QWERTY001";

      final PartNoKey lAircraftPart = Domain.createPart();

      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAircraftAssembly ) {
                  aAircraftAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aConfigurationSlot ) {
                              aConfigurationSlot
                                    .addPartGroup( new DomainConfiguration<PartGroup>() {

                                       @Override
                                       public void configure( PartGroup aPartGroup ) {
                                          aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                                          aPartGroup.addPart( lAircraftPart );
                                       }
                                    } );
                           }
                        } );
               }
            } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.allowSynchronization();
            aAircraft.setPart( lAircraftPart );
            aAircraft.setSerialNumber( ACFT_SERIAL_NUMBER );
         }
      } );

      final ConfigSlotKey lAircraftRootConfigSlot = new ConfigSlotKey( lAircraftAssembly, 0 );

      // Given an activated one-time requirement definition.
      final TaskTaskKey lReqDefnKeyWithInitialApplRule =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setRevisionNumber( REVISION_1 );
                  aReqDefn.setStatus( REVISION );
                  aReqDefn.setApplicabilityRule( TASK_DEFN_INITIAL_APPL_RULE );
               }
            } );

      // When the revised recurring requirement is activated.
      new TaskDefnService().activate( lReqDefnKeyWithInitialApplRule, new TaskDefnRevTO(), HR,
            true );

      // Emulate baseline sync processing of the inv_sync_queue rows
      // ( added by TaskDefnService.activate() ).
      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      final TaskTaskKey lReqDefnKeyWithRevisedApplRule =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setPreviousRevision( lReqDefnKeyWithInitialApplRule );
                  aReqDefn.setRevisionNumber( REVISION_2 );
                  aReqDefn.setStatus( REVISION );
                  aReqDefn.setApplicabilityRange( TASK_DEFN_REVISED_APPL_RULE );
               }
            } );

      // When the revised recurring requirement is activated.
      new TaskDefnService().activate( lReqDefnKeyWithRevisedApplRule, new TaskDefnRevTO(), HR,
            true );

      // Emulate baseline sync processing of the inv_sync_queue rows
      // ( added by TaskDefnService.activate() ).
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      List<TaskKey> lTaskList = TaskDefnUtils.getActualTasks( lReqDefnKeyWithRevisedApplRule,
            lAircraft, RefEventStatusKey.ACTV );

      Assert.assertEquals(
            "Requirement was not initialized on the inventory even though the revised applicability rule is now matching",
            1, lTaskList.size(), 0 );
   }


   /**
    * <pre>
    *   Given that I am a System Engineer and I have created a new recurring requirement definition
    *     And the requirement definition is scheduled from an historic effective date
    *     And the requirement definition has a usage based scheduling rule with an interval
    *     And there is an aircraft with the same usage parameter defined against it
    *     And the aircraft has accrued usage before the chosen effective date
    *   When I activate the requirement definition
    *     And a requirement is initialized against the aircraft
    *   Then the usage deadline of the requirement should be the usage on the given effective date + the interval
    * </pre>
    */
   @Test
   public void
         theUsageDeadlineIsCorrectWhenReqDefnWithHistoricEffectiveDateAndUsageIntervalIsInitialized()
               throws Exception {

      /*
       * Create Aircraft. We need to provide the part and the part group information in order for
       * the applicability logic to work properly. Without the part and part group , the
       * applicability logic throws errors when trying to initialize the requirement onto the
       * aircraft
       */

      final PartNoKey lAircraftPart = Domain.createPart();
      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAircraftAssembly ) {
                  aAircraftAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aConfigurationSlot ) {
                              aConfigurationSlot
                                    .addPartGroup( new DomainConfiguration<PartGroup>() {

                                       @Override
                                       public void configure( PartGroup aPartGroup ) {
                                          aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                                          aPartGroup.addPart( lAircraftPart );
                                       }
                                    } );
                           }
                        } );
               }
            } );

      final FcModelKey lForecastModel = Domain.createForecastModel();
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.setPart( lAircraftPart );
            aAircraft.allowSynchronization();
            aAircraft.setForecastModel( lForecastModel );
            aAircraft.addUsage( HOURS, BigDecimal.ZERO );
         }
      } );

      final ConfigSlotKey lAircraftRootConfigSlot = new ConfigSlotKey( lAircraftAssembly, 0 );

      // Recurring Requirement Definition
      final BigDecimal lRepeatInterval = new BigDecimal( 50 );
      final Date lHistoricEffectiveDate = DateUtils.addDays( new Date(), -30 );

      final RecurringSchedulingRule lNewRecurringSchedulingRule =
            new RecurringSchedulingRule( HOURS, lRepeatInterval );
      final TaskTaskKey lRecurringReqDefnWithHistoricEffectiveDateKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setRecurring( true );
                  aReqDefn.setStatus( REVISION );
                  aReqDefn.setScheduledFromEffectiveDate( lHistoricEffectiveDate );
                  aReqDefn.addRecurringSchedulingRule( lNewRecurringSchedulingRule );
               }
            } );

      /*
       * Accrue usage for the aircraft BEFORE the effective date. Setting up two flights with 10 and
       * 5 flight hours respectively, both taken before the historic effective date. This usage
       * should be included in calculation of the next usage deadline quantity
       */
      final Date lHistoricFlightDate1 = DateUtils.addDays( lHistoricEffectiveDate, -10 );
      final BigDecimal lFlightHours1 = new BigDecimal( 10 );
      accrueUsage( lAircraft, lHistoricFlightDate1, lFlightHours1, lFlightHours1 );

      final Date lHistoricFlightDate2 = DateUtils.addDays( lHistoricEffectiveDate, -5 );
      final BigDecimal lFlightHours2 = new BigDecimal( 5 );
      accrueUsage( lAircraft, lHistoricFlightDate2, lFlightHours1.add( lFlightHours2 ),
            lFlightHours2 );

      /*
       * Accrue usage for the aircraft ON the effective date. This usage should be included in
       * calculation of the next usage deadline quantity
       */
      final BigDecimal lFlightHours3 = new BigDecimal( 5 );
      accrueUsage( lAircraft, lHistoricEffectiveDate,
            lFlightHours1.add( lFlightHours2 ).add( lFlightHours3 ), lFlightHours3 );

      /*
       * Accrue usage for the aircraft AFTER the effective date. The usage from this flight should
       * not be included in calculation of the deadline quantity because this usage was accrued
       * AFTER the effective date
       */
      final Date lFlightDate4 = DateUtils.addDays( lHistoricEffectiveDate, 5 );
      final BigDecimal lFlightHours4 = new BigDecimal( 15 );
      accrueUsage( lAircraft, lFlightDate4,
            lFlightHours1.add( lFlightHours2 ).add( lFlightHours3 ).add( lFlightHours4 ),
            lFlightHours4 );

      // When the requirement is activated.
      new TaskDefnService().activate( lRecurringReqDefnWithHistoricEffectiveDateKey,
            new TaskDefnRevTO(), HR, true );

      // Emulate baseline sync processing of the inv_sync_queue rows
      // ( added by TaskDefnService.activate() ).
      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      List<TaskKey> lTasks = TaskDefnUtils.getActualTasks(
            lRecurringReqDefnWithHistoricEffectiveDateKey, lAircraft, RefEventStatusKey.ACTV );

      Assert.assertEquals( "Requirement was not initialized on the inventory", 1, lTasks.size(),
            0 );

      Double lActualDeadlineHours =
            EvtSchedDeadTable.findByPrimaryKey( lTasks.get( 0 ), HOURS ).getDeadlineQt();

      /*
       * Expected Deadline would be the usage accrued by the aircraft as of the historic effective
       * date plus the scheduling interval. Usage accrued after the historic effective date would
       * not be included in the deadline calculation
       */
      Double lExpectedDeadlineHours = lFlightHours1.add( lFlightHours2 ).add( lFlightHours3 )
            .add( lRepeatInterval ).doubleValue();

      Assert.assertEquals( "Usage based deadline incorrect", lExpectedDeadlineHours,
            lActualDeadlineHours, 0 );

   }


   /**
    * <pre>
    *    Given that I am a System Engineer
    *        and there exists a requirement definition without any job cards
    *        and the requirement has been initialized on an inventory
    *        and the requirement is not part of a maintenance program
    *        and the requirement is not in a committed, in progress or completed work package
    *        and the requirement definition is revised
    *        and a new job card is added to the requirement definition
    *    When I activate the revised requirement definition
    *    Then the job card is added to the previously initialized requirement on the inventory
    * </pre>
    */
   @Test
   public void itUpdatesTheJobCardOnTheRequirement() throws Exception {

      // Baseline Sync needs to have this permission for this test
      HumanResourceKey lHr = new HumanResourceKey( 0, 3 );
      int lUserId = OrgHr.findByPrimaryKey( lHr ).getUserId();
      UserParametersStub lUserParametersStub = new UserParametersStub( lUserId, "LOGIC" );
      lUserParametersStub.setBoolean( "ALLOW_ASSIGN_LRP_ITEM_FOR_INWORK_WP", false );
      UserParameters.setInstance( lUserId, "LOGIC", lUserParametersStub );

      /*
       * Create Aircraft. We need to provide the part and the part group information in order for
       * the applicability logic to work properly. Without the part and part group , the
       * applicability logic throws errors when trying to initialize the requirement onto the
       * aircraft
       */
      final Date lManufacturedDate = addDays( new Date(), -365 );
      final PartNoKey lAircraftPart = Domain.createPart();
      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAircraftAssembly ) {
                  aAircraftAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aConfigurationSlot ) {
                              aConfigurationSlot
                                    .addPartGroup( new DomainConfiguration<PartGroup>() {

                                       @Override
                                       public void configure( PartGroup aPartGroup ) {
                                          aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                                          aPartGroup.addPart( lAircraftPart );
                                       }
                                    } );
                           }
                        } );
               }
            } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.setManufacturedDate( lManufacturedDate );
            aAircraft.allowSynchronization();
            aAircraft.setPart( lAircraftPart );
         }
      } );

      final ConfigSlotKey lAcftRootConfigSlot = new ConfigSlotKey( lAircraftAssembly, 0 );

      // Requirement Definition without any job card
      final TaskTaskKey lReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAcftRootConfigSlot );
                  aReqDefn.setStatus( REVISION );
               }
            } );

      // Create Requirement
      TaskKey lReqActual = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setDefinition( lReqDefn );
            aReq.setInventory( lAircraft );
         }
      } );

      // Check if any JICs seen on the requirement
      List<TaskKey> lJicsForRev = getJicsAssignedToTask( lReqActual );

      Assert.assertEquals(
            "Jic seen for requirement when no JIC was defined for the requirement definition", 0,
            lJicsForRev.size(), 0 );

      // create JIC definition
      final TaskTaskKey lJicDefn =
            Domain.createJobCardDefinition( new DomainConfiguration<JobCardDefinition>() {

               @Override
               public void configure( JobCardDefinition aJicDefn ) {
                  aJicDefn.setOnCondition( false );
                  aJicDefn.setRevisionNumber( REVISION_1 );
                  aJicDefn.setStatus( REVISION );
                  aJicDefn.setConfigurationSlot( lAcftRootConfigSlot );
               }
            } );

      // Revise Req Definition and add JIC to it
      final TaskTaskKey lRevisedReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAcftRootConfigSlot );
                  aReqDefn.setPreviousRevision( lReqDefn );
                  aReqDefn.addJobCardDefinition( lJicDefn );
                  aReqDefn.setRevisionNumber( REVISION_2 );
                  aReqDefn.setStatus( REVISION );
               }
            } );

      // activate the JIC
      new TaskDefnService().activate( lJicDefn, new TaskDefnRevTO(), lHr, true );

      // activate the requirement and emulate baseline sync
      new TaskDefnService().activate( lRevisedReqDefn, new TaskDefnRevTO(), HR, true );
      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      // Check that JIC is added to the previously initialized requirement
      TaskKey lRevReqActual = TaskDefnUtils
            .getActualTasks( lRevisedReqDefn, lAircraft, RefEventStatusKey.ACTV ).get( 0 );
      TaskKey lJicActual =
            TaskDefnUtils.getActualTasks( lJicDefn, lAircraft, RefEventStatusKey.ACTV ).get( 0 );

      Assert.assertTrue( "JIC is not assigned to REQ",
            isTaskAssignedToTask( lJicActual, lRevReqActual ) );

      List<TaskKey> lJicsForRevisedReq = getJicsAssignedToTask( lRevReqActual );

      Assert.assertEquals(
            "Jic not seen for requirement even when JIC was added to the revised requirement definition",
            1, lJicsForRevisedReq.size(), 0 );

   }


   /**
    * <pre>
    *    Given that I am a System Engineer
    *        and there exists a requirement definition with a job card
    *        and the requirement has been initialized on an inventory
    *        and the requirement is not part of a maintenance program
    *        and the requirement is not in a committed, in progress or completed work package
    *        and the requirement definition is revised
    *        and the job card is removed from the requirement definition
    *    When I activate the revised requirement definition
    *    Then the job card is removed from the previously initialized requirement
    * </pre>
    */
   @Test
   public void itCancelsTheJobCardOnTheRequirement() throws Exception {

      // Baseline Sync needs to have this permission for this test
      HumanResourceKey lHr = new HumanResourceKey( 0, 3 );
      int lUserId = OrgHr.findByPrimaryKey( lHr ).getUserId();
      UserParametersStub lUserParametersStub = new UserParametersStub( lUserId, "LOGIC" );
      lUserParametersStub.setBoolean( "ALLOW_ASSIGN_LRP_ITEM_FOR_INWORK_WP", false );
      UserParameters.setInstance( lUserId, "LOGIC", lUserParametersStub );

      /*
       * Create Aircraft. We need to provide the part and the part group information in order for
       * the applicability logic to work properly. Without the part and part group , the
       * applicability logic throws errors when trying to initialize the requirement onto the
       * aircraft
       */
      final Date lManufacturedDate = addDays( new Date(), -365 );
      final PartNoKey lAircraftPart = Domain.createPart();
      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAircraftAssembly ) {
                  aAircraftAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aConfigurationSlot ) {
                              aConfigurationSlot
                                    .addPartGroup( new DomainConfiguration<PartGroup>() {

                                       @Override
                                       public void configure( PartGroup aPartGroup ) {
                                          aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                                          aPartGroup.addPart( lAircraftPart );
                                       }
                                    } );
                           }
                        } );
               }
            } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.setManufacturedDate( lManufacturedDate );
            aAircraft.setPart( lAircraftPart );
            aAircraft.allowSynchronization();
         }
      } );

      final ConfigSlotKey lAircraftRootConfigSlot = new ConfigSlotKey( lAircraftAssembly, 0 );

      /*
       * Data setup for this test is somewhat complicated. The state we need to replicate is
       * outlined in the following steps that have to be taken in the front-end:
       */
      // 1. Create a Requirement Definition (ACTV) and a JIC definition (BUILD)
      // 2. Revise the requirement definition and add the JIC to it
      // 3. Activate both the JIC and the REQ
      // 4. Revise the requirement definition
      // 5. Revise the JIC [It is necessary for the JIC to be in revision, if you want to remove it
      // from the REQ DEFN]
      // 6. Remove the JIC
      // 7. Activate the JIC [It is necessary for the JIC to be activated, if you want to see the
      // JIC removed from REQ DEFN]
      // 8. Activate the Requirement Definition

      // create JIC definition [Rev 1 - Superseded]
      final TaskTaskKey lJicDefnRev1 =
            Domain.createJobCardDefinition( new DomainConfiguration<JobCardDefinition>() {

               @Override
               public void configure( JobCardDefinition aJicDefn ) {
                  aJicDefn.setOnCondition( false );
                  aJicDefn.setRevisionNumber( REVISION_1 );
                  aJicDefn.setStatus( RefTaskDefinitionStatusKey.SUPRSEDE );
                  aJicDefn.setConfigurationSlot( lAircraftRootConfigSlot );
               }
            } );

      // Create Requirement Definition [Rev 1 - Active] and add the JIC to it
      final TaskTaskKey lReqDefnRev1 =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.addJobCardDefinition( lJicDefnRev1 );
                  aReqDefn.setRevisionNumber( REVISION_1 );
                  aReqDefn.setStatus( ACTV );
               }
            } );

      // create revised JIC definition [Rev 2 - Active]
      final TaskTaskKey lJicDefnRev2 =
            Domain.createJobCardDefinition( new DomainConfiguration<JobCardDefinition>() {

               @Override
               public void configure( JobCardDefinition aJicDefn ) {
                  aJicDefn.setOnCondition( false );
                  aJicDefn.setPreviousRevision( lJicDefnRev1 );
                  aJicDefn.setRevisionNumber( REVISION_2 );
                  aJicDefn.setStatus( ACTV );
                  aJicDefn.setConfigurationSlot( lAircraftRootConfigSlot );
               }
            } );

      // Revise requirement with no job card definition associated with it [Rev 2 - Revision]
      final TaskTaskKey lReqDefnRev2 =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setPreviousRevision( lReqDefnRev1 );
                  aReqDefn.setRevisionNumber( REVISION_2 );
                  aReqDefn.setStatus( REVISION );
               }
            } );

      // Create JIC [Rev 2 - Active]
      final TaskKey lJicActualRev2 = Domain.createJobCard( new DomainConfiguration<JobCard>() {

         @Override
         public void configure( JobCard aJic ) {
            aJic.setDefinition( lJicDefnRev2 );
            aJic.setStatus( RefEventStatusKey.ACTV );
         }
      } );

      // Create Requirement with JIC [Actual Requirement with Job Card Rev 2]
      TaskKey lReqActual = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setDefinition( lReqDefnRev1 );
            aReq.setStatus( RefEventStatusKey.ACTV );
            aReq.addJobCard( lJicActualRev2 );
         }
      } );

      /*
       * Check that JIC is added to the requirement - next two Asserts checking the state BEFORE the
       * Act of Activating the Revised ReqDefn
       */
      Assert.assertTrue( "JIC is not assigned to REQ",
            isTaskAssignedToTask( lJicActualRev2, lReqActual ) );

      List<TaskKey> lJicsForReq = getJicsAssignedToTask( lReqActual );

      Assert.assertEquals(
            "Jic not seen for requirement even when JIC was added to the requirement definition", 1,
            lJicsForReq.size(), 0 );

      // activate the revised requirement and emulate baseline sync
      new TaskDefnService().activate( lReqDefnRev2, new TaskDefnRevTO(), HR, true );
      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      // Check that the JIC is removed from the revised requirement
      TaskKey lRevisedReqActual = TaskDefnUtils
            .getActualTasks( lReqDefnRev2, lAircraft, RefEventStatusKey.ACTV ).get( 0 );

      Assert.assertFalse(
            "JIC is still assigned to REQ even when it was removed from the revised requirement definition",
            isTaskAssignedToTask( lJicActualRev2, lRevisedReqActual ) );

      List<TaskKey> lJicsForRevisedReq = getJicsAssignedToTask( lRevisedReqActual );

      Assert.assertEquals(
            "Jic seen for requirement even when JIC was removed from the revised requirement definition",
            0, lJicsForRevisedReq.size(), 0 );

   }


   /**
    * <pre>
    *   Given that I am a System Engineer
    *     And there exists a one-time requirement definition
    *     And it has been initialized on an aircraft
    *     And the requirement is not part of a maintenance program
    *     And the requirement is not in a committed, in progress or completed work package
    *     And the requirement definition is revised to make it recurring
    *   When I activate the revised requirement definition
    *   Then the previously initialized requirement should continue to remain ACTV
    * </pre>
    */
   @Test
   public void activeInstancesOfReqRemainActvWhenReqDefnChangedToRecurring() throws Exception {

      /*
       * Create Aircraft. We need to provide the part and the part group information in order for
       * the applicability logic to work properly. Without the part and part group , the
       * applicability logic throws errors when trying to initialize the requirement onto the
       * aircraft
       */
      final Date lManufacturedDate = addDays( new Date(), -90 );
      final PartNoKey lAircraftPart = Domain.createPart();
      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAircraftAssembly ) {
                  aAircraftAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aConfigurationSlot ) {
                              aConfigurationSlot
                                    .addPartGroup( new DomainConfiguration<PartGroup>() {

                                       @Override
                                       public void configure( PartGroup aPartGroup ) {
                                          aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                                          aPartGroup.addPart( lAircraftPart );
                                       }
                                    } );
                           }
                        } );
               }
            } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.setManufacturedDate( lManufacturedDate );
            aAircraft.setPart( lAircraftPart );
            aAircraft.allowSynchronization();
         }
      } );

      final ConfigSlotKey lAircraftRootConfigSlot = new ConfigSlotKey( lAircraftAssembly, 0 );

      /* Create Requirement Definition */
      final TaskTaskKey lReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setStatus( ACTV );
                  aReqDefn.setRecurring( false );
                  aReqDefn.setScheduledFromManufacturedDate();
                  aReqDefn.setRevisionNumber( REVISION_1 );
               }
            } );

      /* Create Requirement */
      TaskKey lReqActual = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setDefinition( lReqDefn );
            aReq.setStatus( RefEventStatusKey.ACTV );
         }
      } );

      /* Revise Requirement Definition */
      final TaskTaskKey lRevisedReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setPreviousRevision( lReqDefn );
                  aReqDefn.setStatus( REVISION );
                  aReqDefn.setRevisionNumber( REVISION_2 );
                  aReqDefn.setRecurring( true );
               }
            } );

      // activate the revised requirement and emulate baseline sync
      new TaskDefnService().activate( lRevisedReqDefn, new TaskDefnRevTO(), HR, true );
      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      // Check that the requirement still exists
      List<TaskKey> lRevisedActual =
            TaskDefnUtils.getActualTasks( lRevisedReqDefn, lAircraft, RefEventStatusKey.ACTV );
      Assert.assertEquals( "Previously initialized requirement continues to exist", 1,
            lRevisedActual.size(), 0 );

      // Check that the status of the requirement is un-changed
      EvtEventTable lReqEventTable = evtEventDao.findByPrimaryKey( lReqActual.getEventKey() );
      Assert.assertEquals(
            "Previously Initialized Requirement was no longer ACTV when requirement definition was revised from non-recurring to recurring",
            RefEventStatusKey.ACTV, lReqEventTable.getEventStatus() );
   }


   /**
    * <pre>
    *   Given that I am a System Engineer
    *     And there exists a one-time requirement definition
    *     And the requirement is not part of a maintenance program
    *     And the requirement is not in a committed, in progress or completed work package
    *     And it has been initialized and completed on an aircraft
    *     And the requirement definition is revised to make it recurring
    *     And the requirement is not on-condition
    *   When I activate the revised requirement definition
    *   Then the requirement previously completed on the aircraft should remain COMPLETE
    *     And a new instance of the requirement definition should be initialized with status ACTV on the same aircraft
    * </pre>
    */
   @Test
   public void newRequirementWithActvStatusInitializedOnAircraftWhenReqDefnChangedToRecurring()
         throws Exception {
      /*
       * Create Aircraft. We need to provide the part and the part group information in order for
       * the applicability logic to work properly. Without the part and part group , the
       * applicability logic throws errors when trying to initialize the requirement onto the
       * aircraft
       */
      final Date lManufacturedDate = addDays( new Date(), -90 );
      final PartNoKey lAircraftPart = Domain.createPart();
      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAircraftAssembly ) {
                  aAircraftAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aConfigurationSlot ) {
                              aConfigurationSlot
                                    .addPartGroup( new DomainConfiguration<PartGroup>() {

                                       @Override
                                       public void configure( PartGroup aPartGroup ) {
                                          aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                                          aPartGroup.addPart( lAircraftPart );
                                       }
                                    } );
                           }
                        } );
               }
            } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.setManufacturedDate( lManufacturedDate );
            aAircraft.setPart( lAircraftPart );
            aAircraft.allowSynchronization();
         }
      } );

      final ConfigSlotKey lAircraftRootConfigSlot = new ConfigSlotKey( lAircraftAssembly, 0 );

      /* Create Requirement Definition */
      final TaskTaskKey lReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setStatus( ACTV );
                  aReqDefn.setRecurring( false );
                  aReqDefn.setScheduledFromManufacturedDate();
                  aReqDefn.setRevisionNumber( REVISION_1 );
               }
            } );

      /* Create Requirement with status of COMPLETE */
      TaskKey lCompletedTask = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setDefinition( lReqDefn );
            aReq.setStatus( RefEventStatusKey.COMPLETE );
         }
      } );

      /* Revise Requirement Definition , Make it recurring */
      final TaskTaskKey lRevisedReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setPreviousRevision( lReqDefn );
                  aReqDefn.setStatus( REVISION );
                  aReqDefn.setRevisionNumber( REVISION_2 );
                  aReqDefn.setRecurring( true );
               }
            } );

      // activate the revised requirement and emulate baseline sync
      new TaskDefnService().activate( lRevisedReqDefn, new TaskDefnRevTO(), HR, true );
      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      List<TaskKey> lTasks =
            TaskDefnUtils.getActualTasks( lRevisedReqDefn, lAircraft, RefEventStatusKey.ACTV );

      Assert.assertEquals( "Newly Initialized Requirement not seen on the inventory", 1,
            lTasks.size(), 0 );

      Assert.assertEquals(
            "Completed requirement changed to ACTV when Requirement Definition was revised to recurring",
            RefEventStatusKey.COMPLETE,
            evtEventDao.findByPrimaryKey( lCompletedTask ).getEventStatus() );
   }


   /**
    * <pre>
    *        Given a requirement definition
    *        and the requirement definition is not on-condition
    *        and the requirement definition is not part of a maintenance program
    *        and the requirement definition has a step with description
    *        when the requirement is activated
    *        then the requirement initialized against the inventory doesn't have step description
    * </pre>
    */
   @Test
   public void itInitializesTasksAgainstInventoryWithoutStepDescription() throws Exception {

      final PartNoKey lAircraftPart = Domain.createPart();
      final AssemblyKey lAircraftAssembly =
            createAircraftAssembly( lAircraftPart, FIRST_AIRCRAFT_ASSEMBLY_WITH_PART );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.setPart( lAircraftPart );
            aAircraft.allowSynchronization();
         }
      } );

      // Given a one-time requirement definition.
      final TaskTaskKey lReqDefnKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot(
                        Domain.readRootConfigurationSlot( lAircraftAssembly ) );
                  aReqDefn.setStatus( BUILD );
                  aReqDefn.setExecutable( true );
                  aReqDefn.addStep( new DomainConfiguration<Step>() {

                     @Override
                     public void configure( Step aBuilder ) {
                        aBuilder.setDescription( STEP_DESCRIPTION );
                     }
                  } );
               }
            } );

      // When the requirement is activated.
      new TaskDefnService().activate( lReqDefnKey, new TaskDefnRevTO(), HR, true );

      // Emulate baseline sync processing of the inv_sync_queue rows
      // ( added by TaskDefnService.activate() ).
      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      List<TaskKey> lTasks =
            TaskDefnUtils.getActualTasks( lReqDefnKey, lAircraft, RefEventStatusKey.ACTV );

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lTasks.get( 0 ), "sched_db_id", "sched_id" );
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "sched_step", lArgs );

      if ( !lQs.next() ) {
         fail( String.format(
               "Expected a requirement step to be created for Task with TaskKey = %s",
               lTasks.get( 0 ).toString() ) );
      }
      Assert.assertNull( "Unexpected step description, it should be blank",
            lQs.getString( "step_ldesc" ) );
   }


   /**
    * <pre>
    *        Given a requirement definition
    *        and the requirement definition is not on-condition
    *        and the requirement definition is not part of a maintenance program
    *        and the requirement definition has a job card definition
    *        and the job card definition has a step with description
    *        when the requirement is activated
    *        then the job card initialized against the inventory doesn't have step description
    * </pre>
    */
   @Test
   public void itInitializesTaskWithJobCardAgainstInventoryWithoutStepDescription()
         throws Exception {

      // Baseline Sync needs to have this permission for this test
      HumanResourceKey lHr = new HumanResourceKey( 0, 3 );
      int lUserId = OrgHr.findByPrimaryKey( lHr ).getUserId();
      UserParametersStub lUserParametersStub = new UserParametersStub( lUserId, "LOGIC" );
      lUserParametersStub.setBoolean( "ALLOW_ASSIGN_LRP_ITEM_FOR_INWORK_WP", false );
      UserParameters.setInstance( lUserId, "LOGIC", lUserParametersStub );

      final PartNoKey lAircraftPart = Domain.createPart();

      final AssemblyKey lAircraftAssembly =
            createAircraftAssembly( lAircraftPart, FIRST_AIRCRAFT_ASSEMBLY_WITH_PART );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.setPart( lAircraftPart );
            aAircraft.allowSynchronization();
         }
      } );

      final TaskTaskKey lJicDefn =
            Domain.createJobCardDefinition( new DomainConfiguration<JobCardDefinition>() {

               @Override
               public void configure( JobCardDefinition aBuilder ) {
                  aBuilder.setConfigurationSlot(
                        Domain.readRootConfigurationSlot( lAircraftAssembly ) );
                  aBuilder.setStatus( BUILD );
                  aBuilder.addStep( new DomainConfiguration<Step>() {

                     @Override
                     public void configure( Step aBuilder ) {
                        aBuilder.setDescription( STEP_DESCRIPTION );
                     }
                  } );
               }
            } );

      // Given a one-time requirement definition.
      final TaskTaskKey lReqDefnKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot(
                        Domain.readRootConfigurationSlot( lAircraftAssembly ) );
                  aReqDefn.setStatus( BUILD );
                  aReqDefn.addJobCardDefinition( lJicDefn );
               }
            } );

      // When the Job card and requirement are activated.
      new TaskDefnService().activate( lJicDefn, new TaskDefnRevTO(), HR, true );
      new TaskDefnService().activate( lReqDefnKey, new TaskDefnRevTO(), HR, true );

      // Emulate baseline sync processing of the inv_sync_queue rows
      // ( added by TaskDefnService.activate() ).
      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      List<TaskKey> lTasks =
            TaskDefnUtils.getActualTasks( lJicDefn, lAircraft, RefEventStatusKey.ACTV );

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lTasks.get( 0 ), "sched_db_id", "sched_id" );
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "sched_step", lArgs );

      if ( !lQs.next() ) {
         fail( String.format(
               "Expected a requirement step to be created for Task with TaskKey = %s",
               lTasks.get( 0 ).toString() ) );
      }
      Assert.assertNull( "Unexpected step description, it should be blank",
            lQs.getString( "step_ldesc" ) );
   }


   /**
    * <pre>
    * Given an on-condition task definition revision with ACTV status
    * And another revision that is not on-condition in REVISION status
    * And manual initialization is prevented on the task definition
    * When the task definition revision in REVISION status is activated
    * Then manual initialization of task definition is allowed
    * </pre>
    */
   @Test
   public void itAllowsManualInitializationWhenNonOnConditionTaskDefinitionActivated()
         throws Exception {

      // Given
      TaskTaskKey lReqDefnInActvState = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
         aReqDefn.setRevisionNumber( REVISION_1 );
         aReqDefn.setOnCondition( true );
         aReqDefn.setPreventManualInitialization( true );
      } );

      TaskTaskKey lReqDefnInRevisionState = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.setStatus( RefTaskDefinitionStatusKey.REVISION );
         aReqDefn.setRevisionNumber( REVISION_2 );
         aReqDefn.setOnCondition( false );
         aReqDefn.setPreventManualInitialization( true );
         aReqDefn.setPreviousRevision( lReqDefnInActvState );
      } );
      // When
      new TaskDefnService().activate( lReqDefnInRevisionState, new TaskDefnRevTO(), HR, true );

      // Then
      TaskDefnKey lTaskDefnKey =
            TaskTaskTable.findByPrimaryKey( lReqDefnInRevisionState ).getTaskDefn();

      boolean lActualPreventManualInitialization =
            taskDefnDao.findByPrimaryKey( lTaskDefnKey ).isPreventManualInitialization();
      // Prevent manual initialization false implies that manual initialization is allowed
      boolean lExpectedPreventManualInitialization = false;

      assertEquals(
            "Unexpectedly, making the task definition not on-condition, did not allow manual initialization  ",
            lExpectedPreventManualInitialization, lActualPreventManualInitialization );

   }


   /**
    * <pre>
    * Given an on-condition task definition revision with ACTV status
    * And another revision that is not on-condition in REVISION status
    * And manual initialization is prevented on the task definition
    * When the task definition revision in REVISION status is activated
    * Then a history note is generated that indicates manual initialization is allowed
    * </pre>
    */
   @Test
   public void
         itGeneratesHistoryNoteIndicatingManualInitializationAllowedWhenNonOnConditionTaskDefinitionActivated()
               throws Exception {

      // Given
      TaskTaskKey lReqDefnInActvState = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
         aReqDefn.setRevisionNumber( REVISION_1 );
         aReqDefn.setOnCondition( true );
         aReqDefn.setPreventManualInitialization( true );
      } );

      TaskTaskKey lReqDefnInRevisionState = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.setStatus( RefTaskDefinitionStatusKey.REVISION );
         aReqDefn.setRevisionNumber( REVISION_2 );
         aReqDefn.setOnCondition( false );
         aReqDefn.setPreventManualInitialization( true );
         aReqDefn.setPreviousRevision( lReqDefnInActvState );
      } );
      // When
      new TaskDefnService().activate( lReqDefnInRevisionState, new TaskDefnRevTO(), HR, true );

      // Then
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lReqDefnInRevisionState, TaskTaskTable.ColumnName.TASK_DB_ID.name(),
            TaskTaskTable.ColumnName.TASK_ID.name() );
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( TASK_TASK_LOG_TABLE, lArgs );
      if ( !lQs.next() )
         fail( "Unexpectedly, Allow Manual Initialization action not logged" );

      String lExpectedSystemNote = i18n.get( "core.msg.ALLOW_MANUAL_INITIALIZATION" );
      assertEquals( "Unexpected system note for allow manual initialziation", lExpectedSystemNote,
            lQs.getString( TaskTaskLogTable.ColumnName.SYSTEM_NOTE.name() ) );

   }


   /**
    * <pre>
    *  Given - an assembly with a TRK config slot AND
    *          a part on the TRK config slot AND
    *          an inventory based on the assembly AND
    *          an ACTV not-on-condiiton REQ definition (with a code and name) against the assembly AND
    *          an ACTV task based on the REQ definition (with a code and name) against the inventory AND
    *          a revision of the REQ where only the name has changed
    *  When  - the revision REQ is activated and baseline sync is run
    *  Then  - the name of the task against the inventory is the new name
    * </pre>
    */
   @Test
   public void itUpdatesTheTaskNameWhenChangedInRevisionAndBaselineSyncIsRun() throws Exception {

      // Given
      final PartNoKey lAircraftPart = Domain.createPart();
      final AssemblyKey lAircraftAssemblyWithPart =
            createAircraftAssembly( lAircraftPart, FIRST_AIRCRAFT_ASSEMBLY_WITH_PART );

      final InventoryKey lAircraftWithPart = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( lAircraftAssemblyWithPart );
         aAircraft.setPart( lAircraftPart );
         aAircraft.allowSynchronization();
      } );

      final TaskTaskKey lActvReqDefn =
            Domain.createRequirementDefinition( aRequirementDefinition -> {
               aRequirementDefinition.setTaskClass( RefTaskClassKey.REQ );
               aRequirementDefinition.setOnCondition( false );
               aRequirementDefinition.againstConfigurationSlot(
                     Domain.readRootConfigurationSlot( lAircraftAssemblyWithPart ) );
               aRequirementDefinition.setRevisionNumber( REVISION_1 );
               aRequirementDefinition.setStatus( RefTaskDefinitionStatusKey.ACTV );
               aRequirementDefinition.setTaskName( ACTV_REQ_NAME );
               aRequirementDefinition.setCode( ACTV_REQ_CODE );
            } );

      final TaskKey lActvTask = Domain.createRequirement( aTask -> {
         aTask.setDefinition( lActvReqDefn );
         aTask.setInventory( lAircraftWithPart );
         aTask.setStatus( RefEventStatusKey.ACTV );
         aTask.setName( ACTV_REQ_NAME );
         aTask.setCode( ACTV_REQ_CODE );
      } );

      final TaskTaskKey lRevReqDefn =
            Domain.createRequirementDefinition( aRequirementDefinition -> {
               aRequirementDefinition.setTaskClass( RefTaskClassKey.REQ );
               aRequirementDefinition.setOnCondition( false );
               aRequirementDefinition.againstConfigurationSlot(
                     Domain.readRootConfigurationSlot( lAircraftAssemblyWithPart ) );
               aRequirementDefinition.setRevisionNumber( REVISION_2 );
               aRequirementDefinition.setStatus( RefTaskDefinitionStatusKey.REVISION );
               aRequirementDefinition.setPreviousRevision( lActvReqDefn );
               aRequirementDefinition.setTaskName( REV_REQ_NAME );
               aRequirementDefinition.setCode( ACTV_REQ_CODE );
            } );

      // When
      new TaskDefnService().activate( lRevReqDefn, new TaskDefnRevTO(), HR, FORCE_ACTIVATE );

      // Emulate baseline sync processing of the inv_sync_queue rows
      // ( added by TaskDefnService.activate() ).
      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      // Then
      assertThat( "The task against the inventory has the wrong name.",
            evtEventDao.findByPrimaryKey( lActvTask ).getEventSdesc(),
            is( ACTV_REQ_CODE + " (" + REV_REQ_NAME + ")" ) );

   }


   /**
    * <pre>
    *  Given - a TRK part A, an assembly A and a TRK inventory A with a manufactured date (based on assembly A) AND
    *          a TRK part B, an assembly B and a TRK inventory B with a manufactured date (based on assembly B) AND
    *          an ACTV not on-condition REQ definition against part A AND
    *          an ACTV task based on the definition against inventory A AND
    *          a revision of the REQ definition against part B
    *  When  - the revision REQ is activated and baseline sync is run
    *  Then  - the task is created against inventory B and the task against inventory A is cancelled
    * </pre>
    */
   @Test
   public void itCreatesTaskAgainstNewPartWhenPartIsChangedInRevisionAndBaselineSyncIsRun()
         throws Exception {

      // Baseline Sync needs to have this permission for being able
      // to cancel tasks that are no longer applicable
      int lUserId = OrgHr.findByPrimaryKey( HumanResourceKey.ADMIN ).getUserId();
      UserParametersStub lUserParametersStub = new UserParametersStub( lUserId, "LOGIC" );
      lUserParametersStub.setBoolean( "ALLOW_ASSIGN_LRP_ITEM_FOR_INWORK_WP", false );
      UserParameters.setInstance( lUserId, "LOGIC", lUserParametersStub );

      // Given
      final PartNoKey lTrkPartA = Domain.createPart( aPart -> {
         aPart.setInventoryClass( RefInvClassKey.TRK );
      } );
      final AssemblyKey lAssemblyA = Domain.createAircraftAssembly( aAircraftAssembly -> {
         aAircraftAssembly.setCode( ASSEMBLY_WITH_CONFIG_SLOT_A );
      } );
      final InventoryKey lTrkInvA = Domain.createTrackedInventory( aTrkInv -> {
         aTrkInv.setAllowSynchronization( true );
         aTrkInv.setPartNumber( lTrkPartA );
         aTrkInv.setManufactureDate( DateUtils.addDays( new Date(), -10 ) );
         aTrkInv.setAssembly( lAssemblyA );
      } );
      final PartNoKey lTrkPartB = Domain.createPart( aPart -> {
         aPart.setInventoryClass( RefInvClassKey.TRK );
      } );
      final AssemblyKey lAssemblyB = Domain.createAircraftAssembly( aAircraftAssembly -> {
         aAircraftAssembly.setCode( ASSEMBLY_WITH_CONFIG_SLOT_B );
      } );
      final InventoryKey lTrkInvB = Domain.createTrackedInventory( aTrkInv -> {
         aTrkInv.setAllowSynchronization( true );
         aTrkInv.setPartNumber( lTrkPartB );
         aTrkInv.setManufactureDate( DateUtils.addDays( new Date(), -10 ) );
         aTrkInv.setAssembly( lAssemblyB );
      } );
      final TaskTaskKey lActvReqDefn =
            Domain.createRequirementDefinition( aRequirementDefinition -> {
               aRequirementDefinition.setTaskClass( RefTaskClassKey.REQ );
               aRequirementDefinition.setOnCondition( false );
               aRequirementDefinition.addPartNo( lTrkPartA );
               aRequirementDefinition.setRevisionNumber( REVISION_1 );
               aRequirementDefinition.setStatus( RefTaskDefinitionStatusKey.ACTV );
            } );
      final TaskKey lActvTask = Domain.createRequirement( aTask -> {
         aTask.setDefinition( lActvReqDefn );
         aTask.setInventory( lTrkInvA );
         aTask.setStatus( RefEventStatusKey.ACTV );
      } );
      final TaskTaskKey lRevReqDefn =
            Domain.createRequirementDefinition( aRequirementDefinition -> {
               aRequirementDefinition.setTaskClass( RefTaskClassKey.REQ );
               aRequirementDefinition.setOnCondition( false );
               aRequirementDefinition.addPartNo( lTrkPartB );
               aRequirementDefinition.setRevisionNumber( REVISION_2 );
               aRequirementDefinition.setPreviousRevision( lActvReqDefn );
               aRequirementDefinition.setStatus( RefTaskDefinitionStatusKey.REVISION );
            } );

      // When
      new TaskDefnService().activate( lRevReqDefn, new TaskDefnRevTO(), HR, FORCE_ACTIVATE );

      // Emulate baseline sync processing of the inv_sync_queue rows
      // ( added by TaskDefnService.activate() ).
      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      // Then
      List<TaskKey> lActvTasks =
            TaskDefnUtils.getActualTasks( lRevReqDefn, lTrkInvB, RefEventStatusKey.ACTV );
      assertThat( "There was not 1 ACTV task against inventory B.", lActvTasks.size(), is( 1 ) );
      assertThat( "There was not 1 canceled task against inventory A.",
            evtEventDao.findByPrimaryKey( lActvTask ).getEventStatusCd(),
            is( RefEventStatusKey.CANCEL.getCd() ) );

   }


   /**
    * <pre>
    *  Given - an ACTV not on-condition requirement definition with TRK part A (which has a manufactured date) and TRK part B (which has a manufactured date) AND
    *          the TRK parts are based on an assembly with a config slot AND
    *          an ACTV task based on the requirement definition against TRK part A AND
    *          an ACTV task based on the requirement definition against TRK part B AND
    *          a revision of this requirement where the only part assigned to it is TRK part B
    *  When  - the revision is activated and baseline sync is run
    *  Then  - the task against TRK part A is canceled and the task against TRK part B remains active
    * </pre>
    */
   @Test
   public void
         itCancelsTaskAgainstInventoryWhenInventoryIsRemovedFromRevisionAndActivatedAndBaselineSyncIsRun()
               throws Exception {

      // Baseline Sync needs to have this permission for being able
      // to cancel tasks that are no longer applicable
      int lUserId = OrgHr.findByPrimaryKey( HumanResourceKey.ADMIN ).getUserId();
      UserParametersStub lUserParametersStub = new UserParametersStub( lUserId, "LOGIC" );
      lUserParametersStub.setBoolean( "ALLOW_ASSIGN_LRP_ITEM_FOR_INWORK_WP", false );
      UserParameters.setInstance( lUserId, "LOGIC", lUserParametersStub );

      // Given
      final PartNoKey lTrkPartA = Domain.createPart( aPart -> {
         aPart.setInventoryClass( RefInvClassKey.TRK );
      } );
      final AssemblyKey lAssemblyA = Domain.createAircraftAssembly( aAircraftAssembly -> {
         aAircraftAssembly.setCode( ASSEMBLY_WITH_CONFIG_SLOT_A );
      } );
      final InventoryKey lTrkInvA = Domain.createTrackedInventory( aTrkInv -> {
         aTrkInv.setAllowSynchronization( true );
         aTrkInv.setPartNumber( lTrkPartA );
         aTrkInv.setManufactureDate( DateUtils.addDays( new Date(), -10 ) );
         aTrkInv.setAssembly( lAssemblyA );
      } );
      final PartNoKey lTrkPartB = Domain.createPart( aPart -> {
         aPart.setInventoryClass( RefInvClassKey.TRK );
      } );
      final AssemblyKey lAssemblyB = Domain.createAircraftAssembly( aAircraftAssembly -> {
         aAircraftAssembly.setCode( ASSEMBLY_WITH_CONFIG_SLOT_B );
      } );
      final InventoryKey lTrkInvB = Domain.createTrackedInventory( aTrkInv -> {
         aTrkInv.setAllowSynchronization( true );
         aTrkInv.setPartNumber( lTrkPartB );
         aTrkInv.setManufactureDate( DateUtils.addDays( new Date(), -10 ) );
         aTrkInv.setAssembly( lAssemblyB );
      } );
      final TaskTaskKey lActvReqDefn =
            Domain.createRequirementDefinition( aRequirementDefinition -> {
               aRequirementDefinition.setStatus( RefTaskDefinitionStatusKey.ACTV );
               aRequirementDefinition.setRevisionNumber( REVISION_1 );
               aRequirementDefinition.setOnCondition( false );
               aRequirementDefinition.addPartNo( lTrkPartA );
               aRequirementDefinition.addPartNo( lTrkPartB );
            } );
      final TaskKey lTaskInvWithPartA = Domain.createRequirement( aRequirement -> {
         aRequirement.setDefinition( lActvReqDefn );
         aRequirement.setInventory( lTrkInvA );
         aRequirement.setStatus( RefEventStatusKey.ACTV );
      } );
      final TaskKey lTaskInvWithPartB = Domain.createRequirement( aRequirement -> {
         aRequirement.setDefinition( lActvReqDefn );
         aRequirement.setInventory( lTrkInvB );
         aRequirement.setStatus( RefEventStatusKey.ACTV );
      } );
      final TaskTaskKey lRevReqDefn =
            Domain.createRequirementDefinition( aRequirementDefinition -> {
               aRequirementDefinition.setStatus( RefTaskDefinitionStatusKey.REVISION );
               aRequirementDefinition.setRevisionNumber( REVISION_2 );
               aRequirementDefinition.setPreviousRevision( lActvReqDefn );
               aRequirementDefinition.setOnCondition( false );
               aRequirementDefinition.addPartNo( lTrkPartB );
            } );

      // When
      new TaskDefnService().activate( lRevReqDefn, new TaskDefnRevTO(), HR, FORCE_ACTIVATE );
      // Emulate baseline sync processing of the inv_sync_queue rows
      // // ( added by TaskDefnService.activate() ).
      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      // Then
      EvtEventTable lCanceledTask = evtEventDao.findByPrimaryKey( lTaskInvWithPartA );
      EvtEventTable lActvTask = evtEventDao.findByPrimaryKey( lTaskInvWithPartB );
      assertThat( "The task against inventory A was not canceled.",
            lCanceledTask.getEventStatusCd(), is( RefEventStatusKey.CANCEL.getCd() ) );
      assertThat( "The task against inventory B should be ACTV.", lActvTask.getEventStatusCd(),
            is( RefEventStatusKey.ACTV.getCd() ) );

   }


   /**
    * <pre>
    *  Given - a requirement definition with revision status AND
    *          a part A assigned to the requirement definition AND
    *          a part B assigned to the requirement definition
    *  When  - part A is unassigned from the requirement definition
    *  Then  - only part B is assigned to the requirement definition
    * </pre>
    */
   @Test
   public void itUnassignsAPartFromARequirementDefinition() throws Exception {

      // Given
      PartNoKey lPartNoKeysToRemove[] = { Domain.createPart() };
      PartNoKey lPartNoKeyB = Domain.createPart();
      final TaskTaskKey lRevReqDefn =
            Domain.createRequirementDefinition( aRequirementDefinition -> {
               aRequirementDefinition.setStatus( RefTaskDefinitionStatusKey.REVISION );
               aRequirementDefinition.addPartNo( lPartNoKeysToRemove[0] );
               aRequirementDefinition.addPartNo( lPartNoKeyB );
            } );

      // When
      TaskDefnService.unassignParts( lRevReqDefn, lPartNoKeysToRemove );

      // Then
      assertThat( "Part A should not be assigned to the REQ.", TaskPartMapTable
            .findByPrimaryKey( new TaskPartMapKey( lRevReqDefn, lPartNoKeysToRemove[0] ) ).exists(),
            is( false ) );
      assertThat(
            "Part B should still be assigned to the REQ.", TaskPartMapTable
                  .findByPrimaryKey( new TaskPartMapKey( lRevReqDefn, lPartNoKeyB ) ).exists(),
            is( true ) );

   }


   /**
    * This test case is testing when duplicate a repref task definition, the repref attributes will
    * be copied to the new task definition.
    *
    * <pre>
    *    Given a repref task definition.
    *    When duplicate the repref task definition.
    *    Then verify the new repref has the same repref attributes as the original one.
    * </pre>
    */
   @Test
   public void itCopiesReprefAttributesWhenDuplicate() throws MxException {

      Boolean lMocApprovalBool = false;
      Boolean lDamageRecordBool = true;
      Boolean lDamagedComponentBool = false;
      String lOpsRestrictionsDesc = "OpsRestrictionsDesc";
      String lPerfPenaltiesDesc = "PerfPenaltiesDesc";

      AssemblyKey lAssembly = Domain.createAircraftAssembly();
      ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAssembly );
      TaskTaskKey lTaskDefn = Domain.createRequirementDefinition( aRequirementDefn -> {
         aRequirementDefn.setTaskClass( RefTaskClassKey.REPREF );
         aRequirementDefn.againstConfigurationSlot( lRootConfigSlot );
         aRequirementDefn.setMocApprovalBool( lMocApprovalBool );
         aRequirementDefn.setDamageRecordBool( lDamageRecordBool );
         aRequirementDefn.setDamagedComponentBool( lDamagedComponentBool );
         aRequirementDefn.setOpsRestrictionsDesc( lOpsRestrictionsDesc );
         aRequirementDefn.setPerfPenaltiesDesc( lPerfPenaltiesDesc );
      } );

      TaskTaskKey lDuplicatedTaskDefn =
            TaskDefnService.duplicate( lTaskDefn, new DuplicateTaskDefinitionTO(), HR );

      TaskRepRefDao lTaskRepRefDao = InjectorContainer.get().getInstance( TaskRepRefDao.class );
      TaskRepRefTableRow lTaskRepRefTableRow =
            lTaskRepRefDao.findByTaskTaskKey( lDuplicatedTaskDefn );

      assertEquals( "MOC approval bool is not copied correctly.", lMocApprovalBool,
            lTaskRepRefTableRow.getMocApprovalBool() );
      assertEquals( "Damage record bool is not copied correctly.", lDamageRecordBool,
            lTaskRepRefTableRow.getDamageRecordBool() );
      assertEquals( "Damaged component bool is not copied correctly.", lDamagedComponentBool,
            lTaskRepRefTableRow.getDamagedComponentBool() );
      assertEquals( "Operational restrictions description is not copied correctly.",
            lOpsRestrictionsDesc, lTaskRepRefTableRow.getOperationalRestrictionsDescription() );
      assertEquals( "Performance penalties description is not copied correctly.",
            lPerfPenaltiesDesc, lTaskRepRefTableRow.getPerformancePenaltiesDescription() );
   }


   /**
    * This test case is testing when duplicate a requirement definition, the prevent initialization
    * bool will be copied to the new requirement definition.
    *
    * <pre>
    * Given a requirement definition with prevent initialization bool as true.
    * When duplicate it.
    * Then verify the new requirement definition's prevent initialization bool is true.
    * </pre>
    */
   @Test
   public void itRemainsPreventInitializationWhenDuplicate() throws MxException {
      Boolean lPreventManualInitialization = true;
      AssemblyKey lAssembly = Domain.createAircraftAssembly();
      ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAssembly );
      TaskTaskKey lTaskDefn = Domain.createRequirementDefinition( aRequirementDefn -> {
         aRequirementDefn.againstConfigurationSlot( lRootConfigSlot );
         aRequirementDefn.setPreventManualInitialization( lPreventManualInitialization );
      } );

      TaskTaskKey lDuplicatedTaskDefn =
            TaskDefnService.duplicate( lTaskDefn, new DuplicateTaskDefinitionTO(), HR );

      TaskDefnKey lTaskDefnKey =
            TaskTaskTable.findByPrimaryKey( lDuplicatedTaskDefn ).getTaskDefn();

      TaskDefnDao lTaskDefnDao = InjectorContainer.get().getInstance( TaskDefnDao.class );

      TaskDefnTable lTaskDefnTable = lTaskDefnDao.findByPrimaryKey( lTaskDefnKey );

      assertTrue( "Task defnition is not created properly.", lTaskDefnTable.exists() );

      assertEquals( "The prevent manual initialization bool is not copied correctly.",
            lPreventManualInitialization, lTaskDefnTable.isPreventManualInitialization() );

   }


   /**
    * This test case is testing when create revision of a repref task definition, the repref
    * attributes will be copied to the revised task definition.
    *
    * <pre>
    *    Given a repref task definition.
    *    When revise the repref task definition.
    *    Then verify the revised repref has the same repref attributes as the original one.
    * </pre>
    */
   @Test
   public void itCopiesReprefAttributesWhenCreateRevision() throws Exception {

      Boolean lMocApprovalBool = false;
      Boolean lDamageRecordBool = true;
      Boolean lDamagedComponentBool = false;
      String lOpsRestrictionsDesc = "OpsRestrictionsDesc";
      String lPerfPenaltiesDesc = "PerfPenaltiesDesc";

      AssemblyKey lAssembly = Domain.createAircraftAssembly();
      ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAssembly );
      TaskTaskKey lTaskDefn = Domain.createRequirementDefinition( aRequirementDefn -> {
         aRequirementDefn.setTaskClass( RefTaskClassKey.REPREF );
         aRequirementDefn.againstConfigurationSlot( lRootConfigSlot );
         aRequirementDefn.setMocApprovalBool( lMocApprovalBool );
         aRequirementDefn.setDamageRecordBool( lDamageRecordBool );
         aRequirementDefn.setDamagedComponentBool( lDamagedComponentBool );
         aRequirementDefn.setOpsRestrictionsDesc( lOpsRestrictionsDesc );
         aRequirementDefn.setPerfPenaltiesDesc( lPerfPenaltiesDesc );
      } );

      TaskTaskKey lDuplicatedTaskDefn =
            TaskDefnService.createRevision( lTaskDefn, new TaskDefnRevTO() );

      TaskRepRefDao lTaskRepRefDao = InjectorContainer.get().getInstance( TaskRepRefDao.class );
      TaskRepRefTableRow lTaskRepRefTableRow =
            lTaskRepRefDao.findByTaskTaskKey( lDuplicatedTaskDefn );

      assertEquals( "MOC approval bool is not copied correctly.", lMocApprovalBool,
            lTaskRepRefTableRow.getMocApprovalBool() );
      assertEquals( "Damage record bool is not copied correctly.", lDamageRecordBool,
            lTaskRepRefTableRow.getDamageRecordBool() );
      assertEquals( "Damaged component bool is not copied correctly.", lDamagedComponentBool,
            lTaskRepRefTableRow.getDamagedComponentBool() );
      assertEquals( "Operational restrictions description is not copied correctly.",
            lOpsRestrictionsDesc, lTaskRepRefTableRow.getOperationalRestrictionsDescription() );
      assertEquals( "Performance penalties description is not copied correctly.",
            lPerfPenaltiesDesc, lTaskRepRefTableRow.getPerformancePenaltiesDescription() );
   }


   /**
    * <pre>
    *  Given - a requirement definition with revision status AND
    *          a part
    *  When  - the part is assigned to the requirement definition
    *  Then  - the part is now assigned to the requirement definition
    * </pre>
    */
   @Test
   public void itAssignsAPartToARequirementDefinition() throws Exception {

      // Given
      PartNoKey lPartNoKeysToAssign[] = { Domain.createPart() };

      final TaskTaskKey lRevReqDefn =
            Domain.createRequirementDefinition( aRequirementDefinition -> {
               aRequirementDefinition.setStatus( RefTaskDefinitionStatusKey.REVISION );
            } );

      // When
      TaskDefnService.assignParts( lRevReqDefn, lPartNoKeysToAssign );

      // Then
      assertThat( "The part should  be assigned to the REQ.", TaskPartMapTable
            .findByPrimaryKey( new TaskPartMapKey( lRevReqDefn, lPartNoKeysToAssign[0] ) ).exists(),
            is( true ) );

   }


   /**
    * <pre>
    * Given an aircraft assembly
    * When a repair reference type of requirement definition is created against the root config slot of the assembly
    * Then the prevent initialization boolean value is set to true for this repair reference requirement definition
    * </pre>
    */
   @Test
   public void itSetsPreventInitializationToTrueForRepairReferenceOnItsCreation() throws Exception {

      AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly();
      // Needed by the validations in the service
      final OrgKey lOrgkey = Domain
            .createOrganization( aOrganization -> aOrganization.setType( RefOrgTypeKey.MRO ) );
      HumanResourceKey lHr = Domain.createHumanResource( aHr -> {
         aHr.setUser( Domain.createUser() );
         aHr.setOrganization( lOrgkey );
      } );
      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( lHr ) );

      RequirementTO lRequirementTO = new RequirementTO();
      lRequirementTO.setAssembly( lAircraftAssembly, TEST_ERROR_LABEL );
      lRequirementTO.setTaskClass( RefTaskClassKey.REPREF, TEST_ERROR_LABEL );
      lRequirementTO.setCode( TEST_ERROR_LABEL, TEST_ERROR_LABEL );
      lRequirementTO.setOrganization( lOrgkey, TEST_ERROR_LABEL );
      lRequirementTO.setForecastRange( Double.valueOf( 10 ), TEST_ERROR_LABEL );
      TaskTaskKey lRepairReferenceDefinition = TaskDefnService.create( lRequirementTO, lHr );

      TaskDefnKey lTaskDefnKey =
            TaskTaskTable.findByPrimaryKey( lRepairReferenceDefinition ).getTaskDefn();
      TaskDefnDao lTaskDefnDao = InjectorContainer.get().getInstance( TaskDefnDao.class );
      TaskDefnTable lTaskDefnTable = lTaskDefnDao.findByPrimaryKey( lTaskDefnKey );
      assertThat( "The newly created Repair Reference should prevent manual initialization",
            lTaskDefnTable.isPreventManualInitialization(), is( true ) );

   }


   /**
    * Helper method: Returns Active JICs assigned to a given Task
    *
    * @param aTask
    *           The initialized task
    *
    * @return Array of TaskKeys which represent the assigned active JICs
    */
   public List<TaskKey> getJicsAssignedToTask( TaskKey aTask ) {

      // Use the ids of the initialized task that was passed in
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTask, new String[] { "aTaskDbId", "aTaskId" } );

      QuerySet lDs = QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.mx.web.query.task.SubTasks", lArgs );

      // Build the array
      List<TaskKey> lTasks = new ArrayList<TaskKey>( lDs.getRowCount() );

      while ( lDs.next() ) {
         TaskKey lSubTask = new TaskKey( lDs.getInt( "event_db_id" ), lDs.getInt( "event_id" ) );
         SchedStaskTable lJic = schedStaskDao.findByPrimaryKey( lSubTask );

         // Make sure the subtask if of type 'JIC'
         if ( lJic.getTaskClass().equals( RefTaskClassKey.JIC ) ) {
            lTasks.add( lSubTask );
         }
      }

      return lTasks;

   }


   private boolean isTaskAssignedToTask( TaskKey aChildTask, TaskKey aParentTask ) {
      EventKey lParentEvent = evtEventDao.findByPrimaryKey( aParentTask.getEventKey() ).getPk();
      EventKey lChildNextHighestEvent =
            evtEventDao.findByPrimaryKey( aChildTask.getEventKey() ).getNhEvent();

      return lParentEvent.equals( lChildNextHighestEvent );
   }


   /* Helper method to accrue usage on an aircraft in the past by adding a flight */
   public void accrueUsage( InventoryKey aAircraft, Date aUsageDate, BigDecimal aUsageRecordHours,
         BigDecimal aUsageDelta ) throws Exception {

      final InventoryKey lAircraft = aAircraft;
      final Date lUsageDate = aUsageDate;
      final BigDecimal lUsageRecordHours = aUsageRecordHours;
      final BigDecimal lUsageRecordHoursDelta = aUsageDelta;

      final UsageAdjustmentId lUsageRecordKey =
            Domain.createUsageAdjustment( new DomainConfiguration<UsageAdjustment>() {

               @Override
               public void configure( UsageAdjustment aUsageAdjustment ) {
                  aUsageAdjustment.setUsageDate( lUsageDate );
                  aUsageAdjustment.setMainInventory( lAircraft );
                  aUsageAdjustment.addUsage( lAircraft, DataTypeKey.HOURS, lUsageRecordHours,
                        lUsageRecordHoursDelta );
               }
            } );

      FlightLegId aFlightId = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setUsageRecord( lUsageRecordKey );
         }
      } );

      // When the flight leg's usage is applied.
      iUsageAccrualService.applyUsageDelta( new ActualDatesCalculator(), aFlightId, false );

   }


   @Before
   public void setupConfigurationParameters() {
      GlobalParametersFake lConfigParms = new GlobalParametersFake( "WORK_MANAGER" );
      lConfigParms.setString( "WORK_ITEM_BSYNCVALIDATEACTUALS_THRESHOLD", "2" );
      GlobalParameters.setInstance( lConfigParms );
   }


   @Before
   public void setupUsageAccrualService() {

      SoftDeadlineService lSoftDeadlineService = new MxSoftDeadlineService();
      UpdateDeadlineService lUpdateDeadlineService =
            new MxUpdateDeadlineService( lSoftDeadlineService );

      iUsageAccrualService = new MxUsageAccrualService( lUpdateDeadlineService );

      iTaskStepSkillDao = InjectorContainer.get().getInstance( TaskStepSkillDao.class );

   }


   private TaskTaskKey createReqDefinitionWithStep( RefTaskDefinitionStatusKey status,
         String stepDescription ) {
      return Domain.createRequirementDefinition( defn -> {
         defn.addStep( stp -> {
            stp.setDescription( stepDescription );
         } );
         defn.setStatus( status );
      } );
   }


   private AssemblyKey createAircraftAssembly( final PartNoKey aAircraftPart,
         final String aAircraftCode ) {
      return Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

         @Override
         public void configure( AircraftAssembly aAircraftAssembly ) {
            aAircraftAssembly.setCode( aAircraftCode );
            aAircraftAssembly
                  .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aConfigurationSlot ) {
                        aConfigurationSlot.addPartGroup( new DomainConfiguration<PartGroup>() {

                           @Override
                           public void configure( PartGroup aPartGroup ) {
                              aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                              aPartGroup.addPart( aAircraftPart );
                           }
                        } );
                     }
                  } );
         }
      } );

   }


   private void setMaxStepDescriptionLength( int length ) {
      GlobalParametersFake configParmsFake = new GlobalParametersFake( "LOGIC" );
      configParmsFake.setInteger( "JOB_STEP_DEFINITION_DESCRIPTION_LENGTH_LIMIT", length );
      GlobalParameters.setInstance( configParmsFake );
   }

}
