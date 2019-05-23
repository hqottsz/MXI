--liquibase formatted sql


--changeSet 1vw_maint_prgm_task:1 stripComments:false
CREATE OR REPLACE VIEW vw_maint_prgm_task AS
   -- task is issued as temp
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
   -- assigned to latest maintenance program
   SELECT
      maint_prgm_carrier_map.carrier_db_id,
      maint_prgm_carrier_map.carrier_id,
      maint_prgm_task.task_defn_db_id,
      maint_prgm_task.task_defn_id,
      maint_prgm_task.task_db_id,
      maint_prgm_task.task_id
   FROM
      maint_prgm_task
      INNER JOIN maint_prgm_carrier_map ON
         maint_prgm_carrier_map.maint_prgm_db_id = maint_prgm_task.maint_prgm_db_id AND
         maint_prgm_carrier_map.maint_prgm_id    = maint_prgm_task.maint_prgm_id
   WHERE
      maint_prgm_carrier_map.latest_revision_bool = 1
      AND
      maint_prgm_task.unassign_bool = 0
      AND
      NOT EXISTS(
         SELECT 1 FROM
            maint_prgm_carrier_temp_task
         WHERE
            maint_prgm_carrier_temp_task.task_defn_db_id = maint_prgm_task.task_defn_db_id AND
            maint_prgm_carrier_temp_task.task_defn_id    = maint_prgm_task.task_defn_id
            AND
            maint_prgm_carrier_temp_task.carrier_db_id = maint_prgm_carrier_map.carrier_db_id AND
            maint_prgm_carrier_temp_task.carrier_id    = maint_prgm_carrier_map.carrier_id
      )
;