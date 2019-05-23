--liquibase formatted sql


--changeSet MX-18541:1 stripComments:false
DELETE FROM utl_user_parm
WHERE parm_name = 'ACTION_ADD_ESIG_CORR';

--changeSet MX-18541:2 stripComments:false
DELETE FROM utl_role_parm
WHERE parm_name = 'ACTION_ADD_ESIG_CORR';

--changeSet MX-18541:3 stripComments:false
DELETE FROM db_type_config_parm
WHERE parm_name = 'ACTION_ADD_ESIG_CORR';

--changeSet MX-18541:4 stripComments:false
DELETE FROM utl_config_parm
WHERE parm_name = 'ACTION_ADD_ESIG_CORR';