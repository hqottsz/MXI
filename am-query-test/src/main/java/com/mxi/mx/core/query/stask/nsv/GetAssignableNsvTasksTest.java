
package com.mxi.mx.core.query.stask.nsv;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.EventDeadlineKey;
import com.mxi.mx.core.key.EventRelationKey;
import com.mxi.mx.core.key.RefRelationTypeKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskFlagsKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.evt.EvtEventRel;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.EvtSchedDeadTable;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.table.task.TaskTaskFlagsTable;


/**
 * Tests the GetAssignableNsvTasks query.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class GetAssignableNsvTasksTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   // work package with assignable next shop visit tasks against its main inv and sub inv
   private static final TaskKey DEFAULT_WP = new TaskKey( 4650, 1 );

   // another work package
   private static final TaskKey ANOTHER_WP = new TaskKey( 4650, 999 );

   // task definition for off-parent next shop visit task
   private static final TaskTaskKey TASK_DEFN_NSV_OFF_PARENT = new TaskTaskKey( 4650, 1 );

   // task definition for off-wing next shop visit task
   private static final TaskTaskKey TASK_DEFN_NSV_OFF_WING = new TaskTaskKey( 4650, 2 );

   // task definition for n/a next shop visit task
   private static final TaskTaskKey TASK_DEFN_NSV_NA = new TaskTaskKey( 4650, 3 );

   // event relation key for relationship between an off-parent next shop visit task against the
   // WP's main inventory and its dependently related task
   private static final EventRelationKey EVENT_REL_BETWEEN_DEPT_TASK_AND_NSV_OFF_PARENT_TASK =
         new EventRelationKey( 4650, 2141, 1 );

   // event relation key for relationship between an off-wing next shop visit task against the WP's
   // main inventory and its dependently related task
   private static final EventRelationKey EVENT_REL_BETWEEN_DEPT_TASK_AND_NSV_OFF_WING_TASK =
         new EventRelationKey( 4650, 2241, 1 );

   // number of assignable NSV tasks (number of sched_stask records in xml file, not including the
   // work packages)
   private static final int NUM_OF_ASSIGNABLE_NSV_TASKS = 9;

   // number of off-wing NSV tasks
   private static final int NUM_OF_OFF_WING_NSV_TASKS = 5;

   // number of off-parent NSV tasks
   private static final int NUM_OF_OFF_PARENT_NSV_TASKS = 4;

   // one of the off-parent, NSV tasks against the main inventory of the work package
   private static final TaskKey OFF_PARENT_NSV_TASK_AGAINST_MAIN_INV = new TaskKey( 4650, 211 );

   // one of the off-wing, NSV tasks against the main inventory of the work package
   private static final TaskKey OFF_WING_NSV_TASK_AGAINST_MAIN_INV = new TaskKey( 4650, 215 );

   // one of the off-wing, NSV tasks against the sub inventory of the work package
   private static final TaskKey OFF_WING_NSV_TASK_AGAINST_SUB_INV = new TaskKey( 4650, 221 );

   // the related NSV task to an off-parent, NSV tasks against the main inventory of the work
   // package
   private static final TaskKey OFF_PARENT_RELATED_NSV_TASK_AGAINST_MAIN_INV =
         new TaskKey( 4650, 2141 );

   // the related NSV task to an off-wing, NSV tasks against the main inventory of the work package
   private static final TaskKey OFF_WING_RELATED_NSV_TASK_AGAINST_MAIN_INV =
         new TaskKey( 4650, 2241 );

   // the schedule calendar based deadline of the off-parent, NSV tasks against the main inventory
   // of the work package
   private static final EventDeadlineKey SCHED_CA_DEADLINE_OF_OFF_PARENT_NSV_TASK_AGAINST_MAIN_INV =
         new EventDeadlineKey( 4650, 212, 0, 21 );

   // the schedule calendar based deadline of the off-wing, NSV tasks against the sub inventory
   // of the work package
   private static final EventDeadlineKey SCHED_CA_DEADLINE_OF_OFF_WING_NSV_TASK_AGAINST_SUB_INV =
         new EventDeadlineKey( 4650, 222, 0, 21 );

   // the schedule usage based deadline of the off-parent, NSV tasks against the main inventory of
   // the work package
   private static final EventDeadlineKey SCHED_US_DEADLINE_OF_OFF_PARENT_NSV_TASK_AGAINST_MAIN_INV =
         new EventDeadlineKey( 4650, 213, 0, 10 );

   private Date iTomorrow;


   /**
    * Test that the query does not return next shop visit tasks that have their "must be removed"
    * set to "not applicable".
    */
   @Test
   public void testDoesNotReturn_MustBeRemovedNotApplicable_NsvTasks() {

      // set the "must be removed" to N/A for one of the off-parent NSV tasks against the main
      // inventory
      SchedStaskTable lStaskTable =
            SchedStaskTable.findByPrimaryKey( OFF_PARENT_NSV_TASK_AGAINST_MAIN_INV );
      lStaskTable.setTaskTaskKey( TASK_DEFN_NSV_NA );
      lStaskTable.update();

      // set the "must be removed" to N/A for one of the off-wing NSV tasks against the main
      // inventory
      lStaskTable = SchedStaskTable.findByPrimaryKey( OFF_WING_NSV_TASK_AGAINST_MAIN_INV );
      lStaskTable.setTaskTaskKey( TASK_DEFN_NSV_NA );
      lStaskTable.update();

      // set the "must be removed" to N/A for one of the off-wing NSV tasks against the sub
      // inventory
      lStaskTable = SchedStaskTable.findByPrimaryKey( OFF_WING_NSV_TASK_AGAINST_SUB_INV );
      lStaskTable.setTaskTaskKey( TASK_DEFN_NSV_NA );
      lStaskTable.update();

      // execute the test
      Set<TaskKey> lNsvTasks = execute( DEFAULT_WP );

      // verify that the N/A must be returned tasks are not returned
      assertEquals( NUM_OF_ASSIGNABLE_NSV_TASKS - 3, lNsvTasks.size() );
   }


   /**
    * Test that the query does not return assigned off-parent, next shop visit tasks.
    */
   @Test
   public void testDoesNotReturnAssignedOffParentNsvTasks() {

      // set one of the off-parent NSV tasks to be assigned to another work package
      EvtEventTable lEventTable =
            EvtEventTable.findByPrimaryKey( OFF_PARENT_NSV_TASK_AGAINST_MAIN_INV.getEventKey() );
      lEventTable.setHEvent( ANOTHER_WP.getEventKey() );
      lEventTable.update();

      // execute the test
      Set<TaskKey> lNsvTasks = execute( DEFAULT_WP );

      // verify that the assigned task is not returned
      assertEquals( NUM_OF_ASSIGNABLE_NSV_TASKS - 1, lNsvTasks.size() );
   }


   /**
    * Test that the query does not return assigned off-wing, next shop visit tasks.
    */
   @Test
   public void testDoesNotReturnAssignedOffWingNsvTasks() {

      // set one of the off-wing NSV tasks against the main inventory to be assigned to another
      // work package
      EvtEventTable lEventTable =
            EvtEventTable.findByPrimaryKey( OFF_WING_NSV_TASK_AGAINST_MAIN_INV.getEventKey() );
      lEventTable.setHEvent( ANOTHER_WP.getEventKey() );
      lEventTable.update();

      // set one of the off-parent NSV tasks against the sub inventory to be assigned to another
      // work package
      lEventTable =
            EvtEventTable.findByPrimaryKey( OFF_WING_NSV_TASK_AGAINST_SUB_INV.getEventKey() );
      lEventTable.setHEvent( ANOTHER_WP.getEventKey() );
      lEventTable.update();

      // execute the test
      Set<TaskKey> lNsvTasks = execute( DEFAULT_WP );

      // verify that the assigned task is not returned
      assertEquals( NUM_OF_ASSIGNABLE_NSV_TASKS - 2, lNsvTasks.size() );
   }


   /**
    * Test that the query does not return historic, off-parent, next shop visit tasks.
    */
   @Test
   public void testDoesNotReturnHistoricOffParentNsvTasks() {

      // set one of the off-parent NSV tasks to be historic
      EvtEventTable lEventTable =
            EvtEventTable.findByPrimaryKey( OFF_PARENT_NSV_TASK_AGAINST_MAIN_INV.getEventKey() );
      lEventTable.setHistBool( true );
      lEventTable.update();

      // execute the test
      Set<TaskKey> lNsvTasks = execute( DEFAULT_WP );

      // verify that the historic task is not returned
      assertEquals( NUM_OF_ASSIGNABLE_NSV_TASKS - 1, lNsvTasks.size() );
   }


   /**
    * Test that the query does not return historic, off-wing, next shop visit tasks.
    */
   @Test
   public void testDoesNotReturnHistoricOffWingNsvTasks() {

      // set one of the off-wing NSV tasks against the main inventory to be historic
      EvtEventTable lEventTable =
            EvtEventTable.findByPrimaryKey( OFF_WING_NSV_TASK_AGAINST_MAIN_INV.getEventKey() );
      lEventTable.setHistBool( true );
      lEventTable.update();

      // set one of the off-wing NSV tasks against the sub inventory to be historic
      lEventTable =
            EvtEventTable.findByPrimaryKey( OFF_WING_NSV_TASK_AGAINST_SUB_INV.getEventKey() );
      lEventTable.setHistBool( true );
      lEventTable.update();

      // execute the test
      Set<TaskKey> lNsvTasks = execute( DEFAULT_WP );

      // verify that the historic task is not returned
      assertEquals( NUM_OF_ASSIGNABLE_NSV_TASKS - 2, lNsvTasks.size() );
   }


   /**
    * Test that the query does not return off-parent tasks that are not next shop visit.
    */
   @Test
   public void testDoesNotReturnNonNsvOffParentTasks() {

      // set the off parent task definition to not be for NSV tasks
      TaskTaskFlagsTable lTaskFlagsTable =
            TaskTaskFlagsTable.findByPrimaryKey( new TaskTaskFlagsKey( TASK_DEFN_NSV_OFF_PARENT ) );
      lTaskFlagsTable.setNSVBool( false );
      lTaskFlagsTable.update();

      // execute the test
      Set<TaskKey> lNsvTasks = execute( DEFAULT_WP );

      // verify that the non-NSV tasks are not returned
      assertEquals( NUM_OF_ASSIGNABLE_NSV_TASKS - NUM_OF_OFF_PARENT_NSV_TASKS, lNsvTasks.size() );
   }


   /**
    * Test that the query does not return off-wing tasks that are not next shop visit.
    */
   @Test
   public void testDoesNotReturnNonNsvOffWingTasks() {

      // set the off wing task definition to not be for NSV tasks
      TaskTaskFlagsTable lTaskFlagsTable =
            TaskTaskFlagsTable.findByPrimaryKey( new TaskTaskFlagsKey( TASK_DEFN_NSV_OFF_WING ) );
      lTaskFlagsTable.setNSVBool( false );
      lTaskFlagsTable.update();

      // execute the test
      Set<TaskKey> lNsvTasks = execute( DEFAULT_WP );

      // verify that the non-NSV tasks are not returned
      assertEquals( NUM_OF_ASSIGNABLE_NSV_TASKS - NUM_OF_OFF_WING_NSV_TASKS, lNsvTasks.size() );
   }


   /**
    * Test that the query does not return off-parent NSV tasks that are recurring and have a
    * dependently related task assigned to the work package.
    */
   @Test
   public void testDoesNotReturnOffParentNsvTasksWithDependentlyRelatedTasksAssignedToTheWp() {

      // assign the related task to the work package
      EvtEventTable lEventTable = EvtEventTable
            .findByPrimaryKey( OFF_PARENT_RELATED_NSV_TASK_AGAINST_MAIN_INV.getEventKey() );
      lEventTable.setHEvent( DEFAULT_WP.getEventKey() );
      lEventTable.update();

      // execute the test
      Set<TaskKey> lNsvTasks = execute( DEFAULT_WP );

      // verify that the NSV tasks with related tasks assigned to the WP are not returned
      assertEquals( NUM_OF_ASSIGNABLE_NSV_TASKS - 1, lNsvTasks.size() );
   }


   /**
    * Test that the query does not return off-parent NSV tasks that are recurring and have a
    * non-dependently related task (i.e. the relationship type is not DEPT).
    */
   @Test
   public void testDoesNotReturnOffParentNsvTasksWithNonDependentlyRelatedTasks() {

      // for an off-parent task against the main inventory with a related task,
      // set the relationship to be CORRECT (i.e. not DEPT)
      EvtEventRel lEventRelTable =
            EvtEventRel.findByPrimaryKey( EVENT_REL_BETWEEN_DEPT_TASK_AND_NSV_OFF_PARENT_TASK );
      lEventRelTable.setRelType( RefRelationTypeKey.CORRECT );

      // execute the test
      Set<TaskKey> lNsvTasks = execute( DEFAULT_WP );

      // verify that the NSV tasks with non-DEPT related tasks are not returned
      assertEquals( NUM_OF_ASSIGNABLE_NSV_TASKS - 1, lNsvTasks.size() );
   }


   /**
    * Test that the query does not return off-parent NSV tasks whose calendar based deadlines are
    * not overdue yet.
    */
   @Test
   public void testDoesNotReturnOffParentNsvTasksWithNonOverdueCalendarDeadlines() {

      // set the scheduled, calendar based, deadline of the off-parent NSV task to be tomorrow
      // (thus not overdue).
      EvtSchedDeadTable lSchedDead = EvtSchedDeadTable
            .findByPrimaryKey( SCHED_CA_DEADLINE_OF_OFF_PARENT_NSV_TASK_AGAINST_MAIN_INV );
      lSchedDead.setDeadlineDate( iTomorrow );
      lSchedDead.update();

      // execute the test
      Set<TaskKey> lNsvTasks = execute( DEFAULT_WP );

      // verify that the non-overdue, off-parent, NSV tasks that are not returned
      assertEquals( NUM_OF_ASSIGNABLE_NSV_TASKS - 1, lNsvTasks.size() );
   }


   /**
    * Test that the query does not return off-parent NSV tasks whose usage based deadlines are not
    * overdue yet.
    */
   @Test
   public void testDoesNotReturnOffParentNsvTasksWithNonOverdueUsageDeadlines() {

      // set the scheduled, calendar based, deadline of the off-parent NSV task to be tomorrow
      // (thus not overdue).
      EvtSchedDeadTable lSchedDead = EvtSchedDeadTable
            .findByPrimaryKey( SCHED_US_DEADLINE_OF_OFF_PARENT_NSV_TASK_AGAINST_MAIN_INV );
      lSchedDead.setUsageRemaining( -20.0 );
      lSchedDead.update();

      // execute the test
      Set<TaskKey> lNsvTasks = execute( DEFAULT_WP );

      // verify that the non-overdue, off-parent, NSV tasks that are not returned
      assertEquals( NUM_OF_ASSIGNABLE_NSV_TASKS - 1, lNsvTasks.size() );
   }


   /**
    * Test that the query does not return off-wing NSV tasks that are recurring and have a
    * dependently related task assigned to the work package.
    */
   @Test
   public void testDoesNotReturnOffWingNsvTasksWithDependentlyRelatedTasksAssignedToTheWp() {

      // assign the related task to the work package
      EvtEventTable lEventTable = EvtEventTable
            .findByPrimaryKey( OFF_WING_RELATED_NSV_TASK_AGAINST_MAIN_INV.getEventKey() );
      lEventTable.setHEvent( DEFAULT_WP.getEventKey() );
      lEventTable.update();

      // execute the test
      Set<TaskKey> lNsvTasks = execute( DEFAULT_WP );

      // verify that the NSV tasks with related tasks assigned to the WP are not returned
      assertEquals( NUM_OF_ASSIGNABLE_NSV_TASKS - 1, lNsvTasks.size() );
   }


   /**
    * Test that the query does not return off-wing NSV tasks that are recurring and have a
    * non-dependently related task (i.e. the relationship type is not DEPT).
    */
   @Test
   public void testDoesNotReturnOffWingNsvTasksWithNonDependentlyRelatedTasks() {

      // for an off-parent task against the main inventory with a related task,
      // set the relationship to be CORRECT (i.e. not DEPT)
      EvtEventRel lEventRelTable =
            EvtEventRel.findByPrimaryKey( EVENT_REL_BETWEEN_DEPT_TASK_AND_NSV_OFF_WING_TASK );
      lEventRelTable.setRelType( RefRelationTypeKey.CORRECT );

      // execute the test
      Set<TaskKey> lNsvTasks = execute( DEFAULT_WP );

      // verify that the NSV tasks with non-DEPT related tasks are not returned
      assertEquals( NUM_OF_ASSIGNABLE_NSV_TASKS - 1, lNsvTasks.size() );
   }


   /**
    * Test that the query does not return off-wing NSV tasks whose calendar based deadlines are not
    * overdue yet.
    */
   @Test
   public void testDoesNotReturnOffWingNsvTasksWithNonOverdueCalendarDeadlines() {

      // set the scheduled, calendar based, deadline of the off-wing NSV task to be tomorrow
      // (thus not overdue).
      EvtSchedDeadTable lSchedDead = EvtSchedDeadTable
            .findByPrimaryKey( SCHED_CA_DEADLINE_OF_OFF_WING_NSV_TASK_AGAINST_SUB_INV );
      lSchedDead.setDeadlineDate( iTomorrow );
      lSchedDead.update();

      // execute the test
      Set<TaskKey> lNsvTasks = execute( DEFAULT_WP );

      // verify that the non-overdue, off-wing, NSV tasks that are not returned
      assertEquals( NUM_OF_ASSIGNABLE_NSV_TASKS - 1, lNsvTasks.size() );
   }


   /**
    * Test that the query returns all the NSV tasks that are assignable to a work package given the
    * assignable criteria. Refer to the query file for the definition of the assignable criteria.
    */
   @Test
   public void testReturnAssignableNsvTasksSuccessfully() {

      // execute the test
      Set<TaskKey> lNsvTasks = execute( DEFAULT_WP );

      // verify all assignable NSV tasks are returned
      assertEquals( NUM_OF_ASSIGNABLE_NSV_TASKS, lNsvTasks.size() );
   }


   /**
    * {@inheritDoc}
    */
   @Before
   public void setUp() throws Exception {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );

      setUpNsvTaskDefns();

      Calendar lCal = Calendar.getInstance();
      lCal.add( Calendar.DATE, 1 );
      iTomorrow = lCal.getTime();
   }


   /**
    * Executes the query and return the set of NSV tasks (may be empty)
    *
    * @param aWpKey
    *           the work package key
    *
    * @return set of NSV tasks (may be empty)
    */
   private Set<TaskKey> execute( TaskKey aWpKey ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aWpKey, "aWpDbId", "aWpId" );

      QuerySet lQs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // populate the collection with the query results
      Set<TaskKey> lTasks = new HashSet<TaskKey>();
      while ( lQs.next() ) {
         lTasks.add( lQs.getKey( TaskKey.class,
               com.mxi.mx.core.table.sched.SchedStaskDao.ColumnName.SCHED_DB_ID.toString(),
               com.mxi.mx.core.table.sched.SchedStaskDao.ColumnName.SCHED_ID.toString() ) );
      }

      return lTasks;
   }


   /**
    * The task_task_flag records get automatically created by the TIAF_TASK_TASK_FLAG_INSRT trigger
    * on the task_task table. We will update those task_task_flag records to make the task
    * definitions marked as next shop visit.
    */
   private void setUpNsvTaskDefns() {
      TaskTaskFlagsTable lTaskFlagsTable;

      lTaskFlagsTable =
            TaskTaskFlagsTable.findByPrimaryKey( new TaskTaskFlagsKey( TASK_DEFN_NSV_OFF_PARENT ) );
      lTaskFlagsTable.setNSVBool( true );
      lTaskFlagsTable.update();

      lTaskFlagsTable =
            TaskTaskFlagsTable.findByPrimaryKey( new TaskTaskFlagsKey( TASK_DEFN_NSV_OFF_WING ) );
      lTaskFlagsTable.setNSVBool( true );
      lTaskFlagsTable.update();
   }
}
