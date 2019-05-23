--liquibase formatted sql


--changeSet aopr_order_returns_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_order_returns_v1
AS
SELECT
   Order_number,
   Shipment_barcode,
   order_id,
   Shipment_id
FROM
   acor_order_returns_v1;