--liquibase formatted sql


--changeSet acor_req_sched_rules_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_req_sched_rules_v1
AS 
SELECT
   task_task.alt_id                   AS task_id,
   mim_data_type.alt_id               AS data_type_id,
   mim_data_type.data_type_cd         AS data_type_code,
   task_sched_rule.def_initial_qt     AS initial_quantity,
   task_sched_rule.def_interval_qt    AS interval_quantity,
   task_sched_rule.def_notify_qt      AS notify_quantity,
   task_sched_rule.def_deviation_qt   AS deviation_quantity,
   task_sched_rule.def_prefixed_qt    AS prefixed_quantity,
   task_sched_rule.def_postfixed_qt   AS postfixed_quantity
FROM
   task_task
   INNER JOIN ref_task_class ON
      task_task.task_class_db_id = ref_task_class.task_class_db_id AND
      task_task.task_class_cd    = ref_task_class.task_class_cd
   -- scheduling rules
   INNER JOIN task_sched_rule ON
      task_task.task_db_id = task_sched_rule.task_db_id AND
      task_task.task_id    = task_sched_rule.task_id
   INNER JOIN mim_data_type ON
      task_sched_rule.data_type_db_id = mim_data_type.data_type_db_id AND
      task_sched_rule.data_type_id    = mim_data_type.data_type_id
WHERE
   class_mode_cd = 'REQ'
;