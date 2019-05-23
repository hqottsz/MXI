--liquibase formatted sql


--changeSet DEV-81:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table TASK_TASK add (
	"ENFORCE_WORKSCOPE_BOOL" Number(1,0) 
)
');
END;
/

--changeSet DEV-81:2 stripComments:false
UPDATE TASK_TASK SET ENFORCE_WORKSCOPE_BOOL = 0 WHERE ENFORCE_WORKSCOPE_BOOL IS NULL;

--changeSet DEV-81:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table TASK_TASK modify (
	"ENFORCE_WORKSCOPE_BOOL" Number(1,0) Default 0 Check (ENFORCE_WORKSCOPE_BOOL IN (0, 1) ) DEFERRABLE
)
');
END;
/

--changeSet DEV-81:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table SCHED_WO_LINE add (
	"WORKSCOPE_ORDER" Number Default NULL
)
');
END;
/

--changeSet DEV-81:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table SCHED_WP add (
	"ENFORCE_WORKSCOPE_BOOL" Number(1,0) 
)
');
END;
/

--changeSet DEV-81:6 stripComments:false
UPDATE SCHED_WP SET ENFORCE_WORKSCOPE_BOOL = 0 WHERE ENFORCE_WORKSCOPE_BOOL IS NULL;

--changeSet DEV-81:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table SCHED_WP modify (
	"ENFORCE_WORKSCOPE_BOOL" Number(1,0) Default 0 Check (ENFORCE_WORKSCOPE_BOOL IN (0, 1) ) DEFERRABLE 
)
');
END;
/

--changeSet DEV-81:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table SCHED_WP add (
	"DELAY_BOOL" Number(1,0) 
)
');
END;
/

--changeSet DEV-81:9 stripComments:false
UPDATE SCHED_WP SET DELAY_BOOL = 0 WHERE DELAY_BOOL IS NULL;

--changeSet DEV-81:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table SCHED_WP modify (
	"DELAY_BOOL" Number(1,0) Default 0 Check (DELAY_BOOL IN (0, 1) ) DEFERRABLE
)
');
END;
/