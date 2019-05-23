--liquibase formatted sql


--changeSet acor_wp_task_deadlines_v1:1 stripComments:false
CREATE OR REPLACE VIEW acor_wp_task_deadlines_v1 
AS
SELECT
   sched_stask.alt_id                 AS sched_id,
   evt_sched_dead.sched_dead_dt       AS schedule_deadline_date,
   evt_sched_dead.sched_driver_bool   AS schedule_driver_flag,
   sched_stask.barcode_sdesc          AS task_barcode,
   evt_event.event_status_cd          AS task_status_code,
   wp_stask.barcode_sdesc             AS wp_barcode,
   wp_stask.alt_id                    AS wo_sched_id
FROM
   --task
   sched_stask
   -- event
   INNER JOIN evt_event ON
      sched_stask.sched_db_id = evt_event.event_db_id AND
      sched_stask.sched_id    = evt_event.event_id
   -- schedule
   INNER JOIN evt_sched_dead ON
      evt_event.event_db_id = evt_sched_dead.event_db_id AND
      evt_event.event_id    = evt_sched_dead.event_id
   INNER JOIN evt_event wp_event ON
      wp_event.event_db_id  = evt_event.h_event_db_id    AND
      wp_event.event_id     = evt_event.h_event_id
   INNER JOIN sched_stask wp_stask ON
      wp_stask.sched_db_id  = wp_event.event_db_id       AND
      wp_stask.sched_id     = wp_event.event_id
WHERE
    evt_sched_dead.sched_driver_bool=1                   AND
    wp_stask.task_class_cd IN ('CHECK','RO')
;