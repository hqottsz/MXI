--liquibase formatted sql


--changeSet acor_order_lines_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_order_lines_v1
AS 
SELECT
   po_line.line_no_ord AS line_number,
   po_line.line_ldesc AS line_description,
   ref_financial_class.financial_class_cd AS part_financial_class,
   eqp_part_no.inv_class_cd AS inventory_class,
   ref_financial_class.finance_type_cd AS part_financial_type,
   po_line.qty_unit_cd AS quantity_unitofmeasure,
   po_line.order_qt AS quantity,
   po_line.unit_price,
   po_line.price_type_cd AS price_type,
   po_line.line_price,
   po_line.promise_by_dt AS promised_by_date,
   po_line.po_line_ext_sdesc AS external_reference,
   po_line.vendor_note,
   po_line.receiver_note,
   warranty_defn.warranty_sdesc AS Warranty_contract_number,
   fnc_account.alt_id AS account_id,
   po_header.alt_id AS order_id,
   eqp_part_no.alt_id AS part_id,
   po_line.alt_id AS order_line_id,
   sched_stask.alt_id AS work_package_id,
   eqp_part_no.part_no_oem AS part_number,
   eqp_part_no.manufact_cd AS manufacturer_code,
   req_part.req_by_dt
FROM
   po_line
   INNER JOIN evt_event ON
      po_line.po_db_id = evt_event.event_db_id AND
      po_line.po_id    = evt_event.event_id
   INNER JOIN po_header ON
      po_header.po_db_id = po_line.po_db_id AND
      po_header.po_id    = po_line.po_id
   LEFT JOIN eqp_part_no ON
      po_line.PART_NO_DB_ID = eqp_part_no.part_no_db_id AND
      po_line.PART_NO_ID    = eqp_part_no.part_no_id
   LEFT JOIN fnc_account ON
      fnc_account.account_db_id = po_line.account_db_id AND
      fnc_account.account_id    = po_line.account_id
   LEFT JOIN po_line_warranty ON
      po_line_warranty.po_db_id    = po_line.po_db_id   AND
      po_line_warranty.po_id       = po_line.po_id      AND
      po_line_warranty.po_line_id  = po_line.po_line_id
   LEFT JOIN warranty_defn ON
      warranty_defn.warranty_defn_db_id =  po_line_warranty.warranty_defn_db_id   AND
      warranty_defn.warranty_defn_id    =  po_line_warranty.warranty_defn_id
   LEFT JOIN ref_financial_class ON
      ref_financial_class.financial_class_db_id = eqp_part_no.financial_class_db_id AND
      ref_financial_class.financial_class_cd    = eqp_part_no.financial_class_cd
   LEFT JOIN sched_stask ON
      po_line.sched_db_id = sched_stask.sched_db_id AND
      po_line.sched_id    = sched_stask.sched_id
   LEFT JOIN req_part ON
      req_part.po_db_id   = po_line.po_db_id AND
      req_part.po_id      = po_line.po_id AND
      req_part.po_line_id = po_line.po_line_id
WHERE
   po_header.rstat_cd = 0
;