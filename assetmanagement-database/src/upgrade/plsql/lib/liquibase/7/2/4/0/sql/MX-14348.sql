--liquibase formatted sql


--changeSet MX-14348:1 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'false', 'ACTION_EDIT_FLIGHT_MEASUREMENT', 'SECURED_RESOURCE','Permission to edit flight measurements.','USER', 'TRUE/FALSE', 'FALSE', 1,'Assembly - Flight Measurements', '6.8.12',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_EDIT_FLIGHT_MEASUREMENT' );