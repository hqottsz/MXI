--liquibase formatted sql
--changeSet OPER-11903:1 stripComments:false
 /**************************************************************************************
 * API Permission parameter to authorize or reject a deferral request
 ***************************************************************************************/
 INSERT INTO 
  utl_action_config_parm
  (
     parm_name, parm_value, encrypt_bool, parm_desc, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id
  )
 SELECT
  'API_AUTHORIZE_REJECT_DEFERRAL_REQUEST', 'FALSE',  0, 'Permission to authorize or reject a deferral request' , 'TRUE/FALSE', 'FALSE', 1, 'API - MAINTENANCE', '8.2-SP3', 0
 FROM
  DUAL
 WHERE 
  NOT EXISTS (
     SELECT 1 FROM utl_action_config_parm WHERE parm_name = 'API_AUTHORIZE_REJECT_DEFERRAL_REQUEST'
  );
  
 --changeSet OPER-11903:2 stripComments:false
 /**************************************************************************************
 * API Permission parameter to search deferral requests
 ***************************************************************************************/
 INSERT INTO 
  utl_action_config_parm
  (
     parm_name, parm_value, encrypt_bool, parm_desc, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id
  )
 SELECT
  'API_SEARCH_DEFERRAL_REQUEST', 'FALSE',  0, 'Permission to search deferral requests' , 'TRUE/FALSE', 'FALSE', 1, 'API - MAINTENANCE', '8.2-SP3', 0
 FROM
  DUAL
 WHERE 
  NOT EXISTS (
     SELECT 1 FROM utl_action_config_parm WHERE parm_name = 'API_SEARCH_DEFERRAL_REQUEST'
  );