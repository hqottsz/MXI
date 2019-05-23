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

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mxi.mx.util.ActualsLoaderTest;
import com.mxi.mx.util.TableUtil;


/**
 *
 *
 * @author Alicia Qian
 */
public class ValidateTaskDefnsTest extends ActualsLoaderTest {

   /**
    * Clean up after each individual test
    *
    * @throws Exception
    */
   @Override
   @After
   public void after() throws Exception {

      // clear import results from all tables
      clearMxTestData();

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
    * This test error code AL-TASK-125: The task cannot be loaded through the loading tools as the
    * task_cd is related to multiple SYS config slots on the assembly
    *
    * @throws Exception
    *
    *
    */
   @Test
   public void test_AL_TASK_125_1() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'DUP-ROOT-SYS'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "COMPLETION_DUE_DT", "null" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_EXCEPTWARNING( "AL-TASK-125" );

   }


   /**
    * This test error code AL-TASK-125: The task cannot be loaded through the loading tools as the
    * task_cd is related to multiple SYS config slots on the assembly
    *
    * @throws Exception
    *
    *
    */
   @Test
   public void test_AL_TASK_125_2() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'DUP-SYS'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "COMPLETION_DUE_DT", "null" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_EXCEPTWARNING( "AL-TASK-125" );

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
    * <li>AL-TASK-024 validation failure is found - Task definition does not exist</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */
   @Test
   public void test_AL_TASK_024_TaskDefExists() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'DOESNOTEXIST'" ); // value not found in Mx
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // call express validation runner and result checker with expected error code
      runValidateTasks();
      checkTaskValidation_WITHWARNING( "AL-TASK-024" );
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
    * <li>AL-TASK-025 validation failure is found - Task is in build or is obsolete</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */

   @Test
   public void test_AL_TASK_025_ActiveObsolete() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'AL_BUILD'" ); // task in build status
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // call express validation runner and result checker with expected error code
      runValidateTasks();
      checkTaskValidation_WITHWARNING( "AL-TASK-025" );
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
    * <li>AL-TASK-026 validation failure is found - Task is not a REQ or BLOCK</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */
   @Test
   public void test_AL_TASK_026_TaskTypeREQOrBLOCK() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'SYS-JIC-10'" ); // Job card task
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // call express validation runner and result checker with expected error code
      runValidateTasks();
      checkTaskValidation_WITHWARNING( "AL-TASK-026" );
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
    * <li>Error Code: AL-TASK-028</li>
    * <li>The provided TASK_CD's task class cannot be a replacement (REPL) or corrective (CORR)</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AL_TASK_028_CORRTask() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" ); // serial number that does not exist
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'SYS-CORR'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "COMPLETION_DUE_DT", "null" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // call express validation runner and result checker with expected error code
      runValidateTasks();
      checkTaskValidation_WITHWARNING( "AL-TASK-028" );
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
    * <li>Error Code: AL-TASK-028</li>
    * <li>The provided TASK_CD's task class cannot be a replacement (REPL) or corrective (CORR)</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AL_TASK_028_REPLTask() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" ); // serial number that does not exist
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'ACFT-SYS-1-1-TRK-P1-REPL'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "COMPLETION_DUE_DT", "null" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // call express validation runner and result checker with expected error code
      runValidateTasks();
      checkTaskValidation_WITHWARNING( "AL-TASK-028" );
   }


   /**
    * Execute the task definition validation.
    *
    * @param aRecordId
    *
    * @return int
    *
    * @throws SQLException
    */
   private int checkTaskDefnDetails( String aRecordId ) throws SQLException {

      CallableStatement lPreparedCall = getConnection()
            .prepareCall( "BEGIN mx_al_task_pkg.check_invalid_task_defn_dtls(?); END;" );

      lPreparedCall.setObject( 1, aRecordId );

      int lExecuteUpdate = lPreparedCall.executeUpdate();

      getConnection().commit();

      return lExecuteUpdate;
   }


   /**
    * Execute the task definition validation.
    *
    * @param aRecordId
    *
    * @return int
    *
    * @throws SQLException
    */
   private int executeTaskDefnValidation( String aRecordId ) throws SQLException {

      CallableStatement lPreparedCall =
            getConnection().prepareCall( "BEGIN mx_al_task_pkg.validate_task_defn(?); END;" );

      lPreparedCall.setObject( 1, aRecordId );

      int lExecuteUpdate = lPreparedCall.executeUpdate();

      getConnection().commit();

      return lExecuteUpdate;
   }


   /**
    * Gets result set from AL_PROC_TASKS_ERROR table
    *
    * @param aCriteria
    *
    * @return ResultSet
    *
    * @throws SQLException
    */

   private ResultSet getErrorData( String aCriteria ) throws SQLException {
      return runQuery( "SELECT * FROM al_proc_tasks_error WHERE error_cd = '" + aCriteria + "'" );
   }


   /**
    * Inserts into AL_PROC_HIST_TASK_PARAMETER table
    *
    * @param lRecordId
    * @param aUsageType
    * @param aTaskCode
    *
    * @return ResultSet
    *
    * @throws SQLException
    */

   private PreparedStatement getInsertProcHistTaskParamStatement( int lRecordId, String aUsageType,
         String aTaskCode ) throws SQLException {
      PreparedStatement lPrepStatement =
            getConnection().prepareStatement( TableUtil.getInsertProcHistTaskParameterString() );

      lPrepStatement.setObject( 1, lRecordId );
      lPrepStatement.setObject( 2, lRecordId );
      lPrepStatement.setObject( 3, Integer.toString( lRecordId ) );
      lPrepStatement.setString( 4, "SN000001" );
      lPrepStatement.setString( 5, "ACFT_ASSY_PN1" );
      lPrepStatement.setString( 6, "00001" );
      lPrepStatement.setString( 7, aTaskCode );
      lPrepStatement.setDate( 8, new Date( System.currentTimeMillis() ) );
      lPrepStatement.setString( 9, "" );
      lPrepStatement.setDate( 10, new Date( System.currentTimeMillis() ) );
      lPrepStatement.setString( 11, aUsageType );
      lPrepStatement.setFloat( 12, 50 );
      lPrepStatement.setFloat( 13, 50f );
      lPrepStatement.setNull( 14, Types.NULL );
      lPrepStatement.setNull( 15, Types.NULL );

      return lPrepStatement;
   }


   /**
    * Inserts into AL_PROC_HIST_TASK_PARAMETER table
    *
    * @param aRecordId
    * @param aTaskCode
    * @param aAssemblyDbId
    * @param aAssemblyCode
    * @param aAssmblBomId
    *
    * @return ResultSet
    *
    * @throws SQLException
    */

   private PreparedStatement getInsertProcHistTaskStatement( int aRecordId, String aTaskCode,
         int aAssemblyDbId, String aAssemblyCode, int aAssmblBomId ) throws SQLException {

      PreparedStatement lPrepStatement =
            getConnection().prepareStatement( TableUtil.getInsertProcHistTaskString() );

      lPrepStatement.setObject( 1, aRecordId );
      lPrepStatement.setObject( 2, Integer.toString( aRecordId ) );
      lPrepStatement.setString( 3, "SN000001" );
      lPrepStatement.setString( 4, "ACFT_ASSY_PN1" );
      lPrepStatement.setString( 5, "00001" );
      lPrepStatement.setString( 6, aTaskCode );
      lPrepStatement.setDate( 7, new Date( System.currentTimeMillis() ) );
      lPrepStatement.setString( 8, "" );
      lPrepStatement.setInt( 9, 4650 );
      lPrepStatement.setInt( 10, 100000 );
      lPrepStatement.setInt( 11, 4650 );
      lPrepStatement.setInt( 12, 100000 );
      lPrepStatement.setInt( 13, aAssemblyDbId );
      lPrepStatement.setInt( 14, aAssmblBomId );
      lPrepStatement.setString( 15, aAssemblyCode );
      lPrepStatement.setInt( 16, 4650 );
      lPrepStatement.setInt( 17, 100000 );
      lPrepStatement.setString( 18, "ACFT_CD1" );
      lPrepStatement.setString( 19, "001" );
      lPrepStatement.setInt( 20, 4650 );
      lPrepStatement.setInt( 21, 100463 );
      lPrepStatement.setNull( 22, Types.NUMERIC );
      lPrepStatement.setNull( 23, Types.NUMERIC );
      lPrepStatement.setString( 24, "" );
      lPrepStatement.setString( 25, "" );
      lPrepStatement.setNull( 26, Types.NULL );
      lPrepStatement.setNull( 27, Types.NULL );

      return lPrepStatement;
   }


   /**
    * Inserts into AL_PROC_TASKS_ERROR table
    *
    * @param aRecordId
    *
    * @return ResultSet
    *
    * @throws SQLException
    */

   private PreparedStatement getInsertProcTaskErrorStatement( int aRecordId ) throws SQLException {
      PreparedStatement lPreparedStatement =
            getConnection().prepareStatement( TableUtil.getInsertProcTaskErrorString() );

      lPreparedStatement.setObject( 1, Integer.toString( aRecordId ) );
      lPreparedStatement.setObject( 2, aRecordId );
      lPreparedStatement.setObject( 3, Integer.toString( aRecordId ) );
      lPreparedStatement.setString( 4, "AL-TASK-001" );
      lPreparedStatement.setString( 5, "AL-BIZ-1003" );

      return lPreparedStatement;
   }


   /**
    * Insert all the data without usage parameters into the tables directly allowing us to bypass
    * all other validations and focus on our validation.
    *
    * @param aRecordId
    * @param aTaskCode
    * @param aAssemblyDbId
    *           DOCUMENT_ME
    * @param aAssemblyCode
    * @param aAssmblBomId
    *
    * @throws SQLException
    */
   private void insertTaskData( int aRecordId, String aTaskCode, int aAssemblyDbId,
         String aAssemblyCode, int aAssmblBomId ) throws SQLException {

      // al_proc_hist_task
      PreparedStatement lInsertProcHistTaskStatement = getInsertProcHistTaskStatement( aRecordId,
            aTaskCode, aAssemblyDbId, aAssemblyCode, aAssmblBomId );
      lInsertProcHistTaskStatement.executeUpdate();
   }


   /**
    * Insert all the task data with usage parameters into the tables directly allowing us to bypass
    * all other validations and focus on our validation.
    *
    * @param aRecordId
    * @param aUsageType
    * @param aTaskCode
    * @param aAssemblyDbId
    * @param aAssemblyCode
    * @param aAssmblBomId
    *
    * @throws SQLException
    */
   private void insertTaskDataWithoutUsageParams( int aRecordId, String aUsageType,
         String aTaskCode, int aAssemblyDbId, String aAssemblyCode, int aAssmblBomId )
         throws SQLException {

      // al_proc_tasks_error
      PreparedStatement lInsertProcTaskErrorStatement =
            getInsertProcTaskErrorStatement( aRecordId );
      lInsertProcTaskErrorStatement.executeUpdate();

      // al_proc_hist_task
      PreparedStatement lInsertProcHistTaskStatement = getInsertProcHistTaskStatement( aRecordId,
            aTaskCode, aAssemblyDbId, aAssemblyCode, aAssmblBomId );
      lInsertProcHistTaskStatement.executeUpdate();
   }


   /**
    * Insert all the task data with usage parameters into the tables directly allowing us to bypass
    * all other validations and focus on our validation.
    *
    * @param aRecordId
    * @param aUsageType
    * @param aTaskCode
    * @param aAssemblyDbId
    * @param aAssemblyCode
    * @param aAssmblBomId
    *
    * @throws SQLException
    */
   private void insertTaskDataWithUsageParams( int aRecordId, String aUsageType, String aTaskCode,
         int aAssemblyDbId, String aAssemblyCode, int aAssmblBomId ) throws SQLException {

      // al_proc_tasks_error
      PreparedStatement lInsertProcTaskErrorStatement =
            getInsertProcTaskErrorStatement( aRecordId );
      lInsertProcTaskErrorStatement.executeUpdate();

      // al_proc_hist_task
      PreparedStatement lInsertProcHistTaskStatement = getInsertProcHistTaskStatement( aRecordId,
            aTaskCode, aAssemblyDbId, aAssemblyCode, aAssmblBomId );
      lInsertProcHistTaskStatement.executeUpdate();

      // al_proc_hist_task_parameter
      PreparedStatement lInsertProcHistTaskParamStatement =
            getInsertProcHistTaskParamStatement( aRecordId, aUsageType, aTaskCode );
      lInsertProcHistTaskParamStatement.executeUpdate();
   }
}
