--liquibase formatted sql

--changeSet SWA-3380:1 stripComments:false
/*
 *
 * Permissions to use the Usage read and write REST APIs.
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
  SELECT 'READ_USAGE',
         'FALSE',
         0,
         'Permission to use the Usage API to read data',
         'FALSE',
         'TRUE/FALSE',
         1,
         'API - MATERIALS',
         '8.2-SP2',
         0 
  FROM dual
  WHERE NOT EXISTS (SELECT 1 
                    FROM   utl_action_config_parm
                    WHERE  parm_name = 'READ_USAGE' );

--changeSet SWA-3380:2 stripComments:false
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
  SELECT 'WRITE_USAGE',
         'FALSE',
         0,
         'Permission to use the Usage API to write data',
         'FALSE',
         'TRUE/FALSE',
         1,
         'API - MATERIALS',
         '8.2-SP2',
         0 
  FROM dual
  WHERE NOT EXISTS (SELECT 1 
                    FROM   utl_action_config_parm
                    WHERE  parm_name = 'WRITE_USAGE' );