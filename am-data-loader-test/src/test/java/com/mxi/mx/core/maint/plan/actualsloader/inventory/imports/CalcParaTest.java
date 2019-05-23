package com.mxi.mx.core.maint.plan.actualsloader.inventory.imports;

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
import com.mxi.mx.util.StoreProcedureRunner;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.TestConstants;
import com.mxi.mx.util.WhereClause;


/**
 * This test suite contains test cases for Actuals Loader to calculate parameters function
 *
 * @author ALICIA QIAN
 */
public class CalcParaTest extends AbstractImportInventory {

   @Rule
   public TestName testName = new TestName();

   public String iSERIAL_NO_OEM_ACFT = "ACFT00001";
   public String iPART_NO_OEM_ACFT = "ACFT_TEST_PN1";
   public String iMANUFACT_CD_ACFT = "10001";
   public String iINV_CLASS_CD_ACFT = "ACFT";

   public simpleIDs iINV_Ids = null;
   public String iDATA_TYPE_CD_AT_CYC = "AT_CYC";
   public String iDATA_TYPE_CD_AT_CYC_M = "AT_CYC_M";
   public String iDATA_TYPE_CD_AT_CYC_AT_CYC_M = "AT_CYC_AT_CYC_M";
   public String iDATA_TYPE_CD_AT_HR = "AT_HR";
   public String iDATA_TYPE_CD_AT_HR_M = "AT_HR_M";
   public String iDATA_TYPE_CD_AT_HR_AT_HR_M = "AT_HR_AT_HR_M";
   public String iDATA_TYPE_CD_AD_CYC = "AD_CYC";


   /**
    * Clean up after each individual test
    *
    * @throws Exception
    */
   @After
   @Override
   public void after() throws Exception {

      // clean up the event data
      clearMxTestData();
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
      clearActualsLoaderTables();
      iINV_Ids = null;
   }


   /**
    *
    * This test is to load inventory which is going to apply calculated parameters.
    */

   public void testACFT_CALC_VALIDATION() throws Exception {

      String lRandom = String.valueOf( getRandom() );

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();
      lMapInventory.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapInventory.put( "PART_NO_OEM", "'" + iPART_NO_OEM_ACFT + "'" );
      lMapInventory.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_ACFT + "'" );
      lMapInventory.put( "MANUFACT_CD", "'" + iMANUFACT_CD_ACFT + "'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "AC_REG_CD", "'" + lRandom + "'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create inventory usage map
      Map<String, String> lInventoryUsage = new LinkedHashMap<String, String>();
      // :usage AT_CYC
      lInventoryUsage.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lInventoryUsage.put( "PART_NO_OEM", "'" + iPART_NO_OEM_ACFT + "'" );
      lInventoryUsage.put( "MANUFACT_CD", "'" + iMANUFACT_CD_ACFT + "'" );
      lInventoryUsage.put( "DATA_TYPE_CD", "'" + iDATA_TYPE_CD_AT_CYC + "'" );
      lInventoryUsage.put( "TSN_QT", "1" );
      lInventoryUsage.put( "TSO_QT", "1" );
      lInventoryUsage.put( "TSI_QT", "1" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE, lInventoryUsage ) );

      // usage AT_CYC_M
      lInventoryUsage.clear();
      lInventoryUsage.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lInventoryUsage.put( "PART_NO_OEM", "'" + iPART_NO_OEM_ACFT + "'" );
      lInventoryUsage.put( "MANUFACT_CD", "'" + iMANUFACT_CD_ACFT + "'" );
      lInventoryUsage.put( "DATA_TYPE_CD", "'" + iDATA_TYPE_CD_AT_CYC_M + "'" );
      lInventoryUsage.put( "TSN_QT", "2" );
      lInventoryUsage.put( "TSO_QT", "2" );
      lInventoryUsage.put( "TSI_QT", "2" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE, lInventoryUsage ) );

      // usage AT_CYC_AT_CYC_M
      lInventoryUsage.clear();
      lInventoryUsage.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lInventoryUsage.put( "PART_NO_OEM", "'" + iPART_NO_OEM_ACFT + "'" );
      lInventoryUsage.put( "MANUFACT_CD", "'" + iMANUFACT_CD_ACFT + "'" );
      lInventoryUsage.put( "DATA_TYPE_CD", "'" + iDATA_TYPE_CD_AT_CYC_AT_CYC_M + "'" );
      lInventoryUsage.put( "TSN_QT", "3" );
      lInventoryUsage.put( "TSO_QT", "3" );
      lInventoryUsage.put( "TSI_QT", "3" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE, lInventoryUsage ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventory( testName.getMethodName(), "PASS", true );

   }


   /**
    *
    * This test is to verify inventory calculated parameters.
    */
   @Test
   public void testACFT_CALC_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testACFT_CALC_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), false, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // get inv IDs
      iINV_Ids = getInvIds( iSERIAL_NO_OEM_ACFT );

      // verify inv_curr_usage
      simpleIDs iAT_CYC_IDs = getDataType( iDATA_TYPE_CD_AT_CYC );
      simpleIDs iAT_CYC_M_IDs = getDataType( iDATA_TYPE_CD_AT_CYC_M );
      simpleIDs iAT_CYC_AT_CYC_M_IDs = getDataType( iDATA_TYPE_CD_AT_CYC_AT_CYC_M );
      simpleIDs iAD_CYC = getDataType( iDATA_TYPE_CD_AD_CYC );

      verifyINVCURRUSAGE( iINV_Ids, iAT_CYC_IDs, "1", "1", "1" );
      verifyINVCURRUSAGE( iINV_Ids, iAT_CYC_M_IDs, "2", "2", "2" );
      verifyINVCURRUSAGE( iINV_Ids, iAT_CYC_AT_CYC_M_IDs, "3", "3", "3" );
      verifyINVCURRUSAGE( iINV_Ids, iAD_CYC, "2", "2", "2" );

   }


   // ================================================================================================================
   /**
    * This function is to verify INV_INV table
    *
    *
    */
   public void verifyINVCURRUSAGE( simpleIDs aINVIDs, simpleIDs aDATA_TYPE_IDs, String aTSN_QT,
         String aTSO_QT, String aTSI_QT ) {

      // EVT_INV table
      String[] iIds = { "TSN_QT", "TSO_QT", "TSI_QT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "INV_NO_DB_ID", aINVIDs.getNO_DB_ID() );
      lArgs.addArguments( "INV_NO_ID", aINVIDs.getNO_ID() );
      lArgs.addArguments( "DATA_TYPE_DB_ID", aDATA_TYPE_IDs.getNO_DB_ID() );
      lArgs.addArguments( "DATA_TYPE_ID", aDATA_TYPE_IDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.INV_CURR_USAGE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "TSN_QT", llists.get( 0 ).get( 0 ).equalsIgnoreCase( aTSN_QT ) );
      Assert.assertTrue( "TSO_QT", llists.get( 0 ).get( 1 ).equalsIgnoreCase( aTSO_QT ) );
      Assert.assertTrue( "TSI_QT", llists.get( 0 ).get( 2 ).equalsIgnoreCase( aTSI_QT ) );

   }


   /**
    * This function is to retrieve data_type ids.
    *
    *
    */
   public simpleIDs getDataType( String aDATA_TYPE_CD ) {
      String[] iIds = { "DATA_TYPE_DB_ID", "DATA_TYPE_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "DATA_TYPE_CD", aDATA_TYPE_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.MIM_DATA_TYPE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;

   }


   /**
    * This function is to retrieve inv ids.
    *
    *
    */
   public simpleIDs getInvIds( String aSERIAL_NO_OEM ) {

      // REF_TASK_CLASS table
      String[] iIds = { "INV_NO_DB_ID", "INV_NO_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SERIAL_NO_OEM", aSERIAL_NO_OEM );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.INV_INV, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;

   }


   /**
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */

   public void RestoreData() {

      String lStrDelete = null;

      // Delete acft_cap_levels data which created by test case
      if ( iINV_Ids != null ) {
         lStrDelete = "delete from " + TableUtil.INV_CURR_USAGE + " where INV_NO_DB_ID='"
               + iINV_Ids.getNO_DB_ID() + "' and INV_NO_ID='" + iINV_Ids.getNO_ID() + "'";
         executeSQL( lStrDelete );

         lStrDelete = "delete from " + TableUtil.INV_INV + " where H_INV_NO_DB_ID='"
               + iINV_Ids.getNO_DB_ID() + "' and H_INV_NO_ID='" + iINV_Ids.getNO_ID() + "'";
         executeSQL( lStrDelete );

         lStrDelete = "delete from " + TableUtil.INV_AC_REG + " where INV_NO_DB_ID='"
               + iINV_Ids.getNO_DB_ID() + "' and INV_NO_ID='" + iINV_Ids.getNO_ID() + "'";
         executeSQL( lStrDelete );

      }

   }

}
