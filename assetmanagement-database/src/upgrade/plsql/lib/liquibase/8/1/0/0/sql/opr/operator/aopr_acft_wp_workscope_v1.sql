--liquibase formatted sql


--changeSet aopr_acft_wp_workscope_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_acft_wp_workscope_v1
AS 
SELECT
    acft_wp.sched_id                  AS wo_sched_id,
    wscope_task.alt_id                   AS sched_id,
    sched_wo_line.workscope_order,
    evt_event.event_sdesc             AS item_code,
    wscope_task.task_class_cd         AS task_class_code,
    wscope_task.task_priority_cd      AS priority_code,
    evt_event.event_status_cd       AS event_status_code,
    evt_event.sched_start_dt        AS schedule_start_date,
    evt_event.sched_end_dt          AS schedule_end_date,
    evt_event.actual_start_dt       AS actual_start_date,
    evt_event.event_dt              AS actual_end_date,
    wscope_task.barcode_sdesc         AS barcode,
    sched_wo_line.unassign_bool       AS unassigned_flag,
    latest_stage.user_stage_note
FROM
   aopr_acft_wp_v1 acft_wp
   INNER JOIN sched_stask ON
      acft_wp.sched_id = sched_stask.alt_id
   INNER JOIN sched_wo_line ON
      sched_stask.sched_db_id = sched_wo_line.wo_sched_db_id AND
      sched_stask.sched_id    = sched_wo_line.wo_sched_id
   -- workscope tasks
   INNER JOIN sched_stask wscope_task ON
      sched_wo_line.sched_db_id = wscope_task.sched_db_id AND
      sched_wo_line.sched_id    = wscope_task.sched_id
   -- latest event stage user note
   INNER JOIN (
               SELECT
                  evt_stage.event_db_id,
                  evt_stage.event_id,
                  user_stage_note
               FROM
                  evt_stage
                  INNER JOIN (
                              SELECT
                                 event_db_id,
                                 event_id,
                                 MAX(stage_id) stage_id
                              FROM
                                 evt_stage
                              GROUP BY
                                 event_db_id,
                                 event_id
                             ) max_stage ON
                     evt_stage.event_db_id = max_stage.event_db_id AND
                     evt_stage.event_id    = max_stage.event_id    AND
                     evt_stage.stage_id    = max_stage.stage_id
               ) latest_stage ON
      wscope_task.sched_db_id = latest_stage.event_db_id AND
      wscope_task.sched_id    = latest_stage.event_id
   INNER JOIN evt_event ON
      wscope_task.sched_db_id = evt_event.event_db_id AND
      wscope_task.sched_id    = evt_event.event_id
;