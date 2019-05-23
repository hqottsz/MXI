--liquibase formatted sql


--changeSet MX-17596:1 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'false', 'ACTION_ASSIGN_USER_TO_QUARANTINE_ACTION', 'SECURED_RESOURCE','Permission to assign a user to a quarantine corrective action','USER', 'TRUE/FALSE', 'FALSE', 1, 'Supply - Inventory', '7.0',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_ASSIGN_USER_TO_QUARANTINE_ACTION' );                

--changeSet MX-17596:2 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'false', 'ACTION_ASSIGN_DEPT_TO_QUARANTINE_ACTION', 'SECURED_RESOURCE','Permission to assign a department to a quarantine corrective action','USER', 'TRUE/FALSE', 'FALSE', 1, 'Supply - Inventory', '7.0',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_ASSIGN_DEPT_TO_QUARANTINE_ACTION' );         

--changeSet MX-17596:3 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'false', 'ACTION_ADD_QUARANTINE_NOTE', 'SECURED_RESOURCE','Permission to add a quarantine note','USER', 'TRUE/FALSE', 'FALSE', 1, 'Supply - Inventory', '7.0',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_ADD_QUARANTINE_NOTE' );         