--liquibase formatted sql


--changeSet vw_users_organizations:1 stripComments:false
CREATE OR REPLACE VIEW VW_USERS_ORGANIZATIONS
(
   org_db_id,
   org_id,
   hr_db_id,
   hr_id,
   org_key,
   org_code,
   org_full_code,
   org_name
)
AS
-- Get Organizations assgined to user
SELECT
   org_org.org_db_id,
   org_org.org_id,
   org_org_hr.hr_db_id,
   org_org_hr.hr_id,
   org_org.org_db_id || ':' || org_org.org_id AS org_key,
   org_org.org_cd AS org_code,
   org_org.code_mdesc || org_org.org_cd AS org_full_code,
   org_org.code_mdesc||org_org.org_cd||' ('||org_org.org_sdesc||')' AS org_name
FROM
   org_org_hr
   INNER JOIN org_org ON org_org.org_db_id = org_org_hr.org_db_id AND
                         org_org.org_id    = org_org_hr.org_id
WHERE
   org_org.org_type_cd != 'DEFAULT'
UNION
-- Get Sub-organizations of organizations assigned to user
SELECT
   org_org.org_db_id,
   org_org.org_id,
   org_org_hr.hr_db_id,
   org_org_hr.hr_id,
   org_org.org_db_id || ':' || org_org.org_id AS org_key,
   org_org.org_cd AS org_code,
   org_org.code_mdesc || org_org.org_cd AS org_full_code,
   org_org.code_mdesc||org_org.org_cd||' ('||org_org.org_sdesc||')' AS org_name
FROM
   org_org_hr
   INNER JOIN org_suborg_cache ON org_suborg_cache.org_db_id 	  = org_org_hr.org_db_id AND
                                  org_suborg_cache.org_id   	  = org_org_hr.org_id
   INNER JOIN org_org         ON  org_org.org_db_id = org_suborg_cache.sub_org_db_id   AND
                                  org_org.org_id    = org_suborg_cache.sub_org_id
WHERE
   org_org.org_type_cd != 'DEFAULT';