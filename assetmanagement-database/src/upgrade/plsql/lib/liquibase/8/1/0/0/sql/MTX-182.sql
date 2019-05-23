--liquibase formatted sql


--changeSet MTX-182:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Create new action configuration parameter for user account request API
****************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_USER_ACCOUNT_REQUEST',
      'Permission to allow API user account request.',
      'TRUE/FALSE',
      'FALSE',
      1,
      'API - ENTERPRISE',
      '8.1-SP1',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/