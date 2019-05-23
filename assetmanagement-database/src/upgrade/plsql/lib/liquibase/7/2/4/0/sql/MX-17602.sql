--liquibase formatted sql


--changeSet MX-17602:1 stripComments:false
DELETE FROM db_type_config_parm t WHERE t.parm_name = 'ACTION_ASSIGN_WARRANTY_CONTRACT' and t.db_type_cd = 'OPER'; 

--changeSet MX-17602:2 stripComments:false
DELETE FROM db_type_config_parm t WHERE t.parm_name = 'ACTION_UNASSIGN_WARRANTY_CONTRACT' and t.db_type_cd = 'OPER'; 