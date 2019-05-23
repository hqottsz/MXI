--liquibase formatted sql


--changeSet MTX-1460:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Renaming ARC_FAULT.FAIL_TYPE_CD  into ARC_RESULT.EX_FAIL_TYPE_CD column.
BEGIN
  utl_migr_schema_pkg.table_column_rename('ARC_FAULT', 'FAIL_TYPE_CD', 'EX_FAIL_TYPE_CD');
END;
/