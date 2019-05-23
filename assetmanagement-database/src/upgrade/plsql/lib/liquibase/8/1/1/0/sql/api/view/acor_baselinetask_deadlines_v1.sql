--liquibase formatted sql


--changeSet acor_baselinetask_deadlines_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_baselinetask_deadlines_v1
AS 
SELECT
   inv_inv.alt_id                   AS inventory_id,
   sched_stask.alt_id               AS sched_id,
   task_task.alt_id                 AS task_id,
   mim_data_type.alt_id             AS data_type_id,
   task_task.task_cd                AS task_code,
   sched_stask.barcode_sdesc        AS barcode,
   mim_data_type.data_type_cd       AS data_type_code,
   evt_sched_dead.sched_dead_dt     AS schedule_dead_date,
   evt_sched_dead.usage_rem_qt      AS remaining_usage_quantity,
   getExtendedDeadlineDt(
         evt_sched_dead.deviation_qt,
         evt_sched_dead.sched_dead_dt,
         mim_data_type.domain_type_cd,
         mim_data_type.data_type_cd,
         ref_eng_unit.ref_mult_qt
   )                                AS extended_due_date,
   evt_sched_dead.deviation_qt      AS deviation_quantity,
   evt_sched_dead.sched_driver_bool AS schedule_driver_flag
FROM
   task_task
   INNER JOIN ref_task_class ON
      ref_task_class.task_class_db_id = task_task.task_class_db_id AND
      ref_task_class.task_class_cd    = task_task.task_class_cd
   INNER JOIN sched_stask ON
      sched_stask.task_db_id = task_task.task_db_id AND
      sched_stask.task_id    = task_task.task_id
   INNER JOIN evt_event ON
      evt_event.event_db_id = sched_stask.sched_db_id AND
      evt_event.event_id    = sched_stask.sched_id
   INNER JOIN inv_inv ON
      inv_inv.inv_no_db_id = sched_stask.main_inv_no_db_id AND
      inv_inv.inv_no_id    = sched_stask.main_inv_no_id
   LEFT OUTER JOIN evt_sched_dead ON
      evt_sched_dead.event_db_id = evt_event.event_db_id AND
      evt_sched_dead.event_id    = evt_event.event_id
   LEFT OUTER JOIN mim_data_type ON
      mim_data_type.data_type_db_id = evt_sched_dead.data_type_db_id AND
      mim_data_type.data_type_id    = evt_sched_dead.data_type_id
   LEFT OUTER JOIN ref_eng_unit ON
      ref_eng_unit.eng_unit_db_id = mim_data_type.eng_unit_db_id AND
      ref_eng_unit.eng_unit_cd    = mim_data_type.eng_unit_cd
WHERE
   -- Make sure the task is based on a requirement task definition
   ref_task_class.class_mode_cd IN ('BLOCK','REQ')
   AND
   evt_event.hist_bool = 0
   AND
   evt_event.event_status_cd != 'FORECAST'
;