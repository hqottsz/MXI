package com.mxi.mx.core.query.inventory.config;

import static com.mxi.mx.testing.matchers.MxMatchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.evt.EvtEventTable;


/**
 * Tests the GetPrePostIncomplatibleTasksForPart query
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetPrePostIncompatibleTasksForPartTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetPrePostIncompatibleTasksForPartTest.class );
   }


   private static final PartGroupKey BOM_PART = new PartGroupKey( 4650, 1 );

   /** The inventory on which the requirement modification is working */
   private static final InventoryKey INVENTORY = new InventoryKey( 4650, 1 );

   /** The actual of requirement task definition to do the modification */
   private static final TaskKey INCOMPAT_REQUIREMENT_TASK = new TaskKey( 4650, 1 );

   /** The part defined in the incompatibility rule that the requirement modification is OPEN */
   private static final PartNoKey OPEN_INCOMPAT_PART = new PartNoKey( 4650, 1 );

   /**
    * The part defined in the incompatibility rule that the requirement modification is COMPLETED
    */
   private static final PartNoKey COMPLETED_INCOMPAT_PART = new PartNoKey( 4650, 2 );

   /** The part is not in the incompatibility rule */
   private static final PartNoKey COMPAT_PART = new PartNoKey( 4650, 3 );

   /** The installation tasks for the testing of different scenarios */
   private static final TaskKey JIC_OF_INCOMPAT_REQUIREMENT_TASK = new TaskKey( 4650, 2 );
   private static final TaskKey CORRECTIVE_TASK_ON_INCOMPAT_REQUIREMENT_TASK =
         new TaskKey( 4650, 12 );
   private static final TaskKey CORRECTIVE_TASK_ON_JIC_OF_INCOMPAT_REQUIREMENT_TASK =
         new TaskKey( 4650, 22 );
   private static final TaskKey ADHOC_INSTALLATION_TASK = new TaskKey( 4650, 3 );

   private static int iRowCount;


   /**
    * The part should be compatible when there is no incompatibility for the part
    */
   @Test
   public void testCompatibleWhenCompatiblePart_HistoricTask() {
      withHistoric( INCOMPAT_REQUIREMENT_TASK );

      Set<TaskKey> lTasks = getIncompatibleTasks( ADHOC_INSTALLATION_TASK, COMPAT_PART );

      assertCompatible( lTasks );
   }


   /**
    * The part should be compatible when there is no incompatibility for the part
    */
   @Test
   public void testCompatibleWhenCompatiblePart_NonHistoricTask() {
      withNonHistoric( INCOMPAT_REQUIREMENT_TASK );

      Set<TaskKey> lTasks = getIncompatibleTasks( ADHOC_INSTALLATION_TASK, COMPAT_PART );

      assertCompatible( lTasks );
   }


   /**
    * The part should be compatible when there is a complete incompatibility to a task that is
    * non-historic
    */
   @Test
   public void testCompatibleWhenCompleteIncompatibilityWithNonHistoricTask() {
      withNonHistoric( INCOMPAT_REQUIREMENT_TASK );

      Set<TaskKey> lTasks =
            getIncompatibleTasks( ADHOC_INSTALLATION_TASK, COMPLETED_INCOMPAT_PART );

      assertCompatible( lTasks );
   }


   /**
    * The part should be compatible when there is an open incompatibility to a task that is historic
    */
   @Test
   public void testCompatibleWhenOpenIncompatibilityWithHistoricTask() {
      withHistoric( INCOMPAT_REQUIREMENT_TASK );

      Set<TaskKey> lTasks = getIncompatibleTasks( ADHOC_INSTALLATION_TASK, OPEN_INCOMPAT_PART );

      assertCompatible( lTasks );
   }


   /**
    * The part should be compatible when there is an open incompatibility to a task that is
    * non-historic and is being completed
    */
   @Test
   public void testCompatibleWhenOpenIncompatibilityWithNonHistoricTaskUsingNonHistoricTask() {
      withNonHistoric( INCOMPAT_REQUIREMENT_TASK );

      Set<TaskKey> lTasks = getIncompatibleTasks( INCOMPAT_REQUIREMENT_TASK, OPEN_INCOMPAT_PART );

      assertCompatible( lTasks );
   }


   /**
    * The part should be incompatible when there is a complete incompatibility to a task that is
    * historic
    */
   @Test
   public void testIncompatibleWhenCompleteIncompatibilityWithHistoricTask() {
      withHistoric( INCOMPAT_REQUIREMENT_TASK );

      Set<TaskKey> lTasks =
            getIncompatibleTasks( ADHOC_INSTALLATION_TASK, COMPLETED_INCOMPAT_PART );

      assertIncompatible( lTasks );
   }


   /**
    * The part should be incompatible when: <br>
    * ---Incompatibility rule: A part is incompatible when the requirement task is completed<br>
    * ---The installation task is the requirement task itself<br>
    * ---During modification: the requirement task status is OPEN<br>
    * In this case, during modification, the part is still considered incompatible even the
    * requirement task is open; otherwise, the incompatible part would be installed when the
    * requirement task is finished, which is against the Incompatibility rule.
    */
   @Test
   public void
         testIncompatibleOnRequirementTaskDuringModification_IncomptiableOnCompletedRequirement() {

      withNonHistoric( INCOMPAT_REQUIREMENT_TASK );

      Set<TaskKey> lTasks =
            getIncompatibleTasks( INCOMPAT_REQUIREMENT_TASK, COMPLETED_INCOMPAT_PART );

      assertIncompatible( lTasks );
   }


   /**
    * The part should be incompatible when: <br>
    * ---Incompatibility rule: A part is incompatible when the requirement task is completed<br>
    * ---The installation task is the JIC of requirement task<br>
    * ---During modification: the requirement task status is OPEN because the JIC is OPEN<br>
    * In this case, during modification, the part is still considered incompatible even the
    * requirement task is open; otherwise, the incompatible part would be installed when the
    * requirement task is finished, which is against the Incompatibility rule.
    */
   @Test
   public void
         testIncompatibleOnJicOfRequirementTaskDuringModification_IncomptiableOnCompletedRequirement() {

      withNonHistoric( INCOMPAT_REQUIREMENT_TASK );
      withNonHistoric( JIC_OF_INCOMPAT_REQUIREMENT_TASK );

      Set<TaskKey> lTasks =
            getIncompatibleTasks( JIC_OF_INCOMPAT_REQUIREMENT_TASK, COMPLETED_INCOMPAT_PART );

      assertIncompatible( lTasks );
   }


   /**
    * The part should be incompatible when: <br>
    * ---Incompatibility rule: A part is incompatible when the requirement task is completed<br>
    * ---The installation task is the corrective task raised on requirement task<br>
    * ---During modification: the requirement task status is OPEN<br>
    * In this case, during modification, the part is still considered incompatible even the
    * requirement task is open; otherwise, the incompatible part would be installed when the
    * requirement task is finished, which is against the Incompatibility rule.
    */
   @Test
   public void
         testIncompatibleOnCorrectiveTaskRasidedOnRequirementTaskDuringModification_IncomptiableOnCompletedRequirement() {

      withNonHistoric( INCOMPAT_REQUIREMENT_TASK );

      Set<TaskKey> lTasks = getIncompatibleTasks( CORRECTIVE_TASK_ON_INCOMPAT_REQUIREMENT_TASK,
            COMPLETED_INCOMPAT_PART );

      assertIncompatible( lTasks );
   }


   /**
    * The part should be incompatible when: <br>
    * ---Incompatibility rule: A part is incompatible when the requirement task is completed<br>
    * ---The installation task is the corrective task raised on jic actual of requirement task<br>
    * ---During modification: the requirement task status is OPEN<br>
    * In this case, during modification, the part is still considered incompatible even the
    * requirement task is open; otherwise, the incompatible part would be installed when the
    * requirement task is finished, which is against the Incompatibility rule.
    */
   @Test
   public void
         testIncompatibleOnCorrectiveTaskRasidedOnJICofRequirementTaskDuringModification_IncomptiableOnCompletedRequirement() {

      withNonHistoric( INCOMPAT_REQUIREMENT_TASK );

      Set<TaskKey> lTasks = getIncompatibleTasks(
            CORRECTIVE_TASK_ON_JIC_OF_INCOMPAT_REQUIREMENT_TASK, COMPLETED_INCOMPAT_PART );

      assertIncompatible( lTasks );
   }


   /**
    * The part should be compatible when: <br>
    * ---Incompatibility rule: A part is incompatible when the requirement task is completed<br>
    * ---The installation task is adhoc task<br>
    * ---During modification: the requirement task status is OPEN<br>
    * In this case, during modification, the part is compatible when the requirement task is open.
    */
   @Test
   public void testCompatibleOnAdhocTaskDuringModification_IncomptiableOnCompletedRequirement() {

      withNonHistoric( INCOMPAT_REQUIREMENT_TASK );
      withNonHistoric( ADHOC_INSTALLATION_TASK );

      Set<TaskKey> lTasks =
            getIncompatibleTasks( ADHOC_INSTALLATION_TASK, COMPLETED_INCOMPAT_PART );

      assertCompatible( lTasks );
   }


   /**
    * The part should be incompatible when: <br>
    * ---Incompatibility rule: A part is incompatible when the requirement task is completed<br>
    * ---The installation task is adhoc task<br>
    * ---After modification: the requirement task is completed<br>
    * In this case, after modification, the part is compatible when the requirement task is
    * completed.
    */
   @Test
   public void testIncompatibleOnAdhocTaskAfterModification_IncomptiableOnCompletedRequirement() {

      withHistoric( INCOMPAT_REQUIREMENT_TASK );
      withNonHistoric( ADHOC_INSTALLATION_TASK );

      Set<TaskKey> lTasks =
            getIncompatibleTasks( ADHOC_INSTALLATION_TASK, COMPLETED_INCOMPAT_PART );

      assertIncompatible( lTasks );
   }


   /**
    * The part should be compatible when: <br>
    * ---Incompatibility rule: A part is incompatible when the requirement task is open<br>
    * ---The installation task is the corrective task raised on actual of requirement task<br>
    * ---During modification: the requirement task status is OPEN<br>
    * In this case, during modification, the part is compatible because this installation is part of
    * the modification engineering order.
    */
   @Test
   public void
         testCompatibleOnCorrectiveTaskRasidedOnRequirementTaskDuringModification_IncomptiableOnOpenRequirement() {

      withNonHistoric( INCOMPAT_REQUIREMENT_TASK );

      Set<TaskKey> lTasks = getIncompatibleTasks( CORRECTIVE_TASK_ON_INCOMPAT_REQUIREMENT_TASK,
            OPEN_INCOMPAT_PART );

      assertCompatible( lTasks );
   }


   /**
    * The part should be compatible when: <br>
    * ---Incompatibility rule: A part is incompatible when the requirement task is open<br>
    * ---The installation task is the corrective task raised on jic actual of requirement task<br>
    * ---During modification: the requirement task status is OPEN<br>
    * In this case, during modification, the part is compatible because this installation is part of
    * the modification engineering order.
    */
   @Test
   public void
         testCompatibleOnCorrectiveTaskRasidedOnJICofRequirementTaskDuringModification_IncomptiableOnOpenRequirement() {

      withNonHistoric( INCOMPAT_REQUIREMENT_TASK );

      Set<TaskKey> lTasks = getIncompatibleTasks(
            CORRECTIVE_TASK_ON_JIC_OF_INCOMPAT_REQUIREMENT_TASK, OPEN_INCOMPAT_PART );

      assertCompatible( lTasks );
   }


   /**
    * The part should be incompatible when: <br>
    * ---Incompatibility rule: A part is incompatible when the requirement task is open<br>
    * ---The installation task is adhoc task<br>
    * ---During modification: the requirement task status is OPEN<br>
    * In this case, during modification, the part is incompatible because this installation is not
    * part of the modification engineering order. Also must return one validation message where
    * single or multiple job cards associated with the requirement
    */
   @Test
   public void testIncompatibleOnAdhocTaskDuringModification_IncomptiableOnOpenRequirement() {

      withNonHistoric( INCOMPAT_REQUIREMENT_TASK );

      Set<TaskKey> lTasks = getIncompatibleTasks( ADHOC_INSTALLATION_TASK, OPEN_INCOMPAT_PART );

      assertEquals( 1, iRowCount );
      assertIncompatible( lTasks );
   }


   /**
    * Assert the result is incompatible.
    *
    * @param aTasks
    *           The incompatible modification tasks
    */
   private void assertIncompatible( Set<TaskKey> aTasks ) {
      assertThat( aTasks, hasItem( INCOMPAT_REQUIREMENT_TASK ) );
   }


   /**
    * Assert the result is compatible.
    *
    * @param aTasks
    *           The incompatible modification tasks
    */
   private void assertCompatible( Set<TaskKey> aTasks ) {
      assertThat( aTasks, is( empty() ) );
   }


   /**
    * Gets the incompatible tasks
    *
    * @param aInstallationTask
    *           the task that is being completed
    * @param aPartNo
    *           the part that is being installed / transformed to
    *
    * @return the tasks that are incompatible with this action
    */
   private Set<TaskKey> getIncompatibleTasks( TaskKey aInstallationTask, PartNoKey aPartNo ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( BOM_PART, "aBomPartDbId", "aBomPartId" );
      lArgs.add( aPartNo, "aPartNoDbId", "aPartNoId" );
      lArgs.add( INVENTORY, "aInvWithHoleInvNoDbId", "aInvWithHoleInvNoId" );
      lArgs.add( aInstallationTask, "aInstallationTaskDbId", "aInstallationTaskId" );

      DataSet lDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
      iRowCount = lDataSet.getTotalRowCount();

      Set<TaskKey> lTasks = new HashSet<TaskKey>();
      while ( lDataSet.next() ) {
         lTasks.add( lDataSet.getKey( TaskKey.class, "task_key" ) );
      }

      return lTasks;
   }


   /**
    * Converts the task to historic
    *
    * @param aTask
    *           the task
    */
   private void withHistoric( TaskKey aTask ) {
      EvtEventTable lTaskTable = EvtEventTable.findByPrimaryKey( aTask );
      lTaskTable.setHistBool( true );
      lTaskTable.update();
   }


   /**
    * Converts the task to non-historic
    *
    * @param aTask
    *           the task
    */
   private void withNonHistoric( TaskKey aTask ) {
      EvtEventTable lTaskTable = EvtEventTable.findByPrimaryKey( aTask );
      lTaskTable.setHistBool( false );
      lTaskTable.update();
   }
}
