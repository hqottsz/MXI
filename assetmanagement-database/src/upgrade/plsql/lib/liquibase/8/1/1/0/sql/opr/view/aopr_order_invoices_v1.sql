--liquibase formatted sql


--changeSet aopr_order_invoices_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_order_invoices_v1
AS 
SELECT
   invoice_number,
   Status,
   Invoice_date,
   Closing_date,
   total_price AS po_invoice_total,
   terms_conditions,
   Cash_discount,
   discount_expiry_date,
   po_invoice_contact,
   external_reference,
   id
FROM
   acor_order_invoices_v1;