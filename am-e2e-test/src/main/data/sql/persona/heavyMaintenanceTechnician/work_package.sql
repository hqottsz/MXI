-- Update the work packages to set its status to IN WORK
UPDATE
   evt_event
SET
   event_status_cd = 'IN WORK',
   evt_event.actual_start_dt = SYSDATE,
   evt_event.actual_start_gdt = SYSDATE
WHERE
   event_db_id = 4650 AND
   event_id IN (SELECT event_id FROM evt_event WHERE event_sdesc IN ('WP20988-01','WP20989-01'));