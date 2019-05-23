--liquibase formatted sql


--changeSet acor_part_exchange_vendor_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_part_exchange_vendor_v1
AS 
SELECT org_vendor.alt_id vendor_id,
       eqp_part_no.alt_id part_id,
       eqp_part_no.part_no_oem part_number,
       eqp_part_no.manufact_cd manufacturer,
       eqp_part_vendor_exchg.part_no_vendor vendor_part_number,
       org_vendor.vendor_cd vendor,
       eqp_part_vendor_exchg.pref_bool preferred,
       eqp_part_vendor_exchg.exchange_cost exchange_cost,
       eqp_part_vendor_exchg.base_price base_price,
       org_vendor.currency_cd currency,
       eqp_part_vendor_exchg.lead_time lead_time_days,
       eqp_part_vendor_exchg.return_time return_time_days,
       LISTAGG(inv_loc.loc_cd, ', ') WITHIN GROUP(order by inv_loc.loc_cd) AS Availability,
	   eqp_part_vendor_exchg.vendor_status_cd vendor_status
  FROM eqp_part_vendor_exchg
  JOIN eqp_part_no ON eqp_part_no.part_no_db_id =
                      eqp_part_vendor_exchg.part_no_db_id
                  AND eqp_part_no.part_no_id =
                      eqp_part_vendor_exchg.part_no_id
  JOIN org_vendor ON org_vendor.vendor_db_id =
                     eqp_part_vendor_exchg.vendor_db_id
                 AND org_vendor.vendor_id = eqp_part_vendor_exchg.vendor_id
  JOIN eqp_part_vendor_exchg_loc ON eqp_part_vendor_exchg_loc.part_no_db_id =
                                    eqp_part_vendor_exchg.part_no_db_id
                                AND eqp_part_vendor_exchg_loc.part_no_id =
                                    eqp_part_vendor_exchg.part_no_id
                                AND eqp_part_vendor_exchg_loc.vendor_db_id =
                                    eqp_part_vendor_exchg.vendor_db_id
                                AND eqp_part_vendor_exchg_loc.vendor_id =
                                    eqp_part_vendor_exchg.vendor_id
  JOIN inv_loc ON inv_loc.loc_db_id = eqp_part_vendor_exchg_loc.loc_db_id
              AND inv_loc.loc_id = eqp_part_vendor_exchg_loc.loc_id
 WHERE eqp_part_vendor_exchg.all_airport_bool = 0
 group by org_vendor.alt_id,
          eqp_part_no.alt_id,
          eqp_part_no.part_no_oem,
          eqp_part_no.manufact_cd,
          eqp_part_vendor_exchg.part_no_vendor,
          org_vendor.vendor_cd,
          eqp_part_vendor_exchg.pref_bool,
          eqp_part_vendor_exchg.exchange_cost,
          eqp_part_vendor_exchg.base_price,
          org_vendor.currency_cd,
          eqp_part_vendor_exchg.lead_time,
          eqp_part_vendor_exchg.return_time,
          eqp_part_vendor_exchg.vendor_status_cd
UNION ALL
SELECT org_vendor.alt_id vendor_id,
       eqp_part_no.alt_id part_id,
       eqp_part_no.part_no_oem part_number,
       eqp_part_no.manufact_cd manufacturer,
       eqp_part_vendor_exchg.part_no_vendor vendor_part_number,
       org_vendor.vendor_cd vendor,
       eqp_part_vendor_exchg.pref_bool preferred,
       eqp_part_vendor_exchg.exchange_cost exchange_cost,
       eqp_part_vendor_exchg.base_price base_price,
       org_vendor.currency_cd currency,
       eqp_part_vendor_exchg.lead_time lead_time_days,
       eqp_part_vendor_exchg.return_time return_time_days,
       'All airports' AS Availability,
       eqp_part_vendor_exchg.vendor_status_cd vendor_status
  FROM eqp_part_vendor_exchg
  JOIN eqp_part_no ON eqp_part_no.part_no_db_id =
                      eqp_part_vendor_exchg.part_no_db_id
                  AND eqp_part_no.part_no_id =
                      eqp_part_vendor_exchg.part_no_id
  JOIN org_vendor ON org_vendor.vendor_db_id =
                     eqp_part_vendor_exchg.vendor_db_id
                 AND org_vendor.vendor_id = eqp_part_vendor_exchg.vendor_id
 WHERE eqp_part_vendor_exchg.all_airport_bool = 1
;