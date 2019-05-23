--liquibase formatted sql


--changeSet acor_acft_ts_block_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_acft_ts_block_v1
AS 
SELECT
   sched_stask.alt_id                AS sched_id,
   acft_inv.alt_id                   AS aircraft_id,
   block_task.task_id                AS block_id,
   block_task.task_id,
   block_task.task_code,
   block_task.config_slot_code,
   inv_ac_reg.ac_reg_cd              AS registration_code,
   block_task.task_class_code,
   block_task.task_subclass_code,
   block_task.originator_code,
   sched_stask.barcode_sdesc         AS barcode,
   block_task.revision_order,
   sched_stask.wo_ref_sdesc          AS work_order_reference_name,
   --
   evt_event.event_dt                AS event_date,
   evt_event.event_type_cd           AS event_type_code,
   evt_event.event_status_cd         AS event_status_code,
   evt_event.hist_bool               AS historical_flag
FROM
   sched_stask
   INNER JOIN task_task ON
      sched_stask.task_db_id = task_task.task_db_id AND
      sched_stask.task_id    = task_task.task_id
   INNER JOIN acor_block_defn_v1 block_task ON
      task_task.alt_id = block_task.task_id
   INNER JOIN evt_event ON
      sched_stask.sched_db_id = evt_event.event_db_id AND
      sched_stask.sched_id    = evt_event.event_id
   -- aircraft
   INNER JOIN inv_inv ON
      sched_stask.main_inv_no_db_id = inv_inv.inv_no_db_id AND
      sched_stask.main_inv_no_id    = inv_inv.inv_no_id
   INNER JOIN inv_ac_reg ON
      inv_inv.h_inv_no_db_id = inv_ac_reg.inv_no_db_id AND
      inv_inv.h_inv_no_id    = inv_ac_reg.inv_no_id
   INNER JOIN inv_inv acft_inv ON
      inv_ac_reg.inv_no_db_id = acft_inv.inv_no_db_id AND
      inv_ac_reg.inv_no_id    = acft_inv.inv_no_id
WHERE
   inv_inv.inv_class_db_id = 0 AND
   inv_inv.inv_class_cd IN ('ACFT','SYS','ASSY')
;