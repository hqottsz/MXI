--liquibase formatted sql


--changeSet acor_inv_aircraft_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_inv_aircraft_v1
AS 
SELECT
   inv_inv.alt_id                               AS aircraft_id,
   -- aircraft Info
   fc_model.alt_id                              AS forecast_id,
   fnc_account.alt_id                           AS account_id,
   inv_ac_reg.etops_bool                        AS etops_flag,
   inv_ac_reg.inv_capability_cd                 AS capability_code,
   inv_ac_reg.airworth_cd,
   --- registration
   inv_ac_reg.country_cd                        AS country_code,
   inv_ac_reg.fin_no_cd                         AS fin_number,
   inv_ac_reg.line_no_oem                       AS oem_line_number,
   inv_ac_reg.var_no_oem                        AS oem_var_number,
   inv_ac_reg.ac_reg_cd                         AS registration_code,
   inv_ac_reg.reg_body_cd                       AS regulatory_body,
   inv_ac_reg.inv_oper_cd,
   -- assembly_info
   eqp_assmbl_bom.assmbl_cd                     AS assmbly_code,
   eqp_assmbl_bom.alt_id                        AS bom_id,
  --- asset info
   org_carrier.alt_id                           AS operator_id,
   org_carrier.carrier_cd                       AS operator_code,
   org_org.org_sdesc                            AS organization_name,
   inv_inv.appl_eff_cd                          AS applicability_range,
   inv_inv.locked_bool                          AS locked_flag,
   inv_inv.orig_assmbl_cd                       AS original_assmbly_code,
   inv_inv.prevent_synch_bool                   AS synchronization_flag,
   inv_inv.mod_status_note                      AS modification_status_note,
   org_authority.alt_id                         AS authority_id,
   org_authority.authority_cd                   AS authority_code,
   --- identity
   inv_inv.barcode_sdesc                        AS barcode,
   inv_inv.inv_no_sdesc                         AS description,
   --- manufacture
   inv_inv.icn_no_sdesc                         AS icn_number,
   inv_inv.manufact_dt                          AS manufact_date,
   inv_inv.install_dt                           AS install_date,
   inv_inv.batch_no_oem                         AS oem_batch_number,
   inv_inv.lot_oem_tag                          AS oem_lot_tag,
   part.alt_id                                  AS part_id,
   part.part_no_oem                             AS part_number,
   part.manufact_cd                             AS manufacturer_code,
   part.part_no_sdesc                           AS part_name,
   --- receipt
   inv_inv.receive_cond_cd                      AS receive_condition_code,
   inv_inv.received_dt                          AS received_date,
   org_vendor.alt_id                            AS vendor_id,
   org_vendor.vendor_cd                         AS vendor_code,
   --- release
   inv_inv.release_dt                           AS release_date,
   inv_inv.release_number_sdesc                 AS release_number_description,
   inv_inv.release_remarks_ldesc                AS release_remarks,
   --- serialized info
   inv_owner.alt_id                             AS owner_id,
   inv_owner.owner_cd                           AS owner_code,
   inv_inv.inv_cond_cd                          AS condition_code,
   inv_inv.serial_no_oem                        AS oem_serial_number,
   inv_loc.alt_id                               AS location_id,
   inv_loc.loc_cd                               AS location_code,
   -- po_line identity
   po_line.alt_id                               AS po_line_id
FROM
   inv_inv
   -- aircraft
   INNER JOIN inv_ac_reg ON
      inv_inv.inv_no_db_id = inv_ac_reg.inv_no_db_id AND
      inv_inv.inv_no_id    = inv_ac_reg.inv_no_id
   -- location
   INNER JOIN inv_loc ON
      inv_inv.loc_db_id = inv_loc.loc_db_id AND
      inv_inv.loc_id    = inv_loc.loc_id
   -- owner
   INNER JOIN inv_owner ON
      inv_inv.owner_db_id = inv_owner.owner_db_id AND
      inv_inv.owner_id    = inv_owner.owner_id
   -- operator
   LEFT JOIN org_carrier ON
      inv_inv.carrier_db_id = org_carrier.carrier_db_id AND
      inv_inv.carrier_id    = org_carrier.carrier_id
   -- organization
   LEFT JOIN org_org ON
      org_carrier.org_db_id = org_org.org_db_id AND
      org_carrier.org_id    = org_org.org_id      
   -- authority
   LEFT JOIN org_authority ON
      inv_inv.authority_db_id = org_authority.authority_db_id AND
      inv_inv.authority_id    = org_authority.authority_id
   LEFT JOIN org_vendor ON
      inv_inv.vendor_db_id = org_vendor.vendor_db_id AND
      inv_inv.vendor_id    = org_vendor.vendor_id
   -- assembly
   INNER JOIN eqp_assmbl_bom ON
      inv_inv.assmbl_db_id  = eqp_assmbl_bom.assmbl_db_id AND
      inv_inv.assmbl_cd     = eqp_assmbl_bom.assmbl_cd    AND
      inv_inv.assmbl_bom_Id = eqp_assmbl_bom.assmbl_bom_id
   -- part number
   INNER JOIN eqp_part_no part ON
      inv_inv.part_no_db_id = part.part_no_db_id AND
      inv_inv.part_no_id    = part.part_no_id
   -- forecast model
   INNER JOIN fc_model ON
      inv_ac_reg.forecast_model_db_id = fc_model.model_db_id AND
      inv_ac_reg.forecast_model_id    = fc_model.model_id
   -- po line
   LEFT JOIN po_line ON
      inv_inv.po_db_id   = po_line.po_db_id AND
      inv_inv.po_id      = po_line.po_id    AND
      inv_inv.po_line_id = po_line.po_line_id
   -- issue to account
   INNER JOIN fnc_account ON
      inv_ac_reg.issue_account_db_id = fnc_account.account_db_id AND
      inv_ac_reg.Issue_Account_Id    = fnc_account.account_id
;