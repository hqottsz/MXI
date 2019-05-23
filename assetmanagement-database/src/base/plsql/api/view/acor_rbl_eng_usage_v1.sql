--liquibase formatted sql


--changeSet acor_rbl_eng_usage_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_rbl_eng_usage_v1 
AS
SELECT
   usg_usage_record.usage_record_id,
   inv_inv.alt_id                AS inventory_id,
   assmbl_inv.assmbl_cd          AS assmbl_code,
   inv_inv.orig_assmbl_cd        AS original_assmbl_code,
   inv_inv.serial_no_oem         AS serial_number,
   eqp_part_no.manufact_cd       AS manufacturer,
   eqp_part_no.part_no_oem       AS part_number,
   --
   org_carrier.alt_id            AS operator_id,
   org_carrier.carrier_cd        AS operator_code,
   --
   usg_usage_record.usage_type_cd usage_type_code,
   mim_data_type.data_type_cd    AS data_type_code,
   usg_usage_record.usage_dt     AS usage_date,
   usg_usage_record.record_dt    AS recorded_date,
   usg_usage_data.tsn_qt,
   usg_usage_data.tso_qt,
   usg_usage_data.tsi_qt,
   usg_usage_data.tsn_delta_qt,
   usg_usage_data.tso_delta_qt,
   usg_usage_data.tsi_delta_qt,
   usg_usage_data.negated_bool
FROM
   usg_usage_record
   INNER JOIN usg_usage_data ON
      usg_usage_record.usage_record_id = usg_usage_data.usage_record_id
   INNER JOIN mim_data_type ON
      usg_usage_data.data_type_db_id = mim_data_type.data_type_db_id AND
      usg_usage_data.data_type_id    = mim_data_type.data_type_id
   INNER JOIN inv_inv ON
      usg_usage_data.inv_no_db_id = inv_inv.inv_no_db_id AND
      usg_usage_data.inv_no_id    = inv_inv.inv_no_id
   INNER JOIN inv_inv assmbl_inv ON
      usg_usage_data.assmbl_inv_no_db_id =  assmbl_inv.inv_no_db_id AND
      usg_usage_data.assmbl_inv_no_id    =  assmbl_inv.inv_no_id
   INNER JOIN eqp_part_no ON
      inv_inv.part_no_db_id = eqp_part_no.part_no_db_id AND
      inv_inv.part_no_id    = eqp_part_no.part_no_id
   INNER JOIN org_carrier ON
      inv_inv.carrier_db_id = org_carrier.carrier_db_id AND
      inv_inv.carrier_id    = org_carrier.carrier_id
   INNER JOIN eqp_assmbl ON
      inv_inv.assmbl_db_id = eqp_assmbl.assmbl_db_id AND
      inv_inv.orig_assmbl_cd    = eqp_assmbl.assmbl_cd
      AND
      eqp_assmbl.assmbl_class_cd = 'ENG'
;