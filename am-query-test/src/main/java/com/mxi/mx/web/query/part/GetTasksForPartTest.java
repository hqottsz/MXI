package com.mxi.mx.web.query.part;

import static com.mxi.mx.core.key.RefInvClassKey.ACFT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.AircraftAssembly;
import com.mxi.am.domain.ConfigurationSlot;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Part;
import com.mxi.am.domain.PartGroup;
import com.mxi.am.domain.builder.DomainBuilder;
import com.mxi.am.domain.builder.TaskRevisionBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.RefTaskSubclassKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.task.TaskTaskTable;


/**
 * Ensures <code>GetTasksForPart</code> query functions properly
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class GetTasksForPartTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   // ~ static fields//initializers---------------------
   private static final Integer REVISION_NUMBER = 1;
   private static final String TASK_CODE = "TASK_CODE";
   private static final PartNoKey PART_NUNMBER = new PartNoKey( 1, 1 );
   private static final PartNoKey ANOTHER_PART_NUNMBER = new PartNoKey( 2, 2 );


   /**
    *
    * Given a part number and a task class mode the query will return a task definition revision
    * when that task defn rev is against the particular part number, has that same class mode, and
    * is in-build.
    *
    */
   @Test
   public void testWhenPartBasedTaskDefnIsInBuild() {

      // We want to ensure the last select of the union is being tested
      // ("Get Part No based Task Definitions").
      //
      // To avoid results from the selects "Get the Config Slot based Task Definitions" and
      // "REPL tasks list the target config-slot under repl_assmbl* fields", we will set the config
      // slot to null.
      //
      DomainBuilder<ConfigSlotKey> lConfigSlotBuilder = null;
      TaskTaskKey lBuildTask = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
            .withTaskCode( TASK_CODE ).withRevisionNumber( REVISION_NUMBER )
            .withStatus( RefTaskDefinitionStatusKey.BUILD )
            .withPart( Arrays.asList( PART_NUNMBER ) ).withConfigSlot( lConfigSlotBuilder ).build();

      // When execute this query GetTasksForPart.qrx
      final DataSet lDs = executeQuery( PART_NUNMBER );

      assertEquals( "Unexpected Number of Rows returned", 1, lDs.getRowCount() );
      assertTrue( lDs.first() );

      String lExpectedTaskCdName = TASK_CODE + " ()";

      assertEquals( "Unexpected Task Definition key", lBuildTask,
            lDs.getKey( TaskTaskKey.class, "task_defn_key" ) );
      assertEquals( "Unexpected Task code/name", lExpectedTaskCdName,
            lDs.getString( "task_cd_name" ) );
      assertEquals( "Unexpected Task class/subclass", RefTaskClassKey.REQ.getCd(),
            lDs.getString( "task_class_subclass" ) );
      assertEquals( "Unexpected Task Definition status", RefTaskDefinitionStatusKey.BUILD.getCd(),
            lDs.getString( "task_def_status_cd" ) );
      assertEquals( "Unexpected Revision number", REVISION_NUMBER,
            lDs.getInteger( "revision_ord" ) );
      assertEquals( "Unexpected Assembly code", null, lDs.getString( "assmbl_cd" ) );
      assertEquals( "Unexpected Assembly key", null, lDs.getString( "assembly_pk" ) );
      assertEquals( "Unexpected Config slot key", null, lDs.getString( "config_slot_pk" ) );
   }


   /**
    *
    * Given a part number and a task class mode the query will return a task definition revision
    * when that task defn rev is against the particular part number, has that same class mode, and
    * is active.
    *
    */
   @Test
   public void testWhenPartBasedTaskDefnIsActive() {

      // We want to ensure the last select of the union is being tested
      // ("Get Part No based Task Definitions").
      //
      // To avoid results from the selects "Get the Config Slot based Task Definitions" and
      // "REPL tasks list the target config-slot under repl_assmbl* fields", we will set the config
      // slot to null.
      //
      DomainBuilder<ConfigSlotKey> lConfigSlotBuilder = null;
      TaskTaskKey lTaskRev = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
            .withTaskCode( TASK_CODE ).withRevisionNumber( REVISION_NUMBER )
            .withStatus( RefTaskDefinitionStatusKey.ACTV ).withPart( Arrays.asList( PART_NUNMBER ) )
            .withConfigSlot( lConfigSlotBuilder ).build();

      // When execute this query GetTasksForPart.qrx
      final DataSet lDs = executeQuery( PART_NUNMBER );;

      assertEquals( "Unexpected Number of Rows returned", 1, lDs.getRowCount() );
      assertTrue( lDs.first() );

      String lExpectedTaskCdName = TASK_CODE + " ()";

      assertEquals( "Unexpected Task Definition key", lTaskRev,
            lDs.getKey( TaskTaskKey.class, "task_defn_key" ) );
      assertEquals( "Unexpected Task code/name", lExpectedTaskCdName,
            lDs.getString( "task_cd_name" ) );
      assertEquals( "Unexpected Task class/subclass", RefTaskClassKey.REQ.getCd(),
            lDs.getString( "task_class_subclass" ) );
      assertEquals( "Unexpected Task Definition status", RefTaskDefinitionStatusKey.ACTV.getCd(),
            lDs.getString( "task_def_status_cd" ) );
      assertEquals( "Unexpected Revision number", REVISION_NUMBER,
            lDs.getInteger( "revision_ord" ) );
      assertEquals( "Unexpected Assembly code", null, lDs.getString( "assmbl_cd" ) );
      assertEquals( "Unexpected Assembly key", null, lDs.getString( "assembly_pk" ) );
      assertEquals( "Unexpected Config slot key", null, lDs.getString( "config_slot_pk" ) );
   }


   /**
    * This test case is testing if the requirement definition with correct prevent manual
    * initialization is returned given a activated and configuration slot based requirement
    * definition with its prevent manual initialization which sets to true, which ensures the first
    * select of the union is being tested.
    *
    * <pre>
    *    Given a activated and configuration slot based requirement definition
    *    And its prevent manual initialization is true
    *    When execute this query GetTasksForPart.qrx
    *    Then verify the task definition with correct prevent manual initialization is returned
    * </pre>
    */
   @Test
   public void testConfigSlotBasedRequirementDefinitionWithPreventManualInitializationIsReturned() {

      // Given a assembly
      final PartNoKey lAircraftPart = createAircraftPart();
      final AssemblyKey lAircraftAssembly = createAcftAssyWithAcftPart( lAircraftPart );
      final ConfigSlotKey lAircraftRootConfigSlot =
            Domain.readRootConfigurationSlot( lAircraftAssembly );

      // Given a activated and configuration slot based requirement definition with prevent manual
      // initialization which sets to true
      final TaskTaskKey lReqDefinition = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
         aReqDefn.setTaskClass( RefTaskClassKey.REQ );
         aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
         aReqDefn.setPreventManualInitialization( true );

      } );

      // When execute this query GetTasksForPart.qrx
      final DataSet lDs = executeQuery( lAircraftPart );

      // verify the task definition with correct prevent manual initialization is returned
      final int lExpectedRowCount = 1;
      final int lActualRowCount = lDs.getRowCount();
      assertEquals( "Unexpected Number of Rows returned", lExpectedRowCount, lActualRowCount );

      lDs.first();

      final TaskTaskKey lExpectedRequirementDefinitionKey = lReqDefinition;
      final TaskTaskKey lActualRequirementDefinitionKey =
            lDs.getKey( TaskTaskKey.class, "task_defn_key" );
      assertEquals( "Unexpected Task Definition key", lExpectedRequirementDefinitionKey,
            lActualRequirementDefinitionKey );

      final boolean lActualPreventManualInitialization =
            lDs.getBoolean( "prevent_manual_init_bool" );
      assertTrue( lActualPreventManualInitialization );
   }


   /**
    * This test case is testing if the requirement definition with correct prevent manual
    * initialization is returned given an activated and replacement task definition with its prevent
    * manual initialization which sets to true, which ensures the second select of the union is
    * being tested.
    *
    * <pre>
    *    Given an activated and replacement requirement definition
    *    And its prevent manual initialization is true
    *    When execute this query GetTasksForPart.qrx
    *    Then verify the requirement definition with correct prevent manual initialization is returned
    * </pre>
    */
   @Test
   public void testReplacementRequirementDefinitionWithPreventManualInitializationIsReturned() {

      // Given a assembly
      final PartNoKey lAircraftPart = createAircraftPart();
      final AssemblyKey lAircraftAssembly = createAcftAssyWithAcftPart( lAircraftPart );
      final ConfigSlotKey lAircraftRootConfigSlot =
            Domain.readRootConfigurationSlot( lAircraftAssembly );

      // Given an activated and replacement requirement definition with prevent manual
      // initialization which sets to true
      final TaskTaskKey lReqDefinition = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
         aReqDefn.setTaskClass( RefTaskClassKey.REPL );
         aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
         aReqDefn.setPreventManualInitialization( true );

      } );

      // When execute this query GetTasksForPart.qrx
      final DataSet lDs = executeQuery( lAircraftPart );

      // verify the task definition with correct prevent manual initialization is returned
      final int lExpectedRowCount = 1;
      final int lActualRowCount = lDs.getRowCount();

      assertEquals( "Unexpected Number of Rows returned", lExpectedRowCount, lActualRowCount );

      lDs.first();

      final TaskTaskKey lExpectedRequirementDefinitionKey = lReqDefinition;
      final TaskTaskKey lActualRequirementDefinitionKey =
            lDs.getKey( TaskTaskKey.class, "task_defn_key" );
      assertEquals( "Unexpected Task Definition key", lExpectedRequirementDefinitionKey,
            lActualRequirementDefinitionKey );

      final boolean lActualPreventManualInitialization =
            lDs.getBoolean( "prevent_manual_init_bool" );
      assertTrue( lActualPreventManualInitialization );
   }


   /**
    * This test case is testing if the task definition with correct prevent manual initialization is
    * returned given an activated and part based task definition with its prevent manual
    * initialization which sets to true, which ensures the last select of the union is being tested.
    *
    * <pre>
    *    Given a activated and part based task definition
    *    And its prevent manual initialization is true
    *    When execute this query GetTasksForPart.qrx
    *    Then verify the task definition with correct prevent manual initialization is returned
    * </pre>
    */
   @Test
   public void testPartBasedReqTaskDefinitionWithPreventManualInitializationIsReturned() {

      // Given a part
      final PartNoKey lAircraftPart = createAircraftPart();

      final ConfigSlotKey lAircraftRootConfigSlot = null;

      // Given an activated requirement definition with prevent manual initialization which sets to
      // true
      final TaskTaskKey lReqDefinition = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
         aReqDefn.setTaskClass( RefTaskClassKey.REQ );
         aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
         aReqDefn.setPreventManualInitialization( true );
         aReqDefn.addPartNo( lAircraftPart );
      } );

      // When execute this query GetTasksForPart.qrx
      final DataSet lDs = executeQuery( lAircraftPart );

      // verify the task definition with correct prevent manual initialization is returned
      final int lExpectedRowCount = 1;
      final int lActualRowCount = lDs.getRowCount();
      assertEquals( "Unexpected Number of Rows returned", lExpectedRowCount, lActualRowCount );

      lDs.first();

      final TaskTaskKey lExpectedRequirementDefinitionKey = lReqDefinition;
      final TaskTaskKey lActualRequirementDefinitionKey =
            lDs.getKey( TaskTaskKey.class, "task_defn_key" );
      assertEquals( "Unexpected Task Definition key", lExpectedRequirementDefinitionKey,
            lActualRequirementDefinitionKey );

      final boolean lActualPreventManualInitialization =
            lDs.getBoolean( "prevent_manual_init_bool" );
      assertTrue( lActualPreventManualInitialization );
   }


   /**
    *
    * Given a part number and a task class mode the query will return a task definition revision
    * when that task defn rev is against the particular part number, has that same class mode, and
    * is active. The query will also return the task defn rev's subclass when set.
    *
    */
   @Test
   public void testWhenPartBasedTaskDefnIsActiveAndHasSubclass() {

      // We want to ensure the last select of the union is being tested
      // ("Get Part No based Task Definitions").
      //
      // To avoid results from the selects "Get the Config Slot based Task Definitions" and
      // "REPL tasks list the target config-slot under repl_assmbl* fields", we will set the config
      // slot to null.
      //
      DomainBuilder<ConfigSlotKey> lConfigSlotBuilder = null;
      TaskTaskKey lTaskRev = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
            .withTaskSubclass( RefTaskSubclassKey.MPCCLOSE ).withTaskCode( TASK_CODE )
            .withRevisionNumber( REVISION_NUMBER ).withStatus( RefTaskDefinitionStatusKey.ACTV )
            .withPart( Arrays.asList( PART_NUNMBER ) ).withConfigSlot( lConfigSlotBuilder ).build();

      // When execute this query GetTasksForPart.qrx
      final DataSet lDs = executeQuery( PART_NUNMBER );

      assertEquals( "Unexpected Number of Rows returned", 1, lDs.getRowCount() );
      assertTrue( lDs.first() );

      String lExpectedTaskCdName = TASK_CODE + " ()";
      String lExpectedTaskSubclassStr =
            RefTaskClassKey.REQ.getCd() + "-" + RefTaskSubclassKey.MPCCLOSE.getCd();

      assertEquals( "Unexpected Task Definition key", lTaskRev,
            lDs.getKey( TaskTaskKey.class, "task_defn_key" ) );
      assertEquals( "Unexpected Task code/name", lExpectedTaskCdName,
            lDs.getString( "task_cd_name" ) );
      assertEquals( "Unexpected Task class/subclass", lExpectedTaskSubclassStr,
            lDs.getString( "task_class_subclass" ) );
      assertEquals( "Unexpected Task Definition status", RefTaskDefinitionStatusKey.ACTV.getCd(),
            lDs.getString( "task_def_status_cd" ) );
      assertEquals( "Unexpected Revision number", REVISION_NUMBER,
            lDs.getInteger( "revision_ord" ) );
      assertEquals( "Unexpected Assembly code", null, lDs.getString( "assmbl_cd" ) );
      assertEquals( "Unexpected Assembly key", null, lDs.getString( "assembly_pk" ) );
      assertEquals( "Unexpected Config slot key", null, lDs.getString( "config_slot_pk" ) );
   }


   /**
    *
    * Given a part number and a task class mode the query will return a task definition revision
    * when that task defn rev is against the particular part number, has that same class mode, and
    * is in-revision.
    *
    */
   @Test
   public void testWhenPartBasedTaskDefnIsInRevision() {

      // We want to ensure the last select of the union is being tested
      // ("Get Part No based Task Definitions").
      //
      // To avoid results from the selects "Get the Config Slot based Task Definitions" and
      // "REPL tasks list the target config-slot under repl_assmbl* fields", we will set the config
      // slot to null.
      //
      DomainBuilder<ConfigSlotKey> lConfigSlotBuilder = null;
      TaskTaskKey lActvTaskRev = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
            .withTaskCode( TASK_CODE ).withRevisionNumber( REVISION_NUMBER )
            .withStatus( RefTaskDefinitionStatusKey.ACTV ).withPart( Arrays.asList( PART_NUNMBER ) )
            .withConfigSlot( lConfigSlotBuilder ).build();

      TaskDefnKey lActvTaskDefn = TaskTaskTable.findByPrimaryKey( lActvTaskRev ).getTaskDefn();

      // Create an in-revision task definition revision based on the active task definition for the
      // same part number as revision can not be done without having an active task
      Integer lNewRevisionNumber = REVISION_NUMBER + 1;
      TaskTaskKey lRevisedTaskRev = new TaskRevisionBuilder().withTaskDefn( lActvTaskDefn )
            .withTaskClass( RefTaskClassKey.REQ ).withTaskCode( TASK_CODE )
            .withRevisionNumber( lNewRevisionNumber )
            .withStatus( RefTaskDefinitionStatusKey.REVISION )
            .withPart( Arrays.asList( PART_NUNMBER ) ).withConfigSlot( lConfigSlotBuilder ).build();

      // When execute this query GetTasksForPart.qrx
      final DataSet lDs = executeQuery( PART_NUNMBER );

      assertEquals( "Unexpected Number of Rows returned", 1, lDs.getRowCount() );
      assertTrue( lDs.first() );

      String lExpectedTaskCdName = TASK_CODE + " ()";

      assertEquals( "Unexpected Task Definition key", lRevisedTaskRev,
            lDs.getKey( TaskTaskKey.class, "task_defn_key" ) );
      assertEquals( "Unexpected Task code/name", lExpectedTaskCdName,
            lDs.getString( "task_cd_name" ) );
      assertEquals( "Unexpected Task class/subclass", RefTaskClassKey.REQ.getCd(),
            lDs.getString( "task_class_subclass" ) );
      assertEquals( "Unexpected Task Definition status",
            RefTaskDefinitionStatusKey.REVISION.getCd(), lDs.getString( "task_def_status_cd" ) );
      assertEquals( "Unexpected Revision number", lNewRevisionNumber,
            lDs.getInteger( "revision_ord" ) );
      assertEquals( "Unexpected Assembly code", null, lDs.getString( "assmbl_cd" ) );
      assertEquals( "Unexpected Assembly key", null, lDs.getString( "assembly_pk" ) );
      assertEquals( "Unexpected Config slot key", null, lDs.getString( "config_slot_pk" ) );
   }


   /**
    *
    * Given a part number and a task class mode, the query will return a row when the task defn rev
    * against the particular part number is in Obsolete status.
    *
    */
   @Test
   public void testWhenPartBasedTaskDefnIsInObsolete() {

      // We want to ensure the last select of the union is being tested
      // ("Get Part No based Task Definitions").
      //
      // To avoid results from the selects "Get the Config Slot based Task Definitions" and
      // "REPL tasks list the target config-slot under repl_assmbl* fields", we will set the config
      // slot to null.
      //
      DomainBuilder<ConfigSlotKey> lConfigSlotBuilder = null;
      TaskTaskKey lSuprsedeTaskRev = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
            .withTaskCode( TASK_CODE ).withRevisionNumber( REVISION_NUMBER )
            .withStatus( RefTaskDefinitionStatusKey.SUPRSEDE )
            .withPart( Arrays.asList( PART_NUNMBER ) ).withConfigSlot( lConfigSlotBuilder ).build();

      TaskDefnKey lTaskDefn = TaskTaskTable.findByPrimaryKey( lSuprsedeTaskRev ).getTaskDefn();

      // Create a task definition revision based on the task definition
      // for the same part number and make this revision obsolete
      Integer lNewRevisionNumber = REVISION_NUMBER + 1;
      new TaskRevisionBuilder().withTaskDefn( lTaskDefn ).withTaskClass( RefTaskClassKey.REQ )
            .withTaskCode( TASK_CODE ).withRevisionNumber( lNewRevisionNumber )
            .withStatus( RefTaskDefinitionStatusKey.OBSOLETE )
            .withPart( Arrays.asList( PART_NUNMBER ) ).withConfigSlot( lConfigSlotBuilder ).build();

      // When execute this query GetTasksForPart.qrx
      final DataSet lDs = executeQuery( PART_NUNMBER );

      assertEquals( "Unexpected Number of Rows returned", 1, lDs.getRowCount() );
   }


   /**
    *
    * Given a part number and a task class mode the query will return a task definition revision
    * when that task defn rev is against the particular part number, has that same class mode, and
    * task definition is in-revision with one supersede and an active revision.
    *
    */
   @Test
   public void testWhenPartBasedTaskDefnHasSuprsedeAndIsInRevision() {

      // We want to ensure the last select of the union is being tested
      // ("Get Part No based Task Definitions").
      //
      // To avoid results from the selects "Get the Config Slot based Task Definitions" and
      // "REPL tasks list the target config-slot under repl_assmbl* fields", we will set the config
      // slot to null.
      //
      DomainBuilder<ConfigSlotKey> lConfigSlotBuilder = null;
      TaskTaskKey lSuprsedeTaskRev = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
            .withTaskCode( TASK_CODE ).withRevisionNumber( REVISION_NUMBER )
            .withStatus( RefTaskDefinitionStatusKey.SUPRSEDE )
            .withPart( Arrays.asList( PART_NUNMBER ) ).withConfigSlot( lConfigSlotBuilder ).build();

      TaskDefnKey lTaskDefn = TaskTaskTable.findByPrimaryKey( lSuprsedeTaskRev ).getTaskDefn();

      // Create an active task definition revision based on the superseded task definition for the
      // same part number as revision can not be done without having an active task
      Integer lNewRevisionNumber = REVISION_NUMBER + 1;

      new TaskRevisionBuilder().withTaskDefn( lTaskDefn ).withTaskClass( RefTaskClassKey.REQ )
            .withTaskCode( TASK_CODE ).withRevisionNumber( lNewRevisionNumber )
            .withStatus( RefTaskDefinitionStatusKey.ACTV ).withPart( Arrays.asList( PART_NUNMBER ) )
            .withConfigSlot( lConfigSlotBuilder ).build();

      // Create an in-revision task definition revision based on the active task definition for the
      // same part number as revision can not be done without having an active task
      lNewRevisionNumber = lNewRevisionNumber + 1;
      TaskTaskKey lRevisedTaskRev = new TaskRevisionBuilder().withTaskDefn( lTaskDefn )
            .withTaskClass( RefTaskClassKey.REQ ).withTaskCode( TASK_CODE )
            .withRevisionNumber( lNewRevisionNumber )
            .withStatus( RefTaskDefinitionStatusKey.REVISION )
            .withPart( Arrays.asList( PART_NUNMBER ) ).withConfigSlot( lConfigSlotBuilder ).build();

      // When execute this query GetTasksForPart.qrx
      final DataSet lDs = executeQuery( PART_NUNMBER );

      assertEquals( "Unexpected Number of Rows returned", 1, lDs.getRowCount() );
      assertTrue( lDs.first() );

      String lExpectedTaskCdName = TASK_CODE + " ()";

      assertEquals( "Unexpected Task Definition key", lRevisedTaskRev,
            lDs.getKey( TaskTaskKey.class, "task_defn_key" ) );
      assertEquals( "Unexpected Task code/name", lExpectedTaskCdName,
            lDs.getString( "task_cd_name" ) );
      assertEquals( "Unexpected Task class/subclass", RefTaskClassKey.REQ.getCd(),
            lDs.getString( "task_class_subclass" ) );
      assertEquals( "Unexpected Task Definition status",
            RefTaskDefinitionStatusKey.REVISION.getCd(), lDs.getString( "task_def_status_cd" ) );
      assertEquals( "Unexpected Revision number", lNewRevisionNumber,
            lDs.getInteger( "revision_ord" ) );
      assertEquals( "Unexpected Assembly code", null, lDs.getString( "assmbl_cd" ) );
      assertEquals( "Unexpected Assembly key", null, lDs.getString( "assembly_pk" ) );
      assertEquals( "Unexpected Config slot key", null, lDs.getString( "config_slot_pk" ) );
   }


   /**
    *
    * Given a part number that is associated to a previous actv revision of an in-revision task defn
    * rev, the query will return the in-revision task defn rev. The in-revision task defn rev is
    * only returned if the passed in class mode matches both its class mode and the previous actv
    * task defn rev's class mode.
    *
    */
   @Test
   public void
         testWhenPartBasedTaskDefnIsInRevisionAndItsPreviousActvRevisionIsAssociatedToThePart() {

      // We want to ensure the last select of the union is being tested
      // ("Get Part No based Task Definitions").
      //
      // To avoid results from the selects "Get the Config Slot based Task Definitions" and
      // "REPL tasks list the target config-slot under repl_assmbl* fields", we will set the config
      // slot to null.
      //
      DomainBuilder<ConfigSlotKey> lConfigSlotBuilder = null;
      TaskTaskKey lActvTaskRev = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
            .withTaskCode( TASK_CODE ).withRevisionNumber( REVISION_NUMBER )
            .withStatus( RefTaskDefinitionStatusKey.ACTV ).withPart( Arrays.asList( PART_NUNMBER ) )
            .withConfigSlot( lConfigSlotBuilder ).build();

      TaskDefnKey lActvTaskDefn = TaskTaskTable.findByPrimaryKey( lActvTaskRev ).getTaskDefn();

      // Create an in-revision task definition revision based on the active task definition
      // revision but associated to a different part number.
      Integer lNewRevisionNumber = REVISION_NUMBER + 1;
      TaskTaskKey lRevisedTaskRev = new TaskRevisionBuilder().withTaskDefn( lActvTaskDefn )
            .withTaskClass( RefTaskClassKey.REQ ).withTaskCode( TASK_CODE )
            .withRevisionNumber( lNewRevisionNumber )
            .withStatus( RefTaskDefinitionStatusKey.REVISION )
            .withPart( Arrays.asList( ANOTHER_PART_NUNMBER ) ).withConfigSlot( lConfigSlotBuilder )
            .build();

      // When execute this query GetTasksForPart.qrx
      final DataSet lDs = executeQuery( PART_NUNMBER );

      assertEquals( "Unexpected Number of Rows returned", 1, lDs.getRowCount() );
      assertTrue( lDs.first() );

      String lExpectedTaskCdName = TASK_CODE + " ()";

      assertEquals( "Unexpected Task Definition key", lRevisedTaskRev,
            lDs.getKey( TaskTaskKey.class, "task_defn_key" ) );
      assertEquals( "Unexpected Task code/name", lExpectedTaskCdName,
            lDs.getString( "task_cd_name" ) );
      assertEquals( "Unexpected Task class/subclass", RefTaskClassKey.REQ.getCd(),
            lDs.getString( "task_class_subclass" ) );
      assertEquals( "Unexpected Task Definition status",
            RefTaskDefinitionStatusKey.REVISION.getCd(), lDs.getString( "task_def_status_cd" ) );
      assertEquals( "Unexpected Revision number", lNewRevisionNumber,
            lDs.getInteger( "revision_ord" ) );
      assertEquals( "Unexpected Assembly code", null, lDs.getString( "assmbl_cd" ) );
      assertEquals( "Unexpected Assembly key", null, lDs.getString( "assembly_pk" ) );
      assertEquals( "Unexpected Config slot key", null, lDs.getString( "config_slot_pk" ) );
   }


   /**
    *
    * Given a part number that is associated to an in-revision task defn rev, the query will return
    * the in-revision task defn rev. The in-revision task defn rev is only returned if the passed in
    * class mode matches its class mode
    *
    */
   @Test
   public void testWhenPartBasedTaskDefnIsInRevisionAndItsCurrentRevisionIsAssociatedToThePart() {

      // We want to ensure the last select of the union is being tested
      // ("Get Part No based Task Definitions").
      //
      // To avoid results from the selects "Get the Config Slot based Task Definitions" and
      // "REPL tasks list the target config-slot under repl_assmbl* fields", we will set the config
      // slot to null.
      //
      DomainBuilder<ConfigSlotKey> lConfigSlotBuilder = null;
      TaskTaskKey lActvTaskRev = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
            .withTaskCode( TASK_CODE ).withRevisionNumber( REVISION_NUMBER )
            .withStatus( RefTaskDefinitionStatusKey.ACTV ).withPart( Arrays.asList( PART_NUNMBER ) )
            .withConfigSlot( lConfigSlotBuilder ).build();

      TaskDefnKey lActvTaskDefn = TaskTaskTable.findByPrimaryKey( lActvTaskRev ).getTaskDefn();

      // Create an in-revision task definition revision based on the active task definition
      // revision but associated to a different part number.
      Integer lNewRevisionNumber = REVISION_NUMBER + 1;
      TaskTaskKey lRevisedTaskRev = new TaskRevisionBuilder().withTaskDefn( lActvTaskDefn )
            .withTaskClass( RefTaskClassKey.REQ ).withTaskCode( TASK_CODE )
            .withRevisionNumber( lNewRevisionNumber )
            .withStatus( RefTaskDefinitionStatusKey.REVISION )
            .withPart( Arrays.asList( ANOTHER_PART_NUNMBER ) ).withConfigSlot( lConfigSlotBuilder )
            .build();

      // When execute this query GetTasksForPart.qrx
      final DataSet lDs = executeQuery( PART_NUNMBER );

      assertEquals( "Unexpected Number of Rows returned", 1, lDs.getRowCount() );
      assertTrue( lDs.first() );

      String lExpectedTaskCdName = TASK_CODE + " ()";

      assertEquals( "Unexpected Task Definition key", lRevisedTaskRev,
            lDs.getKey( TaskTaskKey.class, "task_defn_key" ) );
      assertEquals( "Unexpected Task code/name", lExpectedTaskCdName,
            lDs.getString( "task_cd_name" ) );
      assertEquals( "Unexpected Task class/subclass", RefTaskClassKey.REQ.getCd(),
            lDs.getString( "task_class_subclass" ) );
      assertEquals( "Unexpected Task Definition status",
            RefTaskDefinitionStatusKey.REVISION.getCd(), lDs.getString( "task_def_status_cd" ) );
      assertEquals( "Unexpected Revision number", lNewRevisionNumber,
            lDs.getInteger( "revision_ord" ) );
      assertEquals( "Unexpected Assembly code", null, lDs.getString( "assmbl_cd" ) );
      assertEquals( "Unexpected Assembly key", null, lDs.getString( "assembly_pk" ) );
      assertEquals( "Unexpected Config slot key", null, lDs.getString( "config_slot_pk" ) );
   }


   /**
    *
    * Test all of the types of results together to ensure the query returns multiple rows with
    * content from all of the above tests
    *
    */
   @Test
   public void testWhenMultiplePartBasedTaskDefn() {

      DomainBuilder<ConfigSlotKey> lConfigSlotBuilder = null;

      // TASK_CODE_BUILD - Build Status (Should appear)
      TaskTaskKey lBuildTask = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
            .withTaskCode( TASK_CODE + "BUILD" ).withRevisionNumber( REVISION_NUMBER )
            .withStatus( RefTaskDefinitionStatusKey.BUILD )
            .withPart( Arrays.asList( PART_NUNMBER ) ).withConfigSlot( lConfigSlotBuilder ).build();

      // TASK_CODE_ACTIVE - Active Status (Should appear)
      TaskTaskKey lActiveTask = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
            .withTaskCode( TASK_CODE + "ACTIVE" ).withRevisionNumber( REVISION_NUMBER )
            .withStatus( RefTaskDefinitionStatusKey.ACTV ).withPart( Arrays.asList( PART_NUNMBER ) )
            .withConfigSlot( lConfigSlotBuilder ).build();

      // TASK_CODE_ACTIVE_SUB - Active With Subclass ( Should appear)
      TaskTaskKey lActiveWithSubclassTask = new TaskRevisionBuilder()
            .withTaskClass( RefTaskClassKey.REQ ).withTaskSubclass( RefTaskSubclassKey.MPCCLOSE )
            .withTaskCode( TASK_CODE + "ACTIVE_SUB" ).withRevisionNumber( REVISION_NUMBER )
            .withStatus( RefTaskDefinitionStatusKey.ACTV ).withPart( Arrays.asList( PART_NUNMBER ) )
            .withConfigSlot( lConfigSlotBuilder ).build();

      // TASK_CODE_ACTIVE_REV - Active + In Revision ( Should appear - Only 1)
      TaskTaskKey lActiveInRevisionTask = new TaskRevisionBuilder()
            .withTaskClass( RefTaskClassKey.REQ ).withTaskCode( TASK_CODE + "ACTIVE_REV" )
            .withRevisionNumber( REVISION_NUMBER ).withStatus( RefTaskDefinitionStatusKey.ACTV )
            .withPart( Arrays.asList( PART_NUNMBER ) ).withConfigSlot( lConfigSlotBuilder ).build();

      TaskDefnKey lActiveInRevisionTaskDefn =
            TaskTaskTable.findByPrimaryKey( lActiveInRevisionTask ).getTaskDefn();

      Integer lNewRevisionNumber = REVISION_NUMBER + 1;
      TaskTaskKey lRevisedTaskRev = new TaskRevisionBuilder()
            .withTaskDefn( lActiveInRevisionTaskDefn ).withTaskClass( RefTaskClassKey.REQ )
            .withTaskCode( TASK_CODE + "ACTIVE_REV" ).withRevisionNumber( lNewRevisionNumber )
            .withStatus( RefTaskDefinitionStatusKey.REVISION )
            .withPart( Arrays.asList( PART_NUNMBER ) ).withConfigSlot( lConfigSlotBuilder ).build();

      // TASK_CODE_OBSOLE - Supersede + Obsolete ( Should appear - Only 1)
      TaskTaskKey lObsoleTaskRev = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
            .withTaskCode( TASK_CODE + "OBSOLE" ).withRevisionNumber( REVISION_NUMBER )
            .withStatus( RefTaskDefinitionStatusKey.SUPRSEDE )
            .withPart( Arrays.asList( PART_NUNMBER ) ).withConfigSlot( lConfigSlotBuilder ).build();

      TaskDefnKey lObsoleTaskDefn = TaskTaskTable.findByPrimaryKey( lObsoleTaskRev ).getTaskDefn();

      // Create a task definition revision based on the task definition
      // for the same part number and make this revision obsolete
      new TaskRevisionBuilder().withTaskDefn( lObsoleTaskDefn ).withTaskClass( RefTaskClassKey.REQ )
            .withTaskCode( TASK_CODE + "OBSOLE" ).withRevisionNumber( lNewRevisionNumber )
            .withStatus( RefTaskDefinitionStatusKey.OBSOLETE )
            .withPart( Arrays.asList( PART_NUNMBER ) ).withConfigSlot( lConfigSlotBuilder ).build();

      // TASK_CODE_SUPER - Revision + Supersede ( Should appear - Only 1)
      TaskTaskKey lSuprsedeTaskRev = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
            .withTaskCode( TASK_CODE + "SUPER" ).withRevisionNumber( REVISION_NUMBER )
            .withStatus( RefTaskDefinitionStatusKey.SUPRSEDE )
            .withPart( Arrays.asList( PART_NUNMBER ) ).withConfigSlot( lConfigSlotBuilder ).build();

      TaskDefnKey lSuprsedeTaskDefn =
            TaskTaskTable.findByPrimaryKey( lSuprsedeTaskRev ).getTaskDefn();

      // Create an active task definition revision based on the superseded task definition for the
      // same part number as revision can not be done without having an active task

      new TaskRevisionBuilder().withTaskDefn( lSuprsedeTaskDefn )
            .withTaskClass( RefTaskClassKey.REQ ).withTaskCode( TASK_CODE + "SUPER" )
            .withRevisionNumber( lNewRevisionNumber ).withStatus( RefTaskDefinitionStatusKey.ACTV )
            .withPart( Arrays.asList( PART_NUNMBER ) ).withConfigSlot( lConfigSlotBuilder ).build();

      // Create an in-revision task definition revision based on the active task definition for the
      // same part number as revision can not be done without having an active task
      lNewRevisionNumber = lNewRevisionNumber + 1;
      TaskTaskKey lSuprsedeRevisedTaskRev = new TaskRevisionBuilder()
            .withTaskDefn( lSuprsedeTaskDefn ).withTaskClass( RefTaskClassKey.REQ )
            .withTaskCode( TASK_CODE + "SUPER" ).withRevisionNumber( lNewRevisionNumber )
            .withStatus( RefTaskDefinitionStatusKey.REVISION )
            .withPart( Arrays.asList( PART_NUNMBER ) ).withConfigSlot( lConfigSlotBuilder ).build();

      // TASK_CODE_PREVIOUS - Active + Previous Revision associated to this part ( Should show -
      // Only 1 )
      TaskTaskKey lActvPrevTaskRev = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
            .withTaskCode( TASK_CODE + "PREVIOUS" ).withRevisionNumber( REVISION_NUMBER )
            .withStatus( RefTaskDefinitionStatusKey.ACTV ).withPart( Arrays.asList( PART_NUNMBER ) )
            .withConfigSlot( lConfigSlotBuilder ).build();

      TaskDefnKey lActvTaskDefn = TaskTaskTable.findByPrimaryKey( lActvPrevTaskRev ).getTaskDefn();

      // Create an in-revision task definition revision based on the active task definition
      // revision but associated to a different part number.
      TaskTaskKey lRevisedActvPrevTaskRev = new TaskRevisionBuilder().withTaskDefn( lActvTaskDefn )
            .withTaskClass( RefTaskClassKey.REQ ).withTaskCode( TASK_CODE + "PREVIOUS" )
            .withRevisionNumber( lNewRevisionNumber )
            .withStatus( RefTaskDefinitionStatusKey.REVISION )
            .withPart( Arrays.asList( ANOTHER_PART_NUNMBER ) ).withConfigSlot( lConfigSlotBuilder )
            .build();

      // Execute test.
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( PART_NUNMBER, "aPartNoDbId", "aPartNoId" );
      lArgs.add( "aClassModeCd", RefTaskClassKey.REQ.getCd() );
      DataSet lDs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // 7 in total from above should return)
      assertEquals( "Unexpected Number of Rows returned", 7, lDs.getRowCount() );

   }


   private DataSet executeQuery( PartNoKey aPartNoKey ) {
      final DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aPartNoKey, "aPartNoDbId", "aPartNoId" );
      lArgs.add( "aClassModeCd", RefTaskClassKey.REQ.getCd() );
      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   private AssemblyKey createAcftAssyWithAcftPart( final PartNoKey aAircraftPart ) {
      return Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

         @Override
         public void configure( AircraftAssembly aAcftAssy ) {
            aAcftAssy.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

               @Override
               public void configure( ConfigurationSlot aRootConfigSlot ) {
                  aRootConfigSlot.addPartGroup( new DomainConfiguration<PartGroup>() {

                     @Override
                     public void configure( PartGroup aRootCsPartGroup ) {
                        aRootCsPartGroup.setInventoryClass( ACFT );
                        aRootCsPartGroup.addPart( aAircraftPart );
                     }
                  } );
               }
            } );
         }
      } );
   }


   private PartNoKey createAircraftPart() {
      return Domain.createPart( new DomainConfiguration<Part>() {

         @Override
         public void configure( Part aPart ) {
            aPart.setInventoryClass( RefInvClassKey.ACFT );
         }
      } );
   }
}
