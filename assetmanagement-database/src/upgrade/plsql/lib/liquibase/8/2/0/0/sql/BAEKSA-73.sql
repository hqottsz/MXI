--liquibase formatted sql


--changeSet BAEKSA-73:1 stripComments:false
-- Action Config parm for acknowledging a purchase order
INSERT INTO 
	utl_action_config_parm 
	(
		parm_name, parm_value, encrypt_bool, parm_desc, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id
	)
  SELECT
  	'API_ACKNOWLEDGE_PURCHASE_ORDER_REQUEST', 'FALSE',  0, 'Permission to acknowledge a purchase order' , 'TRUE/FALSE', 'FALSE', 1, 'API - MATERIALS', '8.2-SP1', 0
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_action_config_parm WHERE parm_name = 'API_ACKNOWLEDGE_PURCHASE_ORDER_REQUEST' );