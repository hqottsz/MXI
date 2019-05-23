--liquibase formatted sql


--changeSet acor_order_returns_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_order_returns_v1 
AS
-- Use distinct because we need to use po line to get shipment information, but a po may have multiple
-- po lines that meet the requirement.
SELECT DISTINCT
   evt_event.event_sdesc AS Order_number,
   shipment_event.event_sdesc AS Shipment_barcode,
   po_header.alt_id AS order_id,
   ship_shipment.alt_id AS Shipment_id
FROM
   po_line
   LEFT JOIN ship_shipment ON
      ship_shipment.shipment_db_id = po_line.xchg_shipment_db_id AND
      ship_shipment.shipment_id    = po_line.xchg_shipment_id
   LEFT JOIN evt_event shipment_event ON
      shipment_event.event_db_id = ship_shipment.shipment_db_id AND
      shipment_event.event_id    = ship_shipment.shipment_id
   INNER JOIN po_header ON
      po_line.po_db_id = po_header.po_db_id AND
      po_line.po_id    = po_header.po_id
   INNER JOIN evt_event ON
      po_header.po_db_id = evt_event.event_db_id AND
      po_header.po_id    = evt_event.event_id
WHERE
   po_line.po_line_type_cd IN ('EXCHANGE','BORROW')
   AND
   po_line.deleted_bool = 0
   AND
   (
      ship_shipment.shipment_type_cd IS NULL
      OR
      ship_shipment.shipment_type_cd = 'SENDXCHG'
   );

--changeSet acor_order_returns_v1:2 stripComments:false
COMMENT ON TABLE acor_order_returns_v1 IS 'This view contains information related to po and the return shipment.'
;   