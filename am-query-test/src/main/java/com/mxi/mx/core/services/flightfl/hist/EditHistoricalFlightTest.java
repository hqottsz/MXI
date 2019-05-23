package com.mxi.mx.core.services.flightfl.hist;

import static com.mxi.mx.common.utils.DateUtils.addHours;
import static com.mxi.mx.core.key.DataTypeKey.HOURS;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
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
import com.mxi.mx.core.flight.dao.FlightLegNoteEntity;
import com.mxi.mx.core.flight.dao.FlightLegStatusLogEntity;
import com.mxi.mx.core.flight.dao.JdbcFlightLegNoteDao;
import com.mxi.mx.core.flight.dao.JdbcFlightLegStatusLogDao;
import com.mxi.mx.core.flight.model.FlightLegId;
import com.mxi.mx.core.flight.model.FlightLegNoteId;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.UsageParmKey;
import com.mxi.mx.core.services.event.usage.CollectedUsageParm;
import com.mxi.mx.core.services.flighthist.FlightInformationTO;
import com.mxi.mx.core.services.flighthist.Measurement;
import com.mxi.mx.core.table.mim.MimDataType;


public class EditHistoricalFlightTest {

   private static final BigDecimal INITIAL_HOURS = new BigDecimal( 100 );
   private static final BigDecimal FLIGHT_HOURS = new BigDecimal( 5 );
   private static final BigDecimal UPDATED_FLIGHT_HOURS = new BigDecimal( 8 );
   private static final BigDecimal FLIGHT_HOURS_UPDATE_DIFF =
         UPDATED_FLIGHT_HOURS.subtract( FLIGHT_HOURS );

   private static final Date DEPARTURE_DATE = DateUtils.addDays( new Date(), -10 );
   private static final Date ARRIVAL_DATE =
         DateUtils.addHours( DEPARTURE_DATE, FLIGHT_HOURS.intValue() );

   private static final String FLIGHT_NAME = "FLIGHT NAME";
   private static final String USER_NOTE = "USER NOTE";
   private static final Measurement[] NO_MEASUREMENTS = new Measurement[] {};
   private static final int USER_ID = 1234;

   private LocationKey iDepartureAirport;
   private LocationKey iArrivalAirport;
   private HumanResourceKey iHumanResource;
   FlightHistBean iFlightHistBean;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Verify the non-usage fields is updated, when non-usage fields of historical flight is edited.
    *
    * <pre>
    *       Given an aircraft collection usages
    *       When the historical flight is edited
    *        And non-usage field is modified
    *       Then the historical flight non-usage field is updated
    * </pre>
    *
    */
   @Test
   public void itPersistsArrivalDateWhenEditedArrivalDateHistoricFlight() throws Exception {

      // Given
      final InventoryKey lAircraft = createAircraft();

      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( ARRIVAL_DATE );
         }
      } );

      // When
      Date lNewArrivalDate = addHours( ARRIVAL_DATE, 1 );

      FlightInformationTO lUpdatedFlightTo = new FlightInformationTO( FLIGHT_NAME, null, null, null,
            null, null, iDepartureAirport, iArrivalAirport, null, null, null, null, DEPARTURE_DATE,
            lNewArrivalDate, null, null, false, false );

      iFlightHistBean.editHistFlight( lFlight, iHumanResource, lUpdatedFlightTo, null,
            NO_MEASUREMENTS );

      // Then
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lAircraft, "aircraft_db_id", "aircraft_id" );
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "fl_leg", lArgs );
      lQs.next();

      // DateUtils.floorSecond Return the #0 second for a given minute
      // Oracle Date format does not store millisecond in DATE column
      Date lActualNewArrivalDate = DateUtils.floorSecond( lQs.getDate( "actual_arrival_dt" ) );
      Date lExpectedNewArrivalDate = DateUtils.floorSecond( lNewArrivalDate );

      Assert.assertEquals( "Unexpected new arrival date for aircraft",
            lExpectedNewArrivalDate.compareTo( lActualNewArrivalDate ), 0 );

   }


   /**
    * Verify an user note being persisted when adding an user note to a Historic Flight.
    *
    * <pre>
    *       Given an aircraft collection usages
    *       When the historical flight is edited
    *        And an user note is added
    *       Then the user note being persisted to historic flight
    * </pre>
    *
    */
   @Test
   public void itPersistsUserNoteWhenAddUserNoteHistoricFlight() throws Exception {

      // Given
      final InventoryKey lAircraft = createAircraft();

      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.addUsage( lAircraft, HOURS, FLIGHT_HOURS );
         }
      } );

      // When
      FlightInformationTO lUpdatedFlightTo = new FlightInformationTO( FLIGHT_NAME, null, null, null,
            null, null, iDepartureAirport, iArrivalAirport, null, null, null, null, DEPARTURE_DATE,
            ARRIVAL_DATE, null, null, false, false );
      lUpdatedFlightTo.setEntryNote( USER_NOTE );

      iFlightHistBean.editHistFlight( lFlight, iHumanResource, lUpdatedFlightTo, null,
            NO_MEASUREMENTS );

      // Then
      String lActualUserNote = getUserNote( lFlight );
      Assert.assertEquals( "Unexpected user note added to flight.", USER_NOTE, lActualUserNote );

   }


   /**
    * Verify the usage values being persisted when all the usage delta are modified
    *
    * <pre>
    *       Given an aircraft collection usages
    *       When the historical flight is edited
    *        And all the usage deltas are modified
    *       Then all the flight's usage deltas are persisted
    * </pre>
    *
    */
   @Test
   public void itPersistsUsageValuesWhenEditedAllTheUsageDeltaHistoricalFlight() throws Exception {

      // Given
      final InventoryKey lAircraft = createAircraft();

      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.addUsage( lAircraft, HOURS, FLIGHT_HOURS );
         }
      } );

      // When
      CollectedUsageParm[] lUpdatedUsageParms =
            new CollectedUsageParm[] { new CollectedUsageParm( new UsageParmKey( lAircraft, HOURS ),
                  UPDATED_FLIGHT_HOURS.doubleValue() ) };

      FlightInformationTO lUpdatedFlightTo = new FlightInformationTO( FLIGHT_NAME, null, null, null,
            null, null, iDepartureAirport, iArrivalAirport, null, null, null, null, DEPARTURE_DATE,
            ARRIVAL_DATE, null, null, false, false );

      iFlightHistBean.editHistFlight( lFlight, iHumanResource, lUpdatedFlightTo, lUpdatedUsageParms,
            NO_MEASUREMENTS );

      // Then

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lAircraft, "inv_no_db_id", "inv_no_id" );
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "usg_usage_data", lArgs );
      lQs.next();

      BigDecimal lActualUpdateHours = lQs.getBigDecimal( "tsn_delta_qt" );
      BigDecimal lExpectedUpdateUsageHours = FLIGHT_HOURS.add( FLIGHT_HOURS_UPDATE_DIFF );

      Assert.assertEquals( "Unexpected the aircraft updated usage hours.",
            lExpectedUpdateUsageHours.compareTo( lActualUpdateHours ), 0 );

   }


   /**
    * Verify a system note is generated when the usage delta of a flight is modified.
    *
    * <pre>
    *       Given an aircraft collection usages
    *       When the historical flight is edited
    *        And modified usage delta of a historic flight
    *       Then system note being persisted
    * </pre>
    *
    */
   @Test
   public void itGeneratesASystemNoteWhenTheUsageDeltaOfAFlightIsModified() throws Exception {

      // Given
      final InventoryKey lAircraft = createAircraft();

      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.addUsage( lAircraft, HOURS, FLIGHT_HOURS );
         }
      } );

      // When
      CollectedUsageParm[] lUpdatedUsageParms =
            new CollectedUsageParm[] { new CollectedUsageParm( new UsageParmKey( lAircraft, HOURS ),
                  UPDATED_FLIGHT_HOURS.doubleValue() ) };

      FlightInformationTO lUpdatedFlightTo = new FlightInformationTO( FLIGHT_NAME, null, null, null,
            null, null, iDepartureAirport, iArrivalAirport, null, null, null, null, DEPARTURE_DATE,
            ARRIVAL_DATE, null, null, false, false );

      iFlightHistBean.editHistFlight( lFlight, iHumanResource, lUpdatedFlightTo, lUpdatedUsageParms,
            NO_MEASUREMENTS );

      // Then
      String lExpectedUsageHours = getExpectedUsageDiffString( HOURS, FLIGHT_HOURS_UPDATE_DIFF );
      String lActualUsageHours = getSystemNote( lFlight );

      Assert.assertEquals( "Unexpected the system note being persisted.",
            lActualUsageHours.contains( lExpectedUsageHours ), true );

   }


   @Before
   public void setUp() {

      iDepartureAirport = createLocation();
      iArrivalAirport = createLocation();

      iHumanResource = new HumanResourceDomainBuilder().withUserId( USER_ID ).build();
      UserParameters.setInstance( USER_ID, "LOGIC", new UserParametersFake( USER_ID, "LOGIC" ) );

      iFlightHistBean = new FlightHistBean();
      iFlightHistBean.ejbCreate();
      iFlightHistBean.setSessionContext( new SessionContextFake() );

   }


   @After
   public void tearDown() {
      UserParameters.setInstance( USER_ID, "LOGIC", null );
      iFlightHistBean.setSessionContext( null );;
   }


   private String getExpectedUsageDiffString( DataTypeKey aDataType, BigDecimal aUsageDifference ) {

      String lDataTypeCode = MimDataType.findByPrimaryKey( aDataType ).getDataTypeCd();
      String lSignum = ( aUsageDifference.signum() == 1 ) ? "+" : "";
      return lDataTypeCode + ": " + lSignum + aUsageDifference;
   }


   private LocationKey createLocation() {
      return Domain.createLocation( new DomainConfiguration<Location>() {

         @Override
         public void configure( Location aLocation ) {
            aLocation.setType( RefLocTypeKey.AIRPORT );
         }
      } );
   }


   private InventoryKey createAircraft() {
      return Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, INITIAL_HOURS );
         }
      } );
   }


   private String getUserNote( FlightLegId aFlight ) {
      List<FlightLegStatusLogEntity> lLogEntities =
            new JdbcFlightLegStatusLogDao().findByParent( aFlight );
      assertEquals( "Unexpected number of flight leg status log entities.", 1,
            lLogEntities.size() );

      FlightLegNoteId lUserNoteId = lLogEntities.get( 0 ).getUserNote();
      Assert.assertNotNull( "Expected an user note id to be in the flight leg status log entity.",
            lUserNoteId );

      FlightLegNoteEntity lUserNoteEntity = new JdbcFlightLegNoteDao().findById( lUserNoteId );
      String lUserNote = lUserNoteEntity.getEntryNote();

      return lUserNote;
   }


   private String getSystemNote( FlightLegId aFlight ) {
      List<FlightLegStatusLogEntity> lLogEntities =
            new JdbcFlightLegStatusLogDao().findByParent( aFlight );
      assertEquals( "Unexpected number of flight leg status log entities.", 1,
            lLogEntities.size() );

      FlightLegNoteId lSystemNoteId = lLogEntities.get( 0 ).getSystemNote();
      Assert.assertNotNull( "Expected a system note id to be in the flight leg status log entity.",
            lSystemNoteId );

      FlightLegNoteEntity lSystemNoteEntity = new JdbcFlightLegNoteDao().findById( lSystemNoteId );
      String lSystemNote = lSystemNoteEntity.getEntryNote();

      return lSystemNote;
   }

}
