--liquibase formatted sql

--changeSet OPER-15707:1 stripComments:false
-- add new API permission for Configuration Management Publish Part Group API
INSERT INTO
utl_action_config_parm
(
  PARM_NAME, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
)
SELECT
   'API_PUBLISH_PART_GROUP_PARM', 'FALSE', 0, 'Permission to invoke Configuration Manager Publish Part Group API.', 'TRUE/FALSE', 'FALSE', 1, 'API - CONFIGURATION MANAGER', '8.2-SP4', 0
FROM
  dual
WHERE
  NOT EXISTS ( SELECT 1 FROM utl_action_config_parm WHERE PARM_NAME = 'API_PUBLISH_PART_GROUP_PARM' );