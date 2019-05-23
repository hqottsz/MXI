--liquibase formatted sql


--changeSet OPER-2025:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Create new action configuration parameter for accessing Print Job Cards functionality
****************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'ACTION_PPC_PRINT_JOB_CARDS',
      'Permission to allow Print Job Cards action from File menu.',
      'TRUE/FALSE',
      'FALSE',
      1,
      'Maint - Production Planning and Control',
      '8.1-SP3',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/