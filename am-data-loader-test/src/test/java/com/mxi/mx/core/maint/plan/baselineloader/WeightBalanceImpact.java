package com.mxi.mx.core.maint.plan.baselineloader;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
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

import com.mxi.mx.core.maint.plan.datamodels.partNo;
import com.mxi.mx.util.BaselineLoaderTest;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.ValidationAndImport;
import com.mxi.mx.util.WhereClause;


/**
 * This test suite contains test cases on validation and import functionality of TASK WEIGHT BALANCE
 * package in Weight and Balance Impact area. The TCs are only covering Jira 19760 which synchronize
 * data type of WEIGHT and MOMENT columns with BL_WEIGHT_BALANCE and TASK_WEIGHT_BALANCE
 *
 * @author ALICIA QIAN
 */
public class WeightBalanceImpact extends BaselineLoaderTest {

   @Rule
   public TestName testName = new TestName();

   ValidationAndImport iValidationandimport;

   @SuppressWarnings( "serial" )
   ArrayList<String> updateTables = new ArrayList<String>() {

      {
         add( "delete from BL_WEIGHT_BALANCE" );

      }
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
      RestoreData();
      String lstrTCName = testName.getMethodName();
      if ( lstrTCName.contains( "Multiple" ) ) {
         clearBaselineMTables( updateTables );

      } else {
         SetDefaultValue_ACFT();
         clearBaselineMTables( updateTables );

      }

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
    * This test is to verify integer values of weight and moment in staging table are populated
    * correctly.
    *
    *
    */
   @Test
   public void testWithoutDecimal_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // BL_WEIGHT_BALANCE table
      Map<String, String> lBlWTBLC = new LinkedHashMap<>();

      //
      lBlWTBLC.put( "ASSMBL_CD", "\'ACFT_CD1\'" );
      lBlWTBLC.put( "REQ_TASK_CD", "\'AL_BUILD\'" );
      lBlWTBLC.put( "REQ_ATA_CD", "\'ACFT_CD1\'" );
      lBlWTBLC.put( "PART_NO_OEM", "\'ACFT_ASSY_PN1\'" );
      lBlWTBLC.put( "MANUFACT_CD", "\'10001\'" );
      lBlWTBLC.put( "WEIGHT", "\'15\'" );
      lBlWTBLC.put( "MOMENT", "\'20\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_WEIGHT_BALANCE, lBlWTBLC ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // Validate BL_WEIGHT_BALANCE table
      ValidateBLWEIGHTBALANCE( "ACFT_CD1", "15", "20" );

   }


   /**
    * This test is to verify integer values of weight and moment in staging table are populated
    * correctly into task_weight_balance table.
    *
    *
    */
   @Test
   public void testWithoutDecimal_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testWithoutDecimal_VALIDATION();

      System.out.println( "Finish validation" );

      // Run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Get PartNo information
      partNo lPN = GetPartInfor( "ACFT_ASSY_PN1", "10001" );

      // Validate TASK_WEIGHT_BALANCE table
      ValidateTASKWEIGHTBALANCE( lPN, "15", "20" );

   }


   /**
    * This test is to verify decimal values of weight and moment in staging table are rounded and
    * populated into staging table.
    *
    *
    */
   @Test
   public void testWithDecimalRound_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // BL_WEIGHT_BALANCE table
      Map<String, String> lBlWTBLC = new LinkedHashMap<>();

      //
      lBlWTBLC.put( "ASSMBL_CD", "\'ACFT_CD1\'" );
      lBlWTBLC.put( "REQ_TASK_CD", "\'AL_BUILD\'" );
      lBlWTBLC.put( "REQ_ATA_CD", "\'ACFT_CD1\'" );
      lBlWTBLC.put( "PART_NO_OEM", "\'ACFT_ASSY_PN1\'" );
      lBlWTBLC.put( "MANUFACT_CD", "\'10001\'" );
      lBlWTBLC.put( "WEIGHT", "\'15.123456\'" );
      lBlWTBLC.put( "MOMENT", "\'20.123456\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_WEIGHT_BALANCE, lBlWTBLC ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // Validate BL_WEIGHT_BALANCE table
      ValidateBLWEIGHTBALANCE( "ACFT_CD1", "15.12346", "20.12346" );

   }


   /**
    * This test is to verify decimal values of weight and moment in staging table are rounded and
    * populated into task_weight_balance table.
    *
    *
    */
   @Test
   public void testWithDecimalRound_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testWithDecimalRound_VALIDATION();

      System.out.println( "Finish validation" );

      // Run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Get PartNo information
      partNo lPN = GetPartInfor( "ACFT_ASSY_PN1", "10001" );

      // Validate TASK_WEIGHT_BALANCE table
      ValidateTASKWEIGHTBALANCE( lPN, "15.12346", "20.12346" );

   }


   /**
    * This test is to verify decimal values of weight and moment in staging table are populated
    * correctly into staging table.
    *
    *
    */
   @Test
   public void testWithDecimal_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // BL_WEIGHT_BALANCE table
      Map<String, String> lBlWTBLC = new LinkedHashMap<>();

      //
      lBlWTBLC.put( "ASSMBL_CD", "\'ACFT_CD1\'" );
      lBlWTBLC.put( "REQ_TASK_CD", "\'AL_BUILD\'" );
      lBlWTBLC.put( "REQ_ATA_CD", "\'ACFT_CD1\'" );
      lBlWTBLC.put( "PART_NO_OEM", "\'ACFT_ASSY_PN1\'" );
      lBlWTBLC.put( "MANUFACT_CD", "\'10001\'" );
      lBlWTBLC.put( "WEIGHT", "\'15.12\'" );
      lBlWTBLC.put( "MOMENT", "\'20.12\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_WEIGHT_BALANCE, lBlWTBLC ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // Validate BL_WEIGHT_BALANCE table
      ValidateBLWEIGHTBALANCE( "ACFT_CD1", "15.12", "20.12" );

   }


   /**
    * This test is to verify decimal values of weight and moment in staging table are populated
    * correctly into task_weight_balance table.
    *
    *
    */
   @Test
   public void testWithDecimal_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testWithDecimal_VALIDATION();

      System.out.println( "Finish validation" );

      // Run import
      Assert.assertTrue( runValidationAndImport( false, false ) == 1 );

      // Get PartNo information
      partNo lPN = GetPartInfor( "ACFT_ASSY_PN1", "10001" );

      // Validate TASK_WEIGHT_BALANCE table
      ValidateTASKWEIGHTBALANCE( lPN, "15.12", "20.12" );

   }


   /**
    * This test is to verify default and part multiple data in staging table are populated correctly
    * into staging table.
    *
    *
    */
   @Test
   public void testMultipleRecords_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // BL_WEIGHT_BALANCE table to insert default data
      Map<String, String> lBlWTBLC = new LinkedHashMap<>();
      lBlWTBLC.put( "ASSMBL_CD", "\'ACFT_CD1\'" );
      lBlWTBLC.put( "REQ_TASK_CD", "\'AL_BUILD\'" );
      lBlWTBLC.put( "REQ_ATA_CD", "\'ACFT_CD1\'" );
      lBlWTBLC.put( "PART_NO_OEM", null );
      lBlWTBLC.put( "MANUFACT_CD", null );
      lBlWTBLC.put( "WEIGHT", "\'100.12\'" );
      lBlWTBLC.put( "MOMENT", "\'200.12\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_WEIGHT_BALANCE, lBlWTBLC ) );

      // BL_WEIGHT_BALANCE table to insert part data
      lBlWTBLC.clear();
      lBlWTBLC.put( "ASSMBL_CD", "\'ACFT_CD1\'" );
      lBlWTBLC.put( "REQ_TASK_CD", "\'AL_BUILD\'" );
      lBlWTBLC.put( "REQ_ATA_CD", "\'ACFT_CD1\'" );
      lBlWTBLC.put( "PART_NO_OEM", "\'ACFT_ASSY_PN1\'" );
      lBlWTBLC.put( "MANUFACT_CD", "\'10001\'" );
      lBlWTBLC.put( "WEIGHT", "\'15.12\'" );
      lBlWTBLC.put( "MOMENT", "\'20.12\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_WEIGHT_BALANCE, lBlWTBLC ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // Validate BL_WEIGHT_BALANCE table
      ValidateBLWEIGHTBALANCE( "ACFT_CD1", "15.12", "20.12", "ACFT_ASSY_PN1" );

   }


   /**
    * This test is to verify default and part record are imported correctly into task_weight_balance
    * table.
    *
    *
    */
   @Test
   public void testMultipleRecords_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testMultipleRecords_VALIDATION();

      System.out.println( "Finish validation" );

      // Run import
      Assert.assertTrue( runValidationAndImport( false, false ) == 1 );

      // Get PartNo information
      partNo lPN = GetPartInfor( "ACFT_ASSY_PN1", "10001" );

      // Validate TASK_WEIGHT_BALANCE table
      ValidateTASKWEIGHTBALANCE( lPN, "15.12", "20.12" );

   }


   // ===================================================================================================
   /**
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */

   public void RestoreData() {

      String lStrDelete = "delete from " + TableUtil.TASK_WEIGHT_BALANCE;
      PreparedStatement lStatement;
      try {

         // delete data in TASK_WEIGHT_BALANCE table
         lStatement = getConnection().prepareStatement( lStrDelete );
         lStatement.executeUpdate( lStrDelete );
         commit();

      } catch ( SQLException e ) {

         e.printStackTrace();
      }

   }


   /**
    * This function is called before each test method to set up default weight and moment value.
    *
    *
    */
   public void SetDefaultValue_ACFT() {

      // BL_WEIGHT_BALANCE table
      Map<String, String> lBlWTBLC = new LinkedHashMap<>();

      //
      lBlWTBLC.put( "ASSMBL_CD", "\'ACFT_CD1\'" );
      lBlWTBLC.put( "REQ_TASK_CD", "\'AL_BUILD\'" );
      lBlWTBLC.put( "REQ_ATA_CD", "\'ACFT_CD1\'" );
      lBlWTBLC.put( "PART_NO_OEM", null );
      lBlWTBLC.put( "MANUFACT_CD", null );
      lBlWTBLC.put( "WEIGHT", "\'100.12\'" );
      lBlWTBLC.put( "MOMENT", "\'200.12\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_WEIGHT_BALANCE, lBlWTBLC ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );
   }


   /**
    * This function is to validate BL_WEIGHT_BALANCE.
    *
    *
    */

   public void ValidateBLWEIGHTBALANCE( String aAssmblCD, String aWeight, String aMoment ) {

      String[] lfieds = { "WEIGHT", "MOMENT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( lfieds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "ASSMBL_CD", aAssmblCD );

      String lQueryString = TableUtil.buildTableQuery( "BL_WEIGHT_BALANCE", lfields, lArgs );
      List<ArrayList<String>> llists = execute( lQueryString, lfields );

      // Validation
      Assert.assertTrue( "WEIGHT", llists.get( 0 ).get( 0 ).equalsIgnoreCase( aWeight ) );
      Assert.assertTrue( "MOMENT", llists.get( 0 ).get( 1 ).equalsIgnoreCase( aMoment ) );

   }


   /**
    * This function is to validate BL_WEIGHT_BALANCE.
    *
    *
    */

   public void ValidateBLWEIGHTBALANCE( String aAssmblCD, String aWeight, String aMoment,
         String aPNOEM ) {

      String[] lfieds = { "WEIGHT", "MOMENT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( lfieds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "ASSMBL_CD", aAssmblCD );
      lArgs.addArguments( "PART_NO_OEM", aPNOEM );

      String lQueryString = TableUtil.buildTableQuery( "BL_WEIGHT_BALANCE", lfields, lArgs );
      List<ArrayList<String>> llists = execute( lQueryString, lfields );

      // Validation
      Assert.assertTrue( "WEIGHT", llists.get( 0 ).get( 0 ).equalsIgnoreCase( aWeight ) );
      Assert.assertTrue( "MOMENT", llists.get( 0 ).get( 1 ).equalsIgnoreCase( aMoment ) );

   }


   /**
    * This function is to validate TASK_WEIGHT_BALANCE.
    *
    *
    */
   public void ValidateTASKWEIGHTBALANCE( partNo aPN, String aWeight, String aMoment ) {
      String[] lfieds = { "WEIGHT", "MOMENT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( lfieds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "PART_NO_DB_ID", aPN.getPART_NO_DB_ID() );
      lArgs.addArguments( "PART_NO_ID", aPN.getPART_NO_ID() );

      String lQueryString = TableUtil.buildTableQuery( "TASK_WEIGHT_BALANCE", lfields, lArgs );
      List<ArrayList<String>> llists = execute( lQueryString, lfields );

      // Validation
      Assert.assertTrue( "WEIGHT", llists.get( 0 ).get( 0 ).equalsIgnoreCase( aWeight ) );
      Assert.assertTrue( "MOMENT", llists.get( 0 ).get( 1 ).equalsIgnoreCase( aMoment ) );

   }


   /**
    * This function is to retrieve part no information by given part_no_oem and manufact_cd
    *
    *
    */
   public partNo GetPartInfor( String aPartOEM, String aMACFTCD ) {

      String[] lfieds = { "PART_NO_DB_ID", "PART_NO_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( lfieds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "PART_NO_OEM", aPartOEM );
      lArgs.addArguments( "MANUFACT_CD", aMACFTCD );

      String lQueryString = TableUtil.buildTableQuery( "EQP_PART_NO", lfields, lArgs );
      List<ArrayList<String>> llists = execute( lQueryString, lfields );

      // Get PART NO information
      partNo lPN = new partNo( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lPN;

   }


   /**
    * This function is to implement interface ValidationAndImport
    *
    * @param: blnOnlyValidation
    *
    */
   public int runValidationAndImport( boolean blnOnlyValidation, boolean allornone ) {
      int rtValue = 0;
      iValidationandimport = new ValidationAndImport() {

         @Override
         public int runValidation( boolean allornone ) {
            int lReturn = 0;
            CallableStatement lPrepareCallWeightBalance;

            try {
               if ( allornone ) {
                  lPrepareCallWeightBalance = getConnection().prepareCall(
                        "BEGIN bl_weight_balance_import.validate(aiv_exist_in_mx => 'STRICT', aib_allornone => true, aon_retcode => ?,  aov_retmsg => ?); END;" );
               } else {
                  lPrepareCallWeightBalance = getConnection().prepareCall(
                        "BEGIN bl_weight_balance_import.validate(aiv_exist_in_mx => 'STRICT', aib_allornone => false, aon_retcode => ?,  aov_retmsg => ?); END;" );

               }
               lPrepareCallWeightBalance.registerOutParameter( 1, Types.INTEGER );
               lPrepareCallWeightBalance.registerOutParameter( 2, Types.VARCHAR );
               lPrepareCallWeightBalance.execute();
               commit();
               lReturn = lPrepareCallWeightBalance.getInt( 1 );

            } catch ( SQLException e ) {
               e.printStackTrace();
            }
            return lReturn;

         }


         @Override
         public int runImport( boolean allornone ) {
            int lReturn = 0;
            CallableStatement lPrepareCallWeightBalance;

            try {
               if ( allornone ) {
                  lPrepareCallWeightBalance = getConnection().prepareCall(
                        "BEGIN bl_weight_balance_import.import(aiv_exist_in_mx => 'STRICT', aib_allornone => true, aon_retcode => ?,  aov_retmsg => ?); END;" );
               } else {
                  lPrepareCallWeightBalance = getConnection().prepareCall(
                        "BEGIN bl_weight_balance_import.import(aiv_exist_in_mx => 'STRICT', aib_allornone => false, aon_retcode => ?,  aov_retmsg => ?); END;" );

               }

               lPrepareCallWeightBalance.registerOutParameter( 1, Types.INTEGER );
               lPrepareCallWeightBalance.registerOutParameter( 2, Types.VARCHAR );
               lPrepareCallWeightBalance.execute();
               commit();
               lReturn = lPrepareCallWeightBalance.getInt( 1 );

            } catch ( SQLException e ) {
               e.printStackTrace();
            }
            return lReturn;
         }

      };

      rtValue = blnOnlyValidation ? iValidationandimport.runValidation( allornone )
            : iValidationandimport.runImport( allornone );

      return rtValue;
   }

}
