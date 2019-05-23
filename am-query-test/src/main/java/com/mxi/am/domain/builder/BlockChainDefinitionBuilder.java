package com.mxi.am.domain.builder;

import static org.apache.commons.lang.ObjectUtils.defaultIfNull;

import java.util.HashMap;
import java.util.Map;

import com.mxi.am.domain.BlockChainDefinition;
import com.mxi.am.domain.BlockChainDefinition.BlockInfo;
import com.mxi.am.domain.BlockChainDefinition.RequirementInfo;
import com.mxi.am.domain.BlockChainDefinition.ScheduleFromOption;
import com.mxi.am.domain.OneTimeSchedulingRule;
import com.mxi.am.domain.RecurringSchedulingRule;
import com.mxi.mx.core.key.RefTaskDepActionKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskFleetApprovalKey;
import com.mxi.mx.core.key.TaskTaskDepKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.task.TaskBlockReqMapTable;
import com.mxi.mx.core.table.task.TaskFleetApprovalTable;
import com.mxi.mx.core.table.task.TaskTaskDepTable;
import com.mxi.mx.core.table.task.TaskTaskTable;


public class BlockChainDefinitionBuilder {

   /**
    *
    * Builds a block chain definition based on the configuration attributes in the provided
    * aBlockChainDefinition.
    *
    * Note: there is not natural identifier for a block chain definition, a block chain definition
    * is identified by searching block definitions that all have the same block chain definition
    * name. Thus, the collection of block definition keys being returned.
    *
    * @param aBlockChainDefinition
    *           configuration attributes of the block chain
    * @return a map of block number to block definition key (of the block definitions that comprise
    *         the block chain definition)
    */
   public static Map<Integer, TaskTaskKey> build( BlockChainDefinition aBlockChainDefinition ) {

      Map<Integer, TaskTaskKey> lBlockKeys = new HashMap<Integer, TaskTaskKey>();

      boolean lIsRecurring =
            ( boolean ) defaultIfNull( aBlockChainDefinition.isRecurring(), false );
      boolean lIsOnCondition =
            ( boolean ) defaultIfNull( aBlockChainDefinition.isOnCondition(), false );
      boolean lIsApprovedForFleet =
            ( boolean ) defaultIfNull( aBlockChainDefinition.isApprovedForFleet(), true );

      for ( BlockInfo lBlock : aBlockChainDefinition.getBlocks() ) {

         if ( lBlock.getNumber() == null ) {
            throw new RuntimeException( "All blocks in block chain must have a block number." );
         }
         if ( lBlockKeys.containsKey( lBlock.getNumber() ) ) {
            throw new RuntimeException(
                  "All blocks in block chain must have a unique block number." );
         }

         TaskRevisionBuilder lBuilder = new TaskRevisionBuilder();

         // Block chain name is the key to identifying the block chain definition.
         lBuilder.withBlockChainDesc( aBlockChainDefinition.getName() );

         lBuilder.withTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK );
         lBuilder.withTaskCode( lBlock.getCode() );

         if ( lBlock.getPreviousRevision() != null ) {
            TaskDefnKey lTaskDefnKey =
                  TaskTaskTable.findByPrimaryKey( lBlock.getPreviousRevision() ).getTaskDefn();
            lBuilder.withTaskDefn( lTaskDefnKey );
         }

         lBuilder.withStatus( aBlockChainDefinition.getStatus() );
         lBuilder.withConfigSlot( aBlockChainDefinition.getConfigurationSlot() );
         lBuilder.withRevisionNumber( aBlockChainDefinition.getRevisionNumber() );
         lBuilder.setOnCondition( lIsOnCondition );

         // Default to true in order to allow for zipping.
         // Refer to TaskDefnService.getUniqueBoolForBlockOrReq() for a definition of unique.
         lBuilder.setUnique( ( boolean ) defaultIfNull( aBlockChainDefinition.isUnique(), true ) );

         if ( lIsRecurring ) {
            lBuilder.isRecurring();
         }

         ScheduleFromOption lScheduleFromOption =
               ( ScheduleFromOption ) defaultIfNull( aBlockChainDefinition.getScheduleFromOption(),
                     ScheduleFromOption.MANUFACTURED_DATE );

         switch ( lScheduleFromOption ) {
            case EFFECTIVE_DATE:
               lBuilder.isScheduledFromEffectiveDate( aBlockChainDefinition.getEffectiveDate() );
               break;
            case RECEIVED_DATE:
               lBuilder.isScheduledFromRecievedDate();
               break;
            case MANUFACTURED_DATE:
               lBuilder.isScheduledFromManufacturedDate();
               break;
         }

         TaskTaskKey lBlockKey = lBuilder.build();

         TaskTaskTable lTaskTask = TaskTaskTable.findByPrimaryKey( lBlockKey );

         // The deprecated TaskRevisionBuilder does not support setting the following.
         // So we will update the table row.
         lTaskTask.setBlockOrd( lBlock.getNumber() );
         lTaskTask.setTaskName( lBlock.getName() );
         if ( aBlockChainDefinition.getMinimumForecastRange() != null ) {
            lTaskTask.setForecaseRangeQt(
                  aBlockChainDefinition.getMinimumForecastRange().doubleValue() );
         }
         lTaskTask.update();

         // Add the scheduling rules.
         for ( OneTimeSchedulingRule lSchedulingRule : aBlockChainDefinition
               .getOneTimeSchedulingRules() ) {
            new OneTimeSchedulingRuleBuilder().build( lSchedulingRule, lBlockKey );
         }
         for ( RecurringSchedulingRule lSchedulingRule : aBlockChainDefinition
               .getRecurringSchedulingRules() ) {
            new RecurringSchedulingRuleBuilder().build( lSchedulingRule, lBlockKey );
         }

         if ( lIsApprovedForFleet ) {
            // Create/update the task_fleet_approval record for this block.
            TaskDefnKey lTaskDefn = lTaskTask.getTaskDefn();

            // If there is no previous revision then create a new record in task_fleet_approval,
            // otherwise update the record.
            if ( lBlock.getPreviousRevision() == null ) {
               TaskFleetApprovalTable lTaskFleetApproval =
                     TaskFleetApprovalTable.create( new TaskFleetApprovalKey( lTaskDefn ) );
               lTaskFleetApproval.setTaskRevision( lBlockKey );
               lTaskFleetApproval.insert();
            } else {
               TaskFleetApprovalTable lTaskFleetApproval = TaskFleetApprovalTable
                     .findByPrimaryKey( new TaskFleetApprovalKey( lTaskDefn ) );
               lTaskFleetApproval.setTaskRevision( lBlockKey );
               lTaskFleetApproval.update();
            }
         }

         lBlockKeys.put( lBlock.getNumber(), lBlockKey );
      }

      // Add the requirement definitions to the block definition.
      for ( RequirementInfo lReqInfo : aBlockChainDefinition.getRequirements() ) {
         Integer lBlockNumberToCheck = lReqInfo.getStartingBlock();
         while ( lBlockKeys.containsKey( lBlockNumberToCheck ) ) {

            TaskTaskKey lBlock = lBlockKeys.get( lBlockNumberToCheck );
            TaskDefnKey lReq = TaskTaskTable.findByPrimaryKey( lReqInfo.getRequirementDefinition() )
                  .getTaskDefn();

            TaskBlockReqMapTable lBlockReqTable = TaskBlockReqMapTable.create( lBlock, lReq );
            lBlockReqTable.setIntervalQt( lReqInfo.getInterval() );
            lBlockReqTable.setStartBlockOrd( lReqInfo.getStartingBlock() );
            lBlockReqTable.insert();

            lBlockNumberToCheck = lBlockNumberToCheck + lReqInfo.getInterval();
         }
      }

      // Add the "create" dependencies for each of the blocks to their next block.

      TaskTaskKey lPreviousBlockKey = null;
      for ( TaskTaskKey lBlockKey : lBlockKeys.values() ) {
         if ( lPreviousBlockKey == null ) {
            lPreviousBlockKey = lBlockKey;
            continue;
         }
         TaskDefnKey lBlockDefnKey = TaskTaskTable.findByPrimaryKey( lBlockKey ).getTaskDefn();

         TaskTaskDepTable lTaskDep =
               TaskTaskDepTable.create( TaskTaskDepTable.generatePrimaryKey( lPreviousBlockKey ) );
         lTaskDep.setDepTaskDefn( lBlockDefnKey );
         lTaskDep.setTaskDepAction( RefTaskDepActionKey.CRT );
         lTaskDep.insert();

         lPreviousBlockKey = lBlockKey;
      }

      // if the block chain is recurring, add dependency between the last block in the chain and the
      // first block in the chain.
      if ( lIsRecurring ) {
         int lBlockCount = lBlockKeys.values().size();

         TaskTaskTable lFirstBlock = TaskTaskTable.findByPrimaryKey( lBlockKeys.get( 1 ) );

         TaskTaskDepKey lLastBlockDepKey =
               TaskTaskDepTable.generatePrimaryKey( lBlockKeys.get( lBlockCount ) );

         TaskTaskDepTable lTaskDep = TaskTaskDepTable.create( lLastBlockDepKey );
         lTaskDep.setDepTaskDefn( lFirstBlock.getTaskDefn() );
         lTaskDep.setTaskDepAction( RefTaskDepActionKey.CRT );
         lTaskDep.insert();
      }

      return lBlockKeys;
   }

}
