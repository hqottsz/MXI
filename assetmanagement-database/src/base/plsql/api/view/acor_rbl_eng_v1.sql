--liquibase formatted sql


--changeSet acor_rbl_eng_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_rbl_eng_v1
AS
SELECT
   inventory_id,
   assmbl_code,
   original_assmbl_code,
   serial_number,
   manufacturer,
   part_number,
   condition_code,
   operator_id,
   operator_code,
   in_fleet_date,
   installed_flag
FROM
   (
      SELECT
         inv_inv.alt_id                AS inventory_id,
         eng_assmbl.assmbl_cd          AS eng_assmbl_code,
         acft_assmbl.assmbl_cd         AS assmbl_code,
         inv_inv.orig_assmbl_cd        AS original_assmbl_code,
         inv_inv.serial_no_oem         AS serial_number,
         eqp_part_no.manufact_cd       AS manufacturer,
         eqp_part_no.part_no_oem       AS part_number,
         --
         inv_inv.inv_cond_cd           AS condition_code,
         --
         org_carrier.alt_id            AS operator_id,
         org_carrier.carrier_cd        AS operator_code,
         NVL(inv_inv.received_dt,inv_inv.install_dt) AS in_fleet_date,
         CASE
           WHEN NOT (inv_inv.inv_no_db_id = inv_inv.h_inv_no_db_id AND
                     inv_inv.inv_no_id    = inv_inv.h_inv_no_id
                    ) THEN
              1
           ELSE
              0
         END AS  installed_flag,
         ROW_NUMBER() OVER (PARTITION BY inv_inv.alt_id ORDER BY inv_inv.alt_id) rn
      FROM
         inv_inv
         INNER JOIN eqp_part_no ON
            inv_inv.part_no_db_id = eqp_part_no.part_no_db_id AND
            inv_inv.part_no_id    = eqp_part_no.part_no_id
         INNER JOIN eqp_part_baseline ON
            eqp_part_no.part_no_db_id = eqp_part_baseline.part_no_db_id AND
            eqp_part_no.part_no_id    = eqp_part_baseline.part_no_id
         INNER JOIN eqp_bom_part ON
            eqp_part_baseline.bom_part_db_id = eqp_bom_part.bom_part_db_id AND
            eqp_part_baseline.bom_part_id    = eqp_bom_part.bom_part_id
         INNER JOIN eqp_assmbl acft_assmbl ON
            eqp_bom_part.assmbl_db_id = acft_assmbl.assmbl_db_id AND
            eqp_bom_part.assmbl_cd    = acft_assmbl.assmbl_cd
            AND
            acft_assmbl.assmbl_class_cd = 'ACFT'
         INNER JOIN org_carrier ON
            inv_inv.carrier_db_id = org_carrier.carrier_db_id AND
            inv_inv.carrier_id    = org_carrier.carrier_id
         INNER JOIN eqp_assmbl eng_assmbl ON
            inv_inv.assmbl_db_id    = eng_assmbl.assmbl_db_id AND
            inv_inv.orig_assmbl_cd  = eng_assmbl.assmbl_cd
            AND
            eng_assmbl.assmbl_class_cd = 'ENG'
      WHERE
         inv_inv.inv_cond_db_id = 0 AND
         inv_inv.inv_cond_cd    <> 'SCRAP'
   )
WHERE
   rn = 1
;