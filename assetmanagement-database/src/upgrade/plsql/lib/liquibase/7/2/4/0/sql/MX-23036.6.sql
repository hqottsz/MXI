--liquibase formatted sql


--changeSet MX-23036.6:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      utl_migr_data_pkg.config_parm_insert(
               'SUBCOMPONENTS_THRESHOLD_ON_INVENTORY_TASKS',
               'MXWEB',
               'When editing tasks on the edit inventory tasks page, the number of subcomponents can cause performance issues. When the number of subcomponents exceeds this threshold, the user will be warned of the performance impact.',
               'GLOBAL',
               'NUMBER',
               '1000',
               1,
               'MXWEB',
               '8.0',
               0,
               utl_migr_data_pkg.DbTypeCdList('MASTER')
      );
END;
/