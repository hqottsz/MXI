--liquibase formatted sql


--changeSet SWA-1249:1 stripComments:false
/**************************************************************************************
*
* SWA-1249 Insert script for the Permission to process an Aeroexchange Rejection
*
***************************************************************************************/
INSERT INTO
   utl_action_config_parm
   (
      parm_name, parm_value, encrypt_bool, parm_desc, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id
   )
   SELECT
      'API_AEROEXCHANGE_REJECTIONS', 'FALSE',  0, 'Permission to receive Aeroexchange Rejections' , 'TRUE/FALSE', 'FALSE', 1, 'API - PROCUREMENT', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_action_config_parm WHERE parm_name = 'API_AEROEXCHANGE_REJECTIONS' );