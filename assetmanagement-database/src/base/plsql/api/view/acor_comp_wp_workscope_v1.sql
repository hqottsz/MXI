--liquibase formatted sql


--changeSet acor_comp_wp_workscope_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW ACOR_COMP_WP_WORKSCOPE_V1 AS
SELECT
    comp_wp.sched_id               AS wo_sched_id,
    jic_task.alt_id                AS sched_id,
    req_task_task.alt_id           AS req_task_id,
    jic_task_task.alt_id           AS jic_task_id,
    sched_wo_line.tally_line_ord  AS workscope_order,
    evt_event.event_sdesc         AS item_code,
    jic_task.task_class_cd        AS task_class_code,
    jic_task.task_priority_cd     AS priority_code,
    req_task_task.task_cd         AS requirement_task_code,
    req_task.barcode_sdesc        AS requirement_barcode,
    evt_event.event_status_cd     AS event_status_code,
    evt_event.sched_start_dt      AS schedule_start_date,
    evt_event.sched_end_dt        AS schedule_end_date,
    evt_event.actual_start_dt     AS actual_start_date,
    evt_event.event_dt            AS actual_end_date,
    jic_task.barcode_sdesc        AS barcode,
    sched_wo_line.unassign_bool   AS unassigned_flag,
    latest_stage.stage_note as user_stage_note,
    unassigned_stage.unassigned_date AS requirement_unassigned_date,
    eqp_assmbl_bom.assmbl_bom_cd  AS config_slot_code,
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
   -- ata chapter
   INNER JOIN inv_inv ON
      jic_task.main_inv_no_db_id = inv_inv.inv_no_db_id AND
      jic_task.main_inv_no_id    = inv_inv.inv_no_id
   INNER JOIN eqp_assmbl_bom ON
      inv_inv.assmbl_db_id  = eqp_assmbl_bom.assmbl_db_id AND
      inv_inv.assmbl_cd     = eqp_assmbl_bom.assmbl_cd    AND
      inv_inv.assmbl_bom_id = eqp_assmbl_bom.assmbl_bom_id
   -- latest event stage user note
   INNER JOIN (
               SELECT
                  evt_stage.event_db_id,
                  evt_stage.event_id,
                  stage_note
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
      jic_task.sched_db_id = latest_stage.event_db_id AND
      jic_task.sched_id    = latest_stage.event_id
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
   -- latest event unassigned date
   LEFT JOIN (
               SELECT
                  evt_stage.event_db_id,
                  evt_stage.event_id,
                  MAX(stage_dt)  unassigned_date
               FROM
                  evt_stage
               WHERE
                  event_status_db_id = 0  AND
                  event_status_cd    = 'UNASSIGN'
               GROUP BY
                  evt_stage.event_db_id,
                  evt_stage.event_id
             ) unassigned_stage ON
      DECODE(nh_event.event_db_id,NULL,jic_task.sched_db_id,nh_event.event_db_id) = unassigned_stage.event_db_id AND
      DECODE(nh_event.event_id,NULL,jic_task.sched_id,nh_event.event_id)          = unassigned_stage.event_id
;