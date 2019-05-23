--liquibase formatted sql


--changeSet acor_inv_serialized_tool_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_inv_serialized_tool_v1
AS 
SELECT
   inv_inv.alt_id                        AS inventory_id,
   h_inv.alt_id                          AS h_inventory_id,
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
   inv_inv.batch_no_oem                  AS oem_batch_number,
   inv_inv.lot_oem_tag                   AS oem_lot_tag,
   part.alt_id                           AS part_id,
   part.part_no_oem                      AS part_number,
   -- receipt
   inv_inv.receive_cond_cd               AS receive_condition_code,
   inv_inv.received_dt                   AS received_date,
   org_vendor.alt_id                     AS vendor_id,
   org_vendor.vendor_cd                  AS vendor_code,
   -- warehouse info
   inv_inv.shelf_expiry_dt               AS shelf_expiry_date,
   inv_inv.issued_bool                   AS issued_flag
FROM
   inv_inv
   -- part number
   INNER JOIN eqp_part_no part ON
      inv_inv.part_no_db_id = part.part_no_db_id AND
      inv_inv.part_no_id    = part.part_no_id
   -- part group
   INNER JOIN eqp_part_baseline ON
      part.part_no_db_id = eqp_part_baseline.part_no_db_id AND
      part.part_no_id    = eqp_part_baseline.part_no_id
   INNER JOIN eqp_bom_part ON
      eqp_part_baseline.bom_part_db_id = eqp_bom_part.bom_part_db_id AND
      eqp_part_baseline.bom_part_id    = eqp_bom_part.bom_part_id
   -- assembly
   INNER JOIN eqp_assmbl ON
      eqp_bom_part.assmbl_db_id = eqp_assmbl.assmbl_db_id AND
      eqp_bom_part.assmbl_cd    = eqp_assmbl.assmbl_cd
   -- highest inventory
   INNER JOIN inv_inv h_inv ON
      inv_inv.h_inv_no_db_id = h_inv.inv_no_db_id AND
      inv_inv.h_inv_no_id    = h_inv.inv_no_id
   -- vendor
   LEFT JOIN org_vendor ON
      inv_inv.vendor_db_id = org_vendor.vendor_db_id AND
      inv_inv.vendor_id    = org_vendor.vendor_id
   -- authority
   LEFT JOIN org_authority ON
      inv_inv.authority_db_id = org_authority.authority_db_id AND
      inv_inv.authority_id    = org_authority.authority_id
WHERE
   eqp_assmbl.assmbl_class_db_id = 0 AND
   eqp_assmbl.assmbl_class_cd    = 'TSE'
   AND
   inv_inv.inv_class_db_id = 0 AND
   inv_inv.inv_class_cd    IN ('SER','TRK')
;