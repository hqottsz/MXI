--liquibase formatted sql


--changeSet DEV-90:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('LRP_PLAN', 'ACTIVE_BOOL');
END;
/