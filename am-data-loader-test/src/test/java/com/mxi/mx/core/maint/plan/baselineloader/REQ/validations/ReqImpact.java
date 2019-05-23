package com.mxi.mx.core.maint.plan.baselineloader.REQ.validations;

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
 * c_req_impact.
 *
 *
 */
public class ReqImpact extends ReqTests {

   @Rule
   public TestName testName = new TestName();


   /**
    * Clean up after each individual test
    *
    */
   @After
   @Override
   public void after() {
      // RestoreData();
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

   }


   /**
    * This test is to verify error code OPER-24704 : The combination of fields ASSMBL_CD /
    * REQ_ATA_CD / TASK_CD / IMPACT_CD must be unique in the staging table.
    *
    *
    */

   @Test
   public void testOPER_24704() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iENG_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_3 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_3 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_3 + "'" );
      lReqMap.put( "REQ_TASK_REF_SDESC", "'" + iREQ_TASK_REF_SDESC_3 + "'" );
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

      // C_REQ
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iENG_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_4 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_4 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_4 + "'" );
      lReqMap.put( "REQ_TASK_REF_SDESC", "'" + iREQ_TASK_REF_SDESC_4 + "'" );
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

      // C_REQ_IMPACT
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iENG_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_3 + "'" );
      lReqMap.put( "IMPACT_CD", "'AUTOTEST'" );
      lReqMap.put( "IMPACT_DESC", "'IMPACT1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_IMPACT, lReqMap ) );

      // C_REQ_IMPACT
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iENG_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_4 + "'" );
      lReqMap.put( "IMPACT_CD", "'AUTOTEST'" );
      lReqMap.put( "IMPACT_DESC", "'IMPACT1-2'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_IMPACT, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify error code CFGREQ-11010 : The combination of fields ASSMBL_CD /
    * REQ_ATA_CD / TASK_CD / IMPACT_CD must be unique in the staging table.
    *
    *
    */

   @Test
   public void test_CFGREQ_11010() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iENG_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_3 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_3 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_3 + "'" );
      lReqMap.put( "REQ_TASK_REF_SDESC", "'" + iREQ_TASK_REF_SDESC_3 + "'" );
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

      // C_REQ
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iENG_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_4 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_4 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_4 + "'" );
      lReqMap.put( "REQ_TASK_REF_SDESC", "'" + iREQ_TASK_REF_SDESC_4 + "'" );
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

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_IMPACT
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iENG_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_3 + "'" );
      lReqMap.put( "IMPACT_CD", "'AUTOTEST'" );
      lReqMap.put( "IMPACT_DESC", "'IMPACT1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_IMPACT, lReqMap ) );

      // C_REQ_IMPACT
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iENG_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_3 + "'" );
      lReqMap.put( "IMPACT_CD", "'AUTOTEST'" );
      lReqMap.put( "IMPACT_DESC", "'IMPACT1-2'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_IMPACT, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      // verify no error
      validateErrorCode( "CFGREQ-11010" );

   }

}
