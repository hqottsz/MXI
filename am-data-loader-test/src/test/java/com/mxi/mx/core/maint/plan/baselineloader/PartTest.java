package com.mxi.mx.core.maint.plan.baselineloader;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import com.mxi.mx.core.maint.plan.datamodels.AssmblBomID;
import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.BaselineLoaderTest;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.TestConstants;


/**
 * This is a utility class for importing Parts through the baseline
 *
 */
public class PartTest extends BaselineLoaderTest {

   private List<ArrayList<String>> iPartIds;

   private simpleIDs iBomPart1;
   private simpleIDs iBomPart2;

   private AssmblBomID iBomPart1Ids;
   private AssmblBomID iBomPart2Ids;
   private AssmblBomID iBomPart3Ids;

   protected ArrayList<String> iClearsTables = new ArrayList<String>();
   {
      iClearsTables.add( "DELETE FROM " + TableUtil.C_PART_CONFIG );
      iClearsTables.add( "DELETE FROM " + TableUtil.C_DYN_PART_CONFIG );
   }


   /**
    * This will execute the part_import package for import
    *
    * @return the outcome of whether stored procedure was successful or not
    */
   protected int runImportPartConfig() {
      int lReturn = 0;

      try {
         CallableStatement lPrepareCallInventory = getConnection()
               .prepareCall( "BEGIN   part_import.import_part(on_retcode => ?); END;" );
         lPrepareCallInventory.registerOutParameter( 1, Types.INTEGER );
         lPrepareCallInventory.execute();
         commit();
         lReturn = lPrepareCallInventory.getInt( 1 );
      } catch ( SQLException e ) {
         e.printStackTrace();
      }
      return lReturn;
   }


   /**
    * This will execute the part_import package for validation
    *
    *
    * @return the outcome of whether stored procedure was successful or not
    */
   protected int runValidationPartConfig() {
      int lReturn = 0;

      try {
         CallableStatement lPrepareCallInventory = getConnection()
               .prepareCall( "BEGIN   part_import.validate_part(on_retcode => ?); END;" );
         lPrepareCallInventory.registerOutParameter( 1, Types.INTEGER );
         lPrepareCallInventory.execute();
         commit();
         lReturn = lPrepareCallInventory.getInt( 1 );
      } catch ( SQLException e ) {
         e.printStackTrace();
      }
      return lReturn;
   }


   /**
    * Used to set variables, so they can be used for verification of data in Mx tables
    *
    * @param iPartPrefix2
    * @param iBonCd1
    * @param iBonCd2
    * @param iPosPrefix2
    */
   protected void SetVariables( String aPartPrefix, String aBomCd1, String aBomCd2,
         String aPosPrefix, String aBomCd3 ) {

      // find all PartIds
      String lQuery =
            "SELECT part_no_db_id, part_no_id FROM eqp_part_no WHERE part_no_oem LIKE '737PART%' ORDER BY part_no_oem";
      String[] lIds = { "PART_NO_DB_ID", "PART_NO_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( lIds ) );
      iPartIds = execute( lQuery, lfields );

      // find all BomPartIds
      if ( aBomCd1 != null ) {
         lQuery = "SELECT BOM_PART_DB_ID, BOM_PART_ID FROM eqp_bom_part WHERE bom_part_cd = "
               + aBomCd1;
         iBomPart1 = getIDs( lQuery, "BOM_PART_DB_ID", "BOM_PART_ID" );
         lQuery =
               "SELECT ASSMBL_DB_ID, ASSMBL_CD, ASSMBL_BOM_ID FROM eqp_bom_part WHERE bom_part_cd = "
                     + aBomCd1;
         iBomPart1Ids = getIDs( lQuery, "ASSMBL_DB_ID", "ASSMBL_CD", "ASSMBL_BOM_ID" );
      }
      lQuery =
            "SELECT BOM_PART_DB_ID, BOM_PART_ID FROM eqp_bom_part WHERE bom_part_cd = " + aBomCd2;
      iBomPart2 = getIDs( lQuery, "BOM_PART_DB_ID", "BOM_PART_ID" );
      lQuery =
            "SELECT ASSMBL_DB_ID, ASSMBL_CD, ASSMBL_BOM_ID FROM eqp_bom_part WHERE bom_part_cd = "
                  + aBomCd2;
      iBomPart2Ids = getIDs( lQuery, "ASSMBL_DB_ID", "ASSMBL_CD", "ASSMBL_BOM_ID" );

      if ( aBomCd3 != null ) {
         lQuery =
               "SELECT ASSMBL_DB_ID, ASSMBL_CD, ASSMBL_BOM_ID FROM eqp_assmbl_bom WHERE assmbl_bom_cd = "
                     + aBomCd3;
         iBomPart3Ids = getIDs( lQuery, "ASSMBL_DB_ID", "ASSMBL_CD", "ASSMBL_BOM_ID" );
      }
   }


   /**
    * This will confirm all necessary information from the staging table is loaded into
    * EQP_PART_BASELINE
    *
    * @param aPartAppl
    *
    */
   protected void VerifyEqpPartBaseline( int aNumOfParts, String aBaselineCd, String aPartAppl ) {

      Map<String, String> lEqpPartBaselineMap = new LinkedHashMap<>();

      for ( int i = 0; i < aNumOfParts; i++ ) {
         lEqpPartBaselineMap.clear();
         if ( i == 0 ) {
            lEqpPartBaselineMap.put( "BOM_PART_DB_ID", iBomPart2.getNO_DB_ID() );
            lEqpPartBaselineMap.put( "BOM_PART_ID", iBomPart2.getNO_ID() );
         } else {
            lEqpPartBaselineMap.put( "BOM_PART_DB_ID", iBomPart1.getNO_DB_ID() );
            lEqpPartBaselineMap.put( "BOM_PART_ID", iBomPart1.getNO_ID() );
         }
         lEqpPartBaselineMap.put( "PART_NO_DB_ID", iPartIds.get( i ).get( 0 ) );
         lEqpPartBaselineMap.put( "PART_NO_ID", iPartIds.get( i ).get( 1 ) );
         if ( i == 0 )
            lEqpPartBaselineMap.put( "INTERCHG_ORD", "'" + 1 + "'" );
         else
            lEqpPartBaselineMap.put( "INTERCHG_ORD", "'" + i + "'" );
         lEqpPartBaselineMap.put( "PART_BASELINE_CD", "'" + aBaselineCd + i + "'" );
         lEqpPartBaselineMap.put( "APPL_EFF_LDESC", "'" + aPartAppl + i + "'" );
         if ( ( i < 2 ) || ( i > 20 ) )
            lEqpPartBaselineMap.put( "APPROVED_BOOL", "1" );
         else
            lEqpPartBaselineMap.put( "APPROVED_BOOL", "0" );

         Assert.assertTrue( "Verify values in EQP_PART_BASELINE: Row # " + i,
               runQueryReturnsOneRow( TableUtil.findRecordInDatabase( TableUtil.EQP_PART_BASELINE,
                     lEqpPartBaselineMap ) ) );
      }
   }


   /**
    * This will confirm all necessary information from the staging table is loaded into
    * EQP_ASSMBL_BOM
    *
    * @param lBomFuncCd
    *
    */
   protected void VerifyEqpAssmblBom( String aInvClassCd, String aInvClassCd2, String aBomCd1,
         String aBomCd2, String aBomPartName1, String aBomPartName2, String aPosCt1, String aPosCt2,
         String aBomFuncCd ) {

      Map<String, String> lEqpAssmblBomMap = new LinkedHashMap<>();

      if ( aInvClassCd.equals( "'TRK'" ) ) {
         lEqpAssmblBomMap.put( "ASSMBL_DB_ID", iBomPart1Ids.getDB_ID() );
         lEqpAssmblBomMap.put( "ASSMBL_CD", "'" + iBomPart1Ids.getCD() + "'" );
         lEqpAssmblBomMap.put( "ASSMBL_BOM_ID", iBomPart1Ids.getID() );
         lEqpAssmblBomMap.put( "NH_ASSMBL_DB_ID", iBomPart2Ids.getDB_ID() );
         lEqpAssmblBomMap.put( "NH_ASSMBL_CD", "'" + iBomPart2Ids.getCD() + "'" );
         lEqpAssmblBomMap.put( "NH_ASSMBL_BOM_ID", iBomPart2Ids.getID() );
         lEqpAssmblBomMap.put( "BOM_CLASS_CD", aInvClassCd );
         lEqpAssmblBomMap.put( "POS_CT", aPosCt1 );
         lEqpAssmblBomMap.put( "ASSMBL_BOM_CD", aBomCd1 );
         lEqpAssmblBomMap.put( "ASSMBL_BOM_FUNC_CD", aBomFuncCd );
         lEqpAssmblBomMap.put( "ASSMBL_BOM_NAME", aBomPartName1 );
         lEqpAssmblBomMap.put( "SOFTWARE_BOOL", "0" );
         lEqpAssmblBomMap.put( "ETOPS_BOOL", "0" );

         Assert.assertTrue( "Verify values in EQP_ASSMBL_BOM: Row #1", runQueryReturnsOneRow(
               TableUtil.findRecordInDatabase( TableUtil.EQP_ASSMBL_BOM, lEqpAssmblBomMap ) ) );
      }

      lEqpAssmblBomMap.clear();
      lEqpAssmblBomMap.put( "ASSMBL_DB_ID", iBomPart2Ids.getDB_ID() );
      lEqpAssmblBomMap.put( "ASSMBL_CD", "'" + iBomPart2Ids.getCD() + "'" );
      lEqpAssmblBomMap.put( "ASSMBL_BOM_ID", iBomPart2Ids.getID() );
      lEqpAssmblBomMap.put( "NH_ASSMBL_DB_ID", iBomPart3Ids.getDB_ID() );
      lEqpAssmblBomMap.put( "NH_ASSMBL_CD", "'" + iBomPart3Ids.getCD() + "'" );
      lEqpAssmblBomMap.put( "NH_ASSMBL_BOM_ID", iBomPart3Ids.getID() );
      lEqpAssmblBomMap.put( "BOM_CLASS_CD", aInvClassCd2 );
      lEqpAssmblBomMap.put( "POS_CT", aPosCt2 );
      lEqpAssmblBomMap.put( "ASSMBL_BOM_CD", aBomCd2 );
      lEqpAssmblBomMap.put( "ASSMBL_BOM_FUNC_CD", aBomFuncCd );
      lEqpAssmblBomMap.put( "ASSMBL_BOM_NAME", aBomPartName2 );
      lEqpAssmblBomMap.put( "SOFTWARE_BOOL", "1" );
      lEqpAssmblBomMap.put( "ETOPS_BOOL", "1" );

      Assert.assertTrue( "Verify values in EQP_ASSMBL_BOM: Row #2", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.EQP_ASSMBL_BOM, lEqpAssmblBomMap ) ) );
   }


   /**
    * This verify no rows were created in EQP_ASSMBL_BOM for Batch and serial parts
    *
    */
   protected void VerifyEqpAssmblBom( String aBomCd2 ) {

      Assert.assertTrue( "Verify no entries appear in EQP_ASSMBL_BOM: ", countRowsOfQuery(
            "Select * from EQP_ASSMBL_BOM where assmbl_bom_cd = " + aBomCd2 ) == 0 );

   }


   /**
    * This will confirm all necessary information from the staging table is loaded into
    * EQP_ASSMBL_POS
    *
    * @param aNumOfParts
    *           - The number of Parts being loaded
    *
    */
   protected void VerifyEqpAssmblPos( int aNumOfParts, String aPosPrefix ) {

      Map<String, String> lEqpAssmblPosMap = new LinkedHashMap<>();

      for ( int i = 0; i < aNumOfParts; i++ ) {
         lEqpAssmblPosMap.clear();
         if ( i == 0 ) {
            lEqpAssmblPosMap.put( "ASSMBL_DB_ID", iBomPart2Ids.getDB_ID() );
            lEqpAssmblPosMap.put( "ASSMBL_CD", "'" + iBomPart2Ids.getCD() + "'" );
            lEqpAssmblPosMap.put( "ASSMBL_BOM_ID", iBomPart2Ids.getID() );
            lEqpAssmblPosMap.put( "ASSMBL_POS_ID", "1" );
            lEqpAssmblPosMap.put( "NH_ASSMBL_DB_ID", iBomPart3Ids.getDB_ID() );
            lEqpAssmblPosMap.put( "NH_ASSMBL_CD", "'" + iBomPart3Ids.getCD() + "'" );
            lEqpAssmblPosMap.put( "NH_ASSMBL_BOM_ID", iBomPart3Ids.getID() );
         } else {
            lEqpAssmblPosMap.put( "ASSMBL_DB_ID", iBomPart1Ids.getDB_ID() );
            lEqpAssmblPosMap.put( "ASSMBL_CD", "'" + iBomPart1Ids.getCD() + "'" );
            lEqpAssmblPosMap.put( "ASSMBL_BOM_ID", iBomPart1Ids.getID() );
            lEqpAssmblPosMap.put( "ASSMBL_POS_ID", "'" + i + "'" );
            lEqpAssmblPosMap.put( "NH_ASSMBL_DB_ID", iBomPart2Ids.getDB_ID() );
            lEqpAssmblPosMap.put( "NH_ASSMBL_CD", "'" + iBomPart2Ids.getCD() + "'" );
            lEqpAssmblPosMap.put( "NH_ASSMBL_BOM_ID", iBomPart2Ids.getID() );
         }
         lEqpAssmblPosMap.put( "NH_ASSMBL_POS_ID", "1" );
         lEqpAssmblPosMap.put( "EQP_POS_CD", "'A" + i + "'" );

         Assert.assertTrue( "Verify values in EQP_ASSMBL_POS: Row # " + i, runQueryReturnsOneRow(
               TableUtil.findRecordInDatabase( TableUtil.EQP_ASSMBL_POS, lEqpAssmblPosMap ) ) );
         int lActualRowCount = countRowsOfQuery(
               "Select * from EQP_ASSMBL_POS where eqp_pos_cd LIKE '" + aPosPrefix + "%'" );
         Assert.assertTrue(
               "Verify # of Rows in EQP_ASSMBL_POS: Expected: " + aNumOfParts + " Actual: ",
               lActualRowCount == aNumOfParts );
      }
   }


   /**
    * This verify no rows were created in EQP_ASSMBL_POS for Batch and serial parts
    *
    */
   protected void VerifyEqpAssmblPos( String aPosPrefix ) {

      Assert.assertTrue( "Verify no entries appear in EQP_ASSMBL_POS: ", countRowsOfQuery(
            "Select * from EQP_ASSMBL_POS where eqp_pos_cd LIKE '" + aPosPrefix + "%'" ) == 0 );

   }


   /**
    * This will confirm all necessary information from the staging table is loaded into EQP_PART_NO
    *
    * @param lInvClassCd
    * @param iManufactCd
    *
    * @param i
    */
   protected void VerifyEqpPartNo( int aNumOfParts, String aManufactCd, String aInvClassCd,
         String aInvClassCd2, String aPartPrefix ) {

      Map<String, String> lEqpPartNoMap = new LinkedHashMap<>();

      for ( int i = 0; i < iPartIds.size(); i++ ) {
         lEqpPartNoMap.clear();
         lEqpPartNoMap.put( "PART_NO_DB_ID", iPartIds.get( i ).get( 0 ) );
         lEqpPartNoMap.put( "PART_NO_ID", iPartIds.get( i ).get( 1 ) );
         lEqpPartNoMap.put( "MANUFACT_CD", aManufactCd );
         if ( i == 0 )
            lEqpPartNoMap.put( "INV_CLASS_CD", aInvClassCd2 );
         else
            lEqpPartNoMap.put( "INV_CLASS_CD", aInvClassCd );
         lEqpPartNoMap.put( "PART_NO_SDESC", "'" + aPartPrefix + i + "'" );
         if ( i < 10 )
            lEqpPartNoMap.put( "PART_NO_OEM", "'" + aPartPrefix.toUpperCase() + "0" + i + "'" );
         else
            lEqpPartNoMap.put( "PART_NO_OEM", "'" + aPartPrefix.toUpperCase() + i + "'" );
         if ( i == 0 )
            lEqpPartNoMap.put( "ETOPS_BOOL", "0" ); // for value of Y
         else
            lEqpPartNoMap.put( "ETOPS_BOOL", "0" ); // for value of N

         Assert.assertTrue( "Verify values in EQP_PART_NO: Row # " + i, runQueryReturnsOneRow(
               TableUtil.findRecordInDatabase( TableUtil.EQP_PART_NO, lEqpPartNoMap ) ) );
      }
   }


   /**
    * This will confirm all necessary information from the staging table is loaded into EQP_BOM_PART
    *
    * @param aPartGroupAppl
    *
    */
   protected void VerifyEqpBomPart( String aInvClassCd, String aInvClassCd2, String aBomCd1,
         String aBomCd2, String aBomPartName1, String aBomPartName2, String aPartGroupAppl ) {

      Map<String, String> lEqpBomPartMap = new LinkedHashMap<>();

      if ( aBomCd1 != null ) {
         lEqpBomPartMap.put( "BOM_PART_DB_ID", iBomPart1.getNO_DB_ID() );
         lEqpBomPartMap.put( "BOM_PART_ID", iBomPart1.getNO_ID() );
         lEqpBomPartMap.put( "ASSMBL_DB_ID", iBomPart1Ids.getDB_ID() );
         lEqpBomPartMap.put( "ASSMBL_CD", "'" + iBomPart1Ids.getCD() + "'" );
         lEqpBomPartMap.put( "ASSMBL_BOM_ID", iBomPart1Ids.getID() );
         lEqpBomPartMap.put( "INV_CLASS_CD", aInvClassCd );
         lEqpBomPartMap.put( "LRU_BOOL", "1" );
         lEqpBomPartMap.put( "APPL_EFF_LDESC", aPartGroupAppl );
         lEqpBomPartMap.put( "BOM_PART_CD", aBomCd1 );
         lEqpBomPartMap.put( "BOM_PART_NAME", aBomPartName1 );

         Assert.assertTrue( "Verify values in EQP_BOM_PART: Row #1", runQueryReturnsOneRow(
               TableUtil.findRecordInDatabase( TableUtil.EQP_BOM_PART, lEqpBomPartMap ) ) );
      }

      lEqpBomPartMap.clear();
      lEqpBomPartMap.put( "BOM_PART_DB_ID", iBomPart2.getNO_DB_ID() );
      lEqpBomPartMap.put( "BOM_PART_ID", iBomPart2.getNO_ID() );
      lEqpBomPartMap.put( "ASSMBL_DB_ID", iBomPart2Ids.getDB_ID() );
      lEqpBomPartMap.put( "ASSMBL_CD", "'" + iBomPart2Ids.getCD() + "'" );
      lEqpBomPartMap.put( "ASSMBL_BOM_ID", iBomPart2Ids.getID() );
      lEqpBomPartMap.put( "INV_CLASS_CD", aInvClassCd2 );
      lEqpBomPartMap.put( "LRU_BOOL", "0" );
      lEqpBomPartMap.put( "APPL_EFF_LDESC", aPartGroupAppl );
      lEqpBomPartMap.put( "BOM_PART_CD", aBomCd2 );
      lEqpBomPartMap.put( "BOM_PART_NAME", aBomPartName2 );

      Assert.assertTrue( "Verify values in EQP_BOM_PART: Row #2", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.EQP_BOM_PART, lEqpBomPartMap ) ) );
   }


   /**
    * Calls check c_part error code
    *
    *
    */
   protected void checkPARTErrorCode( String aTestName, String aValidationCode ) {

      String lquery = TestConstants.C_PART_ERROR_CHECK;

      checkErrorCode( lquery, aTestName, aValidationCode );

   }

}
