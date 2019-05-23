--liquibase formatted sql


--changeSet DEV-92:1 stripComments:false
-- Migrations changes to the EQP_PLANNING_TYPE and EQP_PLANNING_TYPE_SKILL tables moved into RND-2222.sql
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   SELECT  'PLANNING_TYPE_SEQ', 100000, 'EQP_PLANNING_TYPE', 'PLANNING_TYPE_ID', 1 ,0
   FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_SEQUENCE WHERE SEQUENCE_CD = 'PLANNING_TYPE_SEQ');

--changeSet DEV-92:2 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.SEQUENCE_CREATE('PLANNING_TYPE_SEQ', 1);
END;
/

--changeSet DEV-92:3 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('TASK_TASK', 'FK_EQPPLANNINGTYPE_TASKTASK');
END;
/

--changeSet DEV-92:4 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('TASK_TASK', 'FK_REFPLANNINGTYPE_TASKTASK');  
END;
/

--changeSet DEV-92:5 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('TASK_TASK', 'EQP_PLANNING_TYPE_DB_ID');
END;
/

--changeSet DEV-92:6 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('TASK_TASK', 'EQP_PLANNING_TYPE_CD');
END;
/

--changeSet DEV-92:7 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('TASK_TASK', 'PLANNING_TYPE_CD');
END;
/

--changeSet DEV-92:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table TASK_TASK add (
	"PLANNING_TYPE_ID" Number(10,0) Check (PLANNING_TYPE_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE 
)
');
END;
/

--changeSet DEV-92:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  utl_migr_schema_pkg.table_column_modify('
  Alter table TASK_TASK modify (
    "PLANNING_TYPE_DB_ID" Number(10,0) Check (PLANNING_TYPE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
  )
  ');
 END;
 /

--changeSet DEV-92:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  utl_migr_schema_pkg.table_column_modify('
  Alter table TASK_TASK modify (
    "PLANNING_TYPE_ID" Number(10,0) Check (PLANNING_TYPE_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
  )
  ');
 END;
 /

--changeSet DEV-92:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "TASK_TASK" add Constraint "FK_EQPPLANNINGTYPE_TASKTASK" foreign key ("PLANNING_TYPE_DB_ID","PLANNING_TYPE_ID") references "EQP_PLANNING_TYPE" ("PLANNING_TYPE_DB_ID","PLANNING_TYPE_ID")  DEFERRABLE
');
END;
/