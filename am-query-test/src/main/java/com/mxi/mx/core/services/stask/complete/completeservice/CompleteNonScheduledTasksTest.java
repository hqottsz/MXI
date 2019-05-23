
package com.mxi.mx.core.services.stask.complete.completeservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.domain.WorkPackage;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.RefTaskDepActionKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.services.stask.complete.CompleteService;
import com.mxi.mx.core.services.stask.labour.LabourUtils;
import com.mxi.mx.core.table.evt.EvtEventTable;


@RunWith( BlockJUnit4ClassRunner.class )
public class CompleteNonScheduledTasksTest {

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private InventoryKey iAircraft;
   private HumanResourceKey iHr;
   private TaskKey iWorkPackage;

   private static final ConfigSlotKey BOM_ITEM_KEY = new ConfigSlotKey( "4650:CNSW:0" );


   @Before
   public void setUp() {
      iAircraft = Domain.createAircraft();
      iHr = new HumanResourceDomainBuilder().build();
      UserParameters.setInstance( iHr.getId(), "LOGIC",
            new UserParametersFake( iHr.getId(), "LOGIC" ) );
      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( iHr ) );
      iWorkPackage = createWorkPackage();
   }


   @Test
   public void completeAdHocTaskWithLabourRow() throws Throwable {

      // ADHOC task with a labour row
      TaskKey lAdHocTask =
            createTaskWithLabourRowInWorkPackage( iWorkPackage, RefTaskClassKey.ADHOC );

      new CompleteService( lAdHocTask ).completeBatch( iHr, new Date(), false );

      // Make sure tasks status are complete
      assertComplete( lAdHocTask );

      // Verifies the labour_stage_cd is set to complete
      assertTrue( LabourUtils.isLabourComplete( lAdHocTask ) );

   }


   @Test
   public void completeTaskBasedOnTaskDefinition() throws Throwable {

      // Task definitions
      TaskTaskKey lOppTaskDef = createRequirementDefinition( BOM_ITEM_KEY, "OPPTASK",
            RefTaskDefinitionStatusKey.ACTV, null );
      TaskTaskKey lTaskDef = createRequirementDefinition( BOM_ITEM_KEY, "TASK",
            RefTaskDefinitionStatusKey.ACTV, lOppTaskDef );

      TaskKey lTask = createTaskFromReqInWorkPackage( iWorkPackage, RefTaskClassKey.REQ, lTaskDef );

      new CompleteService( lTask ).completeBatch( iHr, new Date(), false );

      // Make sure tasks status are complete
      assertComplete( lTask );

      // Verifies the labour_stage_cd is set to complete
      assertTrue( LabourUtils.isLabourComplete( lTask ) );

   }


   @Test
   public void completeOpportunisticTask() throws Throwable {

      // Task definition
      TaskTaskKey lOppTaskDef = createRequirementDefinition( BOM_ITEM_KEY, "OPPTASK",
            RefTaskDefinitionStatusKey.ACTV, null );

      // Opportunistic Task
      TaskKey lOppTask =
            createTaskFromReqInWorkPackage( iWorkPackage, RefTaskClassKey.REQ, lOppTaskDef );

      new CompleteService( lOppTask ).completeBatch( iHr, new Date(), false );

      // Make sure task status is complete
      assertComplete( lOppTask );

      // Verifies the labour_stage_cd is set to complete
      assertTrue( LabourUtils.isLabourComplete( lOppTask ) );

   }


   private void assertComplete( TaskKey aTask ) {
      assertEquals( RefEventStatusKey.COMPLETE,
            EvtEventTable.findByPrimaryKey( new EventKey( aTask.toString() ) ).getEventStatus() );
   }


   private TaskKey createWorkPackage() {
      return Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aBuilder ) {
            aBuilder.setStatus( RefEventStatusKey.IN_WORK );
            aBuilder.setAircraft( iAircraft );
         }
      } );
   }


   private TaskKey createTaskWithLabourRowInWorkPackage( TaskKey aWorkPackage,
         RefTaskClassKey aTaskClass ) {
      return new TaskBuilder().withParentTask( aWorkPackage ).withHighestTask( aWorkPackage )
            .withTaskClass( aTaskClass ).withStatus( RefEventStatusKey.ACTV )
            .withLabour( RefLabourSkillKey.LBR, 10 ).onInventory( new InventoryBuilder()
                  .withClass( RefInvClassKey.TRK ).withParentInventory( iAircraft ).build() )
            .build();
   }


   private TaskKey createTaskFromReqInWorkPackage( TaskKey aWorkPackage, RefTaskClassKey aTaskClass,
         TaskTaskKey aTaskRevision ) {

      return new TaskBuilder().withParentTask( aWorkPackage ).withHighestTask( aWorkPackage )
            .withTaskClass( aTaskClass ).withStatus( RefEventStatusKey.ACTV )
            .withTaskRevision( aTaskRevision ).withLabour( RefLabourSkillKey.LBR, 10 )
            .onInventory( new InventoryBuilder().withClass( RefInvClassKey.TRK )
                  .withParentInventory( iAircraft ).build() )
            .build();

   }


   private TaskTaskKey createRequirementDefinition( final ConfigSlotKey aConfigSlotKey,
         final String aCode, final RefTaskDefinitionStatusKey aStatus,
         final TaskTaskKey aLinkToTaskDef ) {

      TaskTaskKey lTaskDefKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aTaskDefinition ) {
                  aTaskDefinition.againstConfigurationSlot( aConfigSlotKey );
                  aTaskDefinition.setCode( aCode );
                  aTaskDefinition.setStatus( aStatus );
                  if ( aLinkToTaskDef != null ) {
                     aTaskDefinition.addLinkedTaskDefinition( RefTaskDepActionKey.OPPRTNSTC,
                           aLinkToTaskDef );
                  }
               }
            } );
      return lTaskDefKey;

   }

}
