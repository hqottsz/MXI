package com.mxi.am.domain.builder;

import static com.mxi.mx.core.key.RefEventTypeKey.TS;
import static com.mxi.mx.core.key.RefRelationTypeKey.DEPT;
import static com.mxi.mx.core.key.RefTaskClassKey.ADHOC;

import java.math.BigDecimal;
import java.util.Date;

import com.mxi.am.domain.Deadline;
import com.mxi.am.domain.RepetitiveTask;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefRelationTypeKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtEventRel;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.EvtInvTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.sched.SchedStaskDao;
import com.mxi.mx.core.table.sched.SchedStaskTable;


public class RepetitiveTaskBuilder {

   private static EvtEventDao sEvtEventDao =
         InjectorContainer.get().getInstance( EvtEventDao.class );
   private static SchedStaskDao sSchedStaskDao =
         InjectorContainer.get().getInstance( SchedStaskDao.class );

   private final static BigDecimal DEFAULT_REPEAT_INTERVAL = BigDecimal.TEN;


   public static TaskKey build( RepetitiveTask aRepetitiveTask ) {

      // Add row to evt_event.
      EventKey lEvent = sEvtEventDao.generatePrimaryKey();
      EvtEventTable lEvtEvent = sEvtEventDao.create( lEvent );
      lEvtEvent.setEventType( TS );
      lEvtEvent.setStatus( aRepetitiveTask.getStatus() );
      lEvtEvent.setHEvent( lEvent );
      sEvtEventDao.insert( lEvtEvent );

      // Add row to evt_inv when inventory provided.
      InventoryKey lInventory = aRepetitiveTask.getInventory();
      if ( lInventory != null ) {
         InvInvTable lInv = InvInvTable.findByPrimaryKey( lInventory );

         EvtInvTable lEvtInv = EvtInvTable.create( lEvent );
         lEvtInv.setInventoryKey( lInventory );
         lEvtInv.setMainInvBool( true );
         lEvtInv.setHInventoryKey( lInv.getHInvNo() );
         lEvtInv.setAssmblInventoryKey( lInv.getAssmblInvNo() );
         lEvtInv.setNHInventoryKey( lInv.getNhInvNo() );
         lEvtInv.setAssemblyKey( lInv.getOrigAssmbl() );
         lEvtInv.insert();
      }

      // Add row to sched_stask.
      TaskKey lRepetitiveTask = new TaskKey( lEvent );
      SchedStaskTable lSchedStask = sSchedStaskDao.create( lRepetitiveTask );
      lSchedStask.setTaskClass( ADHOC );
      lSchedStask.setMainInventory( lInventory );
      sSchedStaskDao.insert( lSchedStask );

      // Add row to evt_event_rel for a DEPT relationship to the previous task.
      if ( aRepetitiveTask.getPreviousTask() != null ) {
         EvtEventRel.create( aRepetitiveTask.getPreviousTask().getEventKey(), lEvent, DEPT );
      }

      // Add row to evt_event_rel for a FAULTREL relationship to the related fault.
      if ( aRepetitiveTask.getRelatedFault() != null ) {
         RefRelationTypeKey relationType = RefRelationTypeKey.FAULTREL;

         if ( aRepetitiveTask.getFaultRelationType() != null ) {
            relationType = aRepetitiveTask.getFaultRelationType();
         }

         EvtEventRel.create( lEvent, aRepetitiveTask.getRelatedFault().getEventKey(),
               relationType );
      }

      // Create a Calendar Day Deadline with a Repeat Interval
      Deadline lDeadline = new Deadline();
      lDeadline.setUsageType( DataTypeKey.CDY );
      lDeadline
            .setInterval( aRepetitiveTask.getRepeatInterval().orElse( DEFAULT_REPEAT_INTERVAL ) );
      lDeadline.setStartDate( aRepetitiveTask.getStartDate().orElse( new Date() ) );
      lDeadline.setDueDate( DateUtils.addDays( lDeadline.getStartDate().get(),
            lDeadline.getInterval().intValue() ) );
      lDeadline.setTask( lRepetitiveTask );
      DeadlineBuilder.build( lDeadline );

      return lRepetitiveTask;
   }

}
