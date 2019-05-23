--liquibase formatted sql


--changeSet acor_hr_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_hr_user_v1
AS
SELECT
   org_hr.alt_id           AS user_id,
   utl_user.username,
   org_hr.hr_cd            AS hr_code,
   utl_user.first_name,
   utl_user.last_name,
   utl_user.locked_bool    AS locked_flag
FROM
   utl_user
   INNER JOIN org_hr ON
      utl_user.user_id = org_hr.user_id;