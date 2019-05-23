package com.mxi.mx.core.maint.plan.baselineloader.ComHW;

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
 * This test suite contains test cases on validation and import functionality of
 * BL_SENSITIVITY_IMPORT package.
 *
 * @author ALICIA QIAN
 */
public class ComHWValidation extends ComHW {

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
      clearBaselineLoaderTables();

   }


   /**
    * Clean up after each individual test
    */
   @After
   @Override
   public void after() {
      // RestoreData();
      super.after();
   }


   /**
    * This test is to verify valid error code CFGCHW-10001:C_COMHW_DEF.PART_NO_OEM cannot be NULL or
    * consist entirely of spaces.
    *
    *
    */

   @Test
   public void test_CFGCHW_10001() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_comhw_def table
      Map<String, String> lComHW = new LinkedHashMap<>();

      lComHW.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_2 + "\'" );
      lComHW.put( "PART_GROUP_NAME", "\'" + iPART_GROUP_NAME_2 + "\'" );
      // lComHW.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_2 + "\'" );
      lComHW.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "PART_NO_SDESC", "\'" + iPART_NO_SDESC + "\'" );
      lComHW.put( "INV_CLASS_CD", "\'" + iINV_CLASS_CD_BATCH + "\'" );
      lComHW.put( "INTERCHG_ORD", "'1'" );
      lComHW.put( "PART_BASELINE_CD", "\'" + iPART_BASELINE_CD + "\'" );
      lComHW.put( "ALT_PRIMARY_PN", "\'" + iPART_NO_OEM_2 + "\'" );
      lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "APPL_EFF_LDESC", "\'" + iAPPL_EFF_LDESC + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMHW_DEF, lComHW ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkCOMMHWErrorCode( testName.getMethodName(), "CFGCHW-10001" );

   }


   /**
    * This test is to verify valid error code CFGCHW-10002:C_COMHW_DEF.MANUFACT_CD cannot be NULL or
    * consist entirely of spaces.
    *
    *
    */

   @Test
   public void test_CFGCHW_10002() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_comhw_def table
      Map<String, String> lComHW = new LinkedHashMap<>();

      lComHW.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_2 + "\'" );
      lComHW.put( "PART_GROUP_NAME", "\'" + iPART_GROUP_NAME_2 + "\'" );
      lComHW.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_2 + "\'" );
      // lComHW.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "PART_NO_SDESC", "\'" + iPART_NO_SDESC + "\'" );
      lComHW.put( "INV_CLASS_CD", "\'" + iINV_CLASS_CD_BATCH + "\'" );
      lComHW.put( "INTERCHG_ORD", "'1'" );
      lComHW.put( "PART_BASELINE_CD", "\'" + iPART_BASELINE_CD + "\'" );
      lComHW.put( "ALT_PRIMARY_PN", "\'" + iPART_NO_OEM_2 + "\'" );
      lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "APPL_EFF_LDESC", "\'" + iAPPL_EFF_LDESC + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMHW_DEF, lComHW ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkCOMMHWErrorCode( testName.getMethodName(), "CFGCHW-10002" );

   }


   /**
    * This test is to verify valid error code CFGCHW-10003:C_COMHW_DEF.PART_NO_SDESC cannot be NULL
    * or consist entirely of spaces.
    *
    *
    */

   @Test
   public void test_CFGCHW_10003() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_comhw_def table
      Map<String, String> lComHW = new LinkedHashMap<>();

      lComHW.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_2 + "\'" );
      lComHW.put( "PART_GROUP_NAME", "\'" + iPART_GROUP_NAME_2 + "\'" );
      lComHW.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_2 + "\'" );
      lComHW.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      // lComHW.put( "PART_NO_SDESC", "\'" + iPART_NO_SDESC + "\'" );
      lComHW.put( "INV_CLASS_CD", "\'" + iINV_CLASS_CD_BATCH + "\'" );
      lComHW.put( "INTERCHG_ORD", "'1'" );
      lComHW.put( "PART_BASELINE_CD", "\'" + iPART_BASELINE_CD + "\'" );
      lComHW.put( "ALT_PRIMARY_PN", "\'" + iPART_NO_OEM_2 + "\'" );
      lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "APPL_EFF_LDESC", "\'" + iAPPL_EFF_LDESC + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMHW_DEF, lComHW ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkCOMMHWErrorCode( testName.getMethodName(), "CFGCHW-10003" );

   }


   /**
    * This test is to verify valid error code CFGCHW-10004:C_COMHW_DEF.INV_CLASS_CD cannot be NULL
    *
    *
    */

   @Test
   public void test_CFGCHW_10004() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_comhw_def table
      Map<String, String> lComHW = new LinkedHashMap<>();

      lComHW.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_2 + "\'" );
      lComHW.put( "PART_GROUP_NAME", "\'" + iPART_GROUP_NAME_2 + "\'" );
      lComHW.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_2 + "\'" );
      lComHW.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "PART_NO_SDESC", "\'" + iPART_NO_SDESC + "\'" );
      // lComHW.put( "INV_CLASS_CD", "\'" + iINV_CLASS_CD_BATCH + "\'" );
      lComHW.put( "INTERCHG_ORD", "'1'" );
      lComHW.put( "PART_BASELINE_CD", "\'" + iPART_BASELINE_CD + "\'" );
      lComHW.put( "ALT_PRIMARY_PN", "\'" + iPART_NO_OEM_2 + "\'" );
      lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "APPL_EFF_LDESC", "\'" + iAPPL_EFF_LDESC + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMHW_DEF, lComHW ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkCOMMHWErrorCode( testName.getMethodName(), "CFGCHW-10004" );

   }


   /**
    * This test is to verify valid error code CFGCHW-10005:C_COMHW_DEF.ALT_PRIMARY_PN cannot be
    * NULL.
    *
    *
    */

   @Test
   public void test_CFGCHW_10005() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_comhw_def table
      Map<String, String> lComHW = new LinkedHashMap<>();

      lComHW.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_2 + "\'" );
      lComHW.put( "PART_GROUP_NAME", "\'" + iPART_GROUP_NAME_2 + "\'" );
      lComHW.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_2 + "\'" );
      lComHW.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "PART_NO_SDESC", "\'" + iPART_NO_SDESC + "\'" );
      lComHW.put( "INV_CLASS_CD", "\'" + iINV_CLASS_CD_BATCH + "\'" );
      lComHW.put( "INTERCHG_ORD", "'1'" );
      lComHW.put( "PART_BASELINE_CD", "\'" + iPART_BASELINE_CD + "\'" );
      // lComHW.put( "ALT_PRIMARY_PN", "\'" + iPART_NO_OEM_2 + "\'" );
      lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "APPL_EFF_LDESC", "\'" + iAPPL_EFF_LDESC + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMHW_DEF, lComHW ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkCOMMHWErrorCode( testName.getMethodName(), "CFGCHW-10005" );

   }


   /**
    * This test is to verify valid error code CFGCHW-10006:C_COMHW_DEF.ALT_PRIMARY_MANUFACT_CD
    * cannot be NULL.
    *
    *
    */

   @Test
   public void test_CFGCHW_10006() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_comhw_def table
      Map<String, String> lComHW = new LinkedHashMap<>();

      lComHW.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_2 + "\'" );
      lComHW.put( "PART_GROUP_NAME", "\'" + iPART_GROUP_NAME_2 + "\'" );
      lComHW.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_2 + "\'" );
      lComHW.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "PART_NO_SDESC", "\'" + iPART_NO_SDESC + "\'" );
      lComHW.put( "INV_CLASS_CD", "\'" + iINV_CLASS_CD_BATCH + "\'" );
      lComHW.put( "INTERCHG_ORD", "'1'" );
      lComHW.put( "PART_BASELINE_CD", "\'" + iPART_BASELINE_CD + "\'" );
      lComHW.put( "ALT_PRIMARY_PN", "\'" + iPART_NO_OEM_2 + "\'" );
      // lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "APPL_EFF_LDESC", "\'" + iAPPL_EFF_LDESC + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMHW_DEF, lComHW ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkCOMMHWErrorCode( testName.getMethodName(), "CFGCHW-10006" );

   }


   /**
    * This test is to verify valid error code CFGCHW-10007:C_COMHW_DEF.PART_GROUP_CD cannot be NULL.
    *
    *
    */

   @Test
   public void test_CFGCHW_10007() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_comhw_def table
      Map<String, String> lComHW = new LinkedHashMap<>();

      // lComHW.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_2 + "\'" );
      lComHW.put( "PART_GROUP_NAME", "\'" + iPART_GROUP_NAME_2 + "\'" );
      lComHW.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_2 + "\'" );
      lComHW.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "PART_NO_SDESC", "\'" + iPART_NO_SDESC + "\'" );
      lComHW.put( "INV_CLASS_CD", "\'" + iINV_CLASS_CD_BATCH + "\'" );
      lComHW.put( "INTERCHG_ORD", "'1'" );
      lComHW.put( "PART_BASELINE_CD", "\'" + iPART_BASELINE_CD + "\'" );
      lComHW.put( "ALT_PRIMARY_PN", "\'" + iPART_NO_OEM_2 + "\'" );
      lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "APPL_EFF_LDESC", "\'" + iAPPL_EFF_LDESC + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMHW_DEF, lComHW ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkCOMMHWErrorCode( testName.getMethodName(), "CFGCHW-10007" );

   }


   /**
    * This test is to verify valid error code CFGCHW-10008:C_COMHW_DEF.PART_GROUP_NAME cannot be
    * NULL.
    *
    *
    */

   @Test
   public void test_CFGCHW_10008() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_comhw_def table
      Map<String, String> lComHW = new LinkedHashMap<>();

      lComHW.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_2 + "\'" );
      // lComHW.put( "PART_GROUP_NAME", "\'" + iPART_GROUP_NAME_2 + "\'" );
      lComHW.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_2 + "\'" );
      lComHW.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "PART_NO_SDESC", "\'" + iPART_NO_SDESC + "\'" );
      lComHW.put( "INV_CLASS_CD", "\'" + iINV_CLASS_CD_BATCH + "\'" );
      lComHW.put( "INTERCHG_ORD", "'1'" );
      lComHW.put( "PART_BASELINE_CD", "\'" + iPART_BASELINE_CD + "\'" );
      lComHW.put( "ALT_PRIMARY_PN", "\'" + iPART_NO_OEM_2 + "\'" );
      lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "APPL_EFF_LDESC", "\'" + iAPPL_EFF_LDESC + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMHW_DEF, lComHW ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkCOMMHWErrorCode( testName.getMethodName(), "CFGCHW-10008" );

   }


   /**
    * This test is to verify valid error code CFGCHW-11000:C_COMHW_DEF.part group code/part
    * number/manufacturer cannot be duplicated in staging area.
    *
    *
    */

   @Test
   public void test_CFGCHW_11000() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_comhw_def table
      Map<String, String> lComHW = new LinkedHashMap<>();

      lComHW.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_2 + "\'" );
      lComHW.put( "PART_GROUP_NAME", "\'" + iPART_GROUP_NAME_2 + "\'" );
      lComHW.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_2 + "\'" );
      lComHW.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "PART_NO_SDESC", "\'" + iPART_NO_SDESC + "\'" );
      lComHW.put( "INV_CLASS_CD", "\'" + iINV_CLASS_CD_BATCH + "\'" );
      lComHW.put( "INTERCHG_ORD", "'1'" );
      lComHW.put( "PART_BASELINE_CD", "\'" + iPART_BASELINE_CD + "\'" );
      lComHW.put( "ALT_PRIMARY_PN", "\'" + iPART_NO_OEM_2 + "\'" );
      lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "APPL_EFF_LDESC", "\'" + iAPPL_EFF_LDESC + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMHW_DEF, lComHW ) );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMHW_DEF, lComHW ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkCOMMHWErrorCode( testName.getMethodName(), "CFGCHW-11000" );

   }


   /**
    * This test is to verify valid error code CFGCHW-11001:C_COMHW_DEF.there must be only one
    * standard part for a part group.
    *
    *
    */

   @Test
   public void test_CFGCHW_11001() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lComHW = new LinkedHashMap<>();
      // First record
      lComHW.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_1 + "\'" );
      lComHW.put( "PART_GROUP_NAME", "\'" + iPART_GROUP_NAME_1 + "\'" );
      lComHW.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_1 + "\'" );
      lComHW.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lComHW.put( "PART_NO_SDESC", "\'" + iPART_NO_SDESC + "\'" );
      lComHW.put( "INV_CLASS_CD", "\'" + iINV_CLASS_CD_BATCH + "\'" );
      lComHW.put( "INTERCHG_ORD", "'1'" );
      lComHW.put( "PART_BASELINE_CD", "\'" + iPART_BASELINE_CD + "\'" );
      lComHW.put( "ALT_PRIMARY_PN", "\'" + iPART_NO_OEM_1 + "\'" );
      lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lComHW.put( "APPL_EFF_LDESC", "\'" + iAPPL_EFF_LDESC + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMHW_DEF, lComHW ) );

      // Second record
      lComHW.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_1 + "\'" );
      lComHW.put( "PART_GROUP_NAME", "\'" + iPART_GROUP_NAME_1 + "\'" );
      lComHW.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_2 + "\'" );
      lComHW.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "PART_NO_SDESC", "\'" + iPART_NO_SDESC + "\'" );
      lComHW.put( "INV_CLASS_CD", "\'" + iINV_CLASS_CD_BATCH + "\'" );
      lComHW.put( "INTERCHG_ORD", "'2'" );
      lComHW.put( "PART_BASELINE_CD", "\'" + iPART_BASELINE_CD + "\'" );
      lComHW.put( "ALT_PRIMARY_PN", "\'" + iPART_NO_OEM_2 + "\'" );
      lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "APPL_EFF_LDESC", "\'" + iAPPL_EFF_LDESC + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMHW_DEF, lComHW ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkCOMMHWErrorCode( testName.getMethodName(), "CFGCHW-11001" );

   }


   /**
    * This test is to verify valid error code CFGCHW-12001:C_COMHW_DEF.MANUFACT_CD is not a valid
    * value.
    *
    *
    */

   @Test
   public void test_CFGCHW_12001() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_comhw_def table
      Map<String, String> lComHW = new LinkedHashMap<>();

      lComHW.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_2 + "\'" );
      lComHW.put( "PART_GROUP_NAME", "\'" + iPART_GROUP_NAME_2 + "\'" );
      lComHW.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_2 + "\'" );
      // lComHW.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "MANUFACT_CD", "'INVALID'" );
      lComHW.put( "PART_NO_SDESC", "\'" + iPART_NO_SDESC + "\'" );
      lComHW.put( "INV_CLASS_CD", "\'" + iINV_CLASS_CD_BATCH + "\'" );
      lComHW.put( "INTERCHG_ORD", "'1'" );
      lComHW.put( "PART_BASELINE_CD", "\'" + iPART_BASELINE_CD + "\'" );
      lComHW.put( "ALT_PRIMARY_PN", "\'" + iPART_NO_OEM_2 + "\'" );
      // lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "'INVALID'" );
      lComHW.put( "APPL_EFF_LDESC", "\'" + iAPPL_EFF_LDESC + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMHW_DEF, lComHW ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkCOMMHWErrorCode( testName.getMethodName(), "CFGCHW-12001" );

   }


   /**
    * This test is to verify valid error code CFGCHW-12002:C_COMHW_DEF.INV_CLASS_CD is not a valid
    * value.
    *
    *
    */

   @Test
   public void test_CFGCHW_12002() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_comhw_def table
      Map<String, String> lComHW = new LinkedHashMap<>();

      lComHW.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_2 + "\'" );
      lComHW.put( "PART_GROUP_NAME", "\'" + iPART_GROUP_NAME_2 + "\'" );
      lComHW.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_2 + "\'" );
      lComHW.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "PART_NO_SDESC", "\'" + iPART_NO_SDESC + "\'" );
      lComHW.put( "INV_CLASS_CD", "\'" + iINV_CLASS_CD_TRK + "\'" );
      lComHW.put( "INTERCHG_ORD", "'1'" );
      lComHW.put( "PART_BASELINE_CD", "\'" + iPART_BASELINE_CD + "\'" );
      lComHW.put( "ALT_PRIMARY_PN", "\'" + iPART_NO_OEM_2 + "\'" );
      lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "APPL_EFF_LDESC", "\'" + iAPPL_EFF_LDESC + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMHW_DEF, lComHW ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkCOMMHWErrorCode( testName.getMethodName(), "CFGCHW-12002" );

   }


   /**
    * This test is to verify valid error code CFGCHW-12003:C_COMHW_DEF.ALT_PRIMARY_PN is not a valid
    * value.
    *
    *
    */

   @Test
   public void test_CFGCHW_12003() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_comhw_def table
      Map<String, String> lComHW = new LinkedHashMap<>();

      lComHW.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_2 + "\'" );
      lComHW.put( "PART_GROUP_NAME", "\'" + iPART_GROUP_NAME_2 + "\'" );
      lComHW.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_2 + "\'" );
      lComHW.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "PART_NO_SDESC", "\'" + iPART_NO_SDESC + "\'" );
      lComHW.put( "INV_CLASS_CD", "\'" + iINV_CLASS_CD_BATCH + "\'" );
      lComHW.put( "INTERCHG_ORD", "'1'" );
      lComHW.put( "PART_BASELINE_CD", "\'" + iPART_BASELINE_CD + "\'" );
      lComHW.put( "ALT_PRIMARY_PN", "'INVALID'" );
      lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "APPL_EFF_LDESC", "\'" + iAPPL_EFF_LDESC + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMHW_DEF, lComHW ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkCOMMHWErrorCode( testName.getMethodName(), "CFGCHW-12003" );

   }


   /**
    * This test is to verify valid error code
    * CFGCHW-12004:C_COMHW_DEF.ALT_PRIMARY_PN/ALT_PRIMARY_MANUFACT_CD must be present in C_COMHW_DEF
    * with its own PART_NO_OEM/MANUFACT_CD same as the ALT_PRIMARY_PN/ALT_PRIMARY_MANUFACT_CD.
    *
    *
    *
    */

   @Test
   public void test_CFGCHW_12004() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_comhw_def table
      Map<String, String> lComHW = new LinkedHashMap<>();

      // First record
      lComHW.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_1 + "\'" );
      lComHW.put( "PART_GROUP_NAME", "\'" + iPART_GROUP_NAME_1 + "\'" );
      lComHW.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_1 + "\'" );
      lComHW.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lComHW.put( "PART_NO_SDESC", "\'" + iPART_NO_SDESC + "\'" );
      lComHW.put( "INV_CLASS_CD", "\'" + iINV_CLASS_CD_BATCH + "\'" );
      lComHW.put( "INTERCHG_ORD", "'1'" );
      lComHW.put( "PART_BASELINE_CD", "\'" + iPART_BASELINE_CD + "\'" );
      lComHW.put( "ALT_PRIMARY_PN", "\'" + iPART_NO_OEM_2 + "\'" );
      lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "APPL_EFF_LDESC", "\'" + iAPPL_EFF_LDESC + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMHW_DEF, lComHW ) );

      // Second record
      lComHW.clear();
      lComHW.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_1 + "\'" );
      lComHW.put( "PART_GROUP_NAME", "\'" + iPART_GROUP_NAME_1 + "\'" );
      lComHW.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_2 + "\'" );
      lComHW.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "PART_NO_SDESC", "\'" + iPART_NO_SDESC + "\'" );
      lComHW.put( "INV_CLASS_CD", "\'" + iINV_CLASS_CD_BATCH + "\'" );
      lComHW.put( "INTERCHG_ORD", "'2'" );
      lComHW.put( "PART_BASELINE_CD", "\'" + iPART_BASELINE_CD + "\'" );
      lComHW.put( "ALT_PRIMARY_PN", "\'" + iPART_NO_OEM_1 + "\'" );
      lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lComHW.put( "APPL_EFF_LDESC", "\'" + iAPPL_EFF_LDESC + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMHW_DEF, lComHW ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkCOMMHWErrorCode( testName.getMethodName(), "CFGCHW-12004" );

   }


   /**
    * This test is to verify valid error code CFGCHW-12005:All part numbers in C_COMHW_DEF with the
    * same ALT_PRIMARY_PN PN / ALT_PRIMARY_MANUFACT_CD must have the same INV_CLASS_CD. A part in
    * C_COMHW_DEF must also have the same INV_CLASS_CD as the one in Maintenix if it already exists
    * in Maintenix ( EQP_PART_NO table).
    *
    *
    */

   @Test
   public void test_CFGCHW_12005() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_comhw_def table
      Map<String, String> lComHW = new LinkedHashMap<>();
      // First record
      lComHW.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_1 + "\'" );
      lComHW.put( "PART_GROUP_NAME", "\'" + iPART_GROUP_NAME_1 + "\'" );
      lComHW.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_1 + "\'" );
      lComHW.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lComHW.put( "PART_NO_SDESC", "\'" + iPART_NO_SDESC + "\'" );
      lComHW.put( "INV_CLASS_CD", "\'" + iINV_CLASS_CD_BATCH + "\'" );
      lComHW.put( "INTERCHG_ORD", "'1'" );
      lComHW.put( "PART_BASELINE_CD", "\'" + iPART_BASELINE_CD + "\'" );
      lComHW.put( "ALT_PRIMARY_PN", "\'" + iPART_NO_OEM_1 + "\'" );
      lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lComHW.put( "APPL_EFF_LDESC", "\'" + iAPPL_EFF_LDESC + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMHW_DEF, lComHW ) );

      // Second record
      lComHW.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_1 + "\'" );
      lComHW.put( "PART_GROUP_NAME", "\'" + iPART_GROUP_NAME_1 + "\'" );
      lComHW.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_2 + "\'" );
      lComHW.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "PART_NO_SDESC", "\'" + iPART_NO_SDESC + "\'" );
      lComHW.put( "INV_CLASS_CD", "\'" + iINV_CLASS_CD_SER + "\'" );
      lComHW.put( "INTERCHG_ORD", "'2'" );
      lComHW.put( "PART_BASELINE_CD", "\'" + iPART_BASELINE_CD + "\'" );
      lComHW.put( "ALT_PRIMARY_PN", "\'" + iPART_NO_OEM_1 + "\'" );
      lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lComHW.put( "APPL_EFF_LDESC", "\'" + iAPPL_EFF_LDESC + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMHW_DEF, lComHW ) );
      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkCOMMHWErrorCode( testName.getMethodName(), "CFGCHW-12005" );

   }


   /**
    * This test is to verify valid error code CFGCHW-12006:A manufacturer code listed as C_COMHW_
    * DEF.ALT_PRIMARY_MANUFACT_CD must be present in C COMHW _DEF as a MANUFACT_CD.
    *
    *
    */

   @Test
   public void test_CFGCHW_12006() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_comhw_def table
      Map<String, String> lComHW = new LinkedHashMap<>();

      lComHW.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_2 + "\'" );
      lComHW.put( "PART_GROUP_NAME", "\'" + iPART_GROUP_NAME_2 + "\'" );
      lComHW.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_2 + "\'" );
      lComHW.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "PART_NO_SDESC", "\'" + iPART_NO_SDESC + "\'" );
      lComHW.put( "INV_CLASS_CD", "\'" + iINV_CLASS_CD_BATCH + "\'" );
      lComHW.put( "INTERCHG_ORD", "'1'" );
      lComHW.put( "PART_BASELINE_CD", "\'" + iPART_BASELINE_CD + "\'" );
      lComHW.put( "ALT_PRIMARY_PN", "\'" + iPART_NO_OEM_2 + "\'" );
      // lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "'INVALID'" );
      lComHW.put( "APPL_EFF_LDESC", "\'" + iAPPL_EFF_LDESC + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMHW_DEF, lComHW ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkCOMMHWErrorCode( testName.getMethodName(), "CFGCHW-12006" );

   }


   /**
    * This test is to verify valid error code CFGCHW-12007:A combination part number / manufact_cd
    * listed as C_COMHW_DEF.ALT_PRIMARY_PN / ALT_PRIMARY_MANUFACT_CD must be present in C_ COMHW
    * _DEF as a PART_NO_OEM/MANUFACT_CD.
    *
    *
    */

   @Test
   public void test_CFGCHW_12007() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_comhw_def table
      Map<String, String> lComHW = new LinkedHashMap<>();
      // First record
      lComHW.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_1 + "\'" );
      lComHW.put( "PART_GROUP_NAME", "\'" + iPART_GROUP_NAME_1 + "\'" );
      lComHW.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_1 + "\'" );
      lComHW.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lComHW.put( "PART_NO_SDESC", "\'" + iPART_NO_SDESC + "\'" );
      lComHW.put( "INV_CLASS_CD", "\'" + iINV_CLASS_CD_BATCH + "\'" );
      lComHW.put( "INTERCHG_ORD", "'1'" );
      lComHW.put( "PART_BASELINE_CD", "\'" + iPART_BASELINE_CD + "\'" );
      lComHW.put( "ALT_PRIMARY_PN", "\'" + iPART_NO_OEM_1 + "\'" );
      lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lComHW.put( "APPL_EFF_LDESC", "\'" + iAPPL_EFF_LDESC + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMHW_DEF, lComHW ) );

      // Second record
      lComHW.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_1 + "\'" );
      lComHW.put( "PART_GROUP_NAME", "\'" + iPART_GROUP_NAME_1 + "\'" );
      lComHW.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_2 + "\'" );
      lComHW.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "PART_NO_SDESC", "\'" + iPART_NO_SDESC + "\'" );
      lComHW.put( "INV_CLASS_CD", "\'" + iINV_CLASS_CD_BATCH + "\'" );
      lComHW.put( "INTERCHG_ORD", "'2'" );
      lComHW.put( "PART_BASELINE_CD", "\'" + iPART_BASELINE_CD + "\'" );
      lComHW.put( "ALT_PRIMARY_PN", "\'" + iPART_NO_OEM_1 + "\'" );
      lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "APPL_EFF_LDESC", "\'" + iAPPL_EFF_LDESC + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMHW_DEF, lComHW ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkCOMMHWErrorCode( testName.getMethodName(), "CFGCHW-12007" );

   }


   /**
    * This test is to verify valid error code CFGCHW-12009:If a "-" or "," is present in
    * C_COMHW_DEF.APPL_EFF_LDESC, it must be followed by text. This test case is to verify a "-" is
    * followed by text
    *
    *
    */

   @Test
   public void test_CFGCHW_12009_1() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_comhw_def table
      Map<String, String> lComHW = new LinkedHashMap<>();

      lComHW.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_2 + "\'" );
      lComHW.put( "PART_GROUP_NAME", "\'" + iPART_GROUP_NAME_2 + "\'" );
      lComHW.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_2 + "\'" );
      lComHW.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "PART_NO_SDESC", "\'" + iPART_NO_SDESC + "\'" );
      lComHW.put( "INV_CLASS_CD", "\'" + iINV_CLASS_CD_BATCH + "\'" );
      lComHW.put( "INTERCHG_ORD", "'1'" );
      lComHW.put( "PART_BASELINE_CD", "\'" + iPART_BASELINE_CD + "\'" );
      lComHW.put( "ALT_PRIMARY_PN", "\'" + iPART_NO_OEM_2 + "\'" );
      lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "APPL_EFF_LDESC", "'001-'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMHW_DEF, lComHW ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkCOMMHWErrorCode( testName.getMethodName(), "CFGCHW-12009" );

   }


   /**
    * This test is to verify valid error code CFGCHW-12009:If a "-" or "," is present in
    * C_COMHW_DEF.APPL_EFF_LDESC, it must be followed by text. This test case is to verify a "," is
    * followed by text
    *
    *
    */

   @Test
   public void test_CFGCHW_12009_2() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_comhw_def table
      Map<String, String> lComHW = new LinkedHashMap<>();

      lComHW.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_2 + "\'" );
      lComHW.put( "PART_GROUP_NAME", "\'" + iPART_GROUP_NAME_2 + "\'" );
      lComHW.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_2 + "\'" );
      lComHW.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "PART_NO_SDESC", "\'" + iPART_NO_SDESC + "\'" );
      lComHW.put( "INV_CLASS_CD", "\'" + iINV_CLASS_CD_BATCH + "\'" );
      lComHW.put( "INTERCHG_ORD", "'1'" );
      lComHW.put( "PART_BASELINE_CD", "\'" + iPART_BASELINE_CD + "\'" );
      lComHW.put( "ALT_PRIMARY_PN", "\'" + iPART_NO_OEM_2 + "\'" );
      lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "APPL_EFF_LDESC", "'001,'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMHW_DEF, lComHW ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkCOMMHWErrorCode( testName.getMethodName(), "CFGCHW-12009" );

   }


   /**
    * This test is to verify valid error code CFGCHW-12011:C_COMHW_DEF.INTERCHG_ORD must be greater
    * than zero. This test case is to verify interchg_ord = 0.
    *
    *
    */

   @Test
   public void test_CFGCHW_12011_1() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_comhw_def table
      Map<String, String> lComHW = new LinkedHashMap<>();

      lComHW.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_2 + "\'" );
      lComHW.put( "PART_GROUP_NAME", "\'" + iPART_GROUP_NAME_2 + "\'" );
      lComHW.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_2 + "\'" );
      lComHW.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "PART_NO_SDESC", "\'" + iPART_NO_SDESC + "\'" );
      lComHW.put( "INV_CLASS_CD", "\'" + iINV_CLASS_CD_BATCH + "\'" );
      lComHW.put( "INTERCHG_ORD", "'0'" );
      lComHW.put( "PART_BASELINE_CD", "\'" + iPART_BASELINE_CD + "\'" );
      lComHW.put( "ALT_PRIMARY_PN", "\'" + iPART_NO_OEM_2 + "\'" );
      lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "APPL_EFF_LDESC", "\'" + iAPPL_EFF_LDESC + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMHW_DEF, lComHW ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkCOMMHWErrorCode( testName.getMethodName(), "CFGCHW-12011" );

   }


   /**
    * This test is to verify valid error code CFGCHW-12011:C_COMHW_DEF.INTERCHG_ORD must be greater
    * than zero. This test case is to verify interchg_ord < 0.
    *
    *
    */

   @Test
   public void test_CFGCHW_12011_2() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_comhw_def table
      Map<String, String> lComHW = new LinkedHashMap<>();

      lComHW.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_2 + "\'" );
      lComHW.put( "PART_GROUP_NAME", "\'" + iPART_GROUP_NAME_2 + "\'" );
      lComHW.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_2 + "\'" );
      lComHW.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "PART_NO_SDESC", "\'" + iPART_NO_SDESC + "\'" );
      lComHW.put( "INV_CLASS_CD", "\'" + iINV_CLASS_CD_BATCH + "\'" );
      lComHW.put( "INTERCHG_ORD", "'-3'" );
      lComHW.put( "PART_BASELINE_CD", "\'" + iPART_BASELINE_CD + "\'" );
      lComHW.put( "ALT_PRIMARY_PN", "\'" + iPART_NO_OEM_2 + "\'" );
      lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "APPL_EFF_LDESC", "\'" + iAPPL_EFF_LDESC + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMHW_DEF, lComHW ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkCOMMHWErrorCode( testName.getMethodName(), "CFGCHW-12011" );

   }


   /**
    * This test is to verify valid error code
    * CFGCHW-12012:C_COMHW_DEF.PART_GROUP_CD/ALT_PRIMARY_PN/ALT_PRIMARY_MANUFACT_CD must be present
    * in C_COMHW_DEF with its own PART_GROUP_CD/PART_NO_OEM/MANUFACT_CD same as the
    * PART_GROUP_CD/ALT_PRIMARY_PN/ALT_PRIMARY_MANUFACT_CD
    *
    *
    */

   @Test
   public void test_CFGCHW_12012() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_comhw_def table
      Map<String, String> lComHW = new LinkedHashMap<>();

      // First record
      lComHW.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_2 + "\'" );
      lComHW.put( "PART_GROUP_NAME", "\'" + iPART_GROUP_NAME_1 + "\'" );
      lComHW.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_1 + "\'" );
      lComHW.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lComHW.put( "PART_NO_SDESC", "\'" + iPART_NO_SDESC + "\'" );
      lComHW.put( "INV_CLASS_CD", "\'" + iINV_CLASS_CD_BATCH + "\'" );
      lComHW.put( "INTERCHG_ORD", "'1'" );
      lComHW.put( "PART_BASELINE_CD", "\'" + iPART_BASELINE_CD + "\'" );
      lComHW.put( "ALT_PRIMARY_PN", "\'" + iPART_NO_OEM_1 + "\'" );
      lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lComHW.put( "APPL_EFF_LDESC", "\'" + iAPPL_EFF_LDESC + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMHW_DEF, lComHW ) );

      // Second record
      lComHW.clear();
      lComHW.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_1 + "\'" );
      lComHW.put( "PART_GROUP_NAME", "\'" + iPART_GROUP_NAME_1 + "\'" );
      lComHW.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_2 + "\'" );
      lComHW.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "PART_NO_SDESC", "\'" + iPART_NO_SDESC + "\'" );
      lComHW.put( "INV_CLASS_CD", "\'" + iINV_CLASS_CD_BATCH + "\'" );
      lComHW.put( "INTERCHG_ORD", "'2'" );
      lComHW.put( "PART_BASELINE_CD", "\'" + iPART_BASELINE_CD + "\'" );
      lComHW.put( "ALT_PRIMARY_PN", "\'" + iPART_NO_OEM_1 + "\'" );
      lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lComHW.put( "APPL_EFF_LDESC", "\'" + iAPPL_EFF_LDESC + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMHW_DEF, lComHW ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkCOMMHWErrorCode( testName.getMethodName(), "CFGCHW-12012" );

   }


   /**
    * This test is to verify valid error code CFGCHW-12013:NV_CLASS_CD for the specified PART_NO_OEM
    * in Maintenix must match other inv_class_cd of same PART_NO_OE/MANUFACT_CD combination that
    * exist in C_COMHW_DEF.
    *
    *
    */

   @Test
   public void test_CFGCHW_12013() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_comhw_def table
      Map<String, String> lComHW = new LinkedHashMap<>();
      // First record
      lComHW.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_1 + "\'" );
      lComHW.put( "PART_GROUP_NAME", "\'" + iPART_GROUP_NAME_1 + "\'" );
      lComHW.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_1 + "\'" );
      lComHW.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lComHW.put( "PART_NO_SDESC", "\'" + iPART_NO_SDESC + "\'" );
      lComHW.put( "INV_CLASS_CD", "\'" + iINV_CLASS_CD_KIT + "\'" );
      lComHW.put( "INTERCHG_ORD", "'1'" );
      lComHW.put( "PART_BASELINE_CD", "\'" + iPART_BASELINE_CD + "\'" );
      lComHW.put( "ALT_PRIMARY_PN", "\'" + iPART_NO_OEM_1 + "\'" );
      lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lComHW.put( "APPL_EFF_LDESC", "\'" + iAPPL_EFF_LDESC + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMHW_DEF, lComHW ) );

      // Second record
      lComHW.clear();
      lComHW.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_1 + "\'" );
      lComHW.put( "PART_GROUP_NAME", "\'" + iPART_GROUP_NAME_1 + "\'" );
      lComHW.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_2 + "\'" );
      lComHW.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "PART_NO_SDESC", "\'" + iPART_NO_SDESC + "\'" );
      lComHW.put( "INV_CLASS_CD", "\'" + iINV_CLASS_CD_KIT + "\'" );
      lComHW.put( "INTERCHG_ORD", "'2'" );
      lComHW.put( "PART_BASELINE_CD", "\'" + iPART_BASELINE_CD + "\'" );
      lComHW.put( "ALT_PRIMARY_PN", "\'" + iPART_NO_OEM_1 + "\'" );
      lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lComHW.put( "APPL_EFF_LDESC", "\'" + iAPPL_EFF_LDESC + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMHW_DEF, lComHW ) );

      // third record
      lComHW.clear();
      lComHW.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_2 + "\'" );
      lComHW.put( "PART_GROUP_NAME", "\'" + iPART_GROUP_NAME_2 + "\'" );
      lComHW.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_2 + "\'" );
      lComHW.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "PART_NO_SDESC", "\'" + iPART_NO_SDESC + "\'" );
      lComHW.put( "INV_CLASS_CD", "\'" + iINV_CLASS_CD_SER + "\'" );
      lComHW.put( "INTERCHG_ORD", "'1'" );
      lComHW.put( "PART_BASELINE_CD", "\'" + iPART_BASELINE_CD + "\'" );
      lComHW.put( "ALT_PRIMARY_PN", "\'" + iPART_NO_OEM_2 + "\'" );
      lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "APPL_EFF_LDESC", "\'" + iAPPL_EFF_LDESC + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMHW_DEF, lComHW ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkCOMMHWErrorCode( testName.getMethodName(), "CFGCHW-12013" );

   }


   /**
    * This test is to verify valid error code CFGCHW-12014:NV_CLASS_CD for the specified PART_NO_OEM
    * in Maintenix must match other inv_class_cd of same PART_NO_OE/MANUFACT_CD combination that
    * exist in mMintenix.
    *
    *
    */

   @Test
   public void test_CFGCHW_12014() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_comhw_def table
      Map<String, String> lComHW = new LinkedHashMap<>();
      // First record
      lComHW.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_1 + "\'" );
      lComHW.put( "PART_GROUP_NAME", "\'" + iPART_GROUP_NAME_1 + "\'" );
      lComHW.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_1 + "\'" );
      lComHW.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lComHW.put( "PART_NO_SDESC", "\'" + iPART_NO_SDESC + "\'" );
      lComHW.put( "INV_CLASS_CD", "\'" + iINV_CLASS_CD_KIT + "\'" );
      lComHW.put( "INTERCHG_ORD", "'1'" );
      lComHW.put( "PART_BASELINE_CD", "\'" + iPART_BASELINE_CD + "\'" );
      lComHW.put( "ALT_PRIMARY_PN", "\'" + iPART_NO_OEM_1 + "\'" );
      lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lComHW.put( "APPL_EFF_LDESC", "\'" + iAPPL_EFF_LDESC + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMHW_DEF, lComHW ) );

      // Second record
      lComHW.clear();
      lComHW.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_1 + "\'" );
      lComHW.put( "PART_GROUP_NAME", "\'" + iPART_GROUP_NAME_1 + "\'" );
      lComHW.put( "PART_NO_OEM", "'CHW000001'" );
      lComHW.put( "MANUFACT_CD", "'11111'" );
      lComHW.put( "PART_NO_SDESC", "\'" + iPART_NO_SDESC + "\'" );
      lComHW.put( "INV_CLASS_CD", "\'" + iINV_CLASS_CD_KIT + "\'" );
      lComHW.put( "INTERCHG_ORD", "'2'" );
      lComHW.put( "PART_BASELINE_CD", "\'" + iPART_BASELINE_CD + "\'" );
      lComHW.put( "ALT_PRIMARY_PN", "\'" + iPART_NO_OEM_1 + "\'" );
      lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lComHW.put( "APPL_EFF_LDESC", "\'" + iAPPL_EFF_LDESC + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMHW_DEF, lComHW ) );

      // // third record
      // lComHW.clear();
      // lComHW.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_2 + "\'" );
      // lComHW.put( "PART_GROUP_NAME", "\'" + iPART_GROUP_NAME_2 + "\'" );
      // lComHW.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_2 + "\'" );
      // lComHW.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      // lComHW.put( "PART_NO_SDESC", "\'" + iPART_NO_SDESC + "\'" );
      // lComHW.put( "INV_CLASS_CD", "\'" + iINV_CLASS_CD_SER + "\'" );
      // lComHW.put( "INTERCHG_ORD", "'1'" );
      // lComHW.put( "PART_BASELINE_CD", "\'" + iPART_BASELINE_CD + "\'" );
      // lComHW.put( "ALT_PRIMARY_PN", "\'" + iPART_NO_OEM_2 + "\'" );
      // lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      // lComHW.put( "APPL_EFF_LDESC", "\'" + iAPPL_EFF_LDESC + "\'" );
      //
      // // insert map
      // runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMHW_DEF, lComHW ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkCOMMHWErrorCode( testName.getMethodName(), "CFGCHW-12014" );

   }


   /**
    * This test is to verify valid error code CFGCHW-12015:C_COMHW_DEF.INTERCHG_ORD must be NULL or
    * one if it is a batch part. when INTERCHG_ORD=null is valid value.
    *
    *
    */

   @Test
   public void test_CFGCHW_12015_1() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_comhw_def table
      Map<String, String> lComHW = new LinkedHashMap<>();
      // First record
      lComHW.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_1 + "\'" );
      lComHW.put( "PART_GROUP_NAME", "\'" + iPART_GROUP_NAME_1 + "\'" );
      lComHW.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_1 + "\'" );
      lComHW.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lComHW.put( "PART_NO_SDESC", "\'" + iPART_NO_SDESC + "\'" );
      lComHW.put( "INV_CLASS_CD", "'BATCH'" );
      lComHW.put( "INTERCHG_ORD", null );
      lComHW.put( "PART_BASELINE_CD", "\'" + iPART_BASELINE_CD + "\'" );
      lComHW.put( "ALT_PRIMARY_PN", "\'" + iPART_NO_OEM_1 + "\'" );
      lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lComHW.put( "APPL_EFF_LDESC", "\'" + iAPPL_EFF_LDESC + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMHW_DEF, lComHW ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify valid error code CFGCHW-12015:C_COMHW_DEF.INTERCHG_ORD must be NULL or
    * one if it is a batch part. when INTERCHG_ORD=2, error code 12015 is raised.
    *
    *
    */

   @Test
   public void test_CFGCHW_12015_2() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_comhw_def table
      Map<String, String> lComHW = new LinkedHashMap<>();
      // First record
      lComHW.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_1 + "\'" );
      lComHW.put( "PART_GROUP_NAME", "\'" + iPART_GROUP_NAME_1 + "\'" );
      lComHW.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_1 + "\'" );
      lComHW.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lComHW.put( "PART_NO_SDESC", "\'" + iPART_NO_SDESC + "\'" );
      lComHW.put( "INV_CLASS_CD", "'BATCH'" );
      lComHW.put( "INTERCHG_ORD", "'2'" );
      lComHW.put( "PART_BASELINE_CD", "\'" + iPART_BASELINE_CD + "\'" );
      lComHW.put( "ALT_PRIMARY_PN", "\'" + iPART_NO_OEM_1 + "\'" );
      lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lComHW.put( "APPL_EFF_LDESC", "\'" + iAPPL_EFF_LDESC + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMHW_DEF, lComHW ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkCOMMHWErrorCode( testName.getMethodName(), "CFGCHW-12015" );

   }


   /**
    * This test is to verify valid error code CFGCHW-20003:C_COMHW_DEF.PART_GROUP_CD already exists
    * in EQP_BOM_PART as BOM_PART_CD for the COMHW Assembly.
    *
    *
    */

   @Test
   public void test_CFGCHW_20003() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_comhw_def table
      Map<String, String> lComHW = new LinkedHashMap<>();

      lComHW.put( "PART_GROUP_CD", "'CHW-PG1'" );
      lComHW.put( "PART_GROUP_NAME", "'CHW Part Group 1'" );
      lComHW.put( "PART_NO_OEM", "'CHW000001'" );
      lComHW.put( "MANUFACT_CD", "'11111'" );
      lComHW.put( "PART_NO_SDESC", "\'" + iPART_NO_SDESC + "\'" );
      lComHW.put( "INV_CLASS_CD", "\'" + iINV_CLASS_CD_BATCH + "\'" );
      lComHW.put( "INTERCHG_ORD", "'1'" );
      lComHW.put( "PART_BASELINE_CD", "\'" + iPART_BASELINE_CD + "\'" );
      lComHW.put( "ALT_PRIMARY_PN", "'CHW000001'" );
      lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "'11111'" );
      lComHW.put( "APPL_EFF_LDESC", "\'" + iAPPL_EFF_LDESC + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMHW_DEF, lComHW ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkCOMMHWErrorCode( testName.getMethodName(), "CFGCHW-20003" );

   }

}
