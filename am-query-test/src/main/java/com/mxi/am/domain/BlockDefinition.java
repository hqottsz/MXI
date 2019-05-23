package com.mxi.am.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.RefWorkTypeKey;
import com.mxi.mx.core.key.TaskTaskKey;


public class BlockDefinition {

   private TaskTaskKey iPreviousRevision;
   private Boolean iUnique;
   private Boolean iOnCondition;
   private Boolean iRecurring;
   private ConfigSlotKey iConfigurationSlot;
   private RefTaskDefinitionStatusKey iStatus;
   private Boolean iFleetApproval;
   private Integer iRevisionNumber;
   private Set<TaskTaskKey> iRequirementDefinitions = new HashSet<TaskTaskKey>();
   private List<RefWorkTypeKey> iWorkTypes = new ArrayList<>();


   public void makeUnique() {
      iUnique = true;
   }


   public void setUnique( boolean aUnique ) {
      iUnique = aUnique;
   }


   public Boolean isUnique() {
      return iUnique;
   }


   public void setOnCondition( boolean aOnCondition ) {
      iOnCondition = aOnCondition;
   }


   public Boolean isOnCondition() {
      return iOnCondition;
   }


   public void setRecurring( boolean aRecurring ) {
      iRecurring = aRecurring;
   }


   public Boolean isRecurring() {
      return iRecurring;
   }


   public void setStatus( RefTaskDefinitionStatusKey aStatus ) {
      iStatus = aStatus;
   }


   public RefTaskDefinitionStatusKey getStatus() {
      return iStatus;
   }


   public void setConfigurationSlot( ConfigSlotKey aConfigurationSlot ) {
      iConfigurationSlot = aConfigurationSlot;
   }


   public ConfigSlotKey getConfigurationSlot() {
      return iConfigurationSlot;
   }


   public void setFleetApproval( boolean aFleetApproval ) {
      iFleetApproval = aFleetApproval;
   }


   public Boolean getFleetApproval() {
      return iFleetApproval;
   }


   public void addRequirementDefinition( TaskTaskKey aReqKey ) {
      iRequirementDefinitions.add( aReqKey );
   }


   public Set<TaskTaskKey> getRequirementDefinitions() {
      return iRequirementDefinitions;
   }


   public void setRevisionNumber( Integer aRevisionNumber ) {
      iRevisionNumber = aRevisionNumber;
   }


   public Integer getRevisionNumber() {
      return iRevisionNumber;
   }


   public void setPreviousRevision( TaskTaskKey aPreviousRevision ) {
      iPreviousRevision = aPreviousRevision;
   }


   public TaskTaskKey getPreviousRevision() {
      return iPreviousRevision;
   }


   public List<RefWorkTypeKey> getWorkTypes() {
      return iWorkTypes;
   }


   public void addWorkType( RefWorkTypeKey aWorkType ) {
      iWorkTypes.add( aWorkType );
   }

}
