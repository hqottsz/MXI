package com.mxi.mx.common.ejb.task;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import javax.ejb.RemoveException;

import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.message.MxMessage;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.ejb.inventory.InventoryBean;
import com.mxi.mx.core.ejb.stask.TaskBean;
import com.mxi.mx.core.ejb.stask.TaskLocal;
import com.mxi.mx.core.ejb.stask.tool.ToolBusMethods;
import com.mxi.mx.core.exception.InvalidReftermException;
import com.mxi.mx.core.flight.model.FlightLegId;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.DepartmentKey;
import com.mxi.mx.core.key.EventIetmTopicKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.IetmTopicKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefServiceTypeKey;
import com.mxi.mx.core.key.RefWorkTypeKey;
import com.mxi.mx.core.key.SchedActionKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskPartKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.key.ToolKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.key.WarrantyContractKey;
import com.mxi.mx.core.key.WarrantyKey;
import com.mxi.mx.core.production.task.domain.TaskDeadlineStartValue;
import com.mxi.mx.core.services.event.HistoricRecordException;
import com.mxi.mx.core.services.event.NonHistoricRecordException;
import com.mxi.mx.core.services.event.inventory.UsageSnapshot;
import com.mxi.mx.core.services.ietm.IetmDetailsTO;
import com.mxi.mx.core.services.location.LocationDetailsTO;
import com.mxi.mx.core.services.stask.AutoCompleteTaskLabourException;
import com.mxi.mx.core.services.stask.OpportunisticTaskTO;
import com.mxi.mx.core.services.stask.TaskService;
import com.mxi.mx.core.services.stask.WarningCancel;
import com.mxi.mx.core.services.stask.WarningCheckCapability;
import com.mxi.mx.core.services.stask.WarningCheckCapacity;
import com.mxi.mx.core.services.stask.WarningDefer;
import com.mxi.mx.core.services.stask.WarningErrorCompleteCheckWO;
import com.mxi.mx.core.services.stask.WarningLpaRunning;
import com.mxi.mx.core.services.stask.WarningNoDeadline;
import com.mxi.mx.core.services.stask.WarningSetParent;
import com.mxi.mx.core.services.stask.WarningSetParentEffectiveDt;
import com.mxi.mx.core.services.stask.WarningUnassign;
import com.mxi.mx.core.services.stask.deadline.CalendarDeadline;
import com.mxi.mx.core.services.stask.deadline.Deadline;
import com.mxi.mx.core.services.stask.deadline.DeadlineService;
import com.mxi.mx.core.services.stask.deadline.UsageDeadline;
import com.mxi.mx.core.services.stask.taskdeadline.TaskDeadlines;
import com.mxi.mx.core.services.stask.tool.Tool;
import com.mxi.mx.core.services.stask.tool.ToolService;
import com.mxi.mx.core.unittest.table.stask.SchedPart;


/**
 * Stub form home interface for task local.
 *
 * @author akovalevich
 */
public class TaskLocalStub implements TaskLocal {

   private TaskBean iTaskBean;


   /**
    * Initialized task bean to be delegated
    *
    * @param aTaskBean
    */
   public TaskLocalStub(TaskBean aTaskBean) {
      iTaskBean = aTaskBean;
   }


   /**
    * Returns reference to the local interface.
    *
    * @return the value of the local property.
    */
   public static TaskLocal getLocalInterface() {
      return TaskBean.getLocalInterface();
   }


   /**
    * This method is used to append a string to the end of the existing task action.
    *
    * @param aTaskKey
    *           current task
    *
    * @param aHr
    *           human resource
    * @param aAppendAction
    *           append action
    *
    * @throws MxException
    *            if the inventory that the task is on is locked, or the inventory that the fault is
    *            on is locked.
    */
   @Override
   public void addAction( TaskKey aTaskKey, HumanResourceKey aHr, String aAppendAction )
         throws MxException {
      iTaskBean.addAction( aTaskKey, aHr, aAppendAction );
   }


   /**
    * Assigns a task as a child of the current task. The current task will then be listed as the"
    * parentTask" on the child.
    *
    * @param aTaskKey
    *           current task
    * @param aChildTask
    *           the task to be assigned as a child.
    * @param aAuthorizingHr
    *           the person assigning the child task.
    * @param aReason
    *           the reason for assigning the child task.
    * @param aNotes
    *           the notes on the child task.
    *
    * @throws MxException
    *            if<code>aChildTask</code> is a root task. Assigns a task as a child of the current
    *            task. The current task will then be listed as the"
    * @throws TriggerException
    */
   @Override
   public void addChildTask( TaskKey aTaskKey, TaskKey aChildTask, HumanResourceKey aAuthorizingHr,
         String aReason, String aNotes ) throws MxException, TriggerException {
      iTaskBean.addChildTask( aTaskKey, aChildTask, aAuthorizingHr, aReason, aNotes );
   }


   /**
    * Assigns a crew to the current task.
    *
    * @param aTaskKey
    *           current task
    * @param aCrew
    *           a Crew to be assigned.
    * @param aAuthorizingHr
    *           the authorizing human resource
    *
    * @throws MxException
    *            - if aCrew is not specified
    */
   @Override
   public void addCrew( TaskKey aTaskKey, DepartmentKey aCrew, HumanResourceKey aAuthorizingHr )
         throws MxException {
      iTaskBean.addCrew( aTaskKey, aCrew, aAuthorizingHr );
   }


   /**
    * Assigns a fault to the current task. The current task will then be listed as
    * the"foundInTask"on the fault record.
    *
    * <ol>
    * <li>run EventDetailsService.setEventRelation() with the following arguments:</li>
    * <li style="list-style: none">
    * <ul>
    * <li>aInitiatingEvent = this Task</li>
    * <li>aInitiatedEvent = aFoundFault</li>
    * <li>aRelType = "DISCF"</li>
    * </ul>
    * </li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @param aFoundFault
    *           the fault to be assigned.
    *
    * @throws MxException
    *            if aFoundFault is not a valid key.
    */
   @Override
   public void addFoundFault( TaskKey aTaskKey, FaultKey aFoundFault ) throws MxException {
      iTaskBean.addFoundFault( aTaskKey, aFoundFault );
   }


   /**
    * Assigns an IETM to the Task.
    *
    * @param aTaskKey
    *           current task
    *
    * @param aIetmTO
    *           a Ietm to be assigned.
    *
    * @return the key of the EventIetmTopic
    *
    * @throws MxException
    *            if aIetmTO is null
    */
   @Override
   public EventIetmTopicKey addIetm( TaskKey aTaskKey, IetmDetailsTO aIetmTO ) throws MxException {
      return iTaskBean.addIetm( aTaskKey, aIetmTO );
   }


   /**
    * Assigns an IETM to the fault.
    *
    * <ul>
    * <li>add a row to EVT_IETM with EVENT_pk = current task,</li>
    * <li>EVENT_IETM_ID = max(EVENT_IETM_ID for the current task)+1,</li>
    * <li>(IETM_DB_ID, IETM_ID, IETM_TOPIC_ID) = aIetmTopic,</li>
    * <li>IETM_ORD = max(IETM_ORD for the current task)+1</li>
    * </ul>
    *
    * @param aTaskKey
    *           current task
    *
    * @param aIetmTopic
    *           a Ietm to be assigned.
    *
    * @return the key of the EventIetmTopic
    *
    * @throws MxException
    *            if the aItemTopic is not valid key.
    * @throws DuplicateKeyException
    *            if the ietm topic is already assigned to the given event
    */
   @Override
   public EventIetmTopicKey addIetm( TaskKey aTaskKey, IetmTopicKey aIetmTopic )
         throws MxException {
      return iTaskBean.addIetm( aTaskKey, aIetmTopic );
   }


   /**
    * Assigns a Tool to the TaskBean.
    *
    * <ol>
    * <li>run ToolService.addTool()</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    *
    * @param aTool
    *           a Tool to be assigned.
    *
    * @throws MxException
    *            see ToolService.addTool()
    * @throws TriggerException
    *            If an exception occurs in a trigger.
    *
    * @see ToolService#addTool
    * @see ToolBusMethods#addTool
    */
   @Override
   public void addTool( TaskKey aTaskKey, Tool aTool ) throws MxException, TriggerException {
      iTaskBean.addTool( aTaskKey, aTool );
   }


   /**
    * Assigns a usage-based deadline to the current task.
    *
    * @param aTaskKey
    *           current task
    *
    * @param aUsageDeadline
    *           the usage-based deadline to be assigned.
    * @param aHr
    *           the human resource key
    *
    * @throws MxException
    *            if a deadline with the specified iDataType already exists on the task.
    * @throws TriggerException
    *            trigger exception
    * @throws DuplicateKeyException
    *            DOCUMENT_ME
    *
    * @see DeadlineService#addUsageDeadline()
    * @see UsageDeadlineBusMethods#addUsageDeadline()
    */
   @Override
   public void addUsageDeadline( TaskKey aTaskKey, UsageDeadline aUsageDeadline,
         HumanResourceKey aHr ) throws MxException, TriggerException {
      iTaskBean.addUsageDeadline( aTaskKey, aUsageDeadline, aHr );
   }


   /**
    * This method is used to append a string to the end of the existing task Description.
    *
    * @param aTaskKey
    *           current task
    *
    * @param aHr
    *           the HR that is appending the description
    * @param aNoteToAppend
    *           The String that is to be added to the existing description string
    *
    * @throws MxException
    *            if the new String created exceeds 4000 chars
    */
   @Override
   public void appendDescription( TaskKey aTaskKey, HumanResourceKey aHr, String aNoteToAppend )
         throws MxException {
      iTaskBean.appendDescription( aTaskKey, aHr, aNoteToAppend );
   }


   /**
    * Appends a String to the existing Instruction.
    *
    * @param aTaskKey
    *           current task
    * @param aHr
    *           the HR that is appending the instruction.
    * @param aInstructionToAppend
    *           the String that is to be added to the existing instruction.
    *
    * @throws MxException
    *            if the new total instruction string length exceeds characters.
    */
   @Override
   public void appendInstruction( TaskKey aTaskKey, HumanResourceKey aHr,
         String aInstructionToAppend ) throws MxException {
      iTaskBean.appendInstruction( aTaskKey, aHr, aInstructionToAppend );
   }


   /**
    * This method will add a note to the EVT_STAGE table.
    *
    * @param aTaskKey
    *           current task
    * @param aHr
    *           the HR that is appending the note
    * @param aAppendNote
    *           the note to append
    * @param aUserNote
    *           true if the note to append is a user entered note.
    *
    * @throws MxException
    *            if aHr is not a valid key.
    */
   @Override
   public void appendNote( TaskKey aTaskKey, HumanResourceKey aHr, String aAppendNote,
         boolean aUserNote ) throws MxException {
      iTaskBean.appendNote( aTaskKey, aHr, aAppendNote, aUserNote );
   }


   /**
    * Assigns IETMs to the task.
    *
    * @param aTaskKey
    *           current task
    * @param aIetmTopics
    *           The lisf of Ietm topics to be assigned.
    *
    * @throws MxException
    *            if an error occurs
    */
   @Override
   public void assignIetms( TaskKey aTask, IetmTopicKey[] aIetmTopics ) throws MxException {
      iTaskBean.assignIetms( aTask, aIetmTopics );
   }


   /**
    * Assigns a location to the current task.
    *
    * @param aTaskKey
    *           current task
    * @param aLocationDetailsTO
    *           the location details to.
    *
    * @throws MxException
    *            - if something goes wrong.
    */
   @Override
   public void assignLocation( TaskKey aTaskKey, LocationDetailsTO aLocationDetailsTO )
         throws MxException {
      iTaskBean.assignLocation( aTaskKey, aLocationDetailsTO );
   }


   /**
    * Assign OpportunistTasks Selected into the work-package specified
    *
    * @param aTaskKey
    *           current task
    * @param aOpportunisticTasks
    *           {@link OpportunisticTaskTO} The array of Opportunistic Tasks
    *
    * @throws MxException
    *            If some business logic fails
    * @throws TriggerException
    *            If a trigger cannot be called
    */
   @Override
   public void assignOpportunisticTasks( TaskKey aTaskKey,
         OpportunisticTaskTO[] aOpportunisticTasks ) throws MxException, TriggerException {
      iTaskBean.assignOpportunisticTasks( aTaskKey, aOpportunisticTasks );
   }


   /**
    * This method cancels a root task, including all its subtasks.
    *
    * @param aTaskKey
    *           current task
    * @param aAuthorizingHr
    *           the human resource that is authorizing the task cancellation.
    * @param aReason
    *           the reason for the task cancellation.
    * @param aUserNote
    *           the user note to record against the task cancellation.
    *
    * @throws MxException
    *            if aAuthorizingHr is not a valid key.
    * @throws TriggerException
    *            if a triggering error occurs.
    * @throws InvalidReftermException
    *            if aReason is not a valid refterm.
    * @throws HistoricRecordException
    *            if the task is already historic (it was already cancelled or completed)
    * @throws StringTooLongException
    *            if aUserNote is longer than 2000 characters.
    */
   @Override
   public void cancel( TaskKey aTaskKey, HumanResourceKey aAuthorizingHr, String aReason,
         String aUserNote ) throws MxException, TriggerException {
      iTaskBean.cancel( aTaskKey, aAuthorizingHr, aReason, aUserNote );
   }


   /**
    * Marks an action as canceled
    *
    * @param aTaskKey
    *           current task
    * @param aActionKey
    *           The action to cancel
    * @param aCancelNote
    *           The note about why it was canceled
    *
    * @throws MxException
    *            If the inventory is locked.
    *
    * @deprecated This method is no longer supported. Use LabourBean.editWork instead.
    */
   @Deprecated
   @Override
   public void cancelAction( TaskKey aTaskKey, SchedActionKey aActionKey, String aCancelNote )
         throws MxException {
      iTaskBean.cancelAction( aTaskKey, aActionKey, aCancelNote );
   }


   /**
    * Clears the worklines for a check or work order.
    *
    * @param aTaskKey
    *           current task
    * @param aAuthorizingHr
    *           the HR clearing the workscope
    *
    * @throws MxException
    *            if the check's main inventory is locked.
    * @throws TriggerException
    *            trigger exception during package offwing tasks
    */
   @Override
   public void clearWorkscope( TaskKey aTaskKey, HumanResourceKey aAuthorizingHr )
         throws MxException, TriggerException {
      iTaskBean.clearWorkscope( aTaskKey, aAuthorizingHr );
   }


   /**
    * Commits the workscope of the current check or work order.
    *
    * @param aTaskKey
    *           current task
    * @param aAuthorizingHr
    *           the human resource who is committing the check.
    * @param aReason
    *           the reason for the check commitment.
    * @param aNote
    *           a note for the check commitment.
    *
    * @throws MxException
    *            if<code>aAuthorizingHr</code> is not provided.
    * @throws TriggerException
    *            if an error occurs while firing the <code>MX_TS_COMMIT</code> trigger.
    */
   @Override
   public void commitWorkscope( TaskKey aTaskKey, HumanResourceKey aAuthorizingHr, String aReason,
         String aNote ) throws MxException, TriggerException {
      iTaskBean.commitWorkscope( aTaskKey, aAuthorizingHr, aReason, aNote );
   }


   /**
    * Commits the workscope of the current check or work order.
    *
    * @param aTaskKey
    *           current task
    * @param aAuthorizingHr
    *           the human resource who is committing the check.
    * @param aReason
    *           the reason for the check commitment.
    * @param aNote
    *           a note for the check commitment.
    * @param aRevisionDate
    *           the date when the check was last modified.
    *
    * @throws MxException
    *            if<code>aAuthorizingHr</code> is not provided.
    * @throws TriggerException
    *            if an error occurs while firing the <code>MX_TS_COMMIT</code> trigger.
    */
   @Override
   public void commitWorkscope( TaskKey aTaskKey, HumanResourceKey aAuthorizingHr, String aReason,
         String aNote, Date aRevisionDate ) throws MxException, TriggerException {
      iTaskBean.commitWorkscope( aTaskKey, aAuthorizingHr, aReason, aNote, aRevisionDate );
   }


   /**
    * This method auto completes task labours and then completes a task.
    *
    * <ol>
    * <li>complete all the labours</li>
    * <li>complete the task</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @param aHr
    *           human resource key
    * @param aSetRollBackOnly
    *           forces transaction to be rolled back when exception is thrown.
    *
    * @throws MxException
    *            if aHr is not a valid key
    * @throws TriggerException
    *            if a triggering error occurs.
    */
   @Override
   public void complete( TaskKey aTaskKey, HumanResourceKey aHr, boolean aSetRollBackOnly )
         throws MxException, TriggerException {
      iTaskBean.complete( aTaskKey, aHr, aSetRollBackOnly );
   }


   /**
    * This method auto completes task labours and then completes a task. Note this method does not
    * rollback transaction when bus logic exception is thrown.
    *
    * <ol>
    * <li>complete all the labours</li>
    * <li>complete the task</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @param aHr
    *           human resource key
    * @param aCompleteTasksDate
    *           Completion date to complete tasks and labour with.
    * @param aSetRollBackOnly
    *           forces transaction to be rolled back when exception is thrown.
    *
    * @throws MxException
    *            if aHr is not a valid primary key
    * @throws TriggerException
    *            if a triggering error occurs.
    */
   @Override
   public void completeBatch( TaskKey aTaskKey, HumanResourceKey aHr, Date aCompleteTasksDate,
         boolean aSetRollBackOnly ) throws MxException, TriggerException {
      iTaskBean.completeBatch( aTaskKey, aHr, aCompleteTasksDate, aSetRollBackOnly );
   }


   /**
    * Converts a Task to Component Replacement Work Order.
    *
    * <ol>
    * <li>Try to locate an exisiting Component WO on the provided LRU Inventory</li>
    * <li>If there is no Component WO, create a new one</li>
    * <li>Add the provided list of Tasks to the Component WO</li>
    * <li>If a Source Check was provided, find the new REPL Task and assign it to the Check</li>
    * </ol>
    *
    * @param aLRUInventory
    *           The Line Replaceable Inventory to which the Tasks will be assigned
    * @param aParentTask
    *           The Source Check from which the Tasks were obtained (nullable)
    * @param aChildTasks
    *           The list of Tasks to assign the new Component Work Order
    * @param aHRKey
    *           The HR responsible for this action
    * @param aReason
    *           The Reason for this action
    * @param aNotes
    *           The Notes for this action.
    *
    * @return The Component WO that the Tasks were assigned.
    *
    * @throws MxException
    *            If the Reason was invalid
    * @throws TriggerException
    *            If there is an error in creating a new WO
    */
   @Override
   public TaskKey convertToComponentReplacement( InventoryKey aLRUInventory, TaskKey aParentTask,
         TaskKey[] aChildTasks, HumanResourceKey aHRKey, String aReason, String aNotes )
         throws MxException, TriggerException {
      return iTaskBean.convertToComponentReplacement( aLRUInventory, aParentTask, aChildTasks,
            aHRKey, aReason, aNotes );
   }


   /**
    * This method is used to defer a task. When a task is deferred, it is automatically removed from
    * any work package that it is currently part of.
    *
    * <p>
    * This method can be used on "corrective" tasks that have a faultSeverity of "MINOR": 1) if the
    * faultSeverity is AOG or UNKNOWN, then the task cannot be deferred, 2) if the faultSeverity is
    * anything else, then you must use the other @ linkdefer method.
    * </p>
    *
    * <ol>
    * <li>If this task is a corrective task (if it has a parent fault) then check for <code>
    *     InvalidFaultSeverityException</code>. This will occur if the fault's severity is anything
    * other than MINOR.</li>
    * <li>Check for other exceptions.</li>
    * <li>Call {@link TaskService#deferTask(HumanResourceKey, RefCdKey, String, String, String, *
    * FaultKey TaskService.deferTask()}.</li>
    * </ol>
    * <br>
    * <br>
    *
    * @param aTaskKey
    *           current task
    * @param aAuthorizingHr
    *           authorizing human resource.
    * @param aReason
    *           reason the task is deferred.
    * @param aUserNote
    *           user note.
    *
    * @throws MxException
    *            if the task is corrective (it has a parent fault) but the severity of the fault is
    *            anything other than MINOR.
    * @throws TriggerException
    *            if a Trigger related exception occurrs.
    */
   @Override
   public void defer( TaskKey aTaskKey, HumanResourceKey aAuthorizingHr, String aReason,
         String aUserNote ) throws MxException, TriggerException {
      iTaskBean.defer( aTaskKey, aAuthorizingHr, aReason, aUserNote );
   }


   /**
    * This method is used to defer a corrective task where the faultSeverity is anything other than
    * MINOR, UNKNOWN or AOG. This method cannot be used to defer anything other than a corrective
    * task.
    *
    * <ol>
    * <li>if the task is not corrective (it it does not have a parent fault), then throw <code>
    *     InvalidFaultSeverityException</code></li>
    * <li>If the fault's severity is MINOR or AOG, then throw <code>InvalidFaultSeverityException
    *     </code></li>
    * <li>Call {@link TaskService#deferTask(HumanResourceKey, RefCdKey, String, String, String, *
    * FaultKey TaskService.deferTask()}.</li>
    * <li>Run <code>TaskService.setDeferralClass( aDeferralClass )</code></li>
    * <li>Run <code>TaskService.setDeferralReference( aDeferralReference )</code></li>
    * <li>create a new <code>CalendarDeadline</code> class with aDeadlineDate and notifyPeriod = -1,
    * then run <code>setCalendarDeadline()</code></li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @param aAuthorizingHr
    *           authorizing human resource.
    * @param aReason
    *           reason the task is deferred.
    * @param aUserNote
    *           user note.
    * @param aDeferralClass
    *           the deferral class of the fault
    * @param aDeferralReference
    *           the deferral reference for the fault
    * @param aDueDate
    *           list of deadlines for a task, after the task deferal these are the deadlines that
    *           will be assinged to the task
    * @param aDeferralCode
    *           the Deferral Code
    *
    * @throws MxException
    *            if the task is not corrective (it does not have a parent fault) OR the severity of
    *            the fault is incorrect.
    * @throws TriggerException
    *            if a Triggering error occurs.
    */
   @Override
   public void defer( TaskKey aTaskKey, HumanResourceKey aAuthorizingHr, String aReason,
         String aUserNote, String aDeferralClass, String aDeferralReference, Deadline[] aDueDate,
         String aDeferralCode ) throws MxException, TriggerException {
      iTaskBean.defer( aTaskKey, aAuthorizingHr, aReason, aUserNote, aDeferralClass,
            aDeferralReference, aDueDate, aDeferralCode );
   }


   /**
    * This method is used to delay a work package (a check or a work order). Delaying a work package
    * is essentially used to change the ScheduledEndDate of the task.
    *
    * <ol>
    * <li>Check for exceptions.</li>
    * <li>Call {@link TaskService#delay(HumanResourceKey, String, String, Date, MxDataAccess)
    * TaskService.delay()}.</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @param aAuthorizingHr
    *           authorizing human resource.
    * @param aReason
    *           reason the task is delayed.
    * @param aUserNote
    *           user note.
    * @param aNewEndDate
    *           the new estimated end date for the task.
    *
    * @throws MxException
    *            if the aNewEndDate is dated before the task's ActualStartDate.
    * @throws TriggerException
    *            if a triggering error occurs.
    * @throws InvalidReftermException
    *            if aReason is specified, but invalid.
    * @throws StringTooLongException
    *            if aUserNote is longer than 2000 characters.
    */
   @Override
   public void delay( TaskKey aTaskKey, HumanResourceKey aAuthorizingHr, String aReason,
         String aUserNote, Date aNewEndDate ) throws MxException, TriggerException {
      iTaskBean.delay( aTaskKey, aAuthorizingHr, aReason, aUserNote, aNewEndDate );
   }


   /**
    * This method is used to edit the estimated end date of a work package (a root task).
    *
    * @param aTaskKey
    *           current task
    * @param aAuthorizingHr
    *           authorizing human resource.
    * @param aReason
    *           reason of editting the estimated end date.
    * @param aUserNote
    *           user note.
    * @param aNewEndDate
    *           the new estimated end date for the task.
    *
    * @throws MxException
    *            if the aAuthorizingHr is invalid.
    * @throws TriggerException
    *            if a triggering error occurs.
    * @throws InvalidReftermException
    *            invalid referance term exception
    * @throws StringTooLongException
    *            string too long exception
    */
   @Override
   public void editEstimatedEndDate( TaskKey aTaskKey, HumanResourceKey aAuthorizingHr,
         String aReason, String aUserNote, Date aNewEndDate ) throws MxException, TriggerException {
      iTaskBean.editEstimatedEndDate( aTaskKey, aAuthorizingHr, aReason, aUserNote, aNewEndDate );
   }


   /**
    * This method is used to edit the scheduling information of a historic check.
    *
    * @param aTaskKey
    *           current task
    * @param aScheduledStartDate
    *           Scheduled Start Date
    * @param aScheduledEndDate
    *           Scheduled End Date
    * @param aActualStartDate
    *           Actual Start Date
    * @param aActualEndDate
    *           Actual End Date
    * @param aVendorWorkOrderNumber
    *           Vendor Work Order Number
    * @param aRepairOrderVendor
    *           Repair Order Vendor Key
    * @param aRepairOrderNumber
    *           Repair Order Number
    * @param aLocation
    *           Location Key
    * @param aWorkOrderNumber
    *           DOCUMENT_ME
    *
    * @throws MxException
    *            If the aircraft is currently locked.
    * @throws TriggerException
    *            DOCUMENT ME!
    */
   @Override
   public void editSchedule( TaskKey aTaskKey, Date aScheduledStartDate, Date aScheduledEndDate,
         Date aActualStartDate, Date aActualEndDate, String aVendorWorkOrderNumber,
         VendorKey aRepairOrderVendor, String aRepairOrderNumber, LocationKey aLocation,
         String aWorkOrderNumber ) throws MxException, TriggerException {
      iTaskBean.editSchedule( aTaskKey, aScheduledStartDate, aScheduledEndDate, aActualStartDate,
            aActualEndDate, aVendorWorkOrderNumber, aRepairOrderVendor, aRepairOrderNumber,
            aLocation, aWorkOrderNumber );
   }


   /**
    * This method creates baseline task, and updates its deadlines. If deadlines are passed in and
    * the task is on an aircraft, you should recalculate the operating status using
    * {@link InventoryBean#calculateOperatingStatus(Date, Date)}.
    *
    * @param aInventory
    *           main inventory key.
    * @param aTaskTask
    *           task task key.
    * @param aTaskDeadlines
    *           list of deadlines to update.
    * @param aAuthorizingHr
    *           the authorizing hr
    *
    * @return new task key.
    *
    * @throws MxException
    *            if aInventory is not compatible with the task definition.
    * @throws TriggerException
    *            if a trigger exception occurs
    */
   @Override
   public TaskKey createFromTaskClass( InventoryKey aInventory, TaskTaskKey aTaskTask,
         TaskDeadlines aTaskDeadlines, HumanResourceKey aAuthorizingHr )
         throws MxException, TriggerException {
      return iTaskBean.createFromTaskClass( aInventory, aTaskTask, aTaskDeadlines, aAuthorizingHr );
   }


   /**
    * Creates a new replacement task for an inventory 'hole', using a task definition.
    *
    * @param aTaskDefinition
    *           the definition that defines the replacement task
    * @param aParentInventory
    *           the parent inventory of the hole
    * @param aBomItemPosition
    *           the BOM item position of the hole
    * @param aAuthorizingHr
    *           the human resource authorizing the task creation
    *
    * @return the reference to the new replacement task
    *
    * @throws MxException
    *            if auto_apply_bool is true or dependent predecessors exist for this inventory
    *            assembly tree
    * @throws TriggerException
    *            if an error occurs when firing the MX_PR_CREATE trigger
    */
   @Override
   public TaskKey createFromReplacementTaskDefinition( TaskTaskKey aTaskDefinition,
         InventoryKey aParentInventory, ConfigSlotPositionKey aBomItemPosition,
         HumanResourceKey aAuthorizingHr ) throws MxException, TriggerException {
      return iTaskBean.createFromReplacementTaskDefinition( aTaskDefinition, aParentInventory,
            aBomItemPosition, aAuthorizingHr );
   }


   /**
    * Creates a new task based on a Task Definition.
    *
    * <ol>
    * <li>Check for exceptions.</li>
    * <li><code>aTaskPk</code> should be used as the new TaskPk if it was provided.</li>
    * <li>Run StoredProcedureCall.createTaskFromDefinition()</li>
    * <li>Initialize the service classes.</li>
    * </ol>
    *
    * @param aTaskPk
    *           task primary key of a task to be created. Can be null.
    * @param aInventory
    *           the inventory item that the task will be created against.
    * @param aTaskTask
    *           the task definition that the new task will be generated from.
    * @param aPreviousCompletionDt
    *           the completion date of the previous task
    * @param aAuthorizingHr
    *           the human resource that is authorizing the task creation.
    *
    * @return the primary key of the new task
    *
    * @throws MxException
    *            (not used - needed for EJB)
    * @throws TriggerException
    *            if a triggering error occurs.
    */
   @Override
   public TaskKey createFromTaskDefinition( TaskKey aTaskPk, InventoryKey aInventory,
         TaskTaskKey aTaskTask, Date aPreviousCompletionDt, HumanResourceKey aAuthorizingHr )
         throws MxException, TriggerException {
      return iTaskBean.createFromTaskDefinition( aTaskPk, aInventory, aTaskTask,
            aPreviousCompletionDt, aAuthorizingHr );
   }


   /**
    * This method creates task and lets the user mark a task definition as "not applicable". Task
    * becomes historic and N/A.
    *
    * @param aInventory
    *           inventory key
    * @param aTaskTask
    *           task task key
    * @param aReason
    *           reason
    * @param aAuthorizingHr
    *           authorizing human resource
    * @param aNote
    *           a note
    *
    * @return task key
    *
    * @throws MxException
    *            if aInventory, aTaskDefinition, aAuthorizingHer is not a valid key.
    * @throws TriggerException
    *            if a triggering error occurs.
    * @throws InvalidReftermException
    *            if aReason is not a valid refterm.
    * @throws StringTooLongException
    *            if aNote is longer than 4000 characters.
    */
   @Override
   public TaskKey createNonApplicable( InventoryKey aInventory, TaskTaskKey aTaskTask,
         String aReason, HumanResourceKey aAuthorizingHr, String aNote )
         throws MxException, TriggerException {
      return iTaskBean.createNonApplicable( aInventory, aTaskTask, aReason, aAuthorizingHr, aNote );
   }


   /**
    * Creates a new task based on a Task Definition.
    *
    * <ol>
    * <li>Check for exceptions.</li>
    * <li><code>aTaskPk</code> should be used as the new TaskPk if it was provided.</li>
    * <li>Run StoredProcedureCall.createTaskFromDefinition()</li>
    * <li>Initialize the service classes.</li>
    * </ol>
    *
    * @param aTaskPk
    *           task primary key of a task to be created. Can be null.
    * @param aInventory
    *           the inventory item that the task will be created against.
    * @param aTaskTask
    *           the task definition that the new task will be generated from.
    * @param aPreviousCompletionDt
    *           the completion date of the previous task
    * @param aAuthorizingHr
    *           the human resource that is authorizing the task creation.
    * @param aParentTask
    *           the task to which the created task will be assigned after this logic is completed,
    *           if null then new task may be auto assigned
    *
    * @return the primary key of the new task
    *
    * @throws MxException
    *            (not used - needed for EJB)
    * @throws TriggerException
    *            if a triggering error occurs.
    * @throws DuplicateKeyException
    *            if the ietm topic is already assigned to the given event
    */
   @Override
   public TaskKey createFromTaskDefinition( TaskKey aTaskPk, InventoryKey aInventory,
         TaskTaskKey aTaskTask, Date aPreviousCompletionDt, HumanResourceKey aAuthorizingHr,
         TaskKey aParentTask ) throws MxException, TriggerException {
      return iTaskBean.createFromTaskDefinition( aTaskPk, aInventory, aTaskTask,
            aPreviousCompletionDt, aAuthorizingHr, aParentTask );
   }


   /**
    * Creates a new task based on a Task Class.
    *
    * <ol>
    * <li>Check for exceptions.</li>
    * <li><code>aTaskPk</code> should be used as the new TaskPk if it was provided.</li>
    * <li>Call {@link TaskService#createTask(InventoryKey, RefCdKey, String, HumanResourceKey, *
    * MxDataAccess TaskService.createTask()}.</li>
    * <li>Initialize the service classes.</li>
    * </ol>
    *
    * @param aTaskPk
    *           task primary key of a task to be created. Can be null.
    * @param aInventory
    *           the inventory item that the task will be created against.
    * @param aTaskClass
    *           the class of the new, generated task.
    * @param aName
    *           the name of the new task
    * @param aCreateDefaultLabour
    *           If we create the default labour row specified by the config_parm
    * @param aAuthorizingHr
    *           the human resource that is authorizing the task creation.
    *
    * @return the primary key of the new task
    *
    * @throws MxException
    *            if aInventory, or aAuthorizingHr are not valid keys.
    * @throws TriggerException
    *            if a triggering error occurs.
    * @throws InvalidReftermException
    *            if aTaskClass is not a valid refterm.
    * @throws StringTooLongException
    *            if aName is longer than 240 chars.
    * @throws DuplicateKeyException
    *            if the ietm topic is already assigned to the given event
    */
   @Override
   public TaskKey createFromTaskClass( TaskKey aTaskPk, InventoryKey aInventory, String aTaskClass,
         String aName, boolean aCreateDefaultLabour, HumanResourceKey aAuthorizingHr )
         throws MxException, TriggerException {
      return iTaskBean.createFromTaskClass( aTaskPk, aInventory, aTaskClass, aName,
            aCreateDefaultLabour, aAuthorizingHr );
   }


   /**
    * Creates a new task based on a Task Class.
    *
    * @param aTaskPk
    *           task primary key of a task to be created. Can be null.
    * @param aInventory
    *           the inventory item that the task will be created against.
    * @param aTaskClass
    *           the class of the new, generated task.
    * @param aName
    *           the name of the new task
    * @param aCreateDefaultLabour
    *           If we create the default labour row specified by the config_parm
    * @param aAuthorizingHr
    *           the human resource that is authorizing the task creation.
    * @param aSetTaskStatusToForecast
    *           true if the task status has to be FORECAST otherwise false
    *
    * @return the primary key of the new task
    *
    * @throws MxException
    *            if aInventory, or aAuthorizingHr are not valid keys.
    * @throws TriggerException
    *            if a triggering error occurs.
    * @throws InvalidReftermException
    *            if aTaskClass is not a valid refterm.
    * @throws StringTooLongException
    *            if aName is longer than 240 chars.
    * @throws DuplicateKeyException
    *            if aTaskPk already assigned to the given event
    */
   @Override
   public TaskKey createFromTaskClass( TaskKey aTaskPk, InventoryKey aInventory, String aTaskClass,
         String aName, boolean aCreateDefaultLabour, HumanResourceKey aAuthorizingHr,
         boolean aSetTaskStatusToForecast ) throws MxException, TriggerException {
      return iTaskBean.createFromTaskClass( aTaskPk, aInventory, aTaskClass, aName,
            aCreateDefaultLabour, aAuthorizingHr, aSetTaskStatusToForecast );
   }


   /**
    * Returns the predicted deadline for this task for the given data type.
    *
    * @param aTaskKey
    *           current task
    * @param aDataType
    *           The data type.
    * @param aUsageRemaining
    *           The usage remaining
    *
    * @return The predicted deadline.
    */
   @Override
   public Date findPredictedDeadline( TaskKey aTaskKey, DataTypeKey aDataType,
         Double aUsageRemaining ) {
      return iTaskBean.findPredictedDeadline( aTaskKey, aDataType, aUsageRemaining );
   }


   /**
    * Generates the worklines for a check or work order.
    *
    * @param aTaskKey
    *           current task
    * @param aAuthorizingHr
    *           the HR generating the workscope
    *
    * @return the number of workscope lines generated
    *
    * @throws MxException
    *            if the check's main inventory is locked.
    * @throws TriggerException
    *            trigger exception during package offwing tasks
    */
   @Override
   public int generateWorkscope( TaskKey aTaskKey, HumanResourceKey aAuthorizingHr )
         throws MxException, TriggerException {
      return iTaskBean.generateWorkscope( aTaskKey, aAuthorizingHr );
   }


   /**
    * Returns the task's barcode.
    *
    * @param aTaskKey
    *           current task
    * @return task barcode.
    */
   @Override
   public String getBarcode( TaskKey aTaskKey ) {
      return iTaskBean.getBarcode( aTaskKey );
   }


   /**
    * Returns the calendarDeadline of the current task. If there is no calendar deadline for this
    * task, then it will return null.
    *
    * <ol>
    * <li>run DeadlineService.getCalendarDeadline()</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @return the calendarDeadline value.
    */
   @Override
   public CalendarDeadline getCalendarDeadline( TaskKey aTaskKey ) {
      return iTaskBean.getCalendarDeadline( aTaskKey );
   }


   /**
    * Returns the ChildTask property of the TaskBean.
    *
    * <ol>
    * <li>run TaskService.getChildTasks() and convert results into TaskKey[].</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @return The ChildTask value.
    */
   @Override
   public TaskKey[] getChildTask( TaskKey aTaskKey ) {
      return iTaskBean.getChildTask( aTaskKey );
   }


   /**
    * Returns the CorrectedFault property of the TaskBean.
    *
    * <ol>
    * <li>run EvtEventRel.getEventRelationKey(thisKey, "CORRECT") and return the result as a
    * FaultKey</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @return The CorrectedFault value.
    */
   @Override
   public FaultKey getCorrectedFault( TaskKey aTaskKey ) {
      return iTaskBean.getCorrectedFault( aTaskKey );
   }


   /**
    * Returns the drivingDeadline property of the current task. This is the"closest to overdue"
    * deadline for this task. Useful if there is more than one deadline on the task. If no deadlines
    * exist, then null will be returned.
    *
    * <ol>
    * <li>run DeadlineService.getDrivingDeadline()</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @return the drivingDeadline value
    */
   @Override
   public Deadline getDrivingDeadline( TaskKey aTaskKey ) {
      return iTaskBean.getDrivingDeadline( aTaskKey );
   }


   /**
    * Returns the Inventory property of the TaskBean.
    *
    * <ol>
    * <li>return EvtInv.getMainInventory()</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @return The Inventory value.
    */
   @Override
   public InventoryKey getInventory( TaskKey aTaskKey ) {
      return iTaskBean.getInventory( aTaskKey );
   }


   /**
    * Returns the Name property of the Event record.
    *
    * <ol>
    * <li>return EVT_EVENT.EVENT_SDESC</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @return the Name value.
    */
   @Override
   public String getName( TaskKey aTaskKey ) {
      return iTaskBean.getName( aTaskKey );
   }


   /**
    * Returns the RootTask property of the TaskBean.
    *
    * <ol>
    * <li>return EvtEvent.getHEventKey()</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @return The RootTask value.
    */
   @Override
   public TaskKey getRootTask( TaskKey aTaskKey ) {
      return iTaskBean.getRootTask( aTaskKey );
   }


   /**
    * Returns the ScheduledEndDate property of the TaskBean.
    *
    * <ol>
    * <li>return EvtEvent.getSchedEndGdt()</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @return The ScheduledEndDate value.
    */
   @Override
   public Date getScheduledEndDate( TaskKey aTaskKey ) {
      return iTaskBean.getScheduledEndDate( aTaskKey );
   }


   /**
    * Returns the Status property of the TaskBean.
    *
    * <ol>
    * <li>return EvtEvent.getEventStatusCd()</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @return The Status value.
    */
   @Override
   public String getStatus( TaskKey aTaskKey ) {
      return iTaskBean.getStatus( aTaskKey );
   }


   /**
    * Returns a single usageDeadline for the current task, using the deadline's datatype.
    *
    * <ol>
    * <li>run DeadlineService.getUsageDeadline( DataTypeKey )</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @param aDataType
    *           specifies a data type of the usage deadline to get.
    *
    * @return the UsageDeadline , or null in none found.
    *
    * @throws MxException
    *            if aDataType is null
    */
   @Override
   public UsageDeadline getUsageDeadline( TaskKey aTaskKey, DataTypeKey aDataType )
         throws MxException {
      return iTaskBean.getUsageDeadline( aTaskKey, aDataType );
   }


   /**
    * Initialize Task Warranty
    *
    * @param aTaskKey
    *           current task
    * @param aWarrantyContract
    *           Warranty Contract Key
    *
    * @return WarrantyKey
    *
    * @throws MxException
    *            if error occurs in execution
    */
   @Override
   public WarrantyKey initializeWarranty( TaskKey aTaskKey, WarrantyContractKey aWarrantyContract )
         throws MxException {
      return iTaskBean.initializeWarranty( aTaskKey, aWarrantyContract );
   }


   /**
    * Marks the current task as error.
    *
    * @param aTaskKey
    *           current task
    * @param aAuthorizingHr
    *           the human resource who is marking the task in error.
    * @param aReason
    *           the reason for marking the task in error.
    * @param aNote
    *           a note for marking the task in error.
    *
    * @throws MxException
    *            if<code>aAuthorizingHr</code> is not provided.
    * @throws TriggerException
    *            If a trigger error occurs.
    */
   @Override
   public void markAsError( TaskKey aTaskKey, HumanResourceKey aAuthorizingHr, String aReason,
         String aNote ) throws MxException, TriggerException {
      iTaskBean.markAsError( aTaskKey, aAuthorizingHr, aReason, aNote );
   }


   /**
    * Modifies task completion data
    *
    * @param aTaskKey
    *           current task
    * @param aUsageSnapshots
    *           the usage snapshots to be modified.
    * @param aActualCompletionDate
    *           DOCUMENT ME!
    * @param aCopyToSubTasks
    *           flag specifying whether or not the changes will be applied to
    * @param aAuthorizingHr
    *           The authorizing hr key.
    *
    * @throws MxException
    *            if<code>aUsageSnapshots</code> or <code>aActualCompletionDate</code> is not
    *            provided, or if any of the <code>aUsageSnapshots</code> does not have a data type.
    * @throws TriggerException
    *            If a trigger error occurs
    */
   @Override
   public void modifyCompletionData( TaskKey aTaskKey, UsageSnapshot[] aUsageSnapshots,
         Date aActualCompletionDate, boolean aCopyToSubTasks, HumanResourceKey aAuthorizingHr )
         throws MxException, TriggerException {
      iTaskBean.modifyCompletionData( aTaskKey, aUsageSnapshots, aActualCompletionDate,
            aCopyToSubTasks, aAuthorizingHr );
   }


   /**
    * Modifies task completion data
    *
    * @param aTaskKey
    *           current task
    * @param aUsageSnapshots
    *           the usage snapshots to be modified.
    * @param aActualCompletionDate
    *           DOCUMENT ME!
    * @param aCopyToSubTasks
    *           flag specifying whether or not the changes will be applied to
    * @param aAuthorizingHr
    *           The authorizing hr key.
    * @param aWarningsApproved
    *           Whether warnings have been approved or not
    * @throws MxException
    *            if<code>aUsageSnapshots</code> or <code>aActualCompletionDate</code> is not
    *            provided, or if any of the <code>aUsageSnapshots</code> does not have a data type.
    * @throws TriggerException
    *            If a trigger error occurs
    */
   @Override
   public void modifyCompletionData( TaskKey aTaskKey, UsageSnapshot[] aUsageSnapshots,
         Date aActualCompletionDate, boolean aCopyToSubTasks, HumanResourceKey aAuthorizingHr,
         boolean aWarningsApproved ) throws MxException, TriggerException {
      iTaskBean.modifyCompletionData( aTaskKey, aUsageSnapshots, aActualCompletionDate,
            aCopyToSubTasks, aAuthorizingHr, aWarningsApproved );
   }


   /**
    * Modifies the usage deadlines for a task.
    *
    * <ol>
    * <li>run DeadlineService.modifyDeadlines()</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @param aDeadlines
    *           array of usage deadlines
    * @param aHr
    *           the human resource key
    *
    * @throws MxException
    *            if the current event key does not match the aUsageDealind. iDataType key or aHr is
    *            not valid.
    * @throws TriggerException
    *            trigger exception
    *
    * @see DeadlineService#modifyDeadlines
    */
   @Override
   public void modifyDeadlines( TaskKey aTaskKey, Deadline[] aDeadlines, HumanResourceKey aHr )
         throws MxException, TriggerException {
      iTaskBean.modifyDeadlines( aTaskKey, aDeadlines, aHr );
   }


   /**
    * Removes a child task from the list for the current task.
    *
    * @param aTaskKey
    *           current task
    * @param aTask
    *           the task that will be removed
    * @param aAuthorizingHr
    *           the authorizing HR
    * @param aReason
    *           the reason
    * @param aNotes
    *           any notes on the removal
    *
    * @throws MxException
    *            if the Task key is not valid. This will happen when aTask is not a child of the
    *            current task.
    * @throws TriggerException
    *            DOCUMENT ME!
    */
   @Override
   public void removeChildTask( TaskKey aTaskKey, TaskKey aTask, HumanResourceKey aAuthorizingHr,
         String aReason, String aNotes ) throws MxException, TriggerException {
      iTaskBean.removeChildTask( aTaskKey, aTask, aAuthorizingHr, aReason, aNotes );
   }


   /**
    * Removes a crew from the current task.
    *
    * @param aTaskKey
    *           current task
    * @param aCrew
    *           a Crew to be assigned.
    *
    * @throws MxException
    *            - if aCrew is not specified
    */
   @Override
   public void removeCrew( TaskKey aTaskKey, DepartmentKey aCrew ) throws MxException {
      iTaskBean.removeCrew( aTaskKey, aCrew );
   }


   /**
    * Removes a fault that was found from the list for the current task.
    *
    * <ol>
    * <li>Check for exceptions</li>
    * <li>Delete all rows from EVT_EVENT_REL where REL_TYPE_CD = aRelType, and REL_EVENT key =
    * aInitiatedEvent Note: additional where clause is not neccessary [EVENT key = the current
    * event] since the Initiated event should not be assigned to any other.</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @param aFoundFault
    *           the key of the fault that is to be removed.
    *
    * @throws MxException
    *            if a fault with aFoundFault does not exist in the list.
    */
   @Override
   public void removeFoundFault( TaskKey aTaskKey, FaultKey aFoundFault ) throws MxException {
      iTaskBean.removeFoundFault( aTaskKey, aFoundFault );
   }


   /**
    * Removes an IETM from the Task.
    *
    * <ul>
    * <li>EVENT_pk = current task</li>
    * <li>(IETM_DB_ID, IETM_ID, IETM_TOPIC_ID) = aIetmTopic</li>
    * </ul>
    *
    * @param aTaskKey
    *           current task
    * @param aIetmTopic
    *           a Ietm to be assigned.
    *
    * @throws MxException
    *            if the inventory that the task is on is locked.
    */
   @Override
   public void removeIetm( TaskKey aTaskKey, EventIetmTopicKey aIetmTopic ) throws MxException {
      iTaskBean.removeIetm( aTaskKey, aIetmTopic );
   }


   /**
    * Removes a tool assignment from the list for the current task.
    *
    * <ol>
    * <li>run <code>EvtTool.removeTool()</code></li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @param aToolKey
    *           Key of the tool to be removed from the current task.
    *
    * @throws MxException
    *            if a tool with the aToolKey key is not assigned to the current task.
    * @throws TriggerException
    *            If an exception occurs in a trigger.
    */
   @Override
   public void removeTool( TaskKey aTaskKey, ToolKey aToolKey )
         throws MxException, TriggerException {
      iTaskBean.removeTool( aTaskKey, aToolKey );
   }


   /**
    * Removes a usage deadline from the list for the current task.
    *
    * <ol>
    * <li>run DeadlineService.removeUsageDeadline()</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @param aDataType
    *           specifies a data type of the usage deadline to be removed.
    * @param aHr
    *           the human resource
    *
    * @throws MxException
    *            if the the task does not have a usage deadline with aDataType data type or aHr is
    *            not vald.
    * @throws TriggerException
    *            trigger exception
    */
   @Override
   public void removeUsageDeadline( TaskKey aTaskKey, DataTypeKey aDataType, HumanResourceKey aHr )
         throws MxException, TriggerException {
      iTaskBean.removeUsageDeadline( aTaskKey, aDataType, aHr );
   }


   /**
    * This method is used to schedule a work package to follow a flight.
    *
    * @param aTaskKey
    *           current task
    * @param aAuthorizingHr
    *           authorizing human resource.
    * @param aFlight
    *           the flight that the work package is being scheduled to follow
    *
    * @throws MxException
    *            if the aAuthorizingHr or aFlight is invalid
    * @throws TriggerException
    *            if a triggering error occurs.
    */
   @Override
   public void schedule( TaskKey aTaskKey, HumanResourceKey aAuthorizingHr, FlightLegId aFlight )
         throws MxException, TriggerException {
      iTaskBean.schedule( aTaskKey, aAuthorizingHr, aFlight );
   }


   /**
    * Sets the actual end date of the historic task.
    *
    * @param aTaskKey
    *           current task
    * @param aEndDate
    *           actual end date
    *
    * @throws MxException
    *            if an MX error occurs
    * @throws TriggerException
    *            if a trigger error occurs
    */
   @Override
   public void setActualEndDate( TaskKey aTaskKey, Date aEndDate )
         throws MxException, TriggerException {
      iTaskBean.setActualEndDate( aTaskKey, aEndDate );
   }


   /**
    * Sets the actual end date of the historic task.
    *
    * @param aTaskKey
    *           current task
    * @param aEndDate
    *           actual end date
    * @param aWarningApproved
    *           whether warnings have been approved or not
    *
    * @throws MxException
    *            if an MX error occurs
    * @throws TriggerException
    *            if a trigger error occurs
    */
   @Override
   public void setActualEndDate( TaskKey aTaskKey, Date aEndDate, boolean aWarningApproved )
         throws MxException, TriggerException {
      iTaskBean.setActualEndDate( aTaskKey, aEndDate, aWarningApproved );
   }


   /**
    * Sets the actual start date of the historic task.
    *
    * @param aTaskKey
    *           current task
    * @param aStartDate
    *           actual start date
    *
    * @throws MxException
    *            DOCUMENT_ME
    * @throws NonHistoricRecordException
    *            this method is intened only for historic tasks.
    */
   @Override
   public void setActualStartDate( TaskKey aTaskKey, Date aStartDate ) throws MxException {
      iTaskBean.setActualStartDate( aTaskKey, aStartDate );
   }


   /**
    * Changes the AdHocRecurring property of the current task.
    *
    * <ul>
    * <li>Check for exceptions</li>
    * <li>sets <code>sched_stask.adhoc_recur_bool</code> value</li>
    * </ul>
    *
    * @param aTaskKey
    *           current task
    * @param aRecurring
    *           the new AdHocRecurring value.
    *
    * @throws MxException
    *            if the inventory that the event is on is locked.
    */
   @Override
   public void setAdHocRecurring( TaskKey aTaskKey, boolean aRecurring ) throws MxException {
      iTaskBean.setAdHocRecurring( aTaskKey, aRecurring );
   }


   /**
    * Changes the AutoComplete property of the current task.
    *
    * <ol>
    * <li>Check for exceptions</li>
    * <li>Update <code>sched_stask.auto_complete_bool</code></li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @param aAutoCompleteBool
    *           the new AutoComplete value.
    *
    * @throws MxException
    *            if the inventory that the task is on is locked.
    * @throws AutoCompleteTaskLabourException
    *            if the task is being set to auto complete and has labours assigned to it.
    */
   @Override
   public void setAutoCompleteBool( TaskKey aTaskKey, boolean aAutoCompleteBool )
         throws MxException {
      iTaskBean.setAutoCompleteBool( aTaskKey, aAutoCompleteBool );
   }


   /**
    * Sets a new value for the Barcode property.
    *
    * @param aTaskKey
    *           current task
    * @param aBarcode
    *           the new value for the Barcode property.
    * @param aAuthorizingHr
    *           the authorizing hr
    *
    * @throws MxException
    *            if aBarcode is missing
    */
   @Override
   public void setBarcode( TaskKey aTaskKey, String aBarcode, HumanResourceKey aAuthorizingHr )
         throws MxException {
      iTaskBean.setBarcode( aTaskKey, aBarcode, aAuthorizingHr );
   }


   /**
    * Changes the calendarDeadline property of the current task. To remove the calendar-based
    * deadline, then pass in null.
    *
    * <ol>
    * <li>run DeadlineService.setCalendarDeadline()</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @param aCalendarDeadline
    *           the new calendarDeadline value.
    * @param aHr
    *           the human resource key.
    *
    * @throws MxException
    *            if the inventory that the task is on is locked.
    * @throws TriggerException
    *            trigger exception
    */
   @Override
   public void setCalendarDeadline( TaskKey aTaskKey, CalendarDeadline aCalendarDeadline,
         HumanResourceKey aHr ) throws MxException, TriggerException {
      iTaskBean.setCalendarDeadline( aTaskKey, aCalendarDeadline, aHr );
   }


   /**
    * Changes the correctedFault property of the current task.
    *
    * <ol>
    * <li>Check for exceptions.</li>
    * <li>run EventDetailsService.setEventRelation( this Task, aCorrectedFault,"CORRECT" )</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @param aCorrectedFault
    *           the new correctedFault value.
    *
    * @throws MxException
    *            if the aCorrectedFault key is not valid.
    */
   @Override
   public void setCorrectedFault( TaskKey aTaskKey, FaultKey aCorrectedFault ) throws MxException {
      iTaskBean.setCorrectedFault( aTaskKey, aCorrectedFault );
   }


   /**
    * Changes the CorrectiveActionConfirmed flag to indicate whether the corrective action
    * associated with a fault actually fixed the problem.
    *
    * <ol>
    * <li>sets <code>sched_stask.corr_fix_bool</code></li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @param aCorrectiveActionConfirmed
    *           the new CorrectiveActionConfirmed value.
    *
    * @throws MxException
    *            - if the inventory that this task on is locked
    */
   @Override
   public void setCorrectiveActionConfirmed( TaskKey aTaskKey, boolean aCorrectiveActionConfirmed )
         throws MxException {
      iTaskBean.setCorrectiveActionConfirmed( aTaskKey, aCorrectiveActionConfirmed );
   }


   /**
    * Changes the CurrentEditor property of the Event record.
    *
    * <ul>
    * <li>Set the evt_event.editor_PK fields for the current event primary key.</li>
    * </ul>
    *
    * @param aTaskKey
    *           current task
    * @param aCurrentEditor
    *           the new CurrentEditor value.
    *
    * @throws MxException
    *            if the aCurrentEditor value is not a valid key in the org_hr table.
    */
   @Override
   public void setCurrentEditor( TaskKey aTaskKey, HumanResourceKey aCurrentEditor )
         throws MxException {
      iTaskBean.setCurrentEditor( aTaskKey, aCurrentEditor );
   }


   /**
    * Creates DEPT link between two task instances.
    *
    * @param aPrevTask
    *           the first task.
    * @param aTask
    *           the second task
    *
    * @throws MxException
    *            if the given task keys are not valid.
    */
   @Override
   public void setDependentSource( TaskKey aPrevTask, TaskKey aTask ) throws MxException {
      iTaskBean.setDependentSource( aPrevTask, aTask );
   }


   /**
    * Changes the Description property of the Event record.
    *
    * <ul>
    * <li>Set the evt_event.event_ldesc field for the current event primary key.</li>
    * </ul>
    *
    * @param aTaskKey
    *           current task
    * @param aDescription
    *           the new Description value.
    *
    * @throws MxException
    *            if the current event's inventory is locked.
    * @throws StringTooLongException
    *            if aDescription is longer than 4000 characters.
    */
   @Override
   public void setDescription( TaskKey aTaskKey, String aDescription ) throws MxException {
      iTaskBean.setDescription( aTaskKey, aDescription );
   }


   /**
    * Set the task as Do At Next Install If the task is in a check or work order, then unassign it
    * first
    *
    * @param aTaskKey
    *           current task
    * @param aDoAtNext
    *           a boolean value for the Do At Next Install property
    * @param aUnassignTask
    *           a boolean value to indicate if the task need to be unassigned
    * @param aAuthorizingHr
    *           the human resource who is unassigning the task.
    * @param aReason
    *           the reason for unassigning the task.
    * @param aNote
    *           a note for unassigning the task.
    * @param aSystemNote
    *           a system note saying this task will be complete during next component install
    *
    * @throws MxException
    *            if the task's ref_class_cd is a job card (JIC), it cannot be unassigned.
    * @throws TriggerException
    *            if a trigger error occurs
    */
   @Override
   public void setDoAtNextInstall( TaskKey aTaskKey, boolean aDoAtNext, boolean aUnassignTask,
         HumanResourceKey aAuthorizingHr, String aReason, String aNote, String aSystemNote )
         throws MxException, TriggerException {
      iTaskBean.setDoAtNextInstall( aTaskKey, aDoAtNext, aUnassignTask, aAuthorizingHr, aReason,
            aNote, aSystemNote );
   }


   /**
    * Changes the DocumentReference property of the Event record.
    *
    * <ul>
    * <li>Set the evt_event.doc_ref_sdesc field for the current event primary key.</li>
    * </ul>
    *
    * @param aTaskKey
    *           current task
    * @param aDocumentReference
    *           the new DocumentReference value.
    *
    * @throws MxException
    *            if the current event's inventory is locked.
    * @throws StringTooLongException
    *            if the aDocumentReference value is longer than 80 chars.
    */
   @Override
   public void setDocumentReference( TaskKey aTaskKey, String aDocumentReference )
         throws MxException {
      iTaskBean.setDocumentReference( aTaskKey, aDocumentReference );
   }


   /**
    * Changes the ETOPS property of the Task record.
    *
    * <ul>
    * <li>Set the task_task.etops_bool field for the current task primary key.</li>
    * </ul>
    *
    * @param aTaskKey
    *           current task
    * @param aETOPS
    *           the new ETOPS value.
    *
    * @throws MxException
    *            if the current event's inventory is locked.
    */
   @Override
   public void setETOPS( TaskKey aTaskKey, boolean aETOPS ) throws MxException {
      iTaskBean.setETOPS( aTaskKey, aETOPS );
   }


   public void setETOPSSignificant( TaskKey aTaskKey, boolean aETOPS ) throws MxException {
      iTaskBean.setETOPSSignificant( aTaskKey, aETOPS );
   }


   /**
    * Changes the ExternalKey property of the Event record.
    *
    * <ul>
    * <li>Set the evt_event.ext_key_sdesc field for the current event primary key.</li>
    * </ul>
    *
    * @param aTaskKey
    *           current task
    * @param aExternalKey
    *           the new ExternalKey value.
    *
    * @throws MxException
    *            if the current event's inventory is locked.
    * @throws StringTooLongException
    *            if the aExternalKey value is longer than 80 chars.
    */
   @Override
   public void setExternalKey( TaskKey aTaskKey, String aExternalKey ) throws MxException {
      iTaskBean.setExternalKey( aTaskKey, aExternalKey );
   }


   /**
    * Changes the order of specified IETM in context of the task.
    *
    * <ul>
    * <li>sets the EVT_IETM.IETM_ORD=aIetmOrd where EVENT_pk = current task and (IETM_DB_ID,
    * IETM_ID, IETM_TOPIC_ID) = aIetmTopic</li>
    * </ul>
    *
    * @param aTaskKey
    *           current task
    * @param aIetmTopic
    *           a Ietm to be assigned.
    * @param aIetmOrd
    *           the new IetmOrd value.
    *
    * @throws MxException
    *            if the inventory that the task is on is locked.
    */
   @Override
   public void setIetmOrd( TaskKey aTaskKey, EventIetmTopicKey aIetmTopic, int aIetmOrd )
         throws MxException {
      iTaskBean.setIetmOrd( aTaskKey, aIetmTopic, aIetmOrd );
   }


   /**
    * Sets task instructions.
    *
    * @param aTaskKey
    *           current task
    * @param aInstruction
    *           task instruction
    *
    * @throws MxException
    *            if the inventory that the task is on is locked.
    * @throws StringTooLongException
    *            if the aInstruction is more than 4000 characters long.
    */
   @Override
   public void setInstruction( TaskKey aTaskKey, String aInstruction ) throws MxException {
      iTaskBean.setInstruction( aTaskKey, aInstruction );
   }


   /**
    * Sets a new value for the issue account property.
    *
    * @param aTaskKey
    *           current task
    * @param aIssueAccount
    *           the new value for the issue account property.
    *
    * @throws MxException
    *            the inventory is locked
    */
   @Override
   public void setIssueAccount( TaskKey aTaskKey, String aIssueAccount ) throws MxException {
      iTaskBean.setIssueAccount( aTaskKey, aIssueAccount );
   }


   /**
    * Changes the issuedDate property of the current task.
    *
    * <ol>
    * <li>Check for exceptions</li>
    * <li>Update SCHED_STASK.ISSUED_GDT</li>
    * <li>Update SCHED_STASK.ISSUED_DT</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @param aIssuedDate
    *           the new issuedDate value.
    *
    * @throws MxException
    *            if the inventory that the task is on is locked.
    */
   @Override
   public void setIssuedDate( TaskKey aTaskKey, Date aIssuedDate ) throws MxException {
      iTaskBean.setIssuedDate( aTaskKey, aIssuedDate );
   }


   /**
    * Sets a new value for the minimum planning yield property.
    *
    * @param aTaskKey
    *           current task
    * @param aAttributeName
    *           the attribute name being validated
    * @param aMinimumPlanningYield
    *           the new value for the minimum planning yield property.
    *
    * @throws MxException
    *            the minimum planning yield is not a valid percentage
    */
   @Override
   public void setMinimumPlanningYield( TaskKey aTaskKey, String aAttributeName,
         Double aMinimumPlanningYield ) throws MxException {
      iTaskBean.setMinimumPlanningYield( aTaskKey, aAttributeName, aMinimumPlanningYield );
   }


   /**
    * Changes the name property of the current event.
    *
    * <ol>
    * <li>Set the EVT_EVENT.EVENT_SDESC for the current event.</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @param aName
    *           the new name value.
    *
    * @throws MxException
    *            if the current event's inventory is locked.
    * @throws StringTooLongException
    *            if aName is longer than 240 characters.
    */
   @Override
   public void setName( TaskKey aTaskKey, String aName ) throws MxException {
      iTaskBean.setName( aTaskKey, aName );
   }


   /**
    * Changes the order number property of the current task.
    *
    * <ol>
    * <li>Check for exceptions</li>
    * <li>Update EVT_EVENT.SUB_EVENT_ORD</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @param aOrderNumber
    *           the new order number value.
    *
    * @throws MxException
    *            if the inventory that the task is on is locked.
    */
   @Override
   public void setOrderNumber( TaskKey aTaskKey, int aOrderNumber ) throws MxException {
      iTaskBean.setOrderNumber( aTaskKey, aOrderNumber );
   }


   /**
    * Changes the originator property of the current task.
    *
    * <ol>
    * <li>Check for exceptions</li>
    * <li>Update SCHED_STASK.TASK_ORIGINATOR foreign key</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @param aOriginator
    *           the new originator value.
    *
    * @throws MxException
    *            if the inventory that the task is on is locked.
    * @throws InvalidReftermException
    *            if aOriginator is not a valid refterm.
    */
   @Override
   public void setOriginator( TaskKey aTaskKey, String aOriginator ) throws MxException {
      iTaskBean.setOriginator( aTaskKey, aOriginator );
   }


   /**
    * DOCUMENT ME!
    *
    * @param aTaskKey
    *           current task
    * @param aParentTask
    *           the new ParentTask value.
    * @param aAuthorizingHr
    *           the person creating the ParenTask.
    * @param aReason
    *           the reason for creating the ParentTask.
    * @param aNotes
    *           notes on the ParentTask.
    *
    * @throws MxException
    *            if the aParentTask key is not valid.
    * @throws TriggerException
    *            if a trigger error occurs
    */
   @Override
   public void setParentTask( TaskKey aTaskKey, TaskKey aParentTask,
         HumanResourceKey aAuthorizingHr, String aReason, String aNotes )
         throws MxException, TriggerException {
      iTaskBean.setParentTask( aTaskKey, aParentTask, aAuthorizingHr, aReason, aNotes );
   }


   /**
    * Sets the plan by date of the non-historic non-root tasks.
    *
    * @param aTaskKey
    *           sched_stask.barcode_sdesc
    * @param aPlanByDate
    *           task plan by date
    * @param aAuthorizingHr
    *           the person setting Plan By Date.
    *
    * @throws MxException
    */
   @Override
   public void setPlanByDate( TaskKey aTaskKey, Date aPlanByDate, HumanResourceKey aAuthorizingHr )
         throws MxException {
      iTaskBean.setPlanByDate( aTaskKey, aPlanByDate, aAuthorizingHr );
   }


   /**
    * Changes the reason property of the current task.
    *
    * <ol>
    * <li>update EVT_EVENT.EVENT_REASON_CD for the current task</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @param aReason
    *           the new reason value.
    *
    * @throws MxException
    *            if the current event's inventory is locked.
    * @throws InvalidReftermException
    *            if aReason is not a valid refterm.
    */
   @Override
   public void setReason( TaskKey aTaskKey, String aReason ) throws MxException {
      iTaskBean.setReason( aTaskKey, aReason );
   }


   /**
    * Changes received condition.
    *
    * @param aTaskKey
    *           current task
    * @param aInvCondition
    *           the new reason value.
    *
    * @throws MxException
    *            if aInvCondition is not a valid refterm.
    */
   @Override
   public void setReceivedCondition( TaskKey aTaskKey, String aInvCondition ) throws MxException {
      iTaskBean.setReceivedCondition( aTaskKey, aInvCondition );
   }


   /**
    * Sets the fault source for a recurring task. Returns a row from <code>evt_event_rel</code>
    * where:
    *
    * <ul>
    * <li><code>event_pk</code> = current task</li>
    * <li><code>rel_type_pk</code> = <code>0:recsrc</code></li>
    * </ul>
    *
    * @param aTaskKey
    *           current task
    * @param aRecurringSource
    *           the new RecurringSource value.
    *
    * @throws MxException
    *            if the aRecurringSource is not a valid key,
    */
   @Override
   public void setRecurringSource( TaskKey aTaskKey, FaultKey aRecurringSource )
         throws MxException {
      iTaskBean.setRecurringSource( aTaskKey, aRecurringSource );
   }


   /**
    * Changes the referenceDescription property of the current task.
    *
    * <ol>
    * <li>run SCHED_STASK.TASK_REF_SDESC</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @param aReferenceDescription
    *           the new referenceDescription value.
    *
    * @throws MxException
    *            if the inventory that the task is on is locked.
    * @throws StringTooLongException
    *            if aReferenceDescription is longer than 80 characters.
    */
   @Override
   public void setReferenceDescription( TaskKey aTaskKey, String aReferenceDescription )
         throws MxException {
      iTaskBean.setReferenceDescription( aTaskKey, aReferenceDescription );
   }


   /**
    * Changes the repairOrderId property of the current task.
    *
    * <ol>
    * <li>Check for exceptions</li>
    * <li>Update SCHED_STASK.RO_REF_SDESC</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @param aRepairOrderId
    *           the new repairOrderId value.
    *
    * @throws MxException
    *            if the inventory that the task is on is locked.
    * @throws StringTooLongException
    *            if aRepairOrderId is longer than 80 characters.
    */
   @Override
   public void setRepairOrderId( TaskKey aTaskKey, String aRepairOrderId ) throws MxException {
      iTaskBean.setRepairOrderId( aTaskKey, aRepairOrderId );
   }


   /**
    * Changes the RepairOrderLineId property of the current task.
    *
    * <ol>
    * <li>Check for exceptions.</li>
    * <li>Update SCHED_STASK.RO_LINE_SDESC field.</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @param aRepairOrderLineId
    *           the new RepairOrderLineId value.
    *
    * @throws MxException
    *            if the inventory that the task is on is locked.
    * @throws StringTooLongException
    *            if aRepairOrderLineId is longer than 80 characters.
    */
   @Override
   public void setRepairOrderLineId( TaskKey aTaskKey, String aRepairOrderLineId )
         throws MxException {
      iTaskBean.setRepairOrderLineId( aTaskKey, aRepairOrderLineId );
   }


   /**
    * Changes the repairOrderVendor property of the current task.
    *
    * <ol>
    * <li>Check for exceptions</li>
    * <li>Update SCHED_STASK.RO_VENDOR foreign key</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @param aRepairOrderVendor
    *           the new repairOrderVendor value.
    *
    * @throws MxException
    *            if the aRepairOrderVendor key is not valid.
    */
   @Override
   public void setRepairOrderVendor( TaskKey aTaskKey, VendorKey aRepairOrderVendor )
         throws MxException {
      iTaskBean.setRepairOrderVendor( aTaskKey, aRepairOrderVendor );
   }


   /**
    * This method updates resource summary flag for this task.
    *
    * <ol>
    * <li>Check for exceptions</li>
    * <li>Update SCHED_STASK.RESOURCE_SUM_BOOL</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @param aResourceSummary
    *           true if resource summary required.
    *
    * @throws MxException
    *            - if the inventory that this task on is locked
    */
   @Override
   public void setResourceSummary( TaskKey aTaskKey, boolean aResourceSummary ) throws MxException {
      iTaskBean.setResourceSummary( aTaskKey, aResourceSummary );
   }


   /**
    * Changes the Routine property of the current task.
    *
    * <ol>
    * <li>Check for exceptions</li>
    * <li>Update SCHED_STASK.ROUTINE_BOOL</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @param aRoutine
    *           the new Routine value.
    *
    * @throws MxException
    *            if the inventory that the task is on is locked.
    */
   @Override
   public void setRoutine( TaskKey aTaskKey, boolean aRoutine ) throws MxException {
      iTaskBean.setRoutine( aTaskKey, aRoutine );
   }


   /**
    * This method changes the part note property of the given scheduled part.
    *
    * <ol>
    * <li>Check for exceptions.</li>
    * <li>Call {@link SchedPart#setPartNote(String) SchedPart.setPartNote()}.</li>
    * </ol>
    * <br>
    * <br>
    *
    * @param aTaskKey
    *           current task
    * @param aSchedPart
    *           the PK of the scheduled part.
    * @param aNote
    *           the new part note value.
    *
    * @throws MxException
    *            if aSchedPart is not provided.
    * @throws StringTooLongException
    *            if aNote is longer than 4000 characters.
    */
   @Override
   public void setSchedPartNote( TaskKey aTaskKey, TaskPartKey aSchedPart, String aNote )
         throws MxException {
      iTaskBean.setSchedPartNote( aTaskKey, aSchedPart, aNote );
   }


   /**
    * Sets the scheduled end date of the historic task.
    *
    * @param aTaskKey
    *           current task
    * @param aEndDate
    *           scheduled end date
    *
    * @throws MxException
    *            DOCUMENT_ME
    * @throws NonHistoricRecordException
    *            this method is intened only for historic tasks.
    */
   @Override
   public void setScheduledEndDate( TaskKey aTaskKey, Date aEndDate ) throws MxException {
      iTaskBean.setScheduledEndDate( aTaskKey, aEndDate );
   }


   /**
    * Sets the scheduled start date of the historic root and non-root tasks, also non-historic non-
    * root tasks.
    *
    * @param aTaskKey
    *           current task
    * @param aStartDate
    *           scheduled start date
    *
    * @throws MxException
    *            this method is only intended to be run on active subtasks.
    * @throws TriggerException
    *            If something goes wrong with trigger.
    */
   @Override
   public void setScheduledStartDate( TaskKey aTaskKey, Date aStartDate )
         throws MxException, TriggerException {
      iTaskBean.setScheduledStartDate( aTaskKey, aStartDate );
   }


   /**
    * Update the vendor service types while creating and updating a work package.
    *
    * <ol>
    * <li>Check for exceptions</li>
    * <li>Update SCHED_WORK_TYPE refterm</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @param aServiceTypes
    *           the new workType value.
    *
    * @throws MxException
    *            if the inventory that the task is on is locked.
    */
   @Override
   public void setServiceTypes( TaskKey aTaskKey, List<RefServiceTypeKey> aServiceTypes )
         throws MxException {
      iTaskBean.setServiceTypes( aTaskKey, aServiceTypes );
   }


   /**
    * Changes the TaskPriority property of the current task.
    *
    * <ol>
    * <li>Check for exceptions</li>
    * <li>Update SCHED_STASK.TASK_PRIORITY refterm</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @param aTaskPriority
    *           the new TaskPriority value.
    *
    * @throws MxException
    *            if the inventory that the task is on is locked.
    * @throws InvalidReftermException
    *            if aTaskPriority is not a valid refterm.
    */
   @Override
   public void setTaskPriority( TaskKey aTaskKey, String aTaskPriority ) throws MxException {
      iTaskBean.setTaskPriority( aTaskKey, aTaskPriority );
   }


   /**
    * Changes the taskSubclass property of the current task.
    *
    * <ol>
    * <li>Check for exceptions</li>
    * <li>Update SCHED_STASK.TASK_SUBCLASS refterm</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @param aTaskSubclass
    *           the new taskSubclass value.
    *
    * @throws MxException
    *            if aTaskSubClass is not a subclass of the task's current taskClass property.
    * @throws InvalidReftermException
    *            if aTaskSubclass is not a valid refterm.
    */
   @Override
   public void setTaskSubclass( TaskKey aTaskKey, String aTaskSubclass ) throws MxException {
      iTaskBean.setTaskSubclass( aTaskKey, aTaskSubclass );
   }


   /**
    * Sets the task definition (TASK_TASK) key
    *
    * @param aTaskKey
    *           current task
    * @param aTaskTaskKey
    *           new Task_Task key
    *
    * @throws MxException
    *            if the inventory that the task is on is locked.
    */
   @Override
   public void setTaskTaskKey( TaskKey aTaskKey, TaskTaskKey aTaskTaskKey ) throws MxException {
      iTaskBean.setTaskTaskKey( aTaskKey, aTaskTaskKey );
   }


   /**
    * Changes a particular tool record in the list of tool assignments for the current task.
    *
    * <ol>
    * <li>run ToolService.setTool( aTool )</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @param aTool
    *           the new values for the tool record.
    *
    * @throws MxException
    *            if aTool.iToolKey or aTool.iAssignedTool is an invalid key, or if aTool. EventKey
    *            does not match the current event.
    */
   @Override
   public void setTool( TaskKey aTaskKey, Tool aTool ) throws MxException {
      iTaskBean.setTool( aTaskKey, aTool );
   }


   /**
    * Changes a particular record in the list of usage deadlines for the current task.
    *
    * <ol>
    * <li>run DeadlineService.setUsageDeadline()</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @param aUsageDeadline
    *           the new values for the usage deadline record.
    * @param aHr
    *           the human resource key
    *
    * @throws MxException
    *            if the current event key does not match the aUsageDealind. iDataType key or aHr is
    *            not valid.
    * @throws TriggerException
    *            trigger exception
    *
    * @see DeadlineService#setUsageDeadline
    * @see UsageDeadlineBusMethods#setUsageDeadline
    */
   @Override
   public void setUsageDeadline( TaskKey aTaskKey, UsageDeadline aUsageDeadline,
         HumanResourceKey aHr ) throws MxException, TriggerException {
      iTaskBean.setUsageDeadline( aTaskKey, aUsageDeadline, aHr );
   }


   /**
    * Changes the VendorWorkOrderId property of the current task.
    *
    * <ol>
    * <li>Check for exceptions</li>
    * <li>Update SCHED_STASK.VENDOR_WO_REF_SDESC</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @param aVendorWorkOrderId
    *           the new VendorWorkOrderId value.
    *
    * @throws MxException
    *            if aVendorWorkOrderId is longer than 80 characters.
    */
   @Override
   public void setVendorWorkOrderId( TaskKey aTaskKey, String aVendorWorkOrderId )
         throws MxException {
      iTaskBean.setVendorWorkOrderId( aTaskKey, aVendorWorkOrderId );
   }


   /**
    * Changes the warrantyNote property of the current task.
    *
    * <ol>
    * <li>Check for exceptions</li>
    * <li>Update SCHED_STASK.WARRANTY_NOTE</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @param aWarrantyNote
    *           the new warrantyNote value.
    *
    * @throws MxException
    *            if the inventory that the task is on is locked.
    * @throws StringTooLongException
    *            if aWarrantyNote is longer than 2000 characters.
    */
   @Override
   public void setWarrantyNote( TaskKey aTaskKey, String aWarrantyNote ) throws MxException {
      iTaskBean.setWarrantyNote( aTaskKey, aWarrantyNote );
   }


   /**
    * Changes the workDepartment property of the current task.
    *
    * <ol>
    * <li>Ensure that the new work department is of type 'MAINT'.</li>
    * <li>Run EventDetailsService.setDepartment().</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @param aWorkDepartment
    *           the new workDepartment value.
    *
    * @throws MxException
    *            if the aWorkDepartment key is not valid.
    */
   @Override
   public void setWorkDepartment( TaskKey aTaskKey, DepartmentKey aWorkDepartment )
         throws MxException {
      iTaskBean.setWorkDepartment( aTaskKey, aWorkDepartment );
   }


   /**
    * Changes the WorkLocation property of the current task.
    *
    * <ol>
    * <li>Use SQL to delete all rows in EVT_LOC where EVENT FK = the current task</li>
    * <li>Run EventDetailsService.setLocation().</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @param aWorkLocation
    *           the new workLocation value.
    *
    * @throws MxException
    *            if the aWorkLocation key is not valid.
    */
   @Override
   public void setWorkLocation( TaskKey aTaskKey, LocationKey aWorkLocation ) throws MxException {
      iTaskBean.setWorkLocation( aTaskKey, aWorkLocation );
   }


   /**
    * Changes the WorkOrderId property of the current task.
    *
    * <ol>
    * <li>Check for exceptions</li>
    * <li>Update SCHED_STASK.WO_REF_SDESC</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @param aWorkOrderId
    *           the new WorkOrderId value.
    *
    * @throws MxException
    *            if the inventory that the task is on is locked.
    * @throws StringTooLongException
    *            if aWorkOrderId is longer than 80 characters.
    */
   @Override
   public void setWorkOrderId( TaskKey aTaskKey, String aWorkOrderId ) throws MxException {
      iTaskBean.setWorkOrderId( aTaskKey, aWorkOrderId );
   }


   /**
    * Changes the workType property of the current task.
    *
    * <ol>
    * <li>Check for exceptions</li>
    * <li>Update SCHED_STASK.WORK_TYPE refterm</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @param aWorkTypes
    *           the new workType value.
    *
    * @throws MxException
    *            if the inventory that the task is on is locked.
    * @throws MandatoryArgumentException
    *            If aWorkTypes is mandatory and missing
    */
   @Override
   public void setWorkTypes( TaskKey aTaskKey, List<RefWorkTypeKey> aWorkTypes )
         throws MxException {
      iTaskBean.setWorkTypes( aTaskKey, aWorkTypes );
   }


   /**
    * This method is used to indicate that work has started on this work package. Several things
    * will happen when the work package starts: the inventory will be moved to the task's work
    * location (if the root inventory is not of clas ACFT or BATCH or if it's an ACFT and
    * "'MOVE_ACFT_TO_WORK_LOCATION'" is set to true), the condition of the inventory will be
    * modified, and the task will be marked as "IN WORK".
    *
    * <ol>
    * <li>Check for exceptions.</li>
    * <li>Call {@link TaskService#startWork(HumanResourceKey, RefCdKey, Date, InventoryKey, *
    * LocationKey,)TaskService.startWork()}.</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @param aAuthorizingHr
    *           authorizing human resource.
    * @param aActualStartDate
    *           the date that the work actually started.
    * @param aCurrentUsages
    *           the list of current usages
    *
    * @throws MxException
    *            indicates that the check/wo cannot be started because it includes a forecasted
    *            subtask
    * @throws TriggerException
    *            if a triggering error occurs.
    */
   @Override
   public void startWork( TaskKey aTaskKey, HumanResourceKey aAuthorizingHr, Date aActualStartDate,
         UsageSnapshot[] aCurrentUsages ) throws MxException, TriggerException {
      iTaskBean.startWork( aTaskKey, aAuthorizingHr, aActualStartDate, aCurrentUsages );
   }


   /**
    * This method is used to indicate that work has started on this work package. Several things
    * will happen when the work package starts: the inventory will be moved to the task's work
    * location (if the root inventory is not of clas ACFT or BATCH or if it's an ACFT and
    * "'MOVE_ACFT_TO_WORK_LOCATION'" is set to true), the condition of the inventory will be
    * modified, and the task will be marked as "IN WORK".
    *
    * <ol>
    * <li>Check for exceptions.</li>
    * <li>Call {@link TaskService#startWork(HumanResourceKey, RefCdKey, Date, InventoryKey, *
    * LocationKey,)TaskService.startWork()}.</li>
    * </ol>
    *
    * @param aTaskKey
    *           current task
    * @param aAuthorizingHr
    *           authorizing human resource.
    * @param aActualStartDate
    *           the date that the work actually started.
    * @param aCurrentUsages
    *           the list of current usages
    * @param aRevisionDate
    *           the date when the check was last modified.
    *
    * @throws MxException
    *            indicates that the check/wo cannot be started because it includes a forecasted
    *            subtask
    * @throws TriggerException
    *            if a triggering error occurs.
    */
   @Override
   public void startWork( TaskKey aTaskKey, HumanResourceKey aAuthorizingHr, Date aActualStartDate,
         UsageSnapshot[] aCurrentUsages, Date aRevisionDate ) throws MxException, TriggerException {
      iTaskBean.startWork( aTaskKey, aAuthorizingHr, aActualStartDate, aCurrentUsages,
            aRevisionDate );
   }


   /**
    * This method terminates a root task, including all its subtasks.
    *
    * @param aTaskKey
    *           current task
    * @param aAuthorizingHr
    *           the human resource that is authorizing the task termination.
    * @param aReason
    *           the reason for the task termination.
    * @param aUserNote
    *           the user note to record against the task termination.
    *
    * @throws MxException
    *            if aAuthorizingHr is not a valid key.
    * @throws TriggerException
    *            if a triggering error occurs.
    * @throws InvalidReftermException
    *            DOCUMENT_ME
    * @throws HistoricRecordException
    *            DOCUMENT_ME
    * @throws StringTooLongException
    *            DOCUMENT_ME
    */
   @Override
   public void terminate( TaskKey aTaskKey, HumanResourceKey aAuthorizingHr, String aReason,
         String aUserNote ) throws MxException, TriggerException {
      iTaskBean.terminate( aTaskKey, aAuthorizingHr, aReason, aUserNote );
   }


   /**
    * This method is used to execute specific logic that was implemented by the client in class that
    * implements MxTrigger. That new fully qualified will have to be found in utl_trigger table,
    * trigger name MX_TS_TRIGGER.
    *
    * @param aTaskKey
    *           current task
    * @param aCodeValue
    *           code flag.
    * @throws TriggerException
    *            trigger exception.
    */
   @Override
   public void trigger( TaskKey aTaskKey, String aCodeValue ) throws TriggerException {
      iTaskBean.trigger( aTaskKey, aCodeValue );
   }


   /**
    * Unassigns the task from its current check or work order.
    *
    * @param aTaskKey
    *           current task
    * @param aAuthorizingHr
    *           the human resource who is unassigning the task.
    * @param aReason
    *           the reason for unassigning the task.
    * @param aNote
    *           a note for unassigning the task.
    *
    * @throws MxException
    *            if the task's ref_class_cd is a job card (JIC), it cannot be unassigned.
    * @throws TriggerException
    *            if a trigger error occurs
    */
   @Override
   public void unassignTask( TaskKey aTaskKey, HumanResourceKey aAuthorizingHr, String aReason,
         String aNote ) throws MxException, TriggerException {
      iTaskBean.unassignTask( aTaskKey, aAuthorizingHr, aReason, aNote );
   }


   /**
    * Uncommits the workscope of a check or work order, changing the task's status from <code>
    * COMMIT</code> to <code>PLAN</code>
    *
    * @param aTaskKey
    *           current task
    * @param aAuthorizingHr
    *           the human resource who is uncommitting the check.
    * @param aReason
    *           the reason for uncommitting the check.
    * @param aNote
    *           a note for uncommitting the check.
    *
    * @throws MxException
    *            if an error occurs while firing the<code>MX_TS_UNCOMMIT</code> trigger. Commits the
    *            workscope of the current check or work order.
    * @throws TriggerException
    *            if an error occurs while firing the <code>MX_TS_UNCOMMIT</code> trigger. Commits
    *            the workscope of the current check or work order.
    */
   @Override
   public void uncommitWorkscope( TaskKey aTaskKey, HumanResourceKey aAuthorizingHr, String aReason,
         String aNote ) throws MxException, TriggerException {
      iTaskBean.uncommitWorkscope( aTaskKey, aAuthorizingHr, aReason, aNote );
   }


   /**
    * Uncommits the workscope of a check or work order, changing the task's status from <code>
    * COMMIT</code> to <code>PLAN</code>
    *
    * @param aTaskKey
    *           current task
    * @param aAuthorizingHr
    *           the human resource who is uncommitting the check.
    * @param aReason
    *           the reason for uncommitting the check.
    * @param aNote
    *           a note for uncommitting the check.
    * @param aRevisionDate
    *           the date when the check was last modified.
    *
    * @throws MxException
    *            if an error occurs while firing the<code>MX_TS_UNCOMMIT</code> trigger. Commits the
    *            workscope of the current check or work order.
    * @throws TriggerException
    *            if an error occurs while firing the <code>MX_TS_UNCOMMIT</code> trigger. Commits
    *            the workscope of the current check or work order.
    */
   @Override
   public void uncommitWorkscope( TaskKey aTaskKey, HumanResourceKey aAuthorizingHr, String aReason,
         String aNote, Date aRevisionDate ) throws MxException, TriggerException {
      iTaskBean.uncommitWorkscope( aTaskKey, aAuthorizingHr, aReason, aNote, aRevisionDate );
   }


   /**
    * This method will un-do started work. This method changes the status of Work Order/Check back
    * PLAN.
    *
    * @param aTaskKey
    *           current task
    * @param aAuthorizingHr
    *           human resource
    * @param aReason
    *           reason for this operation
    * @param aNote
    *           a note.
    *
    * @throws MxException
    *            if aAuthorizingHr is not provided.
    * @throws TriggerException
    *            if MX_TS_UNDOSTARTWORK or MX_IN_CONDITION fails.
    */
   @Override
   public void undoStartWork( TaskKey aTaskKey, HumanResourceKey aAuthorizingHr, String aReason,
         String aNote ) throws MxException, TriggerException {
      iTaskBean.undoStartWork( aTaskKey, aAuthorizingHr, aReason, aNote );
   }


   /**
    * This method will un-do started work. This method changes the status of Work Order/Check back
    * PLAN.
    *
    * @param aTaskKey
    *           current task
    * @param aAuthorizingHr
    *           human resource
    * @param aReason
    *           reason for this operation
    * @param aNote
    *           a note.
    * @param aRevisionDate
    *           the date when the check was last modified.
    *
    * @throws MxException
    *            if aAuthorizingHr is not provided.
    * @throws TriggerException
    *            if MX_TS_UNDOSTARTWORK or MX_IN_CONDITION fails.
    */
   @Override
   public void undoStartWork( TaskKey aTaskKey, HumanResourceKey aAuthorizingHr, String aReason,
         String aNote, Date aRevisionDate ) throws MxException, TriggerException {
      iTaskBean.undoStartWork( aTaskKey, aAuthorizingHr, aReason, aNote, aRevisionDate );
   }


   /**
    * Validates if the given child tasks can be assigned.
    *
    * @param aTaskKey
    *           current task
    * @param aChildTasks
    *           The child tasks to be assigned.
    *
    * @return A structure that holds the found problems.
    *
    * @throws MxException
    *            If any of the child tasks are invalid.
    */
   @Override
   public WarningCheckCapacity validateAddChildTasks( TaskKey aTaskKey, TaskKey[] aChildTasks )
         throws MxException {
      return iTaskBean.validateAddChildTasks( aTaskKey, aChildTasks );
   }


   /**
    * Checks if there are any non historic tasks under current task and also lists tasks that will
    * not be canceled (these tasks will be unassigned from the parent task)
    *
    * @param aTaskKey
    *           current task
    * @return returns the structure that holds the found problems.
    *
    * @throws MxException
    *            - if an operation can not be performed since current task is forcasted.
    */
   @Override
   public WarningCancel validateCancel( TaskKey aTaskKey ) throws MxException {
      return iTaskBean.validateCancel( aTaskKey );
   }


   /**
    * Validates that the location can support the type of work being done.
    *
    * @param aTaskKey
    *           current task
    * @param aCheck
    *           The check of the task we're validating. It can be the same as the task.
    * @param aScheduledLocation
    *           The location.
    *
    * @return A structure holding the warning.
    *
    * @throws MxException
    *            If the location key is invalid.
    */
   @Override
   public WarningCheckCapability validateCapability( TaskKey aTaskKey, TaskKey aCheck,
         String aScheduledLocation ) throws MxException {
      return iTaskBean.validateCapability( aTaskKey, aCheck, aScheduledLocation );
   }


   /**
    * Validates if there are any known warnings or errors found on the check or work order. These
    * warnings and errors are configured by user.
    *
    * @param aTaskKey
    *           current task
    * @param aHr
    *           human resource key
    *
    * @return returns the structure that holds the found warning and errors.
    *
    * @throws MxException
    *            if the current task is not a root.
    */
   @Override
   public WarningErrorCompleteCheckWO validateCompleteCheckWO( TaskKey aTaskKey,
         HumanResourceKey aHr ) throws MxException {
      return iTaskBean.validateCompleteCheckWO( aTaskKey, aHr );
   }


   /**
    * This method returns lists of potential problems that are found during the installation or/and
    * removal of a part.
    *
    * @param aTaskKey
    *           current task
    * @param aAuthorizingHr
    *           the authorizing hr
    *
    * @return list of warning messages, length 0 if no warnings found
    *
    * @throws MxException
    *            throw exceptions when our logic could not find parent for the given BOM item
    *            position in the given parent inventory tree.
    * @throws TriggerException
    *            trigger exception
    */
   @Override
   public MxMessage[] validateCompletePartRequirement( TaskKey aTaskKey,
         HumanResourceKey aAuthorizingHr ) throws MxException, TriggerException {
      return iTaskBean.validateCompletePartRequirement( aTaskKey, aAuthorizingHr );
   }


   /**
    * Validates if task can be defered.
    *
    * @param aTaskKey
    *           current task
    * @return returns the structure that holds the found problems.
    */
   @Override
   public WarningDefer validateDefer( TaskKey aTaskKey ) {
      return iTaskBean.validateDefer( aTaskKey );
   }


   /**
    * Validates that lpa is running
    *
    * @param aCheck
    *           The task key.
    *
    * @return A structure holding the warning.
    *
    * @throws MxException
    *            If the task key is invalid.
    */
   @Override
   public WarningLpaRunning validateLpaRunning( TaskKey aCheck ) throws MxException {
      return iTaskBean.validateLpaRunning( aCheck );
   }


   /**
    * Validates if task doesn't have deadline
    *
    * @param aTaskKey
    *           current task
    * @return returns the structure that holds the found problems.
    */
   @Override
   public WarningNoDeadline validateNoDeadline( TaskKey aTaskKey ) {
      return iTaskBean.validateNoDeadline( aTaskKey );
   }


   /**
    * Validates that there is enough capacity for the check to be scheduled after the given flight.
    *
    * @param aTaskKey
    *           current task
    * @param aFlight
    *           The flight.
    *
    * @return A structure holding the warning.
    *
    * @throws MxException
    *            If the flight key is invalid.
    */
   @Override
   public WarningCheckCapacity validateScheduleCapacity( TaskKey aTaskKey, FlightLegId aFlight )
         throws MxException {
      return iTaskBean.validateScheduleCapacity( aTaskKey, aFlight );
   }


   /**
    * Validates that there is enough capacity for the check at the given time and location.
    *
    * @param aTaskKey
    *           current task
    * @param aScheduledStart
    *           The time.
    * @param aScheduledLocation
    *           The location.
    *
    * @return A structure holding the warning.
    *
    * @throws MxException
    *            If the location code is not valid.
    */
   @Override
   public WarningCheckCapacity validateScheduleCapacity( TaskKey aTaskKey, Date aScheduledStart,
         String aScheduledLocation ) throws MxException {
      return iTaskBean.validateScheduleCapacity( aTaskKey, aScheduledStart, aScheduledLocation );
   }


   /**
    * Validates if new parent can be assigned.
    *
    * @param aTaskKey
    *           current task
    * @param aParentTask
    *           task to which this task will be assigned.
    *
    * @return returns the structure that holds the found problems.
    *
    * @throws MxException
    *            if parent task is not a valid key
    */
   @Override
   public WarningSetParent validateSetParent( TaskKey aTaskKey, TaskKey aParentTask )
         throws MxException {
      return iTaskBean.validateSetParent( aTaskKey, aParentTask );
   }


   /**
    * Validates the effective date if new parent can be assigned.
    *
    * @param aTaskKey
    *           current task
    * @param aParentTask
    *           task to which this task will be assigned.
    *
    * @return returns the structure that holds the found problems.
    *
    * @throws MxException
    *            if parent task is not a valid key
    */
   @Override
   public WarningSetParentEffectiveDt validateSetParentEffectiveDt( TaskKey aTaskKey,
         TaskKey aParentTask ) throws MxException {
      return iTaskBean.validateSetParentEffectiveDt( aTaskKey, aParentTask );
   }


   /**
    * Validates for any warnings when unassigning the task
    *
    * @param aTaskKey
    *           current task
    * @return the warning if exists.
    */
   @Override
   public WarningUnassign validateUnassignTask( TaskKey aTaskKey ) {
      return iTaskBean.validateUnassignTask( aTaskKey );
   }


   @Override
   public EJBLocalHome getEJBLocalHome() throws EJBException {
      throw new UnsupportedOperationException();
   }


   @Override
   public TaskKey getPrimaryKey() throws EJBException {
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


   @Override
   public void editDeadlineStartValues( TaskDeadlineStartValue taskDeadlineStartValue,
         HumanResourceKey humanResourceKey ) throws MxException, TriggerException {
      iTaskBean.editDeadlineStartValues( taskDeadlineStartValue, humanResourceKey );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void removeDeadlines( TaskKey taskKey,
         Map<DataTypeKey, String> dataTypeKeyToDomainTypeMapping,
         HumanResourceKey humanResourceKey ) throws MxException, TriggerException {
      iTaskBean.removeDeadlines( taskKey, dataTypeKeyToDomainTypeMapping, humanResourceKey );

   }
}
