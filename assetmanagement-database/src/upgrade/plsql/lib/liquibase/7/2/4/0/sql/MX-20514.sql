--liquibase formatted sql


--changeSet MX-20514:1 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC,
      DEFAULT_VALUE,  MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'TOBE_INST_BATCH_INCORRECT_QTY', 'ERROR', 'LOGIC', 0,
      'Severity of the validation for the user if attempting to install more batch pieces than previously scheduled.',
      'USER', 'WARNING/ERROR/INFO', 'ERROR', 1, 'Maint - Tasks', '7.5', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'TOBE_INST_BATCH_INCORRECT_QTY' );      

--changeSet MX-20514:2 stripComments:false
UPDATE UTL_CONFIG_PARM SET MODIFIED_IN = '7.5' WHERE PARM_NAME = 'TOBE_INST_BATCH_INCORRECT_QTY';