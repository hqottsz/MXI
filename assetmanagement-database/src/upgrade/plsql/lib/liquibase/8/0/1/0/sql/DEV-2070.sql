--liquibase formatted sql


--changeSet DEV-2070:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
* Create new configuration parameter API_FRAMEWORK_VERIFICATION
**************************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_FRAMEWORK_VERIFICATION',
      'Permission to allow ARC API calls.',
      'TRUE/FALSE',
      'FALSE',
      1,
      'API - UTILITY',
      '8.0-SP2',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/