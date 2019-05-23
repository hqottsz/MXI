package com.mxi.mx.core.maint.plan.baselineloader;

import java.sql.CallableStatement;
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

import com.mxi.mx.core.maint.plan.actualsloader.PurchaseOrderTest;
import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.AbstractDatabaseConnection;
import com.mxi.mx.util.TableUtil;


/**
 * This will test the loading of the Part Advisory Tools
 *
 */
public class PartAdvisory extends AbstractDatabaseConnection {

   final static String iPartNoOem1 = "ACFT_ASSY_PN1";

   @Rule
   public TestName testName = new TestName();

   private ArrayList<String> iClearTables = new ArrayList<String>();
   {
      iClearTables.add( "DELETE FROM " + TableUtil.BL_PART_ADVISORY );
   }


   @Before
   @Override
   public void before() throws Exception {
      super.before();
      if ( testName.getMethodName().contains( "Lot" ) )
         addInvInvData();
   }


   @Override
   @After
   public void after() throws Exception {
      classDataSetup( iClearTables );
      cleanImportedData();
      if ( testName.getMethodName().contains( "Lot" ) )
         deleteInvRow();
      super.after();
   }


   /**
    * Adds a row to inv_inv table with a particular Serial_No_Oem value
    *
    */
   private void addInvInvData() {
      String lQuery = "SELECT part_no_db_id, part_no_id  FROM eqp_part_no WHERE  PART_NO_OEM = '"
            + iPartNoOem1 + "' AND MANUFACT_CD = '10001' AND inv_class_cd = 'ACFT'";

      PurchaseOrderTest lDataSetup = new PurchaseOrderTest();
      simpleIDs lPartNoIds = lDataSetup.getIDs( lQuery, "PART_NO_DB_ID", "PART_NO_ID" );

      int lInvNoID = getNextValueInSequence( "inv_no_id_seq" );
      String lQueryInsert =
            "INSERT INTO inv_inv (inv_no_db_id, inv_no_id, h_inv_no_db_id, h_inv_no_id, inv_class_db_id, inv_class_cd, part_no_db_id, part_no_id, serial_no_oem, lot_oem_tag ) "
                  + "VALUES(4650, " + lInvNoID + ", 4650, " + lInvNoID + ", 0, 'TRK', "
                  + lPartNoIds.getNO_DB_ID() + ", " + lPartNoIds.getNO_ID() + ", " + "'SN000009'"
                  + ", '351')";
      runUpdate( lQueryInsert );
   }


   /**
    *
    * Delete the row that was previously inserted.
    *
    */

   private void deleteInvRow() {
      runUpdate( "Delete from inv_inv where LOT_OEM_TAG = '351'" );
   }


   /**
    * Clean imported Data
    *
    */
   private void cleanImportedData() {

      String lQuery =
            "Select Advsry_DB_ID, Advsry_ID from EQP_ADVSRY where advsry_name like 'TestADVSRY_NAME%' ";

      String[] lIds = { "ADVSRY_DB_ID", "ADVSRY_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( lIds ) );
      List<ArrayList<String>> lAdvsryIds = execute( lQuery, lfields );

      String lWhereArgAdvsry = "";
      for ( int i = 0; i < lAdvsryIds.size(); i++ ) {
         if ( i == 0 )
            lWhereArgAdvsry += "(ADVSRY_DB_ID = \'" + lAdvsryIds.get( i ).get( 0 )
                  + "\' AND ADVSRY_ID = \'" + lAdvsryIds.get( i ).get( 1 ) + "\') ";
         else
            lWhereArgAdvsry += "OR (ADVSRY_DB_ID = \'" + lAdvsryIds.get( i ).get( 0 )
                  + "\' AND ADVSRY_ID = \'" + lAdvsryIds.get( i ).get( 1 ) + "\')";
      }
      runUpdate( createDeleteQuery( TableUtil.EQP_ADVSRY, lWhereArgAdvsry ) );
      runUpdate( createDeleteQuery( TableUtil.EQP_PART_ADVSRY, lWhereArgAdvsry ) );
      runUpdate( createDeleteQuery( TableUtil.INV_ADVSRY, lWhereArgAdvsry ) );
   }


   /**
    *
    * Creating a delete Query.
    *
    * @param aTable
    *           - the table of where the data will be removed
    * @param aWhereArg
    *           - the Where Arguments (or search criteria)
    * @return a delete query in a String format
    *
    */

   private String createDeleteQuery( String aTable, String aWhereArg ) {
      return "Delete From " + aTable + " Where " + aWhereArg;
   }


   /**
    *
    * This will test Part Advisory with a Serial Range Expect the data to load successfully.
    *
    */

   @Test
   public void testPartAdvisorySerialRange() {
      // inserting data into C_PO_HEADER table
      Map<String, String> lPartAdvisoryMap = new LinkedHashMap<>();

      lPartAdvisoryMap.put( "PART_NO_OEM", "\'ACFT_ASSY_PN1\'" );
      lPartAdvisoryMap.put( "MANUFACT_CD", "\'10001\'" );
      lPartAdvisoryMap.put( "ADVSRY_TYPE_CD", "\'SUP\'" );
      lPartAdvisoryMap.put( "ADVSRY_NAME", "\'TestADVSRY_NAME1\'" );
      lPartAdvisoryMap.put( "ADVSRY_NOTE", "\'TestADVSRY_NOTE\'" );
      lPartAdvisoryMap.put( "FLAG_ALL", "\'N\'" );
      lPartAdvisoryMap.put( "SERIAL_NO_RANGE", "\'\"SN000001\"-\"SN000013\"\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_PART_ADVISORY, lPartAdvisoryMap ) );

      Assert.assertTrue( "Validation error occurred", runValidationBL_PART_ADVISORY( true ) == 1 );

      Assert.assertTrue( "Import error occurred", runImportBL_PART_ADVISORY( true ) == 1 );

      PartAdvisoryTest lVerifyData = new PartAdvisoryTest();
      lVerifyData.findIds( lPartAdvisoryMap );
      lVerifyData.verifyEqpAdvsry( lPartAdvisoryMap );
      lVerifyData.verifyEqpPartAdvsry( lPartAdvisoryMap.get( "SERIAL_NO_RANGE" ), null );
      lVerifyData.verifyInvAdvsry( lPartAdvisoryMap.get( "SERIAL_NO_RANGE" ),
            lPartAdvisoryMap.get( "LOT_NO_RANGE" ) );
   }


   /**
    *
    * This will test Part Advisory with Serial and Lot Ranges Expect the data to load successfully.
    *
    */

   @Test
   public void testPartAdvisorySerialAndLotRanges() {
      // inserting data into C_PO_HEADER table
      Map<String, String> lPartAdvisoryMap = new LinkedHashMap<>();

      lPartAdvisoryMap.put( "PART_NO_OEM", "\'ACFT_ASSY_PN1\'" );
      lPartAdvisoryMap.put( "MANUFACT_CD", "\'10001\'" );
      lPartAdvisoryMap.put( "ADVSRY_TYPE_CD", "\'SUP\'" );
      lPartAdvisoryMap.put( "ADVSRY_NAME", "\'TestADVSRY_NAME1\'" );
      lPartAdvisoryMap.put( "ADVSRY_NOTE", "\'TestADVSRY_NOTE\'" );
      lPartAdvisoryMap.put( "FLAG_ALL", "\'N\'" );
      lPartAdvisoryMap.put( "SERIAL_NO_RANGE", "\'\"SN000001\"-\"SN000013\"\'" );
      lPartAdvisoryMap.put( "LOT_NO_RANGE", "\'\"350\"-\"351\"\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_PART_ADVISORY, lPartAdvisoryMap ) );

      Assert.assertTrue( "Validation error occurred", runValidationBL_PART_ADVISORY( true ) == 1 );

      Assert.assertTrue( "Import error occurred", runImportBL_PART_ADVISORY( true ) == 1 );

      PartAdvisoryTest lVerifyData = new PartAdvisoryTest();
      lVerifyData.findIds( lPartAdvisoryMap );
      lVerifyData.verifyEqpAdvsry( lPartAdvisoryMap );
      lVerifyData.verifyEqpPartAdvsry( lPartAdvisoryMap.get( "SERIAL_NO_RANGE" ),
            lPartAdvisoryMap.get( "LOT_NO_RANGE" ) );
      lVerifyData.verifyInvAdvsry( lPartAdvisoryMap.get( "SERIAL_NO_RANGE" ),
            lPartAdvisoryMap.get( "LOT_NO_RANGE" ) );
   }


   /**
    *
    * This will test Part Advisory with a Lot Range Expect the data to load successfully.
    *
    */
   @Test
   public void testPartAdvisoryLotRange() {
      // inserting data into C_PO_HEADER table
      Map<String, String> lPartAdvisoryMap = new LinkedHashMap<>();

      lPartAdvisoryMap.put( "PART_NO_OEM", "\'ACFT_ASSY_PN1\'" );
      lPartAdvisoryMap.put( "MANUFACT_CD", "\'10001\'" );
      lPartAdvisoryMap.put( "ADVSRY_TYPE_CD", "\'SUP\'" );
      lPartAdvisoryMap.put( "ADVSRY_NAME", "\'TestADVSRY_NAME1\'" );
      lPartAdvisoryMap.put( "ADVSRY_NOTE", "\'TestADVSRY_NOTE\'" );
      lPartAdvisoryMap.put( "FLAG_ALL", "\'N\'" );
      lPartAdvisoryMap.put( "LOT_NO_RANGE", "\'\"350\"-\"351\"\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_PART_ADVISORY, lPartAdvisoryMap ) );

      Assert.assertTrue( "Validation error occurred", runValidationBL_PART_ADVISORY( true ) == 1 );

      Assert.assertTrue( "Import error occurred", runImportBL_PART_ADVISORY( true ) == 1 );

      PartAdvisoryTest lVerifyData = new PartAdvisoryTest();
      lVerifyData.findIds( lPartAdvisoryMap );
      lVerifyData.verifyEqpAdvsry( lPartAdvisoryMap );
      lVerifyData.verifyEqpPartAdvsry( null, lPartAdvisoryMap.get( "LOT_NO_RANGE" ) );
      lVerifyData.verifyInvAdvsry( lPartAdvisoryMap.get( "SERIAL_NO_RANGE" ),
            lPartAdvisoryMap.get( "LOT_NO_RANGE" ) );
   }


   /**
    *
    * This will test Part Advisory with no Ranges Expect the data to load successfully.
    *
    */

   @Test
   public void testPartAdvisoryFlagAll() {
      // inserting data into C_PO_HEADER table
      Map<String, String> lPartAdvisoryMap = new LinkedHashMap<>();

      lPartAdvisoryMap.put( "PART_NO_OEM", "\'ACFT_ASSY_PN1\'" );
      lPartAdvisoryMap.put( "MANUFACT_CD", "\'10001\'" );
      lPartAdvisoryMap.put( "ADVSRY_TYPE_CD", "\'SUP\'" );
      lPartAdvisoryMap.put( "ADVSRY_NAME", "\'TestADVSRY_NAME2\'" );
      lPartAdvisoryMap.put( "ADVSRY_NOTE", "\'TestADVSRY_NOTE\'" );
      lPartAdvisoryMap.put( "FLAG_ALL", "\'Y\'" );
      lPartAdvisoryMap.put( "SERIAL_NO_RANGE", "\'\'" );
      lPartAdvisoryMap.put( "LOT_NO_RANGE", "\'\'" );
      lPartAdvisoryMap.put( "ADVSRY_DT", "to_date(\'01-01-2018\', \'dd-mm-yyyy\')" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_PART_ADVISORY, lPartAdvisoryMap ) );

      Assert.assertTrue( "Validation error occurred", runValidationBL_PART_ADVISORY( true ) == 1 );

      Assert.assertTrue( "Import error occurred", runImportBL_PART_ADVISORY( true ) == 1 );

      PartAdvisoryTest lVerifyData = new PartAdvisoryTest();
      lVerifyData.findIds( lPartAdvisoryMap );
      lVerifyData.verifyEqpAdvsry( lPartAdvisoryMap );
      lVerifyData.verifyEqpPartAdvsry( null, null );
      lVerifyData.verifyInvAdvsry( lPartAdvisoryMap.get( "SERIAL_NO_RANGE" ),
            lPartAdvisoryMap.get( "LOT_NO_RANGE" ) );
   }


   /**
    *
    * Execute the validation stored procedure within the database.
    *
    */

   private int runValidationBL_PART_ADVISORY( boolean allornone ) {
      int lReturn = 0;
      CallableStatement lPrepareCallKIT;

      try {
         if ( allornone ) {
            lPrepareCallKIT = getConnection().prepareCall(
                  "BEGIN  bl_part_advisory_import.validate(aiv_exist_in_mx => 'STRICT', aib_allornone => true, aon_retcode =>?, aov_retmsg =>?); END;" );
         } else {
            lPrepareCallKIT = getConnection().prepareCall(
                  "BEGIN  bl_part_advisory_import.validate(aiv_exist_in_mx => 'STRICT',aib_allornone => false, aon_retcode =>?, aov_retmsg =>?); END;" );
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
   private int runImportBL_PART_ADVISORY( boolean allornone ) {
      int lResult = 0;
      CallableStatement lPrepareCallKIT;

      try {
         if ( allornone ) {
            lPrepareCallKIT = getConnection().prepareCall(
                  "BEGIN  bl_part_advisory_import.import(aiv_exist_in_mx => 'STRICT', aib_allornone => true, aon_retcode =>?, aov_retmsg =>?); END;" );
         } else {
            lPrepareCallKIT = getConnection().prepareCall(
                  "BEGIN  bl_part_advisory_import.import(aiv_exist_in_mx => 'STRICT',aib_allornone => false, aon_retcode =>?, aov_retmsg =>?); END;" );

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
