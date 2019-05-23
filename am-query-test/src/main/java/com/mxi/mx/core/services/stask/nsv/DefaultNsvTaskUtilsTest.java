
package com.mxi.mx.core.services.stask.nsv;

import static com.mxi.mx.testing.matchers.MxMatchers.query;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.internationalization.StringBundles;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QueryAccessObject;
import com.mxi.mx.common.utils.DataTypeUtils;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefTaskMustRemoveKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.table.task.TaskTaskTable;


/**
 * Unit tests for the default Next Shop Visit Task utils.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class DefaultNsvTaskUtilsTest {

   private static final TaskTaskKey NSV_TASK_DEFN_KEY = new TaskTaskKey( 1, 1 );

   private static final TaskKey NSV_TASK_KEY = new TaskKey( 1, 1 );

   private static final EventKey WP_EVENT_KEY = new EventKey( 2, 1 );
   private static final TaskKey WP_KEY = new TaskKey( WP_EVENT_KEY );

   private static final String ASSIGNED_DURING_EXECUTION_MSG =
         "core.msg.NSV_TASK_ASSIGNED_TO_WP_DURING_EXECUTION";
   private static final String ASSIGNED_DURING_PLANNING_MSG =
         "core.msg.NSV_TASK_ASSIGNED_TO_WP_DURING_PLANNING";

   private static final String UNASSIGNED_DURING_EXECUTION_MSG =
         "core.msg.NSV_TASK_UNASSIGNED_FROM_WP_DURING_EXECUTION";
   private static final String UNASSIGNED_DURING_PLANNING_MSG =
         "core.msg.NSV_TASK_UNASSIGNED_FROM_WP_DURING_PLANNING";

   // needed to make a mockery of the test
   private final Mockery iContext = new Mockery();

   // create the mock object to use during testing
   private final QueryAccessObject iMockQao = iContext.mock( QueryAccessObject.class );

   // create an instance of the utils to test
   private NsvTaskUtils iNsvTaskUtils;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Tests that the correct work package key is returned when a work package is found against the
    * provided NSV task's main inventory.
    */
   @Test
   public void testFindApplicableWpForMainInvOfNsvTask() {
      iContext.checking( new Expectations() {

         {

            // simulate the GetWorkPackageForMainInvOfNsvTask query returning a WP
            one( iMockQao ).executeQuery(
                  with( query(
                        "com.mxi.mx.core.query.stask.nsv.GetWorkPackageForMainInvOfNsvTask" ) ),
                  with( any( DataSetArgument.class ) ) );
            will( returnValue( getWpKeyDataSet( WP_KEY ) ) );
         }
      } );

      // execute and verify the test
      assertEquals( WP_KEY, iNsvTaskUtils.findWorkPackageToAssignNsvTask( NSV_TASK_KEY ) );
   }


   /**
    * Tests that the correct work package key is returned when no work package is found against the
    * provided NSV task's main inventory but when the NSV task is marked off-wing and a work package
    * is found against the inventory's parent.
    */
   @Test
   public void testFindApplicableWpForParentInvOfMainInvOfNsvTask() {

      // set up test data to mark the NSV task as off-wing
      TaskTaskTable lTaskDefnTable = TaskTaskTable.create( NSV_TASK_DEFN_KEY );
      lTaskDefnTable.setTaskMustRemove( RefTaskMustRemoveKey.OFFWING );
      lTaskDefnTable.insert();

      SchedStaskTable lStaskTable = SchedStaskTable.create( NSV_TASK_KEY );
      lStaskTable.setTaskTaskKey( NSV_TASK_DEFN_KEY );
      lStaskTable.insert();

      iContext.checking( new Expectations() {

         {

            // simulate the GetWorkPackageForMainInvOfNsvTask query returning nothing
            one( iMockQao ).executeQuery(
                  with( query(
                        "com.mxi.mx.core.query.stask.nsv.GetWorkPackageForMainInvOfNsvTask" ) ),
                  with( any( DataSetArgument.class ) ) );
            will( returnValue( new DataSet() ) );

            // simulate the GetWorkPackageFromInvHierarchyOfNsvTask query returning a WP
            one( iMockQao ).executeQuery(
                  with( query(
                        "com.mxi.mx.core.query.stask.nsv.GetWorkPackageFromInvHierarchyOfNsvTask" ) ),
                  with( any( DataSetArgument.class ) ) );
            will( returnValue( getWpKeyDataSet( WP_KEY ) ) );
         }
      } );

      // execute and verify the test
      assertEquals( WP_KEY, iNsvTaskUtils.findWorkPackageToAssignNsvTask( NSV_TASK_KEY ) );
   }


   /**
    * Tests that the no work package key is returned when there are no work packages found against
    * the provided NSV task's main inventory nor against the inventory's parents.
    */
   @Test
   public void testFindApplicableWpReturnsNothingIfNoWPsExist() {

      // set up test data to mark the NSV task as off-wing
      TaskTaskTable lTaskDefnTable = TaskTaskTable.create( NSV_TASK_DEFN_KEY );
      lTaskDefnTable.setTaskMustRemove( RefTaskMustRemoveKey.OFFWING );
      lTaskDefnTable.insert();

      SchedStaskTable lStaskTable = SchedStaskTable.create( NSV_TASK_KEY );
      lStaskTable.setTaskTaskKey( NSV_TASK_DEFN_KEY );
      lStaskTable.insert();

      iContext.checking( new Expectations() {

         {

            // simulate the GetWorkPackageForMainInvOfNsvTask query returning nothing
            one( iMockQao ).executeQuery(
                  with( query(
                        "com.mxi.mx.core.query.stask.nsv.GetWorkPackageForMainInvOfNsvTask" ) ),
                  with( any( DataSetArgument.class ) ) );
            will( returnValue( new DataSet() ) );

            // simulate the GetWorkPackageFromInvHierarchyOfNsvTask query returning a WP
            one( iMockQao ).executeQuery(
                  with( query(
                        "com.mxi.mx.core.query.stask.nsv.GetWorkPackageFromInvHierarchyOfNsvTask" ) ),
                  with( any( DataSetArgument.class ) ) );
            will( returnValue( new DataSet() ) );
         }
      } );

      // execute and verify the test
      assertNull( iNsvTaskUtils.findWorkPackageToAssignNsvTask( NSV_TASK_KEY ) );
   }


   /**
    * Tests that the no work package key is returned when there are no work packages found against
    * the provided NSV task's main inventory and the NSV task is not marked off-wing.
    */
   @Test
   public void testFindApplicableWpReturnsNothingIfNoWPsExistNsvTaskInvAndNsvTaskIsNotOffWing() {

      // set up test data to mark the NSV task as off-parent (not off-wing)
      TaskTaskTable lTaskDefnTable = TaskTaskTable.create( NSV_TASK_DEFN_KEY );
      lTaskDefnTable.setTaskMustRemove( RefTaskMustRemoveKey.OFFPARENT );
      lTaskDefnTable.insert();

      SchedStaskTable lStaskTable = SchedStaskTable.create( NSV_TASK_KEY );
      lStaskTable.setTaskTaskKey( NSV_TASK_DEFN_KEY );
      lStaskTable.insert();

      iContext.checking( new Expectations() {

         {

            // simulate the GetWorkPackageForMainInvOfNsvTask query returning nothing
            one( iMockQao ).executeQuery(
                  with( query(
                        "com.mxi.mx.core.query.stask.nsv.GetWorkPackageForMainInvOfNsvTask" ) ),
                  with( any( DataSetArgument.class ) ) );
            will( returnValue( new DataSet() ) );
         }
      } );

      // execute and verify the test
      assertNull( iNsvTaskUtils.findWorkPackageToAssignNsvTask( NSV_TASK_KEY ) );
   }


   /**
    * Tests that the correct assignment system note is generated when the work package is in
    * execution.
    */
   @Test
   public void testGetAssignSysNoteWhenWpInExecution() {

      // set up test data to have the work package be in work (thus in execution)
      EvtEventTable lEventTable = EvtEventTable.create( WP_EVENT_KEY );
      lEventTable.setEventStatus( RefEventStatusKey.IN_WORK );
      lEventTable.insert();

      // set up test data to set the WO of the work package
      SchedStaskTable lStaskTable = SchedStaskTable.create( WP_KEY );
      lStaskTable.setWoRefSdesc( "" );
      lStaskTable.insert();

      // execute and verify the test
      assertEquals( i18n.get( ASSIGNED_DURING_EXECUTION_MSG, "N/A" ),
            iNsvTaskUtils.getAssignmentSystemNote( WP_KEY ) );
   }


   /**
    * Tests that the correct assignment system note is generated when the work package is in
    * planning. Planning is considered either active or committed.
    */
   @Test
   public void testGetAssignSysNoteWhenWpInPlanning() {

      // set up test data to have the work package be active (thus in planning)
      EvtEventTable lEventTable = EvtEventTable.create( WP_EVENT_KEY );
      lEventTable.setEventStatus( RefEventStatusKey.ACTV );
      lEventTable.insert();

      // set up test data to set the WO of the work package
      SchedStaskTable lStaskTable = SchedStaskTable.create( WP_KEY );
      lStaskTable.setWoRefSdesc( "" );
      lStaskTable.insert();

      // execute and verify the test
      assertEquals( i18n.get( ASSIGNED_DURING_PLANNING_MSG, "N/A" ),
            iNsvTaskUtils.getAssignmentSystemNote( WP_KEY ) );

      // set up test data to have the work package be committed (thus in planning)
      lEventTable = EvtEventTable.findByPrimaryKey( WP_EVENT_KEY );
      lEventTable.setEventStatus( RefEventStatusKey.ACTV );
      lEventTable.update();

      // execute and verify the test
      assertEquals( i18n.get( ASSIGNED_DURING_PLANNING_MSG, "N/A" ),
            iNsvTaskUtils.getAssignmentSystemNote( WP_KEY ) );
   }


   /**
    * Tests that the correct unassignment system note is generated when the work package is in
    * execution.
    */
   @Test
   public void testGetUnassignSysNoteWhenWpInExecution() {

      // set up test data to have the work package be in work (thus in execution)
      EvtEventTable lEventTable = EvtEventTable.create( WP_EVENT_KEY );
      lEventTable.setEventStatus( RefEventStatusKey.IN_WORK );
      lEventTable.insert();

      // set up test data to set the WO of the work package
      SchedStaskTable lStaskTable = SchedStaskTable.create( WP_KEY );
      lStaskTable.setWoRefSdesc( "" );
      lStaskTable.insert();

      // execute and verify the test
      assertEquals( i18n.get( UNASSIGNED_DURING_EXECUTION_MSG, "N/A" ),
            iNsvTaskUtils.getUnassignmentSystemNote( WP_KEY ) );
   }


   /**
    * Tests that the correct unassignment system note is generated when the work package is in
    * planning. Planning is considered either active or committed.
    */
   @Test
   public void testGetUnassignSysNoteWhenWpInPlanning() {

      // set up test data to have the work package be active (thus in planning)
      EvtEventTable lEventTable = EvtEventTable.create( WP_EVENT_KEY );
      lEventTable.setEventStatus( RefEventStatusKey.ACTV );
      lEventTable.insert();

      // set up test data to set the WO of the work package
      SchedStaskTable lStaskTable = SchedStaskTable.create( WP_KEY );
      lStaskTable.setWoRefSdesc( "" );
      lStaskTable.insert();

      // execute and verify the test
      assertEquals( i18n.get( UNASSIGNED_DURING_PLANNING_MSG, "N/A" ),
            iNsvTaskUtils.getUnassignmentSystemNote( WP_KEY ) );

      // set up test data to have the work package be committed (thus in planning)
      lEventTable = EvtEventTable.findByPrimaryKey( WP_EVENT_KEY );
      lEventTable.setEventStatus( RefEventStatusKey.ACTV );
      lEventTable.update();

      // execute and verify the test
      assertEquals( i18n.get( UNASSIGNED_DURING_PLANNING_MSG, "N/A" ),
            iNsvTaskUtils.getUnassignmentSystemNote( WP_KEY ) );
   }


   /**
    * {@inheritDoc}
    */
   @Before
   public void loadData() throws Exception {
      iNsvTaskUtils = new DefaultNsvTaskUtils( iMockQao, StringBundles.getInstance() );
   }


   /**
    * Returns a test data set containing the provided work package key.
    *
    * @param aWpKey
    *           work package key
    *
    * @return
    */
   private DataSet getWpKeyDataSet( TaskKey aWpKey ) {

      DataSet lDs = new DataSet( new String[] { "SCHED_DB_ID", "SCHED_ID" },
            new String[] { DataTypeUtils.INTEGER, DataTypeUtils.INTEGER } );
      lDs.addRow( new Object[] { aWpKey.getDbId(), aWpKey.getId() } );

      return lDs;
   }
}
