--liquibase formatted sql


--changeSet aopr_order_invoice_lines_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_order_invoice_lines_v1
AS
SELECT
   invoice_line_number,
   description,
   quantity,
   unit_price,
   line_price,
   invoice_id
FROM
   acor_order_invoice_lines_v1;