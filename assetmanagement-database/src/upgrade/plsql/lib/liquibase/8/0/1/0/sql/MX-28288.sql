--liquibase formatted sql


--changeSet MX-28288:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/************************************************************************** 
 * Drop existing index on EVT_INV
 **************************************************************************/ 
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_EVTEVENT_EVTINV');
END;
/

--changeSet MX-28288:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/************************************************************************** 
 * Add new index on EVT_INV
 **************************************************************************/ 
BEGIN
utl_migr_schema_pkg.index_create('
 CREATE INDEX IX_EVTINV_EVTEVENT ON EVT_INV (INV_NO_DB_ID, INV_NO_ID, EVENT_DB_ID, EVENT_ID, MAIN_INV_BOOL)
');
END;
/