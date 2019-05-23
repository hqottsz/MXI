--liquibase formatted sql


--changeSet aopr_comp_actv_ts_adhoc_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_comp_actv_ts_adhoc_v1
AS
SELECT
   sched_id,
   barcode,
   event_name,
   event_description,
   part_number,
   schedule_priority_code
FROM
   acor_comp_ts_adhoc_v1
WHERE
   --event_type_db_id = 0 AND
   event_type_code    = 'TS'
   AND
   --event_status_db_id = 0 AND
   event_status_code    = 'ACTV'
;