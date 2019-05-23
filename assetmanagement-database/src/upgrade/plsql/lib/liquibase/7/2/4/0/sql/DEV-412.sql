--liquibase formatted sql


--changeSet DEV-412:1 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'false', 'ACTION_RESCHED_TASK', 'SECURED_RESOURCE','Permission to reschedule a task with custom deadline.','USER', 'TRUE/FALSE', 'FALSE', 1,'Maint - Tasks', '7.2',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_RESCHED_TASK' );         

--changeSet DEV-412:2 stripComments:false
INSERT INTO
   db_type_config_parm
   (
      PARM_NAME, DB_TYPE_CD, PARM_TYPE
   )
   SELECT 'ACTION_RESCHED_TASK', 'SECURED_RESOURCE','OPER'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_RESCHED_TASK' and db_type_cd = 'OPER' );                  