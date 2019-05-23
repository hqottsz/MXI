--liquibase formatted sql


--changeSet getTaskSchedStatus:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      getTaskSchedStatus
* Arguments:     aInvNoDbId, aInvNoId - pk for the inventory item
*                aTaskDbId, aTaskId - pk for the task definition
* Description:   This function will return the status of the latest initialized
*                task for the specific Inventory Item.
*
* Orig.Coder:    JWurster
* Recent Coder:  asmolko
* Recent Date:   2012-04-17
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION getTaskSchedStatus
(
    aInvNoDbId number,
    aInvNoId number,
    aTaskDbId number,
    aTaskId number
)   RETURN STRING
IS
	ls_SchedStatusKey string(38); /* the latest scheduled task of the task definition*/

BEGIN
    SELECT DISTINCT
	FIRST_VALUE(evt_event.event_db_id || ':' || evt_event.event_id || ':' || evt_event.event_status_cd)
           OVER ( ORDER BY evt_event.hist_bool ASC, evt_event.event_gdt DESC, evt_event.creation_dt DESC ) AS event_status_key
    INTO
        ls_SchedStatusKey
    FROM
	task_task,
        task_task all_task_task,
        sched_stask,
        evt_event,
	ref_event_status
    WHERE
	task_task.task_db_id = aTaskDbId AND
        task_task.task_id    = aTaskId
	AND
        task_task.task_defn_db_id  = all_task_task.task_defn_db_id   AND
        task_task.task_defn_id = all_task_task.task_defn_id
        AND
        all_task_task.task_db_id = sched_stask.task_db_id AND
        all_task_task.task_id = sched_stask.task_id
        AND
        sched_stask.sched_db_id = evt_event.event_db_id AND
        sched_stask.sched_id = evt_event.event_id
        AND
        sched_stask.main_inv_no_db_id = aInvNoDbId AND
        sched_stask.main_inv_no_id = aInvNoId
        AND
        sched_stask.rstat_cd = 0
        AND
        evt_event.event_db_id = sched_stask.sched_db_id AND
        evt_event.event_id = sched_stask.sched_id
        AND
	ref_event_status.event_status_db_id = evt_event.event_status_db_id AND
        ref_event_status.event_status_cd = evt_event.event_status_cd
        AND
        (ref_event_status.event_status_db_id, ref_event_status.event_status_cd) NOT IN ((0, 'ERROR'), (0, 'FORECAST'));

    RETURN ls_SchedStatusKey;

END getTaskSchedStatus;
/