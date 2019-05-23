--liquibase formatted sql


--changeSet OPER-8288:1 stripComments:false
 /**************************************************************************************
 * 
 * API Permission parameter to create a deferral request
 *
 ***************************************************************************************/
 INSERT INTO 
  utl_action_config_parm
  (
     parm_name, parm_value, encrypt_bool, parm_desc, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id
  )
 SELECT
  'API_CREATE_DEFERRAL_REQUEST', 'FALSE',  0, 'Permission to create a deferral request' , 'TRUE/FALSE', 'FALSE', 1, 'API - MAINTENANCE', '8.2-SP3', 0
 FROM
  DUAL
 WHERE 
  NOT EXISTS (
     SELECT 1 FROM utl_action_config_parm WHERE parm_name = 'API_CREATE_DEFERRAL_REQUEST'
  );
