--liquibase formatted sql


--changeSet AM-19:1 stripComments:false
/**************************************************************************************
* 
* AM-19 Permission to allow API for aircraft flight information
*
***************************************************************************************/
INSERT INTO 
  utl_action_config_parm
  (
    parm_name, parm_value, encrypt_bool, parm_desc, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id
  )
  SELECT
    'API_AIRCRAFT_FLIGHTINFO_REQUEST', 'FALSE',  0, 'Permission to allow API for aircraft flight information' , 'TRUE/FALSE', 'FALSE', 1, 'API - ASSET', '8.2-SP3', 0
  FROM
    dual
  WHERE
    NOT EXISTS ( SELECT 1 FROM utl_action_config_parm WHERE parm_name = 'API_AIRCRAFT_FLIGHTINFO_REQUEST' );