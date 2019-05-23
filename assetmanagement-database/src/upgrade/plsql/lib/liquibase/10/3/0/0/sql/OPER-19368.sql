--liquibase formatted sql

--changeSet OPER-19368:1 stripComments:false
INSERT INTO
  utl_action_config_parm
  (
    PARM_NAME, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
  )
  SELECT
     'ACTION_ADD_PREFERRED_LOCATION', 'FALSE',  0, 'Permission to add a preferred materials location configuration. Includes permission for the add button and servlet.' , 'TRUE/FALSE', 'FALSE', 1, 'Org - Locations', '8.3', 0
  FROM
    dual
  WHERE
    NOT EXISTS ( SELECT 1 FROM utl_action_config_parm WHERE PARM_NAME = 'ACTION_ADD_PREFERRED_LOCATION' );
	
--changeSet OPER-19368:2 stripComments:false
INSERT INTO
  utl_action_config_parm
  (
    PARM_NAME, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
  )
  SELECT
     'ACTION_REMOVE_PREFERRED_LOCATION', 'FALSE',  0, 'Permission to remove a preferred materials location configuration. Includes permission for the remove button and servlet.' , 'TRUE/FALSE', 'FALSE', 1, 'Org - Locations', '8.3', 0
  FROM
    dual
  WHERE
    NOT EXISTS ( SELECT 1 FROM utl_action_config_parm WHERE PARM_NAME = 'ACTION_REMOVE_PREFERRED_LOCATION' );
