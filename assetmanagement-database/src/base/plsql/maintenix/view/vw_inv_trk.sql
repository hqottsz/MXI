--liquibase formatted sql


--changeSet vw_inv_trk:1 stripComments:false
CREATE OR REPLACE VIEW vw_inv_trk(
      inv_no_db_id,
      inv_no_id,
      inv_no_sdesc,
      inv_cond_db_id,
      inv_cond_cd,
      inv_note,
      part_no_db_id,
      part_no_id,
      part_no_sdesc,
      part_no_oem,
      authority_db_id,
      authority_id,
      authority_cd,
      loc_db_id,
      loc_id,
      loc_cd,
      owner_db_id,
      owner_id,
      owner_cd,
      icn_no_sdesc,
      manufact_dt,
      lot_no_oem,
      po_ref_sdesc,
      received_dt,
      severity_cd,
      used_bool,
      vendor_db_id,
      vendor_id,
      serial_no_oem,
      assmbl_db_id,
      assmbl_cd,
      assmbl_bom_id,
      assmbl_pos_id,
      assmbl_bom_cd,
      reserved_bool,
      eqp_pos_cd,
      complete_bool,
      install_dt,
      install_gdt,
      locked_bool,
      assmbl_inv_no_db_id,
      assmbl_inv_no_id,
      assmbl_inv_no_sdesc,
      nh_inv_no_db_id,
      nh_inv_no_id,
      nh_inv_no_sdesc,
      h_inv_no_db_id,
      h_inv_no_id,
      h_inv_no_sdesc,
      bitmap_db_id,
      bitmap_tag,
      bitmap_name,
      srv_bool
   ) AS
   SELECT /*+ rule */
      inv_inv.inv_no_db_id,
      inv_inv.inv_no_id,
      inv_inv.inv_no_sdesc,
      inv_inv.inv_cond_db_id,
      inv_inv.inv_cond_cd,
      inv_inv.note,
      inv_inv.part_no_db_id,
      inv_inv.part_no_id,
      eqp_part_no.part_no_sdesc,
      eqp_part_no.part_no_oem,
      inv_inv.authority_db_id,
      inv_inv.authority_id,
      org_authority.owner_cd,
      inv_inv.loc_db_id,
      inv_inv.loc_id,
      inv_loc.loc_cd,
      inv_inv.owner_db_id,
      inv_inv.owner_id,
      inv_owner.owner_cd,
      inv_inv.icn_no_sdesc,
      inv_inv.manufact_dt,
      inv_inv.lot_oem_tag,
      inv_inv.po_ref_sdesc,
      inv_inv.received_dt,
      inv_inv.severity_cd,
      inv_inv.used_bool,
      inv_inv.vendor_db_id,
      inv_inv.vendor_id,
      inv_inv.serial_no_oem,
      inv_inv.assmbl_db_id,
      inv_inv.assmbl_cd,
      inv_inv.assmbl_bom_id,
      inv_inv.assmbl_pos_id,
      inv_inv.reserved_bool,
      eqp_assmbl_bom.assmbl_bom_cd,
      eqp_assmbl_pos.eqp_pos_cd,
      inv_inv.complete_bool,
      inv_inv.install_dt,
      inv_inv.install_gdt,
      inv_inv.locked_bool,
      inv_inv.assmbl_inv_no_db_id,
      inv_inv.assmbl_inv_no_id,
      assmbl_inv_inv.inv_no_sdesc,
      inv_inv.nh_inv_no_db_id,
      inv_inv.nh_inv_no_id,
      nh_inv_inv.inv_no_sdesc,
      inv_inv.h_inv_no_db_id,
      inv_inv.h_inv_no_id,
      h_inv_inv.inv_no_sdesc,
      ref_bitmap.bitmap_db_id,
      ref_bitmap.bitmap_tag,
      ref_bitmap.bitmap_name,
      ref_inv_cond.srv_bool
 FROM inv_inv,
      eqp_part_no,
      inv_owner,
      inv_owner org_authority,
      inv_loc,
      eqp_assmbl_bom,
      eqp_assmbl_pos,
      inv_inv assmbl_inv_inv,
      inv_inv nh_inv_inv,
      inv_inv h_inv_inv,
      ref_bitmap,
      ref_inv_cond
WHERE inv_inv.inv_class_db_id = 0 AND
      inv_inv.inv_class_cd    = 'TRK'
      AND
      eqp_part_no.part_no_db_id = inv_inv.part_no_db_id AND
      eqp_part_no.part_no_id    = inv_inv.part_no_id
      AND
      inv_owner.owner_db_id (+)= inv_inv.owner_db_id AND
      inv_owner.owner_id    (+)= inv_inv.owner_id
      AND
      org_authority.owner_db_id (+)= inv_inv.authority_db_id AND
      org_authority.owner_id    (+)= inv_inv.authority_id
      AND
      inv_loc.loc_db_id (+)= inv_inv.loc_db_id AND
      inv_loc.loc_id    (+)= inv_inv.loc_id
      AND
      eqp_assmbl_bom.assmbl_db_id  = inv_inv.assmbl_db_id AND
      eqp_assmbl_bom.assmbl_cd     = inv_inv.assmbl_cd AND
      eqp_assmbl_bom.assmbl_bom_id = inv_inv.assmbl_bom_id
      AND
      eqp_assmbl_pos.assmbl_db_id  = inv_inv.assmbl_db_id AND
      eqp_assmbl_pos.assmbl_cd     = inv_inv.assmbl_cd AND
      eqp_assmbl_pos.assmbl_bom_id = inv_inv.assmbl_bom_id AND
      eqp_assmbl_pos.assmbl_pos_id = inv_inv.assmbl_pos_id
      AND
      assmbl_inv_inv.inv_no_db_id (+)= inv_inv.assmbl_inv_no_db_id AND
      assmbl_inv_inv.inv_no_id    (+)= inv_inv.assmbl_inv_no_id
      AND
      nh_inv_inv.inv_no_db_id (+)= inv_inv.nh_inv_no_db_id AND
      nh_inv_inv.inv_no_id    (+)= inv_inv.nh_inv_no_id
      AND
      h_inv_inv.inv_no_db_id = inv_inv.h_inv_no_db_id AND
      h_inv_inv.inv_no_id    = inv_inv.h_inv_no_id
      AND
      ref_bitmap.bitmap_db_id = eqp_part_no.bitmap_db_id AND
      ref_bitmap.bitmap_tag   = eqp_part_no.bitmap_tag
      AND
      ref_inv_cond.inv_cond_db_id = inv_inv.inv_cond_db_id AND
      ref_inv_cond.inv_cond_cd    = inv_inv.inv_cond_cd
;