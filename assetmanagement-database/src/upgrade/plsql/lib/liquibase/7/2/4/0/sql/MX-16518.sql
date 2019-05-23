--liquibase formatted sql


--changeSet MX-16518:1 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
SELECT 'sUSSTGPartType','SESSION',null , 0,'Stores the last selected value for Part Type filter on the Unserviceable Staging todo list.','USER', 'STRING', '', 0, 'SESSION', '6.8.12', 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_CONFIG_PARM WHERE PARM_NAME = 'sUSSTGPartType' AND PARM_TYPE = 'SESSION');

--changeSet MX-16518:2 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
SELECT 'sShowOnlyVendorOwnedParts','SESSION',0 , 0,'Whether to show only vendor owned parts on the Unserviceable Staging todo list.','USER', 'Number', '0', 0, 'SESSION', '6.8.12', 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_CONFIG_PARM WHERE PARM_NAME = 'sUSSTGPartType' AND PARM_TYPE = 'SESSION');