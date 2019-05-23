package com.mxi.mx.core.ejb.flighthist.flighthistbean.edithistflight;

import static com.mxi.mx.core.key.DataTypeKey.CYCLES;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.AircraftAssembly;
import com.mxi.am.domain.CalculatedUsageParameter;
import com.mxi.am.domain.ConfigurationSlot;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Engine;
import com.mxi.am.domain.EngineAssembly;
import com.mxi.am.domain.Flight;
import com.mxi.am.domain.Location;
import com.mxi.am.domain.RemovalRecord;
import com.mxi.am.domain.TrackedInventory;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.ejb.flighthist.FlightHistBean;
import com.mxi.mx.core.flight.model.FlightLegId;
import com.mxi.mx.core.key.AssemblyKey;
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
import com.mxi.mx.core.table.inv.InvCurrUsage;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.mim.MimDataType;
import com.mxi.mx.core.table.org.OrgHr;


/**
 * Tests for {@linkplain FlightHistBean#editHistFlight} that involve updating detached components
 * calculated parameters within current usage
 */
public class EditHistFlight_AffectCalcParms_OnDetachedComponentsCurrentUsage {

   private static final String HR_USERNAME = "HR_USERNAME";

   private static final String CALC_PARM_CD = "CALC_PARM_CD";
   private static final String CALC_PARM_FUNCTION_NAME = "CALC_PARM_FUNCTION_NAME";
   private static final String CALC_PARM_CONSTANT_NAME = "CALC_PARM_CONSTANT_NAME";
   private static final BigDecimal CALC_PARM_CONSTANT = new BigDecimal( 2 );

   private static final Date CONFIGURATION_CHANGE_DATE = DateUtils.addDays( new Date(), -1 );

   private static final Date FLIGHT_DATE = DateUtils.addDays( new Date(), -2 );

   private static final String FLIGHT_NAME = "FLIGHT_NAME";

   private static final Measurement[] NO_MEASUREMENTS = new Measurement[] {};

   private FlightHistBean iFlightHistBean;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Verify that all usage parameters and calculated parameters of a component's current usage are
    * adjusted when a flight with an arrival time prior to when the component was detached from the
    * flight's aircraft is edited.
    *
    * Given an aircraft assembly and an aircraft based on this assembly. <br />
    * And an engine on this aircraft tracking CYCLES and a calc parms based on CYCLES. <br />
    * And a loose tracked component that was previously attached to the engine. <br />
    * And the tracked component is tracking the same parameters as the engine. <br />
    * And a historical flight before the component was detached.
    *
    * When the flight is edited and usages are modified.
    *
    * Then verify on the component current CYCLES is the sum of original current usage plus the
    * historical flight CYCLES usage delta and calc parm is recalculated based on the new CYCLES
    * current usage.
    */
   @Test
   public void itUpdatesUsageParmsAndCalcParmsOnDetachedComponents() throws Exception {

      addEquationFunctionToDatabase();
      HumanResourceKey lHrKey = createHumanResource();

      final AssemblyKey lAircraftAssembly = createAircraftAssembly();

      final AssemblyKey lEngineAssembly = createEngineAssembly();

      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aBuilder ) {
            aBuilder.setAssembly( lEngineAssembly );
            aBuilder.addUsage( CYCLES, BigDecimal.ONE );
            aBuilder.addUsage( MimDataType.getDataType( CALC_PARM_CD ), new BigDecimal( 2 ) );

         }

      } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.addEngine( lEngine );
            aBuilder.setAssembly( lAircraftAssembly );
            aBuilder.addUsage( CYCLES, BigDecimal.ONE );
            aBuilder.addUsage( MimDataType.getDataType( CALC_PARM_CD ), new BigDecimal( 2 ) );

         }

      } );

      final InventoryKey lTrackedComponent =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aBuilder ) {
                  aBuilder.addUsage( CYCLES, BigDecimal.ONE );
                  aBuilder.addUsage( MimDataType.getDataType( CALC_PARM_CD ), new BigDecimal( 2 ) );
                  aBuilder.setParent( lEngine );
                  aBuilder.addRemovalRecord( new DomainConfiguration<RemovalRecord>() {

                     @Override
                     public void configure( RemovalRecord aBuilder ) {
                        aBuilder.setParent( lEngine );
                        aBuilder.setHighest( lEngine );
                        aBuilder.setRemovalDate( CONFIGURATION_CHANGE_DATE );
                     }

                  } );

               }

            } );

      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aBuilder ) {
            aBuilder.setName( FLIGHT_NAME );
            aBuilder.setAircraft( lAircraft );
            aBuilder.setArrivalDate( FLIGHT_DATE );
            aBuilder.setHistorical( true );
            aBuilder.addUsage( lAircraft, CYCLES, BigDecimal.ONE, null );
            aBuilder.addUsage( lEngine, CYCLES, BigDecimal.ONE, null );
         }
      } );

      CollectedUsageParm[] lModifiedUsageParms =
            { generateFlightUsage( lAircraft, CYCLES, BigDecimal.TEN ),
                  generateFlightUsage( lEngine, CYCLES, BigDecimal.TEN ) };

      iFlightHistBean.editHistFlight( lFlight, lHrKey,
            generateFlightInfoTO( FLIGHT_NAME, FLIGHT_DATE, FLIGHT_DATE ), lModifiedUsageParms,
            NO_MEASUREMENTS );

      InvCurrUsage[] lInvCurrUsages = InvCurrUsage.findByInventory( lTrackedComponent );

      assertEquals( lInvCurrUsages.length, 2 );

      for ( InvCurrUsage lInvCurrUsage : lInvCurrUsages ) {
         if ( lInvCurrUsage.getDataType().equals( CYCLES ) ) {
            assertEquals( new BigDecimal( lInvCurrUsage.getTsnQt() ), BigDecimal.TEN );

            assertEquals( new BigDecimal( lInvCurrUsage.getTsiQt() ), BigDecimal.TEN );

            assertEquals( new BigDecimal( lInvCurrUsage.getTsoQt() ), BigDecimal.TEN );
         }

         else {

            assertEquals( new BigDecimal( lInvCurrUsage.getTsnQt() ), new BigDecimal( 20 ) );

            assertEquals( new BigDecimal( lInvCurrUsage.getTsiQt() ), new BigDecimal( 20 ) );

            assertEquals( new BigDecimal( lInvCurrUsage.getTsoQt() ), new BigDecimal( 20 ) );
         }
      }

      {
         dropEquationFunctionToDatabase();
      }

   }


   @Before
   public void setup() {
      iFlightHistBean = new FlightHistBean();
      iFlightHistBean.ejbCreate();
      iFlightHistBean.setSessionContext( new SessionContextFake() );
   }


   @After
   public void teardown() {
      iFlightHistBean.setSessionContext( null );
   }


   private AssemblyKey createAircraftAssembly() {
      return Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

         @Override
         public void configure( AircraftAssembly aBuilder ) {

            aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

               @Override
               public void configure( ConfigurationSlot aBuilder ) {
                  aBuilder.addUsageParameter( CYCLES );
               }

            } );

         }

      } );

   }


   private AssemblyKey createEngineAssembly() {
      return Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

         @Override
         public void configure( EngineAssembly aBuilder ) {

            aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

               @Override
               public void configure( ConfigurationSlot aBuilder ) {
                  aBuilder.addCalculatedUsageParameter(
                        new DomainConfiguration<CalculatedUsageParameter>() {

                           @Override
                           public void configure( CalculatedUsageParameter aBuilder ) {
                              aBuilder.setCode( CALC_PARM_CD );
                              aBuilder.setDatabaseCalculation( CALC_PARM_FUNCTION_NAME );
                              aBuilder.setPrecisionQt( 0 );
                              aBuilder.addConstant( CALC_PARM_CONSTANT_NAME, CALC_PARM_CONSTANT );
                              aBuilder.addParameter( CYCLES );

                           }

                        } );

               }

            } );
         }

      } );
   }


   private FlightInformationTO generateFlightInfoTO( String aFlightName, Date aActualDepartureDate,
         Date aActualArrivalDate ) {
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


   private HumanResourceKey createHumanResource() {
      HumanResourceKey lHrKey = new HumanResourceDomainBuilder().withUsername( HR_USERNAME ).build();

      int lUserId = OrgHr.findByPrimaryKey( lHrKey ).getUserId();
      UserParameters.setInstance( lUserId, "LOGIC", new UserParametersFake( lUserId, "LOGIC" ) );

      return lHrKey;
   }


   private void addEquationFunctionToDatabase() throws SQLException {

      // Function creation is DDL which implicitly commits the transaction.
      // We perform explicit roll-back before function creation ensuring no data gets committed
      // accidentally.
      String lCreateFunctionStatement = "CREATE OR REPLACE FUNCTION " + CALC_PARM_FUNCTION_NAME
            + " (" + "aConstant NUMBER, aHoursInput NUMBER" + " )" + " RETURN NUMBER" + " " + "IS "
            + "result NUMBER; " + "BEGIN" + " " + "result := aConstant * aHoursInput ; " + "RETURN"
            + " " + " result;" + "END" + " " + CALC_PARM_FUNCTION_NAME + " ;";

      Domain.createCalculatedParameterEquationFunction( iDatabaseConnectionRule.getConnection(),
            lCreateFunctionStatement );
   }


   private void dropEquationFunctionToDatabase() throws SQLException {

      // Function dropping is DDL which implicitly commits transaction.
      // We perform explicit rollback before function drop ensuring no data gets committed
      // accidentally.
      Domain.dropCalculatedParameterEquationFunction( iDatabaseConnectionRule.getConnection(),
            CALC_PARM_FUNCTION_NAME );
   }
}
