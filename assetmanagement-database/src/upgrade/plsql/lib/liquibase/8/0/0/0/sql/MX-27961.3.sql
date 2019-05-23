--liquibase formatted sql


--changeSet MX-27961.3:1 stripComments:false
-- This view returns the latest approved task revisions for a carrier
CREATE OR REPLACE VIEW vw_carrier_baseline_task
AS
-- If the task defn is temporarily issued, use the temporarily issued task revision instead
SELECT
   vw_maint_prgm_task.task_defn_db_id,
   vw_maint_prgm_task.task_defn_id,
   vw_maint_prgm_task.task_db_id,
   vw_maint_prgm_task.task_id
FROM
   vw_maint_prgm_task
WHERE
   vw_maint_prgm_task.carrier_db_id = context_package.get_carrier_db_id() AND
   vw_maint_prgm_task.carrier_id    = context_package.get_carrier_id()
UNION ALL
-- If the task defn is only approved for other carriers,
-- initialized tasks on this carrier should be cancelled
SELECT DISTINCT
   vw_maint_prgm_task.task_defn_db_id,
   vw_maint_prgm_task.task_defn_id,
   NULL AS task_db_id,
   NULL AS task_id
FROM
   vw_maint_prgm_task
WHERE
   NOT EXISTS(
    SELECT
       1
    FROM
       vw_maint_prgm_task carrier_vw_maint_prgm_task
    WHERE
       carrier_vw_maint_prgm_task.task_defn_db_id = vw_maint_prgm_task.task_defn_db_id AND
       carrier_vw_maint_prgm_task.task_defn_id    = vw_maint_prgm_task.task_defn_id
       AND
       carrier_vw_maint_prgm_task.carrier_db_id = context_package.get_carrier_db_id() AND
       carrier_vw_maint_prgm_task.carrier_id    = context_package.get_carrier_id()
   )
;