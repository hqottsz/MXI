--liquibase formatted sql


--changeSet acor_req_acft_sched_rule_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_req_acft_sched_rule_v1
AS 
SELECT
   task_task.alt_id                AS task_id,
   acft_inv.alt_id                 AS aircraft_id,
   inv_ac_reg.ac_reg_cd            AS registration_code,
   mim_data_type.data_type_cd      AS data_type_code,
   task_ac_rule.interval_qt        AS interval_quantity,
   task_ac_rule.notify_qt          AS notify_quantity,
   task_ac_rule.deviation_qt       AS deviation_quantity,
   task_ac_rule.prefixed_qt        AS prefixed_quantity,
   task_ac_rule.postfixed_qt       AS postfixed_quantity,
   task_ac_rule.initial_qt         AS initial_quantity
FROM
   task_task
   INNER JOIN ref_task_class ON
      task_task.task_class_db_id = ref_task_class.task_class_db_id AND
      task_task.task_class_cd    = ref_task_class.task_class_cd
   -- aircraft
   INNER JOIN task_ac_rule ON
      task_task.task_db_id = task_ac_rule.task_db_id AND
      task_task.task_id    = task_ac_rule.task_id
   INNER JOIN inv_ac_reg ON
      task_ac_rule.inv_no_db_id = inv_ac_reg.inv_no_db_id AND
      task_ac_rule.inv_no_id    = inv_ac_reg.inv_no_id
   INNER JOIN inv_inv acft_inv ON
      inv_ac_reg.inv_no_db_id = acft_inv.inv_no_db_id AND
      inv_ac_reg.inv_no_id    = acft_inv.inv_no_id
   INNER JOIN mim_data_type ON
      task_ac_rule.data_type_db_id = mim_data_type.data_type_db_id AND
      task_ac_rule.data_type_id    = mim_data_type.data_type_id
WHERE
   class_mode_cd = 'REQ';