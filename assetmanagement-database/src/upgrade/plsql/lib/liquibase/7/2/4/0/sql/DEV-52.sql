--liquibase formatted sql
 

--changeSet DEV-52:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
  utl_migr_schema_pkg.table_column_modify('
  Alter table TASK_TASK modify (
     "TASK_MUST_REMOVE_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (TASK_MUST_REMOVE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
  )
  ');
 END;
 /  

--changeSet DEV-52:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
  utl_migr_schema_pkg.table_column_modify('
  Alter table TASK_TASK modify (
     "TASK_MUST_REMOVE_CD" Varchar2 (16) NOT NULL DEFERRABLE
  )
  ');
 END;
 /  