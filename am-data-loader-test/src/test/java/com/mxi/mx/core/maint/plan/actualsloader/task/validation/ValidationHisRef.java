package com.mxi.mx.core.maint.plan.actualsloader.task.validation;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.util.ActualsLoaderTest;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.ValidationAndImport;


/**
 * This test suite contains unit tests for Actuals Loader task validations for historical reference
 * doc
 *
 * @author Alicia Qian
 */
public class ValidationHisRef extends ActualsLoaderTest {

   @Rule
   public TestName testName = new TestName();
   ValidationAndImport ivalidationandimport;

   public static String iREF_Task_CD = "ATREFT1";
   public static String iREF_Task_NAME = "ATREFT1NAME";
   public static String iREF_Task_CD_2 = "ATREFT2";
   public static String iREF_Task_NAME_2 = "ATREFT2NAME";

   public static String iSN = "SN000001";

   public static String iSTAGE_NOTE_SYSTEM = "Created by BULK data loading utility. ";


   /**
    * Clean up after each individual test
    *
    * @throws Exception
    */
   @Override
   @After
   public void after() throws Exception {

      super.after();
   }


   /**
    * Setup before executing each individual test
    *
    * @throws Exception
    */
   @Override
   @Before
   public void before() throws Exception {

      super.before();
      clearActualsLoaderTables();

   }


   /**
    * This test error code AL-TASK-125:If the CLASS_MODE_CD is REF then either COMPLETION_DT is
    * populated OR NOT_APPLICABLE_BOOL is populated to true
    *
    *
    * @throws Exception
    *
    *
    */
   @Test
   public void test_AL_TASK_125() throws Exception {

      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iREF_Task_CD + "'" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      // lMapTask.put( "NOT_APPLICABLE_BOOL", "'Y'" );
      // lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_EXCEPTWARNING( "AL-TASK-125" );
      // validateAndCheckTask( testName.getMethodName(), "AL-TASK-125" );
   }


   /**
    * This test error code AL-TASK-127:If CLASS_MODE_CD is REF and COMPLETION_DT is specified then
    * task must not be recurring.
    *
    *
    *
    * @throws Exception
    *
    *
    */
   @Test
   public void test_AL_TASK_127() throws Exception {

      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iREF_Task_CD_2 + "'" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "NOT_APPLICABLE_BOOL", "'Y'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_EXCEPTWARNING( "AL-TASK-127" );

   }


   /**
    * This test error code AL-TASK-125:If CLASS_MODE_CD is REF and NOT_APPLICABLE_BOOL is true then
    * COMPLETION_DT must not be specified
    *
    *
    *
    * @throws Exception
    *
    *
    */
   @Test
   public void test_AL_TASK_126_1() throws Exception {

      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iREF_Task_CD + "'" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "NOT_APPLICABLE_BOOL", "'Y'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_EXCEPTWARNING( "AL-TASK-126" );

   }


   /**
    * This test error code AL-TASK-129:FIRST_TIME_BOOL,CUSTOM_START_DT, CUSTOM_DUE_DT, TERMINATE_DT,
    * TERMINATE_NOTE does not apply for historic reference document.
    *
    *
    *
    * @throws Exception
    *
    *
    */
   @Test
   public void test_AL_TASK_129_1() throws Exception {

      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iREF_Task_CD_2 + "'" );
      lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "NOT_APPLICABLE_BOOL", "'Y'" );
      // lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_WITHWARNING( "AL-TASK-129" );

   }


   /**
    * This test error code AL-TASK-129:FIRST_TIME_BOOL,CUSTOM_START_DT, CUSTOM_DUE_DT, TERMINATE_DT,
    * TERMINATE_NOTE does not apply for historic reference document.
    *
    *
    *
    * @throws Exception
    *
    *
    */
   @Test
   public void test_AL_TASK_129_2() throws Exception {

      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iREF_Task_CD_2 + "'" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "NOT_APPLICABLE_BOOL", "'Y'" );
      lMapTask.put( "CUSTOM_START_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_WITHWARNING( "AL-TASK-129" );

   }


   /**
    * This test error code AL-TASK-129:FIRST_TIME_BOOL,CUSTOM_START_DT, CUSTOM_DUE_DT, TERMINATE_DT,
    * TERMINATE_NOTE does not apply for historic reference document.
    *
    *
    *
    * @throws Exception
    *
    *
    */
   @Test
   public void test_AL_TASK_129_3() throws Exception {

      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iREF_Task_CD_2 + "'" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "NOT_APPLICABLE_BOOL", "'Y'" );
      lMapTask.put( "CUSTOM_DUE_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_WITHWARNING( "AL-TASK-129" );

   }


   /**
    * This test error code AL-TASK-129:FIRST_TIME_BOOL,CUSTOM_START_DT, CUSTOM_DUE_DT, TERMINATE_DT,
    * TERMINATE_NOTE does not apply for historic reference document.
    *
    *
    *
    * @throws Exception
    *
    *
    */
   @Test
   public void test_AL_TASK_129_4() throws Exception {

      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iREF_Task_CD_2 + "'" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "NOT_APPLICABLE_BOOL", "'Y'" );
      lMapTask.put( "TERMINATED_DT", "to_date('2017/01/01','yyyy/mm/dd')" );
      lMapTask.put( "TERMINATED_NOTE", "'AUTO TEST'" );
      // lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_WITHWARNING( "AL-TASK-129" );

   }


   /**
    * This test error code AL-TASK-128:COMPLETION_DUE_DT cannot be specified if COMPLETION_DT is not
    * specified.
    *
    *
    *
    * @throws Exception
    *
    *
    */
   @Test
   public void test_AL_TASK_128() throws Exception {

      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iREF_Task_CD + "'" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "NOT_APPLICABLE_BOOL", "'Y'" );
      // lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_EXCEPTWARNING( "AL-TASK-128" );
      // validateAndCheckTask( testName.getMethodName(), "AL-TASK-125" );
   }


   /**
    * This test error code AL-TASK-131:CUSTOM_START_VALUE, CUSTOM_DUE_VALUE,TERMINATED_VALUE don’t
    * apply for reference document.
    *
    *
    *
    * @throws Exception
    *
    *
    */
   @Test
   public void test_AL_TASK_131_1() throws Exception {

      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iREF_Task_CD + "'" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "NOT_APPLICABLE_BOOL", "'Y'" );
      // lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // first completed record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'" + iREF_Task_CD + "'" );
      // lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "95" );
      lMapTaskSched.put( "COMPLETION_DUE_VALUE", "90" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "20" );
      // lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );
      // lMapTaskSched.put( "TERMINATED_VALUE", "99" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_EXCEPTWARNING( "AL-TASK-131" );
      // validateAndCheckTask( testName.getMethodName(), "AL-TASK-125" );
   }


   /**
    * This test error code AL-TASK-131:CUSTOM_START_VALUE, CUSTOM_DUE_VALUE,TERMINATED_VALUE don’t
    * apply for reference document.
    *
    *
    *
    * @throws Exception
    *
    *
    */
   @Test
   public void test_AL_TASK_131_2() throws Exception {

      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iREF_Task_CD + "'" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "NOT_APPLICABLE_BOOL", "'Y'" );
      // lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // first completed record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'" + iREF_Task_CD + "'" );
      // lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "95" );
      lMapTaskSched.put( "COMPLETION_DUE_VALUE", "90" );
      // lMapTaskSched.put( "CUSTOM_START_VALUE", "20" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "20" );
      // lMapTaskSched.put( "TERMINATED_VALUE", "99" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_EXCEPTWARNING( "AL-TASK-131" );
      // validateAndCheckTask( testName.getMethodName(), "AL-TASK-125" );
   }


   /**
    * This test error code AL-TASK-131:CUSTOM_START_VALUE, CUSTOM_DUE_VALUE,TERMINATED_VALUE don’t
    * apply for reference document.
    *
    *
    *
    * @throws Exception
    *
    *
    */
   @Test
   public void test_AL_TASK_131_3() throws Exception {

      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iREF_Task_CD + "'" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "NOT_APPLICABLE_BOOL", "'Y'" );
      // lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // first completed record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'" + iREF_Task_CD + "'" );
      // lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "95" );
      lMapTaskSched.put( "COMPLETION_DUE_VALUE", "90" );
      // lMapTaskSched.put( "CUSTOM_START_VALUE", "20" );
      // lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );
      lMapTaskSched.put( "TERMINATED_VALUE", "99" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_EXCEPTWARNING( "AL-TASK-131" );

   }


   /**
    * This test error code AL-TASK-130:If a ref doc has any row with not_applicable_bool as true,
    * you cannot have any row with COMPLETION_DT
    *
    *
    *
    * @throws Exception
    *
    *
    */
   @Test
   public void test_AL_TASK_130() throws Exception {

      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iREF_Task_CD + "'" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "NOT_APPLICABLE_BOOL", "'Y'" );
      // lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // second record
      lMapTask.clear();
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iREF_Task_CD + "'" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      // lMapTask.put( "NOT_APPLICABLE_BOOL", "'Y'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_EXCEPTWARNING( "AL-TASK-130" );

   }


   /**
    * This test error code AL-TASK-132:NOT_APPLICABLE_BOOL can only be ‘Y’,’N’
    *
    *
    *
    * @throws Exception
    *
    *
    */
   @Test
   public void test_AL_TASK_132() throws Exception {

      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iREF_Task_CD + "'" );
      // lMapTask.put( "FIRST_TIME_BOOL", "' '" );
      lMapTask.put( "NOT_APPLICABLE_BOOL", "'A'" );
      // lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_EXCEPTWARNING( "AL-TASK-132" );

   }


   /**
    * This test error code AL-TASK-133:FIRST_TIME_BOOL can only be ‘Y’,’N’
    *
    *
    *
    *
    * @throws Exception
    *
    *
    */
   @Test
   public void test_AL_TASK_133() throws Exception {

      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iREF_Task_CD + "'" );
      lMapTask.put( "FIRST_TIME_BOOL", "'A'" );
      lMapTask.put( "NOT_APPLICABLE_BOOL", "'Y'" );
      // lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_EXCEPTWARNING( "AL-TASK-133" );
      // validateAndCheckTask( testName.getMethodName(), "AL-TASK-125" );
   }


   /**
    * This test error code AL-TASK-134:COMPLETION_DUE_VALUE cannot be specified if COMPLETION_VALUE
    * is not specified.
    *
    *
    *
    *
    * @throws Exception
    *
    *
    */
   @Test
   public void test_AL_TASK_134() throws Exception {

      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iREF_Task_CD + "'" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "NOT_APPLICABLE_BOOL", "'Y'" );
      // lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // first completed record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'" + iREF_Task_CD + "'" );
      // lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      // lMapTaskSched.put( "COMPLETION_VALUE", "95" );
      lMapTaskSched.put( "COMPLETION_DUE_VALUE", "90" );
      // lMapTaskSched.put( "CUSTOM_START_VALUE", "20" );
      // lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );
      // lMapTaskSched.put( "TERMINATED_VALUE", "99" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_EXCEPTWARNING( "AL-TASK-134" );

   }

}
