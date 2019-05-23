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
 * <p>
 * This test suite contains ensures Actuals Loader validation of historic tasks custom start values
 * are handled correctly.
 * </p>
 *
 * @author Stephane Levert
 */
public class ValidateCustomStartTest extends ActualsLoaderTest {

   private static final String HOUR_DATA_TYPE_CD = "HOUR";
   private static final String CYCLE_DATA_TYPE_CD = "CYCLE";
   private static final String USAGE1_DATA_TYPE_CD = "USAGE1";


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
    * AL-TASK-110:You cannot provide a CUSTOM_START_DT in the C_RI_TASK staging table without
    * providing a CUSTOM_START_VALUE in the C_RI_TASK_SCHED staging table for every scheduling
    * parameter on the requirement.
    *
    *
    */

   @Test
   public void test_AL_TASK_110() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'AT_TEST'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "COMPLETION_DUE_DT", "null" );
      lMapTask.put( "CUSTOM_START_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_DUE_DT", null );

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
      lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // check error code
      checkTaskValidation_WITHWARNING( "AL-TASK-110" );

   }


   /**
    * You cannot provide a CUSTOM_START_VALUE in the C_RI_TASK_SCHED staging table without providing
    * a CUSTOM_START_VALUE for every other scheduling parameter on the requirement.
    *
    *
    */

   @Test
   public void test_AL_TASK_111() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'AT_TEST'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "COMPLETION_DUE_DT", "null" );
      lMapTask.put( "CUSTOM_START_DT", null );
      lMapTask.put( "CUSTOM_DUE_DT", null );

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
      lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "100" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // first completed record
      lMapTaskSched.clear();
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'AT_TEST'" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'CYCLE'" );
      lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTaskSched.put( "CUSTOM_START_VALUE", "100" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // check error code
      checkTaskValidation_WITHWARNING( "AL-TASK-111" );

   }


   /**
    * AL-TASK-112:You cannot provide a CUSTOM_START_VALUE in the C_RI_TASK_SCHED staging table
    * without providing a CUSTOM_START_DT in the C_RI_TASK staging table for the same requirement.
    *
    *
    */

   @Test
   public void test_AL_TASK_112() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'AT_TEST'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "COMPLETION_DUE_DT", "null" );
      lMapTask.put( "CUSTOM_START_DT", null );
      lMapTask.put( "CUSTOM_DUE_DT", null );

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
      lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "100" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // check error code
      checkTaskValidation_WITHWARNING( "AL-TASK-112" );

   }

}
