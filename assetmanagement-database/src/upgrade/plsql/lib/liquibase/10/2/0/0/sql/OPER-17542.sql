--liquibase formatted sql

--changeSet OPER-17542:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Add action parameter ACTION_RESOLUTION_CONFIG_SLOT (If missing) 
BEGIN
       utl_migr_data_pkg.action_parm_insert(
               'ACTION_RESOLUTION_CONFIG_SLOT',
               'Permission to use Resolution Config Slots.',
               'TRUE/FALSE',
               'FALSE',
               1,
               'Maint - Faults',
               '8.2-SP5',
               0,
               0,
               UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
        );
END;
/
