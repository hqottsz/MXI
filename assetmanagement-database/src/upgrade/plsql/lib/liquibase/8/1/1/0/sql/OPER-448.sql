--liquibase formatted sql


--changeSet OPER-448:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- for using in set hard deadline 
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table TASK_TASK add (
"HARD_DEAD_BOOL" Number(1,0) Check (HARD_DEAD_BOOL IN (0, 1) ) DEFERRABLE
)
');
END;
/

--changeSet OPER-448:2 stripComments:false
UPDATE
   TASK_TASK
SET
   hard_dead_bool=0
WHERE
   hard_dead_bool IS NULL;

--changeSet OPER-448:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table TASK_TASK modify (
"HARD_DEAD_BOOL" Default 0 NOT NULL DEFERRABLE
)
');
END;
/