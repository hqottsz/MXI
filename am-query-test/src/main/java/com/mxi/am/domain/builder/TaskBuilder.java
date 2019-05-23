package com.mxi.am.domain.builder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.DepartmentKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefEventTypeKey;
import com.mxi.mx.core.key.RefLabourRoleStatusKey;
import com.mxi.mx.core.key.RefLabourRoleTypeKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefLabourStageKey;
import com.mxi.mx.core.key.RefRelationTypeKey;
import com.mxi.mx.core.key.RefSchedPriorityKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.SchedLabourKey;
import com.mxi.mx.core.key.SchedLabourRoleKey;
import com.mxi.mx.core.key.SchedStepKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskStepKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.evt.EvtDept;
import com.mxi.mx.core.table.evt.EvtEventRel;
import com.mxi.mx.core.table.sched.JdbcSchedStaskDao;
import com.mxi.mx.core.table.sched.SchedLabourRoleStatusTable;
import com.mxi.mx.core.table.sched.SchedLabourRoleTable;
import com.mxi.mx.core.table.sched.SchedLabourTable;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.table.sched.SchedStepDao;
import com.mxi.mx.core.table.sched.SchedStepTableRow;
import com.mxi.mx.core.table.task.TaskTaskTable;
import com.mxi.mx.core.task.model.StepStatus;
import com.mxi.mx.persistence.uuid.SequentialUuidGenerator;


/**
 * Builds an <code>sched_stask</code> object
 */
public class TaskBuilder implements DomainBuilder<TaskKey> {

   private String iBarcode;

   private String iDescription;

   private TaskDeadlineBuilder iDeadlineBuilder;

   private boolean iHistoric = false;

   private FncAccountKey iIssueAccount;

   private List<LabourRow> iLabourRows = new ArrayList<TaskBuilder.LabourRow>();

   private LocationKey iLocation;

   private DomainBuilder<InventoryKey> iMainInventory = new InventoryBuilder();

   private String iName;

   private TaskKey iParentTask;

   private boolean iPartsReady = true;

   private boolean iPreventDeadlineSync;

   private TaskKey iPrevTask;

   private TaskKey iDupJicSchedKey;

   private Date iSchedEndDt;

   private double iSchedHoursMultiplier = 0;

   private Date iSchedStartDt;

   private RefEventStatusKey iStatus = RefEventStatusKey.ACTV;

   private RefTaskClassKey iTaskClass;

   private DomainBuilder<TaskTaskKey> iTaskRevisionBuilder = new NullTestDataBuilder<TaskTaskKey>();

   private boolean iToolsReady = true;

   private String iWorkOrderNumber;

   private boolean iOrphanedForecast = false;

   private TaskKey iComponentWorkPackage;

   private boolean iOverdue;

   private FaultKey iFault;

   private TaskKey iHighestTask;

   private DepartmentKey iCrew;

   private boolean iEtopsSignificant;

   private static boolean iRequestedParts;


   /**
    * Sets the task to historic
    *
    * @return the builder
    */
   public TaskBuilder asHistoric() {
      iHistoric = true;

      return this;
   }


   /**
    * Sets the task status to historic with a specific status
    *
    * @param aStatus
    *           the task status
    *
    * @return the builder
    */
   public TaskBuilder asHistoric( RefEventStatusKey aStatus ) {
      iStatus = aStatus;

      return asHistoric();
   }


   /**
    * Sets the location of the task.
    *
    * @param aLocation
    *           The task location.
    *
    * @return The builder.
    */
   public TaskBuilder atLocation( LocationKey aLocation ) {
      iLocation = aLocation;

      return this;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public TaskKey build() {
      EventBuilder lEventBuilder = new EventBuilder();
      lEventBuilder.withType( RefEventTypeKey.TS ).withDescription( iDescription )
            .withStatus( iStatus ).withScheduledStart( iSchedStartDt )
            .withScheduledEnd( iSchedEndDt );

      if ( iHistoric ) {
         lEventBuilder.asHistoric();
      }

      if ( iParentTask != null ) {
         lEventBuilder.withParentEvent( iParentTask.getEventKey() );
      }

      InventoryKey lMainInventory = iMainInventory.build();
      lEventBuilder.onInventory( lMainInventory );

      if ( iLocation != null ) {
         lEventBuilder.atLocation( iLocation );
      }

      if ( iDescription != null ) {
         lEventBuilder.withDescription( iDescription );
      } else {
         lEventBuilder.withDescription( iName );
      }

      lEventBuilder
            .withSchedPriority( iOverdue ? RefSchedPriorityKey.OD : RefSchedPriorityKey.NONE );

      EventKey lEvent = lEventBuilder.build();

      TaskKey lTask = new TaskKey( lEvent.getDbId(), lEvent.getId() );
      SchedStaskTable lTable = SchedStaskTable.create( lTask );
      lTable.setTaskTaskKey( iTaskRevisionBuilder.build() );
      lTable.setBarcode( iBarcode );
      lTable.setAlternateKey( new SequentialUuidGenerator().newUuid() );
      lTable.setPreventDeadlineSync( iPreventDeadlineSync );
      lTable.setPartsReady( iPartsReady );
      lTable.setToolsReady( iToolsReady );
      lTable.setWoRefSdesc( iWorkOrderNumber );
      lTable.setIssueAccount( iIssueAccount );
      lTable.setOrphanFrctBool( iOrphanedForecast );
      lTable.setSoftDeadlineBool( false );
      lTable.setDupJicSchedKey( iDupJicSchedKey );
      lTable.setFault( iFault );
      lTable.setHTaskKey( iHighestTask == null ? lTask : iHighestTask );
      lTable.setETOPS( iEtopsSignificant );
      lTable.setRequestPartsBool( iRequestedParts );

      if ( iTaskClass != null ) {
         lTable.setTaskClass( iTaskClass );
         if ( iTaskClass.equals( RefTaskClassKey.REPL ) && iComponentWorkPackage != null ) {
            EvtEventRel.create( lTask.getEventKey(), iComponentWorkPackage.getEventKey(),
                  RefRelationTypeKey.WORMVL );
         }
      } else if ( iTaskRevisionBuilder instanceof HasTaskClass ) {
         lTable.setTaskClass( ( ( HasTaskClass ) iTaskRevisionBuilder ).getTaskClass() );
      } else {
         lTable.setTaskClass( RefTaskClassKey.REQ );
      }

      lTable.setMainInventory( lMainInventory );

      if ( iSchedHoursMultiplier != 0 ) {
         lTable.setSchedHrMultQt( iSchedHoursMultiplier );
      }

      lTable.insert();

      if ( iPrevTask != null ) {
         EvtEventRel.create( iPrevTask.getEventKey(), lTask.getEventKey(),
               RefRelationTypeKey.DEPT );
      }

      if ( iDeadlineBuilder != null ) {
         iDeadlineBuilder.withTask( lTask ).build();
      }

      // the task has labour rows
      if ( !iLabourRows.isEmpty() ) {

         for ( LabourRow lLabourRow : iLabourRows ) {

            SchedLabourTable lSchedLabourTable = SchedLabourTable.create();
            lSchedLabourTable.setTask( lTask );
            lSchedLabourTable.setLabourSkill( lLabourRow.iLabourSkill );
            lSchedLabourTable.setCurrentStatusOrder( 1 );

            // technician work is set as default
            lSchedLabourTable.setWorkPerformed( true );

            // set the labour stage to active by default
            lSchedLabourTable.setLabourStage( lLabourRow.iLabourStage );

            // create a record in the sched_labour table
            SchedLabourKey lSchedLabourKey = lSchedLabourTable.insert();

            // create a record in the sched_labour_role table
            setScheduleLabourRole( lSchedLabourKey, RefLabourRoleTypeKey.TECH,
                  lLabourRow.iWorkHours );

            // when certification is required
            if ( lLabourRow.iCertifyHours != 0 ) {

               // update sched_labour table with the cert_bool = true
               lSchedLabourTable.setCertification( true );
               lSchedLabourTable.update();

               // create a record in the sched_labour_role table
               setScheduleLabourRole( lSchedLabourKey, RefLabourRoleTypeKey.CERT,
                     lLabourRow.iCertifyHours );
            }

            // when inspection is required
            if ( lLabourRow.iInspectHours != 0 ) {

               // update sched_labour table with insp_bool = true
               lSchedLabourTable.setIndependentInspection( true );
               lSchedLabourTable.update();

               // create a record in the sched_labour_role table
               setScheduleLabourRole( lSchedLabourKey, RefLabourRoleTypeKey.INSP,
                     lLabourRow.iInspectHours );
            }

         }
      }

      if ( iCrew != null ) {
         EvtDept.create( lTask.getEventKey(), iCrew );
      }

      addSteps( lTask );

      return lTask;
   }


   /**
    * Sets the parts ready boolean to false.
    *
    * @return the builder
    */
   public TaskBuilder hasPartsNotReady() {
      iPartsReady = false;

      return this;
   }


   /**
    * Sets the parts ready boolean to true.
    *
    * @return the builder
    */
   public TaskBuilder hasPartsReady() {
      iPartsReady = true;

      return this;
   }


   /**
    * Sets the tools ready boolean to false.
    *
    * @return the builder
    */
   public TaskBuilder hasToolsNotReady() {
      iToolsReady = false;

      return this;
   }


   /**
    * Sets the tools ready boolean to true.
    *
    * @return the builder
    */
   public TaskBuilder hasToolsReady() {
      iToolsReady = true;

      return this;
   }


   /**
    * Sets the orphaned forecast flag
    *
    * @param aOrphandedForecast
    *           the orphaned forecast flag
    *
    * @return the builder
    */
   public TaskBuilder isOrphanedForecast( boolean aOrphanedForecast ) {
      iOrphanedForecast = aOrphanedForecast;

      return this;
   }


   /**
    * Sets the inventory that the task is on
    *
    * @param aInventory
    *           the inventory
    *
    * @return the builder
    */
   public TaskBuilder onInventory( InventoryKey aInventory ) {
      iMainInventory = new TestDataBuilderStub<>( aInventory );

      return this;
   }


   /**
    * Sets the barcode
    *
    * @param aBarcode
    *           The barcode
    *
    * @return The builder
    */
   public TaskBuilder withBarcode( String aBarcode ) {
      iBarcode = aBarcode;

      return this;
   }


   /**
    * Add a completed labour row to task
    *
    * @param aLabourSkill
    *           the labour skill key
    * @param aHoursWorked
    *           number of hours worked
    *
    * @return the builder
    */
   public TaskBuilder withCompletedLabour( RefLabourSkillKey aLabourSkill, double aHoursWorked ) {
      iLabourRows.add(
            new LabourRow( aLabourSkill, RefLabourStageKey.COMPLETE, aHoursWorked, 0.0, 0.0 ) );

      return this;
   }


   public TaskBuilder withCrew( DepartmentKey aDepartment ) {
      iCrew = aDepartment;

      return this;
   }


   /**
    * The deadline date
    *
    * @param aDueDate
    *           The due date
    *
    * @return The builder
    */
   public TaskBuilder withCalendarDeadline( Date aDueDate ) {
      iDeadlineBuilder = new TaskDeadlineBuilder().withDueDate( aDueDate );

      return this;
   }


   /**
    * Sets the description of the task.
    *
    * @param aDescription
    *           The task description
    *
    * @return the builder
    */
   public TaskBuilder withDescription( String aDescription ) {
      iDescription = aDescription;

      return this;
   }


   /**
    * Add an Issue Account to task.
    *
    * @param aIssueAccount
    *           the issue account key
    *
    * @return the builder
    */
   public TaskBuilder withIssueAccount( FncAccountKey aIssueAccount ) {
      iIssueAccount = aIssueAccount;

      return this;
   }


   /**
    * Add a labour row to task
    *
    * @param aLabourSkill
    *           the labour skill key
    * @param aWorkPerformedSchedHours
    *           work performed sched hours
    *
    * @return the builder
    */
   public TaskBuilder withLabour( RefLabourSkillKey aLabourSkill,
         double aWorkPerformedSchedHours ) {
      return withLabour( aLabourSkill, aWorkPerformedSchedHours, 0, 0 );
   }


   /**
    * Add a labour row to task
    *
    * @param aLabourSkill
    *           the labour skill key
    * @param aHoursWorked
    *           work performed sched hours
    * @param aCertifyHours
    *           certification sched hours
    * @param aInspectHours
    *           independent inspection sched hours
    *
    * @return the builder
    */
   public TaskBuilder withLabour( RefLabourSkillKey aLabourSkill, double aHoursWorked,
         double aCertifyHours, double aInspectHours ) {
      iLabourRows.add( new LabourRow( aLabourSkill, RefLabourStageKey.ACTV, aHoursWorked,
            aCertifyHours, aInspectHours ) );

      return this;
   }


   /**
    * Prevents deadline synchronization for this task instance.
    *
    * @return the builder
    */
   public TaskBuilder withManualSchedulingEnabled() {
      iPreventDeadlineSync = true;

      return this;
   }


   /**
    * Sets the task name
    *
    * @param aName
    *           The description
    *
    * @return The builder
    */
   public TaskBuilder withName( String aName ) {
      iName = aName;

      return this;
   }


   /**
    * Sets the parent task
    *
    * @param aParentTask
    *           The parent task
    *
    * @return The builder
    */
   public TaskBuilder withParentTask( TaskKey aParentTask ) {
      iParentTask = aParentTask;

      return this;
   }


   /**
    * Sets the highest task. E.g. Set JIC's highest task as its Requirement
    *
    * @param aHighestTask
    *           The highest task
    *
    * @return The builder
    */
   public TaskBuilder withHighestTask( TaskKey aHighestTask ) {
      iHighestTask = aHighestTask;

      return this;
   }


   /**
    * Sets the previous task
    *
    * @param aLastTask
    *           the previous task
    *
    * @return the builder
    */
   public TaskBuilder withPrevTask( TaskKey aLastTask ) {
      iPrevTask = aLastTask;

      return this;
   }


   /**
    *
    * Sets the key of the suppressing task (indicating this task is suppressed)
    *
    * @param aSupressingTask
    * @return
    */
   public TaskBuilder withSuppressingTask( TaskKey aSupressingTask ) {
      iDupJicSchedKey = aSupressingTask;
      return this;
   }


   /**
    * The schedule hours multiplier
    *
    * @param aSchedHoursMultiplier
    *           The schedule hours multiplier
    *
    * @return The builder
    */
   public TaskBuilder withSchedHoursMultiplier( double aSchedHoursMultiplier ) {
      iSchedHoursMultiplier = aSchedHoursMultiplier;

      return this;
   }


   /**
    * Sets the scheduled end date.
    *
    * @param aSchedEndDt
    *           The scheduled end date
    *
    * @return The builder
    */
   public TaskBuilder withScheduledEnd( Date aSchedEndDt ) {
      iSchedEndDt = aSchedEndDt;

      return this;
   }


   /**
    * Sets the scheduled start date.
    *
    * @param aSchedStartDt
    *           The scheduled start date
    *
    * @return The builder
    */
   public TaskBuilder withScheduledStart( Date aSchedStartDt ) {
      iSchedStartDt = aSchedStartDt;

      return this;
   }


   /**
    * Sets the task status to historic with a specific status
    *
    * @param aStatus
    *           the task status
    *
    * @return the builder
    */
   public TaskBuilder withStatus( RefEventStatusKey aStatus ) {
      iStatus = aStatus;

      return this;
   }


   /**
    * Sets the class of the task.
    *
    * @param aTaskClass
    *           The task class
    *
    * @return the builder
    */
   public TaskBuilder withTaskClass( RefTaskClassKey aTaskClass ) {
      iTaskClass = aTaskClass;

      return this;
   }


   /**
    * Sets the task revision
    *
    * @param aTaskRevisionBuilder
    *           the task revision builder
    *
    * @return the builder
    */
   public TaskBuilder withTaskRevision( DomainBuilder<TaskTaskKey> aTaskRevisionBuilder ) {
      iTaskRevisionBuilder = aTaskRevisionBuilder;

      return this;
   }


   /**
    * Sets the task revision
    *
    * @param aTaskRevision
    *           the task revision
    *
    * @return the builder
    */
   public TaskBuilder withTaskRevision( TaskTaskKey aTaskRevision ) {
      return withTaskRevision( new TestDataBuilderStub<TaskTaskKey>( aTaskRevision ) );
   }


   /**
    * Sets the Request Parts Boolean.
    *
    * @param aRequestParts
    *           Boolean for Requested Parts
    *
    * @return The builder
    */
   public TaskBuilder withRequestParts( Boolean aRequestParts ) {
      iRequestedParts = aRequestParts;

      return this;
   }


   /**
    * Sets the work order number.
    *
    * @param aWorkOrderNumber
    *           The work order number
    *
    * @return The builder
    */
   public TaskBuilder withWorkOrderNumber( String aWorkOrderNumber ) {
      iWorkOrderNumber = aWorkOrderNumber;

      return this;
   }


   /**
    * Create a schedule labour role record in the sched_labour_role and sched_labour_role_status
    * tables.
    *
    * @param aSchedLabourKey
    *           the schedule labour role key
    * @param aRefLabourRoleTypeKey
    *           the labour role type key
    * @param aSchedHours
    *           scheduled hours
    */
   private void setScheduleLabourRole( SchedLabourKey aSchedLabourKey,
         RefLabourRoleTypeKey aRefLabourRoleTypeKey, double aSchedHours ) {
      SchedLabourRoleTable lSchedLabourRoleTable = SchedLabourRoleTable.create();
      lSchedLabourRoleTable.setSchedLabour( aSchedLabourKey );
      lSchedLabourRoleTable.setLabourRoleType( aRefLabourRoleTypeKey );
      lSchedLabourRoleTable.setSchedHours( aSchedHours );

      SchedLabourRoleKey lSchedLabourRole = lSchedLabourRoleTable.insert();

      SchedLabourRoleStatusTable lSchedLabourRoleStatusTable = SchedLabourRoleStatusTable.create();
      lSchedLabourRoleStatusTable.setSchedLabourRole( lSchedLabourRole );
      lSchedLabourRoleStatusTable.setStatusOrder( 1 );
      lSchedLabourRoleStatusTable.setLabourRoleStatus( RefLabourRoleStatusKey.ACTV );
      lSchedLabourRoleStatusTable.insert();
   }


   /**
    * Pojo to hold labour row information.
    */
   private class LabourRow {

      protected double iCertifyHours = 0;
      protected double iInspectHours = 0;
      protected RefLabourSkillKey iLabourSkill;
      protected RefLabourStageKey iLabourStage = RefLabourStageKey.ACTV;
      protected double iWorkHours = 1.0;


      /**
       * Creates a new {@linkplain LabourRow} object.
       *
       * @param aLabourSkill
       *           DOCUMENT_ME
       * @param aLabourStage
       *           DOCUMENT_ME
       * @param aWorkHours
       *           DOCUMENT_ME
       * @param aCertifyHours
       *           DOCUMENT_ME
       * @param aInspectHours
       *           DOCUMENT_ME
       */
      protected LabourRow(RefLabourSkillKey aLabourSkill, RefLabourStageKey aLabourStage,
            double aWorkHours, double aCertifyHours, double aInspectHours) {
         iLabourSkill = aLabourSkill;
         iLabourStage = aLabourStage;
         iWorkHours = aWorkHours;
         iCertifyHours = aCertifyHours;
         iInspectHours = aInspectHours;
      }
   }


   public TaskBuilder withComponentWorkPackage( TaskKey aCheck ) {
      iComponentWorkPackage = aCheck;

      return this;
   }


   public TaskBuilder withUsageDeadline( DataTypeKey aUsageType, double aUsageDue, Date aDueDate ) {
      iDeadlineBuilder = new TaskDeadlineBuilder().withDataType( aUsageType )
            .withDueQuantity( aUsageDue ).withDueDate( aDueDate );

      return this;
   }


   public TaskBuilder asOverdue() {
      iOverdue = true;
      return this;
   }


   public TaskBuilder withFault( FaultKey aFault ) {
      iFault = aFault;
      return this;
   }


   public TaskBuilder withEtopsSignificant( boolean aEtopsSignificant ) {
      iEtopsSignificant = aEtopsSignificant;
      return this;
   }


   private static void addSteps( TaskKey aTask ) {

      TaskTaskKey lTaskTask = new JdbcSchedStaskDao().findByPrimaryKey( aTask ).getTaskTaskKey();

      if ( lTaskTask == null ) {
         return;
      }

      TaskTaskTable lTaskTaskTable = TaskTaskTable.findByPrimaryKey( lTaskTask );

      if ( lTaskTaskTable.getTaskClass() == null ) {
         return;
      }

      SchedStepDao schedStepDao = InjectorContainer.get().getInstance( SchedStepDao.class );
      // if this is an executable requirement, then copy the task steps over to the actual
      if ( ( RefTaskClassKey.REQ_CLASS_MODE_CD.equals( lTaskTaskTable.getTaskClassModeCd() )
            && lTaskTaskTable.isWorkscopeBool() )
            || RefTaskClassKey.JIC_CLASS_MODE_CD.equals( lTaskTaskTable.getTaskClassModeCd() ) ) {

         DataSetArgument lArgs = new DataSetArgument();
         lArgs.add( lTaskTask, "task_db_id", "task_id" );

         QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "TASK_STEP", lArgs, "step_ord",
               "task_db_id", "task_id", "step_id" );

         while ( lQs.next() ) {

            SchedStepKey schedStepKey = schedStepDao.generatePrimaryKey( aTask );

            SchedStepTableRow row = schedStepDao.create( schedStepKey );
            row.setStepOrd( lQs.getInt( "step_ord" ) );
            row.setStepStatus( StepStatus.MXPENDING );
            row.setTaskStepKey(
                  lQs.getKey( TaskStepKey.class, "task_db_id", "task_id", "step_id" ) );
            schedStepDao.insert( row );

         }
      }
   }
}
