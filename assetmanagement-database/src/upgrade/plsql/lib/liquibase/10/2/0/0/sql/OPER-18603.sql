--liquibase formatted sql
--changeSet OPER-18603:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
 utl_migr_data_pkg.config_parm_insert(
 'ALLOW_REQ_CORRECTIVE_ACTIONS',
 'LOGIC',
 'Enables the creation of sub-tasks based on requirement definitions for a fault.',
 'GLOBAL',
 'TRUE/FALSE',
 'FALSE',
 1,
 'Maint - Tasks',
 '8.2-SP5',
 0
); 
END;
/