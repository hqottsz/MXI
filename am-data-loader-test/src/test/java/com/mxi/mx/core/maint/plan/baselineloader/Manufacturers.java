package com.mxi.mx.core.maint.plan.baselineloader;

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
 * This test suite contains test cases on validation and import functionality on Manufacturers area.
 *
 * @author ALICIA QIAN
 */
public class Manufacturers extends BaselineLoaderTest {

   ArrayList<String> ModifiedTables = new ArrayList<String>() {

      {
         add( "delete from EQP_MANUFACT where MANUFACT_CD like 'AUTO%'" );
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
    * This is positive test case for manufacture validation functionality by loading data into
    * staging table and then check whether the data in staging and whether error code(s) has(have)
    * been generated.
    *
    */
   @Test
   public void test_MANUFACTURES_IMPORT_VALIDATION() {
      // C_EQP_MANUFACT_MAP table
      Map<String, String> lEQPMANUFACTUREMAP = new LinkedHashMap<>();

      // #1
      lEQPMANUFACTUREMAP.put( "MANUFACT_CD", "\'AUTO10001\'" );
      lEQPMANUFACTUREMAP.put( "MANUFACT_NAME", "\'AUTOMan Name 1\'" );
      lEQPMANUFACTUREMAP.put( "COUNTRY_CD", null );
      lEQPMANUFACTUREMAP.put( "STATE_CD", null );
      lEQPMANUFACTUREMAP.put( "ADDRESS_PMAIL", "\'Manufacturer Address 1\'" );
      lEQPMANUFACTUREMAP.put( "CITY_NAME", "\'City 1\'" );
      lEQPMANUFACTUREMAP.put( "ZIP_CD", "\'LU4 8Q4\'" );
      lEQPMANUFACTUREMAP.put( "PHONE_PH", "\'44-158-2597246\'" );
      lEQPMANUFACTUREMAP.put( "FAX_PH", "\'44-158-2505959\'" );
      lEQPMANUFACTUREMAP.put( "ADDRESS_EMAIL", null );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_EQP_MANUFACT_MAP, lEQPMANUFACTUREMAP ) );

      // #2
      lEQPMANUFACTUREMAP.clear();
      lEQPMANUFACTUREMAP.put( "MANUFACT_CD", "\'AUTO11111\'" );
      lEQPMANUFACTUREMAP.put( "MANUFACT_NAME", "\'AUTOMan Name 2\'" );
      lEQPMANUFACTUREMAP.put( "COUNTRY_CD", null );
      lEQPMANUFACTUREMAP.put( "STATE_CD", null );
      lEQPMANUFACTUREMAP.put( "ADDRESS_PMAIL", "\'Manufacturer Address 2\'" );
      lEQPMANUFACTUREMAP.put( "CITY_NAME", "\'City 2\'" );
      lEQPMANUFACTUREMAP.put( "ZIP_CD", "\'45246\'" );
      lEQPMANUFACTUREMAP.put( "PHONE_PH", "\'(513) 552-2400\'" );
      lEQPMANUFACTUREMAP.put( "FAX_PH", "\'(513) 552-2177\'" );
      lEQPMANUFACTUREMAP.put( "ADDRESS_EMAIL", null );

      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_EQP_MANUFACT_MAP, lEQPMANUFACTUREMAP ) );

      // #3
      lEQPMANUFACTUREMAP.clear();
      lEQPMANUFACTUREMAP.put( "MANUFACT_CD", "\'AUTO1234567890\'" );
      lEQPMANUFACTUREMAP.put( "MANUFACT_NAME", "\'AUTOMan Name 4\'" );
      lEQPMANUFACTUREMAP.put( "COUNTRY_CD", null );
      lEQPMANUFACTUREMAP.put( "STATE_CD", null );
      lEQPMANUFACTUREMAP.put( "ADDRESS_PMAIL", "\'Manufacturer Address 4\'" );
      lEQPMANUFACTUREMAP.put( "CITY_NAME", "\'City 4\'" );
      lEQPMANUFACTUREMAP.put( "ZIP_CD", "\'TA18 7HP\'" );
      lEQPMANUFACTUREMAP.put( "PHONE_PH", "\'0460 73442\'" );
      lEQPMANUFACTUREMAP.put( "FAX_PH", "\'0460 76202\'" );
      lEQPMANUFACTUREMAP.put( "ADDRESS_EMAIL", null );

      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_EQP_MANUFACT_MAP, lEQPMANUFACTUREMAP ) );

      // #4
      lEQPMANUFACTUREMAP.clear();
      lEQPMANUFACTUREMAP.put( "MANUFACT_CD", "\'AUTOABC11\'" );
      lEQPMANUFACTUREMAP.put( "MANUFACT_NAME", "\'AUTOMan Name 3\'" );
      lEQPMANUFACTUREMAP.put( "COUNTRY_CD", null );
      lEQPMANUFACTUREMAP.put( "STATE_CD", null );
      lEQPMANUFACTUREMAP.put( "ADDRESS_PMAIL", "\'Manufacturer Address 3\'" );
      lEQPMANUFACTUREMAP.put( "CITY_NAME", "\'City 3\'" );
      lEQPMANUFACTUREMAP.put( "ZIP_CD", "\'94121\'" );
      lEQPMANUFACTUREMAP.put( "PHONE_PH", "\'(415) 751-0468\'" );
      lEQPMANUFACTUREMAP.put( "FAX_PH", "\'(415) 751-0468\'" );
      lEQPMANUFACTUREMAP.put( "ADDRESS_EMAIL", null );

      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_EQP_MANUFACT_MAP, lEQPMANUFACTUREMAP ) );

      // #5
      lEQPMANUFACTUREMAP.clear();
      lEQPMANUFACTUREMAP.put( "MANUFACT_CD", "\'AUTOLOCAL\'" );
      lEQPMANUFACTUREMAP.put( "MANUFACT_NAME", "\'AUTOLOCAL\'" );
      lEQPMANUFACTUREMAP.put( "COUNTRY_CD", null );
      lEQPMANUFACTUREMAP.put( "STATE_CD", null );
      lEQPMANUFACTUREMAP.put( "ADDRESS_PMAIL", null );
      lEQPMANUFACTUREMAP.put( "CITY_NAME", null );
      lEQPMANUFACTUREMAP.put( "ZIP_CD", null );
      lEQPMANUFACTUREMAP.put( "PHONE_PH", null );
      lEQPMANUFACTUREMAP.put( "FAX_PH", null );
      lEQPMANUFACTUREMAP.put( "ADDRESS_EMAIL", null );

      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_EQP_MANUFACT_MAP, lEQPMANUFACTUREMAP ) );

      // #6
      lEQPMANUFACTUREMAP.clear();
      lEQPMANUFACTUREMAP.put( "MANUFACT_CD", "\'AUTON/A\'" );
      lEQPMANUFACTUREMAP.put( "MANUFACT_NAME", "\'AUTON/A\'" );
      lEQPMANUFACTUREMAP.put( "COUNTRY_CD", null );
      lEQPMANUFACTUREMAP.put( "STATE_CD", null );
      lEQPMANUFACTUREMAP.put( "ADDRESS_PMAIL", null );
      lEQPMANUFACTUREMAP.put( "CITY_NAME", null );
      lEQPMANUFACTUREMAP.put( "ZIP_CD", null );
      lEQPMANUFACTUREMAP.put( "PHONE_PH", null );
      lEQPMANUFACTUREMAP.put( "FAX_PH", null );
      lEQPMANUFACTUREMAP.put( "ADDRESS_EMAIL", null );

      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_EQP_MANUFACT_MAP, lEQPMANUFACTUREMAP ) );

      // run validation
      Assert.assertTrue( runValidateEQPMANMAP() == 1 );

   }


   /**
    * This is positive test case for manufacture validation and import functionality by loading data
    * into staging table and then check whether the data has been loaded correctly into maintenix
    * tables
    *
    */

   @Test
   public void test_MANUFACTURES_IMPORT_IMPORT() {
      test_MANUFACTURES_IMPORT_VALIDATION();

      // run validation
      Assert.assertTrue( runImportEQPMANMAP() == 1 );

      // check maintenix table with staging table
      runCheckImportValidation();

   }


   /**
    * Run the task Validation using a direct call to the plsql using a prepared statement. This does
    * not call any batch files.
    *
    *
    */
   protected int runImportEQPMANMAP() {
      int lReturn = 0;

      try {
         CallableStatement lPrepareCallInventory = getConnection()
               .prepareCall( "BEGIN   manufact_import.import_manufact(on_retcode => ?); END;" );
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
    * This method checks that the import was successful by calling other functions that compare each
    * row in staging and maitenix tables in finance area and make sure they are identical through
    * SQL queries
    *
    * @throws Exception
    *
    */
   private void runCheckImportValidation() {
      try {
         compareRowEQPMANUFACTUREMAP();
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
   protected int runValidateEQPMANMAP() {
      int lReturn = 0;

      try {
         CallableStatement lPrepareCallInventory = getConnection()
               .prepareCall( "BEGIN manufact_import.validate_manufact(on_retcode =>  ?); END;" );
         lPrepareCallInventory.registerOutParameter( 1, Types.INTEGER );
         lPrepareCallInventory.execute();
         commit();
         lReturn = lPrepareCallInventory.getInt( 1 );
      } catch ( SQLException e ) {
         e.printStackTrace();
      }
      return lReturn;

   }

}
