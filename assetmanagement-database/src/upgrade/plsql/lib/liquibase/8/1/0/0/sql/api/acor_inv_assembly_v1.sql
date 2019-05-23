--liquibase formatted sql


--changeSet acor_inv_assembly_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_inv_assembly_v1
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
        appl_eff_cd,
        prevent_synch_bool,
        mod_status_note,
        barcode_sdesc,
        serial_no_oem,
        inv_no_sdesc,
        inv_class_db_id,
        inv_class_cd,
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
        received_dt,
        owner_db_id,
        owner_id,
        loc_db_id,
        loc_id,
        vendor_db_id,
        vendor_id,
        authority_db_id,
        authority_id,
        carrier_db_id,
        carrier_id,
        inv_cond_db_id,
        inv_cond_cd,
        po_db_id,
        po_id,
        po_line_id
     FROM
        inv_inv
   ),
rvw_eqp_assmbl_bom
AS
   (
     SELECT
        assmbl_db_id,
        assmbl_cd,
        assmbl_bom_id,
        nh_assmbl_db_id,
        nh_assmbl_cd,
        nh_assmbl_bom_id,
        bom_class_db_id,
        bom_class_cd,
        assmbl_bom_cd,
        logcard_form_db_id,
        logcard_form_cd
     FROM
        eqp_assmbl_bom
   ),
rvw_eqp_bom_part
AS
  (
    SELECT
       assmbl_db_id,
       assmbl_cd,
       assmbl_bom_id,
       bom_part_db_id,
       bom_part_id,
       bom_part_cd
    FROM
       eqp_bom_part
  )
SELECT
   inv_inv.alt_id                             AS inventory_id,
   h_inv_inv.alt_id                           AS h_inventory_id,
   nh_inv_inv.alt_id                          AS nh_inventory_id,
   -- assembly info
   eqp_assmbl_bom.assmbl_bom_cd,
   eqp_assmbl_bom.bom_class_cd,
   h_eqp_assmbl_bom.Assmbl_Bom_Cd              AS h_assmbl_bom_cd,
   h_eqp_assmbl_bom.bom_class_cd               AS h_bom_class_cd,
   h_eqp_bom_part.bom_part_cd                  AS root_bom_part_cd,
   --
   org_carrier.alt_id                          AS operator_id,
   org_carrier.carrier_cd                      AS operator_code,
   -- asset info
   inv_inv.appl_eff_cd                         AS applicability_range,
   inv_inv.prevent_synch_bool                  AS synchronization_flag,
   inv_inv.mod_status_note                     AS modification_status_note,
   org_authority.alt_id                        AS authority_id,
   org_authority.authority_cd                  AS authority_code,
   -- identity
   inv_inv.barcode_sdesc                       AS barcode,
   inv_inv.inv_no_sdesc                        AS description,
   inv_inv.inv_class_cd,
   -- installation (Tracked,Assembly)
   inv_inv.assmbl_cd                           AS assmbly_code,
   eqp_bom_part.bom_part_cd                    AS part_group_code,
   acft_inv.alt_id                             AS aircraft_id,
   inv_ac_reg.ac_reg_cd                        AS registration_code,
   CASE
     WHEN inv_inv.inv_class_cd = 'TRK' THEN
        nh_inv_inv.alt_id
      ELSE
         NULL
   END parent_asset_id,
   CASE
     WHEN inv_inv.inv_class_cd = 'SYS' THEN
        nh_inv_inv.alt_id
     ELSE
        NULL
   END parent_system_id,
   -- manufacturer
   inv_inv.manufact_dt                         AS manufact_date,
   eqp_part_no.alt_id                          AS part_id,
   eqp_part_no.manufact_cd                     AS manufacturer_code,
   eqp_part_no.part_no_oem                     AS part_number,
   eqp_part_no.part_no_sdesc                   AS part_number_name,
   inv_inv.install_dt                          AS install_date,
   inv_inv.release_number_sdesc                AS release_number_description,
   inv_inv.release_remarks_ldesc               AS release_remarks,
   -- receipt
   inv_inv.receive_cond_cd                     AS receive_condition_code,
   ref_receive_cond.desc_sdesc                 AS receive_condition_name,   
   inv_inv.received_dt                         AS received_date,
   org_vendor.alt_id                           AS vendor_id,
   org_vendor.vendor_cd                        AS vendor_code,
   -- serialized info
   inv_owner.alt_id                            AS owner_id,
   inv_owner.owner_cd                          AS owner_code,
   inv_loc.alt_id                              AS location_id,
   inv_loc.loc_cd                              AS location_code,
   inv_inv.serial_no_oem                       AS oem_serial_number,
   eqp_assmbl_pos.eqp_pos_cd                   AS position_code,
   --
   eqp_assmbl_bom.logcard_form_cd              AS logcard_form_code,
   inv_inv.inv_cond_cd                         AS inventory_condition,
   -- po_line identity
   po_line.alt_id                              AS po_line_id
FROM
   rvw_inv_inv inv_inv
   -- aircraft
   LEFT JOIN inv_ac_reg ON
      inv_inv.h_inv_no_db_id = inv_ac_reg.inv_no_db_id AND
      inv_inv.h_inv_no_id    = inv_ac_reg.inv_no_id
   LEFT JOIN inv_inv acft_inv ON
      inv_ac_reg.inv_no_db_id = acft_inv.inv_no_db_id AND
      inv_ac_reg.inv_no_id    = acft_inv.inv_no_id
   -- location
   LEFT JOIN inv_loc ON
      inv_inv.loc_db_id = inv_loc.loc_db_id  AND
      inv_inv.loc_id    = inv_loc.loc_id
   -- owner
   LEFT JOIN inv_owner ON
      inv_inv.owner_db_id = inv_owner.owner_db_id AND
      inv_inv.owner_id    = inv_owner.owner_id
   -- vendor
   LEFT JOIN org_vendor ON
      inv_inv.vendor_db_id = org_vendor.vendor_db_id AND
      inv_inv.vendor_id    = org_vendor.vendor_id
   -- authority
   LEFT JOIN org_authority ON
      inv_inv.authority_db_id = org_authority.authority_db_id AND
      inv_inv.authority_id    = org_authority.authority_id
   -- operator
   LEFT JOIN org_carrier ON
      inv_inv.carrier_db_id = org_carrier.carrier_db_id AND
      inv_inv.carrier_id    = org_carrier.carrier_id
   -- part number
   LEFT JOIN eqp_part_no ON
      inv_inv.part_no_db_id = eqp_part_no.part_no_db_id AND
      inv_inv.part_no_id    = eqp_part_no.part_no_id
   -- assembly
   INNER JOIN rvw_eqp_assmbl_bom eqp_assmbl_bom ON
      inv_inv.assmbl_db_id  = eqp_assmbl_bom.assmbl_db_id AND
      inv_inv.assmbl_cd     = eqp_assmbl_bom.assmbl_cd    AND
      inv_inv.assmbl_bom_Id = eqp_assmbl_bom.assmbl_bom_id
   -- position
   LEFT JOIN eqp_assmbl_pos ON
      inv_inv.Assmbl_db_id  = eqp_assmbl_pos.assmbl_db_id  AND
      inv_inv.assmbl_cd     = eqp_assmbl_pos.assmbl_cd     AND
      inv_inv.Assmbl_Bom_Id = eqp_assmbl_pos.assmbl_bom_id AND
      inv_inv.Assmbl_Pos_Id = eqp_assmbl_pos.assmbl_pos_id
   -- part group
   LEFT JOIN rvw_eqp_bom_part eqp_bom_part ON
      inv_inv.bom_part_db_id = eqp_bom_part.bom_part_db_id AND
      inv_inv.bom_part_id    = eqp_bom_part.bom_part_id
   -- parent inventory
   INNER JOIN rvw_inv_inv nh_inv_inv ON
      inv_inv.nh_inv_no_db_id = nh_inv_inv.inv_no_db_id AND
      inv_inv.nh_inv_no_id    = nh_inv_inv.inv_no_id
   -- root inventory
   INNER JOIN rvw_inv_inv h_inv_inv ON
      inv_inv.h_inv_no_db_id = h_inv_inv.Inv_No_Db_Id AND
      inv_inv.h_inv_no_id    = h_inv_inv.inv_no_id
   -- root assembly
   INNER JOIN rvw_eqp_assmbl_bom h_eqp_assmbl_bom ON
      h_inv_inv.Assmbl_Db_Id  = h_eqp_assmbl_bom.assmbl_db_id AND
      h_inv_inv.assmbl_cd     = h_eqp_assmbl_bom.assmbl_cd    AND
      h_inv_inv.Assmbl_Bom_Id = h_eqp_assmbl_bom.assmbl_bom_id
   -- root part group
   LEFT JOIN rvw_eqp_bom_part h_eqp_bom_part ON
      h_inv_inv.bom_part_db_id  = h_eqp_bom_part.bom_part_db_id AND
      h_inv_inv.bom_part_id     = h_eqp_bom_part.bom_part_id
   -- po line
   LEFT JOIN po_line ON
      inv_inv.po_db_id   = po_line.po_db_id AND
      inv_inv.po_id      = po_line.po_id AND
      inv_inv.po_line_id = po_line.po_line_id
   -- receive condition
   LEFT JOIN ref_receive_cond ON
      inv_inv.receive_cond_db_id = ref_receive_cond.receive_cond_db_id AND
      inv_inv.receive_cond_cd    = ref_receive_cond.receive_cond_cd         
WHERE
    inv_inv.inv_class_db_id = 0 AND
    inv_inv.inv_class_cd    IN ('SYS','ASSY')
;