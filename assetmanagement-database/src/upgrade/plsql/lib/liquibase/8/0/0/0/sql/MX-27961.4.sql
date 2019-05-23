--liquibase formatted sql


--changeSet MX-27961.4:1 stripComments:false
-- This view decides the task revision a loose component should use.
-- If the task definition is controlled by the maintenace program,
-- it gets the latest approved revision as determined by vw_maint_prgm_carrier_task.
CREATE OR REPLACE VIEW vw_loose_baseline_task
AS
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
   vw_maint_prgm_task
   INNER JOIN task_task ON
      task_task.task_db_id = vw_maint_prgm_task.task_db_id AND
      task_task.task_id    = vw_maint_prgm_task.task_id
;