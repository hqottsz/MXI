--liquibase formatted sql

/*
 *  Add configuration parameter ACTION_CHANGE_PURCHASE_CONTACT.
 */

--changeSet OPER-23048:1 stripComments:false
INSERT INTO
  utl_action_config_parm
  (
    PARM_NAME, PARM_VALUE, PARM_DESC, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
  )
  SELECT
     'ACTION_CHANGE_PURCHASE_CONTACT', 'FALSE', 'Permission to use the Change Contact button to change the contact name of a buyer or agent on selected orders.','TRUE/FALSE', 'FALSE', 1, 'Purchasing - Purchase Orders', '8.3', 0
  FROM
    dual
  WHERE
    NOT EXISTS ( SELECT 1 FROM utl_action_config_parm WHERE PARM_NAME = 'ACTION_CHANGE_PURCHASE_CONTACT' );