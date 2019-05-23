--liquibase formatted sql


--changeSet OPER-25080-3:1 stripComments:false
/*
   This is a view of inventory to task mappings that uses a sub inventory assembly view
*/
CREATE OR REPLACE VIEW VW_INV_TASK AS
SELECT
   inv_inv.inv_no_db_id,
   inv_inv.inv_no_id,
   inv_inv.assmbl_inv_no_db_id,
   inv_inv.assmbl_inv_no_id,
   inv_inv.carrier_db_id,
   inv_inv.carrier_id,
   inv_inv.inv_class_db_id,
   inv_inv.inv_class_cd,
   task_task.task_defn_db_id,
   task_task.task_defn_id,
   task_task.task_db_id,
   task_task.task_id,
   task_task.revision_ord,
   vw_inv_assmbl.filter_inv_no_db_id,
   vw_inv_assmbl.filter_inv_no_id
FROM
   inv_inv
   INNER JOIN inv_inv h_inv_inv ON
      h_inv_inv.inv_no_db_id = inv_inv.h_inv_no_db_id AND
      h_inv_inv.inv_no_id = inv_inv.h_inv_no_id
   CROSS JOIN vw_inv_assmbl
   INNER JOIN task_task ON
      task_task.assmbl_db_id = vw_inv_assmbl.assmbl_db_id AND
      task_task.assmbl_cd = vw_inv_assmbl.assmbl_cd AND
      task_task.assmbl_bom_id = vw_inv_assmbl.assmbl_bom_id
WHERE
   -- use only the specified inventory
   inv_inv.inv_no_db_id = vw_inv_assmbl.filter_inv_no_db_id AND
   inv_inv.inv_no_id    = vw_inv_assmbl.filter_inv_no_id AND
   -- Skip locked inventory
   inv_inv.locked_bool = 0
   AND
   -- Skip Condemned, Archived, and Scrapped inventory
   (h_inv_inv.inv_cond_db_id, h_inv_inv.inv_cond_cd) NOT IN ((0, 'CONDEMN'), (0, 'ARCHIVE'), (0, 'SCRAP'))
   AND
   -- Limit to ACFT, ASSY, SYS, TRK
   inv_inv.inv_class_db_id = 0 AND
   inv_inv.inv_class_cd IN ('ACFT', 'ASSY', 'SYS', 'TRK')
UNION
-- Get all Config Slot based REPL Baseline Tasks against the Inventory or Original Inventory
SELECT
   inv_inv.inv_no_db_id,
   inv_inv.inv_no_id,
   inv_inv.assmbl_inv_no_db_id,
   inv_inv.assmbl_inv_no_id,
   inv_inv.carrier_db_id,
   inv_inv.carrier_id,
   inv_inv.inv_class_db_id,
   inv_inv.inv_class_cd,
   task_task.task_defn_db_id,
   task_task.task_defn_id,
   task_task.task_db_id,
   task_task.task_id,
   task_task.revision_ord,
   vw_inv_assmbl.filter_inv_no_db_id,
   vw_inv_assmbl.filter_inv_no_id
FROM
   inv_inv
   INNER JOIN inv_inv h_inv_inv ON
      h_inv_inv.inv_no_db_id = inv_inv.h_inv_no_db_id AND
      h_inv_inv.inv_no_id = inv_inv.h_inv_no_id
   CROSS JOIN vw_inv_assmbl
   INNER JOIN task_task ON
      task_task.repl_assmbl_db_id = vw_inv_assmbl.assmbl_db_id AND
      task_task.repl_assmbl_cd = vw_inv_assmbl.assmbl_cd AND
      task_task.repl_assmbl_bom_id = vw_inv_assmbl.assmbl_bom_id
WHERE
   -- use only the specified inventory
   inv_inv.inv_no_db_id = vw_inv_assmbl.filter_inv_no_db_id AND
   inv_inv.inv_no_id    = vw_inv_assmbl.filter_inv_no_id AND
   -- Skip locked inventory
   inv_inv.locked_bool   = 0
   AND
   -- Skip Condemned, Archived, and Scrapped inventory
   (h_inv_inv.inv_cond_db_id, h_inv_inv.inv_cond_cd) NOT IN ((0, 'CONDEMN'), (0, 'ARCHIVE'), (0, 'SCRAP'))
   AND
   -- Limit to ACFT, ASSY, SYS, TRK
   inv_inv.inv_class_db_id = 0 AND
   inv_inv.inv_class_cd IN ('ACFT', 'ASSY', 'SYS', 'TRK')
UNION
-- Get all Part No based Baseline Tasks against the Inventory
SELECT
   inv_inv.inv_no_db_id,
   inv_inv.inv_no_id,
   inv_inv.assmbl_inv_no_db_id,
   inv_inv.assmbl_inv_no_id,
   inv_inv.carrier_db_id,
   inv_inv.carrier_id,
   inv_inv.inv_class_db_id,
   inv_inv.inv_class_cd,
   task_task.task_defn_db_id,
   task_task.task_defn_id,
   task_task.task_db_id,
   task_task.task_id,
   task_task.revision_ord,
   inv_inv.inv_no_db_id as filter_inv_no_db_id,
   inv_inv.inv_no_id as filter_inv_no_id
FROM
   inv_inv
   INNER JOIN inv_inv h_inv_inv ON
      h_inv_inv.inv_no_db_id = inv_inv.h_inv_no_db_id AND
      h_inv_inv.inv_no_id = inv_inv.h_inv_no_id
   -- Get all the Task Definitions for the Inventory based on the parts
   INNER JOIN task_part_map ON
      task_part_map.part_no_db_id = inv_inv.part_no_db_id AND
      task_part_map.part_no_id = inv_inv.part_no_id
   INNER JOIN task_task ON
      task_task.task_db_id = task_part_map.task_db_id AND
      task_task.task_id = task_part_map.task_id
WHERE
   -- Skip locked inventory
   inv_inv.locked_bool   = 0
   AND
   -- Skip Condemned, Archived, and Scrapped inventory
   (h_inv_inv.inv_cond_db_id, h_inv_inv.inv_cond_cd) NOT IN ((0, 'CONDEMN'), (0, 'ARCHIVE'), (0, 'SCRAP'))
   AND
   -- Limit to BATCH, SER, TRK, KIT
   inv_inv.inv_class_db_id = 0 AND
   inv_inv.inv_class_cd IN ('BATCH', 'SER', 'TRK', 'KIT')
UNION
-- Select all baseline tasks against a SER inventory.
-- This is the case where the config slot has been converted to SER
SELECT
   inv_inv.inv_no_db_id,
   inv_inv.inv_no_id,
   NULL AS assmbl_inv_no_db_id,
   NULL AS assmbl_inv_no_id,
   inv_inv.carrier_db_id,
   inv_inv.carrier_id,
   inv_inv.inv_class_db_id,
   inv_inv.inv_class_cd,
   task_task.task_defn_db_id,
   latest_task_task.task_defn_id,
   latest_task_task.task_db_id,
   latest_task_task.task_id,
   latest_task_task.revision_ord,
   inv_inv.inv_no_db_id as filter_inv_no_db_id,
   inv_inv.inv_no_id as filter_inv_no_id
FROM
   inv_inv
   INNER JOIN inv_inv h_inv_inv ON
      h_inv_inv.inv_no_db_id = inv_inv.h_inv_no_db_id AND
      h_inv_inv.inv_no_id = inv_inv.h_inv_no_id
   -- get all actual tasks on the inventory
   INNER JOIN sched_stask ON
      sched_stask.main_inv_no_db_id = inv_inv.inv_no_db_id AND
      sched_stask.main_inv_no_id = inv_inv.inv_no_id
   -- actual tasks are based on task definition
   INNER JOIN task_task ON
      task_task.task_db_id = sched_stask.task_db_id AND
      task_task.task_id = sched_stask.task_id
   INNER JOIN task_defn ON
      task_defn.task_defn_db_id = task_task.task_defn_db_id AND
      task_defn.task_defn_id = task_task.task_defn_id
   -- get the latest revision of the task definition
   INNER JOIN task_task latest_task_task ON
      latest_task_task.task_defn_db_id = task_defn.task_defn_db_id AND
      latest_task_task.task_defn_id = task_defn.task_defn_id
      AND
      latest_task_task.revision_ord = task_defn.last_revision_ord
   LEFT OUTER JOIN task_part_map ON
      task_part_map.task_db_id = latest_task_task.task_db_id AND
      task_part_map.task_id = latest_task_task.task_id
WHERE
   -- Skip locked inventory
   inv_inv.locked_bool   = 0
   AND
   -- Limit to SER
   inv_inv.inv_class_db_id = 0 AND
   inv_inv.inv_class_cd = 'SER'
   AND
   -- Skip Condemned, Archived, and Scrapped inventory
   (h_inv_inv.inv_cond_db_id, h_inv_inv.inv_cond_cd) NOT IN ((0, 'CONDEMN'), (0, 'ARCHIVE'), (0, 'SCRAP'))
   AND
   task_part_map.task_db_id IS NULL AND
   task_part_map.task_id IS NULL
;
