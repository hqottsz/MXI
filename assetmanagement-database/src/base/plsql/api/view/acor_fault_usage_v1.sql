--liquibase formatted sql


--changeSet acor_fault_usage_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_fault_usage_v1
AS
SELECT
       sched_stask.alt_id AS corr_task_id,
       sched_stask.barcode_sdesc AS corr_task_barcode,
       mim_data_type.data_type_cd,
       evt_inv_usage.tsn_qt,
       evt_inv_usage.tso_qt,
       evt_inv_usage.tsi_qt,
       evt_inv_usage.assmbl_tsn_qt,
       evt_inv_usage.assmbl_tso_qt
  FROM
       sched_stask
    INNER JOIN evt_event ON
      evt_event.event_db_id =sched_stask.sched_db_id AND
      evt_event.event_id =sched_stask.sched_id
    INNER JOIN evt_inv_usage ON
      evt_event.event_db_id = evt_inv_usage.event_db_id AND
      evt_event.event_id = evt_inv_usage.event_id
    INNER JOIN mim_data_type ON
      mim_data_type.data_type_db_id = evt_inv_usage.data_type_db_id AND
      mim_data_type.data_type_id  = evt_inv_usage.data_type_id
;