--liquibase formatted sql


--changeSet MX-18375:1 stripComments:false
DELETE FROM db_type_config_parm t WHERE t.parm_name = 'ACTION_REMOVE_KIT_CONTENT' and t.db_type_cd = 'MASTER';

--changeSet MX-18375:2 stripComments:false
DELETE FROM db_type_config_parm t WHERE t.parm_name = 'ACTION_UNASSIGN_PART_GROUP_FROM_INSTALL_KIT' and t.db_type_cd = 'MASTER';

--changeSet MX-18375:3 stripComments:false
DELETE FROM db_type_config_parm t WHERE t.parm_name = 'ACTION_UNASSIGN_INSTALL_KIT_FROM_PART_GROUP' and t.db_type_cd = 'MASTER';