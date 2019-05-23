package com.mxi.am.domain.builder;

import static com.mxi.mx.core.key.RefTaskDefinitionStatusKey.ACTV;
import static org.apache.commons.lang.ObjectUtils.defaultIfNull;

import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.JobCardDefinition;
import com.mxi.am.domain.LabourRequirement;
import com.mxi.am.domain.Step;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskFleetApprovalKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.task.TaskDefnTable;
import com.mxi.mx.core.table.task.TaskFleetApprovalTable;
import com.mxi.mx.core.table.task.TaskTaskTable;


public class JobCardDefinitionBuilder {

   public static TaskTaskKey build( JobCardDefinition aJicDefn ) {
      TaskRevisionBuilder lBuilder = new TaskRevisionBuilder();
      lBuilder.withTaskClass( RefTaskClassKey.JIC );

      lBuilder.withConfigSlot( aJicDefn.getConfigurationSlot() );
      lBuilder.withTaskCode( aJicDefn.getCode() );
      lBuilder.withRevisionNumber( aJicDefn.getRevisionNumber() );

      lBuilder.setOnCondition( ( boolean ) defaultIfNull( aJicDefn.isOnCondition(), false ) );

      lBuilder.withStatus(
            ( RefTaskDefinitionStatusKey ) defaultIfNull( aJicDefn.getStatus(), ACTV ) );

      TaskTaskKey lPrevRevision = aJicDefn.getPreviousRevision();
      if ( lPrevRevision != null ) {
         TaskDefnKey lTaskDefnKey = TaskTaskTable.findByPrimaryKey( lPrevRevision ).getTaskDefn();
         lBuilder.withTaskDefn( lTaskDefnKey );
      }

      if ( aJicDefn.isWorkScopeEnabled().orElse( false ) ) {
         lBuilder.isWorkscoped();
      }

      TaskTaskKey lJicDefnKey = lBuilder.build();

      // Get the task_defn row.
      TaskDefnKey lTaskDefnKey = TaskTaskTable.findByPrimaryKey( lJicDefnKey ).getTaskDefn();
      TaskDefnTable lTaskDefnTable = TaskDefnTable.findByPrimaryKey( lTaskDefnKey );
      lTaskDefnTable.setPreventManualInitializationBool(
            aJicDefn.isPreventManualInitialization().orElse( false ) );
      lTaskDefnTable.update();

      // Check if it is approved for the fleet.
      boolean lIsFleetApproved = ( boolean ) defaultIfNull( aJicDefn.getFleetApproval(), true );

      if ( lIsFleetApproved ) {
         TaskFleetApprovalKey lTaskFleetApprovalKey = new TaskFleetApprovalKey( lTaskDefnKey );
         TaskFleetApprovalTable lApprovalTable =
               TaskFleetApprovalTable.findByPrimaryKey( lTaskFleetApprovalKey );
         if ( lApprovalTable.exists() ) {
            lApprovalTable.setTaskRevision( lJicDefnKey );
            lApprovalTable.update();
         } else {
            lApprovalTable = TaskFleetApprovalTable.create( lTaskFleetApprovalKey );
            lApprovalTable.setTaskRevision( lJicDefnKey );
            lApprovalTable.insert();
         }
      }

      // Add the labour requirements.
      for ( LabourRequirement lLabourRequirement : aJicDefn.getLaborRequirements() ) {
         new LabourRequirementBuilder( lJicDefnKey, lLabourRequirement.getLabourSkill() )
               .withWorkSchedHrs( lLabourRequirement.getWorkPerformedScheduleHours() ).build();
      }

      // Set Steps
      int lStepOrder = 1;
      for ( DomainConfiguration<Step> lStepConfiguration : aJicDefn.getSteps() ) {
         Step lStep = new Step();
         lStepConfiguration.configure( lStep );
         if ( lStep.getJobCardDefinition() != null ) {
            throw new RuntimeException(
                  "Job Card Definition for a Step can only be set from the within the Job Card Definition Builder" );
         }
         lStep.setJobCardDefinition( lJicDefnKey );
         StepBuilder.build( lStep, lStepOrder++ );
      }

      return lJicDefnKey;
   }
}
