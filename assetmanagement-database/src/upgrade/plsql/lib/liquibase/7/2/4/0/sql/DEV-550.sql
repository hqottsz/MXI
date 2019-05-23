--liquibase formatted sql


--changeSet DEV-550:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table ORG_WORK_DEPT add (
   "LABOUR_SKILL_DB_ID" Number(10,0) Check (LABOUR_SKILL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet DEV-550:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table ORG_WORK_DEPT add (
   "LABOUR_SKILL_CD" Varchar2 (8)
)
');
END;
/

--changeSet DEV-550:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ORG_WORK_DEPT" add Constraint "FK_REFLBRSKIL_ORGWRKDPT" foreign key ("LABOUR_SKILL_DB_ID","LABOUR_SKILL_CD") references "REF_LABOUR_SKILL" ("LABOUR_SKILL_DB_ID","LABOUR_SKILL_CD")  DEFERRABLE
');
END;
/