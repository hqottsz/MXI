--liquibase formatted sql


--changeSet MX-24838.5:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
CREATE INDEX "IX_EVT_EVENT_HISTBOOL" ON "EVT_EVENT" ("HIST_BOOL") 
');
END;
/