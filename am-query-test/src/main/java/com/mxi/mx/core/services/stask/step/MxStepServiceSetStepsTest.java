package com.mxi.mx.core.services.stask.step;

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
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.SchedStepKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.event.HistoricRecordException;
import com.mxi.mx.core.table.sched.SchedStepDao;
import com.mxi.mx.core.table.sched.SchedStepTableRow;


/**
 * Tests the editSteps method of the {@link MxStepsService}
 *
 */
public class MxStepServiceSetStepsTest {

   private static final String ALLOW_ADHOC_JOB_STEPS_ON_BASELINE_TASKS =
         "ALLOW_ADHOC_JOB_STEPS_ON_BASELINE_TASKS";

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private InventoryKey engineInventoryKey;
   private RandomStringGenerator descriptionGenerator;
   private GlobalParametersStub configParms = new GlobalParametersStub( "LOGIC" );

   private SchedStepDao schedStepDao;

   // subject under test
   private MxStepService stepService;


   @Before
   public void setUp() {

      stepService = new MxStepService();

      schedStepDao = InjectorContainer.get().getInstance( SchedStepDao.class );

      descriptionGenerator = new RandomStringGenerator.Builder().withinRange( 'a', 'z' ).build();

      configParms.setBoolean( ALLOW_ADHOC_JOB_STEPS_ON_BASELINE_TASKS, false );
      configParms.setBoolean( "SPEC2000_UPPERCASE_ASSMBL_CD", false );
      configParms.setBoolean( "SPEC2000_UPPERCASE_BOM_PART_CD", false );
      configParms.setBoolean( "SPEC2000_UPPERCASE_TASK_CD", false );
      GlobalParameters.setInstance( configParms );

      // create an engine for the tasks
      engineInventoryKey = Domain.createEngine( engine -> {
         engine.setParent( Domain.createAircraft() );
      } );

   }


   /**
    *
    * Test that an exception is thrown if we try to update the step description for a historical
    * adhoc task (which could be a fault)
    *
    * @throws MxException
    *
    */
   @Test( expected = HistoricRecordException.class )
   public void setSteps_historicRecord() throws MxException {

      // ASSEMBLE

      // create a historical adhoc task with a step
      TaskKey adhocTaskKey = Domain.createAdhocTask( task -> {
         task.setInventory( engineInventoryKey );
         task.addStep( "Adding a step to a historical task" );
         task.setIsCompleted( true );
      } );

      // ACT
      stepService.setSteps( adhocTaskKey, null, null );
   }


   /**
    *
    * Test that an exception is thrown if we try to update steps on a baseline task
    *
    * @throws MxException
    *
    */
   @Test( expected = TaskNotAdHocException.class )
   public void setSteps_baselineTask() throws MxException {

      // ASSEMBLE

      // create a baseline task
      TaskKey taskKey = Domain.createJobCard( task -> {
         task.setInventory( engineInventoryKey );
      } );

      // ACT
      stepService.setSteps( taskKey, null, null );
   }


   /**
    *
    * Test that an exception is thrown if we try to update a step to an empty description
    *
    * @throws MxException
    *
    */
   @Test( expected = MandatoryArgumentException.class )
   public void setSteps_emptyDescription() throws MxException {

      // ASSEMBLE

      // create an adhoc task
      TaskKey adhocTaskKey = Domain.createAdhocTask( task -> {
         task.setInventory( engineInventoryKey );
      } );

      SchedStepKey stepKey = stepService.addStep( adhocTaskKey, "Adhoc task step" );

      SchedStepKey stepKeys[] = { stepKey };
      String descriptions[] = { "" };

      // ACT
      stepService.setSteps( adhocTaskKey, stepKeys, descriptions );
   }


   /**
    *
    * Test that an exception is thrown if we try to add steps with a null description
    *
    * @throws MxException
    *
    */
   @Test( expected = MandatoryArgumentException.class )
   public void setSteps_nullDescription() throws MxException {

      // ASSEMBLE

      // create an adhoc task
      TaskKey adhocTaskKey = Domain.createAdhocTask( task -> {
         task.setInventory( engineInventoryKey );
      } );

      SchedStepKey stepKey = stepService.addStep( adhocTaskKey, "Adhoc task step" );

      SchedStepKey stepKeys[] = { stepKey };
      String descriptions[] = { null };

      // ACT
      stepService.setSteps( adhocTaskKey, stepKeys, descriptions );

   }


   /**
    *
    * Test that an exception is thrown if we try to update steps with a description over 4000
    * characters
    *
    * @throws MxException
    *
    */
   @Test( expected = StringTooLongException.class )
   public void setSteps_descriptionTooLong() throws MxException {

      // ASSEMBLE

      // create an adhoc task
      TaskKey adhocTaskKey = Domain.createAdhocTask( task -> {
         task.setInventory( engineInventoryKey );
      } );

      SchedStepKey stepKey = stepService.addStep( adhocTaskKey, "Adhoc task step" );

      SchedStepKey stepKeys[] = { stepKey };
      String descriptions[] = { descriptionGenerator.generate( 4001 ) };

      // ACT
      stepService.setSteps( adhocTaskKey, stepKeys, descriptions );
   }


   /**
    *
    * Test that the description for one step is updated
    *
    * @throws MxException
    *
    */
   @Test
   public void setSteps_updateOneStep() throws MxException {

      // ASSEMBLE

      // create an adhoc task
      TaskKey adhocTaskKey = Domain.createAdhocTask( task -> {
         task.setInventory( engineInventoryKey );
      } );

      String description1 = descriptionGenerator.generate( 50 );
      String description2 = descriptionGenerator.generate( 45 );

      SchedStepKey stepKey = stepService.addStep( adhocTaskKey, description1 );

      SchedStepKey stepKeys[] = { stepKey };
      String descriptions[] = { description2 };

      // ACT
      stepService.setSteps( adhocTaskKey, stepKeys, descriptions );

      // ASSERT
      SchedStepTableRow schedStepTableRow = schedStepDao.findByPrimaryKey( stepKey );
      assertTrue( schedStepTableRow.exists() );
      assertTrue( schedStepTableRow.getStepLdesc().equals( description2 ) );

   }


   /**
    *
    * Test that the step description can be set for a baseline task when the
    * ALLOW_ADHOC_JOB_STEPS_ON_BASELINE_TASKS configuration parameter is enabled.
    *
    * @throws MxException
    *
    */
   @Test
   public void setSteps_baselineTask_configParmEnabled() throws MxException {

      // ASSEMBLE
      configParms.setBoolean( ALLOW_ADHOC_JOB_STEPS_ON_BASELINE_TASKS, true );

      // create a baseline task
      TaskKey baselineTaskKey = Domain.createJobCard( task -> {
         task.setInventory( engineInventoryKey );
      } );

      String description = descriptionGenerator.generate( 50 );
      String description2 = descriptionGenerator.generate( 45 );

      SchedStepKey stepKey = stepService.addStep( baselineTaskKey, description );

      // ACT
      SchedStepKey stepKeys[] = { stepKey };
      String descriptions[] = { description2 };
      stepService.setSteps( baselineTaskKey, stepKeys, descriptions );

      // ASSERT
      SchedStepTableRow schedStepTableRow = schedStepDao.findByPrimaryKey( stepKey );
      assertTrue( schedStepTableRow.exists() );
      assertTrue( schedStepTableRow.getStepLdesc().equals( description2 ) );

   }


   /**
    *
    * Test that the descriptions for multiple steps are updated
    *
    * @throws MxException
    *
    */
   @Test
   public void setSteps_updateMultipleSteps() throws MxException {

      // ASSEMBLE

      // create an adhoc task
      TaskKey adhocTaskKey = Domain.createAdhocTask( task -> {
         task.setInventory( engineInventoryKey );
      } );

      String description1 = descriptionGenerator.generate( 50 );
      String description2 = descriptionGenerator.generate( 45 );
      String description3 = descriptionGenerator.generate( 101 );
      String description4 = descriptionGenerator.generate( 66 );

      SchedStepKey stepKey1 = stepService.addStep( adhocTaskKey, description1 );
      SchedStepKey stepKey2 = stepService.addStep( adhocTaskKey, description2 );

      SchedStepKey stepKeys[] = { stepKey1, stepKey2 };
      String descriptions[] = { description3, description4 };

      // ACT
      stepService.setSteps( adhocTaskKey, stepKeys, descriptions );

      // ASSERT

      SchedStepTableRow schedStepTableRow = schedStepDao.findByPrimaryKey( stepKey1 );
      assertTrue( schedStepTableRow.exists() );
      assertTrue( schedStepTableRow.getStepLdesc().equals( description3 ) );

      schedStepTableRow = schedStepDao.findByPrimaryKey( stepKey2 );
      assertTrue( schedStepTableRow.exists() );
      assertTrue( schedStepTableRow.getStepLdesc().equals( description4 ) );

   }


   /**
    *
    * Test that the descriptions for multiple steps are updated on an MPC task
    *
    * @throws MxException
    *
    */
   @Test
   public void setSteps_updateMultipleStepsOnMPCTask() throws MxException {

      // ASSEMBLE

      // create an mpc task
      TaskKey mpcTask = new TaskBuilder().withTaskClass( RefTaskClassKey.MPC ).build();

      String description1 = descriptionGenerator.generate( 50 );
      String description2 = descriptionGenerator.generate( 45 );
      String description3 = descriptionGenerator.generate( 101 );
      String description4 = descriptionGenerator.generate( 66 );

      SchedStepKey stepKey1 = stepService.addStep( mpcTask, description1 );
      SchedStepKey stepKey2 = stepService.addStep( mpcTask, description2 );

      SchedStepKey stepKeys[] = { stepKey1, stepKey2 };
      String descriptions[] = { description3, description4 };

      // ACT
      stepService.setSteps( mpcTask, stepKeys, descriptions );

      // ASSERT

      SchedStepTableRow schedStepTableRow = schedStepDao.findByPrimaryKey( stepKey1 );
      assertTrue( schedStepTableRow.exists() );
      assertTrue( schedStepTableRow.getStepLdesc().equals( description3 ) );

      schedStepTableRow = schedStepDao.findByPrimaryKey( stepKey2 );
      assertTrue( schedStepTableRow.exists() );
      assertTrue( schedStepTableRow.getStepLdesc().equals( description4 ) );

   }


   /**
    *
    * Test that an exception is thrown if the number of keys does not match the number of
    * descriptions
    *
    * @throws MxException
    *
    */
   @Test( expected = IllegalStateException.class )
   public void setSteps_invalidKeyMatch() throws MxException {

      // ASSEMBLE

      // create an adhoc task
      TaskKey adhocTaskKey = Domain.createAdhocTask( task -> {
         task.setInventory( engineInventoryKey );
      } );

      String description1 = descriptionGenerator.generate( 50 );
      String description2 = descriptionGenerator.generate( 45 );
      String description3 = descriptionGenerator.generate( 101 );
      String description4 = descriptionGenerator.generate( 66 );

      SchedStepKey stepKey1 = stepService.addStep( adhocTaskKey, description1 );
      SchedStepKey stepKey2 = stepService.addStep( adhocTaskKey, description2 );

      SchedStepKey stepKeys[] = { stepKey1, stepKey2 };
      String descriptions[] = { description2, description3, description4 };

      // ACT
      stepService.setSteps( adhocTaskKey, stepKeys, descriptions );
   }

}
