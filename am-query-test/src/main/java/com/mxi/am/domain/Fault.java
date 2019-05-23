package com.mxi.am.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.FailDeferRefKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefFailTypeKey;
import com.mxi.mx.core.key.RefFailureSeverityKey;
import com.mxi.mx.core.key.RefFaultSourceKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.services.event.inventory.UsageSnapshot;


public class Fault {

   private Boolean iIsEvaluated;
   private String iName;
   private RefFailureSeverityKey iSeverity;
   private RefFaultSourceKey iFaultSource;
   private InventoryKey iInventory;
   private InventoryKey iParentAssemblyInventory;
   private RefFailTypeKey iFailType;
   private TaskKey iCorrectiveTask;
   private Date iFoundOnDate;
   private String iAltId;
   private String iDeferralReference;
   private String iOperationalRestriction;
   private RefEventStatusKey iStatus;
   private ConfigSlotKey iResolutionConfigSlot;
   private ConfigSlotKey iFailedSystem;
   private List<TaskKey> iRepetitiveTasks = new ArrayList<>();
   private List<TaskKey> iFaultRelatedTasks = new ArrayList<>();
   private TaskTaskKey iCurrentRepairReference;
   private FailDeferRefKey iCurrentDeferralReference;
   private TaskTaskKey iNonCurrentRepairReference;
   private FailDeferRefKey iNonCurrentDeferralReference;
   private InventoryKey damageRecordInventory;
   private boolean createReferenceRequest;

   // Note: these snapshots are the "Usage When Found" usage snapshots.
   private List<UsageSnapshot> iUsageSnapshots = new ArrayList<UsageSnapshot>();


   public boolean isCreateReferenceRequest() {
      return createReferenceRequest;
   }


   public void setCreateReferenceRequest( boolean createReferenceRequest ) {
      this.createReferenceRequest = createReferenceRequest;
   }


   public String getDeferralReference() {
      return iDeferralReference;
   }


   public void setDeferralReference( String aDeferralReference ) {
      iDeferralReference = aDeferralReference;
   }


   public void addUsageSnapshot( UsageSnapshot aUsageSnapshot ) {
      iUsageSnapshots.add( aUsageSnapshot );
   }


   public String getAltId() {
      return iAltId;
   }


   public void setAltId( String aAltId ) {
      iAltId = aAltId;
   }


   public List<UsageSnapshot> getUsageSnapshots() {
      return iUsageSnapshots;
   }


   public InventoryKey getParentAssemblyInventory() {
      return iParentAssemblyInventory;
   }


   public void setParentAssemblyInventory( InventoryKey aParentAssemblyInventory ) {
      iParentAssemblyInventory = aParentAssemblyInventory;
   }


   public Boolean isEvaluated() {
      return iIsEvaluated == null ? false : iIsEvaluated;
   }


   public void setIsEvaluated( Boolean aIsEvaluated ) {
      iIsEvaluated = aIsEvaluated;
   }


   public String getName() {
      return iName;
   }


   public void setName( String aName ) {
      iName = aName;
   }


   public RefFailureSeverityKey getSeverity() {
      return iSeverity;
   }


   public void setSeverity( RefFailureSeverityKey aSeverity ) {
      iSeverity = aSeverity;
   }


   public RefFaultSourceKey getFaultSource() {
      return iFaultSource;
   }


   public void setFaultSource( RefFaultSourceKey aFaultSource ) {
      iFaultSource = aFaultSource;
   }


   public InventoryKey getInventory() {
      return iInventory;
   }


   public void setInventory( InventoryKey aInventory ) {
      iInventory = aInventory;
   }


   public RefFailTypeKey getFailType() {
      return iFailType;
   }


   public void setFailType( RefFailTypeKey aFailType ) {
      iFailType = aFailType;
   }


   public TaskKey getCorrectiveTask() {
      return iCorrectiveTask;
   }


   public void setCorrectiveTask( TaskKey aCorrectiveTask ) {
      iCorrectiveTask = aCorrectiveTask;
   }


   public Date getFoundOnDate() {
      return iFoundOnDate;
   }


   public void setFoundOnDate( Date aFoundOnDate ) {
      iFoundOnDate = aFoundOnDate;
   }


   public String getOperationalRestriction() {
      return iOperationalRestriction;
   }


   public void setOperationalRestriction( String aOperationalRestriction ) {
      iOperationalRestriction = aOperationalRestriction;
   }


   public RefEventStatusKey getStatus() {
      return iStatus;
   }


   public void setStatus( RefEventStatusKey aStatus ) {
      iStatus = aStatus;
   }


   public ConfigSlotKey getResolutionConfigSlot() {
      return iResolutionConfigSlot;
   }


   public void setResolutionConfigSlot( ConfigSlotKey aResolutionConfigSlot ) {
      iResolutionConfigSlot = aResolutionConfigSlot;
   }


   public ConfigSlotKey getFailedSystem() {
      return iFailedSystem;
   }


   public void setFailedSystem( ConfigSlotKey aFailedSystem ) {
      iFailedSystem = aFailedSystem;
   }


   public List<TaskKey> getRepetitiveTasks() {
      return iRepetitiveTasks;
   }


   public void addRepetitiveTask( TaskKey aRepetiveTask ) {
      iRepetitiveTasks.add( aRepetiveTask );
   }


   public List<TaskKey> getFaultRelatedTasks() {
      return iFaultRelatedTasks;
   }


   public void addFaultRelatedTask( TaskKey aFollowOnTask ) {
      iFaultRelatedTasks.add( aFollowOnTask );
   }


   public FailDeferRefKey getCurrentDeferralReference() {
      return iCurrentDeferralReference;
   }


   public void setCurrentDeferralReference( FailDeferRefKey iCurrentDeferalReference ) {
      this.iCurrentDeferralReference = iCurrentDeferalReference;
   }


   public TaskTaskKey getCurrentRepairReference() {
      return iCurrentRepairReference;
   }


   public void setCurrentRepairReference( TaskTaskKey iCurrentRepairReference ) {
      this.iCurrentRepairReference = iCurrentRepairReference;
   }


   public void setDamageRecordInventory( InventoryKey damageRecordInventory ) {
      this.damageRecordInventory = damageRecordInventory;
   }


   public InventoryKey getDamageRecordInventory() {
      return this.damageRecordInventory;
   }


   public FailDeferRefKey getNonCurrentDeferralReference() {
      return iNonCurrentDeferralReference;
   }


   public void setNonCurrentDeferralReference( FailDeferRefKey aNonCurrentDeferralReference ) {
      this.iNonCurrentDeferralReference = aNonCurrentDeferralReference;
   }


   public TaskTaskKey getNonCurrentRepairReference() {
      return iNonCurrentRepairReference;
   }


   public void setNonCurrentRepairReference( TaskTaskKey aNonCurrentRepairReference ) {
      this.iNonCurrentRepairReference = aNonCurrentRepairReference;
   }

}
