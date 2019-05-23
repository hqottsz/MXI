--liquibase formatted sql


--changeSet DEV-185:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table SCHED_STASK_FLAGS add (
	"DO_AT_NEXT_INSTALL_BOOL" Number(1,0) 
)
');
END;
/

--changeSet DEV-185:2 stripComments:false
UPDATE SCHED_STASK_FLAGS SET DO_AT_NEXT_INSTALL_BOOL = 0 WHERE DO_AT_NEXT_INSTALL_BOOL IS NULL;

--changeSet DEV-185:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table SCHED_STASK_FLAGS modify (
	"DO_AT_NEXT_INSTALL_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (DO_AT_NEXT_INSTALL_BOOL IN (0, 1) ) DEFERRABLE
)
');
END;
/