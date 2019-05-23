package com.mxi.am.domain;

import java.math.BigDecimal;

import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.RefLabourSkillKey;


/**
 * DOCUMENT_ME
 *
 */
public final class LabourRequirement {

   private RefLabourSkillKey iLabourSkill;
   private BigDecimal iNumberOfRequiredPeople;
   private BigDecimal iWorkPerformedScheduleHours;
   private BigDecimal iCertSchedHrs;
   private BigDecimal iInspSchedHrs;
   private boolean iCertRequired = false;
   private boolean iInspRequired = false;
   private HumanResourceKey iTechnician;


   public LabourRequirement(RefLabourSkillKey aLabourSkill,
         BigDecimal aWorkPerformedScheduleHours) {
      iLabourSkill = aLabourSkill;
      iWorkPerformedScheduleHours = aWorkPerformedScheduleHours;
   }


   public LabourRequirement(RefLabourSkillKey aLabourSkill, BigDecimal aWorkPerfSchedHrs,
         BigDecimal aCertSchedHrs, BigDecimal aInspSchedHrs) {
      iLabourSkill = aLabourSkill;
      iWorkPerformedScheduleHours = aWorkPerfSchedHrs;

      iCertSchedHrs = aCertSchedHrs;

      if ( aCertSchedHrs != null ) {
         iCertRequired = true;
      }

      iInspSchedHrs = aInspSchedHrs;

      if ( aInspSchedHrs != null ) {
         iInspRequired = true;
      }
   }


   public LabourRequirement(RefLabourSkillKey aLabourSkill, BigDecimal aWorkPerfSchedHrs,
         BigDecimal aCertSchedHrs, BigDecimal aInspSchedHrs, BigDecimal aNumberOfRequiredPeople) {
      iLabourSkill = aLabourSkill;
      iWorkPerformedScheduleHours = aWorkPerfSchedHrs;

      iCertSchedHrs = aCertSchedHrs;

      if ( aCertSchedHrs != null ) {
         iCertRequired = true;
      }

      iInspSchedHrs = aInspSchedHrs;

      if ( aInspSchedHrs != null ) {
         iInspRequired = true;
      }

      iNumberOfRequiredPeople = aNumberOfRequiredPeople;
   }


   /**
    * Returns the value of the certRequired property.
    *
    * @return the value of the certRequired property
    */
   public boolean isCertRequired() {
      return iCertRequired;
   }


   /**
    * Returns the value of the inspRequired property.
    *
    * @return the value of the inspRequired property
    */
   public boolean isInspRequired() {
      return iInspRequired;
   }


   public RefLabourSkillKey getLabourSkill() {
      return iLabourSkill;
   }


   public BigDecimal getWorkPerformedScheduleHours() {
      return iWorkPerformedScheduleHours;
   }


   public BigDecimal getCertSchedHrs() {
      return iCertSchedHrs;
   }


   public BigDecimal getInspSchedHrs() {
      return iInspSchedHrs;
   }


   public void setNumberOfPeopleRequired( BigDecimal aNumberOfRequiredPeople ) {
      iNumberOfRequiredPeople = aNumberOfRequiredPeople;
   }


   public BigDecimal getNumberOfPeopleRequired() {
      return iNumberOfRequiredPeople;
   }


   /**
    * Returns the value of the technician property.
    *
    * @return the value of the technician property
    */
   public HumanResourceKey getTechnician() {
      return iTechnician;
   }


   /**
    * Sets a new value for the technician property.
    *
    * @param aTechnician
    *           the new value for the technician property
    */
   public void setTechnician( HumanResourceKey aTechnician ) {
      iTechnician = aTechnician;
   }
}
