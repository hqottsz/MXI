--liquibase formatted sql


--changeSet aopr_comp_ts_adhoc_ldone_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_comp_ts_adhoc_ldone_v1
AS
SELECT
   comp_adhoc.sched_id,
   comp_adhoc.inventory_id,
   barcode,
   event_name,
   event_description,
   part_number,
   schedule_priority_code,
   --event_type_db_id,
   event_type_code,
   --event_status_db_id,
   event_status_code,
   historical_flag,
   --  last done
   hist_deadline.event_date,
   --hist_deadline.data_type_db_id,
   hist_deadline.data_type_id,
   hist_deadline.data_type_code,
   hist_deadline.time_since_new_quantity,
   hist_deadline.assmbl_time_since_new_quantity
FROM
   acor_comp_ts_adhoc_v1 comp_adhoc
   INNER JOIN acor_hist_ts_deadline_v1 hist_deadline ON
      comp_adhoc.sched_id    = hist_deadline.sched_id
WHERE
   (
     comp_adhoc.inventory_id,
     comp_adhoc.event_date
   ) IN (
           SELECT
               inventory_id,
               MAX(event_date) event_date
           FROM
              acor_comp_ts_adhoc_v1
           WHERE
              --event_type_db_id = 0 AND
              event_type_code    = 'TS'
              AND
              --event_status_db_id = 0 AND
              event_status_code    = 'COMPLETE'
           GROUP BY
               inventory_id
         )
;