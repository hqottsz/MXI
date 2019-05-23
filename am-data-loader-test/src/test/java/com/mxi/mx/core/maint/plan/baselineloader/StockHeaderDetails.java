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

import com.mxi.mx.util.BaselineLoaderTest;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.ValidationAndImport;
import com.mxi.mx.util.WhereClause;


/**
 * This test suite contains test cases on validation and import functionality of
 * C_STOCK_HEADER_IMPORT and C_STOCK_DETAILS_IMPORT package in stock header and details area.
 *
 * @author ALICIA QIAN
 */
public class StockHeaderDetails extends BaselineLoaderTest {

   @Rule
   public TestName testName = new TestName();

   String iStockCD = "AUTOSTOCK";
   String iStockNM = "AUTOSTOCKNAME";


   enum iInvClass {
      KIT, TRK, SER, BATCH, ASSY
   };

   enum iAbcClass {
      A, B, C, D, BLKOUT
   };

   enum iPurchTypecd {
      ACFT, ENG, CONSUM, OFFICE, CMNHW
   };


   ValidationAndImport ivalidationandimportHeader;
   ValidationAndImport ivalidationandimportDetails;


   /**
    * Setup before executing each individual test
    *
    */
   @Before
   @Override
   public void before() throws Exception {

      super.before();
      clearBaselineLoaderTables();

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
    * This test is to verify C_STOCK_HEADER_IMPORT validation functionality of staging table
    * c_stock_header on INV_CLASS_CD=KIT
    *
    */

   public void testKIT_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_acft_subassy table
      Map<String, String> lstockheader = new LinkedHashMap<>();

      //
      lstockheader.put( "STOCK_NO_CD", "\'" + iStockCD + "\'" );
      lstockheader.put( "STOCK_NO_NAME", "\'" + iStockNM + "\'" );
      lstockheader.put( "QTY_UNIT_CD", "\'EA\'" );
      lstockheader.put( "INV_CLASS_CD", "\'" + iInvClass.KIT + "\'" );
      lstockheader.put( "ABC_CLASS_CD", "\'" + iAbcClass.A + "\'" );
      lstockheader.put( "PURCH_TYPE_CD", "\'" + iPurchTypecd.ACFT + "\'" );
      lstockheader.put( "AUTO_CREATE_PO_BOOL", "\'Y\'" );
      lstockheader.put( "AUTO_ISSUE_PO_BOOL", "\'Y\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_HEADER, lstockheader ) );

      // run validation
      Assert.assertTrue( runValidationAndImportHeader( true, true ) == 1 );

   }


   /**
    * This test is to verify C_STOCK_HEADER_IMPORT validation functionality of staging table
    * c_stock_header on INV_CLASS_CD=KIT
    *
    */
   @Test
   public void testKIT_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testKIT_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImportHeader( false, true ) == 1 );

      // Verify EQP_STOCK_NO table for header part
      verifyEQPSTOCKNOHEADER( iStockNM, iStockCD, "EA", iAbcClass.A, iInvClass.KIT, "1", "1" );

   }


   /**
    * This test is to verify C_STOCK_HEADER_IMPORT validation functionality of staging table
    * c_stock_header on INV_CLASS_CD=TRK
    *
    */
   public void testTRK_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_acft_subassy table
      Map<String, String> lstockheader = new LinkedHashMap<>();

      //
      lstockheader.put( "STOCK_NO_CD", "\'" + iStockCD + "\'" );
      lstockheader.put( "STOCK_NO_NAME", "\'" + iStockNM + "\'" );
      lstockheader.put( "QTY_UNIT_CD", "\'EA\'" );
      lstockheader.put( "INV_CLASS_CD", "\'" + iInvClass.TRK + "\'" );
      lstockheader.put( "ABC_CLASS_CD", "\'" + iAbcClass.B + "\'" );
      lstockheader.put( "PURCH_TYPE_CD", "\'" + iPurchTypecd.ENG + "\'" );
      lstockheader.put( "AUTO_CREATE_PO_BOOL", "\'Y\'" );
      lstockheader.put( "AUTO_ISSUE_PO_BOOL", "\'N\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_HEADER, lstockheader ) );

      // run validation
      Assert.assertTrue( runValidationAndImportHeader( true, true ) == 1 );

   }


   /**
    * This test is to verify C_STOCK_HEADER_IMPORT validation functionality of staging table
    * c_stock_header on INV_CLASS_CD=TRK
    *
    */
   @Test
   public void testTRK_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testTRK_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImportHeader( false, true ) == 1 );

      // Verify EQP_STOCK_NO table for header part
      verifyEQPSTOCKNOHEADER( iStockNM, iStockCD, "EA", iAbcClass.B, iInvClass.TRK, "1", "0" );

   }


   /**
    * This test is to verify C_STOCK_HEADER_IMPORT validation functionality of staging table
    * c_stock_header on INV_CLASS_CD=SER
    *
    */
   public void testSER_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_acft_subassy table
      Map<String, String> lstockheader = new LinkedHashMap<>();

      //
      lstockheader.put( "STOCK_NO_CD", "\'" + iStockCD + "\'" );
      lstockheader.put( "STOCK_NO_NAME", "\'" + iStockNM + "\'" );
      lstockheader.put( "QTY_UNIT_CD", "\'EA\'" );
      lstockheader.put( "INV_CLASS_CD", "\'" + iInvClass.SER + "\'" );
      lstockheader.put( "ABC_CLASS_CD", "\'" + iAbcClass.C + "\'" );
      lstockheader.put( "PURCH_TYPE_CD", "\'" + iPurchTypecd.CONSUM + "\'" );
      lstockheader.put( "AUTO_CREATE_PO_BOOL", "\'Y\'" );
      lstockheader.put( "AUTO_ISSUE_PO_BOOL", "\'Y\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_HEADER, lstockheader ) );

      // run validation
      Assert.assertTrue( runValidationAndImportHeader( true, true ) == 1 );

   }


   /**
    * This test is to verify C_STOCK_HEADER_IMPORT validation functionality of staging table
    * c_stock_header on INV_CLASS_CD=SER
    *
    */
   @Test
   public void testSER_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testSER_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImportHeader( false, true ) == 1 );

      // Verify EQP_STOCK_NO table for header part
      verifyEQPSTOCKNOHEADER( iStockNM, iStockCD, "EA", iAbcClass.C, iInvClass.SER, "1", "1" );

   }


   /**
    * This test is to verify C_STOCK_HEADER_IMPORT validation functionality of staging table
    * c_stock_header on INV_CLASS_CD=BATCH
    *
    */

   public void testBATCH_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_acft_subassy table
      Map<String, String> lstockheader = new LinkedHashMap<>();

      //
      lstockheader.put( "STOCK_NO_CD", "\'" + iStockCD + "\'" );
      lstockheader.put( "STOCK_NO_NAME", "\'" + iStockNM + "\'" );
      lstockheader.put( "QTY_UNIT_CD", "\'EA\'" );
      lstockheader.put( "INV_CLASS_CD", "\'" + iInvClass.BATCH + "\'" );
      lstockheader.put( "ABC_CLASS_CD", "\'" + iAbcClass.D + "\'" );
      lstockheader.put( "PURCH_TYPE_CD", "\'" + iPurchTypecd.OFFICE + "\'" );
      lstockheader.put( "AUTO_CREATE_PO_BOOL", "\'Y\'" );
      lstockheader.put( "AUTO_ISSUE_PO_BOOL", "\'Y\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_HEADER, lstockheader ) );

      // run validation
      Assert.assertTrue( runValidationAndImportHeader( true, true ) == 1 );

   }


   /**
    * This test is to verify C_STOCK_HEADER_IMPORT validation functionality of staging table
    * c_stock_header on INV_CLASS_CD=BATCH
    *
    */
   @Test
   public void testBATCH_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testBATCH_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImportHeader( false, true ) == 1 );

      // Verify EQP_STOCK_NO table for header part
      verifyEQPSTOCKNOHEADER( iStockNM, iStockCD, "EA", iAbcClass.D, iInvClass.BATCH, "1", "1" );

   }


   /**
    * This test is to verify C_STOCK_HEADER_IMPORT validation functionality of staging table
    * c_stock_header on INV_CLASS_CD=ASSY
    *
    */

   public void testASSY_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_acft_subassy table
      Map<String, String> lstockheader = new LinkedHashMap<>();

      //
      lstockheader.put( "STOCK_NO_CD", "\'" + iStockCD + "\'" );
      lstockheader.put( "STOCK_NO_NAME", "\'" + iStockNM + "\'" );
      lstockheader.put( "QTY_UNIT_CD", "\'EA\'" );
      lstockheader.put( "INV_CLASS_CD", "\'" + iInvClass.ASSY + "\'" );
      lstockheader.put( "ABC_CLASS_CD", "\'" + iAbcClass.BLKOUT + "\'" );
      lstockheader.put( "PURCH_TYPE_CD", "\'" + iPurchTypecd.CMNHW + "\'" );
      lstockheader.put( "AUTO_CREATE_PO_BOOL", "\'Y\'" );
      lstockheader.put( "AUTO_ISSUE_PO_BOOL", "\'N\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_HEADER, lstockheader ) );

      // run validation
      Assert.assertTrue( runValidationAndImportHeader( true, true ) == 1 );

   }


   /**
    * This test is to verify C_STOCK_HEADER_IMPORT validation functionality of staging table
    * c_stock_header on INV_CLASS_CD=ASSY
    *
    */
   @Test
   public void testASSY_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testASSY_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImportHeader( false, true ) == 1 );

      // Verify EQP_STOCK_NO table for header part
      verifyEQPSTOCKNOHEADER( iStockNM, iStockCD, "EA", iAbcClass.BLKOUT, iInvClass.ASSY, "1",
            "0" );

   }


   /**
    * This test is to verify C_STOCK_HEADER_IMPORT validation functionality of staging table
    * c_stock_header on INV_CLASS_CD=ASSY and INV_CLASS_CD=KIT
    *
    */

   public void testMutiple_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_acft_subassy table
      Map<String, String> lstockheader = new LinkedHashMap<>();

      //
      lstockheader.put( "STOCK_NO_CD", "\'" + iStockCD + "1\'" );
      lstockheader.put( "STOCK_NO_NAME", "\'" + iStockNM + "1\'" );
      lstockheader.put( "QTY_UNIT_CD", "\'EA\'" );
      lstockheader.put( "INV_CLASS_CD", "\'" + iInvClass.ASSY + "\'" );
      lstockheader.put( "ABC_CLASS_CD", "\'" + iAbcClass.BLKOUT + "\'" );
      lstockheader.put( "PURCH_TYPE_CD", "\'" + iPurchTypecd.CMNHW + "\'" );
      lstockheader.put( "AUTO_CREATE_PO_BOOL", "\'Y\'" );
      lstockheader.put( "AUTO_ISSUE_PO_BOOL", "\'N\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_HEADER, lstockheader ) );

      // c_acft_subassy table
      lstockheader.clear();
      lstockheader.put( "STOCK_NO_CD", "\'" + iStockCD + "2\'" );
      lstockheader.put( "STOCK_NO_NAME", "\'" + iStockNM + "2\'" );
      lstockheader.put( "QTY_UNIT_CD", "\'EA\'" );
      lstockheader.put( "INV_CLASS_CD", "\'" + iInvClass.KIT + "\'" );
      lstockheader.put( "ABC_CLASS_CD", "\'" + iAbcClass.A + "\'" );
      lstockheader.put( "PURCH_TYPE_CD", "\'" + iPurchTypecd.ACFT + "\'" );
      lstockheader.put( "AUTO_CREATE_PO_BOOL", "\'Y\'" );
      lstockheader.put( "AUTO_ISSUE_PO_BOOL", "\'Y\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_HEADER, lstockheader ) );

      // run validation
      Assert.assertTrue( runValidationAndImportHeader( true, true ) == 1 );

   }


   /**
    * This test is to verify C_STOCK_HEADER_IMPORT import functionality of staging table
    * c_stock_header on INV_CLASS_CD=BATCH and INV_CLASS_CD=KIT
    *
    */
   @Test
   public void testMutiple_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testMutiple_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImportHeader( false, true ) == 1 );

      // Verify EQP_STOCK_NO table for header part
      verifyEQPSTOCKNOHEADER( iStockNM + "1", iStockCD + "1", "EA", iAbcClass.BLKOUT,
            iInvClass.ASSY, "1", "0" );
      verifyEQPSTOCKNOHEADER( iStockNM + "2", iStockCD + "2", "EA", iAbcClass.A, iInvClass.KIT, "1",
            "1" );

   }


   /**
    * This test is to verify C_STOCK_DETAILS_IMPORT validation functionality of staging table
    * c_stock_attr on INV_CLASS_CD=KIT
    *
    */

   public void testKITDetail_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testKIT_IMPORT();

      // C_STOCK_ATTR table
      Map<String, String> lstockdetail = new LinkedHashMap<>();

      //
      lstockdetail.put( "STOCK_NO_CD", "\'" + iStockCD + "\'" );
      lstockdetail.put( "MONTHLY_DEMAND_QT", "\'1\'" );
      lstockdetail.put( "CARRY_COST_INTEREST_QT", "\'2.00000\'" );
      lstockdetail.put( "CARRY_COST_INSURANCE_QT", "\'3.00000\'" );
      lstockdetail.put( "CARRY_COST_TAXES_QT", "\'4.00000\'" );
      lstockdetail.put( "CARRY_COST_STORAGE_QT", "\'5.00000\'" );
      lstockdetail.put( "SERVICE_LVL_PCT", "\'1.0\'" );
      lstockdetail.put( "STOCK_WEIGHT_FACT", "\'7\'" );
      lstockdetail.put( "PURCHASE_LEAD_TIME", "\'10\'" );
      lstockdetail.put( "REPAIR_LEAD_TIME", "\'20\'" );
      lstockdetail.put( "SHIPPING_TIME_QT", "\'30\'" );
      lstockdetail.put( "PROCESSING_TIME_QT", "\'40\'" );
      lstockdetail.put( "TOTAL_LEAD_TIME_QT", "\'100\'" );
      lstockdetail.put( "REORDER_QT", "\'50\'" );
      lstockdetail.put( "GLOBAL_REORDER_LVL_QT", "\'2\'" );
      lstockdetail.put( "SAFETY_LEVEL_QT", "\'3\'" );
      lstockdetail.put( "SHIP_QT", "\'4\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_ATTR, lstockdetail ) );

      // run validation
      Assert.assertTrue( runValidationAndImportDetails( true, true ) == 1 );

   }


   /**
    * This test is to verify C_STOCK_DETAILS_IMPORT import functionality of staging table
    * c_stock_attr on INV_CLASS_CD=KIT
    *
    */
   @Test
   public void testKITDetail_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testKITDetail_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImportDetails( false, true ) == 1 );

      // Verify EQP_STOCK_NO table for detail part
      verifyEQPSTOCKNODETAILS( iStockNM, iStockCD, "1", "2", "3", "4", "5", "14", "1", "7", "10",
            "20", "30", "40", "100", "50", "2", "3", "4" );

   }


   /**
    * This test is to verify C_STOCK_DETAILS_IMPORT validation functionality of staging table
    * c_stock_attr on INV_CLASS_CD=TRK
    *
    */

   public void testTRKDetail_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testTRK_IMPORT();

      // c_acft_subassy table
      Map<String, String> lstockdetail = new LinkedHashMap<>();

      //
      lstockdetail.put( "STOCK_NO_CD", "\'" + iStockCD + "\'" );
      lstockdetail.put( "MONTHLY_DEMAND_QT", "\'1\'" );
      lstockdetail.put( "CARRY_COST_INTEREST_QT", "\'2.00000\'" );
      lstockdetail.put( "CARRY_COST_INSURANCE_QT", "\'3.00000\'" );
      lstockdetail.put( "CARRY_COST_TAXES_QT", "\'4.00000\'" );
      lstockdetail.put( "CARRY_COST_STORAGE_QT", "\'5.00000\'" );
      lstockdetail.put( "SERVICE_LVL_PCT", "\'1.0\'" );
      lstockdetail.put( "STOCK_WEIGHT_FACT", "\'7\'" );
      lstockdetail.put( "PURCHASE_LEAD_TIME", "\'10\'" );
      lstockdetail.put( "REPAIR_LEAD_TIME", "\'20\'" );
      lstockdetail.put( "SHIPPING_TIME_QT", "\'30\'" );
      lstockdetail.put( "PROCESSING_TIME_QT", "\'40\'" );
      lstockdetail.put( "TOTAL_LEAD_TIME_QT", "\'100\'" );
      lstockdetail.put( "REORDER_QT", "\'50\'" );
      lstockdetail.put( "GLOBAL_REORDER_LVL_QT", "\'2\'" );
      lstockdetail.put( "SAFETY_LEVEL_QT", "\'3\'" );
      lstockdetail.put( "SHIP_QT", "\'4\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_ATTR, lstockdetail ) );

      // run validation
      Assert.assertTrue( runValidationAndImportDetails( true, true ) == 1 );

   }


   /**
    * This test is to verify C_STOCK_DETAILS_IMPORT import functionality of staging table
    * c_stock_attr on INV_CLASS_CD=TRK
    *
    */
   @Test
   public void testTRKDetail_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testTRKDetail_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImportDetails( false, true ) == 1 );

      // Verify EQP_STOCK_NO table for detail part
      verifyEQPSTOCKNODETAILS( iStockNM, iStockCD, "1", "2", "3", "4", "5", "14", "1", "7", "10",
            "20", "30", "40", "100", "50", "2", "3", "4" );

   }


   /**
    * This test is to verify C_STOCK_DETAILS_IMPORT validation functionality of staging table
    * c_stock_attr on INV_CLASS_CD=SER
    *
    */

   public void testSERDetail_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testSER_IMPORT();

      // c_acft_subassy table
      Map<String, String> lstockdetail = new LinkedHashMap<>();

      //
      lstockdetail.put( "STOCK_NO_CD", "\'" + iStockCD + "\'" );
      lstockdetail.put( "MONTHLY_DEMAND_QT", "\'1\'" );
      lstockdetail.put( "CARRY_COST_INTEREST_QT", "\'2.00000\'" );
      lstockdetail.put( "CARRY_COST_INSURANCE_QT", "\'3.00000\'" );
      lstockdetail.put( "CARRY_COST_TAXES_QT", "\'4.00000\'" );
      lstockdetail.put( "CARRY_COST_STORAGE_QT", "\'5.00000\'" );
      lstockdetail.put( "SERVICE_LVL_PCT", "\'1.0\'" );
      lstockdetail.put( "STOCK_WEIGHT_FACT", "\'7\'" );
      lstockdetail.put( "PURCHASE_LEAD_TIME", "\'10\'" );
      lstockdetail.put( "REPAIR_LEAD_TIME", "\'20\'" );
      lstockdetail.put( "SHIPPING_TIME_QT", "\'30\'" );
      lstockdetail.put( "PROCESSING_TIME_QT", "\'40\'" );
      lstockdetail.put( "TOTAL_LEAD_TIME_QT", "\'100\'" );
      lstockdetail.put( "REORDER_QT", "\'50\'" );
      lstockdetail.put( "GLOBAL_REORDER_LVL_QT", "\'2\'" );
      lstockdetail.put( "SAFETY_LEVEL_QT", "\'3\'" );
      lstockdetail.put( "SHIP_QT", "\'4\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_ATTR, lstockdetail ) );

      // run validation
      Assert.assertTrue( runValidationAndImportDetails( true, true ) == 1 );

   }


   /**
    * This test is to verify C_STOCK_DETAILS_IMPORT import functionality of staging table
    * c_stock_attr on INV_CLASS_CD=SER
    *
    */
   @Test
   public void testSERDetail_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testSERDetail_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImportDetails( false, true ) == 1 );

      // Verify EQP_STOCK_NO table for detail part
      verifyEQPSTOCKNODETAILS( iStockNM, iStockCD, "1", "2", "3", "4", "5", "14", "1", "7", "10",
            "20", "30", "40", "100", "50", "2", "3", "4" );

   }


   /**
    * This test is to verify C_STOCK_DETAILS_IMPORT validation functionality of staging table
    * c_stock_attr on INV_CLASS_CD=BATCH
    *
    */

   public void testBATCHDetail_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testBATCH_IMPORT();

      // c_acft_subassy table
      Map<String, String> lstockdetail = new LinkedHashMap<>();

      //
      lstockdetail.put( "STOCK_NO_CD", "\'" + iStockCD + "\'" );
      lstockdetail.put( "MONTHLY_DEMAND_QT", "\'1\'" );
      lstockdetail.put( "CARRY_COST_INTEREST_QT", "\'2.00000\'" );
      lstockdetail.put( "CARRY_COST_INSURANCE_QT", "\'3.00000\'" );
      lstockdetail.put( "CARRY_COST_TAXES_QT", "\'4.00000\'" );
      lstockdetail.put( "CARRY_COST_STORAGE_QT", "\'5.00000\'" );
      lstockdetail.put( "SERVICE_LVL_PCT", "\'1.0\'" );
      lstockdetail.put( "STOCK_WEIGHT_FACT", "\'7\'" );
      lstockdetail.put( "PURCHASE_LEAD_TIME", "\'10\'" );
      lstockdetail.put( "REPAIR_LEAD_TIME", "\'20\'" );
      lstockdetail.put( "SHIPPING_TIME_QT", "\'30\'" );
      lstockdetail.put( "PROCESSING_TIME_QT", "\'40\'" );
      lstockdetail.put( "TOTAL_LEAD_TIME_QT", "\'100\'" );
      lstockdetail.put( "REORDER_QT", "\'50\'" );
      lstockdetail.put( "GLOBAL_REORDER_LVL_QT", "\'2\'" );
      lstockdetail.put( "SAFETY_LEVEL_QT", "\'3\'" );
      lstockdetail.put( "SHIP_QT", "\'4\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_ATTR, lstockdetail ) );

      // run validation
      Assert.assertTrue( runValidationAndImportDetails( true, true ) == 1 );

   }


   /**
    * This test is to verify C_STOCK_DETAILS_IMPORT import functionality of staging table
    * c_stock_attr on INV_CLASS_CD=BATCH
    *
    */
   @Test
   public void testBATCHDetail_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testBATCHDetail_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImportDetails( false, true ) == 1 );

      // Verify EQP_STOCK_NO table for detail part
      verifyEQPSTOCKNODETAILS( iStockNM, iStockCD, "1", "2", "3", "4", "5", "14", "1", "7", "10",
            "20", "30", "40", "100", "50", "2", "3", "4" );

   }


   /**
    * This test is to verify C_STOCK_DETAILS_IMPORT validation functionality of staging table
    * c_stock_attr on INV_CLASS_CD=ASSY
    *
    */

   public void testASSYDetail_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testASSY_IMPORT();

      // c_acft_subassy table
      Map<String, String> lstockdetail = new LinkedHashMap<>();

      //
      lstockdetail.put( "STOCK_NO_CD", "\'" + iStockCD + "\'" );
      lstockdetail.put( "MONTHLY_DEMAND_QT", "\'1\'" );
      lstockdetail.put( "CARRY_COST_INTEREST_QT", "\'2.00000\'" );
      lstockdetail.put( "CARRY_COST_INSURANCE_QT", "\'3.00000\'" );
      lstockdetail.put( "CARRY_COST_TAXES_QT", "\'4.00000\'" );
      lstockdetail.put( "CARRY_COST_STORAGE_QT", "\'5.00000\'" );
      lstockdetail.put( "SERVICE_LVL_PCT", "\'1.0\'" );
      lstockdetail.put( "STOCK_WEIGHT_FACT", "\'7\'" );
      lstockdetail.put( "PURCHASE_LEAD_TIME", "\'10\'" );
      lstockdetail.put( "REPAIR_LEAD_TIME", "\'20\'" );
      lstockdetail.put( "SHIPPING_TIME_QT", "\'30\'" );
      lstockdetail.put( "PROCESSING_TIME_QT", "\'40\'" );
      lstockdetail.put( "TOTAL_LEAD_TIME_QT", "\'100\'" );
      lstockdetail.put( "REORDER_QT", "\'50\'" );
      lstockdetail.put( "GLOBAL_REORDER_LVL_QT", "\'2\'" );
      lstockdetail.put( "SAFETY_LEVEL_QT", "\'3\'" );
      lstockdetail.put( "SHIP_QT", "\'4\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_ATTR, lstockdetail ) );

      // run validation
      Assert.assertTrue( runValidationAndImportDetails( true, true ) == 1 );

   }


   /**
    * This test is to verify C_STOCK_DETAILS_IMPORT import functionality of staging table
    * c_stock_attr on INV_CLASS_CD=ASSY
    */
   @Test
   public void testASSYDetail_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testASSYDetail_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImportDetails( false, true ) == 1 );

      // Verify EQP_STOCK_NO table for detail part
      verifyEQPSTOCKNODETAILS( iStockNM, iStockCD, "1", "2", "3", "4", "5", "14", "1", "7", "10",
            "20", "30", "40", "100", "50", "2", "3", "4" );

   }


   /**
    * This test is to verify C_STOCK_DETAILS_IMPORT validation functionality of staging table
    * c_stock_attr on INV_CLASS_CD=ASSY and INV_CLASS_CD=KIT
    *
    */

   public void testMutipleDetail_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testMutiple_IMPORT();

      // c_stock_attr table
      Map<String, String> lstockdetail = new LinkedHashMap<>();

      //
      lstockdetail.put( "STOCK_NO_CD", "\'" + iStockCD + "1\'" );
      lstockdetail.put( "MONTHLY_DEMAND_QT", "\'1\'" );
      lstockdetail.put( "CARRY_COST_INTEREST_QT", "\'2.00000\'" );
      lstockdetail.put( "CARRY_COST_INSURANCE_QT", "\'3.00000\'" );
      lstockdetail.put( "CARRY_COST_TAXES_QT", "\'4.00000\'" );
      lstockdetail.put( "CARRY_COST_STORAGE_QT", "\'5.00000\'" );
      lstockdetail.put( "SERVICE_LVL_PCT", "\'1.0\'" );
      lstockdetail.put( "STOCK_WEIGHT_FACT", "\'7\'" );
      lstockdetail.put( "PURCHASE_LEAD_TIME", "\'10\'" );
      lstockdetail.put( "REPAIR_LEAD_TIME", "\'20\'" );
      lstockdetail.put( "SHIPPING_TIME_QT", "\'30\'" );
      lstockdetail.put( "PROCESSING_TIME_QT", "\'40\'" );
      lstockdetail.put( "TOTAL_LEAD_TIME_QT", "\'100\'" );
      lstockdetail.put( "REORDER_QT", "\'50\'" );
      lstockdetail.put( "GLOBAL_REORDER_LVL_QT", "\'2\'" );
      lstockdetail.put( "SAFETY_LEVEL_QT", "\'3\'" );
      lstockdetail.put( "SHIP_QT", "\'4\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_ATTR, lstockdetail ) );

      // c_stock_attr table
      lstockdetail.clear();
      lstockdetail.put( "STOCK_NO_CD", "\'" + iStockCD + "2\'" );
      lstockdetail.put( "MONTHLY_DEMAND_QT", "\'1\'" );
      lstockdetail.put( "CARRY_COST_INTEREST_QT", "\'2.00000\'" );
      lstockdetail.put( "CARRY_COST_INSURANCE_QT", "\'3.00000\'" );
      lstockdetail.put( "CARRY_COST_TAXES_QT", "\'4.00000\'" );
      lstockdetail.put( "CARRY_COST_STORAGE_QT", "\'5.00000\'" );
      lstockdetail.put( "SERVICE_LVL_PCT", "\'1.0\'" );
      lstockdetail.put( "STOCK_WEIGHT_FACT", "\'7\'" );
      lstockdetail.put( "PURCHASE_LEAD_TIME", "\'10\'" );
      lstockdetail.put( "REPAIR_LEAD_TIME", "\'20\'" );
      lstockdetail.put( "SHIPPING_TIME_QT", "\'30\'" );
      lstockdetail.put( "PROCESSING_TIME_QT", "\'40\'" );
      lstockdetail.put( "TOTAL_LEAD_TIME_QT", "\'100\'" );
      lstockdetail.put( "REORDER_QT", "\'50\'" );
      lstockdetail.put( "GLOBAL_REORDER_LVL_QT", "\'2\'" );
      lstockdetail.put( "SAFETY_LEVEL_QT", "\'3\'" );
      lstockdetail.put( "SHIP_QT", "\'4\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_ATTR, lstockdetail ) );

      // run validation
      Assert.assertTrue( runValidationAndImportDetails( true, true ) == 1 );

   }


   /**
    * This test is to verify C_STOCK_DETAILS_IMPORT import functionality of staging table
    * c_stock_attr on INV_CLASS_CD=ASSY and INV_CLASS_CD=KIT
    */
   @Test
   public void testMutipleDetail_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testMutipleDetail_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImportDetails( false, true ) == 1 );

      // Verify EQP_STOCK_NO table for detail part
      verifyEQPSTOCKNODETAILS( iStockNM + "1", iStockCD + "1", "1", "2", "3", "4", "5", "14", "1",
            "7", "10", "20", "30", "40", "100", "50", "2", "3", "4" );
      verifyEQPSTOCKNODETAILS( iStockNM + "2", iStockCD + "2", "1", "2", "3", "4", "5", "14", "1",
            "7", "10", "20", "30", "40", "100", "50", "2", "3", "4" );

   }


   // ====================================================================================================================

   /**
    * This function is to verify eqp_stock_no table header part
    *
    *
    */

   public void verifyEQPSTOCKNOHEADER( String aSTOCKNONAME, String aSTOCKNOCD, String aQTYUNITCD,
         iAbcClass aABCCLASSCD, iInvClass aINVCLASSCD, String aAUTOCREATEPOBOOL,
         String aAUTOISSUEPOBOOL ) {

      // EQP_STOCK_NO table
      String[] iIds = { "STOCK_NO_DB_ID", "STOCK_NO_ID", "QTY_UNIT_CD", "ABC_CLASS_CD",
            "INV_CLASS_CD", "PURCH_TYPE_CD", "AUTO_CREATE_PO_BOOL", "AUTO_ISSUE_PO_BOOL" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "STOCK_NO_NAME", aSTOCKNONAME );
      lArgs.addArguments( "STOCK_NO_CD", aSTOCKNOCD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_STOCK_NO, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Validation
      Assert.assertTrue( "QTY_UNIT_CD", llists.get( 0 ).get( 2 ).equalsIgnoreCase( aQTYUNITCD ) );
      Assert.assertTrue( "ABC_CLASS_CD",
            llists.get( 0 ).get( 3 ).equalsIgnoreCase( aABCCLASSCD.name() ) );
      Assert.assertTrue( "INV_CLASS_CD",
            llists.get( 0 ).get( 4 ).equalsIgnoreCase( aINVCLASSCD.name() ) );
      Assert.assertTrue( "AUTO_CREATE_PO_BOOL",
            llists.get( 0 ).get( 6 ).equalsIgnoreCase( aAUTOCREATEPOBOOL ) );
      Assert.assertTrue( "AUTO_ISSUE_PO_BOOL",
            llists.get( 0 ).get( 7 ).equalsIgnoreCase( aAUTOISSUEPOBOOL ) );

   }


   /**
    * This function is to verify eqp_stock_no table details part
    *
    *
    */

   public void verifyEQPSTOCKNODETAILS( String aSTOCKNONAME, String aSTOCKNOCD,
         String aMONTHLYDEMANDQT, String aCARRYCOSTINTQT, String aCARRYCOSTINSQT,
         String aCARRYCOSTTAXESQT, String aCARRYCOSTSTOQT, String aCARRYCOSTTTLQT,
         String aSERVICELVLPCT, String aMAXMULTQT, String aPURCHASELEADTIME, String aREPAILEADTIME,
         String aSHIPPINGTIMEQT, String aPROCESSINGTIMEQT, String aTOTALLEADTIMEQT,
         String aBATCHSIZE, String aGLOBALREORDERQT, String aSAFETYLEVELQT, String aSHIPQT ) {

      // EQP_STOCK_NO table
      String[] iIds = { "MONTHLY_DEMAND_QT", "CARRY_COST_INTEREST_QT", "CARRY_COST_INSURANCE_QT",
            "CARRY_COST_TAXES_QT", "CARRY_COST_STORAGE_QT", "CARRY_COST_TOTAL_QT",
            "SERVICE_LVL_PCT", "MAX_MULT_QT", "PURCHASE_LEAD_TIME", "REPAIR_LEAD_TIME",
            "SHIPPING_TIME_QT", "PROCESSING_TIME_QT", "TOTAL_LEAD_TIME_QT", "BATCH_SIZE",
            "GLOBAL_REORDER_QT", "SAFETY_LEVEL_QT", "SHIP_QT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "STOCK_NO_NAME", aSTOCKNONAME );
      lArgs.addArguments( "STOCK_NO_CD", aSTOCKNOCD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_STOCK_NO, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Validation
      Assert.assertTrue( "MONTHLY_DEMAND_QT",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aMONTHLYDEMANDQT ) );
      Assert.assertTrue( "CARRY_COST_INTEREST_QT",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aCARRYCOSTINTQT ) );
      Assert.assertTrue( "CARRY_COST_INSURANCE_QT",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aCARRYCOSTINSQT ) );
      Assert.assertTrue( "CARRY_COST_TAXES_QT",
            llists.get( 0 ).get( 3 ).equalsIgnoreCase( aCARRYCOSTTAXESQT ) );
      Assert.assertTrue( "CARRY_COST_STORAGE_QT",
            llists.get( 0 ).get( 4 ).equalsIgnoreCase( aCARRYCOSTSTOQT ) );
      Assert.assertTrue( "CARRY_COST_TOTAL_QT",
            llists.get( 0 ).get( 5 ).equalsIgnoreCase( aCARRYCOSTTTLQT ) );
      Assert.assertTrue( "SERVICE_LVL_PCT",
            llists.get( 0 ).get( 6 ).equalsIgnoreCase( aSERVICELVLPCT ) );
      Assert.assertTrue( "MAX_MULT_QT", llists.get( 0 ).get( 7 ).equalsIgnoreCase( aMAXMULTQT ) );
      Assert.assertTrue( "PURCHASE_LEAD_TIME",
            llists.get( 0 ).get( 8 ).equalsIgnoreCase( aPURCHASELEADTIME ) );
      Assert.assertTrue( "REPAIR_LEAD_TIME",
            llists.get( 0 ).get( 9 ).equalsIgnoreCase( aREPAILEADTIME ) );
      Assert.assertTrue( "SHIPPING_TIME_QT",
            llists.get( 0 ).get( 10 ).equalsIgnoreCase( aSHIPPINGTIMEQT ) );
      Assert.assertTrue( "PROCESSING_TIME_QT",
            llists.get( 0 ).get( 11 ).equalsIgnoreCase( aPROCESSINGTIMEQT ) );
      Assert.assertTrue( "TOTAL_LEAD_TIME_QT",
            llists.get( 0 ).get( 12 ).equalsIgnoreCase( aTOTALLEADTIMEQT ) );
      Assert.assertTrue( "BATCH_SIZE", llists.get( 0 ).get( 13 ).equalsIgnoreCase( aBATCHSIZE ) );
      Assert.assertTrue( "GLOBAL_REORDER_QT",
            llists.get( 0 ).get( 14 ).equalsIgnoreCase( aGLOBALREORDERQT ) );
      Assert.assertTrue( "SAFETY_LEVEL_QT",
            llists.get( 0 ).get( 15 ).equalsIgnoreCase( aSAFETYLEVELQT ) );
      Assert.assertTrue( "SHIP_QT", llists.get( 0 ).get( 16 ).equalsIgnoreCase( aSHIPQT ) );

   }


   /**
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */

   public void RestoreData() {
      // Delete EQP_STOCK_NO table
      String lStrDeleteEQPSTOCKNO = "delete from " + TableUtil.EQP_STOCK_NO;
      executeSQL( lStrDeleteEQPSTOCKNO );

   }


   /**
    * This function is to implement interface ValidationAndImportHeader
    *
    * @param: blnOnlyValidation
    * @param: allornone
    *
    */
   public int runValidationAndImportHeader( boolean blnOnlyValidation, boolean allornone ) {
      int rtValue = 0;
      ivalidationandimportHeader = new ValidationAndImport() {

         @Override
         public int runValidation( boolean allornone ) {
            int lReturn = 0;

            try {
               CallableStatement lPrepareCallInventory = getConnection().prepareCall(
                     "BEGIN c_stock_header_import.validate_stock_header(on_retcode => ?); END;" );

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
               CallableStatement lPrepareCallInventory = getConnection().prepareCall(
                     "BEGIN c_stock_header_import.import_stock_header(on_retcode => ?); END;" );

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

      rtValue = blnOnlyValidation ? ivalidationandimportHeader.runValidation( allornone )
            : ivalidationandimportHeader.runImport( allornone );

      return rtValue;
   }


   /**
    * This function is to implement interface ValidationAndImportDetails
    *
    * @param: blnOnlyValidation
    * @param: allornone
    *
    */
   public int runValidationAndImportDetails( boolean blnOnlyValidation, boolean allornone ) {
      int rtValue = 0;
      ivalidationandimportDetails = new ValidationAndImport() {

         @Override
         public int runValidation( boolean allornone ) {
            int lReturn = 0;

            try {
               CallableStatement lPrepareCallInventory = getConnection().prepareCall(
                     "BEGIN c_stock_details_import.validate_stock_attribute(on_retcode => ?); END;" );

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
               CallableStatement lPrepareCallInventory = getConnection().prepareCall(
                     "BEGIN c_stock_details_import.import_stock_attribute(on_retcode => ?); END;" );

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

      rtValue = blnOnlyValidation ? ivalidationandimportDetails.runValidation( allornone )
            : ivalidationandimportDetails.runImport( allornone );

      return rtValue;
   }

}
