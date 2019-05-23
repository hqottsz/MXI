--liquibase formatted sql

--changeSet OPER-11126:1 stripComments:false
INSERT INTO 
	utl_action_config_parm 
	(
		PARM_NAME, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
	)
  SELECT
  	'API_TASK_EVENT_STATUS_CODE', 'FALSE',  0, 'Permission to utilize the Task Event Status Code API' , 'TRUE/FALSE', 'FALSE', 1, 'API - SYSTEM', '8.2-SP3', 0
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_action_config_parm WHERE PARM_NAME = 'API_TASK_EVENT_STATUS_CODE' );

	
--changeSet OPER-11126:2 stripComments:false
INSERT INTO 
	utl_action_config_parm 
	(
		PARM_NAME, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
	)
  SELECT
  	'API_FAULT_RESULT_CODE', 'FALSE',  0, 'Permission to utilize the Fault Result Code API' , 'TRUE/FALSE', 'FALSE', 1, 'API - SYSTEM', '8.2-SP3', 0
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_action_config_parm WHERE PARM_NAME = 'API_FAULT_RESULT_CODE' );