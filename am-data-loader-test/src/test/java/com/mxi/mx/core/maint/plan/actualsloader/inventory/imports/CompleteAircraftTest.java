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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.TableUtil;


/**
 * This test suite contains test cases for successful aircraft loading using Actuals Loader
 *
 * @author Warren Pinkney
 */
public class CompleteAircraftTest extends AbstractImportInventory {

   simpleIDs iSerialNoOem = null;


   /**
    * Clean up after each individual test
    *
    * @throws Exception
    */
   @After
   @Override
   public void after() throws Exception {

      // clean up the event data

      clearMxTestData();

      if ( iSerialNoOem != null ) {
         String lQuery =
               "Delete FROM inv_inv WHERE (inv_class_cd = 'SYS' OR inv_class_cd = 'ACFT') AND h_inv_no_db_id = "
                     + iSerialNoOem.getNO_DB_ID() + "  AND h_inv_no_id = "
                     + iSerialNoOem.getNO_ID();
         runUpdate( lQuery );
      }
      iSerialNoOem = null;
      super.after();
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>The part numbers exists in maintenix</li>
    * <li>The TRK-on-TRK configurations are defined in maintenix</li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Insert inventory and sub-inventory to the processing tables minus one inventory (a
    * hole)</li>
    * <li>Run Inventory Import</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: NONE</li>
    * <li>No import errors are shown if you try to load loose TRK-on-TRK inventory</li>
    * <li>An inventory with serial number XXX exists where there was a hole in our data</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */

   @Test
   public void testACFTWithHoleImportsSuccessfully() throws Exception {

      int lRandom = getRandom();

      String lParentSerialNo = "TT-AIRCRAFT" + lRandom;

      // create task map
      Map<String, String> lMapInventoryParent = new LinkedHashMap<String, String>();
      lMapInventoryParent.put( "SERIAL_NO_OEM", "'" + lParentSerialNo + "'" );
      lMapInventoryParent.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventoryParent.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventoryParent.put( "MANUFACT_CD", "'10001'" );
      lMapInventoryParent.put( "LOC_CD", "'OPS'" );
      lMapInventoryParent.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventoryParent.put( "ASSMBL_CD", "'ACFT_CD1'" );
      lMapInventoryParent.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventoryParent ) );

      // create task map
      Map<String, String> lInventoryUsage = new LinkedHashMap<String, String>();
      lInventoryUsage.put( "SERIAL_NO_OEM", "'" + lParentSerialNo + "'" );
      lInventoryUsage.put( "PART_NO_OEM", "'A0000005'" );
      lInventoryUsage.put( "MANUFACT_CD", "'10001'" );
      lInventoryUsage.put( "DATA_TYPE_CD", "'HOURS'" );
      lInventoryUsage.put( "TSN_QT", "1" );
      lInventoryUsage.put( "TSO_QT", "1" );
      lInventoryUsage.put( "TSI_QT", "1" );

      validateAndCheckInventory( "testACFTWithHoleImportsSuccessfully", "PASS" );
      importInventory();

      // expected child create task map
      Map<String, String> lMapInventoryChild = new LinkedHashMap<String, String>();
      lMapInventoryChild.put( "PARENT_SERIAL_NO_OEM", "'" + lParentSerialNo + "'" );
      lMapInventoryChild.put( "PARENT_PART_NO_OEM", "'A0000005'" );
      lMapInventoryChild.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventoryChild.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-TRK-CHILD'" );
      lMapInventoryChild.put( "EQP_POS_CD", "'1'" );
      lMapInventoryChild.put( "SERIAL_NO_OEM", "'XXX'" );
      lMapInventoryChild.put( "PART_NO_OEM", "'A0000006'" );
      lMapInventoryChild.put( "MANUFACT_CD", "'11111'" );
      lMapInventoryChild.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventoryChild.put( "INV_COND_CD", "'INSRV'" );

      Map<String, String> lChildUsage = new LinkedHashMap<String, String>();
      lChildUsage.put( "PARENT_SERIAL_NO_OEM", "'" + lParentSerialNo + "'" );
      lChildUsage.put( "PARENT_PART_NO_OEM", "'A0000005'" );
      lChildUsage.put( "PARENT_MANUFACT_CD", "'10001'" );
      lChildUsage.put( "SERIAL_NO_OEM", "XXX" );
      lChildUsage.put( "PART_NO_OEM", "'A0000006'" );
      lChildUsage.put( "MANUFACT_CD", "'11111'" );
      lChildUsage.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-TRK-CHILD'" );
      lChildUsage.put( "DATA_TYPE_CD", "'HOURS'" );
      lChildUsage.put( "TSN_QT", "0" );
      lChildUsage.put( "TSO_QT", "0" );
      lChildUsage.put( "TSI_QT", "0" );

      checkChildCurrentUsage( lChildUsage );

      lChildUsage.put( "DATA_TYPE_CD", "'CYCLES'" );
      checkChildCurrentUsage( lChildUsage );

      lChildUsage.put( "DATA_TYPE_CD", "'USAGE1'" );
      checkChildCurrentUsage( lChildUsage );

      lChildUsage.put( "DATA_TYPE_CD", "'USAGE2'" );
      checkChildCurrentUsage( lChildUsage );

      lChildUsage.put( "DATA_TYPE_CD", "'USAGE3'" );
      checkChildCurrentUsage( lChildUsage );

   }


   /**
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Insert Aircraft and will verify all system slots are created in DB</li>
    * <li>Run Inventory Import</li>
    * <li>complete.assy.bool set to 'FULL', all system slot should be created
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: NONE</li>
    * <li>No import errors are shown.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */

   @Test
   public void testSystemSlotsWhenImportingAircraft_Full() throws Exception {

      int lRandom = getRandom();

      String lSerialNo = "AIRCRAFT" + lRandom;

      // create task map
      Map<String, String> lMapInventoryAcft = new LinkedHashMap<String, String>();
      lMapInventoryAcft.put( "SERIAL_NO_OEM", "'ACFT-" + lSerialNo + "'" );
      lMapInventoryAcft.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventoryAcft.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventoryAcft.put( "MANUFACT_CD", "'10001'" );;
      lMapInventoryAcft.put( "LOC_CD", "'OPS'" );
      lMapInventoryAcft.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventoryAcft.put( "ASSMBL_CD", "'ACFT_CD1'" );
      lMapInventoryAcft.put( "AC_REG_CD", "'" + lRandom + "'" );
      lMapInventoryAcft.put( "INV_OPER_CD", "'NORM'" );
      lMapInventoryAcft.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventoryAcft.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert Aircraft map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventoryAcft ) );

      validateAndCheckInventory( "testACFTWithHoleImportsSuccessfully", "PASS" );
      runImportStageInventory( true ); // complete.assy.bool set to 'FULL'

      String lQuery = "Select * FROM inv_inv WHERE serial_no_oem ="
            + lMapInventoryAcft.get( "SERIAL_NO_OEM" );
      iSerialNoOem = getIDs( lQuery, "INV_NO_DB_ID", "INV_NO_ID" );

      int lBaselineCount =
            countRowsOfQuery( "Select Count(*) FROM eqp_assmbl_bom WHERE assmbl_cd = "
                  + lMapInventoryAcft.get( "ASSMBL_CD" ) + " AND bom_class_cd = 'SYS'" );
      int lActualsCount = countRowsOfQuery(
            "Select Count(*) FROM inv_inv WHERE inv_class_cd = 'SYS' AND h_inv_no_db_id = "
                  + iSerialNoOem.getNO_DB_ID() + " AND h_inv_no_id = " + iSerialNoOem.getNO_ID() );
      String lAssertMsg = "Incorrect number of System slots created, Expected Result: "
            + Integer.toString( lBaselineCount ) + ", Actual Result: "
            + Integer.toString( lActualsCount );
      Assert.assertEquals( lAssertMsg, lBaselineCount, lActualsCount );
   }


   /**
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Insert Aircraft and will verify all system slots are created in DB</li>
    * <li>Run Inventory Import</li>
    * <li>complete.assy.bool set to 'NOT', even though it is set to 'NOT' all system slot should be
    * created
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: NONE</li>
    * <li>No import errors are shown.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */

   @Test
   public void testSystemSlotsWhenImportingAircraft_Not() throws Exception {

      int lRandom = getRandom();

      String lSerialNo = "AIRCRAFT" + lRandom;

      // create inv map
      Map<String, String> lMapInventoryAcft = new LinkedHashMap<String, String>();
      lMapInventoryAcft.put( "SERIAL_NO_OEM", "'ACFT6-" + lSerialNo + "'" );
      lMapInventoryAcft.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventoryAcft.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventoryAcft.put( "MANUFACT_CD", "'10001'" );;
      lMapInventoryAcft.put( "LOC_CD", "'OPS'" );
      lMapInventoryAcft.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventoryAcft.put( "ASSMBL_CD", "'ACFT_CD1'" );
      lMapInventoryAcft.put( "AC_REG_CD", "'" + lRandom + "'" );
      lMapInventoryAcft.put( "INV_OPER_CD", "'NORM'" );
      lMapInventoryAcft.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventoryAcft.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert Aircraft map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventoryAcft ) );

      validateAndCheckInventory( "testACFTWithHoleImportsSuccessfully", "PASS" );
      runImportStageInventory( false ); // complete.assy.bool set to 'NOT'

      String lQuery = "SELECT * FROM inv_inv WHERE serial_no_oem ="
            + lMapInventoryAcft.get( "SERIAL_NO_OEM" );
      iSerialNoOem = getIDs( lQuery, "INV_NO_DB_ID", "INV_NO_ID" );

      int lBaselineCount =
            countRowsOfQuery( "Select Count(*) FROM eqp_assmbl_bom WHERE assmbl_cd = "
                  + lMapInventoryAcft.get( "ASSMBL_CD" ) + " AND bom_class_cd = 'SYS'" );
      int lActualsCount = countRowsOfQuery(
            "Select Count(*) FROM inv_inv WHERE inv_class_cd = 'SYS' AND h_inv_no_db_id = "
                  + iSerialNoOem.getNO_DB_ID() + " AND h_inv_no_id = " + iSerialNoOem.getNO_ID() );
      String lAssertMsg = "Incorrect number of System slots created, Expected Result: "
            + Integer.toString( lBaselineCount ) + ", Actual Result: "
            + Integer.toString( lActualsCount );
      Assert.assertEquals( lAssertMsg, lBaselineCount, lActualsCount );
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
}
