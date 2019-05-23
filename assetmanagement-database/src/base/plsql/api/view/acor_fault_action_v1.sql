--liquibase formatted sql


--changeSet acor_fault_action_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_fault_action_v1
AS
SELECT
   sched_stask.alt_id             AS sched_id,
   first_name || ' ' || last_name AS action_by,
   action_ldesc                   AS action_description,
   action_dt                      AS action_date,
   cancel_bool                    AS cancel_flag
FROM
   sd_fault
   INNER JOIN sched_stask ON
      sd_fault.fault_db_id = sched_stask.fault_db_id AND
      sd_fault.fault_id    = sched_stask.fault_id
   INNER JOIN sched_action ON
      sched_stask.sched_db_id = sched_action.sched_db_id AND
      sched_stask.sched_id    = sched_action.sched_id
   INNER JOIN org_hr ON
      sched_action.hr_db_id = org_hr.hr_db_id AND
      sched_action.hr_id    = org_hr.hr_id
   INNER JOIN utl_user ON
      org_hr.user_id = utl_user.user_id
;