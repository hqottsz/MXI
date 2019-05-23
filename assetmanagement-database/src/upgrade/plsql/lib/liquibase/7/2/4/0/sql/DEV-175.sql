--liquibase formatted sql


--changeSet DEV-175:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_NN_DROP('ER_WEEKLY_RANGE', 'MAX_AIRCRAFT');
END;
/