--liquibase formatted sql


--changeSet MX-25206.1:1 stripComments:false
-- This view determines the what task should be used on an inventory tree
CREATE OR REPLACE VIEW vw_h_baseline_task
AS
(
-- For part-based task definitions
SELECT DISTINCT
   task_task.task_defn_db_id,
   task_task.task_defn_id,
   CASE WHEN task_task.task_def_status_cd = 'OBSOLETE' THEN NULL ELSE task_task.task_db_id END AS task_db_id,
   CASE WHEN task_task.task_def_status_cd = 'OBSOLETE' THEN NULL ELSE task_task.task_id END AS task_id
FROM
   task_task,
   task_task part_task,
   task_part_map
WHERE
   -- Is the latest approved version
   task_task.task_def_status_db_id = 0 AND
   task_task.task_def_status_cd IN ('ACTV', 'OBSOLETE')
   AND
   -- Is a part-based task definition
   part_task.task_db_id = task_part_map.task_db_id AND
   part_task.task_id = task_part_map.task_id
   AND   
   task_task.task_defn_db_id = part_task.task_defn_db_id AND
   task_task.task_defn_id = part_task.task_defn_id
UNION ALL
-- For config task definitions on assemblies or aircrafts
SELECT DISTINCT
   vw_carrier_baseline_task.task_defn_db_id,
   vw_carrier_baseline_task.task_defn_id,
   vw_carrier_baseline_task.task_db_id,
   vw_carrier_baseline_task.task_id
FROM
   inv_inv,
   inv_inv h_inv_inv,
   vw_carrier_baseline_task
WHERE
   -- Highest inventory (attached)
   inv_inv.inv_no_db_id = context_package.get_inv_no_db_id() AND
   inv_inv.inv_no_id = context_package.get_inv_no_id()
   AND
   h_inv_inv.inv_no_db_id = inv_inv.h_inv_no_db_id AND
   h_inv_inv.inv_no_id = inv_inv.h_inv_no_id
   AND
   h_inv_inv.inv_class_cd IN ('ACFT', 'ASSY')
   AND
   (
      (
         h_inv_inv.carrier_db_id IS NOT NULL AND
         h_inv_inv.carrier_id IS NOT NULL
         AND
         vw_carrier_baseline_task.carrier_db_id = h_inv_inv.carrier_db_id AND
         vw_carrier_baseline_task.carrier_id    = h_inv_inv.carrier_id
      )
      OR
      (
         h_inv_inv.carrier_db_id IS NULL AND
         h_inv_inv.carrier_id IS NULL
         AND
         vw_carrier_baseline_task.carrier_db_id IS NULL AND
         vw_carrier_baseline_task.carrier_id IS NULL
      )
   )
   AND
   NOT EXISTS(
     SELECT
        1
     FROM
        task_part_map
     INNER JOIN task_task ON
        task_task.task_db_id = task_part_map.task_db_id AND
        task_task.task_id    = task_part_map.task_id
     WHERE
        task_task.task_defn_db_id = vw_carrier_baseline_task.task_defn_db_id AND
        task_task.task_defn_id    = vw_carrier_baseline_task.task_defn_id
   )
UNION ALL
-- For config task definitions on loose components
SELECT DISTINCT
   vw_loose_baseline_task.task_defn_db_id,
   vw_loose_baseline_task.task_defn_id,
   vw_loose_baseline_task.task_db_id,
   vw_loose_baseline_task.task_id
FROM
   inv_inv,
   inv_inv h_inv_inv,
   vw_loose_baseline_task
WHERE
   -- Highest inventory (loose)
   inv_inv.inv_no_db_id = context_package.get_inv_no_db_id() AND
   inv_inv.inv_no_id = context_package.get_inv_no_id()
   AND
   h_inv_inv.inv_no_db_id = inv_inv.h_inv_no_db_id AND
   h_inv_inv.inv_no_id = inv_inv.h_inv_no_id
   AND
   h_inv_inv.inv_class_cd NOT IN ('ACFT', 'ASSY')
   AND
   NOT EXISTS(
      SELECT
         1
      FROM task_part_map
      INNER JOIN task_task ON
         task_task.task_db_id = task_part_map.task_db_id AND
         task_task.task_id = task_part_map.task_id
      WHERE
         task_task.task_defn_db_id = vw_loose_baseline_task.task_defn_db_id AND
         task_task.task_defn_id = vw_loose_baseline_task.task_defn_id
   )
);