--liquibase formatted sql


--changeSet acor_stocks_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_stocks_v1
AS 
SELECT
    eqp_stock_no.alt_id                  AS stock_id,
    eqp_stock_no.stock_no_cd             AS stock_code,
    eqp_stock_no.stock_no_name           AS stock_name,
    ref_qty_unit.qty_unit_cd             AS quantity_unit_code,
    ref_qty_unit.desc_sdesc              AS quantity_unit_name,
    ref_qty_unit.decimal_places_qt,
    eqp_stock_no.auto_create_po_bool     AS auto_create_po_flag,
    eqp_stock_no.auto_issue_po_bool      AS auto_issue_po_flag,
    ref_inv_class.inv_class_cd           AS inv_class_code,
    ref_inv_class.desc_sdesc             AS inv_class_name,
    ref_inv_class.tracked_bool           AS tracked_flag,
    ref_inv_class.traceable_bin_bool     AS traceable_bin_flag,
    ref_inv_class.serial_bool            AS serial_flag,
    ref_abc_class.abc_class_cd           AS abc_class_code,
    ref_abc_class.desc_sdesc             AS abc_class_name,
    eqp_stock_no.carry_cost_interest_qt,
    eqp_stock_no.carry_cost_insurance_qt,
    eqp_stock_no.carry_cost_taxes_qt,
    eqp_stock_no.carry_cost_storage_qt,
    eqp_stock_no.carry_cost_total_qt,
    eqp_stock_no.service_lvl_pct         AS service_level_percent,
    eqp_stock_no.purchase_lead_time,
    eqp_stock_no.repair_lead_time,
    eqp_stock_no.shipping_time_qt,
    eqp_stock_no.processing_time_qt,
    eqp_stock_no.total_lead_time_qt,
    eqp_stock_no.global_reorder_qt,
    eqp_stock_no.safety_level_qt         AS golbal_safety_level_qt,
    eqp_stock_no.monthly_demand_qt,
    eqp_stock_no.batch_size              As reorder_quantity,
    eqp_stock_no.ship_qt                 AS shipping_quantity,
    ref_purch_type.purch_type_cd         AS purchase_type_code,
    ref_purch_type.desc_sdesc            AS purchase_type_name
FROM
    eqp_stock_no
    INNER JOIN ref_qty_unit ON
       eqp_stock_no.qty_unit_db_id = ref_qty_unit.qty_unit_db_id AND
       eqp_stock_no.qty_unit_cd    = ref_qty_unit.qty_unit_cd
    INNER JOIN ref_inv_class ON
       eqp_stock_no.inv_class_db_id = ref_inv_class.inv_class_db_id AND
       eqp_stock_no.inv_class_cd    = ref_inv_class.inv_class_cd
    LEFT JOIN ref_abc_class ON
       eqp_stock_no.abc_class_db_id = ref_abc_class.abc_class_db_id AND
       eqp_stock_no.abc_class_cd    = ref_abc_class.abc_class_cd
    LEFT JOIN ref_purch_type ON
       eqp_stock_no.purch_type_db_id = ref_purch_type.purch_type_db_id AND
       eqp_stock_no.purch_type_cd    = ref_purch_type.purch_type_cd;