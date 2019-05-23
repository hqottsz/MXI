--liquibase formatted sql

--changeSet OPER-13946:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- delete old aerobuy invoice API permissions
BEGIN 
   utl_migr_data_pkg.action_parm_delete('READ_AEROBUY_INVOICE');
   utl_migr_data_pkg.action_parm_delete('WRITE_AEROBUY_INVOICE');
END;
/

--changeSet OPER-13946:2 stripComments:false 
-- add new API permission for core Invoice POST API 
INSERT INTO 
  utl_action_config_parm 
  (
    PARM_NAME, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
  )
  SELECT
     'WRITE_INVOICE', 'FALSE',  0, 'Permission to utilize the Invoice API to write data' , 'TRUE/FALSE', 'FALSE', 1, 'API - MATERIALS', '8.2-SP4', 0
  FROM
    dual
  WHERE
    NOT EXISTS ( SELECT 1 FROM utl_action_config_parm WHERE PARM_NAME = 'WRITE_INVOICE' );