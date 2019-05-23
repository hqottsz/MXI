--liquibase formatted sql

--changeSet OPER-9713:1 stripComments:false
--
-- Migration script to clean up corrupted data as a result of the issue 
-- reported in OPER-9713.
--
-- It changes the status of non-block, non-recurring tasks from FORECAST to ACTV.
--
UPDATE 
   evt_event
SET
   evt_event.event_status_db_id = 0,
   evt_event.event_status_cd    = 'ACTV'
WHERE
   (evt_event.event_db_id, evt_event.event_id) IN
   (
      SELECT
         evt_event.event_db_id, 
         evt_event.event_id
      FROM
         sched_stask
         INNER JOIN task_task ON
            task_task.task_db_id = sched_stask.task_db_id AND
            task_task.task_id    = sched_stask.task_id 
         INNER JOIN evt_event ON
            evt_event.event_db_id = sched_stask.sched_db_id AND
            evt_event.event_id    = sched_stask.sched_id
      WHERE
         -- select non-block, non-recurring tasks with a status of FORECAST
         -- (blocks are the exception, because non-recurring block chains will have forecast blocks)
         NOT (
            sched_stask.task_class_db_id = 10 AND
            sched_stask.task_class_cd    = 'BLOCK'
         )
         AND
         task_task.recurring_task_bool = 0
         AND
         evt_event.event_status_db_id = 0 AND
         evt_event.event_status_cd    = 'FORECAST'
   );
