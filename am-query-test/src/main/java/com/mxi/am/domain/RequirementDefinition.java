package com.mxi.am.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EqpPlanningTypeKey;
import com.mxi.mx.core.key.IetmDefinitionKey;
import com.mxi.mx.core.key.IetmTopicKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefImpactKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefReschedFromKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.RefTaskDepActionKey;
import com.mxi.mx.core.key.RefTaskMustRemoveKey;
import com.mxi.mx.core.key.RefTaskRevReasonKey;
import com.mxi.mx.core.key.RefTaskSubclassKey;
import com.mxi.mx.core.key.RefWorkTypeKey;
import com.mxi.mx.core.key.TaskTaskKey;


public class RequirementDefinition {

   private TaskTaskKey previousRevision;
   private String code;
   private String taskName;
   private Boolean unique;
   private Boolean onCondition;
   private Boolean executable;
   private String applicabilityRange;
   private Integer revisionNumber;
   private RefTaskDefinitionStatusKey status;
   private ConfigSlotKey configurationSlot;
   private Boolean fleetApproval;
   private Boolean recurring;
   private BigDecimal minimumForecastRange;
   private Date effectiveDate;
   private Date dueDate;
   private List<PartNoKey> partNos = new ArrayList<>();
   private String applicabilityRule;
   private Boolean isCreateOnInstall;
   private Boolean isCreateOnRemove;
   private Boolean isNextShopVisit;
   private Boolean isPreventExecution;
   private Boolean isCancelOnInstallAircraft;
   private Boolean isCancelOnAnyInstall;
   private Boolean isCancelOnRemoveFromAircraft;
   private Boolean isCancelOnRemoveFromAnyComponent;
   private ScheduleFromOption scheduleFromOption;
   private RefTaskMustRemoveKey mustRemove;
   private EqpPlanningTypeKey planningType;
   private RefTaskClassKey taskClass;
   private String revisionNote;
   private RefTaskRevReasonKey revisionReason;
   private Boolean isSchedulingRuleUsedWhenCreatedFromAnotherTask;
   private RefReschedFromKey rescheduleFromOption;
   private String instructions;
   private Boolean mocApprovalBool;
   private Boolean damageRecordBool;
   private Boolean damagedComponentBool;
   private String opsRestrictionsDesc;
   private String perfPenaltiesDesc;
   private RefTaskSubclassKey taskSubClass;

   private Map<RefTaskDepActionKey, Set<TaskTaskKey>> followingTaskDefinitions =
         new HashMap<RefTaskDepActionKey, Set<TaskTaskKey>>();
   private List<SchedulingRule> schedulings = new ArrayList<>();
   private List<TaskTaskKey> jobCardDefinitions = new ArrayList<TaskTaskKey>();
   private Map<RefTaskDepActionKey, Set<TaskTaskKey>> linkedTaskDefinitions =
         new HashMap<RefTaskDepActionKey, Set<TaskTaskKey>>();
   private List<LabourRequirement> labourRequirements = new ArrayList<LabourRequirement>();
   private OrgKey orgKey;
   private List<Zone> zones = new ArrayList<Zone>();
   private List<String> panels = new ArrayList<>();
   private List<ToolRequirement> toolRequirements = new ArrayList<ToolRequirement>();
   private List<DomainConfiguration<PartRequirementDefinition>> partRequirementDefinitionConfigurations =
         new ArrayList<DomainConfiguration<PartRequirementDefinition>>();
   private List<DataTypeKey> measurements = new ArrayList<DataTypeKey>();
   private List<DomainConfiguration<Step>> steps = new ArrayList<>();
   private List<LabourRequirement> customPlanningSkills = new ArrayList<LabourRequirement>();
   private Map<RefImpactKey, String> impacts = new HashMap<RefImpactKey, String>();

   private List<PartTransformation> partTransformations = new ArrayList<PartTransformation>();

   private List<DomainConfiguration<Advisory>> advisories = new ArrayList<>();

   private Map<IetmDefinitionKey, IetmTopicKey> ietmTechnicalReferences =
         new HashMap<IetmDefinitionKey, IetmTopicKey>();
   private Map<IetmDefinitionKey, IetmTopicKey> ietmAttachments =
         new HashMap<IetmDefinitionKey, IetmTopicKey>();

   private List<RefWorkTypeKey> workTypes = new ArrayList<>();

   private Boolean preventManualInitialization;

   private Boolean enforceWorkscope;

   private Map<TaskTaskKey, Integer> jobCardDefinitionsOrder = new HashMap<>();

   private List<DomainConfiguration<WeightAndBalanceImpact>> weightAndBalanceImpacts = new ArrayList<>();


   public static enum ScheduleFromOption {
      MANUFACTURED_DATE, RECEIVED_DATE, EFFECTIVE_DATE
   };


   public Map<RefImpactKey, String> getImpacts() {
      return impacts;
   }


   public void addImpact( RefImpactKey impactKey, String description ) {

      impacts.put( impactKey, description );
   }


   public OrgKey getOrganization() {
      return orgKey;
   }


   public void setOrganization( OrgKey orgKey ) {
      this.orgKey = orgKey;
   }


   public EqpPlanningTypeKey getPlanningType() {
      return planningType;
   }


   public void setPlanningType( EqpPlanningTypeKey planningType ) {
      this.planningType = planningType;
   }


   public void addCustomPlanningSkill( RefLabourSkillKey labourSkill,
         BigDecimal workPerformedScheduleHours ) {
      customPlanningSkills.add( new LabourRequirement( labourSkill, workPerformedScheduleHours ) );
   }


   public List<LabourRequirement> getCustomPlanningSkills() {
      return customPlanningSkills;
   }


   public void setPreviousRevision( TaskTaskKey previousRevision ) {
      this.previousRevision = previousRevision;
   }


   public TaskTaskKey getPreviousRevision() {
      return previousRevision;
   }


   public void setCreateOnInstall() {
      isCreateOnInstall = true;
   }


   public Boolean isCreateOnInstall() {
      return isCreateOnInstall;
   }


   public RefTaskMustRemoveKey getMustRemove() {
      return mustRemove;
   }


   public void setMustRemove( RefTaskMustRemoveKey mustRemove ) {
      this.mustRemove = mustRemove;
   }


   public void setCreateOnRemove() {
      isCreateOnRemove = true;
   }


   public Boolean isCreateOnRemove() {
      return isCreateOnRemove;
   }


   public Boolean isNextShopVisit() {
      return isNextShopVisit;
   }


   public void setNextShopVisit( Boolean isNextShopVisit ) {
      this.isNextShopVisit = isNextShopVisit;
   }


   public Boolean isPreventExecution() {
      return isPreventExecution;
   }


   public void setPreventExecution( Boolean isPreventExecution ) {
      this.isPreventExecution = isPreventExecution;
   }


   public void setCode( String code ) {
      this.code = code;
   }


   public String getCode() {
      return code;
   }


   public void setApplicabilityRange( String applicabilityRange ) {
      this.applicabilityRange = applicabilityRange;
   }


   public String getApplicabilityRange() {
      return applicabilityRange;
   }


   public void setApplicabilityRule( String applicabilityRule ) {
      this.applicabilityRule = applicabilityRule;
   }


   public String getApplicabilityRule() {
      return applicabilityRule;
   }


   public void setUnique( boolean unique ) {
      this.unique = unique;
   }


   public Boolean isUnique() {
      return unique;
   }


   public void setOnCondition( boolean onCondition ) {
      this.onCondition = onCondition;
   }


   public Boolean isOnCondition() {
      return onCondition;
   }


   public void setExecutable( boolean executable ) {
      this.executable = executable;
   }


   public Boolean isExecutable() {
      return executable;
   }


   public void setSchedulingRuleUsedWhenCreatedFromAnotherTask(
         boolean schedulingRuleUsedWhenCreatedFromAnotherTask ) {
      isSchedulingRuleUsedWhenCreatedFromAnotherTask = schedulingRuleUsedWhenCreatedFromAnotherTask;
   }


   public Optional<Boolean> isSchedulingRuleUsedWhenCreatedFromAnotherTask() {
      return Optional.ofNullable( isSchedulingRuleUsedWhenCreatedFromAnotherTask );
   }


   public void setRevisionNumber( Integer revisionNumber ) {
      this.revisionNumber = revisionNumber;
   }


   public Integer getRevisionNumber() {
      return revisionNumber;
   }


   public void setStatus( RefTaskDefinitionStatusKey status ) {
      this.status = status;
   }


   public RefTaskDefinitionStatusKey getStatus() {
      return status;
   }


   public void setMinimumForecastRange( BigDecimal minimumForecastRange ) {
      this.minimumForecastRange = minimumForecastRange;
   }


   public void setMinimumForecastRange( int minimumForecastRange ) {
      this.minimumForecastRange = BigDecimal.valueOf( minimumForecastRange );
   }


   public BigDecimal getMinimumForecastRange() {
      return minimumForecastRange;
   }


   public void setDueDate( Date dueDate ) {
      this.dueDate = dueDate;
   }


   public Date getDueDate() {
      return dueDate;
   }


   public Optional<String> getInstructions() {
      return Optional.ofNullable( instructions );
   }


   public void setInstructions( String instructions ) {
      this.instructions = instructions;
   }


   public Optional<Boolean> isPreventManualInitialization() {
      return Optional.ofNullable( preventManualInitialization );
   }


   public void setPreventManualInitialization( Boolean preventManualInitialization ) {
      this.preventManualInitialization = preventManualInitialization;
   }


   public void addFollowingTaskDefinition( RefTaskDepActionKey action,
         TaskTaskKey followingTaskDefinition ) {
      Set<TaskTaskKey> followingTasks = followingTaskDefinitions.get( action );
      if ( followingTasks == null ) {
         followingTasks = new HashSet<TaskTaskKey>();
         followingTaskDefinitions.put( action, followingTasks );
      }
      followingTasks.add( followingTaskDefinition );
   }


   public Map<RefTaskDepActionKey, Set<TaskTaskKey>> getFollowingTaskDefinitionMap() {
      return followingTaskDefinitions;
   }


   public void addLinkedTaskDefinition( RefTaskDepActionKey action, TaskTaskKey linkToTask ) {
      Set<TaskTaskKey> linkToTaskKeys = linkedTaskDefinitions.get( action );

      if ( linkToTaskKeys == null ) {
         linkToTaskKeys = new HashSet<TaskTaskKey>();
         linkedTaskDefinitions.put( action, linkToTaskKeys );
      }
      linkToTaskKeys.add( linkToTask );
   }


   public Map<RefTaskDepActionKey, Set<TaskTaskKey>> getLinkedTaskDefinitions() {
      return linkedTaskDefinitions;
   }


   public void addJobCardDefinition( TaskTaskKey jicDefinition ) {
      jobCardDefinitions.add( jicDefinition );
   }


   public List<TaskTaskKey> getJobCardDefinitions() {
      return jobCardDefinitions;
   }


   public void addSchedulingRule( OneTimeSchedulingRule schedulingRule ) {
      schedulings.add( schedulingRule );
   }


   public void addRecurringSchedulingRule( RecurringSchedulingRule recurringSchedulingRule ) {
      schedulings.add( recurringSchedulingRule );
   }


   public void addSchedulingRule( DataTypeKey usageParameter, BigDecimal threshold ) {
      schedulings.add( new OneTimeSchedulingRule( usageParameter, threshold ) );
   }


   public void addRecurringSchedulingRule( DataTypeKey usageParameter, BigDecimal interval ) {
      schedulings.add( new RecurringSchedulingRule( usageParameter, interval ) );
   }


   public void addSchedulingRule( DataTypeKey usageParameter, int threshold ) {
      schedulings
            .add( new RecurringSchedulingRule( usageParameter, BigDecimal.valueOf( threshold ) ) );
   }


   public List<SchedulingRule> getSchedulings() {
      return schedulings;
   }


   public void addLinkToTasks( RefTaskDepActionKey action, TaskTaskKey linkedTaskDefinition ) {
      Set<TaskTaskKey> linkToTasks = linkedTaskDefinitions.get( action );
      if ( linkedTaskDefinitions == null ) {
         linkToTasks = new HashSet<TaskTaskKey>();
         linkedTaskDefinitions.put( action, linkToTasks );
      }
      linkToTasks.add( linkedTaskDefinition );
   }


   public void addLabourRequirement( RefLabourSkillKey labourSkill,
         BigDecimal workPerformedScheduleHours ) {
      labourRequirements.add( new LabourRequirement( labourSkill, workPerformedScheduleHours ) );
   }


   public void addLabourRequirement( LabourRequirement labourRequirement ) {
      labourRequirements.add( labourRequirement );
   }


   public List<LabourRequirement> getLaborRequirements() {
      return labourRequirements;
   }


   public void againstConfigurationSlot( ConfigSlotKey configurationSlot ) {
      this.configurationSlot = configurationSlot;
   }


   public ConfigSlotKey getConfigurationSlot() {
      return configurationSlot;
   }


   public void setFleetApproval( Boolean fleetApproval ) {
      this.fleetApproval = fleetApproval;
   }


   public Boolean getFleetApproval() {
      return fleetApproval;
   }


   public void setRecurring( boolean recurring ) {
      this.recurring = recurring;
   }


   public Boolean getRecurring() {
      return recurring;
   }


   public void setScheduledFromManufacturedDate() {
      scheduleFromOption = ScheduleFromOption.MANUFACTURED_DATE;
   }


   public void setScheduledFromReceivedDate() {
      scheduleFromOption = ScheduleFromOption.RECEIVED_DATE;
   }


   public void setScheduledFromEffectiveDate( Date effectiveDate ) {
      if ( effectiveDate == null ) {
         throw new RuntimeException(
               "Effective Date must be provided when scheduling requirement from effective date." );
      }
      scheduleFromOption = ScheduleFromOption.EFFECTIVE_DATE;
      this.effectiveDate = effectiveDate;
   }


   public void setRescheduleFromOption( RefReschedFromKey rescheduleFromOption ) {
      this.rescheduleFromOption = rescheduleFromOption;
   }


   public Optional<RefReschedFromKey> getRescheduleFromOption() {
      return Optional.ofNullable( rescheduleFromOption );
   }


   protected void setScheduleFromOption() {
      // Do not allow the user to set the schedule-from, we will set it via the
      // setScheduledFrom*Date() methods.
   }


   public ScheduleFromOption getScheduleFromOption() {
      return scheduleFromOption;
   }


   public void setEffectiveDate() {
      // Do not allow the user to set the effective date, it will be set via the
      // setScheduledFromEffectiveDate method
   }


   public Date getEffectiveDate() {
      return effectiveDate;
   }


   public List<PartNoKey> getPartNos() {
      return partNos;
   }


   public void addPartNo( PartNoKey partNo ) {
      partNos.add( partNo );
   }


   public List<Zone> getZones() {
      return zones;
   }


   public void addZone( String zoneCode ) {
      zones.add( new Zone( zoneCode ) );
   }


   public List<String> getPanels() {
      return panels;
   }


   public void addPanel( String panelCode ) {
      panels.add( panelCode );
   }


   public List<ToolRequirement> getToolRequirements() {
      return toolRequirements;
   }


   public void addToolRequirement( PartNoKey partNo, BigDecimal schedHours ) {
      toolRequirements.add( new ToolRequirement( partNo, schedHours ) );
   }


   public List<DomainConfiguration<PartRequirementDefinition>> getPartRequirementDefinitions() {
      return partRequirementDefinitionConfigurations;
   }


   public void addPartRequirementDefinition(
         DomainConfiguration<PartRequirementDefinition> partRequirement ) {
      partRequirementDefinitionConfigurations.add( partRequirement );
   }


   public Map<IetmDefinitionKey, IetmTopicKey> getIetmTechnicalReferences() {
      return ietmTechnicalReferences;
   }


   public void
         addIetmTechnicalReferences( Map<IetmDefinitionKey, IetmTopicKey> technicalReference ) {
      ietmTechnicalReferences.putAll( technicalReference );
   }


   public Map<IetmDefinitionKey, IetmTopicKey> getIetmAttachments() {
      return ietmAttachments;
   }


   public void addIetmAttachments( Map<IetmDefinitionKey, IetmTopicKey> attachments ) {
      ietmAttachments.putAll( attachments );
   }


   public List<DataTypeKey> getMeasurements() {
      return measurements;
   }


   public void addMeasurement( DataTypeKey measurement ) {
      measurements.add( measurement );
   }


   public List<DomainConfiguration<Step>> getSteps() {
      return steps;
   }


   public void addStep( DomainConfiguration<Step> step ) {
      steps.add( step );
   }


   public void addPartTransformation( PartNoKey oldPartNo, PartNoKey newPartNo ) {
      partTransformations.add( new PartTransformation( oldPartNo, newPartNo ) );
   }


   public List<PartTransformation> getPartTransformations() {
      return partTransformations;
   }


   public RefTaskClassKey getTaskClass() {
      return taskClass;
   }


   public void setTaskClass( RefTaskClassKey taskClass ) {
      this.taskClass = taskClass;
   }


   public List<DomainConfiguration<Advisory>> getAdvisories() {
      return advisories;
   }


   public void addAdvisory( DomainConfiguration<Advisory> advisory ) {
      advisories.add( advisory );
   }


   public String getRevisionNote() {
      return revisionNote;
   }


   public void setRevisionNote( String revisionNote ) {
      this.revisionNote = revisionNote;
   }


   public RefTaskRevReasonKey getRevisionReason() {
      return revisionReason;
   }


   public void setRevisionReason( RefTaskRevReasonKey revisionReason ) {
      this.revisionReason = revisionReason;
   }


   public List<RefWorkTypeKey> getWorkTypes() {
      return workTypes;
   }


   public void addWorkType( RefWorkTypeKey workType ) {
      workTypes.add( workType );
   }


   public Optional<Boolean> isEnforceWorkscope() {
      return Optional.ofNullable( enforceWorkscope );
   }


   public void setEnforceWorkscope( Boolean enforceWorkscope ) {
      this.enforceWorkscope = enforceWorkscope;
   }


   public Integer getJobCardDefinitionOrder( TaskTaskKey jobCard ) {
      return jobCardDefinitionsOrder.getOrDefault( jobCard, null );
   }


   public void setJobCardDefinitionsOrder( Map<TaskTaskKey, Integer> jobCardOrder ) {
      this.jobCardDefinitionsOrder = jobCardOrder;
   }


   public void setTaskName( String taskName ) {
      this.taskName = taskName;
   }


   public String getTaskName() {
      return taskName;
   }


   public Boolean getMocApprovalBool() {
      return mocApprovalBool;
   }


   public void setMocApprovalBool( Boolean mocApprovalBool ) {
      this.mocApprovalBool = mocApprovalBool;
   }


   public Boolean getDamageRecordBool() {
      return damageRecordBool;
   }


   public void setDamageRecordBool( Boolean damageRecordBool ) {
      this.damageRecordBool = damageRecordBool;
   }


   public Boolean getDamagedComponentBool() {
      return damagedComponentBool;
   }


   public void setDamagedComponentBool( Boolean damagedComponentBool ) {
      this.damagedComponentBool = damagedComponentBool;
   }


   public String getOpsRestrictionsDesc() {
      return opsRestrictionsDesc;
   }


   public void setOpsRestrictionsDesc( String opsRestrictionsDesc ) {
      this.opsRestrictionsDesc = opsRestrictionsDesc;
   }


   public String getPerfPenaltiesDesc() {
      return perfPenaltiesDesc;
   }


   public void setPerfPenaltiesDesc( String perfPenaltiesDesc ) {
      this.perfPenaltiesDesc = perfPenaltiesDesc;
   }


   public RefTaskSubclassKey getTaskSubClass() {
      return taskSubClass;
   }


   public void setTaskSubClass( RefTaskSubclassKey taskSubClass ) {
      this.taskSubClass = taskSubClass;
   }


   public Boolean isCancelOnInstallAircraft() {
      return isCancelOnInstallAircraft;
   }


   public void setCancelOnInstallAircraft( Boolean isCancelOnInstallAircraft ) {
      this.isCancelOnInstallAircraft = isCancelOnInstallAircraft;
   }


   public Boolean isCancelOnAnyInstall() {
      return isCancelOnAnyInstall;
   }


   public void setCancelOnAnyInstall( Boolean isCancelOnAnyInstall ) {
      this.isCancelOnAnyInstall = isCancelOnAnyInstall;
   }


   public Boolean isCancelOnRemoveFromAircraft() {
      return isCancelOnRemoveFromAircraft;
   }


   public void setCancelOnRemoveFromAircraft( Boolean isCancelOnRemoveFromAircraft ) {
      this.isCancelOnRemoveFromAircraft = isCancelOnRemoveFromAircraft;
   }


   public Boolean isCancelOnRemoveFromAnyComponent() {
      return isCancelOnRemoveFromAnyComponent;
   }


   public void setCancelOnRemoveFromAnyComponent( Boolean isCancelOnRemoveFromAnyComponent ) {
      this.isCancelOnRemoveFromAnyComponent = isCancelOnRemoveFromAnyComponent;
   }


   public List<DomainConfiguration<WeightAndBalanceImpact>> getWeightAndBalanceImpactList() {
      return weightAndBalanceImpacts;
   }


   public void addWeightAndBalanceImpact( DomainConfiguration<WeightAndBalanceImpact> weightAndBalance ) {
      this.weightAndBalanceImpacts.add( weightAndBalance );
   }


   public class LinkToTasks {

      private RefTaskDepActionKey linkType;
      private TaskTaskKey linkTaskDefinition;


      public LinkToTasks(RefTaskDepActionKey linkType, TaskTaskKey linkTaskDefinition) {
         this.linkType = linkType;
         this.linkTaskDefinition = linkTaskDefinition;
      }


      public void setLinkType( RefTaskDepActionKey linkType ) {
         this.linkType = linkType;
      }


      public RefTaskDepActionKey getLinkType() {
         return linkType;
      }


      public void setLinkTaskDefinition( TaskTaskKey linkTaskDefinition ) {
         this.linkTaskDefinition = linkTaskDefinition;
      }


      public TaskTaskKey getLinkTaskDefinition() {
         return linkTaskDefinition;
      }
   }
}
