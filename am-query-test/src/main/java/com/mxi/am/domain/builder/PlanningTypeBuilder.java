package com.mxi.am.domain.builder;

import java.util.Map;

import com.mxi.am.domain.PlanningType;
import com.mxi.mx.core.key.EqpPlanningTypeKey;
import com.mxi.mx.core.key.EqpPlanningTypeSkillKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.table.eqp.EqpPlanningTypeSkillTable;
import com.mxi.mx.core.table.eqp.EqpPlanningTypeTable;


public class PlanningTypeBuilder {

   public static EqpPlanningTypeKey build( PlanningType aPlanningType ) {

      EqpPlanningTypeTable lEqpPlanningTypeTable = EqpPlanningTypeTable.create();
      lEqpPlanningTypeTable.setNrFactor( aPlanningType.getNrFactor() );
      lEqpPlanningTypeTable.setAssembly( aPlanningType.getAssembly() );
      lEqpPlanningTypeTable.setCode( aPlanningType.getPlanningTypeCode() );
      EqpPlanningTypeKey lEqpPlanningTypeKey = lEqpPlanningTypeTable.insert();

      Map<RefLabourSkillKey, Double> lLabourSkillMap = aPlanningType.getLabourSkills();

      for ( RefLabourSkillKey lSkill : lLabourSkillMap.keySet() ) {
         EqpPlanningTypeSkillKey lEqpPlanningTypeSkillKey =
               new EqpPlanningTypeSkillKey( lEqpPlanningTypeKey, lSkill );

         EqpPlanningTypeSkillTable lEqpPlanningTypeSkillTable =
               EqpPlanningTypeSkillTable.create( lEqpPlanningTypeSkillKey );
         lEqpPlanningTypeSkillTable.setEffortPct( lLabourSkillMap.get( lSkill ) );
         lEqpPlanningTypeSkillTable.insert();
      }

      return lEqpPlanningTypeKey;
   }

}
