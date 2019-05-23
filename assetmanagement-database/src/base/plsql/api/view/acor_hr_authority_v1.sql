--liquibase formatted sql


--changeSet acor_hr_authority_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_hr_authority_v1
AS
SELECT
   org_hr.alt_id                AS user_id,
   org_authority.authority_cd   AS authority,
   org_authority.authority_name,
   org_hr_authority.notify_bool AS notify_flag
FROM
   org_hr
   INNER JOIN org_hr_authority ON
      org_hr.hr_db_id = org_hr_authority.hr_db_id AND
      org_hr.hr_id    = org_hr_authority.hr_id
   INNER JOIN org_authority ON
      org_hr_authority.authority_db_id = org_authority.authority_db_id AND
      org_hr_authority.authority_id    = org_authority.authority_id;                        