--liquibase formatted sql


--changeSet MX-27961.5:1 stripComments:false
-- This view determines the what task should be used on an inventory tree
CREATE OR REPLACE VIEW vw_h_baseline_task
AS
(
-- Gets the latest approved task definition
SELECT
   task_task.task_defn_db_id,
   task_task.task_defn_id,
   CASE WHEN task_task.task_def_status_cd = 'OBSOLETE' THEN NULL ELSE task_task.task_db_id END AS task_db_id,
   CASE WHEN task_task.task_def_status_cd = 'OBSOLETE' THEN NULL ELSE task_task.task_id END AS task_id
FROM
   task_task
WHERE
   task_task.task_def_status_db_id = 0 AND
   task_task.task_def_status_cd IN ( 'ACTV', 'OBSOLETE' )
   AND
   -- We want to freeze baseline synchronization when the task definition
   -- is a build or revision maintenance program
   NOT EXISTS
   (
      SELECT
         1
      FROM
         maint_prgm_task
         INNER JOIN maint_prgm ON
            maint_prgm.maint_prgm_db_id = maint_prgm_task.maint_prgm_db_id AND
            maint_prgm.maint_prgm_id    = maint_prgm_task.maint_prgm_id
      WHERE
         maint_prgm_task.task_defn_db_id = task_task.task_defn_db_id AND
         maint_prgm_task.task_defn_id    = task_task.task_defn_id
         AND
         maint_prgm.maint_prgm_status_cd IN ('REVISION', 'BUILD')
         AND
         maint_prgm_task.unassign_bool = 0
   )
   AND
   -- If the task definition is controlled by maintenance program, use alternative sub-query
   NOT EXISTS
   (
      SELECT
         1
      FROM
         vw_maint_prgm_task
      WHERE
         vw_maint_prgm_task.task_defn_db_id = task_task.task_defn_db_id AND
         vw_maint_prgm_task.task_defn_id    = task_task.task_defn_id
   )
UNION ALL
-- For config task definitions on assemblies or aircrafts
SELECT
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
   inv_inv.inv_no_id    = context_package.get_inv_no_id()
   AND
   h_inv_inv.inv_no_db_id = inv_inv.h_inv_no_db_id AND
   h_inv_inv.inv_no_id    = inv_inv.h_inv_no_id
   AND
   h_inv_inv.inv_class_cd IN ('ACFT', 'ASSY')
UNION ALL
-- For config task definitions on loose components
SELECT
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
   inv_inv.inv_no_id    = context_package.get_inv_no_id()
   AND
   h_inv_inv.inv_no_db_id = inv_inv.h_inv_no_db_id AND
   h_inv_inv.inv_no_id    = inv_inv.h_inv_no_id
   AND
   h_inv_inv.inv_class_cd NOT IN ('ACFT', 'ASSY')
);