--liquibase formatted sql


--changeSet DEV-404:1 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'REPETITIVE_TASK_TO_MONITOR_FAULT','LOGIC','ERROR', 0,'Severity of the validation for the user is attempting to complete fault having repetitive task to monitor it.', 'USER', 'WARNING/ERROR/INFO', 'ERROR', 1, 'Maint - Tasks', '7.2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'REPETITIVE_TASK_TO_MONITOR_FAULT' );         