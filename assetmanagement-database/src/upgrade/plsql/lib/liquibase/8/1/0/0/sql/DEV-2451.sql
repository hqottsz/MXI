--liquibase formatted sql


--changeSet DEV-2451:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Create new action configuration parameter for Operator retrieval API
****************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_OPERATOR_REQUEST',
      'Permission to allow API operator retrieval calls',
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