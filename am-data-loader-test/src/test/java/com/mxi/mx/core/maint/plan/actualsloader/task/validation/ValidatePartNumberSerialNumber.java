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
 * This test suite contains unit tests for Actuals Loader task validations for Part Numbers and
 * Serial Numbers
 *
 * @author Andrew Bruce, Alicia Qian
 */

public class ValidatePartNumberSerialNumber extends ActualsLoaderTest {

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
    * <li>Error Code: AL-TASK-010</li>
    * <li>Serial Number is Mandatory for a historic task.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AL_TASK_010_MandatorySerialNo() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // lMapTask.put( "SERIAL_NO_OEM", "'99UNKNOWN99'" ); // serial number not provided
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
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // lMapTaskSched.put( "SERIAL_NO_OEM", "'99UNKNOWN99'" ); // serial number not provided
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "1" );
      lMapTaskSched.put( "COMPLETION_DUE_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // call express validation runner and result checker with expected error code
      runValidateTasks();
      checkTaskValidation_WITHWARNING( "AL-TASK-010" );

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
    * <li>Error Code: AL-TASK-011</li>
    * <li>Part Number is Mandatory for a historic task.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AL_TASK_011_MandatoryPartNo() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );

      // lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" ); // part number not provided
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'AL_TASK'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "COMPLETION_DUE_DT", "null" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000001'" );

      // lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" ); // part number not provided
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "1" );
      lMapTaskSched.put( "COMPLETION_DUE_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // call express validation runner and result checker with expected error code
      runValidateTasks();
      checkTaskValidation_WITHWARNING( "AL-TASK-011" );

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
    * <li>Error Code: AL-TASK-017</li>
    * <li>The provided PART_NO_OEM/SERIAL_NO_OEM does not exist in Maintenix or in the staging
    * table.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AL_TASK_017_UnknownPartNoSerialNoCombination() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();
      lMapTask.put( "SERIAL_NO_OEM", "'99UNKNOWN99'" ); // serial number that does not exist
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
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();
      lMapTaskSched.put( "SERIAL_NO_OEM", "'99UNKNOWN99'" ); // serial number that does not exist
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "1" );
      lMapTaskSched.put( "COMPLETION_DUE_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // call express validation runner and result checker with expected error code
      runValidateTasks();
      checkTaskValidation_WITHWARNING( "AL-TASK-017" );
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
    * <li>Error Code: AL-TASK-018</li>
    * <li>The provided PART_NO_OEM/SERIAL_NO_OEM is locked.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AL_TASK_018_InventoryLocked() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();
      lMapTask.put( "SERIAL_NO_OEM", "'SN-LOCKED'" ); // serial number of locked aircraft
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'AL_TASK'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN-LOCKED'" ); // serial number of locked aircraft
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "1" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // call express validation runner and result checker with expected error code
      runValidateTasks();
      checkTaskValidation_WITHWARNING( "AL-TASK-018" );
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
    * <li>Error Code: AL-TASK-019</li>
    * <li>The provided PART_NO_OEM/SERIAL_NO_OEM should not be in status CONDEMN, ARCHIVE or
    * SCRAP.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AL_TASK_019_InventoryArchived() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();
      lMapTask.put( "SERIAL_NO_OEM", "'SN000013'" ); // serial number of archived aircraft
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'AL_TASK'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000013'" ); // serial number of archived aircraft
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "1" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // call express validation runner and result checker with expected error code
      runValidateTasks();
      checkTaskValidation_WITHWARNING( "AL-TASK-019" );

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
    * <li>Error Code: AL-TASK-019</li>
    * <li>The provided PART_NO_OEM/SERIAL_NO_OEM should not be in status CONDEMN, ARCHIVE or
    * SCRAP.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AL_TASK_019_InventoryCondemned() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();
      lMapTask.put( "SERIAL_NO_OEM", "'SN000016'" ); // serial number that does not exist
      lMapTask.put( "PART_NO_OEM", "'A0000001'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'REQ1'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // call express validation runner and result checker with expected error code
      runValidateTasks();
      checkTaskValidation_WITHWARNING( "AL-TASK-019" );
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
    * <li>Error Code: AL-TASK-019</li>
    * <li>The provided PART_NO_OEM/SERIAL_NO_OEM should not be in status CONDEMN, ARCHIVE or
    * SCRAP.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AL_TASK_019_InventoryScrapped() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();
      lMapTask.put( "SERIAL_NO_OEM", "'SN000012'" ); // serial number of scrapped aircraft
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'AL_TASK'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000012'" ); // serial number of scrapped aircraft
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "1" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // call express validation runner and result checker with expected error code
      runValidateTasks();
      checkTaskValidation_WITHWARNING( "AL-TASK-019" );
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
    * <li>Error Code: AL-TASK-020</li>
    * <li>The provided SERIAL_NO_OEM cannot be XXX.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AL_TASK_020_SerialNoXXX() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();
      lMapTask.put( "SERIAL_NO_OEM", "'XXX'" ); // serial number = XXX
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'AL_TASK'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();
      lMapTaskSched.put( "SERIAL_NO_OEM", "'XXX'" ); // serial number = XXX
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "1" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // call express validation runner and result checker with expected error code
      runValidateTasks();
      checkTaskValidation_WITHWARNING( "AL-TASK-020" );
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
    * <li>Error Code: AL-TASK-021</li>
    * <li>The provided PART_NO_OEM does not exist in Maintenix.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AL_TASK_021_UnknownPartNo() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'XXACFT_ASSY_PN1XX'" ); // part number that does not exist
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'AL_TASK'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskSched.put( "PART_NO_OEM", "'XXACFT_ASSY_PN1XX'" ); // part number that does not exist
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'AL_TASK'" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "1" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // call express validation runner and result checker with expected error code
      runValidateTasks();
      checkTaskValidation_WITHWARNING( "AL-TASK-021" );
   }
}
