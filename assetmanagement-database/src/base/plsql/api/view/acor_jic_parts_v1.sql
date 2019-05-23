--liquibase formatted sql


--changeSet acor_jic_parts_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_jic_parts_v1
AS
SELECT
   task_task.alt_id                   AS task_id,
   eqp_bom_part.bom_part_cd           AS part_group_code,
   eqp_bom_part.bom_part_name         AS part_group_name,
   eqp_bom_part.part_qt               AS part_quantity,
   task_part_list.remove_bool         AS removal_flag,
   task_part_list.install_bool        AS install_flag,
   task_part_list.req_priority_cd     AS request_priority_code,
   task_part_list.req_action_cd       AS request_action_code
FROM
   task_task
   INNER JOIN task_part_list ON
      task_task.task_db_id = task_part_list.task_db_id AND
      task_task.task_id    = task_part_list.task_id
   INNER JOIN eqp_bom_part ON
      task_part_list.bom_part_db_id = eqp_bom_part.bom_part_db_id AND
      task_part_list.bom_part_id    = eqp_bom_part.bom_part_id
   INNER JOIN ref_task_class ON
      task_task.task_class_db_id = ref_task_class.task_class_db_id AND
      task_task.task_class_cd    = ref_task_class.task_class_cd
WHERE
   class_mode_cd = 'JIC'
   AND
   NOT (ref_task_class.task_class_db_id = 0 AND
        ref_task_class.task_class_cd   IN ('OPENPANEL','CLOSEPANEL')
       )
;