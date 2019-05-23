--liquibase formatted sql

-- This view holds the number of active task instances for a task definition.
--changeSet VW_ACTIVE_TASK_INSTANCES:1 stripComments:false
CREATE OR REPLACE VIEW VW_ACTIVE_TASK_INSTANCES AS
   SELECT
      COUNT(*) AS active_task_count,
      sched_stask.main_inv_no_db_id AS inv_no_db_id,
      sched_stask.main_inv_no_id AS inv_no_id,
      task_task.task_defn_db_id,
      task_task.task_defn_id
   FROM
      task_task
      INNER JOIN sched_stask ON
         sched_stask.task_db_id = task_task.task_db_id AND
         sched_stask.task_id    = task_task.task_id
      INNER JOIN evt_event ON
      	 evt_event.event_db_id = sched_stask.sched_db_id AND
         evt_event.event_id    = sched_stask.sched_id
   WHERE
      evt_event.event_status_db_id = 0 AND
      evt_event.event_status_cd    = 'ACTV'
   GROUP BY
      sched_stask.main_inv_no_db_id,
      sched_stask.main_inv_no_id,
      task_task.task_defn_db_id,
      task_task.task_defn_id
;