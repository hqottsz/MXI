--liquibase formatted sql


--changeSet SWA-2496:1 stripComments:false
/*
 *  Permissions for using the Cache REST API
 *
 */
INSERT INTO 
  utl_action_config_parm
  (
    parm_name, parm_value, encrypt_bool, parm_desc, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id
  )
  SELECT 'API_CACHE_PARM','FALSE', 0, 'Permission to utilize the Cache API',  'TRUE/FALSE','FALSE', 1, 'API - CACHE', '8.2-SP2', 0
  FROM
  dual
  WHERE NOT EXISTS (SELECT 1  FROM   utl_action_config_parm WHERE  parm_name = 'API_CACHE_PARM' );