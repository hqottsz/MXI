--liquibase formatted sql


--changeSet acor_aircraft_usage_v1:1 stripComments:false
CREATE OR REPLACE VIEW ACOR_AIRCRAFT_USAGE_V1 AS
SELECT
   inv_inv.alt_id                 as aircraft_id,
   inv_ac_reg.ac_reg_cd           as registration_code,
   inv_inv.serial_no_oem          as serial_number,
   inv_inv.assmbl_cd              as assembly_code,
   usg_usage_record.usage_dt      as usage_date,
   usg_usage_record.usage_type_cd as usage_type_code,
   mim_data_type.data_type_cd     as data_type_code,
   usg_usage_data.tsn_qt          as tsn_qty,
   usg_usage_data.tsi_qt          as tsi_qty,
   usg_usage_data.tso_qt          as tso_qty,
   usg_usage_data.tsn_delta_qt    as tsn_delta_qty,
   usg_usage_data.tsi_delta_qt    as tsi_delta_qty,
   usg_usage_data.tso_delta_qt    as tso_delta_qty,
   usg_usage_data.usage_record_id as usage_record_id
   --
FROM
   -- aircraft
   inv_ac_reg
   INNER JOIN inv_inv ON
      inv_ac_reg.inv_no_db_id  = inv_inv.inv_no_db_id    AND
      inv_ac_reg.inv_no_id     = inv_inv.inv_no_id
   -- usage
   INNER JOIN usg_usage_record ON
      inv_ac_reg.inv_no_db_id  = usg_usage_record.inv_no_db_id    AND
      inv_ac_reg.inv_no_id     = usg_usage_record.inv_no_id
   INNER JOIN usg_usage_data ON
      usg_usage_record.usage_record_id = usg_usage_data.usage_record_id AND
      usg_usage_record.inv_no_db_id    = usg_usage_data.inv_no_db_id    AND
      usg_usage_record.inv_no_id       = usg_usage_data.inv_no_id
   INNER JOIN mim_data_type ON
      usg_usage_data.data_type_db_id = mim_data_type.data_type_db_id AND
      usg_usage_data.data_type_id    = mim_data_type.data_type_id;