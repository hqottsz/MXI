--liquibase formatted sql


--changeSet DEV-76:1 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'true', 'ACTION_ENFORCE_WORKSCOPE_ORDER', 'SECURED_RESOURCE','Permission to Toggle Enforce workscope order for a Work package','USER', 'TRUE/FALSE', 'true', 1,'Maint - Tasks', '7.1.0',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_ENFORCE_WORKSCOPE_ORDER' );                

--changeSet DEV-76:2 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'true', 'ACTION_EDIT_WORKSCOPE_ORDER', 'SECURED_RESOURCE','Permission to Edit workscope order','USER', 'TRUE/FALSE', 'true', 1,'Maint - Tasks', '7.1.0',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_EDIT_WORKSCOPE_ORDER' );            

--changeSet DEV-76:3 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'true', 'ACTION_MOVE_UP_WORKSCOPE_ORDER', 'SECURED_RESOURCE','Permission to Move Up the  workscope order','USER', 'TRUE/FALSE', 'true', 1,'Maint - Tasks', '7.1.0',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_MOVE_UP_WORKSCOPE_ORDER' );                

--changeSet DEV-76:4 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'true', 'ACTION_MOVE_DOWN_WORKSCOPE_ORDER', 'SECURED_RESOURCE','Permission to Move Down the workscope order','USER', 'TRUE/FALSE', 'true', 1,'Maint - Tasks', '7.1.0',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_MOVE_DOWN_WORKSCOPE_ORDER' );            

--changeSet DEV-76:5 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'true', 'ACTION_ASSIGN_WS_TASK_LOCATION', 'SECURED_RESOURCE','Permission to Assign Location to a Workscope Task','USER', 'TRUE/FALSE', 'true', 1,'Maint - Tasks', '7.1.0',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_ASSIGN_WS_TASK_LOCATION' );                         

--changeSet DEV-76:6 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
SELECT 212,'core.alert.ENFORCING_WORKSCOPE_ORDER_FAILED_name','core.alert.ENFORCING_WORKSCOPE_ORDER_FAILED_description','ROLE',null,'TASK','core.alert.ENFORCING_WORKSCOPE_ORDER_FAILED_message',1,0,null,1,0
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM UTL_ALERT_TYPE WHERE ALERT_TYPE_ID = 212);