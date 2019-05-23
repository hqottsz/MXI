--liquibase formatted sql


--changeSet acor_inv_serial_cntrl_asset_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_inv_serial_cntrl_asset_v1
AS
WITH rvw_inv_inv
AS
   (
     SELECT
        inv_no_db_id,
        inv_no_id,
        nh_inv_no_db_id,
        nh_inv_no_id,
        h_inv_no_db_id,
        h_inv_no_id,
        alt_id,
        part_no_db_id,
        part_no_id,
        inv_class_db_id,
        inv_class_cd,
        prevent_synch_bool,
        orig_assmbl_cd,
        barcode_sdesc,
        serial_no_oem,
        inv_no_sdesc,
        assmbl_db_id,
        assmbl_cd,
        assmbl_bom_id,
        bom_part_db_id,
        bom_part_id,
        assmbl_pos_id,
        assmbl_inv_no_db_id,
        assmbl_inv_no_id,
        manufact_dt,
        install_dt,
        release_number_sdesc,
        release_remarks_ldesc,
        receive_cond_db_id,
        receive_cond_cd,
        inv_cond_cd,
        received_dt,
        owner_db_id,
        owner_id,
        loc_db_id,
        loc_id,
        po_db_id,
        po_id,
        po_line_id,
        batch_no_oem
     FROM
        inv_inv
   )
SELECT
   inv_inv.alt_id                        AS inventory_id,
   h_inv_inv.alt_id                      AS h_inventory_id,
   nh_inv_inv.alt_id                     AS nh_inventory_id,
   --
   eqp_part_no.alt_id                    AS part_id,
   eqp_part_no.part_no_oem               AS part_number,
   eqp_part_no.part_no_sdesc             AS part_number_name,
   --
   inv_inv.serial_no_oem                 AS oem_serial_number,
   inv_inv.batch_no_oem                  AS oem_batch_number,
   --
   CASE
      WHEN inv_inv.assmbl_cd <> NVL(inv_inv.orig_assmbl_cd,inv_inv.assmbl_cd) THEN
         -- installed
         inv_inv.orig_assmbl_cd
      ELSE
         -- loose
        inv_inv.assmbl_cd
   END  AS assmbly_code,
   --
   eqp_assmbl_pos.eqp_pos_cd             AS position_code,
   eqp_assmbl_bom.assmbl_bom_cd,
   --
   inv_inv.manufact_dt                   AS manufact_date,
   eqp_part_no.manufact_cd               AS manufacturer_code,
   inv_inv.received_dt                   AS received_date,
   inv_inv.inv_class_cd,
   --
   inv_owner.owner_cd                    AS owner_code,
   --
   inv_loc.loc_cd                        AS location_code,
   --
   inv_inv.install_dt                    AS install_date,
   inv_inv.barcode_sdesc                 AS barcode,
   --
   inv_inv.receive_cond_cd               AS receive_condition_code,
   ref_receive_cond.desc_sdesc           AS receive_condition_name,
   inv_inv.release_number_sdesc          AS release_number_description,
   inv_inv.release_remarks_ldesc         AS release_remarks,
   eqp_assmbl_bom.logcard_form_cd        AS logcard_form_code,
   inv_inv.inv_cond_cd                   AS inventory_condition,
   --
   po_line.alt_id                        AS po_line_id
FROM
   rvw_inv_inv inv_inv
   -- part
   INNER JOIN eqp_part_no ON
      inv_inv.part_no_db_id = eqp_part_no.part_no_db_id AND
      inv_inv.part_no_id    = eqp_part_no.part_no_id
   -- parent inventory
   LEFT JOIN rvw_inv_inv nh_inv_inv ON
      inv_inv.nh_inv_no_db_id = nh_inv_inv.inv_no_db_id AND
      inv_inv.nh_inv_no_id    = nh_inv_inv.inv_no_id
   -- highest inventory
   LEFT JOIN rvw_inv_inv h_inv_inv ON
      inv_inv.h_inv_no_db_id = h_inv_inv.inv_no_db_id AND
      inv_inv.h_inv_no_id    = h_inv_inv.inv_no_id
   -- owner
   LEFT JOIN inv_owner ON
      inv_inv.owner_db_id = inv_owner.owner_db_id AND
      inv_inv.owner_id    = inv_owner.Owner_id
   -- location
   LEFT JOIN inv_loc ON
      inv_inv.loc_db_id = inv_loc.loc_db_id AND
      inv_inv.loc_id    = inv_loc.loc_id
   -- position
   LEFT JOIN eqp_assmbl_pos ON
      inv_inv.assmbl_db_id  = eqp_assmbl_pos.assmbl_db_Id AND
      inv_inv.assmbl_cd     = eqp_assmbl_pos.assmbl_cd    AND
      inv_inv.assmbl_bom_id = eqp_assmbl_pos.assmbl_bom_Id AND
      inv_inv.assmbl_pos_id = eqp_assmbl_pos.assmbl_pos_Id
   -- config slot
   LEFT JOIN eqp_assmbl_bom ON
      inv_inv.assmbl_db_id  = eqp_assmbl_bom.assmbl_db_Id AND
      inv_inv.assmbl_cd     = eqp_assmbl_bom.assmbl_cd    AND
      inv_inv.assmbl_bom_id = eqp_assmbl_bom.assmbl_bom_Id
   -- po line
   LEFT JOIN po_line ON
      inv_inv.po_db_id   = po_line.po_db_id AND
      inv_inv.po_id      = po_line.po_id    AND
      inv_inv.po_line_id = po_line.po_line_id
   -- receive condition
   LEFT JOIN ref_receive_cond ON
      inv_inv.receive_cond_db_id = ref_receive_cond.receive_cond_db_id AND
      inv_inv.receive_cond_cd    = ref_receive_cond.receive_cond_cd
WHERE
   inv_inv.inv_class_db_id = 0 AND
   inv_inv.inv_class_cd    IN ('TRK','SER','ASSY');