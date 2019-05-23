--liquibase formatted sql


--changeSet acor_assy_wp_labor_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_assy_wp_labor_v1
AS 
WITH
rvw_labour_role_stat
AS
  (
    SELECT
       sched_labour_role.labour_db_id,
       sched_labour_role.labour_id,
       sched_labour_role_status.status_db_id,
       sched_labour_role_status.status_id,
       sched_labour_role.labour_role_type_cd,
       sched_labour_role.actual_start_dt,
       sched_labour_role.actual_end_dt,
       sched_labour_role_status.labour_role_status_cd,
       CASE
         WHEN sched_labour_role_status.hr_id IS NOT NULL THEN
            utl_user.last_name|| ', ' || utl_user.first_name
       END labor_hr_name,
       utl_user.username,
       sched_labour_role.sched_hr,
       sched_labour_role.actual_hr,
       sched_labour_role.adjusted_billing_hr,
       sched_labour_role.labour_cost,
       sched_labour_role.labour_time_cd
    FROM
       sched_labour_role
       INNER JOIN sched_labour_role_status ON
          sched_labour_role.labour_role_db_id = sched_labour_role_status.labour_role_db_id AND
          sched_labour_role.labour_role_id    = sched_labour_role_status.labour_role_id
       LEFT JOIN org_hr ON
          sched_labour_role_status.hr_db_id = org_hr.hr_db_id AND
          sched_labour_role_status.hr_id    = org_hr.hr_id
       LEFT JOIN utl_user ON
          org_hr.user_id = utl_user.user_id
  )
SELECT
    wp_workscope.wo_sched_id,
    wp_workscope.sched_id,
    sched_labour.labour_skill_cd      AS labour_skill_code,
    technician.username               AS mx_username,
    technician.labor_hr_name          AS technician_name,
    technician.actual_start_dt        AS actual_start_date,
    technician.actual_end_dt          AS actual_end_date,
    sched_labour.labour_stage_cd      AS labour_stage_status,
    technician.labour_role_status_cd  AS labour_role_status,
    sched_labour.work_perf_bool       AS work_perform_flag,
    sched_labour.cert_bool            AS certify_flag,
    sched_labour.insp_bool            AS inspect_flag,
    certifier.labor_hr_name           AS certifier_name,
    inspector.labor_hr_name           AS inspector_name,
    technician.sched_hr               AS scheduled_hours,
    technician.actual_hr              AS actual_hours,
    technician.adjusted_billing_hr    AS adjusted_billing_hours,
    technician.labour_cost            AS labour_cost,
    technician.labour_time_cd         AS labour_time_code
FROM
   acor_assy_wp_workscope_v1 wp_workscope
   INNER JOIN sched_stask ON
      wp_workscope.sched_id = sched_stask.alt_id
   INNER JOIN sched_labour ON
      sched_stask.sched_db_id = sched_labour.sched_db_id AND
      sched_stask.sched_id    = sched_labour.sched_id
   -- technician
   LEFT JOIN rvw_labour_role_stat technician ON
      sched_labour.labour_db_id = technician.labour_db_id AND
      sched_labour.labour_id    = technician.labour_id
      AND
      technician.labour_role_type_cd = 'TECH'
   -- certifier
   LEFT JOIN rvw_labour_role_stat certifier ON
      technician.labour_db_id = certifier.labour_db_id AND
      technician.labour_id    = certifier.labour_id
      AND
      certifier.labour_role_type_cd = 'CERT'
   -- inspector
   LEFT JOIN rvw_labour_role_stat inspector ON
      technician.labour_db_id = inspector.labour_db_id AND
      technician.labour_id    = inspector.labour_id
      AND
      inspector.labour_role_type_cd = 'INSP'
;