package com.mxi.mx.core.maint.plan.baselineloader;

import static org.hamcrest.MatcherAssert.assertThat;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mxi.mx.util.BaselineLoaderTest;
import com.mxi.mx.util.TableUtil;


/**
 * This test suite contains test cases on validation and import functionality on FINANCE area.
 *
 * @author ALICIA QIAN
 */
public class Finance extends BaselineLoaderTest {

   ArrayList<String> ModifiedTables = new ArrayList<String>() {

      {
         add( "delete from fnc_account where ACCOUNT_LDESC like 'AUTO%'" );
         add( "delete from fnc_tcode where DESC_LDESC like 'AUTO%'" );
      }
   };


   /**
    * Clean up after each individual test
    *
    * @throws Exception
    */
   @After
   @Override
   public void after() {
      clearBaselineMTables( ModifiedTables );
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
   }


   /**
    * This is positive test case for finance account validation functionality by loading data into
    * staging table and then check whether the data in staging and whether error code(s) has(have)
    * been generated.
    *
    */
   @Test
   public void test_FINANCE_IMPORT_VALIDATION() {
      // C_FNC_ACCOUNT table
      Map<String, String> lFinanceAccount = new LinkedHashMap<>();

      // #1
      lFinanceAccount.put( "ACCOUNT_CD", "\'AUTO2\'" );
      lFinanceAccount.put( "ACCOUNT_NAME", "\'Inventory Asset Account\'" );
      lFinanceAccount.put( "ACCOUNT_LDESC", "\'AUTOInventory Asset Account\'" );
      lFinanceAccount.put( "ACCOUNT_TYPE_CD", "\'INVASSET\'" );
      lFinanceAccount.put( "NH_ACCOUNT_CD", null );
      lFinanceAccount.put( "DEFAULT_BOOL", "\'Y\'" );
      lFinanceAccount.put( "EXT_KEY_SDESC", null );
      lFinanceAccount.put( "TCODE_CD", "\'AUTOCD1\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_FNC_ACCOUNT, lFinanceAccount ) );

      // #2
      lFinanceAccount.clear();
      lFinanceAccount.put( "ACCOUNT_CD", "\'AUTO8\'" );
      lFinanceAccount.put( "ACCOUNT_NAME", "\'Change Owner Account\'" );
      lFinanceAccount.put( "ACCOUNT_LDESC", "\'AUTOChange Owner Account\'" );
      lFinanceAccount.put( "ACCOUNT_TYPE_CD", "\'CHGOWN\'" );
      lFinanceAccount.put( "NH_ACCOUNT_CD", null );
      lFinanceAccount.put( "DEFAULT_BOOL", "\'Y\'" );
      lFinanceAccount.put( "EXT_KEY_SDESC", null );
      lFinanceAccount.put( "TCODE_CD", "\'AUTOCD2\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_FNC_ACCOUNT, lFinanceAccount ) );

      // #3
      lFinanceAccount.clear();
      lFinanceAccount.put( "ACCOUNT_CD", "\'AUTO3\'" );
      lFinanceAccount.put( "ACCOUNT_NAME", "\'Quantity Adjustment\'" );
      lFinanceAccount.put( "ACCOUNT_LDESC", "\'AUTOQuantity Adjustment\'" );
      lFinanceAccount.put( "ACCOUNT_TYPE_CD", "\'ADJQTY\'" );
      lFinanceAccount.put( "NH_ACCOUNT_CD", null );
      lFinanceAccount.put( "DEFAULT_BOOL", "\'Y\'" );
      lFinanceAccount.put( "EXT_KEY_SDESC", null );
      lFinanceAccount.put( "TCODE_CD", "\'AUTOCD3\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_FNC_ACCOUNT, lFinanceAccount ) );

      // #4
      lFinanceAccount.clear();
      lFinanceAccount.put( "ACCOUNT_CD", "\'AUTO5\'" );
      lFinanceAccount.put( "ACCOUNT_NAME", "\'Expense\'" );
      lFinanceAccount.put( "ACCOUNT_LDESC", "\'AUTOExpense\'" );
      lFinanceAccount.put( "ACCOUNT_TYPE_CD", "\'EXPENSE\'" );
      lFinanceAccount.put( "NH_ACCOUNT_CD", null );
      lFinanceAccount.put( "DEFAULT_BOOL", "\'Y\'" );
      lFinanceAccount.put( "EXT_KEY_SDESC", null );
      lFinanceAccount.put( "TCODE_CD", null );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_FNC_ACCOUNT, lFinanceAccount ) );

      // #5
      lFinanceAccount.clear();
      lFinanceAccount.put( "ACCOUNT_CD", "\'AUTO7\'" );
      lFinanceAccount.put( "ACCOUNT_NAME", "\'Return Vendor Credit Account\'" );
      lFinanceAccount.put( "ACCOUNT_LDESC", "\'AUTOReturn Vendor Credit Account\'" );
      lFinanceAccount.put( "ACCOUNT_TYPE_CD", "\'RTVCRED\'" );
      lFinanceAccount.put( "NH_ACCOUNT_CD", null );
      lFinanceAccount.put( "DEFAULT_BOOL", "\'Y\'" );
      lFinanceAccount.put( "EXT_KEY_SDESC", null );
      lFinanceAccount.put( "TCODE_CD", "\'AUTOCD1\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_FNC_ACCOUNT, lFinanceAccount ) );

      // #6
      lFinanceAccount.clear();
      lFinanceAccount.put( "ACCOUNT_CD", "\'AUTO4\'" );
      lFinanceAccount.put( "ACCOUNT_NAME", "\'Scrap\'" );
      lFinanceAccount.put( "ACCOUNT_LDESC", "\'AUTOScrap\'" );
      lFinanceAccount.put( "ACCOUNT_TYPE_CD", "\'SCRAP\'" );
      lFinanceAccount.put( "NH_ACCOUNT_CD", null );
      lFinanceAccount.put( "DEFAULT_BOOL", "\'Y\'" );
      lFinanceAccount.put( "EXT_KEY_SDESC", null );
      lFinanceAccount.put( "TCODE_CD", "\'AUTOCD1\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_FNC_ACCOUNT, lFinanceAccount ) );

      // #7
      lFinanceAccount.clear();
      lFinanceAccount.put( "ACCOUNT_CD", "\'AUTO6\'" );
      lFinanceAccount.put( "ACCOUNT_NAME", "\'Adjust Price Account\'" );
      lFinanceAccount.put( "ACCOUNT_LDESC", "\'AUTOAdjust Price Account\'" );
      lFinanceAccount.put( "ACCOUNT_TYPE_CD", "\'ADJPRICE\'" );
      lFinanceAccount.put( "NH_ACCOUNT_CD", null );
      lFinanceAccount.put( "DEFAULT_BOOL", "\'Y\'" );
      lFinanceAccount.put( "EXT_KEY_SDESC", null );
      lFinanceAccount.put( "TCODE_CD", null );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_FNC_ACCOUNT, lFinanceAccount ) );

      // #8
      lFinanceAccount.clear();
      lFinanceAccount.put( "ACCOUNT_CD", "\'AUTO1\'" );
      lFinanceAccount.put( "ACCOUNT_NAME", "\'Fixed Asset Account\'" );
      lFinanceAccount.put( "ACCOUNT_LDESC", "\'AUTO Fixed Asset Account\'" );
      lFinanceAccount.put( "ACCOUNT_TYPE_CD", "\'FIXASSET\'" );
      lFinanceAccount.put( "NH_ACCOUNT_CD", null );
      lFinanceAccount.put( "DEFAULT_BOOL", "\'Y\'" );
      lFinanceAccount.put( "EXT_KEY_SDESC", null );
      lFinanceAccount.put( "TCODE_CD", null );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_FNC_ACCOUNT, lFinanceAccount ) );

      // C_FNC_TCODE table
      Map<String, String> lFinanceTCode = new LinkedHashMap<>();
      // #1
      lFinanceTCode.put( "TCODE_CD", "\'AUTOCD1\'" );
      lFinanceTCode.put( "TCODE_NAME", "\'AUTOCD1\'" );
      lFinanceTCode.put( "TCODE_LDESC", "\'AUTOCD1\'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_FNC_TCODE, lFinanceTCode ) );

      // #2
      lFinanceTCode.clear();
      lFinanceTCode.put( "TCODE_CD", "\'AUTOCD2\'" );
      lFinanceTCode.put( "TCODE_NAME", "\'AUTOCD2\'" );
      lFinanceTCode.put( "TCODE_LDESC", "\'AUTOCD2\'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_FNC_TCODE, lFinanceTCode ) );

      // #3
      lFinanceTCode.clear();
      lFinanceTCode.put( "TCODE_CD", "\'AUTOCD3\'" );
      lFinanceTCode.put( "TCODE_NAME", "\'AUTOCD3\'" );
      lFinanceTCode.put( "TCODE_LDESC", "\'AUTOCD3\'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_FNC_TCODE, lFinanceTCode ) );

      // run validation
      Assert.assertTrue( runValidateFNCACCOUNT() == 1 );

   }


   /**
    * This is positive test case for finance account validation and import functionality by loading
    * data into staging table and then check whether the data has been loaded correctly into
    * maintenix tables
    *
    */
   @Test
   public void test_FINANCE_IMPORT_IMPORT() {

      test_FINANCE_IMPORT_VALIDATION();

      // run import validation
      Assert.assertTrue( runImportFNCACCOUNT() == 1 );

      // check maintenix table with staging table
      runCheckImportValidation();

   }


   /**
    * This method checks that the import was successful by calling other functions that compare each
    * row in staging and maitenix tables in finance area and make sure they are identical through
    * SQL queries
    *
    * @throws Exception
    *
    */
   private void runCheckImportValidation() {
      try {
         compareRowFinanceAccount();
         compareRowFinanceTCode();
      } catch ( Exception e ) {
         e.printStackTrace();
      }
   }


   /**
    * Run the task Validation using a direct call to the plsql using a prepared statement. This does
    * not call any batch files.
    *
    *
    */
   protected int runValidateFNCACCOUNT() {

      int lReturn = 0;

      try {
         CallableStatement lPrepareCallInventory = getConnection()
               .prepareCall( "BEGIN finance_import.validate_finance(on_retcode => ?); END;" );
         lPrepareCallInventory.registerOutParameter( 1, Types.INTEGER );
         lPrepareCallInventory.execute();
         commit();
         lReturn = lPrepareCallInventory.getInt( 1 );
      } catch ( SQLException e ) {
         e.printStackTrace();
      }
      return lReturn;

   }


   /**
    * Run the task Validation using a direct call to the plsql using a prepared statement. This does
    * not call any batch files.
    *
    *
    */
   protected int runImportFNCACCOUNT() {
      int lReturn = 0;

      try {
         CallableStatement lPrepareCallInventory = getConnection()
               .prepareCall( "BEGIN   finance_import.import_finance(on_retcode => ?); END;" );
         lPrepareCallInventory.registerOutParameter( 1, Types.INTEGER );
         lPrepareCallInventory.execute();
         commit();
         lReturn = lPrepareCallInventory.getInt( 1 );
      } catch ( SQLException e ) {
         e.printStackTrace();
      }
      return lReturn;

   }


   /**
    * Verifies results of validation, taking into account USG errors
    *
    * @param aTestName
    *           the test method name
    * @param aValidationCode
    *           Expected validation result
    * @param aTableName
    *           The name of the database table to check
    *
    * @throws Exception
    *            if there is an error
    */
   protected void checkError( String aTestName, String aValidationCode, String aTableName )
         throws Exception {

      int lErrorCount;

      lErrorCount = getErrorCodeCount( "RESULT_CD", aTableName );

      String lTestResultErrorList = "";

      // if there are any errors query for results
      if ( lErrorCount > 0 ) {
         lTestResultErrorList = getLoadErrors( aTableName );
      }

      // asserting that expected error was found
      assertThat(
            "Unexpected validation results. Expected this in " + aTableName + ": " + aValidationCode
                  + ". Found:  \n " + lTestResultErrorList,
            lTestResultErrorList.contains( aValidationCode ) );

   }

}
