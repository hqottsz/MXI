/*
 * Confidential, proprietary and/or trade secret information of Mxi Technologies, Ltd.
 *
 * Copyright 2000-2017 Mxi Technologies, Ltd. All Rights Reserved.
 *
 * Except as expressly provided by written license signed by a duly appointed officer of Mxi
 * Technologies, Ltd., any disclosure, distribution, reproduction, compilation, modification,
 * creation of derivative works and/or other use of the Mxi source code is strictly prohibited.
 * Inclusion of a copyright notice shall not be taken to indicate that the source code has been
 * published.
 */

package com.mxi.mx.util;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Assert;

import com.mxi.mx.core.maint.plan.datamodels.AssmblBomID;
import com.mxi.mx.core.maint.plan.datamodels.assmblBOM;
import com.mxi.mx.core.maint.plan.datamodels.assmblBOMATA;
import com.mxi.mx.core.maint.plan.datamodels.assmblPART;
import com.mxi.mx.core.maint.plan.datamodels.assmblPOS;
import com.mxi.mx.core.maint.plan.datamodels.assmble;
import com.mxi.mx.core.maint.plan.datamodels.dataSource;
import com.mxi.mx.core.maint.plan.datamodels.fnc_account;
import com.mxi.mx.core.maint.plan.datamodels.fnc_tcode;
import com.mxi.mx.core.maint.plan.datamodels.mannufact;
import com.mxi.mx.core.maint.plan.datamodels.partInfo;
import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.core.maint.plan.datamodels.taskIDs;


/**
 * This test suite contains common test utilities for Baseline Loader tests.
 *
 * @author Alicia Qian
 */

public abstract class BaselineLoaderTest extends AbstractDatabaseConnection {

   /**
    * Clean up after each individual test
    *
    * @throws Exception
    */
   @After
   @Override
   public void after() {

      try {
         clearBaselineLoaderTables();
         super.after();
      } catch ( Exception e ) {
         e.printStackTrace();
      }
   }


   /**
    * Check Finance Account and staging Finance Account are equal
    *
    * @return void
    */

   public void compareRowFinanceAccount() {

      ArrayList<fnc_account> fncaccount = new ArrayList<fnc_account>();
      ArrayList<fnc_account> c_fncaccount = new ArrayList<fnc_account>();

      StringBuilder fncquery = new StringBuilder();
      fncquery.append( "select ACCOUNT_TYPE_CD, ACCOUNT_CD, ACCOUNT_SDESC from fnc_account " );
      fncquery.append( "where ACCOUNT_CD like \'AUTO%\'" );

      StringBuilder c_fncquery = new StringBuilder();
      c_fncquery.append( "select ACCOUNT_TYPE_CD, ACCOUNT_CD, ACCOUNT_NAME from c_fnc_account " );
      c_fncquery.append( "where ACCOUNT_CD like \'AUTO%\'" );

      ResultSet fncResultSetRecords;
      ResultSet c_fncResultSetRecords;
      try {
         fncResultSetRecords = runQuery( fncquery.toString() );
         while ( fncResultSetRecords.next() ) {
            fnc_account fncAC = new fnc_account( fncResultSetRecords.getString( "ACCOUNT_TYPE_CD" ),
                  fncResultSetRecords.getString( "ACCOUNT_CD" ),
                  fncResultSetRecords.getString( "ACCOUNT_SDESC" ) );
            fncaccount.add( fncAC );

         }

         c_fncResultSetRecords = runQuery( c_fncquery.toString() );
         while ( c_fncResultSetRecords.next() ) {
            fnc_account fncAC =
                  new fnc_account( c_fncResultSetRecords.getString( "ACCOUNT_TYPE_CD" ),
                        c_fncResultSetRecords.getString( "ACCOUNT_CD" ),
                        c_fncResultSetRecords.getString( "ACCOUNT_NAME" ) );
            c_fncaccount.add( fncAC );
         }

      } catch ( SQLException e ) {
         e.printStackTrace();
      } finally {
         checkArraysEqual( fncaccount, c_fncaccount );
         checkArraysEqual( c_fncaccount, fncaccount );

      }

   }


   /**
    * Check tcode and staging tcode tables are equal
    *
    * @return void
    */

   public void compareRowFinanceTCode() {
      ArrayList<fnc_tcode> fncTcode = new ArrayList<fnc_tcode>();
      ArrayList<fnc_tcode> c_fncTcode = new ArrayList<fnc_tcode>();

      StringBuilder fncquery = new StringBuilder();
      fncquery.append( "select TCODE_CD from fnc_tcode " );
      fncquery.append( "where TCODE_CD like \'AUTO%\'" );

      StringBuilder c_fncquery = new StringBuilder();
      c_fncquery.append( "select TCODE_CD from c_fnc_tcode " );
      c_fncquery.append( "where TCODE_CD like \'AUTO%\'" );

      ResultSet fncResultSetRecords;
      ResultSet c_fncResultSetRecords;
      try {
         fncResultSetRecords = runQuery( fncquery.toString() );
         while ( fncResultSetRecords.next() ) {
            fnc_tcode fncTCD = new fnc_tcode( fncResultSetRecords.getString( "TCODE_CD" ) );
            fncTcode.add( fncTCD );

         }

         c_fncResultSetRecords = runQuery( c_fncquery.toString() );
         while ( c_fncResultSetRecords.next() ) {
            fnc_tcode c_fncTCD = new fnc_tcode( c_fncResultSetRecords.getString( "TCODE_CD" ) );
            c_fncTcode.add( c_fncTCD );
         }

      } catch ( SQLException e ) {
         e.printStackTrace();
      } finally {
         checkArraysEqual( fncTcode, c_fncTcode );
         checkArraysEqual( c_fncTcode, fncTcode );

      }

   }


   public void compareRowEQPMANUFACTUREMAP() {
      ArrayList<mannufact> MNFCT = new ArrayList<mannufact>();
      ArrayList<mannufact> cMNFCT = new ArrayList<mannufact>();

      StringBuilder fncquery = new StringBuilder();
      fncquery.append( "select MANUFACT_CD, MANUFACT_NAME from EQP_MANUFACT " );
      fncquery.append( "where MANUFACT_CD like \'AUTO%\'" );

      StringBuilder c_fncquery = new StringBuilder();
      c_fncquery.append( "select MANUFACT_CD, MANUFACT_NAME from c_eqp_manufact_map " );
      c_fncquery.append( "where MANUFACT_CD like \'AUTO%\'" );

      ResultSet fncResultSetRecords;
      ResultSet c_fncResultSetRecords;
      try {
         fncResultSetRecords = runQuery( fncquery.toString() );
         while ( fncResultSetRecords.next() ) {
            mannufact manfct = new mannufact( fncResultSetRecords.getString( "MANUFACT_CD" ),
                  fncResultSetRecords.getString( "MANUFACT_NAME" ) );
            MNFCT.add( manfct );

         }

         c_fncResultSetRecords = runQuery( c_fncquery.toString() );
         while ( c_fncResultSetRecords.next() ) {
            mannufact manfct = new mannufact( c_fncResultSetRecords.getString( "MANUFACT_CD" ),
                  c_fncResultSetRecords.getString( "MANUFACT_NAME" ) );
            cMNFCT.add( manfct );
         }

      } catch ( SQLException e ) {
         e.printStackTrace();
      } finally {
         checkArraysEqual( MNFCT, cMNFCT );
         checkArraysEqual( cMNFCT, MNFCT );

      }

   }


   /**
    * Check 2 arrays of object are equal.
    *
    * @Parametes: Array, Array
    *
    * @return void
    */

   @Override
   public void checkArraysEqual( ArrayList aArray, ArrayList bArray ) {
      // Assert.assertTrue( aArray.size() == bArray.size() );
      boolean found = false;
      int count = aArray.size();
      int countb = bArray.size();

      for ( int i = 0; i < count; i++ ) {
         for ( int j = 0; j < countb; j++ ) {
            if ( aArray.get( i ).equals( bArray.get( j ) ) ) {
               found = true;
               break;
            } else {
               found = false;
            }
         }
         Assert.assertTrue( found );

      }

      // Assert.assertTrue( found );

   }


   /**
    * Runs the provided query on the database
    *
    * @param aTable
    *           Query to be run
    *
    * @return Returns the string query result
    */
   protected int getErrorCodeCount( String strCol, String aTable ) {

      // prepare query for checking test results
      String lQuery = "select count(*) as \"COUNT\" from " + aTable + " where " + strCol
            + " IS NOT NULL and  " + strCol + " != ''";

      try {

         PreparedStatement lStatement = getConnection().prepareStatement( lQuery,
               ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
         ResultSet lResultSet = lStatement.executeQuery( lQuery );
         lResultSet.next();
         return lResultSet.getInt( "COUNT" );

      } catch ( SQLException e ) {
         e.printStackTrace();

         return 0;
      }
   }


   /**
    * Gets a list of the errors that are present in the table passed in
    *
    * @param aTableName
    *           the name of the table to get errors from
    *
    * @throws Exception
    *            if there is an error
    */
   protected String getLoadErrors( String aTableName ) throws Exception {
      StringBuilder lQueryErrorList = new StringBuilder();
      lQueryErrorList.append( "SELECT result_cd FROM " );
      lQueryErrorList.append( aTableName );
      lQueryErrorList.append( " WHERE result_cd IS NOT NULL" );
      PreparedStatement lPs = getConnection().prepareStatement( lQueryErrorList.toString() );

      ResultSet lQueryResults = lPs.executeQuery();
      String lResults = "";
      while ( lQueryResults.next() ) {

         lResults += lQueryResults.getString( "result_cd" );
         lResults += ( ", " );
      }
      lResults = lResults.substring( 0, lResults.length() - 1 ); // trims that last comma
      return lResults;
   }


   /**
    * Check C_ASSMBL_LIST and EQP_ASSMBL are equal
    *
    * @return void
    */

   public void compareEQPAssmblAndAssmblList() {

      ArrayList<assmble> assmblelist = new ArrayList<assmble>();
      ArrayList<assmble> c_assmblelist = new ArrayList<assmble>();

      StringBuilder assmblquery = new StringBuilder();
      assmblquery.append( "select ASSMBL_CD, ASSMBL_NAME, ASSMBL_CLASS_CD from EQP_ASSMBL " );
      assmblquery.append( "where ASSMBL_CD like '%_AT1'" );

      StringBuilder c_assmblquery = new StringBuilder();
      c_assmblquery.append( "select ASSMBL_CD, ASSMBL_NAME, ASSMBL_CLASS_CD from C_ASSMBL_LIST " );
      c_assmblquery.append( "where ASSMBL_CD like '%_AT1'" );

      ResultSet fncResultSetRecords;
      ResultSet c_fncResultSetRecords;
      try {
         fncResultSetRecords = runQuery( assmblquery.toString() );
         while ( fncResultSetRecords.next() ) {
            assmble assmble = new assmble( fncResultSetRecords.getString( "ASSMBL_CD" ),
                  fncResultSetRecords.getString( "ASSMBL_NAME" ),
                  fncResultSetRecords.getString( "ASSMBL_CLASS_CD" ) );
            assmblelist.add( assmble );

         }

         c_fncResultSetRecords = runQuery( c_assmblquery.toString() );
         while ( c_fncResultSetRecords.next() ) {
            assmble assmble = new assmble( c_fncResultSetRecords.getString( "ASSMBL_CD" ),
                  c_fncResultSetRecords.getString( "ASSMBL_NAME" ),
                  c_fncResultSetRecords.getString( "ASSMBL_CLASS_CD" ) );
            c_assmblelist.add( assmble );
         }

      } catch ( SQLException e ) {
         e.printStackTrace();
      } finally {
         checkArraysEqual( assmblelist, c_assmblelist );

      }

   }


   /**
    * Check C_ASSMBL_LIST and EQP_ASSMBL_BOM are equal
    *
    * @return void
    */

   public void compareEQPAssmblBomAndAssmblList() {

      ArrayList<assmblBOM> assmbleBOMlist = new ArrayList<assmblBOM>();
      ArrayList<assmblBOM> c_assmblelist = new ArrayList<assmblBOM>();

      StringBuilder assmblquery = new StringBuilder();
      assmblquery.append(
            "select ASSMBL_CD,ASSMBL_BOM_CD, ASSMBL_BOM_NAME, ASSMBL_BOM_ID,BOM_CLASS_CD,NH_ASSMBL_DB_ID, nh_assmbl_cd,nh_assmbl_bom_id from EQP_ASSMBL_BOM " );
      assmblquery.append( "where ASSMBL_CD like '%_AT1'" );

      StringBuilder c_assmblquery = new StringBuilder();
      c_assmblquery.append( "select ASSMBL_CD, ASSMBL_NAME from C_ASSMBL_LIST " );
      c_assmblquery.append( "where ASSMBL_CD like '%_AT1'" );

      ResultSet assbomResultSetRecords;
      ResultSet c_assbomResultSetRecords;
      try {
         assbomResultSetRecords = runQuery( assmblquery.toString() );
         while ( assbomResultSetRecords.next() ) {
            assmblBOM assmblebom = new assmblBOM( assbomResultSetRecords.getString( "ASSMBL_CD" ),
                  assbomResultSetRecords.getString( "ASSMBL_CD" ),
                  assbomResultSetRecords.getString( "ASSMBL_BOM_NAME" ),
                  assbomResultSetRecords.getString( "ASSMBL_BOM_ID" ),
                  assbomResultSetRecords.getString( "BOM_CLASS_CD" ),
                  assbomResultSetRecords.getString( "NH_ASSMBL_DB_ID" ),
                  assbomResultSetRecords.getString( "NH_ASSMBL_CD" ),
                  assbomResultSetRecords.getString( "NH_ASSMBL_BOM_ID" ) );
            assmbleBOMlist.add( assmblebom );

         }

         c_assbomResultSetRecords = runQuery( c_assmblquery.toString() );
         while ( c_assbomResultSetRecords.next() ) {
            assmblBOM assmblebom = new assmblBOM( c_assbomResultSetRecords.getString( "ASSMBL_CD" ),
                  c_assbomResultSetRecords.getString( "ASSMBL_CD" ),
                  c_assbomResultSetRecords.getString( "ASSMBL_NAME" ), "0", "ROOT", null, null,
                  null );
            c_assmblelist.add( assmblebom );
         }

      } catch ( SQLException e ) {
         e.printStackTrace();
      } finally {
         checkArraysEqual( assmbleBOMlist, c_assmblelist );

      }

   }


   /**
    * Check C_ASSMBL_LIST and EQP_ASSMBL_POS are equal
    *
    * @return void
    */

   public void compareEQPAssmblPosAndAssmblList() {

      ArrayList<assmblPOS> assmblePOSlist = new ArrayList<assmblPOS>();
      ArrayList<assmblPOS> c_assmblePOSlist = new ArrayList<assmblPOS>();

      StringBuilder assmblquery = new StringBuilder();
      assmblquery.append(
            "select ASSMBL_CD,ASSMBL_BOM_ID, ASSMBL_POS_ID, EQP_POS_CD from EQP_ASSMBL_POS " );
      assmblquery.append( "where ASSMBL_CD like '%_AT1'" );

      StringBuilder c_assmblquery = new StringBuilder();
      c_assmblquery.append( "select ASSMBL_CD from C_ASSMBL_LIST " );
      c_assmblquery.append( "where ASSMBL_CD like '%_AT1'" );

      ResultSet assPosResultSetRecords;
      ResultSet c_assPosResultSetRecords;
      try {
         assPosResultSetRecords = runQuery( assmblquery.toString() );
         while ( assPosResultSetRecords.next() ) {
            assmblPOS assmblepos = new assmblPOS( assPosResultSetRecords.getString( "ASSMBL_CD" ),
                  assPosResultSetRecords.getString( "ASSMBL_BOM_ID" ),
                  assPosResultSetRecords.getString( "ASSMBL_POS_ID" ),
                  assPosResultSetRecords.getString( "EQP_POS_CD" ) );
            assmblePOSlist.add( assmblepos );

         }

         c_assPosResultSetRecords = runQuery( c_assmblquery.toString() );
         while ( c_assPosResultSetRecords.next() ) {
            assmblPOS assmblepos =
                  new assmblPOS( c_assPosResultSetRecords.getString( "ASSMBL_CD" ), "0", "1", "1" );
            c_assmblePOSlist.add( assmblepos );
         }

      } catch ( SQLException e ) {
         e.printStackTrace();
      } finally {
         checkArraysEqual( assmblePOSlist, c_assmblePOSlist );

      }
   }


   /**
    * Check C_ASSMBL_LIST and EQP_ASSMBL_POS are equal
    *
    * @return void
    */

   public void compareEQPBOMPartAndAssmblList( String strAssmblyType ) {

      ArrayList<assmblPART> assmblePartlist = new ArrayList<assmblPART>();
      ArrayList<assmblPART> c_assmblePartlist = new ArrayList<assmblPART>();

      StringBuilder assmblquery = new StringBuilder();
      assmblquery.append(
            "select ASSMBL_CD,BOM_PART_CD, BOM_PART_NAME, ASSMBL_BOM_ID, INV_CLASS_CD from EQP_BOM_PART " );
      assmblquery.append( "where ASSMBL_CD like '%_AT1'" );

      StringBuilder c_assmblquery = new StringBuilder();
      c_assmblquery.append( "select ASSMBL_CD, ASSMBL_NAME from C_ASSMBL_LIST " );
      c_assmblquery.append( "where ASSMBL_CD like '%_AT1'" );

      ResultSet assPartResultSetRecords;
      ResultSet c_assPartResultSetRecords;
      try {
         assPartResultSetRecords = runQuery( assmblquery.toString() );

         while ( assPartResultSetRecords.next() ) {
            assmblPART assmblepart =
                  new assmblPART( assPartResultSetRecords.getString( "ASSMBL_CD" ),
                        assPartResultSetRecords.getString( "ASSMBL_CD" ),
                        assPartResultSetRecords.getString( "BOM_PART_NAME" ),
                        assPartResultSetRecords.getString( "ASSMBL_BOM_ID" ),
                        assPartResultSetRecords.getString( "INV_CLASS_CD" ) );
            assmblePartlist.add( assmblepart );

         }

         c_assPartResultSetRecords = runQuery( c_assmblquery.toString() );
         while ( c_assPartResultSetRecords.next() ) {
            assmblPART assmblepart =
                  new assmblPART( c_assPartResultSetRecords.getString( "ASSMBL_CD" ),
                        c_assPartResultSetRecords.getString( "ASSMBL_CD" ),
                        c_assPartResultSetRecords.getString( "ASSMBL_NAME" ), "0", strAssmblyType );
            c_assmblePartlist.add( assmblepart );
         }

      } catch ( SQLException e ) {
         e.printStackTrace();
      } finally {
         checkArraysEqual( assmblePartlist, c_assmblePartlist );

      }

   }


   /**
    * Check C_ASSMBL_LIST and EQP_DATA_SOURCE are equal
    *
    * @return void
    */

   public void compareEQPDataSourceAndAssmblList() {

      dataSource dtsource = null;
      dataSource assmbledtsource = null;

      StringBuilder assmblquery = new StringBuilder();
      assmblquery
            .append( "select ASSMBL_CD,DATA_SOURCE_DB_ID, DATA_SOURCE_CD from EQP_DATA_SOURCE " );
      assmblquery.append( "where ASSMBL_CD like '%_AT1'" );

      StringBuilder c_assmblquery = new StringBuilder();
      c_assmblquery.append( "select ASSMBL_CD from C_ASSMBL_LIST " );
      c_assmblquery.append( "where ASSMBL_CD like '%_AT1'" );

      ResultSet assDataSourceResultSetRecords;
      ResultSet c_assDataSourceResultSetRecords;
      try {
         assDataSourceResultSetRecords = runQuery( assmblquery.toString() );
         assDataSourceResultSetRecords.next();
         dtsource = new dataSource( assDataSourceResultSetRecords.getString( "ASSMBL_CD" ),
               assDataSourceResultSetRecords.getString( "DATA_SOURCE_DB_ID" ),
               assDataSourceResultSetRecords.getString( "DATA_SOURCE_CD" ) );

         c_assDataSourceResultSetRecords = runQuery( c_assmblquery.toString() );
         c_assDataSourceResultSetRecords.next();
         assmbledtsource = new dataSource( c_assDataSourceResultSetRecords.getString( "ASSMBL_CD" ),
               "10", "BULK" );

      } catch ( SQLException e ) {
         e.printStackTrace();
      } finally {
         Assert.assertTrue( dtsource.equals( assmbledtsource ) );

      }
   }


   /**
    * Check C_ATA_SYS and EQP_ASSMBL_BOM are equal
    *
    * @return void
    */

   public void compareEQPAssmblBomAndATAassmblList() {

      ArrayList<assmblBOMATA> assmbleBOMlist = new ArrayList<assmblBOMATA>();
      ArrayList<assmblBOMATA> c_assmblelist = new ArrayList<assmblBOMATA>();

      StringBuilder assmblquery = new StringBuilder();
      assmblquery.append(
            "select ASSMBL_CD,ASSMBL_BOM_ID, NH_ASSMBL_CD, NH_ASSMBL_BOM_ID, BOM_CLASS_CD, " );
      assmblquery.append(
            "ASSMBL_BOM_CD, ASSMBL_BOM_NAME, NH_ASSMBL_DB_ID, NH_ASSMBL_BOM_ID from EQP_ASSMBL_BOM " );
      assmblquery.append( "where ASSMBL_CD like '%_AT1' and BOM_CLASS_CD<>'ROOT'" );

      StringBuilder c_assmblquery = new StringBuilder();
      c_assmblquery.append( "select ASSMBL_CD, ATA_SYS_CD, ATA_SYS_NAME from C_ATA_SYS " );
      c_assmblquery.append( "where ASSMBL_CD like '%_AT1'" );

      ResultSet assbomResultSetRecords;
      ResultSet c_assbomResultSetRecords;
      try {
         assbomResultSetRecords = runQuery( assmblquery.toString() );
         while ( assbomResultSetRecords.next() ) {
            assmblBOMATA assmblebom =
                  new assmblBOMATA( assbomResultSetRecords.getString( "ASSMBL_CD" ),
                        assbomResultSetRecords.getString( "NH_ASSMBL_CD" ),
                        assbomResultSetRecords.getString( "ASSMBL_BOM_CD" ),
                        assbomResultSetRecords.getString( "ASSMBL_BOM_NAME" ),
                        assbomResultSetRecords.getString( "BOM_CLASS_CD" ),
                        assbomResultSetRecords.getString( "NH_ASSMBL_DB_ID" ),
                        assbomResultSetRecords.getString( "NH_ASSMBL_BOM_ID" ) );

            assmbleBOMlist.add( assmblebom );
         }

         c_assbomResultSetRecords = runQuery( c_assmblquery.toString() );
         while ( c_assbomResultSetRecords.next() ) {

            assmblBOMATA assmblebom =
                  new assmblBOMATA( c_assbomResultSetRecords.getString( "ASSMBL_CD" ),
                        c_assbomResultSetRecords.getString( "ASSMBL_CD" ),
                        c_assbomResultSetRecords.getString( "ATA_SYS_CD" ),
                        c_assbomResultSetRecords.getString( "ATA_SYS_NAME" ), "SYS", "4650", null );

            String lstrASSMBL_BOM_CD = assmblebom.getASSMBL_BOM_CD();
            switch ( lstrASSMBL_BOM_CD ) {
               case "SYS-1":
                  assmblebom.setNH_ASSMBL_BOM_ID( "0" );
                  break;
               case "SYS-1-1":
                  assmblebom.setNH_ASSMBL_BOM_ID( "1" );
                  break;
               case "SYS-1-2":
                  assmblebom.setNH_ASSMBL_BOM_ID( "1" );
                  break;
               case "SYS-2":
                  assmblebom.setNH_ASSMBL_BOM_ID( "0" );
                  break;
               case "SYS-2-1":
                  assmblebom.setNH_ASSMBL_BOM_ID( "4" );
                  break;
               case "SYS-2-1-1":
                  assmblebom.setNH_ASSMBL_BOM_ID( "5" );
                  break;
               case "SYS-2-2":
                  assmblebom.setNH_ASSMBL_BOM_ID( "4" );
                  break;
               case "SYS-APU":
                  assmblebom.setNH_ASSMBL_BOM_ID( "0" );
                  break;
               case "SYS-ENG":
                  assmblebom.setNH_ASSMBL_BOM_ID( "0" );
                  break;
               case "APU-SYS-1":
                  assmblebom.setNH_ASSMBL_BOM_ID( "0" );
                  break;
               case "APU-SYS-1-1":
                  assmblebom.setNH_ASSMBL_BOM_ID( "1" );
                  break;
               case "APU-SYS-1-2":
                  assmblebom.setNH_ASSMBL_BOM_ID( "1" );
                  break;
               case "APU-SYS-2":
                  assmblebom.setNH_ASSMBL_BOM_ID( "0" );
                  break;
               case "APU-SYS-2-1":
                  assmblebom.setNH_ASSMBL_BOM_ID( "4" );
                  break;
               case "APU-SYS-2-1-1":
                  assmblebom.setNH_ASSMBL_BOM_ID( "5" );
                  break;
               case "APU-SYS-2-2":
                  assmblebom.setNH_ASSMBL_BOM_ID( "4" );
                  break;
               case "ENG-SYS-1":
                  assmblebom.setNH_ASSMBL_BOM_ID( "0" );
                  break;
               case "ENG-SYS-1-1":
                  assmblebom.setNH_ASSMBL_BOM_ID( "1" );
                  break;
               case "ENG-SYS-1-2":
                  assmblebom.setNH_ASSMBL_BOM_ID( "1" );
                  break;
               case "ENG-SYS-2":
                  assmblebom.setNH_ASSMBL_BOM_ID( "0" );
                  break;
               case "ENG-SYS-2-1":
                  assmblebom.setNH_ASSMBL_BOM_ID( "4" );
                  break;
               case "ENG-SYS-2-1-1":
                  assmblebom.setNH_ASSMBL_BOM_ID( "5" );
                  break;
               case "ENG-SYS-2-2":
                  assmblebom.setNH_ASSMBL_BOM_ID( "4" );
                  break;
               default:
                  assmblebom.setNH_ASSMBL_BOM_ID( "0" );
                  break;
            }

            c_assmblelist.add( assmblebom );
         }

      } catch ( SQLException e ) {
         e.printStackTrace();
      } finally {
         checkArraysEqual( assmbleBOMlist, c_assmblelist );

      }

   }


   /**
    * Get list of String values
    *
    * @return List<String>
    */

   public List<String> getListValues( StringBuilder assmblquery, String strColumn ) {

      ArrayList<String> assmbleBOMlist = new ArrayList<String>();

      ResultSet assbomResultSetRecords;

      try {
         assbomResultSetRecords = runQuery( assmblquery.toString() );
         while ( assbomResultSetRecords.next() ) {

            assmbleBOMlist.add( assbomResultSetRecords.getString( "strColumn" ) );
         }

      } catch ( SQLException e ) {
         e.printStackTrace();
      }
      return assmbleBOMlist;
   }


   /**
    * Get first int value from query
    *
    * @return int
    */

   @Override
   public int getIntValueFromQuery( String strQuery, String strColum ) {

      ResultSet ResultSetRecords;
      int intReturn = -1;
      try {
         ResultSetRecords = runQuery( strQuery );
         if ( ResultSetRecords.next() ) {
            intReturn = Integer.parseInt( ResultSetRecords.getString( strColum ) );
         }
      } catch ( SQLException e ) {
         e.printStackTrace();
      }
      return intReturn;
   }


   /**
    * Get BigDecimal value from query
    *
    * @return BigDecimal
    */

   public BigDecimal getDecimalValueFromQuery( String strQuery, String strColum ) {

      ResultSet ResultSetRecords;
      BigDecimal strReturn = null;
      try {
         ResultSetRecords = runQuery( strQuery );
         if ( ResultSetRecords.next() ) {
            strReturn = ResultSetRecords.getBigDecimal( strColum );
         }
      } catch ( SQLException e ) {
         e.printStackTrace();
      }
      return strReturn;
   }


   /**
    * Get first string value from query
    *
    * @return string
    */

   @Override
   public String getStringValueFromQuery( String strQuery, String strColum ) {

      ResultSet ResultSetRecords;
      String strReturn = null;
      try {
         ResultSetRecords = runQuery( strQuery );
         if ( ResultSetRecords.next() ) {
            strReturn = ResultSetRecords.getString( strColum );
         }
      } catch ( SQLException e ) {
         e.printStackTrace();
      }
      return strReturn;
   }

   /**
    * Execute any SQL string
    *
    * @return void
    */

   // public void executeSQL( String strSQL ) {
   // PreparedStatement lStatement;
   // try {
   // lStatement = getConnection().prepareStatement( strSQL );
   // lStatement.executeUpdate( strSQL );
   // commit();
   // } catch ( SQLException e ) {
   // e.printStackTrace();
   // }
   //
   // }


   /**
    * Add new record into EQP_PART_NO
    *
    * @return void
    */

   public void createRecordInEQPPARTNO( String strPartNoIdNext, String strInvClassDbId,
         String strAssmblCD, String manufactureCD, String strDesCr, String strPartOem ) {
      StringBuilder StrModifiedquery = new StringBuilder();
      StrModifiedquery.append(
            "insert into eqp_part_no (part_no_db_id,part_no_id,inv_class_db_id,inv_class_cd,manufact_db_id,manufact_cd, part_no_sdesc,part_no_oem) values (" )
            .append( CONS_DB_ID ).append( ", " ).append( strPartNoIdNext ).append( "," )
            .append( strInvClassDbId ).append( "," ).append( "'" ).append( strAssmblCD )
            .append( "', " ).append( CONS_DB_ID ).append( ", '" ).append( manufactureCD )
            .append( "', '" ).append( strDesCr ).append( "', '" ).append( strPartOem )
            .append( "')" );

      executeSQL( StrModifiedquery.toString() );
   }


   /**
    * Add new record into EQP_PART_BASELINE
    *
    * @return void
    */

   public void createRecordInEQPPARTBASELINE( String strBomPartIdNext, String strPartNoIdNext ) {
      StringBuilder StrModifiedquery = new StringBuilder();
      StrModifiedquery.append(
            "insert into EQP_PART_BASELINE (BOM_PART_DB_ID,BOM_PART_ID, PART_NO_DB_ID,PART_NO_ID) values (" )
            .append( CONS_DB_ID ).append( ", " ).append( strBomPartIdNext ).append( "," )
            .append( CONS_DB_ID ).append( "," ).append( strPartNoIdNext ).append( ")" );
      executeSQL( StrModifiedquery.toString() );
   }


   /**
    * Add new record into EQP_BOM_PART
    *
    * @return void
    */
   public void createRecordInEQPBOMPART( String strBomPartIdNext, String strAssmbleCD,
         String strAssmblBomIdNext, String strBomClassDbId, String strInvClassDbId, String strType,
         String strBomPartCd, String strBomPartName, String strPartQt ) {
      StringBuilder StrModifiedquery = new StringBuilder();
      StrModifiedquery.append(
            "insert into eqp_bom_part (bom_part_db_id, bom_part_id, assmbl_db_id, assmbl_cd, assmbl_bom_id, inv_class_db_id, inv_class_cd, bom_part_cd,bom_part_name,part_qt) values (" )
            .append( CONS_DB_ID ).append( ", " ).append( strBomPartIdNext ).append( "," )
            .append( CONS_DB_ID ).append( ", '" ).append( strAssmbleCD ).append( "', " )
            .append( strAssmblBomIdNext ).append( "," ).append( strInvClassDbId ).append( ",'" )
            .append( strType ).append( "', '" ).append( strBomPartCd ).append( "', '" )
            .append( strBomPartName ).append( "', " ).append( strPartQt ).append( ")" );
      executeSQL( StrModifiedquery.toString() );

   }


   public void createRecordInEQPAssmblBom( String strAssmbleCD, String strAssmblBomIdNext,
         String strBomClassDbId, String BomClassCd, String strPC, String strAssmblBomCd,
         String strAssmblBomName ) {
      StringBuilder StrModifiedquery = new StringBuilder();
      StrModifiedquery.append(
            "insert into eqp_assmbl_bom (ASSMBL_DB_ID, ASSMBL_CD, ASSMBL_BOM_ID, BOM_CLASS_DB_ID, BOM_CLASS_CD,POS_CT, ASSMBL_BOM_CD, ASSMBL_BOM_NAME) values (" )
            .append( CONS_DB_ID ).append( ", '" ).append( strAssmbleCD ).append( "', " )
            .append( strAssmblBomIdNext ).append( "," ).append( strBomClassDbId ).append( " ,'" )
            .append( BomClassCd ).append( "', " ).append( strPC ).append( ", '" )
            .append( strAssmblBomCd ).append( "', '" ).append( strAssmblBomName ).append( "')" );
      executeSQL( StrModifiedquery.toString() );

   }


   /**
    * Check BL_KIT and EQP_BOM_PART are equal
    *
    * @return void
    */

   public void compareEQPBOMPartAndBLKIT( String strASSMBLCD, String strASSMBLBOMID,
         String strAssmblyType ) {

      ArrayList<assmblPART> assmblePartlist = new ArrayList<assmblPART>();
      ArrayList<assmblPART> bl_kitlist = new ArrayList<assmblPART>();

      StringBuilder assmblquery = new StringBuilder();
      assmblquery.append(
            "select ASSMBL_CD,BOM_PART_CD, BOM_PART_NAME, ASSMBL_BOM_ID, INV_CLASS_CD from EQP_BOM_PART " );
      assmblquery.append( "where BOM_PART_NAME like '%AUTOKIT%'" );

      StringBuilder bl_kitquery = new StringBuilder();
      bl_kitquery.append( "select KIT_PART_NO_OEM, KIT_MANUFACT_CD, KIT_NAME from BL_KIT " );
      bl_kitquery.append( "where KIT_PART_NO_OEM like '%AUTOKIT%'" );

      ResultSet assPartResultSetRecords;
      ResultSet c_assPartResultSetRecords;
      try {
         assPartResultSetRecords = runQuery( assmblquery.toString() );

         while ( assPartResultSetRecords.next() ) {
            assmblPART assmblepart =
                  new assmblPART( assPartResultSetRecords.getString( "ASSMBL_CD" ),
                        assPartResultSetRecords.getString( "BOM_PART_CD" ),
                        assPartResultSetRecords.getString( "BOM_PART_NAME" ),
                        assPartResultSetRecords.getString( "ASSMBL_BOM_ID" ),
                        assPartResultSetRecords.getString( "INV_CLASS_CD" ) );
            assmblePartlist.add( assmblepart );

         }

         c_assPartResultSetRecords = runQuery( bl_kitquery.toString() );
         while ( c_assPartResultSetRecords.next() ) {
            assmblPART assmblepart = new assmblPART( strASSMBLCD,
                  c_assPartResultSetRecords.getString( "KIT_PART_NO_OEM" ) + "-"
                        + c_assPartResultSetRecords.getString( "KIT_MANUFACT_CD" ),
                  c_assPartResultSetRecords.getString( "KIT_NAME" ), strASSMBLBOMID,
                  strAssmblyType );
            bl_kitlist.add( assmblepart );
         }

      } catch ( SQLException e ) {
         e.printStackTrace();
      } finally {
         checkArraysEqual( assmblePartlist, bl_kitlist );

      }

   }


   /**
    * Check BL_KIT and EQP_BOM_PART are equal
    *
    * @return void
    */

   public void compareEQPBOMPartAndBLKIT2( String strASSMBLCD, String strASSMBLBOMID,
         String strAssmblyType ) {

      ArrayList<assmblPART> assmblePartlist = new ArrayList<assmblPART>();
      ArrayList<assmblPART> bl_kitlist = new ArrayList<assmblPART>();

      StringBuilder assmblquery = new StringBuilder();
      assmblquery.append(
            "select ASSMBL_CD,BOM_PART_CD, BOM_PART_NAME, ASSMBL_BOM_ID, INV_CLASS_CD from EQP_BOM_PART " );
      assmblquery.append( "where BOM_PART_NAME like '%AUTOKIT%'" );

      StringBuilder bl_kitquery = new StringBuilder();
      bl_kitquery.append( "select KIT_PART_GROUP_CD, KIT_PART_GROUP_NAME from BL_KIT " );
      bl_kitquery.append( "where KIT_PART_NO_OEM like '%AUTOKIT%'" );

      ResultSet assPartResultSetRecords;
      ResultSet c_assPartResultSetRecords;
      try {
         assPartResultSetRecords = runQuery( assmblquery.toString() );

         while ( assPartResultSetRecords.next() ) {
            assmblPART assmblepart =
                  new assmblPART( assPartResultSetRecords.getString( "ASSMBL_CD" ),
                        assPartResultSetRecords.getString( "BOM_PART_CD" ),
                        assPartResultSetRecords.getString( "BOM_PART_NAME" ),
                        assPartResultSetRecords.getString( "INV_CLASS_CD" ) );
            assmblePartlist.add( assmblepart );

         }

         c_assPartResultSetRecords = runQuery( bl_kitquery.toString() );
         while ( c_assPartResultSetRecords.next() ) {
            assmblPART assmblepart = new assmblPART( strASSMBLCD,
                  c_assPartResultSetRecords.getString( "KIT_PART_GROUP_CD" ),
                  c_assPartResultSetRecords.getString( "KIT_PART_GROUP_NAME" ), strAssmblyType );
            bl_kitlist.add( assmblepart );
         }

      } catch ( SQLException e ) {
         e.printStackTrace();
      } finally {
         checkArraysEqual( assmblePartlist, bl_kitlist );

      }

   }


   /**
    * Check BL_KIT and EQP_PART_NO are equal
    *
    * @return void
    */

   public void compareEQPPartNoAndBLKIT( String strAssmblyType ) {

      ArrayList<partInfo> assmblePartlist = new ArrayList<partInfo>();
      ArrayList<partInfo> bl_kitlist = new ArrayList<partInfo>();

      StringBuilder assmblquery = new StringBuilder();
      assmblquery.append(
            "select PART_NO_OEM,MANUFACT_CD, PART_NO_SDESC, INV_CLASS_CD from EQP_PART_NO " );
      assmblquery.append( "where PART_NO_OEM like '%AUTOKIT%'" );

      StringBuilder bl_kitquery = new StringBuilder();
      bl_kitquery.append( "select KIT_PART_NO_OEM, KIT_MANUFACT_CD, KIT_NAME from BL_KIT " );
      bl_kitquery.append( "where KIT_PART_NO_OEM like '%AUTOKIT%'" );

      ResultSet assPartResultSetRecords;
      ResultSet c_assPartResultSetRecords;
      try {
         assPartResultSetRecords = runQuery( assmblquery.toString() );

         while ( assPartResultSetRecords.next() ) {
            partInfo assmblepart = new partInfo( assPartResultSetRecords.getString( "MANUFACT_CD" ),
                  assPartResultSetRecords.getString( "PART_NO_OEM" ),
                  assPartResultSetRecords.getString( "PART_NO_SDESC" ),
                  assPartResultSetRecords.getString( "INV_CLASS_CD" ) );
            assmblePartlist.add( assmblepart );

         }

         c_assPartResultSetRecords = runQuery( bl_kitquery.toString() );
         while ( c_assPartResultSetRecords.next() ) {
            partInfo assmblepart =
                  new partInfo( c_assPartResultSetRecords.getString( "KIT_MANUFACT_CD" ),
                        c_assPartResultSetRecords.getString( "KIT_PART_NO_OEM" ),
                        c_assPartResultSetRecords.getString( "KIT_NAME" ), strAssmblyType );
            bl_kitlist.add( assmblepart );
         }

      } catch ( SQLException e ) {
         e.printStackTrace();
      } finally {
         checkArraysEqual( assmblePartlist, bl_kitlist );

      }

   }


   /**
    * Check data in EQP_BOM_PART are expected.
    *
    * @return void
    */

   public void compareEQPBOMPartAndExpected( String strBOMPARTID, int intRecordsToVerify,
         String strAssmbleCD, String strBOMPARTCD, String strBOMPARTNAME, String strINVClass ) {

      StringBuilder assmblquery = new StringBuilder();
      assmblquery.append(
            "select ASSMBL_CD,BOM_PART_CD, BOM_PART_NAME, INV_CLASS_CD from EQP_BOM_PART " );
      assmblquery.append( "where BOM_PART_ID = " ).append( strBOMPARTID );

      ResultSet assPartResultSetRecords;

      try {
         assPartResultSetRecords = runQuery( assmblquery.toString() );
         assPartResultSetRecords.next();
         Assert.assertTrue(
               assPartResultSetRecords.getString( "ASSMBL_CD" ).equalsIgnoreCase( strAssmbleCD ) );
         Assert.assertTrue( assPartResultSetRecords.getString( "BOM_PART_CD" )
               .equalsIgnoreCase( strBOMPARTCD ) );
         Assert.assertTrue( assPartResultSetRecords.getString( "BOM_PART_NAME" )
               .equalsIgnoreCase( strBOMPARTNAME ) );
         Assert.assertTrue( assPartResultSetRecords.getString( "INV_CLASS_CD" )
               .equalsIgnoreCase( strINVClass ) );

      } catch ( SQLException e ) {
         e.printStackTrace();
      }

   }


   /**
    * Retrieve BOM_PART_NO from EQP_PART_BASELINE table given part_no_id
    *
    * @return void
    */

   public String getBomPart( String strPN ) {

      String strSQL = "Select BOM_PART_ID from EQP_PART_BASELINE where PART_NO_ID=" + strPN
            + " and PART_NO_DB_ID=" + CONS_DB_ID;
      String strBOMPARTID = getStringValueFromQuery( strSQL, "BOM_PART_ID" ).trim();
      return strBOMPARTID;

   }


   /**
    * Retrieve HR IDs
    *
    * @return HRIDS
    */
   public simpleIDs getHRIds( String aUSERNAME ) {
      String strSQL = "select HR_DB_ID, HR_ID from org_hr " + "inner join utl_user on  "
            + "utl_user.user_id=org_hr.user_id " + "where utl_user.username='" + aUSERNAME + "'";

      ResultSet ResultSetRecords;
      simpleIDs lIds = null;
      try {
         ResultSetRecords = runQuery( strSQL );
         if ( ResultSetRecords.next() ) {
            lIds = new simpleIDs( ResultSetRecords.getString( "HR_DB_ID" ),
                  ResultSetRecords.getString( "HR_ID" ) );
         }
      } catch ( SQLException e ) {
         e.printStackTrace();
         Assert.assertTrue( "getHRIds", false );

      }

      return lIds;
   }


   /**
    * Execute the query for date.
    */
   @Override
   public List<ArrayList<java.sql.Date>> execute_date( String aStrQuery, List<String> lfields ) {

      PreparedStatement lStatement;
      List<ArrayList<java.sql.Date>> louter = new ArrayList<ArrayList<java.sql.Date>>();

      try {
         lStatement = getConnection().prepareStatement( aStrQuery, ResultSet.TYPE_SCROLL_SENSITIVE,
               ResultSet.CONCUR_READ_ONLY );

         ResultSet lResultSet = lStatement.executeQuery( aStrQuery );
         while ( lResultSet.next() ) {
            List<java.sql.Date> iList = new ArrayList<java.sql.Date>();
            for ( int i = 0; i < lfields.size(); i++ ) {
               iList.add( lResultSet.getDate( lfields.get( i ) ) );

            }
            louter.add( ( ArrayList<java.sql.Date> ) iList );

         }

      } catch ( SQLException e ) {
         e.printStackTrace();
      }

      return louter;

   }


   /**
    * This function is to retrieve task IDs from task_task table.
    *
    *
    */
   public taskIDs getTaskIdsAndDefnIds( String aTASK_CD, String aTASK_NAME ) {

      String[] iIds = { "TASK_DEFN_DB_ID", "TASK_DEFN_ID", "TASK_DB_ID", "TASK_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_CD", aTASK_CD );
      lArgs.addArguments( "TASK_NAME", aTASK_NAME );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_TASK, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      taskIDs lIds =
            new taskIDs( new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) ),
                  new simpleIDs( llists.get( 0 ).get( 2 ), llists.get( 0 ).get( 3 ) ) );
      return lIds;
   }


   /**
    *
    * Calls check error code
    *
    *
    */
   protected void checkErrorCode( String aQuery, String aTestName, String aValidationCode ) {

      boolean lFound = false;
      String lerror_desc = null;
      String lerrorMsg = null;

      String lquery = aQuery;

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
    *
    * This used to find all the error codes and associated message.
    *
    * @param aQuery
    *           - this is Query used to look-up error code and message - The Query should using
    *           DL_REF_MESSAGE table
    * @param aActualErrorCode
    *           - this is the error code that is generated by the test
    */
   protected void checkErrorCode( String aQuery, String aActualErrorCode ) {

      boolean lFound = false;
      String lerror_desc = null;
      String lerrorMsg = "";

      String lquery = aQuery;

      String[] iIds = { "RESULT_CD", "TECH_DESC" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      List<ArrayList<String>> lresult = execute( lquery, lfields );

      for ( int i = 0; i < lresult.size(); i++ ) {
         if ( lresult.get( i ).get( 0 ).equalsIgnoreCase( aActualErrorCode ) ) {
            lFound = true;
         } else {
            lerrorMsg =
                  lerrorMsg + lresult.get( i ).get( 0 ) + ": " + lresult.get( i ).get( 1 ) + "\n";
         }

      }

      if ( lFound == false ) {
         lquery =
               "select TECH_DESC from DL_REF_MESSAGE where result_cd = '" + aActualErrorCode + "'";
         lerror_desc = runQuery( lquery, "TECH_DESC" );

      }
      Assert.assertTrue( "The error code not found- " + aActualErrorCode + " : " + lerror_desc
            + "; Other error(s) found:\n" + lerrorMsg + " ", lFound );
   }


   /**
    * This function is to retrieve data type ids by giving domain type cd and ENG Unit CD
    *
    *
    */
   public simpleIDs getDataTypeIDs( String aDomainTypeCd, String aEngUntiCd, String aDataTypeCd ) {

      String[] iIds = { "DATA_TYPE_DB_ID", "DATA_TYPE_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "DOMAIN_TYPE_CD", aDomainTypeCd );
      lArgs.addArguments( "ENG_UNIT_CD", aEngUntiCd );
      lArgs.addArguments( "DATA_TYPE_CD", aDataTypeCd );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.MIM_DATA_TYPE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;
   }


   /**
    * This function is to retrieve AssmblBomID from EQP_ASSMBL_BOM table .
    *
    *
    */
   @Override
   public AssmblBomID getAssmblBomId( String aAssmblCD, String aASSMBL_BOM_CD ) {
      String[] iIds = { "ASSMBL_DB_ID", "ASSMBL_CD", "ASSMBL_BOM_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "ASSMBL_BOM_CD", aASSMBL_BOM_CD );
      lArgs.addArguments( "ASSMBL_CD", aAssmblCD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_ASSMBL_BOM, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      AssmblBomID lIds = new AssmblBomID( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ),
            llists.get( 0 ).get( 2 ) );

      return lIds;

   }


   /**
    * This function is to retrieve PART no from EQP_par_no .
    *
    *
    */
   public simpleIDs getPNId( String aPART_NO_OEM, String aMANUFACT_CD ) {
      String[] iIds = { "PART_NO_DB_ID", "PART_NO_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "PART_NO_OEM", aPART_NO_OEM );
      lArgs.addArguments( "MANUFACT_CD", aMANUFACT_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_PART_NO, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;

   }

}
