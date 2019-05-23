--liquibase formatted sql

--changeSet OPER-10184:1 stripComments:false
/**************************************************************************************
* 
* Insert script for the Permission to allow part request note
*
***************************************************************************************/
INSERT INTO 
   utl_action_config_parm
   (
      parm_value, parm_name, parm_desc, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id
   )
SELECT
   'false', 'ACTION_ADD_PART_REQUEST_NOTE', 'Permission to add the note on the part request.','TRUE/FALSE', 'FALSE', 1,'Supply - Part Requests', '8.2-SP3',0
FROM
   DUAL
WHERE 
   NOT EXISTS (
      SELECT 1 FROM utl_action_config_parm WHERE parm_name = 'ACTION_ADD_PART_REQUEST_NOTE'
   );