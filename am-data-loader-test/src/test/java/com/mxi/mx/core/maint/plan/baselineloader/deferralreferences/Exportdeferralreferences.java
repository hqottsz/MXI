package com.mxi.mx.core.maint.plan.baselineloader.deferralreferences;

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

import com.mxi.mx.core.maint.plan.datamodels.faildeferRef;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.WhereClause;


/**
 * This test suite contains test case on export functionality on Deferral References.
 *
 * @author ALICIA QIAN
 */
public class Exportdeferralreferences extends DeferralReferencesTests {

   @Rule
   public TestName testName = new TestName();

   @SuppressWarnings( "serial" )
   ArrayList<faildeferRef> iFailList = new ArrayList<faildeferRef>() {

      {
         add( new faildeferRef( "ACFT_CD1", "ATDFRACFT", "SYS-10-1", "MEL", "MEL A",
               "AT DEFER ACFT1", "ACTV", "100", "10", null, "AUTO LDSEC ACFT 1", "AT PERF",
               "AT ACTION", "1" ) );

         add( new faildeferRef( "ACFT_CD1", "ATDFRACFTCONFLICT", "SYS-10", "MEL", "MEL A",
               "AT DEFER ACFT2", "ACTV", "100", "10", null, "AUTO LDSEC ACFT 2", "AT PERF",
               "AT ACTION", "1" ) );

         add( new faildeferRef( "ACFT_CD1", "ATDFRACFTRELATED", "SYS-10", "MEL", "MEL A",
               "AT DEFER ACFT3", "ACTV", "100", "10", null, "AUTO LDSEC ACFT 3", "AT PERF",
               "AT ACTION", "0" ) );

         add( new faildeferRef( "ENG_CD1", "ATDFRENG", "ENG-SYS-2", "MEL", "MEL A", "AT DEFER ENG1",
               "ACTV", "100", "10", null, "AUTO LDSEC ENG 1", "AT PERF", "AT ACTION", "0" ) );

         add( new faildeferRef( "ENG_CD1", "ATDFRENGCONFLICT", "ENG-SYS-2", "MEL", "MEL A",
               "AT DEFER ENG2", "ACTV", "100", "10", null, "AUTO LDSEC ENG 2", "AT PERF",
               "AT ACTION", "1" ) );

         add( new faildeferRef( "ENG_CD1", "ATDFRENGRELATED", "ENG-SYS-2", "MEL", "MEL A",
               "AT DEFER ENG3", "ACTV", "100", "10", null, "AUTO LDSEC ENG 3", "AT PERF",
               "AT ACTION", "1" ) );

         add( new faildeferRef( "APU_CD1", "ATDFRAPU", "APU-SYS-1-1", "MEL", "MEL A",
               "AT DEFER APU1", "ACTV", "100", "10", null, "AUTO LDSEC APU 1", "AT PERF",
               "AT ACTION", "0" ) );

         add( new faildeferRef( "APU_CD1", "ATDFRAPUCONFLICT", "APU-SYS-1-1", "MEL", "MEL A",
               "AT DEFER APU2", "ACTV", "100", "10", null, "AUTO LDSEC APU 2", "AT PERF",
               "AT ACTION", "1" ) );

         add( new faildeferRef( "APU_CD1", "ATDFRAPURELATED", "APU-SYS-1-1", "MEL", "MEL A",
               "AT DEFER APU3", "ACTV", "100", "10", null, "AUTO LDSEC APU 3", "AT PERF",
               "AT ACTION", "1" ) );

      }
   };


   /**
    * Clean up after each individual test
    *
    */
   @After
   @Override
   public void after() {
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
    * This test is to verify export functionality is correct when there is no data in stg tables and
    * auto deletion is true.
    *
    *
    */

   @Test
   public void testNO_DATA_AUTODEL() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // run export
      Assert.assertTrue( runExportport( true ) == 1 );

      // Verify BL_FAIL_DEFER_REF table
      ArrayList<faildeferRef> lactualList = getfaildeferRefs();
      checkArraysEqual( iFailList, lactualList );

      // verify BL_FAIL_DEFER_CONFLICT
      String lQuery = "select 1 from " + TableUtil.BL_FAIL_DEFER_CONFLICT + " where ASSMBL_CD='"
            + iACFT_ASSMBLCD + "' and DEFER_REF_NAME='" + iDEFER_REF_NAME_ACFT_EXISTED
            + "' and CONFLICT_DEFER_REF_NAME='" + iDEFER_REF_NAME_ACFT_CONFLICT_MANIX + "'";
      Assert.assertTrue( "Check BL_FAIL_DEFER_CONFLICT table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify BL_FAIL_DEFER_REL
      lQuery = "select 1 from " + TableUtil.BL_FAIL_DEFER_REL + " where ASSMBL_CD='" + iENG_ASSMBLCD
            + "' and DEFER_REF_NAME='" + iDEFER_REF_NAME_ENG_EXISTED + "' and REL_DEFER_REF_NAME='"
            + iDEFER_REF_NAME_ENG_RELATED_MANIX + "'";
      Assert.assertTrue( "Check BL_FAIL_DEFER_REL table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify BL_FAIL_DEFER_INSP
      lQuery = "select 1 from " + TableUtil.BL_FAIL_DEFER_INSP + " where ASSMBL_CD='"
            + iACFT_ASSMBLCD + "' and DEFER_REF_NAME='" + iDEFER_REF_NAME_ACFT_EXISTED
            + "' and INSP_REQ_TASK_CD='" + iACFT_INSP_REQ_TASK_CD_EXIST + "'";
      Assert.assertTrue( "Check BL_FAIL_DEFER_INSP table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify BL_FAIL_DEFER_DEAD
      lQuery =
            "select 1 from " + TableUtil.BL_FAIL_DEFER_DEAD + " where ASSMBL_CD='" + iACFT_ASSMBLCD
                  + "' and DEFER_REF_NAME='" + iDEFER_REF_NAME_ACFT_EXISTED + "' and DATA_TYPE_CD='"
                  + iACFT_DATA_TYPE_CD_HOURS + "' and DEAD_QT='" + iACFT_DEAD_QT + "'";
      Assert.assertTrue( "Check BL_FAIL_DEFER_DEAD table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify BL_FAIL_DEFER_CAP_LEVEL
      lQuery = "select 1 from " + TableUtil.BL_FAIL_DEFER_CAP_LEVEL + " where ASSMBL_CD='"
            + iACFT_ASSMBLCD + "' and DEFER_REF_NAME='" + iDEFER_REF_NAME_ACFT_EXISTED
            + "' and ACFT_CAP_CD='" + iACFT_CAP_CD + "' and ACFT_CAP_LEVEL_CD='"
            + iACFT_CAP_LEVEL_CD + "'";
      Assert.assertTrue( "Check BL_FAIL_DEFER_DEAD table to verify the record exists",
            RecordsExist( lQuery ) );

   }


   /**
    * This test is to verify export functionality is correct when there is no data in stg tables and
    * auto deletion is true.
    *
    *
    */

   @Test
   public void testWITH_DATA_AUTODEL() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lDeferralRefMap = new LinkedHashMap<>();

      // bl_fail_defer_ref of first record
      lDeferralRefMap.put( "ASSMBL_CD", "'AUTOTEST'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'AUTOTEST'" );
      lDeferralRefMap.put( "CONFIG_SLOT_CD", "'AUTOTEST'" );
      lDeferralRefMap.put( "OPERATOR_CD_LIST", "'AUTOTEST'" );
      lDeferralRefMap.put( "FAIL_SEV_CD", "'AUTOTEST'" );
      lDeferralRefMap.put( "FAIL_DEFER_CD", "'AUTOTEST'" );
      lDeferralRefMap.put( "DEFER_REF_LDESC", "'AUTOTEST'" );
      lDeferralRefMap.put( "DEFER_REF_STATUS_CD", "'AUTOTEST'" );
      lDeferralRefMap.put( "INST_SYSTEMS_QT", "'10'" );
      lDeferralRefMap.put( "DISPATCH_SYSTEMS_QT", "'10'" );
      lDeferralRefMap.put( "APPL_LDESC", "'AUTOTEST'" );
      lDeferralRefMap.put( "OPER_RESTRICTIONS_LDESC", "'AUTOTEST'" );
      lDeferralRefMap.put( "PERF_PENALTIES_LDESC", "'AUTOTEST'" );
      lDeferralRefMap.put( "MAINT_ACTIONS_LDESC", "'AUTOTEST'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_REF, lDeferralRefMap ) );

      // run export
      Assert.assertTrue( runExportport( true ) == 1 );

      // Verify BL_FAIL_DEFER_REF table
      String lQuery =
            "select 1 from " + TableUtil.BL_FAIL_DEFER_CONFLICT + " where ASSMBL_CD='AUTOTEST'";
      Assert.assertFalse( "Check BL_FAIL_DEFER_CONFLICT table to verify the record not exists",
            RecordsExist( lQuery ) );

      ArrayList<faildeferRef> lactualList = getfaildeferRefs();
      checkArraysEqual( iFailList, lactualList );

      // verify BL_FAIL_DEFER_CONFLICT
      lQuery = "select 1 from " + TableUtil.BL_FAIL_DEFER_CONFLICT + " where ASSMBL_CD='"
            + iACFT_ASSMBLCD + "' and DEFER_REF_NAME='" + iDEFER_REF_NAME_ACFT_EXISTED
            + "' and CONFLICT_DEFER_REF_NAME='" + iDEFER_REF_NAME_ACFT_CONFLICT_MANIX + "'";
      Assert.assertTrue( "Check BL_FAIL_DEFER_CONFLICT table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify BL_FAIL_DEFER_REL
      lQuery = "select 1 from " + TableUtil.BL_FAIL_DEFER_REL + " where ASSMBL_CD='" + iENG_ASSMBLCD
            + "' and DEFER_REF_NAME='" + iDEFER_REF_NAME_ENG_EXISTED + "' and REL_DEFER_REF_NAME='"
            + iDEFER_REF_NAME_ENG_RELATED_MANIX + "'";
      Assert.assertTrue( "Check BL_FAIL_DEFER_REL table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify BL_FAIL_DEFER_INSP
      lQuery = "select 1 from " + TableUtil.BL_FAIL_DEFER_INSP + " where ASSMBL_CD='"
            + iACFT_ASSMBLCD + "' and DEFER_REF_NAME='" + iDEFER_REF_NAME_ACFT_EXISTED
            + "' and INSP_REQ_TASK_CD='" + iACFT_INSP_REQ_TASK_CD_EXIST + "'";
      Assert.assertTrue( "Check BL_FAIL_DEFER_INSP table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify BL_FAIL_DEFER_DEAD
      lQuery =
            "select 1 from " + TableUtil.BL_FAIL_DEFER_DEAD + " where ASSMBL_CD='" + iACFT_ASSMBLCD
                  + "' and DEFER_REF_NAME='" + iDEFER_REF_NAME_ACFT_EXISTED + "' and DATA_TYPE_CD='"
                  + iACFT_DATA_TYPE_CD_HOURS + "' and DEAD_QT='" + iACFT_DEAD_QT + "'";
      Assert.assertTrue( "Check BL_FAIL_DEFER_DEAD table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify BL_FAIL_DEFER_CAP_LEVEL
      lQuery = "select 1 from " + TableUtil.BL_FAIL_DEFER_CAP_LEVEL + " where ASSMBL_CD='"
            + iACFT_ASSMBLCD + "' and DEFER_REF_NAME='" + iDEFER_REF_NAME_ACFT_EXISTED
            + "' and ACFT_CAP_CD='" + iACFT_CAP_CD + "' and ACFT_CAP_LEVEL_CD='"
            + iACFT_CAP_LEVEL_CD + "'";
      Assert.assertTrue( "Check BL_FAIL_DEFER_DEAD table to verify the record exists",
            RecordsExist( lQuery ) );

   }


   /**
    * This test is to verify export functionality is correct when there is no data in stg tables and
    * auto deletion is false.
    *
    *
    */

   @Test
   public void testNO_DATA_NOAUTODEL() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // run export
      Assert.assertTrue( runExportport( false ) == 1 );

      // Verify BL_FAIL_DEFER_REF table
      ArrayList<faildeferRef> lactualList = getfaildeferRefs();
      checkArraysEqual( iFailList, lactualList );

      // verify BL_FAIL_DEFER_CONFLICT
      String lQuery = "select 1 from " + TableUtil.BL_FAIL_DEFER_CONFLICT + " where ASSMBL_CD='"
            + iACFT_ASSMBLCD + "' and DEFER_REF_NAME='" + iDEFER_REF_NAME_ACFT_EXISTED
            + "' and CONFLICT_DEFER_REF_NAME='" + iDEFER_REF_NAME_ACFT_CONFLICT_MANIX + "'";
      Assert.assertTrue( "Check BL_FAIL_DEFER_CONFLICT table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify BL_FAIL_DEFER_REL
      lQuery = "select 1 from " + TableUtil.BL_FAIL_DEFER_REL + " where ASSMBL_CD='" + iENG_ASSMBLCD
            + "' and DEFER_REF_NAME='" + iDEFER_REF_NAME_ENG_EXISTED + "' and REL_DEFER_REF_NAME='"
            + iDEFER_REF_NAME_ENG_RELATED_MANIX + "'";
      Assert.assertTrue( "Check BL_FAIL_DEFER_REL table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify BL_FAIL_DEFER_INSP
      lQuery = "select 1 from " + TableUtil.BL_FAIL_DEFER_INSP + " where ASSMBL_CD='"
            + iACFT_ASSMBLCD + "' and DEFER_REF_NAME='" + iDEFER_REF_NAME_ACFT_EXISTED
            + "' and INSP_REQ_TASK_CD='" + iACFT_INSP_REQ_TASK_CD_EXIST + "'";
      Assert.assertTrue( "Check BL_FAIL_DEFER_INSP table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify BL_FAIL_DEFER_DEAD
      lQuery =
            "select 1 from " + TableUtil.BL_FAIL_DEFER_DEAD + " where ASSMBL_CD='" + iACFT_ASSMBLCD
                  + "' and DEFER_REF_NAME='" + iDEFER_REF_NAME_ACFT_EXISTED + "' and DATA_TYPE_CD='"
                  + iACFT_DATA_TYPE_CD_HOURS + "' and DEAD_QT='" + iACFT_DEAD_QT + "'";
      Assert.assertTrue( "Check BL_FAIL_DEFER_DEAD table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify BL_FAIL_DEFER_CAP_LEVEL
      lQuery = "select 1 from " + TableUtil.BL_FAIL_DEFER_CAP_LEVEL + " where ASSMBL_CD='"
            + iACFT_ASSMBLCD + "' and DEFER_REF_NAME='" + iDEFER_REF_NAME_ACFT_EXISTED
            + "' and ACFT_CAP_CD='" + iACFT_CAP_CD + "' and ACFT_CAP_LEVEL_CD='"
            + iACFT_CAP_LEVEL_CD + "'";
      Assert.assertTrue( "Check BL_FAIL_DEFER_DEAD table to verify the record exists",
            RecordsExist( lQuery ) );

   }


   /**
    * This test is to verify export functionality is correct when there is no data in stg tables and
    * auto deletion is false.
    *
    *
    */

   @Test
   public void testWITH_DATA_NOAUTODEL() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lDeferralRefMap = new LinkedHashMap<>();

      // bl_fail_defer_ref of first record
      lDeferralRefMap.put( "ASSMBL_CD", "'AUTOTEST'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'AUTOTEST'" );
      lDeferralRefMap.put( "CONFIG_SLOT_CD", "'AUTOTEST'" );
      lDeferralRefMap.put( "OPERATOR_CD_LIST", "'AUTOTEST'" );
      lDeferralRefMap.put( "FAIL_SEV_CD", "'AUTOTEST'" );
      lDeferralRefMap.put( "FAIL_DEFER_CD", "'AUTOTEST'" );
      lDeferralRefMap.put( "DEFER_REF_LDESC", "'AUTOTEST'" );
      lDeferralRefMap.put( "DEFER_REF_STATUS_CD", "'AUTOTEST'" );
      lDeferralRefMap.put( "INST_SYSTEMS_QT", "'10'" );
      lDeferralRefMap.put( "DISPATCH_SYSTEMS_QT", "'10'" );
      lDeferralRefMap.put( "APPL_LDESC", "'AUTOTEST'" );
      lDeferralRefMap.put( "OPER_RESTRICTIONS_LDESC", "'AUTOTEST'" );
      lDeferralRefMap.put( "PERF_PENALTIES_LDESC", "'AUTOTEST'" );
      lDeferralRefMap.put( "MAINT_ACTIONS_LDESC", "'AUTOTEST'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_REF, lDeferralRefMap ) );

      // run export
      Assert.assertTrue( runExportport( false ) == -3 );

      // Verify BL_FAIL_DEFER_REF table
      String lQuery =
            "select 1 from " + TableUtil.BL_FAIL_DEFER_REF + " where ASSMBL_CD='AUTOTEST'";
      Assert.assertTrue( "Check BL_FAIL_DEFER_REF table to verify the record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.BL_FAIL_DEFER_REF + " where ASSMBL_CD='"
            + iACFT_ASSMBLCD + "'";
      Assert.assertFalse( "Check BL_FAIL_DEFER_REF table to verify the record not exists",
            RecordsExist( lQuery ) );

      // verify BL_FAIL_DEFER_CONFLICT
      lQuery = "select 1 from " + TableUtil.BL_FAIL_DEFER_CONFLICT + " where ASSMBL_CD='"
            + iACFT_ASSMBLCD + "'";
      Assert.assertFalse( "Check BL_FAIL_DEFER_CONFLICT table to verify the record not exists",
            RecordsExist( lQuery ) );

      // verify BL_FAIL_DEFER_REL
      lQuery = "select 1 from " + TableUtil.BL_FAIL_DEFER_REL + " where ASSMBL_CD='" + iENG_ASSMBLCD
            + "'";
      Assert.assertFalse( "Check BL_FAIL_DEFER_REL table to verify the record not exists",
            RecordsExist( lQuery ) );

      // verify BL_FAIL_DEFER_INSP
      lQuery = "select 1 from " + TableUtil.BL_FAIL_DEFER_INSP + " where ASSMBL_CD='"
            + iACFT_ASSMBLCD + "'";
      Assert.assertFalse( "Check BL_FAIL_DEFER_INSP table to verify the record not exists",
            RecordsExist( lQuery ) );

      // verify BL_FAIL_DEFER_DEAD
      lQuery = "select 1 from " + TableUtil.BL_FAIL_DEFER_DEAD + " where ASSMBL_CD='"
            + iACFT_ASSMBLCD + "'";
      Assert.assertFalse( "Check BL_FAIL_DEFER_DEAD table to verify the record not exists",
            RecordsExist( lQuery ) );

      // verify BL_FAIL_DEFER_CAP_LEVEL
      lQuery = "select 1 from " + TableUtil.BL_FAIL_DEFER_CAP_LEVEL + " where ASSMBL_CD='"
            + iACFT_ASSMBLCD + "'";
      Assert.assertFalse( "Check BL_FAIL_DEFER_DEAD table to verify the record not exists",
            RecordsExist( lQuery ) );

   }


   /**
    * This function is to verify fail_defer_ref_degrad_cap table after data is imported.
    *
    *
    */

   public ArrayList<faildeferRef> getfaildeferRefs() {

      String[] iIds = { "ASSMBL_CD", "DEFER_REF_NAME", "CONFIG_SLOT_CD", "FAIL_SEV_CD",
            "FAIL_DEFER_CD", "DEFER_REF_LDESC", "DEFER_REF_STATUS_CD", "INST_SYSTEMS_QT",
            "DISPATCH_SYSTEMS_QT", "APPL_LDESC", "OPER_RESTRICTIONS_LDESC", "PERF_PENALTIES_LDESC",
            "MAINT_ACTIONS_LDESC", "MOC_APPROVAL_BOOL" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "FAIL_SEV_CD", "MEL" );

      String iQueryString =
            TableUtil.buildTableQuery( TableUtil.BL_FAIL_DEFER_REF, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      ArrayList<faildeferRef> iDeferlist = new ArrayList<faildeferRef>();

      for ( int i = 0; i < llists.size(); i++ ) {
         iDeferlist.add( new faildeferRef( llists.get( i ).get( 0 ), llists.get( i ).get( 1 ),
               llists.get( i ).get( 2 ), llists.get( i ).get( 3 ), llists.get( i ).get( 4 ),
               llists.get( i ).get( 5 ), llists.get( i ).get( 6 ), llists.get( i ).get( 7 ),
               llists.get( i ).get( 8 ), llists.get( i ).get( 9 ), llists.get( i ).get( 10 ),
               llists.get( i ).get( 11 ), llists.get( i ).get( 12 ), llists.get( i ).get( 13 ) ) );

      }

      return iDeferlist;

   }


   /**
    * This function is to run export functionality of bl_deferral_references_import
    *
    * @param: aAutoDel
    *
    * @return: return code of Int
    *
    */

   public int runExportport( boolean aAutoDel ) {
      int lReturn = 0;
      CallableStatement lPrepareCallDeferral;

      try {

         if ( aAutoDel ) {

            lPrepareCallDeferral = getConnection().prepareCall(
                  "BEGIN bl_deferral_references_import.export(aib_autodel_stg_data =>true,"
                        + " aon_retcode => ?," + " aov_retmsg => ?); END;" );
         } else {
            lPrepareCallDeferral = getConnection().prepareCall(
                  "BEGIN  bl_deferral_references_import.export(aib_autodel_stg_data =>false,"
                        + " aon_retcode => ?," + " aov_retmsg => ?); END;" );

         }
         lPrepareCallDeferral.registerOutParameter( 1, Types.INTEGER );
         lPrepareCallDeferral.registerOutParameter( 2, Types.VARCHAR );
         lPrepareCallDeferral.execute();
         commit();
         lReturn = lPrepareCallDeferral.getInt( 1 );
      } catch ( SQLException e ) {
         e.printStackTrace();
      }
      return lReturn;
   }

}
