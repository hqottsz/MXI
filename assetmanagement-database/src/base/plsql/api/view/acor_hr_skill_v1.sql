--liquibase formatted sql


--changeSet acor_hr_skill_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_hr_skill_v1
AS
SELECT
   org_hr.alt_id                AS user_id,
   org_hr_qual.labour_skill_cd  AS labour_skill_code
FROM
   org_hr
   INNER JOIN org_hr_qual ON
      org_hr.hr_db_id = org_hr_qual.hr_db_id AND
      org_hr.hr_id    = org_hr_qual.hr_id;