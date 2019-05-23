--liquibase formatted sql


--changeSet vw_baseline_task:1 stripComments:false
-- This view determines the what task should be used on an inventory
CREATE OR REPLACE VIEW VW_BASELINE_TASK AS
SELECT
   vw_inv_task.task_defn_db_id,
   vw_inv_task.task_defn_id,
   CASE WHEN task_task.task_def_status_cd = 'OBSOLETE' THEN NULL ELSE vw_inv_task.task_db_id END AS task_db_id,
   CASE WHEN task_task.task_def_status_cd = 'OBSOLETE' THEN NULL ELSE vw_inv_task.task_id END AS task_id,
   vw_inv_task.filter_inv_no_db_id,
   vw_inv_task.filter_inv_no_id
FROM
   vw_inv_task
   INNER JOIN task_task ON
      task_task.task_db_id = vw_inv_task.task_db_id AND
      task_task.task_id    = vw_inv_task.task_id
   INNER JOIN task_fleet_approval ON
      task_fleet_approval.task_db_id = task_task.task_db_id AND
      task_fleet_approval.task_id    = task_task.task_id
UNION ALL
-- If a task defnition is in a maintenance program, special rules apply towards what gets returned
(
-- If the task defn is not applicable to the carrier (as determined by the MP), return NULL
SELECT DISTINCT
   vw_inv_task.task_defn_db_id,
   vw_inv_task.task_defn_id,
   NULL,
   NULL,
   vw_inv_task.filter_inv_no_db_id,
   vw_inv_task.filter_inv_no_id
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
   CASE WHEN task_task.task_def_status_cd = 'OBSOLETE' THEN NULL ELSE vw_inv_task.task_db_id END AS task_db_id,
   CASE WHEN task_task.task_def_status_cd = 'OBSOLETE' THEN NULL ELSE vw_inv_task.task_id END AS task_id,
   vw_inv_task.filter_inv_no_db_id,
   vw_inv_task.filter_inv_no_id
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
SELECT
   loose_baseline_tasks.task_defn_db_id,
   loose_baseline_tasks.task_defn_id,
   CASE WHEN loose_baseline_tasks.task_def_status_cd = 'OBSOLETE' THEN NULL ELSE loose_baseline_tasks.task_db_id END AS task_db_id,
   CASE WHEN loose_baseline_tasks.task_def_status_cd = 'OBSOLETE' THEN NULL ELSE loose_baseline_tasks.task_id END AS task_id,
   loose_baseline_tasks.filter_inv_no_db_id,
   loose_baseline_tasks.filter_inv_no_id
FROM
  (
    SELECT DISTINCT
       vw_inv_task.task_defn_db_id,
       vw_inv_task.task_defn_id,
       FIRST_VALUE(task_task.task_db_id) OVER (
          PARTITION BY task_task.task_defn_db_id, task_task.task_defn_id, vw_inv_task.filter_inv_no_db_id, vw_inv_task.filter_inv_no_id
          ORDER BY task_task.revision_ord DESC
       ) AS task_db_id,
       FIRST_VALUE(task_task.task_id) OVER (
          PARTITION BY task_task.task_defn_db_id, task_task.task_defn_id, vw_inv_task.filter_inv_no_db_id, vw_inv_task.filter_inv_no_id
          ORDER BY task_task.revision_ord DESC
       ) AS task_id,
       FIRST_VALUE(task_task.task_def_status_cd) OVER (
          PARTITION BY task_task.task_defn_db_id, task_task.task_defn_id, vw_inv_task.filter_inv_no_db_id, vw_inv_task.filter_inv_no_id
          ORDER BY task_task.revision_ord DESC
       ) AS task_def_status_cd,
       vw_inv_task.filter_inv_no_db_id,
       vw_inv_task.filter_inv_no_id
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
   ) loose_baseline_tasks
)
;
