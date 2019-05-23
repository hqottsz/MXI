package com.mxi.am.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.CarrierKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.FailDeferRefDeadKey;
import com.mxi.mx.core.key.FailDeferRefKey;
import com.mxi.mx.core.key.RefFailDeferKey;
import com.mxi.mx.core.key.RefFailureSeverityKey;


public class DeferralReference {

   private FailDeferRefKey iLegacyKey;
   private AssemblyKey iAssemblyKey;
   private String iName;
   private RefFailureSeverityKey iFaultSeverityKey;
   private RefFailDeferKey iFaultDeferralKey;
   private String iDescription;
   private String iAltId;
   private FailedSystemInfo iFailedSystemInfo = new FailedSystemInfo();
   private int iInstalledSystems;
   private int iOperationalSystemsForDispatch;
   private String iApplicabilityRange;
   private String iOperationalRestrictions;
   private String iMaintenanceActions;
   private String iPerformancePenalties;
   private String iStatus;
   private boolean requiredMocAuth;

   private List<DeferralReferenceDeadline> iDeadlines = new ArrayList<>();
   private List<CarrierKey> iOperators = new ArrayList<>();
   private List<String> iConflictingDeferralReferences = new ArrayList<>();
   private List<String> iAssociatedDeferralReferences = new ArrayList<>();
   private List<String> iRecurringInspections = new ArrayList<>();


   /**
    * Returns the value of the recurringInspections property.
    *
    * @return the value of the recurringInspections property
    */
   public List<String> getRecurringInspections() {
      return iRecurringInspections;
   }


   /**
    * Sets a new value for the recurringInspections property.
    *
    * @param aRecurringInspections
    *           the new value for the recurringInspections property
    */
   public void setRecurringInspections( List<String> aRecurringInspections ) {
      iRecurringInspections = aRecurringInspections;
   }


   /**
    * Returns the value of the conflictingDeferralReferences property.
    *
    * @return the value of the conflictingDeferralReferences property
    */
   public List<String> getConflictingDeferralReferences() {
      return iConflictingDeferralReferences;
   }


   /**
    * Sets a new value for the conflictingDeferralReferences property.
    *
    * @param aConflictingDeferralReferences
    *           the new value for the conflictingDeferralReferences property
    */
   public void setConflictingDeferralReferences( List<String> aConflictingDeferralReferences ) {
      iConflictingDeferralReferences = aConflictingDeferralReferences;
   }


   /**
    * Returns the value of the associatedDeferralReferences property.
    *
    * @return the value of the associatedDeferralReferences property
    */
   public List<String> getAssociatedDeferralReferences() {
      return iAssociatedDeferralReferences;
   }


   /**
    * Sets a new value for the associatedDeferralReferences property.
    *
    * @param aRelatedDeferralReferences
    *           the new value for the associatedDeferralReferences property
    */
   public void setAssociatedDeferralReferences( List<String> aAssociatedDeferralReferences ) {
      iAssociatedDeferralReferences = aAssociatedDeferralReferences;
   }


   /**
    * Returns the value of the deadlines property.
    *
    * @return the value of the deadlines property
    */
   public List<DeferralReferenceDeadline> getDeadlines() {
      return iDeadlines;
   }


   /**
    * Sets a new value for the deadlines property.
    *
    * @param aDeadlines
    *           the new value for the deadlines property
    */
   public void setDeadlines( List<DeferralReferenceDeadline> aDeadlines ) {
      iDeadlines = aDeadlines;
   }


   /**
    * Returns the value of the operators property.
    *
    * @return the value of the operators property
    */
   public List<CarrierKey> getOperators() {
      return iOperators;
   }


   /**
    * Sets a new value for the operators property.
    *
    * @param aOperators
    *           the new value for the operators property
    */
   public void setOperators( List<CarrierKey> aOperators ) {
      iOperators = aOperators;
   }


   /**
    * Returns the value of the status property.
    *
    * @return the value of the status property
    */
   public String getStatus() {
      return iStatus;
   }


   /**
    * Sets a new value for the status property.
    *
    * @param aStatus
    *           the new value for the status property
    */
   public void setStatus( String aStatus ) {
      iStatus = aStatus;
   }


   public DeferralReference() {

   }


   /**
    * Returns the value of the legacyKey property.
    *
    * @return the value of the legacyKey property
    */
   public FailDeferRefKey getLegacyKey() {
      return iLegacyKey;
   }


   /**
    * Sets a new value for the legacyKey property.
    *
    * @param aLegacyKey
    *           the new value for the legacyKey property
    */
   public void setLegacyKey( FailDeferRefKey aLegacyKey ) {
      iLegacyKey = aLegacyKey;
   }


   /**
    * Returns the value of the assemblyKey property.
    *
    * @return the value of the assemblyKey property
    */
   public AssemblyKey getAssemblyKey() {
      return iAssemblyKey;
   }


   /**
    * Sets a new value for the assemblyKey property.
    *
    * @param aAssemblyKey
    *           the new value for the assemblyKey property
    */
   public void setAssemblyKey( AssemblyKey aAssemblyKey ) {
      iAssemblyKey = aAssemblyKey;
   }


   /**
    * Returns the value of the name property.
    *
    * @return the value of the name property
    */
   public String getName() {
      return iName;
   }


   /**
    * Sets a new value for the name property.
    *
    * @param aName
    *           the new value for the name property
    */
   public void setName( String aName ) {
      iName = aName;
   }


   /**
    * Returns the value of the faultSeverityKey property.
    *
    * @return the value of the faultSeverityKey property
    */
   public RefFailureSeverityKey getFaultSeverityKey() {
      return iFaultSeverityKey;
   }


   /**
    * Sets a new value for the faultSeverityKey property.
    *
    * @param aFaultSeverityKey
    *           the new value for the faultSeverityKey property
    */
   public void setFaultSeverityKey( RefFailureSeverityKey aFaultSeverityKey ) {
      iFaultSeverityKey = aFaultSeverityKey;
   }


   /**
    * Returns the value of the faultDeferralKey property.
    *
    * @return the value of the faultDeferralKey property
    */
   public RefFailDeferKey getFaultDeferralKey() {
      return iFaultDeferralKey;
   }


   /**
    * Sets a new value for the faultDeferralKey property.
    *
    * @param aFaultDeferralKey
    *           the new value for the faultDeferralKey property
    */
   public void setFaultDeferralKey( RefFailDeferKey aFaultDeferralKey ) {
      iFaultDeferralKey = aFaultDeferralKey;
   }


   /**
    * Returns the value of the description property.
    *
    * @return the value of the description property
    */
   public String getDescription() {
      return iDescription;
   }


   /**
    * Sets a new value for the description property.
    *
    * @param aDescription
    *           the new value for the description property
    */
   public void setDescription( String aDescription ) {
      iDescription = aDescription;
   }


   /**
    * Returns the value of the altId property.
    *
    * @return the value of the altId property
    */
   public String getAltId() {
      return iAltId;
   }


   /**
    * Sets a new value for the altId property.
    *
    * @param aAltId
    *           the new value for the altId property
    */
   public void setAltId( String aAltId ) {
      iAltId = aAltId;
   }


   /**
    * Returns the value of the failedSystemInfo property.
    *
    * @return the value of the failedSystemInfo property
    */
   public FailedSystemInfo getFailedSystemInfo() {
      return iFailedSystemInfo;
   }


   /**
    * Sets a new value for the failedSystemInfo property.
    *
    * @param aFailedSystemInfo
    *           the new value for the failedSystemInfo property
    */
   public void setFailedSystemInfo( FailedSystemInfo aFailedSystemInfo ) {
      iFailedSystemInfo = aFailedSystemInfo;
   }


   /**
    * Returns the value of the installedSystems property.
    *
    * @return the value of the installedSystems property
    */
   public int getInstalledSystems() {
      return iInstalledSystems;
   }


   /**
    * Sets a new value for the installedSystems property.
    *
    * @param aInstalledSystems
    *           the new value for the installedSystems property
    */
   public void setInstalledSystems( int aInstalledSystems ) {
      iInstalledSystems = aInstalledSystems;
   }


   /**
    * Returns the value of the operationalSystemsForDispatch property.
    *
    * @return the value of the operationalSystemsForDispatch property
    */
   public int getOperationalSystemsForDispatch() {
      return iOperationalSystemsForDispatch;
   }


   /**
    * Sets a new value for the operationalSystemsForDispatch property.
    *
    * @param aOperationalSystemsForDispatch
    *           the new value for the operationalSystemsForDispatch property
    */
   public void setOperationalSystemsForDispatch( int aOperationalSystemsForDispatch ) {
      iOperationalSystemsForDispatch = aOperationalSystemsForDispatch;
   }


   /**
    * Returns the value of the applicabilityRange property.
    *
    * @return the value of the applicabilityRange property
    */
   public String getApplicabilityRange() {
      return iApplicabilityRange;
   }


   /**
    * Sets a new value for the applicabilityRange property.
    *
    * @param aApplicabilityRange
    *           the new value for the applicabilityRange property
    */
   public void setApplicabilityRange( String aApplicabilityRange ) {
      iApplicabilityRange = aApplicabilityRange;
   }


   /**
    * Returns the value of the operationalRestrictions property.
    *
    * @return the value of the operationalRestrictions property
    */
   public String getOperationalRestrictions() {
      return iOperationalRestrictions;
   }


   /**
    * Sets a new value for the operationalRestrictions property.
    *
    * @param aOperationalRestrictions
    *           the new value for the operationalRestrictions property
    */
   public void setOperationalRestrictions( String aOperationalRestrictions ) {
      iOperationalRestrictions = aOperationalRestrictions;
   }


   /**
    * Returns the value of the maintenanceActions property.
    *
    * @return the value of the maintenanceActions property
    */
   public String getMaintenanceActions() {
      return iMaintenanceActions;
   }


   /**
    * Sets a new value for the maintenanceActions property.
    *
    * @param aMaintenanceActions
    *           the new value for the maintenanceActions property
    */
   public void setMaintenanceActions( String aMaintenanceActions ) {
      iMaintenanceActions = aMaintenanceActions;
   }


   /**
    * Returns the value of the performancePenalties property.
    *
    * @return the value of the performancePenalties property
    */
   public String getPerformancePenalties() {
      return iPerformancePenalties;
   }


   /**
    * Sets a new value for the performancePenalties property.
    *
    * @param aPerformancePenalties
    *           the new value for the performancePenalties property
    */
   public void setPerformancePenalties( String aPerformancePenalties ) {
      iPerformancePenalties = aPerformancePenalties;
   }


   public boolean isRequiredMocAuth() {
      return requiredMocAuth;
   }


   public void setRequiredMocAuth( boolean requiredMocAuth ) {
      this.requiredMocAuth = requiredMocAuth;
   }


   public static class DeferralReferenceDeadline {

      private FailDeferRefDeadKey iId;
      private UUID iDeferralReferenceId;
      private DataTypeKey iDataType;
      private BigDecimal iQuantity;


      /**
       * Returns the value of the id property.
       *
       * @return the value of the id property
       */
      public FailDeferRefDeadKey getId() {
         return iId;
      }


      /**
       * Sets a new value for the id property.
       *
       * @param aId
       *           the new value for the id property
       */
      public void setId( FailDeferRefDeadKey aId ) {
         iId = aId;
      }


      /**
       * Returns the value of the deferralReferenceId property.
       *
       * @return the value of the deferralReferenceId property
       */
      public UUID getDeferralReferenceId() {
         return iDeferralReferenceId;
      }


      /**
       * Sets a new value for the deferralReferenceId property.
       *
       * @param aDeferralReferenceId
       *           the new value for the deferralReferenceId property
       */
      public void setDeferralReferenceId( UUID aDeferralReferenceId ) {
         iDeferralReferenceId = aDeferralReferenceId;
      }


      /**
       * Returns the value of the dataType property.
       *
       * @return the value of the dataType property
       */
      public DataTypeKey getDataType() {
         return iDataType;
      }


      /**
       * Sets a new value for the dataType property.
       *
       * @param aDataType
       *           the new value for the dataType property
       */
      public void setDataType( DataTypeKey aDataType ) {
         iDataType = aDataType;
      }


      /**
       * Returns the value of the quantity property.
       *
       * @return the value of the quantity property
       */
      public BigDecimal getQuantity() {
         return iQuantity;
      }


      /**
       * Sets a new value for the quantity property.
       *
       * @param aQuantity
       *           the new value for the quantity property
       */
      public void setQuantity( BigDecimal aQuantity ) {
         iQuantity = aQuantity;
      }

   }

   /**
    * Allows the failed system config slot associated with this deferral reference to be tracked
    * either via the alt_id or the traditional composite key.
    *
    */
   public static class FailedSystemInfo {

      private ConfigSlotKey iFailedSystemKey;
      // corresponds to the ASSMBL_BOM_ID column in fail_defer_ref
      private UUID iFailedSystemAltId;


      public ConfigSlotKey getFailedSystemKey() {
         return iFailedSystemKey;
      }


      public void setFailedSystemKey( ConfigSlotKey aFailedSystemKey ) {
         iFailedSystemKey = aFailedSystemKey;
      }


      public UUID getFailedSystemAltId() {
         return iFailedSystemAltId;
      }


      public void setFailedSystemAltId( UUID aFailedSystemAltId ) {
         iFailedSystemAltId = aFailedSystemAltId;
      }
   }

}
