--liquibase formatted sql


--changeSet aopr_ad_status_acft_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_ad_status_acft_v1
AS 
SELECT
   opr_asset_ad_status.ID,
   ref_doc.task_id,
   asset_id             AS inv_id,
   assembly_code        AS assmbl_code,
   ref_doc.status_code  AS ad_taskdefn_status,
   oem_serial_number,
   registration_code,
   task_code            AS task_cd,
   task_name,
   opr_ad_status.ad_status_cd,
   display_code,
   display_desc,
   status_notes,
   completion_dt      AS completion_date
FROM
   opr_asset_ad_status -- custom table - SMO solution
   INNER JOIN opr_ad_status ON
      opr_asset_ad_status.ad_status_cd = opr_ad_status.ad_status_cd
   -- get the actual task id
   INNER JOIN acor_ref_doc_defn_v1 ref_doc ON
      opr_asset_ad_status.ad_id = ref_doc.defn_id
   INNER JOIN acor_inv_aircraft_v1 aircraft ON
      opr_asset_ad_status.asset_id = aircraft.aircraft_id
;