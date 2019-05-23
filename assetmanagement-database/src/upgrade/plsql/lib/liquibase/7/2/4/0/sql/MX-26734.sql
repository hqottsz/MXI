--liquibase formatted sql


--changeSet MX-26734:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_DATA_PKG.config_parm_insert(
                 'EARLIEST_DEADLINE_TASK_ORIGINATOR_CODES', 
                 'LOGIC',
                 'The list of originator codes that should be considered when getting the earliest deadlines.',
                 'GLOBAL',
                 'ALL/list of originator codes',
                 'ALL',
                 1,
                 'Adapters - Earliest Deadlines',
                 '8.0',
                 0
   );
END;
/