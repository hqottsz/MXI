package com.mxi.mx.common.ejb.task;

import java.net.URI;
import java.util.Date;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import javax.ejb.RemoveException;
import javax.naming.Context;
import javax.transaction.UserTransaction;

import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.message.MxMessage;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.ejb.stask.STaskBean;
import com.mxi.mx.core.ejb.stask.STaskLocal;
import com.mxi.mx.core.ejb.stask.STaskRemote;
import com.mxi.mx.core.flight.model.FlightLegId;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.DepartmentKey;
import com.mxi.mx.core.key.EventAttachmentKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.InventoryParmDataKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OrgCrewShiftPlanKey;
import com.mxi.mx.core.key.PanelKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefEngUnitKey;
import com.mxi.mx.core.key.RefLogReasonKey;
import com.mxi.mx.core.key.RefStageReasonKey;
import com.mxi.mx.core.key.RefWorkTypeKey;
import com.mxi.mx.core.key.SchedExtPartKey;
import com.mxi.mx.core.key.SchedLabourRoleKey;
import com.mxi.mx.core.key.SchedPanelKey;
import com.mxi.mx.core.key.SchedStepKey;
import com.mxi.mx.core.key.SchedWPSignReqKey;
import com.mxi.mx.core.key.SchedZoneKey;
import com.mxi.mx.core.key.TaskInstPartKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskPartKey;
import com.mxi.mx.core.key.TaskRmvdPartKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.key.WoLineKey;
import com.mxi.mx.core.key.ZoneKey;
import com.mxi.mx.core.services.event.attach.AttachmentTO;
import com.mxi.mx.core.services.event.inventory.UsageSnapshot;
import com.mxi.mx.core.services.lpa.basic.LineCheckTO;
import com.mxi.mx.core.services.stask.WarningCheckCapacity;
import com.mxi.mx.core.services.stask.WarningPreventExecutionJICMissingReq;
import com.mxi.mx.core.services.stask.WarningSchedule;
import com.mxi.mx.core.services.stask.complete.CompleteTaskArrayException;
import com.mxi.mx.core.services.stask.creation.CreateQuickCheckTO;
import com.mxi.mx.core.services.stask.deadline.DeadlineExtensionTO;
import com.mxi.mx.core.services.stask.details.DetailsService;
import com.mxi.mx.core.services.stask.maintrelease.AircraftReleaseTO;
import com.mxi.mx.core.services.stask.maintrelease.ComponentReleaseTO;
import com.mxi.mx.core.services.stask.measurement.MeasurementDetailsTO;
import com.mxi.mx.core.services.stask.mpc.MPCTask;
import com.mxi.mx.core.services.stask.nsv.ManageNsvTasksTO;
import com.mxi.mx.core.services.stask.status.ScheduleExternalTO;
import com.mxi.mx.core.services.stask.status.ScheduleInternalTO;
import com.mxi.mx.core.services.stask.taskpart.CreatePartForTaskTO;
import com.mxi.mx.core.services.stask.taskpart.ExternallyControlledPartTO;
import com.mxi.mx.core.services.stask.taskpart.OperationInvalidForInvClassException;
import com.mxi.mx.core.services.stask.taskpart.PartRequirementTO;
import com.mxi.mx.core.services.stask.taskpart.installed.InstalledPartTO;
import com.mxi.mx.core.services.stask.taskpart.removed.RemovedPartTO;


/**
 * Stub form home interface for Stask local.
 *
 * @author akovalevich
 */
public class STaskLocalStub implements STaskLocal {

   private STaskBean iSTaskBean;


   /**
    * Initialized STask bean to be delegated
    *
    * @param aSTaskBean
    */
   public STaskLocalStub(STaskBean aSTaskBean) {
      iSTaskBean = aSTaskBean;
   }


   /**
    * Returns reference to the remote interface.
    *
    * @return the value of the Remote property.
    */
   public static STaskLocal getLocalInterface() {
      return STaskBean.getLocalInterface();
   }


   /**
    * Returns reference to the remote interface.
    *
    * @param aInitialContext
    *           initial context
    *
    * @return the value of the Remote property.
    */
   public static STaskRemote getRemoteInterface( Context aInitialContext ) {
      return STaskBean.getRemoteInterface( aInitialContext );
   }


   /**
    * Adds an attachment to the task.
    *
    * @param aTaskKey
    *           the task to add the attachment to.
    * @param aAttachmentTO
    *           the Attachment object containing all attachment data.
    *
    * @return the primary key of the new attachment
    *
    * @throws MxException
    *            if aTaskKey is null, if aAttachment is null, if aAttachment.getName() is null or
    *            blank, if aAttachment.getBlob() is null.
    */
   @Override
   public EventAttachmentKey addAttachment( TaskKey aTaskKey, AttachmentTO aAttachmentTO )
         throws MxException {
      return iSTaskBean.addAttachment( aTaskKey, aAttachmentTO );
   }


   /**
    * Assigns a task as a child of the current completed task, and completes the child using the
    * parent task's dates and usage.
    *
    * @param aParentTask
    *           the parent task.
    * @param aChildTask
    *           the task to be assigned as a child.
    * @param aAuthorizingHr
    *           the person assigning the child task.
    * @param aWarningsApproved
    *           If warnings are to be bypassed
    * @param aReason
    *           the reason for assigning the child task.
    * @param aNotes
    *           the notes on the child task.
    *
    * @throws MxException
    *            if<code>aParentTask</code>, <code>aChildTask</code> or <code>
    *                            aAuthorizingHr</code> is not provided.
    * @throws TriggerException
    *            if a trigger exception occurs.
    */
   @Override
   public void addChildTaskToCompletedParent( TaskKey aParentTask, TaskKey aChildTask,
         HumanResourceKey aAuthorizingHr, boolean aWarningsApproved, String aReason, String aNotes )
         throws MxException, TriggerException {
      iSTaskBean.addChildTaskToCompletedParent( aParentTask, aChildTask, aAuthorizingHr,
            aWarningsApproved, aReason, aNotes );
   }


   /**
    * Adds an exernally controlled part.
    *
    * @param aTO
    *           the externally controlled part information.
    *
    * @return SchedExtPartKey
    *
    * @throws MxException
    *            when something goes wrong!
    */
   @Override
   public SchedExtPartKey addExternallyControlledPart( ExternallyControlledPartTO aTO )
         throws MxException {
      return iSTaskBean.addExternallyControlledPart( aTO );
   }


   /**
    * Add measurements to a task
    *
    * @param aTask
    *           the task primary key
    * @param aMeasurementDetailsTO
    *           the measurement details transfer object
    *
    * @throws MxException
    *            if the task already has the measurement
    */
   @Override
   public void addMeasurements( TaskKey aTask, MeasurementDetailsTO[] aMeasurementDetailsTO )
         throws MxException {
      iSTaskBean.addMeasurements( aTask, aMeasurementDetailsTO );
   }


   /**
    * Adds the specified panels to the specified task
    *
    * @param aTask
    *           the task to which we're adding the panel(s)
    * @param aPanel
    *           the panel(s) we want to add to the task
    * @param aAuthorizingHr
    *           the authorizing hr
    *
    * @return the arary of newly created SchedPanelKey(s)
    *
    * @throws MxException
    *            If the main inventory of the task is locked
    * @throws TriggerException
    */
   @Override
   public SchedPanelKey[] addPanel( TaskKey aTask, PanelKey[] aPanel,
         HumanResourceKey aAuthorizingHr ) throws MxException, TriggerException {
      return iSTaskBean.addPanel( aTask, aPanel, aAuthorizingHr );
   }


   /**
    * Adds a step to a task
    *
    * @param aTask
    *           the task
    * @param aDescription
    *           the step details
    *
    * @return the new step key
    *
    * @throws MxException
    *            If the task is historic
    */
   @Override
   public SchedStepKey addStep( TaskKey aTask, String aDescription ) throws MxException {
      return iSTaskBean.addStep( aTask, aDescription );
   }


   /**
    * Adds the specified zones to the specified task
    *
    * @param aTask
    *           the task to which we're adding the zone(s)
    * @param aZone
    *           the zone(s) we want to add to the task
    *
    * @return the arary of newly created SchedZoneKey(s)
    *
    * @throws MxException
    *            If the main inventory of the task is locked
    */
   @Override
   public SchedZoneKey[] addZone( TaskKey aTask, ZoneKey[] aZone ) throws MxException {
      return iSTaskBean.addZone( aTask, aZone );
   }


   /**
    * Modifies the given Labor with <CODE>Adjusted Hours For Billing</CODE> parameter
    *
    * @param aLaborRoleKey
    *           labor role key..
    * @param aAdjustedBillingHr
    *           value of<CODE>Adjusted Hours For Billing</CODE> parameter.
    *
    * @throws MxException
    *            if exception occurs.
    */
   @Override
   public void adjustHoursForBilling( SchedLabourRoleKey aLaborRoleKey, Double aAdjustedBillingHr )
         throws MxException {
      iSTaskBean.adjustHoursForBilling( aLaborRoleKey, aAdjustedBillingHr );
   }


   /**
    * @see DetailsService#allowExecution(TaskKey, RefLogReasonKey, String, HumanResourceKey)
    * @param aActualRequirement
    * @param aStageReason
    * @param aNotes
    * @param aAuthorizingHr
    */
   @Override
   public void allowExecution( TaskKey[] aActualRequirement, RefStageReasonKey aStageReason,
         String aNotes, HumanResourceKey aAuthorizingHr ) throws MxException {
      iSTaskBean.allowExecution( aActualRequirement, aStageReason, aNotes, aAuthorizingHr );
   }


   /**
    * Assigns an alternate part for a part requirement and send an alert to technical records.
    *
    * @param aTaskPartKey
    *           pk of the part requirement
    * @param aPartNoKey
    *           pk of the alternate part
    * @param aHrKey
    *           resource performing the operation
    *
    * @throws MxException
    *            if the part req or part no are not valid
    * @throws TriggerException
    *            if a trigger exception occurs
    * @throws OperationInvalidForInvClassException
    *            cannot execute this method on kit part requirement
    */
   @Override
   public void assignAlternatePartForPartReq( TaskPartKey aTaskPartKey, PartNoKey aPartNoKey,
         HumanResourceKey aHrKey ) throws MxException, TriggerException {
      iSTaskBean.assignAlternatePartForPartReq( aTaskPartKey, aPartNoKey, aHrKey );
   }


   /**
    * Assigns the specified crews to the task.
    *
    * @param aTaskKey
    *           the task having the department assigned.
    * @param aDeptKeys
    *           The crews to be assigned to the task.
    * @param aAuthorizingHr
    *           the authorizing human resource
    *
    * @throws MxException
    *            if any department key is invalid.
    */
   @Override
   public void assignCrews( TaskKey aTaskKey, DepartmentKey[] aDeptKeys,
         HumanResourceKey aAuthorizingHr ) throws MxException {
      iSTaskBean.assignCrews( aTaskKey, aDeptKeys, aAuthorizingHr );
   }


   /**
    * Assigns the specified crews shift plan to the task.
    *
    * @param aTaskKeys
    *           the task having the department assigned.
    * @param aCrewShiftPlanKey
    *           The crews shift plan to be assigned to the task.
    * @param aAuthorizingHr
    *           the authorizing human resource
    *
    * @throws MxException
    *            if any department key is invalid.
    */
   @Override
   public void assignCrewShiftPlan( TaskKey[] aTaskKeys, OrgCrewShiftPlanKey aCrewShiftPlanKey,
         HumanResourceKey aAuthorizingHr ) throws MxException {
      iSTaskBean.assignCrewShiftPlan( aTaskKeys, aCrewShiftPlanKey, aAuthorizingHr );
   }


   /**
    * Add an installed part to the specified part requirement
    *
    * @param aTaskPartKey
    *           part requirement pk
    *
    * @param aAuthorizingHr
    * @return pk for the new installed part row
    *
    * @throws MxException
    *            if the part requirement is not found
    * @throws TriggerException
    * @throws OperationInvalidForInvClassException
    *            cannot execute this method on kit part requirement
    */
   @Override
   public TaskInstPartKey assignInstalledPart( TaskPartKey aTaskPartKey,
         HumanResourceKey aAuthorizingHr ) throws MxException, TriggerException {
      return iSTaskBean.assignInstalledPart( aTaskPartKey, aAuthorizingHr );
   }


   /**
    * Assign Location to a Workscope Task
    *
    * @param aWorkscopeTasks
    *           List of Workscope Task Keys
    * @param aLocationKey
    *           Location Key
    *
    * @throws MxException
    *            if error occurs
    */
   @Override
   public void assignLocation( List<TaskKey> aWorkscopeTasks, LocationKey aLocationKey )
         throws MxException {
      iSTaskBean.assignLocation( aWorkscopeTasks, aLocationKey );
   }


   /**
    * Add a removed part to the specified part requirement
    *
    * @param aTaskPartKey
    *           part requirement pk
    *
    * @param aAuthorizingHr
    * @return pk for the new removed part row
    *
    * @throws MxException
    *            if the part requirement is not found
    * @throws OperationInvalidForInvClassException
    *            cannot execute this method on kit part requirement
    */
   @Override
   public TaskRmvdPartKey assignRemovedPart( TaskPartKey aTaskPartKey,
         HumanResourceKey aAuthorizingHr ) throws MxException {
      return iSTaskBean.assignRemovedPart( aTaskPartKey, aAuthorizingHr );
   }


   /**
    * Assign Workscope Order
    *
    * @param aTaskKey
    *           Workpackge Key
    *
    * @throws MxException
    *            if error occurs
    */
   @Override
   public void assignWorkscopeOrder( TaskKey aTaskKey ) throws MxException {
      iSTaskBean.assignWorkscopeOrder( aTaskKey );
   }


   /**
    * Assign Workscope Order
    *
    * @param aCheckKey
    *           Workpackge Key
    * @param aReqTaskKey
    *           Task Key
    *
    * @throws MxException
    *            if error occurs
    */
   @Override
   public void assignWorkscopeOrderForReassignedReq( TaskKey aCheckKey, TaskKey aReqTaskKey )
         throws MxException {
      iSTaskBean.assignWorkscopeOrderForReassignedReq( aCheckKey, aReqTaskKey );
   }


   /**
    * Cancel task part requirement.
    *
    * @param aTaskPartKey
    *           a task part requirement.
    *
    * @throws MxException
    *            if aTaskPartKey is null.
    */
   @Override
   public void cancelPart( TaskPartKey aTaskPartKey ) throws MxException {
      iSTaskBean.cancelPart( aTaskPartKey );
   }


   /**
    * Clears out an installed part entry from a part requirement.
    *
    * <ol>
    * <li>If there is a part request associated with the installed part:
    *
    * <ol>
    * <li>sever the link between the part request and the inventory</li>
    * <li>if the part request is issued (ie: PRISSUED):
    * <li>create a turn-in transfer for the inventory, linked to the task to which the part
    * requirement belongs</li></li>
    * <li>if the part not completed, cancelled or issued:
    * <li>cancel the part request</li></li>
    * </ol>
    * </li>
    * <li>Delete the installed task part</li>
    * </ol>
    *
    * @param aInstPartKey
    *           pk of the sched_inst_part entry to be removed
    * @param aHrKey
    *           user performing the action
    *
    * @throws MxException
    *            if the installed part is not found
    * @throws TriggerException
    *            if a trigger error occurs
    */
   @Override
   public void clearInstalledPart( TaskInstPartKey aInstPartKey, HumanResourceKey aHrKey )
         throws MxException, TriggerException {
      iSTaskBean.clearInstalledPart( aInstPartKey, aHrKey );
   }


   /**
    * Clear MPC signatures for the given Task list
    *
    * @param aTaskList
    *           {@link List} {@link MPCTask} Task List
    *
    * @throws MxException
    *            if an error from business logic occurs
    */
   @Override
   public void clearMPCSignatures( List<MPCTask> aTaskList ) throws MxException {
      iSTaskBean.clearMPCSignatures( aTaskList );
   }


   /**
    * Clear out a removed part entry from a part requirement
    *
    * @param aRmvdPartKey
    *           pk of the sched_rmvd_part entry to be removed
    *
    * @param aAuthorizingHr
    * @throws MxException
    *            if the removed part is not found
    */
   @Override
   public void clearRemovedPart( TaskRmvdPartKey aRmvdPartKey, HumanResourceKey aAuthorizingHr )
         throws MxException {
      iSTaskBean.clearRemovedPart( aRmvdPartKey, aAuthorizingHr );
   }


   /**
    * Marks the job cards as collected/uncollected
    *
    * @param aCheck
    *           The check or work order that requires the collection
    * @param aBarcode
    *           The job card to mark as collected/uncollected
    * @param aCollectBool
    *           the value of the collect_bool
    * @param aAuthorizingHr
    *           the user responsible for marking the job card as collected
    *
    * @return The error message indicating which job cards did not have the correct status to be
    *         marked as collected
    *
    * @throws MxException
    *            If no authorizing HR is provided
    */
   @Override
   public MxMessage collectJobCard( TaskKey aCheck, String aBarcode, boolean aCollectBool,
         HumanResourceKey aAuthorizingHr ) throws MxException {
      return iSTaskBean.collectJobCard( aCheck, aBarcode, aCollectBool, aAuthorizingHr );
   }


   /**
    * Marks the job cards as collected/uncollected
    *
    * @param aCheck
    *           The check or work order that requires the collection
    * @param aTasks
    *           The job cards to mark as collected/uncollected
    * @param aCollectBool
    *           the value of the collect_bool
    * @param aAuthorizingHr
    *           the user responsible for marking the job card as collected
    *
    * @return The error messages indicating which job cards did not have the correct status to be
    *         marked as collected
    *
    * @throws MxException
    *            If no authorizing HR is provided
    */
   @Override
   public MxMessage[] collectJobCards( TaskKey aCheck, TaskKey[] aTasks, boolean aCollectBool,
         HumanResourceKey aAuthorizingHr ) throws MxException {
      return iSTaskBean.collectJobCards( aCheck, aTasks, aCollectBool, aAuthorizingHr );
   }


   /**
    * Complete a Work Package or a list of Tasks and their children.
    *
    * <OL>
    * <LI>Only completes the tasks that do not throw exceptions.</LI>
    * </OL>
    *
    * @param aWorkPackage
    *           The Work Package PK (if completing a Work Package)
    * @param aTask
    *           The List of Tasks (if not completing a full Work Package)
    * @param aCompleteDate
    *           Date to complete the tasks.
    * @param aHr
    *           Human Resource authorizing the action
    * @param aWarningsApproved
    *           If warnings are to be bypassed
    * @param aUserTx
    *           User Transaction
    * @throws CompleteTaskArrayException
    *            CompleteTaskArrayException
    * @throws Exception
    *            If an error occurs
    */
   @Override
   public void complete( TaskKey aWorkPackage, TaskKey[] aTask, Date aCompleteDate,
         HumanResourceKey aHr, boolean aWarningsApproved, UserTransaction aUserTx )
         throws CompleteTaskArrayException, Exception {
      iSTaskBean.complete( aWorkPackage, aTask, aCompleteDate, aHr, aWarningsApproved, aUserTx );
   }


   /**
    * Creates historic CHECK or Work Order.
    *
    * <OL>
    * <LI>if aInventory is an aircraft historic CHECK will be created.</LI>
    * <LI>if aInvnetory is not an aircraft, historic work order will be created.</LI>
    * </OL>
    *
    * @param aInventory
    *           main inventory primary key
    * @param aAuthorizingHr
    *           authorizing human resource
    * @param aName
    *           task name
    * @param aSubclass
    *           task subclass
    * @param aDocReference
    *           task document reference
    * @param aWorkTypes
    *           task work type
    * @param aDescription
    *           task description
    * @param aIssuedDate
    *           task issued date
    *
    * @return historic task reference
    *
    * @throws MxException
    *            if aInventory is locked.
    * @throws TriggerException
    *            if a core trigger exception occurs during processing
    */
   @Override
   public TaskKey createHistoricRootTask( InventoryKey aInventory, HumanResourceKey aAuthorizingHr,
         String aName, String aSubclass, String aDocReference, List<RefWorkTypeKey> aWorkTypes,
         String aDescription, Date aIssuedDate ) throws MxException, TriggerException {
      return iSTaskBean.createHistoricRootTask( aInventory, aAuthorizingHr, aName, aSubclass,
            aDocReference, aWorkTypes, aDescription, aIssuedDate );
   }


   /**
    * Creates historic adhoc task.
    *
    * @param aInventory
    *           main inventory primary key
    * @param aParentTask
    *           parent task
    * @param aAuthorizingHr
    *           authorizing human resource
    * @param aName
    *           task name
    * @param aSubclass
    *           task subclass
    * @param aDocReference
    *           task document reference
    * @param aWorkTypes
    *           task work type
    * @param aDescription
    *           task description
    * @param aROLineNum
    *           line number
    * @param aIssuedDate
    *           task issued date
    *
    * @return historic task reference
    *
    * @throws MxException
    *            if aInventory is locked.
    * @throws TriggerException
    *            if a core trigger exception occurs during processing
    */
   @Override
   public TaskKey createHistoricTask( InventoryKey aInventory, TaskKey aParentTask,
         HumanResourceKey aAuthorizingHr, String aName, String aSubclass, String aDocReference,
         List<RefWorkTypeKey> aWorkTypes, String aDescription, String aROLineNum, Date aIssuedDate )
         throws MxException, TriggerException {
      return iSTaskBean.createHistoricTask( aInventory, aParentTask, aAuthorizingHr, aName,
            aSubclass, aDocReference, aWorkTypes, aDescription, aROLineNum, aIssuedDate );
   }


   /**
    * Create a historic task that was executed in the past from task definition.
    *
    * @param aInventory
    *           task main inventory.
    * @param aParentKey
    *           task parent.
    * @param aTaskTask
    *           task definition.
    * @param aCompletionDate
    *           task completion date.
    * @param aCompletionUsage
    *           task completion usage
    * @param aReason
    *           reason for creation
    * @param aAuthorizingHr
    *           human resource
    * @param aNote
    *           user stage note
    * @param aFollowingTask
    *           following task
    *
    * @return new task primary key
    *
    * @throws MxException
    *            if aInventory, aTaskDefinition, aCompletionDate, aAuthorizingHr,
    *            aCompletionUsage.iDataType is not provided.
    * @throws TriggerException
    *            if a triggering error occurs.
    */
   @Override
   public TaskKey createHistoricTaskFromDefinition( InventoryKey aInventory, TaskKey aParentKey,
         TaskTaskKey aTaskTask, Date aCompletionDate, UsageSnapshot[] aCompletionUsage,
         String aReason, HumanResourceKey aAuthorizingHr, String aNote, TaskKey aFollowingTask )
         throws MxException, TriggerException {
      return iSTaskBean.createHistoricTaskFromDefinition( aInventory, aParentKey, aTaskTask,
            aCompletionDate, aCompletionUsage, aReason, aAuthorizingHr, aNote, aFollowingTask );
   }


   /**
    * Create a historic task that was executed in the past from task definition.
    *
    * @param aInventory
    *           task main inventory.
    * @param aParentKey
    *           task parent.
    * @param aTaskTask
    *           task definition.
    * @param aCompletionDate
    *           task completion date.
    * @param aCompletionUsage
    *           task completion usage
    * @param aReason
    *           reason for creation
    * @param aAuthorizingHr
    *           human resource
    * @param aNote
    *           user stage note
    * @param aFollowingTask
    *           following task
    * @param aWarningApproved
    *           whether warnings have been approved or not
    *
    * @return new task primary key
    *
    * @throws MxException
    *            if aInventory, aTaskDefinition, aCompletionDate, aAuthorizingHr,
    *            aCompletionUsage.iDataType is not provided.
    * @throws TriggerException
    *            if a triggering error occurs.
    */
   @Override
   public TaskKey createHistoricTaskFromDefinition( InventoryKey aInventory, TaskKey aParentKey,
         TaskTaskKey aTaskTask, Date aCompletionDate, UsageSnapshot[] aCompletionUsage,
         String aReason, HumanResourceKey aAuthorizingHr, String aNote, TaskKey aFollowingTask,
         boolean aWarningApproved ) throws MxException, TriggerException {
      return iSTaskBean.createHistoricTaskFromDefinition( aInventory, aParentKey, aTaskTask,
            aCompletionDate, aCompletionUsage, aReason, aAuthorizingHr, aNote, aFollowingTask,
            aWarningApproved );
   }


   /**
    * This method creates a new part number using the given information.
    *
    * @param aTask
    *           task key for the resulting part requirement
    * @param aCreatePartFotTaskTO
    *           the part number details, bom part max quantity and part request details.
    * @param aAuthorizingHr
    *           the human resource who is authorizing the operation.
    *
    * @return the primary key of the created part number.
    *
    * @throws MxException
    *            if any of the following properties is not a valid refterm:
    *
    *            <ul>
    *            <li>aPartDetails.iStdUnitOfMeasure</li>
    *            <li>aPartDetails.iInventoryClass</li>
    *            <li>aReason</li>
    *            </ul>
    * @throws TriggerException
    *            if an error occurs while firing the MX_PN_CREATE trigger.
    */
   @Override
   public PartNoKey createPartForTask( TaskKey aTask, CreatePartForTaskTO aCreatePartFotTaskTO,
         HumanResourceKey aAuthorizingHr ) throws MxException, TriggerException {
      return iSTaskBean.createPartForTask( aTask, aCreatePartFotTaskTO, aAuthorizingHr );
   }


   /**
    * Create a quick check for the task
    *
    * @param aTasks
    *           the lists of tasks to be assigned to the created check
    * @param aTO
    *           the TO
    * @param aAuthorizingHr
    *           the authorizing hr
    * @param aUsageSnapshot
    *           the starting usage values for the check
    *
    * @return the new check key
    *
    * @throws MxException
    *            if there are more than 1 "IN WORK" root task on the task's main inventory tree.
    * @throws TriggerException
    *            if a trigger related error is encountered
    * @throws CompleteTaskArrayException
    *            if there is a failure with one of the tasks, meant for batch task completion
    */
   @Override
   public TaskKey createQuickCheck( TaskKey[] aTasks, CreateQuickCheckTO aTO,
         HumanResourceKey aAuthorizingHr, UsageSnapshot[] aUsageSnapshot )
         throws MxException, TriggerException {
      return iSTaskBean.createQuickCheck( aTasks, aTO, aAuthorizingHr, aUsageSnapshot );
   }


   /**
    * Creates a replacement task for the given inventory.
    *
    * @param aInventory
    *           the inventory.
    * @param aPoLine
    *           the purchase order line.
    * @param aHr
    *           the hr.
    *
    * @return pk of the created task.
    *
    * @throws MxException
    *            if<code>aInventory</code> or <code>aHr</code> is null.
    * @throws TriggerException
    *            if a triggering error occurs.
    */
   @Override
   public TaskKey createReplacementTaskForInventory( InventoryKey aInventory,
         PurchaseOrderLineKey aPoLine, HumanResourceKey aHr ) throws MxException, TriggerException {
      return iSTaskBean.createReplacementTaskForInventory( aInventory, aPoLine, aHr );
   }


   /**
    * Creates service checks for a task definition revision(block definition that satisfies criteria
    * to be a service check) and an aircraft :
    *
    * <ul>
    * <li>Creates a new work package.</li>
    * <li>If there are already tasks created for the task definition and the aircraft, it creates
    * new FORECAST task, inserts the new task into the chain and assigns it to the work
    * package.</li>
    * <li>If there are no tasks created, it creates whole chain with ACTV and FORECAST tasks and
    * assigns ACTV task to the work package</li>
    * <li>Newly created tasks are scheduled for zipping and baseline sync takes care of reassigning
    * requirements according to task deadlines</li>
    * <li>Schedules the work package internally.</li>
    * </ul>
    *
    * @param aTaskTaskKey
    *           Block task definition revision
    * @param aAircraftInventory
    *           Aircraft inventory
    * @param aHr
    *           Human resource
    * @param aLineCheckTO
    *           Transfer object for creating service check work package
    *
    * @return PK of the newly created service check work package
    *
    * @throws MxException
    *            If an exception is caused by the user's action.
    * @throws TriggerException
    *            Trigger exception.
    */
   @Override
   public TaskKey createServiceCheck( TaskTaskKey aTaskTaskKey, InventoryKey aAircraftInventory,
         HumanResourceKey aHr, LineCheckTO aLineCheckTO ) throws MxException, TriggerException {
      return iSTaskBean.createServiceCheck( aTaskTaskKey, aAircraftInventory, aHr, aLineCheckTO );
   }


   /**
    * Creates a turn check:
    *
    * <ul>
    * <li>Creates a new work package.</li>
    * <li>Creates actual task for the block task definition revision.</li>
    * <li>Assign the actual block task to the work package.</li>
    * <li>Schedules the work package internally.</li>
    * </ul>
    *
    * @param aTaskTaskKey
    *           Block task definition revision
    * @param aAircraftInventory
    *           Aircraft inventory
    * @param aHr
    *           Human resource
    * @param aLineCheckTO
    *           Transfer object for creating turn check(work package)
    *
    * @return PK of the newly created turn check(work package)
    *
    * @throws MxException
    *            If exception is caused by the user's action.
    * @throws TriggerException
    *            Trigger exception.
    */
   @Override
   public TaskKey createTurnCheck( TaskTaskKey aTaskTaskKey, InventoryKey aAircraftInventory,
         HumanResourceKey aHr, LineCheckTO aLineCheckTO ) throws MxException, TriggerException {
      return iSTaskBean.createTurnCheck( aTaskTaskKey, aAircraftInventory, aHr, aLineCheckTO );
   }


   /**
    * Delete an exernally controlled part.
    *
    * @param aTO
    *           the externally controlled part information.
    *
    * @throws MxException
    *            when something goes wrong!
    */
   @Override
   public void deleteExternallyControlledPart( List<ExternallyControlledPartTO> aTO )
         throws MxException {
      iSTaskBean.deleteExternallyControlledPart( aTO );
   }


   /**
    * Edits the details of the attachment
    *
    * @param aEventAttachment
    *           the attachment key
    * @param aAttachmentTO
    *           the new details
    *
    * @throws MxException
    *            if a mandatory arg is missing
    */
   @Override
   public void editAttachment( EventAttachmentKey aEventAttachment, AttachmentTO aAttachmentTO )
         throws MxException {
      iSTaskBean.editAttachment( aEventAttachment, aAttachmentTO );
   }


   /**
    * Edit an exernally controlled part.
    *
    * @param aTO
    *           the externally controlled part information.
    *
    * @throws MxException
    *            when something goes wrong!
    */
   @Override
   public void editExternallyControlledPart( List<ExternallyControlledPartTO> aTO )
         throws MxException {
      iSTaskBean.editExternallyControlledPart( aTO );
   }


   /**
    * Extend the deadliens for a given task.
    *
    * @param aTask
    *           task to extend the deadlines for
    * @param aTO
    *           Transfer object containing the extension details
    * @param aHr
    *           user performing the action
    *
    * @throws MxException
    *            if the task isn't found
    * @throws TriggerException
    *            if a triggering error occurs
    */
   @Override
   public void extendDeadline( TaskKey aTask, DeadlineExtensionTO aTO, HumanResourceKey aHr )
         throws MxException, TriggerException {
      iSTaskBean.extendDeadline( aTask, aTO, aHr );
   }


   /**
    * Returns the value of the installed part property.
    *
    * @param aInstPartKey
    *           installed aprt key
    *
    * @return the value of the installed part property.
    *
    * @throws MxException
    *            if installed part could not be found
    */
   @Override
   public InstalledPartTO getInstalledPart( TaskInstPartKey aInstPartKey ) throws MxException {
      return iSTaskBean.getInstalledPart( aInstPartKey );
   }


   /**
    * Retrieves a boolean indicating if the task is locked. A locked task is uneditable.
    *
    * @param aTaskKey
    *           the pk of the task to lock
    *
    * @return true if the task is locked, false if unlocked
    *
    * @throws MxException
    *            if an error from business logic occurs
    */
   @Override
   public boolean getLock( TaskKey aTaskKey ) throws MxException {
      return iSTaskBean.getLock( aTaskKey );
   }


   /**
    * Get previous oil consumption measurements.
    *
    * @param aInventoryParmDataKey
    *           the inventoryparmdatakey primary key.
    * @param aRefEngUnitKey
    *           the refengunit key.
    *
    * @return returns the previous oil consumption measurements dataset.
    *
    * @throws MxException
    *            when any error occurs.
    */
   @Override
   public DataSet getPreviousOCMeasurements( InventoryParmDataKey aInventoryParmDataKey,
         RefEngUnitKey aRefEngUnitKey ) throws MxException {
      return iSTaskBean.getPreviousOCMeasurements( aInventoryParmDataKey, aRefEngUnitKey );
   }


   /**
    * Returns the value of the removal warnings property.
    *
    * @param aTaskPart
    *           The Task Part key
    * @param aPartNo
    *           The Part Number
    * @param aSerialNo
    *           The Serial Number for the part
    *
    * @return the value of the removal warnings property.
    */
   @Override
   public MxMessage[] getRemovalWarnings( TaskPartKey aTaskPart, PartNoKey aPartNo,
         String aSerialNo ) {
      return iSTaskBean.getRemovalWarnings( aTaskPart, aPartNo, aSerialNo );
   }


   /**
    * Returns the value of the removed part property.
    *
    * @param aRmvdPartKey
    *           removed aprt key
    *
    * @return the value of the removed part property.
    *
    * @throws MxException
    *            if installed part could not be found
    */
   @Override
   public RemovedPartTO getRemovedPart( TaskRmvdPartKey aRmvdPartKey ) throws MxException {
      return iSTaskBean.getRemovedPart( aRmvdPartKey );
   }


   /**
    * Manage Next Shop Visit Tasks
    *
    * @param aTO
    *           transfer object
    *
    * @throws MxException
    *            if any exception is raised by maintenix.
    */
   @Override
   public void manageNsvTasks( ManageNsvTasksTO aTO ) throws MxException {
      iSTaskBean.manageNsvTasks( aTO );
   }


   /**
    *
    * Mark Step Applicability
    *
    * @param aSchedStepKey
    * @param aStepStatus
    * @param aAuthorizingHr
    * @param aNotes
    */
   @Override
   public void toggleApplicability( SchedStepKey aSchedStepKey, HumanResourceKey aAuthorizingHr,
         String aNotes ) throws MxException {
      iSTaskBean.toggleApplicability( aSchedStepKey, aAuthorizingHr, aNotes );
   }


   /**
    * Move down the workscope task across the workscope line.
    *
    * @param aCheckKey
    *           check key
    * @param aTaskKey
    *           task key
    *
    * @throws MxException
    *            if error occurs
    */
   @Override
   public void moveDownWorkscopeTask( TaskKey aCheckKey, TaskKey aTaskKey ) throws MxException {
      iSTaskBean.moveDownWorkscopeTask( aCheckKey, aTaskKey );
   }


   /**
    * Move down the workscope tasks across the workscope line.
    *
    * @param aCheckKey
    *           check key
    * @param aTasks
    *           task key
    *
    * @throws MxException
    *            if error occurs
    */
   @Override
   public void moveDownWorkscopeTasks( TaskKey aCheckKey, List<TaskKey> aTasks )
         throws MxException {
      iSTaskBean.moveDownWorkscopeTasks( aCheckKey, aTasks );
   }


   /**
    * Move Tally Line Order
    *
    * @param aSelectedTallyLineInsert
    *           selected line to insert
    * @param aWoLines
    *           List of WoLine Key
    *
    * @throws MxException
    *            if error occurs
    */
   @Override
   public void moveTallyLineOrder( int aSelectedTallyLineInsert, List<WoLineKey> aWoLines )
         throws MxException {
      iSTaskBean.moveTallyLineOrder( aSelectedTallyLineInsert, aWoLines );
   }


   /**
    * Move up the workscope task across the workscope line.
    *
    * @param aCheckKey
    *           check key
    * @param aTaskKey
    *           task key
    *
    * @throws MxException
    *            if error occurs
    */
   @Override
   public void moveUpWorkscopeTask( TaskKey aCheckKey, TaskKey aTaskKey ) throws MxException {
      iSTaskBean.moveUpWorkscopeTask( aCheckKey, aTaskKey );
   }


   /**
    * Move up the workscope tasks across the workscope line.
    *
    * @param aCheckKey
    *           check key
    * @param aTasks
    *           task key
    *
    * @throws MxException
    *            if error occurs
    */
   @Override
   public void moveUpWorkscopeTasks( TaskKey aCheckKey, List<TaskKey> aTasks ) throws MxException {
      iSTaskBean.moveUpWorkscopeTasks( aCheckKey, aTasks );
   }


   /**
    * Prevent execution on this actual task
    *
    * @param aActualRequirement
    *           {@link TaskKey} The Requirement
    * @param aReviewDt
    *           {@link Date} The Review Date
    * @param aStageReason
    *           {@link RefStageReasonKey} The reason posted
    * @param aNotes
    *           {@link String} Notes for prevention
    * @param aAuthorizingHr
    *           {@link HumanResourceKey} User who performed the action
    *
    * @throws MxException
    *            When the test can not complete
    */
   @Override
   public void preventExecution( TaskKey[] aActualRequirement, Date aReviewDt,
         RefStageReasonKey aStageReason, String aNotes, HumanResourceKey aAuthorizingHr )
         throws MxException {
      iSTaskBean.preventExecution( aActualRequirement, aReviewDt, aStageReason, aNotes,
            aAuthorizingHr );
   }


   /**
    * Removes the attachment from the task.
    *
    * @param aTaskKey
    *           the task to remove the attachment from.
    * @param aEventAttachmentKey
    *           the attachment primary key
    *
    * @return the external blob file to be removed
    *
    * @throws MxException
    *            if aTaskKey is null, if aEventAttachmentKey is null
    */
   @Override
   public URI removeAttachment( TaskKey aTaskKey, EventAttachmentKey aEventAttachmentKey )
         throws MxException {
      return iSTaskBean.removeAttachment( aTaskKey, aEventAttachmentKey );
   }


   /**
    * This method removes a list of external blob files.
    *
    * @param aURI
    *           the external file to be deleted
    *
    * @throws MxException
    *            if an error occurs
    */
   @Override
   public void removeBlobFile( URI aURI ) throws MxException {
      iSTaskBean.removeBlobFile( aURI );
   }


   /**
    * Remove measurements from a task
    *
    * @param aTask
    *           the task primary key
    * @param aDataType
    *           the data types (measurements)
    * @param aInventoryKey
    *           the inventory keys
    *
    * @throws MxException
    *            if the task already has the measurement
    */
   @Override
   public void removeMeasurements( TaskKey aTask, DataTypeKey[] aDataType,
         InventoryKey[] aInventoryKey ) throws MxException {
      iSTaskBean.removeMeasurements( aTask, aDataType, aInventoryKey );
   }


   /**
    * Removes a panel from the current task.
    *
    * @param aTask
    *           The task to which the panel belongs and will be removed
    * @param aPanel
    *           a Panel to be assigned.
    * @param aAuthorizingHr
    *           the authorizing hr
    *
    * @exception MxException
    *               If aTask is not specified
    * @throws TriggerException
    *            If a trigger error occurs
    */
   @Override
   public void removePanel( TaskKey aTask, PanelKey[] aPanel, HumanResourceKey aAuthorizingHr )
         throws MxException, TriggerException {
      iSTaskBean.removePanel( aTask, aPanel, aAuthorizingHr );
   }


   /**
    * Removes a part from the list for the current task. The logic will DELETE the given row from
    * the SCHED_PART table.
    *
    * <ol>
    * <li>run <code>TaskPartService.removePart()</code></li>
    * </ol>
    *
    * @param aTaskPart
    *           key of the scheduled part record that is to be removed.
    * @param aHr
    *           user who remove the part.
    *
    * @exception MxException
    *               if a part with aTaskPart key does not exist for the current task
    * @exception TriggerException
    *               if a Triggering error occurs.
    */
   @Override
   public void removePart( TaskPartKey aTaskPart, HumanResourceKey aHr )
         throws MxException, TriggerException {
      iSTaskBean.removePart( aTaskPart, aHr );
   }


   /**
    * Removes steps from a task
    *
    * @param aTask
    *           the task
    * @param aStep
    *           the steps
    *
    * @throws MxException
    *            If the task is historic
    */
   @Override
   public void removeSteps( TaskKey aTask, SchedStepKey[] aStep ) throws MxException {
      iSTaskBean.removeSteps( aTask, aStep );
   }


   /**
    * Removes a zone from the current task.
    *
    * @param aTask
    *           The task to which the zone belongs and will be removed
    * @param aZone
    *           a Zone to be assigned.
    * @param aAuthorizingHr
    *           the authorizing hr
    *
    * @exception MxException
    *               If aTask is not specified
    * @throws TriggerException
    *            If a trigger error occurs
    */
   @Override
   public void removeZone( TaskKey aTask, ZoneKey[] aZone, HumanResourceKey aAuthorizingHr )
         throws MxException, TriggerException {
      iSTaskBean.removeZone( aTask, aZone, aAuthorizingHr );
   }


   /**
    * This method forces updates to part requests for the given check. It updates requested
    * location, and needed by date of each part request. Auto reservation logic is triggered as
    * well.
    *
    * @param aCheck
    *           The work package
    * @param aAuthorizingHr
    *           human resource authorizing the part request.
    * @param aNote
    *           note
    *
    * @throws MxException
    *            if<code>aAuthorizingHr</code> is not provided
    * @throws TriggerException
    *            trigger exception
    */
   @Override
   public void requestParts( TaskKey aCheck, HumanResourceKey aAuthorizingHr, String aNote )
         throws MxException, TriggerException {
      iSTaskBean.requestParts( aCheck, aAuthorizingHr, aNote );
   }


   /**
    * Reschedules a task with custom deadlines.
    *
    * @param aTask
    *           the task key
    *
    * @throws MxException
    *            if the task cannot be rescheduled.
    * @throws TriggerException
    *            if a trigger error occurs.
    */
   @Override
   public void rescheduleTask( TaskKey aTask ) throws MxException, TriggerException {
      iSTaskBean.rescheduleTask( aTask );
   }


   /**
    * Schedules the check / work order to be done internally
    *
    * @param aCheck
    *           the check / work order to schedule
    * @param aTO
    *           the TO
    * @param aAuthorizingHr
    *           the authorizing hr
    *
    * @throws MxException
    *            if aCheck, aTO or aAuthorizingHr is null
    * @throws TriggerException
    *            if there was a problem with the MX_TS_SCHEDULE_INTERNAL trigger.
    */
   @Override
   public void schedule( TaskKey aCheck, ScheduleInternalTO aTO, HumanResourceKey aAuthorizingHr )
         throws MxException, TriggerException {
      iSTaskBean.schedule( aCheck, aTO, aAuthorizingHr );
   }


   /**
    * Schedules the check / work order to be done externally
    *
    * @param aCheck
    *           the check / work order to schedule
    * @param aTO
    *           the TO
    * @param aAuthorizingHr
    *           the authorizing hr
    *
    * @throws MxException
    *            if an error from business logic occurs
    * @throws TriggerException
    *            if there was a problem with the MX_TS_SCHEDULE_EXTERNAL trigger.
    */
   @Override
   public void schedule( TaskKey aCheck, ScheduleExternalTO aTO, HumanResourceKey aAuthorizingHr )
         throws MxException, TriggerException {
      iSTaskBean.schedule( aCheck, aTO, aAuthorizingHr );
   }


   /**
    * Schedule a new part requirement on a task.
    *
    * @param aTaskKey
    *           task pk
    * @param aPartReqTO
    *           part requirement information data structure
    *
    * @return pk of the new part requirement
    *
    * @throws MxException
    *            if the TO object is null
    * @throws TriggerException
    *            if there is a trigger-related exception
    */
   @Override
   public TaskPartKey schedulePartRequirement( TaskKey aTaskKey, PartRequirementTO aPartReqTO )
         throws MxException, TriggerException {
      return iSTaskBean.schedulePartRequirement( aTaskKey, aPartReqTO );
   }


   /**
    * Updates the collection_required_bool of a check.
    *
    * @param aRootTask
    *           The task key for the check
    * @param aCollectionRequired
    *           Boolean indicating that the check requires collection of the job cards
    *
    * @throws MxException
    *            If the task is not a check or work order
    */
   @Override
   public void setCollectionRequired( TaskKey aRootTask, boolean aCollectionRequired )
         throws MxException {
      iSTaskBean.setCollectionRequired( aRootTask, aCollectionRequired );
   }


   /**
    * Sets the contact information for a task.
    *
    * @param aTaskKey
    *           pk for the task being updated
    * @param aContactInfo
    *           the new contact information
    *
    * @throws MxException
    *            if the task pk is invalid
    */
   @Override
   public void setContactInformation( TaskKey aTaskKey, String aContactInfo ) throws MxException {
      iSTaskBean.setContactInformation( aTaskKey, aContactInfo );
   }


   /**
    * Updates the enforce_workscope_bool of a check.
    *
    * @param aTaskKey
    *           The task key for the check
    * @param aEnforceWorkscopeOrder
    *           Boolean indicating that the check is for enforcing the workscope order
    *
    * @throws MxException
    *            If the task is not a check or work order
    */
   @Override
   public void setEnforceWorkscopeOrder( TaskKey aTaskKey, boolean aEnforceWorkscopeOrder )
         throws MxException {
      iSTaskBean.setEnforceWorkscopeOrder( aTaskKey, aEnforceWorkscopeOrder );
   }


   /**
    * Sets a new value for the estimated duration property.
    *
    * @param aTaskKey
    *           The task key for the the work package
    * @param aEstimatedDuration
    *           the new value to assign to the estimated duration
    *
    * @throws MxException
    *            if the the task is not a work package
    */
   @Override
   public void setEstimatedDuration( TaskKey aTaskKey, Double aEstimatedDuration )
         throws MxException {
      iSTaskBean.setEstimatedDuration( aTaskKey, aEstimatedDuration );
   }


   @Override
   public void setETOPSSignificant( TaskKey aTaskKey, boolean aETOPS ) throws MxException {
      iSTaskBean.setETOPSSignificant( aTaskKey, aETOPS );
   }


   /**
    * Updates the heavy_bool of a check.
    *
    * @param aTaskKey
    *           The task key for the check
    * @param aHeavy
    *           Boolean indicating that the check is for heavy maintenance
    *
    * @throws MxException
    *            If the task is not a check or work order
    */
   @Override
   public void setHeavyMaintenance( TaskKey aTaskKey, boolean aHeavy ) throws MxException {
      iSTaskBean.setHeavyMaintenance( aTaskKey, aHeavy );
   }


   /**
    * Sets a new value for the Installed Part property.
    *
    * @param aInstPartKey
    *           the new value for the Installed Part property.
    * @param aInstPartTO
    *           the new value for the Installed Part property.
    *
    * @throws MxException
    *            if the inst part key is invalid
    */
   @Override
   public void setInstalledPart( TaskInstPartKey aInstPartKey, InstalledPartTO aInstPartTO )
         throws MxException {
      iSTaskBean.setInstalledPart( aInstPartKey, aInstPartTO );
   }


   /**
    * Sets the installed part's serial number based on the specified inventory
    *
    * @param aTaskInspPartKey
    *           pk of the installed part
    * @param aInventoryKey
    *           inventory pk
    *
    * @throws MxException
    *            if the task inst part pk or inventory pk are invalid
    */
   @Override
   public void setInstalledPartInventory( TaskInstPartKey aTaskInspPartKey,
         InventoryKey aInventoryKey ) throws MxException {
      iSTaskBean.setInstalledPartInventory( aTaskInspPartKey, aInventoryKey );
   }


   /**
    * Sets a new value for the issue account property.
    *
    * @param aTaskKey
    *           the new value for the issue account property.
    * @param aIssueToAccount
    *           the new value for the issue account property.
    *
    * @throws MxException
    *            if the accound entered is not expense account
    */
   @Override
   public void setIssueAccount( TaskKey aTaskKey, String aIssueToAccount ) throws MxException {
      iSTaskBean.setIssueAccount( aTaskKey, aIssueToAccount );
   }


   /**
    * Sets a new value for the issue account property.
    *
    * @param aTaskKey
    *           the new value for the issue account property.
    * @param aIssueToAccount
    *           the new value for the issue account property.
    *
    * @throws MxException
    *            if the issue account or the taskkey array are not provided
    */
   @Override
   public void setIssueAccount( TaskKey[] aTaskKey, String aIssueToAccount ) throws MxException {
      iSTaskBean.setIssueAccount( aTaskKey, aIssueToAccount );
   }


   /**
    * Sets the task lock to the desired value. Setting the lock to true makes the task uneditable.
    *
    * @param aTaskKey
    *           the pk of the task to lock
    * @param aLock
    *           true if the task is to be locked, false if unlocked
    *
    * @throws MxException
    *            if the task key is invalid
    */
   @Override
   public void setLock( TaskKey aTaskKey, boolean aLock ) throws MxException {
      iSTaskBean.setLock( aTaskKey, aLock );
   }


   /**
    * Modifies the given Tasks with <CODE>Mark As Adjusted For Billing</CODE> parameter
    *
    * @param aTaskKeys
    *           array of keys of the a given Tasks.
    * @param aValue
    *           value of <CODE>Mark As Adjusted ForBilling</CODE> parameter.
    *
    * @throws MxException
    *            if the task key is invalid
    */
   @Override
   public void setMarkAsAdjustedForBilling( TaskKey[] aTaskKeys, boolean aValue )
         throws MxException {
      iSTaskBean.setMarkAsAdjustedForBilling( aTaskKeys, aValue );
   }


   /**
    * Edit measurements on a task
    *
    * @param aInventoryParmData
    *           the inventory parm data primary key
    * @param aMeasurementDetailsTO
    *           the measurement details transfer object
    *
    * @throws MxException
    *            when error occurs
    */
   @Override
   public void setMeasurement( InventoryParmDataKey aInventoryParmData,
         MeasurementDetailsTO aMeasurementDetailsTO ) throws MxException {
      iSTaskBean.setMeasurement( aInventoryParmData, aMeasurementDetailsTO );
   }


   /**
    * Update the assembly position field on a part requirement
    *
    * @param aTaskPartKey
    *           pk for the part requirement
    * @param aAssemblyPosition
    *           new assembly position for the part requirement
    *
    * @throws MxException
    *            If the specified bom part for this part requirement is not tracked.
    */
   @Override
   public void setPartRequirementAssemblyPosition( TaskPartKey aTaskPartKey, int aAssemblyPosition )
         throws MxException {
      iSTaskBean.setPartRequirementAssemblyPosition( aTaskPartKey, aAssemblyPosition );
   }


   /**
    * Sets a new value for the part requirement config slot position property.
    *
    * @param aTaskPartKey
    *           the new value for the part requirement config slot position property.
    * @param aConfigSlotPosition
    *           the new value for the part requirement config slot position property.
    *
    * @throws MxException
    *            if an error from business logic occurs
    */
   @Override
   public void setPartRequirementConfigSlotPosition( TaskPartKey aTaskPartKey,
         ConfigSlotPositionKey aConfigSlotPosition ) throws MxException {
      iSTaskBean.setPartRequirementConfigSlotPosition( aTaskPartKey, aConfigSlotPosition );
   }


   /**
    * Update the note field on a part requirement
    *
    * @param aTaskPartKey
    *           pk for the part requirement
    * @param aNote
    *           new note for the part requirement
    *
    * @throws MxException
    *            if the part requirement is not found
    */
   @Override
   public void setPartRequirementNote( TaskPartKey aTaskPartKey, String aNote ) throws MxException {
      iSTaskBean.setPartRequirementNote( aTaskPartKey, aNote );
   }


   /**
    * Update the position field on a part requirement
    *
    * @param aTaskPartKey
    *           pk for the part requirement
    * @param aPosition
    *           new position for the part requirement
    *
    * @throws MxException
    *            If the specified bom part for this part requirement is not tracked.
    * @throws TriggerException
    *            if there is a trigger-related exception
    */
   @Override
   public void setPartRequirementPosition( TaskPartKey aTaskPartKey, int aPosition )
         throws MxException, TriggerException {
      iSTaskBean.setPartRequirementPosition( aTaskPartKey, aPosition );
   }


   /**
    * Makes the given Task either "parts ready" or "parts not ready", based on the value parameter.
    * This may also affect other related tasks based on the parts readiness logic (refer to
    * PartReadinessService).
    *
    * @param aTaskKeys
    *           array of keys of the a given Tasks.
    * @param aValue
    */
   @Override
   public void setPartsReady( TaskKey[] aTaskKeys, boolean aValue ) {
      iSTaskBean.setPartsReady( aTaskKeys, aValue );
   }


   /**
    * Sets a new value for the line planning automation hidden property for work packages that are
    * not heavy maintenance.
    *
    * @param aTaskKey
    *           the task key for the work package
    * @param aLPAHidden
    *           true to hide the work package from line planning automation, false to allow
    *           automated planning
    *
    * @throws MxException
    *            If the task key is not provided
    */
   @Override
   public void setPreventLinePlanningAutomation( TaskKey aTaskKey, boolean aLPAHidden )
         throws MxException {
      iSTaskBean.setPreventLinePlanningAutomation( aTaskKey, aLPAHidden );
   }


   /**
    * Changes the Release Number property of the current task.
    *
    * @param aTask
    *           Task Key
    * @param aReleaseNumber
    *           Release Number
    *
    * @throws MxException
    */
   @Override
   public void setReleaseNumber( TaskKey aTask, String aReleaseNumber ) throws MxException {
      iSTaskBean.setReleaseNumber( aTask, aReleaseNumber );
   }


   /**
    * Changes the Release Remarks property of the current task.
    *
    * @param aTask
    *           Task Key
    * @param aReleaseRemarks
    *           Release Remarks
    *
    * @throws MxException
    */
   @Override
   public void setReleaseRemarks( TaskKey aTask, String aReleaseRemarks ) throws MxException {
      iSTaskBean.setReleaseRemarks( aTask, aReleaseRemarks );
   }


   /**
    * Sets a new value for the Removed Part property.
    *
    * @param aRmvdPartKey
    *           the new value for the Removed Part property.
    * @param aRmvdPartTO
    *           the new value for the Removed Part property.
    *
    * @throws MxException
    *            if aRmvdPartKey is null, or not found.
    */
   @Override
   public void setRemovedPart( TaskRmvdPartKey aRmvdPartKey, RemovedPartTO aRmvdPartTO )
         throws MxException {
      iSTaskBean.setRemovedPart( aRmvdPartKey, aRmvdPartTO );
   }


   /**
    * Sets a new value for the Repair Quantity property.
    *
    * @param aTask
    *           work order
    * @param aRepairQt
    *           new value for repair qt
    *
    * @return rest of the items in the BATCH inventory item.
    *
    * @throws MxException
    *            if task could not be found
    */
   @Override
   public double setRepairQuantity( TaskKey aTask, Double aRepairQt ) throws MxException {
      return iSTaskBean.setRepairQuantity( aTask, aRepairQt );
   }


   /**
    * Changes the SchedHourMultiplier property of the current task.
    *
    * @param aTask
    *           Task Key
    * @param aSchedHourMultiplier
    *           Scheduled Hour Multiplier.
    * @param aEditMode
    *           Is Edit Mode
    *
    * @throws MxException
    */
   @Override
   public void setSchedHourMultiplier( TaskKey aTask, Double aSchedHourMultiplier,
         boolean aEditMode ) throws MxException {
      iSTaskBean.setSchedHourMultiplier( aTask, aSchedHourMultiplier, aEditMode );
   }


   /**
    * Sets the step order
    *
    * @param aTask
    *           the task
    * @param aTaskSteps
    *           the steps
    *
    * @throws MxException
    *            If the task is historic
    */
   @Override
   public void setStepOrder( TaskKey aTask, SchedStepKey[] aTaskSteps ) throws MxException {
      iSTaskBean.setStepOrder( aTask, aTaskSteps );
   }


   /**
    * Sets the steps for a task
    *
    * @param aTask
    *           the task
    * @param aTaskStep
    *           the steps
    * @param aDescription
    *           the step details
    *
    * @throws MxException
    *            If the task is historic
    */
   @Override
   public void setSteps( TaskKey aTask, SchedStepKey[] aTaskStep, String[] aDescription )
         throws MxException {
      iSTaskBean.setSteps( aTask, aTaskStep, aDescription );
   }


   /**
    * Makes the given Task either "tools ready" or "tools not ready", based on the value parameter.
    * This may also affect other related tasks based on the tools readiness logic (refer to
    * ToolReadinessService).
    *
    * @param aTaskKeys
    *           array of keys of the given Tasks.
    * @param aValue
    */
   @Override
   public void setToolsReady( TaskKey[] aTaskKeys, boolean aValue ) {
      iSTaskBean.setToolsReady( aTaskKeys, aValue );
   }


   /**
    * Updates the work order no. of a check.
    *
    * @param aTaskKey
    *           The task key for the check
    * @param aWorkOrderNumber
    *           the work order no.
    *
    * @throws MxException
    *            If the task is not a check or work order
    */
   @Override
   public void setWorkOrderNumber( TaskKey aTaskKey, String aWorkOrderNumber ) throws MxException {
      iSTaskBean.setWorkOrderNumber( aTaskKey, aWorkOrderNumber );
   }


   /**
    * Signs an aircraft maintenance release labour row
    *
    * @param aSchedWPSignReqKey
    *           the sign key
    * @param aCheck
    *           the check
    * @param aAircraftReleaseTO
    *           the TO
    *
    * @throws MxException
    *            if a triggering error occurs.
    * @throws TriggerException
    *            if a triggering error occurs.
    */
   @Override
   public void signAircraftRelease( SchedWPSignReqKey aSchedWPSignReqKey, TaskKey aCheck,
         AircraftReleaseTO aAircraftReleaseTO ) throws MxException, TriggerException {
      iSTaskBean.signAircraftRelease( aSchedWPSignReqKey, aCheck, aAircraftReleaseTO );
   }


   /**
    * Signs a component maintenance release labour row
    *
    * @param aSchedWPSignReqKey
    *           the sign key
    * @param aWorkOrder
    *           work order
    * @param aComponentReleaseTO
    *           the TO
    *
    * @throws MxException
    *            if a triggering error occurs.
    * @throws TriggerException
    *            if a triggering error occurs.
    */
   @Override
   public void signComponentRelease( SchedWPSignReqKey aSchedWPSignReqKey, TaskKey aWorkOrder,
         ComponentReleaseTO aComponentReleaseTO ) throws MxException, TriggerException {
      iSTaskBean.signComponentRelease( aSchedWPSignReqKey, aWorkOrder, aComponentReleaseTO );
   }


   /**
    * Suppress duplicate workscope job cards when Update Job Card to Latest Revision button is
    * clicked
    *
    * @param aJICTasks
    *           list of TaskKey of the job cards to update to the latest revision
    * @param aAuthorizingHr
    *           the authoring HR
    *
    * @throws MxException
    *            if an error occurs
    * @throws TriggerException
    *            should one occur when cancelling the part requests.
    */
   @Override
   public void suppressWorkScopeDuplicateJobCards( List<TaskKey> aJICTasks,
         HumanResourceKey aAuthorizingHr ) throws MxException, TriggerException {
      iSTaskBean.suppressWorkScopeDuplicateJobCards( aJICTasks, aAuthorizingHr );
   }


   /**
    * Toggles the soft deadline for task.
    *
    * @param aTask
    *           the task.
    * @param aAuthorizingHr
    *           the hr.
    *
    * @throws MxException
    *            if any exception is raised by maintenix.
    * @throws TriggerException
    *            if any exception is raised by maintenix.
    */
   @Override
   public void toggleSoftDeadline( TaskKey aTask, HumanResourceKey aAuthorizingHr )
         throws MxException, TriggerException {
      iSTaskBean.toggleSoftDeadline( aTask, aAuthorizingHr );
   }


   /**
    * Updates the sched_part_qt
    *
    * @param aTaskPartKey
    *           The task key for the check or work order
    *
    * @throws MxException
    */
   @Override
   public void updateSchedPartQt( TaskPartKey aTaskPartKey ) throws MxException {
      iSTaskBean.updateSchedPartQt( aTaskPartKey );
   }


   /**
    * Validates if the given child tasks can be assigned.
    *
    * @param aChildTasks
    *           The child tasks to be assigned.
    * @param aFlight
    *           The flight after which the check is to be scheduled
    *
    * @return A structure that holds the found problems.
    *
    * @throws MxException
    *            If any of the child tasks are invalid.
    */
   @Override
   public WarningCheckCapacity validateAddChildTasks( List<TaskKey> aChildTasks,
         FlightLegId aFlight ) throws MxException {
      return iSTaskBean.validateAddChildTasks( aChildTasks, aFlight );
   }


   /**
    * Validate the existing workscope order and assign new workscope order
    *
    * @param aTaskKey
    *           Check key
    * @param aIsAutoEnforce
    *           This boolean indicates whether the workscope ordering is enforced manually or by the
    *           system.
    *
    * @return false if the workscope order is violated
    *
    * @throws MxException
    *            if error occurs
    * @throws Exception
    *            if error occurs
    */
   @Override
   public boolean validateAndAssignWorkscopeOrder( TaskKey aTaskKey, boolean aIsAutoEnforce )
         throws MxException, Exception {
      return iSTaskBean.validateAndAssignWorkscopeOrder( aTaskKey, aIsAutoEnforce );
   }


   /**
    * Checks if there are requirements missing job cards.
    *
    * @param aCheck
    *           the check
    *
    * @return returns the structure that holds the found problems.
    *
    * @throws MxException
    *            if any error occurs
    */
   @Override
   public WarningPreventExecutionJICMissingReq
         validateForNonPreventExecutionRequirementsMissingJobCards( TaskKey aCheck )
               throws MxException {
      return iSTaskBean.validateForNonPreventExecutionRequirementsMissingJobCards( aCheck );
   }


   /**
    * Checks if there are requirements missing job cards or if there are requirements prevented from
    * executing because the job cards are missing.
    *
    * @param aCheck
    *           the check
    *
    * @return returns the structure that holds the found problems.
    *
    * @throws MxException
    *            if any error occurs
    */
   @Override
   public WarningPreventExecutionJICMissingReq
         validateForRequirementsMissingJobCards( TaskKey aCheck ) throws MxException {
      return iSTaskBean.validateForRequirementsMissingJobCards( aCheck );
   }


   /**
    * Checks if there are requirements missing job cards.
    *
    * @param aTask
    *           the check
    *
    * @return returns the structure that holds the found problems.
    *
    * @throws MxException
    *            if any error occurs
    */
   @Override
   public WarningPreventExecutionJICMissingReq
         validateForRequirementsMissingJobCardsSansWP( TaskKey[] aTask ) throws MxException {
      return iSTaskBean.validateForRequirementsMissingJobCardsSansWP( aTask );
   }


   /**
    * Checks if there will be tasks with deadlines before the start of the check when you schedule
    * the check with the new start date.
    *
    * @param aCheck
    *           the check
    * @param aScheduledStartDate
    *           the new scheduled start date
    *
    * @return returns the structure that holds the found problems.
    *
    * @throws MxException
    *            if aCheck or aScheduledStartDate is not provided
    */
   @Override
   public WarningSchedule validateSchedule( TaskKey aCheck, Date aScheduledStartDate )
         throws MxException {
      return iSTaskBean.validateSchedule( aCheck, aScheduledStartDate );
   }


   /**
    * Checks if there will be tasks with deadlines before the start of the check when you schedule
    * the check to the flight plan.
    *
    * @param aCheck
    *           the check
    * @param aFlight
    *           the flight
    *
    * @return returns the structure that holds the found problems.
    *
    * @throws MxException
    *            if aCheck or aFlight is not provided
    */
   @Override
   public WarningSchedule validateSchedule( TaskKey aCheck, FlightLegId aFlight )
         throws MxException {
      return iSTaskBean.validateSchedule( aCheck, aFlight );
   }


   /**
    * Send work package coordination message to MRO for integration
    *
    * @param aTaskKey
    *           the work package
    *
    * @throws MxException
    *            if an error occurs
    */
   @Override
   public void sendWorkPackageToMRO( TaskKey aTaskKey ) throws MxException {
      iSTaskBean.sendWorkPackageToMRO( aTaskKey );
   }


   @Override
   public EJBLocalHome getEJBLocalHome() throws EJBException {
      throw new UnsupportedOperationException();
   }


   @Override
   public Object getPrimaryKey() throws EJBException {
      throw new UnsupportedOperationException();
   }


   @Override
   public void remove() throws RemoveException, EJBException {
      throw new UnsupportedOperationException();
   }


   @Override
   public boolean isIdentical( EJBLocalObject aEJBLocalObject ) throws EJBException {
      return this.equals( aEJBLocalObject );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public TaskKey packageAndCompleteWithUsage( TaskKey[] aTasks, CreateQuickCheckTO aTO,
         HumanResourceKey aAuthorizingHr, UsageSnapshot[] aUsageSnapshots,
         boolean aWarningsApproved, UserTransaction aUserTransaction )
         throws MxException, TriggerException {
      return null;
   }
}
