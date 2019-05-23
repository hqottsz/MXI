--liquibase formatted sql


--changeSet MX-21974.3:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- drop deprecated table
BEGIN
   utl_migr_schema_pkg.table_drop('MAINT_PRGM_TEMP_TASK');
END;
/