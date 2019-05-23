package com.mxi.mx.core.ejb.flighthist.flighthistbean.edithistflight;

import static com.mxi.mx.core.key.DataTypeKey.CYCLES;
import static com.mxi.mx.core.key.DataTypeKey.HOURS;
import static com.mxi.mx.core.key.RefDomainTypeKey.USAGE_PARM;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.AircraftAssembly;
import com.mxi.am.domain.ConfigurationSlot;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Engine;
import com.mxi.am.domain.EngineAssembly;
import com.mxi.am.domain.Flight;
import com.mxi.am.domain.Location;
import com.mxi.am.domain.Part;
import com.mxi.am.domain.PartGroup;
import com.mxi.am.domain.UsageAdjustment;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.ejb.flighthist.FlightHistBean;
import com.mxi.mx.core.flight.model.FlightLegId;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.MimPartNumDataKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefDataSourceKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.UsageDefinitionKey;
import com.mxi.mx.core.key.UsageParmKey;
import com.mxi.mx.core.services.event.usage.CollectedUsageParm;
import com.mxi.mx.core.services.flighthist.FlightInformationTO;
import com.mxi.mx.core.services.flighthist.Measurement;
import com.mxi.mx.core.table.eqp.EqpDataSourceSpec;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.mim.MimDataType;
import com.mxi.mx.core.table.mim.MimPartNumData;
import com.mxi.mx.core.table.org.OrgHr;
import com.mxi.mx.core.usage.model.UsageAdjustmentId;


/**
 * This class is to test usage adjust creation including accumulated parm when editing a flight
 */
public class EditHistFlightAdjustAccumulatedParmTest {

   private static final String LOW_THRUSTING_CYCLE = "LOW_THRUSTING_CYCLE";
   private static final String HIGH_THRUSTING_CYCLE = "HIGH_THRUSTING_CYCLE";
   private static final String LOW_THRUSTING_HOURS = "LOW_THRUSTING_HOURS";
   private static final String HIGH_THRUSTING_HOURS = "HIGH_THRUSTING_HOURS";
   private static final String FLIGHT_NAME = "FLIGHT_NAME";

   private static final Measurement[] NO_MEASUREMENTS = new Measurement[] {};

   private static final BigDecimal FLIGHT_CYCLES = new BigDecimal( 1 );
   private static final BigDecimal EDITED_FLIGHT_CYCLES = new BigDecimal( 2 );
   final static Date NEW_DATE = new Date();
   final static Date IPN_DATE = DateUtils.addDays( NEW_DATE, -1 );

   private static final String HR_USERNAME = "HR_USERNAME";
   private FlightHistBean iFlightHistBean;
   private HumanResourceKey iHrKey;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Verifies that when a historic flight is edited and a usage parameter is modified that the
    * corresponding part specific usage parameter is also modified. The corresponding part specific
    * usage parameter is determined by the part that was installed at the time of the historic
    * flight. <br />
    *
    * Given two aggregated usage parameters on an engine assembly<br />
    * And two part specific usage parameters created per aggregated usage parameters <br />
    * And two part numbers part number No.1 and part number No.2 of the engine represents different
    * kinds of thrusting with assembly part specific usage parameters respectively<br />
    * And a historical flight with Engine in part number No.1 <br />
    * When edit this flight by changing the usage parameter <br />
    * Then the part specific usage parameter in the flight usage record of the engine is adjusted by
    * changing only part number No.1 part specific usage parameters.
    */
   @Test
   public void itAdjustUsageParameterBasedOnCurrentPartNoInUsageRecord() throws Exception {
      final PartNoKey lLowThrustingPartNo = Domain.createPart( new DomainConfiguration<Part>() {

         @Override
         public void configure( Part aPartNo ) {
            aPartNo.setInventoryClass( RefInvClassKey.ASSY );

         }

      } );

      final PartNoKey lHighThrustingPartNo = Domain.createPart( new DomainConfiguration<Part>() {

         @Override
         public void configure( Part aPartNo ) {
            aPartNo.setInventoryClass( RefInvClassKey.ASSY );

         }

      } );

      final AssemblyKey lEngineAssembly =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aEngineAssembly ) {

                  aEngineAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aConfigSlot ) {
                              aConfigSlot.addUsageParameter( CYCLES );
                              aConfigSlot.addUsageParameter( HOURS );
                              aConfigSlot.addPartGroup( new DomainConfiguration<PartGroup>() {

                                 @Override
                                 public void configure( PartGroup aPartGroup ) {
                                    aPartGroup.addPart( lLowThrustingPartNo );
                                    aPartGroup.addPart( lHighThrustingPartNo );

                                 }

                              } );

                           }

                        } );

               }

            } );

      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAssembly ) {
                  aAssembly.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aConfigSlot ) {
                        aConfigSlot.addUsageParameter( CYCLES );
                        aConfigSlot.addUsageParameter( HOURS );
                     }

                  } );

               }

            } );

      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.setAssembly( lEngineAssembly );
            aEngine.setPartNumber( lHighThrustingPartNo );

         }

      } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.addEngine( lEngine );

         }

      } );

      final DataTypeKey lLowThrustingCycle = createAccumulatedParm( LOW_THRUSTING_CYCLE,
            lEngineAssembly, lLowThrustingPartNo, CYCLES );

      final DataTypeKey lHighThrustingCycle = createAccumulatedParm( HIGH_THRUSTING_CYCLE,
            lEngineAssembly, lHighThrustingPartNo, CYCLES );

      final DataTypeKey lLowThrustingHours = createAccumulatedParm( LOW_THRUSTING_HOURS,
            lEngineAssembly, lLowThrustingPartNo, HOURS );

      final DataTypeKey lHighThrustingHours = createAccumulatedParm( HIGH_THRUSTING_HOURS,
            lEngineAssembly, lHighThrustingPartNo, HOURS );

      final UsageAdjustmentId lUsageRecord =
            Domain.createUsageAdjustment( new DomainConfiguration<UsageAdjustment>() {

               @Override
               public void configure( UsageAdjustment aUsageAdjustment ) {
                  aUsageAdjustment.setUsageDate( NEW_DATE );
                  aUsageAdjustment.setMainInventory( lAircraft );
                  aUsageAdjustment.addUsage( lAircraft, CYCLES, FLIGHT_CYCLES, BigDecimal.ZERO );
                  aUsageAdjustment.addUsage( lEngine, CYCLES, FLIGHT_CYCLES, BigDecimal.ZERO );
                  aUsageAdjustment.addUsage( lEngine, lHighThrustingCycle, FLIGHT_CYCLES,
                        BigDecimal.ZERO );
               }
            } );

      final FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setName( FLIGHT_NAME );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( NEW_DATE );
            aFlight.setUsageRecord( lUsageRecord );
         }

      } );

      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lAircraft, CYCLES, EDITED_FLIGHT_CYCLES ),
                  generateFlightUsage( lEngine, CYCLES, EDITED_FLIGHT_CYCLES ) };

      FlightInformationTO lFlightInfoTO = generateFlightInfoTO( FLIGHT_NAME, NEW_DATE, NEW_DATE );

      iFlightHistBean.editHistFlight( lFlight, iHrKey, lFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      Map<DataTypeKey, BigDecimal> lDataTypeKeyList = readUsageData( lEngine );

      assertThat( lDataTypeKeyList.get( CYCLES ), equalTo( EDITED_FLIGHT_CYCLES ) );
      assertThat( lDataTypeKeyList.get( lHighThrustingCycle ), equalTo( EDITED_FLIGHT_CYCLES ) );
      assertThat( lDataTypeKeyList.get( lLowThrustingCycle ), nullValue() );
      assertThat( lDataTypeKeyList.get( HOURS ), nullValue() );
      assertThat( lDataTypeKeyList.get( lHighThrustingHours ), nullValue() );
      assertThat( lDataTypeKeyList.get( lLowThrustingHours ), nullValue() );

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
      // CollectedUsageParm lUsageParm =
      new CollectedUsageParm( new UsageParmKey( aInventoryKey, lDataType ), lDelta.doubleValue() );

      CollectedUsageParm lUsageParm = new CollectedUsageParm(
            new UsageParmKey( aInventoryKey, lDataType ), lDelta.doubleValue() );

      // Create flight data source specifications.
      EqpDataSourceSpec
            .create( new UsageDefinitionKey( InvInvTable.getAssemblyByInventoryKey( aInventoryKey ),
                  RefDataSourceKey.MXFL ), lDataType );

      return lUsageParm;
   }


   private DataTypeKey createAccumulatedParm( String aCode, AssemblyKey aAssemblyKey,
         PartNoKey aPartNoKey, DataTypeKey aAggregatedDataType ) {
      DataTypeKey lDataType = MimDataType.create( aCode, null, USAGE_PARM, null, 0, null );
      MimPartNumDataKey lMimPartNumDataKey =
            new MimPartNumDataKey( new ConfigSlotKey( aAssemblyKey, 0 ), lDataType );
      MimPartNumData.create( lMimPartNumDataKey );
      MimPartNumData lMimPartNumData = MimPartNumData.findByPrimaryKey( lMimPartNumDataKey );
      lMimPartNumData.setAssemblyPartNo( aPartNoKey.getDbId(), aPartNoKey.getId() );
      lMimPartNumData.setAggregatedDataType( aAggregatedDataType.getDbId(),
            aAggregatedDataType.getId() );
      lMimPartNumData.update();
      return lDataType;
   }


   private Map<DataTypeKey, BigDecimal> readUsageData( InventoryKey lInventoryKey ) {
      Map<DataTypeKey, BigDecimal> lDataTypeValueMap = new HashMap<>();
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "USG_USAGE_DATA",
            lInventoryKey.getPKWhereArg() );
      while ( lQs.next() ) {
         BigDecimal lDelta = lQs.getBigDecimal( "TSN_DELTA_QT" );
         lDataTypeValueMap.put( lQs.getKey( DataTypeKey.class, "DATA_TYPE_DB_ID", "DATA_TYPE_ID" ),
               lDelta );
      }
      return lDataTypeValueMap;
   }
}
