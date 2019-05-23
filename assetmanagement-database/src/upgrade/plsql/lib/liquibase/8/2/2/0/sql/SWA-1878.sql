--liquibase formatted sql


--changeSet SWA-1878:1 stripComments:false
/**************************************************************************************
* 
* SWA-1878 Insert script for the Permission to allow API inventoy history note retrieval.
*
***************************************************************************************/
INSERT INTO 
  utl_action_config_parm
  (
    parm_name, parm_value, encrypt_bool, parm_desc, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id
  )
  SELECT
    'API_INVENTORY_HISTORY_NOTE_REQUEST', 'FALSE',  0, 'Permission to allow API inventoy history note retrieval' , 'TRUE/FALSE', 'FALSE', 1, 'API - MATERIALS', '8.2-SP3', 0
  FROM
    dual
  WHERE
    NOT EXISTS ( SELECT 1 FROM utl_action_config_parm WHERE parm_name = 'API_INVENTORY_HISTORY_NOTE_REQUEST' );