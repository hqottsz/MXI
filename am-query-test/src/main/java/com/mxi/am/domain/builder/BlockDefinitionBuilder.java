package com.mxi.am.domain.builder;

import static com.mxi.mx.core.key.RefTaskDefinitionStatusKey.ACTV;
import static org.apache.commons.lang.ObjectUtils.defaultIfNull;

import com.mxi.am.domain.BlockDefinition;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.RefWorkTypeKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskFleetApprovalKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.task.TaskFleetApprovalTable;
import com.mxi.mx.core.table.task.TaskTaskTable;
import com.mxi.mx.core.table.task.TaskWorkTypeTable;


public class BlockDefinitionBuilder {

   public static final RefTaskClassKey BLOCK = TaskRevisionBuilder.TASK_CLASS_BLOCK;


   public static TaskTaskKey build( BlockDefinition aBlockDefinition ) {
      TaskRevisionBuilder lTaskRevisionBuilder = new TaskRevisionBuilder();
      lTaskRevisionBuilder.withTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK );
      lTaskRevisionBuilder.withRevisionNumber( aBlockDefinition.getRevisionNumber() );
      if ( aBlockDefinition.getConfigurationSlot() != null ) {
         lTaskRevisionBuilder.withConfigSlot( aBlockDefinition.getConfigurationSlot() );
      }

      lTaskRevisionBuilder.withConfigSlot( aBlockDefinition.getConfigurationSlot() );
      lTaskRevisionBuilder.withStatus(
            ( RefTaskDefinitionStatusKey ) defaultIfNull( aBlockDefinition.getStatus(), ACTV ) );
      lTaskRevisionBuilder
            .setOnCondition( ( boolean ) defaultIfNull( aBlockDefinition.isOnCondition(), false ) );

      // Default to true in order to allow for zipping.
      // Refer to TaskDefnService.getUniqueBoolForBlockOrReq() for a definition of unique.
      if ( ( boolean ) defaultIfNull( aBlockDefinition.isUnique(), true ) ) {
         lTaskRevisionBuilder.isUnique();
      }

      if ( ( boolean ) defaultIfNull( aBlockDefinition.isRecurring(), false ) ) {
         lTaskRevisionBuilder.isRecurring();
      }

      for ( TaskTaskKey lReqKey : aBlockDefinition.getRequirementDefinitions() ) {
         lTaskRevisionBuilder
               .mapToRequirement( TaskTaskTable.findByPrimaryKey( lReqKey ).getTaskDefn() );
      }

      TaskTaskKey lPriorRevision = aBlockDefinition.getPreviousRevision();
      if ( lPriorRevision != null ) {
         TaskDefnKey lTaskDefnKey = TaskTaskTable.findByPrimaryKey( lPriorRevision ).getTaskDefn();
         lTaskRevisionBuilder.withTaskDefn( lTaskDefnKey );
      }

      TaskTaskKey lTaskTaskKey = lTaskRevisionBuilder.build();

      // Get the task_defn row.
      TaskDefnKey lTaskDefnKey = TaskTaskTable.findByPrimaryKey( lTaskTaskKey ).getTaskDefn();

      for ( RefWorkTypeKey lWorkType : aBlockDefinition.getWorkTypes() ) {
         TaskWorkTypeTable.create( lTaskTaskKey, lWorkType ).insert();
      }

      // Check if it is approved for the fleet.
      boolean lIsFleetApproved =
            ( boolean ) defaultIfNull( aBlockDefinition.getFleetApproval(), true );

      if ( lIsFleetApproved ) {
         TaskFleetApprovalKey lTaskFleetApprovalKey = new TaskFleetApprovalKey( lTaskDefnKey );
         TaskFleetApprovalTable lApprovalTable =
               TaskFleetApprovalTable.findByPrimaryKey( lTaskFleetApprovalKey );
         if ( lApprovalTable.exists() ) {
            lApprovalTable.setTaskRevision( lTaskTaskKey );
            lApprovalTable.update();
         } else {
            lApprovalTable = TaskFleetApprovalTable.create( lTaskFleetApprovalKey );
            lApprovalTable.setTaskRevision( lTaskTaskKey );
            lApprovalTable.insert();
         }
      }
      return lTaskTaskKey;
   }

}
