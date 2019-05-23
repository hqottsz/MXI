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
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.AbstractDatabaseConnection;
import com.mxi.mx.util.TableUtil;


/**
 * Happy Path Test Cases for the Owners_Import package
 *
 */
public class Owner extends AbstractDatabaseConnection {

   private simpleIDs iOwnerIDs;

   @Rule
   public TestName testName = new TestName();

   /**
    * Deleting imported data from a Staging Table
    */
   protected ArrayList<String> iClearsStagingTable = new ArrayList<String>();
   {
      iClearsStagingTable.add( "DELETE FROM " + TableUtil.C_OWNER );
   }


   /**
    * This will clear a staging table and remove default owner from maintenix for a particular test
    * so a new can be added.
    *
    * {@inheritDoc}
    */
   @Override
   @Before
   public void before() throws Exception {
      super.before();
      classDataSetup( iClearsStagingTable );
      if ( testName.getMethodName().contains( "Default" ) )
         runUpdate( "UPDATE inv_owner SET default_bool = 0 WHERE default_bool = 1" );
   }


   /**
    * This will remove all imported data, and restore the original default value for a particular
    * test {@inheritDoc}
    */
   @Override
   @After
   public void after() throws Exception {
      removeImportedData();
      // restoring values
      if ( testName.getMethodName().contains( "Default" ) )
         runUpdate( "UPDATE inv_owner SET default_bool = 1 WHERE owner_cd = 'MXI'" );
      super.after();
   }


   protected void removeImportedData() {
      if ( iOwnerIDs != null )
         runUpdate( "Delete from inv_owner where owner_db_id = " + iOwnerIDs.getNO_DB_ID()
               + " and owner_id = " + iOwnerIDs.getNO_ID() );
   }


   /**
    *
    * Test to verify all staging values
    *
    */
   @Test
   public void testAllCOwnerValues() {
      Map<String, String> lCOwnerMap = new LinkedHashMap<>();
      lCOwnerMap.put( "OWNER_CD", "\'VEN36\'" );
      lCOwnerMap.put( "OWNER_NAME", "\'VENDOR 36\'" );
      lCOwnerMap.put( "DEFAULT_BOOL", "\'N\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_OWNER, lCOwnerMap ) );

      Assert.assertTrue( "Valdate Failed ", runValidationOWNER_IMPORT() == 1 );
      Assert.assertTrue( "Import Failed ", runImportOWNER_IMPORT() == 1 );
      verifyInvOwnerData( lCOwnerMap );
   }


   /**
    *
    * Test to ensure the system will default owner values to be loaded.
    *
    */
   @Test
   public void testAllCOwnerValuesDefaultBoolSetToY() {
      Map<String, String> lCOwnerMap = new LinkedHashMap<>();
      lCOwnerMap.put( "OWNER_CD", "\'VEN36\'" );
      lCOwnerMap.put( "OWNER_NAME", "\'VENDOR 36\'" );
      lCOwnerMap.put( "DEFAULT_BOOL", "\'Y\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_OWNER, lCOwnerMap ) );

      Assert.assertTrue( "Valdate Failed ", runValidationOWNER_IMPORT() == 1 );
      Assert.assertTrue( "Import Failed ", runImportOWNER_IMPORT() == 1 );
      verifyInvOwnerData( lCOwnerMap );
   }


   /**
    * This will verify the data in the INV_OWNER Mx table
    *
    * @param aDatMap
    *           - original test data that was entered into staging table.
    *
    */
   private void verifyInvOwnerData( Map<String, String> aDatMap ) {
      String lQuery = "Select OWNER_DB_ID, OWNER_ID from inv_owner where owner_name = "
            + aDatMap.get( "OWNER_NAME" );
      iOwnerIDs = getIDs( lQuery, "OWNER_DB_ID", "OWNER_ID" );

      Map<String, String> lExpectedValuesMap = new LinkedHashMap<>();
      lExpectedValuesMap.put( "OWNER_DB_ID", iOwnerIDs.getNO_DB_ID() );
      lExpectedValuesMap.put( "OWNER_ID", iOwnerIDs.getNO_ID() );
      lExpectedValuesMap.put( "OWNER_CD", aDatMap.get( "OWNER_CD" ) );
      lExpectedValuesMap.put( "OWNER_NAME", aDatMap.get( "OWNER_NAME" ) );
      if ( aDatMap.get( "DEFAULT_BOOL" ).equalsIgnoreCase( "'Y'" ) )
         lExpectedValuesMap.put( "DEFAULT_BOOL", "1" );
      else
         lExpectedValuesMap.put( "DEFAULT_BOOL", "0" );

      lQuery = TableUtil.findRecordInDatabase( TableUtil.INV_OWNER, lExpectedValuesMap );
      Assert.assertTrue( "Verify values in INV_OWNER, Query: " + lQuery,
            runQueryReturnsOneRow( lQuery ) );
   }


   /**
    *
    * All owner staging data to be validated using the stored procedure.
    *
    * @return a success or failure result
    */
   protected int runValidationOWNER_IMPORT() {
      int lReturn = 0;

      try {
         CallableStatement lPrepareCallKIT = getConnection()
               .prepareCall( "BEGIN  owner_import.validate_owner(on_retcode =>?); END;" );
         lPrepareCallKIT.registerOutParameter( 1, Types.INTEGER );
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
    * All owner staging data to be imported using the stored procedure.
    *
    * @return a success or failure result
    */
   protected int runImportOWNER_IMPORT() {
      int lReturn = 0;

      try {
         CallableStatement lPrepareCallKIT = getConnection()
               .prepareCall( "BEGIN owner_import.import_owner(on_retcode =>?); END;" );
         lPrepareCallKIT.registerOutParameter( 1, Types.INTEGER );
         lPrepareCallKIT.execute();
         commit();
         lReturn = lPrepareCallKIT.getInt( 1 );
      } catch ( SQLException e ) {
         e.printStackTrace();
         Assert.assertTrue( false );
      }
      return lReturn;
   }
}
