package com.mxi.am.domain;

import java.util.HashMap;
import java.util.Map;

import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.TaskTaskKey;


/**
 * Domain entity for Step
 *
 */
public class Step {

   private String iDescription;
   private String iApplicabilityRange;
   private Map<RefLabourSkillKey, Boolean> iStepSkills = new HashMap<>();
   private TaskTaskKey iJobCardDefinition;
   private TaskTaskKey iRequirementDefinition;


   public String getApplicabilityRange() {
      return iApplicabilityRange;
   }


   public void setApplicabilityRange( String aApplicabilityRange ) {
      iApplicabilityRange = aApplicabilityRange;
   }


   public String getDescription() {
      return iDescription;
   }


   public void setDescription( String aDescription ) {
      iDescription = aDescription;
   }


   public Map<RefLabourSkillKey, Boolean> getStepSkills() {
      return iStepSkills;
   }


   public void addStepSkill( RefLabourSkillKey aStepSkill, Boolean aIndepentendInspectionRequired ) {
      iStepSkills.put( aStepSkill, aIndepentendInspectionRequired );
   }


   public TaskTaskKey getRequirementDefinition() {
      return iRequirementDefinition;
   }


   public void setRequirementDefinition( TaskTaskKey aRequirementDefinition ) {
      iRequirementDefinition = aRequirementDefinition;
   }


   public TaskTaskKey getJobCardDefinition() {
      return iJobCardDefinition;
   }


   public void setJobCardDefinition( TaskTaskKey aJobCardDefinition ) {
      iJobCardDefinition = aJobCardDefinition;
   }

}
