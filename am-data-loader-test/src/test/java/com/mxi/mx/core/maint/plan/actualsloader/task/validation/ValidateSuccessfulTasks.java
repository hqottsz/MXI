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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mxi.mx.util.ActualsLoaderTest;
import com.mxi.mx.util.StringUtils;
import com.mxi.mx.util.TableUtil;


/**
 * This test suite contains all test cases for Actuals Loader loading of historic tasks
 *
 * @author Andrew Bruce
 */

public class ValidateSuccessfulTasks extends ActualsLoaderTest {

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
    * Ensures expected successful validation
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
    * <li>No validation failure is found.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */
   @Test
   public void test_SuccessfulValidation() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'AL_TASK'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "COMPLETION_DUE_DT", "null" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskFirstTimeSched = new LinkedHashMap<String, String>();
      lMapTaskFirstTimeSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskFirstTimeSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskFirstTimeSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskFirstTimeSched.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskFirstTimeSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskFirstTimeSched.put( "SCHED_PARAMETER", "'HOURS'" ); // entered
      lMapTaskFirstTimeSched.put( "COMPLETION_VALUE", "1" ); // entered
      lMapTaskFirstTimeSched.put( "COMPLETION_DUE_VALUE", "null" ); // entered
      lMapTaskFirstTimeSched.put( "CUSTOM_START_VALUE", "null" );
      lMapTaskFirstTimeSched.put( "CUSTOM_DUE_VALUE", "null" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskFirstTimeSched ) );

      // call express validation runner and result checker with expected error code
      runValidateTasks();
      checkTaskValidation_EXCEPTWARNING( "PASS" );
   }


   /**
    * Ensures successful validation and last due = last done when no last due is provided.
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
    * <li>No validation failure is found.</li>
    * <li>Last due = last done.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */
   @Test
   public void test_LastDueLastDoneValidationAndStaging() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'AL_TASK'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "COMPLETION_DUE_DT", "null" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskFirstTimeSched = new LinkedHashMap<String, String>();
      lMapTaskFirstTimeSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskFirstTimeSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskFirstTimeSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskFirstTimeSched.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskFirstTimeSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskFirstTimeSched.put( "SCHED_PARAMETER", "'HOURS'" ); // entered
      lMapTaskFirstTimeSched.put( "COMPLETION_VALUE", "5" ); // entered
      lMapTaskFirstTimeSched.put( "COMPLETION_DUE_VALUE", "null" ); // entered
      lMapTaskFirstTimeSched.put( "CUSTOM_START_VALUE", "null" );
      lMapTaskFirstTimeSched.put( "CUSTOM_DUE_VALUE", "null" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskFirstTimeSched ) );

      // call express validation runner and result checker with expected error code
      runValidateTasks();
      checkTaskValidation_EXCEPTWARNING( "PASS" );
      verifyLastDueValue( lMapTaskFirstTimeSched );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>The task defn exists in maintenix.</li>
    * <li>The task only tracks HOURS.</li>
    * <li>Custom due hours are specified.</li>
    * <li>Custom due cycles are not specified.</li>
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
    * <li>No validation errors are expected.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_Successful_CustomDue_Value_Hours_Only() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'AL_TASK'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "FIRST_TIME_BOOL", "'N'" );
      lMapTask.put( "COMPLETION_DUE_DT", "null" );
      lMapTask.put( "CUSTOM_START_DT", "null" );

      // lMapTask.put( "CUSTOM_DUE_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "to_date('2016/01/01','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskFirstTimeSched = new LinkedHashMap<String, String>();
      lMapTaskFirstTimeSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskFirstTimeSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskFirstTimeSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskFirstTimeSched.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskFirstTimeSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" ); // entered
      lMapTaskFirstTimeSched.put( "SCHED_PARAMETER", "'HOURS'" ); // entered
      lMapTaskFirstTimeSched.put( "COMPLETION_VALUE", "1" ); // entered
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


   // ================================================================================================
   /**
    * Verifies that the last due value is equal to the last done value after validation has been
    * run.
    *
    * @param aMapTaskFirstTimeSched
    *
    * @throws SQLException
    */
   private void verifyLastDueValue( Map<String, String> aMapTaskFirstTimeSched )
         throws SQLException {
      String lTaskCode = aMapTaskFirstTimeSched.get( "TASK_CD" );
      ResultSet lResultSet = getTaskStagingData( lTaskCode );
      String lCompletionValue = aMapTaskFirstTimeSched.get( "COMPLETION_VALUE" );
      lResultSet.first();

      String lLastDueValue = lResultSet.getString( "last_due_value" );
      Assert.assertTrue( "The expected last due value should be null.",
            StringUtils.isBlank( lLastDueValue ) );

   }


   private ResultSet getTaskStagingData( String aTaskCode ) throws SQLException {
      StringBuilder lTaskDataQuery = new StringBuilder();
      lTaskDataQuery.append( "SELECT * FROM " );
      lTaskDataQuery.append( TableUtil.AL_PROC_TASKS_PARAMETER );
      lTaskDataQuery.append( " WHERE " );
      lTaskDataQuery.append( "task_cd = " + aTaskCode );

      return runQuery( lTaskDataQuery.toString() );
   }

}
