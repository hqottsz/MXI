--liquibase formatted sql


--changeSet MTX-651:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- ASSMBL_DB_ID column
/***************************************************************
* Add new 4 columns in USG_USAGE_DATA table
* to hold the inventory bom item position key
****************************************************************/
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table USG_USAGE_DATA add (
   ASSMBL_DB_ID Number(10,0) Check (ASSMBL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/ 

--changeSet MTX-651:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- ASSMBL_CD column
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table USG_USAGE_DATA add (
   ASSMBL_CD Varchar2 (8)
)
');
END;
/ 

--changeSet MTX-651:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- ASSMBL_BOM_ID column
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table USG_USAGE_DATA add (
   ASSMBL_BOM_ID Number(10,0) Check (ASSMBL_BOM_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/ 

--changeSet MTX-651:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- ASSMBL_POS_ID column
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table USG_USAGE_DATA add (
   ASSMBL_POS_ID Number(10,0) Check (ASSMBL_POS_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE  
)
');
END;
/ 

--changeSet MTX-651:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Index
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSMBLPOS_USGUSAGEDATA" ON "USG_USAGE_DATA" ("ASSMBL_DB_ID","ASSMBL_CD","ASSMBL_BOM_ID","ASSMBL_POS_ID")
');
END;
/

--changeSet MTX-651:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- FK constraint
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "USG_USAGE_DATA" add Constraint "FK_EQPASSMBLPOS_USGUSAGEDATA" foreign key ("ASSMBL_DB_ID","ASSMBL_CD","ASSMBL_BOM_ID","ASSMBL_POS_ID") references "EQP_ASSMBL_POS" ("ASSMBL_DB_ID","ASSMBL_CD","ASSMBL_BOM_ID","ASSMBL_POS_ID")  DEFERRABLE
');
END;
/