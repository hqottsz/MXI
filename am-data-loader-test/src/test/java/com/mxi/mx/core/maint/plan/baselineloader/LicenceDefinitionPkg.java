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
import java.util.Map.Entry;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.util.AbstractDatabaseConnection;
import com.mxi.mx.util.TableUtil;


/**
 * This class will execute all the test Definition for Licence Definition package for Baseline
 * loader
 *
 * @author gehyca
 *
 */
public class LicenceDefinitionPkg extends AbstractDatabaseConnection {

   @Rule
   public TestName testName = new TestName();

   private ArrayList<String> iClearBaselineTables = new ArrayList<String>();
   {
      iClearBaselineTables.add( "delete from C_LIC_DEFN" );
      iClearBaselineTables.add( "delete from C_LIC_PREREQ" );
   };


   @Override
   @Before
   public void before() throws Exception {
      super.before();
      if ( testName.getMethodName().contains( "Prerequisites" ) )
         DataSetupAndCleanUp( iLoadPreData );
   }


   private ArrayList<String> iLoadPreData = new ArrayList<String>();
   {
      // Loading reference codes
      iLoadPreData.add(
            "INSERT INTO REF_LIC_CLASS (LIC_CLASS_DB_ID,LIC_CLASS_CD,DESC_SDESC) VALUES ('4650','CLASS456','Test-456')" );
      iLoadPreData.add(
            "INSERT INTO REF_LIC_CATEGORY (LIC_CAT_DB_ID, LIC_CAT_CD,DESC_SDESC) VALUES ('4650','CAT-456','Test-456')" );
      iLoadPreData.add(
            "INSERT INTO REF_LIC_TYPE (LIC_TYPE_DB_ID,LIC_TYPE_CD,DESC_SDESC) VALUES ('4650','TYPE-456','Test-456')" );
   }


   @Override
   @After
   public void after() throws Exception {
      DataSetupAndCleanUp( iClearBaselineTables );
      cleanUpData();
      // clean up data from the pre-data that was loaded
      if ( testName.getMethodName().contains( "Prerequisites" ) )
         DataSetupAndCleanUp( iRestoreTables );
      super.after();
   }


   /**
    * This will remove all imported data from the staging table.
    *
    * @throws SQLException
    *
    */
   private void cleanUpData() throws SQLException {
      String[] iIds = { "LIC_DB_ID", "LIC_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      List<ArrayList<String>> lListLicIds = execute(
            "Select lic_db_id, lic_id from LIC_DEFN where LIC_SDESC in ('License1-456','License2-456','License3-456')",
            lfields );
      // clean up LIC_DEFN
      runUpdate(
            "delete from LIC_DEFN where LIC_SDESC in ('License1-456','License2-456','License3-456')" );

      if ( countRowsInEntireTable( "EVT_LIC_DEFN" ) > 0 ) {
         deleteRowsFromEvtLicDefnAndEvtEvent( lListLicIds );
      }

      if ( countRowsInEntireTable( "GRP_DEFN" ) > 0 )
         deleteRowsFromGrpDfnAndGrpDefnLic( lListLicIds );
   }


   private ArrayList<String> iRestoreTables = new ArrayList<String>();
   {
      iRestoreTables.add( "DELETE FROM REF_LIC_CLASS WHERE DESC_SDESC = 'Test-456'" );
      iRestoreTables.add( "DELETE FROM REF_LIC_CATEGORY WHERE DESC_SDESC = 'Test-456'" );
      iRestoreTables.add( "DELETE FROM REF_LIC_TYPE WHERE DESC_SDESC = 'Test-456'" );
   };


   /**
    * During Data Clean Up we need to delete all the events in EVT_EVENT and the IDs all exist in
    * EVT_LIC_DEFN. It will also clean up EVT_LIC_DEFN.
    *
    * @param aListLicIds
    *           List of LIC_DB_ID and associated LIC_ID
    * @throws SQLException
    */
   private void deleteRowsFromEvtLicDefnAndEvtEvent( List<ArrayList<String>> aListLicIds )
         throws SQLException {

      String lQuery = "Select event_db_id, event_id FROM EVT_LIC_DEFN Where ";
      for ( int i = 0; i < aListLicIds.size(); i++ ) {
         if ( i == 0 )
            lQuery += "(LIC_DB_ID = \'" + aListLicIds.get( i ).get( 0 ) + "\' AND LIC_ID = \'"
                  + aListLicIds.get( i ).get( 1 ) + "\')";
         else
            lQuery += "OR (LIC_DB_ID = \'" + aListLicIds.get( i ).get( 0 ) + "\' AND LIC_ID = \'"
                  + aListLicIds.get( i ).get( 1 ) + "\')";
      }
      String[] lIds = { "EVENT_DB_ID", "EVENT_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( lIds ) );
      List<ArrayList<String>> lLicIdList = execute( lQuery, lfields );

      // Run the delete same rows in previous query
      lQuery = lQuery.substring( 28 );
      lQuery = "Delete" + lQuery;
      runUpdate( lQuery );

      lQuery = "Delete From evt_event Where ";
      for ( int i = 0; i < lLicIdList.size(); i++ ) {
         if ( i == 0 )
            lQuery += "(EVENT_DB_ID = \'" + lLicIdList.get( i ).get( 0 ) + "\' AND EVENT_ID = \'"
                  + lLicIdList.get( i ).get( 1 ) + "\')";
         else
            lQuery += "OR (EVENT_DB_ID = \'" + lLicIdList.get( i ).get( 0 ) + "\' AND EVENT_ID = \'"
                  + lLicIdList.get( i ).get( 1 ) + "\')";
      }
      runUpdate( lQuery );

      lQuery = "Delete FROM LIC_DEFN_PREREQ Where ";
      for ( int i = 0; i < aListLicIds.size(); i++ ) {
         if ( i == 0 )
            lQuery += "(LIC_DB_ID = \'" + aListLicIds.get( i ).get( 0 ) + "\' AND LIC_ID = \'"
                  + aListLicIds.get( i ).get( 1 ) + "\')";
         else
            lQuery += "OR (LIC_DB_ID = \'" + aListLicIds.get( i ).get( 0 ) + "\' AND LIC_ID = \'"
                  + aListLicIds.get( i ).get( 1 ) + "\')";
      }
      runUpdate( lQuery );
   }


   /**
    * During Data Clean Up we need to delete all the events in EVT_EVENT and the IDs all exist in
    * EVT_LIC_DEFN. It will also clean up EVT_LIC_DEFN.
    *
    * @throws SQLException
    */
   private void deleteRowsFromGrpDfnAndGrpDefnLic( List<ArrayList<String>> aListLicIds )
         throws SQLException {

      String lQuery = "Delete FROM GRP_DEFN_LIC Where ";
      for ( int i = 0; i < aListLicIds.size(); i++ ) {
         if ( i == 0 )
            lQuery += "(LIC_DB_ID = \'" + aListLicIds.get( i ).get( 0 ) + "\' AND LIC_ID = \'"
                  + aListLicIds.get( i ).get( 1 ) + "\')";
         else
            lQuery += "OR (LIC_DB_ID = \'" + aListLicIds.get( i ).get( 0 ) + "\' AND LIC_ID = \'"
                  + aListLicIds.get( i ).get( 1 ) + "\')";
      }
      runUpdate( lQuery );

      lQuery = "delete from GRP_DEFN where GRP_TYPE_CD = 'EQUAL' AND GRP_TYPE_DB_ID = '0'";
      runUpdate( lQuery );
   }


   /**
    * Setup data for the whole class tests
    *
    * @param aQueryList
    *           List of queries used to load preparation data, or clean that preparation data
    *
    * @throws SQLException
    */
   private void DataSetupAndCleanUp( ArrayList<String> aQueryList ) {
      try {
         for ( String lUpdateQuery : aQueryList ) {
            PreparedStatement lStatement = getConnection().prepareStatement( lUpdateQuery );
            lStatement.executeUpdate( lUpdateQuery );
            commit();
         }
      } catch ( SQLException e ) {
         e.printStackTrace();
      }
   }


   /**
    * This test case will validate minimum number of fields required to successfully load the using
    * C_LIC_DEFN staging table.
    *
    */
   @Test
   public void testBasicLicenseDefintiion() {
      System.out.println(
            "======= Starting:  " + testName.getMethodName() + " ========================" );

      // inserting data into c_part_no_misc_info table
      Map<String, String> lCLicDefnMap = new LinkedHashMap<>();

      lCLicDefnMap.put( "LIC_CD", "\'LIC-1-456\'" );
      lCLicDefnMap.put( "LIC_SDESC", "\'License1-456\'" );
      lCLicDefnMap.put( "CARRIER_CD", "\'MXI\'" );
      lCLicDefnMap.put( "PRINT_ONCARD_BOOL", "\'N\'" );
      lCLicDefnMap.put( "EXTRA_PAY_BOOL", "\'Y\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_LIC_DEFN, lCLicDefnMap ) );
      lCLicDefnMap.remove( "CARRIER_CD" );
      lCLicDefnMap.put( "CARRIER_ID", "\'1001\'" );

      // run validation
      Assert.assertTrue( "Validation: ", runValidateLicDfnImport() == 1 );

      // run Import
      Assert.assertTrue( "Import: ", runImportLicDfnImport() == 1 );

      lCLicDefnMap = changeYesTo1AndNoto0( lCLicDefnMap );
      Assert.assertTrue( "Verify values in LIC_DEFN: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.LIC_DEFN, lCLicDefnMap ) ) );
      VerifyEvtEventAndEvtLicDefn( "\'License1-456\'", 2 );

   }


   /**
    * This test case will test both the C_LIC_DEFN and C_LIC_PREREQ The data is expected to be
    * loaded into the database. This will also verify all fields in the C_LIC_DEFN will be loaded
    * into Maintenix
    *
    * @throws SQLException
    */
   @Test
   public void testLicenseDefintiionWithPrerequisites() {
      System.out.println(
            "======= Starting:  " + testName.getMethodName() + " ========================" );

      // inserting data into C_LIC_DEFN table
      Map<String, String> lCLicDefnOneMap = new LinkedHashMap<>();

      lCLicDefnOneMap.put( "LIC_CD", "\'LIC-1-456\'" );
      lCLicDefnOneMap.put( "LIC_SDESC", "\'License1-456\'" );
      lCLicDefnOneMap.put( "LIC_LDESC", "\'License1-456 - long description\'" );
      lCLicDefnOneMap.put( "CARRIER_CD", "\'MXI\'" );
      lCLicDefnOneMap.put( "PRINT_ONCARD_BOOL", "\'N\'" );
      lCLicDefnOneMap.put( "EXTRA_PAY_BOOL", "\'N\'" );
      lCLicDefnOneMap.put( "LIC_CLASS_CD", "\'CLASS456\'" );
      lCLicDefnOneMap.put( "LIC_CAT_CD", "\'CAT-456\'" );
      lCLicDefnOneMap.put( "LIC_TYPE_CD", "\'TYPE-456\'" );
      lCLicDefnOneMap.put( "ASSMBL_CD", "\'ACFT_CD1\'" );
      lCLicDefnOneMap.put( "EXP_INTERVAL", "\'24\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_LIC_DEFN, lCLicDefnOneMap ) );
      lCLicDefnOneMap.remove( "CARRIER_CD" );
      lCLicDefnOneMap.put( "CARRIER_ID", "\'1001\'" );

      Map<String, String> lCLicDefnTwoMap = new LinkedHashMap<>();

      lCLicDefnTwoMap.put( "LIC_CD", "\'LIC-2-456\'" );
      lCLicDefnTwoMap.put( "LIC_SDESC", "\'License2-456\'" );
      lCLicDefnTwoMap.put( "CARRIER_CD", "\'MXI\'" );
      lCLicDefnTwoMap.put( "PRINT_ONCARD_BOOL", "\'Y\'" );
      lCLicDefnTwoMap.put( "EXTRA_PAY_BOOL", "\'N\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_LIC_DEFN, lCLicDefnTwoMap ) );

      lCLicDefnTwoMap.remove( "CARRIER_CD" );
      lCLicDefnTwoMap.put( "CARRIER_ID", "\'1001\'" );

      Map<String, String> lCLicDefnThreeMap = new LinkedHashMap<>();

      lCLicDefnThreeMap.put( "LIC_CD", "\'LIC-3-456\'" );
      lCLicDefnThreeMap.put( "LIC_SDESC", "\'License3-456\'" );
      lCLicDefnThreeMap.put( "CARRIER_CD", "\'MXI\'" );
      lCLicDefnThreeMap.put( "PRINT_ONCARD_BOOL", "\'Y\'" );
      lCLicDefnThreeMap.put( "EXTRA_PAY_BOOL", "\'Y\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_LIC_DEFN, lCLicDefnThreeMap ) );
      lCLicDefnThreeMap.remove( "CARRIER_CD" );
      lCLicDefnThreeMap.put( "CARRIER_ID", "\'1001\'" );

      Map<String, String> lCLicPrereqOneMap = new LinkedHashMap<>();

      lCLicPrereqOneMap.put( "LIC_CD", "\'LIC-2-456\'" );
      lCLicPrereqOneMap.put( "LIC_PREREQ_CD", "\'LIC-1-456\'" );
      lCLicPrereqOneMap.put( "ALT_PRIMARY_LIC_PREREQ_CD", "\'LIC-3-456\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_LIC_PREREQ, lCLicPrereqOneMap ) );

      Map<String, String> lCLicPrereqTwoMap = new LinkedHashMap<>();

      lCLicPrereqTwoMap.put( "LIC_CD", "\'LIC-2-456\'" );
      lCLicPrereqTwoMap.put( "LIC_PREREQ_CD", "\'LIC-3-456\'" );
      lCLicPrereqTwoMap.put( "ALT_PRIMARY_LIC_PREREQ_CD", "\'LIC-3-456\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_LIC_PREREQ, lCLicPrereqTwoMap ) );

      // run validation
      Assert.assertTrue( "Validation: ", runValidateLicDfnImport() == 1 );

      // run Import
      Assert.assertTrue( "Import: ", runImportLicDfnImport() == 1 );

      Map<String, String> lLicIdOneMap = findLicDbIdAndLicId( "LIC-1-456" );
      Map<String, String> lLicIdTwoMap = findLicDbIdAndLicId( "LIC-2-456" );
      Map<String, String> lLicIdThreeMap = findLicDbIdAndLicId( "LIC-3-456" );

      lCLicDefnOneMap = changeYesTo1AndNoto0( lCLicDefnOneMap );
      Assert.assertTrue( "Verify values in LIC_DEFN: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.LIC_DEFN, lCLicDefnOneMap ) ) );
      lCLicDefnTwoMap = changeYesTo1AndNoto0( lCLicDefnTwoMap );
      Assert.assertTrue( "Verify values in LIC_DEFN: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.LIC_DEFN, lCLicDefnTwoMap ) ) );
      lCLicDefnThreeMap = changeYesTo1AndNoto0( lCLicDefnThreeMap );
      Assert.assertTrue( "Verify values in LIC_DEFN: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.LIC_DEFN, lCLicDefnThreeMap ) ) );

      Assert.assertTrue( "Verify values in LIC_DEFN_PREREQ: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.LIC_DEFN_PREREQ, lLicIdTwoMap ) ) );

      Assert.assertTrue( "Verify values in GRP_DEFN_LIC: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.GRP_DEFN_LIC, lLicIdOneMap ) ) );
      Assert.assertTrue( "Verify values in GRP_DEFN_LIC: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.GRP_DEFN_LIC, lLicIdThreeMap ) ) );

      Map<String, String> lGrpDfNMap = new LinkedHashMap<>();
      lGrpDfNMap.put( "GRP_TYPE_DB_ID", "\'0\'" );
      lGrpDfNMap.put( "GRP_TYPE_CD", "\'EQUAL\'" );

      Assert.assertTrue( "Verify values in GRP_DEFN_LIC: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.GRP_DEFN, lGrpDfNMap ) ) );

      // 6 rows = the number of LIC_CD and 2 events per LIC_CD
      VerifyEvtEventAndEvtLicDefn( "\'License1-456\',\'License2-456\',\'License3-456\'", 6 );

   }


   /**
    * Verifies the number of rows generated in EVT_LIC_DEFN and EVT_EVENT based on the Lic_Ids from
    * LIC_DEFN
    *
    * @parm aLicCdList String of LIC_CD that are being used in the test.
    * @parm the number of rows expected to be generated by the test in both EVT_LIC_DEFN and
    *       EVT_EVENT Tables
    * @throws SQLException
    *
    */
   private void VerifyEvtEventAndEvtLicDefn( String aLicCdList, int aRowsExceptedReturned ) {

      String[] iIds = { "LIC_DB_ID", "LIC_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      List<ArrayList<String>> lListLicIds = execute(
            "Select lic_db_id, lic_id from LIC_DEFN where LIC_SDESC in (" + aLicCdList + ")",
            lfields );

      // This is checking the EVT_LIC_DEFN table
      String lQuery = "Select event_db_id, event_id FROM EVT_LIC_DEFN Where ";
      for ( int i = 0; i < lListLicIds.size(); i++ ) {
         if ( i == 0 )
            lQuery += "(LIC_DB_ID = \'" + lListLicIds.get( i ).get( 0 ) + "\' AND LIC_ID = \'"
                  + lListLicIds.get( i ).get( 1 ) + "\')";
         else
            lQuery += " OR (LIC_DB_ID = \'" + lListLicIds.get( i ).get( 0 ) + "\' AND LIC_ID = \'"
                  + lListLicIds.get( i ).get( 1 ) + "\')";
      }
      String[] lIds = { "EVENT_DB_ID", "EVENT_ID" };
      lfields = new ArrayList<String>( Arrays.asList( lIds ) );
      List<ArrayList<String>> lEvtLicIdList = execute( lQuery, lfields );

      // Run the count on the same rows the previous query
      lQuery = lQuery.substring( 47 );
      String lWhereClause = lQuery;
      try {
         Assert.assertTrue( "Row Count for EVT_LIC_DEFN",
               countRowsBasedOnWhereClause( TableUtil.EVT_LIC_DEFN,
                     lWhereClause ) == ( aRowsExceptedReturned ) );
      } catch ( SQLException e ) {
         e.printStackTrace();
      }

      // This is checking the EVT_EVENT table
      lWhereClause = "Where ";
      for ( int i = 0; i < lEvtLicIdList.size(); i++ ) {
         if ( i == 0 )
            lWhereClause += "(EVENT_DB_ID = \'" + lEvtLicIdList.get( i ).get( 0 )
                  + "\' AND EVENT_ID = \'" + lEvtLicIdList.get( i ).get( 1 ) + "\')";
         else
            lWhereClause += " OR (EVENT_DB_ID = \'" + lEvtLicIdList.get( i ).get( 0 )
                  + "\' AND EVENT_ID = \'" + lEvtLicIdList.get( i ).get( 1 ) + "\')";
      }
      try {
         Assert.assertTrue( "Row Count for EVT_EVENT",
               countRowsBasedOnWhereClause( TableUtil.EVT_EVENT,
                     lWhereClause ) == aRowsExceptedReturned );
      } catch ( SQLException e ) {
         e.printStackTrace();
      }
   }


   /**
    * This method will find the LIC_DB_ID and LIC_ID based on the unique LIC_CD value
    *
    * @param the
    *           value of the LIC_CD
    *
    * @return LIC_DB_ID and LIC_ID for the specific LIC_CD
    */
   private Map<String, String> findLicDbIdAndLicId( String aLicCdValue ) {
      String lQuery =
            "SELECT lic_db_id, lic_id FROM lic_defn WHERE lic_cd = \'" + aLicCdValue + "\'";

      Map<String, String> lLicIds = getLicIds( lQuery );
      return lLicIds;
   }


   protected int runValidateLicDfnImport() {
      int lReturn = 0;

      try {
         CallableStatement lPrepareCallInventory = getConnection()
               .prepareCall( "BEGIN licdefn_import.validate_licdefn(on_retcode =>  ?); END;" );
         lPrepareCallInventory.registerOutParameter( 1, Types.INTEGER );
         lPrepareCallInventory.execute();

         lReturn = lPrepareCallInventory.getInt( 1 );
         // rollBack();
      } catch ( SQLException e ) {
         e.printStackTrace();
      }
      return lReturn;
   }


   /**
    * Run the Materials_Import Import using a direct call to the plsql using a prepared statement.
    * This does not call any batch files.
    *
    * @returns the Result code for validation
    */
   protected int runImportLicDfnImport() {
      int lReturn = 0;

      try {
         CallableStatement lPrepareCallInventory = getConnection()
               .prepareCall( "BEGIN licdefn_import.import_licdefn(on_retcode =>  ?); END;" );
         lPrepareCallInventory.registerOutParameter( 1, Types.INTEGER );
         lPrepareCallInventory.execute();

         lReturn = lPrepareCallInventory.getInt( 1 );
         commit();
      } catch ( SQLException e ) {
         e.printStackTrace();
      }
      return lReturn;
   }


   /**
    * This converts all the "Y" values into 1 and all the "N" values into 0
    *
    * @param all
    *           values going into the C_Part_No_Misc_Info_Map
    * @return updated aDataMap
    */
   private Map<String, String> changeYesTo1AndNoto0( Map<String, String> aDataMap ) {

      for ( Entry<String, String> lEntry : aDataMap.entrySet() ) {
         if ( lEntry.getValue().equalsIgnoreCase( "\'Y\'" ) ) {
            aDataMap.put( lEntry.getKey(), "\'1\'" );
         }
         if ( lEntry.getValue().equalsIgnoreCase( "\'N\'" ) ) {
            aDataMap.put( lEntry.getKey(), "\'0\'" );
         }
      }

      return aDataMap;

   }


   /**
    * This function will run a query to obtain the LIC_DB_ID and LIC_ID
    *
    * @param aQuery
    *           Query to be ran
    *
    * @return Returns true if there is only one record, else false if there is no result or +2
    *         records
    *
    *
    * @throws SQLException
    */
   protected Map<String, String> getLicIds( String aQuery ) {

      Map<String, String> lResults = new LinkedHashMap<>();

      PreparedStatement lStatement;
      try {
         lStatement = getConnection().prepareStatement( aQuery, ResultSet.TYPE_SCROLL_SENSITIVE,
               ResultSet.CONCUR_READ_ONLY );

         ResultSet lResultSet = lStatement.executeQuery( aQuery );
         lResultSet.next();
         if ( lResultSet.getString( "LIC_DB_ID" ).equals( null )
               || lResultSet.getString( "LIC_ID" ).equals( null ) )
            Assert.assertTrue( "When running query: " + aQuery + " No values are returned.",
                  false );

         lResults.put( "LIC_DB_ID", lResultSet.getString( "LIC_DB_ID" ) );
         lResults.put( "LIC_ID", lResultSet.getString( "LIC_ID" ) );
      } catch ( SQLException e ) {
         e.printStackTrace();
      }

      return lResults;

   }

}
