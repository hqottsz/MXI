package com.mxi.mx.core.services.event.inventory.usagesnapshotservice;

import static com.mxi.mx.core.key.DataTypeKey.CYCLES;
import static com.mxi.mx.core.key.DataTypeKey.HOURS;
import static com.mxi.mx.core.key.DataTypeKey.LANDING;
import static com.mxi.mx.core.usage.model.UsageType.MXCORRECTION;
import static com.mxi.mx.core.usage.model.UsageType.MXFLIGHT;
import static java.math.BigDecimal.valueOf;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.AccumulatedParameter;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.AircraftAssembly;
import com.mxi.am.domain.CalculatedUsageParameter;
import com.mxi.am.domain.ConfigurationSlot;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Engine;
import com.mxi.am.domain.EngineAssembly;
import com.mxi.am.domain.Part;
import com.mxi.am.domain.PartGroup;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.SerializedInventory;
import com.mxi.am.domain.TrackedInventory;
import com.mxi.am.domain.UsageAdjustment;
import com.mxi.am.domain.UsageDefinition;
import com.mxi.am.domain.WorkPackage;
import com.mxi.am.domain.builder.AttachmentService;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventInventoryUsageKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.event.inventory.UsageSnapshot;
import com.mxi.mx.core.services.event.inventory.UsageSnapshotService;
import com.mxi.mx.core.table.eqp.EqpAssmblBom;
import com.mxi.mx.core.table.evt.EvtInvTable;
import com.mxi.mx.core.table.evt.EvtInvUsageTable;
import com.mxi.mx.core.usage.model.UsageType;


/**
 * Test cases for
 * {@link UsageSnapshotService#takeUsageSnapshotOfEvent(com.mxi.mx.core.key.EventKeyInterface)}
 *
 */
public class TakeUsageSnapshotOfEventTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final String SYSTEM_NAME = "SYSTEM_NAME";
   private static final String ASSEMBLY_CODE = "A320";
   private static final String CALC_USAGE_PARM_CODE = "CALC_USAGE_PARM_CODE";
   private static final String CALCULATION = "CALCULATION";
   private static final String CALCULATION_CONSTANT = "CONSTANT";
   private static final String ACFT_ENGINE_CONFIG_SLOT_CODE = "ACFTENG";
   private static final BigDecimal CALCULATION_CONSTANT_VALUE = BigDecimal.valueOf( 2.0 );
   private static final String ACC_CYCLES_LOW_THRUST_RATING = "ACC_CYCLES_LOW_THRUST_RATING";
   private static final String ACC_CYCLES_HIGH_THRUST_RATING = "ACC_CYCLES_HIGH_THRUST_RATING";


   /**
    *
    * Verify that a usage snapshot is taken when there are no usage adjustments against an aircraft.
    * The snapshot will reflect the current usage of the aircraft.
    *
    * @throws Exception
    */
   @Test
   public void forAcft_whenNoAdjustments() throws Exception {

      // Set up
      // - event: Jan-01
      // - current tsn: 0

      final Date lEventDate = toDate( "01-JAN-2010" );
      final BigDecimal lCurrentTsn = valueOf( 0.0 );

      // Given an aircraft with a system.
      final InventoryKey lAcft = createAircraft( lCurrentTsn );

      // Given an event before the adjustment.
      TaskKey lReq = createRequirement( lAcft, lEventDate );

      // When a usage snapshot is attempted to be taken for the event.
      UsageSnapshotService.takeUsageSnapshotOfEvent( lReq.getEventKey() );

      // Then the usage snapshot reflects the current usage of the system.
      BigDecimal lExpectedTsn = lCurrentTsn;
      BigDecimal lActualTsn = getUsageOfSnapshot( lReq, lAcft, HOURS );
      assertEquals( "Unexpected tsn in usage snapshot of event.", lExpectedTsn, lActualTsn );
   }


   /**
    *
    * Verify that a usage snapshot is taken when there are only usage adjustments before an event
    * against an aircraft. The snapshot will reflect the current usage of the aircraft.
    *
    * @throws Exception
    */
   @Test
   public void forAcft_whenAdjustmentBeforeEvent() throws Exception {

      // Set up
      // - adjustment 1: Jan-01, delta=1 tsn=1
      // - event: Jan-02
      // - current tsn: 1

      final Date lAdjustmentDate1 = toDate( "01-JAN-2010" );
      final BigDecimal lAdjustmentDelta1 = valueOf( 1.0 );
      final BigDecimal lAdjustmentTsn1 = valueOf( 1.0 );
      final Date lEventDate = toDate( "02-JAN-2010" );
      final BigDecimal lCurrentTsn = valueOf( 1.0 );

      // Given an aircraft.
      final InventoryKey lAcft = createAircraft( lCurrentTsn );

      // Given a usage adjustment before the event.
      createFlight( lAcft, lAdjustmentDate1, lAdjustmentTsn1, lAdjustmentDelta1 );

      // Given an event after the adjustment.
      TaskKey lReq = createRequirement( lAcft, lEventDate );

      // When a usage snapshot is taken for the event.
      UsageSnapshotService.takeUsageSnapshotOfEvent( lReq.getEventKey() );

      // Then the usage snapshot reflects the usage at the time of the event,
      // which is the usage of the first adjustment.
      BigDecimal lExpectedTsn = lAdjustmentTsn1;
      BigDecimal lActualTsn = getUsageOfSnapshot( lReq, lAcft, HOURS );
      assertEquals( "Unexpected tsn in usage snapshot of event.", lExpectedTsn, lActualTsn );
   }


   /**
    *
    * Verify that a usage snapshot is taken at the same date and time as a usage record against an
    * aircraft, the snapshot will reflect the current usage of the aircraft.
    *
    * This is because for event at time T or later, we expect the usage being captured to match the
    * usage after. Events at time T-1 should reflect usage before.
    *
    * @throws Exception
    */
   @Test
   public void forAcft_whenAdjustmentAtSameTimeAsEvent() throws Exception {

      // Set up
      // - adjustment 1: Jan-01, delta=1 tsn=1
      // - event: Jan-01
      // - current tsn: 1
      // - before tsn: 0

      final Date lAdjustmentDate1 = toDate( "01-JAN-2010" );
      final BigDecimal lAdjustmentDelta1 = valueOf( 1.0 );
      final BigDecimal lAdjustmentTsn1 = valueOf( 1.0 );
      final Date lEventDate = toDate( "01-JAN-2010" );
      final BigDecimal lCurrentTsn = valueOf( 1.0 );

      // Given an aircraft.
      final InventoryKey lAcft = createAircraft( lCurrentTsn );

      // Given a usage adjustment before the event.
      createFlight( lAcft, lAdjustmentDate1, lAdjustmentTsn1, lAdjustmentDelta1 );

      // Given an event after the adjustment.
      TaskKey lReq = createRequirement( lAcft, lEventDate );

      // When a usage snapshot is taken for the event.
      UsageSnapshotService.takeUsageSnapshotOfEvent( lReq.getEventKey() );

      // Then the usage snapshot reflects the usage at the time of the event,
      // which is the usage of the first adjustment.
      BigDecimal lExpectedTsn = lAdjustmentTsn1;
      BigDecimal lActualTsn = getUsageOfSnapshot( lReq, lAcft, HOURS );
      assertEquals( "Unexpected tsn in usage snapshot of event.", lExpectedTsn, lActualTsn );
   }


   /**
    *
    * Verify that a usage snapshot is taken when there are only usage adjustments after an event
    * against an aircraft. The snapshot will reflect the TSN minus the delta of the usage adjustment
    * immediately after the event.
    *
    * @throws Exception
    */
   @Test
   public void forAcft_whenAdjustmentAfterEvent() throws Exception {

      // Set up
      // - event: Jan-01
      // - adjustment 1: Jan-02, delta=1 tsn=1
      // - current tsn: 1

      final Date lEventDate = toDate( "01-JAN-2010" );
      final Date lAdjustmentDate = toDate( "02-JAN-2010" );
      final BigDecimal lAdjustmentDelta = valueOf( 1.0 );
      final BigDecimal lAdjustmentTsn = valueOf( 1.0 );
      final BigDecimal lCurrentTsn = valueOf( 1.0 );

      // Given an aircraft.
      final InventoryKey lAcft = createAircraft( lCurrentTsn );

      // Given an event before the adjustment.
      TaskKey lReq = createRequirement( lAcft, lEventDate );

      // Given a usage adjustment after the event.
      createFlight( lAcft, lAdjustmentDate, lAdjustmentTsn, lAdjustmentDelta );

      // When a usage snapshot is taken for the event.
      UsageSnapshotService.takeUsageSnapshotOfEvent( lReq.getEventKey() );

      // Then the usage snapshot reflects the usage at the time of the event,
      // which is the TSN-delta of the adjustment immediately after the event.
      BigDecimal lExpectedTsn = lAdjustmentTsn.subtract( lAdjustmentDelta );
      BigDecimal lActualTsn = getUsageOfSnapshot( lReq, lAcft, HOURS );
      assertEquals( "Unexpected tsn in usage snapshot of event.", lExpectedTsn, lActualTsn );
   }


   /**
    *
    * Verify that a usage snapshot is taken when there are usage adjustments before and after an
    * event against an aircraft. The snapshot will reflect the TSN minus the delta of the usage
    * adjustment immediately after the event.
    *
    * @throws Exception
    */
   @Test
   public void forAcft_whenAdjustmentBeforeAndAfterEvent() throws Exception {

      // Set up
      // - adjustment 1: Jan-01, delta=1 tsn=1
      // - event: Jan-02
      // - adjustment 2: Jan-03, delta=2 tsn=3
      // - current tsn: 3

      final Date lAdjustmentDate1 = toDate( "01-JAN-2010" );
      final BigDecimal lAdjustmentDelta1 = valueOf( 1.0 );
      final BigDecimal lAdjustmentTsn1 = valueOf( 1.0 );
      final Date lEventDate = toDate( "02-JAN-2010" );
      final Date lAdjustmentDate2 = toDate( "03-JAN-2010" );
      final BigDecimal lAdjustmentDelta2 = valueOf( 2.0 );
      final BigDecimal lAdjustmentTsn2 = valueOf( 3.0 );
      final BigDecimal lCurrentTsn = valueOf( 3.0 );

      // Given an aircraft with a system.
      final InventoryKey lAcft = createAircraft( lCurrentTsn );

      // Given a usage adjustment before the event.
      createFlight( lAcft, lAdjustmentDate1, lAdjustmentTsn1, lAdjustmentDelta1 );

      // Given an event after the adjustment.
      TaskKey lReq = createRequirement( lAcft, lEventDate );

      // Given another usage adjustment after the event.
      createFlight( lAcft, lAdjustmentDate2, lAdjustmentTsn2, lAdjustmentDelta2 );

      // When a usage snapshot is taken for the event.
      UsageSnapshotService.takeUsageSnapshotOfEvent( lReq.getEventKey() );

      // Then the usage snapshot reflects the usage at the time of the event,
      // which is the TSN-delta of the adjustment immediately after the event.
      BigDecimal lExpectedTsn = lAdjustmentTsn2.subtract( lAdjustmentDelta2 );
      BigDecimal lActualTsn = getUsageOfSnapshot( lReq, lAcft, HOURS );
      assertEquals( "Unexpected tsn in usage snapshot of event.", lExpectedTsn, lActualTsn );
   }


   /**
    *
    * Verify that a usage snapshot is taken for an event against an aircraft when the adjustments
    * before the event consist of all the various types of adjustments. The snapshot will reflect
    * the TSN of the last snapshot before the event.
    *
    * @throws Exception
    */
   @Test
   public void forAcft_whenAllTypesOfAdjustmentsBeforeEvent() throws Exception {

      // Set up
      // - adjustment 1: Jan-01, delta=1 tsn=1 (correction)
      // - adjustment 2: Jan-02, delta=2 tsn=3 (flight)
      // - adjustment 2: Jan-03, delta=3 tsn=6 (accrual)
      // - event: Jan-04
      // - adjustment 1: Jan-05, delta=4 tsn=10 (correction)
      // - adjustment 2: Jan-06, delta=5 tsn=15 (flight)
      // - adjustment 2: Jan-07, delta=6 tsn=21 (accrual)
      // - current tsn: 6

      final Date lAdjustmentDate1 = toDate( "01-JAN-2010" );
      final BigDecimal lAdjustmentDelta1 = valueOf( 1.0 );
      final BigDecimal lAdjustmentTsn1 = valueOf( 1.0 );
      final Date lAdjustmentDate2 = toDate( "02-JAN-2010" );
      final BigDecimal lAdjustmentDelta2 = valueOf( 2.0 );
      final BigDecimal lAdjustmentTsn2 = valueOf( 3.0 );
      final Date lAdjustmentDate3 = toDate( "03-JAN-2010" );
      final BigDecimal lAdjustmentDelta3 = valueOf( 3.0 );
      final BigDecimal lAdjustmentTsn3 = valueOf( 6.0 );
      final Date lEventDate = toDate( "04-JAN-2010" );
      final Date lAdjustmentDate4 = toDate( "05-JAN-2010" );
      final BigDecimal lAdjustmentDelta4 = valueOf( 4.0 );
      final BigDecimal lAdjustmentTsn4 = valueOf( 10.0 );
      final Date lAdjustmentDate5 = toDate( "06-JAN-2010" );
      final BigDecimal lAdjustmentDelta5 = valueOf( 5.0 );
      final BigDecimal lAdjustmentTsn5 = valueOf( 15.0 );
      final Date lAdjustmentDate6 = toDate( "07-JAN-2010" );
      final BigDecimal lAdjustmentDelta6 = valueOf( 6.0 );
      final BigDecimal lAdjustmentTsn6 = valueOf( 21.0 );
      final BigDecimal lCurrentTsn = valueOf( 21.0 );

      // Given an aircraft.
      final InventoryKey lAcft = createAircraft( lCurrentTsn );

      // Given a CORRECTION usage adjustment before the event.
      createCorrection( lAcft, lAdjustmentDate1, lAdjustmentTsn1, lAdjustmentDelta1 );

      // Given a FLIGHT usage adjustment after the event.
      createFlight( lAcft, lAdjustmentDate2, lAdjustmentTsn2, lAdjustmentDelta2 );

      // Given an ACCRUAL usage adjustment after the event.
      createAccrual( lAcft, lAdjustmentDate3, lAdjustmentTsn3, lAdjustmentDelta3 );

      // Given an event after the adjustment.
      TaskKey lReq = createRequirement( lAcft, lEventDate );

      // Given a CORRECTION usage adjustment after the event.
      createCorrection( lAcft, lAdjustmentDate4, lAdjustmentTsn4, lAdjustmentDelta4 );

      // Given a FLIGHT usage adjustment after the event.
      createFlight( lAcft, lAdjustmentDate5, lAdjustmentTsn5, lAdjustmentDelta5 );

      // Given an ACCRUAL usage adjustment after the event.
      createAccrual( lAcft, lAdjustmentDate6, lAdjustmentTsn6, lAdjustmentDelta6 );

      // When a usage snapshot is taken for the event.
      UsageSnapshotService.takeUsageSnapshotOfEvent( lReq.getEventKey() );

      // Then the usage snapshot reflects the usage at the time of the event,
      // which is the TSN-delta of the adjustment immediately after the event.
      BigDecimal lExpectedTsn = lAdjustmentTsn4.subtract( lAdjustmentDelta4 );
      BigDecimal lActualTsn = getUsageOfSnapshot( lReq, lAcft, HOURS );
      assertEquals( "Unexpected tsn in usage snapshot of event.", lExpectedTsn, lActualTsn );
   }


   /**
    *
    * Verify that the usage snapshot of an event captures all the usage data types tracked by the
    * aircraft.
    *
    */
   @Test
   public void forAcft_whenTrackingManyDataTypesAndAdjustmentsBeforeEvent() throws Exception {

      // Set up
      // - aircraft tracking HOURS and CYCLES
      // - event: Jan-01
      // - adjustment 1: Jan-02, HOURS delta=10 tsn=10, CYCLES delta=1 tsn=1
      // - current: HOURS tsn=10, CYCLES tsn=1

      final Date lEventDate = toDate( "01-JAN-2010" );
      final Date lAdjustmentDate = toDate( "02-JAN-2010" );
      final BigDecimal lAdjustmentHoursDelta = valueOf( 10.0 );
      final BigDecimal lAdjustmentHoursTsn = valueOf( 10.0 );
      final BigDecimal lAdjustmentCyclesDelta = valueOf( 1.0 );
      final BigDecimal lAdjustmentCyclesTsn = valueOf( 1.0 );
      final BigDecimal lCurrentHoursTsn = valueOf( 10.0 );
      final BigDecimal lCurrentCyclesTsn = valueOf( 1.0 );

      // Given an aircraft tracking many usage s.
      final InventoryKey lAcft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.addUsage( HOURS, lCurrentHoursTsn );
            aBuilder.addUsage( CYCLES, lCurrentCyclesTsn );
         }
      } );

      // Given a usage adjustment before the event.
      Domain.createUsageAdjustment( new DomainConfiguration<UsageAdjustment>() {

         @Override
         public void configure( UsageAdjustment aBuilder ) {
            aBuilder.setMainInventory( lAcft );
            aBuilder.setUsageDate( lAdjustmentDate );
            aBuilder.setUsageType( MXFLIGHT );
            aBuilder.addUsage( lAcft, HOURS, lAdjustmentHoursTsn, lAdjustmentHoursDelta );
            aBuilder.addUsage( lAcft, CYCLES, lAdjustmentCyclesTsn, lAdjustmentCyclesDelta );
         }
      } );

      // Given an event after the adjustment.
      TaskKey lReq = createRequirement( lAcft, lEventDate );

      // When a usage snapshot is taken for the event.
      UsageSnapshotService.takeUsageSnapshotOfEvent( lReq.getEventKey() );

      // Then the usage snapshot contains all usage data types and reflects the usage at the time of
      // the event, which is the TSN-delta of the adjustment immediately after the event.
      BigDecimal lExpectedHoursTsn = lAdjustmentHoursTsn.subtract( lAdjustmentHoursDelta );
      BigDecimal lActualHoursTsn = getUsageOfSnapshot( lReq, lAcft, HOURS );
      assertEquals( "Unexpected HOURS tsn in usage snapshot of event.", lExpectedHoursTsn,
            lActualHoursTsn );

      BigDecimal lExpectedCyclesTsn = lAdjustmentCyclesTsn.subtract( lAdjustmentCyclesDelta );
      BigDecimal lActualCyclesTsn = getUsageOfSnapshot( lReq, lAcft, CYCLES );
      assertEquals( "Unexpected CYCLES tsn in usage snapshot of event.", lExpectedCyclesTsn,
            lActualCyclesTsn );
   }


   /**
    *
    * Verify that the usage snapshot of a work package against an aircraft captures the usage for
    * any sub-assemblies at the time of the work package.
    *
    * Note: work packages, unlike tasks, may capture the usage of more than one inventory (e.g.
    * against an aircraft with installed engines).
    *
    */
   @Test
   public void forAcft_whenAdjustmentsBeforeWorkPackageAndInstalledSubassembly() throws Exception {

      // Set up
      // - adjustment 1 acft: Jan-01, delta=1, acft tsn=1
      // - adjustment 2 engine1: Jan-02, delta=2, engine1 tsn=2
      // - adjustment 3 engine2: Jan-03, delta=3, engine2 tsn=3
      // - engine1 install record: Jan-04, engine1 tsn=2
      // - adjustment 4 acft: Jan-05, delta=4, acft tsn=5, engine1 tsn=6
      // - work package event: Jan-06
      // - adjustment 5 acft: Jan-07, delta=5, acft tsn=10, engine1 tsn=11
      // - engine1 remove record: Jan-08, tsn=11
      // - engine2 install record: Jan-09, tsn=3
      // - adjustment 6 acft: Jan-10, delta=6, acft tsn=16, engine2 tsn=9
      // - acft current: tsn=16
      // - engine1 current: tsn=11
      // - engine2 current: tsn=9

      final Date lAdjustmentDate1 = toDate( "01-JAN-2010" );
      final BigDecimal lAdjustmentDelta1 = valueOf( 1.0 );
      final BigDecimal lAdjustmentTsn1 = valueOf( 1.0 );
      final Date lAdjustmentDate2 = toDate( "02-JAN-2010" );
      final BigDecimal lAdjustmentDelta2 = valueOf( 2.0 );
      final BigDecimal lAdjustmentTsn2 = valueOf( 2.0 );
      final Date lAdjustmentDate3 = toDate( "03-JAN-2010" );
      final BigDecimal lAdjustmentDelta3 = valueOf( 3.0 );
      final BigDecimal lAdjustmentTsn3 = valueOf( 3.0 );
      final Date lInstallDate1 = toDate( "04-JAN-2010" );
      final BigDecimal lInstallTsn1 = valueOf( 0.0 );
      final Date lAdjustmentDate4 = toDate( "05-JAN-2010" );
      final BigDecimal lAdjustmentAcftDelta4 = valueOf( 4.0 );
      final BigDecimal lAdjustmentAcftTsn4 = valueOf( 5.0 );
      final BigDecimal lAdjustmentEngDelta4 = valueOf( 4.0 );
      final BigDecimal lAdjustmentEngTsn4 = valueOf( 6.0 );
      final Date lEventDate = toDate( "06-JAN-2010" );
      final Date lAdjustmentDate5 = toDate( "07-JAN-2010" );
      final BigDecimal lAdjustmentAcftDelta5 = valueOf( 5.0 );
      final BigDecimal lAdjustmentAcftTsn5 = valueOf( 10.0 );
      final BigDecimal lAdjustmentEngDelta5 = valueOf( 5.0 );
      final BigDecimal lAdjustmentEngTsn5 = valueOf( 11.0 );
      final Date lRemoveDate1 = toDate( "08-JAN-2010" );
      final BigDecimal lRemoveTsn1 = valueOf( 11.0 );
      final Date lInstallDate2 = toDate( "09-JAN-2010" );
      final BigDecimal lInstallTsn2 = valueOf( 3.0 );
      final Date lAdjustmentDate6 = toDate( "10-JAN-2010" );
      final BigDecimal lAdjustmentAcftDelta6 = valueOf( 6.0 );
      final BigDecimal lAdjustmentAcftTsn6 = valueOf( 16.0 );
      final BigDecimal lAdjustmentEngDelta6 = valueOf( 6.0 );
      final BigDecimal lAdjustmentEngTsn6 = valueOf( 9.0 );
      final BigDecimal lAcftCurrentTsn = valueOf( 16.0 );
      final BigDecimal lEngine1CurrentTsn = valueOf( 11.0 );
      final BigDecimal lEngine2CurrentTsn = valueOf( 9.0 );

      // Given an aircraft to install the engine.
      final InventoryKey lAcft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.addUsage( HOURS, lAcftCurrentTsn );
         }
      } );

      // Given an engine.
      final InventoryKey lEngine1 = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aBuilder ) {
            aBuilder.addUsage( HOURS, lEngine1CurrentTsn );
         }
      } );

      // Given another engine.
      final InventoryKey lEngine2 = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aBuilder ) {
            aBuilder.addUsage( HOURS, lEngine2CurrentTsn );
         }
      } );

      // Given a usage adjustment for aircraft before installs.
      createCorrection( lAcft, lAdjustmentDate1, lAdjustmentTsn1, lAdjustmentDelta1 );

      // Given a usage adjustment for engine 1 before installs.
      createCorrection( lEngine1, lAdjustmentDate2, lAdjustmentTsn2, lAdjustmentDelta2 );

      // Given a usage adjustment for engine 2 before installs.
      createCorrection( lEngine2, lAdjustmentDate3, lAdjustmentTsn3, lAdjustmentDelta3 );

      // Given the install record for engine 1.
      createInstallRecord( lAcft, lEngine1, lInstallDate1, lInstallTsn1 );

      // Given a usage adjustment while first engine installed.
      Domain.createUsageAdjustment( new DomainConfiguration<UsageAdjustment>() {

         @Override
         public void configure( UsageAdjustment aBuilder ) {
            aBuilder.setMainInventory( lAcft );
            aBuilder.setUsageDate( lAdjustmentDate4 );
            aBuilder.setUsageType( MXFLIGHT );
            aBuilder.addUsage( lAcft, HOURS, lAdjustmentAcftTsn4, lAdjustmentAcftDelta4 );
            aBuilder.addUsage( lEngine1, HOURS, lAdjustmentEngTsn4, lAdjustmentEngDelta4 );
         }
      } );

      // Given a work package against the aircraft after the first engine was installed and after a
      // usage adjustment.
      TaskKey lWorkPackage = Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aBuilder ) {
            aBuilder.setActualEndDate( lEventDate );
            aBuilder.setAircraft( lAcft );
            aBuilder.addSubAssembly( lEngine1 );
         }
      } );

      // Given another usage adjustment while first engine installed.
      Domain.createUsageAdjustment( new DomainConfiguration<UsageAdjustment>() {

         @Override
         public void configure( UsageAdjustment aBuilder ) {
            aBuilder.setMainInventory( lAcft );
            aBuilder.setUsageDate( lAdjustmentDate5 );
            aBuilder.setUsageType( MXFLIGHT );
            aBuilder.addUsage( lAcft, HOURS, lAdjustmentAcftTsn5, lAdjustmentAcftDelta5 );
            aBuilder.addUsage( lEngine1, HOURS, lAdjustmentEngTsn5, lAdjustmentEngDelta5 );
         }
      } );

      // Given the remove record for engine 1.
      createRemoveRecord( lAcft, lEngine1, lRemoveDate1, lRemoveTsn1 );

      // Given the install record for engine 2.
      createInstallRecord( lAcft, lEngine2, lInstallDate2, lInstallTsn2 );

      // Given a usage adjustment while second engine installed.
      Domain.createUsageAdjustment( new DomainConfiguration<UsageAdjustment>() {

         @Override
         public void configure( UsageAdjustment aBuilder ) {
            aBuilder.setMainInventory( lAcft );
            aBuilder.setUsageDate( lAdjustmentDate6 );
            aBuilder.setUsageType( MXFLIGHT );
            aBuilder.addUsage( lAcft, HOURS, lAdjustmentAcftTsn6, lAdjustmentAcftDelta6 );
            aBuilder.addUsage( lEngine2, HOURS, lAdjustmentEngTsn6, lAdjustmentEngDelta6 );
         }
      } );

      // When a usage snapshot is taken for the event.
      UsageSnapshotService.takeUsageSnapshotOfEvent( lWorkPackage.getEventKey() );

      // Then the usage snapshot reflects the usage of only the aircraft and engine 1, which was
      // installed, at the time of the event.
      // The usage for the aircraft and engine 1 is the usage of the first adjustment when engine 1
      // was installed.
      BigDecimal lExpectedAcftTsn = lAdjustmentAcftTsn4;
      BigDecimal lExpectedEngine1Tsn = lAdjustmentEngTsn4;

      BigDecimal lActualAcftTsn = getUsageOfSnapshot( lWorkPackage, lAcft, HOURS );
      BigDecimal lActualEngine1Tsn = getUsageOfSnapshot( lWorkPackage, lEngine1, HOURS );

      assertEquals( "Unexpected aircraft tsn in usage snapshot of event.", lExpectedAcftTsn,
            lActualAcftTsn );
      assertEquals( "Unexpected engine1 tsn in usage snapshot of event.", lExpectedEngine1Tsn,
            lActualEngine1Tsn );

      // Then there is no usage in the snapshot for engine 2 because it was not installed at the
      // time of the event.
      org.junit.Assert.assertNull( "Unexpected usage in the work package snapshot for engine 1.",
            getUsageOfSnapshot( lWorkPackage, lEngine2, HOURS ) );

   }


   /**
    *
    * Verify that a usage snapshot is taken when there are many usage adjustments, each collecting
    * different data types, before an event against an aircraft.
    *
    * The snapshot will reflect the TSN of all the data types and their TSN values will be taken
    * from the last adjustment in which the data type was found.
    *
    * Note: usage adjustments need not always contain all the data types, they may contain only
    * those data types whose values have changed.
    *
    * <pre>
    * Example:
    *   usage1 has HOURS  tsn=3
    *   usage2 has CYCLES tsn=2
    *   usage3 has HOURS  tsn=5 and LANDINGS tsn=4
    *
    *   If the usages were all in that chronological order with an event occurred after all of them,
    *   then the event snapshot would show HOURS tsn=5, CYCLES tsn=2, and LANDINGS tsn=4
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void forAcft_whenAdjustmentsBeforeEventWithDifferentDataTypes() throws Exception {

      // Set up
      // - adjustment 1: Jan-01, HOURS delta=1 tsn=1
      // - adjustment 2: Jan-02, CYCLES delta=2 tsn=2
      // - adjustment 3: Jan-03, HOURS delta=3 tsn=4, LANDING delta=4 tsn=4
      // - event: Jan-04
      // - adjustment 4: Jan-05, HOURS delta=5 tsn=9
      // - adjustment 5: Jan-06, CYCLES delta=6 tsn=8
      // - adjustment 6: Jan-07, HOURS delta=7 tsn=16, LANDING delta=8 tsn=12
      // - adjustment 7: Jan-08, HOURS delta=9 tsn=25, CYCLES delta=10 tsn=18, LANDING delta=11
      // tsn=23
      // - current: HOURS tsn=25, CYCLES tsn=18, LANDING tsn=23

      final Date lAdjustmentDate1 = toDate( "01-JAN-2010" );
      final BigDecimal lAdjustmentHoursDelta1 = valueOf( 1.0 );
      final BigDecimal lAdjustmentHoursTsn1 = valueOf( 1.0 );
      final Date lAdjustmentDate2 = toDate( "02-JAN-2010" );
      final BigDecimal lAdjustmentCyclesDelta2 = valueOf( 2.0 );
      final BigDecimal lAdjustmentCyclesTsn2 = valueOf( 2.0 );
      final Date lAdjustmentDate3 = toDate( "03-JAN-2010" );
      final BigDecimal lAdjustmentHoursDelta3 = valueOf( 3.0 );
      final BigDecimal lAdjustmentHoursTsn3 = valueOf( 4.0 );
      final BigDecimal lAdjustmentLandingDelta3 = valueOf( 4.0 );
      final BigDecimal lAdjustmentLandingTsn3 = valueOf( 4.0 );
      final Date lEventDate = toDate( "04-JAN-2010" );
      final Date lAdjustmentDate4 = toDate( "05-JAN-2010" );
      final BigDecimal lAdjustmentHoursDelta4 = valueOf( 5.0 );
      final BigDecimal lAdjustmentHoursTsn4 = valueOf( 9.0 );
      final Date lAdjustmentDate5 = toDate( "06-JAN-2010" );
      final BigDecimal lAdjustmentCyclesDelta5 = valueOf( 6.0 );
      final BigDecimal lAdjustmentCyclesTsn5 = valueOf( 8.0 );
      final Date lAdjustmentDate6 = toDate( "07-JAN-2010" );
      final BigDecimal lAdjustmentHoursDelta6 = valueOf( 7.0 );
      final BigDecimal lAdjustmentHoursTsn6 = valueOf( 16.0 );
      final BigDecimal lAdjustmentLandingDelta6 = valueOf( 8.0 );
      final BigDecimal lAdjustmentLandingTsn6 = valueOf( 12.0 );
      final Date lAdjustmentDate7 = toDate( "08-JAN-2010" );
      final BigDecimal lAdjustmentHoursDelta7 = valueOf( 9.0 );
      final BigDecimal lAdjustmentHoursTsn7 = valueOf( 25.0 );
      final BigDecimal lAdjustmentCyclesDelta7 = valueOf( 10.0 );
      final BigDecimal lAdjustmentCyclesTsn7 = valueOf( 18.0 );
      final BigDecimal lAdjustmentLandingDelta7 = valueOf( 11.0 );
      final BigDecimal lAdjustmentLandingTsn7 = valueOf( 23.0 );
      final BigDecimal lCurrentHoursTsn = valueOf( 25.0 );
      final BigDecimal lCurrentCyclesTsn = valueOf( 18.0 );
      final BigDecimal lCurrentLandingTsn = valueOf( 23.0 );

      // Given an aircraft.
      final InventoryKey lAcft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.addUsage( HOURS, lCurrentHoursTsn );
            aBuilder.addUsage( CYCLES, lCurrentCyclesTsn );
            aBuilder.addUsage( LANDING, lCurrentLandingTsn );
         }
      } );

      // Given a usage adjustment before the event.
      Domain.createUsageAdjustment( new DomainConfiguration<UsageAdjustment>() {

         @Override
         public void configure( UsageAdjustment aBuilder ) {
            aBuilder.setMainInventory( lAcft );
            aBuilder.setUsageDate( lAdjustmentDate1 );
            aBuilder.setUsageType( MXFLIGHT );
            aBuilder.addUsage( lAcft, HOURS, lAdjustmentHoursTsn1, lAdjustmentHoursDelta1 );
         }
      } );

      // Given another usage adjustment before the event.
      Domain.createUsageAdjustment( new DomainConfiguration<UsageAdjustment>() {

         @Override
         public void configure( UsageAdjustment aBuilder ) {
            aBuilder.setMainInventory( lAcft );
            aBuilder.setUsageDate( lAdjustmentDate2 );
            aBuilder.setUsageType( MXFLIGHT );
            aBuilder.addUsage( lAcft, CYCLES, lAdjustmentCyclesTsn2, lAdjustmentCyclesDelta2 );
         }
      } );

      // Given yet another usage adjustment before the event.
      Domain.createUsageAdjustment( new DomainConfiguration<UsageAdjustment>() {

         @Override
         public void configure( UsageAdjustment aBuilder ) {
            aBuilder.setMainInventory( lAcft );
            aBuilder.setUsageDate( lAdjustmentDate3 );
            aBuilder.setUsageType( MXFLIGHT );
            aBuilder.addUsage( lAcft, HOURS, lAdjustmentHoursTsn3, lAdjustmentHoursDelta3 );
            aBuilder.addUsage( lAcft, LANDING, lAdjustmentLandingTsn3, lAdjustmentLandingDelta3 );
         }
      } );

      // Given an event after the adjustment.
      TaskKey lReq = createRequirement( lAcft, lEventDate );

      // Given a usage adjustment after the event.
      Domain.createUsageAdjustment( new DomainConfiguration<UsageAdjustment>() {

         @Override
         public void configure( UsageAdjustment aBuilder ) {
            aBuilder.setMainInventory( lAcft );
            aBuilder.setUsageDate( lAdjustmentDate4 );
            aBuilder.setUsageType( MXFLIGHT );
            aBuilder.addUsage( lAcft, HOURS, lAdjustmentHoursTsn4, lAdjustmentHoursDelta4 );
         }
      } );

      // Given another usage adjustment after the event.
      Domain.createUsageAdjustment( new DomainConfiguration<UsageAdjustment>() {

         @Override
         public void configure( UsageAdjustment aBuilder ) {
            aBuilder.setMainInventory( lAcft );
            aBuilder.setUsageDate( lAdjustmentDate5 );
            aBuilder.setUsageType( MXFLIGHT );
            aBuilder.addUsage( lAcft, CYCLES, lAdjustmentCyclesTsn5, lAdjustmentCyclesDelta5 );
         }
      } );

      // Given yet another usage adjustment after the event.
      Domain.createUsageAdjustment( new DomainConfiguration<UsageAdjustment>() {

         @Override
         public void configure( UsageAdjustment aBuilder ) {
            aBuilder.setMainInventory( lAcft );
            aBuilder.setUsageDate( lAdjustmentDate6 );
            aBuilder.setUsageType( MXFLIGHT );
            aBuilder.addUsage( lAcft, HOURS, lAdjustmentHoursTsn6, lAdjustmentHoursDelta6 );
            aBuilder.addUsage( lAcft, LANDING, lAdjustmentLandingTsn6, lAdjustmentLandingDelta6 );
         }
      } );

      // Given a final usage adjustment after the event.
      Domain.createUsageAdjustment( new DomainConfiguration<UsageAdjustment>() {

         @Override
         public void configure( UsageAdjustment aBuilder ) {
            aBuilder.setMainInventory( lAcft );
            aBuilder.setUsageDate( lAdjustmentDate7 );
            aBuilder.setUsageType( MXFLIGHT );
            aBuilder.addUsage( lAcft, HOURS, lAdjustmentHoursTsn7, lAdjustmentHoursDelta7 );
            aBuilder.addUsage( lAcft, CYCLES, lAdjustmentCyclesTsn7, lAdjustmentCyclesDelta7 );
            aBuilder.addUsage( lAcft, LANDING, lAdjustmentLandingTsn7, lAdjustmentLandingDelta7 );
         }
      } );

      // When a usage snapshot is taken for the event.
      UsageSnapshotService.takeUsageSnapshotOfEvent( lReq.getEventKey() );

      // Then the usage snapshot reflects the usage at the time of the event,
      // which is the usage of the last adjustment before the event having the data type.
      BigDecimal lExpectedHoursTsn = lAdjustmentHoursTsn3;
      BigDecimal lExpectedCyclesTsn = lAdjustmentCyclesTsn2;
      BigDecimal lExpectedLandingTsn = lAdjustmentLandingTsn3;

      BigDecimal lActualHoursTsn = getUsageOfSnapshot( lReq, lAcft, HOURS );
      BigDecimal lActualCyclesTsn = getUsageOfSnapshot( lReq, lAcft, CYCLES );
      BigDecimal lActualLandingTsn = getUsageOfSnapshot( lReq, lAcft, LANDING );

      assertEquals( "Unexpected hours tsn in usage snapshot of event.", lExpectedHoursTsn,
            lActualHoursTsn );
      assertEquals( "Unexpected cycles tsn in usage snapshot of event.", lExpectedCyclesTsn,
            lActualCyclesTsn );
      assertEquals( "Unexpected landing tsn in usage snapshot of event.", lExpectedLandingTsn,
            lActualLandingTsn );
   }


   /**
    *
    * Verify that when the aircraft is currently tracking different usage data types then the usage
    * data types being track at the time of an event in the past, that the usage snapshot for that
    * event contains the all the data types. However, the data types that were not being tracked at
    * the time of the event will have a value of zero.
    */
   @Test
   public void forAcft_whenDataTypeCurrentlyTrackedButNotTrackedAtTimeOfEvent() throws Exception {

      // Set up
      // - adjustment 1: Jan-01, HOURS delta=1 tsn=1, CYCLES delta=2 tsn=2
      // - event: Jan-02
      // - adjustment 1: Jan-03, LANDING delta=3 tsn=3
      // - current: HOURS tsn=1, CYCLES tsn=2, LANDING tsn=3
      final Date lAdjustmentDate1 = toDate( "01-JAN-2010" );
      final BigDecimal lAdjustmentHoursDelta1 = valueOf( 1.0 );
      final BigDecimal lAdjustmentHoursTsn1 = valueOf( 1.0 );
      final BigDecimal lAdjustmentCyclesDelta1 = valueOf( 2.0 );
      final BigDecimal lAdjustmentCyclesTsn1 = valueOf( 2.0 );
      final Date lEventDate = toDate( "02-JAN-2010" );
      final Date lAdjustmentDate2 = toDate( "03-JAN-2010" );
      final BigDecimal lAdjustmentLandingDelta2 = valueOf( 3.0 );
      final BigDecimal lAdjustmentLandingTsn2 = valueOf( 3.0 );
      final BigDecimal lCurrentHoursTsn = valueOf( 1.0 );
      final BigDecimal lCurrentCyclesTsn = valueOf( 2.0 );
      final BigDecimal lCurrentLandingTsn = valueOf( 3.0 );

      // Given an aircraft.
      final InventoryKey lAcft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.addUsage( HOURS, lCurrentHoursTsn );
            aBuilder.addUsage( CYCLES, lCurrentCyclesTsn );
            aBuilder.addUsage( LANDING, lCurrentLandingTsn );
         }
      } );

      // Given a usage adjustment before the event.
      Domain.createUsageAdjustment( new DomainConfiguration<UsageAdjustment>() {

         @Override
         public void configure( UsageAdjustment aBuilder ) {
            aBuilder.setMainInventory( lAcft );
            aBuilder.setUsageDate( lAdjustmentDate1 );
            aBuilder.setUsageType( MXFLIGHT );
            aBuilder.addUsage( lAcft, HOURS, lAdjustmentHoursTsn1, lAdjustmentHoursDelta1 );
            aBuilder.addUsage( lAcft, CYCLES, lAdjustmentCyclesTsn1, lAdjustmentCyclesDelta1 );
         }
      } );

      // Given an event after the adjustment.
      TaskKey lReq = createRequirement( lAcft, lEventDate );

      // Given a usage adjustment after the event.
      Domain.createUsageAdjustment( new DomainConfiguration<UsageAdjustment>() {

         @Override
         public void configure( UsageAdjustment aBuilder ) {
            aBuilder.setMainInventory( lAcft );
            aBuilder.setUsageDate( lAdjustmentDate2 );
            aBuilder.setUsageType( MXFLIGHT );
            aBuilder.addUsage( lAcft, LANDING, lAdjustmentLandingTsn2, lAdjustmentLandingDelta2 );
         }
      } );

      // When a usage snapshot is taken for the event.
      UsageSnapshotService.takeUsageSnapshotOfEvent( lReq.getEventKey() );

      // Then the usage snapshot reflects the usage at the time of the event,
      // which is the usage of the last adjustment before the event.
      BigDecimal lExpectedHoursTsn = lAdjustmentHoursTsn1;
      BigDecimal lExpectedCyclesTsn = lAdjustmentCyclesTsn1;

      BigDecimal lActualHoursTsn = getUsageOfSnapshot( lReq, lAcft, HOURS );
      BigDecimal lActualCyclesTsn = getUsageOfSnapshot( lReq, lAcft, CYCLES );

      assertEquals( "Unexpected hours tsn in usage snapshot of event.", lExpectedHoursTsn,
            lActualHoursTsn );
      assertEquals( "Unexpected cycles tsn in usage snapshot of event.", lExpectedCyclesTsn,
            lActualCyclesTsn );

      // The usage snapshot also contains the curent usage data types there were not tracked by the
      // aircraft at the time of the event but their value is zero.
      BigDecimal lExpectedLandingTsn = BigDecimal.valueOf( 0.0 );
      BigDecimal lActualLandingTsn = getUsageOfSnapshot( lReq, lAcft, LANDING );
      assertEquals( "Unexpected landing tsn in usage snapshot of event.", lExpectedLandingTsn,
            lActualLandingTsn );

   }


   /**
    *
    * Verify that a usage snapshot is taken when there are usage adjustments before an event against
    * an assembly. The snapshot will reflect the TSN of the last snapshot before the event.
    *
    * Note: the logic treats ACFT and ASSY the same, so we only require one test.
    *
    * @throws Exception
    */
   @Test
   public void forAssy_whenAdjustmentsBeforeEvent() throws Exception {

      // Set up
      // - adjustment 1: Jan-01, delta=1 tsn=1
      // - adjustment 2: Jan-02, delta=2 tsn=3
      // - event: Jan-03
      // - adjustment 3: Jan-04, delta=3 tsn=6
      // - current tsn: 6

      final Date lAdjustmentDate1 = toDate( "01-JAN-2010" );
      final BigDecimal lAdjustmentDelta1 = valueOf( 1.0 );
      final BigDecimal lAdjustmentTsn1 = valueOf( 1.0 );
      final Date lAdjustmentDate2 = toDate( "02-JAN-2010" );
      final BigDecimal lAdjustmentDelta2 = valueOf( 2.0 );
      final BigDecimal lAdjustmentTsn2 = valueOf( 3.0 );
      final Date lEventDate = toDate( "04-JAN-2010" );
      final Date lAdjustmentDate3 = toDate( "05-JAN-2010" );
      final BigDecimal lAdjustmentDelta3 = valueOf( 3.0 );
      final BigDecimal lAdjustmentTsn3 = valueOf( 6.0 );
      final BigDecimal lCurrentTsn = valueOf( 6.0 );

      // Given an assembly.
      InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aBuilder ) {
            aBuilder.addUsage( HOURS, lCurrentTsn );
         }
      } );

      // Given a usage adjustment before the event.
      createFlight( lEngine, lAdjustmentDate1, lAdjustmentTsn1, lAdjustmentDelta1 );

      // Given another usage adjustment before the event.
      createFlight( lEngine, lAdjustmentDate2, lAdjustmentTsn2, lAdjustmentDelta2 );

      // Given an event after the adjustment.
      TaskKey lReq = createRequirement( lEngine, lEventDate );

      // Given another usage adjustment after the event.
      createFlight( lEngine, lAdjustmentDate3, lAdjustmentTsn3, lAdjustmentDelta3 );

      // When a usage snapshot is taken for the event.
      UsageSnapshotService.takeUsageSnapshotOfEvent( lReq.getEventKey() );

      // Then the usage snapshot reflects the usage at the time of the event,
      // which is the usage of the second adjustment.
      BigDecimal lExpectedTsn = lAdjustmentTsn2;
      BigDecimal lActualTsn = getUsageOfSnapshot( lReq, lEngine, HOURS );
      assertEquals( "Unexpected tsn in usage snapshot of event.", lExpectedTsn, lActualTsn );
   }


   /**
    *
    * Verify that a usage snapshot is taken when there are no usage adjustments against a system.
    * The snapshot will reflect the current usage of the system.
    *
    * @throws Exception
    */
   @Test
   public void forSys_whenNoAdjustments() throws Exception {

      // Set up
      // - event: Jan-01
      // - current tsn: 0

      final Date lEventDate = toDate( "01-JAN-2010" );
      final BigDecimal lCurrentTsn = valueOf( 0.0 );

      // Given an aircraft with a system.
      final InventoryKey lAcft = createAircraft( lCurrentTsn, SYSTEM_NAME );
      final InventoryKey lSys = Domain.readSystem( lAcft, SYSTEM_NAME );

      // Given an event before the adjustment.
      TaskKey lReq = createRequirement( lSys, lEventDate );

      // When a usage snapshot is attempted to be taken for the event.
      UsageSnapshotService.takeUsageSnapshotOfEvent( lReq.getEventKey() );

      // Then the usage snapshot reflects the current usage of the system.
      BigDecimal lExpectedTsn = lCurrentTsn;
      BigDecimal lActualTsn = getUsageOfSnapshot( lReq, lSys, HOURS );
      assertEquals( "Unexpected tsn in usage snapshot of event.", lExpectedTsn, lActualTsn );
   }


   /**
    *
    * Verify that a usage snapshot is taken when there are only usage adjustments before an event
    * against a system. The snapshot will reflect the current usage of the system.
    *
    * Note: the adjustments are against the aircraft on which the system resides.
    *
    *
    * @throws Exception
    */
   @Test
   public void forSys_whenAdjustmentBeforeEvent() throws Exception {

      // Set up
      // - adjustment 1: Jan-01, delta=1 tsn=1
      // - event: Jan-02
      // - current tsn: 1

      final Date lAdjustmentDate1 = toDate( "01-JAN-2010" );
      final BigDecimal lAdjustmentDelta1 = valueOf( 1.0 );
      final BigDecimal lAdjustmentTsn1 = valueOf( 1.0 );
      final Date lEventDate = toDate( "02-JAN-2010" );
      final BigDecimal lCurrentTsn = valueOf( 1.0 );

      // Given an aircraft with a system.
      final InventoryKey lAcft = createAircraft( lCurrentTsn, SYSTEM_NAME );
      final InventoryKey lSys = Domain.readSystem( lAcft, SYSTEM_NAME );

      // Given a usage adjustment before the event (against the system's aircraft).
      createFlight( lAcft, lAdjustmentDate1, lAdjustmentTsn1, lAdjustmentDelta1 );

      // Given an event after the adjustment.
      TaskKey lReq = createRequirement( lSys, lEventDate );

      // When a usage snapshot is taken for the event.
      UsageSnapshotService.takeUsageSnapshotOfEvent( lReq.getEventKey() );

      // Then the usage snapshot reflects the current usage of the system.
      BigDecimal lExpectedTsn = lCurrentTsn;
      BigDecimal lActualTsn = getUsageOfSnapshot( lReq, lSys, HOURS );
      assertEquals( "Unexpected tsn in usage snapshot of event.", lExpectedTsn, lActualTsn );
   }


   /**
    *
    * Verify that a usage snapshot is taken when there are only usage adjustments after an event
    * against a system (none before). The snapshot will reflect the TSN minus the delta of the usage
    * adjustment immediately after the event.
    *
    * Note: the adjustments are against the aircraft on which the system resides.
    *
    * @throws Exception
    */
   @Test
   public void forSys_whenAdjustmentAfterEvent() throws Exception {

      // Set up
      // - event: Jan-01
      // - adjustment 1: Jan-02, delta=1 tsn=1
      // - current tsn: 1

      final Date lEventDate = toDate( "01-JAN-2010" );
      final Date lAdjustmentDate = toDate( "02-JAN-2010" );
      final BigDecimal lAdjustmentDelta = valueOf( 1.0 );
      final BigDecimal lAdjustmentTsn = valueOf( 1.0 );
      final BigDecimal lCurrentTsn = valueOf( 1.0 );

      // Given an aircraft with a system.
      final InventoryKey lAcft = createAircraft( lCurrentTsn, SYSTEM_NAME );
      final InventoryKey lSys = Domain.readSystem( lAcft, SYSTEM_NAME );

      // Given an event before the adjustment.
      TaskKey lReq = createRequirement( lSys, lEventDate );

      // Given a usage adjustment after the event (against the system's aircraft).
      createFlight( lAcft, lAdjustmentDate, lAdjustmentTsn, lAdjustmentDelta );

      // When a usage snapshot is taken for the event.
      UsageSnapshotService.takeUsageSnapshotOfEvent( lReq.getEventKey() );

      // Then the usage snapshot reflects the usage at the time of the event,
      // which is the TSN-delta of the adjustment immediately after the event.
      BigDecimal lExpectedTsn = lAdjustmentTsn.subtract( lAdjustmentDelta );
      BigDecimal lActualTsn = getUsageOfSnapshot( lReq, lSys, HOURS );
      assertEquals( "Unexpected tsn in usage snapshot of event.", lExpectedTsn, lActualTsn );
   }


   /**
    *
    * Verify that a usage snapshot is taken at the same date and time as a usage record against a
    * system, the snapshot will reflect the current usage of the system.
    *
    * This is because for event at time T or later, we expect the usage being captured to match the
    * usage after. Events at time T-1 should reflect usage before.
    *
    *
    * @throws Exception
    */
   @Test
   public void forSys_whenAdjustmentAtSameTimeAsEvent() throws Exception {

      // Set up
      // - adjustment 1: Jan-01, delta=1 tsn=1
      // - event: Jan-01
      // - current tsn: 1
      // - before tsn: 0

      final Date lAdjustmentDate1 = toDate( "01-JAN-2010" );
      final BigDecimal lAdjustmentDelta1 = valueOf( 1.0 );
      final BigDecimal lAdjustmentTsn1 = valueOf( 1.0 );
      final Date lEventDate = toDate( "01-JAN-2010" );
      final BigDecimal lCurrentTsn = valueOf( 1.0 );

      // Given an aircraft with a system.
      final InventoryKey lAcft = createAircraft( lCurrentTsn, SYSTEM_NAME );
      final InventoryKey lSys = Domain.readSystem( lAcft, SYSTEM_NAME );

      // Given a usage adjustment before the event (against the system's aircraft).
      createFlight( lAcft, lAdjustmentDate1, lAdjustmentTsn1, lAdjustmentDelta1 );

      // Given an event after the adjustment.
      TaskKey lReq = createRequirement( lSys, lEventDate );

      // When a usage snapshot is taken for the event.
      UsageSnapshotService.takeUsageSnapshotOfEvent( lReq.getEventKey() );

      // Then the usage snapshot reflects the current usage of the system.
      BigDecimal lExpectedTsn = lCurrentTsn;
      BigDecimal lActualTsn = getUsageOfSnapshot( lReq, lSys, HOURS );
      assertEquals( "Unexpected tsn in usage snapshot of event.", lExpectedTsn, lActualTsn );
   }


   /**
    *
    * Verify that a usage snapshot is taken when there are usage adjustments before and after an
    * event against a system. The snapshot will reflect the TSN minus the delta of the usage
    * adjustment immediately after the event.
    *
    * Note: the adjustments are against the aircraft on which the system resides.
    *
    *
    * @throws Exception
    */
   @Test
   public void forSys_whenAdjustmentBeforeAndAfterEvent() throws Exception {

      // Set up
      // - adjustment 1: Jan-01, delta=1 tsn=1
      // - event: Jan-02
      // - adjustment 2: Jan-03, delta=2 tsn=3
      // - current tsn: 3

      final Date lAdjustmentDate1 = toDate( "01-JAN-2010" );
      final BigDecimal lAdjustmentDelta1 = valueOf( 1.0 );
      final BigDecimal lAdjustmentTsn1 = valueOf( 1.0 );
      final Date lEventDate = toDate( "02-JAN-2010" );
      final Date lAdjustmentDate2 = toDate( "03-JAN-2010" );
      final BigDecimal lAdjustmentDelta2 = valueOf( 2.0 );
      final BigDecimal lAdjustmentTsn2 = valueOf( 3.0 );
      final BigDecimal lCurrentTsn = valueOf( 3.0 );

      // Given an aircraft with a system.
      final InventoryKey lAcft = createAircraft( lCurrentTsn, SYSTEM_NAME );
      final InventoryKey lSys = Domain.readSystem( lAcft, SYSTEM_NAME );

      // Given a usage adjustment before the event (against the system's aircraft).
      createFlight( lAcft, lAdjustmentDate1, lAdjustmentTsn1, lAdjustmentDelta1 );

      // Given an event after the adjustment.
      TaskKey lReq = createRequirement( lSys, lEventDate );

      // Given another usage adjustment after the event (against the system's aircraft).
      createFlight( lAcft, lAdjustmentDate2, lAdjustmentTsn2, lAdjustmentDelta2 );

      // When a usage snapshot is taken for the event.
      UsageSnapshotService.takeUsageSnapshotOfEvent( lReq.getEventKey() );

      // Then the usage snapshot reflects the usage at the time of the event,
      // which is the TSN-delta of the adjustment immediately after the event.
      BigDecimal lExpectedTsn = lAdjustmentTsn2.subtract( lAdjustmentDelta2 );
      BigDecimal lActualTsn = getUsageOfSnapshot( lReq, lSys, HOURS );
      assertEquals( "Unexpected tsn in usage snapshot of event.", lExpectedTsn, lActualTsn );
   }


   /**
    *
    * Verify that a usage snapshot is taken for an event against a system when the adjustments
    * before and after the event consist of all the various types of adjustments. The snapshot will
    * reflect the TSN minus the delta of the usage adjustment immediately after the event.
    *
    * Note: the adjustments are against the aircraft on which the system resides.
    *
    * @throws Exception
    */
   @Test
   public void forSys_whenAllTypesOfAdjustmentsBeforeEvent() throws Exception {

      // Set up
      // - adjustment 1: Jan-01, delta=1 tsn=1 (correction)
      // - adjustment 2: Jan-02, delta=2 tsn=3 (flight)
      // - adjustment 2: Jan-03, delta=3 tsn=6 (accrual)
      // - event: Jan-04
      // - adjustment 1: Jan-05, delta=4 tsn=10 (correction)
      // - adjustment 2: Jan-06, delta=5 tsn=15 (flight)
      // - adjustment 2: Jan-07, delta=6 tsn=21 (accrual)
      // - current tsn: 6

      final Date lAdjustmentDate1 = toDate( "01-JAN-2010" );
      final BigDecimal lAdjustmentDelta1 = valueOf( 1.0 );
      final BigDecimal lAdjustmentTsn1 = valueOf( 1.0 );
      final Date lAdjustmentDate2 = toDate( "02-JAN-2010" );
      final BigDecimal lAdjustmentDelta2 = valueOf( 2.0 );
      final BigDecimal lAdjustmentTsn2 = valueOf( 3.0 );
      final Date lAdjustmentDate3 = toDate( "03-JAN-2010" );
      final BigDecimal lAdjustmentDelta3 = valueOf( 3.0 );
      final BigDecimal lAdjustmentTsn3 = valueOf( 6.0 );
      final Date lEventDate = toDate( "04-JAN-2010" );
      final Date lAdjustmentDate4 = toDate( "05-JAN-2010" );
      final BigDecimal lAdjustmentDelta4 = valueOf( 4.0 );
      final BigDecimal lAdjustmentTsn4 = valueOf( 10.0 );
      final Date lAdjustmentDate5 = toDate( "06-JAN-2010" );
      final BigDecimal lAdjustmentDelta5 = valueOf( 5.0 );
      final BigDecimal lAdjustmentTsn5 = valueOf( 15.0 );
      final Date lAdjustmentDate6 = toDate( "07-JAN-2010" );
      final BigDecimal lAdjustmentDelta6 = valueOf( 6.0 );
      final BigDecimal lAdjustmentTsn6 = valueOf( 21.0 );
      final BigDecimal lCurrentTsn = valueOf( 21.0 );

      // Given an aircraft with a system.
      final InventoryKey lAcft = createAircraft( lCurrentTsn, SYSTEM_NAME );
      final InventoryKey lSys = Domain.readSystem( lAcft, SYSTEM_NAME );

      // Given a CORRECTION usage adjustment before the event.
      createCorrection( lAcft, lAdjustmentDate1, lAdjustmentTsn1, lAdjustmentDelta1 );

      // Given a FLIGHT usage adjustment before the event.
      createFlight( lAcft, lAdjustmentDate2, lAdjustmentTsn2, lAdjustmentDelta2 );

      // Given an ACCRUAL usage adjustment before the event.
      createAccrual( lAcft, lAdjustmentDate3, lAdjustmentTsn3, lAdjustmentDelta3 );

      // Given an event after the adjustment.
      TaskKey lReq = createRequirement( lSys, lEventDate );

      // Given a CORRECTION usage adjustment after the event.
      createCorrection( lAcft, lAdjustmentDate4, lAdjustmentTsn4, lAdjustmentDelta4 );

      // Given a FLIGHT usage adjustment after the event.
      createFlight( lAcft, lAdjustmentDate5, lAdjustmentTsn5, lAdjustmentDelta5 );

      // Given an ACCRUAL usage adjustment after the event.
      createAccrual( lAcft, lAdjustmentDate6, lAdjustmentTsn6, lAdjustmentDelta6 );

      // When a usage snapshot is taken for the event.
      UsageSnapshotService.takeUsageSnapshotOfEvent( lReq.getEventKey() );

      // Then the usage snapshot reflects the usage at the time of the event,
      // which is the TSN-delta of the adjustment immediately after the event.
      BigDecimal lExpectedTsn = lAdjustmentTsn4.subtract( lAdjustmentDelta4 );
      BigDecimal lActualTsn = getUsageOfSnapshot( lReq, lSys, HOURS );
      assertEquals( "Unexpected tsn in usage snapshot of event.", lExpectedTsn, lActualTsn );
   }


   /**
    *
    * Verify that the usage snapshot of an event captures all the usage data types tracked by the
    * system. The snapshot will reflect the TSN minus the delta of the usage adjustment immediately
    * after the event.
    *
    */
   @Test
   public void forSys_whenTrackingManyDataTypesAndAdjustmentsAfterTheEvent() throws Exception {

      // Set up
      // - aircraft/system tracking HOURS and CYCLES
      // - event: Jan-01
      // - adjustment 1: Jan-02, HOURS delta=10 tsn=10, CYCLES delta=1 tsn=1
      // - current: HOURS tsn=10, CYCLES tsn=1

      final Date lEventDate = toDate( "01-JAN-2010" );
      final Date lAdjustmentDate = toDate( "02-JAN-2010" );
      final BigDecimal lAdjustmentHoursDelta = valueOf( 10.0 );
      final BigDecimal lAdjustmentHoursTsn = valueOf( 10.0 );
      final BigDecimal lAdjustmentCyclesDelta = valueOf( 1.0 );
      final BigDecimal lAdjustmentCyclesTsn = valueOf( 1.0 );
      final BigDecimal lCurrentHoursTsn = valueOf( 10.0 );
      final BigDecimal lCurrentCyclesTsn = valueOf( 1.0 );

      // Given an aircraft tracking many usages.
      final InventoryKey lAcft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.addUsage( HOURS, lCurrentHoursTsn );
            aBuilder.addUsage( CYCLES, lCurrentCyclesTsn );
            aBuilder.addSystem( SYSTEM_NAME );
         }
      } );
      final InventoryKey lSys = Domain.readSystem( lAcft, SYSTEM_NAME );

      // Given a usage adjustment before the event.
      Domain.createUsageAdjustment( new DomainConfiguration<UsageAdjustment>() {

         @Override
         public void configure( UsageAdjustment aBuilder ) {
            aBuilder.setMainInventory( lAcft );
            aBuilder.setUsageDate( lAdjustmentDate );
            aBuilder.setUsageType( MXFLIGHT );
            aBuilder.addUsage( lAcft, HOURS, lAdjustmentHoursTsn, lAdjustmentHoursDelta );
            aBuilder.addUsage( lAcft, CYCLES, lAdjustmentCyclesTsn, lAdjustmentCyclesDelta );
         }
      } );

      // Given an event after the adjustment.
      TaskKey lReq = createRequirement( lSys, lEventDate );

      // When a usage snapshot is taken for the event.
      UsageSnapshotService.takeUsageSnapshotOfEvent( lReq.getEventKey() );

      // Then the usage snapshot contains all usage data types and reflects the usage at the time of
      // the event,
      // which is the TSN-delta of the adjustment immediately after the event.
      BigDecimal lExpectedHoursTsn = lAdjustmentHoursTsn.subtract( lAdjustmentHoursDelta );
      BigDecimal lActualHoursTsn = getUsageOfSnapshot( lReq, lSys, HOURS );
      assertEquals( "Unexpected HOURS tsn in usage snapshot of event.", lExpectedHoursTsn,
            lActualHoursTsn );

      BigDecimal lExpectedCyclesTsn = lAdjustmentCyclesTsn.subtract( lAdjustmentCyclesDelta );
      BigDecimal lActualCyclesTsn = getUsageOfSnapshot( lReq, lSys, CYCLES );
      assertEquals( "Unexpected CYCLES tsn in usage snapshot of event.", lExpectedCyclesTsn,
            lActualCyclesTsn );
   }


   /**
    *
    * Verify that when the system is currently tracking different usage data types then the usage
    * data types being track at the time of an event in the past, that the usage snapshot for that
    * event contains the all the data types. However, the data types that were not being tracked at
    * the time of the event will have a value of zero.
    */
   @Test
   public void forSys_whenDataTypeCurrentlyTrackedButNotTrackedAtTimeOfEvent() throws Exception {

      // Set up
      // - adjustment 1: Jan-01, HOURS delta=1 tsn=1, CYCLES delta=2 tsn=2
      // - event: Jan-02
      // - adjustment 1: Jan-03, LANDING delta=3 tsn=3
      // - current: HOURS tsn=1, CYCLES tsn=2, LANDING tsn=3
      final Date lAdjustmentDate1 = toDate( "01-JAN-2010" );
      final BigDecimal lAdjustmentHoursDelta1 = valueOf( 1.0 );
      final BigDecimal lAdjustmentHoursTsn1 = valueOf( 1.0 );
      final BigDecimal lAdjustmentCyclesDelta1 = valueOf( 2.0 );
      final BigDecimal lAdjustmentCyclesTsn1 = valueOf( 2.0 );
      final Date lEventDate = toDate( "02-JAN-2010" );
      final Date lAdjustmentDate2 = toDate( "03-JAN-2010" );
      final BigDecimal lAdjustmentLandingDelta2 = valueOf( 3.0 );
      final BigDecimal lAdjustmentLandingTsn2 = valueOf( 3.0 );
      final BigDecimal lCurrentHoursTsn = valueOf( 1.0 );
      final BigDecimal lCurrentCyclesTsn = valueOf( 2.0 );
      final BigDecimal lCurrentLandingTsn = valueOf( 3.0 );

      // Given an aircraft with a system.
      final InventoryKey lAcft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.addUsage( HOURS, lCurrentHoursTsn );
            aBuilder.addUsage( CYCLES, lCurrentCyclesTsn );
            aBuilder.addUsage( LANDING, lCurrentLandingTsn );
            aBuilder.addSystem( SYSTEM_NAME );
         }
      } );
      final InventoryKey lSys = Domain.readSystem( lAcft, SYSTEM_NAME );

      // Given a usage adjustment before the event.
      Domain.createUsageAdjustment( new DomainConfiguration<UsageAdjustment>() {

         @Override
         public void configure( UsageAdjustment aBuilder ) {
            aBuilder.setMainInventory( lAcft );
            aBuilder.setUsageDate( lAdjustmentDate1 );
            aBuilder.setUsageType( MXFLIGHT );
            aBuilder.addUsage( lAcft, HOURS, lAdjustmentHoursTsn1, lAdjustmentHoursDelta1 );
            aBuilder.addUsage( lAcft, CYCLES, lAdjustmentCyclesTsn1, lAdjustmentCyclesDelta1 );
         }
      } );

      // Given an event after the adjustment.
      TaskKey lReq = createRequirement( lSys, lEventDate );

      // Given a usage adjustment after the event.
      Domain.createUsageAdjustment( new DomainConfiguration<UsageAdjustment>() {

         @Override
         public void configure( UsageAdjustment aBuilder ) {
            aBuilder.setMainInventory( lAcft );
            aBuilder.setUsageDate( lAdjustmentDate2 );
            aBuilder.setUsageType( MXFLIGHT );
            aBuilder.addUsage( lAcft, LANDING, lAdjustmentLandingTsn2, lAdjustmentLandingDelta2 );
         }
      } );

      // When a usage snapshot is taken for the event.
      UsageSnapshotService.takeUsageSnapshotOfEvent( lReq.getEventKey() );

      // Then the usage snapshot reflects the usage at the time of the event,
      // which is the usage of the last adjustment before the event.
      BigDecimal lExpectedHoursTsn = lAdjustmentHoursTsn1;
      BigDecimal lExpectedCyclesTsn = lAdjustmentCyclesTsn1;

      BigDecimal lActualHoursTsn = getUsageOfSnapshot( lReq, lSys, HOURS );
      BigDecimal lActualCyclesTsn = getUsageOfSnapshot( lReq, lSys, CYCLES );

      assertEquals( "Unexpected hours tsn in usage snapshot of event.", lExpectedHoursTsn,
            lActualHoursTsn );
      assertEquals( "Unexpected cycles tsn in usage snapshot of event.", lExpectedCyclesTsn,
            lActualCyclesTsn );

      // The usage snapshot also contains the curent usage data types there were not tracked by the
      // aircraft at the time of the event but their value is zero.
      BigDecimal lExpectedLandingTsn = BigDecimal.valueOf( 0.0 );
      BigDecimal lActualLandingTsn = getUsageOfSnapshot( lReq, lSys, LANDING );
      assertEquals( "Unexpected landing tsn in usage snapshot of event.", lExpectedLandingTsn,
            lActualLandingTsn );

   }


   /**
    *
    * Verify that a usage snapshot is taken when there are no usage adjustments against a TRK. The
    * snapshot will reflect the current usage of the TRK.
    *
    * @throws Exception
    */
   @Test
   public void forTrk_whenNoAdjustments() throws Exception {

      // Set up
      // - event: Jan-01
      // - current tsn: 0

      final Date lEventDate = toDate( "01-JAN-2010" );
      final BigDecimal lCurrentTsn = valueOf( 0.0 );

      // Given a TRK.
      final InventoryKey lTrk = createTracked( lCurrentTsn );

      // Given an event before the adjustment.
      TaskKey lReq = createRequirement( lTrk, lEventDate );

      // When a usage snapshot is attempted to be taken for the event.
      UsageSnapshotService.takeUsageSnapshotOfEvent( lReq.getEventKey() );

      // Then the usage snapshot reflects the current usage of the system.
      BigDecimal lExpectedTsn = lCurrentTsn;
      BigDecimal lActualTsn = getUsageOfSnapshot( lReq, lTrk, HOURS );
      assertEquals( "Unexpected tsn in usage snapshot of event.", lExpectedTsn, lActualTsn );
   }


   /**
    *
    * Verify that a usage snapshot is taken when there is only a usage adjustment before an event
    * against a TRK. The snapshot will reflect the current TSN of the TRK.
    *
    * Usage adjustments against a TRK that do not occur while the TRK is installed are treated
    * equally to any usage changes and are relevant to the recorded usage date.
    *
    * @throws Exception
    */
   @Test
   public void forTrk_whenAdjustmentBeforeEvent() throws Exception {

      // Set up
      // - adjustment 1: Jan-01, delta=1 tsn=1
      // - event: Jan-02
      // - current tsn: 1

      final Date lAdjustmentDate = toDate( "01-JAN-2010" );
      final BigDecimal lAdjustmentDelta = valueOf( 1.0 );
      final BigDecimal lAdjustmentTsn = valueOf( 1.0 );
      final Date lEventDate = toDate( "02-JAN-2010" );
      final BigDecimal lCurrentTsn = valueOf( 1.0 );

      // Given a TRK.
      final InventoryKey lTrk = createTracked( lCurrentTsn );

      // Given a usage adjustment before the event.
      createCorrection( lTrk, lAdjustmentDate, lAdjustmentTsn, lAdjustmentDelta );

      // Given an event after the adjustment.
      TaskKey lReq = createRequirement( lTrk, lEventDate );

      // When a usage snapshot is taken for the event.
      UsageSnapshotService.takeUsageSnapshotOfEvent( lReq.getEventKey() );

      // Then the usage snapshot reflects the current usage.
      // As adjustments made while the TRK is not installed are assumed to be adjustments to the
      // original, initial usage. Which in this case is also the current usage.
      BigDecimal lExpectedTsn = lCurrentTsn;
      BigDecimal lActualTsn = getUsageOfSnapshot( lReq, lTrk, HOURS );
      assertEquals( "Unexpected tsn in usage snapshot of event.", lExpectedTsn, lActualTsn );
   }


   /**
    *
    * Verify that a usage snapshot is taken when there is only a usage adjustment after an event
    * against a TRK. The snapshot will reflect the TSN of the TRK for that usage_dt.
    *
    * Usage adjustments against a TRK that do not occur while the TRK is installed are treated
    * equally to any usage changes and are relevant to the recorded usage date.
    *
    * @throws Exception
    */
   @Test
   public void forTrk_whenAdjustmentAfterEvent() throws Exception {

      // Set up
      // - event: Jan-01
      // - adjustment 1: Jan-02, delta=1 tsn=1
      // - current tsn: 1

      final Date lEventDate = toDate( "01-JAN-2010" );
      final Date lAdjustmentDate1 = toDate( "02-JAN-2010" );
      final BigDecimal lAdjustmentDelta1 = valueOf( 1.0 );
      final BigDecimal lAdjustmentTsn1 = valueOf( 1.0 );
      final BigDecimal lCurrentTsn = valueOf( 1.0 );

      // Given a TRK.
      final InventoryKey lTrk = createTracked( lCurrentTsn );

      // Given an event before the adjustment.
      TaskKey lReq = createRequirement( lTrk, lEventDate );

      // Given a usage adjustment after the event.
      createCorrection( lTrk, lAdjustmentDate1, lAdjustmentTsn1, lAdjustmentDelta1 );

      // When a usage snapshot is taken for the event.
      UsageSnapshotService.takeUsageSnapshotOfEvent( lReq.getEventKey() );

      // Then the usage snapshot reflects the current usage.
      // As adjustments made while the TRK is not installed are assumed to be adjustments to the
      // original, initial usage. Which in this case is also the current usage.
      BigDecimal lExpectedTsn = lCurrentTsn.subtract( lAdjustmentDelta1 );
      BigDecimal lActualTsn = getUsageOfSnapshot( lReq, lTrk, HOURS );
      assertEquals( "Unexpected tsn in usage snapshot of event.", lExpectedTsn, lActualTsn );
   }


   /**
    *
    * Verify that a usage snapshot is taken at the same time as a usage adjustment against a TRK,
    * the snapshot will reflect the usage after the adjustment.
    *
    * This is because for event at time T or later, we expect the usage being captured to match the
    * usage after. Events at time T-1 should reflect usage before.
    *
    * @throws Exception
    */
   @Test
   public void forTrk_whenAdjustmentAtSameTimeAsEvent() throws Exception {

      // Set up
      // - adjustment 1: Jan-01, delta=1 tsn=1
      // - event: Jan-01
      // - current tsn: 1
      // - before tsn: 0

      final Date lAdjustmentDate = toDate( "01-JAN-2010" );
      final BigDecimal lAdjustmentDelta = valueOf( 1.0 );
      final BigDecimal lAdjustmentTsn = valueOf( 1.0 );
      final Date lEventDate = toDate( "01-JAN-2010" );
      final BigDecimal lCurrentTsn = valueOf( 1.0 );

      // Given a TRK.
      final InventoryKey lTrk = createTracked( lCurrentTsn );

      // Given a usage adjustment before the event.
      createCorrection( lTrk, lAdjustmentDate, lAdjustmentTsn, lAdjustmentDelta );

      // Given an event after the adjustment.
      TaskKey lReq = createRequirement( lTrk, lEventDate );

      // When a usage snapshot is taken for the event.
      UsageSnapshotService.takeUsageSnapshotOfEvent( lReq.getEventKey() );

      // Then the usage snapshot reflects the current usage.
      // As adjustments made while the TRK is not installed are assumed to be adjustments to the
      // original, initial usage. Which in this case is also the current usage.
      BigDecimal lExpectedTsn = lCurrentTsn;
      BigDecimal lActualTsn = getUsageOfSnapshot( lReq, lTrk, HOURS );
      assertEquals( "Unexpected tsn in usage snapshot of event.", lExpectedTsn, lActualTsn );
   }


   /**
    *
    * Verify that a usage snapshot is taken when there is usage adjustments before and after an
    * event against a TRK. The snapshot will reflect the current TSN of the TRK.
    *
    * Usage adjustments against a TRK are considered a normal item in the history of delta usage.
    * The installed state of the inventory is not relevant to the usage history, and the delta is
    * applicable to the usage_dt of the correction.
    *
    * @throws Exception
    */
   @Test
   public void forTrk_whenAdjustmentBeforeAndAfterEvent() throws Exception {

      // Set up
      // - adjustment 1: Jan-01, delta=1 tsn=1
      // - event: Jan-02
      // - adjustment 2: Jan-03, delta=2 tsn=3
      // - current tsn: 3

      final Date lAdjustmentDate1 = toDate( "01-JAN-2010" );
      final BigDecimal lAdjustmentDelta1 = valueOf( 1.0 );
      final BigDecimal lAdjustmentTsn1 = valueOf( 1.0 );
      final Date lEventDate = toDate( "02-JAN-2010" );
      final Date lAdjustmentDate2 = toDate( "03-JAN-2010" );
      final BigDecimal lAdjustmentDelta2 = valueOf( 2.0 );
      final BigDecimal lAdjustmentTsn2 = valueOf( 3.0 );
      final BigDecimal lCurrentTsn = valueOf( 3.0 );

      // Given a TRK.
      final InventoryKey lTrk = createTracked( lCurrentTsn );

      // Given a usage adjustment before the event.
      createCorrection( lTrk, lAdjustmentDate1, lAdjustmentTsn1, lAdjustmentDelta1 );

      // Given an event after the adjustment.
      TaskKey lReq = createRequirement( lTrk, lEventDate );

      // Given a usage adjustment after the event.
      createCorrection( lTrk, lAdjustmentDate2, lAdjustmentTsn2, lAdjustmentDelta2 );

      // When a usage snapshot is taken for the event.
      UsageSnapshotService.takeUsageSnapshotOfEvent( lReq.getEventKey() );

      // Then the usage snapshot reflects the current usage less the more recent delta
      // Adjustments made while the TRK is not installed are relevant to the usage_dt.
      BigDecimal lExpectedTsn = lCurrentTsn.subtract( lAdjustmentDelta2 );
      BigDecimal lActualTsn = getUsageOfSnapshot( lReq, lTrk, HOURS );
      assertEquals( "Unexpected tsn in usage snapshot of event.", lExpectedTsn, lActualTsn );
   }


   /**
    *
    * Verify that a usage snapshot is taken when there are TRK installs and removals before an event
    * against the TRK. Also there are usage adjustments against the TRK that occur before the
    * installs, as well as, while the TRK is installed.
    *
    * The snapshot will reflect the TSN of the last adjustment, while the TRK is installed, before
    * the event.
    *
    * @throws Exception
    */
   @Test
   public void forTrk_whenInstallsAndRemovalsBeforeEvent() throws Exception {

      // Set up
      // - adjustment 1: Jan-01, delta=1 tsn=1
      // - install record 1: Jan-02, tsn=1
      // - adjustment 2: Jan-03, delta=2 tsn=3
      // - remove record 1: Jan-04, tsn=3
      // - install record 2: Jan-05, tsn=3
      // - adjustment 3: Jan-06, delta=4 tsn=7
      // - remove record 2: Jan-07, tsn=7
      // - event: Jan-08
      // - current: tsn=7

      final Date lAdjustmentDate1 = toDate( "01-JAN-2010" );
      final BigDecimal lAdjustmentDelta1 = valueOf( 1.0 );
      final BigDecimal lAdjustmentTsn1 = valueOf( 1.0 );
      final Date lInstallDate1 = toDate( "02-JAN-2010" );
      final BigDecimal lInstallTsn1 = valueOf( 1.0 );
      final Date lAdjustmentDate2 = toDate( "03-JAN-2010" );
      final BigDecimal lAdjustmentDelta2 = valueOf( 2.0 );
      final BigDecimal lAdjustmentTsn2 = valueOf( 3.0 );
      final Date lRemoveDate1 = toDate( "04-JAN-2010" );
      final BigDecimal lRemoveTsn1 = valueOf( 3.0 );
      final Date lInstallDate2 = toDate( "05-JAN-2010" );
      final BigDecimal lInstallTsn2 = valueOf( 3.0 );
      final Date lAdjustmentDate3 = toDate( "06-JAN-2010" );
      final BigDecimal lAdjustmentDelta3 = valueOf( 4.0 );
      final BigDecimal lAdjustmentTsn3 = valueOf( 7.0 );
      final Date lRemoveDate2 = toDate( "07-JAN-2010" );
      final BigDecimal lRemoveTsn2 = valueOf( 7.0 );
      final Date lEventDate = toDate( "08-JAN-2010" );

      final BigDecimal lCurrentTsn = valueOf( 7.0 );

      // Given an aircraft to install the TRK.
      final InventoryKey lAcft = Domain.createAircraft();

      // Given a TRK.
      final InventoryKey lTrk = createTracked( lCurrentTsn );

      // Given a usage adjustment before install.
      createCorrection( lTrk, lAdjustmentDate1, lAdjustmentTsn1, lAdjustmentDelta1 );

      // Given the first install record.
      createInstallRecord( lAcft, lTrk, lInstallDate1, lInstallTsn1 );

      // Given a usage adjustment while installed (first install).
      createFlight( lAcft, lAdjustmentDate2, lAdjustmentTsn2, lAdjustmentDelta2 );

      // Given the first remove record.
      createRemoveRecord( lAcft, lTrk, lRemoveDate1, lRemoveTsn1 );

      // Given the second install record.
      createInstallRecord( lAcft, lTrk, lInstallDate2, lInstallTsn2 );

      // Given a usage adjustment while installed (second install).
      createFlight( lAcft, lAdjustmentDate3, lAdjustmentTsn3, lAdjustmentDelta3 );

      // Given the second remove record.
      createRemoveRecord( lAcft, lTrk, lRemoveDate2, lRemoveTsn2 );

      // Given an event after the installs and removals.
      TaskKey lReq = createRequirement( lTrk, lEventDate );

      // When a usage snapshot is taken for the event.
      UsageSnapshotService.takeUsageSnapshotOfEvent( lReq.getEventKey() );

      // Then the usage snapshot reflects the usage at the time of the event,
      // which is the usage after the last remove.
      BigDecimal lExpectedTsn = lRemoveTsn2;
      BigDecimal lActualTsn = getUsageOfSnapshot( lReq, lTrk, HOURS );
      assertEquals( "Unexpected tsn in usage snapshot of event.", lExpectedTsn, lActualTsn );

   }


   /**
    *
    * Verify that a usage snapshot is taken when there are TRK installs and removals after an event
    * against the TRK. Also there are usage adjustments against the TRK that occur before the event,
    * as well as, while the TRK is installed.
    *
    * The snapshot will reflect the TSN of the last adjustment before the event.
    *
    * @throws Exception
    */
   @Test
   public void forTrk_whenInstallsAndRemovalsAfterEvent() throws Exception {

      // Set up
      // - adjustment 1: Jan-01, delta=1 tsn=1
      // - event: Jan-02
      // - install record 1: Jan-03, tsn=1
      // - adjustment 2: Jan-04, delta=2 tsn=3
      // - remove record 1: Jan-05, tsn=3
      // - install record 2: Jan-06, tsn=3
      // - adjustment 3: Jan-07, delta=4 tsn=7
      // - remove record 2: Jan-08, tsn=7
      // - current: tsn=7

      final Date lAdjustmentDate1 = toDate( "01-JAN-2010" );
      final BigDecimal lAdjustmentDelta1 = valueOf( 1.0 );
      final BigDecimal lAdjustmentTsn1 = valueOf( 1.0 );
      final Date lEventDate = toDate( "02-JAN-2010" );
      final Date lInstallDate1 = toDate( "03-JAN-2010" );
      final BigDecimal lInstallTsn1 = valueOf( 1.0 );
      final Date lAdjustmentDate2 = toDate( "04-JAN-2010" );
      final BigDecimal lAdjustmentDelta2 = valueOf( 2.0 );
      final BigDecimal lAdjustmentTsn2 = valueOf( 3.0 );
      final Date lRemoveDate1 = toDate( "05-JAN-2010" );
      final BigDecimal lRemoveTsn1 = valueOf( 3.0 );
      final Date lInstallDate2 = toDate( "06-JAN-2010" );
      final BigDecimal lInstallTsn2 = valueOf( 3.0 );
      final Date lAdjustmentDate3 = toDate( "07-JAN-2010" );
      final BigDecimal lAdjustmentDelta3 = valueOf( 4.0 );
      final BigDecimal lAdjustmentTsn3 = valueOf( 7.0 );
      final Date lRemoveDate2 = toDate( "08-JAN-2010" );
      final BigDecimal lRemoveTsn2 = valueOf( 7.0 );
      final BigDecimal lCurrentTsn = valueOf( 7.0 );

      // Given an aircraft to install the TRK.
      final InventoryKey lAcft = Domain.createAircraft();

      // Given a TRK.
      final InventoryKey lTrk = createTracked( lCurrentTsn );

      // Given a usage adjustment before the event.
      createCorrection( lTrk, lAdjustmentDate1, lAdjustmentTsn1, lAdjustmentDelta1 );

      // Given an event before the install.
      TaskKey lReq = createRequirement( lTrk, lEventDate );

      // Given the first install record.
      createInstallRecord( lAcft, lTrk, lInstallDate1, lInstallTsn1 );

      // Given a usage adjustment while installed (first install).
      createFlight( lAcft, lAdjustmentDate2, lAdjustmentTsn2, lAdjustmentDelta2 );

      // Given the first remove record.
      createRemoveRecord( lAcft, lTrk, lRemoveDate1, lRemoveTsn1 );

      // Given the second install record.
      createInstallRecord( lAcft, lTrk, lInstallDate2, lInstallTsn2 );

      // Given a usage adjustment while installed (second install).
      createFlight( lAcft, lAdjustmentDate3, lAdjustmentTsn3, lAdjustmentDelta3 );

      // Given the second remove record.
      createRemoveRecord( lAcft, lTrk, lRemoveDate2, lRemoveTsn2 );

      // When a usage snapshot is taken for the event.
      UsageSnapshotService.takeUsageSnapshotOfEvent( lReq.getEventKey() );

      // Then the usage snapshot reflects the usage at the time of the event,
      // which is the usage of the first adjustment.
      BigDecimal lExpectedTsn = lAdjustmentTsn1;
      BigDecimal lActualTsn = getUsageOfSnapshot( lReq, lTrk, HOURS );
      assertEquals( "Unexpected tsn in usage snapshot of event.", lExpectedTsn, lActualTsn );

   }


   /**
    *
    * Verify that a usage snapshot is taken when there are TRK installs and removals both before and
    * after an event against the TRK. Also there are usage adjustments against the TRK that occur
    * before the installs, as well as, while the TRK is installed.
    *
    * The snapshot will reflect the TSN of the last adjustment, while the TRK is installed, before
    * the event.
    *
    * @throws Exception
    */
   @Test
   public void forTrk_whenInstallsAndRemovalsBeforeAndAfterEvent() throws Exception {

      // Set up
      // - adjustment 1: Jan-01, delta=1 tsn=1
      // - install record 1: Jan-02, tsn=1
      // - adjustment 2: Jan-03, delta=2 tsn=3
      // - remove record 1: Jan-04, tsn=3
      // - event: Jan-05
      // - install record 2: Jan-06, tsn=3
      // - adjustment 3: Jan-07, delta=4 tsn=7
      // - remove record 2: Jan-08, tsn=7
      // - current: tsn=7

      final Date lAdjustmentDate1 = toDate( "01-JAN-2010" );
      final BigDecimal lAdjustmentDelta1 = valueOf( 1.0 );
      final BigDecimal lAdjustmentTsn1 = valueOf( 1.0 );
      final Date lInstallDate1 = toDate( "02-JAN-2010" );
      final BigDecimal lInstallTsn1 = valueOf( 1.0 );
      final Date lAdjustmentDate2 = toDate( "03-JAN-2010" );
      final BigDecimal lAdjustmentDelta2 = valueOf( 2.0 );
      final BigDecimal lAdjustmentTsn2 = valueOf( 3.0 );
      final Date lRemoveDate1 = toDate( "04-JAN-2010" );
      final BigDecimal lRemoveTsn1 = valueOf( 3.0 );
      final Date lEventDate = toDate( "05-JAN-2010" );
      final Date lInstallDate2 = toDate( "06-JAN-2010" );
      final BigDecimal lInstallTsn2 = valueOf( 3.0 );
      final Date lAdjustmentDate3 = toDate( "07-JAN-2010" );
      final BigDecimal lAdjustmentDelta3 = valueOf( 4.0 );
      final BigDecimal lAdjustmentTsn3 = valueOf( 7.0 );
      final Date lRemoveDate2 = toDate( "08-JAN-2010" );
      final BigDecimal lRemoveTsn2 = valueOf( 7.0 );
      final BigDecimal lCurrentTsn = valueOf( 7.0 );

      // Given an aircraft to install the TRK.
      final InventoryKey lAcft = Domain.createAircraft();

      // Given a TRK.
      final InventoryKey lTrk = createTracked( lCurrentTsn );

      // Given a usage adjustment before the install.
      createCorrection( lTrk, lAdjustmentDate1, lAdjustmentTsn1, lAdjustmentDelta1 );

      // Given the first install record.
      createInstallRecord( lAcft, lTrk, lInstallDate1, lInstallTsn1 );

      // Given a usage adjustment while installed (first install).
      createFlight( lAcft, lAdjustmentDate2, lAdjustmentTsn2, lAdjustmentDelta2 );

      // Given the first remove record.
      createRemoveRecord( lAcft, lTrk, lRemoveDate1, lRemoveTsn1 );

      // Given an event before the second install.
      TaskKey lReq = createRequirement( lTrk, lEventDate );

      // Given the second install record.
      createInstallRecord( lAcft, lTrk, lInstallDate2, lInstallTsn2 );

      // Given a usage adjustment while installed (second install).
      createFlight( lAcft, lAdjustmentDate3, lAdjustmentTsn3, lAdjustmentDelta3 );

      // Given the second remove record.
      createRemoveRecord( lAcft, lTrk, lRemoveDate2, lRemoveTsn2 );

      // When a usage snapshot is taken for the event.
      UsageSnapshotService.takeUsageSnapshotOfEvent( lReq.getEventKey() );

      // Then the usage snapshot reflects the usage at the time of the event,
      // which is the usage of the first removal.
      BigDecimal lExpectedTsn = lRemoveTsn1;
      BigDecimal lActualTsn = getUsageOfSnapshot( lReq, lTrk, HOURS );
      assertEquals( "Unexpected tsn in usage snapshot of event.", lExpectedTsn, lActualTsn );

   }


   /**
    *
    * Verify that a usage snapshot is taken when the TRK was installed after the event and there are
    * adjustments before the event. Also there are usage adjustments against the TRK, while the TRK
    * is installed, that consist of all the various types of adjustments.
    *
    * The snapshot will reflect the TSN of the last adjustment before the event.
    *
    * @throws Exception
    */
   @Test
   public void forTrk_whenInstallAfterEventAndAllTypesOfAdjustmentsAfterInstall() throws Exception {

      // Set up
      // - adjustment 1: Jan-01, delta=1 tsn=1
      // - event: Jan-02
      // - install record 1: Jan-03, tsn=1
      // - adjustment 2: Jan-04, delta=2 tsn=3 (correction)
      // - adjustment 3: Jan-05, delta=3 tsn=6 (flight)
      // - adjustment 4: Jan-06, delta=4 tsn=10 (accrual)
      // - current tsn: 10

      final Date lAdjustmentDate1 = toDate( "01-JAN-2010" );
      final BigDecimal lAdjustmentDelta1 = valueOf( 1.0 );
      final BigDecimal lAdjustmentTsn1 = valueOf( 1.0 );
      final Date lEventDate = toDate( "02-JAN-2010" );
      final Date lInstallDate = toDate( "03-JAN-2010" );
      final BigDecimal lInstallTsn = valueOf( 1.0 );
      final Date lAdjustmentDate2 = toDate( "04-JAN-2010" );
      final BigDecimal lAdjustmentDelta2 = valueOf( 2.0 );
      final BigDecimal lAdjustmentTsn2 = valueOf( 3.0 );
      final Date lAdjustmentDate3 = toDate( "05-JAN-2010" );
      final BigDecimal lAdjustmentDelta3 = valueOf( 3.0 );
      final BigDecimal lAdjustmentTsn3 = valueOf( 6.0 );
      final Date lAdjustmentDate4 = toDate( "06-JAN-2010" );
      final BigDecimal lAdjustmentDelta4 = valueOf( 4.0 );
      final BigDecimal lAdjustmentTsn4 = valueOf( 10.0 );
      final BigDecimal lCurrentTsn = valueOf( 10.0 );

      // Given an aircraft.
      final InventoryKey lAcft = Domain.createAircraft();

      // Given a TRK.
      final InventoryKey lTrk = createTracked( lCurrentTsn );

      // Given a usage adjustment before the install.
      createCorrection( lTrk, lAdjustmentDate1, lAdjustmentTsn1, lAdjustmentDelta1 );

      // Given the install record.
      createInstallRecord( lAcft, lTrk, lInstallDate, lInstallTsn );

      // Given a CORRECTION usage adjustment against the installed TRK before the event.
      createCorrection( lTrk, lAdjustmentDate2, lAdjustmentTsn2, lAdjustmentDelta2 );

      // Given a FLIGHT usage adjustment against the aircraft before the event.
      createFlight( lAcft, lAdjustmentDate3, lAdjustmentTsn3, lAdjustmentDelta3 );

      // Given an ACCRUAL usage adjustment against the aircraft before the event.
      createAccrual( lAcft, lAdjustmentDate4, lAdjustmentTsn4, lAdjustmentDelta4 );

      // Given an event after the adjustment.
      TaskKey lReq = createRequirement( lTrk, lEventDate );

      // When a usage snapshot is taken for the event.
      UsageSnapshotService.takeUsageSnapshotOfEvent( lReq.getEventKey() );

      // Then the usage snapshot reflects the usage at the time of the event,
      // which is the usage of the first adjustment.
      BigDecimal lExpectedTsn = lAdjustmentTsn1;
      BigDecimal lActualTsn = getUsageOfSnapshot( lReq, lTrk, HOURS );
      assertEquals( "Unexpected tsn in usage snapshot of event.", lExpectedTsn, lActualTsn );
   }


   /**
    *
    * Verify that the usage snapshot of an event captures all the usage data types tracked by the
    * TRK.
    *
    */
   @Test
   public void forTrk_whenTrackingManyDataTypesAndAdjustmentsBeforeEvent() throws Exception {

      // Set up
      // - adjustment 1: Jan-01, HOURS delta=10 tsn=10, CYCLES delta=1 tsn=1
      // - event: Jan-02
      // - install record: Jan-03, HOURS tsn=10, CYCLES tsn=1
      // - adjustment 2: Jan-04, HOURS delta=5 tsn=15, CYCLES delta=2 tsn=3
      // - current: HOURS tsn=15, CYCLES tsn=3

      final Date lAdjustmentDate1 = toDate( "01-JAN-2010" );
      final BigDecimal lAdjustmentHoursDelta1 = valueOf( 10.0 );
      final BigDecimal lAdjustmentHoursTsn1 = valueOf( 10.0 );
      final BigDecimal lAdjustmentCyclesDelta1 = valueOf( 1.0 );
      final BigDecimal lAdjustmentCyclesTsn1 = valueOf( 1.0 );
      final Date lEventDate = toDate( "02-JAN-2010" );
      final Date lInstallDate = toDate( "03-JAN-2010" );
      final BigDecimal lInstallHoursTsn = valueOf( 10.0 );
      final BigDecimal lInstallCyclesTsn = valueOf( 1.0 );
      final Date lAdjustmentDate2 = toDate( "04-JAN-2010" );
      final BigDecimal lAdjustmentHoursDelta2 = valueOf( 5.0 );
      final BigDecimal lAdjustmentHoursTsn2 = valueOf( 15.0 );
      final BigDecimal lAdjustmentCyclesDelta2 = valueOf( 2.0 );
      final BigDecimal lAdjustmentCyclesTsn2 = valueOf( 3.0 );
      final BigDecimal lCurrentHoursTsn = valueOf( 15.0 );
      final BigDecimal lCurrentCyclesTsn = valueOf( 3.0 );

      // Given an aircraft to install the TRK.
      final InventoryKey lAcft = Domain.createAircraft();

      // Given a TRK tracking many usage data types.
      final InventoryKey lTrk =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aBuilder ) {
                  aBuilder.addUsage( HOURS, lCurrentHoursTsn );
                  aBuilder.addUsage( CYCLES, lCurrentCyclesTsn );
               }
            } );

      // Given a usage adjustment before the event.
      Domain.createUsageAdjustment( new DomainConfiguration<UsageAdjustment>() {

         @Override
         public void configure( UsageAdjustment aBuilder ) {
            aBuilder.setMainInventory( lTrk );
            aBuilder.setUsageDate( lAdjustmentDate1 );
            aBuilder.setUsageType( MXCORRECTION );
            aBuilder.addUsage( lTrk, HOURS, lAdjustmentHoursTsn1, lAdjustmentHoursDelta1 );
            aBuilder.addUsage( lTrk, CYCLES, lAdjustmentCyclesTsn1, lAdjustmentCyclesDelta1 );
         }
      } );

      // Given an event before the install.
      TaskKey lReq = createRequirement( lTrk, lEventDate );

      // Given an install record.
      Set<UsageSnapshot> aInstallUsages = new HashSet<UsageSnapshot>();
      aInstallUsages.add( new UsageSnapshot( lTrk, HOURS, lInstallHoursTsn.doubleValue() ) );
      aInstallUsages.add( new UsageSnapshot( lTrk, CYCLES, lInstallCyclesTsn.doubleValue() ) );
      AttachmentService.attachInventoryToAircraft( lAcft, lTrk, lInstallDate, aInstallUsages );

      // Given a usage adjustment while installed.
      Domain.createUsageAdjustment( new DomainConfiguration<UsageAdjustment>() {

         @Override
         public void configure( UsageAdjustment aBuilder ) {
            aBuilder.setMainInventory( lAcft );
            aBuilder.setUsageDate( lAdjustmentDate2 );
            aBuilder.setUsageType( MXFLIGHT );
            aBuilder.addUsage( lAcft, HOURS, lAdjustmentHoursTsn2, lAdjustmentHoursDelta2 );
            aBuilder.addUsage( lAcft, CYCLES, lAdjustmentCyclesTsn2, lAdjustmentCyclesDelta2 );
         }
      } );

      // When a usage snapshot is taken for the event.
      UsageSnapshotService.takeUsageSnapshotOfEvent( lReq.getEventKey() );

      // Then the usage snapshot reflects the usage at the time of the event,
      // which is the usage of the first adjustment.
      BigDecimal lExpectedHoursTsn = lAdjustmentHoursTsn1;
      BigDecimal lExpectedCyclesTsn = lAdjustmentCyclesTsn1;
      BigDecimal lActualHoursTsn = getUsageOfSnapshot( lReq, lTrk, HOURS );
      BigDecimal lActualCyclesTsn = getUsageOfSnapshot( lReq, lTrk, CYCLES );
      assertEquals( "Unexpected HOURS tsn in usage snapshot of event.", lExpectedHoursTsn,
            lActualHoursTsn );
      assertEquals( "Unexpected CYCLES tsn in usage snapshot of event.", lExpectedCyclesTsn,
            lActualCyclesTsn );
   }


   /**
    *
    * Verify that when the TRK is currently tracking different usage data types then the usage data
    * types being track at the time of an event in the past, that the usage snapshot for that event
    * contains the all the data types. However, the data types that were not being tracked at the
    * time of the event will have a value of zero.
    */
   @Test
   public void forTrk_whenDataTypeCurrentlyTrackedButNotTrackedAtTimeOfEvent() throws Exception {

      // Set up
      // - install record 1: Jan-01, HOURS tsn=1, CYCLES tsn=2
      // - adjustment 1: Jan-02, HOURS delta=3 tsn=4, CYCLES delta=4 tsn=6
      // - event: Jan-03
      // - adjustment 2: Jan-04, LANDING delta=5 tsn=5
      // - current: HOURS tsn=4, CYCLES tsn=6, LANDING tsn=5
      final Date lInstallDate = toDate( "01-JAN-2010" );
      final BigDecimal lInstallHoursTsn = valueOf( 1.0 );
      final BigDecimal lInstallCyclesTsn = valueOf( 2.0 );
      final Date lAdjustmentDate1 = toDate( "02-JAN-2010" );
      final BigDecimal lAdjustmentHoursDelta1 = valueOf( 3.0 );
      final BigDecimal lAdjustmentHoursTsn1 = valueOf( 4.0 );
      final BigDecimal lAdjustmentCyclesDelta1 = valueOf( 4.0 );
      final BigDecimal lAdjustmentCyclesTsn1 = valueOf( 6.0 );
      final Date lEventDate = toDate( "03-JAN-2010" );
      final Date lAdjustmentDate2 = toDate( "04-JAN-2010" );
      final BigDecimal lAdjustmentLandingDelta2 = valueOf( 5.0 );
      final BigDecimal lAdjustmentLandingTsn2 = valueOf( 5.0 );
      final BigDecimal lCurrentHoursTsn = valueOf( 4.0 );
      final BigDecimal lCurrentCyclesTsn = valueOf( 6.0 );
      final BigDecimal lCurrentLandingTsn = valueOf( 5.0 );

      // Given an aircraft to install the TRK.
      final InventoryKey lAcft = Domain.createAircraft();

      // Given a TRK tracking many usage data types.
      final InventoryKey lTrk =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aBuilder ) {
                  aBuilder.addUsage( HOURS, lCurrentHoursTsn );
                  aBuilder.addUsage( CYCLES, lCurrentCyclesTsn );
                  aBuilder.addUsage( LANDING, lCurrentLandingTsn );
               }
            } );

      // Given an install record.
      Set<UsageSnapshot> aInstallUsages = new HashSet<UsageSnapshot>();
      aInstallUsages.add( new UsageSnapshot( lTrk, HOURS, lInstallHoursTsn.doubleValue() ) );
      aInstallUsages.add( new UsageSnapshot( lTrk, CYCLES, lInstallCyclesTsn.doubleValue() ) );
      AttachmentService.attachInventoryToAircraft( lAcft, lTrk, lInstallDate, aInstallUsages );

      // Given a usage adjustment before the event.
      Domain.createUsageAdjustment( new DomainConfiguration<UsageAdjustment>() {

         @Override
         public void configure( UsageAdjustment aBuilder ) {
            aBuilder.setMainInventory( lAcft );
            aBuilder.setUsageDate( lAdjustmentDate1 );
            aBuilder.setUsageType( MXFLIGHT );
            aBuilder.addUsage( lAcft, HOURS, lAdjustmentHoursTsn1, lAdjustmentHoursDelta1 );
            aBuilder.addUsage( lAcft, CYCLES, lAdjustmentCyclesTsn1, lAdjustmentCyclesDelta1 );
         }
      } );

      // Given an event after the adjustment.
      TaskKey lReq = createRequirement( lTrk, lEventDate );

      // Given a usage adjustment after the event.
      Domain.createUsageAdjustment( new DomainConfiguration<UsageAdjustment>() {

         @Override
         public void configure( UsageAdjustment aBuilder ) {
            aBuilder.setMainInventory( lAcft );
            aBuilder.setUsageDate( lAdjustmentDate2 );
            aBuilder.setUsageType( MXFLIGHT );
            aBuilder.addUsage( lAcft, LANDING, lAdjustmentLandingTsn2, lAdjustmentLandingDelta2 );
         }
      } );

      // When a usage snapshot is taken for the event.
      UsageSnapshotService.takeUsageSnapshotOfEvent( lReq.getEventKey() );

      // Then the usage snapshot reflects the usage at the time of the event,
      // which is the usage of the last adjustment before the event.
      BigDecimal lExpectedHoursTsn = lAdjustmentHoursTsn1;
      BigDecimal lExpectedCyclesTsn = lAdjustmentCyclesTsn1;

      BigDecimal lActualHoursTsn = getUsageOfSnapshot( lReq, lTrk, HOURS );
      BigDecimal lActualCyclesTsn = getUsageOfSnapshot( lReq, lTrk, CYCLES );

      assertEquals( "Unexpected hours tsn in usage snapshot of event.", lExpectedHoursTsn,
            lActualHoursTsn );
      assertEquals( "Unexpected cycles tsn in usage snapshot of event.", lExpectedCyclesTsn,
            lActualCyclesTsn );

      // The usage snapshot also contains the curent usage data types there were not tracked by the
      // aircraft at the time of the event but their value is zero.
      BigDecimal lExpectedLandingTsn = BigDecimal.valueOf( 0.0 );
      BigDecimal lActualLandingTsn = getUsageOfSnapshot( lReq, lTrk, LANDING );
      assertEquals( "Unexpected landing tsn in usage snapshot of event.", lExpectedLandingTsn,
            lActualLandingTsn );

   }


   /**
    *
    * Verify that a usage snapshot is taken when there are usage adjustments before an event against
    * a SER. The snapshot will reflect the TSN of adjustment immediately before the event for the
    * SER.
    *
    * @throws Exception
    */
   @Test
   public void forSer_whenAdjustmentsBeforeEvent() throws Exception {

      // Set up
      // - adjustment 1: Jan-01, delta=1 tsn=1
      // - adjustment 2: Jan-02, delta=2 tsn=3
      // - event: Jan-03
      // - adjustment 3: Jan-04, delta=3 tsn=6
      // - current tsn: 6

      final Date lAdjustmentDate1 = toDate( "01-JAN-2010" );
      final BigDecimal lAdjustmentDelta1 = valueOf( 1.0 );
      final BigDecimal lAdjustmentTsn1 = valueOf( 1.0 );
      final Date lAdjustmentDate2 = toDate( "02-JAN-2010" );
      final BigDecimal lAdjustmentDelta2 = valueOf( 2.0 );
      final BigDecimal lAdjustmentTsn2 = valueOf( 3.0 );
      final Date lEventDate = toDate( "03-JAN-2010" );
      final Date lAdjustmentDate3 = toDate( "04-JAN-2010" );
      final BigDecimal lAdjustmentDelta3 = valueOf( 3.0 );
      final BigDecimal lAdjustmentTsn3 = valueOf( 6.0 );
      final BigDecimal lCurrentTsn = valueOf( 6.0 );

      // Given a SER.
      InventoryKey lSer =
            Domain.createSerializedInventory( new DomainConfiguration<SerializedInventory>() {

               @Override
               public void configure( SerializedInventory aBuilder ) {
                  aBuilder.addUsage( HOURS, lCurrentTsn );
                  aBuilder.setParent( Domain.createAircraft() );
               }
            } );

      // Given a usage adjustment before the event.
      createCorrection( lSer, lAdjustmentDate1, lAdjustmentTsn1, lAdjustmentDelta1 );

      // Given a usage adjustment before the event.
      createCorrection( lSer, lAdjustmentDate2, lAdjustmentTsn2, lAdjustmentDelta2 );

      // Given an event after the adjustment.
      TaskKey lReq = createRequirement( lSer, lEventDate );

      // Given another usage adjustment after the event.
      createCorrection( lSer, lAdjustmentDate3, lAdjustmentTsn3, lAdjustmentDelta3 );

      // When a usage snapshot is taken for the event.
      UsageSnapshotService.takeUsageSnapshotOfEvent( lReq.getEventKey() );

      // Then the usage snapshot reflects the usage of the adjustment immediately before the event.
      BigDecimal lExpectedTsn = lAdjustmentTsn2;
      BigDecimal lActualTsn = getUsageOfSnapshot( lReq, lSer, HOURS );
      assertEquals( "Unexpected tsn in usage snapshot of event.", lExpectedTsn, lActualTsn );
   }


   /**
    * Description: Work package usage snapshot will show the correct usage of all accumulated
    * parameters for the engine at the time the work package was completed
    *
    * <pre>
    * Given two engine part numbers, each representing a unique thrust rating
    * Given an engine assembly and an engine based on it
    * Given two accumulated parameters one for each part number assigned to the engine assembly
    * Given the engine is tracking the accumulated parameters and has current usage containing accumulated parameters
    * Given an aircraft and the engine is attached to it
    * Given a work package against the aircraft
    * Given an usage adjustment without accumulated parameters exists for engine before the work package end date
    * When a usage snapshot is taken for the work package
    * Then the engine's accumulated parameters in work package usage snapshot will show the current usage values
    * for the accumulated parameters of the engine
    *
    * </pre>
    */
   @Test
   public void itTakesWorkPackageUsageSnapshotOfAssmblyTrackingAccumulatedParms() throws Exception {

      final PartNoKey lLowThrustRatingEnginePartNo = createEnginePartNo();
      final PartNoKey lHighThrustRatingEnginePartNo = createEnginePartNo();

      final AssemblyKey lEngineAssembly =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aEngineAssembly ) {
                  aEngineAssembly.setCode( ASSEMBLY_CODE );
                  aEngineAssembly.addUsageDefinitionConfiguration(
                        new DomainConfiguration<UsageDefinition>() {

                           @Override
                           public void configure( UsageDefinition aBuilder ) {
                              aBuilder.addAccumulatedParameterConfiguration(
                                    new DomainConfiguration<AccumulatedParameter>() {

                                       @Override
                                       public void configure( AccumulatedParameter aBuilder ) {
                                          aBuilder.setAggregatedDataType( CYCLES );
                                          aBuilder.setPartNoKey( lLowThrustRatingEnginePartNo );
                                          aBuilder.setCode( ACC_CYCLES_LOW_THRUST_RATING );

                                       }
                                    } );
                              aBuilder.addAccumulatedParameterConfiguration(
                                    new DomainConfiguration<AccumulatedParameter>() {

                                       @Override
                                       public void configure( AccumulatedParameter aBuilder ) {
                                          aBuilder.setAggregatedDataType( CYCLES );
                                          aBuilder.setPartNoKey( lHighThrustRatingEnginePartNo );
                                          aBuilder.setCode( ACC_CYCLES_HIGH_THRUST_RATING );
                                       }
                                    } );
                           }
                        } );
                  aEngineAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aRootCs ) {
                              aRootCs.addPartGroup( new DomainConfiguration<PartGroup>() {

                                 @Override
                                 public void configure( PartGroup aPartGroup ) {
                                    aPartGroup.addPart( lLowThrustRatingEnginePartNo );
                                    aPartGroup.addPart( lHighThrustRatingEnginePartNo );
                                 }
                              } );
                           }
                        } );
               }
            } );

      final DataTypeKey lLowThrustRatingCycle = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lEngineAssembly ), ACC_CYCLES_LOW_THRUST_RATING );
      final DataTypeKey lHighThrustRatingCycle = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lEngineAssembly ), ACC_CYCLES_HIGH_THRUST_RATING );

      final InventoryKey lAircraft = Domain.createAircraft();

      final BigDecimal lLowRatingCyclesCurrentTsn = BigDecimal.valueOf( 20.0 );
      final BigDecimal lHighRatingCyclesCurrentTsn = BigDecimal.TEN;
      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aBuilder ) {
            aBuilder.setParent( lAircraft );
            aBuilder.addUsage( lLowThrustRatingCycle, lLowRatingCyclesCurrentTsn );
            aBuilder.addUsage( lHighThrustRatingCycle, lHighRatingCyclesCurrentTsn );
         }
      } );

      final Date lWorkPackageEndDate = DateUtils.addDays( new Date(), -10 );
      final Date lUsageAdjustmentDate = DateUtils.addDays( lWorkPackageEndDate, -2 );
      final TaskKey lWorkPackage =
            Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

               @Override
               public void configure( WorkPackage aBuilder ) {
                  aBuilder.setAircraft( lAircraft );
                  aBuilder.addSubAssembly( lEngine );
                  aBuilder.setActualEndDate( lWorkPackageEndDate );
               }
            } );
      Domain.createUsageAdjustment( new DomainConfiguration<UsageAdjustment>() {

         @Override
         public void configure( UsageAdjustment aBuilder ) {
            aBuilder.setMainInventory( lEngine );
            aBuilder.setUsageDate( lUsageAdjustmentDate );
            aBuilder.setUsageType( MXFLIGHT );
         }
      } );

      // When a usage snapshot is taken for the event.
      UsageSnapshotService.takeUsageSnapshotOfEvent( lWorkPackage.getEventKey() );

      BigDecimal lExpectedWPEngineLowThrustRatingCycle = lLowRatingCyclesCurrentTsn;
      BigDecimal lExpectedWPEngineHighThrustRatingCycle = lHighRatingCyclesCurrentTsn;

      BigDecimal lActualWPEngineLowThrustRatingCycle =
            getUsageOfSnapshot( lWorkPackage, lEngine, lLowThrustRatingCycle );
      BigDecimal lActualWPEngineHighThrustRatingCycle =
            getUsageOfSnapshot( lWorkPackage, lEngine, lHighThrustRatingCycle );

      assertEquals(
            "Unexpected Engine Low Thrust Rating Cycle tsn in usage snapshot of work package.", 0,
            lExpectedWPEngineLowThrustRatingCycle
                  .compareTo( lActualWPEngineLowThrustRatingCycle ) );
      assertEquals(
            "Unexpected Engine Low Thrust Rating Cycle tsn in usage snapshot of work package.", 0,
            lExpectedWPEngineHighThrustRatingCycle
                  .compareTo( lActualWPEngineHighThrustRatingCycle ) );
   }


   /**
    * Description: Work package usage snapshot will show the correct usage of calculated parameters
    * for the aircraft and engine at the time the work package was completed
    *
    * <pre>
    * Given an aircraft assembly and an aircraft based on it
    * Given an engine assembly and an engine based on it
    * Given the aircraft and the engine are tracking a standard usage parameter and have current usage
    * Given the aircraft and the engine are tracking a calculated usage parameter based on the standard usage
    * parameter defined against the aircraft assembly
    * Given an aircraft and the engine is attached to it
    * Given a work package against the aircraft
    * Given an usage adjustment with standard usage parameter and delta but without the calculated parameter
    * exists for aircraft and engine after the work package end date
    * When a usage snapshot is taken for the work package
    * Then the calculated parameter values for aircraft and engine in work package usage snapshot will be
    * twice the difference between the current aircraft and engine cycles & usage adjustment delta
    *
    * </pre>
    */
   @Test
   public void itTakesWorkPackageUsageSnapshotOfAircraftAndAssmblyTrackingCalculatedParms()
         throws Exception {

      // createCalculationInDatabase() method should be called prior to any data setup in database
      // as the method performs an explicit roll-back followed by an implicit database commit
      createCalculationInDatabase();

      final AssemblyKey lAcftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aBuilder ) {
                  aBuilder.setCode( ASSEMBLY_CODE );
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        aBuilder
                              .addConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                                 @Override
                                 public void configure( ConfigurationSlot aBuilder ) {

                                    aBuilder.setConfigurationSlotClass( RefBOMClassKey.SUBASSY );
                                    aBuilder.setCode( ACFT_ENGINE_CONFIG_SLOT_CODE );
                                 }
                              } );
                        aBuilder.addUsageParameter( DataTypeKey.CYCLES );
                        aBuilder.addCalculatedUsageParameter(
                              new DomainConfiguration<CalculatedUsageParameter>() {

                                 @Override
                                 public void configure( CalculatedUsageParameter aBuilder ) {
                                    aBuilder.setCode( CALC_USAGE_PARM_CODE );
                                    aBuilder.setDatabaseCalculation( CALCULATION );
                                    aBuilder.setPrecisionQt( 2 );
                                    aBuilder.addConstant( CALCULATION_CONSTANT,
                                          CALCULATION_CONSTANT_VALUE );
                                    aBuilder.addParameter( DataTypeKey.CYCLES );
                                 }
                              } );
                     }
                  } );
               }
            } );

      final ConfigSlotKey lAcftEngineConfigSlot =
            EqpAssmblBom.getBomItemKey( ASSEMBLY_CODE, ACFT_ENGINE_CONFIG_SLOT_CODE );

      final DataTypeKey lCalcParmDataType = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lAcftAssembly ), CALC_USAGE_PARM_CODE );

      final BigDecimal lCurrentCyclesTsn = new BigDecimal( 20 );
      final BigDecimal lCurrentCalcParmTsn = new BigDecimal( 40 );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.setAssembly( lAcftAssembly );
            aBuilder.addUsage( lCalcParmDataType, lCurrentCalcParmTsn );
            aBuilder.addUsage( CYCLES, lCurrentCyclesTsn );
         }
      } );

      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aBuilder ) {
            aBuilder.setParent( lAircraft );
            aBuilder.setPosition( new ConfigSlotPositionKey( lAcftEngineConfigSlot, 1 ) );
            aBuilder.addUsage( lCalcParmDataType, lCurrentCalcParmTsn );
            aBuilder.addUsage( CYCLES, lCurrentCyclesTsn );
         }
      } );

      final Date lWorkPackageEndDate = DateUtils.addDays( new Date(), -10 );
      final Date lUsageAdjustmentDate = DateUtils.addDays( lWorkPackageEndDate, 2 );
      final TaskKey lWorkPackage =
            Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

               @Override
               public void configure( WorkPackage aBuilder ) {
                  aBuilder.setAircraft( lAircraft );
                  aBuilder.addSubAssembly( lEngine );
                  aBuilder.setActualEndDate( lWorkPackageEndDate );
               }
            } );

      final BigDecimal lCyclesDeltaUsageAdjustment = new BigDecimal( 5 );
      Domain.createUsageAdjustment( new DomainConfiguration<UsageAdjustment>() {

         @Override
         public void configure( UsageAdjustment aBuilder ) {
            aBuilder.setMainInventory( lAircraft );
            aBuilder.setUsageDate( lUsageAdjustmentDate );
            aBuilder.setUsageType( MXFLIGHT );
            aBuilder.addUsage( lAircraft, CYCLES, lCurrentCyclesTsn, lCyclesDeltaUsageAdjustment );
            aBuilder.addUsage( lEngine, CYCLES, lCurrentCyclesTsn, lCyclesDeltaUsageAdjustment );
         }
      } );

      // When a usage snapshot is taken for the event.
      UsageSnapshotService.takeUsageSnapshotOfEvent( lWorkPackage.getEventKey() );

      BigDecimal lExpectedWPAcftCycles = lCurrentCyclesTsn.subtract( lCyclesDeltaUsageAdjustment );
      BigDecimal lExpectedWPEngineCycles =
            lCurrentCyclesTsn.subtract( lCyclesDeltaUsageAdjustment );
      BigDecimal lExpectedWPAcftCalcParmTsn =
            lExpectedWPAcftCycles.multiply( CALCULATION_CONSTANT_VALUE );
      BigDecimal lExpectedEngineCalcParmTsn =
            lExpectedWPEngineCycles.multiply( CALCULATION_CONSTANT_VALUE );

      BigDecimal lActualWPAcftCalcParmTsn =
            getUsageOfSnapshot( lWorkPackage, lAircraft, lCalcParmDataType );
      BigDecimal lActualWPEngineCalcParmTsn =
            getUsageOfSnapshot( lWorkPackage, lEngine, lCalcParmDataType );

      assertEquals(
            "Unexpected Aircraft Calculated Parameter tsn in usage snapshot of work package.", 0,
            lExpectedWPAcftCalcParmTsn.compareTo( lActualWPAcftCalcParmTsn ) );
      assertEquals( "Unexpected Engine Calculated Parameter tsn in usage snapshot of work package.",
            0, lExpectedEngineCalcParmTsn.compareTo( lActualWPEngineCalcParmTsn ) );

      dropCalculationFromDatabase();
   }


   /***************************************************************************
    *
    * Private methods.
    *
    ***************************************************************************/

   private Date toDate( String aString ) throws Exception {
      return DateUtils.parseString( aString, "dd-MMM-yyyy" );
   }


   private InventoryKey createAircraft( final BigDecimal aCurrentTsn ) {
      return createAircraft( aCurrentTsn, null );
   }


   private InventoryKey createAircraft( final BigDecimal aCurrentTsn, final String aSystemName ) {
      InventoryKey lAcft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.addUsage( HOURS, aCurrentTsn );
            if ( aSystemName != null ) {
               aBuilder.addSystem( aSystemName );
            }
         }
      } );
      return lAcft;
   }


   private InventoryKey createTracked( final BigDecimal aCurrentTsn ) {
      InventoryKey lTrk =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aBuilder ) {
                  aBuilder.addUsage( HOURS, aCurrentTsn );
               }
            } );

      return lTrk;
   }


   private TaskKey createRequirement( final InventoryKey aAcft, final Date aEventDate ) {
      TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( aAcft );
            aReq.setActualEndDate( aEventDate );
         }
      } );
      return lReq;
   }


   private void createCorrection( final InventoryKey aAcft, final Date aDate, final BigDecimal aTsn,
         final BigDecimal aDelta ) {
      createAdjustment( UsageType.MXCORRECTION, aAcft, aDate, aTsn, aDelta );
   }


   private void createFlight( final InventoryKey aAcft, final Date aDate, final BigDecimal aTsn,
         final BigDecimal aDelta ) {
      createAdjustment( UsageType.MXFLIGHT, aAcft, aDate, aTsn, aDelta );
   }


   private void createAccrual( final InventoryKey aAcft, final Date aDate, final BigDecimal aTsn,
         final BigDecimal aDelta ) {
      createAdjustment( UsageType.MXACCRUAL, aAcft, aDate, aTsn, aDelta );
   }


   private void createAdjustment( final UsageType aType, final InventoryKey aMainInventory,
         final Date aDate, final BigDecimal aTsn, final BigDecimal aDelta ) {
      Domain.createUsageAdjustment( new DomainConfiguration<UsageAdjustment>() {

         @Override
         public void configure( UsageAdjustment aBuilder ) {
            aBuilder.setMainInventory( aMainInventory );
            aBuilder.setUsageDate( aDate );
            aBuilder.setUsageType( aType );
            aBuilder.addUsage( aMainInventory, HOURS, aTsn, aDelta );
         }
      } );
   }


   private void createInstallRecord( InventoryKey aAcft, InventoryKey aInv, Date aInstallDate,
         BigDecimal aInstallTsn ) {
      Set<UsageSnapshot> aInstallUsages = new HashSet<UsageSnapshot>();
      aInstallUsages.add( new UsageSnapshot( aInv, HOURS, aInstallTsn.doubleValue() ) );
      AttachmentService.attachInventoryToAircraft( aAcft, aInv, aInstallDate, aInstallUsages );
   }


   private void createRemoveRecord( InventoryKey aAcft, InventoryKey aInv, Date aRemoveDate,
         BigDecimal aRemoveTsn ) {
      Set<UsageSnapshot> aRemoveUsages = new HashSet<UsageSnapshot>();
      aRemoveUsages.add( new UsageSnapshot( aInv, HOURS, aRemoveTsn.doubleValue() ) );
      AttachmentService.detachInventoryFromAircraft( aAcft, aInv, aRemoveDate, aRemoveUsages );
   }


   private BigDecimal getUsageOfSnapshot( TaskKey aTask, InventoryKey aInv,
         DataTypeKey aDataType ) {
      EvtInvTable lEvtInv = EvtInvTable.findByEventAndInventory( aTask.getEventKey(), aInv );
      if ( lEvtInv == null ) {
         return null;
      }
      EventInventoryUsageKey lUsageKey = new EventInventoryUsageKey( lEvtInv.getPk(), aDataType );
      return valueOf( EvtInvUsageTable.findByPrimaryKey( lUsageKey ).getTsnQt() );
   }


   private PartNoKey createEnginePartNo() {
      return Domain.createPart( new DomainConfiguration<Part>() {

         @Override
         public void configure( Part aPart ) {
            aPart.setInventoryClass( RefInvClassKey.ASSY );

         }

      } );
   }


   private void createCalculationInDatabase() throws Exception {
      // Function creation is DDL which implicitly commits transaction
      // Domain.createCalculatedParameterEquationFunction() will perform explicit rollback before
      // function creation ensuring no data gets committed accidentally
      String lCreateFunctionStatement = "CREATE OR REPLACE FUNCTION " + CALCULATION + " ("
            + "aConstant NUMBER, aCyclesInput NUMBER" + " )" + " RETURN FLOAT" + " " + "IS "
            + "result FLOAT; " + "BEGIN" + " " + "result := aConstant * aCyclesInput ; " + "RETURN"
            + " " + " result;" + "END" + " " + CALCULATION + " ;";

      Domain.createCalculatedParameterEquationFunction( iDatabaseConnectionRule.getConnection(),
            lCreateFunctionStatement );

   }


   private void dropCalculationFromDatabase() throws SQLException {

      // Function dropping is DDL which implicitly commits transaction.
      // Domain.dropCalculatedParameterEquationFunction performs explicit rollback before function
      // drop ensuring no data gets committed accidentally.
      Domain.dropCalculatedParameterEquationFunction( iDatabaseConnectionRule.getConnection(),
            CALCULATION );
   }
}
