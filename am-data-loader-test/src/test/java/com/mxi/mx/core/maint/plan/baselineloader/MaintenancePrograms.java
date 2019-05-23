package com.mxi.mx.core.maint.plan.baselineloader;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.core.maint.plan.datamodels.taskIDs;
import com.mxi.mx.util.BaselineLoaderTest;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.ValidationAndImport;
import com.mxi.mx.util.WhereClause;


/**
 * This test suite contains test cases on validation functionality of C_MAINT_PRGM_TASK.
 */
public class MaintenancePrograms extends BaselineLoaderTest {

   private final String iISSUE_ORD = "1";
   private final String iFOLLOW_ON_TASK_ATA_CODE = "ACFT_CD1";
   private final String iFOLLOW_ON_TASK_CODE = "aFollowOnTaskCode";
   private final String iREPREF_TASK_ATA_CODE = "ACFT_CD1";
   private final String iREPREF_TASK_CODE = "aREPREFTaskCode";
   private final String iCARRIER_REVISION_ORDER = "1";
   private final String iASSEMBLY_CODE_ACFT = "ACFT_CD1";
   private final String iASSEMBLY_CODE_ENG = "ENG_CD1";
   private final String iCARRIER_CODE = "MXI";
   private final String iMAINTENANCE_PROGRAM_SHORT_DESCRIPTION =
         "aMaintenanceProgramShortDescription";
   private final String iMAINTENANCE_PROGRAM_SHORT_DESCRIPTION_ENG =
         "bMaintenanceProgramShortDescription";
   private final String iMAINTENANCE_PROGRAM_LONG_DESCRIPTION =
         "aMaintenanceProgramLongDescription";
   private final String iMAINTENANCE_PROGRAM_LONG_DESCRIPTION_ENG =
         "bMaintenanceProgramLongDescription";
   private final String iEXT_REF_DESC = "aMaintenanceProgramREF";
   private final String iEXT_REF_DESC_ENG = "bMaintenanceProgramREF";
   private final String iMAINTENANCE_PROGRAM_CODE = "aMaintenanceProgramCode";
   private final String iMAINTENANCE_PROGRAM_CODE_ENG = "bMaintenanceProgramCode";
   private final String TASK_CLASS_CD_FOLLOW = "FOLLOW";
   private final String TASK_CLASS_CD_REPREF = "REPREF";

   private final String iTASK_ATA_CD_ACFT = "ACFT_CD1";
   private final String iTASK_ATA_CD_ENG = "ENG-SYS-1";
   private final String iTASK_ATA_CD_ACFT_SYS = "SYS-1-1";
   private final String iGROUP_CD = "GROUP TEST";
   private final String iTASK_CD_ACFT = "AL_APPLIC";
   private final String iTASK_CD_ENG = "ENG-SYS-AL-TASK";
   private final String iTASK_CD_ACFT_REF = "ATREFTWO";
   private final String iTASK_CD_NAME_ACFT = "AL_APPLIC";
   private final String iTASK_CD_NAME_ENG = "ENG-SYS-AL-TASK";
   private final String iTASK_CD_NAME_ACFT_REF = "ATREFTWONAME";
   private final String iTASK_REV_REASON_CD_1 = "NEW";
   private final String iACTION_NOTE = "AUTO TEST";

   private simpleIDs iTaskDefinitionIDs = null;
   private ArrayList<simpleIDs> iDeleteRequirementIDs = new ArrayList<simpleIDs>();
   private ArrayList<String> iDeleteRequirementCodes = new ArrayList<String>();

   private final String iMAINT_PRGM_STATUS_CD_BUILD = "BUILD";

   public simpleIDs iPRGM_IDs = null;
   public simpleIDs iPRGM_DEFN_IDs = null;
   public simpleIDs iPRGM_IDs_2 = null;
   public simpleIDs iPRGM_DEFN_IDs_2 = null;

   @Rule
   public TestName testName = new TestName();
   ValidationAndImport ivalidationandimport;


   /**
    * Clean up after each individual test
    */
   @After
   @Override
   public void after() {
      deleteRequiredData();
      RestoreData();
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
      clearBaselineLoaderTables();
      iPRGM_IDs = null;
      iPRGM_DEFN_IDs = null;
      iPRGM_IDs_2 = null;
      iPRGM_DEFN_IDs_2 = null;
   }


   /**
    * This test is to verify OPER-24637: A REQ of type FOLLOW cannot be a part of a Maintenance
    * Program. If a REQ of type FOLLOW is added, validation error code 'CFGMNT-20003' is thrown.
    *
    */

   @Test
   public void testOPER24637_Error20003_TASK_CLASS_FOLLOW_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_MAINT_PRGM
      lReqMap.put( "MAINT_PRGM_CD", "'" + iMAINTENANCE_PROGRAM_CODE + "'" );
      lReqMap.put( "MAINT_PRGM_SDESC", "'" + iMAINTENANCE_PROGRAM_SHORT_DESCRIPTION + "'" );
      lReqMap.put( "CARRIER_CD", "'" + iCARRIER_CODE + "'" );
      lReqMap.put( "CARRIER_REVISION_ORD", "'" + iCARRIER_REVISION_ORDER + "'" );
      lReqMap.put( "ASSMBL_CD", "'" + iASSEMBLY_CODE_ACFT + "'" );
      // Insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_MAINT_PRGM, lReqMap ) );

      // C_MAINT_PRGM_TASK
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iASSEMBLY_CODE_ACFT + "'" );
      lReqMap.put( "MAINT_PRGM_CD", "'" + iMAINTENANCE_PROGRAM_CODE + "'" );
      lReqMap.put( "TASK_ATA_CD", "'" + iFOLLOW_ON_TASK_ATA_CODE + "'" );
      lReqMap.put( "TASK_CD", "'" + iFOLLOW_ON_TASK_CODE + "'" );
      lReqMap.put( "ISSUE_ORD", iISSUE_ORD );

      createRequirement( TASK_CLASS_CD_FOLLOW, iFOLLOW_ON_TASK_CODE, iFOLLOW_ON_TASK_CODE );
      // Insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_MAINT_PRGM_TASK, lReqMap ) );

      // Run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // Validate CFGMNT-20003 error
      validateErrorCode( "CFGMNT-20003" );

   }


   /**
    * This test is to verify OPER-24638: A REQ of type REPREF cannot be a part of a Maintenance
    * Program. If a REQ of type REPREF is added, validation error code 'CFGMNT-20004' is thrown.
    *
    */

   @Test
   public void testOPER24638_Error20004_TASK_CLASS_REPREF_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_MAINT_PRGM
      lReqMap.put( "MAINT_PRGM_CD", "'" + iMAINTENANCE_PROGRAM_CODE + "'" );
      lReqMap.put( "MAINT_PRGM_SDESC", "'" + iMAINTENANCE_PROGRAM_SHORT_DESCRIPTION + "'" );
      lReqMap.put( "MAINT_PRGM_LDESC", "'" + iMAINTENANCE_PROGRAM_LONG_DESCRIPTION + "'" );
      lReqMap.put( "CARRIER_CD", "'" + iCARRIER_CODE + "'" );
      lReqMap.put( "CARRIER_REVISION_ORD", "'" + iCARRIER_REVISION_ORDER + "'" );
      lReqMap.put( "ASSMBL_CD", "'" + iASSEMBLY_CODE_ACFT + "'" );
      // Insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_MAINT_PRGM, lReqMap ) );

      // C_MAINT_PRGM_TASK
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iASSEMBLY_CODE_ACFT + "'" );
      lReqMap.put( "MAINT_PRGM_CD", "'" + iMAINTENANCE_PROGRAM_CODE + "'" );
      lReqMap.put( "TASK_ATA_CD", "'" + iREPREF_TASK_ATA_CODE + "'" );
      lReqMap.put( "TASK_CD", "'" + iREPREF_TASK_CODE + "'" );
      lReqMap.put( "ISSUE_ORD", iISSUE_ORD );
      // Insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_MAINT_PRGM_TASK, lReqMap ) );
      createRequirement( TASK_CLASS_CD_REPREF, iREPREF_TASK_CODE, iREPREF_TASK_CODE );

      // Run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // Validate CFGMNT-20004 error
      validateErrorCode( "CFGMNT-20004" );

   }


   /**
    * This test is to verify validation functionality on MAINT_PRGM_IMPORT with REQ on acft root
    *
    */

   @Test
   public void tesACFT_REQ_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_MAINT_PRGM
      lReqMap.put( "ASSMBL_CD", "'" + iASSEMBLY_CODE_ACFT + "'" );
      lReqMap.put( "MAINT_PRGM_CD", "'" + iMAINTENANCE_PROGRAM_CODE + "'" );
      lReqMap.put( "MAINT_PRGM_SDESC", "'" + iMAINTENANCE_PROGRAM_SHORT_DESCRIPTION + "'" );
      lReqMap.put( "MAINT_PRGM_LDESC", "'" + iMAINTENANCE_PROGRAM_LONG_DESCRIPTION + "'" );
      lReqMap.put( "CARRIER_CD", "'" + iCARRIER_CODE + "'" );
      lReqMap.put( "CARRIER_REVISION_ORD", "'" + iCARRIER_REVISION_ORDER + "'" );
      lReqMap.put( "EXT_REF_DESC", "'" + iEXT_REF_DESC + "'" );
      // Insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_MAINT_PRGM, lReqMap ) );

      // C_MAINT_PRGM_TASK
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iASSEMBLY_CODE_ACFT + "'" );
      lReqMap.put( "MAINT_PRGM_CD", "'" + iMAINTENANCE_PROGRAM_CODE + "'" );
      lReqMap.put( "GROUP_CD", "'" + iGROUP_CD + "'" );
      lReqMap.put( "TASK_ATA_CD", "'" + iTASK_ATA_CD_ACFT + "'" );
      lReqMap.put( "TASK_CD", "'" + iTASK_CD_ACFT + "'" );
      lReqMap.put( "TASK_REV_REASON_CD", "'" + iTASK_REV_REASON_CD_1 + "'" );
      lReqMap.put( "ACTION_NOTE", "'" + iACTION_NOTE + "'" );
      lReqMap.put( "ISSUE_ORD", "'" + iISSUE_ORD + "'" );
      // Insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_MAINT_PRGM_TASK, lReqMap ) );

      // Run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify import functionality on MAINT_PRGM_IMPORT with REQ on acft root
    *
    */

   @Test
   public void tesACFT_REQ_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      tesACFT_REQ_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // verify maint_prgm table
      iPRGM_IDs = getPRGMIds( iMAINTENANCE_PROGRAM_CODE, iMAINTENANCE_PROGRAM_SHORT_DESCRIPTION,
            iMAINTENANCE_PROGRAM_LONG_DESCRIPTION );
      iPRGM_DEFN_IDs = verifyMaintPrgm( iPRGM_IDs, iMAINT_PRGM_STATUS_CD_BUILD, iEXT_REF_DESC );

      // verify main_prgm_carrier_map table
      simpleIDs lcarrierIds = getCarrierIds( iCARRIER_CODE );
      verifyMaintPrgmCarrierMap( iPRGM_IDs, lcarrierIds, iCARRIER_REVISION_ORDER );

      // verify maint_prgm_defn table
      verifyMaintPrgmDefn( iPRGM_DEFN_IDs, iASSEMBLY_CODE_ACFT );

      // verify maint_prgm_task
      taskIDs ltaskIds = getTaskIdsAndDefnIds( iTASK_CD_ACFT, iTASK_CD_NAME_ACFT );
      verifyMaintPrgmTask( iPRGM_IDs, ltaskIds, iGROUP_CD, iTASK_REV_REASON_CD_1, iACTION_NOTE,
            iISSUE_ORD );

   }


   /**
    * This test is to verify validation functionality on MAINT_PRGM_IMPORT with REQ on ENG sys slot
    *
    */
   @Test
   public void tesENG_REQ_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_MAINT_PRGM
      lReqMap.put( "ASSMBL_CD", "'" + iASSEMBLY_CODE_ENG + "'" );
      lReqMap.put( "MAINT_PRGM_CD", "'" + iMAINTENANCE_PROGRAM_CODE_ENG + "'" );
      lReqMap.put( "MAINT_PRGM_SDESC", "'" + iMAINTENANCE_PROGRAM_SHORT_DESCRIPTION_ENG + "'" );
      lReqMap.put( "MAINT_PRGM_LDESC", "'" + iMAINTENANCE_PROGRAM_LONG_DESCRIPTION_ENG + "'" );
      lReqMap.put( "CARRIER_CD", "'" + iCARRIER_CODE + "'" );
      lReqMap.put( "CARRIER_REVISION_ORD", "'" + iCARRIER_REVISION_ORDER + "'" );
      lReqMap.put( "EXT_REF_DESC", "'" + iEXT_REF_DESC_ENG + "'" );
      // Insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_MAINT_PRGM, lReqMap ) );

      // C_MAINT_PRGM_TASK
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iASSEMBLY_CODE_ENG + "'" );
      lReqMap.put( "MAINT_PRGM_CD", "'" + iMAINTENANCE_PROGRAM_CODE_ENG + "'" );
      lReqMap.put( "GROUP_CD", "'" + iGROUP_CD + "'" );
      lReqMap.put( "TASK_ATA_CD", "'" + iTASK_ATA_CD_ENG + "'" );
      lReqMap.put( "TASK_CD", "'" + iTASK_CD_ENG + "'" );
      lReqMap.put( "TASK_REV_REASON_CD", "'" + iTASK_REV_REASON_CD_1 + "'" );
      lReqMap.put( "ACTION_NOTE", "'" + iACTION_NOTE + "'" );
      lReqMap.put( "ISSUE_ORD", "'" + iISSUE_ORD + "'" );
      // Insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_MAINT_PRGM_TASK, lReqMap ) );

      // Run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify root functionality on MAINT_PRGM_IMPORT with REQ on ENG sys slot
    *
    */

   @Test
   public void tesENG_REQ_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      tesENG_REQ_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // verify maint_prgm table
      iPRGM_IDs = getPRGMIds( iMAINTENANCE_PROGRAM_CODE_ENG,
            iMAINTENANCE_PROGRAM_SHORT_DESCRIPTION_ENG, iMAINTENANCE_PROGRAM_LONG_DESCRIPTION_ENG );
      iPRGM_DEFN_IDs = verifyMaintPrgm( iPRGM_IDs, iMAINT_PRGM_STATUS_CD_BUILD, iEXT_REF_DESC_ENG );

      // verify main_prgm_carrier_map table
      simpleIDs lcarrierIds = getCarrierIds( iCARRIER_CODE );
      verifyMaintPrgmCarrierMap( iPRGM_IDs, lcarrierIds, iCARRIER_REVISION_ORDER );

      // verify maint_prgm_defn table
      verifyMaintPrgmDefn( iPRGM_DEFN_IDs, iASSEMBLY_CODE_ENG );

      // verify maint_prgm_task
      taskIDs ltaskIds = getTaskIdsAndDefnIds( iTASK_CD_ENG, iTASK_CD_NAME_ENG );
      verifyMaintPrgmTask( iPRGM_IDs, ltaskIds, iGROUP_CD, iTASK_REV_REASON_CD_1, iACTION_NOTE,
            iISSUE_ORD );

   }


   /**
    * This test is to verify validation functionality on MAINT_PRGM_IMPORT with REF on ACFT sys slot
    *
    */
   @Test
   public void tesACFT_REF_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_MAINT_PRGM
      lReqMap.put( "ASSMBL_CD", "'" + iASSEMBLY_CODE_ACFT + "'" );
      lReqMap.put( "MAINT_PRGM_CD", "'" + iMAINTENANCE_PROGRAM_CODE + "'" );
      lReqMap.put( "MAINT_PRGM_SDESC", "'" + iMAINTENANCE_PROGRAM_SHORT_DESCRIPTION + "'" );
      lReqMap.put( "MAINT_PRGM_LDESC", "'" + iMAINTENANCE_PROGRAM_LONG_DESCRIPTION + "'" );
      lReqMap.put( "CARRIER_CD", "'" + iCARRIER_CODE + "'" );
      lReqMap.put( "CARRIER_REVISION_ORD", "'" + iCARRIER_REVISION_ORDER + "'" );
      lReqMap.put( "EXT_REF_DESC", "'" + iEXT_REF_DESC + "'" );
      // Insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_MAINT_PRGM, lReqMap ) );

      // C_MAINT_PRGM_TASK
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iASSEMBLY_CODE_ACFT + "'" );
      lReqMap.put( "MAINT_PRGM_CD", "'" + iMAINTENANCE_PROGRAM_CODE + "'" );
      lReqMap.put( "GROUP_CD", "'" + iGROUP_CD + "'" );
      lReqMap.put( "TASK_ATA_CD", "'" + iTASK_ATA_CD_ACFT_SYS + "'" );
      lReqMap.put( "TASK_CD", "'" + iTASK_CD_ACFT_REF + "'" );
      lReqMap.put( "TASK_REV_REASON_CD", "'" + iTASK_REV_REASON_CD_1 + "'" );
      lReqMap.put( "ACTION_NOTE", "'" + iACTION_NOTE + "'" );
      lReqMap.put( "ISSUE_ORD", "'" + iISSUE_ORD + "'" );
      // Insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_MAINT_PRGM_TASK, lReqMap ) );

      // Run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify import functionality on MAINT_PRGM_IMPORT with REF on ACFT sys slot
    *
    */
   @Test
   public void tesACFT_REF_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      tesACFT_REF_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // verify maint_prgm table
      iPRGM_IDs = getPRGMIds( iMAINTENANCE_PROGRAM_CODE, iMAINTENANCE_PROGRAM_SHORT_DESCRIPTION,
            iMAINTENANCE_PROGRAM_LONG_DESCRIPTION );
      iPRGM_DEFN_IDs = verifyMaintPrgm( iPRGM_IDs, iMAINT_PRGM_STATUS_CD_BUILD, iEXT_REF_DESC );

      // verify main_prgm_carrier_map table
      simpleIDs lcarrierIds = getCarrierIds( iCARRIER_CODE );
      verifyMaintPrgmCarrierMap( iPRGM_IDs, lcarrierIds, iCARRIER_REVISION_ORDER );

      // verify maint_prgm_defn table
      verifyMaintPrgmDefn( iPRGM_DEFN_IDs, iASSEMBLY_CODE_ACFT );

      // verify maint_prgm_task
      taskIDs ltaskIds = getTaskIdsAndDefnIds( iTASK_CD_ACFT_REF, iTASK_CD_NAME_ACFT_REF );
      verifyMaintPrgmTask( iPRGM_IDs, ltaskIds, iGROUP_CD, iTASK_REV_REASON_CD_1, iACTION_NOTE,
            iISSUE_ORD );

   }


   /**
    * This test is to verify validation functionality on MAINT_PRGM_IMPORT with more than one maint
    * prgm
    *
    */

   @Test
   public void testMultiple_Records_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      tesACFT_REF_VALIDATION();
      tesENG_REQ_VALIDATION();

   }


   /**
    * This test is to verify import functionality on MAINT_PRGM_IMPORT with more than one maint prgm
    *
    */
   @Test
   public void testMultiple_Records_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testMultiple_Records_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // verify maint_prgm table
      iPRGM_IDs = getPRGMIds( iMAINTENANCE_PROGRAM_CODE, iMAINTENANCE_PROGRAM_SHORT_DESCRIPTION,
            iMAINTENANCE_PROGRAM_LONG_DESCRIPTION );
      iPRGM_DEFN_IDs = verifyMaintPrgm( iPRGM_IDs, iMAINT_PRGM_STATUS_CD_BUILD, iEXT_REF_DESC );

      iPRGM_IDs_2 = getPRGMIds( iMAINTENANCE_PROGRAM_CODE_ENG,
            iMAINTENANCE_PROGRAM_SHORT_DESCRIPTION_ENG, iMAINTENANCE_PROGRAM_LONG_DESCRIPTION_ENG );
      iPRGM_DEFN_IDs_2 =
            verifyMaintPrgm( iPRGM_IDs_2, iMAINT_PRGM_STATUS_CD_BUILD, iEXT_REF_DESC_ENG );

      // verify main_prgm_carrier_map table
      simpleIDs lcarrierIds = getCarrierIds( iCARRIER_CODE );
      verifyMaintPrgmCarrierMap( iPRGM_IDs, lcarrierIds, iCARRIER_REVISION_ORDER );
      verifyMaintPrgmCarrierMap( iPRGM_IDs_2, lcarrierIds, iCARRIER_REVISION_ORDER );

      // verify maint_prgm_defn table
      verifyMaintPrgmDefn( iPRGM_DEFN_IDs, iASSEMBLY_CODE_ACFT );
      verifyMaintPrgmDefn( iPRGM_DEFN_IDs_2, iASSEMBLY_CODE_ENG );

      // verify maint_prgm_task
      taskIDs ltaskIds = getTaskIdsAndDefnIds( iTASK_CD_ACFT_REF, iTASK_CD_NAME_ACFT_REF );
      verifyMaintPrgmTask( iPRGM_IDs, ltaskIds, iGROUP_CD, iTASK_REV_REASON_CD_1, iACTION_NOTE,
            iISSUE_ORD );

      taskIDs ltaskIds_2 = getTaskIdsAndDefnIds( iTASK_CD_ENG, iTASK_CD_NAME_ENG );
      verifyMaintPrgmTask( iPRGM_IDs_2, ltaskIds_2, iGROUP_CD, iTASK_REV_REASON_CD_1, iACTION_NOTE,
            iISSUE_ORD );

   }


   // ==========================================================================================================
   /**
    * This function is to verify MAINT_PRGM_task table.
    *
    *
    */
   public void verifyMaintPrgmTask( simpleIDs aPRGM_IDs, taskIDs aTaskIds, String aGROUP_CD,
         String aTASK_REV_REASON_CD, String aACTION_NOTE, String aISSUE_ORD ) {

      String[] iIds = { "TASK_DEFN_DB_ID", "TASK_DEFN_ID", "TASK_DB_ID", "TASK_ID", "GROUP_CD",
            "TASK_REV_REASON_CD", "ACTION_NOTE", "ISSUE_ORD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "MAINT_PRGM_DB_ID", aPRGM_IDs.getNO_DB_ID() );
      lArgs.addArguments( "MAINT_PRGM_ID", aPRGM_IDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.MAINT_PRGM_TASK, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "TASK_DEFN_DB_ID", llists.get( 0 ).get( 0 )
            .equalsIgnoreCase( aTaskIds.getTASK_DEFN_IDs().getNO_DB_ID() ) );
      Assert.assertTrue( "TASK_DEFN_ID",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aTaskIds.getTASK_DEFN_IDs().getNO_ID() ) );
      Assert.assertTrue( "TASK_DB_ID",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aTaskIds.getTASK_IDs().getNO_DB_ID() ) );
      Assert.assertTrue( "TASK_ID",
            llists.get( 0 ).get( 3 ).equalsIgnoreCase( aTaskIds.getTASK_IDs().getNO_ID() ) );
      Assert.assertTrue( "GROUP_CD", llists.get( 0 ).get( 4 ).equalsIgnoreCase( aGROUP_CD ) );
      Assert.assertTrue( "TASK_REV_REASON_CD",
            llists.get( 0 ).get( 5 ).equalsIgnoreCase( aTASK_REV_REASON_CD ) );
      Assert.assertTrue( "ACTION_NOTE", llists.get( 0 ).get( 6 ).equalsIgnoreCase( aACTION_NOTE ) );
      Assert.assertTrue( "ISSUE_ORD", llists.get( 0 ).get( 7 ).equalsIgnoreCase( aISSUE_ORD ) );

   }


   /**
    * This function is to verify MAINT_PRGM_DEFN table.
    *
    *
    */
   public void verifyMaintPrgmDefn( simpleIDs aPRGM_IDs, String aASSMBL_CD ) {

      String[] iIds = { "ASSMBL_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "MAINT_PRGM_DEFN_DB_ID", aPRGM_IDs.getNO_DB_ID() );
      lArgs.addArguments( "MAINT_PRGM_DEFN_ID", aPRGM_IDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.MAINT_PRGM_DEFN, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "ASSMBL_CD", llists.get( 0 ).get( 0 ).equalsIgnoreCase( aASSMBL_CD ) );

   }


   /**
    * This function is to verify MAINT_PRGM_carrier_map table.
    *
    *
    */
   public void verifyMaintPrgmCarrierMap( simpleIDs aPRGM_IDs, simpleIDs aCarrierIds,
         String aCARRIER_REVISION_ORD ) {

      String[] iIds = { "CARRIER_REVISION_ORD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "MAINT_PRGM_DB_ID", aPRGM_IDs.getNO_DB_ID() );
      lArgs.addArguments( "MAINT_PRGM_ID", aPRGM_IDs.getNO_ID() );
      lArgs.addArguments( "CARRIER_DB_ID", aCarrierIds.getNO_DB_ID() );
      lArgs.addArguments( "CARRIER_ID", aCarrierIds.getNO_ID() );

      String iQueryString =
            TableUtil.buildTableQuery( TableUtil.MAINT_PRGM_CARRIER_MAP, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "CARRIER_REVISION_ORD",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aCARRIER_REVISION_ORD ) );

   }


   /**
    * This function is to retrieve carrier IDs from ORG_CARRIER table.
    *
    *
    */
   @Override
   public simpleIDs getCarrierIds( String aCARRIER_CD ) {

      String[] iIds = { "CARRIER_DB_ID", "CARRIER_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "CARRIER_CD", aCARRIER_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.ORG_CARRIER, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );
      return lIds;
   }


   /**
    * This function is to verify MAINT_PRGM table.
    *
    *
    */
   public simpleIDs verifyMaintPrgm( simpleIDs aPRGM_IDs, String aMAINT_PRGM_STATUS_CD,
         String aEXT_REF_SDESC ) {

      String[] iIds = { "MAINT_PRGM_DEFN_DB_ID", "MAINT_PRGM_DEFN_ID", "MAINT_PRGM_STATUS_CD",
            "EXT_REF_SDESC" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "MAINT_PRGM_DB_ID", aPRGM_IDs.getNO_DB_ID() );
      lArgs.addArguments( "MAINT_PRGM_ID", aPRGM_IDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.MAINT_PRGM, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      Assert.assertTrue( "MAINT_PRGM_STATUS_CD",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aMAINT_PRGM_STATUS_CD ) );
      Assert.assertTrue( "EXT_REF_SDESC",
            llists.get( 0 ).get( 3 ).equalsIgnoreCase( aEXT_REF_SDESC ) );

      return lIds;

   }


   /**
    * This function is to retrieve prgm IDs from MAINT_PRGM table.
    *
    *
    */
   public simpleIDs getPRGMIds( String aMAINT_PRGM_CD, String aMAINT_PRGM_SDESC,
         String aMAINT_PRGM_LDESC ) {

      String[] iIds = { "MAINT_PRGM_DB_ID", "MAINT_PRGM_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "MAINT_PRGM_CD", aMAINT_PRGM_CD );
      lArgs.addArguments( "MAINT_PRGM_SDESC", aMAINT_PRGM_SDESC );
      lArgs.addArguments( "MAINT_PRGM_LDESC", aMAINT_PRGM_LDESC );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.MAINT_PRGM, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );
      return lIds;
   }


   /**
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */

   public void RestoreData() {

      String lStrDelete = null;

      if ( iPRGM_IDs != null ) {

         // delete maint_prgm_task
         lStrDelete = "delete from " + TableUtil.MAINT_PRGM_TASK + "  where MAINT_PRGM_DB_ID="
               + iPRGM_IDs.getNO_DB_ID() + " and MAINT_PRGM_ID=" + iPRGM_IDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete maint_prgm_carrier_map
         lStrDelete =
               "delete from " + TableUtil.MAINT_PRGM_CARRIER_MAP + "  where MAINT_PRGM_DB_ID="
                     + iPRGM_IDs.getNO_DB_ID() + " and MAINT_PRGM_ID=" + iPRGM_IDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete maint_prgm
         lStrDelete = "delete from " + TableUtil.MAINT_PRGM + "  where MAINT_PRGM_DB_ID="
               + iPRGM_IDs.getNO_DB_ID() + " and MAINT_PRGM_ID=" + iPRGM_IDs.getNO_ID();
         executeSQL( lStrDelete );

      }

      if ( iPRGM_DEFN_IDs != null ) {
         lStrDelete = "delete from MAINT_PRGM_DEFN where MAINT_PRGM_DEFN_DB_ID="
               + iPRGM_DEFN_IDs.getNO_DB_ID() + " and MAINT_PRGM_DEFN_ID="
               + iPRGM_DEFN_IDs.getNO_ID();
         executeSQL( lStrDelete );

      }

      if ( iPRGM_IDs_2 != null ) {

         // delete maint_prgm_task
         lStrDelete = "delete from " + TableUtil.MAINT_PRGM_TASK + "  where MAINT_PRGM_DB_ID="
               + iPRGM_IDs_2.getNO_DB_ID() + " and MAINT_PRGM_ID=" + iPRGM_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete maint_prgm_carrier_map
         lStrDelete =
               "delete from " + TableUtil.MAINT_PRGM_CARRIER_MAP + "  where MAINT_PRGM_DB_ID="
                     + iPRGM_IDs_2.getNO_DB_ID() + " and MAINT_PRGM_ID=" + iPRGM_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete maint_prgm
         lStrDelete = "delete from " + TableUtil.MAINT_PRGM + "  where MAINT_PRGM_DB_ID="
               + iPRGM_IDs_2.getNO_DB_ID() + " and MAINT_PRGM_ID=" + iPRGM_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

      }

      if ( iPRGM_DEFN_IDs_2 != null ) {
         lStrDelete = "delete from MAINT_PRGM_DEFN where MAINT_PRGM_DEFN_DB_ID="
               + iPRGM_DEFN_IDs_2.getNO_DB_ID() + " and MAINT_PRGM_DEFN_ID="
               + iPRGM_DEFN_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

      }
   }


   /**
    * This function is to implement interface ValidationAndImport
    *
    * @param: blnOnlyValidation
    * @param: allornone
    *
    * @return: return code of Int
    *
    */
   private int runValidationAndImport( boolean ablnOnlyValidation, boolean allornone ) {
      int lrtValue = 0;
      ivalidationandimport = new ValidationAndImport() {

         @Override
         public int runValidation( boolean allornone ) {
            int lReturn = 0;
            CallableStatement lPrepareMaintenanceProgramValidateCall;

            try {

               lPrepareMaintenanceProgramValidateCall = getConnection().prepareCall(
                     "BEGIN  maint_prgm_import.validate_maint_prgm(on_retcode =>?); END;" );

               lPrepareMaintenanceProgramValidateCall.registerOutParameter( 1, Types.INTEGER );
               lPrepareMaintenanceProgramValidateCall.execute();
               commit();
               lReturn = lPrepareMaintenanceProgramValidateCall.getInt( 1 );
            } catch ( SQLException e ) {
               e.printStackTrace();
            }
            return lReturn;

         }


         @Override
         public int runImport( boolean allornone ) {
            int lReturn = 0;
            CallableStatement lPrepareMaintenanceProgramImportCall;

            try {

               lPrepareMaintenanceProgramImportCall = getConnection().prepareCall(
                     "BEGIN maint_prgm_import.import_maint_prgm(on_retcode =>?); END;" );

               lPrepareMaintenanceProgramImportCall.registerOutParameter( 1, Types.INTEGER );

               lPrepareMaintenanceProgramImportCall.execute();
               commit();
               lReturn = lPrepareMaintenanceProgramImportCall.getInt( 1 );
            } catch ( SQLException e ) {
               e.printStackTrace();
            }
            return lReturn;
         }

      };

      lrtValue = ablnOnlyValidation ? ivalidationandimport.runValidation( allornone )
            : ivalidationandimport.runImport( allornone );

      return lrtValue;
   }


   /**
    * This function is to validate error code exists.
    */
   private void validateErrorCode( String aCode ) {

      List<String> llist = getErrorCodeList();
      String lerror_desc = getErrorDetail( aCode );
      Assert.assertTrue( "Error code found " + aCode + ": " + lerror_desc,
            llist.contains( aCode ) );

   }


   /**
    * This function is to retrieve errors code list
    */
   private List<String> getErrorCodeList() {
      String[] iIds = { "RESULT_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      String iQueryString = "select c_maint_prgm.result_cd " + " from c_maint_prgm "
            + " inner join DL_REF_MESSAGE on " + " DL_REF_MESSAGE.result_cd=c_maint_prgm.result_cd "
            + " UNION ALL " + " select c_maint_prgm_task.result_cd "
            + " from c_maint_prgm_task inner join DL_REF_MESSAGE on "
            + " DL_REF_MESSAGE.result_cd=c_maint_prgm_task.result_cd ";
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      List<String> llistM = new ArrayList<String>();
      for ( int i = 0; i < llists.size(); i++ ) {
         llistM.add( llists.get( i ).get( 0 ) );
      }

      return llistM;
   }


   /**
    * This function is to get detail of error code
    */
   private String getErrorDetail( String aErrorcode ) {

      String[] iIds = { "TECH_DESC" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();

      lArgs.addArguments( "RESULT_CD", aErrorcode );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.DL_REF_MESSAGE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      return llists.get( 0 ).get( 0 );

   }


   /**
    *
    * This method creates a requirement in task_task.
    *
    */
   private void createRequirement( String aTaskClassCode, String aTaskCode, String aTaskName ) {

      String iQueryString = null;

      // Create necessary task definitions
      runSqlsInTable( createTaskDefinition );

      // Get new task definition IDs
      iTaskDefinitionIDs = getNewTaskDefinitionIDs();
      iDeleteRequirementIDs.add( iTaskDefinitionIDs );

      // Construct task_task sql string and create new tasks
      String lquery =
            "select ASSMBL_BOM_ID from eqp_bom_part where ASSMBL_CD='ACFT_CD1' and bom_part_cd='ACFT_CD1'";
      String lId = getStringValueFromQuery( lquery, "ASSMBL_BOM_ID" );

      iQueryString =
            "insert into TASK_TASK (TASK_DB_ID, TASK_ID, TASK_DEFN_DB_ID, TASK_DEFN_ID, TASK_CLASS_DB_ID, TASK_CLASS_CD, ASSMBL_DB_ID, "
                  + "ASSMBL_CD, ASSMBL_BOM_ID, TASK_DEF_STATUS_DB_ID, TASK_DEF_STATUS_CD,TASK_CD, TASK_NAME)"
                  + " values (" + CONS_DB_ID + ", TASK_ID_SEQ.nextval, "
                  + iTaskDefinitionIDs.getNO_DB_ID() + ", " + iTaskDefinitionIDs.getNO_ID()
                  + ", 0, '" + aTaskClassCode + "', " + CONS_DB_ID + ", 'ACFT_CD1', " + lId
                  + ", 0, 'ACTV', '" + aTaskCode + "', '" + aTaskName + "')";
      executeSQL( iQueryString );
      iDeleteRequirementCodes.add( aTaskCode );
   }


   /**
    * This method is used to get the new task definition IDs
    *
    */
   private simpleIDs getNewTaskDefinitionIDs() {
      String[] iIds = { "TASK_DEFN_DB_ID", "TASK_DEFN_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      String iQueryString =
            "select TASK_DEFN_DB_ID, TASK_DEFN_ID from task_defn order by CREATION_DT desc";
      List<ArrayList<String>> llists = execute( iQueryString, lfields );
      return new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

   }


   /**
    * This method creates a task definition in task_defn.
    */
   private ArrayList<String> createTaskDefinition = new ArrayList<String>();
   {
      createTaskDefinition.add(
            "insert into TASK_DEFN (TASK_DEFN_DB_ID, TASK_DEFN_ID, LAST_REVISION_ORD, NEW_REVISION_BOOL) values (4650, TASK_DEFN_ID.nextval, 1, 1)" );
   };


   /**
    *
    * This method deletes the required data.
    *
    */
   private void deleteRequiredData() {
      String lStrDelete;
      // Delete task_task table
      for ( String aTaskCode : iDeleteRequirementCodes ) {
         lStrDelete = "delete from TASK_TASK where TASK_CD like '%" + aTaskCode + "%'";
         executeSQL( lStrDelete );
      }

      // Delete task_defn table
      for ( simpleIDs aSimpleID : iDeleteRequirementIDs ) {
         lStrDelete = "delete from TASK_DEFN where TASK_DEFN_DB_ID=" + aSimpleID.getNO_DB_ID()
               + " and TASK_DEFN_ID=" + aSimpleID.getNO_ID();
         executeSQL( lStrDelete );
      }
      iDeleteRequirementIDs.clear();
      iDeleteRequirementCodes.clear();

   }

}
