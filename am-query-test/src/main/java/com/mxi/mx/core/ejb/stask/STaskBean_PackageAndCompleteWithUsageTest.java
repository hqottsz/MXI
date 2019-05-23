package com.mxi.mx.core.ejb.stask;

import static com.mxi.mx.core.key.DataTypeKey.HOURS;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.math.BigDecimal;
import java.util.Date;

import javax.transaction.UserTransaction;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersStub;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventInventoryKey;
import com.mxi.mx.core.key.EventInventoryUsageKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefUsgSnapshotSrcTypeKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.event.inventory.UsageSnapshot;
import com.mxi.mx.core.services.stask.creation.CreateQuickCheckTO;
import com.mxi.mx.core.services.stask.status.ScheduleInternalTO;
import com.mxi.mx.core.table.evt.EvtInvUsageTable;
import com.mxi.mx.core.table.org.OrgHr;
import com.mxi.mx.core.table.sched.SchedStaskDao;


/**
 * Verifies the behavior of
 * {@link STaskBean#packageAndCompleteWithUsage(TaskKey[], com.mxi.mx.core.services.stask.creation.CreateQuickCheckTO, HumanResourceKey, UsageSnapshot[], boolean, javax.transaction.UserTransaction) }
 *
 */
public class STaskBean_PackageAndCompleteWithUsageTest {

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();

   private HumanResourceKey authHr;

   private static final String LINE_LOCATION_CD = "YYZ/LINE";
   private UserTransaction userTransaction = mock( UserTransaction.class );

   private SchedStaskDao schedStaskDao;


   @Before
   public void setup() {
      authHr = Domain.createHumanResource();
      int userId = OrgHr.findByPrimaryKey( authHr ).getUserId();
      UserParametersStub lUserParametersStub = new UserParametersStub( userId, "LOGIC" );
      lUserParametersStub.setBoolean( "ALLOW_ASSIGN_LRP_ITEM_FOR_INWORK_WP", false );
      UserParameters.setInstance( userId, "LOGIC", lUserParametersStub );
      schedStaskDao = InjectorContainer.get().getInstance( SchedStaskDao.class );
      lUserParametersStub.setString( "RELEASE_MISSING_COMPONENT", "OK" );
      lUserParametersStub.setString( "RELEASE_AOG_FAULT", "OK" );
      lUserParametersStub.setString( "RELEASE_NEAR_DUE_FAULT", "OK" );
      lUserParametersStub.setString( "RELEASE_NEAR_DUE_TASK", "OK" );
      lUserParametersStub.setString( "RELEASE_OVERDUE_FAULT", "OK" );
      lUserParametersStub.setString( "RELEASE_OVERDUE_TASK", "OK" );
      lUserParametersStub.setString( "RELEASE_LOW_SEV_OPEN_FAULT", "OK" );
      lUserParametersStub.setString( "RELEASE_UNCOLLECTED_JOB_CARDS", "OK" );
   }


   /**
    * <pre>
    *    Given an active task
         When I create a quick work package and enter usage
         Then the work package usage will be set to the same usage
    * </pre>
    */
   @Test
   public void itCreatesWorkPackageWithManuallyEnteredUsage() throws Exception {

      final PartNoKey acftPart = Domain.createPart();

      final AssemblyKey aircraftAssembly = Domain.createAircraftAssembly(
            aAircraftAssembly -> aAircraftAssembly.setRootConfigurationSlot(
                  aRootConfigSlot -> aRootConfigSlot.addPartGroup( aPartGroup -> {
                     aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                     aPartGroup.addPart( acftPart );
                  } ) ) );

      final LocationKey supplyLocation = Domain.createLocation( aLoc -> {
         aLoc.setIsSupplyLocation( true );
         aLoc.setCode( "YYZ" );
         aLoc.setType( RefLocTypeKey.AIRPORT );
      } );

      final LocationKey locationKey = Domain.createLocation( aLoc -> {
         aLoc.setCode( LINE_LOCATION_CD );
         aLoc.setType( RefLocTypeKey.LINE );
         aLoc.setSupplyLocation( supplyLocation );
         aLoc.setParent( supplyLocation );
      } );

      final InventoryKey aircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( aircraftAssembly );
         aAircraft.setPart( acftPart );
         aAircraft.setLocation( locationKey );
         aAircraft.addUsage( DataTypeKey.HOURS, BigDecimal.TEN );
      } );

      final TaskKey task = Domain.createRequirement( aReq -> {
         aReq.setInventory( aircraft );
      } );

      STaskBean staskBean = new STaskBean();

      TaskKey[] tasksToBeAssigned = { task };

      UsageSnapshot[] aircraftUsageSnapshots =
            { new UsageSnapshot( aircraft, DataTypeKey.HOURS, BigDecimal.TEN ) };

      // ACT
      TaskKey newCheck = staskBean.packageAndCompleteWithUsage( tasksToBeAssigned,
            createQuickCheckTO(), authHr, aircraftUsageSnapshots, false, userTransaction );

      assertThat( schedStaskDao.findByPrimaryKey( newCheck ).exists(), is( true ) );

      EvtInvUsageTable evtInvUsageTable =
            EvtInvUsageTable.findByPrimaryKey( new EventInventoryUsageKey(
                  new EventInventoryKey( newCheck.getEventKey(), 1 ), DataTypeKey.HOURS ) );
      Double actualWorkPackageHoursUsage = evtInvUsageTable.getTsnQt();
      Double expectedWorkPacakgeHoursUsage = new Double( 10 );

      assertThat( "Unexpected work package usage", actualWorkPackageHoursUsage,
            is( expectedWorkPacakgeHoursUsage ) );

   }


   /**
    * <pre>
    *    Given an active task
         When I create a quick work package and enter usage
         Then the task usage will be set to the same usage
    * </pre>
    */
   @Test
   public void itSetsTaskUsageWithManuallyEnteredUsage() throws Exception {
      final PartNoKey acftPart = Domain.createPart();

      final AssemblyKey aircraftAssembly = Domain.createAircraftAssembly(
            aAircraftAssembly -> aAircraftAssembly.setRootConfigurationSlot(
                  aRootConfigSlot -> aRootConfigSlot.addPartGroup( aPartGroup -> {
                     aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                     aPartGroup.addPart( acftPart );
                  } ) ) );

      final LocationKey supplyLocation = Domain.createLocation( aLoc -> {
         aLoc.setIsSupplyLocation( true );
         aLoc.setCode( "YYZ" );
         aLoc.setType( RefLocTypeKey.AIRPORT );
      } );

      final LocationKey locationKey = Domain.createLocation( aLoc -> {
         aLoc.setCode( LINE_LOCATION_CD );
         aLoc.setType( RefLocTypeKey.LINE );
         aLoc.setSupplyLocation( supplyLocation );
         aLoc.setParent( supplyLocation );
      } );

      final InventoryKey aircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( aircraftAssembly );
         aAircraft.setPart( acftPart );
         aAircraft.setLocation( locationKey );
         aAircraft.addUsage( DataTypeKey.HOURS, BigDecimal.TEN );
      } );

      final TaskKey task = Domain.createRequirement( aReq -> {
         aReq.setInventory( aircraft );
      } );

      STaskBean staskBean = new STaskBean();

      TaskKey[] tasksToBeAssigned = { task };

      UsageSnapshot[] aircraftUsageSnapshots =
            { new UsageSnapshot( aircraft, DataTypeKey.HOURS, BigDecimal.TEN ) };

      // ACT
      TaskKey newCheck = staskBean.packageAndCompleteWithUsage( tasksToBeAssigned,
            createQuickCheckTO(), authHr, aircraftUsageSnapshots, false, userTransaction );

      assertThat( schedStaskDao.findByPrimaryKey( newCheck ).exists(), is( true ) );

      EvtInvUsageTable evtInvUsageTable =
            EvtInvUsageTable.findByPrimaryKey( new EventInventoryUsageKey(
                  new EventInventoryKey( tasksToBeAssigned[0].getEventKey(), 1 ),
                  DataTypeKey.HOURS ) );
      Double actualTaskHoursUsage = evtInvUsageTable.getTsnQt();
      Double expectedTaskHoursUsage = new Double( 10 );

      assertThat( "Unexpected task usage", actualTaskHoursUsage, is( expectedTaskHoursUsage ) );

   }


   /**
    * <pre>
    *    Given an active task
         When I create a quick work package and enter usage
         Then the usage snapshots created for the work package will be set to Customer
    * </pre>
    */
   @Test
   public void itMarksWorkPackageUsageAsCustomerEnteredUsage() throws Exception {

      final PartNoKey acftPart = Domain.createPart();

      final AssemblyKey aircraftAssembly = Domain.createAircraftAssembly(
            aAircraftAssembly -> aAircraftAssembly.setRootConfigurationSlot(
                  aRootConfigSlot -> aRootConfigSlot.addPartGroup( aPartGroup -> {
                     aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                     aPartGroup.addPart( acftPart );
                  } ) ) );

      final LocationKey supplyLocation = Domain.createLocation( aLoc -> {
         aLoc.setIsSupplyLocation( true );
         aLoc.setCode( "YYZ" );
         aLoc.setType( RefLocTypeKey.AIRPORT );
      } );

      final LocationKey locationKey = Domain.createLocation( aLoc -> {
         aLoc.setCode( LINE_LOCATION_CD );
         aLoc.setType( RefLocTypeKey.LINE );
         aLoc.setSupplyLocation( supplyLocation );
         aLoc.setParent( supplyLocation );
      } );

      final InventoryKey aircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( aircraftAssembly );
         aAircraft.setPart( acftPart );
         aAircraft.setLocation( locationKey );
         aAircraft.addUsage( DataTypeKey.HOURS, BigDecimal.TEN );
      } );

      final TaskKey task = Domain.createRequirement( aReq -> {
         aReq.setInventory( aircraft );
      } );

      STaskBean staskBean = new STaskBean();

      TaskKey[] tasksToBeAssigned = { task };

      UsageSnapshot[] aircraftUsageSnapshots =
            { new UsageSnapshot( aircraft, DataTypeKey.HOURS, BigDecimal.TEN ) };

      // ACT
      TaskKey newCheck = staskBean.packageAndCompleteWithUsage( tasksToBeAssigned,
            createQuickCheckTO(), authHr, aircraftUsageSnapshots, false, userTransaction );

      assertThat( schedStaskDao.findByPrimaryKey( newCheck ).exists(), is( true ) );

      EvtInvUsageTable evtInvUsageTable =
            EvtInvUsageTable.findByPrimaryKey( new EventInventoryUsageKey(
                  new EventInventoryKey( newCheck.getEventKey(), 1 ), DataTypeKey.HOURS ) );
      RefUsgSnapshotSrcTypeKey actualWorkPackageUsageSourceType =
            evtInvUsageTable.getUsageSnapshotSourceType();
      RefUsgSnapshotSrcTypeKey expectedWorkPackageUsageSourceType =
            RefUsgSnapshotSrcTypeKey.CUSTOMER;

      assertThat( "Unexpected work package usage source type", actualWorkPackageUsageSourceType,
            is( expectedWorkPackageUsageSourceType ) );

   }


   /**
    * <pre>
    *    Given an active task
         When I create a quick work package and enter usage
         Then the usage snapshots created for the task will be set to Customer
    * </pre>
    */
   @Test
   public void itMarksTaskUsageAsCustomerEnteredUsage() throws Exception {

      final PartNoKey acftPart = Domain.createPart();

      final AssemblyKey aircraftAssembly = Domain.createAircraftAssembly(
            aAircraftAssembly -> aAircraftAssembly.setRootConfigurationSlot(
                  aRootConfigSlot -> aRootConfigSlot.addPartGroup( aPartGroup -> {
                     aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                     aPartGroup.addPart( acftPart );
                  } ) ) );

      final LocationKey supplyLocation = Domain.createLocation( aLoc -> {
         aLoc.setIsSupplyLocation( true );
         aLoc.setCode( "YYZ" );
         aLoc.setType( RefLocTypeKey.AIRPORT );
      } );

      final LocationKey locationKey = Domain.createLocation( aLoc -> {
         aLoc.setCode( LINE_LOCATION_CD );
         aLoc.setType( RefLocTypeKey.LINE );
         aLoc.setSupplyLocation( supplyLocation );
         aLoc.setParent( supplyLocation );
      } );

      final InventoryKey aircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( aircraftAssembly );
         aAircraft.setPart( acftPart );
         aAircraft.setLocation( locationKey );
         aAircraft.addUsage( DataTypeKey.HOURS, BigDecimal.TEN );
      } );

      final TaskKey task = Domain.createRequirement( aReq -> {
         aReq.setInventory( aircraft );
      } );

      STaskBean staskBean = new STaskBean();

      TaskKey[] tasksToBeAssigned = { task };

      UsageSnapshot[] aircraftUsageSnapshots =
            { new UsageSnapshot( aircraft, DataTypeKey.HOURS, BigDecimal.TEN ) };

      // ACT
      TaskKey newCheck = staskBean.packageAndCompleteWithUsage( tasksToBeAssigned,
            createQuickCheckTO(), authHr, aircraftUsageSnapshots, false, userTransaction );

      assertThat( schedStaskDao.findByPrimaryKey( newCheck ).exists(), is( true ) );

      EvtInvUsageTable evtInvUsageTable =
            EvtInvUsageTable.findByPrimaryKey( new EventInventoryUsageKey(
                  new EventInventoryKey( tasksToBeAssigned[0].getEventKey(), 1 ),
                  DataTypeKey.HOURS ) );
      RefUsgSnapshotSrcTypeKey actualTaskHoursUsageSourceType =
            evtInvUsageTable.getUsageSnapshotSourceType();
      RefUsgSnapshotSrcTypeKey expectedTaskHoursUsageSourceType = RefUsgSnapshotSrcTypeKey.CUSTOMER;

      assertThat( "Unexpected task usage source type", actualTaskHoursUsageSourceType,
            is( expectedTaskHoursUsageSourceType ) );

   }


   /**
    * <pre>
      Given a component based task
      When I package and complete and add usage
      Then the task usage will be set to the delta between the current and the added usage
    * </pre>
    */
   @Test
   public void itSetsComponentTaskUsageWithDeltaBetweenCurrentAndManuallyEnteredUsage()
         throws Exception {

      final PartNoKey acftPart = Domain.createPart();

      final AssemblyKey aircraftAssembly = Domain.createAircraftAssembly(
            aAircraftAssembly -> aAircraftAssembly.setRootConfigurationSlot(
                  aRootConfigSlot -> aRootConfigSlot.addPartGroup( aPartGroup -> {
                     aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                     aPartGroup.addPart( acftPart );
                  } ) ) );

      final LocationKey supplyLocation = Domain.createLocation( aLoc -> {
         aLoc.setIsSupplyLocation( true );
         aLoc.setCode( "YYZ" );
         aLoc.setType( RefLocTypeKey.AIRPORT );
      } );

      final LocationKey locationKey = Domain.createLocation( aLoc -> {
         aLoc.setCode( LINE_LOCATION_CD );
         aLoc.setType( RefLocTypeKey.LINE );
         aLoc.setSupplyLocation( supplyLocation );
         aLoc.setParent( supplyLocation );
      } );

      BigDecimal aircraftHoursCurrentUsage = BigDecimal.TEN;
      final InventoryKey aircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( aircraftAssembly );
         aAircraft.setPart( acftPart );
         aAircraft.setLocation( locationKey );
         aAircraft.addUsage( DataTypeKey.HOURS, aircraftHoursCurrentUsage );
      } );

      BigDecimal trackedInventoryHoursCurrentUsage = BigDecimal.ONE;
      final InventoryKey trackedInventory = Domain.createTrackedInventory( aTrk -> {
         aTrk.setParent( aircraft );
         aTrk.addUsage( DataTypeKey.HOURS, trackedInventoryHoursCurrentUsage );
      } );

      final TaskKey task = Domain.createRequirement( aReq -> {
         aReq.setInventory( trackedInventory );
      } );

      STaskBean staskBean = new STaskBean();

      TaskKey[] tasksToBeAssigned = { task };

      BigDecimal aircraftManuallyEnteredHoursForPackageAndComplete = new BigDecimal( 14 );
      UsageSnapshot[] aircraftUsageSnapshots = { new UsageSnapshot( aircraft, DataTypeKey.HOURS,
            aircraftManuallyEnteredHoursForPackageAndComplete ) };

      // ACT
      TaskKey newCheck = staskBean.packageAndCompleteWithUsage( tasksToBeAssigned,
            createQuickCheckTO(), authHr, aircraftUsageSnapshots, false, userTransaction );

      assertThat( schedStaskDao.findByPrimaryKey( newCheck ).exists(), is( true ) );

      EvtInvUsageTable evtInvUsageTable =
            EvtInvUsageTable.findByPrimaryKey( new EventInventoryUsageKey(
                  new EventInventoryKey( tasksToBeAssigned[0].getEventKey(), 1 ),
                  DataTypeKey.HOURS ) );
      Double actualTaskHoursUsage = evtInvUsageTable.getTsnQt();
      Double expectedTaskHoursUsage =
            aircraftManuallyEnteredHoursForPackageAndComplete.subtract( aircraftHoursCurrentUsage )
                  .add( trackedInventoryHoursCurrentUsage ).doubleValue();

      assertThat( "Unexpected task usage", actualTaskHoursUsage, is( expectedTaskHoursUsage ) );

   }


   /**
    * <pre>
      Given a loose component with current usage
      And an open task against the component
      When the task is packaged and completed with enter usage
      Then the task  usage snapshot has the entered usage for the component.
    * </pre>
    */
   @Test
   public void itSetsComponentTaskUsageWithManuallyEnteredUsageWhenTaskPackageAndCompleted()
         throws Exception {

      PartNoKey trkPart = Domain.createPart();
      final LocationKey supplyLocation = Domain.createLocation( aLoc -> {
         aLoc.setIsSupplyLocation( true );
         aLoc.setCode( "YYZ" );
         aLoc.setType( RefLocTypeKey.AIRPORT );
      } );

      final LocationKey locationKey = Domain.createLocation( aLoc -> {
         aLoc.setCode( LINE_LOCATION_CD );
         aLoc.setType( RefLocTypeKey.SHOP );
         aLoc.setSupplyLocation( supplyLocation );
         aLoc.setParent( supplyLocation );
      } );

      BigDecimal componentCurrentUsage = BigDecimal.TEN;

      final InventoryKey trackedInventory = Domain.createTrackedInventory( aTrk -> {
         aTrk.addUsage( HOURS, componentCurrentUsage );
         aTrk.setLocation( locationKey );
         aTrk.setPartNumber( trkPart );
      } );

      final TaskKey task = Domain.createRequirement( aReq -> {
         aReq.setInventory( trackedInventory );
      } );

      STaskBean staskBean = new STaskBean();
      staskBean.setSessionContext( new SessionContextFake() );

      TaskKey[] tasksToBeAssigned = { task };

      BigDecimal componenttManuallyEnteredHoursForPackageAndComplete = new BigDecimal( 14 );
      UsageSnapshot[] componentUsageSnapshots = { new UsageSnapshot( trackedInventory,
            DataTypeKey.HOURS, componenttManuallyEnteredHoursForPackageAndComplete ) };

      // ACT
      TaskKey newCheck = staskBean.packageAndCompleteWithUsage( tasksToBeAssigned,
            createQuickCheckTO(), authHr, componentUsageSnapshots, false, userTransaction );

      assertThat( schedStaskDao.findByPrimaryKey( newCheck ).exists(), is( true ) );

      EvtInvUsageTable evtInvUsageTable =
            EvtInvUsageTable.findByPrimaryKey( new EventInventoryUsageKey(
                  new EventInventoryKey( tasksToBeAssigned[0].getEventKey(), 1 ),
                  DataTypeKey.HOURS ) );
      Double actualTaskHoursUsage = evtInvUsageTable.getTsnQt();
      Double expectedTaskHoursUsage =
            componenttManuallyEnteredHoursForPackageAndComplete.doubleValue();

      assertThat( "Unexpected task usage", actualTaskHoursUsage, is( expectedTaskHoursUsage ) );

   }


   /**
    * <pre>
      Given a loose component with current usage
      And a sub-component with usage
      And a task against the sub-component
      When the task is packaged and completed with enter usage
      Then the task usage snapshot for the sub-component has usage equal to the difference of current usage of the sub-component and the offset between the current usage of the highest parent and the entered usage.
    * </pre>
    */
   @Test
   public void
         itSetsSubComponentTaskUsageWithOffsetBetweenManuallyEnteredUsageForHighestParentAndItsCurrentUsageWhenTaskPackageAndCompleted()
               throws Exception {

      PartNoKey trkPart = Domain.createPart();
      final LocationKey supplyLocation = Domain.createLocation( aLoc -> {
         aLoc.setIsSupplyLocation( true );
         aLoc.setCode( "YYZ" );
         aLoc.setType( RefLocTypeKey.AIRPORT );
      } );

      final LocationKey locationKey = Domain.createLocation( aLoc -> {
         aLoc.setCode( LINE_LOCATION_CD );
         aLoc.setType( RefLocTypeKey.SHOP );
         aLoc.setSupplyLocation( supplyLocation );
         aLoc.setParent( supplyLocation );
      } );

      BigDecimal componentCurrentUsage = BigDecimal.TEN;
      BigDecimal subComponentCurrentUsage = new BigDecimal( 15 );

      final InventoryKey component = Domain.createTrackedInventory( aTrk -> {
         aTrk.addUsage( HOURS, componentCurrentUsage );
         aTrk.setLocation( locationKey );
         aTrk.setPartNumber( trkPart );
      } );

      final InventoryKey subComponent = Domain.createTrackedInventory( aTrk -> {
         aTrk.addUsage( HOURS, subComponentCurrentUsage );
         aTrk.setLocation( locationKey );
         aTrk.setPartNumber( trkPart );
         aTrk.setParent( component );
      } );

      final TaskKey task = Domain.createRequirement( aReq -> {
         aReq.setInventory( subComponent );
      } );

      STaskBean staskBean = new STaskBean();
      staskBean.setSessionContext( new SessionContextFake() );

      TaskKey[] tasksToBeAssigned = { task };

      BigDecimal componentManuallyEnteredHoursForPackageAndComplete = new BigDecimal( 5 );
      UsageSnapshot[] componentUsageSnapshots = { new UsageSnapshot( component, DataTypeKey.HOURS,
            componentManuallyEnteredHoursForPackageAndComplete ) };

      // ACT
      TaskKey newCheck = staskBean.packageAndCompleteWithUsage( tasksToBeAssigned,
            createQuickCheckTO(), authHr, componentUsageSnapshots, false, userTransaction );

      assertThat( schedStaskDao.findByPrimaryKey( newCheck ).exists(), is( true ) );

      EvtInvUsageTable evtInvUsageTable =
            EvtInvUsageTable.findByPrimaryKey( new EventInventoryUsageKey(
                  new EventInventoryKey( tasksToBeAssigned[0].getEventKey(), 1 ),
                  DataTypeKey.HOURS ) );
      Double actualTaskHoursUsage = evtInvUsageTable.getTsnQt();
      BigDecimal offset =
            componentCurrentUsage.subtract( componentManuallyEnteredHoursForPackageAndComplete );
      Double expectedTaskHoursUsage = subComponentCurrentUsage.subtract( offset ).doubleValue();

      assertThat( "Unexpected task usage", actualTaskHoursUsage, is( expectedTaskHoursUsage ) );

   }


   private CreateQuickCheckTO createQuickCheckTO() throws Exception {

      Date scheduldedDate = new Date();

      // Create the schedule work order TO
      ScheduleInternalTO scheduleInternalWorkOrderTO = new ScheduleInternalTO();
      scheduleInternalWorkOrderTO.setSchedDates( scheduldedDate, scheduldedDate );
      scheduleInternalWorkOrderTO.setWorkLocationCd( LINE_LOCATION_CD );
      scheduleInternalWorkOrderTO.setWorkOrderNo( "Test123" );

      // Create the create quick check TO
      CreateQuickCheckTO createQuickCheckTO = new CreateQuickCheckTO();
      createQuickCheckTO.setCheck( null );
      createQuickCheckTO.setCheckName( "CHECK" );
      createQuickCheckTO.setScheduleInternalTO( scheduleInternalWorkOrderTO );

      return createQuickCheckTO;
   }

}
