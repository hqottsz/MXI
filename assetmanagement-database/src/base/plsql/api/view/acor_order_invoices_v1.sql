--liquibase formatted sql


--changeSet acor_order_invoices_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_order_invoices_v1
AS
SELECT
   evt_event.event_sdesc AS invoice_number,
   ref_event_status.user_status_cd AS Status,
   po_invoice.invoice_dt AS Invoice_date,
   po_invoice.pay_dt AS Closing_date,
   (
      SELECT
         SUM( inner_line.line_price )
      FROM
         po_invoice_line inner_line
      WHERE
         inner_line.po_invoice_db_id = po_invoice.po_invoice_db_id AND
         inner_line.po_invoice_id    = po_invoice.po_invoice_id
   ) AS total_price,
   DECODE( ref_terms_conditions.terms_conditions_cd, NULL, NULL, ref_terms_conditions.terms_conditions_cd || ' (' || ref_terms_conditions.desc_sdesc || ')' ) AS terms_conditions,
   po_invoice.cash_discount_pct AS Cash_discount,
   po_invoice.cash_discount_exp_dt AS discount_expiry_date,
   DECODE( utl_user.user_id, null, null, utl_user.first_name || ' ' || utl_user.last_name ) as po_invoice_contact,
   evt_event.ext_key_sdesc AS external_reference,
   po_invoice.alt_id AS id
FROM
   po_invoice
   INNER JOIN evt_event ON
      po_invoice.po_invoice_db_id = evt_event.event_db_id AND
      po_invoice.po_invoice_id    = evt_event.event_id
   INNER JOIN ref_event_status ON
      ref_event_status.event_status_db_id = evt_event.event_status_db_id AND
      ref_event_status.event_status_cd    = evt_event.event_status_cd
   LEFT JOIN org_hr ON
      po_invoice.contact_hr_db_id = org_hr.hr_db_id AND
      po_invoice.contact_hr_id    = org_hr.hr_id
   LEFT JOIN utl_user ON
      org_hr.user_id = utl_user.user_id
   LEFT JOIN ref_terms_conditions ON
      po_invoice.terms_conditions_db_id = ref_terms_conditions.terms_conditions_db_id AND
      po_invoice.terms_conditions_cd    = ref_terms_conditions.terms_conditions_cd
WHERE
   evt_event.event_type_cd = 'PI';