package com.mxi.mx.core.maint.plan.baselineloader;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
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

import com.mxi.mx.core.maint.plan.datamodels.assmbleInfor;
import com.mxi.mx.core.maint.plan.datamodels.cREF;
import com.mxi.mx.core.maint.plan.datamodels.cRefDynamicDeadline;
import com.mxi.mx.core.maint.plan.datamodels.cRefTaskLink;
import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.RefDocAndRefDocComponent;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.ValidationAndImport;
import com.mxi.mx.util.WhereClause;


/**
 * This test suite contains test cases on validation and import functionality of
 * C_REF_DOCUMENT_IMPORT package.
 *
 * @author ALICIA QIAN
 */
public class RefDoc extends RefDocAndRefDocComponent {

   @Rule
   public TestName testName = new TestName();
   ValidationAndImport ivalidationandimport;

   @SuppressWarnings( "serial" )
   ArrayList<cREF> iCRefList = new ArrayList<cREF>() {

      {
         add( new cREF( "AATEST", "ATESTNAME", "ATESTDESC", "ACFT_CD1", "SYS-1-1", "AMP", "TEST3",
               "AWL" ) );
         add( new cREF( "BBTEST", "BTESTNAME", "BTESTDESC", "ACFT_CD1",
               "ACFT-SYS-1-1-TRK-BATCH-PARENT", "AMP", "TEST3", "AWL" ) );
      }
   };

   @SuppressWarnings( "serial" )
   ArrayList<cRefTaskLink> iRefTaskLink = new ArrayList<cRefTaskLink>() {

      {
         add( new cRefTaskLink( "AATEST", "ACFT_CD1", "SYS-1-1", "COMPLIES", "AL_TASK",
               "ACFT_CD1" ) );
         add( new cRefTaskLink( "BBTEST", "ACFT_CD1", "ACFT-SYS-1-1-TRK-BATCH-PARENT", "COMPLIES",
               "AL_TASK", "ACFT_CD1" ) );
      }
   };

   @SuppressWarnings( "serial" )
   ArrayList<cRefDynamicDeadline> iRefDynamicDeadline = new ArrayList<cRefDynamicDeadline>() {

      {
         add( new cRefDynamicDeadline( "AATEST", "ACFT_CD1", "SYS-1-1", "HOURS", null, "100" ) );
         add( new cRefDynamicDeadline( "BBTEST", "ACFT_CD1", "ACFT-SYS-1-1-TRK-BATCH-PARENT",
               "CYCLES", "20", null ) );
      }
   };


   /**
    * Clean up after each individual test
    *
    */
   @After
   @Override
   public void after() {
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

   }


   @Test
   public void testOPER15067RD1VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "REF_TASK_NAME", "'" + iREF_TASK_NAME_1 + "'" );
      lReqMap.put( "REF_TASK_DESC", "'" + iREF_TASK_DESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );

      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "RECEIVED_BY", "'" + iRECEIVED_BY + "'" );
      lReqMap.put( "ISSUED_BY_MANUFACT_CD", "'" + iISSUED_BY_MANUFACT_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_BOOL", "'" + iSCHED_FROM_MANUFACT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'" + iMANUAL_SCHEDULING_BOOL + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE", "'" + iMIN_FORECAST_RANGE + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF, lReqMap ) );

      // C_REF_TASK_LINK
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "LINK_TYPE_CD", "'" + iLINK_TYPE_CD + "'" );
      lReqMap.put( "LINKED_TASK_CD", "'" + iLINKED_TASK_CD + "'" );
      lReqMap.put( "LINKED_TASK_ATA_CD", "'" + iLINKED_TASK_ATA_CD + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_TASK_LINK, lReqMap ) );

      // C_REF_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_DYNAMIC_DEADLINE, lReqMap ) );

      setReceivedDate( "01/02/2017", iREF_TASK_DESC_1 );
      setIssueDate( "01/02/2017", iREF_TASK_DESC_1 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   @Test
   public void testOPER15067RD2VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "REF_TASK_NAME", "'" + iREF_TASK_NAME_1 + "'" );
      lReqMap.put( "REF_TASK_DESC", "'" + iREF_TASK_DESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );

      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "RECEIVED_BY", "'" + iRECEIVED_BY + "'" );
      lReqMap.put( "ISSUED_BY_MANUFACT_CD", "'" + iISSUED_BY_MANUFACT_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_BOOL", "'" + iSCHED_FROM_MANUFACT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'" + iMANUAL_SCHEDULING_BOOL + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE", "'" + iMIN_FORECAST_RANGE + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'F'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF, lReqMap ) );

      // C_REF_TASK_LINK
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "LINK_TYPE_CD", "'" + iLINK_TYPE_CD + "'" );
      lReqMap.put( "LINKED_TASK_CD", "'" + iLINKED_TASK_CD + "'" );
      lReqMap.put( "LINKED_TASK_ATA_CD", "'" + iLINKED_TASK_ATA_CD + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_TASK_LINK, lReqMap ) );

      // C_REF_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", "'" + iSCHED_INITIAL_QT + "'" );
      lReqMap.put( "SCHED_INTERVAL_QT", null );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_DYNAMIC_DEADLINE, lReqMap ) );

      setReceivedDate( "01/02/2017", iREF_TASK_DESC_1 );
      setIssueDate( "01/02/2017", iREF_TASK_DESC_1 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );
      // Bug
      validateErrorCode( "CFG_REF-10405" );

   }


   @Test
   public void testOPER15067RD2_2VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "REF_TASK_NAME", "'" + iREF_TASK_NAME_1 + "'" );
      lReqMap.put( "REF_TASK_DESC", "'" + iREF_TASK_DESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );

      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "RECEIVED_BY", "'" + iRECEIVED_BY + "'" );
      lReqMap.put( "ISSUED_BY_MANUFACT_CD", "'" + iISSUED_BY_MANUFACT_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_BOOL", "'" + iSCHED_FROM_MANUFACT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'" + iMANUAL_SCHEDULING_BOOL + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE", "'" + iMIN_FORECAST_RANGE + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'0'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF, lReqMap ) );

      // C_REF_TASK_LINK
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "LINK_TYPE_CD", "'" + iLINK_TYPE_CD + "'" );
      lReqMap.put( "LINKED_TASK_CD", "'" + iLINKED_TASK_CD + "'" );
      lReqMap.put( "LINKED_TASK_ATA_CD", "'" + iLINKED_TASK_ATA_CD + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_TASK_LINK, lReqMap ) );

      // C_REF_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", null );
      lReqMap.put( "SCHED_INTERVAL_QT", iSCHED_INTERVAL_QT );
      // lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_DYNAMIC_DEADLINE, lReqMap ) );

      setReceivedDate( "01/02/2017", iREF_TASK_DESC_1 );
      setIssueDate( "01/02/2017", iREF_TASK_DESC_1 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      validateErrorCode( "CFG_REF-12039" );

   }


   @Test
   public void testOPER15067RD3VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "REF_TASK_NAME", "'" + iREF_TASK_NAME_1 + "'" );
      lReqMap.put( "REF_TASK_DESC", "'" + iREF_TASK_DESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "RECEIVED_BY", "'" + iRECEIVED_BY + "'" );
      lReqMap.put( "ISSUED_BY_MANUFACT_CD", "'" + iISSUED_BY_MANUFACT_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_BOOL", "'" + iSCHED_FROM_MANUFACT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'" + iMANUAL_SCHEDULING_BOOL + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE", "'" + iMIN_FORECAST_RANGE + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "RESCHED_FROM_CD", "'" + iRESCHED_FROM_CD + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'Y'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF, lReqMap ) );

      // C_REF_TASK_LINK
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "LINK_TYPE_CD", "'" + iLINK_TYPE_CD + "'" );
      lReqMap.put( "LINKED_TASK_CD", "'" + iLINKED_TASK_CD + "'" );
      lReqMap.put( "LINKED_TASK_ATA_CD", "'" + iLINKED_TASK_ATA_CD + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_TASK_LINK, lReqMap ) );

      // // C_REF_DYNAMIC_DEADLINE
      // lReqMap.clear();
      // lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      // lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      // lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      // lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      // lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );
      //
      // // insert map
      // runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_DYNAMIC_DEADLINE, lReqMap ) );

      setReceivedDate( "01/02/2017", iREF_TASK_DESC_1 );
      setIssueDate( "01/02/2017", iREF_TASK_DESC_1 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   @Test
   public void testOPER15067RD4VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "REF_TASK_NAME", "'" + iREF_TASK_NAME_1 + "'" );
      lReqMap.put( "REF_TASK_DESC", "'" + iREF_TASK_DESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "RECEIVED_BY", "'" + iRECEIVED_BY + "'" );
      lReqMap.put( "ISSUED_BY_MANUFACT_CD", "'" + iISSUED_BY_MANUFACT_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_BOOL", "'" + iSCHED_FROM_MANUFACT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'" + iMANUAL_SCHEDULING_BOOL + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE", "'" + iMIN_FORECAST_RANGE + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "RESCHED_FROM_CD", "'" + iRESCHED_FROM_CD + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'T'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF, lReqMap ) );

      // C_REF_TASK_LINK
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "LINK_TYPE_CD", "'" + iLINK_TYPE_CD + "'" );
      lReqMap.put( "LINKED_TASK_CD", "'" + iLINKED_TASK_CD + "'" );
      lReqMap.put( "LINKED_TASK_ATA_CD", "'" + iLINKED_TASK_ATA_CD + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_TASK_LINK, lReqMap ) );

      // C_REF_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_DYNAMIC_DEADLINE, lReqMap ) );

      setReceivedDate( "01/02/2017", iREF_TASK_DESC_1 );
      setIssueDate( "01/02/2017", iREF_TASK_DESC_1 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      validateErrorCode( "CFG_REF-12015" );

   }


   @Test
   public void testOPER15067RD5VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "REF_TASK_NAME", "'" + iREF_TASK_NAME_1 + "'" );
      lReqMap.put( "REF_TASK_DESC", "'" + iREF_TASK_DESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "RECEIVED_BY", "'" + iRECEIVED_BY + "'" );
      lReqMap.put( "ISSUED_BY_MANUFACT_CD", "'" + iISSUED_BY_MANUFACT_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_BOOL", "'" + iSCHED_FROM_MANUFACT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'" + iMANUAL_SCHEDULING_BOOL + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE", "'" + iMIN_FORECAST_RANGE + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "RESCHED_FROM_CD", "'" + iRESCHED_FROM_CD + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF, lReqMap ) );

      // C_REF_TASK_LINK
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "LINK_TYPE_CD", "'" + iLINK_TYPE_CD + "'" );
      lReqMap.put( "LINKED_TASK_CD", "'" + iLINKED_TASK_CD + "'" );
      lReqMap.put( "LINKED_TASK_ATA_CD", "'" + iLINKED_TASK_ATA_CD + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_TASK_LINK, lReqMap ) );

      // C_REF_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", null );
      lReqMap.put( "SCHED_INTERVAL_QT", "'" + iSCHED_INTERVAL_QT + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", null );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_DYNAMIC_DEADLINE, lReqMap ) );

      setReceivedDate( "01/02/2017", iREF_TASK_DESC_1 );
      setIssueDate( "01/02/2017", iREF_TASK_DESC_1 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   @Test
   public void testOPER15067RD6VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "REF_TASK_NAME", "'" + iREF_TASK_NAME_1 + "'" );
      lReqMap.put( "REF_TASK_DESC", "'" + iREF_TASK_DESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "RECEIVED_BY", "'" + iRECEIVED_BY + "'" );
      lReqMap.put( "ISSUED_BY_MANUFACT_CD", "'" + iISSUED_BY_MANUFACT_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_BOOL", "'" + iSCHED_FROM_MANUFACT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'" + iMANUAL_SCHEDULING_BOOL + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE", "'" + iMIN_FORECAST_RANGE + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "RESCHED_FROM_CD", "'" + iRESCHED_FROM_CD + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'Y'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF, lReqMap ) );

      // C_REF_TASK_LINK
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "LINK_TYPE_CD", "'" + iLINK_TYPE_CD + "'" );
      lReqMap.put( "LINKED_TASK_CD", "'" + iLINKED_TASK_CD + "'" );
      lReqMap.put( "LINKED_TASK_ATA_CD", "'" + iLINKED_TASK_ATA_CD + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_TASK_LINK, lReqMap ) );

      // C_REF_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", "'" + iSCHED_INITIAL_QT + "'" );
      lReqMap.put( "SCHED_INTERVAL_QT", "'" + iSCHED_INTERVAL_QT + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", null );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_DYNAMIC_DEADLINE, lReqMap ) );

      setReceivedDate( "01/02/2017", iREF_TASK_DESC_1 );
      setIssueDate( "01/02/2017", iREF_TASK_DESC_1 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   @Test
   public void testOPER15067RD7VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "REF_TASK_NAME", "'" + iREF_TASK_NAME_1 + "'" );
      lReqMap.put( "REF_TASK_DESC", "'" + iREF_TASK_DESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "RECEIVED_BY", "'" + iRECEIVED_BY + "'" );
      lReqMap.put( "ISSUED_BY_MANUFACT_CD", "'" + iISSUED_BY_MANUFACT_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_BOOL", "'" + iSCHED_FROM_MANUFACT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'" + iMANUAL_SCHEDULING_BOOL + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE", "'" + iMIN_FORECAST_RANGE + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "RESCHED_FROM_CD", "'" + iRESCHED_FROM_CD + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'Y'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF, lReqMap ) );

      // C_REF_TASK_LINK
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "LINK_TYPE_CD", "'" + iLINK_TYPE_CD + "'" );
      lReqMap.put( "LINKED_TASK_CD", "'" + iLINKED_TASK_CD + "'" );
      lReqMap.put( "LINKED_TASK_ATA_CD", "'" + iLINKED_TASK_ATA_CD + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_TASK_LINK, lReqMap ) );

      // C_REF_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", "'" + iSCHED_INITIAL_QT + "'" );
      lReqMap.put( "SCHED_INTERVAL_QT", null );
      lReqMap.put( "SCHED_THRESHOLD_QT", null );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_DYNAMIC_DEADLINE, lReqMap ) );

      setReceivedDate( "01/02/2017", iREF_TASK_DESC_1 );
      setIssueDate( "01/02/2017", iREF_TASK_DESC_1 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // bug
      validateErrorCode( "CFG_REF-10405" );

   }


   /**
    * This test is to verify OPER-30663: Baseline Loader - Job Card using Class with RSTAT_CD !=0
    * does not get failed by validation
    *
    *
    */

   @Test
   public void testCFG_REF_12225_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "REF_TASK_NAME", "'" + iREF_TASK_NAME_1 + "'" );
      lReqMap.put( "REF_TASK_DESC", "'" + iREF_TASK_DESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'AUTOREF'" );
      lReqMap.put( "SUBCLASS_CD", "'TEST6'" );

      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "RECEIVED_BY", "'" + iRECEIVED_BY + "'" );
      lReqMap.put( "ISSUED_BY_MANUFACT_CD", "'" + iISSUED_BY_MANUFACT_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_BOOL", "'" + iSCHED_FROM_MANUFACT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'" + iMANUAL_SCHEDULING_BOOL + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE", "'" + iMIN_FORECAST_RANGE + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "RECEIVED_DT", "SYSDATE" );
      lReqMap.put( "ISSUE_DT", "SYSDATE" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF, lReqMap ) );

      // C_REF_TASK_LINK
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "LINK_TYPE_CD", "'" + iLINK_TYPE_CD + "'" );
      lReqMap.put( "LINKED_TASK_CD", "'" + iLINKED_TASK_CD + "'" );
      lReqMap.put( "LINKED_TASK_ATA_CD", "'" + iLINKED_TASK_ATA_CD + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_TASK_LINK, lReqMap ) );

      // C_REF_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_DYNAMIC_DEADLINE, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      validateErrorCode( "CFG_REF-12225" );

   }


   /**
    * This test is to verify validation functionality on C_REF_DOCUMENT_IMPORT of non-recurring ref
    * on TRK config slot
    *
    */

   @Test
   public void testNON_RECURRING_TRK_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "REF_TASK_NAME", "'" + iREF_TASK_NAME_1 + "'" );
      lReqMap.put( "REF_TASK_DESC", "'" + iREF_TASK_DESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );

      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "RECEIVED_BY", "'" + iRECEIVED_BY + "'" );
      lReqMap.put( "ISSUED_BY_MANUFACT_CD", "'" + iISSUED_BY_MANUFACT_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_BOOL", "'" + iSCHED_FROM_MANUFACT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'" + iMANUAL_SCHEDULING_BOOL + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE", "'" + iMIN_FORECAST_RANGE + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "RECEIVED_DT", "SYSDATE" );
      lReqMap.put( "ISSUE_DT", "SYSDATE" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF, lReqMap ) );

      // C_REF_TASK_LINK
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "LINK_TYPE_CD", "'" + iLINK_TYPE_CD + "'" );
      lReqMap.put( "LINKED_TASK_CD", "'" + iLINKED_TASK_CD + "'" );
      lReqMap.put( "LINKED_TASK_ATA_CD", "'" + iLINKED_TASK_ATA_CD + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_TASK_LINK, lReqMap ) );

      // C_REF_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_DYNAMIC_DEADLINE, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify import functionality on C_REF_DOCUMENT_IMPORT of non-recurring ref on
    * TRK config slot
    *
    */
   @Test
   public void testNON_RECURRING_TRK_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testNON_RECURRING_TRK_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;
      iTASK_IDs_2 = null;
      iTASK_DEFN_IDs_2 = null;

      // Verify task_task table
      iTASK_IDs = getTaskIds( iREF_TASK_CD_1, iREF_TASK_NAME_1 );
      assmbleInfor lassmblinfor = getassmblInfor( iATA_CD_TRK );

      iTASK_DEFN_IDs = verifyTaskTask( iTASK_IDs, iCLASS_CD, lassmblinfor, iSUBCLASS_CD,
            iORIGINATOR_CD, iREF_TASK_CD_1, iREF_TASK_NAME_1, iREF_TASK_DESC_1, null,
            iSCHED_TO_LATEST_DEADLINE_BOOL_Y_number, iSOFT_DEADLINE_BOOL_Y_number,
            iMUST_BE_REMOVED_CD, iON_CONDITION_BOOL_N_number, "0" );

      // verify task_DEFN
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_task_flag
      lQuery = "select 1 from " + TableUtil.TASK_TASK_FLAGS + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
      Assert.assertTrue( "Check task_task_flags table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_ref_doc table
      // get HR ids
      simpleIDs lHRIDs = getHRIds( iRECEIVED_BY );
      // get receive date and issue date
      lQuery = "SELECT SYSDATE as mydate FROM DUAL";
      java.sql.Date lRCVdate = getDateValueFromQuery( lQuery, "mydate" );
      java.sql.Date lIssuedate = getDateValueFromQuery( lQuery, "mydate" );
      verifyTASKREFDOC( iTASK_IDs, lHRIDs, iISSUED_BY_MANUFACT_CD, "MANUFACT", lRCVdate,
            lIssuedate );

      // verify task_task_dep
      simpleIDs iLink_TASK = getTaskIds( iLINKED_TASK_CD, iLINKED_TASK_NAME );
      verifyTASKTASkDEP( iLink_TASK, iTASK_DEFN_IDs, iLINK_TYPE_CD );

      // verify task_work_type
      verifyWorkType( iTASK_IDs, iWORK_TYPE_LIST_1 );
      verifyWorkType( iTASK_IDs, iWORK_TYPE_LIST_2 );

      // verify task_sched_rule table
      simpleIDs lTypeIds = getTypeIds( iSCHED_DATA_TYPE_CD );
      verifyTASKSCHEDRULE( iTASK_IDs, lTypeIds, iSCHED_THRESHOLD_QT, null );

   }


   /**
    * This test is to verify validation functionality on C_REF_DOCUMENT_IMPORT of non-recurring ref
    * on SYS config slot
    *
    */

   @Test
   public void testNON_RECURRING_SYS_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_SYS + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "REF_TASK_NAME", "'" + iREF_TASK_NAME_1 + "'" );
      lReqMap.put( "REF_TASK_DESC", "'" + iREF_TASK_DESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );

      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "RECEIVED_BY", "'" + iRECEIVED_BY + "'" );
      lReqMap.put( "ISSUED_BY_MANUFACT_CD", "'" + iISSUED_BY_MANUFACT_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_BOOL", "'" + iSCHED_FROM_MANUFACT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'" + iMANUAL_SCHEDULING_BOOL + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE", "'" + iMIN_FORECAST_RANGE + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "RECEIVED_DT", "SYSDATE" );
      lReqMap.put( "ISSUE_DT", "SYSDATE" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF, lReqMap ) );

      // C_REF_TASK_LINK
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_SYS + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "LINK_TYPE_CD", "'" + iLINK_TYPE_CD + "'" );
      lReqMap.put( "LINKED_TASK_CD", "'" + iLINKED_TASK_CD + "'" );
      lReqMap.put( "LINKED_TASK_ATA_CD", "'" + iLINKED_TASK_ATA_CD + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_TASK_LINK, lReqMap ) );

      // C_REF_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_SYS + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD_HOURS + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_DYNAMIC_DEADLINE, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify import functionality on C_REF_DOCUMENT_IMPORT of non-recurring ref on
    * SYS config slot
    *
    */

   @Test
   public void testNON_RECURRING_SYS_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testNON_RECURRING_SYS_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;
      iTASK_IDs_2 = null;
      iTASK_DEFN_IDs_2 = null;

      // Verify task_task table
      iTASK_IDs = getTaskIds( iREF_TASK_CD_1, iREF_TASK_NAME_1 );
      assmbleInfor lassmblinfor = getassmblInfor( iATA_CD_SYS );

      iTASK_DEFN_IDs = verifyTaskTask( iTASK_IDs, iCLASS_CD, lassmblinfor, iSUBCLASS_CD,
            iORIGINATOR_CD, iREF_TASK_CD_1, iREF_TASK_NAME_1, iREF_TASK_DESC_1, null,
            iSCHED_TO_LATEST_DEADLINE_BOOL_Y_number, iSOFT_DEADLINE_BOOL_Y_number,
            iMUST_BE_REMOVED_CD, iON_CONDITION_BOOL_N_number, "0" );

      // verify task_DEFN
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_task_flag
      lQuery = "select 1 from " + TableUtil.TASK_TASK_FLAGS + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
      Assert.assertTrue( "Check task_task_flags table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_ref_doc table
      // get HR ids
      simpleIDs lHRIDs = getHRIds( iRECEIVED_BY );
      // get receive date and issue date
      lQuery = "SELECT SYSDATE as mydate FROM DUAL";
      java.sql.Date lRCVdate = getDateValueFromQuery( lQuery, "mydate" );
      java.sql.Date lIssuedate = getDateValueFromQuery( lQuery, "mydate" );
      verifyTASKREFDOC( iTASK_IDs, lHRIDs, iISSUED_BY_MANUFACT_CD, "MANUFACT", lRCVdate,
            lIssuedate );

      // verify task_task_dep
      simpleIDs iLink_TASK = getTaskIds( iLINKED_TASK_CD, iLINKED_TASK_NAME );
      verifyTASKTASkDEP( iLink_TASK, iTASK_DEFN_IDs, iLINK_TYPE_CD );

      // verify task_work_type
      verifyWorkType( iTASK_IDs, iWORK_TYPE_LIST_1 );
      verifyWorkType( iTASK_IDs, iWORK_TYPE_LIST_2 );

      // verify task_sched_rule table
      simpleIDs lTypeIds = getTypeIds( iSCHED_DATA_TYPE_CD_HOURS );
      verifyTASKSCHEDRULE( iTASK_IDs, lTypeIds, iSCHED_THRESHOLD_QT, null );

   }


   /**
    * This test is to verify validation functionality on C_REF_DOCUMENT_IMPORT of non-recurring ref
    * on ACFT config slot
    *
    */

   @Test
   public void testNON_RECURRING_ACFT_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "REF_TASK_NAME", "'" + iREF_TASK_NAME_1 + "'" );
      lReqMap.put( "REF_TASK_DESC", "'" + iREF_TASK_DESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );

      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "RECEIVED_BY", "'" + iRECEIVED_BY + "'" );
      lReqMap.put( "ISSUED_BY_MANUFACT_CD", "'" + iISSUED_BY_MANUFACT_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_BOOL", "'" + iSCHED_FROM_MANUFACT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'" + iMANUAL_SCHEDULING_BOOL + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE", "'" + iMIN_FORECAST_RANGE + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "RECEIVED_DT", "SYSDATE" );
      lReqMap.put( "ISSUE_DT", "SYSDATE" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF, lReqMap ) );

      // C_REF_TASK_LINK
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "LINK_TYPE_CD", "'" + iLINK_TYPE_CD + "'" );
      lReqMap.put( "LINKED_TASK_CD", "'" + iLINKED_TASK_CD + "'" );
      lReqMap.put( "LINKED_TASK_ATA_CD", "'" + iLINKED_TASK_ATA_CD + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_TASK_LINK, lReqMap ) );

      // C_REF_STANDARD_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_HRS_THRESHOLD", "'" + iSCHED_HRS_THRESHOLD + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_STANDARD_DEADLINE, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify import functionality on C_REF_DOCUMENT_IMPORT of non-recurring ref on
    * ACFT config slot
    *
    */

   @Test
   public void testNON_RECURRING_ACFT_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testNON_RECURRING_ACFT_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;
      iTASK_IDs_2 = null;
      iTASK_DEFN_IDs_2 = null;

      // Verify task_task table
      iTASK_IDs = getTaskIds( iREF_TASK_CD_1, iREF_TASK_NAME_1 );
      assmbleInfor lassmblinfor = getassmblInfor( iACFT_ASSMBLCD );

      iTASK_DEFN_IDs = verifyTaskTask( iTASK_IDs, iCLASS_CD, lassmblinfor, iSUBCLASS_CD,
            iORIGINATOR_CD, iREF_TASK_CD_1, iREF_TASK_NAME_1, iREF_TASK_DESC_1, null,
            iSCHED_TO_LATEST_DEADLINE_BOOL_Y_number, iSOFT_DEADLINE_BOOL_Y_number,
            iMUST_BE_REMOVED_CD, iON_CONDITION_BOOL_N_number, "0" );

      // verify task_DEFN
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_task_flag
      lQuery = "select 1 from " + TableUtil.TASK_TASK_FLAGS + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
      Assert.assertTrue( "Check task_task_flags table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_ref_doc table
      // get HR ids
      simpleIDs lHRIDs = getHRIds( iRECEIVED_BY );
      // get receive date and issue date
      lQuery = "SELECT SYSDATE as mydate FROM DUAL";
      java.sql.Date lRCVdate = getDateValueFromQuery( lQuery, "mydate" );
      java.sql.Date lIssuedate = getDateValueFromQuery( lQuery, "mydate" );
      verifyTASKREFDOC( iTASK_IDs, lHRIDs, iISSUED_BY_MANUFACT_CD, "MANUFACT", lRCVdate,
            lIssuedate );

      // verify task_task_dep
      simpleIDs iLink_TASK = getTaskIds( iLINKED_TASK_CD, iLINKED_TASK_NAME );
      verifyTASKTASkDEP( iLink_TASK, iTASK_DEFN_IDs, iLINK_TYPE_CD );

      // verify task_work_type
      verifyWorkType( iTASK_IDs, iWORK_TYPE_LIST_1 );
      verifyWorkType( iTASK_IDs, iWORK_TYPE_LIST_2 );

      // verify task_sched_rule table
      simpleIDs lTypeIds = getTypeIds( iSCHED_DATA_TYPE_CD_HOURS );
      verifyTASKSCHEDRULE( iTASK_IDs, lTypeIds, iSCHED_HRS_THRESHOLD, null );

   }


   /**
    * This test is to verify validation functionality on C_REF_DOCUMENT_IMPORT of non-recurring ref
    * on SUBASSY config slot
    *
    */

   @Test
   public void testNON_RECURRING_SUBASSY_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_APU + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "REF_TASK_NAME", "'" + iREF_TASK_NAME_1 + "'" );
      lReqMap.put( "REF_TASK_DESC", "'" + iREF_TASK_DESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );

      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "RECEIVED_BY", "'" + iRECEIVED_BY + "'" );
      lReqMap.put( "ISSUED_BY_MANUFACT_CD", "'" + iISSUED_BY_MANUFACT_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_BOOL", "'" + iSCHED_FROM_MANUFACT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'" + iMANUAL_SCHEDULING_BOOL + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE", "'" + iMIN_FORECAST_RANGE + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "RECEIVED_DT", "SYSDATE" );
      lReqMap.put( "ISSUE_DT", "SYSDATE" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF, lReqMap ) );

      // C_REF_TASK_LINK
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_APU + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "LINK_TYPE_CD", "'" + iLINK_TYPE_CD + "'" );
      lReqMap.put( "LINKED_TASK_CD", "'" + iLINKED_TASK_CD + "'" );
      lReqMap.put( "LINKED_TASK_ATA_CD", "'" + iLINKED_TASK_ATA_CD + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_TASK_LINK, lReqMap ) );

      // C_REF_STANDARD_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_APU + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_HRS_THRESHOLD", "'" + iSCHED_HRS_THRESHOLD + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_STANDARD_DEADLINE, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify import functionality on C_REF_DOCUMENT_IMPORT of non-recurring ref on
    * SUBASSY config slot
    *
    */

   @Test
   public void testNON_RECURRING_SUBASSY_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testNON_RECURRING_SUBASSY_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;
      iTASK_IDs_2 = null;
      iTASK_DEFN_IDs_2 = null;

      // Verify task_task table
      iTASK_IDs = getTaskIds( iREF_TASK_CD_1, iREF_TASK_NAME_1 );
      assmbleInfor lassmblinfor = getassmblInfor( iATA_CD_APU );

      iTASK_DEFN_IDs = verifyTaskTask( iTASK_IDs, iCLASS_CD, lassmblinfor, iSUBCLASS_CD,
            iORIGINATOR_CD, iREF_TASK_CD_1, iREF_TASK_NAME_1, iREF_TASK_DESC_1, null,
            iSCHED_TO_LATEST_DEADLINE_BOOL_Y_number, iSOFT_DEADLINE_BOOL_Y_number,
            iMUST_BE_REMOVED_CD, iON_CONDITION_BOOL_N_number, "0" );

      // verify task_DEFN
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_task_flag
      lQuery = "select 1 from " + TableUtil.TASK_TASK_FLAGS + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
      Assert.assertTrue( "Check task_task_flags table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_ref_doc table
      // get HR ids
      simpleIDs lHRIDs = getHRIds( iRECEIVED_BY );
      // get receive date and issue date
      lQuery = "SELECT SYSDATE as mydate FROM DUAL";
      java.sql.Date lRCVdate = getDateValueFromQuery( lQuery, "mydate" );
      java.sql.Date lIssuedate = getDateValueFromQuery( lQuery, "mydate" );
      verifyTASKREFDOC( iTASK_IDs, lHRIDs, iISSUED_BY_MANUFACT_CD, "MANUFACT", lRCVdate,
            lIssuedate );

      // verify task_task_dep
      simpleIDs iLink_TASK = getTaskIds( iLINKED_TASK_CD, iLINKED_TASK_NAME );
      verifyTASKTASkDEP( iLink_TASK, iTASK_DEFN_IDs, iLINK_TYPE_CD );

      // verify task_work_type
      verifyWorkType( iTASK_IDs, iWORK_TYPE_LIST_1 );
      verifyWorkType( iTASK_IDs, iWORK_TYPE_LIST_2 );

      // verify task_sched_rule table
      simpleIDs lTypeIds = getTypeIds( iSCHED_DATA_TYPE_CD_HOURS );
      verifyTASKSCHEDRULE( iTASK_IDs, lTypeIds, iSCHED_HRS_THRESHOLD, null );

   }


   /**
    * This test is to verify validation functionality on C_REF_DOCUMENT_IMPORT of non-recurring ref
    * on ENG_SYS config slot
    *
    */

   @Test
   public void testNON_RECURRING_ENG_SYS_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_ENG_SYS + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "REF_TASK_NAME", "'" + iREF_TASK_NAME_1 + "'" );
      lReqMap.put( "REF_TASK_DESC", "'" + iREF_TASK_DESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );

      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "RECEIVED_BY", "'" + iRECEIVED_BY + "'" );
      lReqMap.put( "ISSUED_BY_MANUFACT_CD", "'" + iISSUED_BY_MANUFACT_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_BOOL", "'" + iSCHED_FROM_MANUFACT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'" + iMANUAL_SCHEDULING_BOOL + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE", "'" + iMIN_FORECAST_RANGE + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "RECEIVED_DT", "SYSDATE" );
      lReqMap.put( "ISSUE_DT", "SYSDATE" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF, lReqMap ) );

      // C_REF_TASK_LINK
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_ENG_SYS + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "LINK_TYPE_CD", "'" + iLINK_TYPE_CD + "'" );
      lReqMap.put( "LINKED_TASK_CD", "'" + iLINKED_TASK_CD_ENG + "'" );
      lReqMap.put( "LINKED_TASK_ATA_CD", "'" + iLINKED_TASK_ATA_CD_ENG + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_TASK_LINK, lReqMap ) );

      // C_REF_STANDARD_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_ENG_SYS + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_EOT_THRESHOLD", "'" + iSCHED_EOT_THRESHOLD + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_STANDARD_DEADLINE, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify import functionality on C_REF_DOCUMENT_IMPORT of non-recurring ref on
    * ENG_SYS config slot
    *
    */

   @Test
   public void testNON_RECURRING_ENG_SYS_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testNON_RECURRING_ENG_SYS_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;
      iTASK_IDs_2 = null;
      iTASK_DEFN_IDs_2 = null;

      // Verify task_task table
      iTASK_IDs = getTaskIds( iREF_TASK_CD_1, iREF_TASK_NAME_1 );
      assmbleInfor lassmblinfor = getassmblInfor( iATA_CD_ENG_SYS );

      iTASK_DEFN_IDs = verifyTaskTask( iTASK_IDs, iCLASS_CD, lassmblinfor, iSUBCLASS_CD,
            iORIGINATOR_CD, iREF_TASK_CD_1, iREF_TASK_NAME_1, iREF_TASK_DESC_1, null,
            iSCHED_TO_LATEST_DEADLINE_BOOL_Y_number, iSOFT_DEADLINE_BOOL_Y_number,
            iMUST_BE_REMOVED_CD, iON_CONDITION_BOOL_N_number, "0" );

      // verify task_DEFN
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_task_flag
      lQuery = "select 1 from " + TableUtil.TASK_TASK_FLAGS + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
      Assert.assertTrue( "Check task_task_flags table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_ref_doc table
      // get HR ids
      simpleIDs lHRIDs = getHRIds( iRECEIVED_BY );
      // get receive date and issue date
      lQuery = "SELECT SYSDATE as mydate FROM DUAL";
      java.sql.Date lRCVdate = getDateValueFromQuery( lQuery, "mydate" );
      java.sql.Date lIssuedate = getDateValueFromQuery( lQuery, "mydate" );
      verifyTASKREFDOC( iTASK_IDs, lHRIDs, iISSUED_BY_MANUFACT_CD, "MANUFACT", lRCVdate,
            lIssuedate );

      // verify task_task_dep
      simpleIDs iLink_TASK = getTaskIds( iLINKED_TASK_CD_ENG, iLINKED_TASK_NAME_ENG );
      verifyTASKTASkDEP( iLink_TASK, iTASK_DEFN_IDs, iLINK_TYPE_CD );

      // verify task_work_type
      verifyWorkType( iTASK_IDs, iWORK_TYPE_LIST_1 );
      verifyWorkType( iTASK_IDs, iWORK_TYPE_LIST_2 );

      // verify task_sched_rule table
      simpleIDs lTypeIds = getTypeIds( iSCHED_DATA_TYPE_CD_EOT );
      verifyTASKSCHEDRULE( iTASK_IDs, lTypeIds, iSCHED_EOT_THRESHOLD, null );

   }


   /**
    * This test is to verify validation functionality on C_REF_DOCUMENT_IMPORT of recurring ref on
    * ACFT config slot
    *
    */

   @Test
   public void test_RECURRING_ACFT_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_2 + "'" );
      lReqMap.put( "REF_TASK_NAME", "'" + iREF_TASK_NAME_2 + "'" );
      lReqMap.put( "REF_TASK_DESC", "'" + iREF_TASK_DESC_2 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );

      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "RECEIVED_BY", "'" + iRECEIVED_BY + "'" );
      lReqMap.put( "ISSUED_BY_MANUFACT_CD", "'" + iISSUED_BY_MANUFACT_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_BOOL", "'" + iSCHED_FROM_MANUFACT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'" + iMANUAL_SCHEDULING_BOOL + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE", "'" + iMIN_FORECAST_RANGE + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "RESCHED_FROM_CD", "'" + iRESCHED_FROM_CD + "'" );
      lReqMap.put( "RECEIVED_DT", "SYSDATE" );
      lReqMap.put( "ISSUE_DT", "SYSDATE" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'Y'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF, lReqMap ) );

      // C_REF_TASK_LINK
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_2 + "'" );
      lReqMap.put( "LINK_TYPE_CD", "'" + iLINK_TYPE_CD + "'" );
      lReqMap.put( "LINKED_TASK_CD", "'" + iLINKED_TASK_CD + "'" );
      lReqMap.put( "LINKED_TASK_ATA_CD", "'" + iLINKED_TASK_ATA_CD + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_TASK_LINK, lReqMap ) );

      // C_REF_STANDARD_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_2 + "'" );
      lReqMap.put( "SCHED_HRS_INTERVAL", "'" + iSCHED_INTERVAL_QT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_STANDARD_DEADLINE, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify import functionality on C_REF_DOCUMENT_IMPORT of recurring ref on ACFT
    * config slot
    *
    */
   @Test
   public void test_RECURRING_ACFT_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      test_RECURRING_ACFT_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;
      iTASK_IDs_2 = null;
      iTASK_DEFN_IDs_2 = null;

      // Verify task_task table
      iTASK_IDs = getTaskIds( iREF_TASK_CD_2, iREF_TASK_NAME_2 );
      assmbleInfor lassmblinfor = getassmblInfor( iACFT_ASSMBLCD );

      iTASK_DEFN_IDs = verifyTaskTask( iTASK_IDs, iCLASS_CD, lassmblinfor, iSUBCLASS_CD,
            iORIGINATOR_CD, iREF_TASK_CD_2, iREF_TASK_NAME_2, iREF_TASK_DESC_2, null,
            iSCHED_TO_LATEST_DEADLINE_BOOL_Y_number, iSOFT_DEADLINE_BOOL_Y_number,
            iMUST_BE_REMOVED_CD, iON_CONDITION_BOOL_N_number, "1" );

      // verify task_DEFN
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_task_flag
      lQuery = "select 1 from " + TableUtil.TASK_TASK_FLAGS + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
      Assert.assertTrue( "Check task_task_flags table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_ref_doc table
      // get HR ids
      simpleIDs lHRIDs = getHRIds( iRECEIVED_BY );
      // get receive date and issue date
      lQuery = "SELECT SYSDATE as mydate FROM DUAL";
      java.sql.Date lRCVdate = getDateValueFromQuery( lQuery, "mydate" );
      java.sql.Date lIssuedate = getDateValueFromQuery( lQuery, "mydate" );
      verifyTASKREFDOC( iTASK_IDs, lHRIDs, iISSUED_BY_MANUFACT_CD, "MANUFACT", lRCVdate,
            lIssuedate );

      // verify task_task_dep
      simpleIDs iLink_TASK = getTaskIds( iLINKED_TASK_CD, iLINKED_TASK_NAME );
      verifyTASKTASkDEP( iLink_TASK, iTASK_DEFN_IDs, iLINK_TYPE_CD );

      // verify task_work_type
      verifyWorkType( iTASK_IDs, iWORK_TYPE_LIST_1 );
      verifyWorkType( iTASK_IDs, iWORK_TYPE_LIST_2 );

      // verify task_sched_rule table
      simpleIDs lTypeIds = getTypeIds( iSCHED_DATA_TYPE_CD_HOURS );
      verifyTASKSCHEDRULE( iTASK_IDs, lTypeIds, iSCHED_INTERVAL_QT, null );

   }


   /**
    * This test is to verify validation functionality on C_REF_DOCUMENT_IMPORT of recurring ref on
    * TRK config slot
    *
    */

   @Test
   public void test_RECURRING_TRK_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_2 + "'" );
      lReqMap.put( "REF_TASK_NAME", "'" + iREF_TASK_NAME_2 + "'" );
      lReqMap.put( "REF_TASK_DESC", "'" + iREF_TASK_DESC_2 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );

      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "RECEIVED_BY", "'" + iRECEIVED_BY + "'" );
      lReqMap.put( "ISSUED_BY_MANUFACT_CD", "'" + iISSUED_BY_MANUFACT_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_BOOL", "'" + iSCHED_FROM_MANUFACT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'" + iMANUAL_SCHEDULING_BOOL + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE", "'" + iMIN_FORECAST_RANGE + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "RESCHED_FROM_CD", "'" + iRESCHED_FROM_CD + "'" );
      lReqMap.put( "RECEIVED_DT", "SYSDATE" );
      lReqMap.put( "ISSUE_DT", "SYSDATE" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF, lReqMap ) );

      // C_REF_TASK_LINK
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_2 + "'" );
      lReqMap.put( "LINK_TYPE_CD", "'" + iLINK_TYPE_CD + "'" );
      lReqMap.put( "LINKED_TASK_CD", "'" + iLINKED_TASK_CD + "'" );
      lReqMap.put( "LINKED_TASK_ATA_CD", "'" + iLINKED_TASK_ATA_CD + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_TASK_LINK, lReqMap ) );

      // C_REF_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_2 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INTERVAL_QT", "'" + iSCHED_INTERVAL_QT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_DYNAMIC_DEADLINE, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify import functionality on C_REF_DOCUMENT_IMPORT of recurring ref on TRK
    * config slot
    *
    */
   @Test
   public void test_RECURRING_TRK_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      test_RECURRING_TRK_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;
      iTASK_IDs_2 = null;
      iTASK_DEFN_IDs_2 = null;

      // Verify task_task table
      iTASK_IDs = getTaskIds( iREF_TASK_CD_2, iREF_TASK_NAME_2 );
      assmbleInfor lassmblinfor = getassmblInfor( iATA_CD_TRK );

      iTASK_DEFN_IDs = verifyTaskTask( iTASK_IDs, iCLASS_CD, lassmblinfor, iSUBCLASS_CD,
            iORIGINATOR_CD, iREF_TASK_CD_2, iREF_TASK_NAME_2, iREF_TASK_DESC_2, null,
            iSCHED_TO_LATEST_DEADLINE_BOOL_Y_number, iSOFT_DEADLINE_BOOL_Y_number,
            iMUST_BE_REMOVED_CD, iON_CONDITION_BOOL_N_number, "1" );

      // verify task_DEFN
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_task_flag
      lQuery = "select 1 from " + TableUtil.TASK_TASK_FLAGS + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
      Assert.assertTrue( "Check task_task_flags table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_ref_doc table
      // get HR ids
      simpleIDs lHRIDs = getHRIds( iRECEIVED_BY );
      // get receive date and issue date
      lQuery = "SELECT SYSDATE as mydate FROM DUAL";
      java.sql.Date lRCVdate = getDateValueFromQuery( lQuery, "mydate" );
      java.sql.Date lIssuedate = getDateValueFromQuery( lQuery, "mydate" );
      verifyTASKREFDOC( iTASK_IDs, lHRIDs, iISSUED_BY_MANUFACT_CD, "MANUFACT", lRCVdate,
            lIssuedate );

      // verify task_task_dep
      simpleIDs iLink_TASK = getTaskIds( iLINKED_TASK_CD, iLINKED_TASK_NAME );
      verifyTASKTASkDEP( iLink_TASK, iTASK_DEFN_IDs, iLINK_TYPE_CD );

      // verify task_work_type
      verifyWorkType( iTASK_IDs, iWORK_TYPE_LIST_1 );
      verifyWorkType( iTASK_IDs, iWORK_TYPE_LIST_2 );

      // verify task_sched_rule table
      simpleIDs lTypeIds = getTypeIds( iSCHED_DATA_TYPE_CD );
      verifyTASKSCHEDRULE( iTASK_IDs, lTypeIds, iSCHED_INTERVAL_QT, null );

   }


   /**
    * This test is to verify validation functionality on C_REF_DOCUMENT_IMPORT on multiple records
    * on TRK and SYS config slots with recurring and non-recurring respectively.
    *
    */

   @Test
   public void test_Multiple_RECORDS_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      test_RECURRING_TRK_VALIDATION();
      testNON_RECURRING_SYS_VALIDATION();
      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify import functionality on C_REF_DOCUMENT_IMPORT on multiple records on
    * TRK and SYS config slots with recurring and non-recurring respectively.
    *
    */

   @Test
   public void test_Multiple_RECORDS_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      test_Multiple_RECORDS_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;
      iTASK_IDs_2 = null;
      iTASK_DEFN_IDs_2 = null;

      // ===Verify TRK
      // Verify task_task table
      iTASK_IDs = getTaskIds( iREF_TASK_CD_2, iREF_TASK_NAME_2 );
      assmbleInfor lassmblinfor = getassmblInfor( iATA_CD_TRK );

      iTASK_DEFN_IDs = verifyTaskTask( iTASK_IDs, iCLASS_CD, lassmblinfor, iSUBCLASS_CD,
            iORIGINATOR_CD, iREF_TASK_CD_2, iREF_TASK_NAME_2, iREF_TASK_DESC_2, null,
            iSCHED_TO_LATEST_DEADLINE_BOOL_Y_number, iSOFT_DEADLINE_BOOL_Y_number,
            iMUST_BE_REMOVED_CD, iON_CONDITION_BOOL_N_number, "1" );

      // verify task_DEFN
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_task_flag
      lQuery = "select 1 from " + TableUtil.TASK_TASK_FLAGS + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
      Assert.assertTrue( "Check task_task_flags table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_ref_doc table
      // get HR ids
      simpleIDs lHRIDs = getHRIds( iRECEIVED_BY );
      // get receive date and issue date
      lQuery = "SELECT SYSDATE as mydate FROM DUAL";
      java.sql.Date lRCVdate = getDateValueFromQuery( lQuery, "mydate" );
      java.sql.Date lIssuedate = getDateValueFromQuery( lQuery, "mydate" );
      verifyTASKREFDOC( iTASK_IDs, lHRIDs, iISSUED_BY_MANUFACT_CD, "MANUFACT", lRCVdate,
            lIssuedate );

      // verify task_task_dep
      simpleIDs iLink_TASK = getTaskIds( iLINKED_TASK_CD, iLINKED_TASK_NAME );
      verifyTASKTASkDEP( iLink_TASK, iTASK_DEFN_IDs, iLINK_TYPE_CD );

      // verify task_work_type
      verifyWorkType( iTASK_IDs, iWORK_TYPE_LIST_1 );
      verifyWorkType( iTASK_IDs, iWORK_TYPE_LIST_2 );

      // verify task_sched_rule table
      simpleIDs lTypeIds = getTypeIds( iSCHED_DATA_TYPE_CD );
      verifyTASKSCHEDRULE( iTASK_IDs, lTypeIds, iSCHED_INTERVAL_QT, null );

      // ==Verify sys
      // Verify task_task table
      iTASK_IDs_2 = getTaskIds( iREF_TASK_CD_1, iREF_TASK_NAME_1 );
      assmbleInfor lassmblinfor_2 = getassmblInfor( iATA_CD_SYS );

      iTASK_DEFN_IDs_2 = verifyTaskTask( iTASK_IDs_2, iCLASS_CD, lassmblinfor_2, iSUBCLASS_CD,
            iORIGINATOR_CD, iREF_TASK_CD_1, iREF_TASK_NAME_1, iREF_TASK_DESC_1, null,
            iSCHED_TO_LATEST_DEADLINE_BOOL_Y_number, iSOFT_DEADLINE_BOOL_Y_number,
            iMUST_BE_REMOVED_CD, iON_CONDITION_BOOL_N_number, "0" );

      // verify task_DEFN
      lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs_2.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs_2.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_task_flag
      lQuery = "select 1 from " + TableUtil.TASK_TASK_FLAGS + " where TASK_DB_ID="
            + iTASK_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_2.getNO_ID();
      Assert.assertTrue( "Check task_task_flags table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_ref_doc table
      // get HR ids
      simpleIDs lHRIDs_2 = getHRIds( iRECEIVED_BY );
      // get receive date and issue date
      lQuery = "SELECT SYSDATE as mydate FROM DUAL";
      java.sql.Date lRCVdate_2 = getDateValueFromQuery( lQuery, "mydate" );
      java.sql.Date lIssuedate_2 = getDateValueFromQuery( lQuery, "mydate" );
      verifyTASKREFDOC( iTASK_IDs_2, lHRIDs_2, iISSUED_BY_MANUFACT_CD, "MANUFACT", lRCVdate_2,
            lIssuedate_2 );

      // verify task_task_dep
      simpleIDs iLink_TASK_2 = getTaskIds( iLINKED_TASK_CD, iLINKED_TASK_NAME );
      verifyTASKTASkDEP( iLink_TASK_2, iTASK_DEFN_IDs_2, iLINK_TYPE_CD );

      // verify task_work_type
      verifyWorkType( iTASK_IDs_2, iWORK_TYPE_LIST_1 );
      verifyWorkType( iTASK_IDs_2, iWORK_TYPE_LIST_2 );

      // verify task_sched_rule table
      simpleIDs lTypeIds_2 = getTypeIds( iSCHED_DATA_TYPE_CD_HOURS );
      verifyTASKSCHEDRULE( iTASK_IDs_2, lTypeIds_2, iSCHED_THRESHOLD_QT, null );

   }


   /**
    * This test is to verify export functionality on C_REF_DOCUMENT_IMPORT on multiple records on
    * TRK and SYS config slots with recurring and non-recurring respectively.
    *
    * @throws SQLException
    *
    */

   @Test
   public void test_Export() throws SQLException {

      // Import data
      test_Multiple_RECORDS_IMPORT();

      // delete data in all staging tables
      deleteAllFromTable( TableUtil.C_REF );
      deleteAllFromTable( TableUtil.C_REF_TASK_LINK );
      deleteAllFromTable( TableUtil.C_REF_DYNAMIC_DEADLINE );

      // run export
      runExport();

      // Verify C_REF staging table
      ArrayList<cREF> lactualCREFList = getCRefs();
      checkArraysEqual( iCRefList, lactualCREFList );

      // Verify C_REF_TASK_LINK staging table
      ArrayList<cRefTaskLink> lactualCRefTaskLinkList = getCRefsTaskLink();
      checkArraysEqual( iRefTaskLink, lactualCRefTaskLinkList );

      // Verify C_REF_DYNAMIC_DEADLINE staging table
      ArrayList<cRefDynamicDeadline> lactualCRefDynDeadList = getCRefsDynDead();
      checkArraysEqual( iRefDynamicDeadline, lactualCRefDynDeadList );

   }


   /**
    * This test is to verify OPER-15284 fix would not break existing valid scenario. There are 2
    * tasks with same ata_cd and task_cd with different assmbl_cd. ref task would be generated based
    * on assmbl_cd of ref_task if assmbl_cd is valid for task link.
    *
    */

   @Test
   public void testOPER_15284_1_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "REF_TASK_NAME", "'" + iREF_TASK_NAME_1 + "'" );
      lReqMap.put( "REF_TASK_DESC", "'" + iREF_TASK_DESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );

      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "RECEIVED_BY", "'" + iRECEIVED_BY + "'" );
      lReqMap.put( "ISSUED_BY_MANUFACT_CD", "'" + iISSUED_BY_MANUFACT_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_BOOL", "'" + iSCHED_FROM_MANUFACT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'" + iMANUAL_SCHEDULING_BOOL + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE", "'" + iMIN_FORECAST_RANGE + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "RECEIVED_DT", "SYSDATE" );
      lReqMap.put( "ISSUE_DT", "SYSDATE" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF, lReqMap ) );

      // C_REF_TASK_LINK
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "LINK_TYPE_CD", "'" + iLINK_TYPE_CD + "'" );
      lReqMap.put( "LINKED_TASK_CD", "'" + iLINKED_TASK_CD_AT_TEST + "'" );
      lReqMap.put( "LINKED_TASK_ATA_CD", "'" + iLINKED_TASK_ATA_CD_AT_TEST + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_TASK_LINK, lReqMap ) );

      // C_REF_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_DYNAMIC_DEADLINE, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify OPER-15284 fix would not break existing valid scenario. There are 2
    * tasks with same ata_cd and task_cd with different assmbl_cd. ref task would not be generated
    * if tasks are not defined on assmbl_cd of task which is already in the MIX.CFG_REF-12208 will
    * be generated.
    *
    */

   @Test
   public void testOPER_15284_CFG_REF_12208_1_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'ACFT_T3'" );
      lReqMap.put( "ATA_CD", "'SYS-1'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "REF_TASK_NAME", "'" + iREF_TASK_NAME_1 + "'" );
      lReqMap.put( "REF_TASK_DESC", "'" + iREF_TASK_DESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );

      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "RECEIVED_BY", "'" + iRECEIVED_BY + "'" );
      lReqMap.put( "ISSUED_BY_MANUFACT_CD", "'" + iISSUED_BY_MANUFACT_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_BOOL", "'" + iSCHED_FROM_MANUFACT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'" + iMANUAL_SCHEDULING_BOOL + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE", "'" + iMIN_FORECAST_RANGE + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "RECEIVED_DT", "SYSDATE" );
      lReqMap.put( "ISSUE_DT", "SYSDATE" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF, lReqMap ) );

      // C_REF_TASK_LINK
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'ACFT_T3'" );
      lReqMap.put( "ATA_CD", "'SYS-1'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "LINK_TYPE_CD", "'" + iLINK_TYPE_CD + "'" );
      lReqMap.put( "LINKED_TASK_CD", "'" + iLINKED_TASK_CD_AT_TEST + "'" );
      lReqMap.put( "LINKED_TASK_ATA_CD", "'" + iLINKED_TASK_ATA_CD_AT_TEST + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_TASK_LINK, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkUsageErrorCode( testName.getMethodName(), "CFG_REF-12208" );

   }


   /**
    * This test is to verify OPER-15284 fix would not break existing valid scenario. There are 2
    * tasks with same ata_cd and task_cd with different assmbl_cd. ref task would not be generated
    * if tasks are not defined on assmbl_cd of task which is in staging table.CFG_REF-12204 will be
    * generated.
    *
    */

   @Test
   public void testOPER_15284_CFG_REF_12208_2_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REF
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'SYS-1'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "REF_TASK_NAME", "'" + iREF_TASK_NAME_1 + "'" );
      lReqMap.put( "REF_TASK_DESC", "'" + iREF_TASK_DESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );

      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "RECEIVED_BY", "'" + iRECEIVED_BY + "'" );
      lReqMap.put( "ISSUED_BY_MANUFACT_CD", "'" + iISSUED_BY_MANUFACT_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_BOOL", "'" + iSCHED_FROM_MANUFACT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'" + iMANUAL_SCHEDULING_BOOL + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE", "'" + iMIN_FORECAST_RANGE + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "RECEIVED_DT", "SYSDATE" );
      lReqMap.put( "ISSUE_DT", "SYSDATE" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF, lReqMap ) );

      // Second record
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'ACFT_T3'" );
      lReqMap.put( "ATA_CD", "'SYS-1'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_2 + "'" );
      lReqMap.put( "REF_TASK_NAME", "'" + iREF_TASK_NAME_2 + "'" );
      lReqMap.put( "REF_TASK_DESC", "'" + iREF_TASK_DESC_2 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );

      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "RECEIVED_BY", "'" + iRECEIVED_BY + "'" );
      lReqMap.put( "ISSUED_BY_MANUFACT_CD", "'" + iISSUED_BY_MANUFACT_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_BOOL", "'" + iSCHED_FROM_MANUFACT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'" + iMANUAL_SCHEDULING_BOOL + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE", "'" + iMIN_FORECAST_RANGE + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "RECEIVED_DT", "SYSDATE" );
      lReqMap.put( "ISSUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF, lReqMap ) );

      // C_REF_TASK_LINK
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'SYS-1'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "LINK_TYPE_CD", "'" + iLINK_TYPE_CD + "'" );
      lReqMap.put( "LINKED_TASK_CD", "'" + iREF_TASK_CD_2 + "'" );
      lReqMap.put( "LINKED_TASK_ATA_CD", " 'SYS-1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_TASK_LINK, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkUsageErrorCode( testName.getMethodName(), "CFG_REF-12208" );

   }


   /**
    * This test is to verify valid error code CFG_REF-10025:C_REF.recurring_task_bool cannot be NULL
    * or consist entirely of spaces.
    *
    *
    */

   @Test
   public void test_CFG_REF_10025_1() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "REF_TASK_NAME", "'" + iREF_TASK_NAME_1 + "'" );
      lReqMap.put( "REF_TASK_DESC", "'" + iREF_TASK_DESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );

      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "RECEIVED_BY", "'" + iRECEIVED_BY + "'" );
      lReqMap.put( "ISSUED_BY_MANUFACT_CD", "'" + iISSUED_BY_MANUFACT_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_BOOL", "'" + iSCHED_FROM_MANUFACT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'" + iMANUAL_SCHEDULING_BOOL + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE", "'" + iMIN_FORECAST_RANGE + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", null );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF, lReqMap ) );

      // C_REF_TASK_LINK
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "LINK_TYPE_CD", "'" + iLINK_TYPE_CD + "'" );
      lReqMap.put( "LINKED_TASK_CD", "'" + iLINKED_TASK_CD + "'" );
      lReqMap.put( "LINKED_TASK_ATA_CD", "'" + iLINKED_TASK_ATA_CD + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_TASK_LINK, lReqMap ) );

      // C_REF_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_DYNAMIC_DEADLINE, lReqMap ) );

      setReceivedDate( "01/02/2017", iREF_TASK_DESC_1 );
      setIssueDate( "01/02/2017", iREF_TASK_DESC_1 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );
      checkUsageErrorCode( testName.getMethodName(), "CFG_REF-10025" );

   }


   /**
    * This test is to verify valid error code CFG_REF-10025:C_REF.recurring_task_bool cannot be NULL
    * or consist entirely of spaces.
    *
    *
    */

   @Test
   public void test_CFG_REF_10025_2() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "REF_TASK_NAME", "'" + iREF_TASK_NAME_1 + "'" );
      lReqMap.put( "REF_TASK_DESC", "'" + iREF_TASK_DESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );

      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "RECEIVED_BY", "'" + iRECEIVED_BY + "'" );
      lReqMap.put( "ISSUED_BY_MANUFACT_CD", "'" + iISSUED_BY_MANUFACT_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_BOOL", "'" + iSCHED_FROM_MANUFACT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'" + iMANUAL_SCHEDULING_BOOL + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE", "'" + iMIN_FORECAST_RANGE + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "' '" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF, lReqMap ) );

      // C_REF_TASK_LINK
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "LINK_TYPE_CD", "'" + iLINK_TYPE_CD + "'" );
      lReqMap.put( "LINKED_TASK_CD", "'" + iLINKED_TASK_CD + "'" );
      lReqMap.put( "LINKED_TASK_ATA_CD", "'" + iLINKED_TASK_ATA_CD + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_TASK_LINK, lReqMap ) );

      // C_REF_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_DYNAMIC_DEADLINE, lReqMap ) );

      setReceivedDate( "01/02/2017", iREF_TASK_DESC_1 );
      setIssueDate( "01/02/2017", iREF_TASK_DESC_1 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );
      checkUsageErrorCode( testName.getMethodName(), "CFG_REF-10025" );

   }


   /**
    * This test is to verify valid error code CFG_REF-12075:C_REF.recurring_task_bool must be Y or N
    * when provided.
    *
    *
    */

   @Test
   public void test_CFG_REF_12075() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "REF_TASK_NAME", "'" + iREF_TASK_NAME_1 + "'" );
      lReqMap.put( "REF_TASK_DESC", "'" + iREF_TASK_DESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );

      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "RECEIVED_BY", "'" + iRECEIVED_BY + "'" );
      lReqMap.put( "ISSUED_BY_MANUFACT_CD", "'" + iISSUED_BY_MANUFACT_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_BOOL", "'" + iSCHED_FROM_MANUFACT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'" + iMANUAL_SCHEDULING_BOOL + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE", "'" + iMIN_FORECAST_RANGE + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'A'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF, lReqMap ) );

      // C_REF_TASK_LINK
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "LINK_TYPE_CD", "'" + iLINK_TYPE_CD + "'" );
      lReqMap.put( "LINKED_TASK_CD", "'" + iLINKED_TASK_CD + "'" );
      lReqMap.put( "LINKED_TASK_ATA_CD", "'" + iLINKED_TASK_ATA_CD + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_TASK_LINK, lReqMap ) );

      // C_REF_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_DYNAMIC_DEADLINE, lReqMap ) );

      setReceivedDate( "01/02/2017", iREF_TASK_DESC_1 );
      setIssueDate( "01/02/2017", iREF_TASK_DESC_1 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );
      checkUsageErrorCode( testName.getMethodName(), "CFG_REF-12075" );

   }


   /**
    * This test is to verify valid error code CFG_REF_12204:C_REF_TASK_LINK.linked_task_cd cannot be
    * found in Maintenix TASK_TASK table or in the staging table C_REF.
    *
    *
    */
   @Test
   public void test_CFG_REF_12204_1() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "REF_TASK_NAME", "'" + iREF_TASK_NAME_1 + "'" );
      lReqMap.put( "REF_TASK_DESC", "'" + iREF_TASK_DESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );

      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "RECEIVED_BY", "'" + iRECEIVED_BY + "'" );
      lReqMap.put( "ISSUED_BY_MANUFACT_CD", "'" + iISSUED_BY_MANUFACT_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_BOOL", "'" + iSCHED_FROM_MANUFACT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'" + iMANUAL_SCHEDULING_BOOL + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE", "'" + iMIN_FORECAST_RANGE + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "RECEIVED_DT", "SYSDATE" );
      lReqMap.put( "ISSUE_DT", "SYSDATE" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF, lReqMap ) );

      // C_REF_TASK_LINK
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "LINK_TYPE_CD", "'" + iLINK_TYPE_CD + "'" );
      lReqMap.put( "LINKED_TASK_CD", "'" + iLINKED_TASK_CD + "'" );
      lReqMap.put( "LINKED_TASK_ATA_CD", "'SYS-2'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_TASK_LINK, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );
      checkUsageErrorCode( testName.getMethodName(), "CFG_REF-12204" );
   }


   /**
    * This test is to verify valid error code CFG_REF_12204:C_REF_TASK_LINK.linked_task_cd cannot be
    * found in Maintenix TASK_TASK table or in the staging table C_REF.
    *
    *
    */
   @Test
   public void test_CFG_REF_12204_2() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REF
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "REF_TASK_NAME", "'" + iREF_TASK_NAME_1 + "'" );
      lReqMap.put( "REF_TASK_DESC", "'" + iREF_TASK_DESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );

      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "RECEIVED_BY", "'" + iRECEIVED_BY + "'" );
      lReqMap.put( "ISSUED_BY_MANUFACT_CD", "'" + iISSUED_BY_MANUFACT_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_BOOL", "'" + iSCHED_FROM_MANUFACT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'" + iMANUAL_SCHEDULING_BOOL + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE", "'" + iMIN_FORECAST_RANGE + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "RECEIVED_DT", "SYSDATE" );
      lReqMap.put( "ISSUE_DT", "SYSDATE" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF, lReqMap ) );

      // second record
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_2 + "'" );
      lReqMap.put( "REF_TASK_NAME", "'" + iREF_TASK_NAME_2 + "'" );
      lReqMap.put( "REF_TASK_DESC", "'" + iREF_TASK_DESC_2 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );

      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "RECEIVED_BY", "'" + iRECEIVED_BY + "'" );
      lReqMap.put( "ISSUED_BY_MANUFACT_CD", "'" + iISSUED_BY_MANUFACT_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_BOOL", "'" + iSCHED_FROM_MANUFACT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'" + iMANUAL_SCHEDULING_BOOL + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE", "'" + iMIN_FORECAST_RANGE + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "RECEIVED_DT", "SYSDATE" );
      lReqMap.put( "ISSUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF, lReqMap ) );

      // C_REF_TASK_LINK
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "LINK_TYPE_CD", "'" + iLINK_TYPE_CD + "'" );
      lReqMap.put( "LINKED_TASK_CD", "'" + iREF_TASK_CD_2 + "'" );
      lReqMap.put( "LINKED_TASK_ATA_CD", "'SYS-2'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_TASK_LINK, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );
      checkUsageErrorCode( testName.getMethodName(), "CFG_REF-12204" );
   }


   /**
    * This test is to verify valid error code CFG_REF_12204:C_REF_TASK_LINK.linked_task_cd cannot be
    * found in Maintenix TASK_TASK table or in the staging table C_REF.
    *
    *
    */
   @Test
   public void test_CFG_REF_12204_3() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "REF_TASK_NAME", "'" + iREF_TASK_NAME_1 + "'" );
      lReqMap.put( "REF_TASK_DESC", "'" + iREF_TASK_DESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );

      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "RECEIVED_BY", "'" + iRECEIVED_BY + "'" );
      lReqMap.put( "ISSUED_BY_MANUFACT_CD", "'" + iISSUED_BY_MANUFACT_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_BOOL", "'" + iSCHED_FROM_MANUFACT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'" + iMANUAL_SCHEDULING_BOOL + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE", "'" + iMIN_FORECAST_RANGE + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "RECEIVED_DT", "SYSDATE" );
      lReqMap.put( "ISSUE_DT", "SYSDATE" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF, lReqMap ) );

      // C_REF_TASK_LINK
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "LINK_TYPE_CD", "'" + iLINK_TYPE_CD + "'" );
      lReqMap.put( "LINKED_TASK_CD", "'" + iLINKED_TASK_CD + "'" );
      lReqMap.put( "LINKED_TASK_ATA_CD", "'SYS-1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_TASK_LINK, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );
      checkUsageErrorCode( testName.getMethodName(), "CFG_REF-12204" );
   }


   /**
    * This test is to verify valid error code CFG_REF_12206:C_COMP_REF_TASK_LINK.linked_task_cd must
    * be of REQ or REF or JIC class mode.
    *
    *
    */
   @Test
   public void test_CFG_REF_12206() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "REF_TASK_NAME", "'" + iREF_TASK_NAME_1 + "'" );
      lReqMap.put( "REF_TASK_DESC", "'" + iREF_TASK_DESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );

      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "RECEIVED_BY", "'" + iRECEIVED_BY + "'" );
      lReqMap.put( "ISSUED_BY_MANUFACT_CD", "'" + iISSUED_BY_MANUFACT_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_BOOL", "'" + iSCHED_FROM_MANUFACT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'" + iMANUAL_SCHEDULING_BOOL + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE", "'" + iMIN_FORECAST_RANGE + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "RECEIVED_DT", "SYSDATE" );
      lReqMap.put( "ISSUE_DT", "SYSDATE" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF, lReqMap ) );

      // C_REF_TASK_LINK
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "LINK_TYPE_CD", "'" + iLINK_TYPE_CD + "'" );
      lReqMap.put( "LINKED_TASK_CD", "'1-TIME NON-RECURRING'" );
      lReqMap.put( "LINKED_TASK_ATA_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_TASK_LINK, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );
      checkUsageErrorCode( testName.getMethodName(), "CFG_REF-12206" );
   }


   /**
    * This test is to verify valid error code CFG_REF_12207:C_REF_TASK_LINK Reference Document
    * cannot be linked to itself.
    *
    *
    */
   @Test
   public void test_CFG_REF_12207() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "REF_TASK_NAME", "'" + iREF_TASK_NAME_1 + "'" );
      lReqMap.put( "REF_TASK_DESC", "'" + iREF_TASK_DESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );

      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "RECEIVED_BY", "'" + iRECEIVED_BY + "'" );
      lReqMap.put( "ISSUED_BY_MANUFACT_CD", "'" + iISSUED_BY_MANUFACT_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_BOOL", "'" + iSCHED_FROM_MANUFACT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'" + iMANUAL_SCHEDULING_BOOL + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE", "'" + iMIN_FORECAST_RANGE + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "RECEIVED_DT", "SYSDATE" );
      lReqMap.put( "ISSUE_DT", "SYSDATE" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF, lReqMap ) );

      // C_REF_TASK_LINK
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "LINK_TYPE_CD", "'" + iLINK_TYPE_CD + "'" );
      lReqMap.put( "LINKED_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "LINKED_TASK_ATA_CD", "'" + iATA_CD_TRK + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_TASK_LINK, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );
      checkUsageErrorCode( testName.getMethodName(), "CFG_REF-12207" );
   }


   /**
    * This test is to verify valid error code CFG_REF-12116:Only one of C_REF Schedule From Received
    * Date Boolean and Schedule From Manufactured Boolean can be set to true.
    *
    *
    */
   @Test
   public void test_CFG_REF_12116() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_2 + "'" );
      lReqMap.put( "REF_TASK_NAME", "'" + iREF_TASK_NAME_2 + "'" );
      lReqMap.put( "REF_TASK_DESC", "'" + iREF_TASK_DESC_2 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "RECEIVED_BY", "'" + iRECEIVED_BY + "'" );
      lReqMap.put( "ISSUED_BY_MANUFACT_CD", "'" + iISSUED_BY_MANUFACT_CD + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'" + iMANUAL_SCHEDULING_BOOL + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE", "'" + iMIN_FORECAST_RANGE + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "RESCHED_FROM_CD", "'" + iRESCHED_FROM_CD + "'" );
      lReqMap.put( "RECEIVED_DT", "SYSDATE" );
      lReqMap.put( "ISSUE_DT", "SYSDATE" );

      lReqMap.put( "SCHED_FROM_MANUFACT_BOOL", "'Y'" );
      lReqMap.put( "SCHED_FROM_RECEIVED_BOOL", "'Y'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'Y'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );
      checkUsageErrorCode( testName.getMethodName(), "CFG_REF-12116" );
   }


   /**
    * This test is to verify valid error code CFG_REF-12117:C_REF effective From Date cannot be
    * provided when Schedule From Received Date Boolean or Schedule From Manufactured Boolean is set
    * to true.
    *
    *
    */
   @Test
   public void test_CFG_REF_12117_1() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_2 + "'" );
      lReqMap.put( "REF_TASK_NAME", "'" + iREF_TASK_NAME_2 + "'" );
      lReqMap.put( "REF_TASK_DESC", "'" + iREF_TASK_DESC_2 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "RECEIVED_BY", "'" + iRECEIVED_BY + "'" );
      lReqMap.put( "ISSUED_BY_MANUFACT_CD", "'" + iISSUED_BY_MANUFACT_CD + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'" + iMANUAL_SCHEDULING_BOOL + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE", "'" + iMIN_FORECAST_RANGE + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "RESCHED_FROM_CD", "'" + iRESCHED_FROM_CD + "'" );
      lReqMap.put( "RECEIVED_DT", "SYSDATE" );
      lReqMap.put( "ISSUE_DT", "SYSDATE" );

      lReqMap.put( "SCHED_EFFECT_FROM_DT", "SYSDATE" );
      lReqMap.put( "SCHED_FROM_MANUFACT_BOOL", "'Y'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'Y'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );
      checkUsageErrorCode( testName.getMethodName(), "CFG_REF-12117" );
   }


   /**
    * This test is to verify valid error code CFG_REF-12117:C_REF effective From Date cannot be
    * provided when Schedule From Received Date Boolean or Schedule From Manufactured Boolean is set
    * to true.
    *
    *
    */
   @Test
   public void test_CFG_REF_12117_2() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_2 + "'" );
      lReqMap.put( "REF_TASK_NAME", "'" + iREF_TASK_NAME_2 + "'" );
      lReqMap.put( "REF_TASK_DESC", "'" + iREF_TASK_DESC_2 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "RECEIVED_BY", "'" + iRECEIVED_BY + "'" );
      lReqMap.put( "ISSUED_BY_MANUFACT_CD", "'" + iISSUED_BY_MANUFACT_CD + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'" + iMANUAL_SCHEDULING_BOOL + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE", "'" + iMIN_FORECAST_RANGE + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "RESCHED_FROM_CD", "'" + iRESCHED_FROM_CD + "'" );
      lReqMap.put( "RECEIVED_DT", "SYSDATE" );
      lReqMap.put( "ISSUE_DT", "SYSDATE" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'Y'" );

      lReqMap.put( "SCHED_EFFECT_FROM_DT", "SYSDATE" );
      lReqMap.put( "SCHED_FROM_RECEIVED_BOOL", "'Y'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );
      checkUsageErrorCode( testName.getMethodName(), "CFG_REF-12117" );
   }


   /**
    * This test is to verify OPER-19012: C_Ref_Document_Import.Load_Task_Link is missing a join
    * criteria causing links to happen across Assmbl_CD.
    *
    */

   @Test
   public void testOPER19012_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "REF_TASK_NAME", "'" + iREF_TASK_NAME_1 + "'" );
      lReqMap.put( "REF_TASK_DESC", "'" + iREF_TASK_DESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );

      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "RECEIVED_BY", "'" + iRECEIVED_BY + "'" );
      lReqMap.put( "ISSUED_BY_MANUFACT_CD", "'" + iISSUED_BY_MANUFACT_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_BOOL", "'" + iSCHED_FROM_MANUFACT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'" + iMANUAL_SCHEDULING_BOOL + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE", "'" + iMIN_FORECAST_RANGE + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "RECEIVED_DT", "SYSDATE" );
      lReqMap.put( "ISSUE_DT", "SYSDATE" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF, lReqMap ) );

      // C_REF_TASK_LINK
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ATA_CD", "'" + iATA_CD_TRK + "'" );
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "LINK_TYPE_CD", "'" + iLINK_TYPE_CD + "'" );
      lReqMap.put( "LINKED_TASK_CD", "'AT_TEST2'" );
      lReqMap.put( "LINKED_TASK_ATA_CD", "'SYS-1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_TASK_LINK, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify OPER-19012: C_Ref_Document_Import.Load_Task_Link is missing a join
    * criteria causing links to happen across Assmbl_CD.
    *
    */
   @Test
   public void testOPER19012_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER19012_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;
      iTASK_IDs_2 = null;
      iTASK_DEFN_IDs_2 = null;

      // Verify task_task table
      iTASK_IDs = getTaskIds( iREF_TASK_CD_1, iREF_TASK_NAME_1 );
      assmbleInfor lassmblinfor = getassmblInfor( iATA_CD_TRK );

      iTASK_DEFN_IDs = verifyTaskTask( iTASK_IDs, iCLASS_CD, lassmblinfor, iSUBCLASS_CD,
            iORIGINATOR_CD, iREF_TASK_CD_1, iREF_TASK_NAME_1, iREF_TASK_DESC_1, null,
            iSCHED_TO_LATEST_DEADLINE_BOOL_Y_number, iSOFT_DEADLINE_BOOL_Y_number,
            iMUST_BE_REMOVED_CD, iON_CONDITION_BOOL_N_number, "0" );

      // verify task_DEFN
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_task_flag
      lQuery = "select 1 from " + TableUtil.TASK_TASK_FLAGS + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
      Assert.assertTrue( "Check task_task_flags table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_ref_doc table
      // get HR ids
      simpleIDs lHRIDs = getHRIds( iRECEIVED_BY );
      // get receive date and issue date
      lQuery = "SELECT SYSDATE as mydate FROM DUAL";
      java.sql.Date lRCVdate = getDateValueFromQuery( lQuery, "mydate" );
      java.sql.Date lIssuedate = getDateValueFromQuery( lQuery, "mydate" );
      verifyTASKREFDOC( iTASK_IDs, lHRIDs, iISSUED_BY_MANUFACT_CD, "MANUFACT", lRCVdate,
            lIssuedate );

      // verify task_task_dep
      simpleIDs iLink_TASK = getTaskIds( "AT_TEST2", "AT_TEST2" );
      verifyTASKTASkDEP( iLink_TASK, iTASK_DEFN_IDs, iLINK_TYPE_CD );

   }


   // ================================================================================
   /**
    * This function is to set date on RECEIVED_DT column of C_REF
    *
    *
    *
    */
   public void setReceivedDate( String aDate, String aREF_DESC ) {

      String aUpdateQuery = "UPDATE C_REF SET RECEIVED_DT= TO_DATE('" + aDate
            + "', 'MM/DD/YYYY') where REF_TASK_DESC='" + aREF_DESC + "'";
      try {

         PreparedStatement lStatement = getConnection().prepareStatement( aUpdateQuery );
         lStatement.executeUpdate( aUpdateQuery );
         commit();

      } catch ( SQLException e ) {
         e.printStackTrace();
      }

   }


   /**
    * This function is to set date on ISSUE_DT column of C_REF
    *
    *
    *
    */
   public void setIssueDate( String aDate, String aREF_DESC ) {

      String aUpdateQuery = "UPDATE C_REF SET ISSUE_DT= TO_DATE('" + aDate
            + "', 'MM/DD/YYYY') where REF_TASK_DESC='" + aREF_DESC + "'";
      try {

         PreparedStatement lStatement = getConnection().prepareStatement( aUpdateQuery );
         lStatement.executeUpdate( aUpdateQuery );
         commit();

      } catch ( SQLException e ) {
         e.printStackTrace();
      }

   }


   /**
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */

   public void RestoreData() {

      String lStrDelete = null;

      if ( iTASK_IDs_15067 != null ) {

         // delete task_work_type
         lStrDelete = "delete from " + TableUtil.TASK_WORK_TYPE + "  where TASK_DB_ID="
               + iTASK_IDs_15067.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_15067.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_REF_DOC
         lStrDelete = "delete from " + TableUtil.TASK_REF_DOC + "  where TASK_DB_ID="
               + iTASK_IDs_15067.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_15067.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_sched_rule
         lStrDelete = "delete from " + TableUtil.TASK_SCHED_RULE + "  where TASK_DB_ID="
               + iTASK_IDs_15067.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_15067.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_task_dep
         lStrDelete = "delete from " + TableUtil.TASK_TASK_DEP + "  where TASK_DB_ID="
               + iTASK_IDs_15067.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_15067.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_interval
         lStrDelete = "delete from " + TableUtil.TASK_TASK_FLAGS + "  where TASK_DB_ID="
               + iTASK_IDs_15067.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_15067.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_task
         lStrDelete = "delete from " + TableUtil.TASK_TASK + "  where TASK_DB_ID="
               + iTASK_IDs_15067.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_15067.getNO_ID();
         executeSQL( lStrDelete );

      }

      if ( iTASK_DEFN_IDs_15067 != null ) {
         lStrDelete =
               "delete from TASK_DEFN where TASK_DEFN_DB_ID=" + iTASK_DEFN_IDs_15067.getNO_DB_ID()
                     + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs_15067.getNO_ID();
         executeSQL( lStrDelete );

         lStrDelete = "delete from TASK_TASK_DEP where DEP_TASK_DEFN_DB_ID="
               + iTASK_DEFN_IDs_15067.getNO_DB_ID() + " and DEP_TASK_DEFN_ID="
               + iTASK_DEFN_IDs_15067.getNO_ID();
         executeSQL( lStrDelete );

      }

      if ( iTASK_IDs != null ) {

         // delete task_work_type
         lStrDelete = "delete from " + TableUtil.TASK_WORK_TYPE + "  where TASK_DB_ID="
               + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_REF_DOC
         lStrDelete = "delete from " + TableUtil.TASK_REF_DOC + "  where TASK_DB_ID="
               + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_sched_rule
         lStrDelete = "delete from " + TableUtil.TASK_SCHED_RULE + "  where TASK_DB_ID="
               + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_task_dep
         lStrDelete = "delete from " + TableUtil.TASK_TASK_DEP + "  where TASK_DB_ID="
               + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_task_flag
         lStrDelete = "delete from " + TableUtil.TASK_TASK_FLAGS + "  where TASK_DB_ID="
               + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_task
         lStrDelete = "delete from " + TableUtil.TASK_TASK + "  where TASK_DB_ID="
               + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
         executeSQL( lStrDelete );

      }

      if ( iTASK_DEFN_IDs != null ) {
         lStrDelete = "delete from TASK_DEFN where TASK_DEFN_DB_ID=" + iTASK_DEFN_IDs.getNO_DB_ID()
               + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs.getNO_ID();
         executeSQL( lStrDelete );

         lStrDelete =
               "delete from TASK_TASK_DEP where DEP_TASK_DEFN_DB_ID=" + iTASK_DEFN_IDs.getNO_DB_ID()
                     + " and DEP_TASK_DEFN_ID=" + iTASK_DEFN_IDs.getNO_ID();
         executeSQL( lStrDelete );

      }

      if ( iTASK_IDs_2 != null ) {

         // delete task_work_type
         lStrDelete = "delete from " + TableUtil.TASK_WORK_TYPE + "  where TASK_DB_ID="
               + iTASK_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_REF_DOC
         lStrDelete = "delete from " + TableUtil.TASK_REF_DOC + "  where TASK_DB_ID="
               + iTASK_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_sched_rule
         lStrDelete = "delete from " + TableUtil.TASK_SCHED_RULE + "  where TASK_DB_ID="
               + iTASK_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_task_dep
         lStrDelete = "delete from " + TableUtil.TASK_TASK_DEP + "  where TASK_DB_ID="
               + iTASK_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_task_flag
         lStrDelete = "delete from " + TableUtil.TASK_TASK_FLAGS + "  where TASK_DB_ID="
               + iTASK_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_task
         lStrDelete = "delete from " + TableUtil.TASK_TASK + "  where TASK_DB_ID="
               + iTASK_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

      }

      if ( iTASK_DEFN_IDs_2 != null ) {
         lStrDelete =
               "delete from TASK_DEFN where TASK_DEFN_DB_ID=" + iTASK_DEFN_IDs_2.getNO_DB_ID()
                     + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

         lStrDelete = "delete from TASK_TASK_DEP where DEP_TASK_DEFN_DB_ID="
               + iTASK_DEFN_IDs_2.getNO_DB_ID() + " and DEP_TASK_DEFN_ID="
               + iTASK_DEFN_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

      }

   }


   /**
    * This function is to verify c_ref table after data is imported.
    *
    *
    */

   public ArrayList<cREF> getCRefs() {

      String[] iIds = { "REF_TASK_CD", "REF_TASK_NAME", "REF_TASK_DESC", "ASSMBL_CD", "ATA_CD",
            "CLASS_CD", "SUBCLASS_CD", "ORIGINATOR_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SUBCLASS_CD", "TEST3" );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.C_REF, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      ArrayList<cREF> icREFlist = new ArrayList<cREF>();

      for ( int i = 0; i < llists.size(); i++ ) {
         icREFlist.add( new cREF( llists.get( i ).get( 0 ), llists.get( i ).get( 1 ),
               llists.get( i ).get( 2 ), llists.get( i ).get( 3 ), llists.get( i ).get( 4 ),
               llists.get( i ).get( 5 ), llists.get( i ).get( 6 ), llists.get( i ).get( 7 ) ) );

      }

      return icREFlist;

   }


   /**
    * This function is to verify C_REF_TASK_LINK table after data is imported.
    *
    *
    */

   public ArrayList<cRefTaskLink> getCRefsTaskLink() {

      String[] iIds = { "REF_TASK_CD", "ASSMBL_CD", "ATA_CD", "LINK_TYPE_CD", "LINKED_TASK_CD",
            "LINKED_TASK_ATA_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "LINKED_TASK_CD", "AL_TASK" );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.C_REF_TASK_LINK, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      ArrayList<cRefTaskLink> icREFlist = new ArrayList<cRefTaskLink>();

      for ( int i = 0; i < llists.size(); i++ ) {
         icREFlist.add( new cRefTaskLink( llists.get( i ).get( 0 ), llists.get( i ).get( 1 ),
               llists.get( i ).get( 2 ), llists.get( i ).get( 3 ), llists.get( i ).get( 4 ),
               llists.get( i ).get( 5 ) ) );

      }

      return icREFlist;

   }


   /**
    * This function is to verify C_REF_DYNAMIC_DEADLINE table after data is imported.
    *
    *
    */

   public ArrayList<cRefDynamicDeadline> getCRefsDynDead() {

      String[] iIds = { "REF_TASK_CD", "ASSMBL_CD", "ATA_CD", "SCHED_DATA_TYPE_CD",
            "SCHED_INTERVAL_QT", "SCHED_THRESHOLD_QT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "ASSMBL_CD", "ACFT_CD1" );

      String iQueryString =
            TableUtil.buildTableQuery( TableUtil.C_REF_DYNAMIC_DEADLINE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      ArrayList<cRefDynamicDeadline> icREFlist = new ArrayList<cRefDynamicDeadline>();

      for ( int i = 0; i < llists.size(); i++ ) {
         icREFlist.add( new cRefDynamicDeadline( llists.get( i ).get( 0 ), llists.get( i ).get( 1 ),
               llists.get( i ).get( 2 ), llists.get( i ).get( 3 ), llists.get( i ).get( 4 ),
               llists.get( i ).get( 5 ) ) );

      }

      return icREFlist;

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
   public int runValidationAndImport( boolean ablnOnlyValidation, boolean allornone ) {
      int lrtValue = 0;
      ivalidationandimport = new ValidationAndImport() {

         @Override
         public int runValidation( boolean allornone ) {
            int lReturn = 0;
            CallableStatement lPrepareCallJICPART;

            try {

               lPrepareCallJICPART = getConnection().prepareCall(
                     "BEGIN  c_ref_document_import.validate_reference_document(on_retcode =>?); END;" );

               lPrepareCallJICPART.registerOutParameter( 1, Types.INTEGER );
               lPrepareCallJICPART.execute();
               commit();
               lReturn = lPrepareCallJICPART.getInt( 1 );
            } catch ( SQLException e ) {
               e.printStackTrace();
            }
            return lReturn;

         }


         @Override
         public int runImport( boolean allornone ) {
            int lReturn = 0;
            CallableStatement lPrepareCallKIT;

            try {

               lPrepareCallKIT = getConnection().prepareCall(
                     "BEGIN c_ref_document_import.import_ref_document(on_retcode =>?); END;" );

               lPrepareCallKIT.registerOutParameter( 1, Types.INTEGER );

               lPrepareCallKIT.execute();
               commit();
               lReturn = lPrepareCallKIT.getInt( 1 );
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
    * This function is to run export functionality of bl_deferral_references_import
    *
    * @param: aAutoDel
    *
    * @return: return code of Int
    *
    */

   public int runExport() {
      int lReturn = 0;
      CallableStatement lPrepareCall;

      try {

         lPrepareCall = getConnection().prepareCall(
               "BEGIN c_ref_document_import.export_ref_document(aib_autodel_stg_data =>true, "
                     + " aon_retcode => ?, aov_retmsg => ?); END;" );

         lPrepareCall.registerOutParameter( 1, Types.INTEGER );
         lPrepareCall.registerOutParameter( 2, Types.VARCHAR );
         lPrepareCall.execute();
         commit();
         lReturn = lPrepareCall.getInt( 1 );
      } catch ( SQLException e ) {
         e.printStackTrace();
      }
      return lReturn;

   }

}
