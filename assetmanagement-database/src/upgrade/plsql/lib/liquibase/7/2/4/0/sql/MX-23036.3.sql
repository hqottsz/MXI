--liquibase formatted sql


--changeSet MX-23036.3:1 stripComments:false
-- This view decides the task revision a loose component should use.
-- If the task definition is controlled by the maintenace program,
-- it gets the latest approved revision as determined by vw_maint_prgm_carrier_task.
CREATE OR REPLACE FORCE VIEW vw_loose_baseline_task
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
            maint_prgm.maint_prgm_id = maint_prgm_task.maint_prgm_id
      WHERE
         maint_prgm_task.task_defn_db_id = task_task.task_defn_db_id AND
         maint_prgm_task.task_defn_id = task_task.task_defn_id
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
         maint_prgm_carrier_map
         INNER JOIN maint_prgm_task ON
            maint_prgm_task.maint_prgm_db_id = maint_prgm_carrier_map.maint_prgm_db_id AND
            maint_prgm_task.maint_prgm_id = maint_prgm_carrier_map.maint_prgm_id
      WHERE
         maint_prgm_task.task_defn_db_id = task_task.task_defn_db_id AND
         maint_prgm_task.task_defn_id = task_task.task_defn_id
         AND
         maint_prgm_carrier_map.latest_revision_bool = 1
         AND
         maint_prgm_task.unassign_bool = 0
   )
UNION ALL
-- If a task definition is controlled by the maintenance program,
-- use the latest approved revision for loose components
SELECT DISTINCT
   FIRST_VALUE(task_task.task_defn_db_id) OVER (
     PARTITION BY task_task.task_defn_db_id, task_task.task_defn_id
     ORDER BY task_task.revision_ord DESC
   ) AS task_defn_db_id,
   FIRST_VALUE(task_task.task_defn_id) OVER (
     PARTITION BY task_task.task_defn_db_id, task_task.task_defn_id
     ORDER BY task_task.revision_ord DESC
   ) AS task_defn_id,
   FIRST_VALUE(task_task.task_db_id) OVER (
     PARTITION BY task_task.task_defn_db_id, task_task.task_defn_id
     ORDER BY task_task.revision_ord DESC
   ) AS task_db_id,
   FIRST_VALUE(task_task.task_id) OVER (
     PARTITION BY task_task.task_defn_db_id, task_task.task_defn_id
     ORDER BY task_task.revision_ord DESC
   ) AS task_id
FROM
   vw_maint_prgm_carrier_task
   INNER JOIN task_task ON
      task_task.task_db_id = vw_maint_prgm_carrier_task.task_db_id AND
      task_task.task_id = vw_maint_prgm_carrier_task.task_id
);