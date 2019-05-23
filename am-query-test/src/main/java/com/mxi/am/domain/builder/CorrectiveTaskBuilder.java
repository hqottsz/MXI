package com.mxi.am.domain.builder;

import java.math.BigDecimal;
import java.util.Map;

import com.mxi.am.domain.CorrectiveTask;
import com.mxi.am.domain.Deadline;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Labour;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventInventoryKey;
import com.mxi.mx.core.key.EventInventoryUsageKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefUsgSnapshotSrcTypeKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.stask.step.MxStepService;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.sched.SchedActionTable;


public class CorrectiveTaskBuilder {

   static EvtEventDao sEvtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );


   public static TaskKey build( CorrectiveTask aCorrectiveTask ) {

      InventoryKey lInventory = aCorrectiveTask.getInventory();

      TaskBuilder lTaskBuilder = new TaskBuilder();
      lTaskBuilder.withTaskClass( RefTaskClassKey.CORR );
      lTaskBuilder.withBarcode( aCorrectiveTask.getBarcode() );
      lTaskBuilder.withDescription( aCorrectiveTask.getName() );

      if ( lInventory != null ) {
         lTaskBuilder.onInventory( lInventory );
      }

      if ( aCorrectiveTask.getStatus() != null ) {
         lTaskBuilder.withStatus( aCorrectiveTask.getStatus() );
      }

      if ( aCorrectiveTask.getFaultKey() != null ) {
         lTaskBuilder.withFault( aCorrectiveTask.getFaultKey() );
      }

      if ( aCorrectiveTask.getHistoricalBool() != null ) {
         if ( aCorrectiveTask.getHistoricalBool() ) {
            lTaskBuilder.asHistoric();
         }

      } else {
         if ( RefEventStatusKey.COMPLETE.equals( aCorrectiveTask.getStatus() )
               || RefEventStatusKey.CANCEL.equals( aCorrectiveTask.getStatus() ) ) {
            lTaskBuilder.asHistoric();
         }
      }

      TaskKey lCorrTask = lTaskBuilder.build();

      // Add steps
      MxStepService stepService = InjectorContainer.get().getInstance( MxStepService.class );

      aCorrectiveTask.getSteps().forEach( step -> {
         try {
            stepService.addStep( lCorrTask, step );
         } catch ( MxException e ) {
            throw new IllegalStateException( String.format(
                  "Data setup failed.  Failed to add step to corrective task: %s", lCorrTask ) );
         }
      } );

      EventKey lCorrTaskEvent = lCorrTask.getEventKey();

      // The TaskBuilder does not support actual start/end dates, so set them here.
      EvtEventTable lEvtEvent = sEvtEventDao.findByPrimaryKey( lCorrTaskEvent );
      lEvtEvent.setActualStartDate( aCorrectiveTask.getStartDate() );
      lEvtEvent.setEventDate( aCorrectiveTask.getEndDate() );
      sEvtEventDao.update( lEvtEvent );

      // If there is usage-at-completion then ensure an inventory was provided.
      Map<DataTypeKey, BigDecimal> lUsagesAtCompletion = aCorrectiveTask.getUsagesAtCompletion();
      if ( !lUsagesAtCompletion.isEmpty() && lInventory == null ) {
         throw new RuntimeException(
               "Inventory must be provided when usage-at-completion is provided." );
      }

      for ( TaskKey lSubTask : aCorrectiveTask.getSubtasks() ) {
         EvtEventTable lSubTaskEvent = sEvtEventDao.findByPrimaryKey( lSubTask.getEventKey() );
         lSubTaskEvent.setNhEvent( lCorrTaskEvent );
         sEvtEventDao.update( lSubTaskEvent );
      }

      // Set the usage-at-completion.
      for ( DataTypeKey lDataType : lUsagesAtCompletion.keySet() ) {
         storeUsageAtCompletion( lCorrTask, lDataType, lUsagesAtCompletion.get( lDataType ) );
      }

      for ( DomainConfiguration<Deadline> lDeadlineConfig : aCorrectiveTask.getDeadlines() ) {
         Deadline lDeadline = new Deadline();
         lDeadlineConfig.configure( lDeadline );
         lDeadline.setTask( lCorrTask );
         DeadlineBuilder.build( lDeadline );
      }

      // Add labour
      for ( DomainConfiguration<Labour> lLabourConfig : aCorrectiveTask
            .getLabourConfigurations() ) {
         Labour lLabour = new Labour();
         lLabourConfig.configure( lLabour );
         if ( lLabour.getTask() != null ) {
            throw new RuntimeException( "Labour cannot have their task key previously set" );
         }
         lLabour.setTask( lCorrTask );
         LabourBuilder.build( lLabour );
      }

      SchedActionTable lSchedActionTable = SchedActionTable.create( lCorrTask );
      lSchedActionTable.setActionLdesc( aCorrectiveTask.getActionDescription() );
      lSchedActionTable.insert();

      return lCorrTask;
   }


   private static void storeUsageAtCompletion( TaskKey aTask, DataTypeKey aDataType,
         BigDecimal aBigDecimal ) {

      EventInventoryUsageKey lKey = new EventInventoryUsageKey(
            new EventInventoryKey( aTask.getEventKey(), 1 ), aDataType );

      DataSetArgument lArgs = lKey.getPKWhereArg();
      lArgs.add( "tsn_qt", aBigDecimal );
      lArgs.add( RefUsgSnapshotSrcTypeKey.MAINTENIX,
            RefUsgSnapshotSrcTypeKey.Column.SOURCE_DB_ID.toString(),
            RefUsgSnapshotSrcTypeKey.Column.SOURCE_CD.toString() );

      MxDataAccess.getInstance().executeInsert( "evt_inv_usage", lArgs );
   }

}
