--liquibase formatted sql


--changeSet PPC-131:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
ALTER TABLE PPC_WP ADD (
  "LAST_LOAD_DT" DATE
)
');
END;
/