--liquibase formatted sql


--changeSet MTX-324:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Create new action configuration parameter for accessing production plan delete baseline functionality
****************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'ACTION_PRODUCTION_PLAN_DELETE_BASELINE',
      'Permission to access/see the Production Planning and Control delete baseline button',
      'TRUE/FALSE',
      'FALSE',
      1,
      'Maint - Production Planning and Control',
      '8.1-SP1',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/

--changeSet MTX-324:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_PRODUCTION_DELETE_BASELINE_REQUEST',
      'Permission allowing API to delete baselines for PPC production plans',
      'TRUE/FALSE',
      'FALSE',
      1,
      'API - MAINTENANCE',
      '8.1-SP1',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/

--changeSet MTX-324:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_PRODUCTION_BASELINE_REQUEST',
      'Permission allowing API to retrieve PPC baselines',
      'TRUE/FALSE',
      'FALSE',
      1,
      'API - MAINTENANCE',
      '8.1-SP1',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/