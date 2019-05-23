package com.mxi.mx.core.services.stask.creation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.configuration.axon.RecordingEventBus;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.production.task.domain.TaskAssignedToWorkPackageEvent;
import com.mxi.mx.core.table.org.OrgHr;
import com.mxi.mx.core.table.sched.JdbcSchedStaskDao;
import com.mxi.mx.core.table.sched.SchedStaskDao;


/**
 * Verifies the behaviour of {@link CreationService}
 *
 */
public class CreationService_TaskDefinitionTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private HumanResourceKey iAuthHr;

   private RecordingEventBus iEventBus;

   private int iUserId;

   private static final String REQ_DEFN_APPLICABILITY_RANGE = "100-150";
   private static final String ACFT_APPLICABILITY_CODE = "205";
   private static final TaskKey PK_FOR_NEW_TASK = null;
   private static final TaskKey PARENT_TASK = null;
   private static final boolean TRIGGER = true;
   private static final boolean IGNORE_APPLICABILITY = false;
   private static final boolean PREVENT_BY_CLASS_MODE = true;
   private static final Date PREVIOUS_COMPLETION_DATE = null;

   private SchedStaskDao iSchedStaskDao;


   /**
    * <pre>
    * Given - An aircraft against an assembly
    *         An on-condition requirement definition with prevent manual initialization set to false, against the same aircraft assembly.
    *         A block definition defined against the same aircraft assembly as the requirement definition, and the requirement is added to the block.
    * When the Block definition is initialized
    * Then the requirement is initialized
    * </pre>
    */
   @Test
   public void itInitializeTheRequirementDefinitionWhenPreventManualInitializationSetToFalse()
         throws Exception {

      // Given
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
      } );

      final TaskTaskKey lReqDefinition = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.setCode( "REQ" );
         aReqDefn.setOnCondition( true );
         aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );;
         aReqDefn.setPreventManualInitialization( false );
         aReqDefn.againstConfigurationSlot( lCnfigSlot );
      } );

      final TaskTaskKey lBlockDefinition = Domain.createBlockDefinition( aBlockDefn -> {
         aBlockDefn.setConfigurationSlot( lCnfigSlot );
         aBlockDefn.setOnCondition( true );
         aBlockDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
         aBlockDefn.addRequirementDefinition( lReqDefinition );
         aBlockDefn.setRecurring( false );
      } );

      // When
      final TaskKey lTask = new CreationService().createTaskFromDefinition( PK_FOR_NEW_TASK,
            lAircraft, lBlockDefinition, iAuthHr, PARENT_TASK, TRIGGER, IGNORE_APPLICABILITY,
            PREVENT_BY_CLASS_MODE, PREVIOUS_COMPLETION_DATE );

      // Then
      assertNotNull( "Unexpectedly, The Block was not initialized", lTask );

      TaskTaskKey lActualReqDefn = iSchedStaskDao.findByPrimaryKey( lTask ).getTaskTaskKey();
      assertEquals( "Unexpectedly, incorrect Block is initialized", lBlockDefinition,
            lActualReqDefn );

      List<TaskKey> lReqTasks = getActualTasks( lAircraft, lReqDefinition );
      assertEquals( "Incorrect number of requirement tasks initialized", 1, lReqTasks.size() );

   }


   /**
    * <pre>
    * Given - An aircraft against an assembly
    *         An on-condition requirement definition with prevent manual initialization set to false and in BUILD status, against the same aircraft assembly.
    *         A block definition defined against the same aircraft assembly as the requirement definition, and the requirement is added to the block.
    * When the Block definition is initialized
    * Then the Requirement is not initialized
    * </pre>
    */
   @Test
   public void itDoesNotInitializeTheRequirementDefinitionWhenTheRequirementInBuildStatus()
         throws Exception {

      // Given
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
      } );

      final TaskTaskKey lReqDefinition = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.setCode( "REQ" );
         aReqDefn.setOnCondition( true );
         aReqDefn.setStatus( RefTaskDefinitionStatusKey.BUILD );
         aReqDefn.setPreventManualInitialization( false );
         aReqDefn.againstConfigurationSlot( lCnfigSlot );
      } );

      final TaskTaskKey lBlockDefinition = Domain.createBlockDefinition( aBlockDefn -> {
         aBlockDefn.setConfigurationSlot( lCnfigSlot );
         aBlockDefn.setOnCondition( true );
         aBlockDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
         aBlockDefn.addRequirementDefinition( lReqDefinition );
         aBlockDefn.setRecurring( false );
      } );

      // When
      final TaskKey lTask = new CreationService().createTaskFromDefinition( PK_FOR_NEW_TASK,
            lAircraft, lBlockDefinition, iAuthHr, PARENT_TASK, TRIGGER, IGNORE_APPLICABILITY,
            PREVENT_BY_CLASS_MODE, PREVIOUS_COMPLETION_DATE );

      // Then
      assertNotNull( "Unexpectedly, The Block was not initialized", lTask );

      TaskTaskKey lActualReqDefn = iSchedStaskDao.findByPrimaryKey( lTask ).getTaskTaskKey();
      assertEquals( "Unexpectedly, incorrect Block is initialized", lBlockDefinition,
            lActualReqDefn );

      List<TaskKey> lReqTasks = getActualTasks( lAircraft, lReqDefinition );
      assertEquals( "Incorrect number of requirement tasks initialized", 0, lReqTasks.size() );

   }


   /**
    * <pre>
    * Given - An aircraft against an assembly
    *         An on-condition requirement definition with prevent manual initialization set to true, against the same aircraft assembly.
    *         A block definition defined against the same aircraft assembly as the requirement definition, and the requirement is added to the block.
    * When the Block definition is initialized
    * Then the Requirement is not initialized
    * </pre>
    */
   @Test
   public void
         itDoesNotInitializeTheRequirementDefinitionWhenPreventManualInitializationIsSetToTrue()
               throws Exception {

      // Given
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
      } );

      final TaskTaskKey lReqDefinition = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.setCode( "REQ" );
         aReqDefn.setOnCondition( true );
         aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
         aReqDefn.setPreventManualInitialization( true );
         aReqDefn.againstConfigurationSlot( lCnfigSlot );
      } );

      final TaskTaskKey lBlockDefinition = Domain.createBlockDefinition( aBlockDefn -> {
         aBlockDefn.setConfigurationSlot( lCnfigSlot );
         aBlockDefn.setOnCondition( true );
         aBlockDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
         aBlockDefn.addRequirementDefinition( lReqDefinition );
         aBlockDefn.setRecurring( false );
      } );

      // When
      final TaskKey lTask = new CreationService().createTaskFromDefinition( PK_FOR_NEW_TASK,
            lAircraft, lBlockDefinition, iAuthHr, PARENT_TASK, TRIGGER, IGNORE_APPLICABILITY,
            PREVENT_BY_CLASS_MODE, PREVIOUS_COMPLETION_DATE );

      // Then
      assertNotNull( "Unexpectedly, The Block was not initialized", lTask );

      TaskTaskKey lActualReqDefn = iSchedStaskDao.findByPrimaryKey( lTask ).getTaskTaskKey();
      assertEquals( "Unexpectedly, incorrect Block is initialized", lBlockDefinition,
            lActualReqDefn );

      List<TaskKey> lReqTasks = getActualTasks( lAircraft, lReqDefinition );
      assertEquals( "Incorrect number of requirement tasks initialized", 0, lReqTasks.size() );

   }


   /**
    * <pre>
    * Given - An aircraft against an assembly
    *         An on-condition requirement definition with prevent manual initialization set to false and in OBSOLETE status, against the same aircraft assembly.
    *         A block definition defined against the same aircraft assembly as the requirement definition, and the requirement is added to the block.
    * When the Block definition is initialized
    * Then the Requirement is not initialized
    * </pre>
    */
   @Test
   public void itDoesNotInitializeTheRequirementDefinitionWhenTheRequirementInObsoleteStatus()
         throws Exception {

      // Given
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
      } );

      final TaskTaskKey lReqDefinition = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.setCode( "REQ" );
         aReqDefn.setOnCondition( true );
         aReqDefn.setStatus( RefTaskDefinitionStatusKey.OBSOLETE );
         aReqDefn.setPreventManualInitialization( false );
         aReqDefn.againstConfigurationSlot( lCnfigSlot );
      } );

      final TaskTaskKey lBlockDefinition = Domain.createBlockDefinition( aBlockDefn -> {
         aBlockDefn.setConfigurationSlot( lCnfigSlot );
         aBlockDefn.setOnCondition( true );
         aBlockDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
         aBlockDefn.addRequirementDefinition( lReqDefinition );
         aBlockDefn.setRecurring( false );
      } );

      // When
      final TaskKey lTask = new CreationService().createTaskFromDefinition( PK_FOR_NEW_TASK,
            lAircraft, lBlockDefinition, iAuthHr, PARENT_TASK, TRIGGER, IGNORE_APPLICABILITY,
            PREVENT_BY_CLASS_MODE, PREVIOUS_COMPLETION_DATE );

      // Then
      assertNotNull( "Unexpectedly, The Block was not initialized", lTask );

      TaskTaskKey lActualReqDefn = iSchedStaskDao.findByPrimaryKey( lTask ).getTaskTaskKey();
      assertEquals( "Unexpectedly, incorrect Block is initialized", lBlockDefinition,
            lActualReqDefn );

      List<TaskKey> lReqTasks = getActualTasks( lAircraft, lReqDefinition );
      assertEquals( "Incorrect number of requirement tasks initialized", 0, lReqTasks.size() );

   }


   /**
    * <pre>
    * Given - An aircraft against an assembly
    *         A non on-condition requirement definition with prevent manual initialization set to false, against the same aircraft assembly.
    *         A block definition defined against the same aircraft assembly as the requirement definition, and the requirement is added to the block.
    * When the Block definition is initialized
    * Then the Requirement is not initialized
    * </pre>
    */
   @Test
   public void itDoesNotInitializeTheRequirementDefintionWhenTheRequirementIsNotOnCondition()
         throws Exception {

      // Given
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
      } );

      final TaskTaskKey lReqDefinition = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.setCode( "REQ" );
         aReqDefn.setOnCondition( false );
         aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
         aReqDefn.setPreventManualInitialization( false );
         aReqDefn.againstConfigurationSlot( lCnfigSlot );
      } );

      final TaskTaskKey lBlockDefinition = Domain.createBlockDefinition( aBlockDefn -> {
         aBlockDefn.setConfigurationSlot( lCnfigSlot );
         aBlockDefn.setOnCondition( true );
         aBlockDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
         aBlockDefn.addRequirementDefinition( lReqDefinition );
         aBlockDefn.setRecurring( false );
      } );

      // When
      final TaskKey lTask = new CreationService().createTaskFromDefinition( PK_FOR_NEW_TASK,
            lAircraft, lBlockDefinition, iAuthHr, PARENT_TASK, TRIGGER, IGNORE_APPLICABILITY,
            PREVENT_BY_CLASS_MODE, PREVIOUS_COMPLETION_DATE );

      // Then
      assertNotNull( "Unexpectedly, The Block was not initialized", lTask );

      TaskTaskKey lActualReqDefn = iSchedStaskDao.findByPrimaryKey( lTask ).getTaskTaskKey();
      assertEquals( "Unexpectedly, incorrect Block is initialized", lBlockDefinition,
            lActualReqDefn );

      List<TaskKey> lReqTasks = getActualTasks( lAircraft, lReqDefinition );
      assertEquals( "Incorrect number of requirement tasks initialized", 0, lReqTasks.size() );

   }


   /**
    * <pre>
    * Given - An on-condition requirement definition with prevent manual initialization set to false, against the an aircraft assembly with an applicability range.
    *         An aircraft against an assembly, with applicability code outside the applicability range of the requirement definition
    *         A block definition defined against the same aircraft assembly as the requirement definition, and the requirement is added to the block.
    * When the Block definition is initialized
    * Then the Requirement is not initialized
    * </pre>
    */
   @Test
   public void itDoesNotInitializeTheRequirementDefinitionWhenTheInventoryNotApplicable()
         throws Exception {

      // Given
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
         aAircraft.setApplicabilityCode( ACFT_APPLICABILITY_CODE );
      } );

      final TaskTaskKey lReqDefinition = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.setCode( "REQ" );
         aReqDefn.setOnCondition( true );
         aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
         aReqDefn.setPreventManualInitialization( false );
         aReqDefn.againstConfigurationSlot( lCnfigSlot );
         aReqDefn.setApplicabilityRange( REQ_DEFN_APPLICABILITY_RANGE );
      } );

      final TaskTaskKey lBlockDefinition = Domain.createBlockDefinition( aBlockDefn -> {
         aBlockDefn.setConfigurationSlot( lCnfigSlot );
         aBlockDefn.setOnCondition( true );
         aBlockDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
         aBlockDefn.addRequirementDefinition( lReqDefinition );
         aBlockDefn.setRecurring( false );
      } );

      // When
      final TaskKey lTask = new CreationService().createTaskFromDefinition( PK_FOR_NEW_TASK,
            lAircraft, lBlockDefinition, iAuthHr, PARENT_TASK, TRIGGER, IGNORE_APPLICABILITY,
            PREVENT_BY_CLASS_MODE, PREVIOUS_COMPLETION_DATE );

      // Then
      assertNotNull( "Unexpectedly, The Block was not initialized", lTask );

      TaskTaskKey lActualReqDefn = iSchedStaskDao.findByPrimaryKey( lTask ).getTaskTaskKey();
      assertEquals( "Unexpectedly, incorrect Block is initialized", lBlockDefinition,
            lActualReqDefn );

      List<TaskKey> lReqTasks = getActualTasks( lAircraft, lReqDefinition );
      assertEquals( "Incorrect number of requirement tasks initialized", 0, lReqTasks.size() );

   }


   @Test
   public void itDoesNotPublishTaskAssignedToWorkPackageEventWhenInitializeABlockWithRequirement()
         throws Exception {
      // ARRANGE
      final PartNoKey partNoKey = Domain.createPart();

      final AssemblyKey assemblyKey =
            Domain.createAircraftAssembly( assembly -> assembly.setRootConfigurationSlot(
                  aRootConfigSlot -> aRootConfigSlot.addPartGroup( aPartGroup -> {
                     aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                     aPartGroup.addPart( partNoKey );
                  } ) ) );

      final ConfigSlotKey configSlotKey = Domain.readRootConfigurationSlot( assemblyKey );

      final InventoryKey aircraftKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( assemblyKey );
         aircraft.setPart( partNoKey );
      } );

      final TaskTaskKey requirementDefinitionKey = Domain.createRequirementDefinition( reqDef -> {
         reqDef.setCode( "REQ" );
         reqDef.setOnCondition( true );
         reqDef.setStatus( RefTaskDefinitionStatusKey.ACTV );
         reqDef.setPreventManualInitialization( false );
         reqDef.againstConfigurationSlot( configSlotKey );
      } );

      final TaskTaskKey blockDefinitionKey = Domain.createBlockDefinition( blockDef -> {
         blockDef.setConfigurationSlot( configSlotKey );
         blockDef.setOnCondition( true );
         blockDef.setStatus( RefTaskDefinitionStatusKey.ACTV );
         blockDef.addRequirementDefinition( requirementDefinitionKey );
         blockDef.setRecurring( false );
      } );

      // When
      final TaskKey taskKey = new CreationService().createTaskFromDefinition( PK_FOR_NEW_TASK,
            aircraftKey, blockDefinitionKey, iAuthHr, PARENT_TASK, TRIGGER, IGNORE_APPLICABILITY,
            PREVENT_BY_CLASS_MODE, PREVIOUS_COMPLETION_DATE );

      // Then
      assertNotNull( "Unexpectedly, The Block was not initialized", taskKey );
      assertEquals( 0, iEventBus.getEventMessages().stream().filter( eventMessage -> {
         return ( eventMessage.getPayload() instanceof TaskAssignedToWorkPackageEvent );
      } ).collect( Collectors.toList() ).size() );

   }


   private List<TaskKey> getActualTasks( InventoryKey aInv, TaskTaskKey aTaskTaskKey ) {
      List<TaskKey> lTasks = new ArrayList<TaskKey>();

      DataSetArgument lSchedStaskArgs = new DataSetArgument();
      lSchedStaskArgs.add( aInv, "main_inv_no_db_id", "main_inv_no_id" );
      lSchedStaskArgs.add( aTaskTaskKey, "task_db_id", "task_id" );
      QuerySet lTasksQs =
            QuerySetFactory.getInstance().executeQueryTable( "sched_stask", lSchedStaskArgs );

      while ( lTasksQs.next() ) {
         lTasks.add( lTasksQs.getKey( TaskKey.class, "sched_db_id", "sched_id" ) );
      }

      return lTasks;
   }


   @Before
   public void before() {

      iAuthHr = Domain.createHumanResource();
      iUserId = OrgHr.findByPrimaryKey( iAuthHr ).getUserId();
      UserParameters.setInstance( iUserId, "LOGIC", new UserParametersFake( iUserId, "LOGIC" ) );
      iSchedStaskDao = new JdbcSchedStaskDao();
      iEventBus = iInjectionOverrideRule.select( RecordingEventBus.class ).get();
   }

}
