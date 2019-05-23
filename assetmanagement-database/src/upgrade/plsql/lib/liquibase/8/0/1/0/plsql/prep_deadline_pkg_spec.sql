--liquibase formatted sql


--changeSet prep_deadline_pkg_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE PREP_DEADLINE_PKG

IS

/********************************************************************************
*
* Package:     PREP_DEADLINE_PKG
* Description: This package is used to prepare scheduling info for a task, calendar and usage deadlines. 
*
* Orig.Coder:   Michal Baje
* Recent Coder: N/A
* Recent Date:  August 24, 2004
*
*********************************************************************************
*
* Copyright 1998-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/

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

-- Sub procedure validation TRUE, FALSE
icn_True    CONSTANT typn_RetCode := 1;  -- True
icn_False   CONSTANT typn_RetCode := 0;  -- False

/*---------------------------------- Procedures -----------------------------*/



PROCEDURE PrepareUsageDeadline (
      an_SchedDbId        IN sched_stask.sched_db_id%TYPE,
      an_SchedId          IN sched_stask.sched_id%TYPE,
      an_OrigTaskTaskDbId IN task_task.task_db_id%TYPE,
      an_OrigTaskTaskId   IN task_task.task_id%TYPE,
      an_DataTypeDbId     IN task_sched_rule.data_type_db_id%TYPE,
      an_DataTypeId       IN task_sched_rule.data_type_id%TYPE,
      abSyncWithBaseline  IN BOOLEAN,
      ad_PreviousCompletionDt IN evt_event.sched_end_gdt%TYPE,      
      on_Return           OUT typn_RetCode
   );


PROCEDURE   FindUsageDeadlineVariables(
            ad_StartQt       IN OUT evt_sched_dead.start_qt%TYPE,
            an_Interval      IN OUT evt_sched_dead.interval_qt%TYPE,
            ad_NewDeadlineTSN IN OUT evt_sched_dead.sched_dead_qt%TYPE,
            on_Return        OUT typn_RetCode
   );

PROCEDURE FindCalendarDeadlineVariables(
            an_DataTypeDbId  IN  evt_sched_dead.data_type_db_id%TYPE,
            an_DataTypeId    IN  evt_sched_dead.data_type_id%TYPE,
            ad_StartDt       IN OUT evt_sched_dead.start_dt%TYPE,
            an_Interval      IN OUT evt_sched_dead.interval_qt%TYPE,
            ad_NewDeadlineDt IN OUT evt_sched_dead.sched_dead_dt%TYPE,
            on_Return        OUT typn_RetCode
   );

PROCEDURE GetCurrentInventoryUsage(
            an_SchedDbId    IN  sched_stask.sched_db_id%TYPE,
            an_SchedId      IN  sched_stask.sched_id%TYPE,
            an_DataTypeDbId IN  evt_sched_dead.data_type_db_id%TYPE,
            an_DataTypeId   IN  evt_sched_dead.data_type_id%TYPE,
            an_TsnQt OUT inv_curr_usage.tsn_qt%TYPE,
            on_Return      OUT typn_RetCode
   );


PROCEDURE UpdateDependentDeadlines(
      an_StartSchedDbId  IN sched_stask.sched_db_id%TYPE,
      an_StartSchedId    IN sched_stask.sched_id%TYPE,
      on_Return          OUT typn_RetCode
   );

PROCEDURE GetHistoricUsageAtDt(
            ad_TargetDate   IN  evt_sched_dead.start_dt%TYPE,
            an_DataTypeDbId IN  evt_sched_dead.data_type_db_id%TYPE,
            an_DataTypeId   IN  evt_sched_dead.data_type_id%TYPE,
            an_InvNoDbId    IN  inv_inv.inv_no_db_id%TYPE,
            an_InvNoId      IN  inv_inv.inv_no_id%TYPE,
            on_TsnQt        OUT evt_inv_usage.tsn_qt%TYPE,
            on_Return       OUT typn_RetCode
   );
   
PROCEDURE PrepareDeadlineForInv(
      an_InvNoDbId    IN  inv_inv.inv_no_db_id%TYPE,
      an_InvNoId      IN  inv_inv.inv_no_id%TYPE,
      as_SchedFrom    IN evt_sched_dead.sched_from_cd%TYPE,
      abSyncWithBaseline   IN BOOLEAN,
      on_Return         OUT NUMBER
   );

PROCEDURE PrepareSchedDeadlines (
      an_SchedDbId        IN sched_stask.sched_db_id%TYPE,
      an_SchedId          IN sched_stask.sched_id%TYPE,
      an_OrigTaskTaskDbId IN task_task.task_db_id%TYPE,
      an_OrigTaskTaskId   IN task_task.task_id%TYPE,
      abSyncWithBaseline  IN BOOLEAN,
      ad_PreviousCompletionDt IN evt_event.sched_end_gdt%TYPE,            
      on_Return           OUT typn_RetCode
   );   
PROCEDURE PrepareCalendarDeadline (
      an_SchedDbId        IN sched_stask.sched_db_id%TYPE,
      an_SchedId          IN sched_stask.sched_id%TYPE,
      an_OrigTaskTaskDbId IN task_task.task_db_id%TYPE,
      an_OrigTaskTaskId   IN task_task.task_id%TYPE,      
      an_DataTypeDbId     IN task_sched_rule.data_type_db_id%TYPE,
      an_DataTypeId       IN task_sched_rule.data_type_id%TYPE,
      abSyncWithBaseline  IN BOOLEAN,
      ad_PreviousCompletionDt IN evt_event.sched_end_gdt%TYPE,            
      on_Return           OUT typn_RetCode
   ); 


PROCEDURE UpdateDependentDeadlinesTree(
      an_StartSchedDbId  IN sched_stask.sched_db_id%TYPE,
      an_StartSchedId    IN sched_stask.sched_id%TYPE,
      on_Return          OUT typn_RetCode );


FUNCTION GetForecastedDrivingDueDate(
            an_STaskDbId            IN sched_stask.sched_db_id%TYPE,
            an_STaskId              IN sched_stask.sched_id%TYPE,
            an_RevisionTaskTaskDbId   IN task_task.task_db_id%TYPE,
            an_RevisionTaskTaskId     IN task_task.task_id%TYPE
                        
   ) RETURN VARCHAR2;


FUNCTION isTaskDefnSchedulingChanged(
            an_ActiveTaskTaskDbId    task_task.task_db_id   %TYPE,
            an_ActiveTaskTaskId      task_task.task_id      %TYPE,
            an_RevisionTaskTaskDbId  task_task.task_db_id   %TYPE,
            an_RevisionTaskTaskId    task_task.task_id      %TYPE,
            an_STaskDbId             sched_stask.sched_db_id%TYPE,
            an_STaskId               sched_stask.sched_id   %TYPE            
   ) RETURN NUMBER ;


FUNCTION GetTaskDeadlines(
            an_TaskTaskDbId   IN task_task.task_db_id   %TYPE,
            an_TaskTaskId     IN task_task.task_id      %TYPE,
            an_STaskDbId      IN sched_stask.sched_db_id%TYPE,
            an_STaskId        IN sched_stask.sched_id   %TYPE

   ) RETURN DeadlineTable PIPELINED;

END PREP_DEADLINE_PKG;
/