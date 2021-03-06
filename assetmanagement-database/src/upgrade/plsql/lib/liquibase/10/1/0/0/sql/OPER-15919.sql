--liquibase formatted sql

--changeSet OPER-15919:1 stripComments:false
INSERT INTO
  UTL_ACTION_CONFIG_PARM
  (
    PARM_VALUE, PARM_NAME, PARM_DESC, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
  )
  SELECT
    'false', 'ACTION_UNASSIGN_TEMPORARY_ROLE', 'Permission to unassign temporary roles','TRUE/FALSE', 'FALSE', 1,'HR - Users', '8.2-SP4',0
  FROM
    DUAL
  WHERE
    NOT EXISTS ( SELECT 1 FROM UTL_ACTION_CONFIG_PARM WHERE PARM_NAME = 'ACTION_UNASSIGN_TEMPORARY_ROLE' );
