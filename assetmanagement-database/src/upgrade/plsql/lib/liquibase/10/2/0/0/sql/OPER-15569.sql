--liquibase formatted sql

--changeSet OPER-15569:1 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_NAME, PARM_TYPE, PARM_VALUE, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'sVisibleCrews', 'SESSION', null, 'Crews visible on the assigned to crew todo list.', 'USER', null, 'STRING', 'SESSION', '8.2-SP5', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM UTL_CONFIG_PARM WHERE PARM_NAME = 'sVisibleCrews' );

--changeSet OPER-15569:2 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_NAME, PARM_TYPE, PARM_VALUE, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'sVisibleTasks', 'SESSION', null, 'Tasks visible on the assigned to crew todo list.', 'USER', null, 'STRING', 'SESSION', '8.2-SP5', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM UTL_CONFIG_PARM WHERE PARM_NAME = 'sVisibleTasks' );