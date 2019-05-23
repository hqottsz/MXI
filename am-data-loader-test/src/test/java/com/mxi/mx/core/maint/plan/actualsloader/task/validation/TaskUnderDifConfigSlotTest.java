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
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.ActualsLoaderTest;
import com.mxi.mx.util.TableUtil;


/**
 * This test suite contains test cases for Actuals Loader where a task is staged and is for a task
 * definition that exists under a different config slot
 *
 * @author Karan Tandon, Alicia Qian
 */
public class TaskUnderDifConfigSlotTest extends ActualsLoaderTest {

   @Rule
   public TestName testName = new TestName();

   simpleIDs iSerialNumIds;


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


   @Override
   @Before
   public void before() throws Exception {
      super.before();
      clearActualsLoaderTables();
      String strTCName = testName.getMethodName();
      if ( strTCName.contains( "_124" ) || ( strTCName.contains( "_123" ) ) ) {
         DataSetup123();
      }
   }


   @After
   public void Restore() {
      if ( iSerialNumIds != null )
         UpdateSerialNoOem( iSerialNumIds, "XXX" );
   }


   /**
    * AL-TASK-003: The provided REQUIREMENT CODE does not apply to the given PART NUMBER/SERIAL
    * NUMBER (via config slot or part number).
    *
    * @throws Exception
    */
   @Test
   public void test_AL_TASK_003_TaskAgainstWrongConfigSlot() {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();
      lMapTask.put( "SERIAL_NO_OEM", "'TRK-TEST04'" );
      lMapTask.put( "PART_NO_OEM", "'A0000001'" );
      lMapTask.put( "MANUFACT_CD", "'00001'" );
      lMapTask.put( "TASK_CD", "'SYS-REQ1'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "COMPLETION_DUE_DT", "null" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();
      lMapTaskSched.put( "SERIAL_NO_OEM", "'TRK-TEST04'" );
      lMapTaskSched.put( "PART_NO_OEM", "'A0000001'" );
      lMapTaskSched.put( "MANUFACT_CD", "'00001'" );
      lMapTaskSched.put( "TASK_CD", "'SYS-REQ1'" );
      lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "0" );
      lMapTaskSched.put( "COMPLETION_DUE_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // call express validation runner and result checker with expected error code
      runValidateTasks();
      checkTaskValidation_WITHWARNING( "AL-TASK-003" );
   }


   /**
    * AL-TASK-123: he config slot of inventory specified does not match with config slot of the task
    * specified. Only the historical task will be created and not the active task
    *
    * @throws Exception
    */
   @Test
   public void test_AL_TASK_123_TaskAgainstWrongSerialNumberHistorical() {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();
      lMapTask.put( "SERIAL_NO_OEM", "'SNAFT737_TRK_XXX'" );
      lMapTask.put( "PART_NO_OEM", "'A0000015'" );
      lMapTask.put( "MANUFACT_CD", "'ABC11'" );
      lMapTask.put( "TASK_CD", "'TRK-MULTI-SLOT-UNIQUE-REQ'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "FIRST_TIME_BOOL", "null" );
      lMapTask.put( "COMPLETION_DUE_DT", "null" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // call express validation runner and result checker with expected error code
      runValidateTasks();
      checkTaskValidation_WITHWARNING( "AL-TASK-123" );

   }


   /**
    * AL-TASK-124: The config slot of inventory specified does not match with config slot of the
    * task specified. The first time task will not be created.
    *
    * @throws Exception
    */
   @Test
   public void test_AL_TASK_124_TaskAgainstWrongSerialNumberFirstTime() {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();
      lMapTask.put( "SERIAL_NO_OEM", "'SNAFT737_TRK_XXX'" );
      lMapTask.put( "PART_NO_OEM", "'A0000015'" );
      lMapTask.put( "MANUFACT_CD", "'ABC11'" );
      lMapTask.put( "TASK_CD", "'TRK-MULTI-SLOT-UNIQUE-REQ'" );
      lMapTask.put( "COMPLETION_DT", "null" );
      lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "COMPLETION_DUE_DT", "null" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // call express validation runner and result checker with expected error code
      runValidateTasks();
      checkTaskValidation_WITHWARNING( "AL-TASK-124" );

   }


   /**
    *
    * Find and update the specific serial_no_oem
    *
    */
   public void DataSetup123() {
      String lQuery = "SELECT ii2.inv_no_db_id,  ii2.inv_no_id                       "
            + "         FROM inv_inv ii1                                               "
            + "         INNER JOIN inv_inv ii2 ON                                      "
            + "            ii2.h_inv_no_db_id = ii1.inv_no_db_id AND                   "
            + "            ii2.h_inv_no_id = ii1.inv_no_id                             "
            + "         WHERE ii2.config_pos_sdesc = 'ACFT-SYS-1-2-TRK-MULTI-SLOT' AND "
            + "            ii1.serial_no_oem = 'SNAFT737'                              ";
      iSerialNumIds = getIDs( lQuery, "inv_no_db_id", "inv_no_id" );
      // update the inv serial no from "XXX" to "SNAFT737_TRK_XXX"
      UpdateSerialNoOem( iSerialNumIds, "SNAFT737_TRK_XXX" );

   }


   /**
    * Update the Serial No OEM in the inv_inv table
    *
    * @param iInvNoIds
    *           - ids for serial No that needs updating
    * @param NewSerialOem
    *           - the new value
    */
   private void UpdateSerialNoOem( simpleIDs iInvNoIds, String NewSerialOem ) {
      String lQuery =
            "Update inv_inv set serial_no_oem = '" + NewSerialOem + "' where inv_no_db_id = "
                  + iInvNoIds.getNO_DB_ID() + " and inv_no_id = " + iInvNoIds.getNO_ID();
      runUpdate( lQuery );

   }

}
