--liquibase formatted sql


--changeSet OPER-22906:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace procedure SCHED_STASK_PKG_GENSCHEDTASK (
     an_EvtEventDbId       IN evt_event.event_db_id%TYPE,
             an_EvtEventId         IN evt_event.event_id%TYPE,
             an_InvNoDbId          IN inv_inv.inv_no_db_id%TYPE,
             an_InvNoId            IN inv_inv.inv_no_id%TYPE,
             an_TaskDbId           IN sched_stask.task_db_id%TYPE ,
             an_TaskId             IN sched_stask.task_id%TYPE,
             an_PreviousTaskDbId   IN evt_event.event_db_id%TYPE,
             an_PreviousTaskId     IN evt_event.event_id%TYPE,
             ad_CompletionDate     IN evt_stage.stage_reason_cd%TYPE,
             an_ReasonDbId         IN evt_stage.stage_reason_db_id%TYPE,
             an_ReasonCd           IN evt_stage.stage_reason_cd%TYPE,
             as_UserNote           IN evt_stage.stage_note%TYPE,
             an_HrDbId             IN org_hr.hr_db_id%TYPE,
             an_HrId               IN org_hr.hr_id%TYPE,
             ab_Historic           IN INTEGER,
             ab_CreateNATask       IN INTEGER,
             ab_CalledExternally   IN INTEGER,
             ad_PreviousCompletionDt   IN evt_event.sched_end_gdt%TYPE,
             on_SchedDbId          OUT evt_event.event_db_id%TYPE,
             on_SchedId            OUT evt_event.event_id%TYPE,
             on_Return             OUT INTEGER
        ) IS
     BEGIN


       SCHED_STASK_PKG.GENSCHEDTASK(
                an_EvtEventDbId,
                an_EvtEventId,
                an_InvNoDbId,
                an_InvNoId,
                an_TaskDbId,
                an_TaskId,
                an_PreviousTaskDbId,
                an_PreviousTaskId,
                to_date( ad_CompletionDate, 'dd-mon-yyyy hh24:mi:ss'),
                an_ReasonDbId,
                an_ReasonCd,
                as_UserNote,
                an_HrDbId,
                an_HrId,
                sys.diutil.int_to_bool(ab_CalledExternally), -- Indicates that the procedure is being called externally
                sys.diutil.int_to_bool(ab_historic),
                sys.diutil.int_to_bool(ab_CreateNATask),
                ad_PreviousCompletionDt,
                on_SchedDbId,
                on_SchedId,
                on_Return
   );
END SCHED_STASK_PKG_GENSCHEDTASK;
/