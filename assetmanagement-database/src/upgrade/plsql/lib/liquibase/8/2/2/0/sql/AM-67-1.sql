--liquibase formatted sql


--changeSet AM-67-1:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE SCHED_STASK_PKG

/********************************************************************************
*
* Package:     SCHED_STASK_PKG
* Description: This package is used to perform various actions on task events
*              1) Generate scheduled tasks based on task definitions,
*              2) Cancels a scheduled task
*              3) use task applicability to determine if a task applies
*
* Orig.Coder:   Siku Adam
* Recent Coder: Lindsay Linkletter
* Recent Date:  March 1, 2000
*
*********************************************************************************
*
* Copyright 1998-2000 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
IS

/*------------------------------------ SUBTYPES ----------------------------*/
-- Define a subtype for return codes
SUBTYPE typn_RetCode IS NUMBER;

/*---------------------------------- Constants -----------------------------*/

-- Basic error handling codes
icn_Success CONSTANT typn_RetCode := 1;       -- Success
icn_NoProc  CONSTANT typn_RetCode := 0;       -- No processing done
icn_Error   CONSTANT typn_RetCode := -1;      -- Error

-- return codes for the GenSchedTask procedure
icn_ApplicabilityInvalid   CONSTANT typn_RetCode := -11;
icn_InvHasIncorrectPartNo  CONSTANT typn_RetCode := -12;
icn_TaskDefnNotActive      CONSTANT typn_RetCode := -13;
icn_InvIsLocked            CONSTANT typn_RetCode := -15;
icn_TaskAlreadyExist       CONSTANT typn_RetCode := -16;

-- Sub procedure validation TRUE, FALSE
icn_True    CONSTANT typn_RetCode := 1;  -- True
icn_False   CONSTANT typn_RetCode := 0;  -- False

/*---------------------------------- Procedures -----------------------------*/

/* procedure used to generate a schedulated task */
PROCEDURE GenSchedTask (
       an_EvtEventDbId       IN evt_event.event_db_id%TYPE,
        an_EvtEventId         IN evt_event.event_id%TYPE,
        an_InvNoDbId          IN inv_inv.inv_no_db_id%TYPE,
        an_InvNoId            IN inv_inv.inv_no_id%TYPE,
        an_TaskDbId           IN sched_stask.task_db_id%TYPE ,
        an_TaskId             IN sched_stask.task_id%TYPE,
        an_PreviousTaskDbId   IN evt_event.event_db_id%TYPE,
        an_PreviousTaskId     IN evt_event.event_id%TYPE,
        ad_CompletionDate     IN evt_event.event_dt%TYPE,
        an_ReasonDbId         IN evt_stage.stage_reason_db_id%TYPE,
        an_ReasonCd           IN evt_stage.stage_reason_cd%TYPE,
        as_UserNote           IN evt_stage.user_stage_note%TYPE,
        an_HrDbId             IN org_hr.hr_db_id%TYPE,
        an_HrId               IN org_hr.hr_id%TYPE,
        ab_CalledExternally   IN BOOLEAN,
        ab_Historic           IN BOOLEAN,
        ab_CreateNATask       IN BOOLEAN,
        ad_PreviousCompletionDt   IN evt_event.sched_end_gdt%TYPE,
        on_SchedDbId          OUT evt_event.event_db_id%TYPE,
        on_SchedId            OUT evt_event.event_id%TYPE,
        on_Return             OUT typn_RetCode);

/* procedure used to generate a schedulated task */
PROCEDURE GenOneSchedTask (
       an_EvtEventDbId       IN evt_event.event_db_id%TYPE,
        an_EvtEventId         IN evt_event.event_id%TYPE,
        an_InvNoDbId          IN inv_inv.inv_no_db_id%TYPE,
        an_InvNoId            IN inv_inv.inv_no_id%TYPE,
        an_TaskDbId           IN sched_stask.task_db_id%TYPE ,
        an_TaskId             IN sched_stask.task_id%TYPE,
        an_PreviousTaskDbId   IN evt_event.event_db_id%TYPE,
        an_PreviousTaskId     IN evt_event.event_id%TYPE,
        ad_CompletionDate     IN evt_event.event_dt%TYPE,
        an_ReasonDbId         IN evt_stage.stage_reason_db_id%TYPE,
        an_ReasonCd           IN evt_stage.stage_reason_cd%TYPE,
        as_UserNote           IN evt_stage.user_stage_note%TYPE,
        an_HrDbId             IN org_hr.hr_db_id%TYPE,
        an_HrId               IN org_hr.hr_id%TYPE,
        ab_CalledExternally   IN BOOLEAN,
        ab_Historic           IN BOOLEAN,
        ab_CreateNATask       IN BOOLEAN,
        on_SchedDbId          OUT evt_event.event_db_id%TYPE,
        on_SchedId            OUT evt_event.event_id%TYPE,
        on_Return             OUT typn_RetCode);

PROCEDURE GenServiceTask(
       an_EvtEventDbId         IN evt_event.event_db_id%TYPE,
        an_EvtEventId           IN evt_event.event_id%TYPE,
        an_InvNoDbId            IN inv_inv.inv_no_db_id%TYPE,
        an_InvNoId              IN inv_inv.inv_no_id%TYPE,
        an_TaskDbId             IN sched_stask.task_db_id%TYPE ,
        an_TaskId               IN sched_stask.task_id%TYPE,
        an_PreviousTaskDbId     IN evt_event.event_db_id%TYPE,
        an_PreviousTaskId       IN evt_event.event_id%TYPE,
        an_NextTaskDbId         IN evt_event.event_db_id%TYPE,
        an_NextTaskId           IN evt_event.event_id%TYPE,
        ad_CompletionDate       IN evt_event.event_dt%TYPE,
        an_ReasonDbId           IN evt_stage.stage_reason_db_id%TYPE,
        an_ReasonCd             IN evt_stage.stage_reason_cd%TYPE,
        as_UserNote             IN evt_stage.user_stage_note%TYPE,
        an_HrDbId               IN org_hr.hr_db_id%TYPE,
        an_HrId                 IN org_hr.hr_id%TYPE,
        ab_CalledExternally     IN BOOLEAN,
        ab_Historic             IN BOOLEAN,
        ab_CreateNATask         IN BOOLEAN,
        ad_PreviousCompletionDt IN evt_event.sched_end_gdt%TYPE,
        on_SchedDbId            OUT evt_event.event_db_id%TYPE,
        on_SchedId              OUT evt_event.event_id%TYPE,
        on_Return               OUT typn_RetCode
);

PROCEDURE InsertTask(
        an_TaskDbId           IN evt_event.event_db_id%TYPE,
        an_TaskId             IN evt_event.event_id%TYPE,
        an_PreviousTaskDbId   IN evt_event.event_db_id%TYPE,
        an_PreviousTaskId     IN evt_event.event_id%TYPE,
        an_NextTaskDbId       IN evt_event.event_db_id%TYPE,
        an_NextTaskId         IN evt_event.event_id%TYPE,
        on_Return             OUT typn_RetCode
        );

/* This function will return number of actv task instances */
FUNCTION CountTaskInstances (
      an_TaskDbId            IN task_task.task_db_id%TYPE,
      an_TaskId              IN task_task.task_id%TYPE,
      an_InvNoDbId           IN inv_inv.inv_no_db_id%TYPE,
      an_InvNoId             IN inv_inv.inv_no_id%TYPE

   ) RETURN NUMBER;


/* This function will return 1 if task is applicable for the inventory */
FUNCTION IsTaskApplicable (
      an_InvNoDbId           IN inv_inv.inv_no_db_id%TYPE,
      an_InvNoId             IN inv_inv.inv_no_id%TYPE,
      an_TaskDbId            IN task_task.task_db_id%TYPE,
      an_TaskId              IN task_task.task_id%TYPE
   ) RETURN NUMBER;


/* This function will return 1 if applicability rule is valid */
FUNCTION VerifyApplicabilityRule (
      as_TaskApplSql IN task_task.task_appl_sql_ldesc%TYPE
   ) RETURN NUMBER;


/* This function will return number of parents for a beseline task*/
FUNCTION CountBaselineParents (
      an_TaskDbId            IN task_task.task_db_id%TYPE,
      an_TaskId              IN task_task.task_id%TYPE
   ) RETURN NUMBER;


/* creates a Dept link between two stasks */
PROCEDURE CreateTaskDependencyLink (
      an_PreviousSchedDbId     IN sched_stask.sched_db_id%TYPE,
      an_PreviousSchedId       IN sched_stask.sched_id%TYPE,
      an_SchedDbId            IN sched_stask.sched_db_id%TYPE,
      an_SchedId              IN sched_stask.sched_id%TYPE,
      on_Return               OUT typn_RetCode
   );

/* creates part requirement */
PROCEDURE AddRmvdPartRequirement (
       an_InvDbId         IN inv_inv.inv_no_db_id%TYPE,
       an_InvId         IN inv_inv.inv_no_id%TYPE,
       an_SchedDbId         IN sched_stask.sched_db_id%TYPE,
       an_SchedId           IN sched_stask.sched_id%TYPE,
       an_SchedPartId       IN sched_part.sched_part_id%TYPE,
       an_RemovedReasonDbid  IN task_part_list.remove_reason_db_id%TYPE,
       as_RemovedReasonCd    IN task_part_list.remove_reason_cd%TYPE,
       an_SchedRmvdPartId    OUT sched_rmvd_part.sched_rmvd_part_id%TYPE,
       on_Return            OUT typn_RetCode

   );

   /* find removed inventory */
   PROCEDURE FindRemovedInventory (
      an_StartInvNoDbId    IN inv_inv.inv_no_db_id%TYPE,
      an_StartInvNoId      IN inv_inv.inv_no_id%TYPE,
      an_FindAssmblDbId    IN eqp_assmbl_pos.assmbl_db_id%TYPE,
      as_FindAssmblCd      IN eqp_assmbl_pos.assmbl_cd%TYPE,
      an_FindAssmblBomId   IN eqp_assmbl_pos.assmbl_bom_id%TYPE,
      an_FindAssmblPosId   IN eqp_assmbl_pos.assmbl_pos_id%TYPE,
      an_FindBomPartDbId   IN eqp_bom_part.bom_part_db_id%TYPE,
      an_FindBomPartId     IN eqp_bom_part.bom_part_id%TYPE,
      on_InvNoDbId         OUT inv_inv.inv_no_db_id%TYPE,
      on_InvNoId           OUT inv_inv.inv_no_id%TYPE,
      on_Return            OUT typn_RetCode
   );

  /* find start inventory */
   PROCEDURE FindStartInventory (
      an_InvNoDbId    IN inv_inv.inv_no_db_id%TYPE,
      an_InvNoId      IN inv_inv.inv_no_id%TYPE,
      an_FindAssmblDbId    IN eqp_assmbl_pos.assmbl_db_id%TYPE,
      as_FindAssmblCd      IN eqp_assmbl_pos.assmbl_cd%TYPE,
      an_FindAssmblBomId   IN eqp_assmbl_pos.assmbl_bom_id%TYPE,
      an_FindAssmblPosId   IN eqp_assmbl_pos.assmbl_pos_id%TYPE,
      on_InvNoDbId         OUT inv_inv.inv_no_db_id%TYPE,
      on_InvNoId           OUT inv_inv.inv_no_id%TYPE,
      on_Return            OUT typn_RetCode
   );

/* find lates stask instance */
PROCEDURE FindLatestTaskInstance (
      an_TaskDbId            IN task_task.task_db_id%TYPE,
      an_TaskId              IN task_task.task_id%TYPE,
      an_InvNoDbId          IN inv_inv.inv_no_db_id%TYPE,
      an_InvNoId             IN inv_inv.inv_no_id%TYPE,
      an_TaskInstanceDbId    OUT sched_stask.sched_db_id%TYPE,
      an_TaskInstanceId      OUT sched_stask.sched_db_id%TYPE,
      on_Return          OUT typn_RetCode);

/* setup remove install part requirements from values in sched_part table */
PROCEDURE GenerateRemoveInstallPartReq (
       an_InvDbId         IN inv_inv.inv_no_db_id%TYPE,
       an_InvId         IN inv_inv.inv_no_id%TYPE,
       an_SchedDbId         IN sched_stask.sched_db_id%TYPE,
       an_SchedId           IN sched_stask.sched_id%TYPE,
       an_SchedPartId       IN sched_part.sched_part_id%TYPE,
       an_RemovedReasonDbid  IN task_part_list.remove_reason_db_id%TYPE,
       as_RemovedReasonCd    IN task_part_list.remove_reason_cd%TYPE,
       ab_Remove_bool        IN task_part_list.remove_bool%TYPE,
       ab_Install_bool        IN task_part_list.install_bool%TYPE,
       on_Return            OUT typn_RetCode
   );

/* This procedure will generate all task dependencies that fall within
   the next icn_ScheduleWindow number of days. */
PROCEDURE GenForecastedTasks (
      an_SchedDbId IN sched_stask.sched_db_id%TYPE,
      an_SchedId   IN sched_stask.sched_id%TYPE,
      os_ExitCd    OUT VARCHAR2,
      on_Return    OUT typn_RetCode );

/* This procedure will update the reference between a task and its
   root, non-check, task. */
PROCEDURE UpdateHSched (
      an_SchedDbId IN sched_stask.sched_db_id%TYPE,
      an_SchedId   IN sched_stask.sched_id%TYPE,
      on_Return    OUT typn_RetCode );

/*******************************************************************************
*
* Procedure:    UpdateReplSchedPart
* Arguments:    an_SchedDbId   (long) - the task that should be updated
*               an_SchedId     (long) - ""
* Return:       on_Return      (long) - succss/failure of procedure
*
* Description:  Updated the replacement part requirement for the task and its
*               children. This will also work if the task is a child of a REPL.
*
********************************************************************************
*
* Copyright 1997-2011 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*******************************************************************************/
PROCEDURE UpdateReplSchedPart(
      an_SchedDbId   IN sched_stask.sched_db_id%TYPE,
      an_SchedId     IN sched_stask.sched_id%TYPE,
      on_Return     OUT typn_RetCode
   );

/* This procedure will update the dwt_task_labour_summary table. */
PROCEDURE UpdateTaskLabourSummary (
      an_Months     IN NUMBER,
      on_Return    OUT typn_RetCode );

PROCEDURE UpdatePartsAndToolsReadyBool(
           an_TaskDbId           IN sched_stask.task_db_id%TYPE,
           an_TaskId             IN sched_stask.task_id%TYPE,
           on_Return             OUT typn_RetCode);

/* To add Assembly Measurement to a Given Task*/
PROCEDURE AddAssemblyMeasurements(
           an_TaskDbId           IN sched_stask.task_db_id%TYPE,
           an_TaskId             IN sched_stask.task_id%TYPE,
           an_InventoryDbId      IN inv_inv.inv_no_db_id%TYPE,
           an_InventoryId        IN inv_inv.inv_no_id%TYPE,
           an_DataTypeDbId       IN mim_data_type.data_type_db_id%TYPE,
           an_DataTypeId         IN mim_data_type.data_type_id%TYPE,
           an_RecParmQty         IN inv_parm_data.rec_parm_qt% TYPE,
           on_Return             OUT typn_RetCode);

/* This procedure will update req_res_ct in the sched_labour table
 * connected to the given (adhoc) task.
 * Arguments:    an_TaskDbId   (long) - the db of the task that should be updated
 *               an_TaskId     (long) - the task that should be updated
 * Return:       on_Return      (long) - succss/failure of procedure
 */
PROCEDURE UpdateReqResCt (
           an_TaskDbId           IN sched_stask.sched_db_id%TYPE,
           an_TaskId             IN sched_stask.sched_id%TYPE,
           on_Return             OUT typn_RetCode);

/* This procedure will update call recalculation of req_res_ct in the sched_labour table
 * for each descendant child for given parent task.
 * Arguments:    an_TaskDbId   (long) - the db of the parent task
 *               an_TaskId     (long) - the id of the parent task
 * Return:       on_Return      (long) - succss/failure of procedure
 */
PROCEDURE UpdateChildReqResCt (
           an_TaskDbId           IN sched_stask.sched_db_id%TYPE,
           an_TaskId             IN sched_stask.sched_id%TYPE,
           on_Return             OUT typn_RetCode);

END SCHED_STASK_PKG;
/