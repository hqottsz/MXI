--liquibase formatted sql


--changeSet acor_financial_trx_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_financial_trx_v1
AS 
SELECT
   log.alt_id AS Transaction_id,
   XACTION_TYPE_CD AS Transaction_type,
   XACTION_DT AS Transaction_date,
   XACTION_LDESC AS Description,
   CASE log.unit_price
      WHEN 0.00000 THEN
         TRIM(TO_CHAR(log.UNIT_PRICE,'9999999999999999999990' || RTRIM(RPAD('.', NVL(ref_currency.sub_units_qt,2) + 1,'9'),'.')))
      ELSE
         TRIM(TO_CHAR(log.UNIT_PRICE,'9999999999999999999999' || RTRIM(RPAD('.', NVL(ref_currency.sub_units_qt,2) + 1,'9'),'.')))
   END  AS unit_price,
   QTY,
   currency.currency_cd AS currency,
   currency.exchg_qt   AS exchange_rate,
   inv_loc.alt_id AS inv_from_loc_id,
   supl_loc.alt_id AS supply_loc_id,
   vendor.alt_id AS vendor_id,
   inv_inv.alt_id AS inventory_id,
   po_header.alt_id AS order_id,
   po_line.alt_id AS order_line_id,
   po_invoice.alt_id AS invoice_id,
   acor_order_invoices_v1.invoice_number,
   po_invoice_line.alt_id AS invoice_line_id,
   po_line.line_no_ord AS order_line_number,
   po_invoice_line.line_no_ord AS invoice_line_number
FROM
   fnc_xaction_log log
   LEFT JOIN evt_loc ON
      log.event_db_id = evt_loc.event_db_id AND
      log.event_id    = evt_loc.event_id AND
      evt_loc.event_loc_id        = 1
   LEFT JOIN inv_inv ON
      log.inv_no_db_id = inv_inv.inv_no_db_id AND
      log.inv_no_id    = inv_inv.inv_no_id
   -- get inventory ship from location
   LEFT JOIN inv_loc ON
      inv_loc.loc_db_id = evt_loc.loc_db_id AND
      inv_loc.loc_id    = evt_loc.loc_id
   -- get supply location
   LEFT JOIN inv_loc supl_loc ON
      inv_loc.supply_loc_db_id = supl_loc.loc_db_id AND
      inv_loc.supply_loc_id    = supl_loc.loc_id
   LEFT OUTER JOIN org_vendor vendor ON
      inv_loc.vendor_db_id = vendor.vendor_db_id AND
      inv_loc.vendor_id    = vendor.vendor_id
   -- get currency
   LEFT OUTER JOIN ref_currency currency ON
      log.currency_db_id = currency.currency_db_id AND
      log.currency_cd    = currency.currency_cd
   LEFT JOIN po_header ON
      inv_inv.po_db_id = po_header.po_db_id AND
      inv_inv.po_id    = po_header.po_id
   LEFT JOIN po_line ON
      log.po_db_id = po_line.po_db_id AND
      log.po_id    = po_line.po_id AND
      log.po_line_id = po_line.po_line_id
   LEFT JOIN po_invoice ON
      log.po_invoice_db_id = po_invoice.po_invoice_db_id AND
      log.po_invoice_id    = po_invoice.po_invoice_id
   LEFT JOIN acor_order_invoices_v1 ON
      po_invoice.alt_id = acor_order_invoices_v1.id
   LEFT JOIN po_invoice_line ON
      log.po_invoice_db_id = po_invoice_line.po_invoice_db_id AND
      log.po_invoice_id    = po_invoice_line.po_invoice_id AND
      log.po_invoice_line_id = po_invoice_line.po_invoice_line_id
   LEFT JOIN ref_currency ON
      log.currency_db_id = ref_currency.currency_db_id AND
      log.currency_cd    = ref_currency.currency_cd
;