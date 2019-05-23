--liquibase formatted sql


--changeSet acor_jic_tools_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_jic_tools_v1
AS
SELECT
   task_task.alt_id               AS task_id,
   eqp_bom_part.bom_part_cd       AS part_group_code,
   eqp_bom_part.bom_part_name     AS part_group_name,
   task_tool_list.sched_hr        AS schedule_hour
FROM
   task_task
   INNER JOIN task_tool_list ON
      task_task.task_db_id = task_tool_list.task_db_id AND
      task_task.task_id    = task_tool_list.task_id
   INNER JOIN eqp_bom_part ON
      task_tool_list.bom_part_db_id = eqp_bom_part.bom_part_db_id AND
      task_tool_list.bom_part_id    = eqp_bom_part.bom_part_id
   INNER JOIN ref_task_class ON
      task_task.task_class_db_id = ref_task_class.task_class_db_id AND
      task_task.task_class_cd    = ref_task_class.task_class_cd
WHERE
   class_mode_cd = 'JIC'
   AND
   NOT (ref_task_class.task_class_db_id = 0 AND
        ref_task_class.task_class_cd   IN ('OPENPANEL','CLOSEPANEL')
       );