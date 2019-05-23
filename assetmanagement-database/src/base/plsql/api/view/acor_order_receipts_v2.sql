--liquibase formatted sql


--changeSet acor_order_receipts_v2:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_order_receipts_v2
AS
SELECT DISTINCT
   evt_event.event_sdesc AS order_number,
   shipment_event.event_sdesc AS shipment_barcode,
   po_header.alt_id AS order_id,
   inbound_shipment_lines.alt_id AS shipment_id
FROM
   po_line
   LEFT JOIN (
      SELECT
         ship_shipment.shipment_db_id,
         ship_shipment.shipment_id,
         ship_shipment_line.po_db_id,
         ship_shipment_line.po_id,
         ship_shipment_line.po_line_id,
         ship_shipment.alt_id
      FROM
         ship_shipment
         INNER JOIN ship_shipment_line ON
            ship_shipment_line.shipment_db_id = ship_shipment.shipment_db_id AND
            ship_shipment_line.shipment_id    = ship_shipment.shipment_id
         WHERE
            (ship_shipment.shipment_type_db_id, ship_shipment.shipment_type_cd) IN ( (0,'PURCHASE'), (0,'REPAIR'), (0,'RTNVEN'))
   ) inbound_shipment_lines ON
      inbound_shipment_lines.po_db_id   = po_line.po_db_id AND
      inbound_shipment_lines.po_id      = po_line.po_id AND
      inbound_shipment_lines.po_line_id = po_line.po_line_id
   -- shipment event
   LEFT JOIN evt_event shipment_event ON
      shipment_event.event_db_id = inbound_shipment_lines.shipment_db_id AND
      shipment_event.event_id    = inbound_shipment_lines.shipment_id
   LEFT JOIN po_header ON
      po_header.po_db_id = po_line.po_db_id AND
      po_header.po_id    = po_line.po_id
   INNER JOIN evt_event ON
      po_header.po_db_id = evt_event.event_db_id AND
      po_header.po_id    = evt_event.event_id
WHERE
   po_line.deleted_bool = 0
   AND
   -- has to be a part line or a repair order line
   (  po_line.part_no_id IS NOT NULL
      OR
      po_line.sched_db_id IS NOT NULL
   );

--changeSet acor_order_receipts_v2:2 stripComments:false
COMMENT ON TABLE acor_order_receipts_v2 IS 'This view contains information related to orders being received.'
;