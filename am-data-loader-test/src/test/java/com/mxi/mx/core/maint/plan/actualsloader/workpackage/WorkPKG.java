package com.mxi.mx.core.maint.plan.actualsloader.workpackage;

import static com.mxi.mx.util.TableUtil.AXON_DOMAIN_EVENT_ENTRY_TABLE;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.core.maint.plan.datamodels.inventoryData;
import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.BatchFileManager;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.TestConstants;
import com.mxi.mx.util.WorkPKGTest;


/**
 * This test suite contains test cases on validation and import functionality of AL_WORK_PACKAGE_PKG
 * package.
 *
 * @author ALICIA QIAN
 */
public class WorkPKG extends WorkPKGTest {

   @Rule
   public TestName testName = new TestName();


   /**
    * Setup before executing each individual test
    *
    */
   @Before
   @Override
   public void before() throws Exception {

      super.before();
      clearActualsLoaderTables();
      DataSetup();
      String strTCName = testName.getMethodName();
      if ( strTCName.contains( "NestedTaskCd" ) ) {
         dataSetupNestedTaskCd();
      }

      iEVENT_1 = null;
      iEVENT_BLOCK = null;
      iEVENT_REQ = null;
      iEVENT_JIC = null;
      iEVENT_REQ_2 = null;
      iEVENT_JIC_2 = null;

      deleteAllFromTable( AXON_DOMAIN_EVENT_ENTRY_TABLE );
   }


   /**
    * Clean up after each individual test
    */
   @After
   @Override
   public void after() {

      try {
         RestoreData();
         String lQuery = null;
         String strTCName = testName.getMethodName();
         if ( strTCName.contains( "NestedTaskCd" ) ) {
            lQuery = "Update evt_event set h_event_db_id = " + iEvtLowTaskCd.getNO_DB_ID()
                  + ", h_event_id = " + iEvtLowTaskCd.getNO_ID() + " where event_db_id = "
                  + iEvtLowTaskCd.getNO_DB_ID() + " AND event_id = " + iEvtLowTaskCd.getNO_ID();
            runUpdate( lQuery );
         }

         deleteAllFromTable( AXON_DOMAIN_EVENT_ENTRY_TABLE );

         super.after();
      } catch ( Exception e ) {
         e.printStackTrace();
      }
   }


   /**
    * This test is to verify AL_WORK_PACKAGE_PKG validation functionality of staging table
    * AL_WORK_PACKAGE, AL_WORK_PACKAGE_TASK and AL_WORK_PACKAGE_TASK_ATA. Task REG2 on TRK, TASk
    * BLOCK1 on SER of SYS_1-1
    *
    *
    */
   @Test
   public void testACFTTASK_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      iEVENT_1 = null;

      // AL_WORK_PACKAGE table
      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );

      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_1 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // AL_WORK_PACKAGE_TASK_ATA
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_ata_5 + "\'" );
      lWPK.put( "ATA_SYS_CD", "\'" + iata_sys_cd_1 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation failed when expected to pass",
            runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify AL_WORK_PACKAGE_PKG import functionality of staging table
    * AL_WORK_PACKAGE, AL_WORK_PACKAGE_TASK and AL_WORK_PACKAGE_TASK_ATA. Task REG2 on TRK, TASk
    * BLOCK1 on SER of SYS_1-1
    *
    *
    */
   @Test
   public void testACFTTASK_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testACFTTASK_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( "Import failed when expected to pass",
            runValidationAndImport( false, true ) == 1 );

      iEVENT_1 = null;
      // Verify EVT_EVENT table
      iAccount_id = getAccountIds( iISSUE_ACCOUNT_CD );
      iEVENT_1 = verifyEVTEVENT( "TS", iWKP_NAME_1, "ACTV", iWKP_DESC_1, iAccount_id );

      // Verify EVT_INV table
      inventoryData linvData = verifyEVTINV( iEVENT_1 );

      // Verify EVT_STAGE table
      VerifyEVTSTAGE( iEVENT_1, iNoteString1, iNoteString2 );

      // Verify SCHED_STASK table
      VerifySCHEDSTASK( iEVENT_1, iTaskClassCD, iSUB_TYPE_CD_1, linvData );

      // Verify evt_loc
      simpleIDs lLocIds = getLocIds( iSCHED_LOC_CD_1 );
      VerifyEVTLOC( iEVENT_1, lLocIds );

      // Verify sched_work_type
      VerifySCHEDWORKTYPE( iEVENT_1, iWORK_TYPE_LIST_1, iWORK_TYPE_LIST_2 );

      // Verify sched_wp
      VerifySCHEDWP( iEVENT_1 );

      // Verify sched_wp_sign_req
      VerifySCHEDWPSIGNREQ( iEVENT_1, iSkill1, iSkill2 );

      // Verify axon event publish
      verifyTaskCreatedEventPublish();

      // Verify axon event publish
      verifyTaskAssignedToWorkPackageEventPublish();

   }


   /**
    * This test is to verify AL_WORK_PACKAGE_PKG validation functionality of staging table
    * AL_WORK_PACKAGE, AL_WORK_PACKAGE_TASK and AL_WORK_PACKAGE_TASK_ATA. FAULT TestTRKFAULT on TRK,
    * FAULT TestSERFAULT on SER of SYS_10-1
    *
    *
    */
   @Test
   public void testACFTFAULT_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      iEVENT_1 = null;

      // AL_WORK_PACKAGE table
      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1_5 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1_5 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1_5 + "\'" );

      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "FAULT_SDESC", "\'" + iFAULT_SDESC_8 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // AL_WORK_PACKAGE_TASK_ATA
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1_5 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "FAULT_SDESC", "\'" + iFAULT_SDESC_2 + "\'" );
      lWPK.put( "ATA_SYS_CD", "\'" + iATA_SYS_CD_7 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation failed when expected to pass ",
            runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify AL_WORK_PACKAGE_PKG import functionality of staging table
    * AL_WORK_PACKAGE, AL_WORK_PACKAGE_TASK and AL_WORK_PACKAGE_TASK_ATA. FAULT TestTRKFAULT on TRK,
    * FAULT TestSERFAULT on SER of SYS_10-1
    *
    *
    */

   @Test
   public void testACFTFAULT_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testACFTFAULT_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( "Import failed when expected to pass",
            runValidationAndImport( false, true ) == 1 );

      iEVENT_1 = null;
      // Verify EVT_EVENT table
      iAccount_id = getAccountIds( iISSUE_ACCOUNT_CD );
      iEVENT_1 = verifyEVTEVENT( "TS", iWKP_NAME_1_5, "ACTV", iWKP_DESC_1, iAccount_id );

      // Verify EVT_INV table
      inventoryData linvData = verifyEVTINV( iEVENT_1 );

      // Verify EVT_STAGE table
      VerifyEVTSTAGE( iEVENT_1, iNoteString1, iNoteString2 );

      // Verify SCHED_STASK table
      VerifySCHEDSTASK( iEVENT_1, iTaskClassCD, iSUB_TYPE_CD_1, linvData );

      // Verify evt_loc
      simpleIDs lLocIds = getLocIds( iSCHED_LOC_CD_1 );
      VerifyEVTLOC( iEVENT_1, lLocIds );

      // Verify sched_work_type
      VerifySCHEDWORKTYPE( iEVENT_1, iWORK_TYPE_LIST_1, iWORK_TYPE_LIST_2 );

      // Verify sched_wp
      VerifySCHEDWP( iEVENT_1 );

      // Verify sched_wp_sign_req
      VerifySCHEDWPSIGNREQ( iEVENT_1, iSkill1, iSkill2 );

      // Verify evt_event_rel
      VerifyEvtEventRel( iEVENT_1, "DRVTASK" );

      // Verify axon event publish
      verifyTaskCreatedEventPublish();

      // Verify axon event publish
      // verifyTaskAssignedToWorkPackageEventPublish();

   }


   /**
    * This test is to verify AL_WORK_PACKAGE_PKG validation functionality of staging table
    * AL_WORK_PACKAGE, AL_WORK_PACKAGE_TASK and AL_WORK_PACKAGE_TASK_ATA. Task REG3 on APU_ASSY_PN1,
    * TASk BLOCK2 on ENG_ASSY_PN1
    *
    *
    */
   @Test
   public void testACFTASSYTASK_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      iEVENT_1 = null;

      // AL_WORK_PACKAGE table
      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );

      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_3 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_3 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_3 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_3 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // AL_WORK_PACKAGE_TASK_ATA
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_2 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_2 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_2 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_ata_2 + "\'" );
      lWPK.put( "ATA_SYS_CD", "\'" + iata_sys_cd_5 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation failed when expected to pass",
            runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify AL_WORK_PACKAGE_PKG import functionality of staging table
    * AL_WORK_PACKAGE, AL_WORK_PACKAGE_TASK and AL_WORK_PACKAGE_TASK_ATA. Task REG3 on APU_ASSY_PN1,
    * TASk BLOCK2 on ENG_ASSY_PN1
    *
    *
    */

   @Test
   public void testACFTASSYTASK_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testACFTASSYTASK_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( "Import failed when expected to pass",
            runValidationAndImport( false, true ) == 1 );

      iEVENT_1 = null;
      // Verify EVT_EVENT table
      iAccount_id = getAccountIds( iISSUE_ACCOUNT_CD );
      iEVENT_1 = verifyEVTEVENT( "TS", iWKP_NAME_1, "ACTV", iWKP_DESC_1, iAccount_id );

      // Verify EVT_INV table
      inventoryData linvData = verifyEVTINV( iEVENT_1 );

      // Verify EVT_STAGE table
      VerifyEVTSTAGE( iEVENT_1, iNoteString1, iNoteString2 );

      // Verify SCHED_STASK table
      VerifySCHEDSTASK( iEVENT_1, iTaskClassCD, iSUB_TYPE_CD_1, linvData );

      // Verify evt_loc
      simpleIDs lLocIds = getLocIds( iSCHED_LOC_CD_1 );
      VerifyEVTLOC( iEVENT_1, lLocIds );

      // Verify sched_work_type
      VerifySCHEDWORKTYPE( iEVENT_1, iWORK_TYPE_LIST_1, iWORK_TYPE_LIST_2 );

      // Verify sched_wp
      VerifySCHEDWP( iEVENT_1 );

      // Verify sched_wp_sign_req
      VerifySCHEDWPSIGNREQ( iEVENT_1, iSkill1, iSkill2 );

      // Verify axon event publish
      verifyTaskCreatedEventPublish();

      // Verify axon event publish
      verifyTaskAssignedToWorkPackageEventPublish();

   }


   /**
    * This test is to verify AL_WORK_PACKAGE_PKG validation functionality of staging table
    * AL_WORK_PACKAGE, AL_WORK_PACKAGE_TASK and AL_WORK_PACKAGE_TASK_ATA. Task REG4 on ENG_ASSY_PN1,
    * TASk BLOCK3 on ENG-SYS-2-1-1-TRK
    *
    *
    */
   @Test
   public void testASSYTASK_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      iEVENT_1 = null;

      // AL_WORK_PACKAGE table
      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_2 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_2 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_2 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_2 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_2 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_2 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_2 + "\'" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_2 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_2 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_2 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_2 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_2 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_4 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_4 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_4 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_4 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // AL_WORK_PACKAGE_TASK_ATA
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_2 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_2 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_2 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_2 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_2 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_2 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_2 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_ata_2 + "\'" );
      lWPK.put( "ATA_SYS_CD", "\'" + iata_sys_cd_5 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation failed when expected to pass",
            runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify AL_WORK_PACKAGE_PKG import functionality of staging table
    * AL_WORK_PACKAGE, AL_WORK_PACKAGE_TASK and AL_WORK_PACKAGE_TASK_ATA. Task REG4 on ENG_ASSY_PN1,
    * TASk BLOCK3 on ENG-SYS-2-1-1-TRK
    *
    *
    */

   @Test
   public void testASSYTASK_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testASSYTASK_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( "Import failed when expected to pass",
            runValidationAndImport( false, true ) == 1 );

      iEVENT_1 = null;
      // Verify EVT_EVENT table
      iAccount_id = getAccountIds( iISSUE_ACCOUNT_CD );
      iEVENT_1 = verifyEVTEVENT( "TS", iWKP_NAME_2, "ACTV", iWKP_DESC_2, iAccount_id );

      // Verify EVT_INV table
      inventoryData linvData = verifyEVTINV( iEVENT_1 );

      // Verify EVT_STAGE table
      VerifyEVTSTAGE( iEVENT_1, iNoteString1, iNoteString3 );

      // Verify SCHED_STASK table
      VerifySCHEDSTASK( iEVENT_1, iTaskClassCD_2, iSUB_TYPE_CD_2, linvData );

      // Verify evt_loc
      simpleIDs lLocIds = getLocIds( iSCHED_LOC_CD_2 );
      VerifyEVTLOC( iEVENT_1, lLocIds );

      // Verify sched_work_type
      VerifySCHEDWORKTYPE( iEVENT_1, iWORK_TYPE_LIST_1, iWORK_TYPE_LIST_2 );

      // Verify sched_wp
      VerifySCHEDWP( iEVENT_1 );

      // Verify sched_wp_sign_req
      VerifySCHEDWPSIGNREQ( iEVENT_1, iSkill1, iSkill2 );

      // Verify axon event publish
      verifyTaskCreatedEventPublish();

      // Verify axon event publish
      verifyTaskAssignedToWorkPackageEventPublish();

   }


   /**
    * This test is to verify AL_WORK_PACKAGE_PKG validation functionality of staging table
    * AL_WORK_PACKAGE, AL_WORK_PACKAGE_TASK and AL_WORK_PACKAGE_TASK_ATA. Task REG2 on TRK, TASk
    * BLOCK1 on SER of SYS_1-1
    *
    *
    */
   @Test
   public void testCOMPTASK_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      iEVENT_1 = null;

      // AL_WORK_PACKAGE table
      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_9 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_9 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_9 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_9 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_9 + "\'" );
      lWPK.put( "SUB_TYPE_CD", null );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_9 + "\'" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );
      lWPK.put( "SCHED_START_DT", "SYSDATE" );
      lWPK.put( "SCHED_END_DT", "SYSDATE+10" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_9 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_9 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_9 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_9 + "\'" );

      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_9 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_9 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_9 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_9 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation failed when expected to pass",
            runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify AL_WORK_PACKAGE_PKG import functionality of staging table
    * AL_WORK_PACKAGE, AL_WORK_PACKAGE_TASK and AL_WORK_PACKAGE_TASK_ATA. Task REG2 on TRK, TASk
    * BLOCK1 on SER of SYS_1-1
    *
    *
    */
   @Test
   public void testCOMPTASK_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testCOMPTASK_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( "Import failed when expected to pass",
            runValidationAndImport( false, true ) == 1 );

      iEVENT_1 = null;
      // Verify EVT_EVENT table
      iAccount_id = getAccountIds( iISSUE_ACCOUNT_CD );
      iEVENT_1 = verifyEVTEVENT( "TS", iWKP_NAME_9, "ACTV", iWKP_DESC_9, iAccount_id );

      // Verify EVT_INV table
      inventoryData linvData = verifyEVTINV_COMPREQ( iEVENT_1 );

      // Verify EVT_STAGE table
      VerifyEVTSTAGE( iEVENT_1, iNoteString1, iNoteString3 );

      // Verify SCHED_STASK table
      VerifySCHEDSTASK( iEVENT_1, "RO", null, linvData );

      // Verify evt_loc
      simpleIDs lLocIds = getLocIds( iSCHED_LOC_CD_9 );
      VerifyEVTLOC( iEVENT_1, lLocIds );

      // Verify sched_work_type
      VerifySCHEDWORKTYPE( iEVENT_1, iWORK_TYPE_LIST_1, iWORK_TYPE_LIST_2 );

      // Verify sched_wp
      VerifySCHEDWP( iEVENT_1 );

      // Verify sched_wp_sign_req
      VerifySCHEDWPSIGNREQ( iEVENT_1, iSkill1, iSkill2 );

      // Verify axon event publish
      verifyTaskCreatedEventPublish();

      // Verify axon event publish
      verifyTaskAssignedToWorkPackageEventPublish();

   }


   /**
    * This test is to verify OPER-16005:should allow TRACK locations instead of HANGAR locations.
    *
    *
    */
   @Test
   public void testOPER_16005_LINE_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      iEVENT_1 = null;

      // AL_WORK_PACKAGE table
      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );

      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_1 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // AL_WORK_PACKAGE_TASK_ATA
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_ata_5 + "\'" );
      lWPK.put( "ATA_SYS_CD", "\'" + iata_sys_cd_1 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation failed when expected to pass",
            runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify OPER-16005:should allow TRACK locations instead of HANGAR locations.
    *
    *
    */
   @Test
   public void testOPER_16005_TRACK_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      iEVENT_1 = null;

      // AL_WORK_PACKAGE table
      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_5 + "\'" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );

      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_1 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // AL_WORK_PACKAGE_TASK_ATA
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );

      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_ata_5 + "\'" );
      lWPK.put( "ATA_SYS_CD", "\'" + iata_sys_cd_1 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation failed when expected to pass",
            runValidationAndImport( true, true ) == 1 );

   }


   @Test
   public void testOPER_16005_TRACK_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testOPER_16005_TRACK_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( "Import failed when expected to pass",
            runValidationAndImport( false, true ) == 1 );

      iEVENT_1 = null;
      // Verify EVT_EVENT table
      iAccount_id = getAccountIds( iISSUE_ACCOUNT_CD );
      iEVENT_1 = verifyEVTEVENT( "TS", iWKP_NAME_1, "ACTV", iWKP_DESC_1, iAccount_id );

      // Verify EVT_INV table
      inventoryData linvData = verifyEVTINV( iEVENT_1 );

      // Verify EVT_STAGE table
      VerifyEVTSTAGE( iEVENT_1, iNoteString1, iNoteString4 );

      // Verify SCHED_STASK table
      VerifySCHEDSTASK( iEVENT_1, iTaskClassCD, iSUB_TYPE_CD_1, linvData );

      // Verify evt_loc
      simpleIDs lLocIds = getLocIds( iSCHED_LOC_CD_5 );
      VerifyEVTLOC( iEVENT_1, lLocIds );

      // Verify sched_work_type
      VerifySCHEDWORKTYPE( iEVENT_1, iWORK_TYPE_LIST_1, iWORK_TYPE_LIST_2 );

      // Verify sched_wp
      VerifySCHEDWP( iEVENT_1 );

      // Verify axon event publish
      verifyTaskCreatedEventPublish();

      // Verify axon event publish
      verifyTaskAssignedToWorkPackageEventPublish();

   }


   /**
    * This test is to verify OPER-16405:should allow Hanger locations instead of HANGAR locations.
    *
    *
    */
   @Test
   public void testOPER_16005_HANGERS() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      iEVENT_1 = null;

      // AL_WORK_PACKAGE table
      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_5_2 + "\'" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // run validation
      Assert.assertTrue( "Validation failed when expected to pass",
            runValidationAndImport( true, true ) == 1 );

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( "Import failed when expected to pass",
            runValidationAndImport( false, true ) == 1 );

      iEVENT_1 = null;
      // Verify EVT_EVENT table
      iAccount_id = getAccountIds( iISSUE_ACCOUNT_CD );
      iEVENT_1 = verifyEVTEVENT( "TS", iWKP_NAME_1, "ACTV", iWKP_DESC_1, iAccount_id );

      // Verify evt_loc
      simpleIDs lLocIds = getLocIds( iSCHED_LOC_CD_5_2 );
      VerifyEVTLOC( iEVENT_1, lLocIds );

   }


   /**
    * This test is to verify AL_WORK_PACKAGE_PKG validation functionality of staging table
    * AL_WORK_PACKAGE, AL_WORK_PACKAGE_TASK and AL_WORK_PACKAGE_TASK_ATA. Multiple records will be
    * tested.
    *
    *
    */
   @Test
   public void testMultipleRecords_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      iEVENT_1 = null;
      iEVENT_2 = null;

      // AL_WORK_PACKAGE table
      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + "n" + "\'" ); // Testing lowercase n passes
                                                                 // validation
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      // AL_WORK_PACKAGE_TASK
      // First record--ACFTTASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );

      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_1 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // Second record--ACFTASSYTASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );

      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_3 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_3 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_3 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_3 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // // AL_WORK_PACKAGE_TASK_ATA
      // // first record--ACFTTASK
      // lWPK.clear();
      // lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      // lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      // lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      // lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      // lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_1 + "\'" );
      // lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_1 + "\'" );
      // lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      // lWPK.put( "TASK_CD", "\'" + iTASK_CD_ata_1 + "\'" );
      // lWPK.put( "ATA_SYS_CD", "\'" + iata_sys_cd_1 + "\'" );
      // // insert map
      // runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // // Second record--ACFTASSYTASK
      // lWPK.clear();
      // lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      // lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      // lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      // lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      // lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_1 + "\'" );
      // lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_1 + "\'" );
      // lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      // lWPK.put( "TASK_CD", "\'" + iTASK_CD_ata_2 + "\'" );
      // lWPK.put( "ATA_SYS_CD", "\'" + iata_sys_cd_3 + "\'" );
      // // insert map
      // runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // =========Copy from ASSYTASK_validation===============
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_2 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_2 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_2 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_2 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_2 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_2 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + "f" + "\'" ); // Testing 'F' for False
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + "0" + "\'" ); // Testing '0' for false
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_2 + "\'" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_2 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_2 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_2 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_2 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_2 + "\'" );

      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_4 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_4 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_4 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_4 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // // AL_WORK_PACKAGE_TASK_ATA
      // lWPK.clear();
      // lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_2 + "\'" );
      // lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_2 + "\'" );
      // lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_2 + "\'" );
      // lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_2 + "\'" );
      // lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_2 + "\'" );
      // lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_2 + "\'" );
      // lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_2 + "\'" );
      // lWPK.put( "TASK_CD", "\'" + iTASK_CD_ata_4 + "\'" );
      // lWPK.put( "ATA_SYS_CD", "\'" + iata_sys_cd_5 + "\'" );
      // // insert map
      // runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation failed when expected to pass",
            runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify AL_WORK_PACKAGE_PKG import functionality of staging table
    * AL_WORK_PACKAGE, AL_WORK_PACKAGE_TASK and AL_WORK_PACKAGE_TASK_ATA. Multiple records will be
    * tested.
    *
    *
    */

   @Test
   public void testMultipleRecords_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testMultipleRecords_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( "Import failed when expected to pass",
            runValidationAndImport( false, true ) == 1 );

      iEVENT_1 = null;
      iEVENT_2 = null;
      // Verify EVT_EVENT table
      iAccount_id = getAccountIds( iISSUE_ACCOUNT_CD );
      iEVENT_1 = verifyEVTEVENT( "TS", iWKP_NAME_1, "ACTV", iWKP_DESC_1, iAccount_id );
      iEVENT_2 = verifyEVTEVENT( "TS", iWKP_NAME_2, "ACTV", iWKP_DESC_2, iAccount_id );

      // Verify EVT_INV table
      inventoryData linvData1 = verifyEVTINV( iEVENT_1 );
      inventoryData linvData2 = verifyEVTINV( iEVENT_2 );

      // Verify EVT_STAGE table
      VerifyEVTSTAGE( iEVENT_1, iNoteString1, iNoteString2 );
      VerifyEVTSTAGE( iEVENT_2, iNoteString1, iNoteString3 );

      // Verify SCHED_STASK table
      VerifySCHEDSTASK( iEVENT_1, iTaskClassCD, iSUB_TYPE_CD_1, linvData1 );
      VerifySCHEDSTASK( iEVENT_2, iTaskClassCD_2, iSUB_TYPE_CD_2, linvData2 );

      // Verify evt_loc
      simpleIDs lLocIds = getLocIds( iSCHED_LOC_CD_1 );
      VerifyEVTLOC( iEVENT_1, lLocIds );

      lLocIds = getLocIds( iSCHED_LOC_CD_2 );
      VerifyEVTLOC( iEVENT_2, lLocIds );

      // Verify sched_work_type
      VerifySCHEDWORKTYPE( iEVENT_1, iWORK_TYPE_LIST_1, iWORK_TYPE_LIST_2 );
      VerifySCHEDWORKTYPE( iEVENT_2, iWORK_TYPE_LIST_1, iWORK_TYPE_LIST_2 );

      // Verify sched_wp
      VerifySCHEDWP( iEVENT_1 );
      VerifySCHEDWP( iEVENT_2 );

      // Verify sched_wp_sign_req
      VerifySCHEDWPSIGNREQ( iEVENT_1, iSkill1, iSkill2 );
      VerifySCHEDWPSIGNREQ( iEVENT_2, iSkill1, iSkill2 );

      // Verify axon event publish
      verifyTaskCreatedEventPublish();

      // Verify axon event publish
      verifyTaskAssignedToWorkPackageEventPublish();

   }


   /**
    * This test is to verify a.load_work_package.bat is to load data from csv file to staging table
    * properly.
    *
    *
    */

   public void testLoadCSV() {

      BatchFileManager lFileMgr = new BatchFileManager();
      // load WPK_CSV_FILE
      lFileMgr.copyFile( TestConstants.TEST_CASE_DATA, TestConstants.WPK_CSV_FILE,
            TestConstants.WPK_BATCH_FILE + "\\data\\" );

      // load WPK_TASK_CSV_FILE
      lFileMgr.copyFile( TestConstants.TEST_CASE_DATA, TestConstants.WPK_TASK_CSV_FILE,
            TestConstants.WPK_BATCH_FILE + "\\data\\" );

      // load WPK_TASK_ATA_CSV_FILE
      lFileMgr.copyFile( TestConstants.TEST_CASE_DATA, TestConstants.WPK_TASK_ATA_CSV_FILE,
            TestConstants.WPK_BATCH_FILE + "\\data\\" );
      lFileMgr.loadWORKPACKAGEViaDataFile( "" );

      // Verify staging tables are loaded
      VerifyStagingTable();

   }


   /**
    * This test is to verify b.load_work_package.bat works exactly same way as
    * AL_WORK_PACKAGE_import.validate_work_package .
    *
    *
    */

   public void testValidateCSV() {

      testLoadCSV();
      BatchFileManager lFileMgr = new BatchFileManager();
      lFileMgr.validateWORKPACKAGEViaBatch();
      VerifyValidation();

   }


   /**
    * This test is to verify b.load_work_package.bat works exactly same way as
    * AL_WORK_PACKAGE_import.validate_work_package .
    *
    *
    */
   @Test
   public void testImportCSV() {

      testValidateCSV();
      BatchFileManager lFileMgr = new BatchFileManager();
      lFileMgr.importWORKPACKAGEViaBatch();

      VerifyImport();

   }


   /**
    * This test is to verify AL_WORK_PACKAGE_PKG validation functionality of staging table
    * AL_WORK_PACKAGE and AL_WORK_PACKAGE_TASK. It will also check to see if Work package and REQ
    * Task can be assigned to ACFT Root.
    *
    *
    */
   @Test
   public void testACFTASK_Root_Validation_Import() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      iEVENT_1 = null;

      // AL_WORK_PACKAGE table
      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );

      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_13 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation failed when expected to pass",
            runValidationAndImport( true, true ) == 1 );

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( "Import failed when expected to pass",
            runValidationAndImport( false, true ) == 1 );

      iEVENT_1 = null;
      // Verify EVT_EVENT table
      iAccount_id = getAccountIds( iISSUE_ACCOUNT_CD );
      iEVENT_1 = verifyEVTEVENT( "TS", iWKP_NAME_1, "ACTV", iWKP_DESC_1, iAccount_id );

      // Verify EVT_INV table
      inventoryData linvData = verifyEVTINV( iEVENT_1 );

      // Verify EVT_STAGE table
      VerifyEVTSTAGE( iEVENT_1, iNoteString1, iNoteString2 );

      // Verify SCHED_STASK table
      VerifySCHEDSTASK( iEVENT_1, iTaskClassCD, iSUB_TYPE_CD_1, linvData );

      // Verify evt_loc
      simpleIDs lLocIds = getLocIds( iSCHED_LOC_CD_1 );
      VerifyEVTLOC( iEVENT_1, lLocIds );

      // Verify sched_work_type
      VerifySCHEDWORKTYPE( iEVENT_1, iWORK_TYPE_LIST_1, iWORK_TYPE_LIST_2 );

      // Verify sched_wp
      VerifySCHEDWP( iEVENT_1 );

      // Verify sched_wp_sign_req
      VerifySCHEDWPSIGNREQ( iEVENT_1, iSkill1, iSkill2 );

      // Verify evt_event_rel
      VerifyEvtEventRel( iEVENT_1, "DRVTASK" );

      // Verify axon event publish
      verifyTaskCreatedEventPublish();

      // Verify axon event publish
      verifyTaskAssignedToWorkPackageEventPublish();

   }


   /**
    * This test is to verify AL_WORK_PACKAGE_PKG validation functionality of staging table
    * AL_WORK_PACKAGE and AL_WORK_PACKAGE_TASK. It will also check to see if Work package and REQ
    * Task can be assigned to ACFT Root.
    *
    *
    */
   @Test
   public void testDefaultValues_Validation_Import() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      iEVENT_1 = null;

      // AL_WORK_PACKAGE table
      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      // lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" ); // this should
      // be defaulted to 0
      // lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" ); // this
      // should be defaulted to 0
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      // run validation
      Assert.assertTrue( "Validation failed when expected to pass",
            runValidationAndImport( true, true ) == 1 );

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( "Import failed when expected to pass",
            runValidationAndImport( false, true ) == 1 );

      iAccount_id = getAccountIds( iISSUE_ACCOUNT_CD );
      iEVENT_1 = verifyEVTEVENT( "TS", iWKP_NAME_1, "ACTV", iWKP_DESC_1, iAccount_id );

      Assert.assertTrue( "Default values are not being saved in DB",
            VerifyDefaultValues( iEVENT_1, 0, 0 ) == 1 );

      // Verify axon event publish
      verifyTaskCreatedEventPublish();

   }


   /**
    *
    * test OPER-26304 AL_WORK_PACKAGE: Scheduled Location is optional
    *
    */
   @Test
   public void testSCHED_LOC_CD_IsOptionalField() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", null );
      lWPK.put( "SCHED_START_DT", "SYSDATE" );
      lWPK.put( "SCHED_END_DT", "SYSDATE+1" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Error when expected to pass ",
            runValidationAndImport( true, true ) == 1 );

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( "Import failed when expected to pass",
            runValidationAndImport( false, true ) == 1 );

      iEVENT_1 = null;
      // Verify EVT_EVENT table
      iAccount_id = getAccountIds( iISSUE_ACCOUNT_CD );
      iEVENT_1 = verifyEVTEVENT( "TS", iWKP_NAME_1, "ACTV", iWKP_DESC_1, iAccount_id );

      // Verify evt_loc
      simpleIDs lLocIds = new simpleIDs( null, null );
      VerifyEVTLOC( iEVENT_1, lLocIds );
   }


   /**
    * This test is to verify the successful loading of Batch parts that are assigned to work package
    * and WP task.
    *
    *
    */
   @Test
   public void testBatchParts_Validation_Import() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      iEVENT_1 = null;

      // AL_WORK_PACKAGE table
      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_4 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_4 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_4 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_4 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_4 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_4 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_4 + "\'" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_4 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_4 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_4 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_4 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_4 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_4 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_4 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_4 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_8 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation failed when expected to pass",
            runValidationAndImport( true, true ) == 1 );

      System.out.println( "============= Finish validation =========" );

      // run import
      Assert.assertTrue( "Import failed when expected to pass",
            runValidationAndImport( false, true ) == 1 );

      iEVENT_1 = null;
      // Verify EVT_EVENT table
      iAccount_id = getAccountIds( iISSUE_ACCOUNT_CD );
      iEVENT_1 = verifyEVTEVENT( "TS", iWKP_NAME_4, "ACTV", iWKP_DESC_4, iAccount_id );

      // Verify EVT_INV table
      inventoryData linvData = verifyEVTINVforBatach( iEVENT_1 );

      // Verify EVT_STAGE table
      VerifyEVTSTAGE( iEVENT_1, iNoteString1, iNoteString3 );

      // Verify SCHED_STASK table
      VerifySCHEDSTASK( iEVENT_1, iTaskClassCD_2, iSUB_TYPE_CD_4, linvData );

      // Verify evt_loc
      simpleIDs lLocIds = getLocIds( iSCHED_LOC_CD_4 );
      VerifyEVTLOC( iEVENT_1, lLocIds );

      // Verify sched_work_type
      VerifySCHEDWORKTYPE( iEVENT_1, iWORK_TYPE_LIST_1, iWORK_TYPE_LIST_2 );

      // Verify sched_wp
      VerifySCHEDWP( iEVENT_1 );

      // Verify sched_wp_sign_req
      VerifySCHEDWPSIGNREQ( iEVENT_1, iSkill1, iSkill2 );

      // Verify axon event publish
      verifyTaskCreatedEventPublish();

   }


   @Test
   public void testNestedTaskCd() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_work_pckkage table
      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" );
      lWPK.put( "SCHED_START_DT", "SYSDATE" );
      lWPK.put( "SCHED_END_DT", "SYSDATE+1" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // AL_WORK_PACKAGE_TASK_ATA
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_ata_5 + "\'" );
      lWPK.put( "ATA_SYS_CD", "\'" + iata_sys_cd_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation failed when expected to pass",
            runValidationAndImport( true, true ) == 1 );

      System.out.println( "============= Finish validation =========" );

      // run import
      Assert.assertTrue( "Import failed when expected to pass",
            runValidationAndImport( false, true ) == 1 );

      iEVENT_1 = null;
      // Verify EVT_EVENT table
      iAccount_id = getAccountIds( iISSUE_ACCOUNT_CD );
      iEVENT_1 = verifyEVTEVENT( "TS", iWKP_NAME_1, "ACTV", iWKP_DESC_1, iAccount_id );

      // Verify EVT_INV table
      inventoryData linvData = verifyEVTINV( iEVENT_1 );

      // Verify EVT_STAGE table
      VerifyEVTSTAGE( iEVENT_1, iNoteString1, iNoteString2 );

      // Verify SCHED_STASK table
      VerifySCHEDSTASK( iEVENT_1, iTaskClassCD, iSUB_TYPE_CD_1, linvData );

      // Verify evt_loc
      simpleIDs lLocIds = getLocIds( iSCHED_LOC_CD_1 );
      VerifyEVTLOC( iEVENT_1, lLocIds );

      // Verify sched_work_type
      VerifySCHEDWORKTYPE( iEVENT_1, iWORK_TYPE_LIST_1, iWORK_TYPE_LIST_2 );

      // Verify sched_wp
      VerifySCHEDWP( iEVENT_1 );

      // Verify sched_wp_sign_req
      VerifySCHEDWPSIGNREQ( iEVENT_1, iSkill1, iSkill2 );

      // Verify nested TASK_CDs
      VerifyTaskCds( iWKP_NAME_1, iTaskCd1, iTaskCd2, iTaskCd3 );

      // Verify evt_event_rel
      VerifyEvtEventRel( iEVENT_1, "DRVTASK" );
   }


   /**
    * This test is to verify AL_WORK_PACKAGE_PKG validation functionality of staging table
    * AL_WORK_PACKAGE, AL_WORK_PACKAGE_TASK. H_EVENT ID would point to wkp for all linked tasks. WKP
    * contains block, block map to req, req map to jic. all blocks, req and jic h_event_id all
    * re-assigned to wpk event_id.
    *
    *
    */
   @Test
   public void testBLOCKREQJICMAP_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      iEVENT_1 = null;

      // AL_WORK_PACKAGE table
      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );

      lWPK.put( "SERIAL_NO_OEM", "'SN000001'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "TASK_CD", "'BLOCK4'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation failed when expected to pass",
            runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify AL_WORK_PACKAGE_PKG import functionality of staging table
    * AL_WORK_PACKAGE, AL_WORK_PACKAGE_TASK. H_EVENT ID would point to wkp for all linked tasks. WKP
    * contains block, block map to req, req map to jic. all blocks, req and jic h_event_id all
    * re-assigned to wpk event_id.
    *
    *
    */
   @Test
   public void testBLOCKREQJICMAP_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testBLOCKREQJICMAP_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( "Import failed when expected to pass",
            runValidationAndImport( false, true ) == 1 );

      iEVENT_1 = null;
      iEVENT_BLOCK = null;
      iEVENT_REQ = null;
      iEVENT_JIC = null;

      // Verify EVT_EVENT table
      iAccount_id = getAccountIds( iISSUE_ACCOUNT_CD );
      iEVENT_1 = verifyEVTEVENT( "TS", iWKP_NAME_1, "ACTV", iWKP_DESC_1, iAccount_id );
      iEVENT_BLOCK = verifyEVTEVENT_nested( "TS", iBLOCK4_EVENT_SDESC, iEVENT_1, "ACTV",
            iBLOCK4_EVENT_LDESC, iEVENT_1 );
      iEVENT_REQ =
            verifyEVTEVENT_nested( "TS", iAT2_EVENT_SDESC, iEVENT_1, "ACTV", null, iEVENT_BLOCK );
      iEVENT_JIC = verifyEVTEVENT_nested( "TS", iAT_JIC_1_EVENT_SDESC, iEVENT_1, "ACTV", null,
            iEVENT_REQ );

      // Verify EVT_INV table
      inventoryData linvData = verifyEVTINV( iEVENT_1 );

      // Verify EVT_STAGE table
      VerifyEVTSTAGE( iEVENT_1, iNoteString1, iNoteString2 );

      // Verify SCHED_STASK table
      VerifySCHEDSTASK( iEVENT_1, iTaskClassCD, iSUB_TYPE_CD_1, linvData );

      // Verify evt_loc
      simpleIDs lLocIds = getLocIds( iSCHED_LOC_CD_1 );
      VerifyEVTLOC( iEVENT_1, lLocIds );

      // Verify sched_work_type
      VerifySCHEDWORKTYPE( iEVENT_1, iWORK_TYPE_LIST_1, iWORK_TYPE_LIST_2 );

      // Verify sched_wp
      VerifySCHEDWP( iEVENT_1 );

      // Verify sched_wp_sign_req
      VerifySCHEDWPSIGNREQ( iEVENT_1, iSkill1, iSkill2 );

      // Verify axon event publish
      verifyTaskCreatedEventPublish();

      // Verify axon event publish
      verifyTaskAssignedToWorkPackageEventPublish();

   }


   /**
    * This test is to verify AL_WORK_PACKAGE_PKG validation functionality of staging table
    * AL_WORK_PACKAGE, AL_WORK_PACKAGE_TASK. H_EVENT ID would point to wkp for all linked tasks. WKP
    * contains req, req map to jic. Req and jic h_event_id all re-assigned to wpk event_id.
    *
    *
    */
   @Test
   public void testREQJICMAP_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      iEVENT_1 = null;

      // AL_WORK_PACKAGE table
      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );

      lWPK.put( "SERIAL_NO_OEM", "'SN000001'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "TASK_CD", "'AT3'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation failed when expected to pass",
            runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify AL_WORK_PACKAGE_PKG import functionality of staging table
    * AL_WORK_PACKAGE, AL_WORK_PACKAGE_TASK. H_EVENT ID would point to wkp for all linked tasks. WKP
    * contains req, req map to jic. Req and jic h_event_id all re-assigned to wpk event_id.
    *
    *
    */
   @Test
   public void testREQJICMAP_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testREQJICMAP_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( "Import failed when expected to pass",
            runValidationAndImport( false, true ) == 1 );

      iEVENT_1 = null;
      iEVENT_BLOCK = null;
      iEVENT_REQ = null;
      iEVENT_JIC = null;
      iEVENT_REQ_2 = null;
      iEVENT_JIC_2 = null;

      // Verify EVT_EVENT table
      iAccount_id = getAccountIds( iISSUE_ACCOUNT_CD );
      iEVENT_1 = verifyEVTEVENT( "TS", iWKP_NAME_1, "ACTV", iWKP_DESC_1, iAccount_id );
      // iEVENT_BLOCK = verifyEVTEVENT_nested( "TS", iBLOCK4_EVENT_SDESC, iEVENT_1, "ACTV",
      // iBLOCK4_EVENT_LDESC, iEVENT_1 );
      iEVENT_REQ_2 =
            verifyEVTEVENT_nested( "TS", iAT3_EVENT_SDESC, iEVENT_1, "ACTV", null, iEVENT_1 );
      iEVENT_JIC_2 = verifyEVTEVENT_nested( "TS", iAT_JIC_2_EVENT_SDESC, iEVENT_1, "ACTV", null,
            iEVENT_REQ_2 );

      // Verify EVT_INV table
      inventoryData linvData = verifyEVTINV( iEVENT_1 );

      // Verify EVT_STAGE table
      VerifyEVTSTAGE( iEVENT_1, iNoteString1, iNoteString2 );

      // Verify SCHED_STASK table
      VerifySCHEDSTASK( iEVENT_1, iTaskClassCD, iSUB_TYPE_CD_1, linvData );

      // Verify evt_loc
      simpleIDs lLocIds = getLocIds( iSCHED_LOC_CD_1 );
      VerifyEVTLOC( iEVENT_1, lLocIds );

      // Verify sched_work_type
      VerifySCHEDWORKTYPE( iEVENT_1, iWORK_TYPE_LIST_1, iWORK_TYPE_LIST_2 );

      // Verify sched_wp
      VerifySCHEDWP( iEVENT_1 );

      // Verify sched_wp_sign_req
      VerifySCHEDWPSIGNREQ( iEVENT_1, iSkill1, iSkill2 );

      // Verify axon event publish
      verifyTaskCreatedEventPublish();

      // Verify axon event publish
      verifyTaskAssignedToWorkPackageEventPublish();

   }


   // ===========================================================================================

   /**
    *
    * This will verify values Heavy Maintenance and Collection Required values.
    *
    * @param iEVENT_1
    * @param aHeavyBool
    *           - expected value for Heavy Maintenance bool
    * @param aCollectionBool
    *           - expected value for Collection Required bool
    * @return row count if the values were found in the db
    */
   private int VerifyDefaultValues( simpleIDs iEVENT_1, int aHeavyBool, int aCollectionBool ) {
      String lQuery = "Select count(*) from sched_stask where Heavy_bool = " + aHeavyBool
            + " and collection_required_bool = " + aCollectionBool + " and sched_db_id = "
            + iEVENT_1.getNO_DB_ID() + " and sched_id = " + iEVENT_1.getNO_ID();
      return countRowsOfQuery( lQuery );
   }


   /**
    * Data Setup the for nested Task Code
    *
    */
   private void dataSetupNestedTaskCd() {
      String lQuery = "Select event_db_id, event_id from evt_event where event_sdesc = '" + iTaskCd1
            + "' and event_status_cd = 'ACTV'";
      iEvtTopTaskCd = getIDs( lQuery, "event_db_id", "event_id" );
      // Make sure it is top level task cd
      lQuery = "Update evt_event set h_event_db_id = " + iEvtTopTaskCd.getNO_DB_ID()
            + ", h_event_id = " + iEvtTopTaskCd.getNO_ID() + " where event_db_id = "
            + iEvtTopTaskCd.getNO_DB_ID() + " AND event_id = " + iEvtTopTaskCd.getNO_ID();
      runUpdate( lQuery );
      // get the smallest event_id
      lQuery = " SELECT event_db_id, event_id FROM evt_event where event_sdesc = '" + iTaskCd2
            + "' and event_status_cd = 'ACTV' AND sub_event_ord = 1";
      iEvtMidTaskCd = getIDs( lQuery, "event_db_id", "event_id" );
      // Make sure it is middle level task cd
      lQuery = "Update evt_event set nh_event_db_id = " + iEvtTopTaskCd.getNO_DB_ID()
            + ", nh_event_id = " + iEvtTopTaskCd.getNO_ID() + ", h_event_db_id = "
            + iEvtTopTaskCd.getNO_DB_ID() + ", h_event_id = " + iEvtTopTaskCd.getNO_ID()
            + " where event_db_id = " + iEvtMidTaskCd.getNO_DB_ID() + " AND event_id = "
            + iEvtMidTaskCd.getNO_ID();
      runUpdate( lQuery );
      lQuery = "Select event_db_id, event_id from evt_event where event_sdesc = '" + iTaskCd3
            + "' and event_status_cd = 'ACTV'";
      iEvtLowTaskCd = getIDs( lQuery, "event_db_id", "event_id" );
      // Make sure it is low level task cd
      lQuery = "Update evt_event set nh_event_db_id = " + iEvtMidTaskCd.getNO_DB_ID()
            + ", nh_event_id = " + iEvtMidTaskCd.getNO_ID() + ", h_event_db_id = "
            + iEvtTopTaskCd.getNO_DB_ID() + ", h_event_id = " + iEvtTopTaskCd.getNO_ID()
            + " where event_db_id = " + iEvtLowTaskCd.getNO_DB_ID() + " AND event_id = "
            + iEvtLowTaskCd.getNO_ID();
      runUpdate( lQuery );
   }

}
