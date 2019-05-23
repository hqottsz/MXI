--liquibase formatted sql


--changeSet aopr_acft_actv_ts_adhoc_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_acft_actv_ts_adhoc_v1
AS 
SELECT
   sched_id,
   aircraft_id,
   barcode,
   event_name,
   event_description,
   registration_code,
   assembly_code,
   schedule_priority_code,
   etops_flag
FROM
   acor_acft_ts_adhoc_v1
WHERE
   --event_type_db_id = 0 AND
   event_type_code    = 'TS'
   AND
   --event_status_db_id = 0 AND
   event_status_code    = 'ACTV'
;