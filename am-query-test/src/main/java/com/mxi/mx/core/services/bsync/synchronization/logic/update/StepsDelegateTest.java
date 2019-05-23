package com.mxi.mx.core.services.bsync.synchronization.logic.update;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.SchedStepKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.services.bsync.synchronization.model.update.StepsDetails;
import com.mxi.mx.core.services.bsync.synchronization.model.update.StepsTO;


public class StepsDelegateTest {

   private static final HumanResourceKey HUMAN_RESOURCE_KEY = new HumanResourceKey( 4650, 1 );
   private static final Integer REVISION_1 = 1;
   private static final Integer REVISION_2 = 2;

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();

   private StepsDelegate stepsDelegate;


   @Before
   public void setUp() {
      stepsDelegate = new StepsDelegate();
   }


   @Test
   public void bsync_doesNotInsertSchedStepShortDescriptionForBaselineTask() throws Exception {
      final PartNoKey aircraftPart = Domain.createPart();
      final AssemblyKey aircraftAssembly = createAircraftAssembly( aircraftPart );

      final InventoryKey aircraftForTask = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssembly );
         aircraft.setPart( aircraftPart );
         aircraft.allowSynchronization();
      } );

      final TaskTaskKey jobCardDefinition = Domain.createJobCardDefinition( jobCardDef -> {

         jobCardDef.setConfigurationSlot( Domain.readRootConfigurationSlot( aircraftAssembly ) );
         jobCardDef.setRevisionNumber( REVISION_1 );
         jobCardDef.setStatus( RefTaskDefinitionStatusKey.ACTV );
         jobCardDef.addStep( step -> {
            step.setDescription( "Description" );
         } );
      } );

      final TaskKey taskBasedFromTaskDefinition = Domain.createRequirement( requirement -> {
         requirement.setDefinition( jobCardDefinition );
         requirement.setInventory( aircraftForTask );
         requirement.setStatus( RefEventStatusKey.ACTV );
      } );

      final TaskTaskKey jobCardDefinitionRevision = Domain.createJobCardDefinition( jobCardDef -> {

         jobCardDef.setPreviousRevision( jobCardDefinition );
         jobCardDef.setRevisionNumber( REVISION_2 );
         jobCardDef.setConfigurationSlot( Domain.readRootConfigurationSlot( aircraftAssembly ) );
         jobCardDef.setStatus( RefTaskDefinitionStatusKey.ACTV );
         jobCardDef.addStep( step -> {
            step.setDescription( "Description updated" );
         } );
      } );

      StepsDetails stepDetails =
            new StepsTO( taskBasedFromTaskDefinition, jobCardDefinitionRevision );
      stepsDelegate.synchronize( stepDetails, HUMAN_RESOURCE_KEY );

      SchedStepKey schedStepKey = new SchedStepKey( taskBasedFromTaskDefinition, 2 );

      DataSetArgument args = new DataSetArgument();
      args.add( schedStepKey, "sched_db_id", "sched_id", "step_id" );

      QuerySet querySet = QuerySetFactory.getInstance().executeQuery( new String[] { "step_ldesc" },
            "sched_step", args );

      assertTrue( querySet.next() );
      assertTrue( querySet.isNull( "step_ldesc" ) );

   }


   private AssemblyKey createAircraftAssembly( final PartNoKey aircraftPart ) {
      return Domain.createAircraftAssembly( aircraftAssembly -> {
         aircraftAssembly.setRootConfigurationSlot( configurationSlot -> {
            configurationSlot.addPartGroup( partGroup -> {
               partGroup.setInventoryClass( RefInvClassKey.ACFT );
               partGroup.addPart( aircraftPart );
            } );
         } );
      } );
   }

}
