--liquibase formatted sql

--changeSet OPER-9247:1 stripComments:false
/**************************************************************************************
* 
* Insert script for the Permission to allow API deferral reference creation
*
***************************************************************************************/
INSERT INTO 
   utl_action_config_parm
   (
      parm_name, parm_value, encrypt_bool, parm_desc, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id
   )
SELECT
   'API_ACTION_CREATE_DEFER_REF', 'FALSE',  0, 'Permission to create a deferral reference configuration' , 'TRUE/FALSE', 'FALSE', 1, 'API - MAINTENANCE', '8.2-SP3', 0
FROM
   DUAL
WHERE 
   NOT EXISTS (
      SELECT 1 FROM utl_action_config_parm WHERE parm_name = 'API_ACTION_CREATE_DEFER_REF'
   );
   
--changeSet OPER-9247:2 stripComments:false
/**************************************************************************************
* 
* Insert script for the Permission to allow API deferral reference edition
*
***************************************************************************************/
INSERT INTO 
   utl_action_config_parm
   (
      parm_name, parm_value, encrypt_bool, parm_desc, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id
   )
SELECT
   'API_ACTION_EDIT_DEFER_REF', 'FALSE',  0, 'Permission to edit a deferral reference configuration' , 'TRUE/FALSE', 'FALSE', 1, 'API - MAINTENANCE', '8.2-SP3', 0
FROM
   DUAL
WHERE 
   NOT EXISTS (
      SELECT 1 FROM utl_action_config_parm WHERE parm_name = 'API_ACTION_EDIT_DEFER_REF'
   );   
   
--changeSet OPER-9247:3 stripComments:false
/**************************************************************************************
* 
* Insert script for the Permission to allow API deferral reference deletion
*
***************************************************************************************/
INSERT INTO 
   utl_action_config_parm
   (
      parm_name, parm_value, encrypt_bool, parm_desc, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id
   )
SELECT
   'API_ACTION_DELETE_DEFER_REF', 'FALSE',  0, 'Permission to delete a deferral reference configuration' , 'TRUE/FALSE', 'FALSE', 1, 'API - MAINTENANCE', '8.2-SP3', 0
FROM
   DUAL
WHERE 
   NOT EXISTS (
      SELECT 1 FROM utl_action_config_parm WHERE parm_name = 'API_ACTION_DELETE_DEFER_REF'
   );      