--liquibase formatted sql


--changeSet aopr_order_invoice_mapping_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_order_invoice_mapping_v1
AS
SELECT
   "ORDER_ID","INVOICE_ID","ORDER_LINE_NUMBER","INVOICE_LINE_NUMBER"
FROM
   acor_order_invoice_mapping_v1;