--liquibase formatted sql


--changeSet acor_rbl_acft_assy_type_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_rbl_acft_assy_type_v1 
AS
SELECT
   DISTINCT
   -- the intention here is to get the baseline level
   -- APU or ENG assembly code attached to an aircraft
   org_carrier.alt_id         AS operator_id,
   org_carrier.carrier_cd     AS operator_code,
   h_inv.assmbl_cd            AS fleet_type,
   h_inv.serial_no_oem        AS acft_serial_number,
   eqp_assmbl.assmbl_cd       AS assmbl_code,
   eqp_assmbl.assmbl_class_cd AS assmbl_class_code
FROM
   inv_inv
   INNER JOIN inv_inv h_inv ON
      inv_inv.h_inv_no_db_id = h_inv.inv_no_db_id AND
      inv_inv.h_inv_no_id    = h_inv.inv_no_id
   INNER JOIN inv_ac_reg ON
      inv_inv.h_inv_no_db_id = inv_ac_reg.inv_no_db_id AND
      inv_inv.h_inv_no_id    = inv_ac_reg.inv_no_id
   INNER JOIN eqp_part_baseline ON
      inv_inv.part_no_db_id = eqp_part_baseline.part_no_db_id AND
      inv_inv.part_no_id    = eqp_part_baseline.part_no_id
   INNER JOIN eqp_bom_part ON
      eqp_part_baseline.bom_part_db_id = eqp_bom_part.bom_part_db_id AND
      eqp_part_baseline.bom_part_id    = eqp_bom_part.bom_part_id
   INNER JOIN eqp_assmbl ON
      eqp_bom_part.assmbl_db_id = eqp_assmbl.assmbl_db_id AND
      eqp_bom_part.assmbl_cd    = eqp_assmbl.assmbl_cd
   INNER JOIN org_carrier ON
      inv_inv.carrier_db_id = org_carrier.carrier_db_id AND
      inv_inv.carrier_id    = org_carrier.carrier_id
WHERE
   inv_inv.inv_class_cd = 'ASSY'
;