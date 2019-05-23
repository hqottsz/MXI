package com.mxi.mx.core.services.fault;

import static com.mxi.am.domain.Domain.createLocation;
import static com.mxi.mx.core.key.RefLocTypeKey.AIRPORT;
import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.ConfigSlotBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersStub;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MandatoryArgumentException;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.exception.StringTooLongException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.common.trigger.TriggerFactory;
import com.mxi.mx.common.trigger.TriggerFactoryStub;
import com.mxi.mx.core.flight.dao.FlightLegEntity;
import com.mxi.mx.core.flight.dao.JdbcFlightLegDao;
import com.mxi.mx.core.flight.model.FlightLegId;
import com.mxi.mx.core.flight.model.FlightLegStatus;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventInventoryUsageKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefFailureSeverityKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefRelationTypeKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.services.MxCoreUtils;
import com.mxi.mx.core.services.event.inventory.UsageSnapshot;
import com.mxi.mx.core.table.eqp.EqpAssmblBom;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.EvtInvTable;
import com.mxi.mx.core.table.evt.EvtInvUsageTable;
import com.mxi.mx.core.table.sd.SdFaultTable;
import com.mxi.mx.core.table.utl.UtlUser;
import com.mxi.mx.core.trigger.MxCoreTriggerType;
import com.mxi.mx.core.trigger.ordernumber.MxOrderNumberGenerator;


/**
 * Tests the {@link FaultService} class.
 */
public final class FaultServiceRaiseFaultTest {

   private LocationKey repairLocation;

   private static final double AIRCRAFT_CYCLES = 1000.0;
   private static final double ENGINE_CYCLES = 500.0;
   private static final HumanResourceKey HR = HumanResourceKey.ADMIN;
   private static final double TRK_CYCLES = 100.0;
   private static final double FAULT_CYCLES = 200.0;
   private static final Date TEST_DATE = new Date();
   private static final String CONFIG_SLOT_ATA = "10-20-30";
   private static final String CONFIG_SLOT_NAME = "Resolved System Name";
   private static final String WORK_ORDER_NUMBER = "WORK_ORDER_NUMBER";

   private final Mockery iContext = new Mockery();

   private MxOrderNumberGenerator iOrderNumberGenerator;
   private UserKey iUserId;
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();


   /**
    * Ensure that the usage recorded within a fault against the subsystem of an assembly (that is
    * installed on an aircraft) reflects the usage of that subsystem.
    *
    * @throws Exception
    */
   @Test
   public void testRaiseFaultAndCloseAgainstSubsystemOfAttachedAssembly() throws Exception {

      // Create an aircraft, an engine, a subsystem on the engine, and a TRK component under that
      // subsystem.

      InventoryKey lAircraft = new InventoryBuilder().withClass( RefInvClassKey.ACFT )
            .atLocation( repairLocation ).withCyclesUsage( AIRCRAFT_CYCLES ).build();
      InventoryKey lEngine =
            new InventoryBuilder().withClass( RefInvClassKey.ASSY ).withParentInventory( lAircraft )
                  .withHighestInventory( lAircraft ).withCyclesUsage( ENGINE_CYCLES ).build();
      InventoryKey lSubAssy = new InventoryBuilder().withClass( RefInvClassKey.SUBASSY )
            .withParentInventory( lEngine ).withHighestInventory( lAircraft )
            .withCyclesUsage( ENGINE_CYCLES ).build();
      new InventoryBuilder().withClass( RefInvClassKey.TRK ).withParentInventory( lSubAssy )
            .withHighestInventory( lAircraft ).withCyclesUsage( TRK_CYCLES ).build();

      // Setup a fault usage snapshot for the sub-assembly.
      UsageSnapshot[] lFaultUsages = new UsageSnapshot[1];
      lFaultUsages[0] = new UsageSnapshot( lSubAssy, DataTypeKey.CYCLES, FAULT_CYCLES, FAULT_CYCLES,
            FAULT_CYCLES, null, null );

      // Raise a "LOG_AND_CLOSE" fault agaisnt aircraft with the failed system being the
      // sub-assembly and with the fault usage snapshot.
      RaiseFaultDetailsTO lRaiseFaultDetailsTO = new RaiseFaultDetailsTO();
      lRaiseFaultDetailsTO.setMode( FaultService.LOG_AND_CLOSE );
      lRaiseFaultDetailsTO.setInventory( lAircraft );
      lRaiseFaultDetailsTO.setAircraftOrHighestInv( lAircraft );
      lRaiseFaultDetailsTO.setFailedSystem( lSubAssy );
      lRaiseFaultDetailsTO.setUsages( lFaultUsages );

      lRaiseFaultDetailsTO.setFoundOnDate( TEST_DATE );
      lRaiseFaultDetailsTO.setFaultSeverity( RefFailureSeverityKey.MINOR );

      RaiseFaultCorrectiveActionTO lCorrectiveActionTO = new RaiseFaultCorrectiveActionTO();
      lCorrectiveActionTO.setRepairLocation( repairLocation );
      lRaiseFaultDetailsTO.setRaiseFaultCorrectiveActionTO( lCorrectiveActionTO );

      // Execute the test.
      FaultKey lFault = FaultService.raiseFault( lRaiseFaultDetailsTO, HR );

      // Verify the fault usage was recorded as the "Usage When Found" for the sub-assembly (the
      // failed subsystem).
      assertUsage( lFault.getEventKey(), lSubAssy, FAULT_CYCLES );
   }


   @Test
   public void testRaiseFaultAndClose_setResolutionConfigSlot() throws Exception {

      InventoryKey lAircraft = new InventoryBuilder().withClass( RefInvClassKey.ACFT )
            .atLocation( repairLocation ).build();

      ConfigSlotKey lResolutionConfigSlot = new ConfigSlotBuilder( CONFIG_SLOT_ATA )
            .withClass( RefBOMClassKey.SYS ).withName( CONFIG_SLOT_NAME ).build();

      RaiseFaultDetailsTO lRaiseFaultDetailsTO = createRaiseFaultWithResolutionConfigSlot(
            lAircraft, lResolutionConfigSlot, FaultService.LOG_AND_CLOSE );
      lRaiseFaultDetailsTO.setHistoric( true );

      // Execute the test.
      FaultKey lFault = FaultService.raiseFault( lRaiseFaultDetailsTO, HR );

      SdFaultTable lActualFault = SdFaultTable.findByPrimaryKey( lFault );

      assertEquals( lResolutionConfigSlot, lActualFault.getResolutionConfigSlot() );
   }


   @Test
   public void raiseFault_withOperationalRestrictions() throws MxException, TriggerException {

      String lOperationalRestrictions = "I am a restriction.";
      RaiseFaultDetailsTO lRaiseFaultDetailsTO = new RaiseFaultDetailsTO();
      lRaiseFaultDetailsTO.setOperationalRestrictions( lOperationalRestrictions );
      lRaiseFaultDetailsTO.setFailedSystem( Domain.createAircraft() );

      FaultKey lFault = FaultService.raiseFault( lRaiseFaultDetailsTO, HR );

      SdFaultTable lActualFault = SdFaultTable.findByPrimaryKey( lFault );

      assertEquals( MxCoreUtils.appendString( HR, null, lOperationalRestrictions ),
            lActualFault.getOperationalRestriction() );
   }


   @Test( expected = StringTooLongException.class )
   public void raiseFault_withOperationalRestrictionsOver4000Characters()
         throws MxException, TriggerException {

      String lOver4000CharactersOperationalRestrictions =
            new String( new char[4001] ).replace( '\0', 'a' );
      RaiseFaultDetailsTO lRaiseFaultDetailsTO = new RaiseFaultDetailsTO();
      lRaiseFaultDetailsTO.setOperationalRestrictions( lOver4000CharactersOperationalRestrictions );
      lRaiseFaultDetailsTO.setFailedSystem( Domain.createAircraft() );

      FaultService.raiseFault( lRaiseFaultDetailsTO, HR );
   }


   private RaiseFaultDetailsTO createRaiseFaultWithResolutionConfigSlot( InventoryKey aAircraft,
         ConfigSlotKey aResolutionConfigSlot, String aMode ) throws MandatoryArgumentException {

      InventoryKey lFailedSystemInv = new InventoryBuilder().withClass( RefInvClassKey.SYS )
            .withParentInventory( aAircraft ).withHighestInventory( aAircraft ).build();

      RaiseFaultDetailsTO lRaiseFaultDetailsTO = new RaiseFaultDetailsTO();
      lRaiseFaultDetailsTO.setMode( aMode );
      lRaiseFaultDetailsTO.setInventory( aAircraft );
      lRaiseFaultDetailsTO.setAircraftOrHighestInv( aAircraft );
      lRaiseFaultDetailsTO.setFailedSystem( lFailedSystemInv );
      lRaiseFaultDetailsTO.setFoundOnDate( TEST_DATE );
      lRaiseFaultDetailsTO.setFaultSeverity( RefFailureSeverityKey.MINOR );

      RaiseFaultCorrectiveActionTO lCorrectiveActionTO = new RaiseFaultCorrectiveActionTO();
      lCorrectiveActionTO.setRepairLocation( repairLocation );
      lRaiseFaultDetailsTO.setRaiseFaultCorrectiveActionTO( lCorrectiveActionTO );
      lCorrectiveActionTO.setResolutionConfigSlotId(
            EqpAssmblBom.findByPrimaryKey( aResolutionConfigSlot ).getAlternateKey() );

      return lRaiseFaultDetailsTO;
   }


   /**
    * Ensure that the usage at completion for a work package auto-created within a fault against the
    * subsystem of an assembly (that is installed on an aircraft) reflects the fault usage or the
    * specified flight usage.
    *
    * @throws Exception
    */
   @Test
   public void testUsageAtCompletionForWPWithFaultFoundDuringFlightWhenRaiseFaultAndClose()
         throws Exception {

      FlightLegId lFlight1 = new FlightLegId( "AC3748508436410CB8294D6C9527E7D9" );
      FlightLegId lFlight2 = new FlightLegId( "CC3748508436410CB8294D6C9527E7D1" );

      // Create an aircraft, an engine, a subsystem on the engine, and a TRK component under that
      // subsystem.
      InventoryKey lAircraft = new InventoryBuilder().withClass( RefInvClassKey.ACFT )
            .atLocation( repairLocation ).withCyclesUsage( AIRCRAFT_CYCLES ).build();
      InventoryKey lEngine =
            new InventoryBuilder().withClass( RefInvClassKey.ASSY ).withParentInventory( lAircraft )
                  .withHighestInventory( lAircraft ).withCyclesUsage( ENGINE_CYCLES ).build();
      InventoryKey lSubAssy = new InventoryBuilder().withClass( RefInvClassKey.SUBASSY )
            .withParentInventory( lEngine ).withHighestInventory( lAircraft )
            .withCyclesUsage( ENGINE_CYCLES ).build();
      new InventoryBuilder().withClass( RefInvClassKey.TRK ).withParentInventory( lSubAssy )
            .withHighestInventory( lAircraft ).withCyclesUsage( TRK_CYCLES ).build();

      // Setup a fault usage snapshot for the sub-assembly.
      UsageSnapshot[] lFaultUsages = new UsageSnapshot[1];
      lFaultUsages[0] = new UsageSnapshot( lSubAssy, DataTypeKey.CYCLES, FAULT_CYCLES, FAULT_CYCLES,
            FAULT_CYCLES, null, null );

      // Raise a "LOG_AND_CLOSE" fault against aircraft with the failed system being the
      // sub-assembly and with the fault usage snapshot.
      RaiseFaultDetailsTO lRaiseFaultDetailsTO = new RaiseFaultDetailsTO();
      lRaiseFaultDetailsTO.setMode( FaultService.LOG_AND_CLOSE );
      lRaiseFaultDetailsTO.setInventory( lSubAssy );
      // lRaiseFaultDetailsTO.setAircraftOrHighestInv( lAircraft );
      lRaiseFaultDetailsTO.setFailedSystem( lSubAssy );
      lRaiseFaultDetailsTO.setUsages( lFaultUsages );
      lRaiseFaultDetailsTO.setFoundDuringFlight( lFlight1 );

      lRaiseFaultDetailsTO.setFoundOnDate( TEST_DATE );
      lRaiseFaultDetailsTO.setFaultSeverity( RefFailureSeverityKey.MINOR );

      RaiseFaultCorrectiveActionTO lCorrectiveActionTO = new RaiseFaultCorrectiveActionTO();
      lCorrectiveActionTO.setRepairLocation( repairLocation );
      lRaiseFaultDetailsTO.setRaiseFaultCorrectiveActionTO( lCorrectiveActionTO );

      FlightLegEntity lFlightLegEntity = new FlightLegEntity( lFlight1 );
      lFlightLegEntity.setFlightName( "flight1" );
      lFlightLegEntity.setAircraft( lAircraft );
      lFlightLegEntity.setFlightStatus( FlightLegStatus.MXCMPLT );

      JdbcFlightLegDao lFlightLegDao = new JdbcFlightLegDao();

      lFlightLegDao.persist( lFlightLegEntity );

      lFlightLegEntity = new FlightLegEntity( lFlight2 );
      lFlightLegEntity.setFlightName( "flight2" );
      lFlightLegEntity.setAircraft( lAircraft );
      lFlightLegEntity.setFlightStatus( FlightLegStatus.MXCMPLT );

      lFlightLegDao.persist( lFlightLegEntity );

      // Execute the test.
      FaultKey lFault = FaultService.raiseFault( lRaiseFaultDetailsTO, HR );

      // Verify that the usage at completion was recorded properly for the auto generated CHECK.
      EventKey lCheck = getCheckContainingFault( lFault );
      assertUsage( lCheck, lAircraft, FAULT_CYCLES );
   }


   /**
    * {@inheritDoc}
    */
   @Before
   public void loadData() throws Exception {
      // Create a mocked order number generator and add it to the trigger factory.
      iOrderNumberGenerator = iContext.mock( MxOrderNumberGenerator.class );

      Map<String, Object> lTriggerMap = new HashMap<String, Object>( 1 );
      lTriggerMap.put( MxCoreTriggerType.WO_NUM_GEN.toString(), iOrderNumberGenerator );

      TriggerFactory lTriggerFactoryStub = new TriggerFactoryStub( lTriggerMap );
      TriggerFactory.setInstance( lTriggerFactoryStub );

      iUserId = UtlUser.findByForeignKey( HR ).getPk();

      UserParameters lUserLogicParameters = new UserParametersStub( iUserId.getId(), "LOGIC" );
      lUserLogicParameters.setBoolean( "ALLOW_ASSIGN_LRP_ITEM_FOR_INWORK_WP", false );
      UserParameters.setInstance( iUserId.getId(), "LOGIC", lUserLogicParameters );

      // Create a test repair location in the DB . (must be either LINE or TRACK according to the
      // utl_config_parm recored for SCHEDULE_LOCATIONS_CHECK)
      repairLocation = createRepairLocation();

      // Mock the Order Number Generator to return a value.
      iContext.checking( new Expectations() {

         {
            one( iOrderNumberGenerator ).getWorkOrderNumber( with( any( TaskKey.class ) ) );
            will( returnValue( WORK_ORDER_NUMBER ) );
         }
      } );
   }


   /**
    * Verify the event usage (for data type of CYCLES) for the provided event and inventory.
    *
    * @param aEvent
    * @param aInventory
    * @param aExpectedQuantity
    */
   private void assertUsage( EventKey aEvent, InventoryKey aInventory, double aExpectedQuantity ) {

      EvtInvTable lEvtInvTable = EvtInvTable.findByEventAndInventory( aEvent, aInventory );

      EvtInvUsageTable lEvtInvUsageTable = EvtInvUsageTable.findByPrimaryKey(
            new EventInventoryUsageKey( lEvtInvTable.getPk(), DataTypeKey.CYCLES ) );

      assertEquals( aExpectedQuantity, lEvtInvUsageTable.getTsnQt(), 0f );
      assertEquals( aExpectedQuantity, lEvtInvUsageTable.getTsoQt(), 0f );
      assertEquals( aExpectedQuantity, lEvtInvUsageTable.getTsiQt(), 0f );
   }


   /**
    * Given a fault key this returns the check (a.k.a. work package) to which it is assigned.
    *
    * @param aFault
    *           the fault key
    *
    * @return event key for the check containing the fault
    */
   private EventKey getCheckContainingFault( FaultKey aFault ) {

      // Get the correction task associated to the fault.
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aFault, "event_db_id", "event_id" );
      lArgs.add( RefRelationTypeKey.CORRECT, "rel_type_db_id", "rel_type_cd" );

      QuerySet lQuerySet =
            QuerySetFactory.getInstance().executeQueryTable( "evt_event_rel", lArgs );
      lQuerySet.first();

      EventKey lCorrectionTask =
            lQuerySet.getKey( EventKey.class, "rel_event_db_id", "rel_event_id" );

      // Return the Check which the correction task is assigned (task's highest event).
      return EvtEventTable.findByPrimaryKey( lCorrectionTask ).getHEvent();
   }


   private LocationKey createRepairLocation() {
      LocationKey airportLocation = createLocation( airportLoc -> {
         airportLoc.setType( AIRPORT );
         airportLoc.setIsSupplyLocation( true );
      } );

      return createLocation( repairLoc -> {
         repairLoc.setCode( "Repair Location" );
         repairLoc.setType( RefLocTypeKey.LINE );
         repairLoc.setParent( airportLocation );
         repairLoc.setSupplyLocation( airportLocation );
      } );
   }

}
