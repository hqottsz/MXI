--liquibase formatted sql


--changeSet MTX-134:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Create new action configuration parameter for accessing production plan release control functionality
****************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'ACTION_PRODUCTION_PLAN_RELEASE_CONTROL',
      'Permission to access the Production Planning and Control release control functionality',
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