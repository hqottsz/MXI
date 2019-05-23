--liquibase formatted sql


--changeSet MTX-487:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Before renaming the column remove the not null contriant
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_NN_DROP('ARC_RESULT', 'CREATION_DT');
END;
/

--changeSet MTX-487:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Renaming ARC_RESULT.CREATION_DT into ARC_RESULT.RESULT_DT column.
BEGIN
  utl_migr_schema_pkg.table_column_rename('ARC_RESULT', 'CREATION_DT', 'RESULT_DT');
END;
/