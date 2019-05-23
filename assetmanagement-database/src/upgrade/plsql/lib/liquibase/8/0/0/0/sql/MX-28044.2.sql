--liquibase formatted sql


--changeSet MX-28044.2:1 stripComments:false
-- This view determines the what task should be used on an inventory
CREATE OR REPLACE VIEW vw_baseline_task
AS
-- gets the ACTV or OBSOLETE version of task defns that are not currently assigned to an ACTV MP
SELECT
   vw_inv_task.task_defn_db_id,
   vw_inv_task.task_defn_id,
   CASE WHEN task_task.task_def_status_cd = 'OBSOLETE' THEN NULL ELSE vw_inv_task.task_db_id END AS task_db_id,
   CASE WHEN task_task.task_def_status_cd = 'OBSOLETE' THEN NULL ELSE vw_inv_task.task_id END AS task_id
FROM
   vw_inv_task,
   task_task
WHERE
   task_task.task_db_id = vw_inv_task.task_db_id AND
   task_task.task_id    = vw_inv_task.task_id
   AND
   task_task.task_def_status_db_id = 0 AND
   task_task.task_def_status_cd IN ( 'ACTV', 'OBSOLETE' )
   AND
   -- if the requirement is specified in any maintenance program, do not use the default
   NOT EXISTS
   (
      SELECT
         1
      FROM
         maint_prgm_task
         INNER JOIN maint_prgm ON
            maint_prgm.maint_prgm_db_id = maint_prgm_task.maint_prgm_db_id AND
            maint_prgm.maint_prgm_id    = maint_prgm_task.maint_prgm_id
         INNER JOIN maint_prgm_carrier_map ON
            maint_prgm_carrier_map.maint_prgm_db_id = maint_prgm.maint_prgm_db_id AND
            maint_prgm_carrier_map.maint_prgm_id    = maint_prgm.maint_prgm_id
      WHERE
         -- map to task in maintenance program
         maint_prgm_task.task_defn_db_id = vw_inv_task.task_defn_db_id AND
         maint_prgm_task.task_defn_id    = vw_inv_task.task_defn_id
         AND
         -- task is assigned to latest maintenance program
         (
            (
               maint_prgm_carrier_map.latest_revision_bool = 1
               AND
               maint_prgm_task.unassign_bool = 0
            )
            OR
            (
               -- We do not want to return a row unless the task is in an active maintenance program.
               -- This will prevent baseline sync from synchronizing this task until the maintenance
               -- program is completed.
               maint_prgm.maint_prgm_status_cd IN ('REVISION', 'BUILD')
               AND
               maint_prgm_task.unassign_bool = 0
            )
         )
   )
   AND
   NOT EXISTS
   (
      SELECT
         1
      FROM
         maint_prgm_carrier_temp_task
      WHERE
         maint_prgm_carrier_temp_task.task_defn_db_id = vw_inv_task.task_defn_db_id AND
         maint_prgm_carrier_temp_task.task_defn_id    = vw_inv_task.task_defn_id
   )
UNION ALL
-- If a task defnition is in a maintenance program, special rules apply towards what gets returned
(
-- If the task defn is not applicable to the carrier (as determined by the MP), return NULL
SELECT DISTINCT
   vw_inv_task.task_defn_db_id,
   vw_inv_task.task_defn_id,
   NULL,
   NULL
FROM
   inv_inv assmbl_inv,
   vw_inv_task
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
   -- The task defn is part of any ACTV MP
   EXISTS
   (
      SELECT
         1
      FROM
         vw_maint_prgm_task
      WHERE
         -- map to task in maintenance program
         vw_maint_prgm_task.task_defn_db_id = vw_inv_task.task_defn_db_id AND
         vw_maint_prgm_task.task_defn_id    = vw_inv_task.task_defn_id
   )
   AND
   -- The task defn is not part of the carrier MP
   NOT EXISTS
   (
      SELECT
         1
      FROM
         vw_maint_prgm_task
      WHERE
         -- from the latest carrier maintenance program...
         vw_maint_prgm_task.carrier_db_id = assmbl_inv.carrier_db_id AND
         vw_maint_prgm_task.carrier_id    = assmbl_inv.carrier_id
         AND
         vw_maint_prgm_task.task_defn_db_id = vw_inv_task.task_defn_db_id AND
         vw_maint_prgm_task.task_defn_id    = vw_inv_task.task_defn_id
   )
UNION ALL
-- If the task defn is applicable to the carrier, return the MPs task defn revision
SELECT
   vw_inv_task.task_defn_db_id,
   vw_inv_task.task_defn_id,
   vw_inv_task.task_db_id,
   vw_inv_task.task_id
FROM
   vw_inv_task
   INNER JOIN task_task ON
      task_task.task_db_id = vw_inv_task.task_db_id AND
      task_task.task_id    = vw_inv_task.task_id
   INNER JOIN inv_inv assmbl_inv ON
      -- from the latest carrier maintenance program...
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
   INNER JOIN vw_maint_prgm_task ON
      vw_maint_prgm_task.carrier_db_id = assmbl_inv.carrier_db_id AND
      vw_maint_prgm_task.carrier_id    = assmbl_inv.carrier_id
      AND
      vw_maint_prgm_task.task_db_id = task_task.task_db_id AND
      vw_maint_prgm_task.task_id    = task_task.task_id
UNION ALL
-- If the task defn is assigned to a MP and the task defn is also applicable to a loose inv
SELECT DISTINCT
   vw_inv_task.task_defn_db_id,
   vw_inv_task.task_defn_id,
   FIRST_VALUE(task_task.task_db_id) OVER (
      PARTITION BY task_task.task_defn_db_id, task_task.task_defn_id
      ORDER BY task_task.revision_ord DESC
   ) AS task_db_id,
   FIRST_VALUE(task_task.task_id) OVER (
      PARTITION BY task_task.task_defn_db_id, task_task.task_defn_id
      ORDER BY task_task.revision_ord DESC
   ) AS task_id
FROM
   vw_inv_task
   INNER JOIN task_task ON
      task_task.task_db_id = vw_inv_task.task_db_id AND
      task_task.task_id    = vw_inv_task.task_id
   INNER JOIN vw_maint_prgm_task ON
      vw_maint_prgm_task.task_db_id = task_task.task_db_id AND
      vw_maint_prgm_task.task_id = task_task.task_id
WHERE
   vw_inv_task.assmbl_inv_no_db_id IS NULL AND
   vw_inv_task.assmbl_inv_no_id IS NULL
   AND
   vw_inv_task.inv_class_cd NOT IN ('ACFT', 'ASSY')
)
;