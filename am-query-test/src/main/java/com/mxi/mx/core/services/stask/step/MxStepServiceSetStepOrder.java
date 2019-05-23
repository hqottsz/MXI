package com.mxi.mx.core.services.stask.step;

import static org.junit.Assert.assertTrue;

import org.apache.commons.text.RandomStringGenerator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersStub;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.SchedStepKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.event.HistoricRecordException;
import com.mxi.mx.core.table.sched.SchedStepDao;
import com.mxi.mx.core.table.sched.SchedStepTableRow;


/**
 * Tests the setStepOrder method of the {@link MxStepService}
 *
 */
public class MxStepServiceSetStepOrder {

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
   private RandomStringGenerator descriptionGenerator;
   private GlobalParametersStub configParms = new GlobalParametersStub( "LOGIC" );

   private static final int ONE = 1;
   private static final int TWO = 2;
   private static final int THREE = 3;

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

      descriptionGenerator = new RandomStringGenerator.Builder().withinRange( 'a', 'z' ).build();

      // create an engine for the tasks
      engineInventoryKey = Domain.createEngine( engine -> {
         engine.setParent( Domain.createAircraft() );
      } );

   }


   /**
    *
    * Test that an exception is thrown if we try to update the step order for a historical adhoc
    * task (which could be a fault)
    *
    * @throws MxException
    *
    */
   @Test( expected = HistoricRecordException.class )
   public void setStepOrder_historicRecord() throws MxException {

      // ASSEMBLE

      // create a historical adhoc task with a step
      TaskKey adhocTaskKey = Domain.createAdhocTask( task -> {
         task.setInventory( engineInventoryKey );
         task.addStep( "Adding a step to a historical task" );
         task.setIsCompleted( true );
      } );

      // ACT
      stepService.setStepOrder( adhocTaskKey, null );
   }


   /**
    *
    * Test that an exception is thrown if we try to update step order on a baseline task
    *
    * @throws MxException
    *
    */
   @Test( expected = TaskNotAdHocException.class )
   public void setStepOrder_baselineTask() throws MxException {

      // ASSEMBLE

      // create a baseline task
      TaskKey taskKey = Domain.createJobCard( task -> {
         task.setInventory( engineInventoryKey );
      } );

      // ACT
      stepService.setStepOrder( taskKey, null );
   }


   /**
    *
    * Test that the order for multiple steps are updated
    *
    * @throws MxException
    *
    */
   @Test
   public void setStepOrder_updateStepOrder() throws MxException {

      // ASSEMBLE

      // create an adhoc task
      TaskKey adhocTaskKey = Domain.createAdhocTask( task -> {
         task.setInventory( engineInventoryKey );
      } );

      String description1 = descriptionGenerator.generate( 50 );
      String description2 = descriptionGenerator.generate( 45 );
      String description3 = descriptionGenerator.generate( 101 );

      SchedStepKey stepKey1 = stepService.addStep( adhocTaskKey, description1 );
      SchedStepKey stepKey2 = stepService.addStep( adhocTaskKey, description2 );
      SchedStepKey stepKey3 = stepService.addStep( adhocTaskKey, description3 );

      // ACT
      // reverse the order
      SchedStepKey stepKeys[] = { stepKey3, stepKey2, stepKey1 };
      stepService.setStepOrder( adhocTaskKey, stepKeys );

      // ASSERT
      assertStep( stepKey1, description1, THREE );
      assertStep( stepKey2, description2, TWO );
      assertStep( stepKey3, description3, ONE );

   }


   /**
    *
    * Test that the order can be set for a baseline task when the
    * ALLOW_ADHOC_JOB_STEPS_ON_BASELINE_TASKS configuration parameter is enabled.
    *
    * @throws MxException
    *
    */
   @Test
   public void setStepOrder_updateStepOrder_baselineTask_configParmEnabled() throws MxException {

      // ASSEMBLE
      configParms.setBoolean( ALLOW_ADHOC_JOB_STEPS_ON_BASELINE_TASKS, true );

      // create a baseline task
      TaskKey baselineTaskKey = Domain.createJobCard( task -> {
         task.setInventory( engineInventoryKey );
      } );

      String description1 = descriptionGenerator.generate( 40 );
      String description2 = descriptionGenerator.generate( 50 );
      SchedStepKey stepKey1 = stepService.addStep( baselineTaskKey, description1 );
      SchedStepKey stepKey2 = stepService.addStep( baselineTaskKey, description2 );

      // ACT
      SchedStepKey stepKeys[] = { stepKey2, stepKey1 };
      stepService.setStepOrder( baselineTaskKey, stepKeys );

      // ASSERT
      assertStep( stepKey1, description1, TWO );
      assertStep( stepKey2, description2, ONE );

   }


   /**
    * Asserts the step exists, the description and the order of the step. Used for assertions above.
    *
    */
   private void assertStep( SchedStepKey stepKey, String description, int expectedOrder ) {
      SchedStepTableRow schedStepTableRow = schedStepDao.findByPrimaryKey( stepKey );
      assertTrue( schedStepTableRow.exists() );
      assertTrue( schedStepTableRow.getStepOrd() == expectedOrder );
      assertTrue( schedStepTableRow.getStepLdesc().equals( description ) );
   }

}
