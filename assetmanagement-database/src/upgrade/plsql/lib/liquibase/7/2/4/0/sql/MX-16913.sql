--liquibase formatted sql


--changeSet MX-16913:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Remove the security parameter BASELINESYNCHSWITCH_TASK_SCHEDULE
BEGIN
   utl_migr_data_pkg.config_parm_delete('BASELINESYNCHSWITCH_TASK_SCHEDULE');
END;
/