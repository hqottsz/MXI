package com.mxi.mx.core.maint.plan.baselineloader;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import com.mxi.mx.util.BaselineLoaderTest;
import com.mxi.mx.util.StringUtils;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.ValidationAndImport;
import com.mxi.mx.util.WhereClause;


/**
 * This test suite contains test cases on validation and import functionality on JIC PART.
 *
 * @author ALICIA QIAN
 */
public class JicPart extends BaselineLoaderTest {

   @Rule
   public TestName testName = new TestName();
   ValidationAndImport validationandimport;

   @SuppressWarnings( "serial" )
   ArrayList<String> lupdateTables = new ArrayList<String>() {

      {
         add( "delete from TASK_TASK where TASK_CD like 'testJICPart%'" );
      }
   };

   // This IDs are for non-multiple TCs
   simpleIDs lTaskIds;
   simpleIDs lTaskDefnIds;

   // The following IDs are for multiple TC
   simpleIDs lTaskIds1;
   simpleIDs lTaskDefnIds1;
   simpleIDs lTaskIds2;
   simpleIDs lTaskDefnIds2;
   simpleIDs lTaskIds3;
   simpleIDs lTaskDefnIds3;


   /**
    * Clean up after each individual test
    *
    */
   @After
   @Override
   public void after() {
      // ===Add some code=====
      String lstrTCName = testName.getMethodName();
      if ( lstrTCName.contains( "IMPORT" ) ) {
         cleanupTaskPartList( lTaskIds );
         clearBaselineMTables( lupdateTables );
         cleanupTaskDefn( lTaskDefnIds );
      } else if ( lstrTCName.contains( "MutipleImprt" ) ) {

         cleanupTaskPartList( lTaskIds1 );
         cleanupTaskPartList( lTaskIds2 );
         cleanupTaskPartList( lTaskIds3 );
         clearBaselineMTables( lupdateTables );
         cleanupTaskDefn( lTaskDefnIds1 );
         cleanupTaskDefn( lTaskDefnIds2 );
         cleanupTaskDefn( lTaskDefnIds3 );

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
      // ===Add some code=====
      clearBaselineLoaderTables();
      clearBaselineMTables( lupdateTables );

   }


   /**
    * This is positive test case for JIC_IMPORT validation functionality by loading data into
    * staging table and then check whether error code(s) has(have) been generated. JIC is assigned
    * to TRK on ENG.
    *
    */
   @Test
   public void testJICTRKOnEngVALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_JIC table
      Map<String, String> lc_JICMap = new LinkedHashMap<>();

      lc_JICMap.put( "ASSMBL_CD", "\'ENG_CD1\'" );
      lc_JICMap.put( "JIC_ATA_CD", "\'ENG-SYS-1-1-TRK-SOFTWARE\'" );
      lc_JICMap.put( "JIC_TASK_CD", "\'testJICPartTRKOnEng\'" );
      lc_JICMap.put( "JIC_TASK_CLASS_CD", "\'JIC\'" );
      lc_JICMap.put( "JIC_TASK_NAME", "\'testJICPartTRKOnEng\'" );
      lc_JICMap.put( "JIC_TASK_DESC", "\'testJICPartTRKOnEng\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC, lc_JICMap ) );

      // C_JIC_PART table
      Map<String, String> lc_JICPARTMap = new LinkedHashMap<>();

      lc_JICPARTMap.put( "ASSMBL_CD", "\'ENG_CD1\'" );
      lc_JICPARTMap.put( "JIC_ATA_CD", "\'ENG-SYS-1-1-TRK-SOFTWARE\'" );
      lc_JICPARTMap.put( "JIC_TASK_CD", "\'testJICPartTRKOnEng\'" );
      lc_JICPARTMap.put( "PART_NO_OEM", "\'E0000008\'" );
      lc_JICPARTMap.put( "MANUFACT_REF", "\'1234567890\'" );
      lc_JICPARTMap.put( "REQ_QT", "\'1\'" );
      lc_JICPARTMap.put( "POSITION", "\'1\'" );
      lc_JICPARTMap.put( "REMOVE_BOOL", "\'Y\'" );
      lc_JICPARTMap.put( "REMOVE_REASON_CD", "\'VENDRET\'" );
      lc_JICPARTMap.put( "INSTALL_BOOL", "\'N\'" );
      lc_JICPARTMap.put( "REQ_ACTION_CD", "\'NOREQ\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_PART, lc_JICPARTMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This is positive test case for JIC_IMPORT import functionality by importing data into system
    * and then check whether error code(s) has(have) been generated, and validate the associated
    * tables has been updated. JIC is assigned to TRK on ENG.
    *
    *
    */
   @Test
   public void testJICTRKOnEngIMPORT() {

      lTaskIds = null;
      lTaskDefnIds = null;

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testJICTRKOnEngVALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Get task Ids
      lTaskIds = getTaskIds( "testJICPartTRKOnEng" );

      // Get task defn Ids
      lTaskDefnIds = getTaskDefnIds( lTaskIds, "testJICPartTRKOnEng" );

      // Verify task_part_list table
      simpleIDs lReqActionIds = getReqActionIds( "NOREQ" );
      simpleIDs lBomIds = getBomPartIds( "E0000008", "ENG_CD1" );
      taskpartlistTableValidation( lTaskIds, "ENG_CD1", "VENDRET", "1", "1", "0",
            lReqActionIds.getNO_DB_ID(), lReqActionIds.getNO_ID(), lBomIds.getNO_DB_ID(),
            lBomIds.getNO_ID() );

   }


   /**
    * This is positive test case for JIC_IMPORT validation functionality by loading data into
    * staging table and then check whether error code(s) has(have) been generated. JIC is assigned
    * to ASSY of ENG.
    *
    */
   @Test
   public void testJICASSYVALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_JIC table
      Map<String, String> lc_JICMap = new LinkedHashMap<>();

      lc_JICMap.put( "ASSMBL_CD", "\'ENG_CD1\'" );
      lc_JICMap.put( "JIC_ATA_CD", "\'ENG_CD1\'" );
      lc_JICMap.put( "JIC_TASK_CD", "\'testJICPartASSY\'" );
      lc_JICMap.put( "JIC_TASK_CLASS_CD", "\'JIC\'" );
      lc_JICMap.put( "JIC_TASK_NAME", "\'testJICPartASSY\'" );
      lc_JICMap.put( "JIC_TASK_DESC", "\'testJICPartASSY\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC, lc_JICMap ) );

      // C_JIC_PART table
      Map<String, String> lc_JICPARTMap = new LinkedHashMap<>();

      lc_JICPARTMap.put( "ASSMBL_CD", "\'ENG_CD1\'" );
      lc_JICPARTMap.put( "JIC_ATA_CD", "\'ENG_CD1\'" );
      lc_JICPARTMap.put( "JIC_TASK_CD", "\'testJICPartASSY\'" );
      lc_JICPARTMap.put( "PART_NO_OEM", "\'ENG_ASSY_PN1\'" );
      lc_JICPARTMap.put( "MANUFACT_REF", "\'ABC11\'" );
      lc_JICPARTMap.put( "REQ_QT", "\'1\'" );
      lc_JICPARTMap.put( "POSITION", "\'1\'" );
      lc_JICPARTMap.put( "REMOVE_BOOL", "\'Y\'" );
      lc_JICPARTMap.put( "REMOVE_REASON_CD", "\'STAGGER\'" );
      lc_JICPARTMap.put( "INSTALL_BOOL", "\'N\'" );
      lc_JICPARTMap.put( "REQ_ACTION_CD", "\'NOREQ\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_PART, lc_JICPARTMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This is positive test case for JIC_IMPORT import functionality by importing data into system
    * and then check whether error code(s) has(have) been generated, and validate the associated
    * tables has been updated. JIC is assigned to ASSY of ENG.
    *
    */
   @Test
   public void testJICASSYIMPORT() {

      lTaskIds = null;
      lTaskDefnIds = null;

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testJICASSYVALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Get task Ids
      lTaskIds = getTaskIds( "testJICPartASSY" );

      // Get task defn Ids
      lTaskDefnIds = getTaskDefnIds( lTaskIds, "testJICPartASSY" );

      // Verify task_part_list table
      simpleIDs lReqActionIds = getReqActionIds( "NOREQ" );
      simpleIDs lBomIds = getBomPartIds( "ENG_ASSY_PN1", "ENG_CD1" );
      taskpartlistTableValidation( lTaskIds, "ENG_CD1", "STAGGER", "1", "1", "0",
            lReqActionIds.getNO_DB_ID(), lReqActionIds.getNO_ID(), lBomIds.getNO_DB_ID(),
            lBomIds.getNO_ID() );

   }


   /**
    * This is positive test case for JIC_IMPORT validation functionality by loading data into
    * staging table and then check whether error code(s) has(have) been generated. JIC is assigned
    * to ASSY of APU. And there is no existing task associated with the assembly.
    *
    */
   @Test
   public void testJICASSYNoExistingTaskVALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_JIC table
      Map<String, String> lc_JICMap = new LinkedHashMap<>();

      lc_JICMap.put( "ASSMBL_CD", "\'ACFT_CD1\'" );
      lc_JICMap.put( "JIC_ATA_CD", "\'APU-ASSY\'" );
      lc_JICMap.put( "JIC_TASK_CD", "\'testJICPartASSYNoTask\'" );
      lc_JICMap.put( "JIC_TASK_CLASS_CD", "\'JIC\'" );
      lc_JICMap.put( "JIC_TASK_NAME", "\'testJICPartASSYNoTask\'" );
      lc_JICMap.put( "JIC_TASK_DESC", "\'testJICPartASSYNoTask\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC, lc_JICMap ) );

      // C_JIC_PART table
      Map<String, String> lc_JICPARTMap = new LinkedHashMap<>();

      lc_JICPARTMap.put( "ASSMBL_CD", "\'ACFT_CD1\'" );
      lc_JICPARTMap.put( "JIC_ATA_CD", "\'APU-ASSY\'" );
      lc_JICPARTMap.put( "JIC_TASK_CD", "\'testJICPartASSYNoTask\'" );
      lc_JICPARTMap.put( "PART_NO_OEM", "\'APU_ASSY_PN1\'" );
      lc_JICPARTMap.put( "MANUFACT_REF", "\'1234567890\'" );
      lc_JICPARTMap.put( "REQ_QT", "\'1\'" );
      lc_JICPARTMap.put( "POSITION", "\'1\'" );
      lc_JICPARTMap.put( "REMOVE_BOOL", "\'Y\'" );
      lc_JICPARTMap.put( "REMOVE_REASON_CD", "\'STAGGER\'" );
      lc_JICPARTMap.put( "INSTALL_BOOL", "\'N\'" );
      lc_JICPARTMap.put( "REQ_ACTION_CD", "\'NOREQ\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_PART, lc_JICPARTMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This is positive test case for JIC_IMPORT import functionality by importing data into system
    * and then check whether error code(s) has(have) been generated, and validate the associated
    * tables has been updated. JIC is assigned to ASSY of APU. And there is no existing task
    * associated with the assembly.
    *
    */
   @Test
   public void testJICASSYNoExistingTaskIMPORT() {

      lTaskIds = null;
      lTaskDefnIds = null;

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testJICASSYNoExistingTaskVALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Get task Ids
      lTaskIds = getTaskIds( "testJICPartASSYNoTask" );

      // Get task defn Ids
      lTaskDefnIds = getTaskDefnIds( lTaskIds, "testJICPartASSYNoTask" );

      // Verify task_part_list table
      simpleIDs lReqActionIds = getReqActionIds( "NOREQ" );
      simpleIDs lBomIds = getBomPartIds( "APU_ASSY_PN1", "ACFT_CD1" );
      taskpartlistTableValidation( lTaskIds, "ACFT_CD1", "STAGGER", "1", "1", "0",
            lReqActionIds.getNO_DB_ID(), lReqActionIds.getNO_ID(), lBomIds.getNO_DB_ID(),
            lBomIds.getNO_ID() );

   }


   /**
    * This is positive test case for JIC_IMPORT validation functionality by loading data into
    * staging table and then check whether error code(s) has(have) been generated. JIC is assigned
    * to BATCH of APU.
    *
    */
   @Test
   public void testJICBATCHVALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_JIC table
      Map<String, String> lc_JICMap = new LinkedHashMap<>();

      lc_JICMap.put( "ASSMBL_CD", "\'APU_CD1\'" );
      lc_JICMap.put( "JIC_ATA_CD", "\'APU-SYS-1-1\'" );
      lc_JICMap.put( "JIC_TASK_CD", "\'testJICPartBATCH\'" );
      lc_JICMap.put( "JIC_TASK_CLASS_CD", "\'JIC\'" );
      lc_JICMap.put( "JIC_TASK_NAME", "\'testJICPartBATCH\'" );
      lc_JICMap.put( "JIC_TASK_DESC", "\'testJICPartBATCH\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC, lc_JICMap ) );

      // C_JIC_PART table
      Map<String, String> lc_JICPARTMap = new LinkedHashMap<>();

      lc_JICPARTMap.put( "ASSMBL_CD", "\'APU_CD1\'" );
      lc_JICPARTMap.put( "JIC_ATA_CD", "\'APU-SYS-1-1\'" );
      lc_JICPARTMap.put( "JIC_TASK_CD", "\'testJICPartBATCH\'" );
      lc_JICPARTMap.put( "PART_NO_OEM", "\'AP0000009\'" );
      lc_JICPARTMap.put( "MANUFACT_REF", "\'10001\'" );
      lc_JICPARTMap.put( "REQ_QT", "\'1\'" );
      lc_JICPARTMap.put( "POSITION", null );
      lc_JICPARTMap.put( "REMOVE_BOOL", "\'Y\'" );
      lc_JICPARTMap.put( "REMOVE_REASON_CD", "\'STAGGER\'" );
      lc_JICPARTMap.put( "INSTALL_BOOL", "\'N\'" );
      lc_JICPARTMap.put( "REQ_ACTION_CD", "\'NOREQ\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_PART, lc_JICPARTMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This is positive test case for JIC_IMPORT import functionality by importing data into system
    * and then check whether error code(s) has(have) been generated, and validate the associated
    * tables has been updated. JIC is assigned to BATCH of APU.
    *
    */
   @Test
   public void testJICBATCHIMPORT() {

      lTaskIds = null;
      lTaskDefnIds = null;

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testJICBATCHVALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Get task Ids
      lTaskIds = getTaskIds( "testJICPartBATCH" );

      // Get task defn Ids
      lTaskDefnIds = getTaskDefnIds( lTaskIds, "testJICPartBATCH" );

      // Verify task_part_list table
      simpleIDs lReqActionIds = getReqActionIds( "NOREQ" );
      simpleIDs lBomIds = getBomPartIds( "AP0000009", "APU_CD1" );
      taskpartlistTableValidation( lTaskIds, null, "STAGGER", "1", "1", "0",
            lReqActionIds.getNO_DB_ID(), lReqActionIds.getNO_ID(), lBomIds.getNO_DB_ID(),
            lBomIds.getNO_ID() );

   }


   /**
    * This is positive test case for JIC_IMPORT validation functionality by loading data into
    * staging table and then check whether error code(s) has(have) been generated. JIC is assigned
    * to BATCH of APU.
    *
    */
   @Test
   public void testJICBATCHNoExistingTaskVALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_JIC table
      Map<String, String> lc_JICMap = new LinkedHashMap<>();

      lc_JICMap.put( "ASSMBL_CD", "\'ENG_CD1\'" );
      lc_JICMap.put( "JIC_ATA_CD", "\'ENG-SYS-1-1-TRK-BATCH-PARENT\'" );
      lc_JICMap.put( "JIC_TASK_CD", "\'testJICPartNoTaskBATCH\'" );
      lc_JICMap.put( "JIC_TASK_CLASS_CD", "\'JIC\'" );
      lc_JICMap.put( "JIC_TASK_NAME", "\'testJICPartNoTaskBATCH\'" );
      lc_JICMap.put( "JIC_TASK_DESC", "\'testJICPartNoTaskBATCH\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC, lc_JICMap ) );

      // C_JIC_PART table
      Map<String, String> lc_JICPARTMap = new LinkedHashMap<>();

      lc_JICPARTMap.put( "ASSMBL_CD", "\'ENG_CD1\'" );
      lc_JICPARTMap.put( "JIC_ATA_CD", "\'ENG-SYS-1-1-TRK-BATCH-PARENT\'" );
      lc_JICPARTMap.put( "JIC_TASK_CD", "\'testJICPartNoTaskBATCH\'" );
      lc_JICPARTMap.put( "PART_NO_OEM", "\'E0000011\'" );
      lc_JICPARTMap.put( "MANUFACT_REF", "\'ABC11\'" );
      lc_JICPARTMap.put( "REQ_QT", "\'1\'" );
      lc_JICPARTMap.put( "POSITION", null );
      lc_JICPARTMap.put( "REMOVE_BOOL", "\'Y\'" );
      lc_JICPARTMap.put( "REMOVE_REASON_CD", "\'STAGGER\'" );
      lc_JICPARTMap.put( "INSTALL_BOOL", "\'N\'" );
      lc_JICPARTMap.put( "REQ_ACTION_CD", "\'NOREQ\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_PART, lc_JICPARTMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This is positive test case for JIC_IMPORT import functionality by importing data into system
    * and then check whether error code(s) has(have) been generated, and validate the associated
    * tables has been updated.JIC is assigned to BATCH of APU.
    *
    */
   @Test
   public void testJICBATCHNoExistingTaskIMPORT() {

      lTaskIds = null;
      lTaskDefnIds = null;

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testJICBATCHNoExistingTaskVALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Get task Ids
      lTaskIds = getTaskIds( "testJICPartNoTaskBATCH" );

      // Get task defn Ids
      lTaskDefnIds = getTaskDefnIds( lTaskIds, "testJICPartNoTaskBATCH" );

      // Verify task_part_list table
      simpleIDs lReqActionIds = getReqActionIds( "NOREQ" );
      simpleIDs lBomIds = getBomPartIds( "E0000011", "ENG_CD1" );
      taskpartlistTableValidation( lTaskIds, null, "STAGGER", "1", "1", "0",
            lReqActionIds.getNO_DB_ID(), lReqActionIds.getNO_ID(), lBomIds.getNO_DB_ID(),
            lBomIds.getNO_ID() );

   }


   /**
    * This is positive test case for JIC_IMPORT validation functionality by loading data into
    * staging table and then check whether error code(s) has(have) been generated. JIC is assigned
    * to SER of ACFT.
    *
    */
   @Test
   public void testJICSERVALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_JIC table
      Map<String, String> lc_JICMap = new LinkedHashMap<>();

      lc_JICMap.put( "ASSMBL_CD", "\'ACFT_CD1\'" );
      lc_JICMap.put( "JIC_ATA_CD", "\'SYS-1-1\'" );
      lc_JICMap.put( "JIC_TASK_CD", "\'testJICPartSER\'" );
      lc_JICMap.put( "JIC_TASK_CLASS_CD", "\'JIC\'" );
      lc_JICMap.put( "JIC_TASK_NAME", "\'testJICPartSER\'" );
      lc_JICMap.put( "JIC_TASK_DESC", "\'testJICPartSER\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC, lc_JICMap ) );

      // C_JIC_PART table
      Map<String, String> lc_JICPARTMap = new LinkedHashMap<>();

      lc_JICPARTMap.put( "ASSMBL_CD", "\'ACFT_CD1\'" );
      lc_JICPARTMap.put( "JIC_ATA_CD", "\'SYS-1-1\'" );
      lc_JICPARTMap.put( "JIC_TASK_CD", "\'testJICPartSER\'" );
      lc_JICPARTMap.put( "PART_NO_OEM", "\'A0000012\'" );
      lc_JICPARTMap.put( "MANUFACT_REF", "\'1234567890\'" );
      lc_JICPARTMap.put( "REQ_QT", "\'1\'" );
      lc_JICPARTMap.put( "POSITION", null );
      lc_JICPARTMap.put( "REMOVE_BOOL", "\'Y\'" );
      lc_JICPARTMap.put( "REMOVE_REASON_CD", "\'T/X-LIFE\'" );
      lc_JICPARTMap.put( "INSTALL_BOOL", "\'N\'" );
      lc_JICPARTMap.put( "REQ_ACTION_CD", "\'NOREQ\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_PART, lc_JICPARTMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This is positive test case for JIC_IMPORT import functionality by importing data into system
    * and then check whether error code(s) has(have) been generated, and validate the associated
    * tables has been updated.JIC is assigned to SER of ACFT.
    *
    */

   @Test
   public void testJICSERIMPORT() {

      lTaskIds = null;
      lTaskDefnIds = null;

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testJICSERVALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Get task Ids
      lTaskIds = getTaskIds( "testJICPartSER" );

      // Get task defn Ids
      lTaskDefnIds = getTaskDefnIds( lTaskIds, "testJICPartSER" );

      // Verify task_part_list table
      simpleIDs lReqActionIds = getReqActionIds( "NOREQ" );
      simpleIDs lBomIds = getBomPartIds( "A0000012", "ACFT_CD1" );
      taskpartlistTableValidation( lTaskIds, null, "T/X-LIFE", "1", "1", "0",
            lReqActionIds.getNO_DB_ID(), lReqActionIds.getNO_ID(), lBomIds.getNO_DB_ID(),
            lBomIds.getNO_ID() );

   }


   /**
    * This is positive test case for JIC_IMPORT validation functionality by loading data into
    * staging table and then check whether error code(s) has(have) been generated. JIC is assigned
    * to SER of ACFT, and installation set to be YES as well.
    *
    */
   @Test
   public void testJICInstallationVALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_JIC table
      Map<String, String> lc_JICMap = new LinkedHashMap<>();

      lc_JICMap.put( "ASSMBL_CD", "\'ACFT_CD1\'" );
      lc_JICMap.put( "JIC_ATA_CD", "\'SYS-1-1\'" );
      lc_JICMap.put( "JIC_TASK_CD", "\'testJICPartSER\'" );
      lc_JICMap.put( "JIC_TASK_CLASS_CD", "\'JIC\'" );
      lc_JICMap.put( "JIC_TASK_NAME", "\'testJICPartSER\'" );
      lc_JICMap.put( "JIC_TASK_DESC", "\'testJICPartSER\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC, lc_JICMap ) );

      // C_JIC_PART table
      Map<String, String> lc_JICPARTMap = new LinkedHashMap<>();

      lc_JICPARTMap.put( "ASSMBL_CD", "\'ACFT_CD1\'" );
      lc_JICPARTMap.put( "JIC_ATA_CD", "\'SYS-1-1\'" );
      lc_JICPARTMap.put( "JIC_TASK_CD", "\'testJICPartSER\'" );
      lc_JICPARTMap.put( "PART_NO_OEM", "\'A0000012\'" );
      lc_JICPARTMap.put( "MANUFACT_REF", "\'1234567890\'" );
      lc_JICPARTMap.put( "REQ_QT", "\'1\'" );
      lc_JICPARTMap.put( "POSITION", null );
      lc_JICPARTMap.put( "REMOVE_BOOL", "\'Y\'" );
      lc_JICPARTMap.put( "REMOVE_REASON_CD", "\'T/X-LIFE\'" );
      lc_JICPARTMap.put( "INSTALL_BOOL", "\'Y\'" );
      lc_JICPARTMap.put( "REQ_ACTION_CD", "\'REQ\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_PART, lc_JICPARTMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This is positive test case for JIC_IMPORT import functionality by importing data into system
    * and then check whether error code(s) has(have) been generated, and validate the associated
    * tables has been updated. JIC is assigned to SER of ACFT, and installation set to be YES as
    * well.
    *
    *
    */
   @Test
   public void testJICInstallationIMPORT() {

      lTaskIds = null;
      lTaskDefnIds = null;

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testJICInstallationVALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Get task Ids
      lTaskIds = getTaskIds( "testJICPartSER" );

      // Get task defn Ids
      lTaskDefnIds = getTaskDefnIds( lTaskIds, "testJICPartSER" );

      // Verify task_part_list table
      simpleIDs lReqActionIds = getReqActionIds( "REQ" );
      simpleIDs lBomIds = getBomPartIds( "A0000012", "ACFT_CD1" );
      taskpartlistTableValidation( lTaskIds, null, "T/X-LIFE", "1", "1", "1",
            lReqActionIds.getNO_DB_ID(), lReqActionIds.getNO_ID(), lBomIds.getNO_DB_ID(),
            lBomIds.getNO_ID() );

   }


   /**
    * This is positive test case for JIC_IMPORT validation functionality by loading data into
    * staging table without remove reason but remove_bool is Y, then check whether error code(s)
    * has(have) been generated. JIC is assigned to SER of ACFT, and installation set to be YES as
    * well.
    *
    */
   @Test
   public void testJICNoReasonVALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_JIC table
      Map<String, String> lc_JICMap = new LinkedHashMap<>();

      lc_JICMap.put( "ASSMBL_CD", "\'ACFT_CD1\'" );
      lc_JICMap.put( "JIC_ATA_CD", "\'SYS-1-1\'" );
      lc_JICMap.put( "JIC_TASK_CD", "\'testJICPartSER\'" );
      lc_JICMap.put( "JIC_TASK_CLASS_CD", "\'JIC\'" );
      lc_JICMap.put( "JIC_TASK_NAME", "\'testJICPartSER\'" );
      lc_JICMap.put( "JIC_TASK_DESC", "\'testJICPartSER\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC, lc_JICMap ) );

      // C_JIC_PART table
      Map<String, String> lc_JICPARTMap = new LinkedHashMap<>();

      lc_JICPARTMap.put( "ASSMBL_CD", "\'ACFT_CD1\'" );
      lc_JICPARTMap.put( "JIC_ATA_CD", "\'SYS-1-1\'" );
      lc_JICPARTMap.put( "JIC_TASK_CD", "\'testJICPartSER\'" );
      lc_JICPARTMap.put( "PART_NO_OEM", "\'A0000012\'" );
      lc_JICPARTMap.put( "MANUFACT_REF", "\'1234567890\'" );
      lc_JICPARTMap.put( "REQ_QT", "\'1\'" );
      lc_JICPARTMap.put( "POSITION", null );
      lc_JICPARTMap.put( "REMOVE_BOOL", "\'Y\'" );
      lc_JICPARTMap.put( "REMOVE_REASON_CD", null );
      lc_JICPARTMap.put( "INSTALL_BOOL", "\'Y\'" );
      lc_JICPARTMap.put( "REQ_ACTION_CD", "\'REQ\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_PART, lc_JICPARTMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This is positive test case for JIC_IMPORT validation functionality by loading multiple valid
    * data into staging table and then check whether error code(s) has(have) been generated.
    *
    */
   @Test
   public void testJICMutipleVALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // insert first record in C_JIC table
      Map<String, String> lc_JICMap = new LinkedHashMap<>();

      lc_JICMap.put( "ASSMBL_CD", "\'ENG_CD1\'" );
      lc_JICMap.put( "JIC_ATA_CD", "\'ENG-SYS-1-1-TRK-SOFTWARE\'" );
      lc_JICMap.put( "JIC_TASK_CD", "\'testJICPartTRKOnEng\'" );
      lc_JICMap.put( "JIC_TASK_CLASS_CD", "\'JIC\'" );
      lc_JICMap.put( "JIC_TASK_NAME", "\'testJICPartTRKOnEng\'" );
      lc_JICMap.put( "JIC_TASK_DESC", "\'testJICPartTRKOnEng\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC, lc_JICMap ) );

      // insert second record in C_JIC table
      lc_JICMap.clear();
      lc_JICMap.put( "ASSMBL_CD", "\'ENG_CD1\'" );
      lc_JICMap.put( "JIC_ATA_CD", "\'ENG_CD1\'" );
      lc_JICMap.put( "JIC_TASK_CD", "\'testJICPartASSY\'" );
      lc_JICMap.put( "JIC_TASK_CLASS_CD", "\'JIC\'" );
      lc_JICMap.put( "JIC_TASK_NAME", "\'testJICPartASSY\'" );
      lc_JICMap.put( "JIC_TASK_DESC", "\'testJICPartASSY\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC, lc_JICMap ) );

      // insert third record in C_JIC table
      lc_JICMap.clear();
      lc_JICMap.put( "ASSMBL_CD", "\'ACFT_CD1\'" );
      lc_JICMap.put( "JIC_ATA_CD", "\'APU-ASSY\'" );
      lc_JICMap.put( "JIC_TASK_CD", "\'testJICPartASSYNoTask\'" );
      lc_JICMap.put( "JIC_TASK_CLASS_CD", "\'JIC\'" );
      lc_JICMap.put( "JIC_TASK_NAME", "\'testJICPartASSYNoTask\'" );
      lc_JICMap.put( "JIC_TASK_DESC", "\'testJICPartASSYNoTask\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC, lc_JICMap ) );

      // Insert first record C_JIC_PART table
      Map<String, String> lc_JICPARTMap = new LinkedHashMap<>();

      lc_JICPARTMap.put( "ASSMBL_CD", "\'ENG_CD1\'" );
      lc_JICPARTMap.put( "JIC_ATA_CD", "\'ENG-SYS-1-1-TRK-SOFTWARE\'" );
      lc_JICPARTMap.put( "JIC_TASK_CD", "\'testJICPartTRKOnEng\'" );
      lc_JICPARTMap.put( "PART_NO_OEM", "\'E0000008\'" );
      lc_JICPARTMap.put( "MANUFACT_REF", "\'1234567890\'" );
      lc_JICPARTMap.put( "REQ_QT", "\'1\'" );
      lc_JICPARTMap.put( "POSITION", "\'1\'" );
      lc_JICPARTMap.put( "REMOVE_BOOL", "\'Y\'" );
      lc_JICPARTMap.put( "REMOVE_REASON_CD", "\'VENDRET\'" );
      lc_JICPARTMap.put( "INSTALL_BOOL", "\'N\'" );
      lc_JICPARTMap.put( "REQ_ACTION_CD", "\'NOREQ\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_PART, lc_JICPARTMap ) );

      // Insert second record C_JIC_PART table
      lc_JICPARTMap.clear();
      lc_JICPARTMap.put( "ASSMBL_CD", "\'ENG_CD1\'" );
      lc_JICPARTMap.put( "JIC_ATA_CD", "\'ENG_CD1\'" );
      lc_JICPARTMap.put( "JIC_TASK_CD", "\'testJICPartASSY\'" );
      lc_JICPARTMap.put( "PART_NO_OEM", "\'ENG_ASSY_PN1\'" );
      lc_JICPARTMap.put( "MANUFACT_REF", "\'ABC11\'" );
      lc_JICPARTMap.put( "REQ_QT", "\'1\'" );
      lc_JICPARTMap.put( "POSITION", "\'1\'" );
      lc_JICPARTMap.put( "REMOVE_BOOL", "\'Y\'" );
      lc_JICPARTMap.put( "REMOVE_REASON_CD", "\'STAGGER\'" );
      lc_JICPARTMap.put( "INSTALL_BOOL", "\'N\'" );
      lc_JICPARTMap.put( "REQ_ACTION_CD", "\'NOREQ\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_PART, lc_JICPARTMap ) );

      // Insert third record C_JIC_PART table
      lc_JICPARTMap.clear();
      lc_JICPARTMap.put( "ASSMBL_CD", "\'ACFT_CD1\'" );
      lc_JICPARTMap.put( "JIC_ATA_CD", "\'APU-ASSY\'" );
      lc_JICPARTMap.put( "JIC_TASK_CD", "\'testJICPartASSYNoTask\'" );
      lc_JICPARTMap.put( "PART_NO_OEM", "\'APU_ASSY_PN1\'" );
      lc_JICPARTMap.put( "MANUFACT_REF", "\'1234567890\'" );
      lc_JICPARTMap.put( "REQ_QT", "\'1\'" );
      lc_JICPARTMap.put( "POSITION", "\'1\'" );
      lc_JICPARTMap.put( "REMOVE_BOOL", "\'Y\'" );
      lc_JICPARTMap.put( "REMOVE_REASON_CD", "\'STAGGER\'" );
      lc_JICPARTMap.put( "INSTALL_BOOL", "\'N\'" );
      lc_JICPARTMap.put( "REQ_ACTION_CD", "\'NOREQ\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_PART, lc_JICPARTMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This is positive test case for JIC_IMPORT validation functionality by loading multiple valid
    * data into staging table and then check whether error code(s) has(have) been generated. And
    * validate the associated tables has been updated.
    *
    *
    */
   @Test
   public void testJICMutipleImprt() {

      lTaskIds1 = null;
      lTaskDefnIds1 = null;
      lTaskIds2 = null;
      lTaskDefnIds2 = null;
      lTaskIds3 = null;
      lTaskDefnIds3 = null;

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testJICMutipleVALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Get task Ids1
      lTaskIds1 = getTaskIds( "testJICPartTRKOnEng" );

      // Get task defn Ids1
      lTaskDefnIds1 = getTaskDefnIds( lTaskIds1, "testJICPartTRKOnEng" );

      // Verify task_part_list table
      simpleIDs lReqActionIds = getReqActionIds( "NOREQ" );
      simpleIDs lBomIds = getBomPartIds( "E0000008", "ENG_CD1" );
      taskpartlistTableValidation( lTaskIds1, "ENG_CD1", "VENDRET", "1", "1", "0",
            lReqActionIds.getNO_DB_ID(), lReqActionIds.getNO_ID(), lBomIds.getNO_DB_ID(),
            lBomIds.getNO_ID() );

      // Get task Ids2
      lTaskIds2 = getTaskIds( "testJICPartASSY" );

      // Get task defn Ids2
      lTaskDefnIds2 = getTaskDefnIds( lTaskIds2, "testJICPartASSY" );

      // Verify task_part_list table
      lReqActionIds = getReqActionIds( "NOREQ" );
      lBomIds = getBomPartIds( "ENG_ASSY_PN1", "ENG_CD1" );
      taskpartlistTableValidation( lTaskIds2, "ENG_CD1", "STAGGER", "1", "1", "0",
            lReqActionIds.getNO_DB_ID(), lReqActionIds.getNO_ID(), lBomIds.getNO_DB_ID(),
            lBomIds.getNO_ID() );

      // Get task Ids3
      lTaskIds3 = getTaskIds( "testJICPartASSYNoTask" );

      // Get task defn Ids3
      lTaskDefnIds3 = getTaskDefnIds( lTaskIds3, "testJICPartASSYNoTask" );

      // Verify task_part_list table
      lReqActionIds = getReqActionIds( "NOREQ" );
      lBomIds = getBomPartIds( "APU_ASSY_PN1", "ACFT_CD1" );
      taskpartlistTableValidation( lTaskIds3, "ACFT_CD1", "STAGGER", "1", "1", "0",
            lReqActionIds.getNO_DB_ID(), lReqActionIds.getNO_ID(), lBomIds.getNO_DB_ID(),
            lBomIds.getNO_ID() );

   }


   // =========================================================================================================================
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
      validationandimport = new ValidationAndImport() {

         @Override
         public int runValidation( boolean allornone ) {
            int lReturn = 0;
            CallableStatement lPrepareCallJICPART;

            try {

               lPrepareCallJICPART = getConnection()
                     .prepareCall( "BEGIN  jic_import.validate_jic(on_retcode =>?); END;" );

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

               lPrepareCallKIT = getConnection()
                     .prepareCall( "BEGIN jic_import.import_jic(on_retcode =>?); END;" );

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

      lrtValue = ablnOnlyValidation ? validationandimport.runValidation( allornone )
            : validationandimport.runImport( allornone );

      return lrtValue;
   }


   /**
    * Retrive task IDs from task_task table table
    *
    * @return simpleIDs
    */

   public simpleIDs getTaskIds( String aTaskCD ) {

      // Get task Ids
      String[] ischedIds = { "TASK_DB_ID", "TASK_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( ischedIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_CD", aTaskCD );

      String iQueryString = TableUtil.buildTableQuery( "Task_Task", lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      simpleIDs lTaskIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );
      return lTaskIds;
   }


   /**
    * Retrive task defn IDs from task_task table table
    *
    * @return simpleIDs
    */

   public simpleIDs getTaskDefnIds( simpleIDs aTaskIds, String aTaskCD ) {

      // Get task Ids
      String[] ischedIds = { "TASK_DEFN_DB_ID", "TASK_DEFN_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( ischedIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );
      lArgs.addArguments( "TASK_CD", aTaskCD );

      String iQueryString = TableUtil.buildTableQuery( "Task_Task", lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      simpleIDs lTaskDefnIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );
      return lTaskDefnIds;
   }


   /**
    * Retrive requirement action simpleIDs
    *
    * @return simpleIDs
    */

   public simpleIDs getReqActionIds( String aReqActionCd ) {

      // Get task Ids
      String[] ischedIds = { "REQ_ACTION_DB_ID", "REQ_ACTION_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( ischedIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "REQ_ACTION_CD", aReqActionCd );

      String iQueryString = TableUtil.buildTableQuery( "REF_REQ_ACTION", lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      simpleIDs lReqActionIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );
      return lReqActionIds;
   }


   /**
    * Retrive BOMPART simpleIds
    *
    * @return simpleIds
    */

   public simpleIDs getBomPartIds( String aStrBomOEM, String aAssmblCd ) {

      simpleIDs lBomPartIds = null;

      StringBuilder lassmblquery = new StringBuilder();
      lassmblquery
            .append(
                  "select eqp_bom_part.BOM_PART_DB_ID, eqp_bom_part.BOM_PART_ID from eqp_bom_part " )
            .append( "inner join eqp_part_baseline on " )
            .append( "eqp_bom_part.bom_part_db_id=eqp_part_baseline.bom_part_db_id and " )
            .append( "eqp_bom_part.bom_part_id=eqp_part_baseline.bom_part_id " )
            .append( "inner join eqp_part_no on " )
            .append( "eqp_part_baseline.part_no_db_id=eqp_part_no.part_no_db_id and " )
            .append( "eqp_part_baseline.part_no_id=eqp_part_no.part_no_id " )
            .append( "where eqp_part_no.part_no_oem='" + aStrBomOEM + "' " )
            .append( "and eqp_bom_part.Assmbl_CD='" + aAssmblCd + "'" );

      ResultSet lResultSetRecords;

      try {
         lResultSetRecords = runQuery( lassmblquery.toString() );

         lResultSetRecords.next();

         lBomPartIds = new simpleIDs( lResultSetRecords.getString( "BOM_PART_DB_ID" ),
               lResultSetRecords.getString( "BOM_PART_ID" ) );

      } catch ( SQLException e ) {
         e.printStackTrace();
      }

      return lBomPartIds;
   }


   /**
    * Validation of task_part_list table
    *
    * @return void
    */

   public void taskpartlistTableValidation( simpleIDs aTaskIds, String aAssmbCD, String aReason,
         String aReqQT, String aRmvBln, String aInstlBln, String aReqACTNDBId, String aReqACTNCd,
         String aBOMDBId, String aBOMId ) {

      // validate task_part_list table
      String[] ischedIds = { "TASK_DB_ID", "TASK_ID", "ASSMBL_CD", "REMOVE_REASON_CD", "REQ_QT",
            "REMOVE_BOOL", "INSTALL_BOOL", "REQ_ACTION_DB_ID", "REQ_ACTION_CD", "BOM_PART_DB_ID",
            "BOM_PART_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( ischedIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( "TASK_PART_LIST", lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Validation
      if ( !StringUtils.isBlank( aAssmbCD ) ) {
         Assert.assertTrue( "ASSMBL_CD", llists.get( 0 ).get( 2 ).equalsIgnoreCase( aAssmbCD ) );
      }
      Assert.assertTrue( "REMOVE_REASON_CD", llists.get( 0 ).get( 3 ).equalsIgnoreCase( aReason ) );
      Assert.assertTrue( "REQ_QT", llists.get( 0 ).get( 4 ).equalsIgnoreCase( aReqQT ) );
      Assert.assertTrue( "REMOVE_BOOL", llists.get( 0 ).get( 5 ).equalsIgnoreCase( aRmvBln ) );
      Assert.assertTrue( "INSTALL_BOOL", llists.get( 0 ).get( 6 ).equalsIgnoreCase( aInstlBln ) );
      Assert.assertTrue( "REQ_ACTION_DB_ID",
            llists.get( 0 ).get( 7 ).equalsIgnoreCase( aReqACTNDBId ) );
      Assert.assertTrue( "REQ_ACTION_CD", llists.get( 0 ).get( 8 ).equalsIgnoreCase( aReqACTNCd ) );
      Assert.assertTrue( "BOM_PART_DB_ID", llists.get( 0 ).get( 9 ).equalsIgnoreCase( aBOMDBId ) );
      Assert.assertTrue( "BOM_PART_ID", llists.get( 0 ).get( 10 ).equalsIgnoreCase( aBOMId ) );

   }


   /**
    * This function is clean up TASK_DEFN table for every TC.
    *
    */

   public void cleanupTaskDefn( simpleIDs ataskDefnIds ) {
      System.out.println( "========Starting TASK DEFN table cleanup ===========" );
      String sqlQuery = null;
      PreparedStatement lStatement;

      try {
         sqlQuery = "delete from TASK_DEFN where TASK_DEFN_DB_ID=" + ataskDefnIds.getNO_DB_ID()
               + " and TASK_DEFN_ID=" + ataskDefnIds.getNO_ID();

         lStatement = getConnection().prepareStatement( sqlQuery );
         lStatement.executeUpdate( sqlQuery );
         commit();

      } catch ( SQLException e ) {
         e.printStackTrace();
      }

   }


   /**
    * This function is clean up TASK_PART_LIST table for every TC.
    *
    */

   public void cleanupTaskPartList( simpleIDs ataskIds ) {
      System.out.println( "========Starting TASK PART LIST table cleanup ===========" );
      String sqlQuery = null;
      PreparedStatement lStatement;

      try {
         sqlQuery = "delete from TASK_PART_LIST where TASK_DB_ID=" + ataskIds.getNO_DB_ID()
               + " and TASK_ID=" + ataskIds.getNO_ID();

         lStatement = getConnection().prepareStatement( sqlQuery );
         lStatement.executeUpdate( sqlQuery );
         commit();

      } catch ( SQLException e ) {
         e.printStackTrace();
      }

   }

}
