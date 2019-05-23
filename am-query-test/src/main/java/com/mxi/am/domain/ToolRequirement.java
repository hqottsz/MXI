package com.mxi.am.domain;

import java.math.BigDecimal;

import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.TaskTaskKey;


/**
 * Domain class for Tool Requirement
 *
 */
public class ToolRequirement {

   private PartNoKey iTool;
   private BigDecimal iScheduledHours;
   private TaskTaskKey iRequirementDefinition;


   public ToolRequirement(PartNoKey aTool, BigDecimal aScheduledHours) {
      iTool = aTool;
      iScheduledHours = aScheduledHours;
   }


   public PartNoKey getTool() {
      return iTool;
   }


   public void setTool( PartNoKey aTool ) {
      iTool = aTool;
   }


   public TaskTaskKey getRequirementDefinition() {
      return iRequirementDefinition;
   }


   public void setRequirementDefinition( TaskTaskKey aRequirementDefinition ) {
      iRequirementDefinition = aRequirementDefinition;
   }


   public BigDecimal getScheduledHours() {
      return iScheduledHours;
   }


   public void setScheduledHours( BigDecimal aScheduledHours ) {
      iScheduledHours = aScheduledHours;
   }

}
