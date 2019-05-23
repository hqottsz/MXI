--liquibase formatted sql


--changeSet acor_order_invoice_lines_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_order_invoice_lines_v1
AS 
SELECT
   po_invoice_line.line_no_ord AS invoice_line_number,
   po_invoice_line.line_ldesc AS description,
   po_invoice_line.invoice_qt AS quantity,
   po_invoice_line.unit_price,
   po_invoice_line.line_price,
   po_invoice.alt_id AS invoice_id,
   po_invoice_line.alt_id AS invoice_line_id
FROM
   PO_INVOICE_LINE
   INNER JOIN po_invoice ON
      po_invoice_line.po_invoice_db_id = po_invoice.po_invoice_db_id AND
      po_invoice_line.po_invoice_id    = po_invoice.po_invoice_id;