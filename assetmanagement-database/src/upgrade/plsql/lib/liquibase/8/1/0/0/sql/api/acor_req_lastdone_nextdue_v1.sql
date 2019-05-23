--liquibase formatted sql


--changeSet acor_req_lastdone_nextdue_v1:1 stripComments:false
CREATE MATERIALIZED VIEW acor_req_lastdone_nextdue_v1
BUILD IMMEDIATE
REFRESH COMPLETE
AS 
WITH
rvw_req
AS
  (
      SELECT
         h_inv_inv.alt_id       AS highest_inventory_id,
         inv_inv.alt_id         AS inventory_id,
         sched_stask.alt_id     AS sched_id,
         task_defn.alt_id       AS task_defn_id,
         task_task.alt_id       AS task_id,
         mim_data_type.alt_id   AS data_type_id,
         evt_event.event_db_id,
         evt_event.event_id,
         task_task.task_cd,
         sched_stask.barcode_sdesc,
         evt_sched_dead.sched_dead_dt,
         evt_sched_dead.deviation_qt,
         evt_sched_dead.sched_dead_qt,
         mim_data_type.data_type_cd,
         mim_data_type.domain_type_cd,
         ref_eng_unit.ref_mult_qt,
         evt_sched_dead.usage_rem_qt,
         mim_data_type.eng_unit_cd,
         mim_data_type.entry_prec_qt,
         evt_event.sched_start_dt,
         evt_event.sched_end_dt,
         evt_event.event_dt,
         --
         evt_inv_usage.tsn_qt,
         --
         evt_event.hist_bool,
         evt_event.event_status_db_id,
         evt_event.event_status_cd
      FROM
         task_task
         INNER JOIN ref_task_class ON
            ref_task_class.task_class_db_id = task_task.task_class_db_id AND
            ref_task_class.task_class_cd    = task_task.task_class_cd
         INNER JOIN task_defn ON
            task_task.task_defn_db_id = task_defn.task_defn_db_id AND
            task_task.task_defn_id    = task_defn.task_defn_id
         INNER JOIN sched_stask ON
            sched_stask.task_db_id = task_task.task_db_id AND
            sched_stask.task_id    = task_task.task_id
         INNER JOIN evt_event ON
            evt_event.event_db_id = sched_stask.sched_db_id AND
            evt_event.event_id    = sched_stask.sched_id
         INNER JOIN inv_inv ON
            inv_inv.inv_no_db_id = sched_stask.main_inv_no_db_id AND
            inv_inv.inv_no_id    = sched_stask.main_inv_no_id
         -- highest inventory
         INNER JOIN inv_inv h_inv_inv ON
            inv_inv.h_inv_no_db_id = h_inv_inv.inv_no_db_id AND
            inv_inv.h_inv_no_id    = h_inv_inv.inv_no_id
         LEFT OUTER JOIN evt_sched_dead ON
            evt_sched_dead.event_db_id = evt_event.event_db_id AND
            evt_sched_dead.event_id    = evt_event.event_id AND
            evt_sched_dead.sched_driver_bool = 1
         LEFT OUTER JOIN mim_data_type ON
            mim_data_type.data_type_db_id = evt_sched_dead.data_type_db_id AND
            mim_data_type.data_type_id    = evt_sched_dead.data_type_id
         LEFT OUTER JOIN ref_eng_unit ON
            ref_eng_unit.eng_unit_db_id = mim_data_type.eng_unit_db_id AND
            ref_eng_unit.eng_unit_cd    = mim_data_type.eng_unit_cd
         --
         LEFT OUTER JOIN evt_inv_usage ON
            evt_inv_usage.event_db_id     = evt_sched_dead.event_db_id AND
            evt_inv_usage.event_id        = evt_sched_dead.event_id    AND
            evt_inv_usage.data_type_db_id = evt_sched_dead.data_type_db_id AND
            evt_inv_usage.data_type_id    = evt_sched_dead.data_type_id
      WHERE
         -- Make sure the task is based on a requirement task definition
         ref_task_class.class_mode_cd = 'REQ'
         AND
         evt_event.event_status_cd != 'FORECAST'
  ),
rvw_req_unq
AS
   (
     SELECT
         highest_inventory_id,
         inventory_id,
         sched_id,
         task_defn_id,
         task_id,
         data_type_id,
         data_type_cd,
         task_cd
     FROM
        rvw_req
     GROUP BY
         highest_inventory_id,
         inventory_id,
         sched_id,
         task_defn_id,
         task_id,
         data_type_id,
         data_type_cd,
         task_cd
   ),
rvw_last_done
AS
   (
     SELECT
         rvw_req.inventory_id,
         rvw_req.sched_id,
         rvw_req.task_defn_id,
         rvw_req.task_id,
         rvw_req.data_type_id,
         rvw_req.data_type_cd,
         rvw_req.barcode_sdesc AS barcode,
         rvw_req.event_dt AS last_completed_date,
         rvw_req.tsn_qt AS last_usage_quantity,
         rvw_req.entry_prec_qt AS last_precision_quantity,
         rvw_req.event_status_cd AS last_status_code
     FROM
        rvw_req
     WHERE
         rvw_req.hist_bool = 1
         AND
         rvw_req.event_status_db_id = 0 AND
         rvw_req.event_status_cd  IN ('COMPLETE','TERMINATE')
         -- Make sure that the complete task doesn't have a DEPT relationship to another historic task
         AND NOT EXISTS
         (
            SELECT
               1
            FROM
               ref_rel_type,
               evt_event_rel,
               evt_event rel_evt_event
            WHERE
               ref_rel_type.rel_type_db_id = 0 AND
               ref_rel_type.rel_type_cd    = 'DEPT'
               AND
               evt_event_rel.rel_type_db_id = ref_rel_type.rel_type_db_id AND
               evt_event_rel.rel_type_cd    = ref_rel_type.rel_type_cd
               AND
               rel_evt_event.event_db_id = evt_event_rel.rel_event_db_id AND
               rel_evt_event.event_id    = evt_event_rel.rel_event_id
               AND
               rel_evt_event.hist_bool = 1
               AND
               rel_evt_event.event_status_db_id = 0 AND
               rel_evt_event.event_status_cd    IN ('COMPLETE','TERMINATE')
               AND
               evt_event_rel.event_db_id = rvw_req.event_db_id AND
               evt_event_rel.event_id    = rvw_req.event_id
         )
   ),
rvw_next_due
AS
   (
      SELECT
         rvw_req.inventory_id,
         rvw_req.sched_id,
         rvw_req.task_defn_id,
         rvw_req.task_id,
         rvw_req.data_type_id,
         rvw_req.data_type_cd,
         rvw_req.barcode_sdesc AS next_barcode,
         rvw_req.sched_dead_dt AS next_due_date,
         getExtendedDeadlineDt(
               rvw_req.deviation_qt,
               rvw_req.sched_dead_dt,
               rvw_req.domain_type_cd,
               rvw_req.data_type_cd,
               rvw_req.ref_mult_qt
         ) AS next_extended_due_date,
         rvw_req.deviation_qt AS next_deviation_quantity,
         rvw_req.usage_rem_qt AS next_usage_rem_quantity,
         rvw_req.sched_dead_qt AS next_usage_quantity,
         rvw_req.event_status_cd AS next_status_code
      FROM
        rvw_req
      WHERE
         rvw_req.hist_bool = 0
   )
SELECT
   rvw_req_unq.highest_inventory_id,
   rvw_req_unq.inventory_id,
   rvw_req_unq.sched_id,
   rvw_req_unq.task_defn_id,
   rvw_req_unq.task_id,
   rvw_req_unq.data_type_id,
   rvw_req_unq.data_type_cd    AS data_type_code,
   rvw_req_unq.task_cd         AS task_code,
   rvw_last_done.barcode,
   rvw_last_done.last_completed_date,
   rvw_last_done.last_usage_quantity,
   rvw_last_done.last_precision_quantity,
   rvw_last_done.last_status_code,
   --
   rvw_next_due.next_barcode,
   rvw_next_due.next_due_date,
   rvw_next_due.next_extended_due_date,
   rvw_next_due.next_deviation_quantity,
   rvw_next_due.next_usage_rem_quantity,
   rvw_next_due.next_usage_quantity,
   rvw_next_due.next_status_code
FROM
   rvw_req_unq
   LEFT JOIN rvw_last_done ON
      rvw_req_unq.inventory_id = rvw_last_done.inventory_id
      AND
      rvw_req_unq.task_defn_id      = rvw_last_done.task_defn_id
      AND
      rvw_req_unq.data_type_id = rvw_last_done.data_type_id
   LEFT JOIN rvw_next_due ON
      rvw_req_unq.inventory_id = rvw_next_due.inventory_id
      AND
      rvw_req_unq.task_defn_id      = rvw_next_due.task_defn_id
      AND
      rvw_req_unq.data_type_id = rvw_next_due.data_type_id
;