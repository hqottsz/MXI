
package com.mxi.mx.web.query.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.builder.EventRelationshipBuilder;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.SdFaultBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.AuthorityKey;
import com.mxi.mx.core.key.DepartmentKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefDeptTypeKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefRelationTypeKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.org.OrgWorkDept;


/**
 * This class tests the com.mxi.mx.web.query.task.EngTasksForUser.qrx query.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class EngTasksForUserTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final String TASK_BARCODE = "TASK_BARCODE";
   private static final String WORK_ORDER_NUMBER = "WORK_ORDER_NUMBER";
   private static final String TASK_DESCRIPTION = "TASK_DESCRIPTION";
   private static final String WP_LOCATION_CODE = "WP_LOCATION_CODE";
   private static final String INV_LOCATION_CODE = "INV_LOCATION_CODE";
   private static final String INV_DESCRIPTION = "INV_DESCRIPTION";
   private static final String INV_DESCRIPTION_A = "INV_DESCRIPTION_A";
   private static final String LOCATION_CODE_A = "LOCATION_CODE_A";
   private static final String LOCATION_CODE_B = "LOCATION_CODE_B";
   private static final String INV_DESCRIPTION_B = "INV_DESCRIPTION_B";

   private static final AuthorityKey AUTHORITY_A = new AuthorityKey( 1, 1 );
   private static final AuthorityKey AUTHORITY_B = new AuthorityKey( 2, 2 );


   /**
    * Verify that tasks with a completed ENG labour row are not returned from the query.
    */
   @Test
   public void testWhenEngLabourIsCompleted() {
      LocationKey lLocation = new LocationDomainBuilder().withCode( WP_LOCATION_CODE ).build();

      InventoryKey lAircraft = new InventoryBuilder().build();

      InventoryKey lTaskInventory = new InventoryBuilder().withHighestInventory( lAircraft )
            .withDescription( INV_DESCRIPTION ).build();

      TaskKey lWorkPackage = new TaskBuilder().withWorkOrderNumber( WORK_ORDER_NUMBER )
            .onInventory( lAircraft ).atLocation( lLocation ).build();

      FaultKey lFault = new SdFaultBuilder().build();

      // Task with completed ENG labour row.
      TaskKey lCorrectiveTask = new TaskBuilder().withBarcode( TASK_BARCODE )
            .withDescription( TASK_DESCRIPTION ).withCompletedLabour( RefLabourSkillKey.ENG, 1.0 )
            .onInventory( lTaskInventory ).withParentTask( lWorkPackage ).build();

      new EventRelationshipBuilder().fromEvent( lFault ).toEvent( lCorrectiveTask )
            .withType( RefRelationTypeKey.CORRECT ).build();

      HumanResourceKey lHr = setupUserInEngDept( lLocation );

      DataSet lDs = execute( lHr );

      assertTrue( "Query unexpectedly returned rows.", lDs.isEmpty() );
   }


   /**
    * Verify that all tasks assigned to the user are returned by the query.
    */
   @Test
   public void testWhenTaskAssignedToUser() {
      LocationKey lLocationA = new LocationDomainBuilder().build();
      LocationKey lLocationB = new LocationDomainBuilder().build();
      LocationKey lLocationC = new LocationDomainBuilder().build();

      InventoryKey lInventoryA = new InventoryBuilder().withDescription( INV_DESCRIPTION )
            .atLocation( lLocationA ).build();

      InventoryKey lInventoryB = new InventoryBuilder().withDescription( INV_DESCRIPTION )
            .atLocation( lLocationB ).build();

      InventoryKey lInventoryC = new InventoryBuilder().withDescription( INV_DESCRIPTION )
            .atLocation( lLocationC ).build();

      TaskKey lTaskA = new TaskBuilder().withBarcode( TASK_BARCODE )
            .withDescription( TASK_DESCRIPTION ).withLabour( RefLabourSkillKey.ENG, 1.0 )
            .onInventory( lInventoryA ).atLocation( lLocationA ).build();

      TaskKey lTaskB = new TaskBuilder().withBarcode( TASK_BARCODE )
            .withDescription( TASK_DESCRIPTION ).withLabour( RefLabourSkillKey.ENG, 1.0 )
            .onInventory( lInventoryB ).atLocation( lLocationB ).build();

      new TaskBuilder().withBarcode( TASK_BARCODE ).withDescription( TASK_DESCRIPTION )
            .withLabour( RefLabourSkillKey.ENG, 1.0 ).onInventory( lInventoryC )
            .atLocation( lLocationC ).build();

      // Create a user.
      HumanResourceKey lHr = new HumanResourceDomainBuilder().build();

      // Assign the user to the first two tasks.
      assignUserToTask( lHr, lTaskA );
      assignUserToTask( lHr, lTaskB );

      DataSet lDs = execute( lHr );

      assertEquals( 2, lDs.getRowCount() );

      while ( lDs.next() ) {

         if ( lTaskA.toString().equals( lDs.getString( "task_key" ) ) ) {
            assertEquals( TASK_DESCRIPTION, lDs.getString( "event_sdesc" ) );
            assertEquals( 0, lDs.getInt( "is_fault" ) );
            assertEquals( 0, lDs.getInt( "is_fault_evaluated" ) );
            assertEquals( TASK_BARCODE, lDs.getString( "barcode_sdesc" ) );
            assertEquals( lInventoryA.toString(), lDs.getString( "inv_key" ) );
            assertEquals( INV_DESCRIPTION, lDs.getString( "inv_no_sdesc" ) );
            assertEquals( lLocationA.toString(), lDs.getString( "loc_key" ) );
         } else if ( lTaskB.toString().equals( lDs.getString( "task_key" ) ) ) {
            assertEquals( TASK_DESCRIPTION, lDs.getString( "event_sdesc" ) );
            assertEquals( 0, lDs.getInt( "is_fault" ) );
            assertEquals( 0, lDs.getInt( "is_fault_evaluated" ) );
            assertEquals( TASK_BARCODE, lDs.getString( "barcode_sdesc" ) );
            assertEquals( lInventoryB.toString(), lDs.getString( "inv_key" ) );
            assertEquals( INV_DESCRIPTION, lDs.getString( "inv_no_sdesc" ) );
            assertEquals( lLocationB.toString(), lDs.getString( "loc_key" ) );
         } else {
            fail( "Query returned an unexpected task = " + lDs.getString( "task_key" ) );
         }
      }
   }


   /**
    * Verify that when a task has one completed ENG labour row and another not completed ENG labour
    * row, that the query returns the task.
    */
   @Test
   public void testWhenTaskHasCompletedEngLabourAndNotCompletedEngLabour() {
      LocationKey lLocation = new LocationDomainBuilder().withCode( WP_LOCATION_CODE ).build();

      InventoryKey lAircraft = new InventoryBuilder().build();

      InventoryKey lTaskInventory = new InventoryBuilder().withHighestInventory( lAircraft )
            .atLocation( lLocation ).withDescription( INV_DESCRIPTION ).build();

      TaskKey lWorkPackage = new TaskBuilder().withWorkOrderNumber( WORK_ORDER_NUMBER )
            .onInventory( lAircraft ).atLocation( lLocation ).build();

      FaultKey lFault = new SdFaultBuilder().build();

      // Task with one completed ENG labour row and one non completed ENG labour row.
      TaskKey lCorrectiveTask = new TaskBuilder().withBarcode( TASK_BARCODE )
            .withDescription( TASK_DESCRIPTION ).withCompletedLabour( RefLabourSkillKey.ENG, 1.0 )
            .withLabour( RefLabourSkillKey.ENG, 1.0 ).onInventory( lTaskInventory )
            .withParentTask( lWorkPackage ).build();

      new EventRelationshipBuilder().fromEvent( lFault ).toEvent( lCorrectiveTask )
            .withType( RefRelationTypeKey.CORRECT ).build();

      HumanResourceKey lHr = setupUserInEngDept( lLocation );

      DataSet lDs = execute( lHr );

      assertEquals( 1, lDs.getRowCount() );
      assertTrue( lDs.next() );

      assertEquals( lCorrectiveTask.toString(), lDs.getString( "task_key" ) );
      assertEquals( TASK_DESCRIPTION, lDs.getString( "event_sdesc" ) );
      assertEquals( lFault.toString(), lDs.getString( "fault_key" ) );
      assertEquals( 1, lDs.getInt( "is_fault" ) );
      assertEquals( 0, lDs.getInt( "is_fault_evaluated" ) );
      assertEquals( TASK_BARCODE, lDs.getString( "barcode_sdesc" ) );
      assertEquals( lTaskInventory.toString(), lDs.getString( "inv_key" ) );
      assertEquals( INV_DESCRIPTION, lDs.getString( "inv_no_sdesc" ) );
      assertEquals( WORK_ORDER_NUMBER, lDs.getString( "wo_ref_sdesc" ) );
      assertEquals( lLocation.toString(), lDs.getString( "loc_key" ) );
      assertEquals( WP_LOCATION_CODE, lDs.getString( "loc_cd" ) );
   }


   /**
    * Verify that tasks that do not have an ENG labour row are not returned from the query.
    */
   @Test
   public void testWhenTaskHasNoEngLabourRow() {
      LocationKey lLocation = new LocationDomainBuilder().withCode( WP_LOCATION_CODE ).build();

      InventoryKey lAircraft = new InventoryBuilder().build();

      InventoryKey lTaskInventory = new InventoryBuilder().withHighestInventory( lAircraft )
            .withDescription( INV_DESCRIPTION ).build();

      TaskKey lWorkPackage = new TaskBuilder().withWorkOrderNumber( WORK_ORDER_NUMBER )
            .onInventory( lAircraft ).atLocation( lLocation ).build();

      FaultKey lFault = new SdFaultBuilder().build();

      // Task with labour row that is not ENG.
      TaskKey lCorrectiveTask = new TaskBuilder().withBarcode( TASK_BARCODE )
            .withDescription( TASK_DESCRIPTION ).withLabour( RefLabourSkillKey.LBR, 1.0 )
            .onInventory( lTaskInventory ).withParentTask( lWorkPackage ).build();

      new EventRelationshipBuilder().fromEvent( lFault ).toEvent( lCorrectiveTask )
            .withType( RefRelationTypeKey.CORRECT ).build();

      HumanResourceKey lHr = setupUserInEngDept( lLocation );

      DataSet lDs = execute( lHr );

      assertTrue( "Query unexpectedly returned rows.", lDs.isEmpty() );
   }


   /**
    * Verify that when the task is a corrective task of a fault and that fault has been marked as
    * evaluated, that query result indicates the fault is evaluated.
    */
   @Test
   public void testWhenTaskIsCorrectiveTaskOfAnEvaluatedFault() {
      LocationKey lLocation = new LocationDomainBuilder().build();
      InventoryKey lAircraft = new InventoryBuilder().build();

      InventoryKey lTaskInventory = new InventoryBuilder().withHighestInventory( lAircraft )
            .atLocation( lLocation ).withDescription( INV_DESCRIPTION ).build();

      FaultKey lFault = new SdFaultBuilder().isEvaluated().build();

      TaskKey lCorrectiveTask = new TaskBuilder().withBarcode( TASK_BARCODE )
            .withDescription( TASK_DESCRIPTION ).withLabour( RefLabourSkillKey.ENG, 1.0 )
            .onInventory( lTaskInventory ).atLocation( lLocation ).build();

      new EventRelationshipBuilder().fromEvent( lFault ).toEvent( lCorrectiveTask )
            .withType( RefRelationTypeKey.CORRECT ).build();

      HumanResourceKey lHr = setupUserInEngDept( lLocation );

      DataSet lDs = execute( lHr );

      assertEquals( 1, lDs.getRowCount() );
      assertTrue( lDs.next() );

      assertEquals( lCorrectiveTask.toString(), lDs.getString( "task_key" ) );
      assertEquals( TASK_DESCRIPTION, lDs.getString( "event_sdesc" ) );
      assertEquals( lFault.toString(), lDs.getString( "fault_key" ) );
      assertEquals( 1, lDs.getInt( "is_fault" ) );
      assertEquals( 1, lDs.getInt( "is_fault_evaluated" ) );
      assertEquals( TASK_BARCODE, lDs.getString( "barcode_sdesc" ) );
      assertEquals( lTaskInventory.toString(), lDs.getString( "inv_key" ) );
      assertEquals( INV_DESCRIPTION, lDs.getString( "inv_no_sdesc" ) );
      assertEquals( lLocation.toString(), lDs.getString( "loc_key" ) );
   }


   /**
    * Verify that when the task is a corrective task of a fault, that the fault information is
    * returned.
    */
   @Test
   public void testWhenTaskIsCorrectiveTaskOfFault() {
      LocationKey lLocation = new LocationDomainBuilder().build();
      InventoryKey lAircraft = new InventoryBuilder().build();

      InventoryKey lTaskInventory = new InventoryBuilder().withHighestInventory( lAircraft )
            .atLocation( lLocation ).withDescription( INV_DESCRIPTION ).build();

      FaultKey lFault = new SdFaultBuilder().build();

      TaskKey lCorrectiveTask = new TaskBuilder().withBarcode( TASK_BARCODE )
            .withDescription( TASK_DESCRIPTION ).withLabour( RefLabourSkillKey.ENG, 1.0 )
            .onInventory( lTaskInventory ).atLocation( lLocation ).build();

      new EventRelationshipBuilder().fromEvent( lFault ).toEvent( lCorrectiveTask )
            .withType( RefRelationTypeKey.CORRECT ).build();

      HumanResourceKey lHr = setupUserInEngDept( lLocation );

      DataSet lDs = execute( lHr );

      assertEquals( 1, lDs.getRowCount() );
      assertTrue( lDs.next() );

      assertEquals( lCorrectiveTask.toString(), lDs.getString( "task_key" ) );
      assertEquals( TASK_DESCRIPTION, lDs.getString( "event_sdesc" ) );
      assertEquals( lFault.toString(), lDs.getString( "fault_key" ) );
      assertEquals( 1, lDs.getInt( "is_fault" ) );
      assertEquals( 0, lDs.getInt( "is_fault_evaluated" ) );
      assertEquals( TASK_BARCODE, lDs.getString( "barcode_sdesc" ) );
      assertEquals( lTaskInventory.toString(), lDs.getString( "inv_key" ) );
      assertEquals( INV_DESCRIPTION, lDs.getString( "inv_no_sdesc" ) );
      assertEquals( lLocation.toString(), lDs.getString( "loc_key" ) );
   }


   /**
    * Verify that historic tasks (e.g. cancelled tasks) are not returned from the query.
    */
   @Test
   public void testWhenTaskIsHistoric() {
      LocationKey lLocation = new LocationDomainBuilder().build();

      InventoryKey lAircraft = new InventoryBuilder().build();

      InventoryKey lTaskInventory = new InventoryBuilder().withHighestInventory( lAircraft )
            .withDescription( INV_DESCRIPTION ).build();

      // Historic task with active ENG labour row.
      new TaskBuilder().withBarcode( TASK_BARCODE ).withDescription( TASK_DESCRIPTION )
            .withLabour( RefLabourSkillKey.ENG, 1.0 ).onInventory( lTaskInventory )
            .atLocation( lLocation ).asHistoric().build();

      HumanResourceKey lHr = setupUserInEngDept( lLocation );

      DataSet lDs = execute( lHr );

      assertTrue( "Query unexpectedly returned rows.", lDs.isEmpty() );
   }


   /**
    * Verify that when the task is part of a work package, that the work package information is
    * returned.
    */
   @Test
   public void testWhenTaskIsInWorkPackage() {
      LocationKey lLocation = new LocationDomainBuilder().withCode( WP_LOCATION_CODE ).build();

      InventoryKey lAircraft = new InventoryBuilder().build();

      InventoryKey lTaskInventory = new InventoryBuilder().withHighestInventory( lAircraft )
            .atLocation( lLocation ).withDescription( INV_DESCRIPTION ).build();

      TaskKey lWorkPackage = new TaskBuilder().withWorkOrderNumber( WORK_ORDER_NUMBER )
            .onInventory( lAircraft ).atLocation( lLocation ).build();

      TaskKey lCorrectiveTask = new TaskBuilder().withBarcode( TASK_BARCODE )
            .withDescription( TASK_DESCRIPTION ).withLabour( RefLabourSkillKey.ENG, 1.0 )
            .onInventory( lTaskInventory ).withParentTask( lWorkPackage ).build();

      HumanResourceKey lHr = setupUserInEngDept( lLocation );

      DataSet lDs = execute( lHr );

      assertEquals( 1, lDs.getRowCount() );
      assertTrue( lDs.next() );

      assertEquals( lCorrectiveTask.toString(), lDs.getString( "task_key" ) );
      assertEquals( TASK_DESCRIPTION, lDs.getString( "event_sdesc" ) );
      assertEquals( 0, lDs.getInt( "is_fault" ) );
      assertEquals( 0, lDs.getInt( "is_fault_evaluated" ) );
      assertEquals( TASK_BARCODE, lDs.getString( "barcode_sdesc" ) );
      assertEquals( lTaskInventory.toString(), lDs.getString( "inv_key" ) );
      assertEquals( INV_DESCRIPTION, lDs.getString( "inv_no_sdesc" ) );
      assertEquals( WORK_ORDER_NUMBER, lDs.getString( "wo_ref_sdesc" ) );
      assertEquals( lLocation.toString(), lDs.getString( "loc_key" ) );
      assertEquals( WP_LOCATION_CODE, lDs.getString( "loc_cd" ) );
   }


   /**
    * Verify that when the task is a corrective task of a fault and the task is part of a work
    * package, that the fault and work package information is returned.
    */
   @Test
   public void testWhenTaskIsInWorkPackageAndIsCorrectiveTaskOfFault() {
      LocationKey lLocation = new LocationDomainBuilder().withCode( WP_LOCATION_CODE ).build();

      InventoryKey lAircraft = new InventoryBuilder().build();

      InventoryKey lTaskInventory = new InventoryBuilder().withHighestInventory( lAircraft )
            .atLocation( lLocation ).withDescription( INV_DESCRIPTION ).build();

      TaskKey lWorkPackage = new TaskBuilder().withWorkOrderNumber( WORK_ORDER_NUMBER )
            .onInventory( lAircraft ).atLocation( lLocation ).build();

      FaultKey lFault = new SdFaultBuilder().build();

      TaskKey lCorrectiveTask = new TaskBuilder().withBarcode( TASK_BARCODE )
            .withDescription( TASK_DESCRIPTION ).withLabour( RefLabourSkillKey.ENG, 1.0 )
            .onInventory( lTaskInventory ).withParentTask( lWorkPackage ).build();

      new EventRelationshipBuilder().fromEvent( lFault ).toEvent( lCorrectiveTask )
            .withType( RefRelationTypeKey.CORRECT ).build();

      HumanResourceKey lHr = setupUserInEngDept( lLocation );

      DataSet lDs = execute( lHr );

      assertEquals( 1, lDs.getRowCount() );
      assertTrue( lDs.next() );

      assertEquals( lCorrectiveTask.toString(), lDs.getString( "task_key" ) );
      assertEquals( TASK_DESCRIPTION, lDs.getString( "event_sdesc" ) );
      assertEquals( lFault.toString(), lDs.getString( "fault_key" ) );
      assertEquals( 1, lDs.getInt( "is_fault" ) );
      assertEquals( 0, lDs.getInt( "is_fault_evaluated" ) );
      assertEquals( TASK_BARCODE, lDs.getString( "barcode_sdesc" ) );
      assertEquals( lTaskInventory.toString(), lDs.getString( "inv_key" ) );
      assertEquals( INV_DESCRIPTION, lDs.getString( "inv_no_sdesc" ) );
      assertEquals( WORK_ORDER_NUMBER, lDs.getString( "wo_ref_sdesc" ) );
      assertEquals( lLocation.toString(), lDs.getString( "loc_key" ) );
      assertEquals( WP_LOCATION_CODE, lDs.getString( "loc_cd" ) );
   }


   /**
    * Verify that when the task is part of a work package that is not scheduled to a location (thus
    * the task has no location) but the task's inventory is at the location of the user's
    * department, that the task is returned.
    */
   @Test
   public void testWhenTaskIsInWorkPackageWithNoLocationButInventoryIsAtDeptLocation() {
      LocationKey lLocation = new LocationDomainBuilder().withCode( INV_LOCATION_CODE ).build();

      InventoryKey lAircraft = new InventoryBuilder().build();

      // Task inventory at a location.
      InventoryKey lTaskInventory = new InventoryBuilder().withHighestInventory( lAircraft )
            .withDescription( INV_DESCRIPTION ).atLocation( lLocation ).build();

      // Work package with no location.
      TaskKey lWorkPackage = new TaskBuilder().withWorkOrderNumber( WORK_ORDER_NUMBER )
            .onInventory( lAircraft ).build();

      // Task also has no location.
      TaskKey lCorrectiveTask = new TaskBuilder().withBarcode( TASK_BARCODE )
            .withDescription( TASK_DESCRIPTION ).withLabour( RefLabourSkillKey.ENG, 1.0 )
            .onInventory( lTaskInventory ).withParentTask( lWorkPackage ).build();

      HumanResourceKey lHr = setupUserInEngDept( lLocation );

      DataSet lDs = execute( lHr );

      assertEquals( 1, lDs.getRowCount() );
      assertTrue( lDs.next() );

      assertEquals( lCorrectiveTask.toString(), lDs.getString( "task_key" ) );
      assertEquals( TASK_DESCRIPTION, lDs.getString( "event_sdesc" ) );
      assertEquals( 0, lDs.getInt( "is_fault" ) );
      assertEquals( 0, lDs.getInt( "is_fault_evaluated" ) );
      assertEquals( TASK_BARCODE, lDs.getString( "barcode_sdesc" ) );
      assertEquals( lTaskInventory.toString(), lDs.getString( "inv_key" ) );
      assertEquals( INV_DESCRIPTION, lDs.getString( "inv_no_sdesc" ) );
      assertEquals( WORK_ORDER_NUMBER, lDs.getString( "wo_ref_sdesc" ) );
      assertEquals( lLocation.toString(), lDs.getString( "loc_key" ) );
      assertEquals( INV_LOCATION_CODE, lDs.getString( "loc_cd" ) );
   }


   /**
    * Verify that when the task is related to a fault but is not its corrective task, that the fault
    * information is not returned.
    */
   @Test
   public void testWhenTaskIsNotCorrectiveTaskOfFault() {
      LocationKey lLocation = new LocationDomainBuilder().build();
      InventoryKey lAircraft = new InventoryBuilder().build();

      InventoryKey lTaskInventory = new InventoryBuilder().withHighestInventory( lAircraft )
            .atLocation( lLocation ).withDescription( INV_DESCRIPTION ).build();

      FaultKey lFault = new SdFaultBuilder().build();

      TaskKey lTask = new TaskBuilder().withBarcode( TASK_BARCODE )
            .withDescription( TASK_DESCRIPTION ).withLabour( RefLabourSkillKey.ENG, 1.0 )
            .onInventory( lTaskInventory ).atLocation( lLocation ).build();

      // Ensure the relationship between the task and fault is not CORRECTIVE.
      new EventRelationshipBuilder().fromEvent( lFault ).toEvent( lTask )
            .withType( RefRelationTypeKey.DRVTASK ).build();

      HumanResourceKey lHr = setupUserInEngDept( lLocation );

      DataSet lDs = execute( lHr );

      assertEquals( 1, lDs.getRowCount() );
      assertTrue( lDs.next() );

      assertEquals( lTask.toString(), lDs.getString( "task_key" ) );
      assertEquals( TASK_DESCRIPTION, lDs.getString( "event_sdesc" ) );
      assertEquals( 0, lDs.getInt( "is_fault" ) );
      assertEquals( 0, lDs.getInt( "is_fault_evaluated" ) );
      assertEquals( TASK_BARCODE, lDs.getString( "barcode_sdesc" ) );
      assertEquals( lTaskInventory.toString(), lDs.getString( "inv_key" ) );
      assertEquals( INV_DESCRIPTION, lDs.getString( "inv_no_sdesc" ) );
      assertEquals( lLocation.toString(), lDs.getString( "loc_key" ) );
   }


   /**
    * Verify that when neither the tasks inventory nor its highest inventory have an authority set
    * and when the task location is the location of the user's department, that the task is returned
    * from the query.
    */
   @Test
   public void testWhenTaskLocationIsLocationOfUsersDeptLocation() {
      LocationKey lDeptLocation = new LocationDomainBuilder().build();

      // Inventory and task at the sub-location.
      InventoryKey lInventory = new InventoryBuilder().atLocation( lDeptLocation )
            .withDescription( INV_DESCRIPTION ).build();

      TaskKey lTask =
            new TaskBuilder().withBarcode( TASK_BARCODE ).withDescription( TASK_DESCRIPTION )
                  .withLabour( RefLabourSkillKey.ENG, 1.0 ).onInventory( lInventory ).build();

      HumanResourceKey lHr = setupUserInEngDept( lDeptLocation );

      DataSet lDs = execute( lHr );

      assertEquals( 1, lDs.getRowCount() );
      assertTrue( lDs.next() );

      assertEquals( lTask.toString(), lDs.getString( "task_key" ) );
      assertEquals( TASK_DESCRIPTION, lDs.getString( "event_sdesc" ) );
      assertEquals( 0, lDs.getInt( "is_fault" ) );
      assertEquals( 0, lDs.getInt( "is_fault_evaluated" ) );
      assertEquals( TASK_BARCODE, lDs.getString( "barcode_sdesc" ) );
      assertEquals( lInventory.toString(), lDs.getString( "inv_key" ) );
      assertEquals( INV_DESCRIPTION, lDs.getString( "inv_no_sdesc" ) );
      assertEquals( lDeptLocation.toString(), lDs.getString( "loc_key" ) );
   }


   /**
    * Verify that when neither the tasks inventory nor its highest inventory have an authority set
    * and when the task location is a sub-location of the user's department location, that the task
    * is returned from the query.
    */
   @Test
   public void testWhenTaskLocationIsSubLocationOfUsersDeptLocation() {
      LocationKey lDeptLocation = new LocationDomainBuilder().build();
      LocationKey lSubLocation = new LocationDomainBuilder().withParent( lDeptLocation ).build();

      // Inventory and task at the sub-location.
      InventoryKey lInventory = new InventoryBuilder().atLocation( lSubLocation )
            .withDescription( INV_DESCRIPTION ).build();

      TaskKey lTask =
            new TaskBuilder().withBarcode( TASK_BARCODE ).withDescription( TASK_DESCRIPTION )
                  .withLabour( RefLabourSkillKey.ENG, 1.0 ).onInventory( lInventory ).build();

      HumanResourceKey lHr = setupUserInEngDept( lDeptLocation );

      DataSet lDs = execute( lHr );

      assertEquals( 1, lDs.getRowCount() );
      assertTrue( lDs.next() );

      assertEquals( lTask.toString(), lDs.getString( "task_key" ) );
      assertEquals( TASK_DESCRIPTION, lDs.getString( "event_sdesc" ) );
      assertEquals( 0, lDs.getInt( "is_fault" ) );
      assertEquals( 0, lDs.getInt( "is_fault_evaluated" ) );
      assertEquals( TASK_BARCODE, lDs.getString( "barcode_sdesc" ) );
      assertEquals( lInventory.toString(), lDs.getString( "inv_key" ) );
      assertEquals( INV_DESCRIPTION, lDs.getString( "inv_no_sdesc" ) );
      assertEquals( lSubLocation.toString(), lDs.getString( "loc_key" ) );
   }


   /**
    * Verify that when the user belongs to an ENG department with multiple locations, that tasks at
    * those multiple locations are returned.
    */
   @Test
   public void testWhenUserBelongsToEngDeptWithManyLocations() {
      LocationKey lLocationA = new LocationDomainBuilder().build();
      LocationKey lLocationB = new LocationDomainBuilder().build();
      LocationKey lLocationC = new LocationDomainBuilder().build();

      InventoryKey lInventoryA = new InventoryBuilder().withDescription( INV_DESCRIPTION )
            .atLocation( lLocationA ).build();

      InventoryKey lInventoryB = new InventoryBuilder().withDescription( INV_DESCRIPTION )
            .atLocation( lLocationB ).build();

      InventoryKey lInventoryC = new InventoryBuilder().withDescription( INV_DESCRIPTION )
            .atLocation( lLocationC ).build();

      TaskKey lTaskA = new TaskBuilder().withBarcode( TASK_BARCODE )
            .withDescription( TASK_DESCRIPTION ).withLabour( RefLabourSkillKey.ENG, 1.0 )
            .onInventory( lInventoryA ).atLocation( lLocationA ).build();

      TaskKey lTaskB = new TaskBuilder().withBarcode( TASK_BARCODE )
            .withDescription( TASK_DESCRIPTION ).withLabour( RefLabourSkillKey.ENG, 1.0 )
            .onInventory( lInventoryB ).atLocation( lLocationB ).build();

      new TaskBuilder().withBarcode( TASK_BARCODE ).withDescription( TASK_DESCRIPTION )
            .withLabour( RefLabourSkillKey.ENG, 1.0 ).onInventory( lInventoryC )
            .atLocation( lLocationC ).build();

      // Create an ENG department.
      OrgWorkDept lOrgWorkDept = OrgWorkDept.create();
      lOrgWorkDept.setType( RefDeptTypeKey.ENG );

      DepartmentKey lDepartment = lOrgWorkDept.insert();

      // Add a two of the locations to the department.
      DataSetArgument lInsertArgs = new DataSetArgument();
      lInsertArgs.add( lDepartment, "dept_db_id", "dept_id" );
      lInsertArgs.add( lLocationA, "loc_db_id", "loc_id" );
      MxDataAccess.getInstance().executeInsert( "inv_loc_dept", lInsertArgs );

      lInsertArgs.add( lLocationB, "loc_db_id", "loc_id" );
      MxDataAccess.getInstance().executeInsert( "inv_loc_dept", lInsertArgs );

      // Create a user.
      HumanResourceKey lHr = new HumanResourceDomainBuilder().build();

      // Add the user to the ENG department.
      lInsertArgs = new DataSetArgument();
      lInsertArgs.add( lDepartment, "dept_db_id", "dept_id" );
      lInsertArgs.add( lHr, "hr_db_id", "hr_id" );
      MxDataAccess.getInstance().executeInsert( "org_dept_hr", lInsertArgs );

      DataSet lDs = execute( lHr );

      assertEquals( 2, lDs.getRowCount() );

      while ( lDs.next() ) {

         if ( lTaskA.toString().equals( lDs.getString( "task_key" ) ) ) {
            assertEquals( TASK_DESCRIPTION, lDs.getString( "event_sdesc" ) );
            assertEquals( 0, lDs.getInt( "is_fault" ) );
            assertEquals( 0, lDs.getInt( "is_fault_evaluated" ) );
            assertEquals( TASK_BARCODE, lDs.getString( "barcode_sdesc" ) );
            assertEquals( lInventoryA.toString(), lDs.getString( "inv_key" ) );
            assertEquals( INV_DESCRIPTION, lDs.getString( "inv_no_sdesc" ) );
            assertEquals( lLocationA.toString(), lDs.getString( "loc_key" ) );
         } else if ( lTaskB.toString().equals( lDs.getString( "task_key" ) ) ) {
            assertEquals( TASK_DESCRIPTION, lDs.getString( "event_sdesc" ) );
            assertEquals( 0, lDs.getInt( "is_fault" ) );
            assertEquals( 0, lDs.getInt( "is_fault_evaluated" ) );
            assertEquals( TASK_BARCODE, lDs.getString( "barcode_sdesc" ) );
            assertEquals( lInventoryB.toString(), lDs.getString( "inv_key" ) );
            assertEquals( INV_DESCRIPTION, lDs.getString( "inv_no_sdesc" ) );
            assertEquals( lLocationB.toString(), lDs.getString( "loc_key" ) );
         } else {
            fail( "Query returned an unexpected task = " + lDs.getString( "task_key" ) );
         }
      }
   }


   /**
    * Verify that when the user belongs to multiple ENG department at different locations, that
    * tasks at those locations are returned.
    */
   @Test
   public void testWhenUserBelongsToManyEngDepts() {
      LocationKey lLocationA = new LocationDomainBuilder().build();
      LocationKey lLocationB = new LocationDomainBuilder().build();
      LocationKey lLocationC = new LocationDomainBuilder().build();

      InventoryKey lInventoryA = new InventoryBuilder().withDescription( INV_DESCRIPTION )
            .atLocation( lLocationA ).build();

      InventoryKey lInventoryB = new InventoryBuilder().withDescription( INV_DESCRIPTION )
            .atLocation( lLocationB ).build();

      InventoryKey lInventoryC = new InventoryBuilder().withDescription( INV_DESCRIPTION )
            .atLocation( lLocationC ).build();

      TaskKey lTaskA = new TaskBuilder().withBarcode( TASK_BARCODE )
            .withDescription( TASK_DESCRIPTION ).withLabour( RefLabourSkillKey.ENG, 1.0 )
            .onInventory( lInventoryA ).atLocation( lLocationA ).build();

      TaskKey lTaskB = new TaskBuilder().withBarcode( TASK_BARCODE )
            .withDescription( TASK_DESCRIPTION ).withLabour( RefLabourSkillKey.ENG, 1.0 )
            .onInventory( lInventoryB ).atLocation( lLocationB ).build();

      new TaskBuilder().withBarcode( TASK_BARCODE ).withDescription( TASK_DESCRIPTION )
            .withLabour( RefLabourSkillKey.ENG, 1.0 ).onInventory( lInventoryC )
            .atLocation( lLocationC ).build();

      // Create an first ENG department.
      OrgWorkDept lOrgWorkDeptA = OrgWorkDept.create();
      lOrgWorkDeptA.setType( RefDeptTypeKey.ENG );

      DepartmentKey lDepartmentA = lOrgWorkDeptA.insert();

      DataSetArgument lInsertArgs = new DataSetArgument();
      lInsertArgs.add( lDepartmentA, "dept_db_id", "dept_id" );
      lInsertArgs.add( lLocationA, "loc_db_id", "loc_id" );
      MxDataAccess.getInstance().executeInsert( "inv_loc_dept", lInsertArgs );

      // Create an second ENG department.
      OrgWorkDept lOrgWorkDeptB = OrgWorkDept.create();
      lOrgWorkDeptB.setType( RefDeptTypeKey.ENG );

      DepartmentKey lDepartmentB = lOrgWorkDeptB.insert();

      lInsertArgs.add( lDepartmentB, "dept_db_id", "dept_id" );
      lInsertArgs.add( lLocationB, "loc_db_id", "loc_id" );
      MxDataAccess.getInstance().executeInsert( "inv_loc_dept", lInsertArgs );

      // Create a user.
      HumanResourceKey lHr = new HumanResourceDomainBuilder().build();

      // Add the user to each of the ENG departments.
      lInsertArgs = new DataSetArgument();
      lInsertArgs.add( lDepartmentA, "dept_db_id", "dept_id" );
      lInsertArgs.add( lHr, "hr_db_id", "hr_id" );
      MxDataAccess.getInstance().executeInsert( "org_dept_hr", lInsertArgs );

      lInsertArgs.add( lDepartmentB, "dept_db_id", "dept_id" );
      MxDataAccess.getInstance().executeInsert( "org_dept_hr", lInsertArgs );

      DataSet lDs = execute( lHr );

      assertEquals( 2, lDs.getRowCount() );

      while ( lDs.next() ) {

         if ( lTaskA.toString().equals( lDs.getString( "task_key" ) ) ) {
            assertEquals( TASK_DESCRIPTION, lDs.getString( "event_sdesc" ) );
            assertEquals( 0, lDs.getInt( "is_fault" ) );
            assertEquals( 0, lDs.getInt( "is_fault_evaluated" ) );
            assertEquals( TASK_BARCODE, lDs.getString( "barcode_sdesc" ) );
            assertEquals( lInventoryA.toString(), lDs.getString( "inv_key" ) );
            assertEquals( INV_DESCRIPTION, lDs.getString( "inv_no_sdesc" ) );
            assertEquals( lLocationA.toString(), lDs.getString( "loc_key" ) );
         } else if ( lTaskB.toString().equals( lDs.getString( "task_key" ) ) ) {
            assertEquals( TASK_DESCRIPTION, lDs.getString( "event_sdesc" ) );
            assertEquals( 0, lDs.getInt( "is_fault" ) );
            assertEquals( 0, lDs.getInt( "is_fault_evaluated" ) );
            assertEquals( TASK_BARCODE, lDs.getString( "barcode_sdesc" ) );
            assertEquals( lInventoryB.toString(), lDs.getString( "inv_key" ) );
            assertEquals( INV_DESCRIPTION, lDs.getString( "inv_no_sdesc" ) );
            assertEquals( lLocationB.toString(), lDs.getString( "loc_key" ) );
         } else {
            fail( "Query returned an unexpected task = " + lDs.getString( "task_key" ) );
         }
      }
   }


   /**
    * Verify that when the user's department location matches the location of the task, that the
    * query returns that task.
    */
   @Test
   public void testWhenUserDeptLocationMatchesTaskLocation() {
      LocationKey lLocation = new LocationDomainBuilder().build();

      // Query will use the inventory's location as the task location.
      InventoryKey lTaskInventory = new InventoryBuilder().withDescription( INV_DESCRIPTION )
            .atLocation( lLocation ).build();

      TaskKey lTask = new TaskBuilder().withBarcode( TASK_BARCODE )
            .withDescription( TASK_DESCRIPTION ).withLabour( RefLabourSkillKey.ENG, 1.0 )
            .onInventory( lTaskInventory ).atLocation( lLocation ).build();

      HumanResourceKey lHr = setupUserInEngDept( lLocation );

      DataSet lDs = execute( lHr );

      assertEquals( 1, lDs.getRowCount() );
      assertTrue( lDs.next() );

      assertEquals( lTask.toString(), lDs.getString( "task_key" ) );
      assertEquals( TASK_DESCRIPTION, lDs.getString( "event_sdesc" ) );
      assertEquals( 0, lDs.getInt( "is_fault" ) );
      assertEquals( 0, lDs.getInt( "is_fault_evaluated" ) );
      assertEquals( TASK_BARCODE, lDs.getString( "barcode_sdesc" ) );
      assertEquals( lTaskInventory.toString(), lDs.getString( "inv_key" ) );
      assertEquals( INV_DESCRIPTION, lDs.getString( "inv_no_sdesc" ) );
      assertEquals( lLocation.toString(), lDs.getString( "loc_key" ) );
   }


   /**
    * Verify that when the user's department location matches the location of the task, however, the
    * user does not have authority over the inventory, that the query does not return the task.
    */
   @Test
   public void testWhenUserDeptLocationMatchesTaskLocationButUserDoesNotHaveAuthority() {
      LocationKey lLocation = new LocationDomainBuilder().build();

      // Query will use the inventory's location as the task location.
      InventoryKey lTaskInventory = new InventoryBuilder().withDescription( INV_DESCRIPTION )
            .atLocation( lLocation ).withRequiredAuthority( AUTHORITY_A ).build();

      new TaskBuilder().withBarcode( TASK_BARCODE ).withDescription( TASK_DESCRIPTION )
            .withLabour( RefLabourSkillKey.ENG, 1.0 ).onInventory( lTaskInventory )
            .atLocation( lLocation ).build();

      // Set up the user with no authorities.
      HumanResourceKey lHr = setupUserInEngDept( lLocation );

      DataSet lDs = execute( lHr );

      assertTrue( lDs.isEmpty() );
   }


   /**
    * Verify that when the user's departments' locations do not include the location of the task,
    * that the query does not return the task.
    */
   @Test
   public void testWhenUserDeptLocationsDoesNotIncludeTaskLocation() {
      LocationKey lWpLocation = new LocationDomainBuilder().build();
      LocationKey lDeptLocation = new LocationDomainBuilder().build();

      InventoryKey lAircraft = new InventoryBuilder().build();

      InventoryKey lTaskInventory =
            new InventoryBuilder().withHighestInventory( lAircraft ).build();

      TaskKey lWorkPackage =
            new TaskBuilder().onInventory( lAircraft ).atLocation( lWpLocation ).build();

      FaultKey lFault = new SdFaultBuilder().build();

      TaskKey lCorrectiveTask = new TaskBuilder().withLabour( RefLabourSkillKey.ENG, 1.0 )
            .onInventory( lTaskInventory ).withParentTask( lWorkPackage ).build();

      new EventRelationshipBuilder().fromEvent( lFault ).toEvent( lCorrectiveTask )
            .withType( RefRelationTypeKey.CORRECT ).build();

      // Ensure the user is a department location that differs from the work package/task location.
      HumanResourceKey lHr = setupUserInEngDept( lDeptLocation );

      DataSet lDs = execute( lHr );

      assertTrue( "Query unexpectedly returned rows.", lDs.isEmpty() );
   }


   /**
    * Verify that when the user has authority over all inventory that all tasks with uncompleted ENG
    * labour rows are returned, regardless of their location. As well, regardless of whether the
    * user is in an ENG department or not.
    */
   @Test
   public void testWhenUserHasAllAuthority() {
      LocationKey lLocationA = new LocationDomainBuilder().withCode( LOCATION_CODE_A ).build();

      InventoryKey lInventory_A = new InventoryBuilder().atLocation( lLocationA )
            .withDescription( INV_DESCRIPTION_A ).build();

      TaskKey lTaskA =
            new TaskBuilder().withBarcode( TASK_BARCODE ).withDescription( TASK_DESCRIPTION )
                  .withLabour( RefLabourSkillKey.ENG, 1.0 ).onInventory( lInventory_A ).build();

      LocationKey lLocationB = new LocationDomainBuilder().withCode( LOCATION_CODE_B ).build();

      InventoryKey lInventory_B = new InventoryBuilder().atLocation( lLocationB )
            .withDescription( INV_DESCRIPTION_B ).build();

      TaskKey lTaskB =
            new TaskBuilder().withBarcode( TASK_BARCODE ).withDescription( TASK_DESCRIPTION )
                  .withLabour( RefLabourSkillKey.ENG, 1.0 ).onInventory( lInventory_B ).build();

      LocationKey lDeptLocation = new LocationDomainBuilder().build();

      HumanResourceKey lHr = setupUserInNonEngDept( lDeptLocation );
      giveUserAllAuthority( lHr );

      DataSet lDs = execute( lHr );

      assertEquals( 2, lDs.getRowCount() );

      while ( lDs.next() ) {

         if ( lTaskA.toString().equals( lDs.getString( "task_key" ) ) ) {
            assertEquals( TASK_DESCRIPTION, lDs.getString( "event_sdesc" ) );
            assertEquals( 0, lDs.getInt( "is_fault" ) );
            assertEquals( 0, lDs.getInt( "is_fault_evaluated" ) );
            assertEquals( TASK_BARCODE, lDs.getString( "barcode_sdesc" ) );
            assertEquals( lInventory_A.toString(), lDs.getString( "inv_key" ) );
            assertEquals( INV_DESCRIPTION_A, lDs.getString( "inv_no_sdesc" ) );
            assertEquals( lLocationA.toString(), lDs.getString( "loc_key" ) );
         } else if ( lTaskB.toString().equals( lDs.getString( "task_key" ) ) ) {
            assertEquals( TASK_DESCRIPTION, lDs.getString( "event_sdesc" ) );
            assertEquals( 0, lDs.getInt( "is_fault" ) );
            assertEquals( 0, lDs.getInt( "is_fault_evaluated" ) );
            assertEquals( TASK_BARCODE, lDs.getString( "barcode_sdesc" ) );
            assertEquals( lInventory_B.toString(), lDs.getString( "inv_key" ) );
            assertEquals( INV_DESCRIPTION_B, lDs.getString( "inv_no_sdesc" ) );
            assertEquals( lLocationB.toString(), lDs.getString( "loc_key" ) );
         } else {
            fail( "Query returned an unexpected task = " + lDs.getString( "task_key" ) );
         }
      }
   }


   /**
    * Verify that when the user has authority over the task inventory but the task location is not
    * at any of the user's ENG department locations or sub-locations, that no tasks are returned
    * from the query.
    */
   @Test
   public void testWhenUserHasAuthorityButTaskNotAtUsersEngDeptLocations() {
      LocationKey lLocation = new LocationDomainBuilder().build();

      // Inventory the user has authority over.
      InventoryKey lInventory_A = new InventoryBuilder().atLocation( lLocation )
            .withDescription( INV_DESCRIPTION_A ).withRequiredAuthority( AUTHORITY_A ).build();

      new TaskBuilder().withBarcode( TASK_BARCODE ).withDescription( TASK_DESCRIPTION )
            .withLabour( RefLabourSkillKey.ENG, 1.0 ).onInventory( lInventory_A ).build();

      // User with authority but not in any ENG departments.
      HumanResourceKey lHr = setupUserInNonEngDept( lLocation );
      giveUserAuthority( lHr, AUTHORITY_A );

      DataSet lDs = execute( lHr );

      assertTrue( lDs.isEmpty() );
   }


   /**
    * Verify that when the user has authority over the task inventory but is does not belong to any
    * ENG departments, that no tasks are returned from the query.
    */
   @Test
   public void testWhenUserHasAuthorityButUserNotInEngDept() {
      LocationKey lLocation = new LocationDomainBuilder().build();

      // Inventory the user has authority over.
      InventoryKey lInventory_A = new InventoryBuilder().atLocation( lLocation )
            .withDescription( INV_DESCRIPTION_A ).withRequiredAuthority( AUTHORITY_A ).build();

      new TaskBuilder().withBarcode( TASK_BARCODE ).withDescription( TASK_DESCRIPTION )
            .withLabour( RefLabourSkillKey.ENG, 1.0 ).onInventory( lInventory_A ).build();

      // User with authority but not in any ENG departments.
      HumanResourceKey lHr = setupUserInNonEngDept( lLocation );
      giveUserAuthority( lHr, AUTHORITY_A );

      DataSet lDs = execute( lHr );

      assertTrue( lDs.isEmpty() );
   }


   /**
    * Verify that when the user has authority over the highest inventory of the task inventory AND
    * the task inventory itself has a different authority requirement, that the task is returned
    * from the query.
    */
   @Test
   public void testWhenUserHasAuthorityOverHighestInventoryButTaskInventoryHasAnotherAuthority() {
      LocationKey lLocation = new LocationDomainBuilder().build();

      // Inventory the user has authority over.
      InventoryKey lInventory_A = new InventoryBuilder().atLocation( lLocation )
            .withDescription( INV_DESCRIPTION_A ).withRequiredAuthority( AUTHORITY_A ).build();

      // Child task inventory that the user does not have authority over.
      InventoryKey lTaskInventory =
            new InventoryBuilder().withParentInventory( lInventory_A ).atLocation( lLocation )
                  .withDescription( INV_DESCRIPTION ).withRequiredAuthority( AUTHORITY_B ).build();

      TaskKey lTaskA =
            new TaskBuilder().withBarcode( TASK_BARCODE ).withDescription( TASK_DESCRIPTION )
                  .withLabour( RefLabourSkillKey.ENG, 1.0 ).onInventory( lTaskInventory ).build();

      // Another inventory the user does not have authority over.
      InventoryKey lInventory_B = new InventoryBuilder().atLocation( lLocation )
            .withDescription( INV_DESCRIPTION_B ).withRequiredAuthority( AUTHORITY_B ).build();

      new TaskBuilder().withBarcode( TASK_BARCODE ).withDescription( TASK_DESCRIPTION )
            .withLabour( RefLabourSkillKey.ENG, 1.0 ).onInventory( lInventory_B ).build();

      HumanResourceKey lHr = setupUserInEngDept( lLocation );
      giveUserAuthority( lHr, AUTHORITY_A );

      DataSet lDs = execute( lHr );

      assertEquals( 1, lDs.getRowCount() );
      assertTrue( lDs.next() );

      assertEquals( lTaskA.toString(), lDs.getString( "task_key" ) );
      assertEquals( TASK_DESCRIPTION, lDs.getString( "event_sdesc" ) );
      assertEquals( 0, lDs.getInt( "is_fault" ) );
      assertEquals( 0, lDs.getInt( "is_fault_evaluated" ) );
      assertEquals( TASK_BARCODE, lDs.getString( "barcode_sdesc" ) );
      assertEquals( lTaskInventory.toString(), lDs.getString( "inv_key" ) );
      assertEquals( INV_DESCRIPTION, lDs.getString( "inv_no_sdesc" ) );
      assertEquals( lLocation.toString(), lDs.getString( "loc_key" ) );
   }


   /**
    * Verify that when the user has authority over the highest inventory of the task inventory that
    * the task is returned from the query, regardless of whether the user is in an ENG department or
    * not.
    */
   @Test
   public void testWhenUserHasAuthorityOverTasksHighestInventory() {
      LocationKey lLocation = new LocationDomainBuilder().build();

      // Inventory the user has authority over.
      InventoryKey lInventory_A = new InventoryBuilder().atLocation( lLocation )
            .withDescription( INV_DESCRIPTION_A ).withRequiredAuthority( AUTHORITY_A ).build();

      // Child task inventory that the user does not have authority over.
      InventoryKey lTaskInventory = new InventoryBuilder().withParentInventory( lInventory_A )
            .atLocation( lLocation ).withDescription( INV_DESCRIPTION ).build();

      TaskKey lTaskA =
            new TaskBuilder().withBarcode( TASK_BARCODE ).withDescription( TASK_DESCRIPTION )
                  .withLabour( RefLabourSkillKey.ENG, 1.0 ).onInventory( lTaskInventory ).build();

      // Another inventory the user does not have authority over.
      InventoryKey lInventory_B = new InventoryBuilder().atLocation( lLocation )
            .withDescription( INV_DESCRIPTION_B ).withRequiredAuthority( AUTHORITY_B ).build();

      new TaskBuilder().withBarcode( TASK_BARCODE ).withDescription( TASK_DESCRIPTION )
            .withLabour( RefLabourSkillKey.ENG, 1.0 ).onInventory( lInventory_B ).build();

      HumanResourceKey lHr = setupUserInEngDept( lLocation );
      giveUserAuthority( lHr, AUTHORITY_A );

      DataSet lDs = execute( lHr );

      assertEquals( 1, lDs.getRowCount() );
      assertTrue( lDs.next() );

      assertEquals( lTaskA.toString(), lDs.getString( "task_key" ) );
      assertEquals( TASK_DESCRIPTION, lDs.getString( "event_sdesc" ) );
      assertEquals( 0, lDs.getInt( "is_fault" ) );
      assertEquals( 0, lDs.getInt( "is_fault_evaluated" ) );
      assertEquals( TASK_BARCODE, lDs.getString( "barcode_sdesc" ) );
      assertEquals( lTaskInventory.toString(), lDs.getString( "inv_key" ) );
      assertEquals( INV_DESCRIPTION, lDs.getString( "inv_no_sdesc" ) );
      assertEquals( lLocation.toString(), lDs.getString( "loc_key" ) );
   }


   /**
    * Verify that when the user is not in an ENG department that the query returns no tasks.
    */
   @Test
   public void testWhenUserNotInEngDept() {
      LocationKey lLocation = new LocationDomainBuilder().build();

      InventoryKey lTaskInventory = new InventoryBuilder().withDescription( INV_DESCRIPTION )
            .atLocation( lLocation ).build();

      new TaskBuilder().withBarcode( TASK_BARCODE ).withDescription( TASK_DESCRIPTION )
            .withLabour( RefLabourSkillKey.ENG, 1.0 ).onInventory( lTaskInventory )
            .atLocation( lLocation ).build();

      // User not in an ENG department but its department is located at the task location.
      HumanResourceKey lHr = setupUserInNonEngDept( lLocation );

      DataSet lDs = execute( lHr );

      assertTrue( lDs.isEmpty() );
   }


   /**
    * Assign the provided user to the provided task.
    *
    * @param aHr
    * @param aTask
    */
   private void assignUserToTask( HumanResourceKey aHr, TaskKey aTask ) {

      // Get the ENG labour row for the task.
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTask, "sched_db_id", "sched_id" );
      lArgs.add( RefLabourSkillKey.ENG, "labour_skill_db_id", "labour_skill_cd" );

      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "sched_labour", lArgs,
            "labour_db_id", "labour_id" );
      lQs.next();

      // Get the labour status (should be only one).
      lArgs = new DataSetArgument();
      lArgs.add( "labour_db_id", lQs.getInt( "labour_db_id" ) );
      lArgs.add( "labour_id", lQs.getInt( "labour_id" ) );

      lQs = QuerySetFactory.getInstance().executeQuery( "sched_labour_role", lArgs,
            "labour_role_db_id", "labour_role_id" );
      lQs.next();

      // Update the labour role status with the HR.
      DataSetArgument lSetArgs = new DataSetArgument();
      lSetArgs.add( aHr, "hr_db_id", "hr_id" );

      DataSetArgument lWhereArgs = new DataSetArgument();
      lWhereArgs.add( "labour_role_db_id", lQs.getInteger( "labour_role_db_id" ) );
      lWhereArgs.add( "labour_role_id", lQs.getInteger( "labour_role_id" ) );
      MxDataAccess.getInstance().executeUpdate( "sched_labour_role_status", lSetArgs, lWhereArgs );
   }


   /**
    * Execute the query with the probided HR key.
    *
    * @param aHr
    *
    * @return DataSet retuned from the query
    */
   private DataSet execute( HumanResourceKey aHr ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aHr, "aHrDbId", "aHrId" );

      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Give the provided user all authority.
    *
    * @param aHr
    */
   private void giveUserAllAuthority( HumanResourceKey aHr ) {

      // Update the user to have all authority.
      DataSetArgument lSetArgs = new DataSetArgument();
      lSetArgs.add( "all_authority_bool", 1 );

      DataSetArgument lWhereArgs = new DataSetArgument();
      lWhereArgs.add( aHr, "hr_db_id", "hr_id" );

      MxDataAccess.getInstance().executeUpdate( "org_hr", lSetArgs, lWhereArgs );
   }


   /**
    * Add the provided authority to the provided user.
    *
    * @param aHr
    * @param aAuthority
    */
   private void giveUserAuthority( HumanResourceKey aHr, AuthorityKey aAuthority ) {

      // Create an HR authority.
      DataSetArgument lInsertArgs = new DataSetArgument();
      lInsertArgs.add( aHr, "hr_db_id", "hr_id" );
      lInsertArgs.add( aAuthority, "authority_db_id", "authority_id" );
      MxDataAccess.getInstance().executeInsert( "org_hr_authority", lInsertArgs );
   }


   /**
    * Create a user and add it to a newly created department that is locationed at the provided
    * location and has the provided department type.
    *
    * @param aLocation
    * @param aDeptType
    *
    * @return HR key of the created user
    */
   private HumanResourceKey setupUserInDept( LocationKey aLocation, RefDeptTypeKey aDeptType ) {

      // Create a department.
      OrgWorkDept lOrgWorkDept = OrgWorkDept.create();
      lOrgWorkDept.setType( aDeptType );

      DepartmentKey lDepartment = lOrgWorkDept.insert();

      // Add a location to the department.
      DataSetArgument lInsertArgs = new DataSetArgument();
      lInsertArgs.add( aLocation, "loc_db_id", "loc_id" );
      lInsertArgs.add( lDepartment, "dept_db_id", "dept_id" );
      MxDataAccess.getInstance().executeInsert( "inv_loc_dept", lInsertArgs );

      // Create a user.
      HumanResourceKey lHr = new HumanResourceDomainBuilder().build();

      // Add the user to the department.
      lInsertArgs = new DataSetArgument();
      lInsertArgs.add( lDepartment, "dept_db_id", "dept_id" );
      lInsertArgs.add( lHr, "hr_db_id", "hr_id" );
      MxDataAccess.getInstance().executeInsert( "org_dept_hr", lInsertArgs );

      return lHr;
   }


   /**
    * Create a user and add it to a newly created ENG department that is locationed at the provided
    * location.
    *
    * @param aLocation
    *
    * @return HR key of the created user
    */
   private HumanResourceKey setupUserInEngDept( LocationKey aLocation ) {
      return setupUserInDept( aLocation, RefDeptTypeKey.ENG );
   }


   /**
    * Create a user and add it to a newly created department, which is not and ENG dept, that is
    * locationed at the provided location.
    *
    * @param aLocation
    *
    * @return HR key of the created user
    */
   private HumanResourceKey setupUserInNonEngDept( LocationKey aLocation ) {
      return setupUserInDept( aLocation, RefDeptTypeKey.CREW );
   }
}
