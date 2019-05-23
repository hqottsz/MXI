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
import com.mxi.mx.util.BaselineLoaderTest;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.ValidationAndImport;
import com.mxi.mx.util.WhereClause;


/**
 * This test suite contains test cases on validation and import functionality of
 * BL_TAIL_SPEC_SCHEDULING_IMPORT package.
 *
 * @author ALICIA QIAN
 */
public class ACScheduling extends BaselineLoaderTest {

   @Rule
   public TestName testName = new TestName();

   ValidationAndImport ivalidationandimport;

   public simpleIDs iTaskDefnIds1 = null;
   public simpleIDs iTaskDefnIds2 = null;
   public simpleIDs iTaskDefnIds3 = null;
   public simpleIDs iTaskIds1 = null;
   public simpleIDs iTaskIds2 = null;
   public simpleIDs iTaskIds3 = null;

   public String iTaskCD1 = "AUTOTEST1";
   public String iTaskCD2 = "AUTOTEST2";
   public String iTaskCD3 = "AUTOTEST3";
   public String iAssmblCD = "ACFT_CD1";
   public String iATACD = "ACFT_CD1";
   public String iACREGCD = "ABC";
   public String iSchedType = "HOURS";

   // public simpleIDs iDataTypeIds = getDataType( iSchedType );
   // public simpleIDs iINVIds = getINVIDs( iACREGCD );

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
    * This test is to verify BL_TAIL_SPEC_SCHEDULING_IMPORT validation functionality of staging
    * table bl_tail_spec_scheduling, on TASK_CLASS_CD=REF and TASK_DEF_STATUS_CD=BUILD
    *
    */
   @Test
   public void testREF_BUILD_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lTailSched = new LinkedHashMap<>();

      lTailSched.put( "TASK_CD", "\'" + iTaskCD1 + "\'" );
      lTailSched.put( "ASSMBL_CD", "\'" + iAssmblCD + "\'" );
      lTailSched.put( "ATA_CD", "\'" + iATACD + "\'" );
      lTailSched.put( "AC_REG_CD", "\'" + iACREGCD + "\'" );
      lTailSched.put( "SCHED_DATA_TYPE_CD", "\'" + iSchedType + "\'" );
      lTailSched.put( "INTERVAL_QT", "\'10\'" );
      lTailSched.put( "NOTIFY_QT", "\'1\'" );
      lTailSched.put( "DEVIATION_QT", "\'2\'" );
      lTailSched.put( "PREFIXED_QT", "\'3\'" );
      lTailSched.put( "POSTFIXED_QT", "\'4\'" );
      lTailSched.put( "INITIAL_QT", "\'5\'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_TAIL_SPEC_SCHEDULING, lTailSched ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify BL_TAIL_SPEC_SCHEDULING_IMPORT import functionality of staging table
    * bl_tail_spec_scheduling, on TASK_CLASS_CD=REF and TASK_DEF_STATUS_CD=BUILD
    *
    */
   @Test
   public void testREF_BUILD_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testREF_BUILD_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify TASK_AC_RULE table
      simpleIDs iDataTypeIds = getDataType( iSchedType );
      simpleIDs iINVIds = getINVIDs( iACREGCD );
      VerifyTASKACRULE( iTaskIds1, iDataTypeIds, iINVIds, "10", "1", "2", "3", "4", "5" );

   }


   /**
    * This test is to verify BL_TAIL_SPEC_SCHEDULING_IMPORT validation functionality of staging
    * table bl_tail_spec_scheduling, on TASK_CLASS_CD=BLOCK and TASK_DEF_STATUS_CD=REVISION
    *
    */
   @Test
   public void testBLOCK_REVISION_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lTailSched = new LinkedHashMap<>();

      lTailSched.put( "TASK_CD", "\'" + iTaskCD2 + "\'" );
      lTailSched.put( "ASSMBL_CD", "\'" + iAssmblCD + "\'" );
      lTailSched.put( "ATA_CD", "\'" + iATACD + "\'" );
      lTailSched.put( "AC_REG_CD", "\'" + iACREGCD + "\'" );
      lTailSched.put( "SCHED_DATA_TYPE_CD", "\'" + iSchedType + "\'" );
      lTailSched.put( "INTERVAL_QT", "\'10\'" );
      lTailSched.put( "NOTIFY_QT", "\'1\'" );
      lTailSched.put( "DEVIATION_QT", "\'2\'" );
      lTailSched.put( "PREFIXED_QT", "\'3\'" );
      lTailSched.put( "POSTFIXED_QT", "\'4\'" );
      lTailSched.put( "INITIAL_QT", "\'5\'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_TAIL_SPEC_SCHEDULING, lTailSched ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify BL_TAIL_SPEC_SCHEDULING_IMPORT import functionality of staging table
    * bl_tail_spec_scheduling, on TASK_CLASS_CD=BLOCK and TASK_DEF_STATUS_CD=REVISION
    *
    */
   @Test
   public void testBLOCK_REVISION_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testBLOCK_REVISION_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify TASK_AC_RULE table
      simpleIDs iDataTypeIds = getDataType( iSchedType );
      simpleIDs iINVIds = getINVIDs( iACREGCD );
      VerifyTASKACRULE( iTaskIds2, iDataTypeIds, iINVIds, "10", "1", "2", "3", "4", "5" );

   }


   /**
    * This test is to verify BL_TAIL_SPEC_SCHEDULING_IMPORT validation functionality of staging
    * table bl_tail_spec_scheduling, on TASK_CLASS_CD=REQ and TASK_DEF_STATUS_CD=BUILD
    *
    */
   @Test
   public void testREQ_BUILD_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lTailSched = new LinkedHashMap<>();

      lTailSched.put( "TASK_CD", "\'" + iTaskCD3 + "\'" );
      lTailSched.put( "ASSMBL_CD", "\'" + iAssmblCD + "\'" );
      lTailSched.put( "ATA_CD", "\'" + iATACD + "\'" );
      lTailSched.put( "AC_REG_CD", "\'" + iACREGCD + "\'" );
      lTailSched.put( "SCHED_DATA_TYPE_CD", "\'" + iSchedType + "\'" );
      lTailSched.put( "INTERVAL_QT", "\'10\'" );
      lTailSched.put( "NOTIFY_QT", "\'1\'" );
      lTailSched.put( "DEVIATION_QT", "\'2\'" );
      lTailSched.put( "PREFIXED_QT", "\'3\'" );
      lTailSched.put( "POSTFIXED_QT", "\'4\'" );
      lTailSched.put( "INITIAL_QT", "\'5\'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_TAIL_SPEC_SCHEDULING, lTailSched ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify BL_TAIL_SPEC_SCHEDULING_IMPORT import functionality of staging table
    * bl_tail_spec_scheduling, on TASK_CLASS_CD=REQ and TASK_DEF_STATUS_CD=BUILD
    *
    */
   @Test
   public void testREQ_BUILD_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testREQ_BUILD_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify TASK_AC_RULE table
      simpleIDs iDataTypeIds = getDataType( iSchedType );
      simpleIDs iINVIds = getINVIDs( iACREGCD );
      VerifyTASKACRULE( iTaskIds3, iDataTypeIds, iINVIds, "10", "1", "2", "3", "4", "5" );

   }


   /**
    * This test is to verify BL_TAIL_SPEC_SCHEDULING_IMPORT validation functionality of staging
    * table bl_tail_spec_scheduling. This test case is to verify multiple records on various valid
    * TASK_CLASS_CD and TASK_DEF_STATUS_CD.
    *
    */
   @Test
   public void testMultiple_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lTailSched = new LinkedHashMap<>();

      lTailSched.put( "TASK_CD", "\'" + iTaskCD1 + "\'" );
      lTailSched.put( "ASSMBL_CD", "\'" + iAssmblCD + "\'" );
      lTailSched.put( "ATA_CD", "\'" + iATACD + "\'" );
      lTailSched.put( "AC_REG_CD", "\'" + iACREGCD + "\'" );
      lTailSched.put( "SCHED_DATA_TYPE_CD", "\'" + iSchedType + "\'" );
      lTailSched.put( "INTERVAL_QT", "\'10\'" );
      lTailSched.put( "NOTIFY_QT", "\'1\'" );
      lTailSched.put( "DEVIATION_QT", "\'2\'" );
      lTailSched.put( "PREFIXED_QT", "\'3\'" );
      lTailSched.put( "POSTFIXED_QT", "\'4\'" );
      lTailSched.put( "INITIAL_QT", "\'5\'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_TAIL_SPEC_SCHEDULING, lTailSched ) );

      // Load second record
      lTailSched.clear();
      lTailSched.put( "TASK_CD", "\'" + iTaskCD2 + "\'" );
      lTailSched.put( "ASSMBL_CD", "\'" + iAssmblCD + "\'" );
      lTailSched.put( "ATA_CD", "\'" + iATACD + "\'" );
      lTailSched.put( "AC_REG_CD", "\'" + iACREGCD + "\'" );
      lTailSched.put( "SCHED_DATA_TYPE_CD", "\'" + iSchedType + "\'" );
      lTailSched.put( "INTERVAL_QT", "\'10\'" );
      lTailSched.put( "NOTIFY_QT", "\'1\'" );
      lTailSched.put( "DEVIATION_QT", "\'2\'" );
      lTailSched.put( "PREFIXED_QT", "\'3\'" );
      lTailSched.put( "POSTFIXED_QT", "\'4\'" );
      lTailSched.put( "INITIAL_QT", "\'5\'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_TAIL_SPEC_SCHEDULING, lTailSched ) );

      // Load third record
      lTailSched.clear();
      lTailSched.put( "TASK_CD", "\'" + iTaskCD3 + "\'" );
      lTailSched.put( "ASSMBL_CD", "\'" + iAssmblCD + "\'" );
      lTailSched.put( "ATA_CD", "\'" + iATACD + "\'" );
      lTailSched.put( "AC_REG_CD", "\'" + iACREGCD + "\'" );
      lTailSched.put( "SCHED_DATA_TYPE_CD", "\'" + iSchedType + "\'" );
      lTailSched.put( "INTERVAL_QT", "\'10\'" );
      lTailSched.put( "NOTIFY_QT", "\'1\'" );
      lTailSched.put( "DEVIATION_QT", "\'2\'" );
      lTailSched.put( "PREFIXED_QT", "\'3\'" );
      lTailSched.put( "POSTFIXED_QT", "\'4\'" );
      lTailSched.put( "INITIAL_QT", "\'5\'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_TAIL_SPEC_SCHEDULING, lTailSched ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify BL_TAIL_SPEC_SCHEDULING_IMPORT import functionality of staging table
    * bl_tail_spec_scheduling. This test case is to verify multiple records on various valid
    * TASK_CLASS_CD and TASK_DEF_STATUS_CD.
    */
   @Test
   public void testMultiple_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testMultiple_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify TASK_AC_RULE table
      simpleIDs iDataTypeIds = getDataType( iSchedType );
      simpleIDs iINVIds = getINVIDs( iACREGCD );
      VerifyTASKACRULE( iTaskIds1, iDataTypeIds, iINVIds, "10", "1", "2", "3", "4", "5" );
      VerifyTASKACRULE( iTaskIds2, iDataTypeIds, iINVIds, "10", "1", "2", "3", "4", "5" );
      VerifyTASKACRULE( iTaskIds3, iDataTypeIds, iINVIds, "10", "1", "2", "3", "4", "5" );

   }


   // ========================================================================================================
   /**
    * This function is to retrieve INV IDs from INV_AC_REG table .
    *
    *
    */

   public simpleIDs getINVIDs( String aACREGCD ) {

      String[] iIds = { "INV_NO_DB_ID", "INV_NO_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "AC_REG_CD", aACREGCD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.INV_AC_REG, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;

   }


   /**
    * This function is to retrieve data type IDs from MIM_DATA_TYPE table .
    *
    *
    */

   public simpleIDs getDataType( String aSchedType ) {

      String[] iIds = { "DATA_TYPE_DB_ID", "DATA_TYPE_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "DATA_TYPE_CD", aSchedType );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.MIM_DATA_TYPE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;

   }


   /**
    * This function is to verify TASK_AC_RULE by given parameters .
    *
    *
    */

   public void VerifyTASKACRULE( simpleIDs aTaskIds, simpleIDs aDataTypeIds, simpleIDs aINVIds,
         String aIntervalQT, String aNotifyQT, String DeviaQT, String aPreFix, String aPostFix,
         String aInitialQT ) {

      String[] iIds =
            { "DATA_TYPE_DB_ID", "DATA_TYPE_ID", "INV_NO_DB_ID", "INV_NO_ID", "INTERVAL_QT",
                  "NOTIFY_QT", "DEVIATION_QT", "PREFIXED_QT", "POSTFIXED_QT", "INITIAL_QT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_AC_RULE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "DATA_TYPE_DB_ID",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aDataTypeIds.getNO_DB_ID() ) );
      Assert.assertTrue( "DATA_TYPE_ID",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aDataTypeIds.getNO_ID() ) );
      Assert.assertTrue( "INV_NO_DB_ID",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aINVIds.getNO_DB_ID() ) );
      Assert.assertTrue( "INV_NO_ID",
            llists.get( 0 ).get( 3 ).equalsIgnoreCase( aINVIds.getNO_ID() ) );
      Assert.assertTrue( "INTERVAL_QT", llists.get( 0 ).get( 4 ).equalsIgnoreCase( aIntervalQT ) );
      Assert.assertTrue( "NOTIFY_QT", llists.get( 0 ).get( 5 ).equalsIgnoreCase( aNotifyQT ) );
      Assert.assertTrue( "DEVIATION_QT", llists.get( 0 ).get( 6 ).equalsIgnoreCase( DeviaQT ) );
      Assert.assertTrue( "PREFIXED_QT", llists.get( 0 ).get( 7 ).equalsIgnoreCase( aPreFix ) );
      Assert.assertTrue( "POSTFIXED_QT", llists.get( 0 ).get( 8 ).equalsIgnoreCase( aPostFix ) );
      Assert.assertTrue( "INITIAL_QT", llists.get( 0 ).get( 9 ).equalsIgnoreCase( aInitialQT ) );

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
      iQueryString =
            "insert into TASK_TASK (TASK_DB_ID, TASK_ID, TASK_DEFN_DB_ID, TASK_DEFN_ID, TASK_CLASS_DB_ID, TASK_CLASS_CD, ASSMBL_DB_ID, "
                  + "ASSMBL_CD, ASSMBL_BOM_ID, TASK_DEF_STATUS_DB_ID, TASK_DEF_STATUS_CD,TASK_CD, TASK_NAME)"
                  + " values (" + CONS_DB_ID + ", TASK_ID_SEQ.nextval, "
                  + iTaskDefnIds1.getNO_DB_ID() + ", " + iTaskDefnIds1.getNO_ID() + ", 10, 'REF', "
                  + CONS_DB_ID + ", 'ACFT_CD1', 0, 0, 'BUILD','AUTOTEST1', 'AUTOTEST1')";
      executeSQL( iQueryString );

      iQueryString =
            "insert into TASK_TASK (TASK_DB_ID, TASK_ID, TASK_DEFN_DB_ID, TASK_DEFN_ID, TASK_CLASS_DB_ID, TASK_CLASS_CD, ASSMBL_DB_ID, "
                  + "ASSMBL_CD, ASSMBL_BOM_ID, TASK_DEF_STATUS_DB_ID, TASK_DEF_STATUS_CD,TASK_CD, TASK_NAME)"
                  + " values (" + CONS_DB_ID + ", TASK_ID_SEQ.nextval, "
                  + iTaskDefnIds2.getNO_DB_ID() + ", " + iTaskDefnIds2.getNO_ID()
                  + ", 10, 'BLOCK', " + CONS_DB_ID
                  + ", 'ACFT_CD1', 0, 0, 'REVISION','AUTOTEST2', 'AUTOTEST2')";
      executeSQL( iQueryString );

      iQueryString =
            "insert into TASK_TASK (TASK_DB_ID, TASK_ID, TASK_DEFN_DB_ID, TASK_DEFN_ID, TASK_CLASS_DB_ID, TASK_CLASS_CD, ASSMBL_DB_ID, "
                  + "ASSMBL_CD, ASSMBL_BOM_ID, TASK_DEF_STATUS_DB_ID, TASK_DEF_STATUS_CD,TASK_CD, TASK_NAME)"
                  + " values (" + CONS_DB_ID + ", TASK_ID_SEQ.nextval, "
                  + iTaskDefnIds3.getNO_DB_ID() + ", " + iTaskDefnIds3.getNO_ID() + ", 0, 'REQ', "
                  + CONS_DB_ID + ", 'ACFT_CD1', 0, 0, 'BUILD','AUTOTEST3', 'AUTOTEST3')";
      executeSQL( iQueryString );

      // Get newly created task ids
      llists.clear();
      String[] iIds2 = { "TASK_DB_ID", "TASK_ID" };
      List<String> lfieldsTask = new ArrayList<String>( Arrays.asList( iIds2 ) );

      // Get task id1
      WhereClause lArgs1 = new WhereClause();
      lArgs1.addArguments( "TASK_CD", "AUTOTEST1" );

      iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_TASK, lfieldsTask, lArgs1 );
      llists.clear();
      llists = execute( iQueryString, lfieldsTask );
      iTaskIds1 = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      // Get task id2
      WhereClause lArgs2 = new WhereClause();
      lArgs2.addArguments( "TASK_CD", "AUTOTEST2" );

      iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_TASK, lfieldsTask, lArgs2 );
      llists.clear();
      llists = execute( iQueryString, lfieldsTask );
      iTaskIds2 = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      // Get task id3
      WhereClause lArgs3 = new WhereClause();
      lArgs3.addArguments( "TASK_CD", "AUTOTEST3" );

      iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_TASK, lfieldsTask, lArgs3 );
      llists.clear();
      llists = execute( iQueryString, lfieldsTask );
      iTaskIds3 = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      // create sched rule for each task
      iQueryString =
            "insert into TASK_SCHED_RULE (TASK_DB_ID, TASK_ID, DATA_TYPE_DB_ID, DATA_TYPE_ID, "
                  + "DEF_INTERVAL_QT, DEF_NOTIFY_QT, DEF_DEVIATION_QT, DEF_PREFIXED_QT, DEF_POSTFIXED_QT, DEF_INITIAL_QT, RSTAT_CD)"
                  + " values (" + iTaskIds1.getNO_DB_ID() + ", " + iTaskIds1.getNO_ID()
                  + ", 0, 1, 1, 1, 1, 1, 1, 1, 0 )";
      executeSQL( iQueryString );

      iQueryString =
            "insert into TASK_SCHED_RULE (TASK_DB_ID, TASK_ID, DATA_TYPE_DB_ID, DATA_TYPE_ID, "
                  + "DEF_INTERVAL_QT, DEF_NOTIFY_QT, DEF_DEVIATION_QT, DEF_PREFIXED_QT, DEF_POSTFIXED_QT, DEF_INITIAL_QT, RSTAT_CD)"
                  + " values (" + iTaskIds2.getNO_DB_ID() + ", " + iTaskIds2.getNO_ID()
                  + ", 0, 1, 1, 1, 1, 1, 1, 1, 0 )";
      executeSQL( iQueryString );

      iQueryString =
            "insert into TASK_SCHED_RULE (TASK_DB_ID, TASK_ID, DATA_TYPE_DB_ID, DATA_TYPE_ID, "
                  + "DEF_INTERVAL_QT, DEF_NOTIFY_QT, DEF_DEVIATION_QT, DEF_PREFIXED_QT, DEF_POSTFIXED_QT, DEF_INITIAL_QT, RSTAT_CD)"
                  + " values (" + iTaskIds3.getNO_DB_ID() + ", " + iTaskIds3.getNO_ID()
                  + ", 0, 1, 1, 1, 1, 1, 1, 1, 0 )";
      executeSQL( iQueryString );

   }


   /**
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */

   public void RestoreData() {

      String lStrDelete = null;

      // Delete TASk_AC_RULE
      lStrDelete = "delete from TASK_AC_RULE";
      executeSQL( lStrDelete );

      // Delete task_sched_rule table
      if ( iTaskIds1 != null ) {
         lStrDelete = "delete from TASk_SCHED_RULE where TASK_DB_ID=" + iTaskIds1.getNO_DB_ID()
               + " and TASK_ID=" + iTaskIds1.getNO_ID();
         executeSQL( lStrDelete );

      }

      if ( iTaskIds2 != null ) {
         lStrDelete = "delete from TASk_SCHED_RULE where TASK_DB_ID=" + iTaskIds2.getNO_DB_ID()
               + " and TASK_ID=" + iTaskIds2.getNO_ID();
         executeSQL( lStrDelete );

      }

      if ( iTaskIds3 != null ) {
         lStrDelete = "delete from TASk_SCHED_RULE where TASK_DB_ID=" + iTaskIds3.getNO_DB_ID()
               + " and TASK_ID=" + iTaskIds3.getNO_ID();
         executeSQL( lStrDelete );

      }

      // Delete task_task table
      lStrDelete = "delete from TASK_TASK where TASK_CD like '%AUTOTEST%'";
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
    *
    */
   public int runValidationAndImport( boolean blnOnlyValidation, boolean allornone ) {
      int rtValue = 0;
      ivalidationandimport = new ValidationAndImport() {

         @Override
         public int runValidation( boolean allornone ) {
            int lReturn = 0;
            CallableStatement lPrepareCallKIT;

            try {
               if ( allornone ) {
                  lPrepareCallKIT = getConnection().prepareCall(
                        "BEGIN  bl_tail_spec_scheduling_import.validate(aiv_exist_in_mx => 'STRICT', aib_allornone => true, aon_retcode =>?, aov_retmsg =>?); END;" );
               } else {
                  lPrepareCallKIT = getConnection().prepareCall(
                        "BEGIN  bl_tail_spec_scheduling_import.validate(aiv_exist_in_mx => 'STRICT', aib_allornone => false, aon_retcode =>?, aov_retmsg =>?); END;" );

               }

               lPrepareCallKIT.registerOutParameter( 1, Types.INTEGER );
               lPrepareCallKIT.registerOutParameter( 2, Types.VARCHAR );
               lPrepareCallKIT.execute();
               commit();
               lReturn = lPrepareCallKIT.getInt( 1 );
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
               if ( allornone ) {
                  lPrepareCallKIT = getConnection().prepareCall(
                        "BEGIN  bl_tail_spec_scheduling_import.import(aiv_exist_in_mx => 'STRICT', aib_allornone => true, aon_retcode =>?, aov_retmsg =>?); END;" );
               } else {
                  lPrepareCallKIT = getConnection().prepareCall(
                        "BEGIN  bl_tail_spec_scheduling_import.import(aiv_exist_in_mx => 'STRICT', aib_allornone => false, aon_retcode =>?, aov_retmsg =>?); END;" );

               }

               lPrepareCallKIT.registerOutParameter( 1, Types.INTEGER );
               lPrepareCallKIT.registerOutParameter( 2, Types.VARCHAR );
               lPrepareCallKIT.execute();
               commit();
               lReturn = lPrepareCallKIT.getInt( 1 );
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
