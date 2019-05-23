package com.mxi.mx.db.view.baselinetask;

import static com.mxi.mx.testing.matchers.MxMatchers.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assume.assumeThat;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.InventoryKey;


/**
 * Tests the task approval process
 */
@RunWith( Theories.class )
public final class TaskApprovalTest extends BaselineTestCase {

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @DataPoint
   public static final TaskDefinition COMPONENT_TASK_DEFN =
         new TaskDefinition( COMPONENT_CONFIG_SLOT );
   @DataPoint
   public static final TaskDefinition SUB_COMPONENT_TASK_DEFN =
         new TaskDefinition( SUB_COMPONENT_CONFIG_SLOT );
   @DataPoint
   public static final TaskDefinition SUB_COMPONENT_PART_BASED_TASK =
         new TaskDefinition( SUB_COMPONENT_PART_NO );

   private MaintenanceProgram iMaintPrgm;


   @Before
   public void setUp() {
      iMaintPrgm = new MaintenanceProgram();
   }


   /**
    * {@inheritDoc}
    *
    * @throws Exception
    */
   @After
   public void tearDown() throws Exception {
      SUB_COMPONENT_TASK_DEFN.rollback();
      COMPONENT_TASK_DEFN.rollback();
      SUB_COMPONENT_PART_BASED_TASK.rollback();
   }


   /**
    * When a task is assigned to a maintenance program, it will no longer be approved via the
    * fleet-approval process. This case covers the scenario where the task was added to a
    * maintenance program while still in-revision. The in-revision version to be initialized on the
    * fleet until the maintenance program is approved.
    *
    * @param aInventory
    *           the inventory
    * @param aTaskDefn
    *           the task definition
    *
    * @throws Exception
    */
   @Theory
   public void testLastApproved_TaskInRevisionMP( InventoryKey aInventory,
         TaskDefinition aTaskDefn ) throws Exception {
      assumeThat( aTaskDefn, canUseMaintenancePrograms() );
      assumeThat( aTaskDefn, appliesTo( aInventory ) );

      aTaskDefn.revise();
      aTaskDefn.activate();
      aTaskDefn.revise();
      iMaintPrgm.create();
      iMaintPrgm.assign( aTaskDefn );
      aTaskDefn.activate();

      assertThat( taskRevisionFor( aInventory, aTaskDefn ), is( 1 ) );
   }


   /**
    * When a task is activated and it doesn't belong to maintenance program, the latest activated
    * should be used
    *
    * @param aInventory
    *           the inventory
    * @param aTaskDefn
    *           the task definition
    *
    * @throws Exception
    */
   @Theory
   @Test
   public void testLatestTask_ActivatedTask( InventoryKey aInventory, TaskDefinition aTaskDefn )
         throws Exception {
      assumeThat( aTaskDefn, appliesTo( aInventory ) );

      aTaskDefn.revise();
      aTaskDefn.activate();

      assertThat( taskRevisionFor( aInventory, aTaskDefn ), is( 1 ) );
   }


   /**
    * When the last approved task is superseded, ensure the latest activated task gets approved for
    * the fleet.
    *
    * @param aInventory
    *           the inventory
    * @param aTaskDefn
    *           the task definition
    *
    * @throws Exception
    */
   @Theory
   @Test
   public void testLatestTask_ActivatedTask_MultipleActivations( InventoryKey aInventory,
         TaskDefinition aTaskDefn ) throws Exception {
      assumeThat( aTaskDefn, appliesTo( aInventory ) );

      aTaskDefn.revise();
      aTaskDefn.activate();
      aTaskDefn.revise();
      aTaskDefn.activate();

      assertThat( taskRevisionFor( aInventory, aTaskDefn ), is( 2 ) );
   }


   /**
    * When the maintenance program is activated, all unassigned tasks should go to the version that
    * should have been approved if not using the maintenance programs (i.e.: The latest activated
    * task revision)
    *
    * @param aInventory
    *           the inventory
    * @param aTaskDefn
    *           the task definition
    *
    * @throws Exception
    */
   @Theory
   public void testLatestTask_MPActivateUnassign( InventoryKey aInventory,
         TaskDefinition aTaskDefn ) throws Exception {
      assumeThat( aTaskDefn, canUseMaintenancePrograms() );
      assumeThat( aTaskDefn, appliesTo( aInventory ) );

      aTaskDefn.revise();
      iMaintPrgm.create();
      iMaintPrgm.assign( aTaskDefn );
      aTaskDefn.activate();
      iMaintPrgm.activate();
      aTaskDefn.revise();
      aTaskDefn.activate();
      iMaintPrgm.unassign( aTaskDefn );
      iMaintPrgm.activate();

      assertThat( taskRevisionFor( aInventory, aTaskDefn ), is( 2 ) );
   }


   /**
    * When a task is assigned to a maintenance program and then unassigned, any task activated
    * before it was unassigned should be fleet-approved.
    *
    * @param aInventory
    *           the inventory
    * @param aTaskDefn
    *           the task definition
    *
    * @throws Exception
    */
   @Theory
   @Test
   public void testLatestTask_MPAssignUnassign( InventoryKey aInventory, TaskDefinition aTaskDefn )
         throws Exception {
      assumeThat( aTaskDefn, canUseMaintenancePrograms() );
      assumeThat( aTaskDefn, appliesTo( aInventory ) );

      aTaskDefn.revise();
      iMaintPrgm.create();
      iMaintPrgm.assign( aTaskDefn );
      aTaskDefn.activate();
      iMaintPrgm.unassign( aTaskDefn );

      assertThat( taskRevisionFor( aInventory, aTaskDefn ), is( 1 ) );
   }


   /**
    * When a task is assigned, it can be temporarily issued without being activated in any
    * maintenance programs. If the temporarily issued task is by activating the maintenance program
    * for the operator, the latest task revision should be fleet-approved.
    *
    * @param aInventory
    *           the inventory
    * @param aTaskDefn
    *           the task definition
    *
    * @throws Exception
    */
   @Theory
   public void testLatestTask_MPTempIssueUnassign( InventoryKey aInventory,
         TaskDefinition aTaskDefn ) throws Exception {
      assumeThat( aTaskDefn, canUseMaintenancePrograms() );
      assumeThat( aTaskDefn, appliesTo( aInventory ) );

      aTaskDefn.revise();
      iMaintPrgm.create();
      iMaintPrgm.activate();
      iMaintPrgm.assign( aTaskDefn );
      aTaskDefn.activate();
      aTaskDefn.issueTaskTemporarily();
      aTaskDefn.revise();
      aTaskDefn.activate();
      iMaintPrgm.unassign( aTaskDefn );
      iMaintPrgm.activate();

      assertThat( taskRevisionFor( aInventory, aTaskDefn ), is( 2 ) );
   }


   /**
    * When a task is in an activated maintenance program, the task becomes operator-approved. All
    * aircraft belonging to the operator should initialize the operator-approved version. Loose
    * components use the highest operator-approved revision.
    *
    * @param aInventory
    *           the inventory
    * @param aTaskDefn
    *           the task definition
    *
    * @throws Exception
    */
   @SuppressWarnings( "unchecked" )
   @Theory
   @Test
   public void testMPTask_MPActivate( InventoryKey aInventory, TaskDefinition aTaskDefn )
         throws Exception {
      assumeThat( aTaskDefn, canUseMaintenancePrograms() );
      assumeThat( aTaskDefn, appliesTo( aInventory ) );
      assumeThat( aInventory, anyOf( belongsToOperator(), loose() ) );

      aTaskDefn.revise();
      iMaintPrgm.create();
      iMaintPrgm.assign( aTaskDefn );
      aTaskDefn.activate();
      iMaintPrgm.activate();

      assertThat( taskRevisionFor( aInventory, aTaskDefn ), is( 1 ) );
   }


   /**
    * When a task is temporarily issued for an operator, it becomes operator-approved. all aircraft
    * belonging to the operator should initialize the operator-approved version. Loose components
    * use the highest operator-approved revision.
    *
    * @param aInventory
    *           the inventory
    * @param aTaskDefn
    *           the task definition
    *
    * @throws Exception
    */
   @SuppressWarnings( "unchecked" )
   @Theory
   @Test
   public void testMPTask_MPTempIssue( InventoryKey aInventory, TaskDefinition aTaskDefn )
         throws Exception {
      assumeThat( aTaskDefn, canUseMaintenancePrograms() );
      assumeThat( aTaskDefn, appliesTo( aInventory ) );
      assumeThat( aInventory, anyOf( belongsToOperator(), loose() ) );

      iMaintPrgm.create();
      iMaintPrgm.activate();

      aTaskDefn.revise();
      iMaintPrgm.assign( aTaskDefn );
      aTaskDefn.activate();
      aTaskDefn.issueTaskTemporarily();

      assertThat( taskRevisionFor( aInventory, aTaskDefn ), is( 1 ) );
   }


   /**
    * If the task is in build, there's nothing to do since there's no approved version
    *
    * @param aInventory
    *           the inventory
    * @param aTaskDefn
    *           the task definition
    *
    * @throws Exception
    */
   @Theory
   @Test
   public void testNoEntry_BuildTask( InventoryKey aInventory, TaskDefinition aTaskDefn )
         throws Exception {
      assumeThat( aTaskDefn, appliesTo( aInventory ) );

      aTaskDefn.revise();

      assertNoEntry( aInventory, aTaskDefn );
   }


   /**
    * If the task is in build (via locked-in version), there's nothing to do since there's no
    * approved version
    *
    * @param aInventory
    *           the inventory
    * @param aTaskDefn
    *           the task definition
    *
    * @throws Exception
    */
   @Theory
   @Test
   public void testNoEntry_TaskFirstActivatedInMP_Build( InventoryKey aInventory,
         TaskDefinition aTaskDefn ) throws Exception {
      assumeThat( aTaskDefn, canUseMaintenancePrograms() );
      assumeThat( aTaskDefn, appliesTo( aInventory ) );

      aTaskDefn.revise();
      iMaintPrgm.create();
      iMaintPrgm.assign( aTaskDefn );
      aTaskDefn.activate();

      assertNoEntry( aInventory, aTaskDefn );
   }


   /**
    * If the task is in build (via locked-in version), there's nothing to do since there's no
    * approved version
    *
    * @param aInventory
    *           the inventory
    * @param aTaskDefn
    *           the task definition
    *
    * @throws Exception
    */
   @Theory
   @Test
   public void testNoEntry_TaskFirstActivatedInMP_Revision( InventoryKey aInventory,
         TaskDefinition aTaskDefn ) throws Exception {
      assumeThat( aTaskDefn, canUseMaintenancePrograms() );
      assumeThat( aTaskDefn, appliesTo( aInventory ) );

      iMaintPrgm.create();
      iMaintPrgm.activate();

      aTaskDefn.revise();
      iMaintPrgm.assign( aTaskDefn );
      aTaskDefn.activate();

      assertNoEntry( aInventory, aTaskDefn );
   }


   /**
    * Ensure that tasks that are not applicable to the inventory do not generate a row.
    *
    * @param aInventory
    *           the inventory
    * @param aTaskDefn
    *           the task definition
    *
    * @throws Exception
    */
   @Theory
   @Test
   public void testNoEntry_UnrelatedInventory( InventoryKey aInventory, TaskDefinition aTaskDefn )
         throws Exception {
      assumeThat( aTaskDefn, not( appliesTo( aInventory ) ) );

      aTaskDefn.revise();
      aTaskDefn.activate();

      assertNoEntry( aInventory, aTaskDefn );
   }


   /**
    * When a task is assigned to an activated maintenance program, the task becomes
    * operator-approved. All operators that have not been approved for the task do not need to do
    * it.
    *
    * @param aInventory
    *           the inventory
    * @param aTaskDefn
    *           the task definition
    *
    * @throws Exception
    */
   @SuppressWarnings( "unchecked" )
   @Theory
   @Test
   public void testNull_MPOtherOperator( InventoryKey aInventory, TaskDefinition aTaskDefn )
         throws Exception {
      assumeThat( aTaskDefn, canUseMaintenancePrograms() );
      assumeThat( aTaskDefn, appliesTo( aInventory ) );
      assumeThat( aInventory, not( anyOf( belongsToOperator(), loose() ) ) );

      aTaskDefn.revise();
      iMaintPrgm.create();
      iMaintPrgm.assign( aTaskDefn );
      aTaskDefn.activate();
      iMaintPrgm.activate();

      assertThat( taskRevisionFor( aInventory, aTaskDefn ), is( nullValue() ) );
   }


   /**
    * When a task is part of a maintenance program for an operator, if we obsolete the task and
    * issue temporary revision for the operator then the task is no longer required for that
    * operator. Loose components use the highest operator-approved revision.
    *
    * @param aInventory
    *           the inventory
    * @param aTaskDefn
    *           the task definition
    *
    * @throws Exception
    *            if an error occurs
    */
   @SuppressWarnings( "unchecked" )
   @Theory
   @Test
   public void testNull_MPTempIssueObsoleteTask( InventoryKey aInventory, TaskDefinition aTaskDefn )
         throws Exception {
      assumeThat( aTaskDefn, canUseMaintenancePrograms() );
      assumeThat( aTaskDefn, appliesTo( aInventory ) );
      assumeThat( aInventory, anyOf( belongsToOperator(), loose() ) );

      aTaskDefn.revise();
      iMaintPrgm.create();
      iMaintPrgm.assign( aTaskDefn );
      aTaskDefn.activate();
      iMaintPrgm.activate();
      aTaskDefn.revise();
      aTaskDefn.obsolete();
      aTaskDefn.issueTaskTemporarily();

      assertThat( taskRevisionFor( aInventory, aTaskDefn ), is( nullValue() ) );
   }


   /**
    * When a task temporarily issued for an operator, the task becomes operator-approved. All
    * operators that have not been approved for the task do not need to do it.
    *
    * @param aInventory
    *           the inventory
    * @param aTaskDefn
    *           the task definition
    *
    * @throws Exception
    */
   @SuppressWarnings( "unchecked" )
   @Theory
   public void testNull_MPTempIssueOtherOperator( InventoryKey aInventory,
         TaskDefinition aTaskDefn ) throws Exception {
      assumeThat( aTaskDefn, canUseMaintenancePrograms() );
      assumeThat( aTaskDefn, appliesTo( aInventory ) );
      assumeThat( aInventory, not( anyOf( belongsToOperator(), loose() ) ) );

      iMaintPrgm.create();
      iMaintPrgm.activate();

      aTaskDefn.revise();
      iMaintPrgm.assign( aTaskDefn );
      aTaskDefn.activate();
      aTaskDefn.issueTaskTemporarily();

      assertThat( taskRevisionFor( aInventory, aTaskDefn ), is( nullValue() ) );
   }


   /**
    * When a task becomes obsolete, it is no longer required.
    *
    * @param aInventory
    *           the inventory
    * @param aTaskDefn
    *           the task definition
    *
    * @throws Exception
    */
   @Theory
   @Test
   public void testNull_ObsoleteTask( InventoryKey aInventory, TaskDefinition aTaskDefn )
         throws Exception {
      assumeThat( aTaskDefn, appliesTo( aInventory ) );

      aTaskDefn.revise();
      aTaskDefn.activate();
      aTaskDefn.revise();
      aTaskDefn.obsolete();

      assertThat( taskRevisionFor( aInventory, aTaskDefn ), is( nullValue() ) );
   }


   /**
    * This matcher determines if the task is applicable for maintenance programs (i.e.: Defined
    * against a config slot)
    *
    * @return the matcher
    */
   @SuppressWarnings( "unchecked" )
   protected Matcher<TaskDefinition> canUseMaintenancePrograms() {
      return anyOf( equalTo( COMPONENT_TASK_DEFN ), equalTo( SUB_COMPONENT_TASK_DEFN ) );
   }


   /**
    * This matcher determines if the inventory is a loose component
    *
    * @return the matcher
    */
   protected Matcher<InventoryKey> loose() {
      return isIn( new InventoryKey[] { INV_LOOSE_COMPONENT, INV_LOOSE_SUB_COMPONENT } );
   }
}
