--liquibase formatted sql


--changeSet acor_rbl_eng_measure_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_rbl_eng_measure_v1
AS
SELECT
      inv_inv.alt_id         AS inventory_id,
      ind_leg.leg_id,
      ind_leg.data_qt        AS data_quantity,
      ind_leg.data_dt        AS data_date,
      ind_leg.data_value_cd  AS data_value_code,
      ind_leg.data_bool      AS data_flag,
      --
      ind_leg.data_type_code,
      ind_leg.data_type_description  AS oper_indicator
   FROM
      fl_leg_measurement
      INNER JOIN ( -- FPTAKEOFF: this is a measurement against ACFT
                   -- find the flight leg with this measurement
                   -- use the flight leg to find the ENG inventory
                  SELECT
                     leg_id,
                     data_dt,
                     data_value_cd,
                     data_qt,
                     data_bool,
                     --
                     data_type_description,
                     data_type_code
                  FROM
                     fl_leg_measurement
                     INNER JOIN mim_data_type ON
                        fl_leg_measurement.data_type_db_id = mim_data_type.data_type_db_id AND
                        fl_leg_measurement.data_type_id    = mim_data_type.data_type_id
                     -- indicator lookup table (OPR)
                     INNER JOIN opr_rbl_eng_indicator_measure ind_measure ON
                        mim_data_type.data_type_cd = ind_measure.data_type_code
                 ) ind_leg ON
         fl_leg_measurement.leg_id =  ind_leg.leg_id
      INNER JOIN inv_inv ON
         fl_leg_measurement.inv_no_db_id = inv_inv.inv_no_db_id AND
         fl_leg_measurement.inv_no_id    = inv_inv.inv_no_id
      INNER JOIN eqp_assmbl ON
         inv_inv.orig_assmbl_db_id = eqp_assmbl.assmbl_db_id AND
         inv_inv.orig_assmbl_cd    = eqp_assmbl.assmbl_cd
         AND
         eqp_assmbl.assmbl_class_cd = 'ENG';