--liquibase formatted sql


--changeSet acor_inv_info_v1:1 stripComments:false
CREATE OR REPLACE VIEW acor_inv_info_v1 
AS
SELECT
    inv_inv.inv_class_cd AS inventory_class_code,
    inv_inv.inv_cond_cd  AS inventory_condition_code,
    inv_inv.serial_no_oem AS serial_number,
    inv_inv.barcode_sdesc AS barcode,
    inv_owner.owner_cd AS owner_code,
    inv_owner.owner_name,
    inv_inv.alt_id AS inventory_id,
    inv_loc.alt_id AS location_id,
    eqp_part_no.alt_id AS part_id,
    inv_inv.bin_qt     AS bin_quantity,
    inv_loc.loc_type_cd AS location_type_code,
    inv_loc.loc_cd AS location_code,
    inv_loc.loc_name AS location_name
FROM
  inv_inv
  INNER JOIN inv_owner ON
  inv_owner.owner_db_id = inv_inv.owner_db_id AND
  inv_owner.owner_id    = inv_inv.owner_id
  INNER JOIN inv_loc ON
  inv_loc.loc_db_id  = inv_inv.loc_db_id   AND
  inv_loc.loc_id     = inv_inv.loc_id
  INNER JOIN eqp_part_no ON
  eqp_part_no.part_no_db_id = inv_inv.part_no_db_id AND
  eqp_part_no.part_no_id    = inv_inv.part_no_id
;