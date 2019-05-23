package com.mxi.mx.core.maint.plan.baselineloader.ComHW;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.BaselineLoaderTest;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.TestConstants;
import com.mxi.mx.util.ValidationAndImport;
import com.mxi.mx.util.WhereClause;


/**
 * This class contains common functions which support validation and import functionality of
 * comhw_import package.
 *
 * @author ALICIA QIAN
 */
public class ComHW extends BaselineLoaderTest {

   ValidationAndImport ivalidationandimport;

   public String iPART_GROUP_CD_1 = "ATCHWGP1";
   public String iPART_GROUP_NAME_1 = "ATCHWGP1NAME";
   public String iPART_GROUP_CD_2 = "ATCHWGP2";
   public String iPART_GROUP_NAME_2 = "ATCHWGP2NAME";
   public String iPART_NO_OEM_1 = "AUTOCHP001";
   public String iPART_NO_OEM_2 = "AUTOCHP002";
   public String iMANUFACT_CD_1 = "1234567890";
   public String iMANUFACT_CD_2 = "11111";
   public String iPART_NO_SDESC = "AUTOTEST";
   public String iINV_CLASS_CD_TRK = "TRK";
   public String iINV_CLASS_CD_SER = "SER";
   public String iINV_CLASS_CD_BATCH = "BATCH";
   public String iINV_CLASS_CD_KIT = "KIT";
   public String iPART_BASELINE_CD = "AUTOTEST";
   public String iAPPL_EFF_LDESC = "001-200";


   /**
    * This function is to verify EQP_PART_BASELINE by given parameters .
    *
    *
    */
   public void verifyEQPPARTBASELINE( simpleIDs aBomPartIds, simpleIDs aPartNoIds,
         String PART_BASELINE_CD, String aSTANDARD_BOOL, String aINTERCHG_ORD,
         String iAPPL_EFF_LDESC ) {

      String[] iIds = { "STANDARD_BOOL", "INTERCHG_ORD", "APPL_EFF_LDESC" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "BOM_PART_DB_ID", aBomPartIds.getNO_DB_ID() );
      lArgs.addArguments( "BOM_PART_ID", aBomPartIds.getNO_ID() );
      lArgs.addArguments( "PART_NO_DB_ID", aPartNoIds.getNO_DB_ID() );
      lArgs.addArguments( "PART_NO_ID", aPartNoIds.getNO_ID() );

      String iQueryString =
            TableUtil.buildTableQuery( TableUtil.EQP_PART_BASELINE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "STANDARD_BOOL",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aSTANDARD_BOOL ) );
      Assert.assertTrue( "INTERCHG_ORD",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aINTERCHG_ORD ) );
      Assert.assertTrue( "APPL_EFF_LDESC",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( iAPPL_EFF_LDESC ) );

   }


   /**
    * This function is to verify EQP_PART_NO by given parameters .
    *
    *
    */
   public simpleIDs verifyEQPPARTNO( String aPART_NO_SDESC, String aPART_NO_OEM,
         String aINV_CLASS_CD, String aMANUFACT_CD ) {

      String[] iIds = { "PART_NO_DB_ID", "PART_NO_ID", "INV_CLASS_CD", "MANUFACT_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "PART_NO_SDESC", aPART_NO_SDESC );
      lArgs.addArguments( "PART_NO_OEM", aPART_NO_OEM );
      lArgs.addArguments( "INV_CLASS_CD", aINV_CLASS_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_PART_NO, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      Assert.assertTrue( "INV_CLASS_CD",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aINV_CLASS_CD ) );
      Assert.assertTrue( "MANUFACT_CD", llists.get( 0 ).get( 3 ).equalsIgnoreCase( aMANUFACT_CD ) );

      return lIds;

   }


   /**
    * This function is to verify EQP_BOM_PART by given parameters .
    *
    *
    */
   public simpleIDs verifyEQPBOMPART( String aBOM_PART_CD, String aBOM_PART_NAME, String aAssmbl_CD,
         String aINV_CLASS_CD ) {

      String[] iIds = { "BOM_PART_DB_ID", "BOM_PART_ID", "ASSMBL_CD", "INV_CLASS_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "BOM_PART_CD", aBOM_PART_CD );
      lArgs.addArguments( "BOM_PART_NAME", aBOM_PART_NAME );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_BOM_PART, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      Assert.assertTrue( "ASSMBL_CD", llists.get( 0 ).get( 2 ).equalsIgnoreCase( aAssmbl_CD ) );
      Assert.assertTrue( "INV_CLASS_CD",
            llists.get( 0 ).get( 3 ).equalsIgnoreCase( aINV_CLASS_CD ) );

      return lIds;

   }


   /**
    * This function is to provide staging table data for some test cases.
    *
    *
    */

   public void prepareData( String aINV_CLASS_CD ) {
      // c_comhw_def table
      Map<String, String> lComHW = new LinkedHashMap<>();
      // First record
      lComHW.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_1 + "\'" );
      lComHW.put( "PART_GROUP_NAME", "\'" + iPART_GROUP_NAME_1 + "\'" );
      lComHW.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_1 + "\'" );
      lComHW.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lComHW.put( "PART_NO_SDESC", "\'" + iPART_NO_SDESC + "\'" );
      lComHW.put( "INV_CLASS_CD", "\'" + aINV_CLASS_CD + "\'" );
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
      lComHW.put( "INV_CLASS_CD", "\'" + aINV_CLASS_CD + "\'" );
      lComHW.put( "INTERCHG_ORD", "'1'" );
      lComHW.put( "PART_BASELINE_CD", "\'" + iPART_BASELINE_CD + "\'" );
      lComHW.put( "ALT_PRIMARY_PN", "\'" + iPART_NO_OEM_1 + "\'" );
      lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lComHW.put( "APPL_EFF_LDESC", "\'" + iAPPL_EFF_LDESC + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMHW_DEF, lComHW ) );

   }


   /**
    * This function is to provide staging table data for some test cases.
    *
    *
    */

   public void prepareData_2( String aINV_CLASS_CD ) {

      // c_comhw_def table
      Map<String, String> lComHW = new LinkedHashMap<>();
      // First record
      lComHW.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_2 + "\'" );
      lComHW.put( "PART_GROUP_NAME", "\'" + iPART_GROUP_NAME_2 + "\'" );
      lComHW.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_2 + "\'" );
      lComHW.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "PART_NO_SDESC", "\'" + iPART_NO_SDESC + "\'" );
      lComHW.put( "INV_CLASS_CD", "\'" + aINV_CLASS_CD + "\'" );
      lComHW.put( "INTERCHG_ORD", "'1'" );
      lComHW.put( "PART_BASELINE_CD", "\'" + iPART_BASELINE_CD + "\'" );
      lComHW.put( "ALT_PRIMARY_PN", "\'" + iPART_NO_OEM_2 + "\'" );
      lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lComHW.put( "APPL_EFF_LDESC", "\'" + iAPPL_EFF_LDESC + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMHW_DEF, lComHW ) );

   }


   /**
    * This function is to provide staging table data for some test cases.
    *
    *
    */

   public void prepareData_3( String aINV_CLASS_CD ) {
      // c_comhw_def table
      Map<String, String> lComHW = new LinkedHashMap<>();
      // First record
      lComHW.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_1 + "\'" );
      lComHW.put( "PART_GROUP_NAME", "\'" + iPART_GROUP_NAME_1 + "\'" );
      lComHW.put( "PART_NO_OEM", "'CHW000001'" );
      lComHW.put( "MANUFACT_CD", "'11111'" );
      lComHW.put( "PART_NO_SDESC", "'Common Hardware Part 1'" );
      lComHW.put( "INV_CLASS_CD", "\'" + aINV_CLASS_CD + "\'" );
      lComHW.put( "INTERCHG_ORD", "'1'" );
      lComHW.put( "PART_BASELINE_CD", "\'" + iPART_BASELINE_CD + "\'" );
      lComHW.put( "ALT_PRIMARY_PN", "'CHW000001'" );
      lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "'11111'" );
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
      lComHW.put( "INV_CLASS_CD", "\'" + aINV_CLASS_CD + "\'" );
      lComHW.put( "INTERCHG_ORD", "'1'" );
      lComHW.put( "PART_BASELINE_CD", "\'" + iPART_BASELINE_CD + "\'" );
      lComHW.put( "ALT_PRIMARY_PN", "'CHW000001'" );
      lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "'11111'" );
      lComHW.put( "APPL_EFF_LDESC", "\'" + iAPPL_EFF_LDESC + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMHW_DEF, lComHW ) );

   }


   /**
    * This function is to provide staging table data for some test cases.
    *
    *
    */

   public void prepareData_4( String aINV_CLASS_CD ) {
      // c_comhw_def table
      Map<String, String> lComHW = new LinkedHashMap<>();
      // First record
      lComHW.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_1 + "\'" );
      lComHW.put( "PART_GROUP_NAME", "\'" + iPART_GROUP_NAME_1 + "\'" );
      lComHW.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_1 + "\'" );
      lComHW.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lComHW.put( "PART_NO_SDESC", "\'" + iPART_NO_SDESC + "\'" );
      lComHW.put( "INV_CLASS_CD", "\'" + aINV_CLASS_CD + "\'" );
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
      lComHW.put( "PART_NO_SDESC", "'Common Hardware Part 1'" );
      lComHW.put( "INV_CLASS_CD", "\'" + aINV_CLASS_CD + "\'" );
      lComHW.put( "INTERCHG_ORD", "'1'" );
      lComHW.put( "PART_BASELINE_CD", "\'" + iPART_BASELINE_CD + "\'" );
      lComHW.put( "ALT_PRIMARY_PN", "\'" + iPART_NO_OEM_1 + "\'" );
      lComHW.put( "ALT_PRIMARY_MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lComHW.put( "APPL_EFF_LDESC", "\'" + iAPPL_EFF_LDESC + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMHW_DEF, lComHW ) );

   }


   /**
    * Calls check comm HW error code
    *
    *
    */
   protected void checkCOMMHWErrorCode( String aTestName, String aValidationCode ) {

      String lquery = TestConstants.BL_COMHW_ERROR_CHECK;

      checkErrorCode( lquery, aTestName, aValidationCode );

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
                        .prepareCall( "BEGIN  comhw_import.validate_comhw(on_retcode =>?); END;" );
               } else {
                  lPrepareCallKIT = getConnection()
                        .prepareCall( "BEGIN  comhw_import.validate_comhw(on_retcode =>?); END;" );

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
                        .prepareCall( "BEGIN  comhw_import.import_comhw(on_retcode =>?); END;" );
               } else {
                  lPrepareCallKIT = getConnection()
                        .prepareCall( "BEGIN  comhw_import.import_comhw(on_retcode =>?); END;" );

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
