package com.mxi.mx.core.services.stask.complete.completeservice;

import static com.mxi.mx.core.key.RefInvClassKey.ACFT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefRelationTypeKey;
import com.mxi.mx.core.key.RefReschedFromKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDepActionKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.services.stask.complete.CompleteService;
import com.mxi.mx.core.table.eqp.EqpAssmblPos;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtEventRel;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.JdbcEvtEventDao;
import com.mxi.mx.core.table.org.OrgHr;


public class CompleteNonRootTaskTest {

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private static final String LOGIC = "LOGIC";
   public static final String SYSTEM_NAME = "SYSTEM_NAME";
   private static final String ACFT_SYS_CONFIG_SLOT_CD = "ASYS_CD";
   private static final String ACFT_TRK_CONFIG_SLOT_CD = "ATRK_CD";
   private static final String ENG_SYS_CONFIG_SLOT_CD = "ESYS_CD";
   private static final String ENG_TRK_CONFIG_SLOT_CD = "ETRK_CD";
   private static final String ACFT_SUB_ASSY_CONFIG_SLOT_CD = "ASA_CD";

   private static final Boolean NO_DAMAGE_RECORD = false;
   private static final Boolean HAS_DAMAGE_RECORD = true;

   private static final String FOLLOW_ON_TASK = "FOLLOW_ON_TASK";

   private static final String DAMAGE_LOCATION = "DAMAGE_LOCATION";

   private EvtEventDao evtEventDao = new JdbcEvtEventDao();
   private int iUserId;

   private HumanResourceKey iAuthorizingHr;


   @After
   public void after() {
      UserParameters.setInstance( iUserId, LOGIC, null );
      GlobalParameters.setInstance( LOGIC, null );
   }


   /**
    * Description: Test the completeNonRootTask methods ability to complete a Fault, Un-Assign it
    * from a work package, and cancel the workpackage
    *
    */
   @Test
   public void
         itAutoCompletesAFaultAndUnassignsItFromTheWorkPackageThenCancelsTheNowEmptyWorkPackage()
               throws Exception {

      this.autoCompletesAFaultAndUnassignsItFromTheWorkPackageDataSetup();

      PartNoKey lTrkPartNo = new PartNoBuilder().withInventoryClass( RefInvClassKey.TRK ).build();

      // Create the TRK component awaiting inspection.
      InventoryKey lInventory = new InventoryBuilder().withClass( RefInvClassKey.TRK )
            .withPartNo( lTrkPartNo ).withCondition( RefInvCondKey.INSPREQ ).build();

      TaskKey lFault = new TaskBuilder().withTaskClass( RefTaskClassKey.CORR )
            .withFault( Domain.createFault() ).build();

      TaskKey lComponentWP = Domain.createComponentWorkPackage( aBuilder -> {

         aBuilder.setStatus( RefEventStatusKey.ACTV );
         aBuilder.assignTask( lFault );
         aBuilder.setInventory( lInventory );

      } );

      CompleteService.completeNonRootTask( lFault, iAuthorizingHr, new Date() );

      EvtEventDao sEvtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );

      EvtEventTable lFaultEvent = sEvtEventDao.findByPrimaryKey( lFault );
      EvtEventTable lComponentEvent = sEvtEventDao.findByPrimaryKey( lComponentWP );
      // Assert the Task is complete
      assertEquals( "Fault was not completed when it was expected to be",
            RefEventStatusKey.COMPLETE.getCd(), lFaultEvent.getEventStatusCd() );

      // Assert the Task was unassigned from the WP (its highest event is itself)
      assertEquals( "Fault was not unassigned from the WP when it was expected to be.",
            lFaultEvent.getPk().getId(), lFaultEvent.getHEvent().getId() );

      // Assert the WP was cancelled.
      assertEquals( "Component work package was not cancelled when it was expected to be.",
            RefEventStatusKey.CANCEL.getCd(), lComponentEvent.getEventStatusCd() );

   }


   /**
    * Description: Test the completeNonRootTask methods ability to complete a Fault, Un-Assign it
    * from a work package, and leaves the Work Package as ACTV due to another task still existing
    * within the Workpackage.
    *
    */
   @Test
   public void itAutoCompletesAFaultAndUnassignsItFromTheWorkPackageThenDoesNotCancelTheWP()
         throws Exception {

      this.autoCompletesAFaultAndUnassignsItFromTheWorkPackageDataSetup();

      PartNoKey lTrkPartNo = new PartNoBuilder().withInventoryClass( RefInvClassKey.TRK ).build();

      // Create the TRK component awaiting inspection.
      InventoryKey lInventory = new InventoryBuilder().withClass( RefInvClassKey.TRK )
            .withPartNo( lTrkPartNo ).withCondition( RefInvCondKey.INSPREQ ).build();

      TaskKey lFault = new TaskBuilder().withTaskClass( RefTaskClassKey.CORR )
            .withFault( Domain.createFault() ).build();

      TaskKey lTask = new TaskBuilder().withTaskClass( RefTaskClassKey.ADHOC ).build();

      TaskKey lComponentWP = Domain.createComponentWorkPackage( aBuilder -> {

         aBuilder.setStatus( RefEventStatusKey.ACTV );
         aBuilder.assignTask( lFault );
         aBuilder.assignTask( lTask );
         aBuilder.setInventory( lInventory );

      } );

      CompleteService.completeNonRootTask( lFault, iAuthorizingHr, new Date() );

      EvtEventDao sEvtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );

      EvtEventTable lFaultEvent = sEvtEventDao.findByPrimaryKey( lFault );
      EvtEventTable lComponentEvent = sEvtEventDao.findByPrimaryKey( lComponentWP );
      // Assert the Task is complete
      assertEquals( "Fault was not completed when it was expected to be",
            RefEventStatusKey.COMPLETE.getCd(), lFaultEvent.getEventStatusCd() );

      // Assert the Task was unassigned from the WP (its highest event is itself)
      assertEquals( "Fault was not unassigned from the WP when it was expected to be.",
            lFaultEvent.getPk().getId(), lFaultEvent.getHEvent().getId() );

      // Assert the WP was cancelled.
      assertEquals( "Component work package was cancelled when it was not expected to be.",
            RefEventStatusKey.ACTV.getCd(), lComponentEvent.getEventStatusCd() );

   }


   /**
    *
    * <pre>
    *    Given - a fault against an aircraft root AND
    *            the fault has a REPREF requirement referenced to it AND
    *            the REPREF has a follow-on task of type FOLLOW with a create link AND
    *            the follow-on is against the aircraft SYS config-slot
    *    When  - the corrective task of the fault is completed
    *    Then  - the FOLLOW task definition is initialized against the aircraft SYS inventory.
    * </pre>
    *
    */
   @Test
   public void itCreatesFollowOnTaskAgainstAcftSysWhenCorrOfFaultIsCompleted() throws Exception {

      final PartNoKey aircraftPart = Domain.createPart();

      final AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly( aircraftAssembly -> {

         aircraftAssembly.setRootConfigurationSlot( rootConfigSlot -> {

            rootConfigSlot.addPartGroup( rootConfigSlotPartGroup -> {
               rootConfigSlotPartGroup.setInventoryClass( RefInvClassKey.ACFT );
               rootConfigSlotPartGroup.addPart( aircraftPart );
            } );
            rootConfigSlot.addConfigurationSlot( sysConfigSlot -> {
               sysConfigSlot.setCode( ACFT_SYS_CONFIG_SLOT_CD );
               sysConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SYS );
            } );
         } );
      } );

      final ConfigSlotKey aircraftRootConfigSlot =
            Domain.readRootConfigurationSlot( aircraftAssemblyKey );

      final ConfigSlotKey aircraftSysConfigSlot =
            Domain.readSubConfigurationSlot( aircraftRootConfigSlot, ACFT_SYS_CONFIG_SLOT_CD );

      final InventoryKey aircraftInventoryKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.addSystem( system -> {
            system.setName( SYSTEM_NAME );
            system.setPosition( aircraftSysConfigSlot, 1 );
         } );
      } );

      final InventoryKey sysInventory = Domain.readSystem( aircraftInventoryKey, SYSTEM_NAME );

      final TaskTaskKey followOnTaskTaskKey =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.againstConfigurationSlot( aircraftSysConfigSlot );
               requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
            } );

      final TaskTaskKey repairReferenceTaskTaskKey =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.setTaskClass( RefTaskClassKey.REPREF );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT,
                     followOnTaskTaskKey );
               requirementDefinition.againstConfigurationSlot( aircraftRootConfigSlot );
               requirementDefinition.setDamageRecordBool( NO_DAMAGE_RECORD );
            } );

      final TaskKey faultCorrTaskKey = Domain.createCorrectiveTask( correctiveTask -> {
         correctiveTask.setInventory( aircraftInventoryKey );
      } );

      Domain.createFault( fault -> {
         fault.setCurrentRepairReference( repairReferenceTaskTaskKey );
         fault.setInventory( aircraftInventoryKey );
         fault.setCorrectiveTask( faultCorrTaskKey );
         fault.setFoundOnDate( new Date() );
      } );

      QuerySet qs = QuerySetFactory.getInstance().executeQueryTable( "sched_stask", null );
      final int initialNumberOfScheduledTasks = qs.getRowCount();

      CompleteService.completeNonRootTask( faultCorrTaskKey, Domain.createHumanResource(),
            new Date() );

      qs = QuerySetFactory.getInstance().executeQueryTable( "sched_stask", null );
      final int numberOfScheduledTasksCreatedAfterCompletingFaultCorrTask =
            qs.getRowCount() - initialNumberOfScheduledTasks;

      assertThat( "Only 1 new task should have been created.",
            numberOfScheduledTasksCreatedAfterCompletingFaultCorrTask, is( 1 ) );

      DataSetArgument args = new DataSetArgument();
      args.add( "task_class_cd", RefTaskClassKey.FOLLOW.getCd() );
      args.add( sysInventory, "main_inv_no_db_id", "main_inv_no_id" );
      qs = QuerySetFactory.getInstance().executeQueryTable( "sched_stask", args );

      assertThat( "The task should have been created against the SYS inventory.", qs.getRowCount(),
            is( 1 ) );
   }


   /**
    *
    * <pre>
    *    Given - a fault against an aircraft root AND
    *            the fault has a REPREF requirement referenced to it AND
    *            the fault has a damage record against a TRK inventory on the aircraft AND
    *            the REPREF has a follow-on task of type FOLLOW with a create link AND
    *            the follow-on is against the aircraft TRK config-slot
    *    When  - the corrective task of the fault is completed
    *    Then  - the FOLLOW task definition is initialized against the aircraft TRK inventory.
    * </pre>
    *
    */
   @Test
   public void itCreatesFollowOnTaskAgainstTrkWhenDmgRecOnTrkWhenCorrOfFaultIsCompleted()
         throws Exception {

      final PartNoKey trkPart = Domain.createPart();
      final PartNoKey aircraftPart = Domain.createPart();

      final AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly( aircraftAssembly -> {
         aircraftAssembly.setRootConfigurationSlot( rootConfigSlot -> {
            rootConfigSlot.addPartGroup( rootConfigSlotPartGroup -> {
               rootConfigSlotPartGroup.setInventoryClass( RefInvClassKey.ACFT );
               rootConfigSlotPartGroup.addPart( aircraftPart );
            } );
            rootConfigSlot.addConfigurationSlot( sysConfigSlot -> {
               sysConfigSlot.setCode( ACFT_SYS_CONFIG_SLOT_CD );
               sysConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SYS );
               sysConfigSlot.addConfigurationSlot( trkConfigSlot -> {
                  trkConfigSlot.setConfigurationSlotClass( RefBOMClassKey.TRK );
                  trkConfigSlot.setCode( ACFT_TRK_CONFIG_SLOT_CD );
                  trkConfigSlot.addPartGroup( trkConfigSlotPartGroup -> {
                     trkConfigSlotPartGroup.setInventoryClass( RefInvClassKey.TRK );
                     trkConfigSlotPartGroup.addPart( trkPart );
                  } );
               } );
            } );
         } );
      } );

      final ConfigSlotKey rootConfigSlotKey =
            Domain.readRootConfigurationSlot( aircraftAssemblyKey );
      final ConfigSlotKey trkConfigSlotKey =
            Domain.readSubConfigurationSlot( rootConfigSlotKey, ACFT_TRK_CONFIG_SLOT_CD );
      final ConfigSlotPositionKey trkConfigSlotPositionKey = new ConfigSlotPositionKey(
            trkConfigSlotKey, EqpAssmblPos.getFirstPosId( trkConfigSlotKey ) );
      final String trkPosCd = EqpAssmblPos.getPosCd( trkConfigSlotPositionKey );

      final InventoryKey aircratInventoryKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
      } );

      final InventoryKey trackedInventoryKey = Domain.createTrackedInventory( trackedInventory -> {
         trackedInventory.setPartNumber( trkPart );
         trackedInventory.setParent( aircratInventoryKey );
         trackedInventory.setLastKnownConfigSlot( trkConfigSlotPositionKey.getCd(),
               ACFT_TRK_CONFIG_SLOT_CD, trkPosCd );

      } );

      final TaskTaskKey trkFollowOnTaskTaskKey =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.againstConfigurationSlot( trkConfigSlotKey );
               requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
            } );

      final TaskTaskKey repairReferenceTaskTaskKey =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.setTaskClass( RefTaskClassKey.REPREF );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT,
                     trkFollowOnTaskTaskKey );
               requirementDefinition.againstConfigurationSlot( rootConfigSlotKey );
               requirementDefinition.setDamageRecordBool( HAS_DAMAGE_RECORD );
            } );

      final TaskKey faultCorrTaskKey = Domain.createCorrectiveTask( correctiveTask -> {
         correctiveTask.setInventory( aircratInventoryKey );
      } );

      Domain.createFault( fault -> {
         fault.setCurrentRepairReference( repairReferenceTaskTaskKey );
         fault.setInventory( aircratInventoryKey );
         fault.setCorrectiveTask( faultCorrTaskKey );
         fault.setFoundOnDate( new Date() );
         fault.setDamageRecordInventory( trackedInventoryKey );
      } );

      QuerySet qs = QuerySetFactory.getInstance().executeQueryTable( "sched_stask", null );
      final int initialNumberOfScheduledTasks = qs.getRowCount();

      CompleteService.completeNonRootTask( faultCorrTaskKey, Domain.createHumanResource(),
            new Date() );

      qs = QuerySetFactory.getInstance().executeQueryTable( "sched_stask", null );
      final int numberOfScheduledTasksCreatedAfterCompletingFaultCorrTask =
            qs.getRowCount() - initialNumberOfScheduledTasks;

      assertThat( "Only 1 new task should have been created.",
            numberOfScheduledTasksCreatedAfterCompletingFaultCorrTask, is( 1 ) );

      DataSetArgument args = new DataSetArgument();
      args.add( "task_class_cd", RefTaskClassKey.FOLLOW.getCd() );
      args.add( trackedInventoryKey, "main_inv_no_db_id", "main_inv_no_id" );
      args.add( trkPart, "orig_part_no_db_id", "orig_part_no_id" );
      qs = QuerySetFactory.getInstance().executeQueryTable( "sched_stask", args );

      assertThat( "The task should have been created against the TRK inventory.", qs.getRowCount(),
            is( 1 ) );
   }


   /**
    *
    * <pre>
    *    Given - a fault against an aircraft root AND
    *            the fault has a REPREF requirement referenced to it AND
    *            the REPREF has a follow-on task of type FOLLOW with a create link AND
    *            the follow-on is against the aircraft TRK config-slot
    *    When  - the corrective task of the fault is completed
    *    Then  - no new task is created
    * </pre>
    *
    */
   @Test
   public void itDoesNotCreateTaskWhenNoDmgRecOnTrkWhenCorrOfFaultIsCompleted() throws Exception {
      final String WRONG_TRK_CS_CD = "W_TRK_CS_CD";
      final PartNoKey trkPart = Domain.createPart();
      final PartNoKey aircraftPart = Domain.createPart();
      final PartNoKey wrongTrkPart = Domain.createPart();

      final AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly( aircraftAssembly -> {
         aircraftAssembly.setRootConfigurationSlot( rootConfigSlot -> {
            rootConfigSlot.addPartGroup( rootConfigSlotPartGroup -> {
               rootConfigSlotPartGroup.setInventoryClass( RefInvClassKey.ACFT );
               rootConfigSlotPartGroup.addPart( aircraftPart );
            } );
            rootConfigSlot.addConfigurationSlot( sysConfigSlot -> {
               sysConfigSlot.setCode( ACFT_SYS_CONFIG_SLOT_CD );
               sysConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SYS );
               sysConfigSlot.addConfigurationSlot( trkConfigSlot -> {
                  trkConfigSlot.setConfigurationSlotClass( RefBOMClassKey.TRK );
                  trkConfigSlot.setCode( ACFT_TRK_CONFIG_SLOT_CD );
                  trkConfigSlot.addPartGroup( trkConfigSlotPartGroup -> {
                     trkConfigSlotPartGroup.setInventoryClass( RefInvClassKey.TRK );
                     trkConfigSlotPartGroup.addPart( trkPart );
                  } );
               } );
               sysConfigSlot.addConfigurationSlot( trkConfigSlot -> {
                  trkConfigSlot.setConfigurationSlotClass( RefBOMClassKey.TRK );
                  trkConfigSlot.setCode( WRONG_TRK_CS_CD );
                  trkConfigSlot.addPartGroup( trkConfigSlotPartGroup -> {
                     trkConfigSlotPartGroup.setInventoryClass( RefInvClassKey.TRK );
                     trkConfigSlotPartGroup.addPart( wrongTrkPart );
                  } );
               } );
            } );

         } );
      } );

      final ConfigSlotKey rootConfigSlotKey =
            Domain.readRootConfigurationSlot( aircraftAssemblyKey );
      final ConfigSlotKey trkConfigSlotKey =
            Domain.readSubConfigurationSlot( rootConfigSlotKey, ACFT_TRK_CONFIG_SLOT_CD );
      final ConfigSlotPositionKey trkConfigSlotPositionKey = new ConfigSlotPositionKey(
            trkConfigSlotKey, EqpAssmblPos.getFirstPosId( trkConfigSlotKey ) );
      final String trkPosCd = EqpAssmblPos.getPosCd( trkConfigSlotPositionKey );
      final ConfigSlotKey wrongTrkConfigSlotKey =
            Domain.readSubConfigurationSlot( rootConfigSlotKey, WRONG_TRK_CS_CD );
      final ConfigSlotPositionKey wrongTrkConfigSlotPositionKey = new ConfigSlotPositionKey(
            wrongTrkConfigSlotKey, EqpAssmblPos.getFirstPosId( wrongTrkConfigSlotKey ) );
      final String wrongTrkPosCd = EqpAssmblPos.getPosCd( wrongTrkConfigSlotPositionKey );

      final InventoryKey aircratInventoryKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
      } );

      Domain.createTrackedInventory( trackedInventory -> {
         trackedInventory.setPartNumber( trkPart );
         trackedInventory.setParent( aircratInventoryKey );
         trackedInventory.setLastKnownConfigSlot( trkConfigSlotPositionKey.getCd(),
               ACFT_TRK_CONFIG_SLOT_CD, trkPosCd );

      } );
      final InventoryKey wrongTrackedInventoryKey =
            Domain.createTrackedInventory( trackedInventory -> {
               trackedInventory.setPartNumber( wrongTrkPart );
               trackedInventory.setParent( aircratInventoryKey );
               trackedInventory.setLastKnownConfigSlot( wrongTrkConfigSlotPositionKey.getCd(),
                     WRONG_TRK_CS_CD, wrongTrkPosCd );

            } );

      final TaskTaskKey trkFollowOnTaskTaskKey =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.againstConfigurationSlot( trkConfigSlotKey );
               requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
            } );

      final TaskTaskKey repairReferenceTaskTaskKey =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.setTaskClass( RefTaskClassKey.REPREF );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT,
                     trkFollowOnTaskTaskKey );
               requirementDefinition.againstConfigurationSlot( rootConfigSlotKey );
               requirementDefinition.setDamageRecordBool( HAS_DAMAGE_RECORD );
            } );

      final TaskKey faultCorrTaskKey = Domain.createCorrectiveTask( correctiveTask -> {
         correctiveTask.setInventory( aircratInventoryKey );
      } );

      Domain.createFault( fault -> {
         fault.setCurrentRepairReference( repairReferenceTaskTaskKey );
         fault.setInventory( aircratInventoryKey );
         fault.setCorrectiveTask( faultCorrTaskKey );
         fault.setFoundOnDate( new Date() );
         fault.setDamageRecordInventory( wrongTrackedInventoryKey );
      } );

      QuerySet qs = QuerySetFactory.getInstance().executeQueryTable( "sched_stask", null );
      final int initialNumberOfScheduledTasks = qs.getRowCount();

      CompleteService.completeNonRootTask( faultCorrTaskKey, Domain.createHumanResource(),
            new Date() );

      qs = QuerySetFactory.getInstance().executeQueryTable( "sched_stask", null );
      final int numberOfScheduledTasksCreatedAfterCompletingFaultCorrTask =
            initialNumberOfScheduledTasks - qs.getRowCount();

      assertThat( "No new task should have been created",
            numberOfScheduledTasksCreatedAfterCompletingFaultCorrTask, is( 0 ) );

   }


   /**
    *
    * <pre>
    *    Given - a fault against an aircraft root AND
    *            the aircraft has an engine AND
    *            the engine has a TRK inventory AND
    *            the fault has a REPREF requirement referenced to it AND
    *            the fault has a damage record against a TRK inventory on the engine AND
    *            the REPREF has a follow-on task of type FOLLOW with a create link AND
    *            the follow-on is against the engine TRK config-slot
    *    When  - the corrective task of the fault is completed
    *    Then  - the FOLLOW task definition is initialized against the engine TRK inventory.
    * </pre>
    *
    */
   @Test
   public void itCreatesFollowOnTaskAgainstEngTrkWhenDmgRecOnEngTrkWhenCorrOfFaultIsCompleted()
         throws MxException, TriggerException {
      final PartNoKey trkPart = Domain.createPart();
      final PartNoKey aircraftPart = Domain.createPart();

      final AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly( aircraftAssembly -> {

         aircraftAssembly.setRootConfigurationSlot( rootConfigSlot -> {
            rootConfigSlot.addPartGroup( rootConfigSlotPartGroup -> {
               rootConfigSlotPartGroup.setInventoryClass( RefInvClassKey.ACFT );
               rootConfigSlotPartGroup.addPart( aircraftPart );
            } );
            rootConfigSlot.addConfigurationSlot( sysConfigSlot -> {
               sysConfigSlot.setCode( ACFT_SYS_CONFIG_SLOT_CD );
               sysConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SYS );

               sysConfigSlot.addConfigurationSlot( subAssyConfigSlot -> {

                  subAssyConfigSlot.setCode( ACFT_SUB_ASSY_CONFIG_SLOT_CD );
                  subAssyConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SUBASSY );
               } );
            } );
         } );
      } );

      final AssemblyKey engineAssemblyKey = Domain.createEngineAssembly( engineAssembly -> {

         engineAssembly.setRootConfigurationSlot( rootConfigSlot -> {

            rootConfigSlot.addConfigurationSlot( sysConfigSlot -> {
               sysConfigSlot.setCode( ENG_SYS_CONFIG_SLOT_CD );
               sysConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SYS );
               sysConfigSlot.addPartGroup( aSysCsPartGroup -> {
                  aSysCsPartGroup.setInventoryClass( RefInvClassKey.SYS );
               } );
               sysConfigSlot.addConfigurationSlot( trkConfigSlot -> {
                  trkConfigSlot.setConfigurationSlotClass( RefBOMClassKey.TRK );
                  trkConfigSlot.setCode( ENG_TRK_CONFIG_SLOT_CD );
                  trkConfigSlot.addPartGroup( trkConfigSlotPartGroup -> {
                     trkConfigSlotPartGroup.setInventoryClass( RefInvClassKey.TRK );
                     trkConfigSlotPartGroup.addPart( trkPart );
                  } );
               } );
            } );
         } );
      } );

      final InventoryKey aircraftInventoryKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
      } );

      final ConfigSlotKey aircraftRootConfigSlot =
            Domain.readRootConfigurationSlot( aircraftAssemblyKey );
      final ConfigSlotKey engineRootConfigSlot =
            Domain.readRootConfigurationSlot( engineAssemblyKey );
      final ConfigSlotKey engineTrkConfigSlot =
            Domain.readSubConfigurationSlot( engineRootConfigSlot, ENG_TRK_CONFIG_SLOT_CD );

      final ConfigSlotPositionKey trkConfigSlotPosition = new ConfigSlotPositionKey(
            engineTrkConfigSlot, EqpAssmblPos.getFirstPosId( engineTrkConfigSlot ) );
      final String trkPosCd = EqpAssmblPos.getPosCd( trkConfigSlotPosition );

      final InventoryKey engineInventoryKey = Domain.createEngine( engine -> {
         engine.setAssembly( aircraftAssemblyKey );
         engine.setParent( aircraftInventoryKey );
         engine.setOriginalAssembly( engineAssemblyKey );
      } );

      final InventoryKey trackedInventoryKey = Domain.createTrackedInventory( aTrackedInventory -> {
         aTrackedInventory.setPartNumber( trkPart );
         aTrackedInventory.setParent( engineInventoryKey );
         aTrackedInventory.setLastKnownConfigSlot( trkConfigSlotPosition.getCd(),
               ENG_TRK_CONFIG_SLOT_CD, trkPosCd );
      } );

      final TaskTaskKey trkFollowOnTaskTaskKey =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.againstConfigurationSlot( engineTrkConfigSlot );
               requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
            } );

      final TaskTaskKey repairReferenceTaskTaskKey =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.setTaskClass( RefTaskClassKey.REPREF );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT,
                     trkFollowOnTaskTaskKey );
               requirementDefinition.againstConfigurationSlot( aircraftRootConfigSlot );
               requirementDefinition.setDamageRecordBool( HAS_DAMAGE_RECORD );
            } );

      final TaskKey faultCorrTaskKey = Domain.createCorrectiveTask( correctiveTask -> {
         correctiveTask.setInventory( aircraftInventoryKey );
      } );

      Domain.createFault( fault -> {
         fault.setCurrentRepairReference( repairReferenceTaskTaskKey );
         fault.setInventory( aircraftInventoryKey );
         fault.setCorrectiveTask( faultCorrTaskKey );
         fault.setFoundOnDate( new Date() );
         fault.setDamageRecordInventory( trackedInventoryKey );
      } );

      QuerySet qs = QuerySetFactory.getInstance().executeQueryTable( "sched_stask", null );
      int initialNumberOfScheduledTasks = qs.getRowCount();

      CompleteService.completeNonRootTask( faultCorrTaskKey, Domain.createHumanResource(),
            new Date() );

      qs = QuerySetFactory.getInstance().executeQueryTable( "sched_stask", null );
      int numberOfScheduledTasksCreatedAfterCompletingFaultCorrTask =
            qs.getRowCount() - initialNumberOfScheduledTasks;

      assertThat( "Only 1 new task should have been created.",
            numberOfScheduledTasksCreatedAfterCompletingFaultCorrTask, is( 1 ) );

      DataSetArgument args = new DataSetArgument();
      args.add( "task_class_cd", RefTaskClassKey.FOLLOW.getCd() );
      args.add( trackedInventoryKey, "main_inv_no_db_id", "main_inv_no_id" );
      args.add( trkPart, "orig_part_no_db_id", "orig_part_no_id" );
      qs = QuerySetFactory.getInstance().executeQueryTable( "sched_stask", args );

      assertThat( "The task should have been created agains the engine TRK inventory.",
            qs.getRowCount(), is( 1 ) );
   }


   /**
    *
    * <pre>
    *    Given - a fault against an aircraft root AND
    *            the aircraft has an engine AND
    *            the fault has a REPREF requirement referenced to it AND
    *            the fault has a damage record against the engine AND
    *            the REPREF has 2 follow-on tasks of type FOLLOW with create links AND
    *            a follow-on is against the aircraft SUBASSY AND
    *            a follow-on is against the engine root
    *    When  - the corrective task of the fault is completed
    *    Then  - a follow-on is created against the aircraft SUBASSY AND
    *            a follow-on is created against the engine root
    * </pre>
    *
    */
   @Test
   public void
         itCreatesFollowOnTaskAgainstEngRootAndAcftAubAssyWhenDmgRecOnEngWhenCorrOfFaultIsCompleted()
               throws MxException, TriggerException {
      final PartNoKey enginePart = Domain.createPart();
      final PartNoKey aircraftPart = Domain.createPart();

      final AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly( aircraftAssembly -> {

         aircraftAssembly.setRootConfigurationSlot( rootConfigSlot -> {

            rootConfigSlot.addPartGroup( rootConfigSlotPartGroup -> {
               rootConfigSlotPartGroup.setInventoryClass( RefInvClassKey.ACFT );
               rootConfigSlotPartGroup.addPart( aircraftPart );
            } );

            rootConfigSlot.addConfigurationSlot( sysConfigSlot -> {
               sysConfigSlot.setCode( ACFT_SYS_CONFIG_SLOT_CD );
               sysConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SYS );

               sysConfigSlot.addConfigurationSlot( subAssyConfigSlot -> {
                  subAssyConfigSlot.setCode( ACFT_SUB_ASSY_CONFIG_SLOT_CD );
                  subAssyConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SUBASSY );
                  subAssyConfigSlot.addPartGroup( subAssyConfigSlotPartGroup -> {
                     subAssyConfigSlotPartGroup.setInventoryClass( RefInvClassKey.ASSY );
                     subAssyConfigSlotPartGroup.addPart( enginePart );
                  } );
               } );
            } );
         } );
      } );

      final AssemblyKey engineAssemblyKey = Domain.createEngineAssembly( engineAssembly -> {

         engineAssembly.setRootConfigurationSlot( rootConfigSlot -> {

            rootConfigSlot.setConfigurationSlotClass( RefBOMClassKey.ROOT );
            rootConfigSlot.addPartGroup( rootConfigSlotPartGroup -> {
               rootConfigSlotPartGroup.setInventoryClass( RefInvClassKey.ASSY );
               rootConfigSlotPartGroup.addPart( enginePart );
            } );
         } );
      } );
      final InventoryKey aircraftInventoryKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
      } );

      final ConfigSlotKey aircraftRootConfigSlot =
            Domain.readRootConfigurationSlot( aircraftAssemblyKey );
      final ConfigSlotKey subAssyConfigSlot =
            Domain.readSubConfigurationSlot( aircraftRootConfigSlot, ACFT_SUB_ASSY_CONFIG_SLOT_CD );
      final ConfigSlotKey engineRootConfigSlot =
            Domain.readRootConfigurationSlot( engineAssemblyKey );

      final InventoryKey engineInventoryKey = Domain.createEngine( engine -> {
         engine.setPartNumber( enginePart );
         engine.setParent( aircraftInventoryKey );
         engine.setOriginalAssembly( engineAssemblyKey );
         engine.setPosition( new ConfigSlotPositionKey( subAssyConfigSlot, 1 ) );
      } );

      final TaskTaskKey engRootFollowOnTaskTaskKey =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.againstConfigurationSlot( engineRootConfigSlot );
               requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
            } );

      final TaskTaskKey acftSubAssyFollowOnTaskTaskKey =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.againstConfigurationSlot( subAssyConfigSlot );
               requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
            } );

      final TaskTaskKey repairReferenceTaskTaskKey =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.setTaskClass( RefTaskClassKey.REPREF );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT,
                     engRootFollowOnTaskTaskKey );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT,
                     acftSubAssyFollowOnTaskTaskKey );
               requirementDefinition.againstConfigurationSlot( aircraftRootConfigSlot );
               requirementDefinition.setDamageRecordBool( HAS_DAMAGE_RECORD );
            } );

      final TaskKey faultCorrTaskKey = Domain.createCorrectiveTask( correctiveTask -> {
         correctiveTask.setInventory( aircraftInventoryKey );
      } );

      Domain.createFault( fault -> {
         fault.setCurrentRepairReference( repairReferenceTaskTaskKey );
         fault.setInventory( aircraftInventoryKey );
         fault.setFailedSystem( engineRootConfigSlot );
         fault.setCorrectiveTask( faultCorrTaskKey );
         fault.setFoundOnDate( new Date() );
         fault.setDamageRecordInventory( engineInventoryKey );
      } );

      QuerySet qs = QuerySetFactory.getInstance().executeQueryTable( "sched_stask", null );
      final int initialNumberOfScheduledTasks = qs.getRowCount();

      CompleteService.completeNonRootTask( faultCorrTaskKey, Domain.createHumanResource(),
            new Date() );

      qs = QuerySetFactory.getInstance().executeQueryTable( "sched_stask", null );
      final int numberOfScheduledTasksCreatedAfterCompletingFaultCorrTask =
            qs.getRowCount() - initialNumberOfScheduledTasks;

      assertThat( "Only 2 new tasks should have been created.",
            numberOfScheduledTasksCreatedAfterCompletingFaultCorrTask, is( 2 ) );

      DataSetArgument args = new DataSetArgument();
      args.add( "task_class_cd", RefTaskClassKey.FOLLOW.getCd() );
      args.add( engineInventoryKey, "main_inv_no_db_id", "main_inv_no_id" );
      args.add( enginePart, "orig_part_no_db_id", "orig_part_no_id" );
      qs = QuerySetFactory.getInstance().executeQueryTable( "sched_stask", args );

      assertThat( "Only 2 tasks should have been created.", qs.getRowCount(), is( 2 ) );

      assertThat( "A task on the engine root was not created",
            this.checkIfTaskTaskKeyInQs( qs, engRootFollowOnTaskTaskKey ), is( true ) );

      assertThat( "A task on the aircraft sub-assembly was not created.",
            this.checkIfTaskTaskKeyInQs( qs, acftSubAssyFollowOnTaskTaskKey ), is( true ) );
   }


   /**
    *
    * <pre>
    *    Given - a fault against an aircraft root AND
    *            the fault has a REPREF requirement referenced to it AND
    *            the REPREF has a follow-on task of type FOLLOW with a create link AND
    *            the follow-on is against the aircraft SYS config-slot AND
    *            the follow-on task has a applicability rule
    *    When  - the corrective task of the fault is completed
    *    Then  - no new task is created
    * </pre>
    *
    */
   @Test
   public void itDoesNotCreateTaskWheFollowTaskNotApplicableWhenCorrOfFaultIsCompleted()
         throws Exception {

      final PartNoKey aircraftPart = Domain.createPart();

      final AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly( aircraftAssembly -> {

         aircraftAssembly.setRootConfigurationSlot( rootConfigSlot -> {
            rootConfigSlot.addPartGroup( rootConfigSlotPartGroup -> {
               rootConfigSlotPartGroup.setInventoryClass( ACFT );
               rootConfigSlotPartGroup.addPart( aircraftPart );
            } );
            rootConfigSlot.addConfigurationSlot( sysConfigSlot -> {
               sysConfigSlot.setCode( ACFT_SYS_CONFIG_SLOT_CD );
               sysConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SYS );
            } );
         } );
      } );
      final ConfigSlotKey aircraftRootConfigSlot =
            Domain.readRootConfigurationSlot( aircraftAssemblyKey );
      final ConfigSlotKey aircraftSysConfigSlot =
            Domain.readSubConfigurationSlot( aircraftRootConfigSlot, ACFT_SYS_CONFIG_SLOT_CD );

      final InventoryKey aircraftInventoryKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.addSystem( system -> {
            system.setName( SYSTEM_NAME );
            system.setPosition( aircraftSysConfigSlot, 1 );
         } );
      } );

      final TaskTaskKey followOnTaskTaskKey =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.againstConfigurationSlot( aircraftSysConfigSlot );
               requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
               requirementDefinition.setApplicabilityRule( "ApplicabilityRule" );
            } );

      final TaskTaskKey repairReferenceTaskTaskKey =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.setTaskClass( RefTaskClassKey.REPREF );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT,
                     followOnTaskTaskKey );
               requirementDefinition.againstConfigurationSlot( aircraftRootConfigSlot );
               requirementDefinition.setDamageRecordBool( NO_DAMAGE_RECORD );
            } );

      final TaskKey faultCorrTaskKey = Domain.createCorrectiveTask( correctiveTask -> {
         correctiveTask.setInventory( aircraftInventoryKey );
      } );

      Domain.createFault( fault -> {
         fault.setCurrentRepairReference( repairReferenceTaskTaskKey );
         fault.setInventory( aircraftInventoryKey );
         fault.setCorrectiveTask( faultCorrTaskKey );
         fault.setFoundOnDate( new Date() );
      } );

      QuerySet qs = QuerySetFactory.getInstance().executeQueryTable( "sched_stask", null );
      final int initialNumberOfScheduledTasks = qs.getRowCount();

      CompleteService.completeNonRootTask( faultCorrTaskKey, Domain.createHumanResource(),
            new Date() );

      qs = QuerySetFactory.getInstance().executeQueryTable( "sched_stask", null );
      final int numberOfScheduledTasksCreatedAfterCompletingFaultCorrTask =
            qs.getRowCount() - initialNumberOfScheduledTasks;

      assertThat( "No new task should have been created.",
            numberOfScheduledTasksCreatedAfterCompletingFaultCorrTask, is( 0 ) );
   }


   /**
    *
    * <pre>
    *    Given - a fault against an aircraft root AND
    *            the fault has a REPREF requirement referenced to it AND
    *            the REPREF has a follow-on task of type REQ with a create link AND
    *            the follow-on is against the aircraft SYS config-slot AND
    *    When  - the corrective task of the fault is completed
    *    Then  - no new task is created
    * </pre>
    *
    */
   @Test
   public void itDoesNotCreateFollowOnTaskOfTypeReqWhenCorrOfFaultIsCompleted() throws Exception {

      PartNoKey aircraftPart = Domain.createPart();

      final AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly( aircraftAssembly -> {

         aircraftAssembly.setRootConfigurationSlot( rootConfigSlot -> {
            rootConfigSlot.addPartGroup( rootConfigSlotPartGroup -> {
               rootConfigSlotPartGroup.setInventoryClass( ACFT );
               rootConfigSlotPartGroup.addPart( aircraftPart );
            } );
            rootConfigSlot.addConfigurationSlot( sysConfigSlot -> {
               sysConfigSlot.setCode( ACFT_SYS_CONFIG_SLOT_CD );
               sysConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SYS );
            } );
         } );
      } );
      final ConfigSlotKey aircraftRootConfigSlot =
            Domain.readRootConfigurationSlot( aircraftAssemblyKey );
      final ConfigSlotKey aircraftSysConfigSlot =
            Domain.readSubConfigurationSlot( aircraftRootConfigSlot, ACFT_SYS_CONFIG_SLOT_CD );

      final InventoryKey aircraftInventoryKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.addSystem( system -> {
            system.setName( SYSTEM_NAME );
            system.setPosition( aircraftSysConfigSlot, 1 );
         } );
      } );

      final TaskTaskKey followOnTaskTaskKey =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.againstConfigurationSlot( aircraftSysConfigSlot );
               requirementDefinition.setTaskClass( RefTaskClassKey.REQ );
            } );

      final TaskTaskKey repairReferenceTaskTaskKey =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.setTaskClass( RefTaskClassKey.REPREF );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT,
                     followOnTaskTaskKey );
               requirementDefinition.againstConfigurationSlot( aircraftRootConfigSlot );
               requirementDefinition.setDamageRecordBool( NO_DAMAGE_RECORD );
            } );

      final TaskKey faultCorrTaskKey = Domain.createCorrectiveTask( correctiveTask -> {
         correctiveTask.setInventory( aircraftInventoryKey );
      } );

      Domain.createFault( fault -> {
         fault.setCurrentRepairReference( repairReferenceTaskTaskKey );
         fault.setInventory( aircraftInventoryKey );
         fault.setCorrectiveTask( faultCorrTaskKey );
         fault.setFoundOnDate( new Date() );
      } );

      QuerySet qs = QuerySetFactory.getInstance().executeQueryTable( "sched_stask", null );
      final int initialNumberOfScheduledTasks = qs.getRowCount();

      CompleteService.completeNonRootTask( faultCorrTaskKey, Domain.createHumanResource(),
            new Date() );

      qs = QuerySetFactory.getInstance().executeQueryTable( "sched_stask", null );
      final int numberOfScheduledTasksCreatedAfterCompletingFaultCorrTask =
            qs.getRowCount() - initialNumberOfScheduledTasks;

      assertThat( "No new task should have been created.",
            numberOfScheduledTasksCreatedAfterCompletingFaultCorrTask, is( 0 ) );
   }


   /**
    * This test is testing when there is a damage record with location, when a new task instance
    * created, it will append the location to the event title.
    *
    * <pre>
    *    Given an aircraft.
    *    And a fault on the aircraft system config slot.
    *    And a repair reference on the fault with follow on task.
    *    And a damage record on the aircraft with location,
    *    When complete the fault / complete the follow on task.
    *    Then the next kicked off follow on task event title will be updated appending with the location.
    * </pre>
    */
   @Test
   public void itUpdatesFollowOnTaskEventSdecWhenDamagedRecordExists() throws Exception {
      final PartNoKey aircraftPart = Domain.createPart();

      final AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly( aircraftAssembly -> {

         aircraftAssembly.setRootConfigurationSlot( rootConfigSlot -> {

            rootConfigSlot.addPartGroup( rootConfigSlotPartGroup -> {
               rootConfigSlotPartGroup.setInventoryClass( RefInvClassKey.ACFT );
               rootConfigSlotPartGroup.addPart( aircraftPart );
            } );
            rootConfigSlot.addConfigurationSlot( sysConfigSlot -> {
               sysConfigSlot.setCode( ACFT_SYS_CONFIG_SLOT_CD );
               sysConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SYS );
            } );
         } );
      } );

      final ConfigSlotKey aircraftRootConfigSlot =
            Domain.readRootConfigurationSlot( aircraftAssemblyKey );

      final ConfigSlotKey aircraftSysConfigSlot =
            Domain.readSubConfigurationSlot( aircraftRootConfigSlot, ACFT_SYS_CONFIG_SLOT_CD );

      final InventoryKey aircraftInventoryKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.addSystem( system -> {
            system.setName( SYSTEM_NAME );
            system.setPosition( aircraftSysConfigSlot, 1 );
         } );
      } );

      final TaskTaskKey followOnTaskTaskKey =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.againstConfigurationSlot( aircraftSysConfigSlot );
               requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
               requirementDefinition.setCode( FOLLOW_ON_TASK );
               requirementDefinition.setTaskName( FOLLOW_ON_TASK );
               requirementDefinition.setRecurring( true );
               requirementDefinition.setRescheduleFromOption( RefReschedFromKey.EXECUTE );
            } );

      final TaskTaskKey repairReferenceTaskTaskKey =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.setTaskClass( RefTaskClassKey.REPREF );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT,
                     followOnTaskTaskKey );
               requirementDefinition.againstConfigurationSlot( aircraftRootConfigSlot );
               requirementDefinition.setDamageRecordBool( NO_DAMAGE_RECORD );
            } );

      final TaskKey faultCorrTaskKey = Domain.createCorrectiveTask( correctiveTask -> {
         correctiveTask.setInventory( aircraftInventoryKey );
      } );

      final FaultKey faultKey = Domain.createFault( fault -> {
         fault.setCurrentRepairReference( repairReferenceTaskTaskKey );
         fault.setInventory( aircraftInventoryKey );
         fault.setCorrectiveTask( faultCorrTaskKey );
         fault.setFoundOnDate( new Date() );
      } );

      Domain.createDamageRecord( damageRecord -> {
         damageRecord.setFaultKey( faultKey );
         damageRecord.setInventoryKey( aircraftInventoryKey );
         damageRecord.setLocation( DAMAGE_LOCATION );
      } );

      CompleteService.completeNonRootTask( faultCorrTaskKey, Domain.createHumanResource(),
            new Date() );
      DataSetArgument args = new DataSetArgument();
      args.add( "rel_event_db_id", faultKey.getDbId() );
      args.add( "rel_event_id", faultKey.getId() );
      args.add( "rel_type_cd", RefRelationTypeKey.FAULTREL.getCd() );
      QuerySet followOnQs =
            QuerySetFactory.getInstance().executeQueryTable( "evt_event_rel", args );
      followOnQs.next();
      EventKey followOnEvent = new EventKey( followOnQs.getInt( 1 ), followOnQs.getInt( 2 ) );

      String updatedEventSdesc = evtEventDao.findByPrimaryKey( followOnEvent ).getEventSdesc();

      assertEquals( "The event title of the first follow on task is not updated correctly.",
            "FOLLOW_ON_TASK (FOLLOW_ON_TASK) - DAMAGE_LOCATION", updatedEventSdesc );

      Domain.createWorkPackage( workPackage -> {
         workPackage.addTask( new TaskKey( followOnQs.getInt( 1 ), followOnQs.getInt( 2 ) ) );
         workPackage.setStatus( RefEventStatusKey.IN_WORK );
      } );
      CompleteService completeService =
            new CompleteService( new TaskKey( followOnQs.getInt( 1 ), followOnQs.getInt( 2 ) ) );
      completeService.completeBatch( iAuthorizingHr, new Date(), true );

      List<EvtEventRel> nextFollowOnTask = EvtEventRel.findByRelationshipType(
            new TaskKey( followOnEvent.getEventKey() ), RefRelationTypeKey.DEPT );
      String nextUpdatedEventSdesc =
            evtEventDao.findByPrimaryKey( nextFollowOnTask.get( 0 ).getRelEvent() ).getEventSdesc();
      assertEquals( "The event title of the kicked off follow on task is not updated correctly.",
            "FOLLOW_ON_TASK (FOLLOW_ON_TASK) - DAMAGE_LOCATION", nextUpdatedEventSdesc );
   }


   private void autoCompletesAFaultAndUnassignsItFromTheWorkPackageDataSetup() {
      iAuthorizingHr = new HumanResourceDomainBuilder().build();
      iUserId = iAuthorizingHr.getId();
      GlobalParameters lGlobalParametersFake = new GlobalParametersFake( LOGIC );

      // Creating a component work package has logic to automatically created labour rows based on
      // the labour skills provided by the BLANK_RO_SIGNATURE config parm. By default those are
      // "AET" and "INSP", unfortunately both of those are 10-level labour skills
      // (ref_labour_skill) and do not exist in the am-query-test DB.
      lGlobalParametersFake.setString( "BLANK_RO_SIGNATURE", "" );

      // The UserParametersFake is used because the user parameter
      // ALLOW_ASSIGN_LRP_ITEM_FOR_INWORK_WP need to have a value.
      GlobalParameters.setInstance( LOGIC, lGlobalParametersFake );

      int lUserId = OrgHr.findByPrimaryKey( iAuthorizingHr ).getUserId();
      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( iAuthorizingHr ) );
      UserParametersFake lUserParms = new UserParametersFake( lUserId, "LOGIC" );
      lUserParms.setProperty( "HOLE_TOBE_RMVD_EXISTS", "INFO" );
      UserParameters.setInstance( lUserId, "LOGIC", lUserParms );

   }


   private boolean checkIfTaskTaskKeyInQs( QuerySet qs, TaskTaskKey desiredTaskTaskKey ) {
      qs.first();
      do {
         if ( qs.getKey( TaskTaskKey.class, "task_db_id", "task_id" )
               .equals( desiredTaskTaskKey ) ) {
            return true;
         }
      } while ( qs.next() );
      return false;
   }

}
