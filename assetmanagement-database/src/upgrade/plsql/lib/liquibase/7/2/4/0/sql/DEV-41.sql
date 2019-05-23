--liquibase formatted sql


--changeSet DEV-41:1 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'true', 'ENFORCE_NEXT_SHOP_VISIT_DEADLINES', 'LOGIC','Determines whether to enforce next shop visit deadlines','GLOBAL', 'TRUE/FALSE', 'TRUE', 1,'Maint - Work Packages', '7.1.0',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ENFORCE_NEXT_SHOP_VISIT_DEADLINES' );    

--changeSet DEV-41:2 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT null, 'sEnforceNSVDeadlines', 'SESSION', 'Enforce Next Shop Visit Deadlines.', 'USER', '', '', 0, 'Maint - Work Packages', '7.1.0', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sEnforceNSVDeadlines' );        

--changeSet DEV-41:3 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'false', 'ACTION_ENFORCE_NSV_DEADLINES', 'SECURED_RESOURCE','Permission to enforce next shop visit deadlines','USER', 'TRUE/FALSE', 'FALSE', 1,'Maint - Work Packages', '7.1.0',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_ENFORCE_NSV_DEADLINES' );                    

--changeSet DEV-41:4 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
SELECT 211, 'core.alert.REMOVAL_REASON_UPDATED_name', 'core.alert.REMOVAL_REASON_UPDATED_description', 'ROLE', null, 'TASK', 'core.alert.REMOVAL_REASON_UPDATED_message', 1, 0, null, 1, 0 
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM UTL_ALERT_TYPE WHERE ALERT_TYPE_ID = 211);      