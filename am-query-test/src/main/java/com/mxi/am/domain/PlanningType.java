package com.mxi.am.domain;

import java.util.HashMap;
import java.util.Map;

import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.RefLabourSkillKey;


public final class PlanningType {

   private Map<RefLabourSkillKey, Double> iLabourSkills = new HashMap<RefLabourSkillKey, Double>();
   private Double iNrFactor;
   private AssemblyKey iAssembly;
   private String iPlanningTypeCode;


   PlanningType() {

   }


   public void addSkill( RefLabourSkillKey aLabourSkill, Double aEffortPct ) {
      iLabourSkills.put( aLabourSkill, aEffortPct );
   }


   public Double getNrFactor() {
      return iNrFactor;
   }


   public Map<RefLabourSkillKey, Double> getLabourSkills() {
      return iLabourSkills;
   }


   public void setNrFactor( Double aNrFactor ) {
      iNrFactor = aNrFactor;
   }


   public AssemblyKey getAssembly() {
      return iAssembly;
   }


   public void setAssembly( AssemblyKey aAssembly ) {
      iAssembly = aAssembly;
   }


   public String getPlanningTypeCode() {
      return iPlanningTypeCode;
   }


   public void setPlanningTypeCode( String aPlanningTypeCode ) {
      iPlanningTypeCode = aPlanningTypeCode;
   }
}
