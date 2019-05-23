--liquibase formatted sql


--changeSet OPER-25080-4:1 stripComments:false
-- This view determines the what task should be used on an inventory tree
CREATE OR REPLACE VIEW VW_H_BASELINE_TASK AS
(
-- Gets the latest approved task definition
SELECT
   task_task.task_defn_db_id,
   task_task.task_defn_id,
   CASE WHEN task_task.task_def_status_cd = 'OBSOLETE' THEN NULL ELSE task_task.task_db_id END AS task_db_id,
   CASE WHEN task_task.task_def_status_cd = 'OBSOLETE' THEN NULL ELSE task_task.task_id END AS task_id,
   null as filter_inv_no_db_id,
   null as filter_inv_no_id
FROM
   task_task
   INNER JOIN task_fleet_approval ON
      task_fleet_approval.task_db_id = task_task.task_db_id AND
      task_fleet_approval.task_id    = task_task.task_id
UNION ALL
-- For config task definitions on assemblies or aircrafts
SELECT
   vw_carrier_baseline_task.task_defn_db_id,
   vw_carrier_baseline_task.task_defn_id,
   CASE WHEN task_task.task_def_status_cd = 'OBSOLETE' THEN NULL ELSE vw_carrier_baseline_task.task_db_id END AS task_db_id,
   CASE WHEN task_task.task_def_status_cd = 'OBSOLETE' THEN NULL ELSE vw_carrier_baseline_task.task_id END AS task_id,
   inv_inv.inv_no_db_id as filter_inv_no_db_id,
   inv_inv.inv_no_id as filter_inv_no_id
FROM
   inv_inv,
   inv_inv h_inv_inv,
   vw_carrier_baseline_task,
   task_task
WHERE
   -- Highest inventory (attached)
   h_inv_inv.inv_no_db_id = inv_inv.h_inv_no_db_id AND
   h_inv_inv.inv_no_id    = inv_inv.h_inv_no_id
   AND
   h_inv_inv.inv_class_cd IN ('ACFT', 'ASSY')
   AND
   task_task.task_db_id = vw_carrier_baseline_task.task_db_id AND
   task_task.task_id    = vw_carrier_baseline_task.task_id AND
   -- Use highest carrier for context
   h_inv_inv.carrier_db_id = vw_carrier_baseline_task.filter_carrier_db_id AND
   h_inv_inv.carrier_id = vw_carrier_baseline_task.filter_carrier_id
UNION ALL
-- For config task definitions on loose components
SELECT
   vw_loose_baseline_task.task_defn_db_id,
   vw_loose_baseline_task.task_defn_id,
   CASE WHEN task_task.task_def_status_cd = 'OBSOLETE' THEN NULL ELSE vw_loose_baseline_task.task_db_id END AS task_db_id,
   CASE WHEN task_task.task_def_status_cd = 'OBSOLETE' THEN NULL ELSE vw_loose_baseline_task.task_id END AS task_id,
   inv_inv.inv_no_db_id as filter_inv_no_db_id,
   inv_inv.inv_no_id as filter_inv_no_id
FROM
   inv_inv,
   inv_inv h_inv_inv,
   vw_loose_baseline_task,
   task_task
WHERE
   -- Highest inventory (loose)
   h_inv_inv.inv_no_db_id = inv_inv.h_inv_no_db_id AND
   h_inv_inv.inv_no_id    = inv_inv.h_inv_no_id
   AND
   h_inv_inv.inv_class_cd NOT IN ('ACFT', 'ASSY')
   AND
   task_task.task_db_id = vw_loose_baseline_task.task_db_id AND
   task_task.task_id    = vw_loose_baseline_task.task_id AND
   -- Use highest carrier for context
   h_inv_inv.carrier_db_id = vw_loose_baseline_task.filter_carrier_db_id AND
   h_inv_inv.carrier_id = vw_loose_baseline_task.filter_carrier_id
)
;
