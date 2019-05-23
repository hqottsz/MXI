--liquibase formatted sql


--changeSet acor_wp_task_work_type_v1:1 stripComments:false
CREATE OR REPLACE VIEW acor_wp_task_work_type_v1
AS
SELECT
      sched_work_type.work_type_cd      AS work_type_code,
      sched_stask.alt_id                AS sched_id,
      wp_stask.alt_id                   AS wo_sched_id,
      wp_stask.barcode_sdesc            AS wp_barcode
FROM
      sched_work_type
      INNER JOIN sched_stask ON
        sched_stask.sched_db_id = sched_work_type.sched_db_id AND
        sched_stask.sched_id    = sched_work_type.sched_id
      INNER JOIN evt_event stask_event ON
        stask_event.event_db_id = sched_stask.sched_db_id     AND
        stask_event.event_id    = sched_stask.sched_id
      INNER JOIN evt_event wp_event ON
        wp_event.event_db_id    = stask_event.h_event_db_id   AND
        wp_event.event_id       = stask_event.h_event_id
      INNER JOIN sched_stask wp_stask ON
         wp_stask.sched_db_id   = wp_event.event_db_id        AND
         wp_stask.sched_id      = wp_event.event_id
;