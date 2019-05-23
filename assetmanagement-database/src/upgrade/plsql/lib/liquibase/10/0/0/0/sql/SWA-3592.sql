--liquibase formatted sql


--changeSet SWA-3592:1 stripComments:false
/*
 *  Permissions for using REST APIs
 *
 */
INSERT INTO 
  utl_action_role_parm (parm_name,
                          role_id,
                          parm_value,
                          session_auth_bool,
                          utl_id)
  SELECT 'API_PUBSUB_PARM',
         '17001',
         'true',
         0,
         0
  FROM dual
  WHERE NOT EXISTS (SELECT 1
                    FROM   utl_action_role_parm
                    WHERE  parm_name = 'API_PUBSUB_PARM' AND role_id = '17001' );

