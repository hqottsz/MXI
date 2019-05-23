--liquibase formatted sql


--changeSet vw_drv_deadline:1 stripComments:false
/**
 * This view provides a table containing all of the driving 
 * deadlines and supplementery data for all non-historic tasks.
 */
CREATE OR REPLACE VIEW vw_drv_deadline
AS
SELECT
   evt_sched_dead.event_db_id,
   evt_sched_dead.event_id,
   evt_sched_dead.data_type_db_id,
   evt_sched_dead.data_type_id,
   evt_sched_dead.deviation_qt,
   evt_sched_dead.usage_rem_qt,
   evt_sched_dead.sched_dead_dt,
   mim_data_type.domain_type_db_id,
   mim_data_type.domain_type_cd,
   mim_data_type.data_type_cd,
   mim_data_type.entry_prec_qt,
   ref_eng_unit.eng_unit_db_id,
   ref_eng_unit.eng_unit_cd,
   ref_eng_unit.ref_mult_qt
FROM
   evt_sched_dead
   INNER JOIN mim_data_type ON
      mim_data_type.data_type_db_id = evt_sched_dead.data_type_db_id AND
      mim_data_type.data_type_id    = evt_sched_dead.data_type_id
   INNER JOIN ref_eng_unit ON
      ref_eng_unit.eng_unit_db_id = mim_data_type.eng_unit_db_id AND
      ref_eng_unit.eng_unit_cd    = mim_data_type.eng_unit_cd
WHERE
   evt_sched_dead.hist_bool_ro = 0
   AND
   evt_sched_dead.sched_driver_bool = 1
;