
package com.mxi.mx.core.services.stask.nsv;

import static com.mxi.mx.testing.matchers.MxMatchers.query;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.internationalization.StringBundles;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QueryAccessObject;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.common.utils.DataTypeUtils;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.SchedWPKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.stask.ManageChildTaskService;
import com.mxi.mx.core.services.stask.nsv.ManageNsvTasksTO.Mode;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.table.task.SchedWPTable;
import com.mxi.mx.core.table.task.SchedWPTable.EnforceNsvTasksState;
import com.mxi.mx.testing.FakeStringBundles;


/**
 * Unit tests for the default Next Shop Visit Tasks service.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class DefaultNsvTaskServiceTest {

   private static final EventKey WP_EVENT_KEY = new EventKey( 1, 1 );
   private static final TaskKey WP_KEY = new TaskKey( WP_EVENT_KEY );
   private static final SchedWPKey SCHED_WP_KEY = new SchedWPKey( WP_KEY );
   private static final InventoryKey WP_MAIN_INV_KEY = new InventoryKey( 2, 2 );

   private static final TaskKey UNKNOWN_WP_KEY = new TaskKey( 0, 0 );

   private static final String NOTE = "NOTE";
   private static final String REASON = "REASON";
   private static final HumanResourceKey HR_KEY = new HumanResourceKey( 3, 3 );
   private static final String SYSTEM_NOTE = "NOTE";

   private static final EventKey NSV_EVENT_KEY_1 = new EventKey( 10, 1 );
   private static final EventKey NSV_EVENT_KEY_2 = new EventKey( 10, 2 );

   private static final TaskKey NSV_TASK_KEY_1 = new TaskKey( NSV_EVENT_KEY_1 );
   private static final TaskKey NSV_TASK_KEY_2 = new TaskKey( NSV_EVENT_KEY_2 );

   private static final String NSV_TASK_SDESC_1 = "NSV_TASK_SDESC_1";
   private static final String NSV_TASK_SDESC_2 = "NSV_TASK_SDESC_2";

   // needed to make a mockery of the test
   private final Mockery iContext = new Mockery();

   // create some test transfer objects
   private ManageNsvTasksTO iEnforceTO;
   private ManageNsvTasksTO iIgnoreTO;
   private ManageNsvTasksTO iInvalidWpTO;
   private final ManageChildTaskService iMockManageChildTaskService =
         iContext.mock( ManageChildTaskService.class );
   private final NsvTaskUtils iMockNsvTaskUtils = iContext.mock( NsvTaskUtils.class );

   // create the mock object to use during testing
   private final QueryAccessObject iMockQao = iContext.mock( QueryAccessObject.class );
   private final NsvTaskUnassignValidator iMockValidator =
         iContext.mock( NsvTaskUnassignValidator.class );

   // create an instance of the service to test
   private NsvTaskService iNsvTasksService;
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Creates a new {@linkplain DefaultNsvTaskServiceTest} object.
    */
   public DefaultNsvTaskServiceTest() {
      super();
   }


   /**
    * Tests that the NSV task is not assigned if there are no applicable work packages.
    *
    * @throws Exception
    */
   @Test
   public void testAssignNsvTaskDoesNothingWhenNoApplicableWorkPackages() throws Exception {
      iContext.checking( new Expectations() {

         {

            // simulate not finding a work package
            one( iMockNsvTaskUtils ).findWorkPackageToAssignNsvTask( NSV_TASK_KEY_1 );
            will( returnValue( null ) );

            // ensure the NSV task is not assigned to a work package
            never( iMockManageChildTaskService );
         }
      } );

      iNsvTasksService.assignNsvTaskToApplicableWorkPackage( NSV_TASK_KEY_1, HR_KEY );

      iContext.assertIsSatisfied();
   }


   /**
    * Tests that the NSV task is assigned to the found applicable work package.
    *
    * @throws Exception
    */
   @Test
   public void testAssignNsvTaskToApplicableWorkPackageSucceeds() throws Exception {
      iContext.checking( new Expectations() {

         {

            // simulate the finding of a work package
            one( iMockNsvTaskUtils ).findWorkPackageToAssignNsvTask( NSV_TASK_KEY_1 );
            will( returnValue( WP_KEY ) );

            // simulate the generation of an assignment system note
            one( iMockNsvTaskUtils ).getAssignmentSystemNote( WP_KEY );
            will( returnValue( NOTE ) );

            // ensure the NSV task is assigned to the work package
            one( iMockManageChildTaskService ).addChildTask( WP_KEY, NSV_TASK_KEY_1, HR_KEY, null,
                  null, NOTE );
         }
      } );

      iNsvTasksService.assignNsvTaskToApplicableWorkPackage( NSV_TASK_KEY_1, HR_KEY );

      iContext.assertIsSatisfied();
   }


   /**
    * Tests that an execution is thrown if the Task Bean cannot assign a task to the work package.
    *
    * @throws Exception
    */
   @Test
   public void testEnforceFailsWhenAssigningTaskToWorkpackageHasError() throws Exception {

      // set up test data to have a sched_wp row with its enforce_nsv set to ignore
      SchedWPTable lSchedWPTable = SchedWPTable.create( SCHED_WP_KEY );
      lSchedWPTable.setEnforceNsv( EnforceNsvTasksState.IGNORE_NSV_TASKS );
      lSchedWPTable.insert();

      iContext.checking( new Expectations() {

         {

            // simulate the retrieval of assignable NSV tasks
            one( iMockNsvTaskUtils ).getAssignableNsvTasks( WP_KEY );
            will( returnValue( getAssignableNsvTasks() ) );

            // simulate the generation of an assignment system note
            one( iMockNsvTaskUtils ).getAssignmentSystemNote( WP_KEY );
            will( returnValue( "" ) );

            // simulate a failure within addChildTask()
            allowing( iMockManageChildTaskService ).addChildTask( with( any( TaskKey.class ) ),
                  with( any( TaskKey.class ) ), with( any( HumanResourceKey.class ) ),
                  with( any( String.class ) ), with( any( String.class ) ),
                  with( any( String.class ) ) );
            will( throwException( new TriggerException() ) );
         }
      } );

      try {
         iNsvTasksService.manageNsvTasks( iEnforceTO );
         fail( "Expected TriggerException" );
      } catch ( TriggerException lEx ) {
         ; // expected
      }

      iContext.assertIsSatisfied();
   }


   /**
    * Tests that the work package status is successfully updated to ENFORCE_NSV in the SCHED_WP
    * table.
    *
    * @throws Exception
    */
   @Test
   public void testEnforceUpdatesWorkpackageStatus() throws Exception {

      // set up test data to have a sched_wp row with its enforce_nsv set to ignore
      SchedWPTable lSchedWPTable = SchedWPTable.create( SCHED_WP_KEY );
      lSchedWPTable.setEnforceNsv( EnforceNsvTasksState.IGNORE_NSV_TASKS );
      lSchedWPTable.insert();

      iContext.checking( new Expectations() {

         {

            // simulate the retrieval of assignable NSV tasks
            one( iMockNsvTaskUtils ).getAssignableNsvTasks( WP_KEY );
            will( returnValue( new HashSet<TaskKey>() ) );
         }
      } );

      iNsvTasksService.manageNsvTasks( iEnforceTO );

      iContext.assertIsSatisfied();

      // verify that the row was updated
      lSchedWPTable = SchedWPTable.findByPrimaryKey( SCHED_WP_KEY );
      assertEquals( EnforceNsvTasksState.ENFORCE_NSV_TASKS, lSchedWPTable.getEnforceNsv() );
   }


   /**
    * Tests that no tasks are assigned to the work package if there are no candidate NSV tasks.
    *
    * @throws Exception
    */
   @Test
   public void testEnforceWithNoNsvTasks() throws Exception {

      // set up test data to have a sched_wp row with its enforce_nsv set to ignore
      SchedWPTable lSchedWPTable = SchedWPTable.create( SCHED_WP_KEY );
      lSchedWPTable.setEnforceNsv( EnforceNsvTasksState.IGNORE_NSV_TASKS );
      lSchedWPTable.insert();

      iContext.checking( new Expectations() {

         {

            // simulate the retrieval of no assignable NSV tasks
            one( iMockNsvTaskUtils ).getAssignableNsvTasks( WP_KEY );
            will( returnValue( new HashSet<TaskKey>() ) );

            // ensure that no tasks are added to the work package
            never( iMockManageChildTaskService ).addChildTask( with( equal( WP_KEY ) ),
                  with( any( TaskKey.class ) ), with( any( HumanResourceKey.class ) ),
                  with( any( String.class ) ), with( any( String.class ) ),
                  with( any( String.class ) ) );
         }
      } );

      iNsvTasksService.manageNsvTasks( iEnforceTO );

      iContext.assertIsSatisfied();
   }


   /**
    * Tests that all the candidate NSV tasks are assigned to the work package.
    *
    * @throws Exception
    */
   @Test
   public void testEnforceWithNsvTasks() throws Exception {

      // set up test data to have a sched_wp row with its enforce_nsv set to ignore
      SchedWPTable lSchedWPTable = SchedWPTable.create( SCHED_WP_KEY );
      lSchedWPTable.setEnforceNsv( EnforceNsvTasksState.IGNORE_NSV_TASKS );
      lSchedWPTable.insert();

      iContext.checking( new Expectations() {

         {

            // simulate the retrieval of assignable NSV tasks
            one( iMockNsvTaskUtils ).getAssignableNsvTasks( WP_KEY );
            will( returnValue( getAssignableNsvTasks() ) );

            // simulate the generation of an assignment system note
            one( iMockNsvTaskUtils ).getAssignmentSystemNote( WP_KEY );
            will( returnValue( SYSTEM_NOTE ) );

            // ensure that the tasks are added to the work package
            one( iMockManageChildTaskService ).addChildTask( with( equal( WP_KEY ) ),
                  with( equal( NSV_TASK_KEY_1 ) ), with( equal( HR_KEY ) ), with( equal( REASON ) ),
                  with( equal( NOTE ) ), with( equal( SYSTEM_NOTE ) ) );
            one( iMockManageChildTaskService ).addChildTask( with( equal( WP_KEY ) ),
                  with( equal( NSV_TASK_KEY_2 ) ), with( equal( HR_KEY ) ), with( equal( REASON ) ),
                  with( equal( NOTE ) ), with( equal( SYSTEM_NOTE ) ) );
         }
      } );

      iNsvTasksService.manageNsvTasks( iEnforceTO );

      iContext.assertIsSatisfied();
   }


   /**
    * Tests that all the candidate NSV tasks are unassigned from the work package.
    *
    * @throws Exception
    */
   @Test
   public void testIgnoreFailsWhenUnassigningTaskFromWpHasError() throws Exception {

      // set up test data to have a sched_wp row with its enforce_nsv set to enforce
      SchedWPTable lSchedWPTable = SchedWPTable.create( SCHED_WP_KEY );
      lSchedWPTable.setEnforceNsv( EnforceNsvTasksState.ENFORCE_NSV_TASKS );
      lSchedWPTable.insert();

      // set up a data set of test nsv task keys
      final DataSet lNsvTaskKeyDs = getNsvTaskKeyDataSet();

      iContext.checking( new Expectations() {

         {

            // simulate the GetNsvTasksAssignedToWorkPackage query returning tasks
            one( iMockQao ).executeQuery(
                  with( query(
                        "com.mxi.mx.core.query.stask.nsv.GetNsvTasksAssignedToWorkPackage" ) ),
                  with( any( DataSetArgument.class ) ) );
            will( returnValue( lNsvTaskKeyDs ) );

            // simulate the generation of an unassignment system note
            one( iMockNsvTaskUtils ).getUnassignmentSystemNote( WP_KEY );
            will( returnValue( SYSTEM_NOTE ) );

            // satisfy the call to validate the nsv task
            one( iMockValidator ).isValid( with( any( TaskKey.class ) ) );
            will( returnValue( true ) );

            // simulate the removeChildTask throwing an exception
            one( iMockManageChildTaskService ).removeChildTask( with( any( TaskKey.class ) ),
                  with( any( TaskKey.class ) ), with( any( HumanResourceKey.class ) ),
                  with( any( String.class ) ), with( any( String.class ) ),
                  with( any( String.class ) ) );
            will( throwException( new TriggerException() ) );
         }
      } );

      try {
         iNsvTasksService.manageNsvTasks( iIgnoreTO );
         fail( "Expected TriggerException" );
      } catch ( TriggerException lEx ) {
         ; // expected
      }

      iContext.assertIsSatisfied();
   }


   /**
    * Tests that the work package status is successfully updated to IGNORE_NSV in the SCHED_WP
    * table.
    *
    * @throws Exception
    */
   @Test
   public void testIgnoreUpdatesWorkpackageStatus() throws Exception {

      // set up test data to have a sched_wp row with its enforce_nsv set to enforce
      SchedWPTable lSchedWPTable = SchedWPTable.create( SCHED_WP_KEY );
      lSchedWPTable.setEnforceNsv( EnforceNsvTasksState.ENFORCE_NSV_TASKS );
      lSchedWPTable.insert();

      iContext.checking( new Expectations() {

         {

            // simulate the GetNsvTasksAssignedToWorkPackage query returning no tasks
            one( iMockQao ).executeQuery(
                  with( query(
                        "com.mxi.mx.core.query.stask.nsv.GetNsvTasksAssignedToWorkPackage" ) ),
                  with( any( DataSetArgument.class ) ) );
            will( returnValue( new DataSet() ) );
         }
      } );

      iNsvTasksService.manageNsvTasks( iIgnoreTO );

      // verify that the row was updated
      lSchedWPTable = SchedWPTable.findByPrimaryKey( SCHED_WP_KEY );
      assertEquals( EnforceNsvTasksState.IGNORE_NSV_TASKS, lSchedWPTable.getEnforceNsv() );

      iContext.assertIsSatisfied();
   }


   /**
    * Tests that invalid tasks are not unassigned from the work package.
    *
    * @throws Exception
    */
   @Test
   public void testIgnoreWithInvalidNsvTasks() throws Exception {

      // set up test data to have a sched_wp row with its enforce_nsv set to ignore
      SchedWPTable lSchedWPTable = SchedWPTable.create( SCHED_WP_KEY );
      lSchedWPTable.setEnforceNsv( EnforceNsvTasksState.IGNORE_NSV_TASKS );
      lSchedWPTable.insert();

      iContext.checking( new Expectations() {

         {

            // simulate the GetNsvTasksAssignedToWorkPackage query returning no tasks
            one( iMockQao ).executeQuery(
                  with( query(
                        "com.mxi.mx.core.query.stask.nsv.GetNsvTasksAssignedToWorkPackage" ) ),
                  with( any( DataSetArgument.class ) ) );
            will( returnValue( getNsvTaskKeyDataSet() ) );

            // simulate the generation of an unassignment system note
            one( iMockNsvTaskUtils ).getUnassignmentSystemNote( WP_KEY );
            will( returnValue( SYSTEM_NOTE ) );

            // setup mock calls for the first nsv task to be invalid, thus NOT unassigned

            // satisfy the call to validate the nsv task
            one( iMockValidator ).isValid( with( equal( NSV_TASK_KEY_1 ) ) );
            will( returnValue( false ) );

            // ensure that the invalid nsv task is NOT unassigned from the work package
            never( iMockManageChildTaskService ).removeChildTask( with( equal( WP_KEY ) ),
                  with( equal( NSV_TASK_KEY_1 ) ), with( equal( HR_KEY ) ), with( equal( REASON ) ),
                  with( equal( NOTE ) ), with( equal( SYSTEM_NOTE ) ) );

            // setup mock calls for the second nsv task to be valid, thus unassigned

            // satisfy the call to validate the nsv task
            one( iMockValidator ).isValid( with( equal( NSV_TASK_KEY_2 ) ) );
            will( returnValue( true ) );

            // ensure that the valid nsv task is unassigned from the work package
            one( iMockManageChildTaskService ).removeChildTask( with( equal( WP_KEY ) ),
                  with( equal( NSV_TASK_KEY_2 ) ), with( equal( HR_KEY ) ), with( equal( REASON ) ),
                  with( equal( NOTE ) ), with( equal( SYSTEM_NOTE ) ) );
         }
      } );

      iNsvTasksService.manageNsvTasks( iIgnoreTO );

      iContext.assertIsSatisfied();
   }


   /**
    * Tests that no tasks are unassigned from the work package if there are no candidate NSV tasks.
    *
    * @throws Exception
    */
   @Test
   public void testIgnoreWithNoNsvTasks() throws Exception {

      // set up test data to have a sched_wp row with its enforce_nsv set to ignore
      SchedWPTable lSchedWPTable = SchedWPTable.create( SCHED_WP_KEY );
      lSchedWPTable.setEnforceNsv( EnforceNsvTasksState.IGNORE_NSV_TASKS );
      lSchedWPTable.insert();

      iContext.checking( new Expectations() {

         {

            // simulate the GetNsvTasksAssignedToWorkPackage query returning no tasks
            one( iMockQao ).executeQuery(
                  with( query(
                        "com.mxi.mx.core.query.stask.nsv.GetNsvTasksAssignedToWorkPackage" ) ),
                  with( any( DataSetArgument.class ) ) );
            will( returnValue( new DataSet() ) );

            // ensure that no nsv tasks are unassigned from the work package
            never( iMockManageChildTaskService ).removeChildTask( with( equal( WP_KEY ) ),
                  with( any( TaskKey.class ) ), with( any( HumanResourceKey.class ) ),
                  with( any( String.class ) ), with( any( String.class ) ),
                  with( any( String.class ) ) );
         }
      } );

      iNsvTasksService.manageNsvTasks( iIgnoreTO );

      iContext.assertIsSatisfied();
   }


   /**
    * Tests that all the candidate NSV tasks are unassigned from the work package.
    *
    * @throws Exception
    */
   @Test
   public void testIgnoreWithNsvTasks() throws Exception {

      // set up test data to have a sched_wp row with its enforce_nsv set to enforce
      SchedWPTable lSchedWPTable = SchedWPTable.create( SCHED_WP_KEY );
      lSchedWPTable.setEnforceNsv( EnforceNsvTasksState.ENFORCE_NSV_TASKS );
      lSchedWPTable.insert();

      iContext.checking( new Expectations() {

         {

            // simulate the GetNsvTasksAssignedToWorkPackage query returning tasks
            one( iMockQao ).executeQuery(
                  with( query(
                        "com.mxi.mx.core.query.stask.nsv.GetNsvTasksAssignedToWorkPackage" ) ),
                  with( any( DataSetArgument.class ) ) );
            will( returnValue( getNsvTaskKeyDataSet() ) );

            // simulate the generation of an unassignment system note
            one( iMockNsvTaskUtils ).getUnassignmentSystemNote( WP_KEY );
            will( returnValue( SYSTEM_NOTE ) );

            // setup mock calls for the first nsv task

            // satisfy the call to validate the nsv task
            one( iMockValidator ).isValid( with( equal( NSV_TASK_KEY_1 ) ) );
            will( returnValue( true ) );

            // ensure that the nsv task is unassigned from the work package
            one( iMockManageChildTaskService ).removeChildTask( with( equal( WP_KEY ) ),
                  with( equal( NSV_TASK_KEY_1 ) ), with( equal( HR_KEY ) ), with( equal( REASON ) ),
                  with( equal( NOTE ) ), with( equal( SYSTEM_NOTE ) ) );

            // setup mock calls for the second nsv task

            // satisfy the call to validate the task
            one( iMockValidator ).isValid( with( equal( NSV_TASK_KEY_2 ) ) );
            will( returnValue( true ) );

            // ensure that the nsv task is unassigned from the work package
            one( iMockManageChildTaskService ).removeChildTask( with( equal( WP_KEY ) ),
                  with( equal( NSV_TASK_KEY_2 ) ), with( equal( HR_KEY ) ), with( equal( REASON ) ),
                  with( equal( NOTE ) ), with( equal( SYSTEM_NOTE ) ) );
         }
      } );

      iNsvTasksService.manageNsvTasks( iIgnoreTO );

      iContext.assertIsSatisfied();
   }


   /**
    * {@inheritDoc}
    */
   @Before
   public void loadData() throws Exception {
      // set up the test enforce nsv tasks TO
      iEnforceTO = new ManageNsvTasksTO();
      iEnforceTO.setMode( Mode.ENFORCE_NSV_TASKS );
      iEnforceTO.setCompWorkpackageKey( WP_KEY );
      iEnforceTO.setNote( NOTE );
      iEnforceTO.setReason( REASON );
      iEnforceTO.setUserHrKey( HR_KEY );

      // set up the test ignore nsv tasks TO
      iIgnoreTO = new ManageNsvTasksTO();
      iIgnoreTO.setMode( Mode.IGNORE_NSV_TASKS );
      iIgnoreTO.setCompWorkpackageKey( WP_KEY );
      iIgnoreTO.setNote( NOTE );
      iIgnoreTO.setReason( REASON );
      iIgnoreTO.setUserHrKey( HR_KEY );

      // set up the test TO with an invalid WP Key
      iInvalidWpTO = new ManageNsvTasksTO();
      iInvalidWpTO.setCompWorkpackageKey( UNKNOWN_WP_KEY );

      setupTestDbTables();

      // stub the StringBundles
      StringBundles.setSingleton( new FakeStringBundles() );

      // create an NSV tasks Service to test
      iNsvTasksService = new DefaultNsvTaskService( iMockQao, iMockValidator,
            iMockManageChildTaskService, iMockNsvTaskUtils );
   }


   /**
    * Returns a set of NSV task keys for testing against.
    *
    * @return set of NSV task keys
    */
   private Set<TaskKey> getAssignableNsvTasks() {

      Set<TaskKey> lAssignableNsvTasks = new HashSet<TaskKey>();
      lAssignableNsvTasks.add( NSV_TASK_KEY_1 );
      lAssignableNsvTasks.add( NSV_TASK_KEY_2 );

      return lAssignableNsvTasks;
   }


   /**
    * Returns a test dataset of NSV Tasks keys.
    *
    * @return
    */
   private DataSet getNsvTaskKeyDataSet() {

      // set up the NSV Tasks data set
      DataSet lDs = new DataSet( new String[] { "SCHED_DB_ID", "SCHED_ID" },
            new String[] { DataTypeUtils.INTEGER, DataTypeUtils.INTEGER } );
      lDs.addRow( new Object[] { NSV_TASK_KEY_1.getDbId(), NSV_TASK_KEY_1.getId() } );
      lDs.addRow( new Object[] { NSV_TASK_KEY_2.getDbId(), NSV_TASK_KEY_2.getId() } );

      return lDs;
   }


   /**
    * Set up the data in the various test tables.
    */
   private void setupTestDbTables() {

      // set up test data to have a sched_stask row with its main inv key set
      SchedStaskTable lSchedStaskTable = SchedStaskTable.create( WP_KEY );
      lSchedStaskTable.setMainInventory( WP_MAIN_INV_KEY );
      lSchedStaskTable.insert();

      // set up test data to have the work package event have an ACTV status
      EvtEventTable lEvtEventTable;
      lEvtEventTable = EvtEventTable.create( WP_EVENT_KEY );
      lEvtEventTable.setEventStatus( RefEventStatusKey.ACTV );
      lEvtEventTable.insert();

      // set up test data to have a sdesc set in the evt_event rows for the nsv tasks
      lEvtEventTable = EvtEventTable.create( NSV_EVENT_KEY_1 );
      lEvtEventTable.setEventSdesc( NSV_TASK_SDESC_1 );
      lEvtEventTable.insert();
      lEvtEventTable = EvtEventTable.create( NSV_EVENT_KEY_2 );
      lEvtEventTable.setEventSdesc( NSV_TASK_SDESC_2 );
      lEvtEventTable.insert();
   }
}
