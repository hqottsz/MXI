--liquibase formatted sql


--changeSet OPER-8045_2:1 stripComments:false
-- Set the actual_start_dtgdt as the creation_dt for all orders. 
-- It is used to save the real creation date of the order. The creation_dt
-- is an audit column to record the time when the row is inserted to the database.
UPDATE
   evt_event
SET
   actual_start_dt  = creation_dt,
   actual_start_gdt = creation_dt
WHERE
   rstat_cd = 0
   AND
   ( event_db_id, event_id )
   IN
   ( SELECT po_db_id, po_id FROM po_header );