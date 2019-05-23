--liquibase formatted sql


--changeSet MX-21182:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_MAIN_INV_EVT_EVTINV" ON "EVT_INV" ("EVENT_DB_ID","EVENT_ID","INV_NO_DB_ID","INV_NO_ID","MAIN_INV_BOOL")
');
END;
/