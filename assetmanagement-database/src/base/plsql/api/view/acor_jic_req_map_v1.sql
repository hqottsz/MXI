--liquibase formatted sql


--changeSet acor_jic_req_map_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_jic_req_map_v1
AS
WITH ivw_task_task
AS
   (
     SELECT
        alt_id,
        task_db_id,
        task_id,
        task_defn_db_id,
        task_defn_id,
        task_cd
     FROM
        task_task
   )
SELECT
   task_jic.alt_id          AS jic_id,
   task_jic.task_cd         AS jic_task_code,
   task_defn.alt_id         AS req_id,
   task_defn.task_cd        AS req_task_code
FROM
   task_jic_req_map
   INNER JOIN ivw_task_task task_jic ON
      task_jic_req_map.jic_task_db_id = task_jic.task_db_id AND
      task_jic_req_map.jic_task_id    = task_jic.task_id
   INNER JOIN ivw_task_task task_defn ON
      task_jic_req_map.req_task_defn_db_id = task_defn.task_defn_db_id AND
      task_jic_req_map.req_task_defn_id   = task_defn.task_defn_id;