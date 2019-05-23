--liquibase formatted sql


--changeSet BAEKSA-9:1 stripComments:false
-- Action Config parm for create history note for repair order
INSERT INTO 
	UTL_ACTION_CONFIG_PARM 
	(
		parm_name, parm_value, encrypt_bool, parm_desc, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
	)
  SELECT
  	'API_REFERENCE_REASON_REQUEST', 'FALSE',  0, 'Permission to allow API to search from reference remove reason' , 'TRUE/FALSE', 'FALSE', 1, 'API - MATERIALS', '8.2', 0
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM UTL_ACTION_CONFIG_PARM WHERE PARM_NAME = 'API_REFERENCE_REASON_REQUEST' );