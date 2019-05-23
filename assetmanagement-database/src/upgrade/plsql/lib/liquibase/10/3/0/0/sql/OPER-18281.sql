--liquibase formatted sql

--changeSet OPER-18281:01 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT DECODE(data_type, 'VARCHAR2', 1, 0) FROM user_tab_columns WHERE table_name = 'TASK_STEP' AND column_name = 'STEP_LDESC';
BEGIN
   upg_migr_schema_v1_pkg.table_column_rename('TASK_STEP', 'STEP_LDESC', 'STEP_LDESC_TMP');
END;
/

--changeSet OPER-18281:02 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM user_tab_columns WHERE table_name = 'TASK_STEP' AND column_name = 'STEP_LDESC_TMP';
BEGIN
   upg_migr_schema_v1_pkg.table_column_add('
      Alter table TASK_STEP add (
         STEP_LDESC CLOB
      )
   ');
END;
/

--changeSet OPER-18281:03 stripComments:false
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM user_tab_columns WHERE table_name = 'TASK_STEP' AND column_name = 'STEP_LDESC_TMP';
UPDATE task_step SET step_ldesc = step_ldesc_tmp;

--changeSet OPER-18281:04 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM user_tab_columns WHERE table_name = 'TASK_STEP' AND column_name = 'STEP_LDESC_TMP';
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE task_step MODIFY step_ldesc NOT NULL
  ');
END;
/

--changeSet OPER-18281:05 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_column_drop('TASK_STEP','STEP_LDESC_TMP');
END;
/

