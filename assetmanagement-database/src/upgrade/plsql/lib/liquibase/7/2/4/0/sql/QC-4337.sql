--liquibase formatted sql


--changeSet QC-4337:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table SCHED_KIT_MAP add (
		"INV_COND_DB_ID" Number(10,0) Check (INV_COND_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE 
)
');
END;
/

--changeSet QC-4337:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table SCHED_KIT_MAP add (
"INV_COND_CD" Varchar2 (8)
)
');
END;
/

--changeSet QC-4337:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table SCHED_KIT_MAP add (
	"OWNER_DB_ID" Number(10,0) Check (OWNER_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet QC-4337:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table SCHED_KIT_MAP add (
	"OWNER_ID" Number(10,0) Check (OWNER_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE 
)
');
END;
/

--changeSet QC-4337:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table SCHED_KIT_MAP add (
	"LOC_DB_ID" Number(10,0) Check (LOC_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet QC-4337:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table SCHED_KIT_MAP add (
"LOC_ID" Number(10,0) Check (LOC_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet QC-4337:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "SCHED_KIT_MAP" add Constraint "FK_INVLOC_SCHEDKITMAP" foreign key ("LOC_DB_ID","LOC_ID") references "INV_LOC" ("LOC_DB_ID","LOC_ID")  DEFERRABLE
');
END;
/

--changeSet QC-4337:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "SCHED_KIT_MAP" add Constraint "FK_INVOWNER_SCHEDKITMAP" foreign key ("OWNER_DB_ID","OWNER_ID") references "INV_OWNER" ("OWNER_DB_ID","OWNER_ID")  DEFERRABLE
');
END;
/

--changeSet QC-4337:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "SCHED_KIT_MAP" add Constraint "FK_REFINVCOND_SCHEDKITMAP" foreign key ("INV_COND_DB_ID","INV_COND_CD") references "REF_INV_COND" ("INV_COND_DB_ID","INV_COND_CD")  DEFERRABLE
');
END;
/