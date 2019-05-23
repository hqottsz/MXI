--liquibase formatted sql


--changeSet OPER-8283:1 stripComments:false
/**************************************************************************************
*
* Request Deferral Button Permission parameter
*
***************************************************************************************/
INSERT INTO
   utl_action_config_parm
   (
      parm_name, parm_value, encrypt_bool, parm_desc, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id
   )
SELECT
   'ACTION_REQUEST_DEFERRAL', 'FALSE',  0, 'Permission to create a deferral request for a fault' , 'TRUE/FALSE', 'FALSE', 1, 'Maint - Faults', '8.2-SP3', 0
FROM
   DUAL
WHERE
   NOT EXISTS (
      SELECT 1 FROM utl_action_config_parm WHERE parm_name = 'ACTION_REQUEST_DEFERRAL'
   );
