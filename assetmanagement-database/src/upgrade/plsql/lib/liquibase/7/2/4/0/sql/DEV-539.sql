--liquibase formatted sql


--changeSet DEV-539:1 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE,
      MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'ENABLE_FINAL_TASK_CERT_CHECK_TASK', 'FALSE', 'LOGIC', 0, 'This configuration parameter enables/disables the final certification work flow for aircraft tasks.',
   'GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'Task-Final Certification', '7.1-SP2', 0 
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ENABLE_FINAL_TASK_CERT_CHECK_TASK' );                  

--changeSet DEV-539:2 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE,
      MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'ENABLE_FINAL_TASK_CERT_ASSEMBLY_TASK', 'FALSE', 'LOGIC', 0, 'This configuration parameter enables/disables the final certification work flow for component tasks.', 
   'GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'Task-Final Certification', '7.1-SP2', 0 
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ENABLE_FINAL_TASK_CERT_ASSEMBLY_TASK' );      

--changeSet DEV-539:3 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE,
      MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'ACTION_ALLOW_FINAL_CERT_ASSEMBLY_TASK', 'false', 'SECURED_RESOURCE', 0, 
  'Enables the user to perform final certification on component tasks.  I.e. tasks assigned to component work packages; the
   work package is scheduled at location types defined in SCHEDULE_LOCATIONS_ASSEMBLY configuration parameter.' , 'USER',
  'TRUE/FALSE', 'FALSE', 1, 'Task - Final Certification', '7.1-SP2', 0 
  FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_ALLOW_FINAL_CERT_ASSEMBLY_TASK');

--changeSet DEV-539:4 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE,
      MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'ACTION_ALLOW_FINAL_CERT_ASSEMBLY_TASK_AT_CHECK_LOC', 'false', 'SECURED_RESOURCE', 0, 
  'Enables the user to perform final certification on component tasks.  I.e. tasks assigned to component work packages; the
   work package is scheduled at location types defined in SCHEDULE_LOCATIONS_CHECK configuration parameter.' , 'USER',
  'TRUE/FALSE', 'FALSE', 1, 'Task - Final Certification', '7.1-SP2', 0 
  FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_ALLOW_FINAL_CERT_ASSEMBLY_TASK_AT_CHECK_LOC');

--changeSet DEV-539:5 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE,
      MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'ACTION_ALLOW_FINAL_CERT_CHECK_TASK', 'false', 'SECURED_RESOURCE', 0, 
   'Enables the user to perform final certification on aircraft tasks.  I.e. tasks assigned to aircraft work packages.' ,
   'USER', 'TRUE/FALSE', 'FALSE', 1, 'Task - Final Certification', '7.1-SP2', 0 
  FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_ALLOW_FINAL_CERT_CHECK_TASK');

--changeSet DEV-539:6 stripComments:false
INSERT INTO
   DB_TYPE_CONFIG_PARM
   (
      PARM_NAME, PARM_TYPE, DB_TYPE_CD
   )
   SELECT 'ACTION_ALLOW_FINAL_CERT_ASSEMBLY_TASK', 'SECURED_RESOURCE', 'OPER' 
  FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM DB_TYPE_CONFIG_PARM WHERE PARM_NAME = 'ACTION_ALLOW_FINAL_CERT_ASSEMBLY_TASK');

--changeSet DEV-539:7 stripComments:false
INSERT INTO
   DB_TYPE_CONFIG_PARM
   (
      PARM_NAME, PARM_TYPE, DB_TYPE_CD
   )
   SELECT 'ACTION_ALLOW_FINAL_CERT_ASSEMBLY_TASK_AT_CHECK_LOC', 'SECURED_RESOURCE', 'OPER' 
  FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM DB_TYPE_CONFIG_PARM WHERE PARM_NAME = 'ACTION_ALLOW_FINAL_CERT_ASSEMBLY_TASK_AT_CHECK_LOC');

--changeSet DEV-539:8 stripComments:false
INSERT INTO
   DB_TYPE_CONFIG_PARM
   (
      PARM_NAME, PARM_TYPE, DB_TYPE_CD
   )
   SELECT 'ACTION_ALLOW_FINAL_CERT_CHECK_TASK', 'SECURED_RESOURCE', 'OPER' 
  FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM DB_TYPE_CONFIG_PARM WHERE PARM_NAME = 'ACTION_ALLOW_FINAL_CERT_CHECK_TASK');