--liquibase formatted sql


--changeSet DEV-973:1 stripComments:false
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- Removes all events and the reference terms for events
-- of type BN.
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
  -- EVT_INV --
  DELETE FROM (
    SELECT 
      * 
    FROM 
      evt_inv 
      INNER JOIN evt_event ON 
        evt_inv.event_db_id = evt_event.event_db_id AND
        evt_inv.event_id    = evt_event.event_id
    WHERE 
      event_type_db_id = 0 AND 
      event_type_cd    = 'BN'
    );

--changeSet DEV-973:2 stripComments:false
  -- EVT_EVENT --
  DELETE FROM evt_event
  WHERE
   event_type_db_id = '0' AND
   event_type_cd    = 'BN';   

--changeSet DEV-973:3 stripComments:false
  -- REF_EVENT_STATUS --
  DELETE FROM ref_event_status 
  WHERE
    event_type_db_id = '0' AND
    event_type_cd    = 'BN';   

--changeSet DEV-973:4 stripComments:false
  -- REF_EVENT_TYPE --
  DELETE FROM ref_event_type 
  WHERE
    event_type_db_id = '0' AND
    event_type_cd    = 'BN'; 