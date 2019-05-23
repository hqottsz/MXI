package com.mxi.am.domain.builder;

import static com.mxi.mx.core.key.RefEventStatusKey.ACTV;
import static com.mxi.mx.core.key.RefEventStatusKey.CANCEL;
import static com.mxi.mx.core.key.RefEventStatusKey.COMPLETE;
import static com.mxi.mx.core.key.RefEventTypeKey.TS;
import static com.mxi.mx.core.key.RefTaskClassKey.CHECK;
import static org.apache.commons.lang.ObjectUtils.defaultIfNull;

import java.util.Arrays;
import java.util.List;

import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.SignOffRequirement;
import com.mxi.am.domain.WorkPackage;
import com.mxi.mx.core.key.DepartmentKey;
import com.mxi.mx.core.key.EventInventoryKey;
import com.mxi.mx.core.key.EventInventoryUsageKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.SchedWPKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.WoLineKey;
import com.mxi.mx.core.services.event.inventory.UsageSnapshot;
import com.mxi.mx.core.table.evt.EvtDept;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.EvtInvTable;
import com.mxi.mx.core.table.evt.EvtInvUsage;
import com.mxi.mx.core.table.evt.EvtLocTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.table.sched.SchedWOLineTable;
import com.mxi.mx.core.table.sched.SchedWorkTypeTable;
import com.mxi.mx.core.table.task.SchedWPTable;
import com.mxi.mx.persistence.uuid.SequentialUuidGenerator;


public class WorkPackageBuilder {

   public static TaskKey build( WorkPackage aWorkPackage ) {

      InventoryKey lAircraftKey = aWorkPackage.getAircraft();
      if ( lAircraftKey == null ) {
         lAircraftKey = Domain.createAircraft();
      }
      InvInvTable lAircraft = InvInvTable.findByPrimaryKey( lAircraftKey );

      RefEventStatusKey lStatus =
            ( RefEventStatusKey ) defaultIfNull( aWorkPackage.getStatus(), ACTV );
      Boolean lRequestParts =
            ( Boolean ) defaultIfNull( aWorkPackage.isRequestParts(), Boolean.FALSE );

      EventKey lEventKey = EvtEventTable.generatePrimaryKey();
      TaskKey lWorkpackageKey = new TaskKey( lEventKey.getDbId(), lEventKey.getId() );

      // Insert the sched_stask row prior to the evt_event row in order for
      // TIUAR_EVT_EVENT_HIST_BOOL to be triggered.
      SchedStaskTable lStaskTable = SchedStaskTable.create( lWorkpackageKey );
      lStaskTable.setTaskClass( CHECK );
      lStaskTable.setAlternateKey( new SequentialUuidGenerator().newUuid() );
      lStaskTable.setMainInventory( lAircraftKey );
      lStaskTable.setRequestPartsBool( lRequestParts );
      lStaskTable.setBarcode( aWorkPackage.getBarcode() );
      lStaskTable.setWoCommitLineOrd( lStatus.equals( RefEventStatusKey.COMMIT ) ? 1 : null );
      lStaskTable.setAutoCompleteBool( aWorkPackage.isAutoComplete() );
      lStaskTable.setWoRefSdesc( aWorkPackage.getWorkOrderDescription() );
      lStaskTable.insert();

      EvtEventTable lEventTable = EvtEventTable.create( lEventKey );
      lEventTable.setHEvent( lEventKey );
      lEventTable.setEventType( TS );
      lEventTable.setEventSdesc( aWorkPackage.getName() );
      lEventTable.setEventStatus( lStatus );
      lEventTable.setHistBool( isWorkPackageHistorical( lStatus ) );
      lEventTable.setActualStartDate( aWorkPackage.getActualStartDate() );
      lEventTable.setEventDate( aWorkPackage.getActualEndDate() );
      lEventTable.setSchedStartDate( aWorkPackage.getScheduledStartDate() );
      lEventTable.setSchedEndDt( aWorkPackage.getScheduledEndDate() );
      lEventTable.insert();

      EvtInvTable lEvtInvTable = EvtInvTable.create( lEventKey );
      lEvtInvTable.setInventoryKey( lAircraftKey );
      lEvtInvTable.setMainInvBool( true );
      lEvtInvTable.setHInventoryKey( lAircraft.getHInvNo() );
      lEvtInvTable.setAssmblInventoryKey( lAircraft.getAssmblInvNo() );
      lEvtInvTable.setNHInventoryKey( lAircraft.getNhInvNo() );
      lEvtInvTable.insert();

      // When creating a work package a row magically gets inserted into sched_wp.
      SchedWPTable lSchedWPTable =
            SchedWPTable.findByPrimaryKey( new SchedWPKey( lWorkpackageKey ) );
      lSchedWPTable.setReleaseNumber( aWorkPackage.getReleaseNumber() );
      lSchedWPTable.setReleaseRemarks( aWorkPackage.getReleaseRemarks() );
      lSchedWPTable.update();

      for ( InventoryKey lSubAssyKey : aWorkPackage.getSubAssemblies() ) {
         InvInvTable lSubAssy = InvInvTable.findByPrimaryKey( lSubAssyKey );
         lEvtInvTable = EvtInvTable.create( lEventKey );
         lEvtInvTable.setInventoryKey( lSubAssyKey );
         lEvtInvTable.setMainInvBool( false );
         lEvtInvTable.setHInventoryKey( lSubAssy.getHInvNo() );
         lEvtInvTable.setAssmblInventoryKey( lSubAssy.getAssmblInvNo() );
         lEvtInvTable.setNHInventoryKey( lSubAssy.getNhInvNo() );
         lEvtInvTable.insert();
      }

      LocationKey lLocation = aWorkPackage.getLocation();
      if ( lLocation != null ) {
         EvtLocTable lEvtLoc = EvtLocTable.create( lEventKey );
         lEvtLoc.setLocation( lLocation );
         lEvtLoc.insert();
      }

      for ( TaskKey lAssignedTask : aWorkPackage.getTasks() ) {
         EvtEventTable lAssignedTaskEvent =
               EvtEventTable.findByPrimaryKey( lAssignedTask.getEventKey() );
         lAssignedTaskEvent.setHEvent( lWorkpackageKey.getEventKey() );
         lAssignedTaskEvent.setNhEvent( lWorkpackageKey.getEventKey() );
         lAssignedTaskEvent.update();
         SchedStaskTable lTaskTable = SchedStaskTable.findByPrimaryKey( lAssignedTask );
         if ( COMPLETE.equals( lAssignedTaskEvent.getEventStatus() ) ) {
            lTaskTable.setWoTaskCompletedOn( lWorkpackageKey );
            lTaskTable.update();
         }
         AssignmentService.updateHighestEventOfSubEvents( lAssignedTask.getEventKey(),
               lWorkpackageKey.getEventKey() );
      }

      List<TaskKey> lCollectedTasks = aWorkPackage.getCollectedTasks();

      int lWorkOrderLineNo = 1;
      for ( TaskKey aWorkScopeTask : aWorkPackage.getWorkScopeTasks() ) {
         WoLineKey aWoLineKey = new WoLineKey( lWorkpackageKey.getDbId(), lWorkpackageKey.getId(),
               lWorkOrderLineNo );
         SchedWOLineTable lWorkOrderLineTable = SchedWOLineTable.create( aWoLineKey );
         lWorkOrderLineTable.setSchedKey( aWorkScopeTask );
         lWorkOrderLineTable.setCollectedBool( lCollectedTasks.contains( aWorkScopeTask ) );
         lWorkOrderLineTable.insert();
         lWorkOrderLineNo++;
      }

      // Assign work package to crew
      for ( DepartmentKey lCrew : aWorkPackage.getCrews() ) {
         EvtDept.create( lEventKey, lCrew );
      }

      buildUsageSnapshot( aWorkPackage, lEventKey );

      aWorkPackage.getWorkTypes().forEach( lWorkType -> {
         SchedWorkTypeTable.create( lWorkpackageKey, lWorkType ).insert();
      } );

      for ( DomainConfiguration<SignOffRequirement> lSignOffRequirementConfig : aWorkPackage
            .getSignOffRequirementConfigs() ) {
         SignOffRequirement lSignOffRequirement = new SignOffRequirement();
         lSignOffRequirementConfig.configure( lSignOffRequirement );
         lSignOffRequirement.setWorkpackage( lWorkpackageKey );
         SignOffRequirementBuilder.build( lSignOffRequirement );
      }

      return lWorkpackageKey;
   }


   private static void buildUsageSnapshot( WorkPackage aWorkPackage,
         EventKey aWorkPackageEventKey ) {

      for ( UsageSnapshot lUsageSnapshot : aWorkPackage.getUsageSnapshots() ) {

         EventInventoryKey lEvtInvKey;
         EvtInvTable lEvtInv = EvtInvTable.findByEventAndInventory( aWorkPackageEventKey,
               lUsageSnapshot.getInventory() );
         if ( lEvtInv == null ) {
            lEvtInv = EvtInvTable.create( aWorkPackageEventKey );
            lEvtInv.setInventoryKey( lUsageSnapshot.getInventory() );
            lEvtInv.setMainInvBool( false );
            lEvtInv.setHInventoryKey( aWorkPackage.getAircraft() );
            lEvtInv.setAssmblInventoryKey( aWorkPackage.getAircraft() );
            lEvtInv.setNHInventoryKey( aWorkPackage.getAircraft() );
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


   private static final boolean isWorkPackageHistorical( RefEventStatusKey aStatus ) {
      return Arrays.asList( COMPLETE, CANCEL ).contains( aStatus );

   }

}
