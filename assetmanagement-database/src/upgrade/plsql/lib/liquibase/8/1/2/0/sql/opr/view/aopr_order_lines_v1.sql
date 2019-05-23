--liquibase formatted sql


--changeSet aopr_order_lines_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_order_lines_v1
AS 
SELECT
   po_line.line_number,
   po_line.line_description,
   eqp_part_no.part_number,
   eqp_part_no.manufact_cd AS manufacturer_code,
   po_line.part_financial_class,
   po_line.inventory_class,
   po_line.part_financial_type,
   po_line.quantity_unitofmeasure,
   po_line.quantity,
   po_line.unit_price,
   po_line.price_type,
   po_line.line_price,
   po_line.promised_by_date,
   po_line.external_reference,
   po_line.vendor_note,
   po_line.receiver_note,
   po_line.Warranty_contract_number,
   DECODE( po_line.account_id, null, null, fnc_account.account_cd ) AS account_code,
   po_line.order_id,
   po_line.order_line_id,
   po_line.work_package_id,
   po_line.req_by_dt
FROM
   acor_order_lines_v1 po_line
   LEFT JOIN acor_part_number_v1 eqp_part_no ON
      po_line.PART_ID = eqp_part_no.part_id
   LEFT JOIN acor_accounts_v1 fnc_account ON
      fnc_account.id = po_line.account_id
;