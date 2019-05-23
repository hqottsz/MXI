--liquibase formatted sql


--changeSet MX-12821:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_drop( 'TASK_TASK', 'PKG_NAME' );
END;
/

--changeSet MX-12821:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_drop( 'TASK_TASK', 'PKG_ORD' );
END;
/

--changeSet MX-12821:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_drop( 'TASK_TASK', 'LR_FORECAST_BOOL' );
END;
/