--liquibase formatted sql


--changeSet MX-16118:1 stripComments:false
-- 
-- MX-16118
-- 
-- There might be existing faults that have entries in EVT_INV_USAGE for incorrect usage parameters.
-- If there is no record for the given usage parameter in INV_CURR_USAGE table but EVT_INV_USAGE table has entries for this usage parameter for the -- same inventory then that means the EVT_INV_USAGE table has bad data caused due to the bug.
-- This migration script is to clean up bad data from EVT_INV_USAGE table. 
DELETE FROM 
   evt_inv_usage
WHERE
   (evt_inv_usage.event_db_id, evt_inv_usage.event_id, evt_inv_usage.event_inv_id, evt_inv_usage.data_type_db_id, evt_inv_usage.data_type_id) IN
   ( 
       SELECT 
          evt_inv_usage.event_db_id,
          evt_inv_usage.event_id, 
          evt_inv_usage.event_inv_id, 
          evt_inv_usage.data_type_db_id, 
          evt_inv_usage.data_type_id       
       FROM 
          evt_inv_usage 
          INNER JOIN evt_inv ON evt_inv.event_db_id = evt_inv_usage.event_db_id AND
                                evt_inv.event_id    = evt_inv_usage.event_id
          INNER JOIN evt_event ON evt_event.event_db_id = evt_inv.event_db_id AND
                                  evt_event.event_id    = evt_inv.event_id                                   
       WHERE
          evt_event.event_type_db_id = 0 AND   
          evt_event.event_type_cd = 'CF' 
          AND NOT EXISTS 
          (   SELECT 
                  1
              FROM inv_curr_usage 
              WHERE 
                  inv_curr_usage.inv_no_db_id = evt_inv.inv_no_db_id AND 
                  inv_curr_usage.inv_no_id    = evt_inv.inv_no_id
                  AND
                  inv_curr_usage.data_type_db_id = evt_inv_usage.data_type_db_id AND
                  inv_curr_usage.data_type_id    = evt_inv_usage.data_type_id
           )           
    )
;    