--liquibase formatted sql


--changeSet acor_acft_wp_steps_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_acft_wp_steps_v1
AS 
SELECT
   wp_workscope.wo_sched_id,
   wp_workscope.sched_id,
   sched_step.step_ord,
   sched_step.step_ldesc AS step_description,
   ref_step_status.display_name AS step_status
FROM
   acor_acft_wp_workscope_v1 wp_workscope
   INNER JOIN sched_stask ON
      wp_workscope.sched_id = sched_stask.alt_id
   INNER JOIN sched_step ON
      sched_stask.sched_db_id = sched_step.sched_db_id AND
      sched_stask.sched_id    = sched_step.sched_id
   INNER JOIN ref_step_status ON
      sched_step.step_status_cd = ref_step_status.step_status_cd
;