--liquibase formatted sql


--changeSet MX-28952:1 stripComments:false
-- Remove all flight status codes from REF_EVENT_STATUS and child records from REF_STAGE_REASON.  
-- These now reside in REF_FLIGHT_LEG_STATUS.  
-- There is no need to migrate data referencing these status codes from evt_event as 
-- this was done in an earlier migration script (see basesql8000MX-27658.sql)
DELETE FROM 
       ref_stage_reason
WHERE
   event_status_db_id = 0 AND
   event_status_cd    IN ('FLPLAN', 'FLOUT', 'FLOFF', 'DIVERT', 'FLON', 'FLIN', 'FLCMPLT', 'FLDELAY', 'FLCANCEL', 'FLERR', 'FLEDIT', 'RETURN', 'FLBLKOUT')
;

--changeSet MX-28952:2 stripComments:false
DELETE FROM
   ref_event_status
WHERE
   event_status_db_id = 0 AND
   event_status_cd    IN ('FLPLAN', 'FLOUT', 'FLOFF', 'DIVERT', 'FLON', 'FLIN', 'FLCMPLT', 'FLDELAY', 'FLCANCEL', 'FLERR', 'FLEDIT', 'RETURN', 'FLBLKOUT')
;