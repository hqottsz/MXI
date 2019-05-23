package com.mxi.mx.core.services.inventory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventInventoryKey;
import com.mxi.mx.core.key.EventInventoryUsageKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InvCndChgEventKey;
import com.mxi.mx.core.key.InvCndChgInvKey;
import com.mxi.mx.core.key.InvCndChgInvUsageKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefEventTypeKey;
import com.mxi.mx.core.key.RefStageReasonKey;
import com.mxi.mx.core.key.RefUsgSnapshotSrcTypeKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.event.inventory.UsageSnapshot;
import com.mxi.mx.core.table.acevent.InvCndChgEventDao;
import com.mxi.mx.core.table.acevent.InvCndChgEventTable;
import com.mxi.mx.core.table.acevent.InvCndChgInvUsageDao;
import com.mxi.mx.core.table.acevent.InvCndChgInvUsageTable;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.EvtInvTable;
import com.mxi.mx.core.table.evt.EvtInvUsageTable;


/**
 * This is to test class {@link InventoryEventRecorder}
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class InventoryEventRecorderTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();


   /**
    * Test that for an inventory without usage snapshot, a AC event is inserted into
    * inv_cnd_chg_event table and also duplicated into evt_event table by the trigger.
    *
    * Note: the assert of data insert into evt_event table will be removed once the AC event
    * conversion is complete and all AC events are removed from evt_event table.
    */
   @Test
   public void testRecordInventoryAcEventWithoutTakingUsageSnapshot() throws MxException {

      // DATA SETUP
      final InventoryKey aircraft = Domain.createAircraft();

      RefEventStatusKey eventStatusKey = RefEventStatusKey.ACARCHIVE;
      RefStageReasonKey conditionChgReason = RefStageReasonKey.ACDMCH;
      HumanResourceKey authorizingHr = Domain.createHumanResource();
      Date localDate = new Date();
      String title = "Aircraft is archived.";
      String note = "Aircraft is archived.";
      boolean isTakingUsageSnapshot = false;

      // ACTION
      InventoryEventRecorder inventoryEventRecorder = new InventoryEventRecorder();
      inventoryEventRecorder.recordInventoryAcEvent( aircraft, eventStatusKey, conditionChgReason,
            localDate, authorizingHr, title, note, isTakingUsageSnapshot );

      // ASSERT
      DataSetArgument queryArgs = new DataSetArgument();
      queryArgs.add( aircraft, "inv_no_db_id", "inv_no_id" );
      QuerySet queryResult =
            QuerySetFactory.getInstance().executeQueryTable( "inv_cnd_chg_inv", queryArgs );

      int expectedValue = 1;
      int actualValue = queryResult.getRowCount();
      assertEquals( "Expected 1 event associated to the inventory.", expectedValue, actualValue );

      queryResult.first();
      EventKey eventKey = queryResult.getKey( EventKey.class, "event_db_id", "event_id" );
      InvCndChgEventKey invCndChgEventKey =
            queryResult.getKey( InvCndChgEventKey.class, "event_db_id", "event_id" );

      // verify the AC event is inserted into inv_cnd_chg_event table
      InvCndChgEventDao invCndChgEventDao =
            InjectorContainer.get().getInstance( InvCndChgEventDao.class );
      InvCndChgEventTable invCndChgEventTable =
            invCndChgEventDao.findByPrimaryKey( invCndChgEventKey );

      assertTrue( "Expected a AC event in inv_cnd_chg_event talbe.", invCndChgEventTable.exists() );

      // verify the AC event is also inserted into evt_event table
      EvtEventDao evtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );
      EvtEventTable evtEventTable = evtEventDao.findByPrimaryKey( eventKey );

      assertTrue( "Expected a AC event in evt_event talbe.", evtEventTable.exists() );
   }


   /**
    * Test that for an inventory with usage snapshot, a AC event is inserted into inv_cnd_chg_event
    * table and also duplicated into evt_event table. The usage is inserted into
    * inv_cnd_chg_inv_usage table and duplicated into evt_inv_usage table.
    *
    * Note: the assert of data insert into evt_event and evt_inv_usage tables will be removed once
    * the AC event conversion is complete and all AC events are removed from evt_event table.
    *
    */
   @Test
   public void testRecordInventoryAcEventWithTakingUsageSnapshot() throws MxException {

      // DATA SETUP
      final InventoryKey aircraft = Domain.createAircraft( ac -> {
         ac.addUsage( DataTypeKey.CYCLES, new BigDecimal( 1 ) );
      } );

      RefEventStatusKey eventStatusKey = RefEventStatusKey.ACARCHIVE;
      RefStageReasonKey conditionChgReason = RefStageReasonKey.ACDMCH;
      HumanResourceKey authorizingHr = Domain.createHumanResource();
      Date localDate = new Date();
      String title = "Aircraft is archived.";
      String note = "Aircraft is archived.";
      boolean isTakingUsageSnapshot = true;

      // ACTION
      InventoryEventRecorder inventoryEventRecorder = new InventoryEventRecorder();
      inventoryEventRecorder.recordInventoryAcEvent( aircraft, eventStatusKey, conditionChgReason,
            localDate, authorizingHr, title, note, isTakingUsageSnapshot );

      // ASSERT
      DataSetArgument queryArgs = new DataSetArgument();
      queryArgs.add( aircraft, "inv_no_db_id", "inv_no_id" );
      QuerySet queryResult =
            QuerySetFactory.getInstance().executeQueryTable( "inv_cnd_chg_inv", queryArgs );

      int expectedValue = 1;
      int actualValue = queryResult.getRowCount();
      assertEquals( "Expected 1 event associated to the inventory.", expectedValue, actualValue );

      queryResult.first();
      EventKey eventKey = queryResult.getKey( EventKey.class, "event_db_id", "event_id" );
      InvCndChgEventKey invCndChgEventKey =
            queryResult.getKey( InvCndChgEventKey.class, "event_db_id", "event_id" );

      // verify the AC event is inserted into inv_cnd_chg_event table
      InvCndChgEventDao invCndChgEventDao =
            InjectorContainer.get().getInstance( InvCndChgEventDao.class );
      InvCndChgEventTable invCndChgEventTable =
            invCndChgEventDao.findByPrimaryKey( invCndChgEventKey );

      assertTrue( "Expected a AC event in inv_cnd_chg_event talbe.", invCndChgEventTable.exists() );

      // verify the AC event is also inserted into evt_event table
      EvtEventDao evtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );
      EvtEventTable evtEventTable = evtEventDao.findByPrimaryKey( eventKey );

      assertTrue( "Expected a AC event in evt_event talbe.", evtEventTable.exists() );

      // verify an entry is inserted into inv_cnd_chg_inv_usage table
      InvCndChgInvKey invCndChgInvKey =
            queryResult.getKey( InvCndChgInvKey.class, "event_db_id", "event_id", "event_inv_id" );
      InvCndChgInvUsageKey invCndChgInvUsageKey =
            new InvCndChgInvUsageKey( invCndChgInvKey, DataTypeKey.CYCLES );

      InvCndChgInvUsageDao invCndChgInvUsageDao =
            InjectorContainer.get().getInstance( InvCndChgInvUsageDao.class );
      InvCndChgInvUsageTable invCndChgInvUsageTable =
            invCndChgInvUsageDao.findByPrimaryKey( invCndChgInvUsageKey );

      assertTrue( "Expected an entry in inv_cnd_chg_event talbe associated to the inventory.",
            invCndChgInvUsageTable.exists() );

      // verify the same entry is also inserted into evt_inv_usage table
      queryArgs.clear();
      queryArgs.add( invCndChgInvUsageKey, "event_db_id", "event_id", "event_inv_id",
            "data_type_db_id", "data_type_id" );
      queryResult = QuerySetFactory.getInstance().executeQueryTable( "evt_inv_usage", queryArgs );

      expectedValue = 1;
      actualValue = queryResult.getRowCount();
      assertEquals( "Expected an entry in evt_inv_usage talbe associated to the inventory.",
            expectedValue, actualValue );
   }


   /**
    * This test is testing when a event under WP is record, the usage source will be set according
    * to WP usage source.
    *
    * <pre>
    * Given a WP usage source is "CUSTOMER"
    * When record a event under the WP
    * Then the event usage source is "CUSTOMER".
    * </pre>
    */
   @Test
   public void
         itCreatesUsageSnapshotSourceAsCustomerWhenWPUsageSourceIsCustomerDuringEventRecording() {
      BigDecimal engingCycles = BigDecimal.valueOf( 5 );
      BigDecimal engineHours = BigDecimal.valueOf( 15 );
      BigDecimal aircraftCycles = BigDecimal.valueOf( 5 );
      BigDecimal aircraftHours = BigDecimal.valueOf( 20 );
      BigDecimal aircraftWPCyclesValue = BigDecimal.valueOf( 5 );
      BigDecimal aircraftWPHoursValue = BigDecimal.TEN;
      InventoryKey engine = Domain.createEngine( eng -> {
         eng.addUsage( DataTypeKey.CYCLES, engingCycles );
         eng.addUsage( DataTypeKey.HOURS, engineHours );
      } );
      InventoryKey aircraft = Domain.createAircraft( af -> {
         af.addEngine( engine );
         af.addUsage( DataTypeKey.CYCLES, aircraftCycles );
         af.addUsage( DataTypeKey.HOURS, aircraftHours );
      } );
      UsageSnapshot aircraftWPCycles =
            new UsageSnapshot( aircraft, DataTypeKey.CYCLES, aircraftWPCyclesValue )
                  .withSnapshotSourceType( RefUsgSnapshotSrcTypeKey.CUSTOMER );
      UsageSnapshot aircraftWPHours =
            new UsageSnapshot( aircraft, DataTypeKey.HOURS, aircraftWPHoursValue )
                  .withSnapshotSourceType( RefUsgSnapshotSrcTypeKey.CUSTOMER );
      TaskKey workPackage = Domain.createWorkPackage( wp -> {
         wp.addUsageSnapshot( aircraftWPCycles );
         wp.addUsageSnapshot( aircraftWPHours );
         wp.setStatus( RefEventStatusKey.IN_WORK );
      } );
      HumanResourceKey authorizingHr = Domain.createHumanResource();
      EventKey taskInWp = new InventoryEventRecorder().recordInventoryEvent( engine,
            RefEventTypeKey.FG, RefEventStatusKey.ACTV, workPackage.getEventKey(),
            RefStageReasonKey.NEW, new Date(), authorizingHr, null, null, true );
      EventInventoryKey eventInventoryKey =
            EvtInvTable.findByEventAndInventory( taskInWp, engine ).getPk();
      RefUsgSnapshotSrcTypeKey usageSource = EvtInvUsageTable
            .findByPrimaryKey( new EventInventoryUsageKey( eventInventoryKey, DataTypeKey.CYCLES ) )
            .getUsageSnapshotSourceType();

      assertEquals( "usage source is not set properly.", usageSource,
            RefUsgSnapshotSrcTypeKey.CUSTOMER );
   }


   /**
    * This test is testing when a event under WP is record, the usage source will be set according
    * to WP usage source.
    *
    * <pre>
    * Given a WP usage source is "MAINTENIX"
    * When record a event under the WP
    * Then the event usage source is "MAINTENIX".
    * </pre>
    */
   @Test
   public void
         itCreatesUsageSnapshotSourceAsMaintenixWhenWPUsageSourceIsMaintenixDuringEventRecording() {
      BigDecimal engingCycles = BigDecimal.valueOf( 5 );
      BigDecimal engineHours = BigDecimal.valueOf( 15 );
      BigDecimal aircraftCycles = BigDecimal.valueOf( 5 );
      BigDecimal aircraftHours = BigDecimal.valueOf( 20 );
      BigDecimal aircraftWPCyclesValue = BigDecimal.valueOf( 5 );
      BigDecimal aircraftWPHoursValue = BigDecimal.TEN;
      InventoryKey engine = Domain.createEngine( eng -> {
         eng.addUsage( DataTypeKey.CYCLES, engingCycles );
         eng.addUsage( DataTypeKey.HOURS, engineHours );
      } );
      InventoryKey aircraft = Domain.createAircraft( af -> {
         af.addEngine( engine );
         af.addUsage( DataTypeKey.CYCLES, aircraftCycles );
         af.addUsage( DataTypeKey.HOURS, aircraftHours );
      } );
      UsageSnapshot aircraftWPCycles =
            new UsageSnapshot( aircraft, DataTypeKey.CYCLES, aircraftWPCyclesValue )
                  .withSnapshotSourceType( RefUsgSnapshotSrcTypeKey.MAINTENIX );
      UsageSnapshot aircraftWPHours =
            new UsageSnapshot( aircraft, DataTypeKey.HOURS, aircraftWPHoursValue )
                  .withSnapshotSourceType( RefUsgSnapshotSrcTypeKey.MAINTENIX );
      TaskKey workPackage = Domain.createWorkPackage( wp -> {
         wp.addUsageSnapshot( aircraftWPCycles );
         wp.addUsageSnapshot( aircraftWPHours );
         wp.setStatus( RefEventStatusKey.IN_WORK );
      } );
      HumanResourceKey authorizingHr = Domain.createHumanResource();
      EventKey taskInWp = new InventoryEventRecorder().recordInventoryEvent( engine,
            RefEventTypeKey.FG, RefEventStatusKey.ACTV, workPackage.getEventKey(),
            RefStageReasonKey.NEW, new Date(), authorizingHr, null, null, true );
      EventInventoryKey eventInventoryKey =
            EvtInvTable.findByEventAndInventory( taskInWp, engine ).getPk();
      RefUsgSnapshotSrcTypeKey usageSource = EvtInvUsageTable
            .findByPrimaryKey( new EventInventoryUsageKey( eventInventoryKey, DataTypeKey.CYCLES ) )
            .getUsageSnapshotSourceType();

      assertEquals( "usage source is not set properly.", usageSource,
            RefUsgSnapshotSrcTypeKey.MAINTENIX );
   }


   /**
    * This is a test case testing if the task is not assigned to work package, the usage source will
    * be "MAINTENIX".
    *
    * <pre>
    * Given a FG task.
    * When record the event.
    * Then verify the usage source is "MAINTENIX".
    * </pre>
    */
   @Test
   public void itCreatesUsageSnapshotSourceAsMaintenixWhenNoWP() {
      BigDecimal engingCycles = BigDecimal.valueOf( 5 );
      BigDecimal engineHours = BigDecimal.valueOf( 15 );
      InventoryKey engine = Domain.createEngine( eng -> {
         eng.addUsage( DataTypeKey.CYCLES, engingCycles );
         eng.addUsage( DataTypeKey.HOURS, engineHours );
      } );

      HumanResourceKey authorizingHr = Domain.createHumanResource();
      EventKey taskInWp = new InventoryEventRecorder().recordInventoryEvent( engine,
            RefEventTypeKey.FG, RefEventStatusKey.ACTV, null, RefStageReasonKey.NEW, new Date(),
            authorizingHr, null, null, true );
      EventInventoryKey eventInventoryKey =
            EvtInvTable.findByEventAndInventory( taskInWp, engine ).getPk();
      RefUsgSnapshotSrcTypeKey usageSource = EvtInvUsageTable
            .findByPrimaryKey( new EventInventoryUsageKey( eventInventoryKey, DataTypeKey.CYCLES ) )
            .getUsageSnapshotSourceType();

      assertEquals( "usage source is not set properly.", usageSource,
            RefUsgSnapshotSrcTypeKey.MAINTENIX );
   }
}
