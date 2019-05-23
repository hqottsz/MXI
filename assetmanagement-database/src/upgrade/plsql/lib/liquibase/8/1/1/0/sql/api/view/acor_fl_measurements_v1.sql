--liquibase formatted sql


--changeSet acor_fl_measurements_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_fl_measurements_v1
AS 
SELECT
   fl_leg.leg_id,
   inv_inv.alt_id                   AS inventory_id,
   mim_data_type.alt_id             AS data_type_id,
   mim_data_type.data_type_cd       AS data_type_code,
   mim_data_type.data_type_sdesc    AS data_type_description,
   fl_leg_measurement.data_value_cd AS data_value_code,
   fl_leg_measurement.data_qt       AS data_quantity,
   fl_leg_measurement.data_text,
   fl_leg_measurement.data_dt       AS data_date,
   fl_leg_measurement.data_bool     AS data_flag,
   fl_leg_measurement.legacy_key
FROM
   fl_leg
   INNER JOIN fl_leg_measurement  ON
      fl_leg.leg_id = fl_leg_measurement.leg_id
   INNER JOIN inv_inv ON
      fl_leg_measurement.inv_no_db_id = inv_inv.inv_no_db_id AND
      fl_leg_measurement.inv_no_id    = inv_inv.inv_no_id
   INNER JOIN mim_data_type ON
      fl_leg_measurement.data_type_db_id = mim_data_type.data_type_db_id AND
      fl_leg_measurement.data_type_id    = mim_data_type.data_type_id
;