--liquibase formatted sql


--changeSet acor_part_number_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_part_number_v1
AS
SELECT
   eqp_part_no.alt_id                     AS part_id,
   inv_class_cd,
   part_type_cd,
   part_status_cd,
   part_use_cd,
   manufact_cd,
   eqp_part_no.bitmap_tag,
   shipping_instr_cd,
   shipping_ldesc             AS shipping_description,
   packaging_instr_cd,
   packaging_ldesc            AS packaging_description,
   storage_instr_cd,
   storage_ldesc              AS storage_description,
   qty_unit_cd,
   part_no_sdesc              AS part_number_name,
   part_no_oem                AS part_number,
   model_name,
   rev_no_name                AS revision_number_name,
   part_no_ldesc              AS part_number_description,
   repair_bool                AS repair_flag,
   receipt_insp_bool          AS inspection_upon_receipt_flag,
   mtbur_qt                   AS mean_time_btw_unsched_removal,
   mtbr_qt                    AS mean_time_btw_removal,
   mtbf_qt                    AS mean_time_btw_failure,
   mttr_qt                    AS meant_time_to_repair,
   stock_pct                  AS stock_percentage,
   scrap_rate_pct             AS scrap_rage,
   shelf_life_unit_cd,
   shelf_life_qt              AS shelf_life_quantity,
   hazmat_cd,
   dg_ref_sdesc               AS dangerous_good_description,
   ref_financial_class.financial_class_cd,
   ref_financial_class.finance_type_cd,
   power_eng_unit_cd,
   power_qt                   AS power_quantity,
   freq_eng_unit_cd,
   freq_qt                    AS frequency_quantity,
   voltage_eng_unit_cd,
   voltage_qt                 AS voltage_quantity,
   current_eng_unit_cd,
   current_qt                 AS current_quantity,
   weight_eng_unit_cd,
   dimension_eng_unit_cd,
   abc_class_cd,
   calc_abc_class_bool         AS calculate_abc_class_flag,
   weight_qt                   AS weight_quantity,
   height_qt                   AS height_quantity,
   width_qt                    AS width_quantity,
   length_qt,
   control_reserve_bool        AS contro_reserve_bool,
   use_default_batch_no_bool   AS default_batch_number_flag,
   doc_req_ldesc               AS doc_required_description,
   sos_bool                    AS ship_or_shelf_flag,
   avg_unit_price              AS average_unit_price,
   total_qt                    AS total_quantity,
   total_value                 AS total_value,
   no_auto_reserve_bool        AS auto_reserve_flag,
   tariff_cd                   AS tariff_code,
   pma_bool                    AS manufacturer_approval_flag,
   eccn_cd                     AS export_control_class_code,
   etops_bool                   AS etops_flag,
   procurable_bool             AS procurable_flag,
   note,
   fnc_account.alt_id          AS assetaccount_id
FROM
  eqp_part_no
  INNER JOIN fnc_account ON
     eqp_part_no.asset_account_db_id = fnc_account.account_db_id and
     eqp_part_no.asset_account_id    = fnc_account.account_id
  INNER JOIN ref_financial_class ON
     eqp_part_no.financial_class_db_id = ref_financial_class.financial_class_db_id AND
     eqp_part_no.financial_class_cd    = ref_financial_class.financial_class_cd
;