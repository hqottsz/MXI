--liquibase formatted sql

--changeSet OPER-14629:1 stripComments:false
INSERT INTO
  UTL_ACTION_CONFIG_PARM
  (
    PARM_VALUE, PARM_NAME, PARM_DESC, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
  )
  SELECT
    'false', 'ACTION_ASSIGN_TEMPORARY_ROLE', 'Permission to assign a temporary role to a user','TRUE/FALSE', 'FALSE', 1,'HR - Users', '8.2-SP4',0
  FROM
    DUAL
  WHERE
    NOT EXISTS ( SELECT 1 FROM UTL_ACTION_CONFIG_PARM WHERE PARM_NAME = 'ACTION_ASSIGN_TEMPORARY_ROLE' );
    
    
--changeSet OPER-14629:2 stripComments:false
INSERT INTO
  UTL_ACTION_CONFIG_PARM
  (
    PARM_VALUE, PARM_NAME, PARM_DESC, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
  )
  SELECT
    'TRUE', 'ACTION_JSP_WEB_USER_ASSIGN_TEMPORARY_ROLE', 'Permission to access the Assign Temporary Role page.', 'TRUE/FALSE', 'TRUE', 1, 'JSP Permission', '8.2-SP4', 0
  FROM
    DUAL
  WHERE
    NOT EXISTS ( SELECT 1 FROM UTL_ACTION_CONFIG_PARM WHERE PARM_NAME = 'ACTION_JSP_WEB_USER_ASSIGN_TEMPORARY_ROLE' );
