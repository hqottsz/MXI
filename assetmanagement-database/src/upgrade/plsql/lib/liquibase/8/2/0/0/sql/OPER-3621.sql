--liquibase formatted sql


--changeSet OPER-3621:1 stripComments:false
UPDATE
   ref_event_status
SET
   ref_event_status.user_status_cd = 'ON ORDER'
WHERE
   ref_event_status.event_status_db_id = 0 AND
   ref_event_status.event_status_cd = 'PRONORDER';