--liquibase formatted sql


--changeSet MTX-489:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Create new action configuration parameter for accessing production plan delete functionality
****************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'ACTION_PRODUCTION_PLAN_DELETE_PLAN_TEMPLATE',
      'Permission to access the Production Planning and Control delete plan/template functionality',
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

--changeSet MTX-489:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Create new action configuration parameter for DELETE for PPC plans and template API
****************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_PRODUCTION_DELETE_PLAN_TEMPLATE_REQUEST',
      'Permission to allow API delete action call for PPC production plans and templates.',
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