--liquibase formatted sql


--changeSet aopr_rfq_vendor_quotes_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_rfq_vendor_quotes_v1
AS
SELECT DISTINCT
   acor_vendors_v1.Vendor_Code,
   status,
   currency,
   exchange_rate,
   terms_and_conditions,
   freight_on_board
FROM
   ACOR_RFQ_VENDOR_QUOTES_V1
   LEFT JOIN acor_vendors_v1  ON
      ACOR_RFQ_VENDOR_QUOTES_V1.vendor_id = acor_vendors_v1.vendor_id;