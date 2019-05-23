--liquibase formatted sql


--changeSet aopr_sb_status_acft_v1:1 stripComments:false
CREATE OR REPLACE VIEW AOPR_SB_STATUS_ACFT_V1 AS
SELECT
   opr_asset_sb_status.ID,
   ref_doc.task_id,
   asset_id              AS inv_id,
   ref_doc.defn_id       AS task_defn_id,
   ref_doc.assembly_code AS assmbl_code,
   ref_doc.status_code   AS ad_taskdefn_status,
   oem_serial_number,
   registration_code,
   task_code             AS task_cd,
   task_name,
   opr_sb_status.sb_status_cd,
   display_code,
   display_desc,
   status_notes,
   completion_dt         AS completion_date
FROM
   opr_asset_sb_status -- custom table - SMO solution
   INNER JOIN opr_sb_status ON
      opr_asset_sb_status.sb_status_cd = opr_sb_status.sb_status_cd
   -- get the actual task id
   INNER JOIN acor_adsb_ref_doc_defn_mv1 ref_doc ON
      opr_asset_sb_status.sb_id = ref_doc.defn_id
   --
   INNER JOIN acor_inv_aircraft_v1 aircraft ON
      aircraft.aircraft_id = opr_asset_sb_status.asset_id;