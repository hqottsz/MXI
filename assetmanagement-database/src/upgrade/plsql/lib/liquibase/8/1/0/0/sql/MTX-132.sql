--liquibase formatted sql


--changeSet MTX-132:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Create new action configuration parameter for Get Production Plan API
****************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_PRODUCTION_PLAN_REQUEST',
      'Permission to allow API retrieval call for PPC production plans.',
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