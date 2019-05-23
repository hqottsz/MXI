package com.mxi.am.domain.builder;

import static com.mxi.mx.core.key.RefRelationTypeKey.FAULTREL;
import static com.mxi.mx.core.key.RefRelationTypeKey.RECSRC;

import java.util.Date;

import com.mxi.am.domain.Fault;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.EventInventoryKey;
import com.mxi.mx.core.key.EventInventoryUsageKey;
import com.mxi.mx.core.key.FailDeferRefKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.FaultReferenceKey;
import com.mxi.mx.core.key.FaultReferenceRequestKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryDamageKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefUsgSnapshotSrcTypeKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.maintenance.exec.fault.app.FaultReferenceAppService;
import com.mxi.mx.core.maintenance.exec.fault.domain.FaultReference;
import com.mxi.mx.core.maintenance.exec.fault.domain.FaultReferenceException;
import com.mxi.mx.core.maintenance.exec.fault.domain.FaultReferenceRequest.FaultRequestStatus;
import com.mxi.mx.core.maintenance.exec.fault.infra.FaultReferenceDao;
import com.mxi.mx.core.maintenance.exec.fault.infra.FaultReferenceRequestDao;
import com.mxi.mx.core.maintenance.exec.fault.infra.FaultReferenceRequestTableRow;
import com.mxi.mx.core.maintenance.exec.fault.infra.FaultReferenceTableRow;
import com.mxi.mx.core.services.event.inventory.UsageSnapshot;
import com.mxi.mx.core.table.evt.EvtEventRel;
import com.mxi.mx.core.table.evt.EvtInvTable;
import com.mxi.mx.core.table.evt.EvtInvUsage;
import com.mxi.mx.core.table.inv.InvDamageDao;
import com.mxi.mx.core.table.inv.InvDamageTable;


public final class FaultBuilder {

   public static FaultKey build( Fault aFault ) {

      SdFaultBuilder lFaultBuilder = new SdFaultBuilder();

      lFaultBuilder.withCorrectiveTask( aFault.getCorrectiveTask() );
      lFaultBuilder.withFaultSource( aFault.getFaultSource() );
      lFaultBuilder.withFailureType( aFault.getFailType() );
      lFaultBuilder.withFailureSeverity( aFault.getSeverity() );
      lFaultBuilder.withFaultFoundOn( aFault.getFoundOnDate() );
      lFaultBuilder.withName( aFault.getName() );
      lFaultBuilder.withAlternateId( aFault.getAltId() );
      lFaultBuilder.withDeferralReference( aFault.getDeferralReference() );
      lFaultBuilder.withStatus( aFault.getStatus() );
      lFaultBuilder.withOperationalRestriction( aFault.getOperationalRestriction() );
      lFaultBuilder.withResolutionConfigSlot( aFault.getResolutionConfigSlot() );

      if ( aFault.isEvaluated() ) {
         lFaultBuilder.isEvaluated();
      }

      // if no inventory was provided, check the corrective task
      if ( aFault.getInventory() == null ) {
         aFault.setInventory( getInventoryKey( aFault ) );
      }
      lFaultBuilder.onInventory( aFault.getInventory() );

      FaultKey lFaultKey = lFaultBuilder.build();

      for ( TaskKey lRepetitiveTask : aFault.getRepetitiveTasks() ) {
         EvtEventRel.create( lRepetitiveTask.getEventKey(), lFaultKey.getEventKey(), RECSRC );
      }

      for ( TaskKey lFaultRelatedTask : aFault.getFaultRelatedTasks() ) {
         EvtEventRel.create( lFaultRelatedTask.getEventKey(), lFaultKey.getEventKey(), FAULTREL );
      }

      if ( aFault.getInventory() != null ) {

         EvtInvTable lEvtInv = EvtInvTable.findMainByEventKey( lFaultKey.getEventKey() );

         if ( aFault.getParentAssemblyInventory() != null ) {
            lEvtInv.setAssmblInventoryKey( aFault.getParentAssemblyInventory() );
            lEvtInv.update();
         }

         if ( aFault.getFailedSystem() != null ) {
            lEvtInv.setBomItemKey( aFault.getFailedSystem() );
            lEvtInv.update();
         }

         // Create the "Usage When Found" usage snapshots.
         EventInventoryKey lEvtInvKey = lEvtInv.getPk();
         for ( UsageSnapshot lUsageSnapshot : aFault.getUsageSnapshots() ) {
            EvtInvUsage lEvtInvUsage = EvtInvUsage.create(
                  new EventInventoryUsageKey( lEvtInvKey, lUsageSnapshot.getDataType() ),
                  lUsageSnapshot.getTSN(), lUsageSnapshot.getTSO(), lUsageSnapshot.getTSI(),
                  RefUsgSnapshotSrcTypeKey.MAINTENIX );
            lEvtInvUsage.setAssmblTsnQt( lUsageSnapshot.getAssemblyTSN() );
            lEvtInvUsage.setAssmblTsoQt( lUsageSnapshot.getAssemblyTSO() );
         }
      }

      // add repair and deferral references
      addReferences( aFault, lFaultKey );

      if ( aFault.getDamageRecordInventory() != null ) {
         InvDamageDao invDamageDao = InjectorContainer.get().getInstance( InvDamageDao.class );
         InventoryDamageKey invDamageKey = invDamageDao.generatePrimaryKey();
         InvDamageTable invDamageRow = invDamageDao.create( invDamageKey );
         invDamageRow.setInventoryKey( aFault.getDamageRecordInventory() );
         invDamageRow.setFaultKey( lFaultKey );
         invDamageDao.insert( invDamageRow );
      }

      return lFaultKey;
   }


   /**
    * Gets the inventory key from the corrective task if the corrective task has been provided.
    * Returns null if there is no corrective task or inventory.
    */
   private static InventoryKey getInventoryKey( Fault fault ) {

      InventoryKey inventoryKey = null;

      if ( fault.getCorrectiveTask() != null ) {

         DataSetArgument whereArgs = new DataSetArgument();
         whereArgs.add( fault.getCorrectiveTask(), "sched_db_id", "sched_id" );

         QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "sched_stask", whereArgs );

         if ( lQs.next() ) {

            inventoryKey = lQs.getKey( InventoryKey.class, "main_inv_no_db_id", "main_inv_no_id" );
         }
      }
      return inventoryKey;

   }


   /**
    * DOCUMENT_ME
    *
    * @param aFault
    * @param lFaultKey
    */
   private static void addReferences( Fault aFault, FaultKey lFaultKey ) {

      FaultReferenceAppService faultReferenceAppService =
            InjectorContainer.get().getInstance( FaultReferenceAppService.class );
      HumanResourceKey REQUESTOR_HR = new HumanResourceKey( "4650:1" );
      FaultReferenceKey faultReferenceKey = null;

      if ( aFault.getCurrentDeferralReference() != null
            && aFault.getCurrentRepairReference() != null ) {
         throw new RuntimeException( "Cannot have more than one current reference per fault" );
      }

      if ( aFault.getCurrentDeferralReference() != null ) {
         faultReferenceKey = setFaultDeferralReferenceRow( lFaultKey, aFault, true );
      }

      if ( aFault.getCurrentRepairReference() != null ) {

         FaultReference reference = FaultReference.builder()
               .repairReferenceKey( aFault.getCurrentRepairReference() ).build();
         try {
            faultReferenceAppService.applyReference( REQUESTOR_HR, lFaultKey, reference );
         } catch ( FaultReferenceException e ) {
            throw new IllegalStateException( e );
         }
      }

      if ( aFault.getNonCurrentDeferralReference() != null ) {
         faultReferenceKey = setFaultDeferralReferenceRow( lFaultKey, aFault, false );
      }

      if ( aFault.getNonCurrentRepairReference() != null ) {
         faultReferenceKey = setFaultRepairReferenceRow( lFaultKey, aFault, false );
      }

      if ( faultReferenceKey != null && aFault.isCreateReferenceRequest() ) {
         createReferenceRequest( faultReferenceKey );
      }
   }


   public static void createReferenceRequest( FaultReferenceKey faultReferenceKey ) {
      FaultReferenceRequestDao faultReferenceRequestDao =
            InjectorContainer.get().getInstance( FaultReferenceRequestDao.class );

      // Create Table Row
      FaultReferenceRequestTableRow faultReferenceRequestTableRow = faultReferenceRequestDao
            .create( new FaultReferenceRequestKey( faultReferenceKey.toValueString() ) );

      faultReferenceRequestTableRow.setDateRequested( new Date() );
      faultReferenceRequestTableRow.setHumanResource( new HumanResourceKey( "0:1" ) );
      faultReferenceRequestTableRow.setRequestStatus( FaultRequestStatus.PENDING.name() );

      faultReferenceRequestDao.insert( faultReferenceRequestTableRow );

   }


   public static FaultReferenceKey setFaultRepairReferenceRow( FaultKey aFaultKey, Fault aFault,
         boolean aCurrentReferenceBool ) {
      FaultReferenceDao lFaultReferenceDao =
            InjectorContainer.get().getInstance( FaultReferenceDao.class );

      // Key
      FaultReferenceKey lFaultReferenceKey = lFaultReferenceDao.generatePrimaryKey();

      // Create Table Row
      FaultReferenceTableRow lFaultReferenceTableRow =
            lFaultReferenceDao.create( lFaultReferenceKey );

      // Add Deferral Reference to table row
      TaskTaskKey repaiReference = aCurrentReferenceBool == true
            ? aFault.getCurrentRepairReference() : aFault.getNonCurrentRepairReference();
      lFaultReferenceTableRow.setRepairReferenceKey( repaiReference );
      lFaultReferenceTableRow.setFaultKey( aFaultKey );
      lFaultReferenceTableRow.setCurrentBool( aCurrentReferenceBool );

      lFaultReferenceDao.insert( lFaultReferenceTableRow );

      return lFaultReferenceKey;
   }


   public static FaultReferenceKey setFaultDeferralReferenceRow( FaultKey aFaultKey, Fault aFault,
         boolean aCurrentReferenceBool ) {
      FaultReferenceDao lFaultReferenceDao =
            InjectorContainer.get().getInstance( FaultReferenceDao.class );

      // Key
      FaultReferenceKey lFaultReferenceKey = lFaultReferenceDao.generatePrimaryKey();

      // Create Table Row
      FaultReferenceTableRow lFaultReferenceTableRow =
            lFaultReferenceDao.create( lFaultReferenceKey );

      // Add Deferral Reference to table row
      FailDeferRefKey deferralReference = aCurrentReferenceBool == true
            ? aFault.getCurrentDeferralReference() : aFault.getNonCurrentDeferralReference();
      lFaultReferenceTableRow.setDeferralReferenceKey( deferralReference );
      lFaultReferenceTableRow.setFaultKey( aFaultKey );
      lFaultReferenceTableRow.setCurrentBool( aCurrentReferenceBool );

      lFaultReferenceDao.insert( lFaultReferenceTableRow );

      return lFaultReferenceKey;
   }

}
