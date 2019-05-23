--liquibase formatted sql


--changeSet MX-18074:1 stripComments:false
DELETE FROM db_type_config_parm t WHERE t.parm_name = 'ALLOW_SAME_USER_INSP_SIGNOFF' and t.db_type_cd = 'OPER'; 

--changeSet MX-18074:2 stripComments:false
DELETE FROM utl_user_parm t where t.parm_name = 'ALLOW_SAME_USER_INSP_SIGNOFF';

--changeSet MX-18074:3 stripComments:false
DELETE FROM utl_role_parm t where t.parm_name = 'ALLOW_SAME_USER_INSP_SIGNOFF';

--changeSet MX-18074:4 stripComments:false
DELETE FROM utl_config_parm t where t.parm_name = 'ALLOW_SAME_USER_INSP_SIGNOFF';