--liquibase formatted sql


--changeSet acor_rfq_vendor_quotes_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_rfq_vendor_quotes_v1
AS 
SELECT DISTINCT
   org_vendor.alt_id AS vendor_id,
   org_vendor_po_type.vendor_status_cd AS status,
   rfq_vendor.currency_cd AS currency,
   DECODE( rfq_vendor.exchg_qt, NULL, NULL, rfq_vendor.exchg_qt || ' ' || rfq_vendor.currency_cd || '/' || (select currency_cd from ref_currency where default_bool=1) ) AS exchange_rate,
   rfq_vendor.terms_conditions_cd AS terms_and_conditions,
   rfq_vendor.fob_cd AS freight_on_board
FROM
   rfq_header
   INNER JOIN rfq_vendor ON
      rfq_vendor.rfq_db_id = rfq_header.rfq_db_id AND
      rfq_vendor.rfq_id    = rfq_header.rfq_id
   LEFT JOIN org_vendor ON
      rfq_vendor.vendor_db_id = org_vendor.vendor_db_id AND
      rfq_vendor.vendor_id    = org_vendor.vendor_id
   LEFT JOIN org_vendor_po_type ON
      org_vendor.vendor_db_id = org_vendor_po_type.vendor_db_id AND
      org_vendor.vendor_id    = org_vendor_po_type.vendor_id
WHERE
   org_vendor_po_type.po_type_db_id = 0 AND
   org_vendor_po_type.po_type_cd    = 'PURCHASE';