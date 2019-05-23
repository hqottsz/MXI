--liquibase formatted sql


--changeSet MX-18483:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      getTaskRevisionKey
* Arguments:     aInvNoDbId, aInvNoId - pk for the inventory item
* Description:   This function will return the latest revision of the task 
*		 for the specific Inventory Item.
*
* Orig.Coder:    ycho
* Recent Coder:  ycho
* Recent Date:   2008-08-25
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION getTaskRevisionKey
(
    aInvNoDbId number,
    aInvNoId number,
    aTaskDefnDbId number,
    aTaskDefnId number
)   RETURN STRING
IS
    ls_RevTaskKey string(32); /* the latest revision of the task definition*/

BEGIN
    SELECT DISTINCT
	FIRST_VALUE( task_task.task_db_id || ':' || task_task.task_id || ':' || task_task.revision_ord )
       		OVER ( ORDER BY ref_event_status.status_ord ASC, task_task.revision_ord DESC, task_task.creation_dt DESC ) AS task_task_key
       	INTO ls_RevTaskKey
    FROM 
	inv_inv,
     	sched_stask,
     	task_task,
     	evt_event,
     	ref_event_type,
	ref_event_status
    WHERE 
	inv_inv.inv_no_db_id = aInvNoDbId AND
      	inv_inv.inv_no_id    = aInvNoId
      	AND
        inv_inv.rstat_cd	= 0
     	AND
        sched_stask.main_inv_no_db_id = inv_inv.inv_no_db_id AND
        sched_stask.main_inv_no_id = inv_inv.inv_no_id
     	AND
        task_task.task_defn_db_id = aTaskDefnDbId AND
        task_task.task_defn_id    = aTaskDefnId
        AND
        task_task.task_db_id = sched_stask.task_db_id AND
        task_task.task_id    = sched_stask.task_id
     	AND
     	evt_event.event_db_id = sched_stask.sched_db_id AND
        evt_event.event_id    = sched_stask.sched_id 
        AND
     	ref_event_type.event_type_db_id = evt_event.event_type_db_id AND
     	ref_event_type.event_type_cd    = evt_event.event_type_cd AND
     	ref_event_type.event_type_db_id = 0 AND
     	ref_event_type.event_type_cd    = 'TS'
        AND
        ref_event_status.event_status_db_id = evt_event.event_status_db_id AND
        ref_event_status.event_status_cd    = evt_event.event_status_cd
        AND
        (ref_event_status.event_status_db_id, ref_event_status.event_status_cd) NOT IN ((0, 'ERROR'));
		
	RETURN ls_RevTaskKey;

END getTaskRevisionKey;
/

--changeSet MX-18483:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      getTaskSchedStatus
* Arguments:     aInvNoDbId, aInvNoId - pk for the inventory item
*                aTaskDbId, aTaskId - pk for the task definition
* Description:   This function will return the status of the latest initialized
*                task for the specific Inventory Item.
*
* Orig.Coder:    JWurster
* Recent Coder:  dbaxter
* Recent Date:   2008-05-12
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
	ls_SchedStatusKey string(26); /* the latest scheduled task of the task definition*/

BEGIN
    SELECT DISTINCT
	FIRST_VALUE(evt_event.event_db_id || ':' || evt_event.event_id || ':' || evt_event.event_status_cd)
           OVER ( ORDER BY ref_event_status.status_ord ASC, task_task.revision_ord DESC, all_task_task.creation_dt DESC ) AS event_status_key
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
        (ref_event_status.event_status_db_id, ref_event_status.event_status_cd) NOT IN ((0, 'ERROR'));
        
    RETURN ls_SchedStatusKey;

END getTaskSchedStatus;
/            