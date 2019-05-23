
package com.mxi.mx.web.query.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.ibm.icu.util.Calendar;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventDeadlineKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.RefDomainTypeKey;
import com.mxi.mx.core.key.RefEngUnitKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.evt.EvtSchedDeadTable;
import com.mxi.mx.core.table.sched.SchedStaskTable;


/**
 * Tests the OpenTasksByInventoryExtraDueInfo query.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class OpenTasksByInventoryExtraDueInfoTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   @ClassRule
   public static FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule =
         new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            OpenTasksByInventoryExtraDueInfoTest.class );
   }


   // Table columns
   private static final String EVENT_KEY = "event_key";
   private static final String EVENT_SDESC = "event_sdesc";
   private static final String EVENT_STATUS_CD = "event_status_cd";
   private static final String SCHED_PRIORITY_CD = "sched_priority_cd";
   private static final String ACTUAL_START_GDT = "actual_start_gdt";
   private static final String TASK_CLASS_CD = "task_class_cd";
   private static final String WORK_TYPE_CD = "work_type_cd";
   private static final String REPEAT_INTERVAL = "repeat_interval";
   private static final String TASK_ORIGINATOR_CD = "task_originator_cd";
   private static final String TASK_PRIORITY_CD = "task_priority_cd";
   private static final String BARCODE_SDESC = "barcode_sdesc";
   private static final String PARTS_READY_BOOL = "parts_ready_bool";
   private static final String TOOLS_READY_BOOL = "tools_ready_bool";
   private static final String LRP_BOOL = "lrp_bool";
   private static final String ETOPS_BOOL = "etops_bool";
   private static final String WORK_LOC_KEY = "work_loc_key";
   private static final String INVENTORY_KEY = "inventory_key";
   private static final String INV_NO_SDESC = "inv_no_sdesc";
   private static final String LOC_KEY = "loc_key";
   private static final String USER_SUBCLASS_CD = "user_subclass_cd";
   private static final String DEVIATION_QT = "deviation_qt";
   private static final String USAGE_REM_QT = "usage_rem_qt";
   private static final String SCHED_DEAD_DT = "sched_dead_dt";
   private static final String DOMAIN_TYPE_CD = "domain_type_cd";
   private static final String ENG_UNIT_CD = "eng_unit_cd";
   private static final String PRECISION_QT = "precision_qt";
   private static final String DATA_TYPE_CD = "data_type_cd";
   private static final String ENG_UNIT_MULT_QT = "eng_unit_mult_qt";
   private static final String EXT_SCHED_DEAD_DT = "ext_sched_dead_dt";
   private static final String SORT_DUE_DT = "sort_due_dt";
   private static final String CHECK_KEY = "check_key";
   private static final String CHECK_SDESC = "check_sdesc";
   private static final String CHECK_BARCODE = "check_barcode";
   private static final String WO_REF_SDESC = "wo_ref_sdesc";
   private static final String PO_KEY = "po_key";
   private static final String SOFT_DEADLINE = "soft_deadline";
   private static final String NSV_TASK = "nsv_task";
   private static final String DRIVING_TASK_KEY = "driving_task_key";
   private static final String DRIVING_TASK_SDESC = "driving_task_sdesc";
   private static final String DRIVING_BARCODE_SDESC = "driving_barcode_sdesc";
   private static final String PLAN_BY_DATE = "plan_by_date";
   private static final String CONFIG_POS_SDESC = "config_pos_sdesc";
   private static final String PREVENT_EXE_BOOL = "prevent_exe_bool";
   private static final String DO_AT_NEXT_INSTALL_BOOL = "do_at_next_install_bool";
   private static final String PREVENT_EXE_REVIEW_DT = "prevent_exe_review_dt";
   private static final String MUST_REMOVE_USER_CD = "must_remove_user_cd";
   private static final String REQUEST_STATUS_DB_ID = "request_status_db_id";
   private static final String REQUEST_STATUS_CD = "request_status_cd";
   private static final String PART_REQUEST_STATUS = "part_request_status";
   private static final String PART_REQUEST_ETA = "part_request_eta";
   private static final String PART_REQUEST_KEY = "part_request_key";
   private static final String WARNING_BOOL = "warning_bool";
   private static final String MATERIAL_AVAIL_SORT_DATE = "material_avail_sort_date";
   private static final String RO_REF_SDESC = "ro_ref_sdesc";

   // Common data
   private static final LocationKey LOCATION = new LocationKey( 1, 1 );
   private static final String DATE_STR = "17-Mar-0013 11:00:00";
   private static final String TRUE = "1";
   private static final String FALSE = "0";
   private static final String EMPTY_KEY_STR = ":";
   private static final String USER_SUBCLASS_A_CHECK = "A_CHECK";
   private static final String USER_SUBCLASS_INST = "INST";
   private static final String USER_SUBCLASS_RMVD = "RMVD";
   private static final String CYCLES_PRECISION = "0";
   private static final String CYCLES_DATA_TYPE = "CYCLES";
   private static final String CYCLES_MULTIPLIER = "1";
   private static final String HOURS_PRECISION = "2";
   private static final String HOURS_DATA_TYPE = "HOURS";
   private static final String HOURS_MULTIPLIER = "0.041667";
   private static final String TASK_MUST_BE_RMVD_OFF_WING = "Off Wing";
   private static final String EVENT_STATUS = "OPEN";
   private static final String EVENT_PRIORITY = "HIGH";
   private static final String TASK_ORIGINATOR = "orgntr";
   private static final String TASK_PRIORITY = "LOW";

   private static final InventoryKey INVENTORY = new InventoryKey( 1, 1 );
   private static final String INV_CONFIG_POS_DESC = "inv config pos";
   private static final String INV_DESC = "inv desc";

   private static final InventoryKey ROOT_INVENTORY = new InventoryKey( 1, 2 );
   private static final String ROOT_INV_DESC = "root inv desc";

   private static final DataTypeKey FLYING_HOURS = new DataTypeKey( 0, 1 );
   private static final DataTypeKey CYCLES = new DataTypeKey( 0, 10 );

   // Test data for first select in the union - tasks for inv and sub inv
   private static final EventKey EVENT_FOR_INV_TASK = new EventKey( 10, 10 );
   private static final String EVENT_FOR_INV_TASK_PART_DESC = "EVENT_FOR_INV_TASK";
   private static final String INV_TASK_CLASS = "inv task class";
   private static final String INV_TASK_BARCODE = "inv task barcode";
   private static final EventKey INV_TASK_DRIVING_EVENT = new EventKey( 10, 12 );
   private static final String INV_TASK_DRIVING_EVENT_DESC = "inv task driving event desc";
   private static final String INV_TASK_DRIVING_TASK_BARCODE = "inv task driving task barcode";
   private static final String INV_TASK_DRIVING_DEADLINE_DEVIATION = "789";
   private static final String INV_TASK_DRIVING_DEADLINE_USAGE_REMAINING = "741";
   private static final EventKey INV_TASK_CHECK = new EventKey( 10, 11 );
   private static final String INV_TASK_CHECK_DESC = "inv task check desc";
   private static final String INV_TASK_WO_REF = "inv task wo ref";
   private static final PurchaseOrderKey INV_TASK_CHECK_PO = new PurchaseOrderKey( 10, 10 );

   // Test data for second select in the union - install part tasks
   private static final EventKey EVENT_FOR_INST_PART_TASK = new EventKey( 1, 2 );
   private static final String EVENT_FOR_INST_PART_TASK_DESC = "EVENT_FOR_INST_PART_TASK";
   private static final String INST_TASK_CLASS = "inst task class";
   private static final String INST_TASK_BARCODE = "inst task barcode";
   private static final EventKey INST_TASK_DRIVING_EVENT = new EventKey( 1, 4 );
   private static final String INST_TASK_DRIVING_EVENT_DESC = "inst task driving event desc";
   private static final String INST_TASK_DRIVING_TASK_BARCODE = "inst task driving task barcode";
   private static final String INST_TASK_DRIVING_DEADLINE_DEVIATION = "789";
   private static final String INST_TASK_DRIVING_DEADLINE_USAGE_REMAINING = "741";

   // Test data for third select in the union - remove part tasks
   private static final EventKey EVENT_FOR_RMVD_PART_TASK = new EventKey( 2, 3 );
   private static final String EVENT_FOR_RMVD_PART_TASK_DESC = "EVENT_FOR_RMVD_PART_TASK";
   private static final String EVENT_FOR_RMVD_PART_TASK_DEVIATION = "123";
   private static final String EVENT_FOR_RMVD_PART_TASK_USAGE_REMAINING = "456";
   private static final String RMVD_TASK_CLASS = "rmvd task class";
   private static final String RMVD_TASK_BARCODE = "rmvd task barcode";
   private static final String RMVD_TASK_WO_REF = "rmvd task wo ref";
   private static final String RMVD_TASK_RO_REF = "rmvd task ro ref";
   private static final EventKey RMVD_TASK_CHECK = new EventKey( 2, 4 );
   private static final String RMVD_TASK_CHECK_DESC = "rmvd task check desc";
   private static final PurchaseOrderKey RMVD_TASK_CHECK_PO = new PurchaseOrderKey( 2, 2 );

   // Test data for task and work package without location scheduled
   private static final InventoryKey INVENTORY_NO_LOCATION = new InventoryKey( 1, 2 );
   private static final InventoryKey INVENTORY_FOR_SORT = new InventoryKey( 1, 3 );
   private static final EventKey EVENT_FOR_NO_LOCATION_TASK = new EventKey( 10, 14 );
   private static final EventKey EVENT_FOR_NO_LOCATION_WORK_PACKAGE = new EventKey( 10, 15 );

   private Map<String, Map<String, String>> iExpectedResultsMap;


   @Before
   public void setUp() throws Exception {
      setupExpectedResults();
   }


   /**
    * Scenario: *[WHERE_CLAUSE] Show Tasks with Soft Deadlines = 1, Show Next Stop Visit Tasks = 1,
    * aWorkLocationKey = null, aShowAssignedTasks = 1, aHideNonExcecutableTasks = 0, aDayCount = -1
    * Result: 3 tasks
    *
    * @throws Exception
    */
   @Test
   public void testShowSoftDeadlinesAndShowNSV() throws Exception {

      // setup query arguments for this test
      boolean lShowAssignedTasks = true;
      int lDayCount = -1;
      String lWorkLocationKey = null;
      boolean lHideNonExcecutableTasks = false;
      boolean lShowSoftDeadline = true;
      boolean aShowNSV = true;

      DataSet lResults = execute( INVENTORY, lShowAssignedTasks, lDayCount, lWorkLocationKey,
            lHideNonExcecutableTasks, FLYING_HOURS, CYCLES, lShowSoftDeadline, aShowNSV );

      assertFalse( "Smoke test failed to return results.", lResults.isEmpty() );

      assertEquals( "Smoke test returned wrong number of rows.", iExpectedResultsMap.size(),
            lResults.getRowCount() );

      // assert returned rows
      while ( lResults.next() ) {
         Map<String, String> lExpectedResults = determineExpectedResults( lResults );
         assertResults( lResults, lExpectedResults );
      }
   }


   /**
    * Scenario: *[WHERE_CLAUSE] Show Tasks with Soft Deadlines = 0, Show Next Stop Visit Tasks = 0,
    * aWorkLocationKey = null, aShowAssignedTasks = 1, aHideNonExcecutableTasks = 0, aDayCount = -1
    * Result: 3 tasks
    *
    * @throws Exception
    */
   @Test
   public void testNotShowSoftDeadlinesNotShowNSV() throws Exception {

      // setup query arguments for this test
      boolean lShowAssignedTasks = true;
      int lDayCount = -1;
      String lWorkLocationKey = null;
      boolean lHideNonExcecutableTasks = false;
      boolean lShowSoftDeadline = false;
      boolean aShowNSV = false;

      DataSet lResults = execute( INVENTORY, lShowAssignedTasks, lDayCount, lWorkLocationKey,
            lHideNonExcecutableTasks, FLYING_HOURS, CYCLES, lShowSoftDeadline, aShowNSV );

      assertFalse( "Smoke test failed to return results.", lResults.isEmpty() );

      assertEquals( "Smoke test returned wrong number of rows.", iExpectedResultsMap.size(),
            lResults.getRowCount() );

      // assert returned rows
      while ( lResults.next() ) {
         Map<String, String> lExpectedResults = determineExpectedResults( lResults );
         assertResults( lResults, lExpectedResults );
      }
   }


   /**
    * Scenario: *[WHERE_CLAUSE] Show Tasks with Soft Deadlines = 1, Show Next Stop Visit Tasks = 0,
    * aWorkLocationKey = null, aShowAssignedTasks = 1, aHideNonExcecutableTasks = 0, aDayCount = -1
    * Result: 3 tasks
    *
    * @throws Exception
    */
   @Test
   public void testShowSoftDeadlines() throws Exception {

      // setup query arguments for this test
      boolean lShowAssignedTasks = true;
      int lDayCount = -1;
      String lWorkLocationKey = null;
      boolean lHideNonExcecutableTasks = false;
      boolean lShowSoftDeadline = true;
      boolean aShowNSV = false;

      DataSet lResults = execute( INVENTORY, lShowAssignedTasks, lDayCount, lWorkLocationKey,
            lHideNonExcecutableTasks, FLYING_HOURS, CYCLES, lShowSoftDeadline, aShowNSV );

      assertFalse( "Smoke test failed to return results.", lResults.isEmpty() );

      assertEquals( "Smoke test returned wrong number of rows.", iExpectedResultsMap.size(),
            lResults.getRowCount() );

      // assert returned rows
      while ( lResults.next() ) {
         Map<String, String> lExpectedResults = determineExpectedResults( lResults );
         assertResults( lResults, lExpectedResults );
      }
   }


   /**
    * Scenario: *[WHERE_CLAUSE] Show Tasks with Soft Deadlines = 1, Show Next Stop Visit Tasks = 1,
    * aWorkLocationKey = null, aShowAssignedTasks = 0, aHideNonExcecutableTasks = 0, aDayCount = -1
    * Result: 0 task
    *
    * @throws Exception
    */
   @Test
   public void testNotShowAssignedTasksNotHideNonExcutableTasks() throws Exception {

      // setup query arguments for this test
      boolean lShowAssignedTasks = false;
      int lDayCount = -1;
      String lWorkLocationKey = null;
      boolean lHideNonExcecutableTasks = false;
      boolean lShowSoftDeadline = true;
      boolean aShowNSV = true;

      DataSet lResults = execute( INVENTORY, lShowAssignedTasks, lDayCount, lWorkLocationKey,
            lHideNonExcecutableTasks, FLYING_HOURS, CYCLES, lShowSoftDeadline, aShowNSV );

      assertEquals( "Smoke test failed to return results.", true, lResults.isEmpty() );
   }


   /**
    * Scenario: *[WHERE_CLAUSE] Show Tasks with Soft Deadlines = 1, Show Next Stop Visit Tasks = 1,
    * aWorkLocationKey = null, aShowAssignedTasks = 0, aHideNonExcecutableTasks = 1, aDayCount = -1
    * Result: 0 task
    *
    * @throws Exception
    */
   @Test
   public void testHideNonExecutableTasks() throws Exception {

      // setup query arguments for this test
      boolean lShowAssignedTasks = false;
      int lDayCount = -1;
      String lWorkLocationKey = null;
      boolean lHideNonExcecutableTasks = true;
      boolean lShowSoftDeadline = true;
      boolean aShowNSV = true;

      DataSet lResults = execute( INVENTORY, lShowAssignedTasks, lDayCount, lWorkLocationKey,
            lHideNonExcecutableTasks, FLYING_HOURS, CYCLES, lShowSoftDeadline, aShowNSV );

      assertEquals( "Smoke test failed to return results.", true, lResults.isEmpty() );
   }


   /**
    * Scenario: *[WHERE_CLAUSE] Show Tasks with Soft Deadlines = 1, Show Next Stop Visit Tasks = 1,
    * aWorkLocationKey = null, aShowAssignedTasks = 1, aHideNonExcecutableTasks = 1, aDayCount = -1
    * Result: 3 tasks
    *
    * @throws Exception
    */
   @Test
   public void testShowAssignedTasksAndHideNonExecutableTasks() throws Exception {

      // setup query arguments for this test
      boolean lShowAssignedTasks = true;
      int lDayCount = -1;
      String lWorkLocationKey = null;
      boolean lHideNonExcecutableTasks = true;
      boolean lShowSoftDeadline = true;
      boolean aShowNSV = true;

      DataSet lResults = execute( INVENTORY, lShowAssignedTasks, lDayCount, lWorkLocationKey,
            lHideNonExcecutableTasks, FLYING_HOURS, CYCLES, lShowSoftDeadline, aShowNSV );

      assertFalse( "Smoke test failed to return results.", lResults.isEmpty() );

      assertEquals( "Smoke test returned wrong number of rows.", iExpectedResultsMap.size(),
            lResults.getRowCount() );

      // assert returned rows
      while ( lResults.next() ) {
         Map<String, String> lExpectedResults = determineExpectedResults( lResults );
         assertResults( lResults, lExpectedResults );
      }
   }


   /**
    * Scenario: When a task is assigned to a work package, and the work package has no location
    * scheduled yet, verify that the work package information is NOT missing on task page.
    *
    * @throws Exception
    */
   @Test
   public void testTaskAssignedToWorkPackageWithoutLocationScheduledIsReturned() throws Exception {

      boolean lShowAssignedTasks = true;
      int lDayCount = -1;
      String lWorkLocationKey = null;
      boolean lHideNonExcecutableTasks = true;
      boolean lShowSoftDeadline = true;
      boolean aShowNSV = true;

      DataSet lResults =
            execute( INVENTORY_NO_LOCATION, lShowAssignedTasks, lDayCount, lWorkLocationKey,
                  lHideNonExcecutableTasks, FLYING_HOURS, CYCLES, lShowSoftDeadline, aShowNSV );

      // verify only one result returned
      assertEquals( "Incorrect number of rows.", 1, lResults.getRowCount() );

      while ( lResults.next() ) {
         // verify the task is correctly retrieved
         assertEquals( "Incorrect task retrieved", EVENT_FOR_NO_LOCATION_TASK.toString(),
               lResults.getString( EVENT_KEY ) );

         // verify that the work package information is not missing
         assertEquals( "Incorrect work package for the task",
               EVENT_FOR_NO_LOCATION_WORK_PACKAGE.toString(), lResults.getString( CHECK_KEY ) );
         assertEquals( "Incorrect work package check description", EVENT_FOR_INV_TASK_PART_DESC,
               lResults.getString( CHECK_SDESC ) );
         assertEquals( "Incorrect work package description", INV_TASK_BARCODE,
               lResults.getString( CHECK_BARCODE ) );
         assertEquals( "Incorrect work package barcode", INV_TASK_WO_REF,
               lResults.getString( WO_REF_SDESC ) );

      }
   }


   /**
    * Scenario: When a task is set a 'Plan By Date' and is not set a 'Extension Due Date', verify
    * that tasks are still on the list for any value (blank or number) in the "Show open events
    * within the next __ days" field that includes the tasks' due date
    *
    * @throws Exception
    */
   @Test
   public void testWorkPackageNotFilteredOutWithPlanByDate() throws Exception {

      boolean lShowAssignedTasks = true;
      boolean lHideNonExcecutableTasks = true;
      boolean lShowSoftDeadline = true;
      boolean aShowNSV = true;

      int lDayCount = 90;
      String lWorkLocationKey = null;

      DataSet lResults =
            execute( INVENTORY_NO_LOCATION, lShowAssignedTasks, lDayCount, lWorkLocationKey,
                  lHideNonExcecutableTasks, FLYING_HOURS, CYCLES, lShowSoftDeadline, aShowNSV );

      // verify the task is still returned
      assertEquals( "Incorrect number of rows.", 1, lResults.getRowCount() );

      lResults.next();

      // verify the task is correctly retrieved
      assertEquals( "Incorrect task retrieved", EVENT_FOR_NO_LOCATION_TASK.toString(),
            lResults.getString( EVENT_KEY ) );
   }


   /**
    * <b>Scenario:</b>
    * <table>
    * <tr>
    * <td>Task 1</td>
    * <td>only have sched_dead_dt on 2017/June 18</td>
    * </tr>
    * <tr>
    * <td>Task 2</td>
    * <td>sched_dead_dt on 2017/June 17 && ext_sched_deadline on 2017/June 19</td>
    * </tr>
    * <tr>
    * <td>Task 3</td>
    * <td>sched_dead_dt on 2017/June 19 && plan_by_date on 2017/June 16</td>
    * </tr>
    * <tr>
    * <td>Task 4</td>
    * <td>sched_dead_dt on 2017/June 17 && plan_by_date on 2017/June 15 && ext_sched_deadline on
    * 2017/June 19</td>
    * </tr>
    * <tr>
    * <td>Task 5</td>
    * <td>without any due date</td>
    * </tr>
    *
    * </table>
    * <br/>
    * <b>Expected Sort Result:</b>
    * <ol>
    * <li>Task 4 (2017/June 15)</li>
    * <li>Task 3 (2017/June 16)</li>
    * <li>Task 1 (2017/June 18)</li>
    * <li>Task 2 (2017/June 19)</li>
    * <li>Task 5 null</li>
    * </ol>
    *
    * @throws Exception
    */
   @Test
   public void testTasksWithPlanByDateOrExtDeadlineSortedCorrectly() throws Exception {

      boolean lShowAssignedTasks = true;
      boolean lHideNonExcecutableTasks = true;
      boolean lShowSoftDeadline = true;
      boolean aShowNSV = true;

      int lDayCount = 90;
      String lWorkLocationKey = null;

      DataSet lResults =
            execute( INVENTORY_FOR_SORT, lShowAssignedTasks, lDayCount, lWorkLocationKey,
                  lHideNonExcecutableTasks, FLYING_HOURS, CYCLES, lShowSoftDeadline, aShowNSV );

      // verify 5 tasks returned
      assertEquals( "Incorrect number of rows.", 5, lResults.getRowCount() );

      // verify the 5 tasks order is: task 4, task 3, task 1, task 2, task 5
      lResults.next();
      assertEquals( "Incorrect task order.", "Task 4", lResults.getString( BARCODE_SDESC ) );
      assertEquals( "Incorrect due date.", "15-Jun-0017 11:00:00",
            lResults.getString( SORT_DUE_DT ) );
      lResults.next();
      assertEquals( "Incorrect task order.", "Task 3", lResults.getString( BARCODE_SDESC ) );
      assertEquals( "Incorrect due date.", "16-Jun-0017 11:00:00",
            lResults.getString( SORT_DUE_DT ) );
      lResults.next();
      assertEquals( "Incorrect task order.", "Task 1", lResults.getString( BARCODE_SDESC ) );
      assertEquals( "Incorrect due date.", "18-Jun-0017 11:00:00",
            lResults.getString( SORT_DUE_DT ) );
      lResults.next();
      assertEquals( "Incorrect task order.", "Task 2", lResults.getString( BARCODE_SDESC ) );
      assertEquals( "Incorrect due date.", "19-Jun-0017 11:00:00",
            lResults.getString( SORT_DUE_DT ) );

      lResults.next();
      assertEquals( "Incorrect task order.", "Task 5", lResults.getString( BARCODE_SDESC ) );
      assertEquals( "Incorrect due date.", null, lResults.getString( SORT_DUE_DT ) );
   }


   /**
    * <b>Scenario:</b>
    * <table>
    * <tr>
    * <td>Task 1</td>
    * <td>only have sched_dead_dt as (today + 10)</td>
    * </tr>
    * <tr>
    * <td>Task 2</td>
    * <td>sched_dead_dt as (today + 11) and extend due date as (today + 13)</td>
    * </tr>
    * <tr>
    * <td>Task 3</td>
    * <td>sched_dead_dt as (today + 11) and plan by date as (today + 5)</td>
    * </tr>
    * <tr>
    * <td>Task 4</td>
    * <td>sched_dead_dt as (today + 10), extend due date as (today + 12), and plan by date as (today
    * + 3)</td>
    * </tr>
    * <tr>
    * <td>Task 5</td>
    * <td>without any due date</td>
    * </tr>
    * </table>
    * <br/>
    * <b>Expected Filter Result (dayCount = 3):</b>
    * <ol>
    * <li>Task 4</li>
    * <li>Task 3</li>
    * <li>Task 5</li>
    * </ol>
    *
    * @throws Exception
    */
   @Test
   public void testTasksWithPlanByDateOrExtDeadlineCanBeFilteredCorrectly() throws Exception {

      boolean lShowAssignedTasks = true;
      boolean lHideNonExcecutableTasks = true;
      boolean lShowSoftDeadline = true;
      boolean lShowNSV = true;

      String lWorkLocationKey = null;

      DataTypeKey lDataTypeKey = new DataTypeKey( 0, 21 );
      Calendar lCalendar = Calendar.getInstance();
      Date lTodayDate = new Date();

      // update task 1 scheduled due date as (today + 10)
      TaskKey lTaskKey1 = new TaskKey( 10, 16 );
      lCalendar.setTime( lTodayDate );
      lCalendar.add( Calendar.DAY_OF_MONTH, 10 );
      updateTaskSchedDeadline( lTaskKey1, lDataTypeKey, lCalendar.getTime() );

      // update task 2 scheduled due date as (today + 11) and extend due date as (today + 13)
      TaskKey lTaskKey2 = new TaskKey( 10, 17 );
      lCalendar.setTime( lTodayDate );
      lCalendar.add( Calendar.DAY_OF_MONTH, 11 );
      updateTaskSchedDeadline( lTaskKey2, lDataTypeKey, lCalendar.getTime() );

      // update task 3 scheduled due date as (today + 11) and plan by date as (today + 5)
      TaskKey lTaskKey3 = new TaskKey( 10, 19 );

      lCalendar.setTime( lTodayDate );
      lCalendar.add( Calendar.DAY_OF_MONTH, 11 );
      updateTaskSchedDeadline( lTaskKey3, lDataTypeKey, lCalendar.getTime() );

      lCalendar.setTime( lTodayDate );
      lCalendar.add( Calendar.DAY_OF_MONTH, 5 );
      updateTaskPlanByDate( lTaskKey3, lCalendar.getTime() );

      // update task 4 scheduled due date as (today + 10), extend due date as (today + 12), and plan
      // by date as (today + 3)
      TaskKey lTaskKey4 = new TaskKey( 10, 18 );

      lCalendar.setTime( lTodayDate );
      lCalendar.add( Calendar.DAY_OF_MONTH, 10 );
      updateTaskSchedDeadline( lTaskKey4, lDataTypeKey, lCalendar.getTime() );

      lCalendar.setTime( lTodayDate );
      lCalendar.add( Calendar.DAY_OF_MONTH, 3 );
      updateTaskPlanByDate( lTaskKey4, lCalendar.getTime() );

      // Verify the task due dates are correct
      int lDayCount = 90;
      DataSet lResults =
            execute( INVENTORY_FOR_SORT, lShowAssignedTasks, lDayCount, lWorkLocationKey,
                  lHideNonExcecutableTasks, FLYING_HOURS, CYCLES, lShowSoftDeadline, lShowNSV );

      // verify 5 tasks returned
      assertEquals( "Incorrect number of rows.", 5, lResults.getRowCount() );

      // verify the 5 tasks order is: task 4, task 3, task 1, task 2, task 5 and their due dates are
      // correct.
      SimpleDateFormat lDateFormat = new SimpleDateFormat( "dd-MMM-yyyy HH:mm:ss" );

      lResults.next();
      lCalendar.setTime( lTodayDate );
      lCalendar.add( Calendar.DAY_OF_MONTH, 3 );
      assertEquals( "Incorrect task order.", "Task 4", lResults.getString( BARCODE_SDESC ) );
      assertEquals( "Incorrect due date.", lDateFormat.format( lCalendar.getTime() ),
            lResults.getString( SORT_DUE_DT ) );

      lResults.next();
      lCalendar.setTime( lTodayDate );
      lCalendar.add( Calendar.DAY_OF_MONTH, 5 );
      assertEquals( "Incorrect task order.", "Task 3", lResults.getString( BARCODE_SDESC ) );
      assertEquals( "Incorrect due date.", lDateFormat.format( lCalendar.getTime() ),
            lResults.getString( SORT_DUE_DT ) );

      lResults.next();
      lCalendar.setTime( lTodayDate );
      lCalendar.add( Calendar.DAY_OF_MONTH, 10 );
      assertEquals( "Incorrect task order.", "Task 1", lResults.getString( BARCODE_SDESC ) );
      assertEquals( "Incorrect due date.", lDateFormat.format( lCalendar.getTime() ),
            lResults.getString( SORT_DUE_DT ) );

      lResults.next();
      lCalendar.setTime( lTodayDate );
      lCalendar.add( Calendar.DAY_OF_MONTH, 13 );
      assertEquals( "Incorrect task order.", "Task 2", lResults.getString( BARCODE_SDESC ) );
      assertEquals( "Incorrect due date.", lDateFormat.format( lCalendar.getTime() ),
            lResults.getString( SORT_DUE_DT ) );

      lResults.next();
      assertEquals( "Incorrect task order.", "Task 5", lResults.getString( BARCODE_SDESC ) );
      assertEquals( "Incorrect due date.", null, lResults.getString( SORT_DUE_DT ) );

      // Set the filter as 7 days and verify only 3 tasks returned
      lDayCount = 7;
      lResults = execute( INVENTORY_FOR_SORT, lShowAssignedTasks, lDayCount, lWorkLocationKey,
            lHideNonExcecutableTasks, FLYING_HOURS, CYCLES, lShowSoftDeadline, lShowNSV );

      // verify 3 tasks returned
      assertEquals( "Incorrect number of rows.", 3, lResults.getRowCount() );
      lResults.next();
      assertEquals( "Incorrect task order.", "Task 4", lResults.getString( BARCODE_SDESC ) );

      lResults.next();
      assertEquals( "Incorrect task order.", "Task 3", lResults.getString( BARCODE_SDESC ) );

      lResults.next();
      assertEquals( "Incorrect task order.", "Task 5", lResults.getString( BARCODE_SDESC ) );
      assertEquals( "Incorrect due date.", null, lResults.getString( SORT_DUE_DT ) );

   }


   /**
    * Assert the results by comparing the expected results to the data set.
    *
    * @param aResults
    *           data set of results
    * @param aExpectedResults
    *           map of expected results
    */
   private void assertResults( DataSet aResults, Map<String, String> aExpectedResults ) {
      for ( String lKey : aExpectedResults.keySet() ) {
         String lMsg = generateMsg( lKey, aResults );

         String lExpectedValue = aExpectedResults.get( lKey );
         assertEquals( lMsg, lExpectedValue, aResults.getString( lKey ) );
      }
   }


   /**
    * Returns a map of expected results that corresponds to the task's event key found in the
    * results.<br>
    * <br>
    * Because we can not guarentee the order in which the records are returned from the query, we
    * have to use a unique identifier (evt_event key) to map the expected values with the task.
    *
    * @param aResults
    *           data set of query results
    *
    * @return map of expected results (keyed off the select column name)
    *
    * @throws Exception
    */
   private Map<String, String> determineExpectedResults( DataSet aResults ) throws Exception {
      Map<String, String> lExpectedResults = new HashMap<String, String>();

      EventKey lEventKey = aResults.getKey( EventKey.class, EVENT_KEY );

      if ( EVENT_FOR_INV_TASK.equals( lEventKey ) ) {
         lExpectedResults = iExpectedResultsMap.get( EVENT_FOR_INV_TASK.toString() );
      } else if ( EVENT_FOR_INST_PART_TASK.equals( lEventKey ) ) {
         lExpectedResults = iExpectedResultsMap.get( EVENT_FOR_INST_PART_TASK.toString() );
      } else if ( EVENT_FOR_RMVD_PART_TASK.equals( lEventKey ) ) {
         lExpectedResults = iExpectedResultsMap.get( EVENT_FOR_RMVD_PART_TASK.toString() );
      } else {
         throw new Exception(
               "Expected results not found; key=" + aResults.getKey( EventKey.class, EVENT_KEY ) );
      }

      return lExpectedResults;
   }


   /**
    * Execute the query with the provided arguments.
    *
    * @param aInventory
    * @param aShowAssignedTasks
    * @param aDayCount
    * @param aWorkLocationKey
    * @param aHideNonExcecutableTasks
    * @param aDataType2
    *           The data type of the extra due info
    * @param aDataType3
    *           Another data type of the extra due info
    * @param aShowSoftDeadlineTasks
    *           Show Soft Deadline Task Boolean
    * @param aShowNsvTasks
    *           Show Next Shop Visit Tasks
    * @return data set of results
    */
   private DataSet execute( InventoryKey aInventory, boolean aShowAssignedTasks, int aDayCount,
         String aWorkLocationKey, boolean aHideNonExcecutableTasks, DataTypeKey aDataType2,
         DataTypeKey aDataType3, boolean aShowSoftDeadlineTasks, boolean aShowNsvTasks ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( aInventory, "aInvNoDbId", "aInvNoId" );
      lDataSetArgument.add( "aShowAssignedTasks", aShowAssignedTasks );
      lDataSetArgument.add( "aDayCount", aDayCount );
      lDataSetArgument.add( "aWorkLocationKey", aWorkLocationKey );
      lDataSetArgument.add( "aHideNonExcecutableTasks", aHideNonExcecutableTasks );
      lDataSetArgument.add( aDataType2, "aDataTypeDbId2", "aDataTypeId2" );
      lDataSetArgument.add( aDataType3, "aDataTypeDbId3", "aDataTypeId3" );

      lDataSetArgument.addWhere( "rvw_stask.orphan_frct_bool = 0 " );
      if ( !aShowSoftDeadlineTasks )
         lDataSetArgument.addWhere( "NVL(rvw_baseline_task.soft_deadline_bool,0) = 0" );
      if ( !aShowNsvTasks )
         lDataSetArgument.addWhere( "NVL(rvw_baseline_task.nsv_bool, 0) = 0" );
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
   }


   /**
    * Generates a message to help diagnose which column is being asserted for which particular
    * expected task (within the query results).
    *
    * @param aKey
    *           column name
    * @param aResults
    *           data set of results
    *
    * @return message string
    */
   private String generateMsg( String aKey, DataSet aResults ) {
      String lEventKey = aResults.getString( EVENT_KEY );
      String lEvent = "unknown";
      if ( EVENT_FOR_INST_PART_TASK.toString().equals( lEventKey ) ) {
         lEvent = "EVENT_FOR_INST_PART_TASK";
      } else if ( EVENT_FOR_RMVD_PART_TASK.toString().equals( lEventKey ) ) {
         lEvent = "EVENT_FOR_RMVD_PART_TASK";
      } else if ( EVENT_FOR_INV_TASK.toString().equals( lEventKey ) ) {
         lEvent = "EVENT_FOR_INV_TASK";
      }

      return "Comparing [" + aKey + "] (event=" + lEvent + " , event_key=" + lEventKey + ") , ";
   }


   /**
    * Update the task scheduled due date
    *
    * @param aTask
    *           Task key
    * @param aDataTypeKey
    *           Data type key
    * @param aDate
    *           The updated date
    */
   private void updateTaskSchedDeadline( TaskKey aTask, DataTypeKey aDataTypeKey, Date aDate ) {
      EventDeadlineKey lEventDeadlineKey =
            new EventDeadlineKey( aTask.getEventKey(), aDataTypeKey );
      EvtSchedDeadTable lEvtSchedDead = EvtSchedDeadTable.findByPrimaryKey( lEventDeadlineKey );

      lEvtSchedDead.setDeadlineDate( aDate );
      lEvtSchedDead.update();
   }


   /**
    * Update the task plan by date
    *
    * @param aTaskKey
    *           Task key
    * @param aPlanByDate
    *           The updated plan by date
    */
   private void updateTaskPlanByDate( TaskKey aTaskKey, Date aPlanByDate ) {
      SchedStaskTable lSchedStaskTable = SchedStaskTable.findByPrimaryKey( aTaskKey );

      lSchedStaskTable.setPlanByDate( aPlanByDate );
      lSchedStaskTable.update();
   }


   /**
    * Sets up the map of expected results for the query.
    */
   private void setupExpectedResults() {
      iExpectedResultsMap = new HashMap<String, Map<String, String>>();

      Map<String, String> lResults;

      // expected results for first select in the union - tasks for inv and sub inv
      lResults = new HashMap<String, String>();
      lResults.put( EVENT_KEY, EVENT_FOR_INV_TASK.toString() );
      lResults.put( EVENT_SDESC, EVENT_FOR_INV_TASK_PART_DESC );
      lResults.put( EVENT_STATUS_CD, EVENT_STATUS );
      lResults.put( SCHED_PRIORITY_CD, EVENT_PRIORITY );
      lResults.put( ACTUAL_START_GDT, DATE_STR );
      lResults.put( TASK_CLASS_CD, INV_TASK_CLASS );
      lResults.put( WORK_TYPE_CD, null );
      lResults.put( REPEAT_INTERVAL, null );
      lResults.put( TASK_ORIGINATOR_CD, TASK_ORIGINATOR );
      lResults.put( TASK_PRIORITY_CD, TASK_PRIORITY );
      lResults.put( BARCODE_SDESC, INV_TASK_BARCODE );
      lResults.put( PARTS_READY_BOOL, TRUE );
      lResults.put( TOOLS_READY_BOOL, TRUE );
      lResults.put( LRP_BOOL, TRUE );
      lResults.put( ETOPS_BOOL, TRUE );
      lResults.put( WORK_LOC_KEY, LOCATION.toString() );
      lResults.put( INVENTORY_KEY, INVENTORY.toString() );
      lResults.put( INV_NO_SDESC, INV_DESC );
      lResults.put( LOC_KEY, LOCATION.toString() );
      lResults.put( USER_SUBCLASS_CD, USER_SUBCLASS_A_CHECK );
      lResults.put( DEVIATION_QT, INV_TASK_DRIVING_DEADLINE_DEVIATION );
      lResults.put( USAGE_REM_QT, INV_TASK_DRIVING_DEADLINE_USAGE_REMAINING );
      lResults.put( SCHED_DEAD_DT, DATE_STR );
      lResults.put( DOMAIN_TYPE_CD, RefDomainTypeKey.USAGE_PARM.getCd() );
      lResults.put( ENG_UNIT_CD, RefEngUnitKey.CYCLES.getCd() );
      lResults.put( PRECISION_QT, CYCLES_PRECISION );
      lResults.put( DATA_TYPE_CD, CYCLES_DATA_TYPE );
      lResults.put( ENG_UNIT_MULT_QT, CYCLES_MULTIPLIER );
      lResults.put( EXT_SCHED_DEAD_DT, DATE_STR );
      lResults.put( SORT_DUE_DT, DATE_STR );
      lResults.put( CHECK_KEY, INV_TASK_CHECK.toString() );
      lResults.put( CHECK_SDESC, INV_TASK_CHECK_DESC );
      lResults.put( CHECK_BARCODE, INV_TASK_BARCODE );
      lResults.put( WO_REF_SDESC, INV_TASK_WO_REF );
      lResults.put( PO_KEY, INV_TASK_CHECK_PO.toString() );
      lResults.put( SOFT_DEADLINE, FALSE );
      lResults.put( NSV_TASK, FALSE );
      lResults.put( DRIVING_TASK_KEY, INV_TASK_DRIVING_EVENT.toString() );
      lResults.put( DRIVING_TASK_SDESC, INV_TASK_DRIVING_EVENT_DESC );
      lResults.put( DRIVING_BARCODE_SDESC, INV_TASK_DRIVING_TASK_BARCODE );
      lResults.put( PLAN_BY_DATE, DATE_STR );
      lResults.put( CONFIG_POS_SDESC, INV_CONFIG_POS_DESC );
      lResults.put( PREVENT_EXE_BOOL, FALSE );
      lResults.put( DO_AT_NEXT_INSTALL_BOOL, FALSE );
      lResults.put( PREVENT_EXE_REVIEW_DT, null );
      lResults.put( MUST_REMOVE_USER_CD, TASK_MUST_BE_RMVD_OFF_WING );
      lResults.put( REQUEST_STATUS_DB_ID, null );
      lResults.put( REQUEST_STATUS_CD, null );
      lResults.put( PART_REQUEST_STATUS, null );
      lResults.put( PART_REQUEST_ETA, null );
      lResults.put( PART_REQUEST_KEY, EMPTY_KEY_STR );
      lResults.put( WARNING_BOOL, null );
      lResults.put( MATERIAL_AVAIL_SORT_DATE, null );
      iExpectedResultsMap.put( EVENT_FOR_INV_TASK.toString(), lResults );

      // expected results for second select in the union - install part tasks
      lResults = new HashMap<String, String>();
      lResults.put( EVENT_KEY, EVENT_FOR_INST_PART_TASK.toString() );
      lResults.put( EVENT_SDESC, EVENT_FOR_INST_PART_TASK_DESC );
      lResults.put( EVENT_STATUS_CD, EVENT_STATUS );
      lResults.put( SCHED_PRIORITY_CD, EVENT_PRIORITY );
      lResults.put( ACTUAL_START_GDT, DATE_STR );
      lResults.put( TASK_CLASS_CD, INST_TASK_CLASS );
      lResults.put( WORK_TYPE_CD, null );
      lResults.put( REPEAT_INTERVAL, null );
      lResults.put( TASK_ORIGINATOR_CD, TASK_ORIGINATOR );
      lResults.put( TASK_PRIORITY_CD, TASK_PRIORITY );
      lResults.put( BARCODE_SDESC, INST_TASK_BARCODE );
      lResults.put( PARTS_READY_BOOL, TRUE );
      lResults.put( TOOLS_READY_BOOL, TRUE );
      lResults.put( LRP_BOOL, TRUE );
      lResults.put( ETOPS_BOOL, TRUE );
      lResults.put( WORK_LOC_KEY, null );
      lResults.put( INVENTORY_KEY, ROOT_INVENTORY.toString() );
      lResults.put( INV_NO_SDESC, ROOT_INV_DESC );
      lResults.put( LOC_KEY, LOCATION.toString() );
      lResults.put( USER_SUBCLASS_CD, USER_SUBCLASS_INST );
      lResults.put( DEVIATION_QT, INST_TASK_DRIVING_DEADLINE_DEVIATION );
      lResults.put( USAGE_REM_QT, INST_TASK_DRIVING_DEADLINE_USAGE_REMAINING );
      lResults.put( SCHED_DEAD_DT, DATE_STR );
      lResults.put( DOMAIN_TYPE_CD, RefDomainTypeKey.USAGE_PARM.getCd() );
      lResults.put( ENG_UNIT_CD, RefEngUnitKey.HOUR.getCd() );
      lResults.put( PRECISION_QT, HOURS_PRECISION );
      lResults.put( DATA_TYPE_CD, HOURS_DATA_TYPE );
      lResults.put( ENG_UNIT_MULT_QT, HOURS_MULTIPLIER );
      lResults.put( EXT_SCHED_DEAD_DT, DATE_STR );
      lResults.put( SORT_DUE_DT, DATE_STR );
      lResults.put( CHECK_KEY, null );
      lResults.put( CHECK_SDESC, null );
      lResults.put( CHECK_BARCODE, null );
      lResults.put( WO_REF_SDESC, null );
      lResults.put( RO_REF_SDESC, null );
      lResults.put( PO_KEY, null );
      lResults.put( SOFT_DEADLINE, FALSE );
      lResults.put( NSV_TASK, FALSE );
      lResults.put( DRIVING_TASK_KEY, INST_TASK_DRIVING_EVENT.toString() );
      lResults.put( DRIVING_TASK_SDESC, INST_TASK_DRIVING_EVENT_DESC );
      lResults.put( DRIVING_BARCODE_SDESC, INST_TASK_DRIVING_TASK_BARCODE );
      lResults.put( PLAN_BY_DATE, DATE_STR );

      lResults.put( PREVENT_EXE_BOOL, FALSE );
      lResults.put( DO_AT_NEXT_INSTALL_BOOL, FALSE );
      lResults.put( PREVENT_EXE_REVIEW_DT, null );
      lResults.put( MUST_REMOVE_USER_CD, null );
      lResults.put( REQUEST_STATUS_DB_ID, null );
      lResults.put( REQUEST_STATUS_CD, null );
      lResults.put( PART_REQUEST_STATUS, null );
      lResults.put( PART_REQUEST_ETA, null );
      lResults.put( PART_REQUEST_KEY, EMPTY_KEY_STR );
      lResults.put( WARNING_BOOL, null );
      lResults.put( MATERIAL_AVAIL_SORT_DATE, null );
      iExpectedResultsMap.put( EVENT_FOR_INST_PART_TASK.toString(), lResults );

      // expected results for third select in the union - remove part tasks
      lResults = new HashMap<String, String>();
      lResults.put( EVENT_KEY, EVENT_FOR_RMVD_PART_TASK.toString() );
      lResults.put( EVENT_SDESC, EVENT_FOR_RMVD_PART_TASK_DESC );
      lResults.put( EVENT_STATUS_CD, EVENT_STATUS );
      lResults.put( SCHED_PRIORITY_CD, EVENT_PRIORITY );
      lResults.put( ACTUAL_START_GDT, DATE_STR );
      lResults.put( TASK_CLASS_CD, RMVD_TASK_CLASS );
      lResults.put( WORK_TYPE_CD, null );
      lResults.put( REPEAT_INTERVAL, null );
      lResults.put( TASK_ORIGINATOR_CD, TASK_ORIGINATOR );
      lResults.put( TASK_PRIORITY_CD, TASK_PRIORITY );
      lResults.put( BARCODE_SDESC, RMVD_TASK_BARCODE );
      lResults.put( PARTS_READY_BOOL, TRUE );
      lResults.put( TOOLS_READY_BOOL, TRUE );
      lResults.put( LRP_BOOL, TRUE );
      lResults.put( ETOPS_BOOL, TRUE );
      lResults.put( WORK_LOC_KEY, LOCATION.toString() );
      lResults.put( INVENTORY_KEY, ROOT_INVENTORY.toString() );
      lResults.put( INV_NO_SDESC, ROOT_INV_DESC );
      lResults.put( LOC_KEY, LOCATION.toString() );
      lResults.put( USER_SUBCLASS_CD, USER_SUBCLASS_RMVD );
      lResults.put( DEVIATION_QT, EVENT_FOR_RMVD_PART_TASK_DEVIATION );
      lResults.put( USAGE_REM_QT, EVENT_FOR_RMVD_PART_TASK_USAGE_REMAINING );
      lResults.put( SCHED_DEAD_DT, DATE_STR );
      lResults.put( DOMAIN_TYPE_CD, RefDomainTypeKey.USAGE_PARM.getCd() );
      lResults.put( ENG_UNIT_CD, RefEngUnitKey.CYCLES.getCd() );
      lResults.put( PRECISION_QT, CYCLES_PRECISION );
      lResults.put( DATA_TYPE_CD, CYCLES_DATA_TYPE );
      lResults.put( ENG_UNIT_MULT_QT, CYCLES_MULTIPLIER );
      lResults.put( EXT_SCHED_DEAD_DT, DATE_STR );
      lResults.put( SORT_DUE_DT, DATE_STR );
      lResults.put( CHECK_KEY, RMVD_TASK_CHECK.toString() );
      lResults.put( CHECK_SDESC, RMVD_TASK_CHECK_DESC );
      lResults.put( CHECK_BARCODE, RMVD_TASK_BARCODE );
      lResults.put( WO_REF_SDESC, RMVD_TASK_WO_REF );
      lResults.put( RO_REF_SDESC, RMVD_TASK_RO_REF );
      lResults.put( PO_KEY, RMVD_TASK_CHECK_PO.toString() );
      lResults.put( SOFT_DEADLINE, FALSE );
      lResults.put( NSV_TASK, FALSE );
      lResults.put( DRIVING_TASK_KEY, null );
      lResults.put( DRIVING_TASK_SDESC, null );
      lResults.put( DRIVING_BARCODE_SDESC, null );
      lResults.put( PLAN_BY_DATE, DATE_STR );
      lResults.put( PREVENT_EXE_BOOL, FALSE );
      lResults.put( DO_AT_NEXT_INSTALL_BOOL, FALSE );
      lResults.put( PREVENT_EXE_REVIEW_DT, null );
      lResults.put( MUST_REMOVE_USER_CD, TASK_MUST_BE_RMVD_OFF_WING );
      lResults.put( REQUEST_STATUS_DB_ID, null );
      lResults.put( REQUEST_STATUS_CD, null );
      lResults.put( PART_REQUEST_STATUS, null );
      lResults.put( PART_REQUEST_ETA, null );
      lResults.put( PART_REQUEST_KEY, EMPTY_KEY_STR );
      lResults.put( WARNING_BOOL, null );
      lResults.put( MATERIAL_AVAIL_SORT_DATE, null );
      iExpectedResultsMap.put( EVENT_FOR_RMVD_PART_TASK.toString(), lResults );
   }
}
