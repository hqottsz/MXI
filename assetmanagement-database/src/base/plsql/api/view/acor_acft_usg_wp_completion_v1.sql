--liquibase formatted sql


--changeSet acor_acft_usg_wp_completion_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_acft_usg_wp_completion_v1
AS
SELECT
   inv_inv.alt_id              AS aircraft_id,
   sched_stask.alt_id          AS sched_id,
   sched_stask.barcode_sdesc   AS barcode,
   mim_data_type.data_type_cd  AS data_type_code,
   evt_inv_usage.h_tsn_qt AS tsn,
   evt_inv_usage.h_tso_qt AS tso
FROM
   sched_stask
   INNER JOIN evt_event ON
      sched_stask.sched_db_id = evt_event.event_db_id AND
      sched_stask.sched_id    = evt_event.event_id
   INNER JOIN evt_inv ON
      evt_event.event_db_id = evt_inv.event_db_id AND
      evt_event.event_id    = evt_inv.event_id
      AND
      evt_inv.main_inv_bool = 1
   INNER JOIN evt_inv_usage ON
      evt_inv.event_db_id  = evt_inv_usage.event_db_id AND
      evt_inv.event_id     = evt_inv_usage.event_id    AND
      evt_inv.event_inv_id = evt_inv_usage.event_inv_id
   INNER JOIN mim_data_type ON
      evt_inv_usage.data_type_db_id = mim_data_type.data_type_db_id AND
      evt_inv_usage.data_type_id    = mim_data_type.data_type_id
   -- highest id
   INNER JOIN inv_inv ON
      evt_inv.h_inv_no_db_id = inv_inv.inv_no_db_id AND
      evt_inv.h_inv_no_id    = inv_inv.inv_no_id
      AND
      inv_inv.inv_class_cd = 'ACFT'
WHERE
   task_class_cd = 'CHECK'
   AND
   evt_event.event_status_cd = 'COMPLETE'
;