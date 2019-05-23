--liquibase formatted sql


--changeSet MX-27961.6:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  utl_migr_schema_pkg.view_drop('VW_MAINT_PRGM_CARRIER_TASK');
END;
/