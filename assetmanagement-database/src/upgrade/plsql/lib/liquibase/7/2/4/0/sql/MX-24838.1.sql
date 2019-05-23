--liquibase formatted sql


--changeSet MX-24838.1:1 stripComments:false
-- This view returns the latest approved task revisions for a carrier
CREATE OR REPLACE VIEW vw_maint_prgm_carrier_task
AS
-- Get the revision specified in the active maintenance program
WITH maint_prgm_carrier_task_defn AS (
    SELECT
       maint_prgm_carrier_map.carrier_db_id,
       maint_prgm_carrier_map.carrier_id,
       maint_prgm_task.task_defn_db_id,
       maint_prgm_task.task_defn_id
    FROM
       maint_prgm_task
       INNER JOIN maint_prgm ON
          maint_prgm_task.maint_prgm_db_id = maint_prgm.maint_prgm_db_id AND
          maint_prgm_task.maint_prgm_id = maint_prgm.maint_prgm_id
       INNER JOIN maint_prgm_carrier_map ON
          maint_prgm.maint_prgm_db_id = maint_prgm_carrier_map.maint_prgm_db_id AND
          maint_prgm.maint_prgm_id = maint_prgm_carrier_map.maint_prgm_id
       LEFT OUTER JOIN maint_prgm_temp_task ON
          maint_prgm_temp_task.maint_prgm_db_id = maint_prgm_task.maint_prgm_db_id AND
          maint_prgm_temp_task.maint_prgm_id = maint_prgm_task.maint_prgm_id AND
          maint_prgm_temp_task.task_defn_db_id = maint_prgm_task.task_defn_db_id AND
          maint_prgm_temp_task.task_defn_id = maint_prgm_task.task_defn_id
    WHERE
       -- task is issued as temp or assigned to latest maintenance program
       (
          (
             maint_prgm.maint_prgm_status_cd = 'REVISION'
             AND
             maint_prgm_temp_task.task_db_id IS NOT NULL
          )
          OR
          (
             maint_prgm_carrier_map.latest_revision_bool = 1
             AND
             maint_prgm_task.unassign_bool = 0
          )
       )
), maint_prgm_carrier_temp_task AS (
SELECT
   maint_prgm_carrier_map.carrier_db_id,
   maint_prgm_carrier_map.carrier_id,
   maint_prgm_temp_task.task_defn_db_id,
   maint_prgm_temp_task.task_defn_id,
   maint_prgm_temp_task.task_db_id,
   maint_prgm_temp_task.task_id
FROM
   maint_prgm
   INNER JOIN maint_prgm_carrier_map ON
      maint_prgm_carrier_map.maint_prgm_db_id = maint_prgm.maint_prgm_db_id AND
      maint_prgm_carrier_map.maint_prgm_id = maint_prgm.maint_prgm_id
   INNER JOIN maint_prgm_temp_task ON
      maint_prgm_temp_task.maint_prgm_db_id = maint_prgm.maint_prgm_db_id AND
      maint_prgm_temp_task.maint_prgm_id = maint_prgm.maint_prgm_id
WHERE
   -- from the revision carrier maintenance program...
   maint_prgm.maint_prgm_status_db_id = 0 AND
   maint_prgm.maint_prgm_status_cd = 'REVISION'
)
SELECT
   org_carrier.carrier_db_id,
   org_carrier.carrier_id,
   maint_prgm_task.task_defn_db_id,
   maint_prgm_task.task_defn_id,
   maint_prgm_task.task_db_id,
   maint_prgm_task.task_id
FROM
   org_carrier
   INNER JOIN maint_prgm_carrier_map ON
      maint_prgm_carrier_map.carrier_db_id = org_carrier.carrier_db_id AND
      maint_prgm_carrier_map.carrier_id = org_carrier.carrier_id
   INNER JOIN maint_prgm_task ON
      maint_prgm_task.maint_prgm_db_id = maint_prgm_carrier_map.maint_prgm_db_id AND
      maint_prgm_task.maint_prgm_id = maint_prgm_carrier_map.maint_prgm_id
WHERE
   -- from the latest carrier maintenance program...
   maint_prgm_carrier_map.latest_revision_bool = 1
   AND
   -- get assigned tasks on the active maintenance program
   maint_prgm_task.unassign_bool = 0
   AND
   -- if the revision maintenance program has a new temp task, then use temp task instead
   NOT EXISTS (
      SELECT
         1
      FROM
         maint_prgm_carrier_temp_task
      WHERE
         maint_prgm_carrier_temp_task.task_defn_db_id = maint_prgm_task.task_defn_db_id AND
         maint_prgm_carrier_temp_task.task_defn_id = maint_prgm_task.task_defn_id
         AND
         maint_prgm_carrier_temp_task.carrier_db_id = org_carrier.carrier_db_id AND
         maint_prgm_carrier_temp_task.carrier_id = org_carrier.carrier_id
   )
UNION ALL
-- If the task defn is temporarily issued, use the temporarily issued task revision instead
SELECT
   maint_prgm_carrier_temp_task.carrier_db_id,
   maint_prgm_carrier_temp_task.carrier_id,
   maint_prgm_carrier_temp_task.task_defn_db_id,
   maint_prgm_carrier_temp_task.task_defn_id,
   maint_prgm_carrier_temp_task.task_db_id,
   maint_prgm_carrier_temp_task.task_id
FROM
   maint_prgm_carrier_temp_task
UNION ALL
-- If the task defn is only approved for other carriers,
-- initialized tasks on this carrier should be cancelled
SELECT
   org_carrier.carrier_db_id,
   org_carrier.carrier_id,
   task_defn.task_defn_db_id,
   task_defn.task_defn_id,
   NULL AS task_db_id,
   NULL AS task_id
FROM
   task_defn,
   org_carrier
WHERE
   -- The task defn is part of any ACTV MP
   EXISTS
   (
      SELECT 1 FROM
        maint_prgm_carrier_task_defn
      WHERE
        maint_prgm_carrier_task_defn.task_defn_db_id = task_defn.task_defn_db_id AND
        maint_prgm_carrier_task_defn.task_defn_id = task_defn.task_defn_id
   )
   AND
   NOT EXISTS
   (
      SELECT 1 FROM
        maint_prgm_carrier_task_defn
      WHERE
        maint_prgm_carrier_task_defn.task_defn_db_id = task_defn.task_defn_db_id AND
        maint_prgm_carrier_task_defn.task_defn_id = task_defn.task_defn_id
        AND
        maint_prgm_carrier_task_defn.carrier_db_id = org_carrier.carrier_db_id AND
        maint_prgm_carrier_task_defn.carrier_id = org_carrier.carrier_id
   )
UNION ALL
-- If the task defn is only approved for other carriers,
-- initialized tasks on this carrier should be cancelled
SELECT
   NULL AS carrier_db_id,
   NULL AS carrier_id,
   task_defn.task_defn_db_id,
   task_defn.task_defn_id,
   NULL AS task_db_id,
   NULL AS task_id
FROM
   task_defn
WHERE
   -- The task defn is part of any ACTV MP
   EXISTS
   (
      SELECT 1 FROM
        maint_prgm_carrier_task_defn
      WHERE
        maint_prgm_carrier_task_defn.task_defn_db_id = task_defn.task_defn_db_id AND
        maint_prgm_carrier_task_defn.task_defn_id = task_defn.task_defn_id
   )
;