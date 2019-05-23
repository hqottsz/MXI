--liquibase formatted sql


--changeSet OPER-7943:1 stripComments:false
-- sets the note in EVT_IETM to NULL on all active and forecast tasks if not already.
-- this will allow to see the ietms point to the ietm definition and will get the updated notes from the ietm definition until the task gets started or become historic.
UPDATE 
   evt_ietm
SET
   evt_ietm.ietm_note = NULL
WHERE
   evt_ietm.ietm_note IS NOT NULL
   AND
   ( evt_ietm.event_db_id, evt_ietm.event_id ) IN
   (
      SELECT
         DISTINCT evt_ietm.event_db_id, evt_ietm.event_id
      FROM
         sched_stask
      INNER JOIN evt_event ON
         evt_event.event_db_id = sched_stask.sched_db_id AND
         evt_event.event_id    = sched_stask.sched_id
      INNER JOIN evt_ietm ON
         evt_ietm.event_db_id = evt_event.event_db_id AND
         evt_ietm.event_id    = evt_event.event_id
      WHERE
         evt_event.event_status_cd IN ('ACTV', 'FORECAST') AND
         evt_event.event_type_cd = 'TS'
         AND
         evt_ietm.ietm_note IS NOT NULL);