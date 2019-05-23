--liquibase formatted sql

--changeSet EXT-1089:1 stripComments:false
-- add new API permission for Labour Skill API
INSERT INTO
  utl_action_config_parm
  (
    PARM_NAME, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
  )
  SELECT
     'API_LABOUR_SKILL_PARM', 'FALSE',  0, 'Permission to access the Labour Skill REST API' , 'TRUE/FALSE', 'FALSE', 1, 'API - MAINTENANCE', '8.3', 0
  FROM
    dual
  WHERE
    NOT EXISTS ( SELECT 1 FROM utl_action_config_parm WHERE PARM_NAME = 'API_LABOUR_SKILL_PARM' );
	
	