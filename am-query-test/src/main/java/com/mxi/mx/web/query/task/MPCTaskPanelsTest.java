
package com.mxi.mx.web.query.task;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.PanelKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskSubclassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;


/**
 * This class tests the query com.mxi.mx.web.query.task.MPCTaskPanels.qrx
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class MPCTaskPanelsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   @ClassRule
   public static InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final TaskKey ACTUAL_TASK_MPC_OPEN_INWORK = new TaskKey( 1, 1 );

   private static final TaskKey ACTUAL_TASK_MPC_CLOSE_INWORK = new TaskKey( 1, 8 );

   private static final TaskKey ACTUAL_TASK_MPC_OPEN_COMPLETE = new TaskKey( 1, 10 );

   private static final TaskKey ACTUAL_TASK_MPC_CLOSE_COMPLETE = new TaskKey( 1, 11 );

   private static final TaskKey ACTUAL_TASK_MPC_OPEN_ACTV = new TaskKey( 1, 2 );

   private static final TaskKey ACTUAL_TASK_MPC_OPEN_CANCEL = new TaskKey( 1, 3 );

   private static final TaskKey ACTUAL_TASK_MPC_CLOSE_ACTV = new TaskKey( 1, 4 );

   private static final TaskKey ADHOC_TASK_1 = new TaskKey( 1, 5 );

   private static final TaskKey ADHOC_TASK_2 = new TaskKey( 1, 9 );

   private static final TaskKey WP1 = new TaskKey( 1, 6 );

   private static final TaskKey WP2 = new TaskKey( 1, 7 );

   private static final PanelKey PANEL_ONE = new PanelKey( 1000, 1000 );

   private static final PanelKey PANEL_TWO = new PanelKey( 1000, 1001 );

   private static final TaskTaskKey TASK_DEFN_MPC_OPEN = new TaskTaskKey( 100, 100 );

   private static final TaskTaskKey TASK_DEFN_MPC_CLOSE = new TaskTaskKey( 100, 101 );


   /**
    * Tests the case where adhoc maintenance task, MPC Open and Close tasks remain assigned to the
    * work package. Verify that the adhoc task has links to MPC open and close tasks
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testAdhocMaintenanceTaskAssignedToWP() throws Exception {

      DataSet lDs = execute( ACTUAL_TASK_MPC_OPEN_ACTV );

      // asserts that only one row returned.
      assertEquals( 1, lDs.getRowCount() );

      assertRow( lDs, PANEL_ONE, "PANEL 1 (Panel for mpc task definition)", RefLabourSkillKey.ENG,
            ADHOC_TASK_1, "Adhoc task with MPC open and close task", RefEventStatusKey.ACTV );

      lDs = execute( ACTUAL_TASK_MPC_CLOSE_ACTV );

      // asserts that only one row returned.
      assertEquals( 1, lDs.getRowCount() );

      assertRow( lDs, PANEL_ONE, "PANEL 1 (Panel for mpc task definition)", RefLabourSkillKey.PILOT,
            ADHOC_TASK_1, "Adhoc task with MPC open and close task", RefEventStatusKey.ACTV );
   }


   /**
    * Tests the case where the adhoc maintenance task is unassigned from the work package and the
    * MPC Open task is already started.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testMPCTaskHasStarted() throws Exception {

      DataSet lDs = execute( ACTUAL_TASK_MPC_OPEN_INWORK );

      // asserts that only one row returned.
      assertEquals( 1, lDs.getRowCount() );

      assertRow( lDs, PANEL_ONE, "PANEL 1 (Panel for mpc task definition)", RefLabourSkillKey.ENG,
            ADHOC_TASK_2, "Adhoc task 2 with MPC open and close task", RefEventStatusKey.ACTV );
   }


   /**
    * Tests the case where the adhoc maintenance task is unassigned from the work package and the
    * MPC Open task has not yet already started. Therefore, both MPC Open and Close tasks are also
    * unassigned from the work package, cancelled and there's no panel assigned to it
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testMPCTasksUnassignedFromWP() throws Exception {

      DataSet lDs = execute( ACTUAL_TASK_MPC_OPEN_CANCEL );

      // asserts that there's no row returned
      assertEquals( 0, lDs.getRowCount() );
   }


   /**
    * Create the test data
    *
    * @throws Exception
    *            if an error occurs
    */
   @BeforeClass
   public static void loadData() throws Exception {

      // ****************
      // Create panels
      // ****************
      MPCTaskPanelsData.createPanel( PANEL_ONE, "PANEL 1", "Panel for mpc task definition" );
      MPCTaskPanelsData.createPanel( PANEL_TWO, "PANEL 2", "Panel for mpc task definition" );

      // *********************************
      // Set up MPC Open Task definition
      // *********************************
      MPCTaskPanelsData.createTaskDefinition( TASK_DEFN_MPC_OPEN, RefTaskClassKey.OPENPANEL );

      MPCTaskPanelsData.createTaskLabourList( TASK_DEFN_MPC_OPEN, RefLabourSkillKey.ENG );

      MPCTaskPanelsData.createTaskPanel( TASK_DEFN_MPC_OPEN, PANEL_ONE );

      // *********************************
      // Set MPC Close Task definition
      // *********************************
      MPCTaskPanelsData.createTaskDefinition( TASK_DEFN_MPC_CLOSE, RefTaskClassKey.CLOSEPANEL );

      MPCTaskPanelsData.createTaskLabourList( TASK_DEFN_MPC_CLOSE, RefLabourSkillKey.PILOT );

      MPCTaskPanelsData.createTaskPanel( TASK_DEFN_MPC_CLOSE, PANEL_ONE );

      // **********************************************************************
      // Case 1:
      // Adhoc task is still assigned to the work package
      // MPC open and close tasks are also assigned to the work package
      // Adhoc task has links to MPC open and close tasks
      // **********************************************************************

      // MPC open task
      MPCTaskPanelsData.createActualTask( ACTUAL_TASK_MPC_OPEN_ACTV, TASK_DEFN_MPC_OPEN,
            RefTaskSubclassKey.MPCOPEN, RefTaskClassKey.MPC,
            "Actual MPC open task with ACTV status", RefEventStatusKey.ACTV, WP1 );

      MPCTaskPanelsData.createSchedPanel( ACTUAL_TASK_MPC_OPEN_ACTV, PANEL_ONE, null, null );

      // MPC close task
      MPCTaskPanelsData.createActualTask( ACTUAL_TASK_MPC_CLOSE_ACTV, TASK_DEFN_MPC_CLOSE,
            RefTaskSubclassKey.MPCCLOSE, RefTaskClassKey.MPC,
            "Actual MPC close task with ACTV status", RefEventStatusKey.ACTV, WP1 );

      MPCTaskPanelsData.createSchedPanel( ACTUAL_TASK_MPC_CLOSE_ACTV, PANEL_ONE, null, null );

      // Adhoc task
      MPCTaskPanelsData.createActualTask( ADHOC_TASK_1, null, null, RefTaskClassKey.ADHOC,
            "Adhoc task with MPC open and close task", RefEventStatusKey.ACTV, WP1 );

      MPCTaskPanelsData.createSchedPanel( ADHOC_TASK_1, PANEL_ONE, ACTUAL_TASK_MPC_OPEN_ACTV,
            ACTUAL_TASK_MPC_CLOSE_ACTV );

      // Create unlinked MPC tasks to validate MX-27345
      // MPC open task
      MPCTaskPanelsData.createActualTask( ACTUAL_TASK_MPC_OPEN_COMPLETE, TASK_DEFN_MPC_OPEN,
            RefTaskSubclassKey.MPCOPEN, RefTaskClassKey.MPC,
            "Actual MPC open task with COMPLETE status", RefEventStatusKey.COMPLETE, WP1 );

      MPCTaskPanelsData.createSchedPanel( ACTUAL_TASK_MPC_OPEN_COMPLETE, PANEL_ONE, null, null );

      // MPC close task
      MPCTaskPanelsData.createActualTask( ACTUAL_TASK_MPC_CLOSE_COMPLETE, TASK_DEFN_MPC_CLOSE,
            RefTaskSubclassKey.MPCCLOSE, RefTaskClassKey.MPC,
            "Actual MPC close task with COMPLETE status", RefEventStatusKey.COMPLETE, WP1 );

      MPCTaskPanelsData.createSchedPanel( ACTUAL_TASK_MPC_CLOSE_COMPLETE, PANEL_ONE, null, null );

      // **********************************************************************
      // Case 2:
      // Adhoc task is unassigned from the work package
      // MPC open task has already started with INWORK status.
      // Panel remains assigned to MPC open task
      // MPC open task is not linked to any maintenance task
      // **********************************************************************
      MPCTaskPanelsData.createActualTask( ACTUAL_TASK_MPC_OPEN_INWORK, TASK_DEFN_MPC_OPEN,
            RefTaskSubclassKey.MPCOPEN, RefTaskClassKey.MPC,
            "Actual MPC open task with INWORK status", RefEventStatusKey.IN_WORK, WP2 );

      MPCTaskPanelsData.createSchedPanel( ACTUAL_TASK_MPC_OPEN_INWORK, PANEL_ONE, null, null );

      MPCTaskPanelsData.createActualTask( ACTUAL_TASK_MPC_CLOSE_INWORK, TASK_DEFN_MPC_CLOSE,
            RefTaskSubclassKey.MPCCLOSE, RefTaskClassKey.MPC,
            "Actual MPC close task matching open with INWORK status", RefEventStatusKey.ACTV, WP2 );

      MPCTaskPanelsData.createSchedPanel( ACTUAL_TASK_MPC_CLOSE_INWORK, PANEL_ONE, null, null );

      // Adhoc task
      MPCTaskPanelsData.createActualTask( ADHOC_TASK_2, null, null, RefTaskClassKey.ADHOC,
            "Adhoc task 2 with MPC open and close task", RefEventStatusKey.ACTV, WP2 );

      MPCTaskPanelsData.createSchedPanel( ADHOC_TASK_2, PANEL_ONE, ACTUAL_TASK_MPC_OPEN_INWORK,
            ACTUAL_TASK_MPC_CLOSE_INWORK );

      // **************************************************************************
      // Case 3:
      // Adhoc task is unassigned from the work package
      // MPC open task is also unassigned from the work package
      // MPC open task is cancelled (because it has not yet started)
      // MPC open task does not have any panels assigned to it
      // MPC open task is not linked to any maintenance task
      // **************************************************************************
      MPCTaskPanelsData.createActualTask( ACTUAL_TASK_MPC_OPEN_CANCEL, TASK_DEFN_MPC_OPEN,
            RefTaskSubclassKey.MPCOPEN, RefTaskClassKey.MPC,
            "Actual MPC open task with CANCEL status", RefEventStatusKey.CANCEL,
            ACTUAL_TASK_MPC_OPEN_CANCEL );
   }


   /**
    * Asserts the row returned
    *
    * @param aDs
    *           The dataset
    * @param aPanel
    *           The panel key
    * @param aPanelDescription
    *           The panel description
    * @param aLabourSkill
    *           The labour skill key
    * @param aTask
    *           The task key
    * @param aTaskDescription
    *           The task description
    * @param aEventStatus
    *           The event status key
    */
   private void assertRow( DataSet aDs, PanelKey aPanel, String aPanelDescription,
         RefLabourSkillKey aLabourSkill, TaskKey aTask, String aTaskDescription,
         RefEventStatusKey aEventStatus ) {
      aDs.first();

      assertEquals( "Panel Key", aPanel, aDs.getKey( PanelKey.class, "panel_key" ) );
      assertEquals( "Panel Description", aPanelDescription, aDs.getString( "panel_description" ) );
      assertEquals( "Labour Skill", aLabourSkill.getCd(), aDs.getString( "labour_skill_cd" ) );
      assertEquals( "Labour Hours", 0, aDs.getInt( "labour_hrs" ) );
      assertEquals( "Zone Code", null, aDs.getString( "zone_cd" ) );
      assertEquals( "Maintenance Task Key", aTask, aDs.getKey( TaskKey.class, "maint_task_key" ) );
      assertEquals( "Task Description", aTaskDescription, aDs.getString( "maint_task_sdesc" ) );
      assertEquals( "Task Status", aEventStatus,
            aDs.getKey( RefEventStatusKey.class, "maint_task_status" ) );
      assertEquals( "Panel Status", "web.lbl.UNSTARTED", aDs.getString( "status" ) );
   }


   /**
    * This method executes the query in MPCTaskPanels.qrx
    *
    * @param aTask
    *           the TaskKey object
    *
    * @return The dataset after execution.
    */
   private DataSet execute( TaskKey aTask ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( aTask, "aTaskDbId", "aTaskId" );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
   }
}
