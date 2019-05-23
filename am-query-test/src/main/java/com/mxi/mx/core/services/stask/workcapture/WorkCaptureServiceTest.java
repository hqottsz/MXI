package com.mxi.mx.core.services.stask.workcapture;

import static com.mxi.am.domain.Domain.createRequirement;
import static com.mxi.mx.core.key.RefLabourRoleStatusKey.ACTV;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.domain.Step;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.dataset.SQLStatement;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.security.SecurityIdentificationInterface;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.FailDeferRefKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.FaultReferenceKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefFailureSeverityKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefLabourStageKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.SchedLabourKey;
import com.mxi.mx.core.key.SchedLabourStepKey;
import com.mxi.mx.core.key.SchedStepKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.maintenance.exec.fault.infra.FaultReferenceDao;
import com.mxi.mx.core.maintenance.exec.fault.infra.FaultReferenceTableRow;
import com.mxi.mx.core.services.fault.faultreference.InconsistentFaultReferenceException;
import com.mxi.mx.core.services.stask.labour.FinishJobTO;
import com.mxi.mx.core.services.stask.labour.JobStopTO;
import com.mxi.mx.core.services.stask.labour.LabourStepTO;
import com.mxi.mx.core.services.stask.labour.SchedLabourTO;
import com.mxi.mx.core.table.sched.JdbcSchedStepDao;
import com.mxi.mx.core.table.sched.SchedLabourStepTable;
import com.mxi.mx.core.table.sched.SchedStepDao;
import com.mxi.mx.core.table.sched.SchedStepTableRow;
import com.mxi.mx.core.task.model.StepStatus;


/**
 * Integration tests for {@link WorkCaptureService}
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class WorkCaptureServiceTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   @Rule
   public ExpectedException expectedException = ExpectedException.none();

   private WorkCaptureService iService;

   private HumanResourceKey iTechnician;

   private static final RefLabourSkillKey ENG = RefLabourSkillKey.ENG;
   private static final RefLabourSkillKey LBR = RefLabourSkillKey.LBR;
   private static final RefLabourSkillKey PILOT = RefLabourSkillKey.PILOT;

   private int iStepRevisionNo = 1;

   private Map<SchedStepKey, Integer> iSchedStepOrdMap;

   private Map<String, UUID> iStepSkillsMap;

   private UserKey user;

   private SchedStepDao schedStepDao = new JdbcSchedStepDao();


   @Before
   public void setUp() {
      iService = new WorkCaptureService();
      // Required by the service
      user = Domain.createUser( aUser -> aUser.setUserId( 9999999 ) );
      iTechnician = Domain.createHumanResource( aHr -> aHr.setUser( user ) );
      SecurityIdentificationInterface securityIdentification =
            new SecurityIdentificationStub( iTechnician );
      SecurityIdentificationUtils.setInstance( securityIdentification );
   }


   @Test
   public void testCaptureStepsWithStepSkills() throws Exception {

      // DATA SETUP: Create requirement definition with steps that have skills
      TaskTaskKey lTaskTaskKey =
            Domain.createRequirementDefinition( ( RequirementDefinition aRefDefnBuilder ) -> {
               aRefDefnBuilder.setExecutable( true );
               aRefDefnBuilder.addStep( ( Step aStepBuilder ) -> {
                  aStepBuilder.setDescription( "Step 1" );
                  aStepBuilder.addStepSkill( ENG, false );
                  aStepBuilder.addStepSkill( PILOT, false );
               } );
               aRefDefnBuilder.addStep( ( Step aStepBuilder ) -> {
                  aStepBuilder.setDescription( "Step 2" );
                  aStepBuilder.addStepSkill( ENG, false );
                  aStepBuilder.addStepSkill( PILOT, false );
               } );
               aRefDefnBuilder.addStep( ( Step aStepBuilder ) -> {
                  aStepBuilder.setDescription( "Step 3" );
                  aStepBuilder.addStepSkill( ENG, false );
                  aStepBuilder.addStepSkill( PILOT, false );
               } );
               aRefDefnBuilder.addStep( ( Step aStepBuilder ) -> {
                  aStepBuilder.setDescription( "Step 4" );
                  aStepBuilder.addStepSkill( ENG, false );
                  aStepBuilder.addStepSkill( PILOT, false );
               } );
            } );

      // DATA SETUP: Create an actual task
      TaskKey lTaskKey = createRequirement( ( Requirement aBuilder ) -> {
         aBuilder.setDefinition( lTaskTaskKey );
         aBuilder.addLabour( aLabour -> {
            aLabour.setSkill( ENG );
            aLabour.setTechnicianRole( tech -> tech.setScheduledHours( 1 ) );
            aLabour.setCertifierRole( cert -> cert.setScheduledHours( 1 ) );
         } );
         aBuilder.addLabour( aLabour -> {
            aLabour.setSkill( PILOT );
            aLabour.setTechnicianRole( tech -> tech.setScheduledHours( 1 ) );
            aLabour.setCertifierRole( cert -> cert.setScheduledHours( 1 ) );
         } );
      } );

      // DATA SETUP: Create a work package and assign the task to it
      Domain.createWorkPackage( ( aWpBuilder ) -> {
         aWpBuilder.addTask( lTaskKey );
         aWpBuilder.setStatus( RefEventStatusKey.IN_WORK );
      } );

      iSchedStepOrdMap = getSteps( lTaskKey );

      assertEquals( iSchedStepOrdMap.size(), 4 );

      List<SchedStepKey> lSchedSteps = new ArrayList<SchedStepKey>( iSchedStepOrdMap.keySet() );

      Map<RefLabourSkillKey, SchedLabourKey> lLabourMap = getLabourRequirements( lTaskKey );

      iStepSkillsMap = getStepSkills( lTaskTaskKey );

      assertEquals( iStepSkillsMap.size(), 8 );

      // *******************************************
      // *******************************************
      // TEST CASE 1: Sign off ENG labour row
      // *******************************************
      // *******************************************
      List<LabourStepTO> lLabourStepTOs = new ArrayList<LabourStepTO>();

      lLabourStepTOs.add( getLabourStepTO( lSchedSteps.get( 0 ), ENG, StepStatus.MXPARTIAL ) );

      lLabourStepTOs.add( getLabourStepTO( lSchedSteps.get( 1 ), ENG, StepStatus.MXCOMPLETE ) );

      lLabourStepTOs.add( getLabourStepTO( lSchedSteps.get( 2 ), ENG, StepStatus.MXCOMPLETE ) );

      lLabourStepTOs.add( getLabourStepTO( lSchedSteps.get( 3 ), ENG, StepStatus.MXPENDING ) );

      iService.captureWork( lLabourMap.get( ENG ), getJobStopTO( lLabourStepTOs ) );

      // *******************************************
      // Asserts Step 1 is PARTIAL
      // Labour Skill: ENG Status: PARTIAL
      // Labour Skill: PILOT Status: Not signed
      // *******************************************
      SchedStepTableRow lSchedStepTable = schedStepDao.findByPrimaryKey( lSchedSteps.get( 0 ) );
      assertEquals( lSchedStepTable.getStepStatus(), StepStatus.MXPARTIAL );

      // *******************************************
      // Asserts Step 2 is PARTIAL
      // Labour Skill: ENG Status: COMPLETE
      // Labour Skill: PILOT Status: Not signed
      // *******************************************
      lSchedStepTable = schedStepDao.findByPrimaryKey( lSchedSteps.get( 1 ) );
      assertEquals( lSchedStepTable.getStepStatus(), StepStatus.MXPARTIAL );

      // *******************************************
      // Asserts Step 3 is PARTIAL
      // Labour Skill: ENG Status: COMPLETE
      // Labour Skill: PILOT Status: Not signed
      // *******************************************
      lSchedStepTable = schedStepDao.findByPrimaryKey( lSchedSteps.get( 2 ) );
      assertEquals( lSchedStepTable.getStepStatus(), StepStatus.MXPARTIAL );

      // *******************************************
      // Asserts Step 4 is PENDING
      // Labour Skill: ENG Status: PENDING
      // Labour Skill: PILOT Status: Not signed
      // *******************************************
      lSchedStepTable = schedStepDao.findByPrimaryKey( lSchedSteps.get( 3 ) );
      assertEquals( lSchedStepTable.getStepStatus(), StepStatus.MXPENDING );

      // *******************************************
      // *******************************************
      // TEST CASE 2: Sign off PILOT labour row
      // *******************************************
      // *******************************************

      lLabourStepTOs = new ArrayList<LabourStepTO>();

      lLabourStepTOs.add( getLabourStepTO( lSchedSteps.get( 0 ), PILOT, StepStatus.MXCOMPLETE ) );

      lLabourStepTOs.add( getLabourStepTO( lSchedSteps.get( 1 ), PILOT, StepStatus.MXCOMPLETE ) );

      lLabourStepTOs.add( getLabourStepTO( lSchedSteps.get( 2 ), PILOT, StepStatus.MXPENDING ) );

      lLabourStepTOs.add( getLabourStepTO( lSchedSteps.get( 3 ), PILOT, StepStatus.MXPENDING ) );

      iService.captureWork( lLabourMap.get( PILOT ), getJobStopTO( lLabourStepTOs ) );

      // *******************************************
      // Asserts Step 1 is PARTIAL
      // Labour Skill: ENG Status: PARTIAL
      // Labour Skill: PILOT Status: COMPLETE
      // *******************************************
      lSchedStepTable = schedStepDao.findByPrimaryKey( lSchedSteps.get( 0 ) );
      assertEquals( lSchedStepTable.getStepStatus(), StepStatus.MXPARTIAL );

      // *******************************************
      // Asserts Step 2 is COMPLETE
      // Labour Skill: ENG Status: COMPLETE
      // Labour Skill: PILOT Status: COMPLETE
      // *******************************************
      lSchedStepTable = schedStepDao.findByPrimaryKey( lSchedSteps.get( 1 ) );
      assertEquals( lSchedStepTable.getStepStatus(), StepStatus.MXCOMPLETE );

      // *******************************************
      // Asserts Step 3 is PARTIAL
      // Labour Skill: ENG Status: COMPLETE
      // Labour Skill: PILOT Status: PENDING
      // *******************************************
      lSchedStepTable = schedStepDao.findByPrimaryKey( lSchedSteps.get( 2 ) );
      assertEquals( lSchedStepTable.getStepStatus(), StepStatus.MXPARTIAL );

      // *******************************************
      // Asserts Step 4 is PENDING
      // Labour Skill: ENG Status: PENDING
      // Labour Skill: PILOT Status: PENDING
      // *******************************************
      lSchedStepTable = schedStepDao.findByPrimaryKey( lSchedSteps.get( 3 ) );
      assertEquals( lSchedStepTable.getStepStatus(), StepStatus.MXPENDING );
   }


   /**
    *
    * Test that baseline steps from a repair reference can be signed off.
    * <ul>
    * <li>Verifies step status is correctly updated when:</li>
    * <li>Step has one skill that is set to partial</li>
    * <li>Step has one skill that is set to complete</li>
    * <li>Step has two skills and only one is set to complete</li>
    * <li>Verifies that the step skill status is also correctly updated.</li>
    * </ul>
    *
    */
   @Test
   public void testCaptureStepsWithSkillsOnFault() throws Exception {

      // ARRANGE
      // create aircraft
      AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly( aircraftAssembly -> {
         aircraftAssembly.setRootConfigurationSlot( rootConfigSlot -> {
            rootConfigSlot.setCode( "ROOT" );
         } );
      } );

      InventoryKey aircraftInventoryKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.setDescription( "Aircraft" );
         aircraft.setLocation( Domain.createLocation() );
      } );

      // create a repair reference with steps and skills
      TaskTaskKey repairReferenceKey = Domain.createRequirementDefinition( reference -> {
         reference.setTaskClass( RefTaskClassKey.REPREF );
         reference.setTaskName( "Repair Reference" );
         reference.setCode( "REPREF1" );
         reference.setStatus( RefTaskDefinitionStatusKey.ACTV );
         reference.addStep( step -> {
            step.setDescription( "Repair Step 1" );
            step.addStepSkill( ENG, false );
         } );
         reference.addStep( step -> {
            step.setDescription( "Repair Step 2" );
            step.addStepSkill( ENG, false );
         } );
         reference.addStep( step -> {
            step.setDescription( "Repair Step 3" );
            step.addStepSkill( ENG, false );
            step.addStepSkill( LBR, false );
         } );
      } );

      // corrective task with labour that requires certification
      TaskKey correctiveTaskKey = Domain.createCorrectiveTask( task -> {
         task.setInventory( aircraftInventoryKey );
         task.addLabour( labour -> {
            labour.setSkill( ENG );
            labour.setTechnicianRole( tech -> {
               tech.setScheduledHours( BigDecimal.ONE );
            } );
            labour.setCertifierRole( cert -> {
               cert.setScheduledHours( BigDecimal.ONE );
            } );
         } );
         task.addLabour( labour -> {
            labour.setSkill( LBR );
            labour.setTechnicianRole( tech -> {
               tech.setScheduledHours( BigDecimal.TEN );
            } );
            labour.setCertifierRole( cert -> {
               cert.setScheduledHours( BigDecimal.ONE );
            } );
         } );
      } );

      // fault with reference
      FaultKey faultKey = Domain.createFault( fault -> {
         fault.setCorrectiveTask( correctiveTaskKey );
         fault.setCurrentRepairReference( repairReferenceKey );
      } );

      // work package for the fault
      Domain.createWorkPackage( ( aWpBuilder ) -> {
         aWpBuilder.addTask( correctiveTaskKey );
         aWpBuilder.setStatus( RefEventStatusKey.IN_WORK );
      } );

      // set up the LabourStepTO
      iSchedStepOrdMap = getSteps( correctiveTaskKey );
      assertEquals( 3, iSchedStepOrdMap.size() );

      iStepSkillsMap = getStepSkills( repairReferenceKey );
      assertEquals( iStepSkillsMap.size(), 4 );

      SchedStepKey stepOneKey = iSchedStepOrdMap.entrySet().stream()
            .filter( order -> order.getValue() == 1 ).findFirst().get().getKey();
      SchedStepKey stepTwoKey = iSchedStepOrdMap.entrySet().stream()
            .filter( order -> order.getValue() == 2 ).findFirst().get().getKey();
      SchedStepKey stepThreeKey = iSchedStepOrdMap.entrySet().stream()
            .filter( order -> order.getValue() == 3 ).findFirst().get().getKey();

      List<LabourStepTO> schedStepTOList = new ArrayList<>();
      schedStepTOList.add( getLabourStepTO( stepOneKey, ENG, StepStatus.MXPARTIAL ) );
      schedStepTOList.add( getLabourStepTO( stepTwoKey, ENG, StepStatus.MXCOMPLETE ) );
      schedStepTOList.add( getLabourStepTO( stepThreeKey, ENG, StepStatus.MXCOMPLETE ) );

      // set up the JobStopTO
      JobStopTO jobStopTO = getJobStopTO( schedStepTOList );
      FaultReferenceKey faultReferenceKey = Domain.readFaultReference( faultKey );
      jobStopTO.setFaultReferenceKey( faultReferenceKey );

      SchedLabourKey labourKey =
            Domain.readLabourRequirement( correctiveTaskKey, ENG, BigDecimal.ONE );

      // ACT

      iService.captureWork( labourKey, jobStopTO );

      // ASSERT

      // step 1 status should be partial
      SchedStepTableRow schedStepTable = schedStepDao.findByPrimaryKey( stepOneKey );
      assertEquals( StepStatus.MXPARTIAL, schedStepTable.getStepStatus() );

      // step 2 status should be complete
      schedStepTable = schedStepDao.findByPrimaryKey( stepTwoKey );
      assertEquals( StepStatus.MXCOMPLETE, schedStepTable.getStepStatus() );

      // step 3 status should be partial
      schedStepTable = schedStepDao.findByPrimaryKey( stepThreeKey );
      assertEquals( StepStatus.MXPARTIAL, schedStepTable.getStepStatus() );

      // step 1, ENG labour row status should be partial
      SchedLabourStepKey labourStepKey = new SchedLabourStepKey( labourKey, stepOneKey );
      SchedLabourStepTable schedLabourStepTable =
            SchedLabourStepTable.findByPrimaryKey( labourStepKey );
      assertEquals( StepStatus.MXPARTIAL, schedLabourStepTable.getStepStatus() );

      // step 2, ENG labour row status should be complete
      labourStepKey = new SchedLabourStepKey( labourKey, stepTwoKey );
      schedLabourStepTable = SchedLabourStepTable.findByPrimaryKey( labourStepKey );
      assertEquals( StepStatus.MXCOMPLETE, schedLabourStepTable.getStepStatus() );

      // step 3, ENG labour row status should be complete
      labourStepKey = new SchedLabourStepKey( labourKey, stepThreeKey );
      schedLabourStepTable = SchedLabourStepTable.findByPrimaryKey( labourStepKey );
      assertEquals( StepStatus.MXCOMPLETE, schedLabourStepTable.getStepStatus() );

   }


   @Test
   public void testCaptureStepsWithoutStepSkills() throws Exception {

      // DATA SETUP: Create requirement definition with steps that have no step skills
      TaskTaskKey lTaskTaskKey =
            Domain.createRequirementDefinition( ( RequirementDefinition aRefDefnBuilder ) -> {
               aRefDefnBuilder.setExecutable( true );
               aRefDefnBuilder.addStep( ( Step aStepBuilder ) -> {
                  aStepBuilder.setDescription( "Step 1" );
               } );
               aRefDefnBuilder.addStep( ( Step aStepBuilder ) -> {
                  aStepBuilder.setDescription( "Step 2" );
               } );
               aRefDefnBuilder.addStep( ( Step aStepBuilder ) -> {
                  aStepBuilder.setDescription( "Step 3" );
               } );
            } );

      // DATA SETUP: Create an actual task
      TaskKey lTaskKey = createRequirement( ( Requirement aBuilder ) -> {
         aBuilder.setDefinition( lTaskTaskKey );
         aBuilder.addLabour( aLabour -> {
            aLabour.setSkill( ENG );
            aLabour.setTechnicianRole( tech -> tech.setScheduledHours( 1 ) );
            aLabour.setCertifierRole( cert -> cert.setScheduledHours( 1 ) );
            aLabour.setInspectorRole( insp -> insp.setScheduledHours( 0 ) );
         } );
      } );

      // DATA SETUP: Create a work package and assign the task to it
      Domain.createWorkPackage( ( aWpBuilder ) -> {
         aWpBuilder.addTask( lTaskKey );
         aWpBuilder.setStatus( RefEventStatusKey.IN_WORK );
      } );

      iSchedStepOrdMap = getSteps( lTaskKey );

      assertEquals( iSchedStepOrdMap.size(), 3 );

      // Get all step keys
      List<SchedStepKey> lSchedSteps = new ArrayList<SchedStepKey>( iSchedStepOrdMap.keySet() );

      Map<RefLabourSkillKey, SchedLabourKey> lLabourMap = getLabourRequirements( lTaskKey );

      // *******************************************
      // *******************************************
      // TEST CASE: Sign off ENG labour row
      // *******************************************
      // *******************************************
      List<LabourStepTO> lLabourStepTOs = new ArrayList<LabourStepTO>();

      lLabourStepTOs.add( getLabourStepTO( lSchedSteps.get( 0 ), null, StepStatus.MXPENDING ) );
      lLabourStepTOs.add( getLabourStepTO( lSchedSteps.get( 1 ), null, StepStatus.MXPARTIAL ) );
      lLabourStepTOs.add( getLabourStepTO( lSchedSteps.get( 2 ), null, StepStatus.MXCOMPLETE ) );

      iService.captureWork( lLabourMap.get( ENG ), getJobStopTO( lLabourStepTOs ) );

      // Asserts Step 1 is PENDING
      SchedStepTableRow lSchedStepTable = schedStepDao.findByPrimaryKey( lSchedSteps.get( 0 ) );
      assertEquals( lSchedStepTable.getStepStatus(), StepStatus.MXPENDING );

      // Asserts Step 2 is PARTIAL
      lSchedStepTable = schedStepDao.findByPrimaryKey( lSchedSteps.get( 1 ) );
      assertEquals( lSchedStepTable.getStepStatus(), StepStatus.MXPARTIAL );

      // Asserts Step 3 is COMPLETE
      lSchedStepTable = schedStepDao.findByPrimaryKey( lSchedSteps.get( 2 ) );
      assertEquals( lSchedStepTable.getStepStatus(), StepStatus.MXCOMPLETE );
   }


   /**
    * <pre>
    * Given an aircraft assembly
    * And an aircraft based on that assembly
    * And a fault against the aircraft
    * And a corrective task based on the fault
    * And the fault is assigned to an active work package
    * And the fault has an associated fault reference
    * And a job stop action for a labour row is performed for the task
    * When a technician attempts to sign off the work while another technician has changed the fault reference to another fault reference
    * Then an error message is thrown indicating the user to re-check the fault reference.
    *
    * </pre>
    */
   @Test
   public void
         itThrowsInconsistentFaultReferenceExceptionWhenFaultReferenceChangedDuringWorkCaptureThroughJobStop()
               throws Exception {

      AssemblyKey aircraftAssembly = Domain.createAircraftAssembly();

      InventoryKey aircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( aircraftAssembly );
      } );

      TaskKey correctiveTask =
            Domain.createCorrectiveTask( aCorrTask -> aCorrTask.addLabour( aLabour -> {
               aLabour.setTechnicianRole( tech -> tech.setStatus( ACTV ) );
               aLabour.setStage( RefLabourStageKey.IN_WORK );
            } ) );

      FailDeferRefKey deferralReference = Domain.createDeferralReference( aDefRef -> {
         aDefRef.setAssemblyKey( aircraftAssembly );
         aDefRef.setFaultSeverityKey( RefFailureSeverityKey.MEL );
         aDefRef.setRequiredMocAuth( false );
      } );

      FailDeferRefKey deferralReferenceChangedWhileWorkCapture =
            Domain.createDeferralReference( aDefRef -> {
               aDefRef.setAssemblyKey( aircraftAssembly );
               aDefRef.setFaultSeverityKey( RefFailureSeverityKey.MEL );
               aDefRef.setRequiredMocAuth( false );
            } );

      Date faultFoundOnDate = new Date();

      Domain.createWorkPackage( aWp -> {
         aWp.addTask( correctiveTask );
         aWp.setStatus( RefEventStatusKey.IN_WORK );
      } );

      Domain.createFault( aFault -> {
         aFault.setCurrentDeferralReference( deferralReferenceChangedWhileWorkCapture );
         aFault.setCorrectiveTask( correctiveTask );
         aFault.setFoundOnDate( faultFoundOnDate );
         aFault.setInventory( aircraft );
         aFault.setNonCurrentDeferralReference( deferralReference );
      } );

      List<SchedLabourKey> labourList = Domain.readLabourRequirement( correctiveTask );
      if ( labourList.size() > 1 )
         fail( "Expected a row from the sched_labour table" );
      SchedLabourKey schedLabourKey = labourList.get( 0 );

      JobStopTO lJobStopTO = new JobStopTO();
      SchedLabourTO lSchedLabourTO = new SchedLabourTO();
      lSchedLabourTO.setEndDate( faultFoundOnDate, "" );
      lJobStopTO.setTechnician( iTechnician, "" );
      lJobStopTO.setSchedLabourTO( lSchedLabourTO );
      lJobStopTO.setRemainingHours( BigDecimal.TEN.doubleValue(), "" );
      FaultReferenceKey faultReferenceForDeferralReference =
            getFaultReferenceForDeferralReference( deferralReference );
      lJobStopTO.setFaultReferenceKey( faultReferenceForDeferralReference );

      expectedException.expect( InconsistentFaultReferenceException.class );
      expectedException.expectMessage( i18n.get( "core.err.33915" ) );
      iService.captureWork( schedLabourKey, lJobStopTO );

   }


   /**
    * <pre>
    * Given an aircraft assembly
    * And an aircraft based on that assembly
    * And a fault against the aircraft
    * And a corrective task based on the fault
    * And the fault is assigned to an active work package
    * And the fault has an associated fault reference
    * And a job stop action for a labour row is performed for the task
    * When a technician attempts to sign off the work while another technician has removed the fault reference
    * Then an error message is thrown indicating the user to re-check the fault reference.
    *
    * </pre>
    */
   @Test
   public void
         itThrowsInconsistentFaultReferenceExceptionWhenFaultReferenceRemovedDuringWorkCaptureThroughJobStop()
               throws Exception {

      AssemblyKey aircraftAssembly = Domain.createAircraftAssembly();

      InventoryKey aircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( aircraftAssembly );
      } );

      TaskKey correctiveTask =
            Domain.createCorrectiveTask( aCorrTask -> aCorrTask.addLabour( aLabour -> {
               aLabour.setTechnicianRole( tech -> tech.setStatus( ACTV ) );
               aLabour.setStage( RefLabourStageKey.IN_WORK );
            } ) );

      FailDeferRefKey deferralReference = Domain.createDeferralReference( aDefRef -> {
         aDefRef.setAssemblyKey( aircraftAssembly );
         aDefRef.setFaultSeverityKey( RefFailureSeverityKey.MEL );
         aDefRef.setRequiredMocAuth( false );
      } );
      Date faultFoundOnDate = new Date();

      Domain.createWorkPackage( aWp -> {
         aWp.addTask( correctiveTask );
         aWp.setStatus( RefEventStatusKey.IN_WORK );
      } );

      Domain.createFault( aFault -> {
         aFault.setCorrectiveTask( correctiveTask );
         aFault.setFoundOnDate( faultFoundOnDate );
         aFault.setInventory( aircraft );
         aFault.setNonCurrentDeferralReference( deferralReference );
      } );

      List<SchedLabourKey> labourList = Domain.readLabourRequirement( correctiveTask );
      if ( labourList.size() > 1 )
         fail( "Expected a row from the sched_labour table" );
      SchedLabourKey schedLabourKey = labourList.get( 0 );

      JobStopTO lJobStopTO = new JobStopTO();
      SchedLabourTO lSchedLabourTO = new SchedLabourTO();
      lSchedLabourTO.setEndDate( faultFoundOnDate, "" );
      lJobStopTO.setTechnician( iTechnician, "" );
      lJobStopTO.setSchedLabourTO( lSchedLabourTO );
      lJobStopTO.setRemainingHours( BigDecimal.TEN.doubleValue(), "" );
      FaultReferenceKey faultReferenceForDeferralReference =
            getFaultReferenceForDeferralReference( deferralReference );
      lJobStopTO.setFaultReferenceKey( faultReferenceForDeferralReference );

      expectedException.expect( InconsistentFaultReferenceException.class );
      expectedException.expectMessage( i18n.get( "core.err.33915" ) );
      iService.captureWork( schedLabourKey, lJobStopTO );

   }


   /**
    * <pre>
    * Given an aircraft assembly
    * And an aircraft based on that assembly
    * And a fault against the aircraft
    * And a corrective task based on the fault
    * And the fault is assigned to an active work package
    * And a job stop action for a labour row is performed for the task
    * When a technician attempts to sign off the work while another technician adds an associated fault reference
    * Then an error message is thrown indicating the user to re-check the fault reference.
    *
    * </pre>
    */
   @Test
   public void
         itThrowsInconsistentFaultReferenceExceptionWhenFaultReferenceAddedDuringWorkCaptureThroughJobStop()
               throws Exception {

      AssemblyKey aircraftAssembly = Domain.createAircraftAssembly();

      InventoryKey aircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( aircraftAssembly );
      } );

      TaskKey correctiveTask =
            Domain.createCorrectiveTask( aCorrTask -> aCorrTask.addLabour( aLabour -> {
               aLabour.setTechnicianRole( tech -> tech.setStatus( ACTV ) );
               aLabour.setStage( RefLabourStageKey.IN_WORK );
            } ) );

      FailDeferRefKey deferralReference = Domain.createDeferralReference( aDefRef -> {
         aDefRef.setAssemblyKey( aircraftAssembly );
         aDefRef.setFaultSeverityKey( RefFailureSeverityKey.MEL );
         aDefRef.setRequiredMocAuth( false );
      } );
      Date faultFoundOnDate = new Date();

      Domain.createWorkPackage( aWp -> {
         aWp.addTask( correctiveTask );
         aWp.setStatus( RefEventStatusKey.IN_WORK );
      } );

      Domain.createFault( aFault -> {
         aFault.setCorrectiveTask( correctiveTask );
         aFault.setFoundOnDate( faultFoundOnDate );
         aFault.setInventory( aircraft );
         aFault.setCurrentDeferralReference( deferralReference );
      } );

      List<SchedLabourKey> labourList = Domain.readLabourRequirement( correctiveTask );
      if ( labourList.size() > 1 )
         fail( "Expected a row from the sched_labour table" );
      SchedLabourKey schedLabourKey = labourList.get( 0 );

      JobStopTO lJobStopTO = new JobStopTO();
      SchedLabourTO lSchedLabourTO = new SchedLabourTO();
      lSchedLabourTO.setEndDate( faultFoundOnDate, "" );
      lJobStopTO.setTechnician( iTechnician, "" );
      lJobStopTO.setSchedLabourTO( lSchedLabourTO );
      lJobStopTO.setRemainingHours( BigDecimal.TEN.doubleValue(), "" );

      expectedException.expect( InconsistentFaultReferenceException.class );
      expectedException.expectMessage( i18n.get( "core.err.33915" ) );
      iService.captureWork( schedLabourKey, lJobStopTO );

   }


   /**
    * <pre>
    * Given an aircraft assembly
    * And an aircraft based on that assembly
    * And a fault against the aircraft
    * And a corrective task based on the fault
    * And the fault is assigned to an active work package
    * And the fault has an associated fault reference
    * And a job finish action for a labour row is performed for the task
    * When a technician attempts to sign off the work while another technician has changed the fault reference to another fault reference
    * Then an error message is thrown indicating the user to re-check the fault reference.
    *
    * </pre>
    */
   @Test
   public void
         itThrowsInconsistentFaultReferenceExceptionWhenFaultReferenceChangedDuringWorkCaptureThroughJobFinish()
               throws Exception {

      AssemblyKey aircraftAssembly = Domain.createAircraftAssembly();

      InventoryKey aircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( aircraftAssembly );
      } );

      TaskKey correctiveTask =
            Domain.createCorrectiveTask( aCorrTask -> aCorrTask.addLabour( aLabour -> {
               aLabour.setTechnicianRole( tech -> tech.setStatus( ACTV ) );
               aLabour.setStage( RefLabourStageKey.IN_WORK );
            } ) );

      FailDeferRefKey deferralReference = Domain.createDeferralReference( aDefRef -> {
         aDefRef.setAssemblyKey( aircraftAssembly );
         aDefRef.setFaultSeverityKey( RefFailureSeverityKey.MEL );
         aDefRef.setRequiredMocAuth( false );
      } );

      FailDeferRefKey deferralReferenceChangedWhileWorkCapture =
            Domain.createDeferralReference( aDefRef -> {
               aDefRef.setAssemblyKey( aircraftAssembly );
               aDefRef.setFaultSeverityKey( RefFailureSeverityKey.MEL );
               aDefRef.setRequiredMocAuth( false );
            } );

      Date faultFoundOnDate = new Date();

      Domain.createWorkPackage( aWp -> {
         aWp.addTask( correctiveTask );
         aWp.setStatus( RefEventStatusKey.IN_WORK );
      } );

      Domain.createFault( aFault -> {
         aFault.setCurrentDeferralReference( deferralReferenceChangedWhileWorkCapture );
         aFault.setCorrectiveTask( correctiveTask );
         aFault.setFoundOnDate( faultFoundOnDate );
         aFault.setInventory( aircraft );
         aFault.setNonCurrentDeferralReference( deferralReference );
      } );

      List<SchedLabourKey> labourList = Domain.readLabourRequirement( correctiveTask );
      if ( labourList.size() > 1 )
         fail( "Expected a row from the sched_labour table" );
      SchedLabourKey schedLabourKey = labourList.get( 0 );

      FinishJobTO lFinishJobTO = new FinishJobTO();
      SchedLabourTO lSchedLabourTO = new SchedLabourTO();
      lSchedLabourTO.setEndDate( faultFoundOnDate, "" );
      lFinishJobTO.setTechnician( iTechnician, "" );
      lFinishJobTO.setSchedLabourTO( lSchedLabourTO );
      FaultReferenceKey faultReferenceForDeferralReference =
            getFaultReferenceForDeferralReference( deferralReference );
      lFinishJobTO.setFaultReferenceKey( faultReferenceForDeferralReference );

      expectedException.expect( InconsistentFaultReferenceException.class );
      expectedException.expectMessage( i18n.get( "core.err.33915" ) );
      iService.captureWork( schedLabourKey, lFinishJobTO );

   }


   /**
    * <pre>
    * Given an aircraft assembly
    * And an aircraft based on that assembly
    * And a fault against the aircraft
    * And a corrective task based on the fault
    * And the fault is assigned to an active work package
    * And the fault has an associated fault reference
    * And a job finish action for a labour row is performed for the task
    * When a technician attempts to sign off the work while another technician has removed the fault reference
    * Then an error message is thrown indicating the user to re-check the fault reference.
    *
    * </pre>
    */
   @Test
   public void
         itThrowsInconsistentFaultReferenceExceptionWhenFaultReferenceRemovedDuringWorkCaptureThroughJobFinish()
               throws Exception {

      AssemblyKey aircraftAssembly = Domain.createAircraftAssembly();

      InventoryKey aircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( aircraftAssembly );
      } );

      TaskKey correctiveTask =
            Domain.createCorrectiveTask( aCorrTask -> aCorrTask.addLabour( aLabour -> {
               aLabour.setTechnicianRole( tech -> tech.setStatus( ACTV ) );
               aLabour.setStage( RefLabourStageKey.IN_WORK );
            } ) );

      FailDeferRefKey deferralReference = Domain.createDeferralReference( aDefRef -> {
         aDefRef.setAssemblyKey( aircraftAssembly );
         aDefRef.setFaultSeverityKey( RefFailureSeverityKey.MEL );
         aDefRef.setRequiredMocAuth( false );
      } );
      Date faultFoundOnDate = new Date();

      Domain.createWorkPackage( aWp -> {
         aWp.addTask( correctiveTask );
         aWp.setStatus( RefEventStatusKey.IN_WORK );
      } );

      Domain.createFault( aFault -> {
         aFault.setCorrectiveTask( correctiveTask );
         aFault.setFoundOnDate( faultFoundOnDate );
         aFault.setInventory( aircraft );
         aFault.setNonCurrentDeferralReference( deferralReference );
      } );

      List<SchedLabourKey> labourList = Domain.readLabourRequirement( correctiveTask );
      if ( labourList.size() > 1 )
         fail( "Expected a row from the sched_labour table" );
      SchedLabourKey schedLabourKey = labourList.get( 0 );

      FinishJobTO lFinishJobTO = new FinishJobTO();
      SchedLabourTO lSchedLabourTO = new SchedLabourTO();
      lSchedLabourTO.setEndDate( faultFoundOnDate, "" );
      lFinishJobTO.setTechnician( iTechnician, "" );
      lFinishJobTO.setSchedLabourTO( lSchedLabourTO );
      FaultReferenceKey faultReferenceForDeferralReference =
            getFaultReferenceForDeferralReference( deferralReference );
      lFinishJobTO.setFaultReferenceKey( faultReferenceForDeferralReference );

      expectedException.expect( InconsistentFaultReferenceException.class );
      expectedException.expectMessage( i18n.get( "core.err.33915" ) );
      iService.captureWork( schedLabourKey, lFinishJobTO );

   }


   /**
    * <pre>
    * Given an aircraft assembly
    * And an aircraft based on that assembly
    * And a fault against the aircraft
    * And a corrective task based on the fault
    * And the fault is assigned to an active work package
    * And a job finish action for a labour row is performed for the task
    * When a technician attempts to sign off the work while another technician adds an associated fault reference
    * Then an error message is thrown indicating the user to re-check the fault reference.
    *
    * </pre>
    */
   @Test
   public void
         itThrowsInconsistentFaultReferenceExceptionWhenFaultReferenceAddedDuringWorkCaptureThroughJobFinish()
               throws Exception {

      AssemblyKey aircraftAssembly = Domain.createAircraftAssembly();

      InventoryKey aircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( aircraftAssembly );
      } );

      TaskKey correctiveTask =
            Domain.createCorrectiveTask( aCorrTask -> aCorrTask.addLabour( aLabour -> {
               aLabour.setTechnicianRole( tech -> tech.setStatus( ACTV ) );
               aLabour.setStage( RefLabourStageKey.IN_WORK );
            } ) );

      FailDeferRefKey deferralReference = Domain.createDeferralReference( aDefRef -> {
         aDefRef.setAssemblyKey( aircraftAssembly );
         aDefRef.setFaultSeverityKey( RefFailureSeverityKey.MEL );
         aDefRef.setRequiredMocAuth( false );
      } );
      Date faultFoundOnDate = new Date();

      Domain.createWorkPackage( aWp -> {
         aWp.addTask( correctiveTask );
         aWp.setStatus( RefEventStatusKey.IN_WORK );
      } );

      Domain.createFault( aFault -> {
         aFault.setCorrectiveTask( correctiveTask );
         aFault.setFoundOnDate( faultFoundOnDate );
         aFault.setInventory( aircraft );
         aFault.setCurrentDeferralReference( deferralReference );
      } );

      List<SchedLabourKey> labourList = Domain.readLabourRequirement( correctiveTask );
      if ( labourList.size() > 1 )
         fail( "Expected a row from the sched_labour table" );
      SchedLabourKey schedLabourKey = labourList.get( 0 );

      FinishJobTO lFinishJobTO = new FinishJobTO();
      SchedLabourTO lSchedLabourTO = new SchedLabourTO();
      lSchedLabourTO.setEndDate( faultFoundOnDate, "" );
      lFinishJobTO.setTechnician( iTechnician, "" );
      lFinishJobTO.setSchedLabourTO( lSchedLabourTO );

      expectedException.expect( InconsistentFaultReferenceException.class );
      expectedException.expectMessage( i18n.get( "core.err.33915" ) );
      iService.captureWork( schedLabourKey, lFinishJobTO );

   }


   private JobStopTO getJobStopTO( List<LabourStepTO> LabourStepTOs ) throws MxException {
      JobStopTO lTO = new JobStopTO();
      lTO.setTechnician( iTechnician, "HR" );
      lTO.addLabourStepTOs( LabourStepTOs );
      lTO.setAutoCertifyWork( true );

      return lTO;
   }


   private LabourStepTO getLabourStepTO( SchedStepKey aSchedStep, RefLabourSkillKey aLabourSkill,
         StepStatus aStepStatus ) {

      UUID lStepSkillId = null;

      // if the step has no step skills
      if ( !( iStepSkillsMap == null || iStepSkillsMap.isEmpty() ) ) {
         String lStepOrder = iSchedStepOrdMap.get( aSchedStep ) + ":" + aLabourSkill.toString();
         lStepSkillId = iStepSkillsMap.get( lStepOrder );
      }

      LabourStepTO lLabourStepTO = new LabourStepTO();
      lLabourStepTO.setSchedStep( aSchedStep );
      lLabourStepTO.setNewNote( "Signoff step with skill: " + aLabourSkill );
      lLabourStepTO.setStepSkillId( lStepSkillId );
      lLabourStepTO.setLabourStepStatus( aStepStatus );
      lLabourStepTO.setSchedStepRevisionNo( iStepRevisionNo++ );

      return lLabourStepTO;
   }


   private Map<RefLabourSkillKey, SchedLabourKey> getLabourRequirements( TaskKey aTaskKey ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTaskKey, "sched_db_id", "sched_id" );

      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "SCHED_LABOUR", lArgs,
            new String[] { "labour_skill_db_id", "labour_skill_cd", "labour_db_id", "labour_id" } );

      Map<RefLabourSkillKey, SchedLabourKey> lLabourMap =
            new HashMap<RefLabourSkillKey, SchedLabourKey>();

      while ( lQs.next() ) {
         lLabourMap.put(
               lQs.getKey( RefLabourSkillKey.class, "labour_skill_db_id", "labour_skill_cd" ),
               lQs.getKey( SchedLabourKey.class, "labour_db_id", "labour_id" ) );
      }

      return lLabourMap;

   }


   private Map<SchedStepKey, Integer> getSteps( TaskKey aTask ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTask, "sched_db_id", "sched_id" );

      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "SCHED_STEP", lArgs,
            new String[] { "sched_db_id", "sched_id", "step_id", "step_ord" } );

      Map<SchedStepKey, Integer> lSchedSteps = new HashMap<SchedStepKey, Integer>();

      while ( lQs.next() ) {
         lSchedSteps.put( lQs.getKey( SchedStepKey.class, "sched_db_id", "sched_id", "step_id" ),
               lQs.getInteger( "step_ord" ) );
      }

      return lSchedSteps;
   }


   private Map<String, UUID> getStepSkills( TaskTaskKey aTaskTask ) {

      SQLStatement lStatement =
            new SQLStatement( "SELECT step_ord, labour_skill_db_id, labour_skill_cd, step_skill_id "
                  + "FROM task_step " + "INNER JOIN task_step_skill ON "
                  + "task_step.task_db_id = task_step_skill.task_db_id AND "
                  + "task_step.task_id = task_step_skill.task_id AND "
                  + "task_step.step_id = task_step_skill.step_id " + "WHERE task_step.task_db_id = "
                  + aTaskTask.getDbId() + " AND task_step.task_id = " + aTaskTask.getId() );

      DataSet lDs = MxDataAccess.getInstance().executeQuery( lStatement, null );

      Map<String, UUID> lStepSkills = new HashMap<String, UUID>();

      while ( lDs.next() ) {

         String lStepSkill = lDs.getString( "step_ord" ) + ":"
               + lDs.getKey( RefLabourSkillKey.class, "labour_skill_db_id", "labour_skill_cd" );

         lStepSkills.put( lStepSkill, lDs.getUuid( "step_skill_id" ) );
      }

      return lStepSkills;
   }


   private FaultReferenceKey
         getFaultReferenceForDeferralReference( FailDeferRefKey deferralReference ) {
      DataSetArgument args = new DataSetArgument();
      args.add( deferralReference, FaultReferenceDao.ColumnName.DEFER_REF_DB_ID.toString(),
            FaultReferenceDao.ColumnName.DEFER_REF_ID.toString() );
      QuerySet qs = QuerySetFactory.getInstance()
            .executeQueryTable( FaultReferenceTableRow.TABLE_NAME, args );
      assertTrue( "Expected a row of fault reference for the provided deferral reference",
            qs.next() );
      return qs.getKey( FaultReferenceKey.class,
            FaultReferenceDao.ColumnName.FAULT_REF_DB_ID.toString(),
            FaultReferenceDao.ColumnName.FAULT_REF_ID.toString() );
   }
}
