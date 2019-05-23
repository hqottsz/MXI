--liquibase formatted sql


--changeSet aopr_req_ietm_part_access_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_req_ietm_part_access_v1
AS 
WITH
rvw_task
AS
(
  SELECT
     alt_id,
     task_defn_db_id,
     task_defn_id,
     task_db_id,
     task_id,
     task_class_db_id,
     task_class_cd,
     workscope_bool,
     task_def_status_cd
  FROM
     task_task
),
rvw_req
AS
(
      SELECT
         req_task.alt_id AS task_id
      FROM
         task_jic_req_map
         INNER JOIN rvw_task req_task ON
            task_jic_req_map.req_task_defn_db_id = req_task.task_defn_db_id AND
            task_jic_req_map.req_task_defn_id    = req_task.task_defn_id
         INNER JOIN rvw_task jic_task ON
            task_jic_req_map.jic_task_db_id = jic_task.task_db_id AND
            task_jic_req_map.jic_task_id    = jic_task.task_id
      WHERE
         jic_task.task_def_status_cd IN ('BUILD','ACTV','OBSOLETE')
      UNION ALL
      SELECT
         alt_id
      FROM
         rvw_task task_task
         INNER JOIN ref_task_class ON
            task_task.task_class_db_id = ref_task_class.task_class_db_id AND
            task_task.task_class_cd    = ref_task_class.task_class_cd
      WHERE
         ref_task_class.class_mode_cd = 'REQ'
         AND
         task_def_status_cd IN ('BUILD','ACTV','OBSOLETE')
         AND
         task_task.workscope_bool = 1
),
rvw_req_ietm
AS
(
  SELECT
     task_id,
     LISTAGG(ietm_name,chr(13)) WITHIN GROUP (ORDER BY 1)  ietm_name
  FROM
     acor_req_ietm_v1
  GROUP BY
     task_id
),
rvw_req_part
AS
(
  SELECT
     task_id,
     LISTAGG(part_number,',') WITHIN GROUP (ORDER BY 1)  part_number
  FROM
     acor_req_part_v1
  GROUP BY
     task_id
),
rvw_req_zone
AS
(
  SELECT
     task_id,
     LISTAGG(zone_code,',') WITHIN GROUP (ORDER BY 1) zone_code
  FROM
     acor_req_zone_v1
  GROUP BY
     task_id
),
rvw_req_panel
AS
(
  SELECT
     task_id,
     LISTAGG(panel_code,',') WITHIN GROUP (ORDER BY 1) panel_code
  FROM
     acor_req_panel_v1
  GROUP BY
     task_id
)
SELECT
   rvw_req.task_id,
   ietm_name,
   part_number,
   zone_code,
   panel_code
FROM
   rvw_req
   LEFT JOIN rvw_req_ietm ON
     rvw_req.task_id = rvw_req_ietm.task_id
   LEFT JOIN rvw_req_part ON
     rvw_req.task_id = rvw_req_part.task_id
   LEFT JOIN rvw_req_zone ON
     rvw_req.task_id = rvw_req_zone.task_id
   LEFT JOIN rvw_req_panel ON
     rvw_req.task_id = rvw_req_panel.task_id;