--changeSet OPER28588-002:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- drop column
BEGIN
    utl_migr_schema_pkg.table_column_drop('MT_CORE_FLEET_LIST', 'PREVENT_EXE_BOOL');
    utl_migr_schema_pkg.table_column_drop('MT_CORE_FLEET_LIST', 'PREVENT_EXE_REVIEW_DT');
END;
/