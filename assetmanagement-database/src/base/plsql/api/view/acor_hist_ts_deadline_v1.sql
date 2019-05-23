--liquibase formatted sql


--changeSet acor_hist_ts_deadline_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_hist_ts_deadline_v1
AS
SELECT
   sched_stask.alt_id              AS sched_id,
   inv_inv.alt_id                  AS inventory_id,
   mim_data_type.alt_id            AS data_type_id,
   mim_data_type.data_type_cd      AS data_type_code,
   evt_event.event_dt              AS event_date,
   TRIM(TO_CHAR(evt_sched_dead.sched_dead_qt,'9999999999999999999999' || RTRIM(RPAD('.', entry_prec_qt + 1,'9'),'.')))         AS time_since_new_quantity,
   TRIM(TO_CHAR(evt_sched_dead.sched_dead_qt,'9999999999999999999999' || RTRIM(RPAD('.', entry_prec_qt + 1,'9'),'.')))  AS assmbl_time_since_new_quantity
FROM
   evt_event
   INNER JOIN sched_stask ON
      evt_event.event_db_id = sched_stask.sched_db_id AND
      evt_event.event_id    = sched_stask.sched_id
   INNER JOIN evt_inv ON
      evt_event.event_db_id = evt_inv.event_db_id AND
      evt_event.event_id    = evt_inv.event_id
   INNER JOIN inv_inv ON
      evt_inv.inv_no_db_id = inv_inv.inv_no_db_id AND
      evt_inv.inv_no_id    = inv_inv.inv_no_id
   INNER JOIN evt_sched_dead ON
      evt_inv.event_db_id  = evt_sched_dead.event_db_id AND
      evt_inv.event_id     = evt_sched_dead.event_id
   INNER JOIN mim_data_type ON
      evt_sched_dead.data_type_db_id = mim_data_type.data_type_db_id AND
      evt_sched_dead.data_type_id    = mim_data_type.data_type_id
WHERE
   evt_event.hist_bool = 1
;