package com.mxi.mx.core.maint.plan.baselineloader;

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
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.WhereClause;


/**
 * These happy path tests will verify BL Parts which will test the Part_Import package. These tests
 * include c_dyn_part_config and c_part_config;
 *
 */
public class Parts extends PartTest {

   @Rule
   public TestName testName = new TestName();

   public final String iBaselineCd = "BLCode";
   public final String iPartPrefix = "737Part";
   public final String iManufactCd = "'10001'";
   public final String iBomCd1 = "'ACFT-SYS-1-1-TRK-P5'";
   public final String iBomCd2 = "'ACFT-SYS-1-1-TRK-MULTIPART2'";
   public final String iBomCd3 = "'SYS-1-1'";
   public final String iPosPrefix = "A";
   public final String iPartGroupAppl = "'A300-A700'";
   public final String iPartAppl = "A380";


   @Override
   @Before
   public void before() throws Exception {
      super.before();
      classDataSetup( iClearsTables );
      clearBaselineLoaderTables();

      String strTCName = testName.getMethodName();
      if ( strTCName.contains( "testOPER_3037" ) ) {
         setupDataBase();
      }

   }


   /**
    * Deleting imported data from Mx Tables
    */
   protected ArrayList<String> iClearsMxTables = new ArrayList<String>();
   {
      iClearsMxTables
            .add( "DELETE FROM " + TableUtil.EQP_PART_NO + " where part_no_oem like '%737PART%'" );
      iClearsMxTables.add( "DELETE FROM " + TableUtil.EQP_PART_BASELINE
            + " where part_baseline_cd like '" + iBaselineCd + "%'" );
      iClearsMxTables.add( "DELETE FROM " + TableUtil.EQP_BOM_PART + " where bom_part_cd in ("
            + iBomCd1 + "," + iBomCd2 + ")" );
      iClearsMxTables.add( "DELETE FROM " + TableUtil.EQP_ASSMBL_BOM + " where assmbl_bom_cd in ("
            + iBomCd1 + "," + iBomCd2 + ")" );
      iClearsMxTables.add( "DELETE FROM " + TableUtil.EQP_ASSMBL_POS + " where eqp_pos_cd like '"
            + iPosPrefix + "%'" );
   }


   @Override
   @After
   public void after() {

      classDataSetup( iClearsMxTables );

      String strTCName = testName.getMethodName();
      if ( strTCName.contains( "testOPER_3037" ) ) {
         clearTables();
         restoreDB();
      }
   }


   /**
    *
    * Test 2 scenarios: Row 1 data staging table: New Tracked item under a Tracked item Row 2 data
    * in staging table:New Tracked item under a SYS (system)
    *
    */
   @Test
   public void testCPartConfigAllFieldsForTRK() {

      System.out.println( "======= Starting: " + testName.getMethodName() + " ========" );

      String lInvClassCd = "'TRK'";
      String lInvClassCd2 = "'TRK'";

      String lPosCt1 = "20";
      String lPosCt2 = "1";
      String lBomFuncCd = "'LOG_CARD'";

      Map<String, String> lCPartConfigMap = new LinkedHashMap<>();
      // 1st entry into staging table
      lCPartConfigMap.put( "ASSMBL_CD", "\'ACFT_CD1\'" );
      lCPartConfigMap.put( "IPC_REF_CD", iBomCd1 );
      lCPartConfigMap.put( "IPC_REF_NAME", "\'Tracked Part\'" );
      lCPartConfigMap.put( "ATA_CD", null );
      lCPartConfigMap.put( "INV_CLASS_CD", lInvClassCd );
      lCPartConfigMap.put( "ASSMBL_BOM_ZONE_CD", "\'Q242\'" );
      lCPartConfigMap.put( "POS_CT", lPosCt1 );
      lCPartConfigMap.put( "POS_NAME_LIST",
            "'A1,A2,A3,A4,A5,A6,A7,A8,A9,A10,A11,A12,A13,A14,A15,A16,A17,A18,A19,A20'" );
      lCPartConfigMap.put( "NH_IPC_REF_CD", "\'ACFT-SYS-1-1-TRK-MULTIPART2\'" );
      lCPartConfigMap.put( "PART_GRP_APPLICABILITY", iPartGroupAppl );
      lCPartConfigMap.put( "LRU_BOOL", "'Y'" );
      lCPartConfigMap.put( "SOFTWARE_BOOL", "\'N\'" );
      lCPartConfigMap.put( "ETOPS_BOOL", "\'N\'" );
      lCPartConfigMap.put( "BOM_FUNC_CD", "\'LOG_CARD\'" );
      for ( int i = 1; i <= 20; i++ ) {
         if ( i < 10 )
            lCPartConfigMap.put( "PN" + i + "_PART_NO_OEM", "'" + iPartPrefix + "0" + i + "'" );
         else
            lCPartConfigMap.put( "PN" + i + "_PART_NO_OEM", "'" + iPartPrefix + i + "'" );
         lCPartConfigMap.put( "PN" + i + "_MANUFACT_REF", iManufactCd );
         lCPartConfigMap.put( "PN" + i + "_PART_NO_SDESC", "'" + iPartPrefix + i + "'" );
         lCPartConfigMap.put( "PN" + i + "_APPLICABILITY_DESC", "'" + iPartAppl + i + "'" );
         lCPartConfigMap.put( "PN" + i + "_INTERCHANGE_ORD", "'" + i + "'" );
         lCPartConfigMap.put( "PN" + i + "_PART_BASELINE_CD", "'" + iBaselineCd + i + "'" );
         if ( i != 1 )
            lCPartConfigMap.put( "PN" + i + "_APPROVED_BOOL", "'N'" );
      }

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_CONFIG, lCPartConfigMap ) );

      // 2nd Entry into staging table
      lCPartConfigMap.clear();
      lCPartConfigMap.put( "ASSMBL_CD", "\'ACFT_CD1\'" );
      lCPartConfigMap.put( "IPC_REF_CD", iBomCd2 );
      lCPartConfigMap.put( "IPC_REF_NAME", "\'Tracked Part\'" );
      lCPartConfigMap.put( "ATA_CD", iBomCd3 );
      lCPartConfigMap.put( "INV_CLASS_CD", lInvClassCd2 );
      lCPartConfigMap.put( "ASSMBL_BOM_ZONE_CD", "\'Q242\'" );
      lCPartConfigMap.put( "POS_CT", lPosCt2 );
      lCPartConfigMap.put( "POS_NAME_LIST", "'A0'" );
      lCPartConfigMap.put( "NH_IPC_REF_CD", null );
      lCPartConfigMap.put( "PART_GRP_APPLICABILITY", iPartGroupAppl );
      lCPartConfigMap.put( "LRU_BOOL", "'N'" );
      lCPartConfigMap.put( "SOFTWARE_BOOL", "\'Y\'" );
      lCPartConfigMap.put( "ETOPS_BOOL", "\'Y\'" );
      lCPartConfigMap.put( "BOM_FUNC_CD", "\'LOG_CARD\'" );
      lCPartConfigMap.put( "PN1_PART_NO_OEM", "'" + iPartPrefix + "00'" );
      lCPartConfigMap.put( "PN1_MANUFACT_REF", iManufactCd );
      lCPartConfigMap.put( "PN1_PART_NO_SDESC", "'" + iPartPrefix + "0'" );
      lCPartConfigMap.put( "PN1_APPLICABILITY_DESC", "'" + iPartAppl + "0'" );
      lCPartConfigMap.put( "PN1_INTERCHANGE_ORD", "'1'" );
      lCPartConfigMap.put( "PN1_PART_BASELINE_CD", "'" + iBaselineCd + "0'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_CONFIG, lCPartConfigMap ) );

      Assert.assertTrue( "Validation error occurred", runValidationPartConfig() == 1 );

      Assert.assertTrue( "Import error occurred", runImportPartConfig() == 1 );

      // Verification
      SetVariables( iPartPrefix, iBomCd1, iBomCd2, iPosPrefix, iBomCd3 );
      VerifyEqpPartBaseline( 21, iBaselineCd, iPartAppl );
      VerifyEqpPartNo( 21, iManufactCd, lInvClassCd, lInvClassCd2, iPartPrefix );
      VerifyEqpAssmblBom( lInvClassCd, lInvClassCd2, iBomCd1, iBomCd2,
            lCPartConfigMap.get( "IPC_REF_NAME" ), lCPartConfigMap.get( "IPC_REF_NAME" ), lPosCt1,
            lPosCt2, lBomFuncCd );
      VerifyEqpAssmblPos( 21, iPosPrefix );
      VerifyEqpBomPart( lInvClassCd, lInvClassCd2, iBomCd1, iBomCd2,
            lCPartConfigMap.get( "IPC_REF_NAME" ), lCPartConfigMap.get( "IPC_REF_NAME" ),
            iPartGroupAppl );
   }


   /**
    *
    * This test case will verify all values in the c_dyn_part_config table are populated within Mx.
    * This
    *
    */
   @Test
   public void testCDynPartConfigforTrkPart() {

      System.out.println( "======= Starting: " + testName.getMethodName() + " ========" );

      String lInvClassCd = "'TRK'";
      String lInvClassCd2 = "'TRK'";

      String lPosCt1 = "21";
      String lPosCt2 = "1";
      String lBomFuncCd = "'LOG_CARD'";

      Map<String, String> lCPartConfigMap = new LinkedHashMap<>();
      // 1st entry into staging table
      lCPartConfigMap.put( "ASSMBL_CD", "\'ACFT_CD1\'" );
      lCPartConfigMap.put( "IPC_REF_CD", iBomCd1 );
      lCPartConfigMap.put( "IPC_REF_NAME", "\'Tracked Part\'" );
      lCPartConfigMap.put( "ATA_CD", null );
      lCPartConfigMap.put( "INV_CLASS_CD", lInvClassCd );
      lCPartConfigMap.put( "ASSMBL_BOM_ZONE_CD", "\'Q242\'" );
      lCPartConfigMap.put( "POS_CT", lPosCt1 );
      lCPartConfigMap.put( "POS_NAME_LIST",
            "'A1,A2,A3,A4,A5,A6,A7,A8,A9,A10,A11,A12,A13,A14,A15,A16,A17,A18,A19,A20,A21'" );
      lCPartConfigMap.put( "NH_IPC_REF_CD", "\'ACFT-SYS-1-1-TRK-MULTIPART2\'" );
      lCPartConfigMap.put( "PART_GRP_APPLICABILITY", iPartGroupAppl );
      lCPartConfigMap.put( "LRU_BOOL", "'Y'" );
      lCPartConfigMap.put( "SOFTWARE_BOOL", "\'N\'" );
      lCPartConfigMap.put( "ETOPS_BOOL", "\'N\'" );
      lCPartConfigMap.put( "BOM_FUNC_CD", "\'LOG_CARD\'" );
      // This will Load 20 parts into the staging table
      for ( int i = 1; i <= 20; i++ ) {
         if ( i < 10 )
            lCPartConfigMap.put( "PN" + i + "_PART_NO_OEM", "'" + iPartPrefix + "0" + i + "'" );
         else
            lCPartConfigMap.put( "PN" + i + "_PART_NO_OEM", "'" + iPartPrefix + i + "'" );
         lCPartConfigMap.put( "PN" + i + "_MANUFACT_REF", iManufactCd );
         lCPartConfigMap.put( "PN" + i + "_PART_NO_SDESC", "'" + iPartPrefix + i + "'" );
         lCPartConfigMap.put( "PN" + i + "_APPLICABILITY_DESC", "'" + iPartAppl + i + "'" );
         lCPartConfigMap.put( "PN" + i + "_INTERCHANGE_ORD", "'" + i + "'" );
         lCPartConfigMap.put( "PN" + i + "_PART_BASELINE_CD", "'" + iBaselineCd + i + "'" );
         if ( i != 1 )
            lCPartConfigMap.put( "PN" + i + "_APPROVED_BOOL", "'N'" );
      }

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_CONFIG, lCPartConfigMap ) );

      // 2nd Entry into staging table
      lCPartConfigMap.clear();
      lCPartConfigMap.put( "ASSMBL_CD", "\'ACFT_CD1\'" );
      lCPartConfigMap.put( "IPC_REF_CD", iBomCd2 );
      lCPartConfigMap.put( "IPC_REF_NAME", "\'Tracked Part\'" );
      lCPartConfigMap.put( "ATA_CD", iBomCd3 );
      lCPartConfigMap.put( "INV_CLASS_CD", lInvClassCd2 );
      lCPartConfigMap.put( "ASSMBL_BOM_ZONE_CD", "\'Q242\'" );
      lCPartConfigMap.put( "POS_CT", lPosCt2 );
      lCPartConfigMap.put( "POS_NAME_LIST", "'A0'" );
      lCPartConfigMap.put( "NH_IPC_REF_CD", null );
      lCPartConfigMap.put( "PART_GRP_APPLICABILITY", iPartGroupAppl );
      lCPartConfigMap.put( "LRU_BOOL", "'N'" );
      lCPartConfigMap.put( "SOFTWARE_BOOL", "\'Y\'" );
      lCPartConfigMap.put( "ETOPS_BOOL", "\'Y\'" );
      lCPartConfigMap.put( "BOM_FUNC_CD", "\'LOG_CARD\'" );
      lCPartConfigMap.put( "PN1_PART_NO_OEM", "'" + iPartPrefix + "00'" );
      lCPartConfigMap.put( "PN1_MANUFACT_REF", iManufactCd );
      lCPartConfigMap.put( "PN1_PART_NO_SDESC", "'" + iPartPrefix + "0'" );
      lCPartConfigMap.put( "PN1_APPLICABILITY_DESC", "'" + iPartAppl + "0'" );
      lCPartConfigMap.put( "PN1_INTERCHANGE_ORD", "'1'" );
      lCPartConfigMap.put( "PN1_PART_BASELINE_CD", "'" + iBaselineCd + "0'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_CONFIG, lCPartConfigMap ) );

      Map<String, String> lCDynPartConfigMap = new LinkedHashMap<>();

      lCDynPartConfigMap.put( "ASSMBL_CD", "\'ACFT_CD1\'" );
      lCDynPartConfigMap.put( "IPC_REF_CD", iBomCd1 );
      lCDynPartConfigMap.put( "PART_NO_OEM", "'" + iPartPrefix + "21'" );
      lCDynPartConfigMap.put( "MANUFACT_REF", iManufactCd );
      lCDynPartConfigMap.put( "PART_NO_SDESC", "'" + iPartPrefix + "21'" );
      lCDynPartConfigMap.put( "APPLICABILITY_DESC", "'" + iPartAppl + "21'" );
      lCDynPartConfigMap.put( "INTERCHANGE_ORD", "'21'" );
      lCDynPartConfigMap.put( "PART_BASELINE_CD", "'" + iBaselineCd + "21'" );
      // Note: intentionally left APPROVED_BOOL blank to verify the Y default value

      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_DYN_PART_CONFIG, lCDynPartConfigMap ) );

      Assert.assertTrue( "Validation error occurred", runValidationPartConfig() == 1 );

      Assert.assertTrue( "Import error occurred", runImportPartConfig() == 1 );

      // Verification
      SetVariables( iPartPrefix, iBomCd1, iBomCd2, iPosPrefix, iBomCd3 );
      VerifyEqpPartBaseline( 22, iBaselineCd, iPartAppl );
      VerifyEqpPartNo( 22, iManufactCd, lInvClassCd, lInvClassCd2, iPartPrefix );
      VerifyEqpAssmblBom( lInvClassCd, lInvClassCd2, iBomCd1, iBomCd2,
            lCPartConfigMap.get( "IPC_REF_NAME" ), lCPartConfigMap.get( "IPC_REF_NAME" ), lPosCt1,
            lPosCt2, lBomFuncCd );
      VerifyEqpAssmblPos( 22, iPosPrefix );
      VerifyEqpBomPart( lInvClassCd, lInvClassCd2, iBomCd1, iBomCd2,
            lCPartConfigMap.get( "IPC_REF_NAME" ), lCPartConfigMap.get( "IPC_REF_NAME" ),
            iPartGroupAppl );
   }


   /**
    * This test will test a New Untracked (SER) item against a Tracked item
    *
    */
   @Test
   public void testCPartConfigForSerializedPart() {

      System.out.println( "======= Starting: " + testName.getMethodName() + " ========" );

      String lInvClassCd = "'SER'";
      String lInvClassCd2 = "'TRK'";

      String lPosCt1 = "1";
      String lPosCt2 = "1";
      String lBomFuncCd = "'LOG_CARD'";

      Map<String, String> lCPartConfigMap = new LinkedHashMap<>();
      // 1st entry into staging table
      lCPartConfigMap.put( "ASSMBL_CD", "\'ACFT_CD1\'" );
      lCPartConfigMap.put( "IPC_REF_CD", iBomCd1 );
      lCPartConfigMap.put( "IPC_REF_NAME", "\'Tracked Part\'" );
      lCPartConfigMap.put( "ATA_CD", null );
      lCPartConfigMap.put( "INV_CLASS_CD", lInvClassCd );
      lCPartConfigMap.put( "ASSMBL_BOM_ZONE_CD", "\'Q242\'" );
      lCPartConfigMap.put( "POS_CT", lPosCt1 );
      lCPartConfigMap.put( "POS_NAME_LIST", "'A1'" );
      lCPartConfigMap.put( "NH_IPC_REF_CD", "\'ACFT-SYS-1-1-TRK-MULTIPART2\'" );
      lCPartConfigMap.put( "PART_GRP_APPLICABILITY", iPartGroupAppl );
      lCPartConfigMap.put( "LRU_BOOL", "'Y'" );
      for ( int i = 1; i <= 1; i++ ) {
         if ( i < 10 )
            lCPartConfigMap.put( "PN" + i + "_PART_NO_OEM", "'" + iPartPrefix + "0" + i + "'" );
         else
            lCPartConfigMap.put( "PN" + i + "_PART_NO_OEM", "'" + iPartPrefix + i + "'" );
         lCPartConfigMap.put( "PN" + i + "_MANUFACT_REF", iManufactCd );
         lCPartConfigMap.put( "PN" + i + "_PART_NO_SDESC", "'" + iPartPrefix + i + "'" );
         lCPartConfigMap.put( "PN" + i + "_APPLICABILITY_DESC", "'" + iPartAppl + i + "'" );
         lCPartConfigMap.put( "PN" + i + "_INTERCHANGE_ORD", "'" + i + "'" );
         lCPartConfigMap.put( "PN" + i + "_PART_BASELINE_CD", "'" + iBaselineCd + i + "'" );
         if ( i != 1 )
            lCPartConfigMap.put( "PN" + i + "_APPROVED_BOOL", "'N'" );
      }

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_CONFIG, lCPartConfigMap ) );

      // 2nd Entry into staging table
      lCPartConfigMap.clear();
      lCPartConfigMap.put( "ASSMBL_CD", "\'ACFT_CD1\'" );
      lCPartConfigMap.put( "IPC_REF_CD", iBomCd2 );
      lCPartConfigMap.put( "IPC_REF_NAME", "\'Tracked Part\'" );
      lCPartConfigMap.put( "ATA_CD", iBomCd3 );
      lCPartConfigMap.put( "INV_CLASS_CD", lInvClassCd2 );
      lCPartConfigMap.put( "ASSMBL_BOM_ZONE_CD", "\'Q242\'" );
      lCPartConfigMap.put( "POS_CT", lPosCt2 );
      lCPartConfigMap.put( "POS_NAME_LIST", "'A0'" );
      lCPartConfigMap.put( "NH_IPC_REF_CD", null );
      lCPartConfigMap.put( "PART_GRP_APPLICABILITY", iPartGroupAppl );
      lCPartConfigMap.put( "LRU_BOOL", "'N'" );
      lCPartConfigMap.put( "ETOPS_BOOL", "\'Y\'" );
      lCPartConfigMap.put( "SOFTWARE_BOOL", "\'Y\'" );
      lCPartConfigMap.put( "BOM_FUNC_CD", "\'LOG_CARD\'" );
      lCPartConfigMap.put( "PN1_PART_NO_OEM", "'" + iPartPrefix + "00'" );
      lCPartConfigMap.put( "PN1_MANUFACT_REF", iManufactCd );
      lCPartConfigMap.put( "PN1_PART_NO_SDESC", "'" + iPartPrefix + "0'" );
      lCPartConfigMap.put( "PN1_APPLICABILITY_DESC", "'" + iPartAppl + "0'" );
      lCPartConfigMap.put( "PN1_INTERCHANGE_ORD", "'1'" );
      lCPartConfigMap.put( "PN1_PART_BASELINE_CD", "'" + iBaselineCd + "0'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_CONFIG, lCPartConfigMap ) );

      Assert.assertTrue( "Validation error occurred", runValidationPartConfig() == 1 );

      Assert.assertTrue( "Import error occurred", runImportPartConfig() == 1 );

      SetVariables( iPartPrefix, iBomCd1, iBomCd2, iPosPrefix, iBomCd3 );
      VerifyEqpPartBaseline( 2, iBaselineCd, iPartAppl );
      VerifyEqpPartNo( 2, iManufactCd, lInvClassCd, lInvClassCd2, iPartPrefix );
      VerifyEqpAssmblBom( lInvClassCd, lInvClassCd2, iBomCd1, iBomCd2,
            lCPartConfigMap.get( "IPC_REF_NAME" ), lCPartConfigMap.get( "IPC_REF_NAME" ), lPosCt1,
            lPosCt2, lBomFuncCd );
      VerifyEqpAssmblPos( 1, iPosPrefix ); // Only for TRK part
      VerifyEqpBomPart( lInvClassCd, lInvClassCd2, iBomCd1, iBomCd2,
            lCPartConfigMap.get( "IPC_REF_NAME" ), lCPartConfigMap.get( "IPC_REF_NAME" ),
            iPartGroupAppl );
   }


   /**
    *
    * Test New Untracked item (batch) against a SYS (system) slot
    *
    */
   @Test
   public void testCPartConfigForBatchPart() {

      System.out.println( "======= Starting: " + testName.getMethodName() + " ========" );

      String lInvClassCd = "'BATCH'";
      String lPosCt2 = "1";

      Map<String, String> lCPartConfigMap = new LinkedHashMap<>();

      lCPartConfigMap.put( "ASSMBL_CD", "\'ACFT_CD1\'" );
      lCPartConfigMap.put( "IPC_REF_CD", iBomCd2 );
      lCPartConfigMap.put( "IPC_REF_NAME", "\'Tracked Part\'" );
      lCPartConfigMap.put( "ATA_CD", iBomCd3 );
      lCPartConfigMap.put( "INV_CLASS_CD", "'BATCH'" );
      lCPartConfigMap.put( "ASSMBL_BOM_ZONE_CD", "\'Q242\'" );
      lCPartConfigMap.put( "POS_CT", lPosCt2 );
      lCPartConfigMap.put( "POS_NAME_LIST", "'A0'" );
      lCPartConfigMap.put( "NH_IPC_REF_CD", null );
      lCPartConfigMap.put( "PART_GRP_APPLICABILITY", iPartGroupAppl );
      lCPartConfigMap.put( "LRU_BOOL", "'N'" );
      lCPartConfigMap.put( "PN1_PART_NO_OEM", "'" + iPartPrefix + "00'" );
      lCPartConfigMap.put( "PN1_MANUFACT_REF", iManufactCd );
      lCPartConfigMap.put( "PN1_PART_NO_SDESC", "'" + iPartPrefix + "0'" );
      lCPartConfigMap.put( "PN1_APPLICABILITY_DESC", "'" + iPartAppl + "0'" );
      lCPartConfigMap.put( "PN1_INTERCHANGE_ORD", "'1'" );
      lCPartConfigMap.put( "PN1_PART_BASELINE_CD", "'" + iBaselineCd + "0'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_CONFIG, lCPartConfigMap ) );

      Assert.assertTrue( "Validation error occurred", runValidationPartConfig() == 1 );

      Assert.assertTrue( "Import error occurred", runImportPartConfig() == 1 );

      SetVariables( iPartPrefix, null, iBomCd2, iPosPrefix, iBomCd3 );
      VerifyEqpPartBaseline( 1, iBaselineCd, iPartAppl );
      VerifyEqpPartNo( 1, iManufactCd, null, lInvClassCd, iPartPrefix );
      VerifyEqpAssmblBom( iBomCd2 );
      VerifyEqpAssmblPos( iPosPrefix );
      VerifyEqpBomPart( null, lInvClassCd, null, iBomCd2, lCPartConfigMap.get( "IPC_REF_NAME" ),
            lCPartConfigMap.get( "IPC_REF_NAME" ), iPartGroupAppl );

   }


   /**
    *
    * Test Untracked item (BATCH) against the ROOT of an assembly
    *
    */
   @Test
   public void testCPartConfigforBatchPartLinkedToRootAssembly() {

      System.out.println( "======= Starting: " + testName.getMethodName() + " ========" );

      String lInvClassCd = "'BATCH'";
      String lPosCt2 = "1";

      Map<String, String> lCPartConfigMap = new LinkedHashMap<>();

      lCPartConfigMap.put( "ASSMBL_CD", "\'ACFT_CD1\'" );
      lCPartConfigMap.put( "IPC_REF_CD", iBomCd2 );
      lCPartConfigMap.put( "IPC_REF_NAME", "\'Tracked Part\'" );
      lCPartConfigMap.put( "ATA_CD", null );
      lCPartConfigMap.put( "INV_CLASS_CD", "'BATCH'" );
      lCPartConfigMap.put( "ASSMBL_BOM_ZONE_CD", "\'Q242\'" );
      lCPartConfigMap.put( "POS_CT", lPosCt2 );
      lCPartConfigMap.put( "POS_NAME_LIST", "'A0'" );
      lCPartConfigMap.put( "NH_IPC_REF_CD", null );
      lCPartConfigMap.put( "PART_GRP_APPLICABILITY", iPartGroupAppl );
      lCPartConfigMap.put( "LRU_BOOL", "'N'" );
      lCPartConfigMap.put( "PN1_PART_NO_OEM", "'" + iPartPrefix + "00'" );
      lCPartConfigMap.put( "PN1_MANUFACT_REF", iManufactCd );
      lCPartConfigMap.put( "PN1_PART_NO_SDESC", "'" + iPartPrefix + "0'" );
      lCPartConfigMap.put( "PN1_APPLICABILITY_DESC", "'" + iPartAppl + "0'" );
      lCPartConfigMap.put( "PN1_INTERCHANGE_ORD", "'1'" );
      lCPartConfigMap.put( "PN1_PART_BASELINE_CD", "'" + iBaselineCd + "0'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_CONFIG, lCPartConfigMap ) );

      Assert.assertTrue( "Validation error occurred", runValidationPartConfig() == 1 );

      Assert.assertTrue( "Import error occurred", runImportPartConfig() == 1 );

      // Verification
      SetVariables( iPartPrefix, null, iBomCd2, iPosPrefix, null );
      VerifyEqpPartBaseline( 1, iBaselineCd, iPartAppl );
      VerifyEqpPartNo( 1, iManufactCd, null, lInvClassCd, iPartPrefix );
      VerifyEqpAssmblBom( iBomCd2 );
      VerifyEqpAssmblPos( iPosPrefix );
      VerifyEqpBomPart( null, lInvClassCd, null, iBomCd2, lCPartConfigMap.get( "IPC_REF_NAME" ),
            lCPartConfigMap.get( "IPC_REF_NAME" ), iPartGroupAppl );

   }


   /**
    *
    * This test case is to verify OPER-18091 fix: Baseline Loader should allow mismatched part
    * applicability and part group applicability. inv_class_cd=TRK, group applicability=002-005,
    * part applicability=001
    *
    *
    */

   @Test
   public void testOPER_18091_1() {
      System.out.println( "======= Starting: " + testName.getMethodName() + " ========" );
      Map<String, String> lCPartConfigMap = new LinkedHashMap<>();

      lCPartConfigMap.put( "ASSMBL_CD", "\'ACFT_T1\'" );
      lCPartConfigMap.put( "IPC_REF_CD", "'SYS-ENG1-TRK'" );
      lCPartConfigMap.put( "IPC_REF_NAME", "'SYS-ENG1-TRK'" );
      lCPartConfigMap.put( "ATA_CD", "'SYS-ENG1'" );
      lCPartConfigMap.put( "INV_CLASS_CD", "'TRK'" );
      lCPartConfigMap.put( "ASSMBL_BOM_ZONE_CD", null );
      lCPartConfigMap.put( "POS_CT", "'1'" );
      lCPartConfigMap.put( "POS_NAME_LIST", "'1'" );
      lCPartConfigMap.put( "NH_IPC_REF_CD", null );
      lCPartConfigMap.put( "PART_GRP_APPLICABILITY", "'001-005'" );
      lCPartConfigMap.put( "LRU_BOOL", "'Y'" );
      lCPartConfigMap.put( "PN1_PART_NO_OEM", "'AUTO001'" );
      lCPartConfigMap.put( "PN1_MANUFACT_REF", "'11111'" );
      lCPartConfigMap.put( "PN1_PART_NO_SDESC", "'AUTO001'" );
      lCPartConfigMap.put( "PN1_APPLICABILITY_DESC", "'001'" );
      lCPartConfigMap.put( "PN1_INTERCHANGE_ORD", "'1'" );
      lCPartConfigMap.put( "PN1_PART_BASELINE_CD", null );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_CONFIG, lCPartConfigMap ) );
      Assert.assertTrue( "Validation error occurred", runValidationPartConfig() == 1 );

   }


   /**
    *
    * This test case is to verify OPER-18091 fix: Baseline Loader should allow mismatched part
    * applicability and part group applicability. inv_class_cd=TRK, group applicability=002-005,
    * part applicability=005
    *
    *
    */

   @Test
   public void testOPER_18091_2() {
      System.out.println( "======= Starting: " + testName.getMethodName() + " ========" );
      Map<String, String> lCPartConfigMap = new LinkedHashMap<>();

      lCPartConfigMap.put( "ASSMBL_CD", "\'ACFT_T1\'" );
      lCPartConfigMap.put( "IPC_REF_CD", "'SYS-ENG1-TRK'" );
      lCPartConfigMap.put( "IPC_REF_NAME", "'SYS-ENG1-TRK'" );
      lCPartConfigMap.put( "ATA_CD", "'SYS-ENG1'" );
      lCPartConfigMap.put( "INV_CLASS_CD", "'TRK'" );
      lCPartConfigMap.put( "ASSMBL_BOM_ZONE_CD", null );
      lCPartConfigMap.put( "POS_CT", "'1'" );
      lCPartConfigMap.put( "POS_NAME_LIST", "'1'" );
      lCPartConfigMap.put( "NH_IPC_REF_CD", null );
      lCPartConfigMap.put( "PART_GRP_APPLICABILITY", "'001-005'" );
      lCPartConfigMap.put( "LRU_BOOL", "'Y'" );
      lCPartConfigMap.put( "PN1_PART_NO_OEM", "'AUTO001'" );
      lCPartConfigMap.put( "PN1_MANUFACT_REF", "'11111'" );
      lCPartConfigMap.put( "PN1_PART_NO_SDESC", "'AUTO001'" );
      lCPartConfigMap.put( "PN1_APPLICABILITY_DESC", "'005'" );
      lCPartConfigMap.put( "PN1_INTERCHANGE_ORD", "'1'" );
      lCPartConfigMap.put( "PN1_PART_BASELINE_CD", null );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_CONFIG, lCPartConfigMap ) );
      Assert.assertTrue( "Validation error occurred", runValidationPartConfig() == 1 );

   }


   /**
    *
    * This test is to verify OPER-19987: C_PART_CONFIG.INTERCHANGE_ORD must be NULL or one for this
    * batch part.
    *
    */
   @Test
   public void testOPER_19987() {

      System.out.println( "======= Starting: " + testName.getMethodName() + " ========" );

      String lInvClassCd = "'BATCH'";
      String lPosCt2 = "1";

      Map<String, String> lCPartConfigMap = new LinkedHashMap<>();

      lCPartConfigMap.put( "ASSMBL_CD", "\'ACFT_CD1\'" );
      lCPartConfigMap.put( "IPC_REF_CD", iBomCd2 );
      lCPartConfigMap.put( "IPC_REF_NAME", "\'Tracked Part\'" );
      lCPartConfigMap.put( "ATA_CD", iBomCd3 );
      lCPartConfigMap.put( "INV_CLASS_CD", "'BATCH'" );
      lCPartConfigMap.put( "ASSMBL_BOM_ZONE_CD", "\'Q242\'" );
      lCPartConfigMap.put( "POS_CT", lPosCt2 );
      lCPartConfigMap.put( "POS_NAME_LIST", "'A0'" );
      lCPartConfigMap.put( "NH_IPC_REF_CD", null );
      lCPartConfigMap.put( "PART_GRP_APPLICABILITY", iPartGroupAppl );
      lCPartConfigMap.put( "LRU_BOOL", "'N'" );
      lCPartConfigMap.put( "PN1_PART_NO_OEM", "'" + iPartPrefix + "00'" );
      lCPartConfigMap.put( "PN1_MANUFACT_REF", iManufactCd );
      lCPartConfigMap.put( "PN1_PART_NO_SDESC", "'" + iPartPrefix + "0'" );
      lCPartConfigMap.put( "PN1_APPLICABILITY_DESC", "'" + iPartAppl + "0'" );
      lCPartConfigMap.put( "PN1_INTERCHANGE_ORD", null );
      lCPartConfigMap.put( "PN1_PART_BASELINE_CD", "'" + iBaselineCd + "0'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_CONFIG, lCPartConfigMap ) );

      Assert.assertTrue( "Validation error occurred", runValidationPartConfig() == 1 );
   }


   /**
    *
    * This test is to verify CFGPRT-12014: C_PART_CONFIG.INTERCHANGE_ORD must be NULL or one for
    * this batch part.
    *
    */
   @Test
   public void testCFGPRT_12014_1() {

      System.out.println( "======= Starting: " + testName.getMethodName() + " ========" );

      String lInvClassCd = "'BATCH'";
      String lPosCt2 = "1";

      Map<String, String> lCPartConfigMap = new LinkedHashMap<>();

      lCPartConfigMap.put( "ASSMBL_CD", "\'ACFT_CD1\'" );
      lCPartConfigMap.put( "IPC_REF_CD", iBomCd2 );
      lCPartConfigMap.put( "IPC_REF_NAME", "\'Tracked Part\'" );
      lCPartConfigMap.put( "ATA_CD", iBomCd3 );
      lCPartConfigMap.put( "INV_CLASS_CD", "'BATCH'" );
      lCPartConfigMap.put( "ASSMBL_BOM_ZONE_CD", "\'Q242\'" );
      lCPartConfigMap.put( "POS_CT", lPosCt2 );
      lCPartConfigMap.put( "POS_NAME_LIST", "'A0'" );
      lCPartConfigMap.put( "NH_IPC_REF_CD", null );
      lCPartConfigMap.put( "PART_GRP_APPLICABILITY", iPartGroupAppl );
      lCPartConfigMap.put( "LRU_BOOL", "'N'" );
      lCPartConfigMap.put( "PN1_PART_NO_OEM", "'" + iPartPrefix + "00'" );
      lCPartConfigMap.put( "PN1_MANUFACT_REF", iManufactCd );
      lCPartConfigMap.put( "PN1_PART_NO_SDESC", "'" + iPartPrefix + "0'" );
      lCPartConfigMap.put( "PN1_APPLICABILITY_DESC", "'" + iPartAppl + "0'" );
      lCPartConfigMap.put( "PN1_INTERCHANGE_ORD", "'100'" );
      lCPartConfigMap.put( "PN1_PART_BASELINE_CD", "'" + iBaselineCd + "0'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_CONFIG, lCPartConfigMap ) );
      // run validation
      Assert.assertTrue( "Validation error occurred", runValidationPartConfig() != 1 );
      // check error code
      checkPARTErrorCode( "testCFGPRT_1201", "CFGPRT-12014" );

   }


   /**
    *
    * This test is to verify CFGPRT-12014: C_PART_CONFIG.INTERCHANGE_ORD must be NULL or one for
    * this batch part.
    *
    */
   @Test
   public void testCFGPRT_12014_2() {

      System.out.println( "======= Starting: " + testName.getMethodName() + " ========" );

      String lInvClassCd = "'BATCH'";
      String lPosCt2 = "1";

      Map<String, String> lCPartConfigMap = new LinkedHashMap<>();

      lCPartConfigMap.put( "ASSMBL_CD", "\'ACFT_CD1\'" );
      lCPartConfigMap.put( "IPC_REF_CD", iBomCd2 );
      lCPartConfigMap.put( "IPC_REF_NAME", "\'Tracked Part\'" );
      lCPartConfigMap.put( "ATA_CD", iBomCd3 );
      lCPartConfigMap.put( "INV_CLASS_CD", "'BATCH'" );
      lCPartConfigMap.put( "ASSMBL_BOM_ZONE_CD", "\'Q242\'" );
      lCPartConfigMap.put( "POS_CT", lPosCt2 );
      lCPartConfigMap.put( "POS_NAME_LIST", "'A0'" );
      lCPartConfigMap.put( "NH_IPC_REF_CD", null );
      lCPartConfigMap.put( "PART_GRP_APPLICABILITY", iPartGroupAppl );
      lCPartConfigMap.put( "LRU_BOOL", "'N'" );
      lCPartConfigMap.put( "PN1_PART_NO_OEM", "'" + iPartPrefix + "00'" );
      lCPartConfigMap.put( "PN1_MANUFACT_REF", iManufactCd );
      lCPartConfigMap.put( "PN1_PART_NO_SDESC", "'" + iPartPrefix + "0'" );
      lCPartConfigMap.put( "PN1_APPLICABILITY_DESC", "'" + iPartAppl + "0'" );
      lCPartConfigMap.put( "PN1_INTERCHANGE_ORD", "'1'" );
      lCPartConfigMap.put( "PN1_PART_BASELINE_CD", "'" + iBaselineCd + "0'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_CONFIG, lCPartConfigMap ) );

      lCPartConfigMap.clear();
      lCPartConfigMap.put( "ASSMBL_CD", "\'ACFT_CD1\'" );
      lCPartConfigMap.put( "IPC_REF_CD", iBomCd2 );
      lCPartConfigMap.put( "PART_NO_OEM", "'" + iPartPrefix + "01'" );
      lCPartConfigMap.put( "MANUFACT_REF", iManufactCd );
      lCPartConfigMap.put( "PART_NO_SDESC", "'" + iPartPrefix + "01'" );
      lCPartConfigMap.put( "APPLICABILITY_DESC", "'" + iPartAppl + "01'" );
      lCPartConfigMap.put( "INTERCHANGE_ORD", "'100'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_DYN_PART_CONFIG, lCPartConfigMap ) );
      // run validation
      Assert.assertTrue( "Validation error occurred", runValidationPartConfig() != 1 );
      // check error code
      checkPARTErrorCode( "testCFGPRT_1201", "CFGPRT-12014" );

   }


   /**
    *
    * This test case is to verify OPER-3037 position per parent: The final position names are a
    * concatenation of the parent name and the child name.
    *
    *
    */

   public void testOPER_3037_PositionsPerParent_VALIDATION() {
      System.out.println( "======= Starting: " + testName.getMethodName() + " ========" );
      Map<String, String> lCPartConfigMap = new LinkedHashMap<>();
      // first record
      lCPartConfigMap.put( "ASSMBL_CD", "'ACFT_12'" );
      lCPartConfigMap.put( "IPC_REF_CD", "'ACFT12-SYS-1-TRK'" );
      lCPartConfigMap.put( "IPC_REF_NAME", "'ACFT12-SYS-1-TRK'" );
      lCPartConfigMap.put( "ATA_CD", "'ACFT12-SYS-1'" );
      lCPartConfigMap.put( "INV_CLASS_CD", "'TRK'" );
      lCPartConfigMap.put( "POS_CT", "'2'" );
      lCPartConfigMap.put( "POS_NAME_LIST", "'FWD,AFT'" );
      lCPartConfigMap.put( "PN1_PART_NO_OEM", "'AUTO001'" );
      lCPartConfigMap.put( "PN1_MANUFACT_REF", "'11111'" );
      lCPartConfigMap.put( "PN1_PART_NO_SDESC", "'AUTO001'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_CONFIG, lCPartConfigMap ) );
      // Second record
      lCPartConfigMap.clear();
      lCPartConfigMap.put( "ASSMBL_CD", "'ACFT_12'" );
      lCPartConfigMap.put( "IPC_REF_CD", "'ACFT12-SYS-1-TRK-TRK'" );
      lCPartConfigMap.put( "IPC_REF_NAME", "'ACFT12-SYS-1-TRK-TRK'" );
      lCPartConfigMap.put( "INV_CLASS_CD", "'TRK'" );
      lCPartConfigMap.put( "POS_CT", "'3'" );
      lCPartConfigMap.put( "POS_NAME_LIST", "'LEFT,MID,RIGHT'" );
      lCPartConfigMap.put( "NH_IPC_REF_CD", "'ACFT12-SYS-1-TRK'" );
      lCPartConfigMap.put( "PN1_PART_NO_OEM", "'AUTO002'" );
      lCPartConfigMap.put( "PN1_MANUFACT_REF", "'11111'" );
      lCPartConfigMap.put( "PN1_PART_NO_SDESC", "'AUTO002'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_CONFIG, lCPartConfigMap ) );

      Assert.assertTrue( "Validation error occurred", runValidationPartConfig() == 1 );

   }


   /**
    *
    * This test case is to verify OPER-3037 position per parent: The final position names are a
    * concatenation of the parent name and the child name.
    *
    *
    */

   @Test
   public void testOPER_3037_PositionsPerParent_IMPORT() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testOPER_3037_PositionsPerParent_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( "Import error occurred", runImportPartConfig() == 1 );

      // Verify eqp_assmbl_bom table import
      String lQuery = "select 1 from " + TableUtil.EQP_ASSMBL_BOM + " where ASSMBL_CD='ACFT_12'"
            + " and ASSMBL_BOM_CD='ACFT12-SYS-1-TRK'";
      Assert.assertTrue( "Check eqp_assmbl_bom table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.EQP_ASSMBL_BOM + " where ASSMBL_CD='ACFT_12'"
            + " and ASSMBL_BOM_CD='ACFT12-SYS-1-TRK-TRK'";
      Assert.assertTrue( "Check eqp_assmbl_bom table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      // Verify eqp_assmbl_pos table import
      checkTables( TableUtil.EQP_ASSMBL_POS, "AFT" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "FWD" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "AFT-LEFT" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "AFT-RIGHT" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "AFT-MID" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "FWD-LEFT" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "FWD-RIGHT" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "FWD-MID" );

      // verify eqp_bom_part
      simpleIDs parentBOMID = getBOMIDs( "ACFT_12", "TRK", "ACFT12-SYS-1-TRK", "ACFT12-SYS-1-TRK" );
      simpleIDs childBOMID =
            getBOMIDs( "ACFT_12", "TRK", "ACFT12-SYS-1-TRK-TRK", "ACFT12-SYS-1-TRK-TRK" );

      Assert.assertTrue( "check parent bom id is not null", parentBOMID != null );
      Assert.assertTrue( "check child bom id is not null", childBOMID != null );

      // verify eqp_prt_baseline table
      lQuery = "select 1 from " + TableUtil.EQP_PART_BASELINE + " where BOM_PART_DB_ID='"
            + parentBOMID.getNO_DB_ID() + "' and BOM_PART_ID='" + parentBOMID.getNO_ID() + "'";
      Assert.assertTrue( "Check EQP_PART_BASELINE table to verify the record exists: 1",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.EQP_PART_BASELINE + " where BOM_PART_DB_ID='"
            + childBOMID.getNO_DB_ID() + "' and BOM_PART_ID='" + childBOMID.getNO_ID() + "'";
      Assert.assertTrue( "Check EQP_PART_BASELINE table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      // verify eqp_part_no table
      simpleIDs parentPARTID = checkEqpPartNoTable( "TRK", "11111", "AUTO001", "AUTO001" );
      simpleIDs chilePARTID = checkEqpPartNoTable( "TRK", "11111", "AUTO002", "AUTO002" );
      Assert.assertTrue( "check parent part no id is not null", parentPARTID != null );
      Assert.assertTrue( "check child part no id is not null", chilePARTID != null );

   }


   /**
    *
    * This test case is to verify OPER-3037 position per assmbly: The child names are explicitly
    * provided for each position and are loaded without modification.
    *
    *
    */

   public void testOPER_3037_PositionsPerAssmbly_VALIDATION() {
      System.out.println( "======= Starting: " + testName.getMethodName() + " ========" );
      Map<String, String> lCPartConfigMap = new LinkedHashMap<>();
      // first record
      lCPartConfigMap.put( "ASSMBL_CD", "'ACFT_12'" );
      lCPartConfigMap.put( "IPC_REF_CD", "'ACFT12-SYS-1-TRK'" );
      lCPartConfigMap.put( "IPC_REF_NAME", "'ACFT12-SYS-1-TRK'" );
      lCPartConfigMap.put( "ATA_CD", "'ACFT12-SYS-1'" );
      lCPartConfigMap.put( "INV_CLASS_CD", "'TRK'" );
      lCPartConfigMap.put( "POS_CT", "'2'" );
      lCPartConfigMap.put( "POS_NAME_LIST", "'FWD,AFT'" );
      lCPartConfigMap.put( "PN1_PART_NO_OEM", "'AUTO001'" );
      lCPartConfigMap.put( "PN1_MANUFACT_REF", "'11111'" );
      lCPartConfigMap.put( "PN1_PART_NO_SDESC", "'AUTO001'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_CONFIG, lCPartConfigMap ) );

      // Second record
      lCPartConfigMap.clear();
      lCPartConfigMap.put( "ASSMBL_CD", "'ACFT_12'" );
      lCPartConfigMap.put( "IPC_REF_CD", "'ACFT12-SYS-1-TRK-TRK'" );
      lCPartConfigMap.put( "IPC_REF_NAME", "'ACFT12-SYS-1-TRK-TRK'" );
      lCPartConfigMap.put( "INV_CLASS_CD", "'TRK'" );
      lCPartConfigMap.put( "POS_CT", "'3'" );
      lCPartConfigMap.put( "POS_NAME_LIST",
            "'FWD LEFT,FWD MID,FWD RIGHT, AFT LEFT, AFT MID, AFT RIGHT'" );
      lCPartConfigMap.put( "NH_IPC_REF_CD", "'ACFT12-SYS-1-TRK'" );
      lCPartConfigMap.put( "PN1_PART_NO_OEM", "'AUTO002'" );
      lCPartConfigMap.put( "PN1_MANUFACT_REF", "'11111'" );
      lCPartConfigMap.put( "PN1_PART_NO_SDESC", "'AUTO002'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_CONFIG, lCPartConfigMap ) );

      Assert.assertTrue( "Validation error occurred", runValidationPartConfig() == 1 );

   }


   /**
    *
    * This test case is to verify OPER-3037 position per assmbly: The child names are explicitly
    * provided for each position and are loaded without modification.
    *
    *
    */

   @Test
   public void testOPER_3037_PositionsPerAssmbly_IMPORT() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testOPER_3037_PositionsPerAssmbly_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( "Import error occurred", runImportPartConfig() == 1 );

      // Verify eqp_assmbl_bom table import
      String lQuery = "select 1 from " + TableUtil.EQP_ASSMBL_BOM + " where ASSMBL_CD='ACFT_12'"
            + " and ASSMBL_BOM_CD='ACFT12-SYS-1-TRK'";
      Assert.assertTrue( "Check eqp_assmbl_bom table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.EQP_ASSMBL_BOM + " where ASSMBL_CD='ACFT_12'"
            + " and ASSMBL_BOM_CD='ACFT12-SYS-1-TRK-TRK'";
      Assert.assertTrue( "Check eqp_assmbl_bom table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      // Verify eqp_assmbl_pos table import
      checkTables( TableUtil.EQP_ASSMBL_POS, "AFT" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "FWD" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "AFT LEFT" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "AFT RIGHT" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "AFT MID" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "FWD LEFT" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "FWD RIGHT" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "FWD MID" );

      // verify eqp_bom_part
      simpleIDs parentBOMID = getBOMIDs( "ACFT_12", "TRK", "ACFT12-SYS-1-TRK", "ACFT12-SYS-1-TRK" );
      simpleIDs childBOMID =
            getBOMIDs( "ACFT_12", "TRK", "ACFT12-SYS-1-TRK-TRK", "ACFT12-SYS-1-TRK-TRK" );

      Assert.assertTrue( "check parent bom id is not null", parentBOMID != null );
      Assert.assertTrue( "check child bom id is not null", childBOMID != null );

      // verify eqp_prt_baseline table
      lQuery = "select 1 from " + TableUtil.EQP_PART_BASELINE + " where BOM_PART_DB_ID='"
            + parentBOMID.getNO_DB_ID() + "' and BOM_PART_ID='" + parentBOMID.getNO_ID() + "'";
      Assert.assertTrue( "Check EQP_PART_BASELINE table to verify the record exists: 1",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.EQP_PART_BASELINE + " where BOM_PART_DB_ID='"
            + childBOMID.getNO_DB_ID() + "' and BOM_PART_ID='" + childBOMID.getNO_ID() + "'";
      Assert.assertTrue( "Check EQP_PART_BASELINE table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      // verify eqp_part_no table
      simpleIDs parentPARTID = checkEqpPartNoTable( "TRK", "11111", "AUTO001", "AUTO001" );
      simpleIDs chilePARTID = checkEqpPartNoTable( "TRK", "11111", "AUTO002", "AUTO002" );
      Assert.assertTrue( "check parent part no id is not null", parentPARTID != null );
      Assert.assertTrue( "check child part no id is not null", chilePARTID != null );

   }


   /**
    *
    * This test case is to verify OPER-3037 auto creation: pos_name_list for parent and child are
    * empty
    *
    *
    */

   public void testOPER_3037_AUTOCREATION_1_VALIDATION() {
      System.out.println( "======= Starting: " + testName.getMethodName() + " ========" );
      Map<String, String> lCPartConfigMap = new LinkedHashMap<>();
      // first record
      lCPartConfigMap.put( "ASSMBL_CD", "'ACFT_12'" );
      lCPartConfigMap.put( "IPC_REF_CD", "'ACFT12-SYS-1-TRK'" );
      lCPartConfigMap.put( "IPC_REF_NAME", "'ACFT12-SYS-1-TRK'" );
      lCPartConfigMap.put( "ATA_CD", "'ACFT12-SYS-1'" );
      lCPartConfigMap.put( "INV_CLASS_CD", "'TRK'" );
      lCPartConfigMap.put( "POS_CT", "'2'" );
      lCPartConfigMap.put( "POS_NAME_LIST", null );
      lCPartConfigMap.put( "PN1_PART_NO_OEM", "'AUTO001'" );
      lCPartConfigMap.put( "PN1_MANUFACT_REF", "'11111'" );
      lCPartConfigMap.put( "PN1_PART_NO_SDESC", "'AUTO001'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_CONFIG, lCPartConfigMap ) );

      // Second record
      lCPartConfigMap.clear();
      lCPartConfigMap.put( "ASSMBL_CD", "'ACFT_12'" );
      lCPartConfigMap.put( "IPC_REF_CD", "'ACFT12-SYS-1-TRK-TRK'" );
      lCPartConfigMap.put( "IPC_REF_NAME", "'ACFT12-SYS-1-TRK-TRK'" );
      lCPartConfigMap.put( "INV_CLASS_CD", "'TRK'" );
      lCPartConfigMap.put( "POS_CT", "'3'" );
      lCPartConfigMap.put( "POS_NAME_LIST", null );
      lCPartConfigMap.put( "NH_IPC_REF_CD", "'ACFT12-SYS-1-TRK'" );
      lCPartConfigMap.put( "PN1_PART_NO_OEM", "'AUTO002'" );
      lCPartConfigMap.put( "PN1_MANUFACT_REF", "'11111'" );
      lCPartConfigMap.put( "PN1_PART_NO_SDESC", "'AUTO002'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_CONFIG, lCPartConfigMap ) );

      Assert.assertTrue( "Validation error occurred", runValidationPartConfig() == 1 );

   }


   /**
    *
    * This test case is to verify OPER-3037 auto creation: pos_name_list for parent and child are
    * empty
    *
    *
    */

   @Test
   public void testOPER_3037_AUTOCREATION_1_IMPORT() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testOPER_3037_AUTOCREATION_1_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( "Import error occurred", runImportPartConfig() == 1 );

      // Verify eqp_assmbl_bom table import
      String lQuery = "select 1 from " + TableUtil.EQP_ASSMBL_BOM + " where ASSMBL_CD='ACFT_12'"
            + " and ASSMBL_BOM_CD='ACFT12-SYS-1-TRK'";
      Assert.assertTrue( "Check eqp_assmbl_bom table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.EQP_ASSMBL_BOM + " where ASSMBL_CD='ACFT_12'"
            + " and ASSMBL_BOM_CD='ACFT12-SYS-1-TRK-TRK'";
      Assert.assertTrue( "Check eqp_assmbl_bom table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      // Verify eqp_assmbl_pos table import
      checkTables( TableUtil.EQP_ASSMBL_POS, "1" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "2" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "1.1" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "1.2" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "1.3" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "2.1" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "2.2" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "2.3" );

      // verify eqp_bom_part
      simpleIDs parentBOMID = getBOMIDs( "ACFT_12", "TRK", "ACFT12-SYS-1-TRK", "ACFT12-SYS-1-TRK" );
      simpleIDs childBOMID =
            getBOMIDs( "ACFT_12", "TRK", "ACFT12-SYS-1-TRK-TRK", "ACFT12-SYS-1-TRK-TRK" );

      Assert.assertTrue( "check parent bom id is not null", parentBOMID != null );
      Assert.assertTrue( "check child bom id is not null", childBOMID != null );

      // verify eqp_prt_baseline table
      lQuery = "select 1 from " + TableUtil.EQP_PART_BASELINE + " where BOM_PART_DB_ID='"
            + parentBOMID.getNO_DB_ID() + "' and BOM_PART_ID='" + parentBOMID.getNO_ID() + "'";
      Assert.assertTrue( "Check EQP_PART_BASELINE table to verify the record exists: 1",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.EQP_PART_BASELINE + " where BOM_PART_DB_ID='"
            + childBOMID.getNO_DB_ID() + "' and BOM_PART_ID='" + childBOMID.getNO_ID() + "'";
      Assert.assertTrue( "Check EQP_PART_BASELINE table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      // verify eqp_part_no table
      simpleIDs parentPARTID = checkEqpPartNoTable( "TRK", "11111", "AUTO001", "AUTO001" );
      simpleIDs chilePARTID = checkEqpPartNoTable( "TRK", "11111", "AUTO002", "AUTO002" );
      Assert.assertTrue( "check parent part no id is not null", parentPARTID != null );
      Assert.assertTrue( "check child part no id is not null", chilePARTID != null );

   }


   /**
    *
    * This test case is to verify OPER-3037 auto creation: pos_name_list for parent is empty
    *
    *
    */

   public void testOPER_3037_AUTOCREATION_2_VALIDATION() {
      System.out.println( "======= Starting: " + testName.getMethodName() + " ========" );
      Map<String, String> lCPartConfigMap = new LinkedHashMap<>();
      // first record
      lCPartConfigMap.put( "ASSMBL_CD", "'ACFT_12'" );
      lCPartConfigMap.put( "IPC_REF_CD", "'ACFT12-SYS-1-TRK'" );
      lCPartConfigMap.put( "IPC_REF_NAME", "'ACFT12-SYS-1-TRK'" );
      lCPartConfigMap.put( "ATA_CD", "'ACFT12-SYS-1'" );
      lCPartConfigMap.put( "INV_CLASS_CD", "'TRK'" );

      lCPartConfigMap.put( "POS_CT", "'2'" );
      lCPartConfigMap.put( "POS_NAME_LIST", null );
      lCPartConfigMap.put( "PN1_PART_NO_OEM", "'AUTO001'" );
      lCPartConfigMap.put( "PN1_MANUFACT_REF", "'11111'" );
      lCPartConfigMap.put( "PN1_PART_NO_SDESC", "'AUTO001'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_CONFIG, lCPartConfigMap ) );

      // Second record
      lCPartConfigMap.clear();
      lCPartConfigMap.put( "ASSMBL_CD", "'ACFT_12'" );
      lCPartConfigMap.put( "IPC_REF_CD", "'ACFT12-SYS-1-TRK-TRK'" );
      lCPartConfigMap.put( "IPC_REF_NAME", "'ACFT12-SYS-1-TRK-TRK'" );
      lCPartConfigMap.put( "INV_CLASS_CD", "'TRK'" );
      lCPartConfigMap.put( "POS_CT", "'3'" );
      lCPartConfigMap.put( "POS_NAME_LIST",
            "'FWD LEFT,FWD MID,FWD RIGHT, AFT LEFT, AFT MID, AFT RIGHT'" );
      lCPartConfigMap.put( "NH_IPC_REF_CD", "'ACFT12-SYS-1-TRK'" );
      lCPartConfigMap.put( "PN1_PART_NO_OEM", "'AUTO002'" );
      lCPartConfigMap.put( "PN1_MANUFACT_REF", "'11111'" );
      lCPartConfigMap.put( "PN1_PART_NO_SDESC", "'AUTO002'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_CONFIG, lCPartConfigMap ) );

      Assert.assertTrue( "Validation error occurred", runValidationPartConfig() == 1 );

   }


   /**
    *
    * This test case is to verify OPER-3037 auto creation: pos_name_list for parent is empty
    *
    *
    */
   @Test
   public void testOPER_3037_AUTOCREATION_2_IMPORT() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testOPER_3037_AUTOCREATION_2_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( "Import error occurred", runImportPartConfig() == 1 );

      // Verify eqp_assmbl_bom table import
      String lQuery = "select 1 from " + TableUtil.EQP_ASSMBL_BOM + " where ASSMBL_CD='ACFT_12'"
            + " and ASSMBL_BOM_CD='ACFT12-SYS-1-TRK'";
      Assert.assertTrue( "Check eqp_assmbl_bom table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.EQP_ASSMBL_BOM + " where ASSMBL_CD='ACFT_12'"
            + " and ASSMBL_BOM_CD='ACFT12-SYS-1-TRK-TRK'";
      Assert.assertTrue( "Check eqp_assmbl_bom table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      // Verify eqp_assmbl_pos table import
      checkTables( TableUtil.EQP_ASSMBL_POS, "1" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "2" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "AFT LEFT" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "AFT RIGHT" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "AFT MID" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "FWD LEFT" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "FWD RIGHT" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "FWD MID" );

      // verify eqp_bom_part
      simpleIDs parentBOMID = getBOMIDs( "ACFT_12", "TRK", "ACFT12-SYS-1-TRK", "ACFT12-SYS-1-TRK" );
      simpleIDs childBOMID =
            getBOMIDs( "ACFT_12", "TRK", "ACFT12-SYS-1-TRK-TRK", "ACFT12-SYS-1-TRK-TRK" );

      Assert.assertTrue( "check parent bom id is not null", parentBOMID != null );
      Assert.assertTrue( "check child bom id is not null", childBOMID != null );

      // verify eqp_prt_baseline table
      lQuery = "select 1 from " + TableUtil.EQP_PART_BASELINE + " where BOM_PART_DB_ID='"
            + parentBOMID.getNO_DB_ID() + "' and BOM_PART_ID='" + parentBOMID.getNO_ID() + "'";
      Assert.assertTrue( "Check EQP_PART_BASELINE table to verify the record exists: 1",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.EQP_PART_BASELINE + " where BOM_PART_DB_ID='"
            + childBOMID.getNO_DB_ID() + "' and BOM_PART_ID='" + childBOMID.getNO_ID() + "'";
      Assert.assertTrue( "Check EQP_PART_BASELINE table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      // verify eqp_part_no table
      simpleIDs parentPARTID = checkEqpPartNoTable( "TRK", "11111", "AUTO001", "AUTO001" );
      simpleIDs chilePARTID = checkEqpPartNoTable( "TRK", "11111", "AUTO002", "AUTO002" );
      Assert.assertTrue( "check parent part no id is not null", parentPARTID != null );
      Assert.assertTrue( "check child part no id is not null", chilePARTID != null );

   }


   /**
    *
    * This test case is to verify OPER-3037 auto creation: pos_name_list for child is empty
    *
    *
    */

   public void testOPER_3037_AUTOCREATION_3_VALIDATION() {
      System.out.println( "======= Starting: " + testName.getMethodName() + " ========" );
      Map<String, String> lCPartConfigMap = new LinkedHashMap<>();
      // first record
      lCPartConfigMap.put( "ASSMBL_CD", "'ACFT_12'" );
      lCPartConfigMap.put( "IPC_REF_CD", "'ACFT12-SYS-1-TRK'" );
      lCPartConfigMap.put( "IPC_REF_NAME", "'ACFT12-SYS-1-TRK'" );
      lCPartConfigMap.put( "ATA_CD", "'ACFT12-SYS-1'" );
      lCPartConfigMap.put( "INV_CLASS_CD", "'TRK'" );
      lCPartConfigMap.put( "POS_CT", "'2'" );
      lCPartConfigMap.put( "POS_NAME_LIST", "'FWD,AFT'" );
      lCPartConfigMap.put( "PN1_PART_NO_OEM", "'AUTO001'" );
      lCPartConfigMap.put( "PN1_MANUFACT_REF", "'11111'" );
      lCPartConfigMap.put( "PN1_PART_NO_SDESC", "'AUTO001'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_CONFIG, lCPartConfigMap ) );
      // Second record
      lCPartConfigMap.clear();
      lCPartConfigMap.put( "ASSMBL_CD", "'ACFT_12'" );
      lCPartConfigMap.put( "IPC_REF_CD", "'ACFT12-SYS-1-TRK-TRK'" );
      lCPartConfigMap.put( "IPC_REF_NAME", "'ACFT12-SYS-1-TRK-TRK'" );
      lCPartConfigMap.put( "INV_CLASS_CD", "'TRK'" );
      lCPartConfigMap.put( "POS_CT", "'3'" );
      lCPartConfigMap.put( "POS_NAME_LIST", null );
      lCPartConfigMap.put( "NH_IPC_REF_CD", "'ACFT12-SYS-1-TRK'" );
      lCPartConfigMap.put( "PN1_PART_NO_OEM", "'AUTO002'" );
      lCPartConfigMap.put( "PN1_MANUFACT_REF", "'11111'" );
      lCPartConfigMap.put( "PN1_PART_NO_SDESC", "'AUTO002'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_CONFIG, lCPartConfigMap ) );

      Assert.assertTrue( "Validation error occurred", runValidationPartConfig() == 1 );

   }


   /**
    *
    * This test case is to verify OPER-3037 auto creation: pos_name_list for child is empty
    *
    *
    */
   @Test
   public void testOPER_3037_AUTOCREATION_3_IMPORT() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testOPER_3037_AUTOCREATION_3_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( "Import error occurred", runImportPartConfig() == 1 );

      // Verify eqp_assmbl_bom table import
      String lQuery = "select 1 from " + TableUtil.EQP_ASSMBL_BOM + " where ASSMBL_CD='ACFT_12'"
            + " and ASSMBL_BOM_CD='ACFT12-SYS-1-TRK'";
      Assert.assertTrue( "Check eqp_assmbl_bom table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.EQP_ASSMBL_BOM + " where ASSMBL_CD='ACFT_12'"
            + " and ASSMBL_BOM_CD='ACFT12-SYS-1-TRK-TRK'";
      Assert.assertTrue( "Check eqp_assmbl_bom table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      // Verify eqp_assmbl_pos table import
      checkTables( TableUtil.EQP_ASSMBL_POS, "FWD" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "AFT" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "AFT-1" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "AFT-2" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "AFT-3" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "FWD-1" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "FWD-2" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "FWD-3" );

      // verify eqp_bom_part
      simpleIDs parentBOMID = getBOMIDs( "ACFT_12", "TRK", "ACFT12-SYS-1-TRK", "ACFT12-SYS-1-TRK" );
      simpleIDs childBOMID =
            getBOMIDs( "ACFT_12", "TRK", "ACFT12-SYS-1-TRK-TRK", "ACFT12-SYS-1-TRK-TRK" );

      Assert.assertTrue( "check parent bom id is not null", parentBOMID != null );
      Assert.assertTrue( "check child bom id is not null", childBOMID != null );

      // verify eqp_prt_baseline table
      lQuery = "select 1 from " + TableUtil.EQP_PART_BASELINE + " where BOM_PART_DB_ID='"
            + parentBOMID.getNO_DB_ID() + "' and BOM_PART_ID='" + parentBOMID.getNO_ID() + "'";
      Assert.assertTrue( "Check EQP_PART_BASELINE table to verify the record exists: 1",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.EQP_PART_BASELINE + " where BOM_PART_DB_ID='"
            + childBOMID.getNO_DB_ID() + "' and BOM_PART_ID='" + childBOMID.getNO_ID() + "'";
      Assert.assertTrue( "Check EQP_PART_BASELINE table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      // verify eqp_part_no table
      simpleIDs parentPARTID = checkEqpPartNoTable( "TRK", "11111", "AUTO001", "AUTO001" );
      simpleIDs chilePARTID = checkEqpPartNoTable( "TRK", "11111", "AUTO002", "AUTO002" );
      Assert.assertTrue( "check parent part no id is not null", parentPARTID != null );
      Assert.assertTrue( "check child part no id is not null", chilePARTID != null );

   }


   /**
    *
    * This test case is to verify OPER-3037 3 level hierarchy combinations. empty
    *
    *
    */

   public void testOPER_3037_COMBINATION_1_VALIDATION() {
      System.out.println( "======= Starting: " + testName.getMethodName() + " ========" );
      Map<String, String> lCPartConfigMap = new LinkedHashMap<>();
      // first record
      lCPartConfigMap.put( "ASSMBL_CD", "'ENG_12'" );
      lCPartConfigMap.put( "IPC_REF_CD", "'ENG12-SYS-1-TRK'" );
      lCPartConfigMap.put( "IPC_REF_NAME", "'ENG12-SYS-1-TRK'" );
      lCPartConfigMap.put( "ATA_CD", "'ENG12-SYS-1'" );
      lCPartConfigMap.put( "INV_CLASS_CD", "'TRK'" );
      lCPartConfigMap.put( "POS_CT", "'2'" );
      lCPartConfigMap.put( "POS_NAME_LIST", "'FWD,AFT'" );
      lCPartConfigMap.put( "PN1_PART_NO_OEM", "'AUTO001'" );
      lCPartConfigMap.put( "PN1_MANUFACT_REF", "'11111'" );
      lCPartConfigMap.put( "PN1_PART_NO_SDESC", "'AUTO001'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_CONFIG, lCPartConfigMap ) );

      // Second record
      lCPartConfigMap.clear();
      lCPartConfigMap.put( "ASSMBL_CD", "'ENG_12'" );
      lCPartConfigMap.put( "IPC_REF_CD", "'ENG12-SYS-1-TRK-TRK'" );
      lCPartConfigMap.put( "IPC_REF_NAME", "'ENG12-SYS-1-TRK-TRK'" );
      lCPartConfigMap.put( "INV_CLASS_CD", "'TRK'" );
      lCPartConfigMap.put( "POS_CT", "'3'" );
      lCPartConfigMap.put( "POS_NAME_LIST", "'LEFT,MID,RIGHT'" );
      lCPartConfigMap.put( "NH_IPC_REF_CD", "'ENG12-SYS-1-TRK'" );
      lCPartConfigMap.put( "PN1_PART_NO_OEM", "'AUTO002'" );
      lCPartConfigMap.put( "PN1_MANUFACT_REF", "'11111'" );
      lCPartConfigMap.put( "PN1_PART_NO_SDESC", "'AUTO002'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_CONFIG, lCPartConfigMap ) );

      // Third record
      lCPartConfigMap.clear();
      lCPartConfigMap.put( "ASSMBL_CD", "'ENG_12'" );
      lCPartConfigMap.put( "IPC_REF_CD", "'ENG12-SYS-1-TRK-TRK-TRK'" );
      lCPartConfigMap.put( "IPC_REF_NAME", "'ENG12-SYS-1-TRK-TRK-TRK'" );
      lCPartConfigMap.put( "INV_CLASS_CD", "'TRK'" );
      lCPartConfigMap.put( "POS_CT", "'1'" );
      lCPartConfigMap.put( "POS_NAME_LIST", null );
      lCPartConfigMap.put( "NH_IPC_REF_CD", "'ENG12-SYS-1-TRK-TRK'" );
      lCPartConfigMap.put( "PN1_PART_NO_OEM", "'AUTO003'" );
      lCPartConfigMap.put( "PN1_MANUFACT_REF", "'11111'" );
      lCPartConfigMap.put( "PN1_PART_NO_SDESC", "'AUTO003'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_CONFIG, lCPartConfigMap ) );

      Assert.assertTrue( "Validation error occurred", runValidationPartConfig() == 1 );

   }


   /**
    *
    * This test case is to verify OPER-3037 3 level hierarchy combinations. empty
    *
    *
    */
   @Test
   public void testOPER_3037_COMBINATION_1_IMPORT() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testOPER_3037_COMBINATION_1_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( "Import error occurred", runImportPartConfig() == 1 );

      // Verify eqp_assmbl_bom table import
      String lQuery = "select 1 from " + TableUtil.EQP_ASSMBL_BOM + " where ASSMBL_CD='ENG_12'"
            + " and ASSMBL_BOM_CD='ENG12-SYS-1-TRK'";
      Assert.assertTrue( "Check eqp_assmbl_bom table to verify the record exists: 1",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.EQP_ASSMBL_BOM + " where ASSMBL_CD='ENG_12'"
            + " and ASSMBL_BOM_CD='ENG12-SYS-1-TRK-TRK'";
      Assert.assertTrue( "Check eqp_assmbl_bom table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.EQP_ASSMBL_BOM + " where ASSMBL_CD='ENG_12'"
            + " and ASSMBL_BOM_CD='ENG12-SYS-1-TRK-TRK'";
      Assert.assertTrue( "Check eqp_assmbl_bom table to verify the record exists: 3",
            RecordsExist( lQuery ) );

      // Verify eqp_assmbl_pos table import
      checkTables( TableUtil.EQP_ASSMBL_POS, "AFT", "ENG_12" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "FWD", "ENG_12" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "AFT-LEFT", "ENG_12" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "AFT-LEFT-1", "ENG_12" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "AFT-RIGHT", "ENG_12" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "AFT-RIGHT-1", "ENG_12" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "AFT-MID", "ENG_12" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "AFT-MID-1", "ENG_12" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "FWD-LEFT", "ENG_12" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "FWD-LEFT-1", "ENG_12" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "FWD-RIGHT", "ENG_12" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "FWD-RIGHT-1", "ENG_12" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "FWD-MID", "ENG_12" );
      checkTables( TableUtil.EQP_ASSMBL_POS, "FWD-MID-1", "ENG_12" );

      // verify eqp_bom_part
      simpleIDs parentBOMID = getBOMIDs( "ENG_12", "TRK", "ENG12-SYS-1-TRK", "ENG12-SYS-1-TRK" );
      simpleIDs childBOMID =
            getBOMIDs( "ENG_12", "TRK", "ENG12-SYS-1-TRK-TRK", "ENG12-SYS-1-TRK-TRK" );
      simpleIDs grandchildBOMID =
            getBOMIDs( "ENG_12", "TRK", "ENG12-SYS-1-TRK-TRK-TRK", "ENG12-SYS-1-TRK-TRK-TRK" );

      Assert.assertTrue( "check parent bom id is not null", parentBOMID != null );
      Assert.assertTrue( "check child bom id is not null", childBOMID != null );
      Assert.assertTrue( "check grandchild bom id is not null", grandchildBOMID != null );

      // verify eqp_prt_baseline table
      lQuery = "select 1 from " + TableUtil.EQP_PART_BASELINE + " where BOM_PART_DB_ID='"
            + parentBOMID.getNO_DB_ID() + "' and BOM_PART_ID='" + parentBOMID.getNO_ID() + "'";
      Assert.assertTrue( "Check EQP_PART_BASELINE table to verify the record exists: 1",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.EQP_PART_BASELINE + " where BOM_PART_DB_ID='"
            + childBOMID.getNO_DB_ID() + "' and BOM_PART_ID='" + childBOMID.getNO_ID() + "'";
      Assert.assertTrue( "Check EQP_PART_BASELINE table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.EQP_PART_BASELINE + " where BOM_PART_DB_ID='"
            + grandchildBOMID.getNO_DB_ID() + "' and BOM_PART_ID='" + grandchildBOMID.getNO_ID()
            + "'";
      Assert.assertTrue( "Check EQP_PART_BASELINE table to verify the record exists: 3",
            RecordsExist( lQuery ) );

      // verify eqp_part_no table
      simpleIDs parentPARTID = checkEqpPartNoTable( "TRK", "11111", "AUTO001", "AUTO001" );
      simpleIDs chilePARTID = checkEqpPartNoTable( "TRK", "11111", "AUTO002", "AUTO002" );
      simpleIDs gtandchilePARTID = checkEqpPartNoTable( "TRK", "11111", "AUTO003", "AUTO003" );
      Assert.assertTrue( "check parent part no id is not null", parentPARTID != null );
      Assert.assertTrue( "check child part no id is not null", chilePARTID != null );
      Assert.assertTrue( "check grandchild part no id is not null", gtandchilePARTID != null );

   }


   /**
    *
    * This test case is to verify CFGPRT-12002 position: If POS_NAME_LIST is specified, then it must
    * have a correct number of position per parent or position per assembly.
    *
    *
    */

   @Test
   public void testCFGPRT_12002_VALIDATION() {
      System.out.println( "======= Starting: " + testName.getMethodName() + " ========" );
      Map<String, String> lCPartConfigMap = new LinkedHashMap<>();
      // first record
      lCPartConfigMap.put( "ASSMBL_CD", "'ACFT_12'" );
      lCPartConfigMap.put( "IPC_REF_CD", "'ACFT12-SYS-1-TRK'" );
      lCPartConfigMap.put( "IPC_REF_NAME", "'ACFT12-SYS-1-TRK'" );
      lCPartConfigMap.put( "ATA_CD", "'ACFT12-SYS-1'" );
      lCPartConfigMap.put( "INV_CLASS_CD", "'TRK'" );
      lCPartConfigMap.put( "POS_CT", "'2'" );
      lCPartConfigMap.put( "POS_NAME_LIST", "'FWD,AFT'" );
      lCPartConfigMap.put( "PN1_PART_NO_OEM", "'AUTO001'" );
      lCPartConfigMap.put( "PN1_MANUFACT_REF", "'11111'" );
      lCPartConfigMap.put( "PN1_PART_NO_SDESC", "'AUTO001'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_CONFIG, lCPartConfigMap ) );
      // Second record
      lCPartConfigMap.clear();
      lCPartConfigMap.put( "ASSMBL_CD", "'ACFT_12'" );
      lCPartConfigMap.put( "IPC_REF_CD", "'ACFT12-SYS-1-TRK-TRK'" );
      lCPartConfigMap.put( "IPC_REF_NAME", "'ACFT12-SYS-1-TRK-TRK'" );
      lCPartConfigMap.put( "INV_CLASS_CD", "'TRK'" );
      lCPartConfigMap.put( "POS_CT", "'3'" );
      lCPartConfigMap.put( "POS_NAME_LIST", "'LEFT,MID'" );
      lCPartConfigMap.put( "NH_IPC_REF_CD", "'ACFT12-SYS-1-TRK'" );
      lCPartConfigMap.put( "PN1_PART_NO_OEM", "'AUTO002'" );
      lCPartConfigMap.put( "PN1_MANUFACT_REF", "'11111'" );
      lCPartConfigMap.put( "PN1_PART_NO_SDESC", "'AUTO002'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_CONFIG, lCPartConfigMap ) );

      Assert.assertTrue( "Validation error occurred", runValidationPartConfig() != 1 );

      // check error code
      checkPARTErrorCode( "testCFGPRT_1201", "CFGPRT-12002" );

   }


   /**
    *
    * This test case is to verify CFGPRT-12048: The position names in POS_NAME_LIST are too long.
    * They will exceed the 200 character limit when concatenated with the parent position names.
    *
    *
    */

   @Test
   public void testCFGPRT_12048_VALIDATION() {
      System.out.println( "======= Starting: " + testName.getMethodName() + " ========" );
      String strTest = "";
      for ( int i = 0; i < 198; i++ )
         strTest = strTest + "A";

      Map<String, String> lCPartConfigMap = new LinkedHashMap<>();
      // first record
      lCPartConfigMap.put( "ASSMBL_CD", "'ACFT_12'" );
      lCPartConfigMap.put( "IPC_REF_CD", "'ACFT12-SYS-1-TRK'" );
      lCPartConfigMap.put( "IPC_REF_NAME", "'ACFT12-SYS-1-TRK'" );
      lCPartConfigMap.put( "ATA_CD", "'ACFT12-SYS-1'" );
      lCPartConfigMap.put( "INV_CLASS_CD", "'TRK'" );
      lCPartConfigMap.put( "POS_CT", "'2'" );
      lCPartConfigMap.put( "POS_NAME_LIST", "'FWD,AFT'" );
      lCPartConfigMap.put( "PN1_PART_NO_OEM", "'AUTO001'" );
      lCPartConfigMap.put( "PN1_MANUFACT_REF", "'11111'" );
      lCPartConfigMap.put( "PN1_PART_NO_SDESC", "'AUTO001'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_CONFIG, lCPartConfigMap ) );

      // Second record
      lCPartConfigMap.clear();
      lCPartConfigMap.put( "ASSMBL_CD", "'ACFT_12'" );
      lCPartConfigMap.put( "IPC_REF_CD", "'ACFT12-SYS-1-TRK-TRK'" );
      lCPartConfigMap.put( "IPC_REF_NAME", "'ACFT12-SYS-1-TRK-TRK'" );
      lCPartConfigMap.put( "INV_CLASS_CD", "'TRK'" );
      lCPartConfigMap.put( "POS_CT", "'3'" );
      lCPartConfigMap.put( "POS_NAME_LIST", "'LEFT,MID," + strTest + "'" );
      lCPartConfigMap.put( "NH_IPC_REF_CD", "'ACFT12-SYS-1-TRK'" );
      lCPartConfigMap.put( "PN1_PART_NO_OEM", "'AUTO002'" );
      lCPartConfigMap.put( "PN1_MANUFACT_REF", "'11111'" );
      lCPartConfigMap.put( "PN1_PART_NO_SDESC", "'AUTO002'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_CONFIG, lCPartConfigMap ) );

      Assert.assertTrue( "Validation error occurred", runValidationPartConfig() != 1 );

      // check error code
      checkPARTErrorCode( "testCFGPRT_1201", "CFGPRT-12048" );

   }


   /**
    *
    * This test case is to verify CFGPRT-12049: Elements of comma separated list of POS_NAME_LIST
    * cannot be empty/blank.
    *
    *
    */

   @Test
   public void testCFGPRT_12049_VALIDATION() {
      System.out.println( "======= Starting: " + testName.getMethodName() + " ========" );
      Map<String, String> lCPartConfigMap = new LinkedHashMap<>();
      // first record
      lCPartConfigMap.put( "ASSMBL_CD", "'ACFT_12'" );
      lCPartConfigMap.put( "IPC_REF_CD", "'ACFT12-SYS-1-TRK'" );
      lCPartConfigMap.put( "IPC_REF_NAME", "'ACFT12-SYS-1-TRK'" );
      lCPartConfigMap.put( "ATA_CD", "'ACFT12-SYS-1'" );
      lCPartConfigMap.put( "INV_CLASS_CD", "'TRK'" );
      lCPartConfigMap.put( "POS_CT", "'2'" );
      lCPartConfigMap.put( "POS_NAME_LIST", "','" );
      lCPartConfigMap.put( "PN1_PART_NO_OEM", "'AUTO001'" );
      lCPartConfigMap.put( "PN1_MANUFACT_REF", "'11111'" );
      lCPartConfigMap.put( "PN1_PART_NO_SDESC", "'AUTO001'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_CONFIG, lCPartConfigMap ) );
      // Second record
      lCPartConfigMap.clear();
      lCPartConfigMap.put( "ASSMBL_CD", "'ACFT_12'" );
      lCPartConfigMap.put( "IPC_REF_CD", "'ACFT12-SYS-1-TRK-TRK'" );
      lCPartConfigMap.put( "IPC_REF_NAME", "'ACFT12-SYS-1-TRK-TRK'" );
      lCPartConfigMap.put( "INV_CLASS_CD", "'TRK'" );
      lCPartConfigMap.put( "POS_CT", "'3'" );
      lCPartConfigMap.put( "POS_NAME_LIST", "'LEFT,MID, RIGHT'" );
      lCPartConfigMap.put( "NH_IPC_REF_CD", "'ACFT12-SYS-1-TRK'" );
      lCPartConfigMap.put( "PN1_PART_NO_OEM", "'AUTO002'" );
      lCPartConfigMap.put( "PN1_MANUFACT_REF", "'11111'" );
      lCPartConfigMap.put( "PN1_PART_NO_SDESC", "'AUTO002'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_CONFIG, lCPartConfigMap ) );

      Assert.assertTrue( "Validation error occurred", runValidationPartConfig() != 1 );

      // check error code
      checkPARTErrorCode( "testCFGPRT_1201", "CFGPRT-12049" );

   }


   /**
    *
    * This test case is to verify CFGPRT-12001: C_PART_CONFIG.pos_ct must be a positive integer.
    *
    *
    */

   @Test
   public void testCFGPRT_12001_VALIDATION() {
      System.out.println( "======= Starting: " + testName.getMethodName() + " ========" );
      Map<String, String> lCPartConfigMap = new LinkedHashMap<>();
      // first record
      lCPartConfigMap.put( "ASSMBL_CD", "'ACFT_12'" );
      lCPartConfigMap.put( "IPC_REF_CD", "'ACFT12-SYS-1-TRK'" );
      lCPartConfigMap.put( "IPC_REF_NAME", "'ACFT12-SYS-1-TRK'" );
      lCPartConfigMap.put( "ATA_CD", "'ACFT12-SYS-1'" );
      lCPartConfigMap.put( "INV_CLASS_CD", "'TRK'" );
      lCPartConfigMap.put( "POS_CT", "'-2'" );
      lCPartConfigMap.put( "POS_NAME_LIST", "'FWD,AFT'" );
      lCPartConfigMap.put( "PN1_PART_NO_OEM", "'AUTO001'" );
      lCPartConfigMap.put( "PN1_MANUFACT_REF", "'11111'" );
      lCPartConfigMap.put( "PN1_PART_NO_SDESC", "'AUTO001'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_CONFIG, lCPartConfigMap ) );
      // Second record
      lCPartConfigMap.clear();
      lCPartConfigMap.put( "ASSMBL_CD", "'ACFT_12'" );
      lCPartConfigMap.put( "IPC_REF_CD", "'ACFT12-SYS-1-TRK-TRK'" );
      lCPartConfigMap.put( "IPC_REF_NAME", "'ACFT12-SYS-1-TRK-TRK'" );
      lCPartConfigMap.put( "INV_CLASS_CD", "'TRK'" );
      lCPartConfigMap.put( "POS_CT", "'3'" );
      lCPartConfigMap.put( "POS_NAME_LIST", "'LEFT,MID, RIGHT'" );
      lCPartConfigMap.put( "NH_IPC_REF_CD", "'ACFT12-SYS-1-TRK'" );
      lCPartConfigMap.put( "PN1_PART_NO_OEM", "'AUTO002'" );
      lCPartConfigMap.put( "PN1_MANUFACT_REF", "'11111'" );
      lCPartConfigMap.put( "PN1_PART_NO_SDESC", "'AUTO002'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_CONFIG, lCPartConfigMap ) );

      Assert.assertTrue( "Validation error occurred", runValidationPartConfig() != 1 );

      // check error code
      checkPARTErrorCode( "testCFGPRT_1201", "CFGPRT-12001" );

   }


   // ======================================================================================
   /**
    * This function is to verify eqp_part_no table
    *
    *
    */
   public simpleIDs checkEqpPartNoTable( String aINV_CLASS_CD, String aMANUFACT_CD,
         String aPART_NO_SDESC, String aPART_NO_OEM ) {

      String[] iIds = { "PART_NO_DB_ID", "PART_NO_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "INV_CLASS_CD", aINV_CLASS_CD );
      lArgs.addArguments( "MANUFACT_CD", aMANUFACT_CD );
      lArgs.addArguments( "PART_NO_SDESC", aPART_NO_SDESC );
      lArgs.addArguments( "PART_NO_OEM", aPART_NO_OEM );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_PART_NO, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;
   }


   /**
    * This function is to verify eqp_bom_part table
    *
    *
    */
   public simpleIDs getBOMIDs( String aAssmbl_CD, String aINV_CLASS_CD, String aBOM_PART_CD,
         String aBOM_PART_NAME ) {

      String[] iIds = { "BOM_PART_DB_ID", "BOM_PART_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "ASSMBL_CD", aAssmbl_CD );
      lArgs.addArguments( "INV_CLASS_CD", aINV_CLASS_CD );
      lArgs.addArguments( "BOM_PART_CD", aBOM_PART_CD );
      lArgs.addArguments( "BOM_PART_NAME", aBOM_PART_NAME );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_BOM_PART, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;
   }


   /**
    * This function is to verify eqp_assmbl_pos table
    *
    *
    */
   public void checkTables( String aTableName, String aEQPPOSCD ) {
      String lQuery = "select 1 from " + aTableName + " where ASSMBL_CD='ACFT_12'"
            + " and EQP_POS_CD='" + aEQPPOSCD + "'";
      Assert.assertTrue( "Check EQP_ASSMBL_POS table to verify the record exists: ",
            RecordsExist( lQuery ) );

   }


   /**
    * This function is to verify eqp_assmbl_pos table
    *
    *
    */
   public void checkTables( String aTableName, String aEQPPOSCD, String aAssmbl_CD ) {
      String lQuery = "select 1 from " + aTableName + " where ASSMBL_CD='" + aAssmbl_CD + "'"
            + " and EQP_POS_CD='" + aEQPPOSCD + "'";
      Assert.assertTrue( "Check EQP_ASSMBL_POS table to verify the record exists: ",
            RecordsExist( lQuery ) );

   }


   /**
    * This function is to setup data base
    *
    *
    */

   public void setupDataBase() {
      String lquery = "UPDATE mim_local_db SET DB_ID=1 WHERE DB_ID=4650";
      runUpdate( lquery );
   }


   /**
    * This function is to restore data base
    *
    *
    */
   public void restoreDB() {
      // Restore DB
      String lquery = "UPDATE mim_local_db SET DB_ID=4650 WHERE DB_ID=1";
      runUpdate( lquery );
   }


   /**
    * This function is to clean up tables
    *
    *
    */
   public void clearTables() {

      // delete EQP_ASSMBL_BOM
      String lStrDelete = "delete from " + TableUtil.EQP_ASSMBL_BOM
            + "  where ASSMBL_CD='ACFT_12' and ASSMBL_BOM_CD in ('ACFT12-SYS-1-TRK-TRK', 'ACFT12-SYS-1-TRK') ";
      executeSQL( lStrDelete );

      lStrDelete = "delete from " + TableUtil.EQP_ASSMBL_BOM
            + "  where ASSMBL_CD='ENG_12' and ASSMBL_BOM_CD in ('ENG12-SYS-1-TRK-TRK', 'ENG12-SYS-1-TRK') ";
      executeSQL( lStrDelete );

      lStrDelete = "delete from " + TableUtil.EQP_ASSMBL_BOM
            + "  where ASSMBL_CD='ENG_12' and ASSMBL_BOM_CD in ('ENG12-SYS-1-TRK-TRK-TRK', 'ENG12-SYS-1-TRK-TRK') ";
      executeSQL( lStrDelete );

      // delete eqp_assmbl_pos
      lStrDelete = "delete from " + TableUtil.EQP_ASSMBL_POS
            + "  where ASSMBL_CD='ACFT_12' and EQP_POS_CD in ('AFT','FWD', 'AFT-RIGHT','AFT-MID', 'AFT-LEFT', 'FWD-RIGHT','FWD-MID','FWD-LEFT')";
      executeSQL( lStrDelete );

      lStrDelete = "delete from " + TableUtil.EQP_ASSMBL_POS
            + "  where ASSMBL_CD='ACFT_12' and EQP_POS_CD in ('AFT','FWD', 'AFT RIGHT','AFT MID', 'AFT LEFT', 'FWD RIGHT','FWD MID','FWD LEFT')";
      executeSQL( lStrDelete );

      lStrDelete = "delete from " + TableUtil.EQP_ASSMBL_POS
            + "  where ASSMBL_CD='ACFT_12' and EQP_POS_CD in ('1','2', '1.1','1.2', '1.3', '2.1','2.2','2.3')";
      executeSQL( lStrDelete );

      lStrDelete = "delete from " + TableUtil.EQP_ASSMBL_POS
            + "  where ASSMBL_CD='ACFT_12' and EQP_POS_CD in ('AFT-1','AFT-2', 'AFT-3','FWD-1', 'FWD-2', 'FWD-3')";
      executeSQL( lStrDelete );

      lStrDelete = "delete from " + TableUtil.EQP_ASSMBL_POS
            + "  where ASSMBL_CD='ENG_12' and EQP_POS_CD in ('AFT','FWD', 'AFT-RIGHT','AFT-MID', 'AFT-LEFT', 'FWD-RIGHT','FWD-MID','FWD-LEFT','AFT-RIGHT-1','AFT-MID-1', 'AFT-LEFT-1', 'FWD-RIGHT-1','FWD-MID-1','FWD-LEFT-1')";
      executeSQL( lStrDelete );

      // delete eqp_bom_part
      lStrDelete = "delete from " + TableUtil.EQP_BOM_PART + "  where BOM_PART_DB_ID=1";
      executeSQL( lStrDelete );

      // delete eqp_part_baseline
      lStrDelete = "delete from " + TableUtil.EQP_PART_BASELINE + "  where BOM_PART_DB_ID=1";
      executeSQL( lStrDelete );

      // delete eqp_part_no
      lStrDelete = "delete from " + TableUtil.EQP_PART_NO + "  where PART_NO_DB_ID=1";
      executeSQL( lStrDelete );

   }

}
