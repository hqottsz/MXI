--liquibase formatted sql


--changeSet MX-19172:1 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'ALLOW_ACTIVATE_TASK_DEFN_W_INV_MISSING_MANUFACT_RECEIVE_DATE','LOGIC', 'FALSE', 0,'This confguration parameter dictates whether or not the user is allowed to activate a task definition scheduled from manufactured date or received date even though there are applicable inventory records missing these values.', 'GLOBAL', 'TRUE/FALSE', '0' , 1, 'Maint Program - Task Definitions', '7.5', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ALLOW_ACTIVATE_TASK_DEFN_W_INV_MISSING_MANUFACT_RECEIVE_DATE' );         

--changeSet MX-19172:2 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, active_bool, utl_id)
SELECT 214, 'core.alert.TASK_DEFN_ACTV_FOR_INV_WITH_MISSING_DATES_name', 'core.alert.TASK_DEFN_ACTV_FOR_INV_WITH_MISSING_DATES_description', 'ROLE', null, 'TASKDEFN', 'core.alert.TASK_DEFN_ACTV_FOR_INV_WITH_MISSING_DATES_message', 1, 0, 1, 0 
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM UTL_ALERT_TYPE WHERE ALERT_TYPE_ID = 214);            