package com.mxi.mx.core.maint.plan.baselineloader.REQ.validations;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.core.maint.plan.baselineloader.REQ.ReqTests;
import com.mxi.mx.util.TableUtil;


/**
 * This test is to verify req_import.validate_req functionality of staging tables: c_req and
 * c_req_jic.
 *
 */
public class ReqJIC extends ReqTests {

   @Rule
   public TestName testName = new TestName();

   public String iBOM_ID_1 = null;

   private ArrayList<String> updateTables = new ArrayList<String>();
   {
      updateTables.add(
            "UPDATE TASK_TASK SET TASK_DEF_STATUS_CD = 'BUILD' WHERE TASK_CLASS_CD='JIC' and ASSMBL_CD='ACFT_CD1' and TASK_CD='SYS-JIC-1'" );

   };

   private ArrayList<String> restoreTables = new ArrayList<String>();
   {
      restoreTables.add(
            "UPDATE TASK_TASK SET TASK_DEF_STATUS_CD = 'ACTV' WHERE TASK_CLASS_CD='JIC' and ASSMBL_CD='ACFT_CD1' and TASK_CD='SYS-JIC-1'" );

   };


   /**
    * Clean up after each individual test
    *
    */
   @After
   @Override
   public void after() {

      if ( testName.getMethodName().contains( "12131" ) ) {
         restoreData12131();
      } else if ( testName.getMethodName().contains( "12129" ) ) {
         classDataSetup( restoreTables );
      }

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
      if ( testName.getMethodName().contains( "12131" ) ) {
         iBOM_ID_1 = DataSetup12131();
      } else if ( testName.getMethodName().contains( "12129" ) ) {
         classDataSetup( updateTables );
      }

   }


   /**
    * This test is to verify error code CFGREQ-12129: C_REQ_JIC.A JIC defined on a SYS slot cannot
    * be assigned to a REQ defined on a TRK/SUBASSY slot.
    *
    *
    */

   @Test
   public void test_CFGREQ_12129() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_1 + "'" );
      lReqMap.put( "REQ_TASK_REF_SDESC", "'" + iREQ_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "APPLICABILITY_DESC", "'" + iAPPLICABILITY_DESC + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "ENFORCE_WORKSCOPE_ORD_BOOL", "'" + iENFORCE_WORKSCOPE_ORD_BOOL + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_JIC
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );

      lReqMap.put( "JIC_ATA_CD", "'SYS-1-1'" );
      lReqMap.put( "JIC_TASK_CD", "'SYS-JIC-1'" );
      lReqMap.put( "REQ_JIC_ORDER", "1" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_JIC, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "CFGREQ-12129" );

   }


   /**
    * This test is to verify error code CFGREQ-12131: C_REQ_JIC.A JIC defined on a ROOT slot cannot
    * be assigned to a REQ defined on a TRK/SUBASSY slot.
    *
    *
    */

   @Test
   public void test_CFGREQ_12131() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_1 + "'" );
      lReqMap.put( "REQ_TASK_REF_SDESC", "'" + iREQ_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "APPLICABILITY_DESC", "'" + iAPPLICABILITY_DESC + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "ENFORCE_WORKSCOPE_ORD_BOOL", "'" + iENFORCE_WORKSCOPE_ORD_BOOL + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_JIC
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );

      lReqMap.put( "JIC_ATA_CD", "'ACFT_CD1'" );
      lReqMap.put( "JIC_TASK_CD", "'AT-JIC-1'" );
      lReqMap.put( "REQ_JIC_ORDER", "1" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_JIC, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "CFGREQ-12131" );

   }
   // ======================================================================================================


   /**
    * This function is to get task_bom_id
    *
    *
    */
   public String getBOMID( String ltaskCD, String ltaskName ) {
      String lQuery = "Select assmbl_bom_id from " + TableUtil.TASK_TASK + " where task_cd='"
            + ltaskCD + "' and task_name='" + ltaskName + "'";

      return getStringValueFromQuery( lQuery, "ASSMBL_BOM_ID" );
   }


   /**
    * This function is data setup for 12131
    *
    *
    */
   public String DataSetup12131() {

      String loriginalV = getBOMID( "AT-JIC-1", "AT JIC 1" );
      String lquery =
            "UPDATE TASK_TASK SET TASK_DEF_STATUS_CD = 'BUILD', ASSMBL_BOM_ID=0 WHERE TASK_CLASS_CD='JIC' and ASSMBL_CD='ACFT_CD1' and TASK_CD='AT-JIC-1'";
      runUpdate( lquery );
      return loriginalV;

   }


   /**
    * This function is restore data for 12131
    *
    *
    */
   public void restoreData12131() {
      String lquery =
            "UPDATE TASK_TASK SET TASK_DEF_STATUS_CD = 'ACTV', ASSMBL_BOM_ID='" + iBOM_ID_1
                  + "' WHERE TASK_CLASS_CD='JIC' and ASSMBL_CD='ACFT_CD1' and TASK_CD='AT-JIC-1'";

      runUpdate( lquery );

   }
}
