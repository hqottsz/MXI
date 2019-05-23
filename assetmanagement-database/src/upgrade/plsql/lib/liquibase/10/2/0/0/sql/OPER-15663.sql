--liquibase formatted sql

--changeSet OPER-15663:1 stripComments:false
INSERT INTO
  UTL_ACTION_CONFIG_PARM
  (
    PARM_VALUE, PARM_NAME, PARM_DESC, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
  )
  SELECT
    'false', 'ACTION_ADD_CREW_SCHEDULE', 'Permission to add crew schedule','TRUE/FALSE', 'FALSE', 1,'HR - Departments', '8.2-SP5',0
  FROM
    DUAL
  WHERE
    NOT EXISTS ( SELECT 1 FROM UTL_ACTION_CONFIG_PARM WHERE PARM_NAME = 'ACTION_ADD_CREW_SCHEDULE' );
    
    
--changeSet OPER-15663:2 stripComments:false
INSERT INTO
  UTL_ACTION_CONFIG_PARM
  (
    PARM_VALUE, PARM_NAME, PARM_DESC, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
  )
  SELECT
    'TRUE', 'ACTION_JSP_WEB_DEPARTMENT_ADD_SHIFT_PATTERN', 'Permission to access the Add Shift Pattern page.', 'TRUE/FALSE', 'TRUE', 1, 'JSP Permission', '8.2-SP5', 0
  FROM
    DUAL
  WHERE
    NOT EXISTS ( SELECT 1 FROM UTL_ACTION_CONFIG_PARM WHERE PARM_NAME = 'ACTION_JSP_WEB_DEPARTMENT_ADD_SHIFT_PATTERN' );


--changeSet OPER-15663:3 stripComments:false
INSERT INTO
  UTL_ACTION_CONFIG_PARM
  (
    PARM_VALUE, PARM_NAME, PARM_DESC, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
  )
  SELECT
    'false', 'ACTION_REMOVE_CREW_SCHEDULE', 'Permission to remove crew schedule','TRUE/FALSE', 'FALSE', 1,'HR - Departments', '8.2-SP5',0
  FROM
    DUAL
  WHERE
    NOT EXISTS ( SELECT 1 FROM UTL_ACTION_CONFIG_PARM WHERE PARM_NAME = 'ACTION_REMOVE_CREW_SCHEDULE' );


--changeSet OPER-15663:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.sequence_create('ORG_CREW_SCHEDULE_ID_SEQ', 1); 
END;
/

--changeSet OPER-15663:5 stripComments:false
INSERT INTO 
      utl_sequence ( 
         sequence_cd, 
         next_value, 
         table_name, 
         column_name, 
         oracle_seq, 
         utl_id 
      )
   SELECT 
      'ORG_CREW_SCHEDULE_ID_SEQ', 
      1, 
      'ORG_CREW_SCHEDULE', 
      'CREW_SCHEDULE_ID' , 
      1, 
      0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_sequence WHERE sequence_cd = 'ORG_CREW_SCHEDULE_ID_SEQ' );
   

--changeSet OPER-15663:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.sequence_create('ORG_CREW_SHIFT_PLAN_ID_SEQ', 1); 
END;
/

--changeSet OPER-15663:7 stripComments:false
INSERT INTO 
      utl_sequence ( 
         sequence_cd, 
         next_value, 
         table_name, 
         column_name, 
         oracle_seq, 
         utl_id 
      )
   SELECT 
      'ORG_CREW_SHIFT_PLAN_ID_SEQ', 
      1, 
      'ORG_CREW_SHIFT_PLAN', 
      'CREW_SHIFT_PLAN_ID' , 
      1, 
      0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_sequence WHERE sequence_cd = 'ORG_CREW_SHIFT_PLAN_ID_SEQ' );