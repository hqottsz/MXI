--liquibase formatted sql


--changeSet DEV-1237:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- remove the table, the associated triggers will be dropped automatically
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('EQP_PART_PRICE_BREAK');
END;
/