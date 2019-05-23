--liquibase formatted sql

--changeSet JOBC-29:1 stripComments:false
-- add new API permission for job card definition API
INSERT INTO
  utl_action_config_parm
  (
    PARM_NAME, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
  )
  SELECT
     'API_JOB_CARD_PARM', 'FALSE',  0, 'Permission to access the job card Definition REST API.' , 'TRUE/FALSE', 'FALSE', 1, 'API - MAINTENANCE', '8.2-SP4', 0
  FROM
    dual
  WHERE
    NOT EXISTS ( SELECT 1 FROM utl_action_config_parm WHERE PARM_NAME = 'API_JOB_CARD_PARM' );