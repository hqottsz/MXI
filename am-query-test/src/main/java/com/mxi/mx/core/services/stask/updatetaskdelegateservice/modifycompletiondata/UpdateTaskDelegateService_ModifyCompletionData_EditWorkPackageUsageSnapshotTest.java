
package com.mxi.mx.core.services.stask.updatetaskdelegateservice.modifycompletiondata;

import static com.mxi.mx.core.key.DataTypeKey.HOURS;
import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Engine;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.WorkPackage;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.EventInventoryKey;
import com.mxi.mx.core.key.EventInventoryUsageKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.event.inventory.UsageSnapshot;
import com.mxi.mx.core.services.inventory.InvUtils;
import com.mxi.mx.core.services.stask.delegate.UpdateTaskDelegateService;
import com.mxi.mx.core.table.evt.EvtInvUsageTable;
import com.mxi.mx.core.table.org.OrgHr;


/**
 * Tests for {@linkplain UpdateTaskDelegateService#modifyCompletionData} that involve editing work
 * package usage snapshots
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class UpdateTaskDelegateService_ModifyCompletionData_EditWorkPackageUsageSnapshotTest {

   private HumanResourceKey iHrKey;
   private int iUserId;
   private static final Date WORK_PACKAGE_COMPLETION_DATE = new Date();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * GIVEN a completed work package against an aircraft
    *
    * AND the work package contains a usage snapshot for the aircraft
    *
    * When the usage snapshot is edited and the value of a usage for the aircraft is modified.
    *
    * Then the usage snapshot is updated
    */
   @Test
   public void itUpdatesAcftUsageWhenWorkPkgAcftTsnUpdated() throws Exception {

      // GIVEN
      final InventoryKey lAircraft = Domain.createAircraft();

      final double lWorkPackageAircraftHoursTsn = 3;

      TaskKey lWorkPackage = Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            final UsageSnapshot lAircraftWorkPackageUsageSnapshot =
                  new UsageSnapshot( lAircraft, HOURS, lWorkPackageAircraftHoursTsn );
            aWorkPackage.setStatus( RefEventStatusKey.COMPLETE );
            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.addUsageSnapshot( lAircraftWorkPackageUsageSnapshot );
         }

      } );

      final double lWorkPackageAircraftHoursTsnDelta = 2;

      final UsageSnapshot lWorkPackageUsageAircraftSnapshotEdited = new UsageSnapshot( lAircraft,
            HOURS, lWorkPackageAircraftHoursTsn + lWorkPackageAircraftHoursTsnDelta );
      UsageSnapshot[] lUsageSnapshotsEdited =
            new UsageSnapshot[] { lWorkPackageUsageAircraftSnapshotEdited };

      EventKey lWorkPackageEventKey = lWorkPackage.getEventKey();

      // WHEN
      new UpdateTaskDelegateService().modifyCompletionData( lWorkPackage, lAircraft,
            lUsageSnapshotsEdited, WORK_PACKAGE_COMPLETION_DATE, true, iHrKey, true );

      // Then
      EventInventoryKey lEventInventoryKey = new EventInventoryKey( lWorkPackageEventKey, 1 );
      EvtInvUsageTable lEvtInvUsageTable = EvtInvUsageTable
            .findByPrimaryKey( new EventInventoryUsageKey( lEventInventoryKey, HOURS ) );
      Double lActualWorkPackageAircraftHoursTsn = lEvtInvUsageTable.getTsnQt();
      Double lExpectedWorkPackageAircraftHoursTsn =
            lWorkPackageAircraftHoursTsnDelta + lWorkPackageAircraftHoursTsn;
      assertEquals(
            String.format(
                  "Unexpectedly, Work Package Usage Snapshot HOURS TSN value for Aircraft inventory= %s didn't get updated",
                  lAircraft ),
            lExpectedWorkPackageAircraftHoursTsn, lActualWorkPackageAircraftHoursTsn );
   }


   /**
    * Given a completed work package against an aircraft.
    *
    * AND the work package contains a usage snapshot for the aircraft and an engine
    *
    * When the engine usage snapshot is edited and the value of a usage for the engine is modified.
    *
    * Then the aircraft usage snapshot is not updated
    */
   @Test
   public void itDoesNotUpdateAcftUsageWhenWorkPkgSubAssyTsnUpdated() throws Exception {

      // GIVEN
      final InventoryKey lEngine = Domain.createEngine();

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addEngine( lEngine );
         }

      } );

      final double lWorkPackageAircraftHoursTsn = 2;
      final double lWorkPackageEngineHoursTsn = 5;

      TaskKey lWorkPackage = Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {

            final UsageSnapshot lAircraftWorkPackageUsageSnapshot =
                  new UsageSnapshot( lAircraft, HOURS, lWorkPackageAircraftHoursTsn );
            final UsageSnapshot lEngineWorkPackageUsageSnapshot =
                  new UsageSnapshot( lEngine, HOURS, lWorkPackageEngineHoursTsn );
            aWorkPackage.addSubAssembly( lEngine );
            aWorkPackage.setStatus( RefEventStatusKey.COMPLETE );
            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.addUsageSnapshot( lAircraftWorkPackageUsageSnapshot );
            aWorkPackage.addUsageSnapshot( lEngineWorkPackageUsageSnapshot );

         }

      } );

      EventKey lWorkPackageEventKey = lWorkPackage.getEventKey();
      final double lWorkPackageEngineHoursTsnDelta = 2;

      final UsageSnapshot lWorkPackageUsageEngineSnapshotEdited = new UsageSnapshot( lEngine, HOURS,
            lWorkPackageEngineHoursTsn + lWorkPackageEngineHoursTsnDelta );
      UsageSnapshot[] lUsageSnapshotsEdited =
            new UsageSnapshot[] { lWorkPackageUsageEngineSnapshotEdited };

      // WHEN
      new UpdateTaskDelegateService().modifyCompletionData( lWorkPackage, lAircraft,
            lUsageSnapshotsEdited, WORK_PACKAGE_COMPLETION_DATE, true, iHrKey, true );

      // Then
      EventInventoryKey lEventInventoryKey = new EventInventoryKey( lWorkPackageEventKey, 1 );
      EvtInvUsageTable lEvtInvUsageTable = EvtInvUsageTable
            .findByPrimaryKey( new EventInventoryUsageKey( lEventInventoryKey, HOURS ) );
      Double lActualWorkPackageAircraftHoursTsn = lEvtInvUsageTable.getTsnQt();
      Double lExpectedWorkPackageAircraftHoursTsn = lWorkPackageAircraftHoursTsn;
      assertEquals(
            String.format(
                  "Unexpectedly, Work Package Usage Snapshot HOURS TSN value for Aircraft inventory= %s got updated",
                  lAircraft ),
            lExpectedWorkPackageAircraftHoursTsn, lActualWorkPackageAircraftHoursTsn );
   }


   /**
    * Given a completed work package against an aircraft.
    *
    * AND the work package contains a usage snapshot for the aircraft and engine
    *
    * When the engine usage snapshot is edited and the value of a usage for the engine is modified.
    *
    * Then the engine usage snapshot is updated
    */
   @Test
   public void itUpdatesSubAssyUsageWhenWorkPkgSubAssyTsnUpdated() throws Exception {

      // GIVEN
      final InventoryKey lEngine = Domain.createEngine();

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addEngine( lEngine );
         }

      } );

      final double lWorkPackageAircraftHoursTsn = 2;
      final double lWorkPackageEngineHoursTsn = 5;

      final UsageSnapshot lAircraftWorkPackageUsageSnapshot =
            new UsageSnapshot( lAircraft, HOURS, lWorkPackageAircraftHoursTsn );

      TaskKey lWorkPackage = Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {

            aWorkPackage.addSubAssembly( lEngine );

            UsageSnapshot lEngineWorkPackageUsageSnapshot =
                  new UsageSnapshot( lEngine, HOURS, lWorkPackageEngineHoursTsn );

            aWorkPackage.setStatus( RefEventStatusKey.COMPLETE );
            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.addUsageSnapshot( lAircraftWorkPackageUsageSnapshot );
            aWorkPackage.addUsageSnapshot( lEngineWorkPackageUsageSnapshot );

         }

      } );

      EventKey lWorkPackageEventKey = lWorkPackage.getEventKey();
      final double lWorkPackageEngineHoursTsnDelta = 2;

      UsageSnapshot lWorkPackageUsageEngineSnapshotEdited = new UsageSnapshot( lEngine, HOURS,
            lWorkPackageEngineHoursTsnDelta + lWorkPackageEngineHoursTsn );
      UsageSnapshot[] lUsageSnapshotsEdited = new UsageSnapshot[] {
            lAircraftWorkPackageUsageSnapshot, lWorkPackageUsageEngineSnapshotEdited };

      // WHEN
      new UpdateTaskDelegateService().modifyCompletionData( lWorkPackage, lAircraft,
            lUsageSnapshotsEdited, WORK_PACKAGE_COMPLETION_DATE, true, iHrKey, true );

      // Then
      EventInventoryKey lEventInventoryKey = new EventInventoryKey( lWorkPackageEventKey, 2 );
      EvtInvUsageTable lEvtInvUsageTable = EvtInvUsageTable
            .findByPrimaryKey( new EventInventoryUsageKey( lEventInventoryKey, HOURS ) );
      Double lActualWorkPackageEngineHoursTsn = lEvtInvUsageTable.getTsnQt();
      Double lExpectedWorkPackageEngineHoursTsn =
            lWorkPackageEngineHoursTsn + lWorkPackageEngineHoursTsnDelta;
      assertEquals(
            String.format(
                  "Unexpectedly, Work Package Usage Snapshot HOURS TSN value for Engine inventory= %s didn't get updated",
                  lAircraft ),
            lExpectedWorkPackageEngineHoursTsn, lActualWorkPackageEngineHoursTsn );
   }


   /**
    * GIVEN an aircraft with a historical work package raised against it
    *
    * AND the work package contains a usage snapshot for the aircraft.
    *
    * AND the work package has a completed requirement against the aircraft with a usage snapshot
    *
    * When aircraft usage snapshot is edited and the value of a usage for the aircraft is modified.
    *
    * THEN the aircraft usage snapshot for aircraft requirement gets updated.
    */
   @Test
   public void itUpdatesReqAgainstAcftUsageWithinWorkPkgWhenWorkPkgAcftTsnUpdated()
         throws Exception {

      // GIVEN

      final InventoryKey lAircraft = Domain.createAircraft();

      final double lAircraftHoursTsn = 3;
      final UsageSnapshot lAircraftWorkPackageUsageSnapshot =
            new UsageSnapshot( lAircraft, HOURS, lAircraftHoursTsn );

      final TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.addUsage( lAircraftWorkPackageUsageSnapshot );
            aReq.setInventory( lAircraft );
            aReq.setStatus( RefEventStatusKey.COMPLETE );
         }

      } );

      TaskKey lWorkPackage = Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setStatus( RefEventStatusKey.COMPLETE );
            aWorkPackage.addTask( lReq );
            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.addUsageSnapshot( lAircraftWorkPackageUsageSnapshot );
         }

      } );

      EventKey lReqEventKey = lReq.getEventKey();
      final double lWorkPackageAircraftHoursTsnDelta = 2;

      final UsageSnapshot lWorkPackageUsageAircraftSnapshotEdited = new UsageSnapshot( lAircraft,
            HOURS, lAircraftHoursTsn + lWorkPackageAircraftHoursTsnDelta );

      UsageSnapshot[] lUsageSnapshotsEdited =
            new UsageSnapshot[] { lWorkPackageUsageAircraftSnapshotEdited };

      // WHEN
      new UpdateTaskDelegateService().modifyCompletionData( lWorkPackage, lAircraft,
            lUsageSnapshotsEdited, WORK_PACKAGE_COMPLETION_DATE, true, iHrKey, true );

      // Then
      EventInventoryKey lEventInventoryKey = new EventInventoryKey( lReqEventKey, 1 );
      EvtInvUsageTable lEvtInvUsageTable = EvtInvUsageTable
            .findByPrimaryKey( new EventInventoryUsageKey( lEventInventoryKey, HOURS ) );
      Double lActualReqAircraftHoursTsn = lEvtInvUsageTable.getTsnQt();
      Double lExpectedReqAircraftHoursTsn = lWorkPackageAircraftHoursTsnDelta + lAircraftHoursTsn;
      assertEquals( String.format(
            "Unexpectedly, Child Requirement HOURS TSN value for Aircraft inventory= %s didn't get updated",
            lAircraft ), lExpectedReqAircraftHoursTsn, lActualReqAircraftHoursTsn );
   }


   /**
    * Given a completed work package against an aircraft.
    *
    * AND the work package contains a usage snapshot for the aircraft
    *
    * AND the work package has a completed requirement against a aircraft SYSTEM with a usage
    * snapshot
    *
    * When aircraft usage snapshot is edited and the value of a usage for the aircraft is modified.
    *
    * AND the updateSubTask check-box is checked
    *
    * THEN the usage snapshot for SYSTEM requirement gets modified correctly.
    */
   @Test
   public void
         itUpdatesReqAgainstAcftSystemUsageWithinWorkPkgWhenWorkPkgAcftTsnUpdatedAndUpdateSubTaskFlagTrue()
               throws Exception {

      // GIVEN

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addSystem( "Aircraft System" );
         }
      } );

      final InventoryKey lAircraftSystemInv =
            InvUtils.getSystemByName( lAircraft, "Aircraft System" );

      final double lAircraftHoursTsn = 5;
      final double lSystemHoursTsn = 3;
      final UsageSnapshot lAircraftWorkPackageUsageSnapshot =
            new UsageSnapshot( lAircraft, HOURS, lAircraftHoursTsn );

      final TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            final UsageSnapshot lAircraftSystemUsageSnapshot =
                  new UsageSnapshot( lAircraftSystemInv, HOURS, lSystemHoursTsn );
            aReq.addUsage( lAircraftSystemUsageSnapshot );
            aReq.setInventory( lAircraftSystemInv );
            aReq.setStatus( RefEventStatusKey.COMPLETE );
         }

      } );

      TaskKey lWorkPackage = Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setStatus( RefEventStatusKey.COMPLETE );
            aWorkPackage.addTask( lReq );
            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.addUsageSnapshot( lAircraftWorkPackageUsageSnapshot );
         }

      } );

      EventKey lReqEventKey = lReq.getEventKey();
      final double lWorkPackageAircraftHoursTsnDelta = 2;

      final UsageSnapshot lAircraftWorkPackageUsageSnapshotEdited = new UsageSnapshot( lAircraft,
            HOURS, lAircraftHoursTsn + lWorkPackageAircraftHoursTsnDelta );
      UsageSnapshot[] lUsageSnapshotsEdited =
            new UsageSnapshot[] { lAircraftWorkPackageUsageSnapshotEdited };

      // WHEN
      new UpdateTaskDelegateService().modifyCompletionData( lWorkPackage, lAircraft,
            lUsageSnapshotsEdited, WORK_PACKAGE_COMPLETION_DATE, true, iHrKey, true );

      // Then
      EventInventoryKey lEventInventoryKey = new EventInventoryKey( lReqEventKey, 1 );
      EvtInvUsageTable lEvtInvUsageTable = EvtInvUsageTable
            .findByPrimaryKey( new EventInventoryUsageKey( lEventInventoryKey, HOURS ) );
      Double lActualReqSystemHoursTsn = lEvtInvUsageTable.getTsnQt();
      Double lExpectedReqSystemHoursTsn = lSystemHoursTsn + lWorkPackageAircraftHoursTsnDelta;
      assertEquals( String.format(
            "Unexpectedly, Child Requirement HOURS TSN value for Aircraft System inventory= %s didn't get updated",
            lAircraftSystemInv ), lExpectedReqSystemHoursTsn, lActualReqSystemHoursTsn );
   }


   /**
    * Given a completed work package against an aircraft.
    *
    * AND the work package contains a usage snapshot for the aircraft.
    *
    * AND the work package has a completed requirement against a aircraft SYSTEM with a usage
    * snapshot
    *
    * When aircraft usage snapshot is edited and the value of a usage for the aircraft is modified
    *
    * AND the updateSubTask check-box is not checked
    *
    * THEN the usage snapshot for SYSTEM requirement does not get updated
    */
   @Test
   public void
         itDoesNotUpdateReqAgainstAcftSystemUsageWithinWorkPkgWhenWorkPkgAcftTsnUpdatedAndUpdateSubTaskFlagFalse()
               throws Exception {

      // GIVEN

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addSystem( "Aircraft System" );
         }
      } );

      final InventoryKey lAircraftSystemInv =
            InvUtils.getSystemByName( lAircraft, "Aircraft System" );

      final double lAircraftHoursTsn = 5;
      final double lSystemHoursTsn = 3;
      final UsageSnapshot lAircraftWorkPackageUsageSnapshot =
            new UsageSnapshot( lAircraft, HOURS, lAircraftHoursTsn );

      final TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            final UsageSnapshot lAircraftSystemUsageSnapshot =
                  new UsageSnapshot( lAircraftSystemInv, HOURS, lSystemHoursTsn );
            aReq.addUsage( lAircraftSystemUsageSnapshot );
            aReq.setInventory( lAircraftSystemInv );
            aReq.setStatus( RefEventStatusKey.COMPLETE );
         }

      } );

      TaskKey lWorkPackage = Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setStatus( RefEventStatusKey.COMPLETE );
            aWorkPackage.addTask( lReq );
            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.addUsageSnapshot( lAircraftWorkPackageUsageSnapshot );
         }

      } );

      EventKey lReqEventKey = lReq.getEventKey();
      final double lWorkPackageAircraftHoursTsnDelta = 2;

      final UsageSnapshot lAircraftWorkPackageUsageSnapshotEdited = new UsageSnapshot( lAircraft,
            HOURS, lAircraftHoursTsn + lWorkPackageAircraftHoursTsnDelta );
      UsageSnapshot[] lUsageSnapshotsEdited =
            new UsageSnapshot[] { lAircraftWorkPackageUsageSnapshotEdited };

      // WHEN
      new UpdateTaskDelegateService().modifyCompletionData( lWorkPackage, lAircraft,
            lUsageSnapshotsEdited, WORK_PACKAGE_COMPLETION_DATE, false, iHrKey, true );

      // Then
      EventInventoryKey lEventInventoryKey = new EventInventoryKey( lReqEventKey, 1 );
      EvtInvUsageTable lEvtInvUsageTable = EvtInvUsageTable
            .findByPrimaryKey( new EventInventoryUsageKey( lEventInventoryKey, HOURS ) );
      Double lActualReqSystemHoursTsn = lEvtInvUsageTable.getTsnQt();
      Double lExpectedReqSystemHoursTsn = lSystemHoursTsn;
      assertEquals( String.format(
            "Unexpectedly, Child Requirement HOURS TSN value for Aircraft System inventory= %s got updated",
            lAircraftSystemInv ), lExpectedReqSystemHoursTsn, lActualReqSystemHoursTsn );
   }


   /**
    * Given a completed work package against an aircraft.
    *
    * AND the work package contains a usage snapshot for the aircraft
    *
    * AND the work package has a completed requirement against an attached aircraft component with a
    * usage snapshot
    *
    * When aircraft usage snapshot is edited and the value of a usage for the aircraft is modified
    *
    * And updateSubTask check-box is checked
    *
    * THEN the usage snapshot for component requirement gets modified correctly.
    */
   @Test
   public void
         itUpdatesReqAgainstAcftComponentUsageWithinWorkPkgWhenWorkPkgAcftTsnUpdatedAndUpdateSubTaskFlagTrue()
               throws Exception {

      // GIVEN
      final InventoryKey lTrk = Domain.createTrackedInventory();
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addTracked( lTrk );
         }
      } );

      final double lAircraftHoursTsn = 5;
      final double lTrkHoursTsn = 3;
      final UsageSnapshot lAircraftWorkPackageUsageSnapshot =
            new UsageSnapshot( lAircraft, HOURS, lAircraftHoursTsn );

      final TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            final UsageSnapshot lTrkUsageSnapshot = new UsageSnapshot( lTrk, HOURS, lTrkHoursTsn );
            aReq.addUsage( lTrkUsageSnapshot );
            aReq.setInventory( lTrk );
            aReq.setStatus( RefEventStatusKey.COMPLETE );
         }

      } );

      TaskKey lWorkPackage = Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setStatus( RefEventStatusKey.COMPLETE );
            aWorkPackage.addTask( lReq );
            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.addUsageSnapshot( lAircraftWorkPackageUsageSnapshot );
         }

      } );

      EventKey lReqEventKey = lReq.getEventKey();
      final double lWorkPackageAircraftHoursTsnDelta = 2;

      final UsageSnapshot lAircraftWorkPackageUsageSnapshotEdited = new UsageSnapshot( lAircraft,
            HOURS, lAircraftHoursTsn + lWorkPackageAircraftHoursTsnDelta );
      UsageSnapshot[] lUsageSnapshotsEdited =
            new UsageSnapshot[] { lAircraftWorkPackageUsageSnapshotEdited };

      // WHEN
      new UpdateTaskDelegateService().modifyCompletionData( lWorkPackage, lAircraft,
            lUsageSnapshotsEdited, WORK_PACKAGE_COMPLETION_DATE, true, iHrKey, true );

      // Then
      EventInventoryKey lEventInventoryKey = new EventInventoryKey( lReqEventKey, 1 );
      EvtInvUsageTable lEvtInvUsageTable = EvtInvUsageTable
            .findByPrimaryKey( new EventInventoryUsageKey( lEventInventoryKey, HOURS ) );
      Double lActualReqAircraftHoursTsn = lEvtInvUsageTable.getTsnQt();
      Double lExpectedReqAircraftHoursTsn = lTrkHoursTsn + lWorkPackageAircraftHoursTsnDelta;
      assertEquals( String.format(
            "Unexpectedly, Child Requirement HOURS TSN value for TRK inventory= %s didn't get updated",
            lTrk ), lExpectedReqAircraftHoursTsn, lActualReqAircraftHoursTsn );
   }


   /**
    * Given a completed work package against an aircraft.
    *
    * AND the work package contains a usage snapshot for the aircraft
    *
    * AND the work package has a completed requirement against an attached aircraft component with a
    * usage snapshot
    *
    * When aircraft usage snapshot is edited and the value of a usage for the aircraft is modified.
    *
    * And updateSubTask check-box is unchecked
    *
    * THEN the usage snapshot for component requirement is not updated
    */
   @Test
   public void
         itDoesNotUpdateReqAgainstAcftComponentUsageWithinWorkPkgWhenWorkPkgAcftTsnUpdatedAndUpdateSubTaskFlagFalse()
               throws Exception {

      // GIVEN
      final InventoryKey lTrk = Domain.createTrackedInventory();
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addTracked( lTrk );
         }
      } );

      final double lAircraftHoursTsn = 5;
      final double lTrkHoursTsn = 3;
      final UsageSnapshot lAircraftWorkPackageUsageSnapshot =
            new UsageSnapshot( lAircraft, HOURS, lAircraftHoursTsn );

      final TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            final UsageSnapshot lTrkUsageSnapshot = new UsageSnapshot( lTrk, HOURS, lTrkHoursTsn );
            aReq.addUsage( lTrkUsageSnapshot );
            aReq.setInventory( lTrk );
            aReq.setStatus( RefEventStatusKey.COMPLETE );
         }

      } );

      TaskKey lWorkPackage = Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setStatus( RefEventStatusKey.COMPLETE );
            aWorkPackage.addTask( lReq );
            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.addUsageSnapshot( lAircraftWorkPackageUsageSnapshot );
         }

      } );

      EventKey lReqEventKey = lReq.getEventKey();
      final double lWorkPackageAircraftHoursTsnDelta = 2;

      final UsageSnapshot lAircraftWorkPackageUsageSnapshotEdited = new UsageSnapshot( lAircraft,
            HOURS, lAircraftHoursTsn + lWorkPackageAircraftHoursTsnDelta );
      UsageSnapshot[] lUsageSnapshotsEdited =
            new UsageSnapshot[] { lAircraftWorkPackageUsageSnapshotEdited };

      // WHEN
      new UpdateTaskDelegateService().modifyCompletionData( lWorkPackage, lAircraft,
            lUsageSnapshotsEdited, WORK_PACKAGE_COMPLETION_DATE, false, iHrKey, true );

      // Then
      EventInventoryKey lEventInventoryKey = new EventInventoryKey( lReqEventKey, 1 );
      EvtInvUsageTable lEvtInvUsageTable = EvtInvUsageTable
            .findByPrimaryKey( new EventInventoryUsageKey( lEventInventoryKey, HOURS ) );
      Double lActualReqAircraftHoursTsn = lEvtInvUsageTable.getTsnQt();
      Double lExpectedReqAircraftHoursTsn = lTrkHoursTsn;
      assertEquals( String.format(
            "Unexpectedly, Child Requirement HOURS TSN value for TRK inventory= %s got updated",
            lTrk ), lExpectedReqAircraftHoursTsn, lActualReqAircraftHoursTsn );
   }


   /**
    * Given a completed work package against an aircraft.
    *
    * AND the work package contains a usage snapshot for the aircraft and engine
    *
    * AND the work package has a completed requirement against the engine with a usage snapshot
    *
    * When engine usage snapshot is edited and the value of a usage for the engine is modified.
    *
    * THEN the usage snapshot for engine requirement snapshot gets modified correctly.
    */
   @Test
   public void itUpdatesReqAgainstSubAssyUsageWithinWorkPkgWhenWorkPkgSubAssyTsnUpdated()
         throws Exception {

      // GIVEN
      final InventoryKey lEngine = Domain.createEngine();
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addEngine( lEngine );
         }
      } );

      final double lAircraftHoursTsn = 5;
      final double lEngineHoursTsn = 3;

      final UsageSnapshot lAircraftWorkPackageUsageSnapshot =
            new UsageSnapshot( lAircraft, HOURS, lAircraftHoursTsn );
      final UsageSnapshot lEngineWorkPackageUsageSnapshot =
            new UsageSnapshot( lEngine, HOURS, lEngineHoursTsn );

      final TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            final UsageSnapshot lEngineUsageSnapshot =
                  new UsageSnapshot( lEngine, HOURS, lEngineHoursTsn );
            aReq.addUsage( lEngineUsageSnapshot );
            aReq.setInventory( lEngine );
            aReq.setStatus( RefEventStatusKey.COMPLETE );
         }

      } );

      TaskKey lWorkPackage = Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setStatus( RefEventStatusKey.COMPLETE );
            aWorkPackage.addTask( lReq );
            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.addUsageSnapshot( lAircraftWorkPackageUsageSnapshot );
            aWorkPackage.addUsageSnapshot( lEngineWorkPackageUsageSnapshot );
         }

      } );

      EventKey lReqEventKey = lReq.getEventKey();
      final double lWorkPackageEngineHoursTsnDelta = 2;

      final UsageSnapshot lWorkPackageUsageEngineSnapshotEdited =
            new UsageSnapshot( lEngine, HOURS, lEngineHoursTsn + lWorkPackageEngineHoursTsnDelta );

      UsageSnapshot[] lUsageSnapshotsEdited = new UsageSnapshot[] {
            lAircraftWorkPackageUsageSnapshot, lWorkPackageUsageEngineSnapshotEdited };

      // WHEN
      new UpdateTaskDelegateService().modifyCompletionData( lWorkPackage, lAircraft,
            lUsageSnapshotsEdited, WORK_PACKAGE_COMPLETION_DATE, true, iHrKey, true );

      // Then
      EventInventoryKey lEventInventoryKey = new EventInventoryKey( lReqEventKey, 1 );
      EvtInvUsageTable lEvtInvUsageTable = EvtInvUsageTable
            .findByPrimaryKey( new EventInventoryUsageKey( lEventInventoryKey, HOURS ) );
      Double lActualReqEngineHoursTsn = lEvtInvUsageTable.getTsnQt();
      Double lExpectedReqEngineHoursTsn = lEngineHoursTsn + lWorkPackageEngineHoursTsnDelta;
      assertEquals( String.format(
            "Unexpectedly, Child Requirement HOURS TSN value for SubAassy Engine = %s didn't get updated",
            lEngine ), lExpectedReqEngineHoursTsn, lActualReqEngineHoursTsn );
   }


   /**
    * Given a completed work package against an aircraft
    *
    * AND the work package contains a usage snapshot for the aircraft and engine
    *
    * AND the work package has a completed requirement against an engine system with a usage
    * snapshot
    *
    * When engine usage snapshot is edited and the value of a usage for the engine is modified.
    *
    * And updateSubTask check-box checked
    *
    * THEN the usage snapshot for system requirement gets modified correctly.
    */
   @Test
   public void
         itUpdatesReqAgainstSubAssySystemUsageWithinWorkPkgWhenWorkPkgSubAssyTsnUpdatedAndUpdateSubTaskFlagTrue()
               throws Exception {

      // GIVEN
      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.addSystem( "Engine System" );
         }
      } );
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addEngine( lEngine );
         }
      } );

      final double lAircraftHoursTsn = 5;
      final double lEngineHoursTsn = 3;
      final double lEngineSystemHoursTsn = 3;

      final UsageSnapshot lAircraftWorkPackageUsageSnapshot =
            new UsageSnapshot( lAircraft, HOURS, lAircraftHoursTsn );
      final UsageSnapshot lEngineWorkPackageUsageSnapshot =
            new UsageSnapshot( lEngine, HOURS, lEngineHoursTsn );

      final InventoryKey lEngineSystem = InvUtils.getSystemByName( lEngine, "Engine System" );

      final TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            final UsageSnapshot lEngineSystemUsageSnapshot =
                  new UsageSnapshot( lEngineSystem, HOURS, lEngineSystemHoursTsn );
            aReq.addUsage( lEngineSystemUsageSnapshot );
            aReq.setInventory( lEngineSystem );
            aReq.setStatus( RefEventStatusKey.COMPLETE );
         }

      } );

      TaskKey lWorkPackage = Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setStatus( RefEventStatusKey.COMPLETE );
            aWorkPackage.addTask( lReq );
            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.addUsageSnapshot( lAircraftWorkPackageUsageSnapshot );
            aWorkPackage.addUsageSnapshot( lEngineWorkPackageUsageSnapshot );
         }

      } );

      EventKey lReqEventKey = lReq.getEventKey();
      final double lWorkPackageEngineHoursTsnDelta = 2;

      final UsageSnapshot lWorkPackageUsageEngineSnapshotEdited =
            new UsageSnapshot( lEngine, HOURS, lEngineHoursTsn + lWorkPackageEngineHoursTsnDelta );

      UsageSnapshot[] lUsageSnapshotsEdited = new UsageSnapshot[] {
            lAircraftWorkPackageUsageSnapshot, lWorkPackageUsageEngineSnapshotEdited };

      // WHEN
      new UpdateTaskDelegateService().modifyCompletionData( lWorkPackage, lAircraft,
            lUsageSnapshotsEdited, WORK_PACKAGE_COMPLETION_DATE, true, iHrKey, true );

      // Then
      EventInventoryKey lEventInventoryKey = new EventInventoryKey( lReqEventKey, 1 );
      EvtInvUsageTable lEvtInvUsageTable = EvtInvUsageTable
            .findByPrimaryKey( new EventInventoryUsageKey( lEventInventoryKey, HOURS ) );
      Double lActualReqSystemHoursTsn = lEvtInvUsageTable.getTsnQt();
      Double lExpectedReqSystemHoursTsn = lEngineSystemHoursTsn + lWorkPackageEngineHoursTsnDelta;
      assertEquals( String.format(
            "Unexpectedly, Child Requirement HOURS TSN value for SubAssy System inventory= %s didn't get updated",
            lEngineSystem ), lExpectedReqSystemHoursTsn, lActualReqSystemHoursTsn );
   }


   /**
    * Given a completed work package against an aircraft
    *
    * AND the work package contains a usage snapshot for the aircraft and engine
    *
    * AND the work package has a completed requirement against an engine system with a usage
    * snapshot
    *
    * When engine usage snapshot is edited and the value of a usage for the engine is modified.
    *
    * And the updateSubTask check-box is unchecked
    *
    * THEN the usage snapshot for system requirement is not updated
    */
   @Test
   public void
         itDoesNotUpdateReqAgainstSubAssySystemUsageWithinWorkPkgWhenWorkPkgSubAssyTsnUpdatedAndUpdateSubTaskFlagFalse()
               throws Exception {

      // GIVEN
      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.addSystem( "Engine System" );
         }
      } );
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addEngine( lEngine );
         }
      } );

      final double lAircraftHoursTsn = 5;
      final double lEngineHoursTsn = 3;
      final double lEngineSystemHoursTsn = 3;
      final UsageSnapshot lAircraftWorkPackageUsageSnapshot =
            new UsageSnapshot( lAircraft, HOURS, lAircraftHoursTsn );
      final UsageSnapshot lEngineWorkPackageUsageSnapshot =
            new UsageSnapshot( lEngine, HOURS, lEngineHoursTsn );

      final InventoryKey lEngineSystem = InvUtils.getSystemByName( lEngine, "Engine System" );

      final TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            final UsageSnapshot lEngineSystemUsageSnapshot =
                  new UsageSnapshot( lEngineSystem, HOURS, lEngineSystemHoursTsn );
            aReq.addUsage( lEngineSystemUsageSnapshot );
            aReq.setInventory( lEngineSystem );
            aReq.setStatus( RefEventStatusKey.COMPLETE );
         }

      } );

      TaskKey lWorkPackage = Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setStatus( RefEventStatusKey.COMPLETE );
            aWorkPackage.addTask( lReq );
            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.addUsageSnapshot( lAircraftWorkPackageUsageSnapshot );
            aWorkPackage.addUsageSnapshot( lEngineWorkPackageUsageSnapshot );
         }

      } );

      EventKey lReqEventKey = lReq.getEventKey();
      final double lWorkPackageEngineHoursTsnDelta = 2;

      final UsageSnapshot lWorkPackageUsageEngineSnapshotEdited =
            new UsageSnapshot( lEngine, HOURS, lEngineHoursTsn + lWorkPackageEngineHoursTsnDelta );

      UsageSnapshot[] lUsageSnapshotsEdited = new UsageSnapshot[] {
            lAircraftWorkPackageUsageSnapshot, lWorkPackageUsageEngineSnapshotEdited };

      // WHEN
      new UpdateTaskDelegateService().modifyCompletionData( lWorkPackage, lAircraft,
            lUsageSnapshotsEdited, WORK_PACKAGE_COMPLETION_DATE, false, iHrKey, true );

      // Then
      EventInventoryKey lEventInventoryKey = new EventInventoryKey( lReqEventKey, 1 );
      EvtInvUsageTable lEvtInvUsageTable = EvtInvUsageTable
            .findByPrimaryKey( new EventInventoryUsageKey( lEventInventoryKey, HOURS ) );
      Double lActualReqSystemHoursTsn = lEvtInvUsageTable.getTsnQt();
      Double lExpectedReqSystemHoursTsn = lEngineSystemHoursTsn;
      assertEquals( String.format(
            "Unexpectedly, Child Requirement HOURS TSN value for SubAssy System inventory= %s got updated",
            lEngineSystem ), lExpectedReqSystemHoursTsn, lActualReqSystemHoursTsn );
   }


   /**
    * Given a completed work package against an aircraft.
    *
    * AND the work package contains a usage snapshot for the aircraft and engine
    *
    * AND the work package has a completed requirement against an engine component with a usage
    * snapshot
    *
    * When engine usage snapshot is edited and the value of a usage for the engine is modified.
    *
    * AND updateSubTask is checked
    *
    * THEN the usage snapshot for component requirement gets modified correctly.
    */
   @Test
   public void
         itUpdatesReqAgainstSubAssyComponentUsageWithinWorkPkgWhenWorkPkgSubAssyTsnUpdatedAndUpdateSubTaskFlagTrue()
               throws Exception {

      // GIVEN

      final InventoryKey lTrk = Domain.createTrackedInventory();
      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.addTracked( lTrk );
         }
      } );
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addEngine( lEngine );
         }
      } );

      final double lAircraftHoursTsn = 5;
      final double lEngineHoursTsn = 3;
      final double lTrkHoursTsn = 2;

      final UsageSnapshot lAircraftWorkPackageUsageSnapshot =
            new UsageSnapshot( lAircraft, HOURS, lAircraftHoursTsn );
      final UsageSnapshot lEngineWorkPackageUsageSnapshot =
            new UsageSnapshot( lEngine, HOURS, lEngineHoursTsn );

      final TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            final UsageSnapshot lTrkUsageSnapshot = new UsageSnapshot( lTrk, HOURS, lTrkHoursTsn );
            aReq.addUsage( lTrkUsageSnapshot );
            aReq.setInventory( lTrk );
            aReq.setStatus( RefEventStatusKey.COMPLETE );
         }

      } );

      TaskKey lWorkPackage = Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setStatus( RefEventStatusKey.COMPLETE );
            aWorkPackage.addTask( lReq );
            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.addUsageSnapshot( lAircraftWorkPackageUsageSnapshot );
            aWorkPackage.addUsageSnapshot( lEngineWorkPackageUsageSnapshot );
         }

      } );

      EventKey lReqEventKey = lReq.getEventKey();
      final double lWorkPackageEngineHoursTsnDelta = 2;

      final UsageSnapshot lWorkPackageUsageEngineSnapshotEdited =
            new UsageSnapshot( lEngine, HOURS, lEngineHoursTsn + lWorkPackageEngineHoursTsnDelta );

      UsageSnapshot[] lUsageSnapshotsEdited = new UsageSnapshot[] {
            lAircraftWorkPackageUsageSnapshot, lWorkPackageUsageEngineSnapshotEdited };

      // WHEN
      new UpdateTaskDelegateService().modifyCompletionData( lWorkPackage, lAircraft,
            lUsageSnapshotsEdited, WORK_PACKAGE_COMPLETION_DATE, true, iHrKey, true );

      // Then
      EventInventoryKey lEventInventoryKey = new EventInventoryKey( lReqEventKey, 1 );
      EvtInvUsageTable lEvtInvUsageTable = EvtInvUsageTable
            .findByPrimaryKey( new EventInventoryUsageKey( lEventInventoryKey, HOURS ) );
      Double lActualReqTrkHoursTsn = lEvtInvUsageTable.getTsnQt();
      Double lExpectedReqTrkHoursTsn = lTrkHoursTsn + lWorkPackageEngineHoursTsnDelta;
      assertEquals( String.format(
            "Unexpectedly, Child Requirement HOURS TSN value for SubAssy component inventory= %s didn't get updated",
            lTrk ), lExpectedReqTrkHoursTsn, lActualReqTrkHoursTsn );
   }


   /**
    * Given a completed work package against an aircraft.
    *
    * AND the work package contains a usage snapshot for an engine
    *
    * AND the work package has a completed requirement against an engine component with a usage
    * snapshot
    *
    * When engine usage snapshot is edited and the value of a usage for the engine is modified.
    *
    * And updateSubTask unchecked
    *
    * THEN the usage snapshot for component requirement it not updated
    */
   @Test
   public void
         itDoesNotUpdateReqAgainstSubAssyComponentUsageWithinWorkPkgWhenWorkPkgSubAssyTsnUpdatedAndUpdateSubTaskFlagFalse()
               throws Exception {

      // GIVEN

      final InventoryKey lTrk = Domain.createTrackedInventory();
      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.addTracked( lTrk );
         }
      } );
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addEngine( lEngine );
         }
      } );

      final double lAircraftHoursTsn = 5;
      final double lEngineHoursTsn = 3;
      final double lTrkHoursTsn = 2;

      final UsageSnapshot lAircraftWorkPackageUsageSnapshot =
            new UsageSnapshot( lAircraft, HOURS, lAircraftHoursTsn );
      final UsageSnapshot lEngineWorkPackageUsageSnapshot =
            new UsageSnapshot( lEngine, HOURS, lEngineHoursTsn );

      final TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            final UsageSnapshot lTrkUsageSnapshot = new UsageSnapshot( lTrk, HOURS, lTrkHoursTsn );
            aReq.addUsage( lTrkUsageSnapshot );
            aReq.setInventory( lTrk );
            aReq.setStatus( RefEventStatusKey.COMPLETE );
         }

      } );

      TaskKey lWorkPackage = Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setStatus( RefEventStatusKey.COMPLETE );
            aWorkPackage.addTask( lReq );
            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.addUsageSnapshot( lAircraftWorkPackageUsageSnapshot );
            aWorkPackage.addUsageSnapshot( lEngineWorkPackageUsageSnapshot );
         }

      } );

      EventKey lReqEventKey = lReq.getEventKey();
      final double lWorkPackageEngineHoursTsnDelta = 2;

      final UsageSnapshot lWorkPackageUsageEngineSnapshotEdited =
            new UsageSnapshot( lEngine, HOURS, lEngineHoursTsn + lWorkPackageEngineHoursTsnDelta );

      UsageSnapshot[] lUsageSnapshotsEdited = new UsageSnapshot[] {
            lAircraftWorkPackageUsageSnapshot, lWorkPackageUsageEngineSnapshotEdited };

      // WHEN
      new UpdateTaskDelegateService().modifyCompletionData( lWorkPackage, lAircraft,
            lUsageSnapshotsEdited, WORK_PACKAGE_COMPLETION_DATE, false, iHrKey, true );

      // Then
      EventInventoryKey lEventInventoryKey = new EventInventoryKey( lReqEventKey, 1 );
      EvtInvUsageTable lEvtInvUsageTable = EvtInvUsageTable
            .findByPrimaryKey( new EventInventoryUsageKey( lEventInventoryKey, HOURS ) );
      Double lActualReqTrkHoursTsn = lEvtInvUsageTable.getTsnQt();
      Double lExpectedReqTrkHoursTsn = lTrkHoursTsn;
      assertEquals( String.format(
            "Unexpectedly, Child Requirement HOURS TSN value for SubAssy component inventory= %s got updated",
            lTrk ), lExpectedReqTrkHoursTsn, lActualReqTrkHoursTsn );
   }


   @Before
   public void setup() {
      iHrKey = new HumanResourceDomainBuilder().withUsername( "HR_USERNAME" ).build();

      iUserId = OrgHr.findByPrimaryKey( iHrKey ).getUserId();
      UserParameters.setInstance( iUserId, "LOGIC", new UserParametersFake( iUserId, "LOGIC" ) );
   }


   @After
   public void teardown() {
      UserParameters.setInstance( iUserId, "LOGIC", null );
   }
}
