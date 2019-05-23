--liquibase formatted sql


--changeSet OPER-3052:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add a budget check ref column
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PO_AUTH', 'BUDGET_CHECK_REF');
END;
/