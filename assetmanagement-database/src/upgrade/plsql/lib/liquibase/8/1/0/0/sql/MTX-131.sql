--liquibase formatted sql


--changeSet MTX-131:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Create new action configuration parameter for Release Control for PPC plans and template API
****************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_PRODUCTION_RELEASE_CONTROL_REQUEST',
      'Permission to allow API release control action call for PPC production plans and templates.',
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