--liquibase formatted sql


--changeSet OPER-3047:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add a budget check code column
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table PO_HEADER add (
   BUDGET_CHECK_CD Varchar2 (40)
)
');
END;
/