package com.mxi.am.domain.builder;

import static com.mxi.mx.core.key.RefEventStatusKey.ACTV;
import static com.mxi.mx.core.key.RefEventStatusKey.CANCEL;
import static com.mxi.mx.core.key.RefEventStatusKey.COMPLETE;
import static com.mxi.mx.core.key.RefEventTypeKey.TS;
import static com.mxi.mx.core.key.RefTaskClassKey.RO;
import static org.apache.commons.lang.ObjectUtils.defaultIfNull;

import java.util.Arrays;

import com.mxi.am.domain.ComponentWorkPackage;
import com.mxi.am.domain.Domain;
import com.mxi.mx.core.key.EventInventoryKey;
import com.mxi.mx.core.key.EventInventoryUsageKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.event.inventory.UsageSnapshot;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.EvtInvTable;
import com.mxi.mx.core.table.evt.EvtInvUsage;
import com.mxi.mx.core.table.evt.EvtLocTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.sched.SchedStaskTable;


public class ComponentWorkPackageBuilder {

   public static TaskKey build( ComponentWorkPackage aComponentWorkPackage ) {

      InventoryKey lComponentKey = aComponentWorkPackage.getInventory();
      if ( lComponentKey == null ) {
         lComponentKey = Domain.createTrackedInventory();
      }
      InvInvTable lComponent = InvInvTable.findByPrimaryKey( lComponentKey );

      RefEventStatusKey lStatus =
            ( RefEventStatusKey ) defaultIfNull( aComponentWorkPackage.getStatus(), ACTV );
      Boolean lRequestParts =
            ( Boolean ) defaultIfNull( aComponentWorkPackage.isRequestParts(), Boolean.FALSE );

      EventKey lEventKey = EvtEventTable.generatePrimaryKey();
      TaskKey lComponentWorkpackageKey = new TaskKey( lEventKey.getDbId(), lEventKey.getId() );

      // Insert the sched_stask row prior to the evt_event row in order for
      // TIUAR_EVT_EVENT_HIST_BOOL to be triggered.
      SchedStaskTable lStaskTable = SchedStaskTable.create( lComponentWorkpackageKey );
      lStaskTable.setTaskClass( RO );
      lStaskTable.setMainInventory( lComponentKey );
      lStaskTable.setRequestPartsBool( lRequestParts );
      lStaskTable.setBarcode( aComponentWorkPackage.getBarcode() );
      lStaskTable.insert();

      EvtEventTable lEventTable = EvtEventTable.create( lEventKey );
      lEventTable.setHEvent( lEventKey );
      lEventTable.setEventType( TS );
      lEventTable.setEventSdesc( aComponentWorkPackage.getName() );
      lEventTable.setEventStatus( lStatus );
      lEventTable.setHistBool( isComponentWorkPackageHistorical( lStatus ) );
      lEventTable.setActualStartDate( aComponentWorkPackage.getActualStartDate() );
      lEventTable.setEventDate( aComponentWorkPackage.getActualEndDate() );
      lEventTable.setSchedStartDate( aComponentWorkPackage.getScheduledStartDate() );
      lEventTable.setSchedEndDt( aComponentWorkPackage.getScheduledEndDate() );
      lEventTable.insert();

      EvtInvTable lEvtInvTable = EvtInvTable.create( lEventKey );
      lEvtInvTable.setInventoryKey( lComponentKey );
      lEvtInvTable.setMainInvBool( true );
      lEvtInvTable.setHInventoryKey( lComponent.getHInvNo() );
      lEvtInvTable.setAssmblInventoryKey( lComponent.getAssmblInvNo() );
      lEvtInvTable.setNHInventoryKey( lComponent.getNhInvNo() );
      lEvtInvTable.insert();

      for ( InventoryKey lSubAssyKey : aComponentWorkPackage.getSubAssemblies() ) {
         InvInvTable lSubAssy = InvInvTable.findByPrimaryKey( lSubAssyKey );
         lEvtInvTable = EvtInvTable.create( lEventKey );
         lEvtInvTable.setInventoryKey( lSubAssyKey );
         lEvtInvTable.setMainInvBool( false );
         lEvtInvTable.setHInventoryKey( lSubAssy.getHInvNo() );
         lEvtInvTable.setAssmblInventoryKey( lSubAssy.getAssmblInvNo() );
         lEvtInvTable.setNHInventoryKey( lSubAssy.getNhInvNo() );
         lEvtInvTable.insert();
      }

      LocationKey lLocation = aComponentWorkPackage.getLocation();
      if ( lLocation != null ) {
         EvtLocTable lEvtLoc = EvtLocTable.create( lEventKey );
         lEvtLoc.setLocation( lLocation );
         lEvtLoc.insert();
      }

      for ( TaskKey lAssignedTask : aComponentWorkPackage.getAssignedTasks() ) {
         EvtEventTable lAssingedTaskEvent =
               EvtEventTable.findByPrimaryKey( lAssignedTask.getEventKey() );
         lAssingedTaskEvent.setHEvent( lComponentWorkpackageKey.getEventKey() );
         lAssingedTaskEvent.setNhEvent( lComponentWorkpackageKey.getEventKey() );
         lAssingedTaskEvent.update();
         SchedStaskTable lTaskTable = SchedStaskTable.findByPrimaryKey( lAssignedTask );
         if ( COMPLETE.equals( lAssingedTaskEvent.getEventStatus() ) ) {
            lTaskTable.setWoTaskCompletedOn( lComponentWorkpackageKey );
            lTaskTable.update();
         }
         AssignmentService.updateHighestEventOfSubEvents( lAssignedTask.getEventKey(),
               lComponentWorkpackageKey.getEventKey() );
      }

      buildUsageSnapshot( aComponentWorkPackage, lEventKey );

      return lComponentWorkpackageKey;
   }


   private static void buildUsageSnapshot( ComponentWorkPackage aWorkPackage,
         EventKey aWorkPackageEventKey ) {

      for ( UsageSnapshot lUsageSnapshot : aWorkPackage.getUsageSnapshots() ) {

         EventInventoryKey lEvtInvKey;
         EvtInvTable lEvtInv = EvtInvTable.findByEventAndInventory( aWorkPackageEventKey,
               lUsageSnapshot.getInventory() );
         if ( lEvtInv == null ) {
            lEvtInv = EvtInvTable.create( aWorkPackageEventKey );
            lEvtInv.setInventoryKey( lUsageSnapshot.getInventory() );
            lEvtInv.setMainInvBool( false );
            lEvtInv.setHInventoryKey( aWorkPackage.getInventory() );
            lEvtInv.setAssmblInventoryKey( aWorkPackage.getInventory() );
            lEvtInv.setNHInventoryKey( aWorkPackage.getInventory() );
            lEvtInvKey = lEvtInv.insert();
         } else {
            lEvtInvKey = lEvtInv.getPk();
         }

         EvtInvUsage lEvtInvUsage = EvtInvUsage.create(
               new EventInventoryUsageKey( lEvtInvKey, lUsageSnapshot.getDataType() ),
               lUsageSnapshot.getTSN(), lUsageSnapshot.getTSO(), lUsageSnapshot.getTSI(),
               lUsageSnapshot.getUsageSnapshotSourceType() );
         lEvtInvUsage.setAssmblTsnQt( lUsageSnapshot.getAssemblyTSN() );
         lEvtInvUsage.setAssmblTsoQt( lUsageSnapshot.getAssemblyTSO() );
      }

   }


   private static final boolean isComponentWorkPackageHistorical( RefEventStatusKey aStatus ) {
      return Arrays.asList( COMPLETE, CANCEL ).contains( aStatus );

   }
}
