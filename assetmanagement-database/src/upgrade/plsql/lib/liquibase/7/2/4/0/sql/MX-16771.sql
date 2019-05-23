--liquibase formatted sql


--changeSet MX-16771:1 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'TRUE', 'sOnlyShowOrdersRequireYourAttention', 'SESSION','Whether to show only orders that require your attention on the Authorization Required POs To Do List.','USER',  'TRUE/FALSE', 'TRUE', 0, 'SESSION', '6.8.12', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sOnlyShowOrdersRequireYourAttention' );         