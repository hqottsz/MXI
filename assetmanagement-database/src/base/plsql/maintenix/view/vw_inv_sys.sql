--liquibase formatted sql


--changeSet vw_inv_sys:1 stripComments:false
CREATE OR REPLACE VIEW vw_inv_sys(
      inv_no_db_id,
      inv_no_id,
      inv_no_sdesc,
      assmbl_db_id,
      assmbl_cd,
      assmbl_bom_id,
      assmbl_pos_id,
      assmbl_bom_cd,
      eqp_pos_cd,
      complete_bool,
      install_dt,
      install_gdt,
      locked_bool,
      reserved_bool,
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
      bitmap_name
   ) AS
   SELECT /*+ rule */
      inv_inv.inv_no_db_id,
      inv_inv.inv_no_id,
      inv_inv.inv_no_sdesc,
      inv_inv.assmbl_db_id,
      inv_inv.assmbl_cd,
      inv_inv.assmbl_bom_id,
      inv_inv.assmbl_pos_id,
      eqp_assmbl_bom.assmbl_bom_cd,
      eqp_assmbl_pos.eqp_pos_cd,
      inv_inv.complete_bool,
      inv_inv.install_dt,
      inv_inv.install_gdt,
      inv_inv.locked_bool,
      inv_inv.reserved_bool,
      inv_inv.assmbl_inv_no_db_id,
      inv_inv.assmbl_inv_no_id,
      assmbl_inv_inv.inv_no_sdesc,
      inv_inv.nh_inv_no_db_id,
      inv_inv.nh_inv_no_id,
      nh_inv_inv.inv_no_sdesc,
      inv_inv.h_inv_no_db_id,
      inv_inv.h_inv_no_id,
      h_inv_inv.inv_no_sdesc,
      0,
      4,
      'phantom.bmp'
 FROM inv_inv,
      eqp_assmbl_bom,
      eqp_assmbl_pos,
      inv_inv assmbl_inv_inv,
      inv_inv nh_inv_inv,
      inv_inv h_inv_inv
WHERE inv_inv.inv_class_db_id = 0 AND
      inv_inv.inv_class_cd    = 'SYS'
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
      nh_inv_inv.inv_no_db_id = inv_inv.nh_inv_no_db_id AND
      nh_inv_inv.inv_no_id    = inv_inv.nh_inv_no_id
      AND
      h_inv_inv.inv_no_db_id = inv_inv.h_inv_no_db_id AND
      h_inv_inv.inv_no_id    = inv_inv.h_inv_no_id
;