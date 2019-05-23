package com.mxi.mx.core.maint.plan.baselineloader;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.AbstractDatabaseConnection;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.WhereClause;


/**
 * All these methods will assist with PartIncompatibility Tests
 *
 */
public class PartIncompatibilityTest extends AbstractDatabaseConnection {

   simpleIDs iIncompatPartIds;
   simpleIDs iPartIds;


   /**
    * This will delete all Imported Data
    *
    */
   protected void removeImportedData() {
      if ( iPartIds != null ) {
         String lQuery = "Delete from EQP_PART_COMPAT_DEF where part_no_db_id = "
               + iPartIds.getNO_DB_ID() + "AND part_no_id = " + iPartIds.getNO_ID();
         runUpdate( lQuery );
      } else if ( iIncompatPartIds != null ) {
         String lQuery = "Delete from EQP_PART_COMPAT_DEF where part_no_db_id = "
               + iIncompatPartIds.getNO_DB_ID() + "AND part_no_id = " + iIncompatPartIds.getNO_ID();
         runUpdate( lQuery );
      }
   }


   /**
    *
    * Provide the Query to search for Part_No_Oem
    *
    * @param aPart
    *           Part_no_oem Inputed into the staging table
    * @return SQL Query
    */
   private String PartNoQuery( String aPart ) {
      return "SELECT part_no_db_id, part_no_id FROM eqp_part_no WHERE part_no_oem = " + aPart;
   }


   /**
    *
    * Provide the Query to search for Part_No_Oem
    *
    * @param aPart
    *           Part_no_oem Inputed into the staging table
    * @return SQL Query
    */
   private String PartNoQuery2( String aPart, String aMANUFACT_CD ) {
      return "SELECT part_no_db_id, part_no_id FROM eqp_part_no WHERE part_no_oem = " + aPart
            + " and MANUFACT_CD='" + aMANUFACT_CD + "'";
   }


   /**
    *
    * Provide the Query to search for BOM_Part_Cd
    *
    * @param aPart
    *           Bom_Part_Cd inputed the staging table
    * @return SQL Query
    */
   private String BomPartQuery( String aBomPart ) {
      return "SELECT BOM_PART_DB_ID, BOM_PART_ID FROM eqp_bom_part WHERE bom_part_cd = " + aBomPart;
   }


   /**
    *
    * Verify data from staging table gets into the mx table, note it includes data from staging
    * table, but also in some cases DL Tool has look-up the other data in Mx tables. That is why all
    * four fields will always be populated.
    *
    * @param aPart
    * @param aIncompatPart
    * @param aBomPart
    * @param aIncompatBomPart
    */
   protected void verifyData( String aPart, String aIncompatPart, String aBomPart,
         String aIncompatBomPart ) {
      Map<String, String> lMapMxTable = new LinkedHashMap<String, String>();

      String lQuery = PartNoQuery( aPart );
      iPartIds = getIDs( lQuery, "PART_NO_DB_ID", "PART_NO_ID" );
      lMapMxTable.put( "PART_NO_DB_ID", iPartIds.getNO_DB_ID() );
      lMapMxTable.put( "PART_NO_ID", iPartIds.getNO_ID() );

      lQuery = PartNoQuery2( aIncompatPart, "ABC11" );
      iIncompatPartIds = getIDs( lQuery, "PART_NO_DB_ID", "PART_NO_ID" );
      lMapMxTable.put( "NH_PART_NO_DB_ID", iIncompatPartIds.getNO_DB_ID() );
      lMapMxTable.put( "NH_PART_NO_ID", iIncompatPartIds.getNO_ID() );

      lQuery = BomPartQuery( aBomPart );
      simpleIDs lBomPartIds = getIDs( lQuery, "BOM_PART_DB_ID", "BOM_PART_ID" );
      lMapMxTable.put( "BOM_PART_DB_ID", lBomPartIds.getNO_DB_ID() );
      lMapMxTable.put( "BOM_PART_ID", lBomPartIds.getNO_ID() );

      lQuery = BomPartQuery( aIncompatBomPart );
      simpleIDs iIncompatPartIds = getIDs( lQuery, "BOM_PART_DB_ID", "BOM_PART_ID" );
      lMapMxTable.put( "NH_BOM_PART_DB_ID", iIncompatPartIds.getNO_DB_ID() );
      lMapMxTable.put( "NH_BOM_PART_ID", iIncompatPartIds.getNO_ID() );

      Assert.assertTrue( "Verify values in EQP_PART_COMPAT_DEF: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.EQP_PART_COMPAT_DEF, lMapMxTable ) ) );
   }


   /**
    *
    * Verify data from staging table gets into the mx table, note it includes data from staging
    * table, but also in some cases DL Tool has look-up the other data in Mx tables. That is why all
    * four fields will always be populated.
    *
    * @param aPart
    * @param aIncompatPart
    * @param aBomPart
    * @param aIncompatBomPart
    */
   protected void verifyData2( String aPart, String aIncompatPart, String aBomPart,
         String aIncompatBomPart ) {
      Map<String, String> lMapMxTable = new LinkedHashMap<String, String>();

      String lQuery = PartNoQuery( aPart );
      iPartIds = getIDs( lQuery, "PART_NO_DB_ID", "PART_NO_ID" );
      lMapMxTable.put( "PART_NO_DB_ID", iPartIds.getNO_DB_ID() );
      lMapMxTable.put( "PART_NO_ID", iPartIds.getNO_ID() );

      lQuery = PartNoQuery( aIncompatPart );
      iIncompatPartIds = getIDs( lQuery, "PART_NO_DB_ID", "PART_NO_ID" );
      lMapMxTable.put( "NH_PART_NO_DB_ID", iIncompatPartIds.getNO_DB_ID() );
      lMapMxTable.put( "NH_PART_NO_ID", iIncompatPartIds.getNO_ID() );

      lQuery = BomPartQuery( aBomPart );
      simpleIDs lBomPartIds = getIDs( lQuery, "BOM_PART_DB_ID", "BOM_PART_ID" );
      lMapMxTable.put( "BOM_PART_DB_ID", lBomPartIds.getNO_DB_ID() );
      lMapMxTable.put( "BOM_PART_ID", lBomPartIds.getNO_ID() );

      lQuery = BomPartQuery( aIncompatBomPart );
      simpleIDs iIncompatPartIds = getIDs( lQuery, "BOM_PART_DB_ID", "BOM_PART_ID" );
      lMapMxTable.put( "NH_BOM_PART_DB_ID", iIncompatPartIds.getNO_DB_ID() );
      lMapMxTable.put( "NH_BOM_PART_ID", iIncompatPartIds.getNO_ID() );

      Assert.assertTrue( "Verify values in EQP_PART_COMPAT_DEF: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.EQP_PART_COMPAT_DEF, lMapMxTable ) ) );
   }


   /**
    *
    * This method runs the query, and returns the ids.
    *
    * @param aQuery
    *           - executes query
    * @param aDbId
    *           - column name of the DB ID
    * @param aId
    *           - column name of the ID
    * @return - Returns the Ids based on the simpleIDs class
    */
   @Override
   public simpleIDs getIDs( String aQuery, String aDbId, String aId ) {

      ResultSet lResultSet = null;
      String lDbId = null;
      String lId = null;

      try {
         PreparedStatement lStatement = getConnection().prepareStatement( aQuery,
               ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY );

         lResultSet = lStatement.executeQuery( aQuery );
         lResultSet.next();
         lDbId = lResultSet.getString( aDbId );
         lId = lResultSet.getString( aId );
         if ( !lResultSet.isLast() )
            throw new IllegalArgumentException( "This query returns more than one row: " + aQuery );
      } catch ( SQLException e ) {
         e.printStackTrace();
      }

      return new simpleIDs( lDbId, lId );
   }


   /**
    * Calls checkTask error code
    *
    * @param aTestName
    *           the test method name
    * @param aValidationCode
    *           Expected validation result
    *
    * @throws Exception
    *            if there is an error
    */
   protected void checkTaskErrorCode( String aTestName, String aValidationCode ) {

      boolean lFound = false;
      String lquery = "Select RESULT_CD from bl_proc_result";

      String[] iIds = { "RESULT_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      List<ArrayList<String>> lresult = execute( lquery, lfields );

      for ( int i = 0; i < lresult.size(); i++ ) {
         if ( lresult.get( i ).get( 0 ).equalsIgnoreCase( aValidationCode ) ) {
            lFound = true;
            break;
         }

      }

      String lerror_desc = getTaskErrorDetail( aValidationCode );;
      Assert.assertTrue( "The error code found- " + aValidationCode + " : " + lerror_desc, lFound );

   }


   /**
    * This function is to get detail of error code
    *
    *
    */

   public String getTaskErrorDetail( String aErrorcode ) {

      String[] iIds = { "USER_DESC" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();

      lArgs.addArguments( "RESULT_CD", aErrorcode );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.DL_REF_MESSAGE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      return llists.get( 0 ).get( 0 );

   }


   protected int runValidationBL_PG_PART_INCOMPAT( boolean allornone ) {
      int lReturn = 0;
      CallableStatement lPrepareCallKIT;

      try {
         if ( allornone ) {
            lPrepareCallKIT = getConnection().prepareCall(
                  "BEGIN  bl_part_incompat_import.validate(aiv_exist_in_mx => 'STRICT', aib_allornone => true, aon_retcode =>?, aov_retmsg =>?); END;" );
         } else {
            lPrepareCallKIT = getConnection().prepareCall(
                  "BEGIN  bl_part_incompat_import.validate(aiv_exist_in_mx => 'STRICT',aib_allornone => false, aon_retcode =>?, aov_retmsg =>?); END;" );
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


   /**
    *
    * Execute the import stored procedure within the database.
    *
    */
   protected int runImportBL_PART_INCOMPAT( boolean allornone ) {
      int lResult = 0;
      CallableStatement lPrepareCallKIT;

      try {
         if ( allornone ) {
            lPrepareCallKIT = getConnection().prepareCall(
                  "BEGIN  bl_part_incompat_import.import(aiv_exist_in_mx => 'STRICT', aib_allornone => true, aon_retcode =>?, aov_retmsg =>?); END;" );
         } else {
            lPrepareCallKIT = getConnection().prepareCall(
                  "BEGIN  bl_part_incompat_import.import(aiv_exist_in_mx => 'STRICT',aib_allornone => false, aon_retcode =>?, aov_retmsg =>?); END;" );
         }

         lPrepareCallKIT.registerOutParameter( 1, Types.INTEGER );
         lPrepareCallKIT.registerOutParameter( 2, Types.VARCHAR );
         lPrepareCallKIT.execute();
         commit();
         lResult = lPrepareCallKIT.getInt( 1 );
      } catch ( SQLException e ) {
         e.printStackTrace();
      }

      return lResult;
   }
}
