--liquibase formatted sql


--changeSet GATHER_SCHEMA_STATS:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace procedure GATHER_SCHEMA_STATS
   IS
BEGIN
      dbms_stats.gather_schema_stats (ownname => NULL, cascade => TRUE, method_opt => 'for all columns size 1', estimate_percent => 100, no_invalidate=> false);
END GATHER_SCHEMA_STATS;
/