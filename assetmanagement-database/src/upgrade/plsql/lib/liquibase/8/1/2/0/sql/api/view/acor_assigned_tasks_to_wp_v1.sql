--liquibase formatted sql


--changeSet acor_assigned_tasks_to_wp_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_assigned_tasks_to_wp_v1 
AS
SELECT 
   evt_event.event_sdesc,
   evt_event.event_ldesc,
   sched_stask.barcode_sdesc,
   sched_stask.task_class_cd,
   evt_event.event_status_cd,
   sched_stask.task_priority_cd,
   evt_event.sched_start_dt,
   evt_event.sched_end_dt,
   evt_event.actual_start_dt,
   evt_event.event_dt AS actual_end_date,
   sd_fault.fail_sev_cd,
   --workpackage key
   evt_event.h_event_db_id,
   evt_event.h_event_id
FROM
   sched_stask
   INNER JOIN evt_event ON
      evt_event.event_db_id = sched_stask.sched_db_id AND
      evt_event.event_id = sched_stask.sched_id    
   LEFT OUTER JOIN sd_fault ON
      sd_fault.fault_db_id = sched_stask.fault_db_id AND
      sd_fault.fault_id = sched_stask.fault_id
WHERE
   --only faults, block, req and ad-hoc
   (sched_stask.task_class_db_id, sched_stask.task_class_cd) IN ((0, 'ADHOC'), (0, 'REQ'), (0, 'CORR'), (10, 'BLOCK'), (0, 'DISCARD'), (0, 'MOD'), (0, 'OVHL'), (0, 'REPL'))
   AND
   --only parent tasks
   sched_stask.sched_db_id = sched_stask.h_sched_db_id AND
   sched_stask.sched_id = sched_stask.h_sched_id 
;