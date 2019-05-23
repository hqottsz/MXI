--liquibase formatted sql


--changeSet acor_fl_faults_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_fl_faults_v1
AS 
SELECT
   fl_leg.leg_id,
   sd_fault.alt_id                  AS fault_id,
   sched_stask.alt_id               AS corr_sched_id,
   evt_event.event_sdesc            AS fault_name,
   eqp_assmbl_bom.assmbl_bom_cd     AS config_slot_code,
   REGEXP_REPLACE(REPLACE(evt_event.event_ldesc,'<br>',CHR(10)),'(\<|\/|u|b|\>)')   AS fault_description,
   sd_fault.op_restriction_ldesc    AS operational_restrictions,
   evt_event.event_type_cd          AS event_type_code,
   evt_event.event_status_cd        AS fault_status_code,
   corr_task_event.event_status_cd  AS corr_task_status_code,
   evt_event.actual_start_dt        AS found_on_date,
   evt_event.doc_ref_sdesc          AS logbook_reference,
   sd_fault.fault_source_cd         AS fault_source,
   ref_fail_sev.sev_type_cd         AS fault_severity,
   sd_fault.fail_defer_cd           AS deferral_class,
   sd_fault.defer_ref_sdesc         AS deferral_reference,
   sd_fault.defer_cd_sdesc          AS deferral_authorization,
   sd_fault.fault_log_type_cd       AS fault_log_type,
   --evt_sched_dead.sched_dead_dt     AS due_date,
   --evt_sched_dead.sched_dead_qt     AS due_usage,
   --evt_sched_dead.usage_rem_qt      AS remaining_quantity,
   --mim_data_type.data_type_cd       AS data_type_code,
   sched_stask.barcode_sdesc        AS barcode,
   acft_inv.alt_id                  AS aircraft_id,
   inv_ac_reg.ac_reg_cd             AS registration_code,
   fl_leg.leg_no                    AS flight_number
FROM
   sd_fault
   -- found date
   INNER JOIN evt_event on
      sd_fault.fault_db_id = evt_event.event_db_id AND
      sd_fault.fault_id    = evt_event.event_id
   INNER JOIN sched_stask ON
      evt_event.event_db_id = sched_stask.fault_db_id AND
      evt_event.event_id    = sched_stask.fault_id
     -- aircraft
   INNER JOIN inv_inv ON
      sched_stask.main_inv_no_db_id = inv_inv.inv_no_db_id AND
      sched_stask.main_inv_no_id    = inv_inv.inv_no_id
   INNER JOIN inv_ac_reg ON
      inv_inv.h_inv_no_db_id = inv_ac_reg.inv_no_db_id AND
      inv_inv.h_inv_no_id    = inv_ac_reg.inv_no_id
   INNER JOIN inv_inv acft_inv ON
      inv_ac_reg.inv_no_db_id = acft_inv.inv_no_db_id AND
      inv_ac_reg.inv_no_id    = acft_inv.inv_no_id
   -- config slot
   INNER JOIN eqp_assmbl_bom ON
      inv_inv.assmbl_db_id = eqp_assmbl_bom.assmbl_db_id AND
      inv_inv.assmbl_cd     = eqp_assmbl_bom.assmbl_cd    AND
      inv_inv.assmbl_bom_id = eqp_assmbl_bom.assmbl_bom_id
   -- corrective task
   INNER JOIN evt_event corr_task_event ON
      sched_stask.sched_db_id = corr_task_event.event_db_id AND
      sched_stask.sched_id    = corr_task_event.event_id
   -- severity
   INNER JOIN ref_fail_sev ON
      sd_fault.fail_sev_db_id = ref_fail_sev.fail_sev_db_id AND
      sd_fault.fail_sev_cd    = ref_fail_sev.fail_sev_cd
   -- scheduling
   --LEFT JOIN evt_sched_dead ON
    --  sched_stask.sched_db_id = evt_sched_dead.event_db_id AND
   --   sched_stask.sched_id    = evt_sched_dead.event_id
  -- LEFT JOIN mim_data_type ON
  --    evt_sched_dead.data_type_db_id = mim_data_type.data_type_db_id AND
  --    evt_sched_dead.data_type_id    = mim_data_type.data_type_id
   -- flight
   INNER JOIN fl_leg ON
      sd_fault.leg_id = fl_leg.leg_id
WHERE
   -- corrective task MUST NOT be linked to a task
   corr_task_event.nh_event_id IS NULL
;