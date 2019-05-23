package com.mxi.mx.core.maint.plan.actualsloader.task.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.ActualsLoaderTest;
import com.mxi.mx.util.StringUtils;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.ValidationAndImport;
import com.mxi.mx.util.WhereClause;


/**
 * This test suite contains test cases on validation and import functionality of
 * mx_al_ctrller_pkg.execute_task_validation on task applicability.
 *
 * @author ALICIA QIAN
 */
public class ApplicableTaskTest extends ActualsLoaderTest {

   @Rule
   public TestName testName = new TestName();
   ValidationAndImport ivalidationandimport;

   public static String iTask_CD = "AL_APPLIC_RULE";
   public static String iTask_CD_2 = "AL_APPLIC";
   public static String iTask_CD_BLOCKCHAIN = "B1R CHAIN RECURRING";
   public static String iTask_CD_BLOCKCHAIN_2 = "B1N CHAIN NON-RECURRING";
   public static String iSN = "SN000001";
   public static String iPART_NO_OEM_valid = "ACFT_ASSY_PN1";
   public simpleIDs iTaskId = null;


   /**
    * Setup before executing each individual test
    *
    */
   @Before
   @Override
   public void before() throws Exception {

      super.before();
      clearActualsLoaderTables();
      String strTCName = testName.getMethodName();
      if ( strTCName.contains( "REQ_NONRECURRING" ) ) {
         dataSetup_REQ_NONRECURRING();
      } else if ( strTCName.contains( "REQ_RANGE" ) ) {
         dataSetup_REQ_RANGE();
      } else if ( strTCName.contains( "BLOCK_CHAIN_SQL_RECURRING" ) ) {
         dataSetup_BLOCK_CHAIN_SQL_RECURRING();
      } else if ( strTCName.contains( "BLOCK_CHAIN_RANGE_RECURRING" ) ) {
         dataSetup_BLOCK_CHAIN_RANGE_RECURRING();
      } else if ( strTCName.contains( "BLOCK_CHAIN_SQL_NONRECURRING" ) ) {
         dataSetup_BLOCK_CHAIN_SQL_NONRECURRING();

      } else if ( strTCName.contains( "REQ_RANGENONRECURRING" ) ) {
         dataSetup_REQ_RANGENONRECURRING();

      } else if ( strTCName.contains( "BLOCK_CHAIN_RANGENONRECURRING" ) ) {
         dataSetup_BLOCK_CHAIN_RANGENONRECURRING();

      }

   }


   /**
    * Clean up after each individual test
    */
   @After
   @Override
   public void after() {

      try {
         restoreData();
         super.after();
      } catch ( Exception e ) {
         e.printStackTrace();

      }
   }


   /**
    * This test is to verify mx_al_ctrller_pkg.execute_task_validation validation functionality of
    * on OPER-22219. when task setting as { Recur=Y, Applcbl=1, Hist=1 }, the task would pass
    * validation, ACTV task would be generated.
    *
    *
    */

   @Test
   public void test_AL_TASK_OPER_22219_1_REQ_RECURRING_VALIDATION() {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // valid record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iTask_CD + "'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // valid record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'" + iTask_CD + "'" );
      lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "0" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // validate there is no error code
      Assert.assertTrue( "There should not have error", checkTaskValidation_cap( "SN000001" ) );

   }


   /**
    * This test is to verify mx_al_ctrller_pkg.execute_task_validation validation functionality of
    * on OPER-22219. when task setting as { Recur=Y, Applcbl=1, Hist=0 }, the task would pass
    * validation, ACTV task would be generated.
    *
    *
    */

   @Test
   public void test_AL_TASK_OPER_22219_2_REQ_RECURRING_VALIDATION() {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // valid record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iTask_CD + "'" );
      lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // valid record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'" + iTask_CD + "'" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // validate only invalid record got error code
      Assert.assertTrue( "There should not be an error in the table.",
            checkTaskValidation_cap( "SN000001" ) );

   }


   /**
    * This test is to verify mx_al_ctrller_pkg.execute_task_validation validation functionality of
    * on OPER-22219. when task setting as { Recur=Y, Applcbl=0(range=0), Hist=1 }, the task would
    * pass validation with WARNING - 'AL-TASK-001'
    *
    *
    */

   @Test
   public void test_AL_TASK_OPER_22219_3a_REQ_RANGE_RECURRING_VALIDATION() {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // Invalid record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000015'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapTask.put( "MANUFACT_CD", "'11111'" );
      lMapTask.put( "TASK_CD", "'" + iTask_CD_2 + "'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "COMPLETION_DUE_DT", "null" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // Invalid record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000015'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapTaskSched.put( "MANUFACT_CD", "'11111'" );
      lMapTaskSched.put( "TASK_CD", "'" + iTask_CD_2 + "'" );
      lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "0" );
      lMapTaskSched.put( "COMPLETION_DUE_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // validate only invalid record got error code
      Assert.assertFalse( "There should be an error in the table.",
            checkTaskValidation_cap( "SN000015" ) );

      // check warning code AL-TASK-001
      checkTaskValidation_WITHWARNING( "AL-TASK-001" );

   }


   /**
    * This test is to verify mx_al_ctrller_pkg.execute_task_validation validation functionality of
    * on OPER-22219. when task setting as { Recur=Y, Applcbl=0 (rule=0), Hist=1 }, the task would
    * pass validation with WARNING - 'AL-TASK-002'
    *
    *
    */

   @Test
   public void test_AL_TASK_OPER_22219_3b_RULE_REQ_RECURRING_VALIDATION() {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // Invalid record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000015'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapTask.put( "MANUFACT_CD", "'11111'" );
      lMapTask.put( "TASK_CD", "'" + iTask_CD + "'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "COMPLETION_DUE_DT", "null" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // Invalid record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000015'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapTaskSched.put( "MANUFACT_CD", "'11111'" );
      lMapTaskSched.put( "TASK_CD", "'" + iTask_CD + "'" );
      lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "0" );
      lMapTaskSched.put( "COMPLETION_DUE_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // validate only invalid record got error code
      Assert.assertFalse( "There should be an error in the table.",
            checkTaskValidation_cap( "SN000015" ) );

      // check warning code AL-TASK-002
      checkTaskValidation_WITHWARNING( "AL-TASK-002" );

   }


   /**
    * This test is to verify mx_al_ctrller_pkg.execute_task_validation validation functionality of
    * on OPER-22219. when task setting as { Recur=Y, Applcbl=0, Hist=0 }, the task would not pass
    * validation, there would be an error AL-TASK-006 is generated.
    *
    *
    */

   @Test
   public void test_AL_TASK_OPER_22219_4a_REQ_RECURRING_VALIDATION() {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // Invalid record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000015'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapTask.put( "MANUFACT_CD", "'11111'" );
      lMapTask.put( "TASK_CD", "'" + iTask_CD + "'" );
      // lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "COMPLETION_DUE_DT", "null" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // Invalid record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000015'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapTaskSched.put( "MANUFACT_CD", "'11111'" );
      lMapTaskSched.put( "TASK_CD", "'" + iTask_CD + "'" );
      // lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      // lMapTaskSched.put( "COMPLETION_VALUE", "0" );
      lMapTaskSched.put( "COMPLETION_DUE_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // validate only invalid record got error code
      Assert.assertFalse( "There should be an error in the table.",
            checkTaskValidation_cap( "SN000015" ) );

      // check error code will be generated AL-TASK-006.
      checkTaskValidation_WITHWARNING( "AL-TASK-006" );

   }


   /**
    * This test is to verify mx_al_ctrller_pkg.execute_task_validation validation functionality of
    * on OPER-22219. when task setting as { Recur=Y, Applcbl=0, Hist=0 }, the task would not pass
    * validation, there would be an error AL-TASK-005 is generated.
    *
    *
    */

   @Test
   public void test_AL_TASK_OPER_22219_4b_REQ_RANGE_RECURRING_VALIDATION() {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // Invalid record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000015'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapTask.put( "MANUFACT_CD", "'11111'" );
      lMapTask.put( "TASK_CD", "'" + iTask_CD_2 + "'" );
      // lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "COMPLETION_DUE_DT", "null" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // Invalid record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000015'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapTaskSched.put( "MANUFACT_CD", "'11111'" );
      lMapTaskSched.put( "TASK_CD", "'" + iTask_CD_2 + "'" );
      // lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      // lMapTaskSched.put( "COMPLETION_VALUE", "0" );
      lMapTaskSched.put( "COMPLETION_DUE_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // validate only invalid record got error code
      Assert.assertFalse( "There should be an error in the table.",
            checkTaskValidation_cap( "SN000015" ) );

      // check error code will be generated AL-TASK-005.
      checkTaskValidation_WITHWARNING( "AL-TASK-005" );

   }


   /**
    * This test is to verify mx_al_ctrller_pkg.execute_task_validation validation functionality of
    * on OPER-22219. when task setting as { Recur=N, Applcbl=1, Hist=1 }, the task would pass
    * validation, ACTV task would be generated.
    *
    *
    */

   @Test
   public void test_AL_TASK_OPER_22219_5_REQ_NONRECURRING_VALIDATION() {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // valid record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iTask_CD + "'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // valid record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'" + iTask_CD + "'" );
      lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "0" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // validate there is no error code
      Assert.assertTrue( "There should not have error/warning",
            checkTaskValidation_cap( "SN000001" ) );

   }


   /**
    * This test is to verify mx_al_ctrller_pkg.execute_task_validation validation functionality of
    * on OPER-22219. when task setting as { Recur=N, Applcbl=1, Hist=0 }, the task would pass
    * validation, ACTV task would be generated.
    *
    *
    */

   @Test
   public void test_AL_TASK_OPER_22219_6_REQ_NONRECURRING_VALIDATION() {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // valid record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iTask_CD + "'" );
      lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // valid record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'" + iTask_CD + "'" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // validate only invalid record got error code
      Assert.assertTrue( "There should not be an error/warning in the table.",
            checkTaskValidation_cap( "SN000001" ) );

   }


   /**
    * This test is to verify mx_al_ctrller_pkg.execute_task_validation validation functionality of
    * on OPER-22219. when task setting as { Recur=N, Applcbl=0, Hist=1 }, the task would pass
    * validation without any error/warning.
    *
    *
    */

   @Test
   public void test_AL_TASK_OPER_22219_7_REQ_NONRECURRING_VALIDATION() {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // Invalid record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000015'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapTask.put( "MANUFACT_CD", "'11111'" );
      lMapTask.put( "TASK_CD", "'" + iTask_CD + "'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "COMPLETION_DUE_DT", "null" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // Invalid record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000015'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapTaskSched.put( "MANUFACT_CD", "'11111'" );
      lMapTaskSched.put( "TASK_CD", "'" + iTask_CD + "'" );
      lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "0" );
      lMapTaskSched.put( "COMPLETION_DUE_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // validate only invalid record got error code
      Assert.assertTrue( "There should be no error/warning in the table.",
            checkTaskValidation_cap( "SN000015" ) );

   }


   /**
    * This test is to verify mx_al_ctrller_pkg.execute_task_validation validation functionality of
    * on OPER-22219. when task setting as { Recur=N, Applcbl=0, Hist=0 }, the task would not pass
    * validation, there would be an error 'AL-TASK-006' is generated.
    *
    *
    */

   @Test
   public void test_AL_TASK_OPER_22219_8a_REQ_NONRECURRING_VALIDATION() {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // Invalid record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000015'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapTask.put( "MANUFACT_CD", "'11111'" );
      lMapTask.put( "TASK_CD", "'" + iTask_CD + "'" );
      // lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "COMPLETION_DUE_DT", "null" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // Invalid record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000015'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapTaskSched.put( "MANUFACT_CD", "'11111'" );
      lMapTaskSched.put( "TASK_CD", "'" + iTask_CD + "'" );
      // lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      // lMapTaskSched.put( "COMPLETION_VALUE", "0" );
      lMapTaskSched.put( "COMPLETION_DUE_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // validate only invalid record got error code
      Assert.assertFalse( "There should be an error in the table.",
            checkTaskValidation_cap( "SN000015" ) );

      // check error code will be generated AL-TASK-006.
      checkTaskValidation_WITHWARNING( "AL-TASK-006" );

   }


   /**
    * This test is to verify mx_al_ctrller_pkg.execute_task_validation validation functionality of
    * on OPER-22219. when task setting as { Recur=N, Applcbl=0, Hist=0 }, the task would not pass
    * validation, there would be an error 'AL-TASK-005' is generated.
    *
    *
    */

   @Test
   public void test_AL_TASK_OPER_22219_8b_REQ_RANGENONRECURRING_VALIDATION() {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // Invalid record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000015'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapTask.put( "MANUFACT_CD", "'11111'" );
      lMapTask.put( "TASK_CD", "'" + iTask_CD_2 + "'" );
      // lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "COMPLETION_DUE_DT", "null" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // Invalid record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000015'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapTaskSched.put( "MANUFACT_CD", "'11111'" );
      lMapTaskSched.put( "TASK_CD", "'" + iTask_CD_2 + "'" );
      // lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      // lMapTaskSched.put( "COMPLETION_VALUE", "0" );
      lMapTaskSched.put( "COMPLETION_DUE_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // validate only invalid record got error code
      Assert.assertFalse( "There should be an error in the table.",
            checkTaskValidation_cap( "SN000015" ) );

      // check error code will be generated AL-TASK-005
      checkTaskValidation_WITHWARNING( "AL-TASK-005" );

   }


   /**
    * This test is to verify mx_al_ctrller_pkg.execute_task_validation validation functionality of
    * on OPER-22219. when task setting as { Recur=Y, Applcbl=1, Hist=1 }, the task would pass
    * validation, ACTV task would be generated.
    *
    *
    */

   @Test
   public void test_AL_TASK_OPER_22219_1_BLOCK_CHAIN_SQL_RECURRING_VALIDATION() {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // valid record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iTask_CD_BLOCKCHAIN + "'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // valid record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'" + iTask_CD_BLOCKCHAIN + "'" );
      lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "0" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // validate there is no error code
      Assert.assertTrue( "There should not have error", checkTaskValidation_cap( "SN000001" ) );

   }


   /**
    * This test is to verify mx_al_ctrller_pkg.execute_task_validation validation functionality of
    * on OPER-22219. when task setting as { Recur=Y, Applcbl=1, Hist=0 }, the task would pass
    * validation, ACTV task would be generated.
    *
    *
    */

   @Test
   public void test_AL_TASK_OPER_22219_2_BLOCK_CHAIN_SQL_RECURRING__VALIDATION() {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // valid record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iTask_CD_BLOCKCHAIN + "'" );
      lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // valid record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'" + iTask_CD_BLOCKCHAIN + "'" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // validate only invalid record got error code
      Assert.assertTrue( "There should not be an error in the table.",
            checkTaskValidation_cap( "SN000001" ) );

   }


   /**
    * This test is to verify mx_al_ctrller_pkg.execute_task_validation validation functionality of
    * on OPER-22219. when task setting as { Recur=Y, Applcbl=0(range=0), Hist=1 }, the task would
    * pass validation with WARNING - 'AL-TASK-001' there is a bug
    *
    */

   @Test
   public void test_AL_TASK_OPER_22219_3a_BLOCK_CHAIN_RANGE_RECURRING__VALIDATION() {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // Invalid record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000015'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapTask.put( "MANUFACT_CD", "'11111'" );
      lMapTask.put( "TASK_CD", "'" + iTask_CD_BLOCKCHAIN + "'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "COMPLETION_DUE_DT", "null" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // Invalid record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000015'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapTaskSched.put( "MANUFACT_CD", "'11111'" );
      lMapTaskSched.put( "TASK_CD", "'" + iTask_CD_BLOCKCHAIN + "'" );
      lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "0" );
      lMapTaskSched.put( "COMPLETION_DUE_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // validate only invalid record got error code
      Assert.assertFalse( "There should be an error in the table.",
            checkTaskValidation_cap( "SN000015" ) );

      // check warning code AL-TASK-001
      checkTaskValidation_WITHWARNING( "AL-TASK-001" );

   }


   /**
    * This test is to verify mx_al_ctrller_pkg.execute_task_validation validation functionality of
    * on OPER-22219. when task setting as { Recur=Y, Applcbl=0 (rule=0), Hist=1 }, the task would
    * pass validation with WARNING - 'AL-TASK-002'
    *
    *
    */

   @Test
   public void test_AL_TASK_OPER_22219_3b_BLOCK_CHAIN_SQL_RECURRING__VALIDATION() {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // Invalid record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000015'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapTask.put( "MANUFACT_CD", "'11111'" );
      lMapTask.put( "TASK_CD", "'" + iTask_CD_BLOCKCHAIN + "'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "COMPLETION_DUE_DT", "null" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // Invalid record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000015'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapTaskSched.put( "MANUFACT_CD", "'11111'" );
      lMapTaskSched.put( "TASK_CD", "'" + iTask_CD_BLOCKCHAIN + "'" );
      lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "0" );
      lMapTaskSched.put( "COMPLETION_DUE_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // validate only invalid record got error code
      Assert.assertFalse( "There should be an error in the table.",
            checkTaskValidation_cap( "SN000015" ) );

      // check warning code AL-TASK-002
      checkTaskValidation_WITHWARNING( "AL-TASK-002" );

   }


   /**
    * This test is to verify mx_al_ctrller_pkg.execute_task_validation validation functionality of
    * on OPER-22219. when task setting as { Recur=Y, Applcbl=0, Hist=0 }, the task would not pass
    * validation, there would be an error AL-TASK-006 is generated.
    *
    *
    */

   @Test
   public void test_AL_TASK_OPER_22219_4a_BLOCK_CHAIN_SQL_RECURRING_VALIDATION() {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // Invalid record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000015'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapTask.put( "MANUFACT_CD", "'11111'" );
      lMapTask.put( "TASK_CD", "'" + iTask_CD_BLOCKCHAIN + "'" );
      // lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "COMPLETION_DUE_DT", "null" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // Invalid record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000015'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapTaskSched.put( "MANUFACT_CD", "'11111'" );
      lMapTaskSched.put( "TASK_CD", "'" + iTask_CD_BLOCKCHAIN + "'" );
      // lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      // lMapTaskSched.put( "COMPLETION_VALUE", "0" );
      lMapTaskSched.put( "COMPLETION_DUE_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // validate only invalid record got error code
      Assert.assertFalse( "There should be an error in the table.",
            checkTaskValidation_cap( "SN000015" ) );

      // check error code will be generated AL-TASK-006.
      checkTaskValidation_WITHWARNING( "AL-TASK-006" );

   }


   /**
    * This test is to verify mx_al_ctrller_pkg.execute_task_validation validation functionality of
    * on OPER-22219. when task setting as { Recur=Y, Applcbl=0, Hist=0 }, the task would not pass
    * validation, there would be an error AL-TASK-005 is generated.
    *
    *
    */

   @Test
   public void test_AL_TASK_OPER_22219_4b_BLOCK_CHAIN_RANGE_RECURRING_VALIDATION() {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // Invalid record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000015'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapTask.put( "MANUFACT_CD", "'11111'" );
      lMapTask.put( "TASK_CD", "'" + iTask_CD_BLOCKCHAIN + "'" );
      // lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "COMPLETION_DUE_DT", "null" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // Invalid record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000015'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapTaskSched.put( "MANUFACT_CD", "'11111'" );
      lMapTaskSched.put( "TASK_CD", "'" + iTask_CD_BLOCKCHAIN + "'" );
      // lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      // lMapTaskSched.put( "COMPLETION_VALUE", "0" );
      lMapTaskSched.put( "COMPLETION_DUE_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // validate only invalid record got error code
      Assert.assertFalse( "There should be an error in the table.",
            checkTaskValidation_cap( "SN000015" ) );

      // check error code will be generated AL-TASK-005.
      checkTaskValidation_WITHWARNING( "AL-TASK-005" );

   }


   /**
    * This test is to verify mx_al_ctrller_pkg.execute_task_validation validation functionality of
    * on OPER-22219. when task setting as { Recur=N, Applcbl=1, Hist=1 }, the task would pass
    * validation, ACTV task would be generated.
    *
    *
    */

   @Test
   public void test_AL_TASK_OPER_22219_5_BLOCK_CHAIN_SQL_NONRECURRING_VALIDATION() {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // valid record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iTask_CD_BLOCKCHAIN_2 + "'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // valid record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'" + iTask_CD_BLOCKCHAIN_2 + "'" );
      lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "0" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // validate there is no error code
      Assert.assertTrue( "There should not have error", checkTaskValidation_cap( "SN000001" ) );

   }


   /**
    * This test is to verify mx_al_ctrller_pkg.execute_task_validation validation functionality of
    * on OPER-22219. when task setting as { Recur=N, Applcbl=1, Hist=0 }, the task would pass
    * validation, ACTV task would be generated.
    *
    *
    */

   @Test
   public void test_AL_TASK_OPER_22219_6_BLOCK_CHAIN_SQL_NONRECURRING_VALIDATION() {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // valid record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iTask_CD_BLOCKCHAIN_2 + "'" );
      lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // valid record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'" + iTask_CD_BLOCKCHAIN_2 + "'" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // validate only invalid record got error code
      Assert.assertTrue( "There should not be an error in the table.",
            checkTaskValidation_cap( "SN000001" ) );

   }


   /**
    * This test is to verify mx_al_ctrller_pkg.execute_task_validation validation functionality of
    * on OPER-22219. when task setting as { Recur=N, Applcbl=0, Hist=1 }, the task would pass
    * validation without any error/warning.
    *
    *
    */

   @Test
   public void test_AL_TASK_OPER_22219_7_BLOCK_CHAIN_SQL_NONRECURRING_VALIDATION() {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // Invalid record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000015'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapTask.put( "MANUFACT_CD", "'11111'" );
      lMapTask.put( "TASK_CD", "'" + iTask_CD_BLOCKCHAIN_2 + "'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "COMPLETION_DUE_DT", "null" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // Invalid record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000015'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapTaskSched.put( "MANUFACT_CD", "'11111'" );
      lMapTaskSched.put( "TASK_CD", "'" + iTask_CD_BLOCKCHAIN_2 + "'" );
      lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "0" );
      lMapTaskSched.put( "COMPLETION_DUE_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // validate only invalid record got error code
      Assert.assertTrue( "There should be an error in the table.",
            checkTaskValidation_cap( "SN000015" ) );

   }


   /**
    * This test is to verify mx_al_ctrller_pkg.execute_task_validation validation functionality of
    * on OPER-22219. when task setting as { Recur=N, Applcbl=0, Hist=0 }, the task would not pass
    * validation, there would be an error AL-TASK-006 is generated.
    *
    *
    */

   @Test
   public void test_AL_TASK_OPER_22219_8a_BLOCK_CHAIN_SQL_NONRECURRING_VALIDATION() {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // Invalid record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000015'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapTask.put( "MANUFACT_CD", "'11111'" );
      lMapTask.put( "TASK_CD", "'" + iTask_CD_BLOCKCHAIN_2 + "'" );
      // lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "COMPLETION_DUE_DT", "null" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // Invalid record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000015'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapTaskSched.put( "MANUFACT_CD", "'11111'" );
      lMapTaskSched.put( "TASK_CD", "'" + iTask_CD_BLOCKCHAIN_2 + "'" );
      // lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      // lMapTaskSched.put( "COMPLETION_VALUE", "0" );
      lMapTaskSched.put( "COMPLETION_DUE_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // validate only invalid record got error code
      Assert.assertFalse( "There should be an error in the table.",
            checkTaskValidation_cap( "SN000015" ) );

      // check error code will be generated AL-TASK-006.
      checkTaskValidation_WITHWARNING( "AL-TASK-006" );

   }


   /**
    * This test is to verify mx_al_ctrller_pkg.execute_task_validation validation functionality of
    * on OPER-22219. when task setting as { Recur=N, Applcbl=0, Hist=0 }, the task would not pass
    * validation, there would be an error 'AL-TASK-005' is generated.
    *
    *
    */

   @Test
   public void test_AL_TASK_OPER_22219_8b_BLOCK_CHAIN_RANGENONRECURRING_VALIDATION() {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // Invalid record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000015'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapTask.put( "MANUFACT_CD", "'11111'" );
      lMapTask.put( "TASK_CD", "'" + iTask_CD_BLOCKCHAIN_2 + "'" );
      // lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "COMPLETION_DUE_DT", "null" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // Invalid record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000015'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapTaskSched.put( "MANUFACT_CD", "'11111'" );
      lMapTaskSched.put( "TASK_CD", "'" + iTask_CD_BLOCKCHAIN_2 + "'" );
      // lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      // lMapTaskSched.put( "COMPLETION_VALUE", "0" );
      lMapTaskSched.put( "COMPLETION_DUE_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // validate only invalid record got error code
      Assert.assertFalse( "There should be an error in the table.",
            checkTaskValidation_cap( "SN000015" ) );

      // check error code will be generated AL-TASK-005.
      checkTaskValidation_WITHWARNING( "AL-TASK-005" );

   }


   // =================================================================================

   /**
    * This function is to check whether task get error by giving serial_no_oem
    *
    *
    *
    */
   @Override
   public boolean checkTaskValidation_cap( String aSN ) {
      String lrecordId = getRecordID( aSN );
      String lquery =
            "Select ERROR_CD from al_proc_tasks_error where record_id='" + lrecordId + "'";
      String lresult = getStringValueFromQuery( lquery, "ERROR_CD" );

      return StringUtils.isBlank( lresult );

   }


   /**
    * This function is to retrieve record id from AL_PROC_HIST_TASK
    *
    *
    *
    */
   @Override
   public String getRecordID( String aSN_OEM ) {
      // MIM_DATA_TYPE table
      String[] iIds = { "RECORD_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SERIAL_NO_OEM", aSN_OEM );

      String iQueryString =
            TableUtil.buildTableQuery( TableUtil.AL_PROC_HIST_TASK, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      String lId = llists.get( 0 ).get( 0 );

      return lId;
   }


   /**
    * This function is to prepare data for non-recurring task test cases
    *
    *
    *
    */
   public void dataSetup_REQ_NONRECURRING() {
      String lQuery = "update task_task set recurring_task_bool=0 where task_cd='" + iTask_CD + "'";
      executeSQL( lQuery );
   }


   /**
    * This function is to prepare data for rang recurring task test cases
    *
    *
    *
    */
   public void dataSetup_REQ_RANGE() {
      String lQuery =
            "update task_task set TASK_APPL_EFF_LDESC='001-003',TASK_APPL_LDESC='001-003' where task_cd='"
                  + iTask_CD_2 + "'";
      executeSQL( lQuery );
   }


   /**
    * This function is to prepare data for sql recurring task test cases
    *
    *
    *
    */
   public void dataSetup_BLOCK_CHAIN_SQL_RECURRING() {
      String lQuery =
            "update task_task set TASK_APPL_SQL_LDESC='rootpart.part_no_oem = ''ACFT_ASSY_PN1''' where task_cd='"
                  + iTask_CD_BLOCKCHAIN + "'";
      executeSQL( lQuery );

   }


   /**
    * This function is to prepare data for block chain rang recurring task test cases
    *
    *
    *
    */

   public void dataSetup_BLOCK_CHAIN_RANGE_RECURRING() {
      String lQuery =
            "update task_task set TASK_APPL_EFF_LDESC='001-003',TASK_APPL_LDESC='001-003' where task_cd='"
                  + iTask_CD_BLOCKCHAIN + "'";
      executeSQL( lQuery );

   }


   /**
    * This function is to prepare data for block chain rang sql non-recurring task test cases
    *
    *
    *
    */
   public void dataSetup_BLOCK_CHAIN_SQL_NONRECURRING() {
      String lQuery =
            "update task_task set TASK_APPL_SQL_LDESC='rootpart.part_no_oem = ''ACFT_ASSY_PN1''' where task_cd='"
                  + iTask_CD_BLOCKCHAIN_2 + "'";
      executeSQL( lQuery );

   }


   /**
    * This function is to prepare data for REQ rang non-recurring task test cases
    *
    *
    *
    */

   public void dataSetup_REQ_RANGENONRECURRING() {

      String lQuery =
            "update task_task set recurring_task_bool=0 where task_cd='" + iTask_CD_2 + "'";
      executeSQL( lQuery );

      lQuery =
            "update task_task set TASK_APPL_EFF_LDESC='001-003',TASK_APPL_LDESC='001-003' where task_cd='"
                  + iTask_CD_2 + "'";
      executeSQL( lQuery );

   }


   /**
    * This function is to prepare data for block chain rang non-recurring task test cases
    *
    *
    *
    */
   public void dataSetup_BLOCK_CHAIN_RANGENONRECURRING() {
      String lQuery =
            "update task_task set TASK_APPL_EFF_LDESC='001-003',TASK_APPL_LDESC='001-003' where task_cd='"
                  + iTask_CD_BLOCKCHAIN_2 + "'";
      executeSQL( lQuery );

   }


   /**
    * This function is to restore data for non-recurring task test cases
    *
    *
    *
    */
   public void restoreData() {
      String lQuery = "update task_task set recurring_task_bool=1 where task_cd='" + iTask_CD + "'";
      executeSQL( lQuery );

      lQuery = "update task_task set recurring_task_bool=1 where task_cd='" + iTask_CD_2 + "'";
      executeSQL( lQuery );

      lQuery =
            "update task_task set TASK_APPL_EFF_LDESC='001-010',TASK_APPL_LDESC='001-010' where task_cd='"
                  + iTask_CD_2 + "'";
      executeSQL( lQuery );

      lQuery = "update task_task set TASK_APPL_SQL_LDESC= NULL where task_cd='"
            + iTask_CD_BLOCKCHAIN + "'";
      executeSQL( lQuery );

      lQuery = "update task_task set TASK_APPL_EFF_LDESC= NULL,TASK_APPL_LDESC=NULL where task_cd='"
            + iTask_CD_BLOCKCHAIN + "'";
      executeSQL( lQuery );

      lQuery = "update task_task set TASK_APPL_SQL_LDESC= NULL where task_cd='"
            + iTask_CD_BLOCKCHAIN_2 + "'";
      executeSQL( lQuery );

      lQuery = "update task_task set TASK_APPL_EFF_LDESC=NULL,TASK_APPL_LDESC= NULL where task_cd='"
            + iTask_CD_BLOCKCHAIN_2 + "'";
      executeSQL( lQuery );
   }

}
