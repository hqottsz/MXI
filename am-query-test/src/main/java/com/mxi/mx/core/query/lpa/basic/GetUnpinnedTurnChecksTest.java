
package com.mxi.mx.core.query.lpa.basic;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.LpaFleetKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.lpa.basic.LpaFleetTable;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the query file with the same package and name.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class GetUnpinnedTurnChecksTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void setUp() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
   }


   /** May 15, 2012 */
   private static final GregorianCalendar MAY_15_2012 = new GregorianCalendar( 2012, 4, 15 );

   /** May 14, 2012 */
   private static final GregorianCalendar MAY_14_2012 = new GregorianCalendar( 2012, 4, 14 );
   private static final GregorianCalendar SCHED_START_DATE1 =
         new GregorianCalendar( 2012, 4, 15, 0, 0 );
   private static final GregorianCalendar SCHED_END_DATE1 =
         new GregorianCalendar( 2012, 4, 15, 12, 0 );
   private static final TaskKey ACTUAL_SRVC_CHK_1 = new TaskKey( 4650, 4001 );
   private static final TaskKey ACTUAL_SRVC_CHK_4 = new TaskKey( 4650, 4004 );
   private static final TaskKey ACTUAL_SRVC_CHK_5 = new TaskKey( 4650, 4005 );
   private static final TaskKey ACTUAL_SRVC_CHK_6 = new TaskKey( 4650, 4006 );
   private static final AircraftKey AIRCRAFT_1 = new AircraftKey( 4650, 1001 );
   private static final AircraftKey AIRCRAFT_2 = new AircraftKey( 4650, 1002 );
   private static final AircraftKey AIRCRAFT_3 = new AircraftKey( 4650, 1003 );
   private static final AircraftKey AIRCRAFT_4 = new AircraftKey( 4650, 1004 );
   private static final AircraftKey AIRCRAFT_5 = new AircraftKey( 4650, 1005 );
   private static final AircraftKey AIRCRAFT_6 = new AircraftKey( 4650, 1006 );
   private static final AircraftKey AIRCRAFT_7 = new AircraftKey( 4650, 1007 );
   private static final AircraftKey AIRCRAFT_8 = new AircraftKey( 4650, 1008 );
   private static final AircraftKey AIRCRAFT_9 = new AircraftKey( 4650, 1009 );
   private static final AircraftKey AIRCRAFT_10 = new AircraftKey( 4650, 1010 );
   private static final LocationKey AIRPORT_1 = new LocationKey( 4650, 7001 );
   private static final LocationKey LOCATION_1 = new LocationKey( 4650, 6001 );
   private static final TaskDefnKey SRVC_CHK_DEFN_1 = new TaskDefnKey( 4650, 2001 );
   private static final TaskDefnKey SRVC_CHK_DEFN_3 = new TaskDefnKey( 4650, 2003 );
   private static final TaskDefnKey SRVC_CHK_DEFN_4 = new TaskDefnKey( 4650, 2004 );
   private static final TaskKey WP_1 = new TaskKey( 4650, 5001 );
   private static final TaskKey WP_2 = new TaskKey( 4650, 5002 );
   private static final TaskKey WP_4 = new TaskKey( 4650, 5004 );
   private static final TaskKey WP_7 = new TaskKey( 4650, 5007 );
   private static final TaskKey WP_8 = new TaskKey( 4650, 5008 );
   private static final TaskKey WP_10 = new TaskKey( 4650, 5010 );
   private static final LpaFleetKey FLEET_C1 = new LpaFleetKey( 4650, "C1" );

   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * AIRCRAFT_1, FLEET_A. FLEET_A has SRVC_CHK_DEFN_1. SRVC_CHK_DEFN_1 has revision SRVC_CHK_1.
    * SRVC_CHK_1 is ACTV. ACTUAL_SRVC_CHK_1 is based on SRVC_CHK_1, assigned to WP_1, for
    * AIRCRAFT_1, not LRP, plan by 15-MAY-2012 01:00, status is ACTV, not historic. WP_1 is not
    * historic, has class CHECK, not prevented from LPA, not for heavy maintenance, scheduled for
    * LOCATION_1. LOCATION_1 under supply location AIRPORT_1.
    *
    * @throws Exception
    */
   @Test
   public void testAircraft01_Basic() throws Exception {
      execute( AIRCRAFT_1, MAY_15_2012.getTime(), 2 );

      assertTrue( iDataSet.next() );

      testRow( ACTUAL_SRVC_CHK_1, "BARCODE1", "TASK_NAME1", SRVC_CHK_DEFN_1, "SRVC_CHK_CD1",
            "SRVC_CHK_NAME1", "AIRCRAFT_NAME1", 0.0, WP_1, "WP_NAME1", SCHED_START_DATE1.getTime(),
            SCHED_END_DATE1.getTime(), LOCATION_1, "LOCATION_CD1", "LOCATION_NAME1", AIRPORT_1,
            "AIRPORT_CD1", "AIRPORT_NAME1" );

      // no more rows
      assertFalse( iDataSet.next() );
   }


   /**
    * AIRCRAFT_2, FLEET_B. FLEET_B has SRVC_CHK_DEFN_2. SRVC_CHK_DEFN_2 has revision SRVC_CHK_2.
    * SRVC_CHK_2 is ACTV. ACTUAL_SRVC_CHK_2 is based on SRVC_CHK_2, assigned to WP_2, for
    * AIRCRAFT_2, not LRP, plan by 15-MAY-2012 01:02, status is ACTV, not historic. WP_2 is not
    * historic, has class CHECK, is prevented from LPA, not for heavy maintenance, scheduled for
    * LOCATION_2. LOCATION_2 under supply location AIRPORT_2.
    *
    * @throws Exception
    */
   @Test
   public void testAircraft02_PreventLPA() throws Exception {
      execute( AIRCRAFT_2, MAY_15_2012.getTime(), 2 );

      // no row returned due to prevent_lpa_bool=1
      assertFalse( iDataSet.next() );

      // update prevent lpa to false and ensure a row is returned (otherwise it could be something
      // else that caused no rows to return)
      SchedStaskTable lWP2SchedStask = SchedStaskTable.findByPrimaryKey( WP_2 );
      lWP2SchedStask.setPreventLinePlanningAutomation( false );
      lWP2SchedStask.update();

      execute( AIRCRAFT_2, MAY_15_2012.getTime(), 2 );

      // row returned due to prevent_lpa_bool=0
      assertTrue( iDataSet.next() );
   }


   /**
    * AIRCRAFT_3, FLEET_C1. FLEET_C2 has SRVC_CHK_DEFN_3. SRVC_CHK_DEFN_3 has revision SRVC_CHK_3.
    * SRVC_CHK_3 is ACTV. ACTUAL_SRVC_CHK_3 is based on SRVC_CHK_3, assigned to WP_3, for
    * AIRCRAFT_3, not LRP, plan by 15-MAY-2012 01:00, status is ACTV, not historic. WP_3 is not
    * historic, has class CHECK, is not prevented from LPA, not for heavy maintenance, scheduled for
    * LOCATION_3. LOCATION_3 under supply location AIRPORT_3.
    *
    * @throws Exception
    */
   @Test
   public void testAircraft03_NotInFleet() throws Exception {
      execute( AIRCRAFT_3, MAY_15_2012.getTime(), 2 );

      // no row returned due to not being in lpa fleet
      assertFalse( iDataSet.next() );

      // add a fleet for the aircraft assembly and ensure a row is returned (otherwise it could be
      // something else that caused no rows to return)
      LpaFleetTable lLpaFleet = LpaFleetTable.create( FLEET_C1 );
      lLpaFleet.setTurnBlockDefinition( SRVC_CHK_DEFN_3 );
      lLpaFleet.insert();

      execute( AIRCRAFT_3, MAY_15_2012.getTime(), 2 );

      // row returned due to aircraft now in fleet
      assertTrue( iDataSet.next() );
   }


   /**
    * AIRCRAFT_4, FLEET_D. FLEET_D has SRVC_CHK_DEFN_4. SRVC_CHK_DEFN_4 has revision SRVC_CHK_4.
    * SRVC_CHK_4 is ACTV. ACTUAL_SRVC_CHK_4 is based on SRVC_CHK_4, assigned to WP_4, for
    * AIRCRAFT_4, not LRP, plan by 15-MAY-2012 01:00, status is ACTV, not historic. WP_4 is not
    * historic, has class CHECK, not prevented from LPA, not for heavy maintenance, not scheduled.
    * LOCATION_4 under supply location AIRPORT_4.
    *
    * @throws Exception
    */
   @Test
   public void testAircraft04_WPNotScheduled() throws Exception {
      execute( AIRCRAFT_4, MAY_15_2012.getTime(), 2 );

      assertTrue( iDataSet.next() );

      testRow( ACTUAL_SRVC_CHK_4, "BARCODE4", "TASK_NAME4", SRVC_CHK_DEFN_4, "SRVC_CHK_CD4",
            "SRVC_CHK_NAME4", "AIRCRAFT_NAME4", 0.0, WP_4, "WP_NAME4", null, null, null, null, null,
            null, null, null );

      // no more rows
      assertFalse( iDataSet.next() );
   }


   /**
    * AIRCRAFT_5, FLEET_E. FLEET_E has SRVC_CHK_DEFN_5. SRVC_CHK_DEFN_5 has revision SRVC_CHK_5.
    * SRVC_CHK_5 is ACTV. ACTUAL_SRVC_CHK_5 is based on SRVC_CHK_5, assigned to WP_5, for
    * AIRCRAFT_5, is LRP, plan by 15-MAY-2012 01:05, status is ACTV, not historic. WP_5 is not
    * historic, has class CHECK, is not prevented from LPA, not for heavy maintenance, scheduled for
    * LOCATION_5. LOCATION_5 under supply location AIRPORT_5.
    *
    * @throws Exception
    */
   @Test
   public void testAircraft05_LRPTask() throws Exception {
      execute( AIRCRAFT_5, MAY_15_2012.getTime(), 2 );

      // no row returned due being an LRP task
      assertFalse( iDataSet.next() );

      // update lrp to false and ensure a row is returned (otherwise it could be something
      // else that caused no rows to return)
      SchedStaskTable lSchedStask = SchedStaskTable.findByPrimaryKey( ACTUAL_SRVC_CHK_5 );
      lSchedStask.setLRPBool( false );
      lSchedStask.update();

      execute( AIRCRAFT_5, MAY_15_2012.getTime(), 2 );

      // row returned due to lrp_bool=0
      assertTrue( iDataSet.next() );
   }


   /**
    * AIRCRAFT_6, FLEET_F. FLEET_F has SRVC_CHK_DEFN_6. SRVC_CHK_DEFN_6 has revision SRVC_CHK_6.
    * SRVC_CHK_6 is ACTV. ACTUAL_SRVC_CHK_6 is based on SRVC_CHK_6, assigned to WP_6, for
    * AIRCRAFT_6, not LRP, plan by 15-MAY-2012 01:06, status is IN WORK, not historic. WP_6 is not
    * historic, has class CHECK, is not prevented from LPA, not for heavy maintenance, scheduled for
    * LOCATION_6. LOCATION_6 under supply location AIRPORT_6.
    *
    * @throws Exception
    */
   @Test
   public void testAircraft06_InWorkTask() throws Exception {
      execute( AIRCRAFT_6, MAY_15_2012.getTime(), 2 );

      // no row returned due being an IN WORK task
      assertFalse( iDataSet.next() );

      // update lrp to false and ensure a row is returned (otherwise it could be something
      // else that caused no rows to return)
      EvtEventTable lEvtEvent = EvtEventTable.findByPrimaryKey( ACTUAL_SRVC_CHK_6 );
      lEvtEvent.setEventStatus( RefEventStatusKey.ACTV );
      lEvtEvent.update();

      execute( AIRCRAFT_6, MAY_15_2012.getTime(), 2 );

      // row returned due status = ACTV
      assertTrue( iDataSet.next() );
   }


   /**
    * AIRCRAFT_7, FLEET_G. FLEET_G has SRVC_CHK_DEFN_7. SRVC_CHK_DEFN_7 has revision SRVC_CHK_7.
    * SRVC_CHK_7 is ACTV. ACTUAL_SRVC_CHK_7 is based on SRVC_CHK_7, assigned to WP_7, for
    * AIRCRAFT_7, not LRP, plan by 15-MAY-2012 01:07, status is ACTV, not historic. WP_7 is not
    * historic, has class CHECK, is not prevented from LPA, is for heavy maintenance, scheduled for
    * LOCATION_7. LOCATION_7 under supply location AIRPORT_7.
    *
    * @throws Exception
    */
   @Test
   public void testAircraft07_HeavyWorkPackage() throws Exception {
      execute( AIRCRAFT_7, MAY_15_2012.getTime(), 2 );

      // no row returned due to heavy_bool=1
      assertFalse( iDataSet.next() );

      // update heavy to false and ensure a row is returned (otherwise it could be something
      // else that caused no rows to return)
      SchedStaskTable lWP2SchedStask = SchedStaskTable.findByPrimaryKey( WP_7 );
      lWP2SchedStask.setHeavy( false );
      lWP2SchedStask.update();

      execute( AIRCRAFT_7, MAY_15_2012.getTime(), 2 );

      // row returned due to heavy_bool=0
      assertTrue( iDataSet.next() );
   }


   /**
    * AIRCRAFT_8, FLEET_H. FLEET_H has SRVC_CHK_DEFN_8. SRVC_CHK_DEFN_8 has revision SRVC_CHK_8.
    * SRVC_CHK_8 is ACTV. ACTUAL_SRVC_CHK_8 is based on SRVC_CHK_8, assigned to WP_8, for
    * AIRCRAFT_8, not LRP, plan by 15-MAY-2012 01:08, status is ACTV, not historic. WP_8 is not
    * historic, status is COMMIT, has class CHECK, is not prevented from LPA, not for heavy
    * maintenance, scheduled for LOCATION_8. LOCATION_8 under supply location AIRPORT_8.
    *
    * @throws Exception
    */
   @Test
   public void testAircraft08_CommittedWorkPackage() throws Exception {
      execute( AIRCRAFT_8, MAY_15_2012.getTime(), 2 );

      // no row returned due to status_cd=COMMIT
      assertFalse( iDataSet.next() );

      // update lrp to false and ensure a row is returned (otherwise it could be something
      // else that caused no rows to return)
      EvtEventTable lEvtEvent = EvtEventTable.findByPrimaryKey( WP_8 );
      lEvtEvent.setEventStatus( RefEventStatusKey.ACTV );
      lEvtEvent.update();

      execute( AIRCRAFT_8, MAY_15_2012.getTime(), 2 );

      // row returned due status = ACTV
      assertTrue( iDataSet.next() );
   }


   /**
    * AIRCRAFT_9, FLEET_I. FLEET_I has SRVC_CHK_DEFN_9. SRVC_CHK_DEFN_9 has revision SRVC_CHK_9.
    * SRVC_CHK_9 is ACTV. ACTUAL_SRVC_CHK_9 is based on SRVC_CHK_9, assigned to WP_9, for
    * AIRCRAFT_9, not LRP, plan by 14-MAY-2012 01:09, status is ACTV, not historic. WP_9 is not
    * historic, status is COMMIT, has class CHECK, is not prevented from LPA, not for heavy
    * maintenance, scheduled for LOCATION_9. LOCATION_9 under supply location AIRPORT_9.
    *
    * @throws Exception
    */
   @Test
   public void testAircraft09_OutsideSchedRange() throws Exception {
      execute( AIRCRAFT_9, MAY_15_2012.getTime(), 2 );

      // no row returned due to sched_start_dt/sched_end_dt being outside the range
      assertFalse( iDataSet.next() );

      execute( AIRCRAFT_9, MAY_14_2012.getTime(), 2 );

      // row returned due to sched_start_dt/sched_end_dt being in the range
      assertTrue( iDataSet.next() );
   }


   /**
    * AIRCRAFT_10, FLEET_J. FLEET_J has SRVC_CHK_DEFN_10. SRVC_CHK_DEFN_10 has revision SRVC_CHK_10.
    * SRVC_CHK_10 is ACTV. ACTUAL_SRVC_CHK_10 is based on SRVC_CHK_10, assigned to WP_10, for
    * AIRCRAFT_10, is not LRP, plan by 15-MAY-2012 01:05, status is ACTV, not historic. WP_10 is not
    * historic, has class CHECK, is not prevented from LPA, not for heavy maintenance, is LRP,
    * scheduled for LOCATION_10. LOCATION_10 under supply location AIRPORT_10.
    *
    * @throws Exception
    */
   @Test
   public void testAircraft10_LRPWorkPackage() throws Exception {
      execute( AIRCRAFT_10, MAY_15_2012.getTime(), 2 );

      // no row returned due being an LRP task
      assertFalse( iDataSet.next() );

      // update lrp to false and ensure a row is returned (otherwise it could be something
      // else that caused no rows to return)
      SchedStaskTable lWP2SchedStask = SchedStaskTable.findByPrimaryKey( WP_10 );
      lWP2SchedStask.setLRPBool( false );
      lWP2SchedStask.update();

      execute( AIRCRAFT_10, MAY_15_2012.getTime(), 2 );

      // row returned due to prevent_lpa_bool=0
      assertTrue( iDataSet.next() );
   }


   /**
    * Execute the query.
    *
    * @param aAircraft
    *           The aircraft
    * @param aStartDate
    *           The Start Date of the Scheduling Window
    * @param aScheduleRange
    *           The duration in days of the Scheduling Window
    */
   private void execute( AircraftKey aAircraft, Date aStartDate, int aScheduleRange ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aAircraft, "aAircraftDbId", "aAircraftId" );
      lArgs.add( "aStartTime", aStartDate );
      lArgs.add( "aScheduleRange", aScheduleRange );

      iDataSet = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Tests a row of the dataset
    *
    * @param aTask
    *           Task PK
    * @param aBarcode
    *           Task's Barcode
    * @param aTaskName
    *           Task's name
    * @param aBlockKey
    *           The key of the block's task definition
    * @param aBlockCode
    *           The task code for the block
    * @param aBlockName
    *           The name of the block
    * @param aAircraftName
    *           Aircraft's Name
    * @param aDuration
    *           Task's Estimated Duration
    * @param aWPTaskKey
    *           Key of the work package
    * @param aWPName
    *           The name of the work package
    * @param aWPScheduledStartDate
    *           The scheduled start date of the work package
    * @param aWPScheduledEndDate
    *           The scheduled end date of the work package
    * @param aWPLocation
    *           The work package location
    * @param aWPLocationCode
    *           The work package location code
    * @param aWPLocationName
    *           The work package location name
    * @param aAirport
    *           The airport
    * @param aAirportCode
    *           The airport code
    * @param aAirportName
    *           The airport name
    */
   private void testRow( TaskKey aTask, String aBarcode, String aTaskName, TaskDefnKey aBlockKey,
         String aBlockCode, String aBlockName, String aAircraftName, double aDuration,
         TaskKey aWPTaskKey, String aWPName, Date aWPScheduledStartDate, Date aWPScheduledEndDate,
         LocationKey aWPLocation, String aWPLocationCode, String aWPLocationName,
         LocationKey aAirport, String aAirportCode, String aAirportName ) {

      MxAssert.assertEquals( "task_pk", aTask.toString(), iDataSet.getString( "task_pk" ) );
      MxAssert.assertEquals( "barcode_sdesc", aBarcode, iDataSet.getString( "barcode_sdesc" ) );
      MxAssert.assertEquals( "event_sdesc", aTaskName, iDataSet.getString( "event_sdesc" ) );
      MxAssert.assertEquals( "block_defn", aBlockKey.toString(),
            iDataSet.getString( "block_defn" ) );
      MxAssert.assertEquals( "block_cd", aBlockCode, iDataSet.getString( "block_cd" ) );
      MxAssert.assertEquals( "block_name", aBlockName, iDataSet.getString( "block_name" ) );
      MxAssert.assertEquals( "duration", aDuration, iDataSet.getDouble( "duration" ) );
      MxAssert.assertEquals( "wp_event_key", aWPTaskKey.toString(),
            iDataSet.getString( "wp_event_key" ) );
      MxAssert.assertEquals( "wp_name", aWPName, iDataSet.getString( "wp_name" ) );
      MxAssert.assertEquals( "wp_sched_start", aWPScheduledStartDate,
            iDataSet.getDate( "wp_sched_start" ), 0 );
      MxAssert.assertEquals( "wp_sched_end", aWPScheduledEndDate,
            iDataSet.getDate( "wp_sched_end" ), 0 );
      MxAssert.assertEquals( ( aWPLocation == null ) ? aWPLocation : aWPLocation.toString(),
            iDataSet.getString( "wp_loc_key" ) );
      MxAssert.assertEquals( aWPLocationCode, iDataSet.getString( "loc_cd" ) );
      MxAssert.assertEquals( aWPLocationName, iDataSet.getString( "loc_name" ) );
      MxAssert.assertEquals( ( aAirport == null ) ? aAirport : aAirport.toString(),
            iDataSet.getString( "wp_airport_key" ) );
      MxAssert.assertEquals( aAirportCode, iDataSet.getString( "airport_cd" ) );
      MxAssert.assertEquals( aAirportName, iDataSet.getString( "airport_name" ) );
   }
}
