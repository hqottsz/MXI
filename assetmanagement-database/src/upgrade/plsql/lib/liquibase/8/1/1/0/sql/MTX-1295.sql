--liquibase formatted sql


--changeSet MTX-1295:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.function_drop('getLabourCapacitySummary');
END;
/

--changeSet MTX-1295:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.function_drop('getPartsCapacitySummary');
END;
/

--changeSet MTX-1295:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.function_drop('getToolsCapacitySummary');
END;
/