--liquibase formatted sql


--changeSet acor_hr_po_auth_level_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_hr_po_auth_level_v1
AS 
SELECT
   org_hr.alt_id                AS user_id,
   org_hr_po_auth_lvl.po_auth_lvl_cd AS po_autho_level_code
FROM
   org_hr
   INNER JOIN org_hr_po_auth_lvl ON
      org_hr.hr_db_id = org_hr_po_auth_lvl.hr_db_id AND
      org_hr.hr_id    = org_hr_po_auth_lvl.hr_id;