--liquibase formatted sql


--changeSet acor_order_filled_requests_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_order_filled_requests_v1
AS
SELECT
   po_line.LINE_NO_ORD AS Line_number,
   req_part.alt_id AS part_request_id,
   evt_event.event_sdesc AS Part_request_barcode,
   po_header.alt_id AS order_id
FROM
   po_line
   INNER JOIN req_part ON
      req_part.po_db_id = po_line.po_db_id AND
      req_part.po_id    = po_line.po_id AND
      req_part.po_line_id = po_line.po_line_id
   INNER JOIN evt_event ON
      req_part.req_part_db_id = evt_event.event_db_id AND
      req_part.req_part_id    = evt_event.event_id
   INNER JOIN  po_header ON
      po_line.PO_DB_ID = po_header.po_db_id AND
      po_line.po_id    = po_header.po_id;