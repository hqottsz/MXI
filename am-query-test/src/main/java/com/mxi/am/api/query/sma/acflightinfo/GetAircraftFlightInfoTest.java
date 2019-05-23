package com.mxi.am.api.query.sma.acflightinfo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.DataSetException;
import com.mxi.mx.core.key.HumanResourceKey;


public final class GetAircraftFlightInfoTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   private static final HumanResourceKey VALID_HR_WITH_ACCESS_TO_WP_ROOT_LOCATION =
         new HumanResourceKey( 1, 7 );
   private static final HumanResourceKey VALID_HR_WITH_ACCESS_TO_WP_LOCATION =
         new HumanResourceKey( 1, 8 );
   private static final HumanResourceKey VALID_HR_WITHOUT_ACCESS_TO_WP_LOCATION =
         new HumanResourceKey( 1, 9 );
   private static final HumanResourceKey INVALID_HR = new HumanResourceKey( 2, 4 );
   private static final int WITHIN_NEXT_HOURS = 8;
   private static final int WITHIN_NEXT_HOURS_VALID = 10;
   private static final int WITHIN_NEXT_HOURS_INVALID = 4;


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetAircraftFlightInfoTest.class );
   }


   /**
    * The below test validates that a user with proper authority over the inventory and flight plan
    * information
    *
    */

   @Test
   public void executeQuery_userWithAuthority() throws DataSetException, ParseException {
      DataSet lDs = executeQuery( VALID_HR_WITH_ACCESS_TO_WP_LOCATION );

      assertEquals( 1, lDs.getTotalRowCount() );
      lDs.next();

      assertEquals( "The aicraft registration code", "N021", lDs.getString( "fin" ) );
      assertEquals( "The inventory operation code", "AWR", lDs.getString( "inv_oper_cd" ) );
      assertEquals( "The assembly", "A320", lDs.getString( "assmbl_cd" ) );
      assertEquals( "The center time zone's location", "America/New_York",
            lDs.getString( "timezone_cd" ) );
      assertEquals( "The fail priority code", "ANALYZE", lDs.getString( "fail_priority_cd" ) );
      assertEquals( "The arrival flight number", "5676",
            lDs.getString( "inbound_master_flight_sdesc" ) );
      SimpleDateFormat lDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
      assertEquals( "The inbound scheduled arrival date", lDateFormat.parse( "2001-11-10 3:21:00" ),
            lDs.getDate( "inbound_sched_arr_gdt" ) );
      assertEquals( "The inbound scheduled departure date",
            lDateFormat.parse( "2001-11-10 1:00:00 AM" ), lDs.getDate( "inbound_sched_dep_gdt" ) );
      assertEquals( "The inbound actual arrival date", lDateFormat.parse( "2001-11-10 3:14:00 AM" ),
            lDs.getDate( "inbound_actual_arr_gdt" ) );
      assertEquals( "The inbound actual departure date",
            lDateFormat.parse( "2001-11-10 1:00:00 AM" ), lDs.getDate( "inbound_actual_dep_gdt" ) );
      assertEquals( "The inbound departure gate code", "31",
            lDs.getString( "inbound_dep_gate_cd" ) );
      assertEquals( "The current location", "EWR", lDs.getString( "current_location" ) );
      assertEquals( "The inbound arrival station", "EWR/1",
            lDs.getString( "inbound_arr_station" ) );
      assertEquals( "The inbound departure station", "EWR/2",
            lDs.getString( "inbound_dep_station" ) );
      assertEquals( "The outbound arrival station", "EWR/3",
            lDs.getString( "outbound_arr_station" ) );
      assertEquals( "The outbound departure station", "EWR/1",
            lDs.getString( "outbound_dep_station" ) );
      assertEquals( "The inbound arrival gate code", "34", lDs.getString( "inbound_arr_gate_cd" ) );
      assertEquals( "The inbound flight status code", "MXPLAN", lDs.getString( "inbound_status" ) );
      assertEquals( "The departure station id", "15:5",
            lDs.getString( "outbound_arr_station_id" ) );
      assertEquals( "The departure flight number", "5677",
            lDs.getString( "outbound_master_flight_sdesc" ) );
      assertEquals( "The outbound scheduled departure date",
            lDateFormat.parse( "2001-11-11 1:00:00 AM" ), lDs.getDate( "outbound_sched_dep_gdt" ) );
      assertEquals( "The outbound actual departure date",
            lDateFormat.parse( "2001-11-11 1:14:00 AM" ),
            lDs.getDate( "outbound_actual_dep_gdt" ) );
      assertEquals( "The outbound departure gate code", "35",
            lDs.getString( "outbound_dep_gate_cd" ) );
      assertEquals( 2, lDs.getInt( "count_unpackaged_faults" ) );
      assertEquals( 1, lDs.getInt( "count_packaged_faults" ) );
   }


   @Test
   public void executeQuery_userWithoutAuthority() {
      DataSet lDs = executeQuery( INVALID_HR );

      assertEquals( 0, lDs.getTotalRowCount() );
   }


   /**
    * Do not show work packages that exist for that aircraft at the station the user does NOT belong
    * to.
    */
   @Test
   public void executeQuery_NoAccessToWorkPackageLocation() {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      {
         Date lFlightInfoWindow = DateUtils.addHours( new Date(), WITHIN_NEXT_HOURS_VALID );

         lDataSetArgument.add( "aHrDbId", VALID_HR_WITHOUT_ACCESS_TO_WP_LOCATION.getDbId() );
         lDataSetArgument.add( "aHrId", VALID_HR_WITHOUT_ACCESS_TO_WP_LOCATION.getId() );
         lDataSetArgument.add( "aFlightInfoWindowDate", lFlightInfoWindow );
         lDataSetArgument.add( "aMinPriority", 0 );
      }
      DataSet lDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );

      assertEquals( 1, lDataSet.getRowCount() );
      lDataSet.next();

      assertNull( "WP 1", lDataSet.getString( "work_package_name" ) );
      assertNull( "IN WORK", lDataSet.getString( "work_package_status" ) );
   }


   /**
    * Show work packages if user belongs to the ROOT of the station where work packages exist for
    * that aircraft.
    */
   @Test
   public void executeQuery_AccessToWorkPackageRootLocation() {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      {
         Date lFlightInfoWindow = DateUtils.addHours( new Date(), WITHIN_NEXT_HOURS_VALID );

         lDataSetArgument.add( "aHrDbId", VALID_HR_WITH_ACCESS_TO_WP_ROOT_LOCATION.getDbId() );
         lDataSetArgument.add( "aHrId", VALID_HR_WITH_ACCESS_TO_WP_ROOT_LOCATION.getId() );
         lDataSetArgument.add( "aFlightInfoWindowDate", lFlightInfoWindow );
         lDataSetArgument.add( "aMinPriority", 0 );
      }
      DataSet lDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );

      assertEquals( 1, lDataSet.getRowCount() );

      lDataSet.next();

      assertEquals( "WP 1", lDataSet.getString( "work_package_name" ) );
      assertEquals( "IN WORK", lDataSet.getString( "work_package_status" ) );
      assertEquals( 1, lDataSet.getInt( "remaining_task_count" ) );
      assertEquals( 1, lDataSet.getInt( "total_task_count" ) );
   }


   /**
    * The 'WP 1' work package's scheduled start date is 8 hours from now. This test sets the window
    * to collect flight info to 10 hours from now, so data for 'WP 1' should be returned with the
    * flight info.
    *
    * Show work packages if user belongs to the station where work packages exist for that aircraft.
    */
   @Test
   public void executeQuery_WorkPackageScheduledTimeWithinBounds() {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      {
         Date lFlightInfoWindow = DateUtils.addHours( new Date(), WITHIN_NEXT_HOURS_VALID );

         lDataSetArgument.add( "aHrDbId", VALID_HR_WITH_ACCESS_TO_WP_LOCATION.getDbId() );
         lDataSetArgument.add( "aHrId", VALID_HR_WITH_ACCESS_TO_WP_LOCATION.getId() );
         lDataSetArgument.add( "aFlightInfoWindowDate", lFlightInfoWindow );
         lDataSetArgument.add( "aMinPriority", 0 );
      }
      DataSet lDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );

      assertEquals( 1, lDataSet.getRowCount() );

      lDataSet.next();

      assertEquals( "WP 1", lDataSet.getString( "work_package_name" ) );
      assertEquals( "IN WORK", lDataSet.getString( "work_package_status" ) );
      assertEquals( 1, lDataSet.getInt( "remaining_task_count" ) );
      assertEquals( 1, lDataSet.getInt( "total_task_count" ) );
   }


   /**
    * The 'WP 1' work package's scheduled start date is 8 hours from now. This test sets the window
    * to collect flight info to 4 hours from now, so data for 'WP 1' should NOT be returned with the
    * flight info.
    */
   @Test
   public void executeQuery_WorkPackageScheduledTimeOutOfBounds() {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      {
         Date lFlightInfoWindow = DateUtils.addHours( new Date(), WITHIN_NEXT_HOURS_INVALID );

         lDataSetArgument.add( "aHrDbId", VALID_HR_WITH_ACCESS_TO_WP_LOCATION.getDbId() );
         lDataSetArgument.add( "aHrId", VALID_HR_WITH_ACCESS_TO_WP_LOCATION.getId() );
         lDataSetArgument.add( "aFlightInfoWindowDate", lFlightInfoWindow );
         lDataSetArgument.add( "aMinPriority", 0 );
      }
      DataSet lDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );

      assertEquals( 1, lDataSet.getRowCount() );

      lDataSet.next();

      assertNull( lDataSet.getString( "work_package_name" ) );
      assertNull( lDataSet.getString( "work_package_status" ) );
      assertEquals( 0, lDataSet.getInt( "remaining_task_count" ) );
      assertEquals( 0, lDataSet.getInt( "total_task_count" ) );
   }


   private DataSet executeQuery( HumanResourceKey aHRKey ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      Date lFlightInfoWindow = DateUtils.addHours( new Date(), WITHIN_NEXT_HOURS );

      lDataSetArgument.add( "aHrDbId", aHRKey.getDbId() );
      lDataSetArgument.add( "aHrId", aHRKey.getId() );
      lDataSetArgument.add( "aFlightInfoWindowDate", lFlightInfoWindow );
      lDataSetArgument.add( "aMinPriority", 0 );

      DataSet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
      return lDs;
   }
}
