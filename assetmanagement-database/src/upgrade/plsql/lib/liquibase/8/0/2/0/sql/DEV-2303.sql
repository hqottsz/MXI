--liquibase formatted sql


--changeSet DEV-2303:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Create new action configuration parameter for Location and Vendor API
****************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_LOCATION_REQUEST',
      'Permission to allow API retrieval call for location.',
      'TRUE/FALSE',
      'FALSE',
      1,
      'API - ENTERPRISE',
      '8.1',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/

--changeSet DEV-2303:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_VENDOR_REQUEST',
      'Permission to allow API retrieval call for vendor.',
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