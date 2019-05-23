--liquibase formatted sql

--changeSet SWA-6022:1 stripComments:false
/*
 *  Permissions for using REST APIs
 *
 */
INSERT INTO utl_action_config_parm ( parm_name, parm_value, encrypt_bool, parm_desc, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id )
SELECT 'API_FAULT_HISTORY_REQUEST', 'FALSE',  0, 'Permission to utilize the Fault History API' , 'TRUE/FALSE', 'FALSE', 1, 'API - MAINTENANCE', '8.2-SP3', 0 FROM dual WHERE NOT EXISTS (SELECT 1 FROM utl_action_config_parm WHERE parm_name = 'API_FAULT_HISTORY_REQUEST');
