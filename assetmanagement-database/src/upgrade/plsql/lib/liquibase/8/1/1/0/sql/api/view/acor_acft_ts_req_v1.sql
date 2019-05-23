--liquibase formatted sql


--changeSet acor_acft_ts_req_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_acft_ts_req_v1
AS 
SELECT
   sched_stask.alt_id                 AS sched_id,
   aircraft.aircraft_id,
   aircraft.registration_code,
   req_task.task_id,
   req_task.task_code,
   req_ata.config_slot_code,
   req_task.task_class_code,
   req_task.task_subclass_code,
   req_task.user_subclass_code,
   req_task.originator_code,
   sched_stask.barcode_sdesc          AS barcode,
   req_task.priority_code,
   req_task.revision_order,
   --
   evt_event.event_dt                 AS event_date,
   evt_event.event_type_cd            AS event_type_code,
   evt_event.event_status_cd          AS event_status_code,
   evt_event.hist_bool                AS historical_flag
FROM
   sched_stask
   -- requirement info
   INNER JOIN task_task ON
      sched_stask.task_db_id = task_task.task_db_id AND
      sched_stask.task_id    = task_task.task_id
   INNER JOIN acor_req_defn_v1 req_task ON
      task_task.alt_id    = req_task.task_id
   -- assembly
   LEFT JOIN acor_req_ata_v1 req_ata ON
      req_task.task_id = req_ata.task_id
   INNER JOIN evt_event ON
      sched_stask.sched_db_id = evt_event.event_db_id AND
      sched_stask.sched_id    = evt_Event.event_id
   -- aircraft
   INNER JOIN inv_inv ON
      sched_stask.main_inv_no_db_id = inv_inv.inv_no_db_id AND
      sched_stask.main_inv_no_id    = inv_inv.inv_no_id
   INNER JOIN inv_inv h_inv_inv ON
      inv_inv.h_inv_no_db_id = h_inv_inv.inv_no_db_id AND
      inv_inv.h_inv_no_id    = h_inv_inv.inv_no_id
   LEFT JOIN acor_inv_aircraft_v1 aircraft ON
      h_inv_inv.alt_id    = aircraft.aircraft_id
WHERE
   inv_inv.inv_class_db_id = 0 AND
   inv_inv.inv_class_cd IN ('ACFT','SYS','ASSY')
;