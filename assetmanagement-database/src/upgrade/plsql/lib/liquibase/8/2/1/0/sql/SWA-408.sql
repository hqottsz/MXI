--liquibase formatted sql


--changeSet SWA-408:1 stripComments:false
/**************************************************************************************
* 
* SWA-70 Insert script for the UnapproveVendor API action config parameter
*
***************************************************************************************/
INSERT INTO 
	utl_action_config_parm 
	(
		PARM_NAME, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
	)
  SELECT
  	'API_UNAPPROVE_VENDOR_REQUEST', 'FALSE',  0, 'Permission to unapprove a vendor.' , 'TRUE/FALSE', 'FALSE', 1, 'API - MATERIALS', '8.2-SP2', 0
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_action_config_parm WHERE PARM_NAME = 'API_UNAPPROVE_VENDOR_REQUEST' );