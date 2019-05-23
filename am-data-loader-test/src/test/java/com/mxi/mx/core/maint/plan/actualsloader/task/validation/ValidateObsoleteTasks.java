package com.mxi.mx.core.maint.plan.actualsloader.task.validation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.util.ActualsLoaderTest;
import com.mxi.mx.util.TableUtil;


/**
 * Test Obsolete Tasks
 *
 */
public class ValidateObsoleteTasks extends ActualsLoaderTest {

   @Rule
   public TestName testName = new TestName();

   String iSerialNoOem = "'SN000016'";
   String iPartNoOem = "'A0000001'";
   String iManufactCd = "'10001'";
   String iTaskCd = "'TRK-REQ'";
   String iCompletionDt = "to_date('01/01/2015','mm/dd/yyyy')";
   String iCompletionDueDT = "to_date('01/01/2015','mm/dd/yyyy')";
   String iSchedParameter = "'HOURS'";
   String iCompletionValue = "'10'";

   // DataSetup
   private ArrayList<String> iDataSetup = new ArrayList<String>();
   {
      iDataSetup.add(
            "UPDATE task_task SET task_def_status_cd = 'OBSOLETE' WHERE task_cd = 'TRK-REQ'" );
      iDataSetup.add( "UPDATE inv_inv SET inv_cond_cd = 'INSRV' WHERE serial_no_oem = 'SN000016'" );
   };

   private ArrayList<String> iRestoreOriginalValues = new ArrayList<String>();
   {
      iRestoreOriginalValues
            .add( "UPDATE task_task SET task_def_status_cd = 'ACTV' WHERE task_cd = " + iTaskCd );
      iRestoreOriginalValues.add(
            "UPDATE inv_inv SET inv_cond_cd = 'CONDEMN' WHERE serial_no_oem = " + iSerialNoOem );
   };


   /**
    * Setup before executing each individual test
    *
    */
   @Before
   @Override
   public void before() throws Exception {
      super.before();
      clearActualsLoaderTables();
      classDataSetup( iDataSetup );
      if ( testName.getMethodName().contains( "NonRecurring" ) )
         runUpdate( "UPDATE task_task SET recurring_task_bool = 0 WHERE task_cd =" + iTaskCd );
   }


   /**
    * Clean up after each individual test
    *
    * @throws Exception
    */
   @Override
   @After
   public void after() throws Exception {
      if ( testName.getMethodName().contains( "NonRecurring" ) )
         runUpdate( "UPDATE task_task SET recurring_task_bool = 1 WHERE task_cd =" + iTaskCd );

      classDataSetup( iRestoreOriginalValues );
      super.after();
   }


   /**
    *
    * If there is a recurring First Time task, and it is obsolete, a critical error will be
    * generated: AL-TASK-007
    *
    * @throws Exception
    */
   @Test
   public void TestFirstTimeRecurringObsoleteActvTask() throws Exception {

      // create task map
      Map<String, String> lMapTaskFirstTime = new LinkedHashMap<String, String>();
      lMapTaskFirstTime.put( "SERIAL_NO_OEM", iSerialNoOem );
      lMapTaskFirstTime.put( "PART_NO_OEM", iPartNoOem );
      lMapTaskFirstTime.put( "MANUFACT_CD", iManufactCd );
      lMapTaskFirstTime.put( "TASK_CD", iTaskCd );
      lMapTaskFirstTime.put( "COMPLETION_DT", "null" );
      lMapTaskFirstTime.put( "FIRST_TIME_BOOL", "'Y'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTaskFirstTime ) );

      // call express validation runner and result checker with expected critical error code
      runValidateTasks();
      checkTaskValidation_WITHWARNING( "AL-TASK-007" );

   }


   /**
    *
    * If there is a non-recurring First Time task, and it is obsolete, a critical error will be
    * generated: AL-TASK-007
    *
    * @throws Exception
    */
   @Test
   public void TestFirstTimeNonRecurringObsoleteActvTask() throws Exception {

      // create task map
      Map<String, String> lMapTaskFirstTime = new LinkedHashMap<String, String>();
      lMapTaskFirstTime.put( "SERIAL_NO_OEM", iSerialNoOem );
      lMapTaskFirstTime.put( "PART_NO_OEM", iPartNoOem );
      lMapTaskFirstTime.put( "MANUFACT_CD", iManufactCd );
      lMapTaskFirstTime.put( "TASK_CD", iTaskCd );
      lMapTaskFirstTime.put( "COMPLETION_DT", "null" );
      lMapTaskFirstTime.put( "FIRST_TIME_BOOL", "'Y'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTaskFirstTime ) );

      // call express validation runner and result checker with critical expected error code
      validateAndCheckTask( testName.getMethodName(), "AL-TASK-007" );
      runValidateTasks();
      checkTaskValidation_WITHWARNING( "AL-TASK-007" );
   }


   /**
    *
    * If there is a non-recurring historical task, and it is obsolete, it will pass validation
    *
    * @throws Exception
    */
   @Test
   public void TestHistNonRecurringObsoleteActvTask() throws Exception {

      // create task map
      Map<String, String> lMapTaskHist = new LinkedHashMap<String, String>();
      lMapTaskHist.put( "SERIAL_NO_OEM", iSerialNoOem );
      lMapTaskHist.put( "PART_NO_OEM", iPartNoOem );
      lMapTaskHist.put( "MANUFACT_CD", iManufactCd );
      lMapTaskHist.put( "TASK_CD", iTaskCd );
      lMapTaskHist.put( "COMPLETION_DT", iCompletionDt );
      lMapTaskHist.put( "FIRST_TIME_BOOL", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTaskHist ) );

      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      lMapTaskSched.put( "SERIAL_NO_OEM", iSerialNoOem );
      lMapTaskSched.put( "PART_NO_OEM", iPartNoOem );
      lMapTaskSched.put( "MANUFACT_CD", iManufactCd );
      lMapTaskSched.put( "TASK_CD", iTaskCd );
      lMapTaskSched.put( "COMPLETION_DT", iCompletionDt );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "10" );
      lMapTaskSched.put( "COMPLETION_DUE_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // call express validation runner and result checker with no error code
      runValidateTasks();
      checkTaskValidation_EXCEPTWARNING( "PASS" );

   }


   /**
    *
    * If there is a recurring historical task, and it is obsolete, it will warning generated by
    * validation
    *
    * @throws Exception
    */
   @Test
   public void TestHistRecurringObsoleteActvTask() throws Exception {

      // create task map
      Map<String, String> lMapTaskHist = new LinkedHashMap<String, String>();
      lMapTaskHist.put( "SERIAL_NO_OEM", iSerialNoOem );
      lMapTaskHist.put( "PART_NO_OEM", iPartNoOem );
      lMapTaskHist.put( "MANUFACT_CD", iManufactCd );
      lMapTaskHist.put( "TASK_CD", iTaskCd );
      lMapTaskHist.put( "COMPLETION_DT", iCompletionDt );
      lMapTaskHist.put( "FIRST_TIME_BOOL", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTaskHist ) );

      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      lMapTaskSched.put( "SERIAL_NO_OEM", iSerialNoOem );
      lMapTaskSched.put( "PART_NO_OEM", iPartNoOem );
      lMapTaskSched.put( "MANUFACT_CD", iManufactCd );
      lMapTaskSched.put( "TASK_CD", iTaskCd );
      lMapTaskSched.put( "COMPLETION_DT", iCompletionDt );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "10" );
      lMapTaskSched.put( "COMPLETION_DUE_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // call express validation runner and result checker with expected warning error code
      runValidateTasks();
      checkTaskValidation_WITHWARNING( "AL-TASK-008" );

   }
}
