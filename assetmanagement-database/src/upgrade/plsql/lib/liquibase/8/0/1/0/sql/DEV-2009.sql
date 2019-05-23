--liquibase formatted sql


--changeSet DEV-2009:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- migration script for CLOSED_BOOL column added in FNC_ACCOUNT table
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table FNC_ACCOUNT add (
	"CLOSED_BOOL" Number(1,0) Check (CLOSED_BOOL IN (0, 1) ) DEFERRABLE
)
');
END;
/

--changeSet DEV-2009:2 stripComments:false
ALTER TRIGGER TUBR_FNC_ACCOUNT DISABLE;

--changeSet DEV-2009:3 stripComments:false
UPDATE FNC_ACCOUNT SET closed_bool = 0 WHERE closed_bool IS NULL;

--changeSet DEV-2009:4 stripComments:false
ALTER TRIGGER TUBR_FNC_ACCOUNT ENABLE;

--changeSet DEV-2009:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table FNC_ACCOUNT modify (
	"CLOSED_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (CLOSED_BOOL IN (0, 1) ) DEFERRABLE
)
');
END;
/