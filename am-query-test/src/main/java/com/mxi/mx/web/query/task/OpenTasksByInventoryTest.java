
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
 * Tests the OpenTasksByInventory query.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class OpenTasksByInventoryTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), OpenTasksByInventoryTest.class );
   }


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
   private static final String HOURS_DATA_TYPE = "HOUR";
   private static final String HOURS_MULTIPLIER = "0.041667";
   private static final String TASK_MUST_BE_RMVD_OFF_WING = "Off Wing";
   private static final String EVENT_STATUS = "OPEN";
   private static final String EVENT_PRIORITY = "HIGH";
   private static final String TASK_ORIGINATOR = "orgntr";
   private static final String TASK_PRIORITY = "LOW";

   // Smoke test common data
   private static final InventoryKey INVENTORY = new InventoryKey( 1, 1 );
   private static final String INV_CONFIG_POS_DESC = "inv config pos";
   private static final String INV_DESC = "inv desc";

   private static final InventoryKey ROOT_INVENTORY = new InventoryKey( 1, 2 );
   private static final String ROOT_INV_DESC = "root inv desc";

   // Smoke test data for first select in the union - tasks for inv and sub inv
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
   private static final PurchaseOrderKey INV_TASK_CHECK_PO = new PurchaseOrderKey( 10, 10 );

   // Smoke test data for second select in the union - install part tasks
   private static final EventKey EVENT_FOR_INST_PART_TASK = new EventKey( 1, 2 );
   private static final String EVENT_FOR_INST_PART_TASK_DESC = "EVENT_FOR_INST_PART_TASK";
   private static final String INST_TASK_CLASS = "inst task class";
   private static final String INST_TASK_BARCODE = "inst task barcode";
   private static final EventKey INST_TASK_DRIVING_EVENT = new EventKey( 1, 4 );
   private static final String INST_TASK_DRIVING_EVENT_DESC = "inst task driving event desc";
   private static final String INST_TASK_DRIVING_TASK_BARCODE = "inst task driving task barcode";
   private static final String INST_TASK_DRIVING_DEADLINE_DEVIATION = "789";
   private static final String INST_TASK_DRIVING_DEADLINE_USAGE_REMAINING = "741";

   // Smoke test data for third select in the union - remove part tasks
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

   private static final String SORT_DUE_DT = "sort_due_dt";
   private static final String BARCODE_SDESC = "barcode_sdesc";
   private static final InventoryKey INVENTORY_FOR_SORT = new InventoryKey( 1, 3 );

   private Map<String, Map<String, String>> iExpectedResultsMap;


   /**
    * Smoke test for the query. This test exercises the three selects within the union and returns
    * one row of data for each. The three selects retrieve a task agaisnt the inventory, an install
    * part task, and a remove part task.
    *
    * @throws Exception
    */
   @Test
   public void testThatEachSelectInUnionReturnsARow() throws Exception {

      // setup query arguments for this test
      InventoryKey lInventory = INVENTORY;
      boolean lShowAssignedTasks = true;
      int lDayCount = -1;
      String lWorkLocationKey = null;
      boolean lHideNonExcecutableTasks = false;

      // execute the query
      DataSet lResults = execute( lInventory, lShowAssignedTasks, lDayCount, lWorkLocationKey,
            lHideNonExcecutableTasks );

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
      int lDayCount = 90;
      String lWorkLocationKey = null;

      DataSet lResults = execute( INVENTORY_FOR_SORT, lShowAssignedTasks, lDayCount,
            lWorkLocationKey, lHideNonExcecutableTasks );

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
      DataSet lResults = execute( INVENTORY_FOR_SORT, lShowAssignedTasks, lDayCount,
            lWorkLocationKey, lHideNonExcecutableTasks );

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
            lHideNonExcecutableTasks );

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


   @Before
   public void setUp() throws Exception {
      setupExpectedResults();
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

      EventKey lEventKey = aResults.getKey( EventKey.class, "event_key" );

      if ( EVENT_FOR_INV_TASK.equals( lEventKey ) ) {
         lExpectedResults = iExpectedResultsMap.get( EVENT_FOR_INV_TASK.toString() );
      } else if ( EVENT_FOR_INST_PART_TASK.equals( lEventKey ) ) {
         lExpectedResults = iExpectedResultsMap.get( EVENT_FOR_INST_PART_TASK.toString() );
      } else if ( EVENT_FOR_RMVD_PART_TASK.equals( lEventKey ) ) {
         lExpectedResults = iExpectedResultsMap.get( EVENT_FOR_RMVD_PART_TASK.toString() );
      } else {
         throw new Exception( "Expected results not found; key="
               + aResults.getKey( EventKey.class, "event_key" ) );
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
    *
    * @return data set of results
    */
   private DataSet execute( InventoryKey aInventory, boolean aShowAssignedTasks, int aDayCount,
         String aWorkLocationKey, boolean aHideNonExcecutableTasks ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( aInventory, "aInvNoDbId", "aInvNoId" );
      lDataSetArgument.add( "aShowAssignedTasks", aShowAssignedTasks );
      lDataSetArgument.add( "aDayCount", aDayCount );
      lDataSetArgument.add( "aWorkLocationKey", aWorkLocationKey );
      lDataSetArgument.add( "aHideNonExcecutableTasks", aHideNonExcecutableTasks );
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
      String lEventKey = aResults.getString( "event_key" );
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
    * Sets up the map of expected results for the query.
    */
   private void setupExpectedResults() {
      iExpectedResultsMap = new HashMap<String, Map<String, String>>();

      Map<String, String> lResults;

      // expected results for first select in the union - tasks for inv and sub inv
      lResults = new HashMap<String, String>();
      lResults.put( "event_key", EVENT_FOR_INV_TASK.toString() );
      lResults.put( "event_sdesc", EVENT_FOR_INV_TASK_PART_DESC );
      lResults.put( "event_status_cd", EVENT_STATUS );
      lResults.put( "sched_priority_cd", EVENT_PRIORITY );
      lResults.put( "actual_start_gdt", DATE_STR );
      lResults.put( "task_class_cd", INV_TASK_CLASS );
      lResults.put( "work_type_cd", null );
      lResults.put( "repeat_interval", null );
      lResults.put( "task_originator_cd", TASK_ORIGINATOR );
      lResults.put( "task_priority_cd", TASK_PRIORITY );
      lResults.put( "barcode_sdesc", INV_TASK_BARCODE );
      lResults.put( "parts_ready_bool", TRUE );
      lResults.put( "tools_ready_bool", TRUE );
      lResults.put( "lrp_bool", TRUE );
      lResults.put( "etops_bool", TRUE );
      lResults.put( "work_loc_key", LOCATION.toString() );
      lResults.put( "inventory_key", INVENTORY.toString() );
      lResults.put( "inv_no_sdesc", INV_DESC );
      lResults.put( "loc_key", LOCATION.toString() );
      lResults.put( "user_subclass_cd", USER_SUBCLASS_A_CHECK );
      lResults.put( "deviation_qt", INV_TASK_DRIVING_DEADLINE_DEVIATION );
      lResults.put( "usage_rem_qt", INV_TASK_DRIVING_DEADLINE_USAGE_REMAINING );
      lResults.put( "sched_dead_dt", DATE_STR );
      lResults.put( "domain_type_cd", RefDomainTypeKey.USAGE_PARM.getCd() );
      lResults.put( "eng_unit_cd", RefEngUnitKey.CYCLES.getCd() );
      lResults.put( "precision_qt", CYCLES_PRECISION );
      lResults.put( "data_type_cd", CYCLES_DATA_TYPE );
      lResults.put( "eng_unit_mult_qt", CYCLES_MULTIPLIER );
      lResults.put( "ext_sched_dead_dt", DATE_STR );
      lResults.put( "sort_due_dt", DATE_STR );
      lResults.put( "check_key", INV_TASK_CHECK.toString() );
      lResults.put( "check_sdesc", INV_TASK_CHECK_DESC );
      lResults.put( "check_barcode", INV_TASK_BARCODE );
      lResults.put( "wo_ref_sdesc", "inv task wo ref" );
      lResults.put( "ro_ref_sdesc", "inv task ro ref" );
      lResults.put( "po_key", INV_TASK_CHECK_PO.toString() );
      lResults.put( "soft_deadline", FALSE );
      lResults.put( "nsv_task", FALSE );
      lResults.put( "driving_task_key", INV_TASK_DRIVING_EVENT.toString() );
      lResults.put( "driving_task_sdesc", INV_TASK_DRIVING_EVENT_DESC );
      lResults.put( "driving_barcode_sdesc", INV_TASK_DRIVING_TASK_BARCODE );
      lResults.put( "plan_by_date", DATE_STR );
      lResults.put( "config_pos_sdesc", INV_CONFIG_POS_DESC );
      lResults.put( "prevent_exe_bool", FALSE );
      lResults.put( "do_at_next_install_bool", FALSE );
      lResults.put( "prevent_exe_review_dt", null );
      lResults.put( "must_remove_user_cd", TASK_MUST_BE_RMVD_OFF_WING );
      lResults.put( "request_status_db_id", null );
      lResults.put( "request_status_cd", null );
      lResults.put( "part_request_status", null );
      lResults.put( "part_request_eta", null );
      lResults.put( "part_request_key", EMPTY_KEY_STR );
      lResults.put( "warning_bool", null );
      lResults.put( "material_avail_sort_date", null );
      iExpectedResultsMap.put( EVENT_FOR_INV_TASK.toString(), lResults );

      // expected results for second select in the union - install part tasks
      lResults = new HashMap<String, String>();
      lResults.put( "event_key", EVENT_FOR_INST_PART_TASK.toString() );
      lResults.put( "event_sdesc", EVENT_FOR_INST_PART_TASK_DESC );
      lResults.put( "event_status_cd", EVENT_STATUS );
      lResults.put( "sched_priority_cd", EVENT_PRIORITY );
      lResults.put( "actual_start_gdt", DATE_STR );
      lResults.put( "task_class_cd", INST_TASK_CLASS );
      lResults.put( "work_type_cd", null );
      lResults.put( "repeat_interval", null );
      lResults.put( "task_originator_cd", TASK_ORIGINATOR );
      lResults.put( "task_priority_cd", TASK_PRIORITY );
      lResults.put( "barcode_sdesc", INST_TASK_BARCODE );
      lResults.put( "parts_ready_bool", TRUE );
      lResults.put( "tools_ready_bool", TRUE );
      lResults.put( "lrp_bool", TRUE );
      lResults.put( "etops_bool", TRUE );
      lResults.put( "work_loc_key", null );
      lResults.put( "inventory_key", ROOT_INVENTORY.toString() );
      lResults.put( "inv_no_sdesc", ROOT_INV_DESC );
      lResults.put( "loc_key", LOCATION.toString() );
      lResults.put( "user_subclass_cd", USER_SUBCLASS_INST );
      lResults.put( "deviation_qt", INST_TASK_DRIVING_DEADLINE_DEVIATION );
      lResults.put( "usage_rem_qt", INST_TASK_DRIVING_DEADLINE_USAGE_REMAINING );
      lResults.put( "sched_dead_dt", DATE_STR );
      lResults.put( "domain_type_cd", RefDomainTypeKey.USAGE_PARM.getCd() );
      lResults.put( "eng_unit_cd", RefEngUnitKey.HOUR.getCd() );
      lResults.put( "precision_qt", HOURS_PRECISION );
      lResults.put( "data_type_cd", HOURS_DATA_TYPE );
      lResults.put( "eng_unit_mult_qt", HOURS_MULTIPLIER );
      lResults.put( "ext_sched_dead_dt", DATE_STR );
      lResults.put( "sort_due_dt", DATE_STR );
      lResults.put( "check_key", null );
      lResults.put( "check_sdesc", null );
      lResults.put( "check_barcode", INST_TASK_BARCODE );
      lResults.put( "wo_ref_sdesc", null );
      lResults.put( "ro_ref_sdesc", null );
      lResults.put( "po_key", null );
      lResults.put( "soft_deadline", FALSE );
      lResults.put( "nsv_task", FALSE );
      lResults.put( "driving_task_key", INST_TASK_DRIVING_EVENT.toString() );
      lResults.put( "driving_task_sdesc", INST_TASK_DRIVING_EVENT_DESC );
      lResults.put( "driving_barcode_sdesc", INST_TASK_DRIVING_TASK_BARCODE );
      lResults.put( "plan_by_date", DATE_STR );
      lResults.put( "config_pos_sdesc", INV_CONFIG_POS_DESC );
      lResults.put( "prevent_exe_bool", FALSE );
      lResults.put( "do_at_next_install_bool", FALSE );
      lResults.put( "prevent_exe_review_dt", null );
      lResults.put( "must_remove_user_cd", null );
      lResults.put( "request_status_db_id", null );
      lResults.put( "request_status_cd", null );
      lResults.put( "part_request_status", null );
      lResults.put( "part_request_eta", null );
      lResults.put( "part_request_key", EMPTY_KEY_STR );
      lResults.put( "warning_bool", null );
      lResults.put( "material_avail_sort_date", null );
      iExpectedResultsMap.put( EVENT_FOR_INST_PART_TASK.toString(), lResults );

      // expected results for third select in the union - remove part tasks
      lResults = new HashMap<String, String>();
      lResults.put( "event_key", EVENT_FOR_RMVD_PART_TASK.toString() );
      lResults.put( "event_sdesc", EVENT_FOR_RMVD_PART_TASK_DESC );
      lResults.put( "event_status_cd", EVENT_STATUS );
      lResults.put( "sched_priority_cd", EVENT_PRIORITY );
      lResults.put( "actual_start_gdt", DATE_STR );
      lResults.put( "task_class_cd", RMVD_TASK_CLASS );
      lResults.put( "work_type_cd", null );
      lResults.put( "repeat_interval", null );
      lResults.put( "task_originator_cd", TASK_ORIGINATOR );
      lResults.put( "task_priority_cd", TASK_PRIORITY );
      lResults.put( "barcode_sdesc", RMVD_TASK_BARCODE );
      lResults.put( "parts_ready_bool", TRUE );
      lResults.put( "tools_ready_bool", TRUE );
      lResults.put( "lrp_bool", TRUE );
      lResults.put( "etops_bool", TRUE );
      lResults.put( "work_loc_key", LOCATION.toString() );
      lResults.put( "inventory_key", ROOT_INVENTORY.toString() );
      lResults.put( "inv_no_sdesc", ROOT_INV_DESC );
      lResults.put( "loc_key", LOCATION.toString() );
      lResults.put( "user_subclass_cd", USER_SUBCLASS_RMVD );
      lResults.put( "deviation_qt", EVENT_FOR_RMVD_PART_TASK_DEVIATION );
      lResults.put( "usage_rem_qt", EVENT_FOR_RMVD_PART_TASK_USAGE_REMAINING );
      lResults.put( "sched_dead_dt", DATE_STR );
      lResults.put( "domain_type_cd", RefDomainTypeKey.USAGE_PARM.getCd() );
      lResults.put( "eng_unit_cd", RefEngUnitKey.CYCLES.getCd() );
      lResults.put( "precision_qt", CYCLES_PRECISION );
      lResults.put( "data_type_cd", CYCLES_DATA_TYPE );
      lResults.put( "eng_unit_mult_qt", CYCLES_MULTIPLIER );
      lResults.put( "ext_sched_dead_dt", DATE_STR );
      lResults.put( "sort_due_dt", DATE_STR );
      lResults.put( "check_key", RMVD_TASK_CHECK.toString() );
      lResults.put( "check_sdesc", RMVD_TASK_CHECK_DESC );
      lResults.put( "check_barcode", RMVD_TASK_BARCODE );
      lResults.put( "wo_ref_sdesc", RMVD_TASK_WO_REF );
      lResults.put( "ro_ref_sdesc", RMVD_TASK_RO_REF );
      lResults.put( "po_key", RMVD_TASK_CHECK_PO.toString() );
      lResults.put( "soft_deadline", FALSE );
      lResults.put( "nsv_task", FALSE );
      lResults.put( "driving_task_key", null );
      lResults.put( "driving_task_sdesc", null );
      lResults.put( "driving_barcode_sdesc", null );
      lResults.put( "plan_by_date", DATE_STR );
      lResults.put( "config_pos_sdesc", INV_CONFIG_POS_DESC );
      lResults.put( "prevent_exe_bool", FALSE );
      lResults.put( "do_at_next_install_bool", FALSE );
      lResults.put( "prevent_exe_review_dt", null );
      lResults.put( "must_remove_user_cd", TASK_MUST_BE_RMVD_OFF_WING );
      lResults.put( "request_status_db_id", null );
      lResults.put( "request_status_cd", null );
      lResults.put( "part_request_status", null );
      lResults.put( "part_request_eta", null );
      lResults.put( "part_request_key", EMPTY_KEY_STR );
      lResults.put( "warning_bool", null );
      lResults.put( "material_avail_sort_date", null );
      iExpectedResultsMap.put( EVENT_FOR_RMVD_PART_TASK.toString(), lResults );
   }
}
