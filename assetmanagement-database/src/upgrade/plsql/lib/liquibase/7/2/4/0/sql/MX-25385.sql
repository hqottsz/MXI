--liquibase formatted sql


--changeSet MX-25385:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_EVENT_TASK_HIST_NH_DATE');
END;
/

--changeSet MX-25385:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
CREATE INDEX ix_event_task_hist_nh_date ON evt_event(event_type_db_id, event_type_cd, hist_bool, NVL(nh_event_db_id,(-1)), NVL(event_gdt,sched_end_gdt))
');
END;
/