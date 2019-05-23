--liquibase formatted sql


--changeSet aopr_order_line_tax_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_order_line_tax_v1
AS 
SELECT
   line_number,
   tax_code,
   tax_rate,
   order_id
FROM
   acor_order_line_tax_v1;