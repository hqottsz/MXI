--liquibase formatted sql


--changeSet MX-24288:1 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC,
      DEFAULT_VALUE,  MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT
      'TOBE_COMP_NOT_PART_COMPATIBLE','ERROR', 'LOGIC', 0,
      'Severity of the validation for the user attempting to complete a task that is incompatible with an installed part.',
      'USER', 'WARNING/ERROR/INFO', 'ERROR', 1, 'Maint - Tasks', '8.0', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'TOBE_COMP_NOT_PART_COMPATIBLE'  AND parm_type = 'LOGIC' );