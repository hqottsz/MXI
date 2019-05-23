--liquibase formatted sql


--changeSet OPER-453:1 stripComments:false
-- task definition: create cancel on removal
ALTER TRIGGER tubr_task_task DISABLE;

--changeSet OPER-453:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table TASK_TASK add (
"CANCEL_ON_AC_RMVL_BOOL" Number(1,0) Check (CANCEL_ON_AC_RMVL_BOOL IN (0, 1) ) DEFERRABLE
)
');
END;
/

--changeSet OPER-453:3 stripComments:false
UPDATE
   TASK_TASK
SET
   CANCEL_ON_AC_RMVL_BOOL=0
WHERE
   CANCEL_ON_AC_RMVL_BOOL IS NULL;

--changeSet OPER-453:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table TASK_TASK modify (
"CANCEL_ON_AC_RMVL_BOOL" Default 0 NOT NULL DEFERRABLE
)
');
END;
/

--changeSet OPER-453:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table TASK_TASK add (
"CANCEL_ON_ANY_RMVL_BOOL" Number(1,0) Check (CANCEL_ON_ANY_RMVL_BOOL IN (0, 1) ) DEFERRABLE
)
');
END;
/

--changeSet OPER-453:6 stripComments:false
UPDATE
   TASK_TASK
SET
   CANCEL_ON_ANY_RMVL_BOOL=0
WHERE
   CANCEL_ON_ANY_RMVL_BOOL IS NULL;

--changeSet OPER-453:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table TASK_TASK modify (
"CANCEL_ON_ANY_RMVL_BOOL" Default 0 NOT NULL DEFERRABLE
)
');
END;
/

--changeSet OPER-453:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table TASK_TASK add (
"CREATE_ON_AC_RMVL_BOOL" Number(1,0) Check (CREATE_ON_AC_RMVL_BOOL IN (0, 1) ) DEFERRABLE
)
');
END;
/

--changeSet OPER-453:9 stripComments:false
UPDATE
   TASK_TASK
SET
   CREATE_ON_AC_RMVL_BOOL=0
WHERE
   CREATE_ON_AC_RMVL_BOOL IS NULL;

--changeSet OPER-453:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table TASK_TASK modify (
"CREATE_ON_AC_RMVL_BOOL" Default 0 NOT NULL DEFERRABLE
)
');
END;
/

--changeSet OPER-453:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table TASK_TASK add (
"CREATE_ON_ANY_RMVL_BOOL" Number(1,0) Check (CREATE_ON_ANY_RMVL_BOOL IN (0, 1) ) DEFERRABLE
)
');
END;
/

--changeSet OPER-453:12 stripComments:false
UPDATE
   TASK_TASK
SET
   CREATE_ON_ANY_RMVL_BOOL=0
WHERE
   CREATE_ON_ANY_RMVL_BOOL IS NULL;

--changeSet OPER-453:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table TASK_TASK modify (
"CREATE_ON_ANY_RMVL_BOOL" Default 0 NOT NULL DEFERRABLE
)
');
END;
/

--changeSet OPER-453:14 stripComments:false
ALTER TRIGGER tubr_task_task ENABLE;