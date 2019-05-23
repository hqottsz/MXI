--liquibase formatted sql


--changeSet acor_order_line_tax_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_order_line_tax_v1
AS 
SELECT
   po_line.line_no_ord AS line_number,
   tax.tax_code,
   po_line_tax.tax_rate,
   po_header.alt_id AS order_id
FROM
   po_line_tax
   INNER JOIN tax ON
      po_line_tax.tax_id = tax.tax_id
   INNER JOIN po_line ON
      po_line_tax.po_db_id = po_line.po_db_id AND
      po_line_tax.po_id    = po_line.po_id AND
      po_line_tax.po_line_id = po_line.po_line_id
   INNER JOIN po_header ON
      po_line.po_db_id = po_header.po_db_id AND
      po_line.po_id    = po_header.po_id;