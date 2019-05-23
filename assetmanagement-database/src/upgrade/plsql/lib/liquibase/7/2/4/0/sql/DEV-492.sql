--liquibase formatted sql


--changeSet DEV-492:1 stripComments:false
DELETE FROM utl_user_parm WHERE utl_user_parm.parm_name = 'ACTION_APPROVE_VENDOR' AND utl_user_parm.parm_type = 'SECURED_RESOURCE';

--changeSet DEV-492:2 stripComments:false
DELETE FROM utl_role_parm WHERE utl_role_parm.parm_name = 'ACTION_APPROVE_VENDOR';

--changeSet DEV-492:3 stripComments:false
DELETE FROM db_type_config_parm WHERE db_type_config_parm.parm_name = 'ACTION_APPROVE_VENDOR';

--changeSet DEV-492:4 stripComments:false
DELETE FROM utl_config_parm WHERE utl_config_parm.parm_name = 'ACTION_APPROVE_VENDOR';

--changeSet DEV-492:5 stripComments:false
DELETE FROM utl_user_parm WHERE utl_user_parm.parm_name = 'ACTION_UNAPPROVE_VENDOR' AND utl_user_parm.parm_type = 'SECURED_RESOURCE';

--changeSet DEV-492:6 stripComments:false
DELETE FROM utl_role_parm WHERE utl_role_parm.parm_name = 'ACTION_UNAPPROVE_VENDOR';

--changeSet DEV-492:7 stripComments:false
DELETE FROM db_type_config_parm WHERE db_type_config_parm.parm_name = 'ACTION_UNAPPROVE_VENDOR';

--changeSet DEV-492:8 stripComments:false
DELETE FROM utl_config_parm WHERE utl_config_parm.parm_name = 'ACTION_UNAPPROVE_VENDOR';

--changeSet DEV-492:9 stripComments:false
DELETE FROM utl_user_parm WHERE utl_user_parm.parm_name = 'ACTION_WARNING_VENDOR' AND utl_user_parm.parm_type = 'SECURED_RESOURCE';

--changeSet DEV-492:10 stripComments:false
DELETE FROM utl_role_parm WHERE utl_role_parm.parm_name = 'ACTION_WARNING_VENDOR';

--changeSet DEV-492:11 stripComments:false
DELETE FROM db_type_config_parm WHERE db_type_config_parm.parm_name = 'ACTION_WARNING_VENDOR';

--changeSet DEV-492:12 stripComments:false
DELETE FROM utl_config_parm WHERE utl_config_parm.parm_name = 'ACTION_WARNING_VENDOR';

--changeSet DEV-492:13 stripComments:false
DELETE FROM utl_user_parm WHERE utl_user_parm.parm_name = 'ACTION_ENABLE_VIEW_ALL_VENDORS' AND utl_user_parm.parm_type = 'SECURED_RESOURCE';

--changeSet DEV-492:14 stripComments:false
DELETE FROM utl_role_parm WHERE utl_role_parm.parm_name = 'ACTION_ENABLE_VIEW_ALL_VENDORS';

--changeSet DEV-492:15 stripComments:false
DELETE FROM db_type_config_parm WHERE db_type_config_parm.parm_name = 'ACTION_ENABLE_VIEW_ALL_VENDORS';

--changeSet DEV-492:16 stripComments:false
DELETE FROM utl_config_parm WHERE utl_config_parm.parm_name = 'ACTION_ENABLE_VIEW_ALL_VENDORS';