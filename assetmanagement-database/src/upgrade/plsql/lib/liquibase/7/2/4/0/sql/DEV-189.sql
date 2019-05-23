--liquibase formatted sql


--changeSet DEV-189:1 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'HIDE_SCHED_LABOUR_HOURS', 'false', 'SECURED_RESOURCE', 0, 'Determines whether a user is allowed to view scheduled labour information.' , 'USER', 'TRUE/FALSE', 'FALSE', 1, 'Task - Subtypes', '7.5', 0 
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'HIDE_SCHED_LABOUR_HOURS' );   

--changeSet DEV-189:2 stripComments:false
INSERT INTO
   db_type_config_parm
   (
      PARM_NAME, DB_TYPE_CD, PARM_TYPE
   )
   SELECT 'HIDE_SCHED_LABOUR_HOURS', 'OPER', 'SECURED_RESOURCE'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'HIDE_SCHED_LABOUR_HOURS' and db_type_cd = 'OPER' );            