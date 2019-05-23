--liquibase formatted sql


--changeSet DEV-1294:1 stripComments:false
/*****************************************************************************
 * Adding new configuration parameter 
 *    ADD_VENDOR_PART_PRICE
 *    EDIT_VENDOR_PART_PRICE
 *    DUPLICATE_VENDOR_PART_PRICE
 ******************************************************************************/
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC,
      DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'ACTION_ADD_VENDOR_PART_PRICE', 'TRUE', 'SECURED_RESOURCE', 0,
      'Permission to  add vendor part price.',
      'USER', 'TRUE/FALSE', 'FALSE', 1, 'Purchasing - Prices', '8.0', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_ADD_VENDOR_PART_PRICE' AND parm_type = 'SECURED_RESOURCE' );                

--changeSet DEV-1294:2 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
	PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC,
	DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'ACTION_EDIT_VENDOR_PART_PRICE', 'TRUE', 'SECURED_RESOURCE', 0,
	'Permission to edit vendor part price.',
	'USER', 'TRUE/FALSE', 'FALSE', 1, 'Purchasing - Prices', '8.0', 0
   FROM
	dual
  WHERE
	NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_EDIT_VENDOR_PART_PRICE' AND parm_type = 'SECURED_RESOURCE' );

--changeSet DEV-1294:3 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
	PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC,
	DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'ACTION_DUPLICATE_VENDOR_PART_PRICE', 'TRUE', 'SECURED_RESOURCE', 0,
	'Permission to make vendor part price duplicate.',
	'USER', 'TRUE/FALSE', 'FALSE', 1, 'Purchasing - Prices', '8.0', 0
   FROM
	dual
  WHERE
	NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_DUPLICATE_VENDOR_PART_PRICE' AND parm_type = 'SECURED_RESOURCE' );    