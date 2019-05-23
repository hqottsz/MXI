--liquibase formatted sql


--changeSet MX-24838.2:1 stripComments:false
-- This view decides the task revision a carrier should use.
-- If the task definition is controlled by the maintenace program,
-- it delegates the decision to vw_maint_prgm_carrier_task.
CREATE OR REPLACE VIEW vw_carrier_baseline_task
AS
WITH latest_actv_task AS (
SELECT
   task_task.task_defn_db_id,
   task_task.task_defn_id,
   CASE WHEN task_task.task_def_status_cd = 'OBSOLETE' THEN NULL ELSE task_task.task_db_id END AS task_db_id,
   CASE WHEN task_task.task_def_status_cd = 'OBSOLETE' THEN NULL ELSE task_task.task_id END AS task_id
FROM
   task_task
WHERE
   (task_task.task_def_status_db_id, task_task.task_def_status_cd) IN ( (0, 'ACTV'), (0, 'OBSOLETE') )
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
         vw_maint_prgm_carrier_task
      WHERE
         vw_maint_prgm_carrier_task.task_defn_db_id = task_task.task_defn_db_id AND
         vw_maint_prgm_carrier_task.task_defn_id = task_task.task_defn_id
   )
)
(
-- Gets the latest approved task definition
SELECT
  org_carrier.carrier_db_id,
  org_carrier.carrier_id,
  latest_actv_task.task_defn_db_id,
  latest_actv_task.task_defn_id,
  latest_actv_task.task_db_id,
  latest_actv_task.task_id
FROM
  org_carrier,
  latest_actv_task
UNION ALL
SELECT
  NULL,
  NULL,
  latest_actv_task.task_defn_db_id,
  latest_actv_task.task_defn_id,
  latest_actv_task.task_db_id,
  latest_actv_task.task_id
FROM
  latest_actv_task
UNION ALL
-- If a task definition is controlled by the maintenance program, use the maintenance program version
SELECT
   vw_maint_prgm_carrier_task.carrier_db_id,
   vw_maint_prgm_carrier_task.carrier_id,
   vw_maint_prgm_carrier_task.task_defn_db_id,
   vw_maint_prgm_carrier_task.task_defn_id,
   vw_maint_prgm_carrier_task.task_db_id,
   vw_maint_prgm_carrier_task.task_id
FROM
   vw_maint_prgm_carrier_task
);