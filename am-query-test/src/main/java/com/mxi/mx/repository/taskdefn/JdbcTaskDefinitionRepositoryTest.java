package com.mxi.mx.repository.taskdefn;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.OrganizationDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.services.taskdefn.criteria.TaskDefinitionSearchCriteria;
import com.mxi.mx.core.table.task.TaskTaskTable;


public class JdbcTaskDefinitionRepositoryTest {

   final private static boolean ON_CONDITION_FILTER_ENABLED = true;
   final private static boolean ON_CONDITION_FILTER_DISABLED = false;

   final private static boolean CORRECTIVE_TASK = true;
   final private static boolean NOT_CORRECTIVE_TASK = false;

   private static final boolean ONLY_REQUIREMENT_DEFINITIONS = true;
   private static final boolean ANY_TASK_DEFINITIONS = false;

   private static final boolean ON_CONDITION = true;
   private static final boolean NOT_ON_CONDITION = false;

   private static final boolean PREVENT_MANUAL_INITIALIZATION = true;
   private static final boolean ALLOW_MANUAL_INITIALIZATION = false;

   private JdbcTaskDefinitionRepository iJdbcTaskDefinitionRepository =
         new JdbcTaskDefinitionRepository();

   private AssemblyKey iAircraftAssembly;
   private ConfigSlotKey iAircraftRootConfigSlot;
   private InventoryKey iAircraft;
   private HumanResourceKey iHr;
   private OrgKey iOrganization;
   private TaskDefinitionSearchCriteria iTaskDefinitionSearchCriteria;

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * When user wants to create a not corrective task based on Task Definition without "Show Only On
    * Condition Tasks" clicked under an aircraft, the not on-condition REQ task definition
    * applicable to the aircraft should be returned.
    */
   @Test
   public void
         get_notOnConditionReqTaskDefinition_forNotCorrectiveTaskAndNotReplConfigSlot_withoutOnlyShowOnCondition() {

      TaskTaskKey lNotOnConditionReqTaskDefinition =
            createTaskDefinition( RefTaskClassKey.REQ, false );
      iTaskDefinitionSearchCriteria.setReplConfigSlot( null );

      QuerySet lQuerySet = iJdbcTaskDefinitionRepository.get( iTaskDefinitionSearchCriteria,
            ON_CONDITION_FILTER_DISABLED, NOT_CORRECTIVE_TASK, ANY_TASK_DEFINITIONS );

      assertEquals( 1, lQuerySet.getRowCount() );
      lQuerySet.next();
      assertEquals( lNotOnConditionReqTaskDefinition,
            lQuerySet.getKey( TaskTaskKey.class, "task_definition_key" ) );
   }


   /**
    * When user wants to create a not corrective task based on Task Definition with "Show Only On
    * Condition Tasks" clicked under an aircraft, the on-condition REQ task applicable to the
    * aircraft should be returned.
    */
   @Test
   public void
         get_onConditionReqTaskDefinition_forNotCorrectiveTaskAndNotReplConfigSlot_withOnlyShowOnCondition() {

      TaskTaskKey lOnConditionReqTaskDefinition = createTaskDefinition( RefTaskClassKey.REQ, true );
      iTaskDefinitionSearchCriteria.setReplConfigSlot( null );

      QuerySet lQuerySet = iJdbcTaskDefinitionRepository.get( iTaskDefinitionSearchCriteria,
            ON_CONDITION_FILTER_DISABLED, NOT_CORRECTIVE_TASK, ANY_TASK_DEFINITIONS );

      assertEquals( 1, lQuerySet.getRowCount() );
      lQuerySet.next();
      assertEquals( lOnConditionReqTaskDefinition,
            lQuerySet.getKey( TaskTaskKey.class, "task_definition_key" ) );
   }


   /**
    * When user wants to create a not corrective task based on Task Definition with "Show Only On
    * Condition Tasks" clicked under an aircraft, the not on-condition REQ task applicable to the
    * aircraft should NOT be returned.
    */
   @Test
   public void
         get_notOnConditionReqTaskDefinition_forNotCorrectiveTaskAndNotReplConfigSlot_withOnlyShowOnCondition() {

      createTaskDefinition( RefTaskClassKey.REQ, false );
      iTaskDefinitionSearchCriteria.setReplConfigSlot( null );

      QuerySet lQuerySet = iJdbcTaskDefinitionRepository.get( iTaskDefinitionSearchCriteria,
            ON_CONDITION_FILTER_ENABLED, NOT_CORRECTIVE_TASK, ANY_TASK_DEFINITIONS );

      assertFalse( lQuerySet.hasNext() );
   }


   /**
    * When user wants to create a corrective subtask based on Job Card Task Definition under an
    * aircraft, the JIC not on-condition task definition applicable to the aircraft should be
    * returned. (on-condition does not matter for Create Task Based on Job Card Task Definition)
    */
   @Test
   public void get_notOnConditionJicTaskDefinition_forCorrectiveTaskAndNotReplConfigSlot() {

      TaskTaskKey lNotOnConditionJicTaskDefinition =
            createTaskDefinition( RefTaskClassKey.JIC, false );
      iTaskDefinitionSearchCriteria.setReplConfigSlot( null );

      QuerySet lQuerySet = iJdbcTaskDefinitionRepository.get( iTaskDefinitionSearchCriteria,
            ON_CONDITION_FILTER_ENABLED, CORRECTIVE_TASK, ANY_TASK_DEFINITIONS );

      assertEquals( 1, lQuerySet.getRowCount() );
      lQuerySet.next();
      assertEquals( lNotOnConditionJicTaskDefinition,
            lQuerySet.getKey( TaskTaskKey.class, "task_definition_key" ) );
   }


   /**
    * When user wants to create a corrective subtask based on Job Card Task Definition under an
    * aircraft, the JIC on-condition task definition applicable to the aircraft should be returned.
    * (on-condition does not matter for Create Task Based on Job Card Task Definition)
    */
   @Test
   public void get_onConditionJicTaskDefinition_forCorrectiveTaskAndNotReplConfigSlot() {

      TaskTaskKey lOnConditionJicTaskDefinition = createTaskDefinition( RefTaskClassKey.JIC, true );
      iTaskDefinitionSearchCriteria.setReplConfigSlot( null );

      QuerySet lQuerySet = iJdbcTaskDefinitionRepository.get( iTaskDefinitionSearchCriteria,
            ON_CONDITION_FILTER_DISABLED, CORRECTIVE_TASK, ANY_TASK_DEFINITIONS );

      assertEquals( 1, lQuerySet.getRowCount() );
      lQuerySet.next();
      assertEquals( lOnConditionJicTaskDefinition,
            lQuerySet.getKey( TaskTaskKey.class, "task_definition_key" ) );
   }


   /**
    * When user wants to create a corrective subtask based on Requirement Task Definition under an
    * aircraft, the REQ on-condition task definition applicable to the aircraft should be returned.
    * (the task definition MUST be on-condition for Create Task Based on Requirement Task
    * Definition)
    */
   @Test
   public void get_onConditionReqTaskDefinition_forCorrectiveTaskAndNotReplConfigSlot() {

      TaskTaskKey lOnConditionReqTaskDefinition = createTaskDefinition( RefTaskClassKey.REQ, true );
      iTaskDefinitionSearchCriteria.setReplConfigSlot( null );

      QuerySet lQuerySet = iJdbcTaskDefinitionRepository.get( iTaskDefinitionSearchCriteria,
            ON_CONDITION_FILTER_ENABLED, CORRECTIVE_TASK, ONLY_REQUIREMENT_DEFINITIONS );

      assertEquals( 1, lQuerySet.getRowCount() );
      lQuerySet.next();
      assertEquals( lOnConditionReqTaskDefinition,
            lQuerySet.getKey( TaskTaskKey.class, "task_definition_key" ) );
   }


   /**
    * When user wants to create a corrective subtask based on Requirement Task Definition under an
    * aircraft, the REQ not on-condition task definition applicable to the aircraft should NOT be
    * returned. (the task definition MUST be on-condition for Create Task Based on Requirement Task
    * Definition)
    */
   @Test
   public void get_notOnConditionReqTaskDefinition_forCorrectiveTaskAndNotReplConfigSlot() {

      createTaskDefinition( RefTaskClassKey.REQ, false );
      iTaskDefinitionSearchCriteria.setReplConfigSlot( null );

      QuerySet lQuerySet = iJdbcTaskDefinitionRepository.get( iTaskDefinitionSearchCriteria,
            ON_CONDITION_FILTER_DISABLED, CORRECTIVE_TASK, ONLY_REQUIREMENT_DEFINITIONS );

      assertFalse( lQuerySet.hasNext() );
   }


   /**
    * When user wants to create a not corrective task based on REPL Requirement Task Definition
    * without "Show Only On Condition Tasks" clicked under a REPL config slot, the REPL not
    * on-condition task definition applicable to the REPL config slot sould be returned.
    */
   @Test
   public void
         get_notOnConditionReplTaskDefinition_forNotCorrectiveTaskAndWithReplConfigSlot_withoutOnlyShowOnCondition() {

      TaskTaskKey lNotOnConditionReplTaskDefinition =
            createTaskDefinition( RefTaskClassKey.REPL, false );

      TaskTaskTable lTaskDefinition =
            TaskTaskTable.findByPrimaryKey( lNotOnConditionReplTaskDefinition );
      lTaskDefinition.setReplBomItem( iAircraftRootConfigSlot );
      lTaskDefinition.update();
      iTaskDefinitionSearchCriteria.setReplConfigSlot( iAircraftRootConfigSlot );

      QuerySet lQuerySet = iJdbcTaskDefinitionRepository.get( iTaskDefinitionSearchCriteria,
            ON_CONDITION_FILTER_DISABLED, NOT_CORRECTIVE_TASK, ANY_TASK_DEFINITIONS );

      assertEquals( 1, lQuerySet.getRowCount() );
      lQuerySet.next();
      assertEquals( lNotOnConditionReplTaskDefinition,
            lQuerySet.getKey( TaskTaskKey.class, "task_definition_key" ) );
   }


   /**
    * When user wants to create a not corrective task based on REPL Requirement Task Definition with
    * "Show Only On Condition Tasks" clicked under a REPL config slot, the REPL on-condition task
    * definition applicable to the REPL config slot sould be returned.
    */
   @Test
   public void
         get_onConditionReplTaskDefinition_forNotCorrectiveTaskAndWithReplConfigSlot_withOnlyShowOnCondition() {

      TaskTaskKey lOnConditionReplTaskDefinition =
            createTaskDefinition( RefTaskClassKey.REPL, true );

      TaskTaskTable lTaskDefinition =
            TaskTaskTable.findByPrimaryKey( lOnConditionReplTaskDefinition );
      lTaskDefinition.setReplBomItem( iAircraftRootConfigSlot );
      lTaskDefinition.update();
      iTaskDefinitionSearchCriteria.setReplConfigSlot( iAircraftRootConfigSlot );

      QuerySet lQuerySet = iJdbcTaskDefinitionRepository.get( iTaskDefinitionSearchCriteria,
            ON_CONDITION_FILTER_ENABLED, NOT_CORRECTIVE_TASK, ANY_TASK_DEFINITIONS );

      assertEquals( 1, lQuerySet.getRowCount() );
      lQuerySet.next();
      assertEquals( lOnConditionReplTaskDefinition,
            lQuerySet.getKey( TaskTaskKey.class, "task_definition_key" ) );
   }


   /**
    * When user wants to create a not corrective task based on REPL Requirement Task Definition with
    * "Show Only On Condition Tasks" clicked under a REPL config slot, the REPL not on-condition
    * task definition applicable to the REPL config slot should NOT be returned.
    */
   @Test
   public void
         get_notOnConditionReplTaskDefinition_forNotCorrectiveTaskAndWithReplConfigSlot_withOnlyShowOnCondition() {

      TaskTaskKey lNotOnConditionReplTaskDefinition =
            createTaskDefinition( RefTaskClassKey.REPL, false );

      TaskTaskTable lTaskDefinition =
            TaskTaskTable.findByPrimaryKey( lNotOnConditionReplTaskDefinition );
      lTaskDefinition.setReplBomItem( iAircraftRootConfigSlot );
      lTaskDefinition.update();
      iTaskDefinitionSearchCriteria.setReplConfigSlot( iAircraftRootConfigSlot );

      QuerySet lQuerySet = iJdbcTaskDefinitionRepository.get( iTaskDefinitionSearchCriteria,
            ON_CONDITION_FILTER_ENABLED, NOT_CORRECTIVE_TASK, ANY_TASK_DEFINITIONS );

      assertFalse( lQuerySet.hasNext() );
   }


   /**
    * Test that Requirement Definitions with Prevent Manual Initialization are excluded when
    * retrieving on-condition requirement definitions from a Corrective Task.<br/>
    * This tests the underlying
    * {@link com.mxi.mx.core.query.taskdefn.ReqTaskDefinitionListForCorrTask.qrx} query, which is
    * hard-coded to always retrieve on-condition requirement definitions (class_mode_cd = REQ AND
    * on-condition = true).
    *
    * <pre>
    * Given an on-condition Requirement Definition
    * And a second on-condition Requirement Definition with Prevent Manual Initialization
    * When querying for requirement definitions from a Corrective Task
    * Then a Requirement Definition is returned and it isn't the Requirement Definition with Prevent Manual Initialization
    * </pre>
    */
   @Test
   public void testPreventManualInitializationForCorrectiveTaskRequirementDefinitions() {
      createTaskDefinition( RefTaskClassKey.REQ, ON_CONDITION, ALLOW_MANUAL_INITIALIZATION );
      TaskTaskKey lPreventManualInitializationTaskDef = createTaskDefinition( RefTaskClassKey.REQ,
            ON_CONDITION, PREVENT_MANUAL_INITIALIZATION );

      QuerySet lQuerySet = iJdbcTaskDefinitionRepository.get( iTaskDefinitionSearchCriteria,
            ON_CONDITION_FILTER_DISABLED, CORRECTIVE_TASK, ONLY_REQUIREMENT_DEFINITIONS );

      assertThat( lQuerySet.getRowCount(), is( equalTo( 1 ) ) );

      lQuerySet.next();
      assertThat( lQuerySet.getKey( TaskTaskKey.class, "task_definition_key" ),
            is( not( equalTo( lPreventManualInitializationTaskDef ) ) ) );
   }


   /**
    * Test that Definitions with Prevent Manual Initialization are excluded when retrieving
    * definitions.<br/>
    * This tests the underlying {@link com.mxi.mx.core.query.taskdefn.TaskDefinitionList.qrx} query,
    * which is hard-coded to always retrieve requirement or block definitions (class_mode_cd IN (
    * BLOCK, REQ) ) and will conditionally return only on-condition definitions depending on the get
    * method's aOnlyOnCondition parameter.
    *
    * <pre>
    * Given a Task Definition
    * And a second Task Definition with Prevent Manual Initialization
    * When querying for task definitions
    * Then a Task Definition is returned and it isn't the Task Definition with Prevent Manual Initialization
    * </pre>
    */
   @Test
   public void testPreventManualInitializationForRequirementDefinitions() {
      createTaskDefinition( RefTaskClassKey.REQ, NOT_ON_CONDITION, ALLOW_MANUAL_INITIALIZATION );
      TaskTaskKey lPreventManualInitializationTaskDef = createTaskDefinition( RefTaskClassKey.REQ,
            NOT_ON_CONDITION, PREVENT_MANUAL_INITIALIZATION );

      QuerySet lQuerySet = iJdbcTaskDefinitionRepository.get( iTaskDefinitionSearchCriteria,
            ON_CONDITION_FILTER_DISABLED, NOT_CORRECTIVE_TASK, ANY_TASK_DEFINITIONS );

      assertThat( lQuerySet.getRowCount(), is( equalTo( 1 ) ) );

      lQuerySet.next();
      assertThat( lQuerySet.getKey( TaskTaskKey.class, "task_definition_key" ),
            is( not( equalTo( lPreventManualInitializationTaskDef ) ) ) );

      assertThat( lQuerySet.getBoolean( "prevent_manual_init_bool" ),
            is( equalTo( ALLOW_MANUAL_INITIALIZATION ) ) );
   }


   /**
    * Test that Definitions with Prevent Manual Initialization are excluded when retrieving
    * on-condition requirement definitions.<br/>
    * This tests the underlying {@link com.mxi.mx.core.query.taskdefn.TaskDefinitionList.qrx} query,
    * which is hard-coded to always retrieve requirement or block definitions (class_mode_cd IN (
    * BLOCK, REQ) ) and will conditionally return only on-condition definitions depending on the get
    * method's aOnlyOnCondition parameter.
    *
    * <pre>
    * Given an on-condition Task Definition
    * And a second on-condition Task Definition with Prevent Manual Initialization
    * When querying for on-condition task definitions
    * Then a Task Definition is returned and it isn't the Task Definition with Prevent Manual Initialization
    * </pre>
    */
   @Test
   public void testPreventManualInitializationForOnConditionRequirementDefinitions() {
      createTaskDefinition( RefTaskClassKey.REQ, ON_CONDITION, ALLOW_MANUAL_INITIALIZATION );
      TaskTaskKey lPreventManualInitializationTaskDef = createTaskDefinition( RefTaskClassKey.REQ,
            ON_CONDITION, PREVENT_MANUAL_INITIALIZATION );

      QuerySet lQuerySet = iJdbcTaskDefinitionRepository.get( iTaskDefinitionSearchCriteria,
            ON_CONDITION_FILTER_ENABLED, NOT_CORRECTIVE_TASK, ANY_TASK_DEFINITIONS );

      assertThat( lQuerySet.getRowCount(), is( equalTo( 1 ) ) );

      lQuerySet.next();
      assertThat( lQuerySet.getKey( TaskTaskKey.class, "task_definition_key" ),
            is( not( equalTo( lPreventManualInitializationTaskDef ) ) ) );

      assertThat( lQuerySet.getBoolean( "prevent_manual_init_bool" ),
            is( equalTo( ALLOW_MANUAL_INITIALIZATION ) ) );
   }


   private TaskTaskKey createTaskDefinition( RefTaskClassKey aRefTaskClassKey,
         boolean aOnCondition ) {
      return createTaskDefinition( aRefTaskClassKey, aOnCondition, false );
   }


   private TaskTaskKey createTaskDefinition( RefTaskClassKey aRefTaskClassKey, boolean aOnCondition,
         boolean aPreventManualInitialization ) {
      return Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

         @Override
         public void configure( RequirementDefinition aRequirementDefinition ) {
            aRequirementDefinition.setTaskClass( aRefTaskClassKey );
            aRequirementDefinition.setOnCondition( aOnCondition );
            aRequirementDefinition.setOrganization( iOrganization );
            aRequirementDefinition.againstConfigurationSlot( iAircraftRootConfigSlot );
            if ( aPreventManualInitialization ) {
               aRequirementDefinition
                     .setPreventManualInitialization( aPreventManualInitialization );
            }
         }
      } );
   }


   @Before
   public void setUp() {
      iAircraftAssembly = Domain.createAircraftAssembly();
      iAircraftRootConfigSlot = new ConfigSlotKey( iAircraftAssembly, 0 );
      iAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( iAircraftAssembly );
         }
      } );
      iOrganization =
            new OrganizationDomainBuilder().withCode( "ORG1" ).withDescription( "ORG-1" ).build();
      iHr = new HumanResourceDomainBuilder().inOrganization( iOrganization ).build();

      iTaskDefinitionSearchCriteria = new TaskDefinitionSearchCriteria();
      iTaskDefinitionSearchCriteria.setAuthorizingHR( iHr );
      iTaskDefinitionSearchCriteria.setInventory( iAircraft );
   }

}
