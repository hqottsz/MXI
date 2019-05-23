-- Update the work packages to set its status to IN WORK
UPDATE
   evt_event
SET
   event_status_cd = 'IN WORK',
   evt_event.actual_start_dt = SYSDATE,
   evt_event.actual_start_gdt = SYSDATE
WHERE
   event_db_id = 4650 AND
   event_id IN (SELECT event_id FROM evt_event WHERE event_sdesc IN ('WP24838-01'));

-- Commit Work Scope and turn off the flag of Collected   
INSERT INTO sched_wo_line (
   wo_sched_db_id, 
   wo_sched_id, 
   wo_line_id, 
   tally_line_ord, 
   sched_db_id, 
   sched_id, 
   unassign_bool, 
   collected_bool,
   workscope_order)
VALUES (
   4650,
   (SELECT event_id FROM evt_event WHERE event_sdesc = 'WP24838-01'),
   1,
   1,
   4650,
   (SELECT sched_id FROM sched_stask WHERE task_id = (SELECT task_id FROM task_task WHERE task_cd IN ('BM_SCH_TSK19'))),
   0,
   0,
   1);

-- Commit Work Scope and turn on the flag of Collected   
INSERT INTO sched_wo_line (
   wo_sched_db_id, 
   wo_sched_id, 
   wo_line_id, 
   tally_line_ord, 
   sched_db_id, 
   sched_id, 
   unassign_bool, 
   collected_bool,
   collection_hr_db_id,
   collection_hr_id,
   collection_dt,
   workscope_order)
VALUES (
   4650,
   (SELECT event_id FROM evt_event WHERE event_sdesc = 'WP24838-01'),
   2,
   2,
   4650,
   (SELECT sched_id FROM sched_stask WHERE task_id = (SELECT task_id FROM task_task WHERE task_cd IN ('BM_SCH_TSK20'))),
   0,
   1,
   (SELECT oh.hr_db_id FROM org_hr oh INNER JOIN utl_user uu ON oh.user_id = uu.user_id WHERE uu.username = 'mxi'),
   (SELECT oh.hr_id FROM org_hr oh INNER JOIN utl_user uu ON oh.user_id = uu.user_id WHERE uu.username = 'mxi'),
   CURRENT_DATE,
   1);