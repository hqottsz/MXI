--liquibase formatted sql


--changeSet DEV-2209:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_SUPPLYCHAIN',
      'Permission to allow SUPPLY CHAIN API calls.',
      'TRUE/FALSE',
      'FALSE',
      1,
      'API - MATERIALS',
      '8.0-SP2',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/

--changeSet DEV-2209:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_TIMESHEET',
      'Permission to allow TIMESHEET API calls.',
      'TRUE/FALSE',
      'FALSE',
      1,
      'API - MAINTENANCE',
      '8.0-SP2',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/