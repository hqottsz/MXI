--liquibase formatted sql


--changeSet acor_req_pn_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_req_pn_v1
AS
SELECT
   task_task.alt_id              AS task_id,
   task_task.task_cd             AS task_code,
   part.alt_id                   AS part_id,
   part.part_no_oem              AS part_number,
   task_task.revision_ord        AS revision_order
FROM
   task_task
   -- task class
   INNER JOIN ref_task_class ON
      task_task.task_class_db_id = ref_task_class.task_class_db_id AND
      task_task.task_class_cd    = ref_task_class.task_class_cd
   -- parts
   INNER JOIN task_part_map ON
      task_task.task_db_id = task_part_map.task_db_id AND
      task_task.task_id    = task_part_map.task_id
   INNER JOIN eqp_part_no part ON
      task_part_map.part_no_db_id = part.part_no_db_id AND
      task_part_map.part_no_id    = part.part_no_id
WHERE
   class_mode_cd = 'REQ'
   AND
   task_task.assmbl_cd IS NULL
   AND
   task_task.workscope_bool = 0
;