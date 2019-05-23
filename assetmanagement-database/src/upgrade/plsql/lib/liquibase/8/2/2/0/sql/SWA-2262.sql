--liquibase formatted sql


--changeSet SWA-2262:1 stripComments:false
/*
 *  Permissions for using REST APIs
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
  SELECT 'API_WORKPACKAGE_PARM',
         'FALSE',
         0,
         'Permission to utilize the Workpackage API',
         'FALSE',
         'TRUE/FALSE',
         1,
         'API - MAINTENANCE',
         '8.2-SP2',
         0
  FROM dual
  WHERE NOT EXISTS (SELECT 1
                    FROM   utl_action_config_parm
                    WHERE  parm_name = 'API_WORKPACKAGE_PARM' );

--changeSet SWA-2262:2 stripComments:false
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
  SELECT 'API_AIRCRAFT_PARM',
         'FALSE',
         0,
         'Permission to utilize the Aircraft API',
         'FALSE',
         'TRUE/FALSE',
         1,
         'API - MATERIALS',
         '8.2-SP2',
         0
  FROM dual
  WHERE NOT EXISTS (SELECT 1
                    FROM   utl_action_config_parm
                    WHERE  parm_name = 'API_AIRCRAFT_PARM' );

--changeSet SWA-2262:3 stripComments:false
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
  SELECT 'API_FAULT_PARM',
         'FALSE',
         0,
         'Permission to utilize the Fault API',
         'FALSE',
         'TRUE/FALSE',
         1,
         'API - MATERIALS',
         '8.2-SP2',
         0
  FROM dual
  WHERE NOT EXISTS (SELECT 1
                    FROM   utl_action_config_parm
                    WHERE  parm_name = 'API_FAULT_PARM' );

--changeSet SWA-2262:4 stripComments:false
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
  SELECT 'API_FLIGHT_PARM',
         'FALSE',
         0,
         'Permission to utilize the Flight API',
         'FALSE',
         'TRUE/FALSE',
         1,
         'API - MAINTENANCE',
         '8.2-SP2',
         0
  FROM dual
  WHERE NOT EXISTS (SELECT 1
                    FROM   utl_action_config_parm
                    WHERE  parm_name = 'API_FLIGHT_PARM' );

--changeSet SWA-2262:5 stripComments:false
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
  SELECT 'API_LOCATION_PARM',
         'FALSE',
         0,
         'Permission to utilize the Location API',
         'FALSE',
         'TRUE/FALSE',
         1,
         'API - LOCATION',
         '8.2-SP2',
         0
  FROM dual
  WHERE NOT EXISTS (SELECT 1
                    FROM   utl_action_config_parm
                    WHERE  parm_name = 'API_LOCATION_PARM' );

--changeSet SWA-2262:6 stripComments:false
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
  SELECT 'API_TASKDEF_PARM',
         'FALSE',
         0,
         'Permission to utilize the Task Definition API',
         'FALSE',
         'TRUE/FALSE',
         1,
         'API - MAINTENANCE',
         '8.2-SP2',
         0
  FROM dual
  WHERE NOT EXISTS (SELECT 1
                    FROM   utl_action_config_parm
                    WHERE  parm_name = 'API_TASKDEF_PARM' );

--changeSet SWA-2262:7 stripComments:false
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
  SELECT 'API_TASK_PARM',
         'FALSE',
         0,
         'Permission to utilize the Task API',
         'FALSE',
         'TRUE/FALSE',
         1,
         'API - MAINTENANCE',
         '8.2-SP2',
         0
  FROM dual
  WHERE NOT EXISTS (SELECT 1
                    FROM   utl_action_config_parm
                    WHERE  parm_name = 'API_TASK_PARM' );