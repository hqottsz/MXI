--liquibase formatted sql


--changeSet DEV-2305:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_PLANNING_ITEM_REQUEST',
      'Permission to allow API planning item retrieval calls',
      'TRUE/FALSE',
      'FALSE',
      1,
      'API - MAINTENANCE',
      '8.1',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/