--liquibase formatted sql


--changeSet DEV-2422:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Create new action configuration parameter for Fault retrieval API
****************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_DATE_TIME_ZONE_REQUEST',
      'Permission to allow API to translate timestamps into formatted data time strings.',
      'TRUE/FALSE',
      'FALSE',
      1,
      'API - UTILITY',
      '8.1',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/