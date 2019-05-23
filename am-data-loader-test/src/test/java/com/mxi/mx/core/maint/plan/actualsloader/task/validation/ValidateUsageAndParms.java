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

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mxi.mx.util.ActualsLoaderTest;
import com.mxi.mx.util.TableUtil;


/**
 * This test suite contains unit tests for Actuals Loader task validations for Manufacturer Codes
 *
 * @author Andrew Bruce
 */

public class ValidateUsageAndParms extends ActualsLoaderTest {

   /**
    * Clean up after each individual test
    *
    * @throws Exception
    */
   @Override
   @After
   public void after() throws Exception {

      // // clean up the event data
      // clearMxTestData();

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
    * Ensures expected validation failure can be triggered when appropriate
    *
    * <p>
    * Preconditions:
    *
    * <ul>
    * <li>Data is already in the al_proc tables</li>
    * </ul>
    * </p>
    *
    * <p>
    * Action:
    *
    * <ul>
    * <li>run the validate task functionality</li>
    * </ul>
    * </p>
    *
    * <p>
    * Expectations:
    *
    * <ul>
    * <li>AL-TASK-016 validation failure is found - Duplicate usage parameter provided</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */
   @Test
   public void test_AL_TASK_016_DuplicateUsageParameter() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'AL_TASK'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "1" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      lMapTaskSched.put( "COMPLETION_VALUE", "2" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // call express validation runner and result checker with expected error code
      runValidateTasks();
      checkTaskValidation_WITHWARNING( "AL-TASK-016" );
   }


   /**
    * Ensures expected validation failure can be triggered when appropriate
    *
    * <p>
    * Preconditions:
    *
    * <ul>
    * <li>Data is already in the al_proc tables</li>
    * </ul>
    * </p>
    *
    * <p>
    * Action:
    *
    * <ul>
    * <li>run the validate task functionality</li>
    * </ul>
    * </p>
    *
    * <p>
    * Expectations:
    * <li>Error Code: AL-TASK-101</li>
    * <li>Error Message: The provided TASK_CD, COMPLETION_DT, SERIAL_NO_OEM, and PART_NO_OEM does
    * not exist in the C_RI_TASK staging table</li>
    *
    * @throws Exception
    */

   @Test
   public void test_AL_TASK_101_NotInC_RI_Task() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" ); // serial number of locked aircraft
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'XXUNKXX'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000001'" ); // serial number of locked aircraft
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "1" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // call express validation runner and result checker with expected error code
      runValidateTasks();
      checkTaskValidation_WITHWARNING( "AL-TASK-101" );

   }


   /**
    * Ensures expected validation failure can be triggered when appropriate
    *
    * <p>
    * Preconditions:
    *
    * <ul>
    * <li>Data is already in the al_proc tables</li>
    * </ul>
    * </p>
    *
    * <p>
    * Action:
    *
    * <ul>
    * <li>run the validate task functionality</li>
    * </ul>
    * </p>
    *
    * <p>
    * Expectations:
    *
    * <ul>
    * <li>AL-TASK-102 validation failure is found - No parameter provided</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */
   @Test
   public void test_AL_TASK_102_TaskParamMandatory() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'AL_TASK'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'AL_TASK'" );

      // lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" ); / usage parm not provided
      lMapTaskSched.put( "COMPLETION_VALUE", "1" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // update task map
      lMapTaskSched.put( "SCHED_PARAMETER", "'CYCLES'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // call express validation runner and result checker with expected error code
      runValidateTasks();
      checkTaskValidation_WITHWARNING( "AL-TASK-102" );

   }


   /**
    * Ensures expected validation failure can be triggered when appropriate
    *
    * <p>
    * Preconditions:
    *
    * <ul>
    * <li>Data is already in the al_proc tables</li>
    * </ul>
    * </p>
    *
    * <p>
    * Action:
    *
    * <ul>
    * <li>run the validate task functionality</li>
    * </ul>
    * </p>
    *
    * <p>
    * Expectations:
    *
    * <ul>
    * <li>AL-TASK-103 validation failure is found - Completion value not provided</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */
   @Test
   public void test_AL_TASK_103_CompletionValueMandatory() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'AL_TASK'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      // lMapTaskSched.put( "COMPLETION_VALUE", "1" ); // usage value not provided

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // update task map
      lMapTaskSched.put( "SCHED_PARAMETER", "'CYCLES'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "1" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // call express validation runner and result checker with expected error code
      runValidateTasks();
      checkTaskValidation_WITHWARNING( "AL-TASK-103" );
   }


   /**
    * Ensures expected validation failure can be triggered when appropriate
    *
    * <p>
    * Preconditions:
    *
    * <ul>
    * <li>Data is already in the al_proc tables</li>
    * </ul>
    * </p>
    *
    * <p>
    * Action:
    *
    * <ul>
    * <li>run the validate task functionality</li>
    * </ul>
    * </p>
    *
    * <p>
    * Expectations:
    *
    * <ul>
    * <li>AL-TASK-105 validation failure is found - Missing usage parameter</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */
   @Test
   public void test_AL_TASK_105_ParmIntervalNotInStagingTable() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'AL_TASK'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'CYCLES'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "1" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // call express validation runner and result checker with expected error code
      runValidateTasks();
      checkTaskValidation_WITHWARNING( "AL-TASK-105" );
   }


   /**
    * Ensures expected validation failure can be triggered when appropriate
    *
    * <p>
    * Preconditions:
    *
    * <ul>
    * <li>Data is already in the al_proc tables</li>
    * </ul>
    * </p>
    *
    * <p>
    * Action:
    *
    * <ul>
    * <li>run the validate task functionality</li>
    * </ul>
    * </p>
    *
    * <p>
    * Expectations:
    *
    * <ul>
    * <li>AL-TASK-106 validation failure is found - Unknown parameter provided</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */
   @Test
   public void test_AL_TASK_106_ParmNotInMaintenix() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'AL_TASK'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'MXHR'" ); // unknown parm
      lMapTaskSched.put( "COMPLETION_VALUE", "1" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // call express validation runner and result checker with expected error code
      runValidateTasks();
      checkTaskValidation_WITHWARNING( "AL-TASK-106" );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>The task defn exists in maintenix</li>
    * <li>The task being imported has a usage value of type CA</li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Run Task Validation</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: AL-TASK-107 validation (The provided SCHED_PARAMETER is not a usage-type
    * parameter) error/warning is raised</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AL_TASK_107_ParmNotTypeUSage() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'AL_TASK'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'CYR'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "1" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // call express validation runner and result checker with expected error code
      runValidateTasks();
      checkTaskValidation_WITHWARNING( "AL-TASK-107" );
   }


   /**
    * Ensures expected validation failure can be triggered when appropriate
    *
    * <p>
    * Preconditions:
    *
    * <ul>
    * <li>Data is already in the al_proc tables</li>
    * </ul>
    * </p>
    *
    * <p>
    * Action:
    *
    * <ul>
    * <li>run the validate task functionality</li>
    * </ul>
    * </p>
    *
    * <p>
    * Expectations:
    *
    * <ul>
    * <li>AL-TASK-108 validation failure is found - Negative usage provided</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */
   @Test
   public void test_AL_TASK_108_NegativeCompletionValue() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'AL_TASK'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "-1" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // call express validation runner and result checker with expected error code
      runValidateTasks();
      checkTaskValidation_WITHWARNING( "AL-TASK-108" );
   }


   /**
    * Ensures expected validation failure can be triggered when appropriate
    *
    * <p>
    * Preconditions:
    *
    * <ul>
    * <li>Data is already in the al_proc tables</li>
    * </ul>
    * </p>
    *
    * <p>
    * Action:
    *
    * <ul>
    * <li>run the validate task functionality</li>
    * </ul>
    * </p>
    *
    * <p>
    * Expectations:
    *
    * <ul>
    * <li>AL-TASK-109 validation failure is found - parameter provided greater than current
    * usage</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */
   @Test
   public void test_AL_TASK_109_ParmCompletionValueGTInvCurreUsage() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'AL_TASK'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "1000" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // call express validation runner and result checker with expected error code
      runValidateTasks();
      checkTaskValidation_WITHWARNING( "AL-TASK-109" );
   }
}
