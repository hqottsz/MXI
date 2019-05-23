--liquibase formatted sql


--changeSet acor_part_repair_vendor_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_part_repair_vendor_v1
AS 
SELECT org_vendor.alt_id vendor_id,
       eqp_part_no.alt_id part_id,
       eqp_part_no.part_no_oem part_number,
       eqp_part_no.manufact_cd manufacturer,
       org_vendor.vendor_cd vendor,
       eqp_part_vendor_rep.pref_bool preferred,
       eqp_part_vendor_rep.repair_cost repair_price,
       org_vendor.currency_cd currency,
       eqp_part_vendor_rep.lead_time repair_time_days,
       eqp_part_vendor_rep.vendor_status_cd vendor_status
  FROM eqp_part_vendor_rep
  JOIN eqp_part_no ON eqp_part_no.part_no_db_id =
                      eqp_part_vendor_rep.part_no_db_id
                  AND eqp_part_no.part_no_id =
                      eqp_part_vendor_rep.part_no_id
  JOIN org_vendor ON org_vendor.vendor_db_id =
                     eqp_part_vendor_rep.vendor_db_id
                 AND org_vendor.vendor_id = eqp_part_vendor_rep.vendor_id
;