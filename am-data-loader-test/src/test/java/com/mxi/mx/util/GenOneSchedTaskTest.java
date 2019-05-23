package com.mxi.mx.util;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import com.mxi.mx.core.maint.plan.datamodels.assmbleInfor;
import com.mxi.mx.core.maint.plan.datamodels.inventoryData;
import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;


/**
 * This test suite contains common test utilities forGenOneSchedTask tests and data setup for each
 * test cases.
 *
 * @author Alicia Qian
 */
public class GenOneSchedTaskTest extends AbstractDatabaseConnection {

   /**
    * This function is to retrieve inventory record
    *
    * @param: strAssembly
    *            to specify assembly type
    */

   public inventoryData getInventory( String astrAssembly ) {
      ResultSet lResultSetRecords;
      inventoryData linvData = null;
      StringBuilder lstrBSQL = new StringBuilder();

      // This is ACFT query string
      if ( astrAssembly.equalsIgnoreCase( "ASSY" ) || astrAssembly.equalsIgnoreCase( "ACFT" ) ) {
         lstrBSQL.append( "Select INV_NO_DB_ID," ).append( "INV_NO_ID," )
               .append( "INV_CLASS_DB_ID," ).append( "INV_CLASS_CD," ).append( "BOM_PART_DB_ID," )
               .append( "BOM_PART_ID," ).append( "PART_NO_DB_ID," ).append( "PART_NO_ID," )
               .append( "H_INV_NO_DB_ID," ).append( "H_INV_NO_ID," )
               .append( "ASSMBL_INV_NO_DB_ID," ).append( "ASSMBL_INV_NO_ID," )
               .append( "INV_COND_DB_ID," ).append( "INV_COND_CD," ).append( "ASSMBL_DB_ID," )
               .append( "ASSMBL_CD," ).append( "ASSMBL_BOM_ID," ).append( "ASSMBL_POS_ID," )
               .append( "ORIG_ASSMBL_DB_ID," ).append( "ORIG_ASSMBL_CD," ).append( "INV_NO_SDESC," )
               .append( "SERIAL_NO_OEM," ).append( "FINANCE_STATUS_CD," ).append( "APPL_EFF_CD," )
               .append( "CARRIER_DB_ID," ).append( "CARRIER_ID" ).append( " from " )
               .append( TableUtil.INV_INV ).append( " where INV_CLASS_CD='" ).append( astrAssembly )
               .append( "'" );
      }

      if ( astrAssembly.equalsIgnoreCase( "ASSY-ENG" ) ) {
         lstrBSQL.append( "Select INV_NO_DB_ID," ).append( "INV_NO_ID," )
               .append( "INV_CLASS_DB_ID," ).append( "INV_CLASS_CD," ).append( "BOM_PART_DB_ID," )
               .append( "BOM_PART_ID," ).append( "PART_NO_DB_ID," ).append( "PART_NO_ID," )
               .append( "H_INV_NO_DB_ID," ).append( "H_INV_NO_ID," )
               .append( "ASSMBL_INV_NO_DB_ID," ).append( "ASSMBL_INV_NO_ID," )
               .append( "INV_COND_DB_ID," ).append( "INV_COND_CD," ).append( "ASSMBL_DB_ID," )
               .append( "ASSMBL_CD," ).append( "ASSMBL_BOM_ID," ).append( "ASSMBL_POS_ID," )
               .append( "ORIG_ASSMBL_DB_ID," ).append( "ORIG_ASSMBL_CD," ).append( "INV_NO_SDESC," )
               .append( "SERIAL_NO_OEM," ).append( "FINANCE_STATUS_CD," ).append( "APPL_EFF_CD," )
               .append( "CARRIER_DB_ID," ).append( "CARRIER_ID" ).append( " from " )
               .append( TableUtil.INV_INV ).append( " where INV_CLASS_CD='" ).append( "ASSY" )
               .append( "'" );
      }

      // This is ASSY query string -ENG
      if ( astrAssembly.contains( "ASSY" ) && astrAssembly.contains( "ENG" ) ) {
         lstrBSQL.append( "  and ASSMBL_BOM_ID<>0 and ORIG_ASSMBL_CD like '%ENG%'" );
      }

      // This is ASSY query string - APU
      if ( astrAssembly.contains( "ASSY" ) && astrAssembly.contains( "APU" ) ) {
         lstrBSQL.append( "  and ASSMBL_BOM_ID<>0 and ORIG_ASSMBL_CD like '%APU%'" );
      }

      if ( astrAssembly.contains( "ASSY" ) && !astrAssembly.contains( "APU" )
            && !astrAssembly.contains( "ENG" ) ) {
         lstrBSQL.append( "  and ASSMBL_BOM_ID<>0" );
      }

      // This is for TRK query
      if ( astrAssembly.equalsIgnoreCase( "TRK" ) ) {
         lstrBSQL.append( " select INV_INV.INV_NO_DB_ID," ).append( "INV_INV.INV_NO_ID," )
               .append( "INV_INV.INV_CLASS_DB_ID," ).append( "INV_INV.INV_CLASS_CD," )
               .append( "INV_INV.BOM_PART_DB_ID," ).append( "INV_INV.BOM_PART_ID," )
               .append( "INV_INV.PART_NO_DB_ID," ).append( "INV_INV.PART_NO_ID," )
               .append( "INV_INV.H_INV_NO_DB_ID," ).append( "INV_INV.H_INV_NO_ID," )
               .append( "INV_INV.ASSMBL_INV_NO_DB_ID," ).append( "INV_INV.ASSMBL_INV_NO_ID," )
               .append( "INV_INV.INV_COND_DB_ID," ).append( "INV_INV.INV_COND_CD," )
               .append( "INV_INV.ASSMBL_DB_ID," ).append( "INV_INV.ASSMBL_CD," )
               .append( "INV_INV.ASSMBL_BOM_ID," ).append( "INV_INV.ASSMBL_POS_ID," )
               .append( "INV_INV.ORIG_ASSMBL_DB_ID," ).append( "INV_INV.ORIG_ASSMBL_CD," )
               .append( "INV_INV.INV_NO_SDESC," ).append( "INV_INV.SERIAL_NO_OEM," )
               .append( "INV_INV.FINANCE_STATUS_CD," ).append( "INV_INV.APPL_EFF_CD," )
               .append( "INV_INV.CARRIER_DB_ID," ).append( "INV_INV.CARRIER_ID" ).append( " from " )
               .append( TableUtil.INV_INV ).append( " INNER JOIN inv_inv h_inv_inv ON " )
               .append( "h_inv_inv.inv_no_db_id = inv_inv.h_inv_no_db_id AND " )
               .append( "h_inv_inv.inv_no_id = inv_inv.h_inv_no_id AND " )
               .append( "h_inv_inv.rstat_cd = 0 " )
               .append( " LEFT OUTER JOIN inv_inv assmbl_inv_inv ON " )
               .append( "assmbl_inv_inv.inv_no_db_id = inv_inv.assmbl_inv_no_db_id AND " )
               .append( "assmbl_inv_inv.inv_no_id = inv_inv.assmbl_inv_no_id AND " )
               .append( "assmbl_inv_inv.rstat_cd = 0 " )
               .append( " where inv_inv.inv_class_cd='TRK'" );
      }

      try {
         lResultSetRecords = runQuery( lstrBSQL.toString() );
         lResultSetRecords.next();
         linvData = new inventoryData( lResultSetRecords.getString( "INV_NO_DB_ID" ),
               lResultSetRecords.getString( "INV_NO_ID" ),
               lResultSetRecords.getString( "INV_CLASS_DB_ID" ),
               lResultSetRecords.getString( "INV_CLASS_CD" ),
               lResultSetRecords.getString( "BOM_PART_DB_ID" ),
               lResultSetRecords.getString( "BOM_PART_ID" ),
               lResultSetRecords.getString( "PART_NO_DB_ID" ),
               lResultSetRecords.getString( "PART_NO_ID" ),
               lResultSetRecords.getString( "H_INV_NO_DB_ID" ),
               lResultSetRecords.getString( "H_INV_NO_ID" ),
               lResultSetRecords.getString( "ASSMBL_INV_NO_DB_ID" ),
               lResultSetRecords.getString( "ASSMBL_INV_NO_ID" ),
               lResultSetRecords.getString( "INV_COND_DB_ID" ),
               lResultSetRecords.getString( "INV_COND_CD" ),
               lResultSetRecords.getString( "ASSMBL_DB_ID" ),
               lResultSetRecords.getString( "ASSMBL_CD" ),
               lResultSetRecords.getString( "ASSMBL_BOM_ID" ),
               lResultSetRecords.getString( "ASSMBL_POS_ID" ),
               lResultSetRecords.getString( "ORIG_ASSMBL_DB_ID" ),
               lResultSetRecords.getString( "ORIG_ASSMBL_CD" ),
               lResultSetRecords.getString( "INV_NO_SDESC" ),
               lResultSetRecords.getString( "SERIAL_NO_OEM" ),
               lResultSetRecords.getString( "FINANCE_STATUS_CD" ),
               lResultSetRecords.getString( "APPL_EFF_CD" ),
               lResultSetRecords.getString( "CARRIER_DB_ID" ),
               lResultSetRecords.getString( "CARRIER_ID" ) );

      } catch ( SQLException e ) {
         e.printStackTrace();

      }

      return linvData;

   }


   /**
    * This function is to retrieve data from MIM_DATA_TYPE table
    *
    */

   public MimData getMimDataType() {
      ResultSet lResultSetRecords;
      MimData lmimDataAdd = null;
      StringBuilder lstrBSQL = new StringBuilder();

      lstrBSQL.append(
            "select DATA_TYPE_DB_ID, DATA_TYPE_ID, ENG_UNIT_DB_ID, ENG_UNIT_CD, DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT, " )
            .append(
                  "DATA_TYPE_CD, DATA_TYPE_SDESC, DATA_TYPE_MDESC, FORECAST_BOOL from MIM_DATA_TYPE " )
            .append( "where DATA_TYPE_DB_ID=" ).append( "\'" ).append( CONS_DB_ID ).append( "\'" );

      try {
         lResultSetRecords = runQuery( lstrBSQL.toString() );
         lResultSetRecords.next();

         lmimDataAdd = new MimData( lResultSetRecords.getString( "DATA_TYPE_DB_ID" ),
               lResultSetRecords.getString( "DATA_TYPE_ID" ),
               lResultSetRecords.getString( "ENG_UNIT_DB_ID" ),
               lResultSetRecords.getString( "ENG_UNIT_CD" ),
               lResultSetRecords.getString( "DOMAIN_TYPE_DB_ID" ),
               lResultSetRecords.getString( "DOMAIN_TYPE_CD" ),
               lResultSetRecords.getString( "ENTRY_PREC_QT" ),
               lResultSetRecords.getString( "DATA_TYPE_CD" ),
               lResultSetRecords.getString( "DATA_TYPE_SDESC" ),
               lResultSetRecords.getString( "DATA_TYPE_MDESC" ),
               lResultSetRecords.getString( "FORECAST_BOOL" ) );

      } catch ( SQLException e ) {
         e.printStackTrace();

      }
      return lmimDataAdd;

   }


   /**
    * This function is to create inventory by given assembly information via parameters.
    *
    *
    */

   public simpleIDs createInventory( String astrAssmebly, simpleIDs aHIDs, simpleIDs aAIDs,
         assmbleInfor aInfor, simpleIDs aOIDs, String astrDescription, String astrSerialNo,
         String astrApplEffCD ) {

      String lstrGetMax = getmaxID( "Select max(INV_NO_ID) from INV_INV", "max(INV_NO_ID)" );
      String lstrNextID = Integer.toString( Integer.parseInt( lstrGetMax ) + 1 );

      // inventoryData invData = getInventory( "ACFT" );
      inventoryData linvData = getInventory( astrAssmebly );

      Map<String, String> lInv_inv = new LinkedHashMap<>();

      lInv_inv.put( "INV_NO_DB_ID", linvData.getINV_NO_DB_ID() );
      lInv_inv.put( "INV_NO_ID", lstrNextID );
      lInv_inv.put( "INV_CLASS_DB_ID", linvData.getINV_CLASS_DB_ID() );
      lInv_inv.put( "INV_CLASS_CD", "\'" + linvData.getINV_CLASS_CD() + "\'" );
      lInv_inv.put( "BOM_PART_DB_ID", linvData.getBOM_PART_DB_ID() );
      lInv_inv.put( "BOM_PART_ID", linvData.getBOM_PART_ID() );
      lInv_inv.put( "PART_NO_DB_ID", linvData.getPART_NO_DB_ID() );
      lInv_inv.put( "PART_NO_ID", linvData.getPART_NO_ID() );

      if ( astrAssmebly.equalsIgnoreCase( "ACFT" ) || astrAssmebly.equalsIgnoreCase( "ASSY" ) ) {
         lInv_inv.put( "H_INV_NO_DB_ID", linvData.getINV_NO_DB_ID() );
         lInv_inv.put( "H_INV_NO_ID", lstrNextID );
      } else {
         lInv_inv.put( "H_INV_NO_DB_ID", linvData.getINV_NO_DB_ID() );
         lInv_inv.put( "H_INV_NO_ID", linvData.getINV_NO_ID() );

      }

      if ( astrAssmebly.equalsIgnoreCase( "ACFT" ) || astrAssmebly.equalsIgnoreCase( "ASSY" ) ) {
         lInv_inv.put( "ASSMBL_INV_NO_DB_ID", linvData.getINV_NO_DB_ID() );
         lInv_inv.put( "ASSMBL_INV_NO_ID", lstrNextID );

      } else {
         lInv_inv.put( "ASSMBL_INV_NO_DB_ID", linvData.getINV_NO_DB_ID() );
         lInv_inv.put( "ASSMBL_INV_NO_ID", linvData.getINV_NO_ID() );

      }

      lInv_inv.put( "INV_COND_DB_ID", linvData.getINV_COND_DB_ID() );
      lInv_inv.put( "INV_COND_CD", "\'INSRV\'" );

      lInv_inv.put( "ASSMBL_DB_ID", linvData.getASSMBL_DB_ID() );
      lInv_inv.put( "ASSMBL_CD", "\'" + linvData.getASSMBL_CD() + "\'" );
      lInv_inv.put( "ASSMBL_BOM_ID", linvData.getASSMBL_BOM_ID() );
      lInv_inv.put( "ASSMBL_POS_ID", linvData.getASSMBL_POS_ID() );
      lInv_inv.put( "ORIG_ASSMBL_DB_ID", linvData.getORIG_ASSMBL_DB_ID() );
      lInv_inv.put( "ORIG_ASSMBL_CD", "\'" + linvData.getORIG_ASSMBL_CD() + "\'" );

      lInv_inv.put( "INV_NO_SDESC", "\'" + astrDescription + "\'" );
      lInv_inv.put( "SERIAL_NO_OEM", "\'" + astrSerialNo + "\'" );
      lInv_inv.put( "FINANCE_STATUS_CD", "\'INSP\'" );
      lInv_inv.put( "APPL_EFF_CD", "\'" + astrApplEffCD + "\'" );
      lInv_inv.put( "CARRIER_DB_ID", null );
      lInv_inv.put( "CARRIER_ID", null );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.INV_INV, lInv_inv ) );

      // simpleInvIds
      simpleIDs InvIds = new simpleIDs( linvData.getINV_NO_DB_ID(), lstrNextID );

      return InvIds;

   }


   /**
    * This function is to create task definition record.
    *
    * @return: IDs of task definition record.
    *
    */

   public simpleIDs createTaskDEFN() {
      String lstrGetMax =
            getmaxID( "Select max(TASK_DEFN_ID) from TASK_DEFN", "max(TASK_DEFN_ID)" );
      String lstrNextID = Integer.toString( Integer.parseInt( lstrGetMax ) + 1 );

      Map<String, String> ltask_defn = new LinkedHashMap<>();
      ltask_defn.put( "TASK_DEFN_DB_ID", "\'4650\'" );
      ltask_defn.put( "TASK_DEFN_ID", lstrNextID );
      ltask_defn.put( "LAST_REVISION_ORD", "\'1\'" );
      ltask_defn.put( "NEW_REVISION_BOOL", "\'1\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.TASK_DEFN, ltask_defn ) );

      // simpleInvIds
      simpleIDs tdfIds = new simpleIDs( "4650", lstrNextID );

      return tdfIds;

   }


   /**
    * This function is to create task record.
    *
    * @return: IDs of task record.
    *
    */

   public simpleIDs createTask( String strTskDfnDBId, String TskDfnId, String strTaskType,
         assmbleInfor assmbly, String strTaskDfnStatusCd, String strTaskCd, String strTaskName,
         String strOnCondition, String aResourceSumBool ) {

      // Get next id to insert
      String lstrGetMax = getmaxID( "Select max(TASK_ID) from TASK_TASK", "max(TASK_ID)" );
      String lstrNextID = Integer.toString( Integer.parseInt( lstrGetMax ) + 1 );

      // Get task class db Id
      String lstrSQL =
            "select TASK_CLASS_DB_ID from ref_task_class where task_class_cd='" + strTaskType + "'";
      String lstrTaskClassDbid = getStringValue( lstrSQL, "TASK_CLASS_DB_ID" );

      // Get task_def_status_Db_id
      lstrSQL = "select TASK_DEF_STATUS_DB_ID from REF_TASK_DEF_STATUS where TASK_DEF_STATUS_CD='"
            + strTaskDfnStatusCd + "'";
      String strTaskDfnStatusDbid = getStringValue( lstrSQL, "TASK_DEF_STATUS_DB_ID" );

      Map<String, String> ltask_task = new LinkedHashMap<>();
      // ltask_task.put( "TASK_DB_ID", "\'" + CONS_DB_ID + "\'" );
      ltask_task.put( "TASK_DB_ID", Integer.toString( CONS_DB_ID ) );
      ltask_task.put( "TASK_ID", lstrNextID );
      ltask_task.put( "TASK_DEFN_DB_ID", strTskDfnDBId );
      ltask_task.put( "TASK_DEFN_ID", TskDfnId );
      ltask_task.put( "TASK_CLASS_DB_ID", lstrTaskClassDbid );
      ltask_task.put( "TASK_CLASS_CD", "\'" + strTaskType + "\'" );
      ltask_task.put( "ASSMBL_DB_ID", assmbly.getASSMBL_DB_ID() );
      ltask_task.put( "ASSMBL_CD", "\'" + assmbly.getASSMBL_CD() + "\'" );
      ltask_task.put( "ASSMBL_BOM_ID", assmbly.getASSMBL_BOM_ID() );
      ltask_task.put( "TASK_DEF_STATUS_DB_ID", strTaskDfnStatusDbid );
      ltask_task.put( "TASK_DEF_STATUS_CD", "\'" + strTaskDfnStatusCd + "\'" );
      ltask_task.put( "TASK_CD", "\'" + strTaskCd + "\'" );
      ltask_task.put( "TASK_NAME", "\'" + strTaskName + "\'" );
      ltask_task.put( "ON_CONDITION_BOOL", strOnCondition );
      ltask_task.put( "RESOURCE_SUM_BOOL", aResourceSumBool );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.TASK_TASK, ltask_task ) );

      // simpleInvIds
      simpleIDs tdfIds = new simpleIDs( Integer.toString( CONS_DB_ID ), lstrNextID );

      return tdfIds;

   }


   /**
    * This function is to create labor req record.
    *
    */

   public void createLabourREQ( simpleIDs aTaskIDs, String aStrLabourSkillCD ) {

      String istrLabourDBId = getLabourDBId( aStrLabourSkillCD );

      Map<String, String> ltask_labour_list = new LinkedHashMap<>();
      ltask_labour_list.put( "TASK_DB_ID", aTaskIDs.getNO_DB_ID() );
      ltask_labour_list.put( "TASK_ID", aTaskIDs.getNO_ID() );
      ltask_labour_list.put( "LABOUR_SKILL_DB_ID", istrLabourDBId );
      ltask_labour_list.put( "LABOUR_SKILL_CD", "\'" + aStrLabourSkillCD + "\'" );
      ltask_labour_list.put( "MAN_PWR_CT", "\'" + 1 + "\'" );
      ltask_labour_list.put( "WORK_PERF_BOOL", "\'" + 1 + "\'" );
      ltask_labour_list.put( "WORK_PERF_HR", "\'" + 1.00000 + "\'" );
      ltask_labour_list.put( "CERT_BOOL", "\'" + 1 + "\'" );
      ltask_labour_list.put( "INSP_BOOL", "\'" + 1 + "\'" );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.TASK_LABOUR_LIST, ltask_labour_list ) );

   }


   /**
    * This function is to create tool req record.
    *
    * @return: IDs of tool req record
    */

   public simpleIDs createToolREQ( simpleIDs aTaskIDs, boolean aFirst ) {

      String lstrNextID;

      String lstrSQL = "select count(*) from task_tool_list where rownum=1";
      int lnum = getIntValueFromQuery( lstrSQL, "count(*)" );

      if ( lnum > 0 ) {
         String lstrGetMax = getmaxID(
               "Select max(task_tool_id) from task_tool_list where TASK_DB_ID="
                     + aTaskIDs.getNO_DB_ID() + " and Task_ID=" + aTaskIDs.getNO_ID(),
               "max(task_tool_id)" );
         lstrNextID = Integer.toString( Integer.parseInt( lstrGetMax ) + 1 );

      } else {
         lstrNextID = "0";
      }

      simpleIDs lIds = getToolBomPartIds( aFirst );

      Map<String, String> ltask_tool_list = new LinkedHashMap<>();
      ltask_tool_list.put( "TASK_DB_ID", aTaskIDs.getNO_DB_ID() );
      ltask_tool_list.put( "TASK_ID", aTaskIDs.getNO_ID() );
      ltask_tool_list.put( "TASK_TOOL_ID", lstrNextID );
      ltask_tool_list.put( "BOM_PART_DB_ID", lIds.getNO_DB_ID() );
      ltask_tool_list.put( "BOM_PART_ID", lIds.getNO_ID() );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.TASK_TOOL_LIST, ltask_tool_list ) );

      return lIds;

   }


   /**
    * This function is to create step req record.
    *
    */

   public void createStepREQ( simpleIDs aTaskIDs, String aStepOrder, String aStepLSDEC ) {

      // Get next id to insert
      String lstrGetMax = getmaxID( "Select max(STEP_ID) from TASK_STEP", "max(STEP_ID)" );
      String lstrNextID = Integer.toString( Integer.parseInt( lstrGetMax ) + 1 );

      Map<String, String> ltask_step_list = new LinkedHashMap<>();
      ltask_step_list.put( "TASK_DB_ID", aTaskIDs.getNO_DB_ID() );
      ltask_step_list.put( "TASK_ID", aTaskIDs.getNO_ID() );
      ltask_step_list.put( "STEP_ID", lstrNextID );
      ltask_step_list.put( "STEP_ORD", "\'" + aStepOrder + "\'" );
      ltask_step_list.put( "STEP_LDESC", "\'" + aStepLSDEC + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.TASK_STEP, ltask_step_list ) );

   }


   /**
    * This function is to create step zone record.
    *
    */
   public void createZoneREQ( simpleIDs aTaskIDs, String aZone_cd ) {

      String lstrNextID = null;
      String lstrSQL = "select count(*) from TASK_ZONE where rownum=1";
      int lnum = getIntValueFromQuery( lstrSQL, "count(*)" );

      if ( lnum > 0 ) {
         // Get next id to insert
         String lstrGetMax =
               getmaxID( "Select max(TASK_ZONE_ID) from TASK_ZONE", "max(TASK_ZONE_ID)" );
         lstrNextID = Integer.toString( Integer.parseInt( lstrGetMax ) + 1 );
      } else {
         lstrNextID = "0";

      }

      // Get Zone Ids
      simpleIDs lIds = getZoneIds( aZone_cd );

      Map<String, String> ltask_step_list = new LinkedHashMap<>();
      ltask_step_list.put( "TASK_DB_ID", aTaskIDs.getNO_DB_ID() );
      ltask_step_list.put( "TASK_ID", aTaskIDs.getNO_ID() );
      ltask_step_list.put( "TASK_ZONE_ID", "\'" + lstrNextID + "\'" );
      ltask_step_list.put( "ZONE_DB_ID", lIds.getNO_DB_ID() );
      ltask_step_list.put( "ZONE_ID", lIds.getNO_ID() );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.TASK_ZONE, ltask_step_list ) );

   }


   /**
    * This function is to create panel req record.
    *
    */

   public void createPanelREQ( simpleIDs aTaskIDs, String aPanel_cd ) {

      String lstrNextID = null;
      String lstrSQL = "select count(*) from TASK_PANEL where rownum=1";
      int lnum = getIntValueFromQuery( lstrSQL, "count(*)" );

      if ( lnum > 0 ) {
         // Get next id to insert
         String lstrGetMax =
               getmaxID( "Select max(TASK_PANEL_ID) from TASK_PANEL", "max(TASK_PANEL_ID)" );
         lstrNextID = Integer.toString( Integer.parseInt( lstrGetMax ) + 1 );
      } else {
         lstrNextID = "0";

      }

      // Get PANEL Ids
      simpleIDs lIds = getPanelIds( aPanel_cd );

      Map<String, String> ltask_panel_list = new LinkedHashMap<>();
      ltask_panel_list.put( "TASK_DB_ID", aTaskIDs.getNO_DB_ID() );
      ltask_panel_list.put( "TASK_ID", aTaskIDs.getNO_ID() );
      ltask_panel_list.put( "TASK_PANEL_ID", "\'" + lstrNextID + "\'" );
      ltask_panel_list.put( "PANEL_DB_ID", lIds.getNO_DB_ID() );
      ltask_panel_list.put( "PANEL_ID", lIds.getNO_ID() );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.TASK_PANEL, ltask_panel_list ) );

   }


   /**
    * This function is to create work type req record.
    *
    */
   public void createWorkTypeREQ( simpleIDs aTaskIDs, String aWorkType_cd ) {

      // Get WorkType DB Ids
      String lDBId = getWorkTypeDBId( aWorkType_cd );

      Map<String, String> ltask_worktype_list = new LinkedHashMap<>();
      ltask_worktype_list.put( "TASK_DB_ID", aTaskIDs.getNO_DB_ID() );
      ltask_worktype_list.put( "TASK_ID", aTaskIDs.getNO_ID() );
      ltask_worktype_list.put( "WORK_TYPE_DB_ID", "\'" + lDBId + "\'" );
      ltask_worktype_list.put( "WORK_TYPE_CD", "\'" + aWorkType_cd + "\'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.TASK_WORK_TYPE, ltask_worktype_list ) );

   }


   /**
    * This function is to create record in TASK_PARM_DATA table.
    *
    */
   public void createParmData( simpleIDs aTaskIDs, simpleIDs aParmDataIDs, String aDataOrd ) {

      Map<String, String> ltask_parmdata_list = new LinkedHashMap<>();
      ltask_parmdata_list.put( "TASK_DB_ID", aTaskIDs.getNO_DB_ID() );
      ltask_parmdata_list.put( "TASK_ID", aTaskIDs.getNO_ID() );
      ltask_parmdata_list.put( "DATA_TYPE_DB_ID", aParmDataIDs.getNO_DB_ID() );
      ltask_parmdata_list.put( "DATA_TYPE_ID", aParmDataIDs.getNO_ID() );
      ltask_parmdata_list.put( "DATA_ORD", "\'" + aDataOrd + "\'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.TASK_PARM_DATA, ltask_parmdata_list ) );

   }


   /**
    * This function is to create record in REF_DATA_TYPE_ASSMBL_CLASS table.
    *
    */
   public void createRefDataAssmblClass( simpleIDs aParmDataIDs, String aAssmblyClassCD ) {

      String lDbId = getAssmblClassDBId( aAssmblyClassCD );

      Map<String, String> lassmblclass_list = new LinkedHashMap<>();
      lassmblclass_list.put( "DATA_TYPE_DB_ID", aParmDataIDs.getNO_DB_ID() );
      lassmblclass_list.put( "DATA_TYPE_ID", aParmDataIDs.getNO_ID() );
      lassmblclass_list.put( "ASSMBL_CLASS_DB_ID", lDbId );
      lassmblclass_list.put( "ASSMBL_CLASS_CD", "\'" + aAssmblyClassCD + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.REF_DATA_TYPE_ASSMBL_CLASS,
            lassmblclass_list ) );

   }


   /**
    * This function is to create record in MIM_DATA_TYPE table.
    *
    * @param: short
    *            description of record
    * @return: IDs of created record in MIM_DATA_TYPE table
    *
    */

   public simpleIDs createMimDataType( String aSDESC ) {

      // Get next seq value of data type id
      String lstrGetMax =
            getmaxID( "Select max(DATA_TYPE_ID) from MIM_DATA_TYPE", "max(DATA_TYPE_ID)" );
      String lstrNextID = Integer.toString( Integer.parseInt( lstrGetMax ) + 1 );

      // modify value of retrieved data
      MimData lmimDataAdd = getMimDataType();
      lmimDataAdd.setDATA_TYPE_ID( lstrNextID );
      lmimDataAdd.setDATA_TYPE_SDESC( aSDESC );

      Map<String, String> ltask_mimdataType_list = new LinkedHashMap<>();
      ltask_mimdataType_list.put( "DATA_TYPE_DB_ID", lmimDataAdd.getDATA_TYPE_DB_ID() );
      ltask_mimdataType_list.put( "DATA_TYPE_ID", lmimDataAdd.getDATA_TYPE_ID() );
      ltask_mimdataType_list.put( "ENG_UNIT_DB_ID", lmimDataAdd.getENG_UNIT_DB_ID() );
      ltask_mimdataType_list.put( "ENG_UNIT_CD", "\'" + lmimDataAdd.getENG_UNIT_CD() + "\'" );
      ltask_mimdataType_list.put( "DOMAIN_TYPE_DB_ID", lmimDataAdd.getDOMAIN_TYPE_DB_ID() );
      ltask_mimdataType_list.put( "DOMAIN_TYPE_CD", "\'" + lmimDataAdd.getDOMAIN_TYPE_CD() + "\'" );
      ltask_mimdataType_list.put( "ENTRY_PREC_QT", lmimDataAdd.getENTRY_PREC_QT() );
      ltask_mimdataType_list.put( "DATA_TYPE_CD", "\'" + lmimDataAdd.getDATA_TYPE_CD() + "\'" );
      ltask_mimdataType_list.put( "DATA_TYPE_SDESC",
            "\'" + lmimDataAdd.getDATA_TYPE_SDESC() + "\'" );
      ltask_mimdataType_list.put( "DATA_TYPE_MDESC",
            "\'" + lmimDataAdd.getDATA_TYPE_MDESC() + "\'" );
      ltask_mimdataType_list.put( "FORECAST_BOOL", lmimDataAdd.getFORECAST_BOOL() );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.MIM_DATA_TYPE, ltask_mimdataType_list ) );

      return new simpleIDs( lmimDataAdd.getDATA_TYPE_DB_ID(), lmimDataAdd.getDATA_TYPE_ID() );

   }


   /**
    * This function is to create record in IETM_IETM table.
    *
    * @param: short
    *            description of record
    * @return: IDs of created record in IETM_IETM table
    *
    */
   public simpleIDs createIetmIetm( String aSDESC ) {

      String lstrNextID = null;
      String lstrSQL = "select count(*) from IETM_IETM where rownum=1";
      int lnum = getIntValueFromQuery( lstrSQL, "count(*)" );

      if ( lnum > 0 ) {
         // Get next id to insert
         String lstrGetMax = getmaxID( "Select max(IETM_ID) from IETM_IETM", "max(IETM_ID)" );
         lstrNextID = Integer.toString( Integer.parseInt( lstrGetMax ) + 1 );
      } else {
         lstrNextID = "0";

      }

      Map<String, String> lIetm_list = new LinkedHashMap<>();
      lIetm_list.put( "IETM_DB_ID", Integer.toString( CONS_DB_ID ) );
      lIetm_list.put( "IETM_ID", lstrNextID );
      lIetm_list.put( "IETM_CD", "\'" + aSDESC + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.IETM_IETM, lIetm_list ) );

      return new simpleIDs( Integer.toString( CONS_DB_ID ), lstrNextID );

   }


   /**
    * This function is to create record in IETM_TOPIC table.
    *
    * @param aSDESC
    *           : short description of record
    * @param ablob
    *           : blob value
    * @param attachType
    *           : attachment type
    * @param aApplEff
    *           valdue
    * @return: IDs of created record in IETM_TOPIC table
    *
    */
   public simpleIDs createIetmTopic( String aSDESC, String ablob, String attachType,
         String aApplEff ) {

      String lstrNextID = null;
      String lblob = ablob;
      String lstrSQL = "select count(*) from IETM_TOPIC where rownum=1";
      int lnum = getIntValueFromQuery( lstrSQL, "count(*)" );

      if ( lnum > 0 ) {
         // Get next id to insert
         String lstrGetMax = getmaxID( "Select max(IETM_ID) from IETM_TOPIC", "max(IETM_ID)" );
         lstrNextID = Integer.toString( Integer.parseInt( lstrGetMax ) + 1 );
      } else {
         lstrNextID = "0";

      }
      simpleIDs lIetmIds = createIetmIetm( "New Ietm" );
      simpleIDs lAttachIDs = getAttachIds( attachType );

      Map<String, String> lIetem_list = new LinkedHashMap<>();
      lIetem_list.put( "IETM_DB_ID", lIetmIds.getNO_DB_ID() );
      lIetem_list.put( "IETM_ID", lIetmIds.getNO_ID() );
      lIetem_list.put( "IETM_TOPIC_ID", "\'" + 0 + "\'" );
      lIetem_list.put( "TOPIC_SDESC", "\'" + aSDESC + "\'" );
      lIetem_list.put( "PRINT_BOOL", "\'" + 1 + "\'" );
      lIetem_list.put( "ATTACH_TYPE_DB_ID", lAttachIDs.getNO_DB_ID() );
      lIetem_list.put( "ATTACH_TYPE_CD", "\'" + lAttachIDs.getNO_ID() + "\'" );
      lIetem_list.put( "ATTACH_BLOB", null );
      lIetem_list.put( "APPL_EFF_LDESC", "\'" + aApplEff + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.IETM_TOPIC, lIetem_list ) );

      // Insert blob
      if ( ablob != null ) {
         insertBlobViaSetBlob( TableUtil.IETM_TOPIC, lblob.getBytes(), lIetmIds );
      }

      return new simpleIDs( lIetmIds.getNO_DB_ID(), lIetmIds.getNO_ID() );

   }


   /**
    * This function is to create record in IETM_TOPIC table.
    *
    * @param aSDESC
    *           : short description of record
    * @param ablob
    *           : blob value
    * @param attachType
    *           : attachment type
    * @return: IDs of created record in IETM_TOPIC table
    *
    */

   public simpleIDs createTaskIetmTopic( String aSDESC, String ablob, String attachType ) {

      String lstrNextID = null;
      String lstrSQL = "select count(*) from IETM_TOPIC where rownum=1";
      int lnum = getIntValueFromQuery( lstrSQL, "count(*)" );

      if ( lnum > 0 ) {
         // Get next id to insert
         String strGetMax = getmaxID( "Select max(IETM_ID) from IETM_TOPIC", "max(IETM_ID)" );
         lstrNextID = Integer.toString( Integer.parseInt( strGetMax ) + 1 );
      } else {
         lstrNextID = "0";

      }
      simpleIDs lIetmIds = createIetmIetm( "New Ietm" );
      simpleIDs lAttachIDs = getAttachIds( attachType );

      Map<String, String> lIetem_list = new LinkedHashMap<>();
      lIetem_list.put( "IETM_DB_ID", lIetmIds.getNO_DB_ID() );
      lIetem_list.put( "IETM_ID", lIetmIds.getNO_ID() );
      lIetem_list.put( "IETM_TOPIC_ID", "\'" + 0 + "\'" );
      lIetem_list.put( "TOPIC_SDESC", "\'" + aSDESC + "\'" );
      lIetem_list.put( "PRINT_BOOL", "\'" + 1 + "\'" );
      lIetem_list.put( "ATTACH_TYPE_DB_ID", lAttachIDs.getNO_DB_ID() );
      lIetem_list.put( "ATTACH_TYPE_CD", lAttachIDs.getNO_ID() );
      lIetem_list.put( "ATTACH_BLOB", ablob );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.IETM_TOPIC, lIetem_list ) );

      return new simpleIDs( lIetmIds.getNO_DB_ID(), lIetmIds.getNO_DB_ID() );

   }


   /**
    * This function is to create record in TASK_TASK_IETM table.
    *
    * @param aTaskIDs
    *           : Task IDs
    * @param aIetmIDs
    *           : ietm IDs
    * @return: IDs of created record in TASK_TASK_IETM table
    *
    */

   public void createTaskTaskIetm( simpleIDs aTaskIDs, simpleIDs aIetmIDs ) {

      String lstrNextID = null;
      String lstrSQL = "select count(*) from TASK_TASK_IETM where rownum=1";
      int lnum = getIntValueFromQuery( lstrSQL, "count(*)" );

      if ( lnum > 0 ) {
         // Get next id to insert
         String lstrGetMax =
               getmaxID( "Select max(TASK_IETM_ID) from TASK_TASK_IETM", "max(TASK_IETM_ID)" );
         lstrNextID = Integer.toString( Integer.parseInt( lstrGetMax ) + 1 );
      } else {
         lstrNextID = "0";

      }

      Map<String, String> lIetem_list = new LinkedHashMap<>();
      lIetem_list.put( "TASK_DB_ID", aTaskIDs.getNO_DB_ID() );
      lIetem_list.put( "TASK_ID", aTaskIDs.getNO_ID() );
      lIetem_list.put( "TASK_IETM_ID", "\'" + lstrNextID + "\'" );
      lIetem_list.put( "IETM_DB_ID", aIetmIDs.getNO_DB_ID() );
      lIetem_list.put( "IETM_ID", aIetmIDs.getNO_ID() );
      lIetem_list.put( "IETM_TOPIC_ID", "\'" + 0 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.TASK_TASK_IETM, lIetem_list ) );

   }


   /**
    * This function is to get max value of a column in a table
    *
    * @param strSQL
    *           : query string
    * @param strcolumnLabel
    *           : column name
    * @return: value of max
    *
    */

   public String getmaxID( String astrSQL, String astrcolumnLabel ) {
      ResultSet ResultSetRecords;
      String strID = null;

      try {
         ResultSetRecords = runQuery( astrSQL.toString() );
         ResultSetRecords.next();
         strID = ResultSetRecords.getString( astrcolumnLabel );
      } catch ( SQLException e ) {
         e.printStackTrace();

      }
      return strID;
   }


   /**
    * This function is to retrieve the first string value of a column in a table
    *
    * @param strSQL
    *           : query string
    * @param strcolumnLabel
    *           : column name
    * @return: value of first string value
    *
    */
   public String getStringValue( String astrSQL, String astrColName ) {
      ResultSet ResultSetRecords;
      String lstrID = null;

      try {
         ResultSetRecords = runQuery( astrSQL );
         ResultSetRecords.next();
         lstrID = ResultSetRecords.getString( astrColName );
      } catch ( SQLException e ) {
         e.printStackTrace();

      }
      return lstrID;
   }


   /**
    * This function is to retrieve the assembly information by given string of query
    *
    * @param strSQL
    *           : query string
    * @return: value of assembly
    *
    */
   public assmbleInfor getAssmbleInfor( String astrSQL ) {

      ResultSet ResultSetRecords;
      assmbleInfor lassInfor = null;
      try {
         ResultSetRecords = runQuery( astrSQL );
         ResultSetRecords.next();
         lassInfor = new assmbleInfor( ResultSetRecords.getString( "ASSMBL_DB_ID" ),
               ResultSetRecords.getString( "ASSMBL_CD" ),
               ResultSetRecords.getString( "ASSMBL_BOM_ID" ), null );
      } catch ( SQLException e ) {
         e.printStackTrace();

      }

      return lassInfor;

   }


   /**
    * This function is to retrieve part no ids from eqp_part_baseline table
    *
    * @param aBomPartIds
    *           : bom part ids
    * @return: part no ids
    *
    */

   public simpleIDs getPartNoIds( simpleIDs aBomPartIds ) {

      simpleIDs lIds = null;
      ResultSet ResultSetRecords;
      String lstrSQL =
            "select part_no_db_id, part_no_id from eqp_part_baseline where standard_bool  = 1 "
                  + " and bom_part_db_id=" + aBomPartIds.getNO_DB_ID() + " and bom_part_id="
                  + aBomPartIds.getNO_ID();

      try {
         ResultSetRecords = runQuery( lstrSQL );
         ResultSetRecords.next();
         lIds = new simpleIDs( ResultSetRecords.getString( "part_no_db_id" ),
               ResultSetRecords.getString( "part_no_id" ) );
      } catch ( SQLException e ) {
         e.printStackTrace();

      }

      return lIds;

   }


   /**
    * This function is to retrieve HR ids from org_hr table with system
    *
    * @return: hr ids
    *
    */
   public simpleIDs getHRIds() {
      ResultSet ResultSetRecords;
      String lstrID = null;
      String lstrSQL = "select HR_DB_ID, HR_ID from org_hr where HR_CD='SYSTEM'";
      simpleIDs lIds = null;

      try {
         ResultSetRecords = runQuery( lstrSQL );
         ResultSetRecords.next();
         lIds = new simpleIDs( ResultSetRecords.getString( "HR_DB_ID" ),
               ResultSetRecords.getString( "HR_ID" ) );
      } catch ( SQLException e ) {
         e.printStackTrace();

      }
      return lIds;

   }


   /**
    * This function is to retrieve labor DB id from REF_LABOUR_SKILL table
    *
    * @return: DB id
    *
    */
   public String getLabourDBId( String aLabourCD ) {
      ResultSet ResultSetRecords;
      String lstrID = null;
      String lstrSQL = "select LABOUR_SKILL_DB_ID from REF_LABOUR_SKILL where LABOUR_SKILL_CD='"
            + aLabourCD + "'";

      try {
         ResultSetRecords = runQuery( lstrSQL );
         ResultSetRecords.next();
         lstrID = ResultSetRecords.getString( "LABOUR_SKILL_DB_ID" );
      } catch ( SQLException e ) {
         e.printStackTrace();

      }
      return lstrID;

   }


   /**
    * This function is to retrieve assembly class DB id from REF_ASSMBL_CLASS table by given
    * assembly class cd
    *
    * @return: DB id
    *
    */
   public String getAssmblClassDBId( String aAssmblyClassCD ) {
      ResultSet ResultSetRecords;
      String lstrID = null;
      String lstrSQL = "select ASSMBL_CLASS_DB_ID from REF_ASSMBL_CLASS where ASSMBL_CLASS_CD='"
            + aAssmblyClassCD + "'";

      try {
         ResultSetRecords = runQuery( lstrSQL );
         ResultSetRecords.next();
         lstrID = ResultSetRecords.getString( "ASSMBL_CLASS_DB_ID" );
      } catch ( SQLException e ) {
         e.printStackTrace();

      }
      return lstrID;

   }


   /**
    * This function is to retrieve Part no IDs from INV-INV table by given assembly class inventory
    * IDs
    *
    * @return: Part no ids.
    *
    */

   public simpleIDs getPNIds( simpleIDs aIDs ) {
      ResultSet ResultSetRecords;
      String lstrID = null;
      String lstrSQL = "select PART_NO_DB_ID, PART_NO_ID from INV_INV where INV_NO_DB_ID="
            + aIDs.getNO_DB_ID() + " and INV_NO_ID=" + aIDs.getNO_ID();
      simpleIDs lIds = null;

      try {
         ResultSetRecords = runQuery( lstrSQL );
         ResultSetRecords.next();
         lIds = new simpleIDs( ResultSetRecords.getString( "PART_NO_DB_ID" ),
               ResultSetRecords.getString( "PART_NO_ID" ) );
      } catch ( SQLException e ) {
         e.printStackTrace();

      }
      return lIds;

   }


   /**
    * This function is to retrieve BOM Part IDs from INV-INV table
    *
    * @param: Assmbl_Bom_Id
    *            is 0 or tool attached to assembly
    * @return: bom Part no ids.
    *
    */
   public simpleIDs getToolBomPartIds( boolean aFirst ) {

      ResultSet ResultSetRecords;
      simpleIDs lIds = null;
      String lstrSQL = null;

      if ( aFirst ) {
         lstrSQL = "select EQP_BOM_PART.Bom_Part_Db_Id, EQP_BOM_PART.BOM_PART_ID from EQP_BOM_PART "
               + "inner join eqp_part_baseline on "
               + "EQP_BOM_PART.bom_part_db_id=eqp_part_baseline.bom_part_db_id and "
               + "EQP_BOM_PART.BOM_PART_ID=eqp_part_baseline.BOM_PART_ID "
               + "where  eqp_part_baseline.standard_bool  = 1 and "
               + "eqp_part_baseline.appl_eff_LDESC is null and EQP_BOM_PART.Assmbl_Cd='TSE' and EQP_BOM_PART.Assmbl_Bom_Id<>0";
      } else {
         lstrSQL =
               "select EQP_BOM_PART.Bom_Part_Db_Id, EQP_BOM_PART.BOM_PART_ID from EQP_BOM_PART  "
                     + "inner join eqp_part_baseline on "
                     + "EQP_BOM_PART.bom_part_db_id=eqp_part_baseline.bom_part_db_id and "
                     + "EQP_BOM_PART.BOM_PART_ID=eqp_part_baseline.BOM_PART_ID "
                     + "where  eqp_part_baseline.standard_bool  = 1 and "
                     + "eqp_part_baseline.appl_eff_LDESC is null and EQP_BOM_PART.Assmbl_Cd='TSE' and EQP_BOM_PART.INV_CLASS_CD='SER'";
      }

      try {
         ResultSetRecords = runQuery( lstrSQL );
         ResultSetRecords.next();
         lIds = new simpleIDs( ResultSetRecords.getString( "Bom_Part_Db_Id" ),
               ResultSetRecords.getString( "BOM_PART_ID" ) );
      } catch ( SQLException e ) {
         e.printStackTrace();

      }

      return lIds;

   }


   /**
    * This function is to retrieve zone IDs from INV-EQP_TASK_ZONE table
    *
    * @param: Zone
    *            cd
    * @return: Zone IDs.
    *
    */

   public simpleIDs getZoneIds( String aZone_cd ) {
      ResultSet ResultSetRecords;
      String lstrID = null;
      String lstrSQL =
            "select ZONE_DB_ID, ZONE_ID from EQP_TASK_ZONE where ZONE_CD='" + aZone_cd + "'";

      simpleIDs Ids = null;

      try {
         ResultSetRecords = runQuery( lstrSQL );
         ResultSetRecords.next();
         Ids = new simpleIDs( ResultSetRecords.getString( "ZONE_DB_ID" ),
               ResultSetRecords.getString( "ZONE_ID" ) );
      } catch ( SQLException e ) {
         e.printStackTrace();

      }
      return Ids;

   }


   /**
    * This function is to retrieve panel IDs from INV-EQP_TASK_ZONE table
    *
    * @param: panel
    *            cd
    * @return: panel IDs.
    *
    */

   public simpleIDs getPanelIds( String aPanel_cd ) {
      ResultSet ResultSetRecords;
      String lstrID = null;
      String lstrSQL =
            "select PANEL_DB_ID, PANEL_ID from EQP_TASK_PANEL where PANEL_CD='" + aPanel_cd + "'";

      simpleIDs Ids = null;

      try {
         ResultSetRecords = runQuery( lstrSQL );
         ResultSetRecords.next();
         Ids = new simpleIDs( ResultSetRecords.getString( "PANEL_DB_ID" ),
               ResultSetRecords.getString( "PANEL_ID" ) );
      } catch ( SQLException e ) {
         e.printStackTrace();

      }
      return Ids;

   }


   /**
    * This function is to retrieve work type IDs from INV-REF_WORK_TYPE table
    *
    * @param: work
    *            type cd
    * @return: work type IDs.
    *
    */
   public String getWorkTypeDBId( String aWorkTypeCD ) {
      ResultSet ResultSetRecords;
      String lstrID = null;
      String lstrSQL =
            "select WORK_TYPE_DB_ID from REF_WORK_TYPE where WORK_TYPE_CD='" + aWorkTypeCD + "'";

      try {
         ResultSetRecords = runQuery( lstrSQL );
         ResultSetRecords.next();
         lstrID = ResultSetRecords.getString( "WORK_TYPE_DB_ID" );
      } catch ( SQLException e ) {
         e.printStackTrace();

      }
      return lstrID;

   }


   /**
    * This function is to retrieve attachment IDs from INV-ATTACH_TYPE_CD table
    *
    * @param: attachment
    *            cd
    * @return: attachment IDs.
    *
    */

   public simpleIDs getAttachIds( String attachType ) {
      ResultSet ResultSetRecords;
      String lstrID = null;
      String lstrSQL =
            "select ATTACH_TYPE_DB_ID, ATTACH_TYPE_CD from REF_ATTACH_TYPE where ATTACH_TYPE_CD='"
                  + attachType + "'";

      simpleIDs Ids = null;

      try {
         ResultSetRecords = runQuery( lstrSQL );
         ResultSetRecords.next();
         Ids = new simpleIDs( ResultSetRecords.getString( "ATTACH_TYPE_DB_ID" ),
               ResultSetRecords.getString( "ATTACH_TYPE_CD" ) );
      } catch ( SQLException e ) {
         e.printStackTrace();

      }
      return Ids;

   }


   /**
    * This function is to validate evt_event table
    *
    *
    */
   public void validateEvt_Event( simpleIDs aEventIds, String aTaskStatusCD, simpleIDs aHLEventIds,
         String aEventSdesc, int aHistbool ) {

      ResultSet ResultSetRecords = null;
      String iTaskStatusCD = null;
      simpleIDs iHLEventIds = null;
      String iEventSdesc = null;
      int iHistbool = -1;
      String istrSQL =
            "select EVENT_STATUS_CD, H_EVENT_DB_ID,H_EVENT_ID, EVENT_SDESC,  HIST_BOOL from EVT_EVENT where EVENT_DB_ID="
                  + aEventIds.getNO_DB_ID() + " and EVENT_ID=" + aEventIds.getNO_ID();

      try {
         ResultSetRecords = runQuery( istrSQL );
         ResultSetRecords.next();
         iTaskStatusCD = ResultSetRecords.getString( "EVENT_STATUS_CD" );
         iHLEventIds = new simpleIDs( ResultSetRecords.getString( "H_EVENT_DB_ID" ),
               ResultSetRecords.getString( "H_EVENT_ID" ) );
         iEventSdesc = ResultSetRecords.getString( "EVENT_SDESC" );
         iHistbool = ResultSetRecords.getInt( "HIST_BOOL" );

      } catch ( SQLException e ) {
         e.printStackTrace();

      }

      Assert.assertTrue( "Check EVENT_STATUS_CD ",
            iTaskStatusCD.equalsIgnoreCase( aTaskStatusCD ) );
      Assert.assertTrue( "Check H_EVENT_IDs", iHLEventIds.equals( aHLEventIds ) );
      Assert.assertTrue( "Check Event-Sdesc", iEventSdesc.equals( aEventSdesc ) );
      Assert.assertTrue( "Check HIST BOOL  ", iHistbool == aHistbool );

   }


   /**
    * This function is to validate evt_inv table
    *
    *
    */

   public void validateEvt_Inv( simpleIDs aEventIds, simpleIDs aINVIds, String aAssmbly ) {

      ResultSet ResultSetRecords = null;

      // Get inv_id
      simpleIDs iINVIds = null;
      simpleIDs iAssmblINVIds = null;
      simpleIDs iHINVIds = null;
      assmbleInfor iassmblinforEVT = null;
      assmbleInfor iassmblinforINV = null;
      simpleIDs iPARTEVT = null;
      simpleIDs iPARTINV = null;
      simpleIDs iBOMPARTEVT = null;
      simpleIDs iBOMPARTINV = null;

      String istrSQL =
            "Select INV_NO_DB_ID, INV_NO_ID, ASSMBL_INV_NO_DB_ID, ASSMBL_INV_NO_ID, H_INV_NO_DB_ID, H_INV_NO_ID, ASSMBL_DB_ID,"
                  + "ASSMBL_CD, ASSMBL_BOM_ID, PART_NO_DB_ID, PART_NO_ID, BOM_PART_DB_ID,BOM_PART_ID from EVT_INV "
                  + "Where EVENT_DB_ID=" + aEventIds.getNO_DB_ID() + " and EVENT_ID="
                  + aEventIds.getNO_ID();

      // Get data from evt_inv table
      try {
         ResultSetRecords = runQuery( istrSQL );
         ResultSetRecords.next();

         iINVIds = new simpleIDs( ResultSetRecords.getString( "INV_NO_DB_ID" ),
               ResultSetRecords.getString( "INV_NO_ID" ) );
         iAssmblINVIds = new simpleIDs( ResultSetRecords.getString( "ASSMBL_INV_NO_DB_ID" ),
               ResultSetRecords.getString( "ASSMBL_INV_NO_ID" ) );
         iHINVIds = new simpleIDs( ResultSetRecords.getString( "H_INV_NO_DB_ID" ),
               ResultSetRecords.getString( "H_INV_NO_ID" ) );

         iassmblinforEVT = new assmbleInfor( ResultSetRecords.getString( "ASSMBL_DB_ID" ),
               ResultSetRecords.getString( "ASSMBL_CD" ),
               ResultSetRecords.getString( "ASSMBL_BOM_ID" ), null );

         iPARTEVT = new simpleIDs( ResultSetRecords.getString( "PART_NO_DB_ID" ),
               ResultSetRecords.getString( "PART_NO_ID" ) );

         iBOMPARTEVT = new simpleIDs( ResultSetRecords.getString( "BOM_PART_DB_ID" ),
               ResultSetRecords.getString( "BOM_PART_ID" ) );

         ResultSetRecords.close();

      } catch ( SQLException e ) {
         e.printStackTrace();

      }

      // Check inv, assmbl_inv, and H_inv IDS
      Assert.assertTrue( "Check inv IDS ", iINVIds.equals( aINVIds ) );
      if ( !aAssmbly.equalsIgnoreCase( "TRK" ) ) {
         Assert.assertTrue( "Check assmbl inv IDS ", iAssmblINVIds.equals( aINVIds ) );
         Assert.assertTrue( "Check H inv IDS ", iHINVIds.equals( aINVIds ) );
      }

      // Get data from INV_INV table
      istrSQL =
            "Select ASSMBL_DB_ID,ASSMBL_CD, ASSMBL_BOM_ID, PART_NO_DB_ID, PART_NO_ID, BOM_PART_DB_ID,BOM_PART_ID "
                  + "from INV_INV where INV_NO_DB_ID=" + aINVIds.getNO_DB_ID() + " and INV_NO_ID="
                  + aINVIds.getNO_ID();
      try {
         ResultSetRecords = runQuery( istrSQL );
         ResultSetRecords.next();

         iassmblinforINV = new assmbleInfor( ResultSetRecords.getString( "ASSMBL_DB_ID" ),
               ResultSetRecords.getString( "ASSMBL_CD" ),
               ResultSetRecords.getString( "ASSMBL_BOM_ID" ), null );

         iPARTINV = new simpleIDs( ResultSetRecords.getString( "PART_NO_DB_ID" ),
               ResultSetRecords.getString( "PART_NO_ID" ) );

         iBOMPARTINV = new simpleIDs( ResultSetRecords.getString( "BOM_PART_DB_ID" ),
               ResultSetRecords.getString( "BOM_PART_ID" ) );

      } catch ( SQLException e ) {
         e.printStackTrace();

      }

      // compare evt_inv and inv_inv tables
      Assert.assertTrue( "Check assmbl infor ", iassmblinforEVT.equals( iassmblinforINV ) );
      Assert.assertTrue( "Check part nos are equal  ", iPARTEVT.equals( iPARTINV ) );
      Assert.assertTrue( "Check bom parts are equal  ", iBOMPARTEVT.equals( iBOMPARTINV ) );

   }


   /**
    * This function is to validate sched_stask table
    *
    *
    */
   public void validate_sched_stask( simpleIDs aEventIds, simpleIDs aTASKIds, String aTaskType,
         String aToolBool ) {

      ResultSet ResultSetRecords = null;
      simpleIDs iTASKIds = null;
      String iTaskType = null;
      String lToolReady = null;

      String istrSQL =
            "Select TASK_DB_ID, TASK_ID, TASK_CLASS_CD, TOOLS_READY_BOOL from SCHED_STASK "
                  + " where SCHED_DB_ID=" + aEventIds.getNO_DB_ID() + " and SCHED_ID="
                  + aEventIds.getNO_ID();

      try {
         ResultSetRecords = runQuery( istrSQL );
         ResultSetRecords.next();
         iTASKIds = new simpleIDs( ResultSetRecords.getString( "TASK_DB_ID" ),
               ResultSetRecords.getString( "TASK_ID" ) );
         iTaskType = ResultSetRecords.getString( "TASK_CLASS_CD" );
         lToolReady = ResultSetRecords.getString( "TOOLS_READY_BOOL" );

      } catch ( SQLException e ) {
         e.printStackTrace();

      }
      // Check task IDs, task class cd and HIST_BOOL
      Assert.assertTrue( "Check IDs ", iTASKIds.equals( aTASKIds ) );
      Assert.assertTrue( "Check task class cd  ", iTaskType.equalsIgnoreCase( aTaskType ) );
      Assert.assertTrue( "Check tool ready bool ", aToolBool.equalsIgnoreCase( lToolReady ) );

   }


   /**
    * This function is to validate sched_labour table
    *
    *
    */
   public List<simpleIDs> validate_sched_labour( simpleIDs aTASKIds ) {

      ResultSet ResultSetRecords = null;
      List<simpleIDs> ilistIds = new ArrayList<simpleIDs>();
      ArrayList<String> iListSkillCD = new ArrayList<String>() {

         {
            add( "LBR" );
            add( "HYDRAUL" );
         }
      };
      ArrayList<String> iList = new ArrayList<String>();

      String istrSQL =
            "Select LABOUR_DB_ID, LABOUR_ID, LABOUR_SKILL_CD from SCHED_LABOUR where SCHED_DB_ID="
                  + aTASKIds.getNO_DB_ID() + " and SCHED_ID=" + aTASKIds.getNO_ID();

      try {
         ResultSetRecords = runQuery( istrSQL );

         while ( ResultSetRecords.next() ) {
            simpleIDs ids = new simpleIDs( ResultSetRecords.getString( "LABOUR_DB_ID" ),
                  ResultSetRecords.getString( "LABOUR_ID" ) );
            ilistIds.add( ids );

            String iskillcd = ResultSetRecords.getString( "LABOUR_SKILL_CD" ).trim();
            iList.add( iskillcd );

         }
      } catch ( SQLException e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      checkArraysEqual( iList, iListSkillCD );

      return ilistIds;

   }


   /**
    * This function is to validate sched_labour_role table
    *
    *
    */
   public List<simpleIDs> validate_sched_labour_role( simpleIDs aIds ) {

      ResultSet ResultSetRecords = null;
      List<simpleIDs> ilistIds = new ArrayList<simpleIDs>();
      ArrayList<String> iListTypeCD = new ArrayList<String>() {

         {
            add( "TECH" );
            add( "CERT" );
            add( "INSP" );
         }
      };
      ArrayList<String> iList = new ArrayList<String>();

      String istrSQL =
            "Select LABOUR_ROLE_DB_ID, LABOUR_ROLE_ID, LABOUR_ROLE_TYPE_CD from SCHED_LABOUR_ROLE where LABOUR_DB_ID="
                  + aIds.getNO_DB_ID() + " and LABOUR_ID=" + aIds.getNO_ID();

      try {
         ResultSetRecords = runQuery( istrSQL );

         while ( ResultSetRecords.next() ) {
            simpleIDs ids = new simpleIDs( ResultSetRecords.getString( "LABOUR_ROLE_DB_ID" ),
                  ResultSetRecords.getString( "LABOUR_ROLE_ID" ) );
            ilistIds.add( ids );

            String iTypecd = ResultSetRecords.getString( "LABOUR_ROLE_TYPE_CD" ).trim();
            iList.add( iTypecd );

         }
      } catch ( SQLException e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      checkArraysEqual( iList, iListTypeCD );

      return ilistIds;

   }


   /**
    * This function is to validate sched_labour_role_status table
    *
    *
    */

   public void validate_sched_labour_role_status( List<simpleIDs> aListIds ) {

      ResultSet ResultSetRecords = null;

      ArrayList<String> iListStatusCD = new ArrayList<String>() {

         {
            add( "ACTV" );
            add( "PENDING" );
            add( "PENDING" );
         }
      };
      ArrayList<String> iList = new ArrayList<String>();

      String istrSQL =
            "Select LABOUR_ROLE_STATUS_CD from SCHED_LABOUR_ROLE_STATUS where (LABOUR_ROLE_DB_ID="
                  + aListIds.get( 0 ).getNO_DB_ID() + " and LABOUR_ROLE_ID="
                  + aListIds.get( 0 ).getNO_ID() + ") or (LABOUR_ROLE_DB_ID="
                  + aListIds.get( 1 ).getNO_DB_ID() + " and LABOUR_ROLE_ID="
                  + aListIds.get( 1 ).getNO_ID() + ") or (LABOUR_ROLE_DB_ID="
                  + aListIds.get( 2 ).getNO_DB_ID() + "  and LABOUR_ROLE_ID="
                  + aListIds.get( 2 ).getNO_ID() + ")";

      try {
         ResultSetRecords = runQuery( istrSQL );

         while ( ResultSetRecords.next() ) {

            String iStatuscd = ResultSetRecords.getString( "LABOUR_ROLE_STATUS_CD" ).trim();
            iList.add( iStatuscd );

         }
      } catch ( SQLException e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      checkArraysEqual( iList, iListStatusCD );

   }


   /**
    * This function is to validate evt_tools table
    *
    *
    */
   public void validate_evt_tool( simpleIDs aSchedTaskIDs, simpleIDs aTASKIDs,
         simpleIDs aBomPartIDs ) {

      ResultSet ResultSetRecords = null;

      String lstrSQL =
            "Select EVENT_DB_ID, EVENT_ID, TOOL_ID, PART_NO_DB_ID, PART_NO_ID, TASK_DB_ID, TASK_ID "
                  + "from evt_tool where BOM_PART_DB_ID=" + aBomPartIDs.getNO_DB_ID()
                  + " and BOM_PART_ID=" + aBomPartIDs.getNO_ID();
      simpleIDs lPartidsOrigin = getPartNoIds( aBomPartIDs );

      try {
         ResultSetRecords = runQuery( lstrSQL );
         ResultSetRecords.next();

         simpleIDs lEVENTids = new simpleIDs( ResultSetRecords.getString( "EVENT_DB_ID" ),
               ResultSetRecords.getString( "EVENT_ID" ) );

         simpleIDs lTASKids = new simpleIDs( ResultSetRecords.getString( "TASK_DB_ID" ),
               ResultSetRecords.getString( "TASK_ID" ) );

         simpleIDs lPARTids = new simpleIDs( ResultSetRecords.getString( "PART_NO_DB_ID" ),
               ResultSetRecords.getString( "PART_NO_ID" ) );

         Assert.assertTrue( "Checking event ids. ", lEVENTids.equals( aSchedTaskIDs ) );
         Assert.assertTrue( "Checking task ids. ", lTASKids.equals( aTASKIDs ) );
         Assert.assertTrue( "Checking part no ids. ", lPartidsOrigin.equals( lPARTids ) );

      } catch ( SQLException e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }


   /**
    * This function is to validate sched_step table
    *
    *
    */
   public void validate_sched_step( simpleIDs aSchedTaskIDs, String aStepORD, stepdata adata ) {

      ResultSet ResultSetRecords = null;

      String lstrSQL = "Select STEP_LDESC, STEP_STATUS_CD " + "from sched_step where SCHED_DB_ID="
            + aSchedTaskIDs.getNO_DB_ID() + " and SCHED_ID=" + aSchedTaskIDs.getNO_ID()
            + " and STEP_ORD=" + aStepORD;

      try {
         ResultSetRecords = runQuery( lstrSQL );
         ResultSetRecords.next();

         stepdata lstepdata = new stepdata( ResultSetRecords.getString( "STEP_LDESC" ),
               ResultSetRecords.getString( "STEP_STATUS_CD" ) );

         Assert.assertTrue( "Checking step data: ", lstepdata.equals( adata ) );

      } catch ( SQLException e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }


   /**
    * This function is to validate sched_zone table
    *
    *
    */
   public void validate_sched_zone( simpleIDs aSchedTaskIDs, String aZoneCD ) {

      simpleIDs lIds = getZoneIds( aZoneCD );

      ResultSet ResultSetRecords = null;

      String lstrSQL = "Select COUNT(*) " + "from sched_zone where SCHED_DB_ID="
            + aSchedTaskIDs.getNO_DB_ID() + " and SCHED_ID=" + aSchedTaskIDs.getNO_ID()
            + " and zone_db_id=" + lIds.getNO_DB_ID() + " and zone_id=" + lIds.getNO_ID();

      try {
         ResultSetRecords = runQuery( lstrSQL );
         ResultSetRecords.next();

         String lcount = ResultSetRecords.getString( "COUNT(*)" );

         Assert.assertTrue( "Checking zone record exist: ", Integer.parseInt( lcount ) == 1 );

      } catch ( SQLException e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }


   /**
    * This function is to validate sched_panel table
    *
    *
    */
   public void validate_sched_panel( simpleIDs aSchedTaskIDs, String aPanelCD ) {

      simpleIDs lIds = getPanelIds( aPanelCD );

      ResultSet ResultSetRecords = null;

      String lstrSQL = "Select COUNT(*) " + "from sched_panel where SCHED_DB_ID="
            + aSchedTaskIDs.getNO_DB_ID() + " and SCHED_ID=" + aSchedTaskIDs.getNO_ID()
            + " and panel_db_id=" + lIds.getNO_DB_ID() + " and panel_id=" + lIds.getNO_ID();

      try {
         ResultSetRecords = runQuery( lstrSQL );
         ResultSetRecords.next();

         String lcount = ResultSetRecords.getString( "COUNT(*)" );

         Assert.assertTrue( "Checking panel record exist: ", Integer.parseInt( lcount ) == 1 );

      } catch ( SQLException e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }


   /**
    * This function is to validate sched_worktype table
    *
    *
    */
   public void validate_sched_worktype( simpleIDs aSchedTaskIDs, String aWorkTypeCD ) {

      String lDbId = getWorkTypeDBId( aWorkTypeCD );

      ResultSet ResultSetRecords = null;

      String lstrSQL = "Select COUNT(*) " + "from sched_work_type where SCHED_DB_ID="
            + aSchedTaskIDs.getNO_DB_ID() + " and SCHED_ID=" + aSchedTaskIDs.getNO_ID()
            + " and work_type_db_id=" + lDbId + " and work_type_cd='" + aWorkTypeCD + "'";

      try {
         ResultSetRecords = runQuery( lstrSQL );
         ResultSetRecords.next();

         String lcount = ResultSetRecords.getString( "COUNT(*)" );

         Assert.assertTrue( "Checking work type record exist: ", Integer.parseInt( lcount ) == 1 );

      } catch ( SQLException e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }


   /**
    * This function is to validate inv_parm_data table
    *
    *
    */

   public void validate_inv_parm_data( simpleIDs aSchedTaskIDs, simpleIDs aMimDataTypeIds,
         simpleIDs aInvIds, String aOrd ) {

      ResultSet ResultSetRecords = null;

      String lstrSQL = "Select COUNT(*) " + "from inv_parm_data where EVENT_DB_ID="
            + aSchedTaskIDs.getNO_DB_ID() + " and EVENT_ID=" + aSchedTaskIDs.getNO_ID()
            + " and data_type_db_id=" + aMimDataTypeIds.getNO_DB_ID() + " and data_type_id="
            + aMimDataTypeIds.getNO_ID() + " and inv_no_db_id=" + aInvIds.getNO_DB_ID()
            + " and inv_no_id=" + aInvIds.getNO_ID() + " and data_ord=" + aOrd;

      try {
         ResultSetRecords = runQuery( lstrSQL );
         ResultSetRecords.next();

         String lcount = ResultSetRecords.getString( "COUNT(*)" );

         Assert.assertTrue( "Checking inv parm data record exist: ",
               Integer.parseInt( lcount ) == 1 );

      } catch ( SQLException e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }


   /**
    * This function is to validate evt_attach_data table
    *
    *
    */
   public void validate_evt_attach_data( simpleIDs aSchedTaskIDs, simpleIDs aIetmIds,
         int aExpectedValue ) {

      ResultSet ResultSetRecords = null;

      String lstrSQL = "Select COUNT(*) " + "from evt_attach where EVENT_DB_ID="
            + aSchedTaskIDs.getNO_DB_ID() + " and EVENT_ID=" + aSchedTaskIDs.getNO_ID()
            + " and IETM_DB_ID=" + aIetmIds.getNO_DB_ID() + " and IETM_ID=" + aIetmIds.getNO_ID();

      try {
         ResultSetRecords = runQuery( lstrSQL );
         ResultSetRecords.next();

         String lcount = ResultSetRecords.getString( "COUNT(*)" );

         Assert.assertTrue( "Checking evt_attach record exist: ",
               Integer.parseInt( lcount ) == aExpectedValue );

      } catch ( SQLException e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }


   /**
    * This function is to validate evt_ietm_data table
    *
    *
    */
   public void validate_evt_ietm_data( simpleIDs aSchedTaskIDs, simpleIDs aIetmIds,
         int aExpectedValue ) {

      ResultSet ResultSetRecords = null;

      String lstrSQL = "Select COUNT(*) " + "from evt_ietm where EVENT_DB_ID="
            + aSchedTaskIDs.getNO_DB_ID() + " and EVENT_ID=" + aSchedTaskIDs.getNO_ID()
            + " and IETM_DB_ID=" + aIetmIds.getNO_DB_ID() + " and IETM_ID=" + aIetmIds.getNO_ID();

      try {
         ResultSetRecords = runQuery( lstrSQL );
         ResultSetRecords.next();

         String lcount = ResultSetRecords.getString( "COUNT(*)" );

         Assert.assertTrue( "Checking evt_attach record exist: ",
               Integer.parseInt( lcount ) == aExpectedValue );

      } catch ( SQLException e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }


   /**
    * This function is to clear tables after each test by given IDs
    *
    *
    */
   public void resumeTables( simpleIDs aSchedTaskIDs, simpleIDs aTASKIDs, simpleIDs aTASKDEFNIDs,
         simpleIDs aACFTIDs ) {

      PreparedStatement lStatement;
      String[] iSQLList = new String[6];
      iSQLList[0] = "delete from SCHED_STASK where SCHED_DB_ID=" + aSchedTaskIDs.getNO_DB_ID()
            + " and SCHED_ID=" + aSchedTaskIDs.getNO_ID();
      iSQLList[1] = "delete from EVT_INV where EVENT_DB_ID=" + aSchedTaskIDs.getNO_DB_ID()
            + " and EVENT_ID=" + aSchedTaskIDs.getNO_ID();
      iSQLList[2] = "delete from EVT_EVENT where EVENT_DB_ID=" + aSchedTaskIDs.getNO_DB_ID()
            + " and EVENT_ID=" + aSchedTaskIDs.getNO_ID();
      iSQLList[3] = "delete from TASK_TASK where TASK_DB_ID=" + aTASKIDs.getNO_DB_ID()
            + " and TASK_ID=" + aTASKIDs.getNO_ID();
      iSQLList[4] = "delete from TASK_DEFN where TASK_DEFN_DB_ID=" + aTASKDEFNIDs.getNO_DB_ID()
            + " and TASK_DEFN_ID=" + aTASKDEFNIDs.getNO_ID();
      iSQLList[5] = "delete from INV_INV where INV_NO_DB_ID=" + aACFTIDs.getNO_DB_ID()
            + " and INV_NO_ID=" + aACFTIDs.getNO_ID();

      for ( int i = 0; i < 6; i++ ) {
         try {
            lStatement = getConnection().prepareStatement( iSQLList[i] );

            lStatement.executeUpdate( iSQLList[i] );
            commit();
         } catch ( SQLException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }

   }


   /**
    * This function is to clear tables after each test by given task IDs
    *
    *
    */

   public void removeTableData( String atablename, simpleIDs aTASKIDs ) {

      String istrSQL = "delete from " + atablename + " where TASK_DB_ID=" + aTASKIDs.getNO_DB_ID()
            + " and TASK_ID=" + aTASKIDs.getNO_ID();

      PreparedStatement lStatement;
      try {
         lStatement = getConnection().prepareStatement( istrSQL );
         lStatement.executeUpdate( istrSQL );
         commit();
      } catch ( SQLException e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }


   /**
    * This function is to clear Event tables after each test by given IDs
    *
    *
    */
   public void removeTableEventData( String atablename, simpleIDs aTASKIDs ) {

      String istrSQL = "delete from " + atablename + " where EVENT_DB_ID=" + aTASKIDs.getNO_DB_ID()
            + " and EVENT_ID=" + aTASKIDs.getNO_ID();

      PreparedStatement lStatement;
      try {
         lStatement = getConnection().prepareStatement( istrSQL );
         lStatement.executeUpdate( istrSQL );
         commit();
      } catch ( SQLException e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }


   /**
    * This function is to clear ietm tables after each test by given IDs
    *
    *
    */
   public void removeTableDataIetm( String atablename, simpleIDs aIetmIDs ) {

      String istrSQL = "delete from " + atablename + " where IETM_DB_ID=" + aIetmIDs.getNO_DB_ID()
            + " and IETM_ID=" + aIetmIDs.getNO_ID();

      PreparedStatement lStatement;
      try {
         lStatement = getConnection().prepareStatement( istrSQL );
         lStatement.executeUpdate( istrSQL );
         commit();
      } catch ( SQLException e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }


   /**
    * This function is to clear tables after each test by given IDs
    *
    *
    */
   public void removeTableData_mimDataType( String atablename, simpleIDs aTASKIDs ) {

      String istrSQL = "delete from " + atablename + " where DATA_TYPE_DB_ID="
            + aTASKIDs.getNO_DB_ID() + " and DATA_TYPE_ID=" + aTASKIDs.getNO_ID();

      PreparedStatement lStatement;
      try {
         lStatement = getConnection().prepareStatement( istrSQL );
         lStatement.executeUpdate( istrSQL );
         commit();
      } catch ( SQLException e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }


   /**
    * This function is to clear labor related tables after each test.
    *
    *
    */
   public void clearLabourTables() {

      try {
         for ( String lTableName : TableUtil.SCHED_LABOUR_TABLES ) {

            deleteAllFromTable( lTableName );

         }
      } catch ( SQLException e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }


   /**
    * This function is to clear tool related tables after each test.
    *
    *
    */
   public void clearToolTables() {

      try {
         for ( String lTableName : TableUtil.SCHED_TOOL_TABLES ) {

            deleteAllFromTable( lTableName );

         }
      } catch ( SQLException e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }


   /**
    * This function is to clear step related tables after each test.
    *
    *
    */
   public void clearStepTables() {

      try {
         for ( String lTableName : TableUtil.SCHED_TOOL_TABLES ) {

            deleteAllFromTable( lTableName );

         }
      } catch ( SQLException e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }


   /**
    * This function is to clear zone related tables after each test.
    *
    *
    */
   public void clearZoneTables() {

      try {
         for ( String lTableName : TableUtil.SCHED_ZONE_TABLES ) {

            deleteAllFromTable( lTableName );

         }
      } catch ( SQLException e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }


   /**
    * This function is to clear panel related tables after each test.
    *
    *
    */
   public void clearPanelTables() {

      try {
         for ( String lTableName : TableUtil.SCHED_PANEL_TABLES ) {

            deleteAllFromTable( lTableName );

         }
      } catch ( SQLException e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }


   /**
    * This function is to clear work type related tables after each test.
    *
    *
    */
   public void clearWorkTypeTables() {

      try {
         for ( String lTableName : TableUtil.SCHED_WORKTYPE_TABLES ) {

            deleteAllFromTable( lTableName );

         }
      } catch ( SQLException e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }


   /**
    * This function is to clear inv_parm related tables after each test.
    *
    *
    */
   public void clearInvParmTables() {

      try {
         for ( String lTableName : TableUtil.SCHED_ASSMBLYMEASURES_TABLES ) {

            deleteAllFromTable( lTableName );

         }
      } catch ( SQLException e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }


   /**
    * This class is internal used class to store data of step.
    *
    *
    */
   public class stepdata {

      String STEP_LDESC;
      String STEP_STATUS_CD;


      public stepdata(String STEP_LDESC, String STEP_STATUS_CD) {
         this.STEP_LDESC = STEP_LDESC;
         this.STEP_STATUS_CD = STEP_STATUS_CD;
      }


      /**
       * Returns the value of the sTEP_LDESC property.
       *
       * @return the value of the sTEP_LDESC property
       */
      public String getSTEP_LDESC() {
         return STEP_LDESC;
      }


      /**
       * Sets a new value for the sTEP_LDESC property.
       *
       * @param aSTEP_LDESC
       *           the new value for the sTEP_LDESC property
       */
      public void setSTEP_LDESC( String aSTEP_LDESC ) {
         STEP_LDESC = aSTEP_LDESC;
      }


      /**
       * Returns the value of the sTEP_STATUS_CD property.
       *
       * @return the value of the sTEP_STATUS_CD property
       */
      public String getSTEP_STATUS_CD() {
         return STEP_STATUS_CD;
      }


      /**
       * Sets a new value for the sTEP_STATUS_CD property.
       *
       * @param aSTEP_STATUS_CD
       *           the new value for the sTEP_STATUS_CD property
       */
      public void setSTEP_STATUS_CD( String aSTEP_STATUS_CD ) {
         STEP_STATUS_CD = aSTEP_STATUS_CD;
      }


      /**
       * {@inheritDoc}
       */
      @Override
      public int hashCode() {
         final int prime = 31;
         int result = 1;
         result = prime * result + getOuterType().hashCode();
         result = prime * result + ( ( STEP_LDESC == null ) ? 0 : STEP_LDESC.hashCode() );
         result = prime * result + ( ( STEP_STATUS_CD == null ) ? 0 : STEP_STATUS_CD.hashCode() );
         return result;
      }


      /**
       * {@inheritDoc}
       */
      @Override
      public boolean equals( Object obj ) {
         if ( this == obj )
            return true;
         if ( obj == null )
            return false;
         if ( getClass() != obj.getClass() )
            return false;
         stepdata other = ( stepdata ) obj;
         if ( !getOuterType().equals( other.getOuterType() ) )
            return false;
         if ( STEP_LDESC == null ) {
            if ( other.STEP_LDESC != null )
               return false;
         } else if ( !STEP_LDESC.equalsIgnoreCase( other.STEP_LDESC ) )
            return false;
         if ( STEP_STATUS_CD == null ) {
            if ( other.STEP_STATUS_CD != null )
               return false;
         } else if ( !STEP_STATUS_CD.equalsIgnoreCase( other.STEP_STATUS_CD ) )
            return false;
         return true;
      }


      private GenOneSchedTaskTest getOuterType() {
         return GenOneSchedTaskTest.this;
      }

   }

   /**
    * This class is internal used class to store data of mim data.
    *
    *
    */

   public class MimData {

      String DATA_TYPE_DB_ID;
      String DATA_TYPE_ID;
      String ENG_UNIT_DB_ID;
      String ENG_UNIT_CD;
      String DOMAIN_TYPE_DB_ID;
      String DOMAIN_TYPE_CD;
      String ENTRY_PREC_QT;
      String DATA_TYPE_CD;
      String DATA_TYPE_SDESC;
      String DATA_TYPE_MDESC;
      String FORECAST_BOOL;


      public MimData(String DATA_TYPE_DB_ID, String DATA_TYPE_ID, String ENG_UNIT_DB_ID,
            String ENG_UNIT_CD, String DOMAIN_TYPE_DB_ID, String DOMAIN_TYPE_CD,
            String ENTRY_PREC_QT, String DATA_TYPE_CD, String DATA_TYPE_SDESC,
            String DATA_TYPE_MDESC, String FORECAST_BOOL) {
         this.DATA_TYPE_DB_ID = DATA_TYPE_DB_ID;
         this.DATA_TYPE_ID = DATA_TYPE_ID;
         this.ENG_UNIT_DB_ID = ENG_UNIT_DB_ID;
         this.ENG_UNIT_CD = ENG_UNIT_CD;
         this.DOMAIN_TYPE_DB_ID = DOMAIN_TYPE_DB_ID;
         this.DOMAIN_TYPE_CD = DOMAIN_TYPE_CD;
         this.ENTRY_PREC_QT = ENTRY_PREC_QT;
         this.DATA_TYPE_CD = DATA_TYPE_CD;
         this.DATA_TYPE_SDESC = DATA_TYPE_SDESC;
         this.DATA_TYPE_MDESC = DATA_TYPE_MDESC;
         this.FORECAST_BOOL = FORECAST_BOOL;

      }


      /**
       * Returns the value of the dATA_TYPE_DB_ID property.
       *
       * @return the value of the dATA_TYPE_DB_ID property
       */
      public String getDATA_TYPE_DB_ID() {
         return DATA_TYPE_DB_ID;
      }


      /**
       * Sets a new value for the dATA_TYPE_DB_ID property.
       *
       * @param aDATA_TYPE_DB_ID
       *           the new value for the dATA_TYPE_DB_ID property
       */
      public void setDATA_TYPE_DB_ID( String aDATA_TYPE_DB_ID ) {
         DATA_TYPE_DB_ID = aDATA_TYPE_DB_ID;
      }


      /**
       * Returns the value of the dATA_TYPE_ID property.
       *
       * @return the value of the dATA_TYPE_ID property
       */
      public String getDATA_TYPE_ID() {
         return DATA_TYPE_ID;
      }


      /**
       * Sets a new value for the dATA_TYPE_ID property.
       *
       * @param aDATA_TYPE_ID
       *           the new value for the dATA_TYPE_ID property
       */
      public void setDATA_TYPE_ID( String aDATA_TYPE_ID ) {
         DATA_TYPE_ID = aDATA_TYPE_ID;
      }


      /**
       * Returns the value of the eNG_UNIT_CD property.
       *
       * @return the value of the eNG_UNIT_CD property
       */
      public String getENG_UNIT_CD() {
         return ENG_UNIT_CD;
      }


      /**
       * Sets a new value for the eNG_UNIT_CD property.
       *
       * @param aENG_UNIT_CD
       *           the new value for the eNG_UNIT_CD property
       */
      public void setENG_UNIT_CD( String aENG_UNIT_CD ) {
         ENG_UNIT_CD = aENG_UNIT_CD;
      }


      /**
       * Returns the value of the dOMAIN_TYPE_DB_ID property.
       *
       * @return the value of the dOMAIN_TYPE_DB_ID property
       */
      public String getDOMAIN_TYPE_DB_ID() {
         return DOMAIN_TYPE_DB_ID;
      }


      /**
       * Sets a new value for the dOMAIN_TYPE_DB_ID property.
       *
       * @param aDOMAIN_TYPE_DB_ID
       *           the new value for the dOMAIN_TYPE_DB_ID property
       */
      public void setDOMAIN_TYPE_DB_ID( String aDOMAIN_TYPE_DB_ID ) {
         DOMAIN_TYPE_DB_ID = aDOMAIN_TYPE_DB_ID;
      }


      /**
       * Returns the value of the dOMAIN_TYPE_CD property.
       *
       * @return the value of the dOMAIN_TYPE_CD property
       */
      public String getDOMAIN_TYPE_CD() {
         return DOMAIN_TYPE_CD;
      }


      /**
       * Sets a new value for the dOMAIN_TYPE_CD property.
       *
       * @param aDOMAIN_TYPE_CD
       *           the new value for the dOMAIN_TYPE_CD property
       */
      public void setDOMAIN_TYPE_CD( String aDOMAIN_TYPE_CD ) {
         DOMAIN_TYPE_CD = aDOMAIN_TYPE_CD;
      }


      /**
       * Returns the value of the eNTRY_PREC_QT property.
       *
       * @return the value of the eNTRY_PREC_QT property
       */
      public String getENTRY_PREC_QT() {
         return ENTRY_PREC_QT;
      }


      /**
       * Sets a new value for the eNTRY_PREC_QT property.
       *
       * @param aENTRY_PREC_QT
       *           the new value for the eNTRY_PREC_QT property
       */
      public void setENTRY_PREC_QT( String aENTRY_PREC_QT ) {
         ENTRY_PREC_QT = aENTRY_PREC_QT;
      }


      /**
       * Returns the value of the dATA_TYPE_CD property.
       *
       * @return the value of the dATA_TYPE_CD property
       */
      public String getDATA_TYPE_CD() {
         return DATA_TYPE_CD;
      }


      /**
       * Sets a new value for the dATA_TYPE_CD property.
       *
       * @param aDATA_TYPE_CD
       *           the new value for the dATA_TYPE_CD property
       */
      public void setDATA_TYPE_CD( String aDATA_TYPE_CD ) {
         DATA_TYPE_CD = aDATA_TYPE_CD;
      }


      /**
       * Returns the value of the dATA_TYPE_SDESC property.
       *
       * @return the value of the dATA_TYPE_SDESC property
       */
      public String getDATA_TYPE_SDESC() {
         return DATA_TYPE_SDESC;
      }


      /**
       * Sets a new value for the dATA_TYPE_SDESC property.
       *
       * @param aDATA_TYPE_SDESC
       *           the new value for the dATA_TYPE_SDESC property
       */
      public void setDATA_TYPE_SDESC( String aDATA_TYPE_SDESC ) {
         DATA_TYPE_SDESC = aDATA_TYPE_SDESC;
      }


      /**
       * Returns the value of the dATA_TYPE_MDESC property.
       *
       * @return the value of the dATA_TYPE_MDESC property
       */
      public String getDATA_TYPE_MDESC() {
         return DATA_TYPE_MDESC;
      }


      /**
       * Sets a new value for the dATA_TYPE_MDESC property.
       *
       * @param aDATA_TYPE_MDESC
       *           the new value for the dATA_TYPE_MDESC property
       */
      public void setDATA_TYPE_MDESC( String aDATA_TYPE_MDESC ) {
         DATA_TYPE_MDESC = aDATA_TYPE_MDESC;
      }


      /**
       * Returns the value of the fORECAST_BOOL property.
       *
       * @return the value of the fORECAST_BOOL property
       */
      public String getFORECAST_BOOL() {
         return FORECAST_BOOL;
      }


      /**
       * Sets a new value for the fORECAST_BOOL property.
       *
       * @param aFORECAST_BOOL
       *           the new value for the fORECAST_BOOL property
       */
      public void setFORECAST_BOOL( String aFORECAST_BOOL ) {
         FORECAST_BOOL = aFORECAST_BOOL;
      }


      /**
       * Returns the value of the eNG_UNIT_DB_ID property.
       *
       * @return the value of the eNG_UNIT_DB_ID property
       */
      public String getENG_UNIT_DB_ID() {
         return ENG_UNIT_DB_ID;
      }


      /**
       * Sets a new value for the eNG_UNIT_DB_ID property.
       *
       * @param aENG_UNIT_DB_ID
       *           the new value for the eNG_UNIT_DB_ID property
       */
      public void setENG_UNIT_DB_ID( String aENG_UNIT_DB_ID ) {
         ENG_UNIT_DB_ID = aENG_UNIT_DB_ID;
      }

   }


   /**
    * This function is to insert blob data into a table
    *
    *
    */
   public void insertBlobViaSetBlob( String tableName, byte value[], simpleIDs aIetmIds ) {

      PreparedStatement lStatement = null;
      Blob blob = null;

      try {
         blob = getConnection().createBlob();
         lStatement = getConnection().prepareStatement( String
               .format( "UPDATE %s SET ATTACH_BLOB=? where IETM_DB_ID=" + aIetmIds.getNO_DB_ID()
                     + " and IETM_ID=" + aIetmIds.getNO_ID(), tableName ) );
         OutputStream out = new BufferedOutputStream( blob.setBinaryStream( 1L ) );
         out.write( value );

         lStatement.setBlob( 1, blob );
         lStatement.executeUpdate();
         commit();

      } catch ( SQLException e ) {

         e.printStackTrace();
      } catch ( IOException e ) {
         e.printStackTrace();
      }

   }
}
