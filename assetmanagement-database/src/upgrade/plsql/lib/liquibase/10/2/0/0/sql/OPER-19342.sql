--liquibase formatted sql

/*
 *  Add configuration parameter ACTION_REMOVE_IGNORE_LOCATION.
 */

--changeSet OPER-19342:1 stripComments:false
INSERT INTO
  utl_action_config_parm
  (
    PARM_NAME, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
  )
  SELECT
     'ACTION_REMOVE_IGNORE_LOCATION', 'FALSE',  0, 'Permission to remove a location from the list of locations that auto-reservation ignores. Includes permission for the remove button and servlet.' , 'TRUE/FALSE', 'FALSE', 1, 'Org - Locations', '8.2-SP5', 0
  FROM
    dual
  WHERE
    NOT EXISTS ( SELECT 1 FROM utl_action_config_parm WHERE PARM_NAME = 'ACTION_REMOVE_IGNORE_LOCATION' );
