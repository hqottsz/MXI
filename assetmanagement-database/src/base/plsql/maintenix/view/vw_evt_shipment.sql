--liquibase formatted sql


--changeSet vw_evt_shipment:1 stripComments:false
CREATE OR REPLACE VIEW vw_evt_shipment (
   shipment_db_id,
   shipment_id,
   shipment_sdesc,
   bitmap_db_id,
   bitmap_tag,
   bitmap_name,
   event_status_db_id,
   event_status_cd,
   event_status_user_cd,
   shipment_type_db_id,
   shipment_type_cd,
   waybill_sdesc,
   ship_from_loc_db_id,
   ship_from_loc_id,
   ship_from_loc_cd,
   ship_to_loc_db_id,
   ship_to_loc_id,
   ship_to_loc_cd,
   shipment_dt,
   receipt_dt )
AS
SELECT
   ship_shipment.shipment_db_id,
   ship_shipment.shipment_id,
   evt_event.event_sdesc,
   evt_event.bitmap_db_id,
   evt_event.bitmap_tag,
   ref_bitmap.bitmap_name,
   evt_event.event_status_db_id,
   evt_event.event_status_cd,
   ref_event_status.user_status_cd,
   ship_shipment.shipment_type_db_id,
   ship_shipment.shipment_type_cd,
   ship_shipment.waybill_sdesc,
   ship_from_evt_loc.loc_db_id,
   ship_from_evt_loc.loc_id,
   ship_from_inv_loc.loc_cd,
   ship_to_evt_loc.loc_db_id,
   ship_to_evt_loc.loc_id,
   ship_to_inv_loc.loc_cd,
   evt_event.actual_start_gdt,
   evt_event.event_gdt
FROM
   evt_event,
   ship_shipment,
   evt_loc ship_from_evt_loc,
   evt_loc ship_to_evt_loc,
   inv_loc ship_from_inv_loc,
   inv_loc ship_to_inv_loc,
   ref_event_status,
   ref_bitmap
WHERE
   evt_event.event_db_id = ship_shipment.shipment_db_id AND
   evt_event.event_id = ship_shipment.shipment_id AND
   ship_from_evt_loc.event_db_id (+)= evt_event.event_db_id AND
   ship_from_evt_loc.event_id (+)= evt_event.event_id AND
   ship_from_evt_loc.event_loc_id (+)= 1 AND
   ship_from_inv_loc.loc_db_id (+)= ship_from_evt_loc.loc_db_id AND
   ship_from_inv_loc.loc_id (+)= ship_from_evt_loc.loc_id AND
   ship_to_evt_loc.event_db_id (+)= evt_event.event_db_id AND
   ship_to_evt_loc.event_id (+)= evt_event.event_id AND
   ship_to_evt_loc.event_loc_id (+)= 2 AND
   ship_to_inv_loc.loc_db_id (+)= ship_to_evt_loc.loc_db_id AND
   ship_to_inv_loc.loc_id (+)= ship_to_evt_loc.loc_id AND
   ref_event_status.event_status_db_id = evt_event.event_status_db_id AND
   ref_event_status.event_status_cd = evt_event.event_status_cd AND
   ref_bitmap.bitmap_db_id (+)= evt_event.bitmap_db_id AND
   ref_bitmap.bitmap_tag (+)= evt_event.bitmap_tag;