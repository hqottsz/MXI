--liquibase formatted sql


--changeSet acor_eqp_task_panel_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_eqp_task_panel_v1
AS
SELECT
  -- panel_db_id,
  -- panel_id,
  -- assmbl_db_id,
   assmbl_cd,
   panel_cd        AS panel_code,
  -- zone_db_id,
  -- zone_id,
   desc_sdesc      AS name,
   desc_ldesc      AS description
FROM
   eqp_task_panel
;