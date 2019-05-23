--liquibase formatted sql


--changeSet acor_acft_wp_task_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_acft_wp_task_v1
AS
SELECT
    acft_wp.sched_id                  AS wo_sched_id,
    sched_stask.alt_id                AS sched_id,
    evt_event.event_sdesc             AS item_code,
    sched_stask.task_class_cd         AS task_class_code,
    sched_stask.task_priority_cd      AS priority_code,
    evt_event.event_status_cd         AS event_status_code,
    wp_event.sched_start_dt           AS wp_schedule_start_date,
    wp_event.sched_end_dt             AS wp_schedule_end_date,
    wp_event.actual_start_dt          AS wp_actual_start_date,
    wp_event.event_dt                 AS wp_actual_end_date,
    wp_stask.barcode_sdesc            AS wp_barcode,
    sched_stask.barcode_sdesc         AS barcode,
    --
    ref_task_class.class_mode_cd      AS class_mode
FROM
   acor_acft_wp_v1 acft_wp
   INNER JOIN sched_stask wp_stask ON
      acft_wp.sched_id = wp_stask.alt_id
   INNER JOIN ref_task_class ON 
      wp_stask.task_class_db_id = ref_task_class.task_class_db_id AND
      wp_stask.task_class_cd    = ref_task_class.task_class_cd
   INNER JOIN evt_event wp_event ON 
      wp_event.h_event_db_id = wp_stask.sched_db_id AND
      wp_event.h_event_id    = wp_stask.sched_id
   INNER JOIN sched_stask ON 
      wp_event.event_db_id = sched_stask.sched_db_id AND
      wp_event.event_id    = sched_stask.sched_id
   INNER JOIN evt_event ON 
      sched_stask.sched_db_id = evt_event.event_db_id AND
      sched_stask.sched_id    = evt_event.event_id
WHERE
   NOT (sched_stask.task_class_db_id = 0 AND
        sched_stask.task_class_cd = 'CHECK'
       );      