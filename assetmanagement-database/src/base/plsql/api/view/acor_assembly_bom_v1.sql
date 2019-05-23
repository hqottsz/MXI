--liquibase formatted sql


--changeSet acor_assembly_bom_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_assembly_bom_v1
AS
SELECT
   alt_id               AS ID,
   assmbl_cd            AS assembly_code,
   nh_assmbl_cd         AS nh_assembly_code,
   bom_class_cd         AS class_code,
   logcard_form_cd      AS logcard_form_code,
   pos_ct               AS position_count,
   assmbl_bom_cd        AS config_slot_code,
   assmbl_bom_func_cd   AS function_code,
   assmbl_bom_zone_cd   AS zone_cd,
   assmbl_bom_name      AS config_slot_name,
   mandatory_bool       AS mandatory_flag,
   cfg_slot_status_cd   AS config_slot_status_cd,
   rvsm_bool            AS rvsm_flag,
   software_bool        AS software_flag,
   etops_bool           AS etops_flag
FROM
   eqp_assmbl_bom
;