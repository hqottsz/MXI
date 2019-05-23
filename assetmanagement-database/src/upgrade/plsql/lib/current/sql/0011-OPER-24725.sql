--liquibase formatted sql

--changeSet OPER-24725:1 stripComments:false
UPDATE utl_config_parm 
	SET default_value = 200
	WHERE parm_name = 'TREE_TABLE_SIZE_LIMIT';

--changeSet OPER-24725:2 stripComments:false
UPDATE utl_config_parm 
	SET default_value = 500
	WHERE parm_name = 'MAX_TREE_TABLE_SIZE_LIMIT';
