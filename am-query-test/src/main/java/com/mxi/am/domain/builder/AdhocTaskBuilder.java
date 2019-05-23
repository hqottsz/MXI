package com.mxi.am.domain.builder;

import static com.mxi.mx.core.key.RefEventTypeKey.TS;
import static com.mxi.mx.core.key.RefRelationTypeKey.DEPT;
import static com.mxi.mx.core.key.RefRelationTypeKey.FAULTREL;
import static com.mxi.mx.core.key.RefTaskClassKey.ADHOC;

import com.mxi.am.domain.AdhocTask;
import com.mxi.am.domain.Deadline;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Labour;
import com.mxi.am.domain.PartRequirement;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.stask.step.MxStepService;
import com.mxi.mx.core.table.evt.EvtDept;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtEventRel;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.EvtInvTable;
import com.mxi.mx.core.table.evt.EvtLocTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.org.OrgCrewShiftTask;
import com.mxi.mx.core.table.sched.SchedStaskDao;
import com.mxi.mx.core.table.sched.SchedStaskTable;


public class AdhocTaskBuilder {

   static EvtEventDao evtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );
   static SchedStaskDao schedStaskDao = InjectorContainer.get().getInstance( SchedStaskDao.class );


   public static TaskKey build( AdhocTask adhocTask ) {

      // Add row to evt_event.
      EventKey event = evtEventDao.generatePrimaryKey();
      EvtEventTable evtEvent = evtEventDao.create( event );
      evtEvent.setEventType( TS );
      evtEvent.setStatus( adhocTask.getStatus() );
      evtEvent.setHEvent( event );
      evtEventDao.insert( evtEvent );

      // Add row to evt_inv when inventory provided.
      InventoryKey inventory = adhocTask.getInventory();
      if ( inventory != null ) {
         InvInvTable invInv = InvInvTable.findByPrimaryKey( inventory );

         EvtInvTable evtInv = EvtInvTable.create( event );
         evtInv.setInventoryKey( inventory );
         evtInv.setMainInvBool( true );
         evtInv.setHInventoryKey( invInv.getHInvNo() );
         evtInv.setAssmblInventoryKey( invInv.getAssmblInvNo() );
         evtInv.setNHInventoryKey( invInv.getNhInvNo() );
         evtInv.setAssemblyKey( invInv.getOrigAssmbl() );
         evtInv.insert();
      }

      // Add row to sched_stask.
      TaskKey adhocTaskKey = new TaskKey( event );
      SchedStaskTable schedStask = schedStaskDao.create( adhocTaskKey );
      schedStask.setTaskClass( ADHOC );
      schedStask.setMainInventory( inventory );
      schedStask.setSoftDeadlineBool( adhocTask.isSoftDeadline().orElse( false ) );
      schedStaskDao.insert( schedStask );

      // Add deadline
      for ( DomainConfiguration<Deadline> deadlineConfig : adhocTask.getDeadlines() ) {
         Deadline deadline = new Deadline();
         deadlineConfig.configure( deadline );
         deadline.setTask( adhocTaskKey );
         DeadlineBuilder.build( deadline );
      }

      // Add row to evt_event_rel for a DEPT relationship to the previous task.
      if ( adhocTask.getPreviousTask() != null ) {
         EvtEventRel.create( adhocTask.getPreviousTask().getEventKey(), event, DEPT );
      }

      // Add row to evt_event_rel for a FAULTREL relationship to the related fault.
      if ( adhocTask.getRelatedFault() != null ) {
         EvtEventRel.create( event, adhocTask.getRelatedFault().getEventKey(), FAULTREL );
      }

      for ( DomainConfiguration<PartRequirement> partRequirementConfiguration : adhocTask
            .getPartRequirementConfigurations() ) {
         PartRequirement partRequirement = new PartRequirement();
         partRequirementConfiguration.configure( partRequirement );
         if ( partRequirement.getTaskKey() != null ) {
            throw new RuntimeException(
                  "Part Requirement cannot have their task key previously set" );
         }
         partRequirement.setTaskKey( adhocTaskKey );
         PartRequirementBuilder.build( partRequirement );
      }

      // Add steps
      MxStepService stepService = InjectorContainer.get().getInstance( MxStepService.class );
      adhocTask.getSteps().forEach( step -> {
         try {
            stepService.addStep( adhocTaskKey, step );
         } catch ( MxException e ) {
            e.printStackTrace();
         }
      } );

      // Add labour
      for ( DomainConfiguration<Labour> labourConfig : adhocTask.getLabourConfigurations() ) {
         Labour labour = new Labour();
         labourConfig.configure( labour );
         if ( labour.getTask() != null ) {
            throw new RuntimeException( "Labour cannot have their task key previously set" );
         }
         labour.setTask( adhocTaskKey );
         LabourBuilder.build( labour );
      }

      // Add crew shift day
      adhocTask.getCrewShiftDay().ifPresent( crewShiftDay -> {
         OrgCrewShiftTask.create( adhocTaskKey, crewShiftDay ).insert();
         EvtDept.create( adhocTaskKey.getEventKey(), crewShiftDay.getCrewKey() );
      } );

      // Assign crew
      adhocTask.getCrew().ifPresent( crew -> {
         EvtDept.create( event, crew );
      } );

      // Set to historic, if applicable - do this at the end so previous settings do not fail
      if ( adhocTask.isCompleted() != null ) {
         evtEvent.setHistBool( adhocTask.isCompleted() );
      }

      evtEventDao.update( evtEvent );

      // add task location
      adhocTask.getLocation().ifPresent( location -> {
         EvtLocTable evtLocTable = EvtLocTable.create( event );
         evtLocTable.setLocation( location );
         evtLocTable.insert();
      } );
      return adhocTaskKey;
   }

}
