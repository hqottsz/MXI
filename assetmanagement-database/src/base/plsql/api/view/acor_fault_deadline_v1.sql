--liquibase formatted sql


--changeSet acor_fault_deadline_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_fault_deadline_v1
AS
SELECT
   sd_fault.alt_id                  as fault_id,
   sched_stask.alt_id               AS corr_sched_id,
   evt_sched_dead.sched_dead_dt     AS due_date,
   evt_sched_dead.sched_dead_qt     AS due_usage,
   evt_sched_dead.usage_rem_qt      AS remaining_quantity,
   evt_sched_dead.start_dt          AS start_date,
   evt_sched_dead.start_qt          AS start_quantity,
   evt_sched_dead.notify_qt         AS notification_quantity,
   evt_sched_dead.deviation_qt      AS deviation_quantity,
   evt_sched_dead.sched_from_cd     AS schedule_from_code,
   mim_data_type.data_type_cd       AS data_type_code,
   getExtendedDeadlineDt(
         evt_sched_dead.deviation_qt,
         evt_sched_dead.sched_dead_dt,
         mim_data_type.domain_type_cd,
         mim_data_type.data_type_cd,
         ref_eng_unit.ref_mult_qt
   ) AS extended_due_date
FROM
   evt_sched_dead
   INNER JOIN sched_stask ON
     sched_stask.sched_db_id = evt_sched_dead.event_db_id AND
     sched_stask.sched_id    = evt_sched_dead.event_id
   INNER JOIN mim_data_type ON
      evt_sched_dead.data_type_db_id = mim_data_type.data_type_db_id AND
      evt_sched_dead.data_type_id    = mim_data_type.data_type_id
   INNER JOIN ref_eng_unit ON
      mim_data_type.eng_unit_db_id = ref_eng_unit.eng_unit_db_id AND
      mim_data_type.eng_unit_cd    = ref_eng_unit.eng_unit_cd
   INNER JOIN sd_fault ON
      sd_fault.fault_db_id = sched_stask.fault_db_id AND
      sd_fault.fault_id    = sched_stask.fault_id
;