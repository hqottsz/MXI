package com.mxi.am.domain.builder;

import static com.mxi.mx.core.key.RefTaskDefinitionStatusKey.ACTV;
import static org.apache.commons.lang.ObjectUtils.defaultIfNull;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import com.mxi.am.domain.Advisory;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.LabourRequirement;
import com.mxi.am.domain.OneTimeSchedulingRule;
import com.mxi.am.domain.PartRequirementDefinition;
import com.mxi.am.domain.PartTransformation;
import com.mxi.am.domain.RecurringSchedulingRule;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.domain.RequirementDefinition.ScheduleFromOption;
import com.mxi.am.domain.SchedulingRule;
import com.mxi.am.domain.Step;
import com.mxi.am.domain.ToolRequirement;
import com.mxi.am.domain.WeightAndBalanceImpact;
import com.mxi.am.domain.Zone;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.utils.StringUtils;
import com.mxi.mx.common.utils.TimeUnit;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.IetmDefinitionKey;
import com.mxi.mx.core.key.IetmTopicKey;
import com.mxi.mx.core.key.PartTransformationKey;
import com.mxi.mx.core.key.RefImpactKey;
import com.mxi.mx.core.key.RefReschedFromKey;
import com.mxi.mx.core.key.RefTaskAdvisoryTypeKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.RefTaskDepActionKey;
import com.mxi.mx.core.key.RefTaskMustRemoveKey;
import com.mxi.mx.core.key.RefWorkTypeKey;
import com.mxi.mx.core.key.TaskAdvisoryKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskFleetApprovalKey;
import com.mxi.mx.core.key.TaskJicReqMapKey;
import com.mxi.mx.core.key.TaskParmDataKey;
import com.mxi.mx.core.key.TaskPlanningTypeSkillKey;
import com.mxi.mx.core.key.TaskTaskFlagsKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.key.ZoneKey;
import com.mxi.mx.core.table.task.TaskAdvisoryTable;
import com.mxi.mx.core.table.task.TaskDefnTable;
import com.mxi.mx.core.table.task.TaskFleetApprovalTable;
import com.mxi.mx.core.table.task.TaskJicReqMapTable;
import com.mxi.mx.core.table.task.TaskPanelTable;
import com.mxi.mx.core.table.task.TaskParmData;
import com.mxi.mx.core.table.task.TaskPartTransform;
import com.mxi.mx.core.table.task.TaskPlanningTypeSkillTable;
import com.mxi.mx.core.table.task.TaskRepRefDao;
import com.mxi.mx.core.table.task.TaskRepRefTableRow;
import com.mxi.mx.core.table.task.TaskTaskDepTable;
import com.mxi.mx.core.table.task.TaskTaskFlagsTable;
import com.mxi.mx.core.table.task.TaskTaskIetm;
import com.mxi.mx.core.table.task.TaskTaskTable;
import com.mxi.mx.core.table.task.TaskWorkTypeTable;
import com.mxi.mx.core.table.task.TaskZoneTable;
import com.mxi.mx.core.table.utl.UtlRole;


public class RequirementDefinitionBuilder {

   public static TaskTaskKey build( RequirementDefinition aRequirementDefinition ) {
      TaskRevisionBuilder lBuilder = new TaskRevisionBuilder();
      lBuilder.withTaskCode( aRequirementDefinition.getCode() );

      lBuilder.withRevisionNumber( aRequirementDefinition.getRevisionNumber() );
      if ( aRequirementDefinition.getConfigurationSlot() != null ) {
         lBuilder.withConfigSlot( aRequirementDefinition.getConfigurationSlot() );
      }
      lBuilder.withApplicabilityRange( aRequirementDefinition.getApplicabilityRange() );
      lBuilder.withApplicabilityRule( aRequirementDefinition.getApplicabilityRule() );
      lBuilder.withPart( aRequirementDefinition.getPartNos() );
      lBuilder.withPlanningType( aRequirementDefinition.getPlanningType() );
      lBuilder.withStatus( ( RefTaskDefinitionStatusKey ) defaultIfNull(
            aRequirementDefinition.getStatus(), ACTV ) );
      lBuilder.setOnCondition(
            ( boolean ) defaultIfNull( aRequirementDefinition.isOnCondition(), false ) );
      lBuilder.setMustRemove( ( RefTaskMustRemoveKey ) defaultIfNull(
            aRequirementDefinition.getMustRemove(), RefTaskMustRemoveKey.NA ) );
      lBuilder.forOrganization( aRequirementDefinition.getOrganization() );
      RefTaskClassKey lTaskClass =
            ( RefTaskClassKey ) defaultIfNull( aRequirementDefinition.getTaskClass(),
                  RefTaskClassKey.REQ );
      lBuilder.withTaskClass( lTaskClass );
      lBuilder.withTaskSubclass( aRequirementDefinition.getTaskSubClass() );

      lBuilder.setIsSchedulingRuleUsedWhenCreatedFromAnotherTask( aRequirementDefinition
            .isSchedulingRuleUsedWhenCreatedFromAnotherTask().orElse( false ) );

      lBuilder.withRescheduleFrom(
            aRequirementDefinition.getRescheduleFromOption().orElse( RefReschedFromKey.EXECUTE ) );

      // Default to true in order to allow for zipping.
      // Refer to TaskDefnService.getUniqueBoolForBlockOrReq() for a definition of unique.
      lBuilder.setUnique( ( boolean ) defaultIfNull( aRequirementDefinition.isUnique(), true ) );

      if ( ( boolean ) defaultIfNull( aRequirementDefinition.isCreateOnInstall(), false ) ) {
         lBuilder.isCreateOnAnyInst();
      }

      if ( ( boolean ) defaultIfNull( aRequirementDefinition.isCreateOnRemove(), false ) ) {
         lBuilder.isCreateOnRemove();
      }

      if ( ( boolean ) defaultIfNull( aRequirementDefinition.isCancelOnAnyInstall(), false ) ) {
         lBuilder.isCancelOnInstallOnAnyComponent();
      }

      if ( ( boolean ) defaultIfNull( aRequirementDefinition.isCancelOnInstallAircraft(),
            false ) ) {
         lBuilder.isCancelOnInstallOnAircraft();
      }

      if ( ( boolean ) defaultIfNull( aRequirementDefinition.isCancelOnRemoveFromAircraft(),
            false ) ) {
         lBuilder.isCancelOnRemoveFromAircraft();
      }

      if ( ( boolean ) defaultIfNull( aRequirementDefinition.isCancelOnRemoveFromAnyComponent(),
            false ) ) {
         lBuilder.isCancelOnRemoveFromAnyComponent();
      }

      if ( ( boolean ) defaultIfNull( aRequirementDefinition.isExecutable(), false ) ) {
         lBuilder.isExecutableRequirement();
      }

      boolean lRecurring =
            ( boolean ) defaultIfNull( aRequirementDefinition.getRecurring(), false );
      if ( lRecurring ) {
         lBuilder.isRecurring();
      }

      ScheduleFromOption lScheduleFromOption =
            ( ScheduleFromOption ) defaultIfNull( aRequirementDefinition.getScheduleFromOption(),
                  ScheduleFromOption.MANUFACTURED_DATE );

      switch ( lScheduleFromOption ) {
         case EFFECTIVE_DATE:
            lBuilder.isScheduledFromEffectiveDate( aRequirementDefinition.getEffectiveDate() );
            break;
         case RECEIVED_DATE:
            lBuilder.isScheduledFromRecievedDate();
            break;
         case MANUFACTURED_DATE:
            lBuilder.isScheduledFromManufacturedDate();
            break;
      }

      TaskTaskKey lPriorRevision = aRequirementDefinition.getPreviousRevision();
      if ( lPriorRevision != null ) {
         TaskDefnKey lTaskDefnKey = TaskTaskTable.findByPrimaryKey( lPriorRevision ).getTaskDefn();
         lBuilder.withTaskDefn( lTaskDefnKey );
      }

      if ( aRequirementDefinition.getOrganization() != null ) {
         lBuilder.forOrganization( aRequirementDefinition.getOrganization() );
      }

      lBuilder.withRevisionNote( aRequirementDefinition.getRevisionNote() );
      lBuilder.withRevisionReason( aRequirementDefinition.getRevisionReason() );
      lBuilder.withInstructions( aRequirementDefinition.getInstructions().orElse( null ) );

      TaskTaskKey lReqDefnKey = lBuilder.build();

      TaskTaskTable lTaskTaskTable = TaskTaskTable.findByPrimaryKey( lReqDefnKey );

      if ( aRequirementDefinition.isEnforceWorkscope().isPresent() ) {
         lTaskTaskTable
               .setEnforceWorkscopeBool( aRequirementDefinition.isEnforceWorkscope().get() );
      }
      if ( !StringUtils.isBlank( aRequirementDefinition.getTaskName() ) ) {
         lTaskTaskTable.setTaskName( aRequirementDefinition.getTaskName() );
         lTaskTaskTable.update();
      }

      // The deprecated TaskRevisionBuilder does not support the minimum forecast range, so add it
      // here if provided.
      if ( aRequirementDefinition.getMinimumForecastRange() != null ) {
         lTaskTaskTable.setForecaseRangeQt(
               aRequirementDefinition.getMinimumForecastRange().doubleValue() );
         lTaskTaskTable.update();
      }

      if ( lRecurring ) {
         // Recurring requirement definitions need a creation dependency to itself.
         TaskTaskDepTable lTaskDepTable =
               TaskTaskDepTable.create( TaskTaskDepTable.generatePrimaryKey( lReqDefnKey ) );
         lTaskDepTable.setDepTaskDefn( lTaskTaskTable.getTaskDefn() );
         lTaskDepTable.setTaskDepAction( RefTaskDepActionKey.CRT );
         lTaskDepTable.insert();
      } else {
         // Non-recurring requirement definitions with a due date need a scheduling rule.
         // (Logic derived from TaskDefinitionService.updateHardDeadlineDetails )
         if ( aRequirementDefinition.getDueDate() != null ) {

            if ( lScheduleFromOption.equals( ScheduleFromOption.EFFECTIVE_DATE ) ) {
               int lInterval =
                     Math.round( TimeUnit.DAY.getDateDiff( aRequirementDefinition.getDueDate(),
                           aRequirementDefinition.getEffectiveDate() ) );

               OneTimeSchedulingRule lOneTimeSchedulingRule =
                     new OneTimeSchedulingRule( DataTypeKey.CDY, new BigDecimal( lInterval ) );
               lOneTimeSchedulingRule.setDeviation( new BigDecimal( 0.0 ) );
               lOneTimeSchedulingRule.setNotification( new BigDecimal( 0.0 ) );
               lOneTimeSchedulingRule.setSchedToPlanLow( new BigDecimal( 0.0 ) );
               lOneTimeSchedulingRule.setSchedToPlanHigh( new BigDecimal( 0.0 ) );
               new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRule, lReqDefnKey );
            }
         }
      }

      // Assign the following tasks.
      Map<RefTaskDepActionKey, Set<TaskTaskKey>> lFollowingTaskDefnMap =
            aRequirementDefinition.getFollowingTaskDefinitionMap();
      for ( RefTaskDepActionKey lAction : lFollowingTaskDefnMap.keySet() ) {
         for ( TaskTaskKey lFollowingTaskDefn : lFollowingTaskDefnMap.get( lAction ) ) {
            TaskDefnKey lFollowingTaskDefnKey =
                  TaskTaskTable.findByPrimaryKey( lFollowingTaskDefn ).getTaskDefn();
            TaskTaskDepTable lTaskDepTable =
                  TaskTaskDepTable.create( TaskTaskDepTable.generatePrimaryKey( lReqDefnKey ) );
            lTaskDepTable.setTaskDepAction( lAction );
            lTaskDepTable.setDepTaskDefn( lFollowingTaskDefnKey );
            lTaskDepTable.insert();
         }
      }

      // Assign the Link to tasks.
      Map<RefTaskDepActionKey, Set<TaskTaskKey>> lLinkToTaskDefnMap =
            aRequirementDefinition.getLinkedTaskDefinitions();
      for ( RefTaskDepActionKey lAction : lLinkToTaskDefnMap.keySet() ) {
         for ( TaskTaskKey lLinkToTaskDefn : lLinkToTaskDefnMap.get( lAction ) ) {
            TaskDefnKey lLinkToTaskDefnKey =
                  TaskTaskTable.findByPrimaryKey( lLinkToTaskDefn ).getTaskDefn();
            TaskTaskDepTable lTaskDepTable =
                  TaskTaskDepTable.create( TaskTaskDepTable.generatePrimaryKey( lReqDefnKey ) );
            lTaskDepTable.setTaskDepAction( lAction );
            lTaskDepTable.setDepTaskDefn( lLinkToTaskDefnKey );
            lTaskDepTable.insert();
         }
      }

      // Assign the job card definitions.
      TaskDefnKey lReqTaskDefnKey = lTaskTaskTable.getTaskDefn();
      for ( TaskTaskKey lJicDefn : aRequirementDefinition.getJobCardDefinitions() ) {
         TaskJicReqMapKey lTaskJicReqMapKey =
               TaskJicReqMapTable.create( lJicDefn, lReqTaskDefnKey ).insert();
         TaskJicReqMapTable lTaskJicReqMapTable =
               TaskJicReqMapTable.findByPrimaryKey( lTaskJicReqMapKey );
         lTaskJicReqMapTable
               .setOrder( aRequirementDefinition.getJobCardDefinitionOrder( lJicDefn ) );
         lTaskJicReqMapTable.update();
      }

      // set prevent manual initialization
      TaskDefnTable lTaskDefnTable = TaskDefnTable.findByPrimaryKey( lReqTaskDefnKey );
      lTaskDefnTable.setPreventManualInitializationBool(
            aRequirementDefinition.isPreventManualInitialization().orElse( false ) );
      lTaskDefnTable.update();

      // Add the scheduling rules.
      for ( SchedulingRule lScheduling : aRequirementDefinition.getSchedulings() ) {

         if ( lScheduling.getUsageParameter() == null ) {
            throw new RuntimeException( "Usage parameter must be set for a scheduling rule" );
         }
         if ( lScheduling.getDeviation() == null ) {
            lScheduling.setDeviation( new BigDecimal( 0.0 ) );
         }

         if ( lScheduling.getNotification() == null ) {
            lScheduling.setNotification( new BigDecimal( 0.0 ) );
         }

         if ( lScheduling.getSchedToPlanHigh() == null ) {
            lScheduling.setSchedToPlanHigh( new BigDecimal( 0.0 ) );
         }

         if ( lScheduling.getSchedToPlanLow() == null ) {
            lScheduling.setSchedToPlanLow( new BigDecimal( 0.0 ) );
         }

         if ( lRecurring ) {
            BigDecimal lInterval = ( ( RecurringSchedulingRule ) lScheduling ).getInterval();
            if ( lInterval == null ) {
               throw new RuntimeException( "Interval must be supplied for the scheduling rule" );
            }
            new RecurringSchedulingRuleBuilder().build( ( RecurringSchedulingRule ) lScheduling,
                  lReqDefnKey );

         } else {
            BigDecimal lThreshold = ( ( OneTimeSchedulingRule ) lScheduling ).getThreshold();
            if ( lThreshold == null ) {
               throw new RuntimeException( "Threshold must be supplied for the scheduling rule" );
            }
            new OneTimeSchedulingRuleBuilder().build( ( OneTimeSchedulingRule ) lScheduling,
                  lReqDefnKey );
         }
      }

      for ( RefWorkTypeKey lWorkType : aRequirementDefinition.getWorkTypes() ) {
         TaskWorkTypeTable.create( lReqDefnKey, lWorkType ).insert();
      }

      // Check if it is approved for the fleet.
      boolean lIsFleetApproved =
            ( boolean ) defaultIfNull( aRequirementDefinition.getFleetApproval(), true );
      if ( lIsFleetApproved ) {
         TaskFleetApprovalKey lApproval = new TaskFleetApprovalKey( lReqTaskDefnKey );
         TaskFleetApprovalTable lApprovalTable =
               TaskFleetApprovalTable.findByPrimaryKey( lApproval );
         if ( lApprovalTable.exists() ) {
            lApprovalTable.setTaskRevision( lReqDefnKey );
            lApprovalTable.update();
         } else {
            lApprovalTable = TaskFleetApprovalTable.create( lApproval );
            lApprovalTable.setTaskRevision( lReqDefnKey );
            lApprovalTable.insert();
         }
      }

      // Add the labour requirements.
      for ( LabourRequirement lLabourRequirement : aRequirementDefinition.getLaborRequirements() ) {
         new LabourRequirementBuilder( lReqDefnKey, lLabourRequirement.getLabourSkill() )
               .withWorkSchedHrs( lLabourRequirement.getWorkPerformedScheduleHours() )
               .withCertRequired( lLabourRequirement.isCertRequired() )
               .withCertSchedHrs( lLabourRequirement.getCertSchedHrs() )
               .withInspRequired( lLabourRequirement.isInspRequired() )
               .withInspSchedHrs( lLabourRequirement.getInspSchedHrs() )
               .withNumberOfPeopleRequired( lLabourRequirement.getNumberOfPeopleRequired() )
               .build();
      }

      // Add Zone
      for ( Zone lZone : aRequirementDefinition.getZones() ) {
         ZoneKey lZoneKey = Domain.createZone( zone -> {
            zone.setZoneCode( lZone.getZoneCode() );
            zone.setParentAssembly(
                  aRequirementDefinition.getConfigurationSlot().getAssemblyKey() );
         } );

         TaskZoneTable lTaskZone =
               TaskZoneTable.create( TaskZoneTable.generatePrimaryKey( lReqDefnKey ) );
         lTaskZone.setZone( lZoneKey );
         lTaskZone.insert();
      }

      // Add Panel
      for ( String lPanelCode : aRequirementDefinition.getPanels() ) {

         TaskPanelTable lTaskPanel =
               TaskPanelTable.create( TaskPanelTable.generatePrimaryKey( lReqDefnKey ) );
         lTaskPanel.setPanel( Domain.readPanel(
               aRequirementDefinition.getConfigurationSlot().getAssemblyKey(), lPanelCode ) );
         lTaskPanel.insert();
      }

      // Set Tool Requirements
      for ( ToolRequirement lToolRequirement : aRequirementDefinition.getToolRequirements() ) {
         if ( lToolRequirement.getRequirementDefinition() != null ) {
            throw new RuntimeException(
                  "Requirement Definition for a tool requirement can only be set from the within the Requirement Definition Builder" );
         }
         lToolRequirement.setRequirementDefinition( lReqDefnKey );
         ToolRequirementBuilder.build( lToolRequirement );
      }

      // Set Part Requirements
      for ( DomainConfiguration<PartRequirementDefinition> lPartRequirementConfiguration : aRequirementDefinition
            .getPartRequirementDefinitions() ) {
         PartRequirementDefinition lPartRequirement = new PartRequirementDefinition();
         lPartRequirementConfiguration.configure( lPartRequirement );
         if ( lPartRequirement.getTaskDefinition() != null ) {
            throw new RuntimeException(
                  "Requirement Definition for a part requirement can only be set from the within the Requirement Definition Builder" );
         }
         lPartRequirement.setTaskDefinition( lReqDefnKey );
         PartRequirementDefinitionBuilder.build( lPartRequirement );
      }

      // Set Part Transformation
      for ( PartTransformation lPartTransformation : aRequirementDefinition
            .getPartTransformations() ) {
         PartTransformationKey lPartTransformationKey =
               new PartTransformationKey( lReqDefnKey, lPartTransformation.getOldPartNo() );

         TaskPartTransform lTaskPartTransform = TaskPartTransform.create( lPartTransformationKey );
         lTaskPartTransform.setNewPartNo( lPartTransformation.getNewPartNo() );

         lTaskPartTransform.insert();
      }

      // Set Technical References
      int lIetmOrder = 1;
      for ( Map.Entry<IetmDefinitionKey, IetmTopicKey> lTechnicalReference : aRequirementDefinition
            .getIetmTechnicalReferences().entrySet() ) {

         TaskTaskIetm lTaskTaskIetm =
               TaskTaskIetm.create( TaskTaskIetm.generatePrimaryKey( lReqDefnKey ) );
         lTaskTaskIetm.setIETM( lTechnicalReference.getValue() );
         lTaskTaskIetm.setIETMOrd( lIetmOrder++ );
         lTaskTaskIetm.insert();
      }

      // Set Steps
      int lStepOrder = 1;
      for ( DomainConfiguration<Step> lStepConfiguration : aRequirementDefinition.getSteps() ) {
         Step lStep = new Step();
         lStepConfiguration.configure( lStep );
         if ( lStep.getRequirementDefinition() != null ) {
            throw new RuntimeException(
                  "Requirement Definition for a Step can only be set from the within the Requirement Definition Builder" );
         }
         lStep.setRequirementDefinition( lReqDefnKey );
         StepBuilder.build( lStep, lStepOrder++ );
      }

      // Set Attachment
      for ( Map.Entry<IetmDefinitionKey, IetmTopicKey> lAttachment : aRequirementDefinition
            .getIetmAttachments().entrySet() ) {

         TaskTaskIetm lTaskTaskIetm =
               TaskTaskIetm.create( TaskTaskIetm.generatePrimaryKey( lReqDefnKey ) );
         lTaskTaskIetm.setIETM( lAttachment.getValue() );
         lTaskTaskIetm.setIETMOrd( lIetmOrder++ );
         lTaskTaskIetm.insert();
      }

      // Set Measurements
      for ( DataTypeKey lMeasurement : aRequirementDefinition.getMeasurements() ) {
         TaskParmData lTaskParmData =
               TaskParmData.create( new TaskParmDataKey( lReqDefnKey, lMeasurement ) );
         lTaskParmData
               .setDataOrd( aRequirementDefinition.getMeasurements().indexOf( lMeasurement ) );
         lTaskParmData.insert();
      }

      // Set Custom Planning Skills
      for ( LabourRequirement lCustomSkill : aRequirementDefinition.getCustomPlanningSkills() ) {

         TaskPlanningTypeSkillKey lNewKey =
               new TaskPlanningTypeSkillKey( lReqDefnKey, lCustomSkill.getLabourSkill() );
         TaskPlanningTypeSkillTable lNewTableEntry = TaskPlanningTypeSkillTable.create( lNewKey );
         lNewTableEntry.setEffortHr( lCustomSkill.getWorkPerformedScheduleHours().doubleValue() );
         lNewTableEntry.insert();

      }

      // Set Impacts
      for ( Map.Entry<RefImpactKey, String> lImpact : aRequirementDefinition.getImpacts()
            .entrySet() ) {
         DataSetArgument lArgs = new DataSetArgument();

         lArgs.add( lReqDefnKey, new String[] { "task_db_id", "task_id" } );
         lArgs.add( lImpact.getKey(), new String[] { "impact_db_id", "impact_cd" } );
         lArgs.add( "impact_ldesc", lImpact.getValue() );

         MxDataAccess.getInstance().executeInsert( "task_impact", lArgs );
      }

      // Set Task Flags
      TaskTaskFlagsKey lTaskFlagKey = new TaskTaskFlagsKey( lReqDefnKey );
      TaskTaskFlagsTable lTaskFlags = TaskTaskFlagsTable.findByPrimaryKey( lTaskFlagKey );
      if ( !lTaskFlags.exists() ) {
         lTaskFlags = TaskTaskFlagsTable.create( lTaskFlagKey );
         lTaskFlags.insert();
      }
      lTaskFlags.setPreventExeBool(
            ( boolean ) defaultIfNull( aRequirementDefinition.isPreventExecution(), false ) );
      lTaskFlags.setNSVBool(
            ( boolean ) defaultIfNull( aRequirementDefinition.isNextShopVisit(), false ) );
      lTaskFlags.update();

      // Set Advisories
      for ( DomainConfiguration<Advisory> lAdvisoryConfiguration : aRequirementDefinition
            .getAdvisories() ) {
         Advisory lAdvisory = new Advisory();
         lAdvisoryConfiguration.configure( lAdvisory );
         RefTaskAdvisoryTypeKey lAdvisoryTypeKey =
               ( RefTaskAdvisoryTypeKey ) defaultIfNull( lAdvisory.getType(),
                     RefTaskAdvisoryTypeKey.EXECUTE );
         TaskAdvisoryKey lTaskAdvisoryKey = new TaskAdvisoryKey( lReqDefnKey,
               UtlRole.getRoleId( "ADMIN" ).getRoleId(), lAdvisoryTypeKey );

         TaskAdvisoryTable lTaskAdvisoryTable = TaskAdvisoryTable.create( lTaskAdvisoryKey );
         lTaskAdvisoryTable.setAdvisoryLDesc( lAdvisory.getNotes() );
         lTaskAdvisoryTable.insert();
      }

      if ( RefTaskClassKey.REPREF.equals( aRequirementDefinition.getTaskClass() ) ) {
         TaskRepRefDao lTaskRepRefDao = InjectorContainer.get().getInstance( TaskRepRefDao.class );
         TaskRepRefTableRow lTaskRepRefTableRow = lTaskRepRefDao.create( lReqDefnKey );
         lTaskRepRefTableRow.setDamagedComponentBool( ( boolean ) defaultIfNull(
               aRequirementDefinition.getDamagedComponentBool(), false ) );
         lTaskRepRefTableRow.setMocApprovalBool(
               ( boolean ) defaultIfNull( aRequirementDefinition.getMocApprovalBool(), false ) );
         lTaskRepRefTableRow.setDamageRecordBool(
               ( boolean ) defaultIfNull( aRequirementDefinition.getDamageRecordBool(), true ) );
         lTaskRepRefTableRow.setOperationalRestrictionsDescription(
               aRequirementDefinition.getOpsRestrictionsDesc() );
         lTaskRepRefTableRow
               .setPerformancePenaltiesDescription( aRequirementDefinition.getPerfPenaltiesDesc() );
         lTaskRepRefDao.insert( lTaskRepRefTableRow );
      }

      addWeightAndBalance( aRequirementDefinition, lReqDefnKey );

      return lReqDefnKey;
   }


   private static void addWeightAndBalance( RequirementDefinition aRequirementDefinition,
         TaskTaskKey taskTaskKey ) {

      for ( DomainConfiguration<WeightAndBalanceImpact> weightAndBalanceImpactConfiguration : aRequirementDefinition
            .getWeightAndBalanceImpactList() ) {

         WeightAndBalanceImpact weightAndBalanceImpact = new WeightAndBalanceImpact();
         weightAndBalanceImpactConfiguration.configure( weightAndBalanceImpact );
         weightAndBalanceImpact.getTaskTaskKey().ifPresent( key -> {
            throw new RuntimeException(
                  "Weight and balance impact cannot have their TaskTaskKey previously set." );
         } );
         weightAndBalanceImpact.setTaskTaskKey( taskTaskKey );
         WeightAndBalanceImpactBuilder.build( weightAndBalanceImpact );

      }
   }
}
