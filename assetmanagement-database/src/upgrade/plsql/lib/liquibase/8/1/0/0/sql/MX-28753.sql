--liquibase formatted sql


--changeSet MX-28753:1 stripComments:false
-- disable foreign key constraint from db_type_config_parm
ALTER TABLE DB_TYPE_CONFIG_PARM DISABLE CONSTRAINT FK_UTLACTCFGPRM_DBTYPECFGPRM;

--changeSet MX-28753:2 stripComments:false
-- disable foreign key constraint from utl_action_user_parm
ALTER TABLE UTL_ACTION_USER_PARM DISABLE CONSTRAINT FK_UTLACTCFGPRM_UTLACTUSRPRM;

--changeSet MX-28753:3 stripComments:false
-- disable foreign key constraint from utl_action_role_parm
ALTER TABLE UTL_ACTION_ROLE_PARM DISABLE CONSTRAINT FK_UTLACTCFGPRM_UTLROLEPARM;

--changeSet MX-28753:4 stripComments:false
UPDATE db_type_config_parm
SET parm_name='API_EXECUTION_STATUS_REQUEST'
WHERE parm_name='API_STATUS_REQUEST';

--changeSet MX-28753:5 stripComments:false
UPDATE utl_action_user_parm
SET parm_name='API_EXECUTION_STATUS_REQUEST'
WHERE parm_name='API_STATUS_REQUEST';

--changeSet MX-28753:6 stripComments:false
UPDATE utl_action_role_parm
SET parm_name='API_EXECUTION_STATUS_REQUEST'
WHERE parm_name='API_STATUS_REQUEST';

--changeSet MX-28753:7 stripComments:false
UPDATE utl_action_config_parm
SET parm_name='API_EXECUTION_STATUS_REQUEST',parm_desc='Permission to allow API retrieval call for execution statuses.'
WHERE parm_name='API_STATUS_REQUEST';

--changeSet MX-28753:8 stripComments:false
-- enable the foreign key constraints in the utl_action_role_parm table
ALTER TABLE UTL_ACTION_ROLE_PARM ENABLE CONSTRAINT FK_UTLACTCFGPRM_UTLROLEPARM;

--changeSet MX-28753:9 stripComments:false
-- enable the foreign key constraints in the utl_action_user_parm table
ALTER TABLE UTL_ACTION_USER_PARM ENABLE CONSTRAINT FK_UTLACTCFGPRM_UTLACTUSRPRM;

--changeSet MX-28753:10 stripComments:false
-- enable foreign key constraint from db_type_config_parm
ALTER TABLE DB_TYPE_CONFIG_PARM ENABLE CONSTRAINT FK_UTLACTCFGPRM_DBTYPECFGPRM;