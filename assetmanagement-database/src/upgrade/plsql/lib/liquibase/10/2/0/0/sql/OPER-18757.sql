--liquibase formatted sql

--changeSet OPER-18757:1 stripComments:false 
INSERT INTO 
   utl_action_config_parm
   ( 
     parm_name, parm_value, encrypt_bool, parm_desc, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id
   )
SELECT 
    'API_TASK_SCHEDULING', 'FALSE', 0, 'Permission to utilize the Task Scheduling API', 'TRUE/FALSE', 'FALSE', 1, 'API - MAINTENANCE', '8.2-SP5', 0 
FROM
   dual
WHERE
   NOT EXISTS (SELECT 1 FROM utl_action_config_parm WHERE parm_name = 'API_TASK_SCHEDULING');
