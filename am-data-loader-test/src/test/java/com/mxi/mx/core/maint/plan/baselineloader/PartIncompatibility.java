package com.mxi.mx.core.maint.plan.baselineloader;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.util.TableUtil;


/**
 * This is for positive tests for BL Part incompatibility
 *
 */
public class PartIncompatibility extends PartIncompatibilityTest {

   @Rule
   public TestName testName = new TestName();


   @Before
   @Override
   public void before() throws Exception {
      super.before();
      clearBaselineLoaderTables();

   }


   @Override
   @After
   public void after() throws Exception {
      removeImportedData();
      super.after();

   }


   String iAssemblCd = "'ACFT_CD1'";
   String iBomPartCd = "'ACFT-SYS-1-1-TRK-P1'";
   String iPartNoOem = "'A0000001'";
   String iManufactCd = "'10001'";
   String iIncompatAssemblCd = "'ENG_CD1'";
   String iIncompatBomPartCd = "'ENG-SYS-1-1-TRK-P2'";
   String iIncompatPartNoOem = "'E0000002'";
   String iIncompatManufactCd = "'11111'";
   String iIncompatAssemblCd2 = "'ACFT_CD1'";
   String iIncompatBomPartCd2 = "'ACFT-SYS-1-1-TRK-SRU'";
   String iIncompatPartNoOem2 = "'A0000007'";
   String iIncompatManufactCd2 = "'ABC11'";

   public String iAssemblCd_ACFT_T1 = "ACFT_T1";
   public String iPART_GROUP_CD1 = "TRK-ACFT-T1-G1";
   public String iPART_GROUP_CD2 = "TRK-ACFT-T1-G2";
   public String iPART_NO_OEM_PN100 = "PN100";
   public String iPART_NO_OEM_PN200 = "PN200";


   /**
    *
    * This test will verify import if all staging table column are populated.
    *
    */
   @Test
   public void TestincompatibilityParts() {

      Map<String, String> lMapBlCompatPart = new LinkedHashMap<String, String>();

      lMapBlCompatPart.put( "ASSMBL_CD", iAssemblCd );
      lMapBlCompatPart.put( "PART_GROUP_CD", iBomPartCd );
      lMapBlCompatPart.put( "PART_NO_OEM", iPartNoOem );
      lMapBlCompatPart.put( "MANUFACT_CD", iManufactCd );
      lMapBlCompatPart.put( "INCOMPAT_ASSMBL_CD", iIncompatAssemblCd );
      lMapBlCompatPart.put( "INCOMPAT_PART_GROUP_CD", iIncompatBomPartCd );
      lMapBlCompatPart.put( "INCOMPAT_PART_NO_OEM", iIncompatPartNoOem );
      lMapBlCompatPart.put( "INCOMPAT_MANUFACT_CD", iIncompatManufactCd );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_PG_PART_INCOMPAT, lMapBlCompatPart ) );

      Assert.assertTrue( "Validation error occurred",
            runValidationBL_PG_PART_INCOMPAT( true ) == 1 );

      Assert.assertTrue( "Import error occurred", runImportBL_PART_INCOMPAT( true ) == 1 );

      verifyData2( iPartNoOem, iIncompatPartNoOem, iBomPartCd, iIncompatBomPartCd );
   }


   /**
    *
    * This test will verify import if all staging table column are populated.
    *
    */
   @Test
   public void TestincompatibilityParts_onlyINCOMPAT_PART_NO_OEM() {

      Map<String, String> lMapBlCompatPart = new LinkedHashMap<String, String>();

      lMapBlCompatPart.put( "ASSMBL_CD", iAssemblCd );
      lMapBlCompatPart.put( "PART_GROUP_CD", iBomPartCd );
      lMapBlCompatPart.put( "PART_NO_OEM", iPartNoOem );
      lMapBlCompatPart.put( "MANUFACT_CD", iManufactCd );
      lMapBlCompatPart.put( "INCOMPAT_PART_NO_OEM", iIncompatPartNoOem2 );
      lMapBlCompatPart.put( "INCOMPAT_MANUFACT_CD", "'ABC11'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_PG_PART_INCOMPAT, lMapBlCompatPart ) );

      Assert.assertTrue( "Validation error occurred",
            runValidationBL_PG_PART_INCOMPAT( true ) == 1 );

      Assert.assertTrue( "Import error occurred", runImportBL_PART_INCOMPAT( true ) == 1 );

      verifyData( iPartNoOem, iIncompatPartNoOem2, iBomPartCd, iIncompatBomPartCd2 );
   }


   /**
    *
    * This test will verify import if all staging table column are populated.
    *
    */
   @Test
   public void TestincompatibilityPart_NoManufactCd() {

      Map<String, String> lMapBlCompatPart = new LinkedHashMap<String, String>();

      lMapBlCompatPart.put( "ASSMBL_CD", iAssemblCd );
      lMapBlCompatPart.put( "PART_GROUP_CD", iBomPartCd );
      lMapBlCompatPart.put( "PART_NO_OEM", iPartNoOem );
      lMapBlCompatPart.put( "INCOMPAT_ASSMBL_CD", iIncompatAssemblCd2 );
      lMapBlCompatPart.put( "INCOMPAT_PART_GROUP_CD", iIncompatBomPartCd2 );
      lMapBlCompatPart.put( "INCOMPAT_PART_NO_OEM", iIncompatPartNoOem2 );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_PG_PART_INCOMPAT, lMapBlCompatPart ) );

      Assert.assertTrue( "Validation error occurred",
            runValidationBL_PG_PART_INCOMPAT( true ) == 1 );

      Assert.assertTrue( "Import error occurred", runImportBL_PART_INCOMPAT( true ) == 1 );

      verifyData( iPartNoOem, iIncompatPartNoOem2, iBomPartCd, iIncompatBomPartCd2 );
   }


   /**
    *
    * This test will verify import if all staging table column are populated.
    *
    */
   @Test
   public void TestincompatibilityPart_NoAssemblCd() {

      Map<String, String> lMapBlCompatPart = new LinkedHashMap<String, String>();

      lMapBlCompatPart.put( "PART_GROUP_CD", iBomPartCd );
      lMapBlCompatPart.put( "PART_NO_OEM", iPartNoOem );
      lMapBlCompatPart.put( "INCOMPAT_PART_GROUP_CD", iIncompatBomPartCd2 );
      lMapBlCompatPart.put( "INCOMPAT_PART_NO_OEM", iIncompatPartNoOem2 );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_PG_PART_INCOMPAT, lMapBlCompatPart ) );

      Assert.assertTrue( "Validation error occurred",
            runValidationBL_PG_PART_INCOMPAT( true ) == 1 );

      Assert.assertTrue( "Import error occurred", runImportBL_PART_INCOMPAT( true ) == 1 );

      verifyData( iPartNoOem, iIncompatPartNoOem2, iBomPartCd, iIncompatBomPartCd2 );
   }


   /**
    *
    * This test will verify import if all staging table column are populated.
    *
    */
   @Test
   public void TestincompatibilityPart_PartBomCd() {

      Map<String, String> lMapBlCompatPart = new LinkedHashMap<String, String>();

      lMapBlCompatPart.put( "PART_NO_OEM", iPartNoOem );
      lMapBlCompatPart.put( "INCOMPAT_PART_NO_OEM", iIncompatPartNoOem2 );
      lMapBlCompatPart.put( "INCOMPAT_MANUFACT_CD", "'ABC11'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_PG_PART_INCOMPAT, lMapBlCompatPart ) );

      Assert.assertTrue( "Validation error occurred",
            runValidationBL_PG_PART_INCOMPAT( true ) == 1 );

      Assert.assertTrue( "Import error occurred", runImportBL_PART_INCOMPAT( true ) == 1 );
      verifyData( iPartNoOem, iIncompatPartNoOem2, iBomPartCd, iIncompatBomPartCd2 );
   }


   /**
    *
    * This test verifies the fix of OPER-25960. 2 records are identical but reversed in order.
    *
    */
   @Test
   public void testOPER25960_2ReverseData() {

      Map<String, String> lMapBlCompatPart = new LinkedHashMap<String, String>();
      // first record
      lMapBlCompatPart.put( "ASSMBL_CD", "'" + iAssemblCd_ACFT_T1 + "'" );
      lMapBlCompatPart.put( "PART_GROUP_CD", "'" + iPART_GROUP_CD1 + "'" );
      lMapBlCompatPart.put( "PART_NO_OEM", "'" + iPART_NO_OEM_PN200 + "'" );
      lMapBlCompatPart.put( "MANUFACT_CD", iManufactCd );
      lMapBlCompatPart.put( "INCOMPAT_ASSMBL_CD", "'" + iAssemblCd_ACFT_T1 + "'" );
      lMapBlCompatPart.put( "INCOMPAT_PART_GROUP_CD", "'" + iPART_GROUP_CD2 + "'" );
      lMapBlCompatPart.put( "INCOMPAT_PART_NO_OEM", "'" + iPART_NO_OEM_PN100 + "'" );
      lMapBlCompatPart.put( "INCOMPAT_MANUFACT_CD", iManufactCd );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_PG_PART_INCOMPAT, lMapBlCompatPart ) );

      // second record
      lMapBlCompatPart.clear();
      lMapBlCompatPart.put( "ASSMBL_CD", "'" + iAssemblCd_ACFT_T1 + "'" );
      lMapBlCompatPart.put( "PART_GROUP_CD", "'" + iPART_GROUP_CD2 + "'" );
      lMapBlCompatPart.put( "PART_NO_OEM", "'" + iPART_NO_OEM_PN100 + "'" );
      lMapBlCompatPart.put( "MANUFACT_CD", iManufactCd );
      lMapBlCompatPart.put( "INCOMPAT_ASSMBL_CD", "'" + iAssemblCd_ACFT_T1 + "'" );
      lMapBlCompatPart.put( "INCOMPAT_PART_GROUP_CD", "'" + iPART_GROUP_CD1 + "'" );
      lMapBlCompatPart.put( "INCOMPAT_PART_NO_OEM", "'" + iPART_NO_OEM_PN200 + "'" );
      lMapBlCompatPart.put( "INCOMPAT_MANUFACT_CD", iManufactCd );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_PG_PART_INCOMPAT, lMapBlCompatPart ) );

      Assert.assertTrue( "Validation error occurred",
            runValidationBL_PG_PART_INCOMPAT( true ) == -2 );
      checkTaskErrorCode( testName.getMethodName(), "BLPGI-00290" );

   }


   /**
    *
    * This test verifies the fix of OPER-25960. 2 records are identical but reversed in order, third
    * record is duplicate with one of them. error code = "BLPGI-00290"
    *
    */

   @Test
   public void testOPER25960_2ReverseData_1Dup_BLPGI_00290() {

      Map<String, String> lMapBlCompatPart = new LinkedHashMap<String, String>();
      // first record
      lMapBlCompatPart.put( "ASSMBL_CD", "'" + iAssemblCd_ACFT_T1 + "'" );
      lMapBlCompatPart.put( "PART_GROUP_CD", "'" + iPART_GROUP_CD1 + "'" );
      lMapBlCompatPart.put( "PART_NO_OEM", "'" + iPART_NO_OEM_PN200 + "'" );
      lMapBlCompatPart.put( "MANUFACT_CD", iManufactCd );
      lMapBlCompatPart.put( "INCOMPAT_ASSMBL_CD", "'" + iAssemblCd_ACFT_T1 + "'" );
      lMapBlCompatPart.put( "INCOMPAT_PART_GROUP_CD", "'" + iPART_GROUP_CD2 + "'" );
      lMapBlCompatPart.put( "INCOMPAT_PART_NO_OEM", "'" + iPART_NO_OEM_PN100 + "'" );
      lMapBlCompatPart.put( "INCOMPAT_MANUFACT_CD", iManufactCd );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_PG_PART_INCOMPAT, lMapBlCompatPart ) );

      // second record
      lMapBlCompatPart.clear();
      lMapBlCompatPart.put( "ASSMBL_CD", "'" + iAssemblCd_ACFT_T1 + "'" );
      lMapBlCompatPart.put( "PART_GROUP_CD", "'" + iPART_GROUP_CD2 + "'" );
      lMapBlCompatPart.put( "PART_NO_OEM", "'" + iPART_NO_OEM_PN100 + "'" );
      lMapBlCompatPart.put( "MANUFACT_CD", iManufactCd );
      lMapBlCompatPart.put( "INCOMPAT_ASSMBL_CD", "'" + iAssemblCd_ACFT_T1 + "'" );
      lMapBlCompatPart.put( "INCOMPAT_PART_GROUP_CD", "'" + iPART_GROUP_CD1 + "'" );
      lMapBlCompatPart.put( "INCOMPAT_PART_NO_OEM", "'" + iPART_NO_OEM_PN200 + "'" );
      lMapBlCompatPart.put( "INCOMPAT_MANUFACT_CD", iManufactCd );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_PG_PART_INCOMPAT, lMapBlCompatPart ) );

      // third record
      lMapBlCompatPart.clear();
      lMapBlCompatPart.put( "ASSMBL_CD", "'" + iAssemblCd_ACFT_T1 + "'" );
      lMapBlCompatPart.put( "PART_GROUP_CD", "'" + iPART_GROUP_CD2 + "'" );
      lMapBlCompatPart.put( "PART_NO_OEM", "'" + iPART_NO_OEM_PN100 + "'" );
      lMapBlCompatPart.put( "MANUFACT_CD", iManufactCd );
      lMapBlCompatPart.put( "INCOMPAT_ASSMBL_CD", "'" + iAssemblCd_ACFT_T1 + "'" );
      lMapBlCompatPart.put( "INCOMPAT_PART_GROUP_CD", "'" + iPART_GROUP_CD1 + "'" );
      lMapBlCompatPart.put( "INCOMPAT_PART_NO_OEM", "'" + iPART_NO_OEM_PN200 + "'" );
      lMapBlCompatPart.put( "INCOMPAT_MANUFACT_CD", iManufactCd );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_PG_PART_INCOMPAT, lMapBlCompatPart ) );

      Assert.assertTrue( "Validation error occurred",
            runValidationBL_PG_PART_INCOMPAT( true ) == -2 );

      checkTaskErrorCode( testName.getMethodName(), "BLPGI-00290" );

   }


   /**
    *
    * This test verifies the fix of OPER-25960. 2 records are duplicate records
    *
    */

   @Test
   public void testOPER25960_DuplicateRecord_BLPGI_00290() {

      Map<String, String> lMapBlCompatPart = new LinkedHashMap<String, String>();

      // first record
      lMapBlCompatPart.put( "ASSMBL_CD", "'" + iAssemblCd_ACFT_T1 + "'" );
      lMapBlCompatPart.put( "PART_GROUP_CD", "'" + iPART_GROUP_CD2 + "'" );
      lMapBlCompatPart.put( "PART_NO_OEM", "'" + iPART_NO_OEM_PN100 + "'" );
      lMapBlCompatPart.put( "MANUFACT_CD", iManufactCd );
      lMapBlCompatPart.put( "INCOMPAT_ASSMBL_CD", "'" + iAssemblCd_ACFT_T1 + "'" );
      lMapBlCompatPart.put( "INCOMPAT_PART_GROUP_CD", "'" + iPART_GROUP_CD1 + "'" );
      lMapBlCompatPart.put( "INCOMPAT_PART_NO_OEM", "'" + iPART_NO_OEM_PN200 + "'" );
      lMapBlCompatPart.put( "INCOMPAT_MANUFACT_CD", iManufactCd );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_PG_PART_INCOMPAT, lMapBlCompatPart ) );

      // first record
      lMapBlCompatPart.clear();
      lMapBlCompatPart.put( "ASSMBL_CD", "'" + iAssemblCd_ACFT_T1 + "'" );
      lMapBlCompatPart.put( "PART_GROUP_CD", "'" + iPART_GROUP_CD2 + "'" );
      lMapBlCompatPart.put( "PART_NO_OEM", "'" + iPART_NO_OEM_PN100 + "'" );
      lMapBlCompatPart.put( "MANUFACT_CD", iManufactCd );
      lMapBlCompatPart.put( "INCOMPAT_ASSMBL_CD", "'" + iAssemblCd_ACFT_T1 + "'" );
      lMapBlCompatPart.put( "INCOMPAT_PART_GROUP_CD", "'" + iPART_GROUP_CD1 + "'" );
      lMapBlCompatPart.put( "INCOMPAT_PART_NO_OEM", "'" + iPART_NO_OEM_PN200 + "'" );
      lMapBlCompatPart.put( "INCOMPAT_MANUFACT_CD", iManufactCd );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_PG_PART_INCOMPAT, lMapBlCompatPart ) );

      Assert.assertTrue( "Validation error occurred",
            runValidationBL_PG_PART_INCOMPAT( true ) == -2 );

      checkTaskErrorCode( testName.getMethodName(), "BLPGI-00290" );

   }

}
