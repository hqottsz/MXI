--liquibase formatted sql
--Add the param API_DOMAIN_TYPE_CODE_PARM

--changeSet OPER-24470:1 stripComments:false
INSERT INTO 
  utl_action_config_parm 
  (
    parm_name, parm_value, encrypt_bool, parm_desc, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id
  )
  SELECT
    'API_DOMAIN_TYPE_CODE_PARM', 'FALSE',  0, 'Permission to utilize the Domain Type Code API' , 'TRUE/FALSE', 'FALSE', 1, 'API - SYSTEM', '8.3-SP1', 0 
  FROM
    dual
  WHERE
    NOT EXISTS ( SELECT 1 FROM utl_action_config_parm WHERE parm_name = 'API_DOMAIN_TYPE_CODE_PARM' );
