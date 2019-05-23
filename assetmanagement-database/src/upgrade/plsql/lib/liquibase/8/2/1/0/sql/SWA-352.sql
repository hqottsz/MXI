--liquibase formatted sql


--changeSet SWA-352:1 stripComments:false
/**************************************************************************************
* 
* Insert script for the Open Finance Account API action config parameter
*
***************************************************************************************/
INSERT INTO 
	utl_action_config_parm 
	(
		PARM_NAME, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
	)
  SELECT
  	'API_OPEN_FINANCE_ACCOUNT_REQUEST','FALSE',0,'Permission to open finance accounts.','TRUE/FALSE', 'FALSE', 1, 'API - FINANCE', '8.2-SP2', 0
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_action_config_parm WHERE PARM_NAME = 'API_OPEN_FINANCE_ACCOUNT_REQUEST' );

--changeSet SWA-352:2 stripComments:false
/**************************************************************************************
* 
* Insert script for the Create Vendor API action config parameter
*
***************************************************************************************/
INSERT INTO 
	utl_action_config_parm 
	(
		PARM_NAME, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
	)
  SELECT
  	'API_CREATE_VENDOR_REQUEST','FALSE',0,'Permission to create a vendor.','TRUE/FALSE', 'FALSE', 1, 'API - MATERIALS', '8.2-SP2', 0
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_action_config_parm WHERE PARM_NAME = 'API_CREATE_VENDOR_REQUEST' );

--changeSet SWA-352:3 stripComments:false
/**************************************************************************************
* 
* Insert script for the Update Vendor API action config parameter
*
***************************************************************************************/
INSERT INTO 
	utl_action_config_parm 
	(
		PARM_NAME, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
	)
  SELECT
  	'API_UPDATE_VENDOR_REQUEST','FALSE',0,'Permission to update a vendor.','TRUE/FALSE', 'FALSE', 1, 'API - MATERIALS', '8.2-SP2', 0
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_action_config_parm WHERE PARM_NAME = 'API_UPDATE_VENDOR_REQUEST' );