--liquibase formatted sql


--changeSet acor_order_invoice_mapping_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_order_invoice_mapping_v1
AS
SELECT
   po_header.alt_id AS order_id,
   po_invoice.alt_id AS invoice_id,
   map.po_line_id AS order_line_number,
   map.po_invoice_line_id AS invoice_line_number
FROM
   po_invoice_line_map map
   INNER JOIN po_header ON
      po_header.po_db_id = map.po_db_id AND
      po_header.po_id    = map.po_id
   INNER JOIN po_invoice ON
      po_invoice.po_invoice_db_id = map.po_invoice_db_id AND
      po_invoice.po_invoice_id    = map.po_invoice_id;