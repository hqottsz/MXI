--liquibase formatted sql

--changeSet OPER-27164:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Insert new action parameter
BEGIN
      upg_migr_schema_v1_pkg.table_drop('UTL_CONTEXT');
END;
/