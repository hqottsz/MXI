--liquibase formatted sql
   

--changeSet DEV-248:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table ORG_HR_SHIFT_PLAN add (
	"CREW_LOC_DB_ID" Number(10,0) Check (CREW_LOC_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/   

--changeSet DEV-248:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table ORG_HR_SHIFT_PLAN add (
	"CREW_LOC_ID" Number(10,0) Check (CREW_LOC_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE 
)
');
END;
/   

--changeSet DEV-248:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ORG_HR_SHIFT_PLAN" add Constraint "FK_INVLOC2_ORGHRSHIFTPLAN" foreign key ("CREW_LOC_DB_ID","CREW_LOC_ID") references "INV_LOC" ("LOC_DB_ID","LOC_ID")  DEFERRABLE

');
END;
/