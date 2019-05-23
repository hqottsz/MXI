package com.mxi.mx.core.maint.plan.baselineloader.Usage;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;

import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.BaselineLoaderTest;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.TestConstants;
import com.mxi.mx.util.ValidationAndImport;
import com.mxi.mx.util.WhereClause;


/**
 * This class contains common functions which support validation and import functionality of
 * usage_import package.
 *
 * @author ALICIA QIAN
 */
public class Usage extends BaselineLoaderTest {

   ValidationAndImport ivalidationandimport;

   public String iAssmblCD_ACFT = "ACFT_T9";
   public String iAssmblCD_ENG = "ENG_CD9";
   public String iIPC_REF_CD_TRK = "ACFT-SYS-1-1-TRK-BATCH-PARENT";
   public String iIPC_REF_CD_SUBASSY = "ENG-ASSY";
   public String iIPC_REF_CD_ENG = "ENG_CD9";
   public String iDATA_TYPE_CD_HOUR = "HOURS";
   public String iDATA_TYPE_CD_CYCLES = "CYCLES";
   public String iDATA_TYPE_CD_ECYC = "ECYC";
   public String iDATA_SOURCE_CD = "BULK";


   /**
    * This function is to verify eqp_data_source_spec table
    *
    *
    */
   public void VerifyEQPDATASOURCESPEC( String aASSMBL_CD, String aDATA_SOURCE_CD,
         simpleIDs ldatatypeIds ) {

      String[] iIds = { "DATA_TYPE_DB_ID", "DATA_TYPE_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "ASSMBL_CD", aASSMBL_CD );
      lArgs.addArguments( "DATA_SOURCE_CD", aDATA_SOURCE_CD );

      String iQueryString =
            TableUtil.buildTableQuery( TableUtil.EQP_DATA_SOURCE_SPEC, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "DATA_TYPE_DB_ID",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( ldatatypeIds.getNO_DB_ID() ) );
      Assert.assertTrue( "DATA_TYPE_ID",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( ldatatypeIds.getNO_ID() ) );

   }


   /**
    * This function is to verify mim_part_numdata table
    *
    *
    */
   public void VerifyMIMPARTNUMDATA( String aASSMBL_CD, String aASSMBL_BOM_ID,
         simpleIDs ldatatypeIds, String aCUST_CALC_BOOL, String aEQN_LDESC ) {

      String[] iIds = { "DATA_TYPE_DB_ID", "DATA_TYPE_ID", "CUST_CALC_BOOL", "EQN_LDESC" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "ASSMBL_CD", aASSMBL_CD );
      lArgs.addArguments( "ASSMBL_BOM_ID", aASSMBL_BOM_ID );
      lArgs.addArguments( "DATA_TYPE_DB_ID", ldatatypeIds.getNO_DB_ID() );
      lArgs.addArguments( "DATA_TYPE_ID", ldatatypeIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.MIM_PART_NUMDATA, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "DATA_TYPE_DB_ID",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( ldatatypeIds.getNO_DB_ID() ) );
      Assert.assertTrue( "DATA_TYPE_ID",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( ldatatypeIds.getNO_ID() ) );
      Assert.assertTrue( "CUST_CALC_BOOL",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aCUST_CALC_BOOL ) );

      if ( aEQN_LDESC != null ) {
         Assert.assertTrue( "CUST_CALC_BOOL",
               llists.get( 0 ).get( 3 ).equalsIgnoreCase( aEQN_LDESC ) );

      }

   }


   /**
    * Calls check bl_usage error code
    *
    *
    */
   protected void checkUsageErrorCode( String aTestName, String aValidationCode ) {

      boolean lFound = false;
      String lerror_desc = null;
      String lerrorMsg = null;

      String lquery = TestConstants.BL_USAGE_ERROR_CHECK;

      String[] iIds = { "RESULT_CD", "TECH_DESC" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      List<ArrayList<String>> lresult = execute( lquery, lfields );

      for ( int i = 0; i < lresult.size(); i++ ) {
         if ( lresult.get( i ).get( 0 ).equalsIgnoreCase( aValidationCode ) ) {
            lerror_desc = lresult.get( i ).get( 1 );
            lFound = true;
         } else {
            lerrorMsg = lerrorMsg + lresult.get( i ).get( 0 ) + ": " + lresult.get( i ).get( 1 );
         }

      }

      Assert.assertTrue( "The error code not found- " + aValidationCode + " : " + lerror_desc
            + " other error found [" + lerrorMsg + "]", lFound );

   }


   /**
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */
   public void RestoreData() {

      String lStrDelete = "delete from " + TableUtil.MIM_PART_NUMDATA + " where ASSMBL_CD='"
            + iAssmblCD_ACFT + "'";
      executeSQL( lStrDelete );

      lStrDelete = "delete from " + TableUtil.EQP_DATA_SOURCE_SPEC + " where ASSMBL_CD='"
            + iAssmblCD_ACFT + "'";
      executeSQL( lStrDelete );

      lStrDelete = "delete from " + TableUtil.MIM_PART_NUMDATA + " where ASSMBL_CD='"
            + iAssmblCD_ENG + "'";
      executeSQL( lStrDelete );

      lStrDelete = "delete from " + TableUtil.EQP_DATA_SOURCE_SPEC + " where ASSMBL_CD='"
            + iAssmblCD_ENG + "'";
      executeSQL( lStrDelete );

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
                  lPrepareCallKIT = getConnection()
                        .prepareCall( "BEGIN  usage_import.validate_usage(on_retcode =>?); END;" );
               } else {
                  lPrepareCallKIT = getConnection()
                        .prepareCall( "BEGIN  usage_import.validate_usage(on_retcode =>?); END;" );

               }

               lPrepareCallKIT.registerOutParameter( 1, Types.INTEGER );
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
                  lPrepareCallKIT = getConnection()
                        .prepareCall( "BEGIN  usage_import.import_usage(on_retcode =>?); END;" );
               } else {
                  lPrepareCallKIT = getConnection()
                        .prepareCall( "BEGIN  usage_import.import_usage(on_retcode =>?); END;" );

               }

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

      rtValue = blnOnlyValidation ? ivalidationandimport.runValidation( allornone )
            : ivalidationandimport.runImport( allornone );

      return rtValue;
   }

}
