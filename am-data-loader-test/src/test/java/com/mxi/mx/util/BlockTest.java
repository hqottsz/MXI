package com.mxi.mx.util;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;

import com.mxi.mx.core.maint.plan.datamodels.assmbleInfor;
import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;


/**
 * DOCUMENT_ME
 *
 */
public class BlockTest extends BaselineLoaderTest {

   public simpleIDs iTASK_IDs = null;
   public simpleIDs iTASK_DEFN_IDs = null;
   public simpleIDs iTASK_IDs_2 = null;
   public simpleIDs iTASK_DEFN_IDs_2 = null;
   public simpleIDs iTASK_IDs_3 = null;
   public simpleIDs iTASK_DEFN_IDs_3 = null;

   public ValidationAndImport ivalidationandimport;


   /**
    * This function is to retrieve assemble information from EQP_ASSMBL_BOM table.
    *
    *
    */

   public assmbleInfor getassmblInfor( String aASSMBL_BOM_CD ) {

      String[] iIds = { "ASSMBL_DB_ID", "ASSMBL_CD", "ASSMBL_BOM_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "ASSMBL_BOM_CD", aASSMBL_BOM_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_ASSMBL_BOM, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      assmbleInfor lIds = new assmbleInfor( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ),
            llists.get( 0 ).get( 2 ), null );

      return lIds;

   }


   /**
    * This function is to retrieve task IDs from task_task table.
    *
    *
    */
   public simpleIDs getTaskIds( String aTASK_CD, String aTASK_NAME ) {

      String[] iIds = { "TASK_DB_ID", "TASK_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_CD", aTASK_CD );
      lArgs.addArguments( "TASK_NAME", aTASK_NAME );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_TASK, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );
      return lIds;
   }


   /**
    * This function is to verify task_task table for 15067.
    *
    *
    */
   public simpleIDs verifyTaskTask( simpleIDs aTaskIds, String aTASK_CLASS_CD,
         assmbleInfor aassmbleInfor, String aTASK_SUBCLASS_CD, String aTASK_ORIGINATOR_CD,
         String aTaskCD, String aTaskName, String aTASK_LDESC, String aTASK_REF_SDESC,
         String aLAST_SCHED_DEAD_BOOL, String aSOFT_DEADLINE_BOOL, String aTASK_MUST_REMOVE_CD,
         String aON_CONDITION_BOOL ) {

      String[] iIds = { "TASK_DEFN_DB_ID", "TASK_DEFN_ID", "TASK_CLASS_CD", "ASSMBL_DB_ID",
            "ASSMBL_CD", "ASSMBL_BOM_ID", "TASK_SUBCLASS_CD", "TASK_ORIGINATOR_CD", "TASK_CD",
            "TASK_NAME", "TASK_LDESC", "TASK_REF_SDESC", "LAST_SCHED_DEAD_BOOL",
            "SOFT_DEADLINE_BOOL", "TASK_MUST_REMOVE_CD", "ON_CONDITION_BOOL" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_TASK, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      Assert.assertTrue( "TASK_CLASS_CD",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aTASK_CLASS_CD ) );
      if ( aassmbleInfor != null ) {
         Assert.assertTrue( "ASSMBL_DB_ID",
               llists.get( 0 ).get( 3 ).equalsIgnoreCase( aassmbleInfor.getASSMBL_DB_ID() ) );
         Assert.assertTrue( "ASSMBL_CD",
               llists.get( 0 ).get( 4 ).equalsIgnoreCase( aassmbleInfor.getASSMBL_CD() ) );
         Assert.assertTrue( "ASSMBL_BOM_ID",
               llists.get( 0 ).get( 5 ).equalsIgnoreCase( aassmbleInfor.getASSMBL_BOM_ID() ) );
      }
      if ( aTASK_SUBCLASS_CD != null ) {
         Assert.assertTrue( "TASK_SUBCLASS_CD",
               llists.get( 0 ).get( 6 ).equalsIgnoreCase( aTASK_SUBCLASS_CD ) );
      }
      Assert.assertTrue( "TASK_ORIGINATOR_CD",
            llists.get( 0 ).get( 7 ).equalsIgnoreCase( aTASK_ORIGINATOR_CD ) );
      Assert.assertTrue( "TASK_CD", llists.get( 0 ).get( 8 ).equalsIgnoreCase( aTaskCD ) );
      Assert.assertTrue( "TASK_NAME", llists.get( 0 ).get( 9 ).equalsIgnoreCase( aTaskName ) );
      Assert.assertTrue( "TASK_LDESC", llists.get( 0 ).get( 10 ).equalsIgnoreCase( aTASK_LDESC ) );
      if ( aTASK_REF_SDESC != null ) {
         Assert.assertTrue( "TASK_REF_SDESC",
               llists.get( 0 ).get( 11 ).equalsIgnoreCase( aTASK_REF_SDESC ) );
      }
      Assert.assertTrue( "LAST_SCHED_DEAD_BOOL",
            llists.get( 0 ).get( 12 ).equalsIgnoreCase( aLAST_SCHED_DEAD_BOOL ) );
      Assert.assertTrue( "SOFT_DEADLINE_BOOL",
            llists.get( 0 ).get( 13 ).equalsIgnoreCase( aSOFT_DEADLINE_BOOL ) );

      if ( aTASK_MUST_REMOVE_CD != null ) {
         Assert.assertTrue( "TASK_MUST_REMOVE_CD",
               llists.get( 0 ).get( 14 ).equalsIgnoreCase( aTASK_MUST_REMOVE_CD ) );

      }

      if ( aON_CONDITION_BOOL != null ) {
         Assert.assertTrue( "ON_CONDITION_BOOL",
               llists.get( 0 ).get( 15 ).equalsIgnoreCase( aON_CONDITION_BOOL ) );

      }

      return lIds;
   }


   /**
    * This function is to verify task_task table.
    *
    *
    */
   public simpleIDs verifyTaskTask( simpleIDs aTaskIds, String aTASK_CLASS_CD,
         assmbleInfor aassmbleInfor, String aTASK_SUBCLASS_CD, String aTASK_ORIGINATOR_CD,
         String aTaskCD, String aTaskName, String aTASK_LDESC, String aTASK_REF_SDESC,
         String aLAST_SCHED_DEAD_BOOL, String aSOFT_DEADLINE_BOOL, String aTASK_MUST_REMOVE_CD,
         String aON_CONDITION_BOOL, String aBLOCK_CHAIN_SDESC ) {

      String[] iIds = { "TASK_DEFN_DB_ID", "TASK_DEFN_ID", "TASK_CLASS_CD", "ASSMBL_DB_ID",
            "ASSMBL_CD", "ASSMBL_BOM_ID", "TASK_SUBCLASS_CD", "TASK_ORIGINATOR_CD", "TASK_CD",
            "TASK_NAME", "TASK_LDESC", "TASK_REF_SDESC", "LAST_SCHED_DEAD_BOOL",
            "SOFT_DEADLINE_BOOL", "TASK_MUST_REMOVE_CD", "ON_CONDITION_BOOL", "BLOCK_CHAIN_SDESC" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_TASK, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      Assert.assertTrue( "TASK_CLASS_CD",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aTASK_CLASS_CD ) );
      if ( aassmbleInfor != null ) {
         Assert.assertTrue( "ASSMBL_DB_ID",
               llists.get( 0 ).get( 3 ).equalsIgnoreCase( aassmbleInfor.getASSMBL_DB_ID() ) );
         Assert.assertTrue( "ASSMBL_CD",
               llists.get( 0 ).get( 4 ).equalsIgnoreCase( aassmbleInfor.getASSMBL_CD() ) );
         Assert.assertTrue( "ASSMBL_BOM_ID",
               llists.get( 0 ).get( 5 ).equalsIgnoreCase( aassmbleInfor.getASSMBL_BOM_ID() ) );
      }
      if ( aTASK_SUBCLASS_CD != null ) {
         Assert.assertTrue( "TASK_SUBCLASS_CD",
               llists.get( 0 ).get( 6 ).equalsIgnoreCase( aTASK_SUBCLASS_CD ) );
      }
      Assert.assertTrue( "TASK_ORIGINATOR_CD",
            llists.get( 0 ).get( 7 ).equalsIgnoreCase( aTASK_ORIGINATOR_CD ) );
      Assert.assertTrue( "TASK_CD", llists.get( 0 ).get( 8 ).equalsIgnoreCase( aTaskCD ) );
      Assert.assertTrue( "TASK_NAME", llists.get( 0 ).get( 9 ).equalsIgnoreCase( aTaskName ) );
      Assert.assertTrue( "TASK_LDESC", llists.get( 0 ).get( 10 ).equalsIgnoreCase( aTASK_LDESC ) );
      if ( aTASK_REF_SDESC != null ) {
         Assert.assertTrue( "TASK_REF_SDESC",
               llists.get( 0 ).get( 11 ).equalsIgnoreCase( aTASK_REF_SDESC ) );
      }
      Assert.assertTrue( "LAST_SCHED_DEAD_BOOL",
            llists.get( 0 ).get( 12 ).equalsIgnoreCase( aLAST_SCHED_DEAD_BOOL ) );
      Assert.assertTrue( "SOFT_DEADLINE_BOOL",
            llists.get( 0 ).get( 13 ).equalsIgnoreCase( aSOFT_DEADLINE_BOOL ) );

      if ( aTASK_MUST_REMOVE_CD != null ) {
         Assert.assertTrue( "TASK_MUST_REMOVE_CD",
               llists.get( 0 ).get( 14 ).equalsIgnoreCase( aTASK_MUST_REMOVE_CD ) );

      }

      if ( aON_CONDITION_BOOL != null ) {
         Assert.assertTrue( "ON_CONDITION_BOOL",
               llists.get( 0 ).get( 15 ).equalsIgnoreCase( aON_CONDITION_BOOL ) );

      }

      if ( aBLOCK_CHAIN_SDESC != null ) {
         Assert.assertTrue( "BLOCK_CHAIN_SDESC",
               llists.get( 0 ).get( 16 ).equalsIgnoreCase( aBLOCK_CHAIN_SDESC ) );

      }

      return lIds;
   }


   /**
    * This function is to verify task_sched_rule table for 15067.
    *
    *
    */

   public void verifyTASKSCHEDRULE( simpleIDs aTaskIds, simpleIDs aDataTypeIds,
         String aDEF_INTERVAL_QT, String aDEF_INITIAL_QT ) {
      String[] iIds = { "DATA_TYPE_DB_ID", "DATA_TYPE_ID", "DEF_INTERVAL_QT", "DEF_INITIAL_QT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_SCHED_RULE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "DATA_TYPE_DB_ID",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aDataTypeIds.getNO_DB_ID() ) );
      Assert.assertTrue( "DATA_TYPE_ID",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aDataTypeIds.getNO_ID() ) );
      Assert.assertTrue( "DEF_INTERVAL_QT",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aDEF_INTERVAL_QT ) );
      if ( aDEF_INITIAL_QT != null ) {
         Assert.assertTrue( "DEF_INITIAL_QT",
               llists.get( 0 ).get( 3 ).equalsIgnoreCase( aDEF_INITIAL_QT ) );

      }

   }


   /**
    * This function is to verify task_task_dep table.
    *
    *
    */

   public void verifyTASKTASkDEP( simpleIDs aTaskIds, simpleIDs aDEPIds,
         String aTASK_DEP_ACTION_CD ) {
      String[] iIds = { "DEP_TASK_DEFN_DB_ID", "DEP_TASK_DEFN_ID", "TASK_DEP_ACTION_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_TASK_DEP, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "DEP_TASK_DEFN_DB_ID",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aDEPIds.getNO_DB_ID() ) );
      Assert.assertTrue( "DEP_TASK_DEFN_ID",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aDEPIds.getNO_ID() ) );
      Assert.assertTrue( "TASK_DEP_ACTION_CD",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aTASK_DEP_ACTION_CD ) );

   }


   /**
    * This function is to retrieve part no ids.
    *
    *
    */

   public simpleIDs getPartIds( String aPART_NO_OEM, String aMANUFACT_CD ) {

      String[] iIds = { "PART_NO_DB_ID", "PART_NO_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "PART_NO_OEM", aPART_NO_OEM );
      lArgs.addArguments( "MANUFACT_CD", aMANUFACT_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_PART_NO, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );
      return lIds;

   }


   /**
    * This function is to verify task_sched_rule table for 15067.
    *
    *
    */

   public void verifyTASKINTERVAL( simpleIDs aTaskIds, simpleIDs aDataTypeIds, simpleIDs aPartIds,
         String aDEF_INTERVAL_QT, String aDEF_INITIAL_QT ) {

      String[] iIds = { "DATA_TYPE_DB_ID", "DATA_TYPE_ID", "PART_NO_DB_ID", "PART_NO_ID",
            "INTERVAL_QT", "INITIAL_QT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_INTERVAL, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "DATA_TYPE_DB_ID",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aDataTypeIds.getNO_DB_ID() ) );
      Assert.assertTrue( "DATA_TYPE_ID",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aDataTypeIds.getNO_ID() ) );
      Assert.assertTrue( "PART_NO_DB_ID",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aPartIds.getNO_DB_ID() ) );
      Assert.assertTrue( "PART_NO_ID",
            llists.get( 0 ).get( 3 ).equalsIgnoreCase( aPartIds.getNO_ID() ) );
      Assert.assertTrue( "INTERVAL_QT",
            llists.get( 0 ).get( 4 ).equalsIgnoreCase( aDEF_INTERVAL_QT ) );
      if ( aDEF_INITIAL_QT != null ) {
         Assert.assertTrue( "INITIAL_QT",
               llists.get( 0 ).get( 5 ).equalsIgnoreCase( aDEF_INITIAL_QT ) );

      }

   }


   /**
    * This function is to retrieve data type IDs from MIM_DATA_TYPE table.
    *
    *
    */

   public simpleIDs getTypeIds( String aDATA_TYPE_CD ) {
      String[] iIds = { "DATA_TYPE_DB_ID", "DATA_TYPE_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "DATA_TYPE_CD", aDATA_TYPE_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.MIM_DATA_TYPE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );
      return lIds;

   }


   /**
    * Get first record value from query
    *
    * @return string
    */

   public simpleIDs getREQDEFNIDs( String strQuery ) {

      ResultSet ResultSetRecords;
      simpleIDs lReturn = null;
      try {
         ResultSetRecords = runQuery( strQuery );
         if ( ResultSetRecords.next() ) {
            lReturn = new simpleIDs( ResultSetRecords.getString( "TASK_DEFN_DB_ID" ),
                  ResultSetRecords.getString( "TASK_DEFN_ID" ) );
         }
      } catch ( SQLException e ) {
         e.printStackTrace();
         Assert.assertTrue( "getREQDEFNIDs", false );

      }
      return lReturn;
   }


   /**
    * This function is to verify task_block_req_map table.
    *
    *
    */

   public void verifyTASKBLOCKREQMAP( simpleIDs aTaskIds, simpleIDs aReqTaskDefnIDs,
         String aSTART_BLOCK_ORD, String aINTERVAL_QT ) {
      String[] iIds = { "START_BLOCK_ORD", "INTERVAL_QT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "BLOCK_TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "BLOCK_TASK_ID", aTaskIds.getNO_ID() );
      lArgs.addArguments( "REQ_TASK_DEFN_DB_ID", aReqTaskDefnIDs.getNO_DB_ID() );
      lArgs.addArguments( "REQ_TASK_DEFN_ID", aReqTaskDefnIDs.getNO_ID() );

      String iQueryString =
            TableUtil.buildTableQuery( TableUtil.TASK_BLOCK_REQ_MAP, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "START_BLOCK_ORD",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aSTART_BLOCK_ORD ) );
      if ( aINTERVAL_QT != null ) {
         Assert.assertTrue( "INTERVAL_QT",
               llists.get( 0 ).get( 1 ).equalsIgnoreCase( aINTERVAL_QT ) );

      }

   }


   /**
    * Calls check block error code
    *
    *
    */
   protected void checkBLOCKErrorCode( String aTestName, String aValidationCode ) {

      boolean lFound = false;
      String lerror_desc = null;
      String lerrorMsg = null;

      String lquery = TestConstants.BL_BLOCK_ERROR_CHECK;

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
      ivalidationandimport = new ValidationAndImport() {

         @Override
         public int runValidation( boolean allornone ) {
            int lReturn = 0;
            CallableStatement lPrepareBlockValidateCall;

            try {

               lPrepareBlockValidateCall = getConnection()
                     .prepareCall( "BEGIN   block_import.validate_block(on_retcode =>?); END;" );

               lPrepareBlockValidateCall.registerOutParameter( 1, Types.INTEGER );
               lPrepareBlockValidateCall.execute();
               commit();
               lReturn = lPrepareBlockValidateCall.getInt( 1 );
            } catch ( SQLException e ) {
               e.printStackTrace();
            }
            return lReturn;

         }


         @Override
         public int runImport( boolean allornone ) {
            int lReturn = 0;
            CallableStatement lPrepareBlockImportCall;

            try {

               lPrepareBlockImportCall = getConnection()
                     .prepareCall( "BEGIN block_import.import_block(on_retcode =>?); END;" );

               lPrepareBlockImportCall.registerOutParameter( 1, Types.INTEGER );

               lPrepareBlockImportCall.execute();
               commit();
               lReturn = lPrepareBlockImportCall.getInt( 1 );
            } catch ( SQLException e ) {
               e.printStackTrace();
            }
            return lReturn;
         }

      };

      lrtValue = ablnOnlyValidation ? ivalidationandimport.runValidation( allornone )
            : ivalidationandimport.runImport( allornone );

      return lrtValue;
   }


   /**
    * This function is to implement interface ValidationAndImport
    *
    * @param: blnOnlyValidation
    * @param: allornone
    *
    * @return: return code of Int
    * @throws SQLException
    *
    */
   public int runExport() throws SQLException {

      int lReturn = 0;
      CallableStatement lPrepareBlockValidateCall;

      lPrepareBlockValidateCall =
            getConnection().prepareCall( "BEGIN  block_import.export_block(on_retcode =>?); END;" );

      lPrepareBlockValidateCall.registerOutParameter( 1, Types.INTEGER );
      lPrepareBlockValidateCall.execute();
      commit();
      lReturn = lPrepareBlockValidateCall.getInt( 1 );

      return lReturn;

   }

}
