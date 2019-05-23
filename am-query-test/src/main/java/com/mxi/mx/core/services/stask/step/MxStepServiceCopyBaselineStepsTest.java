package com.mxi.mx.core.services.stask.step;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.SchedStepKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.sched.SchedStepDao;
import com.mxi.mx.core.table.sched.SchedStepTableRow;
import com.mxi.mx.core.task.model.StepStatus;


public class MxStepServiceCopyBaselineStepsTest {

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private SchedStepDao schedStepDao;

   private InventoryKey aircraftInventoryKey;

   // subject under test
   private MxStepService stepService;


   @Before
   public void setUp() {

      stepService = new MxStepService();
      schedStepDao = InjectorContainer.get().getInstance( SchedStepDao.class );

      // create aircraft and engine
      AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly( aircraftAssembly -> {
         aircraftAssembly.setRootConfigurationSlot( rootConfigSlot -> {
            rootConfigSlot.setCode( "ROOT" );
         } );
      } );

      aircraftInventoryKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.setDescription( "Aircraft" );
         aircraft.setLocation( Domain.createLocation() );
         aircraft.setApplicabilityCode( "100" );
      } );

   }


   /**
    *
    * Test that an exception is thrown if no task definition key is provided.
    *
    * @throws MxException
    *
    */
   @Test( expected = MxException.class )
   public void copyBaselineSteps_noTaskDefnKey() throws MxException {

      // ASSEMBLE

      TaskKey adhocTaskKey = Domain.createAdhocTask();

      // ACT
      try {
         stepService.copyBaselineSteps( adhocTaskKey, null );
         fail( "MxException was not thrown." );
      } catch ( MxException e ) {
         assertEquals( e.getMessage(),
               "[MXERR-10000] The 'TaskTaskKey' is a mandatory field.<br><br>Please enter a value for the 'TaskTaskKey'." );
         throw e;
      }

   }


   /**
    *
    * Test that an exception is thrown if no task key is provided.
    *
    * @throws MxException
    *
    */
   @Test( expected = MxException.class )
   public void copyBaselineSteps_noTaskKey() throws MxException {

      // ACT
      try {
         stepService.copyBaselineSteps( null, null );
         fail( "MxException was not thrown." );
      } catch ( MxException e ) {
         assertEquals( e.getMessage(),
               "[MXERR-10000] The 'TaskKey' is a mandatory field.<br><br>Please enter a value for the 'TaskKey'." );
         throw e;
      }

   }


   /**
    *
    * Test that steps are copied and applicability is set. Note, the descriptions of baseline steps
    * are not copied to the sched_step table.
    *
    * @throws MxException
    *
    */
   @Test
   public void copyBaselineSteps_happyPath() throws MxException {

      // ASSEMBLE

      // create a corrective task
      TaskKey correctiveTask = Domain.createCorrectiveTask( task -> {
         task.setInventory( aircraftInventoryKey );

      } );

      TaskTaskKey taskDefinitionKey = Domain.createRequirementDefinition( req -> {
         req.setTaskClass( RefTaskClassKey.REPREF );
         req.addStep( step -> {
            step.setDescription( "Here's one" );
            step.setApplicabilityRange( "100" );
         } );
         req.addStep( step -> {
            step.setDescription( "Here's two" );
            step.setApplicabilityRange( "200" );
         } );
      } );

      // ACT
      stepService.copyBaselineSteps( correctiveTask, taskDefinitionKey );

      // ASSERT
      SchedStepKey schedStepKey =
            new SchedStepKey( correctiveTask.getDbId(), correctiveTask.getId(), 1 );
      SchedStepTableRow schedStepTableRow = schedStepDao.findByPrimaryKey( schedStepKey );

      assertTrue( schedStepTableRow.exists() );
      assertEquals( null, schedStepTableRow.getStepLdesc() );
      assertEquals( Integer.valueOf( 1 ), schedStepTableRow.getStepOrd() );
      assertEquals( StepStatus.MXPENDING, schedStepTableRow.getStepStatus() );

      schedStepKey = new SchedStepKey( correctiveTask.getDbId(), correctiveTask.getId(), 2 );
      schedStepTableRow = schedStepDao.findByPrimaryKey( schedStepKey );

      assertEquals( null, schedStepTableRow.getStepLdesc() );
      assertEquals( Integer.valueOf( 2 ), schedStepTableRow.getStepOrd() );
      // step 2 was not applicable and should be marked NA
      assertEquals( StepStatus.MXNA, schedStepTableRow.getStepStatus() );
   }

}
