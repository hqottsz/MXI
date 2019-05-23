--liquibase formatted sql


--changeSet acor_non_routine_fault_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_non_routine_fault_v1
AS 
SELECT
   sd_fault.alt_id                  AS fault_id,
   sched_stask.alt_id               AS sched_id,
   workpackage.alt_id               AS workpackage_id,
   found_on_task.alt_id             AS found_on_sched_id,
   found_on_task.barcode_sdesc      AS found_on_barcode,
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
   sched_stask.barcode_sdesc        AS barcode,
   acft_inv.alt_id                  AS aircraft_id,
   inv_ac_reg.ac_reg_cd             AS registration_code,
   utl_user.username                AS found_by_hr
FROM
   sd_fault
   -- found date
   INNER JOIN evt_event on
      sd_fault.fault_db_id = evt_event.event_db_id AND
      sd_fault.fault_id    = evt_event.event_id
   INNER JOIN sched_stask ON
      evt_event.event_db_id = sched_stask.fault_db_id AND
      evt_event.event_id    = sched_stask.fault_id
   --found by user
   INNER JOIN org_hr ON
      org_hr.hr_db_id = sd_fault.found_by_hr_db_id AND
      org_hr.hr_id    = sd_fault.found_by_hr_id
   INNER JOIN utl_user ON
      utl_user.user_id = org_hr.user_id
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
   -- work package
   LEFT JOIN sched_stask workpackage ON
      corr_task_event.h_event_db_id = workpackage.sched_db_id AND
      corr_task_event.h_event_id    = workpackage.sched_id
   -- found during task, note non-routine fault must be raised during a task
   INNER JOIN evt_event_rel ON
      evt_event.event_db_id = evt_event_rel.rel_event_db_id AND
      evt_event.event_id    = evt_event_rel.rel_event_id
      AND
      rel_type_cd = 'DISCF'
   INNER JOIN sched_stask found_on_task ON
      evt_event_rel.event_db_id = found_on_task.sched_db_id AND
      evt_event_rel.event_id    = found_on_task.sched_id
   -- severity
   INNER JOIN ref_fail_sev ON
      sd_fault.fail_sev_db_id = ref_fail_sev.fail_sev_db_id AND
      sd_fault.fail_sev_cd    = ref_fail_sev.fail_sev_cd
WHERE
   inv_inv.inv_class_db_id = 0 AND
   inv_inv.inv_class_cd    = 'SYS'
;