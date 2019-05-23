--liquibase formatted sql


--changeSet acor_part_purchase_vendor_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_part_purchase_vendor_v1
AS 
SELECT org_vendor.alt_id                        vendor_id,
       eqp_part_no.alt_id                       part_id,
       eqp_part_no.part_no_oem                  part_number,
       eqp_part_no.manufact_cd                  manufacturer,
       eqp_part_vendor.part_no_vendor           vendor_part_number,
       org_vendor.vendor_cd                     vendor,
       eqp_part_vendor.pref_bool                preferred,
       eqp_part_vendor_price.unit_price         current_price,
       eqp_part_vendor_price.discount_pct       discount_percent,
       eqp_part_vendor_price.currency_cd        currency,
       eqp_part_vendor_price.qty_unit_cd        unit,
       eqp_part_vendor_price.min_order_qt       min_qty,
       eqp_part_vendor_price.std_sale_qt        standard_qty,
       eqp_part_vendor_price.price_type_cd      price_type,
       eqp_part_vendor_price.lead_time          lead_time_days,
       eqp_part_vendor_price.contract_ref_sdesc contract_number,
       eqp_part_vendor_price.doc_ref_sdesc      quote_reference,
       rfq.event_sdesc                          rfq,
       eqp_part_vendor_price.effective_from_dt  effective_from,
       eqp_part_vendor_price.effective_to_dt    effective_to,
       eqp_part_vendor_price.hist_bool          expired,
       eqp_part_vendor_price.vendor_note,
       eqp_part_vendor.vendor_status_cd vendor_status
  FROM eqp_part_vendor
  JOIN eqp_part_no ON eqp_part_no.part_no_db_id =
                      eqp_part_vendor.part_no_db_id
                  AND eqp_part_no.part_no_id = eqp_part_vendor.part_no_id
  JOIN eqp_part_vendor_price ON eqp_part_vendor_price.part_no_db_id =
                                eqp_part_vendor.part_no_db_id
                            AND eqp_part_vendor_price.part_no_id =
                                eqp_part_vendor.part_no_id
                            AND eqp_part_vendor_price.vendor_db_id =
                                eqp_part_vendor.vendor_db_id
                            AND eqp_part_vendor_price.vendor_id =
                                eqp_part_vendor.vendor_id
  JOIN org_vendor ON org_vendor.vendor_db_id = eqp_part_vendor.vendor_db_id
                 AND org_vendor.vendor_id = eqp_part_vendor.vendor_id
  LEFT OUTER JOIN rfq_line_vendor ON rfq_line_vendor.purch_vendor_db_id =
                                     eqp_part_vendor.vendor_db_id
                                 AND rfq_line_vendor.purch_vendor_id =
                                     eqp_part_vendor.vendor_id
                                 AND rfq_line_vendor.purch_part_no_db_id =
                                     eqp_part_vendor.part_no_db_id
                                 AND rfq_line_vendor.purch_part_no_id =
                                     eqp_part_vendor.part_no_id
  LEFT OUTER JOIN rfq_header ON rfq_header.rfq_db_id =
                                rfq_line_vendor.rfq_db_id
                            AND rfq_header.rfq_id = rfq_line_vendor.rfq_id
  LEFT OUTER JOIN evt_event rfq ON rfq.event_db_id = rfq_header.rfq_db_id
                               AND rfq.event_id = rfq_header.rfq_id
 ORDER BY part_id
;