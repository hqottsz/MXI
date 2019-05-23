--liquibase formatted sql


--changeSet acor_hr_license_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_hr_license_v1
AS 
SELECT
   org_hr.alt_id                     AS user_id,
   org_hr_license.certif_tag         AS license_tag,
   org_hr_license.certif_org_name    AS license_org_name,
   org_hr_license.license_type_cd    AS license_type_code,
   org_hr_license.expiry_dt          AS license_expiry_date
FROM
   org_hr
   INNER JOIN org_hr_license ON
      org_hr.hr_db_id = org_hr_license.hr_db_id AND
      org_hr.hr_id    = org_hr_license.hr_id;