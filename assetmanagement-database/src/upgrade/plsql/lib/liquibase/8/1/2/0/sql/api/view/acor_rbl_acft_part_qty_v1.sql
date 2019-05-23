--liquibase formatted sql


--changeSet acor_rbl_acft_part_qty_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_rbl_acft_part_qty_v1 
AS
WITH
rvw_ser_act_qty
AS
   ( -- serialized actuals count
     SELECT
        assmbl_cd,
        -- manufact_cd,
        part_no_oem,
        MIN(qty) min_qty,
        MAX(qty) max_qty,
        ROUND(AVG(qty)) avg_qty
     FROM
        (
           SELECT
              h_inv.assmbl_cd,
              ac_reg_cd,
              manufact_cd,
              part_no_oem,
              COUNT(1) qty
           FROM
              inv_inv
              INNER JOIN eqp_part_no ON
                 inv_inv.part_no_db_id = eqp_part_no.part_no_db_id AND
                 inv_inv.part_no_id    = eqp_part_no.part_no_id
                 AND
                 eqp_part_no.inv_class_cd = 'SER'
              INNER JOIN inv_ac_reg ON
                 inv_inv.h_inv_no_db_id = inv_ac_reg.inv_no_db_id AND
                 inv_inv.h_inv_no_id    = inv_ac_reg.inv_no_id
              INNER JOIN inv_inv h_inv ON
                 inv_ac_reg.inv_no_db_id = h_inv.inv_no_db_id AND
                 inv_ac_reg.inv_no_id    = h_inv.inv_no_id
           GROUP BY
              h_inv.assmbl_cd,
              ac_reg_cd,
              manufact_cd,
              part_no_oem
        )
     GROUP BY
        assmbl_cd,
        -- manufact_cd,
        part_no_oem
   ),
rvw_bsl_part_qty
AS
   ( -- baseline count
      SELECT
         eqp_bom_part.alt_id      AS part_group_id,
         eqp_part_no.alt_id       AS part_id,
          CASE
            WHEN eqp_assmbl.assmbl_class_cd = 'ACFT' THEN
               eqp_assmbl_bom.assmbl_cd
            ELSE
               assy_qty.fleet_type
          END assmbl_cd,
         eqp_part_no.manufact_cd,
         eqp_part_no.part_no_oem,
         eqp_part_no.inv_class_cd,
         eqp_bom_part.bom_part_cd,
         eqp_bom_part.bom_part_name,
         SUM(CASE
               WHEN eqp_assmbl.assmbl_class_cd = 'ACFT' THEN
                  (eqp_assmbl_bom.pos_ct * NVL(nh_assmbl_bom.pos_ct,1))
               ELSE
                  (eqp_assmbl_bom.pos_ct * NVL(assy_qty.pos_ct,1)) 
             END) OVER ( PARTITION BY
                            eqp_assmbl_bom.assmbl_cd,
                            -- eqp_part_no.manufact_cd,
                            eqp_part_no.part_no_oem
                       )  part_qty
      FROM
         eqp_assmbl_bom
         -- assembly
         INNER JOIN eqp_assmbl ON
            eqp_assmbl_bom.assmbl_db_id = eqp_assmbl.assmbl_db_id AND
            eqp_assmbl_bom.assmbl_cd    = eqp_assmbl.assmbl_cd
         LEFT JOIN (
                        SELECT
                          acft_eng_assy.fleet_type,
                          acft_eng_assy.assmbl_cd,
                          acft_eng_assy.assmbl_class_cd,
                          assy_qty.pos_ct
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
                              acft_eng_assy.assmbl_class_cd = assy_qty.assmbl_class_cd   
                   ) assy_qty ON 
            eqp_assmbl.assmbl_cd       = assy_qty.assmbl_cd AND
            eqp_assmbl.assmbl_class_cd =  assy_qty.assmbl_class_cd
         -- part group
         INNER JOIN eqp_bom_part ON
            eqp_assmbl_bom.assmbl_db_id  = eqp_bom_part.assmbl_db_id AND
            eqp_assmbl_bom.assmbl_cd     = eqp_bom_part.assmbl_cd AND
            eqp_assmbl_bom.assmbl_bom_id = eqp_bom_part.assmbl_bom_id
         INNER JOIN eqp_part_baseline ON
            eqp_bom_part.bom_part_db_id = eqp_part_baseline.bom_part_db_id AND
            eqp_bom_part.bom_part_id    = eqp_part_baseline.bom_part_id
         INNER JOIN eqp_part_no ON
            eqp_part_baseline.part_no_db_id = eqp_part_no.part_no_db_id AND
            eqp_part_baseline.part_no_id    = eqp_part_no.part_no_id
        -- nh highest position count
        LEFT JOIN eqp_assmbl_bom nh_assmbl_bom ON
           eqp_assmbl_bom.nh_assmbl_db_id  = nh_assmbl_bom.assmbl_db_id AND
           eqp_assmbl_bom.nh_assmbl_cd     = nh_assmbl_bom.assmbl_cd AND
           eqp_assmbl_bom.nh_assmbl_bom_id = nh_assmbl_bom.assmbl_bom_id
   )
SELECT
   rvw_bsl_part_qty.part_id,
   rvw_bsl_part_qty.part_group_id,
   rvw_bsl_part_qty.assmbl_cd,
   rvw_bsl_part_qty.manufact_cd,
   rvw_bsl_part_qty.part_no_oem,
   rvw_bsl_part_qty.inv_class_cd,
   rvw_bsl_part_qty.bom_part_cd,
   rvw_bsl_part_qty.bom_part_name,
   CASE
      WHEN rvw_bsl_part_qty.inv_class_cd = 'SER' THEN
         NVL(rvw_ser_act_qty.avg_qty,0)
      ELSE
        rvw_bsl_part_qty.part_qty
   END  AS acft_part_qty
FROM
  rvw_bsl_part_qty
  -- serialized actuals count
  LEFT JOIN rvw_ser_act_qty ON
     rvw_bsl_part_qty.assmbl_cd   = rvw_ser_act_qty.assmbl_cd AND
     -- rvw_bsl_part_qty.manufact_cd = rvw_ser_act_qty.manufact_cd AND
     rvw_bsl_part_qty.part_no_oem = rvw_ser_act_qty.part_no_oem
;  