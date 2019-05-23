--liquibase formatted sql


--changeSet acor_block_req_map_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_block_req_map_v1
AS
WITH ivw_task_task
AS
   (
     SELECT
        alt_id ID,
        task_db_id,
        task_id,
        task_defn_db_id,
        task_defn_id,
        task_cd
     FROM
        task_task
   )
SELECT
   task_block.ID            AS block_id,
   task_block.task_cd       AS block_task_code,
   task_defn.ID             AS req_id,
   task_defn.task_cd        AS req_task_code
FROM
   task_block_req_map
   INNER JOIN ivw_task_task task_block ON
      task_block_req_map.block_task_db_id = task_block.task_db_id AND
      task_block_req_map.block_task_id    = task_block.task_id
   INNER JOIN ivw_task_task task_defn ON
      task_block_req_map.req_task_defn_db_id = task_defn.task_defn_db_id AND
       task_block_req_map.req_task_defn_id   = task_defn.task_defn_id;