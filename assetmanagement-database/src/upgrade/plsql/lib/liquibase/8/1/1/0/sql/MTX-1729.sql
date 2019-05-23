--liquibase formatted sql


--changeSet MTX-1729:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_drop('UTL_HELP');
END;
/