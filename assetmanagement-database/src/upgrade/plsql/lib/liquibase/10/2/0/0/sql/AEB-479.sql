--liquibase formatted sql

--changeSet AEB-479:1 stripComments:false
-- add new API permission for RFQ 
INSERT INTO
  utl_action_config_parm
  (
    PARM_NAME, PARM_VALUE, PARM_DESC, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
  )
  SELECT
     'API_RFQ_PARM', 'FALSE', 'Permission to access the RFQ REST API', 'TRUE/FALSE', 'FALSE', 1, 'API - PROCUREMENT', '8.2-SP5', 0
  FROM
    dual
  WHERE
    NOT EXISTS ( SELECT 1 FROM utl_action_config_parm WHERE PARM_NAME = 'API_RFQ_PARM' );