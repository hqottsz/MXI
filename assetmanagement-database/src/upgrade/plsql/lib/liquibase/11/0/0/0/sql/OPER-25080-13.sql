--liquibase formatted sql
--changeSet OPER-25080:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  upg_migr_schema_v1_pkg.package_drop('CONTEXT_PACKAGE');
END;
/

--changeSet OPER-25080:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  upg_migr_schema_v1_pkg.view_drop('VW_INV_CONTEXT');
END;
/
