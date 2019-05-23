--liquibase formatted sql

--changeSet LMOC-695:1 stripComments:false
/**************************************************************************************
* 
* LMOC-695 Insert script for the Permission to allow API deferral reference searching.
*
***************************************************************************************/
INSERT INTO 
   utl_action_config_parm
   (
      parm_name, parm_value, encrypt_bool, parm_desc, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id
   )
SELECT
   'API_SEARCH_DEFERRAL_REFERENCE_REQUEST', 'FALSE',  0, 'Permission to search deferral references' , 'TRUE/FALSE', 'FALSE', 1, 'API - MAINTENANCE', '8.2-SP3', 0
FROM
   DUAL
WHERE 
   NOT EXISTS (
      SELECT 1 FROM utl_action_config_parm WHERE parm_name = 'API_SEARCH_DEFERRAL_REFERENCE_REQUEST'
   );