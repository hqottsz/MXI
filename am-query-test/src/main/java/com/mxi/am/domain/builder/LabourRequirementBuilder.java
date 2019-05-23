
package com.mxi.am.domain.builder;

import java.math.BigDecimal;

import com.mxi.mx.common.exception.MxRuntimeException;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.TaskLabourListKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.task.TaskLabourList;


/**
 * Builds a <code>task_sched_rule</code> object
 */
public class LabourRequirementBuilder implements DomainBuilder<TaskLabourListKey> {

   private BigDecimal iNumberOfPeopleRequired;
   private BigDecimal iWorkSchedHrs;
   private boolean iCertRequired = false;
   private BigDecimal iCertSchedHrs;
   private boolean iInspRequired = false;
   private BigDecimal iInspSchedHrs;

   private TaskTaskKey iTaskRevision;
   private RefLabourSkillKey iLabourSkill;


   /**
    * Creates a new {@linkplain LabourRequirementBuilder} object.
    *
    * @param aTaskRevision
    *           the task revision
    * @param aLabourSkill
    *           the paramter data type key
    */
   public LabourRequirementBuilder(TaskTaskKey aTaskRevision, RefLabourSkillKey aLabourSkill) {
      iTaskRevision = aTaskRevision;
      iLabourSkill = aLabourSkill;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public TaskLabourListKey build() {
      if ( iLabourSkill == null ) {
         throw new MxRuntimeException( "Parameter date type may not be null" );
      }

      if ( iTaskRevision == null ) {
         throw new MxRuntimeException( "Task revision may not be null" );
      }

      TaskLabourList lTable =
            TaskLabourList.create( new TaskLabourListKey( iTaskRevision, iLabourSkill ) );

      if ( iNumberOfPeopleRequired != null ) {
         lTable.setManPwrCt( iNumberOfPeopleRequired.intValue() );
      }

      if ( iWorkSchedHrs != null ) {
         lTable.setWorkPerfHr( iWorkSchedHrs.doubleValue() );
      }

      lTable.setCert( iCertRequired );

      if ( iCertSchedHrs != null ) {
         lTable.setCertHr( iCertSchedHrs.doubleValue() );
      }

      lTable.setInsp( iInspRequired );

      if ( iInspSchedHrs != null ) {
         lTable.setInspHr( iInspSchedHrs.doubleValue() );
      }

      return lTable.insert();
   }


   /**
    * Sets the Number Of People Required.
    *
    * @param aNumberOfPeopleRequired
    *
    * @return the builder
    */
   public LabourRequirementBuilder
         withNumberOfPeopleRequired( BigDecimal aNumberOfPeopleRequired ) {
      iNumberOfPeopleRequired = aNumberOfPeopleRequired;

      return this;
   }


   /**
    * Sets the Work Performance Schedule Hours.
    *
    * @param aWorkSchedHrs
    *
    * @return the builder
    */
   public LabourRequirementBuilder withWorkSchedHrs( BigDecimal aWorkSchedHrs ) {
      iWorkSchedHrs = aWorkSchedHrs;

      return this;
   }


   /**
    * Sets the Certification Required.
    *
    * @param aCertRequired
    *
    * @return the builder
    */
   public LabourRequirementBuilder withCertRequired( boolean aCertRequired ) {
      iCertRequired = aCertRequired;

      return this;
   }


   /**
    * Sets the Certification Schedule Hours.
    *
    * @param aCertSchedHrs
    *
    * @return the builder
    */
   public LabourRequirementBuilder withCertSchedHrs( BigDecimal aCertSchedHrs ) {
      iCertSchedHrs = aCertSchedHrs;

      return this;
   }


   /**
    * Sets the Independent Inspection Required.
    *
    * @param aInspRequired
    *
    * @return the builder
    */
   public LabourRequirementBuilder withInspRequired( boolean aInspRequired ) {
      iInspRequired = aInspRequired;

      return this;
   }


   /**
    * Sets the Independent Inspection Schedule Hours
    *
    * @param aInspSchedHrs
    *
    * @return the builder
    */
   public LabourRequirementBuilder withInspSchedHrs( BigDecimal aInspSchedHrs ) {
      iInspSchedHrs = aInspSchedHrs;

      return this;
   }

}
