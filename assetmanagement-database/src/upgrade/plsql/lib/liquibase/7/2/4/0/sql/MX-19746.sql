--liquibase formatted sql


--changeSet MX-19746:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table LRP_TASK_DEFN modify (
   MAN_HOURS Number(8,2) NOT NULL DEFERRABLE
)
');
END;
/

--changeSet MX-19746:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table LRP_TASK_DEFN modify (
   ROUTINE_HRS Number(8,2) Default 0
)
');
END;
/

--changeSet MX-19746:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table LRP_TASK_DEFN modify (
   NR_HRS Number(8,2) Default 0
)
');
END;
/

--changeSet MX-19746:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table LRP_TASK_DEFN modify (
   TOTAL_HRS Number(10,2) Default 0
)
');
END;
/

--changeSet MX-19746:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table LRP_EVENT_WORKSCOPE modify (
   MAN_HOURS Number(8,2) NOT NULL DEFERRABLE
)
');
END;
/