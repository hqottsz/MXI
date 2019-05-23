--liquibase formatted sql


--changeSet aopr_comp_hist_ts_req_ldone_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_comp_hist_ts_req_ldone_v1
AS
SELECT
   comp_ts_req.sched_id,
   comp_ts_req.inventory_id,
   task_id,
   task_code,
   config_slot_code,
   part_number,
   oem_serial_number,
   task_class_code,
   task_subclass_code,
   user_subclass_code,
   -- last done
   hist_ts_deadline.event_date,
   hist_ts_deadline.data_type_id,
   hist_ts_deadline.data_type_code,
   hist_ts_deadline.time_since_new_quantity,
   hist_ts_deadline.assmbl_time_since_new_quantity
   --
   barcode_sdesc,
   priority_code
FROM
   acor_comp_ts_req_v1 comp_ts_req
   INNER JOIN acor_hist_ts_deadline_v1 hist_ts_deadline ON
      comp_ts_req.sched_id    = hist_ts_deadline.sched_id
WHERE
   (
     comp_ts_req.inventory_id,
     task_id,
     comp_ts_req.event_date
   ) IN (
           SELECT
               inventory_id,
               task_id,
               MAX(event_date) event_date
           FROM
              acor_comp_ts_req_v1
           WHERE
              --event_type_db_id = 0 AND
              event_type_code    = 'TS'
              AND
              --event_status_db_id = 0 AND
              event_status_code    = 'COMPLETE'
           GROUP BY
               inventory_id,
               task_id
         )
;