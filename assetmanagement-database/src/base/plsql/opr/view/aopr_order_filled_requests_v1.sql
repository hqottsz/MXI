--liquibase formatted sql


--changeSet aopr_order_filled_requests_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_order_filled_requests_v1
AS
SELECT
   Line_number,
   Part_request_barcode,
   order_id,
   part_request_id
FROM
   acor_order_filled_requests_v1;