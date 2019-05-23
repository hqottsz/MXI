--liquibase formatted sql


--changeSet acor_inv_batch_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_inv_batch_v1
AS 
SELECT
   inv_inv.alt_id                        AS inventory_id,
   -- asset info
   inv_inv.locked_bool                   AS locked_flag,
   org_authority.alt_id                  AS authority_id,
   org_authority.authority_cd            AS authority_code,
   -- batch info
   inv_inv.batch_no_oem                  AS oem_batch_number,
   inv_inv.bin_qt                        AS bin_quantity,
   -- identity
   inv_inv.barcode_sdesc                 AS barcode,
   inv_inv.inv_no_sdesc                  AS description,
   -- manufacture
   inv_inv.icn_no_sdesc                  AS icn_number,
   inv_inv.manufact_dt                   AS manufact_date,
   inv_inv.received_dt                   AS received_date,
   inv_inv.lot_oem_tag                   AS oem_lot_tag,
   inv_inv.serial_no_oem                 AS oem_serial_number,
   part.alt_id                           AS part_id,
   part.part_no_oem                      AS part_number,
   part.manufact_cd                      AS manufacturer_code,
   inv_inv.release_remarks_ldesc         AS release_remarks,
   -- warehouse info
   inv_inv.shelf_expiry_dt               AS shelf_expiry_date,
   inv_inv.issued_bool                   AS issued_flag,
   -- po_line identity
   po_line.alt_id                        AS po_line_id,
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
   -- po line
   LEFT JOIN po_line ON
      inv_inv.po_db_id   = po_line.po_db_id AND
      inv_inv.po_id      = po_line.po_id    AND
      inv_inv.po_line_id = po_line.po_line_id
WHERE
   inv_inv.inv_class_db_id = 0 AND
   inv_inv.inv_class_cd    = 'BATCH'
   AND 
   NOT EXISTS ( -- exclude tools
                SELECT
                   1
                FROM 
                   eqp_assmbl
                   INNER JOIN eqp_bom_part ON 
                      eqp_assmbl.assmbl_db_id = eqp_bom_part.assmbl_db_id AND
                      eqp_assmbl.assmbl_cd    = eqp_bom_part.assmbl_cd
                   INNER JOIN eqp_part_baseline ON 
                      eqp_bom_part.bom_part_db_id = eqp_part_baseline.bom_part_db_id AND
                      eqp_bom_part.bom_part_id    = eqp_part_baseline.bom_part_id
                WHERE
                   eqp_assmbl.assmbl_class_cd = 'TSE'
                   AND --
                   eqp_part_baseline.part_no_db_id = part.part_no_db_id AND
                   eqp_part_baseline.part_no_id    = part.part_no_id
              )
;   