--liquibase formatted sql


--changeSet SWA-2726:1 stripComments:false
/*
 *  Permissions for using REST Alert API
 *
 */
INSERT INTO 
  utl_action_config_parm (parm_name,
                          parm_value,
                          encrypt_bool,
                          parm_desc,
                          default_value,
                          allow_value_desc,
                          mand_config_bool,
                          category,
                          modified_in,
                          utl_id )
  SELECT 'API_ALERT_PARM',
         'FALSE',
         0,
         'Permission to utilize the Alert API',
         'FALSE',
         'TRUE/FALSE',
         1,
         'API - ALERT',
         '8.2-SP2',
         0
  FROM dual
  WHERE NOT EXISTS (SELECT 1
                    FROM   utl_action_config_parm
                    WHERE  parm_name = 'API_ALERT_PARM' );