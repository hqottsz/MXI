--liquibase formatted sql


--changeSet acor_eqp_task_zone_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_eqp_task_zone_v1
AS
SELECT
  -- zone_db_id,
 --  zone_id,
 --  nh_zone_db_id,
 --  nh_zone_id,
 --  assmbl_db_id,
   assmbl_cd,
   zone_cd         AS zone_code,
   desc_sdesc      AS name,
   desc_ldesc      AS description,
   hr_limit        AS hour_limit
FROM
   eqp_task_zone
;