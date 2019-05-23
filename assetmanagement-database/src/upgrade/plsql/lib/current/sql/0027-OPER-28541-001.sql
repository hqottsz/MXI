--liquibase formatted sql

--changeSet OPER-28541-001:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE axon_event_publish_pkg AS
/********************************************************************************
*
* Package:      axon_event_publish_pkg
*
* Description:  This package is used to publish axon events.
*
*********************************************************************************/
   icn_Success CONSTANT NUMBER := 1;
   icn_NoProc  CONSTANT NUMBER := 0;

   PROCEDURE publish_task_created_event (
                an_SchedDbId              IN sched_stask.sched_db_id%TYPE,
                an_SchedId                IN sched_stask.sched_id%TYPE,
                an_OrigTaskTaskDbId       IN task_task.task_db_id%TYPE,
                an_OrigTaskTaskId         IN task_task.task_id%TYPE,
                an_MaxRangeQt             IN task_task.forecast_range_qt%TYPE,
                an_InvNoDbId              IN inv_inv.inv_no_db_id%TYPE,
                an_InvNoId                IN inv_inv.inv_no_id%TYPE,
                an_PreviousTaskDbId       IN evt_event.event_db_id%TYPE,
                an_PreviousTaskId         IN evt_event.event_id%TYPE,
                ad_PreviousCompletionDt   IN evt_event.sched_end_gdt%TYPE,
                an_ReasonDbId             IN evt_stage.stage_reason_db_id%TYPE,
                an_ReasonCd               IN evt_stage.stage_reason_cd%TYPE,
                as_UserNote               IN evt_stage.stage_note%TYPE,
                an_HrDbId                 IN org_hr.hr_db_id%TYPE,
                an_HrId                   IN org_hr.hr_id%TYPE,
                ab_CalledExternally       IN BOOLEAN,
                ab_Historic               IN BOOLEAN,
                ab_CreateNATask           IN BOOLEAN,
                on_Return                 OUT NUMBER
   );

   PROCEDURE publish_task_deadl_resched_evt (
      an_TaskDbId         IN evt_event.event_db_id%TYPE,
      an_TaskId           IN evt_event.event_id%TYPE,
      an_DeadlineTypeDbId IN evt_sched_dead.data_type_db_id%TYPE,
      an_DeadlineTypeId   IN evt_sched_dead.data_type_id%TYPE,
      ad_DeadlineDate     IN evt_sched_dead.sched_dead_dt%TYPE,
      an_DeadlineQt        IN evt_sched_dead.sched_dead_qt%TYPE,
      ad_EstimatedDate    IN evt_sched_dead.sched_dead_dt%TYPE,
      on_Return OUT NUMBER
   );

END axon_event_publish_pkg;
/
