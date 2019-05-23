--liquibase formatted sql


--changeSet SWA-642:1 stripComments:false
/**************************************************************************************
* 
* SWA-642 Insert script for the Permission to create an Order Exception API action config parameter
*
***************************************************************************************/
INSERT INTO 
	utl_action_config_parm 
	(
		parm_name, parm_value, encrypt_bool, parm_desc, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id
	)
  SELECT
  	'API_CREATE_ORDER_EXCEPTION_REQUEST', 'FALSE',  0, 'Permission to create an Order Exception' , 'TRUE/FALSE', 'FALSE', 1, 'API - PROCUREMENT', '8.2-SP2', 0
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_action_config_parm WHERE parm_name = 'API_CREATE_ORDER_EXCEPTION_REQUEST' );