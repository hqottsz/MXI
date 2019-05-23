--liquibase formatted sql


--changeSet acor_req_part_sched_rules_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_req_part_sched_rules_v1
AS
SELECT
   task_task.alt_id              AS task_id,
   part.alt_id                   AS part_id,
   part.part_no_oem              AS part_number,
   mim_data_type.data_type_cd    AS data_type_code,
   task_interval.interval_qt     AS interval_quantity,
   task_interval.notify_qt       AS notify_quantity,
   task_interval.deviation_qt    AS deviation_quantity,
   task_interval.prefixed_qt     AS prefixed_quantity,
   task_interval.postfixed_qt    AS postfixed_quantity,
   task_interval.initial_qt      AS initial_quantity
FROM
   task_task
   INNER JOIN ref_task_class ON
      task_task.task_class_db_id = ref_task_class.task_class_db_id AND
      task_task.task_class_cd    = ref_task_class.task_class_cd
   -- part number scheduling rules
   INNER JOIN task_interval ON
      task_task.task_db_id = task_interval.task_db_id AND
      task_task.task_id    = task_interval.task_id
   INNER JOIN eqp_part_no part ON
      task_interval.part_no_db_id = part.part_no_db_id AND
      task_interval.part_no_id    = part.part_no_id
   INNER JOIN mim_data_type ON
      task_interval.data_type_db_id = mim_data_type.data_type_db_id AND
      task_interval.data_type_id    = mim_data_type.data_type_id
WHERE
   class_mode_cd = 'REQ'
   AND
   task_task.assmbl_cd IS NULL
;