package com.mxi.mx.core.ejb.flighthist.flighthistbean;

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
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.ejb.flighthist.CurrentUsages;
import com.mxi.mx.core.ejb.flighthist.FlightHistBean;
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
import com.mxi.mx.core.services.inventory.exception.LockedInventoryException;
import com.mxi.mx.core.table.eqp.EqpDataSourceSpec;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.org.OrgHr;
import com.mxi.mx.core.usage.dao.JdbcUsageRecordDao;
import com.mxi.mx.core.usage.dao.UsageRecordDao;
import com.mxi.mx.core.usage.dao.UsageRecordEntity;


/**
 * Tests for {@linkplain FlightHistBean}
 *
 * Note: some of the tests that exercise {@linkplain FlightHistBean#editHistFlight} have been moved
 * into classes within the package com.mxi.mx.core.ejb.flighthist.flighthistbean.edithistflight
 *
 */

public class EditHistFlightTest {

   private static final String HR_USERNAME = "HR_USERNAME";
   private static final String FLIGHT = "FLIGHT";
   private static final Measurement[] NO_MEASUREMENTS = new Measurement[] {};

   // current cycle and hour values
   private static final BigDecimal INITIAL_CYCLES = new BigDecimal( 50 );
   private static final BigDecimal INITIAL_HOURS = new BigDecimal( 100 );

   // some deltas
   private static final BigDecimal INITIAL_FLIGHT_DELTA = new BigDecimal( 10 );
   private static final BigDecimal DELTA_VALUE_TWO = new BigDecimal( 2 );

   // the bean under test
   private FlightHistBean iFlightHistBean;

   private HumanResourceKey iHrKey;
   private FlightInformationTO iLatestFlightInfoTO;
   private FlightInformationTO iFirstFlightInfoTO;
   private InventoryKey iEngineInvKey;
   private InventoryKey iAircraftWithOneEngineInvKey;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    *
    * <pre>
    * Given an aircraft having one engine and 2 historical flights
    * When the first historical flight's delta cycles and hours usage value is edited and provide null value
    * Then An exception should be thrown.
    * </pre>
    *
    */
   @Test( expected = NullPointerException.class )
   public void itDoesNotAllowBlankDeltaUsagesWhenFlightEdited() throws Exception {

      // create 2 flights each with usage value
      // Get a reference to the Leg IDs of the flights
      FlightLegId[] lLegIds = createFlightsForAircraftWithOneEngineInv();

      // Change the usage by editing first of the 2 flights
      // We provide delta for engine as edit flight requires us to send usage params for both
      // engine and aircraft.
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( iAircraftWithOneEngineInvKey, CYCLES, null ),
                  generateFlightUsage( iAircraftWithOneEngineInvKey, HOURS, null ),
                  generateFlightUsage( iEngineInvKey, CYCLES, null ),
                  generateFlightUsage( iEngineInvKey, HOURS, null ) };

      // Expect an exception to be thrown here
      iFlightHistBean.editHistFlight( lLegIds[0], iHrKey, iFirstFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

   }


   /**
    *
    * <pre>
    * Given a locked aircraft
    * When the first historical flight is attempted to edit
    * Then Locked Inventory exception is thrown.
    * </pre>
    *
    */
   @Test( expected = LockedInventoryException.class )
   public void itDoesNotAllowEditingHistoricalFlightWhenAircraftLocked() throws Exception {

      // create 2 flights each with usage value
      // Get a reference to the Leg IDs of the flights
      FlightLegId[] lLegIds = createFlightsForAircraftWithOneEngineInv();

      // lock aircraft
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iAircraftWithOneEngineInvKey );
      lInvInv.setLockedBool( true );
      lInvInv.update();

      // Change the usage by editing first of the 2 flights
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( iAircraftWithOneEngineInvKey, CYCLES, DELTA_VALUE_TWO ),
                  generateFlightUsage( iAircraftWithOneEngineInvKey, HOURS, DELTA_VALUE_TWO ) };
      iFlightHistBean.editHistFlight( lLegIds[0], iHrKey, iFirstFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );
   }


   @Test
   public void itMaintainsSameUsageDateForUpdatedActualArrivalDate() throws Exception {
      // create 2 flights each with usage value
      // Will use only one of them for this test
      FlightLegId[] lLegIds = createFlightsForAircraftWithOneEngineInv();
      // Changing Actual Arrival Date
      Date newDate = DateUtils.addHours( iFirstFlightInfoTO.getActualArrivalDate(), -1 );
      iFirstFlightInfoTO.setActualArrivalDate( newDate );
      CollectedUsageParm[] lEditUsageParms = {};
      // Edit the flight with updated Actual Arrival Date
      iFlightHistBean.editHistFlight( lLegIds[0], iHrKey, iFirstFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );
      FlightLegDao lFlightLegDao = new JdbcFlightLegDao();
      FlightLegEntity flightLegEntity = lFlightLegDao.findById( lLegIds[0] );

      UsageRecordDao lUsageRecordDao = new JdbcUsageRecordDao();
      UsageRecordEntity lUsageRecordEntity =
            lUsageRecordDao.findById( flightLegEntity.getUsageRecord() );
      Assert.assertEquals( newDate.toString(), lUsageRecordEntity.getUsageDate().toString() );
   }


   @Before
   public void setup() {
      iHrKey = new HumanResourceDomainBuilder().withUsername( HR_USERNAME ).build();

      int lUserId = OrgHr.findByPrimaryKey( iHrKey ).getUserId();
      UserParameters.setInstance( lUserId, "LOGIC", new UserParametersFake( lUserId, "LOGIC" ) );

      // create an Engine assembly with current usage
      iEngineInvKey = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.addUsage( HOURS, INITIAL_HOURS );
            aEngine.addUsage( CYCLES, INITIAL_CYCLES );

         }
      } );

      // create an Engine assembly with current usage
      Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.addUsage( HOURS, INITIAL_HOURS );
            aEngine.addUsage( CYCLES, INITIAL_CYCLES );

         }
      } );

      // create aircraft with current usage
      Domain.createAircraft( new DomainConfiguration<Aircraft>() {

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
         FlightInformationTO aFlightInformationTO, BigDecimal aDelta ) throws Exception {

      CollectedUsageParm[] lFlightUsageParms =
            { generateFlightUsage( aAircraftInvKey, CYCLES, aDelta ),
                  generateFlightUsage( aAircraftInvKey, HOURS, aDelta ) };

      return iFlightHistBean.createHistFlight( new AircraftKey( aAircraftInvKey ), iHrKey,
            aFlightInformationTO, lFlightUsageParms, NO_MEASUREMENTS );
   }


   private FlightLegId[] createFlightsForAircraftWithOneEngineInv() throws Exception {

      FlightLegId[] lLegIds = {
            createFlight( iAircraftWithOneEngineInvKey, iFirstFlightInfoTO, INITIAL_FLIGHT_DELTA ),
            createFlight( iAircraftWithOneEngineInvKey, iLatestFlightInfoTO,
                  INITIAL_FLIGHT_DELTA ) };

      // Expected cycles would be the sum of the current cycles usage and the usage of the 2 flights
      // created above
      final BigDecimal lExpectedCyclesBeforeEdit =
            INITIAL_CYCLES.add( INITIAL_FLIGHT_DELTA ).add( INITIAL_FLIGHT_DELTA );
      final BigDecimal lExpectedHoursBeforeEdit =
            INITIAL_HOURS.add( INITIAL_FLIGHT_DELTA ).add( INITIAL_FLIGHT_DELTA );
      assertCurrentUsages( iAircraftWithOneEngineInvKey, CYCLES, lExpectedCyclesBeforeEdit );
      assertCurrentUsages( iAircraftWithOneEngineInvKey, HOURS, lExpectedHoursBeforeEdit );

      return lLegIds;
   }


   private CollectedUsageParm generateFlightUsage( InventoryKey aInventoryKey,
         DataTypeKey lDataType, BigDecimal lDelta ) {

      // Create a usage collection to be returned.
      CollectedUsageParm lUsageParm = new CollectedUsageParm(
            new UsageParmKey( aInventoryKey, lDataType ), lDelta.doubleValue() );

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


   private void assertCurrentUsages( InventoryKey aInventory, DataTypeKey aDataType,
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
