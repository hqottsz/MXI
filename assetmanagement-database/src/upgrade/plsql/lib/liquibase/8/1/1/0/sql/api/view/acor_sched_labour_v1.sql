--liquibase formatted sql


--changeSet acor_sched_labour_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_sched_labour_v1
AS 
SELECT
   sched_stask.alt_id            AS sched_stask_id,
   sched_labour.labour_stage_cd,
   sched_labour.labour_skill_cd,
   sched_labour.work_perf_bool,
   sched_labour.cert_bool,
   sched_labour.insp_bool,
   labour_role.sched_hr,
   labour_role.actual_hr,
   labour_role.labour_role_type_cd,
   labour_role.labour_time_cd,
   labour_role.adjusted_billing_hr,
   labour_role.labour_cost,
   labour_role.actual_start_dt,
   labour_role.actual_end_dt,
   role_type.desc_sdesc         AS labour_role
FROM
   sched_labour
   INNER JOIN sched_stask ON
      sched_stask.sched_db_id = sched_labour.sched_db_id AND
      sched_stask.sched_id    = sched_labour.sched_id
   INNER JOIN sched_labour_role labour_role ON
      labour_role.labour_db_id = sched_labour.labour_db_id AND
      labour_role.labour_id    = sched_labour.labour_id
   INNER JOIN ref_labour_role_type role_type ON
      role_type.labour_role_type_db_id = labour_role.labour_role_type_db_id AND
      role_type.labour_role_type_cd    = labour_role.labour_role_type_cd
;