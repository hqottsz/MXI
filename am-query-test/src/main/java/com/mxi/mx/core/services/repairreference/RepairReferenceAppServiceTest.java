package com.mxi.mx.core.services.repairreference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.google.inject.AbstractModule;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDepActionKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.maintenance.plan.repairreference.app.RepairReferenceAppService;
import com.mxi.mx.core.maintenance.plan.repairreference.app.RepairReferenceWithFollowOnTasksTO;
import com.mxi.mx.core.services.fault.FaultDetailsService;
import com.mxi.mx.core.services.fault.MxFaultDetailsService;
import com.mxi.mx.core.table.eqp.EqpAssmblPos;


@RunWith( BlockJUnit4ClassRunner.class )
public class RepairReferenceAppServiceTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule injectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( FaultDetailsService.class ).to( MxFaultDetailsService.class );
            }
         } );

   private static final String ACFT_SYS_CS_CD = "ASCSCD";
   private static final String ENG_SYS_CS_CD = "ESCSCD";
   private static final String ACFT_SYS_CS_CD_2 = "ASCSCD2";
   private static final String ACFT_TRK_CS_CD = "ATCSCD";
   private static final String ENG_TRK_CS_CD = "ETCSCD";
   private static final String ACFT_SA_CS_CD = "ASACSCD";
   private static final String ACFT_SYSTEM = "ACFT_SYSTEM";

   private RepairReferenceAppService repairReferenceAppService;

   private PartNoKey aircraftTrkPart;
   private PartNoKey engineTrkPart;
   private PartNoKey enginePart;
   private AssemblyKey aircraftAssemblyKey;
   private InventoryKey aircraftInventoryKey;
   private AssemblyKey engineAssemblyKey;
   private LocationKey locationKey;

   private ConfigSlotKey aircraftRootConfigSlot;
   private ConfigSlotKey aircraftSysConfigSlot;
   private ConfigSlotKey aircraftTrkConfigSlot;
   private ConfigSlotKey aircraftSubAssyConfigSlot;
   private ConfigSlotKey engineRootConfigSlot;
   private ConfigSlotKey engineTrkConfigSlot;

   private TaskTaskKey acftRootTask;
   private TaskTaskKey acftSysTask;
   private TaskTaskKey engRootTask;

   private TaskTaskKey repRefTaskDefinition;


   @Before
   public void setUp() {
      repairReferenceAppService =
            InjectorContainer.get().getInstance( RepairReferenceAppService.class );

      // Parts
      aircraftTrkPart = Domain.createPart();
      engineTrkPart = Domain.createPart();
      enginePart = Domain.createPart();

      // Assemblies
      aircraftAssemblyKey =
            this.createAircraftAssemblyWithAcftTrkPartAndEnginePart( aircraftTrkPart, enginePart );
      aircraftInventoryKey = this.createAircraftInventory( aircraftAssemblyKey );
      engineAssemblyKey = this.createEngineAssemblyWithEngTrkPart( enginePart, engineTrkPart );

      // ConfigSlots
      aircraftRootConfigSlot = Domain.readRootConfigurationSlot( aircraftAssemblyKey );
      aircraftSysConfigSlot =
            Domain.readSubConfigurationSlot( aircraftRootConfigSlot, ACFT_SYS_CS_CD );
      aircraftTrkConfigSlot =
            Domain.readSubConfigurationSlot( aircraftRootConfigSlot, ACFT_TRK_CS_CD );
      aircraftSubAssyConfigSlot =
            Domain.readSubConfigurationSlot( aircraftRootConfigSlot, ACFT_SA_CS_CD );
      engineRootConfigSlot = Domain.readRootConfigurationSlot( engineAssemblyKey );
      engineTrkConfigSlot = Domain.readSubConfigurationSlot( engineRootConfigSlot, ENG_TRK_CS_CD );

      locationKey = new LocationKey( "1:1" );
      // Aircraft follow on tasks
      acftRootTask = Domain.createRequirementDefinition( requirementDefinition -> {
         requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
         requirementDefinition.againstConfigurationSlot( aircraftRootConfigSlot );
         requirementDefinition.setCode( "FOT1" );
         requirementDefinition.setTaskName( "FOT1" );
      } );
      acftSysTask = Domain.createRequirementDefinition( requirementDefinition -> {
         requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
         requirementDefinition.againstConfigurationSlot( aircraftSysConfigSlot );
         requirementDefinition.setCode( "FOT2" );
         requirementDefinition.setTaskName( "FOT2" );
      } );
      // Engine root follow on tasks
      engRootTask = Domain.createRequirementDefinition( requirementDefinition -> {
         requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
         requirementDefinition.againstConfigurationSlot( engineRootConfigSlot );
         requirementDefinition.setCode( "FOT3" );
         requirementDefinition.setTaskName( "FOT3" );
      } );

      repRefTaskDefinition = Domain.createRequirementDefinition( requirementDefinition -> {
         requirementDefinition.setCode( "REPREF1" );
         requirementDefinition.setTaskName( "REPREF1" );
         requirementDefinition.setTaskClass( RefTaskClassKey.REPREF );
         requirementDefinition.againstConfigurationSlot( aircraftRootConfigSlot );
         requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT, acftRootTask );
         requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT, acftSysTask );
         requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT, engRootTask );

      } );

   }


   @Test
   public void getRepairReferenceWithFollowOnTasks_ForApplicableInventoryForAcftRootAndAcftSys() {

      final TaskKey lCorrectiveTaskKey = new TaskBuilder().onInventory( aircraftInventoryKey )
            .withTaskClass( RefTaskClassKey.CORR ).withStatus( RefEventStatusKey.ACTV ).build();

      // Create a fault
      final FaultKey faultKey = Domain.createFault( fault -> {
         fault.setCurrentRepairReference( repRefTaskDefinition );
         fault.setCorrectiveTask( lCorrectiveTaskKey );
      } );

      // Execute
      RepairReferenceWithFollowOnTasksTO repairReferenceWithFollowOnTasksTO =
            repairReferenceAppService.getRepairReferenceWithFollowOnTasks( faultKey,
                  repRefTaskDefinition );

      assertEquals( repairReferenceWithFollowOnTasksTO.getName(), "REPREF1" );
      assertEquals( 2, repairReferenceWithFollowOnTasksTO.getFollowOnTasks().size() );
      assertTrue( repairReferenceWithFollowOnTasksTO.getFollowOnTasks().stream()
            .filter( t -> t.getCode().equals( "FOT1" ) ).findFirst().isPresent() );
      assertTrue( repairReferenceWithFollowOnTasksTO.getFollowOnTasks().stream()
            .filter( t -> t.getCode().equals( "FOT2" ) ).findFirst().isPresent() );
   }


   @Test
   public void getRepairReferenceWithFollowOnTasks_ForEngTrkInventory() {

      final InventoryKey engineInventoryKey = Domain.createEngine( engine -> {
         engine.setAssembly( aircraftAssemblyKey );
         engine.setParent( aircraftInventoryKey );
         engine.setOriginalAssembly( engineAssemblyKey );
      } );

      final ConfigSlotPositionKey trkConfigSlotPosition = new ConfigSlotPositionKey(
            engineTrkConfigSlot, EqpAssmblPos.getFirstPosId( engineTrkConfigSlot ) );
      final String trkPosCd = EqpAssmblPos.getPosCd( trkConfigSlotPosition );

      final InventoryKey engineTrackedInventory =
            Domain.createTrackedInventory( trackedInventory -> {
               trackedInventory.setPartNumber( engineTrkPart );
               trackedInventory.setParent( engineInventoryKey );
               trackedInventory.setLastKnownConfigSlot( trkConfigSlotPosition.getCd(),
                     ENG_TRK_CS_CD, trkPosCd );
               trackedInventory.setLocation( locationKey );
            } );

      final TaskKey lCorrectiveTaskKey = new TaskBuilder().onInventory( engineTrackedInventory )
            .withTaskClass( RefTaskClassKey.CORR ).withStatus( RefEventStatusKey.ACTV ).build();

      // Create a fault
      final FaultKey faultKey = Domain.createFault( fault -> {
         fault.setCurrentRepairReference( repRefTaskDefinition );
         fault.setCorrectiveTask( lCorrectiveTaskKey );
         fault.setInventory( engineInventoryKey );
      } );

      // Execute
      RepairReferenceWithFollowOnTasksTO repairReferenceWithFollowOnTasksTO =
            repairReferenceAppService.getRepairReferenceWithFollowOnTasks( faultKey,
                  repRefTaskDefinition );

      assertEquals( repairReferenceWithFollowOnTasksTO.getName(), "REPREF1" );
      assertEquals( 1, repairReferenceWithFollowOnTasksTO.getFollowOnTasks().size() );
      assertTrue( repairReferenceWithFollowOnTasksTO.getFollowOnTasks().stream()
            .filter( t -> t.getCode().equals( "FOT3" ) ).findFirst().isPresent() );
   }


   @Test
   public void getRepairReferenceWithFollowOnTasks_ForAcftSubAssyAndEngRootInventory() {

      final InventoryKey engineInventoryKey = Domain.createEngine( engine -> {
         engine.setPartNumber( enginePart );
         engine.setParent( aircraftInventoryKey );
         engine.setOriginalAssembly( engineAssemblyKey );
         engine.setPosition( new ConfigSlotPositionKey( aircraftSubAssyConfigSlot, 1 ) );
         engine.setLocation( locationKey );
      } );

      TaskKey correctiveTaskKey = Domain.createCorrectiveTask( task -> {
         task.setInventory( engineInventoryKey );
      } );
      // Create a fault
      FaultKey faultKey = Domain.createFault( fault -> {
         fault.setCurrentRepairReference( repRefTaskDefinition );
         fault.setCorrectiveTask( correctiveTaskKey );
         fault.setInventory( engineInventoryKey );
      } );

      // Execute
      RepairReferenceWithFollowOnTasksTO repairReferenceWithFollowOnTasksTO =
            repairReferenceAppService.getRepairReferenceWithFollowOnTasks( faultKey,
                  repRefTaskDefinition );

      assertEquals( repairReferenceWithFollowOnTasksTO.getName(), "REPREF1" );
      assertEquals( 2, repairReferenceWithFollowOnTasksTO.getFollowOnTasks().size() );
      assertTrue( repairReferenceWithFollowOnTasksTO.getFollowOnTasks().stream()
            .filter( t -> t.getCode().equals( "FOT1" ) ).findFirst().isPresent() );
      assertTrue( repairReferenceWithFollowOnTasksTO.getFollowOnTasks().stream()
            .filter( t -> t.getCode().equals( "FOT2" ) ).findFirst().isPresent() );
   }


   @Test
   public void getRepairReferenceWithFollowOnTasks_ForAcftTrkInventory() {

      final ConfigSlotPositionKey trkConfigSlotPositionKey = new ConfigSlotPositionKey(
            aircraftTrkConfigSlot, EqpAssmblPos.getFirstPosId( aircraftTrkConfigSlot ) );
      final String trkPosCd = EqpAssmblPos.getPosCd( trkConfigSlotPositionKey );

      final InventoryKey aircraftTrackedInventory =
            Domain.createTrackedInventory( trackedInventory -> {
               trackedInventory.setPartNumber( aircraftTrkPart );
               trackedInventory.setParent( aircraftInventoryKey );
               trackedInventory.setLastKnownConfigSlot( trkConfigSlotPositionKey.getCd(),
                     ACFT_TRK_CS_CD, trkPosCd );
               trackedInventory.setLocation( locationKey );
            } );

      TaskKey correctiveTaskKey = Domain.createCorrectiveTask( task -> {
         task.setInventory( aircraftTrackedInventory );
      } );

      // Create a fault
      FaultKey faultKey = Domain.createFault( fault -> {
         fault.setCurrentRepairReference( repRefTaskDefinition );
         fault.setCorrectiveTask( correctiveTaskKey );
         fault.setInventory( aircraftInventoryKey );
      } );

      // Execute
      RepairReferenceWithFollowOnTasksTO repairReferenceWithFollowOnTasksTO =
            repairReferenceAppService.getRepairReferenceWithFollowOnTasks( faultKey,
                  repRefTaskDefinition );

      assertEquals( repairReferenceWithFollowOnTasksTO.getName(), "REPREF1" );
      assertEquals( 2, repairReferenceWithFollowOnTasksTO.getFollowOnTasks().size() );
      assertTrue( repairReferenceWithFollowOnTasksTO.getFollowOnTasks().stream()
            .filter( t -> t.getCode().equals( "FOT1" ) ).findFirst().isPresent() );
      assertTrue( repairReferenceWithFollowOnTasksTO.getFollowOnTasks().stream()
            .filter( t -> t.getCode().equals( "FOT2" ) ).findFirst().isPresent() );
   }


   private InventoryKey createAircraftInventory( AssemblyKey aircraftAssemblyKey ) {

      final ConfigSlotKey aircraftRootConfigSlotKey =
            Domain.readRootConfigurationSlot( aircraftAssemblyKey );
      final ConfigSlotKey aircraftSysConfigSlotKey =
            Domain.readSubConfigurationSlot( aircraftRootConfigSlotKey, ACFT_SYS_CS_CD );

      return Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.addSystem( system -> {
            system.setName( ACFT_SYSTEM );
            system.setPosition( aircraftSysConfigSlotKey, 1 );
         } );
      } );
   }


   private AssemblyKey createEngineAssemblyWithEngTrkPart( PartNoKey engPart,
         PartNoKey engTrkPart ) {

      final PartNoKey engineSysPart = Domain.createPart();

      return Domain.createEngineAssembly( engineAssembly -> {
         engineAssembly.setRootConfigurationSlot( rootConfigSlot -> {
            rootConfigSlot.setConfigurationSlotClass( RefBOMClassKey.ROOT );
            rootConfigSlot.addPartGroup( rootConfigSlotPartGroup -> {
               rootConfigSlotPartGroup.setInventoryClass( RefInvClassKey.ASSY );
               rootConfigSlotPartGroup.addPart( engPart );
            } );
            rootConfigSlot.addConfigurationSlot( sysConfigSlot -> {
               sysConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SYS );
               sysConfigSlot.setCode( ENG_SYS_CS_CD );
               sysConfigSlot.addPartGroup( sysConfigSlotPartGroup -> {
                  sysConfigSlotPartGroup.setInventoryClass( RefInvClassKey.SYS );
                  sysConfigSlotPartGroup.addPart( engineSysPart );
               } );

               sysConfigSlot.addConfigurationSlot( trkConfigSlot -> {
                  trkConfigSlot.setConfigurationSlotClass( RefBOMClassKey.TRK );
                  trkConfigSlot.setCode( ENG_TRK_CS_CD );
                  trkConfigSlot.addPartGroup( trkConfigSlotPartGroup -> {
                     trkConfigSlotPartGroup.setInventoryClass( RefInvClassKey.TRK );
                     trkConfigSlotPartGroup.addPart( engTrkPart );
                  } );
               } );

            } );
         } );
      } );
   }


   private AssemblyKey createAircraftAssemblyWithAcftTrkPartAndEnginePart( PartNoKey acftTrkPart,
         PartNoKey enginePart ) {

      final PartNoKey aircraftPart = Domain.createPart();
      final PartNoKey aircraftSysPart = Domain.createPart();

      return Domain.createAircraftAssembly( aircraftAssembly -> {
         aircraftAssembly.setRootConfigurationSlot( rootConfigSlot -> {

            rootConfigSlot.addPartGroup( rootConfigSlotPartGroup -> {
               rootConfigSlotPartGroup.setInventoryClass( RefInvClassKey.ACFT );
               rootConfigSlotPartGroup.addPart( aircraftPart );
            } );

            rootConfigSlot.addConfigurationSlot( sysConfigSlot -> {
               sysConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SYS );
               sysConfigSlot.setCode( ACFT_SYS_CS_CD );
               sysConfigSlot.addPartGroup( sysConfigSlotPartGroup -> {
                  sysConfigSlotPartGroup.setInventoryClass( RefInvClassKey.SYS );
                  sysConfigSlotPartGroup.addPart( aircraftSysPart );
               } );

               sysConfigSlot.addConfigurationSlot( trkConfigSlot -> {
                  trkConfigSlot.setConfigurationSlotClass( RefBOMClassKey.TRK );
                  trkConfigSlot.setCode( ACFT_TRK_CS_CD );
                  trkConfigSlot.addPartGroup( trkConfigSlotPartGroup -> {
                     trkConfigSlotPartGroup.setInventoryClass( RefInvClassKey.TRK );
                     trkConfigSlotPartGroup.addPart( acftTrkPart );
                  } );
               } );

            } );

            rootConfigSlot.addConfigurationSlot( sysConfigSlot -> {
               sysConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SYS );
               sysConfigSlot.setCode( ACFT_SYS_CS_CD_2 );
               sysConfigSlot.addConfigurationSlot( subAssyConfigSlot -> {
                  subAssyConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SUBASSY );
                  subAssyConfigSlot.setCode( ACFT_SA_CS_CD );
                  subAssyConfigSlot.addPartGroup( subAssyConfigSlotPartGroup -> {
                     subAssyConfigSlotPartGroup.setInventoryClass( RefInvClassKey.ASSY );
                     subAssyConfigSlotPartGroup.addPart( enginePart );
                  } );
               } );

            } );

         } );
      } );
   }

}
