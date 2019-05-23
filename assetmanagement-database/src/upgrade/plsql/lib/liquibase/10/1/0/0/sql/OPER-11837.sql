--liquibase formatted sql 
 
--changeSet OPER-11837 Making an Edit to a Shipment's Details after it has been Completed, results in the Received Date becoming Null
-- Update evt_event table event_dt null records with connected last completed shipment with completed date

UPDATE 
   evt_event
SET 
   (
      event_dt,
	  event_gdt
   ) = 
   (
      SELECT 
         ship_segment.complete_dt,
		 ship_segment.complete_dt
      FROM ship_segment
      JOIN ship_segment_map ON       
         ship_segment.segment_db_id = ship_segment_map.segment_db_id AND 
         ship_segment.segment_id = ship_segment_map.segment_id
      WHERE
         ship_segment_map.shipment_db_id = evt_event.event_db_id AND
         ship_segment_map.shipment_id = evt_event.event_id 
      AND 
         ship_segment_map.segment_ord = 
         (
            SELECT 
               MAX(ship_segment_map.segment_ord) 
            FROM ship_segment_map 
            JOIN ship_segment ON
               ship_segment.segment_db_id = ship_segment_map.segment_db_id AND 
               ship_segment.segment_id = ship_segment_map.segment_id
            WHERE
               ship_segment_map.shipment_db_id = evt_event.event_db_id AND
               ship_segment_map.shipment_id = evt_event.event_id 
            AND            
               ship_segment.segment_status_db_id = 0 AND
               ship_segment.segment_status_cd = 'CMPLT'
         )
      AND 
         ship_segment.complete_dt IS NOT NULL
   )
WHERE 
   evt_event.event_status_db_id = 0 AND
   evt_event.event_status_cd = 'IXCMPLT' AND
   evt_event.event_dt IS NULL;