
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
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.SchedWPKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.task.SchedWPTable;
import com.mxi.mx.core.table.task.SchedWPTable.EnforceNsvTasksState;


/**
 * Tests the GetWorkPackageForMainInvOfNsvTask query.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class GetWorkPackageForMainInvOfNsvTaskTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void setUp() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
   }


   private static final TaskKey NSV_TASK_FOR_INV_WITH_NO_WPS = new TaskKey( 4650, 1 );
   private static final TaskKey NSV_TASK_FOR_INV_WITH_DEFAULT_EXPECTED_WP = new TaskKey( 4650, 2 );
   private static final TaskKey NSV_TASK_FOR_INV_WITH_VARIOUS_SCHEDULED_WPS =
         new TaskKey( 4650, 3 );
   private static final TaskKey NSV_TASK_FOR_INV_WITH_VARIOUS_CREATION_DATE_WPS =
         new TaskKey( 4650, 4 );
   private static final TaskKey NSV_TASK_FOR_INV_WITH_DEFAULT_EXPECTED_RO_WP =
         new TaskKey( 4650, 5 );

   private static final TaskKey DEFAULT_EXPECTED_WP = new TaskKey( 4650, 21 );
   private static final EventKey DEFAULT_EXPECTED_WP_EVENT_KEY =
         new EventKey( DEFAULT_EXPECTED_WP.getDbId(), DEFAULT_EXPECTED_WP.getId() );

   private static final TaskKey EARLIER_SCHEDULED_WP = new TaskKey( 4650, 31 );
   private static final TaskKey LATER_SCHEDULED_WP = new TaskKey( 4650, 32 );
   private static final TaskKey EARLIER_CREATED_WP = new TaskKey( 4650, 41 );
   private static final TaskKey DEFAULT_EXPECTED_RO_WP = new TaskKey( 4650, 51 );


   /**
    * Ensure that when there are multiple valid work packages, with the same scheduled date, that
    * the one that was created first is returned.
    */
   @Test
   public void testReturnsEarliestCreatedValidWorkPackage() {
      Set<TaskKey> lWPs = execute( NSV_TASK_FOR_INV_WITH_VARIOUS_CREATION_DATE_WPS );
      assertEquals( 1, lWPs.size() );
      assertTrue( lWPs.contains( EARLIER_CREATED_WP ) );
   }


   /**
    * Ensure that when there are multiple valid work packages that the one that is schedule first is
    * returned.
    */
   @Test
   public void testReturnsEarliestScheduledValidWorkPackage() {
      Set<TaskKey> lWPs = execute( NSV_TASK_FOR_INV_WITH_VARIOUS_SCHEDULED_WPS );
      assertEquals( 1, lWPs.size() );
      assertTrue( lWPs.contains( EARLIER_SCHEDULED_WP ) );
   }


   /**
    * Ensures that a work package is not returned if there is a work package against the NSV task's
    * main inventory but that work package has a state other than ACTV.
    */
   @Test
   public void testReturnsNothingWhenNoActvWorkPackages() {

      // setup work package to have a state of COMMIT
      EvtEventTable lEventTable = EvtEventTable.findByPrimaryKey( DEFAULT_EXPECTED_WP_EVENT_KEY );
      lEventTable.setEventStatus( RefEventStatusKey.COMMIT );
      lEventTable.update();

      Set<TaskKey> lWPs = execute( NSV_TASK_FOR_INV_WITH_DEFAULT_EXPECTED_WP );
      assertTrue( lWPs.isEmpty() );
   }


   /**
    * Ensures that a work package is not returned if there is a work package against the NSV task's
    * main inventory but that work package is historic.
    */
   @Test
   public void testReturnsNothingWhenNoNonHistoricWorkPackages() {

      // setup work package to be historic
      EvtEventTable lEventTable = EvtEventTable.findByPrimaryKey( DEFAULT_EXPECTED_WP_EVENT_KEY );
      lEventTable.setHistBool( true );
      lEventTable.update();

      Set<TaskKey> lWPs = execute( NSV_TASK_FOR_INV_WITH_DEFAULT_EXPECTED_WP );
      assertTrue( lWPs.isEmpty() );
   }


   /**
    * Ensures that a work package is not returned if there are no work packages against the NSV
    * task's main inventory.
    */
   @Test
   public void testReturnsNothingWhenNoWorkPackagesExist() {
      Set<TaskKey> lWPs = execute( NSV_TASK_FOR_INV_WITH_NO_WPS );
      assertTrue( lWPs.isEmpty() );
   }


   /**
    * Ensures that a work package is not returned if there is a work package against the NSV task's
    * main inventory but that work package is not enforcing NSV tasks.<br>
    * <br>
    * Note, we need to reset the state manually and cannot use iForceRebuildData, as the sched_wp
    * table is populated via a db trigger (that does not get triggered when rebuilding the data)
    */
   @Test
   public void testReturnsNothingWhenNoWorkPackagesThatAreEnforcingNsvTasks() {

      // set state of WP to ignore NSV tasks
      SchedWPTable lSchedWPTable =
            SchedWPTable.findByPrimaryKey( new SchedWPKey( DEFAULT_EXPECTED_WP ) );
      lSchedWPTable.setEnforceNsv( EnforceNsvTasksState.IGNORE_NSV_TASKS );
      lSchedWPTable.update();

      Set<TaskKey> lWPs = execute( NSV_TASK_FOR_INV_WITH_DEFAULT_EXPECTED_WP );
      assertTrue( lWPs.isEmpty() );
   }


   /**
    * Ensures that the valid work package is returned when there is only one valid work package and
    * that work package is an RO.
    */
   @Test
   public void testReturnsValidRoWorkPackage() {
      Set<TaskKey> lWPs = execute( NSV_TASK_FOR_INV_WITH_DEFAULT_EXPECTED_RO_WP );
      assertEquals( 1, lWPs.size() );
      assertTrue( lWPs.contains( DEFAULT_EXPECTED_RO_WP ) );
   }


   /**
    * Ensures that the valid work package is returned when there is only one valid work package.
    */
   @Test
   public void testSuccessWhenOnlyOneValidWorkPackage() {
      Set<TaskKey> lWPs = execute( NSV_TASK_FOR_INV_WITH_DEFAULT_EXPECTED_WP );
      assertEquals( 1, lWPs.size() );
      assertTrue( lWPs.contains( DEFAULT_EXPECTED_WP ) );
   }


   /**
    * Ensure that when there are multiple valid work packages that the one that is schedule first is
    * returned. Also ensure that work packages with no scheduled date are considered scheduled last.
    */
   @Test
   public void testUnscheduledWorkPackagesAreConsideredScheduledLast() {

      // remove the scheduled date of the earlier scheduled work package
      EvtEventTable lEventTable = EvtEventTable.findByPrimaryKey( EARLIER_SCHEDULED_WP );
      lEventTable.setSchedStartDate( null );
      lEventTable.update();

      // now the expected WP is the later scheduled
      Set<TaskKey> lWPs = execute( NSV_TASK_FOR_INV_WITH_VARIOUS_SCHEDULED_WPS );
      assertEquals( 1, lWPs.size() );
      assertTrue( lWPs.contains( LATER_SCHEDULED_WP ) );
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
