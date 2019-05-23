--liquibase formatted sql


--changeSet acor_req_zone_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_req_zone_v1
AS 
SELECT
   task.alt_id            AS task_id,
   eqp_task_zone.zone_cd  AS zone_code
FROM
   (
      SELECT
         req_task.alt_id,
         jic_task.task_db_id,
         jic_task.task_id
      FROM
         task_jic_req_map
         INNER JOIN task_task req_task ON
            task_jic_req_map.req_task_defn_db_id = req_task.task_defn_db_id AND
            task_jic_req_map.req_task_defn_id    = req_task.task_defn_id
         INNER JOIN task_task jic_task ON
            task_jic_req_map.jic_task_db_id = jic_task.task_db_id AND
            task_jic_req_map.jic_task_id    = jic_task.task_id
      WHERE
         jic_task.task_def_status_cd IN ('BUILD','ACTV','OBSOLETE')
      UNION ALL
      SELECT
         alt_id,
         task_db_id,
         task_id
      FROM
         task_task
         INNER JOIN ref_task_class ON
            task_task.task_class_db_id = ref_task_class.task_class_db_id AND
            task_task.task_class_cd    = ref_task_class.task_class_cd
      WHERE
         ref_task_class.class_mode_cd = 'REQ'
         AND
         task_def_status_cd IN ('BUILD','ACTV','OBSOLETE')
         AND
         task_task.workscope_bool = 1
   ) task
   INNER JOIN task_zone ON
      task.task_db_id = task_zone.task_db_id AND
      task.task_id    = task_zone.task_id
   INNER JOIN eqp_task_zone ON
      task_zone.zone_db_id = eqp_task_zone.zone_db_id AND
      task_zone.zone_id    = eqp_task_zone.zone_id
GROUP BY
   task.alt_id,
   eqp_task_zone.zone_cd;