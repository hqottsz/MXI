--liquibase formatted sql


--changeSet acor_csn_stock_requests_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_csn_stock_requests_v1
AS
SELECT req_part.alt_id AS ID,
       eqp_stock_no.alt_id AS stock_id,
       eqp_part_no.alt_id AS part_id,
       req_loc.alt_id AS where_needed_loc_id,
       org_vendor.alt_id AS vendor_id,
       evt_event.event_sdesc request_id,
       org_vendor.vendor_cd AS vendor_code,
       org_vendor.vendor_name AS vendor_name,
       eqp_stock_no.stock_no_cd AS stock_number,
       eqp_part_no.part_no_oem AS part_number,
       req_part.req_qt AS qty,
       ref_qty_unit.qty_unit_cd AS unit,
       req_loc.loc_cd AS where_needed,
       req_part.purch_type_cd AS purchase_type,
       req_part.req_by_dt AS stock_low_date
  FROM evt_event
 INNER JOIN req_part ON req_part.req_part_db_id = evt_event.event_db_id
                    AND req_part.req_part_id = evt_event.event_id
                    AND req_part.req_type_db_id = 0
                    AND req_part.req_type_cd = 'CSNSTOCK'
 INNER JOIN inv_loc req_loc ON req_loc.loc_db_id = req_part.req_loc_db_id
                           AND req_loc.loc_id = req_part.req_loc_id
  LEFT OUTER JOIN eqp_part_no ON
 eqp_part_no.part_no_db_id = req_part.req_spec_part_no_db_id
 AND eqp_part_no.part_no_id = req_part.req_spec_part_no_id
  LEFT OUTER JOIN eqp_stock_no ON eqp_stock_no.stock_no_db_id =
                                  req_part.req_stock_no_db_id
                              AND eqp_stock_no.stock_no_id =
                                  req_part.req_stock_no_id
  LEFT OUTER JOIN eqp_part_vendor ON eqp_part_vendor.part_no_db_id =
                                     eqp_part_no.part_no_db_id
                                 AND eqp_part_vendor.part_no_id =
                                     eqp_part_no.part_no_id
                                 AND eqp_part_vendor.pref_bool = 1
  LEFT OUTER JOIN org_vendor ON org_vendor.vendor_db_id =
                                eqp_part_vendor.vendor_db_id
                            AND org_vendor.vendor_id =
                                eqp_part_vendor.vendor_id
  LEFT OUTER JOIN ref_qty_unit ON
--get quantity related information
 ref_qty_unit.qty_unit_db_id = eqp_part_no.qty_unit_db_id
 AND ref_qty_unit.qty_unit_cd = eqp_part_no.qty_unit_cd
  LEFT OUTER JOIN ref_currency ON ref_currency.currency_db_id =
                                  org_vendor.currency_db_id
                              AND ref_currency.currency_cd =
                                  org_vendor.currency_cd
 WHERE
-- Get only Part Requests that require a Purchase Order
 evt_event.event_status_cd = 'PRPOREQ'
 AND req_part.rfq_db_id IS NULL
 AND req_part.po_db_id IS NULL
;