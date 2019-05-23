--liquibase formatted sql


--changeSet OPER-25080-6:1 stripComments:false
-- This view returns the latest approved task revisions for a carrier
CREATE OR REPLACE VIEW VW_CARRIER_BASELINE_TASK AS
SELECT
   vw_maint_prgm_task.task_defn_db_id,
   vw_maint_prgm_task.task_defn_id,
   vw_maint_prgm_task.task_db_id,
   vw_maint_prgm_task.task_id,
   vw_maint_prgm_task.carrier_db_id as filter_carrier_db_id,
   vw_maint_prgm_task.carrier_id as filter_carrier_id
FROM
   vw_maint_prgm_task
UNION ALL
-- If the task defn is only approved for other carriers,
-- initialized tasks on this carrier should be cancelled
SELECT DISTINCT
   vw_maint_prgm_task.task_defn_db_id,
   vw_maint_prgm_task.task_defn_id,
   NULL AS task_db_id,
   NULL AS task_id,
   carrier_vw_maint_prgm_task.carrier_db_id as filter_carrier_db_id,
   carrier_vw_maint_prgm_task.carrier_id as filter_carrier_id
FROM
   vw_maint_prgm_task
   LEFT OUTER JOIN vw_maint_prgm_task carrier_vw_maint_prgm_task ON
       carrier_vw_maint_prgm_task.task_defn_db_id = vw_maint_prgm_task.task_defn_db_id AND
       carrier_vw_maint_prgm_task.task_defn_id    = vw_maint_prgm_task.task_defn_id
WHERE
   carrier_vw_maint_prgm_task.task_defn_id is null
;


