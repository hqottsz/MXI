--liquibase formatted sql


--changeSet acor_comp_ts_adhoc_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_comp_ts_adhoc_v1
AS 
SELECT
   sched_stask.alt_id              AS sched_id,
   inv_inv.alt_id                  AS inventory_id,
   sched_stask.barcode_sdesc       AS barcode,
   evt_event.event_sdesc           AS event_name,
   evt_event.event_ldesc           AS event_description,
   part.part_no_oem                AS part_number,
   evt_event.sched_priority_cd     AS schedule_priority_code,
   --
   evt_event.event_dt              AS event_date,
   evt_event.event_type_cd         AS event_type_code,
   evt_event.event_status_cd       AS event_status_code,
   evt_event.hist_bool             AS historical_flag
FROM
   sched_stask
   -- requirement to block map
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
   AND
   task_class_db_id = 0 AND
   task_class_cd    = 'ADHOC'
;