--liquibase formatted sql


--changeSet MX-25206.2:1 stripComments:false
-- This view determines the what task should be used on an inventory
CREATE OR REPLACE VIEW vw_baseline_task
AS
SELECT DISTINCT
   vw_h_baseline_task.task_defn_db_id,
   vw_h_baseline_task.task_defn_id,
   vw_h_baseline_task.task_db_id,
   vw_h_baseline_task.task_id
FROM
   vw_inv_task
   INNER JOIN vw_h_baseline_task ON
      vw_h_baseline_task.task_defn_db_id = vw_inv_task.task_defn_db_id AND
      vw_h_baseline_task.task_defn_id    = vw_inv_task.task_defn_id
WHERE
   (
      vw_h_baseline_task.task_db_id = vw_inv_task.task_db_id AND
      vw_h_baseline_task.task_id    = vw_inv_task.task_id
   ) OR (
      vw_h_baseline_task.task_db_id IS NULL AND
      vw_h_baseline_task.task_id IS NULL
      AND
      -- When the task is part-based, vw_inv_task needs to be the latest revision to continue.
      -- vw_h_baseline_task should not know if the active task is applicable for part-based tasks.
      EXISTS (
        SELECT
           1
        FROM
           task_task
           INNER JOIN task_task part_task ON
              part_task.task_defn_db_id = task_task.task_defn_db_id AND
              part_task.task_defn_id = task_task.task_defn_id
           LEFT OUTER JOIN task_part_map ON
              task_part_map.task_db_id = part_task.task_db_id AND
              task_part_map.task_id = part_task.task_id
        WHERE
           task_task.task_db_id = vw_inv_task.task_db_id AND
           task_task.task_id = vw_inv_task.task_id
           AND
           (
              (
                 task_task.task_def_status_db_id = 0 AND
                 task_task.task_def_status_cd IN ('ACTV', 'OBSOLETE')
              ) OR (
                 task_part_map.task_db_id IS NULL AND
                 task_part_map.task_id IS NULL
              )
           )
      )
   )
;