--liquibase formatted sql


--changeSet MX-23036.5:1 stripComments:false
-- This view determines the what task should be used on an inventory
CREATE OR REPLACE FORCE VIEW vw_baseline_task
AS
WITH part_task_defn AS (
   SELECT DISTINCT
      task_task.task_defn_db_id,
      task_task.task_defn_id
   FROM
      task_part_map
      INNER JOIN task_task ON
         task_task.task_db_id = task_part_map.task_db_id AND
         task_task.task_id = task_part_map.task_id
)
(
-- For part-based task definitions
SELECT
   task_task.task_defn_db_id,
   task_task.task_defn_id,
   CASE WHEN task_task.task_def_status_cd = 'OBSOLETE' THEN NULL ELSE task_task.task_db_id END AS task_db_id,
   CASE WHEN task_task.task_def_status_cd = 'OBSOLETE' THEN NULL ELSE task_task.task_id END AS task_id
FROM
   vw_inv_task,
   task_task,
   part_task_defn
WHERE
   -- Is a part-based task definition
   part_task_defn.task_defn_db_id = vw_inv_task.task_defn_db_id AND
   part_task_defn.task_defn_id = vw_inv_task.task_defn_id
   AND
   -- Is the latest approved version
   task_task.task_db_id = vw_inv_task.task_db_id AND
   task_task.task_id = vw_inv_task.task_id
   AND
   task_task.task_def_status_db_id = 0 AND
   task_task.task_def_status_cd IN ('ACTV', 'OBSOLETE')
UNION ALL
-- For config task definitions on assemblies or aircrafts
SELECT DISTINCT
   vw_carrier_baseline_task.task_defn_db_id,
   vw_carrier_baseline_task.task_defn_id,
   vw_carrier_baseline_task.task_db_id,
   vw_carrier_baseline_task.task_id
FROM
   inv_inv assmbl_inv,
   vw_inv_task,
   vw_carrier_baseline_task,
   part_task_defn
WHERE
   assmbl_inv.inv_no_db_id =
      CASE    WHEN vw_inv_task.inv_class_cd IN ('ACFT', 'ASSY')
         THEN vw_inv_task.inv_no_db_id
         ELSE vw_inv_task.assmbl_inv_no_db_id
      END
   AND
   assmbl_inv.inv_no_id =
      CASE    WHEN vw_inv_task.inv_class_cd IN ('ACFT', 'ASSY')
        THEN vw_inv_task.inv_no_id
        ELSE vw_inv_task.assmbl_inv_no_id
     END
   AND
   (
      (
         assmbl_inv.carrier_db_id IS NOT NULL AND
         assmbl_inv.carrier_id IS NOT NULL
         AND
         vw_carrier_baseline_task.carrier_db_id = assmbl_inv.carrier_db_id AND
         vw_carrier_baseline_task.carrier_id = assmbl_inv.carrier_id
      )
      OR
      (
         assmbl_inv.carrier_db_id IS NULL AND
         assmbl_inv.carrier_id IS NULL
         AND
         vw_carrier_baseline_task.carrier_db_id IS NULL AND
         vw_carrier_baseline_task.carrier_id IS NULL
      )
   )
   AND
   vw_carrier_baseline_task.task_defn_db_id = vw_inv_task.task_defn_db_id AND
   vw_carrier_baseline_task.task_defn_id = vw_inv_task.task_defn_id
   AND
   part_task_defn.task_defn_db_id (+)= vw_inv_task.task_defn_db_id AND
   part_task_defn.task_defn_id (+)= vw_inv_task.task_defn_id
   AND
   part_task_defn.task_defn_id IS NULL
UNION ALL
-- For config task definitions on loose components
SELECT DISTINCT
   vw_loose_baseline_task.task_defn_db_id,
   vw_loose_baseline_task.task_defn_id,
   vw_loose_baseline_task.task_db_id,
   vw_loose_baseline_task.task_id
FROM
   vw_inv_task,
   vw_loose_baseline_task,
   part_task_defn
WHERE
   vw_inv_task.assmbl_inv_no_db_id IS NULL AND
   vw_inv_task.assmbl_inv_no_id IS NULL
   AND
   vw_inv_task.inv_class_cd NOT IN ('ACFT', 'ASSY')
   AND
   vw_loose_baseline_task.task_defn_db_id = vw_inv_task.task_defn_db_id AND
   vw_loose_baseline_task.task_defn_id = vw_inv_task.task_defn_id
   AND
   part_task_defn.task_defn_db_id (+)= vw_inv_task.task_defn_db_id AND
   part_task_defn.task_defn_id (+)= vw_inv_task.task_defn_id
   AND
   part_task_defn.task_defn_id IS NULL
);