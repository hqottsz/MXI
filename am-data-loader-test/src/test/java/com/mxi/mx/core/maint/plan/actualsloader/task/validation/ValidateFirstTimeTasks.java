/*
 * Confidential, proprietary and/or trade secret information of Mxi Technologies, Ltd.
 *
 * Copyright 2000-2016 Mxi Technologies, Ltd. All Rights Reserved.
 *
 * Except as expressly provided by written license signed by a duly appointed officer of Mxi
 * Technologies, Ltd., any disclosure, distribution, reproduction, compilation, modification,
 * creation of derivative works and/or other use of the Mxi source code is strictly prohibited.
 * Inclusion of a copyright notice shall not be taken to indicate that the source code has been
 * published.
 */

package com.mxi.mx.core.maint.plan.actualsloader.task.validation;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mxi.mx.util.ActualsLoaderTest;
import com.mxi.mx.util.TableUtil;


/**
 * This test suite contains all test cases for Actuals Loader loading of historic tasks
 *
 * @author Andrew Bruce
 */

public class ValidateFirstTimeTasks extends ActualsLoaderTest {

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
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>The task defn exists in maintenix</li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Insert tasks to the staging table</li>
    * <li>Run Task Validation</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: AL-TASK-117</li>
    * <li>Either FIRST_TIME_BOOL or COMPLETION_DT can be provided, but not both</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AL_TASK_117_FirstTimeAndCompletionDate() throws Exception {

      // create task map
      Map<String, String> lMapTaskFirstTime = new LinkedHashMap<String, String>();
      lMapTaskFirstTime.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskFirstTime.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskFirstTime.put( "MANUFACT_CD", "'10001'" );
      lMapTaskFirstTime.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskFirstTime.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" ); // entered
      lMapTaskFirstTime.put( "FIRST_TIME_BOOL", "'Y'" ); // entered
      lMapTaskFirstTime.put( "COMPLETION_DUE_DT", "null" );
      lMapTaskFirstTime.put( "CUSTOM_START_DT", "null" );
      lMapTaskFirstTime.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTaskFirstTime ) );

      // call express validation runner and result checker with expected error code
      runValidateTasks();
      checkTaskValidation_WITHWARNING( "AL-TASK-117" );

   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>The task defn exists in maintenix</li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Insert tasks to the staging table</li>
    * <li>Run Task Validation</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: AL-TASK-118</li>
    * <li>At least one of FIRST_TIME_BOOL or COMPLETION_DT cannot be blank</li>
    * </ul>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AL_TASK_118_FirstTimeAndCompletionBlank() throws Exception {

      // create task map
      Map<String, String> lMapTaskFirstTime = new LinkedHashMap<String, String>();
      lMapTaskFirstTime.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskFirstTime.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskFirstTime.put( "MANUFACT_CD", "'10001'" );
      lMapTaskFirstTime.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskFirstTime.put( "COMPLETION_DT", "null" ); // empty
      lMapTaskFirstTime.put( "FIRST_TIME_BOOL", "null" ); // empty

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTaskFirstTime ) );

      // call express validation runner and result checker with expected error code
      runValidateTasks();
      checkTaskValidation_WITHWARNING( "AL-TASK-118" );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>The task defn exists in maintenix</li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Insert tasks to the staging table</li>
    * <li>Run Task Validation</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: AL-TASK-119</li>
    * <li>If a TASK in C_RI_TASK is marked as FIRST_TIME, you cannot have another instance of the
    * same TASK in the table</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AL_TASK_119_FirstTimeAndSecondInstanceOfTask() throws Exception {

      // create task map
      Map<String, String> lMapTaskFirstTime = new LinkedHashMap<String, String>();
      lMapTaskFirstTime.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskFirstTime.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskFirstTime.put( "MANUFACT_CD", "'10001'" );
      lMapTaskFirstTime.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskFirstTime.put( "COMPLETION_DT", "null" );
      lMapTaskFirstTime.put( "FIRST_TIME_BOOL", "'Y'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTaskFirstTime ) );

      // create map
      Map<String, String> lMapTaskHistoric = new LinkedHashMap<String, String>(); // historic
                                                                                  // instance
      lMapTaskHistoric.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskHistoric.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskHistoric.put( "MANUFACT_CD", "'10001'" );
      lMapTaskHistoric.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskHistoric.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskHistoric.put( "FIRST_TIME_BOOL", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTaskHistoric ) );

      // create task map
      Map<String, String> lMapTaskFirstTimeSched = new LinkedHashMap<String, String>();
      lMapTaskFirstTimeSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskFirstTimeSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskFirstTimeSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskFirstTimeSched.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskFirstTimeSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskFirstTimeSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskFirstTimeSched.put( "COMPLETION_VALUE", "1" ); // entered

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskFirstTimeSched ) );

      // call express validation runner and result checker with expected error code
      runValidateTasks();
      checkTaskValidation_WITHWARNING( "AL-TASK-119" );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>The task defn exists in maintenix</li>
    * <li>Two inventory items with the same serial no but different part nos exists in
    * maintenix</li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Insert tasks to the staging table</li>
    * <li>Run Task Validation</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: None</li>
    * <li>The different part numbers should prevent the validation error from being triggered.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AL_TASK_119_FirstTimeAndSecondInstanceOfTask_DifferentInventorySameSerialNo()
         throws Exception {

      // create task map - first time
      Map<String, String> lMapTaskFirstTime = new LinkedHashMap<String, String>();
      lMapTaskFirstTime.put( "SERIAL_NO_OEM", "'SNDUPLICATE'" );
      lMapTaskFirstTime.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskFirstTime.put( "MANUFACT_CD", "'10001'" );
      lMapTaskFirstTime.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskFirstTime.put( "COMPLETION_DT", "null" );
      lMapTaskFirstTime.put( "FIRST_TIME_BOOL", "'Y'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTaskFirstTime ) );

      // create task map - historic
      Map<String, String> lMapTaskHistoric = new LinkedHashMap<String, String>(); // historic
                                                                                  // instance
      lMapTaskHistoric.put( "SERIAL_NO_OEM", "'SNDUPLICATE'" );
      lMapTaskHistoric.put( "PART_NO_OEM", "'ACFT_ASSY_PNX'" );
      lMapTaskHistoric.put( "MANUFACT_CD", "'10001'" );
      lMapTaskHistoric.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskHistoric.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskHistoric.put( "FIRST_TIME_BOOL", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTaskHistoric ) );

      // create task map - historic usage
      Map<String, String> lMapTaskHistoricSched = new LinkedHashMap<String, String>();
      lMapTaskHistoricSched.put( "SERIAL_NO_OEM", "'SNDUPLICATE'" );
      lMapTaskHistoricSched.put( "PART_NO_OEM", "'ACFT_ASSY_PNX'" );
      lMapTaskHistoricSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskHistoricSched.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskHistoricSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskHistoricSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskHistoricSched.put( "COMPLETION_VALUE", "0" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskHistoricSched ) );

      // call express validation runner and result checker with expected error code
      runValidateTasks();
      checkTaskValidation_EXCEPTWARNING( "PASS" );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>The task defn exists in maintenix</li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Insert tasks to the staging table</li>
    * <li>Run Task Validation</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: AL-TASK-120</li>
    * <li>You cannot have multiple rows for the same task listed as FIRST_TIME</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AL_TASK_120_MultipleFirstTimeForTask() throws Exception {

      // create map
      Map<String, String> lMapTaskFirstTime = new LinkedHashMap<String, String>(); // first time
                                                                                   // instance
      lMapTaskFirstTime.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskFirstTime.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskFirstTime.put( "MANUFACT_CD", "'10001'" );
      lMapTaskFirstTime.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskFirstTime.put( "COMPLETION_DT", "null" );
      lMapTaskFirstTime.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTaskFirstTime.put( "COMPLETION_DUE_DT", "null" );
      lMapTaskFirstTime.put( "CUSTOM_START_DT", "null" );
      lMapTaskFirstTime.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTaskFirstTime ) );

      // create map
      Map<String, String> lMapTaskHistoric = new LinkedHashMap<String, String>(); // historic
                                                                                  // instance
      lMapTaskHistoric.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskHistoric.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskHistoric.put( "MANUFACT_CD", "'10001'" );
      lMapTaskHistoric.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskHistoric.put( "COMPLETION_DT", "null" );
      lMapTaskHistoric.put( "FIRST_TIME_BOOL", "'Y'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTaskHistoric ) );

      // call express validation runner and result checker with expected error code
      runValidateTasks();
      checkTaskValidation_WITHWARNING( "AL-TASK-120" );

   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>The task defn exists in maintenix</li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Insert tasks to the staging table</li>
    * <li>Run Task Validation</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: AL-TASK-121</li>
    * <li>You cannot provide a COMPLETION_VALUE for first-time tasks, ie. tasks that have
    * FIRST_TIME_BOOL = 1 in the C_RI_TASK table.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AL_TASK_121_FirstTimeAndCompletionValue() throws Exception {

      // create task map
      Map<String, String> lMapTaskFirstTime = new LinkedHashMap<String, String>();
      lMapTaskFirstTime.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskFirstTime.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskFirstTime.put( "MANUFACT_CD", "'10001'" );
      lMapTaskFirstTime.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskFirstTime.put( "COMPLETION_DT", "null" );
      lMapTaskFirstTime.put( "FIRST_TIME_BOOL", "'Y'" ); // entered

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTaskFirstTime ) );

      // create task map
      Map<String, String> lMapTaskFirstTimeSched = new LinkedHashMap<String, String>();
      lMapTaskFirstTimeSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskFirstTimeSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskFirstTimeSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskFirstTimeSched.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskFirstTimeSched.put( "COMPLETION_DT", "null" );
      lMapTaskFirstTimeSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskFirstTimeSched.put( "COMPLETION_VALUE", "1" ); // entered

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskFirstTimeSched ) );

      // call express validation runner and result checker with expected error code
      runValidateTasks();
      checkTaskValidation_WITHWARNING( "AL-TASK-121" );

   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>The task defn exists in maintenix</li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Insert tasks to the staging table</li>
    * <li>Run Task Validation</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: NONE</li>
    * <li>No validation errors are expected</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_Successful_FirstTimeTask() throws Exception {

      // create task map
      Map<String, String> lMapTaskFirstTime = new LinkedHashMap<String, String>();
      lMapTaskFirstTime.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskFirstTime.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskFirstTime.put( "MANUFACT_CD", "'10001'" );
      lMapTaskFirstTime.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskFirstTime.put( "COMPLETION_DT", "null" );
      lMapTaskFirstTime.put( "FIRST_TIME_BOOL", "'Y'" ); // entered
      lMapTaskFirstTime.put( "COMPLETION_DUE_DT", "null" );
      lMapTaskFirstTime.put( "CUSTOM_START_DT", "null" );
      lMapTaskFirstTime.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTaskFirstTime ) );

      // call express validation runner and result checker with expected error code
      runValidateTasks();
      checkTaskValidation_EXCEPTWARNING( "PASS" );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>The task defn exists in maintenix</li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Insert tasks to the staging table</li>
    * <li>Run Task Validation</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: None</li>
    * <li>No validation errors are expected</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_Successful_FirstTimeTask_CustomDue_Date() throws Exception {

      // create task map
      Map<String, String> lMapTaskFirstTime = new LinkedHashMap<String, String>();
      lMapTaskFirstTime.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskFirstTime.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskFirstTime.put( "MANUFACT_CD", "'10001'" );
      lMapTaskFirstTime.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskFirstTime.put( "COMPLETION_DT", "null" );
      lMapTaskFirstTime.put( "FIRST_TIME_BOOL", "'Y'" ); // entered
      lMapTaskFirstTime.put( "COMPLETION_DUE_DT", "null" );
      lMapTaskFirstTime.put( "CUSTOM_START_DT", "null" );
      lMapTaskFirstTime.put( "CUSTOM_DUE_DT", "to_date('2015/01/01','yyyy/mm/dd')" ); // entered

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTaskFirstTime ) );

      // call express validation runner and result checker with expected error code
      runValidateTasks();
      checkTaskValidation_EXCEPTWARNING( "PASS" );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>The task defn exists in maintenix</li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Insert tasks to the staging table</li>
    * <li>Run Task Validation</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: None</li>
    * <li>No validation errors are expected</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_Successful_FirstTimeTask_CustomDue_Value() throws Exception {

      // create task map
      Map<String, String> lMapTaskFirstTime = new LinkedHashMap<String, String>();
      lMapTaskFirstTime.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskFirstTime.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskFirstTime.put( "MANUFACT_CD", "'10001'" );
      lMapTaskFirstTime.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskFirstTime.put( "COMPLETION_DT", "null" );
      lMapTaskFirstTime.put( "FIRST_TIME_BOOL", "'Y'" ); // entered
      lMapTaskFirstTime.put( "COMPLETION_DUE_DT", "null" );
      lMapTaskFirstTime.put( "CUSTOM_START_DT", "null" );
      lMapTaskFirstTime.put( "CUSTOM_DUE_DT", "to_date('2016/01/01','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTaskFirstTime ) );

      // create task map
      Map<String, String> lMapTaskFirstTimeSched = new LinkedHashMap<String, String>();
      lMapTaskFirstTimeSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskFirstTimeSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskFirstTimeSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskFirstTimeSched.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskFirstTimeSched.put( "COMPLETION_DT", "null" ); // entered
      lMapTaskFirstTimeSched.put( "SCHED_PARAMETER", "'HOURS'" ); // entered
      lMapTaskFirstTimeSched.put( "COMPLETION_VALUE", "null" ); // entered
      lMapTaskFirstTimeSched.put( "COMPLETION_DUE_VALUE", "null" ); // entered
      lMapTaskFirstTimeSched.put( "CUSTOM_START_VALUE", "null" );
      lMapTaskFirstTimeSched.put( "CUSTOM_DUE_VALUE", "1000" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskFirstTimeSched ) );

      // call express validation runner and result checker with expected error code
      runValidateTasks();
      checkTaskValidation_EXCEPTWARNING( "PASS" );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>The task defn exists in maintenix</li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Insert tasks to the staging table</li>
    * <li>Run Task Validation</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: None</li>
    * <li>No validation errors are expected</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_Successful_FirstTimeTask_CustomStart_Date() throws Exception {

      // create task map
      Map<String, String> lMapTaskFirstTime = new LinkedHashMap<String, String>();
      lMapTaskFirstTime.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskFirstTime.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskFirstTime.put( "MANUFACT_CD", "'10001'" );
      lMapTaskFirstTime.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskFirstTime.put( "COMPLETION_DT", "null" );
      lMapTaskFirstTime.put( "FIRST_TIME_BOOL", "'Y'" ); // entered
      lMapTaskFirstTime.put( "COMPLETION_DUE_DT", "null" );
      lMapTaskFirstTime.put( "CUSTOM_START_DT", "to_date('2015/01/01','yyyy/mm/dd')" ); // entered
      lMapTaskFirstTime.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTaskFirstTime ) );

      // call express validation runner and result checker with expected error code
      runValidateTasks();
      checkTaskValidation_EXCEPTWARNING( "PASS" );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>The task defn exists in maintenix</li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Insert tasks to the staging table</li>
    * <li>Run Task Validation</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: None</li>
    * <li>No validation errors are expected</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_Successful_FirstTimeTask_CustomStart_Value() throws Exception {

      // create task map
      Map<String, String> lMapTaskFirstTime = new LinkedHashMap<String, String>();
      lMapTaskFirstTime.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskFirstTime.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskFirstTime.put( "MANUFACT_CD", "'10001'" );
      lMapTaskFirstTime.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskFirstTime.put( "COMPLETION_DT", "null" );
      lMapTaskFirstTime.put( "FIRST_TIME_BOOL", "'Y'" ); // entered
      lMapTaskFirstTime.put( "COMPLETION_DUE_DT", "null" );
      lMapTaskFirstTime.put( "CUSTOM_START_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskFirstTime.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTaskFirstTime ) );

      // create task map
      Map<String, String> lMapTaskFirstTimeSched = new LinkedHashMap<String, String>();
      lMapTaskFirstTimeSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskFirstTimeSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskFirstTimeSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskFirstTimeSched.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskFirstTimeSched.put( "COMPLETION_DT", "null" ); // entered
      lMapTaskFirstTimeSched.put( "SCHED_PARAMETER", "'HOURS'" ); // entered
      lMapTaskFirstTimeSched.put( "COMPLETION_VALUE", "null" );
      lMapTaskFirstTimeSched.put( "COMPLETION_DUE_VALUE", "null" );
      lMapTaskFirstTimeSched.put( "CUSTOM_START_VALUE", "1" );
      lMapTaskFirstTimeSched.put( "CUSTOM_DUE_VALUE", "null" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskFirstTimeSched ) );

      // call express validation runner and result checker with expected error code
      runValidateTasks();
      checkTaskValidation_EXCEPTWARNING( "PASS" );
   }
}
