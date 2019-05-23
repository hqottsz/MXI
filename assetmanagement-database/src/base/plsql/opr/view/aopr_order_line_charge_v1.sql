--liquibase formatted sql


--changeSet aopr_order_line_charge_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_order_line_charge_v1
AS
SELECT
   line_number,
   charge_code,
   charge_amount ,
   order_id
FROM
   acor_order_line_charge_v1;