package com.mxi.mx.core.services.flightfl.hist;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersStub;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersStub;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefDataSourceKey;
import com.mxi.mx.core.key.UsageDefinitionKey;
import com.mxi.mx.core.key.UsageParmKey;
import com.mxi.mx.core.services.event.usage.CollectedUsageParm;
import com.mxi.mx.core.services.flighthist.FlightHistService;
import com.mxi.mx.core.services.flighthist.FlightInformationTO;
import com.mxi.mx.core.services.flighthist.Measurement;
import com.mxi.mx.core.table.eqp.EqpDataSourceSpec;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.org.OrgHr;
import com.mxi.mx.core.usage.service.UsageDelta;
import com.mxi.mx.core.usage.service.UsageUtils;


/**
 * This is a unit test to test creating history flight.
 *
 * @author Libin Cai
 * @created April 10, 2017
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class CreateHistoryFlightTest {

   /** Data types */
   private static final DataTypeKey DATA_TYPE_CYCLES = DataTypeKey.CYCLES;

   /** Aircraft and engine info */
   private static final BigDecimal ACFT_CURRENT_CYCLE_USAGE = new BigDecimal( 300.0 );

   private CollectedUsageParm[] iUsagParms;
   private InventoryKey iAircraft;
   private LocationKey iDepartureLocation;
   private LocationKey iArrivalLocation;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    * This test is to verify the usages are correct when two users create two flights on the same
    * aircraft.
    *
    * Scenario:
    *
    * -- On a Aircraft, User A opened Create Flight page and populated the usage table.<br>
    * -- On the same Aircraft, User B opened Create Flight page and populated the usage table with
    * the same usage values as User A's page.<br>
    * -- User B's flight arrival date is earlier than User A's flight arrival date.<br>
    * -- User A clicked OK and saved the flight.<br>
    * -- User B clicked OK and saved the flight.<br>
    *
    * Verify:
    *
    * -- 1. User B's flight is before User A's flight by checking its usage. -- 2. User A's flight
    * usage is increased by User B's tsn delta.
    */
   @Test
   public void testTwoUsersCreateTwoFlightsOnTheSameAircraftAtTheSameTime() throws Exception {

      GlobalParametersStub lGlobalParams = new GlobalParametersStub( "LOGIC" );
      lGlobalParams.setBoolean( "SPEC2000_UPPERCASE_ASSMBL_CD", false );
      GlobalParameters.setInstance( lGlobalParams );

      iDepartureLocation = Domain.createLocation();
      iArrivalLocation = Domain.createLocation();

      createAnAircraftWithCycles();

      iUsagParms = new CollectedUsageParm[] {
            new CollectedUsageParm( new UsageParmKey( iAircraft, DATA_TYPE_CYCLES ),
                  ACFT_CURRENT_CYCLE_USAGE.doubleValue(), 0, 0, 1 ) };

      int lDayOfMonth = 28;

      // User A creates a historic flight with parameter iUsagParms
      HumanResourceKey lUserA = createUser();
      createHistoricFlight( lUserA, lDayOfMonth );

      QuerySet lQs = getUsage();

      assertEquals( 1, lQs.getRowCount() );

      lQs.next();
      assertUsageIncreasedBy( lQs, 1.0 );

      String lUsageDataId = lQs.getString( "usage_data_id" );

      // User B creates another historic flight with the same parameter iUsagParms one day before
      // the User A's flight
      HumanResourceKey lUserB = createUser();
      createHistoricFlight( lUserB, lDayOfMonth - 1 );

      lQs = getUsage();

      assertEquals( 2, lQs.getRowCount() );

      while ( lQs.next() ) {

         // Assert UserA's flight usage increased 1 after UserB created a fligh.
         if ( lUsageDataId.equals( lQs.getString( "usage_data_id" ) ) ) {
            assertUsageIncreasedBy( lQs, 2.0 );;
         }
         // Assert UserB's flight usage is inserted before UserA's flight by checking is TSN value.
         else {
            assertUsageIncreasedBy( lQs, 1.0 );
         }
      }

   }


   /**
    * Get usage.
    *
    * @return the usage data set
    */
   private QuerySet getUsage() {

      DataSetArgument lArgs = iAircraft.getPKWhereArg();
      lArgs.add( DATA_TYPE_CYCLES.getPKWhereArg() );

      return QuerySetFactory.getInstance().executeQuery( "usg_usage_data", lArgs, "usage_data_id",
            "tsn_qt" );

   }


   /**
    * Assert the usage is increased by the expected value.
    *
    * @param lQs
    *           the dataset
    * @param aIncreasedValue
    *           the expected value
    */
   private void assertUsageIncreasedBy( QuerySet lQs, double aIncreasedValue ) {

      assertEquals( ACFT_CURRENT_CYCLE_USAGE.doubleValue() + aIncreasedValue,
            lQs.getDouble( "tsn_qt" ), 0 );
   }


   /**
    * Create historic flight.
    *
    * @param aUser
    *           the user
    * @param aDayOfMonth
    *           the date
    *
    * @throws MxException
    *            if a mx error occurs
    * @throws TriggerException
    *            if a trigger error occurs
    */
   private void createHistoricFlight( HumanResourceKey aUser, int aDayOfMonth )
         throws MxException, TriggerException {

      Date lDepartureDate =
            new GregorianCalendar( 2004, GregorianCalendar.MAY, aDayOfMonth ).getTime();

      Date lArrivalDate = DateUtils.addHours( lDepartureDate, 3 );

      FlightInformationTO lFlightInfoTO = new FlightInformationTO( "Historical Flight",
            "Test Description", "Core Unittest Master Flight No", null,
            "Core Unittest Log Reference", "FLVALID", iDepartureLocation, iArrivalLocation,
            "DepGate", "ArrGate", lDepartureDate, lArrivalDate, lDepartureDate, lArrivalDate,
            lDepartureDate, lArrivalDate, false, true );

      new FlightHistService().createHistFlight( new AircraftKey( iAircraft ), aUser, lFlightInfoTO,
            new UsageUtils().convertToUsageDelta( iUsagParms ).toArray( new UsageDelta[0] ),
            new Measurement[0] );
   }


   /**
    * Create a user.
    *
    * @return the user hr key
    */
   private HumanResourceKey createUser() {

      HumanResourceKey lUser = new HumanResourceDomainBuilder().build();

      int lUserId = OrgHr.findByPrimaryKey( lUser ).getUserId();

      UserParametersStub lUserParams = new UserParametersStub( lUserId, "LOGIC" );
      lUserParams.setBoolean( "ALLOW_COMPLETE_FUTURE_FLIGHT", false );
      UserParameters.setInstance( lUserId, "LOGIC", lUserParams );

      return lUser;
   }


   private void createAnAircraftWithCycles() {

      // Create an aircraft
      iAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( DATA_TYPE_CYCLES, ACFT_CURRENT_CYCLE_USAGE );
         }
      } );

      // Create flight data source specifications
      EqpDataSourceSpec
            .create( new UsageDefinitionKey( InvInvTable.getAssemblyByInventoryKey( iAircraft ),
                  RefDataSourceKey.MXFL ), DATA_TYPE_CYCLES );

   }

}
