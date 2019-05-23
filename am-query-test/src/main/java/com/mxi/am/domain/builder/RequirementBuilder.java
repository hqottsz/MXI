package com.mxi.am.domain.builder;

import static com.mxi.mx.core.key.RefRelationTypeKey.DEPT;
import static com.mxi.mx.core.key.RefRelationTypeKey.FAULTREL;
import static com.mxi.mx.core.key.RefRelationTypeKey.RECSRC;
import static org.apache.commons.lang.ObjectUtils.defaultIfNull;

import java.util.Map;
import java.util.Set;

import com.mxi.am.domain.Deadline;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.InstallationRecord;
import com.mxi.am.domain.Labour;
import com.mxi.am.domain.PartRequirement;
import com.mxi.am.domain.Requirement;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.services.dao.DataAccessObject;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.utils.StringUtils;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.IetmTopicKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefEventTypeKey;
import com.mxi.mx.core.key.RefImpactKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefUsgSnapshotSrcTypeKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.event.EventUtils;
import com.mxi.mx.core.services.event.inventory.UsageSnapshot;
import com.mxi.mx.core.table.evt.EvtAttachTable;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtEventRel;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.EvtIetm;
import com.mxi.mx.core.table.sched.SchedStaskDao;
import com.mxi.mx.core.table.sched.SchedStaskTable;


public class RequirementBuilder {

   static EvtEventDao sEvtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );
   static SchedStaskDao sSchedStaskDao = InjectorContainer.get().getInstance( SchedStaskDao.class );


   public static TaskKey build( Requirement aRequirement ) {

      TaskBuilder lTaskBuilder = new TaskBuilder();

      lTaskBuilder.withPrevTask( aRequirement.getPreviousTask() );
      lTaskBuilder.withParentTask( aRequirement.getParentTask() );
      lTaskBuilder.withIssueAccount( aRequirement.getIssueToAccount() );
      lTaskBuilder.withBarcode( aRequirement.getBarcode() );
      lTaskBuilder.withFault( aRequirement.getAssociatedFault() );
      lTaskBuilder.withDescription( aRequirement.getDescription() );

      if ( aRequirement.getTaskClass() == null ) {
         aRequirement.setTaskClass( RefTaskClassKey.REQ );
      }
      lTaskBuilder.withTaskClass( aRequirement.getTaskClass() );

      if ( aRequirement.getInventory() == null ) {
         aRequirement.setInventory( Domain.createAircraft() );
      }
      lTaskBuilder.onInventory( aRequirement.getInventory() );

      if ( aRequirement.getDefinition() == null ) {
         aRequirement.setDefinition( Domain.createRequirementDefinition() );
      }

      lTaskBuilder.withTaskRevision( aRequirement.getDefinition() );

      if ( aRequirement.getStatus() == null ) {
         aRequirement.setStatus( RefEventStatusKey.ACTV );
      }
      lTaskBuilder.withStatus( aRequirement.getStatus() );

      if ( RefEventStatusKey.COMPLETE.equals( aRequirement.getStatus() )
            || RefEventStatusKey.CANCEL.equals( aRequirement.getStatus() ) ) {
         lTaskBuilder.asHistoric();
      }

      lTaskBuilder.isOrphanedForecast( aRequirement.isOrphaned().orElse( false ) );

      TaskKey lReqKey = lTaskBuilder.build();

      EventKey lReqEventKey = lReqKey.getEventKey();

      // The deprecated TaskBuilder does not support the start/end dates, so add them here.
      if ( aRequirement.getActualStartDate() != null || aRequirement.getActualEndDate() != null
            || aRequirement.getScheduledStartDate() != null
            || aRequirement.getScheduledEndDate() != null ) {
         EvtEventTable lEvtEvent = sEvtEventDao.findByPrimaryKey( lReqEventKey );
         lEvtEvent.setEventType( RefEventTypeKey.TS );
         lEvtEvent.setActualStartDate( aRequirement.getActualStartDate() );
         lEvtEvent.setEventDate( aRequirement.getActualEndDate() );
         lEvtEvent.setSchedStartDate( aRequirement.getScheduledStartDate() );
         lEvtEvent.setSchedEndDt( aRequirement.getScheduledEndDate() );
         lEvtEvent.setSchedPriority( aRequirement.getSchedulingPriority() );
         sEvtEventDao.update( lEvtEvent );
      }

      if ( aRequirement.getDescription() == null ) {
         // Provided helper logic that sets the description using the code and name when the
         // description is not provided.
         if ( !StringUtils.isBlank( aRequirement.getName() )
               && !StringUtils.isBlank( aRequirement.getCode() ) ) {
            EvtEventTable lEvtEvent = sEvtEventDao.findByPrimaryKey( lReqEventKey );
            lEvtEvent.setEventSdesc( aRequirement.getCode() + " (" + aRequirement.getName() + ")" );
            sEvtEventDao.update( lEvtEvent );
         }
      }

      // Set any columns not supported by the deprecated TaskBuilder.
      // Note; the deprecated TaskBuilder hard codes the soft-deadline flag to false.
      SchedStaskTable lSchedStask = sSchedStaskDao.findByPrimaryKey( lReqKey );
      lSchedStask.setSoftDeadlineBool( aRequirement.isSoftDeadline().orElse( false ) );
      lSchedStask.setPlanByDate( aRequirement.getPlanByDate() );
      lSchedStask.setTaskSubclass( aRequirement.getTaskSubclass() );
      lSchedStask.setTaskPriority( aRequirement.getPriority() );
      sSchedStaskDao.update( lSchedStask );

      // Set the next task if provided.
      TaskKey lNextTask = aRequirement.getNextTask();
      if ( lNextTask != null ) {
         if ( !lReqKey.equals( getPrevTask( lNextTask ) ) ) {
            // Only create relationship if not already created (previously by the next task).
            EvtEventRel.create( lReqEventKey, lNextTask.getEventKey(), DEPT );
         }
      }

      // Set the recurrent resource if provided.
      FaultKey lRecurrentResource = aRequirement.getRecurrentSource();
      if ( lRecurrentResource != null ) {
         EvtEventRel.create( lReqEventKey, lRecurrentResource.getEventKey(), RECSRC );
      }

      if ( aRequirement.getRelatedFault() != null ) {
         EvtEventRel.create( lReqEventKey, aRequirement.getRelatedFault().getEventKey(), FAULTREL );
      }

      TaskKey lHighestTaskKey =
            ( TaskKey ) defaultIfNull( sSchedStaskDao.findByPrimaryKey( lReqKey ).getHTaskKey(),
                  lReqKey );

      for ( TaskKey lJobCard : aRequirement.getJobCards() ) {
         EvtEventTable lAssignedJobCardEvent =
               sEvtEventDao.findByPrimaryKey( lJobCard.getEventKey() );
         lAssignedJobCardEvent.setNhEvent( lReqEventKey );
         lAssignedJobCardEvent.setHEvent( lHighestTaskKey.getEventKey() );
         sEvtEventDao.update( lAssignedJobCardEvent );
      }

      for ( TaskKey lSubTask : aRequirement.getSubTasks() ) {
         EvtEventTable lAssignedSubTaskEvent =
               sEvtEventDao.findByPrimaryKey( lSubTask.getEventKey() );
         lAssignedSubTaskEvent.setNhEvent( lReqEventKey );
         sEvtEventDao.update( lAssignedSubTaskEvent );
      }

      addUsageSnapshots( lReqKey, aRequirement.getUsages() );

      for ( DomainConfiguration<Deadline> lDeadlineConfig : aRequirement.getDeadlines() ) {
         Deadline lDeadline = new Deadline();
         lDeadlineConfig.configure( lDeadline );
         lDeadline.setTask( lReqKey );
         DeadlineBuilder.build( lDeadline );
      }

      for ( Map.Entry<RefImpactKey, String> lImpact : aRequirement.getImpacts().entrySet() ) {
         DataSetArgument lArgs = new DataSetArgument();

         lArgs.add( lReqKey, new String[] { "sched_db_id", "sched_id" } );
         lArgs.add( lImpact.getKey(), new String[] { "impact_db_id", "impact_cd" } );
         lArgs.add( "impact_ldesc", lImpact.getValue() );

         MxDataAccess.getInstance().executeInsert( "sched_impact", lArgs );
      }

      for ( IetmTopicKey lTechRef : aRequirement.getTechnicalReferences() ) {
         EvtIetm.create( lReqEventKey, lTechRef );
      }

      for ( IetmTopicKey lAttached : aRequirement.getAttachments() ) {
         EvtAttachTable.create( lReqEventKey, lAttached );
      }

      for ( DomainConfiguration<InstallationRecord> lInstallConfig : aRequirement
            .getInstallConfigurations() ) {
         InstallationRecord lInstallationRecord = new InstallationRecord();
         lInstallConfig.configure( lInstallationRecord );

         // Set the installation record's task to this Requirement.
         if ( lInstallationRecord.getTask() != null ) {
            throw new RuntimeException(
                  "Installation Records configured for a Requirement cannot have their task attribute previously set." );
         }
         lInstallationRecord.setTask( lReqKey );

         InstallationRecordBuilder.build( lInstallationRecord );
      }

      for ( DomainConfiguration<PartRequirement> lPartRequirementConfiguration : aRequirement
            .getPartRequirementConfigurations() ) {
         PartRequirement lPartRequirement = new PartRequirement();
         lPartRequirementConfiguration.configure( lPartRequirement );
         if ( lPartRequirement.getTaskKey() != null ) {
            throw new RuntimeException(
                  "Part Requirement cannot have their task key previously set" );
         }
         lPartRequirement.setTaskKey( lReqKey );
         PartRequirementBuilder.build( lPartRequirement );
      }

      // Add labour
      for ( DomainConfiguration<Labour> lLabourConfig : aRequirement.getLabourConfigurations() ) {
         Labour lLabour = new Labour();
         lLabourConfig.configure( lLabour );
         if ( lLabour.getTask() != null ) {
            throw new RuntimeException( "Labour cannot have their task key previously set" );
         }
         lLabour.setTask( lReqKey );
         LabourBuilder.build( lLabour );
      }

      return lReqKey;
   }


   private static void addUsageSnapshots( TaskKey aReqKey, Set<UsageSnapshot> aUsages ) {

      EventKey lReqEvent = aReqKey.getEventKey();

      DataAccessObject lDao = MxDataAccess.getInstance();
      DataSetArgument lArg;

      for ( UsageSnapshot lUsage : aUsages ) {
         lArg = new DataSetArgument();

         lArg.add( lReqEvent, "aEventDbId", "aEventId" );
         lArg.add( "aEventInvId", EventUtils.getEventInvId( lReqEvent, lUsage.getInventory() ) );
         lArg.add( lUsage.getDataType(), "aDataTypeDbId", "aDataTypeId" );

         lArg.add( "aTsnQt", lUsage.getTSN() );
         lArg.add( "aTsoQt", lUsage.getTSO() );
         lArg.add( "aTsiQt", lUsage.getTSI() );
         lArg.add( "aAssmblTsnQt", lUsage.getAssemblyTSN() );
         lArg.add( "aAssmblTsoQt", lUsage.getAssemblyTSO() );
         lArg.add( "aHighestTsnQt", lUsage.getHighestTSN() );
         lArg.add( "aHighestTsoQt", lUsage.getHighestTSO() );
         lArg.add( "aNextHighestTsnQt", lUsage.getNextHighestTSN() );
         lArg.add( "aNextHighestTsoQt", lUsage.getNextHighestTSO() );
         lArg.add( ( RefUsgSnapshotSrcTypeKey ) defaultIfNull( lUsage.getUsageSnapshotSourceType(),
               RefUsgSnapshotSrcTypeKey.MAINTENIX ), "aUsageSourceDbId", "aUsageSourceCd" );

         if ( lDao.executeUpdate( "com.mxi.mx.core.query.table.evt.addUsageSnapshot",
               lArg ) != 1 ) {
            throw new RuntimeException( "Unable to create usage snapshot: " + lArg );
         }
      }

   }


   private static TaskKey getPrevTask( TaskKey aTask ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTask, "rel_event_db_id", "rel_event_id" );
      lArgs.add( DEPT, "rel_type_db_id", "rel_type_cd" );
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "evt_event_rel", lArgs );
      if ( lQs.next() ) {
         return lQs.getKey( TaskKey.class, "event_db_id", "event_id" );
      } else {
         return null;
      }
   }
}
