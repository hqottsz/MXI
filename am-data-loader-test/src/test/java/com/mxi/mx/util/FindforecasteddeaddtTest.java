package com.mxi.mx.util;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;

import com.mxi.mx.core.maint.plan.datamodels.mim_data_type;


/**
 * This test suite contains common test utilities for FindforecastdeadDT tests and data setup for
 * each test cases.
 *
 * @author Alicia Qian
 */

public class FindforecasteddeaddtTest extends AbstractDatabaseConnection {

   protected ArrayList<mim_data_type> mimDTtype = new ArrayList<mim_data_type>();
   protected String strINVDBID = null;
   protected String strINVID = null;
   protected String FCMODDBID = null;
   protected String FCMODID = null;

   String strModifiedINVDBID = null;
   String strModifiedINVID = null;

   protected String strEventDBId = null;
   protected String strEventId = null;


   /**
    * data setup
    */

   public void ClassDataSetup() {

      ResultSet ResultSetRecords;

      // get aircraft and forecast IDs information
      StringBuilder Strquery = new StringBuilder();
      Strquery.append(
            "select INV_NO_DB_ID, INV_NO_ID,FORECAST_MODEL_DB_ID,FORECAST_MODEL_ID from INV_AC_REG " );
      Strquery.append( "WHERE AC_REG_CD='001'" );

      StringBuilder mimDTtypequery = new StringBuilder();

      try {
         ResultSetRecords = runQuery( Strquery.toString() );
         ResultSetRecords.next();
         strINVDBID = ResultSetRecords.getString( "INV_NO_DB_ID" );
         strINVID = ResultSetRecords.getString( "INV_NO_ID" );
         FCMODDBID = ResultSetRecords.getString( "FORECAST_MODEL_DB_ID" );
         FCMODID = ResultSetRecords.getString( "FORECAST_MODEL_ID" );
         ResultSetRecords.close();

         // update fc_range table
         StringBuilder[] updateTables = new StringBuilder[11];

         updateTables[0] = new StringBuilder(
               "insert into fc_range (MODEL_DB_ID, MODEL_ID,RANGE_ID, START_MONTH, START_DAY, RSTAT_CD) values (" );
         updateTables[0].append( FCMODDBID ).append( "," ).append( FCMODID ).append( ",2,2,1,0)" );

         updateTables[1] = new StringBuilder(
               "insert into fc_range (MODEL_DB_ID, MODEL_ID,RANGE_ID, START_MONTH, START_DAY, RSTAT_CD) values (" );
         updateTables[1].append( FCMODDBID ).append( "," ).append( FCMODID ).append( ",3,3,1,0)" );

         updateTables[2] = new StringBuilder(
               "insert into fc_range (MODEL_DB_ID, MODEL_ID,RANGE_ID, START_MONTH, START_DAY, RSTAT_CD) values (" );
         updateTables[2].append( FCMODDBID ).append( "," ).append( FCMODID ).append( ",4,4,1,0)" );

         updateTables[3] = new StringBuilder(
               "insert into fc_range (MODEL_DB_ID, MODEL_ID,RANGE_ID, START_MONTH, START_DAY, RSTAT_CD) values (" );
         updateTables[3].append( FCMODDBID ).append( "," ).append( FCMODID ).append( ",5,5,1,0)" );

         updateTables[4] = new StringBuilder(
               "insert into fc_range (MODEL_DB_ID, MODEL_ID,RANGE_ID, START_MONTH, START_DAY, RSTAT_CD) values (" );
         updateTables[4].append( FCMODDBID ).append( "," ).append( FCMODID ).append( ",6,6,1,0)" );

         updateTables[5] = new StringBuilder(
               "insert into fc_range (MODEL_DB_ID, MODEL_ID,RANGE_ID, START_MONTH, START_DAY, RSTAT_CD) values (" );
         updateTables[5].append( FCMODDBID ).append( "," ).append( FCMODID ).append( ",7,7,1,0)" );

         updateTables[6] = new StringBuilder(
               "insert into fc_range (MODEL_DB_ID, MODEL_ID,RANGE_ID, START_MONTH, START_DAY, RSTAT_CD) values (" );
         updateTables[6].append( FCMODDBID ).append( "," ).append( FCMODID ).append( ",8,8,1,0)" );

         updateTables[7] = new StringBuilder(
               "insert into fc_range (MODEL_DB_ID, MODEL_ID,RANGE_ID, START_MONTH, START_DAY, RSTAT_CD) values (" );
         updateTables[7].append( FCMODDBID ).append( "," ).append( FCMODID ).append( ",9,9,1,0)" );

         updateTables[8] = new StringBuilder(
               "insert into fc_range (MODEL_DB_ID, MODEL_ID,RANGE_ID, START_MONTH, START_DAY, RSTAT_CD) values (" );
         updateTables[8].append( FCMODDBID ).append( "," ).append( FCMODID )
               .append( ",10,10,1,0)" );

         updateTables[9] = new StringBuilder(
               "insert into fc_range (MODEL_DB_ID, MODEL_ID,RANGE_ID, START_MONTH, START_DAY, RSTAT_CD) values (" );
         updateTables[9].append( FCMODDBID ).append( "," ).append( FCMODID )
               .append( ",11,11,1,0)" );

         updateTables[10] = new StringBuilder(
               "insert into fc_range (MODEL_DB_ID, MODEL_ID,RANGE_ID, START_MONTH, START_DAY, RSTAT_CD) values (" );
         updateTables[10].append( FCMODDBID ).append( "," ).append( FCMODID )
               .append( ",12,12,1,0)" );

         for ( int i = 0; i < updateTables.length; i++ ) {
            PreparedStatement lStatement =
                  getConnection().prepareStatement( updateTables[i].toString() );
            lStatement.executeUpdate( updateTables[i].toString() );
            commit();
         }

         // Get MIM_DATA_TYPE
         mimDTtypequery.append( "select DATA_TYPE_DB_ID, DATA_TYPE_ID from fc_rate " );
         mimDTtypequery.append( "where MODEL_DB_ID=" + FCMODDBID + " and MODEL_ID=" + FCMODID );

         ResultSetRecords = runQuery( mimDTtypequery.toString() );
         while ( ResultSetRecords.next() ) {
            mim_data_type mdtype =
                  new mim_data_type( ResultSetRecords.getString( "DATA_TYPE_DB_ID" ),
                        ResultSetRecords.getString( "DATA_TYPE_ID" ) );
            mimDTtype.add( mdtype );

         }

         ResultSetRecords.close();
         // update fc_rate table

         for ( int i = 0; i < mimDTtype.size(); i++ ) {
            String[] updateMimTables = new String[11];
            updateMimTables[0] =
                  "insert into fc_rate (MODEL_DB_ID, MODEL_ID,RANGE_ID,DATA_TYPE_DB_ID, DATA_TYPE_ID,FORECAST_RATE_QT) values ("
                        + FCMODDBID + "," + FCMODID + "," + "2,"
                        + mimDTtype.get( i ).getDATA_TYPE_DB_ID() + ","
                        + mimDTtype.get( i ).getDATA_TYPE_ID() + ",1)";
            updateMimTables[1] =
                  "insert into fc_rate (MODEL_DB_ID, MODEL_ID,RANGE_ID,DATA_TYPE_DB_ID, DATA_TYPE_ID,FORECAST_RATE_QT) values ("
                        + FCMODDBID + "," + FCMODID + "," + "3,"
                        + mimDTtype.get( i ).getDATA_TYPE_DB_ID() + ","
                        + mimDTtype.get( i ).getDATA_TYPE_ID() + ",1)";
            updateMimTables[2] =
                  "insert into fc_rate (MODEL_DB_ID, MODEL_ID,RANGE_ID,DATA_TYPE_DB_ID, DATA_TYPE_ID,FORECAST_RATE_QT) values ("
                        + FCMODDBID + "," + FCMODID + "," + "4,"
                        + mimDTtype.get( i ).getDATA_TYPE_DB_ID() + ","
                        + mimDTtype.get( i ).getDATA_TYPE_ID() + ",1)";
            updateMimTables[3] =
                  "insert into fc_rate (MODEL_DB_ID, MODEL_ID,RANGE_ID,DATA_TYPE_DB_ID, DATA_TYPE_ID,FORECAST_RATE_QT) values ("
                        + FCMODDBID + "," + FCMODID + "," + "5,"
                        + mimDTtype.get( i ).getDATA_TYPE_DB_ID() + ","
                        + mimDTtype.get( i ).getDATA_TYPE_ID() + ",1)";
            updateMimTables[4] =
                  "insert into fc_rate (MODEL_DB_ID, MODEL_ID,RANGE_ID,DATA_TYPE_DB_ID, DATA_TYPE_ID,FORECAST_RATE_QT) values ("
                        + FCMODDBID + "," + FCMODID + "," + "6,"
                        + mimDTtype.get( i ).getDATA_TYPE_DB_ID() + ","
                        + mimDTtype.get( i ).getDATA_TYPE_ID() + ",1)";
            updateMimTables[5] =
                  "insert into fc_rate (MODEL_DB_ID, MODEL_ID,RANGE_ID,DATA_TYPE_DB_ID, DATA_TYPE_ID,FORECAST_RATE_QT) values ("
                        + FCMODDBID + "," + FCMODID + "," + "7,"
                        + mimDTtype.get( i ).getDATA_TYPE_DB_ID() + ","
                        + mimDTtype.get( i ).getDATA_TYPE_ID() + ",1)";
            updateMimTables[6] =
                  "insert into fc_rate (MODEL_DB_ID, MODEL_ID,RANGE_ID,DATA_TYPE_DB_ID, DATA_TYPE_ID,FORECAST_RATE_QT) values ("
                        + FCMODDBID + "," + FCMODID + "," + "8,"
                        + mimDTtype.get( i ).getDATA_TYPE_DB_ID() + ","
                        + mimDTtype.get( i ).getDATA_TYPE_ID() + ",1)";
            updateMimTables[7] =
                  "insert into fc_rate (MODEL_DB_ID, MODEL_ID,RANGE_ID,DATA_TYPE_DB_ID, DATA_TYPE_ID,FORECAST_RATE_QT) values ("
                        + FCMODDBID + "," + FCMODID + "," + "9,"
                        + mimDTtype.get( i ).getDATA_TYPE_DB_ID() + ","
                        + mimDTtype.get( i ).getDATA_TYPE_ID() + ",1)";
            updateMimTables[8] =
                  "insert into fc_rate (MODEL_DB_ID, MODEL_ID,RANGE_ID,DATA_TYPE_DB_ID, DATA_TYPE_ID,FORECAST_RATE_QT) values ("
                        + FCMODDBID + "," + FCMODID + "," + "10,"
                        + mimDTtype.get( i ).getDATA_TYPE_DB_ID() + ","
                        + mimDTtype.get( i ).getDATA_TYPE_ID() + ",1)";
            updateMimTables[9] =
                  "insert into fc_rate (MODEL_DB_ID, MODEL_ID,RANGE_ID,DATA_TYPE_DB_ID, DATA_TYPE_ID,FORECAST_RATE_QT) values ("
                        + FCMODDBID + "," + FCMODID + "," + "11,"
                        + mimDTtype.get( i ).getDATA_TYPE_DB_ID() + ","
                        + mimDTtype.get( i ).getDATA_TYPE_ID() + ",1)";
            updateMimTables[10] =
                  "insert into fc_rate (MODEL_DB_ID, MODEL_ID,RANGE_ID,DATA_TYPE_DB_ID, DATA_TYPE_ID,FORECAST_RATE_QT) values ("
                        + FCMODDBID + "," + FCMODID + "," + "12,"
                        + mimDTtype.get( i ).getDATA_TYPE_DB_ID() + ","
                        + mimDTtype.get( i ).getDATA_TYPE_ID() + ",1)";
            for ( int j = 0; j < updateMimTables.length; j++ ) {
               PreparedStatement lStatement =
                     getConnection().prepareStatement( updateMimTables[j] );
               lStatement.executeUpdate( updateMimTables[j] );
               commit();
            }
         }
         // Create event of blk for in eve_event
         PreparedStatement lStatement;
         StringBuilder StrModifiedquery = new StringBuilder();
         StrModifiedquery
               .append(
                     "insert into evt_event (event_db_id, event_id, event_type_db_id, event_type_cd," )
               .append( " event_status_cd, event_sdesc) values (" )
               .append( "4650, event_id_seq.NEXTVAL, 0, 'BLK', 'ACTV', 'AUTOEVENT')" );
         lStatement = getConnection().prepareStatement( StrModifiedquery.toString() );
         lStatement.executeUpdate( StrModifiedquery.toString() );
         commit();

         // Get event_id which just created
         StrModifiedquery.delete( 0, StrModifiedquery.length() );
         StrModifiedquery.append( "select event_id from evt_event where event_sdesc='AUTOEVENT'" );
         ResultSetRecords = runQuery( StrModifiedquery.toString() );
         ResultSetRecords.next();
         strEventId = ResultSetRecords.getString( "event_id" );

         // Create event for ACFT in inv_event
         StrModifiedquery.delete( 0, StrModifiedquery.length() );
         StrModifiedquery
               .append(
                     "insert into evt_inv (event_db_id, event_id, event_inv_id, inv_no_db_id, inv_no_id)" )
               .append( " values (4650," ).append( strEventId ).append( " , 1, " )
               .append( strINVDBID ).append( " , " ).append( strINVID ).append( ")" );
         lStatement = getConnection().prepareStatement( StrModifiedquery.toString() );
         lStatement.executeUpdate( StrModifiedquery.toString() );
         commit();

         // Get event IDs
         StrModifiedquery.delete( 0, StrModifiedquery.length() );
         StrModifiedquery
               .append(
                     "select evt_event.event_db_id, evt_event.event_id from evt_event inner join evt_inv on " )
               .append( "evt_event.event_db_id=evt_inv.event_db_id and " )
               .append(
                     "evt_event.event_id=evt_inv.event_id where evt_event.event_sdesc='AUTOEVENT' and evt_event.event_status_cd='ACTV' and " )
               .append( "evt_inv.inv_no_db_id=" ).append( strINVDBID )
               .append( " and evt_inv.inv_no_id=" ).append( strINVID );

         ResultSetRecords = runQuery( StrModifiedquery.toString() );

         ResultSetRecords.next();
         strEventDBId = ResultSetRecords.getString( "event_db_id" );
         strEventId = ResultSetRecords.getString( "event_id" );

         StrModifiedquery.delete( 0, StrModifiedquery.length() );

      } catch ( SQLException e ) {
         e.printStackTrace();
      }

   }


   /**
    * data restore after tests
    */
   public void ClassDataRestore() {
      // delete fc_range and fc_rate tables data
      String StrFCrange = "Delete from fc_range where MODEL_DB_ID=" + FCMODDBID + " AND MODEL_ID="
            + FCMODID + " AND RANGE_ID<>1 ";
      String StrFCRate = "Delete from fc_rate where MODEL_DB_ID=" + FCMODDBID + " AND MODEL_ID="
            + FCMODID + " AND RANGE_ID<>1 ";

      String StrEvtInv =
            "delete evt_inv where event_db_id=" + strEventDBId + " and event_id=" + strEventId;
      String StrEvtEvent = "delete evt_event where event_db_id=" + strEventDBId + " and event_id="
            + strEventId + " and event_type_cd='BLK'";

      PreparedStatement lStatement;
      try {
         // delete fc_range table
         lStatement = getConnection().prepareStatement( StrFCrange );
         lStatement.executeUpdate( StrFCrange );
         commit();

         // delete fc_rate table
         lStatement = getConnection().prepareStatement( StrFCRate );
         lStatement.executeUpdate( StrFCRate );
         commit();

         // delete added record in evt_inv table
         lStatement = getConnection().prepareStatement( StrEvtInv );
         lStatement.executeUpdate( StrEvtInv );
         commit();

         // Delete added record in evt_event table
         lStatement = getConnection().prepareStatement( StrEvtEvent );
         lStatement.executeUpdate( StrEvtEvent );
         commit();

      } catch ( SQLException e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }


   /**
    * call event_pkg.findforecasteddeaddt
    *
    * @parameters: aircraftDBid, aircraftid, datatypeDBid, datatypeid, remaining Usage and startDate
    * @return: forecast date
    *
    */
   protected Date runValidateFindForecast( int aaircraftdbid, int aaircraftid, int adatatypedbid,
         int adatatypeid, int aremainingusageqt, Date astartdt ) {

      Date lReturn = null;
      CallableStatement lPrepareCallFindForecast;
      try {
         lPrepareCallFindForecast = getConnection()
               .prepareCall( "BEGIN   event_pkg.findforecasteddeaddt(aaircraftdbid => ?,"
                     + "aaircraftid =>?, adatatypedbid => ?,"
                     + "adatatypeid =>?,aremainingusageqt =>?," + "astartdt => ?,aforecastdt =>?,"
                     + "ol_return => ?); END;" );

         lPrepareCallFindForecast.setInt( 1, aaircraftdbid );
         lPrepareCallFindForecast.setInt( 2, aaircraftid );
         lPrepareCallFindForecast.setInt( 3, adatatypedbid );
         lPrepareCallFindForecast.setInt( 4, adatatypeid );
         lPrepareCallFindForecast.setInt( 5, aremainingusageqt );
         lPrepareCallFindForecast.setDate( 6, astartdt );
         lPrepareCallFindForecast.registerOutParameter( 7, Types.DATE );
         lPrepareCallFindForecast.registerOutParameter( 8, Types.INTEGER );

         lPrepareCallFindForecast.execute();
         commit();
         lReturn = lPrepareCallFindForecast.getDate( 7 );
      } catch ( SQLException e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      return lReturn;

   }


   /**
    * Add blk with given start and end date
    *
    * @parameters: startDate and endDate
    *
    */

   public void addBLKSCHEDDate( java.sql.Date startDate, java.sql.Date endDate ) {
      StringBuilder StrModifiedquery = new StringBuilder();
      PreparedStatement lStatement;

      // change event actual start data and end date
      try {
         StrModifiedquery.append( "update evt_event set ACTUAL_START_DT=" ).append( "to_date('" )
               .append( startDate ).append( "', 'YYYY-MM-DD')" ).append( ", EVENT_DT=" )
               .append( "to_date('" ).append( endDate ).append( "', 'YYYY-MM-DD')" )
               .append( " where event_db_id=" ).append( strEventDBId ).append( " and event_id=" )
               .append( strEventId );
         lStatement = getConnection().prepareStatement( StrModifiedquery.toString() );
         lStatement.executeUpdate( StrModifiedquery.toString() );
         commit();
      } catch ( SQLException e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }


   /**
    * Set blk actual start and end date as null this is used by restoring data step.
    *
    */

   public void setNullBLKSCHEDDate() {
      StringBuilder StrModifiedquery = new StringBuilder();
      PreparedStatement lStatement;

      // change event schedule start data and end date
      try {
         StrModifiedquery.append( "update evt_event set ACTUAL_START_DT= Null" )
               .append( ", EVENT_DT= Null" ).append( " where event_db_id=" ).append( strEventDBId )
               .append( " and event_id=" ).append( strEventId );
         lStatement = getConnection().prepareStatement( StrModifiedquery.toString() );
         lStatement.executeUpdate( StrModifiedquery.toString() );
         commit();
      } catch ( SQLException e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }


   /**
    * This is common function to add days into current date
    */

   public java.sql.Date addDaysToCurrentDate( int days ) {
      Calendar cNow = Calendar.getInstance();
      // java.util.Date dNow = cNow.getTime();
      cNow.add( Calendar.DATE, days );

      java.util.Date addedDate = cNow.getTime();
      java.sql.Date newDate = convertFromJAVADateToSQLDate( addedDate );

      return newDate;
   }


   /**
    * convert date of sql to date of util
    */

   public static java.util.Date convertFromSQLDateToJAVADate( java.sql.Date sqlDate ) {
      java.util.Date javaDate = null;
      if ( sqlDate != null ) {
         javaDate = new Date( sqlDate.getTime() );
      }
      return javaDate;
   }


   /**
    * convert date of util to date of sql
    */

   public static java.sql.Date convertFromJAVADateToSQLDate( java.util.Date javaDate ) {
      java.sql.Date sqlDate = null;
      if ( javaDate != null ) {
         sqlDate = new Date( javaDate.getTime() );
      }
      return sqlDate;
   }


   /**
    * check date difference between date1 and date2
    *
    * @return: day(s) of date difference
    */
   public long DateDiffInDays( java.util.Date d1, java.util.Date d2 ) {

      long diffDays = 0;

      ResultSet ResultSetRecords;

      String Strquery = "SELECT TO_DATE('" + d2 + "', 'YYYY-MM-DD') - TO_DATE('" + d1
            + "', 'YYYY-MM-DD') AS DateDiff FROM dual";

      try {
         ResultSetRecords = runQuery( Strquery.toString() );
         ResultSetRecords.next();
         diffDays = ResultSetRecords.getLong( "DateDiff" );
      } catch ( SQLException e ) {
         e.printStackTrace();

      }

      return diffDays;

   }
}
