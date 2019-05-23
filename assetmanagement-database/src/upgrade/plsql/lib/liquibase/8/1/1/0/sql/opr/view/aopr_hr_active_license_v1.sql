--liquibase formatted sql


--changeSet aopr_hr_active_license_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_hr_active_license_v1
AS 
SELECT
   user_id,
   certificate_tag,
   certificate_org_name,
   certificate_expiry_date,
   license_type_code,
   stage_note,
   expiry_date,
   effective_date,
   prereq_expiry_date,
   prereq_effective_date,
   release_number,
   stage_reason
FROM
   acor_hr_license_v1
WHERE   
   status_code = 'ACTV' 
; 