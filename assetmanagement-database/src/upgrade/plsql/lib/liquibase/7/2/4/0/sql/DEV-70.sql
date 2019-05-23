--liquibase formatted sql


--changeSet DEV-70:1 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'ACTION_CREATE_SHIFTS', 'true', 'SECURED_RESOURCE', 0, 'Determines whether a user is allowed to create shift.' , 'USER', 'TRUE/FALSE', 'TRUE', 1, 'Shift - Subtypes', '7.5', 0 
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_CREATE_SHIFTS' );  

--changeSet DEV-70:2 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'ACTION_EDIT_SHIFT', 'true', 'SECURED_RESOURCE', 0, 'Determines whether a user is allowed to edit shift.' , 'USER', 'TRUE/FALSE', 'TRUE', 1, 'Shift - Subtypes', '7.5', 0 
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_EDIT_SHIFT' );  

--changeSet DEV-70:3 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'ACTION_DELETE_SHIFTS', 'true', 'SECURED_RESOURCE', 0, 'Determines whether a user is allowed to delete shifts.' , 'USER', 'TRUE/FALSE', 'TRUE', 1, 'Shift - Subtypes', '7.5', 0 
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_DELETE_SHIFTS' );  

--changeSet DEV-70:4 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'ACTION_CREATE_USER_SHIFT_PATTERN', 'true', 'SECURED_RESOURCE', 0, 'Determines whether a user is allowed to create user shift pattern.' , 'USER', 'TRUE/FALSE', 'TRUE', 1, 'Shift - Subtypes', '7.5', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_CREATE_USER_SHIFT_PATTERN' );  

--changeSet DEV-70:5 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'ACTION_EDIT_USER_SHIFT_PATTERN', 'true', 'SECURED_RESOURCE', 0, 'Determines whether a user is allowed to edit user shift pattern.' , 'USER', 'TRUE/FALSE', 'TRUE', 1, 'Shift - Subtypes', '7.5', 0 
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_EDIT_USER_SHIFT_PATTERN' );  

--changeSet DEV-70:6 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'ACTION_DELETE_USER_SHIFT_PATTERNS', 'true', 'SECURED_RESOURCE', 0, 'Determines whether a user is allowed to delete user shift patterns.' , 'USER', 'TRUE/FALSE', 'TRUE', 1, 'Shift - Subtypes', '7.5', 0 
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_DELETE_USER_SHIFT_PATTERNS' );  

--changeSet DEV-70:7 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'ACTION_EDIT_USER_SHIFT_PATTER_DAY_SHIFT', 'true', 'SECURED_RESOURCE', 0, 'Determines whether the user is allowed to edit user shift pattern day shifts.' , 'USER', 'TRUE/FALSE', 'TRUE', 1, 'Shift - Subtypes', '7.5', 0 
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_EDIT_USER_SHIFT_PATTER_DAY_SHIFT' );  

--changeSet DEV-70:8 stripComments:false
INSERT INTO utl_todo_tab (TODO_TAB_ID,TODO_TAB_CD,TODO_TAB_NAME,PATH,TODO_TAB_LDESC,UTL_ID)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
SELECT 11023, 'idTabShiftSetup', 'web.todotab.SHIFT_SETUP', '/web/todolist/ShiftSetupTab.jsp', 'web.todotab.SHIFT_SETUP_TAB', 0
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM utl_todo_tab WHERE TODO_TAB_ID = 11023);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      

--changeSet DEV-70:9 stripComments:false
INSERT INTO utl_todo_tab (TODO_TAB_ID,TODO_TAB_CD,TODO_TAB_NAME,PATH,TODO_TAB_LDESC,UTL_ID)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
SELECT 11024, 'idTabUserShiftPatternSetup', 'web.todotab.USER_SHIFT_PATTERN_SETUP', '/web/todolist/UserShiftPatternSetupTab.jsp', 'web.todotab.USER_SHIFT_PATTERN_SETUP_TAB', 0
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM utl_todo_tab WHERE TODO_TAB_ID = 11024);  

--changeSet DEV-70:10 stripComments:false
INSERT INTO
   db_type_config_parm
   (
      PARM_NAME,  PARM_TYPE, DB_TYPE_CD
   )
   SELECT 'ACTION_CREATE_SHIFTS', 'SECURED_RESOURCE','MASTER'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_CREATE_SHIFTS' and db_type_cd = 'MASTER' ); 

--changeSet DEV-70:11 stripComments:false
 INSERT INTO
    db_type_config_parm
    (
       PARM_NAME,  PARM_TYPE, DB_TYPE_CD
    )
    SELECT 'ACTION_CREATE_SHIFTS', 'SECURED_RESOURCE','OPER'
    FROM
       dual
    WHERE
       NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_CREATE_SHIFTS' and db_type_cd = 'OPER' );  

--changeSet DEV-70:12 stripComments:false
INSERT INTO
   db_type_config_parm
   (
      PARM_NAME,  PARM_TYPE, DB_TYPE_CD
   )
   SELECT 'ACTION_EDIT_SHIFT', 'SECURED_RESOURCE','MASTER'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_EDIT_SHIFT' and db_type_cd = 'MASTER' ); 

--changeSet DEV-70:13 stripComments:false
INSERT INTO
   db_type_config_parm
   (
      PARM_NAME,  PARM_TYPE, DB_TYPE_CD
   )
   SELECT 'ACTION_EDIT_SHIFT', 'SECURED_RESOURCE','OPER'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_EDIT_SHIFT' and db_type_cd = 'OPER' ); 

--changeSet DEV-70:14 stripComments:false
INSERT INTO
   db_type_config_parm
   (
      PARM_NAME,  PARM_TYPE, DB_TYPE_CD
   )
   SELECT 'ACTION_DELETE_SHIFTS', 'SECURED_RESOURCE','MASTER'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_DELETE_SHIFTS' and db_type_cd = 'MASTER' ); 

--changeSet DEV-70:15 stripComments:false
 INSERT INTO
    db_type_config_parm
    (
       PARM_NAME,  PARM_TYPE, DB_TYPE_CD
    )
    SELECT 'ACTION_DELETE_SHIFTS', 'SECURED_RESOURCE','OPER'
    FROM
       dual
    WHERE
       NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_DELETE_SHIFTS' and db_type_cd = 'OPER' );  

--changeSet DEV-70:16 stripComments:false
INSERT INTO
   db_type_config_parm
   (
      PARM_NAME,  PARM_TYPE, DB_TYPE_CD
   )
   SELECT 'ACTION_CREATE_USER_SHIFT_PATTERN', 'SECURED_RESOURCE','MASTER'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_CREATE_USER_SHIFT_PATTERN' and db_type_cd = 'MASTER' ); 

--changeSet DEV-70:17 stripComments:false
INSERT INTO
   db_type_config_parm
   (
      PARM_NAME,  PARM_TYPE, DB_TYPE_CD
   )
   SELECT 'ACTION_CREATE_USER_SHIFT_PATTERN', 'SECURED_RESOURCE','OPER'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_CREATE_USER_SHIFT_PATTERN' and db_type_cd = 'OPER' ); 

--changeSet DEV-70:18 stripComments:false
INSERT INTO
   db_type_config_parm
   (
      PARM_NAME,  PARM_TYPE, DB_TYPE_CD
   )
   SELECT 'ACTION_EDIT_USER_SHIFT_PATTERN', 'SECURED_RESOURCE','MASTER'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_EDIT_USER_SHIFT_PATTERN' and db_type_cd = 'MASTER' ); 

--changeSet DEV-70:19 stripComments:false
 INSERT INTO
    db_type_config_parm
    (
       PARM_NAME,  PARM_TYPE, DB_TYPE_CD
    )
    SELECT 'ACTION_EDIT_USER_SHIFT_PATTERN', 'SECURED_RESOURCE','OPER'
    FROM
       dual
    WHERE
       NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_EDIT_USER_SHIFT_PATTERN' and db_type_cd = 'OPER' );  

--changeSet DEV-70:20 stripComments:false
INSERT INTO
   db_type_config_parm
   (
      PARM_NAME,  PARM_TYPE, DB_TYPE_CD
   )
   SELECT 'ACTION_DELETE_USER_SHIFT_PATTERNS', 'SECURED_RESOURCE','MASTER'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_DELETE_USER_SHIFT_PATTERNS' and db_type_cd = 'MASTER' ); 

--changeSet DEV-70:21 stripComments:false
INSERT INTO
   db_type_config_parm
   (
      PARM_NAME,  PARM_TYPE, DB_TYPE_CD
   )
   SELECT 'ACTION_DELETE_USER_SHIFT_PATTERNS', 'SECURED_RESOURCE','OPER'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_DELETE_USER_SHIFT_PATTERNS' and db_type_cd = 'OPER' ); 

--changeSet DEV-70:22 stripComments:false
 INSERT INTO
    db_type_config_parm
    (
       PARM_NAME,  PARM_TYPE, DB_TYPE_CD
    )
    SELECT 'ACTION_EDIT_USER_SHIFT_PATTER_DAY_SHIFT', 'SECURED_RESOURCE','MASTER'
    FROM
       dual
    WHERE
       NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_EDIT_USER_SHIFT_PATTER_DAY_SHIFT' and db_type_cd = 'MASTER' );  

--changeSet DEV-70:23 stripComments:false
 INSERT INTO
    db_type_config_parm
    (
       PARM_NAME,  PARM_TYPE, DB_TYPE_CD
    )
    SELECT 'ACTION_EDIT_USER_SHIFT_PATTER_DAY_SHIFT', 'SECURED_RESOURCE','OPER'
    FROM
       dual
    WHERE
       NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_EDIT_USER_SHIFT_PATTER_DAY_SHIFT' and db_type_cd = 'OPER' );      