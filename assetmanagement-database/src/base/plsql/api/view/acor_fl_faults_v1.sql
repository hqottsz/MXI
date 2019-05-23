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
   fail_defer_ref.defer_ref_ldesc   AS deferral_description,
   CASE evt_event.event_status_cd
     WHEN 'CFDEFER' THEN
         defer_stage.stage_reason_cd
   END                             AS deferral_reason,
   sd_fault.fault_log_type_cd       AS fault_log_type,
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
      sd_fault.fault_db_id = sched_stask.fault_db_id AND
      sd_fault.fault_id    = sched_stask.fault_id
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
   -- flight
   INNER JOIN fl_leg ON
      sd_fault.leg_id = fl_leg.leg_id
   -- deferral description
   LEFT JOIN fail_defer_ref ON
      sd_fault.fail_defer_db_id = fail_defer_ref.fail_defer_db_id AND
      sd_fault.fail_defer_cd    = fail_defer_ref.fail_defer_cd
      AND -- filter with assembly as well
      fail_defer_ref.assmbl_db_id = inv_inv.assmbl_db_id AND
      fail_defer_ref.Assmbl_cd    = inv_inv.Assmbl_cd
      AND
      sd_fault.defer_ref_sdesc   = fail_defer_ref.defer_ref_sdesc
   -- defferal reason
   LEFT JOIN (
               SELECT
                  evt_stage.event_db_id,
                  evt_stage.event_id,
                  stage_reason_cd
               FROM
                  evt_stage
                  INNER JOIN (
                              SELECT
                                 event_db_id,
                                 event_id,
                                 MAX(stage_id) stage_id
                              FROM
                                 evt_stage
                              WHERE
                                 event_status_db_id = 0 AND
                                 event_status_cd = 'CFDEFER'
                              GROUP BY
                                 event_db_id,
                                 event_id
                             ) max_stage ON
                     evt_stage.event_db_id = max_stage.event_db_id AND
                     evt_stage.event_id    = max_stage.event_id    AND
                     evt_stage.stage_id    = max_stage.stage_id
             ) defer_stage ON
      sd_fault.fault_db_id = defer_stage.event_db_id AND
      sd_fault.fault_id    = defer_stage.event_id
WHERE
   -- corrective task MUST NOT be linked to a task
   corr_task_event.nh_event_id IS NULL
;