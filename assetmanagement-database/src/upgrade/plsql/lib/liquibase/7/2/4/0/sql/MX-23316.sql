--liquibase formatted sql


--changeSet MX-23316:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index "IX_SCHEDSTASK_PLANBYDT" ON "SCHED_STASK" ("PLAN_BY_DT")
   ');
END;
/

--changeSet MX-23316:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index "IX_EVTSCHEDDEAD_DEADDT" ON "EVT_SCHED_DEAD" ("SCHED_DEAD_DT") 
   ');
END;
/