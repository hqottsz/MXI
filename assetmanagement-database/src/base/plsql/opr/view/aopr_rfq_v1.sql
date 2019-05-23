--liquibase formatted sql


--changeSet aopr_rfq_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_rfq_v1
AS
SELECT
   rfq_number,
   status,
   respond_by_date,
   needed_by_date,
   purchasing_contact,
   external_ref,
   desired_currency,
   exchange_rate,
   terms_conditions,
   transportation_type,
   freight_on_board,
   vendor_note,
   spec2000_customer_code,
   rfq_id
FROM
   ACOR_RFQ_V1;