package com.mxi.mx.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Assert;

import com.mxi.mx.core.maint.plan.actualsloader.inventory.imports.AbstractImportInventory;
import com.mxi.mx.core.maint.plan.datamodels.AssmblBomID;


/**
 * DOCUMENT_ME
 *
 */
public class AutoCompleteUtil extends AbstractImportInventory {

   protected static AssmblBomID iBlSysAPart = null;
   protected static AssmblBomID iBlSysBPart = null;
   protected static AssmblBomID iBlTrkCPart = null;
   protected static AssmblBomID iBlTrkDPart = null;
   protected static String iAcftID = null;
   protected static String iAcftDbId = "4650";


   /*-
    * These test scenarios will be referencing the Part Letters below in the baseline
    *
    * @Before is creating this tree of Data in this order:
    *  SYS (A) (ACTV)
    *   |-> SYS (B) (Inactive)
    *        |-> TRK (C) (non-mandatory)
    *             |-> TRK (D) (mandatory)
    *
    */
   public void setupBaseline() throws Exception {
      String lDbId;
      String lCd;
      String lId;
      // Find primary keys for baseline
      String lQuery =
            "SELECT eab.assmbl_db_id SYSA_DB_ID, eab.assmbl_cd SYSA_CD, eab.assmbl_bom_id SYSA_ID,"
                  + " nheab.assmbl_db_id SYSB_DB_ID, nheab.assmbl_cd SYSB_CD, nheab.assmbl_bom_id SYSB_ID,"
                  + " nheab2.assmbl_db_id TRKC_DB_ID, nheab2.assmbl_cd TRKC_CD, nheab2.assmbl_bom_id TRKC_ID, "
                  + " nheab3.assmbl_db_id TRKD_DB_ID, nheab3.assmbl_cd TRKD_CD, nheab3.assmbl_bom_id TRKD_ID "
                  + " FROM eqp_assmbl_bom eab" + " INNER JOIN eqp_assmbl_bom nheab ON"
                  + "      eab.assmbl_db_id = nheab.nh_assmbl_db_id AND"
                  + "      eab.assmbl_cd = nheab.nh_assmbl_cd AND"
                  + "      eab.assmbl_bom_id = nheab.nh_assmbl_bom_id"
                  + " INNER JOIN eqp_assmbl_bom nheab2 ON"
                  + "      nheab.assmbl_db_id = nheab2.nh_assmbl_db_id AND"
                  + "      nheab.assmbl_cd = nheab2.nh_assmbl_cd AND"
                  + "      nheab.assmbl_bom_id = nheab2.nh_assmbl_bom_id"
                  + " INNER JOIN eqp_assmbl_bom nheab3 ON"
                  + "      nheab2.assmbl_db_id = nheab3.nh_assmbl_db_id AND"
                  + "      nheab2.assmbl_cd = nheab3.nh_assmbl_cd AND"
                  + "      nheab2.assmbl_bom_id = nheab3.nh_assmbl_bom_id"
                  + " WHERE nheab.bom_class_cd = 'SYS' AND" + "      eab.bom_class_cd = 'SYS' AND"
                  + "      nheab2.bom_class_cd = 'TRK' AND"
                  + "      nheab3.bom_class_cd = 'TRK' AND"
                  + " ROWNUM = 1 AND eab.assmbl_cd='ACFT_CD1'";

      ResultSet lResults = runQuery( lQuery );
      lResults.next();
      // Storing SYS/TRK slots baseline's primary keys
      lDbId = lResults.getString( "SYSA_DB_ID" );
      lCd = lResults.getString( "SYSA_CD" );
      lId = lResults.getString( "SYSA_ID" );
      iBlSysAPart = new AssmblBomID( lDbId, lCd, lId );
      lDbId = lResults.getString( "SYSB_DB_ID" );
      lCd = lResults.getString( "SYSB_CD" );
      lId = lResults.getString( "SYSB_ID" );
      iBlSysBPart = new AssmblBomID( lDbId, lCd, lId );
      lDbId = lResults.getString( "TRKC_DB_ID" );
      lCd = lResults.getString( "TRKC_CD" );
      lId = lResults.getString( "TRKC_ID" );
      iBlTrkCPart = new AssmblBomID( lDbId, lCd, lId );
      lDbId = lResults.getString( "TRKD_DB_ID" );
      lCd = lResults.getString( "TRKD_CD" );
      lId = lResults.getString( "TRKD_ID" );
      iBlTrkDPart = new AssmblBomID( lDbId, lCd, lId );
      // Baseline Data Setup
      lQuery = "UPDATE eqp_assmbl_bom eab SET cfg_slot_status_cd = 'OBSOLETE'"
            + " WHERE assmbl_db_id = " + iBlSysBPart.getDB_ID() + " AND assmbl_cd = '"
            + iBlSysBPart.getCD() + "'" + " AND assmbl_bom_id = " + iBlSysBPart.getID();
      runUpdate( lQuery );
      lQuery = "UPDATE eqp_assmbl_bom eab SET mandatory_bool = 0                "
            + " WHERE assmbl_db_id = " + iBlTrkCPart.getDB_ID() + " AND assmbl_cd = '"
            + iBlTrkCPart.getCD() + "'" + "AND assmbl_bom_id = " + iBlTrkCPart.getID();
      runUpdate( lQuery );

   }


   public void restoreOriginalValues() {
      // restore Baseline values back to original values
      String lQuery = "UPDATE eqp_assmbl_bom eab SET cfg_slot_status_cd = 'ACTV'"
            + " WHERE assmbl_db_id = " + iBlSysBPart.getDB_ID() + " AND assmbl_cd = '"
            + iBlSysBPart.getCD() + "'" + " AND assmbl_bom_id = " + iBlSysBPart.getID();
      runUpdate( lQuery );
      lQuery = "UPDATE eqp_assmbl_bom eab SET mandatory_bool = 1               "
            + " WHERE assmbl_db_id = " + iBlTrkCPart.getDB_ID() + " AND assmbl_cd = '"
            + iBlTrkCPart.getCD() + "'" + "AND assmbl_bom_id = " + iBlTrkCPart.getID();
      runUpdate( lQuery );
      // Delete aircraft AND RELATED PARTS
      if ( iAcftID != null ) {
         lQuery = "DELETE from inv_inv Where (INV_NO_DB_ID = " + iAcftDbId + " AND"
               + " INV_NO_ID = " + iAcftID + ") OR (H_INV_NO_DB_ID = " + iAcftDbId + " AND"
               + " H_INV_NO_ID = " + iAcftID + ")";
         runUpdate( lQuery );

         // delete inv_ac_reg table
         lQuery = "DELETE from inv_ac_reg Where INV_NO_DB_ID = " + iAcftDbId + " AND"
               + " INV_NO_ID = " + iAcftID;
         runUpdate( lQuery );

         iAcftID = null;
      }

   }


   /**
    * Finds the part inv_inv table that was created related to the baseline
    *
    * @param aBlPart
    *           - Ids of the Baseline part
    * @return determine BlPart exists or not
    */
   protected boolean doesPartExist( AssmblBomID aBlPart ) {
      if ( iAcftID == null )
         iAcftID = getAcftID();
      String lQuery = "Select Count(*) FROM inv_inv WHERE" + " ASSMBL_DB_ID = " + aBlPart.getDB_ID()
            + " AND" + " ASSMBL_CD = '" + aBlPart.getCD() + "' AND" + " ASSMBL_BOM_ID = "
            + aBlPart.getID() + " AND" + " H_INV_NO_ID = " + iAcftID;
      int lRowCount = countRowsOfQuery( lQuery );
      if ( lRowCount == 0 )
         return false;
      else
         return true;
   }


   // /**
   // * This will find the aircraft IDs
   // *
   // * @return IDs for the aircraft using simpleIDs class
   // */
   // protected String getAcftID() {
   // String lQuery = "SELECT inv_inv.INV_NO_ID FROM inv_inv" + " INNER JOIN al_proc_inv_keys ON"
   // + " inv_inv.inv_no_id = al_proc_inv_keys.inv_no_id"
   // + " WHERE inv_inv.inv_class_cd = 'ACFT'";
   // ResultSet lResults;
   // String lId = null;
   // try {
   // lResults = runQuery( lQuery );
   // lResults.next();
   // Assert.assertTrue( "Only supposed to be one Aircraft Loaded", lResults.isLast() );
   // lId = lResults.getString( "INV_NO_ID" );
   // } catch ( SQLException e ) {
   // Assert.assertTrue( "Find aircraft query failed", false );
   // e.printStackTrace();
   // }
   // return lId;
   // }

   /**
    * This will find the aircraft IDs
    *
    * @return IDs for the aircraft using simpleIDs class
    */
   protected String getAcftID() {
      String lQuery = "SELECT inv_inv.INV_NO_ID FROM inv_inv " + "inner join c_ri_inventory on "
            + "c_ri_inventory.serial_no_oem = inv_inv.serial_no_oem "
            + "WHERE inv_inv.inv_class_cd = 'ACFT'";
      ResultSet lResults;
      String lId = null;
      try {
         lResults = runQuery( lQuery );
         lResults.next();
         Assert.assertTrue( "Only supposed to be one Aircraft Loaded", lResults.isLast() );
         lId = lResults.getString( "INV_NO_ID" );
      } catch ( SQLException e ) {
         Assert.assertTrue( "Find aircraft query failed", false );
         e.printStackTrace();
      }
      return lId;
   }

}
