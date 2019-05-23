/*
 * Confidential, proprietary and/or trade secret information of Mxi Technologies, Ltd.
 *
 * Copyright 2000-2015 Mxi Technologies, Ltd. All Rights Reserved.
 *
 * Except as expressly provided by written license signed by a duly appointed officer of Mxi
 * Technologies, Ltd., any disclosure, distribution, reproduction, compilation, modification,
 * creation of derivative works and/or other use of the Mxi source code is strictly prohibited.
 * Inclusion of a copyright notice shall not be taken to indicate that the source code has been
 * published.
 */

package com.mxi.mx.core.maint.plan.actualsloader.task.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.ActualsLoaderTest;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.ValidationAndImport;
import com.mxi.mx.util.WhereClause;


/**
 * This test suite contains unit tests for Actuals Loader task validations
 *
 * @author Alicia Qian
 */
public class ValidationTest extends ActualsLoaderTest {

   @Rule
   public TestName testName = new TestName();
   ValidationAndImport ivalidationandimport;


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
    * This test error code AL-TASK-029:'You cannot provide TERMINATED_NOTE in the C_RI_TASK table if
    * the TERMINATED_DT is not specified'
    *
    * @throws Exception
    *
    *
    */
   @Test
   public void test_AL_TASK_029() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'AL_TASK'" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "TERMINATED_DT", "to_date('2017/01/01','yyyy/mm/dd')" );
      lMapTask.put( "TERMINATED_DT", null );
      lMapTask.put( "TERMINATED_NOTE", "'AUTO TEST'" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // first completed record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "95" );
      lMapTaskSched.put( "COMPLETION_DUE_VALUE", "90" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );
      lMapTaskSched.put( "TERMINATED_VALUE", "150" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_EXCEPTWARNING( "AL-TASK-029" );
      // validateAndCheckTask( testName.getMethodName(), "AL-TASK-029" );
   }


   /**
    * This test error code AL-TASK-030:The provided TERMINATED_DT in the C_RI_TASK table cannot be a
    * future date
    *
    * @throws Exception
    *
    *
    */
   @Test
   public void test_AL_TASK_030() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'AL_TASK'" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "TERMINATED_DT", "to_date('2017/01/01','yyyy/mm/dd')" );
      lMapTask.put( "TERMINATED_DT", "SYSDATE+10" );
      lMapTask.put( "TERMINATED_NOTE", "'AUTO TEST'" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // first completed record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "95" );
      lMapTaskSched.put( "COMPLETION_DUE_VALUE", "90" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );
      lMapTaskSched.put( "TERMINATED_VALUE", "150" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation

      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_WITHWARNING( "AL-TASK-030" );
      // validateAndCheckTask( testName.getMethodName(), "AL-TASK-030" );
   }


   /**
    * This test error code AL-TASK-031:The provided TERMINATED_DT in the C_RI_TASK table cannot be
    * before COMPLETION_DT.
    *
    * @throws Exception
    *
    *
    */
   @Test
   public void test_AL_TASK_031() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'AL_TASK'" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "TERMINATED_DT", "to_date('2017/01/01','yyyy/mm/dd')" );
      lMapTask.put( "TERMINATED_DT", "to_date('2014/01/01','yyyy/mm/dd')" );
      lMapTask.put( "TERMINATED_NOTE", "'AUTO TEST'" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // first completed record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "95" );
      lMapTaskSched.put( "COMPLETION_DUE_VALUE", "90" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );
      lMapTaskSched.put( "TERMINATED_VALUE", "150" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_WITHWARNING( "AL-TASK-031" );
      // validateAndCheckTask( testName.getMethodName(), "AL-TASK-031" );
   }


   /**
    * This test error code AL-TASK-032:If the TERMINATED_DT is specified in the C_RI_TASK table then
    * CUSTOM_START_DT and CUSTOM_DUE_DATE must be blank
    *
    * @throws Exception
    *
    *
    */
   @Test
   public void test_AL_TASK_032_1() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'AL_TASK'" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "TERMINATED_DT", "to_date('2017/01/01','yyyy/mm/dd')" );
      lMapTask.put( "TERMINATED_NOTE", "'AUTO TEST'" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_START_DT", "to_date('2015/01/02','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // first completed record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "95" );
      lMapTaskSched.put( "COMPLETION_DUE_VALUE", "90" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );
      lMapTaskSched.put( "TERMINATED_VALUE", "150" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_WITHWARNING( "AL-TASK-032" );

   }


   /**
    * This test error code AL-TASK-032:If the TERMINATED_DT is specified in the C_RI_TASK table then
    * CUSTOM_START_DT and CUSTOM_DUE_DATE must be blank
    *
    * @throws Exception
    *
    *
    */
   @Test
   public void test_AL_TASK_032_2() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'AL_TASK'" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "TERMINATED_DT", "to_date('2017/01/01','yyyy/mm/dd')" );
      lMapTask.put( "TERMINATED_NOTE", "'AUTO TEST'" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_START_DT", null );
      lMapTask.put( "CUSTOM_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // first completed record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "95" );
      lMapTaskSched.put( "COMPLETION_DUE_VALUE", "90" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );
      lMapTaskSched.put( "TERMINATED_VALUE", "150" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_WITHWARNING( "AL-TASK-032" );
   }


   /**
    * This test error code AL-TASK-033:The TERMINATED_DT has been specied not on the latest
    * completion record in C_RI_TASK table among several completion records for the same task.
    *
    * @throws Exception
    *
    *
    */
   @Test
   public void test_AL_TASK_033() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'AL_TASK'" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "TERMINATED_DT", "to_date('2017/01/01','yyyy/mm/dd')" );
      lMapTask.put( "TERMINATED_NOTE", "'AUTO TEST'" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // second completed record
      lMapTask.clear();
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'AL_TASK'" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2016/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "TERMINATED_DT", "to_date('2017/01/01','yyyy/mm/dd')" );
      lMapTask.put( "TERMINATED_NOTE", "'AUTO TEST'" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // first completed record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "95" );
      lMapTaskSched.put( "COMPLETION_DUE_VALUE", "90" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );
      lMapTaskSched.put( "TERMINATED_VALUE", "150" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_WITHWARNING( "AL-TASK-033" );

   }


   /**
    * This test error code AL-TASK-034: You cannot provide TERMINATED_VALUE in the C_RI_TASK_SCHED
    * table without providing TERMINATED_DT in the C_RI_TASK table
    *
    * @throws Exception
    *
    *
    */
   @Test
   public void test_AL_TASK_034() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'AL_TASK'" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "TERMINATED_DT", "to_date('2017/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "TERMINATED_NOTE", "'AUTO TEST'" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // first completed record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "95" );
      lMapTaskSched.put( "COMPLETION_DUE_VALUE", "90" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );
      lMapTaskSched.put( "TERMINATED_VALUE", "150" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_EXCEPTWARNING( "AL-TASK-034" );

   }


   /**
    * This test error code AL-TASK-035: The TERMINATED_VALUE cannot be less then the COMPLETED_VALUE
    * specied in C_RI_TASK_SCHED table
    *
    * @throws Exception
    *
    *
    */
   @Test
   public void test_AL_TASK_035() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'AL_TASK'" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "TERMINATED_DT", "to_date('2017/01/01','yyyy/mm/dd')" );
      lMapTask.put( "TERMINATED_NOTE", "'AUTO TEST'" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // first completed record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "95" );
      lMapTaskSched.put( "COMPLETION_DUE_VALUE", "90" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );
      lMapTaskSched.put( "TERMINATED_VALUE", "50" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_EXCEPTWARNING( "AL-TASK-035" );

   }


   /**
    * This test error code AL-TASK-036: The provided TERMINATED_VALUE cannot be greater than the
    * current usage identified by SERIAL_NO_OEM/PART_NO_OEM'
    *
    * @throws Exception
    *
    *
    */
   @Test
   public void test_AL_TASK_036() throws Exception {

      simpleIDs ldatatypeID = getDataTypeIDs( "HOURS", "Flying Hours", "HOUR", "US" );
      int lCurrentUsage = getCurrentUsage( "SN000001", ldatatypeID );

      int usage = lCurrentUsage + 100;

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'AT_TEST'" );
      lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      // lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "TERMINATED_DT", "to_date('2017/01/01','yyyy/mm/dd')" );
      lMapTask.put( "TERMINATED_NOTE", "'AUTO TEST'" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // first completed record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'AT_TEST'" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "TERMINATED_VALUE", "'" + usage + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_EXCEPTWARNING( "AL-TASK-036" );

   }


   /**
    * This test error code AL-TASK-037:The provided date values CUSTOM_START_VALUE and
    * CUSTOM_DUE_VALUE will be ignored as they only apply to first time (and not terminated) tasks'
    *
    * @throws Exception
    *
    *
    */
   @Test
   public void test_AL_TASK_037_1() throws Exception {
      simpleIDs ldatatypeID = getDataTypeIDs( "HOURS", "Flying Hours", "HOUR", "US" );
      int lCurrentUsage = getCurrentUsage( "SN000001", ldatatypeID );
      int usage = lCurrentUsage - 1;

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'AL_TASK'" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "TERMINATED_DT", "SYSDATE-1" );
      lMapTask.put( "TERMINATED_NOTE", "'AUTO TEST'" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_START_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "CUSTOM_DUE_DT", "to_date('2015/01/07','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // first completed record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "95" );
      lMapTaskSched.put( "COMPLETION_DUE_VALUE", "90" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "50" );
      // lMapTaskSched.put( "CUSTOM_DUE_VALUE", "30" );
      lMapTaskSched.put( "TERMINATED_VALUE", "'" + usage + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_WITHWARNING( "AL-TASK-037" );
      // validateAndCheckTask( testName.getMethodName(), "AL-TASK-029" );
   }


   /**
    * This test error code AL-TASK-037:The provided date values CUSTOM_START_VALUE and
    * CUSTOM_DUE_VALUE will be ignored as they only apply to first time (and not terminated) tasks'
    *
    * @throws Exception
    *
    *
    */
   @Test
   public void test_AL_TASK_037_2() throws Exception {
      simpleIDs ldatatypeID = getDataTypeIDs( "HOURS", "Flying Hours", "HOUR", "US" );
      int lCurrentUsage = getCurrentUsage( "SN000001", ldatatypeID );
      int usage = lCurrentUsage - 1;

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'AL_TASK'" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "TERMINATED_DT", "SYSDATE-1" );
      lMapTask.put( "TERMINATED_NOTE", "'AUTO TEST'" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );
      // lMapTask.put( "CUSTOM_START_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_DUE_DT", "to_date('2015/01/07','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // first completed record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "95" );
      lMapTaskSched.put( "COMPLETION_DUE_VALUE", "90" );
      // lMapTaskSched.put( "CUSTOM_START_VALUE", "50" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "30" );
      lMapTaskSched.put( "TERMINATED_VALUE", "'" + usage + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_WITHWARNING( "AL-TASK-037" );
      // validateAndCheckTask( testName.getMethodName(), "AL-TASK-029" );
   }


   /**
    * This test error code AL-TASK-038:The provided TERMINATED VALUE cannot be negative.
    *
    * @throws Exception
    *
    *
    */
   @Test
   public void test_AL_TASK_038() throws Exception {
      simpleIDs ldatatypeID = getDataTypeIDs( "HOURS", "Flying Hours", "HOUR", "US" );
      int lCurrentUsage = getCurrentUsage( "SN000001", ldatatypeID );
      int usage = lCurrentUsage - 10000;

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'AL_TASK'" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "TERMINATED_DT", "SYSDATE-1" );
      lMapTask.put( "TERMINATED_NOTE", "'AUTO TEST'" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );
      // lMapTask.put( "CUSTOM_START_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_DUE_DT", "to_date('2015/01/07','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // first completed record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "95" );
      lMapTaskSched.put( "COMPLETION_DUE_VALUE", "90" );
      // lMapTaskSched.put( "CUSTOM_START_VALUE", "50" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "30" );
      lMapTaskSched.put( "TERMINATED_VALUE", "'" + usage + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_EXCEPTWARNING( "AL-TASK-038" );
      // validateAndCheckTask( testName.getMethodName(), "AL-TASK-029" );
   }


   // =============================================================================================================

   /**
    * This function is to get assmbl_bom_id given assmbl_cd and assmbl_bom_cd.
    *
    *
    */

   public int getSlotId( String aAssmbl_Cd, String aAssmblBomCd ) {

      String[] iIds = { "ASSMBL_BOM_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "ASSMBL_DB_ID", Integer.toString( CONS_DB_ID ) );
      lArgs.addArguments( "ASSMBL_CD", aAssmbl_Cd );
      lArgs.addArguments( "ASSMBL_BOM_CD", aAssmblBomCd );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_ASSMBL_BOM, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      return Integer.parseInt( llists.get( 0 ).get( 0 ) );

   }


   /**
    * This function is to get first inventory id given assmnl_cd and assmbl_id
    *
    *
    */
   public String getInvNoID_local( String aAssmblCd, String aAssmblId ) {

      String[] iIds = { "INV_NO_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "ASSMBL_DB_ID", Integer.toString( CONS_DB_ID ) );
      lArgs.addArguments( "ASSMBL_CD", aAssmblCd );
      lArgs.addArguments( "ASSMBL_BOM_ID", aAssmblId );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.INV_INV, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      return llists.get( 0 ).get( 0 );

   }
}
