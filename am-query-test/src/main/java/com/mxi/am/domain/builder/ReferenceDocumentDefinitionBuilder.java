package com.mxi.am.domain.builder;

import static com.mxi.mx.core.key.RefTaskDefinitionStatusKey.ACTV;
import static org.apache.commons.lang.ObjectUtils.defaultIfNull;

import java.util.Map;
import java.util.Set;

import com.mxi.am.domain.ReferenceDocumentDefinition;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.RefTaskDepActionKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskFleetApprovalKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.task.TaskFleetApprovalTable;
import com.mxi.mx.core.table.task.TaskTaskDepTable;
import com.mxi.mx.core.table.task.TaskTaskTable;


public class ReferenceDocumentDefinitionBuilder {

   public static TaskTaskKey build( ReferenceDocumentDefinition aReferenceDocumentDefinition ) {
      TaskRevisionBuilder lTaskRevisionBuilder = new TaskRevisionBuilder();
      lTaskRevisionBuilder.withTaskCode( aReferenceDocumentDefinition.getCode() );
      lTaskRevisionBuilder.withTaskClass( TaskRevisionBuilder.TASK_CLASS_REF );
      lTaskRevisionBuilder.withRevisionNumber( aReferenceDocumentDefinition.getRevisionNumber() );
      lTaskRevisionBuilder.withConfigSlot( aReferenceDocumentDefinition.getConfigurationSlot() );
      lTaskRevisionBuilder.withStatus( ( RefTaskDefinitionStatusKey ) defaultIfNull(
            aReferenceDocumentDefinition.getStatus(), ACTV ) );

      lTaskRevisionBuilder.setUnique(
            ( boolean ) defaultIfNull( aReferenceDocumentDefinition.isUnique(), false ) );
      lTaskRevisionBuilder.setOnCondition(
            ( boolean ) defaultIfNull( aReferenceDocumentDefinition.isOnCondition(), false ) );

      switch ( aReferenceDocumentDefinition.getScheduleFromOption() ) {
         case EFFECTIVE_DATE:
            lTaskRevisionBuilder
                  .isScheduledFromEffectiveDate( aReferenceDocumentDefinition.getEffectiveDate() );
            break;
         case MANUFACTURED_DATE:
            lTaskRevisionBuilder.isScheduledFromManufacturedDate();
            break;
         case RECEIVED_DATE:
            lTaskRevisionBuilder.isScheduledFromRecievedDate();
            break;
      }

      TaskTaskKey lPriorRevision = aReferenceDocumentDefinition.getPreviousRevision();
      if ( lPriorRevision != null ) {
         TaskDefnKey lTaskDefnKey = TaskTaskTable.findByPrimaryKey( lPriorRevision ).getTaskDefn();
         lTaskRevisionBuilder.withTaskDefn( lTaskDefnKey );
      }

      TaskTaskKey lReqDefnKey = lTaskRevisionBuilder.build();

      // Assign the link to tasks.
      Map<RefTaskDepActionKey, Set<TaskTaskKey>> lLinkToTasksDefnMap =
            aReferenceDocumentDefinition.getLinkToTasksDefinitionMap();
      for ( RefTaskDepActionKey lAction : lLinkToTasksDefnMap.keySet() ) {
         for ( TaskTaskKey lLinkToTasksDefn : lLinkToTasksDefnMap.get( lAction ) ) {
            TaskDefnKey lLinkToTasksDefnKey =
                  TaskTaskTable.findByPrimaryKey( lLinkToTasksDefn ).getTaskDefn();
            TaskTaskDepTable lTaskDepTable =
                  TaskTaskDepTable.create( TaskTaskDepTable.generatePrimaryKey( lReqDefnKey ) );
            lTaskDepTable.setTaskDepAction( lAction );
            lTaskDepTable.setDepTaskDefn( lLinkToTasksDefnKey );
            lTaskDepTable.insert();
         }
      }

      TaskDefnKey lReqTaskDefnKey = TaskTaskTable.findByPrimaryKey( lReqDefnKey ).getTaskDefn();

      // Check if it is approved for the fleet.
      boolean lIsFleetApproved =
            ( boolean ) defaultIfNull( aReferenceDocumentDefinition.getFleetApproval(), true );
      if ( lIsFleetApproved ) {
         TaskFleetApprovalKey lTaskFleetApprovalKey = new TaskFleetApprovalKey( lReqTaskDefnKey );
         if ( !lTaskFleetApprovalKey.isValid() ) {
            TaskFleetApprovalTable lTaskFleetApproval =
                  TaskFleetApprovalTable.create( lTaskFleetApprovalKey );
            lTaskFleetApproval.setTaskRevision( lReqDefnKey );
            lTaskFleetApproval.insert();
         }
      }

      return lReqDefnKey;
   }
}
