--liquibase formatted sql


--changeSet acor_fault_step_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_fault_step_v1
AS 
SELECT
   sched_stask.alt_id             AS sched_id,
   step_ord                       AS step_order,
   REGEXP_REPLACE(REPLACE(step_ldesc,'<br>',CHR(10)),'(\<|\/|u|b|\>)')                      AS step_description,
   ref_step_status.display_name   AS step_status
FROM
   sched_stask
   INNER JOIN sched_step ON
      sched_stask.sched_db_id = sched_step.sched_db_id AND
      sched_stask.sched_id    = sched_step.sched_id
   INNER JOIN ref_step_status ON
      sched_step.step_status_cd = ref_step_status.step_status_cd
;