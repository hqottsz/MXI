--liquibase formatted sql


--changeSet aopr_acft_actv_ts_req_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_acft_actv_ts_req_v1
AS 
SELECT
   acft_ts_req.sched_id,
   aircraft_id,
   task_id,
   task_code,
   config_slot_code,
   registration_code,
   acft_ts_req.task_class_code,
   task_subclass_code,
   originator_code,
   user_subclass_code,
   -- next due
   acft_ts_deadline.data_type_id,
   acft_ts_deadline.data_type_code,
   acft_ts_deadline.extended_deadline_date,
   acft_ts_deadline.schedule_deadline_date,
   acft_ts_deadline.extended_deadline_quantity,
   acft_ts_deadline.schedule_deadline_quantity,
   acft_ts_deadline.usage_remaining_quantity,
   acft_ts_deadline.interval_quantity,
   --
   barcode,
   priority_code,
   revision_order,
   event_status_code
FROM
   acor_acft_ts_req_v1 acft_ts_req
   LEFT JOIN acor_acft_ts_deadline_v1 acft_ts_deadline ON
      acft_ts_req.sched_id    = acft_ts_deadline.sched_id
WHERE
   --event_type_db_id = 0 AND
   event_type_code    = 'TS'
   AND
   --event_status_db_id = 0 AND
   event_status_code    = 'ACTV'
;