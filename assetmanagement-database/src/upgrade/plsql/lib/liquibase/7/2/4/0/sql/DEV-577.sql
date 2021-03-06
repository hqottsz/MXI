--liquibase formatted sql


--changeSet DEV-577:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
SELECT 'false', 'ACTION_HIST_EDIT_CUST_PART_REQUIREMENT', 'SECURED_RESOURCE','Permission to edit historic task cust part requirements in WebMaintenix','USER', 'TRUE/FALSE', 'FALSE', 1,'Maint - Historic Editing', '7.5',0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_config_parm WHERE utl_config_parm.parm_name = 'ACTION_HIST_EDIT_CUST_PART_REQUIREMENT');

--changeSet DEV-577:2 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
SELECT 'false', 'ACTION_EDIT_CUST_PART_REQ', 'SECURED_RESOURCE','Permission to edit cust part requirements in WebMaintenix','USER', 'TRUE/FALSE', 'FALSE', 1,'Maint - Historic Editing', '7.5',0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_config_parm WHERE utl_config_parm.parm_name = 'ACTION_EDIT_CUST_PART_REQ');

--changeSet DEV-577:3 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
SELECT 'false', 'ACTION_EDIT_CUST_PART_REQUIREMENT', 'SECURED_RESOURCE','Permission to edit cust part requirements in WebMaintenix','USER', 'TRUE/FALSE', 'FALSE', 1,'Maint - Historic Editing', '7.5',0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_config_parm WHERE utl_config_parm.parm_name = 'ACTION_EDIT_CUST_PART_REQUIREMENT');

--changeSet DEV-577:4 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
SELECT 'false', 'ACTION_HIST_ADD_CUST_PART_REQUIREMENT', 'SECURED_RESOURCE','Permission to add histroic cust part requirements in WebMaintenix','USER', 'TRUE/FALSE', 'FALSE', 1,'Maint - Historic Editing', '7.5',0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_config_parm WHERE utl_config_parm.parm_name = 'ACTION_HIST_ADD_CUST_PART_REQUIREMENT');

--changeSet DEV-577:5 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
SELECT 'false', 'ACTION_ADD_CUST_PART_REQUIREMENT', 'SECURED_RESOURCE','Permission to add cust part requirements in WebMaintenix','USER', 'TRUE/FALSE', 'FALSE', 1,'Maint - Historic Editing', '7.5',0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_config_parm WHERE utl_config_parm.parm_name = 'ACTION_ADD_CUST_PART_REQUIREMENT');

--changeSet DEV-577:6 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
SELECT 'false', 'ACTION_HIST_RMV_CUST_PART_REQUIREMENT', 'SECURED_RESOURCE','Permission to remove historic cust part requirements in WebMaintenix','USER', 'TRUE/FALSE', 'FALSE', 1,'Maint - Historic Editing', '7.5',0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_config_parm WHERE utl_config_parm.parm_name = 'ACTION_HIST_RMV_CUST_PART_REQUIREMENT');

--changeSet DEV-577:7 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
SELECT 'false', 'ACTION_RMV_CUST_PART_REQUIREMENT', 'SECURED_RESOURCE','Permission to remove cust part requirements in WebMaintenix','USER', 'TRUE/FALSE', 'FALSE', 1,'Maint - Historic Editing', '7.5',0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_config_parm WHERE utl_config_parm.parm_name = 'ACTION_RMV_CUST_PART_REQUIREMENT');

--changeSet DEV-577:8 stripComments:false
INSERT INTO DB_TYPE_CONFIG_PARM (PARM_NAME, PARM_TYPE, DB_TYPE_CD)
SELECT 'ACTION_HIST_EDIT_CUST_PART_REQUIREMENT', 'SECURED_RESOURCE','OPER'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM db_type_config_parm WHERE db_type_config_parm.parm_name = 'ACTION_HIST_EDIT_CUST_PART_REQUIREMENT');

--changeSet DEV-577:9 stripComments:false
INSERT INTO DB_TYPE_CONFIG_PARM (PARM_NAME, PARM_TYPE, DB_TYPE_CD)
SELECT 'ACTION_EDIT_CUST_PART_REQ', 'SECURED_RESOURCE','OPER'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM db_type_config_parm WHERE db_type_config_parm.parm_name = 'ACTION_EDIT_CUST_PART_REQ');

--changeSet DEV-577:10 stripComments:false
INSERT INTO DB_TYPE_CONFIG_PARM (PARM_NAME, PARM_TYPE, DB_TYPE_CD)
SELECT 'ACTION_EDIT_CUST_PART_REQUIREMENT', 'SECURED_RESOURCE','OPER'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM db_type_config_parm WHERE db_type_config_parm.parm_name = 'ACTION_EDIT_CUST_PART_REQUIREMENT');

--changeSet DEV-577:11 stripComments:false
INSERT INTO DB_TYPE_CONFIG_PARM (PARM_NAME, PARM_TYPE, DB_TYPE_CD)
SELECT 'ACTION_HIST_ADD_CUST_PART_REQUIREMENT', 'SECURED_RESOURCE','OPER'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM db_type_config_parm WHERE db_type_config_parm.parm_name = 'ACTION_HIST_ADD_CUST_PART_REQUIREMENT');

--changeSet DEV-577:12 stripComments:false
INSERT INTO DB_TYPE_CONFIG_PARM (PARM_NAME, PARM_TYPE, DB_TYPE_CD)
SELECT 'ACTION_ADD_CUST_PART_REQUIREMENT', 'SECURED_RESOURCE','OPER'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM db_type_config_parm WHERE db_type_config_parm.parm_name = 'ACTION_ADD_CUST_PART_REQUIREMENT');

--changeSet DEV-577:13 stripComments:false
INSERT INTO DB_TYPE_CONFIG_PARM (PARM_NAME, PARM_TYPE, DB_TYPE_CD)
SELECT 'ACTION_HIST_RMV_CUST_PART_REQUIREMENT', 'SECURED_RESOURCE','OPER'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM db_type_config_parm WHERE db_type_config_parm.parm_name = 'ACTION_HIST_RMV_CUST_PART_REQUIREMENT');

--changeSet DEV-577:14 stripComments:false
INSERT INTO DB_TYPE_CONFIG_PARM (PARM_NAME, PARM_TYPE, DB_TYPE_CD)
SELECT 'ACTION_RMV_CUST_PART_REQUIREMENT', 'SECURED_RESOURCE','OPER'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM db_type_config_parm WHERE db_type_config_parm.parm_name = 'ACTION_RMV_CUST_PART_REQUIREMENT');