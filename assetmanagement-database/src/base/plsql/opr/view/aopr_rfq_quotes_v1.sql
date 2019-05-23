--liquibase formatted sql


--changeSet aopr_rfq_quotes_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_rfq_quotes_v1
AS
SELECT
   line_number,
   acor_vendors_v1.Vendor_Code,
   quote_qty,
   unit_price,
   line_price,
   lead_time,
   effective_to_date,
   ACOR_RFQ_QUOTES_V1.vendor_note,
   quote_date,
   quote_number,
   quote_detail_prefered,
   quote_detail_vendor_status,
   rfq_id
FROM
   ACOR_RFQ_QUOTES_V1
   INNER JOIN acor_vendors_v1 ON
      acor_vendors_v1.vendor_id = ACOR_RFQ_QUOTES_V1.vendor_id;