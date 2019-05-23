--liquibase formatted sql


--changeSet acor_inv_kit_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_inv_kit_v1
AS
SELECT
   inv_inv.alt_id                        AS inventory_id,
   h_inv.alt_id                          AS h_inventory_id,
   -- asset info
   inv_inv.appl_eff_cd                   AS applicability_range,
   inv_inv.locked_bool                   AS locked_flag,
   -- identity
   inv_inv.barcode_sdesc                 AS barcode,
   inv_inv.inv_no_sdesc                  AS description,
   -- kit info
   inv_kit.kit_sealed_bool               AS kit_sealed_flag,
   inv_owner.alt_id                      AS owner_id,
   inv_owner.owner_cd                    AS owner_code,
   inv_loc.alt_id                        AS location_id,
   inv_loc.loc_cd                        AS location_code,
   -- manufacture
   inv_inv.serial_no_oem                 AS oem_serial_number,
   inv_inv.icn_no_sdesc                  AS icn_number,
   inv_inv.manufact_dt                   AS manufact_date,
   inv_inv.lot_oem_tag                   AS oem_lot_tag,
   part.alt_id                           AS part_id,
   part.part_no_oem                      AS part_number,
   part.manufact_cd                      AS manufacturer_code,
   -- receipt
   inv_inv.receive_cond_cd               AS receive_condition_code,
   inv_inv.received_dt                   AS received_date,
   org_vendor.alt_id                     as vendor_alt_id,
   org_vendor.vendor_cd                  AS vendor_code,
   -- warehouse info
   inv_inv.shelf_expiry_dt               AS shelf_expiry_date,
   inv_inv.issued_bool                   AS issued_flag
FROM
   inv_inv
   -- kit
   INNER JOIN inv_kit ON
      inv_inv.inv_no_db_id = inv_kit.inv_no_db_id AND
      inv_inv.inv_no_id    = inv_kit.inv_no_id
   -- highest inventory
   INNER JOIN inv_inv h_inv ON
      inv_inv.h_inv_no_db_id = h_inv.inv_no_db_id AND
      inv_inv.h_inv_no_id    = h_inv.inv_no_id
   -- owner
   INNER JOIN inv_owner ON
      inv_inv.owner_db_id = inv_owner.owner_db_id AND
      inv_inv.owner_id    = inv_owner.owner_id
   -- location
   INNER JOIN inv_loc ON
       inv_inv.loc_db_id = inv_loc.loc_db_id AND
       inv_inv.loc_id    = inv_loc.loc_id
   -- part number
   INNER JOIN eqp_part_no part ON
      inv_inv.part_no_db_id = part.part_no_db_id AND
      inv_inv.part_no_id    = part.part_no_id
   LEFT JOIN org_vendor ON
      inv_inv.vendor_db_id = org_vendor.vendor_db_id AND
      inv_inv.vendor_id    = org_vendor.vendor_id
WHERE
   inv_inv.inv_class_db_id = 0 AND
   inv_inv.inv_class_cd    = 'KIT'
;