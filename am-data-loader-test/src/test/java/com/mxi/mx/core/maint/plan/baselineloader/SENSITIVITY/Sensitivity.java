package com.mxi.mx.core.maint.plan.baselineloader.SENSITIVITY;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;

import com.mxi.mx.core.maint.plan.datamodels.AssmblBomID;
import com.mxi.mx.core.maint.plan.datamodels.bomPartPN;
import com.mxi.mx.util.BaselineLoaderTest;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.TestConstants;
import com.mxi.mx.util.ValidationAndImport;
import com.mxi.mx.util.WhereClause;


/**
 * This class contains common functions which support validation and import functionality of
 * BL_SENSITIVITY_IMPORT package.
 *
 * @author ALICIA QIAN
 */
public class Sensitivity extends BaselineLoaderTest {

   public String iAssmblCD_ACFT = "ACFT_CD1";
   public String iAssmblCD_ENG = "ENG_CD1";
   public String iCONFIG_SLOT_CD_ACFT = "SYS-1";
   public String iCONFIG_SLOT_CD_ENG = "ENG-SYS-1";
   public String iPART_GROUP_CD_TRK = "ACFT-SYS-1-1-TRK-P1";
   public String iPART_GROUP_CD_ASSY = "APU-ASSY";
   public String iPART_GROUP_CD_KIT = "ATKITGP";
   public String iPART_GROUP_CD_BATCH = "ACFT-SYS-1-1-TRK-BATCH-CHILD";
   public String iPART_GROUP_CD_SER = "ACFT-SYS-1-1-TRK-SER-CHILD";
   public String iSENSITIVITY_CD = "AT_SENS";
   public String iSENSITIVITY_CD_2 = "AT_SN2";

   ValidationAndImport ivalidationandimport;


   /**
    * Calls check sensitivity error code
    *
    *
    */
   protected void checkSensitivityErrorCode( String aTestName, String aValidationCode ) {

      boolean lFound = false;
      String lerror_desc = null;
      String lerrorMsg = null;

      String lquery = TestConstants.BL_SENSITIVITY_ERROR_CHECK;

      String[] iIds = { "RESULT_CD", "USER_DESC" };
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
    * This function is to retrieve AssmblBomID from EQP_ASSMBL_BOM table .
    *
    *
    */
   @Override
   public AssmblBomID getAssmblBomId( String aAssmblCD, String aASSMBL_BOM_CD ) {
      String[] iIds = { "ASSMBL_DB_ID", "ASSMBL_CD", "ASSMBL_BOM_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "ASSMBL_BOM_CD", aASSMBL_BOM_CD );
      lArgs.addArguments( "ASSMBL_CD", aAssmblCD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_ASSMBL_BOM, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      AssmblBomID lIds = new AssmblBomID( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ),
            llists.get( 0 ).get( 2 ) );

      return lIds;

   }


   /**
    * This function is to retrieve bomPartPN from EQP_BOM_PART table .
    *
    *
    */
   public bomPartPN getPartGroup( String aAssmbl_CD, String aBOM_PART_CD ) {
      String[] iIds = { "BOM_PART_DB_ID", "BOM_PART_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "ASSMBL_CD", aAssmbl_CD );
      lArgs.addArguments( "BOM_PART_CD", aBOM_PART_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_BOM_PART, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      bomPartPN lIds =
            new bomPartPN( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ), null, null );

      return lIds;

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
                        "BEGIN  bl_sensitivity_import.validate(aiv_exist_in_mx => 'STRICT', aib_allornone => true, aon_retcode =>?, aov_retmsg =>?); END;" );
               } else {
                  lPrepareCallKIT = getConnection().prepareCall(
                        "BEGIN  bl_sensitivity_import.validate(aiv_exist_in_mx => 'STRICT', aib_allornone => false, aon_retcode =>?, aov_retmsg =>?); END;" );

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
                        "BEGIN  bl_sensitivity_import.import(aiv_exist_in_mx => 'STRICT', aib_allornone => true, aon_retcode =>?, aov_retmsg =>?); END;" );
               } else {
                  lPrepareCallKIT = getConnection().prepareCall(
                        "BEGIN  bl_sensitivity_import.import(aiv_exist_in_mx => 'STRICT', aib_allornone => false, aon_retcode =>?, aov_retmsg =>?); END;" );

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
