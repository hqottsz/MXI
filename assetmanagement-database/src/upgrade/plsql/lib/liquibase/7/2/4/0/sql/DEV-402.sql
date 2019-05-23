--liquibase formatted sql


--changeSet DEV-402:1 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
     PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'ADHOC_REPEAT_TASK_FORECAST_QT', '0', 'LOGIC', 0,'This parameter dictates the number of forecasted task to be created when creating a repetitive task for a fault.', 'GLOBAL', 'NON-NEGATIVE NUMBER', '0', 1, 'Core Logic', '7.2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ADHOC_REPEAT_TASK_FORECAST_QT' );                     