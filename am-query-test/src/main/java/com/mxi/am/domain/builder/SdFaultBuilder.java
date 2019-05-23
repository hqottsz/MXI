package com.mxi.am.domain.builder;

import java.util.Date;

import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefCdKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefEventTypeKey;
import com.mxi.mx.core.key.RefFailTypeKey;
import com.mxi.mx.core.key.RefFailureSeverityKey;
import com.mxi.mx.core.key.RefFaultSourceKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.evt.EvtEventRel;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.table.sd.SdFaultTable;
import com.mxi.mx.persistence.uuid.UuidUtils;


/**
 * Builds an <code>sd_fault</code> object
 */
public class SdFaultBuilder implements DomainBuilder<FaultKey> {

   private Boolean iIsEvaluated;

   private String iName;
   private RefFailureSeverityKey iSeverity;
   private RefFaultSourceKey iFaultSource;
   private InventoryKey iInventory;
   private RefFailTypeKey iFailType;
   private TaskKey iCorrectiveTask;
   private Date iFoundOnDate;
   private RefEventStatusKey iEventStatus;
   private String iAlternateId;
   private String iDeferralReference;
   private String iOperationalRestriction;
   private ConfigSlotKey iResolutionConfigSlot;


   /**
    * {@inheritDoc}
    */
   @Override
   public FaultKey build() {

      EventBuilder lEventBuilder = new EventBuilder();
      lEventBuilder.withType( RefEventTypeKey.CF );
      lEventBuilder.withDescription( iName );
      lEventBuilder.withActualStartDate( iFoundOnDate );

      if ( iInventory != null ) {
         lEventBuilder.onInventory( iInventory );
      }

      if ( iEventStatus != null ) {
         lEventBuilder.withStatus( iEventStatus );
      }

      EventKey lEvent = lEventBuilder.build();

      FaultKey lFaultKey = new FaultKey( lEvent.getDbId(), lEvent.getId() );

      SdFaultTable lTable = null;

      if ( iAlternateId != null ) {
         lTable = SdFaultTable.create( lFaultKey, UuidUtils.fromHexString( iAlternateId ) );
      } else {
         lTable = SdFaultTable.create( lFaultKey );
      }

      if ( iIsEvaluated != null ) {
         lTable.setEvalBool( iIsEvaluated );
      }

      if ( iSeverity != null ) {
         lTable.setFailSev( iSeverity );
      }

      if ( iFailType != null ) {
         lTable.setFailureType( iFailType );
      }

      if ( iFaultSource != null ) {
         lTable.setFaultSource( iFaultSource );
      }

      if ( iCorrectiveTask != null ) {
         EvtEventRel.create( lFaultKey.getEventKey(), iCorrectiveTask.getEventKey(),
               new RefCdKey( "rel_type", "CORRECT" ) );

         // Update fault link in sched_stask for corrective task
         SchedStaskTable lSchedStaskTable = SchedStaskTable.findByPrimaryKey( iCorrectiveTask );
         lSchedStaskTable.setFault( lFaultKey );
         lSchedStaskTable.update();
      }

      if ( iDeferralReference != null ) {
         lTable.setDeferRefSdesc( iDeferralReference );
      }

      lTable.setOperationalRestriction( iOperationalRestriction );
      lTable.setResolutionConfigSlot( iResolutionConfigSlot );

      lTable.insert();

      return lFaultKey;
   }


   public SdFaultBuilder isEvaluated() {
      iIsEvaluated = true;

      return this;
   }


   public SdFaultBuilder withAlternateId( String aAltId ) {
      iAlternateId = aAltId;

      return this;
   }


   public SdFaultBuilder withName( String aName ) {
      iName = aName;

      return this;
   }


   public SdFaultBuilder withFaultFoundOn( Date aFoundOnDate ) {
      iFoundOnDate = aFoundOnDate;

      return this;
   }


   public SdFaultBuilder withFailureSeverity( RefFailureSeverityKey aSeverity ) {
      iSeverity = aSeverity;
      return this;
   }


   public SdFaultBuilder withFailureType( RefFailTypeKey aType ) {
      iFailType = aType;
      return this;
   }


   public SdFaultBuilder onInventory( InventoryKey aInventory ) {
      iInventory = aInventory;
      return this;
   }


   public SdFaultBuilder withFaultSource( RefFaultSourceKey aFaultSource ) {
      iFaultSource = aFaultSource;
      return this;
   }


   public SdFaultBuilder withCorrectiveTask( TaskKey aCorrTaskKey ) {
      iCorrectiveTask = aCorrTaskKey;
      return this;
   }


   public SdFaultBuilder withStatus( RefEventStatusKey aEventStatus ) {
      iEventStatus = aEventStatus;
      return this;
   }


   public SdFaultBuilder withDeferralReference( String aDeferralReference ) {
      iDeferralReference = aDeferralReference;
      return this;
   }


   public SdFaultBuilder withOperationalRestriction( String aOperationalRestriction ) {
      iOperationalRestriction = aOperationalRestriction;
      return this;
   }


   public SdFaultBuilder withResolutionConfigSlot( ConfigSlotKey aResolutionConfigSlot ) {
      iResolutionConfigSlot = aResolutionConfigSlot;
      return this;
   }
}
