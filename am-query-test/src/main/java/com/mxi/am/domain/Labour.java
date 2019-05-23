package com.mxi.am.domain;

import java.util.Optional;

import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefLabourStageKey;
import com.mxi.mx.core.key.RefStageReasonKey;
import com.mxi.mx.core.key.SchedLabourKey;
import com.mxi.mx.core.key.TaskKey;


/**
 * Represents a single labour requirement within a task (one row of work).
 *
 * Note; that the name of the class is "labour" and not "labour requirement". This is due "labour
 * requirement" having many meanings in the Mx domain. Task definitions (e.g. Job Card Definition)
 * have labour requirements describing the labour that is required by the definition. However, this
 * labour is in relation to work done within a task.
 */
public class Labour {

   private TaskKey task;
   private RefLabourSkillKey skill;
   private RefLabourStageKey stage;
   private RefStageReasonKey stageReason;
   private SchedLabourKey sourceJobStopLabour;
   private LabourRole technician;
   private LabourRole certifier;
   private LabourRole inspector;
   private Boolean requiresCertification;
   private Boolean requiresInspection;


   public TaskKey getTask() {
      return task;
   }


   public void setTask( TaskKey task ) {
      this.task = task;
   }


   public Optional<RefLabourSkillKey> getSkill() {
      return Optional.ofNullable( skill );
   }


   public void setSkill( RefLabourSkillKey skill ) {
      this.skill = skill;
   }


   public Optional<RefLabourStageKey> getStage() {
      return Optional.ofNullable( stage );
   }


   public void setStage( RefLabourStageKey stage ) {
      this.stage = stage;
   }


   public Optional<RefStageReasonKey> getStageReason() {
      return Optional.ofNullable( stageReason );
   }


   public void setStageReason( RefStageReasonKey stageReason ) {
      this.stageReason = stageReason;
   }


   public Optional<SchedLabourKey> getSourceJobStopLabour() {
      return Optional.ofNullable( sourceJobStopLabour );
   }


   public void setSourceJobStopLabour( SchedLabourKey sourceJobStopLabour ) {
      this.sourceJobStopLabour = sourceJobStopLabour;
   }


   public Optional<Boolean> getRequiresCertification() {
      return Optional.ofNullable( requiresCertification );
   }


   public void setRequiresCertification( boolean requiresCertification ) {
      this.requiresCertification = requiresCertification;
   }


   public Optional<Boolean> getRequiresInspection() {
      return Optional.ofNullable( requiresInspection );
   }


   public void setRequiresInspection( boolean requiresInspection ) {
      this.requiresInspection = requiresInspection;
   }


   public Optional<LabourRole> getTechnicianRole() {
      return Optional.ofNullable( technician );
   }


   public void setTechnicianRole( DomainConfiguration<LabourRole> technicianConfig ) {
      technician = new LabourRole();
      technicianConfig.configure( technician );
   }


   public Optional<LabourRole> getCertifierRole() {
      return Optional.ofNullable( certifier );
   }


   public void setCertifierRole( DomainConfiguration<LabourRole> certifierConfig ) {
      certifier = new LabourRole();
      certifierConfig.configure( certifier );
   }


   public Optional<LabourRole> getInspectorRole() {
      return Optional.ofNullable( inspector );
   }


   public void setInspectorRole( DomainConfiguration<LabourRole> inspectorConfig ) {
      inspector = new LabourRole();
      inspectorConfig.configure( inspector );
   }

}
