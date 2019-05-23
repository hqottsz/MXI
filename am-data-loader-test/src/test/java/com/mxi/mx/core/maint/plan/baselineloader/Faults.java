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

import com.mxi.mx.core.maint.plan.datamodels.assmbleInfor;
import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.BaselineLoaderTest;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.ValidationAndImport;
import com.mxi.mx.util.WhereClause;


/**
 * This test suite contains test cases on validation and import functionality of FAULT_IMPORT
 * package.
 *
 * @author ALICIA QIAN
 */
public class Faults extends BaselineLoaderTest {

   @Rule
   public TestName testName = new TestName();

   ValidationAndImport ivalidationandimport;

   public simpleIDs iTaskDefnIds1 = null;
   public simpleIDs iTaskDefnIds2 = null;
   public simpleIDs iTaskDefnIds3 = null;
   public simpleIDs iTaskIds1 = null;
   public simpleIDs iTaskIds2 = null;
   public simpleIDs iTaskIds3 = null;

   public String iAssmblAPUCD = "APU_CD1";
   public String iAssmblENGCD = "ENG_CD1";
   public String iAssmblACFTCD = "ACFT_CD1";
   public String iAssmblAPUSYSCD = "APU-SYS-1-1-TRK-MULTI-SLOT";
   public String iAssmblENGSYSCD = "ENG-SYS-1-1-TRK-MULTI-SLOT";
   public String iAssmblACFTSYSCD = "ENG-ASSY";
   public String iFailModeAPUCD = "AUTOAPUFAIL";
   public String iFailModeENGCD = "AUTOENGFAIL";
   public String iFailModeACFTCD = "AUTOACFTFAIL";
   public String iFailCAT = "E-S";
   public String iFailPRIORITYCD = "MED";
   public String iFailTYPECD = "F";
   public String iFailDEFERCD = "MEL A";
   public String iFailSRVCD = "MINOR";
   public String iFailDeferRefSdescCD = "AUTOMINOR";
   public String iDataTypeCD = "USAGE1";
   public String iFailModeName = "AUTONAME";
   public String iFailModeLdesc = "AUTOLDESC";
   public String iOPRestrictionLdesc = "AUTOOPLDESC";
   public String iCorrAPUTaskCD = "AUTOCORRAPU";
   public String iCorrENGTaskCD = "AUTOCORRENG";
   public String iCorrACFTTaskCD = "AUTOCORRACFT";

   private ArrayList<String> updateTables = new ArrayList<String>();
   {
      updateTables.add(
            "insert into TASK_DEFN (TASK_DEFN_DB_ID, TASK_DEFN_ID, LAST_REVISION_ORD, NEW_REVISION_BOOL) values (4650, TASK_DEFN_ID.nextval, 1, 1)" );
      updateTables.add(
            "insert into TASK_DEFN (TASK_DEFN_DB_ID, TASK_DEFN_ID, LAST_REVISION_ORD, NEW_REVISION_BOOL) values (4650, TASK_DEFN_ID.nextval, 1, 1)" );
      updateTables.add(
            "insert into TASK_DEFN (TASK_DEFN_DB_ID, TASK_DEFN_ID, LAST_REVISION_ORD, NEW_REVISION_BOOL) values (4650, TASK_DEFN_ID.nextval, 1, 1)" );
   };


   /**
    * Setup before executing each individual test
    *
    */
   @Before
   @Override
   public void before() throws Exception {

      super.before();
      clearBaselineLoaderTables();
      DataSetup();

   }


   /**
    * Clean up after each individual test
    */
   @After
   @Override
   public void after() {
      RestoreData();
      super.after();
   }


   /**
    * This test is to verify FAULT_IMPORT validation functionality of staging table C_FAIL_MODE on
    * ASSMBL_CD='APU_CD1'
    *
    *
    */
   @Test
   public void testAPUFAULT_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_FAIL_MODE table
      Map<String, String> lFault = new LinkedHashMap<>();

      lFault.put( "ASSMBL_CD", "\'" + iAssmblAPUCD + "\'" );
      lFault.put( "ATA_SYS_CD", "\'" + iAssmblAPUSYSCD + "\'" );
      lFault.put( "FAIL_MODE_CD", "\'" + iFailModeAPUCD + "\'" );
      lFault.put( "FAIL_CATGRY_CD", "\'" + iFailCAT + "\'" );
      lFault.put( "FAIL_PRIORITY_CD", "\'" + iFailPRIORITYCD + "\'" );
      lFault.put( "FAIL_TYPE_CD", "\'" + iFailTYPECD + "\'" );
      lFault.put( "FAIL_DEFER_CD", "\'" + iFailDEFERCD + "\'" );
      lFault.put( "FAIL_SEVER_CD", "\'" + iFailSRVCD + "\'" );
      lFault.put( "DEFER_REF_SDESC", "\'" + iFailDeferRefSdescCD + "\'" );
      lFault.put( "DATA_TYPE_CD", "\'" + iDataTypeCD + "\'" );
      lFault.put( "FAIL_MODE_NAME", "\'" + iFailModeName + "\'" );
      lFault.put( "FAIL_MODE_LDESC", "\'" + iFailModeLdesc + "\'" );
      lFault.put( "MTBF_QT", "\'1\'" );
      lFault.put( "MTTR_QT", "\'2\'" );
      lFault.put( "CALC_PRIORITY_BOOL", "\'Y\'" );
      lFault.put( "OP_RESTRICTION_LDESC", "\'" + iOPRestrictionLdesc + "\'" );
      lFault.put( "MANUAL_RAISE_BOOL", "\'Y\'" );
      lFault.put( "OBSOLETE_BOOL", "\'N\'" );
      lFault.put( "CORR_TASK_CD", "\'" + iCorrAPUTaskCD + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_FAIL_MODE, lFault ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify FAULT_IMPORT import functionality of staging table C_FAIL_MODE on
    * ASSMBL_CD='APU_CD1'
    *
    *
    */
   @Test
   public void testAPUFAULT_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testAPUFAULT_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify fail_mode table
      simpleIDs ltaskIds = getTaskIds( iCorrAPUTaskCD );
      assmbleInfor lAssmblInf = getAssmblInfor( iAssmblAPUCD, iAssmblAPUSYSCD );
      simpleIDs lTypeIds = getDataType( iDataTypeCD );

      verifyFailMode( ltaskIds, lAssmblInf, lTypeIds, iFailCAT, iFailPRIORITYCD, iFailTYPECD,
            iFailDEFERCD, iFailSRVCD, iFailDeferRefSdescCD, iFailModeAPUCD, iFailModeName,
            iFailModeLdesc, iOPRestrictionLdesc, "1", "2", "1", "1", "0" );

   }


   /**
    * This test is to verify FAULT_IMPORT validation functionality of staging table C_FAIL_MODE on
    * ASSMBL_CD='ENG_CD1'
    *
    *
    */
   @Test
   public void testENGFAULT_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_FAIL_MODE table
      Map<String, String> lFault = new LinkedHashMap<>();

      lFault.put( "ASSMBL_CD", "\'" + iAssmblENGCD + "\'" );
      lFault.put( "ATA_SYS_CD", "\'" + iAssmblENGSYSCD + "\'" );
      lFault.put( "FAIL_MODE_CD", "\'" + iFailModeENGCD + "\'" );
      lFault.put( "FAIL_CATGRY_CD", "\'" + iFailCAT + "\'" );
      lFault.put( "FAIL_PRIORITY_CD", "\'" + iFailPRIORITYCD + "\'" );
      lFault.put( "FAIL_TYPE_CD", "\'" + iFailTYPECD + "\'" );
      lFault.put( "FAIL_DEFER_CD", "\'" + iFailDEFERCD + "\'" );
      lFault.put( "FAIL_SEVER_CD", "\'" + iFailSRVCD + "\'" );
      lFault.put( "DEFER_REF_SDESC", "\'" + iFailDeferRefSdescCD + "\'" );
      lFault.put( "DATA_TYPE_CD", "\'" + iDataTypeCD + "\'" );
      lFault.put( "FAIL_MODE_NAME", "\'" + iFailModeName + "\'" );
      lFault.put( "FAIL_MODE_LDESC", "\'" + iFailModeLdesc + "\'" );
      lFault.put( "MTBF_QT", "\'1\'" );
      lFault.put( "MTTR_QT", "\'2\'" );
      lFault.put( "CALC_PRIORITY_BOOL", "\'Y\'" );
      lFault.put( "OP_RESTRICTION_LDESC", "\'" + iOPRestrictionLdesc + "\'" );
      lFault.put( "MANUAL_RAISE_BOOL", "\'Y\'" );
      lFault.put( "OBSOLETE_BOOL", "\'N\'" );
      lFault.put( "CORR_TASK_CD", "\'" + iCorrENGTaskCD + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_FAIL_MODE, lFault ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify FAULT_IMPORT import functionality of staging table C_FAIL_MODE on
    * ASSMBL_CD='APU_CD1'
    *
    *
    */
   @Test
   public void testENGFAULT_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testENGFAULT_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify fail_mode table
      simpleIDs ltaskIds = getTaskIds( iCorrENGTaskCD );
      assmbleInfor lAssmblInf = getAssmblInfor( iAssmblENGCD, iAssmblENGSYSCD );
      simpleIDs lTypeIds = getDataType( iDataTypeCD );

      verifyFailMode( ltaskIds, lAssmblInf, lTypeIds, iFailCAT, iFailPRIORITYCD, iFailTYPECD,
            iFailDEFERCD, iFailSRVCD, iFailDeferRefSdescCD, iFailModeENGCD, iFailModeName,
            iFailModeLdesc, iOPRestrictionLdesc, "1", "2", "1", "1", "0" );

   }


   /**
    * This test is to verify FAULT_IMPORT validation functionality of staging table C_FAIL_MODE on
    * ASSMBL_CD='ACFT_CD1'
    *
    *
    */
   @Test
   public void testACFTFAULT_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_FAIL_MODE table
      Map<String, String> lFault = new LinkedHashMap<>();

      lFault.put( "ASSMBL_CD", "\'" + iAssmblACFTCD + "\'" );
      lFault.put( "ATA_SYS_CD", "\'" + iAssmblACFTSYSCD + "\'" );
      lFault.put( "FAIL_MODE_CD", "\'" + iFailModeACFTCD + "\'" );
      lFault.put( "FAIL_CATGRY_CD", "\'" + iFailCAT + "\'" );
      lFault.put( "FAIL_PRIORITY_CD", "\'" + iFailPRIORITYCD + "\'" );
      lFault.put( "FAIL_TYPE_CD", "\'" + iFailTYPECD + "\'" );
      lFault.put( "FAIL_DEFER_CD", "\'" + iFailDEFERCD + "\'" );
      lFault.put( "FAIL_SEVER_CD", "\'" + iFailSRVCD + "\'" );
      lFault.put( "DEFER_REF_SDESC", "\'" + iFailDeferRefSdescCD + "\'" );
      lFault.put( "DATA_TYPE_CD", "\'" + iDataTypeCD + "\'" );
      lFault.put( "FAIL_MODE_NAME", "\'" + iFailModeName + "\'" );
      lFault.put( "FAIL_MODE_LDESC", "\'" + iFailModeLdesc + "\'" );
      lFault.put( "MTBF_QT", "\'1\'" );
      lFault.put( "MTTR_QT", "\'2\'" );
      lFault.put( "CALC_PRIORITY_BOOL", "\'Y\'" );
      lFault.put( "OP_RESTRICTION_LDESC", "\'" + iOPRestrictionLdesc + "\'" );
      lFault.put( "MANUAL_RAISE_BOOL", "\'Y\'" );
      lFault.put( "OBSOLETE_BOOL", "\'N\'" );
      lFault.put( "CORR_TASK_CD", "\'" + iCorrACFTTaskCD + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_FAIL_MODE, lFault ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify FAULT_IMPORT import functionality of staging table C_FAIL_MODE on
    * ASSMBL_CD='ACFT_CD1'
    *
    *
    */
   @Test
   public void testACFTFAULT_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testACFTFAULT_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify fail_mode table
      simpleIDs ltaskIds = getTaskIds( iCorrACFTTaskCD );
      assmbleInfor lAssmblInf = getAssmblInfor( iAssmblACFTCD, iAssmblACFTSYSCD );
      simpleIDs lTypeIds = getDataType( iDataTypeCD );

      verifyFailMode( ltaskIds, lAssmblInf, lTypeIds, iFailCAT, iFailPRIORITYCD, iFailTYPECD,
            iFailDEFERCD, iFailSRVCD, iFailDeferRefSdescCD, iFailModeACFTCD, iFailModeName,
            iFailModeLdesc, iOPRestrictionLdesc, "1", "2", "1", "1", "0" );

   }


   /**
    * This test is to verify FAULT_IMPORT validation functionality of staging table C_FAIL_MODE on
    * ASSMBL_CD='ACFT_CD1'&ASSMBL_CD='ENG_CD1'
    *
    *
    */
   @Test
   public void testMultipleFAULTs_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_FAIL_MODE table
      // ACFT record
      Map<String, String> lFault = new LinkedHashMap<>();

      lFault.put( "ASSMBL_CD", "\'" + iAssmblACFTCD + "\'" );
      lFault.put( "ATA_SYS_CD", "\'" + iAssmblACFTSYSCD + "\'" );
      lFault.put( "FAIL_MODE_CD", "\'" + iFailModeACFTCD + "\'" );
      lFault.put( "FAIL_CATGRY_CD", "\'" + iFailCAT + "\'" );
      lFault.put( "FAIL_PRIORITY_CD", "\'" + iFailPRIORITYCD + "\'" );
      lFault.put( "FAIL_TYPE_CD", "\'" + iFailTYPECD + "\'" );
      lFault.put( "FAIL_DEFER_CD", "\'" + iFailDEFERCD + "\'" );
      lFault.put( "FAIL_SEVER_CD", "\'" + iFailSRVCD + "\'" );
      lFault.put( "DEFER_REF_SDESC", "\'" + iFailDeferRefSdescCD + "\'" );
      lFault.put( "DATA_TYPE_CD", "\'" + iDataTypeCD + "\'" );
      lFault.put( "FAIL_MODE_NAME", "\'" + iFailModeName + "\'" );
      lFault.put( "FAIL_MODE_LDESC", "\'" + iFailModeLdesc + "\'" );
      lFault.put( "MTBF_QT", "\'1\'" );
      lFault.put( "MTTR_QT", "\'2\'" );
      lFault.put( "CALC_PRIORITY_BOOL", "\'Y\'" );
      lFault.put( "OP_RESTRICTION_LDESC", "\'" + iOPRestrictionLdesc + "\'" );
      lFault.put( "MANUAL_RAISE_BOOL", "\'Y\'" );
      lFault.put( "OBSOLETE_BOOL", "\'N\'" );
      lFault.put( "CORR_TASK_CD", "\'" + iCorrACFTTaskCD + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_FAIL_MODE, lFault ) );

      // ENG record
      lFault.clear();
      lFault.put( "ASSMBL_CD", "\'" + iAssmblENGCD + "\'" );
      lFault.put( "ATA_SYS_CD", "\'" + iAssmblENGSYSCD + "\'" );
      lFault.put( "FAIL_MODE_CD", "\'" + iFailModeENGCD + "\'" );
      lFault.put( "FAIL_CATGRY_CD", "\'" + iFailCAT + "\'" );
      lFault.put( "FAIL_PRIORITY_CD", "\'" + iFailPRIORITYCD + "\'" );
      lFault.put( "FAIL_TYPE_CD", "\'" + iFailTYPECD + "\'" );
      lFault.put( "FAIL_DEFER_CD", "\'" + iFailDEFERCD + "\'" );
      lFault.put( "FAIL_SEVER_CD", "\'" + iFailSRVCD + "\'" );
      lFault.put( "DEFER_REF_SDESC", "\'" + iFailDeferRefSdescCD + "\'" );
      lFault.put( "DATA_TYPE_CD", "\'" + iDataTypeCD + "\'" );
      lFault.put( "FAIL_MODE_NAME", "\'" + iFailModeName + "\'" );
      lFault.put( "FAIL_MODE_LDESC", "\'" + iFailModeLdesc + "\'" );
      lFault.put( "MTBF_QT", "\'1\'" );
      lFault.put( "MTTR_QT", "\'2\'" );
      lFault.put( "CALC_PRIORITY_BOOL", "\'Y\'" );
      lFault.put( "OP_RESTRICTION_LDESC", "\'" + iOPRestrictionLdesc + "\'" );
      lFault.put( "MANUAL_RAISE_BOOL", "\'Y\'" );
      lFault.put( "OBSOLETE_BOOL", "\'N\'" );
      lFault.put( "CORR_TASK_CD", "\'" + iCorrENGTaskCD + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_FAIL_MODE, lFault ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify FAULT_IMPORT import functionality of staging table C_FAIL_MODE on
    * ASSMBL_CD='ACFT_CD1'&ASSMBL_CD='ENG_CD1'
    *
    *
    */
   @Test
   public void testMultipleFAULTs_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testMultipleFAULTs_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify fail_mode table for ACFT
      simpleIDs ltaskIds = getTaskIds( iCorrACFTTaskCD );
      assmbleInfor lAssmblInf = getAssmblInfor( iAssmblACFTCD, iAssmblACFTSYSCD );
      simpleIDs lTypeIds = getDataType( iDataTypeCD );

      verifyFailMode( ltaskIds, lAssmblInf, lTypeIds, iFailCAT, iFailPRIORITYCD, iFailTYPECD,
            iFailDEFERCD, iFailSRVCD, iFailDeferRefSdescCD, iFailModeACFTCD, iFailModeName,
            iFailModeLdesc, iOPRestrictionLdesc, "1", "2", "1", "1", "0" );

      // Verify fail_mode table for ENG
      simpleIDs lENGtaskIds = getTaskIds( iCorrENGTaskCD );
      assmbleInfor lENGAssmblInf = getAssmblInfor( iAssmblENGCD, iAssmblENGSYSCD );
      // simpleIDs lTypeIds = getDataType( iDataTypeCD );

      verifyFailMode( lENGtaskIds, lENGAssmblInf, lTypeIds, iFailCAT, iFailPRIORITYCD, iFailTYPECD,
            iFailDEFERCD, iFailSRVCD, iFailDeferRefSdescCD, iFailModeENGCD, iFailModeName,
            iFailModeLdesc, iOPRestrictionLdesc, "1", "2", "1", "1", "0" );

   }


   // ===============================================

   /**
    * This function is to verify FAIL_MODE by given parameters .
    *
    *
    */
   public void verifyFailMode( simpleIDs ataskIds, assmbleInfor aAssmbleInfor, simpleIDs aTypeIds,
         String aFailCAT, String aFailPRIORITYCD, String aFailTYPECD, String aFailDEFERCD,
         String aFailSRVCD, String aFailDeferRefSdescCD, String aFailModeCD, String aFailModeName,
         String aFailModeLdesc, String aOPRestrictionLdesc, String aMTBFQT, String aMTTRQT,
         String aCALCPRIORITYBOOL, String aManualRaise, String aObsoleteBool ) {

      String[] iIds = { "ASSMBL_DB_ID", "ASSMBL_CD", "ASSMBL_BOM_ID", "DATA_TYPE_DB_ID",
            "DATA_TYPE_ID", "FAIL_CATGRY_CD", "FAIL_PRIORITY_CD", "FAIL_TYPE_CD", "FAIL_DEFER_CD",
            "FAIL_SEV_CD", "DEFER_REF_SDESC", "FAIL_MODE_CD", "FAIL_MODE_NAME", "FAIL_MODE_LDESC",
            "OP_RESTRICTION_LDESC", "MTBF_QT", "MTTR_QT", "CALC_PRIORITY_BOOL", "MANUAL_RAISE_BOOL",
            "OBSOLETE_BOOL" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", ataskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", ataskIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.FAIL_MODE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "ASSMBL_DB_ID",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aAssmbleInfor.getASSMBL_DB_ID() ) );
      Assert.assertTrue( "ASSMBL_CD",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aAssmbleInfor.getASSMBL_CD() ) );
      Assert.assertTrue( "ASSMBL_BOM_ID",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aAssmbleInfor.getASSMBL_BOM_ID() ) );
      Assert.assertTrue( "DATA_TYPE_DB_ID",
            llists.get( 0 ).get( 3 ).equalsIgnoreCase( aTypeIds.getNO_DB_ID() ) );
      Assert.assertTrue( "DATA_TYPE_ID",
            llists.get( 0 ).get( 4 ).equalsIgnoreCase( aTypeIds.getNO_ID() ) );
      Assert.assertTrue( "FAIL_CATGRY_CD", llists.get( 0 ).get( 5 ).equalsIgnoreCase( aFailCAT ) );
      Assert.assertTrue( "FAIL_PRIORITY_CD",
            llists.get( 0 ).get( 6 ).equalsIgnoreCase( aFailPRIORITYCD ) );
      Assert.assertTrue( "FAIL_TYPE_CD", llists.get( 0 ).get( 7 ).equalsIgnoreCase( aFailTYPECD ) );
      Assert.assertTrue( "FAIL_DEFER_CD",
            llists.get( 0 ).get( 8 ).equalsIgnoreCase( aFailDEFERCD ) );
      Assert.assertTrue( "FAIL_SEV_CD", llists.get( 0 ).get( 9 ).equalsIgnoreCase( aFailSRVCD ) );
      Assert.assertTrue( "DEFER_REF_SDESC",
            llists.get( 0 ).get( 10 ).equalsIgnoreCase( aFailDeferRefSdescCD ) );
      Assert.assertTrue( "FAIL_MODE_CD",
            llists.get( 0 ).get( 11 ).equalsIgnoreCase( aFailModeCD ) );
      Assert.assertTrue( "FAIL_MODE_NAME",
            llists.get( 0 ).get( 12 ).equalsIgnoreCase( aFailModeName ) );
      Assert.assertTrue( "FAIL_MODE_LDESC",
            llists.get( 0 ).get( 13 ).equalsIgnoreCase( aFailModeLdesc ) );
      Assert.assertTrue( "OP_RESTRICTION_LDESC",
            llists.get( 0 ).get( 14 ).equalsIgnoreCase( aOPRestrictionLdesc ) );
      Assert.assertTrue( "MTBF_QT", llists.get( 0 ).get( 15 ).equalsIgnoreCase( aMTBFQT ) );
      Assert.assertTrue( "MTTR_QT", llists.get( 0 ).get( 16 ).equalsIgnoreCase( aMTTRQT ) );
      Assert.assertTrue( "CALC_PRIORITY_BOOL",
            llists.get( 0 ).get( 17 ).equalsIgnoreCase( aCALCPRIORITYBOOL ) );
      Assert.assertTrue( "MANUAL_RAISE_BOOL",
            llists.get( 0 ).get( 18 ).equalsIgnoreCase( aManualRaise ) );
      Assert.assertTrue( "OBSOLETE_BOOL",
            llists.get( 0 ).get( 19 ).equalsIgnoreCase( aObsoleteBool ) );

   }


   /**
    * This function is to retrieve assemble information from EQP_ASSMBL_BOM table .
    *
    *
    */

   public assmbleInfor getAssmblInfor( String aAssmblCD, String aAssmblBomCD ) {
      String[] iIds = { "ASSMBL_DB_ID", "ASSMBL_CD", "ASSMBL_BOM_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "ASSMBL_CD", aAssmblCD );
      lArgs.addArguments( "ASSMBL_BOM_CD", aAssmblBomCD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_ASSMBL_BOM, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      assmbleInfor lIds = new assmbleInfor( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ),
            llists.get( 0 ).get( 2 ), null );

      return lIds;

   }


   /**
    * This function is to retrieve task IDs from task_task table .
    *
    *
    */

   public simpleIDs getTaskIds( String aTaskCD ) {

      String[] iIds = { "TASK_DB_ID", "TASK_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_CD", aTaskCD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_TASK, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;

   }


   /**
    * This function is to retrieve data type IDs from MIM_DATA_TYPE table .
    *
    *
    */

   public simpleIDs getDataType( String aTypeCD ) {

      String[] iIds = { "DATA_TYPE_DB_ID", "DATA_TYPE_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "DATA_TYPE_CD", aTypeCD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.MIM_DATA_TYPE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;

   }


   /**
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */

   public void DataSetup() {

      iTaskDefnIds1 = null;
      iTaskDefnIds2 = null;
      iTaskDefnIds3 = null;
      iTaskIds1 = null;
      iTaskIds2 = null;
      iTaskIds3 = null;

      String iQueryString = null;

      // Create task_defn
      runSqlsInTable( updateTables );

      // Get newly created task defn ids
      String[] iIds = { "TASK_DEFN_DB_ID", "TASK_DEFN_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      iQueryString =
            "select TASK_DEFN_DB_ID, TASK_DEFN_ID from task_defn order by CREATION_DT desc";
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      iTaskDefnIds1 = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );
      iTaskDefnIds2 = new simpleIDs( llists.get( 1 ).get( 0 ), llists.get( 1 ).get( 1 ) );
      iTaskDefnIds3 = new simpleIDs( llists.get( 2 ).get( 0 ), llists.get( 2 ).get( 1 ) );

      // Construct task_task sql string and create new tasks
      String lquery =
            "select ASSMBL_BOM_ID from eqp_bom_part where ASSMBL_CD='APU_CD1' and bom_part_cd='APU-SYS-1-1-TRK-MULTI-SLOT'";
      String lId = getStringValueFromQuery( lquery, "ASSMBL_BOM_ID" );

      iQueryString =
            "insert into TASK_TASK (TASK_DB_ID, TASK_ID, TASK_DEFN_DB_ID, TASK_DEFN_ID, TASK_CLASS_DB_ID, TASK_CLASS_CD, ASSMBL_DB_ID, "
                  + "ASSMBL_CD, ASSMBL_BOM_ID, TASK_DEF_STATUS_DB_ID, TASK_DEF_STATUS_CD,TASK_CD, TASK_NAME)"
                  + " values (" + CONS_DB_ID + ", TASK_ID_SEQ.nextval, "
                  + iTaskDefnIds1.getNO_DB_ID() + ", " + iTaskDefnIds1.getNO_ID() + ", 0, 'CORR', "
                  + CONS_DB_ID + ", 'APU_CD1', " + lId
                  + ", 0, 'ACTV','AUTOCORRAPU', 'AUTOCORRAPU')";
      executeSQL( iQueryString );

      lquery =
            "select ASSMBL_BOM_ID from eqp_bom_part where ASSMBL_CD='ENG_CD1' and bom_part_cd='ENG-SYS-1-1-TRK-MULTI-SLOT'";
      lId = getStringValueFromQuery( lquery, "ASSMBL_BOM_ID" );

      iQueryString =
            "insert into TASK_TASK (TASK_DB_ID, TASK_ID, TASK_DEFN_DB_ID, TASK_DEFN_ID, TASK_CLASS_DB_ID, TASK_CLASS_CD, ASSMBL_DB_ID, "
                  + "ASSMBL_CD, ASSMBL_BOM_ID, TASK_DEF_STATUS_DB_ID, TASK_DEF_STATUS_CD,TASK_CD, TASK_NAME)"
                  + " values (" + CONS_DB_ID + ", TASK_ID_SEQ.nextval, "
                  + iTaskDefnIds2.getNO_DB_ID() + ", " + iTaskDefnIds2.getNO_ID() + ", 0, 'CORR', "
                  + CONS_DB_ID + ", 'ENG_CD1'," + lId + ", 0, 'ACTV','AUTOCORRENG', 'AUTOCORRENG')";
      executeSQL( iQueryString );

      // Get ASSMBL_BOM_ID from eqp_bom_part
      lquery =
            "select ASSMBL_BOM_ID from eqp_bom_part where ASSMBL_CD='ACFT_CD1' and bom_part_cd='ENG-ASSY'";
      lId = getStringValueFromQuery( lquery, "ASSMBL_BOM_ID" );

      iQueryString =
            "insert into TASK_TASK (TASK_DB_ID, TASK_ID, TASK_DEFN_DB_ID, TASK_DEFN_ID, TASK_CLASS_DB_ID, TASK_CLASS_CD, ASSMBL_DB_ID, "
                  + "ASSMBL_CD, ASSMBL_BOM_ID, TASK_DEF_STATUS_DB_ID, TASK_DEF_STATUS_CD,TASK_CD, TASK_NAME)"
                  + " values (" + CONS_DB_ID + ", TASK_ID_SEQ.nextval, "
                  + iTaskDefnIds3.getNO_DB_ID() + ", " + iTaskDefnIds3.getNO_ID() + ", 0, 'CORR', "
                  + CONS_DB_ID + ", 'ACFT_CD1'," + lId
                  + ", 0, 'ACTV','AUTOCORRACFT', 'AUTOCORRACFT')";
      executeSQL( iQueryString );

      // Get newly created task ids
      llists.clear();
      String[] iIds2 = { "TASK_DB_ID", "TASK_ID" };
      List<String> lfieldsTask = new ArrayList<String>( Arrays.asList( iIds2 ) );

      // Get task id1
      WhereClause lArgs1 = new WhereClause();
      lArgs1.addArguments( "TASK_CD", "AUTOCORRAPU" );

      iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_TASK, lfieldsTask, lArgs1 );
      llists.clear();
      llists = execute( iQueryString, lfieldsTask );
      iTaskIds1 = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      // Get task id2
      WhereClause lArgs2 = new WhereClause();
      lArgs2.addArguments( "TASK_CD", "AUTOCORRENG" );

      iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_TASK, lfieldsTask, lArgs2 );
      llists.clear();
      llists = execute( iQueryString, lfieldsTask );
      iTaskIds2 = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      // Get task id3
      WhereClause lArgs3 = new WhereClause();
      lArgs3.addArguments( "TASK_CD", "AUTOCORRACFT" );

      iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_TASK, lfieldsTask, lArgs3 );
      llists.clear();
      llists = execute( iQueryString, lfieldsTask );
      iTaskIds3 = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      // // create sched rule for each task
      // iQueryString =
      // "insert into TASK_SCHED_RULE (TASK_DB_ID, TASK_ID, DATA_TYPE_DB_ID, DATA_TYPE_ID, "
      // + "DEF_INTERVAL_QT, DEF_NOTIFY_QT, DEF_DEVIATION_QT, DEF_PREFIXED_QT, DEF_POSTFIXED_QT,
      // DEF_INITIAL_QT, RSTAT_CD)"
      // + " values (" + iTaskIds1.getNO_DB_ID() + ", " + iTaskIds1.getNO_ID()
      // + ", 0, 1, 1, 1, 1, 1, 1, 1, 0 )";
      // executeSQL( iQueryString );
      //
      // iQueryString =
      // "insert into TASK_SCHED_RULE (TASK_DB_ID, TASK_ID, DATA_TYPE_DB_ID, DATA_TYPE_ID, "
      // + "DEF_INTERVAL_QT, DEF_NOTIFY_QT, DEF_DEVIATION_QT, DEF_PREFIXED_QT, DEF_POSTFIXED_QT,
      // DEF_INITIAL_QT, RSTAT_CD)"
      // + " values (" + iTaskIds2.getNO_DB_ID() + ", " + iTaskIds2.getNO_ID()
      // + ", 0, 1, 1, 1, 1, 1, 1, 1, 0 )";
      // executeSQL( iQueryString );
      //
      // iQueryString =
      // "insert into TASK_SCHED_RULE (TASK_DB_ID, TASK_ID, DATA_TYPE_DB_ID, DATA_TYPE_ID, "
      // + "DEF_INTERVAL_QT, DEF_NOTIFY_QT, DEF_DEVIATION_QT, DEF_PREFIXED_QT, DEF_POSTFIXED_QT,
      // DEF_INITIAL_QT, RSTAT_CD)"
      // + " values (" + iTaskIds3.getNO_DB_ID() + ", " + iTaskIds3.getNO_ID()
      // + ", 0, 1, 1, 1, 1, 1, 1, 1, 0 )";
      // executeSQL( iQueryString );

   }


   /**
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */

   public void RestoreData() {

      String lStrDelete = null;

      // Delete FAIL_MODE
      lStrDelete = "delete from FAIL_MODE where FAIL_MODE_NAME like '%AUTO%'";
      executeSQL( lStrDelete );

      // Delete task_task table
      lStrDelete = "delete from TASK_TASK where TASK_CD like '%AUTOCORR%'";
      executeSQL( lStrDelete );

      // Delete task_defn table
      if ( iTaskDefnIds1 != null ) {
         lStrDelete = "delete from TASK_DEFN where TASK_DEFN_DB_ID=" + iTaskDefnIds1.getNO_DB_ID()
               + " and TASK_DEFN_ID=" + iTaskDefnIds1.getNO_ID();
         executeSQL( lStrDelete );

      }

      if ( iTaskDefnIds2 != null ) {
         lStrDelete = "delete from TASK_DEFN where TASK_DEFN_DB_ID=" + iTaskDefnIds2.getNO_DB_ID()
               + " and TASK_DEFN_ID=" + iTaskDefnIds2.getNO_ID();
         executeSQL( lStrDelete );

      }

      if ( iTaskDefnIds3 != null ) {
         lStrDelete = "delete from TASK_DEFN where TASK_DEFN_DB_ID=" + iTaskDefnIds3.getNO_DB_ID()
               + " and TASK_DEFN_ID=" + iTaskDefnIds3.getNO_ID();
         executeSQL( lStrDelete );

      }

   }


   /**
    * This function is to implement interface ValidationAndImport
    *
    * @param: blnOnlyValidation
    * @param: allornone
    *
    */
   public int runValidationAndImport( boolean blnOnlyValidation, boolean allornone ) {
      int rtValue = 0;
      ivalidationandimport = new ValidationAndImport() {

         @Override
         public int runValidation( boolean allornone ) {
            int lReturn = 0;

            try {
               CallableStatement lPrepareCallInventory = getConnection()
                     .prepareCall( "BEGIN fault_import.validate_fault(on_retcode => ?); END;" );

               lPrepareCallInventory.registerOutParameter( 1, Types.INTEGER );
               lPrepareCallInventory.execute();
               commit();
               lReturn = lPrepareCallInventory.getInt( 1 );

            } catch ( SQLException e ) {
               e.printStackTrace();
            }
            return lReturn;

         }


         @Override
         public int runImport( boolean allornone ) {
            int lReturn = 0;

            try {
               CallableStatement lPrepareCallInventory = getConnection()
                     .prepareCall( "BEGIN fault_import.import_fault(on_retcode => ?); END;" );

               lPrepareCallInventory.registerOutParameter( 1, Types.INTEGER );

               lPrepareCallInventory.execute();
               commit();
               lReturn = lPrepareCallInventory.getInt( 1 );

            } catch ( SQLException e ) {
               e.printStackTrace();
            }
            return lReturn;
         }

      };

      rtValue = blnOnlyValidation ? ivalidationandimport.runValidation( allornone )
            : ivalidationandimport.runImport( allornone );

      return rtValue;
   }

}
