package com.mxi.mx.core.maint.plan.actualsloader.ODF;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;

import com.mxi.mx.core.maint.plan.datamodels.asmInfor;
import com.mxi.mx.core.maint.plan.datamodels.assmblPOS;
import com.mxi.mx.core.maint.plan.datamodels.assmbleInfor;
import com.mxi.mx.core.maint.plan.datamodels.invIDs;
import com.mxi.mx.core.maint.plan.datamodels.invInfor;
import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.ActualsLoaderTest;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.TestConstants;
import com.mxi.mx.util.ValidationAndImport;
import com.mxi.mx.util.WhereClause;


/**
 * This is common function area for ODF tests
 *
 */
public class Faults extends ActualsLoaderTest {

   ValidationAndImport ivalidationandimport;


   /**
    * This function is to add days by given strign of date
    *
    *
    *
    */
   public Date DateAddDays( Date aDate, int days ) {

      Calendar c = Calendar.getInstance();
      c.setTime( aDate );
      c.add( Calendar.DATE, days );
      return c.getTime();
   }


   /**
    * This function is to retrieve data type information from mim_data_type table.
    *
    *
    */
   public int getDaysToAdd( String aCD, String aDESC_SDESC ) {

      // MIM_DATA_TYPE table
      String[] iIds = { "DEADLINE_DAYS_QT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "FAIL_DEFER_CD", aCD );
      lArgs.addArguments( "DESC_SDESC", aDESC_SDESC );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.REF_FAIL_DEFER, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      return Integer.parseInt( llists.get( 0 ).get( 0 ) );

   }


   /**
    * This function is to retrieve inventory ID by giving Serial number.
    *
    *
    *
    */
   @Override
   public simpleIDs getInvIDs( String aSN ) {

      String[] iIds = { "INV_NO_DB_ID", "INV_NO_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SERIAL_NO_OEM", aSN );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.INV_INV, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;
   }


   /**
    * This function is to retrieve current inventory value by giving serial no
    *
    *
    *
    */

   @Override
   public int getCurrentUsage( String aSN, simpleIDs aDataTypeIds ) {

      simpleIDs linv_ids = getInvIDs( aSN );

      String[] iIds = { "TSN_QT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "INV_NO_DB_ID", linv_ids.getNO_DB_ID() );
      lArgs.addArguments( "INV_NO_ID", linv_ids.getNO_ID() );
      lArgs.addArguments( "DATA_TYPE_DB_ID", aDataTypeIds.getNO_DB_ID() );
      lArgs.addArguments( "DATA_TYPE_ID", aDataTypeIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.INV_CURR_USAGE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      return Integer.parseInt( llists.get( 0 ).get( 0 ) );

   }


   /**
    * This function is to verify SCHED_RMVD_PART table details part
    *
    *
    */
   public void verifySCHEDRMVDPART( simpleIDs aIDs, String aInfor, String aRMReasonCD,
         String aRMVD_QT, int aNumRecord, invInfor ainvInfor, boolean aCheck ) {

      // SCHED_RMVD_PART table
      String[] iIds =
            { "REMOVE_REASON_CD", "RMVD_QT", "INV_NO_DB_ID", "INV_NO_ID", "SERIAL_NO_OEM" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SCHED_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "SCHED_ID", aIDs.getNO_ID() );
      lArgs.addArguments( "SCHED_PART_ID", aInfor );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.SCHED_RMVD_PART, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Check # of records is correct
      Assert.assertTrue( "Check number of records display: ", llists.size() == aNumRecord );

      // Check each record
      for ( int i = 0; i < llists.size(); i++ ) {

         Assert.assertTrue( "REMOVE_REASON_CD",
               llists.get( 0 ).get( 0 ).equalsIgnoreCase( aRMReasonCD ) );
         Assert.assertTrue( "RMVD_QT", llists.get( 0 ).get( 1 ).equalsIgnoreCase( aRMVD_QT ) );
         if ( aCheck ) {
            Assert.assertTrue( "INV_NO_DB_ID", llists.get( 0 ).get( 2 )
                  .equalsIgnoreCase( ainvInfor.getiINVIDs().getNO_DB_ID() ) );
            Assert.assertTrue( "INV_NO_ID",
                  llists.get( 0 ).get( 3 ).equalsIgnoreCase( ainvInfor.getiINVIDs().getNO_ID() ) );
            Assert.assertTrue( "SERIAL_NO_OEM",
                  llists.get( 0 ).get( 4 ).equalsIgnoreCase( ainvInfor.getiSerial_no_oem() ) );
         }
      }

   }


   /**
    * This function is to verify SCHED_RMVD_PART table details part
    *
    *
    */
   public void verifySCHEDRMVDPART( simpleIDs aIDs, asmInfor aInfor, String aRMReasonCD,
         String aRMVD_QT, int aNumRecord, invInfor ainvInfor, boolean aCheck ) {

      // SCHED_RMVD_PART table
      String[] iIds =
            { "REMOVE_REASON_CD", "RMVD_QT", "INV_NO_DB_ID", "INV_NO_ID", "SERIAL_NO_OEM" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SCHED_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "SCHED_ID", aIDs.getNO_ID() );
      lArgs.addArguments( "PART_NO_DB_ID", aInfor.getlPNIDs().getNO_DB_ID() );
      lArgs.addArguments( "PART_NO_ID", aInfor.getlPNIDs().getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.SCHED_RMVD_PART, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Check # of records is correct
      Assert.assertTrue( "Check number of records display: ", llists.size() == aNumRecord );

      // Check each record
      for ( int i = 0; i < llists.size(); i++ ) {

         Assert.assertTrue( "REMOVE_REASON_CD",
               llists.get( 0 ).get( 0 ).equalsIgnoreCase( aRMReasonCD ) );
         Assert.assertTrue( "RMVD_QT", llists.get( 0 ).get( 1 ).equalsIgnoreCase( aRMVD_QT ) );
         if ( aCheck ) {
            Assert.assertTrue( "INV_NO_DB_ID", llists.get( 0 ).get( 2 )
                  .equalsIgnoreCase( ainvInfor.getiINVIDs().getNO_DB_ID() ) );
            Assert.assertTrue( "INV_NO_ID",
                  llists.get( 0 ).get( 3 ).equalsIgnoreCase( ainvInfor.getiINVIDs().getNO_ID() ) );
            Assert.assertTrue( "SERIAL_NO_OEM",
                  llists.get( 0 ).get( 4 ).equalsIgnoreCase( ainvInfor.getiSerial_no_oem() ) );
         }
      }

   }


   /**
    * This function is to verify SCHED_INST_PART table details part
    *
    *
    */
   public void verifySCHEDINSTPART( simpleIDs aIDs, asmInfor aInfor, String aINSTQT,
         int aNumRecord ) {

      // SCHED_INST_PART
      String[] iIds = { "PART_NO_DB_ID", "PART_NO_ID", "INST_QT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SCHED_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "SCHED_ID", aIDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.SCHED_INST_PART, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Check # of records is correct
      Assert.assertTrue( "Check number of records display: ", llists.size() == aNumRecord );

      // Check each record
      for ( int i = 0; i < llists.size(); i++ ) {
         // Assert.assertTrue( "PART_NO_DB_ID",
         // llists.get( i ).get( 0 ).equalsIgnoreCase( aInfor.getlPNIDs().getNO_DB_ID() ) );
         // Assert.assertTrue( "PART_NO_ID",
         // llists.get( i ).get( 1 ).equalsIgnoreCase( aInfor.getlPNIDs().getNO_ID() ) );
         Assert.assertTrue( "INST_QT", llists.get( i ).get( 2 ).equalsIgnoreCase( aINSTQT ) );
      }

   }


   /**
    * This function is to verify SCHED_INST_PART table
    *
    *
    */
   public void verifySCHEDINSTPART_MultipleParts( simpleIDs aIDs, String aInfor, String aINSTQT,
         int aNumRecord ) {

      // SCHED_INST_PART
      String[] iIds = { "INST_QT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SCHED_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "SCHED_ID", aIDs.getNO_ID() );
      lArgs.addArguments( "SCHED_PART_ID", aInfor );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.SCHED_INST_PART, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Check # of records is correct
      Assert.assertTrue( "Check number of records display: ", llists.size() == aNumRecord );

      // Check each record
      for ( int i = 0; i < llists.size(); i++ ) {
         Assert.assertTrue( "INST_QT", llists.get( i ).get( 0 ).equalsIgnoreCase( aINSTQT ) );
      }

   }


   /**
    * This function is to verify SCHED_PART table.
    *
    *
    */
   public void verifySCHEDPART( simpleIDs aIDs, asmInfor aInfor, String aSTATUSCD, String aSCHEDQT,
         String aREQACTCD, boolean aCheck ) {

      // SCHED_PART
      String[] iIds = { "SCHED_BOM_PART_DB_ID", "SCHED_BOM_PART_ID", "SCHED_PART_STATUS_CD",
            "SPEC_PART_NO_DB_ID", "SPEC_PART_NO_ID", "NH_ASSMBL_DB_ID", "NH_ASSMBL_CD",
            "NH_ASSMBL_BOM_ID", "NH_ASSMBL_POS_ID", "ASSMBL_DB_ID", "ASSMBL_CD", "ASSMBL_BOM_ID",
            "ASSMBL_POS_ID", "SCHED_QT", "REQ_ACTION_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SCHED_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "SCHED_ID", aIDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.SCHED_PART, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "SCHED_BOM_PART_DB_ID",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aInfor.getlPGIDs().getNO_DB_ID() ) );
      Assert.assertTrue( "SCHED_BOM_PART_ID",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aInfor.getlPGIDs().getNO_ID() ) );
      // Assert.assertTrue( "SCHED_PART_STATUS_CD",
      // llists.get( 0 ).get( 2 ).equalsIgnoreCase( aSTATUSCD ) );
      // Assert.assertTrue( "SPEC_PART_NO_DB_ID",
      // llists.get( 0 ).get( 3 ).equalsIgnoreCase( aInfor.getlPNIDs().getNO_DB_ID() ) );
      // Assert.assertTrue( "SPEC_PART_NO_ID",
      // llists.get( 0 ).get( 4 ).equalsIgnoreCase( aInfor.getlPNIDs().getNO_ID() ) );
      Assert.assertTrue( "NH_ASSMBL_DB_ID",
            llists.get( 0 ).get( 5 ).equalsIgnoreCase( aInfor.getlNHASMBL().getASSMBL_DB_ID() ) );
      Assert.assertTrue( "NH_ASSMBL_CD",
            llists.get( 0 ).get( 6 ).equalsIgnoreCase( aInfor.getlNHASMBL().getASSMBL_CD() ) );
      // Assert.assertTrue( "NH_ASSMBL_BOM_ID",
      // llists.get( 0 ).get( 7 ).equalsIgnoreCase( aInfor.getlNHASMBL().getASSMBL_BOM_ID() ) );
      // Assert.assertTrue( "NH_ASSMBL_BOM_POS_ID",
      // llists.get( 0 ).get( 8 ).equalsIgnoreCase( aInfor.getlNHASMBL().getASSMBL_POS_ID() ) );
      if ( aCheck ) {
         Assert.assertTrue( "ASSMBL_DB_ID",
               llists.get( 0 ).get( 9 ).equalsIgnoreCase( aInfor.getlASMBL().getASSMBL_DB_ID() ) );
         Assert.assertTrue( "ASSMBL_CD",
               llists.get( 0 ).get( 10 ).equalsIgnoreCase( aInfor.getlASMBL().getASSMBL_CD() ) );
         Assert.assertTrue( "ASSMBL_BOM_ID", llists.get( 0 ).get( 11 )
               .equalsIgnoreCase( aInfor.getlASMBL().getASSMBL_BOM_ID() ) );
         Assert.assertTrue( "ASSMBL_POS_ID", llists.get( 0 ).get( 12 )
               .equalsIgnoreCase( aInfor.getlASMBL().getASSMBL_POS_ID() ) );
      }
      Assert.assertTrue( "SCHED_QT", llists.get( 0 ).get( 13 ).equalsIgnoreCase( aSCHEDQT ) );
      Assert.assertTrue( "REQ_ACTION_CD", llists.get( 0 ).get( 14 ).equalsIgnoreCase( aREQACTCD ) );

   }


   /**
    * This function is to verify SCHED_PART table.
    *
    *
    */
   public String verifySCHEDPART_MultipleParts( simpleIDs aIDs, asmInfor aInfor, String aSTATUSCD,
         String aSCHEDQT, String aREQACTCD ) {

      // SCHED_PART
      String[] iIds = { "SCHED_PART_STATUS_CD", "SPEC_PART_NO_DB_ID", "SPEC_PART_NO_ID",
            "NH_ASSMBL_DB_ID", "NH_ASSMBL_CD", "NH_ASSMBL_BOM_ID", "NH_ASSMBL_POS_ID", "SCHED_QT",
            "REQ_ACTION_CD", "SCHED_PART_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SCHED_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "SCHED_ID", aIDs.getNO_ID() );
      lArgs.addArguments( "SCHED_BOM_PART_DB_ID", aInfor.getlPGIDs().getNO_DB_ID() );
      lArgs.addArguments( "SCHED_BOM_PART_ID", aInfor.getlPGIDs().getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.SCHED_PART, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "SCHED_PART_STATUS_CD",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aSTATUSCD ) );
      // Assert.assertTrue( "SPEC_PART_NO_DB_ID",
      // llists.get( 0 ).get( 1 ).equalsIgnoreCase( aInfor.getlPNIDs().getNO_DB_ID() ) );
      // Assert.assertTrue( "SPEC_PART_NO_ID",
      // llists.get( 0 ).get( 2 ).equalsIgnoreCase( aInfor.getlPNIDs().getNO_ID() ) );
      Assert.assertTrue( "NH_ASSMBL_DB_ID",
            llists.get( 0 ).get( 3 ).equalsIgnoreCase( aInfor.getlNHASMBL().getASSMBL_DB_ID() ) );
      Assert.assertTrue( "NH_ASSMBL_CD",
            llists.get( 0 ).get( 4 ).equalsIgnoreCase( aInfor.getlNHASMBL().getASSMBL_CD() ) );
      // Assert.assertTrue( "NH_ASSMBL_BOM_ID",
      // llists.get( 0 ).get( 5 ).equalsIgnoreCase( aInfor.getlNHASMBL().getASSMBL_BOM_ID() ) );
      // Assert.assertTrue( "NH_ASSMBL_BOM_POS_ID",
      // llists.get( 0 ).get( 6 ).equalsIgnoreCase( aInfor.getlNHASMBL().getASSMBL_POS_ID() ) );
      Assert.assertTrue( "SCHED_QT", llists.get( 0 ).get( 7 ).equalsIgnoreCase( aSCHEDQT ) );
      Assert.assertTrue( "REQ_ACTION_CD", llists.get( 0 ).get( 8 ).equalsIgnoreCase( aREQACTCD ) );

      String lschedId = llists.get( 0 ).get( 9 );

      return lschedId;

   }


   /**
    * This function is to retrieve PART's assemble information.
    *
    *
    */
   public asmInfor getASMINFor( String aPART_NO_OEM, String aMANUFACT_CD ) {

      asmInfor lassm = null;

      String lQuery =
            "select eqp_part_no.part_no_db_id, eqp_part_no.part_no_id,eqp_assmbl_bom.nh_assmbl_db_id,eqp_assmbl_bom.nh_assmbl_cd, eqp_assmbl_bom.nh_assmbl_bom_id,eqp_assmbl_pos.nh_assmbl_pos_id,"
                  + " eqp_assmbl_bom.assmbl_db_id, eqp_assmbl_bom.assmbl_cd, eqp_assmbl_bom.assmbl_bom_id, eqp_assmbl_pos.assmbl_pos_id, eqp_part_baseline.bom_part_db_id, eqp_part_baseline.bom_part_id from eqp_part_no"
                  + " inner join eqp_part_baseline on eqp_part_baseline.part_no_db_id=eqp_part_no.part_no_db_id and eqp_part_baseline.part_no_id=eqp_part_no.part_no_id"
                  + " inner join eqp_bom_part on eqp_bom_part.bom_part_db_id=eqp_part_baseline.bom_part_db_id and eqp_bom_part.bom_part_id=eqp_part_baseline.bom_part_id"
                  + " inner join eqp_assmbl_pos on eqp_assmbl_pos.assmbl_db_id=eqp_bom_part.bom_part_db_id and eqp_assmbl_pos.assmbl_cd=eqp_bom_part.assmbl_cd and eqp_assmbl_pos.assmbl_bom_id=eqp_bom_part.assmbl_bom_id"
                  + " inner join eqp_assmbl_bom on eqp_assmbl_bom.assmbl_db_id=eqp_bom_part.assmbl_db_id and eqp_assmbl_bom.assmbl_cd=eqp_bom_part.assmbl_cd and eqp_assmbl_bom.assmbl_bom_id=eqp_bom_part.assmbl_bom_id"
                  + " where eqp_part_no.PART_NO_OEM='" + aPART_NO_OEM
                  + "' and eqp_part_no.MANUFACT_CD='" + aMANUFACT_CD + "'";

      ResultSet assbomResultSetRecords;
      try {
         assbomResultSetRecords = runQuery( lQuery );
         assbomResultSetRecords.next();
         lassm = new asmInfor(
               new simpleIDs( assbomResultSetRecords.getString( "part_no_db_id" ),
                     assbomResultSetRecords.getString( "part_no_id" ) ),
               new assmbleInfor( assbomResultSetRecords.getString( "nh_assmbl_db_id" ),
                     assbomResultSetRecords.getString( "nh_assmbl_cd" ),
                     assbomResultSetRecords.getString( "nh_assmbl_bom_id" ),
                     assbomResultSetRecords.getString( "nh_assmbl_pos_id" ) ),
               new assmbleInfor( assbomResultSetRecords.getString( "assmbl_db_id" ),
                     assbomResultSetRecords.getString( "assmbl_cd" ),
                     assbomResultSetRecords.getString( "assmbl_bom_id" ),
                     assbomResultSetRecords.getString( "assmbl_pos_id" ) ),
               new simpleIDs( assbomResultSetRecords.getString( "bom_part_db_id" ),
                     assbomResultSetRecords.getString( "bom_part_id" ) ) );

      } catch ( SQLException e ) {
         e.printStackTrace();
      }

      return lassm;

   }


   /**
    * This function is to retrieve PART's assemble information.
    *
    *
    */
   public invInfor getINVINFor( String aPART_NO_OEM, String aMANUFACT_CD, String aSerial_NO_OEM ) {

      invInfor lassm = null;

      String lQuery = "select inv1.inv_no_db_id, " + "inv1.inv_no_id, " + "inv1.serial_no_oem "
            + "from eqp_part_no " + "inner join eqp_part_baseline on "
            + "eqp_part_baseline.part_no_db_id=eqp_part_no.part_no_db_id and "
            + "eqp_part_baseline.part_no_id=eqp_part_no.part_no_id " + "inner join eqp_bom_part on "
            + "eqp_bom_part.bom_part_db_id=eqp_part_baseline.bom_part_db_id and "
            + "eqp_bom_part.bom_part_id=eqp_part_baseline.bom_part_id "
            + "inner join eqp_assmbl_pos on "
            + "eqp_assmbl_pos.assmbl_db_id=eqp_bom_part.bom_part_db_id and "
            + "eqp_assmbl_pos.assmbl_cd=eqp_bom_part.assmbl_cd and "
            + "eqp_assmbl_pos.assmbl_bom_id=eqp_bom_part.assmbl_bom_id "
            + "inner join eqp_assmbl_bom on "
            + "eqp_assmbl_bom.assmbl_db_id=eqp_bom_part.assmbl_db_id and "
            + "eqp_assmbl_bom.assmbl_cd=eqp_bom_part.assmbl_cd and "
            + "eqp_assmbl_bom.assmbl_bom_id=eqp_bom_part.assmbl_bom_id "
            + "inner join inv_inv inv1 on " + "inv1.part_no_id=eqp_part_no.part_no_id "
            + "inner join inv_inv inv2 on " + "inv2.inv_no_id=inv1.h_inv_no_id "
            + "where eqp_part_no.PART_NO_OEM='" + aPART_NO_OEM + "' and eqp_part_no.MANUFACT_CD='"
            + aMANUFACT_CD + "' and inv2.serial_no_oem='" + aSerial_NO_OEM + "'";

      ResultSet assbomResultSetRecords;
      try {
         assbomResultSetRecords = runQuery( lQuery );
         assbomResultSetRecords.next();
         lassm = new invInfor(
               new simpleIDs( assbomResultSetRecords.getString( "inv_no_db_id" ),
                     assbomResultSetRecords.getString( "inv_no_id" ) ),
               assbomResultSetRecords.getString( "serial_no_oem" ) );

      } catch ( SQLException e ) {
         e.printStackTrace();
      }

      return lassm;

   }


   /**
    * This function is to verify SCHED_LABOUR_ROLE_STATUS table details part
    *
    *
    */
   public void verifySCHEDLABOURROLESTATUS( simpleIDs aIDs, String aStatusCD ) {

      String ldbid = getlocalDBid();

      // SCHED_LABOUR_ROLE_STATUS table
      String[] iIds = { "LABOUR_ROLE_STATUS_CD", "STATUS_DB_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "LABOUR_ROLE_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "LABOUR_ROLE_ID", aIDs.getNO_ID() );

      String iQueryString =
            TableUtil.buildTableQuery( TableUtil.SCHED_LABOUR_ROLE_STATUS, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "LABOUR_ROLE_STATUS_CD",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aStatusCD ) );
      Assert.assertTrue( "STATUS_DB_ID", llists.get( 0 ).get( 1 ).equalsIgnoreCase( ldbid ) );

   }


   /**
    * This function is to verify SCHED_LABOUR_ROLE table details part
    *
    * @return LABOUUR_ROLE ID
    *
    *
    */
   public simpleIDs verifySCHEDLABOURROLE( simpleIDs aIDs, String aRoleTypeCD ) {

      // SCHED_LABOUR_ROLE table
      String[] iIds = { "LABOUR_ROLE_DB_ID", "LABOUR_ROLE_ID", "LABOUR_ROLE_TYPE_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "LABOUR_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "LABOUR_ID", aIDs.getNO_ID() );

      String iQueryString =
            TableUtil.buildTableQuery( TableUtil.SCHED_LABOUR_ROLE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      Assert.assertTrue( "LABOUR_ROLE_TYPE_CD",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aRoleTypeCD ) );

      return lIds;

   }


   /**
    * This function is to verify SCHED_LABOUR table details part
    *
    * @return LABOUUR ID
    *
    *
    */
   public simpleIDs verifySCHEDLABOUR( simpleIDs aIDs, String aStageCD, String aSkillCD ) {

      String llaborSkillDBid = getStringValueFromQuery(
            "SELECT LABOUR_SKILL_DB_ID FROM  ref_labour_skill WHERE labour_skill_cd='" + aSkillCD
                  + "'",
            "LABOUR_SKILL_DB_ID" );
      // SCHED_LABOUR table
      String[] iIds = { "LABOUR_DB_ID", "LABOUR_ID", "LABOUR_STAGE_CD", "LABOUR_SKILL_CD",
            "LABOUR_SKILL_DB_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SCHED_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "SCHED_ID", aIDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.SCHED_LABOUR, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      Assert.assertTrue( "LABOUR_STAGE_CD", llists.get( 0 ).get( 2 ).equalsIgnoreCase( aStageCD ) );
      Assert.assertTrue( "LABOUR_SKILL_CD", llists.get( 0 ).get( 3 ).equalsIgnoreCase( aSkillCD ) );
      Assert.assertTrue( "LABOUR_SKILL_DB_ID",
            llists.get( 0 ).get( 4 ).equalsIgnoreCase( llaborSkillDBid ) );

      return lIds;

   }


   /**
    * This function is to verify SD_FAULT table details part
    *
    *
    */
   public void verifySDFAULT( simpleIDs aIDs, String aFAIL_DEFER_CD, String aFAULT_SOURCE_CD,
         String aFAIL_SEV_CD, String aDEFER_REF_SDESC, String aOP_RESTRICTION_LDESC,
         String aDEFER_CD_SDESC, String aFAULT_LOG_TYPE_CD, String aFRM_CD ) {

      // SD_FAULT table
      String[] iIds = { "FAIL_DEFER_CD", "FAULT_SOURCE_CD", "FAIL_SEV_CD", "DEFER_REF_SDESC",
            "OP_RESTRICTION_LDESC", "DEFER_CD_SDESC", "FAULT_LOG_TYPE_CD", "FRM_SDESC" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "FAULT_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "FAULT_ID", aIDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.SD_FAULT, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      if ( aFAIL_DEFER_CD != null ) {
         Assert.assertTrue( "FAIL_DEFER_CD",
               llists.get( 0 ).get( 0 ).equalsIgnoreCase( aFAIL_DEFER_CD ) );
      }
      Assert.assertTrue( "FAULT_SOURCE_CD",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aFAULT_SOURCE_CD ) );
      Assert.assertTrue( "FAIL_SEV_CD", llists.get( 0 ).get( 2 ).equalsIgnoreCase( aFAIL_SEV_CD ) );
      if ( aDEFER_REF_SDESC != null ) {
         Assert.assertTrue( "DEFER_REF_SDESC",
               llists.get( 0 ).get( 3 ).equalsIgnoreCase( aDEFER_REF_SDESC ) );
      }
      Assert.assertTrue( "OP_RESTRICTION_LDESC",
            llists.get( 0 ).get( 4 ).equalsIgnoreCase( aOP_RESTRICTION_LDESC ) );
      if ( aDEFER_CD_SDESC != null ) {
         Assert.assertTrue( "DEFER_CD_SDESC",
               llists.get( 0 ).get( 5 ).equalsIgnoreCase( aDEFER_CD_SDESC ) );
      }
      Assert.assertTrue( "FAULT_LOG_TYPE_CD",
            llists.get( 0 ).get( 6 ).equalsIgnoreCase( aFAULT_LOG_TYPE_CD ) );
      Assert.assertTrue( "FRM_SDESC", llists.get( 0 ).get( 7 ).equalsIgnoreCase( aFRM_CD ) );

   }


   /**
    * This function is to verify SCHED_STASK table details part
    *
    *
    */
   public void verifySCHEDSTASK( simpleIDs aTSIDs, simpleIDs aCFIDs, simpleIDs aPriorityIds,
         simpleIDs aTasKCDs, simpleIDs aINVIDs ) {

      // SCHED_STASK table
      String[] iIds = { "TASK_PRIORITY_DB_ID", "TASK_PRIORITY_CD", "TASK_CLASS_DB_ID",
            "TASK_CLASS_CD", "MAIN_INV_NO_DB_ID", "MAIN_INV_NO_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SCHED_DB_ID", aTSIDs.getNO_DB_ID() );
      lArgs.addArguments( "SCHED_ID", aTSIDs.getNO_ID() );
      lArgs.addArguments( "FAULT_DB_ID", aCFIDs.getNO_DB_ID() );
      lArgs.addArguments( "FAULT_ID", aCFIDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.SCHED_STASK, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "TASK_PRIORITY_DB_ID",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aPriorityIds.getNO_DB_ID() ) );
      Assert.assertTrue( "TASK_PRIORITY_CD",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aPriorityIds.getNO_ID() ) );
      Assert.assertTrue( "TASK_CLASS_DB_ID",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aTasKCDs.getNO_DB_ID() ) );
      Assert.assertTrue( "TASK_CLASS_CD",
            llists.get( 0 ).get( 3 ).equalsIgnoreCase( aTasKCDs.getNO_ID() ) );
      Assert.assertTrue( "MAIN_INV_NO_DB_ID",
            llists.get( 0 ).get( 4 ).equalsIgnoreCase( aINVIDs.getNO_DB_ID() ) );
      Assert.assertTrue( "MAIN_INV_NO_ID",
            llists.get( 0 ).get( 5 ).equalsIgnoreCase( aINVIDs.getNO_ID() ) );

   }


   /**
    * This function is to retrieve task class information from ref_task_class table.
    *
    *
    */
   public simpleIDs getTaskCDs( String aTASK_CD ) {

      // REF_TASK_CLASS table
      String[] iIds = { "TASK_CLASS_DB_ID", "TASK_CLASS_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_CLASS_CD", aTASK_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.REF_TASK_CLASS, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;

   }


   /**
    * This function is to retrieve task priority information from ref_task_priority table.
    *
    *
    */
   public simpleIDs getPriorityIDs( String aTASK_PRIORITY_CD ) {
      // ref_task_priority table
      String[] iIds = { "TASK_PRIORITY_DB_ID", "TASK_PRIORITY_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_PRIORITY_CD", aTASK_PRIORITY_CD );

      String iQueryString =
            TableUtil.buildTableQuery( TableUtil.REF_TASK_PRIORITY, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;

   }


   /**
    * This function is to verify EVT_SCHED_DEAD table
    *
    * @throws ParseException
    *
    *
    */
   public int verifyEVTSCHEDDEAD( simpleIDs aEvtIDs, simpleIDs aTypeIds, String aDUE_QT,
         String aCurrent_Usage, String aRate, String aSCHED_FROM_CD, String aSTART_QT,
         String aINTERVAL_QT ) throws ParseException {
      String[] iIds = { "SCHED_FROM_CD", "START_QT", "INTERVAL_QT", "SCHED_DEAD_QT",
            "SCHED_DEAD_DT", "SCHED_DRIVER_BOOL" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_DB_ID", aEvtIDs.getNO_DB_ID() );
      lArgs.addArguments( "EVENT_ID", aEvtIDs.getNO_ID() );
      lArgs.addArguments( "DATA_TYPE_DB_ID", aTypeIds.getNO_DB_ID() );
      lArgs.addArguments( "DATA_TYPE_ID", aTypeIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EVT_SCHED_DEAD, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "SCHED_FROM_CD",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aSCHED_FROM_CD ) );
      Assert.assertTrue( "START_QT", llists.get( 0 ).get( 1 ).equalsIgnoreCase( aSTART_QT ) );
      Assert.assertTrue( "INTERVAL_QT", llists.get( 0 ).get( 2 ).equalsIgnoreCase( aINTERVAL_QT ) );
      Assert.assertTrue( "SCHED_DEAD_QT", llists.get( 0 ).get( 3 ).equalsIgnoreCase( aDUE_QT ) );

      // calculate sched_dead_dt
      int dateadd = calDeadlineDate( Integer.parseInt( aDUE_QT ),
            Integer.parseInt( aCurrent_Usage ), Integer.parseInt( aRate ) );

      String pattern = "yyyy-MM-dd";
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat( pattern );
      String strSchedDeadDT = simpleDateFormat.format( new Date() );
      Date lSched_dead_dt = DateAddDays( strSchedDeadDT, dateadd );

      Assert.assertTrue( "SCHED_DEAD_DT",
            DateDiffInDays(
                  new java.sql.Date( simpleDateFormat
                        .parse( llists.get( 0 ).get( 4 ).substring( 0, 10 ) ).getTime() ),
                  new java.sql.Date( lSched_dead_dt.getTime() ) ) == 0 );

      return Integer.parseInt( llists.get( 0 ).get( 5 ) );

   }


   public int calDeadlineDate( int aDue_QT, int aCurrentUsage, int aRate ) {

      int returnvalue = ( aDue_QT - aCurrentUsage ) / aRate;

      return returnvalue >= 0 ? returnvalue : -1;
   }


   /**
    * This function is to verify EVT_SCHED_DEAD table
    *
    *
    */
   public void verifyEVTSCHEDDEAD_DAY( simpleIDs aEvtIDs, simpleIDs aTypeIds, Date aSCHED_DEAD_DT,
         String aSCHED_DRIVER_BOOL, Date aSTART_DT ) {
      String[] iIds = { "SCHED_DEAD_DT", "SCHED_DRIVER_BOOL", "START_DT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_DB_ID", aEvtIDs.getNO_DB_ID() );
      lArgs.addArguments( "EVENT_ID", aEvtIDs.getNO_ID() );
      lArgs.addArguments( "DATA_TYPE_DB_ID", aTypeIds.getNO_DB_ID() );
      lArgs.addArguments( "DATA_TYPE_ID", aTypeIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EVT_SCHED_DEAD, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      String pattern = "yyyy-MM-dd";
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat( pattern );
      String strSchedDeadDT = simpleDateFormat.format( aSCHED_DEAD_DT );

      try {
         Assert.assertTrue( "SCHED_DEAD_DT", DateDiffInDays(
               new java.sql.Date( simpleDateFormat
                     .parse( llists.get( 0 ).get( 0 ).substring( 0, 10 ) ).getTime() ),
               new java.sql.Date( simpleDateFormat.parse( strSchedDeadDT ).getTime() ) ) == 0 );
      } catch ( Exception e ) {
         e.printStackTrace();
         Assert.assertTrue( "VerifyEvtSchedDead_CA.", false );
      }

      Assert.assertTrue( "SCHED_DRIVER_BOOL",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aSCHED_DRIVER_BOOL ) );

      String strStartDT = simpleDateFormat.format( aSTART_DT );

      try {
         Assert.assertTrue( "START_DT",
               DateDiffInDays(
                     new java.sql.Date( simpleDateFormat
                           .parse( llists.get( 0 ).get( 2 ).substring( 0, 10 ) ).getTime() ),
                     new java.sql.Date( simpleDateFormat.parse( strStartDT ).getTime() ) ) == 0 );
      } catch ( Exception e ) {
         e.printStackTrace();
         Assert.assertTrue( "VerifyEvtSchedDead_CA.", false );
      }

   }


   /**
    * This function is to verify EVT_SCHED_DEAD table
    *
    *
    */
   public void verifyEVTSCHEDDEAD_NOTEXIST( simpleIDs aEvtIDs, simpleIDs aTypeIds ) {
      String lQuery = "Select 1 from " + TableUtil.EVT_SCHED_DEAD + " where EVENT_DB_ID="
            + aEvtIDs.getNO_DB_ID() + " and EVENT_ID=" + aEvtIDs.getNO_ID()
            + " and DATA_TYPE_DB_ID=" + aTypeIds.getNO_DB_ID() + " and DATA_TYPE_ID="
            + aTypeIds.getNO_ID();

      Assert.assertFalse( "Check EVT_SCHED_DEAD table to verify the record exists.",
            RecordsExist( lQuery ) );

   }


   /**
    * This function is to retrieve data type information from mim_data_type table.
    *
    *
    */
   public simpleIDs getMIMType( String aType ) {

      // MIM_DATA_TYPE table
      String[] iIds = { "DATA_TYPE_DB_ID", "DATA_TYPE_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "DATA_TYPE_CD", aType );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.MIM_DATA_TYPE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;

   }


   /**
    * This function is to verify EVT_STAGE table
    *
    *
    */
   public void verifyEVTSTAGE( simpleIDs aIDs, List<String> alist ) {
      for ( int i = 0; i < alist.size(); i++ ) {
         String lQuery = "Select 1 from " + TableUtil.EVT_STAGE + " where EVENT_DB_ID="
               + aIDs.getNO_DB_ID() + " and EVENT_ID=" + aIDs.getNO_ID()
               + " and upper(STAGE_NOTE) like '%" + alist.get( i ) + "%'";

         Assert.assertTrue(
               "Check EVT_STAGE table to verify the record: " + alist.get( i ) + " exists.",
               RecordsExist( lQuery ) );
      }

   }


   /**
    * This function is to verify EVT_EVENT_REL table
    *
    *
    */
   public void verifyEVTEVENT_REL( simpleIDs aEvtIDs, simpleIDs aRelIDs, String aRelTypeCD ) {

      String lQuery = "Select 1 from " + TableUtil.EVT_EVENT_REL + " where EVENT_DB_ID="
            + aEvtIDs.getNO_DB_ID() + " and EVENT_ID=" + aEvtIDs.getNO_ID()
            + " and REL_EVENT_DB_ID=" + aRelIDs.getNO_DB_ID() + " and REL_EVENT_ID="
            + aRelIDs.getNO_ID() + " and REL_TYPE_CD='" + aRelTypeCD + "'";

      Assert.assertTrue( "Check EVT_EVENT_REl table to verify the record exists",
            RecordsExist( lQuery ) );
   }


   /**
    * This function is to verify EVT_INV table
    *
    *
    */
   public invIDs verifyEVTINV_ENG( simpleIDs aIDs, String sAssmbl_CD ) {

      // EVT_INV table
      String[] iIds = { "INV_NO_DB_ID", "INV_NO_ID", "ASSMBL_INV_NO_DB_ID", "ASSMBL_INV_NO_ID",
            "ASSMBL_CD", "ASSMBL_BOM_ID", "ASSMBL_POS_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "EVENT_ID", aIDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EVT_INV, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      invIDs lIds = new invIDs( new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) ),
            new simpleIDs( llists.get( 0 ).get( 2 ), llists.get( 0 ).get( 3 ) ) );

      Assert.assertTrue( "ASSMBL_CD", llists.get( 0 ).get( 4 ).equalsIgnoreCase( sAssmbl_CD ) );

      return lIds;

   }


   /**
    * This function is to verify EVT_INV table
    *
    *
    */
   public invIDs verifyEVTINV( simpleIDs aIDs, String sAssmbl_CD, String aBomID, String aPosID ) {

      // EVT_INV table
      String[] iIds = { "INV_NO_DB_ID", "INV_NO_ID", "ASSMBL_INV_NO_DB_ID", "ASSMBL_INV_NO_ID",
            "ASSMBL_CD", "ASSMBL_BOM_ID", "ASSMBL_POS_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "EVENT_ID", aIDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EVT_INV, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      invIDs lIds = new invIDs( new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) ),
            new simpleIDs( llists.get( 0 ).get( 2 ), llists.get( 0 ).get( 3 ) ) );

      Assert.assertTrue( "ASSMBL_CD", llists.get( 0 ).get( 4 ).equalsIgnoreCase( sAssmbl_CD ) );
      Assert.assertTrue( "ASSMBL_BOM_ID", llists.get( 0 ).get( 5 ).equalsIgnoreCase( aBomID ) );
      Assert.assertTrue( "ASSMBL_POS_ID", llists.get( 0 ).get( 6 ).equalsIgnoreCase( aPosID ) );

      return lIds;

   }


   /**
    * This function is to verify EVT_EVENT table
    *
    * @return event ID
    *
    */
   public simpleIDs verifyEVTEVENT( String aEventTypeCd, String aSDESC, String aStatusCD,
         String aLDESC, String aDOCRefSDESC ) {

      // EVT_EVENT table
      String[] iIds =
            { "EVENT_DB_ID", "EVENT_ID", "EVENT_STATUS_CD", "EVENT_LDESC", "DOC_REF_SDESC" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_TYPE_CD", aEventTypeCd );
      lArgs.addArguments( "EVENT_SDESC", aSDESC );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EVT_EVENT, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      Assert.assertTrue( "EVENT_STATUS_CD",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aStatusCD ) );
      Assert.assertTrue( "EVENT_EVENT_LDESC", llists.get( 0 ).get( 3 ).contains( aLDESC ) );
      if ( aDOCRefSDESC != null ) {
         Assert.assertTrue( "DOC_REF_SDESC",
               llists.get( 0 ).get( 4 ).equalsIgnoreCase( aDOCRefSDESC ) );
      }

      return lIds;

   }


   /**
    * This function is to set current date on DUE_DT column of AL_OPEN_DEFERRED_FAULT
    *
    *
    *
    */

   public void setCurrentDate( String aSN ) {
      String aUpdateQuery =
            "UPDATE AL_OPEN_DEFERRED_FAULT SET DUE_DT= (SELECT SYSDATE FROM DUAL) where SERIAL_NO_OEM='"
                  + aSN + "'";
      try {

         PreparedStatement lStatement = getConnection().prepareStatement( aUpdateQuery );
         lStatement.executeUpdate( aUpdateQuery );
         commit();

      } catch ( SQLException e ) {
         e.printStackTrace();
      }
   }


   /**
    * This function is called by before AMLODF-12025 test to setup PCT=2 of SYS-1-1
    *
    *
    */

   public void dataSetupPCT() {
      String aUpdateQuery = "UPDATE eqp_assmbl_bom SET POS_CT= '2' where assmbl_bom_cd='SYS-1-1'";

      try {

         PreparedStatement lStatement = getConnection().prepareStatement( aUpdateQuery );
         lStatement.executeUpdate( aUpdateQuery );
         commit();

      } catch ( SQLException e ) {
         e.printStackTrace();
      }

   }


   /**
    * This function is called by before AMLODF-12110 test to setup precision as 2
    *
    *
    */

   public void dataSetupPrecision() {
      String aUpdateQuery = "UPDATE ref_qty_unit SET decimal_places_qt= '2' where qty_unit_cd='EA'";

      try {

         PreparedStatement lStatement = getConnection().prepareStatement( aUpdateQuery );
         lStatement.executeUpdate( aUpdateQuery );
         commit();

      } catch ( SQLException e ) {
         e.printStackTrace();
      }

   }


   /**
    * This function is called by before AMLODF-12026 test to add one more position record
    *
    *
    */

   public void dataSetupPOS() {
      String lquery =
            "select assmbl_bom_id from  eqp_assmbl_bom where assmbl_bom_cd='SYS-1-1' and assmbl_cd='ACFT_CD1'";
      String lBomID = getStringValueFromQuery( lquery, "ASSMBL_BOM_ID" );
      assmblPOS ldata = getRecordToCopy( "ACFT_CD1", lBomID, "2" );

      lquery =
            "insert into eqp_assmbl_pos (ASSMBL_DB_ID, ASSMBL_CD, ASSMBL_BOM_ID, ASSMBL_POS_ID,EQP_POS_CD, NH_ASSMBL_DB_ID,"
                  + " NH_ASSMBL_CD, NH_ASSMBL_BOM_ID,NH_ASSMBL_POS_ID) "
                  + " values (4650, 'ACFT_CD1','" + ldata.getASSMBL_BOM_ID() + "','2', '"
                  + ldata.getEQP_POS_CD() + "', 4650, '" + ldata.getNH_ASSMBL_CD() + "', '"
                  + ldata.getNH_ASSMBL_BOM_ID() + "', '" + ldata.getNH_ASSMBL_POS_ID() + "')";
      executeSQL( lquery );

   }


   /**
    * This function is to retrive a record in eqp-assmbl_pos table and save it to assmblPOS object.
    *
    *
    */

   public assmblPOS getRecordToCopy( String aASSMBL_CD, String aASSMBL_BOM_ID,
         String aAssmblPOSID ) {

      // MIM_DATA_TYPE table
      String[] iIds = { "EQP_POS_CD", "NH_ASSMBL_CD", "NH_ASSMBL_BOM_ID", "NH_ASSMBL_POS_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "ASSMBL_CD", aASSMBL_CD );
      lArgs.addArguments( "ASSMBL_BOM_ID", aASSMBL_BOM_ID );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_ASSMBL_POS, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      assmblPOS ldata =
            new assmblPOS( aASSMBL_CD, aASSMBL_BOM_ID, aAssmblPOSID, llists.get( 0 ).get( 0 ),
                  llists.get( 0 ).get( 1 ), llists.get( 0 ).get( 2 ), llists.get( 0 ).get( 3 ) );

      return ldata;

   }


   /**
    * This function is to retrive parm value from UTL_CONFIG_PARM for ADHOC_CORRTASK_LABOUR
    *
    *
    */

   public String getParmValue() {
      String lquery =
            "select PARM_VALUE from UTL_CONFIG_PARM where PARM_NAME='ADHOC_CORRTASK_LABOUR'";
      return getStringValueFromQuery( lquery, "PARM_VALUE" );

   }


   /**
    * This function is to setup duplicate ACFT
    *
    *
    */
   public void dataSetupACFTDup() {
      // String lquery =
      // "update inv_inv set serial_no_oem ='sn000013' where inv_no_sdesc='Aircraft Part 1 - LOCK'";
      // runUpdate( lquery );

      String lquery =
            "update eqp_part_no set part_no_oem='ACFT_ASSY_PN1' where part_no_oem='ACFT_ASSY_PN2' and "
                  + " manufact_cd='11111' and part_no_sdesc='Aircraft Part 2'";
      runUpdate( lquery );
   }


   /**
    * This function is to restore duplicate ACFT
    *
    *
    */
   public void dataRestoreACFTDup() {
      // String lquery =
      // "update inv_inv set serial_no_oem ='SN-LOCKED' where inv_no_sdesc='Aircraft Part 1 -
      // LOCK'";
      // runUpdate( lquery );

      String lquery =
            "update eqp_part_no set part_no_oem='ACFT_ASSY_PN2' where part_no_oem='ACFT_ASSY_PN1' and "
                  + " manufact_cd='11111' and part_no_sdesc='Aircraft Part 2'";
      runUpdate( lquery );
   }


   /**
    * This function is to setup duplicate part_no_oem with different manfacture code
    *
    *
    */
   public void dataSetupMACFACTDup() {
      String lquery =
            "update inv_inv set serial_no_oem='SN000001' where serial_no_oem='SN000012' and inv_no_sdesc='Aircraft Part 1 - SCRP'";
      runUpdate( lquery );
      //
      // lquery =
      // "update eqp_part_no set part_no_oem='ACFT_ASSY_PN1' where part_no_oem='ACFT_ASSY_PN2' and
      // manufact_cd='11111' and part_no_sdesc='Aircraft Part 2'";
      // runUpdate( lquery );
   }


   /**
    * This function is to restore duplicate part_no_oem with different manfacture code
    *
    *
    */
   public void dataRestoreMACFACTDup() {
      String lquery =
            "update inv_inv set serial_no_oem='SN000012' where serial_no_oem='SN000001' and inv_no_sdesc='Aircraft Part 1 - SCRP'";
      runUpdate( lquery );

      // lquery =
      // "update eqp_part_no set part_no_oem='ACFT_ASSY_PN2' where part_no_oem='ACFT_ASSY_PN1' and
      // manufact_cd='11111' and part_no_sdesc='Aircraft Part 2'";
      // runUpdate( lquery );
   }


   /**
    * This function is to implement interface ValidationAndImport
    *
    * @param: blnOnlyValidation
    * @param: allornone
    *
    */
   public int runValidationAndImport( boolean blnOnlyValidation, boolean allornone ) {
      int rtValue = 0;
      ivalidationandimport = new ValidationAndImport() {

         @Override
         public int runValidation( boolean allornone ) {
            int lReturn = 0;
            CallableStatement lPrepareCallInventory = null;

            try {
               if ( allornone ) {
                  lPrepareCallInventory = getConnection().prepareCall(
                        "BEGIN al_open_deferred_fault_pkg.validate(aib_allornone => true, ain_gather_stats_bool => ?,aon_retcode => ?, aov_retmsg =>?); END;" );
               } else {
                  lPrepareCallInventory = getConnection().prepareCall(
                        "BEGIN al_open_deferred_fault_pkg.validate(aib_allornone => false, ain_gather_stats_bool => ?,aon_retcode => ?, aov_retmsg =>?); END;" );

               }

               lPrepareCallInventory.setInt( 1, TestConstants.GATHER_STATS_DEFAULT );
               lPrepareCallInventory.registerOutParameter( 2, Types.INTEGER );
               lPrepareCallInventory.registerOutParameter( 3, Types.VARCHAR );
               lPrepareCallInventory.execute();
               commit();
               lReturn = lPrepareCallInventory.getInt( 2 );

            } catch ( SQLException e ) {
               e.printStackTrace();
            }
            return lReturn;

         }


         @Override
         public int runImport( boolean allornone ) {
            int lReturn = 0;

            try {
               CallableStatement lPrepareCallInventory = getConnection().prepareCall(
                     "BEGIN  al_open_deferred_fault_pkg.import(ain_gather_stats_bool => ?,aon_retcode => ?, aov_retmsg =>?); END;" );

               lPrepareCallInventory.setInt( 1, TestConstants.GATHER_STATS_DEFAULT );
               lPrepareCallInventory.registerOutParameter( 2, Types.INTEGER );
               lPrepareCallInventory.registerOutParameter( 3, Types.VARCHAR );

               lPrepareCallInventory.execute();
               commit();
               lReturn = lPrepareCallInventory.getInt( 2 );

            } catch ( SQLException e ) {
               e.printStackTrace();
            }
            return lReturn;
         }

      };

      rtValue = blnOnlyValidation ? ivalidationandimport.runValidation( allornone )
            : ivalidationandimport.runImport( allornone );

      return rtValue;
   }

}
