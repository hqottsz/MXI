--liquibase formatted sql

--changeSet OPER-17626:1 stripComments:false
INSERT INTO
  UTL_ACTION_CONFIG_PARM
  (
    PARM_VALUE, PARM_NAME, PARM_DESC, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
  )
  SELECT
    'false', 'ACTION_ASSIGN_CREW_SHIFT_DAY', 'Permission to assign a crew shift day to task.','TRUE/FALSE', 'FALSE', 1,'Maint - Tasks', '8.2-SP5',0
  FROM
    DUAL
  WHERE
    NOT EXISTS ( SELECT 1 FROM UTL_ACTION_CONFIG_PARM WHERE PARM_NAME = 'ACTION_ASSIGN_CREW_SHIFT_DAY' );
    