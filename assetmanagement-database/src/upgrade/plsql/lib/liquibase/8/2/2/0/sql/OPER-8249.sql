--liquibase formatted sql


--changeSet OPER-8249:1 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT null, 'sUSSTGOptionalColumns', 'SESSION', 'Unserviceable Staging todo list hidden columns', 'USER', 'STRING', null, 0, 'SESSION', '8.2-SP3', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM UTL_CONFIG_PARM WHERE PARM_NAME = 'sUSSTGOptionalColumns' );