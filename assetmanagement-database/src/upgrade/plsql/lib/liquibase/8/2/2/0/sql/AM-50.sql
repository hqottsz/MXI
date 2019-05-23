--liquibase formatted sql


--changeSet AM-50:1 stripComments:false
INSERT INTO 
	utl_action_config_parm
	(
	   parm_name, parm_value, encrypt_bool, parm_desc, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id
	)
  SELECT
  	'ACTION_EDIT_BIN_LOCATION', 'FALSE',  0, 'Permission required to edit a Bin location.' , 'TRUE/FALSE', 'FALSE', 1, 'Org - Locations', '8.2-SP3', 0
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_action_config_parm WHERE parm_name = 'ACTION_EDIT_BIN_LOCATION' );