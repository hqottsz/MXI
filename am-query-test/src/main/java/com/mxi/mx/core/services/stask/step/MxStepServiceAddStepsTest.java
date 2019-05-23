package com.mxi.mx.core.services.stask.step;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.text.RandomStringGenerator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersStub;
import com.mxi.mx.common.exception.MandatoryArgumentException;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.exception.StringTooLongException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.SchedStepKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.services.event.HistoricRecordException;
import com.mxi.mx.core.table.sched.SchedStepDao;
import com.mxi.mx.core.table.sched.SchedStepTableRow;
import com.mxi.mx.core.task.model.StepStatus;


/**
 * Tests the addSteps method of the {@link MxStepService}
 *
 */
public class MxStepServiceAddStepsTest {

   private static final String ALLOW_ADHOC_JOB_STEPS_ON_BASELINE_TASKS =
         "ALLOW_ADHOC_JOB_STEPS_ON_BASELINE_TASKS";

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private SchedStepDao schedStepDao;

   private InventoryKey engineInventoryKey;
   private InventoryKey aircraftInventoryKey;
   private RandomStringGenerator generator;
   private GlobalParametersStub configParms = new GlobalParametersStub( "LOGIC" );

   // subject under test
   private MxStepService stepService;


   @Before
   public void setUp() {

      stepService = new MxStepService();
      schedStepDao = InjectorContainer.get().getInstance( SchedStepDao.class );

      configParms.setBoolean( ALLOW_ADHOC_JOB_STEPS_ON_BASELINE_TASKS, false );
      configParms.setBoolean( "SPEC2000_UPPERCASE_ASSMBL_CD", false );
      configParms.setBoolean( "SPEC2000_UPPERCASE_BOM_PART_CD", false );
      configParms.setBoolean( "SPEC2000_UPPERCASE_TASK_CD", false );
      GlobalParameters.setInstance( configParms );

      generator = new RandomStringGenerator.Builder().withinRange( 'a', 'z' ).build();

      // create aircraft and engine
      AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly( aircraftAssembly -> {
         aircraftAssembly.setRootConfigurationSlot( rootConfigSlot -> {
            rootConfigSlot.setCode( "ROOT" );
         } );
      } );

      // create an engine for the tasks
      engineInventoryKey = Domain.createEngine( engine -> {
         engine.setAssembly( Domain.createEngineAssembly() );
      } );

      aircraftInventoryKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.setDescription( "Aircraft" );
         aircraft.setLocation( Domain.createLocation() );
      } );

   }


   /**
    *
    * Test that an exception is thrown if we try to add steps to a historical adhoc task (which
    * could be a fault)
    *
    * @throws MxException
    *
    */
   @Test( expected = HistoricRecordException.class )
   public void addStep_historicRecord() throws MxException {

      // ASSEMBLE

      // create a historical adhoc task
      TaskKey adhocTaskKey = Domain.createAdhocTask( task -> {
         task.setInventory( engineInventoryKey );
         task.setIsCompleted( true );
      } );

      // ACT
      stepService.addStep( adhocTaskKey, "Adding an adhoc step to a historical task" );

   }


   /**
    *
    * Test that an exception is thrown if we try to add steps to a baseline task
    *
    * @throws MxException
    *
    */
   @Test( expected = TaskNotAdHocException.class )
   public void addStep_baselineTask() throws MxException {

      // ASSEMBLE

      // create a baseline task
      TaskKey taskKey = Domain.createJobCard( task -> {
         task.setInventory( engineInventoryKey );
      } );

      // ACT
      stepService.addStep( taskKey, "Adding step to a baseline task." );

   }


   /**
    *
    * Test that adhoc steps can be added to a baseline task when the
    * ALLOW_ADHOC_JOB_STEPS_ON_BASELINE_TASKS configuration parameter is enabled.
    *
    * @throws MxException
    *
    */
   @Test
   public void addStep_baselineTask_configParmEnabled() throws MxException {

      // ASSEMBLE
      configParms.setBoolean( ALLOW_ADHOC_JOB_STEPS_ON_BASELINE_TASKS, true );

      // create a baseline task
      TaskKey baselineTaskKey = Domain.createJobCard( task -> {
         task.setInventory( engineInventoryKey );
      } );

      String description = "Adding step to a baseline task.";

      // ACT
      stepService.addStep( baselineTaskKey, description );

      // ASSERT
      SchedStepKey schedStepKey =
            new SchedStepKey( baselineTaskKey.getDbId(), baselineTaskKey.getId(), 1 );
      SchedStepTableRow SchedStepTableRow = schedStepDao.findByPrimaryKey( schedStepKey );
      assertTrue( SchedStepTableRow.exists() );
      assertEquals( SchedStepTableRow.getStepLdesc(), description );
      assertEquals( SchedStepTableRow.getStepOrd(), Integer.valueOf( 1 ) );
      assertEquals( SchedStepTableRow.getStepStatus(), StepStatus.MXPENDING );

   }


   /**
    *
    * Test that an exception is thrown if we try to add steps with an empty description
    *
    * @throws MxException
    *
    */
   @Test( expected = MandatoryArgumentException.class )
   public void addStep_emptyDescription() throws MxException {

      // ASSEMBLE

      // create an adhoc task
      TaskKey adhocTaskKey = Domain.createAdhocTask( task -> {
         task.setInventory( engineInventoryKey );

      } );

      // ACT
      stepService.addStep( adhocTaskKey, "" );

   }


   /**
    *
    * Test that an exception is thrown if we try to add steps with a null description
    *
    * @throws MxException
    *
    */
   @Test( expected = MandatoryArgumentException.class )
   public void addStep_nullDescription() throws MxException {

      // ASSEMBLE

      // create an adhoc task
      TaskKey adhocTaskKey = Domain.createAdhocTask( task -> {
         task.setInventory( engineInventoryKey );
      } );

      // ACT
      stepService.addStep( adhocTaskKey, null );
   }


   /**
    *
    * Test that an exception is thrown if we try to add steps with a description over 4000
    * characters
    *
    * @throws MxException
    *
    */
   @Test( expected = StringTooLongException.class )
   public void addStep_descriptionTooLong() throws MxException {

      // ASSEMBLE

      // create an adhoc task
      TaskKey adhocTaskKey = Domain.createAdhocTask( task -> {
         task.setInventory( engineInventoryKey );
      } );

      // ACT
      stepService.addStep( adhocTaskKey, generator.generate( 4001 ) );

   }


   /**
    *
    * Test that one step is correctly added to an adhoc task
    *
    * @throws MxException
    *
    */
   @Test
   public void addStep_addOneStepToAdhocTask() throws MxException {

      // ASSEMBLE

      // create an adhoc task
      TaskKey adhocTaskKey = Domain.createAdhocTask( task -> {
         task.setInventory( engineInventoryKey );

      } );

      String description = generator.generate( 100 );

      // ACT
      stepService.addStep( adhocTaskKey, description );

      // ASSERT
      SchedStepKey schedStepKey =
            new SchedStepKey( adhocTaskKey.getDbId(), adhocTaskKey.getId(), 1 );
      SchedStepTableRow SchedStepTableRow = schedStepDao.findByPrimaryKey( schedStepKey );
      assertTrue( SchedStepTableRow.exists() );
      assertEquals( SchedStepTableRow.getStepLdesc(), description );
      assertEquals( SchedStepTableRow.getStepOrd(), Integer.valueOf( 1 ) );
      assertEquals( SchedStepTableRow.getStepStatus(), StepStatus.MXPENDING );
   }


   /**
    *
    * Test that multiple steps are correctly added to an adhoc task
    *
    * @throws MxException
    *
    */
   @Test
   public void addStep_addMultipleStepsToAdhocTask() throws MxException {

      // ASSEMBLE

      // create an adhoc task
      TaskKey adhocTaskKey = Domain.createAdhocTask( task -> {
         task.setInventory( engineInventoryKey );
      } );

      String description1 = generator.generate( 100 );
      String description2 = generator.generate( 150 );

      // ACT

      stepService.addStep( adhocTaskKey, description1 );
      stepService.addStep( adhocTaskKey, description2 );

      // ASSERT

      SchedStepKey schedStepKey =
            new SchedStepKey( adhocTaskKey.getDbId(), adhocTaskKey.getId(), 1 );

      SchedStepTableRow SchedStepTableRow = schedStepDao.findByPrimaryKey( schedStepKey );

      assertTrue( SchedStepTableRow.exists() );
      assertEquals( SchedStepTableRow.getStepLdesc(), description1 );
      assertEquals( SchedStepTableRow.getStepOrd(), Integer.valueOf( 1 ) );
      assertEquals( SchedStepTableRow.getStepStatus(), StepStatus.MXPENDING );

      schedStepKey = new SchedStepKey( adhocTaskKey.getDbId(), adhocTaskKey.getId(), 2 );
      SchedStepTableRow = schedStepDao.findByPrimaryKey( schedStepKey );

      assertTrue( SchedStepTableRow.exists() );
      assertEquals( SchedStepTableRow.getStepLdesc(), description2 );
      assertEquals( SchedStepTableRow.getStepOrd(), Integer.valueOf( 2 ) );
      assertEquals( SchedStepTableRow.getStepStatus(), StepStatus.MXPENDING );
   }


   /**
    *
    * Test that multiple steps are correctly added to an mpc task
    *
    * @throws MxException
    *
    */
   @Test
   public void addStep_addMultipleStepsToMPCTask() throws MxException {

      // ASSEMBLE

      // create an mpc task
      TaskKey mpcTask = new TaskBuilder().withTaskClass( RefTaskClassKey.MPC ).build();

      String description1 = generator.generate( 100 );
      String description2 = generator.generate( 150 );

      // ACT
      stepService.addStep( mpcTask, description1 );
      stepService.addStep( mpcTask, description2 );

      // ASSERT
      SchedStepKey schedStepKey = new SchedStepKey( mpcTask.getDbId(), mpcTask.getId(), 1 );
      SchedStepTableRow SchedStepTableRow = schedStepDao.findByPrimaryKey( schedStepKey );

      assertTrue( SchedStepTableRow.exists() );
      assertEquals( SchedStepTableRow.getStepLdesc(), description1 );
      assertEquals( SchedStepTableRow.getStepOrd(), Integer.valueOf( 1 ) );
      assertEquals( SchedStepTableRow.getStepStatus(), StepStatus.MXPENDING );

      schedStepKey = new SchedStepKey( mpcTask.getDbId(), mpcTask.getId(), 2 );
      SchedStepTableRow = schedStepDao.findByPrimaryKey( schedStepKey );

      assertTrue( SchedStepTableRow.exists() );
      assertEquals( SchedStepTableRow.getStepLdesc(), description2 );
      assertEquals( SchedStepTableRow.getStepOrd(), Integer.valueOf( 2 ) );
      assertEquals( SchedStepTableRow.getStepStatus(), StepStatus.MXPENDING );
   }


   /**
    *
    * Test that adhoc steps are correctly added to a fault that also has repair reference steps
    * (baseline steps)
    *
    * @throws MxException
    *
    */
   @Test
   public void addStep_addStepsToFaultWithReference() throws MxException {

      // ASSEMBLE

      // create a repair reference with steps
      TaskTaskKey repairReferenceKey = Domain.createRequirementDefinition( reference -> {
         reference.setTaskClass( RefTaskClassKey.REPREF );
         reference.setTaskName( "Repair Reference" );
         reference.setCode( "REPREF1" );
         reference.setStatus( RefTaskDefinitionStatusKey.ACTV );
         reference.addStep( step -> {
            step.setDescription( "Repair Step 1" );
         } );
         reference.addStep( step -> {
            step.setDescription( "Repair Step 2" );
         } );
      } );

      // create a fault with the repair reference
      TaskKey correctiveTaskKey = Domain.createCorrectiveTask( task -> {
         task.setInventory( aircraftInventoryKey );
      } );

      Domain.createFault( fault -> {
         fault.setCorrectiveTask( correctiveTaskKey );
         fault.setCurrentRepairReference( repairReferenceKey );
      } );

      String description1 = generator.generate( 130 );

      // ACT
      stepService.addStep( correctiveTaskKey, description1 );

      // ASSERT

      // the adhoc step should be the third step
      SchedStepKey schedStepKey =
            new SchedStepKey( correctiveTaskKey.getDbId(), correctiveTaskKey.getId(), 3 );
      SchedStepTableRow schedStepTableRow = schedStepDao.findByPrimaryKey( schedStepKey );

      assertTrue( schedStepTableRow.exists() );
      assertEquals( Integer.valueOf( 3 ), schedStepTableRow.getStepOrd() );
      assertEquals( StepStatus.MXPENDING, schedStepTableRow.getStepStatus() );

      // this step is an adhoc step, the description should be filled in and the task step id
      // should be null
      assertEquals( description1, schedStepTableRow.getStepLdesc() );
      assertNull( schedStepTableRow.getTaskStepKey() );

   }

}
