--liquibase formatted sql


--changeSet aopr_acft_ts_adhoc_ldone_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_acft_ts_adhoc_ldone_v1
AS
SELECT
   acft_ts_adhoc.sched_id,
   aircraft_id,
   barcode,
   event_name,
   event_description,
   registration_code,
   schedule_priority_code,
   hist_ts_deadline.event_date,
   hist_ts_deadline.data_type_id,
   hist_ts_deadline.data_type_code,
   hist_ts_deadline.time_since_new_quantity,
   hist_ts_deadline.assmbl_time_since_new_quantity
FROM
   acor_acft_ts_adhoc_v1 acft_ts_adhoc
   INNER JOIN acor_hist_ts_deadline_v1 hist_ts_deadline ON
      acft_ts_adhoc.sched_id    = hist_ts_deadline.sched_id
WHERE
   (
     aircraft_id,
     task_id,
     acft_ts_adhoc.event_date
   ) IN (
           SELECT
               aircraft_id,
               task_id,
               MAX(event_date) event_date
           FROM
              acor_acft_ts_adhoc_v1
           WHERE
              --event_type_db_id = 0 AND
              event_type_code    = 'TS'
              AND
              --event_status_db_id = 0 AND
              event_status_code    = 'COMPLETE'
           GROUP BY
              aircraft_id,
              task_id
         )
;