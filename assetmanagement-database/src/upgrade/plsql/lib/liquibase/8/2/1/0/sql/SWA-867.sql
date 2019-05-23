--liquibase formatted sql


--changeSet SWA-867:1 stripComments:false
/**************************************************************************************
* 
* SWA-867 Insert script for the Permission to update a purchase order API action config parameter
*
***************************************************************************************/
INSERT INTO 
	utl_action_config_parm 
	(
		parm_name, parm_value, encrypt_bool, parm_desc, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id
	)
  SELECT
  	'API_UPDATE_PURCHASE_ORDER_REQUEST', 'FALSE',  0, 'Permission to update a Purchase Order' , 'TRUE/FALSE', 'FALSE', 1, 'API - MATERIALS', '8.2-SP2', 0
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_action_config_parm WHERE parm_name = 'API_UPDATE_PURCHASE_ORDER_REQUEST' );