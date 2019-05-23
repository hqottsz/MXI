--liquibase formatted sql


--changeSet aopr_acft_actv_ts_block_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_acft_actv_ts_block_v1
AS 
SELECT
   acft_ts_block.sched_id,
   acft_ts_block.block_id,
   acft_ts_block.task_id,
   acft_ts_block.aircraft_id,
   acft_ts_block.task_code,
   acft_ts_block.config_slot_code,
   acft_ts_block.registration_code,
   acft_ts_block.task_class_code,
   acft_ts_block.task_subclass_code,
   acft_ts_block.originator_code,
   --
   --acft_ts_deadline.data_type_db_id,
   acft_ts_deadline.data_type_id,
   acft_ts_deadline.data_type_code,
   acft_ts_deadline.schedule_deadline_date,
   acft_ts_deadline.schedule_deadline_quantity,
   acft_ts_deadline.usage_remaining_quantity,
   acft_ts_deadline.interval_quantity,
   --
   acft_ts_block.barcode,
   acft_ts_block.revision_order
FROM
   acor_acft_ts_block_v1 acft_ts_block
   LEFT JOIN acor_acft_ts_deadline_v1 acft_ts_deadline ON
      acft_ts_block.sched_id    = acft_ts_deadline.sched_id
WHERE
   --event_type_db_id = 0 AND
   event_type_code    = 'TS'
   AND
   --event_status_db_id = 0 AND
   event_status_code    = 'ACTV'
;