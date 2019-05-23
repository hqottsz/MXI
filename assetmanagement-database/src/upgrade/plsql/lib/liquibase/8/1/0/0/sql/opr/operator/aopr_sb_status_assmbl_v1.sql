--liquibase formatted sql


--changeSet aopr_sb_status_assmbl_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_sb_status_assmbl_v1
AS 
SELECT
   opr_asset_sb_status.Id,
   task_id,
   asset_id              AS inventory_id,
   ref_doc.assembly_code AS assmbl_code,
   oem_serial_number,
   registration_code,
   task_code              AS task_cd,
   task_name,
   opr_sb_status.sb_status_cd,
   display_code,
   display_desc,
   status_notes,
   completion_dt      AS completion_date
FROM
   opr_asset_sb_status -- custom table - SMO solution
   INNER JOIN opr_sb_status ON
      opr_asset_sb_status.sb_status_cd = opr_sb_status.sb_status_cd
   -- get the actual task id
   INNER JOIN acor_ref_doc_defn_v1 ref_doc ON
      opr_asset_sb_status.sb_id = ref_doc.defn_id
   INNER JOIN acor_inv_assembly_v1 assembly ON
      opr_asset_sb_status.asset_Id = assembly.inventory_id;