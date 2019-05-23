/*
 * Confidential, proprietary and/or trade secret information of Mxi Technologies, Ltd.
 *
 * Copyright 2000-2016 Mxi Technologies, Ltd. All Rights Reserved.
 *
 * Except as expressly provided by written license signed by a duly appointed officer of Mxi
 * Technologies, Ltd., any disclosure, distribution, reproduction, compilation, modification,
 * creation of derivative works and/or other use of the Mxi source code is strictly prohibited.
 * Inclusion of a copyright notice shall not be taken to indicate that the source code has been
 * published.
 */

package com.mxi.mx.core.maint.plan.actualsloader.inventory.imports;

import static org.hamcrest.MatcherAssert.assertThat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.ActualsLoaderTest;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.WhereClause;


/**
 * Abstract Import Inventory class
 */
public class AbstractImportInventory extends ActualsLoaderTest {

   /**
    * Verifies that imported inventory was created as expected
    *
    * @param aMap
    *
    * @throws Exception
    */
   protected void checkInventoryDetails( Map<String, String> aMap ) throws Exception {

      // verify that inventory is created as expected
      ResultSet lResultSetINVINV =
            getInventoryRecord( aMap.get( "SERIAL_NO_OEM" ), aMap.get( "PART_NO_OEM" ) );

      // verify that only at least one row is returned
      assertThat( "No inventory rows were imported.", lResultSetINVINV.first() );

      // verify that only one inventory row is returned
      assertThat( "Too many rows were returned.", lResultSetINVINV.isLast() );

      // verify inv_class_cd
      verifyRecordDetail( lResultSetINVINV, aMap, "INV_CLASS_CD" );

      // verify manufact_cd
      verifyRecordDetail( lResultSetINVINV, aMap, "MANUFACT_CD" );

      // verify loc_cd
      verifyRecordDetail( lResultSetINVINV, aMap, "LOC_CD" );

      // verify inv_cond_cd
      verifyRecordDetail( lResultSetINVINV, aMap, "INV_COND_CD" );

      // verify RESERVE_QT to be zero for BATCH, NULL for TRK and SER
      if ( aMap.get( "INV_CLASS_CD" ).equalsIgnoreCase( "'BATCH'" ) ) {
         verifyRecordDetail( lResultSetINVINV, "0", "RESERVE_QT" );
      } else {
         verifyRecordDetail( lResultSetINVINV, "", "RESERVE_QT" );

      }

      // for tests on batch inventory
      if ( aMap.containsKey( "BATCH_NO_OEM" ) ) {

         // verify batch_no_oem
         verifyRecordDetail( lResultSetINVINV, aMap, "BATCH_NO_OEM" );

         // verify owner_cd
         verifyRecordDetail( lResultSetINVINV, aMap, "OWNER_CD" );

         // verify bin_qt
         verifyRecordDetail( lResultSetINVINV, aMap, "BIN_QT" );
      }

      // for tests that want to check total quantity for a part number
      if ( aMap.containsKey( "TOTAL_QT" ) ) {

         // verify inv_cond_cd
         verifyRecordDetail( lResultSetINVINV, aMap, "TOTAL_QT" );
      }
   }


   /**
    * Ensure that the parent inventory identified by the data on aParent does not have any holes in
    * it's configuration. This query will find all children that have the parent inventory as their
    * highest inventory and it will exclude the parent from the results.
    *
    * @param aParent
    * @param aExpectedChildren
    *           DOCUMENT_ME
    *
    * @throws Exception
    */
   protected void checkInventoryHasRightNumberOfChildren( Map<String, String> aParent,
         int aExpectedChildren ) throws Exception {

      StringBuilder lQuery = new StringBuilder();

      lQuery.append( "SELECT count(1) AS count " );
      lQuery.append( "FROM inv_inv p " );
      lQuery.append( "INNER JOIN inv_inv c ON " );
      lQuery.append( "p.inv_no_db_id = c.h_inv_no_db_id AND " );
      lQuery.append( "p.inv_no_id = c.h_inv_no_id " );
      lQuery.append( "WHERE p.serial_no_oem = " + aParent.get( "SERIAL_NO_OEM" ) + " AND " );
      lQuery.append( "p.inv_no_id != c.inv_no_id" );

      ResultSet lQueryResult = runQuery( lQuery.toString() );

      lQueryResult.first();

      int lChildrenFound = lQueryResult.getInt( "count" );

      lQueryResult.close();

      assertThat(
            "Expected to find " + aExpectedChildren + " children but found: " + lChildrenFound,
            lChildrenFound == aExpectedChildren );
   }


   /**
    * Ensure that the capabilities for specified aircraft are inserted into the table
    * acft_cap_levels.
    *
    * @param aParent
    * @param aExpectedCapabilities
    *           the capabilities for the aircraft
    *
    * @throws Exception
    */
   protected void checkAircraftHasRightNumberOfCapabilities( Map<String, String> aParent,
         int aExpectedNumber ) throws Exception {

      StringBuilder lQuery = new StringBuilder();

      lQuery.append( "SELECT count(1) AS count " );
      lQuery.append( "FROM acft_cap_levels p " );
      lQuery.append( "INNER JOIN inv_inv c ON " );
      lQuery.append( "p.acft_no_db_id = c.inv_no_db_id AND " );
      lQuery.append( "p.acft_no_id = c.inv_no_id " );
      lQuery.append( "WHERE c.serial_no_oem = " + aParent.get( "SERIAL_NO_OEM" ) );

      ResultSet lQueryResult = runQuery( lQuery.toString() );

      lQueryResult.first();

      int lCapabilitiesFound = lQueryResult.getInt( "count" );

      lQueryResult.close();

      assertThat( "Expected to find " + aExpectedNumber + " capabilities but found: "
            + lCapabilitiesFound, lCapabilitiesFound == aExpectedNumber );
   }


   /**
    * Verifies all values made it into Mx from C_RI_INVENTORY_CAP_LEVELS
    *
    * @param lDataMap
    */
   protected void checkCapabilitiesDetails( Map<String, String> lDataMap ) {
      String lQuery = "SELECT inv_no_db_id, inv_no_id FROM inv_inv ii "
            + "INNER JOIN eqp_part_no ep ON " + "ep.part_no_db_id = ii.part_no_db_id AND "
            + "ep.part_no_id = ii.part_no_id " + "WHERE serial_no_oem = "
            + lDataMap.get( "SERIAL_NO_OEM" ) + " AND " + "ii.inv_class_cd = 'ACFT' AND "
            + "ep.part_no_oem = " + lDataMap.get( "PART_NO_OEM" ) + " AND " + "ep.manufact_cd = "
            + lDataMap.get( "MANUFACT_CD" );
      simpleIDs lAcftIds = getIDs( lQuery, "INV_NO_DB_ID", "INV_NO_ID" );

      lQuery = "SELECT acft_cap_db_id, acft_cap_cd FROM Ref_Acft_Cap WHERE acft_cap_cd = "
            + lDataMap.get( "CAP_CD" );
      simpleIDs lCapIds = getIDs( lQuery, "ACFT_CAP_DB_ID", "ACFT_CAP_CD" );

      lQuery =
            "SELECT acft_cap_level_db_id, acft_cap_level_cd FROM Ref_Acft_Cap_Level WHERE acft_cap_level_cd = "
                  + lDataMap.get( "LEVEL_CD" ) + " AND acft_cap_cd = " + lDataMap.get( "CAP_CD" );
      simpleIDs lLevelIds = getIDs( lQuery, "ACFT_CAP_LEVEL_DB_ID", "ACFT_CAP_LEVEL_CD" );
      lQuery =
            "SELECT acft_cap_level_db_id, acft_cap_level_cd FROM Ref_Acft_Cap_Level WHERE acft_cap_level_cd = "
                  + lDataMap.get( "CONFIG_LEVEL_CD" ) + " AND acft_cap_cd = "
                  + lDataMap.get( "CAP_CD" );
      simpleIDs lConfigIds = getIDs( lQuery, "ACFT_CAP_LEVEL_DB_ID", "ACFT_CAP_LEVEL_CD" );

      // Values that are expected to be in AcftCapLevels
      Map<String, String> ExpectedValuesMap = new LinkedHashMap<String, String>();

      ExpectedValuesMap.put( "ACFT_NO_DB_ID", lAcftIds.getNO_DB_ID() );
      ExpectedValuesMap.put( "ACFT_NO_ID", lAcftIds.getNO_ID() );
      ExpectedValuesMap.put( "CAP_DB_ID", lCapIds.getNO_DB_ID() );
      ExpectedValuesMap.put( "CAP_CD", "'" + lCapIds.getNO_ID() + "'" );
      ExpectedValuesMap.put( "LEVEL_DB_ID", lLevelIds.getNO_DB_ID() );
      ExpectedValuesMap.put( "LEVEL_CD", "'" + lLevelIds.getNO_ID() + "'" );
      ExpectedValuesMap.put( "CUSTOM_LEVEL", lDataMap.get( "CUSTOM_LEVEL" ) );
      ExpectedValuesMap.put( "CONFIG_LEVEL_DB_ID", lConfigIds.getNO_DB_ID() );
      ExpectedValuesMap.put( "CONFIG_LEVEL_CD", "'" + lConfigIds.getNO_ID() + "'" );

      Assert.assertTrue( "Verify values in ACFT_CAP_LEVELS: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.ACFT_CAP_LEVELS, ExpectedValuesMap ) ) );
   }


   /**
    * Verifies that imported sub-inventory was created as expected
    *
    * @param aMap
    *
    * @throws Exception
    */
   protected void checkSubInventoryDetails( Map<String, String> aMap ) throws Exception {

      // verify that SUB inventory is created as expected
      ResultSet lSubInvResultSet =
            getSubInventoryRecord( aMap.get( "SERIAL_NO_OEM" ), aMap.get( "PART_NO_OEM" ),
                  aMap.get( "PARENT_SERIAL_NO_OEM" ), aMap.get( "MANUFACT_CD" ) );

      // verify that only at least one row is returned
      assertThat( "No inventory rows were imported.", lSubInvResultSet.first() );

      // verify that only one inventory row is returned
      assertThat( "Too many rows were returned.", lSubInvResultSet.isLast() );

      // verify parent_serial_no
      verifyRecordDetail( lSubInvResultSet, aMap, "PARENT_SERIAL_NO_OEM" );

      // verify parent_part_no
      verifyRecordDetail( lSubInvResultSet, aMap, "PARENT_PART_NO_OEM" );

      // verify parent_manuf_cd
      verifyRecordDetail( lSubInvResultSet, aMap, "PARENT_MANUFACT_CD" );

      // verify bom_part_cd
      verifyRecordDetail( lSubInvResultSet, aMap, "BOM_PART_CD" );

      // verify inv_class_cd
      verifyRecordDetail( lSubInvResultSet, aMap, "INV_CLASS_CD" );

      // verify manufact_cd
      verifyRecordDetail( lSubInvResultSet, aMap, "MANUFACT_CD" );

      // verify inv_cond_cd
      verifyRecordDetail( lSubInvResultSet, aMap, "INV_COND_CD" );

   }


   protected void checkSubInventoryOwner( Map<String, String> aMap ) throws Exception {

      // verify that SUB inventory is created as expected
      ResultSet lSubInvResultSet =
            getSubInventoryRecord( aMap.get( "SERIAL_NO_OEM" ), aMap.get( "PART_NO_OEM" ),
                  aMap.get( "PARENT_SERIAL_NO_OEM" ), aMap.get( "MANUFACT_CD" ) );

      // verify that only at least one row is returned
      assertThat( "No inventory rows were imported.", lSubInvResultSet.first() );

      // verify that only one inventory row is returned
      assertThat( "Too many rows were returned.", lSubInvResultSet.isLast() );

      // verify owner_cd
      verifyRecordDetail( lSubInvResultSet, aMap, "OWNER_CD" );

   }


   /**
    * Verifies that imported sub-inventory usage values are created as expected
    *
    * @param aMap
    *
    * @throws Exception
    */
   protected void checkChildCurrentUsage( Map<String, String> aMap ) throws Exception {

      // verify that SUB inventory Usage is created as expected
      ResultSet lChildUsageResultSet = getChildCurrentUsage( aMap.get( "PARENT_SERIAL_NO_OEM" ),
            aMap.get( "PARENT_PART_NO_OEM" ), aMap.get( "PARENT_MANUFACT_CD" ),
            aMap.get( "SERIAL_NO_OEM" ), aMap.get( "PART_NO_OEM" ), aMap.get( "MANUFACT_CD" ),
            aMap.get( "BOM_PART_CD" ), aMap.get( "DATA_TYPE_CD" ) );

      // verify that only at least one row is returned
      assertThat( "No inventory rows were imported.", lChildUsageResultSet.first() );

      // verify that only one inventory row is returned
      assertThat( "Too many rows were returned.", lChildUsageResultSet.isLast() );

      // verify parent_serial_no
      verifyRecordDetail( lChildUsageResultSet, aMap, "PARENT_SERIAL_NO_OEM" );

      // verify parent_part_no
      verifyRecordDetail( lChildUsageResultSet, aMap, "PARENT_PART_NO_OEM" );

      // verify parent_manuf_cd
      verifyRecordDetail( lChildUsageResultSet, aMap, "PARENT_MANUFACT_CD" );

      // verify bom_part_cd
      verifyRecordDetail( lChildUsageResultSet, aMap, "BOM_PART_CD" );

      // verify part_no
      verifyRecordDetail( lChildUsageResultSet, aMap, "PART_NO_OEM" );

      // verify manufact_cd
      verifyRecordDetail( lChildUsageResultSet, aMap, "MANUFACT_CD" );

      // verify tsn
      verifyRecordDetail( lChildUsageResultSet, aMap, "TSN_QT" );

      // verify tsi
      verifyRecordDetail( lChildUsageResultSet, aMap, "TSI_QT" );

      // verify tso
      verifyRecordDetail( lChildUsageResultSet, aMap, "TSO_QT" );

   }


   /**
    * Retrieves the total quantity, total value and average unit price of a given part
    *
    * @param aPart
    *
    * @return DOCUMENT_ME
    *
    * @throws Exception
    */
   protected Map<String, String> getPartTotalQtyAndValue( Map<String, String> aPart )
         throws Exception {

      StringBuilder lQuery = new StringBuilder();

      lQuery.append( "SELECT part_no_oem, manufact_cd, total_qt, total_value, avg_unit_price " );
      lQuery.append( "FROM eqp_part_no " );
      lQuery.append( "WHERE part_no_oem = " + aPart.get( "PART_NO_OEM" ) + " AND " );
      lQuery.append( "manufact_cd = " + aPart.get( "MANUFACT_CD" ) );

      Map<String, String> lMapPart = new LinkedHashMap<String, String>();
      ResultSet lResultSet = runQuery( lQuery.toString() );

      lResultSet.first();

      lMapPart.put( "PART_NO_OEM", "'" + lResultSet.getString( "part_no_oem" ) + "'" );
      lMapPart.put( "MANUFACT_CD", "'" + lResultSet.getString( "manufact_cd" ) + "'" );

      Double lTotalQt = lResultSet.getDouble( "total_qt" );
      lMapPart.put( "TOTAL_QT", lTotalQt.toString() );

      Double lTotalVal = lResultSet.getDouble( "total_value" );
      lMapPart.put( "TOTAL_VALUE", lTotalVal.toString() );

      Double lAvgUnitPrice = lResultSet.getDouble( "avg_unit_price" );
      lMapPart.put( "AVG_UNIT_PRICE", lAvgUnitPrice.toString() );

      return lMapPart;
   }


   @Override
   protected int getRandom() {
      return ( int ) ( ( Math.random() * 50000 ) + 1 );
   }


   /**
    * Import the inventory that was set up prior. This involves calling Stage Inventory and then the
    * Import Inventory
    *
    * @throws SQLException
    */
   protected void importInventory() throws SQLException {

      runImportInventory();
   }


   /**
    * random number used to help name serial and batch no
    *
    * @param aCompleteInventory
    *           DOCUMENT_ME
    *
    * @throws SQLException
    *            DOCUMENT_ME
    */

   protected void importInventory( boolean aCompleteInventory ) throws SQLException {

      // call inventory import
      runImportStageInventory( aCompleteInventory );
      runImportInventory();
   }


   /**
    * this function is clear out all events which are generated by inventory loading.
    */
   public void clearEvent( simpleIDs aEvent_ID ) {

      if ( aEvent_ID != null ) {

         // delete evt_sched_dead
         String lStrDelete = "delete from " + TableUtil.EVT_SCHED_DEAD + "  where EVENT_DB_ID="
               + aEvent_ID.getNO_DB_ID() + " and EVENT_ID=" + aEvent_ID.getNO_ID();
         executeSQL( lStrDelete );

         // delete evt_inv_usage
         lStrDelete = "delete from " + TableUtil.EVT_INV_USAGE + "  where EVENT_DB_ID="
               + aEvent_ID.getNO_DB_ID() + " and EVENT_ID=" + aEvent_ID.getNO_ID();
         executeSQL( lStrDelete );

         // delete evt_stage
         lStrDelete = "delete from " + TableUtil.EVT_STAGE + "  where EVENT_DB_ID="
               + aEvent_ID.getNO_DB_ID() + " and EVENT_ID=" + aEvent_ID.getNO_ID();
         executeSQL( lStrDelete );

         // delete evt_inv
         lStrDelete = "delete from " + TableUtil.EVT_INV + "  where EVENT_DB_ID="
               + aEvent_ID.getNO_DB_ID() + " and EVENT_ID=" + aEvent_ID.getNO_ID();
         executeSQL( lStrDelete );

         // delete evt_event_rel
         lStrDelete = "delete from " + TableUtil.EVT_EVENT_REL + "  where EVENT_DB_ID="
               + aEvent_ID.getNO_DB_ID() + " and EVENT_ID=" + aEvent_ID.getNO_ID();
         executeSQL( lStrDelete );

         // delete sched_stask
         lStrDelete = "delete from " + TableUtil.SCHED_STASK + "  where SCHED_DB_ID="
               + aEvent_ID.getNO_DB_ID() + " and SCHED_ID=" + aEvent_ID.getNO_ID();
         executeSQL( lStrDelete );

         // delete evt_event
         lStrDelete = "delete from " + TableUtil.EVT_EVENT + "  where EVENT_DB_ID="
               + aEvent_ID.getNO_DB_ID() + " and EVENT_ID=" + aEvent_ID.getNO_ID();
         executeSQL( lStrDelete );

      }

   }


   /**
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */

   public void RestoreINVData( simpleIDs iINV_Ids ) {

      String lStrDelete = null;

      // Delete acft_cap_levels data which created by test case
      if ( iINV_Ids != null ) {

         lStrDelete = "delete from " + TableUtil.ACFT_CAP_LEVELS + " where ACFT_NO_DB_ID='"
               + iINV_Ids.getNO_DB_ID() + "' and ACFT_NO_ID='" + iINV_Ids.getNO_ID() + "'";
         executeSQL( lStrDelete );

         lStrDelete = "DELETE inv_curr_usage "
               + "WHERE (inv_no_db_id, inv_no_id) IN (SELECT inv_no_db_id, inv_no_id FROM inv_inv WHERE h_inv_no_id ='"
               + iINV_Ids.getNO_ID() + "')";
         executeSQL( lStrDelete );

         lStrDelete = "delete from " + TableUtil.INV_INV + " where H_INV_NO_DB_ID='"
               + iINV_Ids.getNO_DB_ID() + "' and H_INV_NO_ID='" + iINV_Ids.getNO_ID() + "'";
         executeSQL( lStrDelete );

         lStrDelete = "delete from " + TableUtil.INV_AC_REG + " where INV_NO_DB_ID='"
               + iINV_Ids.getNO_DB_ID() + "' and INV_NO_ID='" + iINV_Ids.getNO_ID() + "'";
         executeSQL( lStrDelete );

      }

   }


   /**
    * This function is to verify inv_inv table.
    *
    *
    */
   public simpleIDs verifyINVINVtable( String aSERIAL_NO_OEM, String aINV_CLASS_CD ) {
      // inv_inv table
      String[] iIds = { "INV_NO_DB_ID", "INV_NO_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SERIAL_NO_OEM", aSERIAL_NO_OEM );
      lArgs.addArguments( "INV_CLASS_CD", aINV_CLASS_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.INV_INV, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;

   }


   /**
    * This function is to verify inv_inv table.
    *
    *
    */
   public simpleIDs verifySERINVINVtable( String aSERIAL_NO_OEM, String aINV_CLASS_CD,
         String aINV_COND_CD ) {
      // inv_inv table
      String[] iIds = { "INV_NO_DB_ID", "INV_NO_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SERIAL_NO_OEM", aSERIAL_NO_OEM );
      lArgs.addArguments( "INV_CLASS_CD", aINV_CLASS_CD );
      lArgs.addArguments( "INV_COND_CD", aINV_COND_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.INV_INV, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;

   }


   /**
    * This function is to verify inv_inv table.
    *
    *
    */
   public void verifyINVINVtable( simpleIDs aHIDs, String aAssmbl_CD, String aSERIAL_NO_OEM,
         String aCONFIG_POS_SDESC, String aINV_CLASS_CD, String aComplete_BOOL,
         String aINV_COND_CD ) {
      // inv_inv table
      String[] iIds = { "COMPLETE_BOOL", "INV_COND_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "H_INV_NO_DB_ID", aHIDs.getNO_DB_ID() );
      lArgs.addArguments( "H_INV_NO_ID", aHIDs.getNO_ID() );
      lArgs.addArguments( "ASSMBL_CD", aAssmbl_CD );
      lArgs.addArguments( "SERIAL_NO_OEM", aSERIAL_NO_OEM );
      if ( aCONFIG_POS_SDESC != null )
         lArgs.addArguments( "CONFIG_POS_SDESC", aCONFIG_POS_SDESC );
      lArgs.addArguments( "INV_CLASS_CD", aINV_CLASS_CD );

      String iQueryString = TableUtil.buildTableQueryOrderBy( TableUtil.INV_INV, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "COMPLETE_BOOL",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aComplete_BOOL ) );
      Assert.assertTrue( "INV_COND_CD", llists.get( 0 ).get( 1 ).equalsIgnoreCase( aINV_COND_CD ) );

   }


   /**
    * This function is to get inv loc ids
    *
    *
    */
   public simpleIDs getINV_LOC_IDs( String aLOC_TYPE_CD, String aLOC_CD, String aLOC_NAME ) {
      // inv_inv table
      String[] iIds = { "LOC_DB_ID", "LOC_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "LOC_TYPE_CD", aLOC_TYPE_CD );
      lArgs.addArguments( "LOC_CD", aLOC_CD );
      lArgs.addArguments( "LOC_NAME", aLOC_NAME );

      String iQueryString = TableUtil.buildTableQueryOrderBy( TableUtil.INV_LOC, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      return new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

   }

}
