--liquibase formatted sql


--changeSet acor_logbook_fault_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_logbook_fault_v1
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
  ),
rvw_fault
AS
   (
     SELECT
        sd_fault.alt_id,
        fault_db_id,
        fault_id,
        leg_id,
        flight_stage_db_id,
        flight_stage_cd,
        fail_sev_db_id,
        fail_sev_cd,
        found_by_hr_db_id,
        found_by_hr_id,
        fail_defer_db_id,
        fail_defer_cd,
        --
        op_restriction_ldesc,
        defer_ref_sdesc,
        defer_cd_sdesc,
        fault_log_type_cd,
        fault_source_db_id,
        fault_source_cd
     FROM
       sd_fault
       INNER JOIN evt_event nr_fault ON
          sd_fault.fault_db_id = nr_fault.event_db_id AND
          sd_fault.fault_id    = nr_fault.event_id
     WHERE
         nr_fault.doc_ref_sdesc IS NULL
         -- found during task is null
         AND NOT EXISTS
          (
            SELECT 1
             FROM
                 evt_event_rel
             WHERE
                 nr_fault.event_db_id     = evt_event_rel.rel_event_db_id AND
                 nr_fault.event_id        = evt_event_rel.rel_event_id AND
                 evt_event_rel.rel_type_cd = 'DISCF'
          )
     UNION ALL
     SELECT
        sd_fault.alt_id,
        fault_db_id,
        fault_id,
        leg_id,
        flight_stage_db_id,
        flight_stage_cd,
        fail_sev_db_id,
        fail_sev_cd,
        found_by_hr_db_id,
        found_by_hr_id,
        fail_defer_db_id,
        fail_defer_cd,
        --
        op_restriction_ldesc,
        defer_ref_sdesc,
        defer_cd_sdesc,
        fault_log_type_cd,
        fault_source_db_id,
        fault_source_cd
     FROM
       sd_fault
       INNER JOIN evt_event r_fault ON
          sd_fault.fault_db_id = r_fault.event_db_id AND
          sd_fault.fault_id    = r_fault.event_id
     WHERE
         r_fault.doc_ref_sdesc IS NOT NULL
   ),
rvw_labour
AS
  (
    SELECT
       sched_db_id,
       sched_id,
       labour_role_type_cd,
       LISTAGG(labor_hr_name, ' | ') WITHIN GROUP ( ORDER BY labor_hr_name) labour_name
    FROM
       (
          SELECT
             sched_labour.sched_db_id,
             sched_labour.sched_id,
             sched_labour_role.labour_role_type_cd,
             CASE
                WHEN sched_labour_role_status.hr_id IS NOT NULL THEN
                   utl_user.last_name|| ', ' || utl_user.first_name
             END labor_hr_name,
             ROW_NUMBER() OVER (PARTITION BY sched_labour.sched_id, sched_labour_role.labour_role_type_cd, org_hr.user_id ORDER BY utl_user.last_name) rn
          FROM
             sched_labour
             INNER JOIN sched_labour_role ON
                sched_labour.labour_db_id = sched_labour_role.labour_db_id AND
                sched_labour.labour_id    = sched_labour_role.labour_id
             INNER JOIN sched_labour_role_status ON
                sched_labour_role.labour_role_db_id = sched_labour_role_status.labour_role_db_id AND
                sched_labour_role.labour_role_id    = sched_labour_role_status.labour_role_id
             LEFT JOIN org_hr ON
                sched_labour_role_status.hr_db_id = org_hr.hr_db_id AND
                sched_labour_role_status.hr_id    = org_hr.hr_id
             LEFT JOIN utl_user ON
                org_hr.user_id = utl_user.user_id
       )
    WHERE
       rn = 1
    GROUP BY
       sched_db_id,
       sched_id,
       labour_role_type_cd
  ),
rvw_airport
AS
   (
     SELECT
        loc_db_id,
        loc_id,
        loc_cd
     FROM
        inv_loc
     WHERE
        loc_type_cd = 'AIRPORT'
   )
SELECT
   rvw_fault.alt_id                 AS fault_id,
   corr_task.alt_id                 AS sched_id,
   workpackage.alt_id               AS workpackage_id,
   acft_inv.alt_id                  AS aircraft_id,
   fl_leg.leg_id,
   evt_event.event_sdesc            AS fault_name,
   eqp_assmbl_bom.assmbl_bom_cd     AS config_slot_code,
   eqp_assmbl_bom.Assmbl_Bom_Name   AS config_slot_name,
   REGEXP_REPLACE(REPLACE(evt_event.event_ldesc,'<br>',CHR(10)),'(\<|\/|u|b|\>)')   AS fault_description,
   rvw_fault.op_restriction_ldesc   AS operational_restrictions,
   evt_event.event_type_cd          AS event_type_code,
   evt_event.event_status_cd        AS fault_status_code,
   corr_event.event_status_cd       AS corr_task_status_code,
   corr_event.event_dt              AS closed_date,
   evt_event.actual_start_dt        AS found_on_date,
   evt_event.doc_ref_sdesc          AS logbook_reference,
   rvw_fault.fault_source_cd        AS fault_source,
   ref_fault_source.spec2k_fault_source_cd AS spec2k_fault_source,
   ref_fail_sev.sev_type_cd         AS fault_severity,
   rvw_fault.fail_defer_cd          AS deferral_class,
   rvw_fault.defer_ref_sdesc        AS deferral_reference,
   rvw_fault.defer_cd_sdesc         AS deferral_authorization,
   fail_defer_ref.defer_ref_ldesc   AS deferral_description,
   CASE evt_event.event_status_cd
     WHEN 'CFDEFER' THEN
         defer_stage.stage_reason_cd
   END                              AS deferral_reason,
   rvw_fault.fault_log_type_cd      AS fault_log_type,
   ref_flight_stage.flight_stage_cd AS flight_stage_code,
   ref_flight_stage.desc_sdesc      AS flight_stage_name,
   inv_ac_reg.ac_reg_cd             AS registration_code,
   fl_leg.leg_no                    AS flight_number,
   departure_loc.loc_cd             AS departure_airport,
   arrival_loc.loc_cd               AS arrival_airport,
   utl_user.username                AS found_by_hr,
   -- task
   corr_task.barcode_sdesc          AS barcode,
   found_on_task.alt_id             AS jic_sched_id,
   found_on_task.barcode_sdesc      AS jic_barcode,
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
   corr_event.doc_ref_sdesc         AS doc_reference,
   -- labour
   technician.labour_name           AS mechanic,
   certifier.labour_name            AS certifier,
   inspector.labour_name            AS inspector
FROM
   rvw_fault
   -- found date
   INNER JOIN rvw_event evt_event on
      rvw_fault.fault_db_id = evt_event.event_db_id AND
      rvw_fault.fault_id    = evt_event.event_id
   -- corrective task
   INNER JOIN rvw_sched corr_task ON
      rvw_fault.fault_db_id = corr_task.fault_db_id AND
      rvw_fault.fault_id    = corr_task.fault_id
   INNER JOIN rvw_event corr_event ON
      corr_task.sched_db_id = corr_event.event_db_id AND
      corr_task.sched_id    = corr_event.event_id
    -- work package
   INNER JOIN rvw_sched workpackage ON
      corr_event.h_event_db_id = workpackage.sched_db_id AND
      corr_event.h_event_id    = workpackage.sched_id
   -- jic routine task
   LEFT JOIN evt_event_rel ON
      rvw_fault.fault_db_id = evt_event_rel.rel_event_db_id AND
      rvw_fault.fault_id    = evt_event_rel.rel_event_id
      AND
      rel_type_cd = 'DISCF'
   LEFT JOIN rvw_sched found_on_task ON
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
   --found by user
   LEFT JOIN org_hr ON
      org_hr.hr_db_id = rvw_fault.found_by_hr_db_id AND
      org_hr.hr_id    = rvw_fault.found_by_hr_id
   LEFT JOIN utl_user ON
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
   -- severity
   INNER JOIN ref_fail_sev ON
      rvw_fault.fail_sev_db_id = ref_fail_sev.fail_sev_db_id AND
      rvw_fault.fail_sev_cd    = ref_fail_sev.fail_sev_cd
   -- fault source spec2000
   INNER JOIN ref_fault_source ON
      rvw_fault.fault_source_db_id = ref_fault_source.fault_source_db_id AND
      rvw_fault.fault_source_cd    = ref_fault_source.fault_source_cd
   -- flight
   LEFT JOIN fl_leg ON
      rvw_fault.leg_id = fl_leg.leg_id
   -- arrival and departure airport
   LEFT JOIN rvw_airport departure_loc ON
      fl_leg.departure_loc_db_id = departure_loc.loc_db_id AND
      fl_leg.departure_loc_id    = departure_loc.loc_id
   LEFT JOIN rvw_airport arrival_loc ON
      fl_leg.arrival_loc_db_id = arrival_loc.loc_db_id AND
      fl_leg.arrival_loc_id    = arrival_loc.loc_id
   -- flight stage
   LEFT JOIN ref_flight_stage ON
      rvw_fault.flight_stage_db_id = ref_flight_stage.flight_stage_db_id AND
      rvw_fault.flight_stage_cd    = ref_flight_stage.flight_stage_cd
   -- deferral description
   LEFT JOIN fail_defer_ref ON
      rvw_fault.fail_defer_db_id = fail_defer_ref.fail_defer_db_id AND
      rvw_fault.fail_defer_cd    = fail_defer_ref.fail_defer_cd
      AND -- filter with assembly as well
      fail_defer_ref.assmbl_db_id = inv_inv.assmbl_db_id AND
      fail_defer_ref.Assmbl_cd    = inv_inv.Assmbl_cd
      AND
      rvw_fault.defer_ref_sdesc   = fail_defer_ref.defer_ref_sdesc
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
      rvw_fault.fault_db_id = defer_stage.event_db_id AND
      rvw_fault.fault_id    = defer_stage.event_id
   -- labour
      -- technician
   LEFT JOIN rvw_labour technician ON
      corr_task.sched_db_id = technician.sched_db_id AND
      corr_task.sched_id    = technician.sched_id
      AND
      technician.labour_role_type_cd = 'TECH'
      -- certifier
   LEFT JOIN rvw_labour certifier ON
      corr_task.sched_db_id = certifier.sched_db_id AND
      corr_task.sched_id    = certifier.sched_id
      AND
      certifier.labour_role_type_cd = 'CERT'
      -- inspector
   LEFT JOIN rvw_labour inspector ON
      corr_task.sched_db_id = inspector.sched_db_id AND
      corr_task.sched_id    = inspector.sched_id
      AND
      inspector.labour_role_type_cd = 'INSP'
;   