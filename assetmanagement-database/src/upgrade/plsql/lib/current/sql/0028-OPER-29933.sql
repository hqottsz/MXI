--liquibase formatted sql

--changeSet OPER-29933:1 stripComments:false
UPDATE utl_config_parm 
	SET MODIFIED_IN = '8.3-SP1'
	WHERE parm_name = 'DEFER_ON_APPROVAL';

--changeSet OPER-29933:2 stripComments:false
UPDATE utl_config_parm 
	SET MODIFIED_IN = '8.3-SP1'
	WHERE parm_name = 'SHOW_PART_REQUIREMENT_REFERENCE';

	--changeSet OPER-29933:3 stripComments:false
UPDATE utl_config_parm 
	SET MODIFIED_IN = '8.3-SP1'
	WHERE parm_name = 'PART_REQUIREMENT_REFERENCE_MANDATORY_INV_CLASSES';

	--changeSet OPER-29933:4 stripComments:false
UPDATE utl_config_parm 
	SET MODIFIED_IN = '8.3-SP1'
	WHERE parm_name = 'PART_REQUIREMENT_REFERENCE_MANDATORY_ACTIONS';


	

