--liquibase formatted sql


--changeSet DEV-2435:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Create new action configuration parameter for Work Package API
****************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_WORK_PACKAGE_REQUEST',
      'Permission to allow API retrieval call for work package.',
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