/*
 * Confidential, proprietary and/or trade secret information of Mxi Technologies.
 *
 * Copyright 2000-2016 Mxi Technologies. All Rights Reserved.
 *
 * Except as expressly provided by written license signed by a duly appointed officer of Mxi
 * Technologies, any disclosure, distribution, reproduction, compilation, modification, creation of
 * derivative works and/or other use of the Mxi source code is strictly prohibited. Inclusion of a
 * copyright notice shall not be taken to indicate that the source code has been published.
 */

package com.mxi.mx.core.maint.plan.actualsloader.inventory.imports;

import static org.hamcrest.MatcherAssert.assertThat;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mxi.mx.util.TableUtil;


/**
 * This test suite contains all test cases for Actuals Loader loading of inventory where the serial
 * number of AL_XXX_* is converted to XXX after running a script.
 *
 * @author Suzanne Michal
 */

public class FinalizeInventoryTest extends AbstractImportInventory {

   private static final String CYCLE_ID = "F7F61D34F8DD11E498141955789ABCDE";
   private static final String RECORD_ID = "1234567891";


   /**
    * Clean up after each individual test
    *
    * @throws Exception
    */
   @After
   @Override
   public void after() throws Exception {

      // clean up the event data
      clearMxTestData();

      super.after();
   }


   /**
    * Setup before executing each individual test
    *
    * @throws Exception
    */
   @Before
   @Override
   public void before() throws Exception {

      super.before();
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>The part numbers exists in Maintenix</li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ol>
    * <li>Insert an unknown inventory (indicated by temporary serial number AL_XXX_*) into the
    * processing tables</li>
    * <li>Run Inventory Import</li>
    * <li>Run a script to finalize the inventory (convert the AL_XXX_ numbers to XXX)</li>
    * <li>Check that the inventory's serial number has been changed to XXX</li>
    * </ol>
    * </p>
    *
    * <p>
    *
    * <ul>
    * <li>Error Code: NONE</li>
    * <li>The inventory exists in Maintenix and the SN starting with AL_XXX_ has been converted to
    * XXX</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */

   @Test
   public void testUnknownInvImportsSuccessfully() throws Exception {

      int lRandom = getRandom();
      String lParentAcftSerialNo = "TTT-PARENT" + lRandom;
      String lTRKSerialNo = "AL_XXX_" + lRandom;
      String lACFTRegCd = "AC" + lRandom;

      // create inventory map for parent aircraft
      Map<String, String> lParent = new LinkedHashMap<String, String>();
      lParent.put( "SERIAL_NO_OEM", "'" + lParentAcftSerialNo + "'" );
      lParent.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lParent.put( "INV_CLASS_CD", "'ACFT'" );
      lParent.put( "MANUFACT_CD", "'10001'" );
      lParent.put( "LOC_CD", "'OPS'" );
      lParent.put( "INV_COND_CD", "'REPREQ'" );
      lParent.put( "AC_REG_CD", "'" + lACFTRegCd + "'" );
      lParent.put( "INV_OPER_CD", "'NORM'" );
      lParent.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lParent.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lParent.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // parent Acft into C_RI_INVENTORY
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lParent ) );

      // create inventory map for the original subcomponent.
      Map<String, String> lMapSubInventoryALXXX = new LinkedHashMap<String, String>();
      lMapSubInventoryALXXX.put( "PARENT_SERIAL_NO_OEM", "'" + lParentAcftSerialNo + "'" );
      lMapSubInventoryALXXX.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapSubInventoryALXXX.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapSubInventoryALXXX.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-P1'" );
      lMapSubInventoryALXXX.put( "SERIAL_NO_OEM", "'" + lTRKSerialNo + "'" );
      lMapSubInventoryALXXX.put( "PART_NO_OEM", "'A0000001'" );
      lMapSubInventoryALXXX.put( "MANUFACT_CD", "'10001'" );
      lMapSubInventoryALXXX.put( "INV_CLASS_CD", "'TRK'" );
      lMapSubInventoryALXXX.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert only the original one with AL_XXX_ in it
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB,
            lMapSubInventoryALXXX ) );

      // assumption: no need to put anything in C_RI_ATTACH (no APUs)

      // create inventory usage map for ACFT
      Map<String, String> lInventoryUsage = new LinkedHashMap<String, String>();
      lInventoryUsage.put( "SERIAL_NO_OEM", "'" + lParentAcftSerialNo + "'" );
      lInventoryUsage.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lInventoryUsage.put( "MANUFACT_CD", "'10001'" );
      lInventoryUsage.put( "DATA_TYPE_CD", "'USAGE2'" );
      lInventoryUsage.put( "TSN_QT", "0" );
      lInventoryUsage.put( "TSO_QT", "0" );
      lInventoryUsage.put( "TSI_QT", "0" );
      // insert usage map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE, lInventoryUsage ) );
      // insert row for each usage parm
      lInventoryUsage.put( "DATA_TYPE_CD", "'CYCLES'" );
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE, lInventoryUsage ) );
      lInventoryUsage.put( "DATA_TYPE_CD", "'USAGE1'" );
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE, lInventoryUsage ) );
      lInventoryUsage.put( "DATA_TYPE_CD", "'HOURS'" );
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE, lInventoryUsage ) );
      lInventoryUsage.put( "DATA_TYPE_CD", "'USAGE3'" );
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE, lInventoryUsage ) );

      // create subinventory usage map
      Map<String, String> lSubInventoryUsage = new LinkedHashMap<String, String>();
      lSubInventoryUsage.put( "SERIAL_NO_OEM", "'" + lTRKSerialNo + "'" );
      lSubInventoryUsage.put( "PART_NO_OEM", "'A0000001'" );
      lSubInventoryUsage.put( "MANUFACT_CD", "'10001'" );
      lSubInventoryUsage.put( "DATA_TYPE_CD", "'USAGE3'" );
      lSubInventoryUsage.put( "TSN_QT", "0" );
      lSubInventoryUsage.put( "TSO_QT", "0" );
      lSubInventoryUsage.put( "TSI_QT", "0" );

      // insert usage map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lSubInventoryUsage ) );
      // insert row for each usage parm
      lSubInventoryUsage.put( "DATA_TYPE_CD", "'HOURS'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lSubInventoryUsage ) );
      lSubInventoryUsage.put( "DATA_TYPE_CD", "'USAGE2'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lSubInventoryUsage ) );
      lSubInventoryUsage.put( "DATA_TYPE_CD", "'USAGE1'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lSubInventoryUsage ) );
      lSubInventoryUsage.put( "DATA_TYPE_CD", "'CYCLES'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lSubInventoryUsage ) );

      // call inventory validation & import
      validateAndCheckInventory( "testALXXXImportsSuccessfully", "PASS" );
      importInventory();

      // first check for the existence of the AL_XXX_ sub-inventory item
      checkInventoryDetails( lParent );
      lMapSubInventoryALXXX.put( "INV_COND_CD", "'INSRV'" );
      checkSubInventoryDetails( lMapSubInventoryALXXX );
      String lInvNoDbId = runQuery(
            "Select * from INV_INV where SERIAL_NO_OEM = '" + lTRKSerialNo + "'", "INV_NO_DB_ID" );
      // get rid of trailing comma
      lInvNoDbId = removeTrailingComma( lInvNoDbId );

      String lInvNoId = runQuery(
            "Select * from INV_INV where SERIAL_NO_OEM = '" + lTRKSerialNo + "'", "INV_NO_ID" );
      // get rid of trailing comma
      lInvNoId = removeTrailingComma( lInvNoId );

      // run the conversion script d. finalize_inventory.bat
      runFinalizeInventory();

      // verify that import inventory was updated as expected
      String lLongQuery = "Select * from INV_INV where INV_NO_DB_ID = '" + lInvNoDbId
            + "' AND INV_NO_ID = '" + lInvNoId + "' ";

      String lSerialNoOem = runQuery( lLongQuery, "SERIAL_NO_OEM" );

      // get rid of trailing comma
      lSerialNoOem = removeTrailingComma( lSerialNoOem );

      // confirm that AL_XXX* was converted to XXX for this inventory
      assertThat( "AL_XXX_ was not converted to XXX for this subinventory item.",
            lSerialNoOem.equals( "XXX" ) );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>The part numbers exists in Maintenix</li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ol>
    * <li>Insert an unknown inventory (indicated by temporary serial number AL_XXX_*) into the
    * processing tables</li>
    * <li>Run Inventory Import</li>
    * <li>Insert a task for that unknown inventory
    * <li>Validate and Import the task
    * <li>Run a script to finalize the inventory (convert the AL_XXX_ numbers to XXX)</li>
    * <li>Check that the inventory's serial number has been changed to XXX</li>
    * </ol>
    * </p>
    *
    * <p>
    *
    * <ul>
    * <li>Error Code: NONE</li>
    * <li>The inventory exists in Maintenix and the SN starting with AL_XXX_ has been converted to
    * XXX</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */

   @Test
   public void testUnknownInvImportsSuccessfullyWithTask() throws Exception {

      int lRandom = getRandom();
      String lParentAcftSerialNo = "TTT-PARENT" + lRandom;
      String lTRKSerialNo = "AL_XXX_" + lRandom;
      String lACFTRegCd = "AC" + lRandom;
      int iEventDbId;
      int iEventId;

      // create inventory map for parent aircraft
      Map<String, String> lParent = new LinkedHashMap<String, String>();
      lParent.put( "SERIAL_NO_OEM", "'" + lParentAcftSerialNo + "'" );
      lParent.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lParent.put( "INV_CLASS_CD", "'ACFT'" );
      lParent.put( "MANUFACT_CD", "'10001'" );
      lParent.put( "LOC_CD", "'OPS'" );
      lParent.put( "INV_COND_CD", "'REPREQ'" );
      lParent.put( "AC_REG_CD", "'" + lACFTRegCd + "'" );
      lParent.put( "INV_OPER_CD", "'NORM'" );
      lParent.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lParent.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lParent.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // parent Acft into C_RI_INVENTORY
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lParent ) );

      // create inventory map for the original subcomponent.
      Map<String, String> lMapSubInventoryALXXX = new LinkedHashMap<String, String>();
      lMapSubInventoryALXXX.put( "PARENT_SERIAL_NO_OEM", "'" + lParentAcftSerialNo + "'" );
      lMapSubInventoryALXXX.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapSubInventoryALXXX.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapSubInventoryALXXX.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-P1'" );
      lMapSubInventoryALXXX.put( "SERIAL_NO_OEM", "'" + lTRKSerialNo + "'" );
      lMapSubInventoryALXXX.put( "PART_NO_OEM", "'A0000001'" );
      lMapSubInventoryALXXX.put( "MANUFACT_CD", "'10001'" );
      lMapSubInventoryALXXX.put( "INV_CLASS_CD", "'TRK'" );
      lMapSubInventoryALXXX.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert only the original one with AL_XXX_ in it
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB,
            lMapSubInventoryALXXX ) );

      // assumption: no need to put anything in C_RI_ATTACH (no APUs)

      // create inventory usage map for ACFT
      Map<String, String> lInventoryUsage = new LinkedHashMap<String, String>();
      lInventoryUsage.put( "SERIAL_NO_OEM", "'" + lParentAcftSerialNo + "'" );
      lInventoryUsage.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lInventoryUsage.put( "MANUFACT_CD", "'10001'" );
      lInventoryUsage.put( "DATA_TYPE_CD", "'USAGE2'" );
      lInventoryUsage.put( "TSN_QT", "0" );
      lInventoryUsage.put( "TSO_QT", "0" );
      lInventoryUsage.put( "TSI_QT", "0" );

      // insert usage map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE, lInventoryUsage ) );

      // insert row for each usage parm
      lInventoryUsage.put( "DATA_TYPE_CD", "'CYCLES'" );
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE, lInventoryUsage ) );
      lInventoryUsage.put( "DATA_TYPE_CD", "'USAGE1'" );
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE, lInventoryUsage ) );
      lInventoryUsage.put( "DATA_TYPE_CD", "'HOURS'" );
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE, lInventoryUsage ) );
      lInventoryUsage.put( "DATA_TYPE_CD", "'USAGE3'" );
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE, lInventoryUsage ) );

      // create subinventory usage map
      Map<String, String> lSubInventoryUsage = new LinkedHashMap<String, String>();
      lSubInventoryUsage.put( "SERIAL_NO_OEM", "'" + lTRKSerialNo + "'" );
      lSubInventoryUsage.put( "PART_NO_OEM", "'A0000001'" );
      lSubInventoryUsage.put( "MANUFACT_CD", "'10001'" );
      lSubInventoryUsage.put( "DATA_TYPE_CD", "'USAGE3'" );
      lSubInventoryUsage.put( "TSN_QT", "0" );
      lSubInventoryUsage.put( "TSO_QT", "0" );
      lSubInventoryUsage.put( "TSI_QT", "0" );

      // insert usage map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lSubInventoryUsage ) );
      // insert row for each usage parm
      lSubInventoryUsage.put( "DATA_TYPE_CD", "'HOURS'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lSubInventoryUsage ) );
      lSubInventoryUsage.put( "DATA_TYPE_CD", "'USAGE2'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lSubInventoryUsage ) );
      lSubInventoryUsage.put( "DATA_TYPE_CD", "'USAGE1'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lSubInventoryUsage ) );
      lSubInventoryUsage.put( "DATA_TYPE_CD", "'CYCLES'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lSubInventoryUsage ) );

      // call inventory validation & import
      validateAndCheckInventory( "testALXXXImportsSuccessfullyWithTask inventory", "PASS" );
      importInventory();

      // first check for the existence of the AL_XXX_ sub-inventory item
      checkInventoryDetails( lParent );

      // get the inventory key details of the parent ACFT
      String lParentInvNoDbId =
            runQuery( "Select * from INV_INV where SERIAL_NO_OEM = '" + lParentAcftSerialNo + "'",
                  "INV_NO_DB_ID" );
      // get rid of trailing comma
      lParentInvNoDbId = removeTrailingComma( lParentInvNoDbId );
      String lParentInvNoId =
            runQuery( "Select * from INV_INV where SERIAL_NO_OEM = '" + lParentAcftSerialNo + "'",
                  "INV_NO_ID" );
      lParentInvNoId = removeTrailingComma( lParentInvNoId );
      lMapSubInventoryALXXX.put( "INV_COND_CD", "'INSRV'" );
      checkSubInventoryDetails( lMapSubInventoryALXXX );

      // get inventory key details of the tracked part
      String lInvNoDbId = runQuery(
            "Select * from INV_INV where SERIAL_NO_OEM = '" + lTRKSerialNo + "'", "INV_NO_DB_ID" );
      // get rid of trailing comma
      lInvNoDbId = removeTrailingComma( lInvNoDbId );

      String lInvNoId = runQuery(
            "Select * from INV_INV where SERIAL_NO_OEM = '" + lTRKSerialNo + "'", "INV_NO_ID" );
      // get rid of trailing comma
      lInvNoId = removeTrailingComma( lInvNoId );

      // insert a task directly into AL_PROC_TASKS for the aircraft
      // note: we are bypassing validation here
      String lTaskCode = "1-TIME NON-RECURRING";

      Map<String, String> lMapTaskData = new LinkedHashMap<String, String>();
      lMapTaskData.put( "SERIAL_NO_OEM", "'" + lParentAcftSerialNo + "'" );
      lMapTaskData.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskData.put( "MANUFACT_CD", "'10001'" );
      lMapTaskData.put( "TASK_CD", "'" + lTaskCode + "'" );
      lMapTaskData.put( "FIRST_TIME_BOOL", "1" );
      lMapTaskData.put( "CYCLE_ID", "'" + CYCLE_ID + "'" );
      lMapTaskData.put( "RECORD_ID", "'" + RECORD_ID + "'" );
      lMapTaskData.put( "INV_NO_DB_ID", lParentInvNoDbId );
      lMapTaskData.put( "INV_NO_ID", lParentInvNoId );
      lMapTaskData.put( "ACFT_NO_DB_ID", "4650" );
      lMapTaskData.put( "ACFT_NO_ID", "100000" ); // ???
      lMapTaskData.put( "PART_NO_DB_ID", "4650" );
      lMapTaskData.put( "PART_NO_ID", "100000" ); // ??
      lMapTaskData.put( "REFDOC_BOOL", "0" );
      addTaskIdsToMap( lTaskCode, lMapTaskData );

      // insert map
      String lInsertStatement =
            TableUtil.getInsertForTableByMap( lMapTaskData, TableUtil.AL_PROC_TASKS );

      // insert into database
      runInsert( lInsertStatement );

      // execute the import procedure which should insert the tasks into maintenix and zip
      runImportTasks( CYCLE_ID, 0 );

      // check if the task got imported
      // get the generated id's needed to query maintenix tables
      String lQuery =
            "SELECT * FROM " + TableUtil.AL_PROC_TASKS + " inner join " + TableUtil.EVT_EVENT
                  + " on ACTV_EVENT_DB_ID = EVENT_DB_ID AND ACTV_EVENT_ID = EVENT_ID";
      ResultSet lResultSet = runQuery( lQuery );

      assertThat( "Query returned no results: ", lResultSet != null );
      assertThat( "Expected results but there were none.", lResultSet.first() == true );
      iEventDbId = lResultSet.getInt( "actv_event_db_id" );
      iEventId = lResultSet.getInt( "actv_event_id" );

      // assert that the active task was created
      lResultSet = getEventData( iEventDbId, iEventId );
      assertThat( "Query returned no results: ", lResultSet != null );
      assertThat( "There are no results", lResultSet.first() == true );

      // run the conversion script d. finalize_inventory.bat
      runFinalizeInventory();

      // verify that import inventory was updated as expected
      String lLongQuery = "Select * from INV_INV where INV_NO_DB_ID = '" + lInvNoDbId
            + "' AND INV_NO_ID = '" + lInvNoId + "' ";
      String lSerialNoOem = runQuery( lLongQuery, "SERIAL_NO_OEM" );
      // get rid of trailing comma
      lSerialNoOem = removeTrailingComma( lSerialNoOem );

      // confirm that AL_XXX* was converted to XXX for this inventory
      assertThat( "AL_XXX_ was not converted to XXX for this subinventory item.",
            lSerialNoOem.equals( "XXX" ) );
   }


   /**
    * Run the d.finalize_inventory.bat capability to convert sub inventory with SN AL_XXX_ to XXX
    *
    * @throws SQLException
    */
   protected void runFinalizeInventory() throws SQLException {

      CallableStatement lPrepareCallTask =
            getConnection().prepareCall( "BEGIN AL_FINALIZE_INVENTORY; END;" );

      lPrepareCallTask.execute();
   }


   /**
    * This function can be used in conjunction with runQuery. The runQuery() returns a string with a
    * trailing comma on it. This function will strip out the comma.
    *
    * @throws SQLException
    */
   public String removeTrailingComma( String aQuery ) {
      String lQuery;
      int length = aQuery.length();
      lQuery = aQuery.substring( 0, length - 1 );
      return lQuery;
   }
}
