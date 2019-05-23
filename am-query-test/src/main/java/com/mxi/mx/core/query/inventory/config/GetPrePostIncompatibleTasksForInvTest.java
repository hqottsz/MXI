package com.mxi.mx.core.query.inventory.config;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.builder.AssemblyBuilder;
import com.mxi.am.domain.builder.ConfigSlotBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.PartGroupDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.SdFaultBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.domain.builder.TaskIncompatibilityBuilder;
import com.mxi.am.domain.builder.TaskRevisionBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefRelationTypeKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.evt.EvtEventRel;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.table.task.TaskTaskTable;


/**
 * A Query Test for GetPrePostIncompatibleTasksForInv
 *
 * @author dbaxter
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class GetPrePostIncompatibleTasksForInvTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final String PART_GROUP = "PG";

   private static final String MAIN_INV_DESC = "MAIN_INV_DESC";

   private static final String TEST_ASSEMBLY = "T_A";
   private static final String TEST_CONFIG_SLOT = "T_CS";

   private static final String CHILD_ASSEMBLY = "C_A1";
   private static final String CHILD_CONFIG_SLOT = "C_CS1";
   private static final String CHILD_INV_DESC = "CHILD_INV_DESC";

   private static final String ANOTHER_CHILD_ASSEMBLY = "C_A2";
   private static final String ANOTHER_CHILD_CONFIG_SLOT = "C_CS2";
   private static final String ANOTHER_CHILD_INV_DESC = "ANOTHER_CHILD_INV_DESC";

   private static final String GRANDCHILD_ASSEMBLY = "GC_A";
   private static final String GRANDCHILD_CONFIG_SLOT = "GC_CS";
   private static final String GRANDCHILD_INV_DESC = "GRANDCHILD_INV_DESC";

   private static final String INCOMPATIBLE_REQUIREMENT = "1000:10";
   private static final String TASK_DEFN_1 = "1000:1";
   private static final String TASK_DEFN_2 = "1000:2";

   private TaskKey iActiveTask;

   // not referenced but it helps create the complexity we are looking to test
   @SuppressWarnings( "unused" )
   private InventoryKey iAnotherChildInv;
   private InventoryKey iChildInv;
   private InventoryKey iGrandchildInv;
   private TaskKey iActualRequirementTask;
   // This is a system inventory with a hole
   private InventoryKey iInventoryWithHole;
   private InventoryKey iSubInventoryWithHole;
   private DataSet iResultDs;
   private InventoryKey iLooseInventory;
   private TaskKey iInstallationTask;
   private TaskKey iJICTask;
   private PartNoKey iIncompatiblePartNo;

   private PartGroupKey iPartGroup;

   private TaskDefnKey iModificationRequirementTaskDefn;

   private TaskTaskKey iModificationRequirement;


   /**
    * The part should be compatible when: <br>
    * ---Incompatibility rule: A part is compatible when the requirement task is OPEN<br>
    * ---Modification requirement task is on the inventory with hole<br>
    * ---The incompatible part to be installed is on the loose inventory<br>
    * ---The installation task is actual requirement task<br>
    * ---During the modification: the requirement task is open<br>
    * In this case, during modification, the part is compatible.
    */
   @Test
   public void testCompatibleOnReqTaskDuringModification_IncomptiableOnOpenRequirement() {

      withDefaultData();

      // Actual requirement is the installation task in this scenario
      iInstallationTask = iActualRequirementTask;

      execute();

      assertCompatible();

   }


   /**
    * The part should be compatible when: <br>
    * ---Incompatibility rule: A part is compatible when the requirement task is OPEN<br>
    * ---Modification requirement task is on the inventory with hole<br>
    * ---The incompatible part to be installed is on the loose inventory<br>
    * ---The installation task is actual jic task<br>
    * ---during the modification: the requirement task is open<br>
    * In this case, during modification, the part is compatible.
    */
   @Test
   public void testCompatibleOnJicTaskDuringModification_IncomptiableOnOpenRequirement() {

      withDefaultData();

      withJicTask();

      // Actual jic task is the installation task in this scenario
      iInstallationTask = iJICTask;

      execute();

      assertCompatible();

   }


   /**
    * The part should be compatible when: <br>
    * ---Incompatibility rule: A part is compatible when the requirement task is OPEN<br>
    * ---Modification requirement task is on the inventory with hole<br>
    * ---The incompatible part to be installed is on the loose inventory<br>
    * ---During the open work package, fault is created on the requirement<br>
    * ---The installation task is the corrective task of the fault <br>
    * ---During the modification: the requirement task is open<br>
    * In this case, during modification, the part is compatible.
    */
   @Test
   public void testCompatibleOnReqTaskFaultDuringModification_IncomptiableOnOpenRequirement() {

      withDefaultData();

      // Corrective task of the fault.
      withFault( iActualRequirementTask.getEventKey() );

      execute();

      assertCompatible();

   }


   /**
    * The part should be compatible when: <br>
    * ---Incompatibility rule: A part is compatible when the requirement task is OPEN<br>
    * ---Modification requirement task is on the inventory with hole<br>
    * ---The incompatible part to be installed is on the loose inventory<br>
    * ---During the open work package, fault is created on the jic<br>
    * ---The installation task is the corrective task of the fault<br>
    * ---During the modification: the requirement task is open<br>
    * In this case, during modification, the part is compatible.
    */
   @Test
   public void testCompatibleOnJicTaskFaultDuringModification_IncomptiableOnOpenRequirement() {

      withDefaultData();

      withJicTask();

      // Corrective task of the fault.
      withFault( iJICTask.getEventKey() );

      execute();

      assertCompatible();

   }


   /**
    * The part should be incompatible when: <br>
    * ---Incompatibility rule: A part is incompatible when the requirement task is OPEN<br>
    * ---Modification requirement task is on the inventory with hole<br>
    * ---The incompatible part to be installed is on the loose inventory<br>
    * ---The installation task is adhoc task<br>
    * ---During modification: the requirement task is open<br>
    * In this case, during modification, the part is incompatible.
    */
   @Test
   public void testIncompatibleOnAdhocTaskDuringModification_IncomptiableOnOpenRequirement() {

      withDefaultData();

      withAdhocTask();

      execute();

      assertIncompatible();
   }


   /**
    * The part should be incompatible when: <br>
    * ---Incompatibility rule: A part is incompatible when the requirement task is OPEN<br>
    * ---Modification requirement task is on the inventory with hole<br>
    * ---The incompatible part to be installed is on the loose inventory<br>
    * ---The job card associated to the requirement<br>
    * ---The installation task is adhoc task<br>
    * ---During modification: the requirement task is open<br>
    * In this case, during modification, the part is incompatible and must return one validation
    * message where single or multiple job cards associated with the requirement
    */
   @Test
   public void
         testOneRowReturnedWhenIncompatibleOnAdhocTaskDuringModification_IncomptiableOnOpenRequirement() {

      withDefaultData();

      withJicTask();

      withAdhocTask();

      execute();

      assertIncompatible();
   }


   /**
    * The part should be compatible when: <br>
    * ---Incompatibility rule: A part is incompatible when the requirement task is OPEN<br>
    * ---Modification requirement task is on the inventory with hole<br>
    * ---The incompatible part to be installed is on the loose inventory<br>
    * * ---The installation task is adhoc task<br>
    * ---After modification: the requirement task is completed<br>
    * In this case, after modification, the part is compatible.
    */
   @Test
   public void testCompatibleOnAdhocTaskAfterModification_IncomptiableOnOpenRequirement() {

      withDefaultData();

      withCompletedActualRequirementTask();

      withAdhocTask();

      execute();

      assertCompatible();
   }


   /**
    * The part should be incompatible when: <br>
    * ---Incompatibility rule: A part is incompatible when the requirement task is OPEN<br>
    * ---Modification requirement task is on the different inventory than the inventory with hole,
    * but both inventories are on under the same highest inventory<br>
    * ---The incompatible part to be installed is on the sub inventory of the loose inventory<br>
    * ---The installation task is adhoc task<br>
    * ---During modification: the requirement task is open<br>
    * In this case, during modification, the part is incompatible.
    */
   @Test
   public void
         testIncompatibleOnAdhocTaskDuringModificationOnSubInventoryWithHole_IncomptiableOnOpenRequirement() {

      createDataForModificationOnDifferentInventoryThanTheInventoryWithHole( false );

      execute();

      assertIncompatible();
   }


   /**
    * The part should be compatible when: <br>
    * ---Incompatibility rule: A part is incompatible when the requirement task is OPEN<br>
    * ---Modification requirement task is on the different inventory than the inventory with hole,
    * but both inventories are on under the same highest inventory<br>
    * ---The incompatible part to be installed is on the sub inventory of the loose inventory<br>
    * ---The installation task is adhoc task<br>
    * ---After modification: the requirement task is completed<br>
    * In this case, after modification, the part is compatible.
    */
   @Test
   public void
         testCompatibleOnAdhocTaskAfterModificationOnSubInventoryWithHole_IncomptiableOnOpenRequirement() {

      createDataForModificationOnDifferentInventoryThanTheInventoryWithHole( true );

      execute();

      assertCompatible();
   }


   /**
    * DOCUMENT_ME
    *
    */
   private void createDataForModificationOnDifferentInventoryThanTheInventoryWithHole(
         boolean aCreateHistRequirementActual ) {

      withDefaultData();

      // get the highest inventory of the inventory-with-hole
      InventoryKey lHighestInventory =
            InvInvTable.findByPrimaryKey( iInventoryWithHole ).getHInvNo();

      // create a new inventory (under the same highest inventory) on which the modification is
      // working
      InventoryKey lInventoryWithModification = new InventoryBuilder()
            .withClass( RefInvClassKey.ASSY ).withHighestInventory( lHighestInventory ).build();

      // modify the main inventory
      SchedStaskTable lActualRequirementTask =
            SchedStaskTable.findByPrimaryKey( iActualRequirementTask );
      lActualRequirementTask.setMainInventory( lInventoryWithModification );
      lActualRequirementTask.update();

      if ( aCreateHistRequirementActual ) {
         withCompletedActualRequirementTask();
      }

      withAdhocTask();

      withSubInventoryOnLooseInventory();
   }


   /**
    * Tests when the part has no incompatibilities and there is a set of tasks which may have caused
    * incompatibilities.
    */
   @Test
   public void testCompatibleWhenCompatible_AnyTask() {
      withMainInventoryActive( INCOMPATIBLE_REQUIREMENT );
      withHistoricTask( iInventoryWithHole, TASK_DEFN_2 );

      execute();

      assertEquals( 0, iResultDs.getRowCount() );
   }


   /**
    * Tests when the part is a complex component with no incompatibilities and there is a set of
    * tasks which may have caused incompatibilities.
    */
   @Test
   public void testCompatibleWhenComplexComponent_AnyTask() {
      withMainInventoryActive( INCOMPATIBLE_REQUIREMENT );
      withHistoricTask( iInventoryWithHole, TASK_DEFN_2 );

      withChildInventory();
      withAnotherChildInventory();
      withGrandchildInventory();

      execute();

      assertEquals( 0, iResultDs.getRowCount() );
   }


   /**
    * Tests when the part is incompatible with an active task but the task is historic so it should
    * be deemed compatible
    */
   @Test
   public void testCompatibleWhenIncompatibleActive_TaskHistoric() {
      withMainInventoryHistoric( TASK_DEFN_1 );
      withLooseInventory();
      withIncompatibilityOnOpenRequirement( iLooseInventory, TASK_DEFN_1 );

      execute();

      assertEquals( 0, iResultDs.getRowCount() );
   }


   /**
    * Tests when the part is incompatible with an active task but a different task is active so it
    * should be deemed compatible
    */
   @Test
   public void testCompatibleWhenIncompatibleDifferentTask_TaskActive() {
      withMainInventoryActive( TASK_DEFN_1 );

      withLooseInventory();
      withHistoricIncompatibility( iLooseInventory, INCOMPATIBLE_REQUIREMENT );

      execute();

      assertEquals( 0, iResultDs.getRowCount() );
   }


   /**
    * Tests when the part is incompatible with a historic task but a different task is historic so
    * it should be deemed compatible
    */
   @Test
   public void testCompatibleWhenIncompatibleDifferentTask_TaskHistoric() {
      withMainInventoryHistoric( TASK_DEFN_1 );

      withLooseInventory();
      withHistoricIncompatibility( iLooseInventory, TASK_DEFN_2 );

      execute();

      assertEquals( 0, iResultDs.getRowCount() );
   }


   /**
    * Tests when the part is incompatible with a historic task but task is active so it should be
    * deemed compatible
    */
   @Test
   public void testCompatibleWhenIncompatibleHistoric_TaskActive() {
      withMainInventoryActive( TASK_DEFN_1 );
      withLooseInventory();
      withIncompatibilityOnOpenRequirement( iLooseInventory, TASK_DEFN_2 );

      execute();

      assertEquals( 0, iResultDs.getRowCount() );
   }


   /**
    * Tests when the part is a complex component and one or more sub components is incompatible with
    * the active task.
    */
   @Test
   public void testIncompatibleWhenComplexComponentIncompatible_TaskActive() {

      withMainInventoryActive( TASK_DEFN_1 );
      withLooseInventory();
      withChildInventory();
      withAnotherChildInventory();
      withGrandchildInventory();

      withIncompatibilityOnOpenRequirement( iGrandchildInv, TASK_DEFN_1 );

      execute();

      assertEquals( 1, iResultDs.getRowCount() );

      InvInvTable lTestInv = InvInvTable.findByPrimaryKey( iLooseInventory );
      InvInvTable lSubInv = InvInvTable.findByPrimaryKey( iGrandchildInv );

      iResultDs.next();
      assertIncompatibleInventory( lTestInv.getPartNo(), lSubInv.getPartNo(), iActiveTask,
            RefEventStatusKey.ACTV );
   }


   /**
    * Tests when the part is a complex component and one or more sub components is incompatible with
    * the historic task.
    */
   @Test
   public void testIncompatibleWhenComplexComponentIncompatible_TaskHistoric() {
      withMainInventoryHistoric( INCOMPATIBLE_REQUIREMENT );
      withLooseInventory();
      withChildInventory();
      withAnotherChildInventory();
      withGrandchildInventory();

      withHistoricIncompatibility( iGrandchildInv, INCOMPATIBLE_REQUIREMENT );

      execute();

      assertIncompatible();

      InvInvTable lTestInv = InvInvTable.findByPrimaryKey( iLooseInventory );
      InvInvTable lSubInv = InvInvTable.findByPrimaryKey( iGrandchildInv );

      iResultDs.next();
      assertIncompatibleInventory( lTestInv.getPartNo(), lSubInv.getPartNo(),
            iActualRequirementTask, RefEventStatusKey.COMPLETE );
   }


   /**
    * The part should be incompatible when: <br>
    * ---Incompatibility rule: A part is incompatible when the requirement task is OPEN<br>
    * ---Modification requirement task is on the inventory with hole<br>
    * ---The incompatible part to be installed is on the loose inventory<br>
    * ---No installation task<br>
    * ---During modification: the requirement task is open<br>
    * In this case, during modification, the part is incompatible.
    */
   @Test
   public void
         testIncompatibleNoInstallationTaskDuringModification_IncomptiableOnOpenRequirement() {

      withDefaultData();

      execute();

      assertIncompatible();

      InvInvTable lTestInv = InvInvTable.findByPrimaryKey( iLooseInventory );

      iResultDs.next();
      assertIncompatibleInventory( lTestInv.getPartNo(), iActualRequirementTask,
            RefEventStatusKey.ACTV );
   }


   /**
    * Tests when the part is incompatible with a historic task and the task exists
    */
   @Test
   public void testIncompatibleWhenIncompatibleHistoric_TaskHistoric() {
      withMainInventoryHistoric( INCOMPATIBLE_REQUIREMENT );

      withLooseInventory();
      withHistoricIncompatibility( iLooseInventory, INCOMPATIBLE_REQUIREMENT );

      execute();

      assertIncompatible();

      InvInvTable lTestInv = InvInvTable.findByPrimaryKey( iLooseInventory );

      iResultDs.next();
      assertIncompatibleInventory( lTestInv.getPartNo(), iActualRequirementTask,
            RefEventStatusKey.COMPLETE );
   }


   /**
    * Wrapper that redirects to the other {@link assertIncompatibleInventory} method when the
    * expected incompatibility is on the parent part
    *
    * @param aLoosePart
    *           The Parent loose part
    * @param aActualRequirement
    *           The actual requirement task that caused the incompatibility warning
    * @param aStatusOfActualRequirement
    *           The status of the actual requirement task
    */
   private void assertIncompatibleInventory( PartNoKey aLoosePart, TaskKey aActualRequirement,
         RefEventStatusKey aStatusOfActualRequirement ) {

      assertIncompatibleInventory( aLoosePart, aLoosePart, aActualRequirement,
            aStatusOfActualRequirement );
   }


   /**
    * Assertion helper to check that the data row is as expected
    *
    * @param aLoosePart
    *           The Parent loose part
    * @param aSubLoosePart
    *           The Child loose part
    * @param aActualRequirement
    *           The actual requirement task that caused the incompatibility warning
    * @param aStatusOfActualRequirement
    *           The status of the actual requirement task
    */
   private void assertIncompatibleInventory( PartNoKey aLoosePart, PartNoKey aSubLoosePart,
         TaskKey aActualRequirement, RefEventStatusKey aStatusOfActualRequirement ) {

      assertEquals( aLoosePart, iResultDs.getKey( PartNoKey.class, "loose_part_key" ) );
      assertEquals( aSubLoosePart, iResultDs.getKey( PartNoKey.class, "loose_sub_part_key" ) );
      assertEquals( aActualRequirement, iResultDs.getKey( TaskKey.class, "task_key" ) );
      assertEquals( aStatusOfActualRequirement.getCd(), iResultDs.getString( "task_status" ) );
   }


   /**
    * Execute the query with the provided ivnentory keys.
    */
   private void execute() {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( iInventoryWithHole, "aInvWithHoleInvNoDbId", "aInvWithHoleInvNoId" );
      lArgs.add( iLooseInventory, "aLooseInvNoDbId", "aLooseInvNoId" );
      lArgs.add( iInstallationTask, "aInstallationTaskDbId", "aInstallationTaskId" );

      iResultDs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Creates an Active Task against the inventory
    *
    * @param aInventory
    * @param aTaskDefn
    */
   private void withActiveTask( InventoryKey aInventory, String aTaskDefn ) {

      iActiveTask =
            new TaskBuilder().onInventory( aInventory )
                  .withTaskRevision(
                        new TaskRevisionBuilder().withTaskDefn( new TaskDefnKey( aTaskDefn ) ) )
                  .build();
   }


   /**
    * Create another child inventory, of the test inventory.
    */
   private void withAnotherChildInventory() {

      AssemblyKey lAssembly = new AssemblyBuilder( ANOTHER_CHILD_ASSEMBLY ).build();

      ConfigSlotKey lConfigSlot = new ConfigSlotBuilder( ANOTHER_CHILD_CONFIG_SLOT )
            .withRootAssembly( lAssembly ).build();

      PartNoKey lPartNo = new PartNoBuilder().build();

      PartGroupKey lPartGroup = new PartGroupDomainBuilder( PART_GROUP )
            .withConfigSlot( lConfigSlot ).withPartNo( lPartNo ).build();

      iAnotherChildInv = new InventoryBuilder().withParentInventory( iLooseInventory )
            .withPartGroup( lPartGroup ).withPartNo( lPartNo )
            .withDescription( ANOTHER_CHILD_INV_DESC ).build();
   }


   /**
    * Create a child inventory of the test inventory.
    */
   private void withChildInventory() {

      AssemblyKey lAssembly = new AssemblyBuilder( CHILD_ASSEMBLY ).build();

      ConfigSlotKey lConfigSlot =
            new ConfigSlotBuilder( CHILD_CONFIG_SLOT ).withRootAssembly( lAssembly ).build();

      PartNoKey lPartNo = new PartNoBuilder().build();

      PartGroupKey lPartGroup = new PartGroupDomainBuilder( PART_GROUP )
            .withConfigSlot( lConfigSlot ).withPartNo( lPartNo ).build();

      iChildInv = new InventoryBuilder().withParentInventory( iLooseInventory )
            .withPartGroup( lPartGroup ).withPartNo( lPartNo ).withDescription( CHILD_INV_DESC )
            .build();
   }


   /**
    * Create a grandchild inventory of the child inventory.
    */
   private void withGrandchildInventory() {

      AssemblyKey lAssembly = new AssemblyBuilder( GRANDCHILD_ASSEMBLY ).build();

      ConfigSlotKey lConfigSlot =
            new ConfigSlotBuilder( GRANDCHILD_CONFIG_SLOT ).withRootAssembly( lAssembly ).build();

      PartNoKey lPartNo = new PartNoBuilder().build();

      PartGroupKey lPartGroup = new PartGroupDomainBuilder( PART_GROUP )
            .withConfigSlot( lConfigSlot ).withPartNo( lPartNo ).build();

      iGrandchildInv = new InventoryBuilder().withHighestInventory( iLooseInventory )
            .withParentInventory( iChildInv ).withPartGroup( lPartGroup ).withPartNo( lPartNo )
            .withDescription( GRANDCHILD_INV_DESC ).build();
   }


   /**
    * Creates an incompatibility for the historic task
    *
    * @param aInventory
    * @param aTaskDefn
    */
   private void withHistoricIncompatibility( InventoryKey aInventory, String aTaskDefn ) {
      InvInvTable lInventory = InvInvTable.findByPrimaryKey( aInventory );
      PartGroupKey lBomPart = lInventory.getBomPart();
      PartNoKey lPartNo = lInventory.getPartNo();
      TaskDefnKey lTaskDefn = new TaskDefnKey( aTaskDefn );

      new TaskIncompatibilityBuilder().withBomPart( lBomPart ).withPartNo( lPartNo )
            .withTaskDefn( lTaskDefn ).isPreCompletion( false ).build();
   }


   /**
    * Creates a Historic Task against the inventory
    *
    * @param aInventory
    * @param aTaskDefn
    */
   private void withHistoricTask( InventoryKey aInventory, String aTaskDefn ) {
      iActualRequirementTask = new TaskBuilder().onInventory( aInventory )
            .withTaskRevision(
                  new TaskRevisionBuilder().withTaskDefn( new TaskDefnKey( aTaskDefn ) ) )
            .asHistoric().withStatus( RefEventStatusKey.COMPLETE ).build();
   }


   /**
    * Create a main inventory with Active task.
    *
    * @param aTaskDefn
    */
   private void withMainInventoryActive( String aTaskDefn ) {
      iInventoryWithHole = new InventoryBuilder().withClass( RefInvClassKey.ASSY ).build();

      withActiveTask( iInventoryWithHole, aTaskDefn );
   }


   /**
    * Create a main inventory with Historic task.
    *
    * @param aTaskDefn
    */
   private void withMainInventoryHistoric( String aTaskDefn ) {
      iInventoryWithHole = new InventoryBuilder().withClass( RefInvClassKey.ASSY )
            .withDescription( MAIN_INV_DESC ).build();

      withHistoricTask( iInventoryWithHole, aTaskDefn );
   }


   /**
    * Create a loose inventory and its sub inventory. The incompatible part in on the sub inventory.
    */
   private void withSubInventoryOnLooseInventory() {

      InvInvTable lLooseInv = InvInvTable.findByPrimaryKey( iLooseInventory );
      lLooseInv.setPartGroup( null );
      lLooseInv.setPartNo( null );
      lLooseInv.update();

      // create a sub inventory of the loose inventory
      new InventoryBuilder().withPartGroup( iPartGroup ).withPartNo( iIncompatiblePartNo )
            .withHighestInventory( iLooseInventory ).withClass( RefInvClassKey.TRK ).build();
   }


   /**
    * Creates an incompatibility for the active task
    *
    * @param aInventory
    * @param aTaskDefn
    */
   private void withIncompatibilityOnOpenRequirement( InventoryKey aInventory, String aTaskDefn ) {

      InvInvTable lInventory = InvInvTable.findByPrimaryKey( aInventory );
      PartGroupKey lBomPart = lInventory.getBomPart();
      PartNoKey lPartNo = lInventory.getPartNo();
      TaskDefnKey lTaskDefn = new TaskDefnKey( aTaskDefn );

      new TaskIncompatibilityBuilder().withBomPart( lBomPart ).isPreCompletion( true )
            .withPartNo( lPartNo ).withTaskDefn( lTaskDefn ).build();
   }


   /**
    * Create a loose inventor.
    */
   private void withLooseInventory() {

      AssemblyKey lAssembly = new AssemblyBuilder( TEST_ASSEMBLY ).build();

      ConfigSlotKey lConfigSlot =
            new ConfigSlotBuilder( TEST_CONFIG_SLOT ).withRootAssembly( lAssembly ).build();

      PartNoKey lLoosePartNo = new PartNoBuilder().build();

      PartGroupKey lPartGroup = new PartGroupDomainBuilder( PART_GROUP )
            .withConfigSlot( lConfigSlot ).withPartNo( lLoosePartNo ).build();

      iLooseInventory = new InventoryBuilder().withPartGroup( lPartGroup )
            .withPartNo( lLoosePartNo ).withClass( RefInvClassKey.ASSY ).build();
   }


   /**
    * create a adhoc task as a installation task
    *
    */
   private void withAdhocTask() {
      iInstallationTask = new TaskBuilder().build();
   }


   /**
    * create a job card having actual requirement as a highest task
    *
    */
   private void withJicTask() {
      iJICTask = new TaskBuilder().withHighestTask( iActualRequirementTask ).build();
   }


   /**
    * update actual requirement task to historical task
    *
    */
   private void withCompletedActualRequirementTask() {

      EvtEventTable lActualRequirementTaskEvent =
            EvtEventTable.findByPrimaryKey( iActualRequirementTask.getEventKey() );
      lActualRequirementTaskEvent.setHistBool( true );
      lActualRequirementTaskEvent.update();

   }


   /**
    * Create a fault <br>
    * ---Create a actaul fault task<br>
    * ---Create a fault with event relationship to the task(req/jic)
    */
   private void withFault( EventKey aTaskEvent ) {
      iInstallationTask = new TaskBuilder().onInventory( iInventoryWithHole ).build();
      FaultKey lFaultKey = new SdFaultBuilder().onInventory( iInventoryWithHole )
            .withCorrectiveTask( iInstallationTask ).build();

      EvtEventRel.create( aTaskEvent, lFaultKey.getEventKey(), RefRelationTypeKey.DISCF );
   }


   /**
    * assert that part is compatible
    *
    */
   private void assertCompatible() {
      Assert.assertTrue( iResultDs.isEmpty() );
   }


   /**
    * assert that part is incompatible
    *
    */
   private void assertIncompatible() {
      assertEquals( 1, iResultDs.getRowCount() );
   }


   @Before
   public void loadData() throws Exception {

   }


   /**
    * Create default incompatible data setup <br>
    * ---Create an inventory eg. SYS under the highest inventory eg. ACFT<br>
    * ---Create a Modification requirement task<br>
    * ---Create a actual task of the modification task on the inventory with hole<br>
    * ---Create a incompatible part <br>
    * ---Assign the part to the config slot<br>
    * ---Create loose inventory of the incompatible part and link to part group<br>
    * ---Create the incompatibility rule for OPEN(isPreCompletion( true )) modification requirement
    * task defn.
    */
   private void withDefaultData() {

      iInventoryWithHole =
            new InventoryBuilder().withHighestInventory( new InventoryBuilder().build() ).build();

      iModificationRequirement = new TaskRevisionBuilder().build();
      iModificationRequirementTaskDefn =
            TaskTaskTable.findByPrimaryKey( iModificationRequirement ).getTaskDefn();
      iActualRequirementTask = new TaskBuilder().onInventory( iInventoryWithHole )
            .withTaskRevision( iModificationRequirement ).build();

      iIncompatiblePartNo = new PartNoBuilder().build();
      iPartGroup = new PartGroupDomainBuilder( PART_GROUP )
            .withConfigSlot( new ConfigSlotBuilder( TEST_CONFIG_SLOT )
                  .withRootAssembly( new AssemblyBuilder( TEST_ASSEMBLY ).build() ).build() )
            .withPartNo( iIncompatiblePartNo ).build();

      iLooseInventory = new InventoryBuilder().withPartGroup( iPartGroup )
            .withPartNo( iIncompatiblePartNo ).build();

      new TaskIncompatibilityBuilder().withBomPart( iPartGroup ).isPreCompletion( true )
            .withPartNo( iIncompatiblePartNo ).withTaskDefn( iModificationRequirementTaskDefn )
            .build();
   }

}
