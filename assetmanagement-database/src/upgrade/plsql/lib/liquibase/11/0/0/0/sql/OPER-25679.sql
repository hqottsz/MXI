--liquibase formatted sql

--changeSet OPER-25679:1 stripComments:false

INSERT INTO 
  utl_action_config_parm 
  (
    PARM_NAME, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
  )
  SELECT
    'API_MANUFACTURER_REQUEST', 'FALSE', 0, 'Permission to access the Manufacturer API.', 'TRUE/FALSE', 'FALSE', 1, 'API - PROCUREMENT', '8.3-SP1', 0
  FROM
    dual
  WHERE
    NOT EXISTS ( SELECT 1 FROM utl_action_config_parm WHERE PARM_NAME = 'API_MANUFACTURER_REQUEST' );