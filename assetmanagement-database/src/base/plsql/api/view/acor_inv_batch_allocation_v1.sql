--liquibase formatted sql


--changeSet acor_inv_batch_allocation_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_inv_batch_allocation_v1
AS
SELECT
   inv_inv.alt_id                       AS inventory_id,
   -- asset info
   inv_inv.locked_bool                   AS locked_flag,
   org_authority.alt_id                  AS authority_id,
   org_authority.authority_cd            AS authority_code,
   -- identity
   inv_inv.barcode_sdesc                 AS barcode,
   inv_inv.inv_no_sdesc                  AS description,
   -- manufacture
   inv_inv.icn_no_sdesc                  AS icn_number,
   inv_inv.manufact_dt                   AS manufact_date,
   inv_inv.lot_oem_tag                   AS oem_lot_tag,
   part.alt_id                           AS part_id,
   part.part_no_oem                      AS part_number,
   -- warehouse info
   inv_inv.shelf_expiry_dt               AS shelf_expiry_date,
   inv_inv.issued_bool                   AS issued_flag,
   -- receipt
   inv_inv.received_dt                   AS received_date,
   -- location
   acor_location_v1.location_id          AS location_id,
   acor_location_v1.nh_location_id       AS nh_location_id,
   CASE
     WHEN airport_loc.loc_type_cd = 'AIRPORT' THEN
        acor_location_v1.airport_alt_id
   END                                   AS airport_id,
   CASE
     WHEN airport_loc.loc_type_cd = 'AIRPORT' THEN
        airport_loc.loc_cd
   END                                   AS airport_code
FROM
   inv_inv
   -- part number
   INNER JOIN eqp_part_no part ON
      inv_inv.part_no_db_id = part.part_no_db_id AND
      inv_inv.part_no_id    = part.part_no_id
   -- location
   INNER JOIN inv_loc ON
      inv_inv.loc_db_id = inv_loc.loc_db_id AND
      inv_inv.loc_id    = inv_loc.loc_id
   INNER JOIN acor_location_v1 ON
      inv_loc.alt_id = acor_location_v1.location_id
   -- airport
   INNER JOIN inv_loc airport_loc ON
     acor_location_v1.airport_alt_id = airport_loc.alt_id
   -- authority
   LEFT JOIN org_authority ON
      inv_inv.authority_db_id = org_authority.authority_db_id AND
      inv_inv.authority_id    = org_authority.authority_id
WHERE
   inv_inv.inv_class_db_id = 0 AND
   inv_inv.inv_class_cd    = 'BATCH'
;