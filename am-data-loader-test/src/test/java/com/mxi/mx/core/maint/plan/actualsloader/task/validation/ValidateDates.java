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

public class ValidateDates extends ActualsLoaderTest {

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
    * <li>AL-TASK-013 validation failure is found - Completion date is in the future</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */
   @Test
   public void test_AL_TASK_013_CompletionDateInFuture() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'AL_TASK'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2020/01/01','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // call express validation runner and result checker with expected error code
      runValidateTasks();
      checkTaskValidation_WITHWARNING( "AL-TASK-013" );
   }
}
