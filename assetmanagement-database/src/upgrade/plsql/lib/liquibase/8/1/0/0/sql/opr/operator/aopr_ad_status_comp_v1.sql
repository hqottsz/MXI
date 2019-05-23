--liquibase formatted sql


--changeSet aopr_ad_status_comp_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_ad_status_comp_v1
AS 
SELECT
   opr_asset_ad_status.ID,
   task_id,
   asset_id               AS inventory_id,
   ref_doc.assembly_code  AS assmbl_code,
   oem_serial_number,
   registration_code,
   ref_doc.task_code      AS task_cd,
   task_name,
   opr_ad_status.ad_status_cd,
   display_code,
   display_desc,
   status_notes,
   completion_dt          AS completion_date
FROM
   opr_asset_ad_status -- custom table - SMO solution
   INNER JOIN opr_ad_status ON
      opr_asset_ad_status.ad_status_cd = opr_ad_status.ad_status_cd
   -- get the actual task id
   INNER JOIN acor_ref_doc_defn_v1 ref_doc ON
      opr_asset_ad_status.ad_id = ref_doc.defn_id
   INNER JOIN acor_inv_tracked_v1  tracked ON
      opr_asset_ad_status.asset_id = tracked.inventory_id;