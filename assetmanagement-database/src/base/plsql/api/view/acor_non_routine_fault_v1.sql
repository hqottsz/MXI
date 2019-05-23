--liquibase formatted sql


--changeSet acor_non_routine_fault_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_non_routine_fault_v1
AS
WITH
rvw_sched
AS
  (
     SELECT
        alt_id,
        h_sched_db_id,
        h_sched_id,
        sched_db_id,
        sched_id,
        fault_db_id,
        fault_id,
        task_db_id,
        task_id,
        main_inv_no_db_id,
        main_inv_no_id,
        task_class_cd,
        wo_ref_sdesc,
        barcode_sdesc
     FROM
        sched_stask
  ),
rvw_event
AS
  (
     SELECT
        event_db_id,
        event_id,
        nh_event_db_id,
        nh_event_id,
        h_event_db_id,
        h_event_id,
        --
        event_type_cd,
        event_status_cd,
        event_dt,
        actual_start_dt,
        doc_ref_sdesc,
        event_sdesc,
        event_ldesc
     FROM
        evt_event
  ),
rvw_task
AS
  (
     SELECT
        alt_id,
        task_db_id,
        task_id,
        task_defn_db_id,
        task_defn_id,
        task_task.task_cd,
        task_task.task_name,
        task_def_status_cd
     FROM
        task_task
  )
SELECT
   sd_fault.alt_id                  AS fault_id,
   corr_task.alt_id                 AS sched_id,
   workpackage.alt_id               AS workpackage_id,
   found_on_task.alt_id             AS found_on_sched_id,
   found_on_task.barcode_sdesc      AS found_on_barcode,
   evt_event.event_sdesc            AS fault_name,
   eqp_assmbl_bom.assmbl_bom_cd     AS config_slot_code,
   eqp_assmbl_bom.assmbl_bom_name   AS config_slot_name,
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
   END                              AS deferral_reason,
   sd_fault.fault_log_type_cd       AS fault_log_type,
   corr_task.barcode_sdesc          AS barcode,
   acft_inv.alt_id                  AS aircraft_id,
   inv_ac_reg.ac_reg_cd             AS registration_code,
   utl_user.username                AS found_by_hr,
   --
   jic_task.alt_id                  AS jic_task_id,
   jic_task.task_cd                 AS jic_code,
   jic_task.task_name               AS jic_name,
   req_sched.alt_id                 AS req_sched_id,
   req_sched.barcode_sdesc          AS req_barcode,
   req_task.alt_id                  AS req_task_id,
   req_task.task_cd                 AS req_code,
   req_task.task_name               AS req_name,
   block_sched.alt_id               AS block_sched_id,
   block_sched.barcode_sdesc        AS block_barcode,
   block_task.alt_id                AS block_task_id,
   block_task.task_cd               AS block_code,
   block_task.task_name             AS block_name,
   -- display fault barcode if there is at least 1 removal
   NVL((
     SELECT
        1
     FROM
        sched_rmvd_part
     WHERE
       sched_db_id = corr_task.sched_db_id AND
       sched_id    = corr_task.sched_id
       AND
       ROWNUM = 1
       ),0) removal_bool,
   workpackage.barcode_sdesc        AS wp_barcode,
   workpackage.wo_ref_sdesc         AS work_order,
   corr_task_event.doc_ref_sdesc    AS doc_reference
FROM
   sd_fault
   -- found date
   INNER JOIN rvw_event evt_event on
      sd_fault.fault_db_id = evt_event.event_db_id AND
      sd_fault.fault_id    = evt_event.event_id
   INNER JOIN rvw_sched corr_task ON
      sd_fault.fault_db_id = corr_task.fault_db_id AND
      sd_fault.fault_id    = corr_task.fault_id
   --found by user
   INNER JOIN org_hr ON
      org_hr.hr_db_id = sd_fault.found_by_hr_db_id AND
      org_hr.hr_id    = sd_fault.found_by_hr_id
   INNER JOIN utl_user ON
      utl_user.user_id = org_hr.user_id
   -- aircraft
   INNER JOIN inv_inv ON
      corr_task.main_inv_no_db_id = inv_inv.inv_no_db_id AND
      corr_task.main_inv_no_id    = inv_inv.inv_no_id
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
      corr_task.sched_db_id = corr_task_event.event_db_id AND
      corr_task.sched_id    = corr_task_event.event_id
   -- work package
   LEFT JOIN rvw_sched workpackage ON
      corr_task_event.h_event_db_id = workpackage.sched_db_id AND
      corr_task_event.h_event_id    = workpackage.sched_id
   -- found during task, note non-routine fault must be raised during a task
   INNER JOIN evt_event_rel ON
      evt_event.event_db_id = evt_event_rel.rel_event_db_id AND
      evt_event.event_id    = evt_event_rel.rel_event_id
      AND
      rel_type_cd = 'DISCF'
   INNER JOIN rvw_sched found_on_task ON
      evt_event_rel.event_db_id = found_on_task.sched_db_id AND
      evt_event_rel.event_id    = found_on_task.sched_id
   LEFT JOIN rvw_event found_on_event ON
      evt_event_rel.event_db_id = found_on_event.event_db_id AND
      evt_event_rel.event_id    = found_on_event.event_id
      -- jic baseline
   LEFT JOIN rvw_task jic_task ON
      found_on_task.task_db_id = jic_task.task_db_id AND
      found_on_task.task_id    = jic_task.task_id
      -- req baseline
   LEFT JOIN rvw_sched req_sched ON
      found_on_event.nh_event_db_id = req_sched.sched_db_id AND
      found_on_event.nh_event_id    = req_sched.sched_id
      AND
      req_sched.task_class_cd NOT IN ('BLOCK','CHECK','RO')
   LEFT JOIN rvw_task req_task ON
      req_sched.task_db_id = req_task.task_db_id AND
      req_sched.task_id    = req_task.task_id
      -- block
   LEFT JOIN rvw_event req_event ON
      req_sched.sched_db_id = req_event.event_db_id AND
      req_sched.sched_id    = req_event.event_id
   LEFT JOIN rvw_sched block_sched ON
      req_event.nh_event_db_id = block_sched.sched_db_id AND
      req_event.nh_event_id    = block_sched.sched_id
      AND
      block_sched.task_class_cd = 'BLOCK'
      -- block baseline code
   LEFT JOIN rvw_task block_task ON
      block_sched.task_db_id = block_task.task_db_id AND
      block_sched.task_id    = block_task.task_id
   -- severity
   INNER JOIN ref_fail_sev ON
      sd_fault.fail_sev_db_id = ref_fail_sev.fail_sev_db_id AND
      sd_fault.fail_sev_cd    = ref_fail_sev.fail_sev_cd
  -- deferral description
   LEFT JOIN fail_defer_ref ON
      sd_fault.fail_defer_db_id = fail_defer_ref.fail_defer_db_id AND
      sd_fault.fail_defer_cd    = fail_defer_ref.fail_defer_cd
      AND -- filter with assembly as well
      fail_defer_ref.assmbl_db_id = inv_inv.assmbl_db_id AND
      fail_defer_ref.Assmbl_cd    = inv_inv.Assmbl_cd
      AND
      sd_fault.defer_ref_sdesc  = fail_defer_ref.defer_ref_sdesc
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
   inv_inv.inv_class_db_id = 0 AND
   inv_inv.inv_class_cd    = 'SYS'
;