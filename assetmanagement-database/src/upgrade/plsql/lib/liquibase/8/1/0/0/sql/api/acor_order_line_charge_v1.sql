--liquibase formatted sql


--changeSet acor_order_line_charge_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_order_line_charge_v1
AS 
SELECT
   po_line.line_no_ord AS line_number,
   charge.charge_code,
   po_line_charge.charge_amount ,
   po_header.alt_id AS order_id
FROM
   po_line_charge
   INNER JOIN charge ON
      po_line_charge.charge_id = charge.charge_id
   INNER JOIN po_line ON
      po_line_charge.po_db_id   = po_line.po_db_id AND
      po_line_charge.po_id      = po_line.po_id AND
      po_line_charge.po_line_id = po_line.po_line_id
   INNER JOIN po_header ON
      po_line.po_db_id = po_header.po_db_id AND
      po_line.po_id    = po_header.po_id;