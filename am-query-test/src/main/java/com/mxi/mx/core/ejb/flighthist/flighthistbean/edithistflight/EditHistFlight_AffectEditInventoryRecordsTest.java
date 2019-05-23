package com.mxi.mx.core.ejb.flighthist.flighthistbean.edithistflight;

import static com.mxi.mx.common.utils.DateUtils.addHours;
import static com.mxi.mx.core.key.DataTypeKey.HOURS;
import static com.mxi.mx.core.usage.model.UsageType.MXCORRECTION;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Flight;
import com.mxi.am.domain.Location;
import com.mxi.am.domain.SerializedInventory;
import com.mxi.am.domain.TrackedInventory;
import com.mxi.am.domain.UsageAdjustment;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.ejb.flighthist.FlightHistBean;
import com.mxi.mx.core.flight.model.FlightLegId;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.UsageParmKey;
import com.mxi.mx.core.services.event.usage.CollectedUsageParm;
import com.mxi.mx.core.services.flighthist.FlightInformationTO;
import com.mxi.mx.core.services.flighthist.Measurement;
import com.mxi.mx.core.table.org.OrgHr;
import com.mxi.mx.core.usage.model.UsageAdjustmentId;


/**
 * Tests for {@linkplain FlightHistBean#editHistFlight} that involve updating the usage deltas of a
 * historical flight and their effects on edit-inventory records (e.g. usage corrections) that
 * occurred after the flight and against inventory the flew as part of the flight.
 *
 */
public class EditHistFlight_AffectEditInventoryRecordsTest {

   private static final BigDecimal AIRCRAFT_CURRENT_HOURS = new BigDecimal( "1000" );
   private static final BigDecimal TRK_CURRENT_HOURS = new BigDecimal( "5000" );

   private static final String FLIGHT_NAME = "FLIGHT_NAME";
   private static final BigDecimal FLIGHT_HOURS = new BigDecimal( "3" );
   private static final Date HIST_FLIGHT_DEPARTURE_DATE = DateUtils.addDays( new Date(), -100 );
   private static final Date HIST_FLIGHT_ARRIVAL_DATE =
         DateUtils.addHours( HIST_FLIGHT_DEPARTURE_DATE, FLIGHT_HOURS.intValue() );

   private static final BigDecimal UPDATED_FLIGHT_HOURS = new BigDecimal( "5" );

   private static final Measurement[] NO_MEASUREMENTS = new Measurement[] {};

   private int iUserId;
   private HumanResourceKey iHrKey;

   // Class under test.
   private FlightHistBean iFlightHistBean;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    *
    * Verify that an edit-inventory record for a TRK is NOT UPDATED when it was collection prior to
    * a flight whose usage is updated.
    *
    * <pre>
    * Given an aircraft that is collecting usage And a TRK inventory that is attached to the
    * aircraft and collecting the same usage And the aircraft has a historical flight that accrued
    * usage (which the TRK was part of) And the TRK inventory has an edit-inventory usage correction
    * that was collected prior to the flight When the flight usage is edited Then the edit-inventory
    * usage correction is not modified
    *
    * <pre>
    *
    */
   @Test
   public void itDoesNotUpdateTrkEditInvRecordCollectedPriorToEditedFlight() throws Exception {

      // Set up some dates and usage TSN for the test (flight arrival is in the past).
      final BigDecimal lFlightHoursTsn = AIRCRAFT_CURRENT_HOURS.subtract( new BigDecimal( "100" ) );

      final Date lEditInvPriorToFlightDate = DateUtils.addDays( HIST_FLIGHT_ARRIVAL_DATE, -15 );
      final BigDecimal lEditInvHoursTsn = lFlightHoursTsn.subtract( new BigDecimal( "35" ) );

      // Given an aircraft that is collecting usage,
      // and a TRK inventory that is attached to the aircraft and collecting the same usage.
      final InventoryKey lTrkInv =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aTrk ) {
                  aTrk.addUsage( HOURS, TRK_CURRENT_HOURS );
               }
            } );
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, AIRCRAFT_CURRENT_HOURS );
            aAircraft.addTracked( lTrkInv );
         }
      } );

      // Given the aircraft has a historical flight that accrued usage (which the TRK was part of).
      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( HIST_FLIGHT_ARRIVAL_DATE );
            aFlight.addUsage( lAircraft, HOURS, FLIGHT_HOURS, null );
         }
      } );

      // Given the TRK inventory has an edit-inventory usage correction that was collected prior to
      // the flight (thus, has an HOURS tsn that is less than the current).
      UsageAdjustmentId lUsageAdjustmentId =
            Domain.createUsageAdjustment( new DomainConfiguration<UsageAdjustment>() {

               @Override
               public void configure( UsageAdjustment aBuilder ) {
                  aBuilder.setUsageDate( lEditInvPriorToFlightDate );
                  aBuilder.setMainInventory( lTrkInv );
                  aBuilder.setUsageType( MXCORRECTION );
                  aBuilder.addUsage( lTrkInv, HOURS, lEditInvHoursTsn, null );
               }
            } );

      // When the flight usage is edited.
      editHistFlightUsage( lFlight, UPDATED_FLIGHT_HOURS, lAircraft );

      // Then the edit-inventory usage correction is not modified.
      assertEquals( "Unexpected HOURS TSN for usage adjustment.", lEditInvHoursTsn,
            getUsageAdjustmentHoursTsn( lTrkInv, lUsageAdjustmentId ) );
   }


   /**
    *
    * Verify that all edit-inventory records for a TRK are UPDATED when they are collected after a
    * flight whose usage is updated.
    *
    * <pre>
    * Given an aircraft that is collecting usage And a TRK inventory that is attached to the
    * aircraft and collecting the same usage And the aircraft has a historical flight that accrued
    * usage (which the TRK was part of) And the TRK inventory has many edit-inventory usage
    * corrections that were collected after the flight When the flight usage is edited Then the
    * difference in the edited flight usage is applied to the edit-inventory usage corrections
    *
    * <pre>
    *
    */
   @Test
   public void itUpdatesTrkEditInvRecordsCollectedAfterAnEditedFlight() throws Exception {

      // Set up some dates and usage TSN for the test (flight arrival is in the past).
      final BigDecimal lFlightHoursTsn = AIRCRAFT_CURRENT_HOURS.subtract( new BigDecimal( "200" ) );

      final Date lEditInvAfterFlightDate1 = DateUtils.addDays( HIST_FLIGHT_ARRIVAL_DATE, 5 );
      final BigDecimal lEditInvHoursTsn1 = lFlightHoursTsn.add( new BigDecimal( "25" ) );

      final Date lEditInvAfterFlightDate2 = DateUtils.addDays( lEditInvAfterFlightDate1, 40 );
      final BigDecimal lEditInvHoursTsn2 = lEditInvHoursTsn1.add( new BigDecimal( "30" ) );

      // Given an aircraft that is collecting usage,
      // and a TRK inventory that is attached to the aircraft and collecting the same usage.
      final InventoryKey lTrkInv =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aTrk ) {
                  aTrk.addUsage( HOURS, TRK_CURRENT_HOURS );
               }
            } );
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, AIRCRAFT_CURRENT_HOURS );
            aAircraft.addTracked( lTrkInv );
         }
      } );

      // Given the aircraft has a historical flight that accrued usage (which the TRK was part of).
      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( HIST_FLIGHT_ARRIVAL_DATE );
            aFlight.addUsage( lAircraft, HOURS, FLIGHT_HOURS, null );
         }
      } );

      // Given the TRK inventory has many edit-inventory usage corrections that were collected after
      // the flight.
      UsageAdjustmentId lUsageAdjustmentId1 =
            Domain.createUsageAdjustment( new DomainConfiguration<UsageAdjustment>() {

               @Override
               public void configure( UsageAdjustment aBuilder ) {
                  aBuilder.setUsageDate( lEditInvAfterFlightDate1 );
                  aBuilder.setMainInventory( lTrkInv );
                  aBuilder.setUsageType( MXCORRECTION );
                  aBuilder.addUsage( lTrkInv, HOURS, lEditInvHoursTsn1, null );
               }
            } );

      UsageAdjustmentId lUsageAdjustmentId2 =
            Domain.createUsageAdjustment( new DomainConfiguration<UsageAdjustment>() {

               @Override
               public void configure( UsageAdjustment aBuilder ) {
                  aBuilder.setUsageDate( lEditInvAfterFlightDate2 );
                  aBuilder.setMainInventory( lTrkInv );
                  aBuilder.setUsageType( MXCORRECTION );
                  aBuilder.addUsage( lTrkInv, HOURS, lEditInvHoursTsn2, null );
               }
            } );

      // When the flight usage is edited.
      editHistFlightUsage( lFlight, UPDATED_FLIGHT_HOURS, lAircraft );

      // Then the difference in the edited flight usage is applied to the edit-inventory usage
      // corrections.
      BigDecimal lFlightUsageDifference = UPDATED_FLIGHT_HOURS.subtract( FLIGHT_HOURS );

      BigDecimal lExpectedHoursForAdjustment1 = lEditInvHoursTsn1.add( lFlightUsageDifference );
      assertEquals( "Unexpected HOURS TSN for first usage adjustment.",
            lExpectedHoursForAdjustment1,
            getUsageAdjustmentHoursTsn( lTrkInv, lUsageAdjustmentId1 ) );

      BigDecimal lExpectedHoursForAdjustment2 = lEditInvHoursTsn2.add( lFlightUsageDifference );
      assertEquals( "Unexpected HOURS TSN for second usage adjustment.",
            lExpectedHoursForAdjustment2,
            getUsageAdjustmentHoursTsn( lTrkInv, lUsageAdjustmentId2 ) );
   }


   /**
    *
    * Verify that all edit-inventory records for a SER are UPDATED when they are collected after a
    * flight whose usage is updated.
    *
    * <pre>
    * Given an aircraft that is collecting usage And a SER inventory that is attached to the
    * aircraft and collecting the same usage And the aircraft has a historical flight that accrued
    * usage (which the SER was part of) And the SER inventory has many edit-inventory usage
    * corrections that were collected after the flight When the flight usage is edited Then the
    * difference in the edited flight usage is applied to the edit-inventory usage corrections
    *
    * <pre>
    *
    */
   @Test
   public void itUpdatesSEREditInvRecordsCollectedAfterAnEditedFlight() throws Exception {

      // Set up some dates and usage TSN for the test (flight arrival is in the past).
      final BigDecimal lFlightHoursTsn = AIRCRAFT_CURRENT_HOURS.subtract( new BigDecimal( "300" ) );

      final Date lEditInvAfterFlightDate1 = DateUtils.addDays( HIST_FLIGHT_ARRIVAL_DATE, 50 );
      final BigDecimal lEditInvHoursTsn1 = lFlightHoursTsn.add( new BigDecimal( "300" ) );

      final Date lEditInvAfterFlightDate2 = DateUtils.addDays( lEditInvAfterFlightDate1, 25 );
      final BigDecimal lEditInvHoursTsn2 = lEditInvHoursTsn1.add( new BigDecimal( "100" ) );

      // Given an aircraft that is collecting usage,
      // and a SER inventory that is attached to the aircraft and collecting the same usage.
      final InventoryKey lSerInv =
            Domain.createSerializedInventory( new DomainConfiguration<SerializedInventory>() {

               @Override
               public void configure( SerializedInventory aSer ) {
                  aSer.addUsage( HOURS, TRK_CURRENT_HOURS );
               }
            } );
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, AIRCRAFT_CURRENT_HOURS );
            aAircraft.addSerialized( lSerInv );
         }
      } );

      // Given the aircraft has a historical flight that accrued usage (which the SER was part of).
      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( HIST_FLIGHT_ARRIVAL_DATE );
            aFlight.addUsage( lAircraft, HOURS, FLIGHT_HOURS, null );
         }
      } );

      // Given the SER inventory has many edit-inventory usage corrections that were collected after
      // the flight.
      UsageAdjustmentId lUsageAdjustmentId1 =
            Domain.createUsageAdjustment( new DomainConfiguration<UsageAdjustment>() {

               @Override
               public void configure( UsageAdjustment aBuilder ) {
                  aBuilder.setUsageDate( lEditInvAfterFlightDate1 );
                  aBuilder.setMainInventory( lSerInv );
                  aBuilder.setUsageType( MXCORRECTION );
                  aBuilder.addUsage( lSerInv, HOURS, lEditInvHoursTsn1, null );
               }
            } );

      UsageAdjustmentId lUsageAdjustmentId2 =
            Domain.createUsageAdjustment( new DomainConfiguration<UsageAdjustment>() {

               @Override
               public void configure( UsageAdjustment aBuilder ) {
                  aBuilder.setUsageDate( lEditInvAfterFlightDate2 );
                  aBuilder.setMainInventory( lSerInv );
                  aBuilder.setUsageType( MXCORRECTION );
                  aBuilder.addUsage( lSerInv, HOURS, lEditInvHoursTsn2, null );
               }
            } );

      // When the flight usage is edited.
      editHistFlightUsage( lFlight, UPDATED_FLIGHT_HOURS, lAircraft );

      // Then the difference in the edited flight usage is applied to the edit-inventory usage
      // corrections.
      BigDecimal lFlightUsageDifference = UPDATED_FLIGHT_HOURS.subtract( FLIGHT_HOURS );

      BigDecimal lExpectedHoursForAdjustment1 = lEditInvHoursTsn1.add( lFlightUsageDifference );
      assertEquals( "Unexpected HOURS TSN for first usage adjustment.",
            lExpectedHoursForAdjustment1,
            getUsageAdjustmentHoursTsn( lSerInv, lUsageAdjustmentId1 ) );

      BigDecimal lExpectedHoursForAdjustment2 = lEditInvHoursTsn2.add( lFlightUsageDifference );
      assertEquals( "Unexpected HOURS TSN for second usage adjustment.",
            lExpectedHoursForAdjustment2,
            getUsageAdjustmentHoursTsn( lSerInv, lUsageAdjustmentId2 ) );
   }


   @Before
   public void setup() {
      iHrKey = new HumanResourceDomainBuilder().build();
      iUserId = OrgHr.findByPrimaryKey( iHrKey ).getUserId();

      // Use the fake user parameters to set MAX_FLIGHT_DURATION_VALUE.
      UserParameters.setInstance( iUserId, "LOGIC", new UserParametersFake( iUserId, "LOGIC" ) );

      iFlightHistBean = new FlightHistBean();
      iFlightHistBean.ejbCreate();
      iFlightHistBean.setSessionContext( new SessionContextFake() );
   }


   @After
   public void tearDown() {
      UserParameters.setInstance( iUserId, "LOGIC", null );
   }


   private void editHistFlightUsage( FlightLegId aFlight, BigDecimal aUpdatedFlightHours,
         InventoryKey aAircraft ) throws Exception {

      // Departure and arrival airports are mandatory but not relevant to the tests.
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

      Date lNewArrivalDate = addHours( HIST_FLIGHT_DEPARTURE_DATE, aUpdatedFlightHours.intValue() );

      FlightInformationTO lUpdatedFlightTo = new FlightInformationTO( FLIGHT_NAME, null, null, null,
            null, null, lDepartureAirport, lArrivalAirport, null, null, null, null,
            HIST_FLIGHT_DEPARTURE_DATE, lNewArrivalDate, null, null, false, false );

      CollectedUsageParm[] lUpdatedUsageParms =
            new CollectedUsageParm[] { new CollectedUsageParm( new UsageParmKey( aAircraft, HOURS ),
                  aUpdatedFlightHours.doubleValue() ) };

      iFlightHistBean.editHistFlight( aFlight, iHrKey, lUpdatedFlightTo, lUpdatedUsageParms,
            NO_MEASUREMENTS );
   }


   private BigDecimal getUsageAdjustmentHoursTsn( InventoryKey aInvKey,
         UsageAdjustmentId aUsageAdjustmentId ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "usage_record_id", aUsageAdjustmentId );
      lArgs.add( aInvKey, "inv_no_db_id", "inv_no_id" );
      lArgs.add( HOURS, "data_type_db_id", "data_type_id" );
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "usg_usage_data", lArgs );
      lQs.next();

      return lQs.getBigDecimal( "tsn_qt" );
   }

}
