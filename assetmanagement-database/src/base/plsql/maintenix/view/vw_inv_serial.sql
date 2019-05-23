--liquibase formatted sql


--changeSet vw_inv_serial:1 stripComments:false
CREATE OR REPLACE VIEW vw_inv_serial(
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
      reserved_bool,
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
      inv_inv.reserved_bool,
      ref_bitmap.bitmap_db_id,
      ref_bitmap.bitmap_tag,
      ref_bitmap.bitmap_name,
      ref_inv_cond.srv_bool
 FROM inv_inv,
      eqp_part_no,
      inv_owner,
      inv_owner org_authority,
      inv_loc,
      ref_bitmap,
      ref_inv_cond
WHERE inv_inv.inv_class_db_id = 0 AND
      inv_inv.inv_class_cd    = 'SER'
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
      ref_bitmap.bitmap_db_id = eqp_part_no.bitmap_db_id AND
      ref_bitmap.bitmap_tag   = eqp_part_no.bitmap_tag
      AND
      ref_inv_cond.inv_cond_db_id = inv_inv.inv_cond_db_id AND
      ref_inv_cond.inv_cond_cd    = inv_inv.inv_cond_cd
;