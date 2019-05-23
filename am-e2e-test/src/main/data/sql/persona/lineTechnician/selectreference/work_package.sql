-- Update the work package RTD-WP to set its status to IN WORK
UPDATE
   evt_event
SET
   event_status_cd = 'IN WORK',
   evt_event.actual_start_dt = SYSDATE,
   evt_event.actual_start_gdt = SYSDATE
WHERE
   event_db_id = 4650 AND
   event_id = (SELECT event_id FROM evt_event WHERE event_sdesc = 'RTD-WP');

-- Update the work package RTD-WP-2 to set its status to IN WORK
UPDATE
   evt_event
SET
   event_status_cd = 'IN WORK',
   evt_event.actual_start_dt = SYSDATE,
   evt_event.actual_start_gdt = SYSDATE
WHERE
   event_db_id = 4650 AND
   event_id = (SELECT event_id FROM evt_event WHERE event_sdesc = 'RTD-WP-2');

-- Update the work package RTD-WP-3 to set its status to IN WORK
UPDATE
   evt_event
SET
   event_status_cd = 'IN WORK',
   evt_event.actual_start_dt = SYSDATE,
   evt_event.actual_start_gdt = SYSDATE
WHERE
   event_db_id = 4650 AND
   event_id = (SELECT event_id FROM evt_event WHERE event_sdesc = 'RTD-WP-3');

-- Update the work package SREF-WP to set its status to IN WORK
UPDATE
   evt_event
SET
   event_status_cd = 'IN WORK',
   evt_event.actual_start_dt = SYSDATE,
   evt_event.actual_start_gdt = SYSDATE
WHERE
   event_db_id = 4650 AND
   event_id = (SELECT event_id FROM evt_event WHERE event_sdesc = 'SREF-WP');