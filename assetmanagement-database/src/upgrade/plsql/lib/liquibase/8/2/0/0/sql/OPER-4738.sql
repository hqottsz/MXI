--liquibase formatted sql


--changeSet OPER-4738:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_EVENT_TASK_HIST_NH_DATE');
END;
/

--changeSet OPER-4738:2 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_EVT_EVENT_TYPEHISTNH');
END;
/

--changeSet OPER-4738:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
CREATE INDEX IX_EVT_EVENT_TYPEHISTNH ON evt_event(EVENT_TYPE_DB_ID, EVENT_TYPE_CD, HIST_BOOL, NH_EVENT_DB_ID, EVENT_GDT, SCHED_END_GDT)
');
END;
/