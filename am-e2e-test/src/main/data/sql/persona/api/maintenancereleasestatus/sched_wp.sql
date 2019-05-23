/**************************************************
********* UPDATE SCRIPT FOR TABLE SCHED_WP ********
***************************************************/
-- update the sched_wp record's release_to_service_bool
UPDATE
   sched_wp
SET
   release_to_service_bool = 1
WHERE 
   (
      sched_db_id,
	  sched_id
   ) IN 
   (
      SELECT 
	     sched_db_id,
		 sched_id
      FROM 
	     sched_wp,
		 evt_event
      WHERE 
	     sched_wp.sched_db_id = evt_event.event_db_id AND
         sched_wp.sched_id = evt_event.event_id AND
         evt_event.event_sdesc = 'Maintenance Release Status WKP'
   );