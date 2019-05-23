package com.mxi.am.domain.builder;

import static org.apache.commons.lang.ObjectUtils.defaultIfNull;

import java.util.Map;

import com.mxi.am.domain.Step;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.TaskStepKey;
import com.mxi.mx.core.key.TaskStepSkillKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.task.TaskStepSkillDao.ColumnName;
import com.mxi.mx.core.table.task.TaskStepTable;
import com.mxi.mx.persistence.uuid.SequentialUuidGenerator;


/**
 * Domain Builder for Step Entity
 *
 */
public class StepBuilder {

   private static final String STEP_DESCRIPTION = "STEP_DESCRIPTION";
   private static final String TASK_STEP_SKILL_TABLE = "task_step_skill";


   public static void build( Step aStep, int aStepOrder ) {

      TaskStepKey lTaskStepKey =
            TaskStepTable
                  .generatePrimaryKey( getAssociatedJobCardDefinitionOrRequirementDefinition( aStep ) );
      TaskStepTable lTaskStepTable = TaskStepTable.create( lTaskStepKey );
      lTaskStepTable.setApplRangeLdesc( aStep.getApplicabilityRange() );
      lTaskStepTable.setStepLdesc( ( String ) defaultIfNull( aStep.getDescription(),
            STEP_DESCRIPTION ) );
      lTaskStepTable.setStepOrd( aStepOrder );
      lTaskStepKey = lTaskStepTable.insert();
      // Set Skills for each step
      for ( Map.Entry<RefLabourSkillKey, Boolean> lStepSkill : aStep.getStepSkills().entrySet() ) {

         TaskStepSkillKey lTaskStepSkillKey =
               new TaskStepSkillKey( lTaskStepKey, lStepSkill.getKey() );
         DataSetArgument lArgs = new DataSetArgument();
         lArgs.add( lTaskStepSkillKey, ColumnName.TASK_DB_ID.name(), ColumnName.TASK_ID.name(),
               ColumnName.STEP_ID.name(), ColumnName.LABOUR_SKILL_DB_ID.name(),
               ColumnName.LABOUR_SKILL_CD.name() );
         lArgs.add( ColumnName.INSP_BOOL.name(), lStepSkill.getValue() );
         lArgs.add( ColumnName.STEP_SKILL_ID.name(), new SequentialUuidGenerator().newUuid() );
         MxDataAccess.getInstance().executeInsert( TASK_STEP_SKILL_TABLE, lArgs );
      }
   }


   private static TaskTaskKey getAssociatedJobCardDefinitionOrRequirementDefinition( Step aStep ) {

      if ( aStep.getJobCardDefinition() != null & aStep.getRequirementDefinition() != null ) {
         throw new RuntimeException(
               "Step can either be assigned to a Job Card Definition or a Task definition" );
      }
      return aStep.getJobCardDefinition() != null ? aStep.getJobCardDefinition() : aStep
            .getRequirementDefinition();
   }

}
