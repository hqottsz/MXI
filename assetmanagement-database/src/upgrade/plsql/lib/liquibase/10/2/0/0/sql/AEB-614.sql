--liquibase formatted sql

--changeSet AEB-614:1 stripComments:false
-- add new API permission for RFQ History note
INSERT INTO UTL_ACTION_CONFIG_PARM ( 
parm_name, parm_value, encrypt_bool, parm_desc, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
 )
  SELECT
     'API_RFQ_HISTORY_NOTE_REQUEST', 'FALSE', 0, 'Permission to access PO RFQ History Notes', 'TRUE/FALSE', 'FALSE', 1, 'API - PROCUREMENT', '8.2-SP4', 0 
  FROM
    dual
  WHERE
    NOT EXISTS ( SELECT 1 FROM utl_action_config_parm WHERE PARM_NAME = 'API_RFQ_HISTORY_NOTE_REQUEST' );