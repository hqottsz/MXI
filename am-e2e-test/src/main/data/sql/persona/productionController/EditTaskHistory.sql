-- Update the Task to set its status to COMPLETE
UPDATE
   evt_event
SET
   event_status_cd = 'COMPLETE',
   actual_start_dt = SYSDATE,
   actual_start_gdt = SYSDATE
WHERE 
   event_sdesc IN ('BM_SCH_TSK19 (BM_SCH_TSK19)', 'BM_SCH_TSK20 (BM_SCH_TSK20)');