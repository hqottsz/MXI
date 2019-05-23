--liquibase formatted sql

--changeSet OPER-12840:1 stripComments:false
INSERT INTO 
  utl_action_config_parm 
  (
    PARM_NAME, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
  )
  SELECT
    'READ_AEROBUY_INVOICE', 'FALSE',  0, 'Permission to utilize the Aerobuy Invoice API to read data' , 'TRUE/FALSE', 'FALSE', 1, 'API - INVOICE', '8.2-SP3', 0
  FROM
    dual
  WHERE
    NOT EXISTS ( SELECT 1 FROM utl_action_config_parm WHERE PARM_NAME = 'READ_AEROBUY_INVOICE' );
    
    
--changeSet OPER-12840:2 stripComments:false
INSERT INTO 
  utl_action_config_parm 
  (
    PARM_NAME, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
  )
  SELECT
     'WRITE_AEROBUY_INVOICE', 'FALSE',  0, 'Permission to utilize the Aerobuy Invoice API to write data' , 'TRUE/FALSE', 'FALSE', 1, 'API - INVOICE', '8.2-SP3', 0
  FROM
    dual
  WHERE
    NOT EXISTS ( SELECT 1 FROM utl_action_config_parm WHERE PARM_NAME = 'WRITE_AEROBUY_INVOICE' );