--liquibase formatted sql


--changeSet DEV-158:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table ORG_HR_SHIFT_PLAN add (
	"START_HOUR" Number(6,2)
)
');
END;
/

--changeSet DEV-158:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table ORG_HR_SHIFT_PLAN add (
	"DURATION_QT" Float
)
');
END;
/

--changeSet DEV-158:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table ORG_HR_SHIFT_PLAN add (
	"WORK_HOURS_QT" Float
)
');
END;
/

--changeSet DEV-158:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table ORG_HR_SHIFT_PLAN add (
	"LOC_DB_ID" Number(10,0) Check (LOC_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet DEV-158:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table ORG_HR_SHIFT_PLAN add (
	"LOC_ID" Number(10,0) Check (LOC_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet DEV-158:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table ORG_HR_SHIFT_PLAN add (
	"CREW_DB_ID" Number(10,0) Check (CREW_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet DEV-158:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table ORG_HR_SHIFT_PLAN add (
	"CREW_ID" Number(10,0) Check (CREW_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet DEV-158:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table ORG_HR_SHIFT_PLAN add (
	"LINE_CAPACITY_BOOL" Number(1,0) 
)
');
END;
/

--changeSet DEV-158:9 stripComments:false
UPDATE ORG_HR_SHIFT_PLAN SET LINE_CAPACITY_BOOL = 0 WHERE LINE_CAPACITY_BOOL IS NULL;

--changeSet DEV-158:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table ORG_HR_SHIFT_PLAN modify (
	"LINE_CAPACITY_BOOL" Number(1,0) Default 0 Check (LINE_CAPACITY_BOOL IN (0, 1) ) DEFERRABLE
)
');
END;
/

--changeSet DEV-158:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ORG_HR_SHIFT_PLAN" add Constraint "FK_INVLOC_ORGHRSHIFTPLAN" foreign key ("LOC_DB_ID","LOC_ID") references "INV_LOC" ("LOC_DB_ID","LOC_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-158:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ORG_HR_SHIFT_PLAN" add Constraint "FK_ORGWORKDEPT_ORGHRSHIFT" foreign key ("CREW_DB_ID","CREW_ID") references "ORG_WORK_DEPT" ("DEPT_DB_ID","DEPT_ID")  DEFERRABLE
');
END;
/