--liquibase formatted sql


--changeSet acor_comp_ts_req_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_comp_ts_req_v1
AS
SELECT
   sched_stask.alt_id                  AS sched_id,
   inv_inv.alt_id                      AS inventory_id,
   req_task.task_id,
   req_task.task_code,
   req_ata.config_slot_code,
   block_req_map.block_task_code,
   inv_inv.serial_no_oem                AS oem_serial_number,
   part.alt_id                          AS part_id,
   part.part_no_oem                     AS part_number,
   req_task.task_class_code,
   req_task.task_subclass_code,
   req_task.originator_code,
   req_task.user_subclass_code,
   sched_stask.barcode_sdesc            AS barcode,
   req_task.priority_code,
   req_task.revision_order,
   --
   evt_event.event_dt                   AS event_date,
   evt_event.event_type_cd              AS event_type_code,
   evt_event.event_status_cd            AS event_status_code,
   evt_event.hist_bool                  AS historical_flag
FROM
   sched_stask
   -- requirement info
   INNER JOIN task_task ON
      sched_stask.task_db_id = task_task.task_db_id AND
      sched_stask.task_id    = task_task.task_id
   INNER JOIN acor_req_defn_v1 req_task ON
      task_task.alt_id = req_task.task_id
   -- assembly
   LEFT JOIN acor_req_ata_v1 req_ata  ON
      req_task.task_id = req_ata.task_id
   -- requirement to block map
   LEFT JOIN acor_block_req_map_v1 block_req_map ON
      req_ata.task_id = block_req_map.req_id
   INNER JOIN evt_event ON
      sched_stask.sched_db_id = evt_event.event_db_id AND
      sched_stask.sched_id    = evt_Event.event_id
   -- inventory
   INNER JOIN inv_inv ON
      sched_stask.main_inv_no_db_id = inv_inv.inv_no_db_id AND
      sched_stask.main_inv_no_id    = inv_inv.inv_no_id
   INNER JOIN eqp_part_no part ON
      inv_inv.part_no_db_id = part.part_no_db_id AND
      inv_inv.part_no_id    = part.part_no_id
WHERE
   inv_inv.inv_class_db_id = 0 AND
   inv_inv.inv_class_cd    IN ('TRK','SER', 'ASSY')
;