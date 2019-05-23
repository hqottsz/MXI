--liquibase formatted sql


--changeSet acor_hr_license_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_hr_license_v1
AS
SELECT
   org_hr.alt_id                     AS user_id,
   org_hr_license.certif_tag         AS certificate_tag,
   org_hr_license.certif_org_name    AS certificate_org_name,
   org_hr_license.expiry_dt          AS certificate_expiry_date,
   org_hr_license.license_type_cd    AS license_type_code,
   stage_note,
   org_hr_lic.expiry_dt              AS expiry_date,
   org_hr_lic.effect_dt              AS effective_date,
   org_hr_lic.prereq_expiry_dt       AS prereq_expiry_date,
   org_hr_lic.prereq_effect_dt       AS prereq_effective_date,
   org_hr_lic.release_no             AS release_number,
   org_hr_lic.stage_reason_cd        AS stage_reason,
   org_hr_lic.hr_lic_status_cd       AS status_code
FROM
   org_hr
   LEFT JOIN org_hr_license ON
      org_hr.hr_db_id = org_hr_license.hr_db_id AND
      org_hr.hr_id    = org_hr_license.hr_id
   LEFT JOIN org_hr_lic ON
      org_hr.hr_db_id = org_hr_lic.hr_db_id AND
      org_hr.hr_id    = org_hr_lic.hr_id
   ; 