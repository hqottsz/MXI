package com.mxi.mx.core.ejb.flighthist.flighthistbean.edithistflight;

import static com.mxi.mx.core.key.DataTypeKey.HOURS;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Flight;
import com.mxi.am.domain.Location;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.ejb.flighthist.FlightHistBean;
import com.mxi.mx.core.flight.model.FlightLegId;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefDataSourceKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefSchedFromKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.UsageDefinitionKey;
import com.mxi.mx.core.key.UsageParmKey;
import com.mxi.mx.core.services.event.usage.CollectedUsageParm;
import com.mxi.mx.core.services.flighthist.FlightInformationTO;
import com.mxi.mx.core.services.flighthist.Measurement;
import com.mxi.mx.core.table.eqp.EqpDataSourceSpec;
import com.mxi.mx.core.table.evt.EvtSchedDeadTable;
import com.mxi.mx.core.table.evt.EvtStageTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.org.OrgHr;


/**
 * Tests for {@linkplain FlightHistBean#editHistFlight} that involve adding system notes to deadline
 * of tasks
 *
 */

public class EditHistFlight_AddHistoryNoteForAffecedDeadlineTest {

   private static final BigDecimal INITIAL_FLIGHT_HOURS = new BigDecimal( 5 );
   private static final BigDecimal INITIAL_FLIGHT_HOURS_TSN = new BigDecimal( 555 );
   private static final BigDecimal UPDATED_FLIGHT_HOURS = new BigDecimal( 7 );
   private static final BigDecimal AIRCRAFT_CURRENT_HOURS = new BigDecimal( 1000 );
   private static final BigDecimal DEADLINE_HOURS_INTERVAL = new BigDecimal( 100 );

   // Not relevant to tests.
   private static final String HR_USERNAME = "HR_USERNAME";
   private static final Measurement[] NO_MEASUREMENTS = new Measurement[] {};
   private static final String FLIGHT_NAME = "FLIGHT_NAME";
   private static final Date FLIGHT_DATE = DateUtils.addDays( new Date(), -200 );

   // The bean under test.
   private FlightHistBean iFlightHistBean;
   private HumanResourceKey iHrKey;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Test
   public void itAdjustsDeadlinesAndAddSystemNoteOnAircraftTaskThatWasInitializedAfterEditedFlight()
         throws Exception {

      // Given an aircraft collecting usage.
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, AIRCRAFT_CURRENT_HOURS );
         }
      } );

      // Given a flight for the aircraft that accrued usage.
      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( FLIGHT_DATE );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS, INITIAL_FLIGHT_HOURS_TSN );
         }
      } );

      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( DateUtils.addDays( FLIGHT_DATE, 1 ) );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS,
                  INITIAL_FLIGHT_HOURS_TSN.add( INITIAL_FLIGHT_HOURS ) );
         }
      } );

      // Given a requirement with a usage deadline, is against the aircraft, and was initialized
      // after the flight.
      //
      // Note: The task is considered initialized after the flight when its deadline start value is
      // greater than the flight usage value.

      final BigDecimal lDeadlineStartValue = INITIAL_FLIGHT_HOURS_TSN.add( TEN );
      final BigDecimal lDeadlineDueValue = lDeadlineStartValue.add( ONE );

      TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lAircraft );
            aReq.addUsageDeadline( HOURS, RefSchedFromKey.EFFECTIV, lDeadlineStartValue,
                  DEADLINE_HOURS_INTERVAL, lDeadlineDueValue );
         }
      } );

      // When the flight is edited and the usage is modified.
      editHistFlightUsage( lFlight, UPDATED_FLIGHT_HOURS, lAircraft );

      // Then the task's deadline start value and due value are adjusted by the difference of the
      // deltas.
      BigDecimal lUsageDifference = UPDATED_FLIGHT_HOURS.subtract( INITIAL_FLIGHT_HOURS );

      Double lExpectedStartValue = lDeadlineStartValue.add( lUsageDifference ).doubleValue();
      Double lExpectedDueValue = lDeadlineDueValue.add( lUsageDifference ).doubleValue();

      EvtSchedDeadTable lEvtSchedDead = EvtSchedDeadTable.findByPrimaryKey( lReq, HOURS );
      Double lActualStartValue = lEvtSchedDead.getStartQt();
      Double lActualDueValue = lEvtSchedDead.getDeadlineQt();

      assertEquals( "Unexpected deadline start value.", lExpectedStartValue, lActualStartValue );
      assertEquals( "Unexpected deadline due value.", lExpectedDueValue, lActualDueValue );

      String lExpectedDeadlineSystemNote = i18n.get( "core.msg.DEADLINE_USAGE_CHANGE_SYSTEM_NOTE" );
      assertDeadlineNote( lReq.getEventKey(), lExpectedDeadlineSystemNote );
   }


   @Test
   public void
         itDoesNotAdjustDeadlinesAndDoesNotAddSystemNoteOnAircraftTaskThatWasInitializedBeforeEditedFlight()
               throws Exception {

      // Given an aircraft.
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, AIRCRAFT_CURRENT_HOURS );
         }
      } );

      // Given a flight for the aircraft that accrued usage.
      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( FLIGHT_DATE );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS, INITIAL_FLIGHT_HOURS_TSN );
         }
      } );

      // Given a requirement with a usage deadline, is against the aircraft, and was initialized
      // before the flight.
      //
      // Note: The task is considered initialized before the flight when its deadline start value is
      // less than the flight usage value.

      final BigDecimal lDeadlineStartValue = INITIAL_FLIGHT_HOURS_TSN.subtract( TEN );
      final BigDecimal lDeadlineDueValue = lDeadlineStartValue.add( ONE );

      TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lAircraft );
            aReq.addUsageDeadline( HOURS, RefSchedFromKey.EFFECTIV, lDeadlineStartValue,
                  DEADLINE_HOURS_INTERVAL, lDeadlineDueValue );
         }
      } );

      // When the flight is edited and the usage is modified.
      editHistFlightUsage( lFlight, UPDATED_FLIGHT_HOURS, lAircraft );

      // Then the task's deadline start value and due value are NOT adjusted.
      EvtSchedDeadTable lEvtSchedDead = EvtSchedDeadTable.findByPrimaryKey( lReq, HOURS );
      BigDecimal lActualStartValue = new BigDecimal( lEvtSchedDead.getStartQt() );
      BigDecimal lActualDueValue = new BigDecimal( lEvtSchedDead.getDeadlineQt() );

      assertEquals( "Unexpected deadline start value.", lDeadlineStartValue, lActualStartValue );
      assertEquals( "Unexpected deadline due value.", lDeadlineDueValue, lActualDueValue );

      String lExpectedDeadlineSystemNote = "DEADLINE_USAGE_CHANGE_SYSTEM_NOTE";
      assertDeadlineNoteNotFound( lReq.getEventKey(), lExpectedDeadlineSystemNote );
   }


   @Before
   public void setup() {
      iHrKey = new HumanResourceDomainBuilder().withUsername( HR_USERNAME ).build();

      int lUserId = OrgHr.findByPrimaryKey( iHrKey ).getUserId();
      UserParameters.setInstance( lUserId, "LOGIC", new UserParametersFake( lUserId, "LOGIC" ) );

      iFlightHistBean = new FlightHistBean();
      iFlightHistBean.ejbCreate();
      iFlightHistBean.setSessionContext( new SessionContextFake() );

   }


   @After
   public void tearDown() {
      int lUserId = OrgHr.findByPrimaryKey( iHrKey ).getUserId();
      UserParameters.setInstance( lUserId, "LOGIC", null );
   }


   private void editHistFlightUsage( FlightLegId aFlight, BigDecimal aUpdatedFlightHours,
         InventoryKey... aInventoryKeys ) throws Exception {

      FlightInformationTO lUpdatedFlightTo =
            generateFlightInfoTO( FLIGHT_NAME, FLIGHT_DATE, UPDATED_FLIGHT_HOURS );

      List<CollectedUsageParm> lUpdatedFlightUsages =
            new ArrayList<CollectedUsageParm>( aInventoryKeys.length );
      for ( InventoryKey lInventoryKey : aInventoryKeys ) {
         lUpdatedFlightUsages
               .add( generateFlightUsage( lInventoryKey, HOURS, aUpdatedFlightHours ) );
      } ;

      iFlightHistBean.editHistFlight( aFlight, iHrKey, lUpdatedFlightTo,
            lUpdatedFlightUsages.toArray( new CollectedUsageParm[] {} ), NO_MEASUREMENTS );
   }


   private FlightInformationTO generateFlightInfoTO( String aName, Date aArrivalDate,
         BigDecimal aHours ) {

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

      Date lDepartureDate =
            DateUtils.addHours( aArrivalDate, aHours.multiply( new BigDecimal( -1 ) ).intValue() );

      return new FlightInformationTO( aName, null, null, null, null, null, lDepartureAirport,
            lArrivalAirport, null, null, null, null, lDepartureDate, aArrivalDate, null, null,
            false, false );
   }


   private CollectedUsageParm generateFlightUsage( InventoryKey aInventory, DataTypeKey lDataType,
         BigDecimal lDelta ) {

      // Create a usage collection to be returned.
      CollectedUsageParm lUsageParm = new CollectedUsageParm(
            new UsageParmKey( aInventory, lDataType ), lDelta.doubleValue() );

      // Create flight data source specifications.
      EqpDataSourceSpec
            .create( new UsageDefinitionKey( InvInvTable.getAssemblyByInventoryKey( aInventory ),
                  RefDataSourceKey.MXFL ), lDataType );

      return lUsageParm;
   }


   private void assertDeadlineNote( EventKey aEventKey, String aSystemNote ) {
      DataSet lStageSnapshot = EvtStageTable.getStageSnapshot( aEventKey );
      while ( lStageSnapshot.next() ) {
         String lNote = lStageSnapshot.getString( "stage_note" );
         Assert.assertTrue( lNote.contains( aSystemNote ) );
         return;
      }

      Assert.fail( "Deadline note is not found." );
   }


   private void assertDeadlineNoteNotFound( EventKey aEventKey, String aSystemNote ) {

      DataSet lStageSnapshot = EvtStageTable.getStageSnapshot( aEventKey );

      Assert.assertTrue( "There should not be any system note, expected 0 row",
            lStageSnapshot.getRowCount() == 0 );

   }

}
