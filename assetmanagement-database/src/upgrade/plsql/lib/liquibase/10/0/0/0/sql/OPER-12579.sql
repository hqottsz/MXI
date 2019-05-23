--liquibase formatted sql

--changeSet SWA-4010:1 stripComments:false

INSERT INTO 
	utl_action_config_parm 
	(
		PARM_NAME, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
	)
  SELECT
  	'API_PARTPROVIDERTYPE_PARM', 'FALSE',  0, 'Permission to utilize the PartProviderType API' , 'TRUE/FALSE', 'FALSE', 1, 'API - SYSTEM', '8.2-SP3', 0
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_action_config_parm WHERE PARM_NAME = 'API_PARTPROVIDERTYPE_PARM' );