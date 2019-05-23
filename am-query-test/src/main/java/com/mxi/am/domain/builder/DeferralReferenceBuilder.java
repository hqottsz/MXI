package com.mxi.am.domain.builder;

import java.math.BigDecimal;
import java.util.UUID;

import com.mxi.am.domain.DeferralReference;
import com.mxi.am.domain.DeferralReference.DeferralReferenceDeadline;
import com.mxi.mx.common.utils.FormatUtil;
import com.mxi.mx.core.key.CarrierKey;
import com.mxi.mx.core.key.FailDeferRefConflictDefKey;
import com.mxi.mx.core.key.FailDeferRefKey;
import com.mxi.mx.core.key.FailDeferRefRelDefKey;
import com.mxi.mx.core.key.FailDeferRefTaskDefnKey;
import com.mxi.mx.core.table.fail.FailDeferCarrier;
import com.mxi.mx.core.table.fail.FailDeferRef;
import com.mxi.mx.core.table.fail.FailDeferRefConflictDef;
import com.mxi.mx.core.table.fail.FailDeferRefDead;
import com.mxi.mx.core.table.fail.FailDeferRefRelDef;
import com.mxi.mx.core.table.fail.FailDeferRefTaskDefn;


/**
 * Creates a fail_defer_ref object
 *
 */
public class DeferralReferenceBuilder {

   public static FailDeferRefKey build( DeferralReference aDeferralReference ) {

      FailDeferRef lFailDeferRefTable = new FailDeferRef();

      lFailDeferRefTable.setDeferralSDesc( aDeferralReference.getName() );
      lFailDeferRefTable.setDeferralLDesc( aDeferralReference.getDescription() );
      lFailDeferRefTable.setInstalledSystemQty( aDeferralReference.getInstalledSystems() );
      lFailDeferRefTable
            .setOperationalSystemQty( aDeferralReference.getOperationalSystemsForDispatch() );
      lFailDeferRefTable.setApplLDesc( aDeferralReference.getApplicabilityRange() );
      lFailDeferRefTable
            .setOperationalRestrictionsLDesc( aDeferralReference.getOperationalRestrictions() );
      lFailDeferRefTable.setMaintenanceActionLDesc( aDeferralReference.getMaintenanceActions() );
      lFailDeferRefTable
            .setPerformancePenaltiesLDesc( aDeferralReference.getPerformancePenalties() );
      lFailDeferRefTable.setDeferRefStatusCd( aDeferralReference.getStatus() );
      lFailDeferRefTable.setSeverity( aDeferralReference.getFaultSeverityKey() );
      lFailDeferRefTable.setAssembly( aDeferralReference.getAssemblyKey() );
      lFailDeferRefTable.setDeferral( aDeferralReference.getFaultDeferralKey() );
      lFailDeferRefTable.setMocApprovalBool( aDeferralReference.isRequiredMocAuth() );

      if ( aDeferralReference.getFailedSystemInfo() != null
            && aDeferralReference.getFailedSystemInfo().getFailedSystemAltId() != null ) {
         lFailDeferRefTable
               .setAssemblyBomId( aDeferralReference.getFailedSystemInfo().getFailedSystemAltId() );
      }

      FailDeferRefKey lFailDeferRefKey = lFailDeferRefTable.insert();
      UUID lDeferralReferenceAltId = lFailDeferRefTable.getAlternateKey();

      // create deadlines
      for ( DeferralReferenceDeadline lDeadline : aDeferralReference.getDeadlines() ) {
         if ( lDeadline != null ) {
            BigDecimal lQuantity = lDeadline.getQuantity();
            FailDeferRefDead lFailDeferRefDead = FailDeferRefDead.create();
            {
               lFailDeferRefDead.setDeferralReferenceId( lDeferralReferenceAltId );
               lFailDeferRefDead.setDataType( lDeadline.getDataType() );
               lFailDeferRefDead.setQuantity( lQuantity );
            }
            lFailDeferRefDead.insert();
         }
      }

      // create operators
      for ( CarrierKey lOperator : aDeferralReference.getOperators() ) {
         if ( lOperator != null ) {
            FailDeferCarrier.create( lFailDeferRefKey, lOperator ).insert();
         }
      }

      String lDeferralReferenceId = FormatUtil
            .formatUniqueIdRemoveHyphens( lFailDeferRefTable.getAlternateKey().toString() );

      // create conflicting deferral references
      for ( String lConflictId : aDeferralReference.getConflictingDeferralReferences() ) {

         FailDeferRefConflictDefKey lConflictKey =
               new FailDeferRefConflictDefKey( lDeferralReferenceId, lConflictId );

         FailDeferRefConflictDef.create( lConflictKey ).insert();
      }

      // create related deferral references
      for ( String lRelatedId : aDeferralReference.getAssociatedDeferralReferences() ) {
         FailDeferRefRelDefKey lRelatedKey =
               new FailDeferRefRelDefKey( lDeferralReferenceId, lRelatedId );
         FailDeferRefRelDef.create( lRelatedKey ).insert();
      }

      // create recurring inspections task definitions
      for ( String lRecurringInspectionId : aDeferralReference.getRecurringInspections() ) {
         FailDeferRefTaskDefnKey lDeferRefTaskDefnKey =
               new FailDeferRefTaskDefnKey( lDeferralReferenceId, lRecurringInspectionId );
         FailDeferRefTaskDefn.create( lDeferRefTaskDefnKey ).insert();
      }

      return lFailDeferRefKey;

   }
}
