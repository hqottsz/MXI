--liquibase formatted sql


--changeSet acor_rbl_eng_wp_loc_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_rbl_eng_wp_loc_v1
AS   
SELECT
   inv_loc.alt_id       AS location_id,
   inventory_id, 
   sched_stask.alt_id    AS sched_id,
   assmbl_code, 
   original_assmbl_code, 
   serial_number, 
   manufacturer, 
   part_number, 
   condition_code, 
   operator_id, 
   operator_code,
   inv_loc.loc_cd       AS location_code,
   inv_loc.loc_type_cd  AS location_type_code,
   evt_event.event_dt   AS completed_date
FROM
   acor_rbl_eng_v1 
   INNER JOIN inv_inv ON 
      acor_rbl_eng_v1.inventory_id = inv_inv.alt_id
   INNER JOIN evt_inv ON 
      inv_inv.inv_no_db_id = evt_inv.inv_no_db_id AND
      inv_inv.inv_no_id    = evt_inv.inv_no_id      
   INNER JOIN evt_event ON 
      evt_inv.event_db_id = evt_event.event_db_id AND
      evt_inv.event_id    = evt_event.event_id
      AND
      evt_inv.main_inv_bool = 1
   INNER JOIN sched_stask ON 
      evt_event.event_db_id = sched_stask.sched_db_id AND
      evt_event.event_id    = sched_stask.sched_id
   INNER JOIN evt_loc ON 
      evt_event.event_db_id = evt_loc.event_db_id AND
      evt_event.event_id    = evt_loc.event_id
   INNER JOIN inv_loc ON 
      evt_loc.loc_db_id = inv_loc.loc_db_id AND
      evt_loc.loc_id    = inv_loc.loc_id
;