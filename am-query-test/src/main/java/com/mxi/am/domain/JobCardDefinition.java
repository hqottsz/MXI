package com.mxi.am.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.TaskTaskKey;


public class JobCardDefinition {

   private ConfigSlotKey iConfigurationSlot;
   private TaskTaskKey iPreviousRevision;
   private String iCode;
   private Boolean iOnCondition;
   private Integer iRevisionNumber;
   private RefTaskDefinitionStatusKey iStatus;
   private Boolean iFleetApproval;
   private Boolean iIsWorkScopeEnabled;

   private Boolean iPreventManualInitialization;
   private List<DomainConfiguration<Step>> iSteps = new ArrayList<>();
   private List<LabourRequirement> iLabourRequirements = new ArrayList<>();


   public ConfigSlotKey getConfigurationSlot() {
      return iConfigurationSlot;
   }


   public void setConfigurationSlot( ConfigSlotKey aConfigurationSlot ) {
      iConfigurationSlot = aConfigurationSlot;
   }


   public TaskTaskKey getPreviousRevision() {
      return iPreviousRevision;
   }


   public void setPreviousRevision( TaskTaskKey aPreviousRevision ) {
      iPreviousRevision = aPreviousRevision;
   }


   public String getCode() {
      return iCode;
   }


   public void setCode( String aCode ) {
      iCode = aCode;
   }


   public Boolean isOnCondition() {
      return iOnCondition;
   }


   public void setOnCondition( boolean aOnCondition ) {
      iOnCondition = aOnCondition;
   }


   public Integer getRevisionNumber() {
      return iRevisionNumber;
   }


   public void setRevisionNumber( Integer aRevisionNumber ) {
      iRevisionNumber = aRevisionNumber;
   }


   public RefTaskDefinitionStatusKey getStatus() {
      return iStatus;
   }


   public void setStatus( RefTaskDefinitionStatusKey aStatus ) {
      iStatus = aStatus;
   }


   public void setFleetApproval( boolean aFleetApproval ) {
      iFleetApproval = aFleetApproval;
   }


   public Boolean getFleetApproval() {
      return iFleetApproval;
   }


   public void setWorkScopeEnabled( boolean aValue ) {
      iIsWorkScopeEnabled = aValue;
   }


   public Optional<Boolean> isWorkScopeEnabled() {
      return Optional.ofNullable( iIsWorkScopeEnabled );
   }


   public List<DomainConfiguration<Step>> getSteps() {
      return iSteps;
   }


   public void addStep( DomainConfiguration<Step> aStep ) {
      iSteps.add( aStep );
   }


   public void addLabourRequirement( RefLabourSkillKey aLabourSkill,
         BigDecimal aWorkPerformedScheduleHours ) {
      iLabourRequirements.add( new LabourRequirement( aLabourSkill, aWorkPerformedScheduleHours ) );
   }


   public List<LabourRequirement> getLaborRequirements() {
      return iLabourRequirements;
   }


   public Optional<Boolean> isPreventManualInitialization() {
      return Optional.ofNullable( iPreventManualInitialization );
   }


   public void setPreventManualInitialization( Boolean aPreventManualInitialization ) {
      iPreventManualInitialization = aPreventManualInitialization;
   }

}
