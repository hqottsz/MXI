package com.mxi.mx.core.ejb.flighthist.flighthistbean.edithistflight;

import static com.mxi.mx.core.key.DataTypeKey.CYCLES;
import static com.mxi.mx.core.key.DataTypeKey.HOURS;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Engine;
import com.mxi.am.domain.Location;
import com.mxi.am.domain.UsageAdjustment;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.ejb.flighthist.CurrentUsages;
import com.mxi.mx.core.ejb.flighthist.FlightHistBean;
import com.mxi.mx.core.ejb.flighthist.SnapshotUsage;
import com.mxi.mx.core.flight.dao.FlightLegDao;
import com.mxi.mx.core.flight.dao.FlightLegEntity;
import com.mxi.mx.core.flight.dao.JdbcFlightLegDao;
import com.mxi.mx.core.flight.model.FlightLegId;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefDataSourceKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.UsageDefinitionKey;
import com.mxi.mx.core.key.UsageParmKey;
import com.mxi.mx.core.services.event.usage.CollectedUsageParm;
import com.mxi.mx.core.services.flighthist.FlightInformationTO;
import com.mxi.mx.core.services.flighthist.Measurement;
import com.mxi.mx.core.table.eqp.EqpDataSourceSpec;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.org.OrgHr;
import com.mxi.mx.core.usage.model.UsageAdjustmentId;


/**
 * Tests for {@linkplain FlightHistBean#editHistFlight} that involve updating the snapshot usage TSN
 * of inventory involved in the flight.
 *
 */

public class EditHistFlight_AffectSnapshotTsnTest {

   private static final String HR_USERNAME = "HR_USERNAME";
   private static final String FLIGHT = "FLIGHT";
   private static final Measurement[] NO_MEASUREMENTS = new Measurement[] {};

   // current cycle and hour values
   private static final BigDecimal INITIAL_CYCLES = new BigDecimal( 50 );
   private static final BigDecimal INITIAL_HOURS = new BigDecimal( 100 );

   // some deltas
   private static final BigDecimal INITIAL_FLIGHT_DELTA = new BigDecimal( 1 );
   private static final BigDecimal DELTA_VALUE_TWO = new BigDecimal( 2 );
   private static final BigDecimal DELTA_VALUE_THREE = new BigDecimal( 3 );

   // the bean under test
   private FlightHistBean iFlightHistBean;

   private HumanResourceKey iHrKey;
   private FlightInformationTO iLatestFlightInfoTO;
   private FlightInformationTO iFirstFlightInfoTO;
   private InventoryKey iAircraftInvKey;
   private InventoryKey iEngineInvKey;
   private InventoryKey iAircraftWithOneEngineInvKey;

   private FlightLegDao iFlightLegDao;
   private UsageAdjustmentId iUsageRecordId;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * <pre>
    * Test: Test that the aircraft assembly's flight usage snapshot are updated for the edited flight
    * Given an Aircraft with historical flights
    * When I edit the Aircraft usage Deltas of a historical flight
    * Then the Aircraft TSN of that flight's snapshot should be updated by the change to the Delta
    * </pre>
    */
   @Test
   public void itUpdatesFlightAircraftTsnByDeltaChangeWhenFlightUsageEdited() throws Exception {

      // Given an aircraft collecting usage and historical flight against the aircraft.
      FlightLegId lFlightLegId[] = createFlightsForAircraftInv();

      // Get the flight of interest's usage record id
      FlightLegEntity lFlightLegEntity = iFlightLegDao.findById( lFlightLegId[0] );
      iUsageRecordId = lFlightLegEntity.getUsageRecord();

      // Get the aircraft's snapshot usage value for the flight of interest
      SnapshotUsage lPostFlightUsage = new SnapshotUsage( iAircraftInvKey, iUsageRecordId );

      // Generate the flight usage params and Edit the historical flight by changing the Delta
      CollectedUsageParm[] lEditUsageParms = {
            generateFlightUsage( iAircraftInvKey, CYCLES, DELTA_VALUE_THREE,
                  lPostFlightUsage.getTsn( CYCLES ).add( DELTA_VALUE_THREE )
                        .subtract( INITIAL_FLIGHT_DELTA ) ),
            generateFlightUsage( iAircraftInvKey, HOURS, DELTA_VALUE_THREE, lPostFlightUsage
                  .getTsn( HOURS ).add( DELTA_VALUE_THREE ).subtract( INITIAL_FLIGHT_DELTA ) ) };

      iFlightHistBean.editHistFlight( lFlightLegId[0], iHrKey, iFirstFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      // Get the aircraft's snapshot usage value after the edit of flight usage.
      SnapshotUsage lAfterEditSnapshotUsage = new SnapshotUsage( iAircraftInvKey, iUsageRecordId );

      BigDecimal lHoursDeltaDiff = DELTA_VALUE_THREE.subtract( INITIAL_FLIGHT_DELTA );
      BigDecimal lExpectedHours = lPostFlightUsage.getTsn( HOURS ).add( lHoursDeltaDiff );

      Assert.assertEquals( "Unexpected aircraft TSN value for HOURS.", lExpectedHours,
            lAfterEditSnapshotUsage.getTsn( HOURS ) );

   }


   /**
    * <pre>
    * Test:Test that the aircraft assembly's flight usage snapshots are updated since the edited flight
    * Given an Aircraft with historical flights
    * When I edit the Aircraft usage Deltas of a historical flight
    * Then the Aircraft TSN of all the following flights' snapshot should be updated by the change to the Delta
    * </pre>
    */
   @Test
   public void itUpdatesFollowingFlightAircraftTsnByDeltaChangeWhenFlightUsageEdited()
         throws Exception {

      // Given an aircraft collecting usage and multiple historical flights against the aircraft.
      FlightLegId lFlightLegId[] = createFlightsForAircraftInv();

      // Get the flight of interest's usage record id
      FlightLegEntity lFollowingFlightLegEntity = iFlightLegDao.findById( lFlightLegId[1] );
      iUsageRecordId = lFollowingFlightLegEntity.getUsageRecord();

      // Get the aircraft's snapshot usage value for the flight of interest
      SnapshotUsage lPostFlightUsage = new SnapshotUsage( iAircraftInvKey, iUsageRecordId );

      // Generate the flight usage params and Edit the historical flight by changing the Delta
      CollectedUsageParm[] lEditUsageParms = {
            generateFlightUsage( iAircraftInvKey, CYCLES, DELTA_VALUE_THREE,
                  lPostFlightUsage.getTsn( CYCLES ).add( DELTA_VALUE_THREE ) ),
            generateFlightUsage( iAircraftInvKey, HOURS, DELTA_VALUE_THREE,
                  lPostFlightUsage.getTsn( HOURS ).add( DELTA_VALUE_THREE ) ) };

      iFlightHistBean.editHistFlight( lFlightLegId[0], iHrKey, iFirstFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      // Get the aircraft's snapshot usage value after editing flight usage
      SnapshotUsage lAfterEditSnapshotUsage = new SnapshotUsage( iAircraftInvKey, iUsageRecordId );

      BigDecimal lHoursDeltaDiff = DELTA_VALUE_THREE.subtract( INITIAL_FLIGHT_DELTA );
      BigDecimal lExpectedHours = lPostFlightUsage.getTsn( HOURS ).add( lHoursDeltaDiff );

      Assert.assertEquals( "Unexpected aircraft TSN value for HOURS.", lExpectedHours,
            lAfterEditSnapshotUsage.getTsn( HOURS ) );

   }


   /**
    * <pre>
    * Test: Test that the aircraft assembly's flight usage snapshots prior to the edited flight are not updated
    * Given an Aircraft with historical flights
    * When I edit the Aircraft usage Deltas of a historical flight
    * Then the Aircraft TSN of the flights' snapshot prior to the edited flight should not be updated by the change to Delta
    * </pre>
    */
   @Test
   public void itDoesNotUpdatePreviousFlightAircraftTsnByDeltaChangeWhenFlightUsageEdited()
         throws Exception {

      // Given an aircraft collecting usage and multiple historical flights against the aircraft.
      FlightLegId lFlightLegId[] = createFlightsForAircraftInv();

      // Get the flight of interest's usage record id
      FlightLegEntity lFlightLegEntity = iFlightLegDao.findById( lFlightLegId[0] );
      iUsageRecordId = lFlightLegEntity.getUsageRecord();

      // Get the aircraft's snapshot usage value for the flight of interest
      SnapshotUsage lPostFlightUsage = new SnapshotUsage( iAircraftInvKey, iUsageRecordId );

      // Generate the flight usage params and Edit the historical flight by changing the Delta
      CollectedUsageParm[] lEditUsageParms = {
            generateFlightUsage( iAircraftInvKey, CYCLES, DELTA_VALUE_THREE,
                  lPostFlightUsage.getTsn( CYCLES ).add( DELTA_VALUE_THREE ) ),
            generateFlightUsage( iAircraftInvKey, HOURS, DELTA_VALUE_THREE,
                  lPostFlightUsage.getTsn( HOURS ).add( DELTA_VALUE_THREE ) ) };

      iFlightHistBean.editHistFlight( lFlightLegId[1], iHrKey, iLatestFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      // Get the aircraft's snapshot usage value after editing flight usage
      SnapshotUsage lAfterEditSnapshotUsage = new SnapshotUsage( iAircraftInvKey, iUsageRecordId );

      Assert.assertEquals( "Unexpected aircraft TSN value for HOURS.",
            lPostFlightUsage.getTsn( HOURS ), lAfterEditSnapshotUsage.getTsn( HOURS ) );

   }


   /**
    * <pre>
    * Test: Test that the engine assembly's flight usage snapshots are updated for the edited flight
    * Given an Aircraft with historical flights
    * When I edit the Engine usage Deltas of a historical flight
    * Then the Engine TSN of that flight's snapshot should be updated by the change to Delta
    * </pre>
    */
   @Test
   public void itUpdatesFlightEngineTsnByDeltaChangeWhenFlightUsageEdited() throws Exception {

      // Given an aircraft collecting usage and multiple historical flights against the aircraft.
      FlightLegId lFlightLegId[] = createFlightsForAircraftWithOneEngineInv();

      // Get the flight of interest's usage record id
      FlightLegEntity lFlightLegEntity = iFlightLegDao.findById( lFlightLegId[0] );
      iUsageRecordId = lFlightLegEntity.getUsageRecord();

      // Get the aircraft's snapshot usage value for the flight of interest
      SnapshotUsage lPostFlightUsage = new SnapshotUsage( iEngineInvKey, iUsageRecordId );

      // Generate the flight usage params and Edit the historical flight by changing the Delta
      CollectedUsageParm[] lEditUsageParms = {
            generateFlightUsage( iAircraftWithOneEngineInvKey, CYCLES, DELTA_VALUE_THREE,
                  lPostFlightUsage.getTsn( CYCLES ).add( DELTA_VALUE_THREE )
                        .subtract( INITIAL_FLIGHT_DELTA ) ),
            generateFlightUsage( iAircraftWithOneEngineInvKey, HOURS, DELTA_VALUE_THREE,
                  lPostFlightUsage.getTsn( HOURS ).add( DELTA_VALUE_THREE )
                        .subtract( INITIAL_FLIGHT_DELTA ) ),
            generateFlightUsage( iEngineInvKey, CYCLES, DELTA_VALUE_THREE,
                  lPostFlightUsage.getTsn( CYCLES ).add( DELTA_VALUE_THREE )
                        .subtract( INITIAL_FLIGHT_DELTA ) ),
            generateFlightUsage( iEngineInvKey, HOURS, DELTA_VALUE_THREE, lPostFlightUsage
                  .getTsn( HOURS ).add( DELTA_VALUE_THREE ).subtract( INITIAL_FLIGHT_DELTA ) ) };

      iFlightHistBean.editHistFlight( lFlightLegId[0], iHrKey, iFirstFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      // Get the aircraft's snapshot usage value after editing flight usage
      SnapshotUsage lAfterEditSnapshotUsage = new SnapshotUsage( iEngineInvKey, iUsageRecordId );

      BigDecimal lHoursDeltaDiff = DELTA_VALUE_THREE.subtract( INITIAL_FLIGHT_DELTA );
      BigDecimal lExpectedHours = lPostFlightUsage.getTsn( HOURS ).add( lHoursDeltaDiff );

      Assert.assertEquals( "Unexpected aircraft TSN value for HOURS.", lExpectedHours,
            lAfterEditSnapshotUsage.getTsn( HOURS ) );

   }


   /**
    * <pre>
    * Test: Test that the engine assembly's flight usage snapshots are updated since the edited flight
    * Given an Aircraft with historical flights
    * When I edit the Engine usage Deltas of a historical flight
    * Then the Engine TSN of all the following flights' snapshot should be updated by the change to Delta
    * </pre>
    */
   @Test
   public void itUpdatesFollowingFlightEngineTsnByDeltaChangeWhenFlightUsageEdited()
         throws Exception {

      // Given an aircraft collecting usage and multiple historical flights against the aircraft.
      FlightLegId lFlightLegId[] = createFlightsForAircraftWithOneEngineInv();

      FlightLegEntity lFollowingFlightLegEntity = iFlightLegDao.findById( lFlightLegId[1] );
      iUsageRecordId = lFollowingFlightLegEntity.getUsageRecord();

      // Get the aircraft's snapshot usage value for the flight of interest
      SnapshotUsage lPostFlightUsage = new SnapshotUsage( iEngineInvKey, iUsageRecordId );

      // Generate the flight usage params and Edit the historical flight by changing the Delta
      CollectedUsageParm[] lEditUsageParms = {
            generateFlightUsage( iAircraftWithOneEngineInvKey, CYCLES, DELTA_VALUE_THREE,
                  lPostFlightUsage.getTsn( CYCLES ).add( DELTA_VALUE_THREE ) ),
            generateFlightUsage( iAircraftWithOneEngineInvKey, HOURS, DELTA_VALUE_THREE,
                  lPostFlightUsage.getTsn( HOURS ).add( DELTA_VALUE_THREE ) ),
            generateFlightUsage( iEngineInvKey, CYCLES, DELTA_VALUE_THREE,
                  lPostFlightUsage.getTsn( CYCLES ).add( DELTA_VALUE_THREE ) ),
            generateFlightUsage( iEngineInvKey, HOURS, DELTA_VALUE_THREE,
                  lPostFlightUsage.getTsn( HOURS ).add( DELTA_VALUE_THREE ) ) };

      iFlightHistBean.editHistFlight( lFlightLegId[0], iHrKey, iFirstFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      // Get the aircraft's snapshot usage value after editing flight usage
      SnapshotUsage lAfterEditSnapshotUsage = new SnapshotUsage( iEngineInvKey, iUsageRecordId );

      BigDecimal lHoursDeltaDiff = DELTA_VALUE_THREE.subtract( INITIAL_FLIGHT_DELTA );
      BigDecimal lExpectedHours = lPostFlightUsage.getTsn( HOURS ).add( lHoursDeltaDiff );

      Assert.assertEquals( "Unexpected aircraft TSN value for HOURS.", lExpectedHours,
            lAfterEditSnapshotUsage.getTsn( HOURS ) );

   }


   /**
    * <pre>
    * Test: Test that the engine assembly's flight usage snapshots prior to the edited flight are not updated
    * Given an Aircraft with historical flights
    * When I edit the Engine usage Deltas of a historical flight
    * Then the Engine TSN of the flights' snapshot prior to the edited flight should not be updated by the change to Delta
    * </pre>
    */
   @Test
   public void itDoesNotUpdatePriorFlightEngineTsnByDeltaChangeWhenFlightUsageEdited()
         throws Exception {

      // Given an aircraft collecting usage and multiple historical flights against the aircraft.
      FlightLegId lFlightLegId[] = createFlightsForAircraftWithOneEngineInv();

      FlightLegEntity lFlightLegEntity = iFlightLegDao.findById( lFlightLegId[0] );
      iUsageRecordId = lFlightLegEntity.getUsageRecord();

      // Get the aircraft's snapshot usage value for the flight of interest
      SnapshotUsage lPostFlightUsage = new SnapshotUsage( iEngineInvKey, iUsageRecordId );

      // Generate the flight usage params and Edit the historical flight by changing the Delta
      CollectedUsageParm[] lEditUsageParms = {
            generateFlightUsage( iAircraftWithOneEngineInvKey, CYCLES, DELTA_VALUE_THREE,
                  lPostFlightUsage.getTsn( CYCLES ).add( DELTA_VALUE_THREE ) ),
            generateFlightUsage( iAircraftWithOneEngineInvKey, HOURS, DELTA_VALUE_THREE,
                  lPostFlightUsage.getTsn( HOURS ).add( DELTA_VALUE_THREE ) ),
            generateFlightUsage( iEngineInvKey, CYCLES, DELTA_VALUE_THREE,
                  lPostFlightUsage.getTsn( CYCLES ).add( DELTA_VALUE_THREE ) ),
            generateFlightUsage( iEngineInvKey, HOURS, DELTA_VALUE_THREE,
                  lPostFlightUsage.getTsn( HOURS ).add( DELTA_VALUE_THREE ) ) };

      iFlightHistBean.editHistFlight( lFlightLegId[1], iHrKey, iLatestFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );
      // Get the aircraft's snapshot usage value after editing flight usage
      SnapshotUsage lAfterEditSnapshotUsage = new SnapshotUsage( iEngineInvKey, iUsageRecordId );

      Assert.assertEquals( "Unexpected aircraft TSN value for HOURS.",
            lPostFlightUsage.getTsn( HOURS ), lAfterEditSnapshotUsage.getTsn( HOURS ) );

   }


   /**
    * <pre>
    * Test: Test that the loose engine assembly's flight usage snapshots are updated for the edited flight
    * Given an Aircraft with historical flights
    * And the Engine is removed from Aircraft after a historical flight
    * When I edit the current loose engine deltas of the historical flight
    * Then the loose Engine TSN of that flight's snapshot should be updated by the change to Delta
    * </pre>
    */
   @Test
   public void itUpdatesFlightEngineTsnByDeltaChangeForLooseEngineWhenFlightUsageEdited()
         throws Exception {

      // Given an aircraft collecting usage and multiple historical flights against the aircraft.
      FlightLegId lFlightLegId[] = createFlightsForAircraftWithOneEngineInv();

      FlightLegEntity lFlightLegEntity = iFlightLegDao.findById( lFlightLegId[0] );
      iUsageRecordId = lFlightLegEntity.getUsageRecord();

      // Get the snapshot usage value of the engine part of the flight
      SnapshotUsage lPostFlightUsage = new SnapshotUsage( iEngineInvKey, iUsageRecordId );

      // detach engine from the aircraft
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iEngineInvKey );
      lInvInv.setNhInvNo( iEngineInvKey );
      lInvInv.setHInvNo( iEngineInvKey );
      lInvInv.setAssmblInvNo( iEngineInvKey );
      lInvInv.update();

      // Generate the flight usage params and Edit the historical flight by changing the Delta
      CollectedUsageParm[] lEditUsageParms = {
            generateFlightUsage( iAircraftWithOneEngineInvKey, CYCLES, DELTA_VALUE_THREE,
                  lPostFlightUsage.getTsn( CYCLES ).add( DELTA_VALUE_THREE )
                        .subtract( INITIAL_FLIGHT_DELTA ) ),
            generateFlightUsage( iAircraftWithOneEngineInvKey, HOURS, DELTA_VALUE_THREE,
                  lPostFlightUsage.getTsn( HOURS ).add( DELTA_VALUE_THREE )
                        .subtract( INITIAL_FLIGHT_DELTA ) ),
            generateFlightUsage( iEngineInvKey, CYCLES, DELTA_VALUE_THREE,
                  lPostFlightUsage.getTsn( CYCLES ).add( DELTA_VALUE_THREE )
                        .subtract( INITIAL_FLIGHT_DELTA ) ),
            generateFlightUsage( iEngineInvKey, HOURS, DELTA_VALUE_THREE, lPostFlightUsage
                  .getTsn( HOURS ).add( DELTA_VALUE_THREE ).subtract( INITIAL_FLIGHT_DELTA ) ) };

      iFlightHistBean.editHistFlight( lFlightLegId[0], iHrKey, iFirstFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      // Get the engine's snapshot usage value after editing flight usage
      SnapshotUsage lAfterEditSnapshotUsage = new SnapshotUsage( iEngineInvKey, iUsageRecordId );

      BigDecimal lHoursDeltaDiff = DELTA_VALUE_THREE.subtract( INITIAL_FLIGHT_DELTA );
      BigDecimal lExpectedHours = lPostFlightUsage.getTsn( HOURS ).add( lHoursDeltaDiff );

      Assert.assertEquals( "Unexpected aircraft TSN value for HOURS.", lExpectedHours,
            lAfterEditSnapshotUsage.getTsn( HOURS ) );
   }


   /**
    * <pre>
    * Test: Test that the loose engine assembly's flight usage snapshots are updated since the edited flight
    * Given an Aircraft with historical flights
    * And the Engine is removed from Aircraft after a historical flight
    * When I edit the current loose engine deltas of the historical flight
    * Then the loose Engine TSN of the following flights' snapshot should be updated by the change to Delta
    * </pre>
    */
   @Test
   public void itUpdatesFollowingFlightEngineTsnByDeltaChangeForLooseEngineWhenFlightUsageEdited()
         throws Exception {

      // Given an aircraft collecting usage and multiple historical flights against the aircraft.
      FlightLegId lFlightLegId[] = createFlightsForAircraftWithOneEngineInv();

      FlightLegEntity lFollowingFlightLegEntity = iFlightLegDao.findById( lFlightLegId[1] );
      iUsageRecordId = lFollowingFlightLegEntity.getUsageRecord();

      // Get the snapshot usage value of the engine part of the flight
      SnapshotUsage lPostFlightUsage = new SnapshotUsage( iEngineInvKey, iUsageRecordId );

      // detach engine from the aircraft
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iEngineInvKey );
      lInvInv.setNhInvNo( iEngineInvKey );
      lInvInv.setHInvNo( iEngineInvKey );
      lInvInv.setAssmblInvNo( iEngineInvKey );
      lInvInv.update();

      // Generate the flight usage params and Edit the historical flight by changing the Delta
      CollectedUsageParm[] lEditUsageParms = {
            generateFlightUsage( iAircraftWithOneEngineInvKey, CYCLES, DELTA_VALUE_THREE,
                  lPostFlightUsage.getTsn( CYCLES ).add( DELTA_VALUE_THREE ) ),
            generateFlightUsage( iAircraftWithOneEngineInvKey, HOURS, DELTA_VALUE_THREE,
                  lPostFlightUsage.getTsn( HOURS ).add( DELTA_VALUE_THREE ) ),
            generateFlightUsage( iEngineInvKey, CYCLES, DELTA_VALUE_THREE,
                  lPostFlightUsage.getTsn( CYCLES ).add( DELTA_VALUE_THREE ) ),
            generateFlightUsage( iEngineInvKey, HOURS, DELTA_VALUE_THREE,
                  lPostFlightUsage.getTsn( HOURS ).add( DELTA_VALUE_THREE ) ) };

      iFlightHistBean.editHistFlight( lFlightLegId[0], iHrKey, iFirstFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      // Get the engine's snapshot usage value after editing flight usage
      SnapshotUsage lAfterEditSnapshotUsage = new SnapshotUsage( iEngineInvKey, iUsageRecordId );

      BigDecimal lHoursDeltaDiff = DELTA_VALUE_THREE.subtract( INITIAL_FLIGHT_DELTA );
      BigDecimal lExpectedHours = lPostFlightUsage.getTsn( HOURS ).add( lHoursDeltaDiff );

      Assert.assertEquals( "Unexpected aircraft TSN value for HOURS.", lExpectedHours,
            lAfterEditSnapshotUsage.getTsn( HOURS ) );
   }


   /**
    * <pre>
    * Test: Test that when an engine is reinstalled onto an Aircraft, the engine assembly's flight usage snapshots are updated for the edited flight
    * Given an Aircraft with historical flights
    * And the Engine is removed from the Aircraft after a historical flight and reinstalled onto another Aircraft
    * And the other Aircraft has historical flights with the installed engine
    * When I edit the Engine usage Deltas of the historical flight of the former Aircraft
    * Then the Engine usage TSN snapshots of that flight for the former aircraft should be updated by the change to Delta
    * </pre>
    */
   @Test
   public void
         itUpdatesFlightEngineTsnByDeltaChangeForFlightBeforeEngineReinstalledOnAnotherAircraftWhenFlightUsageEdited()
               throws Exception {

      // Given an aircraft collecting usage and multiple historical flights against the aircraft.
      FlightLegId lFlightLegId[] = createFlightsForAircraftWithOneEngineInv();

      FlightLegEntity lFollowingFlightLegEntity = iFlightLegDao.findById( lFlightLegId[0] );
      iUsageRecordId = lFollowingFlightLegEntity.getUsageRecord();

      // Get the snapshot usage value of the engine part of the flight
      SnapshotUsage lPostFlightUsage = new SnapshotUsage( iEngineInvKey, iUsageRecordId );

      // detach engine from the aircraft
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iEngineInvKey );
      lInvInv.setNhInvNo( iEngineInvKey );
      lInvInv.setHInvNo( iEngineInvKey );
      lInvInv.setAssmblInvNo( iEngineInvKey );
      lInvInv.update();

      // attach the removed engine onto another aircraft
      lInvInv.setNhInvNo( iAircraftInvKey );
      lInvInv.setHInvNo( iAircraftInvKey );
      lInvInv.setAssmblInvNo( iEngineInvKey );
      lInvInv.update();

      // Generate the flight usage params and Edit the historical flight by changing the Delta
      CollectedUsageParm[] lEditUsageParms = {
            generateFlightUsage( iAircraftWithOneEngineInvKey, CYCLES, DELTA_VALUE_THREE,
                  lPostFlightUsage.getTsn( CYCLES ).add( DELTA_VALUE_THREE )
                        .subtract( INITIAL_FLIGHT_DELTA ) ),
            generateFlightUsage( iAircraftWithOneEngineInvKey, HOURS, DELTA_VALUE_THREE,
                  lPostFlightUsage.getTsn( HOURS ).add( DELTA_VALUE_THREE )
                        .subtract( INITIAL_FLIGHT_DELTA ) ),
            generateFlightUsage( iEngineInvKey, CYCLES, DELTA_VALUE_THREE,
                  lPostFlightUsage.getTsn( CYCLES ).add( DELTA_VALUE_THREE )
                        .subtract( INITIAL_FLIGHT_DELTA ) ),
            generateFlightUsage( iEngineInvKey, HOURS, DELTA_VALUE_THREE, lPostFlightUsage
                  .getTsn( HOURS ).add( DELTA_VALUE_THREE ).subtract( INITIAL_FLIGHT_DELTA ) ) };

      iFlightHistBean.editHistFlight( lFlightLegId[0], iHrKey, iFirstFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      // Get the engine's snapshot usage value after editing flight usage
      SnapshotUsage lAfterEditSnapshotUsage = new SnapshotUsage( iEngineInvKey, iUsageRecordId );

      BigDecimal lHoursDeltaDiff = DELTA_VALUE_THREE.subtract( INITIAL_FLIGHT_DELTA );
      BigDecimal lExpectedHours = lPostFlightUsage.getTsn( HOURS ).add( lHoursDeltaDiff );

      Assert.assertEquals( "Unexpected aircraft TSN value for HOURS.", lExpectedHours,
            lAfterEditSnapshotUsage.getTsn( HOURS ) );
   }


   /**
    * <pre>
    * Test: Test that when an engine is reinstalled onto an Aircraft, the engine assembly's flight usage snapshots are updated since the edited flight
    * Given an Aircraft with historical flights
    * And the Engine is removed from the Aircraft after a historical flight and reinstalled onto another Aircraft
    * And the other Aircraft with the installed engine has historical flights
    * When I edit the Engine usage Deltas of the historical flight of the former Aircraft
    * Then the Engine usage TSN snapshots of the flight with the installed engine of the latter aircraft should be updated by the change to Delta
    * </pre>
    */
   @Test
   public void
         itUpdatesFlightEngineTsnByDeltaChangeForFlightAfterEngineReinstalledOnAnotherAircraftWhenFlightUsageEdited()
               throws Exception {

      // Given an aircraft collecting usage and multiple historical flights against the aircraft.
      FlightLegId lFlightLegIdBeforeEngineRemoval[] = createFlightsForAircraftWithOneEngineInv();

      // detach engine from the aircraft
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iEngineInvKey );
      lInvInv.setNhInvNo( iEngineInvKey );
      lInvInv.setHInvNo( iEngineInvKey );
      lInvInv.setAssmblInvNo( iEngineInvKey );
      lInvInv.update();

      // attach the removed engine onto another aircraft
      lInvInv.setNhInvNo( iAircraftInvKey );
      lInvInv.setHInvNo( iAircraftInvKey );
      lInvInv.setAssmblInvNo( iEngineInvKey );
      lInvInv.update();

      // Setup a Flight Transfer Object for the aircraft with the reinstalled engine
      StringBuilder lFlightName = new StringBuilder().append( FLIGHT )
            .append( new SimpleDateFormat( "SSSS" ).format( new Date() ) );

      Date lActualDepartureDate = DateUtils.addDays( new Date(), 10 );
      Date lActualArrivalDate = DateUtils.addHours( lActualDepartureDate, 2 );

      FlightInformationTO lFlightInfoTO =
            generateFlightInfoTO( lFlightName, lActualDepartureDate, lActualArrivalDate );

      CollectedUsageParm[] lFlightUsageParms = {
            generateFlightUsage( iAircraftInvKey, CYCLES, INITIAL_FLIGHT_DELTA,
                  INITIAL_FLIGHT_DELTA.add( INITIAL_CYCLES ) ),
            generateFlightUsage( iAircraftInvKey, HOURS, INITIAL_FLIGHT_DELTA,
                  INITIAL_FLIGHT_DELTA.add( INITIAL_HOURS ) ),
            generateFlightUsage( iEngineInvKey, CYCLES, INITIAL_FLIGHT_DELTA,
                  INITIAL_FLIGHT_DELTA.add( INITIAL_CYCLES ) ),
            generateFlightUsage( iEngineInvKey, HOURS, INITIAL_FLIGHT_DELTA,
                  INITIAL_FLIGHT_DELTA.add( INITIAL_HOURS ) ) };

      // Create a flight for aircraft with the reinstalled engine
      FlightLegId lFlightLegIdAfterEngineReinstall =
            createFlight( iAircraftInvKey, lFlightInfoTO, lFlightUsageParms );

      FlightLegEntity lFollowingFlightLegEntity =
            iFlightLegDao.findById( lFlightLegIdAfterEngineReinstall );
      iUsageRecordId = lFollowingFlightLegEntity.getUsageRecord();

      // Get the snapshot usage value of the engine part of the flight
      SnapshotUsage lPostFlightUsage = new SnapshotUsage( iEngineInvKey, iUsageRecordId );

      // Generate the flight usage params and Edit the historical flight by changing the Delta
      CollectedUsageParm[] lEditUsageParms = {
            generateFlightUsage( iAircraftWithOneEngineInvKey, CYCLES, DELTA_VALUE_THREE,
                  lPostFlightUsage.getTsn( CYCLES ).add( DELTA_VALUE_THREE ) ),
            generateFlightUsage( iAircraftWithOneEngineInvKey, HOURS, DELTA_VALUE_THREE,
                  lPostFlightUsage.getTsn( HOURS ).add( DELTA_VALUE_THREE ) ),
            generateFlightUsage( iEngineInvKey, CYCLES, DELTA_VALUE_THREE,
                  lPostFlightUsage.getTsn( CYCLES ).add( DELTA_VALUE_THREE ) ),
            generateFlightUsage( iEngineInvKey, HOURS, DELTA_VALUE_THREE,
                  lPostFlightUsage.getTsn( HOURS ).add( DELTA_VALUE_THREE ) ) };

      iFlightHistBean.editHistFlight( lFlightLegIdBeforeEngineRemoval[0], iHrKey,
            iFirstFlightInfoTO, lEditUsageParms, NO_MEASUREMENTS );

      // Get the engine's snapshot usage value after editing flight usage
      SnapshotUsage lAfterEditSnapshotUsage = new SnapshotUsage( iEngineInvKey, iUsageRecordId );

      BigDecimal lHoursDeltaDiff = DELTA_VALUE_THREE.subtract( INITIAL_FLIGHT_DELTA );
      BigDecimal lExpectedHours = lPostFlightUsage.getTsn( HOURS ).add( lHoursDeltaDiff );

      Assert.assertEquals( "Unexpected aircraft TSN value for HOURS.", lExpectedHours,
            lAfterEditSnapshotUsage.getTsn( HOURS ) );
   }


   /**
    * <pre>
    * Test: Test that when a usage record exists against an engine which had prior usage accrued as part of flights and a historical flight usage is edited then the engine's usage record is updated
    * Given an Aircraft with historical flights
    * And the Engine is removed from the Aircraft after a historical flight
    * And a usage record is created against the loose Engine
    * When I edit the Engine usage Deltas of the historical flight
    * Then the loose Engine's usage record TSN snapshots should be updated by the change to Delta
    * </pre>
    */
   @Test
   public void itUpdatesEngineUsageRecordTsnByDeltaChangeForEngineRemovedWhenFlightUsageEdited()
         throws Exception {

      // Given an aircraft collecting usage and multiple historical flights against the aircraft.
      FlightLegId lFlightLegIdBeforeEngineRemoval[] = createFlightsForAircraftWithOneEngineInv();

      // detach engine from the aircraft
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iEngineInvKey );
      lInvInv.setNhInvNo( iEngineInvKey );
      lInvInv.setHInvNo( iEngineInvKey );
      lInvInv.setAssmblInvNo( iEngineInvKey );
      lInvInv.update();

      FlightLegEntity lFollowingFlightLegEntity =
            iFlightLegDao.findById( lFlightLegIdBeforeEngineRemoval[0] );
      iUsageRecordId = lFollowingFlightLegEntity.getUsageRecord();

      // Get the snapshot usage value of the engine part of the flight
      SnapshotUsage lPostFlightUsage = new SnapshotUsage( iEngineInvKey, iUsageRecordId );

      final BigDecimal lPostFlightUsageHours = lPostFlightUsage.getTsn( HOURS );

      // Create a usage record for the engine after it's removal from the aircraft
      UsageAdjustmentId lUsageRecordEngineId =
            Domain.createUsageAdjustment( new DomainConfiguration<UsageAdjustment>() {

               @Override
               public void configure( UsageAdjustment aUsageAdjustment ) {
                  aUsageAdjustment.setMainInventory( iEngineInvKey );
                  aUsageAdjustment.setUsageDate( DateUtils.addHours( new Date(), -2 ) );
                  aUsageAdjustment.addUsage( iEngineInvKey, HOURS, lPostFlightUsageHours,
                        DELTA_VALUE_TWO );
               }
            } );

      // Get the usage snapshot of the engine after the creation of the usage record as a result of
      // Test run against it
      SnapshotUsage lEngineSnapshotAfterTestRun =
            new SnapshotUsage( iEngineInvKey, lUsageRecordEngineId );

      // Generate the flight usage params and Edit the historical flight by changing the Delta
      CollectedUsageParm[] lEditUsageParms = {
            generateFlightUsage( iAircraftWithOneEngineInvKey, CYCLES, DELTA_VALUE_THREE,
                  INITIAL_CYCLES.add( DELTA_VALUE_THREE ) ),
            generateFlightUsage( iAircraftWithOneEngineInvKey, HOURS, DELTA_VALUE_THREE,
                  INITIAL_HOURS.add( DELTA_VALUE_THREE ) ),
            generateFlightUsage( iEngineInvKey, CYCLES, DELTA_VALUE_THREE,
                  INITIAL_CYCLES.add( DELTA_VALUE_THREE ) ),
            generateFlightUsage( iEngineInvKey, HOURS, DELTA_VALUE_THREE,
                  INITIAL_HOURS.add( DELTA_VALUE_THREE ) ) };

      iFlightHistBean.editHistFlight( lFlightLegIdBeforeEngineRemoval[0], iHrKey,
            iFirstFlightInfoTO, lEditUsageParms, NO_MEASUREMENTS );

      // Get the engine's snapshot usage value after editing flight usage
      SnapshotUsage lAfterEditSnapshotUsage =
            new SnapshotUsage( iEngineInvKey, lUsageRecordEngineId );

      BigDecimal lHoursDeltaDiff = DELTA_VALUE_THREE.subtract( INITIAL_FLIGHT_DELTA );
      BigDecimal lExpectedHours =
            lEngineSnapshotAfterTestRun.getTsn( HOURS ).add( lHoursDeltaDiff );

      Assert.assertEquals( "Unexpected aircraft TSN value for HOURS.", lExpectedHours,
            lAfterEditSnapshotUsage.getTsn( HOURS ) );
   }


   @Before
   public void setup() {
      iHrKey = new HumanResourceDomainBuilder().withUsername( HR_USERNAME ).build();

      int lUserId = OrgHr.findByPrimaryKey( iHrKey ).getUserId();
      UserParameters.setInstance( lUserId, "LOGIC", new UserParametersFake( lUserId, "LOGIC" ) );

      iFlightLegDao = new JdbcFlightLegDao();

      // create an Engine assembly with current usage
      iEngineInvKey = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.addUsage( HOURS, INITIAL_HOURS );
            aEngine.addUsage( CYCLES, INITIAL_CYCLES );

         }
      } );

      // create aircraft with current usage
      iAircraftInvKey = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( CYCLES, INITIAL_CYCLES );
            aAircraft.addUsage( HOURS, INITIAL_HOURS );
         }
      } );

      // create aircraft with one engine & current usage
      iAircraftWithOneEngineInvKey = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( CYCLES, INITIAL_CYCLES );
            aAircraft.addUsage( HOURS, INITIAL_HOURS );
            aAircraft.addEngine( iEngineInvKey );
         }
      } );

      // create 2 flights - first and latest
      StringBuilder lFirstFlightName = new StringBuilder().append( FLIGHT )
            .append( new SimpleDateFormat( "SSSS" ).format( new Date() ) );
      Date lActualDepartureDate = DateUtils.addDays( new Date(), -2 );
      Date lActualArrivalDate = DateUtils.addHours( lActualDepartureDate, 2 );

      iFirstFlightInfoTO =
            generateFlightInfoTO( lFirstFlightName, lActualDepartureDate, lActualArrivalDate );

      StringBuilder lLatestFlightName = new StringBuilder().append( FLIGHT )
            .append( new SimpleDateFormat( "SSSS" ).format( new Date() ) );
      lActualDepartureDate = DateUtils.addHours( new Date(), -5 );
      lActualArrivalDate = DateUtils.addHours( lActualDepartureDate, 2 );

      iLatestFlightInfoTO =
            generateFlightInfoTO( lLatestFlightName, lActualDepartureDate, lActualArrivalDate );

      iFlightHistBean = new FlightHistBean();
      iFlightHistBean.ejbCreate();
      iFlightHistBean.setSessionContext( new SessionContextFake() );
   }


   @After
   public void teardown() {
      int lUserId = OrgHr.findByPrimaryKey( iHrKey ).getUserId();
      UserParameters.setInstance( lUserId, "LOGIC", null );
   }


   private FlightLegId createFlight( InventoryKey aAircraftInvKey,
         FlightInformationTO aFlightInformationTO, CollectedUsageParm[] aFlightUsageParms )
         throws Exception {

      return iFlightHistBean.createHistFlight( new AircraftKey( aAircraftInvKey ), iHrKey,
            aFlightInformationTO, aFlightUsageParms, NO_MEASUREMENTS );
   }


   private FlightLegId[] createFlightsForAircraftInv() throws Exception {

      CollectedUsageParm[] lFlightUsageParms = {
            generateFlightUsage( iAircraftInvKey, CYCLES, INITIAL_FLIGHT_DELTA,
                  INITIAL_CYCLES.add( INITIAL_FLIGHT_DELTA ) ),
            generateFlightUsage( iAircraftInvKey, HOURS, INITIAL_FLIGHT_DELTA,
                  INITIAL_HOURS.add( INITIAL_FLIGHT_DELTA ) ) };

      FlightLegId[] lLegIds =
            { createFlight( iAircraftInvKey, iFirstFlightInfoTO, lFlightUsageParms ),
                  createFlight( iAircraftInvKey, iLatestFlightInfoTO, lFlightUsageParms ) };

      // Expected usage would be the sum of the initial usage and the usage of the 2 flights
      // created above
      final BigDecimal lExpectedCyclesBeforeEdit =
            INITIAL_CYCLES.add( INITIAL_FLIGHT_DELTA ).add( INITIAL_FLIGHT_DELTA );
      final BigDecimal lExpectedHoursBeforeEdit =
            INITIAL_HOURS.add( INITIAL_FLIGHT_DELTA ).add( INITIAL_FLIGHT_DELTA );
      assertCurrentUsage( iAircraftInvKey, CYCLES, lExpectedCyclesBeforeEdit );
      assertCurrentUsage( iAircraftInvKey, HOURS, lExpectedHoursBeforeEdit );

      return lLegIds;
   }


   private FlightLegId[] createFlightsForAircraftWithOneEngineInv() throws Exception {

      CollectedUsageParm[] lFlightUsageParms = {
            generateFlightUsage( iAircraftWithOneEngineInvKey, CYCLES, INITIAL_FLIGHT_DELTA,
                  INITIAL_CYCLES.add( INITIAL_FLIGHT_DELTA ) ),
            generateFlightUsage( iAircraftWithOneEngineInvKey, HOURS, INITIAL_FLIGHT_DELTA,
                  INITIAL_HOURS.add( INITIAL_FLIGHT_DELTA ) ),
            generateFlightUsage( iEngineInvKey, CYCLES, INITIAL_FLIGHT_DELTA,
                  INITIAL_CYCLES.add( INITIAL_FLIGHT_DELTA ) ),
            generateFlightUsage( iEngineInvKey, HOURS, INITIAL_FLIGHT_DELTA,
                  INITIAL_HOURS.add( INITIAL_FLIGHT_DELTA ) ) };

      FlightLegId[] lLegIds = {
            createFlight( iAircraftWithOneEngineInvKey, iFirstFlightInfoTO, lFlightUsageParms ),
            createFlight( iAircraftWithOneEngineInvKey, iLatestFlightInfoTO, lFlightUsageParms ) };

      // Expected cycles would be the sum of the current cycles usage and the usage of the 2 flights
      // created above
      final BigDecimal lExpectedCyclesBeforeEdit =
            INITIAL_CYCLES.add( INITIAL_FLIGHT_DELTA ).add( INITIAL_FLIGHT_DELTA );
      final BigDecimal lExpectedHoursBeforeEdit =
            INITIAL_HOURS.add( INITIAL_FLIGHT_DELTA ).add( INITIAL_FLIGHT_DELTA );
      assertCurrentUsage( iAircraftWithOneEngineInvKey, CYCLES, lExpectedCyclesBeforeEdit );
      assertCurrentUsage( iAircraftWithOneEngineInvKey, HOURS, lExpectedHoursBeforeEdit );
      assertCurrentUsage( iEngineInvKey, CYCLES, lExpectedCyclesBeforeEdit );
      assertCurrentUsage( iEngineInvKey, HOURS, lExpectedHoursBeforeEdit );

      return lLegIds;
   }


   private CollectedUsageParm generateFlightUsage( InventoryKey aInventoryKey,
         DataTypeKey lDataType, BigDecimal lDelta, BigDecimal lNewTSValue ) {

      // Create a usage collection to be returned.
      // CollectedUsageParm lUsageParm =
      new CollectedUsageParm( new UsageParmKey( aInventoryKey, lDataType ),
            lNewTSValue.doubleValue(), lNewTSValue.doubleValue(), lNewTSValue.doubleValue(),
            lDelta.doubleValue() );

      CollectedUsageParm lUsageParm = new CollectedUsageParm(
            new UsageParmKey( aInventoryKey, lDataType ), lNewTSValue.doubleValue(),
            lNewTSValue.doubleValue(), lNewTSValue.doubleValue(), lDelta.doubleValue() );

      // Create flight data source specifications.
      EqpDataSourceSpec
            .create( new UsageDefinitionKey( InvInvTable.getAssemblyByInventoryKey( aInventoryKey ),
                  RefDataSourceKey.MXFL ), lDataType );

      return lUsageParm;
   }


   private FlightInformationTO generateFlightInfoTO( StringBuilder aFlightName,
         Date aActualDepartureDate, Date aActualArrivalDate ) {
      LocationKey lDepartureAirport = Domain.createLocation( new DomainConfiguration<Location>() {

         @Override
         public void configure( Location aLocation ) {
            aLocation.setType( RefLocTypeKey.AIRPORT );
         }
      } );
      LocationKey lArrivalAirport = Domain.createLocation( new DomainConfiguration<Location>() {

         @Override
         public void configure( Location aLocation ) {
            aLocation.setType( RefLocTypeKey.AIRPORT );
         }
      } );

      return new FlightInformationTO( aFlightName.toString(), null, null, null, null, null,
            lDepartureAirport, lArrivalAirport, null, null, null, null, aActualDepartureDate,
            aActualArrivalDate, null, null, false, false );

   }


   private void assertCurrentUsage( InventoryKey aInventory, DataTypeKey aDataType,
         BigDecimal aExpectedUsage ) {

      CurrentUsages lCurrentUsage = new CurrentUsages( aInventory );
      assertEquals( "Inventory=" + aInventory + " , data type=" + aDataType + " : unexpected TSN",
            aExpectedUsage, lCurrentUsage.getTsn( aDataType ) );
      assertEquals( "Inventory=" + aInventory + " , data type=" + aDataType + " : unexpected TSO",
            aExpectedUsage, lCurrentUsage.getTso( aDataType ) );
      assertEquals( "Inventory=" + aInventory + " , data type=" + aDataType + " : unexpected TSI",
            aExpectedUsage, lCurrentUsage.getTsi( aDataType ) );

   }

}
