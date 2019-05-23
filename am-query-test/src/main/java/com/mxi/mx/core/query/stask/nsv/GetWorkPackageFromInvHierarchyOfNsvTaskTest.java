
package com.mxi.mx.core.query.stask.nsv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.SchedWPKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.table.task.SchedWPTable;
import com.mxi.mx.core.table.task.SchedWPTable.EnforceNsvTasksState;


/**
 * Tests the GetWorkPackageFromInvHierarchyOfNsvTask query.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class GetWorkPackageFromInvHierarchyOfNsvTaskTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void setUp() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
   }


   private static final TaskKey NSV_TASK_FOR_INV_WHOSE_HIERARCHY_HAS_NO_WPS =
         new TaskKey( 4650, 2 );
   private static final TaskKey NSV_TASK_FOR_INV_WITH_APPLICABLE_WP_AGAINST_HIGHEST_INV =
         new TaskKey( 4650, 5 );
   private static final TaskKey NSV_TASK_FOR_INV_WITH_APPLICABLE_RO_WP_AGAINST_HIGHEST_INV =
         new TaskKey( 4650, 7 );
   private static final TaskKey NSV_TASK_FOR_INV_WITH_APPLICABLE_WP_AGAINST_MANY_HIERARCHY_INVS =
         new TaskKey( 4650, 10 );

   private static final TaskKey APPLICABLE_WP_AGAINST_HIGHEST_INV = new TaskKey( 4650, 31 );
   private static final TaskKey APPLICABLE_RO_WP_AGAINST_HIGHEST_INV = new TaskKey( 4650, 61 );
   private static final TaskKey APPLICABLE_WP_AGAINST_HIGHEST_INV_OF_MANY = new TaskKey( 4650, 81 );
   private static final TaskKey APPLICABLE_WP_AGAINST_INTERMEDIARY_INV = new TaskKey( 4650, 91 );
   private static final TaskKey APPLICABLE_WP_AGAINST_LOWEST_INV = new TaskKey( 4650, 101 );

   private static final EventKey APPLICABLE_WP_EVENT_AGAINST_INTERMEDIARY_INV =
         new EventKey( 4650, 91 );

   private static final InventoryKey INTERMEDIARY_INV_KEY = new InventoryKey( 4650, 4 );
   private static final InventoryKey HIGHEST_INV_KEY = new InventoryKey( 4650, 8 );


   /**
    * Ensures that the correct work package is returned when there is only one applicable RO work
    * package against the highest inventory.
    */
   @Test
   public void testReturnsApplicableRoWorkPackageAgainstHighestInv() {
      Set<TaskKey> lWPs = execute( NSV_TASK_FOR_INV_WITH_APPLICABLE_RO_WP_AGAINST_HIGHEST_INV );
      assertEquals( 1, lWPs.size() );
      assertTrue( lWPs.contains( APPLICABLE_RO_WP_AGAINST_HIGHEST_INV ) );
   }


   /**
    * Ensures that the earliest created work package is returned when more than one applicable work
    * package is against the highest inventory in the hierarchy and they have the same scheduled
    * date.
    */
   @Test
   public void testReturnsEarliestCreatedWorkPackageWhenManySameScheduledWPsExistForHighestInv() {

      // setup the intermediary inventory's work package (which is scheduled earliest) to be against
      // highest inventory
      SchedStaskTable lIntermediaryWP =
            SchedStaskTable.findByPrimaryKey( APPLICABLE_WP_AGAINST_INTERMEDIARY_INV );
      lIntermediaryWP.setMainInventory( HIGHEST_INV_KEY );
      lIntermediaryWP.update();

      // setup the lowest inventory's work package (which is also scheduled earliest) to be against
      // highest inventory
      SchedStaskTable lLowestWP =
            SchedStaskTable.findByPrimaryKey( APPLICABLE_WP_AGAINST_LOWEST_INV );
      lLowestWP.setMainInventory( HIGHEST_INV_KEY );
      lLowestWP.update();

      Set<TaskKey> lWPs =
            execute( NSV_TASK_FOR_INV_WITH_APPLICABLE_WP_AGAINST_MANY_HIERARCHY_INVS );
      assertEquals( 1, lWPs.size() );
      assertTrue( lWPs.contains( APPLICABLE_WP_AGAINST_LOWEST_INV ) );
   }


   /**
    * Ensures that the earliest scheduled work package is returned when more than one applicable
    * work package is against the highest inventory in the hierarchy.
    */
   @Test
   public void testReturnsEarliestScheduledWorkPackageWhenManyApplicableWPsExistForHighestInv() {

      // setup the intermediary inventory's work package (which is scheduled earliest) to be against
      // highest inventory
      SchedStaskTable lIntermediaryWP =
            SchedStaskTable.findByPrimaryKey( APPLICABLE_WP_AGAINST_INTERMEDIARY_INV );
      lIntermediaryWP.setMainInventory( HIGHEST_INV_KEY );
      lIntermediaryWP.update();

      Set<TaskKey> lWPs =
            execute( NSV_TASK_FOR_INV_WITH_APPLICABLE_WP_AGAINST_MANY_HIERARCHY_INVS );
      assertEquals( 1, lWPs.size() );
      assertTrue( lWPs.contains( APPLICABLE_WP_AGAINST_INTERMEDIARY_INV ) );
   }


   /**
    * Ensures that a work package is not returned if there is a work package against a parent
    * inventory of the NSV task's main inventory, but that work package has a state other than ACTV.
    */
   @Test
   public void testReturnsNothingWhenNoActvWorkPackages() {

      // setup work package to have a state of COMMIT
      EvtEventTable lEventTable =
            EvtEventTable.findByPrimaryKey( APPLICABLE_WP_AGAINST_HIGHEST_INV );
      lEventTable.setEventStatus( RefEventStatusKey.COMMIT );
      lEventTable.update();

      Set<TaskKey> lWPs = execute( NSV_TASK_FOR_INV_WITH_APPLICABLE_WP_AGAINST_HIGHEST_INV );
      assertTrue( lWPs.isEmpty() );
   }


   /**
    * Ensures that a work package is not returned if there is a work package against a parent
    * inventory of the NSV task's main inventory, but that work package is historic.
    */
   @Test
   public void testReturnsNothingWhenNoNonHistoricWorkPackages() {

      // setup work package to be historic
      EvtEventTable lEventTable =
            EvtEventTable.findByPrimaryKey( APPLICABLE_WP_AGAINST_HIGHEST_INV );
      lEventTable.setHistBool( true );
      lEventTable.update();

      Set<TaskKey> lWPs = execute( NSV_TASK_FOR_INV_WITH_APPLICABLE_WP_AGAINST_HIGHEST_INV );
      assertTrue( lWPs.isEmpty() );
   }


   /**
    * Ensures that a work package is not returned if there are no work packages against the NSV
    * task's main inventory or its hierarchical inventories.
    */
   @Test
   public void testReturnsNothingWhenNoWorkPackagesExist() {
      Set<TaskKey> lWPs = execute( NSV_TASK_FOR_INV_WHOSE_HIERARCHY_HAS_NO_WPS );
      assertTrue( lWPs.isEmpty() );
   }


   /**
    * Ensures that a work package is not returned if there is a work package against a parent
    * inventory of the NSV task's main inventory, but that work package is not enforcing NSV tasks.
    * <br>
    * <br>
    * Note, we need to reset the state manually and cannot use iForceRebuildData, as the sched_wp
    * table is populated via a db trigger (that does not get triggered when rebuilding the data)
    */
   @Test
   public void testReturnsNothingWhenNoWorkPackagesThatAreEnforcingNsvTasks() {

      // set state of WP to ignore NSV tasks
      SchedWPTable lSchedWPTable =
            SchedWPTable.findByPrimaryKey( new SchedWPKey( APPLICABLE_WP_AGAINST_HIGHEST_INV ) );
      lSchedWPTable.setEnforceNsv( EnforceNsvTasksState.IGNORE_NSV_TASKS );
      lSchedWPTable.update();

      Set<TaskKey> lWPs = execute( NSV_TASK_FOR_INV_WITH_APPLICABLE_WP_AGAINST_HIGHEST_INV );
      assertTrue( lWPs.isEmpty() );
   }


   /**
    * Ensures that when there are applicable work packages against more then one inventory in the
    * inventory hierarchy, that the work package against the highest inventory is returned.
    */
   @Test
   public void testReturnsWorkPackageAgainstHighestInvWhenApplicableWPsExistForManyInv() {
      Set<TaskKey> lWPs =
            execute( NSV_TASK_FOR_INV_WITH_APPLICABLE_WP_AGAINST_MANY_HIERARCHY_INVS );
      assertEquals( 1, lWPs.size() );
      assertTrue( lWPs.contains( APPLICABLE_WP_AGAINST_HIGHEST_INV_OF_MANY ) );
   }


   /**
    * Ensures that the valid work package is returned when there is only one valid work package
    * against the highest inventory.
    */
   @Test
   public void testSuccessWhenOnlyOneApplicableWorkPackageAgainstHighestInv() {
      Set<TaskKey> lWPs = execute( NSV_TASK_FOR_INV_WITH_APPLICABLE_WP_AGAINST_HIGHEST_INV );
      assertEquals( 1, lWPs.size() );
      assertTrue( lWPs.contains( APPLICABLE_WP_AGAINST_HIGHEST_INV ) );
   }


   /**
    * Ensures that the valid work package is returned when there is only one valid work package
    * against an intermediary inventory.
    */
   @Test
   public void testSuccessWhenOnlyOneApplicableWorkPackageAgainstIntermediaryInv() {

      // modify the applicable work package from being against the highest inventory
      // to being against an inventory in the middle of the hierarchy
      SchedStaskTable lStaskTable =
            SchedStaskTable.findByPrimaryKey( APPLICABLE_WP_AGAINST_HIGHEST_INV );
      lStaskTable.setMainInventory( INTERMEDIARY_INV_KEY );
      lStaskTable.update();

      Set<TaskKey> lWPs = execute( NSV_TASK_FOR_INV_WITH_APPLICABLE_WP_AGAINST_HIGHEST_INV );
      assertEquals( 1, lWPs.size() );
      assertTrue( lWPs.contains( APPLICABLE_WP_AGAINST_HIGHEST_INV ) );
   }


   /**
    * Ensures that when unscheduled work packages exist, when more than one applicable work package
    * is against the highest inventory in the hierarchy, that they are considered scheduled last.
    * i.e. if a WP is scheduled then it is chosen over an unscheduled WP
    */
   @Test
   public void testUnscheduledWorkPackagesAreConsisderedScheduledLast() {

      // setup the intermediary inventory's work package to be against
      // highest inventory
      SchedStaskTable lIntermediaryWP =
            SchedStaskTable.findByPrimaryKey( APPLICABLE_WP_AGAINST_INTERMEDIARY_INV );
      lIntermediaryWP.setMainInventory( HIGHEST_INV_KEY );
      lIntermediaryWP.update();

      // setup the intermediary inventory's work package to be unscheduled
      EvtEventTable lIntermediaryEvent =
            EvtEventTable.findByPrimaryKey( APPLICABLE_WP_EVENT_AGAINST_INTERMEDIARY_INV );
      lIntermediaryEvent.setSchedStartDate( null );
      lIntermediaryEvent.update();

      Set<TaskKey> lWPs =
            execute( NSV_TASK_FOR_INV_WITH_APPLICABLE_WP_AGAINST_MANY_HIERARCHY_INVS );
      assertEquals( 1, lWPs.size() );
      assertTrue( lWPs.contains( APPLICABLE_WP_AGAINST_HIGHEST_INV_OF_MANY ) );
   }


   /**
    * Executes the query for a given next shop visit task
    *
    * @param aNsvTaskKey
    *           the NSV task key
    *
    * @return the set of fault tasks
    */
   private Set<TaskKey> execute( TaskKey aNsvTaskKey ) {
      Set<TaskKey> lWpKeys = new HashSet<TaskKey>();

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aNsvTaskKey, "aTaskDbId", "aTaskId" );

      QuerySet lQs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      while ( lQs.next() ) {
         lWpKeys.add( lQs.getKey( TaskKey.class, "sched_db_id", "sched_id" ) );
      }

      return lWpKeys;
   }
}
