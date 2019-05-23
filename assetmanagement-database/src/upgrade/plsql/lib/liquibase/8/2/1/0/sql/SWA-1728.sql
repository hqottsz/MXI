--liquibase formatted sql


--changeSet SWA-1728:1 stripComments:false
/**************************************************************************************
* 
* SWA-1728 Insert script for the Permission to allow API organization request.
*
***************************************************************************************/
INSERT INTO 
	utl_action_config_parm 
	(
		parm_name, parm_value, encrypt_bool, parm_desc, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id
	)
  SELECT
  	'API_ORGANIZATION_REQUEST', 'FALSE',  0, 'Permission to allow API organization request.' , 'TRUE/FALSE', 'FALSE', 1, 'API - ENTERPRISE', '8.2-SP2', 0
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_action_config_parm WHERE parm_name = 'API_ORGANIZATION_REQUEST' );