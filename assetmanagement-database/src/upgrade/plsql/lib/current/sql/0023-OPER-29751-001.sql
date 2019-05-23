--liquibase formatted sql

--changeSet 0023-OPER-29751-001:1 stripComments:false
INSERT INTO UTL_ACTION_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_DESC, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
SELECT 'FALSE', 'ACTION_CREATE_RO_FOR_INV', 'Permission to create repair orders for inventory.','TRUE/FALSE', 'FALSE', 1,'Purchasing - Repair Orders', '8.3-SP2',0
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM utl_action_config_parm WHERE parm_name = 'ACTION_CREATE_RO_FOR_INV');
