--liquibase formatted sql


--changeSet DEV-1016:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Schema Changes --		
--
-- Add new column to SCHED_LABOUR
--
BEGIN
utl_migr_schema_pkg.table_column_add('
alter table SCHED_LABOUR add EXT_KEY_SDESC varchar2(80)
');
END;
/