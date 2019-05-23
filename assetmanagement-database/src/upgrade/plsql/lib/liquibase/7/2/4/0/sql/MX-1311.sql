--liquibase formatted sql


--changeSet MX-1311:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table TASK_DEFN modify (
   LAST_REVISION_ORD Number(4,0) NOT NULL DEFERRABLE
)
');


utl_migr_schema_pkg.table_column_modify('
Alter table TASK_DEFN modify (
   NEW_REVISION_BOOL Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (NEW_REVISION_BOOL IN (0, 1) ) DEFERRABLE
)
');

END;
/