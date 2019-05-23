--liquibase formatted sql


--changeSet DEV-72:1 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'true', 'ACTION_CREATE_CAPACITY_PATTERN', 'SECURED_RESOURCE','Determines whether a user is allowed to create a capacity pattern.','USER', 'TRUE/FALSE', 'TRUE', 1,'Assembly - Subtypes', '7.5',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_CREATE_CAPACITY_PATTERN' );                  

--changeSet DEV-72:2 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'true', 'ACTION_DELETE_CAPACITY_PATTERN', 'SECURED_RESOURCE','Determines whether a user is allowed to delete a capacity pattern.','USER', 'TRUE/FALSE', 'TRUE', 1,'Assembly - Subtypes', '7.5',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_DELETE_CAPACITY_PATTERN' );         

--changeSet DEV-72:3 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'true', 'ACTION_DUPLICATE_CAPACITY_PATTERN', 'SECURED_RESOURCE','Determines whether a user is allowed to duplicate a capacity pattern.','USER', 'TRUE/FALSE', 'TRUE', 1,'Assembly - Subtypes', '7.5',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_DUPLICATE_CAPACITY_PATTERN' );                  

--changeSet DEV-72:4 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'true', 'ACTION_EDIT_LABOR_SKILLS', 'SECURED_RESOURCE','Determines whether a user is allowed to edit labor skills.','USER', 'TRUE/FALSE', 'TRUE', 1,'Assembly - Subtypes', '7.5',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_EDIT_LABOR_SKILLS' );         

--changeSet DEV-72:5 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'true', 'ACTION_EDIT_SHIFTS_FOR_DAY', 'SECURED_RESOURCE','Determines whether a user is allowed to edit shifts for a day.','USER', 'TRUE/FALSE', 'TRUE', 1,'Assembly - Subtypes', '7.5',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_EDIT_SHIFTS_FOR_DAY' );                  

--changeSet DEV-72:6 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'true', 'ACTION_EDIT_LABOR_PROFILE', 'SECURED_RESOURCE','Determines whether a user is allowed to edit labor profiles.','USER', 'TRUE/FALSE', 'TRUE', 1,'Assembly - Subtypes', '7.5',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_EDIT_LABOR_PROFILE' );         

--changeSet DEV-72:7 stripComments:false
INSERT INTO
   UTL_TODO_TAB
   (
      TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC, UTL_ID
   )
   SELECT 11025, 'idTabCapacityPatternSetup', 'web.todotab.CAPACITY_PATTERN_SETUP', '/web/todolist/CapacityPatternSetup.jsp', 'web.todotab.CAPACITY_PATTERN_SETUP_TAB', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM UTL_TODO_TAB WHERE TODO_TAB_ID = 11025 );         

--changeSet DEV-72:8 stripComments:false
INSERT INTO
   db_type_config_parm
   (
      PARM_NAME, DB_TYPE_CD, PARM_TYPE
   )
   SELECT 'ACTION_CREATE_CAPACITY_PATTERN', 'OPER', 'SECURED_RESOURCE'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_CREATE_CAPACITY_PATTERN' and db_type_cd = 'OPER' );

--changeSet DEV-72:9 stripComments:false
INSERT INTO
   db_type_config_parm
   (
      PARM_NAME, DB_TYPE_CD, PARM_TYPE
   )
   SELECT 'ACTION_DELETE_CAPACITY_PATTERN', 'OPER', 'SECURED_RESOURCE'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_DELETE_CAPACITY_PATTERN' and db_type_cd = 'OPER' );

--changeSet DEV-72:10 stripComments:false
INSERT INTO
   db_type_config_parm
   (
      PARM_NAME, DB_TYPE_CD, PARM_TYPE
   )
   SELECT 'ACTION_DUPLICATE_CAPACITY_PATTERN', 'OPER', 'SECURED_RESOURCE'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_DUPLICATE_CAPACITY_PATTERN' and db_type_cd = 'OPER' );

--changeSet DEV-72:11 stripComments:false
INSERT INTO
   db_type_config_parm
   (
      PARM_NAME, DB_TYPE_CD, PARM_TYPE
   )
   SELECT 'ACTION_EDIT_LABOR_SKILLS', 'OPER', 'SECURED_RESOURCE'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_EDIT_LABOR_SKILLS' and db_type_cd = 'OPER' );

--changeSet DEV-72:12 stripComments:false
INSERT INTO
   db_type_config_parm
   (
      PARM_NAME, DB_TYPE_CD, PARM_TYPE
   )
   SELECT 'ACTION_EDIT_SHIFTS_FOR_DAY', 'OPER', 'SECURED_RESOURCE'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_EDIT_SHIFTS_FOR_DAY' and db_type_cd = 'OPER' );

--changeSet DEV-72:13 stripComments:false
INSERT INTO
   db_type_config_parm
   (
      PARM_NAME, DB_TYPE_CD, PARM_TYPE
   )
   SELECT 'ACTION_EDIT_LABOR_PROFILE', 'OPER', 'SECURED_RESOURCE'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_EDIT_LABOR_PROFILE' and db_type_cd = 'OPER' );