package com.mxi.mx.core.maint.plan.corePKGs.schedStaskPKG;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.core.maint.plan.datamodels.assmbleInfor;
import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.GenOneSchedTaskTest;
import com.mxi.mx.util.TableUtil;


/**
 * This test suite contains test cases on validation of core procedure GenOneSchedTask package.
 *
 * @author ALICIA QIAN
 */
public class GenOneSchedTask extends GenOneSchedTaskTest {

   @Rule
   public TestName testName = new TestName();

   public simpleIDs ACFTIDs;
   public simpleIDs ASSYIDs;
   public simpleIDs TRKIDs;

   public simpleIDs TASKDEFNIDs;
   public simpleIDs TASKIDs;
   public simpleIDs schedTaskIDs_ACFT;
   public simpleIDs schedTaskIDs_ASSY;
   public simpleIDs schedTaskIDs_TRK;

   public simpleIDs ietm1IDs;
   public simpleIDs ietm2IDs;

   public simpleIDs iTool1;
   public simpleIDs iTool2;

   public simpleIDs MIMDATATYPEIDs1;
   public simpleIDs MIMDATATYPEIDs2;

   public String iTaskCD = "AUTO-TASK-CD";
   public String iTaskName = "AUTO-TASK-NAME";
   public String iTaskACTVStatusCD = "ACTV";
   public String iTaskCOMPLETEStatusCD = "COMPLETE";
   public String iTaskTypeSRVC = "SRVC";
   public String iTaskTypeJIC = "JIC";

   public String iACFTSDSC = "AircraftPart-AUTO-SDESC";
   public String iACFTSN = "ACFT-AUTO-SN";
   public String iASSYSDSC = "ASSYPART-AUTO-SDESC";
   public String iASSYSN = "ASSYPART-AUTO-SN";
   public String iTRKSDSC = "TRKPART-AUTO-SDESC";
   public String iTRKSN = "TRKPART-AUTO-SN";
   public String iAPPLEFFCDINV = "901-904";
   public String iAPPLEFFCDTOPIC = "902-904";

   public String iZone1 = "ZONE1-1";
   public String iZone2 = "ZONE3";

   public String iPanel1 = "P2-2-001";
   public String iPanel2 = "P1-2-001";

   public String iWorkType1 = "HANGAR";
   public String iWorkType2 = "FUEL";

   public String iParmDESC1 = "AUTOMATION1";
   public String iParmDESC2 = "AUTOMATION2";

   public String iIetm1DESC = "AUTOMATION1";
   public String iIetm2DESC = "AUTOMATION2";
   public String iAttachType = "CONTRACT";


   /**
    * There would be different data setup for each test case. The common ones are creating inventory
    * for assembly, task creation and task definition creation steps
    */
   @Override
   @Before
   public void before() throws Exception {

      super.before();
      String strTCName = testName.getMethodName();
      if ( strTCName.contains( "ACFT" ) ) {
         ACFTIDs = DataSetup_ACFT();
         TASKDEFNIDs = DataSetup_TASKDEFN();
      } else if ( strTCName.contains( "ASSY_ENG" ) ) {
         ASSYIDs = DataSetup_ENG();
         TASKDEFNIDs = DataSetup_TASKDEFN();
      } else if ( strTCName.contains( "ASSY" ) ) {
         ASSYIDs = DataSetup_ASSY();
         TASKDEFNIDs = DataSetup_TASKDEFN();
      } else {
         TRKIDs = DataSetup_TRK();
         TASKDEFNIDs = DataSetup_TASKDEFN();
         TASKIDs = DataSetup_TASK( TRKIDs, iTaskTypeSRVC, "1" );

      }

      if ( strTCName.contains( "Historic" ) ) {
         TASKIDs = DataSetup_TASK( ACFTIDs, iTaskTypeSRVC, "1" );
      }

      if ( strTCName.contains( "Labour" ) ) {
         TASKIDs = DataSetup_TASK( ASSYIDs, iTaskTypeJIC, "0" );
         DataSetup_LABOUR( TASKIDs );
         // clearLabourTables();
      }

      if ( strTCName.contains( "Tool" ) ) {
         TASKIDs = DataSetup_TASK( ASSYIDs, iTaskTypeSRVC, "1" );
         DataSetup_TOOL( TASKIDs );

      }

      if ( strTCName.contains( "Step" ) ) {
         TASKIDs = DataSetup_TASK( ASSYIDs, iTaskTypeSRVC, "1" );
         DataSetup_STEP( TASKIDs );

      }

      if ( strTCName.contains( "Zone" ) ) {
         TASKIDs = DataSetup_TASK( ACFTIDs, iTaskTypeJIC, "1" );
         DataSetup_ZONE( TASKIDs );

      }

      if ( strTCName.contains( "Panel" ) ) {
         TASKIDs = DataSetup_TASK( ACFTIDs, iTaskTypeJIC, "1" );
         DataSetup_PANEL( TASKIDs );

      }

      if ( strTCName.contains( "WorkType" ) ) {
         TASKIDs = DataSetup_TASK( ACFTIDs, iTaskTypeJIC, "1" );
         DataSetup_WORKTYPE( TASKIDs );

      }

      if ( strTCName.contains( "MeasuresOnly" ) ) {
         TASKIDs = DataSetup_TASK( ACFTIDs, iTaskTypeJIC, "1" );
         DataSetup_MeasuresOnly( TASKIDs );

      }

      if ( strTCName.contains( "AssmblyMeasures" ) ) {
         TASKIDs = DataSetup_TASK( ASSYIDs, iTaskTypeJIC, "0" );
         DataSetup_AssmblyMeasures( TASKIDs );
      }

      if ( strTCName.contains( "Ietm_Blob" ) ) {
         ietm1IDs = DataSetup_Ietm( TASKIDs, true, iIetm1DESC );
         ietm2IDs = DataSetup_Ietm( TASKIDs, true, iIetm2DESC );
      }

      if ( strTCName.contains( "Ietm_NABlob" ) ) {
         ietm1IDs = DataSetup_Ietm( TASKIDs, false, iIetm1DESC );
         ietm2IDs = DataSetup_Ietm( TASKIDs, false, iIetm2DESC );
      }

   }


   /**
    * Clean up after each individual test
    *
    *
    */
   @After
   @Override
   public void after() throws Exception {
      String strTCName = testName.getMethodName();
      if ( strTCName.contains( "ACFT" ) ) {
         resumeTables( schedTaskIDs_ACFT, TASKIDs, TASKDEFNIDs, ACFTIDs );
      } else if ( strTCName.contains( "ASSY" ) ) {
         resumeTables( schedTaskIDs_ASSY, TASKIDs, TASKDEFNIDs, ASSYIDs );
      } else {
         resumeTables( schedTaskIDs_TRK, TASKIDs, TASKDEFNIDs, TRKIDs );

      }

      if ( strTCName.contains( "Labour" ) ) {
         removeTableData( TableUtil.TASK_LABOUR_LIST, TASKIDs );
         clearLabourTables();
      }

      if ( strTCName.contains( "Tool" ) ) {
         clearToolTables();
      }

      if ( strTCName.contains( "Step" ) ) {
         removeTableData( TableUtil.TASK_STEP, TASKIDs );
         clearStepTables();
      }

      if ( strTCName.contains( "Zone" ) ) {
         removeTableData( TableUtil.TASK_ZONE, TASKIDs );
         clearZoneTables();
      }

      if ( strTCName.contains( "Panel" ) ) {
         removeTableData( TableUtil.TASK_PANEL, TASKIDs );
         clearPanelTables();
      }

      if ( strTCName.contains( "WorkType" ) ) {
         removeTableData( TableUtil.TASK_WORK_TYPE, TASKIDs );
         clearPanelTables();
      }

      if ( strTCName.contains( "MeasuresOnly" ) ) {
         removeTableData( TableUtil.TASK_PARM_DATA, TASKIDs );
         removeTableData_mimDataType( TableUtil.MIM_DATA_TYPE, MIMDATATYPEIDs1 );
         removeTableData_mimDataType( TableUtil.MIM_DATA_TYPE, MIMDATATYPEIDs2 );
         clearInvParmTables();

      }

      if ( strTCName.contains( "AssmblyMeasures" ) ) {
         removeTableData( TableUtil.TASK_PARM_DATA, TASKIDs );
         removeTableData_mimDataType( TableUtil.MIM_DATA_TYPE, MIMDATATYPEIDs1 );
         removeTableData_mimDataType( TableUtil.MIM_DATA_TYPE, MIMDATATYPEIDs2 );
         removeTableData_mimDataType( TableUtil.REF_DATA_TYPE_ASSMBL_CLASS, MIMDATATYPEIDs1 );
         removeTableData_mimDataType( TableUtil.REF_DATA_TYPE_ASSMBL_CLASS, MIMDATATYPEIDs2 );
         clearInvParmTables();

      }

      if ( strTCName.contains( "Ietm" ) ) {
         removeTableEventData( TableUtil.EVT_ATTACH, schedTaskIDs_TRK );
         removeTableEventData( TableUtil.EVT_IETM, schedTaskIDs_TRK );
         removeTableData( TableUtil.TASK_TASK_IETM, TASKIDs );
         removeTableDataIetm( TableUtil.IETM_TOPIC, ietm1IDs );
         removeTableDataIetm( TableUtil.IETM_TOPIC, ietm2IDs );
         removeTableDataIetm( TableUtil.IETM_IETM, ietm1IDs );
         removeTableDataIetm( TableUtil.IETM_IETM, ietm2IDs );

      }

      super.after();

   }


   /**
    * Verify non historical task on ACFT
    *
    *
    */

   @Test
   public void test_No_Historic_ACFT() {
      System.out.print( "===== Starting " + testName.getMethodName() + " ==================" );

      simpleIDs lpretaskIDs = new simpleIDs( "-1", "-1" );
      String lstrSQL = "select * from ref_stage_reason where event_status_cd='ACTV'";
      int lan_reasondbid = Integer.parseInt( getStringValue( lstrSQL, "STAGE_REASON_DB_ID" ) );
      simpleIDs lhrids = getHRIds();
      int lHistbool = 0;
      String lToolBool = "1";

      validationReturn lrtrn =
            runValidation( null, ACFTIDs, TASKIDs, lpretaskIDs, null, lan_reasondbid,
                  "ReasonCD: Historic_ACFT", "USERNOTE:Historic_ACFT", lhrids, false, false, true );
      Assert.assertTrue( "The return code 1. ", lrtrn.getReturnCode() == 1 );
      schedTaskIDs_ACFT = lrtrn.getSchedids();

      // Validation on evt_event table
      validateEvt_Event( lrtrn.getSchedids(), iTaskACTVStatusCD, lrtrn.getSchedids(),
            ( iTaskCD + " (" + iTaskName + ")" ), lHistbool );

      // validate evt_inv table
      validateEvt_Inv( lrtrn.getSchedids(), ACFTIDs, "ACFT" );

      // validate sched_stask table
      validate_sched_stask( lrtrn.getSchedids(), TASKIDs, iTaskTypeSRVC, lToolBool );

      System.out.print( "===== Ending " + testName.getMethodName() + " ==================" );

   }


   /**
    * Verify historical task on ACFT
    *
    *
    */

   @Test
   public void test_Historic_ACFT() {
      System.out.print( "===== Starting " + testName.getMethodName() + " ==================" );

      simpleIDs lpretaskIDs = new simpleIDs( "-1", "-1" );
      String lstrSQL = "select * from ref_stage_reason where event_status_cd='ACTV'";
      int lan_reasondbid = Integer.parseInt( getStringValue( lstrSQL, "STAGE_REASON_DB_ID" ) );
      simpleIDs lhrids = getHRIds();
      int lHistbool = 1;
      String lToolBool = "1";

      validationReturn rtrn =
            runValidation( null, ACFTIDs, TASKIDs, lpretaskIDs, null, lan_reasondbid,
                  "ReasonCD: Historic_ACFT", "USERNOTE:Historic_ACFT", lhrids, false, true, true );
      Assert.assertTrue( "The return code 1. ", rtrn.getReturnCode() == 1 );
      schedTaskIDs_ACFT = rtrn.getSchedids();

      // Validation on evt_event table
      validateEvt_Event( rtrn.getSchedids(), iTaskCOMPLETEStatusCD, rtrn.getSchedids(),
            ( iTaskCD + " (" + iTaskName + ")" ), lHistbool );

      // validate evt_inv table
      validateEvt_Inv( rtrn.getSchedids(), ACFTIDs, "ACFT" );

      // validate sched_stask table
      validate_sched_stask( rtrn.getSchedids(), TASKIDs, iTaskTypeSRVC, lToolBool );

      System.out.print( "===== Ending " + testName.getMethodName() + " ==================" );

   }


   /**
    * Verify multiple labors requirement on ASSY
    *
    *
    */
   @Test
   public void test_Mutiple_Labour_JIC_ASSY() {

      System.out.print( "===== Starting " + testName.getMethodName() + " ==================" );

      simpleIDs lpretaskIDs = new simpleIDs( "-1", "-1" );
      String lstrSQL = "select * from ref_stage_reason where event_status_cd='ACTV'";
      int an_reasondbid = Integer.parseInt( getStringValue( lstrSQL, "STAGE_REASON_DB_ID" ) );
      simpleIDs lhrids = getHRIds();
      int lHistbool = 0;
      String lToolBool = "1";

      validationReturn lrtrn =
            runValidation( null, ASSYIDs, TASKIDs, lpretaskIDs, null, an_reasondbid,
                  "ReasonCD: Labor_ASSY", "USERNOTE:Labor_ASSY", lhrids, false, false, true );
      Assert.assertTrue( "The return code 1. ", lrtrn.getReturnCode() == 1 );
      schedTaskIDs_ASSY = lrtrn.getSchedids();

      // Validation on evt_event table
      validateEvt_Event( lrtrn.getSchedids(), iTaskACTVStatusCD, lrtrn.getSchedids(),
            ( iTaskCD + " (" + iTaskName + ")" ), lHistbool );

      // validate evt_inv table
      validateEvt_Inv( lrtrn.getSchedids(), ASSYIDs, "ASSY" );

      // validate sched_stask table
      validate_sched_stask( lrtrn.getSchedids(), TASKIDs, iTaskTypeJIC, lToolBool );

      // Validate sched labour tables
      List<simpleIDs> iLabourList = validate_sched_labour( lrtrn.getSchedids() );
      Assert.assertTrue( "The size of labour list is 2. ", iLabourList.size() == 2 );

      // Validate sched labour role table
      List<simpleIDs> iLabourRoleList1 = validate_sched_labour_role( iLabourList.get( 0 ) );
      Assert.assertTrue( "The size of labour role list1 is 3. ", iLabourRoleList1.size() == 3 );
      List<simpleIDs> iLabourRoleList2 = validate_sched_labour_role( iLabourList.get( 1 ) );
      Assert.assertTrue( "The size of labour role list2 is 3. ", iLabourRoleList2.size() == 3 );

      // Validate sched labour role status table
      validate_sched_labour_role_status( iLabourRoleList1 );
      validate_sched_labour_role_status( iLabourRoleList2 );

      System.out.print( "===== Ending " + testName.getMethodName() + " ==================" );

   }


   /**
    * Verify multiple tools requirement on ASSY
    *
    *
    */
   @Test
   public void test_Mutiple_Tool_ASSY() {
      System.out.print( "===== Starting " + testName.getMethodName() + " ==================" );

      simpleIDs lpretaskIDs = new simpleIDs( "-1", "-1" );
      String lstrSQL = "select * from ref_stage_reason where event_status_cd='ACTV'";
      int lan_reasondbid = Integer.parseInt( getStringValue( lstrSQL, "STAGE_REASON_DB_ID" ) );
      simpleIDs lhrids = getHRIds();
      int lHistbool = 0;
      String lToolBool = "0";

      validationReturn lrtrn =
            runValidation( null, ASSYIDs, TASKIDs, lpretaskIDs, null, lan_reasondbid,
                  "ReasonCD: Tool_ASSY", "USERNOTE:Tool_ASSY", lhrids, false, false, true );
      Assert.assertTrue( "The return code 1. ", lrtrn.getReturnCode() == 1 );
      schedTaskIDs_ASSY = lrtrn.getSchedids();

      // Validation on evt_event table
      validateEvt_Event( lrtrn.getSchedids(), iTaskACTVStatusCD, lrtrn.getSchedids(),
            ( iTaskCD + " (" + iTaskName + ")" ), lHistbool );

      // validate evt_inv table
      validateEvt_Inv( lrtrn.getSchedids(), ASSYIDs, "ASSY" );

      // validate sched_stask table
      validate_sched_stask( lrtrn.getSchedids(), TASKIDs, iTaskTypeSRVC, lToolBool );

      // validate EVT_TOOL table
      validate_evt_tool( lrtrn.getSchedids(), TASKIDs, iTool1 );
      validate_evt_tool( lrtrn.getSchedids(), TASKIDs, iTool2 );

   }


   /**
    * Verify multiple steps requirement on ASSY
    *
    *
    */
   @Ignore
   @Test
   public void test_Mutiple_Step_ASSY() {
      System.out.print( "===== Starting " + testName.getMethodName() + " ==================" );

      simpleIDs lpretaskIDs = new simpleIDs( "-1", "-1" );
      String lstrSQL = "select * from ref_stage_reason where event_status_cd='ACTV'";
      int lan_reasondbid = Integer.parseInt( getStringValue( lstrSQL, "STAGE_REASON_DB_ID" ) );
      simpleIDs lhrids = getHRIds();
      int lHistbool = 0;
      String lToolBool = "1";

      validationReturn lrtrn =
            runValidation( null, ASSYIDs, TASKIDs, lpretaskIDs, null, lan_reasondbid,
                  "ReasonCD: Tool_ASSY", "USERNOTE:Tool_ASSY", lhrids, false, false, true );
      Assert.assertTrue( "The return code 1. ", lrtrn.getReturnCode() == 1 );
      schedTaskIDs_ASSY = lrtrn.getSchedids();

      // Validation on evt_event table
      validateEvt_Event( lrtrn.getSchedids(), iTaskACTVStatusCD, lrtrn.getSchedids(),
            ( iTaskCD + " (" + iTaskName + ")" ), lHistbool );

      // validate evt_inv table
      validateEvt_Inv( lrtrn.getSchedids(), ASSYIDs, "ASSY" );

      // validate sched_stask table
      validate_sched_stask( lrtrn.getSchedids(), TASKIDs, iTaskTypeSRVC, lToolBool );

      // validate sched_step table
      stepdata adata1 = new stepdata( null, "MXPENDING" );
      stepdata adata2 = new stepdata( null, "MXPENDING" );
      validate_sched_step( lrtrn.getSchedids(), "1", adata1 );
      validate_sched_step( lrtrn.getSchedids(), "2", adata2 );
   }


   /**
    * Verify multiple zones requirement on ASSY
    *
    *
    */
   @Test
   public void test_Mutiple_Zone_ACFT() {
      System.out.print( "===== Starting " + testName.getMethodName() + " ==================" );

      simpleIDs lpretaskIDs = new simpleIDs( "-1", "-1" );
      String lstrSQL = "select * from ref_stage_reason where event_status_cd='ACTV'";
      int lan_reasondbid = Integer.parseInt( getStringValue( lstrSQL, "STAGE_REASON_DB_ID" ) );
      simpleIDs hrids = getHRIds();
      int iHistbool = 0;
      String lToolBool = "1";

      validationReturn lrtrn =
            runValidation( null, ACFTIDs, TASKIDs, lpretaskIDs, null, lan_reasondbid,
                  "ReasonCD: ZONE_ACFT", "USERNOTE:ZONE_ACFT", hrids, false, false, true );
      Assert.assertTrue( "The return code 1. ", lrtrn.getReturnCode() == 1 );
      schedTaskIDs_ACFT = lrtrn.getSchedids();

      // Validation on evt_event table
      validateEvt_Event( lrtrn.getSchedids(), iTaskACTVStatusCD, lrtrn.getSchedids(),
            ( iTaskCD + " (" + iTaskName + ")" ), iHistbool );

      // validate evt_inv table
      validateEvt_Inv( lrtrn.getSchedids(), ACFTIDs, "ACFT" );

      // validate sched_stask table
      validate_sched_stask( lrtrn.getSchedids(), TASKIDs, iTaskTypeJIC, lToolBool );

      // validate sched_zone table
      validate_sched_zone( lrtrn.getSchedids(), iZone1 );
      validate_sched_zone( lrtrn.getSchedids(), iZone2 );

      System.out.print( "===== Ending " + testName.getMethodName() + " ==================" );

   }


   /**
    * Verify multiple panels requirement on ASSY
    *
    *
    */
   @Test
   public void test_Mutiple_Panel_ACFT() {
      System.out.print( "===== Starting " + testName.getMethodName() + " ==================" );

      simpleIDs lpretaskIDs = new simpleIDs( "-1", "-1" );
      String lstrSQL = "select * from ref_stage_reason where event_status_cd='ACTV'";
      int lan_reasondbid = Integer.parseInt( getStringValue( lstrSQL, "STAGE_REASON_DB_ID" ) );
      simpleIDs lhrids = getHRIds();
      int lHistbool = 0;
      String lToolBool = "1";

      validationReturn lrtrn =
            runValidation( null, ACFTIDs, TASKIDs, lpretaskIDs, null, lan_reasondbid,
                  "ReasonCD: PANEL_ACFT", "USERNOTE:PANEL_ACFT", lhrids, false, false, true );
      Assert.assertTrue( "The return code 1. ", lrtrn.getReturnCode() == 1 );
      schedTaskIDs_ACFT = lrtrn.getSchedids();

      // Validation on evt_event table
      validateEvt_Event( lrtrn.getSchedids(), iTaskACTVStatusCD, lrtrn.getSchedids(),
            ( iTaskCD + " (" + iTaskName + ")" ), lHistbool );

      // validate evt_inv table
      validateEvt_Inv( lrtrn.getSchedids(), ACFTIDs, "ACFT" );

      // validate sched_stask table
      validate_sched_stask( lrtrn.getSchedids(), TASKIDs, iTaskTypeJIC, lToolBool );

      // validate sched_panel table
      validate_sched_panel( lrtrn.getSchedids(), iPanel1 );
      validate_sched_panel( lrtrn.getSchedids(), iPanel2 );

      System.out.print( "===== Ending " + testName.getMethodName() + " ==================" );

   }


   /**
    * Verify multiple works requirement on ASSY
    *
    *
    */
   @Test
   public void test_Mutiple_WorkType_ACFT() {
      System.out.print( "===== Starting " + testName.getMethodName() + " ==================" );

      simpleIDs lpretaskIDs = new simpleIDs( "-1", "-1" );
      String lstrSQL = "select * from ref_stage_reason where event_status_cd='ACTV'";
      int lan_reasondbid = Integer.parseInt( getStringValue( lstrSQL, "STAGE_REASON_DB_ID" ) );
      simpleIDs lhrids = getHRIds();
      int lHistbool = 0;
      String lToolBool = "1";

      validationReturn lrtrn =
            runValidation( null, ACFTIDs, TASKIDs, lpretaskIDs, null, lan_reasondbid,
                  "ReasonCD: WORKTYPE_ACFT", "USERNOTE:WORKTYPE_ACFT", lhrids, false, false, true );
      Assert.assertTrue( "The return code 1. ", lrtrn.getReturnCode() == 1 );
      schedTaskIDs_ACFT = lrtrn.getSchedids();

      // Validation on evt_event table
      validateEvt_Event( lrtrn.getSchedids(), iTaskACTVStatusCD, lrtrn.getSchedids(),
            ( iTaskCD + " (" + iTaskName + ")" ), lHistbool );

      // validate evt_inv table
      validateEvt_Inv( lrtrn.getSchedids(), ACFTIDs, "ACFT" );

      // validate sched_stask table
      validate_sched_stask( lrtrn.getSchedids(), TASKIDs, iTaskTypeJIC, lToolBool );

      // validate sched_panel table
      validate_sched_worktype( lrtrn.getSchedids(), iWorkType1 );
      validate_sched_worktype( lrtrn.getSchedids(), iWorkType2 );

      System.out.print( "===== Ending " + testName.getMethodName() + " ==================" );

   }


   /**
    * Verify multiple measurements on ACFT
    *
    *
    */
   @Test
   public void test_MeasuresOnly_ACFT() {
      System.out.print( "===== Starting " + testName.getMethodName() + " ==================" );

      simpleIDs lpretaskIDs = new simpleIDs( "-1", "-1" );
      String lstrSQL = "select * from ref_stage_reason where event_status_cd='ACTV'";
      int lan_reasondbid = Integer.parseInt( getStringValue( lstrSQL, "STAGE_REASON_DB_ID" ) );
      simpleIDs lhrids = getHRIds();
      int lHistbool = 0;
      String lToolBool = "1";

      validationReturn lrtrn = runValidation( null, ACFTIDs, TASKIDs, lpretaskIDs, null,
            lan_reasondbid, "ReasonCD: MEASURESONLY_ACFT", "USERNOTE:MEASURESONLY_ACFT", lhrids,
            false, false, true );
      Assert.assertTrue( "The return code 1. ", lrtrn.getReturnCode() == 1 );
      schedTaskIDs_ACFT = lrtrn.getSchedids();

      // Validation on evt_event table
      validateEvt_Event( lrtrn.getSchedids(), iTaskACTVStatusCD, lrtrn.getSchedids(),
            ( iTaskCD + " (" + iTaskName + ")" ), lHistbool );

      // validate evt_inv table
      validateEvt_Inv( lrtrn.getSchedids(), ACFTIDs, "ACFT" );

      // validate sched_stask table
      validate_sched_stask( lrtrn.getSchedids(), TASKIDs, iTaskTypeJIC, lToolBool );

      // validate inv_parm_data table
      validate_inv_parm_data( lrtrn.getSchedids(), MIMDATATYPEIDs1, ACFTIDs, "1" );
      validate_inv_parm_data( lrtrn.getSchedids(), MIMDATATYPEIDs2, ACFTIDs, "2" );

      System.out.print( "===== Ending " + testName.getMethodName() + " ==================" );

   }


   /**
    * Verify multiple assembly measurements on ASSY
    *
    *
    */

   @Test
   public void test_AssmblyMeasures_ASSY_ENG() {
      System.out.print( "===== Starting " + testName.getMethodName() + " ==================" );

      simpleIDs lpretaskIDs = new simpleIDs( "-1", "-1" );
      String lstrSQL = "select * from ref_stage_reason where event_status_cd='ACTV'";
      int lan_reasondbid = Integer.parseInt( getStringValue( lstrSQL, "STAGE_REASON_DB_ID" ) );
      simpleIDs lhrids = getHRIds();
      int lHistbool = 0;
      String lToolBool = "1";

      validationReturn lrtrn = runValidation( null, ASSYIDs, TASKIDs, lpretaskIDs, null,
            lan_reasondbid, "ReasonCD: AssmblyMeasures_ASSY", "USERNOTE:AssmblyMeasures_ASSY",
            lhrids, false, false, true );
      Assert.assertTrue( "The return code 1. ", lrtrn.getReturnCode() == 1 );
      schedTaskIDs_ASSY = lrtrn.getSchedids();

      // Validation on evt_event table
      validateEvt_Event( lrtrn.getSchedids(), iTaskACTVStatusCD, lrtrn.getSchedids(),
            ( iTaskCD + " (" + iTaskName + ")" ), lHistbool );

      // validate evt_inv table
      // validateEvt_Inv( lrtrn.getSchedids(), ASSYIDs, "ASSY" );

      // validate sched_stask table
      validate_sched_stask( lrtrn.getSchedids(), TASKIDs, iTaskTypeJIC, lToolBool );

      // validate inv_parm table
      validate_inv_parm_data( lrtrn.getSchedids(), MIMDATATYPEIDs1, ASSYIDs, "1" );
      validate_inv_parm_data( lrtrn.getSchedids(), MIMDATATYPEIDs2, ASSYIDs, "2" );

      System.out.print( "===== Ending " + testName.getMethodName() + " ==================" );

   }


   /**
    * Verify multiple ietms with blob on TRK
    *
    *
    */
   @Test
   public void test_Ietm_Blob_TRK() {
      System.out.print( "===== Starting " + testName.getMethodName() + " ==================" );

      simpleIDs lpretaskIDs = new simpleIDs( "-1", "-1" );
      String lstrSQL = "select * from ref_stage_reason where event_status_cd='ACTV'";
      int lan_reasondbid = Integer.parseInt( getStringValue( lstrSQL, "STAGE_REASON_DB_ID" ) );
      simpleIDs lhrids = getHRIds();
      int lHistbool = 0;
      String lToolBool = "1";

      validationReturn rtrn =
            runValidation( null, TRKIDs, TASKIDs, lpretaskIDs, null, lan_reasondbid,
                  "ReasonCD: Ietm_Blob_TRK", "USERNOTE:Ietm_Blob_TRK", lhrids, false, false, true );
      Assert.assertTrue( "The return code 1. ", rtrn.getReturnCode() == 1 );
      schedTaskIDs_TRK = rtrn.getSchedids();

      // Validation on evt_event table
      validateEvt_Event( rtrn.getSchedids(), iTaskACTVStatusCD, rtrn.getSchedids(),
            ( iTaskCD + " (" + iTaskName + ")" ), lHistbool );

      // validate evt_inv table
      validateEvt_Inv( rtrn.getSchedids(), TRKIDs, "TRK" );

      // validate sched_stask table
      validate_sched_stask( rtrn.getSchedids(), TASKIDs, iTaskTypeSRVC, lToolBool );

      // validate evt_attach table
      validate_evt_attach_data( rtrn.getSchedids(), ietm1IDs, 1 );
      validate_evt_attach_data( rtrn.getSchedids(), ietm2IDs, 1 );

      // Validate evt_ietm table
      validate_evt_ietm_data( rtrn.getSchedids(), ietm1IDs, 0 );
      validate_evt_ietm_data( rtrn.getSchedids(), ietm2IDs, 0 );

      System.out.print( "===== Ending " + testName.getMethodName() + " ==================" );

   }


   /**
    * Verify multiple ietms without blob on TRK
    *
    *
    */
   @Test
   public void test_Ietm_NABlob_TRK() {
      System.out.print( "===== Starting " + testName.getMethodName() + " ==================" );

      simpleIDs lpretaskIDs = new simpleIDs( "-1", "-1" );
      String lstrSQL = "select * from ref_stage_reason where event_status_cd='ACTV'";
      int lan_reasondbid = Integer.parseInt( getStringValue( lstrSQL, "STAGE_REASON_DB_ID" ) );
      simpleIDs lhrids = getHRIds();
      int lHistbool = 0;
      String lToolBool = "1";

      validationReturn lrtrn =
            runValidation( null, TRKIDs, TASKIDs, lpretaskIDs, null, lan_reasondbid,
                  "ReasonCD: Ietm_Blob_TRK", "USERNOTE:Ietm_Blob_TRK", lhrids, false, false, true );
      Assert.assertTrue( "The return code 1. ", lrtrn.getReturnCode() == 1 );
      schedTaskIDs_TRK = lrtrn.getSchedids();

      // Validation on evt_event table
      validateEvt_Event( lrtrn.getSchedids(), iTaskACTVStatusCD, lrtrn.getSchedids(),
            ( iTaskCD + " (" + iTaskName + ")" ), lHistbool );

      // validate evt_inv table
      validateEvt_Inv( lrtrn.getSchedids(), TRKIDs, "TRK" );

      // validate sched_stask table
      validate_sched_stask( lrtrn.getSchedids(), TASKIDs, iTaskTypeSRVC, lToolBool );

      // validate evt_attach table
      validate_evt_attach_data( lrtrn.getSchedids(), ietm1IDs, 0 );
      validate_evt_attach_data( lrtrn.getSchedids(), ietm2IDs, 0 );

      // Validate evt_ietm table
      validate_evt_ietm_data( lrtrn.getSchedids(), ietm1IDs, 1 );
      validate_evt_ietm_data( lrtrn.getSchedids(), ietm2IDs, 1 );

      System.out.print( "===== Ending " + testName.getMethodName() + " ==================" );

   }


   /**
    * This function is to create inventory of ACFT
    */

   public simpleIDs DataSetup_ACFT() {
      return createInventory( "ACFT", null, null, null, null, iACFTSDSC, iACFTSN, null );

   }


   /**
    * This function is to create inventory of ASSY
    */

   public simpleIDs DataSetup_ASSY() {
      return createInventory( "ASSY", null, null, null, null, iASSYSDSC, iASSYSN, null );

   }


   /**
    * This function is to create inventory of ASSY-ENG
    */

   public simpleIDs DataSetup_ENG() {
      return createInventory( "ASSY-ENG", null, null, null, null, iASSYSDSC, iASSYSN, null );

   }


   /**
    * This function is to create inventory of ASSY-ENG
    */

   public simpleIDs DataSetup_APU() {
      return createInventory( "ASSY-APU", null, null, null, null, iASSYSDSC, iASSYSN, null );

   }


   /**
    * This function is to create inventory of TRK
    */
   public simpleIDs DataSetup_TRK() {
      return createInventory( "TRK", null, null, null, null, iTRKSDSC, iTRKSN, iAPPLEFFCDINV );

   }


   /**
    * This function is to create task definition record
    */

   public simpleIDs DataSetup_TASKDEFN() {
      return createTaskDEFN();

   }


   /**
    * This function is to create task record.
    */

   public simpleIDs DataSetup_TASK( simpleIDs IDs, String aTaskType, String aResourceSumBool ) {

      String lstrSQL =
            "Select ASSMBL_DB_ID,ASSMBL_CD,ASSMBL_BOM_ID from INV_INV where INV_NO_DB_ID="
                  + IDs.getNO_DB_ID() + " and INV_NO_ID=" + IDs.getNO_ID();
      assmbleInfor assmbly = getAssmbleInfor( lstrSQL );

      return createTask( TASKDEFNIDs.getNO_DB_ID(), TASKDEFNIDs.getNO_ID(), aTaskType, assmbly,
            iTaskACTVStatusCD, iTaskCD, iTaskName, "0", aResourceSumBool );

   }


   /**
    * This function is to create labor req records.
    */

   public void DataSetup_LABOUR( simpleIDs aIDs ) {
      createLabourREQ( aIDs, "LBR" );
      createLabourREQ( aIDs, "HYDRAUL" );

   }


   /**
    * This function is to create tool req records.
    */
   public void DataSetup_TOOL( simpleIDs aIDs ) {
      iTool1 = createToolREQ( aIDs, true );
      iTool2 = createToolREQ( aIDs, false );

   }


   /**
    * This function is to create step req records.
    */

   public void DataSetup_STEP( simpleIDs aIDs ) {
      createStepREQ( aIDs, "1", "Step 1" );
      createStepREQ( aIDs, "2", "Step 2" );

   }


   /**
    * This function is to create zone req records.
    */

   public void DataSetup_ZONE( simpleIDs aIDs ) {
      createZoneREQ( aIDs, iZone1 );
      createZoneREQ( aIDs, iZone2 );

   }


   /**
    * This function is to create panel req records.
    */
   public void DataSetup_PANEL( simpleIDs aIDs ) {
      createPanelREQ( aIDs, iPanel1 );
      createPanelREQ( aIDs, iPanel2 );

   }


   /**
    * This function is to create worktype req records.
    */

   public void DataSetup_WORKTYPE( simpleIDs aIDs ) {
      createWorkTypeREQ( aIDs, iWorkType1 );
      createWorkTypeREQ( aIDs, iWorkType2 );

   }


   /**
    * This function is to create measurement req record.
    */

   public void DataSetup_MeasuresOnly( simpleIDs aIDs ) {
      MIMDATATYPEIDs1 = createMimDataType( iParmDESC1 );
      MIMDATATYPEIDs2 = createMimDataType( iParmDESC2 );
      createParmData( aIDs, MIMDATATYPEIDs1, "1" );
      createParmData( aIDs, MIMDATATYPEIDs2, "2" );
   }


   /**
    * This function is to create assembly measurement req records.
    */
   public void DataSetup_AssmblyMeasures( simpleIDs aIDs ) {
      MIMDATATYPEIDs1 = createMimDataType( iParmDESC1 );
      MIMDATATYPEIDs2 = createMimDataType( iParmDESC2 );

      createParmData( aIDs, MIMDATATYPEIDs1, "1" );
      createParmData( aIDs, MIMDATATYPEIDs2, "2" );

      createRefDataAssmblClass( MIMDATATYPEIDs1, "ENG" );
      createRefDataAssmblClass( MIMDATATYPEIDs2, "ENG" );
   }


   /**
    * This function is to create ietmopic and task_task_ietm record.
    */
   public simpleIDs DataSetup_Ietm( simpleIDs aIDs, boolean ablnBlob, String aIetmDESC ) {
      simpleIDs ietmIDs = null;

      if ( ablnBlob ) {

         ietmIDs = createIetmTopic( aIetmDESC, "C9CBBBCCCEB9C8CABCCCCEB9C9CBBB", iAttachType,
               iAPPLEFFCDTOPIC );
      } else {
         ietmIDs = createIetmTopic( aIetmDESC, null, iAttachType, iAPPLEFFCDTOPIC );

      }
      createTaskTaskIetm( aIDs, ietmIDs );

      return ietmIDs;

   }


   /**
    * This function is to run store procedure sched_stask_pkg.genoneschedtask.
    */

   public validationReturn runValidation( simpleIDs eventIDs, simpleIDs invIDs, simpleIDs taskIDs,
         simpleIDs pretaskIDs, Date sqlDate, int an_reasondbid, String an_reasoncd,
         String as_usernote, simpleIDs hrIDs, boolean ab_calledexternally, boolean ab_historic,
         boolean ab_createnatask ) {

      CallableStatement lPrepareCallGenSchedTask;
      validationReturn lReturn = null;
      try {

         // Build CallableStatement String
         StringBuilder strCall = new StringBuilder();
         strCall.append( "BEGIN sched_stask_pkg.genoneschedtask(an_evteventdbid => ?, " )
               .append( "an_evteventid => ?, " + "an_invnodbid => ?, " )
               .append( "an_invnoid => ?, " + "an_taskdbid => ?, " + "an_taskid => ?, " )
               .append( "an_previoustaskdbid => ?, " + "an_previoustaskid => ?, " )
               .append( "ad_completiondate => ?, " + "an_reasondbid => ?, " )
               .append( "an_reasoncd => ?, " + "as_usernote => ?, " + "an_hrdbid => ?, " )
               .append( "an_hrid => ?, " + "ab_calledexternally => false, " );

         if ( ab_historic == true ) {
            strCall.append( "ab_historic => true, " );

         } else {
            strCall.append( "ab_historic => false, " );

         }

         if ( ab_createnatask == true ) {
            strCall.append( "ab_createnatask => true, " );

         } else {
            strCall.append( "ab_createnatask => false, " );

         }

         strCall.append( "on_scheddbid => ?,  on_schedid => ?, " )
               .append( "on_return => ?); End;" );

         // prepare CallableStatement
         lPrepareCallGenSchedTask = getConnection().prepareCall( strCall.toString() );

         // Provide parameters
         if ( eventIDs != null ) {
            lPrepareCallGenSchedTask.setInt( 1, Integer.parseInt( eventIDs.getNO_DB_ID() ) );
            lPrepareCallGenSchedTask.setInt( 2, Integer.parseInt( eventIDs.getNO_ID() ) );
         } else {
            lPrepareCallGenSchedTask.setNull( 1, Types.NULL );
            lPrepareCallGenSchedTask.setNull( 2, Types.NULL );
         }

         if ( invIDs != null ) {
            lPrepareCallGenSchedTask.setInt( 3, Integer.parseInt( invIDs.getNO_DB_ID() ) );
            lPrepareCallGenSchedTask.setInt( 4, Integer.parseInt( invIDs.getNO_ID() ) );
         } else {
            lPrepareCallGenSchedTask.setNull( 3, Types.INTEGER );
            lPrepareCallGenSchedTask.setNull( 4, Types.INTEGER );
         }

         if ( taskIDs != null ) {
            lPrepareCallGenSchedTask.setInt( 5, Integer.parseInt( taskIDs.getNO_DB_ID() ) );
            lPrepareCallGenSchedTask.setInt( 6, Integer.parseInt( taskIDs.getNO_ID() ) );
         } else {
            lPrepareCallGenSchedTask.setNull( 5, Types.INTEGER );
            lPrepareCallGenSchedTask.setNull( 6, Types.INTEGER );

         }

         if ( pretaskIDs != null ) {

            lPrepareCallGenSchedTask.setInt( 7, Integer.parseInt( pretaskIDs.getNO_DB_ID() ) );
            lPrepareCallGenSchedTask.setInt( 8, Integer.parseInt( pretaskIDs.getNO_ID() ) );
         } else {
            lPrepareCallGenSchedTask.setNull( 7, Types.INTEGER );
            lPrepareCallGenSchedTask.setNull( 8, Types.INTEGER );

         }

         if ( sqlDate != null ) {
            lPrepareCallGenSchedTask.setDate( 9, sqlDate );
         } else {
            lPrepareCallGenSchedTask.setNull( 9, Types.NULL );

         }

         lPrepareCallGenSchedTask.setInt( 10, an_reasondbid );
         lPrepareCallGenSchedTask.setString( 11, an_reasoncd );
         lPrepareCallGenSchedTask.setString( 12, as_usernote );

         if ( hrIDs != null ) {

            lPrepareCallGenSchedTask.setInt( 13, Integer.parseInt( hrIDs.getNO_DB_ID() ) );
            lPrepareCallGenSchedTask.setInt( 14, Integer.parseInt( hrIDs.getNO_ID() ) );
         } else {
            lPrepareCallGenSchedTask.setNull( 13, Types.INTEGER );
            lPrepareCallGenSchedTask.setNull( 14, Types.INTEGER );

         }

         lPrepareCallGenSchedTask.registerOutParameter( 15, Types.INTEGER );
         lPrepareCallGenSchedTask.registerOutParameter( 16, Types.INTEGER );
         lPrepareCallGenSchedTask.registerOutParameter( 17, Types.INTEGER );

         // Execute CallableStatement
         lPrepareCallGenSchedTask.execute();
         commit();

         lReturn = new validationReturn( lPrepareCallGenSchedTask.getInt( 15 ),
               lPrepareCallGenSchedTask.getInt( 16 ), lPrepareCallGenSchedTask.getInt( 17 ) );

      } catch ( SQLException e ) {

         e.printStackTrace();
      }

      return lReturn;

   }


   // Return class for CallableStatement execution
   class validationReturn {

      simpleIDs schedids;
      int returnCode;


      public validationReturn(int scheddbid, int schedid, int returnCode) {
         this.schedids =
               new simpleIDs( Integer.toString( scheddbid ), Integer.toString( schedid ) );
         this.returnCode = returnCode;

      }


      /**
       * Returns the value of the schedids property.
       *
       * @return the value of the schedids property
       */
      public simpleIDs getSchedids() {
         return schedids;
      }


      /**
       * Sets a new value for the schedids property.
       *
       * @param aSchedids
       *           the new value for the schedids property
       */
      public void setSchedids( simpleIDs aSchedids ) {
         schedids = aSchedids;
      }


      /**
       * Returns the value of the returnCode property.
       *
       * @return the value of the returnCode property
       */
      public int getReturnCode() {
         return returnCode;
      }


      /**
       * Sets a new value for the returnCode property.
       *
       * @param aReturnCode
       *           the new value for the returnCode property
       */
      public void setReturnCode( int aReturnCode ) {
         returnCode = aReturnCode;
      }

   }

}
