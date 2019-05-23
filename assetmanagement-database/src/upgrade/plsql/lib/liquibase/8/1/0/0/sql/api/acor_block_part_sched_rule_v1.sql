--liquibase formatted sql


--changeSet acor_block_part_sched_rule_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_block_part_sched_rule_v1
AS 
SELECT
   task_task.alt_id               AS task_id,
   eqp_part_no.alt_id             AS part_id,
   eqp_part_no.part_no_oem        AS part_number,
   mim_data_type.data_type_cd     AS data_type_code,
   task_interval.interval_qt      AS interval_quantity,
   task_interval.notify_qt        AS notify_quantity,
   task_interval.deviation_qt     AS deviation_quantity,
   task_interval.prefixed_qt      AS prefixed_quantity,
   task_interval.postfixed_qt     AS postfixed_quantity,
   task_interval.initial_qt       AS initial_quantity
FROM
   task_task
   INNER JOIN ref_task_class ON
      task_task.task_class_db_id = ref_task_class.task_class_db_id AND
      task_task.task_class_cd    = ref_task_class.task_class_cd
   INNER JOIN task_interval ON
      task_task.task_db_id = task_interval.task_db_id AND
      task_task.task_id    = task_interval.task_id
   -- parts
   INNER JOIN eqp_part_no ON
      task_interval.part_no_db_id = eqp_part_no.part_no_db_id AND
      task_interval.part_no_id    = eqp_part_no.part_no_id
   INNER JOIN mim_data_type ON
      task_interval.Data_Type_Db_Id = mim_data_type.data_type_db_id AND
      task_interval.data_type_id    = mim_data_type.data_type_id
WHERE
   class_mode_cd = 'BLOCK'
;