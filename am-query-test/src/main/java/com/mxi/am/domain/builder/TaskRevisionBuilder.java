package com.mxi.am.domain.builder;

import static com.mxi.mx.core.key.RefTaskSchedFromKey.EFFECTIVE_DT;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mxi.mx.common.exception.MxRuntimeException;
import com.mxi.mx.core.enumeration.task.ClassMode;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.EqpPlanningTypeKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefReschedFromKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.RefTaskDepActionKey;
import com.mxi.mx.core.key.RefTaskMustRemoveKey;
import com.mxi.mx.core.key.RefTaskRevReasonKey;
import com.mxi.mx.core.key.RefTaskSchedFromKey;
import com.mxi.mx.core.key.RefTaskSubclassKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskPartMapKey;
import com.mxi.mx.core.key.TaskRefDocKey;
import com.mxi.mx.core.key.TaskTaskDepKey;
import com.mxi.mx.core.key.TaskTaskFlagsKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.ref.RefTaskClass;
import com.mxi.mx.core.table.task.TaskDefnTable;
import com.mxi.mx.core.table.task.TaskLabourList;
import com.mxi.mx.core.table.task.TaskPartMapTable;
import com.mxi.mx.core.table.task.TaskRefDocTable;
import com.mxi.mx.core.table.task.TaskTaskDepTable;
import com.mxi.mx.core.table.task.TaskTaskFlagsTable;
import com.mxi.mx.core.table.task.TaskTaskTable;


/**
 * Builds a <code>task_task</code> object
 */
public class TaskRevisionBuilder implements DomainBuilder<TaskTaskKey>, HasTaskClass {

   public static final RefTaskClassKey TASK_CLASS_REF = new RefTaskClassKey( 10, "REF" );
   public static final RefTaskClassKey TASK_CLASS_BLOCK = new RefTaskClassKey( 10, "BLOCK" );

   private String applicabilityRange;
   private String applicabilityRule;
   private String blockChainDesc;
   private double certificationSchedHours;
   private final List<DomainBuilder<TaskDefnKey>> compliesWith =
         new ArrayList<DomainBuilder<TaskDefnKey>>();
   private DomainBuilder<ConfigSlotKey> configSlotBuilder = null;
   private Date effectiveFromDate;
   private double indepInspectionSchedHours;
   private boolean isNextShopVisit = false;
   private boolean isOnCondition = true;
   private boolean isRecurring = false;
   private boolean isUnique = false;
   private RefLabourSkillKey labourSkill;
   private boolean manualScheduling;
   private List<PartNoKey> partKeyList = new ArrayList<PartNoKey>();
   private int peopleRequired = 1;
   private final Set<TaskDefnKey> requirements = new HashSet<TaskDefnKey>();
   private final Map<TaskDefnKey, Integer> revisionMap = new HashMap<TaskDefnKey, Integer>();
   private Integer revisionNumber;
   private RefTaskSchedFromKey scheduleFrom;
   private RefTaskClassKey taskClass = RefTaskClassKey.REQ;
   private RefTaskSubclassKey taskSubclass;
   private RefTaskMustRemoveKey mustRemove = RefTaskMustRemoveKey.NA;
   private String taskCode = "REQ";
   private TaskDefnKey taskDefn;
   private RefTaskDefinitionStatusKey taskStatus = RefTaskDefinitionStatusKey.ACTV;
   private double workPerformedSchedHours;
   private RefReschedFromKey reschedFrom;
   private OrgKey organization;
   private boolean isWorkscoped = false;
   private boolean isCreateOnAcInst;
   private boolean isCreateOnAnyInst;
   private boolean isCreateOnRemove;
   private boolean isCancelOnInstallOnAircraft;
   private boolean isCancelOnInstallOnAny;
   private boolean isCancelOnRemoveFromAircraft;
   private boolean isCnacelOnRemoveFromAny;
   private EqpPlanningTypeKey planningType;
   private String revisionNote;
   private RefTaskRevReasonKey revisionReason;
   private boolean isSchedulingRuleUsedWhenCreatedFromAnotherTask;
   private String instructions;


   /**
    * {@inheritDoc}
    */
   @Override
   public TaskTaskKey build() {
      if ( taskDefn == null ) {
         taskDefn = new TaskDefnBuilder().build();
      }

      TaskTaskKey taskRevision = TaskTaskTable.generatePrimaryKey();
      TaskTaskTable table = TaskTaskTable.create( taskRevision );
      table.setTaskDefn( taskDefn );
      table.setTaskDefStatus( taskStatus );
      table.setTaskClass( taskClass );
      table.setTaskSubclass( taskSubclass );
      table.setTaskCd( taskCode );
      table.setOnConditionBool( isOnCondition );
      table.setRecurringTaskBool( isRecurring );
      table.setUniqueBool( isUnique );
      table.setBlockChainSdesc( blockChainDesc );
      table.setManualSchedulingBool( manualScheduling );
      table.setTaskApplEffLdesc( applicabilityRange );
      table.setTaskApplSqlLdesc( applicabilityRule );
      table.setOrganization( organization );
      table.setWorkscopeBool( isWorkscoped );
      table.setCreateOnAcInstBool( isCreateOnAcInst );
      table.setCreateOnAnyInstBool( isCreateOnAnyInst );
      table.setCreateOnAnyRemovalBool( isCreateOnRemove );
      table.setCancelOnAcInstBool( isCancelOnInstallOnAircraft );
      table.setCancelOnAcRemovalBool( isCancelOnRemoveFromAircraft );
      table.setCancelOnAnyInstBool( isCancelOnInstallOnAny );
      table.setCancelOnAnyRemovalBool( isCnacelOnRemoveFromAny );
      table.setTaskMustRemove( mustRemove );
      table.setPlanningType( planningType );
      table.setSchedulingRuleUsedWhenCreatedFromAnotherTask(
            isSchedulingRuleUsedWhenCreatedFromAnotherTask );

      if ( configSlotBuilder != null ) {
         table.setBomItem( configSlotBuilder.build() );
      }

      if ( scheduleFrom != null ) {
         table.setScheduleFrom( scheduleFrom );
         if ( EFFECTIVE_DT.equals( scheduleFrom ) && ( effectiveFromDate != null ) ) {
            table.setEffectiveDt( effectiveFromDate );
            table.setEffectiveGdt( effectiveFromDate );
         }
      }

      // if revision number explicitly set then use it, otherwise use the incremented revision
      // number based on task defn
      Integer revision = revisionNumber;
      if ( revision == null ) {

         if ( !revisionMap.containsKey( taskDefn ) ) {
            revisionMap.put( taskDefn, new Integer( 1 ) );
         } else {
            int ord = revisionMap.get( taskDefn );
            revisionMap.put( taskDefn, new Integer( ord + 1 ) );
         }

         revision = revisionMap.get( table.getTaskDefn() );
      }

      if ( reschedFrom != null ) {
         table.setReschedFrom( reschedFrom );
      }

      table.setRevisionOrd( revision );
      table.setTaskRefSdesc( revisionNote );
      table.setTaskRevReason( revisionReason );
      if ( instructions != null ) {
         table.setInstructionLdesc( instructions );
      }

      table.insert();

      TaskDefnTable taskDefnTable = TaskDefnTable.findByPrimaryKey( table.getTaskDefn() );
      taskDefnTable.setLastRevisionOrd( table.getRevisionOrd() );
      taskDefnTable.update();

      if ( TASK_CLASS_REF.equals( taskClass ) ) {
         TaskRefDocTable.create( new TaskRefDocKey( taskRevision ) ).insert();

         if ( !RefTaskClass.findByPrimaryKey( taskClass ).exists() ) {
            RefTaskClass refTaskClass = RefTaskClass.create();
            refTaskClass.setClassModeCd( ClassMode.REF.name() );
            refTaskClass.insert( taskClass );
         }
      } else if ( TASK_CLASS_BLOCK.equals( taskClass )
            && !RefTaskClass.findByPrimaryKey( TASK_CLASS_BLOCK ).exists() ) {
         RefTaskClass refTaskClass = RefTaskClass.create();
         refTaskClass.setClassModeCd( ClassMode.BLOCK.name() );
         refTaskClass.insert( taskClass );
      }

      if ( isNextShopVisit ) {
         TaskTaskFlagsTable taskFlags =
               TaskTaskFlagsTable.findByPrimaryKey( new TaskTaskFlagsKey( taskRevision ) );
         taskFlags.setNSVBool( true );
         taskFlags.update();
      }

      int dependencyId = 0;
      for ( DomainBuilder<TaskDefnKey> taskDefn : compliesWith ) {
         TaskTaskDepTable depTable =
               TaskTaskDepTable.create( new TaskTaskDepKey( taskRevision, dependencyId++ ) );
         depTable.setTaskDepAction( RefTaskDepActionKey.COMPLIES );
         depTable.setDepTaskDefn( taskDefn.build() );
         depTable.insert();
      }

      for ( TaskDefnKey reqDefn : requirements ) {
         new BlockReqMapBuilder().from( taskRevision ).to( reqDefn ).build();
      }

      if ( !partKeyList.isEmpty() ) {

         if ( configSlotBuilder == null ) {

            for ( PartNoKey partNoKey : partKeyList ) {

               // create a task definition part based only
               TaskPartMapKey taskPartMapKey = new TaskPartMapKey( taskRevision, partNoKey );
               TaskPartMapTable taskPartMapTable = TaskPartMapTable.create( taskPartMapKey );
               taskPartMapTable.insert();
            }
         } else {

            // cannot have a task definition based on part and config slot at the same time
            throw new MxRuntimeException(
                  "Cannot create a task definition based on a part and config slot at the same time" );
         }
      }

      // the task has labour requirement
      if ( labourSkill != null ) {

         TaskLabourList taskLabourList = TaskLabourList.create( taskRevision, labourSkill );
         taskLabourList.setManPwrCt( peopleRequired );

         // technician work is set as default
         taskLabourList.setWorkPerf( true );
         taskLabourList.setWorkPerfHr( workPerformedSchedHours );

         // when certification is required
         if ( certificationSchedHours != 0 ) {
            taskLabourList.setCert( true );
            taskLabourList.setCertHr( certificationSchedHours );
         }

         // when inspection is required
         if ( indepInspectionSchedHours != 0 ) {
            taskLabourList.setInsp( true );
            taskLabourList.setInspHr( indepInspectionSchedHours );
         }

         taskLabourList.insert();
      }

      return taskRevision;
   }


   /**
    * Sets the compliance
    *
    * @param taskDefn
    *           the task definition
    *
    * @return the builder
    */
   public TaskRevisionBuilder compliesWith( TaskDefnKey taskDefn ) {
      compliesWith.add( new TestDataBuilderStub<TaskDefnKey>( taskDefn ) );

      return this;
   }


   /**
    * Sets the organization
    *
    * @param organization
    *           the organization
    *
    * @return the builder
    */
   public TaskRevisionBuilder forOrganization( OrgKey organization ) {
      this.organization = organization;

      return this;
   }


   @Override
   public RefTaskClassKey getTaskClass() {
      return taskClass;
   }


   /**
    * Sets the task subclass
    *
    * @param taskSubclass
    *           the task subclass
    *
    * @return the builder
    */
   public TaskRevisionBuilder withTaskSubclass( RefTaskSubclassKey taskSubclass ) {
      this.taskSubclass = taskSubclass;

      return this;
   }


   public TaskRevisionBuilder isExecutableRequirement() {

      withTaskClass( RefTaskClassKey.REQ );
      isWorkscoped();

      return this;
   }


   public TaskRevisionBuilder isMandatory() {
      isOnCondition = false;

      return this;
   }


   public TaskRevisionBuilder isNextShopVisit() {
      isNextShopVisit = true;

      return this;
   }


   public TaskRevisionBuilder isOnCondition() {
      isOnCondition = true;

      return this;
   }


   public TaskRevisionBuilder setOnCondition( boolean onCondition ) {
      isOnCondition = onCondition;

      return this;
   }


   public TaskRevisionBuilder setMustRemove( RefTaskMustRemoveKey mustRemove ) {
      this.mustRemove = mustRemove;

      return this;
   }


   /**
    * Sets the recurring flag to true
    *
    * @return the build
    */
   public TaskRevisionBuilder isRecurring() {
      isRecurring = true;

      return this;
   }


   /**
    * Sets the task definition to be schedule from the provided effective date. (sets both the
    * scheduled from and the effective dates attributes)
    *
    * @param effectiveDate
    *           effective date (optional, may be null)
    *
    * @return the builder
    */
   public TaskRevisionBuilder isScheduledFromEffectiveDate( Date effectiveDate ) {

      scheduleFrom = RefTaskSchedFromKey.EFFECTIVE_DT;
      effectiveFromDate = effectiveDate;

      return this;
   }


   /**
    * Sets the task definition to be schedule from the inventory's manufactured date.
    *
    * @return the builder
    */
   public TaskRevisionBuilder isScheduledFromManufacturedDate() {

      scheduleFrom = RefTaskSchedFromKey.MANUFACT_DT;

      return this;
   }


   /**
    * Sets the task definition to be schedule from the inventory's received date.
    *
    * @return the builder
    */
   public TaskRevisionBuilder isScheduledFromRecievedDate() {

      scheduleFrom = RefTaskSchedFromKey.RECEIVED_DT;

      return this;
   }


   public TaskRevisionBuilder isScheduledManually() {
      manualScheduling = true;

      return this;
   }


   /**
    * Sets the unique flag to true
    *
    * @return the build
    */
   public TaskRevisionBuilder isUnique() {
      isUnique = true;

      return this;
   }


   public TaskRevisionBuilder setUnique( boolean unique ) {
      isUnique = unique;

      return this;
   }


   /**
    * Sets the CreateOnAnyInst flag to true
    *
    * @return the build
    */
   public TaskRevisionBuilder isCreateOnAnyInst() {
      isCreateOnAnyInst = true;

      return this;
   }


   public TaskRevisionBuilder isCreateOnRemove() {
      isCreateOnRemove = true;

      return this;
   }


   /**
    * Sets the CreateOnAcInst flag to true
    *
    * @return the build
    */
   public TaskRevisionBuilder isCreateOnAcInst() {
      isCreateOnAcInst = true;

      return this;
   }


   public TaskRevisionBuilder isCancelOnInstallOnAircraft() {
      this.isCancelOnInstallOnAircraft = true;

      return this;
   }


   public TaskRevisionBuilder isCancelOnInstallOnAnyComponent() {
      this.isCancelOnInstallOnAny = true;

      return this;
   }


   public TaskRevisionBuilder isCancelOnRemoveFromAircraft() {
      this.isCancelOnRemoveFromAircraft = true;

      return this;
   }


   public TaskRevisionBuilder isCancelOnRemoveFromAnyComponent() {
      this.isCnacelOnRemoveFromAny = true;

      return this;
   }


   public TaskRevisionBuilder isWorkscoped() {
      isWorkscoped = true;

      return this;
   }


   /**
    * Maps this task definition revision to a requirement definition. This should only be used with
    * blocks.
    *
    * @param reqDefn
    *           The requirement definition.
    *
    * @return The builder
    */
   public TaskRevisionBuilder mapToRequirement( TaskDefnKey reqDefn ) {
      requirements.add( reqDefn );

      return this;
   }


   /**
    * Sets the applicability range
    *
    * @param applicabilityRange
    *           the applicability range
    *
    * @return the builder
    */
   public TaskRevisionBuilder withApplicabilityRange( String applicabilityRange ) {
      this.applicabilityRange = applicabilityRange;

      return this;
   }


   public TaskRevisionBuilder withRescheduleFrom( RefReschedFromKey refReschedFromKey ) {

      reschedFrom = refReschedFromKey;

      return this;
   }


   /**
    * Sets the applicability rule
    *
    * @param applicabilityRule
    *           the applicability rule
    *
    * @return the builder
    */
   public TaskRevisionBuilder withApplicabilityRule( String applicabilityRule ) {
      this.applicabilityRule = applicabilityRule;

      return this;
   }


   /**
    * Sets the block chain description
    *
    * @param blockChainDesc
    *           the block chain description
    *
    * @return the builder
    */
   public TaskRevisionBuilder withBlockChainDesc( String blockChainDesc ) {
      this.blockChainDesc = blockChainDesc;

      return this;
   }


   /**
    * Sets the configuration slot
    *
    * @param configSlot
    *           the configuration slot
    *
    * @return the builder
    */
   public TaskRevisionBuilder withConfigSlot( ConfigSlotKey configSlot ) {
      return withConfigSlot( new TestDataBuilderStub<ConfigSlotKey>( configSlot ) );
   }


   /**
    * Sets the configuration slot
    *
    * @param configSlotBuilder
    *           the configuration slot builder
    *
    * @return the builder
    */
   public TaskRevisionBuilder withConfigSlot( DomainBuilder<ConfigSlotKey> configSlotBuilder ) {
      this.configSlotBuilder = configSlotBuilder;

      return this;
   }


   /**
    * Add a labour requirement to task defn
    *
    * @param labourSkill
    *           the labour skill key
    * @param peopleRequired
    *           number of people required
    * @param workPerformedSchedHours
    *           work performed sched hours
    *
    * @return the builder
    */
   public TaskRevisionBuilder withLabourRequirement( RefLabourSkillKey labourSkill,
         int peopleRequired, double workPerformedSchedHours ) {

      return withLabourRequirement( labourSkill, peopleRequired, workPerformedSchedHours, 0, 0 );
   }


   /**
    * Add a labour requirement to task defn
    *
    * @param labourSkill
    *           the labour skill key
    * @param peopleRequired
    *           number of people required
    * @param workPerformedSchedHours
    *           work performed sched hours
    * @param certificationSchedHours
    *           certification sched hours
    * @param indepInspectionSchedHours
    *           independent inspection sched hours
    *
    * @return the builder
    */
   public TaskRevisionBuilder withLabourRequirement( RefLabourSkillKey labourSkill,
         int peopleRequired, double workPerformedSchedHours, double certificationSchedHours,
         double indepInspectionSchedHours ) {
      this.labourSkill = labourSkill;
      this.peopleRequired = peopleRequired;
      this.workPerformedSchedHours = workPerformedSchedHours;
      this.certificationSchedHours = certificationSchedHours;
      this.indepInspectionSchedHours = indepInspectionSchedHours;

      return this;
   }


   /**
    * Set the Part key if the task is part based
    *
    * @param partKeyList
    *           Part Key
    *
    * @return the builder
    */
   public TaskRevisionBuilder withPart( List<PartNoKey> partKeyList ) {
      this.partKeyList = partKeyList;

      return this;
   }


   /**
    * Sets the revision number
    *
    * @param revisionNumber
    *           the revision number
    *
    * @return the builder
    */
   public TaskRevisionBuilder withRevisionNumber( Integer revisionNumber ) {
      this.revisionNumber = revisionNumber;

      return this;
   }


   /**
    * Sets the task status
    *
    * @param taskStatus
    *           the task status
    *
    * @return the builder
    */
   public TaskRevisionBuilder withStatus( RefTaskDefinitionStatusKey taskStatus ) {
      this.taskStatus = taskStatus;

      return this;
   }


   /**
    * Sets the task class
    *
    * @param taskClass
    *           the task class
    *
    * @return the builder
    */
   public TaskRevisionBuilder withTaskClass( RefTaskClassKey taskClass ) {
      this.taskClass = taskClass;

      return this;
   }


   /**
    * Sets the task code
    *
    * @param taskCode
    *           the task code
    *
    * @return the builder
    */
   public TaskRevisionBuilder withTaskCode( String taskCode ) {
      this.taskCode = taskCode;

      return this;
   }


   /**
    * Sets the task definition builder
    *
    * @param taskDefn
    *           the task definition
    *
    * @return the builder
    */
   public TaskRevisionBuilder withTaskDefn( TaskDefnKey taskDefn ) {
      this.taskDefn = taskDefn;

      return this;
   }


   public TaskRevisionBuilder withPlanningType( EqpPlanningTypeKey planningType ) {
      this.planningType = planningType;

      return this;
   }


   /**
    * Sets the isWorkscoped property
    *
    * @param workscope
    *           the workscope value
    *
    * @return the builder
    */
   public TaskRevisionBuilder withWorkscope( boolean workscope ) {
      isWorkscoped = workscope;

      return this;
   }


   public TaskRevisionBuilder withRevisionReason( RefTaskRevReasonKey revisionReason ) {
      this.revisionReason = revisionReason;

      return this;
   }


   public TaskRevisionBuilder withRevisionNote( String revisionNote ) {
      this.revisionNote = revisionNote;

      return this;
   }


   public TaskRevisionBuilder withInstructions( String instructions ) {
      this.instructions = instructions;
      return this;
   }


   /**
    * Builds a <code>task_defn</code> object
    */
   private static class TaskDefnBuilder implements DomainBuilder<TaskDefnKey> {

      /**
       * {@inheritDoc}
       */
      @Override
      public TaskDefnKey build() {
         TaskDefnTable table = TaskDefnTable.create( TaskDefnTable.generatePrimaryKey() );

         return table.insert();
      }
   }


   public TaskRevisionBuilder setIsSchedulingRuleUsedWhenCreatedFromAnotherTask(
         boolean schedulingRuleUsedWhenCreatedFromAnotherTask ) {
      isSchedulingRuleUsedWhenCreatedFromAnotherTask = schedulingRuleUsedWhenCreatedFromAnotherTask;
      return this;

   }
}
