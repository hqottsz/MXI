--liquibase formatted sql


--changeSet aopr_comp_actv_ts_req_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_comp_actv_ts_req_v1
AS 
SELECT
   comp_req.sched_id,
   comp_req.inventory_id,
   task_id,
   task_code,
   config_slot_code,
   block_task_code,
   oem_serial_number,
   part_number,
   comp_req.task_class_code,
   task_subclass_code,
   originator_code,
   --
   comp_ts_deadline.data_type_id,
   comp_ts_deadline.data_type_code,
   comp_ts_deadline.extended_deadline_date,
   comp_ts_deadline.schedule_deadline_date,
   comp_ts_deadline.extended_deadline_quantity,
   comp_ts_deadline.schedule_deadline_quantity,
   comp_ts_deadline.usage_remaining_quantity,
   comp_ts_deadline.interval_quantity,
   --
   barcode,
   priority_code,
   revision_order
FROM
   acor_comp_ts_req_v1 comp_req
   LEFT JOIN acor_comp_ts_deadline_v1 comp_ts_deadline ON
      comp_req.sched_id    = comp_ts_deadline.sched_id
WHERE
   --event_type_db_id = 0 AND
   event_type_code    = 'TS'
   AND
   --event_status_db_id = 0 AND
   event_status_code    = 'ACTV'
;