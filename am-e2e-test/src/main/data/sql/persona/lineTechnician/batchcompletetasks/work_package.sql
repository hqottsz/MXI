-- Update the work package LT-BCS-WP to set its status to IN WORK
UPDATE
   evt_event
SET
   event_status_cd = 'IN WORK',
   evt_event.actual_start_dt = SYSDATE,
   evt_event.actual_start_gdt = SYSDATE
WHERE
   event_db_id = 4650 AND
   event_id = (SELECT event_id FROM evt_event WHERE event_sdesc = 'LT-BCS-WP');

-- Update the work package LT-BCA-WP to set its status to IN WORK
UPDATE
   evt_event
SET
   event_status_cd = 'IN WORK',
   evt_event.actual_start_dt = SYSDATE,
   evt_event.actual_start_gdt = SYSDATE
WHERE
   event_db_id = 4650 AND
   event_id = (SELECT event_id FROM evt_event WHERE event_sdesc = 'LT-BCA-WP');