--liquibase formatted sql


--changeSet acor_rbl_subassy_type_v1:1 stripComments:false
CREATE OR REPLACE VIEW ACOR_RBL_SUBASSY_TYPE_V1 AS
SELECT
  acft_eng_assy.fleet_type,
  acft_eng_assy.assmbl_cd,
  acft_eng_assy.assmbl_class_cd
FROM
   (
      SELECT
         h_inv.assmbl_cd fleet_type,
         inv_inv.assmbl_cd,
         eqp_assmbl.assmbl_class_cd
      FROM
         inv_inv
        INNER JOIN eqp_assmbl ON
            inv_inv.assmbl_db_id = eqp_assmbl.assmbl_db_id AND
            inv_inv.assmbl_cd    = eqp_assmbl.assmbl_cd
            AND
            eqp_assmbl.assmbl_class_cd <> 'ACFT'
         INNER JOIN inv_inv h_inv ON
            inv_inv.h_inv_no_db_id = h_inv.inv_no_db_id AND
            inv_inv.h_inv_no_id    = h_inv.inv_no_id
            AND
            h_inv.inv_class_cd = 'ACFT'
      GROUP BY
         h_inv.assmbl_cd,
         inv_inv.assmbl_cd,
         eqp_assmbl.assmbl_class_cd
   ) acft_eng_assy
   INNER JOIN (
                  SELECT
                     assmbl_cd,
                     pos_ct,
                     CASE
                        WHEN REGEXP_INSTR(assmbl_bom_cd,'ENG|ENGINE|72-') > 0 THEN
                          'ENG'
                        WHEN REGEXP_INSTR(assmbl_bom_cd,'APU|POWER|49-') > 0 THEN
                          'APU'
                     END assmbl_class_cd,
                     bom_class_cd
                  FROM
                     eqp_assmbl_bom
                  WHERE
                     bom_class_cd = 'SUBASSY'
              ) assy_qty ON
      acft_eng_assy.fleet_type      = assy_qty.assmbl_cd AND
      acft_eng_assy.assmbl_class_cd = assy_qty.assmbl_class_cd;