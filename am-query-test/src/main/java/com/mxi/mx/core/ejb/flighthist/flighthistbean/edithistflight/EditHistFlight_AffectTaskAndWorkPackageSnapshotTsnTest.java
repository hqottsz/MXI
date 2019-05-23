package com.mxi.mx.core.ejb.flighthist.flighthistbean.edithistflight;

import static com.mxi.mx.core.key.DataTypeKey.CYCLES;
import static com.mxi.mx.core.key.DataTypeKey.HOURS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.ejb.EJBException;

import org.junit.After;
import org.junit.Assert;
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
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.WorkPackage;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.PartRequirementDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.ejb.flighthist.FlightHistBean;
import com.mxi.mx.core.flight.model.FlightLegId;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventInventoryKey;
import com.mxi.mx.core.key.EventInventoryUsageKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefAssmblClassKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefDataSourceKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefRelationTypeKey;
import com.mxi.mx.core.key.RefRemoveReasonKey;
import com.mxi.mx.core.key.RefSchedPartStatusKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefUsgSnapshotSrcTypeKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskPartKey;
import com.mxi.mx.core.key.UsageDefinitionKey;
import com.mxi.mx.core.key.UsageParmKey;
import com.mxi.mx.core.services.event.EventRelationshipUtils;
import com.mxi.mx.core.services.event.inventory.EventInventoryService;
import com.mxi.mx.core.services.event.inventory.UsageSnapshot;
import com.mxi.mx.core.services.event.usage.CollectedUsageParm;
import com.mxi.mx.core.services.flighthist.FlightInformationTO;
import com.mxi.mx.core.services.flighthist.Measurement;
import com.mxi.mx.core.services.inventory.config.RemoveInstallPartTO;
import com.mxi.mx.core.services.inventory.phys.InventoryUsage;
import com.mxi.mx.core.services.inventory.phys.SerializedInventoryService;
import com.mxi.mx.core.services.stask.DefaultManageChildTaskService;
import com.mxi.mx.core.services.stask.creation.CreationService;
import com.mxi.mx.core.services.stask.status.StatusService;
import com.mxi.mx.core.services.stask.taskpart.installed.InstalledPartService;
import com.mxi.mx.core.services.stask.taskpart.removed.RemovedPartService;
import com.mxi.mx.core.table.eqp.EqpAssmblBom;
import com.mxi.mx.core.table.eqp.EqpAssmblPos;
import com.mxi.mx.core.table.eqp.EqpBomPart;
import com.mxi.mx.core.table.eqp.EqpDataSourceSpec;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.EvtInvUsageTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.inv.InvOwnerTable;
import com.mxi.mx.core.table.org.OrgHr;
import com.mxi.mx.core.table.sched.SchedPartTable;


/**
 * Tests for {@linkplain FlightHistBean#editHistFlight} that involve updating the task and work
 * package snapshot usage TSN of inventory involved in the flight.
 *
 */
public class EditHistFlight_AffectTaskAndWorkPackageSnapshotTsnTest {

   private static final String HR_USERNAME = "HR_USERNAME";
   private static final Measurement[] NO_MEASUREMENTS = new Measurement[] {};

   // current cycle and hour values
   private static final BigDecimal INITIAL_CYCLES = new BigDecimal( 50 );
   private static final BigDecimal INITIAL_HOURS = new BigDecimal( 100 );

   // some deltas
   private static final BigDecimal INITIAL_FLIGHT_DELTA = new BigDecimal( 1 );
   private static final BigDecimal MODIFIED_FLIGHT_DELTA = new BigDecimal( 3 );
   private static final double DELTA_DIFF =
         MODIFIED_FLIGHT_DELTA.subtract( INITIAL_FLIGHT_DELTA ).doubleValue();
   private static final int TASK_DURATION_HOURS = 2;

   // the bean under test
   private FlightHistBean iFlightHistBean;

   private HumanResourceKey iHrKey;
   private FlightInformationTO iFlightTO;
   private InventoryKey iAircraftInvKey;
   private InventoryKey iEngineInvKey;
   private InventoryKey iAlternateEngineInvKey;
   private static final String iEngineSerialNumber = "Engine1";
   private static final String iAlternateEngineSerialNumber = "Engine2";
   private PartNoKey iEnginePart;
   private PartGroupKey iEngineBomPartKey;
   private ConfigSlotPositionKey iEnginePosition;
   private FlightLegId iFlightLegId;
   private Date iFlightActualArrivalDate;
   private LocationKey iMaintenanceLocation;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * <pre>
    * Test: Test that the usage of a task completed after the flight is updated
    * Given an Aircraft with historical flight
    * And the aircraft has one completed task with a completion date after the flight actual arrival date
    * And the work package for that task is started after the flight actual arrival date
    * When I edit the usage Deltas of the historical flight
    * Then TSN usage of the task is updated by the delta difference
    * and the TSN usage of the work package is updated by the delta difference
    * </pre>
    */
   @Test
   public void itUpdatesTaskUsageTsnByDeltaChangeForTaskCompletedAfterEditedFlight()
         throws Exception {

      // create a task with the completion date after the flights actual arrival date
      TaskKey lTaskKey = createTaskAfterFlightOnInventory( iAircraftInvKey );

      // create an in-work work package
      TaskKey lCheckKey = createInWorkWorkPackageForTask( lTaskKey );

      // Get the pre-edit state
      UsageSnapshot lAircraftHoursBeforeEdit =
            getHoursSnapshotForAircraftForEvent( lTaskKey.getEventKey() );
      UsageSnapshot lAircraftCyclesBeforeEdit =
            getCyclesSnapshotForAircraftForEvent( lTaskKey.getEventKey() );

      // Edit the historical flight by changing the Delta
      editFlight();

      // Get the post-edit state
      UsageSnapshot lAircraftHoursAfterEdit =
            getHoursSnapshotForAircraftForEvent( lTaskKey.getEventKey() );
      UsageSnapshot lAircraftCyclesAfterEdit =
            getCyclesSnapshotForAircraftForEvent( lTaskKey.getEventKey() );
      UsageSnapshot lAircraftWPHoursAfterEdit =
            getHoursSnapshotForAircraftForEvent( lCheckKey.getEventKey() );
      UsageSnapshot lAircraftWPCyclesAfterEdit =
            getCyclesSnapshotForAircraftForEvent( lCheckKey.getEventKey() );

      // Assert the aircraft task snapshot has been updated as expected
      Assert.assertEquals( "Unexpected task aircraft TSN value for HOURS.",
            lAircraftHoursBeforeEdit.getTSN() + DELTA_DIFF, lAircraftHoursAfterEdit.getTSN(),
            0.0001 );
      Assert.assertEquals( "Unexpected task aircraft TSN value for CYCLES.",
            lAircraftCyclesBeforeEdit.getTSN() + DELTA_DIFF, lAircraftCyclesAfterEdit.getTSN(),
            0.0001 );

      // Assert the snapshot work package has been updated as expected
      Assert.assertEquals( "Unexpected work package aircraft TSN value for HOURS.",
            lAircraftHoursBeforeEdit.getTSN() + DELTA_DIFF, lAircraftWPHoursAfterEdit.getTSN(),
            0.0001 );
      Assert.assertEquals( "Unexpected work package aircraft TSN value for CYCLES.",
            lAircraftCyclesBeforeEdit.getTSN() + DELTA_DIFF, lAircraftWPCyclesAfterEdit.getTSN(),
            0.0001 );
   }


   /**
    * <pre>
    * Test: Test that the usage of a task completed before the flight is not updated
    * Given an Aircraft with historical flight
    * And the aircraft has one completed task with a completion date before the flight actual arrival date
    * And the work package for that task is 'in-progress'
    * When I edit the usage Deltas of the historical flight
    * Then the TSN usages of the aircraft for the task are not updated by the delta difference
    * And the TSN usage for the aircraft for the work package are not updated
    * </pre>
    */
   @Test
   public void itDoesNotUpdateTaskUsageTsnByDeltaChangeForTaskCompletedBeforeEditedFlight()
         throws Exception {

      // create a task with the completion date before the flights actual arrival date
      TaskKey lTaskKey = createTaskBeforeFlightOnInventory( iAircraftInvKey );

      // create an in-work work package
      TaskKey lCheckKey = createInWorkWorkPackageForTask( lTaskKey );

      // Get the pre-edit state
      UsageSnapshot lAircraftHoursBeforeEdit =
            getHoursSnapshotForAircraftForEvent( lTaskKey.getEventKey() );
      UsageSnapshot lAircraftCyclesBeforeEdit =
            getCyclesSnapshotForAircraftForEvent( lTaskKey.getEventKey() );

      // Edit the historical flight by changing the Delta
      editFlight();

      // Get the post-edit state
      UsageSnapshot lAircraftHoursAfterEdit =
            getHoursSnapshotForAircraftForEvent( lTaskKey.getEventKey() );
      UsageSnapshot lAircraftCyclesAfterEdit =
            getCyclesSnapshotForAircraftForEvent( lTaskKey.getEventKey() );
      UsageSnapshot lAircraftWPHoursAfterEdit =
            getHoursSnapshotForAircraftForEvent( lCheckKey.getEventKey() );
      UsageSnapshot lAircraftWPCyclesAfterEdit =
            getCyclesSnapshotForAircraftForEvent( lCheckKey.getEventKey() );

      // Assert the snapshot has NOT been updated as expected
      Assert.assertEquals( "Unexpected task aircraft TSN value for HOURS.",
            lAircraftHoursBeforeEdit.getTSN(), lAircraftHoursAfterEdit.getTSN(), 0.0001 );
      Assert.assertEquals( "Unexpected task aircraft TSN value for CYCLES.",
            lAircraftCyclesBeforeEdit.getTSN(), lAircraftCyclesAfterEdit.getTSN(), 0.0001 );
      // Assert the snapshot work package has NOT been updated as expected
      Assert.assertEquals( "Unexpected work package aircraft TSN value for HOURS.",
            lAircraftHoursBeforeEdit.getTSN(), lAircraftWPHoursAfterEdit.getTSN(), 0.0001 );
      Assert.assertEquals( "Unexpected work package aircraft TSN value for CYCLES.",
            lAircraftCyclesBeforeEdit.getTSN(), lAircraftWPCyclesAfterEdit.getTSN(), 0.0001 );
   }


   /**
    * <pre>
    * Test: Test that the usage of a task completed at the same time as the flight is updated
    * Given an Aircraft with historical flight
    * And the aircraft has one completed task with a completion date the same time as the flight actual arrival date
    * When I edit the usage Deltas of the historical flight
    * The TSN usages of the task is updated by the delta difference
    * </pre>
    */
   @Test
   public void itUpdatesTaskUsageTsnByDeltaChangeForTaskCompletedConcurrentToEditedFlight()
         throws Exception {

      // create a task with the completion date before the flights actual arrival date
      TaskKey lTaskKey = createTaskCoincidentWithFlightOnInventory( iAircraftInvKey );

      // Get the pre-edit state
      UsageSnapshot lAircraftHoursBeforeEdit =
            getHoursSnapshotForAircraftForEvent( lTaskKey.getEventKey() );
      UsageSnapshot lAircraftCyclesBeforeEdit =
            getCyclesSnapshotForAircraftForEvent( lTaskKey.getEventKey() );

      // Edit the historical flight by changing the Delta
      editFlight();

      // Get the post-edit state
      UsageSnapshot lAircraftHoursAfterEdit =
            getHoursSnapshotForAircraftForEvent( lTaskKey.getEventKey() );
      UsageSnapshot lAircraftCyclesAfterEdit =
            getCyclesSnapshotForAircraftForEvent( lTaskKey.getEventKey() );

      // Assert the snapshot has been updated as expected
      Double lDeltaDiff =
            new Double( MODIFIED_FLIGHT_DELTA.subtract( INITIAL_FLIGHT_DELTA ).doubleValue() );
      Assert.assertEquals( "Unexpected aircraft TSN value for HOURS.",
            lAircraftHoursBeforeEdit.getTSN() + lDeltaDiff, lAircraftHoursAfterEdit.getTSN(),
            0.0001 );
      Assert.assertEquals( "Unexpected aircraft TSN value for CYCLES.",
            lAircraftCyclesBeforeEdit.getTSN() + lDeltaDiff, lAircraftCyclesAfterEdit.getTSN(),
            0.0001 );

   }


   /**
    * <pre>
    * Test: Test that the usage of a task against a sub-assembly completed after the flight is updated
    * Given an Aircraft with historical flight
    * And the aircraft has a sub-assembly which has one completed task with a completion date after the flight actual arrival date
    * When I edit the usage Deltas of sub-assembly in the historical flight
    * The TSN usages of the task is updated by the delta difference
    * </pre>
    */
   @Test
   public void itUpdatesSubAssemblyTaskUsageTsnByDeltaChangeForTaskCompletedAfterEditedFlight()
         throws Exception {

      // create a task with the completion date after the flights actual arrival date
      TaskKey lTaskKey = createTaskAfterFlightOnInventory( iEngineInvKey );

      // Get the pre-edit state
      UsageSnapshot lEngineHoursBeforeEdit =
            getHoursSnapshotForEngineForEvent( iEngineInvKey, lTaskKey.getEventKey() );
      UsageSnapshot lEngineCyclesBeforeEdit =
            getCyclesSnapshotForEngineForEvent( iEngineInvKey, lTaskKey.getEventKey() );

      // Edit the historical flight by changing the Delta
      editFlight();

      // Get the post-edit state
      UsageSnapshot lEngineHoursAfterEdit =
            getHoursSnapshotForEngineForEvent( iEngineInvKey, lTaskKey.getEventKey() );
      UsageSnapshot lEngineCyclesAfterEdit =
            getCyclesSnapshotForEngineForEvent( iEngineInvKey, lTaskKey.getEventKey() );

      // Assert the snapshot has been updated as expected
      Double lDeltaDiff =
            new Double( MODIFIED_FLIGHT_DELTA.subtract( INITIAL_FLIGHT_DELTA ).doubleValue() );
      Assert.assertEquals( "Unexpected aircraft TSN value for HOURS.",
            lEngineHoursBeforeEdit.getTSN() + lDeltaDiff, lEngineHoursAfterEdit.getTSN(), 0.0001 );
      Assert.assertEquals( "Unexpected aircraft TSN value for CYCLES.",
            lEngineCyclesBeforeEdit.getTSN() + lDeltaDiff, lEngineCyclesAfterEdit.getTSN(),
            0.0001 );

   }


   /**
    * <pre>
    * Test: Test that the usage of a task against a sub-assembly completed before the flight is not updated
    * Given an Aircraft with historical flight
    * And the aircraft has a sub-assembly which has one completed task with a completion date before the flight actual arrival date
    * When I edit the usage Deltas of sub-assembly in the historical flight
    * The TSN usages of the task is not updated by the delta difference
    * </pre>
    */
   @Test
   public void
         itDoesNotUpdateSubAssemblyTaskUsageTsnByDeltaChangeForTaskCompletedBeforeEditedFlight()
               throws Exception {

      // create a task with the completion date after the flights actual arrival date
      TaskKey lTaskKey = createTaskBeforeFlightOnInventory( iEngineInvKey );

      // Get the pre-edit state
      UsageSnapshot lEngineHoursBeforeEdit =
            getHoursSnapshotForEngineForEvent( iEngineInvKey, lTaskKey.getEventKey() );
      UsageSnapshot lEngineCyclesBeforeEdit =
            getCyclesSnapshotForEngineForEvent( iEngineInvKey, lTaskKey.getEventKey() );

      // Edit the historical flight by changing the Delta
      editFlight();

      // Get the post-edit state
      UsageSnapshot lEngineHoursAfterEdit =
            getHoursSnapshotForEngineForEvent( iEngineInvKey, lTaskKey.getEventKey() );
      UsageSnapshot lEngineCyclesAfterEdit =
            getCyclesSnapshotForEngineForEvent( iEngineInvKey, lTaskKey.getEventKey() );

      // Assert the snapshot has been updated as expected
      Assert.assertEquals( "Unexpected aircraft TSN value for HOURS.",
            lEngineHoursBeforeEdit.getTSN(), lEngineHoursAfterEdit.getTSN(), 0.0001 );
      Assert.assertEquals( "Unexpected aircraft TSN value for CYCLES.",
            lEngineCyclesBeforeEdit.getTSN(), lEngineCyclesAfterEdit.getTSN(), 0.0001 );

   }


   /**
    * <pre>
    * Test that the usage of a task against a sub-assembly completed at the same time as the flight is updated
    * Given an Aircraft with historical flight
    * And the aircraft has a sub-assembly which has one completed task with a completion date the same time as the flight actual arrival date
    * When I edit the usage Deltas of sub-assembly in the historical flight
    * The TSN usages of the task is updated by the delta difference
    * </pre>
    */
   @Test
   public void
         itUpdatesSubAssemblyTaskUsageTsnByDeltaChangeForTaskCompletedConcurrentToEditedFlight()
               throws Exception {

      // create a task with the completion date after the flights actual arrival date
      TaskKey lTaskKey = createTaskCoincidentWithFlightOnInventory( iEngineInvKey );

      // Get the pre-edit state
      UsageSnapshot lEngineHoursBeforeEdit =
            getHoursSnapshotForEngineForEvent( iEngineInvKey, lTaskKey.getEventKey() );
      UsageSnapshot lEngineCyclesBeforeEdit =
            getCyclesSnapshotForEngineForEvent( iEngineInvKey, lTaskKey.getEventKey() );

      // Edit the historical flight by changing the Delta
      editFlight();

      // Get the post-edit state
      UsageSnapshot lEngineHoursAfterEdit =
            getHoursSnapshotForEngineForEvent( iEngineInvKey, lTaskKey.getEventKey() );
      UsageSnapshot lEngineCyclesAfterEdit =
            getCyclesSnapshotForEngineForEvent( iEngineInvKey, lTaskKey.getEventKey() );

      // Assert the snapshot has been updated as expected
      Double lDeltaDiff =
            new Double( MODIFIED_FLIGHT_DELTA.subtract( INITIAL_FLIGHT_DELTA ).doubleValue() );
      Assert.assertEquals( "Unexpected aircraft TSN value for HOURS.",
            lEngineHoursBeforeEdit.getTSN() + lDeltaDiff, lEngineHoursAfterEdit.getTSN(), 0.0001 );
      Assert.assertEquals( "Unexpected aircraft TSN value for CYCLES.",
            lEngineCyclesBeforeEdit.getTSN() + lDeltaDiff, lEngineCyclesAfterEdit.getTSN(),
            0.0001 );
   }


   /**
    * <pre>
    * Test: Test that usage snapshot taken for the removal of an assembly subinventory during a task is updated after a historical flight is updated
    * Given an Aircraft with a historical flight
    * and the aircraft has a sub-assembly which is removed from the aircraft in a completed task with a completion date after the flight actual arrival date
    * When I edit the usage Deltas of sub-assembly in the historical flight
    * The TSN usage of the configuration change task is updated by the delta difference
    * </pre>
    */
   @Test
   public void itUpdatesRemovalSnapshotTsnByDeltaChangeForTaskCompletedAfterEditedFlight()
         throws Exception {

      // Create a task after the flight
      TaskKey lTaskKey = createTaskAfterFlightOnInventory( iAircraftInvKey );

      // In that task, remove the engine from the aircraft
      removeEngineFromAircraftDuringTask( lTaskKey );

      // Get the configuration event
      EventKey lRemovalEventKey = getConfigurationEventOfTask( lTaskKey, RefEventStatusKey.FGRMVL );

      // Get the pre-edit state
      UsageSnapshot lEngineHoursBeforeEdit =
            getHoursSnapshotForEngineForEvent( iEngineInvKey, lRemovalEventKey );
      UsageSnapshot lEngineCyclesBeforeEdit =
            getCyclesSnapshotForEngineForEvent( iEngineInvKey, lRemovalEventKey );

      // Generate the flight usage params and Edit the historical flight by changing the Delta
      editFlight();

      // Get the post-edit state
      UsageSnapshot lEngineHoursAfterEdit =
            getHoursSnapshotForEngineForEvent( iEngineInvKey, lRemovalEventKey );
      UsageSnapshot lEngineCyclesAfterEdit =
            getCyclesSnapshotForEngineForEvent( iEngineInvKey, lRemovalEventKey );

      // Assert the snapshot has been updated as expected
      Double lDeltaDiff =
            new Double( MODIFIED_FLIGHT_DELTA.subtract( INITIAL_FLIGHT_DELTA ).doubleValue() );
      Assert.assertEquals( "Unexpected aircraft TSN value for HOURS.",
            lEngineHoursBeforeEdit.getTSN() + lDeltaDiff, lEngineHoursAfterEdit.getTSN(), 0.0001 );
      Assert.assertEquals( "Unexpected aircraft TSN value for CYCLES.",
            lEngineCyclesBeforeEdit.getTSN() + lDeltaDiff, lEngineCyclesAfterEdit.getTSN(),
            0.0001 );

   }


   /**
    * <pre>
    * Test: Test that usage snapshot taken for the reinstallation of an assembly subinventory during a task is updated after a historical flight is updated
    * Given an Aircraft with a historical flight
    * and the aircraft has a sub-assembly which is installed on the aircraft in a completed task with a completion date after the flight actual arrival date
    * When I edit the usage Deltas of sub-assembly in the historical flight
    * The TSN usage of the configuration change task is updated by the delta difference
    * </pre>
    */
   @Test
   public void itUpdatesReInstallationSnapshotTsnByDeltaChangeForTaskCompletedAfterEditedFlight()
         throws Exception {

      // Create a task after the flight
      TaskKey lTask1Key = createTaskAfterFlightOnInventory( iAircraftInvKey );

      // In that task, remove the engine from the aircraft
      removeEngineFromAircraftDuringTask( lTask1Key );

      // Create another task after the flight
      TaskKey lTask2Key = createTaskAfterFlightOnInventory( iAircraftInvKey );

      // In the second task, reinstall the same engine to the same aircraft
      installEngineToAircraftDuringTask( iEngineInvKey, iEngineSerialNumber, lTask2Key );

      // Get the configuration event
      EventKey lInstallEventKey =
            getConfigurationEventOfTask( lTask2Key, RefEventStatusKey.FGINST );

      // Get the pre-edit state
      UsageSnapshot lEngineHoursBeforeEdit =
            getHoursSnapshotForEngineForEvent( iEngineInvKey, lInstallEventKey );
      UsageSnapshot lEngineCyclesBeforeEdit =
            getCyclesSnapshotForEngineForEvent( iEngineInvKey, lInstallEventKey );

      // Generate the flight usage params and Edit the historical flight by changing the Delta
      editFlight();

      // Get the post-edit state
      UsageSnapshot lEngineHoursAfterEdit =
            getHoursSnapshotForEngineForEvent( iEngineInvKey, lInstallEventKey );
      UsageSnapshot lEngineCyclesAfterEdit =
            getCyclesSnapshotForEngineForEvent( iEngineInvKey, lInstallEventKey );

      // Assert the snapshot has been updated as expected
      Double lDeltaDiff =
            new Double( MODIFIED_FLIGHT_DELTA.subtract( INITIAL_FLIGHT_DELTA ).doubleValue() );
      Assert.assertEquals( "Unexpected aircraft TSN value for HOURS.",
            lEngineHoursBeforeEdit.getTSN() + lDeltaDiff, lEngineHoursAfterEdit.getTSN(), 0.0001 );
      Assert.assertEquals( "Unexpected aircraft TSN value for CYCLES.",
            lEngineCyclesBeforeEdit.getTSN() + lDeltaDiff, lEngineCyclesAfterEdit.getTSN(),
            0.0001 );

   }


   /**
    * <pre>
    * Test: Test that usage snapshot taken for the reinstallation of an assembly subinventory during a task is updated after a historical flight is updated
    * Given an Aircraft with a historical flight
    * and the aircraft has a sub-assembly which is installed on the aircraft in a completed task with a completion date after the flight actual arrival date
    * When I edit the usage Deltas of sub-assembly in the historical flight
    * The TSN usage of the configuration change task is updated by the delta difference
    * </pre>
    */
   @Test
   public void
         itDoesNotUpdateNewEngineInstallationSnapshotTsnByDeltaChangeForTaskCompletedAfterEditedFlight()
               throws Exception {

      // Create a task after the flight
      TaskKey lTask1Key = createTaskAfterFlightOnInventory( iAircraftInvKey );

      // In that task, remove the engine from the aircraft
      removeEngineFromAircraftDuringTask( lTask1Key );

      // Create another task after the flight
      TaskKey lTask2Key = createTaskAfterFlightOnInventory( iAircraftInvKey );

      // In the second task, install a different engine to the same aircraft
      installEngineToAircraftDuringTask( iAlternateEngineInvKey, iAlternateEngineSerialNumber,
            lTask2Key );

      // Get the configuration event
      EventKey lInstallEventKey =
            getConfigurationEventOfTask( lTask2Key, RefEventStatusKey.FGINST );

      // Get the pre-edit state
      UsageSnapshot lEngineHoursBeforeEdit =
            getHoursSnapshotForEngineForEvent( iAlternateEngineInvKey, lInstallEventKey );
      UsageSnapshot lEngineCyclesBeforeEdit =
            getCyclesSnapshotForEngineForEvent( iAlternateEngineInvKey, lInstallEventKey );

      // Generate the flight usage params and Edit the historical flight by changing the Delta
      editFlight();

      // Get the post-edit state
      UsageSnapshot lEngineHoursAfterEdit =
            getHoursSnapshotForEngineForEvent( iAlternateEngineInvKey, lInstallEventKey );
      UsageSnapshot lEngineCyclesAfterEdit =
            getCyclesSnapshotForEngineForEvent( iAlternateEngineInvKey, lInstallEventKey );

      // Assert the snapshot has not been updated as expected
      Assert.assertEquals( "Unexpected aircraft TSN value for HOURS.",
            lEngineHoursBeforeEdit.getTSN(), lEngineHoursAfterEdit.getTSN(), 0.0001 );
      Assert.assertEquals( "Unexpected aircraft TSN value for CYCLES.",
            lEngineCyclesBeforeEdit.getTSN(), lEngineCyclesAfterEdit.getTSN(), 0.0001 );

   }


   /**
    * <pre>
    * Test that usage snapshot of a completed work package started after the flight is updated
    * Given an Aircraft with a historical flight
    * and the aircraft has one completed work package with a start date and completion date after the flight actual arrival date
    * When I edit the usage Deltas of the historical flight
    * Then the TSN usages of the aircraft for the work package is updated by the delta difference
    * </pre>
    */
   @Test
   public void
         itDoesNotUpdateCompletedAircraftWorkPackageSnapshotTsnByDeltaChangeForWorkPackageCompletedBeforeEditedFlight()
               throws Exception {

      // create a task with the completion date after the flights actual arrival date
      TaskKey lTaskKey = createTaskBeforeFlightOnInventory( iAircraftInvKey );

      // create an in-work work package
      TaskKey lCheckKey = createCompletedWorkPackageForTask( lTaskKey );

      // Get the pre-edit state
      UsageSnapshot lAircraftHoursBeforeEdit =
            getHoursSnapshotForAircraftForEvent( lTaskKey.getEventKey() );
      UsageSnapshot lAircraftCyclesBeforeEdit =
            getCyclesSnapshotForAircraftForEvent( lTaskKey.getEventKey() );

      // Edit the historical flight by changing the Delta
      editFlight();

      // Get the post-edit state
      UsageSnapshot lAircraftWPHoursAfterEdit =
            getHoursSnapshotForAircraftForEvent( lCheckKey.getEventKey() );
      UsageSnapshot lAircraftWPCyclesAfterEdit =
            getCyclesSnapshotForAircraftForEvent( lCheckKey.getEventKey() );

      // Assert the work package snapshot has been updated as expected
      Assert.assertEquals( "Unexpected aircraft TSN value for HOURS.",
            lAircraftHoursBeforeEdit.getTSN(), lAircraftWPHoursAfterEdit.getTSN(), 0.0001 );
      Assert.assertEquals( "Unexpected aircraft TSN value for CYCLES.",
            lAircraftCyclesBeforeEdit.getTSN(), lAircraftWPCyclesAfterEdit.getTSN(), 0.0001 );
   }


   /**
    * <pre>
    * Test that usage snapshot of a completed work package started after the flight is updated
    * Given an Aircraft with a historical flight
    * and the aircraft has one completed work package with a start date and completion date after the flight actual arrival date
    * When I edit the usage Deltas of the historical flight
    * Then the TSN usages of the aircraft for the work package is updated by the delta difference
    * </pre>
    */
   @Test
   public void
         itUpdatesCompletedAircraftWorkPackageSnapshotTsnByDeltaChangeForWorkPackageStartedAfterEditedFlight()
               throws Exception {

      // create a task with the completion date after the flights actual arrival date
      TaskKey lTaskKey = createTaskAfterFlightOnInventory( iAircraftInvKey );

      // create an in-work work package
      TaskKey lCheckKey = createCompletedWorkPackageForTask( lTaskKey );

      // Get the pre-edit state
      UsageSnapshot lAircraftHoursBeforeEdit =
            getHoursSnapshotForAircraftForEvent( lTaskKey.getEventKey() );
      UsageSnapshot lAircraftCyclesBeforeEdit =
            getCyclesSnapshotForAircraftForEvent( lTaskKey.getEventKey() );

      // Edit the historical flight by changing the Delta
      editFlight();

      // Get the post-edit state
      UsageSnapshot lAircraftWPHoursAfterEdit =
            getHoursSnapshotForAircraftForEvent( lCheckKey.getEventKey() );
      UsageSnapshot lAircraftWPCyclesAfterEdit =
            getCyclesSnapshotForAircraftForEvent( lCheckKey.getEventKey() );

      // Assert the work package snapshot has been updated as expected
      Assert.assertEquals( "Unexpected aircraft TSN value for HOURS.",
            lAircraftHoursBeforeEdit.getTSN() + DELTA_DIFF, lAircraftWPHoursAfterEdit.getTSN(),
            0.0001 );
      Assert.assertEquals( "Unexpected aircraft TSN value for CYCLES.",
            lAircraftCyclesBeforeEdit.getTSN() + DELTA_DIFF, lAircraftWPCyclesAfterEdit.getTSN(),
            0.0001 );
   }


   /**
    * <pre>
    * Test that usage snapshot of an in-work work package of subassembly started after the flight is updated
    * Given an Aircraft with a historical flight
    * and a sub-assembly that was installed during the flight has an in-work work package with a start date after the flight actual arrival date
    * When I edit the usage deltas of the historical flight
    * Then the TSN usage of the subassembly for the work package is updated by the delta difference
    * </pre>
    */
   @Test
   public void
         itUpdatesInWorkSubAssemblyWorkPackageSnapshotTsnByDeltaChangeForWorkPackageStartedAfterEditedFlight()
               throws Exception {

      // create a task with the completion date after the flights actual arrival date
      TaskKey lTaskKey = createTaskAfterFlightOnInventory( iEngineInvKey );

      // create a work package
      TaskKey lCheckKey = createInWorkRepairOrderForTask( lTaskKey );

      // Get the pre-edit state
      UsageSnapshot lSubAssemblyHoursBeforeEdit =
            getHoursSnapshotForEngineForEvent( iEngineInvKey, lTaskKey.getEventKey() );
      UsageSnapshot lSubAssemblyCyclesBeforeEdit =
            getCyclesSnapshotForEngineForEvent( iEngineInvKey, lTaskKey.getEventKey() );

      // Edit the historical flight by changing the Delta
      editFlight();

      // Get the post-edit state
      UsageSnapshot lSubAssemblyWPHoursAfterEdit =
            getHoursSnapshotForEngineForEvent( iEngineInvKey, lCheckKey.getEventKey() );
      UsageSnapshot lSubAssemblyWPCyclesAfterEdit =
            getCyclesSnapshotForEngineForEvent( iEngineInvKey, lCheckKey.getEventKey() );

      // Assert the work package snapshot has been updated as expected
      Assert.assertEquals( "Unexpected work package sub-assembly TSN value for HOURS.",
            lSubAssemblyHoursBeforeEdit.getTSN() + DELTA_DIFF,
            lSubAssemblyWPHoursAfterEdit.getTSN(), 0.0001 );
      Assert.assertEquals( "Unexpected work package sub-assembly TSN value for CYCLES.",
            lSubAssemblyCyclesBeforeEdit.getTSN() + DELTA_DIFF,
            lSubAssemblyWPCyclesAfterEdit.getTSN(), 0.0001 );
   }


   /**
    * <pre>
    * Given an aircraft with current usage
      And an in-work work package with manually added usage
      And a historical flight completed before the work package start date
      When we edit the flight with completion date at the same time as the work package start date
      Then the usage snapshot of the work package should not be updated automatically
    * </pre>
    */
   @Test
   public void
         itDoesNotUpdateManuallyEnteredWorkPackageUsageForWorkPackageStartedAtTheSameTimeAsEditedFlight()
               throws Exception {

      final AssemblyKey lAcftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aBuilder ) {
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        aBuilder.addUsageParameter( DataTypeKey.HOURS );
                     }
                  } );
               }
            } );
      InventoryKey lAircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( lAcftAssembly );
         aAircraft.addUsage( HOURS, BigDecimal.TEN );
      } );
      final BigDecimal lFlightHours = BigDecimal.valueOf( 10 );
      Date lWorkPackageStartDate = new Date();
      BigDecimal lWorkPackageHoursUsage = lFlightHours;
      UsageSnapshot lWorkpackageUsageSnapshot =
            new UsageSnapshot( lAircraft, HOURS, lWorkPackageHoursUsage );
      lWorkpackageUsageSnapshot.withSnapshotSourceType( RefUsgSnapshotSrcTypeKey.CUSTOMER );
      final TaskKey lWorkPackage =
            Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

               @Override
               public void configure( WorkPackage aBuilder ) {
                  aBuilder.setActualStartDate( lWorkPackageStartDate );
                  aBuilder.setAircraft( lAircraft );
                  aBuilder.setStatus( RefEventStatusKey.IN_WORK );
                  aBuilder.addUsageSnapshot( lWorkpackageUsageSnapshot );
               }

            } );

      Date lFlightArrivalDate = lWorkPackageStartDate;
      FlightLegId lFlight = Domain.createFlight( aFlight -> {
         aFlight.setAircraft( lAircraft );
         aFlight.setHistorical( true );
         aFlight.setArrivalDate( lFlightArrivalDate );
         aFlight.addUsage( lAircraft, HOURS, lFlightHours, lFlightHours );
      } );

      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( DateUtils.addDays( lFlightArrivalDate, 1 ) );
            aFlight.addUsage( lAircraft, HOURS, lFlightHours, lFlightHours.add( BigDecimal.ONE ) );
         }
      } );

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
      Date lEditedFlightArrivalDate = lFlightArrivalDate;
      Date lEditedFlightDepartureDate =
            DateUtils.addHours( lEditedFlightArrivalDate, -MODIFIED_FLIGHT_DELTA.intValue() );
      BigDecimal lEditedFlightHours = lFlightHours.subtract( MODIFIED_FLIGHT_DELTA );
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lAircraft, HOURS, lEditedFlightHours ) };
      FlightInformationTO lFlightTo = new FlightInformationTO( "Flight Name", null, null, null,
            null, null, lDepartureAirport, lArrivalAirport, null, null, null, null,
            lEditedFlightDepartureDate, lEditedFlightArrivalDate, null, null, false, false );

      iFlightHistBean.editHistFlight( lFlight, iHrKey, lFlightTo, lEditUsageParms,
            NO_MEASUREMENTS );

      EvtInvUsageTable lEvtInvUsageTable =
            EvtInvUsageTable.findByPrimaryKey( new EventInventoryUsageKey(
                  new EventInventoryKey( lWorkPackage.getEventKey(), 1 ), HOURS ) );
      Double lActualHoursWPUsage = lEvtInvUsageTable.getTsnQt();
      Double lExpectedHoursWPUsage = lWorkPackageHoursUsage.doubleValue();
      assertEquals( "Unexpected update of workpackage usage snapshot", lExpectedHoursWPUsage,
            lActualHoursWPUsage );
   }


   /**
    * <pre>
    * Given An aircraft with current usage
      And an in work package with manually added usage
      And a historical flight completed before the work package start date
      When we edit the flight with completion date before the work package start date
      Then the usage snapshot of the work package should not be updated automatically
    * </pre>
    */
   @Test
   public void
         itDoesNotUpdateManuallyEnteredWorkPackageUsageForWorkPackageStartedAfterEditedFlight()
               throws Exception {

      final AssemblyKey lAcftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aBuilder ) {
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        aBuilder.addUsageParameter( DataTypeKey.HOURS );
                     }
                  } );
               }
            } );
      InventoryKey lAircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( lAcftAssembly );
         aAircraft.addUsage( HOURS, BigDecimal.TEN );
      } );
      final BigDecimal lFlightHours = BigDecimal.valueOf( 10 );
      Date lWorkPackageStartDate = new Date();
      BigDecimal lWorkPackageHoursUsage = lFlightHours;
      UsageSnapshot lWorkpackageUsageSnapshot =
            new UsageSnapshot( lAircraft, HOURS, lWorkPackageHoursUsage );
      lWorkpackageUsageSnapshot.withSnapshotSourceType( RefUsgSnapshotSrcTypeKey.CUSTOMER );
      final TaskKey lWorkPackage =
            Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

               @Override
               public void configure( WorkPackage aBuilder ) {
                  aBuilder.setActualStartDate( lWorkPackageStartDate );
                  aBuilder.setAircraft( lAircraft );
                  aBuilder.setStatus( RefEventStatusKey.IN_WORK );
                  aBuilder.addUsageSnapshot( lWorkpackageUsageSnapshot );
               }

            } );

      Date lFlightArrivalDate = DateUtils.addHours( lWorkPackageStartDate, -5 );
      FlightLegId lFlight = Domain.createFlight( aFlight -> {
         aFlight.setAircraft( lAircraft );
         aFlight.setHistorical( true );
         aFlight.setArrivalDate( lFlightArrivalDate );
         aFlight.addUsage( lAircraft, HOURS, lFlightHours, lFlightHours );
      } );

      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( DateUtils.addDays( lFlightArrivalDate, 1 ) );
            aFlight.addUsage( lAircraft, HOURS, lFlightHours, lFlightHours.add( BigDecimal.ONE ) );
         }
      } );

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
      Date lEditedFlightArrivalDate = lFlightArrivalDate;
      Date lEditedFlightDepartureDate =
            DateUtils.addHours( lEditedFlightArrivalDate, -MODIFIED_FLIGHT_DELTA.intValue() );
      BigDecimal lEditedFlightHours = BigDecimal.valueOf( 7 );
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lAircraft, HOURS, lEditedFlightHours ) };

      // Create flight data source specifications.
      EqpDataSourceSpec.create( new UsageDefinitionKey(
            InvInvTable.getAssemblyByInventoryKey( lAircraft ), RefDataSourceKey.MXFL ), HOURS );

      FlightInformationTO lFlightTo = new FlightInformationTO( "Flight Name", null, null, null,
            null, null, lDepartureAirport, lArrivalAirport, null, null, null, null,
            lEditedFlightDepartureDate, lEditedFlightArrivalDate, null, null, false, false );

      iFlightHistBean.editHistFlight( lFlight, iHrKey, lFlightTo, lEditUsageParms,
            NO_MEASUREMENTS );

      EvtInvUsageTable lEvtInvUsageTable =
            EvtInvUsageTable.findByPrimaryKey( new EventInventoryUsageKey(
                  new EventInventoryKey( lWorkPackage.getEventKey(), 1 ), HOURS ) );
      Double lActualHoursWPUsage = lEvtInvUsageTable.getTsnQt();
      Double lExpectedHoursWPUsage = lWorkPackageHoursUsage.doubleValue();
      assertEquals( "Unexpected update of workpackage usage snapshot", lExpectedHoursWPUsage,
            lActualHoursWPUsage );
   }


   /**
    * <pre>
    * Given An aircraft with current usage
      And a completed task with a usage snapshot with 'CUSTOMER' source of usage snapshot type
      And a historical flight completed before the task completion date
      When we edit the flight with completion date before the task completion date
      Then the usage snapshot of the work package should not be updated automatically
    * </pre>
    */
   @Test
   public void itDoesNotUpdateManuallyEnteredTaskUsageForTaskCompletedAfterEditedFlight()
         throws Exception {

      final AssemblyKey lAcftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aBuilder ) {
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        aBuilder.addUsageParameter( DataTypeKey.HOURS );
                     }
                  } );
               }
            } );
      InventoryKey lAircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( lAcftAssembly );
         aAircraft.addUsage( HOURS, BigDecimal.TEN );
      } );
      final BigDecimal lFlightHours = BigDecimal.valueOf( 10 );
      Date lTaskCompletionDate = new Date();
      BigDecimal lTaskUsageHours = lFlightHours;
      UsageSnapshot lTaskUsageSnapshot = new UsageSnapshot( lAircraft, HOURS, lTaskUsageHours );
      lTaskUsageSnapshot.withSnapshotSourceType( RefUsgSnapshotSrcTypeKey.CUSTOMER );
      final TaskKey lTask = Domain.createRequirement( aReq -> {
         aReq.setActualEndDate( lTaskCompletionDate );
         aReq.setInventory( lAircraft );
         aReq.addUsage( lTaskUsageSnapshot );
      } );
      Date lFlightArrivalDate = DateUtils.addHours( lTaskCompletionDate, -5 );
      FlightLegId lFlight = Domain.createFlight( aFlight -> {
         aFlight.setAircraft( lAircraft );
         aFlight.setHistorical( true );
         aFlight.setArrivalDate( lFlightArrivalDate );
         aFlight.addUsage( lAircraft, HOURS, lFlightHours, lFlightHours );
      } );

      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( DateUtils.addDays( lFlightArrivalDate, 1 ) );
            aFlight.addUsage( lAircraft, HOURS, lFlightHours, lFlightHours.add( BigDecimal.ONE ) );
         }
      } );

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
      Date lEditedFlightArrivalDate = lFlightArrivalDate;
      Date lEditedFlightDepartureDate =
            DateUtils.addHours( lEditedFlightArrivalDate, -MODIFIED_FLIGHT_DELTA.intValue() );
      BigDecimal lEditedFlightHours = BigDecimal.valueOf( 7 );
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lAircraft, HOURS, lEditedFlightHours ) };

      // Create flight data source specifications.
      EqpDataSourceSpec.create( new UsageDefinitionKey(
            InvInvTable.getAssemblyByInventoryKey( lAircraft ), RefDataSourceKey.MXFL ), HOURS );

      FlightInformationTO lFlightTo = new FlightInformationTO( "Flight Name", null, null, null,
            null, null, lDepartureAirport, lArrivalAirport, null, null, null, null,
            lEditedFlightDepartureDate, lEditedFlightArrivalDate, null, null, false, false );

      iFlightHistBean.editHistFlight( lFlight, iHrKey, lFlightTo, lEditUsageParms,
            NO_MEASUREMENTS );

      EvtInvUsageTable lEvtInvUsageTable = EvtInvUsageTable.findByPrimaryKey(
            new EventInventoryUsageKey( new EventInventoryKey( lTask.getEventKey(), 1 ), HOURS ) );
      Double lActualHoursWPUsage = lEvtInvUsageTable.getTsnQt();
      Double lExpectedHoursWPUsage = lTaskUsageHours.doubleValue();
      assertEquals( "Unexpected update of workpackage usage snapshot", lExpectedHoursWPUsage,
            lActualHoursWPUsage );
   }


   /**
    * <pre>
    * Test: Test that completed work package of subassembly started before the flight is not updated
    * Given an Aircraft with a historical flight
    * And a sub-assembly that was installed during the flight has one completed work package with a start date and completion date before the flight actual arrival date
    * When I edit the usage deltas of the historical flight
    * The TSN usage of the sub-assembly for the work package is not updated by the delta difference
    * </pre>
    */
   @Test
   public void
         itDoesNotUpdateInWorkSubAssemblyWorkPackageSnapshotTsnByDeltaChangeForWorkPackageStartedBeforeEditedFlight()
               throws Exception {

      // create a task with the completion date before the flights actual arrival date
      TaskKey lTaskKey = createTaskBeforeFlightOnInventory( iEngineInvKey );

      // create an in-work work package
      TaskKey lCheckKey = createCompletedRepairOrderForTask( lTaskKey );

      // Get the pre-edit state
      UsageSnapshot lSubAssemblyHoursBeforeEdit =
            getHoursSnapshotForEngineForEvent( iEngineInvKey, lTaskKey.getEventKey() );
      UsageSnapshot lSubAssemblyCyclesBeforeEdit =
            getCyclesSnapshotForEngineForEvent( iEngineInvKey, lTaskKey.getEventKey() );

      // Edit the historical flight by changing the Delta
      editFlight();

      // Get the post-edit state
      UsageSnapshot lSubAssemblyWPHoursAfterEdit =
            getHoursSnapshotForEngineForEvent( iEngineInvKey, lCheckKey.getEventKey() );
      UsageSnapshot lSubAssemblyWPCyclesAfterEdit =
            getCyclesSnapshotForEngineForEvent( iEngineInvKey, lCheckKey.getEventKey() );

      // Assert the snapshot work package has NOT been updated as expected
      Assert.assertEquals( "Unexpected work package sub-assembly TSN value for HOURS.",
            lSubAssemblyHoursBeforeEdit.getTSN(), lSubAssemblyWPHoursAfterEdit.getTSN(), 0.0001 );
      Assert.assertEquals( "Unexpected work package sub-assembly TSN value for CYCLES.",
            lSubAssemblyCyclesBeforeEdit.getTSN(), lSubAssemblyWPCyclesAfterEdit.getTSN(), 0.0001 );
   }


   /**
    * <pre>
    * Test that "in work" work package of subassembly started at the same time the flight arrival is updated
    * Given an Aircraft with a historical flight
    * And a sub-assembly that was installed during the flight has one "in work" work package with a start date the same as the flight actual arrival date
    * When I edit the usage Deltas of the historical flight
    * The TSN usages of the work package is updated by the delta difference
    * </pre>
    */
   @Test
   public void
         itUpdatesInWorkSubAssemblyWorkPackageSnapshotTsnByDeltaChangeForWorkPackageStartedConcurrentToEditedFlight()
               throws Exception {

      // create a task with the completion date concurrent to the flights actual arrival date
      // create an in-work work package
      TaskKey lCheckKey = createInWorkRepairOrderCoincidentWithFlight();

      // Get the pre-edit state
      UsageSnapshot lSubAssemblyHoursBeforeEdit =
            getHoursSnapshotForEngineForEvent( iEngineInvKey, lCheckKey.getEventKey() );
      UsageSnapshot lSubAssemblyCyclesBeforeEdit =
            getCyclesSnapshotForEngineForEvent( iEngineInvKey, lCheckKey.getEventKey() );

      // Edit the historical flight by changing the Delta
      editFlight();

      // Get the post-edit state
      UsageSnapshot lSubAssemblyWPHoursAfterEdit =
            getHoursSnapshotForEngineForEvent( iEngineInvKey, lCheckKey.getEventKey() );
      UsageSnapshot lSubAssemblyWPCyclesAfterEdit =
            getCyclesSnapshotForEngineForEvent( iEngineInvKey, lCheckKey.getEventKey() );

      // Assert the work package snapshot has been updated as expected
      Assert.assertEquals( "Unexpected work package sub-assembly TSN value for HOURS.",
            lSubAssemblyHoursBeforeEdit.getTSN() + DELTA_DIFF,
            lSubAssemblyWPHoursAfterEdit.getTSN(), 0.0001 );
      Assert.assertEquals( "Unexpected work package sub-assembly TSN value for CYCLES.",
            lSubAssemblyCyclesBeforeEdit.getTSN() + DELTA_DIFF,
            lSubAssemblyWPCyclesAfterEdit.getTSN(), 0.0001 );
   }


   @Before
   public void setup() throws Exception {

      // Set up the user
      iHrKey = new HumanResourceDomainBuilder().withUsername( HR_USERNAME ).build();
      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( iHrKey ) );

      // Initialize the bean under test
      iFlightHistBean = new FlightHistBean();
      iFlightHistBean.ejbCreate();
      iFlightHistBean.setSessionContext( new SessionContextFake() );

      // Set up config parms
      int lUserId = OrgHr.findByPrimaryKey( iHrKey ).getUserId();
      UserParametersFake lUserParms = new UserParametersFake( lUserId, "LOGIC" );
      UserParameters.setInstance( lUserId, "LOGIC", lUserParms );

      GlobalParametersFake lConfigParms = new GlobalParametersFake( "LOGIC" );
      lConfigParms.setString( "BLANK_RO_SIGNATURE", "" );
      GlobalParameters.setInstance( lConfigParms );

      // Set up an engine part
      iEnginePart = new PartNoBuilder().withInventoryClass( RefInvClassKey.ASSY )
            .withStatus( RefPartStatusKey.ACTV ).build();

      // Set up an engine assembly
      final AssemblyKey lEngineAssembly =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aAssembly ) {
                  aAssembly.setCode( RefAssmblClassKey.ENG.getCd() );
                  aAssembly.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aRootConfigurationSlot ) {
                        aRootConfigurationSlot.setConfigurationSlotClass( RefBOMClassKey.ROOT );
                        aRootConfigurationSlot.addPartGroup( new DomainConfiguration<PartGroup>() {

                           @Override
                           public void configure( PartGroup lPartGroup ) {
                              lPartGroup.setCode( "ENGINECODE" );
                              lPartGroup.setInventoryClass( RefInvClassKey.ASSY );
                              lPartGroup.addPart( iEnginePart );
                           }
                        } );
                     }
                  } );
               }
            } );

      // Set up an aircraft assembly
      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aBuilder ) {
                  aBuilder.setCode( "ACFTASSY" );
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {

                        aBuilder.setCode( "RootSlot" );
                        aBuilder
                              .addConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                                 @Override
                                 public void configure( ConfigurationSlot aBuilder ) {

                                    aBuilder.setCode( "72" );
                                    aBuilder.setName( "SlotName" );
                                    aBuilder.setConfigurationSlotClass( RefBOMClassKey.SUBASSY );
                                    aBuilder.addPartGroup( new DomainConfiguration<PartGroup>() {

                                       @Override
                                       public void configure( PartGroup aPartGroup ) {
                                          aPartGroup.setCode( "AIRCRAFTENGINECODE" );
                                          aPartGroup.setInventoryClass( RefInvClassKey.ASSY );
                                          aPartGroup.addPart( iEnginePart );
                                       }
                                    } );
                                 }

                              } );
                     }

                  } );

               }

            } );

      // Get the config slot position and part group for the engine config slot in the aircraft
      // assembly
      iEngineBomPartKey = EqpBomPart.getBomPartKey( lAircraftAssembly, "AIRCRAFTENGINECODE" );
      ConfigSlotKey lAircraftEngineConfigSlot = EqpAssmblBom.getBomItemKey( "ACFTASSY", "72" );
      iEnginePosition = new ConfigSlotPositionKey( lAircraftEngineConfigSlot,
            EqpAssmblPos.getFirstPosId( lAircraftEngineConfigSlot ) );

      // Set up a location for the maintenance
      iMaintenanceLocation = new LocationDomainBuilder().isSupplyLocation()
            .withType( RefLocTypeKey.AIRPORT ).build();

      // Create an engine with current usage
      iEngineInvKey = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.addUsage( HOURS, INITIAL_HOURS );
            aEngine.addUsage( CYCLES, INITIAL_CYCLES );
            aEngine.setPartNumber( iEnginePart );
            aEngine.setSerialNumber( iEngineSerialNumber );
            aEngine.setLocation( iMaintenanceLocation );
            aEngine.setOwner( InvOwnerTable.getOwnerKey( "N/A" ) );
            aEngine.setPartGroup( iEngineBomPartKey );
            aEngine.setPosition( iEnginePosition );
         }
      } );

      // Create another engine with current usage
      iAlternateEngineInvKey = createEngine( iAlternateEngineSerialNumber );

      final PartNoKey lAircraftPart = Domain.createPart( new DomainConfiguration<Part>() {

         @Override
         public void configure( Part aPart ) {
            aPart.setInventoryClass( RefInvClassKey.ACFT );
         }

      } );

      // Create aircraft with one engine & current usage
      iAircraftInvKey = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( CYCLES, INITIAL_CYCLES );
            aAircraft.addUsage( HOURS, INITIAL_HOURS );
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.setLocation( iMaintenanceLocation );
            aAircraft.setPart( lAircraftPart );
            aAircraft.addEngine( iEngineInvKey );

         }
      } );

      // Create a flight for the aircraft
      createFlightWithInitialDelta();

   }


   @After
   public void teardown() {
      int lUserId = OrgHr.findByPrimaryKey( iHrKey ).getUserId();
      UserParameters.setInstance( lUserId, "LOGIC", null );
      GlobalParameters.setInstance( "LOGIC", null );
      SecurityIdentificationUtils.setInstance( null );
   }


   private InventoryKey createEngine( final String aSerialNumber ) {

      return Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.addUsage( HOURS, INITIAL_HOURS );
            aEngine.addUsage( CYCLES, INITIAL_CYCLES );
            aEngine.setPartNumber( iEnginePart );
            aEngine.setSerialNumber( aSerialNumber );
            aEngine.setLocation( iMaintenanceLocation );
            aEngine.setOwner( InvOwnerTable.getOwnerKey( "N/A" ) );
         }
      } );

   }


   private void createFlightWithInitialDelta() throws Exception {

      Date lActualDepartureDate = DateUtils.addDays( new Date(), -2 );
      iFlightActualArrivalDate = DateUtils.addHours( lActualDepartureDate, 2 );

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

      CollectedUsageParm[] lFlightUsageParms =
            { generateFlightUsage( iAircraftInvKey, CYCLES, INITIAL_FLIGHT_DELTA ),
                  generateFlightUsage( iAircraftInvKey, HOURS, INITIAL_FLIGHT_DELTA ),
                  generateFlightUsage( iEngineInvKey, CYCLES, INITIAL_FLIGHT_DELTA ),
                  generateFlightUsage( iEngineInvKey, HOURS, INITIAL_FLIGHT_DELTA ) };

      iFlightTO = new FlightInformationTO( "Flight Name", null, null, null, null, null,
            lDepartureAirport, lArrivalAirport, null, null, null, null, lActualDepartureDate,
            iFlightActualArrivalDate, null, null, false, false );

      iFlightLegId = iFlightHistBean.createHistFlight( new AircraftKey( iAircraftInvKey ), iHrKey,
            iFlightTO, lFlightUsageParms, NO_MEASUREMENTS );
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


   // Get the current usage of the inventory in a UsageSnapshot key (assembly inventory usage is
   // included). Consider swapping in UsageSnapshotService when it is in service (OPER-8447)
   private Set<UsageSnapshot> getCurrentUsageSnapshot( InventoryKey aInvKey ) {
      InventoryUsage[] lMainUsages = new SerializedInventoryService( aInvKey ).getUsages();

      InventoryUsage[] lAssyInvUsages = new SerializedInventoryService(
            InvInvTable.findByPrimaryKey( aInvKey ).getAssmblInvNo() ).getUsages();

      Set<UsageSnapshot> lUsages = new HashSet<UsageSnapshot>();

      for ( InventoryUsage lMainUsage : lMainUsages ) {

         InventoryUsage lAssyUsageOfSameType = null;

         for ( InventoryUsage lAssyUsage : lAssyInvUsages ) {
            if ( lAssyUsage.getUsageParmKey().getDataType()
                  .equals( lMainUsage.getUsageParmKey().getDataType() ) ) {
               lAssyUsageOfSameType = lAssyUsage;
               break;
            }
         }

         if ( lAssyUsageOfSameType == null ) {
            lUsages.add( new UsageSnapshot( lMainUsage.getUsageParmKey().getInventory(),
                  lMainUsage.getUsageParmKey().getDataType(), lMainUsage.getTsoQt(),
                  lMainUsage.getTsnQt(), lMainUsage.getTsiQt(), null, null ) );
         } else {
            lUsages.add( new UsageSnapshot( lMainUsage.getUsageParmKey().getInventory(),
                  lMainUsage.getUsageParmKey().getDataType(), lMainUsage.getTsoQt(),
                  lMainUsage.getTsnQt(), lMainUsage.getTsiQt(), lAssyUsageOfSameType.getTsoQt(),
                  lAssyUsageOfSameType.getTsnQt() ) );
         }
      }
      return lUsages;

   }


   // Get the first related TTFG (configuration) event with a given status code.
   private EventKey getConfigurationEventOfTask( TaskKey aTaskKey,
         RefEventStatusKey aRelatedEventStatusCd ) {
      // Find the removal event
      EventKey lEventKey = aTaskKey.getEventKey();

      EventKey[] lEventKeys =
            EventRelationshipUtils.getRelatedEvents( lEventKey, RefRelationTypeKey.TTFG );

      EventKey lConfigurationEventKey = null;
      for ( EventKey lEventItr : lEventKeys ) {
         EvtEventTable lRelatedEvent = EvtEventTable.findByPrimaryKey( lEventItr );
         if ( aRelatedEventStatusCd.equals( lRelatedEvent.getEventStatus() ) ) {
            lConfigurationEventKey = lRelatedEvent.getPk();
            break;
         }
      }
      assertNotNull( "No ".concat( aRelatedEventStatusCd.toValueString() ).concat( " Event" ),
            lConfigurationEventKey );
      return lConfigurationEventKey;
   }


   private UsageSnapshot getSnapshotForEventForUsageParm( EventKey aEventKey,
         UsageParmKey aUsageParmKey ) {

      EventInventoryService lEventInventoryService = new EventInventoryService( aEventKey );
      UsageSnapshot lUsageSnapshots[] = lEventInventoryService.getUsageSnapshot();

      for ( UsageSnapshot lUsageSnapshot : lUsageSnapshots ) {
         if ( lUsageSnapshot.getUsageParmKey().equals( aUsageParmKey ) ) {
            return lUsageSnapshot;
         }
      }
      return null;
   }


   private UsageSnapshot getHoursSnapshotForEngineForEvent( InventoryKey aEngineInvKey,
         EventKey aEventKey ) {
      return getSnapshotForEventForUsageParm( aEventKey, new UsageParmKey( aEngineInvKey, HOURS ) );
   }


   private UsageSnapshot getCyclesSnapshotForEngineForEvent( InventoryKey aEngineInvKey,
         EventKey aEventKey ) {
      return getSnapshotForEventForUsageParm( aEventKey,
            new UsageParmKey( aEngineInvKey, CYCLES ) );
   }


   private UsageSnapshot getHoursSnapshotForAircraftForEvent( EventKey aEventKey ) {
      return getSnapshotForEventForUsageParm( aEventKey,
            new UsageParmKey( iAircraftInvKey, HOURS ) );
   }


   private UsageSnapshot getCyclesSnapshotForAircraftForEvent( EventKey aEventKey ) {
      return getSnapshotForEventForUsageParm( aEventKey,
            new UsageParmKey( iAircraftInvKey, CYCLES ) );
   }


   // Edit the usage deltas for the flight
   private void editFlight() throws MxException, TriggerException {
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( iAircraftInvKey, CYCLES, MODIFIED_FLIGHT_DELTA ),
                  generateFlightUsage( iAircraftInvKey, HOURS, MODIFIED_FLIGHT_DELTA ),
                  generateFlightUsage( iEngineInvKey, CYCLES, MODIFIED_FLIGHT_DELTA ),
                  generateFlightUsage( iEngineInvKey, HOURS, MODIFIED_FLIGHT_DELTA ) };

      iFlightHistBean.editHistFlight( iFlightLegId, iHrKey, iFlightTO, lEditUsageParms,
            NO_MEASUREMENTS );
   }


   private TaskKey createTaskOnInventoryOnDate( final InventoryKey aMainInventoryKey,
         final Date aTaskActualEndDate ) {

      final Date lTaskActualStartDate =
            DateUtils.addHours( aTaskActualEndDate, -TASK_DURATION_HOURS );

      final Set<UsageSnapshot> lUsages = getCurrentUsageSnapshot( aMainInventoryKey );

      // create a task
      return Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aRequirement ) {

            aRequirement.setInventory( aMainInventoryKey );
            aRequirement.setActualStartDate( lTaskActualStartDate );
            aRequirement.setActualEndDate( aTaskActualEndDate );
            aRequirement.setStatus( RefEventStatusKey.COMPLETE );
            for ( UsageSnapshot lUsage : lUsages ) {
               aRequirement.addUsage( lUsage );
            }
         }
      } );
   }


   private TaskKey createTaskBeforeFlightOnInventory( InventoryKey aMainInventory ) {
      final Date lTaskActualEndDate = DateUtils.addDays( iFlightActualArrivalDate, -1 );
      return createTaskOnInventoryOnDate( aMainInventory, lTaskActualEndDate );
   }


   private TaskKey createTaskCoincidentWithFlightOnInventory( InventoryKey aMainInventory ) {
      final Date lTaskActualEndDate = iFlightActualArrivalDate;
      return createTaskOnInventoryOnDate( aMainInventory, lTaskActualEndDate );

   }


   private TaskKey createTaskAfterFlightOnInventory( InventoryKey aMainInventory ) {
      final Date lTaskActualEndDate = DateUtils.addDays( iFlightActualArrivalDate, 1 );
      return createTaskOnInventoryOnDate( aMainInventory, lTaskActualEndDate );
   }


   private void dumpTable( String aTableName ) {
      com.mxi.mx.common.dataset.QuerySet lQs = com.mxi.mx.common.table.QuerySetFactory.getInstance()
            .executeQueryTable( aTableName, null );

      java.lang.System.out.println( aTableName + " ..." );
      while ( lQs.next() ) {
         java.lang.System.out.println( lQs.toDataSetArgument( lQs.getRowNumber() ) );
      }
   }


   private void removeEngineFromAircraftDuringTask( TaskKey aTaskKey )
         throws MxException, TriggerException {

      // build a part requirement
      TaskPartKey lTaskPartKey = new PartRequirementDomainBuilder( aTaskKey ).forPart( iEnginePart )
            .withRemovalQuantity( 1 ).withRemovalReason( RefRemoveReasonKey.IMSCHD )
            .forPartGroup( iEngineBomPartKey ).withRemovalInventory( iEngineInvKey )
            .withRemovalSerialNo( iEngineSerialNumber ).forPosition( iEnginePosition ).build();

      // remove the part
      RemovedPartService.removeParts( lTaskPartKey, null, iHrKey, true, true );
   }


   private void installEngineToAircraftDuringTask( InventoryKey aEngineInvKey,
         String aEngineSerialNumber, TaskKey aTaskKey ) throws MxException, TriggerException {

      // build a part requirement
      TaskPartKey lTaskPartKey = new PartRequirementDomainBuilder( aTaskKey ).forPart( iEnginePart )
            .withInstallQuantity( 1 ).forPartGroup( iEngineBomPartKey )
            .withInstallPart( iEnginePart ).withInstallInventory( aEngineInvKey )
            .withInstallSerialNumber( aEngineSerialNumber ).forPosition( iEnginePosition ).build();

      // We must make it RFI before we can install it
      InvInvTable lEngineInventory = InvInvTable.findByPrimaryKey( aEngineInvKey );
      lEngineInventory.setInvCond( RefInvCondKey.RFI );
      lEngineInventory.update();

      // install the part
      InstalledPartService.installParts( iAircraftInvKey, lTaskPartKey, null, iHrKey, true, true,
            false, null, RemoveInstallPartTO.Builder.createForTaskPart( lTaskPartKey ) );
   }


   // Create a new work package for the task in the manner of CreateNewCheck.java
   private TaskKey createInWorkWorkPackageForTask( TaskKey aTaskKey )
         throws EJBException, Exception {

      // Create the work package
      TaskKey lCheckTaskKey = new CreationService().createTask( null, null, iAircraftInvKey,
            RefTaskClassKey.CHECK, "WP Name", iHrKey, false, true );

      StatusService lStatusService = new StatusService( lCheckTaskKey );

      // add child tasks
      EvtEventTable aChildEvent = EvtEventTable.findByPrimaryKey( aTaskKey.getEventKey() );
      new DefaultManageChildTaskService().addChildTask( lCheckTaskKey, aTaskKey, iHrKey, null, null,
            null );

      // Schedule the work package
      lStatusService.schedule( iHrKey, iMaintenanceLocation, aChildEvent.getActualStartDt(),
            aChildEvent.getEventGdt(), "WO - 1", null, null, null, null, true, false, false );

      // Start the work package
      Set<UsageSnapshot> lCurrentUsages = getCurrentUsageSnapshot( iAircraftInvKey );
      lCurrentUsages.addAll( getCurrentUsageSnapshot( iEngineInvKey ) );

      new StatusService( lCheckTaskKey ).startWork( iHrKey, aChildEvent.getActualStartDt(),
            lCurrentUsages.toArray( new UsageSnapshot[0] ), true, null,
            StatusService.VALIDATE_LOCATION_CONFLICT );

      return lCheckTaskKey;

   }


   // Create a new work package for the task in the manner of CreateNewCheck.java
   private TaskKey createCompletedWorkPackageForTask( TaskKey aTaskKey )
         throws EJBException, Exception {

      TaskKey lCheckKey = createInWorkWorkPackageForTask( aTaskKey );

      // CompleteService.completeCheck uses Asynchronous actions to complete child tasks, and we do
      // not have access to this framework. Instead, emulate CompleteService.basicComplete and
      // complete the check directly
      SchedPartTable.updateStatus( lCheckKey, RefSchedPartStatusKey.COMPLETE );

      return lCheckKey;
   }


   // Create a new work package for the task in the manner of CreateNewCheck.java
   private TaskKey createInWorkRepairOrderForTask( TaskKey aTaskKey )
         throws EJBException, Exception {

      EvtEventTable aChildEvent = EvtEventTable.findByPrimaryKey( aTaskKey.getEventKey() );

      // We can only start an RO on a loose inventory, so remove the engine yesterday
      // Create and execute a part removal of the engine from the aircraft on the given date
      TaskKey lRemovalTask =
            createTaskOnInventoryOnDate( iAircraftInvKey, aChildEvent.getEventDate() );
      createInWorkWorkPackageForTask( lRemovalTask );
      removeEngineFromAircraftDuringTask( lRemovalTask );

      // Create the work package
      TaskKey lCheckTaskKey = new CreationService().createTask( null, null, iEngineInvKey,
            RefTaskClassKey.RO, "WP Name", iHrKey, false, true );

      // add child tasks
      new DefaultManageChildTaskService().addChildTask( lCheckTaskKey, aTaskKey, iHrKey, null, null,
            null );

      // Schedule the work package
      new StatusService( lCheckTaskKey ).schedule( iHrKey, iMaintenanceLocation,
            aChildEvent.getActualStartDt(), aChildEvent.getEventGdt(), "WO - 1", null, null, null,
            null, true, false, false );

      // Start the work package
      Set<UsageSnapshot> lCurrentUsages = getCurrentUsageSnapshot( iEngineInvKey );

      new StatusService( lCheckTaskKey ).startWork( iHrKey, aChildEvent.getActualStartDt(),
            lCurrentUsages.toArray( new UsageSnapshot[0] ), true, null,
            StatusService.VALIDATE_LOCATION_CONFLICT );

      return lCheckTaskKey;

   }


   private TaskKey createCompletedRepairOrderForTask( TaskKey aTaskKey )
         throws EJBException, Exception {

      // Create the work package
      TaskKey lCheckTaskKey = createInWorkRepairOrderForTask( aTaskKey );

      // CompleteService.completeCheck uses Asynchronous actions to complete child tasks, and we do
      // not have access to this framework. Instead, emulate CompleteService.basicComplete and
      // complete
      // the check directly
      SchedPartTable.updateStatus( lCheckTaskKey, RefSchedPartStatusKey.COMPLETE );

      return lCheckTaskKey;

   }


   // Create an in-work repair order, containing one task, on the engine inventory, such that the
   // repair order actual start time is the flight actual arrival time.
   private TaskKey createInWorkRepairOrderCoincidentWithFlight() throws EJBException, Exception {

      // The task will end two hours after it starts,
      Date lTaskActualEndDate = DateUtils.addHours( iFlightActualArrivalDate, TASK_DURATION_HOURS );
      TaskKey lTaskKey = createTaskOnInventoryOnDate( iEngineInvKey, lTaskActualEndDate );

      TaskKey lCheckKey = createInWorkRepairOrderForTask( lTaskKey );

      return lCheckKey;

   }
}
