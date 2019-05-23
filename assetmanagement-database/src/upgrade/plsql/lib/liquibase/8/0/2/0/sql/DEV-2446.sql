--liquibase formatted sql


--changeSet DEV-2446:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Create new action configuration parameter for Fault retrieval API
****************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_DATA_TYPE_REQUEST',
      'Permission to allow API retrieval call for data types.',
      'TRUE/FALSE',
      'FALSE',
      1,
      'API - MATERIALS',
      '8.1',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/