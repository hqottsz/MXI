--liquibase formatted sql


--changeSet MV_MRS_DRIVING:1 stripComments:false
/********************************************************************************
*
* Materialized View: MV_MRS_DRIVING
*
* Description:    This materialized view is used as part of the materials request
*                 status job. It produces the initial list of tasks to work on.
*
*
*********************************************************************************/
CREATE MATERIALIZED VIEW MV_MRS_DRIVING
PCTFREE 0
PCTUSED 99
BUILD DEFERRED
REFRESH ON DEMAND
WITH PRIMARY KEY
AS
SELECT
   sched_stask.sched_db_id      AS sched_db_id,
   sched_stask.sched_id         AS sched_id,
   0                            AS request_status_db_id,
   req_task_class.task_class_cd AS req_part_sdesc
FROM
   sched_stask
   INNER JOIN evt_event ON
   evt_event.event_db_id = sched_stask.sched_db_id AND
   evt_event.event_id = sched_stask.sched_id
   INNER JOIN ref_task_class ON
   ref_task_class.task_class_db_id = sched_stask.task_class_db_id AND
   ref_task_class.task_class_cd = sched_stask.task_class_cd
   LEFT OUTER JOIN sched_stask req_stask ON
   evt_event.nh_event_db_id = req_stask.sched_db_id AND
   evt_event.nh_event_id = req_stask.sched_id
   LEFT OUTER JOIN ref_task_class req_task_class ON
   req_task_class.task_class_db_id = req_stask.task_class_db_id AND
   req_task_class.task_class_cd = req_stask.task_class_cd
WHERE
   ref_task_class.class_mode_cd NOT IN ('BLOCK') AND
   evt_event.hist_bool = 0 AND
   ((req_task_class.task_class_cd IS NULL) OR (req_task_class.task_class_cd NOT IN ('BLOCK')));