--liquibase formatted sql


--changeSet DEV-80:1 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, ENCRYPT_BOOL, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT null, 'sVisibleLocations', 'SESSION','Locations that are visible on the assigned to crew todo list.','USER', 'STRING', null, 1, 0, 'SESSION', '7.1', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sVisibleLocations' );         