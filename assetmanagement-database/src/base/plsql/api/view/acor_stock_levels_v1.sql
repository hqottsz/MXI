--liquibase formatted sql


--changeSet acor_stock_levels_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_stock_levels_v1
AS
SELECT
    inv_loc_stock.alt_id             AS stock_level_id,
    eqp_stock_no.alt_id              AS stock_id,
    inv_loc.alt_id                   AS location_id,
    inv_loc.loc_cd                   AS location_code,
    inv_loc.loc_name                 AS location_name,
    inv_owner.owner_cd               AS owner_code,
    inv_owner.alt_id                 AS owner_id,
    hub_inv_loc.alt_id               AS supplier_location_id,
    hub_inv_loc.loc_cd               AS supplier_location_code,
    hub_inv_loc.loc_name             AS supplier_location_name,
    inv_loc.inbound_flights_qt,
    inv_loc_stock.weight_factor_qt   AS station_weight_factor_qt,
    inv_loc_stock.alloc_pct          AS allocation_percent,
    inv_loc_stock.min_reorder_qt     AS min_reorder_level,
    inv_loc_stock.reorder_qt         AS reorder_level,
    inv_loc_stock.batch_size         AS local_reorder_quantity,
    inv_loc_stock.max_qt             AS max_qt,
    inv_loc_stock.stock_low_actn_cd  AS stock_low_action_code,
    eqp_stock_no.max_mult_qt,
    eqp_stock_no.batch_size          AS global_reorder_quantity,
    ref_qty_unit.qty_unit_cd         AS quantity_unit_code,
    ref_qty_unit.decimal_places_qt   AS decimal_places_qt
FROM
    eqp_stock_no
    INNER JOIN inv_loc_stock ON
       eqp_stock_no.stock_no_db_id = inv_loc_stock.stock_no_db_id AND
       eqp_stock_no.stock_no_id    = inv_loc_stock.stock_no_id
    INNER JOIN inv_loc ON
       inv_loc.loc_db_id = inv_loc_stock.loc_db_id AND
       inv_loc.loc_id    = inv_loc_stock.loc_id
    LEFT JOIN inv_loc hub_inv_loc ON
       inv_loc.hub_loc_db_id = hub_inv_loc.loc_db_id AND
       inv_loc.hub_loc_id    = hub_inv_loc.loc_id
    LEFT JOIN inv_owner ON
       inv_loc_stock.owner_db_id = inv_owner.owner_db_id AND
       inv_loc_stock.owner_id    = inv_owner.owner_id
    LEFT JOIN ref_stock_low_actn ON
       inv_loc_stock.stock_low_actn_db_id = ref_stock_low_actn.stock_low_actn_db_id AND
       inv_loc_stock.stock_low_actn_cd    = ref_stock_low_actn.stock_low_actn_cd
    INNER JOIN ref_qty_unit ON
       ref_qty_unit.qty_unit_db_id = eqp_stock_no.qty_unit_db_id AND
       ref_qty_unit.qty_unit_cd    = eqp_stock_no.qty_unit_cd
WHERE 
    inv_loc.supply_bool = 1  
ORDER BY
    location_code;