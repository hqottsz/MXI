package com.mxi.mx.core.services.fault;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.collections.SetUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.AxonDomainEventDao;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParametersStub;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.common.utils.FormatUtil;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.FailDeferRefKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.RefTaskDepActionKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.production.task.domain.TaskCreatedEvent;
import com.mxi.mx.core.table.eqp.EqpAssmblPos;
import com.mxi.mx.core.table.task.TaskDefnDao;
import com.mxi.mx.core.table.task.TaskTaskTable;


/**
 * This class tests MxFaultDetailsService.
 */
@RunWith( BlockJUnit4ClassRunner.class )

public class MxFaultDetailsServiceTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private MxFaultDetailsService iMxFaultDetailsService;

   private TaskDefnDao iTaskDefnDao;

   private static final String ENGINECONFIGSLOT_CODE = "engine_configslot";
   private static final String TRACKED_COMPONENT_CODE = "tracked_component";
   private static final double DEFAULT_FC_MODEL_RATE = 2.252363464d;

   private static final AxonDomainEventDao axonDomainEventDao = new AxonDomainEventDao();


   public void configurationSetup() {
      GlobalParametersStub lConfigParms = new GlobalParametersStub( "LOGIC" );
      lConfigParms.setBoolean( "SPEC2000_UPPERCASE_ASSMBL_CD", false );
      lConfigParms.setBoolean( "SPEC2000_UPPERCASE_BOM_PART_CD", false );
      lConfigParms.setBoolean( "SPEC2000_UPPERCASE_TASK_CD", false );

      iTaskDefnDao = InjectorContainer.get().getInstance( TaskDefnDao.class );
      iMxFaultDetailsService = new MxFaultDetailsService();
   }


   /**
    * This test case is testing if the method is getting follow up task of a deferral reference for
    * a fault for aircraft.
    *
    * <pre>
    *    Given an aircraft assembly.
    *    And an engine assembly.
    *    And inventories for the assemblies.
    *    And fault on the engine with deferral reference with follow up task on aircraft.
    *
    *    When get the follow up tasks by aircraft inventory keys.
    *
    *    Then verify the follow up task will be returned.
    * </pre>
    */
   @Test
   public void itGetsSetsOfFollowUpTasksOnAircraft() {

      this.configurationSetup();

      final PartNoKey partNo = Domain.createPart();
      final AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly( aircraftAssembly -> {
         aircraftAssembly.setRootConfigurationSlot( rootConfigSlot -> {
            rootConfigSlot.addConfigurationSlot( subConfigSlot -> {
               subConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SUBASSY );
               subConfigSlot.setCode( ENGINECONFIGSLOT_CODE );
               subConfigSlot.addPartGroup( partGroup -> {
                  partGroup.addPart( partNo );
               } );
            } );
         } );
      } );
      final AssemblyKey engineAssemblyKey = Domain.createEngineAssembly( engineAssembly -> {
         engineAssembly.setRootConfigurationSlot( rootConfigSlot -> {
            rootConfigSlot.addPartGroup( partGroup -> {
               partGroup.addPart( partNo );
            } );
         } );
      } );

      final ConfigSlotKey rootConfigSlot = Domain.readRootConfigurationSlot( aircraftAssemblyKey );

      final ConfigSlotKey subConfigSlot =
            Domain.readSubConfigurationSlot( rootConfigSlot, ENGINECONFIGSLOT_CODE );

      final InventoryKey engineInventory = Domain.createEngine( engine -> {
         engine.setAssembly( engineAssemblyKey );
         engine.setPartNumber( partNo );
         engine.setPosition( new ConfigSlotPositionKey( subConfigSlot, 1 ) );
      } );

      Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.addEngine( engineInventory );
      } );

      TaskTaskKey taskTaskKey = Domain.createRequirementDefinition( requirementDefinition -> {
         requirementDefinition.setCode( "FOLLOW_UP_TASK" );
         requirementDefinition.againstConfigurationSlot( rootConfigSlot );
      } );

      String recurringInspection = FormatUtil.formatUniqueIdRemoveHyphens( iTaskDefnDao
            .findByPrimaryKey( TaskTaskTable.findByPrimaryKey( taskTaskKey ).getTaskDefn() )
            .getAlternateKey().toString() );
      List<String> recurringInspections = new ArrayList<>();
      recurringInspections.add( recurringInspection );

      final FailDeferRefKey failDeferRefKey = Domain.createDeferralReference( deferralReference -> {
         deferralReference.setAssemblyKey( engineAssemblyKey );
         deferralReference.setRecurringInspections( recurringInspections );
      } );

      final FaultKey faultKey = Domain.createFault( fault -> {
         fault.setCurrentDeferralReference( failDeferRefKey );
         fault.setFailedSystem( subConfigSlot );
         fault.setInventory( engineInventory );
      } );
      HashSet<TaskTaskKey> followUpTaskTaskKeys = iMxFaultDetailsService
            .getFollowUpTasksForDeferralReferenceForAircraft( faultKey, engineInventory );

      assertEquals( "The follow up tasks are not retrieved properly.", taskTaskKey,
            followUpTaskTaskKeys.iterator().next() );
   }


   /**
    * This test case is testing if the method is getting follow up task of a deferral reference for
    * a fault for engine.
    *
    * <pre>
    *    Given an aircraft assembly.
    *    And an engine assembly.
    *    And inventories for the assemblies.
    *    And fault on the engine with deferral reference with follow up task on the engine.
    *
    *    When get the follow up tasks by engine inventory keys.
    *
    *    Then verify the follow up task will be returned.
    * </pre>
    */
   @Test
   public void itGetsSetsOfFollowUpTasksOnEngine() {

      this.configurationSetup();

      final PartNoKey partNo = Domain.createPart();
      final AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly();
      final AssemblyKey engineAssemblyKey = Domain.createEngineAssembly( engineAssembly -> {
         engineAssembly.setRootConfigurationSlot( rootConfigSlot -> {
            rootConfigSlot.setRootAssembly( aircraftAssemblyKey );
            rootConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SUBASSY );
         } );
      } );

      final ConfigSlotKey engineConfigSlot = Domain.readRootConfigurationSlot( engineAssemblyKey );

      final InventoryKey engineInventory = Domain.createEngine( engine -> {
         engine.setAssembly( engineAssemblyKey );
         engine.setPartNumber( partNo );
         engine.setPosition( new ConfigSlotPositionKey( engineConfigSlot, 1 ) );
      } );

      Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.addEngine( engineInventory );
      } );

      TaskTaskKey taskTaskKey = Domain.createRequirementDefinition( requirementDefinition -> {
         requirementDefinition.setCode( "FOLLOW_UP_TASK" );
         requirementDefinition.againstConfigurationSlot( engineConfigSlot );
      } );

      String recurringInspection = FormatUtil.formatUniqueIdRemoveHyphens( iTaskDefnDao
            .findByPrimaryKey( TaskTaskTable.findByPrimaryKey( taskTaskKey ).getTaskDefn() )
            .getAlternateKey().toString() );
      List<String> recurringInspections = new ArrayList<>();
      recurringInspections.add( recurringInspection );

      final FailDeferRefKey failDeferRefKey = Domain.createDeferralReference( deferralReference -> {
         deferralReference.setAssemblyKey( engineAssemblyKey );
         deferralReference.setRecurringInspections( recurringInspections );
      } );

      final FaultKey faultKey = Domain.createFault( fault -> {
         fault.setCurrentDeferralReference( failDeferRefKey );
         fault.setFailedSystem( engineConfigSlot );
         fault.setInventory( engineInventory );
      } );
      HashSet<TaskTaskKey> followUpTaskTaskKeys = iMxFaultDetailsService
            .getFollowUpTasksForDeferralReferenceForSubAssembly( faultKey, engineInventory );

      assertEquals( "The follow up tasks are not retrieved properly.", taskTaskKey,
            followUpTaskTaskKeys.iterator().next() );
   }


   /**
    * This test case is testing if the method is getting follow up task of a deferral reference for
    * a fault for tracked component.
    *
    * <pre>
    *    Given an aircraft assembly.
    *    And an engine assembly.
    *    And a track configslot under the engine.
    *    And inventories for the assemblies and track component.
    *    And fault on the engine with deferral reference with follow up task on the track component.
    *
    *    When get the follow up tasks by track component inventory keys.
    *
    *    Then verify the follow up task will be returned.
    * </pre>
    */
   @Test
   public void itGetsSetsOfFollowUpTasksOnTrackComponent() {

      this.configurationSetup();

      final PartNoKey partNo = Domain.createPart();
      final AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly();
      final AssemblyKey engineAssemblyKey = Domain.createEngineAssembly( engineAssembly -> {
         engineAssembly.setRootConfigurationSlot( rootConfigSlot -> {
            rootConfigSlot.setRootAssembly( aircraftAssemblyKey );
            rootConfigSlot.setCode( ENGINECONFIGSLOT_CODE );
            rootConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SUBASSY );
            rootConfigSlot.addConfigurationSlot( trackConfigSlot -> {
               trackConfigSlot.setConfigurationSlotClass( RefBOMClassKey.TRK );
               trackConfigSlot.setCode( TRACKED_COMPONENT_CODE );
            } );
         } );
      } );

      final ConfigSlotKey engineConfigSlot = Domain.readRootConfigurationSlot( engineAssemblyKey );

      final ConfigSlotKey trackedConfigSlot =
            Domain.readSubConfigurationSlot( engineConfigSlot, TRACKED_COMPONENT_CODE );

      final InventoryKey engineInventory = Domain.createEngine( engine -> {
         engine.setAssembly( engineAssemblyKey );
         engine.setPartNumber( partNo );
         engine.setPosition( new ConfigSlotPositionKey( engineConfigSlot, 1 ) );
      } );

      final ConfigSlotPositionKey trkConfigSlotPositionKey = new ConfigSlotPositionKey(
            trackedConfigSlot, EqpAssmblPos.getFirstPosId( trackedConfigSlot ) );

      final String trrackedPositionCd = EqpAssmblPos.getPosCd( trkConfigSlotPositionKey );

      final InventoryKey trackedComponent = Domain.createTrackedInventory( trackedInventory -> {
         trackedInventory.setOriginalAssembly( engineAssemblyKey );
         trackedInventory.setParent( engineInventory );
         trackedInventory.setLastKnownConfigSlot( "ENG", TRACKED_COMPONENT_CODE,
               trrackedPositionCd );
      } );

      Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.addEngine( engineInventory );
      } );

      TaskTaskKey taskTaskKey = Domain.createRequirementDefinition( requirementDefinition -> {
         requirementDefinition.setCode( "FOLLOW_UP_TASK" );
         requirementDefinition.againstConfigurationSlot( trackedConfigSlot );
      } );

      String recurringInspection = FormatUtil.formatUniqueIdRemoveHyphens( iTaskDefnDao
            .findByPrimaryKey( TaskTaskTable.findByPrimaryKey( taskTaskKey ).getTaskDefn() )
            .getAlternateKey().toString() );
      List<String> recurringInspections = new ArrayList<>();
      recurringInspections.add( recurringInspection );

      final FailDeferRefKey failDeferRefKey = Domain.createDeferralReference( deferralReference -> {
         deferralReference.setAssemblyKey( engineAssemblyKey );
         deferralReference.setRecurringInspections( recurringInspections );
      } );

      final FaultKey faultKey = Domain.createFault( fault -> {
         fault.setCurrentDeferralReference( failDeferRefKey );
         fault.setFailedSystem( engineConfigSlot );
         fault.setInventory( engineInventory );
      } );
      HashSet<TaskTaskKey> followUpTaskTaskKeys = iMxFaultDetailsService
            .getFollowUpTasksForDeferralReferenceForTrack( faultKey, trackedComponent );

      assertEquals( "The follow up tasks are not retrieved properly.", taskTaskKey,
            followUpTaskTaskKeys.iterator().next() );
   }


   private static final String ACFT_SYS_CS_CD = "ASCSCD";
   private static final String ENG_SYS_CS_CD = "ESCSCD";
   private static final String ACFT_SYS_CS_CD_2 = "ASCSCD2";
   private static final String ACFT_TRK_CS_CD = "ATCSCD";
   private static final String ENG_TRK_CS_CD = "ETCSCD";
   private static final String ACFT_SA_CS_CD = "ASACSCD";
   private static final String ACFT_SYSTEM = "ACFT_SYSTEM";


   @Test
   public void
         itGetsRepairReferenceFollowOnDefinitionsForApplicableInventoryForAcftRootAndAcftSys() {

      // Data setup
      final PartNoKey aircraftTrkPart = Domain.createPart();
      final PartNoKey engineTrkPart = Domain.createPart();;
      final PartNoKey enginePart = Domain.createPart();;

      final AssemblyKey aircraftAssemblyKey =
            this.createAircraftAssemblyWithAcftTrkPartAndEnginePart( aircraftTrkPart, enginePart );
      final InventoryKey aircraftInventoryKey = this.createAircraftInventory( aircraftAssemblyKey );
      final AssemblyKey engineAssemblyKey =
            this.createEngineAssemblyWithEngTrkPart( enginePart, engineTrkPart );

      final ConfigSlotKey aircraftRootConfigSlot =
            Domain.readRootConfigurationSlot( aircraftAssemblyKey );
      final ConfigSlotKey aircraftSysConfigSlot =
            Domain.readSubConfigurationSlot( aircraftRootConfigSlot, ACFT_SYS_CS_CD );
      final ConfigSlotKey aircraftTrkConfigSlot =
            Domain.readSubConfigurationSlot( aircraftRootConfigSlot, ACFT_TRK_CS_CD );
      final ConfigSlotKey aircraftSubAssyConfigSlot =
            Domain.readSubConfigurationSlot( aircraftRootConfigSlot, ACFT_SA_CS_CD );
      final ConfigSlotKey engineRootConfigSlot =
            Domain.readRootConfigurationSlot( engineAssemblyKey );
      final ConfigSlotKey engineTrkConfigSlot =
            Domain.readSubConfigurationSlot( engineRootConfigSlot, ENG_TRK_CS_CD );

      final TaskTaskKey acftRootTask =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
               requirementDefinition.againstConfigurationSlot( aircraftRootConfigSlot );
            } );
      final TaskTaskKey acftSysTask = Domain.createRequirementDefinition( requirementDefinition -> {
         requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
         requirementDefinition.againstConfigurationSlot( aircraftSysConfigSlot );
      } );
      final TaskTaskKey acftTrkTask = Domain.createRequirementDefinition( requirementDefinition -> {
         requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
         requirementDefinition.againstConfigurationSlot( aircraftTrkConfigSlot );
      } );
      final TaskTaskKey engTrkTask = Domain.createRequirementDefinition( requirementDefinition -> {
         requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
         requirementDefinition.againstConfigurationSlot( engineTrkConfigSlot );
      } );
      final TaskTaskKey engRootTask = Domain.createRequirementDefinition( requirementDefinition -> {
         requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
         requirementDefinition.againstConfigurationSlot( engineRootConfigSlot );
      } );
      final TaskTaskKey acftSubAssyTask =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
               requirementDefinition.againstConfigurationSlot( aircraftSubAssyConfigSlot );
            } );

      final TaskTaskKey reqTask = Domain.createRequirementDefinition( requirementDefinition -> {
         requirementDefinition.setTaskClass( RefTaskClassKey.REQ );
         requirementDefinition.againstConfigurationSlot( aircraftSubAssyConfigSlot );
      } );

      final TaskTaskKey applicabilityTask =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
               requirementDefinition.againstConfigurationSlot( aircraftSubAssyConfigSlot );
               requirementDefinition.setApplicabilityRule( "APPLICABILITY_RULE" );
            } );

      final TaskTaskKey nonCRTFollowTask =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
               requirementDefinition.againstConfigurationSlot( aircraftRootConfigSlot );
            } );

      final TaskTaskKey repRefTaskDefinition =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.setTaskClass( RefTaskClassKey.REPREF );
               requirementDefinition.againstConfigurationSlot( aircraftRootConfigSlot );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT,
                     acftRootTask );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT,
                     acftSysTask );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT,
                     acftTrkTask );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT,
                     acftSubAssyTask );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT,
                     engTrkTask );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT,
                     engRootTask );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT, reqTask );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT,
                     applicabilityTask );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.COMPLETE,
                     nonCRTFollowTask );
            } );

      // Execute
      final HashSet<TaskTaskKey> followOnTaskDefinitionsReturnedFromQuery =
            new MxFaultDetailsService().getRepairReferenceFollowOnDefinitionsForApplicableInventory(
                  repRefTaskDefinition, aircraftInventoryKey );

      final HashSet<TaskTaskKey> expectedFollowOnTaskDefinitionResults =
            new HashSet<TaskTaskKey>( Arrays.asList( acftRootTask, acftSysTask ) );

      Assert.assertThat( "Expected task definitions were not returned.",
            SetUtils.isEqualSet( followOnTaskDefinitionsReturnedFromQuery,
                  expectedFollowOnTaskDefinitionResults ),
            is( true ) );

   }


   @Test
   public void itGetsRepairReferenceFollowOnDefinitionsForAcftTrkInventory() {

      // Data setup
      final PartNoKey aircraftTrkPart = Domain.createPart();
      final PartNoKey engineTrkPart = Domain.createPart();;
      final PartNoKey enginePart = Domain.createPart();;

      final AssemblyKey aircraftAssemblyKey =
            this.createAircraftAssemblyWithAcftTrkPartAndEnginePart( aircraftTrkPart, enginePart );
      final InventoryKey aircraftInventoryKey = this.createAircraftInventory( aircraftAssemblyKey );
      final AssemblyKey engineAssemblyKey =
            this.createEngineAssemblyWithEngTrkPart( enginePart, engineTrkPart );

      final ConfigSlotKey aircraftRootConfigSlot =
            Domain.readRootConfigurationSlot( aircraftAssemblyKey );
      final ConfigSlotKey aircraftSysConfigSlot =
            Domain.readSubConfigurationSlot( aircraftRootConfigSlot, ACFT_SYS_CS_CD );
      final ConfigSlotKey aircraftTrkConfigSlot =
            Domain.readSubConfigurationSlot( aircraftRootConfigSlot, ACFT_TRK_CS_CD );
      final ConfigSlotKey aircraftSubAssyConfigSlot =
            Domain.readSubConfigurationSlot( aircraftRootConfigSlot, ACFT_SA_CS_CD );
      final ConfigSlotKey engineRootConfigSlot =
            Domain.readRootConfigurationSlot( engineAssemblyKey );
      final ConfigSlotKey engineTrkConfigSlot =
            Domain.readSubConfigurationSlot( engineRootConfigSlot, ENG_TRK_CS_CD );

      final TaskTaskKey acftRootTask =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
               requirementDefinition.againstConfigurationSlot( aircraftRootConfigSlot );
            } );
      final TaskTaskKey acftSysTask = Domain.createRequirementDefinition( requirementDefinition -> {
         requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
         requirementDefinition.againstConfigurationSlot( aircraftSysConfigSlot );
      } );
      final TaskTaskKey acftTrkTask = Domain.createRequirementDefinition( requirementDefinition -> {
         requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
         requirementDefinition.againstConfigurationSlot( aircraftTrkConfigSlot );
      } );
      final TaskTaskKey engTrkTask = Domain.createRequirementDefinition( requirementDefinition -> {
         requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
         requirementDefinition.againstConfigurationSlot( engineTrkConfigSlot );
      } );
      final TaskTaskKey engRootTask = Domain.createRequirementDefinition( requirementDefinition -> {
         requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
         requirementDefinition.againstConfigurationSlot( engineRootConfigSlot );
      } );
      final TaskTaskKey acftSubAssyTask =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
               requirementDefinition.againstConfigurationSlot( aircraftSubAssyConfigSlot );
            } );

      final TaskTaskKey reqTask = Domain.createRequirementDefinition( requirementDefinition -> {
         requirementDefinition.setTaskClass( RefTaskClassKey.REQ );
         requirementDefinition.againstConfigurationSlot( aircraftSubAssyConfigSlot );
      } );

      final TaskTaskKey applicabilityTask =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
               requirementDefinition.againstConfigurationSlot( aircraftSubAssyConfigSlot );
               requirementDefinition.setApplicabilityRule( "APPLICABILITY_RULE" );
            } );

      final TaskTaskKey nonCRTFollowTask =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
               requirementDefinition.againstConfigurationSlot( aircraftRootConfigSlot );
            } );

      final TaskTaskKey repRefTaskDefinition =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.setTaskClass( RefTaskClassKey.REPREF );
               requirementDefinition.againstConfigurationSlot( aircraftRootConfigSlot );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT,
                     acftRootTask );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT,
                     acftSysTask );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT,
                     acftTrkTask );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT,
                     acftSubAssyTask );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT,
                     engTrkTask );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT,
                     engRootTask );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT, reqTask );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT,
                     applicabilityTask );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.COMPLETE,
                     nonCRTFollowTask );
            } );

      final ConfigSlotPositionKey trkConfigSlotPositionKey = new ConfigSlotPositionKey(
            aircraftTrkConfigSlot, EqpAssmblPos.getFirstPosId( aircraftTrkConfigSlot ) );
      final String trkPosCd = EqpAssmblPos.getPosCd( trkConfigSlotPositionKey );

      final InventoryKey aircraftTrackedInventory =
            Domain.createTrackedInventory( trackedInventory -> {
               trackedInventory.setPartNumber( aircraftTrkPart );
               trackedInventory.setParent( aircraftInventoryKey );
               trackedInventory.setLastKnownConfigSlot( trkConfigSlotPositionKey.getCd(),
                     ACFT_TRK_CS_CD, trkPosCd );
            } );

      // Execute
      final HashSet<TaskTaskKey> followOnTaskDefinitionsReturnedFromQuery =
            new MxFaultDetailsService()
                  .getRepairReferenceFollowOnDefinitionsForDamageRecordInventory(
                        repRefTaskDefinition, aircraftTrackedInventory );

      final HashSet<TaskTaskKey> expectedFollowOnTaskDefinitionResults =
            new HashSet<TaskTaskKey>( Arrays.asList( acftTrkTask ) );

      Assert.assertThat( "Expected task definitions were not returned.",
            SetUtils.isEqualSet( followOnTaskDefinitionsReturnedFromQuery,
                  expectedFollowOnTaskDefinitionResults ),
            is( true ) );

   }


   @Test
   public void itGetsRepairReferenceFollowOnDefinitionsForEngTrkInventory() {

      // Data setup
      final PartNoKey aircraftTrkPart = Domain.createPart();
      final PartNoKey engineTrkPart = Domain.createPart();;
      final PartNoKey enginePart = Domain.createPart();;

      final AssemblyKey aircraftAssemblyKey =
            this.createAircraftAssemblyWithAcftTrkPartAndEnginePart( aircraftTrkPart, enginePart );
      final InventoryKey aircraftInventoryKey = this.createAircraftInventory( aircraftAssemblyKey );
      final AssemblyKey engineAssemblyKey =
            this.createEngineAssemblyWithEngTrkPart( enginePart, engineTrkPart );

      final ConfigSlotKey aircraftRootConfigSlot =
            Domain.readRootConfigurationSlot( aircraftAssemblyKey );
      final ConfigSlotKey aircraftSysConfigSlot =
            Domain.readSubConfigurationSlot( aircraftRootConfigSlot, ACFT_SYS_CS_CD );
      final ConfigSlotKey aircraftTrkConfigSlot =
            Domain.readSubConfigurationSlot( aircraftRootConfigSlot, ACFT_TRK_CS_CD );
      final ConfigSlotKey aircraftSubAssyConfigSlot =
            Domain.readSubConfigurationSlot( aircraftRootConfigSlot, ACFT_SA_CS_CD );
      final ConfigSlotKey engineRootConfigSlot =
            Domain.readRootConfigurationSlot( engineAssemblyKey );
      final ConfigSlotKey engineTrkConfigSlot =
            Domain.readSubConfigurationSlot( engineRootConfigSlot, ENG_TRK_CS_CD );

      final TaskTaskKey acftRootTask =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
               requirementDefinition.againstConfigurationSlot( aircraftRootConfigSlot );
            } );
      final TaskTaskKey acftSysTask = Domain.createRequirementDefinition( requirementDefinition -> {
         requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
         requirementDefinition.againstConfigurationSlot( aircraftSysConfigSlot );
      } );
      final TaskTaskKey acftTrkTask = Domain.createRequirementDefinition( requirementDefinition -> {
         requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
         requirementDefinition.againstConfigurationSlot( aircraftTrkConfigSlot );
      } );
      final TaskTaskKey engTrkTask = Domain.createRequirementDefinition( requirementDefinition -> {
         requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
         requirementDefinition.againstConfigurationSlot( engineTrkConfigSlot );
      } );
      final TaskTaskKey engRootTask = Domain.createRequirementDefinition( requirementDefinition -> {
         requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
         requirementDefinition.againstConfigurationSlot( engineRootConfigSlot );
      } );
      final TaskTaskKey acftSubAssyTask =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
               requirementDefinition.againstConfigurationSlot( aircraftSubAssyConfigSlot );
            } );

      final TaskTaskKey reqTask = Domain.createRequirementDefinition( requirementDefinition -> {
         requirementDefinition.setTaskClass( RefTaskClassKey.REQ );
         requirementDefinition.againstConfigurationSlot( aircraftSubAssyConfigSlot );
      } );

      final TaskTaskKey applicabilityTask =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
               requirementDefinition.againstConfigurationSlot( aircraftSubAssyConfigSlot );
               requirementDefinition.setApplicabilityRule( "APPLICABILITY_RULE" );
            } );

      final TaskTaskKey nonCRTFollowTask =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
               requirementDefinition.againstConfigurationSlot( aircraftRootConfigSlot );
            } );

      final TaskTaskKey repRefTaskDefinition =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.setTaskClass( RefTaskClassKey.REPREF );
               requirementDefinition.againstConfigurationSlot( aircraftRootConfigSlot );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT,
                     acftRootTask );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT,
                     acftSysTask );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT,
                     acftTrkTask );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT,
                     acftSubAssyTask );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT,
                     engTrkTask );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT,
                     engRootTask );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT, reqTask );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT,
                     applicabilityTask );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.COMPLETE,
                     nonCRTFollowTask );
            } );

      final InventoryKey engineInventoryKey = Domain.createEngine( engine -> {
         engine.setAssembly( aircraftAssemblyKey );
         engine.setParent( aircraftInventoryKey );
         engine.setOriginalAssembly( engineAssemblyKey );
      } );

      final ConfigSlotPositionKey trkConfigSlotPosition = new ConfigSlotPositionKey(
            engineTrkConfigSlot, EqpAssmblPos.getFirstPosId( engineTrkConfigSlot ) );
      final String trkPosCd = EqpAssmblPos.getPosCd( trkConfigSlotPosition );

      final InventoryKey engineTrackedInventory =
            Domain.createTrackedInventory( aTrackedInventory -> {
               aTrackedInventory.setPartNumber( engineTrkPart );
               aTrackedInventory.setParent( engineInventoryKey );
               aTrackedInventory.setLastKnownConfigSlot( trkConfigSlotPosition.getCd(),
                     ENG_TRK_CS_CD, trkPosCd );
            } );

      // Execute
      final HashSet<TaskTaskKey> followOnTaskDefinitionsReturnedFromQuery =
            new MxFaultDetailsService()
                  .getRepairReferenceFollowOnDefinitionsForDamageRecordInventory(
                        repRefTaskDefinition, engineTrackedInventory );

      final HashSet<TaskTaskKey> expectedFollowOnTaskDefinitionResults =
            new HashSet<TaskTaskKey>( Arrays.asList( engTrkTask ) );

      Assert.assertThat( "Expected task definitions were not returned.",
            SetUtils.isEqualSet( followOnTaskDefinitionsReturnedFromQuery,
                  expectedFollowOnTaskDefinitionResults ),
            is( true ) );

   }


   @Test
   public void itGetsetRepairReferenceFollowOnDefinitionsForAcftSubAssyAndEngRootInventory() {

      // Data setup
      final PartNoKey aircraftTrkPart = Domain.createPart();
      final PartNoKey engineTrkPart = Domain.createPart();;
      final PartNoKey enginePart = Domain.createPart();;

      final AssemblyKey aircraftAssemblyKey =
            this.createAircraftAssemblyWithAcftTrkPartAndEnginePart( aircraftTrkPart, enginePart );
      final InventoryKey aircraftInventoryKey = this.createAircraftInventory( aircraftAssemblyKey );
      final AssemblyKey engineAssemblyKey =
            this.createEngineAssemblyWithEngTrkPart( enginePart, engineTrkPart );

      final ConfigSlotKey aircraftRootConfigSlot =
            Domain.readRootConfigurationSlot( aircraftAssemblyKey );
      final ConfigSlotKey aircraftSysConfigSlot =
            Domain.readSubConfigurationSlot( aircraftRootConfigSlot, ACFT_SYS_CS_CD );
      final ConfigSlotKey aircraftTrkConfigSlot =
            Domain.readSubConfigurationSlot( aircraftRootConfigSlot, ACFT_TRK_CS_CD );
      final ConfigSlotKey aircraftSubAssyConfigSlot =
            Domain.readSubConfigurationSlot( aircraftRootConfigSlot, ACFT_SA_CS_CD );
      final ConfigSlotKey engineRootConfigSlot =
            Domain.readRootConfigurationSlot( engineAssemblyKey );
      final ConfigSlotKey engineTrkConfigSlot =
            Domain.readSubConfigurationSlot( engineRootConfigSlot, ENG_TRK_CS_CD );

      final TaskTaskKey acftRootTask =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
               requirementDefinition.againstConfigurationSlot( aircraftRootConfigSlot );
            } );
      final TaskTaskKey acftSysTask = Domain.createRequirementDefinition( requirementDefinition -> {
         requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
         requirementDefinition.againstConfigurationSlot( aircraftSysConfigSlot );
      } );
      final TaskTaskKey acftTrkTask = Domain.createRequirementDefinition( requirementDefinition -> {
         requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
         requirementDefinition.againstConfigurationSlot( aircraftTrkConfigSlot );
      } );
      final TaskTaskKey engTrkTask = Domain.createRequirementDefinition( requirementDefinition -> {
         requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
         requirementDefinition.againstConfigurationSlot( engineTrkConfigSlot );
      } );
      final TaskTaskKey engRootTask = Domain.createRequirementDefinition( requirementDefinition -> {
         requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
         requirementDefinition.againstConfigurationSlot( engineRootConfigSlot );
      } );
      final TaskTaskKey acftSubAssyTask =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
               requirementDefinition.againstConfigurationSlot( aircraftSubAssyConfigSlot );
            } );

      final TaskTaskKey reqTask = Domain.createRequirementDefinition( requirementDefinition -> {
         requirementDefinition.setTaskClass( RefTaskClassKey.REQ );
         requirementDefinition.againstConfigurationSlot( aircraftSubAssyConfigSlot );
      } );

      final TaskTaskKey applicabilityTask =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
               requirementDefinition.againstConfigurationSlot( aircraftSubAssyConfigSlot );
               requirementDefinition.setApplicabilityRule( "APPLICABILITY_RULE" );
            } );

      final TaskTaskKey nonCRTFollowTask =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
               requirementDefinition.againstConfigurationSlot( aircraftRootConfigSlot );
            } );

      final TaskTaskKey repRefTaskDefinition =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.setTaskClass( RefTaskClassKey.REPREF );
               requirementDefinition.againstConfigurationSlot( aircraftRootConfigSlot );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT,
                     acftRootTask );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT,
                     acftSysTask );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT,
                     acftTrkTask );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT,
                     acftSubAssyTask );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT,
                     engTrkTask );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT,
                     engRootTask );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT, reqTask );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT,
                     applicabilityTask );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.COMPLETE,
                     nonCRTFollowTask );
            } );

      final InventoryKey engineInventoryKey = Domain.createEngine( engine -> {
         engine.setPartNumber( enginePart );
         engine.setParent( aircraftInventoryKey );
         engine.setOriginalAssembly( engineAssemblyKey );
         engine.setPosition( new ConfigSlotPositionKey( aircraftSubAssyConfigSlot, 1 ) );
      } );

      // Execute
      final HashSet<TaskTaskKey> followOnTaskDefinitionsReturnedFromQuery =
            new MxFaultDetailsService()
                  .getRepairReferenceFollowOnDefinitionsForDamageRecordSubAssemblyInventory(
                        repRefTaskDefinition, engineInventoryKey );

      final HashSet<TaskTaskKey> expectedFollowOnTaskDefinitionResults =
            new HashSet<TaskTaskKey>( Arrays.asList( engRootTask, acftSubAssyTask ) );

      Assert.assertThat( "Expected task definitions were not returned.",
            SetUtils.isEqualSet( followOnTaskDefinitionsReturnedFromQuery,
                  expectedFollowOnTaskDefinitionResults ),
            is( true ) );

   }


   @Test
   public void
         whenTaskIsCreatedAgainstFollowOnRequirementWithSchedulingRuleOnHistoricFaultThenTaskCreatedEventIsEmitted()
               throws MxException, TriggerException {
      // Given
      final HumanResourceKey lAuthHr = Domain.createHumanResource();

      final PartNoKey lAcftPart = Domain.createPart();

      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly(
            aAircraftAssembly -> aAircraftAssembly.setRootConfigurationSlot(
                  aRootConfigSlot -> aRootConfigSlot.addPartGroup( aPartGroup -> {
                     aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                     aPartGroup.addPart( lAcftPart );
                  } ) ) );

      final ConfigSlotKey lCnfigSlot = Domain.readRootConfigurationSlot( lAircraftAssembly );

      final InventoryKey lAircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( lAircraftAssembly );
         aAircraft.setPart( lAcftPart );
         aAircraft.addUsage( DataTypeKey.HOURS, BigDecimal.ZERO );
         aAircraft.setForecastModel( Domain.createForecastModel( aForecastModel -> {
            aForecastModel.addRange( 1, 1, DataTypeKey.HOURS, DEFAULT_FC_MODEL_RATE );
         } ) );
      } );

      final TaskTaskKey lReqDefinition = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.againstConfigurationSlot( lCnfigSlot );
         aReqDefn.setRecurring( false );
         aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
         aReqDefn.setOnCondition( true );
         aReqDefn.setScheduledFromManufacturedDate();
         aReqDefn.addSchedulingRule( DataTypeKey.HOURS, BigDecimal.TEN );
         aReqDefn.setTaskClass( RefTaskClassKey.FOLLOW );
      } );

      TaskKey lCorrectiveTaskKey = Domain.createCorrectiveTask( aCorrectiveTask -> {
         aCorrectiveTask.setInventory( lAircraft );
         aCorrectiveTask.setStatus( RefEventStatusKey.COMPLETE );
      } );

      FaultKey lFaultKey = Domain.createFault( aFault -> {
         aFault.setInventory( lAircraft );
         aFault.setStatus( RefEventStatusKey.COMPLETE ); // CFCERT
         aFault.setCorrectiveTask( lCorrectiveTaskKey );
      } );

      // clean the specified table
      axonDomainEventDao.purgeAll();

      // When
      final TaskKey lTask = new MxFaultDetailsService().createFollowOnTaskFromDefinition(
            lCorrectiveTaskKey, lAircraftAssembly, lAircraft, lReqDefinition, lAuthHr );

      // Then
      final QuerySet querySet = axonDomainEventDao.findByPayLoadType( TaskCreatedEvent.class );
      assertEquals( "Unexpected number of events.", 1, querySet.getRowCount() );
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
}
