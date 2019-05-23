--liquibase formatted sql


--changeSet acor_inv_current_usage_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_inv_current_usage_v1
AS
SELECT
   inv_inv.alt_id                    AS inventory_id,
   --
   mim_data_type.alt_id              AS data_type_id,
   mim_data_type.data_type_cd        AS data_type_code,
   TRIM(TO_CHAR(inv_curr_usage.tsn_qt,'9999999999999999999999' || RTRIM(RPAD('.', entry_prec_qt + 1,'9'),'.')))  AS time_since_new_quantity,
   TRIM(TO_CHAR(inv_curr_usage.tso_qt,'9999999999999999999999' || RTRIM(RPAD('.', entry_prec_qt + 1,'9'),'.')))  AS time_since_overhaul_quantity,
   TRIM(TO_CHAR(inv_curr_usage.tsi_qt,'9999999999999999999999' || RTRIM(RPAD('.', entry_prec_qt + 1,'9'),'.')))  AS time_since_install_quantity
FROM
   inv_inv
   -- current usage
   INNER JOIN inv_curr_usage ON
      inv_inv.inv_no_db_id = inv_curr_usage.inv_no_db_id AND
      inv_inv.inv_no_id    = inv_curr_usage.inv_no_id
   -- data type
   INNER JOIN mim_data_type ON
      inv_curr_usage.data_type_db_id = mim_data_type.data_type_db_id AND
      inv_curr_usage.data_type_id    = mim_data_type.data_type_id;