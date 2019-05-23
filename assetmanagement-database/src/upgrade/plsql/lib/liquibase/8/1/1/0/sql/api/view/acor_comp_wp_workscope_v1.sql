--liquibase formatted sql


--changeSet acor_comp_wp_workscope_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_comp_wp_workscope_v1
AS 
SELECT
    comp_wp.sched_id               AS wo_sched_id,
    jic_task.alt_id                AS sched_id,
    req_task_task.alt_id           AS req_task_id,
    jic_task_task.alt_id           AS jic_task_id,    
    sched_wo_line.tally_line_ord   AS workscope_order,    
    evt_event.event_sdesc         AS item_code,
    jic_task.task_class_cd        AS task_class_code,
    jic_task.task_priority_cd     AS priority_code,
    req_task_task.task_cd             AS requirement_task_code,
    evt_event.event_status_cd     AS event_status_code,
    evt_event.sched_start_dt      AS schedule_start_date,
    evt_event.sched_end_dt        AS schedule_end_date,
    evt_event.actual_start_dt     AS actual_start_date,
    evt_event.event_dt            AS actual_end_date,
    jic_task.barcode_sdesc        AS barcode,
    --
    fnc_account.alt_id            AS account_id,
    fnc_account.account_cd        AS issue_to_account      
FROM
   acor_comp_wp_v1 comp_wp
   INNER JOIN sched_stask ON
      comp_wp.sched_id = sched_stask.alt_id
   INNER JOIN sched_wo_line ON
      sched_stask.sched_db_id = sched_wo_line.wo_sched_db_id AND
      sched_stask.sched_id    = sched_wo_line.wo_sched_id
      AND -- only assigned task
      sched_wo_line.unassign_bool = 0
   -- JICs and executable requirements
   INNER JOIN sched_stask jic_task ON
      sched_wo_line.sched_db_id = jic_task.sched_db_id AND
      sched_wo_line.sched_id    = jic_task.sched_id
   LEFT JOIN fnc_account ON
      sched_stask.issue_account_db_id = fnc_account.account_db_id AND
      sched_stask.issue_account_id    = fnc_account.account_id          
   -- jic task id
   LEFT JOIN task_task jic_task_task ON 
      jic_task.task_db_id = jic_task_task.task_db_id AND
      jic_task.task_id    = jic_task_task.task_id          
   -- requirements
   INNER JOIN evt_event ON
      jic_task.sched_db_id = evt_event.event_db_id AND
      jic_task.sched_id    = evt_event.event_id
   LEFT JOIN evt_event nh_event ON
      evt_event.nh_event_db_id = nh_event.event_db_id AND
      evt_event.nh_event_id    = nh_event.event_id
   LEFT JOIN sched_stask req_task ON
      nh_event.event_db_id = req_task.sched_db_id AND
      nh_event.event_id    = req_task.sched_id
   LEFT JOIN task_task req_task_task ON
      req_task.task_db_id = req_task_task.task_db_id AND
      req_task.task_id    = req_task_task.task_id
;