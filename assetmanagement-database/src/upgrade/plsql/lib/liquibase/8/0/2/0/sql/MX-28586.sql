--liquibase formatted sql


--changeSet MX-28586:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
   CREATE INDEX IX_EVT_EVENT_HISTTYPESCHEDNH ON EVT_EVENT (HIST_BOOL, EVENT_TYPE_DB_ID, EVENT_TYPE_CD, SCHED_START_GDT, SCHED_END_GDT, NVL(NH_EVENT_DB_ID, -1))
   ');
END;
/