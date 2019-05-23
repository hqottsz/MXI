--liquibase formatted sql

--changeset OPER-9723:1 stripComments:false
INSERT INTO   
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT null, 'sWorkScopeTabOptionalColumns', 'SESSION', 'Controls coulmn visibility on the Workscope Tab on Workpackage Details page', 'USER', 'STRING', null, 0, 'SESSION', '8.2-SP3', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM UTL_CONFIG_PARM WHERE PARM_NAME = 'sWorkScopeTabOptionalColumns' );

